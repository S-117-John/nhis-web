package com.zebone.nhis.compay.ins.shenzhen.vo.yd;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_STCLRESULT - ins_szyb_stclresult 
 *
 * @since 2019-12-30 03:20:59
 */
@Table(value="INS_SZYB_STCLRESULT")
public class InsSzybStclresult   {

    /** PK_INSSTCLRESULT - 主键 */
	@PK
	@Field(value="PK_INSSTCLRESULT",id=KeyId.UUID)
    private String pkInsstclresult;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** EU_HPTYPE - 02=省内异地,03=跨省异地 */
	@Field(value="EU_HPTYPE")
    private String euHptype;

	@Field(value="YZZ041")
    private Date yzz041;

	@Field(value="YZZ042")
    private Date yzz042;

	@Field(value="YZZ060")
    private String yzz060;

	@Field(value="YZZ061")
    private String yzz061;

	@Field(value="SEQNO")
    private Long seqno;

	@Field(value="AKB026")
    private String akb026;

	@Field(value="AKB021")
    private String akb021;

    /** AAB301 - 地市编码 */
	@Field(value="AAB301")
    private String aab301;

	@Field(value="AAC002")
    private String aac002;

	@Field(value="AAC043")
    private String aac043;

    /** AAC044 - 以社会保障号为主，当证件类型为“社会保障卡”时，aac044要与aac002一致 */
	@Field(value="AAC044")
    private String aac044;

	@Field(value="AAC003")
    private String aac003;

	@Field(value="YKC021")
    private String ykc021;

	@Field(value="AKA130")
    private String aka130;

	@Field(value="AKC190")
    private String akc190;

	@Field(value="AKB068")
    private Double akb068;

	@Field(value="YZZ134")
    private String yzz134;

	@Field(value="YZZ135")
    private String yzz135;

	@Field(value="YZZ136")
    private String yzz136;

	@Field(value="YZZ137")
    private Date yzz137;

	@Field(value="YZZ138")
    private String yzz138;

	@Field(value="YZZ065")
    private String yzz065;

    /** YZZ062 - 申报业务标识’B’+就医地统筹地市区编 号(6 位)＋ 年度（YY）+月份（MM）＋流水号 （7 位） */
	@Field(value="YZZ062")
    private String yzz062;

    /** YKC700 - 就诊业务标识’S’+参保地统筹地市区编 号(6 位)＋ 日期（6 位YYMMDD）＋流水号 （7 位） */
	@Field(value="YKC700")
    private String ykc700;

    /** YKC618 - 与表ins_szyb_stclear表结算流水号aaz216对应
结算业务标识’A’+参保地统筹地市区编 号(6 位)＋ 日期（6 位YYMMDD）＋流水号 （7 位） */
	@Field(value="YKC618")
    private String ykc618;

	@Field(value="AKA105")
    private String aka105;

    /** YZZ022 - 1：通过；2：扣款；3：拒付；4：延期支付；0：补回 */
	@Field(value="YZZ022")
    private String yzz022;

    /** AAZ212 - 跨省异地 */
	@Field(value="AAZ212")
    private String aaz212;

    /** AKC265 - 负数表示扣除，正数表示补回 */
	@Field(value="AKC265")
    private Double akc265;

    /** AKC269 - 跨省异地 */
	@Field(value="AKC269")
    private String akc269;

	@Field(value="YZZ024")
    private String yzz024;

    /** YZZ080 - 01:参保人，02：医生，03：医院，11：医生、医院共担 */
	@Field(value="YZZ080")
    private String yzz080;

    /** AAE011 - 跨省异地 */
	@Field(value="AAE011")
    private String aae011;

    /** AAE036 - 跨省异地 */
	@Field(value="AAE036")
    private Date aae036;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkInsstclresult(){
        return this.pkInsstclresult;
    }
    public void setPkInsstclresult(String pkInsstclresult){
        this.pkInsstclresult = pkInsstclresult;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getEuHptype(){
        return this.euHptype;
    }
    public void setEuHptype(String euHptype){
        this.euHptype = euHptype;
    }

    public Date getYzz041(){
        return this.yzz041;
    }
    public void setYzz041(Date yzz041){
        this.yzz041 = yzz041;
    }

    public Date getYzz042(){
        return this.yzz042;
    }
    public void setYzz042(Date yzz042){
        this.yzz042 = yzz042;
    }

    public String getYzz060(){
        return this.yzz060;
    }
    public void setYzz060(String yzz060){
        this.yzz060 = yzz060;
    }

    public String getYzz061(){
        return this.yzz061;
    }
    public void setYzz061(String yzz061){
        this.yzz061 = yzz061;
    }

    public Long getSeqno(){
        return this.seqno;
    }
    public void setSeqno(Long seqno){
        this.seqno = seqno;
    }

    public String getAkb026(){
        return this.akb026;
    }
    public void setAkb026(String akb026){
        this.akb026 = akb026;
    }

    public String getAkb021(){
        return this.akb021;
    }
    public void setAkb021(String akb021){
        this.akb021 = akb021;
    }

    public String getAab301(){
        return this.aab301;
    }
    public void setAab301(String aab301){
        this.aab301 = aab301;
    }

    public String getAac002(){
        return this.aac002;
    }
    public void setAac002(String aac002){
        this.aac002 = aac002;
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

    public String getAac003(){
        return this.aac003;
    }
    public void setAac003(String aac003){
        this.aac003 = aac003;
    }

    public String getYkc021(){
        return this.ykc021;
    }
    public void setYkc021(String ykc021){
        this.ykc021 = ykc021;
    }

    public String getAka130(){
        return this.aka130;
    }
    public void setAka130(String aka130){
        this.aka130 = aka130;
    }

    public String getAkc190(){
        return this.akc190;
    }
    public void setAkc190(String akc190){
        this.akc190 = akc190;
    }

    public Double getAkb068(){
        return this.akb068;
    }
    public void setAkb068(Double akb068){
        this.akb068 = akb068;
    }

    public String getYzz134(){
        return this.yzz134;
    }
    public void setYzz134(String yzz134){
        this.yzz134 = yzz134;
    }

    public String getYzz135(){
        return this.yzz135;
    }
    public void setYzz135(String yzz135){
        this.yzz135 = yzz135;
    }

    public String getYzz136(){
        return this.yzz136;
    }
    public void setYzz136(String yzz136){
        this.yzz136 = yzz136;
    }

    public Date getYzz137(){
        return this.yzz137;
    }
    public void setYzz137(Date yzz137){
        this.yzz137 = yzz137;
    }

    public String getYzz138(){
        return this.yzz138;
    }
    public void setYzz138(String yzz138){
        this.yzz138 = yzz138;
    }

    public String getYzz065(){
        return this.yzz065;
    }
    public void setYzz065(String yzz065){
        this.yzz065 = yzz065;
    }

    public String getYzz062(){
        return this.yzz062;
    }
    public void setYzz062(String yzz062){
        this.yzz062 = yzz062;
    }

    public String getYkc700(){
        return this.ykc700;
    }
    public void setYkc700(String ykc700){
        this.ykc700 = ykc700;
    }

    public String getYkc618(){
        return this.ykc618;
    }
    public void setYkc618(String ykc618){
        this.ykc618 = ykc618;
    }

    public String getAka105(){
        return this.aka105;
    }
    public void setAka105(String aka105){
        this.aka105 = aka105;
    }

    public String getYzz022(){
        return this.yzz022;
    }
    public void setYzz022(String yzz022){
        this.yzz022 = yzz022;
    }

    public String getAaz212(){
        return this.aaz212;
    }
    public void setAaz212(String aaz212){
        this.aaz212 = aaz212;
    }

    public Double getAkc265(){
        return this.akc265;
    }
    public void setAkc265(Double akc265){
        this.akc265 = akc265;
    }

    public String getAkc269(){
        return this.akc269;
    }
    public void setAkc269(String akc269){
        this.akc269 = akc269;
    }

    public String getYzz024(){
        return this.yzz024;
    }
    public void setYzz024(String yzz024){
        this.yzz024 = yzz024;
    }

    public String getYzz080(){
        return this.yzz080;
    }
    public void setYzz080(String yzz080){
        this.yzz080 = yzz080;
    }

    public String getAae011(){
        return this.aae011;
    }
    public void setAae011(String aae011){
        this.aae011 = aae011;
    }

    public Date getAae036(){
        return this.aae036;
    }
    public void setAae036(Date aae036){
        this.aae036 = aae036;
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