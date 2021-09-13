package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public abstract class PlatFormRes<T> {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "createTime")
    private String createTime;

    @XmlElement(name = "actionId")
    private String actionId;

    @XmlElement(name = "actionName")
    private String actionName;

    /**
     * result为处理结果
     */
    protected T result;

    public abstract T getResult();

    public abstract void setResult(T result);

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

}
