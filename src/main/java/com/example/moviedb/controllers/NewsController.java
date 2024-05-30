package com.example.moviedb.controllers;

import com.example.moviedb.models.DTOs.CommentDTO;
import com.example.moviedb.models.DTOs.DirectorDTO;
import com.example.moviedb.models.DTOs.NewsDTO;
import com.example.moviedb.models.entity.Comment;
import com.example.moviedb.models.entity.Director;
import com.example.moviedb.models.entity.News;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.services.CommentService;
import com.example.moviedb.services.NewsService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;
    private final CurrentUser currentUser;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public NewsController(NewsService newsService, CurrentUser currentUser, CommentService commentService, UserService userService) {
        this.newsService = newsService;
        this.currentUser = currentUser;
        this.commentService = commentService;
        this.userService = userService;
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

        if (currentUser.isLogged()) {
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
    public String addNews(@ModelAttribute NewsDTO newsDTO, Model model) {
        String title = newsDTO.getNewsTitle();
        String content = newsDTO.getNewsContent();
        LocalDate date = newsDTO.getDate();

        News news = new News(title, content, date);
        newsService.addNews(news);
        model.addAttribute("successMessage", "Филмът е успешно добавен!");

        return "add-news";
    }

    @PostMapping("/update")
    public String updateNews(@ModelAttribute News updatedNews) {
        Long newsId = updatedNews.getId();

        NewsDTO newsDTO = newsService.getNewsById(newsId);
        News existingNews = newsService.convertDtoToNews(newsDTO);

        existingNews.setNewsTitle(updatedNews.getNewsTitle());
        existingNews.setNewsContent(updatedNews.getNewsContent());
        existingNews.setDate(updatedNews.getDate());

        newsService.updateNews(existingNews);

        return "redirect:/news/" + newsId;
    }
}
