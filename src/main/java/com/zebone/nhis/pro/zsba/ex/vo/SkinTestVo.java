package com.zebone.nhis.pro.zsba.ex.vo;

import java.util.Date;

public class SkinTestVo {
	/**
	 * 皮试名称
	 */
	public String name;
    /**
     * 皮试结果
     */
	public String result;
	/**
	 * 皮试时间
	 */
	public Date DateBeginSt;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Date getDateBeginSt() {
		return DateBeginSt;
	}
	public void setDateBeginSt(Date dateBeginSt) {
		DateBeginSt = dateBeginSt;
	}
	@Override
	public String toString() {
		return "SkinTestVo [name=" + name + ", result=" + result
				+ ", DateBeginSt=" + DateBeginSt + "]";
	}		

}
