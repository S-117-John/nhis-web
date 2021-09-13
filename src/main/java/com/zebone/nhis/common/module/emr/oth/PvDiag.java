package com.zebone.nhis.common.module.emr.oth;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_DIAG  - 患者就诊_临床综合诊断 
 *
 * @since 2016-09-12 09:50:56
 */
@Table(value="PV_DIAG")
public class PvDiag extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_PVDIAG - 就诊诊断主键 */
	@PK
	@Field(value="PK_PVDIAG",id=KeyId.UUID)
    private String pkPvdiag;

    /** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    /** SORT_NO - 诊断序号 */
	@Field(value="SORT_NO")
    private Integer sortNo;

    /** DT_DIAGTYPE - 诊断类型 
     *  0X 门诊过程：00 门诊诊断。
	 *	1X 住院过程：10  入院诊断，11 手术诊断，12 出院诊断。
	 *	2X 医技辅助：20 放射诊断，21 病理诊断
	 */
	@Field(value="DT_DIAGTYPE")
    private String dtDiagtype;

    /** PK_DIAG - 诊断编码 */
	@Field(value="PK_DIAG")
    private String pkDiag;

    /** DESC_DIAG - 诊断描述 */
	@Field(value="DESC_DIAG")
    private String descDiag;

    /** FLAG_MAJ - 主诊断标志 */
	@Field(value="FLAG_MAJ")
    private String flagMaj;

    /** FLAG_SUSP - 疑似诊断标志 */
	@Field(value="FLAG_SUSP")
    private String flagSusp;

    /** FLAG_CONTAGION - 传染病标志 */
	@Field(value="FLAG_CONTAGION")
    private String flagContagion;

    /** DATE_DIAG - 诊断日期 */
	@Field(value="DATE_DIAG")
    private Date dateDiag;

    /** PK_EMP_DIAG - 诊断医生 */
	@Field(value="PK_EMP_DIAG")
    private String pkEmpDiag;

    /** NAME_EMP_DIAG - 诊断医生姓名 */
	@Field(value="NAME_EMP_DIAG")
    private String nameEmpDiag;

    /** FLAG_FINALLY - 确诊标志 */
	@Field(value="FLAG_FINALLY")
    private String flagFinally;

    /** FLAG_CURE - 治愈标志 */
	@Field(value="FLAG_CURE")
    private String flagCure;

	/** 诊断名称（标准诊断编码表的字段） */
	private String nameDiag;

	/** 诊断编码（标准诊断编码表的字段） */
	@Field(value="CODE_ICD")
	private String codeDiag;

	private String status;
	
    public String getPkPvdiag(){
        return this.pkPvdiag;
    }
    public void setPkPvdiag(String pkPvdiag){
        this.pkPvdiag = pkPvdiag;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getDtDiagtype(){
        return this.dtDiagtype;
    }
    public void setDtDiagtype(String dtDiagtype){
        this.dtDiagtype = dtDiagtype;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getDescDiag(){
        return this.descDiag;
    }
    public void setDescDiag(String descDiag){
        this.descDiag = descDiag;
    }

    public String getFlagMaj(){
        return this.flagMaj;
    }
    public void setFlagMaj(String flagMaj){
        this.flagMaj = flagMaj;
    }

    public String getFlagSusp(){
        return this.flagSusp;
    }
    public void setFlagSusp(String flagSusp){
        this.flagSusp = flagSusp;
    }

    public String getFlagContagion(){
        return this.flagContagion;
    }
    public void setFlagContagion(String flagContagion){
        this.flagContagion = flagContagion;
    }

    public Date getDateDiag(){
        return this.dateDiag;
    }
    public void setDateDiag(Date dateDiag){
        this.dateDiag = dateDiag;
    }

    public String getPkEmpDiag(){
        return this.pkEmpDiag;
    }
    public void setPkEmpDiag(String pkEmpDiag){
        this.pkEmpDiag = pkEmpDiag;
    }

    public String getNameEmpDiag(){
        return this.nameEmpDiag;
    }
    public void setNameEmpDiag(String nameEmpDiag){
        this.nameEmpDiag = nameEmpDiag;
    }

    public String getFlagFinally(){
        return this.flagFinally;
    }
    public void setFlagFinally(String flagFinally){
        this.flagFinally = flagFinally;
    }

    public String getFlagCure(){
        return this.flagCure;
    }
    public void setFlagCure(String flagCure){
        this.flagCure = flagCure;
    }

	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
	public String getCodeDiag() {
		return codeDiag;
	}
	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}