package com.zebone.nhis.ma.pub.zsba.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * @Classname ExMedBag
 * @Description 药袋记录表-博爱项目使用
 * @Date 2020-04-08 14:15
 * @Created by wuqiang
 */
@Table(value="EX_MED_BAG")
public class ExMedBag extends BaseModule {

    /** PK_MEDBAG - 主键 */
    @PK
    @Field(value="PK_MEDBAG",id= Field.KeyId.UUID)
    private String pkMedbag;

    /** EU_BAG - 药袋类型：01摆药机药袋；02人工药袋 */
    @Field(value="EU_BAG")
    private String euBag;

    /** CODE_DE - 发药单号 */
    @Field(value="CODE_DE")
    private String codeDe;
    /** CODE_DE - 药袋编码 */
    @Field(value="CODE_BAG")
    private String codeBag;

    /** NOTE - 备注 */
    @Field(value="NOTE")
    private String note;
    @Field(value="MODITY_TIME")
    private Date modityTime;

    /** PK_CGIP - 记费主键 */
    @Field(value="PK_CGIP")
    private String pkCgip;

    /** PK_PV - 患者就诊主键 */
    @Field(value="PK_PV")
    private String pkPv;



    /** 用作计算药袋费用使用 */
    private int   statue;
    /**  患者主键 */
    private String pkPi;
    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }



    public ExMedBag() {
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }
    public String getPkMedbag() {
        return pkMedbag;
    }

    public void setPkMedbag(String pkMedbag) {
        this.pkMedbag = pkMedbag;
    }

    public String getEuBag() {
        return euBag;
    }

    public void setEuBag(String euBag) {
        this.euBag = euBag;
    }

    public String getCodeDe() {
        return codeDe;
    }

    public void setCodeDe(String codeDe) {
        this.codeDe = codeDe;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }

    public String getPkCgip() {
        return pkCgip;
    }

    public void setPkCgip(String pkCgip) {
        this.pkCgip = pkCgip;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getCodeBag() {
        return codeBag;
    }

    public void setCodeBag(String codeBag) {
        this.codeBag = codeBag;
    }



}
