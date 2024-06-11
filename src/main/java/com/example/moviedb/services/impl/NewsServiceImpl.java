package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.NewsDTO;
import com.example.moviedb.models.entity.News;
import com.example.moviedb.repositories.CommentRepository;
import com.example.moviedb.services.FileStorageService;
import com.example.moviedb.services.NewsService;
import com.example.moviedb.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final FileStorageService fileStorageService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, CommentRepository commentRepository, FileStorageService fileStorageService) {
        this.newsRepository = newsRepository;
        this.commentRepository = commentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<NewsDTO> getLatestNews() {
        return this.newsRepository.findTop1ByOrderByDateDesc()
                .stream()
                .map(news -> new NewsDTO(
                        news.getId(),
                        news.getNewsTitle(),
                        news.getNewsContent(),
                        news.getDate(),
                        news.getImageName()
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
                        news.getDate(),
                        news.getImageName()
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
                news.getDate(),
                news.getImageName());
    }


    @Override
    public void addNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public News convertDtoToNews(NewsDTO newsDTO) {
        News news = new News();
        news.setId(newsDTO.getId());
        news.setNewsTitle(newsDTO.getNewsTitle());
        news.setNewsContent(newsDTO.getNewsContent());
        news.setDate(newsDTO.getDate());
        news.setImageName(newsDTO.getImage());
        return news;
    }

    @Override
    public void updateNews(News updatedNews) {
        Optional<News> newsOptional = newsRepository.findById(updatedNews.getId());

        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            news.setNewsTitle(updatedNews.getNewsTitle());
            news.setNewsContent(updatedNews.getNewsContent());
            news.setDate(updatedNews.getDate());
            news.setImageName(updatedNews.getImageName());

            newsRepository.save(news);
        } else {
            throw new NoSuchElementException("News not found with id: " + updatedNews.getId());
        }
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        commentRepository.deleteByNewsId(id);

        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();

            if (news.getImageName() != null) {
                fileStorageService.deleteFile(news.getImageName());
            }

            newsRepository.delete(news);
        } else {
            throw new IllegalArgumentException("News not found");
        }
    }

    @Override
    public void saveNews(String title, String content, LocalDate date, MultipartFile file) throws IOException {
        // Запазване на файла на файловата система
        String fileName = file.getOriginalFilename();
        Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        // Запазване на метаданните в базата данни
        News news = new News();
        news.setNewsTitle(title);
        news.setNewsContent(content);
        news.setDate(date); // уверете се, че имате дата в заявката
        news.setImageName(fileName);

        newsRepository.save(news);
    }
}
