package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.CategoryDTO;
import com.example.moviedb.models.entity.*;
import com.example.moviedb.repositories.CategoryRepository;
import com.example.moviedb.repositories.MovieRepository;
import com.example.moviedb.repositories.TVSeriesRepository;
import com.example.moviedb.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final MovieRepository movieRepository;
    private final TVSeriesRepository tvSeriesRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(MovieRepository movieRepository, TVSeriesRepository tvSeriesRepository, CategoryRepository categoryRepository) {
        this.movieRepository = movieRepository;
        this.tvSeriesRepository = tvSeriesRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Movie> getMoviesByCategoryId(Long categoryId) {
        return movieRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<TVSeries> getSeriesByCategoryId(Long categoryId) {
        return tvSeriesRepository.findByCategoryId(categoryId);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
    }

    @Override
    public List<CategoryDTO> getCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO convertToDto(Category category){
        return new CategoryDTO(category.getId(), category.getCategoryName());
    }



    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByCategoryName(name);
    }
}
