package com.example.moviedb.services;

import com.example.moviedb.models.entity.MovieActor;

public interface MovieCategoryService {
     void addCategoryToMovie(Long movieId, Long categoryId);
}
