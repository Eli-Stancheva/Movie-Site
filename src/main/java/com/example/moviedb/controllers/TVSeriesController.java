package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.TVSeriesDTO;
import com.example.moviedb.models.entity.*;
import com.example.moviedb.services.*;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/series")
public class TVSeriesController {
    private final TVSeriesService tvSeriesService;
    private final UserService userService;
    private final ActorService actorService;
    private final CategoryService categoryService;
    private final DirectorService directorService;
    private final SeriesActorService seriesActorService;
    private final TVSeriesCategoryService tvSeriesCategoryService;
    private final FileStorageService fileStorageService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public TVSeriesController(TVSeriesService tvSeriesService, UserService userService, ActorService actorService, CategoryService categoryService, DirectorService directorService, SeriesActorService seriesActorService, TVSeriesCategoryService tvSeriesCategoryService, FileStorageService fileStorageService) {
        this.tvSeriesService = tvSeriesService;
        this.userService = userService;
        this.actorService = actorService;
        this.categoryService = categoryService;
        this.directorService = directorService;
        this.seriesActorService = seriesActorService;
        this.tvSeriesCategoryService = tvSeriesCategoryService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/allSeries")
    public String getTvSeries(Model model, HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        List<TVSeriesDTO> allSeries = tvSeriesService.getTVSeries();
        model.addAttribute("allSeries", allSeries);
        return "series";
    }

    @GetMapping("/bestSeries")
    public String getBestSeries(Model model){
        List<TVSeriesDTO> allSeries = tvSeriesService.getTVSeries();
        List<TVSeriesDTO> bestSeries = allSeries.stream()
                .filter(s -> s.getRating() > 8.0)
                .sorted(Comparator.comparingDouble(TVSeriesDTO::getRating).reversed())
                .collect(Collectors.toList());
        model.addAttribute("bestSeries", bestSeries);
        return "best-series";
    }

    @GetMapping("/{id}")
    public String getSeriesDetails(Model model, @PathVariable Long id) {
        TVSeriesDTO seriesById = tvSeriesService.getSeriesById(id);
        model.addAttribute("seriesById", seriesById);

        CurrentUser currentUser = userService.getCurrentUser();
        int userRating = tvSeriesService.getUserRatingForSeries(id, currentUser.getId());
        model.addAttribute("userRating", userRating);

        double averageRating = tvSeriesService.getAverageRatingForSeries(id);
        model.addAttribute("averageRating", averageRating);

        List<TVSeries> similarSeries = new ArrayList<>();
        if (!currentUser.isAdmin()) {
            similarSeries = tvSeriesService.findSimilarSeries(id);
        }
        model.addAttribute("similarSeries", similarSeries);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String releaseYear = seriesById.getReleaseDate().format(formatter);
        model.addAttribute("releaseYear", releaseYear);

        List<Director> allDirectors = directorService.findAll();
        model.addAttribute("allDirectors", allDirectors);
        return "series-details";
    }

    @PostMapping("/rateSeries/{id}")
    public String rateSeries(@PathVariable Long id, @RequestParam int rating) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        tvSeriesService.rateSeries(id, rating, user);

        return "redirect:/series/" + id;
    }

    @PostMapping("/delete-series-rating/{id}")
    public String deleteSeriesRating(@PathVariable Long id) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        tvSeriesService.removeSeriesRating(id, user);

        return "redirect:/";
    }

    @PostMapping("/add/{seriesId}")
    public String addToSeriesWatchlist(@PathVariable Long seriesId, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        try {
            tvSeriesService.addToSeriesWatchlist(seriesId, user);
            redirectAttributes.addFlashAttribute("message", "TV Series added to your watchlist.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/series/" + seriesId;
    }

    @PostMapping("/delete-watchlist-series/{id}")
    public String deleteFromSeriesWatchlist(@PathVariable Long id) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        tvSeriesService.removeSeriesFromWatchlist(id, user);

        return "redirect:/series/watchlist-series";
    }

    @GetMapping("/watchlist-series")
    public String getSeriesWatchlist(Model model) {
        CurrentUser currentUser = userService.getCurrentUser();
        List<TVSeries> watchlistSeries = tvSeriesService.getAllWatchlistSeriesForUser(currentUser.getId());
        model.addAttribute("watchlistSeries", watchlistSeries);
        return "watchlist-series";
    }

    @GetMapping("/add-form")
    public String showAddSeriesForm(Model model) {
        List<Director> allDirectors = directorService.findAll();
        model.addAttribute("series", new TVSeries());
        model.addAttribute("allDirectors", allDirectors);
        return "add-series";
    }

//    @PostMapping("/add")
//    public String addSeries(@ModelAttribute TVSeriesDTO seriesDTO, @RequestParam Long directorId, Model model) {
//        tvSeriesService.addSeries(seriesDTO.getTitle(),
//                seriesDTO.getReleaseDate(),
//                seriesDTO.getSeasons(),
//                seriesDTO.getRating(),
//                seriesDTO.getImageURL(),
//                seriesDTO.getVideoURL(),
//                seriesDTO.getDescription(),
//                directorId);
//
//        model.addAttribute("successMessage", "Successfully added!");
//
//        return "add-series";
//    }


    @PostMapping("/add")
    public String addSeries(@RequestParam("title") String title,
                           @RequestParam("releaseDate") LocalDate date,
                           @RequestParam("seasons") int seasons,
                           @RequestParam("rating") double rating,
                           @RequestParam("imageURL") MultipartFile file,
                           @RequestParam("videoURL") String videoURL,
                           @RequestParam("description") String description,
                           @RequestParam("directorId") Long directorId) throws IOException {
        tvSeriesService.saveSeries(title, date, seasons, rating, file, videoURL, description, directorId);
        return "redirect:/series/add-form";
    }

    @PostMapping("/update/{id}")
    public String updateSeries(@ModelAttribute TVSeries updatedSeries,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("directorId") Long directorId) throws IOException {

        Long seriesId = updatedSeries.getId();

        TVSeriesDTO seriesDTO = tvSeriesService.getSeriesById(seriesId);
        TVSeries existingSeries = tvSeriesService.convertDtoToSeries(seriesDTO);

        existingSeries.setTitle(updatedSeries.getTitle());
        existingSeries.setReleaseDate(updatedSeries.getReleaseDate());
        existingSeries.setSeasons(updatedSeries.getSeasons());
        existingSeries.setRating(updatedSeries.getRating());
        existingSeries.setVideoURL(updatedSeries.getVideoURL());
        existingSeries.setDescription(updatedSeries.getDescription());

        if (!file.isEmpty()) {
            String oldFileName = existingSeries.getImageURL();
            if (oldFileName != null && !oldFileName.isEmpty()) {
                fileStorageService.deleteFile(oldFileName);
            }

            String fileName = file.getOriginalFilename();
            Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            existingSeries.setImageURL(fileName);
        }

        tvSeriesService.updateSeries(existingSeries.getId(),
                existingSeries.getTitle(),
                existingSeries.getReleaseDate(),
                existingSeries.getSeasons(),
                existingSeries.getRating(),
                existingSeries.getImageURL(),
                existingSeries.getVideoURL(),
                existingSeries.getDescription(),
                directorId);

        return "redirect:/series/" + seriesId;
    }


    @PostMapping("/add-actors")
    public String addActorToSeries(@RequestParam("seriesId") Long seriesId, @RequestParam("actorName") String actorName) {
        Actor actor = actorService.findByName(actorName);
        if (actor == null) {
            actor = new Actor();
            actor.setActorName(actorName);
            actorService.save(actor);
        }

        seriesActorService.addActorToSeries(seriesId, actor.getId());
        return "redirect:/series/" + seriesId;
    }

    @PostMapping("/add-category")
    public String addCategoryToSeries(@RequestParam("seriesId") Long seriesId, @RequestParam("categoryName") String categoryName) {
        Category category = categoryService.findByName(categoryName);
        if (category == null) {
            category = new Category();
            category.setCategoryName(categoryName);
            categoryService.save(category);
        }

        tvSeriesCategoryService.addCategoryToSeries(seriesId, category.getId());
        return "redirect:/series/" + seriesId;
    }

    @PostMapping("/remove-actor/{seriesId}/{actorId}")
    public String removeActorFromSeries(@PathVariable Long seriesId, @PathVariable Long actorId) {
        tvSeriesService.removeActorFromSeries(seriesId, actorId);
        return "redirect:/series/" + seriesId;
    }

    @PostMapping("/remove-category/{seriesId}/{categoryId}")
    public String removeCategoryFromSeries(@PathVariable Long seriesId, @PathVariable Long categoryId) {
        tvSeriesService.removeCategoryFromSeries(seriesId, categoryId);
        return "redirect:/series/" + seriesId;
    }

    @PostMapping("/delete/{id}")
    public String deleteSeries(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();

        if (currentUser.isAdmin()) {
            try {
                tvSeriesService.deleteSeries(id);
                redirectAttributes.addFlashAttribute("message", "TV Series successfully deleted.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error deleting TV Series: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to delete TV Series.");
        }

        return "redirect:/series/allSeries";
    }

}
