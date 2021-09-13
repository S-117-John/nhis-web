package com.zebone.nhis.ma.pub.platform.pskq.model.param;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NotificationInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class NoticeNotification {

    @XmlElement(name = "CommonInfo")
    private CommonInfo CommonInfo;

	public CommonInfo getCommonInfo() {
		return CommonInfo;
	}

	public void setCommonInfo(CommonInfo commonInfo) {
		CommonInfo = commonInfo;
	}

}
