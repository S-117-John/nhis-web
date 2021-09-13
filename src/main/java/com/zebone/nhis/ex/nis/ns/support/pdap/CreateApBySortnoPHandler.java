package com.zebone.nhis.ex.nis.ns.support.pdap;

import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;


/**
 * 生成一组医嘱的请领单
 * @author yangxue
 *
 */
public class CreateApBySortnoPHandler extends CreateApHandler{

	@Override
	protected CreateApHandler getNext() {
		if(null == next){
			next = new CreateApByOrdPHandler();
		}
		return next;
	}

	
	@Override
	protected String getKeyValue(GeneratePdApExListVo vo) {
		return vo.getPkPv();
	}
	
	
	@Override
	public String getErrorMsg(GeneratePdApExListVo vo) {
		return vo.getNamePi() + vo.getPdname() +"医嘱生成有误，请查看！";
	}
}
