package com.zebone.nhis.base.bd.service;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.base.bd.vo.AdtConfigVo;
import com.zebone.nhis.common.module.base.bd.wf.BdAdtConfig;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class TransWardSetService {

	/**
	 * 保存  转病区参数
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional
	public void saveAdtConfig(String param, IUser user){
		AdtConfigVo res = JsonUtil.readValue(param, AdtConfigVo.class);
		if(res == null || res.getDtList() == null){
			throw new BusException("未获取到待保存的转病区参数记录！");
		}
		if(res.getDtList().size() > 1){
			for(BdAdtConfig adtConfig : res.getDtList()){
				if(adtConfig.getPkAdtconfig() == null){
					adtConfig.setDtAdtconftype("01");
					DataBaseHelper.insertBean(adtConfig);
				}else{
					DataBaseHelper.updateBeanByPk(adtConfig,false);
				}
			}
		}
		
		if( "" != res.getDelstr()){
			DataBaseHelper.execute("delete from bd_adt_config where pk_adtconfig in ("+res.getDelstr()+") ", new Object[]{});
		}
	}
	
}
