package com.zebone.nhis.common.module.base.bd.price;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ITEM_DEFDOC 
 *
 * @since 2018-09-25 09:53:19
 */
@Table(value="BD_ITEM_DEFDOC")
public class BdItemDefdoc extends BaseModule  {

	@PK
	@Field(value="PK_ITEMDEFDOC",id=KeyId.UUID)
    private String pkItemdefdoc;

	@Field(value="CODE_DEFDOCLIST")
    private String codeDefdoclist;

	@Field(value="CODE_DEFDOC")
    private String codeDefdoc;

	@Field(value="PK_ITEM")
    private String pkItem;
	
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getPkItemdefdoc(){
        return this.pkItemdefdoc;
    }
    public void setPkItemdefdoc(String pkItemdefdoc){
        this.pkItemdefdoc = pkItemdefdoc;
    }

    public String getCodeDefdoclist(){
        return this.codeDefdoclist;
    }
    public void setCodeDefdoclist(String codeDefdoclist){
        this.codeDefdoclist = codeDefdoclist;
    }

    public String getCodeDefdoc(){
        return this.codeDefdoc;
    }
    public void setCodeDefdoc(String codeDefdoc){
        this.codeDefdoc = codeDefdoc;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
}
