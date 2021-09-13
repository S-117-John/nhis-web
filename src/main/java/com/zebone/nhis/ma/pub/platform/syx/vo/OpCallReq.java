package com.zebone.nhis.ma.pub.platform.syx.vo;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class OpCallReq {

    /**
     * 消息ID 32位UUID
     */
    @XmlElement(name = "id")
    private String id;

    /**
     * 时间戳
     */
    @XmlElement(name = "creationTime")
    private String creationTime;

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
    private OpCallReqSender sender;
    
    /**
     * 请求数据
     */
    @XmlElementWrapper(name="itemList")
    @XmlElement(name = "item")  
    private List<OpCall> item;
    
	public List<OpCall> getItem() {
		return item;
	}

	public void setItem(List<OpCall> item) {
		this.item = item;
	}

	public OpCallReqSender getSender() {
        return sender;
    }

    public void setSender(OpCallReqSender sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
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
}
