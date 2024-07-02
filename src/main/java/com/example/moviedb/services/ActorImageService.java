package com.example.moviedb.services;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.ActorImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ActorImageService {
    void saveGalleryImages(List<MultipartFile> files, Actor actor) throws IOException;
    void deleteActorGalleryImage(Long actorId, Long imageId);
    void addActorGalleryImages(Long actorId, MultipartFile[] images);
}
