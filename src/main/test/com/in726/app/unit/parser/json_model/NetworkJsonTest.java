package com.in726.app.unit.parser.json_model;

import com.in726.app.parser.json_model.NetworkJson;
import org.junit.Assert;
import org.junit.Test;

public class NetworkJsonTest {

    @Test
    public void gettingAndSettingPropertiesPositive() {
        var name = "world";
        var in = 1;
        var out = 5;

        var networkJson = new NetworkJson();
        networkJson.setName(name);
        networkJson.setIn(in);
        networkJson.setOut(out);

        Assert.assertTrue(in == networkJson.getIn());
        Assert.assertTrue(out == networkJson.getOut());
        Assert.assertEquals(name, networkJson.getName());
    }
}
