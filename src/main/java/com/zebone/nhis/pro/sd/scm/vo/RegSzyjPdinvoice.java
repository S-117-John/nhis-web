package com.zebone.nhis.pro.sd.scm.vo;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: REG_SZYJ_PDINVOICE - reg_szyj_pdinvoice
深圳药监管理系统 
 *
 * @since 2019-12-30 08:12:41
 */
@Table(value="REG_SZYJ_PDINVOICE")
public class RegSzyjPdinvoice   {

	@PK
	@Field(value="PK_PDINV",id=KeyId.UUID)
    private String pkPdinv;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

    /** FPBH - 市药品交易监管系统发布的对发票的统编代码 */
	@Field(value="FPBH")
    private String fpbh;

	@Field(value="FPH")
    private String fph;

	@JSONField(format="yyyy-MM-dd")
	@Field(value="FPRQ")
    private Date fprq;

	@Field(value="GYSBM")
    private String gysbm;

	@Field(value="BHSZJE")
    private Double bhszje;

	@Field(value="HSZJE")
    private Double hszje;

    /** SFCH - 0为不冲红，1为冲红 */
	@Field(value="SFCH")
    private String sfch;

	@Field(value="FPBZ")
    private String fpbz;

    /** ZT - 0为未结算，1为已结算，2为作废 */
	@Field(value="ZT")
    private String zt;

	@Field(value="JLS")
    private Integer jls;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;
	
	private List<RegSzyjPdinvoicedt> mx;


    public String getPkPdinv(){
        return this.pkPdinv;
    }
    public void setPkPdinv(String pkPdinv){
        this.pkPdinv = pkPdinv;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getFpbh(){
        return this.fpbh;
    }
    public void setFpbh(String fpbh){
        this.fpbh = fpbh;
    }

    public String getFph(){
        return this.fph;
    }
    public void setFph(String fph){
        this.fph = fph;
    }

    public String getGysbm(){
        return this.gysbm;
    }
    public void setGysbm(String gysbm){
        this.gysbm = gysbm;
    }

    public Double getBhszje(){
        return this.bhszje;
    }
    public void setBhszje(Double bhszje){
        this.bhszje = bhszje;
    }

    public Double getHszje(){
        return this.hszje;
    }
    public void setHszje(Double hszje){
        this.hszje = hszje;
    }

    public String getSfch(){
        return this.sfch;
    }
    public void setSfch(String sfch){
        this.sfch = sfch;
    }

    public String getFpbz(){
        return this.fpbz;
    }
    public void setFpbz(String fpbz){
        this.fpbz = fpbz;
    }

    public String getZt(){
        return this.zt;
    }
    public void setZt(String zt){
        this.zt = zt;
    }

    public Integer getJls(){
        return this.jls;
    }
    public void setJls(Integer jls){
        this.jls = jls;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
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

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
	public List<RegSzyjPdinvoicedt> getMx() {
		return mx;
	}
	public void setMx(List<RegSzyjPdinvoicedt> mx) {
		this.mx = mx;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	@JsonDeserialize(using=CustJsonDateFormat.class)
	public Date getFprq() {
		return fprq;
	}
	public void setFprq(Date fprq) {
		this.fprq = fprq;
	}
    
}