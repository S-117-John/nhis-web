package com.zebone.nhis.pro.zsba.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

import net.sf.json.JSONObject;

/**
 * 支付宝缴费的订单数据检查
 * 因为有可能网络问题导致，支付宝支付成功，但本地未生成押金数据的情况
 * @author songs
 */
@Service
public class ZsbaAliTradeService {

	private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	public void executeCheckOrder(QrtzJobCfg cfg){  
		String dateTime = DateUtils.addDate(new Date(), -6, 5, "yyyy-MM-dd HH:mm:ss");
		try {
			updateAli(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 本地支付宝小程序交易记录数据处理
	 * @param queryDate
	 */
	private void updateAli(String queryDate){
		String wxSql = "select * from PAY_ALI_RECORD where TRADE_STATE=? and CONVERT(varchar(19), CREATE_TIME, 120)<=? and APPID=?";
		List<PayAliRecord> wrList = DataBaseHelper.queryForList(wxSql, PayAliRecord.class, new Object[]{"INIT", 
				queryDate,ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_APP_ID", "")});
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		
		for(PayAliRecord wr : wrList){
			Map<String,String> resultMap = alipayOrderQuery(wr.getOutTradeNo());
			
			TransactionStatus status = platformTransactionManager.getTransaction(def);
			try {
				String tradeResult = resultMap.get("state");
				if(StringUtils.isNotEmpty(tradeResult)){
					if("FAIL".equals(tradeResult)){
						if(StringUtils.isNotEmpty(wr.getPkExtpay())){
							BlExtPay extpay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where PK_EXTPAY=?", BlExtPay.class, new Object[]{wr.getPkExtpay()});
							if(extpay!=null){
								if(StringUtils.isNotEmpty(extpay.getPkDepo())){
									BlDeposit depo = DataBaseHelper.queryForBean("select * from BL_DEPOSIT where PK_DEPO=?", BlDeposit.class, new Object[]{extpay.getPkDepo()});
									if(depo!=null){
										if(StringUtils.isNotEmpty(depo.getPkSettle())){
											/**已结算的如何处理*/
										}
										//删除预交金记录
										DataBaseHelper.deleteBeanByPk(depo);
										logger.info("删除【预交金】记录：{},{}", depo.getPkDepo(), depo.getAmount());
									}
								}
								//删除外部接口支付记录
								DataBaseHelper.deleteBeanByPk(extpay);
								logger.info("删除【外部接口支付】记录：{},{}", extpay.getPkExtpay(), extpay.getAmount());
							}
						}
						//删除本地交易数据
						DataBaseHelper.deleteBeanByPk(wr);
						logger.info("删除【支付宝交易】记录：{},{}", wr.getPkPayAliRecord(), wr.getTotalAmount());
					}else{

						@SuppressWarnings("unchecked")
						Map<String,String> map = JsonUtil.readValue(resultMap.get("result"), Map.class);
						
						//更新本地交易数据
						String wrSql = " update PAY_ALI_RECORD set PAY_DATA=?, TRADE_STATE=?, WECHAT_TRADE_NO=?, OPENID=?, PAY_TIME=?, MODITY_TIME=GETDATE() where PK_PAY_ALI_RECORD=? ";
  						DataBaseHelper.update(wrSql, resultMap.get("result"), tradeResult, map.get("tradeNo"), map.get("buyerId"), map.get("sendPayDate"), wr.getPkPayAliRecord());
						logger.info("更新【支付宝交易】记录：{},{}", wr.getPkPayAliRecord(), wr.getTotalAmount());
						
						//更新外部接口支付记录
						if(StringUtils.isNotEmpty(wr.getPkExtpay())){
							BlExtPay extpay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where PK_EXTPAY=?", BlExtPay.class, new Object[]{wr.getPkExtpay()});
							if(extpay!=null){
								String pkDepo = createDepoist(extpay, wr.getSystemModule());//生成付款记录
								String extpaySql = " update BL_EXT_PAY set FLAG_PAY=?, DATE_PAY=?, PK_DEPO=? where PK_EXTPAY=? ";
		  						DataBaseHelper.update(extpaySql, "1", new Date(), pkDepo, wr.getPkExtpay());
								logger.info("更新【外部接口支付】记录：{},{}", extpay.getPkExtpay(), extpay.getAmount());
							}
						}
					}
				}
				platformTransactionManager.commit(status);
			} catch (Exception e) {
				logger.error("处理交易数据异常："+e.getMessage());
				platformTransactionManager.rollback(status);
			}
		}
	}
	
	/**
	 * 调用支付宝小程序接口查询订单信息
	 * @param outTradeNo
	 * @return
	 */
	private Map<String,String> alipayOrderQuery(String outTradeNo){
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "");
		resultMap.put("state", "FAIL");
		try {
			//构造biz_data业务数据JSON《统一收单线下交易查询》
			JSONObject buzData = new JSONObject();
			buzData.put("out_trade_no", outTradeNo);	//商户订单号
			
			//构造预下单参数MAP
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_GATEWAY", ""));
			parameters.put("appid", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_APP_ID", ""));
			parameters.put("charset", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_CHARSET", ""));
			parameters.put("publicKey", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_PUBLIC_KEY", ""));
			parameters.put("privateKey", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_PRIVATE_KEY", ""));
			parameters.put("bizContent", buzData.toString());//业务数据
			
			//发送http请求通过中转服务器请求支付宝接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_applets_order_query;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			logger.info("支付宝[主动查询订单]返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				if(map.get("state").equals("true")) {
					@SuppressWarnings("unchecked")
					Map<String,String> returnInfo = JsonUtil.readValue(map.get("return_info"), Map.class);
					resultMap.put("result", map.get("return_info"));
					if (returnInfo.get("is_success").equals("true")) {
						/*
						 *  交易状态：
						 *  WAIT_BUYER_PAY（交易创建，等待买家付款）、
						 *  TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
						 *  TRADE_SUCCESS（交易支付成功）、
						 *  TRADE_FINISHED（交易结束，不可退款）
						 */
						if(!"WAIT_BUYER_PAY".equals(returnInfo.get("trade_status"))){
							if("TRADE_SUCCESS".equals(returnInfo.get("trade_status")) || "TRADE_FINISHED".equals(returnInfo.get("trade_status"))){
								resultMap.put("state", "SUCCESS");//交易成功
							}else{
								resultMap.put("state", "FAIL");//交易失败
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("主动查询[支付宝]订单交易结果出现异常："+e.getMessage());
		}
		return resultMap;
	}
	
	
	/**
	 * 生成预交金记录、并支付记录关联预交金
	 * @param blExtPay
	 * @return
	 */
	private String createDepoist(BlExtPay blExtPay, String sysModel){
		// 1.生成预交金记录
		ZsbaBlDeposit depo = new ZsbaBlDeposit();
		depo.setPkPv(blExtPay.getPkPv());	//就诊主键
		depo.setPkPi(blExtPay.getPkPi());	//患者主键
		depo.setEuDirect("1");				//收退方向-收
		depo.setVoidType("0");				//正常
		depo.setAmount(blExtPay.getAmount());//金额
		if("zy".equals(sysModel)){
			depo.setEuPvtype("3");				//就诊类型-住院
			depo.setEuDptype("9");				//收付款类型-住院预交金
			depo.setNote("住院预交金");
			depo.setFlagSettle("0");			//未结算
		}
		if("mz".equals(sysModel)){
			depo.setEuPvtype("1");				//就诊类型-门诊
			depo.setEuDptype("4");				//收付款类型-就诊结算
			depo.setNote("门诊就诊结算");
			depo.setFlagSettle("1");			//已结算
		}
		if("0".equals(blExtPay.getEuPaytype())){
			depo.setDtPaymode("7");				//收付款方式-微信
		}else{
			depo.setDtPaymode("8");				//收付款方式-支付宝
		}
		depo.setPkOrg(blExtPay.getPkOrg());	//所属机构
		depo.setPkEmpPay(blExtPay.getCreator());//收款人
		String nameEmp = "99999".equals(blExtPay.getCreator().trim())?"支付宝小程序":"自助机:"+blExtPay.getCreator().trim();
		depo.setNameEmpPay(nameEmp);//收款人姓名
		depo.setCreator(blExtPay.getCreator());//创建人
		depo.setModifier(blExtPay.getCreator());//修改人
		depo.setCodeDepo(ApplicationUtils.getCode("0606"));//交款编码
		depo.setDatePay(blExtPay.getCreateTime());//收付款日期
		depo.setTs(new Date());//时间戳
		DataBaseHelper.insertBean(depo);
		
		return depo.getPkDepo();
	}
	
}
