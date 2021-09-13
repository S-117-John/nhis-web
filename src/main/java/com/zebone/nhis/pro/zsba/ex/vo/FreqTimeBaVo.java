package com.zebone.nhis.pro.zsba.ex.vo;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;

public class FreqTimeBaVo extends BdTermFreqTime {
    public String euCycle;//周期类型

    public String getEuCycle() {
        return euCycle;
    }

    public void setEuCycle(String euCycle) {
        this.euCycle = euCycle;
    }
}
