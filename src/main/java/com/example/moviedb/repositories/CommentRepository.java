package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByNewsId(Long newsId);
    void deleteByNewsId(Long newsId);
    void deleteByUserId(Long userId);
}
