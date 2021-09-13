package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="INS_QGYB_ITEMMAP")
public class InsQgybItemMap {
	@PK
	@Field(value="PK_ITEMMAP",id=KeyId.UUID)
    private String pkItemmap;
	/** EU_HPDICTTYPE - 1-全国医保，2-省工伤 */
	@Field(value="EU_HPDICTTYPE")
    private String euHpdicttype;
	@Field(value="AKE003")
    private String ake003;
	@Field(value="AKA031")
    private String aka031;
	@Field(value="BKM031")
    private String bkm031;
	@Field(value="PK_ITEM")
    private String pkItem;
	@Field(value="CODE_HOSP")
    private String codeHosp;
	@Field(value="PK_INSITEM")
    private String pkInsitem;
	@Field(value="MED_LIST_CODG")
    private String medListCode;
	@Field(value="AKE002")
    private String ake002;
	@Field(value="AKA036")
    private Date aka036;
	@Field(value="CKM099")
    private String ckm099;
	@Field(value="BKM032")
    private String bkm032;
	@Field(value="FLAG_STATE")
    private String flagState;
	@Field(value="DATE_APPLY")
    private String dateApply;
	@Field(value="EU_APPLY")
    private String euApply;
	@Field(value="DATE_AUDIT")
    private String dateAudit;
	@Field(value="BKM017")
    private String bkm017;
	@Field(value="FLAG_AUDIT")
    private String flagAudit;
	@Field(value="DATE_BENGIN")
    private Date dateBengin;
	@Field(value="DATE_END")
    private Date dateEnd;
	@Field(value="CREATOR")
    private String creator;
	@Field(value="CREATE_TIME")
    private Date createTime;
	@Field(value="DEL_FLAG")
    private String delFlag;
	@Field(value="TS")
    private Date ts;
	
	@Field(value="STATE")
    private String state;
	
	@Field(value="INS_TYPE")
    private String insType;
	
	@Field(value="FLAG_PD")
	private String flagPd;

	@Field(value="LIST_TYPE")
	private String listType;

	private String hisType;

	public String getHisType() {
		return hisType;
	}

	public void setHisType(String hisType) {
		this.hisType = hisType;
	}

	public String getPkItemmap() {
		return pkItemmap;
	}
	public void setPkItemmap(String pkItemmap) {
		this.pkItemmap = pkItemmap;
	}
	public String getEuHpdicttype() {
		return euHpdicttype;
	}
	public void setEuHpdicttype(String euHpdicttype) {
		this.euHpdicttype = euHpdicttype;
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
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getCodeHosp() {
		return codeHosp;
	}
	public void setCodeHosp(String codeHosp) {
		this.codeHosp = codeHosp;
	}
	public String getPkInsitem() {
		return pkInsitem;
	}
	public void setPkInsitem(String pkInsitem) {
		this.pkInsitem = pkInsitem;
	}
	public String getMedListCode() {
		return medListCode;
	}
	public void setMedListCode(String medListCode) {
		this.medListCode = medListCode;
	}
	public String getAke002() {
		return ake002;
	}
	public void setAke002(String ake002) {
		this.ake002 = ake002;
	}
	public Date getAka036() {
		return aka036;
	}
	public void setAka036(Date aka036) {
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
	public String getFlagState() {
		return flagState;
	}
	public void setFlagState(String flagState) {
		this.flagState = flagState;
	}
	public String getDateApply() {
		return dateApply;
	}
	public void setDateApply(String dateApply) {
		this.dateApply = dateApply;
	}
	public String getEuApply() {
		return euApply;
	}
	public void setEuApply(String euApply) {
		this.euApply = euApply;
	}
	public String getDateAudit() {
		return dateAudit;
	}
	public void setDateAudit(String dateAudit) {
		this.dateAudit = dateAudit;
	}
	public String getBkm017() {
		return bkm017;
	}
	public void setBkm017(String bkm017) {
		this.bkm017 = bkm017;
	}
	public String getFlagAudit() {
		return flagAudit;
	}
	public void setFlagAudit(String flagAudit) {
		this.flagAudit = flagAudit;
	}

	public Date getDateBengin() {
		return dateBengin;
	}

	public void setDateBengin(Date dateBengin) {
		this.dateBengin = dateBengin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getInsType() {
		return insType;
	}
	public void setInsType(String insType) {
		this.insType = insType;
	}
	public String getFlagPd() {
		return flagPd;
	}
	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}
}
