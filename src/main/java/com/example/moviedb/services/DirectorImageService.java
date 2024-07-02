package com.example.moviedb.services;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Director;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DirectorImageService {
    void saveGalleryImages(List<MultipartFile> files, Director director) throws IOException;
    void deleteDirectorGalleryImage(Long directorId, Long imageId);
    void addDirectorGalleryImages(Long directorId, MultipartFile[] images);
}
