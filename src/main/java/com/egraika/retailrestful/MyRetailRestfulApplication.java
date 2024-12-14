package com.egraika.retailrestful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyRetailRestfulApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyRetailRestfulApplication.class, args);
	}
}
