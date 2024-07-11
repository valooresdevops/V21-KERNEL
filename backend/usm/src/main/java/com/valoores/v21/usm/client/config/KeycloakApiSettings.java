package com.valoores.v21.usm.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "keycloak.api")
public class KeycloakApiSettings {
	
	private String adminTokenEndpoint;
	private String usersEndpoint;
	private String username;
	private String password;
	private String grantType;
	private String clientId;

}
