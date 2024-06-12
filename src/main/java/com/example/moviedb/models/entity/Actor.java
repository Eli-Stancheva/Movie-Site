package com.example.moviedb.models.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Actor")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String actorName;

    @Column(name = "birthdate")
    private LocalDate actorBirthdate;

    @Column(name = "biography")
    private String actorBiography;

    @Column(name = "actor_img")
    private String actorImg;

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActorImage> images;

    public Actor() {}

    public Actor(String actorName, LocalDate actorBirthdate, String actorBiography, String actorImg) {
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

    public List<ActorImage> getImages() {
        return images;
    }

    public void setImages(List<ActorImage> images) {
        this.images = images;
    }
}
