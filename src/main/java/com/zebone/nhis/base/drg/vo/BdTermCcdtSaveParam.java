package com.zebone.nhis.base.drg.vo;

import com.zebone.nhis.common.module.base.bd.drg.BdTermCcdt;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCcdtExclu;

import java.util.List;

public class BdTermCcdtSaveParam extends BdTermCcdtExclu {

    private String tlname;
    private BdTermCcdtExclu bdTermCcdtExclu;
    private List<BdTermCcdtExclu> ccdts;
    private List<BdTermCcdtExclu> DelCcdts ;

    public String getTlname() {
        return tlname;
    }

    public void setTlname(String tlname) {
        this.tlname = tlname;
    }

    public BdTermCcdtExclu getBdTermCcdtExclu() {
        return bdTermCcdtExclu;
    }

    public void setBdTermCcdtExclu(BdTermCcdtExclu bdTermCcdtExclu) {
        this.bdTermCcdtExclu = bdTermCcdtExclu;
    }

    public List<BdTermCcdtExclu> getCcdts() {
        return ccdts;
    }

    public void setCcdts(List<BdTermCcdtExclu> ccdts) {
        this.ccdts = ccdts;
    }

    public List<BdTermCcdtExclu> getDelCcdts() {
        return DelCcdts;
    }

    public void setDelCcdts(List<BdTermCcdtExclu> delCcdts) {
        DelCcdts = delCcdts;
    }
}
