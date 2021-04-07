package com.in726.app.unit.security;

import com.in726.app.security.Persistent;
import org.junit.Assert;
import org.junit.Test;

public class PersistentTest {
    @Test
    public void gettingAndSettingPositive() {
        var username = "world";
        var password = "worldPassword";
        var email = "world@gmail.com";

        var actualResult = new Persistent();
        actualResult.setUsername(username);
        actualResult.setPassword(password);
        actualResult.setEmail(email);

        Assert.assertEquals(username, actualResult.getUsername());
        Assert.assertEquals(password, actualResult.getPassword());
        Assert.assertEquals(email, actualResult.getEmail());
    }
}
