package com.example.moviedb.services;

import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.models.entity.Watchlist;

import java.util.List;

public interface WatchlistService {
    Watchlist createWatchlist(String username, String listName);
    List<Watchlist> getUserWatchlist(String username);
    Watchlist getWatchlistById(Long listId);
    void removeMovieFromWatchlist(Long watchlistId, Long movieId);
    void removeSeriesFromWatchlist(Long watchlistId, Long seriesId);

    void addMovieToWatchlist(Watchlist watchlist, Movie movie);
    void addSeriesToWatchlist(Watchlist watchlist, TVSeries series);
}
