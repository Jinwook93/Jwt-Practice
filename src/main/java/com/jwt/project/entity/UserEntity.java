package com.jwt.project.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jwt.project.dto.JoinDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;

    private String role;
    
    
    public UserEntity(JoinDTO joinDTO){
    	
    	this.username = joinDTO.getUsername();
//    	this.role = "ROLE_ADMIN";
    }
    
    
}