package com.in726.app.e2e.test.firefox;

import com.in726.app.e2e.page.LoginPage;
import com.in726.app.e2e.page.MainPage;
import com.in726.app.e2e.page.NewServerPage;
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

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainPageTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static MainPage mainPage;
    private static NewServerPage newServerPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.gecko.driver", Util.getProperty("firefoxdriver"));
        var options = new FirefoxOptions();
        options.addArguments("incognito");
        driver = new FirefoxDriver(options);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        newServerPage = new NewServerPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void selectServerTest() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        mainPage.setServerNameSelect(Util.getProperty("serverNameForGraphic"));
        Thread.sleep(5000);
//        Util.takeScreenShot(driver);
        Assert.assertTrue(mainPage.isDisplayedCpuChart());
        Assert.assertTrue(mainPage.isDisplayedDiskChart());
        Assert.assertTrue(mainPage.isDisplayedMemoryChart());

        mainPage.clickLogoutBtn();
    }

    @Test
    public void newServerCreation() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        mainPage.clickNewServerBtn();
        var newPublicKey = "seleniumPublicKey-" + new Random().nextInt();
        var newSecretKey = "seleniumSecretKey-" + new Random().nextInt();
        newServerPage.setPublicKeyInput(newPublicKey);
        newServerPage.setSecretKeyInput(newSecretKey);
        Util.takeScreenShot(driver);
        newServerPage.clickServerCreateBtn();
        Thread.sleep(2000);
        var actualServerCreatedMessage = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Assert.assertEquals(Util.getProperty("messageNewServerCreated"), actualServerCreatedMessage);

        newServerPage.clickMainPageBtn();
        mainPage.clickLogoutBtn();
    }

    @Test
    public void footerPart() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        Assert.assertTrue(mainPage.isDisplayedForDevelopersLink());
        Assert.assertTrue(mainPage.isDisplayedPrivatPolicyLink());
        Assert.assertTrue(mainPage.isDisplayedSpeedikEmail());

        mainPage.clickLogoutBtn();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
