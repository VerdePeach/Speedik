package com.in726.app.unit.security;

import com.in726.app.security.PasswordCreator;
import org.junit.Assert;
import org.junit.Test;

public class PasswordCreatorTest {
    @Test
    public void createPasswordPositive() {
        Assert.assertNotNull(PasswordCreator.createPassword());
    }
}
