package com.zebone.nhis.common.module.base.bd.mk;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * 医嘱用法附加费用<不再使用>
 * Table: BD_TERM_USAGE_ITEM  - bd_term_usage_item 
 *
 * @since 2016-08-29 10:56:14
 */
@Table(value="BD_TERM_USAGE_ITEM")
public class BdTermUsageItem extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_USAGEITEM",id=KeyId.UUID)
    private String pkUsageitem;

	/** 用法主键，关联医嘱用法 */
	@Field(value="PK_USAGE")
    private String pkUsage;

	/** 绑定项目 */
	@Field(value="PK_ITEM")
    private String pkItem;

	/** 绑定项目编码 */
	@Field(value="CODE_ITEM")
    private String codeItem;

	/** 绑定项目名称 */
	@Field(value="NAME_ITEM")
    private String nameItem;

    /** 就诊类别   EU_PVTYPE - onp门诊 inp住院 sos急诊  mec体检 fp家庭病床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;


    public String getPkUsageitem(){
        return this.pkUsageitem;
    }
    public void setPkUsageitem(String pkUsageitem){
        this.pkUsageitem = pkUsageitem;
    }

    public String getPkUsage(){
        return this.pkUsage;
    }
    public void setPkUsage(String pkUsage){
        this.pkUsage = pkUsage;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getCodeItem(){
        return this.codeItem;
    }
    public void setCodeItem(String codeItem){
        this.codeItem = codeItem;
    }

    public String getNameItem(){
        return this.nameItem;
    }
    public void setNameItem(String nameItem){
        this.nameItem = nameItem;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }
}