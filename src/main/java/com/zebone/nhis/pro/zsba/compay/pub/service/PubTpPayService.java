package com.zebone.nhis.pro.zsba.compay.pub.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliBill;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayBillBackage;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatBill;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PubTpPayService {

	
	public String createPay(BlExtPay blExtPay){
		DataBaseHelper.insertBean(blExtPay);
		return blExtPay.getPkExtpay();
	}
	
	public int updatePay(BlExtPay blExtPay){
		return DataBaseHelper.updateBeanByPk(blExtPay, false);
	}
	
	public String createWechatPay(PayWechatBill tpPayWechatBill){
		DataBaseHelper.insertBean(tpPayWechatBill);
		return tpPayWechatBill.getPkPayWechatBill();
	}
	
	public int updateWechatPay(PayWechatBill tpPayWechatBill){
		return DataBaseHelper.updateBeanByPk(tpPayWechatBill, false);
	}
	
	public String createAliPay(PayAliBill tpPayAliBill){
		DataBaseHelper.insertBean(tpPayAliBill);
		return tpPayAliBill.getPkPayAliBill();
	}
	
	public int updateAliPay(PayAliBill tpPayAliBill){
		return DataBaseHelper.updateBeanByPk(tpPayAliBill, false);
	}
	
	public String createBackage(PayBillBackage tpPayBillBackage){
		DataBaseHelper.insertBean(tpPayBillBackage);
		return tpPayBillBackage.getPkWBillBackage();
	}
	
	public int updateBackage(PayBillBackage tpPayBillBackage){
		return DataBaseHelper.updateBeanByPk(tpPayBillBackage, false);
	}
	
	public BlExtPay savePay(String param, IUser user){
		BlExtPay blExtPay = JsonUtil.readValue(param, BlExtPay.class);
		if(StringUtils.isEmpty(blExtPay.getPkExtpay())){
			throw new BusException("支付主键不能为空！");
		}
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay;
	}
}