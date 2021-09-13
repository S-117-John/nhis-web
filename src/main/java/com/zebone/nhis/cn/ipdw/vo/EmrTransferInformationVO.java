package com.zebone.nhis.cn.ipdw.vo;

/**
 * 转科信息
 */
public class EmrTransferInformationVO {

    private String pkDept;

    private String codeDept;

    private String nameDept;

    private String dateBegin;

    private String flagIcu;

    private String codeIcutype;

    private String nameIcutype;

    private String dateEnd;

    private String transDate;

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getCodeDept() {
        return codeDept;
    }

    public void setCodeDept(String codeDept) {
        this.codeDept = codeDept;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getFlagIcu() {
        return flagIcu;
    }

    public void setFlagIcu(String flagIcu) {
        this.flagIcu = flagIcu;
    }

    public String getCodeIcutype() {
        return codeIcutype;
    }

    public void setCodeIcutype(String codeIcutype) {
        this.codeIcutype = codeIcutype;
    }

    public String getNameIcutype() {
        return nameIcutype;
    }

    public void setNameIcutype(String nameIcutype) {
        this.nameIcutype = nameIcutype;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
