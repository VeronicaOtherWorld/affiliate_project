package com.hl.affiliate_project.service;


import com.hl.affiliate_project.model.VerifyCode;
import com.hl.affiliate_project.repository.VerifyCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
		} catch (jakarta.mail.MessagingException e) {
			throw new RuntimeException(e);
		}
	}


	// 校验验证码
	public boolean verifyCode(String email, String code) {
		List<VerifyCode> codes = verifyCodeRepository.findByEmailAndStatus(email, VerifyCode.Status.PENDING);

		if (codes.isEmpty()) {
			return false; // ❌ 没有可用的验证码
		}

		// 找到匹配的验证码
		Optional<VerifyCode> matchedCode = codes.stream()
						.filter(vc -> vc.getCode().equals(code))
						.findFirst();

		if (matchedCode.isEmpty()) {
			return false; // ❌ 输入的验证码不匹配
		}

		VerifyCode verifyCode = matchedCode.get();

		if (verifyCode.getExpireTime().isBefore(LocalDateTime.now())) {
			verifyCode.setStatus(VerifyCode.Status.EXPIRED);
			verifyCodeRepository.save(verifyCode);
			return false; // ❌ 验证码已过期
		}

		// ✅ 通过验证，标记为已使用
		verifyCode.setStatus(VerifyCode.Status.USED);
		verifyCodeRepository.save(verifyCode);

		return true;
	}

	public void sendVerificationCode(String email) throws MessagingException, jakarta.mail.MessagingException {
		String code = generateCode(); // 生成6位随机验证码
		LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);


		// 先查找当前邮箱是否已有未过期验证码
		List<VerifyCode> existingCodes = verifyCodeRepository.findByEmailAndStatus(email, VerifyCode.Status.PENDING);

		// **如果存在未过期验证码，手动过期它**
		if (!existingCodes.isEmpty()) {
			for (VerifyCode codeItem : existingCodes) {
				codeItem.setStatus(VerifyCode.Status.EXPIRED);
			}
			verifyCodeRepository.saveAll(existingCodes); // 批量更新验证码状态
		}

		// **检查是否已有未过期的验证码，避免重复**
		// List<VerifyCode> existingCodes = verifyCodeRepository.findByEmailAndStatus(email, VerifyCode.Status.PENDING);
		// if (!existingCodes.isEmpty()) {
		// 	throw new RuntimeException("验证码已发送，请稍后再试");
		// }

		// 创建新验证码并存入数据库
		VerifyCode verifyCode = new VerifyCode(email, code, expireTime);
		verifyCodeRepository.save(verifyCode);

		// **HTML 格式邮件**
		String subject = "Your Verification Code";
		String text = "<p>Your verification code is: <b>" + code + "</b></p>";

		// 发送邮件（确保支持 HTML）
		emailService.sendEmail(email, subject, text);
	}
}