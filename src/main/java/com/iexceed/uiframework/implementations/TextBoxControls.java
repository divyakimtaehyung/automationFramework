package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.Itextbox;
import org.openqa.selenium.WebElement;

public class TextBoxControls implements Itextbox {
    @Override
    public void setText(WebElement element, String textToSet) throws Exception {
        element.sendKeys(textToSet);

    }

    @Override
    public void clearText(WebElement element) throws Exception {
        element.clear();

    }
}
