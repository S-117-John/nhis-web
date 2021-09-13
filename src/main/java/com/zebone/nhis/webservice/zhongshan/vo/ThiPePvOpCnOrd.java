package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiPePvOpCnOrd
 * @Description 16号接口出入参
 * @Date 2021-06-15 16:24
 * @Created by wuqiang
 */
public class ThiPePvOpCnOrd {
    @NotBlank(message = "体检医嘱流水号不能为空")
    private String orderid;
    @NotBlank(message = "医嘱主键不能为空")
    private String pkOrd;
    /** 医嘱编码*/
    private String codeOrd;
    /** 医嘱名称*/
    private String nameOrd;
    /** 医嘱描述*/
    private String descOrd;
    /** 医嘱重复类型，0 长期(可重复)，1 临时(一次)；体检医嘱传1*/
    @NotBlank(message = "医嘱重复类型不能为空")
    private String euAlways;
    /** 医嘱频次,默认为once。
     *体检医嘱传once, 医嘱重复类型1-临时。
     */
    @NotBlank(message = "医嘱频次不能为空")
    private String codeFreq;
    @NotBlank(message = "医嘱数量不能为空")
    private String quan;
    /** 嘱托标志，1 嘱托医嘱；0非嘱托医嘱；默认传0
     */
    private String flagNote;
    /**记费标志，1 记费；0不记费；
     *默认传1
     */
    @NotBlank(message = "是否记费标志不能为空")
    private String flagBl;
    /**开始时间，yyyy-mm-dd hh24:mi:ss
     */
    @NotBlank(message = "开始时间不能为空")
    private String dateStart;
    /**录入时间,yyyy-mm-dd hh24:mi:ss
     */
    @NotBlank(message = "录入时间不能为空")
    private String dateEnter;
    /**录入人主键
     */
    @NotBlank(message = "录入人主键不能为空")
    private String pkEmpInput;
    /**开立病区主键
     */
    private String pkDeptNs;
    /**开立科室主键
     */
    @NotBlank(message = "开立科室主键")
    private String pkDept;
    /**开立科室主键
     */
    @NotBlank(message = "开立科室主键")
    private String pkEmpOrd;
    /**执行科室主键
     */
    @NotBlank(message = "执行科室主键")
    private String pkDeptExec;
    /**开加急标志，1 加急；0 不加急
     */
    @NotBlank(message = "加急标志不能为空")
    private String flagEmer;
    /**医嘱备注
     */
    private String noteOrd;
  //返回值
    /**HIS医嘱主键
     */
    private String pkCnord;
    @Override
    public String toString() {
        return "ThiPePvOpCnOrd{" +
                ", orderid='" + orderid + '\'' +
                ", pkOrd='" + pkOrd + '\'' +
                ", codeOrd='" + codeOrd + '\'' +
                ", nameOrd='" + nameOrd + '\'' +
                ", descOrd='" + descOrd + '\'' +
                ", euAlways='" + euAlways + '\'' +
                ", codeFreq='" + codeFreq + '\'' +
                ", quan='" + quan + '\'' +
                ", flagNote='" + flagNote + '\'' +
                ", flagBl='" + flagBl + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnter='" + dateEnter + '\'' +
                ", pkEmpInput='" + pkEmpInput + '\'' +
                ", pkDeptNs='" + pkDeptNs + '\'' +
                ", pkDept='" + pkDept + '\'' +
                ", pkEmpOrd='" + pkEmpOrd + '\'' +
                ", pkDeptExec='" + pkDeptExec + '\'' +
                ", flagEmer='" + flagEmer + '\'' +
                ", noteOrd='" + noteOrd + '\'' +
                '}';
    }



    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public String getCodeOrd() {
        return codeOrd;
    }

    public void setCodeOrd(String codeOrd) {
        this.codeOrd = codeOrd;
    }

    public String getNameOrd() {
        return nameOrd;
    }

    public void setNameOrd(String nameOrd) {
        this.nameOrd = nameOrd;
    }

    public String getDescOrd() {
        return descOrd;
    }

    public void setDescOrd(String descOrd) {
        this.descOrd = descOrd;
    }

    public String getEuAlways() {
        return euAlways;
    }

    public void setEuAlways(String euAlways) {
        this.euAlways = euAlways;
    }

    public String getCodeFreq() {
        return codeFreq;
    }

    public void setCodeFreq(String codeFreq) {
        this.codeFreq = codeFreq;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getFlagNote() {
        return flagNote;
    }

    public void setFlagNote(String flagNote) {
        this.flagNote = flagNote;
    }

    public String getFlagBl() {
        return flagBl;
    }

    public void setFlagBl(String flagBl) {
        this.flagBl = flagBl;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnter() {
        return dateEnter;
    }

    public void setDateEnter(String dateEnter) {
        this.dateEnter = dateEnter;
    }

    public String getPkEmpInput() {
        return pkEmpInput;
    }

    public void setPkEmpInput(String pkEmpInput) {
        this.pkEmpInput = pkEmpInput;
    }

    public String getPkDeptNs() {
        return pkDeptNs;
    }

    public void setPkDeptNs(String pkDeptNs) {
        this.pkDeptNs = pkDeptNs;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkEmpOrd() {
        return pkEmpOrd;
    }

    public void setPkEmpOrd(String pkEmpOrd) {
        this.pkEmpOrd = pkEmpOrd;
    }

    public String getPkDeptExec() {
        return pkDeptExec;
    }

    public void setPkDeptExec(String pkDeptExec) {
        this.pkDeptExec = pkDeptExec;
    }

    public String getFlagEmer() {
        return flagEmer;
    }

    public void setFlagEmer(String flagEmer) {
        this.flagEmer = flagEmer;
    }

    public String getNoteOrd() {
        return noteOrd;
    }

    public void setNoteOrd(String noteOrd) {
        this.noteOrd = noteOrd;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }
}
