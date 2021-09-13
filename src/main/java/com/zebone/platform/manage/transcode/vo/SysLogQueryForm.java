package com.zebone.platform.manage.transcode.vo;

/**
 * 系统日志查询条件对象
 * 
 * @author Xulj
 *
 */
public class SysLogQueryForm {

	private String transcode;

	private String type;
	
	private String begintime;
	
	private String endtime;

	public String getTranscode() {
		return transcode;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
}
