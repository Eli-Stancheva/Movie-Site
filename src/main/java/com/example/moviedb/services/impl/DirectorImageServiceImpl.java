package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.ActorImage;
import com.example.moviedb.models.entity.Director;
import com.example.moviedb.models.entity.DirectorImage;
import com.example.moviedb.repositories.DirectorImageRepository;
import com.example.moviedb.services.DirectorImageService;
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
import java.util.List;

@Service
public class DirectorImageServiceImpl implements DirectorImageService {
    @Autowired
    private DirectorImageRepository directorImageRepository;

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
}
