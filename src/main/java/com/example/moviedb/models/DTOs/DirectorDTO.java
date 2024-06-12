package com.example.moviedb.models.DTOs;

import java.time.LocalDate;
import java.util.List;

public class DirectorDTO {
    private Long id;
    private String directorName;
    private LocalDate directorBirthdate;
    private String directorBiography;
    private String directorImg;
    private List<DirectorImageDTO> images;
    public DirectorDTO(Long id, String directorName, LocalDate directorBirthdate, String directorBiography, String directorImg, List<DirectorImageDTO> images) {
        this.id = id;
        this.directorName = directorName;
        this.directorBirthdate = directorBirthdate;
        this.directorBiography = directorBiography;
        this.directorImg = directorImg;
        this.images = images;
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

    public List<DirectorImageDTO> getImages() {
        return images;
    }

    public void setImages(List<DirectorImageDTO> images) {
        this.images = images;
    }
}
