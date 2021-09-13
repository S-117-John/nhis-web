package com.zebone.nhis.pro.zsrm.bl.vo;

public class ExOrderVo {
    /**
     * 执行医嘱主键
     */
    private String pkCnord;
    /**
     *执行人
     */
    private String pkEmpOcc;
    /**
     *执行人姓名
     */
    private String nameEmpOcc;

    public String getNameEmpOcc() {
        return nameEmpOcc;
    }

    public void setNameEmpOcc(String nameEmpOcc) {
        this.nameEmpOcc = nameEmpOcc;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getPkEmpOcc() {
        return pkEmpOcc;
    }

    public void setPkEmpOcc(String pkEmpOcc) {
        this.pkEmpOcc = pkEmpOcc;
    }
}
