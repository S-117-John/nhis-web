package com.zebone.nhis.pro.zsba.compay.other.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 投票结果表
 * @author 85102
 *
 */
@Table(value="DRUG_OPT_RESULT")
public class DrugOptResult {
	
	/**  投票结果主键 */
	@PK
	@Field(value="PK_RESULT",id=KeyId.UUID)
    private String pkResult;

    /**  选中标志*/
	@Field(value="FLAG_PASS")
    public String flagPass;

    /**  药品主键 */
	@Field(value="PK_PD")
    private String pkPd;

    /**  得票数 */
	@Field(value="QUAN")
	private int quan;
	
    /**  得票率 */
	@Field(value="RATIO")
	private double ratio;

	/** 创建时间 */
	@Field(value="CREATE_TIME")
    private Date CreateTime;

    /** 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** 二次投票标志 */
	@Field(value="FLAG_SECOND")
    private String flagSecond;

    /** 二次得票数 */
	@Field(value="QUAN_SECOND")
	private int quanSecond;
	
	/** 二次得票主键 */
	@Field(value="PK_RESULT_SECOND")
	private String pkResultSecond;
	
	public String getPkResultSecond() {
		return pkResultSecond;
	}

	public void setPkResultSecond(String pkResultSecond) {
		this.pkResultSecond = pkResultSecond;
	}

	public int getQuanSecond() {
		return quanSecond;
	}

	public void setQuanSecond(int quanSecond) {
		this.quanSecond = quanSecond;
	}

	public String getPkResult() {
		return pkResult;
	}

	public void setPkResult(String pkResult) {
		this.pkResult = pkResult;
	}

	public String getFlagPass() {
		return flagPass;
	}

	public void setFlagPass(String flagPass) {
		this.flagPass = flagPass;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public int getQuan() {
		return quan;
	}

	public void setQuan(int quan) {
		this.quan = quan;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getFlagSecond() {
		return flagSecond;
	}

	public void setFlagSecond(String flagSecond) {
		this.flagSecond = flagSecond;
	}
	
}
