package com.in726.app.security.jwt;

import lombok.Data;

/**
 * JWT Response.
 */
@Data
public class JWTResponse {
    private String jwt;

    /**
     * Creates JTWResponse object.
     *
     * @param jwt
     */
    public JWTResponse(String jwt) {
        this.jwt = jwt;
    }
}