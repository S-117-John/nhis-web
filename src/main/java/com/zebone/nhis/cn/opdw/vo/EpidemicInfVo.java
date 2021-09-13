package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;

/**
 * @Classname EpidemicInfV
 * @Description 患者流行病史相关信息
 * @Date 2020-02-02 23:07
 * @Created by wuqiang
 */
public class EpidemicInfVo {
    /**
     * 就诊主键
     */
    public String pkPv;
    /**
     * 症状和体征
     */
    public String descSymp;
    /**
     * 流行病信息
     */
    public String descEpid;
    /**
     * 处理方式
     */
    public String descTreat;
    /**
     * 分流科室
     */

    public String pkDeptDist;
    /**
     * 0复诊，1初诊
     */
    public int flagFirst;

    /**
     * 发病日期
     */
    public Date dateOnset;

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getDescSymp() {
        return descSymp;
    }

    public void setDescSymp(String descSymp) {
        this.descSymp = descSymp;
    }

    public String getDescEpid() {
        return descEpid;
    }

    public void setDescEpid(String descEpid) {
        this.descEpid = descEpid;
    }

    public String getDescTreat() {
        return descTreat;
    }

    public void setDescTreat(String descTreat) {
        this.descTreat = descTreat;
    }

    public String getPkDeptDist() {
        return pkDeptDist;
    }

    public void setPkDeptDist(String pkDeptDist) {
        this.pkDeptDist = pkDeptDist;
    }

    public int getFlagFirst() {
        return flagFirst;
    }

    public void setFlagFirst(int flagFirst) {
        this.flagFirst = flagFirst;
    }

    public Date getDateOnset() {
        return dateOnset;
    }

    public void setDateOnset(Date dateOnset) {
        this.dateOnset = dateOnset;
    }


}
