package io.flippedclassroom.server.exception;

public class Http400BadRequestException extends Exception {
	public Http400BadRequestException() {
		super();
	}

	public Http400BadRequestException(String message) {
		super(message);
	}

	public Http400BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
