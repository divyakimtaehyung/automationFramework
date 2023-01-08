package com.iexceed.uiframework.core;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class TestBase {
    protected static Properties props = new Properties();
    public static WebDriver driver1;
    public static ThreadLocal<WebDriver> webdriver = new ThreadLocal<WebDriver>();
    public static DesiredCapabilities capabilities;
    private static Logger LOGGER = LogManager.getLogger(TestBase.class);


    /**To load the project configuration files **/
    public TestBase() {

        FileInputStream ip;
        try {
            ip = new FileInputStream(new File("src/main/resources/project.properties"));
            props.load(ip);
            LOGGER.info("properties files loaded in TestBase Constructor");

        } catch (Exception e) {
            LOGGER.error("Error in creating properties loader in TestBase Constructor");
        }
    }

    /**
     *      * Run Test in Selenium GRID when runMode is "hub" and run test in local browser when runMOde is standalone
     *      * Run tests in all different browser such as Chrome, IE, Firefox
     */

    public static void initialization(String browserType) throws Exception {
        WebDriver driver = null;
        LOGGER.info("Instantiate browser details");
        LOGGER.info("browserName = "+browserType);
        //String browserType = props.getProperty("browser");
        LOGGER.info("Browser name is set");
        String runMode = props.getProperty("runmode");
        String browser = browserType.trim();
        LOGGER.info("Browser name trim if space");
        if ("hub".equalsIgnoreCase(runMode)) {
            capabilities = new DesiredCapabilities();
            if ("chrome".equalsIgnoreCase(browser)) {
                capabilities.setBrowserName(BrowserType.CHROME);
            } else if ("firefox".equalsIgnoreCase(browser)) {
                capabilities.setBrowserName(BrowserType.FIREFOX);
            } else if ("ie".equalsIgnoreCase(browser)) {
                capabilities.setBrowserName(BrowserType.EDGE);
            }
            String host = props.getProperty("seleniumHubHost");
            driver = new RemoteWebDriver(new URL(host + "wd/hub"),capabilities);
            LOGGER.info("Browser Invoked "+browser);
            webdriver.set(driver);
        } else if ("chrome".equalsIgnoreCase(browser)) {
            LOGGER.info("setting chromedriver path details");
            System.setProperty("webdriver.chrome.driver", props.getProperty("com.iexceed.chrome.driverPath"));
            ChromeOptions options = new ChromeOptions();
            LOGGER.info("setting insecure certificate options");
            options.setAcceptInsecureCerts(true);
            if ("true".equalsIgnoreCase(props.getProperty("com.web.iexceed.headlessmode", "false"))) {
                LOGGER.info("setting headless moe and jenkins config enabled");
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.setHeadless(true);
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920x1080");

            }
            driver = new ChromeDriver(options);
            webdriver.set(driver);
        } else if ("firefox".equalsIgnoreCase(browser)) {
            System.setProperty("webdriver.gecko.driver", props.getProperty("com.iexceed.firefox.driverPath"));
            FirefoxOptions options = new FirefoxOptions();
            options.setAcceptInsecureCerts(true);
            if ("true".equalsIgnoreCase(props.getProperty("com.web.iexceed.headlessmode", "false"))) {
                options.setHeadless(true);
            }
            driver = new FirefoxDriver(options);
            webdriver.set(driver);
        } else if ("ie".equalsIgnoreCase(browser)) {
            System.setProperty("webdriver.ie.driver", props.getProperty("com.iexceed.ie.driverPath"));
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
            driver = new InternetExplorerDriver(options);
            webdriver.set(driver);
        } else {
            throw new Exception("Browser is invalid");
        }

    }
    public static WebDriver pcloudyInitialization(String url) throws Exception {
        LOGGER.info("Instantiate browser details");
        LOGGER.info(props.getProperty("browser"));
        String browserType = props.getProperty("browser");
        LOGGER.info("Browser name is set");
        String runMode = props.getProperty("runmode");
        String browser = browserType.trim();
        LOGGER.info("Browser name trim if space");
        if ("hub".equalsIgnoreCase(runMode)) {
            capabilities = new DesiredCapabilities();
            if ("chrome".equalsIgnoreCase(browser)) {
                capabilities.setBrowserName(BrowserType.CHROME);
            } else if ("firefox".equalsIgnoreCase(browser)) {
                capabilities.setBrowserName(BrowserType.FIREFOX);
            } else if ("ie".equalsIgnoreCase(browser)) {
                capabilities.setBrowserName(BrowserType.IE);
            }
            String host = props.getProperty("seleniumHubHost");
            driver1 = new RemoteWebDriver(new URL(host + "wd/hub"),capabilities);
        } else if ("chrome".equalsIgnoreCase(browser)) {
            LOGGER.info("setting chromedriver path details");
            System.setProperty("webdriver.chrome.driver", props.getProperty("com.iexceed.chrome.driverPath"));
            ChromeOptions options = new ChromeOptions();
            LOGGER.info("setting insecure certificate options");
            options.setAcceptInsecureCerts(true);
            if ("true".equalsIgnoreCase(props.getProperty("com.web.iexceed.headlessmode"))) {
                LOGGER.info("setting headless mode and jenkins config enabled");
                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", props.getProperty("downloadFilepath"));
                chromePrefs.put("safebrowsing.enabled", "true");
                options.setExperimentalOption("prefs", chromePrefs);
                options.addArguments("--safebrowsing-disable-download-protection");
                options.addArguments("--no-sandbox");
                options.setHeadless(true);
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920x1080");
            }
            driver1 = new ChromeDriver(options);
        } else if ("firefox".equalsIgnoreCase(browser)) {
            System.setProperty("webdriver.gecko.driver", props.getProperty("com.iexceed.firefox.driverPath"));
            FirefoxOptions options = new FirefoxOptions();
            options.setAcceptInsecureCerts(true);
            if ("true".equalsIgnoreCase(props.getProperty("com.web.iexceed.headlessmode"))) {
                options.setHeadless(true);
            }
            driver1 = new FirefoxDriver(options);
        } else if ("ie".equalsIgnoreCase(browser)) {
            System.setProperty("webdriver.ie.driver", props.getProperty("com.iexceed.ie.driverPath"));
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
            driver1 = new InternetExplorerDriver(options);
        } else {
            throw new Exception("Browser is invalid");
        }
        driver1.get(url);
        Thread.sleep(10000);
        driver1.manage().deleteAllCookies();
        driver1.manage().window().maximize();

        return driver1;
    }


    public static WebDriver getDriver()
    {
        return webdriver.get();
    }

    public static void setDriver(WebDriver driver) {
        webdriver.set(driver);
    }
}
