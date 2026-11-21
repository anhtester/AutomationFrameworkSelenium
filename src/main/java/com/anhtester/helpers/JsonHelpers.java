package com.anhtester.helpers;

import com.anhtester.utils.LogUtils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonHelpers {
    //Json Path
    private BufferedReader bufferedReader;
    private StringBuffer stringBuffer;
    private DocumentContext jsonContext;
    private String lines;
    private String jsonFilePathDefault = SystemHelpers.getCurrentDir() + "src/test/resources/datajson/store.json";

    public void setJsonFile(String jsonPath) {
        try {
            bufferedReader = new BufferedReader(new FileReader(SystemHelpers.getCurrentDir() + jsonPath));
            stringBuffer = new StringBuffer();
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }
            jsonContext = JsonPath.parse(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            LogUtils.error(e.getMessage(), e);
        } catch (IOException e) {
            LogUtils.error(e.getMessage(), e);
        }
    }

    public Object getData(String key) {
        //JsonPath.read(getJsonDataSourceString(), key);
        return jsonContext.read(key);
    }

}
