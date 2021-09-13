package com.zebone.nhis.webservice.zhongshan.vo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 12370
 * @Classname TaiKangRequestVo
 * @Description 泰康人寿接口查询入院信息和出院信息请求实体类
 * @Date 2020-11-23 9:38
 * @Created by wuqiang
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MedicalInfo")
public class TaiKangRequestAdmOutVo {
    /*
     * 住院号-业务流水号
     * */

    @XmlElement(name = "PatientNumber")
    private String codeIp;
    /*
     * 入科时间-入院时间
     * */

    @XmlElement(name = "InHosDate")
    private String dateAdmit;
    /*
     * 开始时间
     * */

    @XmlElement(name = "TreatStartDate")
    private String dataBegin;
    /*
     * 结束时间-结束时间
     * */

    @XmlElement(name = "TreatEndDate")
    private String dataEnd;
    /*
     * 证件类型-证件类型
     * */

    @XmlElement(name = "IdType")
    private String dtIdtype;
    /*
     * 证件号码-证件号码
     * */

    @XmlElement(name = "CardId")
    private String idNo;
    /*
     * 姓名-用户名
     * */

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getDateAdmit() {
        return dateAdmit;
    }

    public void setDateAdmit(String dateAdmit) {
        this.dateAdmit = dateAdmit;
    }

    public String getDataBegin() {
        return dataBegin;
    }

    public void setDataBegin(String dataBegin) {
        this.dataBegin = dataBegin;
    }

    public String getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(String dataEnd) {
        this.dataEnd = dataEnd;
    }

    public String getDtIdtype() {
        return dtIdtype;
    }

    public void setDtIdtype(String dtIdtype) {
        this.dtIdtype = dtIdtype;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "Name")
    private String name;


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }


}
