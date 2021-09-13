package com.zebone.nhis.ma.pub.platform.pskq.model.param;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NotificationInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificationInfo {

    @XmlElement(name = "WeiXinPayInfo")
    private WeiXinPayInfo weiXinPayInfo;

    public WeiXinPayInfo getWeiXinPayInfo() {
        return weiXinPayInfo;
    }

    public void setWeiXinPayInfo(WeiXinPayInfo weiXinPayInfo) {
        this.weiXinPayInfo = weiXinPayInfo;
    }
}
