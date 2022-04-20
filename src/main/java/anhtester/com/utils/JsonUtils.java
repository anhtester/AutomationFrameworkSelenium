/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package anhtester.com.utils;

import anhtester.com.constants.FrameworkConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * To construct the map by the reading the config values from JSON. Not used in this framework but can be leveraged
 * instead of property file based on the requirements
 */
public class JsonUtils {


    private static Map<String, String> CONFIGMAP;

    private JsonUtils() {
    }

    //Được sử dụng để khởi tạo thành viên dữ liệu static. (CONFIGMAP)
    //Nó được thực thi trước phương thức main tại lúc tải lớp.
    static {
        try {
            CONFIGMAP = new ObjectMapper().readValue(new File(FrameworkConstants.JSON_CONFIG_FILE_PATH),
                    new TypeReference<HashMap<String, String>>() {
                    });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        if (Objects.isNull(key) || Objects.isNull(CONFIGMAP.get(key.toLowerCase()))) {
            try {
                throw new Exception("Key name " + key + " is not found. Please check config.json");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return CONFIGMAP.get(key.toLowerCase());
    }

}
