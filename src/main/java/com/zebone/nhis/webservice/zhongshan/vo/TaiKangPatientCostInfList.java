package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author wq
 * @Classname TaiKangPatientCostInfList
 * @Description 病人出院信息集合
 * @Date 2020-11-23 16:08
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PrescriptionHisDetailList")
public class TaiKangPatientCostInfList {
    @XmlElement(name = "PrescriptionHisDetail")
    private List<TaiKangPatientCostInf> taiKangPatientCostInfList;

    public List<TaiKangPatientCostInf> getTaiKangPatientCostInfList() {
        return taiKangPatientCostInfList;
    }

    public void setTaiKangPatientCostInfList(List<TaiKangPatientCostInf> taiKangPatientCostInfList) {
        this.taiKangPatientCostInfList = taiKangPatientCostInfList;
    }
}
