package com.hl.affiliate_project.service;
import com.hl.affiliate_project.model.PersonInfo;
import com.hl.affiliate_project.repository.PersonInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.Optional;

@Service
public class PersonInfoService {
	private static final Logger logger = LoggerFactory.getLogger(PersonInfoService.class);
	private final JwtService jwtService;

	@Autowired
	private PersonInfoRepository personInfoRepository; // 数据库访问

	@Autowired
	private PasswordEncoder passwordEncoder; // ✅ 自动注入 PasswordEncoder

	private PersonInfo personInfo;

	public PersonInfoService(PersonInfoRepository personInfoRepository, JwtService jwtService) {
		this.personInfoRepository = personInfoRepository;
		this.jwtService = jwtService;

	}





	// get person by email
	public Optional<PersonInfo> getPersonByEmail(String email) {
		return personInfoRepository.findByEmail(email);
	}

	// register a new person
	public PersonInfo register(PersonInfo personInfo) {
		if (personInfo.getStatus() == null) {
			personInfo.setStatus((byte)1); // 默认值
		}
		// 检查邮箱是否已注册
		if (personInfoRepository.findByEmail(personInfo.getEmail()).isPresent()) {
			throw new RuntimeException("邮箱已存在");
		}
		System.out.println("注册请求: " + personInfo.getEmail());
		// 对密码进行加密
		String encryptedPassword = passwordEncoder.encode(personInfo.getPwd());
		personInfo.setPwd(encryptedPassword);  // 设置加密后的密码
		// 保存用户信息到数据库
		return personInfoRepository.save(personInfo);
	}

	// login a person
	public String login(String email, String rawPassword) {
		// 1️⃣ 查询用户
		PersonInfo person = personInfoRepository.findByEmail(email)
						.orElseThrow(() -> new RuntimeException("用户不存在"));

		// 2️⃣ 获取数据库中的加密密码
		String encryptedPassword = person.getPwd();
		System.out.println("🔍 the encrypted password in the database: " + encryptedPassword);
		System.out.println("🔍 the password entered by the user: " + rawPassword);

		// 3️⃣ 进行密码比对
		if (passwordEncoder.matches(rawPassword, encryptedPassword)) {
			System.out.println("✅ the password entered by the user: " + rawPassword + " is correct");
			return jwtService.generateToken(String.valueOf(personInfo)); // 生成并返回JWT
		} else {
			System.out.println("❌ the password entered by the user is not correct");
			throw new RuntimeException("Invalid email or password");
			// return jwtService.generateToken(String.valueOf(personInfo)); // 生成并返回JWT
		}
	}
}
