package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.ActorDTO;
import com.example.moviedb.models.entity.*;
import com.example.moviedb.services.ActorImageService;
import com.example.moviedb.services.ActorService;
import com.example.moviedb.services.FileStorageService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/actors")
public class ActorController {
    private final ActorService actorService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ActorImageService actorImageService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public ActorController(ActorService actorService, UserService userService, FileStorageService fileStorageService, ActorImageService actorImageService) {
        this.actorService = actorService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.actorImageService = actorImageService;
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

    @PostMapping("/addGalleryImages/{actorId}")
    public String addActorGalleryImages(@PathVariable Long actorId, @RequestParam("images") MultipartFile[] images) {
        actorImageService.addActorGalleryImages(actorId, images);
        return "redirect:/actors/{actorId}";
    }

    @PostMapping("/deleteGalleryImage/{actorId}/{imageId}")
    public String deleteActorGalleryImages(@PathVariable Long actorId, @PathVariable Long imageId) {
        actorImageService.deleteActorGalleryImage(actorId, imageId);
        return "redirect:/actors/{actorId}";
    }

    @PostMapping("/add")
    public String addActors(@RequestParam("actorName") String name,
                            @RequestParam("actorImg") MultipartFile file,
                            @RequestParam("actorBirthdate") LocalDate date,
                            @RequestParam("actorBiography") String bio,
                            @RequestParam("images") List<MultipartFile> files) throws IOException {

        Actor actor = actorService.saveActors(name, file, date, bio);
        actorImageService.saveGalleryImages(files, actor);

        return "redirect:/actors/add-form";
    }

    @PostMapping("/update")
    public String updateActors(@ModelAttribute Actor updatedActor,
                               @RequestParam("file") MultipartFile file) throws IOException {
        Long actorId = updatedActor.getId();

        ActorDTO actorDTO = actorService.getActorById(actorId);
        Actor existingActor = actorService.convertDtoToActor(actorDTO);

        existingActor.setActorName(updatedActor.getActorName());
        existingActor.setActorBirthdate(updatedActor.getActorBirthdate());
        existingActor.setActorBiography(updatedActor.getActorBiography());

        if (!file.isEmpty()) {
            String oldFileName = existingActor.getActorImg();
            if (oldFileName != null && !oldFileName.isEmpty()) {
                fileStorageService.deleteFile(oldFileName);
            }

            String fileName = file.getOriginalFilename();
            Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            existingActor.setActorImg(fileName);
        }

        actorService.updateActor(existingActor);

        return "redirect:/actors/" + actorId;
    }


    @PostMapping("/delete/{id}")
    public String deleteActor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();

        if (currentUser.isAdmin()) {
            try {
                actorService.deleteActor(id);
                redirectAttributes.addFlashAttribute("message", "Actor successfully deleted.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error deleting Actor: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to delete actor.");
        }

        return "redirect:/actors/allActors";
    }
}
