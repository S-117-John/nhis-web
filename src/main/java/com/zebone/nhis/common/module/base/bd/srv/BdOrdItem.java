package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_ITEM  - bd_ord_item 
 *
 * @since 2016-09-08 01:59:50
 */
@Table(value="BD_ORD_ITEM")
public class BdOrdItem extends BaseModule  {

	@PK
	@Field(value="PK_ORDITEM",id=KeyId.UUID)
    private String pkOrditem;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="QUAN")
    private double quan;

	@Field(value="SORTNO")
    private Integer sortno;
	
	@Field(value="FLAG_PD")
    private String flagPd;
	
	@Field(value="FLAG_UNION")
    private String flagUnion;
    @Field(value="PK_PAYER")
    private String pkPayer;
    @Field(value="RATIO_SELF")
    private double ratioSelf;

    public String getPkPayer() {
        return pkPayer;
    }

    public void setPkPayer(String pkPayer) {
        this.pkPayer = pkPayer;
    }

    public double getRatioSelf() {
        return ratioSelf;
    }

    public void setRatioSelf(double ratioSelf) {
        this.ratioSelf = ratioSelf;
    }

    public String getFlagUnion() {
		return flagUnion;
	}
	public void setFlagUnion(String flagUnion) {
		this.flagUnion = flagUnion;
	}
	public String getFlagPd() {
		return flagPd;
	}
	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}
	public String getPkOrditem(){
        return this.pkOrditem;
    }
    public void setPkOrditem(String pkOrditem){
        this.pkOrditem = pkOrditem;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
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

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

}