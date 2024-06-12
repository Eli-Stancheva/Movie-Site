package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.DirectorDTO;
import com.example.moviedb.models.DTOs.DirectorImageDTO;
import com.example.moviedb.models.entity.*;
import com.example.moviedb.repositories.DirectorRepository;
import com.example.moviedb.repositories.MovieRepository;
import com.example.moviedb.repositories.TVSeriesRepository;
import com.example.moviedb.services.DirectorService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;
    private final TVSeriesRepository tvSeriesRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository, MovieRepository movieRepository, TVSeriesRepository tvSeriesRepository) {
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
        this.tvSeriesRepository = tvSeriesRepository;
    }

    @Override
    public List<DirectorDTO> getAllDirectors() {
        return directorRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DirectorDTO getDirectorById(Long id) {
        Optional<Director> directorOptional = directorRepository.findById(id);

        if (directorOptional.isPresent()) {
            Director director = directorOptional.get();
            return convertToDto(director);
        } else {
            throw new NoSuchElementException("Director not found with id: " + id);
        }
    }

    @Override
    public DirectorDTO convertToDto(Director director) {
        return new DirectorDTO(
                director.getId(),
                director.getDirectorName(),
                director.getDirectorBirthdate(),
                director.getDirectorBiography(),
                director.getDirectorImg(),
                director.getImages().stream()
                        .map(i -> new DirectorImageDTO(
                                i.getId(),
                                i.getImage(),
                                director
                        )).collect(Collectors.toList()));
    }
    @Override
    public List<Movie> getMoviesByDirectorId(Long directorId) {
        return movieRepository.findMoviesByDirectorId(directorId);
    }
    @Override
    public List<TVSeries> getSeriesByDirectorId(Long directorId) {
        return tvSeriesRepository.findSeriesByDirectorId(directorId);
    }

    @Override
    public List<Director> findAll() {
        return directorRepository.findAll();
    }

    @Override
    public Director findById(Long id) {
        return directorRepository.findById(id).orElse(null);
    }

    @Override
    public void addDirector(Director director) {
        directorRepository.save(director);
    }

    @Override
    public Director convertDtoToDirector(DirectorDTO directorDTO) {
        Director director = new Director();
        director.setId(directorDTO.getId());
        director.setDirectorName(directorDTO.getDirectorName());
        director.setDirectorBirthdate(directorDTO.getDirectorBirthdate());
        director.setDirectorBiography(directorDTO.getDirectorBiography());
        director.setDirectorImg(directorDTO.getDirectorImg());
        return director;
    }

    @Override
    public void updateDirector(Director updatedDirector) {
        Optional<Director> directorOptional = directorRepository.findById(updatedDirector.getId());

        if (directorOptional.isPresent()) {
            Director director = directorOptional.get();
            director.setDirectorName(updatedDirector.getDirectorName());
            director.setDirectorBirthdate(updatedDirector.getDirectorBirthdate());
            director.setDirectorBiography(updatedDirector.getDirectorBiography());
            director.setDirectorImg(updatedDirector.getDirectorImg());

            directorRepository.save(director);
        } else {
            throw new NoSuchElementException("Director not found with id: " + updatedDirector.getId());
        }
    }

    @Override
    @Transactional
    public void deleteDirector(Long id) {
        Optional<Director> directorOptional = directorRepository.findById(id);
        if (directorOptional.isPresent()) {
            directorRepository.delete(directorOptional.get());
        } else {
            throw new IllegalArgumentException("Director not found");
        }
    }

    @Override
    public Director saveDirectors(String name, MultipartFile file, LocalDate date, String bio) throws IOException {
        String fileName = file.getOriginalFilename();
        Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        Director director = new Director();
        director.setDirectorName(name);
        director.setDirectorBiography(bio);
        director.setDirectorBirthdate(date);
        director.setDirectorImg(fileName);

        return directorRepository.save(director);
    }
}
