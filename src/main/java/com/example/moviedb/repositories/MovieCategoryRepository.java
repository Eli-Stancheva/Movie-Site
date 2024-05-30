package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {

}
