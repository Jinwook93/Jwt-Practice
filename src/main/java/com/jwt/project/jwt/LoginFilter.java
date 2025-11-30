package com.jwt.project.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.project.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		//클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);
		System.out.println(username);
		//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,null);

		//token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

	//로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    	System.out.println("success");
    	
    	CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    	String username = customUserDetails.getUsername();

    	// Authentication 객체에서 사용자 권한(roles, authorities) 목록을 가져옴
    	Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    	// 가져온 권한 목록을 순회하기 위해 Iterator 생성
    	Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

    	// 첫 번째 권한을 꺼내옴 (예: ROLE_USER, ROLE_ADMIN 등)
    	GrantedAuthority auth = iterator.next();


        String role = auth.getAuthority();

        String token = jwtUtil.createToken(username, role, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);
    }

	//로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    	//로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }
}


