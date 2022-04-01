package anhtester.com.utils;

import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ObjectRepository {

    private Properties prop;

    public ObjectRepository(String objectFilePath) {
        prop = new Properties();

        try {
            FileInputStream fis = new FileInputStream(objectFilePath);
            try {
                prop.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fis.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public By getLocator(String elementName) {

        // retrieve the specified object from the object list in properties file
        String locator = prop.getProperty(elementName);

        // extract the locator type and value from the object
        String locatorType = locator.split(":")[0];
        String locatorValue = locator.split(":")[1];

        Log.info("Retrieving object of type '" + locatorType + "' and value '" + locatorValue + "' from the object repository");

        // Trả về một thể hiện của lớp By dựa trên loại định vị (id, name, xpath, css,...)
        // Đối tượng By có thể được sử dụng bởi driver.findElement (WebElement)
        if (locatorType.toLowerCase().equals("id"))
            return By.id(locatorValue);
        else if (locatorType.toLowerCase().equals("name"))
            return By.name(locatorValue);
        else if (locatorType.toLowerCase().equals("xpath"))
            return By.xpath(locatorValue);
        else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
            return By.cssSelector(locatorValue);
        else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
            return By.className(locatorValue);
        else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
            return By.tagName(locatorValue);
        else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
            return By.linkText(locatorValue);
        else if ((locatorType.toLowerCase().equals("partiallinktext")) || (locatorType.toLowerCase().equals("partial")))
            return By.partialLinkText(locatorValue);
        else
            try {
                throw new Exception("Unknown locator type '" + locatorType + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }
}
