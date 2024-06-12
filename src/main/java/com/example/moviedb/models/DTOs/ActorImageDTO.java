package com.example.moviedb.models.DTOs;

import com.example.moviedb.models.entity.Actor;
public class ActorImageDTO {
    private Long id;
    private String image;
    private Actor actor;

    public ActorImageDTO(Long id, String image, Actor actor) {
        this.id = id;
        this.image = image;
        this.actor = actor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
