package com.valoores.v21.usm.utils.keycloakdatacleansing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FetchIdsFromKeycloakUsers {

	public static void main(String[] args) {

		// Create empty file to store the id of each user

		try {
			File ids = new File("/home/ucf/Desktop/users_id.txt");
			if (ids.createNewFile()) {
				System.out.println("File created: " + ids.getName());
				
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		// Read JSON of Keycloak Users from file and retrieve IDs and persist them to users_id.txt
		JSONParser jsonParser = new JSONParser();

		try {

			JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader("/home/ucf/Desktop/users.json"));
			
			FileWriter myWriter = new FileWriter("/home/ucf/Desktop/users_id.txt");
			
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				
				String id = (String) iterator.next().get("id");
				System.out.println(id);
				
				myWriter.write(id);
				myWriter.write(System.getProperty("line.separator"));
			}
			
			
			myWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
