package anhtester.com.config;

import anhtester.com.helpers.PropertiesHelpers;

public class Constants {
    public static final int IMPLICIT_WAIT = Integer.parseInt(PropertiesHelpers.getValue("IMPLICIT_WAIT"));
    public static final int PAGE_LOAD_TIMEOUT  = Integer.parseInt(PropertiesHelpers.getValue("PAGE_LOAD_TIMEOUT"));
    public static final int DEFAULT_WAIT = Integer.parseInt(PropertiesHelpers.getValue("DEFAULT_WAIT"));
}
