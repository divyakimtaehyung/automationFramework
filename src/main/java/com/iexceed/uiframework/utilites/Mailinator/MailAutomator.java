package com.iexceed.uiframework.utilites.Mailinator;

import com.iexceed.uiframework.core.TestBase;
import com.iexceed.uiframework.utilites.QuickUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import java.util.ArrayList;
import java.util.List;

public class MailAutomator extends TestBase {

    // Mailinator - Mail Automation used to get Email Message Body , getEmailBody() first param Email ID , second param for Subject

    WebDriver driver = TestBase.getDriver();

    @FindBy(xpath = "//input[@id='search']")
    WebElement searchField;
    @FindBy(xpath = "//button[@value='Search for public inbox for free']")
    WebElement searchGo;
    @FindBy(xpath = "//table[@class='table-striped jambo_table']/tbody/tr/td[3]")
    List<WebElement> subjectList;
    @FindBy(xpath = "//a[@onclick='deleteEmail();']")
    WebElement deleteMail;


    QuickUtility utility;
    public MailAutomator(){
        PageFactory.initElements(driver,this);
        utility = new QuickUtility();
    }

    public static void main(String[] args) throws Exception {
//        TestBase tn = new TestBase();
//        TestBase.initialization("chrome");
//        TestBase.getDriver().get("https://www.google.co.in/");
        MailAutomator gm = new MailAutomator();
        System.out.println(gm.getEmailBody("abc@mailinator.com","Your flight booking is confirmed"));
    }

    public String getEmailBody(String mailID,String mailSubject) throws Exception {
        openUrl();
        findEmailID(mailID.split("@")[0]);
        selectRecentMail(mailSubject);
        return fetchMailBody();
    }

    public String getEmailBodyMobileAutomation(String mailID,String mailSubject) throws Exception {
        openUrlMobileAutomation();
        findEmailID(mailID.split("@")[0]);
        selectRecentMail(mailSubject);
        return fetchMailBody();
    }

    private void openUrlMobileAutomation()  {
        TestBase.getDriver().get("https://www.mailinator.com");
        TestBase.getDriver().manage().deleteAllCookies();
        TestBase.getDriver().manage().window().maximize();
    }

    private void openUrl()  {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        TestBase.getDriver().get("https://www.mailinator.com");
        TestBase.getDriver().manage().deleteAllCookies();
        TestBase.getDriver().manage().window().maximize();
    }

    private void findEmailID(String receiver) throws Exception {
        utility.sendKeys(searchField,receiver);
        utility.click(searchGo);
    }

    private String fetchMailBody() throws Exception {
        driver.switchTo().frame("html_msg_body");
        waitForMessageDisplay();
        utility.waitForSeconds(3);
        String message = driver.findElement(By.xpath("html/body")).getText();
        driver.switchTo().defaultContent();
//        utility.click(deleteMail);
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().defaultContent();
        driver.switchTo().window(tabs.get(0));
        return message;
    }


    private void selectRecentMail(String value) throws Exception {
        WebElement target = null;
        utility.waitForFirstElement(subjectList);
        for(WebElement elm : subjectList){
            System.out.println(elm.getText()+" = "+value);
            if (elm.getText().trim().equals(value)){
                target = elm;
                break;
            }
        }
        if (target != null) {
            utility.click(target);
        }
    }


    private void waitForMessageDisplay() throws Exception {
        for (int i=0;i < 30;i++){
            if (driver.findElement(By.xpath("html/body")).getText().length() > 10){
                break;
            }
            utility.waitForSeconds(1);
        }
    }


}

