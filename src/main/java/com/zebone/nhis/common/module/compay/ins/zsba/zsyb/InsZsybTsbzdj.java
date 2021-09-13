package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_zsyb_tsbzdj - 中山医保特殊病种登记：（特殊病种登记[6001]、特殊病种登记资料修改[6002]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="ins_zsyb_tsbzdj")
public class InsZsybTsbzdj extends BaseModule  {

	@PK
	@Field(value="PK_INSZSYBTSBZDJ",id=KeyId.UUID)
    private String pkInszsybtsbzdj;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="GRSXH")
    private String grsxh;

	@Field(value="YSGH")
    private String ysgh;

	@Field(value="LXFS")
    private String lxfs;

	@Field(value="LXDH")
    private String lxdh;

	@Field(value="BZDM")
    private String bzdm;

	@Field(value="KSSJ")
    private String kssj;

    /** ZZSJ - 如果是限期病种则必须录入终止日期，但是终止日期与开始日期不能超过72周 */
	@Field(value="ZZSJ")
    private String zzsj;

	@Field(value="TMDJLSH")
    private String tmdjlsh;

	@Field(value="FHZ")
    private Integer fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;



    public String getPkInszsybtsbzdj() {
		return pkInszsybtsbzdj;
	}
	public void setPkInszsybtsbzdj(String pkInszsybtsbzdj) {
		this.pkInszsybtsbzdj = pkInszsybtsbzdj;
	}
	
	public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getGrsxh(){
        return this.grsxh;
    }
    public void setGrsxh(String grsxh){
        this.grsxh = grsxh;
    }

    public String getYsgh(){
        return this.ysgh;
    }
    public void setYsgh(String ysgh){
        this.ysgh = ysgh;
    }

    public String getLxfs(){
        return this.lxfs;
    }
    public void setLxfs(String lxfs){
        this.lxfs = lxfs;
    }

    public String getLxdh(){
        return this.lxdh;
    }
    public void setLxdh(String lxdh){
        this.lxdh = lxdh;
    }

    public String getBzdm(){
        return this.bzdm;
    }
    public void setBzdm(String bzdm){
        this.bzdm = bzdm;
    }

    public String getKssj(){
        return this.kssj;
    }
    public void setKssj(String kssj){
        this.kssj = kssj;
    }

    public String getZzsj(){
        return this.zzsj;
    }
    public void setZzsj(String zzsj){
        this.zzsj = zzsj;
    }

    public String getTmdjlsh(){
        return this.tmdjlsh;
    }
    public void setTmdjlsh(String tmdjlsh){
        this.tmdjlsh = tmdjlsh;
    }

    public Integer getFhz(){
        return this.fhz;
    }
    public void setFhz(Integer fhz){
        this.fhz = fhz;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}