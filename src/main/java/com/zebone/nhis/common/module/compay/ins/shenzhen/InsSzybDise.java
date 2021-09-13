package com.zebone.nhis.common.module.compay.ins.shenzhen;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="INS_SZYB_DISE")
public class InsSzybDise extends BaseModule {
	@PK
    @Field(value="PK_INSDISE",id= Field.KeyId.UUID)
    private String pkInsdise;
	
	// 疾病编码
	@Field(value="AKA120")
    private String aka120;
    
    // 中文名称 
	@Field(value="AKA121")
    private String aka121;
    
    // 英文名称 
	@Field(value="AKA062")
    private String aka062;

    // 拼音简码 
	@Field(value="AKA020")
    private String aka020;
    
    // 疾病分类
	@Field(value="AKA122")
    private String aka122;
     
    // 大病类别
	@Field(value="CKA303")
    private String cka303;
    
    // 转诊病种类型
	@Field(value="BKM070")
    private String bkm070;
    
    // 分组序号
	@Field(value="BKF199")
    private String bkf199;
    
    // 偿付标准
	@Field(value="BKM071")
    private String bkm071;

    // 偿付周期
	@Field(value="BKM072")
    private String bkm072;
    
    // 启用标志
	@Field(value="AAE569")
    private String aae569;
    
    // 启用日期 
	@Field(value="AAE030")
    private String aae030;

    // 终止日期
	@Field(value="AAE031")
    private String aae031;

    // 更新日期
	@Field(value="AAE396")
    private String aae396;

    // 备注
	@Field(value="AAE013")
    private String aae013;

	public String getPkInsdise() {
		return pkInsdise;
	}

	public void setPkInsdise(String pkInsdise) {
		this.pkInsdise = pkInsdise;
	}

	public String getAka120() {
		return aka120;
	}

	public void setAka120(String aka120) {
		this.aka120 = aka120;
	}

	public String getAka121() {
		return aka121;
	}

	public void setAka121(String aka121) {
		this.aka121 = aka121;
	}

	public String getAka062() {
		return aka062;
	}

	public void setAka062(String aka062) {
		this.aka062 = aka062;
	}

	public String getAka020() {
		return aka020;
	}

	public void setAka020(String aka020) {
		this.aka020 = aka020;
	}

	public String getAka122() {
		return aka122;
	}

	public void setAka122(String aka122) {
		this.aka122 = aka122;
	}

	public String getCka303() {
		return cka303;
	}

	public void setCka303(String cka303) {
		this.cka303 = cka303;
	}

	public String getBkm070() {
		return bkm070;
	}

	public void setBkm070(String bkm070) {
		this.bkm070 = bkm070;
	}

	public String getBkf199() {
		return bkf199;
	}

	public void setBkf199(String bkf199) {
		this.bkf199 = bkf199;
	}

	public String getBkm071() {
		return bkm071;
	}

	public void setBkm071(String bkm071) {
		this.bkm071 = bkm071;
	}

	public String getBkm072() {
		return bkm072;
	}

	public void setBkm072(String bkm072) {
		this.bkm072 = bkm072;
	}

	public String getAae569() {
		return aae569;
	}

	public void setAae569(String aae569) {
		this.aae569 = aae569;
	}

	public String getAae030() {
		return aae030;
	}

	public void setAae030(String aae030) {
		this.aae030 = aae030;
	}

	public String getAae031() {
		return aae031;
	}

	public void setAae031(String aae031) {
		this.aae031 = aae031;
	}

	public String getAae396() {
		return aae396;
	}

	public void setAae396(String aae396) {
		this.aae396 = aae396;
	}

	public String getAae013() {
		return aae013;
	}

	public void setAae013(String aae013) {
		this.aae013 = aae013;
	}
    
	
}
