package com.jungle.studybbitback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudybbitBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudybbitBackApplication.class, args);
	}

}
