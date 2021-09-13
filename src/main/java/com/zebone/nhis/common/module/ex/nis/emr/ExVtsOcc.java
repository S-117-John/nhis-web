package com.zebone.nhis.common.module.ex.nis.emr;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: EX_VTS_OCC - ex_vts_occ
 * 
 * @since 2016-10-10 11:54:24
 */
@Table(value = "EX_VTS_OCC")
public class ExVtsOcc extends BaseModule {

	@PK
	@Field(value = "PK_VTSOCC", id = KeyId.UUID)
	private String pkVtsocc;

	@Field(value = "PK_PI")
	private String pkPi;

	@Field(value = "PK_PV")
	private String pkPv;

	@Field(value = "DATE_VTS")
	private Date dateVts;

	/** EU_STOOLTYPE - 0 正常，1 失禁 ，2人工肛门*/
	@Field(value = "EU_STOOLTYPE")
	private String euStooltype;

	@Field(value = "VAL_STOOL")
	private Long valStool;

	@Field(value = "FLAG_COLO")
	private String flagColo;

	@Field(value = "VAL_STOOL_COLO")
	private Long valStoolColo;

	@Field(value = "VAL_URINE")
	private String valUrine;

	@Field(value = "VAL_GAST")
	private Long valGast;

	@Field(value = "VAL_GALL")
	private Long valGall;

	@Field(value = "VAL_DRAINAGE")
	private Long valDrainage;

	@Field(value = "VAL_OUTPUT")
	private String valOutput;

	@Field(value = "VAL_INPUT")
	private String valInput;

	@Field(value = "VAL_WEIGHT")
	private String valWeight;

	@Field(value = "VAL_SBP")
	private Long valSbp;

	@Field(value = "VAL_DBP")
	private Long valDbp;

	@Field(value = "VAL_SBP_ADD")
	private Long valSbpAdd;

	@Field(value = "VAL_DBP_ADD")
	private Long valDbpAdd;

	@Field(value = "VAL_AL")
	private String valAl;

	@Field(value = "VAL_ST")
	private String valSt;

	@Field(value = "VAL_OTH")
	private String valOth;

	@Field(value = "PK_DEPT_INPUT")
	private String pkDeptInput;

	@Field(value = "DATE_INPUT")
	private Date dateInput;

	@Field(value = "PK_EMP_INPUT")
	private String pkEmpInput;

	@Field(value = "NAME_EMP_INPUT")
	private String nameEmpInput;

	@Field(value = "MODITY_TIME")
	private Date modityTime;

	@Field(value = "INFANT_NO")
	private String infantNo;
	/**
	 * 其他排出类型
	 */
	@Field(value = "DT_OUTPUTTYPE")
	private String dtOutputtype;
	/**
	 * 其他排出量小时数
	 */
	@Field(value = "HOUR_OUTPUT")
	private Integer hourOutput;
	/**
	 * 总出量小时数
	 */
	@Field(value = "HOUR_OUTPUT_TOTAL")
	private Integer hourOutputTotal;
	/**
	 * 液体入量小时数
	 */
	@Field(value = "HOUR_INPUT")
	private Integer hourInput;
	
	/**
	 * 小便排出量小时数
	 */
	@Field(value = "HOUR_URINE")
	private Integer hourUrine;
	
	@Field(value="MODIFIER")
	private String modifier;
	
	@Field(value="VAl_OUTPUT_TOTAL")
	private String valOutputTotal;
	
	public String getInfantNo() {
		return infantNo;
	}

	public void setInfantNo(String infantNo) {
		this.infantNo = infantNo;
	}

	public String getPkVtsocc() {
		return this.pkVtsocc;
	}

	public void setPkVtsocc(String pkVtsocc) {
		this.pkVtsocc = pkVtsocc;
	}

	public String getPkPi() {
		return this.pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return this.pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public Date getDateVts() {
		return this.dateVts;
	}

	public void setDateVts(Date dateVts) {
		this.dateVts = dateVts;
	}

	public String getEuStooltype() {
		return this.euStooltype;
	}

	public void setEuStooltype(String euStooltype) {
		this.euStooltype = euStooltype;
	}

	public Long getValStool() {
		return this.valStool;
	}

	public void setValStool(Long valStool) {
		this.valStool = valStool;
	}

	public String getFlagColo() {
		return this.flagColo;
	}

	public void setFlagColo(String flagColo) {
		this.flagColo = flagColo;
	}

	public Long getValStoolColo() {
		return this.valStoolColo;
	}

	public void setValStoolColo(Long valStoolColo) {
		this.valStoolColo = valStoolColo;
	}

	public String getValUrine() {
		return this.valUrine;
	}

	public void setValUrine(String valUrine) {
		this.valUrine = valUrine;
	}

	public Long getValGast() {
		return this.valGast;
	}

	public void setValGast(Long valGast) {
		this.valGast = valGast;
	}

	public Long getValGall() {
		return this.valGall;
	}

	public void setValGall(Long valGall) {
		this.valGall = valGall;
	}

	public Long getValDrainage() {
		return this.valDrainage;
	}

	public void setValDrainage(Long valDrainage) {
		this.valDrainage = valDrainage;
	}

	public String getValOutput() {
		return this.valOutput;
	}

	public void setValOutput(String valOutput) {
		this.valOutput = valOutput;
	}

	public String getValInput() {
		return this.valInput;
	}

	public void setValInput(String valInput) {
		this.valInput = valInput;
	}

	public String getValWeight() {
		return valWeight;
	}

	public void setValWeight(String valWeight) {
		this.valWeight = valWeight;
	}

	public Long getValSbp() {
		return this.valSbp;
	}

	public void setValSbp(Long valSbp) {
		this.valSbp = valSbp;
	}

	public Long getValDbp() {
		return this.valDbp;
	}

	public void setValDbp(Long valDbp) {
		this.valDbp = valDbp;
	}

	public Long getValSbpAdd() {
		return this.valSbpAdd;
	}

	public void setValSbpAdd(Long valSbpAdd) {
		this.valSbpAdd = valSbpAdd;
	}

	public Long getValDbpAdd() {
		return this.valDbpAdd;
	}

	public void setValDbpAdd(Long valDbpAdd) {
		this.valDbpAdd = valDbpAdd;
	}

	public String getValAl() {
		return this.valAl;
	}

	public void setValAl(String valAl) {
		this.valAl = valAl;
	}

	public String getValSt() {
		return this.valSt;
	}

	public void setValSt(String valSt) {
		this.valSt = valSt;
	}

	public String getValOth() {
		return this.valOth;
	}

	public void setValOth(String valOth) {
		this.valOth = valOth;
	}

	public String getPkDeptInput() {
		return this.pkDeptInput;
	}

	public void setPkDeptInput(String pkDeptInput) {
		this.pkDeptInput = pkDeptInput;
	}

	public Date getDateInput() {
		return this.dateInput;
	}

	public void setDateInput(Date dateInput) {
		this.dateInput = dateInput;
	}

	public String getPkEmpInput() {
		return this.pkEmpInput;
	}

	public void setPkEmpInput(String pkEmpInput) {
		this.pkEmpInput = pkEmpInput;
	}

	public String getNameEmpInput() {
		return this.nameEmpInput;
	}

	public void setNameEmpInput(String nameEmpInput) {
		this.nameEmpInput = nameEmpInput;
	}

	public Date getModityTime() {
		return this.modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getDtOutputtype() {
		return dtOutputtype;
	}

	public void setDtOutputtype(String dtOutputtype) {
		this.dtOutputtype = dtOutputtype;
	}

	public Integer getHourOutput() {
		return hourOutput;
	}

	public void setHourOutput(Integer hourOutput) {
		this.hourOutput = hourOutput;
	}

	public Integer getHourOutputTotal() {
		return hourOutputTotal;
	}

	public void setHourOutputTotal(Integer hourOutputTotal) {
		this.hourOutputTotal = hourOutputTotal;
	}

	public Integer getHourInput() {
		return hourInput;
	}

	public void setHourInput(Integer hourInput) {
		this.hourInput = hourInput;
	}

	public Integer getHourUrine() {
		return hourUrine;
	}

	public void setHourUrine(Integer hourUrine) {
		this.hourUrine = hourUrine;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getValOutputTotal() {
		return valOutputTotal;
	}

	public void setValOutputTotal(String valOutputTotal) {
		this.valOutputTotal = valOutputTotal;
	}	
	

}