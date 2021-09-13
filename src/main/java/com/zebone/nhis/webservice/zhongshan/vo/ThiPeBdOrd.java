package com.zebone.nhis.webservice.zhongshan.vo;

/**
 * @Classname ThiPeBdOrd
 * @Description 16号接口医嘱基本信息
 * @Date 2021-06-17 14:34
 * @Created by wuqiang
 */
public class ThiPeBdOrd {
    /** 医嘱主键*/
    private String pkOrd;
    /** 医嘱编码*/
    private String codeord;
    /** 医嘱名称*/
    private String name;
    /** 医嘱类型*/
    private String codeOrdtype;
    /** 医嘱单价*/
    private  Double price;
    /** 默认频次*/
    private String codeFreq;
    /** 记费标记*/
    private String flagCg;
    /** 标本类型*/
    private String dtSamptype;
    /** 试管类型*/
    private String dtContype;
    /** 采集方法*/
    private String dtColltype;
    //检查信息
    /** 检查部位*/
    private String dtBody;

    /** 注意事项*/
    private String descAtt;

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public String getCodeord() {
        return codeord;
    }

    public void setCodeord(String codeord) {
        this.codeord = codeord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeOrdtype() {
        return codeOrdtype;
    }

    public void setCodeOrdtype(String codeOrdtype) {
        this.codeOrdtype = codeOrdtype;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCodeFreq() {
        return codeFreq;
    }

    public void setCodeFreq(String codeFreq) {
        this.codeFreq = codeFreq;
    }

    public String getFlagCg() {
        return flagCg;
    }

    public void setFlagCg(String flagCg) {
        this.flagCg = flagCg;
    }

    public String getDtSamptype() {
        return dtSamptype;
    }

    public void setDtSamptype(String dtSamptype) {
        this.dtSamptype = dtSamptype;
    }

    public String getDtContype() {
        return dtContype;
    }

    public void setDtContype(String dtContype) {
        this.dtContype = dtContype;
    }

    public String getDtColltype() {
        return dtColltype;
    }

    public void setDtColltype(String dtColltype) {
        this.dtColltype = dtColltype;
    }

    public String getDtBody() {
        return dtBody;
    }

    public void setDtBody(String dtBody) {
        this.dtBody = dtBody;
    }

    public String getDescAtt() {
        return descAtt;
    }

    public void setDescAtt(String descAtt) {
        this.descAtt = descAtt;
    }

    @Override
    public String toString() {
        return "ThiPeBdOrd{" +
                "pkOrd='" + pkOrd + '\'' +
                ", codeord='" + codeord + '\'' +
                ", name='" + name + '\'' +
                ", codeOrdtype='" + codeOrdtype + '\'' +
                ", price=" + price +
                ", codeFreq='" + codeFreq + '\'' +
                ", flagCg='" + flagCg + '\'' +
                ", dtSamptype='" + dtSamptype + '\'' +
                ", dtContype='" + dtContype + '\'' +
                ", dtColltype='" + dtColltype + '\'' +
                ", dtBody='" + dtBody + '\'' +
                ", descAtt='" + descAtt + '\'' +
                '}';
    }
}
