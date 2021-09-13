package com.zebone.nhis.ma.pub.platform.pskq.model.param;

import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Req")
@XmlAccessorType(XmlAccessType.FIELD)
public class QrCodeParam {

    @XmlElement(name = "NotificationType")
    private String notificationType;

    @XmlElement(name = "NotificationInfo")
    private NotificationInfo notificationInfo;


    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationInfo getNotificationInfo() {
        return notificationInfo;
    }

    public void setNotificationInfo(NotificationInfo notificationInfo) {
        this.notificationInfo = notificationInfo;
    }






    public static void main(String[] args) {
        HttpRestTemplate httpRestTemplate =  new HttpRestTemplate();
        QrCodeParam qrCodeParam = new QrCodeParam();
        NotificationInfo notificationInfo = new NotificationInfo();
        qrCodeParam.setNotificationInfo(notificationInfo);
        qrCodeParam.setNotificationType("123");

        String result = XmlUtil.beanToXml(qrCodeParam,QrCodeParam.class);

        System.out.println(result);
    }
}
