package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.CommentDTO;
import com.example.moviedb.models.entity.Comment;
import com.example.moviedb.models.entity.News;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.repositories.CommentRepository;
import com.example.moviedb.repositories.NewsRepository;
import com.example.moviedb.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(NewsRepository newsRepository, CommentRepository commentRepository) {
        this.newsRepository = newsRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void addCommentToNews(Long newsId, Comment comment, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News with id " + newsId + " not found"));

        if (comment == null || comment.getComment() == null || comment.getComment().isEmpty()) {
            throw new IllegalArgumentException("Comment must not be empty");
        }

        comment.setNews(news);
        comment.setUser(user);
        comment.setPostDate(LocalDate.now());

        commentRepository.save(comment);
    }

    @Override
    public List<CommentDTO> getCommentsForNews(Long newsId) {
        List<Comment> comments = commentRepository.findByNewsId(newsId);

        List<CommentDTO> commentDTOs = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();

            commentDTO.setId(comment.getId());
            commentDTO.setComment(comment.getComment());
            commentDTO.setPostDate(comment.getPostDate());
            commentDTO.setUser(comment.getUser());

            commentDTOs.add(commentDTO);
        }

        return commentDTOs;
    }

    @Override
    public boolean deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment != null && comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
            return true;
        }

        return false;
    }
}
