package com.zebone.nhis.cn.pub.vo;

/**
 * @Classname QueryCnEmyImgSign
 * @Description
 * 用作存放医嘱打印签名照片
 * * @Date 2019-11-08 13:29
 * @Created by wuqiang
 */
public class QueryCnEmyImgSign {
    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getEuAlways() {
        return euAlways;
    }

    public void setEuAlways(String euAlways) {
        this.euAlways = euAlways;
    }

    public byte[] getPkEmpOrdPic() {
        return pkEmpOrdPic;
    }

    public void setPkEmpOrdPic(byte[] pkEmpOrdPic) {
        this.pkEmpOrdPic = pkEmpOrdPic;
    }

    public byte[] getPkEmpChkPic() {
        return pkEmpChkPic;
    }

    public void setPkEmpChkPic(byte[] pkEmpChkPic) {
        this.pkEmpChkPic = pkEmpChkPic;
    }

    public byte[] getPkEmpExPic() {
        return pkEmpExPic;
    }

    public void setPkEmpExPic(byte[] pkEmpExPic) {
        this.pkEmpExPic = pkEmpExPic;
    }

    public byte[] getPkEmpStopPic() {
        return pkEmpStopPic;
    }

    public void setPkEmpStopPic(byte[] pkEmpStopPic) {
        this.pkEmpStopPic = pkEmpStopPic;
    }

    public byte[] getPkEmpStopChkPic() {
        return pkEmpStopChkPic;
    }

    public void setPkEmpStopChkPic(byte[] pkEmpStopChkPic) {
        this.pkEmpStopChkPic = pkEmpStopChkPic;
    }
   /**
    * 与前台约定，开立医生签名=cnOider中开立医生主键+ Pic
    * */
    private  String pkCnord;
    private  String euAlways;
    private byte[]  pkEmpOrdPic;
    private  byte[]  pkEmpChkPic;
    private byte[]   pkEmpExPic;
    private  byte[]  pkEmpStopPic;
    private byte[]   pkEmpStopChkPic;



}
