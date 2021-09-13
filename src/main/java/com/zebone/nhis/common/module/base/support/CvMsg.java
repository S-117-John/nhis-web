package com.zebone.nhis.common.module.base.support;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CV_MSG - CV_MSG 
 *
 * @since 2017-8-15 9:20:02
 */
@Table(value="CV_MSG")
public class CvMsg   {

	@PK
	@Field(value="PK_MSG",id=KeyId.UUID)
    private String pkMsg;

	@Field(value="ORGCODE")
    private String orgcode;

	@Field(value="CODE_PI")
    private String codePi;

    /** EU_PVTYPE - 1门诊，2急诊，3住院，4体检，5家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="CODE_PV")
    private String codePv;

	@Field(value="NAME_PI")
    private String namePi;

	@Field(value="SUBJECT")
    private String subject;

	@Field(value="CONTENT")
    private String content;

	@Field(value="DATE_MSG")
    private Date dateMsg;

	@Field(value="FLAG_REPLY")
    private String flagReply;

    /** EU_LEVEL - 0 普通，1 重要，2 紧急 */
	@Field(value="EU_LEVEL")
    private String euLevel;

    /** EU_STATUS - 0 未发送，1 已发送，2 已阅读，3 已回复，8 已完成 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** DT_SYSTYPE - 基础码表051001 */
	@Field(value="DT_SYSTYPE")
    private String dtSystype;

	@Field(value="CODE_DEPT")
    private String codeDept;

	@Field(value="NAME_DEPT")
    private String nameDept;

	@Field(value="CODE_SENDER")
    private String codeSender;

	@Field(value="NAME_SENDER")
    private String nameSender;

	@Field(value="TIMES")
    private BigDecimal times;

	@Field(value="DATE_SEND")
    private Date dateSend;

	@Field(value="NOTE")
    private String note;

	@Field(value="CODE_SRC")
    private String codeSrc;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="BED_NO")
    private String bedNo;

	@Field(value="CODE_DR")
    private String codeDr;

	@Field(value="NAME_DR")
    private String nameDr;

	@Field(value="DESC_DIAG")
    private String descDiag;

	@Field(value="IP_SEND")
    private String ipSend;

	@Field(value="NAME_DEPT_PV")
    private String nameDeptPv;


	

    public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getCodeDr() {
		return codeDr;
	}
	public void setCodeDr(String codeDr) {
		this.codeDr = codeDr;
	}
	public String getNameDr() {
		return nameDr;
	}
	public void setNameDr(String nameDr) {
		this.nameDr = nameDr;
	}
	public String getDescDiag() {
		return descDiag;
	}
	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}
	public String getIpSend() {
		return ipSend;
	}
	public void setIpSend(String ipSend) {
		this.ipSend = ipSend;
	}
	public String getNameDeptPv() {
		return nameDeptPv;
	}
	public void setNameDeptPv(String nameDeptPv) {
		this.nameDeptPv = nameDeptPv;
	}
	public String getPkMsg(){
        return this.pkMsg;
    }
    public void setPkMsg(String pkMsg){
        this.pkMsg = pkMsg;
    }

    public String getOrgcode(){
        return this.orgcode;
    }
    public void setOrgcode(String orgcode){
        this.orgcode = orgcode;
    }

    public String getCodePi(){
        return this.codePi;
    }
    public void setCodePi(String codePi){
        this.codePi = codePi;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getCodePv(){
        return this.codePv;
    }
    public void setCodePv(String codePv){
        this.codePv = codePv;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public String getSubject(){
        return this.subject;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public Date getDateMsg(){
        return this.dateMsg;
    }
    public void setDateMsg(Date dateMsg){
        this.dateMsg = dateMsg;
    }

    public String getFlagReply(){
        return this.flagReply;
    }
    public void setFlagReply(String flagReply){
        this.flagReply = flagReply;
    }

    public String getEuLevel(){
        return this.euLevel;
    }
    public void setEuLevel(String euLevel){
        this.euLevel = euLevel;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getDtSystype(){
        return this.dtSystype;
    }
    public void setDtSystype(String dtSystype){
        this.dtSystype = dtSystype;
    }

    public String getCodeDept(){
        return this.codeDept;
    }
    public void setCodeDept(String codeDept){
        this.codeDept = codeDept;
    }

    public String getNameDept(){
        return this.nameDept;
    }
    public void setNameDept(String nameDept){
        this.nameDept = nameDept;
    }

    public String getCodeSender(){
        return this.codeSender;
    }
    public void setCodeSender(String codeSender){
        this.codeSender = codeSender;
    }

    public String getNameSender(){
        return this.nameSender;
    }
    public void setNameSender(String nameSender){
        this.nameSender = nameSender;
    }

    public BigDecimal getTimes(){
        return this.times;
    }
    public void setTimes(BigDecimal times){
        this.times = times;
    }

    public Date getDateSend(){
        return this.dateSend;
    }
    public void setDateSend(Date dateSend){
        this.dateSend = dateSend;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getCodeSrc(){
        return this.codeSrc;
    }
    public void setCodeSrc(String codeSrc){
        this.codeSrc = codeSrc;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}