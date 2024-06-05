package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.NewsDTO;
import com.example.moviedb.models.entity.News;

import java.util.List;


public interface NewsService {
    List<NewsDTO> getLatestNews();
    List<NewsDTO> getNews();
    NewsDTO getNewsById(Long id);
    NewsDTO convertToDto(News news);
    void addNews(News news);
    News convertDtoToNews(NewsDTO newsDTO);
    void updateNews(News updatedNews);
    void deleteNews(Long id);
}
