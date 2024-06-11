package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.ActorDTO;
import com.example.moviedb.models.DTOs.DirectorDTO;
import com.example.moviedb.models.DTOs.NewsDTO;
import com.example.moviedb.models.entity.*;
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
import java.util.List;

@Controller
@RequestMapping("/actors")
public class ActorController {
    private final ActorService actorService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public ActorController(ActorService actorService, UserService userService, FileStorageService fileStorageService) {
        this.actorService = actorService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
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
    public String addActors(@RequestParam("actorName") String name,
                            @RequestParam("actorImg") MultipartFile file,
                            @RequestParam("actorBirthdate") LocalDate date,
                            @RequestParam("actorBiography") String bio) throws IOException {
        // Запазване на файла на файловата система
        String fileName = file.getOriginalFilename();
        Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        // Запазване на метаданните в базата данни
        Actor actor = new Actor();
        actor.setActorName(name);
        actor.setActorImg(fileName);
        actor.setActorBirthdate(date);
        actor.setActorBiography(bio);

        actorService.save(actor);

        return "redirect:/actors/add-form";
    }


    @PostMapping("/update")
    public String updateActors(@ModelAttribute Actor updatedActor, @RequestParam("file") MultipartFile file) throws IOException {
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

//    @PostMapping("/update")
//    public String updateActors(@ModelAttribute Actor updatedActors,
//                             @RequestParam("actorImg") MultipartFile file) throws IOException {
//        Long actorsId = updatedActors.getId();
//
//        ActorDTO actorDTO = actorService.getActorById(actorsId);
//        Actor existingActors = actorService.convertDtoToActor(actorDTO);
//
//        // Актуализиране на заглавието, съдържанието и датата
//        existingActors.setActorName(updatedActors.getActorName());
//        existingActors.setActorBirthdate(updatedActors.getActorBirthdate());
//        existingActors.setActorBiography(updatedActors.getActorBiography());
//
//        // Проверка дали има ново изображение
//        if (!file.isEmpty()) {
//            // Изтриване на старото изображение, ако съществува
//            String oldFileName = existingActors.getActorImg();
//            if (oldFileName != null && !oldFileName.isEmpty()) {
//                fileStorageService.deleteFile(oldFileName);
//            }
//
//            // Запазване на новия файл
//            String fileName = file.getOriginalFilename();
//            Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
//            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
//
//            // Актуализиране на името на изображението в обекта
//            existingActors.setActorImg(fileName);
//        }
//
//        // Запазване на актуализираните данни в базата
//        actorService.updateActor(existingActors);
//
//        return "redirect:/actors/" + actorsId;
//    }

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
