package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.RatingFromUser;
import com.example.moviedb.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingFromUserRepository extends JpaRepository<RatingFromUser, Long> {
    RatingFromUser findByMovieAndUser(Movie movie, User user);
    RatingFromUser findByMovieIdAndUserId(Long movieId, Long userId);
    List<RatingFromUser> findByMovieId(Long movieId);
    List<RatingFromUser> findByUser(User user);
    void deleteByUserId(Long userId);
    void deleteByMovieId(Long movieId);
}
