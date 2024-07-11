package com.valoores.v21.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.valoores.v21.apigateway.config.RibbonConfiguration;

@EnableZuulProxy
@EnableEurekaClient
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
@SpringBootApplication
public class V21ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(V21ApiGatewayApplication.class, args);
	}

}
