package com.example.moviedb.models.DTOs;



import com.example.moviedb.models.entity.Category;

import java.time.LocalDate;
import java.util.Set;

public class MovieDTO {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private double rating;
    private String imageURL;
    private String videoURL;
    private String description;
    private Set<CategoryDTO> category;
    private Set<ActorDTO> actors;
    private DirectorDTO director;

    public MovieDTO() {}

    public MovieDTO(Long id, String title, LocalDate releaseDate, double rating, String imageURL, String videoURL, String description, Set<CategoryDTO> category, Set<ActorDTO> actors, DirectorDTO director) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.imageURL = imageURL;
        this.videoURL = videoURL;
        this.description = description;
        this.category = category;
        this.actors = actors;
        this.director = director;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ActorDTO> getActors() {
        return actors;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Set<CategoryDTO> getCategory() {
        return category;
    }

    public void setCategory(Set<CategoryDTO> category) {
        this.category = category;
    }

    public DirectorDTO getDirector() {
        return director;
    }

    public void setDirector(DirectorDTO director) {
        this.director = director;
    }
}
