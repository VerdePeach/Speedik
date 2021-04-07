package com.in726.app.unit.security.jwt;

import com.in726.app.security.jwt.JWTResponse;
import org.junit.Assert;
import org.junit.Test;

public class JWTResponseTest {
    @Test
    public void constructorJWTResponsePositive() {
        var jwtResponse = new JWTResponse("jwt");
        Assert.assertEquals("jwt", jwtResponse.getJwt());
    }
    @Test
    public void gettingAndSettingJWTResponsePositive() {
        var jwtResponse = new JWTResponse("jwt");
        jwtResponse.setJwt("Bearer");
        Assert.assertEquals("Bearer", jwtResponse.getJwt());
    }
}
