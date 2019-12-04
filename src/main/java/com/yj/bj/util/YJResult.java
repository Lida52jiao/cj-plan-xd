package com.yj.bj.util;


import java.io.Serializable;

/**
 * 自定义响应信息
 */
public class YJResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7109353265833616747L;

	// 响应业务状态
	private String respCode;

	// 响应消息
	private String respDesc;

	// 响应中的数据
	private Object data;

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static YJResult build(String respCode, String respDesc, Object data) {
		return new YJResult(respCode, respDesc, data);
	}

	public static YJResult build(String respCode, String respDesc) {
		return new YJResult(respCode, respDesc);
	}
	public static YJResult ok(Object data) {
		return new YJResult(data);
	}

	public static YJResult ok() {
		return new YJResult(null);
	}

	public YJResult(String respCode, String respDesc) {
		this.respCode = respCode;
		this.respDesc = respDesc;
	}
	

	public YJResult(String respCode, String respDesc, Object data) {
		this.respCode = respCode;
		this.respDesc = respDesc;
		this.data = data;
	}
	public YJResult(Object data) {
		this.respCode = "0000";
		this.respDesc = "success";
		this.data = data;
	}
}
