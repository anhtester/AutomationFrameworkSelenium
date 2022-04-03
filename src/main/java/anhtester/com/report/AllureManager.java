package anhtester.com.report;

import anhtester.com.config.ConfigFactory;
import anhtester.com.constants.FrameworkConstants;
import anhtester.com.driver.DriverManager;
import anhtester.com.enums.Browser;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static anhtester.com.config.ConfigFactory.getConfig;

public class AllureManager {

    public AllureManager() {
    }

    public static void setAllureEnvironmentInformation() {
        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder().
                        put("Test URL", FrameworkConstants.BASE_URL).
                        put("Target Execution", FrameworkConstants.TARGET).
                        put("Global Timeout", String.valueOf(FrameworkConstants.WAIT_DEFAULT)).
                        put("Headless Dode", FrameworkConstants.HEADLESS).
                        put("Local Browser", String.valueOf(Browser.CHROME)).
                        put("Remote URL", FrameworkConstants.REMOTE_URL).
                        put("Remote Port", FrameworkConstants.REMOTE_PORT).
                        build());
    }

    @Attachment(value = "Failed test screenshot", type = "image/png")
    public static byte[] takeScreenshotToAttachOnAllureReport() {
        System.out.println(((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES));
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Browser information", type = "text/plain")
    public static String addBrowserInformationOnAllureReport() {
        return DriverManager.getInfo();
    }

    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    //HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    //Text attachments for Allure
//    @Attachment(value = "Page screenshot", type = "image/png")
//    public static byte[] saveScreenshotPNG() {
//        File file = new File("Screenshots");
//        BufferedImage bufferedImage = null;
//        try {
//            bufferedImage = ImageIO.read(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        byte[] image = null;
//        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//            ImageIO.write(bufferedImage, "png", bos);
//            image = bos.toByteArray();
//        } catch (Exception e) { }
//
//        // if decoding is not necessary just return image
//        return image != null ? Base64.getMimeDecoder().decode(image) : null;
//    }

}
