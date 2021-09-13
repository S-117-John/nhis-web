package com.zebone.nhis.ex.nis.ns.support;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.vo.OrderOccVo;
import com.zebone.nhis.ex.pub.support.SortByOrdUtil;


public class ExOccSortByOrdUtil extends SortByOrdUtil{

	@Override
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof OrderOccVo)||!(vo instanceof OrderOccVo)){
			return false;
		}
		OrderOccVo before = (OrderOccVo)vo_before;
		if(CommonUtils.isEmptyString(before.getOrdsnParent())){
			return false;
		}
		OrderOccVo now = (OrderOccVo)vo;
		if(CommonUtils.isEmptyString(now.getOrdsnParent())){
			return false;
		}
		return now.getOrdsnParent().equals(before.getOrdsnParent());
	}

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof OrderOccVo){
			OrderOccVo vo = (OrderOccVo)o;
			vo.setSign(sign);
		}
	}

	
}
