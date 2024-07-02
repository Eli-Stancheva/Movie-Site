package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.MovieDTO;
import com.example.moviedb.models.entity.*;
import com.example.moviedb.repositories.*;
import com.example.moviedb.services.ActorService;
import com.example.moviedb.services.CategoryService;
import com.example.moviedb.services.DirectorService;
import com.example.moviedb.services.MoviesService;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoviesServiceImpl implements MoviesService {
    private final MovieRepository movieRepository;
    private final TVSeriesRepository tvSeriesRepository;
    private final CategoryRepository categoryRepository;
    private final ActorRepository actorRepository;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final CategoryService categoryService;
    private final DirectorRepository directorRepository;
    private final RatingFromUserRepository ratingFromUserRepository;
    private final RatingSeriesFromUserRepository ratingSeriesFromUserRepository;
    private final WatchlistMovieRepository watchlistMovieRepository;
    private final MovieActorRepository movieActorRepository;
    private final MovieCategoryRepository movieCategoryRepository;
    private final WatchlistRepository watchlistRepository;
    private final List<MovieDTO> movies;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public MoviesServiceImpl(MovieRepository movieRepository, TVSeriesRepository tvSeriesRepository, CategoryRepository categoryRepository, ActorRepository actorRepository, ActorService actorService, DirectorService directorService, CategoryService categoryService, DirectorRepository directorRepository, RatingFromUserRepository ratingFromUserRepository, RatingSeriesFromUserRepository ratingSeriesFromUserRepository, WatchlistMovieRepository watchlistMovieRepository, MovieActorRepository movieActorRepository, MovieCategoryRepository movieCategoryRepository, WatchlistRepository watchlistRepository, List<MovieDTO> movies){
        this.movieRepository = movieRepository;
        this.tvSeriesRepository = tvSeriesRepository;
        this.categoryRepository = categoryRepository;
        this.actorRepository = actorRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.categoryService = categoryService;
        this.directorRepository = directorRepository;
        this.ratingFromUserRepository = ratingFromUserRepository;
        this.ratingSeriesFromUserRepository = ratingSeriesFromUserRepository;
        this.watchlistMovieRepository = watchlistMovieRepository;
        this.movieActorRepository = movieActorRepository;
        this.movieCategoryRepository = movieCategoryRepository;
        this.watchlistRepository = watchlistRepository;
        this.movies = movies;
    }

    @Override
    public Optional<Movie> findByName(String name) {
        return movieRepository.findByTitle(name);
    }

    @Override
    public List<MovieDTO> getMovies() {
        return this.movieRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public MovieDTO getMovieById(Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);

        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            return convertToDto(movie);
        } else {
            throw new NoSuchElementException("Movie not found with id: " + id);
        }
    }

    @Override
    public MovieDTO convertToDto(Movie movie){
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.getImageURL(),
                movie.getVideoURL(),
                movie.getDescription(),
                movie.getCategory().stream().map(categoryService::convertToDto).collect(Collectors.toSet()),
                movie.getActors().stream().map(actorService::convertToDto).collect(Collectors.toSet()),
                directorService.convertToDto(movie.getDirector()));
    }

    @Override
    public Movie convertDtoToMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setRating(movieDTO.getRating());
        movie.setImageURL(movieDTO.getImageURL());
        movie.setVideoURL(movieDTO.getVideoURL());
        movie.setDescription(movieDTO.getDescription());
        return movie;
    }

    @Override
    public List<MovieDTO> getTopRatedMovies() {
        List<Movie> allMovies = movieRepository.findAll();

        List<Movie> topRatedMovies = allMovies.stream()
                .filter(movie -> movie.getRating() >= 8.0).toList();

        return topRatedMovies.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getTopRatedMoviesLimited() {
        List<Movie> allMovies = movieRepository.findAll();

        List<Movie> topRatedMovies = allMovies.stream()
                .filter(movie -> movie.getRating() >= 8.0).toList();

        return topRatedMovies.stream()
                .map(this::convertToDto)
                .limit(6)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getNewestMovie() {
        Movie newestMovie = movieRepository.findTopByOrderByReleaseDateDesc();

        if (newestMovie == null) {
            return null;
        }
        return convertToDto(newestMovie);
    }

    @Override
    public List<MovieDTO> searchMovieByTitleOrActorOrDirectorOrCategory(String query) {
    List<Movie> books = movieRepository.findByTitleContainingIgnoreCaseOrActors_ActorNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCaseOrDirector_DirectorNameContainingIgnoreCase(query, query, query, query);
    return books.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> searchMovieIgnoreCase(String query) {
        return this.searchMovieByTitleOrActorOrDirectorOrCategory(query).stream()
            .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }

    @Override
    public Movie findById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));
    }

    @Override
    public void rateMovie(Long movieId, int rating, User user) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));

        RatingFromUser existingRating = ratingFromUserRepository.findByMovieAndUser(movie, user);

        if (existingRating != null) {
            existingRating.setRating(rating);
            ratingFromUserRepository.save(existingRating);
        } else {
            RatingFromUser newRating = new RatingFromUser();
            newRating.setMovie(movie);
            newRating.setUser(user);
            newRating.setRating(rating);
            ratingFromUserRepository.save(newRating);
        }
    }

    @Override
    public List<Movie> getUserRatings(User user) {
        List<RatingFromUser> userRatings = ratingFromUserRepository.findByUser(user);
        List<Movie> ratedMovies = new ArrayList<>();

        for (RatingFromUser rating : userRatings) {
            Movie movie = rating.getMovie();
            movie.setRating(rating.getRating());
            ratedMovies.add(movie);
        }

        return ratedMovies;
    }

    @Override
    public void removeRating(Long movieId, User user) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));

        RatingFromUser ratingFromUser = ratingFromUserRepository.findByMovieAndUser(movie, user);

        if (ratingFromUser != null) {
            ratingFromUserRepository.delete(ratingFromUser);
        } else {
            throw new IllegalArgumentException("Rating not found for movieId: " + movieId + " and userId: " + user.getId());
        }
    }

    @Override
    public int getUserRatingForMovie(Long movieId, Long userId) {
        RatingFromUser rating = ratingFromUserRepository.findByMovieIdAndUserId(movieId, userId);
        return rating != null ? rating.getRating() : 0;
    }

    @Override
    public double getAverageRatingForMovie(Long movieId) {
        List<RatingFromUser> ratings = ratingFromUserRepository.findByMovieId(movieId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (RatingFromUser rating : ratings) {
            totalRating += rating.getRating();
        }
        return  totalRating * 1.0 / ratings.size();
    }

    @Override
    public List<Movie> getAllWatchlistMoviesForUser(Long userId) {
        List<WatchlistMovie> watchlistMovies = watchlistMovieRepository.findAllByUserId(userId);

        List<Movie> movies = new ArrayList<>();

        for (WatchlistMovie list : watchlistMovies) {
            movies.add(list.getMovie());
        }
        return movies;
    }

    @Override
    public void addToWatchlist(Long movieId, User user) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));

        WatchlistMovie existingWatchlistMovie = watchlistMovieRepository.findByUserAndMovie(user, movie);
        if (existingWatchlistMovie == null) {
            WatchlistMovie watchlistMovie = new WatchlistMovie();
            watchlistMovie.setUser(user);
            watchlistMovie.setMovie(movie);
            watchlistMovieRepository.save(watchlistMovie);
        } else {
            throw new IllegalArgumentException("Movie already exists in the user's watchlist");
        }
    }

    @Override
    public List<WatchlistMovie> getWatchlistMovies(Long userId) {
        return watchlistMovieRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public void removeMovieFromWatchlist(Long movieId, User user) {
        watchlistMovieRepository.deleteByMovieIdAndUser(movieId, user);
    }

    @Override
    public void addMovie(String title, LocalDate releaseDate, double rating, String imageURL, String videoURL, String description, Long directorId) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseDate(releaseDate);
        movie.setRating(rating);
        movie.setImageURL(imageURL);
        movie.setVideoURL(videoURL);
        movie.setDescription(description);

        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new NoSuchElementException("Director not found with id: " + directorId));
        movie.setDirector(director);

        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(Long id, String title, LocalDate releaseDate, Double rating, String imageURL, String videoURL, String description, Long directorId) {
        Movie movie = movieRepository.findById(id).orElse(null);

        if (movie != null){
            movie.setTitle(title);
            movie.setReleaseDate(releaseDate);
            movie.setRating(rating);
            movie.setImageURL(imageURL);
            movie.setVideoURL(videoURL);
            movie.setDescription(description);

            if (directorId != null) {
                directorRepository.findById(directorId).ifPresent(movie::setDirector);
            }

            movieRepository.save(movie);
        }
    }

    @Override
    public List<Movie> findSimilarMovies(Long movieId) {
        Movie currentMovie = movieRepository.findById(movieId).orElse(null);

        if (currentMovie == null) {
            return new ArrayList<>();
        }

        Category movieCategory = currentMovie.getCategory().iterator().next();
        return movieRepository.findByCategoryAndIdNot(movieCategory, movieId);
    }

    @Override
    public List<Movie> findMoviesByYear(int year) {
        return movieRepository.findByReleaseYear(year);
    }

    @Override
    public void removeActorFromMovie(Long movieId, Long actorId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new RuntimeException("Actor not found"));
        movie.getActors().remove(actor);
        movieRepository.save(movie);
    }
    @Override
    public void removeCategoryFromMovie(Long movieId, Long categoryId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        movie.getCategory().remove(category);
        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            movieRepository.delete(movieOptional.get());
        } else {
            throw new IllegalArgumentException("Movie not found");
        }
    }

    @Override
    public boolean isMovieInWatchlist(User user, Long movieId) {
        return watchlistMovieRepository.existsByUserAndMovieId(user, movieId);
    }

    @Override
    public void saveMovies(String title, LocalDate date, double rating, MultipartFile file, String videoURL, String description, Long directorId) throws IOException {
        // Запазване на файла на файловата система
        String fileName = file.getOriginalFilename();
        Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        // Запазване на метаданните в базата данни
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseDate(date);
        movie.setRating(rating); // уверете се, че имате дата в заявката
        movie.setImageURL(fileName);
        movie.setVideoURL(videoURL);
        movie.setDescription(description);

        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new NoSuchElementException("Director not found with id: " + directorId));
        movie.setDirector(director);

        movieRepository.save(movie);
    }
}
