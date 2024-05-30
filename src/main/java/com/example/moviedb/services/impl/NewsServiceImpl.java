package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.DirectorDTO;
import com.example.moviedb.models.DTOs.NewsDTO;
import com.example.moviedb.models.entity.Director;
import com.example.moviedb.models.entity.News;
import com.example.moviedb.services.NewsService;
import com.example.moviedb.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<NewsDTO> getLatestNews() {
        return this.newsRepository.findTop1ByOrderByDateDesc()
                .stream()
                .map(news -> new NewsDTO(
                        news.getId(),
                        news.getNewsTitle(),
                        news.getNewsContent(),
                        news.getDate()
                )).collect(Collectors.toList());
    }

    @Override
    public List<NewsDTO> getNews() {
        return this.newsRepository.findAll()
                .stream()
                .map(news -> new NewsDTO(
                        news.getId(),
                        news.getNewsTitle(),
                        news.getNewsContent(),
                        news.getDate()
                )).collect(Collectors.toList());
    }

    @Override
    public NewsDTO getNewsById(Long id) {
        Optional<News> newsOptional = newsRepository.findById(id);

        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            return convertToDto(news);
        } else {
            throw new NoSuchElementException("News not found with id: " + id);
        }
    }

    @Override
    public NewsDTO convertToDto(News news){
        return new NewsDTO(
                news.getId(),
                news.getNewsTitle(),
                news.getNewsContent(),
                news.getDate());
    }


    @Override
    public void addNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public News convertDtoToNews(NewsDTO newsDTO) {
        News director = new News();
        director.setId(newsDTO.getId());
        director.setNewsTitle(newsDTO.getNewsTitle());
        director.setNewsContent(newsDTO.getNewsContent());
        director.setDate(newsDTO.getDate());
        return director;
    }

    @Override
    public void updateNews(News updatedNews) {
        Optional<News> newsOptional = newsRepository.findById(updatedNews.getId());

        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            news.setNewsTitle(updatedNews.getNewsTitle());
            news.setNewsContent(updatedNews.getNewsContent());
            news.setDate(updatedNews.getDate());

            newsRepository.save(news);
        } else {
            throw new NoSuchElementException("News not found with id: " + updatedNews.getId());
        }
    }
}
