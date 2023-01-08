package com.iexceed.uiframework.utilites;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RandomDataGenerator {

    public static void main(String[] args) {
        RandomDataGenerator rg = new RandomDataGenerator();
//        String invalidEmail = rg.getLowerCase(5)+"@"+ rg.getLowerCase(4)+"."+rg.getLowerCase(3);
        System.out.println("random "+rg.getNumber(30));
    }

    public String getTimeStamp() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        return myDateObj.format(myFormatObj);
    }

    public String getString(int count) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+ "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public String getUpperCase(int count){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public String getLowerCase(int count){
        String AlphaNumericString = "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public String getSpecialCharacters(int count){
        String AlphaNumericString = "@#$&!^&=-/?+_";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }


    public String getNumber(int count) {
        String number = "1234567891";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = (int)(number.length() * Math.random());
            sb.append(number.charAt(index));
        }
        return sb.toString();
    }

    public String getAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+ "0123456789"+ "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public String getStringWithSpecialCharacters(int n)
    {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
        String specialChars =  "@#$%^&*(){}|:/.,:?><+_";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n/2; i++) {
            int index = (int)(specialChars.length() * Math.random());
            sb.append(specialChars.charAt(index));
            sb.append(letters.charAt(index*2));
        }
        if (n%2 ==1) {
            sb.append(letters.charAt(0));
        }
        return sb.toString();
    }

    public String getOneUpperCaseOneSpecialCharOneLowerCaseOneNumber(int count){
        String upperCase =   "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase =   "abcdefghijklmnopqrstuvxyz";
        String specialChars ="@#$&!^&=-/?+_";
        String number = "1234567890123";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count/4; i++) {
            int multiplier = (int) ((Math.random() * 2) + 1);
            int index = (int)(number.length() * Math.random());
            sb.append(upperCase.charAt(index*multiplier));
            sb.append(specialChars.charAt(index));
            sb.append(lowerCase.charAt(index*multiplier));
            sb.append(number.charAt(index));
        }
        if (count%4 == 1) {
            sb.append(upperCase.charAt(1));
        } else if (count%4 == 2) {
            int index = (int) ((Math.random() * 2) + 1);
            sb.append(lowerCase.charAt(index));
            sb.append(upperCase.charAt(index));
        }else if (count%4 ==  3){
            int index = (int) ((Math.random() * 3) + 1);
            sb.append(specialChars.charAt(index));
            sb.append(lowerCase.charAt(index));
            sb.append(number.charAt(index));
        }else {
            System.out.println(sb);
        }
        return sb.toString();
    }






}
