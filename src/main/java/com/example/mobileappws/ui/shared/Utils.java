package com.example.mobileappws.ui.shared;

import com.example.mobileappws.security.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIGQLMONPQRSTUVWXYZabcdefghigqlmnopqrstuvwxyz";

    public static boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.getTokenSecret())
                .parseClaimsJws(token).getBody();

        Date claimsExpiration = claims.getExpiration();
        Date today = new Date();

        return claimsExpiration.before(today);
    }

    public static String generatePasswordResetToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_PASSWORD_RESET_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS512,SecurityConstant.getTokenSecret())
                .compact();
    }

    public String generateRandomId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }

    public String generateEmailVerificationToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_EMAIL_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS512,SecurityConstant.getTokenSecret())
                .compact();
    }
}
