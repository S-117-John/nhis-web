package com.zebone.nhis.pro.zsba.mz.pub.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.up.service.TpPayService;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 医保个帐数据存储到第三方表、付款记录表
 * @author Administrator
 * 
 */
@Service
public class ZsbaYbPosService {
	
	@Autowired TpPayService payService;
	
	/**
	 * 生成第三方交易记录
	 * @param posTrans
	 * @return
	 */
	public String saveYbPosToExtPay(JSONObject posTrans){
		String codeEmp = posTrans.getString("codeEmp");
		User emp = null;
		// 兼容自助机、公众号不登录调用接口服务时，取不到登录人信息的问题
		if(StringUtils.isNotEmpty(codeEmp) && codeEmp.startsWith("999")){
			emp = payService.getByCodeEmp(codeEmp);
		}else{
			emp = UserContext.getUser();
		}
		
		BlExtPay blExtPay=new BlExtPay();
		blExtPay.setSerialNo(posTrans.getString("tradeNo"));//商户订单号
		blExtPay.setTradeNo(NHISUUID.getKeyId());//交易号，用于对账
		blExtPay.setAmount(new BigDecimal(posTrans.getString("amount")));
		blExtPay.setEuPaytype(posTrans.getString("payType"));//个帐：13.二维码、14.实体卡
		blExtPay.setDtBank("0");
		blExtPay.setDescPay("门诊结算-医保个帐");
		blExtPay.setSysname("mz");
		blExtPay.setDateAp(new Date());//请求时间
		blExtPay.setFlagPay("1");//支付标志 未完成
		blExtPay.setPkPi(posTrans.getString("pkPi"));//患者主键
		blExtPay.setPkPv(posTrans.getString("pkPv"));//就诊主键
		blExtPay.setPkOrg("89ace0e12aba439991c0eb001aaf02f7");//当前操作人关联机构
		blExtPay.setCreator(emp.getPkEmp());//当前操作人关联编码
		blExtPay.setCreateTime(new Date());
		blExtPay.setModifier(blExtPay.getCreator());
		blExtPay.setModityTime(blExtPay.getCreateTime());
		blExtPay.setTs(blExtPay.getCreateTime());
		DataBaseHelper.insertBean(blExtPay);
		
		// 1.生成预交金记录
		ZsbaBlDeposit depo = new ZsbaBlDeposit();
		depo.setPkPv(blExtPay.getPkPv());	//就诊主键
		depo.setPkPi(blExtPay.getPkPi());	//患者主键
		depo.setEuPvtype("1");				//就诊类型-门诊
		depo.setEuDptype("0");				//收付款类型-门诊就诊结算
		depo.setNote("门诊结算-医保个帐");
		depo.setEuDirect("1");				//收退方向-收
		depo.setFlagSettle("0");			//未结算
		depo.setVoidType("0");				//正常
		depo.setAmount(blExtPay.getAmount());//金额
		depo.setDtPaymode(blExtPay.getEuPaytype());
		depo.setDtBank(blExtPay.getDtBank());
		depo.setPayInfo(blExtPay.getSerialNo());
		depo.setPkOrg(blExtPay.getPkOrg());	//所属机构
		depo.setPkEmpPay(blExtPay.getCreator());//收款人
		depo.setNameEmpPay(emp.getNameEmp());//收款人姓名
		depo.setCreator(blExtPay.getCreator());//收款人
		depo.setModifier(blExtPay.getCreator());//收款人
		depo.setCodeDepo(ApplicationUtils.getCode("0606"));//交款编码
		depo.setDatePay(blExtPay.getCreateTime());//收付款日期
		depo.setTs(new Date());//时间戳
		DataBaseHelper.insertBean(depo);
		
		//关联付款表+结算表
		blExtPay.setPkDepo(depo.getPkDepo());
		blExtPay.setTs(new Date());
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay.getPkExtpay();
	}
	
	/**
	 * 生成付款记录 并关联结算
	 * @param pkExtPay
	 * @param pkSettle
	 * @return
	 */
	public String saveDepoByExtPay(String pkExtPay, String pkSettle, String insSettId){
		String pkDepo = "";
		BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where PK_EXTPAY=?", BlExtPay.class, new Object[]{pkExtPay});
		if(blExtPay!=null){
			//更新第三方交易数据
			String extSql = "update BL_EXT_PAY set pk_settle=? where PK_PV=? and pk_settle is null";
			DataBaseHelper.execute(extSql, pkSettle, blExtPay.getPkPv());
			
			//更新付款数据
			String depoSql = "update BL_DEPOSIT set pk_settle=? where PK_PV=? and pk_settle is null";
			DataBaseHelper.execute(depoSql, pkSettle, blExtPay.getPkPv());
			
			//更新医保交易数据
			String posSql = "update pay_pos_tr set pk_settle=? where PK_PV=? and pk_settle is null";
			DataBaseHelper.execute(posSql, pkSettle, blExtPay.getPkPv());
			
			//更新医保个帐金额
			if(StringUtils.isNotEmpty(insSettId)){
				String sql = "";
				if(insSettId.startsWith("gs")){
					sql = "update ins_sgsyb_st set amt_grzhzf=?, pk_settle=? where PK_PV=? and yb_pksettle=?";
				}else{
					sql = "update ins_qgyb_st  set amt_grzhzf=?, pk_settle=? where PK_PV=? and yb_pksettle=?";
				}
				DataBaseHelper.execute(sql, blExtPay.getAmount(), pkSettle, blExtPay.getPkPv(), insSettId);
			}
			pkDepo = blExtPay.getPkDepo();
		}
		return pkDepo;
	}
	
	/**
	 * 删除第三方交易记录
	 * @param pkExtPay
	 * @return
	 */
	public Integer deleteExtPay(String pkExtPay){
		int num = 0;
		String delDepoSql = "delete from BL_DEPOSIT where pk_depo=(select pk_depo from BL_EXT_PAY where pk_extpay=?)";
		num += DataBaseHelper.execute(delDepoSql, pkExtPay);
		String delExtSql  = "delete from BL_EXT_PAY where pk_extpay=?";
		num += DataBaseHelper.execute(delExtSql, pkExtPay);
		return num;
	}
	
	/**
	 * 门诊取消结算-第三方退费失败-后台自动退费-返回个帐及银联数据
	 * 022003027120
	 * @return
	 */
	public List<Map<String, Object>> refundExrPay(String param,IUser user){
		List<Map<String, Object>> retMap = new ArrayList<Map<String,Object>>();
		
		JSONArray extpats = JSONArray.fromObject(param);
		
		for(int i=0; i<extpats.size(); i++){
			JSONObject extObj = extpats.getJSONObject(i);
			String pkExtpay = extObj.getString("pkExtpay");
			String refundAmt = extObj.getString("refundAmt");
			List<Map<String, Object>> extpatList = getExtpay(pkExtpay);
			if(extpatList.isEmpty()){
				continue;
			}
			
			Map<String, Object> map = extpatList.get(0);
			if("7".equals(map.get("euPaytype").toString())){//微信在线自动退
				JSONObject refundJson = new JSONObject();
				refundJson.put("refundReason", "取消结算");
				PayWechatRecord ar = DataBaseHelper.queryForBean("select * from PAY_WECHAT_RECORD where ORDER_TYPE='pay' and PK_EXTPAY=?", PayWechatRecord.class, pkExtpay);
				if(ar!=null){
					try {
						refundJson.put("refundType", "1");
						refundJson.put("tradeNo", ar.getOutTradeNo());
						refundJson.put("refundAmount", refundAmt);
						refundJson.put("isCreateExtDepoRefund", "false");
						payService.payRefund(refundJson.toString(), user);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}else if("8".equals(map.get("euPaytype").toString())){//支付宝在线自动退
					JSONObject refundJson = new JSONObject();
					refundJson.put("refundReason", "取消结算");
					PayAliRecord ar = DataBaseHelper.queryForBean("select * from PAY_ALI_RECORD where ORDER_TYPE='pay' and PK_EXTPAY=?", PayAliRecord.class, pkExtpay);
					if(ar!=null){
						try {
							refundJson.put("refundType", "2");
							refundJson.put("tradeNo", ar.getOutTradeNo());
							refundJson.put("refundAmount", refundAmt);
							refundJson.put("isCreateExtDepoRefund", "false");
							payService.payRefund(refundJson.toString(), user);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
			}else{
				map.put("amount", refundAmt);
				retMap.add(map);
			}
		}
		return retMap;
	}
	private List<Map<String, Object>> getExtpay(String pkExtpay){
		StringBuffer sql = new StringBuffer("SELECT");
		sql.append(" CONVERT(VARCHAR(10), ext.DATE_PAY, 120) DATE_PAY, pi.NAME_PI, ");
		sql.append(" CASE WHEN ext.EU_PAYTYPE='14' THEN ISNULL(pi.THIRD_SOCIAL_NO, pi.INSUR_NO) WHEN ext.EU_PAYTYPE='3' THEN ISNULL(ext.CARD_NO, '')  ELSE '' end card_no, ");//返回社保卡、银行卡的卡号
		sql.append(" ext.AMOUNT, ext.SERIAL_NO, ext.PK_EXTPAY, ext.EU_PAYTYPE, ");
		sql.append(" CASE ext.EU_PAYTYPE WHEN '13' THEN '个帐(电子卡)' WHEN '14' THEN '个帐(实体卡)' WHEN '3' THEN '自费(银行卡)'  WHEN '7' THEN '自费(支付宝)'  WHEN '8' THEN '自费(支付宝)' ELSE '其它' END AS pay_mod ");
		sql.append(" FROM ");
		sql.append(" BL_EXT_PAY ext ");
		sql.append(" LEFT JOIN PI_MASTER pi on pi.pk_pi=ext.PK_PI ");
		sql.append(" WHERE ");
		sql.append(" ext.PK_EXTPAY = '"+pkExtpay+"' ");
		
		return DataBaseHelper.queryForList(sql.toString());
	}
	
	/**
	 * 根据就诊主键获取主索引信息
	 * @param pkPv
	 * @return
	 */
	public Map<String, Object> getPiMasterByPkPv(String pkPv){
		String sql = "SELECT pi.PK_PI, pi.ID_NO, pi.DT_IDTYPE FROM PV_ENCOUNTER pv INNER JOIN PI_MASTER pi ON pi.PK_PI = pv.PK_PI WHERE pv.PK_PV=?";
		return DataBaseHelper.queryForMap(sql, pkPv);
	}
	
}
