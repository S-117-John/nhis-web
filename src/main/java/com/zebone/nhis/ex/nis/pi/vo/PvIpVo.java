package com.zebone.nhis.ex.nis.pi.vo;

import com.zebone.nhis.common.module.pv.PvIp;

public class PvIpVo extends PvIp {

    private String pkDept;

    private String pkDeptNs;

    private String pkWard;

    public String getPkDept() {return pkDept;}
    public void setPkDept(String pkDept) {this.pkDept = pkDept;}

    public String getPkDeptNs() {return pkDeptNs;}
    public void setPkDeptNs(String pkDeptNs) {this.pkDeptNs = pkDeptNs;}

    public String getPkWard() {return pkWard;}
    public void setPkWard(String pkWard) {this.pkWard = pkWard;}
}
