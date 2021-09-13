package com.zebone.nhis.task.bl.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(value = "oth_sta_month_unst_rec")
public class OthStaMonthUnstRec implements Serializable {
    //主键
    @PK
    @Field(value = "pk_oth_sta", id = Field.KeyId.UUID)
    private String pkOthSta;
    //所属机构
    @Field(value = "pk_org", userfield = "pkOrg", userfieldscop = Field.FieldType.INSERT)
    private String pkOrg;
    //统计月
    @Field(value = "month")
    private String month;
    //序号
    @Field(value = "seq_no")
    private Integer seqNo;
    //科室主键
    @Field(value = "pk_dept")
    private String pkDept;
    //科室名称
    @Field(value = "name_dept")
    private String nameDept;
    //患者主键
    @Field(value = "pk_pi")
    private String pkPi;
    //患者姓名
    @Field(value = "name_pi")
    private String namePi;
    //住院号
    @Field(value = "code_ip")
    private String codeIp;
    //医保计划
    @Field(value = "pk_hp")
    private String pkHp;
    //结算类型
    @Field(value = "name_hp")
    private String nameHp;
    //诊查费
    @Field(value = "amount1")
    private BigDecimal amount1;
    //检查费
    @Field(value = "amount2")
    private BigDecimal amount2;
    //床位费
    @Field(value = "amount3")
    private BigDecimal amount3;
    //治疗费
    @Field(value = "amount4")
    private BigDecimal amount4;
    //护理费
    @Field(value = "amount5")
    private BigDecimal amount5;
    //手术费
    @Field(value = "amount6")
    private BigDecimal amount6;
    //化验费
    @Field(value = "amount7")
    private BigDecimal amount7;
    //其他费
    @Field(value = "amount8")
    private BigDecimal amount8;
    //西药费
    @Field(value = "amount9")
    private BigDecimal amount9;
    //中草药费
    @Field(value = "amount10")
    private BigDecimal amount10;
    //中成药费
    @Field(value = "amount11")
    private BigDecimal amount11;
    //卫生材料费
    @Field(value = "amount12")
    private BigDecimal amount12;
    //其他费2
    @Field(value = "amount13")
    private BigDecimal amount13;
    //其他费3
    @Field(value = "amount14")
    private BigDecimal amount14;
    //其他费4
    @Field(value = "amount15")
    private BigDecimal amount15;
    //其他费5
    @Field(value = "amount16")
    private BigDecimal amount16;
    //总费用
    @Field(value = "amount_sum")
    private BigDecimal amountSum;
    //预交金
    @Field(value = "amount_prep")
    private BigDecimal amountPrep;
    //余额
    @Field(value = "amount_remain")
    private BigDecimal amountRemain;
    //备注
    @Field(value = "note")
    private String note;
    //创建人
    @Field(value = "creator")
    private String creator;
    //创建时间
    @Field(value = "create_time")
    private Date createTime;
    //修改人
    @Field(value = "modifier")
    private String modifier;
    //修改时间
    @Field(value = "modity_time")
    private Date modityTime;
    //删除标志
    @Field(value = "del_flag")
    public String delFlag = "0";
    //时间戳
    @Field(value = "ts")
    private Date ts;

    public String getPkOthSta() {
        return pkOthSta;
    }

    public void setPkOthSta(String pkOthSta) {
        this.pkOthSta = pkOthSta;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getPkHp() {
        return pkHp;
    }

    public void setPkHp(String pkHp) {
        this.pkHp = pkHp;
    }

    public String getNameHp() {
        return nameHp;
    }

    public void setNameHp(String nameHp) {
        this.nameHp = nameHp;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount3() {
        return amount3;
    }

    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

    public BigDecimal getAmount4() {
        return amount4;
    }

    public void setAmount4(BigDecimal amount4) {
        this.amount4 = amount4;
    }

    public BigDecimal getAmount5() {
        return amount5;
    }

    public void setAmount5(BigDecimal amount5) {
        this.amount5 = amount5;
    }

    public BigDecimal getAmount6() {
        return amount6;
    }

    public void setAmount6(BigDecimal amount6) {
        this.amount6 = amount6;
    }

    public BigDecimal getAmount7() {
        return amount7;
    }

    public void setAmount7(BigDecimal amount7) {
        this.amount7 = amount7;
    }

    public BigDecimal getAmount8() {
        return amount8;
    }

    public void setAmount8(BigDecimal amount8) {
        this.amount8 = amount8;
    }

    public BigDecimal getAmount9() {
        return amount9;
    }

    public void setAmount9(BigDecimal amount9) {
        this.amount9 = amount9;
    }

    public BigDecimal getAmount10() {
        return amount10;
    }

    public void setAmount10(BigDecimal amount10) {
        this.amount10 = amount10;
    }

    public BigDecimal getAmount11() {
        return amount11;
    }

    public void setAmount11(BigDecimal amount11) {
        this.amount11 = amount11;
    }

    public BigDecimal getAmount12() {
        return amount12;
    }

    public void setAmount12(BigDecimal amount12) {
        this.amount12 = amount12;
    }

    public BigDecimal getAmount13() {
        return amount13;
    }

    public void setAmount13(BigDecimal amount13) {
        this.amount13 = amount13;
    }

    public BigDecimal getAmount14() {
        return amount14;
    }

    public void setAmount14(BigDecimal amount14) {
        this.amount14 = amount14;
    }

    public BigDecimal getAmount15() {
        return amount15;
    }

    public void setAmount15(BigDecimal amount15) {
        this.amount15 = amount15;
    }

    public BigDecimal getAmount16() {
        return amount16;
    }

    public void setAmount16(BigDecimal amount16) {
        this.amount16 = amount16;
    }

    public BigDecimal getAmountSum() {
        return amountSum;
    }

    public void setAmountSum(BigDecimal amountSum) {
        this.amountSum = amountSum;
    }

    public BigDecimal getAmountPrep() {
        return amountPrep;
    }

    public void setAmountPrep(BigDecimal amountPrep) {
        this.amountPrep = amountPrep;
    }

    public BigDecimal getAmountRemain() {
        return amountRemain;
    }

    public void setAmountRemain(BigDecimal amountRemain) {
        this.amountRemain = amountRemain;
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
