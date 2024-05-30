package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.MovieActor;
import com.example.moviedb.repositories.MovieActorRepository;
import com.example.moviedb.services.MovieActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieActorServiceImpl implements MovieActorService {
    private final MovieActorRepository movieActorRepository;

    @Autowired
    public MovieActorServiceImpl(MovieActorRepository movieActorRepository) {
        this.movieActorRepository = movieActorRepository;
    }

    public void addActorToMovie(Long movieId, Long actorId) {
        MovieActor movieActor = new MovieActor(movieId, actorId);
        movieActorRepository.save(movieActor);
    }
}
