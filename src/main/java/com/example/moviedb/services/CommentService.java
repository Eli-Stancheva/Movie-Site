package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.CommentDTO;
import com.example.moviedb.models.entity.Comment;
import com.example.moviedb.models.entity.User;

import java.util.List;

public interface CommentService {
    void addCommentToNews(Long newsId, Comment comment, User user);
    List<CommentDTO> getCommentsForNews(Long newsId);
//    boolean deleteComment(Long commentId, User user);
    boolean deleteComment(Long commentId, Long userId, boolean isAdmin);
    Comment findById(Long commentId);
    void addReply(Comment reply);
    boolean isCommentAuthor(Long commentId, Long userId);
}
