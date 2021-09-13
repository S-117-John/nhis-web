package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_ORDER_PRINT 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_ORDER_PRINT")
public class CnOrderPrint extends BaseModule  {

	@PK
	@Field(value="PK_ORDPRINT",id=KeyId.UUID)
    private String pkOrdprint;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="DATE_START")
    private Date dateStart;

	@Field(value="PK_EMP_ORD")
    private String pkEmpOrd;

	@Field(value="NAME_EMP_ORD")
    private String nameEmpOrd;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="DESC_ORD")
    private String descOrd;

	@Field(value="DATE_STOP")
    private Date dateStop;

	@Field(value="PK_EMP_STOP")
    private String pkEmpStop;

	@Field(value="NAME_EMP_STOP")
    private String nameEmpStop;

	@Field(value="PK_EMP_STOP_CHK")
    private String pkEmpStopChk;

	@Field(value="NAME_EMP_STOP_CHK")
    private String nameEmpStopChk;

	@Field(value="PAGE_NO")
    private Integer pageNo;

	@Field(value="ROW_NO")
    private Integer rowNo;

	@Field(value="EU_ALWAYS")
    private String euAlways;

	@Field(value="FLAG_ERASE")
    private String flagErase;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="PRINT_NO")
    private Integer printNo;

	@Field(value="PINAME")
    private String piname;

	@Field(value="SEX")
    private String sex;

	@Field(value="AGE")
    private String age;

	@Field(value="BEDNO")
    private String bedno;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="NAME_DEPT")
    private String nameDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="NAME_DEPT_NS")
    private String nameDeptNs;

	@Field(value="PK_EMP_PRINT")
    private String pkEmpPrint;

	@Field(value="NAME_EMP_PRINT")
    private String nameEmpPrint;
	
	@Field(value="COMBINE_TIME")
    private Date combineTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="FLAG_EX")
    private String flagEx;

    @Field(value="DATE_PLAN_EX")
    private Date datePlanEx;
    @Field(value="NAME_ORD")
    private String nameOrd;
    @Field(value="NAME_EMP_EX")
    private String nameEmpEx;
    @Field(value="PK_EMP_EX")
    private String pkEmpEx;


    public String getPkOrdprint(){
        return this.pkOrdprint;
    }
    public void setPkOrdprint(String pkOrdprint){
        this.pkOrdprint = pkOrdprint;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
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

    public Date getDateStart(){
        return this.dateStart;
    }
    public void setDateStart(Date dateStart){
        this.dateStart = dateStart;
    }

    public String getPkEmpOrd(){
        return this.pkEmpOrd;
    }
    public void setPkEmpOrd(String pkEmpOrd){
        this.pkEmpOrd = pkEmpOrd;
    }

    public String getNameEmpOrd(){
        return this.nameEmpOrd;
    }
    public void setNameEmpOrd(String nameEmpOrd){
        this.nameEmpOrd = nameEmpOrd;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public String getDescOrd(){
        return this.descOrd;
    }
    public void setDescOrd(String descOrd){
        this.descOrd = descOrd;
    }

    public Date getDateStop(){
        return this.dateStop;
    }
    public void setDateStop(Date dateStop){
        this.dateStop = dateStop;
    }

    public String getPkEmpStop(){
        return this.pkEmpStop;
    }
    public void setPkEmpStop(String pkEmpStop){
        this.pkEmpStop = pkEmpStop;
    }

    public String getNameEmpStop(){
        return this.nameEmpStop;
    }
    public void setNameEmpStop(String nameEmpStop){
        this.nameEmpStop = nameEmpStop;
    }

    public String getPkEmpStopChk(){
        return this.pkEmpStopChk;
    }
    public void setPkEmpStopChk(String pkEmpStopChk){
        this.pkEmpStopChk = pkEmpStopChk;
    }

    public String getNameEmpStopChk(){
        return this.nameEmpStopChk;
    }
    public void setNameEmpStopChk(String nameEmpStopChk){
        this.nameEmpStopChk = nameEmpStopChk;
    }

    public Integer getPageNo(){
        return this.pageNo;
    }
    public void setPageNo(Integer pageNo){
        this.pageNo = pageNo;
    }

    public Integer getRowNo(){
        return this.rowNo;
    }
    public void setRowNo(Integer rowNo){
        this.rowNo = rowNo;
    }

    public String getEuAlways(){
        return this.euAlways;
    }
    public void setEuAlways(String euAlways){
        this.euAlways = euAlways;
    }

    public String getFlagErase(){
        return this.flagErase;
    }
    public void setFlagErase(String flagErase){
        this.flagErase = flagErase;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Integer getPrintNo(){
        return this.printNo;
    }
    public void setPrintNo(Integer printNo){
        this.printNo = printNo;
    }

    public String getPiname(){
        return this.piname;
    }
    public void setPiname(String piname){
        this.piname = piname;
    }

    public String getSex(){
        return this.sex;
    }
    public void setSex(String sex){
        this.sex = sex;
    }

    public String getAge(){
        return this.age;
    }
    public void setAge(String age){
        this.age = age;
    }

    public String getBedno(){
        return this.bedno;
    }
    public void setBedno(String bedno){
        this.bedno = bedno;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getNameDept(){
        return this.nameDept;
    }
    public void setNameDept(String nameDept){
        this.nameDept = nameDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public String getNameDeptNs(){
        return this.nameDeptNs;
    }
    public void setNameDeptNs(String nameDeptNs){
        this.nameDeptNs = nameDeptNs;
    }

    public String getPkEmpPrint(){
        return this.pkEmpPrint;
    }
    public void setPkEmpPrint(String pkEmpPrint){
        this.pkEmpPrint = pkEmpPrint;
    }

    public String getNameEmpPrint(){
        return this.nameEmpPrint;
    }
    public void setNameEmpPrint(String nameEmpPrint){
        this.nameEmpPrint = nameEmpPrint;
    }

    public Date getCombineTime() {
		return combineTime;
	}
	public void setCombineTime(Date combineTime) {
		this.combineTime = combineTime;
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

    public String getFlagEx() {
        return flagEx;
    }

    public void setFlagEx(String flagEx) {
        this.flagEx = flagEx;
    }

    public Date getDatePlanEx() {
        return datePlanEx;
    }

    public void setDatePlanEx(Date datePlanEx) {
        this.datePlanEx = datePlanEx;
    }

    public String getNameOrd() {
        return nameOrd;
    }

    public void setNameOrd(String nameOrd) {
        this.nameOrd = nameOrd;
    }

    public String getNameEmpEx() {
        return nameEmpEx;
    }

    public void setNameEmpEx(String nameEmpEx) {
        this.nameEmpEx = nameEmpEx;
    }

    public String getPkEmpEx() {
        return pkEmpEx;
    }

    public void setPkEmpEx(String pkEmpEx) {
        this.pkEmpEx = pkEmpEx;
    }
}