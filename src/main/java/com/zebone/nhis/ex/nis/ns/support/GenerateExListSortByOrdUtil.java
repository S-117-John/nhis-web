package com.zebone.nhis.ex.nis.ns.support;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyVo;
import com.zebone.nhis.ex.pub.support.SortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;


public class GenerateExListSortByOrdUtil extends SortByOrdUtil{

	@Override
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof GeneratePdApExListVo)||!(vo instanceof GeneratePdApExListVo)){
			return false;
		}
		GeneratePdApExListVo before = (GeneratePdApExListVo)vo_before;
		if(CommonUtils.isEmptyString(before.getOrdsnParent())){
			return false;
		}
		GeneratePdApExListVo now = (GeneratePdApExListVo)vo;
		if(CommonUtils.isEmptyString(now.getOrdsnParent())){
			return false;
		}
		
		return now.getOrdsnParent().equals(before.getOrdsnParent())&&DateUtils.getDateTimeStr(now.getDateEx()).equals(DateUtils.getDateTimeStr(before.getDateEx()));
	}

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof GeneratePdApExListVo){
			GeneratePdApExListVo vo = (GeneratePdApExListVo)o;
			vo.setSign(sign);
		}
	}

	
}
