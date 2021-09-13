package com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo;

import java.util.List;
import java.util.Map;

/**
 * 日对账返回数据
 * @author zrj
 *
 */
public class DayReconciliationData {

	private List<Map<String, Object>> mapList; //日对账数据

	private String drStatus; //对账结果  0：失败，1：成功
	
	private int errorNum; //失败数量
	
	private String message; //返回信息

	public List<Map<String, Object>> getMapList() {
		return mapList;
	}

	public void setMapList(List<Map<String, Object>> mapList) {
		this.mapList = mapList;
	}

	public String getDrStatus() {
		return drStatus;
	}

	public void setDrStatus(String drStatus) {
		this.drStatus = drStatus;
	}

	public int getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(int errorNum) {
		this.errorNum = errorNum;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
