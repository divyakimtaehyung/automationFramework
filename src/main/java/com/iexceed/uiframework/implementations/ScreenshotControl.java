package com.iexceed.uiframework.implementations;

import com.iexceed.uiframework.contracts.Iscreenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class ScreenshotControl implements Iscreenshot{

    private TakesScreenshot camera;
    public  ScreenshotControl (WebDriver driver){
        camera = (TakesScreenshot) driver;
    }
    @Override
    public String captureAndSaveScreenshot(String fileName)throws Exception{
        fileName = fileName.trim();
        File imgFile, tempFile;
        imgFile = new File(fileName);
        if (imgFile.exists()){
            throw new Exception("File Already Exists");
        }
        tempFile=camera.getScreenshotAs(OutputType.FILE);
        FileUtils.moveFile(tempFile,imgFile);
        return imgFile.getAbsolutePath();
        
    }
}
