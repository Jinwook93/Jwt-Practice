package com.jwt.project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.project.dto.CustomUserDetails;
import com.jwt.project.entity.UserEntity;
import com.jwt.project.repository.UserRepository;

@Service
public class CustomDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	
	public CustomDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//DB에서 조회
        UserEntity userData = userRepository.findByUsername(username);
		
        if(userData != null) {
        	//UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }
        
        
		return null;
	}

}
