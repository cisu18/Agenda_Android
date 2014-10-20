package com.clmdev.reutilizables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

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

	public static boolean isUserValid(String c) {
		boolean isValid = false;

		String expression = "^[a-zA-Z0-9]";
		CharSequence inputStr = c;
		Log.e("vAL:Error", c);
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

	public static boolean isVacio(String str1, String str2, String str3,
			String str4) {
		boolean isValid = true;
		if (str1.equals("") || str2.equals("") || str3.equals("")
				|| str4.equals("")) {
			isValid = false;
		}
		return isValid;
	}

	public static void setEvaluated(Context context, String id) {
		SharedPreferences preferencias = context.getSharedPreferences(id,
				Context.MODE_PRIVATE);
		Editor editor = preferencias.edit();
		editor.putString("eval", Util.getFechaActual());
		editor.commit();

	}

	public static boolean isEvaluated(Context context, String id) {
		SharedPreferences preferencias = context.getSharedPreferences(id,
				Context.MODE_PRIVATE);
		boolean isValid = false;
		if (preferencias.getString("eval", "0").equals(Util.getFechaActual())) {
			isValid = true;
		}
		return isValid;
	}
}
