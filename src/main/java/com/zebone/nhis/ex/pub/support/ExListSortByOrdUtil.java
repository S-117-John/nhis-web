package com.zebone.nhis.ex.pub.support;

import com.zebone.nhis.ex.pub.vo.ExlistPubVo;

public class ExListSortByOrdUtil extends SortByOrdUtil{

	@Override 
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof ExlistPubVo)||!(vo instanceof ExlistPubVo)){
			return false;
		}
		ExlistPubVo before = (ExlistPubVo)vo_before;
		ExlistPubVo now = (ExlistPubVo)vo;
		//如果是临时医嘱，只判断父医嘱号
		if("1".equals(before.getEuAlwaysFreq())&&"1".equals(now.getEuAlwaysFreq())){
			return now.getOrdsnParent()==before.getOrdsnParent();
		}else{
			return now.getOrdsnParent()==before.getOrdsnParent()&&now.getDatePlan().getTime()==before.getDatePlan().getTime();
		}
	}

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof ExlistPubVo){
			ExlistPubVo vo = (ExlistPubVo)o;
			vo.setSign(sign);
		}
	}

}
