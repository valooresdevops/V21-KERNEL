package com.valoores.datacrowd;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@EnableEurekaClient 
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Data Crowd APIs User Interface", version = "2.0", description = "Available endpoints/APIs to be used in USM"))

public class MapDatacrowdApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapDatacrowdApplication.class, args);
	} 

}
