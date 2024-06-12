package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.DirectorImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorImageRepository extends JpaRepository<DirectorImage, Long> {
}
