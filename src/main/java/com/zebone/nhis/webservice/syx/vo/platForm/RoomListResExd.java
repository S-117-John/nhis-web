package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class RoomListResExd {

	@XmlElement(name = "id")
    private String id;

    @XmlElement(name = "createTime")
    private String createTime;

    @XmlElement(name = "actionId")
    private String actionId;

    @XmlElement(name = "actionName")
    private String actionName;
	
    @XmlElement(name="result")
    private RoomListResResult result;
    
    @XmlElement(name="subject")
    private RoomListResSubject subject;

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

	public RoomListResResult getResult() {
		return result;
	}

	public void setResult(RoomListResResult result) {
		this.result = result;
	}

	public RoomListResSubject getSubject() {
		return subject;
	}

	public void setSubject(RoomListResSubject subject) {
		this.subject = subject;
	}

    
}
