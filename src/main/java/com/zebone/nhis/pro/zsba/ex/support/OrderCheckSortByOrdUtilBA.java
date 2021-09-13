package com.zebone.nhis.pro.zsba.ex.support;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.support.SortByOrdUtil;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;

public class OrderCheckSortByOrdUtilBA extends SortByOrdUtil{

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof OrderCheckVO){
			OrderCheckVO vo = (OrderCheckVO)o;
			vo.setSign(sign);
		}
	}

	@Override
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof OrderCheckVO)||!(vo instanceof OrderCheckVO)){
			return false;
		}
		OrderCheckVO before = (OrderCheckVO)vo_before;
		if(CommonUtils.isEmptyString(CommonUtils.getString(before.getOrdsnParent()))){
			return false;
		}
		OrderCheckVO now = (OrderCheckVO)vo;
		if(CommonUtils.isEmptyString(CommonUtils.getString(now.getOrdsnParent()))){
			return false;
		}
		return now.getOrdsnParent().equals(before.getOrdsnParent());
	}

}
