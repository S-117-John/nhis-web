package com.zebone.nhis.task.emr.vo;

public class EmrContextInfo {
    private String pkDoc;

    public String getPkDoc() {
        return pkDoc;
    }

    public void setPkDoc(String pkDoc) {
        this.pkDoc = pkDoc;
    }

    public String getPkTmp() {
        return pkTmp;
    }

    public void setPkTmp(String pkTmp) {
        this.pkTmp = pkTmp;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    private String pkTmp;

    private String context;

    private String docName;

    public String getPkRec() {
        return pkRec;
    }

    public void setPkRec(String pkRec) {
        this.pkRec = pkRec;
    }

    private String pkRec;
}
