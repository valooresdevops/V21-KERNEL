package com.valoores.datacrowd.utils;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.json.JSONObject;

public class StringUtils {

	// JSON object to String
	public static String toJsonString(Object obj) {
		return (new JSONObject(obj)).toString();
	}

	// Encode a String base 64
	public static String encodeBase64(String value) {

		try {
			return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8.toString()));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}

	}

	public static String decodeBase64(String value) {

		try {
			return new String(Base64.getDecoder().decode(value));
		} catch (IllegalArgumentException ex) {
			System.out.println("ERROR decoding string '" + value + "':::::::: not a valid Base64 encoded string.");
			throw new RuntimeException(ex);
		}

	}

	public static String sha256(String message) {

		try {

			// Static getInstance method is called with hashing SHA
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// digest() method called
			// to calculate message digest of an input
			// and return array of byte
			byte[] messageDigest = md.digest(message.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			System.out.println("Exception thrown" + " for incorrect algorithm: " + e);
			return null;
		}
	}

}