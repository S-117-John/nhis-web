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
 * MRD广东省病案系统业务处理服务(无事务)
 * @author huanghaisheng
 *
 */
@Service
public class MrdSystemHandler {
	@Autowired
	private MrdSystemService mrdSystemService;
	
	public Object invokeMethod(String methodName, Object... args) {
		Object result = null;
		switch (methodName) {
		case "qrySettlements":
			result=this.qrySettlements(args);
			break;
		}
		return result;
	}
	
	/**
	 * 获取患者结算记录
	 * 此方法是提供在业务系统调用ExtSystemProcessUtils.processExtMethod使用
	 * @param args
	 */
	private Object qrySettlements(Object... args) {
		List<Map<String,Object>> rtnMapList=null;
		if (args != null) {
			Map<String,Object> paramMap=JsonUtil.readValue((String) args[0], Map.class);
			try {
				DataSourceRoute.putAppId("yy0040020");// 切换数据源
				// 调用Service类保存数据并提交事务
				rtnMapList=mrdSystemService.qrySettlements(paramMap);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("从广东省病案系统获取患者结算记录失败！");
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
		return rtnMapList;
	}
	
	/**
	 * 获取患者结算记录
	 * 此方法是提供给直接通过交易码使用(因为切换了数据源，所有写在Handle类里面--无事务)
	 */
	public List<Map<String,Object>>  qrySettlements(String param,IUser user) throws Exception{
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnMapList=null;
		if (paramMap != null) {
			try {
				// 切换数据源
				DataSourceRoute.putAppId("syxip");
				// 调用Service类保存数据并提交事务
				rtnMapList=mrdSystemService.qrySettlements(paramMap);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("从广东省病案系统获取患者结算记录失败！");
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
		return rtnMapList;
	}

}
