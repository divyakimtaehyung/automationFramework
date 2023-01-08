package com.iexceed.uiframework.utilites.readexcel;

import java.util.Map;
import java.util.TreeMap;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExcelHandler
{
    private static Logger LOGGER = LogManager.getLogger(ExcelHandler.class);
    public static Map<String,String> getTestDataInMap(String testDataFile,String sheetName,String testCaseId) throws Exception
    {
        Map<String,String> TestDataInMap=new TreeMap<String,String>();
        String query=null;
        query=String.format("SELECT * FROM %s WHERE Run='Yes' and TestCaseId='%s'",
                sheetName,testCaseId);
        Fillo fillo=new Fillo();
        Connection conn=null;
        Recordset recordset=null;
        try
        {
            conn=fillo.getConnection(testDataFile);
            recordset=conn.executeQuery(query);
            while(recordset.next())
            {
                for(String field:recordset.getFieldNames())
                {
                    TestDataInMap.put(field, recordset.getField(field));
                }
            }
        }
        catch(FilloException e)
        {

            throw new Exception("Test data cannot find . . .");
        }
        conn.close();
        return TestDataInMap;
    }

    public static void UpdateTestDataToExcel(String testDataFilePath,String sheetName,String colName,String tcStatus,String testCaseId)
    {
        Connection conn=null;
        Fillo fillo =new Fillo();
        try{
            conn=fillo.getConnection(testDataFilePath);
            String query=String.format("UPDATE %s SET "+ colName +"='%s' where TestCaseID='%s'", sheetName,tcStatus,testCaseId);
            // String query=String.format("UPDATE %s SET TestCaseStatus='%s' where TestCaseID='%s'", sheetName,tcStatus,testCaseId);
            conn.executeUpdate(query);
        } catch(FilloException e){
            LOGGER.info("Query not updated");
        }
        conn.close();
    }

}