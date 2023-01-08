package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.IwindowHandling;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class WindowHandling implements IwindowHandling {
    private WebDriver driver;

    public WindowHandling(WebDriver driver){
        this.driver = driver;
    }
    @Override
    public void switchToChildWindow(int childWindowIndex) throws Exception {
        String childWindow = driver.getWindowHandles().toArray()[childWindowIndex].toString();
        driver.switchTo().window(childWindow);

    }

    @Override
    public void switchToAnyWindow(String windowHandle) throws Exception {
        driver.switchTo().window(windowHandle);
    }

    @Override
    public String getWindowHandle() throws Exception {
        return driver.getWindowHandle();
    }

    @Override
    public Set<String> getWindowHandles() throws Exception {
        return driver.getWindowHandles();
    }
}
