package com.example.moviedb.models.DTOs;

import java.time.LocalDate;
import java.util.Date;

public class ActorDTO {
    private Long id;
    private String actorName;
    private LocalDate actorBirthdate;
    private String actorBiography;
    private String actorImg;

    public ActorDTO(Long id, String actorName, LocalDate actorBirthdate, String actorBiography, String actorImg) {
        this.id = id;
        this.actorName = actorName;
        this.actorBirthdate = actorBirthdate;
        this.actorBiography = actorBiography;
        this.actorImg = actorImg;
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
}

