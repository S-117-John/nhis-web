package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_ORD_CHARGE 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_ORD_CHARGE")
public class CnOrdCharge   {

	@PK
	@Field(value="PK_ORDCHARGE",id=KeyId.UUID)
    private String pkOrdcharge;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="PRICE_ORG")
    private Double priceOrg;

	@Field(value="PRICE")
    private Double price;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkOrdcharge(){
        return this.pkOrdcharge;
    }
    public void setPkOrdcharge(String pkOrdcharge){
        this.pkOrdcharge = pkOrdcharge;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public Double getPriceOrg(){
        return this.priceOrg;
    }
    public void setPriceOrg(Double priceOrg){
        this.priceOrg = priceOrg;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}