package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.Watchlist;
import com.example.moviedb.repositories.MovieRepository;
import com.example.moviedb.repositories.TVSeriesRepository;
import com.example.moviedb.repositories.UserRepository;
import com.example.moviedb.repositories.WatchlistRepository;
import com.example.moviedb.services.WatchlistService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    private WatchlistRepository watchlistRepository;
    private UserRepository userRepository;
    private MovieRepository movieRepository;
    private TVSeriesRepository seriesRepository;

    @Autowired
    public WatchlistServiceImpl(WatchlistRepository watchlistRepository, UserRepository userRepository, MovieRepository movieRepository, TVSeriesRepository seriesRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
    }

    @Override
    public Watchlist createWatchlist(String username, String listName) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Watchlist watchlist = new Watchlist();
        watchlist.setName(listName);
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

//    @Override
//    public void addMovieToWatchlist(Long listId, Long movieId) {
//        Watchlist watchlist = watchlistRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("Watchlist not found"));
//        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new EntityNotFoundException("Movie not found"));
//        watchlist.getMovies().add(movie);
//        watchlistRepository.save(watchlist);
//    }
//
//    @Override
//    public void addSeriesToWatchlist(Long listId, Long seriesId) {
//        Watchlist watchlist = watchlistRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("Watchlist not found"));
//        TVSeries series = seriesRepository.findById(seriesId).orElseThrow(() -> new EntityNotFoundException("Series not found"));
//        watchlist.getSeries().add(series);
//        watchlistRepository.save(watchlist);
//    }

    @Override
    public List<Watchlist> getUserWatchlist(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return watchlistRepository.findByUser(user);
    }

    @Override
    public void addMovieToWatchlistByName(Long listId, String movieName) {
        Watchlist watchlist = watchlistRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist not found with id: " + listId));


        Optional<Movie> optionalMovie = movieRepository.findByTitle(movieName);
        Movie movie = optionalMovie.orElseThrow(() -> new EntityNotFoundException("Movie not found with title: " + movieName));


        if (movie == null) {
            throw new EntityNotFoundException("Movie not found with title: " + movieName);
        }

        watchlist.getMovies().add(movie);
        watchlistRepository.save(watchlist);
    }

    @Override
    public void addSeriesToWatchlistByName(Long listId, String seriesName) {
        Watchlist watchlist = watchlistRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist not found with id: " + listId));


        Optional<TVSeries> optionalSeries = seriesRepository.findByTitle(seriesName);
        TVSeries series = optionalSeries.orElseThrow(() -> new EntityNotFoundException("Movie not found with title: " + seriesName));


        if (series == null) {
            throw new EntityNotFoundException("TV Series not found with title: " + seriesName);
        }

        watchlist.getSeries().add(series);
        watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist getWatchlistById(Long listId) {
        return watchlistRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist not found with id: " + listId));
    }
}
