package com.in726.app.unit.tariff;

import com.in726.app.database.service.AgentDataService;
import com.in726.app.database.service.AgentService;
import com.in726.app.database.service.LinkService;
import com.in726.app.database.service.UserService;
import com.in726.app.enums.TariffPlan;
import com.in726.app.model.Agent;
import com.in726.app.model.AgentData;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.model.sub_functional_model.Word;
import com.in726.app.tariff.TariffUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TariffUtilTest {

    @Mock
    private UserService userService;

    @Mock
    private AgentService agentService;

    @Mock
    private AgentDataService agentDataService;

    @Mock
    private LinkService linkService;

    @InjectMocks
    private TariffUtil tariffUtil;

    @Test
    public void isPossibleToAddServerForUserTariffFreePositive() {
        var user = new User();
        user.setTariff(TariffPlan.FREE);
        user.setId(1);

        when(agentService.amountOfServersForUserByUserId(user.getId()))
                .thenReturn(1L);

        Assert.assertTrue(tariffUtil.isPossibleToAddServerForUserTariff(user));
    }

    @Test
    public void isPossibleToAddServerForUserTariffPremiumPositive() {
        var user = new User();
        user.setTariff(TariffPlan.PREMIUM);
        user.setId(1);

        when(agentService.amountOfServersForUserByUserId(user.getId()))
                .thenReturn(3L);

        Assert.assertTrue(tariffUtil.isPossibleToAddServerForUserTariff(user));
    }

    @Test
    public void isPossibleToAddServerForUserTariffAdminPositive() {
        var user = new User();
        user.setTariff(TariffPlan.ADMIN);
        user.setId(1);

        when(agentService.amountOfServersForUserByUserId(user.getId()))
                .thenReturn(15L);

        Assert.assertTrue(tariffUtil.isPossibleToAddServerForUserTariff(user));
    }

    @Test
    public void isPossibleToAddServerForUserTariffNegative() {
        var user = new User();
        user.setTariff(TariffPlan.FREE);
        user.setId(1);

        when(agentService.amountOfServersForUserByUserId(user.getId()))
                .thenReturn(15L);

        Assert.assertTrue(!tariffUtil.isPossibleToAddServerForUserTariff(user));
    }

    @Test
    public void cleanAgentDataByTariffFreePositive() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.FREE);
        var users = Arrays.asList(user);

        var agent = new Agent();
        var agents = Arrays.asList(agent);

        when(userService.getAll()).thenReturn(users);
        when(agentService.findAgentsByUserId(user.getId())).thenReturn(agents);
        when(agentDataService.findAgentDataByAgentIdAndDays(anyInt(), anyObject())).thenReturn(Arrays.asList(new AgentData()));

        tariffUtil.cleanAgentDataByTariff();
    }

    @Test
    public void cleanAgentDataByTariffPremiumPositive() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.PREMIUM);
        var users = Arrays.asList(user);

        var agent = new Agent();
        var agents = Arrays.asList(agent);

        when(userService.getAll()).thenReturn(users);
        when(agentService.findAgentsByUserId(user.getId())).thenReturn(agents);
        when(agentDataService.findAgentDataByAgentIdAndDays(anyInt(), anyObject())).thenReturn(Arrays.asList(new AgentData()));

        tariffUtil.cleanAgentDataByTariff();
    }

    @Test
    public void isPossibleToAddLinkForUserTariffFreePositive() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.FREE);

        when(linkService.amountOfLinksForUserByUserId(user.getId())).thenReturn(2L);

        tariffUtil.isPossibleToAddLinkForUserTariff(user);
    }

    @Test
    public void isPossibleToAddLinkForUserTariffPremiumPositive() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.PREMIUM);

        when(linkService.amountOfLinksForUserByUserId(user.getId())).thenReturn(9L);

        tariffUtil.isPossibleToAddLinkForUserTariff(user);
    }

    @Test
    public void isPossibleToAddLinkForUserTariffAdminPositive() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.ADMIN);

        when(linkService.amountOfLinksForUserByUserId(user.getId())).thenReturn(12L);

        tariffUtil.isPossibleToAddLinkForUserTariff(user);
    }

    @Test
    public void isPossibleToAddLinkNegative() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.FREE);

        when(linkService.amountOfLinksForUserByUserId(user.getId())).thenReturn(5L);

        tariffUtil.isPossibleToAddLinkForUserTariff(user);
    }

    @Test
    public void checkLinksBySchedulePositive() {
        var user = new User();
        user.setId(1);

        var link = new Link();
        link.setSecondsToCheck(60);
        link.setUrl("https://t6.tss2l020.site");

        var word = new Word();
        word.setValue("log");

        link.setWords(Arrays.asList(word));

        when(userService.getAll()).thenReturn(Arrays.asList(user));
        when(linkService.findLinksByUserId(user.getId())).thenReturn(Arrays.asList(link));

        tariffUtil.checkLinksBySchedule();
    }

    @Test
    public void buildPatternPositive() {
        var expectedPattern = Pattern.compile("world");
        Assert.assertEquals(expectedPattern.toString(), TariffUtil.buildPattern("world").toString());
    }

    @Test
    public void areWordsInResponsePositive() {
        var text = "hello it is World. Do you Like me?";
        var word = new Word();
        word.setValue("World");

        var expectedPattern = Pattern.compile("world");
        Assert.assertTrue(TariffUtil.areWordsInResponse(text, Arrays.asList(word)));
    }

    @Test
    public void checkLinkNegative() {
        Assert.assertTrue(!TariffUtil.checkLinkForTariff(null, null));
    }

    @Test
    public void isChangeTariffNullUserNegative() {
       Assert.assertTrue(!tariffUtil.isChangeTariff(null, "FREE"));
    }

    @Test
    public void isChangeTariffIncorrectNewTariffNegative() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.FREE);
        when(agentService.amountOfServersForUserByUserId(user.getId())).thenReturn(1L);
        when(linkService.amountOfLinksForUserByUserId(user.getId())).thenReturn(1L);
        Assert.assertTrue(!tariffUtil.isChangeTariff(user, "WORLD"));
    }

    @Test
    public void isChangeTariffFreePositive() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.FREE);
        when(agentService.amountOfServersForUserByUserId(user.getId())).thenReturn(1L);
        when(linkService.amountOfLinksForUserByUserId(user.getId())).thenReturn(1L);
        Assert.assertTrue(tariffUtil.isChangeTariff(user, "PREMIUM"));
    }

    @Test
    public void isChangeTariffPremiumPositive() {
        var user = new User();
        user.setId(1);
        user.setTariff(TariffPlan.PREMIUM);
        when(agentService.amountOfServersForUserByUserId(user.getId())).thenReturn(1L);
        when(linkService.amountOfLinksForUserByUserId(user.getId())).thenReturn(1L);
        Assert.assertTrue(tariffUtil.isChangeTariff(user, "FREE"));
    }
}
