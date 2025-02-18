package com.hl.affiliate_project.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "verify_code", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "code"})})
public class VerifyCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String email;

	@Column(nullable = false, length = 6)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.PENDING;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime expireTime;

	public VerifyCode() {
		// 默认构造函数，JPA 需要
	}

	public VerifyCode(String email, String code, LocalDateTime expireTime) {
		this.email = email;
		this.code = code;
		this.status = Status.PENDING;
		this.createdAt = LocalDateTime.now();
		this.expireTime = Timestamp.valueOf(expireTime).toLocalDateTime(); // 解决 LocalDateTime 和 Date 类型不匹配的问题
	}

	public enum Status {
		PENDING, USED, EXPIRED
	}

	// Getters & Setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getExpireTime() { return expireTime; }
	public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
}