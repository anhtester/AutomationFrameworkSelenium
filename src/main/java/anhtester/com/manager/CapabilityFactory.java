package anhtester.com.manager;

import org.openqa.selenium.Capabilities;

public class CapabilityFactory {
    public Capabilities capabilities;

    public Capabilities getCapabilities(String browser) {

        switch (browser.toLowerCase().trim()) {
            case "chrome":
                capabilities = OptionsManager.getChromeOptions();
                break;
            case "firefox":
                capabilities = OptionsManager.getFirefoxOptions();
                break;
            default:
                capabilities = OptionsManager.getChromeOptions();
                break;
        }
        return capabilities;
    }
}
