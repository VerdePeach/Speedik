package com.in726.app.e2e.test.chrome;

import com.in726.app.e2e.page.LoginPage;
import com.in726.app.e2e.page.MainPage;
import com.in726.app.e2e.page.SignUpPage;
import com.in726.app.e2e.util.Util;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SingUpTest {

    private static WebDriver driver;
    private static SignUpPage signUpPage;
    private static LoginPage loginPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", Util.getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        signUpPage = new SignUpPage(driver);
        loginPage = new LoginPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void singUpUser() throws InterruptedException {
        loginPage.clickSingUpBtn();
        var userPassword = Util.getProperty("singUpUserPassword");
        var username = "selUN" + new Date().getTime();
        var userEmail = "selenium" + new Date().getTime() + "UserEmail@sen.com";
        signUpPage.setUsernameInput(username);
        signUpPage.setEmailInput(userEmail);
        signUpPage.setPasswordInput(userPassword);
        signUpPage.setConfPassInput(userPassword);
        Util.takeScreenShot(driver);
        signUpPage.clickSignUpBtn();
        Thread.sleep(2000);
        var actualSingUpStatus = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Assert.assertEquals("User registered successfully.\nTo your email was sent confirmation letter.\nCheck it to finish registration.", actualSingUpStatus);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
