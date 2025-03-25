package com.example.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.example.spring",
		"com.example.util" 
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
