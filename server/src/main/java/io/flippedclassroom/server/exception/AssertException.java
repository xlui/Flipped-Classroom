package io.flippedclassroom.server.exception;

public class AssertException extends Exception {
	public AssertException() {
		super();
	}

	public AssertException(String s) {
		super(s);
	}

	public AssertException(String message, Throwable cause) {
		super(message, cause);
	}
}
