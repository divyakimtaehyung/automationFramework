package com.iexceed.uiframework.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PcloudyConnection {
    public CreateSession createSession = new CreateSession();
    public static ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<AppiumDriver>();
    private static Logger LOGGER = LogManager.getLogger(PcloudyConnection.class);

    /**
     * this method for connecting a remote device ( pcloudy ) to our test
     *
     * @param pcloudyUserName   User name of Pcloudy Account ex Email
     * @param pcloudyAPIKey     API key for Pcloudy Account
     * @param platform          platform of mobile application
     * @param version           version of the device
     * @param deviceName        deviceName manufacture
     * @param automationName    automationName (UI automator and xcuitest)
     * @param applicationName   application name of Android = Pcloudy uploaded apk name , IOS = Download link of IPA
     * @param androidActivity   For Android - Activity name
     * @param androidPackage    For Android - Android Package name
     * @param bundleID          for IOS - Bundle ID of the IPA
     * @param maxDuration       Pcloudy session max duration
     * @param url               Connection URl of the Device
     * @param orientation       Orientation of the device ( Landscape or Portrait )
     * @param isTrustDevice     In Distribution build of IOS device ,that time it's should be true
     * @param targetCompanyName By element which has name of the distribution company name
     * @param trustCompanyName  By element which has company in trust window
     * @param trustBtn          By element Button which has a trust keyword
     * @throws MalformedURLException throws exception when URL malformed
     * @throws InterruptedException  throws exception when Interruption occurred on the execution
     */

    public void setRemoteDeviceCapabilities(String pcloudyUserName, String pcloudyAPIKey, String platform, String version, String deviceName, String automationName, String applicationName, String androidActivity, String androidPackage, String bundleID, String maxDuration, String url, String orientation,
                                            Boolean isTrustDevice, By targetCompanyName, By trustCompanyName, By trustBtn) throws MalformedURLException, InterruptedException {
        AppiumDriver driver = null;
        if (appiumDriver == null) {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("pCloudy_Username", pcloudyUserName);
            capabilities.setCapability("pCloudy_ApiKey", pcloudyAPIKey);
            capabilities.setCapability("pCloudy_DurationInMinutes", maxDuration);
            capabilities.setCapability("newCommandTimeout", 600000);
            capabilities.setCapability("launchTimeout", 90000);
            capabilities.setCapability("pCloudy_DeviceFullName", deviceName);
            capabilities.setCapability("platformVersion", version);
            capabilities.setCapability("platformName", platform);
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);
            capabilities.setCapability("acceptAlerts", true);
            capabilities.setCapability("automationName", automationName);
            capabilities.setCapability("pCloudy_WildNet", "true");
            capabilities.setCapability("pCloudy_EnableVideo", "false");
            capabilities.setCapability("pCloudy_EnablePerformanceData", "false");
            capabilities.setCapability("pCloudy_EnableDeviceLogs", "true");
            capabilities.setCapability("autoGrantPermissions", "true");
            capabilities.setCapability("autoAcceptAlerts", "true");
            capabilities.setCapability("orientation", orientation.toUpperCase());
            try {
                if (platform.equalsIgnoreCase("ios")) {
                    if (isTrustDevice) {
                        capabilities.setCapability("bundleId", "com.apple.Preferences");
                        driver = new IOSDriver<WebElement>(new URL(url), capabilities);
                        launchApp(bundleID, applicationName, targetCompanyName, trustCompanyName, trustBtn);
                    } else {
                        capabilities.setCapability("pCloudy_ApplicationName", applicationName);
                        capabilities.setCapability("bundleId", bundleID);
                        driver = new IOSDriver<WebElement>(new URL(url), capabilities);
                    }
                    LOGGER.info("Driver connected");
                } else {
                    capabilities.setCapability("pCloudy_ApplicationName", applicationName);
                    capabilities.setCapability("appPackage", androidPackage);
                    capabilities.setCapability("appActivity", androidActivity);
                    driver = new AndroidDriver<WebElement>(new URL(url), capabilities);
                    LOGGER.info("Android Driver connected");
                }
                setDriver(driver);
            } catch (Exception e) {
                LOGGER.info("Devices not available : " + e);
            }
        } else {
            if (platform.equalsIgnoreCase("ios")) {
                if (isTrustDevice) {
                    reInstallApp(bundleID, applicationName, targetCompanyName, trustCompanyName, trustBtn);
                }else {
                    reInstallAppInResigned(bundleID, applicationName);
                }
            } else {
                reStartAppAndroid();
            }
            LOGGER.info("Restarting new App");
        }
    }

    /**
     * this method for connecting a local Android device to our test
     *
     * @param platform        platform of mobile application
     * @param deviceName      deviceName manufacture
     * @param automationName  automationName (UI automator )
     * @param applicationName application name of Android
     * @param androidActivity For Android - Activity name
     * @param androidPackage  For Android - Android Package name
     * @param orientation     Orientation of the device ( Landscape or Portrait )
     * @throws Exception throws exception when Connecting to device
     */

    public void setRealDeviceCapabilities(String platform, String deviceName, String automationName, String applicationName, String androidActivity, String androidPackage, String deviceURL, String noReset, String orientation) throws Exception {
        if (getDriver() == null) {
            setCapability(platform, deviceName, automationName, applicationName, androidActivity, androidPackage, deviceURL, noReset);
        } else {
            if (platform.equalsIgnoreCase("ios")){
                setCapability(platform, deviceName, automationName, applicationName, androidActivity, androidPackage, deviceURL, noReset);
            }else {
                reStartAppAndroid();
            }
            LOGGER.info("Restarting new App");
        }
        if (orientation.equalsIgnoreCase("landscape")) {
            getDriver().rotate(ScreenOrientation.LANDSCAPE);
            LOGGER.info("Device in Landscape");
        }
    }

    public void setCapability(String platform, String deviceName, String automationName, String applicationName, String androidActivity, String androidPackage, String deviceURL, String noReset) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", platform);
        capabilities.setCapability("app", applicationName);
        capabilities.setCapability("noReset", Boolean.parseBoolean(noReset));
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("autoGrantPermissions", "true");
        capabilities.setCapability("autoAcceptAlerts", "true");
        capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("resetKeyboard", true);
        capabilities.setCapability("autoGrantPermissions", "true");
        capabilities.setCapability("autoAcceptAlerts", "true");
        capabilities.setCapability("newCommandTimeout", 600000);
        if (platform.equalsIgnoreCase("ios")){
            capabilities.setCapability("bundleId", androidPackage);
        }else {
            capabilities.setCapability("appPackage", androidPackage);
            capabilities.setCapability("appActivity", androidActivity);
        }
        setDriver(new AndroidDriver(new URL(deviceURL), capabilities));
    }

    public void launchApp(String bundleId, String app, By targetCompanyName, By trustCompanyName, By trustBtn) throws InterruptedException, MalformedURLException {
        AppiumDriver driver = getDriver();
        HashMap<String, Object> args = new HashMap<>();
        args.put("app", app);
        args.put("bundleId", bundleId);
        LOGGER.info("driver connected");
        driver.terminateApp("com.apple.Preferences");
//        args.put("instrument", "noinstrument");
        driver.executeScript("mobile:installApp", args);
        System.out.println("app installed");
        trustDevice(targetCompanyName, trustCompanyName, trustBtn);
        args.put("bundleId", bundleId);
        driver.executeScript("mobile: launchApp", args);
        System.out.println("app launched");
        LOGGER.info("launch Application");
    }

    public void reInstallApp(String bundleId, String app, By targetCompanyName, By trustCompanyName, By trustBtn) throws InterruptedException {
        AppiumDriver driver = getDriver();
        LOGGER.info("terminate app");
        HashMap<String, Object> args = new HashMap<>();
        args.put("bundleId", bundleId);
        driver.executeScript("mobile: removeApp", args);
        LOGGER.info("Relaunch App");
        HashMap<String, Object> arg = new HashMap<>();
        arg.put("app", app);
        arg.put("bundleId", bundleId);
        driver.executeScript("mobile: installApp", arg);
        trustDevice(targetCompanyName, trustCompanyName, trustBtn);
        arg.put("bundleId", bundleId);
        driver.executeScript("mobile: launchApp", arg);
        System.out.println("app launched");
        LOGGER.info("launch Application");
    }

    public void reInstallAppInResigned(String bundleId, String app) throws InterruptedException {
        AppiumDriver driver = getDriver();
        LOGGER.info("terminate app");
        HashMap<String, Object> args = new HashMap<>();
        args.put("bundleId", bundleId);
        driver.executeScript("mobile: removeApp", args);
        LOGGER.info("Relaunch App");
        HashMap<String, Object> arg = new HashMap<>();
        arg.put("app", app);
        arg.put("bundleId", bundleId);
        driver.executeScript("mobile: installApp", arg);
        arg.put("bundleId", bundleId);
        driver.executeScript("mobile: launchApp", arg);
        System.out.println("app launched");
        LOGGER.info("launch Application");
    }

    public void reStartAppAndroid() throws InterruptedException {
        LOGGER.info("terminate app");
        Thread.sleep(2000);
        getDriver().resetApp();
    }

    public void trustDevice(By targetCompanyName, By trustCompanyName, By trustBtn) throws InterruptedException {
        AppiumDriver driver = getDriver();
        LOGGER.info("activate Settings app");
        driver.activateApp("com.apple.Preferences");
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
        scrollClick("General");
        Thread.sleep(2000);
        try {
            scrollClick("Device Management");
        } catch (Exception e) {
            scrollClick("Profiles & Device Management");
        }

        try {
            driver.findElement(targetCompanyName).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='iPhone Distribution: i-exceed technology solutions private limited']")).click();
            LOGGER.info(e);
        }
        Thread.sleep(1000);
        try {
            driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Trust “i-exceed technology solutions private limited”']")).click();
            Thread.sleep(2000);
        } catch (Exception e) {
            try{
                driver.findElement(trustCompanyName).click();
            }catch (Exception var7){
                LOGGER.debug(var7);
            }
            LOGGER.info("Problem with trust option");
        }
        try {
            driver.findElement(By.xpath("//XCUIElementTypeButton[@name='Trust']")).click();
            Thread.sleep(3000);
            LOGGER.info("terminate settings");
            driver.terminateApp("com.apple.Preferences");
        } catch (Exception e) {
            LOGGER.info("Problem with setting");
            driver.terminateApp("com.apple.Preferences");
        }

    }


    public void scrollClick(String name) {
        AppiumDriver driver = getDriver();
        RemoteWebElement element = (RemoteWebElement) driver.findElement(By.xpath("//*[@name='" + name + "']"));
        String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("element", elementID);
        scrollObject.put("toVisible", "not an empty string");
        driver.executeScript("mobile:scroll", scrollObject);
        element.click();
    }


    public void set_Hybrid_PcloudyDeviceCapabilities(String pcloudyUserName, String pcloudyAPIKey, String platform, String version, String deviceName, String automationName, String applicationName, String androidActivity, String androidPackage, String bundleID, String maxDuration, String url, String orientation,
                                                     Boolean isTrustDevice, By targetCompanyName, By trustCompanyName, By trustBtn) throws MalformedURLException, InterruptedException {
        AppiumDriver driver = null;
        if (appiumDriver == null) {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("pCloudy_Username", pcloudyUserName);
            capabilities.setCapability("pCloudy_ApiKey", pcloudyAPIKey);
            capabilities.setCapability("pCloudy_DurationInMinutes", maxDuration);
            capabilities.setCapability("newCommandTimeout", 600);
            capabilities.setCapability("launchTimeout", 90000);
            capabilities.setCapability("pCloudy_DeviceFullName", deviceName);
            capabilities.setCapability("platformVersion", version);
            capabilities.setCapability("platformName", platform);
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);
            capabilities.setCapability("noReset",true);
            capabilities.setCapability("acceptAlerts", true);
            capabilities.setCapability("automationName", automationName);
            capabilities.setCapability("pCloudy_WildNet", "true");
            capabilities.setCapability("pCloudy_EnableVideo", "false");
            capabilities.setCapability("pCloudy_EnablePerformanceData", "false");
            capabilities.setCapability("pCloudy_EnableDeviceLogs", "false");
            capabilities.setCapability("autoGrantPermissions", "true");
            capabilities.setCapability("autoAcceptAlerts", "true");
            capabilities.setCapability("wdaStartupRetries", "5");
            capabilities.setCapability("autoWebview", true);
            capabilities.setCapability("webviewConnectTimeout", 15000);
            capabilities.setCapability("webviewConnectRetries", "20");
            capabilities.setCapability("orientation", orientation.toUpperCase());
            try {
                if (platform.equalsIgnoreCase("ios")) {
                    if (isTrustDevice) {
                        capabilities.setCapability("bundleId", "com.apple.Preferences");
                        driver = new IOSDriver<WebElement>(new URL(url), capabilities);
                        launchApp(bundleID, applicationName, targetCompanyName, trustCompanyName, trustBtn);
                    } else {
                        capabilities.setCapability("pCloudy_ApplicationName", applicationName);
                        capabilities.setCapability("bundleId", bundleID);
                        driver = new IOSDriver<WebElement>(new URL(url), capabilities);
                    }
                    LOGGER.info("Driver connected");
                } else {
                    capabilities.setCapability("pCloudy_ApplicationName", applicationName);
                    capabilities.setCapability("appPackage", androidPackage);
                    capabilities.setCapability("appActivity", androidActivity);
                    driver = new AndroidDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"), capabilities);
                    LOGGER.info("Android Driver connected");
                }
                setDriver(driver);
            } catch (Exception e) {
                LOGGER.info("Devices not available : " + e);
            }
        } else {
            if (platform.equalsIgnoreCase("ios")) {
                if (isTrustDevice) {
                    reInstallApp(bundleID, applicationName, targetCompanyName, trustCompanyName, trustBtn);
                }else {
                    reInstallAppInResigned(bundleID, applicationName);
                }
            } else {
                reStartAppAndroid();
            }
            LOGGER.info("Restarting new App");
        }
        if (orientation.equalsIgnoreCase("landscape")) {
            getDriver().rotate(ScreenOrientation.LANDSCAPE);
            LOGGER.info("Device in Landscape");
        }
    }

    public static AppiumDriver getDriver()
    {
        return appiumDriver.get();
    }

    public static void setDriver(AppiumDriver driver) {
        appiumDriver.set(driver);
    }
}
