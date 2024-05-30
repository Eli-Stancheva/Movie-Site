package com.example.moviedb.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tvseries_actors")
public class SeriesActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tvSeries_id", nullable = false)
    private Long tvSeriesId;

    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    public SeriesActor() {}
    public SeriesActor(Long tvSeriesId, Long actorId) {
        this.tvSeriesId = tvSeriesId;
        this.actorId = actorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return tvSeriesId;
    }

    public void setMovieId(Long tvSeriesId) {
        this.tvSeriesId = tvSeriesId;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }
}
