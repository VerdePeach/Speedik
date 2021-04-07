package com.in726.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.in726.app.Speedik;
import com.in726.app.database.service.AgentDataService;
import com.in726.app.database.service.AgentService;
import com.in726.app.database.service.LinkService;
import com.in726.app.database.service.UserService;
import com.in726.app.enums.Roles;
import com.in726.app.enums.TariffPlan;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.model.Agent;
import com.in726.app.model.AgentData;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.parser.EasyJsonParser;
import com.in726.app.security.PasswordEncoder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class SpeedikTest {

    @BeforeClass
    public static void setup() throws IOException {
        String[] args = new String[0];
        Speedik.main(args);
    }

    //registration
    @Test
    public void registrationPositive() throws URISyntaxException, IOException, InterruptedException {

        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/registration/registrationData.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        var message = response.body();
        Assert.assertEquals("User successfully created.", message);
    }

    @Test
    public void registrationUserBadEmailNegative() throws URISyntaxException, IOException, InterruptedException {

        var sendData = new String(Files.readAllBytes(Paths.get("src/main/test_resources/registration/registrationDataWrongEmail.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendData))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        var message = response.body();
        System.out.println(message);
        Assert.assertEquals("Incorrect data of new user.", message);
    }

    @Test
    public void registrationDuplicateUsernameNegative() throws URISyntaxException, IOException, InterruptedException {

        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/registration/registrationDuplicateUsername.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        var message = response.body();

        HttpRequest requestDuplicate = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> responseDuplicate =
                client.send(requestDuplicate, HttpResponse.BodyHandlers.ofString());

        var messageDuplicate = responseDuplicate.body();

        Assert.assertEquals("User successfully created.", message);
        Assert.assertEquals("User with such username already exists", messageDuplicate);
    }


    @Test
    public void registrationDuplicateEmailNegative() throws URISyntaxException, IOException, InterruptedException {

        var sendDate1 = new String(Files.readAllBytes(Paths.get("src/main/test_resources/registration/registrationDuplicateEmail1.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate1))
                .setHeader("ContentType", "application/json")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        var message = response.body();


        var sendDate2 = new String(Files.readAllBytes(Paths.get("src/main/test_resources/registration/registrationDuplicateEmail2.json")));

        HttpRequest requestDuplicate = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate2))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> responseDuplicate =
                client.send(requestDuplicate, HttpResponse.BodyHandlers.ofString());

        var messageDuplicate = responseDuplicate.body();

        Assert.assertEquals("User successfully created.", message);
        Assert.assertEquals("User with such email already exists", messageDuplicate);
    }

    //TODO: confirm account
    @Test
    public void confirmUserEmailPositive() throws IOException, URISyntaxException, InterruptedException {
        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/confirm/confirmUserPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        var message = response.body();

        var savedUser = new UserService().getUserByUsername("confirmUserEmailPositive");

        var confirmCode = "ea0cc26a3c63175aacc9ab8f78bad3ceff08d2e6171b915b78fe489a41e5b9e7bf130d8c5b5a4177563ae55205acef6418889e1d5b5f383836aaae3f3f6d76f4";

        HttpRequest requestConfirm = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/confirm/" + savedUser.getId() + "?confirmCode=" + confirmCode))
                .GET()
                .build();
        HttpResponse<String> responseConfirm =
                client.send(requestConfirm, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("User successfully created.", message);
        Assert.assertEquals("Email confirmed successfully", responseConfirm.body());
    }

    @Test
    public void confirmUserEmailNegative() throws IOException, URISyntaxException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestConfirm = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/confirm/392121321"))
                .GET()
                .build();
        HttpResponse<String> responseConfirm =
                client.send(requestConfirm, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Check the link or write to the speedik support team.", responseConfirm.body());
    }


    //TODO: login
    @Test
    public void loginPositive() throws URISyntaxException, IOException, InterruptedException {

        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/login/userForLoginRegPositive.json")));
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        var message = response.body();
        Assert.assertEquals("User successfully created.", message);

        var savedUser = new UserService().getUserByUsername("loginUserForReg");
        var confirmCode = "819fbb0219ab16b715ef1d14bec0258063d196554ba3dfe3d68c075388c063a095ca19dd461d682c08efd0f55c9f64662fcd3d41c144bb7fd20e6a06439454b8";
        HttpRequest requestConfirm = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/confirm/" + savedUser.getId() + "?confirmCode=" + confirmCode))
                .GET()
                .build();
        HttpResponse<String> responseConfirm =
                client.send(requestConfirm, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Email confirmed successfully", responseConfirm.body());

        var loginData = new String(Files.readAllBytes(Paths.get("src/main/test_resources/login/loginUserPositive.json")));
        HttpRequest requestLogin = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/login"))
                .POST(HttpRequest.BodyPublishers.ofString(loginData))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> responseLogin =
                client.send(requestLogin, HttpResponse.BodyHandlers.ofString());

        var messageLogin = responseLogin.body();
        Assert.assertNotNull(EasyJsonParser.parseJsonToObject(messageLogin, User.class));
        Assert.assertTrue(responseLogin.headers().firstValue("Authorization").isPresent());
    }

    @Test
    public void loginNotConfirmedEmailNegative() throws URISyntaxException, IOException, InterruptedException {

        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/login/loginNotConfimedEmailUserReg.json")));
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        var message = response.body();
        Assert.assertEquals("User successfully created.", message);

        var loginData = new String(Files.readAllBytes(Paths.get("src/main/test_resources/login/userForLoginRegNotConfirmedNegative.json")));
        HttpRequest requestLogin = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/login"))
                .POST(HttpRequest.BodyPublishers.ofString(loginData))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> responseLogin =
                client.send(requestLogin, HttpResponse.BodyHandlers.ofString());

        var messageLogin = responseLogin.body();
        Assert.assertEquals("Email is not confirmed."
                + " Confirm your email, look for a letter in the mail box.", messageLogin);
    }

    @Test
    public void loginIncorrectPassNegative() throws URISyntaxException, IOException, InterruptedException {
        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/login/userForLoginIncorrectPass.json")));
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        var message = response.body();
        Assert.assertEquals("User successfully created.", message);

        var savedUser = new UserService().getUserByUsername("userForLoginIncorrectPass");
        var confirmCode = "2da4aebabc030944403aedefb3af042a20a21842c067239f6ea4554d2136a2c14b1a45cdee52ed3cda9a828fa4883fc5006cf411881dc485e3534a4ae32451f1";
        HttpRequest requestConfirm = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/confirm/" + savedUser.getId() + "?confirmCode=" + confirmCode))
                .GET()
                .build();
        HttpResponse<String> responseConfirm =
                client.send(requestConfirm, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Email confirmed successfully", responseConfirm.body());

        var loginData = new String(Files.readAllBytes(Paths.get("src/main/test_resources/login/loginUserIncorrectPassNegative.json")));
        HttpRequest requestLogin = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/login"))
                .POST(HttpRequest.BodyPublishers.ofString(loginData))
                .setHeader("ContentType", "application/json")
                .build();

        HttpResponse<String> responseLogin =
                client.send(requestLogin, HttpResponse.BodyHandlers.ofString());

        var messageLogin = responseLogin.body();
        Assert.assertEquals("Incorrect username or password.", messageLogin);
    }

    //TODO: tariff
    @Test
    public void chooseTariffFreeToPremiumPositive() throws IOException, URISyntaxException, InterruptedException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/tariff/chooseTariffRegUser.json")));
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(userToCreate))
                .setHeader("ContentType", "application/json")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        var message = response.body();
        Assert.assertEquals("User successfully created.", message);

        var savedUser = new UserService().getUserByUsername("chooseTariffRegUser");
        var confirmCode = "9b8868989a945ecc37d8735775cfd4279071204e52fb0f0ca2a74dbb146ef98b2ca6bea88050af1397baf376a468bcdf7399db89b30efd497170b0edd9c29154";
        HttpRequest requestConfirm = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/confirm/" + savedUser.getId() + "?confirmCode=" + confirmCode))
                .GET()
                .build();
        HttpResponse<String> responseConfirm =
                client.send(requestConfirm, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Email confirmed successfully", responseConfirm.body());


        HttpRequest requestTariffPremium = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/tariff/" + savedUser.getId() + "/PREMIUM"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDY4NjgzNzA2LCJ1c2VybmFtZSI6ImNob29zZVRhcmlmZlJlZ1VzZXIifQ.vag6iQ9qg6Blp1KFVk0-wQWHmt9Z2PKoQrRzeDxG8Ok")
                .build();
        HttpResponse<String> responseTariffPremium =
                client.send(requestTariffPremium, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Tariff changed successfully.", responseTariffPremium.body());

        HttpRequest requestTariffFree = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/tariff/" + savedUser.getId() + "/FREE"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDY4NjgzNzA2LCJ1c2VybmFtZSI6ImNob29zZVRhcmlmZlJlZ1VzZXIifQ.vag6iQ9qg6Blp1KFVk0-wQWHmt9Z2PKoQrRzeDxG8Ok")
                .build();
        HttpResponse<String> responseTariffFree =
                client.send(requestTariffFree, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Tariff changed successfully.", responseTariffFree.body());

        HttpRequest requestTariffFreeToFree = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/tariff/" + savedUser.getId() + "/FREE"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDY4NjgzNzA2LCJ1c2VybmFtZSI6ImNob29zZVRhcmlmZlJlZ1VzZXIifQ.vag6iQ9qg6Blp1KFVk0-wQWHmt9Z2PKoQrRzeDxG8Ok")
                .build();
        HttpResponse<String> responseTariffFreeToFree =
                client.send(requestTariffFreeToFree, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Impossible to change tariff. Amount of servers or links is bigger than allowed for the new tariff", responseTariffFreeToFree.body());
    }

    @Test
    public void chooseTariffUnrecognizedUserNegative() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestTariff = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/tariff/" + 13123213 + "/PREMIUM"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse<String> responseTariffPremium =
                client.send(requestTariff, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Unauthorized user", responseTariffPremium.body());
    }

    //TODO: agent
    @Test
    public void addAgentWithSecretKeyTariffFreePositive() throws URISyntaxException, IOException, InterruptedException {

        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agent/addAgentUserRegTariffFree.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername(user.getUsername());

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestTariff = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/agent/" + foundUser.getId() + "/ddAgentWithSecretKeyTariffFreePositive/ddAgentWithSecretKeyTariffFreePositive"))
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDY4NjgzNzA2LCJ1c2VybmFtZSI6ImFkZEFnZW50V2l0aFNlY3JldEtleVBvc2l0aXZlVXNlciJ9.9IHaSPHYBgYLJH6_1FEqmGpcnUWEcMwg_roopf8ZF9Q")
                .build();
        HttpResponse<String> responseTariffPremium =
                client.send(requestTariff, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Agent created successfully", responseTariffPremium.body());
    }

    @Test
    public void addAgentWithSecretKeyTariffFreeManyAgentsNegative() throws URISyntaxException, IOException, InterruptedException {

        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agent/addAgentUserRegTariffFreeManyAgentsNegative.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername(user.getUsername());

        int i = 3;
        while (i > 0) {
            HttpClient client = HttpClient
                    .newBuilder()
                    .build();
            HttpRequest requestTariff = HttpRequest
                    .newBuilder(new URI("http://localhost:8080/api/agent/" + foundUser.getId() + "/addAgentPublicKey" + i + "/addAgentSecretKey"))
                    .PUT(HttpRequest.BodyPublishers.ofString(""))
                    .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDY4NjgzNzA2LCJ1c2VybmFtZSI6ImFkZEFnZW50V2l0aFNlY3JldEtleVRhcmlmZkZyZWVNYW55QWdlbnRzTmVnYXRpdmVVc2VyIn0.iSvUz0TtNvxaFx2NivBh-Jqtf0bCycEZ-BstaRnUhcY")
                    .build();
            HttpResponse<String> responseManyAgentsNegative =
                    client.send(requestTariff, HttpResponse.BodyHandlers.ofString());
            i--;
            if (i == 0)
                Assert.assertEquals("Achieves max number of servers for tariff FREE", responseManyAgentsNegative.body());
        }
    }

    @Test
    public void addAgentAlreadyExistsNegative() throws URISyntaxException, IOException, InterruptedException {

        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agent/addAgentAlreadyExistsNegative.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername(user.getUsername());

        var i = 2;
        while (i > 0) {
            HttpClient client = HttpClient
                    .newBuilder()
                    .build();
            HttpRequest requestTariff = HttpRequest
                    .newBuilder(new URI("http://localhost:8080/api/agent/" + foundUser.getId() + "/addAgentAlreadyExistsNegative/addAgentAlreadyExistsNegative"))
                    .PUT(HttpRequest.BodyPublishers.ofString(""))
                    .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDY4NjgzNzA2LCJ1c2VybmFtZSI6ImFkZEFnZW50QWxyZWFkeUV4aXN0c05lZ2F0aXZlIn0.MHIOtY5haAK1MAZ6nT3iD9dbheyfeNvewhCesytXZHY")
                    .build();
            HttpResponse<String> responseTariffPremium =
                    client.send(requestTariff, HttpResponse.BodyHandlers.ofString());
            i--;
            if (i == 0)
                Assert.assertEquals("Agent already exists", responseTariffPremium.body());
        }

    }

    @Test
    public void addAgentTakeTokenNegative() throws URISyntaxException, IOException, InterruptedException {

        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agent/addAgentTakeTokenNegative.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername(user.getUsername());

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestTariff = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/agent/" + foundUser.getId() + "/addAgentTakeTokenNegative/addAgentTakeTokenNegative"))
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDY4NjgzNzA2LCJ1c2VybmFtZSI6ImFkZEFnZW50V2l0aFNlY3JldEtleVBvc2l0aXZlVXNlciJ9.9IHaSPHYBgYLJH6_1FEqmGpcnUWEcMwg_roopf8ZF9Q")
                .build();
        HttpResponse<String> responseTariffPremium =
                client.send(requestTariff, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Incorrect data", responseTariffPremium.body());
    }

    //TODO: add agentData
    @Test
    public void saveAgentDataPositive() throws IOException, URISyntaxException, InterruptedException {

        var agent = new Agent();
        agent.setPublicKey("saveAgentDataPositive");
        agent.setSecretKey("saveAgentDataPositive");
        var agentService = new AgentService();
        agentService.createAgent(agent);

        var agentDataToSave = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agentData/saveAgentDataPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/endpoint"))
                .POST(HttpRequest.BodyPublishers.ofString(agentDataToSave))
                .setHeader("Sign", "7396f2b653987cc646c27e12a42e09a4a708079371f42abd79307c3f29af7955")
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("success", responseAgentData.body());
    }

    @Test
    public void saveAgentDataNoSignNegative() throws IOException, URISyntaxException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/endpoint"))
                .POST(HttpRequest.BodyPublishers.ofString("agentDataToSave"))
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Object was not saved", responseAgentData.body());
    }

    @Test
    public void saveAgentDataIncorrectDataNegative() throws IOException, URISyntaxException, InterruptedException {
        var agentDataToSave = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agentData/saveAgentDataNoSing.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/endpoint"))
                .POST(HttpRequest.BodyPublishers.ofString(agentDataToSave))
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("Incorrect data", responseAgentData.body());
    }

    @Test
    public void getAllAgentsByUserIdPositive() throws IOException, URISyntaxException, InterruptedException {

        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agentData/getAllUserPositive.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername(user.getUsername());

        var agent = new Agent();
        agent.setPublicKey("getAllAgentsByUserIdPositive");
        agent.setSecretKey("getAllAgentsByUserIdPositive");
        agent.setUser(foundUser);
        var agentService = new AgentService();
        agentService.createAgent(agent);

        var agentDataToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/agentData/getAllAgentDataPositive.json")));
        var agentData = EasyJsonParser.parseJsonToObject(agentDataToCreate, AgentData.class);
        var agentDataService = new AgentDataService();
        agentData.setAgent(agent);
        agentData.setTimeAdd(new Date(new Date().getTime() + 50000 * 5000000));
        agentDataService.save(agentData);
        agentDataService.save(agentData);


        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/agents/" + foundUser.getId()))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImdldEFsbEFnZW50c0J5VXNlcklkUG9zaXRpdmUifQ.FadKfPWYMLUymzDr1O6oErRs-StjNRtllB8mKLUDRys")
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseAgentData.body());
        Assert.assertNotNull(responseAgentData.body());
        Assert.assertTrue(responseAgentData.body().length() > 200);
    }

    @Test
    public void getAllAgentsByUserIdNegative() throws IOException, URISyntaxException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/agents/45467567"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImdldEFsbEFnZW50c0J5VXNlcklkUG9zaXRpdmUifQ.FadKfPWYMLUymzDr1O6oErRs-StjNRtllB8mKLUDRys")
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseAgentData.body());
        Assert.assertNotNull("Unauthorized user", responseAgentData.body());
    }

    @Test
    public void resetUserPasswordByMailPositive() throws URISyntaxException, IOException, InterruptedException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/peset_pass/resetUserPasswordByMailPositive.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reset/password/email"))
                .POST(HttpRequest.BodyPublishers.ofString("resetUserPasswordByMailPositive@gmail.com"))
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseAgentData.body());
        Assert.assertNotNull("success", responseAgentData.body());
    }

    @Test
    public void resetUserPasswordByMailNoSuchUserNegative() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reset/password/email"))
                .POST(HttpRequest.BodyPublishers.ofString("dkpasfjsdfpafjojafeokpojgrargij@gmail.com"))
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseAgentData.body());
        Assert.assertNotNull("User with such email is not found.", responseAgentData.body());
    }


    @Test
    public void resetUserPasswordByMailNoSendDataNegative() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestAgentData = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reset/password/email"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse<String> responseAgentData =
                client.send(requestAgentData, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseAgentData.body());
        Assert.assertNotNull("Incorrect email.", responseAgentData.body());
    }

    @Test
    public void resetUserPasswordByUserPositive() throws URISyntaxException, IOException, InterruptedException, NoSuchAlgorithmException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/old_pass/resetUserPasswordByUserPositiveUser.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        user.setPassword(PasswordEncoder.hashPassword(user.getPassword()));
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername("resetUserPasswordByUserPositive");

        var passwords = new String(Files.readAllBytes(Paths.get("src/main/test_resources/old_pass/resetUserPasswordByUserPositivePass.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reset/password/" + foundUser.getId()))
                .POST(HttpRequest.BodyPublishers.ofString(passwords))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6InJlc2V0VXNlclBhc3N3b3JkQnlVc2VyUG9zaXRpdmUifQ.h6EWA1ujRha0IRgsDhwa4VM6BNvaKGirQj-uo7VeAs8")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Password changed successfully", response.body());
    }

    @Test
    public void resetUserPasswordByUserNoMachNegative() throws URISyntaxException, IOException, InterruptedException, NoSuchAlgorithmException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/old_pass/resetUserPasswordByUserNoMachNegativeUser.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        user.setPassword(PasswordEncoder.hashPassword(user.getPassword()));
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername("resetUserPasswordByUserNoMachNegative");

        var passwords = new String(Files.readAllBytes(Paths.get("src/main/test_resources/old_pass/resetUserPasswordByUserNoMachNegativePass.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reset/password/" + foundUser.getId()))
                .POST(HttpRequest.BodyPublishers.ofString(passwords))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6InJlc2V0VXNlclBhc3N3b3JkQnlVc2VyTm9NYWNoTmVnYXRpdmUifQ.GlITaYvwGYXJKQeNK7GZ6Tc4Fu2_2Zr6pNj4AvFSOx0")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Old password is not matched", response.body());
    }

    @Test
    public void resetUserPasswordByUserNoUserIdNegative() throws URISyntaxException, IOException, InterruptedException, NoSuchAlgorithmException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reset/password/" + 7867465))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6InJlc2V0VXNlclBhc3N3b3JkQnlVc2VyTm9NYWNoTmVnYXRpdmUifQ.GlITaYvwGYXJKQeNK7GZ6Tc4Fu2_2Zr6pNj4AvFSOx0")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Unauthorized user", response.body());
    }

    @Test
    public void addLinkForCheckPremiumPositive() throws IOException, URISyntaxException, InterruptedException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckPositiveUser.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername("addLinkForCheckPositive");

        var linkToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/link/" + foundUser.getId()))
                .PUT(HttpRequest.BodyPublishers.ofString(linkToCreate))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImFkZExpbmtGb3JDaGVja1Bvc2l0aXZlIn0.JeUSR94JVcvSky4Dd6YLK-jg8uMK6BXgy1b-XV7SOQs")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Link created successfully.", response.body());
    }

    @Test
    public void addLinkForCheckFreePositive() throws IOException, URISyntaxException, InterruptedException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckPositiveFreeUser.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername("addLinkForCheckPositiveFree");

        var linkToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/link/" + foundUser.getId()))
                .PUT(HttpRequest.BodyPublishers.ofString(linkToCreate))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImFkZExpbmtGb3JDaGVja1Bvc2l0aXZlRnJlZSJ9.5q_XrLsl9SaB-wKGAuBQpDUJFQCAK7CKBUUlsZg7lK4")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Link created successfully.", response.body());
    }

    @Test
    public void addLinkForCheckIncorrectLinkNegative() throws IOException, URISyntaxException, InterruptedException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckIncorrectLinkNegativeUser.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername("addLinkForCheckIncorrectLinkNegative");

        var linkToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckIncorrectLinkNegativeLink.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/link/" + foundUser.getId()))
                .PUT(HttpRequest.BodyPublishers.ofString(linkToCreate))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImFkZExpbmtGb3JDaGVja0luY29ycmVjdExpbmtOZWdhdGl2ZSJ9.1sRK-fFYm43a4hnIjiVDkV8Oof0nJVA5mIL61r36gGg")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Incorrect data.", response.body());
    }

    @Test
    public void addLinkForCheckMaxNumOfLinkPremiumNegative() throws IOException, URISyntaxException, InterruptedException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckMaxNumOfLinkPremiumNegativeUser.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername("addLinkForCheckMaxNumOfLinkPremiumNegative");

        var linkToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/addLinkForCheckMaxNumOfLinkPremiumNegativeLink.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        var i = 11;
        while (i > 0) {
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:8080/api/link/" + foundUser.getId()))
                    .PUT(HttpRequest.BodyPublishers.ofString(linkToCreate))
                    .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImFkZExpbmtGb3JDaGVja01heE51bU9mTGlua1ByZW1pdW1OZWdhdGl2ZSJ9.2pTxCMxo-TXEBpog9bIpB_Z1kr0HKvxVyZkyGvy6r3Y")
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            i--;
            if (i == 0) {
                Assert.assertEquals("Achieves max number of links for user tariff PREMIUM", response.body());
                return;
            }
        }
        Assert.assertTrue(false);
    }

    @Test
    public void addLinkForCheckNoUserNegative() throws IOException, URISyntaxException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/link/45647"))
                .PUT(HttpRequest.BodyPublishers.ofString("linkToCreate"))
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImFkZExpbmtGb3JDaGVja0luY29ycmVjdExpbmtOZWdhdGl2ZSJ9.1sRK-fFYm43a4hnIjiVDkV8Oof0nJVA5mIL61r36gGg")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Unauthorized userId", response.body());
    }


    @Test
    public void getLinksPositive() throws IOException, URISyntaxException, InterruptedException {
        var userToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/getLinksPositiveUser.json")));
        var user = EasyJsonParser.parseJsonToObject(userToCreate, User.class);
        var userService = new UserService();
        userService.save(user);
        var foundUser = userService.getUserByUsername("getLinksPositive");

        var linkToCreate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/link/getLinksPositiveLink.json")));
        var link = EasyJsonParser.parseJsonToObject(linkToCreate, Link.class);
        link.setUser(foundUser);
        new LinkService().addLink(link);

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/link/" + foundUser.getId()))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IlVTRVIiLCJleHAiOjE2MDU4MzA0NzY3LCJ1c2VybmFtZSI6ImdldExpbmtzUG9zaXRpdmUifQ.zK2vvJQIuGTFOo4in3jpV-4NwzW_q5SvlqcrlwnNPmo")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertNotNull(response.body());
        Assert.assertTrue(response.body().length() > 200);
    }

    //TODO: statistics
    @Test
    public void letterStatisticPositive() throws IOException, InterruptedException, URISyntaxException {
        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/statistic/userTorRegPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestUser = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        client.send(requestUser, HttpResponse.BodyHandlers.ofString());

        var userService = new UserService();
        var foundUser = userService.getUserByUsername("letterStatisticPositive");
        foundUser.setConfirm(YesNoStatus.YES);
        foundUser.setTariff(TariffPlan.ADMIN);
        foundUser.setRole(Roles.ADMIN);
        userService.update(foundUser);

        HttpRequest requestStatistic = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/letter"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJsZXR0ZXJTdGF0aXN0aWNQb3NpdGl2ZSJ9.jJYcAZAg3E1u5-AJfW06va1MvRsneg7lWI7BfhQviiU")
                .build();
        HttpResponse<String> responseStatistic =
                client.send(requestStatistic, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(Integer.parseInt(responseStatistic.body()) >= 1);
    }

    @Test
    public void letterStatisticNegative() throws IOException, InterruptedException, URISyntaxException {
        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/statistic/letterStatisticNegativeUser.json")));
        var user = EasyJsonParser.parseJsonToObject(sendDate, User.class);
        new UserService().save(user);

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestStatistic = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/letter"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJhZ2VudFN0YXRpc3RpY1Bvc3NmZHNpdGl2ZSJ9.GMn3ETucGwWMCz_SDT2sAOts3P-UShUoCP4hBxBWi8o")
                .build();
        HttpResponse<String> responseStatistic =
                client.send(requestStatistic, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Unauthorized user", responseStatistic.body());
    }

    @Test
    public void linkStatisticPositive() throws URISyntaxException, IOException, InterruptedException {
        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/statistic/userToRegLinkStatisticPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestUser = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        client.send(requestUser, HttpResponse.BodyHandlers.ofString());

        var userService = new UserService();
        var foundUser = userService.getUserByUsername("linkStatisticPositive");
        foundUser.setConfirm(YesNoStatus.YES);
        foundUser.setTariff(TariffPlan.ADMIN);
        foundUser.setRole(Roles.ADMIN);
        userService.update(foundUser);

        HttpRequest requestStatistic = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/link"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJsaW5rU3RhdGlzdGljUG9zaXRpdmUifQ.jc6nPwa27I3t4Uq06p6EQSduiW2ePysdObjxde88vjA")
                .build();
        HttpResponse<String> responseStatistic =
                client.send(requestStatistic, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(Integer.parseInt(responseStatistic.body()) >= 0);

    }

    @Test
    public void linkStatisticNegative() throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest requestStatistic = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/link"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJsaW5rU3RhdGlzZHdzdGljUG9zaXRpdmUifQ.viqR0k8sHYIbr82LtUdGMa9ET16aNqFHIr5pQy5ISXM")
                .build();
        HttpResponse<String> responseStatistic =
                client.send(requestStatistic, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Unauthorized user", responseStatistic.body());
    }

    @Test
    public void agentStatisticPositive() throws URISyntaxException, IOException, InterruptedException {
        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/statistic/userToRegAgentStatisticPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestUser = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        client.send(requestUser, HttpResponse.BodyHandlers.ofString());

        var userService = new UserService();
        var foundUser = userService.getUserByUsername("agentStatisticPositive");
        foundUser.setConfirm(YesNoStatus.YES);
        foundUser.setTariff(TariffPlan.ADMIN);
        foundUser.setRole(Roles.ADMIN);
        userService.update(foundUser);


        HttpRequest requestStatisticYes = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/agent/YES"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJhZ2VudFN0YXRpc3RpY1Bvc2l0aXZlIn0.kwv4KzPR_BDP2Frn-e5vIKCMeY1xgSwDvtTT1sF7G4A")
                .build();
        HttpResponse<String> responseStatisticYes =
                client.send(requestStatisticYes, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestStatisticNo = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/agent/NO"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJhZ2VudFN0YXRpc3RpY1Bvc2l0aXZlIn0.kwv4KzPR_BDP2Frn-e5vIKCMeY1xgSwDvtTT1sF7G4A")
                .build();
        HttpResponse<String> responseStatisticNo =
                client.send(requestStatisticNo, HttpResponse.BodyHandlers.ofString());

        try {
            new ObjectMapper().readTree(responseStatisticYes.body());
            new ObjectMapper().readTree(responseStatisticNo.body());
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void agentStatisticNegative() throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest requestStatistic = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/agent/NO"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJhZ2VudFN0YXRpc3RpY1Bvc3NmZHNpdGl2ZSJ9.GMn3ETucGwWMCz_SDT2sAOts3P-UShUoCP4hBxBWi8o")
                .build();
        HttpResponse<String> responseStatistic =
                client.send(requestStatistic, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Incorrect data", responseStatistic.body());
    }

    @Test
    public void userStatisticPositive() throws IOException, URISyntaxException, InterruptedException {
        var sendDate = new String(Files.readAllBytes(Paths.get("src/main/test_resources/statistic/userToRegUserStatisticPositive.json")));

        HttpClient client = HttpClient
                .newBuilder()
                .build();
        HttpRequest requestUser = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/reg"))
                .POST(HttpRequest.BodyPublishers.ofString(sendDate))
                .setHeader("ContentType", "application/json")
                .build();
        client.send(requestUser, HttpResponse.BodyHandlers.ofString());

        var userService = new UserService();
        var foundUser = userService.getUserByUsername("userStatisticNewPositive");
        foundUser.setConfirm(YesNoStatus.YES);
        foundUser.setTariff(TariffPlan.ADMIN);
        foundUser.setRole(Roles.ADMIN);
        userService.update(foundUser);


        HttpRequest requestStatisticYes = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/user/YES"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJ1c2VyU3RhdGlzdGljTmV3UG9zaXRpdmUifQ.Z3kdPRV8-YlYcLHKZnyNhJX4sRB9cZ_AZiukL9my2s0")
                .build();
        HttpResponse<String> responseStatisticYes =
                client.send(requestStatisticYes, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestStatisticNo = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/user/NO"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJ1c2VyU3RhdGlzdGljTmV3UG9zaXRpdmUifQ.Z3kdPRV8-YlYcLHKZnyNhJX4sRB9cZ_AZiukL9my2s0")
                .build();
        HttpResponse<String> responseStatisticNo =
                client.send(requestStatisticNo, HttpResponse.BodyHandlers.ofString());

        try {
            new ObjectMapper().readTree(responseStatisticYes.body());
            new ObjectMapper().readTree(responseStatisticNo.body());
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void userStatisticNegative() throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest requestStatistic = HttpRequest
                .newBuilder(new URI("http://localhost:8080/api/statistic/user/YES"))
                .GET()
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsZXZlbCI6IkFETUlOIiwiZXhwIjoxNjA1ODMwNDc2NywidXNlcm5hbWUiOiJhZ2VudFN0YXRpc3RpY1Bvc3NmZHNpdGl2ZSJ9.GMn3ETucGwWMCz_SDT2sAOts3P-UShUoCP4hBxBWi8o")
                .build();
        HttpResponse<String> responseStatistic =
                client.send(requestStatistic, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals("Incorrect data", responseStatistic.body());
    }
}
