package com.zebone.nhis.pro.zsba.compay.up.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.support.PayCommonUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.up.vo.AmountBean;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.SettleRefundRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.SettleRefundTurnFinance;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 转财务退预交金
 * @author lipz
 *
 */
@Service
public class SettleRefundTurnFinanceService {

	/**
	 * 查询财务待转账退费记录
	 * 022003002009
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SettleRefundTurnFinance> queryRefundList(String param, IUser user) {
		List<SettleRefundTurnFinance> srrList = Lists.newArrayList();

		SettleRefundTurnFinance srtf = JsonUtil.readValue(param, SettleRefundTurnFinance.class);
		if (srtf != null
				&& (StringUtils.isNotEmpty(srtf.getPkSettle()) || StringUtils.isNotEmpty(srtf.getPkPv()))) {

			Object[] params = null;
			StringBuffer sql = new StringBuffer(
					" SELECT rtf.*, d.amount_prep as amount FROM PAY_SETTLE_REFUND_TURN_FINANCE rtf LEFT JOIN BL_SETTLE d on d.PK_SETTLE=rtf.PK_SETTLE WHERE rtf.DEL_FLAG='0'");
			if (StringUtils.isNotEmpty(srtf.getPkSettle())) {
				sql.append(" and rtf.PK_SETTLE=?");
				params = new Object[] { srtf.getPkSettle() };
			} else if (StringUtils.isNotEmpty(srtf.getPkPv())) {
				sql.append(" and rtf.PK_PV=?");
				params = new Object[] {srtf.getPkPv() };
			}
			if(StringUtils.isNotEmpty(srtf.getRefundStatus())){
				sql.append(" and rtf.REFUND_STATUS='"+ srtf.getRefundStatus() +"' ");
			}
			if (params != null) {
				srrList = DataBaseHelper.queryForList(sql.toString(), SettleRefundTurnFinance.class, params);
			}
		} else {
			throw new BusException("参数错误，pkSettle不能为空 或者 pkPv不能为空！");
		}
		return srrList;
	}
	
	
	/**
	 * 转财务退费
	 * 022003002008
	 * @param param
	 * @param user
	 * @return
	 */
	public SettleRefundTurnFinance turnFinance(String param, IUser user) {
		SettleRefundTurnFinance srtf = JsonUtil.readValue(param, SettleRefundTurnFinance.class);
		if (srtf != null && (StringUtils.isNotEmpty(srtf.getPkSettle()) || StringUtils.isNotEmpty(srtf.getPkPv()))) {

			Object[] params = null;
			StringBuffer sql = new StringBuffer("SELECT * FROM PAY_SETTLE_REFUND_RECORD WHERE DEL_FLAG='0' and REFUND_STATUS='0' ");
			if (StringUtils.isNotEmpty(srtf.getPkSettle())) {
				sql.append(" and PK_SETTLE=?");
				params = new Object[] { srtf.getPkSettle() };
			} else if (StringUtils.isNotEmpty(srtf.getPkPv())) {
				sql.append(" and PK_PV=?");
				params = new Object[] {srtf.getPkPv() };
			}
			
			if (params != null) {
				//现金未退金额
				String xjSql = "SELECT nvl(sum(REFUND_AMOUNT),0) as amount FROM PAY_SETTLE_REFUND_RECORD where DEL_FLAG='0' and REFUND_STATUS='0' and ORDER_TYPE='3' and PK_SETTLE=?";
				AmountBean xjAmount = DataBaseHelper.queryForBean(xjSql, AmountBean.class, new Object[] {srtf.getPkSettle()});
				
				List<SettleRefundRecord> srrList = DataBaseHelper.queryForList(sql.toString(), SettleRefundRecord.class, params);
				if(srrList!=null && srrList.size()>0){
					/*
					 *  遍历微信、支付宝-原路退费
					 */
					String pkSrrs = "";
					String refundReason = "出院结算多余预交金退款";
					BigDecimal tmzfAmount = new BigDecimal("0");
					for(SettleRefundRecord srr : srrList){
						//初始化退款押金记录
						ZsbaBlDeposit depo = new ZsbaBlDeposit();
						depo.setPkPv(srr.getPkPv());	//就诊主键
						depo.setPkPi(srr.getPkPi());	//患者主键
						depo.setEuPvtype("3");				//就诊类型-住院
						if(srtf.getDtSttype().equals("21")){
							depo.setEuDptype("4");				//收付款类型-取消结算
						}else{
							depo.setEuDptype("0");				//收付款类型-就诊结算
						}
						depo.setEuDirect("-1");				//收退方向-退
						depo.setFlagSettle("1");			//已结算
						depo.setPkSettle(srr.getPkSettle());//结算主键
						depo.setVoidType("0");				//正常
						depo.setAmount(srr.getRefundAmount().subtract(srr.getRefundAmount()).subtract(srr.getRefundAmount()));//金额
						
						if("0".equals(srr.getOrderType()) || "1".equals(srr.getOrderType())){
							tmzfAmount = tmzfAmount.add(srr.getRefundAmount());
							srr.setRefundStatus("2");
							DataBaseHelper.updateBeanByPk(srr);
							pkSrrs = "'"+srr.getPkSettleRefundRecord()+"',";
							//调用微信接口实现原路退，退成功后才更新退款标记
/*							if(refundWx(srr.getPkPayRecord(), srr.getOriginalTradeNo(), srr.getOriginalAmount(), srr.getRefundAmount(), refundReason, depo)){
								//生成退款押金记录
								depo.setDtPaymode("7");
								depo.setNote("出院结算：退微信");
								ApplicationUtils.setDefaultValue(depo, true);
								depo.setPkDept(UserContext.getUser().getPkDept());  //所属科室
								depo.setPkEmpPay(UserContext.getUser().getPkEmp()); //收款人
								depo.setNameEmpPay(UserContext.getUser().getNameEmp());//收款人姓名
								depo.setDatePay(new Date());//收付款日期
								DataBaseHelper.insertBean(depo);
								
								//更新退款标记和关联预交金主键
								srr.setRefundStatus("1");
								srr.setPkDepo(depo.getPkDepo());
								DataBaseHelper.updateBeanByPk(srr);
							}*/
						}/*else if("1".equals(srr.getOrderType())){
							//调用阿里接口实现原路退，退成功后才更新退款标记
							if(refundAli(srr.getPkPayRecord(), srr.getOriginalTradeNo(), srr.getOriginalAmount(), srr.getRefundAmount(), refundReason, depo)){
								//生成退款押金记录
								depo.setDtPaymode("8");
								depo.setNote("出院结算：退支付宝");
								ApplicationUtils.setDefaultValue(depo, true);
								depo.setPkDept(UserContext.getUser().getPkDept());  //所属科室
								depo.setPkEmpPay(UserContext.getUser().getPkEmp()); //收款人
								depo.setNameEmpPay(UserContext.getUser().getNameEmp());//收款人姓名
								depo.setDatePay(new Date());//收付款日期
								DataBaseHelper.insertBean(depo);
								
								//更新退款标记和关联预交金主键
								srr.setRefundStatus("1");
								srr.setPkDepo(depo.getPkDepo());
								DataBaseHelper.updateBeanByPk(srr);
							}
							srr.setRefundStatus("2");
							DataBaseHelper.updateBeanByPk(srr);
							pkSrrs = "'"+srr.getPkSettleRefundRecord()+"',";
						}*/else{
							// 银联和现金的直接更新为转财务退,因为该金额在后续财务中实现退费
							srr.setRefundStatus("2");
							DataBaseHelper.updateBeanByPk(srr);
							
							pkSrrs = "'"+srr.getPkSettleRefundRecord()+"',";
						}
					}
					
					SettleRefundRecord record = srrList.get(0);
					/*
					 *  转财务应退费金额 = 银联应退金额 + 支付宝、微信剩余应退金额- 银联已退金额  + 现金未退金额
					 */
					String ytSql = "select nvl(sum(YT_AMOUNT),0) as amount from (SELECT DISTINCT PK_SETTLE, YT_AMOUNT FROM PAY_SETTLE_REFUND_RECORD where DEL_FLAG='0' and ORDER_TYPE='2' and PK_SETTLE=? ) as t";
					AmountBean ytAmount = DataBaseHelper.queryForBean(ytSql, AmountBean.class, new Object[] {srtf.getPkSettle()});
					
					String tSql = "SELECT nvl(sum(REFUND_AMOUNT),0) as amount FROM PAY_SETTLE_REFUND_RECORD where DEL_FLAG='0' and REFUND_STATUS='1' and ORDER_TYPE='2' and PK_SETTLE=? ";
					AmountBean tAmount = DataBaseHelper.queryForBean(tSql, AmountBean.class, new Object[] {srtf.getPkSettle()});
					
					BigDecimal ytFee = ytAmount.getAmount().subtract(tAmount.getAmount()).add(xjAmount.getAmount()).add(tmzfAmount);
					
					//初始化退款押金记录
					ZsbaBlDeposit depo = new ZsbaBlDeposit();
					depo.setPkPv(record.getPkPv());	//就诊主键
					depo.setPkPi(record.getPkPi());	//患者主键
					depo.setEuPvtype("3");				//就诊类型-住院
					if(srtf.getDtSttype().equals("21")){
						depo.setEuDptype("4");				//收付款类型-取消结算
					}else{
						depo.setEuDptype("0");				//收付款类型-就诊结算
					}
					depo.setEuDirect("-1");				//收退方向-退
					depo.setFlagSettle("1");			//已结算
					depo.setPkSettle(record.getPkSettle());//结算主键
					depo.setVoidType("0");				//正常
					depo.setAmount(ytFee.subtract(ytFee).subtract(ytFee));//金额
					
					//生成退款押金记录
					depo.setDtPaymode("3");
					depo.setNote("出院结算：转财务退");
					ApplicationUtils.setDefaultValue(depo, true);
					depo.setPkDept(UserContext.getUser().getPkDept());  //所属科室
					depo.setPkEmpPay(UserContext.getUser().getPkEmp()); //收款人
					depo.setNameEmpPay(UserContext.getUser().getNameEmp());//收款人姓名
					depo.setDatePay(new Date());//收付款日期
					depo.setFlagAcc("0");
					depo.setFlagCc("0");
					depo.setFlagReptBack("0");
					DataBaseHelper.insertBean(depo);
					
					if(StringUtils.isNotEmpty(pkSrrs)){
						pkSrrs = pkSrrs.substring(0, pkSrrs.length()-1);
						DataBaseHelper.execute(" update PAY_SETTLE_REFUND_RECORD set PK_DEPO=? where PK_SETTLE_REFUND_RECORD in ("+pkSrrs+")", new Object[] {depo.getPkDepo()});
					}
					
					//生成待财务转账记录
					srtf.setPkSettle(record.getPkSettle());
					srtf.setPkPi(record.getPkPi());
					srtf.setPkPv(record.getPkPv());
					srtf.setPkDepo(depo.getPkDepo());
					srtf.setRefundAmount(ytFee);
					srtf.setRefundStatus("0");
					srtf.setDelFlag("0");
					srtf.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
					srtf.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
					srtf.setCreateTime(new Date());
					srtf.setModifier(srtf.getCreator());
					srtf.setModityTime(srtf.getCreateTime());
					
					DataBaseHelper.insertBean(srtf);
					
					// 删除 多余未退的银联待退费记录
					String deleteSql = "DELETE FROM PAY_SETTLE_REFUND_RECORD where REFUND_STATUS='0' and ORDER_TYPE='2' and PK_PV=? ";
					DataBaseHelper.execute(deleteSql, new Object[] {record.getPkPv()});
				}
			}
		} else {
			throw new BusException("参数错误，pkSettle不能为空 或者 pkPv不能为空！");
		}
		return srtf;
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
							refundExtPay.setTradeNo(map.get("refund_id"));
							refundExtPay.setSerialNo(outRefundNo);
							refundExtPay.setRefundNo(blExtPay.getSerialNo());//退款单号
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
						refundWr.setWechatTradeNo(map.get("refund_id"));
						refundWr.setOutTradeNo(outRefundNo);
						refundWr.setRefundTradeNo(wr.getOutTradeNo());
						
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
			String outRefundNo = NHISUUID.getKeyId();
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
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			
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
							refundExtPay.setOutTradeNo(outRefundNo);
							refundExtPay.setRefundNo(blExtPay.getTradeNo());//退款单号
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
						refundAr.setOutTradeNo(outRefundNo);
						refundAr.setRefundTradeNo(ar.getOutTradeNo());
						
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
}
