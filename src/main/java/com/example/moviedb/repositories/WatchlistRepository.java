package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUser(User user);
    void deleteByUserId(Long userId);
    void deleteByMoviesId(Long movieId);
    void deleteByIdAndUser(Long watchlistId, User user);
}
