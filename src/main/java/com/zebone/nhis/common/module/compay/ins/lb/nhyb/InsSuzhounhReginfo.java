package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNH_REGINFO 
 *宿州农合医保登记信息
 *Pk_Pv 关联HIS登记信息
 * @since 2018-04-18 04:30:44
 */
@Table(value="INS_SUZHOUNH_REGINFO")
public class InsSuzhounhReginfo extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** REGTYPE - REGTYPE-登记类型: */
	@Field(value="REGTYPE")
    private String regtype;

    /** RECCODE - RECCODE-就诊号(通过GetRecCode获得的) */
	@Field(value="RECCODE")
    private String reccode;

    /** PERSONNO - PERSONNO-个人编号 */
	@Field(value="PERSONNO")
    private String personno;

    /** DISEASENO1 - DISEASENO1-入院主诊断(合管办提供的疾病名称) */
	@Field(value="DISEASENO1")
    private String diseaseno1;

    /** DISEASENO2 - DISEASENO2-入院次诊断(合管办提供的疾病名称) */
	@Field(value="DISEASENO2")
    private String diseaseno2;

    /** DISEASENO3 - DISEASENO3-入院三诊断(合管办提供的疾病名称) */
	@Field(value="DISEASENO3")
    private String diseaseno3;

    /** REGDATE - REGDATE-入院日期 */
	@Field(value="REGDATE")
    private String regdate;

    /** INPATOPERATOR - INPATOPERATOR-入院登记人/门诊就诊登记人 */
	@Field(value="INPATOPERATOR")
    private String inpatoperator;

    /** LEAVEDATE - LEAVEDATE-出院日期 */
	@Field(value="LEAVEDATE")
    private String leavedate;

    /** LEAVEOPERATOR - LEAVEOPERATOR-出院登记人(回归、冲消登记人) */
	@Field(value="LEAVEOPERATOR")
    private String leaveoperator;

    /** DEPARTMENT - DEPARTMENT-住院科室 */
	@Field(value="DEPARTMENT")
    private String department;

    /** MARRIAGE - MARRIAGE-电话号码 */
	@Field(value="MARRIAGE")
    private String marriage;

    /** TRANSFER - TRANSFER-是否转院(0、1) */
	@Field(value="TRANSFER")
    private String transfer;

    /** TRANSFERNO - TRANSFERNO-转诊单号 */
	@Field(value="TRANSFERNO")
    private String transferno;

    /** LDISEASENO1 - LDISEASENO1-出院主诊断(合管办提供的疾病名称) */
	@Field(value="LDISEASENO1")
    private String ldiseaseno1;

    /** LDISEASENO2 - LDISEASENO2-出院次诊断(合管办提供的疾病名称) */
	@Field(value="LDISEASENO2")
    private String ldiseaseno2;

    /** LDISEASENO3 - LDISEASENO3-出院三诊断(合管办提供的疾病名称) */
	@Field(value="LDISEASENO3")
    private String ldiseaseno3;

    /** DISEASE1 - DISEASE1-入院病情(入院主诊断对应的疾病编码) */
	@Field(value="DISEASE1")
    private String disease1;

    /** DISEASE2 - DISEASE2-入院病情(入院次诊断对应的疾病编码) */
	@Field(value="DISEASE2")
    private String disease2;

    /** DISEASE3 - DISEASE3-入院病情(入院三诊断对应的疾病编码) */
	@Field(value="DISEASE3")
    private String disease3;

    /** LDISEASE1 - LDISEASE1-出院病情(出院主诊断对应的疾病编码) */
	@Field(value="LDISEASE1")
    private String ldisease1;

    /** LDISEASE2 - LDISEASE2-出院病情(出院次诊断对应的疾病编码) */
	@Field(value="LDISEASE2")
    private String ldisease2;

    /** LDISEASE3 - LDISEASE3-出院病情(出院三诊断对应的疾病编码) */
	@Field(value="LDISEASE3")
    private String ldisease3;

    /** LREASON - LREASON-出院原因(康复；转院；死亡；其他) */
	@Field(value="LREASON")
    private String lreason;

    /** INHOSNO - INHOSNO-病人的住院号 */
	@Field(value="INHOSNO")
    private String inhosno;

    /** BEDNO - BEDNO-病人的床位号（没有床位管理的医院传入空字符串即可） */
	@Field(value="BEDNO")
    private String bedno;

    /** INPATCOUNT - INPATCOUNT-本年度住院次数 */
	@Field(value="INPATCOUNT")
    private String inpatcount;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

    /** PK_PV - 门诊挂号信息 */
	@Field(value="PK_PV")
    private String pkPv;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getRegtype(){
        return this.regtype;
    }
    public void setRegtype(String regtype){
        this.regtype = regtype;
    }

    public String getReccode(){
        return this.reccode;
    }
    public void setReccode(String reccode){
        this.reccode = reccode;
    }

    public String getPersonno(){
        return this.personno;
    }
    public void setPersonno(String personno){
        this.personno = personno;
    }

    public String getDiseaseno1(){
        return this.diseaseno1;
    }
    public void setDiseaseno1(String diseaseno1){
        this.diseaseno1 = diseaseno1;
    }

    public String getDiseaseno2(){
        return this.diseaseno2;
    }
    public void setDiseaseno2(String diseaseno2){
        this.diseaseno2 = diseaseno2;
    }

    public String getDiseaseno3(){
        return this.diseaseno3;
    }
    public void setDiseaseno3(String diseaseno3){
        this.diseaseno3 = diseaseno3;
    }

    public String getRegdate(){
        return this.regdate;
    }
    public void setRegdate(String regdate){
        this.regdate = regdate;
    }

    public String getInpatoperator(){
        return this.inpatoperator;
    }
    public void setInpatoperator(String inpatoperator){
        this.inpatoperator = inpatoperator;
    }

    public String getLeavedate(){
        return this.leavedate;
    }
    public void setLeavedate(String leavedate){
        this.leavedate = leavedate;
    }

    public String getLeaveoperator(){
        return this.leaveoperator;
    }
    public void setLeaveoperator(String leaveoperator){
        this.leaveoperator = leaveoperator;
    }

    public String getDepartment(){
        return this.department;
    }
    public void setDepartment(String department){
        this.department = department;
    }

    public String getMarriage(){
        return this.marriage;
    }
    public void setMarriage(String marriage){
        this.marriage = marriage;
    }

    public String getTransfer(){
        return this.transfer;
    }
    public void setTransfer(String transfer){
        this.transfer = transfer;
    }

    public String getTransferno(){
        return this.transferno;
    }
    public void setTransferno(String transferno){
        this.transferno = transferno;
    }

    public String getLdiseaseno1(){
        return this.ldiseaseno1;
    }
    public void setLdiseaseno1(String ldiseaseno1){
        this.ldiseaseno1 = ldiseaseno1;
    }

    public String getLdiseaseno2(){
        return this.ldiseaseno2;
    }
    public void setLdiseaseno2(String ldiseaseno2){
        this.ldiseaseno2 = ldiseaseno2;
    }

    public String getLdiseaseno3(){
        return this.ldiseaseno3;
    }
    public void setLdiseaseno3(String ldiseaseno3){
        this.ldiseaseno3 = ldiseaseno3;
    }

    public String getDisease1(){
        return this.disease1;
    }
    public void setDisease1(String disease1){
        this.disease1 = disease1;
    }

    public String getDisease2(){
        return this.disease2;
    }
    public void setDisease2(String disease2){
        this.disease2 = disease2;
    }

    public String getDisease3(){
        return this.disease3;
    }
    public void setDisease3(String disease3){
        this.disease3 = disease3;
    }

    public String getLdisease1(){
        return this.ldisease1;
    }
    public void setLdisease1(String ldisease1){
        this.ldisease1 = ldisease1;
    }

    public String getLdisease2(){
        return this.ldisease2;
    }
    public void setLdisease2(String ldisease2){
        this.ldisease2 = ldisease2;
    }

    public String getLdisease3(){
        return this.ldisease3;
    }
    public void setLdisease3(String ldisease3){
        this.ldisease3 = ldisease3;
    }

    public String getLreason(){
        return this.lreason;
    }
    public void setLreason(String lreason){
        this.lreason = lreason;
    }

    public String getInhosno(){
        return this.inhosno;
    }
    public void setInhosno(String inhosno){
        this.inhosno = inhosno;
    }

    public String getBedno(){
        return this.bedno;
    }
    public void setBedno(String bedno){
        this.bedno = bedno;
    }

    public String getInpatcount(){
        return this.inpatcount;
    }
    public void setInpatcount(String inpatcount){
        this.inpatcount = inpatcount;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }
}