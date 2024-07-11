package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "USM APIs User Interface", version = "2.0", description = "Available endpoints/APIs to be used in USM"))
public class QbeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QbeApplication.class, args);
	}

}
