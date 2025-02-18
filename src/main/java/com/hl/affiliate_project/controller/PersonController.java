package com.hl.affiliate_project.controller;

import com.hl.affiliate_project.model.LoginRequest;
import com.hl.affiliate_project.model.PersonInfo;
import com.hl.affiliate_project.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody PersonInfo personInfo) {
		System.out.println("注册请求: " + personInfo.getEmail());
		try {
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
