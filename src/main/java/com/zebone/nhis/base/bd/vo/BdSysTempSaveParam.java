package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.code.BdSysparamTemp;

import java.util.ArrayList;
import java.util.List;

public class BdSysTempSaveParam {
    private BdSysparamTemp editParam  = new BdSysparamTemp();
    private List<BdSysparamExt> sysparamList =new ArrayList<BdSysparamExt>();
    private List<BdSysparamVO> delsysparamList = new ArrayList<BdSysparamVO>();

    public BdSysparamTemp getEditParam() {
        return editParam;
    }

    public void setEditParam(BdSysparamTemp editParam) {
        this.editParam = editParam;
    }

    public List<BdSysparamExt> getSysparamList() {
        return sysparamList;
    }

    public void setSysparamList(List<BdSysparamExt> sysparamList) {
        this.sysparamList = sysparamList;
    }

    public List<BdSysparamVO> getDelsysparamList() { return delsysparamList; }

    public void setDelsysparamList(List<BdSysparamVO> delsysparamList) { this.delsysparamList = delsysparamList; }
}
