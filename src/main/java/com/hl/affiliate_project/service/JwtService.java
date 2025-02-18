package com.hl.affiliate_project.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // **安全密钥**
	// @Value("${jwt.secret}")
	// private String secretKey;

	// 生成一个新的 JWT，并签名。
	public String generateToken(String email) {
		return Jwts.builder()
						.setSubject(email)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // **1小时有效期**
						.signWith(SECRET_KEY, SignatureAlgorithm.HS256) // **正确的 signWith() 语法**
						.compact();
	}

	// 从 JWT 中提取出用户的 email。
	public String extractUsername(String token) {
		return Jwts.parserBuilder()
						.setSigningKey(SECRET_KEY)
						.build()
						.parseClaimsJws(token)
						.getBody()
						.getSubject();
	}

	// 判断 JWT 是否已经过期。
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// 从 JWT 中提取出过期时间。
	private Date extractExpiration(String token) {
		return Jwts.parserBuilder()
						.setSigningKey(SECRET_KEY)
						.build()
						.parseClaimsJws(token)
						.getBody()
						.getExpiration();
	}

	public boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
}