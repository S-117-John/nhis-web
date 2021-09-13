package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.util.List;

public class HisItemInfo {
    private String port;
    private List<HisItem> params;
    private List<String> pkItemmapList;
    private int PageIndex;
    private int PageSize;
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<HisItem> getParams() {
        return params;
    }

    public void setParams(List<HisItem> params) {
        this.params = params;
    }

    public List<String> getPkItemmapList() {
        return pkItemmapList;
    }

    public void setPkItemmapList(List<String> pkItemmapList) {
        this.pkItemmapList = pkItemmapList;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }
}
