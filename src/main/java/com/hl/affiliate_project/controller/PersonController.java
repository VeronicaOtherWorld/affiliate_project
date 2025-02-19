package com.hl.affiliate_project.controller;

import com.hl.affiliate_project.model.LoginRequest;
import com.hl.affiliate_project.model.PersonInfo;
import com.hl.affiliate_project.service.PersonInfoService;
import com.hl.affiliate_project.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 *
 * handle HTTP requests (API entry point)
 *
 * */

@RestController
@RequestMapping("/api/person")
 public class PersonController {
	@Autowired
	private PersonInfoService personInfoService;

	@Autowired
	private VerifyCodeService verifyCodeService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String password = request.get("password");
		String code = request.get("code"); // 获取验证码

		System.out.println("注册请求: " + email);

		// 1️⃣ **检查参数是否为空**
		if (email == null || password == null || code == null) {
			return ResponseEntity.badRequest().body("缺少必要参数: email, password, code");
		}

		// 2️⃣ **校验验证码**
		try {
			boolean isValid = verifyCodeService.verifyCode(email, code);
			if (!isValid) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("验证码无效或已过期");
			}
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("验证码错误：" + e.getMessage());
		}

		try {
			// 3️⃣ **验证码正确后，继续注册**
			PersonInfo personInfo = new PersonInfo();
			personInfo.setEmail(email);
			personInfo.setPwd(passwordEncoder.encode(password)); // 加密密码

			personInfoService.register(personInfo);

			return ResponseEntity.ok("注册成功");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("注册失败：" + e.getMessage());
		}
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<PersonInfo> getByEmail(@PathVariable String email) {
		return personInfoService.getPersonByEmail(email)
						.map(ResponseEntity::ok)
						.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}


	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
		try {
			String token = String.valueOf(personInfoService.login(loginRequest.getEmail(), loginRequest.getPwd()));
			return ResponseEntity.ok(token); // **直接返回 Token**
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
	}
}
