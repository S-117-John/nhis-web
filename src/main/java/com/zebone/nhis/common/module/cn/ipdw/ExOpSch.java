package com.zebone.nhis.common.module.cn.ipdw;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: EX_OP_SCH 
 *
 * @since 2019-03-26 06:46:04
 */
@Table(value="EX_OP_SCH")
public class ExOpSch extends BaseModule  {

	@PK
	@Field(value="PK_OPSCH",id=KeyId.UUID)
    private String pkOpsch;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="CODE_APPLY")
    private String codeApply;

	@Field(value="DATE_APPLY")
    private Date dateApply;

	@Field(value="EU_OPTYPE")
    private String euOptype;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="NAME_DEPT")
    private String nameDept;

	@Field(value="ROOM_NO")
    private String roomNo;
	
	@Field(value="ROOM_PLACE")
	private String roomPlace;

	@Field(value="TICKETNO")
    private Integer ticketno;

	@Field(value="NAME_PI")
    private String namePi;

	@Field(value="CODE_IP")
    private String codeIp;

	@Field(value="DT_SEX")
    private String dtSex;

	@Field(value="AGE_PV")
    private String agePv;

	@Field(value="BED_NO")
    private String bedNo;

	@Field(value="NAME_OPT")
    private String nameOpt;

	@Field(value="PK_EMP")
    private String pkEmp;

	public String getRoomPlace() {
		return roomPlace;
	}
	public void setRoomPlace(String roomPlace) {
		this.roomPlace = roomPlace;
	}
	public String getPkEmpAsst() {
		return pkEmpAsst;
	}
	public void setPkEmpAsst(String pkEmpAsst) {
		this.pkEmpAsst = pkEmpAsst;
	}
	public String getNameEmpAsst() {
		return nameEmpAsst;
	}
	public void setNameEmpAsst(String nameEmpAsst) {
		this.nameEmpAsst = nameEmpAsst;
	}
	@Field(value="NAME_EMP")
    private String nameEmp;
	
	@Field(value="pk_emp_asst")
    private String pkEmpAsst;

	@Field(value="name_emp_asst")
    private String nameEmpAsst;
	

	@Field(value="SPEC_EQUIPMENT")
    private String specEquipment;

	@Field(value="PK_DEPT_OP")
    private String pkDeptOp;

	@Field(value="NAME_DEPT_OP")
    private String nameDeptOp;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_ORGAREA")
    private String pkOrgarea;

	@Field(value="NAME_ORGAREA")
    private String nameOrgarea;

	@Field(value="DATE_SCH")
    private Date dateSch;

	@Field(value="PK_DEPT_SCH")
    private String pkDeptSch;

	@Field(value="PK_EMP_SCH")
    private String pkEmpSch;

	@Field(value="NAME_EMP_SCH")
    private String nameEmpSch;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

	@Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOpsch(){
        return this.pkOpsch;
    }
    public void setPkOpsch(String pkOpsch){
        this.pkOpsch = pkOpsch;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getCodeApply(){
        return this.codeApply;
    }
    public void setCodeApply(String codeApply){
        this.codeApply = codeApply;
    }

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public String getEuOptype(){
        return this.euOptype;
    }
    public void setEuOptype(String euOptype){
        this.euOptype = euOptype;
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

    public String getRoomNo(){
        return this.roomNo;
    }
    public void setRoomNo(String roomNo){
        this.roomNo = roomNo;
    }

    public Integer getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(Integer ticketno){
        this.ticketno = ticketno;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public String getCodeIp(){
        return this.codeIp;
    }
    public void setCodeIp(String codeIp){
        this.codeIp = codeIp;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public String getAgePv(){
        return this.agePv;
    }
    public void setAgePv(String agePv){
        this.agePv = agePv;
    }

    public String getBedNo(){
        return this.bedNo;
    }
    public void setBedNo(String bedNo){
        this.bedNo = bedNo;
    }

    public String getNameOpt(){
        return this.nameOpt;
    }
    public void setNameOpt(String nameOpt){
        this.nameOpt = nameOpt;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public String getSpecEquipment(){
        return this.specEquipment;
    }
    public void setSpecEquipment(String specEquipment){
        this.specEquipment = specEquipment;
    }

    public String getPkDeptOp(){
        return this.pkDeptOp;
    }
    public void setPkDeptOp(String pkDeptOp){
        this.pkDeptOp = pkDeptOp;
    }

    public String getNameDeptOp(){
        return this.nameDeptOp;
    }
    public void setNameDeptOp(String nameDeptOp){
        this.nameDeptOp = nameDeptOp;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkOrgarea(){
        return this.pkOrgarea;
    }
    public void setPkOrgarea(String pkOrgarea){
        this.pkOrgarea = pkOrgarea;
    }

    public String getNameOrgarea(){
        return this.nameOrgarea;
    }
    public void setNameOrgarea(String nameOrgarea){
        this.nameOrgarea = nameOrgarea;
    }

    public Date getDateSch(){
        return this.dateSch;
    }
    public void setDateSch(Date dateSch){
        this.dateSch = dateSch;
    }

    public String getPkDeptSch(){
        return this.pkDeptSch;
    }
    public void setPkDeptSch(String pkDeptSch){
        this.pkDeptSch = pkDeptSch;
    }

    public String getPkEmpSch(){
        return this.pkEmpSch;
    }
    public void setPkEmpSch(String pkEmpSch){
        this.pkEmpSch = pkEmpSch;
    }

    public String getNameEmpSch(){
        return this.nameEmpSch;
    }
    public void setNameEmpSch(String nameEmpSch){
        this.nameEmpSch = nameEmpSch;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getPkEmpCanc(){
        return this.pkEmpCanc;
    }
    public void setPkEmpCanc(String pkEmpCanc){
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc(){
        return this.nameEmpCanc;
    }
    public void setNameEmpCanc(String nameEmpCanc){
        this.nameEmpCanc = nameEmpCanc;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}