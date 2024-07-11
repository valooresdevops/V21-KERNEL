package com.valoores.wfm.config;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration {

	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOriginPatterns("*").allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS", "TRACE")
						.allowedHeaders("*");
			}
		};
	}
	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
	    return (tomcat) -> tomcat.addConnectorCustomizers((connector) -> {
	        if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
	            AbstractHttp11Protocol<?> protocolHandler = (AbstractHttp11Protocol<?>) connector
	                    .getProtocolHandler();
	            protocolHandler.setKeepAliveTimeout(80000);
	            protocolHandler.setMaxKeepAliveRequests(500);
	            protocolHandler.setUseKeepAliveResponseHeader(true);
	        }
	    });
	}
}