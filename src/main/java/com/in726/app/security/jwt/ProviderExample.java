package com.in726.app.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.in726.app.Speedik;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Class for provider setting and generation.
 */
public class ProviderExample {

    private static final long Hoers5InMilliseconds = 5 * 60 * 60000;
    private static String signature;

    private static final Logger logger = LoggerFactory.getLogger(Speedik.class);

    /**
     * Generates and sets up provider.
     *
     * @return JWTProvider
     */
    public static JWTProvider createHMAC512() throws IOException {
        JWTGenerator<MockUser> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("username", user.getUsername())
                    .withClaim("level", user.getLevel()).withExpiresAt(new Date(new Date().getTime() + Hoers5InMilliseconds));
            return token.sign(alg);
        };
        signature = TokenSignatureReader.readSignature();
        Algorithm algorithm = Algorithm.HMAC256(signature);
        JWTVerifier verifier = JWT.require(algorithm).build();

        return new JWTProvider(algorithm, generator, verifier);
    }
}