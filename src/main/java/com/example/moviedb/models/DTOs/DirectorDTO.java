package com.example.moviedb.models.DTOs;

import jakarta.persistence.Column;

import java.time.LocalDate;

public class DirectorDTO {
    private Long id;
    private String directorName;
    private LocalDate directorBirthdate;
    private String directorBiography;
    private String directorImg;

    public DirectorDTO(Long id, String directorName, LocalDate directorBirthdate, String directorBiography, String directorImg) {
        this.id = id;
        this.directorName = directorName;
        this.directorBirthdate = directorBirthdate;
        this.directorBiography = directorBiography;
        this.directorImg = directorImg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
