package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.NHISUUID;

public class ResponsePubVo {
	
	public ResponsePubVo(){
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
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setCreateTime(String createTime) {
         this.createTime = createTime;
     }
     public String getCreateTime() {
         return createTime;
     }

    public void setActionId(String actionId) {
         this.actionId = actionId;
     }
     public String getActionId() {
         return actionId;
     }

    public void setActionName(String actionName) {
         this.actionName = actionName;
     }
     public String getActionName() {
         return actionName;
     }

    public void setResult(Result result) {
         this.result = result;
     }
     public Result getResult() {
    	 if(result==null)result=new Result();
         return result;
     }

    public void setITSVersion(String iTSVersion) {
         this.iTSVersion = iTSVersion;
     }
     public String getITSVersion() {
         return iTSVersion;
     }
}
