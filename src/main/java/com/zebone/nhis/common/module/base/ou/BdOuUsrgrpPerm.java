package com.zebone.nhis.common.module.base.ou;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * @Auther: wuqiang
 * @Date: 2018/8/29 10:48
 * @Description:
 */
@Table(value="bd_ou_usrgrp_perm")
public class BdOuUsrgrpPerm {

    /**
     * 报表关联用户主键
     */
    @Field(value="pk_usrgrpperm",id= KeyId.UUID)
    private String pkUsrgrpperm;
    /**
     * 所属机构主键
     */
    @Field(value="pk_org",userfield="pkOrg",userfieldscop= FieldType.INSERT)
    public String pkOrg;
    /**
     * 控制类型 01代表报表
     */
    @Field(value="dt_datapermtype")
    public String dtDatapermtype;

    /**
     * 控制类型编码
     */
    @Field(value="code_dp",userfield="codeDp",userfieldscop= FieldType.INSERT)
    public String codeDp;

    /**
     * 用户组主键
     */
    @Field(value="pk_usrgrp",userfield="pk_usrgrp",userfieldscop= FieldType.INSERT)
    public String pkUsrgrp;

    /**
     * 修改人
     */
    @Field(value="modifier",userfieldscop= FieldType.INSERT)
    public String modifier;

    /**
     * 修改时间
     */
    @Field(value="modifier_time",date= FieldType.INSERT)
    public Date modifierTime;

    /**
     * 删除标记 0 未删除，1删除
     */
    @Field(value="del_flag",userfieldscop= FieldType.INSERT)
    public String delFlag;


    /**
     * 创建人
     */
    @Field(userfield="pkEmp",userfieldscop= FieldType.INSERT)
    public String creator;

    public void setPkUsrgrpperm(String pkUsrgrpperm) {
        this.pkUsrgrpperm = pkUsrgrpperm;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public void setDtDatapermtype(String dtDatapermtype) {
        this.dtDatapermtype = dtDatapermtype;
    }

    public void setCode_dp(String codeDp) {
        this.codeDp = codeDp;
    }

    public void setPkUsrgrp(String pkUsrgrp) {
        this.pkUsrgrp = pkUsrgrp;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setmodifierTime(Date modifierTime) {
        this.modifierTime = modifierTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    /**
     * 创建时间
     */
    @Field(value="create_time",date= FieldType.INSERT)
    public Date createTime;


    public String getPkUsrgrpperm() {
        return pkUsrgrpperm;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public String getDtDatapermtype() {
        return dtDatapermtype;
    }

    public String getCodeDp() {
        return codeDp;
    }

    public String getPkUsrgrp() {
        return pkUsrgrp;
    }

    public String getModifier() {
        return modifier;
    }

    public Date getmodifierTime() {
        return modifierTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getCreator() {
        return creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getTs() {
        return ts;
    }

    /**
     * 时间戳
     */
    @Field(date= FieldType.ALL)
    public Date ts;


}
