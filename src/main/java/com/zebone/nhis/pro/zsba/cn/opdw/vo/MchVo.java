package com.zebone.nhis.pro.zsba.cn.opdw.vo;

/**
 * @Classname MchVo
 * @Description 妇幼保健接口vo
 * @Date 2021-05-24 9:45
 * @Created by wuqiang
 */
public class MchVo {
    /**身份证号*/
    private String idNo;
    /**妇幼保健项目编码*/
    private String mchCode;
    /**是否符合补助条件 1 符合*/
    private String healthNo;
    /**是否已经开立 1开立 */
    private Integer isCheck;
    /**机构名称*/
    private String chkOrgan;
    /**开立时间*/
    private String chkDate;
    /**更新时主键*/
    private  String sysId;
    /**
     * 等于1时可以开立，除此之位给出message提示*/
    private Integer state;

    private  String message;

    /**门诊号*/
    private String codeOp;
   /** 医嘱序号*/
    private String orderSn;
    /*妇幼相关信息--*/
    /** 妇幼编码名称*/
    private  String ItemName;
    /**
     * 是否已做免费婚检
     * */
    private  String isMarrycheck;
    /** 年龄*/
    private  String dueAge;



    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMchCode() {
        return mchCode;
    }

    public void setMchCode(String mchCode) {
        this.mchCode = mchCode;
    }



    public String getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(String healthNo) {
        this.healthNo = healthNo;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public String getChkOrgan() {
        return chkOrgan;
    }

    public void setChkOrgan(String chkOrgan) {
        this.chkOrgan = chkOrgan;
    }

    public String getChkDate() {
        return chkDate;
    }

    public void setChkDate(String chkDate) {
        this.chkDate = chkDate;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getIsMarrycheck() {
        return isMarrycheck;
    }

    public void setIsMarrycheck(String isMarrycheck) {
        this.isMarrycheck = isMarrycheck;
    }

    public String getDueAge() {
        return dueAge;
    }

    public void setDueAge(String dueAge) {
        this.dueAge = dueAge;
    }
}
