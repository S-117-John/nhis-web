package com.zebone.nhis.common.module.compay.ins.syx;

import java.util.Date;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_GZYB_DS 数据源配置
 * 
 * @since 2018-07-20 10:35:53
 */
@Table(value = "INS_GZYB_DS")
public class GzybDataSource extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_GZYBDS - 主键 */
	@PK
	@Field(value = "PK_GZYBDS", id = KeyId.UUID)
	private String pkGzybds;

	public String getPkGzybds() {
		return pkGzybds;
	}

	public void setPkGzybds(String pkGzybds) {
		this.pkGzybds = pkGzybds;
	}

	/** PK_HP - PK_HP-医保计划主键 */
	@Field(value = "PK_HP")
	private String pkhp;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** PK_DEPT-收费科室 */
	@Field(value = "PK_DEPT")
	private String pkdept;

	/** EU_TREATMENT-待遇类别 */
	@Field(value = "EU_TREATMENT")
	private String euTreatment;

	/** DESC_CONN-数据库连接串 */
	@Field(value = "DESC_CONN")
	private String descConn;

	/** SERVER_API接口服务器 */
	@Field(value = "SERVER_API")
	private String serverApi;

	/** FLAG_REST-限制用药登记 */
	@Field(value = "FLAG_REST")
	private String flagRest;

	/** CODE_ORG-医院编号 */
	@Field(value = "CODE_ORG")
	private String codeOrg;

	/** CODE_CENTER-中心编号 */
	@Field(value = "CODE_CENTER")
	private String codeCenter;

	/** NOTE-备注 */
	@Field(value = "NOTE")
	private String note;

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
	private String ts;

	/** EU_HPDICTTYPE-医保目录类型*/
	@Field(value = "EU_HPDICTTYPE")
	private String euHpDictType;

	
	public String getPkhp() {
		return this.pkhp;
	}

	public void setPkhp(String pkhp) {
		this.pkhp = pkhp;
	}

	public String getPkdept() {
		return this.pkdept;
	}

	public void setPkdept(String pkdept) {
		this.pkdept = pkdept;
	}

	public String getEuTreatment() {
		return this.euTreatment;
	}

	public void setEuTreatment(String euTreatment) {
		this.euTreatment = euTreatment;
	}

	public String getDescConn() {
		return this.descConn;
	}

	public void setDescConn(String descConn) {
		this.descConn = descConn;
	}

	public String getServerApi() {
		return this.serverApi;
	}

	public void setServerApi(String serverApi) {
		this.serverApi = serverApi;
	}

	public String getFlagRest() {
		return this.flagRest;
	}

	public void setFlagRest(String flagRest) {
		this.flagRest = flagRest;
	}

	public String getCodeOrg() {
		return this.codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	public String getCodeCenter() {
		return this.codeCenter;
	}

	public void setCodeCenter(String codeCenter) {
		this.codeCenter = codeCenter;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEuHpDictType() {
		return euHpDictType;
	}

	public void setEuHpDictType(String euHpDictType) {
		this.euHpDictType = euHpDictType;
	}
}
