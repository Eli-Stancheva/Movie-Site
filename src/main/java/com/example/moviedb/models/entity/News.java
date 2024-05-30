package com.example.moviedb.models.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "News")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String newsTitle;

    @Column(nullable = false, name = "content")
    private String newsContent;

    @Column(nullable = false, name = "date")
    private LocalDate date;

    public News() {}

    public News(String newsTitle, String newsContent, LocalDate date) {
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.date = date;
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
}
