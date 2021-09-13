package com.zebone.nhis.base.bd.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.InterfaceExtMapper;
import com.zebone.nhis.common.module.base.bd.wf.BdInterfaceExt;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InterfaceExtService {
	
	@Resource
	private InterfaceExtMapper interfaceExtMapper;
	
	/**
	 * 查询接口
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdInterfaceExt> qryInf(String param,IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		return interfaceExtMapper.qryInf(map);
	}
	
	/**
	 * 保存接口
	 * @param param
	 * @param user
	 */
	public void saveInf(String param,IUser user) {
		BdInterfaceExt bdInterfaceExt = JsonUtil.readValue(param, BdInterfaceExt.class);
		
		if (bdInterfaceExt==null)return;
		String pkInterface = bdInterfaceExt.getPkInterface();
		
		if(StringUtils.isNotBlank(pkInterface)){
			DataBaseHelper.updateBeanByPk(bdInterfaceExt,false);
		}else{
			DataBaseHelper.insertBean(bdInterfaceExt);
			
		}
	}
	
	/**
	 * 删除接口
	 * @param param
	 * @param user
	 */
	public void delInf(String param,IUser user) {
		String[] pkInterfaces = JsonUtil.readValue(param, String[].class);
		if (pkInterfaces==null||pkInterfaces.length<0)return;
		for (String pkInterface : pkInterfaces) {
			interfaceExtMapper.delInf(pkInterface);
		}
	}
}
