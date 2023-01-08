package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.Idropdown;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class DropdownControls implements Idropdown {
    private Select getDropdown(WebElement element){
        Select dropdown = new Select(element);
        return dropdown;
    }
    @Override
    public void selectViaVisibleText(WebElement element, String visibleText) throws Exception {
        getDropdown(element).selectByVisibleText(visibleText);
    }

    @Override
    public void selectViaValue(WebElement element, String value) throws Exception {
        getDropdown(element).selectByValue(value);
    }

    @Override
    public void selectViaIndex(WebElement element, int index) throws Exception {
        getDropdown(element).selectByIndex(index);
    }

    @Override
    public boolean isMultiple(WebElement element) throws Exception {
        return getDropdown(element).isMultiple();
    }

    @Override
    public List<WebElement> getAllOptions(WebElement element) throws Exception {
        return getDropdown(element).getOptions();
    }

    @Override
    public List<WebElement> getAllSelectedOptions(WebElement element) throws Exception {
        return getDropdown(element).getAllSelectedOptions();
    }

    @Override
    public WebElement getFirstSelectedOption(WebElement element) throws Exception {
        return getDropdown(element).getFirstSelectedOption();
    }
}
