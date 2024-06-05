package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.ActorDTO;
import com.example.moviedb.models.DTOs.DirectorDTO;
import com.example.moviedb.models.entity.Director;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;

import java.util.List;

public interface DirectorService {
    List<DirectorDTO> getAllDirectors();
    DirectorDTO getDirectorById(Long id);
    DirectorDTO convertToDto(Director Director);
    List<Movie> getMoviesByDirectorId(Long actorId);
    List<TVSeries> getSeriesByDirectorId(Long directorId);
    List<Director> findAll();
    Director findById(Long id);
    void addDirector(Director director);
    Director convertDtoToDirector(DirectorDTO directorDTO);
    void updateDirector(Director updatedDirector);
    void deleteDirector(Long id);
}
