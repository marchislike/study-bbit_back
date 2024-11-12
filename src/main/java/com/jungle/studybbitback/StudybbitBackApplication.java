package com.jungle.studybbitback;

import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@SpringBootApplication
//@EnableJpaAuditing
public class StudybbitBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudybbitBackApplication.class, args);
	}

}
