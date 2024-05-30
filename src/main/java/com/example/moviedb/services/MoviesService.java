package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.MovieDTO;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.WatchlistMovie;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface MoviesService {
    List<MovieDTO> getMovies();
    MovieDTO getMovieById(Long id);
    List<MovieDTO> searchMovieIgnoreCase(String query);
    MovieDTO convertToDto(Movie movie);
    Movie convertDtoToMovie(MovieDTO movieDTO);
    List<MovieDTO> getTopRatedMovies();
    MovieDTO getNewestMovie();
    List<MovieDTO> getTopRatedMoviesLimited();
    Movie findById(Long movieId);
    void rateMovie(Long movieId, int rating, User user);
    List<Movie> getUserRatings(User user);
    void removeRating(Long movieId, User user);
    int getUserRatingForMovie(Long movieId, Long userId);
    double getAverageRatingForMovie(Long movieId);
    List<Movie> getAllWatchlistMoviesForUser(Long userId);
    void addToWatchlist(Long movieId, User user);
    List<WatchlistMovie> getWatchlistMovies(Long userId);
    void removeMovieFromWatchlist(Long movieId, User user);
    void updateMovie(Long id, String title, LocalDate releaseDate, Double rating, String imageURL, String videoURL, String description, Long directorId);
    List<Movie> findSimilarMovies(Long movieId);
    List<Movie> findMoviesByYear(int year);
    void addMovie(String title, LocalDate releaseDate, double rating, String imageURL, String videoURL, String description, Long directorId);
    void removeActorFromMovie(Long movieId, Long actorId);
    void removeCategoryFromMovie(Long movieId, Long categoryId);
}
