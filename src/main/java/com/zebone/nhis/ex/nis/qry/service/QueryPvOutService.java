package com.zebone.nhis.ex.nis.qry.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.platform.Application;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.qry.dao.PvOutQueryMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 出院患者查询
 * @author yangxue
 *
 */
@Service
public class QueryPvOutService {

	@Resource
	private PvOutQueryMapper pvOutQueryMapper;
	/**
	 * 出院患者查询
	 * @param param{dateBegin,dateEnd,pkDeptNs,codePi,namePi,pkDept,euStatus,dateBeginIn,dateEndIn,pkPv}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPvOutList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
		String pkDeptNs = CommonUtils.getString(map.get("pkDeptNs"));
		if(pkDeptNs==null || pkDeptNs.equals("")){
			map.put("pkDeptNs",((User)user).getPkDept());
		}
		map.put("dataBaseType",Application.isSqlServer()?"sqlserver":"oracle");
		return pvOutQueryMapper.queryPvOutList(map);
	}
}
