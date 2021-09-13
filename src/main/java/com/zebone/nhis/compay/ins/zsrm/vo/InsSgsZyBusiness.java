package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 住院业务信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="program")
public class InsSgsZyBusiness extends InsSgsPubParam{
    @XmlElement(name = "bizinfo")
    private List<ZyBusinessInfo> businessInfo;

    public List<ZyBusinessInfo> getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(List<ZyBusinessInfo> businessInfo) {
        this.businessInfo = businessInfo;
    }
}
