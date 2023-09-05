package com.anhtester.exceptions;

public class HeadlessNotSupportedException extends IllegalStateException {

    public HeadlessNotSupportedException(String browser) {
        super(String.format("Headless not supported for %s browser", browser));
    }
}
