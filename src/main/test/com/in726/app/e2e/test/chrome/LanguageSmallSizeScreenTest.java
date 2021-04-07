package com.in726.app.e2e.test.chrome;

import com.in726.app.e2e.page.*;
import com.in726.app.e2e.util.Util;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class LanguageSmallSizeScreenTest {
    private static WebDriver driver;
    private static LoginPage loginPage;
    private static MainPage mainPage;
    private static AdminPage adminPage;
    private static UrlMonitorPage urlMonitorPage;
    private static NewServerPage newServerPage;
    private static ChangePasswordPage changePasswordPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", Util.getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        adminPage = new AdminPage(driver);
        urlMonitorPage = new UrlMonitorPage(driver);
        newServerPage = new NewServerPage(driver);
        changePasswordPage = new ChangePasswordPage(driver);

        Dimension dimension = new Dimension( 480,  767);
        driver.manage().window().setSize(dimension);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void ukraineLanguageMaxSizeTest() throws InterruptedException {
        Thread.sleep(2000);
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        Thread.sleep(2000);
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        mainPage.clickUkraineLanguageBtn();
        Thread.sleep(1000);
        Assert.assertEquals(Util.getProperty("newServerBtnUA").toUpperCase(), mainPage.getTextOfNewServerBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassBtnUA").toUpperCase(), mainPage.getTextOfChangePasswordBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("changeTariffBtnUA").toUpperCase(), mainPage.getTextOfTariffPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlBtnUA").toUpperCase(), mainPage.getTextOfNewUrlBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("adminPanelBtnUA").toUpperCase(), mainPage.getTextOfAdminPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("logoutBtnUA").toUpperCase(), mainPage.getTextOfLogoutBtn().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        mainPage.clickNewServerBtn();

        Assert.assertEquals(Util.getProperty("mainPageBackBtnUA").toUpperCase(), newServerPage.getTextMainPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPageTitleUA").toUpperCase(), newServerPage.getTextPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPagePublicKeyInputUA").toUpperCase(), newServerPage.getTextPublicKeyInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPageSecretKeyInputUA").toUpperCase(), newServerPage.getTextSecretKeyInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPageCreateBtnUA").toUpperCase(), newServerPage.getTextServerCreateBtn().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        newServerPage.clickMainPageBtn();
        mainPage.clickChangePasswordBtn();

        Assert.assertEquals(Util.getProperty("mainPageBackBtnUA").toUpperCase(), changePasswordPage.getTextMainPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageTitleUA").toUpperCase(), changePasswordPage.getTextPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageOldPassUA").toUpperCase(), changePasswordPage.getTextOldPasswordInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageNewPassUA").toUpperCase(), changePasswordPage.getTextNewPasswordInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageConfPassUA").toUpperCase(), changePasswordPage.getTextConfPassInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassBtnUA").toUpperCase(), changePasswordPage.getTextChangePasswordBtn().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        changePasswordPage.clickMainPageBtn();
        mainPage.clickNewUrlBtn();

        Assert.assertEquals(Util.getProperty("mainPageBackBtnUA").toUpperCase(), urlMonitorPage.getTextMainPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("addUrlPageTitleUA").toUpperCase(), urlMonitorPage.getTextPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlTitleUA").toUpperCase(), urlMonitorPage.getTextUrlNameInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlValueUA").toUpperCase(), urlMonitorPage.getTextUrlInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("addUrlBtnUA").toUpperCase(), urlMonitorPage.getTextUrlAddBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlCheckWordUA").toUpperCase(), urlMonitorPage.getTextCheckWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlKeyWordsWordUA").toUpperCase(), urlMonitorPage.getTextKeyWordsWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlKeySeparateWordUA").toUpperCase(), urlMonitorPage.getTextSeparateWord().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        urlMonitorPage.clickMainPageBtn();
        mainPage.clickAdminPageBtn();

        Assert.assertEquals(Util.getProperty("adminPageTitleUA").toUpperCase(), adminPage.getTextAdminPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("activeUsersUA").toUpperCase(), adminPage.getTextActiveUsersWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("inactiveUsersUA").toUpperCase(), adminPage.getTextInactiveUsersWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlChecksAmountUA").toUpperCase(), adminPage.getTextUrlCheckWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("activeAgentsUA").toUpperCase(), adminPage.getTextActiveAgentsWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("inactiveAgentsUA").toUpperCase(), adminPage.getTextInactiveAgentsWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("sentLettersUA").toUpperCase(), adminPage.getTextLettersWord().toUpperCase());
        Util.takeScreenShot(driver);
        Thread.sleep(1000);

        adminPage.clickBurgerBtn();
        Thread.sleep(2000);
        adminPage.clickMainPageBtn();
        Thread.sleep(2000);
        mainPage.clickLogoutBtn();
    }

    @Test
    public void englishLanguageMaxSizeTest() throws InterruptedException {
        Thread.sleep(2000);
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        Thread.sleep(2000);
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        mainPage.clickEnglishLanguageBtn();
        Thread.sleep(1000);
        Assert.assertEquals(Util.getProperty("newServerBtnEN").toUpperCase(), mainPage.getTextOfNewServerBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassBtnEN").toUpperCase(), mainPage.getTextOfChangePasswordBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("changeTariffBtnEN").toUpperCase(), mainPage.getTextOfTariffPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlBtnEN").toUpperCase(), mainPage.getTextOfNewUrlBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("adminPanelBtnEN").toUpperCase(), mainPage.getTextOfAdminPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("logoutBtnEN").toUpperCase(), mainPage.getTextOfLogoutBtn().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        mainPage.clickNewServerBtn();

        Assert.assertEquals(Util.getProperty("mainPageBackBtnEN").toUpperCase(), newServerPage.getTextMainPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPageTitleEN").toUpperCase(), newServerPage.getTextPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPagePublicKeyInputEN").toUpperCase(), newServerPage.getTextPublicKeyInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPageSecretKeyInputEN").toUpperCase(), newServerPage.getTextSecretKeyInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("newServerPageCreateBtnEN").toUpperCase(), newServerPage.getTextServerCreateBtn().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        newServerPage.clickMainPageBtn();
        mainPage.clickChangePasswordBtn();

        Assert.assertEquals(Util.getProperty("mainPageBackBtnEN").toUpperCase(), changePasswordPage.getTextMainPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageTitleEN").toUpperCase(), changePasswordPage.getTextPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageOldPassEN").toUpperCase(), changePasswordPage.getTextOldPasswordInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageNewPassEN").toUpperCase(), changePasswordPage.getTextNewPasswordInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassPageConfPassEN").toUpperCase(), changePasswordPage.getTextConfPassInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("changePassBtnEN").toUpperCase(), changePasswordPage.getTextChangePasswordBtn().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        changePasswordPage.clickMainPageBtn();
        mainPage.clickNewUrlBtn();

        Assert.assertEquals(Util.getProperty("mainPageBackBtnEN").toUpperCase(), urlMonitorPage.getTextMainPageBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("addUrlPageTitleEN").toUpperCase(), urlMonitorPage.getTextPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlTitleEN").toUpperCase(), urlMonitorPage.getTextUrlNameInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlValueEN").toUpperCase(), urlMonitorPage.getTextUrlInput().toUpperCase());
        Assert.assertEquals(Util.getProperty("addUrlBtnEN").toUpperCase(), urlMonitorPage.getTextUrlAddBtn().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlCheckWordEN").toUpperCase(), urlMonitorPage.getTextCheckWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlKeyWordsWordEN").toUpperCase(), urlMonitorPage.getTextKeyWordsWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("urlKeySeparateWordEN").toUpperCase(), urlMonitorPage.getTextSeparateWord().toUpperCase());

        Util.takeScreenShot(driver);
        Thread.sleep(1000);
        urlMonitorPage.clickMainPageBtn();
        mainPage.clickAdminPageBtn();

        Assert.assertEquals(Util.getProperty("adminPageTitleEN").toUpperCase(), adminPage.getTextAdminPageTitle().toUpperCase());
        Assert.assertEquals(Util.getProperty("activeUsersEN").toUpperCase(), adminPage.getTextActiveUsersWord().toUpperCase());

        Assert.assertTrue(Util.getProperty("inactiveUsersEN").toUpperCase()
                .equals(adminPage.getTextInactiveUsersWord().toUpperCase())
                || "Unactive Users".toUpperCase()
                .equals(adminPage.getTextInactiveUsersWord().toUpperCase()));

        Assert.assertEquals(Util.getProperty("urlChecksAmountEN").toUpperCase(), adminPage.getTextUrlCheckWord().toUpperCase());
        Assert.assertEquals(Util.getProperty("activeAgentsEN").toUpperCase(), adminPage.getTextActiveAgentsWord().toUpperCase());
        Assert.assertTrue(Util.getProperty("inactiveAgentsEN").toUpperCase()
                .equals(adminPage.getTextInactiveAgentsWord().toUpperCase())
                || "Unactive Agents".toUpperCase()
                .equals(adminPage.getTextInactiveAgentsWord().toUpperCase()));
        Assert.assertEquals(Util.getProperty("sentLettersEN").toUpperCase(), adminPage.getTextLettersWord().toUpperCase());
        Util.takeScreenShot(driver);
        Thread.sleep(1000);

        adminPage.clickBurgerBtn();
        Thread.sleep(2000);
        adminPage.clickMainPageBtn();
        Thread.sleep(2000);
        mainPage.clickLogoutBtn();
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
