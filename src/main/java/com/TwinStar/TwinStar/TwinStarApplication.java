package com.TwinStar.TwinStar;

import jakarta.persistence.EntityListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing//@LastModifiedDate 사용하려고 넣음
public class TwinStarApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwinStarApplication.class, args);
	}

}
