package com.example.mobileappws.security;

import com.example.mobileappws.SpringContextApplication;
import com.example.mobileappws.ui.shared.AppProperties;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 864000000;
    public static final long EXPIRATION_EMAIL_TOKEN_TIME = 1800000;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SING_UP_URL = "/users";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringContextApplication.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
