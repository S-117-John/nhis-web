package com.zebone.nhis.common.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnPiPubService {
	
	/**
	 * 根据pk_pi计算患者年龄。
	 * @param pkPi
	 * @return
	 * @throws Exception 
	 */
	public static BigDecimal getPvAge(String pkPi) {
		if(CommonUtils.isEmptyString(pkPi))
			throw new BusException("计算患者年龄时未传入pkPi,请检查！");
		
		BigDecimal  age = new BigDecimal(0);
		PiMaster pi = DataBaseHelper.queryForBean("select  birth_date from pi_master where pk_pi = ?", PiMaster.class, new Object[]{pkPi});
		try {
			age = new BigDecimal(DateUtils.getAge(pi.getBirthDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//日期相减返回天数，转换为年
		//age = age.divide(new BigDecimal(365),2,BigDecimal.ROUND_HALF_UP); //四舍五入保留两位小数
		
		return age;
	}
	
	/**
	 * 根据患者pkpi获取患者年龄
	 * 003001001003
	 * @param param
	 * @param user
	 * @return
	 */
	public BigDecimal getPvAge(String param ,IUser user){
		String pkPi = JsonUtil.getFieldValue(param, "pkPi");
		return getPvAge(pkPi);
	}
	
}
