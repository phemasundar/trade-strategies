package com.strategies.trade.copied;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

/**
 * @author hemasundarpenugonda
 */
public class NseDownloadDataUI {

    public void test() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/hemasundarpenugonda/Downloads/chromedriver89");
        System.setProperty("webdriver.edge.driver", "/Users/hemasundarpenugonda/Downloads/edgedriver_mac64/msedgedriver");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("chrome.page.customHeaders.referrer", "https://www1.nseindia.com/products/content/equities/equities/eq_security.htm");
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("user-data-dir=/Users/hemasundarpenugonda/Library/Application Support/Google/Chrome");
//        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("enable-automation");
//        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-gpu");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
//        options.addArguments("enable-features=NetworkServiceInProcess");
//        options.addArguments("disable-features=NetworkService");
        options.addArguments("--force-device-scale-factor=1");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--aggressive-cache-discard");
        options.addArguments("--disable-cache");
        options.addArguments("--disable-application-cache");
        options.addArguments("--disable-offline-load-stale-cache");
        options.addArguments("--disk-cache-size=0");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--no-proxy-server");
        options.addArguments("--log-level=3"); options.addArguments("--silent"); options.addArguments("--disable-browser-side-navigation"); options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setProxy(null);
        options.merge(capabilities);

        WebDriver driver = new ChromeDriver(options);
//        WebDriver driver = new EdgeDriver();

        driver.get("https://www1.nseindia.com/products/content/equities/equities/eq_security.htm");

        Thread.sleep(5000);
        driver.findElement(By.id("symbol")).sendKeys("HDFC");
        new Select(driver.findElement(By.id("series"))).selectByValue("EQ");
        Select dateRange = new Select(driver.findElement(By.id("dateRange")));
        dateRange.selectByValue("24month");

        Thread.sleep(2000);
        WebElement submitMe = driver.findElement(By.id("submitMe"));
        WebElement get = driver.findElement(By.id("get"));
//        submitMe.sendKeys(Keys.ENTER);
//        submitMe.sendKeys(Keys.RETURN);
        ((JavascriptExecutor) driver).executeScript("document.getElementById('submitMe').click()");
//        submitMe.click();
        get.click();
        Thread.sleep(5000);
        driver.findElement(By.cssSelector(".historic-bar a")).click();
        Thread.sleep(5000);

    }
}
