package com.in726.app.unit.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.in726.app.enums.Roles;
import com.in726.app.model.User;
import com.in726.app.security.jwt.JWTGenerator;
import com.in726.app.security.jwt.JWTProvider;
import com.in726.app.security.jwt.MockUser;
import com.in726.app.security.jwt.TokenSignatureReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class JWTProviderTest {

   static JWTProvider jwtProvider;

    @BeforeClass
    public static void  setup() throws IOException {
        JWTGenerator<MockUser> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("username", user.getUsername())
                    .withClaim("level", user.getLevel());
            return token.sign(alg);
        };
        var signature = TokenSignatureReader.readSignature();
        Algorithm algorithm = Algorithm.HMAC256(signature);
        JWTVerifier verifier = JWT.require(algorithm).build();
        jwtProvider = new JWTProvider(algorithm, generator, verifier);
    }

    @Test
    public void  generateTokenPositive() {
        var user = new MockUser();
        user.setUsername("world");
        user.setLevel("user");
        Assert.assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6InVzZXIiLCJ1c2VybmFtZSI6IndvcmxkIn0.YeuRGkZDgdVJNLYd1Lp_wsR1l57iy-8J-iu0spW2OJA",jwtProvider.generateToken(user));
    }

    @Test
    public void validateTokenPositive() {
        var actual = jwtProvider.validateToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6InVzZXIiLCJ1c2VybmFtZSI6IndvcmxkIn0.YeuRGkZDgdVJNLYd1Lp_wsR1l57iy-8J-iu0spW2OJA");
        Assert.assertTrue(actual.isPresent());
    }

    @Test
    public void validateTokenNegative() {
        var actual = jwtProvider.validateToken("world");
        Assert.assertTrue(!actual.isPresent());
    }
}
