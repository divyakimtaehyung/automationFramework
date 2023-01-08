package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.Ijavascript;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptControls implements Ijavascript {
    private WebDriver driver;

    public JavaScriptControls(WebDriver driver){
        this.driver = driver;
    }
    private JavascriptExecutor getJsEngine(){
        JavascriptExecutor jsEngine = (JavascriptExecutor)driver;
        return jsEngine;


    }
    @Override
    public void executeJavaScript(String scriptToExecute, WebElement element) throws Exception {

        getJsEngine().executeScript(scriptToExecute,element);

        
    }

    @Override
    public void scrollDown(int x, int y) throws Exception {
        String scriptToExecute =String.format("window.scrollBy(%d.%d)",x,y);
        getJsEngine().executeScript(scriptToExecute);
    }

    @Override
    public String executeJavaScriptWithReturnValue(String scriptToExecute) throws Exception {
        return (String) getJsEngine().executeScript(scriptToExecute);

    }
}
