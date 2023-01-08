package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.Icheckbox;
import org.openqa.selenium.WebElement;

public class CheckboxControl implements Icheckbox {

    @Override
    public void changeCheckboxStatus(WebElement element, boolean status) throws Exception {
        if((element.isSelected() && !status) || (!element.isSelected() && status)){
            element.click();
        }
    }
}
