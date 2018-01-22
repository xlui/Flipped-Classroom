package io.flippedclassroom.server.entity;

import io.flippedclassroom.server.config.Constant;

import java.io.Serializable;

public class JsonResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Constant.status status;
	private String message;

	public JsonResponse() {
		super();
	}

	public JsonResponse(Constant.status status, String message) {
		this.status = status;
		this.message = message;
	}

	public Constant.status getStatus() {
		return status;
	}

	public void setStatus(Constant.status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
