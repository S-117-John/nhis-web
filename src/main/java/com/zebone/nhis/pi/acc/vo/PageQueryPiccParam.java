package com.zebone.nhis.pi.acc.vo;

/**
 * @Classname PageQueryPiccParam
 * @Description 患者信息查询参数
 * * @Date 2019-11-06 1:14
 * @Created by wuqiang
 */
 public  class PageQueryPiccParam {

    /**
     * 患者编码
     */
     private String codePi;

    /* *
     * 患者姓名
     */
     private String namePi;
    /* *
     *证件号码
     */
     private String idNo;
    /**
     * 卡号
     */
     private String cardNo;
    /// <summary>
    /// 每页行数
    /// </summary>

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    /** 所属机构 */
    private String pkOrg;
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 每行页数
     */
     private int pageSize ;
    /**
     * 页码
     */
     private int pageIndex ;

     private String getCodePi() {
        return codePi;
    }

     private void setCodePi(String codePi) {
        this.codePi = codePi;
    }

     private String getNamePi() {
        return namePi;
    }

     private void setNamePi(String namePi) {
        this.namePi = namePi;
    }

     private String getIdNo() {
        return idNo;
    }

     private void setIdNo(String idNo) {
        this.idNo = idNo;
    }

     private String getCardNo() {
        return cardNo;
    }

     private void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

     private String getMobile() {
        return mobile;
    }

     private void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /*
     *发卡时间开始
     */
     private String mobile;


}
