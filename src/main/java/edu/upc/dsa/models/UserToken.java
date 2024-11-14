package edu.upc.dsa.util;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;

public class UserToken {
    private String token;
    private ZonedDateTime expiration;

    public UserToken(){
        SecureRandom random = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes);
        this.token = base64Encoder.encodeToString(randomBytes);
        this.expiration = ZonedDateTime.now().plusDays(1);   // the token has a duration of one day
    }

    public String getToken() {
        return token;
    }

    public ZonedDateTime getExpiration() {
        return expiration;
    }

    public void renewToken(){
        this.expiration = ZonedDateTime.now().plusDays(1);
    }
}
