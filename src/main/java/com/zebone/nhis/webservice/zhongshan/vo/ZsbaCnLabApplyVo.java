package com.zebone.nhis.webservice.zhongshan.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;

public class ZsbaCnLabApplyVo extends CnLabApply  {
	
	public String pkEmpOrd; //开立医生主键

	public String euOrdtype; //科研医嘱标志

    public String flagFit;//适应症标志

    public String getEuOrdtype() {
        return euOrdtype;
    }

    public void setEuOrdtype(String euOrdtype) {
        this.euOrdtype = euOrdtype;
    }

    public String getFlagFit() {
        return flagFit;
    }

    public void setFlagFit(String flagFit) {
        this.flagFit = flagFit;
    }

	public String getPkEmpOrd() {
		return pkEmpOrd;
	}

	public void setPkEmpOrd(String pkEmpOrd) {
		this.pkEmpOrd = pkEmpOrd;
	}
    
}
