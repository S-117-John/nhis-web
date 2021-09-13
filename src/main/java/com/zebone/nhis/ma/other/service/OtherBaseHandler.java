package com.zebone.nhis.ma.other.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 
 * 其他基础事务处理
 * @author chengjia
 * 
 */
@Service
public class OtherBaseHandler {


	/**
	 * 获取报表数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getRptData(String param , IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		String datasource="";
		if(map.containsKey("datasource")){
			datasource = map.get("datasource")==null?"default":map.get("datasource").toString();
		}
		String sql="";
		if(map.containsKey("sql")){
			sql =  CommonUtils.decode(map.get("sql").toString().getBytes());
		    //去除在报表中写的分号
		    if(!CommonUtils.isEmptyString(sql)){
		    	sql = sql.replaceAll(";", "");
		    }
		}else{
			throw new BusException("缺少sql参数");
		}
		
		if(datasource.equals("")){
			return DataBaseHelper.queryForList(sql);
		}else{
			try {
				DataSourceRoute.putAppId(datasource);
				return DataBaseHelper.queryForList(sql);
			} catch (Exception e) {
				throw new BusException(e);
				//e.printStackTrace();
			} finally {
				DataSourceRoute.putAppId("default");
			}
		}

	}
	

}
