package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbLog;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybLogService {

	/**
	 * 保存
	 * @param param 实体对象数据
	 * @param user  登录用户
	 * @return InsLog 医保业务日志
	 */
	@Transactional
	public InsZsBaYbLog save(String param , IUser user){
		InsZsBaYbLog insLog = JsonUtil.readValue(param, InsZsBaYbLog.class);
		if(insLog!=null){
			if(insLog.getPkInslog()!=null){
				DataBaseHelper.updateBeanByPk(insLog, false);
			}else{
				DataBaseHelper.insertBean(insLog);
			}
		}
		return insLog;
	}
}