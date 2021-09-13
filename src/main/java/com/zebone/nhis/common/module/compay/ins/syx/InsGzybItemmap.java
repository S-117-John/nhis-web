package com.zebone.nhis.common.module.compay.ins.syx;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value = "INS_GZYB_ITEMMAP")
public class InsGzybItemmap {

	/** PK_ITEMMAP - 主键 */
	@PK
	@Field(value = "PK_ITEMMAP", id = KeyId.UUID)
	private String pkItemmap;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** EU_HPDICTTYPE - 医保目录类别 */
	@Field(value = "EU_HPDICTTYPE")
	private String euHpDictType;

	/** EU_ITEMTYPE - 项目类型 */
	@Field(value = "EU_ITEMTYPE")
	private String euItemtype;

	/** CODE_CENTER - 中心目录编码 */
	@Field(value = "CODE_CENTER")
	private String codeCenter;

	/** CODE_HOSP - 医院目录编码 */
	@Field(value = "CODE_HOSP")
	private String codeHosp;

	/** FLAG_AUDIT - 审核标志 */
	@Field(value = "FLAG_AUDIT")
	private String flagAudit;

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

	public String getPkItemmap() {
		return this.pkItemmap;
	}

	public void setPkItemmap(String pkItemmap) {
		this.pkItemmap = pkItemmap;
	}
	
	public String getEuHpDictType() {
		return euHpDictType;
	}

	public void setEuHpDictType(String euHpDictType) {
		this.euHpDictType = euHpDictType;
	}

	public String getDescConn() {
		return descConn;
	}

	public void setDescConn(String descConn) {
		this.descConn = descConn;
	}

	public String getEuItemtype() {
		return this.euItemtype;
	}

	public void setEuItemtype(String euItemtype) {
		this.euItemtype = euItemtype;
	}
	
	public String getCodeCenter() {
		return this.codeCenter;
	}

	public void setCodeCenter(String codeCenter) {
		this.codeCenter = codeCenter;
	}

	public String getCodeHosp() {
		return this.codeHosp;
	}

	public void setCodeHosp(String codeHosp) {
		this.codeHosp = codeHosp;
	}

	public String getFlagAudit() {
		return this.flagAudit;
	}

	public void setFlagAudit(String flagAudit) {
		this.flagAudit = flagAudit;
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
