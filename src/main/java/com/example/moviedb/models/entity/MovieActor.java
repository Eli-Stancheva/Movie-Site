package com.example.moviedb.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "movie_actors")
public class MovieActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    public MovieActor() {}
    public MovieActor(Long movieId, Long actorId) {
        this.movieId = movieId;
        this.actorId = actorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }
}