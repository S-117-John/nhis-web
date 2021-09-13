package com.zebone.nhis.compay.ins.shenzhen.vo.yd;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_STCLEAR - ins_szyb_stclear 
 *
 * @since 2019-12-14 10:20:27
 */
@Table(value="INS_SZYB_STCLEAR")
public class InsSzybStclear   {

    /** PK_INSSTCLEAR - 主键 */
	@PK
	@Field(value="PK_INSSTCLEAR",id=KeyId.UUID)
    private String pkInsstclear;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="YZZ060")
    private String yzz060;

	@Field(value="YZZ061")
    private String yzz061;

    /** AAB299 - 地市编码 */
	@Field(value="AAB299")
    private String aab299;

	@Field(value="AAC044")
    private String aac044;

    /** YKC700 - 就诊业务标识’S’+参保地统筹地市区编 号(6 位)＋ 日期（6 位YYMMDD）＋流水号 （7 位） */
	@Field(value="YKC700")
    private String ykc700;

    /** AKC194 - 格式：YYYYMMDDHH24MISS */
	@Field(value="AKC194")
    private Date akc194;

	@Field(value="AAZ216")
    private String aaz216;

	@Field(value="AKE105")
    private String ake105;

	@Field(value="AKC264")
    private Double akc264;

	@Field(value="AKE149")
    private Double ake149;

    /** YKC707 - 0：不确认，本次不纳入清分；1：确认，纳入本次清分； */
	@Field(value="YKC707")
    private String ykc707;

    /** TRANSID - 确认清分结果成功时保存 */
	@Field(value="TRANSID")
    private String transid;

	@Field(value="YZZ022")
    private String yzz022;

	@Field(value="AKC265")
    private Double akc265;

	@Field(value="AKB081")
    private Double akb081;

	@Field(value="YZZ062")
    private String yzz062;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkInsstclear(){
        return this.pkInsstclear;
    }
    public void setPkInsstclear(String pkInsstclear){
        this.pkInsstclear = pkInsstclear;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
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

    public String getAab299(){
        return this.aab299;
    }
    public void setAab299(String aab299){
        this.aab299 = aab299;
    }

    public String getAac044(){
        return this.aac044;
    }
    public void setAac044(String aac044){
        this.aac044 = aac044;
    }

    public String getYkc700(){
        return this.ykc700;
    }
    public void setYkc700(String ykc700){
        this.ykc700 = ykc700;
    }

    public Date getAkc194(){
        return this.akc194;
    }
    public void setAkc194(Date akc194){
        this.akc194 = akc194;
    }

    public String getAaz216(){
        return this.aaz216;
    }
    public void setAaz216(String aaz216){
        this.aaz216 = aaz216;
    }

    public String getAke105(){
        return this.ake105;
    }
    public void setAke105(String ake105){
        this.ake105 = ake105;
    }

    public Double getAkc264(){
        return this.akc264;
    }
    public void setAkc264(Double akc264){
        this.akc264 = akc264;
    }

    public Double getAke149(){
        return this.ake149;
    }
    public void setAke149(Double ake149){
        this.ake149 = ake149;
    }

    public String getYkc707(){
        return this.ykc707;
    }
    public void setYkc707(String ykc707){
        this.ykc707 = ykc707;
    }

    public String getTransid(){
        return this.transid;
    }
    public void setTransid(String transid){
        this.transid = transid;
    }

    public String getYzz022(){
        return this.yzz022;
    }
    public void setYzz022(String yzz022){
        this.yzz022 = yzz022;
    }

    public Double getAkc265(){
        return this.akc265;
    }
    public void setAkc265(Double akc265){
        this.akc265 = akc265;
    }

    public Double getAkb081(){
        return this.akb081;
    }
    public void setAkb081(Double akb081){
        this.akb081 = akb081;
    }

    public String getYzz062(){
        return this.yzz062;
    }
    public void setYzz062(String yzz062){
        this.yzz062 = yzz062;
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