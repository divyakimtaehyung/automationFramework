package com.iexceed.uiframework.contracts;

import org.openqa.selenium.WebElement;

public interface Itextbox {

    public void setText(WebElement element, String textToSet) throws Exception;
    public void clearText(WebElement element) throws Exception;
}
