package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class InsQgybIcuInfo {
    /**
     *重症监护病房类型
     */
    @JSONField(name = "scs_cutd_ward_type")
    private String scsCutdWardType;
    /**
     *重症监护进入时间
     */
    @JSONField(name = "scs_cutd_inpool_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date scsCutdInpoolTime;
    /**
     *重症监护退出时间
     */
    @JSONField(name = "scs_cutd_exit_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date scsCutdExitTime;
    /**
     *重症监护合计时长
     */
    @JSONField(name = "scs_cutd_sum_dura")
    private String scsCutdSumDura;

    public String getScsCutdWardType() {
        return scsCutdWardType;
    }

    public void setScsCutdWardType(String scsCutdWardType) {
        this.scsCutdWardType = scsCutdWardType;
    }

    public Date getScsCutdInpoolTime() {
        return scsCutdInpoolTime;
    }

    public void setScsCutdInpoolTime(Date scsCutdInpoolTime) {
        this.scsCutdInpoolTime = scsCutdInpoolTime;
    }

    public Date getScsCutdExitTime() {
        return scsCutdExitTime;
    }

    public void setScsCutdExitTime(Date scsCutdExitTime) {
        this.scsCutdExitTime = scsCutdExitTime;
    }

    public String getScsCutdSumDura() {
        return scsCutdSumDura;
    }

    public void setScsCutdSumDura(String scsCutdSumDura) {
        this.scsCutdSumDura = scsCutdSumDura;
    }
}
