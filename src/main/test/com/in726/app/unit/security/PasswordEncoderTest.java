package com.in726.app.unit.security;

import com.in726.app.security.PasswordEncoder;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class PasswordEncoderTest {
    @Test
    public void encodePasswordPositive() throws NoSuchAlgorithmException {
        var expectedResult = "3fb5eebffc5da547bb6823900d3d22c6edb6619d67aed6fb92a5b5df9a6b6f01c5336cf9569e92a8405555aa5e9a3d1569cfb50421c9784261c594366cb86bb6";
        var actualResult = PasswordEncoder.hashPassword("worldPassword");
        Assert.assertEquals(expectedResult, actualResult);
    }
}
