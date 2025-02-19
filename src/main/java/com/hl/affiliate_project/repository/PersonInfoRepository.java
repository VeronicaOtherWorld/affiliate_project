package com.hl.affiliate_project.repository;
import com.hl.affiliate_project.model.PersonInfo;
import com.hl.affiliate_project.model.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// interface for person info repository
public interface PersonInfoRepository  extends JpaRepository<PersonInfo, Integer> {
	// find the existing person by email
	Optional<PersonInfo> findByEmail(String email);
}
