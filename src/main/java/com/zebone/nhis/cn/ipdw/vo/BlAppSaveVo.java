package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;
import java.util.Map;

public class BlAppSaveVo {
    public List<BloodApplyVO> bloods;
    /** 大量输血申请 **/
    private String ifMaxApp; //是否大量输血
    private List<Map<String,Object>> maxList; //大量输血项目

    public List<BloodApplyVO> getBloods() {
        return bloods;
    }

    public void setBloods(List<BloodApplyVO> bloods) {
        this.bloods = bloods;
    }

    public String getIfMaxApp() {
        return ifMaxApp;
    }

    public void setIfMaxApp(String ifMaxApp) {
        this.ifMaxApp = ifMaxApp;
    }

    public List<Map<String, Object>> getMaxList() {
        return maxList;
    }

    public void setMaxList(List<Map<String, Object>> maxList) {
        this.maxList = maxList;
    }
}
