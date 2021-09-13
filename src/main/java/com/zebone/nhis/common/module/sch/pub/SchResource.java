package com.zebone.nhis.common.module.sch.pub;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: SCH_RESOURCE  - sch_resource 
 *
 * @since 2016-09-20 01:13:08
 */
@Table(value="SCH_RESOURCE")
public class SchResource extends BaseModule  {

	
	@PK
	@Field(value="PK_SCHRES",id=KeyId.UUID)
    private String pkSchres;

    /** EU_SCHCLASS - 0 门诊出诊；1 医技排班；2 床位预约；3 手术排班 */
	@Field(value="EU_SCHCLASS")
    private String euSchclass;

    /** EU_RESTYPE - 0 部门；1 人员；2 手术台；3 床位；4 医技 */
	@Field(value="EU_RESTYPE")
    private String euRestype;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="DESC_RES")
    private String descRes;

	@Field(value="SPEC")
    private String spec;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="MINUTE_PER")
    private Long minutePer;

	@Field(value="PK_TICKETRULES")
    private String pkTicketrules;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="PK_BED")
    private String pkBed;

	@Field(value="PK_OPT")
    private String pkOpt;

	@Field(value="PK_MSP")
    private String pkMsp;

	@Field(value="PK_DEPT_BELONG")
	private String pkDeptBelong;
	
	@Field(value="FLAG_STOP")
    private String flagStop;

    /** PRICE - 用于床位预约 */
	@Field(value="PRICE")
    private Double price;

    /** HOUSE_NO - 用于床位预约 */
	@Field(value="HOUSE_NO")
    private String houseNo;

    /** DT_SEX - 用于床位预约,0或空代表不受限制 */
	@Field(value="DT_SEX")
    private String dtSex;

	@Field(value="PK_SCHSRV")
    private String pkSchsrv;
	
	@Field(value="DT_DATESLOTTYPE")
	private String dtDateslottype;
	
	@Field(value="PK_FATHER")
    private String pkFather;
	
	@Field(value="DT_CNLEVEL")
    private String dtCnlevel;

    @Field(value="FLAG_SPECDISE")
    private String flagSpecdise;

    @Field(value="PK_DEPT_AREA")
    private String pkDeptArea;

    @Field(value="SORTNO")
    private String sortno;

    @Field(value = "FLAG_SHARE")
    private String flagShare;

    public String getSortno() {
        return sortno;
    }

    public void setSortno(String sortno) {
        this.sortno = sortno;
    }

    public String getPkFather() {
		return pkFather;
	}
	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}
	public String getPkSchres(){
        return this.pkSchres;
    }
    public void setPkSchres(String pkSchres){
        this.pkSchres = pkSchres;
    }

    public String getEuSchclass(){
        return this.euSchclass;
    }
    public void setEuSchclass(String euSchclass){
        this.euSchclass = euSchclass;
    }

    public String getEuRestype(){
        return this.euRestype;
    }
    public void setEuRestype(String euRestype){
        this.euRestype = euRestype;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getDescRes(){
        return this.descRes;
    }
    public void setDescRes(String descRes){
        this.descRes = descRes;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public Long getMinutePer(){
        return this.minutePer;
    }
    public void setMinutePer(Long minutePer){
        this.minutePer = minutePer;
    }

    public String getPkTicketrules(){
        return this.pkTicketrules;
    }
    public void setPkTicketrules(String pkTicketrules){
        this.pkTicketrules = pkTicketrules;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getPkBed(){
        return this.pkBed;
    }
    public void setPkBed(String pkBed){
        this.pkBed = pkBed;
    }

    public String getPkOpt(){
        return this.pkOpt;
    }
    public void setPkOpt(String pkOpt){
        this.pkOpt = pkOpt;
    }

    public String getPkMsp(){
        return this.pkMsp;
    }
    public void setPkMsp(String pkMsp){
        this.pkMsp = pkMsp;
    }

    public String getPkDeptBelong() {
		return pkDeptBelong;
	}
	public void setPkDeptBelong(String pkDeptBelong) {
		this.pkDeptBelong = pkDeptBelong;
	}
	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public String getHouseNo(){
        return this.houseNo;
    }
    public void setHouseNo(String houseNo){
        this.houseNo = houseNo;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public String getPkSchsrv(){
        return this.pkSchsrv;
    }
    public void setPkSchsrv(String pkSchsrv){
        this.pkSchsrv = pkSchsrv;
    }
	public String getDtDateslottype() {
		return dtDateslottype;
	}
	public void setDtDateslottype(String dtDateslottype) {
		this.dtDateslottype = dtDateslottype;
	}
	public String getDtCnlevel() {
		return dtCnlevel;
	}
	public void setDtCnlevel(String dtCnlevel) {
		this.dtCnlevel = dtCnlevel;
	}

    public String getFlagSpecdise() {
        return flagSpecdise;
    }

    public void setFlagSpecdise(String flagSpecdise) {
        this.flagSpecdise = flagSpecdise;
    }

    public String getPkDeptArea() {
        return pkDeptArea;
    }

    public void setPkDeptArea(String pkDeptArea) {
        this.pkDeptArea = pkDeptArea;
    }

    public String getFlagShare() {
        return flagShare;
    }

    public void setFlagShare(String flagShare) {
        this.flagShare = flagShare;
    }
}