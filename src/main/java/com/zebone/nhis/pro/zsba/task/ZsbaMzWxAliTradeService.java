package com.zebone.nhis.pro.zsba.task;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.up.service.TpPayService;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 门诊重复收费-自动退款
 * 因付款成功，但院内结算失败的
 */
@Service
public class ZsbaMzWxAliTradeService {
	
	private final static Logger logger = LoggerFactory.getLogger(ZsbaMzWxAliTradeService.class);
	
	@Autowired TpPayService wxAliService;

	/**
	 * 执行自动退款
	 * @param cfg
	 */
	public void executeTradeRefund(QrtzJobCfg cfg){  
		String beginTime = "2021-06-10 00:00:00";
		//十分钟前的数据
		String endTime = DateUtils.addDate(new Date(), -10, 5, "yyyy-MM-dd HH:mm:ss");
		try {
			toRefund(beginTime, endTime);
		} catch (Exception e) {
			logger.error("自动退款任务执行异常：{}", e.getMessage());
		}
	}
	
	/**
	 * 进入验证是否进行退款
	 * @param beginTime	开始时间
	 * @param endTime	结束时间
	 */
	private void toRefund(String beginTime, String endTime) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM BL_EXT_PAY WHERE PK_SETTLE IS NULL AND REFUND_NO IS NULL AND SYSNAME='mz' ");
		sql.append(" AND AMOUNT>0 AND EU_PAYTYPE in ('7', '8') AND DT_BANK='0' ");
		sql.append(" AND CREATE_TIME>=? AND CREATE_TIME<=? ");
		sql.append(" ORDER BY ts ASC ");
		List<BlExtPay> extList = DataBaseHelper.queryForList(sql.toString(), BlExtPay.class, new Object[]{beginTime, endTime});
		logger.error("开始自动退款：{}--{}--{}", beginTime, endTime, extList.size());
		
		int i = 0;
		String settSql ="select pk_settle, amount_st from BL_SETTLE where PK_PV=? and AMOUNT_PI=?";
		String countSql = "select pk_extpay,amount from BL_EXT_PAY where PK_PV=? and AMOUNT=?";
		for (BlExtPay blExtPay : extList) {
			//是否存在结算记录
			Map<String, Object> settMap = DataBaseHelper.queryForMap(settSql, blExtPay.getPkPv(), blExtPay.getAmount());
			if(settMap!=null && settMap.containsKey("pkSettle")) {
				List<Map<String, Object>> countList = DataBaseHelper.queryForList(countSql, blExtPay.getPkPv(), blExtPay.getAmount());
				//存在结算记录的要有大于一条付款记录数时才能进行退
				if(countList.size()>1) {
					i += refundTrade(blExtPay);
				}
			}else {
				//没有结算记录的可以直接退
				i += refundTrade(blExtPay);
			}
		}
		logger.error("自动退款结束。执行退款成功条数："+i);
	}
	
	/*
	 * 执行退款
	 */
	private Integer refundTrade(BlExtPay blExtPay) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("refundType", "7".equals(blExtPay.getEuPaytype())?"1":"2");//退款类型，1微信、2支付宝
		queryMap.put("tradeNo", blExtPay.getSerialNo());//商户订单号
		queryMap.put("refundAmount", blExtPay.getAmount().toString());
		queryMap.put("refundReason", "结算失败");
		queryMap.put("pkOrg", "89ace0e12aba439991c0eb001aaf02f7");
		queryMap.put("codeEmp","99998");
		
		Integer num = 0;
		String param = JsonUtil.writeValueAsString(queryMap);
		try {
			Map<String,String> result = wxAliService.payRefund(param, UserContext.getUser());
			if("200".equals(result.get("code"))) {
				num = 1;
			}
			logger.error("自动退款结果：{}--{}--{}", blExtPay.getSerialNo(), result.get("code"), result.get("msg"));
		} catch (Exception e) {
			logger.error("自动退款异常：{}--{}", blExtPay.getSerialNo(), e.getMessage());
		}
		return num;
	}
}
