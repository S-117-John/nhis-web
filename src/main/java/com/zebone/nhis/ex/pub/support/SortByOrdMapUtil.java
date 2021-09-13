package com.zebone.nhis.ex.pub.support;

import java.util.Map;


public class SortByOrdMapUtil extends SortByOrdUtil {

	@Override 
	protected boolean isSameGroup(Object vo_before, Object vo) {
		if(!(vo_before instanceof Map)||!(vo instanceof Map)){
			return false;
		}
		Map<String,Object> before = (Map<String,Object>)vo_before;
		Map<String,Object> now = (Map<String,Object>)vo;
		return (now.get("ordsnParent").toString()).equals(before.get("ordsnParent").toString());
	}

	@Override
	protected void setSign(Object o, String sign) {
		if(o instanceof Map){
			Map<String,Object> vo = (Map<String,Object>)o;
			vo.put("sign", sign);
		}
	}


}
