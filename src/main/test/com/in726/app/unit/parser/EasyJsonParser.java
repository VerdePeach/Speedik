package com.in726.app.unit.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.in726.app.model.Cpu;
import com.in726.app.parser.json_model.AgentJson;
import com.in726.app.parser.json_model.NetworkJson;
import com.in726.app.security.PasswordChanger;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EasyJsonParser {
    // TODO 2 positive and 2 negative

    @Test
    public void parseByClassPositive() throws IOException {
        var expectedResult = new PasswordChanger();
        expectedResult.setNewPassword("Hello");
        expectedResult.setOldPassword("Hallo");

        var pathToFile = "src/main/test_resources/old_pass/passwordChange.json";

        var passwords = loadDataFormFile(pathToFile);
        var actualResult = com.in726.app.parser.EasyJsonParser.parseJsonToObject(
                passwords,
                PasswordChanger.class);

        Assert.assertEquals(expectedResult.getNewPassword(), actualResult.getNewPassword());
        Assert.assertEquals(expectedResult.getOldPassword(), actualResult.getOldPassword());
    }

    @Test
    public void parseByClassAndDateFormatPositive() throws IOException {
        var expectedResult = new AgentJson();
        expectedResult.setHost("potapuff.example.com");
        expectedResult.setPublic_key("bright-host1");
        expectedResult.setAgent_version("0.1.0");
        expectedResult.setLoad(Arrays.asList(new Object[]{0.27, 0.12, 0.07}));
        var network = new ArrayList<NetworkJson>();
        var net1 = new NetworkJson();
        var net2 = new NetworkJson();
        net1.setName("ens3");
        net1.setIn(126318244556L);
        net1.setOut(2743616382L);
        net2.setName("lo");
        net2.setIn(1034794894L);
        net2.setOut(1034794894L);
        network.add(net1);
        network.add(net2);
        expectedResult.setNetwork(network);

        var pathToFile = "src/main/test_resources/extra/oneAgentData.json";

        var agentData = loadDataFormFile(pathToFile);


        var actualResult = com.in726.app.parser.EasyJsonParser.parseJsonToObject(
                agentData,
                AgentJson.class,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"));

        // TODO: Override equals and hashCode by properties.
        Assert.assertEquals(expectedResult.getAgent_version(), actualResult.getAgent_version());
        Assert.assertEquals(expectedResult.getPublic_key(), actualResult.getPublic_key());
        Assert.assertEquals(expectedResult.getHost(), actualResult.getHost());
        Assert.assertEquals(expectedResult.getLoad(), actualResult.getLoad());
        Assert.assertEquals(expectedResult.getNetwork(), actualResult.getNetwork());
        Assert.assertNotNull(actualResult.getCpu());
        Assert.assertNotNull(actualResult.getDisks());
        Assert.assertNotNull(actualResult.getMemory());
    }

    @Test(expected = JsonProcessingException.class)
    public void parseByClassNegative() throws IOException {
        var expectedResult = new PasswordChanger();
        expectedResult.setNewPassword("Hello");
        expectedResult.setOldPassword("Hallo");

        var pathToFile = "src/main/test_resources/old_pass/passwordChangeWrong.json";

        var passwords = loadDataFormFile(pathToFile);
        com.in726.app.parser.EasyJsonParser.parseJsonToObject(passwords, PasswordChanger.class);
    }

    @Test(expected = JsonProcessingException.class)
    public void parseByClassAndDateFormatNegative() throws IOException {
        var pathToFile = "src/main/test_resources/extra/oneAgentDataWrong.json";
        var agentData = loadDataFormFile(pathToFile);

        com.in726.app.parser.EasyJsonParser.parseJsonToObject(
                agentData,
                AgentJson.class,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"));
    }

    private String loadDataFormFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
