package com.in726.app.unit.security.jwt;

import com.in726.app.security.jwt.ProviderExample;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ProviderExampleTest {

    @Test
    public void createHMAC512Positive() throws IOException {
       Assert.assertNotNull(ProviderExample.createHMAC512());
    }
}
