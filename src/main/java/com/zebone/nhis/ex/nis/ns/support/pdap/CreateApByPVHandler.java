package com.zebone.nhis.ex.nis.ns.support.pdap;

import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;

/**
 * 生成同一患者的请领单
 * @author yangxue
 *
 */
public class CreateApByPVHandler extends CreateApHandler{

	@Override
	protected CreateApHandler getNext() {
		if(null == next){
			next = new CreateApBySortnoPHandler();
		}
		return next;
	}

	
	@Override
	protected String getKeyValue(GeneratePdApExListVo vo) {
		return vo.getPkPv();
	}

	
	@Override
	public String getErrorMsg(GeneratePdApExListVo vo) {
		return vo.getNamePi() + "医嘱生成有误，请查看！";
	}
	
	
}
