package com.in726.app.unit.model;

import com.in726.app.model.Network;
import org.junit.Assert;
import org.junit.Test;

public class NetworkTest {
    @Test
    public void cteateNetworkObjectPositive() {
        Assert.assertNotNull(new Network());
    }
}
