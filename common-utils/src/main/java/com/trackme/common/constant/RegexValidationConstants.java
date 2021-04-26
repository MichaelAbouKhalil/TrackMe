package com.trackme.common.constant;

public class RegexValidationConstants {

    // this regex pattern was obtained from OWASP organization
    public final static String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // this regex is from BCryptPasswordEncoder.class
    public final static String BCRYPT_PATTERN_REGEX =
            "\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}";
}
