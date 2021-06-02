package com.maxxipoint.javatest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyClass {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String inputString = "00:01:50";
        Date date = null;
        try {
            date = sdf.parse("1970-01-01 " + inputString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("in milliseconds: " + date.getTime()/1000);
    }
}
