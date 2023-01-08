package com.iexceed.uiframework.contracts;

public interface IalertHandling {
    public void acceptAlert() throws Exception;
    public void rejectAlert() throws Exception;
    public String getMessageAlert() throws Exception;
    public boolean isAlertPresent(int timeoutInSeconds) throws Exception;
}
