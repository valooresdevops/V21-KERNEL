package com.valoores.v21.usm.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.valoores.v21.usm.client.config.KeycloakApiSettings;
import com.valoores.v21.usm.client.representation.token.KeycloakToken;
import com.valoores.v21.usm.client.representation.user.Credentials;
import com.valoores.v21.usm.client.representation.user.KeycloakUser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeycloakUserClientImpl implements IKeycloakUserClient{

	private final KeycloakApiSettings settings;
	private final RestTemplate keycloakRestTemplate;

	/*
	 * Returns the admin access token that is going to be used to read/create/delete
	 * users from keycloak
	 */
	@Override
	public String getAdminAccessToken() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");

		System.out.println("[getAdminAccessToken()], Headers ======= " + headers.toString());

		String data = "username=" + settings.getUsername() + "&password=" + settings.getPassword() + "&grant_type="
				+ settings.getGrantType() + "&client_id=" + settings.getClientId();

		System.out.println("[getAdminAccessToken()], application/x-www-form-urlencoded data ======= " + data);

		HttpEntity<String> request = new HttpEntity<>(data, headers);

		System.out.println("[getAdminAccessToken()], request ======= " + request.toString());

		KeycloakToken tokenResponse = keycloakRestTemplate.postForObject(settings.getAdminTokenEndpoint(), request,
				KeycloakToken.class);

		System.out.println("[getAdminAccessToken()], tokenResponse ======= " + tokenResponse.toString());
		System.out.println("[getAdminAccessToken()], accessToken  ======= " + tokenResponse.getAccessToken());

		return tokenResponse.getAccessToken();

	}

	/*
	 * Takes Keycloak user id 
	 * Returns corresponding Keycloak user object
	 */
	@Override
	public KeycloakUser getKeycloakUserById(String id) {

		HttpHeaders headers = new HttpHeaders();

		String token = "";

		try {
			token = getAdminAccessToken();
		} catch (HttpClientErrorException ex) {
			System.out.println("<<<Keycloak admin token request <===> " + ex.getMessage() + ">>>");
		}

		headers.add("Authorization", "Bearer " + token);
		System.out.println("[getKeycloakUserById()], headers ======== " + headers.toString());

		HttpEntity<Void> request = new HttpEntity<>(headers);
		System.out.println("[getKeycloakUserById()], request ======== " + request.toString());

		ResponseEntity<KeycloakUser> response = keycloakRestTemplate.exchange(
				settings.getUsersEndpoint().toString() + "/" + id, HttpMethod.GET, request, KeycloakUser.class);

		System.out.println("[getKeycloakUserById()], response ======== " + response.toString());

		KeycloakUser user = response.getBody();
		System.out.println("[getKeycloakUserById()], user ======== " + user.toString());

		return user;
	}

	/*
	 * Takes Keycloak user username 
	 * Returns corresponding Keycloak user id Useful in
	 * delete function below
	 * 
	 */
	@Override
	public String getKeycloakUserIdGivenUsername(String username) {
		
		HttpHeaders headers = new HttpHeaders();

		String token = "";

		try {
			token = getAdminAccessToken();
		} catch (HttpClientErrorException ex) {
			System.out.println("<<<Keycloak admin token request <===> " + ex.getMessage() + ">>>");
		}

		headers.add("Authorization", "Bearer " + token);
		System.out.println("[getKeycloakUserById()], headers ======== " + headers.toString());
		
		String url = UriComponentsBuilder.fromHttpUrl(settings.getUsersEndpoint())
		        .queryParam("exact", "{exact}")
		        .queryParam("username", "{username}")
		        .encode()
		        .toUriString();
		
		Map<String, String> params = new HashMap<>();
		params.put("exact", "true");
		params.put("username", username);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		System.out.println("[getKeycloakUserById()], request ======== " + request.toString());
		
		ResponseEntity<KeycloakUser[]> response = keycloakRestTemplate.exchange(
				url, 
				HttpMethod.GET,
				request,
				KeycloakUser[].class,
				params);
		
		System.out.println("[getKeycloakUserById()], response ======== " + response.toString());
		
		KeycloakUser[] returnedUsers = response.getBody();
		
		if(returnedUsers.length != 1) //
			return ""; // No user found with given username
		
		System.out.println("[getKeycloakUserById()], retrieved id  ======== " + returnedUsers[0].getId());
		return returnedUsers[0].getId(); // return the keycloak id of the user
	}

	/*
	 * Takes a username and a password 
	 * Creates a Keycloak user with given username and password
	 * Returns 201 CREATED if successful
	 */
	@Override
	public String addKeycloakUser(String username, String password, String firstName, String lastName, String email) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		String token = "";

		try {
			token = getAdminAccessToken();
		} catch (HttpClientErrorException ex) {
			System.out.println("<<<Keycloak admin token request <===> " + ex.getMessage() + ">>>");
		}

		headers.add("Authorization", "Bearer " + token);
		System.out.println("[addKeycloakUser()], headers ======== " + headers.toString());

		Credentials creds = Credentials.builder().type("password").value(password).temporary(false).build();

		List<Credentials> credsList = new ArrayList<>();
		credsList.add(creds);

		KeycloakUser data = KeycloakUser.builder().username(username).credentials(credsList).firstName(firstName)
				.lastName(lastName).email(email).enabled(true).build();

		System.out.println("[addKeycloakUser()], data ======== " + data.toString());

		HttpEntity<KeycloakUser> request = new HttpEntity<>(data, headers);

		System.out.println("[addKeycloakUser()], request ======== " + request.toString());

		ResponseEntity<String> response = keycloakRestTemplate.postForEntity(settings.getUsersEndpoint(), request,
				String.class);

		return response.getStatusCode().toString();
	}
	
	/*
	 * Takes Keycloak user id
	 * Deletes user with corresponding id 
	 */
	@Override
	public String deleteKeycloakUser(String id) {

		HttpHeaders headers = new HttpHeaders();
		
		String token = "";

		try {
			token = getAdminAccessToken();
		} catch (HttpClientErrorException ex) {
			System.out.println("<<<Keycloak admin token request <===> " + ex.getMessage() + ">>>");
		}
		
		headers.add("Authorization", "Bearer " + token);
		System.out.println("[deleteKeycloakUser()], headers ======== " + headers.toString());

		HttpEntity<Void> request = new HttpEntity<>(headers);
		System.out.println("[deleteKeycloakUser()], request ======== " + request.toString());

		ResponseEntity<String> response = keycloakRestTemplate
				.exchange(settings.getUsersEndpoint().toString() + "/" + id, HttpMethod.DELETE, request, String.class);

		System.out.println("[deleteKeycloakUser()], response ======== " + response.getStatusCode().toString());
		return response.getStatusCode().toString();

	}

}
