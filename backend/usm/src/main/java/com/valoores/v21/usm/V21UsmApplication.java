package com.valoores.v21.usm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class V21UsmApplication {

	public static void main(String[] args) {
		SpringApplication.run(V21UsmApplication.class, args);
	}

}
 