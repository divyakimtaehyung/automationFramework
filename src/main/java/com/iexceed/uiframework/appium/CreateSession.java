package com.iexceed.uiframework.appium;

import com.iexceed.uiframework.utilites.ProjectConfigurator;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/** Contains all methods to create new session and 
 * destroy the session after each test(s) execution is over.
 * Each test extends this class
 */

public class CreateSession {
    public WebDriver driver = null;
    public static Properties config;
    public static ProjectConfigurator projectConfigurator;
    private static Logger LOGGER = LogManager.getLogger(CreateSession.class);
    String OS;


    static {
        try {
            config = ProjectConfigurator.initializeProjectConfigurationsFromFile("src/main/resources/project.properties");
        } catch (IOException e) {
            LOGGER.error("Error in creating properties loader in createSession ");
        }
    }
    /**
     * this method starts Appium server.Calls startAppiumServer method to start the session depending upon your os
     * @Throws Exception unable to start the appium server
     */
    //@BeforeSuite
    public void invokeAppium() throws Exception{
        OS = System.getProperty("os.name").toLowerCase();
        try{
            startAppiumServer(OS);
            LOGGER.info("Appium server started successfully");
        }catch (Exception e){
            LOGGER.error(getClass().getName(),"StartAppium","unable to start appium server");
            throw new Exception(e.getMessage());
        }
        
    }
    /**
     * this method stops Appium server.Calls stopAppiumServer method to stop the session depending upon your os
     * @Throws Exception unable to stop the appium server
     */
    //@AfterSuite
    public void StopAppium() throws Exception{
        String OS = System.getProperty("os.name").toLowerCase();
        try{
            stopAppiumServer(OS);
            LOGGER.info("Appium server stopped successfully");
        }catch (Exception e){
            LOGGER.error(getClass().getName(),"StartAppium","unable to stop appium server");
            throw new Exception(e.getMessage());
        }

    }

    /** 
     * this method creates the driver based on the parameter passed(android or ios)
     * @param os android or ios
     * @param methodName name of the method under execution
     * @throws MalformedURLException Thrown to indicate the malformed URL has occurred
     */
    
    public void createDriver(String os, Method methodName) throws MalformedURLException {
        if(os.equalsIgnoreCase("android")){
            String buildPath = choosebuild(os);
            LOGGER.info("creating driver for Android");
            androidDriver(buildPath,methodName);
        }
        else if(os.equalsIgnoreCase("ios")){
            String buildPath = choosebuild(os);
            LOGGER.info("creating driver for ios");
            iosDriver(buildPath,methodName);
        }
        else if(os.equalsIgnoreCase("web")){
            LOGGER.info("creating driver for browser");
            webappDriver();
        }
    }

    private void webappDriver() throws MalformedURLException {
        LOGGER.info("Setting the  properties to run test in browser");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName",config.getProperty("PlatformName"));
        capabilities.setCapability("platformVersion",config.getProperty("PlatformVersion"));
        capabilities.setCapability("DeviceName",config.getProperty("DeviceName"));
        capabilities.setCapability("automationName",config.getProperty("AutomationName"));
        capabilities.setCapability("browserName",config.getProperty("BrowserName"));
        driver = new AppiumDriver(new URL(config.getProperty("DeviceUrl")),capabilities);
    }

    /**
     * this method creates the android driver
     * @param buildPath : path to pick the location of the app
     * @param methodName : name of the method under execution
     * @throws MalformedURLException Thrown to indicate the malformed URL has occurred
     **/
    
    public synchronized void androidDriver(String buildPath, Method methodName) throws MalformedURLException {
        File app = new File(buildPath);
        LOGGER.info("Setting the AndroidDriver Properties");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName",config.getProperty("DeviceName"));
        capabilities.setCapability("platformName",config.getProperty("PlatformName"));
        capabilities.setCapability("appPackage",config.getProperty("AppPackage"));
        capabilities.setCapability("appActivity",config.getProperty("AppActivity"));
        capabilities.setCapability("name",methodName.getName());
        capabilities.setCapability("app",app.getAbsolutePath());
        capabilities.setCapability(MobileCapabilityType.NO_RESET,false);
        capabilities.setCapability("automationName",config.getProperty("AutomationName"));
        driver = new AndroidDriver(new URL(config.getProperty("DeviceUrl")),capabilities);
        
    }

    /**
     * this method creates the IOS driver
     * @param buildPath : patn to pick the location of the app
     * @param methodName : name of the method under execution
     * @throws MalformedURLException Thrown to indicate the malformed URL has occurred
     */
    public synchronized void iosDriver(String buildPath, Method methodName) throws MalformedURLException {
        File app = new File(buildPath);
        LOGGER.info("Setting the IOSDriver Properties");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName",config.getProperty("PlatformName"));
        capabilities.setCapability("appiumVersion",config.getProperty("AppiumVersion"));
        capabilities.setCapability("name",methodName.getName());
        capabilities.setCapability("app",app.getAbsolutePath());
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,config.getProperty("DeviceName"));
        capabilities.setCapability("automationName",config.getProperty("AutomationName"));
        driver = new IOSDriver(new URL(config.getProperty("DeviceUrl")),capabilities);

    }
    /** this method starts the appium server
     *
     * @param os your machine OS(window/mac/linux)
     * @throws IOException Signals thats I/o exception of some sort has occured
     */
    private void startAppiumServer(String os) throws IOException, InterruptedException {
        if(os.contains("windows")){
            CommandLine command = new CommandLine("cmd");
            command.addArgument("/c");
            command.addArgument("/c:/Program Files/nodejs/node.exe");
            command.addArgument("/c:/Appium/node_modules/appium/bin/appium.js");
            command.addArgument("--address",false);
            command.addArgument("127.0.0.1");
            command.addArgument("--port",false);
            command.addArgument("4723");
            command.addArgument("--full-Reset",false);
            
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(1);
            executor.execute(command ,resultHandler);
            Thread.sleep(50000);
        }
        else if(os.contains("mac os x")){
            CommandLine command = new CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");
            command.addArgument("/c:/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js",false);
            command.addArgument("--address",false);
            command.addArgument("127.0.0.1");
            command.addArgument("--port",false);
            command.addArgument("4723");
            command.addArgument("--full-Reset",false);

            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(1);
            executor.execute(command ,resultHandler);
        }
        else if(os.contains("linux")){
            CommandLine command = new CommandLine("/bin/bash");
            command.addArgument("/c");
            command.addArgument("~/.linuxbew/bin/node");
            command.addArgument("~/.linuxbew/lib/node_modules/appium/lib/appium.js",true);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(1);
            executor.execute(command ,resultHandler);
            Thread.sleep(5000);
        }
        else{
            LOGGER.info("os " + "is not supported yet");
        }
        
        
    }

    /** this method stops the appium server
     * 
     * @param os your machine OS(window/mac/linux)
     * @throws IOException Signals thats I/o exception of some sort has occured
     */
    private void stopAppiumServer(String os) throws IOException {
        if(os.contains("windows")){
            CommandLine command = new CommandLine("cmd");
            command.addArgument("/c");
            command.addArgument("Taskkill/F /IM node.exe");

            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(1);
            executor.execute(command ,resultHandler);
        }
        else if(os.contains("mac os x")){
            String[] command = {"/usr/bin/killall","-KILL","node"};
            Runtime.getRuntime().exec(command);
            LOGGER.info("Appium server stopped");
        }
        else if(os.contains("linux")){
            String[] command = {"/usr/bin/killall","-KILL","node"};
            Runtime.getRuntime().exec(command);
            LOGGER.info("Appium server stopped");
        }
    }
    
   

    /**
     * this method quits the driver after the execution of tests
     */
    //AfterMethod
    public void teardown(){
        LOGGER.info("Shutting down the driver");
        driver.quit();
    }

    /**
     * This method sets the application app path on android or ios
     * @param invokeDriver pass the driver name as android or IOS
     * @return  app path configured
     */

    public String choosebuild(String invokeDriver){
        String appPath = null;
        if(invokeDriver.equals("android")) {
            LOGGER.info("Setting the AndroidAppPath");
            appPath = config.getProperty("AndroidAppPath");
            return appPath;
        }else if(invokeDriver.equals("ios")){
            LOGGER.info("Setting the IOSAppPath");
            appPath = config.getProperty("iosAppPath");
            return appPath;
        
        }
        return appPath;
    }
    
    public WebDriver getWebDriver(){
        return this.driver;
    }

}
