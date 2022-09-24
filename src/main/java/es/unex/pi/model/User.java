package es.unex.pi.model;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class User {

	private long id;
	private String username;
	private String email;
	private String password;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean validate(Map<String, String> messages) {
/*
		final int MAX = 8;
		final int MIN_Uppercase = 1;
		final int MIN_Lowercase = 1;
		final int NUM_Digits = 1;
		final int Special = 1;
		// Counts
		int uppercaseCounter = 0;
		int lowercaseCounter = 0;
		int digitCounter = 0;
		int specialCounter = 0;

		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			if (Character.isUpperCase(c))
				uppercaseCounter++;
			else if (Character.isLowerCase(c))
				lowercaseCounter++;
			else if (Character.isDigit(c))
				digitCounter++;
			if (c >= 33 && c <= 46 || c == 64) {
				specialCounter++;
			}
		}

		if (password.length() >= MAX && uppercaseCounter >= MIN_Uppercase && lowercaseCounter >= MIN_Lowercase
				&& digitCounter >= NUM_Digits && specialCounter >= Special) {
			return true;
		} else {
			messages.put("error1", "Your password does not contain the following:");
			if (password.length() < MAX)
				messages.put("error2", "atleast 8 characters");
			if (lowercaseCounter < MIN_Lowercase)
				messages.put("error3", "Minimum lowercase letters");
			if (uppercaseCounter < MIN_Uppercase)
				messages.put("error4", "Minimum uppercase letters");
			if (digitCounter < NUM_Digits)
				messages.put("error5", "Minimum number of numeric digits");
			if (specialCounter < Special)
				messages.put("error6", "Password should contain at lest 1 special characters");
			return false;
		}*/
		return true;
	}
}
