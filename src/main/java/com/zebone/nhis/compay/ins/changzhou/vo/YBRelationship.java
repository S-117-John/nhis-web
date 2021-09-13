package com.zebone.nhis.compay.ins.changzhou.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用于保存医保与HIS之间的关系
 */
@Table(value = "ins_czyb_relationship")
public class YBRelationship extends BaseModule {
    /**
     * 主键
     */
    @PK
    @Field(value = "ID",id = Field.KeyId.UUID)
    private String id;
    /**
     * HIS就诊主键
     */
    @Field(value = "PK_PV")
    private String pkPv;
    /**
     * HIS结算主键
     */
    @Field(value = "PK_SETTLE")
    private String pkSettle;
    /**
     * 医保结算状态
     */
    @Field(value = "EU_STATUS")
    private String euStatus;
    /**
     * 业务类型：0 挂号，1 收费，2 住院
     */
    @Field(value = "YWLX")
    private String yWLX;
    /**
     * 医保结算主键
     */
    @Field(value = "PK_PTMZJS")
    private String pkPtmzjs;
    /**
     * 医保就诊ID
     */
    @Field(value = "YBREGID")
    private String yBRegId;
    /**
     * 中途结算标志
     */
    @Field(value = "FLAG_MIDWAY")
    private String flagMidway;
    /**
     * 结算表主键
     */
    @Field(value = "JSID")
    private String jsId;
    /**
     * 修改时间
     */
    @Field(value = "MODIFY_TIME")
    private Date modifyTime;

    /**
     * HIS部分退费前，费用明细主键
     */
    @Field(value = "PK_CGOLD")
    private String pkCgOld;
    /**
     * HIS部分退费后，新的费用明细主键
     */
    @Field(value = "PK_CGNEW")
    private String pkCgNew;

    private List<Map<String,String>> relationPkCg;

    public List<Map<String, String>> getRelationPkCg() {
        return relationPkCg;
    }

    public void setRelationPkCg(List<Map<String, String>> relationPkCg) {
        this.relationPkCg = relationPkCg;
    }

    public String getPkCgOld() {
        return pkCgOld;
    }

    public void setPkCgOld(String pkCgOld) {
        this.pkCgOld = pkCgOld;
    }

    public String getPkCgNew() {
        return pkCgNew;
    }

    public void setPkCgNew(String pkCgNew) {
        this.pkCgNew = pkCgNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getyWLX() {
        return yWLX;
    }

    public void setyWLX(String yWLX) {
        this.yWLX = yWLX;
    }

    public String getPkPtmzjs() {
        return pkPtmzjs;
    }

    public void setPkPtmzjs(String pkPtmzjs) {
        this.pkPtmzjs = pkPtmzjs;
    }

    public String getyBRegId() {
        return yBRegId;
    }

    public void setyBRegId(String yBRegId) {
        this.yBRegId = yBRegId;
    }

    public String getFlagMidway() {
        return flagMidway;
    }

    public void setFlagMidway(String flagMidway) {
        this.flagMidway = flagMidway;
    }

    public String getJsId() {
        return jsId;
    }

    public void setJsId(String jsId) {
        this.jsId = jsId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
