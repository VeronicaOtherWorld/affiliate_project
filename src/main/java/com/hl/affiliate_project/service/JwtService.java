package com.hl.affiliate_project.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
	// private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // **安全密钥**
	@Value("${jwt.secret}")
	private String secretKey;

	private Key getSigningKey() {
		System.out.println("===========================");
		System.out.println("🔹 当前的 Secret Key：" + secretKey);
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)); // ✅ 固定密钥
	}

	// 生成一个新的 JWT，并签名。
	public String generateToken(String email) {
		System.out.println("🔹 生成 JWT 的 email：" + email);
		return Jwts.builder()
						.setSubject(email)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // **1小时有效期**
						// .signWith(SECRET_KEY) // **正确的 signWith() 语法**
						.signWith(getSigningKey(), SignatureAlgorithm.HS256) // **正确的 signWith() 语法**
						.compact();
	}

	// 从 JWT 中提取出用户的 email。
	public String extractUsername(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
							// .setSigningKey(SECRET_KEY)
							.setSigningKey(getSigningKey())
							.build()
							.parseClaimsJws(token)
							.getBody();

			System.out.println("🔍 解析出的用户 email：" + claims.getSubject()); // 👉 确保能解析到 email
			return claims.getSubject();
		} catch (Exception e) {
			System.out.println("❌ JWT 解析失败：" + e.getMessage());
			return null;
		}
	}

	// 判断 JWT 是否已经过期。
	// public boolean isTokenExpired(String token) {
	// 	return extractExpiration(token).before(new Date());
	// }

	// 从 JWT 中提取出过期时间。
	// private Date extractExpiration(String token) {
	// 	return Jwts.parserBuilder()
	// 					.setSigningKey(SECRET_KEY)
	// 					.build()
	// 					.parseClaimsJws(token)
	// 					.getBody()
	// 					.getExpiration();
	// }

	// public boolean validateToken(String token) {
	// 	return !isTokenExpired(token);
	// }
}