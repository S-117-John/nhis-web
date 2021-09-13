package com.zebone.nhis.compay.ins.changzhou.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * 签到签退信息
 */
@Table(value = "INS_CZYB_LOGINRECORD")
public class LogInOutInfo extends BaseModule {
    /**
     * 主键
     */
    @PK
    @Field(value = "ID",id = Field.KeyId.UUID)
    private String id;
    /**
     * 签到人
     */
    @Field(value = "PK_EMP")
    private String pkEmp;
    /**
     * 医保类型
     */
    @Field(value = "YBLX")
    private String yblx;
    /**
     * 签到时间
     */
    @Field(value = "QDSJ")
    private Date qdsj;
    /**
     * 签到业务周期号
     */
    @Field(value = "QDYWZQH")
    private String qdywzqh;
    /**
     * 签退时间
     */
    @Field(value = "QTSJ")
    private Date qtsj;
    /**
     * 签退业务周期号
     */
    @Field(value = "QTYWZQH")
    private String qtywzqh;
    /**
     * 签到状态
     */
    @Field(value = "QDZT")
    private String qdzt;
    /**
     * 最后操作时间
     */
    @Field(value = "MODIFY_TIME")
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public Date getQdsj() {
        return qdsj;
    }

    public void setQdsj(Date qdsj) {
        this.qdsj = qdsj;
    }

    public String getQdywzqh() {
        return qdywzqh;
    }

    public void setQdywzqh(String qdywzqh) {
        this.qdywzqh = qdywzqh;
    }

    public Date getQtsj() {
        return qtsj;
    }

    public void setQtsj(Date qtsj) {
        this.qtsj = qtsj;
    }

    public String getQtywzqh() {
        return qtywzqh;
    }

    public void setQtywzqh(String qtywzqh) {
        this.qtywzqh = qtywzqh;
    }

    public String getQdzt() {
        return qdzt;
    }

    public void setQdzt(String qdzt) {
        this.qdzt = qdzt;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
