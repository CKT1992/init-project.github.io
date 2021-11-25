package com.example.backend.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;


public class ValidationUtil {
    static byte[] key = {102,29,-63,13,77,-48,-101,123,69,38,87,3,-37,121,-52,83,39,49,117,-62,55,71,122,74,94,21,81,-106,-19,15,9,70};

	private ValidationUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean isValidEmail(String email) {
		final String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isValidPassword(String password) {
		// Minimum eight characters, at least one letter, one number and one special
		// character:
		// final String regex =
		// "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&_])[A-Za-z\\d@$!%*#?&_]{8,32}$"

		// Minimum eight characters, at least one letter and one number:
		final String regex = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d][A-Za-z!@#$%^&*()_+-=]{7,}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static boolean isValidDateFormat(String dateStr) {
		String dateFormat = "yyyy-MM-dd";
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static boolean isValidToken(String token) {
		if (token == null) {
			return false;
		}
		try {
			// // Parse into JWE object
            JWEObject jweObject = JWEObject.parse(token);
			// Decrypt
            jweObject.decrypt(new DirectDecrypter(key));
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	public static boolean expaired(String tokenTime) {
        // Current time
        Date date = new Date();
        long currentTime = date.getTime();

        long diffSec = Math.abs(currentTime - Long.valueOf(tokenTime)) / (2 * 1000);

        // The token is valid for 10 minutes
        if (diffSec > 600) {
            return false;
        }
        return true;
    }
}
