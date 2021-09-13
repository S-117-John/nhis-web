package com.zebone.nhis.ma.pub.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Classname EcgHealthGetQrIn
 * @Description 获取电子健康二维码A1026入参
 * @Date 2021-03-27 17:18
 * @Created by wuqiang
 */
@XmlRootElement(name="Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EcgHealthGetQrIn extends HeadVo {
    @XmlElement(name = "Body")
    private EcgHealthGetQrInVo ecgHealthGetQrInVo;


    public EcgHealthGetQrInVo getEcgHealthGetQrInVo() {
        return ecgHealthGetQrInVo;
    }

    public void setEcgHealthGetQrInVo(EcgHealthGetQrInVo ecgHealthGetQrInVo) {
        this.ecgHealthGetQrInVo = ecgHealthGetQrInVo;
    }

}

