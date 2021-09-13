package com.zebone.nhis.cn.ipdw.vo;

/**
 * 中国医疗服务操作分类与编码
 */
public class EmrCchiVO {

    private int sortno;

    private String codeCchi;

    private String nameCchi;

    private String descCchi;

    private String codeEmp;

    private String nameEmpEntry;

    private String codeDept;

    private String nameDept;

    private String flagMaj;


    public int getSortno() {
        return sortno;
    }

    public void setSortno(int sortno) {
        this.sortno = sortno;
    }

    public String getCodeCchi() {
        return codeCchi;
    }

    public void setCodeCchi(String codeCchi) {
        this.codeCchi = codeCchi;
    }

    public String getNameCchi() {
        return nameCchi;
    }

    public void setNameCchi(String nameCchi) {
        this.nameCchi = nameCchi;
    }

    public String getDescCchi() {
        return descCchi;
    }

    public void setDescCchi(String descCchi) {
        this.descCchi = descCchi;
    }

    public String getCodeEmp() {
        return codeEmp;
    }

    public void setCodeEmp(String codeEmp) {
        this.codeEmp = codeEmp;
    }

    public String getNameEmpEntry() {
        return nameEmpEntry;
    }

    public void setNameEmpEntry(String nameEmpEntry) {
        this.nameEmpEntry = nameEmpEntry;
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

    public String getFlagMaj() {
        return flagMaj;
    }

    public void setFlagMaj(String flagMaj) {
        this.flagMaj = flagMaj;
    }
}
