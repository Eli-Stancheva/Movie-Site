package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String name);
}
