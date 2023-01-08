package com.iexceed.uiframework.contracts;

import java.util.Set;

public interface IwindowHandling {

    public void switchToChildWindow(int childWindowIndex)throws Exception;
    public void switchToAnyWindow(String windowHandle)throws Exception;
    public String getWindowHandle()throws Exception;
    public Set<String> getWindowHandles()throws Exception;
}
