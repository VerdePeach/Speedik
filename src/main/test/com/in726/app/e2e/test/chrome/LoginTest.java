package com.in726.app.e2e.test.chrome;

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

import java.util.concurrent.TimeUnit;

public class LoginTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static MainPage mainPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", Util.getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("loginpage"));
    }

    @Test
    public void loginTest() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        Util.takeScreenShot(driver);
        loginPage.clickLoginBtn();
        Thread.sleep(2000);
        String actualUsernameText = mainPage.getUsernameText();
        String actualUserTariffText = mainPage.getUserTariffText();
        Util.takeScreenShot(driver);
        Assert.assertEquals("Current user: speedik", actualUsernameText);
        Assert.assertEquals("Current tariff: ADMIN", actualUserTariffText);
        mainPage.clickLogoutBtn();
    }

    @Test
    public void logoutTest() throws InterruptedException {
        Thread.sleep(2000);
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);
        Assert.assertEquals("Current user: speedik", mainPage.getUsernameText());
        Assert.assertEquals("Current tariff: ADMIN", mainPage.getUserTariffText());
        mainPage.clickLogoutBtn();
        Thread.sleep(2000);
        Assert.assertEquals("Log in", loginPage.getLoginHeader());
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}

