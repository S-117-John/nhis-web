package com.zebone.nhis.pro.zsba.compay.other.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 投票人员字典
 * @author 85102
 *
 */
@Table(value="DRUG_OPT_EMP")
public class DrugOptEmp {
	
	/** 人员主键 */
	@PK
	@Field(value="PK_EMP",id=KeyId.UUID)
    private String pkEmp ;

    /** 人员编码 */
	@Field(value="CODE_EMP")
    private String codeEmp ;

    /** 人员名称 */
	@Field(value="NAME_EMP")
    private String nameEmp ;

    /** 电话 */
	@Field(value="TEL_NO")
    private String telNo ;

    /** 选中标志 */
	@Field(value="FLAG_CHOOSE")
    public String flagChoose ;

    /** 到场标志【0-未到、1-到达】 */
	@Field(value="EU_JOIN")
    private String euJoin ;

    /** 人员类型【0-医生、1-护士、2、药师、3、技师、4、行政】 */
	@Field(value="EU_TYPE")
    private String euType ;

    /** 创建时间 */
	@Field(value="CREATE_TIME")
    private Date createTime;

    /** 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getFlagChoose() {
		return flagChoose;
	}

	public void setFlagChoose(String flagChoose) {
		this.flagChoose = flagChoose;
	}

	public String getEuJoin() {
		return euJoin;
	}

	public void setEuJoin(String euJoin) {
		this.euJoin = euJoin;
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

}
