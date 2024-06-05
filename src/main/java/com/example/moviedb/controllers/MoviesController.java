
package com.example.moviedb.controllers;


import com.example.moviedb.models.DTOs.MovieDTO;
import com.example.moviedb.models.entity.*;
import com.example.moviedb.services.*;
import com.example.moviedb.util.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MoviesController {
    private final MoviesService moviesService;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final CategoryService categoryService;
    private final TVSeriesService tvSeriesService;
    private final UserService userService;
    private final MovieActorService movieActorService;
    private final MovieCategoryService movieCategoryService;
    @Autowired
    public MoviesController(MoviesService moviesService, ActorService actorService, DirectorService directorService, CategoryService categoryService, TVSeriesService tvSeriesService, UserService userService, MovieActorService movieActorService, MovieCategoryService movieCategoryService) {
        this.moviesService = moviesService;
        this.actorService = actorService;
        this.directorService = directorService;
        this.categoryService = categoryService;
        this.tvSeriesService = tvSeriesService;
        this.userService = userService;
        this.movieActorService = movieActorService;
        this.movieCategoryService = movieCategoryService;
    }

    @GetMapping("/allMovies")
    public String getMovies(Model model, HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        List<MovieDTO> allMovies = moviesService.getMovies();
        model.addAttribute("allMovies", allMovies);
        return "movies";
    }

    @GetMapping("/bestMovies")
    public String getBestMovies(Model model) {
        List<MovieDTO> allMovies = moviesService.getMovies();
        List<MovieDTO> bestMovies = allMovies.stream()
                .filter(movie -> movie.getRating() >= 8.0)
                .sorted(Comparator.comparingDouble(MovieDTO::getRating).reversed())
                .collect(Collectors.toList());
        model.addAttribute("bestMovies", bestMovies);
        return "best-movies";
    }

    @GetMapping("/{id}")
    public String getMovieDetails(Model model, @PathVariable Long id) {
        MovieDTO movieById = moviesService.getMovieById(id);
        model.addAttribute("movieById", movieById);

        CurrentUser currentUser = userService.getCurrentUser();
        int userRating = moviesService.getUserRatingForMovie(id, currentUser.getId());
        model.addAttribute("userRating", userRating);

        double averageRating = moviesService.getAverageRatingForMovie(id);
        model.addAttribute("averageRating", averageRating);

        List<Movie> similarMovies = new ArrayList<>();
        if (!currentUser.isAdmin()) {
            similarMovies = moviesService.findSimilarMovies(id);
        }
        model.addAttribute("similarMovies", similarMovies);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String releaseYear = movieById.getReleaseDate().format(formatter);
        model.addAttribute("releaseYear", releaseYear);

        List<Director> allDirectors = directorService.findAll();
        model.addAttribute("allDirectors", allDirectors);
        return "movie-details";
    }

    @PostMapping("/rate/{id}")
    public String rateMovie(@PathVariable Long id, @RequestParam int rating) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        moviesService.rateMovie(id, rating, user);

        return "redirect:/movies/" + id;
    }

    @PostMapping("/delete-rating/{id}")
    public String deleteRating(@PathVariable Long id) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        moviesService.removeRating(id, user);

        return "redirect:/";
    }

    @PostMapping("/add/{movieId}")
    public String addToWatchlist(@PathVariable Long movieId, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        try {
            moviesService.addToWatchlist(movieId, user);
            redirectAttributes.addFlashAttribute("message", "Movie added to your watchlist.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/delete-watchlist-movie/{id}")
    public String deleteFromWatchlist(@PathVariable Long id) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        moviesService.removeMovieFromWatchlist(id, user);

        return "redirect:/movies/watchlist-movie";
    }

    @GetMapping("/watchlist-movie")
    public String getWatchlist(Model model) {
        CurrentUser currentUser = userService.getCurrentUser();
        List<Movie> watchlistMovie = moviesService.getAllWatchlistMoviesForUser(currentUser.getId());
        model.addAttribute("watchlistMovies", watchlistMovie);
        return "watchlist-movie";
    }

    @GetMapping("/add-form")
    public String showAddMovieForm(Model model) {
        List<Director> allDirectors = directorService.findAll();
        List<Category> allCategories = categoryService.findAll();
        model.addAttribute("movie", new Movie());
        model.addAttribute("allDirectors", allDirectors);
        model.addAttribute("allCategories", allCategories);
        return "add-movies";
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute MovieDTO movieDTO, @RequestParam Long directorId, Model model) {
    moviesService.addMovie(movieDTO.getTitle(),
            movieDTO.getReleaseDate(),
            movieDTO.getRating(),
            movieDTO.getImageURL(),
            movieDTO.getVideoURL(),
            movieDTO.getDescription(),
            directorId);

    model.addAttribute("successMessage", "Successfully added!");
    return "add-movies";
    }

    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute MovieDTO movieDTO, @RequestParam("directorId") Long directorId) {
        moviesService.updateMovie(id, movieDTO.getTitle(), movieDTO.getReleaseDate(), movieDTO.getRating(),
                movieDTO.getImageURL(), movieDTO.getVideoURL(), movieDTO.getDescription(), directorId);
        return "redirect:/movies/{id}";
    }

    @PostMapping("/add-actors")
    public String addActorToMovie(@RequestParam("movieId") Long movieId, @RequestParam("actorName") String actorName) {
        Actor actor = actorService.findByName(actorName);
        if (actor == null) {
            actor = new Actor();
            actor.setActorName(actorName);
            actorService.save(actor);
        }

        movieActorService.addActorToMovie(movieId, actor.getId());
        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/add-category")
    public String addCategoryToMovie(@RequestParam("movieId") Long movieId, @RequestParam("categoryName") String categoryName) {
        Category category = categoryService.findByName(categoryName);
        if (category == null) {
            category = new Category();
            category.setCategoryName(categoryName);
            categoryService.save(category);
        }

        movieCategoryService.addCategoryToMovie(movieId, category.getId());
        return "redirect:/movies/" + movieId;
    }

    @GetMapping("/year/{year}")
    public String getMoviesByYear(@PathVariable int year, Model model) {
        List<Movie> movies = moviesService.findMoviesByYear(year);
        model.addAttribute("movies", movies);
        List<TVSeries> series = tvSeriesService.findSeriesByYear(year);
        model.addAttribute("series", series);
        return "movies-by-year";
    }

    @PostMapping("/remove-actor/{movieId}/{actorId}")
    public String removeActorFromMovie(@PathVariable Long movieId, @PathVariable Long actorId) {
        moviesService.removeActorFromMovie(movieId, actorId);
        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/remove-category/{movieId}/{categoryId}")
    public String removeCategoryFromMovie(@PathVariable Long movieId, @PathVariable Long categoryId) {
        moviesService.removeCategoryFromMovie(movieId, categoryId);
        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();

        if (currentUser.isAdmin()) {
            try {
                moviesService.deleteMovie(id);
                redirectAttributes.addFlashAttribute("message", "Movie successfully deleted.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error deleting movie: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to delete movies.");
        }

        return "redirect:/movies/allMovies";
    }
}

