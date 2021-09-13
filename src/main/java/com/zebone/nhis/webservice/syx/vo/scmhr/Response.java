package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.NHISUUID;

/**
 * 平台规范返回vo
 * @author jd
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="return")
public class Response {
	public Response(){
		this.setId(NHISUUID.getKeyId());
		this.setCreateTime(DateUtils.dateToStr("yyyyMMddHHmmss", new Date()));
	}
	@XmlElement(name="id")
	private String id;
	@XmlElement(name="createTime")
    private String createTime;
	@XmlElement(name="actionId")
    private String actionId;
	@XmlElement(name="actionName")
    private String actionName;
	@XmlElement(name="result")
    private Result result;
	@XmlElement(name="ITSVersion")
    private String iTSVersion;
	@XmlElement(name="subject")
	private List<ResSubject> resSubject;
	@XmlElement(name="subject")
	private ResSubject subject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Result getResult() {
		if(result==null)result=new Result();
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getiTSVersion() {
		return iTSVersion;
	}

	public void setiTSVersion(String iTSVersion) {
		this.iTSVersion = iTSVersion;
	}

	public List<ResSubject> getResSubject() {
		if(this.resSubject==null)resSubject=new ArrayList<ResSubject>();
		return resSubject;
	}

	public void setResSubject(List<ResSubject> resSubject) {
		this.resSubject = resSubject;
	}

	public ResSubject getSubject() {
		if(subject==null)subject=new ResSubject();
		return subject;
	}

	public void setSubject(ResSubject subject) {
		this.subject = subject;
	}
}
