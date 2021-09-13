package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class SetQueueListReqSubject {

	/**
     * 消息ID 32位UUID
     */
    @XmlElement(name = "id")
    private String id;

    /**
     * 时间戳
     */
    @XmlElement(name = "createTime")
    private String createTime;

    /**
     * funid
     */
    @XmlElement(name = "actionId")
    private String actionId;

    /**
     * 方法名称
     */
    @XmlElement(name = "actionName")
    private String actionName;

    /**
     * 请求方信息
     */
    @XmlElement(name = "sender")
    private PlatFormReqSender sender;
	
	@XmlElementWrapper(name="itemList")
    @XmlElement(name="item")
	private List<SetQueueListReq> item;

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

	public PlatFormReqSender getSender() {
		return sender;
	}

	public void setSender(PlatFormReqSender sender) {
		this.sender = sender;
	}

	public List<SetQueueListReq> getItem() {
		return item;
	}

	public void setItem(List<SetQueueListReq> item) {
		this.item = item;
	}

	
}
