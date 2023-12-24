package com.bsmm.login.util;

public class Constants {
    private Constants() {
    }

    public static final String TOKEN_TYPE = "Bearer";
    public static final String TOKEN_TYPE_WITH_SPACE = TOKEN_TYPE.concat(" ");
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_NAME = "name";
    public static final String CLAIM_ROLES = "roles";
    public static final String COMPLEX_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-_])(?=\\S+$).{8,25}$";
}
