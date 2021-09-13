package com.zebone.nhis.common.module.emr.rec.tmp;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;

/**
 * Table: EMR_TEMPLATE 
 *
 * @since 2018-09-21 10:19:00
 */
@Table(value="EMR_TEMPLATE")
public class EmrTemplateVo extends BaseModule {
	@PK
	@Field(value="PK_TMP",id=KeyId.UUID)
    private String pkTmp;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="DESCRIBE")
    private String describe;

	@Field(value="TYPE_CODE")
    private String typeCode;

    /** EU_TMP_LEVEL - 0：全院

1：科室

2：个人 */
	@Field(value="EU_TMP_LEVEL")
    private BigDecimal euTmpLevel;

	@Field(value="TMP_TITLE")
    private String tmpTitle;

	@Field(value="TEXT_TITLE")
    private String textTitle;

	@Field(value="SORT_CODE")
    private BigDecimal sortCode;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_WARD")
    private String pkWard;

    /** EU_STATUS - 0、新建

1、医生审核

2、科主任审核

3、医院审核 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** FLAG_AUDIT - 0：不需要审签

1：需要审签 */
	@Field(value="FLAG_AUDIT")
    private String flagAudit;

	@Field(value="AUDIT_LEVEL")
    private BigDecimal auditLevel;

	@Field(value="AUDIT_LEVEL_SET")
    private String auditLevelSet;

    /** LIMIT_NUM - 0：不限制

>0 限制次数 */
	@Field(value="LIMIT_NUM")
    private BigDecimal limitNum;

	@Field(value="FLAG_DEFAULT")
    private String flagDefault;

	@Field(value="FLAG_OPEN")
    private String flagOpen;

	@Field(value="FLAG_MERGE")
    private String flagMerge;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="FLAG_SYS")
    private String flagSys;

	@Field(value="DEL_FLAG")
    private String delFlag;

    /** DOC_DATA - BLOB */
	@Field(value="DOC_DATA")
    private byte[] docData;

    /** DOC_XML - CLOB */
	@Field(value="DOC_XML")
    private String docXml;

    /** ASSOCIATION_RULES - CLOB */
	@Field(value="ASSOCIATION_RULES")
    private String associationRules;

	@Field(value="TMP_PATH")
    private String tmpPath;
	
	@Field(value="REMARK")
    private String remark;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="FLAG_GAUGE")
    private String flagGauge;

	@Field(value="PK_FATHER")
    private String pkFather;


    public String getPkTmp(){
        return this.pkTmp;
    }
    public void setPkTmp(String pkTmp){
        this.pkTmp = pkTmp;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
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

    public String getDescribe(){
        return this.describe;
    }
    public void setDescribe(String describe){
        this.describe = describe;
    }

    public String getTypeCode(){
        return this.typeCode;
    }
    public void setTypeCode(String typeCode){
        this.typeCode = typeCode;
    }

    public BigDecimal getEuTmpLevel(){
        return this.euTmpLevel;
    }
    public void setEuTmpLevel(BigDecimal euTmpLevel){
        this.euTmpLevel = euTmpLevel;
    }

    public String getTmpTitle(){
        return this.tmpTitle;
    }
    public void setTmpTitle(String tmpTitle){
        this.tmpTitle = tmpTitle;
    }

    public String getTextTitle(){
        return this.textTitle;
    }
    public void setTextTitle(String textTitle){
        this.textTitle = textTitle;
    }

    public BigDecimal getSortCode(){
        return this.sortCode;
    }
    public void setSortCode(BigDecimal sortCode){
        this.sortCode = sortCode;
    }

    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkWard(){
        return this.pkWard;
    }
    public void setPkWard(String pkWard){
        this.pkWard = pkWard;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getFlagAudit(){
        return this.flagAudit;
    }
    public void setFlagAudit(String flagAudit){
        this.flagAudit = flagAudit;
    }

    public BigDecimal getAuditLevel(){
        return this.auditLevel;
    }
    public void setAuditLevel(BigDecimal auditLevel){
        this.auditLevel = auditLevel;
    }

    public String getAuditLevelSet(){
        return this.auditLevelSet;
    }
    public void setAuditLevelSet(String auditLevelSet){
        this.auditLevelSet = auditLevelSet;
    }

    public BigDecimal getLimitNum(){
        return this.limitNum;
    }
    public void setLimitNum(BigDecimal limitNum){
        this.limitNum = limitNum;
    }

    public String getFlagDefault(){
        return this.flagDefault;
    }
    public void setFlagDefault(String flagDefault){
        this.flagDefault = flagDefault;
    }

    public String getFlagOpen(){
        return this.flagOpen;
    }
    public void setFlagOpen(String flagOpen){
        this.flagOpen = flagOpen;
    }

    public String getFlagMerge(){
        return this.flagMerge;
    }
    public void setFlagMerge(String flagMerge){
        this.flagMerge = flagMerge;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getFlagSys(){
        return this.flagSys;
    }
    public void setFlagSys(String flagSys){
        this.flagSys = flagSys;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public byte[] getDocData(){
        return this.docData;
    }
    public void setDocData(byte[] docData){
        this.docData = docData;
    }

    public String getDocXml(){
        return this.docXml;
    }
    public void setDocXml(String docXml){
        this.docXml = docXml;
    }

    public String getAssociationRules(){
        return this.associationRules;
    }
    public void setAssociationRules(String associationRules){
        this.associationRules = associationRules;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getFlagGauge(){
        return this.flagGauge;
    }
    public void setFlagGauge(String flagGauge){
        this.flagGauge = flagGauge;
    }

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
    }
	public String getTmpPath() {
		return tmpPath;
	}
	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}

}
