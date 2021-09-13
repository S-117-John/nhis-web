package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 手术操作信息
 */
public class InsQgybOprnInfo {
    /**
     * 手术操作类别
     */
    @JSONField(name = "oprn_oprt_type")
    private String oprnOprtType;
    /**
     * 手术操作名称
     */
    @JSONField(name = "oprn_oprt_name")
    private String oprnOprtName;
    /**
     * 手术操作代码
     */
    @JSONField(name = "oprn_oprt_code")
    private String oprnOprtCode;
    /**
     * 手术操作日期
     */
    @JSONField(name = "oprn_oprt_date",format = "yyyy-MM-dd HH:mm:ss")
    private Date oprnOprtDate;
    /**
     * 麻醉方式
     */
    @JSONField(name = "anst_way")
    private String anstWay;
    /**
     * 术者医师姓名
     */
    @JSONField(name = "oper_dr_name")
    private String operDrName;
    /**
     * 术者医师代码
     */
    @JSONField(name = "oper_dr_code")
    private String operDrCode;
    /**
     * 麻醉医师姓名
     */
    @JSONField(name = "anst_dr_name")
    private String anstDrName;
    /**
     * 麻醉医师代码
     */
    @JSONField(name = "anst_dr_code")
    private String anstDrCode;

    public String getOprnOprtType() {
        return oprnOprtType;
    }

    public void setOprnOprtType(String oprnOprtType) {
        this.oprnOprtType = oprnOprtType;
    }

    public String getOprnOprtName() {
        return oprnOprtName;
    }

    public void setOprnOprtName(String oprnOprtName) {
        this.oprnOprtName = oprnOprtName;
    }

    public String getOprnOprtCode() {
        return oprnOprtCode;
    }

    public void setOprnOprtCode(String oprnOprtCode) {
        this.oprnOprtCode = oprnOprtCode;
    }

    public Date getOprnOprtDate() {
        return oprnOprtDate;
    }

    public void setOprnOprtDate(Date oprnOprtDate) {
        this.oprnOprtDate = oprnOprtDate;
    }

    public String getAnstWay() {
        return anstWay;
    }

    public void setAnstWay(String anstWay) {
        this.anstWay = anstWay;
    }

    public String getOperDrName() {
        return operDrName;
    }

    public void setOperDrName(String operDrName) {
        this.operDrName = operDrName;
    }

    public String getOperDrCode() {
        return operDrCode;
    }

    public void setOperDrCode(String operDrCode) {
        this.operDrCode = operDrCode;
    }

    public String getAnstDrName() {
        return anstDrName;
    }

    public void setAnstDrName(String anstDrName) {
        this.anstDrName = anstDrName;
    }

    public String getAnstDrCode() {
        return anstDrCode;
    }

    public void setAnstDrCode(String anstDrCode) {
        this.anstDrCode = anstDrCode;
    }
}
