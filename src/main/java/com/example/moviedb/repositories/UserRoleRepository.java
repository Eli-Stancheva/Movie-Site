package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.UserRole;
import com.example.moviedb.models.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(RoleEnum roleEnum);
    UserRole findFirstByName(RoleEnum roleEnum);
}
