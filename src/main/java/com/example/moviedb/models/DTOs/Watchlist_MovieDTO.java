package com.example.moviedb.models.DTOs;

import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;

public class Watchlist_MovieDTO {
    private Long id;
    private User user;
    private Movie movie;

    private TVSeries tvSeries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public TVSeries getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(TVSeries tvSeries) {
        this.tvSeries = tvSeries;
    }
}
