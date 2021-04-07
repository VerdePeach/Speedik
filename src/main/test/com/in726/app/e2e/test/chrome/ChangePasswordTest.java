package com.in726.app.e2e.test.chrome;

import com.in726.app.e2e.page.ChangePasswordPage;
import com.in726.app.e2e.page.LoginPage;
import com.in726.app.e2e.page.MainPage;
import com.in726.app.e2e.page.UrlMonitorPage;
import com.in726.app.e2e.util.Util;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.concurrent.TimeUnit;

public class ChangePasswordTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static MainPage mainPage;
    private static ChangePasswordPage changePasswordPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", Util.getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        changePasswordPage = new ChangePasswordPage(driver);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void changePasswordTest() throws InterruptedException {
        var userName = Util.getProperty("loginChangePassUser");
        var oldPass = Util.getProperty("passwordChangePassOld");
        var newPass = Util.getProperty("passwordChangePassNew");
        var p = "";
        loginPage.setUsernameInput(userName);
        loginPage.setPasswordInput(oldPass);
        loginPage.clickLoginBtn();
        Thread.sleep(2000);
        try {
            driver.switchTo().alert().accept();
            Thread.sleep(1000);
            loginPage.setUsernameInput(userName);
            Thread.sleep(1000);
            loginPage.setPasswordInput(newPass);
            loginPage.clickLoginBtn();
            p = oldPass;
            oldPass = newPass;
            newPass = p;
        } catch (Exception e) {

        }
        mainPage.clickChangePasswordBtn();

        Thread.sleep(2000);
        changePasswordPage.setOldPasswordInput(oldPass);
        changePasswordPage.setNewPasswordInput(newPass);
        changePasswordPage.setConfpassInput(newPass);
        Util.takeScreenShot(driver);
        Thread.sleep(2000);
        changePasswordPage.changePasswordBtn();

        Thread.sleep(2000);
        Assert.assertEquals("Password changed", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        Thread.sleep(2000);

        changePasswordPage.clickMainPageBtn();
        mainPage.clickLogoutBtn();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
