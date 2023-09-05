package com.anhtester.projects.crm.models;

import lombok.Data;

@Data
public class SignInModel {

    public static int row;

    public static String testCaseName = "TESTCASENAME";

    public static String email = "EMAIL";

    public static String password = "PASSWORD";

    public static String expectedTitle = "EXPECTED_TITLE";

    public static String expectedError = "EXPECTED_ERROR";

    public static String expectedUrl = "EXPECTED_URL";

    public static int getRow() {
        return row;
    }

    public static String getTestCaseName() {
        return testCaseName;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    public static String getExpectedTitle() {
        return expectedTitle;
    }

    public static String getExpectedError() {
        return expectedError;
    }

    public static String getExpectedUrl() {
        return expectedUrl;
    }
}
