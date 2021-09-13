package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author wq
 * @Classname TaiKangRequestMedVo
 * @Description泰康人寿接口查询医疗目录请求实体类
 * @Date 2020-11-24 9:17
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MedicalNoTreatmentHospital")
public class TaiKangRequestMedVo {

    /*
     * 药品，医嘱编码-医院目录编码
     * */

    @XmlElement(name = "HospitalListNumber")
    private List<String>  code;

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }
}
