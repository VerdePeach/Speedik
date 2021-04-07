package com.in726.app.unit.security.jwt;

import com.in726.app.security.jwt.JavalinJWT;
import org.junit.Test;



public class JavalinJWTTest {

    @Test(expected = NullPointerException.class)
    public void containsJWTNegative() {
        JavalinJWT.containsJWT(null);
    }
}
