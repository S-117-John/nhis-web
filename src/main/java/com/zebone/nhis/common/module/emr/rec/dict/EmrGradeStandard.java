package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;
import java.util.List;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: EMR_GRADE_STANDARD 
 *
 * @since 2016-10-31 04:53:14
 */
@Table(value="EMR_GRADE_STANDARD")
public class EmrGradeStandard   {

	@PK
	@Field(value="PK_STAND",id=KeyId.UUID)
    private String pkStand;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="SHORT_NAME")
    private String shortName;

    /** TYPE_CODE - emr_grade_type.code */
	@Field(value="TYPE_CODE")
    private String typeCode;

    /** EU_STAND_TYPE - before：预先提醒，save:-文档保存，commit-病案提交，check-质控检查 */
	@Field(value="EU_STAND_TYPE")
    private String euStandType;

	@Field(value="DOC_TYPE_CODE")
    private String docTypeCode;

	@Field(value="SCORE_STAND")
    private String scoreStand;

    /** SCORE_TYPE - 扣分类型Std_Score_Type：single-单项，single_not-单否，each-每项，all_not-全否 */
	@Field(value="SCORE_TYPE")
    private String scoreType;

	@Field(value="SINGLE_SCORE")
    private Double singleScore;

	@Field(value="SCORE_XML")
    private String scoreXml;

    /** STAND_MODE - 1:自动
0:手动 */
	@Field(value="STAND_MODE")
    private String standMode;

	@Field(value="SORT_NUM")
    private Integer sortNum;

	@Field(value="PK_DEPT")
    private String pkDept;

    /** EU_STATUS - 1:生效
0：未生效 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="FLAG_TIME")
    private String flagTime;
	
	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="REMARK")
    private String remark;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	private String xmlFlag;
	
	private String status;
	
	private List<String> excludeCodes;
	
	/**系统分类*/
	@Field(value="cate_code")
	private String cateCode;
	
	public String getCateCode() {
		return cateCode;
	}

	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}

	public String getPkStand() {
		return pkStand;
	}

	public void setPkStand(String pkStand) {
		this.pkStand = pkStand;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getEuStandType() {
		return euStandType;
	}

	public void setEuStandType(String euStandType) {
		this.euStandType = euStandType;
	}

	public String getDocTypeCode() {
		return docTypeCode;
	}

	public void setDocTypeCode(String docTypeCode) {
		this.docTypeCode = docTypeCode;
	}

	public String getScoreStand() {
		return scoreStand;
	}

	public void setScoreStand(String scoreStand) {
		this.scoreStand = scoreStand;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public Double getSingleScore() {
		return singleScore;
	}

	public void setSingleScore(Double singleScore) {
		this.singleScore = singleScore;
	}

	public String getScoreXml() {
		return scoreXml;
	}

	public void setScoreXml(String scoreXml) {
		this.scoreXml = scoreXml;
	}

	public String getStandMode() {
		return standMode;
	}

	public void setStandMode(String standMode) {
		this.standMode = standMode;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getXmlFlag() {
		return xmlFlag;
	}

	public void setXmlFlag(String xmlFlag) {
		this.xmlFlag = xmlFlag;
	}

	public List<String> getExcludeCodes() {
		return excludeCodes;
	}

	public void setExcludeCodes(List<String> excludeCodes) {
		this.excludeCodes = excludeCodes;
	}

	public String getFlagTime() {
		return flagTime;
	}

	public void setFlagTime(String flagTime) {
		this.flagTime = flagTime;
	}
	
	
}