package com.zebone.nhis.compay.pub.support;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public enum  NationalDict {

    ACC_DETAIL("对账明细", Lists.newArrayList("psn_no","mdtrt_id","setl_id","msgid","stmt_rslt","refd_setl_flag","memo","medfee_sumamt","fund_pay_sumamt","acct_pay"));




    private String desc;
    private List<String> fileds;

    NationalDict(String desc, ArrayList<String> fields) {
        this.desc = desc;
        this.fileds = fields;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getFileds() {
        return fileds;
    }

    public void setFileds(List<String> fileds) {
        this.fileds = fileds;
    }
}
