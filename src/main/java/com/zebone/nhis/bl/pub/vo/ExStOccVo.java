package com.zebone.nhis.bl.pub.vo;

import com.zebone.nhis.common.module.ex.nis.ns.ExStOcc;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.List;


/**
 * Table: EX_ST_OCC  - ex_st_occ 
 *
 * @since 2019-07-26 05:53:54
 */
@Table(value="EX_ST_OCC")
public class ExStOccVo extends ExStOcc {

	private String euSt;
	
	private List<CnOrderVo> rlOrds;

    public List<CnOrderVo> getRlOrds() {
        return rlOrds;
    }

    public void setRlOrds(List<CnOrderVo> rlOrds) {
        this.rlOrds = rlOrds;
    }

    public String getEuSt() {
		return euSt;
	}
	public void setEuSt(String euSt) {
		this.euSt = euSt;
	}
    
}