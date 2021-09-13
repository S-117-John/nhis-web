package com.zebone.nhis.ma.pub.lb.vo;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class HighValueHRPVo {
	 /**

     * 返回码

     */
	@JSONField(name = "CODE")
    private String code;

    

    /**

     * 返回消息

     */
	@JSONField(name = "MESSAGE")
    private String message;

    

    /**

     * 返回数据集

     */
	@JSONField(name = "DATA")
    private List<RespHighValueDataVo> data;


	
	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public List<RespHighValueDataVo> getData() {
		return data;
	}



	public void setData(List<RespHighValueDataVo> data) {
		this.data = data;
	}

}
