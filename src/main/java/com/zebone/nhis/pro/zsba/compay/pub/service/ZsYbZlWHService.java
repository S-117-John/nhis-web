package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 中山医保资料维护验证
 * @author Administrator
 *
 */
@Service
public class ZsYbZlWHService {

	/**
	 * 验证是否存在生育资料、计生手术的维护资料信息
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void isExistInfo(String param, IUser user) {
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class); 
		String pkPv = (String)paraMap.get("pkPv");
		String type = (String)paraMap.get("type");
		
		if(StringUtils.isEmpty(pkPv) && StringUtils.isEmpty(type)){
			throw new BusException("参数[pkPv]、[type]不能为空！");
		}
		
		if("1".equals(type)){//生育
			String sql = "select count(1) as num from INS_BEAR where fhz='1' and pk_pv=?";
			Map<String, Object> data = DataBaseHelper.queryForMap(sql, pkPv);
			if(data.isEmpty() || data.get("num").toString().equals("0")){
				throw new BusException("不存在【生育资料】维护信息，请先到【临床管理】【医保维护】下维护后再做出院登记！");
			}
		}

		if("2".equals(type)){//计生手术
			String sql = "select count(1) as num from INS_OPT_PB where fhz='1' and pk_pv=?";
			Map<String, Object> data = DataBaseHelper.queryForMap(sql, pkPv);
			if(data.isEmpty() || data.get("num").toString().equals("0")){
				throw new BusException("不存在【计生手术】维护信息，请先到【临床管理】【医保维护】下维护后再做出院登记！");
			}
		}
		
		if("3".equals(type)){//日间手术
			String sql = "select count(1) as num from INS_OPT_DAY where fhz='1' and pk_pv=?";
			Map<String, Object> data = DataBaseHelper.queryForMap(sql, pkPv);
			if(data.isEmpty() || data.get("num").toString().equals("0")){
				throw new BusException("不存在【日间手术】维护信息，请先到【临床管理】【医保维护】下维护后再做出院登记！");
			}
		}
	}
}
