package com.zebone.nhis.common.module.bl;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BL_CC_INV  - 收费结算-操作员结账_票据明细信息 
 *
 * @since 2016-10-26 01:25:59
 */
@Table(value="BL_CC_INV")
public class BlCcInv extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_CCINV - 操作员结账票据信息主键 */
	@PK
	@Field(value="PK_CCINV",id=KeyId.UUID)
    private String pkCcinv;

    /** PK_CC - 操作员结账主键 */
	@Field(value="PK_CC")
    private String pkCc;

    /** PK_INVCATE - 发票分类 */
	@Field(value="PK_INVCATE")
    private String pkInvcate;

    /** BEGIN_NO - 票据开始号如果是退废票的话，则票据开始号与结束号一致 */
	@Field(value="BEGIN_NO")
    private String beginNo;

    /** END_NO - 票据结束号 */
	@Field(value="END_NO")
    private String endNo;

    /** FLAG_CANC - 作废标志 */
	@Field(value="FLAG_CANC")
    private String flagCanc;

    /** FLAG_WG - 核销标志 */
	@Field(value="FLAG_WG")
    private String flagWg;

    public String getPkCcinv(){
        return this.pkCcinv;
    }
    public void setPkCcinv(String pkCcinv){
        this.pkCcinv = pkCcinv;
    }

    public String getPkCc(){
        return this.pkCc;
    }
    public void setPkCc(String pkCc){
        this.pkCc = pkCc;
    }

    public String getPkInvcate(){
        return this.pkInvcate;
    }
    public void setPkInvcate(String pkInvcate){
        this.pkInvcate = pkInvcate;
    }

    public String getBeginNo(){
        return this.beginNo;
    }
    public void setBeginNo(String beginNo){
        this.beginNo = beginNo;
    }

    public String getEndNo(){
        return this.endNo;
    }
    public void setEndNo(String endNo){
        this.endNo = endNo;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public String getFlagWg(){
        return this.flagWg;
    }
    public void setFlagWg(String flagWg){
        this.flagWg = flagWg;
    }
}