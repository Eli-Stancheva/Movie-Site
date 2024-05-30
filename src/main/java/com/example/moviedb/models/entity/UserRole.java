package com.example.moviedb.models.entity;

import com.example.moviedb.models.enums.RoleEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "Roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleEnum name;

    public UserRole() {}

    public UserRole(RoleEnum name) {
        this.name = name;
    }

    public RoleEnum getName() {
        return name;
    }

    public UserRole setName(RoleEnum name) {
        this.name = name;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserRole setId(Long id) {
        this.id = id;
        return this;
    }
}
