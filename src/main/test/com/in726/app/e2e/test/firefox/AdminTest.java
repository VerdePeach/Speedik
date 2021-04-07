package com.in726.app.e2e.test.firefox;

import com.in726.app.e2e.page.AdminPage;
import com.in726.app.e2e.page.LoginPage;
import com.in726.app.e2e.page.MainPage;
import com.in726.app.e2e.util.Util;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

public class AdminTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static MainPage mainPage;
    private static AdminPage adminPage;


    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.gecko.driver", Util.getProperty("firefoxdriver"));
        var options = new FirefoxOptions();
        options.addArguments("incognito");
        driver = new FirefoxDriver(options);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        adminPage = new AdminPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void checkStatistic() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);
        mainPage.clickAdminPageBtn();
        Thread.sleep(30000);
        try {
            Util.takeScreenShot(driver);
            Integer.parseInt(adminPage.getActiveAgentText());
            Integer.parseInt(adminPage.getInactiveAgentText());
            Integer.parseInt(adminPage.getActiveUserText());
            Integer.parseInt(adminPage.getInactiveUserText());
            Integer.parseInt(adminPage.getLettersCountText());
            Integer.parseInt(adminPage.getLettersCountText());
            Assert.assertTrue(true);
        } catch (NumberFormatException ex) {
            Assert.assertTrue(false);
        }
        adminPage.clickMainPageBtn();
        mainPage.clickLogoutBtn();
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
