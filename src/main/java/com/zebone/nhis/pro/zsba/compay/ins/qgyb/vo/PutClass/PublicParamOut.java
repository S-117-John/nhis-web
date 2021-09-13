package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.PutClass;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 9001签到出参
 * @author Administrator
 *
 */
public class PublicParamOut{
	
	private String	infcode	;//	Y	交易状态码	详见下节 0：成功 -1：失败
	private String	inf_refmsgid	;//	Y	接收方报文ID	接收方返回，接收方医保区划代码(6)+时间(14)+流水号(10)
	private String	refmsg_time	;//		接收报文时间	时间格式：yyyyMMddHHmmss
	private String	respond_time	;//		响应报文时间	格式：yyyyMMddHHmmssSSS
	private String	err_msg;//		错误信息	格式：yyyyMMddHHmmssSSS
	private String code;//
	private String message;//
	private String msgid;//报文id  医保没返回这个，这个是为了业务能获取到报文id保存而加的
	private String fileAddr; //文件在本地服务器的地址，下载文件用的
	
	public String getInf_refmsgid() {
		return inf_refmsgid;
	}
	public void setInf_refmsgid(String inf_refmsgid) {
		this.inf_refmsgid = inf_refmsgid;
	}
	public String getRefmsg_time() {
		return refmsg_time;
	}
	public void setRefmsg_time(String refmsg_time) {
		this.refmsg_time = refmsg_time;
	}
	public String getRespond_time() {
		return respond_time;
	}
	public void setRespond_time(String respond_time) {
		this.respond_time = respond_time;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public String getInfcode() {
		return infcode;
	}
	public void setInfcode(String infcode) {
		this.infcode = infcode;
	}
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
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getFileAddr() {
		return fileAddr;
	}
	public void setFileAddr(String fileAddr) {
		this.fileAddr = fileAddr;
	}
	
	
}
