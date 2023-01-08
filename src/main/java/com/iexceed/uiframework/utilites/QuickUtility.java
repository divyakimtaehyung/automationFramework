package com.iexceed.uiframework.utilites;


import com.iexceed.uiframework.core.TestBase;
import com.iexceed.uiframework.implementations.CommonElements;
import com.iexceed.uiframework.implementations.DropdownControls;
import com.iexceed.uiframework.implementations.JavaScriptControls;
import com.iexceed.uiframework.implementations.TextBoxControls;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;


import java.util.List;
import java.util.Map;

public class QuickUtility extends TestBase {

    //Quick Utility are Short Hand Utility for Reduce Line of Code
    
    WebDriver localDriver = TestBase.getDriver();
    TextBoxControls textBoxControls;
    CommonElements commonElements;
    JavaScriptControls javaScriptControls;
    WaitUtility waitUtility;
    DropdownControls dropdownControls;

    @FindBy(className = "ajs-content")
    WebElement popMessage;
    @FindBy(className = "ajs-button")
    WebElement popOk;


    public QuickUtility(){
        PageFactory.initElements(localDriver,this);
        textBoxControls = new TextBoxControls();
        commonElements = new CommonElements();
        javaScriptControls = new JavaScriptControls(localDriver);
        waitUtility = new WaitUtility();
        dropdownControls = new DropdownControls();
    }

    public void waitForElement(WebElement element){
        waitUtility.waitTillElementVisible(localDriver,element,100);
    }

    public void sendKeys(WebElement element, String value) throws Exception {
        textBoxControls.clearText(element);
        textBoxControls.setText(element,value);
    }

    public void click(WebElement element) throws Exception {
        try {
            commonElements.click(element);
        }catch (Exception e){
            javaScriptControls.executeJavaScript("arguments[0].scrollIntoView();", element);
            javaScriptControls.executeJavaScript("arguments[0].click();", element);
        }
    }

    public void listSelection(WebElement element, List<WebElement> webElementList, String value) throws Exception {
        javaScriptControls.executeJavaScript("arguments[0].scrollIntoView();", element);
        waitUtility.waitTillElementBecomeClickable(localDriver,element,30);
        click(element);
        selectFromDropDown(webElementList,value);
    }

    public void selectFromDropDown(List<WebElement> elementList, String value) throws Exception {
        waitForFirstElement(elementList);
        for(WebElement elm : elementList){
            if (elm.getText().equals(value)){
                click(elm);
                break;
            }
        }
    }

    public void listSelectionByData(WebElement element, List<WebElement> webElementList, String value) throws Exception {
        javaScriptControls.executeJavaScript("arguments[0].scrollIntoView();", element);
        waitUtility.waitTillElementBecomeClickable(localDriver,element,30);
        click(element);
        selectFromDropDownByData(webElementList,value);
    }

    public void selectFromDropDownByData(List<WebElement> elementList, String value) throws Exception {
        waitForFirstElement(elementList);
        for(WebElement elm : elementList){
            if (elm.getAttribute("data-value").equals(value)){
                click(elm);
                break;
            }
        }
    }


    public void checkListContains(List<WebElement> elementList, String value) {
        boolean flag = true;
        for(WebElement elm : elementList){
            if (elm.getText().equals(value)){
                flag = false;
                break;
            }
        }
        if (flag)
            Assert.fail("List value is not found");
    }

    public boolean verifyListContains(List<WebElement> elementList, String value) {
        boolean flag = false;
        for(WebElement elm : elementList){
            if (elm.getText().equals(value)){
                flag = true;
                break;
            }
        }
       return flag;
    }

    public void assertMessage(WebElement element, String message) {
        waitForElement(element);
        Assert.assertEquals(element.getText().trim(),message);
    }

    public void assertClassName(WebElement element, String message) {
        waitForElement(element);
        Assert.assertTrue(element.getAttribute("class").contains(message));
    }

    public void selectTagSelection(WebElement entityName, String value) throws Exception {
        dropdownControls.selectViaVisibleText(entityName,value);
    }

    public void waitForSeconds(int i) throws Exception {
        waitUtility.waitForSeconds(i);
    }

    public void multiSelection(WebElement element, List<WebElement> elementList, String value) throws Exception {
        javaScriptControls.executeJavaScript("arguments[0].scrollIntoView();", element);
        click(element);
        String[] valueArray = value.split(",");
        for (String val : valueArray) {
            selectFromDropDown(elementList, val.trim());
        }
    }

    public void waitForFirstElement(List<WebElement> listELm) throws Exception {
        int i = 0;
        while (!(listELm.size() >= 1)){
            waitUtility.waitForSeconds(1);
            if (i == 60) {
                Assert.fail("Drop down / List not found");
                break;
            }
            i++;
        }
    }


    public void waitForFirstElementPro(List<WebElement> listELm) throws Exception {
        int i = 0;
        while (!(listELm.get(0).getText().length() >= 1)){
            waitUtility.waitForSeconds(1);
            if (i == 60) {
                Assert.fail("Drop down not found");
                break;
            }
            i++;
        }
    }
    
    public void verifyListContainsAllValues(WebElement element, List<WebElement> elementList, List<Map<String, String>> allListValues) throws Exception {
        javaScriptControls.executeJavaScript("arguments[0].scrollIntoView();", element);
        waitUtility.waitTillElementBecomeClickable(localDriver,element,30);
        click(element);
        System.out.println("Access List "+elementList.size());
        for (Map<String, String> dataList : allListValues) {
            checkListContains(elementList,dataList.get("ApplicationName").trim());
        }
    }

    public void validateMessageBox(String message) throws Exception {
        waitForElement(popMessage);
        assertMessage(popMessage,message);
        click(popOk);
    }


    public boolean checkSameTextEnteredPresent(WebElement element, String text) {
        return element.getText().trim().equals(text);
    }


    public void waitForTextVisible(WebElement element) throws Exception {
        int i = 50;
        while (element.getText().isEmpty() && i!=0) {
            waitForSeconds(1);
            i--;
        }
    }

}
