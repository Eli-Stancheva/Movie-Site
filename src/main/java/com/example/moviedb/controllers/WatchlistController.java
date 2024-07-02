package com.example.moviedb.controllers;

import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.Watchlist;
import com.example.moviedb.services.MoviesService;
import com.example.moviedb.services.TVSeriesService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.services.WatchlistService;
import com.example.moviedb.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    private final CurrentUser currentUser;
    private final MoviesService moviesService;
    private final TVSeriesService tvSeriesService;
    private final UserService userService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService, CurrentUser currentUser, MoviesService moviesService, TVSeriesService tvSeriesService, UserService userService) {
        this.watchlistService = watchlistService;
        this.currentUser = currentUser;
        this.moviesService = moviesService;
        this.tvSeriesService = tvSeriesService;
        this.userService = userService;
    }

    @GetMapping("/create-page")
    public String getCreateWatchlistPage(Model model) {
        String username = currentUser.getUsername();
        List<Watchlist> userWatchlist = watchlistService.getUserWatchlist(username);
        model.addAttribute("userWatchlist", userWatchlist);
        return "create-watchlist";
    }

    @PostMapping("/create")
    public String createWatchlist(@RequestParam String listName) {
        String username = currentUser.getUsername();
        Watchlist newWatchlist = watchlistService.createWatchlist(username, listName);
        return "redirect:/watchlist/" + newWatchlist.getId();
    }

    @PostMapping("/{listId}/addItem")
    public String addItemToWatchlist(@PathVariable Long listId, @RequestParam String itemName, RedirectAttributes redirectAttributes) {
        Watchlist watchlist = watchlistService.getWatchlistById(listId);

        // Check if the movie already exists in the watchlist
        Optional<Movie> movieOptional = moviesService.findByName(itemName);
        if (movieOptional.isPresent()) {
            if (watchlist.getMovies().contains(movieOptional.get())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Movie already in watchlist.");
                return "redirect:/watchlist/" + listId;
            }
            watchlistService.addMovieToWatchlist(watchlist, movieOptional.get());
            redirectAttributes.addFlashAttribute("message", "Movie added to watchlist.");
            return "redirect:/watchlist/" + listId;
        }

        Optional<TVSeries> seriesOptional = tvSeriesService.findByName(itemName);
        if (seriesOptional.isPresent()) {
            if (watchlist.getSeries().contains(seriesOptional.get())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Series already in watchlist.");
                return "redirect:/watchlist/" + listId;
            }
            watchlistService.addSeriesToWatchlist(watchlist, seriesOptional.get());
            redirectAttributes.addFlashAttribute("message", "Series added to watchlist.");
            return "redirect:/watchlist/" + listId;
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Item not found.");
        return "redirect:/watchlist/" + listId;
    }


    @GetMapping("/{listId}")
    public String viewWatchlist(@PathVariable Long listId, Model model) {
        Watchlist watchlist = watchlistService.getWatchlistById(listId);
        model.addAttribute("watchlist", watchlist);
        return "watchlist-info";
    }

    @PostMapping("/{watchlistId}/movies/{movieId}/delete")
    public String deleteMovieFromWatchlist(@PathVariable Long watchlistId, @PathVariable Long movieId) {
        watchlistService.removeMovieFromWatchlist(watchlistId, movieId);
        return "redirect:/watchlist/" + watchlistId;
    }

    @PostMapping("/{watchlistId}/series/{seriesId}/delete")
    public String deleteSeriesFromWatchlist(@PathVariable Long watchlistId, @PathVariable Long seriesId) {
        watchlistService.removeSeriesFromWatchlist(watchlistId, seriesId);
        return "redirect:/watchlist/" + watchlistId;
    }


    @PostMapping("/delete/{id}")
    public String deleteWatchlist(@PathVariable Long id) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());
        watchlistService.deleteWatchlist(id, user);
        return "redirect:/watchlist/create-page";
    }
}
