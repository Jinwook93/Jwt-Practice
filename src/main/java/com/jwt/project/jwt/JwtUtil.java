package com.jwt.project.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

	private SecretKey secretKey;

	
	public JwtUtil(@Value("${spring.jwt.key}") String secret) {
	    // application.yml 또는 application.properties에 정의된 spring.jwt.secret 값을 주입받음
	    // 예: spring.jwt.secret: my-secret-key
	    // secret 변수에는 "my-secret-key" 문자열이 들어옴
	    secretKey = new SecretKeySpec(
	        // 문자열 secret을 UTF-8 인코딩된 바이트 배열로 변환
	        secret.getBytes(StandardCharsets.UTF_8),
	        // JWT 라이브러리에서 HS256 알고리즘 이름을 가져옴 ("HmacSHA256")
	        Jwts.SIG.HS256.key().build().getAlgorithm()
	    );
	    // SecretKeySpec을 이용해 HS256 알고리즘용 SecretKey 객체를 생성
	    // 이 secretKey는 JWT 서명 및 검증에 사용됨
	}
	
	public String getUsername(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username",String.class);
	}
	
	public String getRole(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
	}
	
	 public Boolean isExpired(String token) {

	        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
	    }

	public String createToken(String username, String role, Long expiredMs){
			return Jwts.builder()
					.claim("username", username)
	                .claim("role", role)
	                .issuedAt(new Date(System.currentTimeMillis()))
	                .expiration(new Date(System.currentTimeMillis() + expiredMs))
	                .signWith(secretKey)
	                .compact();
	}
}
