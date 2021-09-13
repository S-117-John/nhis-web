package com.zebone.nhis.common.module.compay.ins.shenzhen;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 深圳医保医师备案
 * @author 李晋
 */
@Table(value="INS_SZYB_PHYSIRECORD")
public class InsSzybPhysirecord implements Serializable {
    /**
     * 医师主键
     */
    @PK
    @Field(value="PK_PHYSIRECORD",id= Field.KeyId.UUID)
    private String pkPhysirecord;

    /**
     * 所属机构
     */
    @Field(value="pk_org",userfield="pkOrg",userfieldscop= Field.FieldType.INSERT)
    private String pkOrg;

    @Field(value="PK_EMP")
    private String pkEmp;

    /**
     * 现从事专业名称
     */
    @Field(value="BKC322")
    private String bkc322;

    /**
     * 码表000003
     */
    @Field(value="AAC005")
    private String aac005;

    /**
     * 来自基础表AAC011
     */
    @Field(value="AAC011")
    private String aac011;

    /**
     * 0=不在职、1=在职、2=返聘、3=临聘、4=返聘不满半年、5=临聘不满半年
     */
    @Field(value="BKC321")
    private String bkc321;

    /**
     * 1 医师、2药师
     */
    @Field(value="BKA633")
    private String bka633;

    /**
     * 所学的专业名称
     */
    @Field(value="AAC183")
    private String aac183;

    /**
     * 医（药）师执业证书编码
     */
    @Field(value="BKC323")
    private String bkc323;

    /**
     * 医（药）师资格证编码
     */
    @Field(value="BKE955")
    private String bke955;

    /**
     * 来自对照字典AKC055
     */
    @Field(value="AKC055")
    private String akc055;

    /**
     * 来自基础表BCC950
     */
    @Field(value="BCC950")
    private String bcc950;

    /**
     * 来自基础表BCC955
     */
    @Field(value="BCC955")
    private String bcc955;

    /**
     * 医保医师编号
     */
    @Field(value="BKA503")
    private String bka503;

    /**
     * 1=是 0=否
     */
    @Field(value="BKC324")
    private String bkc324;

    /**
     * 1=是 0=否
     */
    @Field(value="BKC326")
    private String bkc326;

    /**
     * 1=是 0=否
     */
    @Field(value="BKC328")
    private String bkc328;

    /**
     * 1=是 0=否
     */
    @Field(value="BKC329")
    private String bkc329;

    /**
     * 母婴保健技术考核合格证书编号
     */
    @Field(value="BKE801")
    private String bke801;

    /**
     * 计划生育技术服务人员合格证编号
     */
    @Field(value="BKC330")
    private String bkc330;

    /**
     * 执业药师注册证编号
     */
    @Field(value="BKC335")
    private String bkc335;

    /**
     * 职务名称
     */
    @Field(value="AHC451")
    private String ahc451;

    /**
     * 来自对照字典AAF009
     */
    @Field(value="AAF009")
    private String aaf009;

    /**
     * 1=是 0=否
     */
    @Field(value="CKC302")
    private String ckc302;

    /**
     * 1=是 0=否
     */
    @Field(value="BKC325")
    private String bkc325;

    /**
     * 来自基础表CKA303
     */
    @Field(value="CKC304")
    private String ckc304;

    /**
     * 开始日期
     */
    @Field(value="AAE030")
    private Date aae030;

    /**
     * 结束日期
     */
    @Field(value="AAE031")
    private Date aae031;

    /**
     * 来自基础表BKC332
     */
    @Field(value="BKC332")
    private String bkc332;

    /**
     * 来自基础表BKC333
     */
    @Field(value="BKC333")
    private String bkc333;

    /**
     * 来自基础表BKC334
     */
    @Field(value="BKC334")
    private String bkc334;

    /**
     * 1.正常、3=注销、4=暂停(状态变更时)
     */
    @Field(value="BKE155")
    private String bke155;

    @Field(userfield="pkEmp",userfieldscop= Field.FieldType.UPDATE)
    private String modifier;

    @Field(value="MODITY_TIME",date= Field.FieldType.UPDATE)
    private Date modityTime;

    /**
     * 创建人
     */
    @Field(userfield="pkEmp",userfieldscop= Field.FieldType.INSERT,date = Field.FieldType.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @Field(value="CREATE_TIME",date= Field.FieldType.INSERT,userfieldscop= Field.FieldType.INSERT)
    private Date createTime;

    /**
     * 删除标志
     */
    @Field(value="DEL_FLAG")
    private String delFlag;

    /**
     * 时间戳
     */
    @Field(date= Field.FieldType.ALL)
    private Date ts;

    public String getPkPhysirecord() {
        return pkPhysirecord;
    }

    public void setPkPhysirecord(String pkPhysirecord) {
        this.pkPhysirecord = pkPhysirecord;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public String getBkc322() {
        return bkc322;
    }

    public void setBkc322(String bkc322) {
        this.bkc322 = bkc322;
    }

    public String getAac005() {
        return aac005;
    }

    public void setAac005(String aac005) {
        this.aac005 = aac005;
    }

    public String getAac011() {
        return aac011;
    }

    public void setAac011(String aac011) {
        this.aac011 = aac011;
    }

    public String getBkc321() {
        return bkc321;
    }

    public void setBkc321(String bkc321) {
        this.bkc321 = bkc321;
    }

    public String getBka633() {
        return bka633;
    }

    public void setBka633(String bka633) {
        this.bka633 = bka633;
    }

    public String getAac183() {
        return aac183;
    }

    public void setAac183(String aac183) {
        this.aac183 = aac183;
    }

    public String getBkc323() {
        return bkc323;
    }

    public void setBkc323(String bkc323) {
        this.bkc323 = bkc323;
    }

    public String getBke955() {
        return bke955;
    }

    public void setBke955(String bke955) {
        this.bke955 = bke955;
    }

    public String getAkc055() {
        return akc055;
    }

    public void setAkc055(String akc055) {
        this.akc055 = akc055;
    }

    public String getBcc950() {
        return bcc950;
    }

    public void setBcc950(String bcc950) {
        this.bcc950 = bcc950;
    }

    public String getBcc955() {
        return bcc955;
    }

    public void setBcc955(String bcc955) {
        this.bcc955 = bcc955;
    }

    public String getBka503() {
        return bka503;
    }

    public void setBka503(String bka503) {
        this.bka503 = bka503;
    }

    public String getBkc324() {
        return bkc324;
    }

    public void setBkc324(String bkc324) {
        this.bkc324 = bkc324;
    }

    public String getBkc326() {
        return bkc326;
    }

    public void setBkc326(String bkc326) {
        this.bkc326 = bkc326;
    }

    public String getBkc328() {
        return bkc328;
    }

    public void setBkc328(String bkc328) {
        this.bkc328 = bkc328;
    }

    public String getBkc329() {
        return bkc329;
    }

    public void setBkc329(String bkc329) {
        this.bkc329 = bkc329;
    }

    public String getBke801() {
        return bke801;
    }

    public void setBke801(String bke801) {
        this.bke801 = bke801;
    }

    public String getBkc330() {
        return bkc330;
    }

    public void setBkc330(String bkc330) {
        this.bkc330 = bkc330;
    }

    public String getBkc335() {
        return bkc335;
    }

    public void setBkc335(String bkc335) {
        this.bkc335 = bkc335;
    }

    public String getAhc451() {
        return ahc451;
    }

    public void setAhc451(String ahc451) {
        this.ahc451 = ahc451;
    }

    public String getAaf009() {
        return aaf009;
    }

    public void setAaf009(String aaf009) {
        this.aaf009 = aaf009;
    }

    public String getCkc302() {
        return ckc302;
    }

    public void setCkc302(String ckc302) {
        this.ckc302 = ckc302;
    }

    public String getBkc325() {
        return bkc325;
    }

    public void setBkc325(String bkc325) {
        this.bkc325 = bkc325;
    }

    public String getCkc304() {
        return ckc304;
    }

    public void setCkc304(String ckc304) {
        this.ckc304 = ckc304;
    }

    public Date getAae030() {
        return aae030;
    }

    public void setAae030(Date aae030) {
        this.aae030 = aae030;
    }

    public Date getAae031() {
        return aae031;
    }

    public void setAae031(Date aae031) {
        this.aae031 = aae031;
    }

    public String getBkc332() {
        return bkc332;
    }

    public void setBkc332(String bkc332) {
        this.bkc332 = bkc332;
    }

    public String getBkc333() {
        return bkc333;
    }

    public void setBkc333(String bkc333) {
        this.bkc333 = bkc333;
    }

    public String getBkc334() {
        return bkc334;
    }

    public void setBkc334(String bkc334) {
        this.bkc334 = bkc334;
    }

    public String getBke155() {
        return bke155;
    }

    public void setBke155(String bke155) {
        this.bke155 = bke155;
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    private static final long serialVersionUID = 1L;
}