package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.MovieActor;
import com.example.moviedb.models.entity.MovieCategory;
import com.example.moviedb.repositories.MovieCategoryRepository;
import com.example.moviedb.services.MovieCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieCategoryServiceImpl implements MovieCategoryService {
    private final MovieCategoryRepository movieCategoryRepository;

    @Autowired
    public MovieCategoryServiceImpl(MovieCategoryRepository movieCategoryRepository) {
        this.movieCategoryRepository = movieCategoryRepository;
    }

    public void addCategoryToMovie(Long movieId, Long categoryId) {
        MovieCategory movieCategory = new MovieCategory(movieId, categoryId);
        movieCategoryRepository.save(movieCategory);
    }
}
