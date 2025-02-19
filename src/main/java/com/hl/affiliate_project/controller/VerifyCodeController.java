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
		System.out.println(email + "==================");

		if (email == null) {
			return "Missing email parameter.";
		}

		try {
			verifyCodeService.sendVerificationCode(email);
			return "Verification code sent!";
		} catch (MessagingException e) { // ✅ 确保这里的异常和方法的 throws 匹配
			return "Failed to send verification code: " + e.getMessage();
		} catch (javax.mail.MessagingException e) {
			throw new RuntimeException(e);
		}
	}


	// check the verification code
	@PostMapping("/check")
	public String checkCode(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String code = request.get("code");

		if (email == null || code == null) {
			return "Missing email or code.";
		}

		boolean isValid = verifyCodeService.verifyCode(email, code);
		return isValid ?
						"Verification code valid!"
						: "Invalid or expired verification code.";
	}
}