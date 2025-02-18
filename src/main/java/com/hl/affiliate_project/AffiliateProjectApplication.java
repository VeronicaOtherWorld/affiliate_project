package com.hl.affiliate_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * application entry point
 * */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hl.affiliate_project.service", "com.hl.affiliate_project.controller", "com.hl.affiliate_project.repository", "com.hl.affiliate_project.config"})
public class AffiliateProjectApplication {

	public static void main(String[] args) {

		SpringApplication.run(AffiliateProjectApplication.class, args);
	}

}


/*
* your-project/
├── src/
│   ├── main/
│   │   ├── java/com/example/yourproject/  ← 你的 Java 代码
│   │   │   ├── controller/   ← 处理 HTTP 请求（API 入口）
│   │   │   ├── service/      ← 业务逻辑层（数据处理）
│   │   │   ├── repository/   ← 数据库访问层（JPA）
│   │   │   ├── model/        ← 定义实体类（数据库表映射）
│   │   │   ├── YourProjectApplication.java  ← 入口文件
│   │   ├── resources/
│   │   │   ├── application.yml  ← 配置文件（数据库、端口等）
│   ├── test/  ← 你写的测试代码（可选）
│   ├── pom.xml  ← 依赖管理（Maven）
│   ├── README.md  ← 你的项目文档
*
*
* */