package com.zebone.nhis.ex.pub.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;

public class OrderSortUtil {
	/**
	 * 医嘱分组方法
	 * @param ords
	 * @return
	 */
	public static  Map<String,List<GenerateExLisOrdVo>> groupOrdBySortnoP(List<GenerateExLisOrdVo> ords){
		Map<String,List<GenerateExLisOrdVo>> map = new HashMap<String,List<GenerateExLisOrdVo>>();
		for(GenerateExLisOrdVo ord : ords){
			//String sortno = ord.getOrdsn();
			String sortno_parent = ord.getOrdsnParent();
			if(map.get(sortno_parent) == null ){
				List<GenerateExLisOrdVo> list = new ArrayList<GenerateExLisOrdVo>();
				list.add(ord);
				map.put(sortno_parent, list);
			}else{
				map.get(sortno_parent).add(ord);	
			}
			
		}
		return map;
	}
	 public static  String convertPkListToStr(List<String> pkordlist){
	    	if(pkordlist == null||pkordlist.size()<=0) return null;
	    	String pk_ord_str="";
	    	for(String pk_ord:pkordlist){
	    		pk_ord_str = pk_ord_str +",'"+pk_ord+"'";
	    	}
	    	return pk_ord_str;
	    }
}
