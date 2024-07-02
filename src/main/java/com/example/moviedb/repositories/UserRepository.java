package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.User;
import com.example.moviedb.models.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User saveAndFlush(User user);
    void deleteById(Long id);
    List<User> findByRole_Name(RoleEnum roleName);
}
