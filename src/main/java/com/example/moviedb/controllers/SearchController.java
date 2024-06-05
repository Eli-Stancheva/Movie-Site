package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.MovieDTO;
import com.example.moviedb.models.DTOs.TVSeriesDTO;
import com.example.moviedb.services.MoviesService;
import com.example.moviedb.services.TVSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class SearchController {
    private final MoviesService movieService;
    private final TVSeriesService tvSeriesService;

    @Autowired
    public SearchController(MoviesService movieService, TVSeriesService tvSeriesService) {
        this.movieService = movieService;
        this.tvSeriesService = tvSeriesService;
    }


//@GetMapping("/search")
//    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
//        if (query != null && !query.trim().isEmpty()) {
//            List<MovieDTO> movieResults = movieService.searchMovieByTitleOrActorOrDirectorOrCategory(query);
//            List<TVSeriesDTO> seriesResults = tvSeriesService.searchSeriesIgnoreCase(query);
//
//            if (movieResults.size() == 1 && seriesResults.isEmpty()) {
//                return "redirect:/movies/" + movieResults.get(0).getId();
//            } else if (seriesResults.size() == 1 && movieResults.isEmpty()) {
//                return "redirect:/series/" + seriesResults.get(0).getId();
//            }
//
//            // Ако не е намерен само един филм или сериал, покажи страницата с резултатите от търсенето
//            model.addAttribute("movieResults", movieResults);
//            model.addAttribute("seriesResults", seriesResults);
//        }
//
//        return "searchResults";
//    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
        List<MovieDTO> searchResultsMovies = new ArrayList<>();
        List<TVSeriesDTO> searchResultsSeries = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            searchResultsMovies = movieService.searchMovieByTitleOrActorOrDirectorOrCategory(query);
            searchResultsSeries = tvSeriesService.searchSeriesByTitleOrActorOrDirectorOrCategory(query);
        }


        model.addAttribute("searchResultsMovies", searchResultsMovies);
        model.addAttribute("searchResultsSeries", searchResultsSeries);
        model.addAttribute("searchQuery", query);
        return "searchResults";

    }
}
