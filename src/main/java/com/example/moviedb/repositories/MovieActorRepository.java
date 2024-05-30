package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.MovieActor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieActorRepository extends JpaRepository<MovieActor, Long> {

}