package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.MovieCategory;
import com.example.moviedb.models.entity.TVSeriesCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TVSeriesCategoryRepository extends JpaRepository<TVSeriesCategory, Long> {
}
