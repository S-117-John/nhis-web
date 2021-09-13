package com.zebone.nhis.common.module.base.bd.price;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * 固定收费套-收费项目
 * Table: BD_DAYCG_SET_ITEM -bd_daycg_set_item
 *
 * @since 2016-08-30 02:49:03
 */
@Table(value="BD_DAYCG_SET_ITEM")
public class BdDaycgSetItem{

	/** 固定收费套收费项目主键 */
	@PK
	@Field(value="PK_DAYCGITEM",id=KeyId.UUID)
    private String pkDaycgitem;

	/** 固定收费套主键 */
	@Field(value="PK_DAYCGSET")
    private String pkDaycgset;

	/** 收费项目主键 */
	@Field(value="PK_ITEM")
    private String pkItem;

	/** 有效月份-开始 */
	@Field(value="MONTH_BEGIN")
    private BigDecimal monthBegin;

	/** 有效月份-结束 */
	@Field(value="MONTH_END")
    private BigDecimal monthEnd;

	/** 有效日期-开始 */
	@Field(value="DAY_BEGIN")
    private BigDecimal dayBegin;

	/** 有效日期-结束 */
	@Field(value="DAY_END")
    private BigDecimal dayEnd;

	/** 固定收费方式 */
	@Field(value="EU_CGMODE")
    private String euCgmode;
	
	/** 创建人  */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;

	/** 创建时间 */
	@Field(value="create_time",date=FieldType.INSERT)
	public Date createTime;    

	/**
     * 修改人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;
	
	/** 时间戳 */
	@Field(date=FieldType.ALL)
	public Date ts;

	/**
     *删除标志  
     */
	@Field(value="del_flag")
	public String delFlag = "0";  // 0未删除  1：删除
	
	public String getPkDaycgitem() {
		return pkDaycgitem;
	}

	public void setPkDaycgitem(String pkDaycgitem) {
		this.pkDaycgitem = pkDaycgitem;
	}

	public String getPkDaycgset() {
		return pkDaycgset;
	}

	public void setPkDaycgset(String pkDaycgset) {
		this.pkDaycgset = pkDaycgset;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public BigDecimal getMonthBegin() {
		return monthBegin;
	}

	public void setMonthBegin(BigDecimal monthBegin) {
		this.monthBegin = monthBegin;
	}

	public BigDecimal getMonthEnd() {
		return monthEnd;
	}

	public void setMonthEnd(BigDecimal monthEnd) {
		this.monthEnd = monthEnd;
	}

	public BigDecimal getDayBegin() {
		return dayBegin;
	}

	public void setDayBegin(BigDecimal dayBegin) {
		this.dayBegin = dayBegin;
	}

	public BigDecimal getDayEnd() {
		return dayEnd;
	}

	public void setDayEnd(BigDecimal dayEnd) {
		this.dayEnd = dayEnd;
	}

	public String getEuCgmode() {
		return euCgmode;
	}

	public void setEuCgmode(String euCgmode) {
		this.euCgmode = euCgmode;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}