package com.zebone.nhis.ex.nis.ns.support;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyVo;
import com.zebone.nhis.ex.pub.support.SortByOrdUtil;


public class PdApplySortByOrdUtil extends SortByOrdUtil{

	@Override
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof PdApplyVo)||!(vo instanceof PdApplyVo)){
			return false;
		}
		PdApplyVo before = (PdApplyVo)vo_before;
		if(CommonUtils.isEmptyString(before.getOrdsnParent())){
			return false;
		}
		PdApplyVo now = (PdApplyVo)vo;
		if(CommonUtils.isEmptyString(now.getOrdsnParent())){
			return false;
		}
		boolean samePivas = false;
		if(now.getFlagPivas()==null&&before.getFlagPivas()==null)
			samePivas = true;
		if(now.getFlagPivas()!=null&&before.getFlagPivas()!=null&&now.getFlagPivas().equals(before.getFlagPivas()))
			samePivas = true;
		return now.getOrdsnParent().equals(before.getOrdsnParent())&&samePivas;
	}

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof PdApplyVo){
			PdApplyVo vo = (PdApplyVo)o;
			vo.setSign(sign);
		}
	}

	
}
