package com.zebone.nhis.pro.zsba.compay.ins.pub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubCertOut;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubLog;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsPubLogService {

	/**
	 * 保存医保操作日志
	 * @param param
	 * @param user
	 */
	@Transactional
	public void saveInsCertOut(String param , IUser user) {
		InsZsPubLog insLog = JsonUtil.readValue(param, InsZsPubLog.class);
		DataBaseHelper.insertBean(insLog);
	}
}
