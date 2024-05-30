package com.example.moviedb.controllers;

import com.example.moviedb.models.entity.Watchlist;
import com.example.moviedb.services.WatchlistService;
import com.example.moviedb.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    private final CurrentUser currentUser;

    @Autowired
    public WatchlistController(WatchlistService watchlistService, CurrentUser currentUser) {
        this.watchlistService = watchlistService;
        this.currentUser = currentUser;
    }

    @GetMapping("/create-page")
    public String getCreateWatchlistPage(Model model) {
        String username = currentUser.getUsername();
        List<Watchlist> userWatchlist = watchlistService.getUserWatchlist(username);
        model.addAttribute("userWatchlist", userWatchlist);
        return "create-watchlist";
    }

    @GetMapping("/user-watchlist")
    public String getWatchlistPage(Model model) {
        String username = currentUser.getUsername();
        List<Watchlist> watchlist = watchlistService.getUserWatchlist(username);
        model.addAttribute("watchlist", watchlist);
        return "watchlist-info";
    }

    @PostMapping("/create")
    public String createWatchlist(@RequestParam String listName) {
        String username = currentUser.getUsername();
//        watchlistService.createWatchlist(username, listName);
//        return "redirect:/watchlist/user-watchlist";

        Watchlist newWatchlist = watchlistService.createWatchlist(username, listName);
        return "redirect:/watchlist/" + newWatchlist.getId();
    }

    @PostMapping("/{listId}/moviesByName")
    public String addMovieToWatchlist(@PathVariable Long listId, @RequestParam String movieName) {
        watchlistService.addMovieToWatchlistByName(listId, movieName);
        return "redirect:/watchlist/user-watchlist";
    }

    @PostMapping("/{listId}/seriesByName")
    public String addSeriesToWatchlist(@PathVariable Long listId, @RequestParam String seriesName) {
        watchlistService.addSeriesToWatchlistByName(listId, seriesName);
        return "redirect:/watchlist/user-watchlist";
    }

    @GetMapping("/{listId}")
    public String viewWatchlist(@PathVariable Long listId, Model model) {
//        String username = currentUser.getUsername();
//        List<Watchlist> watchlist = watchlistService.getUserWatchlist(username);
//        model.addAttribute("watchlist", watchlist);

        Watchlist watchlist = watchlistService.getWatchlistById(listId);
        model.addAttribute("watchlist", watchlist);
        return "watchlist-info";
    }

}
