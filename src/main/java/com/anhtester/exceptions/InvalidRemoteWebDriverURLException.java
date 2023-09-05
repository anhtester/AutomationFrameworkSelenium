package com.anhtester.exceptions;

@SuppressWarnings("serial")
public class InvalidRemoteWebDriverURLException extends FrameworkException {

	public InvalidRemoteWebDriverURLException(String message) {
		super(message);
	}

	public InvalidRemoteWebDriverURLException(String message, Throwable cause) {
		super(message, cause);
	}

}
