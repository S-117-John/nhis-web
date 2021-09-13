package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author wq
 * @Classname TaiKangMedicalDirectoryList
 * @Description 医疗目录返回实体集合
 * @Date 2020-11-23 18:37
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MedicalNoTreatmentHospitalList")
public class TaiKangMedicalDirectoryList {
    @XmlElementWrapper(name = "DrugList")
    @XmlElement(name = "Drug")
   private  List<TaiKangMedicalDirectory> taiKangMedicalDirectoryList;

    public List<TaiKangMedicalDirectory> getTaiKangMedicalDirectoryList() {
        return taiKangMedicalDirectoryList;
    }

    public void setTaiKangMedicalDirectoryList(List<TaiKangMedicalDirectory> taiKangMedicalDirectoryList) {
        this.taiKangMedicalDirectoryList = taiKangMedicalDirectoryList;
    }
}
