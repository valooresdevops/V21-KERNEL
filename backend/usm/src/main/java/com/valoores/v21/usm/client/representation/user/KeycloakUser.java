package com.valoores.v21.usm.client.representation.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUser {
	
	@JsonProperty
	private String id;
	@JsonProperty
	private long createdTimestamp;
	@JsonProperty
	private String username;
	@JsonProperty
	private List<Credentials> credentials;
	@JsonProperty
	private String email;
	@JsonProperty
	private boolean enabled;
	@JsonProperty
	private boolean totp;
	@JsonProperty
	private boolean emailVerified;
	@JsonProperty
	private String firstName;
	@JsonProperty
	private String lastName;
	@JsonProperty
	private String federationLink;
	@JsonProperty
	private Attributes attributes;
	@JsonProperty
	private List<Object> disableableCredentialTypes;
	@JsonProperty
	private List<Object> requiredActions;
	@JsonProperty
	private int notBefore;
	@JsonProperty
	private Access access;
	
	

}
