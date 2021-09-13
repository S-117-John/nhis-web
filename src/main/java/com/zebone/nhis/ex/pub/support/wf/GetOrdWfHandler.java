package com.zebone.nhis.ex.pub.support.wf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.BdWfOrdArguVo;
import com.zebone.platform.modules.exception.BusException;


/**
 * 符合医嘱用法和服务类别
 * @author yangxue
 *
 */
public class GetOrdWfHandler {
	
	protected GetOrdWfHandler getSuccessor(){
		return null;
	}
	
	public Map<String,Object> exce(BdWfOrdArguVo vo ,List<Map<String,Object>> list) throws BusException{
		String codeOrderType = vo.getOrderType();
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> arguMap: list){
			if(codeOrderType.equals(arguMap.get("orderType"))){
				result.add(arguMap);
			}
		}
		if(result.size() == 0)
			return null;
		if(result.size() != 1)
			throw new BusException("科室流向定义冲突，请检查！");
		return result.get(0);
	}
	
}
