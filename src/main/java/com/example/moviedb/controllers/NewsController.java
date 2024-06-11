package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.CommentDTO;
import com.example.moviedb.models.DTOs.NewsDTO;
import com.example.moviedb.models.entity.Comment;
import com.example.moviedb.models.entity.News;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.repositories.NewsRepository;
import com.example.moviedb.services.CommentService;
import com.example.moviedb.services.FileStorageService;
import com.example.moviedb.services.NewsService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
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
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;
    private final CurrentUser currentUser;
    private final CommentService commentService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final NewsRepository newsRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    public NewsController(NewsService newsService, CurrentUser currentUser, CommentService commentService, UserService userService, FileStorageService fileStorageService, NewsRepository newsRepository) {
        this.newsService = newsService;
        this.currentUser = currentUser;
        this.commentService = commentService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.newsRepository = newsRepository;
    }

    @GetMapping
    public String getNews(Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        List<NewsDTO> news = newsService.getNews();

        model.addAttribute("news", news);
        return "news";
    }

    @PostMapping(value = "comments/{newsId}")
    public String addCommentToNews(@PathVariable Long newsId, @RequestParam("comment") String commentText, RedirectAttributes redirectAttributes) {
        if (commentText == null || commentText.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Comment cannot be empty.");
            return "redirect:/news/" + newsId;
        }


        CurrentUser currentUser = userService.getCurrentUser();
        Comment comment = new Comment();
        comment.setComment(commentText);
        comment.setPostDate(LocalDate.now());

        if (currentUser.isLogged()) {
            User user = new User();
            user.setId(currentUser.getId());

            commentService.addCommentToNews(newsId, comment, user);
        } else {
            return "redirect:/users/login";
        }

        return "redirect:/news/" + newsId;
    }

    @PostMapping("/delete-comment/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long newsId) {
        CurrentUser currentUser = userService.getCurrentUser();

        if (currentUser.isLogged() || currentUser.isAdmin()) {
            User user = new User();
            user.setId(currentUser.getId());

            boolean isDeleted = commentService.deleteComment(commentId, user);
            if (!isDeleted) {
                return "redirect:/news/" + newsId + "?error=not-authorized";
            }
        } else {
            return "redirect:/users/login";
        }

        return "redirect:/news/" + newsId;
    }

    @GetMapping("/{id}")
    public String getNewsDetails(Model model, @PathVariable Long id) {
        NewsDTO newsById = newsService.getNewsById(id);
        model.addAttribute("newsById", newsById);

        List<CommentDTO> commentsForNews = commentService.getCommentsForNews(id);
        model.addAttribute("commentsForNews", commentsForNews);

        return "news-details";
    }

    @GetMapping("/add-form")
    public String showAddNewsForm(Model model) {
        model.addAttribute("news", new News());
        return "add-news";
    }

    @PostMapping("/add")
    public String addNews(@RequestParam("newsTitle") String title,
                                          @RequestParam("newsContent") String content,
                                          @RequestParam("date") LocalDate date,
                                          @RequestParam("image") MultipartFile file) throws IOException {
        newsService.saveNews(title, content, date, file);
        return "redirect:/news/add-form";
    }


    @PostMapping("/update")
    public String updateNews(@ModelAttribute News updatedNews,
                             @RequestParam("image") MultipartFile file) throws IOException {
        Long newsId = updatedNews.getId();

        NewsDTO newsDTO = newsService.getNewsById(newsId);
        News existingNews = newsService.convertDtoToNews(newsDTO);

        existingNews.setNewsTitle(updatedNews.getNewsTitle());
        existingNews.setNewsContent(updatedNews.getNewsContent());
        existingNews.setDate(updatedNews.getDate());

        if (!file.isEmpty()) {
            // Изтриване на старото изображение, ако съществува
            String oldFileName = existingNews.getImageName();
            if (oldFileName != null && !oldFileName.isEmpty()) {
                fileStorageService.deleteFile(oldFileName);
            }

            // Запазване на новия файл
            String fileName = file.getOriginalFilename();
            Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            // Актуализиране на името на изображението в обекта
            existingNews.setImageName(fileName);
        }

        // Запазване на актуализираните данни в базата
        newsService.updateNews(existingNews);

        return "redirect:/news/" + newsId;
    }
    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();

        if (currentUser.isAdmin()) {
            try {
                newsService.deleteNews(id);
                redirectAttributes.addFlashAttribute("message", "News successfully deleted.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error deleting News: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to delete News.");
        }

        return "redirect:/news";
    }

    @PostMapping("/reply/{commentId}")
    public String replyToComment(@PathVariable Long commentId, @RequestParam String replyContent, RedirectAttributes redirectAttributes) {
        CurrentUser currentUser = userService.getCurrentUser();

        if (currentUser.isLogged()) {
            try {
                Comment parentComment = commentService.findById(commentId);
                User user = new User();
                user.setId(currentUser.getId());

                Comment reply = new Comment();
                reply.setComment(replyContent);
                reply.setPostDate(LocalDate.now());
                reply.setUser(user);
                reply.setParentComment(parentComment);

                commentService.addReply(reply);
                redirectAttributes.addFlashAttribute("message", "Reply successfully added.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error adding reply: " + e.getMessage());
            }
        } else {
            return "redirect:/users/login";
        }

        return "redirect:/news/" + commentService.findById(commentId).getNews().getId();
    }

}
