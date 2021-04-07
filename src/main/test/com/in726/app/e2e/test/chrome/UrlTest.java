package com.in726.app.e2e.test.chrome;

import com.in726.app.e2e.page.LoginPage;
import com.in726.app.e2e.page.MainPage;
import com.in726.app.e2e.page.UrlMonitorPage;
import com.in726.app.e2e.util.Util;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UrlTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static MainPage mainPage;
    private static UrlMonitorPage urlMonitorPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", Util.getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        urlMonitorPage = new UrlMonitorPage(driver);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void createUrl() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        mainPage.clickNewUrlBtn();
        var urlName = "seleniumTest" + new Date().getTime();
        var url = "https://t6.tss2020.site";
        urlMonitorPage.setUrlNameInput(urlName);
        urlMonitorPage.setUrlInput(url);
        urlMonitorPage.setSelectSecondsToCheckByIndex("1800");
        urlMonitorPage.setWordsTextArea("Log in");
        Util.takeScreenShot(driver);
        Thread.sleep(2000);
        urlMonitorPage.clickUrlAddBtn();
        Thread.sleep(2000);
        var actualSingUpStatus = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Assert.assertEquals("Link created successfully.", actualSingUpStatus);
        driver.navigate().refresh();
        Thread.sleep(2000);
        urlMonitorPage.linkSelectByVisual(urlName);
        Assert.assertTrue(urlMonitorPage.isDisplayedUrlsTable());

        urlMonitorPage.clickMainPageBtn();
        mainPage.clickLogoutBtn();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
