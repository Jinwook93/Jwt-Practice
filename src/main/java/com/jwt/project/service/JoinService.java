package com.jwt.project.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.project.dto.JoinDTO;
import com.jwt.project.entity.UserEntity;
import com.jwt.project.repository.UserRepository;

@Service
public class JoinService {

	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	
	public void  joinProcess (JoinDTO joinDTO) {
		
		Boolean result =userRepository.existsByUsername(joinDTO.getUsername());
		if(result) {
			return;
		}
		String encodedPassword = bCryptPasswordEncoder.encode(joinDTO.getUsername());
		
		UserEntity newuser = new UserEntity(joinDTO);
		newuser.setRole("ROLE_ADMIN");
		newuser.setPassword(encodedPassword);
		
		
		userRepository.save(newuser);
		
		
	}
	
	
}
