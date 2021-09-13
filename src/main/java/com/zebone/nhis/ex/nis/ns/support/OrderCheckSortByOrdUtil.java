package com.zebone.nhis.ex.nis.ns.support;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.nhis.ex.pub.support.SortByOrdUtil;

public class OrderCheckSortByOrdUtil extends SortByOrdUtil{

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof OrderCheckVo){
			OrderCheckVo vo = (OrderCheckVo)o;
			vo.setSign(sign);
		}
	}

	@Override
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof OrderCheckVo)||!(vo instanceof OrderCheckVo)){
			return false;
		}
		OrderCheckVo before = (OrderCheckVo)vo_before;
		if(CommonUtils.isEmptyString(CommonUtils.getString(before.getOrdsnParent()))){
			return false;
		}
		OrderCheckVo now = (OrderCheckVo)vo;
		if(CommonUtils.isEmptyString(CommonUtils.getString(now.getOrdsnParent()))){
			return false;
		}
		return now.getOrdsnParent().equals(before.getOrdsnParent());
	}

}
