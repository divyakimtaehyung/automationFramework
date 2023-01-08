package com.iexceed.uiframework.utilites.readexcel;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class AdditionalExcelHandler {

    //Additional Handler - We can specify which column as Condition and added some useful methods
    public static List<String> getRowValue (String file, String testSheet, String colName, String rowName) throws IOException, IOException {
        FileInputStream fs = new FileInputStream(file);
        XSSFWorkbook ex = new XSSFWorkbook(fs);
        XSSFSheet xs;
        List<String> result = new ArrayList<String>();
        int noSheet = ex.getNumberOfSheets(),i=0,j=0;
        for (i=0;i<noSheet;i++){
           if(ex.getSheetName(i).equalsIgnoreCase(testSheet)){
                break;
            }
        }
        xs = ex.getSheetAt(i);
        Iterator<Row> rows = xs.iterator();
        Row firstRow = rows.next();
        Iterator<Cell> cells= firstRow.iterator();
        while (cells.hasNext()){
            if (cells.next().getStringCellValue().equalsIgnoreCase(colName)){
                break;
            }
            j++;
        }
        Iterator<Cell> reqCells = null;
        while (rows.hasNext()){
            Row reqRow = rows.next();
            if(reqRow.getCell(j).getStringCellValue().equalsIgnoreCase(rowName)){
                reqCells = reqRow.iterator();
                break;
            }
        }
        while (reqCells.hasNext()){
            Cell value = reqCells.next();
            if (value.getCellType() == CellType.STRING) {
                result.add(value.getStringCellValue());
            }else if(value.getCellType() == CellType.NUMERIC){
                result.add(String.valueOf(value.getNumericCellValue()));
            }
            else {
                reqCells.next();
            }
        }
        return result;
    }

    public static Map<String, String> getByColumnTestDataInMap(String testDataFile, String sheetName, String columnName , String testCaseId) throws Exception {
        Map<String, String> TestDataInMap = new TreeMap();
        String query = null;
        query = String.format("SELECT * FROM `%s` WHERE `%s`='%s'", sheetName,columnName, testCaseId);
        Fillo fillo = new Fillo();
        Connection conn = null;
        Recordset recordset = null;

        try {
            conn = fillo.getConnection(testDataFile);
            recordset = conn.executeQuery(query);

            while(recordset.next()) {
                Iterator var8 = recordset.getFieldNames().iterator();

                while(var8.hasNext()) {
                    String field = (String)var8.next();
                    TestDataInMap.put(field, recordset.getField(field));
                }
            }
        } catch (FilloException var10) {
            throw new Exception("Test data cannot find . . ."+var10);
        }

        conn.close();
        return TestDataInMap;
    }

    public static void UpdateTestDataToExcel(String testDataFilePath, String sheetName, String conditionColumn, String testCaseId, String updateColName, String updateDataValue) {
        Connection conn = null;
        Fillo fillo = new Fillo();

        try {
            conn = fillo.getConnection(testDataFilePath);
            String query = String.format("UPDATE %s SET `" + updateColName + "`='%s' where `"+conditionColumn+"`='%s'", sheetName, updateDataValue, testCaseId);
            conn.executeUpdate(query);
        } catch (FilloException var8) {
           Assert.fail("Query not updated "+var8);
        }

        conn.close();
    }

    public static List<Map<String, String>> getAllData(String testDataFile, String sheetName) throws Exception {
        String query = null;
        query = String.format("SELECT * FROM `%s`", sheetName);
        Fillo fillo = new Fillo();
        Connection conn = null;
        Recordset recordset = null;
        List<Map<String,String>> allData = new ArrayList<>();

        try {
            conn = fillo.getConnection(testDataFile);
            recordset = conn.executeQuery(query);

            while(recordset.next()) {
                Map<String, String> TestDataInMap = new TreeMap<>();
                Iterator var8 = recordset.getFieldNames().iterator();

                while(var8.hasNext()) {
                    String field = (String)var8.next();
                    TestDataInMap.put(field, recordset.getField(field));
                }

                allData.add(TestDataInMap);
            }
        } catch (FilloException var10) {
            throw new Exception("Test data cannot find . . ."+var10);
        }

        conn.close();
        return allData;
    }

    public static void main(String[] args) throws Exception {
        List<Map<String,String>> alllist;
        alllist = getAllData("src/test/resources/DataSet/CorporateDetails.xlsx","Applications");
        System.out.println(alllist.get(0).get("ApplicationName"));
    }


    public static void UpdateAllRowOnColumn(String testDataFilePath, String sheetName, String updateColName, String updateDataValue) {
        Connection conn = null;
        Fillo fillo = new Fillo();

        try {
            conn = fillo.getConnection(testDataFilePath);
            String query = String.format("UPDATE %s SET `" + updateColName + "`='%s'", sheetName, updateDataValue);
            conn.executeUpdate(query);
        } catch (FilloException var8) {
            Assert.fail("Query not updated "+var8);
        }

        conn.close();
    }
}
