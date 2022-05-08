/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//final -> We do not want any class to extend this class
public final class DateUtils {

    //private -> We do not want anyone to create the object of this class
    //Private constructor to avoid external instantiation
    private DateUtils() {
    }

    /**
     * @return lấy ra ngày tháng năm hiện tại của máy theo cấu trúc mặc định
     */
    public static String getCurrentDate() {
        Date date = new Date();
        return date.toString().replace(":", "_").replace(" ", "_");
    }

    /**
     * @return lấy ra ngày tháng năm và giờ phút giây hiện tại của máy theo cấu trúc dd/MM/yyyy HH:mm:ss
     */
    public static String getCurrentDateTime() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        System.out.println(formatter.format(now));
//        String Timestamp = now.toString().replace(":", "-");
        return formatter.format(now);
    }

}
