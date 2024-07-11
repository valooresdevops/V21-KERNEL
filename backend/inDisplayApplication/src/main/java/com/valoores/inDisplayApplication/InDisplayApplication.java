package com.valoores.inDisplayApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "In Display APIs User Interface", version = "2.0", description = "Available endpoints/APIs to be used in USM"))
public class InDisplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(InDisplayApplication.class, args);
	}

}
