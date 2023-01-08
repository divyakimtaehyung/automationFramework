package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.IFrameHandling;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FrameControls implements IFrameHandling {
    private WebDriver driver;

    public FrameControls(WebDriver driver){
        this.driver = driver;
    }
    @Override
    public void switchToFrame(String frameId) throws Exception {
        driver.switchTo().frame(frameId);
    }

    @Override
    public void switchToFrame(WebElement element) throws Exception {
        driver.switchTo().frame(element);
    }

    @Override
    public void switchToFrame(int index) throws Exception {
        driver.switchTo().frame(index);

    }

    @Override
    public void switchToDefaultContent() throws Exception {
        driver.switchTo().defaultContent();

    }
}
