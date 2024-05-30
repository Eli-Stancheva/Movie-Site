package com.example.moviedb.services.impl;

import com.example.moviedb.models.entity.SeriesActor;
import com.example.moviedb.repositories.SeriesActorRepository;
import com.example.moviedb.services.SeriesActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeriesActorServiceImpl implements SeriesActorService {
    private final SeriesActorRepository seriesActorRepository;

    @Autowired
    public SeriesActorServiceImpl(SeriesActorRepository seriesActorRepository) {
        this.seriesActorRepository = seriesActorRepository;
    }

    @Override
    public void addActorToSeries(Long seriesId, Long actorId) {
        SeriesActor seriesActor = new SeriesActor(seriesId, actorId);
        seriesActorRepository.save(seriesActor);
    }
}
