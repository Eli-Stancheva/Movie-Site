package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.DirectorDTO;
import com.example.moviedb.models.entity.Director;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.services.DirectorService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;
    private final UserService userService;

    @Autowired
    public DirectorController(DirectorService directorService, UserService userService) {
        this.directorService = directorService;
        this.userService = userService;
    }

    @GetMapping("/allDirectors")
    public String getDirectors(Model model, HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        List<DirectorDTO> directors = directorService.getAllDirectors();
        model.addAttribute("directors", directors);
        return "directors";
    }

    @GetMapping("/{id}")
    public String getDirectorsDetails(Model model, @PathVariable Long id) {
        DirectorDTO directorById = directorService.getDirectorById(id);
        List<Movie> directorMovies = directorService.getMoviesByDirectorId(id);
        List<TVSeries> directorSeries = directorService.getSeriesByDirectorId(id);

        model.addAttribute("directorById", directorById);
        model.addAttribute("directorMovies", directorMovies);
        model.addAttribute("directorSeries", directorSeries);

        return "director-details";
    }

    @GetMapping("/all")
    public String getDirectors(Model model) {
        List<Director> allDirectors = directorService.findAll();
        model.addAttribute("allDirectors", allDirectors);
        return "add-movies";
    }

    @GetMapping("/add-form")
    public String showAddDirectorsForm(Model model) {
        model.addAttribute("director", new Director());
        return "add-directors";
    }

    @PostMapping("/add")
    public String addDirectors(@ModelAttribute DirectorDTO directorDTO) {
        String name = directorDTO.getDirectorName();
        LocalDate birth = directorDTO.getDirectorBirthdate();
        String bio = directorDTO.getDirectorBiography();
        String img = directorDTO.getDirectorImg();

        Director director = new Director(name, birth, bio, img);

        directorService.addDirector(director);

        return "add-directors";
    }

    @PostMapping("/update")
    public String updateDirector(@ModelAttribute Director updatedDirector) {
        Long directorId = updatedDirector.getId();

        DirectorDTO directorDTO = directorService.getDirectorById(directorId);
        Director existingDirector = directorService.convertDtoToDirector(directorDTO);

        existingDirector.setDirectorName(updatedDirector.getDirectorName());
        existingDirector.setDirectorBirthdate(updatedDirector.getDirectorBirthdate());
        existingDirector.setDirectorBiography(updatedDirector.getDirectorBiography());
        existingDirector.setDirectorImg(updatedDirector.getDirectorImg());

        directorService.updateDirector(existingDirector);

        return "redirect:/directors/" + directorId;
    }

    @PostMapping("/delete/{id}")
    public String deleteDirector(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();

        if (currentUser.isAdmin()) {
            try {
                directorService.deleteDirector(id);
                redirectAttributes.addFlashAttribute("message", "Director successfully deleted.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error deleting Director: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to delete Director.");
        }

        return "redirect:/directors/allDirectors";
    }
}

