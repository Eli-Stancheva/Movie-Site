package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.ActorDTO;
import com.example.moviedb.models.DTOs.ActorImageDTO;
import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.repositories.ActorRepository;
import com.example.moviedb.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public List<ActorDTO> getAllActors() {
        return actorRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ActorDTO getActorById(Long id) {
        Optional<Actor> actorOptional = actorRepository.findById(id);

        if (actorOptional.isPresent()) {
            Actor actor = actorOptional.get();
            return convertToDto(actor);
        } else {
            throw new NoSuchElementException("Actor not found with id: " + id);
        }
    }

    @Override
    public void addActor(Actor actor) {
        actorRepository.save(actor);
    }

    @Override
    public void updateActor(Actor updatedActor) {
        Optional<Actor> actorOptional = actorRepository.findById(updatedActor.getId());

        if (actorOptional.isPresent()) {
            Actor actor = actorOptional.get();
            actor.setActorName(updatedActor.getActorName());
            actor.setActorBirthdate(updatedActor.getActorBirthdate());
            actor.setActorBiography(updatedActor.getActorBiography());
            actor.setActorImg(updatedActor.getActorImg());

            actorRepository.save(actor);
        } else {
            throw new NoSuchElementException("Actor not found with id: " + updatedActor.getId());
        }
    }


    @Override
    @Transactional
    public void deleteActor(Long id) {
        Optional<Actor> actorOptional = actorRepository.findById(id);
        if (actorOptional.isPresent()) {
            actorRepository.delete(actorOptional.get());
        } else {
            throw new IllegalArgumentException("Actor not found");
        }
    }

    @Override
    public ActorDTO convertToDto(Actor actor) {
        return new ActorDTO(
                actor.getId(),
                actor.getActorName(),
                actor.getActorBirthdate(),
                actor.getActorBiography(),
                actor.getActorImg(),
                actor.getImages().stream()
                        .map(i -> new ActorImageDTO(
                                i.getId(),
                                i.getImage(),  // Използвайте правилния гетър за изображението
                                actor
                        )).collect(Collectors.toList())  // Завършете стрийма, като съберете резултатите в списък
        );
    }

    @Override
    public Set<ActorDTO> getActors() {
        List<Actor> actors = actorRepository.findAll(); // Извличане на данни от базата данни
        return actors.stream()
                .map(actor -> new ActorDTO(
                        actor.getId(),
                        actor.getActorName(),
                        actor.getActorBirthdate(),
                        actor.getActorBiography(),
                        actor.getActorImg(),
                        actor.getImages().stream()
                                .map(image -> new ActorImageDTO(
                                        image.getId(),
                                        image.getImage(), // Уверете се, че използвате правилния гетър за изображението
                                        actor
                                ))
                                .collect(Collectors.toList()) // Преобразуване на стрийма в списък
                ))
                .collect(Collectors.toSet()); // Събиране на резултатите в множество
    }


//    @Override
//    public Set<ActorDTO> getActors() {
//        List<Actor> actors = actorRepository.findAll(); // Извличане на данни от базата данни
//        return actors.stream()
//                .map(actor -> new ActorDTO(
//                        actor.getId(),
//                        actor.getActorName(),
//                        actor.getActorBirthdate(),
//                        actor.getActorBiography(),
//                        actor.getActorImg(),
//                        images))
//                .collect(Collectors.toSet());
//    }

    @Override
    public Actor convertDtoToActor(ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setId(actorDTO.getId());
        actor.setActorName(actorDTO.getActorName());
        actor.setActorBirthdate(actorDTO.getActorBirthdate());
        actor.setActorBiography(actorDTO.getActorBiography());
        actor.setActorImg(actorDTO.getActorImg());
        return actor;
    }

    @Override
    public Actor findByName(String name) {
        return actorRepository.findByActorName(name);
    }

    @Override
    public void save(Actor actor) {
        actorRepository.save(actor);
    }

    @Override
    public List<Movie> getMoviesByActorId(Long actorId) {
        return actorRepository.findMoviesByActorId(actorId);
    }

    @Override
    public List<TVSeries> getSeriesByActorId(Long actorId) {
        return actorRepository.findTVSeriesByActorId(actorId);
    }
}