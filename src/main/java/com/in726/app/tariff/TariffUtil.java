package com.in726.app.tariff;

import com.in726.app.database.service.*;
import com.in726.app.enums.LinkStatus;
import com.in726.app.enums.TariffPlan;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.CheckLink;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.model.sub_functional_model.Word;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class contains all logic for user tariff plans.
 */
public class TariffUtil {


    private UserService userService = new UserService();
    private AgentService agentService = new AgentService();
    private AgentDataService agentDataService = new AgentDataService();
    private LinkService linkService = new LinkService();

    private static final long SERVER_AMOUNT_FOR_FREE_TARIFF = 2;
    private static final long SERVER_AMOUNT_FOR_PREMIUM_TARIFF = 10;

    private static final int DAYS_CLEAN_FREE_TARIFF = 3;
    private static final int DAYS_CLEAN_PREMIUM_TARIFF = 10;

    private static final long LINK_AMOUNT_FOR_FREE_TARIFF = 3;
    private static final long LINK_AMOUNT_FOR_PREMIUM_TARIFF = 10;

    private static final long LINK_AMOUNT_WORDS_FOR_FREE_TARIFF = 5;
    private static final long LINK_AMOUNT_WORDS_FOR_PREMIUM_TARIFF = 10;

    private static final long LINK_MIN_TIME_TO_CHECK_FOR_FREE_TARIFF = 300;
    private static final long LINK_MIN_TIME_TO_CHECK_FOR_PREMIUM_TARIFF = 60;

    private static final int LINK_STATUS_FROM = 200;
    private static final int LINK_STATUS_TO = 299;

    //private static ScheduledExecutorService scheduler;


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /**
     * Method checks is it possible to add new server
     * for current user tariff
     *
     * @param user user
     * @return true or false
     */
    public boolean isPossibleToAddServerForUserTariff(User user) {
        var countOfServers = agentService.amountOfServersForUserByUserId(user.getId());
        if (countOfServers < SERVER_AMOUNT_FOR_FREE_TARIFF
                && user.getTariff().name().toUpperCase().equals("FREE")) {
            return true;
        } else if (countOfServers < SERVER_AMOUNT_FOR_PREMIUM_TARIFF
                && user.getTariff().name().toUpperCase().equals("PREMIUM")) {
            return true;
        } else if (user.getTariff().name().toUpperCase().equals("ADMIN")) {
            return true;
        } else
            return false;
    }

    /**
     * Method cleans old agent data every 30 minutes
     * by user tariff plan.
     */
    public void cleanAgentDataByTariff() {
        Runnable cleanThread = () -> {
            var users = userService.getAll();
            users.forEach(u -> { //TODO: lazy load exception
                var agents = agentService.findAgentsByUserId(u.getId());
                if (agents != null) {
                    agents.forEach(a -> { //TODO: lazy load exception
                        if (u.getTariff().name().toUpperCase().equals("FREE")) {
                            deleteOldAgentData(a.getId(), DAYS_CLEAN_FREE_TARIFF);
                        } else if (u.getTariff().name().toUpperCase().equals("PREMIUM")) {
                            deleteOldAgentData(a.getId(), DAYS_CLEAN_PREMIUM_TARIFF);
                        }
                    });
                }
            });
        };
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(cleanThread, 0, 30, TimeUnit.MINUTES);
    }

    private void deleteOldAgentData(int agentId, int days) {
        var date = LocalDate.now().minusDays(days);
        var dateTillDelete = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var agentDataList = agentDataService.findAgentDataByAgentIdAndDays(agentId, dateTillDelete);
        agentDataList.forEach(ad -> {
            agentDataService.deleteAgentData(ad);
        });
    }

    /**
     * Method checks possibility to add new link by user tariff.
     *
     * @param user user
     * @return true or false
     */
    public boolean isPossibleToAddLinkForUserTariff(User user) {
        var countOfLinks = linkService.amountOfLinksForUserByUserId(user.getId());
        if (countOfLinks < LINK_AMOUNT_FOR_FREE_TARIFF && user.getTariff().name().toUpperCase().equals("FREE")) {
            return true;
        } else if (countOfLinks < LINK_AMOUNT_FOR_PREMIUM_TARIFF
                && user.getTariff().name().toUpperCase().equals("PREMIUM")) {
            return true;
        } else if (user.getTariff().name().toUpperCase().equals("ADMIN")) {
            return true;
        } else
            return false;
    }

    /**
     * Method checks links by links schedules.
     */
    public void checkLinksBySchedule() {
        var users = userService.getAll();
        if (users != null) {
            users.forEach(u -> {
                var links = linkService.findLinksByUserId(u.getId());
                if (links != null) {
                    links.forEach(l -> {
                        checkLinkBySchedule(l);
                    });
                }
            });
        }
    }

    /**
     * Method checks link by link schedule.
     *
     * @param link link
     */

    public static void checkLinkBySchedule(Link link) {
        Runnable linkCheckerThread = () -> {
            HttpRequest request;
            if (link != null) {
                request = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(link.getUrl()))
                        .build();

                try {
                    CompletableFuture<HttpResponse<String>> response =
                            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

                    var httpStatus = response.get().statusCode();
                    var bodyString = response.get().body();

                    if (httpStatus >= LINK_STATUS_FROM && httpStatus <= LINK_STATUS_TO) {
                        var words = link.getWords();
                        var isAllMatch = areWordsInResponse(bodyString, words);

                        var checkLinkService = new CheckLinkService();
                        var checkLink = new CheckLink(httpStatus, isAllMatch, LinkStatus.UP, link);
                        checkLinkService.save(checkLink);
                    } else {
                        var checkLinkService = new CheckLinkService();
                        var checkLink = new CheckLink(httpStatus, false, LinkStatus.DOWN, link);
                        checkLinkService.save(checkLink);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    //todo: think about approach.
                    var checkLinkService = new CheckLinkService();
                    var checkLink = new CheckLink(404, false, LinkStatus.DOWN, link);
                    checkLinkService.save(checkLink);
                    e.printStackTrace();
                }
            }
        };
        //TODO: what to do if link is deleted? null pointer.
        var schedulerLink = Executors.newSingleThreadScheduledExecutor();
        schedulerLink.scheduleWithFixedDelay(linkCheckerThread, 0, link.getSecondsToCheck(), TimeUnit.SECONDS);
    }

    /**
     * Method creates regexp pattern for string - word.
     *
     * @param word word
     * @return pattern
     */
    public static Pattern buildPattern(String word) { //TODO: was private before testing
        Pattern pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
        return pattern;
    }

    /**
     * Method looks for link words in text.
     *
     * @param inputString text
     * @param words       words
     * @return true or false
     */
    public static boolean areWordsInResponse(String inputString, List<Word> words) { //TODO: was private before testing
        var isAllMatch = true;
        if (words != null) {
            for (int i = 0; i < words.size(); i++) {
                var word = words.get(i);
                if (word != null) {
                    var pattern = buildPattern(word.getValue());
                    Matcher matcher = pattern.matcher(inputString);
                    isAllMatch &= matcher.find();
                    if (!isAllMatch) break;
                }
            }
        }
        return isAllMatch;
    }

    /**
     * Method checks amount of words for link by user tariff.
     *
     * @param link link
     * @param user user
     * @return true or false
     */
    public static boolean checkLinkForTariff(Link link, User user) {
        if (user != null && link != null) {
            if (user.getTariff().name().toUpperCase().equals(TariffPlan.FREE.name().toUpperCase()) && link.getWords().size() <= LINK_AMOUNT_WORDS_FOR_FREE_TARIFF && link.getSecondsToCheck() >= LINK_MIN_TIME_TO_CHECK_FOR_FREE_TARIFF) {
                return true;
            } else if (user.getTariff().name().toUpperCase().equals(TariffPlan.PREMIUM.name().toUpperCase())
                    && link.getWords().size() <= LINK_AMOUNT_WORDS_FOR_PREMIUM_TARIFF
                    && link.getSecondsToCheck() >= LINK_MIN_TIME_TO_CHECK_FOR_PREMIUM_TARIFF) {
                return true;
            } else if (user.getTariff().name().toUpperCase().equals(TariffPlan.ADMIN.name().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method checks possibility to change tariff.
     *
     * @param user   user
     * @param tariff tariff
     * @return true or false
     */
    public boolean isChangeTariff(User user, String tariff) {
        if (user != null && !user.getTariff().name().toUpperCase().equals(tariff)) {
            var agentsAmount = agentService.amountOfServersForUserByUserId(user.getId());
            var linksAmount = linkService.amountOfLinksForUserByUserId(user.getId());
            if (tariff.equals(TariffPlan.FREE.name().toUpperCase())
                    && agentsAmount <= SERVER_AMOUNT_FOR_FREE_TARIFF
                    && linksAmount <= LINK_AMOUNT_FOR_FREE_TARIFF) {
                return true;
            } else if (tariff.equals(TariffPlan.PREMIUM.name().toUpperCase())
                    && agentsAmount <= SERVER_AMOUNT_FOR_PREMIUM_TARIFF
                    && linksAmount <= LINK_AMOUNT_FOR_PREMIUM_TARIFF) {
                return true;
            } else
                return false;
        } else
            return false;
    }
}
