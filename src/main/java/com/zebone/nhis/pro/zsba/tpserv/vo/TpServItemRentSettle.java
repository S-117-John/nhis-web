package com.zebone.nhis.pro.zsba.tpserv.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 第三方服务项目出租结算表
 * Table: tp_serv_item_rent_settle
 *
 * @since 2017-06-26 02:34:48
 */
@Table(value="TP_SERV_ITEM_RENT_SETTLE")
public class TpServItemRentSettle  {

	@PK
	@Field(value="PK_RENT_SETTLE",id=KeyId.UUID)
    private String pkRentSettle;

	@Field(value="FK_DEPT")
    private String fkDept;

	@Field(value="FK_PATIENT")
    private String fkPatient;
	
	@Field(value="PATIENT_NAME")
    private String patientName;
	
	@Field(userfield="PK_RENT",userfieldscop=FieldType.ALL)
    private String fkRent;
	
	@Field(value="PRICE")
    private float price;

	@Field(value="NUM")
    private int num;

	@Field(value="DATE_NUM")
    private int date_num;
	
	@Field(value="AMOUNT")
    private float amount;
	
	@Field(value="SJ_AMOUNT")
    private float sj_amount;
	
	@Field(userfield="PK_UNIONPAY_TRADING",userfieldscop=FieldType.ALL)
    private String fkUnionpayTrading;
	
	@Field(value="CREATOR")
    private String creator;
	
	@Field(value="CREATE_TIME")
    private String createTime;
	
	@Field(value="MODIFIER")
    private String modifier;
	
	@Field(value="MODITY_TIME")
    private String modity_time;
	
	@Field(value="TS")
    private String ts;

	public String getPkRentSettle() {
		return pkRentSettle;
	}

	public void setPkRentSettle(String pkRentSettle) {
		this.pkRentSettle = pkRentSettle;
	}

	public String getFkDept() {
		return fkDept;
	}

	public void setFkDept(String fkDept) {
		this.fkDept = fkDept;
	}

	public String getFkPatient() {
		return fkPatient;
	}

	public void setFkPatient(String fkPatient) {
		this.fkPatient = fkPatient;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getFkRent() {
		return fkRent;
	}

	public void setFkRent(String fkRent) {
		this.fkRent = fkRent;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getDate_num() {
		return date_num;
	}

	public void setDate_num(int date_num) {
		this.date_num = date_num;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getSj_amount() {
		return sj_amount;
	}

	public void setSj_amount(float sj_amount) {
		this.sj_amount = sj_amount;
	}

	public String getFkUnionpayTrading() {
		return fkUnionpayTrading;
	}

	public void setFkUnionpayTrading(String fkUnionpayTrading) {
		this.fkUnionpayTrading = fkUnionpayTrading;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModity_time() {
		return modity_time;
	}

	public void setModity_time(String modity_time) {
		this.modity_time = modity_time;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}


}