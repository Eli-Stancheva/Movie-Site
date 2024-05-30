package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.TVSeriesCategory;
import com.example.moviedb.repositories.TVSeriesCategoryRepository;
import com.example.moviedb.services.TVSeriesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TVSeriesCategoryServiceImpl implements TVSeriesCategoryService {
    private final TVSeriesCategoryRepository tvSeriesCategoryRepository;

    @Autowired
    public TVSeriesCategoryServiceImpl(TVSeriesCategoryRepository tvSeriesCategoryRepository) {
        this.tvSeriesCategoryRepository = tvSeriesCategoryRepository;
    }

    @Override
    public void addCategoryToSeries(Long seriesId, Long categoryId) {
        TVSeriesCategory seriesCategory = new TVSeriesCategory(seriesId, categoryId);
        tvSeriesCategoryRepository.save(seriesCategory);
    }
}
