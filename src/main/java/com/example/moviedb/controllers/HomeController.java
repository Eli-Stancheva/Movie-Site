package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.CategoryDTO;
import com.example.moviedb.models.DTOs.MovieDTO;
import com.example.moviedb.models.DTOs.TVSeriesDTO;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.services.CategoryService;
import com.example.moviedb.services.MoviesService;
import com.example.moviedb.services.TVSeriesService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class HomeController {
    private final MoviesService moviesService;
    private final TVSeriesService tvSeriesService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public HomeController(MoviesService moviesService, TVSeriesService tvSeriesService, UserService userService, CategoryService categoryService) {
        this.moviesService = moviesService;
        this.tvSeriesService = tvSeriesService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getBestMoviesLimited(Model model) {
        List<MovieDTO> topRatedMovies = moviesService.getTopRatedMoviesLimited();
        MovieDTO newestMovies = moviesService.getNewestMovie();
        List<CategoryDTO> categories = categoryService.getCategories();
        List<TVSeriesDTO> topRatedSeries = tvSeriesService.getTopRatedSeriesLimited();
        CurrentUser currentUser = userService.getCurrentUser();
        List<Movie> watchlistMovie = moviesService.getAllWatchlistMoviesForUser(currentUser.getId());

        model.addAttribute("bestMoviesLimited", topRatedMovies);
        model.addAttribute("bestSeriesLimited", topRatedSeries);
        model.addAttribute("newestMovie", newestMovies);
        model.addAttribute("watchlistMovies", watchlistMovie);
        model.addAttribute("categories", categories);

        return "index";
    }
}
