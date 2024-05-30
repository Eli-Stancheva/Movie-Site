package com.example.moviedb.controllers;

import com.example.moviedb.models.entity.Category;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.services.CategoryService;
import com.example.moviedb.services.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final MoviesService moviesService;

    @Autowired
    public CategoryController(CategoryService categoryService, MoviesService moviesService) {
        this.categoryService = categoryService;
        this.moviesService = moviesService;
    }


    @GetMapping("/{id}")
    public String getMoviesByCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        List<Movie> movies = categoryService.getMoviesByCategoryId(id);
        model.addAttribute("categoryName", category.getCategoryName());
        model.addAttribute("movies", movies);
        return "by-category";
    }
}
