package com.anhtester.listeners;

import com.anhtester.helpers.PropertiesHelpers;
import com.anhtester.utils.LogUtils;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adjust every suite before it runs, from config.properties / -D / environment variables:
 * <ul>
 *     <li>GROUPS: run only these TestNG groups, ex: mvn test -DGROUPS=smoke (comma separated)</li>
 *     <li>EXCLUDED_GROUPS: do not run these groups</li>
 *     <li>THREAD_COUNT: number of threads for the parallel tests of the suites (parallel="methods"...)</li>
 * </ul>
 * Registered for every run in src/test/resources/META-INF/services/org.testng.ITestNGListener
 * (suite XML, IDE, Maven). Empty values keep what the suite XML defines.
 */
public class SuiteConfigurator implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {
        List<String> groups = split(PropertiesHelpers.getValue("GROUPS", ""));
        List<String> excludedGroups = split(PropertiesHelpers.getValue("EXCLUDED_GROUPS", ""));
        int threadCount = parseThreadCount(PropertiesHelpers.getValue("THREAD_COUNT", ""));

        for (XmlSuite suite : suites) {
            alterSuite(suite, groups, excludedGroups, threadCount);
        }
    }

    private void alterSuite(XmlSuite suite, List<String> groups, List<String> excludedGroups, int threadCount) {
        if (threadCount > 0) {
            suite.setThreadCount(threadCount);
            suite.setDataProviderThreadCount(threadCount);
        }
        for (XmlTest test : suite.getTests()) {
            groups.forEach(test::addIncludedGroup);
            excludedGroups.forEach(test::addExcludedGroup);
            if (threadCount > 0) {
                test.setThreadCount(threadCount);
            }
        }
        if (!groups.isEmpty() || !excludedGroups.isEmpty() || threadCount > 0) {
            LogUtils.info(String.format("Suite '%s': groups=%s, excluded groups=%s, thread count=%s",
                    suite.getName(), groups.isEmpty() ? "all" : groups, excludedGroups.isEmpty() ? "none" : excludedGroups,
                    threadCount > 0 ? threadCount : "from suite XML"));
        }
        // Child suites (<suite-files>) are altered too
        for (XmlSuite child : suite.getChildSuites()) {
            alterSuite(child, groups, excludedGroups, threadCount);
        }
    }

    private static List<String> split(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.toList());
    }

    private static int parseThreadCount(String value) {
        if (value.isEmpty()) {
            return 0;
        }
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException e) {
            LogUtils.warn("Invalid THREAD_COUNT='" + value + "', the thread count of the suite XML is used.");
            return 0;
        }
    }
}
