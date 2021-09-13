package com.zebone.nhis.common.module.compay.ins.shenzhen;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: INS_SZYB_ITEM - ins_szyb_item 
 *
 * @since 2019-12-06 11:31:19
 */
@Table(value="INS_SZYB_ITEM")
public class InsSzybItem   {

	@PK
	@Field(value="PK_INSITEM",id=KeyId.UUID)
    private String pkInsitem;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** EU_HPDICTTYPE - 01=深圳医保 */
	@Field(value="EU_HPDICTTYPE")
    private String euHpdicttype;

    /** AKC222 - 省药品编码、省诊疗编码、省材料编码 */
	@Field(value="AKC222")
    private String akc222;

	@Field(value="AKE001")
    private String ake001;

	@Field(value="AKE002")
    private String ake002;

    /** AKE003 - 1=药品，2=诊疗，3=材料，5=辅助器具项目 */
	@Field(value="AKE003")
    private String ake003;

	@Field(value="BKE111")
    private String bke111;

	@Field(value="AKA061")
    private String aka061;

    /** BKM017 - 西药、中成药为药品本位码；中药饮片（包括颗粒）：国家标准编码
（GB/T 31774—2015） */
	@Field(value="BKM017")
    private String bkm017;

	@Field(value="AKA062")
    private String aka062;

	@Field(value="BKM030")
    private String bkm030;

	@Field(value="CKM097")
    private String ckm097;

	@Field(value="CKM098")
    private String ckm098;

	@Field(value="AKA020")
    private String aka020;

    /** AKA111 - 医保大类代码字典 */
	@Field(value="AKA111")
    private String aka111;

    /** AKA036 - 0=否，1=是，限定协议机构使用标记 */
	@Field(value="AKA036")
    private String aka036;

	@Field(value="CKM099")
    private String ckm099;

    /** BKM100 - 10=限二级及以上，11=限三级 */
	@Field(value="BKM100")
    private String bkm100;

    /** BKM101 - 10=限综合医院，11=限专科医院 */
	@Field(value="BKM101")
    private String bkm101;

    /** BKM102 - 10=限肿瘤，11=限精神病 */
	@Field(value="BKM102")
    private String bkm102;

    /** CKM107 - 1=不限制，2=限门诊，3=限住院 */
	@Field(value="CKM107")
    private String ckm107;

	@Field(value="BKM007")
    private String bkm007;

	@Field(value="BKM008")
    private String bkm008;

	@Field(value="BKM009")
    private Integer bkm009;

	@Field(value="BKA053")
    private String bka053;

	@Field(value="AKA074")
    private String aka074;

    /** CKM108 - 诊疗    1=是，0或空=否 */
	@Field(value="CKM108")
    private String ckm108;

    /** CKM109 - 诊疗    1=是，0或空=否 */
	@Field(value="CKM109")
    private String ckm109;

    /** BKM031 - 诊疗    1=综合医疗服务，2=医技诊疗，3=临床诊疗及手术项目类，4=中医及民族医诊疗，9=其它 */
	@Field(value="BKM031")
    private String bkm031;

    /** BKM010 - 诊疗 */
	@Field(value="BKM010")
    private String bkm010;

    /** BKM011 - 诊疗 */
	@Field(value="BKM011")
    private String bkm011;

    /** BKA956 - 诊疗 */
	@Field(value="BKA956")
    private String bka956;

	@Field(value="AKA067")
    private String aka067;

	@Field(value="BKA640")
    private String bka640;

	@Field(value="AKA070")
    private String aka070;

    /** AKA065 - 1 甲类 ，2 乙类 ，3 丙类 */
	@Field(value="AKA065")
    private String aka065;

    /** AKE004 - 材料    1=国内，2=国外 */
	@Field(value="AKE004")
    private String ake004;

    /** ALA026 - 材料 */
	@Field(value="ALA026")
    private String ala026;

    /** BKM032 - 01=基本记帐，02=地补记帐，03=重疾记账，99=自费 */
	@Field(value="BKM032")
    private String bkm032;

    /** AKA031 - 1=西药，2=中成药，3=中药饮片，4=中药颗粒 */
	@Field(value="AKA031")
    private String aka031;

    /** BKM054 - 0=否，1=是 */
	@Field(value="BKM054")
    private String bkm054;

	@Field(value="AKB020")
    private String akb020;

    /** AKA064 - 0=否，1=是 */
	@Field(value="AKA064")
    private String aka064;

    /** AKA022 - 0=否，1=是 */
	@Field(value="AKA022")
    private String aka022;

	@Field(value="BKM033")
    private Integer bkm033;

	@Field(value="BKM034")
    private Integer bkm034;

	@Field(value="BKM035")
    private String bkm035;

	@Field(value="BKM036")
    private Integer bkm036;

	@Field(value="BKM037")
    private Integer bkm037;

	@Field(value="BKM038")
    private String bkm038;

	@Field(value="BKM039")
    private Integer bkm039;

	@Field(value="BKM040")
    private Integer bkm040;

	@Field(value="BKM041")
    private String bkm041;

	@Field(value="BKM042")
    private Integer bkm042;

	@Field(value="BKM043")
    private Integer bkm043;

	@Field(value="BKM044")
    private String bkm044;

	@Field(value="BKM045")
    private Integer bkm045;

	@Field(value="BKM046")
    private Integer bkm046;

	@Field(value="BKM047")
    private String bkm047;

	@Field(value="BKM048")
    private Integer bkm048;

	@Field(value="BKM049")
    private Integer bkm049;

	@Field(value="AMA011")
    private String ama011;

	@Field(value="BMA030")
    private Integer bma030;

	@Field(value="BMA031")
    private Integer bma031;

	@Field(value="ALA011")
    private String ala011;

	@Field(value="BLA050")
    private Integer bla050;

	@Field(value="BLA051")
    private Integer bla051;

	@Field(value="BLA052")
    private String bla052;

	@Field(value="BLA053")
    private Integer bla053;

	@Field(value="BLA054")
    private Integer bla054;

    /** BKM014 - 诊疗 */
	@Field(value="BKM014")
    private Double bkm014;

    /** BKM015 - 诊疗 */
	@Field(value="BKM015")
    private Double bkm015;

    /** BKM016 - 诊疗 */
	@Field(value="BKM016")
    private Double bkm016;

    /** BKM055 - 诊疗 */
	@Field(value="BKM055")
    private Double bkm055;

    /** BKM091 - 诊疗 */
	@Field(value="BKM091")
    private Double bkm091;

    /** BKM092 - 诊疗 */
	@Field(value="BKM092")
    private Double bkm092;

    /** BKM093 - 诊疗 */
	@Field(value="BKM093")
    private Double bkm093;

    /** BKM094 - 诊疗 */
	@Field(value="BKM094")
    private Double bkm094;

	@Field(value="CKM104")
    private String ckm104;

	@Field(value="CKM105")
    private String ckm105;

	@Field(value="CKM106")
    private String ckm106;

	@Field(value="BKM080")
    private Integer bkm080;

	@Field(value="BKM081")
    private Integer bkm081;

	@Field(value="BKM082")
    private Integer bkm082;

	@Field(value="BKM083")
    private Integer bkm083;

	@Field(value="BKM090")
    private Double bkm090;

    /** CKM102 - 1=国家，2=广东省，3=深圳市 */
	@Field(value="CKM102")
    private String ckm102;

	@Field(value="BKM095")
    private Double bkm095;

    /** BKM096 - 诊疗 */
	@Field(value="BKM096")
    private Double bkm096;

    /** BKM097 - 诊疗 */
	@Field(value="BKM097")
    private Double bkm097;

    /** BKM098 - 诊疗 */
	@Field(value="BKM098")
    private Double bkm098;

    /** BKM099 - 诊疗 */
	@Field(value="BKM099")
    private Double bkm099;

	@Field(value="AKA068")
    private Double aka068;

    /** BKM056 - 诊疗 */
	@Field(value="BKM056")
    private Double bkm056;

    /** BKM057 - 诊疗 */
	@Field(value="BKM057")
    private Double bkm057;

    /** BKM058 - 诊疗 */
	@Field(value="BKM058")
    private Double bkm058;

    /** BLA055 - 材料 */
	@Field(value="BLA055")
    private String bla055;

    /** BLA056 - 材料 */
	@Field(value="BLA056")
    private Integer bla056;

    /** BLA057 - 材料 */
	@Field(value="BLA057")
    private Integer bla057;

    /** BKM059 - 诊疗 */
	@Field(value="BKM059")
    private Double bkm059;

	@Field(value="AAE396")
    private Integer aae396;

	@Field(value="AAE013")
    private String aae013;

	@Field(value="CKE900")
    private String cke900;

	@Field(value="CKE901")
    private String cke901;

	@Field(value="CKE902")
    private String cke902;

	@Field(value="CKE903")
    private String cke903;

	@Field(value="CKE904")
    private String cke904;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkInsitem(){
        return this.pkInsitem;
    }
    public void setPkInsitem(String pkInsitem){
        this.pkInsitem = pkInsitem;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getEuHpdicttype(){
        return this.euHpdicttype;
    }
    public void setEuHpdicttype(String euHpdicttype){
        this.euHpdicttype = euHpdicttype;
    }

    public String getAkc222(){
        return this.akc222;
    }
    public void setAkc222(String akc222){
        this.akc222 = akc222;
    }

    public String getAke001(){
        return this.ake001;
    }
    public void setAke001(String ake001){
        this.ake001 = ake001;
    }

    public String getAke002(){
        return this.ake002;
    }
    public void setAke002(String ake002){
        this.ake002 = ake002;
    }

    public String getAke003(){
        return this.ake003;
    }
    public void setAke003(String ake003){
        this.ake003 = ake003;
    }

    public String getBke111(){
        return this.bke111;
    }
    public void setBke111(String bke111){
        this.bke111 = bke111;
    }

    public String getAka061(){
        return this.aka061;
    }
    public void setAka061(String aka061){
        this.aka061 = aka061;
    }

    public String getBkm017(){
        return this.bkm017;
    }
    public void setBkm017(String bkm017){
        this.bkm017 = bkm017;
    }

    public String getAka062(){
        return this.aka062;
    }
    public void setAka062(String aka062){
        this.aka062 = aka062;
    }

    public String getBkm030(){
        return this.bkm030;
    }
    public void setBkm030(String bkm030){
        this.bkm030 = bkm030;
    }

    public String getCkm097(){
        return this.ckm097;
    }
    public void setCkm097(String ckm097){
        this.ckm097 = ckm097;
    }

    public String getCkm098(){
        return this.ckm098;
    }
    public void setCkm098(String ckm098){
        this.ckm098 = ckm098;
    }

    public String getAka020(){
        return this.aka020;
    }
    public void setAka020(String aka020){
        this.aka020 = aka020;
    }

    public String getAka111(){
        return this.aka111;
    }
    public void setAka111(String aka111){
        this.aka111 = aka111;
    }

    public String getAka036(){
        return this.aka036;
    }
    public void setAka036(String aka036){
        this.aka036 = aka036;
    }

    public String getCkm099(){
        return this.ckm099;
    }
    public void setCkm099(String ckm099){
        this.ckm099 = ckm099;
    }

    public String getBkm100(){
        return this.bkm100;
    }
    public void setBkm100(String bkm100){
        this.bkm100 = bkm100;
    }

    public String getBkm101(){
        return this.bkm101;
    }
    public void setBkm101(String bkm101){
        this.bkm101 = bkm101;
    }

    public String getBkm102(){
        return this.bkm102;
    }
    public void setBkm102(String bkm102){
        this.bkm102 = bkm102;
    }

    public String getCkm107(){
        return this.ckm107;
    }
    public void setCkm107(String ckm107){
        this.ckm107 = ckm107;
    }

    public String getBkm007(){
        return this.bkm007;
    }
    public void setBkm007(String bkm007){
        this.bkm007 = bkm007;
    }

    public String getBkm008(){
        return this.bkm008;
    }
    public void setBkm008(String bkm008){
        this.bkm008 = bkm008;
    }

    public Integer getBkm009(){
        return this.bkm009;
    }
    public void setBkm009(Integer bkm009){
        this.bkm009 = bkm009;
    }

    public String getBka053(){
        return this.bka053;
    }
    public void setBka053(String bka053){
        this.bka053 = bka053;
    }

    public String getAka074(){
        return this.aka074;
    }
    public void setAka074(String aka074){
        this.aka074 = aka074;
    }

    public String getCkm108(){
        return this.ckm108;
    }
    public void setCkm108(String ckm108){
        this.ckm108 = ckm108;
    }

    public String getCkm109(){
        return this.ckm109;
    }
    public void setCkm109(String ckm109){
        this.ckm109 = ckm109;
    }

    public String getBkm031(){
        return this.bkm031;
    }
    public void setBkm031(String bkm031){
        this.bkm031 = bkm031;
    }

    public String getBkm010(){
        return this.bkm010;
    }
    public void setBkm010(String bkm010){
        this.bkm010 = bkm010;
    }

    public String getBkm011(){
        return this.bkm011;
    }
    public void setBkm011(String bkm011){
        this.bkm011 = bkm011;
    }

    public String getBka956(){
        return this.bka956;
    }
    public void setBka956(String bka956){
        this.bka956 = bka956;
    }

    public String getAka067(){
        return this.aka067;
    }
    public void setAka067(String aka067){
        this.aka067 = aka067;
    }

    public String getBka640(){
        return this.bka640;
    }
    public void setBka640(String bka640){
        this.bka640 = bka640;
    }

    public String getAka070(){
        return this.aka070;
    }
    public void setAka070(String aka070){
        this.aka070 = aka070;
    }

    public String getAka065(){
        return this.aka065;
    }
    public void setAka065(String aka065){
        this.aka065 = aka065;
    }

    public String getAke004(){
        return this.ake004;
    }
    public void setAke004(String ake004){
        this.ake004 = ake004;
    }

    public String getAla026(){
        return this.ala026;
    }
    public void setAla026(String ala026){
        this.ala026 = ala026;
    }

    public String getBkm032(){
        return this.bkm032;
    }
    public void setBkm032(String bkm032){
        this.bkm032 = bkm032;
    }

    public String getAka031(){
        return this.aka031;
    }
    public void setAka031(String aka031){
        this.aka031 = aka031;
    }

    public String getBkm054(){
        return this.bkm054;
    }
    public void setBkm054(String bkm054){
        this.bkm054 = bkm054;
    }

    public String getAkb020(){
        return this.akb020;
    }
    public void setAkb020(String akb020){
        this.akb020 = akb020;
    }

    public String getAka064(){
        return this.aka064;
    }
    public void setAka064(String aka064){
        this.aka064 = aka064;
    }

    public String getAka022(){
        return this.aka022;
    }
    public void setAka022(String aka022){
        this.aka022 = aka022;
    }

    public Integer getBkm033(){
        return this.bkm033;
    }
    public void setBkm033(Integer bkm033){
        this.bkm033 = bkm033;
    }

    public Integer getBkm034(){
        return this.bkm034;
    }
    public void setBkm034(Integer bkm034){
        this.bkm034 = bkm034;
    }

    public String getBkm035(){
        return this.bkm035;
    }
    public void setBkm035(String bkm035){
        this.bkm035 = bkm035;
    }

    public Integer getBkm036(){
        return this.bkm036;
    }
    public void setBkm036(Integer bkm036){
        this.bkm036 = bkm036;
    }

    public Integer getBkm037(){
        return this.bkm037;
    }
    public void setBkm037(Integer bkm037){
        this.bkm037 = bkm037;
    }

    public String getBkm038(){
        return this.bkm038;
    }
    public void setBkm038(String bkm038){
        this.bkm038 = bkm038;
    }

    public Integer getBkm039(){
        return this.bkm039;
    }
    public void setBkm039(Integer bkm039){
        this.bkm039 = bkm039;
    }

    public Integer getBkm040(){
        return this.bkm040;
    }
    public void setBkm040(Integer bkm040){
        this.bkm040 = bkm040;
    }

    public String getBkm041(){
        return this.bkm041;
    }
    public void setBkm041(String bkm041){
        this.bkm041 = bkm041;
    }

    public Integer getBkm042(){
        return this.bkm042;
    }
    public void setBkm042(Integer bkm042){
        this.bkm042 = bkm042;
    }

    public Integer getBkm043(){
        return this.bkm043;
    }
    public void setBkm043(Integer bkm043){
        this.bkm043 = bkm043;
    }

    public String getBkm044(){
        return this.bkm044;
    }
    public void setBkm044(String bkm044){
        this.bkm044 = bkm044;
    }

    public Integer getBkm045(){
        return this.bkm045;
    }
    public void setBkm045(Integer bkm045){
        this.bkm045 = bkm045;
    }

    public Integer getBkm046(){
        return this.bkm046;
    }
    public void setBkm046(Integer bkm046){
        this.bkm046 = bkm046;
    }

    public String getBkm047(){
        return this.bkm047;
    }
    public void setBkm047(String bkm047){
        this.bkm047 = bkm047;
    }

    public Integer getBkm048(){
        return this.bkm048;
    }
    public void setBkm048(Integer bkm048){
        this.bkm048 = bkm048;
    }

    public Integer getBkm049(){
        return this.bkm049;
    }
    public void setBkm049(Integer bkm049){
        this.bkm049 = bkm049;
    }

    public String getAma011(){
        return this.ama011;
    }
    public void setAma011(String ama011){
        this.ama011 = ama011;
    }

    public Integer getBma030(){
        return this.bma030;
    }
    public void setBma030(Integer bma030){
        this.bma030 = bma030;
    }

    public Integer getBma031(){
        return this.bma031;
    }
    public void setBma031(Integer bma031){
        this.bma031 = bma031;
    }

    public String getAla011(){
        return this.ala011;
    }
    public void setAla011(String ala011){
        this.ala011 = ala011;
    }

    public Integer getBla050(){
        return this.bla050;
    }
    public void setBla050(Integer bla050){
        this.bla050 = bla050;
    }

    public Integer getBla051(){
        return this.bla051;
    }
    public void setBla051(Integer bla051){
        this.bla051 = bla051;
    }

    public String getBla052(){
        return this.bla052;
    }
    public void setBla052(String bla052){
        this.bla052 = bla052;
    }

    public Integer getBla053(){
        return this.bla053;
    }
    public void setBla053(Integer bla053){
        this.bla053 = bla053;
    }

    public Integer getBla054(){
        return this.bla054;
    }
    public void setBla054(Integer bla054){
        this.bla054 = bla054;
    }

    public Double getBkm014(){
        return this.bkm014;
    }
    public void setBkm014(Double bkm014){
        this.bkm014 = bkm014;
    }

    public Double getBkm015(){
        return this.bkm015;
    }
    public void setBkm015(Double bkm015){
        this.bkm015 = bkm015;
    }

    public Double getBkm016(){
        return this.bkm016;
    }
    public void setBkm016(Double bkm016){
        this.bkm016 = bkm016;
    }

    public Double getBkm055(){
        return this.bkm055;
    }
    public void setBkm055(Double bkm055){
        this.bkm055 = bkm055;
    }

    public Double getBkm091(){
        return this.bkm091;
    }
    public void setBkm091(Double bkm091){
        this.bkm091 = bkm091;
    }

    public Double getBkm092(){
        return this.bkm092;
    }
    public void setBkm092(Double bkm092){
        this.bkm092 = bkm092;
    }

    public Double getBkm093(){
        return this.bkm093;
    }
    public void setBkm093(Double bkm093){
        this.bkm093 = bkm093;
    }

    public Double getBkm094(){
        return this.bkm094;
    }
    public void setBkm094(Double bkm094){
        this.bkm094 = bkm094;
    }

    public String getCkm104(){
        return this.ckm104;
    }
    public void setCkm104(String ckm104){
        this.ckm104 = ckm104;
    }

    public String getCkm105(){
        return this.ckm105;
    }
    public void setCkm105(String ckm105){
        this.ckm105 = ckm105;
    }

    public String getCkm106(){
        return this.ckm106;
    }
    public void setCkm106(String ckm106){
        this.ckm106 = ckm106;
    }

    public Integer getBkm080(){
        return this.bkm080;
    }
    public void setBkm080(Integer bkm080){
        this.bkm080 = bkm080;
    }

    public Integer getBkm081(){
        return this.bkm081;
    }
    public void setBkm081(Integer bkm081){
        this.bkm081 = bkm081;
    }

    public Integer getBkm082(){
        return this.bkm082;
    }
    public void setBkm082(Integer bkm082){
        this.bkm082 = bkm082;
    }

    public Integer getBkm083(){
        return this.bkm083;
    }
    public void setBkm083(Integer bkm083){
        this.bkm083 = bkm083;
    }

    public Double getBkm090(){
        return this.bkm090;
    }
    public void setBkm090(Double bkm090){
        this.bkm090 = bkm090;
    }

    public String getCkm102(){
        return this.ckm102;
    }
    public void setCkm102(String ckm102){
        this.ckm102 = ckm102;
    }

    public Double getBkm095(){
        return this.bkm095;
    }
    public void setBkm095(Double bkm095){
        this.bkm095 = bkm095;
    }

    public Double getBkm096(){
        return this.bkm096;
    }
    public void setBkm096(Double bkm096){
        this.bkm096 = bkm096;
    }

    public Double getBkm097(){
        return this.bkm097;
    }
    public void setBkm097(Double bkm097){
        this.bkm097 = bkm097;
    }

    public Double getBkm098(){
        return this.bkm098;
    }
    public void setBkm098(Double bkm098){
        this.bkm098 = bkm098;
    }

    public Double getBkm099(){
        return this.bkm099;
    }
    public void setBkm099(Double bkm099){
        this.bkm099 = bkm099;
    }

    public Double getAka068(){
        return this.aka068;
    }
    public void setAka068(Double aka068){
        this.aka068 = aka068;
    }

    public Double getBkm056(){
        return this.bkm056;
    }
    public void setBkm056(Double bkm056){
        this.bkm056 = bkm056;
    }

    public Double getBkm057(){
        return this.bkm057;
    }
    public void setBkm057(Double bkm057){
        this.bkm057 = bkm057;
    }

    public Double getBkm058(){
        return this.bkm058;
    }
    public void setBkm058(Double bkm058){
        this.bkm058 = bkm058;
    }

    public String getBla055(){
        return this.bla055;
    }
    public void setBla055(String bla055){
        this.bla055 = bla055;
    }

    public Integer getBla056(){
        return this.bla056;
    }
    public void setBla056(Integer bla056){
        this.bla056 = bla056;
    }

    public Integer getBla057(){
        return this.bla057;
    }
    public void setBla057(Integer bla057){
        this.bla057 = bla057;
    }

    public Double getBkm059(){
        return this.bkm059;
    }
    public void setBkm059(Double bkm059){
        this.bkm059 = bkm059;
    }

    public Integer getAae396(){
        return this.aae396;
    }
    public void setAae396(Integer aae396){
        this.aae396 = aae396;
    }

    public String getAae013(){
        return this.aae013;
    }
    public void setAae013(String aae013){
        this.aae013 = aae013;
    }

    public String getCke900(){
        return this.cke900;
    }
    public void setCke900(String cke900){
        this.cke900 = cke900;
    }

    public String getCke901(){
        return this.cke901;
    }
    public void setCke901(String cke901){
        this.cke901 = cke901;
    }

    public String getCke902(){
        return this.cke902;
    }
    public void setCke902(String cke902){
        this.cke902 = cke902;
    }

    public String getCke903(){
        return this.cke903;
    }
    public void setCke903(String cke903){
        this.cke903 = cke903;
    }

    public String getCke904(){
        return this.cke904;
    }
    public void setCke904(String cke904){
        this.cke904 = cke904;
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
}