package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Period;

public class Member {

    private EntityWg entity;
    private Period period;
    private boolean inactive;

    public EntityWg getEntity() {
        return entity;
    }

    public void setEntity(EntityWg entity) {
        this.entity = entity;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }
}