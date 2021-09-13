package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ITEM_PV  - bd_item_pv 
 *
 * @since 2016-09-09 09:36:05
 */
@Table(value="BD_ITEM_PV")
public class BdItemPv extends BaseModule  {

	@PK
	@Field(value="PK_PVITEM",id=KeyId.UUID)
    private String pkPvitem;

    /** EU_PVTYPE - onp门诊 inp住院 sos急诊 
mec体检 fp家庭病床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="QUAN")
    private double quan;

    /** FLAG_CHOOSE - 0必选 */
	@Field(value="FLAG_CHOOSE")
    private String flagChoose;

	@Field(value="NOTE")
    private String note;

    public String getPkPvitem(){
        return this.pkPvitem;
    }
    public void setPkPvitem(String pkPvitem){
        this.pkPvitem = pkPvitem;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public double getQuan(){
        return this.quan;
    }
    public void setQuan(double quan){
        this.quan = quan;
    }

    public String getFlagChoose(){
        return this.flagChoose;
    }
    public void setFlagChoose(String flagChoose){
        this.flagChoose = flagChoose;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

}