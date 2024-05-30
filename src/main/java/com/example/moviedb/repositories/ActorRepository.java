package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findById(Long id);
    Actor findByActorName(String name);

    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
    List<Movie> findMoviesByActorId(Long actorId);
    @Query("SELECT s FROM TVSeries s JOIN s.actors a WHERE a.id = :actorId")
    List<TVSeries> findTVSeriesByActorId(Long actorId);
}
