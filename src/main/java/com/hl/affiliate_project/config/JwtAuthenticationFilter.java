package com.hl.affiliate_project.config;

import com.hl.affiliate_project.model.PersonInfo;
import com.hl.affiliate_project.service.JwtService;
import com.hl.affiliate_project.service.PersonInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	@Lazy
	private PersonInfoService personInfoService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {

		String token = extractToken(request);
		System.out.println("request1111"+ request);
		// if (token != null && jwtService.validateToken(token)) {
			if (token != null) {

			String username = jwtService.extractUsername(token);
			logger.info("Token valid for user: {}" + username);
			PersonInfo user = personInfoService.getPersonByEmail(username)
							.orElseThrow(() -> new UsernameNotFoundException("User not found"));

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							user, null, Collections.emptyList());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			logger.warn("Token is invalid or missing");
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String authorizationHeader = request.getHeader("Authorization");
		System.out.println("🔹 Authorization Header: " + authorizationHeader);
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7); // 获取 Bearer 后面的 token
		}
		return null;
	}
}