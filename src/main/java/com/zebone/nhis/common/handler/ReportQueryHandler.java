package com.zebone.nhis.common.handler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 *
 * 报表查询服务
 * @author yangxue
 *
 */
@Service
public class ReportQueryHandler {

	private Logger logger = LoggerFactory.getLogger("nhis.report");
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
			logger.info("报表查询sql："+sql);

		}else{
			throw new BusException("缺少sql参数");
		}
		if(datasource.equals("")){
			return DataBaseHelper.queryForList(sql);
			//return (List<Map<String,Object>>) DataBaseHelper.queryForPageData(sql, 1, 100000).getRows();
		}else {
			try {
				DataSourceRoute.putAppId(datasource);
				return DataBaseHelper.queryForList(sql);
				//return (List<Map<String, Object>>) DataBaseHelper.queryForPageData(sql, 1, 100000).getRows();
			} catch (Exception e) {
				throw new BusException(e);
			} finally {
				DataSourceRoute.putAppId("default");
			}
		}
//		if(datasource.equals("")){
//			//return DataBaseHelper.queryForListFj(sql);
//			return DataBaseHelper.queryForList(sql);
//		}else{
//			try {
//				DataSourceRoute.putAppId(datasource);
//				return DataBaseHelper.queryForList(sql);
//				//return DataBaseHelper.queryForListFj(sql);
//			} catch (Exception e) {
//				throw new BusException(e);
//				//e.printStackTrace();
//			} finally {
//				DataSourceRoute.putAppId("default");
//			}
//		}

	}

}
