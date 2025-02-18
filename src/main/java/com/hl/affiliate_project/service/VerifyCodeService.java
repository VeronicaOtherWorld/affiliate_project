package com.hl.affiliate_project.service;


import com.hl.affiliate_project.model.VerifyCode;
import com.hl.affiliate_project.repository.VerifyCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerifyCodeService {

	@Autowired
	private VerifyCodeRepository verifyCodeRepository;

	@Autowired
	private EmailService emailService; // 发送邮件服务

	// 生成6位验证码
	private String generateCode() {
		Random random = new Random();
		int code = 100000 + random.nextInt(900000); // 生成100000 - 999999的随机数
		return String.valueOf(code);
	}

	// 发送验证码
	public void sendVerifyCode(String email) {
		String code = generateCode();
		LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10); // 10分钟有效

		VerifyCode verifyCode = new VerifyCode(email, code, expireTime);
		verifyCodeRepository.save(verifyCode);

		// 发送邮件
		String subject = "Your Verification Code";
		String content = "Your verification code is: " + code + "\nValid for 10 minutes.";
		try {
			emailService.sendEmail(email, subject, content);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (jakarta.mail.MessagingException e) {
			throw new RuntimeException(e);
		}
	}


	// 校验验证码
	public boolean verifyCode(String email, String code) {
		VerifyCode verifyCode = verifyCodeRepository.findByEmailAndCode(email, code)
						.orElseThrow(() -> new RuntimeException("Invalid verification code."));

		if (verifyCode.getStatus() == VerifyCode.Status.USED) {
			throw new RuntimeException("This verification code has already been used.");
		}

		if (verifyCode.getExpireTime().isBefore(LocalDateTime.now())) {
			verifyCode.setStatus(VerifyCode.Status.EXPIRED);
			verifyCodeRepository.save(verifyCode);
			throw new RuntimeException("Verification code has expired.");
		}

		// 标记验证码为已使用
		verifyCode.setStatus(VerifyCode.Status.USED);
		verifyCodeRepository.save(verifyCode);

		return true;
	}

	public void sendVerificationCode(String email)
					throws MessagingException, jakarta.mail.MessagingException {
		String code = EmailService.CodeGenerator.generateCode();

		// 创建验证码实体

		// expire time 5 minutes from now
		LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
		VerifyCode verifyCode = new VerifyCode(email, code, expireTime);


		// 存入数据库
		verifyCodeRepository.save(verifyCode);

		// 发送邮件
		String subject = "Your Verification Code";
		String text = "<p>Your verification code is: <b>" + code + "</b></p>";
		emailService.sendEmail(email, subject, text);
	}
}