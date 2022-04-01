package anhtester.com.projects.website.crm.pages.Projects;

import anhtester.com.driver.DriverManager;
import anhtester.com.utils.WebUI;
import anhtester.com.projects.website.crm.pages.CommonPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class ProjectPage extends CommonPage {

    public ProjectPage() {
    }

    private String pageText = "Projects";

    //Project Page Element
    private By projectMenu = By.xpath("//span[normalize-space()='Projects']");
    private By searchInput = By.xpath("//input[@type='search']");

    public void searchByValue(String value) {
        webUI.clearText(searchInput);
        webUI.setText(searchInput, value);
        webUI.sleep(1);
    }

    public void checkContainsSearchTableByColumn(int column, String value) {
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
        webUI.sleep(1);
        System.out.println("");
        System.out.println("Số kết quả cho từ khóa (" + value + "): " + totalRows.size());

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));
            res = title.getText().toUpperCase().contains(value.toUpperCase());
            System.out.println("Dòng thứ " + i + ": " + res + " - " + title.getText());
            Assert.assertTrue(res, "Dòng thứ " + i + " (" + title.getText() + ")" + " không chứa giá trị " + value);
        }
    }

    public void displayValueTableByColumn(int column) {
        List<WebElement> totalRows = DriverManager.getDriver().findElements(By.xpath("//tbody/tr"));
        webUI.sleep(1);
        System.out.println("");
        System.out.println("Số kết quả cho cột (" + column + "): " + totalRows.size());

        for (int i = 1; i <= totalRows.size(); i++) {
            boolean res = false;
            WebElement title = DriverManager.getDriver().findElement(By.xpath("//tbody/tr[" + i + "]/td[" + column + "]"));

            System.out.println("Dòng thứ " + i + ":" + title.getText());
        }
    }
}
