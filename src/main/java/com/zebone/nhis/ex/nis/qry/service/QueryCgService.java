package com.zebone.nhis.ex.nis.qry.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ex.nis.fee.dao.BlCgIpQueryMapper;
import com.zebone.nhis.ex.nis.qry.dao.ExListQueryMapper;
import com.zebone.nhis.ex.nis.qry.dao.OrderQueryMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 费用联查服务
 * @author yangxue
 *
 */
@Service
public class QueryCgService {
	@Resource
	private OrderQueryMapper ordQueryMapper;
	@Resource
	private ExListQueryMapper exlistQueryMapper;
	@Resource
	private BlCgIpQueryMapper blCgIpQueryMapper;
	/**
	 * 根据患者就诊主键查询患者费用明细
	 * @param param{pkPv,nameCg,flagPd}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryCgDetailsByPv(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		//String pk_dept_cur = ((User)user).getPkDept();
		//map.put("pkDeptNs", pk_dept_cur);
		return blCgIpQueryMapper.queryBlCgIpDetailsByPv(map);
	}
	/**
	 * 根据计费明细查询执行列表
	 * @param param{pkCgip}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryExlistByCg(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		return exlistQueryMapper.queryExlistByCg(map);
	}
	/**
	 * 根据计费明细查询医嘱列表
	 * @param param{pkCgip}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryOrderByCg(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		return ordQueryMapper.queryOrdByCg(map);
	}
	
}
