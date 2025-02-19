package com.hl.affiliate_project.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String to, String subject, String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("aisileyi@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true); // `true` 代表支持 HTML


		// 发送邮件
		mailSender.send(message);
		try {
			mailSender.send(message);
			System.out.println("✅ 邮件发送成功：" + to);
		} catch (MailException e) {
			System.err.println("❌ 发送邮件失败：" + e.getMessage());
			e.printStackTrace();
		}
	}


	//generate a random code and send it to the user's email address
	public class CodeGenerator {
		private static final SecureRandom random = new SecureRandom();
		private static final int CODE_LENGTH = 6;

		public static String generateCode() {
			int code = 100000 + random.nextInt(900000); // 生成 100000 - 999999 之间的数字
			return String.valueOf(code);
		}
	}
}