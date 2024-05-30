package com.example.moviedb.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tvseries_category")
public class TVSeriesCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tvSeries_id", nullable = false)
    private Long tvSeriesId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;


    public TVSeriesCategory() {}

    public TVSeriesCategory(Long tvSeriesId, Long categoryId) {
        this.tvSeriesId = tvSeriesId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTvSeriesId() {
        return tvSeriesId;
    }

    public void setTvSeriesId(Long tvSeriesId) {
        this.tvSeriesId = tvSeriesId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
