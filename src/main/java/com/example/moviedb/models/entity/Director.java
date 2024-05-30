package com.example.moviedb.models.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Director")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String directorName;

    @Column(name = "birthdate")
    private LocalDate directorBirthdate;

    @Column(name = "biography")
    private String directorBiography;

    @Column(name = "img")
    private String directorImg;

    public Director() {}

    public Director(String directorName, LocalDate directorBirthdate, String directorBiography, String directorImg) {
        this.directorName = directorName;
        this.directorBirthdate = directorBirthdate;
        this.directorBiography = directorBiography;
        this.directorImg = directorImg;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public LocalDate getDirectorBirthdate() {
        return directorBirthdate;
    }

    public void setDirectorBirthdate(LocalDate directorBirthdate) {
        this.directorBirthdate = directorBirthdate;
    }

    public String getDirectorBiography() {
        return directorBiography;
    }

    public void setDirectorBiography(String directorBiography) {
        this.directorBiography = directorBiography;
    }

    public String getDirectorImg() {
        return directorImg;
    }

    public void setDirectorImg(String directorImg) {
        this.directorImg = directorImg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
