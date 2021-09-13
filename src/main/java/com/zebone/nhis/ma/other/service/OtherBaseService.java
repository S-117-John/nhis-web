package com.zebone.nhis.ma.other.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.LoadDataSource;
import com.zebone.platform.modules.dao.jdbc.entity.DataBaseEntity;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.FileUtils;
import com.zebone.platform.modules.utils.JsonUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 常用
 * @author think
 *
 */
@Service
public class OtherBaseService {
	
	 private Logger logger = LoggerFactory.getLogger("com.zebone");

	
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> getDatas(String param , IUser user){
		
		Map map = JsonUtil.readValue(param, Map.class);
		
		if(map.containsKey("data")){
			
			
		     String sql =  CommonUtils.decode(map.get("data").toString().getBytes());
		     //去除在报表中写的分号
		     if(!CommonUtils.isEmptyString(sql)){
		    	 sql = sql.replaceAll(";", "");
		     }
		     return DataBaseHelper.queryForList(sql);
			
		}else{
			throw new BusException("缺少data参数");
		}
		
	}
	
	
	/**
	 * 根据单号生成规则，获取单号
	 * @param param：规则编码
	 * @param user
	 * @return
	 */
	public String getAppCode(String param, IUser user){
		String code = JsonUtil.readValue(param, String.class);
		String codeip = ApplicationUtils.getCode(code);
		if("0203".equals(code)){
			logger.info("==========前台调用获取住院号:"+codeip+"=========");
		}
		return codeip;
	}
	
	/**
	 * 获取数据源名称
	 * @param param
	 * @param user
	 * @return
	 */
	public List<String> getDSNameList(String param, IUser user){
		List<String> listName = new ArrayList();
		try {
			String path = LoadDataSource.class.getClassLoader().getResource("config/datasource.xml").getFile().toString();
			File file = new File(path.replace("%20", " "));
			Document doc = FileUtils.getDocument(file);
			Element rootElement = doc.getRootElement();
			
			if(rootElement != null){
				List<Element> list = rootElement.elements("datasource");
				
				for(int i=0;i<list.size();i++){
					DataBaseEntity dataBaseEntity = new DataBaseEntity();
					Element datasources = list.get(i);
					if(datasources != null){
						String dsName = datasources.attributeValue("name");
						listName.add(dsName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listName;
	}
}
