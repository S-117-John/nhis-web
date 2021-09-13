package com.zebone.nhis.pro.zsba.compay.up.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayUnionpayRecord;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class TpPayBlDepositService {

	@Autowired
	private CommonService commonService;
	
	
	/**
	 * 银联交易预下单
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> initUnionPayData(String param,IUser user){
		Map<String,String> returnMap = new HashMap<String, String>();
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		try {
			String pkPi = paramMap.get("pkPi");
			String pkPv = paramMap.get("pkPv");
			String transType = paramMap.get("transtype");												//交易类型：31消费、41撤销、71退货
			String systemModule = paramMap.get("systemModule");	
			BigDecimal totalFee = new BigDecimal(paramMap.get("amount")).divide(new BigDecimal(100));	// 从分计算为元
			if(!"31".equals(transType)){
				totalFee = totalFee.multiply(new BigDecimal(-1));// 转为负数
			}
			String outTradeNo = NHISUUID.getKeyId();													//生成系统商户订单号(时间戳+4位随机数)
		
			// 保存银联交易记录
			PayUnionpayRecord pur = new PayUnionpayRecord();
			pur.setPkExtpay(null);
			pur.setPkPi(pkPi);
			pur.setPkPv(pkPv);
			pur.setAmount(totalFee);
			pur.setTranstype(transType);
			pur.setOutTradeNo(outTradeNo);
			pur.setTradeState("INIT");
			pur.setBillStatus("0");//未对账
			
			pur.setSystemModule("zy");
			if(StringUtils.isNotEmpty(systemModule)){
				pur.setSystemModule(systemModule);
			}

			pur.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
			pur.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
			pur.setCreateTime(new Date());
			pur.setModifier(pur.getCreator());
			pur.setModityTime(pur.getCreateTime());
			DataBaseHelper.insertBean(pur);
			

			returnMap.put("outTradeNo", outTradeNo);
			returnMap.put("pkUnionpayRecord", pur.getPkUnionpayRecord());
			returnMap.put("code", "200");
			returnMap.put("msg", "【银联交易预下单】记录保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("pkUnionpayRecord", "");
			returnMap.put("code", "404");
			returnMap.put("msg", "【银联交易预下单】记录保存异常："+e.getLocalizedMessage());
		}
		return returnMap;
	}

	/**
	 * 保存银联交易记录
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> savaBlExtPay(String param,IUser user){
		Map<String,String> returnMap = new HashMap<String, String>();
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		try {
			String pkUnionpayRecord = paramMap.get("pkUnionpayRecord");//银联交易记录主键
			if(StringUtils.isEmpty(pkUnionpayRecord)){
				throw new BusException("银联预交易记录主键【pkUnionpayRecord】不能为空！");
			}
			
			PayUnionpayRecord pur = DataBaseHelper.queryForBean("select * from PAY_UNIONPAY_RECORD where PK_UNIONPAY_RECORD=?", 
													PayUnionpayRecord.class, new Object[]{pkUnionpayRecord});
			if(pur==null){
				throw new BusException("银联预交易记录【"+pkUnionpayRecord+"】不存在！");
			}
			
			String transType = paramMap.get("transtype");
			if(!transType.equals(pur.getTranstype())){
				throw new BusException("交易类型不一致，预交易类型【"+pur.getTranstype()+"】实际交易类型【"+transType+"】！");
			}
			
			BigDecimal totalFee = new BigDecimal(paramMap.get("amount")).divide(new BigDecimal(100));// 从分计算为元
			if(!"31".equals(transType)){
				totalFee = totalFee.multiply(new BigDecimal(-1));// 转为负数
			}
			if(totalFee.compareTo(pur.getAmount())!=0){
				throw new BusException("交易金额不一致，预交易金额【"+totalFee.toString()+"】实际交易金额【"+pur.getAmount().toString()+"】！");
			}
			
			String originTraceNo = paramMap.get("origintraceno");//撤销时返回
			String originSysrefNo = paramMap.get("originSysrefno");//退货时返回
			
			PayUnionpayRecord purOrig = null;
			BlExtPay bep = null;
			if(!"31".equals(transType)){
				String unionSql="";
				Object[] params = null;
				if("41".equals(transType)){
					// 银联交易记录查询
					unionSql = "select * from PAY_UNIONPAY_RECORD where SYSTRACDNO=?";
					params = new Object[]{originTraceNo};
				}else{
					// 银联交易记录查询
					unionSql = "select * from PAY_UNIONPAY_RECORD where SYSREFNO=?";
					params = new Object[]{originSysrefNo.substring(0,12)};
				}
				purOrig = DataBaseHelper.queryForBean(unionSql, PayUnionpayRecord.class, params);
				if(purOrig!=null){
					// 外部支付记录查询
					String bepSql = "select * from BL_EXT_PAY where PK_EXTPAY=?";
					bep = DataBaseHelper.queryForBean(bepSql, BlExtPay.class, new Object[]{purOrig.getPkExtpay()});
				}
			}

			// 保存外部接口-支付记录
			BlExtPay blExtPay = new BlExtPay();
			blExtPay.setPkPi(pur.getPkPi());
			blExtPay.setPkPv(pur.getPkPv());
			blExtPay.setAmount(totalFee);
			if("31".equals(transType)){
				blExtPay.setDescPay("银联缴费");
				blExtPay.setTradeNo(paramMap.get("sysrefno")+paramMap.get("transdate").substring(4,8));
			}else {
				if ("41".equals(transType)){
					blExtPay.setDescPay("银联撤销");
					if(bep!=null){
						blExtPay.setRefundNo(bep.getTradeNo());
					}else{
						blExtPay.setRefundNo(originTraceNo);
					}
				}else if ("71".equals(transType)){
					blExtPay.setDescPay("银联退货");
					if(bep!=null){
						blExtPay.setRefundNo(bep.getTradeNo());
					}else{
						blExtPay.setRefundNo(originSysrefNo);
					}
				}
				blExtPay.setTradeNo(paramMap.get("sysrefno"));
			} 
			blExtPay.setEuPaytype("3");
			blExtPay.setDtBank(paramMap.get("dtBank"));
			blExtPay.setNameBank(paramMap.get("nameBank"));
			blExtPay.setDatePay(DateUtils.strToDate(paramMap.get("transdate")+paramMap.get("transtime"), "yyyyMMddHHmmss"));
			blExtPay.setSerialNo(paramMap.get("systracdno"));
			blExtPay.setTradeNo(pur.getOutTradeNo());
			blExtPay.setEuBill("0");
			blExtPay.setFlagPay("1");
			blExtPay.setSysname(pur.getSystemModule());
			blExtPay.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
			blExtPay.setCreator(UserContext.getUser().getPkEmp());//当前操作人关联编码
			blExtPay.setCreateTime(new Date());
			blExtPay.setModifier(blExtPay.getCreator());
			blExtPay.setModityTime(blExtPay.getCreateTime());
			DataBaseHelper.insertBean(blExtPay);
			
			// 修改外部支付记录关联退款记录
			if(bep!=null){
				bep.setRefundNo(blExtPay.getTradeNo());
				DataBaseHelper.updateBeanByPk(bep);
			}
			
			// 保存银联交易记录
			pur.setPkExtpay(blExtPay.getPkExtpay());
			pur.setOperatetype(paramMap.get("operatetype"));
			pur.setCardtype(paramMap.get("cardtype"));
			pur.setResponsecode(paramMap.get("responsecode"));
			pur.setResponsemsg(paramMap.get("responsemsg"));
			pur.setCashregno(paramMap.get("cashregno"));
			pur.setCasherno(paramMap.get("casherno"));
			pur.setSelltenum(paramMap.get("selltenum"));
			pur.setMerchantid(paramMap.get("merchantid"));
			pur.setMerchantname(paramMap.get("merchantname"));
			pur.setTerminalid(paramMap.get("terminalid"));
			pur.setCardno(paramMap.get("cardno"));
			pur.setExpDate(paramMap.get("expDate"));
			pur.setBankno(paramMap.get("bankno"));
			pur.setTransdate(paramMap.get("transdate"));
			pur.setTranstime(paramMap.get("transtime"));
			pur.setAuthCode(paramMap.get("authCode"));
			pur.setSysrefno(paramMap.get("sysrefno"));
			pur.setCashtraceno(paramMap.get("cashtraceno"));
			pur.setOrigintraceno(originTraceNo);
			pur.setSystracdno(paramMap.get("systracdno"));
			pur.setOriginsystraceno(paramMap.get("originsystraceno"));
			pur.setReserved(paramMap.get("reserved"));
			pur.setTradeState("SUCCESS");
			if(!"31".equals(transType)){
				if(purOrig!=null){
					pur.setOriginSysrefno(purOrig.getSysrefno());
				}else if("41".equals(transType)){
					pur.setOriginSysrefno(originTraceNo);
				}else{
					pur.setOriginSysrefno(originSysrefNo.substring(0,12));
				}
			}
			pur.setModifier(UserContext.getUser().getCodeEmp());
			pur.setModityTime(new Date());
			DataBaseHelper.updateBeanByPk(pur);
			
			// 修改缴款记录关联退款记录
			if(purOrig!=null){
				purOrig.setOrigintraceno(pur.getSysrefno());
				DataBaseHelper.updateBeanByPk(purOrig);
			}
			
			returnMap.put("pkExtpay", blExtPay.getPkExtpay());
			returnMap.put("outTradeNo", blExtPay.getSerialNo());
			returnMap.put("code", "200");
			returnMap.put("msg", "保存银联交易记录");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("pkExtpay", "");
			returnMap.put("outTrandNo", "");
			returnMap.put("code", "404");
			returnMap.put("msg", "系统接口异常："+e.getLocalizedMessage());
		}
		return returnMap;
	}
	
	/**
	 * 生成预交金记录
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> newDeposit(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,String> returnMap = new HashMap<String,String>();
		try {
			User u = UserContext.getUser();
			Date date = new Date();
			
			String pkOrg = paramMap.get("pkOrg");//所属机构
			String pkPi = paramMap.get("pkPi");//患者主键
			String pkPv = paramMap.get("pkPv");//就诊主键
			String euDirect = paramMap.get("euDirect");//收退方向 1收 -1退
			String euDptype = paramMap.get("euDptype");//收付款类型
			String euPvtype = paramMap.get("euPvtype");//就诊类型
			//收付款方式 例如：1.现金；2.支票；3.银行卡；4.账户；5.内部转账；6.单位记账；7.微信；8.支付宝；99.其他
			String dtPaymode = paramMap.get("dtPaymode");
			BigDecimal amount = new BigDecimal(paramMap.get("amount"));//金额 
			//1.缴费、2.住院押金、3.账户充值
			String tradeType = paramMap.get("tradeType");//交易类型
			String datePay = paramMap.get("datePay");//收付款日期
			String pkDept = paramMap.get("pkDept");//收付款部门
			String pkEmpPay = paramMap.get("pkEmpPay");//收款人
			String nameEmpPay = paramMap.get("nameEmpPay");//收款人姓名
			boolean inserFlag=true;
			BlDeposit blDeposit=new BlDeposit();
			if(tradeType.equals("3")){
				inserFlag=false;
				//患者信息-账户表
				PiAcc pkc=DataBaseHelper.queryForBean("select * from pi_acc where pk_pi=?", PiAcc.class, pkPi);
				if(pkc.getEuStatus()==null||EnumerateParameter.ONE.equals(pkc.getEuStatus())){
					if(pkc.getAmtAcc()==null||"".equals(pkc.getAmtAcc())){
						pkc.setAmtAcc(BigDecimal.ZERO);
					}
					pkc.setAmtAcc(pkc.getAmtAcc().add(amount));
					DataBaseHelper.updateBeanByPk(pkc,false);
				}else{
					throw new BusException("该账户已冻结，不可充值");
				}
			}
			if(inserFlag){
				if("3".equals(euPvtype)){
					if(tradeType.equals("1")){
						//缴费
						blDeposit.setEuPvtype(euPvtype);//就诊类型
						blDeposit.setEuDptype(euDptype);//收付款类型
						blDeposit.setNote("住院缴费");
					}else if(tradeType.equals("2")){
						//押金
						blDeposit.setEuDptype("9");//住院预交金
						blDeposit.setEuPvtype("3");//住院
						blDeposit.setNote("住院预交金");
					}
				}else{
					blDeposit.setEuDptype("0");//门诊就诊结算
					blDeposit.setEuPvtype("1");//门诊
					blDeposit.setNote("门诊就诊结算");
				}	
				blDeposit.setDtPaymode(dtPaymode);//收付款方式
				blDeposit.setEuDirect(euDirect);//收退方向
				blDeposit.setAmount(amount.multiply(new BigDecimal(euDirect)));//金额
				blDeposit.setPkPv(pkPv);//就诊主键
				blDeposit.setPkPi(pkPi);//患者主键
				blDeposit.setPkOrg(pkOrg);//所属机构
				blDeposit.setPkEmpPay(pkEmpPay);//收款人
				blDeposit.setNameEmpPay(nameEmpPay);//收款人姓名
				blDeposit.setDatePay(DateUtils.parseDate(datePay));//收付款日期
				blDeposit.setPkDept(pkDept);//收付款部门
				switch (dtPaymode) {
					case "2":
					case "3":
						String dtBank = paramMap.get("dtBank");
						String bankNo = paramMap.get("bankNo");
						blDeposit.setDtBank(dtBank);// 对应银行
						blDeposit.setBankNo(bankNo);// 支票号码或银行卡号
						break;
					case "4":
						blDeposit.setFlagAcc(EnumerateParameter.ONE);
						String pkAcc = paramMap.get("pkAcc");
						blDeposit.setPkAcc(pkAcc);//账户主键
						break;
					default:
						break;
				}
				if(euDirect.equals("-1")){
					//退
					blDeposit.setDateReptBack(date);//票据收回日期
					blDeposit.setPkEmpBack(u.getPkEmp());//票据收回人员
					blDeposit.setNameEmpBack(u.getNameEmp());//票据收回人员姓名
				}else{
					//收
					List<Map<String, Object>> nextempinv = DataBaseHelper.queryForList("select * from bl_emp_invoice inv " +
							"where inv.del_flag = '0' and inv.pk_org = ? and inv.pk_emp_opera = ? " +
							"and inv.flag_active = '1' and inv.flag_use = '1' order by inv.date_opera",
							new Object[]{blDeposit.getPkOrg(), blDeposit.getPkEmpPay()});
					if (nextempinv == null || nextempinv.size() == 0) {
						//如果不存在，不处理
						//throw new BusException("没有可使用的票据了！");
					}else{		
						String pkinv = nextempinv.get(0).get("pkEmpinv").toString();
						String invPrefix = nextempinv.get(0).get("invPrefix").toString();
						String curNo = nextempinv.get(0).get("curNo").toString();
						blDeposit.setPkEmpinv(pkinv);//票据领用主键
						blDeposit.setReptNo(invPrefix+curNo);//收据编号
				    	commonService.confirmUseEmpInv(pkinv, (long)1);
					}
				}
				blDeposit.setCreator(UserContext.getUser().getPkEmp());//创建人
				blDeposit.setCreateTime(date);//创建时间
				blDeposit.setTs(date);//时间戳
				DataBaseHelper.insertBean(blDeposit);
			}
			returnMap.put("returnCode", "0");
		} catch (Exception e) {
			returnMap.put("returnCode", "1");
			throw new BusException("操作失败");
		}
		return returnMap;
	}

	
}