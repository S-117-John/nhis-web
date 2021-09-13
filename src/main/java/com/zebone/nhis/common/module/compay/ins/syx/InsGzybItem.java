package com.zebone.nhis.common.module.compay.ins.syx;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_GZYB_ITEM - 诊疗项目及服务设施
 * 
 * @since 2018-09-22 09:04:08
 */
@Table(value = "INS_GZYB_ITEM")
public class InsGzybItem {

	/** PK_INSITEM - 主键 */
	@PK
	@Field(value = "PK_INSITEM", id = KeyId.UUID)
	private String pkInsitem;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** EU_HPDICTTYPE - 医保目录类别 */
	@Field(value = "EU_HPDICTTYPE")
	private String euHpDictType;

	/** EU_ITEMTYPE - 项目类型 */
	@Field(value = "EU_ITEMTYPE")
	private String euItemtype;

	/** DT_STATTYPE - 统计类别 */
	@Field(value = "DT_STATTYPE")
	private String dtStattype;

	/** EU_DRUGTYPE - 药品类型 */
	@Field(value = "EU_DRUGTYPE")
	private String euDrugtype;

	/** SERIAL_ITEM - 诊疗项目序列号 */
	@Field(value = "SERIAL_ITEM")
	private Integer serialItem;

	/** CODE_ITEM - 项目编码 */
	@Field(value = "CODE_ITEM")
	private String codeItem;

	/** NAME_ITEM - 项目名称 */
	@Field(value = "NAME_ITEM")
	private String nameItem;

	/** NAME_ENG - 英文名称 */
	@Field(value = "NAME_ENG")
	private String nameEng;

	/** SPCODE - 拼音码 */
	@Field(value = "SPCODE")
	private String spcode;

	/** D_CODE - 五码码 */
	@Field(value = "D_CODE")
	private String dCode;

	/** UNIT - 标准单位 */
	@Field(value = "UNIT")
	private String unit;

	/** MODEL - 剂型 */
	@Field(value = "MODEL")
	private String model;

	/** EU_MEDTYPE - 医疗目录 */
	@Field(value = "EU_MEDTYPE")
	private String euMedtype;

	/** EU_STAPLE - 甲乙标志 */
	@Field(value = "EU_STAPLE")
	private String euStaple;

	/** FLAG_OTC - 处方用药标志 */
	@Field(value = "FLAG_OTC")
	private String flagOtc;

	/** FLAG_WL - 工伤补充目录 */
	@Field(value = "FLAG_WL")
	private String flagWl;

	/** FLAG_BO - 生育补充目录 */
	@Field(value = "FLAG_BO")
	private String flagBo;

	/** FLAG_VALID - 有效标志 */
	@Field(value = "FLAG_VALID")
	private String flagValid;

	/** DATE_EFFECT - 生效日期 */
	@Field(value = "DATE_EFFECT")
	private Date dateEffect;

	/** DATE_EXPIRE - 失效日期 */
	@Field(value = "DATE_EXPIRE")
	private Date dateExpire;

	/** RATIO - 自负比例 */
	@Field(value = "RATIO")
	private BigDecimal ratio;

	/** FLAG_REST - 限制用药标志 */
	@Field(value = "FLAG_REST")
	private String flagRest;
	
	/** FLAG_REST - 限制用药范围 */
	@Field(value = "RANGE_REST")
	private String rangeRest;
	
	/** DESC_CONN - 数据库连接串 */
	@Field(value = "DESC_CONN")
	private String descConn;

	/** CREATOR-创建人 */
	@Field(value = "CREATOR")
	private String creator;

	/** CREATE_TIME-创建时间 */
	@Field(value = "CREATE_TIME")
	private Date createTime;

	/** DEL_FLAG-删除标志 */
	@Field(value = "DEL_FLAG")
	private String delFlag;

	/** TS-时间戳 */
	@Field(value = "TS")
	private Date ts;

	public String getPkInsitem() {
		return this.pkInsitem;
	}

	public void setPkInsitem(String pkInsitem) {
		this.pkInsitem = pkInsitem;
	}

	public String getEuHpDictType() {
		return euHpDictType;
	}

	public void setEuHpDictType(String euHpDictType) {
		this.euHpDictType = euHpDictType;
	}

	public String getEuItemtype() {
		return this.euItemtype;
	}

	public void setEuItemtype(String euItemtype) {
		this.euItemtype = euItemtype;
	}

	public String getDtStattype() {
		return this.dtStattype;
	}

	public void setDtStattype(String dtStattype) {
		this.dtStattype = dtStattype;
	}

	public String getEuDrugtype() {
		return this.euDrugtype;
	}

	public void setEuDrugtype(String euDrugtype) {
		this.euDrugtype = euDrugtype;
	}

	public Integer getSerialItem() {
		return this.serialItem;
	}

	public void setSerialItem(Integer serialItem) {
		this.serialItem = serialItem;
	}

	public String getCodeItem() {
		return this.codeItem;
	}

	public void setCodeItem(String codeItem) {
		this.codeItem = codeItem;
	}

	public String getNameItem() {
		return this.nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

	public String getNameEng() {
		return this.nameEng;
	}

	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}

	public String getSpcode() {
		return this.spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return this.dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEuMedtype() {
		return this.euMedtype;
	}

	public void setEuMedtype(String euMedtype) {
		this.euMedtype = euMedtype;
	}

	public String getEuStaple() {
		return this.euStaple;
	}

	public void setEuStaple(String euStaple) {
		this.euStaple = euStaple;
	}

	public String getFlagOtc() {
		return this.flagOtc;
	}

	public void setFlagOtc(String flagOtc) {
		this.flagOtc = flagOtc;
	}

	public String getFlagWl() {
		return this.flagWl;
	}

	public void setFlagWl(String flagWl) {
		this.flagWl = flagWl;
	}

	public String getFlagBo() {
		return this.flagBo;
	}

	public void setFlagBo(String flagBo) {
		this.flagBo = flagBo;
	}

	public String getFlagValid() {
		return this.flagValid;
	}

	public void setFlagValid(String flagValid) {
		this.flagValid = flagValid;
	}

	public Date getDateEffect() {
		return this.dateEffect;
	}

	public void setDateEffect(Date dateEffect) {
		this.dateEffect = dateEffect;
	}

	public Date getDateExpire() {
		return this.dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public BigDecimal getRatio() {
		return this.ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public String getFlagRest() {
		return this.flagRest;
	}

	public void setFlagRest(String flagRest) {
		this.flagRest = flagRest;
	}

	public String getDescConn() {
		return descConn;
	}

	public void setDescConn(String descConn) {
		this.descConn = descConn;
	}

	public String getRangeRest() {
		return rangeRest;
	}

	public void setRangeRest(String rangeRest) {
		this.rangeRest = rangeRest;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

}
