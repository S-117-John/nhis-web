package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import com.zebone.nhis.common.module.pi.InsQgybPV;

public class PiOpVo {
    /**患者医保信息**/
    private InsQgybPV pati;
    /**诊查费用**/
    private String zcf;
    /**医保**/
    private String yb;
    /**就诊主键**/
    private String pkPv;
    /**服务主键**/
    private String pkSchsrv;
    /**服务诊区**/
    private String pkDeptArea;
    /**日志输出来源**/
    private String ifLog;

    /**
     * 是否60岁以上老人，前端传来的标记，可以修改年龄
     */
    private String flagOldMan;

    /**
     * 是否中山医保-博爱传字段
     */
    private String flagZshp;

    public InsQgybPV getPati() {
        return pati;
    }

    public void setPati(InsQgybPV pati) {
        this.pati = pati;
    }

    public String getZcf() {
        return zcf;
    }

    public void setZcf(String zcf) {
        this.zcf = zcf;
    }

    public String getYb() {
        return yb;
    }

    public void setYb(String yb) {
        this.yb = yb;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkSchsrv() {
        return pkSchsrv;
    }

    public void setPkSchsrv(String pkSchsrv) {
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkDeptArea() {
        return pkDeptArea;
    }

    public void setPkDeptArea(String pkDeptArea) {
        this.pkDeptArea = pkDeptArea;
    }

    public String getIfLog() {
        return ifLog;
    }

    public void setIfLog(String ifLog) {
        this.ifLog = ifLog;
    }

    public String getFlagOldMan() {
        return flagOldMan;
    }

    public void setFlagOldMan(String flagOldMan) {
        this.flagOldMan = flagOldMan;
    }

    public String getFlagZshp() {
        return flagZshp;
    }

    public void setFlagZshp(String flagZshp) {
        this.flagZshp = flagZshp;
    }
}