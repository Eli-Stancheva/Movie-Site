package com.example.moviedb.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DirectorImage")
public class DirectorImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    public DirectorImage() {
    }

    public DirectorImage(Long id, String image, Director director) {
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
