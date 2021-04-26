package com.trackme.common.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.trackme.common.constant.RegexValidationConstants.EMAIL_REGEX;

public class EmailValidator {

    public static boolean isEmail(String value) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);

        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}
