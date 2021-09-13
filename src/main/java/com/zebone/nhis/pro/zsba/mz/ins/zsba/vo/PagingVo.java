package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.util.List;

public class PagingVo {
    private int totalCount;
    private List<MedicalCharges> itemList;
    private List<InsurCata> insur;
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<MedicalCharges> getItemList() {
        return itemList;
    }

    public void setItemList(List<MedicalCharges> itemList) {
        this.itemList = itemList;
    }

    public List<InsurCata> getInsur() {
        return insur;
    }

    public void setInsur(List<InsurCata> insur) {
        this.insur = insur;
    }
}
