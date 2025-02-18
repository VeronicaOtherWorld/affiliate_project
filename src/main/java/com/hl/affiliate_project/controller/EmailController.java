package com.hl.affiliate_project.controller;

import com.hl.affiliate_project.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/send-email")
	public String sendVerificationEmail(@RequestParam String email) {
		try {
			emailService.sendEmail(email, "Verification Code", "Your verification code is 123456");
			return "Email sent successfully!";
		} catch (Exception e) {
			return "Failed to send email: " + e.getMessage();
		}
	}
}