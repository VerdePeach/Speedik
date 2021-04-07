package com.in726.app.unit.convertor;

import com.in726.app.convertor.AgentConvertor;
import com.in726.app.model.Agent;
import com.in726.app.model.AgentData;
import com.in726.app.model.Cpu;
import com.in726.app.model.Disk;
import com.in726.app.parser.EasyJsonParser;
import com.in726.app.parser.json_model.AgentJson;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Test class for Convertor class
 */
public class AgentConvertorTest {

    @Test
    public void convertAgentDataSuccess() throws IOException, ParseException {
        var expectedResult = new Agent();
        expectedResult.setId(0);
        expectedResult.setHost("potapuff.example.com");
        expectedResult.setPublicKey("bright-host1");
        expectedResult.setSecretKey(null);
        expectedResult.setUser(null);
        var agentData = new AgentData();
        var cpu = new Cpu();
        cpu.setId(0);
        cpu.setNum(0);
        cpu.setUserLoad(0.07);
        cpu.setSystemLoad(0.09);
        cpu.setIdle(99.84);
        agentData.setCpus(Arrays.asList(cpu));
        var disk = new Disk();
        disk.setId(0);
        disk.setOrigin("/dev/disk0s2");
        disk.setFree(3118575616L);
        disk.setTotal(15448670208L);
        agentData.setDisks(Arrays.asList(disk));
        agentData.setAgentVersion("0.1.0");

        agentData.setTimeAdd(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse("2020-11-10 00:36:54 +0300"));
        agentData.setBootTime(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse("2019-11-10 19:45:52 +0300"));

        agentData.setTimeAdd(new Date());
        expectedResult.setAgentData(Arrays.asList(agentData));
        expectedResult.setLastActive(null);


        var pathToFile = "src/main/test_resources/extra/oneAgentData.json";

        var agentDataFile = loadAgentFromFile(pathToFile);


        var agentJson = EasyJsonParser.parseJsonToObject(
                agentDataFile,
                AgentJson.class,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"));

        var actualResult = new AgentConvertor().convertAgentJsonToAgent(agentJson);

        System.out.println(actualResult);
        expectedResult.getAgentData().get(0).setTimeAdd(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse("2020-11-10 00:36:54 +0300"));

        // TODO: Override equals and hashCode by properties.
        Assert.assertTrue(expectedResult.getPublicKey().equals(actualResult.getPublicKey()));
        Assert.assertTrue(expectedResult.getHost().equals(actualResult.getHost()));
        Assert.assertTrue(expectedResult.getId() == actualResult.getId());
        Assert.assertTrue(expectedResult.getAgentData().get(0).getTimeAdd()
                .equals(actualResult.getAgentData().get(0).getTimeAdd()));
    }

    private String loadAgentFromFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
