package com.valoores.v21.usm.client;

import com.valoores.v21.usm.client.representation.user.KeycloakUser;

public interface IKeycloakUserClient {
	
	public String getAdminAccessToken();
	public KeycloakUser getKeycloakUserById(String id);
	public String getKeycloakUserIdGivenUsername(String username);
	public String addKeycloakUser(String username, String password, String firstName, String lastName, String email);
	public String deleteKeycloakUser(String id);

}
