package anhtester.com.projects.website.crm.testcases;

import anhtester.com.common.BaseWeb;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TestQRCode extends BaseWeb {

    WebDriver driver = null;

    @BeforeClass
    public void setupBrowser() {
        driver = new BaseWeb().createBrowser("chrome");
    }

    @Test
    public void QRCode() throws NotFoundException, IOException {
        driver.get("http://qrcode.meetheed.com/qrcode_examples.php");
        driver.manage().window().maximize();
        String qrCodeURL = driver.findElement(By.xpath("//img[@src='images/qr_code_con.png']")).getAttribute("src");
        //Create an object of URL Class
        URL url = new URL(qrCodeURL);
        //Pass the URL class object to store the file as image
        BufferedImage bufferedimage = ImageIO.read(url);
        // Process the image
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedimage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        //To Capture details of QR code
        Result result = new MultiFormatReader().decode(binaryBitmap);
        System.out.println(result.getText());
    }
}
