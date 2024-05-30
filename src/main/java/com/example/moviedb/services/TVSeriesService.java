package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.TVSeriesDTO;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.WatchlistSeries;

import java.time.LocalDate;
import java.util.List;

public interface TVSeriesService {
    List<TVSeriesDTO> getTVSeries();
    List<TVSeriesDTO> getTopRatedSeriesLimited();
    TVSeriesDTO convertToDto(TVSeries series);
    List<TVSeriesDTO> searchSeriesIgnoreCase(String query);
    TVSeriesDTO getSeriesById(Long id);
    int getUserRatingForSeries(Long movieId, Long userId);
    double getAverageRatingForSeries(Long movieId);
    void rateSeries(Long seriesId, int rating, User user);
    List<TVSeries> getUserRatings(User user);
    void removeSeriesRating(Long seriesId, User user);
    List<TVSeries> getAllWatchlistSeriesForUser(Long userId);
    void addToSeriesWatchlist(Long movieId, User user);
    List<WatchlistSeries> getWatchlistSeriesMovies(Long userId);
    void removeSeriesFromWatchlist(Long movieId, User user);
    void addSeries(String title, LocalDate releaseDate, int seasons, double rating, String imageURL, String videoURL, String description, Long directorId);
    TVSeries convertDtoToSeries(TVSeriesDTO seriesDTO);
    void updateSeries(Long id, String title, LocalDate releaseDate, int seasons, Double rating, String imageURL, String videoURL, String description, Long directorId);
    List<TVSeries> findSimilarSeries(Long seriesId);
    List<TVSeries> findSeriesByYear(int year);
    void removeActorFromSeries(Long seriesId, Long actorId);
    void removeCategoryFromSeries(Long seriesId, Long categoryId);
}
