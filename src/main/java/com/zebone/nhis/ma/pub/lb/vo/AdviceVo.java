package com.zebone.nhis.ma.pub.lb.vo;

public class AdviceVo {
    /**
     * 就诊流水号
     */
    private String Visitno;
    /**
     * 就诊机构编码
     */
    private String orgcode;
    /**
     * 就诊机构名称
     */
    private String orgname;

    public String getVisitno() {
        return Visitno;
    }

    public void setVisitno(String visitno) {
        Visitno = visitno;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
}
