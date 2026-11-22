package com.anhtester.helpers;

import com.anhtester.constants.FrameworkConstants;

import java.util.Locale;

/**
 * VIDEO_RECORD in config.properties (or -DVIDEO_RECORD):
 * <ul>
 *     <li>no: no video</li>
 *     <li>yes: keep the video of every test</li>
 *     <li>failed: record every test, keep only the videos of failed (and retried) tests</li>
 * </ul>
 */
public enum VideoMode {
    NO, YES, FAILED;

    public static VideoMode current() {
        String value = String.valueOf(FrameworkConstants.VIDEO_RECORD).trim().toLowerCase(Locale.ROOT);
        switch (value) {
            case "yes":
            case "true":
            case "all":
                return YES;
            case "failed":
            case "fail":
            case "on-failure":
                return FAILED;
            default:
                return NO;
        }
    }

    /**
     * @param passed true when the test passed
     * @return true when the video of this test must be saved
     */
    public static boolean keep(boolean passed) {
        VideoMode mode = current();
        return mode == YES || (mode == FAILED && !passed);
    }
}
