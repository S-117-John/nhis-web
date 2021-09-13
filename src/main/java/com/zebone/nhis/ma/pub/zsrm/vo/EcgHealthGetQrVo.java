package com.zebone.nhis.ma.pub.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Classname EcgHealthGetQrVo
 * @Description TODO
 * @Date 2021-03-29 15:04
 * @Created by wuqiang
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EcgHealthGetQrVo {
    //电子二维码
    @XmlElement(name = "Ehealth_code")
    private String ehealthCode;

    public String getEhealthCode() {
        return ehealthCode;
    }

    public void setEhealthCode(String ehealthCode) {
        this.ehealthCode = ehealthCode;
    }
}
