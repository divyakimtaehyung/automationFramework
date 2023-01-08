# Appzillon AUTOMATION FRAMEWORK

It is an Automation framework for developing tests using Selenium/WebDriver and TestNG.Single code framework to test andriod, ios , web app using appium. Its a boilerplate code.Add it as dependency in pom.xml file and you are good to go!

This framework abstracts away the boiler plate code that are needed for setting up any Selenium/WebDriver/TestNG based automation framework. It provides the below abilities:

- Logging: Log4j enabled for logging
- Generation of screenshots in case of test failure
- Run tests on different browsers/platforms
- Run tests locally, on in-house selenium grid, on sauce labs or on browserstack
- Implementation class to handle the web components.
- Generate customized reports like Cucumber reporting, Extent reports
- Abstract away driver creation, capability creation based on browser
- Rerun failed test cases
- Inject test data from data source like Excel(DDT)
- Store/retrieve locators from object repositories
- Scalable and Maintainable
- Email Reporter
- Configurable project parameters like, application url, time waits, selenium grid information etc.
- Single code framework to test andriod, ios , web app using appium[CreateSession and Generic Methods]
- Rest Assured Wrapper class for Rest Assured API Test Automation [RestAssuredWrapper]

To use the framework, clone the repo and run the command:
```
git clone git@192.168.2.6:product-test-automation/automation-platform.git
mvn -s settings.xml clean package
```
It will generate `AAF-jar-with-dependencies.jar` file. Now create a simple maven project using the command 
```
mvn archetype:generate -DgroupId={project-packaging}
   -DartifactId={project-name}
   -DarchetypeArtifactId=maven-archetype-quickstart
   -DinteractiveMode=false
```

Import this project in Intelliji and any other Java IDE. Add the `AAF-jar-with-dependencies.jar` file to build path or add the dependency in pom.xml
 <dependency>
    <groupId>com.iexceed</groupId>
    <artifactId>appzillon-automation-framework</artifactId>
     <version>1.0-SNAPSHOT</version>
      <classifier>jar-with-dependencies</classifier>
  </dependency>

### Steps to create Hooks.java under step definitions


```
package com.iexceed.uiframework.stepdefinitions;

import com.iexceed.uiframework.core.TestBase;
import io.cucumber.java.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class Hooks extends TestBase {
    @Before()
    public  void init(){
        System.out.println("Starting scenario");

    }
    @BeforeStep()
    public  void bf(){
        System.out.println("Before scenario");

    }
    @AfterStep("not @api")
    public  void af(Scenario scenario) throws IOException {
        File src =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        scenario.attach(fileContent,"image/png","screenshot");
    }

    @After("not @api")
    public void teardown(Scenario scenario) throws Exception {
       /* if(scenario.isFailed()){
            System.out.println("taking screenshot");
            final byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot,"image/png","screenshot");

        }*/

      //  VideoRecorder_utlity.stopRecord();
        driver.quit();
    }

}

```
### Create a project configuration file

Create a new file `project.properties` in the root of the project and add following  lines:

```
# Base application url
url=http://www.yahoo.com

# Directory where element locators will be stored. All element locator files should end with .properties.
# Locators can be specified in this format: locator_name=locator_strategy,locator
# For ex: mail_link=XPATH,//span[text()='Mail']. Supported locator strategies are 'XPATH', 'ID', 'NAME', 
# 'CLASS_NAME', 'TAG_NAME', 'CSS_SELECTOR', 'LINK_TEXT', 'PARTIAL_LINK_TEXT'
object.repository.dir=src/main/resources

# Application modules
application.modules=login,booking,search

# Number of times to rerun the test in case of test failure. '0' means, failed tests will not be
# executed again
max.retry.count=0
#Grid

seleniumHubHost               = http://iexceed.grid.com
com.web.iexceed.headlessmode = true #Run in headless mode
# Path to chromedriver executable file
com.iexceed.chrome.driverPath = src/main/resources/drivers/chromedriver/chromedriver
#com.iexceed.firefox.driverPath = src/main/resources/drivers/geckodriver/geckodriver
runmode                       = Standalone
```

### Create Feature file

Create a test script to open browser, navigate to login page and click on 'SignOn' buttton

```
@sanity 
Feature: Onboard a product onboarding application with all 4 products and hierarchy as Parent,Customer Group and customer

  Scenario Outline: Create an application with all 4 products and enrichment details with hierarchy as Parent Group
    Given a product onboarding application which is at Input stage
    And a product onboarding web portal for OPMAKER role
    When staff user logs in with the role
```
### Create Base Test to instantiate browser and StepDefinitions
````
#Base Test Class under step definitions
package com.iexceed.uiframework.stepDefinitions;

import com.iexceed.uiframework.core.TestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest extends TestBase{
    private static Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeMethod()
    @Parameters({ "browserType" })
    public static void launchBrowser(String browserType) throws Exception {

        LOGGER.info("Launching browser");
        TestBase.initialization(browserType);
    }

}
#Create an LoginPageStepDefinitions file in the root of the directory src/test/java and add the following content:

package com.iexceed.uiframework.stepdefinitions.ProductOnboarding;

import com.iexceed.uiframework.core.TestBase;
import com.iexceed.uiframework.steps.ProductOnboarding.DashboardPageActions;
import com.iexceed.uiframework.steps.ProductOnboarding.LoginPageActions;
import com.iexceed.uiframework.utilites.readexcel.ExcelHandler;
import com.iexceed.uiframework.utilites.readexcel.TestDataHandler;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class LoginPageStepDefinitions extends TestBase{
    private static Logger LOGGER = LogManager.getLogger(LoginPageStepDefinitions.class);
    LoginPageActions loginPageActions = new LoginPageActions();
    TestDataHandler testdata = new TestDataHandler();
    public static String Screenshotdir ="test-output/Screenshots";

    @Given("^user is on the login page$")
    public void userIsOnLoginPage() throws Exception {
        LOGGER.info("Browser Initialized and navigating to url");
        TestBase.initialization(props.getProperty("url"));
        LOGGER.info("intialized browser");

        //VideoRecorder_utlity.startRecord("ProductOnboardingRecording");

    }
    @When("^user enters login credentials '(.*)' and '(.*)'$")
    public void userEntersLoginCredentials(String username, String password) throws Exception {
        LoginPageActions loginPageActions = new LoginPageActions();
        LOGGER.info("Enter the username and password");
        loginPageActions.typeUsername(username);
        loginPageActions.typePassword(password);
    }
    @Then("user logs into application")
    public void userLogsApplication() throws Exception {
        LoginPageActions loginPageActions = new LoginPageActions();
        LOGGER.info("Click on loginBtn");
        loginPageActions.clickOnLoginBtn();
    }
    @And("opChecker logs in with the credentials '(.*)' and '(.*)'$")
    public void opCheckerLogsInWithTheCredentials(String username, String password) throws Exception {
        LoginPageActions loginPageActions = new LoginPageActions();
        loginPageActions.navigateToTheUrl();
        loginPageActions.enterLoginCredentials(username,password);
        loginPageActions.clickSignInButton();
        DashboardPageActions dashboardPageActions =new DashboardPageActions();
        dashboardPageActions.verifyDashboardPageTitle();
    }
```
````
### Create StepActions

```
#Create an LoginPageActions file in the root of the directory src/test/java and add the following content:
package com.iexceed.uiframework.steps.ProductOnboarding;

import com.iexceed.uiframework.PageObjects.ProductOnboarding.HomePage;
import com.iexceed.uiframework.PageObjects.ProductOnboarding.LoginPage;
import com.iexceed.uiframework.core.TestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPageActions extends TestBase {
    private static Logger LOGGER = LogManager.getLogger(LoginPageActions.class);
    LoginPage loginPage = new LoginPage();
    HomePage homePage;

    public void LoginPageActions(){
        PageFactory.initElements(driver,this);
    }

    public void typeUsername(String username) throws Exception {
        loginPage.refreshPage();
        loginPage.setWaitUtilityUsername();
        loginPage.setUserName(username);

    }

    public void typePassword(String pasword) throws Exception {
        loginPage.setWaitUtilityPassword();
        loginPage.setWaitUtilityPassword();
        loginPage.setPassword(pasword);
    }

    public void clickOnLoginBtn() throws Exception {
       homePage = loginPage.clickOnSigninButton();
    }

    public void enterLoginCredentials(String username, String pasword)throws Exception{

        loginPage.setUserName(username);
        loginPage.setPassword(pasword);
    }

```
````
### Create Few Page classes
```
Create a new file `loginPage.java` in `src/main/java` folder.
package com.iexceed.uiframework.uitests;

public class LoginPage extends TestBase {
    WebDriver driver = TestBase.getDriver();

    @FindBy(id="ologin__Login__username")
    WebElement userName;
    @FindBy(id="ologin__Login__password")
    WebElement password;
    @FindBy(id="ologin__Login__el_btn_1")
    WebElement loginBtn;

     private CommonElements commonElements;
        private CommonDriver commonDriver;
        private TextBoxControls textBoxControls;
        private WaitUtility waitUtility;
        private WindowHandling windowHandling;
        private ScreenshotControl screenshotControl;
        private JavaScriptControls javaScriptControls;
    
    
        public LoginPage(){
            PageFactory.initElements(driver,this);
            commonElements = new CommonElements();
            commonDriver = new CommonDriver(driver);
            textBoxControls= new TextBoxControls();
            waitUtility= new WaitUtility();
            windowHandling = new WindowHandling(driver);
            screenshotControl = new ScreenshotControl(driver);
            javaScriptControls = new JavaScriptControls(driver);
    
    
        }
    public void enterLoginCredentials(String username, String pasword)throws Exception{
    
            setUserName(username);
            waitUtility.waitForSeconds(10);
            setPassword(pasword);
    
        }
    
        public HomePage clickOnSigninButton() throws Exception {
            setWaitUtilityLoginBtn();
            getSignInBtn();
            return new HomePage();
        }

```

## Store element locators for appium

To store element locators create .properties files inside src/main/resources folder. Sample locator file will look like this:

```
mail_link=XPATH,//span[text()='Mail']
email_text_field=ID,email
password_field=NAME,password
```

## Run Test Script

To run the test script, we need a test runner file. Create an testrunner file in the root of the directory src/test/java and add the following content:

```
package com.iexceed.uiframework.runners;
//import io.cucumber.junit.Cucumber;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

//import org.junit.runner.RunWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.cucumber.testng.FeatureWrapper;


//@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/Features/ProductOnboarding/",
        glue = {"com.iexceed.uiframework.stepdefinitions"},
        dryRun=false,
        monochrome=true, //clear output
        tags= "@sanitytest1",
        plugin= {"pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "json:target/jsonReports/cucumber-reports.json","html:target/cucumber-ui-reports.html",
                "rerun:target/failedrerun.txt"
        }
        //sanity and regression tags= {"@sanity,@regression"}-sanity or regression

)
public class TestRunner {
    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws Exception {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(dataProvider = "features")
    public void feature(PickleWrapper eventwrapper, FeatureWrapper cucumberFeature) throws Throwable {
        //testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
        testNGCucumberRunner.runScenario(eventwrapper.getPickle());
    }

    @DataProvider//(parallel=true)
    public Object[][] features() {
        // return testNGCucumberRunner.provideFeatures();
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception {
        testNGCucumberRunner.finish();
    }
}


```
At this point, chrome browser will be launched and test will be executed. Reports will be generated inside test-output folder. Currently supported browsers are `firefox`, `chrome` and `ie`.

# Additional Information

## Creating browser profile

We can set different profile parameters for different browsers using browser profiles. Currently supported browser profiles are ChromeBrowserProfile, FirefoxBrowserProfile and IEBrowserProfile. To use a profile, use it like this:

```
FirefoxBrowserProfile ffProfile = new FirefoxBrowserProfile();
	
ffProfile.setAcceptUntrustedCertificates(true).showDownloadManagerWhenStarting(false)
                .setDownloadDirectory("/Users/ankita/Downloads");
        
DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser)
                .addBrowserDriverExecutablePath(config.getProperty("gecko.driver.path")).addVersion(version)
                .addPlatform(platform).addBrowserProfile(ffProfile).build();

driver = new WebDriverFactory().createDriver(caps);

```
## Using proxy

To access the websites using proxy, use it like:

```
DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser).addProxy("192.168.3.60", 5678).
                .addBrowserDriverExecutablePath(config.getProperty("gecko.driver.path")).addVersion(version)
                .addPlatform(platform).addBrowserProfile(ffProfile).build();
```

## Running tests using selenium GRID

Assuming that selenium Grid is configured on localhost and is running on port 4444, you can run your tests on grid using `GridUrlBuilder`

```
URL remoteHubUrl = new SeleniumGridUrlBuilder().addProtocol(Protocol.HTTP).addSeleniumHubHost("127.0.0.1")
                .addSeleniumHubPort(4444).build();

driver = new WebDriverFactory().createDriver(remoteHubUrl, caps);
```

## Supported element actions

To check the supported element actions/supported selenium commands see [BasePage.java](src/main/java/com/iexceed/uiframework/core/BasePage.java) 

## Test Data wih excel
One approach could be to store the data in an external file and load them in runtime.We are going to use the concept of step argument transform to load the data and convert it into data table and then one can use the data table on the step defination


## Failure screenshot

In case of test failure, the framework will generate the failure screenshot and will copy the file to target folder

## Logging

Shows the logs on the console and save the logs in the Logs folder of each run

This will create a new file in logs folder with the name of test class being executed, and will capture all logs in that file. Ideally it's a good idea to capture the test script logs in separate log files, instead of putting all logs inside a huge log file. It helps in debugging issues faster.


## Package : appium

CreateSession.java : All the methods to create the session and destroy the sessions after the test's
execution is over. Each tests extend this class. Below are the methods in create session.

  - invokeAppium() - method starts the Appium Server. Calls the AppiumStartServer method to start the appium session based on your OS.
  - createDriver(String os, Method methodName) - method creates the driver based on the passed parameters(android,ios,web)
  - tearDown()- method quits the driver after the execution.
  - stopAppium() - method stops the Appium Server. Calls the AppiumStopServer method to stop the appium session based on your OS.
  
Generic Methods: It's  a  common repository for all the  appium methods.It is to reduce the duplicate code .Each OR class extends this class.
  

```
