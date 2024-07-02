package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.CommentDTO;
import com.example.moviedb.models.entity.Comment;
import com.example.moviedb.models.entity.News;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.enums.RoleEnum;
import com.example.moviedb.repositories.CommentRepository;
import com.example.moviedb.repositories.NewsRepository;
import com.example.moviedb.services.CommentService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(NewsRepository newsRepository, CommentRepository commentRepository, UserService userService) {
        this.newsRepository = newsRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
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


            // Добави отговорите към коментара
            List<CommentDTO> replyDTOs = new ArrayList<>();
            for (Comment reply : comment.getReplies()) {
                CommentDTO replyDTO = new CommentDTO();

                replyDTO.setId(reply.getId());
                replyDTO.setComment(reply.getComment());
                replyDTO.setPostDate(reply.getPostDate());
                replyDTO.setUser(reply.getUser());

                replyDTOs.add(replyDTO);
            }
            commentDTO.setReplies(replyDTOs);

            commentDTOs.add(commentDTO);
        }

        return commentDTOs;
    }

//    @Override
//    public boolean deleteComment(Long commentId, User user) {
//        Comment comment = commentRepository.findById(commentId).orElse(null);
//
//        if (comment != null && comment.getUser().getId().equals(user.getId())) {
//            commentRepository.delete(comment);
//            return true;
//        } else if (user.getRole() != null && user.getRole().getName().equals("ADMIN")) {
//            commentRepository.delete(comment);
//            return true;
//        }
//
//        if (comment != null && comment.getUser().getId().equals(user.getId()) || user.getRole().getName().equals("ADMIN")) {
//            commentRepository.delete(comment);
//            return true;
//        }
//
//        return false;
//    }

    @Override
    public boolean deleteComment(Long commentId, Long userId, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return false;
        }

        User user = comment.getUser();
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            throw new IllegalStateException("User role is not properly set for user ID: " + userId);
        }

        if (isAdmin || user.getId().equals(userId)) {
            commentRepository.delete(comment);
            return true;
        }

        return false;
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
    }

    public void addReply(Comment reply) {
        commentRepository.save(reply);
    }

    @Override
    public boolean isCommentAuthor(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        return comment != null && comment.getUser().getId().equals(userId);
    }
}
