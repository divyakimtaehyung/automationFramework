package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CommonDriver implements Driver {
    private WebDriver driver;
    private long pageLoadTimeout;
    private long implicitWaitTimeout;
    public static Properties props ;

    public CommonDriver(WebDriver driver){
        this.driver = driver;
    }



    public WebDriver getDriver() {
        return driver;
    }

    public void setPageLoadTimeout(long pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public void setImplicitWaitTimeout(long implicitWaitTimeout) {
        this.implicitWaitTimeout = implicitWaitTimeout;

    }

    @Override
    public void openBrowserAndNavigateToUrl(String url) throws Exception {
        driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(implicitWaitTimeout,TimeUnit.SECONDS);
        url = url.trim();
        driver.get(url);
    }

    @Override
    public String getTitle() throws Exception {

        return driver.getTitle();
    }

    @Override
    public String getCurrentUrl() throws Exception{
        return driver.getCurrentUrl();
    }

    @Override
    public String getPageSource() throws Exception{
        return driver.getPageSource();
    }

    @Override
    public void navigateToUrl(String url) throws Exception {
        url = url.trim();
        driver.navigate().to(url);
    }

    @Override
    public void navigateForward() throws Exception {
        driver.navigate().forward();
    }

    @Override
    public void navigateBackward() throws Exception {
        driver.navigate().back();
    }

    @Override
    public void navigateRefresh() throws Exception {
        driver.navigate().refresh();
    }

    @Override
    public void closeBrowser() throws Exception {
        if (driver != null){
            driver.close();
        }

    }

    @Override
    public void closeAllBrowser() throws Exception {
        if (driver != null){
            driver.quit();
        }

    }
}
