package com.zebone.nhis.common.module.compay.ins.qgyb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value="INS_SIGN_IN_QG")
public class InsSignInQg  extends BaseModule {
    @PK
    @Field(value="PK_INSSIGNINQG",id= Field.KeyId.UUID)
    private String pkInssigninqg;

    /**
     * 操作员编号
     */
    @Field(value="OPTER_NO")
    private String opterNo;

    /**
     * 签到MAC地址
     */
    @Field(value="MAC")
    private String mac;
    /**
     * 签到IP地址
     */
    @Field(value="IP")
    private String ip;
    /**
     * 签到时间
     */
    @Field(value="SIGN_TIME")
    private Date signTime;
    /**
     * 签到编号
     */
    @Field(value="SIGN_NO")
    private String signNo;
    /**
     * 状态 1：签到 2：签退 3:签退失败
     */
    @Field(value="STATUS")
    private String status;

    /**
     * 签退时间
     */
    @Field(value="SIGN_OUT_TIME")
    private Date signOutTime;

    private String code;

    @Field(value="MSG")
    private String msg;

    public String getPkInssigninqg() {
        return pkInssigninqg;
    }

    public void setPkInssigninqg(String pkInssigninqg) {
        this.pkInssigninqg = pkInssigninqg;
    }

    public String getOpterNo() {
        return opterNo;
    }

    public void setOpterNo(String opterNo) {
        this.opterNo = opterNo;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignNo() {
        return signNo;
    }

    public void setSignNo(String signNo) {
        this.signNo = signNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(Date signOutTime) {
        this.signOutTime = signOutTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
