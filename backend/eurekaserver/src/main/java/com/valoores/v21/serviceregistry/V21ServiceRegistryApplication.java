package com.valoores.v21.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class V21ServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(V21ServiceRegistryApplication.class, args);
	}

}
