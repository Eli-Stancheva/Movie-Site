package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.ActorDTO;
import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;

import java.util.List;
import java.util.Set;

public interface ActorService {
    List<ActorDTO> getAllActors();
    ActorDTO getActorById(Long id);
    void addActor(Actor actor);
    void updateActor(Actor actor);
    void deleteActor(Long id);
    ActorDTO convertToDto(Actor actor);
    Set<ActorDTO> getActors();
    Actor convertDtoToActor(ActorDTO actorDTO);
    Actor findByName(String name);
    void save(Actor actor);
    List<Movie> getMoviesByActorId(Long actorId);
    List<TVSeries> getSeriesByActorId(Long actorId);
}
