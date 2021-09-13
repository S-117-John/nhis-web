package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 摆药机统一入参
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class MachInParam {

    @XmlElement(name = "accessKey")
    private String accessKey;
    @XmlElement(name = "MethodName")
    private String methodName;
    @XmlElement(name = "OrderId")
    private String orderId;

    @XmlElement(name = "Status")
    private String status;

    @XmlElement(name = "UserId")
    private String userId;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @XmlRootElement(name="DocumentElement")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class MachInputVoHeader {

        @XmlElement(name = "DataTable")
        private List<MachInParam> items;

        public List<MachInParam> getItems() {
            return items;
        }

        public void setItems(List<MachInParam> items) {
            this.items = items;
        }
    }

}
