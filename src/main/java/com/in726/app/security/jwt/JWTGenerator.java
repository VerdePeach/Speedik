package com.in726.app.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;

/**
 * Interface for JWT token generation.
 *
 * @param <T> Object with data for token
 */
@FunctionalInterface
public interface JWTGenerator<T> {
    String generate(T obj, Algorithm algorithm);
}