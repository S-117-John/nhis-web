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

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.support.PayCommonUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 微信缴纳住院押金的订单数据检查
 * 因为有可能网络问题导致，微信支付成功，但本地未生成押金数据的情况
 * @author lipz
 *
 */
@Service
public class ZsbaWxTradeService {

	private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	public void executeCheckOrder(QrtzJobCfg cfg){  
		String dateTime = DateUtils.addDate(new Date(), -7, 5, "yyyy-MM-dd HH:mm:ss");
		try {
			updateWx(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 本地微信交易记录数据处理
	 * @param queryDate
	 */
	private void updateWx(String queryDate){
		String wxSql = "select * from PAY_WECHAT_RECORD where TRADE_STATE=? and CONVERT(varchar(19), CREATE_TIME, 120)<=?";
		List<PayWechatRecord> wrList = DataBaseHelper.queryForList(wxSql, PayWechatRecord.class, new Object[]{"INIT", queryDate});
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		
		for(PayWechatRecord wr : wrList){
			Map<String,String> resultMap = wechatOrderQuery(wr.getOutTradeNo());
			
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
						logger.info("删除【微信交易】记录：{},{}", wr.getPkPayWechatRecord(), wr.getTotalAmount());
					}else{

						@SuppressWarnings("unchecked")
						Map<String,String> map = JsonUtil.readValue(resultMap.get("result"), Map.class);
						
						//更新本地交易数据
						String wrSql = " update PAY_WECHAT_RECORD set PAY_DATA=?, TRADE_STATE=?, WECHAT_TRADE_NO=?, OPENID=?, PAY_TIME=?, MODITY_TIME=GETDATE() where PK_PAY_WECHAT_RECORD=? ";
  						DataBaseHelper.update(wrSql, resultMap.get("result"), tradeResult, map.get("transaction_id"), map.get("openid"), map.get("time_end"), wr.getPkPayWechatRecord());
						logger.info("更新【微信交易】记录：{},{}", wr.getPkPayWechatRecord(), wr.getTotalAmount());
						
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
						//更新非医疗收费数据
						if("nhis-nm".equals(wr.getSystemModule()) && StringUtils.isNotEmpty(wr.getProductId())){
							String updateStSql = " update nm_ci_st set is_pay=?, pay_method=?, pk_pay=?, charge_time=?, modity_time=? where pk_ci_st=?";
							DataBaseHelper.update(updateStSql, "1","W", new Date(), new Date(), wr.getPkPayWechatRecord(), wr.getProductId());
							logger.info("更新【非医疗收费结算】记录：{},{}", wr.getPkPayWechatRecord(), wr.getProductId());
							
							String updateStDetailSql = "update nm_ci_st_details set is_pay=?, charge_time=?, modity_time=? where pk_ci_st=?";
							DataBaseHelper.update(updateStDetailSql, "1",new Date(), new Date(), wr.getProductId());
							logger.info("更新【非医疗收费计费】记录：{}", wr.getProductId());
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
	 * 调用微信接口查询订单信息
	 * @param outTradeNo
	 * @return
	 */
	private Map<String,String> wechatOrderQuery(String outTradeNo){
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "");
		resultMap.put("state", "FAIL");
		try {
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));
			parameters.put("mch_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));
			parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
			parameters.put("out_trade_no", outTradeNo);//商户订单号
			parameters.put("transaction_id", "");//微信订单号
			parameters.put("pay_sign_key", ApplicationUtils.getPropertyValue("WECHAT_PAY_SIGN_KEY", ""));//商户支付密钥
			
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_order_query;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			resultMap.put("result", result);
			logger.info("微信[主动查询订单]返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				if (map.get("return_code").equals("SUCCESS")) {
					if(map.get("result_code").equals("SUCCESS")){
						String tradeState = map.get("trade_state");
						if(tradeState.equals("SUCCESS")){
							resultMap.put("state", "SUCCESS");//支付成功-退出
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("主动查询[微信]订单交易结果出现异常："+e.getMessage());
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
		depo.setDtPaymode(blExtPay.getEuPaytype());//收付款方式-7微信、8支付宝
		depo.setDtBank("0");
		depo.setAmount(blExtPay.getAmount());//金额
		if("zy".equals(sysModel)){
			depo.setEuPvtype("3");				//就诊类型-住院
			depo.setEuDptype("9");				//收付款类型-住院预交金
			depo.setNote("住院预交金");
			depo.setFlagSettle("0");			//未结算
		}
		if("mz".equals(sysModel)){
			depo.setEuPvtype("1");				//就诊类型-门诊
			depo.setEuDptype("0");				//收付款类型-就诊结算
			depo.setNote("门诊就诊结算");
			depo.setFlagSettle("1");			//已结算
		}
		depo.setPkOrg(blExtPay.getPkOrg());	//所属机构
		depo.setPkEmpPay(blExtPay.getCreator());//收款人
		BdOuEmployee emp = DataBaseHelper.queryForBean("select * from bd_ou_employee where pk_emp=?", BdOuEmployee.class, new Object[]{blExtPay.getCreator()});
		if(emp!=null) {
			depo.setNameEmpPay(emp.getNameEmp());//收款人姓名
		}else {
			depo.setNameEmpPay("自动任务");//收款人姓名
		}
		depo.setCreator(blExtPay.getCreator());//创建人
		depo.setModifier(blExtPay.getCreator());//修改人
		depo.setCodeDepo(ApplicationUtils.getCode("0606"));//交款编码
		depo.setDatePay(blExtPay.getCreateTime());//收付款日期
		depo.setTs(new Date());//时间戳
		DataBaseHelper.insertBean(depo);
		
		return depo.getPkDepo();
	}
	
}
