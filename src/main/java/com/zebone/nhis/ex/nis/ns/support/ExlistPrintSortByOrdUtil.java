package com.zebone.nhis.ex.nis.ns.support;

import java.util.Map;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.support.SortByOrdUtil;

public class ExlistPrintSortByOrdUtil extends SortByOrdUtil{

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof Map){
			Map<String,Object> map = (Map<String,Object>)o;
			map.put("sign", sign);
		}
	}

	@Override
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof Map)||!(vo instanceof Map)){
			return false;
		}
		Map<String,Object> before = (Map<String,Object>)vo_before;
		if(CommonUtils.isNull(before.get("ordsnParent"))){
			return false;
		}
		Map<String,Object> now = (Map<String,Object>)vo;
		if(CommonUtils.isNull(now.get("ordsnParent"))){
			return false;
		}
		//如果是临时医嘱，只判断父医嘱号
		if("1".equals(before.get("euAlwaysFreq"))&&"1".equals(now.get("euAlwaysFreq"))){
			return CommonUtils.getString(now.get("ordsnParent")).equals(CommonUtils.getString(before.get("ordsnParent")));
		}else{
		    return CommonUtils.getString(now.get("ordsnParent")).equals(CommonUtils.getString(before.get("ordsnParent")))
				&& ( CommonUtils.isNull(now.get("datePlanDis")) 
				     || ( !CommonUtils.isNull(now.get("datePlanDis"))
						    && now.get("datePlanDis").equals(before.get("datePlanDis"))));
		}
	}

}
