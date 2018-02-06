package io.flippedclassroom.server.exception;

/**
 * 文件存储位置不合法
 */
public class PositionInvalidException extends Exception {
	public PositionInvalidException() {
		super();
	}

	public PositionInvalidException(String s) {
		super(s);
	}
}
