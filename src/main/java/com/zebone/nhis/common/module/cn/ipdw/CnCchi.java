package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table("CN_CCHI")
public class CnCchi {
    /**
     * 主键
     */
    @PK
    @Field(value = "PK_CNCCHI",id= Field.KeyId.UUID)
    private String pkCncchi;

    /**
     * 所属机构
     */
    @Field(value = "PK_ORG")
    private String pkOrg;

    /**
     * 患者就诊
     */
    @Field(value = "PK_PV")
    private String pkPv;

    /**
     * 序号
     */
    @Field(value = "SORTNO")
    private Integer sortno;

    /**
     * CCHI项目
     */
    @Field(value = "PK_CCHI")
    private String pkCchi;

    /**
     * 项目编码
     */
    @Field(value = "CODE_CCHI")
    private String codeCchi;

    /**
     * 项目名称
     */
    @Field(value = "NAME_CCHI")
    private String nameCchi;

    /**
     * 部位
     */
    @Field(value = "DESC_BODYPART")
    private String descBodypart;

    /**
     * 属性
     */
    @Field(value = "DESC_DRGPROP")
    private String descDrgprop;

    /**
     * 备注
     */
    @Field(value = "NOTE")
    private String note;

    /**
     * 修饰符
     */
    @Field(value = "DESC_CCHI")
    private String descCchi;

    /**
     * 主要操作
     */
    @Field(value = "FLAG_MAJ")
    private String flagMaj;

    /**
     * 病案DRG页使用
     */
    @Field(value = "FLAG_DRG")
    private String flagDrg;

    /**
     * 数据来源
     */
    @Field(value = "EU_SRC")
    private String euSrc;

    /**
     * 关联的收费项目
     */
    @Field(value = "PK_ITEM")
    private String pkItem;

    /**
     * 关联的医嘱
     */
    @Field(value = "PK_ORD")
    private String pkOrd;

    /**
     * 关联的手术
     */
    @Field(value = "PK_DIAG")
    private String pkDiag;

    /**
     * 操作医生主键
     */
    @Field(value = "PK_EMP")
    private String pkEmp;

    /**
     * 操作医生姓名
     */
    @Field(value = "NAME_EMP")
    private String nameEmp;

    /**
     * 操作医生所属科室
     */
    @Field(value = "PK_DEPT")
    private String pkDept;

    /**
     * 录入人
     */
    @Field(value = "PK_EMP_ENTRY")
    private String pkEmpEntry;

    /**
     * 录入人姓名
     */
    @Field(value = "NAME_EMP_ENTRY")
    private String nameEmpEntry;

    /**
     * 录入时间
     */
    @Field(value = "DATE_ENTRY")
    private Date dateEntry;

    /**
     * 创建人
     */
    @Field(value = "CREATOR")
    private String creator;

    /**
     * 创建时间
     */
    @Field(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @Field(value = "MODIFIER")
    private String modifier;

    /**
     * 删除标志
     */
    @Field(value = "FLAG_DEL")
    private String flagDel;

    /**
     * 时间戳
     */
    @Field(value = "TS")
    private Date ts;

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkCncchi() {
        return pkCncchi;
    }

    public void setPkCncchi(String pkCncchi) {
        this.pkCncchi = pkCncchi;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public String getPkCchi() {
        return pkCchi;
    }

    public void setPkCchi(String pkCchi) {
        this.pkCchi = pkCchi;
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

    public String getDescBodypart() {
        return descBodypart;
    }

    public void setDescBodypart(String descBodypart) {
        this.descBodypart = descBodypart;
    }

    public String getDescDrgprop() {
        return descDrgprop;
    }

    public void setDescDrgprop(String descDrgprop) {
        this.descDrgprop = descDrgprop;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescCchi() {
        return descCchi;
    }

    public void setDescCchi(String descCchi) {
        this.descCchi = descCchi;
    }

    public String getFlagMaj() {
        return flagMaj;
    }

    public void setFlagMaj(String flagMaj) {
        this.flagMaj = flagMaj;
    }

    public String getFlagDrg() {
        return flagDrg;
    }

    public void setFlagDrg(String flagDrg) {
        this.flagDrg = flagDrg;
    }

    public String getEuSrc() {
        return euSrc;
    }

    public void setEuSrc(String euSrc) {
        this.euSrc = euSrc;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public String getPkDiag() {
        return pkDiag;
    }

    public void setPkDiag(String pkDiag) {
        this.pkDiag = pkDiag;
    }

    public String getPkEmpEntry() {
        return pkEmpEntry;
    }

    public void setPkEmpEntry(String pkEmpEntry) {
        this.pkEmpEntry = pkEmpEntry;
    }

    public String getNameEmpEntry() {
        return nameEmpEntry;
    }

    public void setNameEmpEntry(String nameEmpEntry) {
        this.nameEmpEntry = nameEmpEntry;
    }

    public Date getDateEntry() {
        return dateEntry;
    }

    public void setDateEntry(Date dateEntry) {
        this.dateEntry = dateEntry;
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
}

