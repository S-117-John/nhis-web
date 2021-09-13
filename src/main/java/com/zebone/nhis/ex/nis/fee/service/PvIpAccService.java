package com.zebone.nhis.ex.nis.fee.service;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.ex.nis.fee.PvIpAcc;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 担保信息
 * @author yangxue
 *
 */
@Service
public class PvIpAccService {

	/**
	 * 保存和更新担保信息
	 * @param param
	 */
	public PvIpAcc savePvIpAcc(String param,IUser user){
		
		PvIpAcc acc = JsonUtil.readValue(param,PvIpAcc.class);
		
		if(acc.getPkIpacc() == null){
				DataBaseHelper.insertBean(acc);
		}else{
				DataBaseHelper.updateBeanByPk(acc,false);
		}
		return acc;
	}
	
	
	
}
