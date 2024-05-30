package com.example.moviedb.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Watchlist_Series")
public class WatchlistSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tvSeries_id")
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

    public TVSeries getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(TVSeries tvSeries) {
        this.tvSeries = tvSeries;
    }
}
