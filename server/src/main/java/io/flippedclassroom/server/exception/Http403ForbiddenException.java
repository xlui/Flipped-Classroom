package io.flippedclassroom.server.exception;

public class Http403ForbiddenException extends Exception {
	public Http403ForbiddenException() {
		super();
	}

	public Http403ForbiddenException(String message) {
		super(message);
	}

	public Http403ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}
}
