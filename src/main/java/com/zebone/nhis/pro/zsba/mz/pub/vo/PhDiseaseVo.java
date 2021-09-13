package com.zebone.nhis.pro.zsba.mz.pub.vo;

import com.zebone.nhis.common.module.pro.lb.PhDisease;
import com.zebone.nhis.common.module.pro.lb.PhDiseaseFoodborne;
import com.zebone.nhis.common.module.pro.lb.PhDiseaseFoodborneDt;
import com.zebone.nhis.common.module.pro.lb.PhDiseaseSample;

import java.util.List;

public class PhDiseaseVo {

    private PhDisease phDisease;

    private List<PhDiseaseSample> sampleList;

    private PhDiseaseFoodborne borne;

    private List<PhDiseaseFoodborneDt> borneDtList;

    public PhDisease getPhDisease() {
        return phDisease;
    }

    public void setPhDisease(PhDisease phDisease) {
        this.phDisease = phDisease;
    }

    public List<PhDiseaseSample> getSampleList() {
        return sampleList;
    }

    public void setSampleList(List<PhDiseaseSample> sampleList) {
        this.sampleList = sampleList;
    }

    public PhDiseaseFoodborne getBorne() {
        return borne;
    }

    public void setBorne(PhDiseaseFoodborne borne) {
        this.borne = borne;
    }

    public List<PhDiseaseFoodborneDt> getBorneDtList() {
        return borneDtList;
    }

    public void setBorneDtList(List<PhDiseaseFoodborneDt> borneDtList) {
        this.borneDtList = borneDtList;
    }
}
