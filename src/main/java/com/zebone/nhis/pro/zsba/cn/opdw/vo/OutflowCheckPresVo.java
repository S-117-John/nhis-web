package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import com.zebone.nhis.ma.pub.zsba.vo.outflow.StoreVo;

import java.util.List;

public class OutflowCheckPresVo {
    public Integer isOutflow;
    public List<StoreVo> storeList;

    public Integer isOutflow() {
        return isOutflow;
    }

    public void setOutflow(Integer outflow) {
        isOutflow = outflow;
    }

    public List<StoreVo> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreVo> storeList) {
        this.storeList = storeList;
    }
}
