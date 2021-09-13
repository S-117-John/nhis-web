package com.zebone.nhis.pro.zsba.compay.pub.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 修改支付方式记录表
 * @author Administrator
 *
 */
@Table(value = "BL_UP_PAYMODE")
public class ZsbaBlUpPaymode extends BaseModule implements Cloneable{

	private static final long serialVersionUID = 1L;
	
	 /** 主键 */
	@PK
	@Field(value="PK_UP_PAYMODE",id=KeyId.UUID)
    private String pkUpPaymode;

    /** 收付款主键 */
	@Field(value="PK_DEPO")
    private String pkDepo;
	
    /** 原支付方式 */
	@Field(value="OLD_PAYMODE")
    private String oldPaymode;
	
    /** 新支付方式 */
	@Field(value="NEW_PAYMODE")
    private String newPaymode;
	
    /** 修改人姓名 */
	@Field(value="NAME_EMP")
    private String nameEmp;

	public String getPkUpPaymode() {
		return pkUpPaymode;
	}

	public void setPkUpPaymode(String pkUpPaymode) {
		this.pkUpPaymode = pkUpPaymode;
	}

	public String getPkDepo() {
		return pkDepo;
	}

	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}

	public String getOldPaymode() {
		return oldPaymode;
	}

	public void setOldPaymode(String oldPaymode) {
		this.oldPaymode = oldPaymode;
	}

	public String getNewPaymode() {
		return newPaymode;
	}

	public void setNewPaymode(String newPaymode) {
		this.newPaymode = newPaymode;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	
	
}
