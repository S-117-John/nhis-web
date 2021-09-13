package com.zebone.nhis.ex.nis.ns.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.cp.CpRecExpDt;
/**
 * 临床路径变异记录
 * @author yangxue
 *
 */
public class CpRecExpVo extends CpRecExp{
    private List<CpRecExpDt> dtlist;
    
    
	public List<CpRecExpDt> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<CpRecExpDt> dtlist) {
		this.dtlist = dtlist;
	}
    
}
