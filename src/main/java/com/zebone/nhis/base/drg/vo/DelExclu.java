package com.zebone.nhis.base.drg.vo;

public class DelExclu {
    private String  pkCcdtexclu ;
    private String  groupno;
    private String  groupnoExclu;
    private String  euExclutype;
    private String yesOrNo;

    public String getYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(String yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public String getPkCcdtexclu() {
        return pkCcdtexclu;
    }

    public void setPkCcdtexclu(String pkCcdtexclu) {
        this.pkCcdtexclu = pkCcdtexclu;
    }

    public String getGroupno() {
        return groupno;
    }

    public void setGroupno(String groupno) {
        this.groupno = groupno;
    }

    public String getGroupnoExclu() {
        return groupnoExclu;
    }

    public void setGroupnoExclu(String groupnoExclu) {
        this.groupnoExclu = groupnoExclu;
    }

    public String getEuExclutype() {
        return euExclutype;
    }

    public void setEuExclutype(String euExclutype) {
        this.euExclutype = euExclutype;
    }


}
