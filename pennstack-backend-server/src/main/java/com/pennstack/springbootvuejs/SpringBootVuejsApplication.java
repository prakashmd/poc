package com.pennstack.springbootvuejs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.pennstack")
	public class SpringBootVuejsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootVuejsApplication.class, args);
	}
}
