package com.iexceed.uiframework.utilites;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtility {
    public void waitForSeconds(int timeoutInSeconds)throws Exception{
        Thread.sleep(timeoutInSeconds * 1000);
    }
    private WebDriverWait getWaitDriver(WebDriver driver,int timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver,timeoutInSeconds);
        return  wait;
    }
    public void waitTillElementVisible(WebDriver driver, WebElement element,int timeoutInSeconds){
        getWaitDriver(driver,timeoutInSeconds).until(ExpectedConditions.visibilityOf(element));
    }
    public void waitTillAlertPresent(WebDriver driver,int timeoutInSeconds){
        getWaitDriver(driver,timeoutInSeconds).until(ExpectedConditions.alertIsPresent());
    }
    public void waitTillElementBecomeClickable(WebDriver driver, WebElement element,int timeoutInSeconds){
        getWaitDriver(driver,timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
    }
}
