package com.jwt.project.config;

import java.util.Collections;
import java.util.List;

import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jwt.project.jwt.JwtFilter;
import com.jwt.project.jwt.JwtUtil;
import com.jwt.project.jwt.LoginFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtil jwtUtil;
	
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
	    configuration.setAllowedMethods(Collections.singletonList("*"));
	    configuration.setAllowCredentials(true);
	    configuration.setAllowedHeaders(Collections.singletonList("*"));
	    configuration.setMaxAge(3600L);
	    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
//		http
//        .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//
//            @Override
//            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//                CorsConfiguration configuration = new CorsConfiguration();
//
//                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//                configuration.setAllowedMethods(Collections.singletonList("*"));
//                configuration.setAllowCredentials(true);
//                configuration.setAllowedHeaders(Collections.singletonList("*"));
//                configuration.setMaxAge(3600L);
//
//									configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//
//                return configuration;
//            }
//        })));
		
		http.cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource ())); // Bean 주입

		
		// csrf disable
		http.csrf((auth) -> auth.disable());
		// From 로그인 방식 disable
		http.formLogin().disable();
		// http basic 인증 방식 disable
		http.httpBasic(auth -> auth.disable());
		// 경로별 인가 작업
		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/login", "/", "/join").permitAll()
				.requestMatchers("/admin").hasRole("ADMIN")
				.anyRequest().authenticated());

		http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
		http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		// 세션 설정
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();

	}



}
