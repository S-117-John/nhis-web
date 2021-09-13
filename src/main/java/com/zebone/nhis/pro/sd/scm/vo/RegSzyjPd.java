package com.zebone.nhis.pro.sd.scm.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: REG_SZYJ_PD - reg_szyj_pd
深圳药监管理系统 
 *
 * @since 2019-12-26 04:57:41
 */
@Table(value="REG_SZYJ_PD")
public class RegSzyjPd   {

	@PK
	@Field(value="PK_REGPD",id=KeyId.UUID)
    private String pkRegpd;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** YPBM - 市药品交易监管系统发布的对药品的统编代码 */
	@Field(value="YPBM")
    private String ypbm;

    /** TYM - 药品通用名 */
	@Field(value="TYM")
    private String tym;

    /** CPM - 药品生产企业产品名 */
	@Field(value="CPM")
    private String cpm;

	@Field(value="YWM")
    private String ywm;

    /** SPM - 药品生产企业商品名 */
	@Field(value="SPM")
    private String spm;

	@Field(value="JXMC")
    private String jxmc;

	@Field(value="GG")
    private String gg;

    /** SCQYMC - 药品生产企业名称 */
	@Field(value="SCQYMC")
    private String scqymc;

	@Field(value="PZWH")
    private String pzwh;

    /** ZXYFL - 01为西药，02为中成药，03为中草药 */
	@Field(value="ZXYFL")
    private String zxyfl;

	@Field(value="BZCZ")
    private String bzcz;

    /** BZDW - 药品采购配送单位 */
	@Field(value="BZDW")
    private String bzdw;

	@Field(value="BZSL")
    private Long bzsl;

	@Field(value="BZGG")
    private String bzgg;

	@Field(value="DWZHB")
    private Long dwzhb;

    /** TZMS - 药品大件包装描述 */
	@Field(value="TZMS")
    private String tzms;

	@Field(value="SFGPO")
    private String sfgpo;

    /** GPOBM - 市药品交易监管系统发布的对GPO的统编代码 */
	@Field(value="GPOBM")
    private String gpobm;

	@Field(value="SFJY")
    private String sfjy;

    /** BCGXSJ - YYYY-MM-DD hh:mm:ss */
	@Field(value="BCGXSJ")
    private Date bcgxsj;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkRegpd(){
        return this.pkRegpd;
    }
    public void setPkRegpd(String pkRegpd){
        this.pkRegpd = pkRegpd;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getYpbm(){
        return this.ypbm;
    }
    public void setYpbm(String ypbm){
        this.ypbm = ypbm;
    }

    public String getTym(){
        return this.tym;
    }
    public void setTym(String tym){
        this.tym = tym;
    }

    public String getCpm(){
        return this.cpm;
    }
    public void setCpm(String cpm){
        this.cpm = cpm;
    }

    public String getYwm(){
        return this.ywm;
    }
    public void setYwm(String ywm){
        this.ywm = ywm;
    }

    public String getSpm(){
        return this.spm;
    }
    public void setSpm(String spm){
        this.spm = spm;
    }

    public String getJxmc(){
        return this.jxmc;
    }
    public void setJxmc(String jxmc){
        this.jxmc = jxmc;
    }

    public String getGg(){
        return this.gg;
    }
    public void setGg(String gg){
        this.gg = gg;
    }

    public String getScqymc(){
        return this.scqymc;
    }
    public void setScqymc(String scqymc){
        this.scqymc = scqymc;
    }

    public String getPzwh(){
        return this.pzwh;
    }
    public void setPzwh(String pzwh){
        this.pzwh = pzwh;
    }

    public String getZxyfl(){
        return this.zxyfl;
    }
    public void setZxyfl(String zxyfl){
        this.zxyfl = zxyfl;
    }

    public String getBzcz(){
        return this.bzcz;
    }
    public void setBzcz(String bzcz){
        this.bzcz = bzcz;
    }

    public String getBzdw(){
        return this.bzdw;
    }
    public void setBzdw(String bzdw){
        this.bzdw = bzdw;
    }

    public Long getBzsl(){
        return this.bzsl;
    }
    public void setBzsl(Long bzsl){
        this.bzsl = bzsl;
    }

    public String getBzgg(){
        return this.bzgg;
    }
    public void setBzgg(String bzgg){
        this.bzgg = bzgg;
    }

    public Long getDwzhb(){
        return this.dwzhb;
    }
    public void setDwzhb(Long dwzhb){
        this.dwzhb = dwzhb;
    }

    public String getTzms(){
        return this.tzms;
    }
    public void setTzms(String tzms){
        this.tzms = tzms;
    }

    public String getSfgpo(){
        return this.sfgpo;
    }
    public void setSfgpo(String sfgpo){
        this.sfgpo = sfgpo;
    }

    public String getGpobm(){
        return this.gpobm;
    }
    public void setGpobm(String gpobm){
        this.gpobm = gpobm;
    }

    public String getSfjy(){
        return this.sfjy;
    }
    public void setSfjy(String sfjy){
        this.sfjy = sfjy;
    }

    public Date getBcgxsj(){
        return this.bcgxsj;
    }
    public void setBcgxsj(Date bcgxsj){
        this.bcgxsj = bcgxsj;
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