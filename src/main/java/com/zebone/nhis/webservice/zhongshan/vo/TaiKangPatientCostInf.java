package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author 12370
 * @Classname TaiKangPatientCostInf
 * @Description 病人费用信息返回实体类
 * @Date 2020-11-23 14:28
 */
@XmlAccessorType(XmlAccessType.NONE)
public class TaiKangPatientCostInf {

    /*
     * 业务流水号_入院日期（拼接）
     * */

    @XmlElement(name = "TreatSerialNo")
    private String codeIpDateAdmit;

    /*
     * 就诊号-业务流水号
     * */

    @XmlElement(name = "PatientNumber")
    private String codeIp;

    /*
     * 入院时间-入院时间
     * */

    @XmlElement(name = "InHosDate")
    private String dateAdmit;

    /*
     * 开立医生
     * */

    @XmlElement(name = "Doctor")
    private String doctor;
    /*
     * 医嘱，药品编码-编码
     * */

    @XmlElement(name = "InfoCode")

    private String code;
    /*
     * 费用金额 保留小数点后2位
     * */

    @XmlElement(name = "InfoMoney")

    private String infoMoney;
    /*
     * 费用名称
     * */

    @XmlElement(name = "InfoName")

    private String infoName;
    /*
     * 数量 保留小数点后2位
     * */

    @XmlElement(name = "InfoNumber")

    private String infonumber;
    /*
     * 单价 保留小数点后2位
     * */

    @XmlElement(name = "InfoUnit")

    private String infounit;
    /*
     * 费用日期
     * */

    @XmlElement(name = "FeesDate")

    private String feesDate;
    /*
     * 标识号
     * 病人费用记录在医院HIS系统内的唯一标识，需要全局。
     * */
    @XmlElement(name = "Sign")

    private String sign;



    public String getcodeIp() {
        return codeIp;
    }

    public void setcodeIp(String codeIp) {
        this.codeIp = codeIp;
    }



    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfoMoney() {
        return infoMoney;
    }

    public void setInfoMoney(String infoMoney) {
        this.infoMoney = infoMoney;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfonumber() {
        return infonumber;
    }

    public void setInfonumber(String infonumber) {
        this.infonumber = infonumber;
    }

    public String getInfounit() {
        return infounit;
    }

    public void setInfounit(String infounit) {
        this.infounit = infounit;
    }



    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getCodeIpDateAdmit() {
        return codeIpDateAdmit;
    }

    public void setCodeIpDateAdmit(String codeIpDateAdmit) {
        this.codeIpDateAdmit = codeIpDateAdmit;
    }

    public String getDateAdmit() {
        return dateAdmit;
    }

    public void setDateAdmit(String dateAdmit) {
        this.dateAdmit = dateAdmit;
    }

    public String getFeesDate() {
        return feesDate;
    }

    public void setFeesDate(String feesDate) {
        this.feesDate = feesDate;
    }
}




