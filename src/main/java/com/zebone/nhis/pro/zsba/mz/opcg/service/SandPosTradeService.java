package com.zebone.nhis.pro.zsba.mz.opcg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 窗口银联POS机退款业务
 * @author lipz
 * @date 2021-06-09
 */
@Service
public class SandPosTradeService {

	/**
	 * 获取操作员的pos机失败的交易数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPosTradeFail(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT ");
		sql.append(" pi.NAME_PI, pi.CODE_OP,  ");
		sql.append(" ext.TRADE_NO, ext.SERIAL_NO,ext.CARD_NO, ext.AMOUNT, ");
		sql.append(" CONVERT(VARCHAR(19), ext.CREATE_TIME, 120) CREATE_TIME, ext.FLAG_PAY, ext.DATE_PAY, ");
		sql.append(" emp.CODE_EMP,emp.NAME_EMP ");
		sql.append(" FROM ");
		sql.append(" BL_EXT_PAY ext ");
		sql.append(" INNER JOIN PI_MASTER pi on pi.PK_PI = ext.PK_PI ");
		sql.append(" INNER JOIN BD_OU_EMPLOYEE emp on emp.PK_EMP = ext.CREATOR ");
		sql.append(" WHERE ");
		sql.append(" ext.DEL_FLAG='0' ");
		
		if(StringUtils.isNotEmpty(paramMap.get("serialNo").toString())) {
			sql.append(" and ext.SERIAL_NO='"+paramMap.get("serialNo").toString()+"' ");
		}
		if(StringUtils.isNotEmpty(paramMap.get("flagPay").toString())) {
			sql.append(" and ext.FLAG_PAY='"+paramMap.get("flagPay").toString()+"' ");
		}
		if(StringUtils.isNotEmpty(paramMap.get("codeEmp").toString())) {
			sql.append(" and emp.CODE_EMP='"+paramMap.get("codeEmp").toString()+"' ");
		}
		if(StringUtils.isNotEmpty(paramMap.get("beginDate").toString())) {
			sql.append(" and ext.CREATE_TIME>='"+paramMap.get("beginDate").toString()+" 00:00:00' ");
		}
		if(StringUtils.isNotEmpty(paramMap.get("endDate").toString())) {
			sql.append(" and ext.CREATE_TIME<='"+paramMap.get("endDate").toString()+" 23:59:59' ");
		}
		sql.append(" ORDER BY ext.CREATE_TIME DESC ");
		
		List<Map<String, Object>> result = DataBaseHelper.queryForList(sql.toString());
		
		return result;
	}
	
	/**
	 * 保存操作员手工退款交易数据 : 022003027145
	 * @param param
	 * @param user
	 * @return
	 */
	public Integer savePosRefundTrade(String params, IUser user){
		BlExtPay extPay = JsonUtil.readValue(params, BlExtPay.class);
		if(extPay == null) {
			throw new BusException("交易数据对象不能为空！");
		}
		if(StringUtils.isEmpty(extPay.getSerialNo())) {
			throw new BusException("交易退款订单号不能为空！");
		}
		if(StringUtils.isEmpty(extPay.getRefundNo())) {
			throw new BusException("原消费订单号不能为空！");
		}
		String sql = "select * from BL_EXT_PAY where SERIAL_NO=?";
		BlExtPay orgExtPay = DataBaseHelper.queryForBean(sql, BlExtPay.class, extPay.getRefundNo());
		if(orgExtPay == null) {
			throw new BusException("原交易订单不存在！");
		}
		String updateOrgSql = " update BL_EXT_PAY set FLAG_PAY='1' where PK_EXTPAY=? ";
		DataBaseHelper.execute(updateOrgSql, orgExtPay.getPkExtpay());
				
		extPay.setSysname("mz");
		extPay.setFlagPay("1");//支付标志 完成
		extPay.setDateAp(new Date());//请求时间
		extPay.setDatePay(new Date());
		extPay.setPkPi(orgExtPay.getPkPi());//患者主键
		extPay.setPkPv(orgExtPay.getPkPv());//就诊主键
		ApplicationUtils.setDefaultValue(extPay, true);
		DataBaseHelper.insertBean(extPay);
		
		// 更新业务类型为：手工操作退款
		String updateSql = " update BL_EXT_PAY set BUS_TYPE='pos_refund' where PK_EXTPAY=? ";
		return DataBaseHelper.execute(updateSql, extPay.getPkExtpay());
	}
	
	/**
	 * 查询POS机手工退款记录 ： 022003027144
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPosRefundData(String params, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT ");
		sql.append(" pi.NAME_PI, pi.CODE_OP, ");
		sql.append(" ext.TRADE_NO, ext.SERIAL_NO,ext.CARD_NO, ext.AMOUNT, ");
		sql.append(" CONVERT(VARCHAR(19), ext.CREATE_TIME, 120) CREATE_TIME, ");
		sql.append(" ext.FLAG_PAY, CONVERT(VARCHAR(19), ext.DATE_PAY, 120) DATE_PAY, ");
		sql.append(" emp.CODE_EMP,emp.NAME_EMP ");
		sql.append(" FROM ");
		sql.append(" BL_EXT_PAY ext ");
		sql.append(" INNER JOIN PI_MASTER pi on pi.PK_PI = ext.PK_PI ");
		sql.append(" INNER JOIN BD_OU_EMPLOYEE emp on emp.PK_EMP = ext.CREATOR ");
		sql.append(" WHERE ");
		sql.append(" ext.DEL_FLAG='0' ");
		sql.append(" and ext.BUS_TYPE='pos_refund' ");//手工退款
		sql.append(" and ext.FLAG_PAY='1' ");//退款成功

		if(paramMap.containsKey("pkEmp") && StringUtils.isNotEmpty(paramMap.get("pkEmp").toString())) {
			sql.append(" and emp.PK_EMP='"+paramMap.get("pkEmp").toString()+"' ");
		}
		if(paramMap.containsKey("beginDate") && StringUtils.isNotEmpty(paramMap.get("beginDate").toString())) {
			sql.append(" and ext.CREATE_TIME>='"+paramMap.get("beginDate").toString()+" 00:00:00' ");
		}
		if(paramMap.containsKey("endDate") && StringUtils.isNotEmpty(paramMap.get("endDate").toString())) {
			sql.append(" and ext.CREATE_TIME<='"+paramMap.get("endDate").toString()+" 23:59:59' ");
		}
		sql.append(" ORDER BY ext.CREATE_TIME DESC ");
		
		List<Map<String, Object>> result = DataBaseHelper.queryForList(sql.toString());
		
		return result;
	}
	
	
}
