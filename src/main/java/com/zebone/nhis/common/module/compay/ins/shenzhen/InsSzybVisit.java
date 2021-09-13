package com.zebone.nhis.common.module.compay.ins.shenzhen;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: INS_SZYB_VISIT  - ins_szyb_visit
 *
 * @since 2019-12-05 04:35:32
 */
@Table(value="INS_SZYB_VISIT")
public class InsSzybVisit extends BaseModule  {

    @PK
    @Field(value="PK_VISIT",id=KeyId.UUID)
    private String pkVisit;

    @Field(value="PK_HP")
    private String pkHp;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="PK_PI")
    private String pkPi;

    @Field(value="CODE_CENTER")
    private String codeCenter;

    @Field(value="CODE_ORG")
    private String codeOrg;

    /** PVCODE_INS - 定点协议机构编码+HIS端住院流水号 */
    @Field(value="PVCODE_INS")
    private String pvcodeIns;

    /** NAME_ORG - 定点医疗机构名称 */
    @Field(value="NAME_ORG")
    private String nameOrg;

    @Field(value="NAME_PI")
    private String namePi;

    @Field(value="PERSONTYPE")
    private String persontype;

    @Field(value="BIRTH_DATE")
    private Date birthDate;

    @Field(value="IDNO")
    private String idno;

    /** DT_EXTHP - 码表040006 */
    @Field(value="DT_EXTHP")
    private String dtExthp;

    /** CODE_AREAYD - 用于保存异地医保参保地 */
    @Field(value="CODE_AREAYD")
    private String codeAreayd;

    @Field(value="DATE_REG")
    private Date dateReg;

    @Field(value="DT_STTYPE_INS")
    private String dtSttypeIns;

    /** EU_STATUS_ST - 门诊：0结算中（挂号登记开始） 1结算完成（挂号登记完成）,住院：0结算中（登记完成） 1结算完成（出院或中途结算完成） */
    @Field(value="EU_STATUS_ST")
    private String euStatusSt;

    @Field(value="NOTE")
    private String note;

    /** 救助对象类型 */
    @Field(value="EU_RESCTYPE")
    private String euResctype;

    /** 原交易流水号 */
    @Field(value="TRANSID")
    private String transid;

    
    /** 医药机构结算业务序列号*/
    @Field(value="CODE_INSST")
    private String codeInsst;
    
    
    /** 医药机构结算业务序列号*/
    @Field(value="CODE_MEDINO")
    private String codeMedino;
    
    /** 医保卡密码*/
    @Field(value="PASSWORD")
    private String password;

    /**证件类型*/
    @Field(value="DT_IDTYPE")
    private String dtIdtype;

    public String getDtIdtype() {
        return dtIdtype;
    }

    public void setDtIdtype(String dtIdtype) {
        this.dtIdtype = dtIdtype;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCodeMedino() {
		return codeMedino;
	}

	public void setCodeMedino(String codeMedino) {
		this.codeMedino = codeMedino;
	}

	public String getCodeInsst() {
		return codeInsst;
	}

	public void setCodeInsst(String codeInsst) {
		this.codeInsst = codeInsst;
	}

	public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getEuResctype() {
        return euResctype;
    }

    public void setEuResctype(String euResctype) {
        this.euResctype = euResctype;
    }

    public String getPkVisit(){
        return this.pkVisit;
    }
    public void setPkVisit(String pkVisit){
        this.pkVisit = pkVisit;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCodeCenter(){
        return this.codeCenter;
    }
    public void setCodeCenter(String codeCenter){
        this.codeCenter = codeCenter;
    }

    public String getCodeOrg(){
        return this.codeOrg;
    }
    public void setCodeOrg(String codeOrg){
        this.codeOrg = codeOrg;
    }

    public String getPvcodeIns(){
        return this.pvcodeIns;
    }
    public void setPvcodeIns(String pvcodeIns){
        this.pvcodeIns = pvcodeIns;
    }

    public String getNameOrg(){
        return this.nameOrg;
    }
    public void setNameOrg(String nameOrg){
        this.nameOrg = nameOrg;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public String getPersontype(){
        return this.persontype;
    }
    public void setPersontype(String persontype){
        this.persontype = persontype;
    }

    public Date getBirthDate(){
        return this.birthDate;
    }
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }

    public String getIdno(){
        return this.idno;
    }
    public void setIdno(String idno){
        this.idno = idno;
    }

    public String getDtExthp(){
        return this.dtExthp;
    }
    public void setDtExthp(String dtExthp){
        this.dtExthp = dtExthp;
    }

    public String getCodeAreayd(){
        return this.codeAreayd;
    }
    public void setCodeAreayd(String codeAreayd){
        this.codeAreayd = codeAreayd;
    }

    public Date getDateReg(){
        return this.dateReg;
    }
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }

    public String getDtSttypeIns(){
        return this.dtSttypeIns;
    }
    public void setDtSttypeIns(String dtSttypeIns){
        this.dtSttypeIns = dtSttypeIns;
    }

    public String getEuStatusSt(){
        return this.euStatusSt;
    }
    public void setEuStatusSt(String euStatusSt){
        this.euStatusSt = euStatusSt;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}