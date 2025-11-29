package com.jwt.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.project.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}