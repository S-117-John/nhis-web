package com.zebone.nhis.ma.pub.syx.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * JhEmr嘉禾病历系统业务处理服务(无事务)
 * @author huanghaisheng
 *
 */
@Service
public class JhEmrHandler {
	@Autowired
	private JhEmrService jhEmrService;
	
	/**
	 * 获取嘉禾病历列表
	 * 此方法是通过交易码使用(因为切换了数据源，所有写在Handle类里面--无事务)
	 */
	public List<Map<String,Object>>  qryJhEmrList(String param,IUser user) throws Exception{
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnMapList=null;
		if (paramMap != null) {
			try {
				// 切换数据源
				DataSourceRoute.putAppId("jhemr");
				// 调用Service类保存数据并提交事务
				rtnMapList=jhEmrService.qryJhEmrList(paramMap);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("从嘉禾病历系统获取病历数据失败！");
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
		return rtnMapList;
	}

}
