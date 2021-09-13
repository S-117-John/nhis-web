package com.zebone.nhis.ma.pub.platform.emr.vo.sendvo;

import javax.xml.bind.annotation.*;

/**电子病历调用平台请求参数vo构建
 * create by: gao shiheng
 *
 * @Param: null
 * @return
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="request")
public class RequestEmrVo {

    @XmlAttribute(name="ITSVersion")
    private String iTSVersion;

    @XmlElement(name="id")
    private String id;

    @XmlElement(name="createTime")
    private String createTime;

    @XmlElement(name="actionId")
    private String actionId;

    @XmlElement(name="actionName")
    private String actionName;

    @XmlElement(name="sender")
    private ReqEmrSender reqEmrSender;

    @XmlElement(name="subject")
    private ReqEmrSubject reqEmrSubject;

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

    public ReqEmrSender getReqEmrSender() {
        if (reqEmrSender == null) reqEmrSender = new ReqEmrSender();

        return reqEmrSender;
    }

    public void setReqEmrSender(ReqEmrSender reqEmrSender) {
        this.reqEmrSender = reqEmrSender;
    }

    public ReqEmrSubject getReqEmrSubject() {
        if (reqEmrSubject == null) reqEmrSubject = new ReqEmrSubject();
        return reqEmrSubject;
    }

    public void setReqEmrSubject(ReqEmrSubject reqEmrSubject) {
        this.reqEmrSubject = reqEmrSubject;
    }

    public String getiTSVersion() {
        return iTSVersion;
    }

    public void setiTSVersion(String iTSVersion) {
        this.iTSVersion = iTSVersion;
    }
}
