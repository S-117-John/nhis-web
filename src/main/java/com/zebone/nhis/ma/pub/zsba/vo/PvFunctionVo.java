package com.zebone.nhis.ma.pub.zsba.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;



@Table(value = "PV_FUNCTION")
public class PvFunctionVo {

    /** PK_MEDBAG - 主键 */
    @PK
    @Field(value="PK_FUN",id= Field.KeyId.UUID)
    private String pkFun;

    /**患者就诊主键*/
    @Field(value="PK_PV")
    private String pkPv;

    /**患者就诊科室主键*/
    @Field(value="PK_DEPT")
    private String pkDept;

    /**操作日期*/
    @Field(value="DATE_FUN")
    private Date dateFun;

    /**创建人*/
    @Field(value="CREATOR")
    private String creator;

    /**创建日期*/
    @Field(value="CREATE_TIME")
    private Date createTime;

    /**修改人*/
    @Field(value="MODIFIER")
    private String modifier;

    /**修改时间*/
    @Field(value="MODITY_TIME")
    private Date modityTime;

    /**删除标识*/
    @Field(value="DEL_FLAG")
    private String delFlag;

    /**修改时间*/
    @Field(value="TS")
    private Date ts;


    private String codeIp;
    private String namePi;
    private String nameDept;
    private String euStatus;



    public String getPkFun() {
        return pkFun;
    }

    public void setPkFun(String pkFun) {
        this.pkFun = pkFun;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public Date getDateFun() {
        return dateFun;
    }

    public void setDateFun(Date dateFun) {
        this.dateFun = dateFun;
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

    public String getCodeIp() {return codeIp;}

    public void setCodeIp(String codeIp) {this.codeIp = codeIp;}

    public String getNamePi() {return namePi;}

    public void setNamePi(String namePi) {this.namePi = namePi;}

    public String getNameDept() {return nameDept;}

    public void setNameDept(String nameDept) {this.nameDept = nameDept;}

    public String getEuStatus() {return euStatus;}

    public void setEuStatus(String euStatus) {this.euStatus = euStatus;}
}
