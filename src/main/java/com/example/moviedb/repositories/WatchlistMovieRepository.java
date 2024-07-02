package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.WatchlistMovie;
import com.example.moviedb.util.CurrentUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistMovieRepository extends JpaRepository<WatchlistMovie, Long> {
    List<WatchlistMovie> findByUserId(Long userId);
    Optional<WatchlistMovie> findById(Long movieId);
    WatchlistMovie findByUserAndMovie(User user, Movie movie);
    List<WatchlistMovie> findAllByUserId(Long userId);
    void deleteByMovieIdAndUser(Long movieId, User user);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    Optional<WatchlistMovie> findByUserIdAndMovieId(Long userId, Long movieId);
    boolean existsByUserAndMovieId(User currentUser, Long movieId);
    void deleteByUserId(Long userId);
    void deleteByMovieId(Long userId);
}
