package com.zebone.nhis.bl.pub.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class CnOrderVo extends CnOrder {

    /**
     * 用法试敏
     */
    private String supSt;
    /**
     * 附加用法试敏
     */
    private String supaSt;
    /**
     * 用法分类编码
     */
    private String clsCode;

    public String getSupSt() {
        return supSt;
    }

    public void setSupSt(String supSt) {
        this.supSt = supSt;
    }

    public String getSupaSt() {
        return supaSt;
    }

    public void setSupaSt(String supaSt) {
        this.supaSt = supaSt;
    }

    public String getClsCode() {
        return clsCode;
    }

    public void setClsCode(String clsCode) {
        this.clsCode = clsCode;
    }

}