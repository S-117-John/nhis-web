package com.zebone.nhis.pro.zsba.mz.pub.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.mz.pub.dao.ZsbaBlOtherPayMapper;
import com.zebone.nhis.pro.zsba.mz.pub.vo.ZsbaBlExtPay;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病历本、托儿本等收费接口
 */
@Service
public class ZsbaBlOtherPayService {

	@Resource
	private ZsbaBlOtherPayMapper zsbaBlOtherPayMapper;

	/**
	 * 功能描述：新增费用订单表
	 * 交易号：022003027148
	 */
	public ZsbaBlExtPay blOtherPaySave(String params, IUser user) {
		ZsbaBlExtPay blExtPay = JsonUtil.readValue(params, ZsbaBlExtPay.class);
		if(CommonUtils.isEmptyString(blExtPay.getDescPay())) {
			blExtPay.setDescPay("门诊病历本收费");
		}
		blExtPay.setFlagBus(null);
		ApplicationUtils.setDefaultValue(blExtPay, true);
		User currUser = UserContext.getUser();
		//先插入交款表记录
		BlDeposit blDeposit = new BlDeposit();
		blDeposit.setEuDptype("0");//0就诊结算、4取消结算
		blDeposit.setEuDirect("1");//1收、-1退
		blDeposit.setAmount(blExtPay.getAmount());
		blDeposit.setDtPaymode(blExtPay.getEuPaytype());
		blDeposit.setDtBank((StringUtils.isNotEmpty(blExtPay.getDtBank()) && blExtPay.getDtBank().equals("cash"))?null:blExtPay.getDtBank());
		blDeposit.setBankNo(blExtPay.getCardNo());
		blDeposit.setPayInfo(blExtPay.getSerialNo());
		blDeposit.setDatePay(blExtPay.getDatePay());
		blDeposit.setPkDept(currUser.getPkDept());
		blDeposit.setPkEmpPay(currUser.getPkEmp());
		blDeposit.setNameEmpPay(currUser.getNameEmp());
		blDeposit.setNote(blExtPay.getBodyPay());
		blDeposit.setEuPvtype("1");//默认1门诊
		blDeposit.setFlagCc("0");
		blDeposit.setPkCc(null);
		ApplicationUtils.setDefaultValue(blDeposit, true);
		DataBaseHelper.insertBean(blDeposit);
		//再保存第三方订单记录
		blExtPay.setDtBank((StringUtils.isNotEmpty(blExtPay.getDtBank()) && blExtPay.getDtBank().equals("cash"))?null:blExtPay.getDtBank());
		blExtPay.setPkDepo(blDeposit.getPkDepo());
		DataBaseHelper.insertBean(blExtPay);
		return blExtPay;
	}
	
	/**
	 * 功能描述：退费接口
	 * 交易号：022003027149
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> refundBlOtherPay(String params, IUser user) {
		//退费入参：收费订单主键、退费数量、退费金额、操作人
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		User currUser = UserContext.getUser();
		if(paramMap.get("pkExtPay")==null || CommonUtils.isEmptyString(paramMap.get("pkExtPay").toString())){
			throw new BusException("收费结算主键【pkExtPay】不能为空");
		}
		if(paramMap.get("serialNo")==null || CommonUtils.isEmptyString(paramMap.get("serialNo").toString())){
			throw new BusException("退款单号【serialNo】不能为空");
		}
		if(paramMap.get("quan")==null || CommonUtils.isEmptyString(paramMap.get("quan").toString()) || Integer.parseInt(paramMap.get("quan").toString())<=0){
			throw new BusException("退费数量【quan】不能为空且必须大于0");
		}
		if(paramMap.get("amount")==null || CommonUtils.isEmptyString(paramMap.get("amount").toString()) || (new BigDecimal(paramMap.get("amount").toString())).compareTo(BigDecimal.ZERO)==1){
			throw new BusException("退费金额【amount】不能为空且不能为0");
		}
		//查询收费订单记录
		String pkExtPay = CommonUtils.getString(paramMap.get("pkExtPay"));
		ZsbaBlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where PK_EXTPAY=?", ZsbaBlExtPay.class, new Object[]{pkExtPay});
		if(blExtPay==null) {
			throw new BusException("找不到该退费订单对应的收费订单记录！");
		}
		if(CommonUtils.isNotNull(blExtPay.getRefundNo())) {
			throw new BusException("该订单为已退费记录不能重复操作退费！");
		}
		BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from BL_DEPOSIT where PK_DEPO=?", BlDeposit.class, new Object[]{blExtPay.getPkDepo()});
		if(blDeposit==null) {
			throw new BusException("找不到该退费订单对应的交款记录！");
		}
		Date currDate = new Date();
		//复制一份交款表订单记录全退冲负
		BlDeposit cancelBlDeposit = new BlDeposit();
		ApplicationUtils.copyProperties(cancelBlDeposit, blDeposit);
		cancelBlDeposit.setAmount(new BigDecimal(-1).multiply(cancelBlDeposit.getAmount()));
		cancelBlDeposit.setEuDptype("4");//0就诊结算、4取消结算
		cancelBlDeposit.setEuDirect("-1");//1收、-1退
		cancelBlDeposit.setFlagCc("0");
		cancelBlDeposit.setPkCc(null);
		cancelBlDeposit.setPkDepoBack(blDeposit.getPkDepo());
		cancelBlDeposit.setPkEmpPay(currUser.getPkEmp());
		cancelBlDeposit.setNameEmpPay(currUser.getNameEmp());
		ApplicationUtils.setDefaultValue(cancelBlDeposit, true);
		cancelBlDeposit.setDatePay(currDate);
		DataBaseHelper.insertBean(cancelBlDeposit);
		//复制一份第三方订单记录全退冲负
		ZsbaBlExtPay cancelBlExtPay = new ZsbaBlExtPay();
		ApplicationUtils.copyProperties(cancelBlExtPay, blExtPay);
		cancelBlExtPay.setAmount(new BigDecimal(-1).multiply(cancelBlExtPay.getAmount()));
		cancelBlExtPay.setQuan((-1)*cancelBlExtPay.getQuan());
		cancelBlExtPay.setDescPay("门诊病历本退费");
		cancelBlExtPay.setSerialNo(paramMap.get("serialNo").toString());
		cancelBlExtPay.setRefundNo(blExtPay.getSerialNo());
		cancelBlExtPay.setDateRefund(new Date());
		cancelBlExtPay.setFlagBus(null);
		cancelBlExtPay.setPkDepo(cancelBlDeposit.getPkDepo());
		ApplicationUtils.setDefaultValue(cancelBlExtPay, true);
		cancelBlExtPay.setDatePay(currDate);
		//更新收费订单关联退款单号
		String updateExtPaySql = "update BL_EXT_PAY set REFUND_NO=? where PK_EXTPAY=?";
		DataBaseHelper.execute(updateExtPaySql, cancelBlExtPay.getSerialNo(), pkExtPay);
		//存在剩余数量收费则插入重收记录
		Integer refundQuan = Integer.parseInt(paramMap.get("quan").toString());//入参正数数量
		BigDecimal refundAmount = new BigDecimal(paramMap.get("amount").toString());//入参负数金额
		BlDeposit newBlDeposit = null;
		ZsbaBlExtPay newBlExtPay = null;
		if(blExtPay.getQuan()-refundQuan>0) {
			//生成部分退重收的交款记录
			newBlDeposit = new BlDeposit();
			newBlDeposit.setEuDptype("0");//0就诊结算、4取消结算
			newBlDeposit.setEuDirect("1");//1收、-1退
			newBlDeposit.setAmount(blExtPay.getAmount().subtract(refundAmount.multiply(new BigDecimal(-1))).setScale(2, BigDecimal.ROUND_HALF_UP));
			newBlDeposit.setDtPaymode(blExtPay.getEuPaytype());
			newBlDeposit.setDtBank(blExtPay.getDtBank());
			newBlDeposit.setBankNo(blExtPay.getCardNo());
			newBlDeposit.setPayInfo(blExtPay.getSerialNo());
			newBlDeposit.setDatePay(currDate);
			newBlDeposit.setPkDept(currUser.getPkDept());
			newBlDeposit.setPkEmpPay(currUser.getPkEmp());
			newBlDeposit.setNameEmpPay(currUser.getNameEmp());
			newBlDeposit.setNote(blExtPay.getBodyPay());
			newBlDeposit.setEuPvtype("1");//默认1门诊
			newBlDeposit.setFlagCc("0");
			newBlDeposit.setPkCc(null);
			ApplicationUtils.setDefaultValue(newBlDeposit, true);
			DataBaseHelper.insertBean(newBlDeposit);
			//生成部分退重收的第三方订单记录
			newBlExtPay = new ZsbaBlExtPay();
			ApplicationUtils.copyProperties(newBlExtPay, blExtPay);
			newBlExtPay.setAmount(newBlDeposit.getAmount());
			newBlExtPay.setQuan(blExtPay.getQuan()-refundQuan);
			newBlExtPay.setDescPay("门诊病历本收费");
			newBlExtPay.setRefundNo(null);
			newBlExtPay.setDateRefund(null);
			newBlExtPay.setFlagBus(null);
			ApplicationUtils.setDefaultValue(newBlExtPay, true);
			newBlExtPay.setPkDepo(newBlDeposit.getPkDepo());
			newBlExtPay.setDatePay(currDate);
			DataBaseHelper.insertBean(newBlExtPay);
			//全退冲负记录关联重结主键
			cancelBlExtPay.setPkOtherpayRecharge(newBlExtPay.getPkExtPay());
		}
		//插入第三方订单全退冲负记录
		DataBaseHelper.insertBean(cancelBlExtPay);
		//退费完成后返回数据
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("serialNo", blExtPay.getSerialNo());//收费订单号
		returnMap.put("amount", blExtPay.getAmount());//收费金额
		returnMap.put("refundAmount", refundAmount);//退费金额
		returnMap.put("rechargeAmount", newBlExtPay!=null?newBlExtPay.getAmount():0);//重收金额
		return returnMap;
	}
	
	
	/**
	 * 查询收费订单列表
	 * 交易号：022003027150
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryBlOtherPayList(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = zsbaBlOtherPayMapper.queryBlOtherPayList(paramMap);
		return list;
	}
	
	/**
	 * 查询病历本退费明细
	 * 交易号：022003027153
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryBlOtherPayRefundDetail(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = zsbaBlOtherPayMapper.queryBlOtherPayRefundDetail(paramMap);
		return list;
	}
	
	/**
	 * 查询病历本收费小计金额
	 * 交易号：022003027152
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryBlOtherPayReport(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = zsbaBlOtherPayMapper.queryBlOtherPayReport(paramMap);
		for(Map<String,Object> map : list) {
			BigDecimal cashBigDecimal = new BigDecimal(String.valueOf(map.get("caseAmount")));
			BigDecimal bankBigDecimal = new BigDecimal(String.valueOf(map.get("bankAmount")));
			BigDecimal wechatBigDecimal = new BigDecimal(String.valueOf(map.get("wechatAmount")));
			BigDecimal alipayBigDecimal = new BigDecimal(String.valueOf(map.get("alipayAmount")));
			BigDecimal amountBigDecimal = cashBigDecimal.add(bankBigDecimal).add(wechatBigDecimal).add(alipayBigDecimal);
			map.put("amount", amountBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		return list;
	}
	
	/**
	 * 病历本日结结账
	 */
	@SuppressWarnings("unchecked")
	public void blOtherPayDayEnd(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkCc")==null || CommonUtils.isEmptyString(paramMap.get("pkCc").toString())){
			throw new BusException("结账主键【pkCc】不能为空");
		}
		if(paramMap.get("pkEmp")==null || CommonUtils.isEmptyString(paramMap.get("pkEmp").toString())){
			throw new BusException("操作员主键【pkEmp】不能为空");
		}
		if(paramMap.get("endDate")==null || CommonUtils.isEmptyString(paramMap.get("endDate").toString())){
			throw new BusException("日结截止时间【endDate】不能为空");
		}
		//操作员结账
		zsbaBlOtherPayMapper.blOtherPayDayEnd(paramMap);
	}
	
	/**
	 * 迁移病历本收费数据
	 * 交易号：022003027155
	 */
	public void moveBlbPayData(String param, IUser user) {
		System.out.println("========>>迁移病历本收费数据开始："+DateUtils.getDateTime());
		String minPayTime = "2021-06-23 10:22:25";//最早病历本收费时间
		//查询所有收过病历本的收费员人员主键
		String qryEmpSql = "select b.PK_EMP pkemp,b.NAME_EMP nameemp from BL_OTHER_PAY a " + 
			"INNER JOIN BD_OU_EMPLOYEE b on b.PK_EMP=a.CREATOR " + 
			"group by b.PK_EMP,b.NAME_EMP order by b.NAME_EMP";
		List<Map<String, Object>> empList = DataBaseHelper.queryForList(qryEmpSql, new Object[]{});
		for(Map<String, Object> empMap : empList) {
			String pkEmp = empMap.get("pkemp").toString();
			String nameEmp = empMap.get("nameemp").toString();
			System.out.println("迁移病历本收费员开始："+pkEmp+"<<==>>"+nameEmp);
			//获取收费员日结记录范围在病历本最早收费时间以后的日结数据
			List<BlCc> ccList = DataBaseHelper.queryForList("select * from bl_cc where date_end>=? and pk_emp_opera=? order by date_cc", BlCc.class, new Object[]{minPayTime,pkEmp});
			/**一、通过日结记录的开始和结束日期迁移病历本收费记录并关联日结主键**/
			for(BlCc cc : ccList) {
				String startDate = DateUtils.formatDate(cc.getDateBegin(), "yyyy-MM-dd HH:mm:ss");
				String endDate = DateUtils.formatDate(cc.getDateEnd(), "yyyy-MM-dd HH:mm:ss");
				System.out.println("迁移病历本收费员日结开始："+cc.getPkCc()+"<<==>>"+startDate+"<<==>>"+endDate);
				//1.迁移到第三方订单表
				String moveExtSql = "insert into BL_EXT_PAY(PK_EXTPAY,PK_ORG,AMOUNT,EU_PAYTYPE,DT_BANK,NAME_BANK,DESC_PAY,FLAG_PAY," + 
					"DATE_PAY,TRADE_NO,SERIAL_NO,SYSNAME,REFUND_NO,DATE_REFUND,CREATOR,CREATE_TIME,MODIFIER,MODITY_TIME," + 
					"DEL_FLAG,TS,CARD_NO,BODY_PAY,PRICE,QUAN,PK_OTHERPAY_RECHARGE,PK_DEPO) " + 
					"select PK_OTHERPAY as PK_EXTPAY,PK_ORG,AMOUNT,EU_PAYTYPE,CASE when DT_BANK='cash' then NULL else DT_BANK end as DT_BANK,NAME_BANK," + 
					"CASE when DESC_PAY='门诊收费|病历本|托儿本' THEN '门诊病历本收费' else '门诊病历本退费' end as DESC_PAY," + 
					"FLAG_PAY,CREATE_TIME as DATE_PAY,TRADE_NO,SERIAL_NO,SYSNAME,REFUND_NO,DATE_REFUND,CREATOR,CREATE_TIME," + 
					"MODIFIER,MODITY_TIME,DEL_FLAG,TS,CARD_NO,CHARGE_NAME as BODY_PAY,PRICE,QUAN,PK_OTHERPAY_RECHARGE,PK_OTHERPAY as PK_DEPO from BL_OTHER_PAY " + 
					"where CREATOR=? and DEL_FLAG='0' and CHARGE_CODE is null and CREATE_TIME>=? and CREATE_TIME<=?";
				Integer extSize = DataBaseHelper.execute(moveExtSql, new Object[]{pkEmp,startDate,endDate});
				System.out.println("迁移病历本收费记录[已日结]到第三方表数量为："+extSize);
				//2.迁移到交款记录表
				String moveDepositSql = "insert into BL_DEPOSIT(PK_DEPO,PK_ORG,EU_DPTYPE,EU_DIRECT,AMOUNT,DT_PAYMODE,DT_BANK,BANK_NO,PAY_INFO," + 
					"DATE_PAY,PK_EMP_PAY,NAME_EMP_PAY,NOTE,EU_PVTYPE,CREATOR,CREATE_TIME,MODIFIER,DEL_FLAG,TS,FLAG_CC,PK_CC) " + 
					"select PK_OTHERPAY as PK_DEPO,PK_ORG,CASE WHEN AMOUNT>0 then '0' else '4' end as EU_DPTYPE," + 
					"CASE WHEN AMOUNT>0 then '1' else '-1' end as EU_DIRECT," + 
					"AMOUNT,EU_PAYTYPE as DT_PAYMODE,CASE when DT_BANK='cash' then NULL else DT_BANK end as DT_BANK,CARD_NO as BANK_NO," + 
					"SERIAL_NO as PAY_INFO,CREATE_TIME as DATE_PAY,CREATOR as PK_EMP_PAY,'"+nameEmp+"' as NAME_EMP_PAY,CHARGE_NAME as NOTE,'1' EU_PVTYPE," + 
					"CREATOR,CREATE_TIME,MODIFIER,DEL_FLAG,TS,'1' FLAG_CC,'"+cc.getPkCc()+"' PK_CC from BL_OTHER_PAY " + 
					"where CREATOR=? and DEL_FLAG='0' and CHARGE_CODE is null and CREATE_TIME>=? and CREATE_TIME<=?";
				Integer depositSize = DataBaseHelper.execute(moveDepositSql, new Object[]{pkEmp,startDate,endDate});
				System.out.println("迁移病历本收费记录[已日结]到交款表数量为："+depositSize);
				//3.更新病历本收费记录表的charge_code(随便赋个值不为空即可)避免执行此任务重复处理已迁移过的数据
				String updateOtherSql = "update BL_OTHER_PAY set CHARGE_CODE='1' where CREATOR=? and DEL_FLAG='0' and CHARGE_CODE is null and CREATE_TIME>=? and CREATE_TIME<=?";
				Integer otherSize = DataBaseHelper.execute(updateOtherSql, new Object[]{pkEmp,startDate,endDate});
				System.out.println("更新病历本收费记录[已日结]状态数量为："+otherSize);
			}
			
			/**二、迁移收费员日结记录外未日结范围内的数据**/
			//1.迁移到第三方订单表
			String moveExtSql = "insert into BL_EXT_PAY(PK_EXTPAY,PK_ORG,AMOUNT,EU_PAYTYPE,DT_BANK,NAME_BANK,DESC_PAY,FLAG_PAY," + 
				"DATE_PAY,TRADE_NO,SERIAL_NO,SYSNAME,REFUND_NO,DATE_REFUND,CREATOR,CREATE_TIME,MODIFIER,MODITY_TIME," + 
				"DEL_FLAG,TS,CARD_NO,BODY_PAY,PRICE,QUAN,PK_OTHERPAY_RECHARGE,PK_DEPO) " + 
				"select PK_OTHERPAY as PK_EXTPAY,PK_ORG,AMOUNT,EU_PAYTYPE,CASE when DT_BANK='cash' then NULL else DT_BANK end as DT_BANK,NAME_BANK," + 
				"CASE when DESC_PAY='门诊收费|病历本|托儿本' THEN '门诊病历本收费' else '门诊病历本退费' end as DESC_PAY," + 
				"FLAG_PAY,CREATE_TIME as DATE_PAY,TRADE_NO,SERIAL_NO,SYSNAME,REFUND_NO,DATE_REFUND,CREATOR,CREATE_TIME," + 
				"MODIFIER,MODITY_TIME,DEL_FLAG,TS,CARD_NO,CHARGE_NAME as BODY_PAY,PRICE,QUAN,PK_OTHERPAY_RECHARGE,PK_OTHERPAY as PK_DEPO from BL_OTHER_PAY " + 
				"where CREATOR=? and DEL_FLAG='0' and CHARGE_CODE is null";
			Integer extSize = DataBaseHelper.execute(moveExtSql, new Object[]{pkEmp});
			System.out.println("迁移病历本收费记录[未日结]到第三方表数量为："+extSize);
			//2.迁移到交款记录表
			String moveDepositSql = "insert into BL_DEPOSIT(PK_DEPO,PK_ORG,EU_DPTYPE,EU_DIRECT,AMOUNT,DT_PAYMODE,DT_BANK,BANK_NO,PAY_INFO," + 
				"DATE_PAY,PK_EMP_PAY,NAME_EMP_PAY,NOTE,EU_PVTYPE,CREATOR,CREATE_TIME,MODIFIER,DEL_FLAG,TS,FLAG_CC,PK_CC) " + 
				"select PK_OTHERPAY as PK_DEPO,PK_ORG,CASE WHEN AMOUNT>0 then '0' else '4' end as EU_DPTYPE," + 
				"CASE WHEN AMOUNT>0 then '1' else '-1' end as EU_DIRECT," + 
				"AMOUNT,EU_PAYTYPE as DT_PAYMODE,CASE when DT_BANK='cash' then NULL else DT_BANK end as DT_BANK,CARD_NO as BANK_NO," + 
				"SERIAL_NO as PAY_INFO,CREATE_TIME as DATE_PAY,CREATOR as PK_EMP_PAY,'"+nameEmp+"' as NAME_EMP_PAY,CHARGE_NAME as NOTE,'1' EU_PVTYPE," + 
				"CREATOR,CREATE_TIME,MODIFIER,DEL_FLAG,TS,'0' FLAG_CC,NULL PK_CC from BL_OTHER_PAY " + 
				"where CREATOR=? and DEL_FLAG='0' and CHARGE_CODE is null";
			Integer depositSize = DataBaseHelper.execute(moveDepositSql, new Object[]{pkEmp});
			System.out.println("迁移病历本收费记录[未日结]到交款表数量为："+depositSize);
			//3.更新病历本收费记录表的charge_code(随便赋个值不为空即可)避免执行此任务重复处理已迁移过的数据
			String updateOtherSql = "update BL_OTHER_PAY set CHARGE_CODE='1' where CREATOR=? and DEL_FLAG='0' and CHARGE_CODE is null";
			Integer otherSize = DataBaseHelper.execute(updateOtherSql, new Object[]{pkEmp});
			System.out.println("更新病历本收费记录[未日结]状态数量为："+otherSize);
		}
		System.out.println("========>>迁移病历本收费数据结束："+DateUtils.getDateTime());
	}
	
	/**
	 * 更新交款表病历本记录日结范围内未日结的记录的日结状态
	 * 交易号：022003027157
	 */
	public void updateBlbCcStatus(String param, IUser user) {
		System.out.println("========>>更新病历本收费记录日结状态开始："+DateUtils.getDateTime());
		String minPayTime = "2021-06-23 10:22:25";//最早病历本收费时间
		//查询所有收过病历本的收费员人员主键
		String qryEmpSql = "select b.PK_EMP pkemp,b.NAME_EMP nameemp from BL_DEPOSIT a " + 
			"INNER JOIN BD_OU_EMPLOYEE b on b.PK_EMP=a.PK_EMP_PAY " + 
			"where a.NOTE in ('病历本','托儿本') " + 
			"group by b.PK_EMP,b.NAME_EMP order by b.NAME_EMP";
		List<Map<String, Object>> empList = DataBaseHelper.queryForList(qryEmpSql, new Object[]{});
		Integer sumDataSize = 0;
		for(Map<String, Object> empMap : empList) {
			String pkEmp = empMap.get("pkemp").toString();
			String nameEmp = empMap.get("nameemp").toString();
			System.out.println("更新收费员病历本收费记录日结状态开始："+pkEmp+"<<==>>"+nameEmp);
			//获取收费员日结记录范围在病历本最早收费时间以后的日结数据
			List<BlCc> ccList = DataBaseHelper.queryForList("select * from bl_cc where date_end>=? and pk_emp_opera=? order by date_cc", BlCc.class, new Object[]{minPayTime,pkEmp});
			/**一、通过日结记录的开始和结束日期更新该范围内未日结的记录的日结标志并关联该日结主键**/
			for(BlCc cc : ccList) {
				String startDate = DateUtils.formatDate(cc.getDateBegin(), "yyyy-MM-dd HH:mm:ss");
				String endDate = DateUtils.formatDate(cc.getDateEnd(), "yyyy-MM-dd HH:mm:ss");
				System.out.println("更新病历本收费记录日结状态开始："+cc.getPkCc()+"<<==>>"+startDate+"<<==>>"+endDate);
				
				String qryOtherSql = "select * from BL_DEPOSIT where PK_EMP_PAY=? and NOTE in ('病历本','托儿本') and DEL_FLAG='0' and FLAG_CC='0' and DATE_PAY>=? and DATE_PAY<=?";
				List<Map<String, Object>> dataList = DataBaseHelper.queryForList(qryOtherSql, new Object[]{pkEmp,startDate,endDate});
				System.out.println("更新病历本收费记录日结状态数量为："+dataList.size());
				sumDataSize += dataList.size();
				
				String updateOtherSql = "update BL_DEPOSIT set FLAG_CC='1',PK_CC=? where PK_EMP_PAY=? and NOTE in ('病历本','托儿本') and DEL_FLAG='0' and FLAG_CC='0' and DATE_PAY>=? and DATE_PAY<=?";
				Integer updateSize = DataBaseHelper.execute(updateOtherSql, new Object[]{cc.getPkCc(),pkEmp,startDate,endDate});
				System.out.println("更新病历本收费记录日结状态数量为："+updateSize);
			}
		}
		System.out.println("病历本更新日结状态数据量为："+sumDataSize);
		System.out.println("========>>更新病历本收费记录日结状态结束："+DateUtils.getDateTime());
	}
}
