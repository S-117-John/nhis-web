package com.zebone.nhis.pro.zsba.common.support;

import net.sf.json.JSONObject;


public class JsonResult {

	private Boolean state;		// 状态
	private Integer code;		// 状态码
	private String message;		// 消息
	private Object result;		// 数据对象

	/**
	 * 无参构造器
	 */
	public JsonResult() {
		super();
	}

	/**
	 * 数据对象
	 * @param data
	 */
	public JsonResult(Object result) {
		super();
		this.result = result;
	}
	
	/*
	 * 成功
	 */
	public JsonResult success(){
		this.state = true;
		this.code = SUCCESS;
		this.message = "操作成功";
		return this;
	}
	public JsonResult success(String message){
		this.state = true;
		this.code = SUCCESS;
		this.message = message;
		return this;
	}
	
	/*
	 * 失败
	 */
	public JsonResult fail(Integer code){
		this.state = false;
		this.code = code;
		this.message = "操作失败";
		return this;
	}
	public JsonResult fail(Integer code, String message){
		this.state = false;
		this.code = code;
		this.message = message;
		return this;
	}
	
	/**
	 * 直接对数据转换(外层为单对象)
	 * @param data		源数据
	 * @return String  	转换后的json格式字符串
	 * @author lpz
	 * @date 2018-11-5
	 */
	public static String toJsonObject(JsonResult data){
		if(data.result==null){
			data.result = "";
		}
		return JSONObject.fromObject(data).toString();
	}
	
	
	
	
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}


	public static Integer SUCCESS = 200;		// 成功
	public static Integer SYSTEM_ERROR = 9999;	// 系统异常
	public static Integer PARAMS_ERROR = 100;	// 请求参数无效
	public static Integer NOT_EXIST = 204;		// 对象不存在
	public static Integer NOT_ALLOWED = 405;	//不允许的方法 
	
	
	
}