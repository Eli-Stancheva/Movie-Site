package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.ActorImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorImageRepository extends JpaRepository<ActorImage, Long> {
}
