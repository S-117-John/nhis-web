package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class ZyPubParam {
    @JSONField(name = "function_id")
    private String functionId;
    private String akb020;
    private String aaz218;

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getAkb020() {
        return akb020;
    }

    public void setAkb020(String akb020) {
        this.akb020 = akb020;
    }

    public String getAaz218() {
        return aaz218;
    }

    public void setAaz218(String aaz218) {
        this.aaz218 = aaz218;
    }
}
