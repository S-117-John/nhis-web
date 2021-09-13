package com.zebone.nhis.ma.pub.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Classname EcgHealthGetQrInVo
 * @Description TODO
 * @Date 2021-03-29 14:50
 * @Created by wuqiang
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EcgHealthGetQrInVo {

    //健康卡号
    @XmlElement(name = "ehealth_card_id")
    private String ehealthCardId;
    //代码类型 1静态 0动态  Null则默认为静态二维码
    @XmlElement(name = "code_type")
    private String codeType;

    public String getEhealthCardId() {
        return ehealthCardId;
    }

    public void setEhealthCardId(String ehealthCardId) {
        this.ehealthCardId = ehealthCardId;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }


}
