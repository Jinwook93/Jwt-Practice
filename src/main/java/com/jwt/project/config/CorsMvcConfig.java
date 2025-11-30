package com.jwt.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//컨트롤러 데이터에서 접근 방법 지정
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000");		//프론트단 주소
		
//		WebMvcConfigurer.super.addCorsMappings(registry);
	}

}
