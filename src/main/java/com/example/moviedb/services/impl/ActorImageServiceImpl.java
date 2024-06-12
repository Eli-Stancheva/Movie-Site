package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.ActorImage;
import com.example.moviedb.repositories.ActorImageRepository;
import com.example.moviedb.services.ActorImageService;
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
public class ActorImageServiceImpl implements ActorImageService {
    @Autowired
    private ActorImageRepository actorImageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Override
    public void saveGalleryImages(List<MultipartFile> files, Actor actor) throws IOException {
        if (files != null) {
            for (MultipartFile galleryFile : files) {
                if (!galleryFile.isEmpty()) {
                    String galleryFileName = galleryFile.getOriginalFilename();
                    Path galleryCopyLocation = Paths.get(uploadDir + File.separator + galleryFileName);
                    Files.copy(galleryFile.getInputStream(), galleryCopyLocation, StandardCopyOption.REPLACE_EXISTING);

                    // Създаване на нов обект ActorImage
                    ActorImage actorImage = new ActorImage();
                    actorImage.setImage(galleryFileName);
                    actorImage.setActor(actor);

                    // Запазване на изображението в базата данни чрез actorImageRepository
                    actorImageRepository.save(actorImage);
                }
            }
        }
    }
}
