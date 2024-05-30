package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.ActorDTO;
import com.example.moviedb.models.DTOs.MovieDTO;
import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.services.ActorService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/actors")
public class ActorController {
    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/allActors")
    public String getActors(Model model, HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        List<ActorDTO> actors = actorService.getAllActors();
        model.addAttribute("actors", actors);
        return "actors";
    }

    @GetMapping("/{id}")
    public String getActorsDetails(Model model, @PathVariable Long id) {
        ActorDTO actorById = actorService.getActorById(id);
        List<Movie> actorMovies = actorService.getMoviesByActorId(id);
        List<TVSeries> actorSeries = actorService.getSeriesByActorId(id);
        model.addAttribute("actorById", actorById);
        model.addAttribute("actorMovies", actorMovies);
        model.addAttribute("actorSeries", actorSeries);
        return "actor-details";
    }

    @GetMapping("/add-form")
    public String showAddActorsForm(Model model) {
        model.addAttribute("actor", new Actor());
        return "add-actors";
    }

    @PostMapping("/add")
    public String addActors(@ModelAttribute ActorDTO actorDTO, Model model) {
        String name = actorDTO.getActorName();
        LocalDate birth = actorDTO.getActorBirthdate();
        String bio = actorDTO.getActorBiography();
        String img = actorDTO.getActorImg();

        Actor actor = new Actor(name, birth, bio, img);

        actorService.addActor(actor);

        model.addAttribute("successMessage", "Филмът е успешно добавен!");

        return "add-actors";
    }

    @PostMapping("/update")
    public String updateActor(@ModelAttribute Actor updatedActor) {
        Long actorId = updatedActor.getId();

        ActorDTO actorDTO = actorService.getActorById(actorId);
        Actor existingActor = actorService.convertDtoToActor(actorDTO);

        existingActor.setActorName(updatedActor.getActorName());
        existingActor.setActorBirthdate(updatedActor.getActorBirthdate());
        existingActor.setActorBiography(updatedActor.getActorBiography());
        existingActor.setActorImg(updatedActor.getActorImg());

        actorService.updateActor(existingActor);

        return "redirect:/actors/" + actorId;
    }
}
