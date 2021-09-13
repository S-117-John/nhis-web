package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value="CN_NOTICE")
public class CnNotice {

    @PK
    @Field(value="PK_CNNOTICE",id= Field.KeyId.UUID)
    private String pkCnnotice;

    @Field(value="PK_ORG",userfield="pkOrg",userfieldscop= Field.FieldType.INSERT)
    private String pkOrg;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="NAME_PI")
    private String namePi;

    /**
     * 急医嘱数
     */
    @Field(value="CNT_EMER")
    private Integer cntEmer;

    /**
     * 新开医嘱
     */
    @Field(value="CNT_NEW")
    private Integer cntNew;

    /**
     * 新停医嘱
     */
    @Field(value="CNT_END")
    private Integer cntEnd;

    /**
     * 作废医嘱
     */
    @Field(value="CNT_VOID")
    private Integer cntVoid;

    @Field(value="NOTE")
    private String note;

    /**
     * 当前科室编码
     */
    @Field(value="PK_DEPT_SEND")
    private String pkDeptSend;

    /**
     * 当前科室名称
     */
    @Field(value="DEPT_SEND")
    private String deptSend;

    /**
     * 当前医生名称
     */
    @Field(value="EMP_SEND")
    private String empSend;

    /**
     * 当前时间
     */
    @Field(value="DATE_SEND",date= Field.FieldType.INSERT)
    private Date dateSend;

    /**
     * 接收病区编码
     */
    @Field(value="PK_DEPT_RECP")
    private String pkDeptRecp;

    /**
     * 接收病区名称
     */
    @Field(value="DEPT_RECP")
    private String deptRecp;

    @Field(value="EMP_RECP")
    private String empRecp;

    @Field(value="DATE_RECP")
    private Date dateRecp;

    @Field(value="DATE_CHK")
    private Date dateChk;

    @Field(value="EU_STATUS")
    private String euStatus;

    @Field(value="EU_TYPE")
    private String euType;

    @Field(value="PK_CNORDER")
    private String pkCnorder;

    @Field(value="CREATOR",userfield="pkEmp",userfieldscop= Field.FieldType.INSERT)
    private String creator;

    @Field(value="CREATE_TIME",date= Field.FieldType.INSERT)
    private Date createTime;

    @Field(value="DEL_FLAG",date= Field.FieldType.INSERT)
    private String delFlag;

    @Field(value="TS",date= Field.FieldType.ALL)
    private Date ts;

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public String getPkCnorder() {
        return pkCnorder;
    }

    public void setPkCnorder(String pkCnorder) {
        this.pkCnorder = pkCnorder;
    }

    public Integer getCntVoid() {
        return cntVoid;
    }

    public void setCntVoid(Integer cntVoid) {
        this.cntVoid = cntVoid;
    }

    public String getPkCnnotice() {
        return pkCnnotice;
    }

    public void setPkCnnotice(String pkCnnotice) {
        this.pkCnnotice = pkCnnotice;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public Integer getCntEmer() {
        return cntEmer;
    }

    public void setCntEmer(Integer cntEmer) {
        this.cntEmer = cntEmer;
    }

    public Integer getCntNew() {
        return cntNew;
    }

    public void setCntNew(Integer cntNew) {
        this.cntNew = cntNew;
    }

    public Integer getCntEnd() {
        return cntEnd;
    }

    public void setCntEnd(Integer cntEnd) {
        this.cntEnd = cntEnd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPkDeptSend() {
        return pkDeptSend;
    }

    public void setPkDeptSend(String pkDeptSend) {
        this.pkDeptSend = pkDeptSend;
    }

    public String getDeptSend() {
        return deptSend;
    }

    public void setDeptSend(String deptSend) {
        this.deptSend = deptSend;
    }

    public String getEmpSend() {
        return empSend;
    }

    public void setEmpSend(String empSend) {
        this.empSend = empSend;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public String getPkDeptRecp() {
        return pkDeptRecp;
    }

    public void setPkDeptRecp(String pkDeptRecp) {
        this.pkDeptRecp = pkDeptRecp;
    }

    public String getDeptRecp() {
        return deptRecp;
    }

    public void setDeptRecp(String deptRecp) {
        this.deptRecp = deptRecp;
    }

    public String getEmpRecp() {
        return empRecp;
    }

    public void setEmpRecp(String empRecp) {
        this.empRecp = empRecp;
    }

    public Date getDateRecp() {
        return dateRecp;
    }

    public void setDateRecp(Date dateRecp) {
        this.dateRecp = dateRecp;
    }

    public Date getDateChk() {
        return dateChk;
    }

    public void setDateChk(Date dateChk) {
        this.dateChk = dateChk;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}