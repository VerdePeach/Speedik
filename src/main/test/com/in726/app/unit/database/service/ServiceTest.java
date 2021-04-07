package com.in726.app.unit.database.service;

import com.in726.app.Speedik;
import com.in726.app.database.service.*;
import com.in726.app.enums.LinkStatus;
import com.in726.app.model.Agent;
import com.in726.app.model.AgentData;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.CheckLink;
import com.in726.app.model.sub_functional_model.Letter;
import com.in726.app.model.sub_functional_model.Link;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTest {

    private UserService userService = new UserService();
    private LinkService linkService = new LinkService();
    private LetterService letterService = new LetterService();
    private CheckLinkService checkLinkService = new CheckLinkService();
    private AgentService agentService = new AgentService();
    private AgentDataService agentDataService = new AgentDataService();

    @BeforeClass
    public static void setup() throws IOException {
        String[] args = new String[0];
        Speedik.main(args);
    }

//User
    @Test
    public void saveUserPositive() throws IOException, InterruptedException {

        var user = new User();
        user.setUsername("world");

        userService.save(user);
        Assert.assertEquals(user.getUsername(), userService.getUserByUsername("world").getUsername());
    }

    @Test
    public void updateUserPositive() throws IOException {

        var user = new User();
        user.setUsername("old_world");

        userService.save(user);
        var savedUser = userService.getUserByUsername("old_world");
        savedUser.setUsername("new_world");
        userService.update(savedUser);
        Assert.assertEquals("new_world", userService.getUserByUsername("new_world").getUsername());
    }

    @Test
    public void deleteUserPositive() throws IOException {

        var user = new User();
        user.setUsername("old_world1234567890");
        userService.save(user);
        userService.delete(user);
        Assert.assertNull(userService.getUserByUsername("old_world1234567890"));
    }

    @Test
    public void getAllUserPositive() throws IOException {

        var user = new User();
        user.setUsername("worldUserGetAll");
        userService.save(user);

        var users = userService.getAll();
        Assert.assertNotNull(users);
        Assert.assertNotNull(users instanceof List<User>);
    }

    @Test
    public void getUserByUsernamePositive() throws IOException {

        var user = new User();
        user.setUsername("userGetUserByUsername");
        userService.save(user);
        var actual = userService.getUserByUsername("userGetUserByUsername");
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof User);
    }

    @Test
    public void getUserByIdPositive() {

        var user = new User();
        user.setUsername("userGetUserById");
        userService.save(user);
        var savedUser = userService.getUserByUsername("userGetUserById");

        var actual = userService.getUserById(savedUser.getId());
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof User);
    }

    @Test
    public void getUserByEmailPositive() {

        var user = new User();
        user.setUsername("userGetUserByEmail");
        user.setEmail("userGetUserByEmail@gmail.com");
        userService.save(user);

        var savedUser = userService.getUserByUsername("userGetUserByEmail");

        var actual = userService.getUserByEmail(savedUser.getEmail());
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof User);
    }


    @Test
    public void setUserActiveByUserIdPositive() {

        var user = new User();
        user.setUsername("userSetUserActiveByUserId");
        userService.save(user);

        var savedUser = userService.getUserByUsername("userSetUserActiveByUserId");
        userService.setUserActiveByUserId(savedUser.getId());
        var updatedUser = userService.getUserByUsername("userSetUserActiveByUserId");
        Assert.assertNotNull(updatedUser);
        Assert.assertNotEquals(savedUser.getLastActive(), updatedUser.getLastActive());
    }

    @Test
    public void findUsersByActivePositive() {

        var user = new User();
        user.setUsername("userFindUsersByActive");
        userService.save(user);
        var users = userService.findUsersByActive(new Date(new Date().getTime() - 20000), "YES");
        var actualResult = users.stream().filter(u -> u.getUsername().equals("userFindUsersByActive")).collect(Collectors.toList());
        Assert.assertTrue(actualResult.size() > 0);
    }


    // link service


    @Test
    public void addLinkTest() {

        var link = new Link();
        link.setName("addLinkTestLink");
        link.setSecondsToCheck(60);
        link.setUrl("http://t6.tss2020.site");

        var user = new User();
        user.setUsername("addLinkTestUser");
        userService.save(user);
        var savedUser = userService.getUserByUsername("addLinkTestUser");

        link.setUser(savedUser);
        linkService.addLink(link);

        var actualLink = linkService.findLastAddedLinkByUserId(savedUser.getId());
        Assert.assertEquals(link.getName(), actualLink.getName());
        Assert.assertEquals(link.getUrl(), actualLink.getUrl());
    }

    @Test
    public void amountOfLinksForUserByUserIdTest() {
        var link = new Link();
        link.setName("amountOfLinksForUserByUserIdTestLink");
        link.setSecondsToCheck(60);
        link.setUrl("http://t6.tss2020.site");

        var user = new User();
        user.setUsername("amountOfLinksForUserByUserIdTestUser");
        userService.save(user);
        var savedUser = userService.getUserByUsername("amountOfLinksForUserByUserIdTestUser");

        link.setUser(savedUser);
        linkService.addLink(link);

        var actualAmount = linkService.amountOfLinksForUserByUserId(savedUser.getId());
        Assert.assertTrue(actualAmount > 0);
    }

    @Test
    public void findLastAddedLinkByUserIdPositive() {
        var link = new Link();
        link.setName("findLastAddedLinkByUserIdPositiveLink");
        link.setSecondsToCheck(60);
        link.setUrl("http://t6.tss2020.site");

        var user = new User();
        user.setUsername("findLastAddedLinkByUserIdPositiveUser");
        userService.save(user);
        var savedUser = userService.getUserByUsername("findLastAddedLinkByUserIdPositiveUser");

        link.setUser(savedUser);
        linkService.addLink(link);

        var actualLink = linkService.findLastAddedLinkByUserId(savedUser.getId());

        Assert.assertEquals(link.getUrl(), actualLink.getUrl());
        Assert.assertEquals(link.getName(), actualLink.getName());
    }

//Letter

    @Test
    public void saveLetter() {
        var letter = new Letter();
        letter.setBody("Hello saveLetter");
        letter.setSubject("Say hello saveLetter");
        var oldCount = letterService.getLettersCount();
        letterService.save(letter);
        var actualCount = letterService.getLettersCount();

        Assert.assertTrue(oldCount + 1 == actualCount);
    }

    //CheckLink
    @Test
    public void saveCheckLinkPositive() {
        var checkLink = new CheckLink();
        checkLink.setHttpStatus(200);
        checkLink.setCheckDate(new Date());
        checkLink.setStatus(LinkStatus.UP);
        checkLink.setAllWordsFind(true);
        checkLinkService.save(checkLink);
    }

    @Test
    public void getLinkChecksCountPositive() {
        var checkLink = new CheckLink();
        checkLink.setHttpStatus(200);
        checkLink.setCheckDate(new Date());
        checkLink.setStatus(LinkStatus.UP);
        checkLink.setAllWordsFind(true);
        checkLinkService.save(checkLink);
        var actualAmount = checkLinkService.getLinkChecksCount();
        Assert.assertTrue(actualAmount > 0);
    }

    @Test
    public void getChecksByLinkIdPositive() {
        var link = new Link();
        link.setName("getChecksByLinkIdPositiveLink");
        link.setSecondsToCheck(60);
        link.setUrl("http://t6.tss2020.site");

        var user = new User();
        user.setUsername("getChecksByLinkIdPositiveUser");
        userService.save(user);
        var savedUser = userService.getUserByUsername("getChecksByLinkIdPositiveUser");

        link.setUser(savedUser);
        linkService.addLink(link);
        var savedLink = linkService.findLinksByUserId(savedUser.getId());

        var checkLink = new CheckLink();
        checkLink.setHttpStatus(200);
        checkLink.setCheckDate(new Date());
        checkLink.setStatus(LinkStatus.UP);
        checkLink.setAllWordsFind(true);
        checkLink.setLink(savedLink.get(0));
        checkLinkService.save(checkLink);

        var actualAmount = checkLinkService.getChecksByLinkId(savedLink.get(0).getId());
        Assert.assertTrue(actualAmount.size() > 0);
    }

    //Agent
    @Test
    public void createAgentPositive() {
        var agent = new Agent();
        agent.setHost("createAgentHost");
        agent.setPublicKey("createAgentPublicKey");
        agent.setSecretKey("createAgentSecretKey");
        agentService.createAgent(agent);
        var actualResult = agentService.findAgentByPublicKey("createAgentPublicKey");
        Assert.assertNotNull(actualResult);
    }

    @Test
    public void updateAgentPositive() {
        var agent = new Agent();
        agent.setHost("updateAgentHost");
        agent.setPublicKey("updateAgentPublicKey");
        agent.setSecretKey("updateAgentSecretKey");
        agentService.createAgent(agent);
        var foundAgent = agentService.findAgentByPublicKey("updateAgentPublicKey");
        foundAgent.setHost("updateAgentHostNew");
        agentService.updateAgent(foundAgent);
        var actualAgent = agentService.findAgentByPublicKey("updateAgentPublicKey");
        Assert.assertEquals(foundAgent.getHost(), actualAgent.getHost());
    }

    @Test
    public void deleteAgentPositive() {
        var agent = new Agent();
        agent.setHost("deleteAgentPositiveHost");
        agent.setPublicKey("deleteAgentPositivePublicKey");
        agent.setSecretKey("deleteAgentPositiveSecretKey");
        agentService.createAgent(agent);
        var foundAgent = agentService.findAgentByPublicKey("deleteAgentPositivePublicKey");
        agentService.deleteAgent(foundAgent);
        var actualResult = agentService.findAgentByPublicKey("deleteAgentPositivePublicKey");
        Assert.assertNull(actualResult);
    }

    @Test
    public void amountOfServersForUserByUserIdPositive() {
        var user = new User();
        user.setUsername("amountOfServersForUserByUserIdPositiveUsername");
        var agent = new Agent();
        agent.setHost("amountOfServersForUserByUserIdPositiveHost");
        agent.setPublicKey("amountOfServersForUserByUserIdPositivePublicKey");
        agent.setSecretKey("amountOfServersForUserByUserIdPositiveSecretKey");
        userService.save(user);
        var savedUser =userService.getUserByUsername("amountOfServersForUserByUserIdPositiveUsername");
        agent.setUser(savedUser);
        agentService.createAgent(agent);

        Assert.assertTrue(1 == agentService.amountOfServersForUserByUserId(savedUser.getId()));
    }

    @Test
    public void findAgentsByUserIdPositive() {
        var user = new User();
        user.setUsername("findAgentsByUserIdPositiveUsername");
        var agent = new Agent();
        agent.setHost("findAgentsByUserIdPositiveHost");
        agent.setPublicKey("findAgentsByUserIdPositivePublicKey");
        agent.setSecretKey("findAgentsByUserIdPositiveSecretKey");
        userService.save(user);
        var savedUser =userService.getUserByUsername("findAgentsByUserIdPositiveUsername");
        agent.setUser(savedUser);
        agentService.createAgent(agent);

        Assert.assertTrue(1 == agentService.findAgentsByUserId(savedUser.getId()).size());
    }

    @Test
    public void findAgentsByActivePositive() {
        var agent = new Agent();
        agent.setHost("findAgentsByActivePositiveHost");
        agent.setPublicKey("findAgentsByActivePositivePublicKey");
        agent.setSecretKey("findAgentsByActivePositiveSecretKey");

        agentService.createAgent(agent);
        var actualResult = agentService.findAgentsByActive(new Date(new Date().getTime() - 10000), "YES");
        Assert.assertTrue(1 == actualResult.stream().filter(a ->
                a.getPublicKey().equals(agent.getPublicKey())).collect(Collectors.toList()).size());
    }

    //agentService

    @Test
    public void findAgentDataByAgentIdAndDaysPositive() {
        var agent = new Agent();
        agent.setPublicKey("findAgentDataByAgentIdAndDaysPositivePublicKey");
        agentService.createAgent(agent);

        var agentData = new AgentData();
        agentData.setAgentVersion("findAgentDataByAgentIdAndDaysPositiveVersion");
        var foundAgent = agentService.findAgentByPublicKey("findAgentDataByAgentIdAndDaysPositivePublicKey");
        agentData.setAgent(foundAgent);
        agentData.setTimeAdd(new Date());
        agentDataService.save(agentData);

        var actualResult = agentDataService.findAgentDataByAgentIdAndDays(foundAgent.getId(),
                new Date());
        Assert.assertTrue(0 <= actualResult.stream().filter(ad ->
                        ad.getAgentVersion().equals("findAgentDataByAgentIdAndDaysPositiveVersion"))
                        .count()
                );
    }

    @Test
    public void deleteAgentDataPositive() {
        var agent = new Agent();
        agent.setPublicKey("deleteAgentDataPositivePublicKey");
        agentService.createAgent(agent);

        var agentData = new AgentData();
        agentData.setAgentVersion("deleteAgentDataPositiveVersion");
        var foundAgent = agentService.findAgentByPublicKey("deleteAgentDataPositivePublicKey");
        agentData.setAgent(foundAgent);
        agentData.setTimeAdd(new Date());
        agentDataService.save(agentData);

        var foundAgentData = agentDataService.findAgentDataByAgentIdAndDays(foundAgent.getId(),
                new Date(new Date().getTime() + 5000));
        var savedAgentData = foundAgentData.stream().filter(ad ->
                ad.getAgentVersion().equals("deleteAgentDataPositiveVersion"))
                .collect(Collectors.toList()).get(0);

        agentDataService.deleteAgentData(savedAgentData);

        var actualResult = agentDataService.findAgentDataByAgentIdAndDays(foundAgent.getId(),
                new Date(new Date().getTime() + 5000));

        Assert.assertTrue(0 == actualResult.stream().filter(ad ->
                ad.getAgentVersion().equals("deleteAgentDataPositiveVersion"))
                .count()
        );
    }
}
