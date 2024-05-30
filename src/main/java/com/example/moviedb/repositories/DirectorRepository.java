package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Director;
import com.example.moviedb.models.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findById(Long id);
    Director findByDirectorName(String name);

}
