package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;

import java.util.List;

/**
 * 模板类
 */
public class BdTempVo {
    private BdOrdSet tmpSet;
    private List<String> listRight;

    public BdOrdSet getTmpSet() {
        return tmpSet;
    }

    public void setTmpSet(BdOrdSet tmpSet) {
        this.tmpSet = tmpSet;
    }

    public List<String> getListRight() {
        return listRight;
    }

    public void setListRight(List<String> listRight) {
        this.listRight = listRight;
    }
}
