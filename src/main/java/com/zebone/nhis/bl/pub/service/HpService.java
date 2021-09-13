package com.zebone.nhis.bl.pub.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.platform.framework.support.IUser;

/**
 * 医保服务
 * @author Roger
 *
 */
@Service
public class HpService {
	
	/**
	 * 医保费用明细上传
	 * @param param
	 * @param user
	 */
	public void uploadItems(String param, IUser user) {
		
	}
	
	
	/**
	 * 医保预结算 各种医保调用过后返回患者的自付金额
	 * @param param
	 * @param user
	 */
	public Map<String,Object> preSettleHp(String param, IUser user) {
		return getHpInfo(null,null);
	}
	
	public Map<String,Object> getHpInfo(Double price,Map<String,Object> HpInfo) {
		Map<String,Object> res =  new HashMap<String,Object>();
		//TODO 第三方医保处理过程
		res.put("amtHps", BigDecimal.ZERO);
		return res;
	}
	
	

}
