package com.zebone.nhis.webservice.syx.vo.platForm;

import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.platform.common.support.User;

public class RegApptVo {

    private RegPiMasterVo regvo;

    private SchSch schSch;

    private SchTicket ticket;

    private SchPlan schplan;

    private User u;

    private boolean isGh;

    public RegPiMasterVo getRegvo() {
        return regvo;
    }

    public void setRegvo(RegPiMasterVo regvo) {
        this.regvo = regvo;
    }

    public SchSch getSchSch() {
        return schSch;
    }

    public void setSchSch(SchSch schSch) {
        this.schSch = schSch;
    }

    public SchTicket getTicket() {
        return ticket;
    }

    public void setTicket(SchTicket ticket) {
        this.ticket = ticket;
    }

    public SchPlan getSchplan() {
        return schplan;
    }

    public void setSchplan(SchPlan schplan) {
        this.schplan = schplan;
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public boolean isGh() {
        return isGh;
    }

    public void setGh(boolean gh) {
        isGh = gh;
    }
}
