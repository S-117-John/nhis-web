package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import java.util.List;

public class OpcgData {

    private String resourceType;
    private String implicitRules;
    private String patientid;
    private String namePi;
    private String idno;
    private String euPvtype;
    private String codeDept;
    private String nameDept;
    private String codeDoc;
    private String nameDoc;
    private String codeICD;
    private String nameICD;
    private String codeDiag;
    private String nameDIag;
    private String flagSpec;
    private List<CgData> cgDataList;


    public String getFlagSpec() {
        return flagSpec;
    }

    public void setFlagSpec(String flagSpec) {
        this.flagSpec = flagSpec;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getImplicitRules() {
        return implicitRules;
    }

    public void setImplicitRules(String implicitRules) {
        this.implicitRules = implicitRules;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
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

    public String getCodeDoc() {
        return codeDoc;
    }

    public void setCodeDoc(String codeDoc) {
        this.codeDoc = codeDoc;
    }

    public String getNameDoc() {
        return nameDoc;
    }

    public void setNameDoc(String nameDoc) {
        this.nameDoc = nameDoc;
    }

    public String getCodeICD() {
        return codeICD;
    }

    public void setCodeICD(String codeICD) {
        this.codeICD = codeICD;
    }

    public String getNameICD() {
        return nameICD;
    }

    public void setNameICD(String nameICD) {
        this.nameICD = nameICD;
    }

    public String getCodeDiag() {
        return codeDiag;
    }

    public void setCodeDiag(String codeDiag) {
        this.codeDiag = codeDiag;
    }

    public String getNameDIag() {
        return nameDIag;
    }

    public void setNameDIag(String nameDIag) {
        this.nameDIag = nameDIag;
    }

    public List<CgData> getCgDataList() {
        return cgDataList;
    }

    public void setCgDataList(List<CgData> cgDataList) {
        this.cgDataList = cgDataList;
    }
}