package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbBirthCatalogue;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybBirthCatalogueService {
	
	/**
	 * 保存生育目录库下载的资料
	 * @param param
	 * @param user
	 */
	public void saveDownloadData(String param, IUser user){
		try{
			JSONObject jo = JSONObject.fromObject(param);
			String symlklx=jo.getString("symlklx");
			String bcStr = jo.getString("bcList");
			
			List<InsZsBaYbBirthCatalogue> list = JsonUtil.readValue(bcStr, new TypeReference<List<InsZsBaYbBirthCatalogue>>(){});
			
			DataBaseHelper.execute("delete ins_birth_catalogue  where symlklx = ?", symlklx);
			
			for(int i=0; i<list.size(); i++){
				DataBaseHelper.insertBean(list.get(i));
			}
		}catch(Exception e){
			throw new BusException(e.getMessage());
		}
	}
}
