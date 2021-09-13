package com.zebone.nhis.common.module.base.bd.drg;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table("BD_TERM_CCHI")
public class BdTermCchi {
    //CCHI主键
    @PK
    @Field(value="PK_CCHI",id= Field.KeyId.UUID)
    private String pkCchi;
    //所属机构
    @Field(value="PK_ORG")
    private String pkOrg;
    //CCHI编码
    @Field(value="CODE_CCHI")
    private String codeCchi;
    //CCHI名称
    @Field(value="NAME_CCHI")
    private String nameCchi;
    //拼音码
    @Field(value="SPCODE")
    private String spcode;
    //自定义码
    @Field(value="D_CODE")
    private String dCode;
    //所属分类
    @Field(value="DT_CCHICATE")
    private String dtCchicate;
    //所属专业
    @Field(value="DT_CCHISPEC")
    private String dtCchispec;
    //版本
    @Field(value="VERSION")
    private String version;
    //风险系数
    @Field(value="RISK")
    private String risk;
    //技术难度
    @Field(value="DIFF")
    private String diff;
    //主要操作标识
    @Field(value="EU_MAJ")
    private String euMaj;
    //适用性别
    @Field(value="EU_SEX")
    private String euSex;
    //适用年龄上限
    @Field(value="AGE_MAX")
    private int ageMax;
    //适用年龄下限
    @Field(value="AGE_MIN")
    private int ageMin;
    //内涵
    @Field(value="CONTENT")
    private String content;
    //除外
    @Field(value="EXCEPT")
    private String except;
    //备注
    @Field(value="NOTE")
    private String note;
    //停用标志
    @Field(value="FLAG_STOP")
    private String flagStop;
    //创建人
    @Field(value="CREATOR")
    private String creator;
    //创建时间
    @Field(value="CREATE_TIME")
    private Date createTime;
    //修改人
    @Field(value="MODIFIER")
    private String modifier;

    public String getPkCchi() {
        return pkCchi;
    }

    public void setPkCchi(String pkCchi) {
        this.pkCchi = pkCchi;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getCodeCchi() {
        return codeCchi;
    }

    public void setCodeCchi(String codeCchi) {
        this.codeCchi = codeCchi;
    }

    public String getNameCchi() {
        return nameCchi;
    }

    public void setNameCchi(String nameCchi) {
        this.nameCchi = nameCchi;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getDtCchicate() {
        return dtCchicate;
    }

    public void setDtCchicate(String dtCchicate) {
        this.dtCchicate = dtCchicate;
    }

    public String getDtCchispec() {
        return dtCchispec;
    }

    public void setDtCchispec(String dtCchispec) {
        this.dtCchispec = dtCchispec;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getEuMaj() {
        return euMaj;
    }

    public void setEuMaj(String euMaj) {
        this.euMaj = euMaj;
    }

    public String getEuSex() {
        return euSex;
    }

    public void setEuSex(String euSex) {
        this.euSex = euSex;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcept() {
        return except;
    }

    public void setExcept(String except) {
        this.except = except;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFlagStop() {
        return flagStop;
    }

    public void setFlagStop(String flagStop) {
        this.flagStop = flagStop;
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

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getFlagDel() {
        return flagDel;
    }

    public void setFlagDel(String flagDel) {
        this.flagDel = flagDel;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    //删除标志
    @Field(value="FLAG_DEL")
    private String flagDel;
    //时间戳
    @Field(value="TS")
    private Date ts;

}
