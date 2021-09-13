package com.zebone.nhis.pro.zsba.cn.ipdw.vo;

import com.zebone.nhis.cn.ipdw.vo.CnLabApplyVo;
import com.zebone.nhis.cn.ipdw.vo.CnRisApplyVo;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;

import java.util.ArrayList;
import java.util.List;

public class BaCanlParam {
    private List<String> pkCnOrds = new ArrayList<String>();
    private List<String> ordsns=new ArrayList<String>();
    private List<CnRisApplyVo> saveRisList = new ArrayList<CnRisApplyVo>();
    private List<CnLabApplyVo> saveLisList = new ArrayList<CnLabApplyVo>();
    private String pkPv;

    List<CnSignCa> cnSignCaList = new ArrayList<CnSignCa>();
    public List<String> getPkCnOrds() {
        return pkCnOrds;
    }
    public void setPkCnOrds(List<String> pkCnOrds) {
        this.pkCnOrds = pkCnOrds;
    }
    public List<CnRisApplyVo> getSaveRisList() {
        return saveRisList;
    }
    public void setSaveRisList(List<CnRisApplyVo> saveRisList) {
        this.saveRisList = saveRisList;
    }
    public List<CnLabApplyVo> getSaveLisList() {
        return saveLisList;
    }
    public void setSaveLisList(List<CnLabApplyVo> saveLisList) {
        this.saveLisList = saveLisList;
    }
    public List<CnSignCa> getCnSignCaList() {
        return cnSignCaList;
    }
    public void setCnSignCaList(List<CnSignCa> cnSignCaList) {
        this.cnSignCaList = cnSignCaList;
    }

    public List<String> getOrdsns() {
        return ordsns;
    }

    public void setOrdsns(List<String> ordsns) {
        this.ordsns = ordsns;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }
}
