package com.zebone.nhis.scm.st.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.scm.st.dao.SpecialPdStatisticMapper;
import com.zebone.nhis.scm.st.vo.SpecialPdVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 特殊药品统计
 * @author yangxue
 *
 */
@Service
public class SpecialPdStatisticService {
	@Resource
	private SpecialPdStatisticMapper specialPdStatisticMapper;
	/**
	 * 统计特殊药品数量
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SpecialPdVo>  querySpecialPdNumList(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||paramMap.get("dateBegin")==null||paramMap.get("dateEnd")==null){
			throw new BusException("未获取到需要统计的时间区间！");
		}
		paramMap.put("dateBegin",paramMap.get("dateBegin")+"000000");
		paramMap.put("dateEnd", paramMap.get("dateEnd")+"235959");
		paramMap.put("pkDept",((User)user).getPkDept());
		List<SpecialPdVo> list = specialPdStatisticMapper.querySpecialPdNumList(paramMap);
		return list;
	}
	/**
	 * 查询出入库明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryStDetailByPd(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||paramMap.get("dateBegin")==null||paramMap.get("dateEnd")==null){
			return null;
		}
		paramMap.put("dateBegin",paramMap.get("dateBegin")+"000000");
		paramMap.put("dateEnd", paramMap.get("dateEnd")+"235959");
		paramMap.put("pkDept",((User)user).getPkDept());
		List<Map<String,Object>> list = specialPdStatisticMapper.queryStDetailByPd(paramMap);
		return list;
	}

}
