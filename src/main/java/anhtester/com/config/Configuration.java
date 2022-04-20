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

    @Key("target")
    String target();

    @Key("browser")
    String browser();

    @Key("headless")
    Boolean headless();

    @Key("base_url")
    String baseUrl();

    @Key("remote_url")
    String gridUrl();

    @Key("remote_port")
    String gridPort();

}
