/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SystemHelpers {

    private SystemHelpers() {
    }

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String makeSlug(String input) {
        if (input == null)
            throw new IllegalArgumentException();

        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("_");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    /**
     * @return Get the path to your source directory with a / at the end
     */
    public static String getCurrentDir() {
        String current = System.getProperty("user.dir") + File.separator;
        return current;
    }

    /**
     * Create a folder if it does not exist
     *
     * @param path is the path to create the folder
     */
    public static void createFolder(String path) {
        File folder = new File(path);

        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Folder created: " + folder.getAbsolutePath());
            } else {
                System.out.println("Failed to create folder: " + folder.getAbsolutePath());
            }
        } else {
            System.out.println("Folder already exists: " + folder.getAbsolutePath());
        }
    }

    /**
     * @param str        is value as type is String to be split based on condition
     * @param valueSplit the character to split the string into an array of values
     * @return array of string values after splitting
     */
    public static ArrayList<String> splitString(String str, String valueSplit) {
        ArrayList<String> arrayListString = new ArrayList<>();
        for (String s : str.split(valueSplit, 0)) {
            arrayListString.add(s);
        }
        return arrayListString;
    }

    public static String removeAccent(String text) {
        if (text == null) return null;
        // Chuẩn hóa thành dạng tổ hợp Unicode (NFD)
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        // Loại bỏ các dấu thanh, móc, mũ...
        String withoutDiacritics = normalized.replaceAll("\\p{M}", "");
        // Xử lý riêng cho đ, Đ
        withoutDiacritics = withoutDiacritics.replace("đ", "d").replace("Đ", "D");

        return withoutDiacritics;
    }

}