package com.zebone.nhis.ex.pub.support.wf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.BdWfOrdArguVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
  
/**
 * 过滤服务类型
 * @author yangxue
 *
 */
public class GetOrdWfExOrderTypeHandler extends GetOrdWfHandler{

	
	
	@Override
	protected GetOrdWfHandler getSuccessor() {
		return new GetOrdWfExUsageHandler();
	}

	@Override
	public Map<String, Object> exce(BdWfOrdArguVo vo,
			List<Map<String, Object>> list) throws BusException {
		List<Map<String, Object>> result = this.getResultList(list, vo.getOrderType());
		if(result == null || result.size() == 0)
			return null;
		if(result.size() > 1)//根据类型过滤后根据用法过滤
			return getSuccessor().exce(vo, result);
		return result.get(0);
	}
	
	/**
	 * 递归计算符合条件的数据，当前dt_srvtype无数据，取上级，比较
	 * @param list
	 * @param dt_srvtype
	 * @return
	 * @throws BusException
	 */
	private List<Map<String, Object>> getResultList(
			List<Map<String, Object>> list, String ordtype)
			throws BusException {
		if(ordtype == null){
			return null;
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> map : list){
			if(map.get("orderType").equals(ordtype))
				result.add(map);
		}
		if(result.size() == 0){
			String pDt = getParentType(ordtype);
			if(null != pDt && !"".equals(pDt))
				return getResultList(list, this.getParentType(ordtype));
		}
		return result;
	}
	
	/**
	 * 获取上级ordtype
	 * @param ordtype
	 * @return
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	private String getParentType(String ordtype) throws BusException{
		String sql = "select a.code from bd_ordtype a,bd_ordtype b where a.pk_ordtype = b.pk_parent and b.code = ? ";
		List<Map<String, Object>> list = (List<Map<String, Object>>) DataBaseHelper.queryForList(sql, ordtype);
		if(list != null && list.size() > 0)
			return list.get(0).get("code").toString();
		else
			return null;
	}
}
