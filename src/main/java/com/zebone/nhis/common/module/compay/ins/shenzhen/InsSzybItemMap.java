package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_SZYB_ITEMMAP - ins_szyb_itemmap
 *
 * @since 2019-12-16 14:12:19
 */
@Table(value="INS_SZYB_ITEMMAP")
public class InsSzybItemMap {

	@PK
	@Field(value="PK_ITEMMAP",id=KeyId.UUID)
    private String pkItemmap;
	//所属机构
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
	private String pkorg;
    /** EU_HPDICTTYPE - 01=深圳医保 */
	@Field(value="EU_HPDICTTYPE")
	private String euhpdicttype;
	/** AKE003 - 1=药品，2=诊疗，3=材料，5=辅助器具项目 */
	@Field(value="AKE003")
    private String ake003;
    /** AKA031 - 1=西药，2=中成药，3=中药饮片，4=中药颗粒 */
	@Field(value="AKA031")
    private String aka031;
    /** BKM031 - 诊疗    1=综合医疗服务，2=医技诊疗，3=临床诊疗及手术项目类，4=中医及民族医诊疗，9=其它 */
	@Field(value="BKM031")
    private String bkm031;
	/** 4+7药品标志*/
	@Field(value="flag_state")
	private String flagState;
	///医院目录主键
	@Field(value="PK_ITEM")
	private String pkitem;
	///医院目录编码
	@Field(value="CODE_HOSP")
	private String codehosp;
	///社保目录主键
	@Field(value="PK_INSITEM")
	private String pkinsitem;
	///社保目录编码
	@Field(value="AKE001")
	private String ake001;
	///社保目录名称
	@Field(value="AKE002")
	private String ake002;
	///限制使用标志
	@Field(value="AKA036")
	private String aka036;
	//限制使用说明
	@Field(value="CKM099")
	private String ckm099;
	/**01=基本记帐，02=地补记帐，03=重疾记账，99=自费**/
	//记账标志
	@Field(value="BKM032")
	private String bkm032;
	///审核标志
	@Field(value="FLAG_AUDIT")
	private String flagaudit;
	///创建人
	@Field(value="CREATOR")
	private String creator;
	///创建时间
	@Field(value="CREATE_TIME")
	private Date createtime;
	///删除标志
	@Field(value="DEL_FLAG")
	private String delflag;
	///时间戳
	@Field(value="TS")
	private Date ts;

	@Field(value="DATE_BEGIN")
	private Date dateBegin;
	
	@Field(value="DATE_END")
	private Date dateEnd;

	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getFlagState() {
		return flagState;
	}
	public void setFlagState(String flagState) {
		this.flagState = flagState;
	}
	public String getPkItemmap() {
		return pkItemmap;
	}
	public void setPkItemmap(String pkItemmap) {
		this.pkItemmap = pkItemmap;
	}
	public String getPkorg() {
		return pkorg;
	}
	public void setPkorg(String pkorg) {
		this.pkorg = pkorg;
	}
	public String getEuhpdicttype() {
		return euhpdicttype;
	}
	public void setEuhpdicttype(String euhpdicttype) {
		this.euhpdicttype = euhpdicttype;
	}
	public String getAke003() {
		return ake003;
	}
	public void setAke003(String ake003) {
		this.ake003 = ake003;
	}
	public String getAka031() {
		return aka031;
	}
	public void setAka031(String aka031) {
		this.aka031 = aka031;
	}
	public String getBkm031() {
		return bkm031;
	}
	public void setBkm031(String bkm031) {
		this.bkm031 = bkm031;
	}
	public String getPkitem() {
		return pkitem;
	}
	public void setPkitem(String pkitem) {
		this.pkitem = pkitem;
	}
	public String getCodehosp() {
		return codehosp;
	}
	public void setCodehosp(String codehosp) {
		this.codehosp = codehosp;
	}
	public String getPkinsitem() {
		return pkinsitem;
	}
	public void setPkinsitem(String pkinsitem) {
		this.pkinsitem = pkinsitem;
	}
	public String getAke001() {
		return ake001;
	}
	public void setAke001(String ake001) {
		this.ake001 = ake001;
	}
	public String getAke002() {
		return ake002;
	}
	public void setAke002(String ake002) {
		this.ake002 = ake002;
	}
	public String getFlagaudit() {
		return flagaudit;
	}
	public void setFlagaudit(String flagaudit) {
		this.flagaudit = flagaudit;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getDelflag() {
		return delflag;
	}
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getAka036() {
		return aka036;
	}
	public void setAka036(String aka036) {
		this.aka036 = aka036;
	}
	public String getCkm099() {
		return ckm099;
	}
	public void setCkm099(String ckm099) {
		this.ckm099 = ckm099;
	}
	public String getBkm032() {
		return bkm032;
	}
	public void setBkm032(String bkm032) {
		this.bkm032 = bkm032;
	}




}
