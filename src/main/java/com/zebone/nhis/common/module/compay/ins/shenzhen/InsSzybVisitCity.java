package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_SZYB_VISIT_CITY 
 *
 * @since 2020-01-07 10:41:11
 */
@Table(value="INS_SZYB_VISIT_CITY")
public class InsSzybVisitCity   {

	@PK
	@Field(value="PK_VISITCITY",id=KeyId.UUID)
    private String pkVisitcity;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_VISIT")
    private String pkVisit;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="AAZ515")
    private String aaz515;

	@Field(value="AAZ500")
    private String aaz500;

	@Field(value="AAC001")
    private Long aac001;

	@Field(value="AAC999")
    private String aac999;

	@Field(value="AAC058")
    private String aac058;

	@Field(value="AAC147")
    private String aac147;

	@Field(value="AAC002")
    private String aac002;

	@Field(value="AAC003")
    private String aac003;

	@Field(value="AAC004")
    private String aac004;

	@Field(value="AAC006")
    private Integer aac006;

	@Field(value="BAE093")
    private Integer bae093;

	@Field(value="CAC215")
    private String cac215;

	@Field(value="CKC601")
    private String ckc601;

	@Field(value="CAZ574")
    private String caz574;

	@Field(value="CKC403")
    private String ckc403;

	@Field(value="AAA060")
    private String aaa060;

	@Field(value="AAC042")
    private String aac042;

	@Field(value="CAC999")
    private String cac999;

	@Field(value="AAC043")
    private String aac043;

	@Field(value="AAC044")
    private String aac044;

	@Field(value="AAB999")
    private String aab999;

	@Field(value="AAB004")
    private String aab004;

	@Field(value="AAB019")
    private String aab019;

	@Field(value="AAE140")
    private String aae140;

	@Field(value="AAE030")
    private Integer aae030;

	@Field(value="AAE031")
    private Integer aae031;

	@Field(value="BCC334")
    private String bcc334;

	@Field(value="AAC031")
    private String aac031;

	@Field(value="CKZ220")
    private Long ckz220;

	@Field(value="CKA303")
    private String cka303;

	@Field(value="AKA120")
    private String aka120;

	@Field(value="AAE002")
    private Integer aae002;

	@Field(value="CKE290")
    private Integer cke290;

	@Field(value="CKE291")
    private Integer cke291;

	@Field(value="CKE292")
    private Integer cke292;

	@Field(value="CKE201")
    private Integer cke201;

	@Field(value="CKE202")
    private Integer cke202;

	@Field(value="CKE203")
    private Integer cke203;

	@Field(value="AKA037")
    private Integer aka037;

	@Field(value="AKE053")
    private Double ake053;

	@Field(value="BKE263")
    private Double bke263;

	@Field(value="BKE264")
    private Double bke264;

	@Field(value="AAC066")
    private String aac066;

	@Field(value="AAC008")
    private String aac008;

	@Field(value="BKE404")
    private String bke404;

	@Field(value="AAE240")
    private Double aae240;

	@Field(value="BKC346")
    private String bkc346;

	@Field(value="CKA304")
    private String cka304;

    @Field(value="CKA305")
    private String cka305;

	@Field(value="CME320")
    private Integer cme320;

	@Field(value="AMC021")
    private String amc021;

	@Field(value="CME331")
    private Integer cme331;

	@Field(value="ALC005")
    private String alc005;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;
	
	@Field(value="BKC378")
    private String bkc378;

	private String pkInsu;
	private String persontype;
	private String password;
	
	/** 出院诊断编码*/
    @Field(value="AKC196")
    private String akc196;
    
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAkc196() {
		return akc196;
	}

	public void setAkc196(String akc196) {
		this.akc196 = akc196;
	}

	
    public String getBkc378() {
		return bkc378;
	}
	public void setBkc378(String bkc378) {
		this.bkc378 = bkc378;
	}
	public String getPkInsu() {
		return pkInsu;
	}
	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}
	public String getPersontype() {
		return persontype;
	}
	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}
	public String getPkVisitcity(){
        return this.pkVisitcity;
    }
    public void setPkVisitcity(String pkVisitcity){
        this.pkVisitcity = pkVisitcity;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkVisit(){
        return this.pkVisit;
    }
    public void setPkVisit(String pkVisit){
        this.pkVisit = pkVisit;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getAaz515(){
        return this.aaz515;
    }
    public void setAaz515(String aaz515){
        this.aaz515 = aaz515;
    }

    public String getAaz500(){
        return this.aaz500;
    }
    public void setAaz500(String aaz500){
        this.aaz500 = aaz500;
    }

    public Long getAac001(){
        return this.aac001;
    }
    public void setAac001(Long aac001){
        this.aac001 = aac001;
    }

    public String getAac999(){
        return this.aac999;
    }
    public void setAac999(String aac999){
        this.aac999 = aac999;
    }

    public String getAac058(){
        return this.aac058;
    }
    public void setAac058(String aac058){
        this.aac058 = aac058;
    }

    public String getAac147(){
        return this.aac147;
    }
    public void setAac147(String aac147){
        this.aac147 = aac147;
    }

    public String getAac002(){
        return this.aac002;
    }
    public void setAac002(String aac002){
        this.aac002 = aac002;
    }

    public String getAac003(){
        return this.aac003;
    }
    public void setAac003(String aac003){
        this.aac003 = aac003;
    }

    public String getAac004(){
        return this.aac004;
    }
    public void setAac004(String aac004){
        this.aac004 = aac004;
    }

    public Integer getAac006(){
        return this.aac006;
    }
    public void setAac006(Integer aac006){
        this.aac006 = aac006;
    }

    public Integer getBae093(){
        return this.bae093;
    }
    public void setBae093(Integer bae093){
        this.bae093 = bae093;
    }

    public String getCac215(){
        return this.cac215;
    }
    public void setCac215(String cac215){
        this.cac215 = cac215;
    }

    public String getCkc601(){
        return this.ckc601;
    }
    public void setCkc601(String ckc601){
        this.ckc601 = ckc601;
    }

    public String getCaz574(){
        return this.caz574;
    }
    public void setCaz574(String caz574){
        this.caz574 = caz574;
    }

    public String getCkc403(){
        return this.ckc403;
    }
    public void setCkc403(String ckc403){
        this.ckc403 = ckc403;
    }

    public String getAaa060(){
        return this.aaa060;
    }
    public void setAaa060(String aaa060){
        this.aaa060 = aaa060;
    }

    public String getAac042(){
        return this.aac042;
    }
    public void setAac042(String aac042){
        this.aac042 = aac042;
    }

    public String getCac999(){
        return this.cac999;
    }
    public void setCac999(String cac999){
        this.cac999 = cac999;
    }

    public String getAac043(){
        return this.aac043;
    }
    public void setAac043(String aac043){
        this.aac043 = aac043;
    }

    public String getAac044(){
        return this.aac044;
    }
    public void setAac044(String aac044){
        this.aac044 = aac044;
    }

    public String getAab999(){
        return this.aab999;
    }
    public void setAab999(String aab999){
        this.aab999 = aab999;
    }

    public String getAab004(){
        return this.aab004;
    }
    public void setAab004(String aab004){
        this.aab004 = aab004;
    }

    public String getAab019(){
        return this.aab019;
    }
    public void setAab019(String aab019){
        this.aab019 = aab019;
    }

    public String getAae140(){
        return this.aae140;
    }
    public void setAae140(String aae140){
        this.aae140 = aae140;
    }

    public Integer getAae030(){
        return this.aae030;
    }
    public void setAae030(Integer aae030){
        this.aae030 = aae030;
    }

    public Integer getAae031(){
        return this.aae031;
    }
    public void setAae031(Integer aae031){
        this.aae031 = aae031;
    }

    public String getBcc334(){
        return this.bcc334;
    }
    public void setBcc334(String bcc334){
        this.bcc334 = bcc334;
    }

    public String getAac031(){
        return this.aac031;
    }
    public void setAac031(String aac031){
        this.aac031 = aac031;
    }

    public Long getCkz220(){
        return this.ckz220;
    }
    public void setCkz220(Long ckz220){
        this.ckz220 = ckz220;
    }

    public String getCka303(){
        return this.cka303;
    }
    public void setCka303(String cka303){
        this.cka303 = cka303;
    }

    public String getAka120(){
        return this.aka120;
    }
    public void setAka120(String aka120){
        this.aka120 = aka120;
    }

    public Integer getAae002(){
        return this.aae002;
    }
    public void setAae002(Integer aae002){
        this.aae002 = aae002;
    }

    public Integer getCke290(){
        return this.cke290;
    }
    public void setCke290(Integer cke290){
        this.cke290 = cke290;
    }

    public Integer getCke291(){
        return this.cke291;
    }
    public void setCke291(Integer cke291){
        this.cke291 = cke291;
    }

    public Integer getCke292(){
        return this.cke292;
    }
    public void setCke292(Integer cke292){
        this.cke292 = cke292;
    }

    public Integer getCke201(){
        return this.cke201;
    }
    public void setCke201(Integer cke201){
        this.cke201 = cke201;
    }

    public Integer getCke202(){
        return this.cke202;
    }
    public void setCke202(Integer cke202){
        this.cke202 = cke202;
    }

    public Integer getCke203(){
        return this.cke203;
    }
    public void setCke203(Integer cke203){
        this.cke203 = cke203;
    }

    public Integer getAka037(){
        return this.aka037;
    }
    public void setAka037(Integer aka037){
        this.aka037 = aka037;
    }

    public Double getAke053(){
        return this.ake053;
    }
    public void setAke053(Double ake053){
        this.ake053 = ake053;
    }

    public Double getBke263(){
        return this.bke263;
    }
    public void setBke263(Double bke263){
        this.bke263 = bke263;
    }

    public Double getBke264(){
        return this.bke264;
    }
    public void setBke264(Double bke264){
        this.bke264 = bke264;
    }

    public String getAac066(){
        return this.aac066;
    }
    public void setAac066(String aac066){
        this.aac066 = aac066;
    }

    public String getAac008(){
        return this.aac008;
    }
    public void setAac008(String aac008){
        this.aac008 = aac008;
    }

    public String getBke404(){
        return this.bke404;
    }
    public void setBke404(String bke404){
        this.bke404 = bke404;
    }

    public Double getAae240(){
        return this.aae240;
    }
    public void setAae240(Double aae240){
        this.aae240 = aae240;
    }

    public String getBkc346(){
        return this.bkc346;
    }
    public void setBkc346(String bkc346){
        this.bkc346 = bkc346;
    }

    public String getCka304(){
        return this.cka304;
    }
    public void setCka304(String cka304){
        this.cka304 = cka304;
    }

    public Integer getCme320(){
        return this.cme320;
    }
    public void setCme320(Integer cme320){
        this.cme320 = cme320;
    }

    public String getAmc021(){
        return this.amc021;
    }
    public void setAmc021(String amc021){
        this.amc021 = amc021;
    }

    public Integer getCme331(){
        return this.cme331;
    }
    public void setCme331(Integer cme331){
        this.cme331 = cme331;
    }

    public String getAlc005(){
        return this.alc005;
    }
    public void setAlc005(String alc005){
        this.alc005 = alc005;
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

    public String getCka305() {
        return cka305;
    }

    public void setCka305(String cka305) {
        this.cka305 = cka305;
    }
}