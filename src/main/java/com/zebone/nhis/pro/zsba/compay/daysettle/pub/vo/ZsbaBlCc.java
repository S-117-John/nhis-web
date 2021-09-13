package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BL_CC  - 收费结算-操作员结账 
 *
 * @since 2016-10-21 09:24:57
 */
@Table(value="BL_CC")
public class ZsbaBlCc extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_CC - 操作员结账主键 */
	@PK
	@Field(value="PK_CC",id=KeyId.UUID)
    private String pkCc;

    /** EU_CCTYPE - 结账类型:0 门诊结账；1  门诊挂号结账；2 门诊收费结账；3 账户结账；8 住院结账； */
	@Field(value="EU_CCTYPE")
    private String euCctype;

    /** PK_EMP_OPERA - 操作员 */
	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

    /** NAME_EMP_OPERA - 操作员姓名 */
	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

    /** DATE_BEGIN - 开始日期 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;

    /** DATE_END - 终止日期 */
	@Field(value="DATE_END")
    private Date dateEnd;

    /** AMT_SETTLE - 结算金额 */
	@Field(value="AMT_SETTLE")
    private Double amtSettle;

    /** AMT_PREP - 缴款合计 */
	@Field(value="AMT_PREP")
    private Double amtPrep;

	/** AMT_Disc - 优惠金额 */
	@Field(value="AMT_DISC")
    private Double amtDisc;
	
    /** AMT_INSU - 医保金额 */
	@Field(value="AMT_INSU")
    private Double amtInsu;
	

    /** AMT_PREP_RT - 退预交金 */
	@Field(value="AMT_PREP_RT")
    private Double amtPrepRt;

    /** EU_STATUS - 结账状态 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** INV_INFO - 使用结算发票号信息串 */
	@Field(value="INV_INFO")
    private String invInfo;

    /** CNT_INV - 发票张数 */
	@Field(value="CNT_INV")
    private Integer cntInv;

    /** INV_INFO_CANC - 作废结算发票号信息串 */
	@Field(value="INV_INFO_CANC")
    private String invInfoCanc;

    /** CNT_INV_CANC - 已作废的总张数 */
	@Field(value="CNT_INV_CANC")
    private Integer cntInvCanc;

    /** AMT_PI - 患者预交金_收款 */
	@Field(value="AMT_PI")
    private Double amtPi;

    /** AMT_PI_BACK - 患者预交金_退款 */
	@Field(value="AMT_PI_BACK")
    private Double amtPiBack;

    /** DEPO_INFO - 住院押金收据使用号码串 */
	@Field(value="DEPO_INFO")
    private String depoInfo;

    /** DEPO_INFO_BACK - 住院押金收据收回号码串 */
	@Field(value="DEPO_INFO_BACK")
    private String depoInfoBack;

    /** CNT_DEPO - 住院押金收据发出张数 */
	@Field(value="CNT_DEPO")
    private Long cntDepo;

    /** CNT_DEPO_BACK - 住院押金收据收回张数 */
	@Field(value="CNT_DEPO_BACK")
    private Long cntDepoBack;

    /** DATE_CC - 操作员结账日期时间 */
	@Field(value="DATE_CC")
    private Date dateCc;

    /** FLAG_CLEAR - 财务交割标志 */
	@Field(value="FLAG_CLEAR")
    private String flagClear;

    /** DATE_CLEAR - 财务交割日期 */
	@Field(value="DATE_CLEAR")
    private Date dateClear;

    /** PK_CLEAR - 财务交割主键 */
	@Field(value="PK_CLEAR")
    private String pkClear;

    /** FLAG_LEADER - 组长日结标志 */
	@Field(value="FLAG_LEADER")
    private String flagLeader;

    /** DATE_LEADER - 组长日结日期 */
	@Field(value="DATE_LEADER")
    private Date dateLeader;

    /** PK_EMP_LEADER - 组长 */
	@Field(value="PK_EMP_LEADER")
    private String pkEmpLeader;

    /** NAME_EMP_LEADER - 组长姓名 */
	@Field(value="NAME_EMP_LEADER")
    private String nameEmpLeader;
	
	/** K_DEPT - 结账科室 */
	@Field(value="PK_DEPT")
	private String pkDept;
	
	@Field(value="AMT_AR")
	private Double  amtAr;
	
	@Field(value="AMT_REPAIR")
	private Double amtRepair;
	
	@Field(value="AMT_SOR")
	private Double amtSor;
	
	@Field(value="AMT_CA")
	private Double amtCa;
	
	@Field(value="AMT_ROUND")
	private Double amtRound;
	
	private Double amtGz;
	
    public Double getAmtRound() {
		return amtRound;
	}
	public void setAmtRound(Double amtRound) {
		this.amtRound = amtRound;
	}
	public Double getAmtSor() {
		return amtSor;
	}
	public void setAmtSor(Double amtSor) {
		this.amtSor = amtSor;
	}
	public Double getAmtCa() {
		return amtCa;
	}
	public void setAmtCa(Double amtCa) {
		this.amtCa = amtCa;
	}
	public Double getAmtAr() {
		return amtAr;
	}
	public void setAmtAr(Double amtAr) {
		this.amtAr = amtAr;
	}
	public Double getAmtRepair() {
		return amtRepair;
	}
	public void setAmtRepair(Double amtRepair) {
		this.amtRepair = amtRepair;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkCc(){
        return this.pkCc;
    }
    public void setPkCc(String pkCc){
        this.pkCc = pkCc;
    }

    public String getEuCctype(){
        return this.euCctype;
    }
    public void setEuCctype(String euCctype){
        this.euCctype = euCctype;
    }

    public String getPkEmpOpera(){
        return this.pkEmpOpera;
    }
    public void setPkEmpOpera(String pkEmpOpera){
        this.pkEmpOpera = pkEmpOpera;
    }

    public String getNameEmpOpera(){
        return this.nameEmpOpera;
    }
    public void setNameEmpOpera(String nameEmpOpera){
        this.nameEmpOpera = nameEmpOpera;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public Double getAmtSettle(){
        return this.amtSettle;
    }
    public void setAmtSettle(Double amtSettle){
        this.amtSettle = amtSettle;
    }

    public Double getAmtPrep(){
        return this.amtPrep;
    }
    public void setAmtPrep(Double amtPrep){
        this.amtPrep = amtPrep;
    }

    public Double getAmtInsu(){
        return this.amtInsu;
    }
    public void setAmtInsu(Double amtInsu){
        this.amtInsu = amtInsu;
    }

    public String getInvInfo(){
        return this.invInfo;
    }
    public void setInvInfo(String invInfo){
        this.invInfo = invInfo;
    }

    public Integer getCntInv(){
        return this.cntInv;
    }
    public void setCntInv(Integer cntInv){
        this.cntInv = cntInv;
    }

    public String getInvInfoCanc(){
        return this.invInfoCanc;
    }
    public void setInvInfoCanc(String invInfoCanc){
        this.invInfoCanc = invInfoCanc;
    }

    public Integer getCntInvCanc(){
        return this.cntInvCanc;
    }
    public void setCntInvCanc(Integer cntInvCanc){
        this.cntInvCanc = cntInvCanc;
    }

    public Double getAmtPi(){
        return this.amtPi;
    }
    public void setAmtPi(Double amtPi){
        this.amtPi = amtPi;
    }

    public Double getAmtPiBack(){
        return this.amtPiBack;
    }
    public void setAmtPiBack(Double amtPiBack){
        this.amtPiBack = amtPiBack;
    }

    public String getDepoInfo(){
        return this.depoInfo;
    }
    public void setDepoInfo(String depoInfo){
        this.depoInfo = depoInfo;
    }

    public String getDepoInfoBack(){
        return this.depoInfoBack;
    }
    public void setDepoInfoBack(String depoInfoBack){
        this.depoInfoBack = depoInfoBack;
    }

    public Long getCntDepo(){
        return this.cntDepo;
    }
    public void setCntDepo(Long cntDepo){
        this.cntDepo = cntDepo;
    }

    public Long getCntDepoBack(){
        return this.cntDepoBack;
    }
    public void setCntDepoBack(Long cntDepoBack){
        this.cntDepoBack = cntDepoBack;
    }

    public Date getDateCc(){
        return this.dateCc;
    }
    public void setDateCc(Date dateCc){
        this.dateCc = dateCc;
    }

    public String getFlagClear(){
        return this.flagClear;
    }
    public void setFlagClear(String flagClear){
        this.flagClear = flagClear;
    }

    public Date getDateClear(){
        return this.dateClear;
    }
    public void setDateClear(Date dateClear){
        this.dateClear = dateClear;
    }

    public String getPkClear(){
        return this.pkClear;
    }
    public void setPkClear(String pkClear){
        this.pkClear = pkClear;
    }

    public String getFlagLeader(){
        return this.flagLeader;
    }
    public void setFlagLeader(String flagLeader){
        this.flagLeader = flagLeader;
    }

    public Date getDateLeader(){
        return this.dateLeader;
    }
    public void setDateLeader(Date dateLeader){
        this.dateLeader = dateLeader;
    }

    public String getPkEmpLeader(){
        return this.pkEmpLeader;
    }
    public void setPkEmpLeader(String pkEmpLeader){
        this.pkEmpLeader = pkEmpLeader;
    }

    public String getNameEmpLeader(){
        return this.nameEmpLeader;
    }
    public void setNameEmpLeader(String nameEmpLeader){
        this.nameEmpLeader = nameEmpLeader;
    }

    public Double getAmtPrepRt(){
        return this.amtPrepRt;
    }
    public void setAmtPrepRt(Double amtPrepRt){
        this.amtPrepRt = amtPrepRt;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }
	public Double getAmtDisc() {
		return amtDisc;
	}
	public void setAmtDisc(Double amtDisc) {
		this.amtDisc = amtDisc;
	}
	public Double getAmtGz() {
		return amtGz;
	}
	public void setAmtGz(Double amtGz) {
		this.amtGz = amtGz;
	}
    
}