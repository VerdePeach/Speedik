package com.in726.app.e2e.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Util {

    protected static FileInputStream fileInputStream;
    protected static Properties PROPERTIES;

    static {
        try {
            //указание пути до файла с настройками
            fileInputStream = new FileInputStream("src/main/test_resources/e2e/e2e.properties");
            PROPERTIES = new Properties();
            PROPERTIES.load(new InputStreamReader(fileInputStream, Charset.forName("windows-1251")));
        } catch (IOException e) {
            e.printStackTrace();
            //обработка возможного исключения (нет файла и т.п.)
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    /**
     * метод для возврата строки со значением из файла с настройками
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String takeScreenShot(WebDriver driver) {
        String path;
        try {
            var augmentedDriver = new Augmenter().augment(driver);
            File source = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);

            var dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            var date = dateFormat.format(new Date());

            path = "src/main/test_resources/e2e/e2eScreenShots/date" + date + "/" + source.getName();
            FileUtils.copyFile(source, new File(path));
        }
        catch(IOException e) {
            path = "Failed to capture screenshot: " + e.getMessage();
        }
        return path;
    }
}

