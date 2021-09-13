package com.zebone.nhis.base.bd.service;

import com.zebone.nhis.common.module.base.ou.BdDatasource;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.ReflectionUtils;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonDictService {
	private Logger logger = LoggerFactory.getLogger("nhis.datasource");
	/**
	 * 获取系统当前时间
	 * @return
	 */
	public Map<String,Object> getCurrentDate(String param , IUser user){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		DateTime dt = new DateTime();
		resultMap.put("year", dt.getYear());
		resultMap.put("month", dt.getMonthOfYear());
		resultMap.put("day", dt.getDayOfMonth());
		resultMap.put("currentDatetime", dt.toString("yyyy-MM-dd HH:mm:ss"));
		return resultMap;
	}
	
	
	/**
	 * 动态执行数据源
	 * @param param
	 * @param user
	 * @return
	 */
	public ResponseJson getDatasourceData(String param , IUser user){
		
		
		ResponseJson rjson  = new  ResponseJson();
		
		Map paramMap = JsonUtil.readValue(param,Map.class);
		if(!paramMap.containsKey("pkDatasource")){
			throw new BusException("缺少数据源主键！");
		}
		String pkDatasource=(String)paramMap.get("pkDatasource");
		
		//RedisUtils.delCache("bd:datasource");
		BdDatasource bd = RedisUtils.getCacheHashObj("bd:datasource", pkDatasource,BdDatasource.class);
		
		if(bd == null){
			bd = DataBaseHelper.queryForBean("select * from BD_DATASOURCE where PK_DATASOURCE = ? ", 
					BdDatasource.class, paramMap.get("pkDatasource"));
			RedisUtils.setCacheHashObj("bd:datasource", pkDatasource, bd);
		}
		
		/*
		BdDatasource bd = DataBaseHelper.queryForBean("select * from BD_DATASOURCE where PK_DATASOURCE = ? ", 
				BdDatasource.class, paramMap.get("pkDatasource"));
		*/
		
		if(bd == null){
			throw new BusException("数据源无效！");
		}

		DataOption option = new DataOption();
		option.setIsdubbo(true);
		option.setSql(bd.getSource().replaceAll("\r\n", ""));

		String[] fileds = ReflectionUtils.getFiledName(user);
		
		for(String filed : fileds){
			paramMap.put("U"+filed, ReflectionUtils.getFieldValue(user, filed));
		}
		long begin = new Date().getTime();
		rjson = DataSupport.doSQL(option, paramMap);
		//logger.info("执行数据源sql:==========="+option.getSql()+"===========");
		//logger.info("执行数据源sql耗时:==========="+(new Date().getTime()-begin)+"毫秒===========");
		if(rjson.getStatus() < 0){
			throw new BusException(rjson.getDesc());
		}
		
		return rjson;
	}
	
	/**
	 * 保存数据源定义
	 * @param param
	 * @param user
	 * @return
	 */
	public BdDatasource saveDatasource(String param , IUser user){
		
		BdDatasource dsource = JsonUtil.readValue(param, BdDatasource.class);
		if(dsource.getSource()!=null){
			Base64 base64 = new Base64();
			try {
				dsource.setSource(new String(base64.decode(dsource.getSource()), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		if(dsource.getPkDatasource() == null){
			DataBaseHelper.insertBean(dsource);
		}else{
			//清除缓存
			RedisUtils.delCacheHashObj("bd:datasource", dsource.getPkDatasource());

			DataBaseHelper.updateBeanByPk(dsource, false);
		}
		
		return dsource;
	}
	/**
	 * 查询数据源信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDatasource(String param , IUser user){
		Map<String,String> map = JsonUtil.readValue(param, Map.class);
		String sql="select  pk_datasource,name,eu_source_type,config,display_member,value_member,fetch_once,"
				+ "pk_org,creator,create_time,del_flag,descr,eu_mnemonic_code,custom_sort,ts,flag_loaddef,  "
				+ "case eu_source_type when '1' then null else source end as source "
	            + "from bd_datasource where del_flag = '0' ";
		if(map==null||CommonUtils.isEmptyString(map.get("name"))){
			return DataBaseHelper.queryForList(sql);
		}else{
			sql = sql+" and name = ? ";
			return DataBaseHelper.queryForList(sql, new Object[]{map.get("name")});
		}
		
	}

}
