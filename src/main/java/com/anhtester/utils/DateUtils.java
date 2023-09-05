/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//final -> We do not want any class to extend this class
public final class DateUtils {

    private DateUtils() {
        super();
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
        return formatter.format(now);
    }

    public static String getCurrentDateTimeCustom(String separator_Character) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String timeStamp = formatter.format(now).replace("/", separator_Character);
        timeStamp = timeStamp.replace(" ", separator_Character);
        timeStamp = timeStamp.replace(":", separator_Character);
        return timeStamp;
    }

}
