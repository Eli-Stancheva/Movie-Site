package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.ActorImage;
import com.example.moviedb.models.entity.Director;
import com.example.moviedb.models.entity.DirectorImage;
import com.example.moviedb.repositories.DirectorImageRepository;
import com.example.moviedb.repositories.DirectorRepository;
import com.example.moviedb.services.DirectorImageService;
import com.example.moviedb.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectorImageServiceImpl implements DirectorImageService {
    private final DirectorImageRepository directorImageRepository;
    private final DirectorRepository directorRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public DirectorImageServiceImpl(DirectorImageRepository directorImageRepository, DirectorRepository directorRepository, FileStorageService fileStorageService) {
        this.directorImageRepository = directorImageRepository;
        this.directorRepository = directorRepository;
        this.fileStorageService = fileStorageService;
    }



    @Value("${file.upload-dir}")
    private String uploadDir;
    @Override
    public void saveGalleryImages(List<MultipartFile> files, Director director) throws IOException {
        if (files != null) {
            for (MultipartFile galleryFile : files) {
                if (!galleryFile.isEmpty()) {
                    String galleryFileName = galleryFile.getOriginalFilename();
                    Path galleryCopyLocation = Paths.get(uploadDir + File.separator + galleryFileName);
                    Files.copy(galleryFile.getInputStream(), galleryCopyLocation, StandardCopyOption.REPLACE_EXISTING);

                    // Създаване на нов обект ActorImage
                    DirectorImage dImage = new DirectorImage();
                    dImage.setImage(galleryFileName);
                    dImage.setDirector(director);

                    // Запазване на изображението в базата данни чрез actorImageRepository
                    directorImageRepository.save(dImage);
                }
            }
        }
    }

    @Override
    public void deleteDirectorGalleryImage(Long directorId, Long imageId) {
        Director director = directorRepository.findById(directorId).orElseThrow(() -> new IllegalArgumentException("Invalid director Id"));

        DirectorImage imageToDelete = director.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Image with id " + imageId + " not found for actor " + directorId));

        director.getImages().remove(imageToDelete);

        directorRepository.save(director);
    }

    @Override
    public void addDirectorGalleryImages(Long directorId, MultipartFile[] images) {
        Director director = directorRepository.findById(directorId).orElseThrow(() -> new IllegalArgumentException("Invalid director Id"));

        List<DirectorImage> directorImages = director.getImages();
        if (directorImages == null) {
            directorImages = new ArrayList<>();
        }

        for (MultipartFile image : images) {
            String fileName = fileStorageService.storeFile(image);
            DirectorImage directorImage = new DirectorImage(fileName, director);
            directorImages.add(directorImage);
        }
        director.setImages(directorImages);
        directorRepository.save(director);
    }
}
