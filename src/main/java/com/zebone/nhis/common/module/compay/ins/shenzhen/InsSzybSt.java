package com.zebone.nhis.common.module.compay.ins.shenzhen;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: INS_SZYB_ST - ins_szyb_st 
 *
 * @since 2019-12-11 04:36:12
 */
@Table(value="INS_SZYB_ST")
public class InsSzybSt   {

    /** PK_INSST - 主键 */
	@PK
	@Field(value="PK_INSST",id=KeyId.UUID)
    private String pkInsst;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_VISIT")
    private String pkVisit;

	@Field(value="CODE_HPST")
    private String codeHpst;

	@Field(value="PK_HP")
    private String pkHp;

    /** PK_PV - 病人就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

    /** DATE_INP - 入院日期 */
	@Field(value="DATE_INP")
    private Date dateInp;

    /** DATE_OUTP - 出院日期 */
	@Field(value="DATE_OUTP")
    private Date dateOutp;

    /** DAYS - 住院天数 */
	@Field(value="DAYS")
    private Integer days;

    /** PK_SETTLE - 结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;

	@Field(value="PVCODE_INS")
    private String pvcodeIns;

	@Field(value="DATE_ST")
    private Date dateSt;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="CODE_CENTER")
    private String codeCenter;

    /** CODE_ORG - 定点医疗机构编号 */
	@Field(value="CODE_ORG")
    private String codeOrg;

    /** NAME_ORG - 定点医疗机构名称 */
	@Field(value="NAME_ORG")
    private String nameOrg;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="code_serialno")
    private String codeSerialno;

    @Field(value="BILLNO")
    private String billno;

    /**
     * 出院登记流水号用于出院登记回退
     */
    @Field(value="transid")
    private String transid;
    
   /**
   *  医疗类别
   */
    @Field(value="PERSONTYPE")
    private String persontype;
	
	@Field(date=FieldType.ALL)
    private Date ts;

    public String getPersontype() {
		return persontype;
	}

	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}

	public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getCodeSerialno() {
		return codeSerialno;
	}
	public void setCodeSerialno(String codeSerialno) {
		this.codeSerialno = codeSerialno;
	}
	public String getPkInsst(){
        return this.pkInsst;
    }
    public void setPkInsst(String pkInsst){
        this.pkInsst = pkInsst;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkVisit(){
        return this.pkVisit;
    }
    public void setPkVisit(String pkVisit){
        this.pkVisit = pkVisit;
    }

    public String getCodeHpst(){
        return this.codeHpst;
    }
    public void setCodeHpst(String codeHpst){
        this.codeHpst = codeHpst;
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

    public Date getDateInp(){
        return this.dateInp;
    }
    public void setDateInp(Date dateInp){
        this.dateInp = dateInp;
    }

    public Date getDateOutp(){
        return this.dateOutp;
    }
    public void setDateOutp(Date dateOutp){
        this.dateOutp = dateOutp;
    }

    public Integer getDays(){
        return this.days;
    }
    public void setDays(Integer days){
        this.days = days;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getPvcodeIns(){
        return this.pvcodeIns;
    }
    public void setPvcodeIns(String pvcodeIns){
        this.pvcodeIns = pvcodeIns;
    }

    public Date getDateSt(){
        return this.dateSt;
    }
    public void setDateSt(Date dateSt){
        this.dateSt = dateSt;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
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

    public String getNameOrg(){
        return this.nameOrg;
    }
    public void setNameOrg(String nameOrg){
        this.nameOrg = nameOrg;
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