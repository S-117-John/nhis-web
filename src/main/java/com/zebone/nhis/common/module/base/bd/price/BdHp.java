package com.zebone.nhis.common.module.base.bd.price;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP  - 医保计划 
 *
 * @since 2016-09-13 02:24:23
 */
@Table(value="BD_HP")
public class BdHp extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_HP - 医保计划主键 */
	@PK
	@Field(value="PK_HP",id=KeyId.UUID)
    private String pkHp;

    /** PK_PARENT - 上级医保计划 */
	@Field(value="PK_PARENT")
    private String pkParent;

    /** EU_HPTYPE - 医保计划类型 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它 */
	@Field(value="EU_HPTYPE")
    private String euHptype;

    /** CODE - 医保计划编码 */
	@Field(value="CODE")
    private String code;

    /** NAME - 医保计划名称 */
	@Field(value="NAME")
    private String name;

    /** PK_PAYER - 付款方 */
	@Field(value="PK_PAYER")
    private String pkPayer;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spcode;

    /** D_CODE - 自定义码 */
	@Field(value="D_CODE")
    private String dCode;

    /** FLAG_OP - 门诊使用 */
	@Field(value="FLAG_OP")
    private String flagOp;

    /** FLAG_ER - 急诊使用 */
	@Field(value="FLAG_ER")
    private String flagEr;

    /** FLAG_IP - 住院使用 */
	@Field(value="FLAG_IP")
    private String flagIp;

    /** FLAG_PE - 体检使用 */
	@Field(value="FLAG_PE")
    private String flagPe;

    /** FLAG_HM - 家床使用 */
	@Field(value="FLAG_HM")
    private String flagHm;

    /** DT_EXTHP - 对应第三方医保 */
	@Field(value="DT_EXTHP")
    private String dtExthp;
	
	@Field(value="PK_HPCGDIV")
    private String pkHpcgdiv;

    /** PK_HPSTDIV - 关联的结算策略 */
	@Field(value="PK_HPSTDIV")
    private String pkHpstdiv;

	@Field(value="RATE_OP")
    private Double rateOp;

	@Field(value="RATE_ER")
    private Double rateEr;

	@Field(value="RATE_IP")
    private Double rateIp;

	@Field(value="RATE_PE")
    private Double ratePe;

	@Field(value="RATE_HM")
    private Double rateHm;

	@Field(value="DRUGQUOTA_OP")
    private Double drugquotaOp;

	@Field(value="ITEMQUOTA_OP")
    private Double itemquotaOp;

	@Field(value="DRUGQUOTA_IP")
    private Double drugquotaIp;

	@Field(value="ITEMQUOTA_IP")
    private Double itemquotaIp;

	@Field(value="BEDQUOTA")
    private Double bedquota;

	@Field(value="DRUGQUOTA_APP")
    private Double drugquotaApp;

	@Field(value="ITEMQUOTA_APP")
    private Double itemquotaApp;

	@Field(value="FLAG_IPNOOP")
    private String flagIpnoop;
	
	@Field(value="Note")
    private String note;
	
	@Field(value="DT_MDPAYTYPE")
    private String dtMdpaytype;
	
	//门诊诊查费限额
	@Field(value="DTQUOTA_OP")
	private Double dtquotaOp;
	
	//住院诊查费限额
	@Field(value="DTQUOTA_IP")
	private Double dtquotaIp;
	
    public Double getDtquotaOp() {
		return dtquotaOp;
	}
	public void setDtquotaOp(Double dtquotaOp) {
		this.dtquotaOp = dtquotaOp;
	}
	public Double getDtquotaIp() {
		return dtquotaIp;
	}
	public void setDtquotaIp(Double dtquotaIp) {
		this.dtquotaIp = dtquotaIp;
	}
	public String getDtMdpaytype() {
		return dtMdpaytype;
	}
	public void setDtMdpaytype(String dtMdpaytype) {
		this.dtMdpaytype = dtMdpaytype;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkParent(){
        return this.pkParent;
    }
    public void setPkParent(String pkParent){
        this.pkParent = pkParent;
    }

    public String getEuHptype(){
        return this.euHptype;
    }
    public void setEuHptype(String euHptype){
        this.euHptype = euHptype;
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

    public String getPkPayer(){
        return this.pkPayer;
    }
    public void setPkPayer(String pkPayer){
        this.pkPayer = pkPayer;
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

    public String getFlagOp(){
        return this.flagOp;
    }
    public void setFlagOp(String flagOp){
        this.flagOp = flagOp;
    }

    public String getFlagEr(){
        return this.flagEr;
    }
    public void setFlagEr(String flagEr){
        this.flagEr = flagEr;
    }

    public String getFlagIp(){
        return this.flagIp;
    }
    public void setFlagIp(String flagIp){
        this.flagIp = flagIp;
    }

    public String getFlagPe(){
        return this.flagPe;
    }
    public void setFlagPe(String flagPe){
        this.flagPe = flagPe;
    }

    public String getFlagHm(){
        return this.flagHm;
    }
    public void setFlagHm(String flagHm){
        this.flagHm = flagHm;
    }

    public String getDtExthp(){
        return this.dtExthp;
    }
    public void setDtExthp(String dtExthp){
        this.dtExthp = dtExthp;
    }
	public String getPkHpcgdiv() {
		return pkHpcgdiv;
	}
	public void setPkHpcgdiv(String pkHpcgdiv) {
		this.pkHpcgdiv = pkHpcgdiv;
	}
	public String getPkHpstdiv() {
		return pkHpstdiv;
	}
	public void setPkHpstdiv(String pkHpstdiv) {
		this.pkHpstdiv = pkHpstdiv;
	}
	public Double getRateOp() {
		return rateOp;
	}
	public void setRateOp(Double rateOp) {
		this.rateOp = rateOp;
	}
	public Double getRateEr() {
		return rateEr;
	}
	public void setRateEr(Double rateEr) {
		this.rateEr = rateEr;
	}
	public Double getRateIp() {
		return rateIp;
	}
	public void setRateIp(Double rateIp) {
		this.rateIp = rateIp;
	}
	public Double getRatePe() {
		return ratePe;
	}
	public void setRatePe(Double ratePe) {
		this.ratePe = ratePe;
	}
	public Double getRateHm() {
		return rateHm;
	}
	public void setRateHm(Double rateHm) {
		this.rateHm = rateHm;
	}
	public Double getDrugquotaOp() {
		return drugquotaOp;
	}
	public void setDrugquotaOp(Double drugquotaOp) {
		this.drugquotaOp = drugquotaOp;
	}
	public Double getItemquotaOp() {
		return itemquotaOp;
	}
	public void setItemquotaOp(Double itemquotaOp) {
		this.itemquotaOp = itemquotaOp;
	}
	public Double getDrugquotaIp() {
		return drugquotaIp;
	}
	public void setDrugquotaIp(Double drugquotaIp) {
		this.drugquotaIp = drugquotaIp;
	}
	public Double getItemquotaIp() {
		return itemquotaIp;
	}
	public void setItemquotaIp(Double itemquotaIp) {
		this.itemquotaIp = itemquotaIp;
	}
	public Double getBedquota() {
		return bedquota;
	}
	public void setBedquota(Double bedquota) {
		this.bedquota = bedquota;
	}
	public Double getDrugquotaApp() {
		return drugquotaApp;
	}
	public void setDrugquotaApp(Double drugquotaApp) {
		this.drugquotaApp = drugquotaApp;
	}
	public Double getItemquotaApp() {
		return itemquotaApp;
	}
	public void setItemquotaApp(Double itemquotaApp) {
		this.itemquotaApp = itemquotaApp;
	}
	public String getFlagIpnoop() {
		return flagIpnoop;
	}
	public void setFlagIpnoop(String flagIpnoop) {
		this.flagIpnoop = flagIpnoop;
	}
    
    

}