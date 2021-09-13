package com.zebone.nhis.ex.pub.support.wf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguVo;
import com.zebone.platform.modules.exception.BusException;

/**
 * 根据医嘱用法过滤
 * @author yangxue
 *
 */
public class GetOrdWfExUsageHandler extends GetOrdWfHandler{

	@Override
	protected GetOrdWfHandler getSuccessor() {
		return new GetOrdWfHandler();
	}

	@Override
	public Map<String, Object> exce(BdWfOrdArguVo vo,List<Map<String,Object>>  list)
			throws BusException {
		String pk_supplycate = vo.getPkSupplycate();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> arguMap: list){
			String spcate = CommonUtils.getString(arguMap.get("pkSupplycate"));
			if(pk_supplycate == null || "".equals(pk_supplycate)) {
				//如果传入值为空，待查数据也是空，通过，否则不通过
				if(null == spcate || "".equals(pk_supplycate)){
					result.add(arguMap);
				}
				continue;
			}
			if(pk_supplycate.equals(spcate)){
				result.add(arguMap);
			}
		}
		if(result.size() == 0){
			return null;
		}
		if(result.size() == 1)
			return result.get(0);
		return getSuccessor().exce(vo, result);
	}

}
