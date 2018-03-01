package io.flippedclassroom.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "JsonResponse", description = "普通API的返回信息都是通过该类进行构建的")
public class JsonResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "操作结果，为成功或者失败", example = "SUCCESS/FAILED，请根据Status来判断操作结果")
	private String status;
	@ApiModelProperty(value = "操作结果附带的说明信息，供调试使用", example = "操作成功/失败! 附带信息，方便调试")
	private String message;

	public JsonResponse() {
		super();
	}

	public JsonResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
