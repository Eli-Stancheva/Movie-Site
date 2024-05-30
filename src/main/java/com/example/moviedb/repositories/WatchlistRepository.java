package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUser(User user);
}
