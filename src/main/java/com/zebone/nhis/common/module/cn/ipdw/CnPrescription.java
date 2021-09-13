package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.cn.opdw.vo.HerbPresDt;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_PRESCRIPTION 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_PRESCRIPTION")
public class CnPrescription extends BaseModule  {

	@PK
	@Field(value="PK_PRES",id=KeyId.UUID)
    private String pkPres;
	/**
	 * 就诊主键
	 */
	@Field(value="PK_PV")
    private String pkPv;
	/**
	 * 患者主键
	 */
	@Field(value="PK_PI")
    private String pkPi;
	/**
	 * 处方类型
	 */
	@Field(value="DT_PRESTYPE")
    private String dtPrestype;
	/**
	 * 处方性质
	 */
	@Field(value="DT_PROPERTIES")
    private String dtProperties;
	/**
	 * 处方号
	 */
	@Field(value="PRES_NO")
    private String presNo;
	/**
	 * 处方日期
	 */
	@Field(value="DATE_PRES")
    private Date datePres;
	/**
	 * 开立科室
	 */
	@Field(value="PK_DEPT")
    private String pkDept;
	/**
	 * 开立病区
	 */
	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;
	/**
	 * 开立医生
	 */
	@Field(value="PK_EMP_ORD")
    private String pkEmpOrd;
	/**
	 * 开立医生名称
	 */
	@Field(value="NAME_EMP_ORD")
    private String nameEmpOrd;
	/**
	 * 备注
	 */
	@Field(value="NOTE")
    private String note;
	/**
	 * 打印标志
	 */
	@Field(value="FLAG_PRT")
    private String flagPrt;
	/**
	 * 代办人姓名
	 */
	@Field(value="NAME_AGENT")
    private String nameAgent;
	/**
	 * 代办人身份证号
	 */
	@Field(value="IDNO_AGENT")
    private String idnoAgent;
	/**
	 * 代办人联系地址
	 */
	@Field(value="ADDR_AGENT")
    private String addrAgent;
	/**
	 * 代办人电话
	 */
	@Field(value="TEL_AGENT")
    private String telAgent;
	/**
	 * 关联诊断编码
	 */
	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="FALG_SELF")
	private String flagSelf;
	/**
	 * 煎药标识，030412，对应dt_boilway
	 */
	@Field(value="EU_BOIL")
	private String euBoil;

	/**
	 * 延长处方用量时间原因
	 */
	@Field(value="DT_EXTREASON")
	private String dtExtreason;

	/**
	 * 医保属性，0医保外，1医保外
	 */
	private String euHptype;
	/**
	 * 几煎，用于中药，煎几次
	 */
	@Field(value="FRIED_NUM")
	private Integer friedNum;
	/**
	 * 每剂用药次数
	 */
	@Field(value="USAGE_COUNT")
	private Integer usageCount;
	/**
	 * 打包剂量，用于中药，每次用药毫升数
	 */
	@Field(value="DOSAGE_PACK")
	private Double dosagePack;
	/**
	 * 煎法，030411
	 */
	@Field(value="DT_BOILTYPE")
	private String dtBoiltype;
	/**
	 * 外购电子处方上传标志
	 */
	@Field(value="FLAG_INSU")
	private String flagInsu;
	/**
	 * 医保结算业务序列表，外购处方上传，保存深圳医保结算序列号
	 */
	@Field(value="CODE_INSU")
	private String codeInsu;

	@Field(value="NAME_DIAG")
	private String nameDiag;

	@Field(value="CODE_DIAG")
	private String codeDiag;

	@Field(value = "PK_SYMP")
	private String pkSymp;

	@Field(value = "CODE_SYMP")
	private String codeSymp;

	@Field(value = "NAME_SYMP")
	private String nameSymp;

	/**处方医保类型*/
	@Field(value = "DT_HPPROP")
	private String dtHpprop;

	/**草药处方名称*/
	@Field(value = "NAME_PRES")
	private String namePres;

	/**
	 * 送达时间
	 */
	@Field(value = "DATE_SEND")
	private Date dateSend;
	/**查询字段*/
	private long herbords;
	private String flagAcc;
	private String flagSettle;
	private List<HerbPresDt> herbDt;
	private String pkDeptExec;
	private String pkOrgExec;


	public String getDtBoiltype() {
		return dtBoiltype;
	}
	public void setDtBoiltype(String dtBoiltype) {
		this.dtBoiltype = dtBoiltype;
	}
	public String getEuBoil() {
		return euBoil;
	}
	public void setEuBoil(String euBoil) {
		this.euBoil = euBoil;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public String getPkOrgExec() {
		return pkOrgExec;
	}
	public void setPkOrgExec(String pkOrgExec) {
		this.pkOrgExec = pkOrgExec;
	}
	public List<HerbPresDt> getHerbDt() {
		return herbDt;
	}
	public void setHerbDt(List<HerbPresDt> herbDt) {
		this.herbDt = herbDt;
	}
	public long getHerbords() {
		return herbords;
	}
	public void setHerbords(long herbords) {
		this.herbords = herbords;
	}
	public String getFlagAcc() {
		return flagAcc;
	}
	public void setFlagAcc(String flagAcc) {
		this.flagAcc = flagAcc;
	}
	public String getFlagSettle() {
		return flagSettle;
	}
	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}
	public String getPkPres(){
        return this.pkPres;
    }
    public void setPkPres(String pkPres){
        this.pkPres = pkPres;
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

    public String getDtPrestype(){
        return this.dtPrestype;
    }
    public void setDtPrestype(String dtPrestype){
        this.dtPrestype = dtPrestype;
    }

    public String getDtProperties(){
        return this.dtProperties;
    }
    public void setDtProperties(String dtProperties){
        this.dtProperties = dtProperties;
    }

    public String getPresNo(){
        return this.presNo;
    }
    public void setPresNo(String presNo){
        this.presNo = presNo;
    }

    public Date getDatePres(){
        return this.datePres;
    }
    public void setDatePres(Date datePres){
        this.datePres = datePres;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
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

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagPrt(){
        return this.flagPrt;
    }
    public void setFlagPrt(String flagPrt){
        this.flagPrt = flagPrt;
    }

    public String getNameAgent(){
        return this.nameAgent;
    }
    public void setNameAgent(String nameAgent){
        this.nameAgent = nameAgent;
    }

    public String getIdnoAgent(){
        return this.idnoAgent;
    }
    public void setIdnoAgent(String idnoAgent){
        this.idnoAgent = idnoAgent;
    }

    public String getAddrAgent(){
        return this.addrAgent;
    }
    public void setAddrAgent(String addrAgent){
        this.addrAgent = addrAgent;
    }

 

    public String getTelAgent() {
		return telAgent;
	}
	public void setTelAgent(String telAgent) {
		this.telAgent = telAgent;
	}
	public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
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
    /**
	 * 数据更新状态
	 */
	private String rowStatus;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getDtExtreason() {
		return dtExtreason;
	}
	public void setDtExtreason(String dtExtreason) {
		this.dtExtreason = dtExtreason;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public Integer getFriedNum() {
		return friedNum;
	}
	public void setFriedNum(Integer friedNum) {
		this.friedNum = friedNum;
	}
	public Integer getUsageCount() {
		return usageCount;
	}
	public void setUsageCount(Integer usageCount) {
		this.usageCount = usageCount;
	}
	public Double getDosagePack() {
		return dosagePack;
	}
	public void setDosagePack(Double dosagePack) {
		this.dosagePack = dosagePack;
	}
	public String getFlagInsu() {
		return flagInsu;
	}
	public void setFlagInsu(String flagInsu) {
		this.flagInsu = flagInsu;
	}
	public String getCodeInsu() {
		return codeInsu;
	}
	public void setCodeInsu(String codeInsu) {
		this.codeInsu = codeInsu;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public String getEuHptype() {
		return euHptype;
	}

	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}

	public String getNameSymp() {
		return nameSymp;
	}

	public void setNameSymp(String nameSymp) {
		this.nameSymp = nameSymp;
	}
	public String getDtHpprop() {
		return dtHpprop;
	}

	public void setDtHpprop(String dtHpprop) {
		this.dtHpprop = dtHpprop;
	}

	public Date getDateSend() {
		return dateSend;
	}

	public void setDateSend(Date dateSend) {
		this.dateSend = dateSend;
	}

	public String getNamePres() {
		return namePres;
	}

	public void setNamePres(String namePres) {
		this.namePres = namePres;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public String getPkSymp() {
		return pkSymp;
	}

	public void setPkSymp(String pkSymp) {
		this.pkSymp = pkSymp;
	}

	public String getCodeSymp() {
		return codeSymp;
	}

	public void setCodeSymp(String codeSymp) {
		this.codeSymp = codeSymp;
	}
}