package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.WatchlistSeries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistSeriesRepository extends JpaRepository<WatchlistSeries, Long> {
    List<WatchlistSeries> findByUserId(Long userId);
    Optional<WatchlistSeries> findById(Long seriesId);
    WatchlistSeries findByUserAndTvSeries(User user, TVSeries series);
    List<WatchlistSeries> findAllByUserId(Long userId);
    void deleteByTvSeriesIdAndUser(Long seriesId, User user);
}
