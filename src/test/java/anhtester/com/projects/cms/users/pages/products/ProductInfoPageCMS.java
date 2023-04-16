package anhtester.com.projects.cms.users.pages.products;

import anhtester.com.projects.cms.CommonPageCMS;
import anhtester.com.driver.DriverManager;
import anhtester.com.keywords.WebUI;
import anhtester.com.projects.cms.users.pages.dashboard.DashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoPageCMS extends CommonPageCMS {
    private By productName = By.xpath("//h1");
    public static By productPrice = By.xpath("(//div[text()='Discount Price:']/parent::div)/following-sibling::div//strong");
    private By productUnit = By.xpath("//span[@class='opacity-70']");
    private By productDescription = By.xpath("//div[@class = 'mw-100 overflow-auto text-left aiz-editor-data']/p");
    private By selectProductName = By.xpath("(//div[contains(@class,'product-name')])[1]");

    public ArrayList productInfo(String product) {
        WebUI.waitForPageLoaded();
        WebUI.setText(DashboardPage.inputSearchProduct, product);
        WebUI.clickElement(selectProductName);
        WebUI.waitForPageLoaded();
        String name = WebUI.getTextElement(By.xpath("//h1[contains(.,'" + product + "')]"));
        String price = WebUI.getTextElement(productPrice);
        String unit = WebUI.getTextElement(productUnit);
        String unitProduct = unit.substring(1);
        String description = WebUI.getTextElement(productDescription);
        List<String> arrayList = new ArrayList<String>();
        arrayList.add(name);
        arrayList.add(price);
        arrayList.add(unitProduct);
        arrayList.add(description);
        System.out.println("Array" + arrayList);
        System.out.println("Array" + arrayList.get(0));
        return (ArrayList) arrayList;
    }
}
