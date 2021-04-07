package com.in726.app.unit.security.jwt;

import com.in726.app.security.jwt.MockUser;
import org.junit.Assert;
import org.junit.Test;

public class MockUserTest {
    @Test
    public void consturcotWithArgsPositive() {
        var mockUser = new MockUser("world", "space");
        Assert.assertEquals("world", mockUser.getUsername());
        Assert.assertEquals("space", mockUser.getLevel());
    }
    @Test
    public void consturcotNoArgsPositive() {
        var mockUser = new MockUser();
        mockUser.setUsername("world");
        mockUser.setLevel("space");
        Assert.assertEquals("world", mockUser.getUsername());
        Assert.assertEquals("space", mockUser.getLevel());
    }
}
