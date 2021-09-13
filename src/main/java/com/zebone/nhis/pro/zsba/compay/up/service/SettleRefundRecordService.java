package com.zebone.nhis.pro.zsba.compay.up.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.support.PayCommonUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.up.vo.AmountBean;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayUnionpayRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.SettleRefundRecord;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 
 * 结算多余预交金退费记录查询与退费
 * 
 * @author lipz
 * 
 */
@Service
public class SettleRefundRecordService {

	/**
	 * 查询待退费记录
	 * 022003002005
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> queryRefunds(String param, IUser user) {
		Map<String,Object> resultMap = Maps.newHashMap();

		SettleRefundRecord srr = JsonUtil.readValue(param, SettleRefundRecord.class);
		if (srr != null
				&& (StringUtils.isNotEmpty(srr.getPkSettle()) || StringUtils.isNotEmpty(srr.getPkPv())) ) {

			Object[] params = null;
			String where = "";
			if (StringUtils.isNotEmpty(srr.getPkSettle())) {
				where = " and PK_SETTLE=?";
				params = new Object[] { srr.getPkSettle() };
			} else if (StringUtils.isNotEmpty(srr.getPkPv())) {
				where = " and PK_PV=?";
				params = new Object[] { srr.getPkPv() };
			}
			if (params != null) {
				// 待退费数据
				String dataSql = " SELECT * FROM PAY_SETTLE_REFUND_RECORD WHERE DEL_FLAG='0' and REFUND_STATUS='0' ";
				List<SettleRefundRecord> srrList = DataBaseHelper.queryForList(dataSql + where, SettleRefundRecord.class, params);
				resultMap.put("srrList", srrList);
				if(srrList!=null && srrList.size()>0){
					// 剩余应退金额
					String ytSql = "select nvl(sum(YT_AMOUNT),0) as amount from (SELECT DISTINCT PK_SETTLE, YT_AMOUNT FROM PAY_SETTLE_REFUND_RECORD where DEL_FLAG='0' and REFUND_STATUS='0' and ORDER_TYPE='2' and PK_PV=? ) as t";
					AmountBean amount = DataBaseHelper.queryForBean(ytSql, AmountBean.class, params);
					resultMap.put("refundAmount", amount.getAmount());
				}else{
					resultMap.put("refundAmount", 0);
				}
			}
		} else {
			throw new BusException("参数错误，pkSettle不能为空 或者 pkPi和pkPv不能为空！");
		}
		return resultMap;
	}
	
	/**
	 * 退微信、支付宝、退现金
	 * 022003002006
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String refundWxAliXj(String param, IUser user) {
		String refundReason = "出院结算多余预交金退款";
		String dtStTypeName = "出院结算";
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkSrrTxt = paramMap.get("pkSettleRefundRecord");
		String amountTxt = paramMap.get("returnAmounts");
		String[] pkSrrArray = pkSrrTxt.split(",");
		String[] amountArray = amountTxt.split(",");
		String dtSttype = paramMap.get("dtSttype");
		if(dtSttype.equals("21")){
			refundReason = "取消结算最后一笔退款";
			dtStTypeName = "取消结算";
		}
		
		int num=0;
		String sql = "select * from PAY_SETTLE_REFUND_RECORD where PK_SETTLE_REFUND_RECORD=?";
		SettleRefundRecord srr = null;
		for(int i=0; i < pkSrrArray.length; i++){
			String pkSrr = pkSrrArray[i];
			BigDecimal amount = new BigDecimal(amountArray[i]);
			srr = DataBaseHelper.queryForBean(sql, SettleRefundRecord.class, new Object[]{pkSrr});
			if(srr!=null){
				boolean isRefund = false;
				//初始化退款押金记录
				ZsbaBlDeposit depo = new ZsbaBlDeposit();
				depo.setPkPv(srr.getPkPv());	//就诊主键
				depo.setPkPi(srr.getPkPi());	//患者主键
				depo.setEuPvtype("3");				//就诊类型-住院
				if(dtSttype.equals("21")){
					depo.setEuDptype("4");				//收付款类型-取消结算
				}else{
					depo.setEuDptype("0");				//收付款类型-就诊结算
				}
				depo.setEuDirect("-1");				//收退方向-退
				depo.setFlagSettle("1");			//已结算
				depo.setPkSettle(srr.getPkSettle());//结算主键
				depo.setVoidType("0");				//正常
				depo.setAmount(amount.subtract(amount).subtract(amount));//金额
				
				
				//执行退款
				if("0".equals(srr.getOrderType())){
					// 退微信
					isRefund = refundWx(srr.getPkPayRecord(), srr.getOriginalTradeNo(), srr.getOriginalAmount(), amount, refundReason, depo);
					depo.setDtPaymode("7");
					depo.setNote(dtStTypeName+"：退微信");
				}else if("1".equals(srr.getOrderType())){
					// 退支付宝
					isRefund = refundAli(srr.getPkPayRecord(), srr.getOriginalTradeNo(), srr.getOriginalAmount(), amount, refundReason, depo);
					depo.setDtPaymode("8");
					depo.setNote(dtStTypeName+"：退支付宝");
				}else{
					// 退现金
					isRefund = true;
					depo.setDtPaymode("1");
					depo.setNote(dtStTypeName+"：退现金");
				}
				if(isRefund){
					//生成退款押金记录
					ApplicationUtils.setDefaultValue(depo, true);
					depo.setPkDept(UserContext.getUser().getPkDept());  //所属科室
					depo.setPkEmpPay(UserContext.getUser().getPkEmp()); //收款人
					depo.setNameEmpPay(UserContext.getUser().getNameEmp());//收款人姓名
					depo.setDatePay(new Date());//收付款日期
					depo.setFlagAcc("0");
					depo.setFlagCc("0");
					depo.setFlagReptBack("0");
					DataBaseHelper.insertBean(depo);
					
					// 更新待退费记录
					srr.setPkDepo(depo.getPkDepo());
					srr.setRefundStatus("1");
					DataBaseHelper.updateBeanByPk(srr);
					num++;
				}
			}
		}
		
		String result = "false";
		if(num == pkSrrArray.length){
			result = "true";
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private boolean refundWx(String wxPk, String outTradeNo, BigDecimal totalMoney, BigDecimal refundAmount, String refundReason, ZsbaBlDeposit depo){
		boolean isRefund = false;
		String sql = "select * from PAY_WECHAT_RECORD where PK_PAY_WECHAT_RECORD=?";
		PayWechatRecord wr = DataBaseHelper.queryForBean(sql, PayWechatRecord.class, new Object[]{wxPk});
		if(wr!=null){
			/**
			 * 微信支付申请退款接口
			 */
			String outRefundNo = NHISUUID.getKeyId();
			//根据生成的订单信息调用《微信申请退款接口》
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));//公众账号ID
			parameters.put("mch_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));//商户号
			parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
			parameters.put("out_trade_no", outTradeNo);//商户订单号
			parameters.put("out_refund_no", outRefundNo);//商户退款单号
			BigDecimal amount = totalMoney.multiply(new BigDecimal(100));//转分
			Integer totalFee = Integer.parseInt(amount.stripTrailingZeros().toPlainString());
			parameters.put("total_fee", totalFee);//订单金额
			BigDecimal money = refundAmount.multiply(new BigDecimal(100));//转分
			Integer refundMoney = Integer.parseInt(money.stripTrailingZeros().toPlainString());
			parameters.put("refund_fee", refundMoney);//退款金额
			parameters.put("refund_fee_type", "CNY");//货币种类
			parameters.put("refund_desc", refundReason);//退款原因
			parameters.put("refund_account", "");//退款资金来源
			parameters.put("op_user_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));//操作员-默认商户号
			parameters.put("pay_sign_key", ApplicationUtils.getPropertyValue("WECHAT_PAY_SIGN_KEY", ""));
			
			//发送http请求通过中转服务器请求微信接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_refund_url;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			
			if(StringUtils.isNotEmpty(result)){
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				
				String returnCode = map.get("return_code");//返回状态码
				if(returnCode.equals("SUCCESS")){
					String resultCode = map.get("result_code");//业务结果
					if(resultCode.equals("SUCCESS")){
						//外部接口-支付退款信息
						String extPaySql = "select * from BL_EXT_PAY where PK_EXTPAY = ?";
						BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{wr.getPkExtpay()});
						BlExtPay refundExtPay = new BlExtPay();
						if(blExtPay!=null){
							BeanUtils.copyProperties(blExtPay, refundExtPay);
							refundExtPay.setPkExtpay(null);
							refundExtPay.setAmount(refundAmount.multiply(new BigDecimal(-1)));
							refundExtPay.setDescPay("微信统一退款");
							refundExtPay.setResultPay(result);
							refundExtPay.setRefundNo(outRefundNo);//退款单号
							refundExtPay.setDateRefund(new Date());//退款支付时间
							DataBaseHelper.insertBean(refundExtPay);
							
							// 更新缴款支付记录关联退款记录
							blExtPay.setRefundNo(outRefundNo);
							DataBaseHelper.updateBeanByPk(blExtPay);
							
							//设置被退的预交金主键
							depo.setPkDepoBack(blExtPay.getPkDepo());
						}
						
						//生成交易记录表退款记录
						PayWechatRecord refundWr = new PayWechatRecord();
						BeanUtils.copyProperties(wr, refundWr);
						refundWr.setPkPayWechatRecord(null);
						refundWr.setInitData(null);
						refundWr.setPayTime(null);
						refundWr.setPayData(null);
						refundWr.setPkExtpay(refundExtPay.getPkExtpay());
						refundWr.setOrderType("refund");//退费
						refundWr.setTotalAmount(refundAmount.multiply(new BigDecimal(-1)));//退款金额
						refundWr.setTradeState(resultCode);
						refundWr.setCreateTime(new Date());
						refundWr.setRefundData(result);
						refundWr.setDetail(refundReason);
						refundWr.setRefundTradeNo(outRefundNo);
						
						refundWr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
						refundWr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
						refundWr.setCreateTime(new Date());
						refundWr.setModifier(refundWr.getCreator());
						refundWr.setModityTime(refundWr.getCreateTime());
						
						DataBaseHelper.insertBean(refundWr);
						
						// 更新缴款记录关联退款记录
						wr.setRefundTradeNo(outRefundNo);
						DataBaseHelper.updateBeanByPk(wr);
						
						isRefund = true;
					}
				}
			}
		}
		return isRefund;
	}
	
	@SuppressWarnings("unchecked")
	private boolean refundAli(String aliPk, String outTradeNo, BigDecimal totalMoney, BigDecimal refundAmount, String refundReason, ZsbaBlDeposit depo){
		boolean isRefund = false;
		String sql = "select * from PAY_ALI_RECORD where PK_PAY_ALI_RECORD=?";
		PayAliRecord ar = DataBaseHelper.queryForBean(sql, PayAliRecord.class, new Object[]{aliPk});
		if(ar!=null){
			/*
			 * 支付宝申请退款
			 */
			String outRefundNo = NHISUUID.getKeyId();//生成系统商户退款单号(时间戳+4位随机数)
			//构造申请退款接口的JSON参数
			StringBuilder sb = new StringBuilder();
			sb.append("{\"out_trade_no\":\"" + outTradeNo + "\",");//支付宝交易号
			sb.append("\"refund_amount\":\""+refundAmount+"\",");//退款金额
			sb.append("\"out_request_no\":\""+outRefundNo+"\",");//商户退款请求号
			sb.append("\"refund_reason\":\""+refundReason+"\"}");//退款原因
			
			//构造预下单参数MAP
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_GATEWAY", ""));
			parameters.put("appid",ApplicationUtils.getPropertyValue("APP_ID", ""));
			parameters.put("private_key", ApplicationUtils.getPropertyValue("PRIVATE_KEY", ""));
			parameters.put("charset", "utf-8");
			parameters.put("alipay_public_key", ApplicationUtils.getPropertyValue("ALIPAY_PUBLIC_KEY", ""));
			parameters.put("biz_content", sb.toString());//业务数据
			
			//发送http请求通过中转服务器请求支付宝接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_refund_url;
			String params = JsonUtil.writeValueAsString(parameters);
			String result = HttpUtil.httpPost(queryUrl, params);
			
			if(StringUtils.isNotEmpty(result)){
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				if(map.get("is_success").equals("true")){
					String code = map.get("code");
					if(code.equals("10000")){
						//外部接口-支付退款信息
						String extPaySql = "select * from BL_EXT_PAY where PK_EXTPAY = ?";
						BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{ar.getPkExtpay()});
						BlExtPay refundExtPay = new BlExtPay();
						if(blExtPay!=null){
							BeanUtils.copyProperties(blExtPay, refundExtPay);
							refundExtPay.setPkExtpay(null);
							refundExtPay.setAmount(refundAmount.multiply(new BigDecimal(-1)));
							refundExtPay.setDescPay("支付宝统一退款");
							refundExtPay.setResultPay(result);
							refundExtPay.setRefundNo(outRefundNo);//退款单号
							refundExtPay.setDateRefund(new Date());//退款支付时间
							DataBaseHelper.insertBean(refundExtPay);
							
							// 更新缴款支付记录关联退款记录
							blExtPay.setRefundNo(outRefundNo);
							DataBaseHelper.updateBeanByPk(blExtPay);

							//设置被退的预交金主键
							depo.setPkDepoBack(blExtPay.getPkDepo());
						}
						//生成交易记录表退款记录
						PayAliRecord refundAr = new PayAliRecord();
						BeanUtils.copyProperties(ar, refundAr);
						refundAr.setPkPayAliRecord(null);
						refundAr.setInitData(null);
						refundAr.setPayTime(null);
						refundAr.setPayData(null);
						refundAr.setPkExtpay(refundExtPay.getPkExtpay());
						refundAr.setOrderType("refund");//退费
						refundAr.setTotalAmount(refundAmount.multiply(new BigDecimal(-1)));//退款金额
						refundAr.setTradeState("SUCCESS");
						refundAr.setCreateTime(new Date());
						refundAr.setRefundData(result);
						refundAr.setDetail(refundReason);
						refundAr.setRefundTradeNo(outRefundNo);
						
						refundAr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
						refundAr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
						refundAr.setCreateTime(new Date());
						refundAr.setModifier(refundAr.getCreator());
						refundAr.setModityTime(refundAr.getCreateTime());
						DataBaseHelper.insertBean(refundAr);
						
						// 更新缴款记录关联退款记录
						ar.setRefundTradeNo(outRefundNo);
						DataBaseHelper.updateBeanByPk(ar);
						
						isRefund = true;
					}
				}
			}
		}
		return isRefund;
	}
	
	/**
	 * 退银联保存交易数据
	 * 022003002007
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String refundUnionPay(String param, IUser user) {
		String result = "false";
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkSrr = paramMap.get("pkSettleRefundRecord");
		String dtSttype = paramMap.get("dtSttype");
		if(StringUtils.isNotEmpty(pkSrr)){
			//解析银联数据
			JSONObject jo = JSONObject.fromObject(param);
			String o = jo.get("param").toString();
			PayUnionpayRecord pur = JsonUtil.readValue(o, PayUnionpayRecord.class);
			//验证时退货，且原交易号不为空
			if(pur!=null && "71".equals(pur.getTranstype()) && StringUtils.isNotEmpty(pur.getOriginSysrefno())){
				BigDecimal refundAmount = pur.getAmount().divide(new BigDecimal(100)).multiply(new BigDecimal(-1));//转元再转负数
				
				//查询待退费记录
				String sql = "select * from PAY_SETTLE_REFUND_RECORD where PK_SETTLE_REFUND_RECORD=?";
				SettleRefundRecord srr = DataBaseHelper.queryForBean(sql, SettleRefundRecord.class, new Object[]{pkSrr});
				if(srr!=null){
					
					//查询原交易记录
					String unionSql = "select * from PAY_UNIONPAY_RECORD where SYSREFNO=?";
					PayUnionpayRecord payPur = DataBaseHelper.queryForBean(unionSql, PayUnionpayRecord.class, new Object[]{pur.getOriginSysrefno().substring(0,12)});
					if(payPur!=null){
						//初始化退款押金记录
						ZsbaBlDeposit depo = new ZsbaBlDeposit();
						depo.setPkPv(srr.getPkPv());	//就诊主键
						depo.setPkPi(srr.getPkPi());	//患者主键
						depo.setEuPvtype("3");				//就诊类型-住院
						if(dtSttype.equals("21")){
							depo.setEuDptype("4");				//收付款类型-取消结算
							depo.setNote("取消结算：收银行卡");
						}else{
							depo.setEuDptype("0");				//收付款类型-就诊结算
							depo.setNote("出院结算：收银行卡");
						}
						depo.setEuDirect("-1");				//收退方向-退
						depo.setFlagSettle("1");			//已结算
						depo.setPkSettle(srr.getPkSettle());//结算主键
						depo.setVoidType("0");				//正常
						depo.setDtPaymode("3");
						depo.setFlagAcc("0");
						depo.setFlagCc("0");
						depo.setFlagReptBack("0");
						BigDecimal amount = pur.getAmount().divide(new BigDecimal(100));
						if(depo.getEuDirect().equals("-1")){
							depo.setNote("出院结算：退银行卡");
							amount = amount.subtract(amount).subtract(amount);
						}
						depo.setAmount(amount);//金额
						
						
						//查询原外部交易表
						String bepSql = "select * from BL_EXT_PAY where PK_EXTPAY=?";
						BlExtPay bep = DataBaseHelper.queryForBean(bepSql, BlExtPay.class, new Object[]{payPur.getPkExtpay()});
						if(bep!=null){
							bep.setRefundNo(pur.getOriginSysrefno());
							DataBaseHelper.updateBeanByPk(bep);
							
							//设置被退的预交金主键
							depo.setPkDepoBack(bep.getPkDepo());
						}
						payPur.setOriginSysrefno(pur.getOriginSysrefno());
						DataBaseHelper.updateBeanByPk(payPur);
						
						//生成退款押金记录
						ApplicationUtils.setDefaultValue(depo, true);
						depo.setPkDept(UserContext.getUser().getPkDept());  //所属科室
						depo.setPkEmpPay(UserContext.getUser().getPkEmp()); //收款人
						depo.setNameEmpPay(UserContext.getUser().getNameEmp());//收款人姓名
						depo.setDatePay(new Date());//收付款日期
						DataBaseHelper.insertBean(depo);
						
						// 保存外部接口-支付记录
						BlExtPay blExtPay = new BlExtPay();
						blExtPay.setPkPi(payPur.getPkPi());
						blExtPay.setPkPv(payPur.getPkPv());
						blExtPay.setPkDepo(depo.getPkDepo());
						blExtPay.setAmount(refundAmount);
						blExtPay.setEuPaytype("3");
						blExtPay.setDatePay(DateUtils.strToDate(pur.getTransdate() + pur.getTranstime(), "yyyyMMddHHmmss"));
						blExtPay.setSerialNo(pur.getSystracdno());
						blExtPay.setDescPay("银联退货");
						blExtPay.setRefundNo(pur.getOriginSysrefno());
						blExtPay.setTradeNo(pur.getSysrefno());
						blExtPay.setEuBill("0");
						blExtPay.setFlagPay("1");
						blExtPay.setSysname("综合服务平台");
						blExtPay.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
						blExtPay.setCreator(UserContext.getUser().getPkEmp());//当前操作人关联编码
						blExtPay.setCreateTime(new Date());
						blExtPay.setModifier(blExtPay.getCreator());
						blExtPay.setModityTime(blExtPay.getCreateTime());
						blExtPay.setPkSettle(srr.getPkSettle());
						DataBaseHelper.insertBean(blExtPay);
						
						// 保存银联交易记录
						pur.setPkPi(payPur.getPkPi());
						pur.setPkPv(payPur.getPkPv());
						pur.setPkExtpay(blExtPay.getPkExtpay());
						pur.setAmount(refundAmount);
						pur.setBillStatus("0");//未对账
						pur.setPkOrg(blExtPay.getPkOrg());//当前操作人关联机构
						pur.setCreator(blExtPay.getCreator());//当前操作人关联编码
						pur.setCreateTime(blExtPay.getCreateTime());
						pur.setModifier(blExtPay.getModifier());
						pur.setModityTime(blExtPay.getModityTime());
						DataBaseHelper.insertBean(pur);
						
						// 更新为已退款，并关联退款预交金
						srr.setRefundStatus("1");
						srr.setPkDepo(depo.getPkDepo());
						DataBaseHelper.updateBeanByPk(srr);
						
						result = "true";
					}
				}
			}
		}
		return result;
	}

	/**
	 * 新pos机退银联、微信、支付宝保存交款表
	 */
	@SuppressWarnings("unchecked")
	public String refundUnionPaySand(String param, IUser user) {
		String result = "false";
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkSrr = paramMap.get("pkSettleRefundRecord");
		String dtSttype = paramMap.get("dtSttype");
		String pkExtPay = paramMap.get("pkExtPay");
		String amount = paramMap.get("amount");
		if(StringUtils.isNotEmpty(pkSrr)){
				BigDecimal refundAmount = new BigDecimal(amount);
				
				//查询待退费记录
				String sql = "select * from PAY_SETTLE_REFUND_RECORD where PK_SETTLE_REFUND_RECORD=?";
				SettleRefundRecord srr = DataBaseHelper.queryForBean(sql, SettleRefundRecord.class, new Object[]{pkSrr});
				if(srr!=null){
					
					//查询原交易记录
					String unionSql = "select * from bl_ext_pay where pk_extpay=?";
					BlExtPay payPur = DataBaseHelper.queryForBean(unionSql, BlExtPay.class, new Object[]{pkExtPay});
					if(payPur!=null){
						//初始化退款押金记录
						ZsbaBlDeposit depo = new ZsbaBlDeposit();
						depo.setPkPv(srr.getPkPv());	//就诊主键
						depo.setPkPi(srr.getPkPi());	//患者主键
						depo.setEuPvtype("3");				//就诊类型-住院
						if(dtSttype.equals("21")){
							depo.setEuDptype("4");				//收付款类型-取消结算
							if(srr.getOrderType().equals("4")){
								depo.setNote("取消结算：收衫德POS机微信");
							}else if(srr.getOrderType().equals("5")){
								depo.setNote("取消结算：收衫德POS机支付宝");
							}else if(srr.getOrderType().equals("6")){
								depo.setNote("取消结算：收衫德POS机银行卡");
							}
						}else{
							depo.setEuDptype("0");				//收付款类型-就诊结算
							if(srr.getOrderType().equals("4")){
								depo.setNote("出院结算：收衫德POS机微信");
							}else if(srr.getOrderType().equals("5")){
								depo.setNote("出院结算：收衫德POS机支付宝");
							}else if(srr.getOrderType().equals("6")){
								depo.setNote("出院结算：收衫德POS机银行卡");
							}
						}
						depo.setEuDirect("-1");				//收退方向-退
						depo.setFlagSettle("1");			//已结算
						depo.setPkSettle(srr.getPkSettle());//结算主键
						depo.setVoidType("0");				//正常
						if(srr.getOrderType().equals("4")){
							depo.setDtPaymode("7");
						}else if(srr.getOrderType().equals("5")){
							depo.setDtPaymode("8");
						}else if(srr.getOrderType().equals("6")){
							depo.setDtPaymode("3");
						}
						depo.setDtBank("1");
						depo.setFlagAcc("0");
						depo.setFlagCc("0");
						depo.setFlagReptBack("0");
						BigDecimal amount2 = new BigDecimal(amount);
						if(depo.getEuDirect().equals("-1")){
							if(srr.getOrderType().equals("4")){
								depo.setNote("出院结算：退衫德POS机微信");
							}else if(srr.getOrderType().equals("5")){
								depo.setNote("出院结算：退衫德POS机支付宝");
							}else if(srr.getOrderType().equals("6")){
								depo.setNote("出院结算：退衫德POS机银行卡");
							}
						}
						depo.setAmount(amount2);//金额
						
		
						//设置被退的预交金主键
						//depo.setPkDepoBack(bep.getPkDepo());
						
						
						
						//生成退款押金记录
						ApplicationUtils.setDefaultValue(depo, true);
						depo.setPkDept(UserContext.getUser().getPkDept());  //所属科室
						depo.setPkEmpPay(UserContext.getUser().getPkEmp()); //收款人
						depo.setNameEmpPay(UserContext.getUser().getNameEmp());//收款人姓名
						depo.setDatePay(new Date());//收付款日期
						DataBaseHelper.insertBean(depo);
						
						// 更新为已退款，并关联退款预交金
						srr.setRefundStatus("1");
						srr.setPkDepo(depo.getPkDepo());
						DataBaseHelper.updateBeanByPk(srr);
						
						result = "true";
					}
				}
		}
		return result;
	}
	
	
	/**
	 * 删除 多余未退的待退费记录
	 * 022003002013
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String deleteRefunds(String param, IUser user) {
		String result = "false";
		Map<String, String> paramsMap = JsonUtil.readValue(param, Map.class);
		String pkPv = paramsMap.get("pkPv");
		if (StringUtils.isNotEmpty(pkPv)) {
			// 删除 多余未退的POS机待退费记录
			String deleteSql = "DELETE FROM PAY_SETTLE_REFUND_RECORD where REFUND_STATUS='0' and PK_PV=? ";
			DataBaseHelper.execute(deleteSql, new Object[] {pkPv});
			
			result = "true";
		} else {
			throw new BusException("参数错误，pkPv不能为空！");
		}
		return result;
	}
	
	
	/**
	 * 剩余应退金额转财务记账或现金
	 * 1.插入交款记录
	 * 2.删除多余未退的待退费记录
	 * @param param
	 * @param user
	 */
	public void refundCwjzOrXj(String param, IUser user){
		Map<String, String> paramsMap = JsonUtil.readValue(param, Map.class);
		String pkPv = paramsMap.get("pkPv");
		String pkPi = paramsMap.get("pkPi");
		String dtSttype = paramsMap.get("dtSttype");
		String pkSettle = paramsMap.get("pkSettle");
		String ytAmount = paramsMap.get("ytAmount");
		String remittanceUnit = paramsMap.get("remittanceUnit");
		String dtPayMode = paramsMap.get("dtPayMode");
		
		String dtPayModeName = "现金";
		if(dtPayMode.equals("92")){
			dtPayModeName = "财务记账";
		}
		
		PvEncounter pm = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ? ", PvEncounter.class, pkPv);
		
		//初始化退款押金记录
		ZsbaBlDeposit depo = new ZsbaBlDeposit();
		depo.setPkPv(pkPv);	//就诊主键
		depo.setPkPi(pm.getPkPi());	//患者主键
		if(dtPayMode.equals("92")){
			depo.setRemittanceUnit(remittanceUnit);
		}
		depo.setEuPvtype("3");				//就诊类型-住院
		if(dtSttype.equals("21")){
			depo.setEuDptype("4");				//收付款类型-取消结算
		}else{
			depo.setEuDptype("0");				//收付款类型-就诊结算
		}
		depo.setEuDirect("-1");				//收退方向-退
		depo.setFlagSettle("1");			//已结算
		depo.setPkSettle(pkSettle);//结算主键
		depo.setVoidType("0");				//正常
		depo.setAmount(new BigDecimal(ytAmount));//金额
		
		//生成退款押金记录
		depo.setDtPaymode(dtPayMode);
		if(dtSttype.equals("21")){
			depo.setNote("取消结算：转"+dtPayModeName+"退");
		}else{
			depo.setNote("出院结算：转"+dtPayModeName+"退");
		}
		ApplicationUtils.setDefaultValue(depo, true);
		depo.setPkDept(UserContext.getUser().getPkDept());  //所属科室
		depo.setPkEmpPay(UserContext.getUser().getPkEmp()); //收款人
		depo.setNameEmpPay(UserContext.getUser().getNameEmp());//收款人姓名
		depo.setDatePay(new Date());//收付款日期
		depo.setFlagAcc("0");
		depo.setFlagCc("0");
		depo.setFlagReptBack("0");
		DataBaseHelper.insertBean(depo);
		
		
		// 删除 多余未退的待退费记录
		String deleteSql = "DELETE FROM PAY_SETTLE_REFUND_RECORD where REFUND_STATUS='0' and PK_PV=? ";
		DataBaseHelper.execute(deleteSql, pkPv);
	}
}
