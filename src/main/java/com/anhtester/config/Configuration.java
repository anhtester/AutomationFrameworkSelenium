package com.anhtester.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:properties",
        "system:env",
        "file:./src/test/resources/config/config.properties",
        "file:./src/test/resources/config/data.properties"})

public interface Configuration extends Config {

    @Key("TARGET")
    String TARGET();

    @Key("BROWSER")
    String BROWSER();

    @Key("HEADLESS")
    Boolean HEADLESS();

    @Key("URL_CRM")
    String URL_CRM();

    @Key("REMOTE_URL")
    String REMOTE_URL();

    @Key("REMOTE_PORT")
    String REMOTE_PORT();

    @Key("EXCEL_DATA_FILE_PATH")
    String EXCEL_DATA_FILE_PATH();

}
