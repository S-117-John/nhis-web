package com.zebone.nhis.ma.pub.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Classname EcgHealthGetQr
 * @Description 获取电子健康二维码A1026返回参数
 * @Date 2021-03-27 17:23
 * @Created by wuqiang
 */
@XmlRootElement(name="Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EcgHealthGetQr extends HeadVo {
    @XmlElement(name = "Body")
    private  EcgHealthGetQrVo ecgHealthGetQrVo;

    public EcgHealthGetQrVo getEcgHealthGetQrVo() {
        return ecgHealthGetQrVo;
    }

    public void setEcgHealthGetQrVo(EcgHealthGetQrVo ecgHealthGetQrVo) {
        this.ecgHealthGetQrVo = ecgHealthGetQrVo;
    }
}
