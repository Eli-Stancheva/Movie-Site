package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.MovieActor;
import com.example.moviedb.models.entity.SeriesActor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesActorRepository extends JpaRepository<SeriesActor, Long> {
}
