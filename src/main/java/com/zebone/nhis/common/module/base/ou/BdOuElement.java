package com.zebone.nhis.common.module.base.ou;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * BD_OU_ELEMENT表结构
 * @author Administrator
 *
 */
@Table(value="BD_OU_ELEMENT")
public class BdOuElement {
    /**
     * 元素主键
     */
    @PK
    @Field(value="PK_ELEMENT",id= Field.KeyId.UUID)
    private String  pkElement ;
    /**
     * 菜单主键
     */
    @Field(value="PK_MENU")
    private String  pkMenu;
    /**
     * 所属机构
     */
    @Field(value="PK_ORG")
    private String  pkOrg;
    /**
     * 菜单编码
     */
    @Field(value="CODE_EL")
    private String  codeEl;
    /**
     * 菜单名称
     */
    @Field(value="NAME_EL")
    private String  nameEl;
    /**
     * 拼音码
     */
    @Field(value="SPCODE")
    private String  spcode;
    /**
     * 自定义码
     */
    @Field(value="D_CODE")
    private String  dCode;
    /**
     * 是否可用
     */
    @Field(value="FLAG_ENABLE")
    private String  flagEnable;
    /**
     * 备注
     */
    @Field(value="NOTE")
    private String  note;
    /**
     * 创建人
     */
    @Field(value="CREATOR")
    private String  creator;
    /**
     * 菜创建时间
     */
    @Field(value="CREATE_TIME")
    private Date createTime;
    /**
     * 修改人
     */
    @Field(value="MODIFIER")
    private String  modifier;
    /**
     * 修改时间
     */
    @Field(value="MODITY_TIME")
    private Date  modityTime;
    /**
     * 删除标志
     */
    @Field(value="DEL_FLAG")
    private String  delFlag;
    /**
     * 时间戳
     */
    @Field(value="TS")
    private Date  ts;

    public String getPkElement() {
        return pkElement;
    }

    public void setPkElement(String pkElement) {
        this.pkElement = pkElement;
    }

    public String getPkMenu() {
        return pkMenu;
    }

    public void setPkMenu(String pkMenu) {
        this.pkMenu = pkMenu;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getCodeEl() {
        return codeEl;
    }

    public void setCodeEl(String codeEl) {
        this.codeEl = codeEl;
    }

    public String getNameEl() {
        return nameEl;
    }

    public void setNameEl(String nameEl) {
        this.nameEl = nameEl;
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

    public String getFlagEnable() {
        return flagEnable;
    }

    public void setFlagEnable(String flagEnable) {
        this.flagEnable = flagEnable;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
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
}
