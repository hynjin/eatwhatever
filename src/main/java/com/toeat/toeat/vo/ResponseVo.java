package com.toeat.toeat.vo;

public class ResponseVo {
	public String code;
	public String body;
	
	public ResponseVo() {
		code = null;
		body = null;
	}
	
	public ResponseVo(String code, String body) {
		this.code = code;
		this.body = body;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	
	
}


