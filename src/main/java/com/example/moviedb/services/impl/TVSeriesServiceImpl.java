package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.*;
import com.example.moviedb.models.entity.*;
import com.example.moviedb.repositories.*;
import com.example.moviedb.services.ActorService;
import com.example.moviedb.services.CategoryService;
import com.example.moviedb.services.DirectorService;
import com.example.moviedb.services.TVSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TVSeriesServiceImpl implements TVSeriesService {
    private final TVSeriesRepository tvSeriesRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final RatingSeriesFromUserRepository ratingSeriesFromUserRepository;
    private final WatchlistSeriesRepository watchlistSeriesRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public TVSeriesServiceImpl(TVSeriesRepository tvSeriesRepository, DirectorRepository directorRepository, ActorRepository actorRepository, ActorService actorService, DirectorService directorService, CategoryService categoryService, CategoryRepository categoryRepository, RatingSeriesFromUserRepository ratingSeriesFromUserRepository, WatchlistSeriesRepository watchlistSeriesRepository) {
        this.tvSeriesRepository = tvSeriesRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.ratingSeriesFromUserRepository = ratingSeriesFromUserRepository;
        this.watchlistSeriesRepository = watchlistSeriesRepository;
    }

    @Override
    public List<TVSeriesDTO> getTVSeries() {
        return this.tvSeriesRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TVSeries> findByName(String name) {
        return tvSeriesRepository.findByTitle(name);
    }

    @Override
    public List<TVSeriesDTO> getTopRatedSeriesLimited() {
        List<TVSeries> allSeries = tvSeriesRepository.findAll();

        List<TVSeries> topRatedSeries = allSeries.stream()
                .filter(s -> s.getRating() >= 8.0).toList();

        return topRatedSeries.stream()
                .map(this::convertToDto)
                .limit(6)
                .collect(Collectors.toList());
    }

    @Override
    public TVSeriesDTO getSeriesById(Long id) {
        Optional<TVSeries> seriesOptional = tvSeriesRepository.findById(id);

        if (seriesOptional.isPresent()) {
            TVSeries series = seriesOptional.get();
            return convertToDto(series);
        } else {
            throw new NoSuchElementException("Series not found with id: " + id);
        }
    }

    @Override
    public List<TVSeriesDTO> searchSeriesByTitleOrActorOrDirectorOrCategory(String query) {
        List<TVSeries> series = tvSeriesRepository.findByTitleContainingIgnoreCaseOrActors_ActorNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCaseOrDirector_DirectorNameContainingIgnoreCase(query, query, query, query);
        return series.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TVSeriesDTO> searchSeriesIgnoreCase(String query) {
        return this.searchSeriesByTitleOrActorOrDirectorOrCategory(query).stream()
                .filter(s -> s.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public int getUserRatingForSeries(Long movieId, Long userId) {
        SeriesRatingFromUser rating = ratingSeriesFromUserRepository.findByTvSeriesIdAndUserId(movieId, userId);
        return rating != null ? rating.getRating() : 0;
    }

    @Override
    public double getAverageRatingForSeries(Long movieId) {
        List<SeriesRatingFromUser> ratings = ratingSeriesFromUserRepository.findByTvSeriesId(movieId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (SeriesRatingFromUser rating : ratings) {
            totalRating += rating.getRating();
        }
        return  totalRating * 1.0 / ratings.size();
    }

    @Override
    public void rateSeries(Long seriesId, int rating, User user) {
        TVSeries tvSeries = tvSeriesRepository.findById(seriesId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid series Id:" + seriesId));

        SeriesRatingFromUser existingRating = ratingSeriesFromUserRepository.findByTvSeriesAndUser(tvSeries, user);

        if (existingRating != null) {
            existingRating.setRating(rating);
            ratingSeriesFromUserRepository.save(existingRating);
        } else {
            SeriesRatingFromUser newSeriesRating = new SeriesRatingFromUser();
            newSeriesRating.setTvSeries(tvSeries);
            newSeriesRating.setUser(user);
            newSeriesRating.setRating(rating);
            ratingSeriesFromUserRepository.save(newSeriesRating);
        }
    }

    @Override
    public List<TVSeries> getUserRatings(User user) {
        List<SeriesRatingFromUser> userRatings = ratingSeriesFromUserRepository.findByUser(user);
        List<TVSeries> ratedSeries = new ArrayList<>();

        for (SeriesRatingFromUser rating : userRatings) {
            TVSeries series = rating.getTvSeries();
            series.setRating(rating.getRating());
            ratedSeries.add(series);
        }

        return ratedSeries;
    }
    @Override
    public void removeSeriesRating(Long seriesId, User user) {
        TVSeries tvSeries = tvSeriesRepository.findById(seriesId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid series Id:" + seriesId));

        SeriesRatingFromUser ratingFromUser = ratingSeriesFromUserRepository.findByTvSeriesAndUser(tvSeries, user);

        if (ratingFromUser != null) {
            ratingSeriesFromUserRepository.delete(ratingFromUser);
        } else {
            throw new IllegalArgumentException("Rating not found for movieId: " + seriesId + " and userId: " + user.getId());
        }
    }
    @Override
    public TVSeriesDTO convertToDto(TVSeries series){
        return new TVSeriesDTO(
                series.getId(),
                series.getTitle(),
                series.getReleaseDate(),
                series.getSeasons(),
                series.getRating(),
                series.getImageURL(),
                series.getVideoURL(),
                series.getDescription(),
                series.getCategory().stream().map(categoryService::convertToDto).collect(Collectors.toSet()),
                series.getActors().stream().map(actorService::convertToDto).collect(Collectors.toSet()),
                directorService.convertToDto(series.getDirector()));
    }

    @Override
    public List<TVSeries> getAllWatchlistSeriesForUser(Long userId) {
        List<WatchlistSeries> watchlistSeries = watchlistSeriesRepository.findAllByUserId(userId);

        List<TVSeries> series = new ArrayList<>();

        for (WatchlistSeries list : watchlistSeries) {
            series.add(list.getTvSeries());
        }
        return series;
    }

    @Override
    public void addToSeriesWatchlist(Long seriesId, User user) {
        TVSeries series = tvSeriesRepository.findById(seriesId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid series Id:" + seriesId));

        WatchlistSeries existingWatchlistSeries = watchlistSeriesRepository.findByUserAndTvSeries(user, series);
        if (existingWatchlistSeries == null) {
            WatchlistSeries watchlistSeries = new WatchlistSeries();
            watchlistSeries.setUser(user);
            watchlistSeries.setTvSeries(series);
            watchlistSeriesRepository.save(watchlistSeries);
        } else {
            throw new IllegalArgumentException("TV Series ie already exists in the user's watchlist");
        }
    }

    @Override
    public List<WatchlistSeries> getWatchlistSeriesMovies(Long userId) {
        return watchlistSeriesRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public void removeSeriesFromWatchlist(Long movieId, User user) {
        watchlistSeriesRepository.deleteByTvSeriesIdAndUser(movieId, user);
    }

    @Override
    public void addSeries(String title, LocalDate releaseDate, int seasons, double rating, String imageURL, String videoURL, String description, Long directorId) {
        TVSeries series = new TVSeries();
        series.setTitle(title);
        series.setReleaseDate(releaseDate);
        series.setSeasons(seasons);
        series.setRating(rating);
        series.setImageURL(imageURL);
        series.setVideoURL(videoURL);
        series.setDescription(description);

        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new NoSuchElementException("Director not found with id: " + directorId));
        series.setDirector(director);

        tvSeriesRepository.save(series);
    }

    @Override
    public TVSeries convertDtoToSeries(TVSeriesDTO seriesDTO) {
        TVSeries series = new TVSeries();
        series.setId(seriesDTO.getId());
        series.setTitle(seriesDTO.getTitle());
        series.setReleaseDate(seriesDTO.getReleaseDate());
        series.setSeasons(seriesDTO.getSeasons());
        series.setRating(seriesDTO.getRating());
        series.setImageURL(seriesDTO.getImageURL());
        series.setVideoURL(seriesDTO.getVideoURL());
        series.setDescription(seriesDTO.getDescription());
        return series;
    }

    @Override
    public void updateSeries(Long id, String title, LocalDate releaseDate, int seasons, Double rating, String imageURL, String videoURL, String description, Long directorId) {
        TVSeries series = tvSeriesRepository.findById(id).orElse(null);

        if (series != null){
            series.setTitle(title);
            series.setReleaseDate(releaseDate);
            series.setSeasons(seasons);
            series.setRating(rating);
            series.setImageURL(imageURL);
            series.setVideoURL(videoURL);
            series.setDescription(description);

            if (directorId != null) {
                directorRepository.findById(directorId).ifPresent(series::setDirector);
            }

            tvSeriesRepository.save(series);
        }
    }

    @Override
    public List<TVSeries> findSimilarSeries(Long movieId) {
        TVSeries currentSeries = tvSeriesRepository.findById(movieId).orElse(null);

        if (currentSeries == null) {
            return new ArrayList<>();
        }

        Category movieCategory = currentSeries.getCategory().iterator().next();
        return tvSeriesRepository.findByCategoryAndIdNot(movieCategory, movieId);
    }
    @Override
    public List<TVSeries> findSeriesByYear(int year) {
        return tvSeriesRepository.findSeriesByReleaseYear(year);
    }

    @Override
    public void removeActorFromSeries(Long seriesId, Long actorId) {
        TVSeries series = tvSeriesRepository.findById(seriesId).orElseThrow(() -> new RuntimeException("Series not found"));
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new RuntimeException("Actor not found"));
        series.getActors().remove(actor);
        tvSeriesRepository.save(series);
    }
    @Override
    public void removeCategoryFromSeries(Long seriesId, Long categoryId) {
        TVSeries series = tvSeriesRepository.findById(seriesId).orElseThrow(() -> new RuntimeException("Series not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        series.getCategory().remove(category);
        tvSeriesRepository.save(series);
    }

    @Override
    @Transactional
    public void deleteSeries(Long id) {
        Optional<TVSeries> seriesOptional = tvSeriesRepository.findById(id);
        if (seriesOptional.isPresent()) {
            tvSeriesRepository.delete(seriesOptional.get());
        } else {
            throw new IllegalArgumentException("TV Series not found");
        }
    }

    @Override
    public void saveSeries(String title, LocalDate date, int seasons, double rating, MultipartFile file, String videoURL, String description, Long directorId) throws IOException {
        String fileName = file.getOriginalFilename();
        Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        TVSeries series = new TVSeries();
        series.setTitle(title);
        series.setReleaseDate(date);
        series.setSeasons(seasons);
        series.setRating(rating);
        series.setImageURL(fileName);
        series.setVideoURL(videoURL);
        series.setDescription(description);

        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new NoSuchElementException("Director not found with id: " + directorId));
        series.setDirector(director);

        tvSeriesRepository.save(series);
    }
}
