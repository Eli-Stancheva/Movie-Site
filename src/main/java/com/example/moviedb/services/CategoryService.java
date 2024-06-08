package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.CategoryDTO;
import com.example.moviedb.models.DTOs.MovieDTO;
import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Category;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;

import java.util.List;

public interface CategoryService {
    List<Movie> getMoviesByCategoryId(Long categoryId);
    List<TVSeries> getSeriesByCategoryId(Long categoryId);
    Category getCategoryById(Long id);
    List<CategoryDTO> getCategories();
    CategoryDTO convertToDto(Category category);
    List<Category> findAll();
    Category findByName(String name);
    void save(Category category);
}
