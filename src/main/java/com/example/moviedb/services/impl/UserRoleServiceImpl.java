package com.example.moviedb.services.impl;

import com.example.moviedb.services.UserRoleService;
import com.example.moviedb.models.entity.UserRole;
import com.example.moviedb.models.enums.RoleEnum;
import com.example.moviedb.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

//    @Override
//    public UserRole getRole(String name) {
//        return this.userRoleRepository.findByName(RoleEnum.valueOf(name));
//    }

    @Override
    public UserRole getRole(String name) {
        return this.userRoleRepository.findFirstByName(RoleEnum.valueOf(name));
    }
}
