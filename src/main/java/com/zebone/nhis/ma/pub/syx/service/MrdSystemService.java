package com.zebone.nhis.ma.pub.syx.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.ma.pub.syx.dao.MrdSystemMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class MrdSystemService {
	@Autowired
	private MrdSystemMapper mrdSystemMapper ;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<Map<String,Object>> qrySettlements(Map<String,Object> paramMap) throws Exception{
		return mrdSystemMapper.qrySettlements(paramMap);
	}
	
	/**
	 * 根据患者诊断（截取编码前5位）和操作编码查询可用的病种分值字典数据
	 */
	public List<Map<String,Object>>  qryDisescore(String param,IUser user) throws Exception{
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String sqlStr="select * from bd_term_diag_treatway";
		List<Map<String,Object>> rtnMapList = DataBaseHelper.queryForList(sqlStr, paramMap);
		return rtnMapList;
	}

}
