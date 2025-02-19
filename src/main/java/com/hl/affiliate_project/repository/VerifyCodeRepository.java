package com.hl.affiliate_project.repository;

import com.hl.affiliate_project.model.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {
	Optional<VerifyCode> findByEmailAndCode(String email, String code);


	List<VerifyCode> findByEmailAndStatus(String email, VerifyCode.Status status);
}