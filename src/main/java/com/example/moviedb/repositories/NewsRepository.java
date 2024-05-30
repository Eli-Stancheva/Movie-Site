package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findTop1ByOrderByDateDesc();
    List<News> findAll();
    Optional<News> findById(Long id);
}
