package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.MovieActor;
import com.example.moviedb.models.entity.RatingFromUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieActorRepository extends JpaRepository<MovieActor, Long> {
    List<MovieActor> deleteByMovieId(Long movieId);
}