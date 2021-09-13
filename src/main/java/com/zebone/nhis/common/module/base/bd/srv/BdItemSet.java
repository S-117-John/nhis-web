package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ITEM_SET  - bd_item_set 
 *
 * @since 2016-09-09 10:33:57
 */
@Table(value="BD_ITEM_SET")
public class BdItemSet extends BaseModule  {

	@PK
	@Field(value="PK_ITEMSET",id=KeyId.UUID)
    private String pkItemset;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="PK_ITEM_CHILD")
    private String pkItemChild;

	@Field(value="QUAN")
    private double quan;

	private String state;
	
	
    public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPkItemset(){
        return this.pkItemset;
    }
    public void setPkItemset(String pkItemset){
        this.pkItemset = pkItemset;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getPkItemChild(){
        return this.pkItemChild;
    }
    public void setPkItemChild(String pkItemChild){
        this.pkItemChild = pkItemChild;
    }

    public double getQuan(){
        return this.quan;
    }
    public void setQuan(double quan){
        this.quan = quan;
    }

}