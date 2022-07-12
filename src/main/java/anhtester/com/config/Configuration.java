package anhtester.com.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config.properties",
        "classpath:datatest.properties",
})

public interface Configuration extends Config {

    @Key("TARGET")
    String target();

    @Key("BROWSER")
    String browser();

    @Key("HEADLESS")
    Boolean headless();

    @Key("URL_CRM")
    String baseUrl();

    @Key("REMOTE_URL")
    String gridUrl();

    @Key("REMOTE_PORT")
    String gridPort();

}
