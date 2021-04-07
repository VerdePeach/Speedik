package com.in726.app.security.jwt;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Optional;

/**
 * JWT Provider, contains the logic of token handling.
 */
public class JWTProvider {
    private final Algorithm algorithm;
    private final JWTGenerator generator;
    private final JWTVerifier verifier;

    /**
     * Constructor of JWTProvider.
     *
     * @param algorithm hashing algorithm.
     * @param generator JWT generator.
     * @param verifier  JWT verifier.
     */
    public JWTProvider(Algorithm algorithm, JWTGenerator generator, JWTVerifier verifier) {
        this.algorithm = algorithm;
        this.generator = generator;
        this.verifier = verifier;
    }

    /**
     * Generates JWT token
     *
     * @param obj Object
     * @return Object with data for token
     */
    public String generateToken(Object obj) {
        return generator.generate(obj, algorithm);
    }

    /**
     * Validates token.
     *
     * @param token JTW
     * @return decoded JWT
     */
    public Optional<DecodedJWT> validateToken(String token) {
        try {
            return Optional.of(verifier.verify(token));
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
