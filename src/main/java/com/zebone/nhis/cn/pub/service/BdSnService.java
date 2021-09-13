package com.zebone.nhis.cn.pub.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.cn.pub.dao.BdSerialnoMapper;
import com.zebone.nhis.cn.pub.vo.BdSerialno;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class BdSnService {
	
	@Autowired      
	private BdSerialnoMapper bdSnMapper;

	private boolean isInit = false;

	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public int getSerialNo(String tableName, String fieldName, int count, IUser user){
		if(tableName==null) return 0;
//		if(tableName.toUpperCase().equals("CN_ORDER")){
//			String mode=ApplicationUtils.getPropertyValue("cn.ordsn.gen.mode", "nhis");
//			if(mode!=null&&mode.equals("cis")){
//				int orderSn = -1;
//				Map<String, Object> param = new HashMap<String, Object>();
//				param.put("count", 1);
//
//				bdSnMapper.updateCisOrdSn(param);
//
//				orderSn=(int) param.get("orderSn");
//				System.out.println(orderSn);
//				return orderSn;
//			}
//		}
		if(!isInit){ 
			Double sn = bdSnMapper.selectSn(tableName.toUpperCase(), fieldName.toUpperCase()); 
			if(sn==null){
				BdSerialno initSn = new BdSerialno();
				initSn.setPkSerialno(NHISUUID.getKeyId());
				initSn.setPkOrg(CommonUtils.getGlobalOrg());
				initSn.setNameTb(tableName.toUpperCase());
				initSn.setNameFd(fieldName.toUpperCase());
				initSn.setValInit((short)1000);
				initSn.setVal((short)1000);
				bdSnMapper.initSn(initSn);
				isInit = true;
			}
		}
		int ret = ApplicationUtils.getSerialNo(tableName,fieldName,count);
//		int rs = bdSnMapper.updateSn(tableName.toUpperCase(), fieldName.toUpperCase(), count);
//		if(rs==1)
//			ret = bdSnMapper.selectSn(tableName.toUpperCase(), fieldName.toUpperCase()).intValue()-count;
		return ret;
	}
	
	public int getSerialNo(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		return this.getSerialNo(paramMap.get("tableName"), paramMap.get("fieldName"), 
					Integer.parseInt(paramMap.get("count")), user);
	}
	
	public List<String> getApplyCodeList(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String[] codes = ApplicationUtils.getCode(paramMap.get("code"), Integer.parseInt(paramMap.get("count")) );
		return Arrays.asList(codes);
	}
	
	public String getApplyCode(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		return ApplicationUtils.getCode(paramMap.get("code"));
	}
	
	public Map<String,String> getApplyCodeMap(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		Map<String,String> map = new HashMap<String,String>();
		map.put("code",  ApplicationUtils.getCode(paramMap.get("code")));
		return map;
	}
}
