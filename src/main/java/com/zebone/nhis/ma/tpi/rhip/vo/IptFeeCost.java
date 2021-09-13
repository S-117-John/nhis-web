package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 费用分类
 *
 */
@XmlRootElement(name = "Ipt_FeeCost")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptFeeCost {
    @XmlElement(name = "ZYFYFL_DM")
    protected String zyfyfl_dm;

    @XmlElement(name = "ZYFYFL_JE")
    protected String zyfyfl_je;

    @XmlElement(name = "FPFYFLMC")
    protected String fpfyflmc;

    public String getZyfyfl_dm() {
        return zyfyfl_dm;
    }

    public void setZyfyfl_dm(String zyfyfl_dm) {
        this.zyfyfl_dm = zyfyfl_dm;
    }

    public String getZyfyfl_je() {
        return zyfyfl_je;
    }

    public void setZyfyfl_je(String zyfyfl_je) {
        this.zyfyfl_je = zyfyfl_je;
    }

    public String getFpfyflmc() {
        return fpfyflmc;
    }

    public void setFpfyflmc(String fpfyflmc) {
        this.fpfyflmc = fpfyflmc;
    }
}
