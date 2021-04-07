package com.in726.app.unit.security;

import com.in726.app.security.EncoderSHA256HMAC;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncoderSHA256HMACTest {
    @Test
    public void encodeStringPositive() throws InvalidKeyException, NoSuchAlgorithmException {
        var expectedResult = "3da69a5144cdad13bff133c1cec038f2a5ea776a651275a5424b56409339ac55";
        var actualResult = EncoderSHA256HMAC.encodeString("secreteWorld", "ourWorld");

        Assert.assertEquals(expectedResult, actualResult);
    }
}
