package com.zebone.nhis.ma.pub.syx.vo;

import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: tFunctionItemListForPACS 
 *
 * @since 2018-12-12 05:07:21
 */
@Table(value="tFunctionItemListForPACS")
public class Tfunctionitemlistforpacs   {

//	@PK
//	@Field(value="FunctionItemListForPACSID",id=KeyId.UUID)
    private Integer functionitemlistforpacsid;

	@Field(value="FunctionRequestID")
    private String functionrequestid;

	@Field(value="ItemID")
    private String itemid;

	@Field(value="ItemNo")
    private String itemno;

	@Field(value="ItemDesc")
    private String itemdesc;

	@Field(value="Amount")
    private String amount;

	@Field(value="FunctionPositionName")
    private String functionpositionname;


    public Integer getFunctionitemlistforpacsid(){
        return this.functionitemlistforpacsid;
    }
    public void setFunctionitemlistforpacsid(Integer functionitemlistforpacsid){
        this.functionitemlistforpacsid = functionitemlistforpacsid;
    }

    public String getFunctionrequestid(){
        return this.functionrequestid;
    }
    public void setFunctionrequestid(String functionrequestid){
        this.functionrequestid = functionrequestid;
    }

    public String getItemid(){
        return this.itemid;
    }
    public void setItemid(String itemid){
        this.itemid = itemid;
    }

    public String getItemno(){
        return this.itemno;
    }
    public void setItemno(String itemno){
        this.itemno = itemno;
    }

    public String getItemdesc(){
        return this.itemdesc;
    }
    public void setItemdesc(String itemdesc){
        this.itemdesc = itemdesc;
    }

    public String getAmount(){
        return this.amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getFunctionpositionname(){
        return this.functionpositionname;
    }
    public void setFunctionpositionname(String functionpositionname){
        this.functionpositionname = functionpositionname;
    }
}