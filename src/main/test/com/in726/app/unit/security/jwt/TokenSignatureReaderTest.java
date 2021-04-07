package com.in726.app.unit.security.jwt;

import com.in726.app.security.jwt.TokenSignatureReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TokenSignatureReaderTest {

    @Test
    public void readSignaturePositive() throws IOException {
        Assert.assertNotNull(TokenSignatureReader.readSignature());
    }
}
