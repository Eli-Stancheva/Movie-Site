package com.example.moviedb.models.DTOs;

import java.time.LocalDate;
import java.util.List;

public class ActorDTO {
    private Long id;
    private String actorName;
    private LocalDate actorBirthdate;
    private String actorBiography;
    private String actorImg;
    private List<ActorImageDTO> images;

    public ActorDTO(Long id, String actorName, LocalDate actorBirthdate, String actorBiography, String actorImg, List<ActorImageDTO> images) {
        this.id = id;
        this.actorName = actorName;
        this.actorBirthdate = actorBirthdate;
        this.actorBiography = actorBiography;
        this.actorImg = actorImg;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public LocalDate getActorBirthdate() {
        return actorBirthdate;
    }

    public void setActorBirthdate(LocalDate actorBirthdate) {
        this.actorBirthdate = actorBirthdate;
    }

    public String getActorBiography() {
        return actorBiography;
    }

    public void setActorBiography(String actorBiography) {
        this.actorBiography = actorBiography;
    }


    public String getActorImg() {
        return actorImg;
    }

    public void setActorImg(String actorImg) {
        this.actorImg = actorImg;
    }

    public List<ActorImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ActorImageDTO> images) {
        this.images = images;
    }
}

