package com.hl.affiliate_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	// **注入 JwtAuthenticationFilter**
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
						.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
						.csrf(csrf -> csrf.disable()) // **禁用 CSRF**
						.authorizeHttpRequests(auth -> auth
										.requestMatchers("/api/person/register",
														"/api/person/login",
														"/api/verify/send").permitAll() // **允许不带 Token 访问**
										.anyRequest().authenticated()
						)
						.sessionManagement(
										session ->
														session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // **无状态**

		return http.build();
	}
}