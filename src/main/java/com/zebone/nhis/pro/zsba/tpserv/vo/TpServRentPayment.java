package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 第三方服务项目出租表和缴费记录表的中间表
 * Table: tp_serv_rent_payment
 *
 * @since 2017-10-18 11:24:48
 */
@Table(value="TP_SERV_RENT_PAYMENT")
public class TpServRentPayment  {

	/** PK_ITEM - 主键 */
	@PK
	@Field(value="PK_RENT_PAYMENT",id=KeyId.UUID)
    private String pkRentPayment;

	/** FK_DEPT - 缴费记录主键 */
	@Field(value="PK_PAYMENT")
    private String pkPayment;

	/** FK_ITEM_TYPE - 项目出租主键 */
	@Field(value="PK_RENT")
    private String pkRent;

	/** DELFLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;
	
	/** CREATOR - 创建人 */
	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;
	
	/** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;
	
	/** MODIFIER - 修改人 */
	@Field(value="MODIFIER",userfield="pkEmp",userfieldscop=FieldType.UPDATE)
    private String modifier;
	
	/** MODITY_TIME - 修改时间 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;
	
	/** TS - 时间戳 */
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;

	public String getPkRentPayment() {
		return pkRentPayment;
	}

	public void setPkRentPayment(String pkRentPayment) {
		this.pkRentPayment = pkRentPayment;
	}

	public String getPkPayment() {
		return pkPayment;
	}

	public void setPkPayment(String pkPayment) {
		this.pkPayment = pkPayment;
	}

	public String getPkRent() {
		return pkRent;
	}

	public void setPkRent(String pkRent) {
		this.pkRent = pkRent;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
}