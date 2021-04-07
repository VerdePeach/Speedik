package com.in726.app;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.in726.app.convertor.AgentConvertor;
import com.in726.app.database.HibernateUtil;

import com.in726.app.database.service.*;
import com.in726.app.email.MailUtil;
import com.in726.app.enums.Period;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.enums.Roles;
import com.in726.app.enums.TariffPlan;
import com.in726.app.model.Agent;
import com.in726.app.model.Dashboard;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.Letter;
import com.in726.app.model.sub_functional_model.Link;

import com.in726.app.parser.EasyJsonParser;
import com.in726.app.parser.json_model.AgentJson;
import com.in726.app.security.*;
import com.in726.app.security.jwt.*;
import com.in726.app.tariff.TariffUtil;
import io.javalin.Javalin;
import io.javalin.core.security.Role;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.in726.app.security.UserDataValidator.*;
import static com.in726.app.security.jwt.JWTAccessManager.isTokenBelongsToUser;

/**
 * Speedik is the main class of the application.
 */
public class Speedik {

    private static final UserService userService = new UserService();
    private static final AgentService agentService = new AgentService();
    private static final LinkService linkService = new LinkService();
    private static final LetterService letterService = new LetterService();
    private static final TariffUtil tariffUtil = new TariffUtil();
    private static final DashboardService dashboardService = new DashboardService();

//    private static PasswordEncoder passwordEncoder = new PasswordEncoder();

    /**
     * Main method of the app.
     * Includes realisation of endpoints.
     *
     * @param args arguments for main function
     */
    public static void main(String[] args) throws IOException {

        Logger logger = LoggerFactory.getLogger(Speedik.class);

        HibernateUtil.init();
        logger.info("\"Hibernate\" initiated");

        //Remote exceptions monitoring. logger
//        Rollbar rollbar = Rollbar.init(ConfigBuilder.withAccessToken("62a8a4e7a8104402a4ada636074f1ba0").build());
//        rollbar.log("\"Hibernate\" initiated");

        JWTProvider provider = ProviderExample.createHMAC512();

        Map<String, Role> rolesMapping = new HashMap<>() {{
            put("USER", Roles.USER);
            put("VIP_USER", Roles.USER_VIP);
            put("ADMIN", Roles.ADMIN);
        }};

        JWTAccessManager accessManager = new JWTAccessManager("level", rolesMapping, Roles.ANYONE);

        Javalin app = Javalin.create()
                .start(8080);
        app.config.accessManager(accessManager);
        /*
         * A decode handler which captures the value of a JWT from an
         * authorization header in the form of "Bearer {jwt}". The handler
         * decodes and verifies the JWT then puts the decoded object as
         * a context attribute for future handlers to access directly.
         */
        Handler decodeHandler = JavalinJWT.createHeaderDecodeHandler(provider);

        app.before(decodeHandler);

        Handler loginHandler = context -> {
            var persistent = EasyJsonParser.parseJsonToObject(context.body(), Persistent.class);
            var user = userService.getUserByUsername(persistent.getUsername());
            //TODO separate user != null to send correct message.
            if (user != null && user.getConfirm().toString().toUpperCase().equals("YES")) {
                if (user.getPassword().equals(PasswordEncoder.hashPassword(persistent.getPassword()))) {
                    var mockUser = new MockUser(user.getUsername(), user.getRole().toString());
                    String token = provider.generateToken(mockUser);

                    context.header("Authorization", "Bearer " + new JWTResponse(token).getJwt());
                    user.setPassword(null);
                    userService.setUserActiveByUserId(user.getId());
                    context.json(user);
                } else {
                    context.status(401).result("Incorrect username or password.");
                }
            } else {
                context.status(403).result("Email is not confirmed."
                        + " Confirm your email, look for a letter in the mail box.");
            }
        };

        Handler deleteAgentHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                int userId = Integer.parseInt(ctx.pathParam("userId"));
                int agentId = Integer.parseInt(ctx.pathParam("agentId"));
                var user = userService.getUserById(userId);
                if (user != null && isTokenBelongsToUser(decodedJWT, user.getUsername())) {
                    var agent = agentService.findByAgentId(agentId);
                    if (agent != null) {
                        agentService.deleteAgent(agent);
                        ctx.result("Agent deleted").status(200);
                        return;
                    } else {
                        ctx.result("Bad request").status(400);
                        return;
                    }
                } else {
                    ctx.result("Unauthorized user").status(401);
                }
            }
        };

        Handler saveAgentDataHandler = ctx -> {
            try {
                var requestBody = ctx.body();
                var agentJson = EasyJsonParser.parseJsonToObject(
                        requestBody,
                        AgentJson.class,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"));

                var agent = new AgentConvertor().convertAgentJsonToAgent(agentJson);

                var agentFound = agentService.findAgentByPublicKey(agent.getPublicKey());

                var sign = ctx.header("Sign");
                if (agentFound != null
                        && !sign.equals("")
                        && sign.equals(EncoderSHA256HMAC.encodeString(agentFound.getSecretKey(), requestBody))) {

                    agent.setId(agentFound.getId());
                    agent.setUser(agentFound.getUser());
                    agent.setPublicKey(agentFound.getPublicKey());
                    agent.setSecretKey(agentFound.getSecretKey());
                    agentService.updateAgent(agent);
                    ctx.result("success").status(200);
                } else {
                    ctx.result("Incorrect data").status(403);
                }
            } catch (Exception ex) {
                ctx.result("Object was not saved").status(400);
            }
        };

        Handler getAllAgentsByUserIdHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            var userId = Integer.parseInt(ctx.pathParam("userId"));
            if (decodedJWT != null && isTokenBelongsToUser(decodedJWT, userId)) {
                var agents = agentService.findAgentsByUserId(userId);
                // Get data by last 2 hours
                if (agents != null)
                    agents.stream().forEach(agent ->
                            agent.setAgentData(agent.getAgentData().stream()
                                    .filter(agentData -> agentData.getTimeAdd().getTime()
                                            >= new Date().getTime() - 3600000 * 2) // 2 hours
                                    .collect(Collectors.toList()))
                    );
                ctx.json(agents != null ? agents : "This user does not have any agents").status(200);
            } else {
                ctx.result("Unauthorized user").status(401);
            }
        };

        Handler registrationUserHandler = context -> {
            var persistent = EasyJsonParser.parseJsonToObject(context.body(), Persistent.class);

//            if (persistent == null) {
//                context.result("Incorrect data of new user.").status(403);
//            }

            persistent.setEmail(persistent.getEmail().trim());
            persistent.setUsername(persistent.getUsername().trim());
            persistent.setPassword(persistent.getPassword().trim());

            if (persistent.getUsername() == null
                    || persistent.getEmail() == null
                    || persistent.getPassword() == null
                    || persistent.getUsername().equals("")
                    || persistent.getEmail().equals("")
                    || persistent.getPassword().equals("")
                    || !isUsername(persistent.getUsername())
                    || !isEmail(persistent.getEmail())) {
                context.result("Incorrect data of new user.").status(403);
            } else {

                var userByUsername = userService.getUserByUsername(persistent.getUsername());
                var userByEmail = userService.getUserByEmail(persistent.getEmail());
                if (userByUsername != null && userByUsername.getUsername().equals(persistent.getUsername())) {
                    context.status(400).result("User with such username already exists");
                } else if (userByEmail != null && userByEmail.getEmail().equals(persistent.getEmail())) {
                    context.status(400).result("User with such email already exists");
                } else {
                    var newUser = new User();
                    newUser.setPassword(PasswordEncoder.hashPassword(persistent.getPassword()));
                    newUser.setUsername(persistent.getUsername());
                    newUser.setEmail(persistent.getEmail());

                    newUser.setRole(Roles.USER);
                    newUser.setConfirm(YesNoStatus.NO);
                    newUser.setTariff(TariffPlan.FREE);
                    userService.save(newUser);

                    var createdUser = userService.getUserByUsername(newUser.getUsername());
                    if (createdUser != null) {
                        var emailOfNewUser = createdUser.getEmail();

                        new Thread(() -> {
                            try {
                                var letter = new Letter();
                                letter.setBody("Click on the next link to confirm your email:"
                                        + " https://t6.tss2020.site/api/confirm/" + createdUser.getId()
                                        + "?confirmCode=" + MailUtil.createEmailConfirmHash(emailOfNewUser)
                                        + "\nIf you did not register in Speedik service, just ignore this message.");
                                letter.setSubject("Email confirmation");
                                letter.setUser(createdUser);

                                MailUtil.sendMail(emailOfNewUser, letter.getBody(), letter.getSubject());
                                letterService.save(letter);
                            } catch (MessagingException e) {
                                //logger.error(e.getMessage());
                                //e.printStackTrace();
                            }
                        }).run();


                        context.result("User successfully created.").status(200);
                    } //else {
//                        context.result("User creation failed.").status(403);
//                    }
                }
            }
        };

        Handler addAgentWithSecretKeyHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                int userId = Integer.parseInt(ctx.pathParam("userId"));
                var publicKey = ctx.pathParam("publicKey");
                var secretKey = ctx.pathParam("secretKey");

                var user = userService.getUserById(userId);
                if (user != null
                        && isTokenBelongsToUser(decodedJWT, user.getUsername())
                        && isAgentWithKeys(publicKey, secretKey)) {
                    if (agentService.findAgentByPublicKey(publicKey) == null) {
                        if (tariffUtil.isPossibleToAddServerForUserTariff(user)) {
                            Agent agent = new Agent();
                            agent.setUser(user);
                            agent.setPublicKey(publicKey);
                            agent.setSecretKey(secretKey);
                            agentService.createAgent(agent);
                            ctx.result("Agent created successfully").status(200);
                        } else {
                            ctx.result("Achieves max number of servers for tariff "
                                    + user.getTariff().name()).status(400);
                        }
                    } else {
                        ctx.result("Agent already exists").status(400);
                    }
                } else {
                    ctx.result("Incorrect data").status(400);
                }
            }
        };

        Handler confirmUserEmailHandler = ctx -> {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            var user = userService.getUserById(userId);
            if (user != null
                    && ctx.queryParam("confirmCode")
                    .equals(MailUtil.createEmailConfirmHash(user.getEmail()))) {
                user.setConfirm(YesNoStatus.YES);
                userService.update(user);
                ctx.result("Email confirmed successfully").status(200);
            } else {
                ctx.result("Check the link or write to the speedik support team.").status(400);
            }
        };

        Handler resetUserPasswordByMailHandler = ctx -> {
            var email = ctx.body();
            if (!email.isEmpty()) {
                var user = userService.getUserByEmail(email);
                if (user != null) {
                    var newPassword = PasswordCreator.createPassword();
                    user.setPassword(PasswordEncoder.hashPassword(newPassword));
                    userService.update(user);

                    var letter = new Letter();
                    letter.setBody("It is your new password of Speedik service: "
                            + newPassword + "\n\nDo not show it anyone!"
                            + "\nYou can change this later.");
                    letter.setSubject("Reset password for Speedik account");
                    letter.setUser(user);

                    MailUtil.sendMail(user.getEmail(), letter.getBody(), letter.getSubject());
                    letterService.save(letter);
                    ctx.result("success").status(200);
                } else {
                    ctx.result("User with such email is not found.").status(401);
                }
            } else {
                ctx.result("Incorrect email.").status(401);
            }
        };

        Handler resetUserPasswordByUserHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                int userId = Integer.parseInt(ctx.pathParam("userId"));
                var user = userService.getUserById(userId);
                if (user != null && isTokenBelongsToUser(decodedJWT, user.getUsername())) {
                    try {
                        var passwordChanger = EasyJsonParser.parseJsonToObject(ctx.body(), PasswordChanger.class);
                        passwordChanger.changePassword(user);

                        var letter = new Letter();
                        letter.setBody("Your password was changed for Speedik service!"
                                + "\nIf you did not do it, connect to our support team "
                                + "speedik.inc@gmail.com or reset your password!"
                                + "\nYou can reset it here: https://t6.tss2020.site/remind.html.");
                        letter.setSubject("Changed password for Speedik account");
                        letter.setUser(user);

                        MailUtil.sendMail(user.getEmail(), letter.getBody(), letter.getSubject());

                        letterService.save(letter);
                        ctx.result("Password changed successfully").status(200);
                    } catch (Exception e) {
                        ctx.result("Old password is not matched").status(400);
                    }
                } else {
                    ctx.result("Unauthorized user").status(401);
                }
            }
        };

        Handler addLinkForCheckHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                int userId = Integer.parseInt(ctx.pathParam("userId"));
                var user = userService.getUserById(userId);
                if (user != null && isTokenBelongsToUser(decodedJWT, user.getUsername())) {
                    if (tariffUtil.isPossibleToAddLinkForUserTariff(user)) {
                        var link = EasyJsonParser.parseJsonToObject(ctx.body(), Link.class);
                        if (isLink(link, user)) {
                            link.setUser(user);
                            linkService.addLink(link);
                            var newLink = linkService.findLastAddedLinkByUserId(userId);
                            tariffUtil.checkLinkBySchedule(newLink);
                            ctx.result("Link created successfully.").status(200);
                        } else {
                            ctx.result("Incorrect data.").status(400);
                        }
                    } else {
                        ctx.result("Achieves max number of links for user tariff " + user.getTariff()).status(400);
                    }
                } else {
                    ctx.result("Unauthorized userId").status(400);
                }
            }
        };

        Handler getLinksHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            var userId = Integer.parseInt(ctx.pathParam("userId"));
            if (decodedJWT != null && isTokenBelongsToUser(decodedJWT, userId)) {
                var links = linkService.findLinksByUserId(userId);
                var checkLinkService = new CheckLinkService();
                links.forEach(l -> {
                    l.setChecks(checkLinkService.getChecksByLinkId(l.getId()));
                });
                ctx.json(links);
            } else {
                ctx.result("Unauthorized user").status(403);
            }
        };

//        //TODO
//        Handler updateLinkForCheckHandler = ctx -> {
//
//        };
//
//        //TODO: Logic. What to do with thread?
//        Handler deleteLinkForCheckHandler = ctx -> {
//
//        };

        Handler chooseTariffHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var userId = Integer.parseInt(ctx.pathParam("userId"));
                var user = userService.getUserById(userId);
                var newTariff = ctx.pathParam("tariff");
                if (!tariffUtil.isChangeTariff(user, newTariff.toUpperCase())) {
                    ctx.result("Impossible to change tariff. " +
                            "Amount of servers or links is bigger than allowed for the new tariff").status(400);
                    return;
                }
                if (user != null && isTokenBelongsToUser(decodedJWT, user.getUsername())) {
                    switch (newTariff) {
                        case "PREMIUM" -> user.setTariff(TariffPlan.PREMIUM);
                        case "FREE" -> user.setTariff(TariffPlan.FREE);
                    }
                    userService.update(user);
                    ctx.result("Tariff changed successfully.").status(200);
                    return;
                }
            }
//            ctx.result("Unauthorized user").status(401);
        };

        Handler letterStatisticHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var username = decodedJWT.getClaim("username").asString();
                var user = userService.getUserByUsername(username);
                if (user != null) {
                    ctx.result("" + letterService.getLettersCount()).status(200);
                    return;
                }
            }
            ctx.result("Unauthorized user").status(401);
        };

        Handler linkStatisticHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var username = decodedJWT.getClaim("username").asString();
                var user = userService.getUserByUsername(username);
                if (user != null) {
                    ctx.result("" + new CheckLinkService().getLinkChecksCount()).status(200);
                    return;
                }
            }
            ctx.result("Unauthorized user").status(401);
        };

        Handler agentStatisticHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var username = decodedJWT.getClaim("username").asString();
                var user = userService.getUserByUsername(username);
                var agentActive = ctx.pathParam("active");
                if (user != null && !agentActive.equals("")) {
                    List<Agent> agents = null;

                    var date = LocalDate.now().minusDays(5); // 5 days
                    var datePoint = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    if (agentActive.toUpperCase().equals(YesNoStatus.YES.name().toUpperCase()))
                        agents = agentService.findAgentsByActive(datePoint, YesNoStatus.YES.name()); // Over the past 5 days.
                    else if (agentActive.toUpperCase().equals(YesNoStatus.NO.name().toUpperCase()))
                        agents = agentService.findAgentsByActive(datePoint, YesNoStatus.NO.name()); // Until the last 5 days.
                    ctx.json(agents).status(200);
                    return;
                }
            }
            ctx.result("Incorrect data").status(400);
        };

        Handler userStatisticHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var username = decodedJWT.getClaim("username").asString();
                var user = userService.getUserByUsername(username);
                var userActive = ctx.pathParam("active");
                if (user != null && !userActive.equals("")) {
                    List<User> users = null;
                    //TODO: remove magic numbers
                    var date = LocalDate.now().minusDays(5); // 5 days
                    var datePoint = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    if (userActive.toUpperCase().equals(YesNoStatus.YES.name().toUpperCase()))
                        users = userService.findUsersByActive(datePoint, YesNoStatus.YES.name()); // Over the past 5 days.
                    else if (userActive.toUpperCase().equals(YesNoStatus.NO.name().toUpperCase()))
                        users = userService.findUsersByActive(datePoint, YesNoStatus.NO.name()); // Until the last 5 days.
                    ctx.json(users).status(200);
                    return;
                }
            }
            ctx.result("Incorrect data").status(400);
        };

        Handler dashboardSaveHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var userId = Integer.parseInt(ctx.pathParam("userId"));
                var user = userService.getUserById(userId);
                if (user != null && isTokenBelongsToUser(decodedJWT, user.getUsername())) {
                    var dashboard = EasyJsonParser.parseJsonToObject(ctx.body(), Dashboard.class);
                    dashboard.setUser(user);
                    if (!UserDataValidator.isDashboardDescription(dashboard.getDescription())) {
                        ctx.result("Dashboard contains bad description.").status(400);
                        return;
                    }
                    ctx.result(dashboardService.saveDashboard(dashboard)).status(200);
                    return;
                }
            }
            ctx.result("Something goes wrong.").status(400);
        };

        Handler dashboardGetHandler = ctx -> {
            var userId = Integer.parseInt(ctx.pathParam("id"));
            var key = ctx.pathParam("key");
            try (Stream<String> stream = Files.lines(Paths.get("emailConfirmSignature.txt"))) {
                if (EncoderSHA256HMAC.encodeString(stream.limit(1).collect(Collectors.toList()).get(0), "" + userId)
                        .equals(key)) {
                    ctx.json(dashboardService.getByUserId(userId)).status(200);
                } else {
                    ctx.result("Something goes wrong.").status(400);
                }
            } catch (IOException e) {
                e.printStackTrace();
                ctx.result("Something goes wrong.").status(400);
            }
        };


        Handler letterStatisticPeriodHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var username = decodedJWT.getClaim("username").asString();
                var user = userService.getUserByUsername(username);
                var period = ctx.pathParam("period");
                if (user != null &&
                        (period.toUpperCase().equals(Period.DAY.name().toUpperCase())
                                || period.toUpperCase().equals(Period.WEEK.name().toUpperCase())
                                || period.toUpperCase().equals(Period.NOWDAY.name().toUpperCase())
                                || period.toUpperCase().equals(Period.NOWWEEK.name().toUpperCase()))) {
                    ctx.result("" + letterService.getLettersCountByPeriod(period.toUpperCase())).status(200);
                    return;
                }
            }
            ctx.result("Unauthorized user").status(401);
        };

        Handler linkStatisticPeriodHandler = ctx -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
            if (decodedJWT != null) {
                var username = decodedJWT.getClaim("username").asString();
                var user = userService.getUserByUsername(username);
                var period = ctx.pathParam("period");
                if (user != null &&
                        (period.toUpperCase().equals(Period.DAY.name().toUpperCase())
                                || period.toUpperCase().equals(Period.WEEK.name().toUpperCase())
                                || period.toUpperCase().equals(Period.NOWDAY.name().toUpperCase())
                                || period.toUpperCase().equals(Period.NOWWEEK.name().toUpperCase()))) {
                    ctx.result("" + new CheckLinkService().getLinkChecksCountByPeriod(period.toUpperCase())).status(200);
                    return;
                }
            }
            ctx.result("Unauthorized user").status(401);
        };
//
//        Handler agentStatisticPeriodHandler = ctx -> {
//            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(ctx);
//            if (decodedJWT != null) {
//                var username = decodedJWT.getClaim("username").asString();
//                var user = userService.getUserByUsername(username);
//                var agentActive = ctx.pathParam("active");
//                var period = ctx.pathParam("period");
//                if (user != null && !agentActive.equals("") && !period.equals("")) {
//                    List<Agent> agents = null;
//
//                    var date = LocalDate.now().minusDays(5); // 5 days
//                    var datePoint = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//                    if (agentActive.toUpperCase().equals(YesNoStatus.YES.name().toUpperCase()))
//                        agents = agentService.findAgentsByActive(datePoint, YesNoStatus.YES.name()); // Over the past 5 days.
//                    else if (agentActive.toUpperCase().equals(YesNoStatus.NO.name().toUpperCase()))
//                        agents = agentService.findAgentsByActive(datePoint, YesNoStatus.NO.name()); // Until the last 5 days.
//                    ctx.json(agents).status(200);
//                    return;
//                }
//            }
//            ctx.result("Incorrect data").status(400);
//        };


        // Login
        app.post("/api/login", loginHandler, Collections.singleton(Roles.ANYONE));

        // Receive data from agents
        app.post("/api/endpoint", saveAgentDataHandler, Collections.singleton(Roles.ANYONE));

        // Get data about the servers by user
        app.get("/api/agents/:userId",
                getAllAgentsByUserIdHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        // delete agent
        app.delete("/api/agents/:userId/:agentId",
                deleteAgentHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        //Create new server with secret key.
        app.put("/api/agent/:userId/:publicKey/:secretKey",
                addAgentWithSecretKeyHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        // User registration.
        app.post("/api/reg", registrationUserHandler, Collections.singleton(Roles.ANYONE));

        // Confirming email.
        app.get("/api/confirm/:userId", confirmUserEmailHandler,
                Collections.singleton(Roles.ANYONE));

        // Send new password by email.
        app.post("/api/reset/password/email", resetUserPasswordByMailHandler, Collections.singleton(Roles.ANYONE));
        // Reset User password by user.
        app.post("/api/reset/password/:userId", resetUserPasswordByUserHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        //TODO: check logic for incorrect data.
        //Add new link.
        app.put("/api/link/:userId", addLinkForCheckHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        //TODO: get links and checks for ordinary user.
        app.get("/api/link/:userId", getLinksHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        // update link.
        //TODO: write updating for link.
//        app.post("/api/link/:userId/:linkId", updateLinkForCheckHandler,
//                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));
//        //TODO: write deleting for link. How to stop thread.
//        app.delete("/api/link/:userId/:linkId", deleteLinkForCheckHandler,
//                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        app.post("/api/tariff/:userId/:tariff", chooseTariffHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));


//period WEEK/DAY/NOWWEEK/NOWDAY
        app.get("/api/statistic/letter/:period", letterStatisticPeriodHandler,
                Collections.singleton(Roles.ADMIN));

//period WEEK/DAY/NOWWEEK/NOWDAY
        app.get("/api/statistic/link/:period", linkStatisticPeriodHandler,
                Collections.singleton(Roles.ADMIN));

        // active = YES / NO
//period WEEK/DAY/NOWWEEK/NOWDAY
//        app.get("/api/statistic/agent/:active/:period", agentStatisticPeriodHandler,
//                Collections.singleton(Roles.ADMIN));

        // active = YES / NO
//period WEEK/DAY/NOWWEEK/NOWDAY
//        app.get("/api/statistic/user/:active/:period", userStatisticPeriodHandler,
//                Collections.singleton(Roles.ADMIN));


        app.get("/api/statistic/letter", letterStatisticHandler,
                Collections.singleton(Roles.ADMIN));

        app.get("/api/statistic/link", linkStatisticHandler,
                Collections.singleton(Roles.ADMIN));

        // active = YES / NO
        app.get("/api/statistic/agent/:active", agentStatisticHandler,
                Collections.singleton(Roles.ADMIN));

        // active = YES / NO
        app.get("/api/statistic/user/:active", userStatisticHandler,
                Collections.singleton(Roles.ADMIN));

        app.put("/api/dashboard/:userId", dashboardSaveHandler,
                new HashSet<>(Arrays.asList(Roles.USER_VIP, Roles.USER, Roles.ADMIN)));

        app.get("/api/dashboard/:id/:key", dashboardGetHandler,
                Collections.singleton(Roles.ANYONE));


        // extra required things
        // 1. Database cleaner.
        tariffUtil.cleanAgentDataByTariff();
        //2. Run already existing links.
        tariffUtil.checkLinksBySchedule();
    }
}
