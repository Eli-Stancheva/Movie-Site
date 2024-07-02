package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.ActorImage;
import com.example.moviedb.repositories.ActorImageRepository;
import com.example.moviedb.repositories.ActorRepository;
import com.example.moviedb.services.ActorImageService;
import com.example.moviedb.services.FileStorageService;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class ActorImageServiceImpl implements ActorImageService {
    private final ActorImageRepository actorImageRepository;
    private final ActorRepository actorRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ActorImageServiceImpl(ActorImageRepository actorImageRepository, ActorRepository actorRepository, FileStorageService fileStorageService) {
        this.actorImageRepository = actorImageRepository;
        this.actorRepository = actorRepository;
        this.fileStorageService = fileStorageService;
    }

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

                    ActorImage actorImage = new ActorImage();
                    actorImage.setImage(galleryFileName);
                    actorImage.setActor(actor);

                    actorImageRepository.save(actorImage);
                }
            }
        }
    }

    public void addActorGalleryImages(Long actorId, MultipartFile[] images) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid actor Id"));

        List<ActorImage> actorImages = actor.getImages();
        if (actorImages == null) {
            actorImages = new ArrayList<>();
        }

        for (MultipartFile image : images) {
            String fileName = fileStorageService.storeFile(image);
            ActorImage actorImage = new ActorImage(fileName, actor);
            actorImages.add(actorImage);
        }
        actor.setImages(actorImages);
        actorRepository.save(actor);
    }

    @Transactional
    public void deleteActorGalleryImage(Long actorId, Long imageId) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid actor Id"));

        ActorImage imageToDelete = actor.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Image with id " + imageId + " not found for actor " + actorId));

        actor.getImages().remove(imageToDelete);
        actorRepository.save(actor);
    }
}
