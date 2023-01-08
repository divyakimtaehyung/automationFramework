package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.IalertHandling;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

public class AlertControls implements IalertHandling {
    private WebDriver driver;

    private Alert getAlert(){
        return driver.switchTo().alert();
    }

    public AlertControls(WebDriver driver){
        this.driver = driver;
    }

    @Override
    public void acceptAlert() throws Exception {
        getAlert().accept();
    }

    @Override
    public void rejectAlert() throws Exception {
        getAlert().dismiss();
    }

    @Override
    public String getMessageAlert() throws Exception {
        return getAlert().getText();
    }

    @Override
    public boolean isAlertPresent(int timeoutInSeconds) throws Exception {
        return false;

    }
}
