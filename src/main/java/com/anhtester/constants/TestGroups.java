package com.anhtester.constants;

/**
 * Names of the TestNG groups. Run a group with: mvn test -DGROUPS=smoke (or env GROUPS, or GROUPS in config.properties).
 */
public final class TestGroups {

    /**
     * Critical path, quick check after a deployment
     */
    public static final String SMOKE = "smoke";

    /**
     * Full regression
     */
    public static final String REGRESSION = "regression";

    /**
     * Tests that change data shared by every run (ex: cart and orders of the demo customer account).
     * They must not run in two jobs at the same time: CI runs them in one job only (-DEXCLUDED_GROUPS=single-run elsewhere)
     */
    public static final String SINGLE_RUN = "single-run";

    private TestGroups() {
    }
}
