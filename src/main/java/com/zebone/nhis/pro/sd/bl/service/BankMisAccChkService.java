package com.zebone.nhis.pro.sd.bl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.sd.bl.dao.BankMisAccChkMapper;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class BankMisAccChkService {
	@Autowired
	private BankMisAccChkMapper bankMisAccChkMapper;
	
	/**
	 * 交易号：007002003051
	 * 查询门诊第三方收退款记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOpDepoInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		return bankMisAccChkMapper.qryOpDepoInfo(paramMap);
	}
	
	/**
	 * 交易号：007002003056
	 * 查询第三方订单信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryExtPayInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		Map<String,Object> payInfo = new HashMap<String, Object>(16);
		
		if(paramMap!=null && paramMap.size()>0){
			//根据发票号查询第三方订单信息
			if(paramMap.containsKey("codeInv")
					&& !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("codeInv")))){
				payInfo = bankMisAccChkMapper.qryExtInfoByCodeInv(paramMap);
			}else if(paramMap.containsKey("descPay")//根据流水号、票据号、授权号、日期查询订单信息
					&& !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("descPay")))){
				
				payInfo = DataBaseHelper.queryForMap(
						"select * from bl_ext_pay where replace(DESC_PAY,' ','')=? and flag_pay = '1' and REFUND_NO is null and amount>0",
						new Object[]{paramMap.get("descPay")});
				
				//根据流水号、票据号、授权号、日期没有查出信息时再根据订单号查询
				if((payInfo==null || payInfo.size()<=0)
						&& paramMap.containsKey("tradeNo")
						&& !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("tradeNo")))){
					List<Map<String,Object>> payList =  DataBaseHelper.queryForList(
							"select * from bl_ext_pay where flag_pay = '1' and trade_no = ? and REFUND_NO is null and amount>0 order by date_pay asc",
							new Object[]{paramMap.get("tradeNo")});
					
					if(payList!=null && payList.size()>0){
						payInfo = payList.get(0);
					}
					
				}
			}else if(paramMap.containsKey("serialNo")
					&& !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("serialNo")))){//根据订单号查询订单信息
				List<Map<String, Object>> payList = DataBaseHelper.queryForList(
						"select * from bl_ext_pay where flag_pay = '0' and REFUND_NO=? order by date_ap desc",
						new Object[]{paramMap.get("serialNo")});
				
				if(payList!=null && payList.size()>0){
					payInfo = payList.get(0);
				}
			}
		}
		
		return payInfo;
		
	}
	
}
