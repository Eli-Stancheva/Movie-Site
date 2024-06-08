package com.example.moviedb.models.DTOs;

import jakarta.persistence.Column;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

public class NewsDTO {
    private Long id;
    private String newsTitle;
    private String newsContent;
    private LocalDate date;
    private String image;
    public NewsDTO(Long id, String newsTitle, String newsContent, LocalDate date, String image){
        this.id = id;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.date = date;
        this.image = image;
    }
    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String  image) {
        this.image = image;
    }
}
