package com.zebone.nhis.pro.zsba.mz.pub.vo;

import com.zebone.nhis.common.module.pro.lb.PhDisease;

public class PhDiseaseHarmVo {
    /**
     * 伤害报卡表
     */
    public PhDiseaseHarm phDiseaseHarm;
    /**
     * 报卡主表
     */
    public PhDisease phDisease;

    public PhDiseaseHarm getPhDiseaseHarm() {
        return phDiseaseHarm;
    }

    public void setPhDiseaseHarm(PhDiseaseHarm phDiseaseHarm) {
        this.phDiseaseHarm = phDiseaseHarm;
    }

    public PhDisease getPhDisease() {
        return phDisease;
    }

    public void setPhDisease(PhDisease phDisease) {
        this.phDisease = phDisease;
    }
}
