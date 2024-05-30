package com.example.moviedb.services;

import com.example.moviedb.models.entity.Watchlist;

import java.util.List;

public interface WatchlistService {
    Watchlist createWatchlist(String username, String listName);
//    void addMovieToWatchlist(Long listId, Long movieId);
//    void addSeriesToWatchlist(Long listId, Long seriesId);
    List<Watchlist> getUserWatchlist(String username);
    void addMovieToWatchlistByName(Long listId, String movieName);
    void addSeriesToWatchlistByName(Long listId, String seriesName);
    Watchlist getWatchlistById(Long listId);
}
