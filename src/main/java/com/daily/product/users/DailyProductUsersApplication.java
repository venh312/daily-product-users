package com.daily.product.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DailyProductUsersApplication {
	public static void main(String[] args) {
		SpringApplication.run(DailyProductUsersApplication.class, args);
	}
}
