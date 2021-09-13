package com.zebone.nhis.compay.pub.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 转院备案参数
 * @author 卡卡西
 */
public class TransferToHospitalForRecordParam {

    public static Map<String,Object> insutypeMap = new HashMap<>(16);



    /**
     * 人员编号
     */
    @NotBlank(message = "人员编号不能为空")
    @Length(max = 30)
    private String psnNo;

    /**
     * 险种类型
     */
    @Length(max = 6)
    @NotBlank(message = "险种类型不能为空")
    private String insutype;

    /**
     * 联系电话
     */
    @Length(max = 50)
    private String tel;

    /**
     * 联系地址
     */
    @Length(max = 200)
    private String addr;

    /**
     * 参保机构医保区划
     */
    @Length(max = 6)
    private String insuOptins;

    /**
     * 诊断代码
     */
    @NotBlank(message = "诊断代码不能为空")
    @Length(max = 20)
    private String diagCode;

    /**
     * 诊断名称
     */
    @NotBlank(message = "诊断名称不能为空")
    @Length(max = 100)
    private String diagName;

    /**
     * 疾病病情描述
     */
    @Length(max = 2000)
    private String diseCondDscr;

    /**
     * 转往定点医药机构编号
     */
    @NotBlank(message = "转往定点医药机构编号不能为空")
    @Length(max = 12)
    private String reflinMedinsNo;

    /**
     * 转往医院名称
     */
    @NotBlank(message = "转往医院名称不能为空")
    @Length(max = 200)
    private String reflinMedinsName;

    /**
     * 就医地行政区划
     */
    @Length(max = 6)
    private String mdtrtareaAdmdvs;

    /**
     * 医院同意转院标志
     */
    @Length(max = 30)
    @NotBlank(message = "医院同意转院标志不能为空")
    private String hospAgreReflFlag;

    /**
     * 转院类型
     */
    @NotBlank(message = "转院类型不能为空")
    @Length(max = 30)
    private String reflType;


    /**
     * 转院日期
     * yyyy-MM-dd
     */

    private Date reflDate;

    /**
     * 转院原因
     */
    @NotBlank(message = "转院原因不能为空")
    @Length(max = 100)
    private String reflRea;

    /**
     * 转院意见
     */
    @NotBlank(message = "转院意见不能为空")
    @Length(max = 200)
    private String reflOpnn;

    /**
     * 开始日期
     * yyyy-MM-dd
     */
    private Date begndate;

    /**
     * 结束日期
     * yyyy-MM-dd
     */
    private Date enddate;

    /**
     * 转诊使用标志
     */
    @Length(max = 3)
    private String reflUsedFlag;


    private String mdtrtId;

    public String getMdtrtId() {
        return mdtrtId;
    }

    public void setMdtrtId(String mdtrtId) {
        this.mdtrtId = mdtrtId;
    }

    public static Map<String, Object> getInsutypeMap() {
        return insutypeMap;
    }

    public static void setInsutypeMap(Map<String, Object> insutypeMap) {
        TransferToHospitalForRecordParam.insutypeMap = insutypeMap;
    }

    public String getPsnNo() {
        return psnNo;
    }

    public void setPsnNo(String psnNo) {
        this.psnNo = psnNo;
    }

    public String getInsutype() {
        return insutype;
    }

    public void setInsutype(String insutype) {
        this.insutype = insutype;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getInsuOptins() {
        return insuOptins;
    }

    public void setInsuOptins(String insuOptins) {
        this.insuOptins = insuOptins;
    }

    public String getDiagCode() {
        return diagCode;
    }

    public void setDiagCode(String diagCode) {
        this.diagCode = diagCode;
    }

    public String getDiagName() {
        return diagName;
    }

    public void setDiagName(String diagName) {
        this.diagName = diagName;
    }

    public String getDiseCondDscr() {
        return diseCondDscr;
    }

    public void setDiseCondDscr(String diseCondDscr) {
        this.diseCondDscr = diseCondDscr;
    }

    public String getReflinMedinsNo() {
        return reflinMedinsNo;
    }

    public void setReflinMedinsNo(String reflinMedinsNo) {
        this.reflinMedinsNo = reflinMedinsNo;
    }

    public String getReflinMedinsName() {
        return reflinMedinsName;
    }

    public void setReflinMedinsName(String reflinMedinsName) {
        this.reflinMedinsName = reflinMedinsName;
    }

    public String getMdtrtareaAdmdvs() {
        return mdtrtareaAdmdvs;
    }

    public void setMdtrtareaAdmdvs(String mdtrtareaAdmdvs) {
        this.mdtrtareaAdmdvs = mdtrtareaAdmdvs;
    }

    public String getHospAgreReflFlag() {
        return hospAgreReflFlag;
    }

    public void setHospAgreReflFlag(String hospAgreReflFlag) {
        this.hospAgreReflFlag = hospAgreReflFlag;
    }

    public String getReflType() {
        return reflType;
    }

    public void setReflType(String reflType) {
        this.reflType = reflType;
    }

    public Date getReflDate() {
        return reflDate;
    }

    public void setReflDate(Date reflDate) {
        this.reflDate = reflDate;
    }

    public String getReflRea() {
        return reflRea;
    }

    public void setReflRea(String reflRea) {
        this.reflRea = reflRea;
    }

    public String getReflOpnn() {
        return reflOpnn;
    }

    public void setReflOpnn(String reflOpnn) {
        this.reflOpnn = reflOpnn;
    }

    public Date getBegndate() {
        return begndate;
    }

    public void setBegndate(Date begndate) {
        this.begndate = begndate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getReflUsedFlag() {
        return reflUsedFlag;
    }

    public void setReflUsedFlag(String reflUsedFlag) {
        this.reflUsedFlag = reflUsedFlag;
    }
}
