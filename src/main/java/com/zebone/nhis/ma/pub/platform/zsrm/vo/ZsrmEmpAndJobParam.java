package com.zebone.nhis.ma.pub.platform.zsrm.vo;

import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;

import java.util.ArrayList;
import java.util.List;

public class ZsrmEmpAndJobParam {
    /**
     * 人员信息
     */
    private BdOuEmployee emp = new BdOuEmployee();

    /**
     * 关系
     */
    private List<BdOuEmpjob> empJobs = new ArrayList<BdOuEmpjob>();

    /**
     * 是否发送推送消息到平台
     */
    private String flagSendMsg;

    public String getFlagSendMsg() {
        return flagSendMsg;
    }

    public void setFlagSendMsg(String flagSendMsg) {
        this.flagSendMsg = flagSendMsg;
    }

    public BdOuEmployee getEmp() {
        return emp;
    }

    public void setEmp(BdOuEmployee emp) {
        this.emp = emp;
    }

    public List<BdOuEmpjob> getEmpJobs() {
        return empJobs;
    }

    public void setEmpJobs(List<BdOuEmpjob> empJobs) {
        this.empJobs = empJobs;
    }
}

