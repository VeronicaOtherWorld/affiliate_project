package com.hl.affiliate_project.controller;

import com.hl.affiliate_project.service.VerifyCodeService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verify")
public class VerifyCodeController {
	@Autowired
	private VerifyCodeService verifyCodeService;

	@PostMapping("/send")
	public String sendCode(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		System.out.println(email+ "==================");
		if (email == null) {
			return "Missing email parameter.";
		}
		try {
			verifyCodeService.sendVerificationCode(email);
			return "Verification code sent!";
		} catch (MessagingException e) {
			return "Failed to send verification code.";
		} catch (javax.mail.MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}