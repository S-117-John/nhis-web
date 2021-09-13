package com.zebone.nhis.pi.fix.vo;

/**
 * 患者信息查询对象
 */
public class PiParamsDto {
    String teCardNo;
    String teNamePi;
    String teMobile;


    public String getTeCardNo() {
        return teCardNo;
    }

    public void setTeCardNo(String teCardNo) {
        this.teCardNo = teCardNo;
    }

    public String getTeNamePi() {
        return teNamePi;
    }

    public void setTeNamePi(String teNamePi) {
        this.teNamePi = teNamePi;
    }

    public String getTeMobile() {
        return teMobile;
    }

    public void setTeMobile(String teMobile) {
        this.teMobile = teMobile;
    }
}
