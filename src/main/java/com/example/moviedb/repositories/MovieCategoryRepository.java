package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.MovieActor;
import com.example.moviedb.models.entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {
    List<MovieCategory> deleteByMovieId(Long movieId);
}
