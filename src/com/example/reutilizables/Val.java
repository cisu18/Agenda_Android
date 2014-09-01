package com.example.reutilizables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Val {

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public static boolean isPasswordEquals(String pass1, String pass2) {
		boolean isValid = false;
		if (pass1.equals(pass2)) {
			isValid = true;
		}
		return isValid;
	}
}
