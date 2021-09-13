package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import java.util.List;

public class PiPhyVo {
    public List<PiPhysiological> addList;

    public List<PiPhysiological> editList;

    public List<PiPhysiological> getAddList() {
        return addList;
    }

    public void setAddList(List<PiPhysiological> addList) {
        this.addList = addList;
    }

    public List<PiPhysiological> getEditList() {
        return editList;
    }

    public void setEditList(List<PiPhysiological> editList) {
        this.editList = editList;
    }
}
