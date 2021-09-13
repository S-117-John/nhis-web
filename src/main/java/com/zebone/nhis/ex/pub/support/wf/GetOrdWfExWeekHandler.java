package com.zebone.nhis.ex.pub.support.wf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguVo;
import com.zebone.platform.modules.exception.BusException;

/**
 * 医嘱流向计算算法，过滤星期值
 * @author yangxue
 *
 */
public class GetOrdWfExWeekHandler extends GetOrdWfHandler{

	@Override
	protected GetOrdWfHandler getSuccessor() {
		return new GetOrdWfExOrderTypeHandler();
	}

	@Override
	public Map<String, Object> exce(BdWfOrdArguVo vo ,List<Map<String, Object>> list)
			throws BusException {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> arguMap: list){
			if(this.isWeekInclude(vo.getWeeknos(), arguMap.get("weeknos"))){
				result.add(arguMap);
			}
		}
		if(result.size() == 0){
			return null;
		}
		//再根据服务类型过滤
		return this.getSuccessor().exce(vo, result);
	}
	
	/**
	 * 过滤星期值，规则：
	 * 1.如果医嘱流向VO维护为空，表示表示全部支持，
	 * 2.如果传入参数为空，默认当天星期		
	 * @param week,星期数对应的数字
	 * @param weeknos，星期数串
	 * @return
	 */
	private boolean isWeekInclude(String week,Object weeknos){
		if(weeknos == null || "".equals(weeknos))
			return true;
		if(null == week || "".equals(week)){
			week = DateUtils.getDayNumOfWeek(new Date())+"";
		}
		String wk_str = weeknos.toString();
		if(wk_str.contains(week))
			return true;
		return false;
	}
	
}
