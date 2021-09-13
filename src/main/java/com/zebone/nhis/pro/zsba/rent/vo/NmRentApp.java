package com.zebone.nhis.pro.zsba.rent.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 非医疗费用-出租设备
 * @author lipz
 *
 */
@Table(value="nm_rent_app")
public class NmRentApp extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6049252235328881706L;

	@PK
	@Field(value="pk_app",id=KeyId.UUID)
	private String pkApp;// 设备主键
	
	@Field(value="code_app")
    private String codeApp;//项目编码
	
    @Field(value="name_app")
    private String nameApp;//项目名称
    
    @Field(value="py_code")
    private String pyCode;//拼音码
    
    @Field(value="depo_amt")
    private Double depoAmt;//押金金额
    
    @Field(value="file_path")
    private String filePath;//协议文件路径
	
    @Field(value="modity_time")
    private Date modityTime;//

    @Field(value="modifier")
    private String modifier;
    
    
	public String getPkApp() {
		return pkApp;
	}
	public void setPkApp(String pkApp) {
		this.pkApp = pkApp;
	}

	public String getCodeApp() {
		return codeApp;
	}
	public void setCodeApp(String codeApp) {
		this.codeApp = codeApp;
	}

	public String getNameApp() {
		return nameApp;
	}
	public void setNameApp(String nameApp) {
		this.nameApp = nameApp;
	}

	public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public Double getDepoAmt() {
		return depoAmt;
	}
	public void setDepoAmt(Double depoAmt) {
		this.depoAmt = depoAmt;
	}

	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
    
    
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
}
