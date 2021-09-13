package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNHYD_REGINFO 
 *
 * @since 2018-10-19 05:31:46
 */
@Table(value="INS_SUZHOUNHYD_REGINFO")
public class InsSuzhounhydReginfo extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** USERNAME - USERNAME-当前用户在新农合系统的登录用户名 */
	@Field(value="USERNAME")
    private String username;

    /** USERPWD - USERPWD-当前用户在新农合系统的登录用户口令 */
	@Field(value="USERPWD")
    private String userpwd;

    /** HOSORGNO - HOSORGNO-农合系统中医疗机构编码 */
	@Field(value="HOSORGNO")
    private String hosorgno;

    /** INPATIENTNO - INPATIENTNO-HIS住院号 */
	@Field(value="INPATIENTNO")
    private String inpatientno;

    /** CENTERNO - CENTERNO-农合中心编码（省外6位县地区编码） */
	@Field(value="CENTERNO")
    private String centerno;

    /** FAMILYSYSNO - FAMILYSYSNO-家庭编号 */
	@Field(value="FAMILYSYSNO")
    private String familysysno;

    /** MEMBERSYSNO - MEMBERSYSNO-个人编号 */
	@Field(value="MEMBERSYSNO")
    private String membersysno;

    /** STATURE - STATURE-身高 */
	@Field(value="STATURE")
    private String stature;

    /** WEIGHT - WEIGHT-体重 */
	@Field(value="WEIGHT")
    private String weight;

    /** ICDALLNO - ICDALLNO-住院疾病诊断（icd编码） */
	@Field(value="ICDALLNO")
    private String icdallno;

    /** SECONDICDNO - SECONDICDNO-第二疾病诊断（icd编码） */
	@Field(value="SECONDICDNO")
    private String secondicdno;

    /** OPSID - OPSID-手术编号 */
	@Field(value="OPSID")
    private String opsid;

    /** TREATCODE - TREATCODE-治疗方式编码 */
	@Field(value="TREATCODE")
    private String treatcode;

    /** INOFFICEID - INOFFICEID-入院科室编码 */
	@Field(value="INOFFICEID")
    private String inofficeid;

    /** OFFICEDATE - OFFICEDATE-入院日期 */
	@Field(value="OFFICEDATE")
    private String officedate;

    /** CUREID - CUREID-就诊类型 */
	@Field(value="CUREID")
    private String cureid;

    /** COMPLICATION - COMPLICATION-并发症 */
	@Field(value="COMPLICATION")
    private String complication;

    /** INHOSID - INHOSID-来院状态 */
	@Field(value="INHOSID")
    private String inhosid;

    /** CUREDOCTOR - CUREDOCTOR-经治医生 */
	@Field(value="CUREDOCTOR")
    private String curedoctor;

    /** BEDNO - BEDNO-床位号码 */
	@Field(value="BEDNO")
    private String bedno;

    /** SECTIONNO - SECTIONNO-入院病区 */
	@Field(value="SECTIONNO")
    private String sectionno;

    /** TURNMODE - TURNMODE-转诊类型 */
	@Field(value="TURNMODE")
    private String turnmode;

    /** TURNCODE - TURNCODE-转诊转院编码 */
	@Field(value="TURNCODE")
    private String turncode;

    /** TURNDATE - TURNDATE-转院日期 */
	@Field(value="TURNDATE")
    private String turndate;

    /** TICKETNO - TICKETNO-医院住院收费收据号 */
	@Field(value="TICKETNO")
    private String ticketno;

    /** MINISTERNOTICE - MINISTERNOTICE-民政通知书号 */
	@Field(value="MINISTERNOTICE")
    private String ministernotice;

    /** PROCREATENOTICE - PROCREATENOTICE-生育证号 */
	@Field(value="PROCREATENOTICE")
    private String procreatenotice;

    /** OBLIGATEONE - OBLIGATEONE-预留参数一 */
	@Field(value="OBLIGATEONE")
    private String obligateone;

    /** OBLIGATETWO - OBLIGATETWO-预留参数二 */
	@Field(value="OBLIGATETWO")
    private String obligatetwo;

    /** OBLIGATETHREE - OBLIGATETHREE-预留参数三 */
	@Field(value="OBLIGATETHREE")
    private String obligatethree;

    /** OBLIGATEFOUR - OBLIGATEFOUR-预留参数四 */
	@Field(value="OBLIGATEFOUR")
    private String obligatefour;

    /** OBLIGATEFIVE - OBLIGATEFIVE-预留参数五 */
	@Field(value="OBLIGATEFIVE")
    private String obligatefive;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

    /** INPATIENTSN - 医保登记主键 */
	@Field(value="INPATIENTSN")
    private String inpatientsn;

    /** PK_PV - HIS登记主键 */
	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_INS_PI")
    private String pkInsPi;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getUserpwd(){
        return this.userpwd;
    }
    public void setUserpwd(String userpwd){
        this.userpwd = userpwd;
    }

    public String getHosorgno(){
        return this.hosorgno;
    }
    public void setHosorgno(String hosorgno){
        this.hosorgno = hosorgno;
    }

    public String getInpatientno(){
        return this.inpatientno;
    }
    public void setInpatientno(String inpatientno){
        this.inpatientno = inpatientno;
    }

    public String getCenterno(){
        return this.centerno;
    }
    public void setCenterno(String centerno){
        this.centerno = centerno;
    }

    public String getFamilysysno(){
        return this.familysysno;
    }
    public void setFamilysysno(String familysysno){
        this.familysysno = familysysno;
    }

    public String getMembersysno(){
        return this.membersysno;
    }
    public void setMembersysno(String membersysno){
        this.membersysno = membersysno;
    }

    public String getStature(){
        return this.stature;
    }
    public void setStature(String stature){
        this.stature = stature;
    }

    public String getWeight(){
        return this.weight;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }

    public String getIcdallno(){
        return this.icdallno;
    }
    public void setIcdallno(String icdallno){
        this.icdallno = icdallno;
    }

    public String getSecondicdno(){
        return this.secondicdno;
    }
    public void setSecondicdno(String secondicdno){
        this.secondicdno = secondicdno;
    }

    public String getOpsid(){
        return this.opsid;
    }
    public void setOpsid(String opsid){
        this.opsid = opsid;
    }

    public String getTreatcode(){
        return this.treatcode;
    }
    public void setTreatcode(String treatcode){
        this.treatcode = treatcode;
    }

    public String getInofficeid(){
        return this.inofficeid;
    }
    public void setInofficeid(String inofficeid){
        this.inofficeid = inofficeid;
    }

    public String getOfficedate(){
        return this.officedate;
    }
    public void setOfficedate(String officedate){
        this.officedate = officedate;
    }

    public String getCureid(){
        return this.cureid;
    }
    public void setCureid(String cureid){
        this.cureid = cureid;
    }

    public String getComplication(){
        return this.complication;
    }
    public void setComplication(String complication){
        this.complication = complication;
    }

    public String getInhosid(){
        return this.inhosid;
    }
    public void setInhosid(String inhosid){
        this.inhosid = inhosid;
    }

    public String getCuredoctor(){
        return this.curedoctor;
    }
    public void setCuredoctor(String curedoctor){
        this.curedoctor = curedoctor;
    }

    public String getBedno(){
        return this.bedno;
    }
    public void setBedno(String bedno){
        this.bedno = bedno;
    }

    public String getSectionno(){
        return this.sectionno;
    }
    public void setSectionno(String sectionno){
        this.sectionno = sectionno;
    }

    public String getTurnmode(){
        return this.turnmode;
    }
    public void setTurnmode(String turnmode){
        this.turnmode = turnmode;
    }

    public String getTurncode(){
        return this.turncode;
    }
    public void setTurncode(String turncode){
        this.turncode = turncode;
    }

    public String getTurndate(){
        return this.turndate;
    }
    public void setTurndate(String turndate){
        this.turndate = turndate;
    }

    public String getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(String ticketno){
        this.ticketno = ticketno;
    }

    public String getMinisternotice(){
        return this.ministernotice;
    }
    public void setMinisternotice(String ministernotice){
        this.ministernotice = ministernotice;
    }

    public String getProcreatenotice(){
        return this.procreatenotice;
    }
    public void setProcreatenotice(String procreatenotice){
        this.procreatenotice = procreatenotice;
    }

    public String getObligateone(){
        return this.obligateone;
    }
    public void setObligateone(String obligateone){
        this.obligateone = obligateone;
    }

    public String getObligatetwo(){
        return this.obligatetwo;
    }
    public void setObligatetwo(String obligatetwo){
        this.obligatetwo = obligatetwo;
    }

    public String getObligatethree(){
        return this.obligatethree;
    }
    public void setObligatethree(String obligatethree){
        this.obligatethree = obligatethree;
    }

    public String getObligatefour(){
        return this.obligatefour;
    }
    public void setObligatefour(String obligatefour){
        this.obligatefour = obligatefour;
    }

    public String getObligatefive(){
        return this.obligatefive;
    }
    public void setObligatefive(String obligatefive){
        this.obligatefive = obligatefive;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getInpatientsn(){
        return this.inpatientsn;
    }
    public void setInpatientsn(String inpatientsn){
        this.inpatientsn = inpatientsn;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkInsPi(){
        return this.pkInsPi;
    }
    public void setPkInsPi(String pkInsPi){
        this.pkInsPi = pkInsPi;
    }
}