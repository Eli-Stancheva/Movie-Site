package com.example.moviedb.models.DTOs;

import com.example.moviedb.models.entity.News;
import com.example.moviedb.models.entity.User;

import java.time.LocalDate;

public class CommentDTO {
    private Long id;
    private User user;
    private News news;
    private String comment;
    private LocalDate postDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }
}
