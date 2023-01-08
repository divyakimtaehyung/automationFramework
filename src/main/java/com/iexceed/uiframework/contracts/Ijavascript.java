package com.iexceed.uiframework.contracts;

import org.openqa.selenium.WebElement;

public interface Ijavascript {
    public void executeJavaScript(String scriptToExecute, WebElement element)throws Exception;
    public void scrollDown(int x, int y)throws Exception;
    public String executeJavaScriptWithReturnValue(String scriptToExecute)throws Exception;

}
