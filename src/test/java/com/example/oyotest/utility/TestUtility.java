package com.example.oyotest.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtility {
    public static Date changeStrToDate(String inpDateStr) throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdformat.parse(inpDateStr);
    }
}
