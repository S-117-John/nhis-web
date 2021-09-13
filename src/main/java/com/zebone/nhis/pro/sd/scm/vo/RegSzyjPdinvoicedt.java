package com.zebone.nhis.pro.sd.scm.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: REG_SZYJ_PDINVOICEDT - reg_szyj_pdinvoicedt
深圳药监管理系统 
 *
 * @since 2019-12-31 11:20:57
 */
@Table(value="REG_SZYJ_PDINVOICEDT")
public class RegSzyjPdinvoicedt   {

	@PK
	@Field(value="PK_PDINVDT",id=KeyId.UUID)
    private String pkPdinvdt;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** PK_PDINV - reg_szyj_pdinvoice.pk_pdinv */
	@Field(value="PK_PDINV")
    private String pkPdinv;

    /** FPBH - 市药品交易监管系统发布的对发票的统编代码 */
	@Field(value="FPBH")
    private String fpbh;

	@Field(value="SXH")
    private Integer sxh;

	@Field(value="FPMXBH")
    private String fpmxbh;

	@Field(value="YPBM")
    private String ypbm;

	@Field(value="DW")
    private String dw;

	@Field(value="SPSL")
    private Double spsl;

	@Field(value="BHSDJ")
    private Double bhsdj;

	@Field(value="HSDJ")
    private Double hsdj;

	@Field(value="BHSJE")
    private Double bhsje;

	@Field(value="HSJE")
    private Double hsje;

    /** SL - 比如17%的税率，填写17 */
	@Field(value="SL")
    private Double sl;

	@Field(value="STDBH")
    private String stdbh;

	@Field(value="STDMXBH")
    private String stdmxbh;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkPdinvdt(){
        return this.pkPdinvdt;
    }
    public void setPkPdinvdt(String pkPdinvdt){
        this.pkPdinvdt = pkPdinvdt;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPdinv(){
        return this.pkPdinv;
    }
    public void setPkPdinv(String pkPdinv){
        this.pkPdinv = pkPdinv;
    }

    public String getFpbh(){
        return this.fpbh;
    }
    public void setFpbh(String fpbh){
        this.fpbh = fpbh;
    }

    public Integer getSxh(){
        return this.sxh;
    }
    public void setSxh(Integer sxh){
        this.sxh = sxh;
    }

    public String getFpmxbh(){
        return this.fpmxbh;
    }
    public void setFpmxbh(String fpmxbh){
        this.fpmxbh = fpmxbh;
    }

    public String getYpbm(){
        return this.ypbm;
    }
    public void setYpbm(String ypbm){
        this.ypbm = ypbm;
    }

    public String getDw(){
        return this.dw;
    }
    public void setDw(String dw){
        this.dw = dw;
    }

    public Double getSpsl(){
        return this.spsl;
    }
    public void setSpsl(Double spsl){
        this.spsl = spsl;
    }

    public Double getBhsdj(){
        return this.bhsdj;
    }
    public void setBhsdj(Double bhsdj){
        this.bhsdj = bhsdj;
    }

    public Double getHsdj(){
        return this.hsdj;
    }
    public void setHsdj(Double hsdj){
        this.hsdj = hsdj;
    }

    public Double getBhsje(){
        return this.bhsje;
    }
    public void setBhsje(Double bhsje){
        this.bhsje = bhsje;
    }

    public Double getHsje(){
        return this.hsje;
    }
    public void setHsje(Double hsje){
        this.hsje = hsje;
    }

    public Double getSl(){
        return this.sl;
    }
    public void setSl(Double sl){
        this.sl = sl;
    }

    public String getStdbh(){
        return this.stdbh;
    }
    public void setStdbh(String stdbh){
        this.stdbh = stdbh;
    }

    public String getStdmxbh(){
        return this.stdmxbh;
    }
    public void setStdmxbh(String stdmxbh){
        this.stdmxbh = stdmxbh;
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
}