package com.zebone.nhis.pro.zsba.compay.up.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliBill;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayUnionpayRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatBill;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 三方对账查询与处理
 * @author Administrator
 *
 */
@Service
public class PayBillService {
	
	@Autowired TpPayService payService;
	
	/**
	 * 获取对账数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPayBillData(String param, IUser user){
		List<Map<String,Object>> returnMap = new ArrayList<Map<String,Object>>();
		
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String beginDate = paramMap.get("dateBegin");
		String endDate = paramMap.get("endDate");
		String orderType = paramMap.get("orderType");	// 订单类型，0微信、1支付宝、2银联
		
		if(StringUtils.isNotEmpty(orderType)){
			if("0".equals(orderType)){
				returnMap = getWxBillData(beginDate, endDate);
			}else if("1".equals(orderType)){
				returnMap = getAliBillData(beginDate, endDate);
			}else{
				returnMap = getUnionBillData(beginDate, endDate);
			}
		}else{
			throw new BusException("参数错误，orderType不能为空！");
		}
		return returnMap;
	}
	
	
	private List<Map<String,Object>> getWxBillData(String beginDate, String endDate){
		StringBuffer sql = new StringBuffer("SELECT ");
		sql.append(" CONVERT(varchar(16), b.TIME_END, 20) as BILL_TIME_END, ");
		sql.append(" case b.TRADE_STATE when 'SUCCESS' then b.OUT_TRADE_NO else b.OUT_REFUND_NO end as BILL_TRADE_NO, ");
		sql.append(" case b.TRADE_STATE when 'SUCCESS' then b.TOTAL_FEE else b.REFUND_FEE end as BILL_AMOUNT, ");
		
		sql.append(" case r.BILL_STATUS when '0' then '未对账' when '1' then '对账成功' when '2' then '金额不一致' when '3' then '单边账' end as R_BILL_STATUS, ");
		sql.append(" case r.ORDER_TYPE when 'pay' then '消费' else '退款' end as R_ORDER_TYPE, ");
		sql.append(" case r.TRADE_STATE when 'SUCCESS' then '交易成功' when 'FAIL' then '交易失败' when 'INIT' then '未付款' end as R_TRADE_STATE, ");
		sql.append(" CONVERT(varchar(16), r.CREATE_TIME, 20) as R_CREATE_TIME, r.OUT_TRADE_NO as R_TRADE_NO, r.TOTAL_AMOUNT as R_AMOUNT,  ");
		sql.append(" case when p.pk_depo is NULL then '单边账' else (case r.NHIS_BILL_STATUS when '0' then '未对账' when '1' then '对账成功' when '2' then '金额不一致' when '3' then '单边账' end) end as R_NHIS_STATUS, ");
		
		sql.append(" CONVERT(varchar(16), d.DATE_PAY, 20) as D_DATE_PAY, d.PK_DEPO as D_PK_DEPO, d.AMOUNT as D_AMOUNT, d.name_emp_pay as D_NAME_EMP, ");
		sql.append(" case d.FLAG_SETTLE when '1' then '已结算' when '0' then '未结算' else '' end as D_FLAG_SETTLE ");

		sql.append(" FROM ");
		sql.append(" PAY_WECHAT_RECORD r ");
		sql.append(" LEFT JOIN PAY_WECHAT_BILL b on ((b.OUT_TRADE_NO = r.OUT_TRADE_NO and b.TRADE_STATE='SUCCESS') or (b.OUT_REFUND_NO = r.OUT_TRADE_NO and b.TRADE_STATE='REFUND')) ");
		sql.append(" LEFT JOIN BL_EXT_PAY p on p.PK_EXTPAY = r.PK_EXTPAY ");
		sql.append(" LEFT JOIN BL_DEPOSIT d on d.PK_DEPO = p.PK_DEPO ");
		sql.append(" WHERE ");
		sql.append(" r.DEL_FLAG is null and r.SYSTEM_MODULE is null ");
		
		if(StringUtils.isNotEmpty(beginDate)){
			sql.append(" and r.CREATE_TIME>='"+beginDate+" 00:00:00' ");
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and r.CREATE_TIME<='"+endDate+" 23:59:59' ");
		}
		
		sql.append(" ORDER BY r.CREATE_TIME,r.pk_pv asc ");
				
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}
	
	private List<Map<String,Object>> getAliBillData(String beginDate, String endDate){
		StringBuffer sql = new StringBuffer("SELECT ");
		sql.append(" CONVERT(varchar(16), b.FINISH_TIME, 20) as BILL_TIME_END,  ");
		sql.append(" case b.SERVICE_TYPE when '交易' then b.ORDER_NO else b.REFUND_NO end as BILL_TRADE_NO,  ");
		sql.append(" b.ORDER_MONEY as BILL_AMOUNT, ");
		
		sql.append(" case r.BILL_STATUS when '0' then '未对账' when '1' then '对账成功' when '2' then '金额不一致' when '3' then '单边账' end as R_BILL_STATUS, ");
		sql.append(" case r.ORDER_TYPE when 'pay' then '消费' else '退款' end as R_ORDER_TYPE, ");
		sql.append(" case r.TRADE_STATE when 'SUCCESS' then '交易成功' when 'FAIL' then '交易失败' when 'INIT' then '未付款' end as R_TRADE_STATE, ");
		sql.append(" CONVERT(varchar(16), r.CREATE_TIME, 20) as R_CREATE_TIME, r.OUT_TRADE_NO as R_TRADE_NO, r.TOTAL_AMOUNT as R_AMOUNT,  ");
		sql.append(" case when p.pk_depo is NULL then '单边账' else (case r.NHIS_BILL_STATUS when '0' then '未对账' when '1' then '对账成功' when '2' then '金额不一致' when '3' then '单边账' end) end as R_NHIS_STATUS, ");
		
		sql.append(" CONVERT(varchar(16), d.DATE_PAY, 20) as D_DATE_PAY, d.PK_DEPO as D_PK_DEPO, d.AMOUNT as D_AMOUNT, d.name_emp_pay as D_NAME_EMP, ");
		sql.append(" case d.FLAG_SETTLE when '1' then '已结算' when '0' then '未结算' else '' end as D_FLAG_SETTLE ");

		sql.append(" FROM ");
		sql.append(" PAY_ALI_RECORD r ");
		sql.append(" LEFT JOIN PAY_ALI_BILL b on ((b.ORDER_NO = r.OUT_TRADE_NO and b.SERVICE_TYPE='交易') or (b.REFUND_NO = r.OUT_TRADE_NO and b.SERVICE_TYPE='退款')) ");
		sql.append(" LEFT JOIN BL_EXT_PAY p on p.PK_EXTPAY = r.PK_EXTPAY ");
		sql.append(" LEFT JOIN BL_DEPOSIT d on d.PK_DEPO = p.PK_DEPO ");
		sql.append(" WHERE ");
		sql.append(" r.DEL_FLAG is null and r.SYSTEM_MODULE is null ");
		
		if(StringUtils.isNotEmpty(beginDate)){
			sql.append(" and r.CREATE_TIME>='"+beginDate+" 00:00:00' ");
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and r.CREATE_TIME<='"+endDate+" 23:59:59' ");
		}
		
		sql.append(" ORDER BY r.CREATE_TIME,r.pk_pv asc ");
				
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}
	
	private List<Map<String,Object>> getUnionBillData(String beginDate, String endDate){
		StringBuffer sql = new StringBuffer("SELECT ");
		sql.append(" b.TRANS_DATE +''+ b.TRANS_TIME as BILL_TIME_END, ");
		sql.append(" b.MCH_NO, bm.NAME as MCH_NAME, b.SYS_REF_NO as BILL_TRADE_NO, ");
		sql.append(" b.TRANS_AMOUNT as BILL_AMOUNT, ");
		
		sql.append(" case r.BILL_STATUS when '0' then '未对账' when '1' then '对账成功' when '2' then '金额不一致' when '3' then '单边账' end as R_BILL_STATUS, ");
		sql.append(" case r.TRANSTYPE when '31' then '消费' when '41' then '撤销' else '退款' end as R_ORDER_TYPE, ");
		sql.append(" case r.TRADE_STATE when 'SUCCESS' then '交易成功' when 'FAIL' then '交易失败' when 'INIT' then '未付款' end as R_TRADE_STATE, ");
		sql.append(" CONVERT(varchar(16), r.CREATE_TIME, 20) as R_CREATE_TIME, r.SYSREFNO as R_TRADE_NO, r.AMOUNT as R_AMOUNT, ");
		sql.append(" case when p.pk_depo is NULL then '单边账' else (case r.NHIS_BILL_STATUS when '0' then '未对账' when '1' then '对账成功' when '2' then '金额不一致' when '3' then '单边账' end) end as R_NHIS_STATUS, ");
		
		sql.append(" CONVERT(varchar(16), d.DATE_PAY, 20) as D_DATE_PAY, d.PK_DEPO as D_PK_DEPO, d.AMOUNT as D_AMOUNT, d.name_emp_pay as D_NAME_EMP, ");
		sql.append(" case d.FLAG_SETTLE when '1' then '已结算' when '0' then '未结算' else '' end as D_FLAG_SETTLE ");

		sql.append(" FROM ");
		sql.append(" PAY_UNIONPAY_RECORD r ");
		sql.append(" LEFT JOIN PAY_UNIONPAY_BILL b on b.SYS_REF_NO = r.SYSREFNO ");
		sql.append(" LEFT JOIN bd_defdoc bm on bm.code = b.MCH_NO and bm.CODE_DEFDOCLIST='BA001' ");
		sql.append(" LEFT JOIN BL_EXT_PAY p on p.PK_EXTPAY = r.PK_EXTPAY ");
		sql.append(" LEFT JOIN BL_DEPOSIT d on d.PK_DEPO = p.PK_DEPO ");
		sql.append(" WHERE ");
		sql.append(" r.DEL_FLAG is null and bm.DEL_FLAG='0' ");
		
		if(StringUtils.isNotEmpty(beginDate)){
			sql.append(" and r.CREATE_TIME>='"+beginDate+" 00:00:00' ");
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and r.CREATE_TIME<='"+endDate+" 23:59:59' ");
		}
		
		sql.append(" ORDER BY r.CREATE_TIME,r.pk_pv asc ");
				
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}
	
	
	/**
	 * 处理对账单数据
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public Boolean handleBillData(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String handleType = paramMap.get("handleType");	// 处理类型，1删除、2退款、3同步状态
		String orderType = paramMap.get("orderType");	// 订单类型，0微信、1支付宝、2银联
		String rTradeNo = paramMap.get("rTradeNo");	// 商户订单号
		
		Boolean result = false;
		if(StringUtils.isNotEmpty(handleType) && StringUtils.isNotEmpty(orderType) && StringUtils.isNotEmpty(rTradeNo)){
			if("0".equals(orderType)){
				result = handleWx(handleType, rTradeNo, user);
			}else if("1".equals(orderType)){
				result = handleAli(handleType, rTradeNo, user);
			}else{
				result = handleUnion(handleType, rTradeNo, user);
			}
		}else{
			throw new BusException("参数错误，handleType、orderType、rTradeNo不能为空！");
		}
		return result;
	}
	
	/**
	 *  处理类型，1删除、2退款、3同步状态
	 * @param handleType
	 * @param rTradeNo
	 * @param user
	 */
	private Boolean handleWx(String handleType, String rTradeNo, IUser user){
		Boolean result = false;
		String wrSql = " select * from PAY_WECHAT_RECORD where OUT_TRADE_NO=? ";
		PayWechatRecord wr = DataBaseHelper.queryForBean(wrSql, PayWechatRecord.class, rTradeNo);
		//删除
		if("1".equals(handleType)){
			if(wr!=null && !"SUCCESS".equals(wr.getTradeState())){
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, wr.getPkExtpay());
				if(ext!=null && "0".equals(ext.getFlagPay())){
					ext.setDelFlag("1");
					DataBaseHelper.updateBeanByPk(ext);
				}
				wr.setDelFlag("1");
				DataBaseHelper.updateBeanByPk(wr);
				result = true;
			}
		}
		//退款
		if("2".equals(handleType)){
			Boolean flag = false;
			if(wr!=null){
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, wr.getPkExtpay());
				if(ext==null || StringUtils.isEmpty(ext.getPkDepo())){
					flag = true;
				}
			}
			if(flag){
				if("SUCCESS".equals(wr.getTradeState())){
					try {
						SortedMap<Object, Object> params = new TreeMap<Object, Object>();
						params.put("refundType", "1");//微信
						params.put("tradeNo", rTradeNo);//原商户订单号
						params.put("refundAmount",  wr.getTotalAmount().toString());//退款金额
						params.put("refundReason", "不存在预交金的记录，做平账原路退费处理");//退款原因
						
						Map<String,String> returnMap = payService.payRefund(JsonUtil.writeValueAsString(params), user);
						if(returnMap!=null && "200".equals(returnMap.get("code"))){
							wr.setDelFlag("1");
							DataBaseHelper.updateBeanByPk(wr);
							
							String wrRefundSql = " select * from PAY_WECHAT_RECORD where REFUND_TRADE_NO=? ";
							PayWechatRecord wrRefund = DataBaseHelper.queryForBean(wrRefundSql, PayWechatRecord.class, rTradeNo);
							if(wrRefund!=null){
								wrRefund.setDelFlag("0");
								DataBaseHelper.updateBeanByPk(wrRefund);
							}
							
							result = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		//同步状态
		if("3".equals(handleType)){
			if(wr!=null){
				PayWechatBill wb = null;
				if("pay".equals(wr.getOrderType())){
					String billSql = " select * from PAY_WECHAT_BILL where OUT_TRADE_NO=? and TRADE_STATE='SUCCESS' ";
					wb = DataBaseHelper.queryForBean(billSql, PayWechatBill.class, wr.getOutTradeNo());
				}else{
					String billSql = " select * from PAY_WECHAT_BILL where OUT_REFUND_NO=? and TRADE_STATE='REFUND' ";
					wb = DataBaseHelper.queryForBean(billSql, PayWechatBill.class, wr.getOutTradeNo());
				}
				if(wb!=null){
					wr.setTradeState("SUCCESS");
					if(wr.getTotalAmount().equals(wb.getTotalFee()) || wr.getTotalAmount().equals(wb.getRefundFee())){
						wr.setBillStatus("1");
						wr.setBillDesc("对账成功");
						
						wb.setBillStatus("1");
						wb.setBillDesc("对账成功");
					}else{
						wr.setBillStatus("2");
						wr.setBillDesc("金额不一致");
						
						wb.setBillStatus("2");
						wb.setBillDesc("金额不一致");
					}
					DataBaseHelper.updateBeanByPk(wr);
					DataBaseHelper.updateBeanByPk(wb);
				}else{
					wr.setBillStatus("3");
					wr.setBillDesc("单边账");
					DataBaseHelper.updateBeanByPk(wr);
				}
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, wr.getPkExtpay());
				if(ext!=null){
					if("SUCCESS".equals(wr.getTradeState())){
						ext.setFlagPay("1");
					}else{
						ext.setFlagPay("0");
					}
					wr.setNhisBillStatus("1");
					wr.setBillDesc("对账成功");
					DataBaseHelper.updateBeanByPk(wr);
					
					ext.setEuBill("1");
					DataBaseHelper.updateBeanByPk(ext);
				}else{
					wr.setNhisBillStatus("3");
					wr.setNhisBillDesc("单边账");
					DataBaseHelper.updateBeanByPk(wr);
				}
				result = true;
			}
		}
		return result;
	}
	
	private Boolean handleAli(String handleType, String rTradeNo, IUser user){
		Boolean result = false;
		String wrSql = " select * from PAY_ALI_RECORD where OUT_TRADE_NO=? ";
		PayAliRecord ar = DataBaseHelper.queryForBean(wrSql, PayAliRecord.class, rTradeNo);
		if("1".equals(handleType)){
			if(ar!=null && !"SUCCESS".equals(ar.getTradeState())){
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, ar.getPkExtpay());
				if(ext!=null && "0".equals(ext.getFlagPay())){
					ext.setDelFlag("1");
					DataBaseHelper.updateBeanByPk(ext);
				}
				ar.setDelFlag("1");
				DataBaseHelper.updateBeanByPk(ar);
				result = true;
			}
		}
		
		if("2".equals(handleType)){
			Boolean flag = false;
			if(ar!=null){
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, ar.getPkExtpay());
				if(ext==null || StringUtils.isEmpty(ext.getPkDepo())){
					flag = true;
				}
			}
			if(flag){
				if("SUCCESS".equals(ar.getTradeState())){
					try {
						SortedMap<Object, Object> params = new TreeMap<Object, Object>();
						params.put("refundType", "2");//支付宝
						params.put("tradeNo", rTradeNo);//原商户订单号
						params.put("refundAmount",  ar.getTotalAmount().toString());//退款金额
						params.put("refundReason", "不存在预交金的记录，做平账原路退费处理");//退款原因
						
						Map<String,String> returnMap = payService.payRefund(JsonUtil.writeValueAsString(params), user);
						if(returnMap!=null && "200".equals(returnMap.get("code"))){
							ar.setDelFlag("1");
							DataBaseHelper.updateBeanByPk(ar);
							
							String arRefundSql = " select * from PAY_ALI_RECORD where REFUND_TRADE_NO=? ";
							PayAliRecord arRefund = DataBaseHelper.queryForBean(arRefundSql, PayAliRecord.class, rTradeNo);
							if(arRefund!=null){
								arRefund.setDelFlag("0");
								DataBaseHelper.updateBeanByPk(arRefund);
							}
							
							result = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if("3".equals(handleType)){
			if(ar!=null){
				PayAliBill ab = null;
				if("pay".equals(ar.getOrderType())){
					String billSql = " select * from PAY_ALI_BILL where OUT_TRADE_NO=? and TRADE_STATE='SUCCESS' ";
					ab = DataBaseHelper.queryForBean(billSql, PayAliBill.class, ar.getOutTradeNo());
				}else{
					String billSql = " select * from PAY_ALI_BILL where OUT_REFUND_NO=? and TRADE_STATE='REFUND' ";
					ab = DataBaseHelper.queryForBean(billSql, PayAliBill.class, ar.getOutTradeNo());
				}
				if(ab!=null){
					ar.setTradeState("SUCCESS");
					if(ar.getTotalAmount().equals(ab.getOrderMoney())){
						ar.setBillStatus("1");
						ar.setBillDesc("对账成功");
						
						ab.setBillStatus("1");
						ab.setBillDesc("对账成功");
					}else{
						ar.setBillStatus("2");
						ar.setBillDesc("金额不一致");
						
						ab.setBillStatus("2");
						ab.setBillDesc("金额不一致");
					}
					DataBaseHelper.updateBeanByPk(ar);
					DataBaseHelper.updateBeanByPk(ab);
				}else{
					ar.setBillStatus("3");
					ar.setBillDesc("单边账");
					DataBaseHelper.updateBeanByPk(ar);
				}
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, ar.getPkExtpay());
				if(ext!=null){
					if("SUCCESS".equals(ar.getTradeState())){
						ext.setFlagPay("1");
					}else{
						ext.setFlagPay("0");
					}
					ar.setNhisBillStatus("1");
					ar.setBillDesc("对账成功");
					DataBaseHelper.updateBeanByPk(ar);
					
					ext.setEuBill("1");
					DataBaseHelper.updateBeanByPk(ext);
				}else{
					ar.setNhisBillStatus("3");
					ar.setNhisBillDesc("单边账");
					DataBaseHelper.updateBeanByPk(ar);
				}
				result = true;
			}
		}
		return result;
	}

	private Boolean handleUnion(String handleType, String rTradeNo, IUser user){
		Boolean result = false;
		String urSql = " select * from PAY_UNIONPAY_RECORD where SYSREFNO=? ";
		PayUnionpayRecord ur = DataBaseHelper.queryForBean(urSql, PayUnionpayRecord.class, rTradeNo);
		if("1".equals(handleType)){
			if(ur!=null && !"SUCCESS".equals(ur.getTradeState())){
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, ur.getPkExtpay());
				if(ext!=null && "0".equals(ext.getFlagPay())){
					ext.setDelFlag("1");
					DataBaseHelper.updateBeanByPk(ext);
				}
				ur.setDelFlag("1");
				DataBaseHelper.updateBeanByPk(ur);
				result = true;
			}
		}
		
		if("2".equals(handleType)){
			Boolean flag = false;
			if(ur!=null){
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, ur.getPkExtpay());
				if(ext==null || StringUtils.isEmpty(ext.getPkDepo())){
					flag = true;
				}
			}
			if(flag){
				if("SUCCESS".equals(ur.getTradeState())){
					try {
						//TODO:怎么退呢？
						
						//result = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if("3".equals(handleType)){
			if(ur!=null){
				String urBillSql = " select * from PAY_UNIONPAY_BILL where SYSREFNO=? ";
				PayUnionpayRecord ub = DataBaseHelper.queryForBean(urBillSql, PayUnionpayRecord.class, rTradeNo);
				if(ub!=null){
					ur.setTradeState("SUCCESS");
					if(ur.getAmount().equals(ub.getAmount())){
						ur.setBillStatus("1");
						ur.setBillDesc("对账成功");
						
						ub.setBillStatus("1");
						ub.setBillDesc("对账成功");
					}else{
						ur.setBillStatus("2");
						ur.setBillDesc("金额不一致");
						
						ub.setBillStatus("2");
						ub.setBillDesc("金额不一致");
					}
					DataBaseHelper.updateBeanByPk(ur);
					DataBaseHelper.updateBeanByPk(ub);
				}else{
					ur.setBillStatus("3");
					ur.setBillDesc("单边账");
					DataBaseHelper.updateBeanByPk(ur);
				}
				String extSql = " select * from BL_EXT_PAY where PK_EXTPAY=? ";
				BlExtPay ext = DataBaseHelper.queryForBean(extSql, BlExtPay.class, ur.getPkExtpay());
				if(ext!=null){
					if("SUCCESS".equals(ur.getTradeState())){
						ext.setFlagPay("1");
					}else{
						ext.setFlagPay("0");
					}
					ur.setNhisBillStatus("1");
					ur.setBillDesc("对账成功");
					DataBaseHelper.updateBeanByPk(ur);
					
					ext.setEuBill("1");
					DataBaseHelper.updateBeanByPk(ext);
				}else{
					ur.setNhisBillStatus("3");
					ur.setNhisBillDesc("单边账");
					DataBaseHelper.updateBeanByPk(ur);
				}
				result = true;
			}
		}
		return result;
	}
	
}
