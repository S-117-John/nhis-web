package com.zebone.nhis.compay.ins.changzhou.vo;

/**
 * 医保诊断匹配信息
 */
public class ComparedDiagInfo {
    /**
     * 医院诊断编码
     */
    private String codeDiag;
    /**
     * 医保诊断便阿门
     */
    private String ybCodeDiag;

    public String getCodeDiag() {
        return codeDiag;
    }

    public void setCodeDiag(String codeDiag) {
        this.codeDiag = codeDiag;
    }

    public String getYbCodeDiag() {
        return ybCodeDiag;
    }

    public void setYbCodeDiag(String ybCodeDiag) {
        this.ybCodeDiag = ybCodeDiag;
    }
}
