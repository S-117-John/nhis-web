package com.zebone.nhis.ma.pub.platform.pskq.model.param;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Req")
@XmlAccessorType(XmlAccessType.FIELD)
public class NoticeParam {

    @XmlElement(name = "NotificationType")
    private String notificationType;

    @XmlElement(name = "NotificationInfo")
    private NoticeNotification noticeNotification;


    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    public NoticeNotification getNoticeNotification() {
		return noticeNotification;
	}

	public void setNoticeNotification(NoticeNotification noticeNotification) {
		this.noticeNotification = noticeNotification;
	}
}
