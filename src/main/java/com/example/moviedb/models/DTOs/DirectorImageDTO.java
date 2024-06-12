package com.example.moviedb.models.DTOs;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Director;

public class DirectorImageDTO {
    private Long id;
    private String image;
    private Director director;

    public DirectorImageDTO(Long id, String image, Director director) {
        this.id = id;
        this.image = image;
        this.director = director;
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

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }
}
