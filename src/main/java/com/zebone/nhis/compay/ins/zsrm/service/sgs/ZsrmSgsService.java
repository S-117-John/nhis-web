package com.zebone.nhis.compay.ins.zsrm.service.sgs;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.compay.ins.zsrm.dao.ZsrmSgsMapper;
import com.zebone.nhis.compay.ins.zsrm.vo.*;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ZsrmSgsService {

	private Logger logger = LoggerFactory.getLogger("nhis.ZsrmSGSLog");

	@Value("#{applicationProperties['sgsyb.rejected_url']}")
	private String REJECTED_URL;
	@Value("#{applicationProperties['sgsyb.userid']}")
	private String INSUSERID;
	@Value("#{applicationProperties['sgsyb.password']}")
	private String PASSWORD;
	@Value("#{applicationProperties['sgsyb.medical_institution_code']}")
	private String MEDICAL_INSTITUTION_CODE;

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	@Resource
	private ZsrmSgsMapper zsrmSgsMapper;

	/**
	 * @param params
	 *            015001013002 门诊收费预结算
	 */
	public Map<String, Object> mzHpHuaJia(String params, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 校验入参
		String errMsg = validaParam(paramMap);
		if (CommonUtils.isNotNull(errMsg)) {
			throw new BusException(errMsg);
		}
		// 公共参数
		Map<String, Object> reqMap = new HashMap<>();// 存放入参
		Map<String, Object> retMap = new HashMap<>();// 接收返回结果
		String retStr = "";// 接收返回结果
		User userInfo = (User) user;

		InsSgsybVisit visit = getPvVist(paramMap, user);
		if (visit == null || CommonUtils.isNull(visit.getAaz218())) {
			throw new BusException("获取医保登记ID信息信息失败");
		}

		List<Map<String, Object>> dts = zsrmSgsMapper.qryChargeDetailNoUpload(paramMap);

		// 待上传明细为空，直接反回，可能只有特需费用
		if (dts == null || dts.size() == 0) {

			Double dtAmt = 0d;
			// 检索非特需费用
			List<BlOpDt> dtRet = zsrmSgsMapper.qryChargeDetailNoUploadSpec(paramMap);
			for (BlOpDt dt : dtRet) {
				dtAmt = dtAmt + dt.getAmount();
			}
			InsSgsybSt insSgsybSt = new InsSgsybSt();
			insSgsybSt.setAmount(dtAmt);
			insSgsybSt.setAmtJjzf(0d);
			insSgsybSt.setAmtGrzf(dtAmt);
			retMap.clear();
			retMap.put("YbPreSettleInfo", insSgsybSt);
			return (Map<String, Object>) ApplicationUtils.beanToMap(retMap);
		}
		/* if (!(feeinfoPreMap != null && feeinfoPreMap.size() > 0)) { throw new BusException("未查询到待上传费用信息"); } */

		// 4.1.2收费时提取门诊业务信息
		reqMap.clear();

		reqMap.put("function_id", "bizh110102");
		reqMap.put("bka895", "aaz218");
		reqMap.put("bka896", visit.getAaz218());
		reqMap.put("akb020", INSUSERID);
		reqMap.put("bka006", "410");// 医疗待遇类型 工伤门诊
		retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!(retStr != null && "1".equals(retMap.get("return_code")))) {

			throw new BusException("预结算失败： bizh110102 " + CommonUtils.getString(retMap.get("return_code_message")));
		}
		// 调用预结算接口
		Map<String, Object> paramPreMap = new HashMap<String, Object>();
		reqMap.clear();
		paramPreMap.put("function_id", "bizh110105");
		paramPreMap.put("akb020", INSUSERID);
		paramPreMap.put("aaz218", visit.getAaz218());
		paramPreMap.put("bka014", userInfo.getCodeEmp());
		paramPreMap.put("bka015", userInfo.getNameEmp());
		paramPreMap.put("bka893", "0");

		paramPreMap.put("feeinfo", dts);
		// paramPreMap.put("program", ApplicationUtils.beanToJson(feeinfoPreMap));

		retStr = ybFileSigned(ApplicationUtils.beanToJson(paramPreMap), user);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (retStr != null && "1".equals(retMap.get("return_code"))) {
			List<Map<String, Object>> tmpRet = (List<Map<String, Object>>) retMap.get("payinfo");

			InsSgsybSt insSgsybSt = JsonUtil.readValue(JsonUtil.writeValueAsString(tmpRet.get(0)), InsSgsybSt.class);
			ApplicationUtils.setDefaultValue(insSgsybSt, true);

			insSgsybSt.setPkPv(visit.getPkPv());
			insSgsybSt.setPkPi(visit.getPkPi());
			insSgsybSt.setPkHp(visit.getPkHp());
			insSgsybSt.setPkVisit(visit.getPkVisit());

			insSgsybSt.setAmount(new Double(insSgsybSt.getAkc264()));// 总金额
			insSgsybSt.setAmtGrzhzf(new Double(insSgsybSt.getAkb066()));// 个人账户
			insSgsybSt.setAmtGrzf(new Double(insSgsybSt.getBka831()));// 现金
			insSgsybSt.setAmtJjzf(new Double(insSgsybSt.getBka832()));// 基金
			insSgsybSt.setYbPksettle(insSgsybSt.getAaz218() + "_" + insSgsybSt.getBka001());
			// DataBaseHelper.insertBean(insSgsybSt);

			retMap.clear();
			retMap.put("YbPreSettleInfo", insSgsybSt);
			retMap.put("YbPreSettleParam", paramPreMap);
			retMap.put("yBPkSettle", insSgsybSt.getYbPksettle());

			return retMap;
		} else {
			throw new BusException("预结算失败： bizh110105 " + CommonUtils.getString(retMap.get("return_code_message")));
		}

	}

	/**
	 * @param params
	 *            015001013002 门诊收费
	 */
	public Map<String, Object> mzHpJiaokuan(String params, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 公共参数
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> retMap = new HashMap<>();
		String retStr = "";
		String pkPv = paramMap.get("pkPv").toString();

		InsSgsybVisit visit = DataBaseHelper.queryForBean(
				"select * from ins_sgsyb_visit where pk_pv=? and ROWNUM=1 order by create_time desc ",
				InsSgsybVisit.class, pkPv);

		reqMap = (Map<String, Object>) paramMap.get("ybPreSettlParam");
		if (reqMap.containsKey("function_id")) {
			reqMap.put("function_id", "bizh110105");
		}
		if (reqMap.containsKey("bka893")) {
			reqMap.put("bka893", "1");
		}

		// 调用结算
		retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user);

		retMap = JsonUtil.readValue(retStr, Map.class);
		if ("1".equals(CommonUtils.getString(retMap.get("return_code")))) {

			List<Map<String, Object>> tmpRet = (List<Map<String, Object>>) retMap.get("payinfo");
			retMap = tmpRet.get(0);
			InsSgsybSt insSgsybSt = JsonUtil.readValue(JsonUtil.writeValueAsString(tmpRet.get(0)), InsSgsybSt.class);

			insSgsybSt.setPkPv(pkPv);
			insSgsybSt.setPkPi(visit.getPkPi());
			insSgsybSt.setPkHp(visit.getPkHp());
			insSgsybSt.setPkVisit(visit.getPkVisit());

			insSgsybSt.setAmount(new Double(insSgsybSt.getAkc264()));// 总金额
			insSgsybSt.setAmtGrzhzf(new Double(insSgsybSt.getAkb066()));// 个人账户
			insSgsybSt.setAmtGrzf(new Double(insSgsybSt.getBka831()));// 现金
			insSgsybSt.setAmtJjzf(new Double(insSgsybSt.getBka832()));// 基金
			insSgsybSt.setYbPksettle(insSgsybSt.getAaz218() + "_" + insSgsybSt.getBka001());

			insertInsSt(insSgsybSt);

			retMap.put("yBPkSettle", insSgsybSt.getYbPksettle());

			/* 暂留，因结算接口已返回费用序号 // 调-次 退费时提取门诊业务信息 目的是获取批次号 reqMap.clear(); reqMap.put("function_id", "bizh110103"); reqMap.put("bka895", "aaz218");
			 * reqMap.put("bka896", visit.getAaz218()); reqMap.put("akb020", USERID); reqMap.put("bka006", "410");
			 * 
			 * reqMap.put("bka001", "0"); // 0-获取所有结算批次信息 retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user); */

			return (Map<String, Object>) ApplicationUtils.beanToMap(insSgsybSt);

		} else {
			throw new BusException("医保结算失败：  " + CommonUtils.getString(retMap.get("return_code_message")));
		}

	}

	/**
	 * 校验就诊主键、费用明细主键
	 */
	private String validaParam(Map<String, Object> paramMap) {
		String errMsg = "";

		if (paramMap == null) {
			errMsg = "未获取到参数信息";
			return errMsg;
		}

		if (CommonUtils.isNull(paramMap.get("pkPv"))) {
			errMsg = "未传入就诊信息主键pkPv";
			return errMsg;
		}

		if (CommonUtils.isNull(paramMap.get("pkCgops"))) {
			errMsg = "未传入费用信息(pkCgops)";
			return errMsg;
		}
		Set<String> pkCgops = new HashSet<String>((List<String>) paramMap.get("pkCgops"));

		int cnt = DataBaseHelper.execute("select * from bl_op_dt where flag_settle='1' and pk_pv=?" + " and pk_cgop in("
				+ CommonUtils.convertSetToSqlInPart(pkCgops, "pk_cgop") + ")", paramMap.get("pkPv"));
		if (cnt > 0) {
			errMsg = "数据已变更，请刷新重试";
			return errMsg;
		}
		return errMsg;
	}

	private InsSgsybVisit getPvVist(Map<String, Object> paramMap, IUser user) throws Exception {
		
		String pkPv = CommonUtils.getPropValueStr(paramMap, "pkPv");
		if (CommonUtils.isNull(pkPv)) {
			throw new BusException("未传入就诊信息主键pkPv");
		}
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> retMap = new HashMap<>();

		String ret = "";
		String req = "";

		InsSgsybVisit piVisit = DataBaseHelper.queryForBean(
				"select * from ins_sgsyb_visit where pk_pv=? and del_flag='0'and ROWNUM='1' ", InsSgsybVisit.class,
				pkPv);// 查询医保登记信息

		reqMap = zsrmSgsMapper.qryInsBasePre(pkPv);
		req = ApplicationUtils.beanToJson(reqMap);

		reqMap=(Map<String, Object>)paramMap.get("yBPatientInfo");
		InsSgsPi pi =   JsonUtil.readValue(ApplicationUtils.beanToJson(reqMap), InsSgsPi.class);
		

		if (piVisit == null || CommonUtils.isNull(piVisit.getAaz218()))// 医保挂号
		{

			Personinfo piInfo = pi.getPersoninfo().get(0);
			Injuryorbirthinfo injuryorbirthinfo = new Injuryorbirthinfo();
			if (pi.getInjuryorbirthinfo() != null) {
				injuryorbirthinfo = pi.getInjuryorbirthinfo().get(0);
			}

			reqMap = zsrmSgsMapper.qryInsRegPre(pkPv);

			if(reqMap == null){
				throw new BusException("未获取到患者挂号信息");
			}
			Map<String, Object> RegPreMap = new HashMap<String, Object>();
			RegPreMap.put("function_id", "bizh110104");
			// reqMap.put("aaa027", ""); //中心编码
			RegPreMap.put("akb020", INSUSERID);
			RegPreMap.put("aac001", piInfo.getAac001());
			RegPreMap.put("bka006", "410");// 待遇类型 410-门诊，420住院
			RegPreMap.put("aka130", "41");// 业务类型 11-门诊
			RegPreMap.put("bka893", "0");// 保存标志 0-试算
			RegPreMap.put("bka042", injuryorbirthinfo.getBka042());
			RegPreMap.putAll(reqMap);
			ret = ybFileSigned(ApplicationUtils.beanToJson(RegPreMap), user);

			retMap = JsonUtil.readValue(ret, Map.class);
			if (ret != null && "1".equals(retMap.get("return_code"))) {// 挂号成功

				// 获取险种信息，默认取医保计划编码，若
				PvEncounter pvencounter = DataBaseHelper.queryForBean(
						"select * from pv_encounter where pk_pv=? and eu_pvtype in (1,2,4) and del_flag='0' ",
						PvEncounter.class, pkPv);
				BdHp hp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp=? and del_flag='0' ", BdHp.class,
						pvencounter.getPkInsu());
				if (hp == null) {
					throw new RuntimeException(
							"医保登记失败,未查询到对应的险种信息" + pvencounter.getCodePv() + pvencounter.getPkInsu());
				}

				// 保存挂号信息
				InsSgsybVisit visit = new InsSgsybVisit();
				ApplicationUtils.copyProperties(visit, piInfo);
				ApplicationUtils.copyProperties(visit, injuryorbirthinfo);

				visit.setPkPv(pkPv);
				visit.setPkPi(pvencounter.getPkPi());
				visit.setPkHp(pvencounter.getPkInsu());
				visit.setAaz218(retMap.get("aaz218").toString());// 就医登记号

				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				TransactionStatus status = platformTransactionManager.getTransaction(def);

				try {
					ApplicationUtils.setDefaultValue(visit, true);
					DataBaseHelper.insertBean(visit);
					platformTransactionManager.commit(status); // 提交事务
				} catch (Exception e) {
					platformTransactionManager.rollback(status); // 添加失败 回滚事务；
					e.printStackTrace();
					throw new RuntimeException("保存挂号信息失败，请重试：" + e);
				}

				return visit;
			}else{
				throw new BusException("预结算失败： bizh110104 " + CommonUtils.getString(retMap.get("return_code_message")));
				
			}

		}

		return piVisit;
	}

	/**
	 * 获取患者基本信息
	 * 015001014009
	 */
	public InsSgsPi getPersonInfo(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		
		String pkPv=CommonUtils.getPropValueStr(paramMap, "pkPv");
		String bka895=CommonUtils.getPropValueStr(paramMap, "bka895");
		if(CommonUtils.isNotNull(pkPv) &&"aac001".equals(bka895)){
			
		}
		else if(CommonUtils.isNotNull(pkPv)){
			Map<String, Object> baseMap = zsrmSgsMapper.qryInsBasePre(pkPv);
			paramMap.clear();
			paramMap.putAll(baseMap);
		}else{
			throw new BusException("bizh110001调用,未获取到患者就诊主键");
		}
		
		
		String ret = "";
		paramMap.put("function_id", "bizh110001");
		paramMap.put("bka006", "410");// 门诊-410，住院-420
		ret = ybFileSigned(ApplicationUtils.beanToJson(paramMap), user);

		// InsSgsPi pi = (InsSgsPi) XmlUtil.XmlToBean(ret, InsSgsPi.class);
		InsSgsPi pi = JsonUtil.readValue(ret, InsSgsPi.class);
		if (pi != null && "1".equals(pi.getReturn_code())) {

			if (pi.getPersoninfo().size() > 0) {

				return pi;
			} else {
				throw new BusException("bizh110001调用成功，但未获取到医保个人信息");
			}

		} else {
			throw new BusException("医保获取人员信息失败  " + pi.getReturn_code_message());
		}

	}

	/**
	 * @param 015001013007
	 *            门诊退费预结算
	 * @throws Exception
	 */
	public Map<String, Object> PreReturnSettlement(String params, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 查询医保登记信息
		InsSgsybVisit visit = DataBaseHelper.queryForBean("select * from ins_sgsyb_visit where pk_pv=?",
				InsSgsybVisit.class, paramMap.get("pkPv"));
		if (visit == null) {
			throw new BusException("医保预退费失败，未查询到对应的医保登记记录");
		}
		// 查询医保结算信息
		InsSgsybSt insSt = DataBaseHelper.queryForBean("select * from ins_sgsyb_st where pk_settle=? ",
				InsSgsybSt.class, paramMap.get("pkSettle"));
		// 不退费费用明细
		List<BlOpDt> noCancleDt = noCancleDt(params);
		
		// 无医保结算记录，为特需费用结算
		if (insSt == null) {
			Double dtAmt = 0d;
			
			// 部分退，计算重收费用
			if (noCancleDt != null && noCancleDt.size() > 0) {
				Set<String> pkBlOpDt = new HashSet<String>();
				for (BlOpDt blOpDt : noCancleDt) {
					pkBlOpDt.add(blOpDt.getPkCgop());
				}
				Map<String, Object> reqMap = paramMap;
				reqMap.put("pkCgops", pkBlOpDt);
				reqMap.put("rePay", "1");
				Double amtAdd = 0d;//特诊加收金额
				Double amtSpec = 0d;//特需费用
				List<BlOpDt> allDts = zsrmSgsMapper.qryChargeDts(reqMap);
				for (BlOpDt dt : allDts) {
					amtAdd= amtAdd + dt.getAmountAdd();
					if("96".equals(dt.getCodeBill())){
						amtSpec =amtSpec + dt.getAmount();	
					}		
				}
				dtAmt=amtAdd+amtSpec;
			}
			InsSgsybSt insSgsybSt =new InsSgsybSt();
			insSgsybSt.setAmount(dtAmt);
			insSgsybSt.setAmtJjzf(0d);
			insSgsybSt.setAmtGrzf(dtAmt);
			insSgsybSt.setAggregateAmount(dtAmt);
			insSgsybSt.setMedicarePayments(0d);
			insSgsybSt.setPatientsPay(dtAmt);

			return (Map<String, Object>) ApplicationUtils.beanToMap(insSgsybSt);
		}

		// 部分退逻辑
		if (noCancleDt != null && noCancleDt.size() > 0) {
			Map<String, Object> rePayIns = rePayIns(visit, noCancleDt, "0", user, params);
			if (rePayIns != null) {
				return rePayIns;
			} else {
				throw new BusException("医保预退费失败，部分退返回信息错误");
			}
		}

		// 医保预退费校验
		// Map<String, Object> m = paramMap;
		// m.put("ybPksettle", insSt.getYbPksettle());
		// insSettleCancle(JsonUtil.writeValueAsString(m), user);
		// 调用医保退费预结算返回信息
		Map<String, Object> stMap = zsrmSgsMapper.qryBlSt(paramMap);
		if (stMap == null) {
			throw new BusException("医保预退费失败，未查询到对应的结算记录");
		}
		//此处返回金额为重收金额，故全退时返回0
		stMap.put("aggregate_amount", 0);
		stMap.put("patients_pay", 0);
		stMap.put("medicare_payments", 0);
		stMap.put("ybPksettle", insSt.getYbPksettle());
		return stMap;

	}

	private List<BlOpDt> noCancleDt(String params) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 查询本次结算的的所有明细
		List<BlOpDt> allList = DataBaseHelper.queryForList(
				"select * from bl_op_dt where pk_pv=? and pk_settle =? and del_flag='0' ", BlOpDt.class,
				paramMap.get("pkPv"), paramMap.get("pkSettle"));
		
		JsonNode jsonnode = JsonUtil.getJsonNode(params, "returnPkcgop");
		if (jsonnode == null) {
			throw new BusException("医保预退费失败，未查询到对应的医保结算记录");
		}

		List<BlOpDt> returnPkcgop = JsonUtil.readValue(jsonnode, new TypeReference<List<BlOpDt>>() {});
		List<BlOpDt> newOpList = new ArrayList<BlOpDt>();
		for (BlOpDt opdt : allList) {
			boolean flag = false;
			for (BlOpDt oldDt : returnPkcgop) {
				if (opdt.getPkCgop().equals(oldDt.getPkCgop())) {
					
					if(opdt.getQuan().equals(oldDt.getQuanBack()))
					{
						flag = true;
						break;
					}
					opdt.setAmountAdd(opdt.getAmountAdd()-oldDt.getQuanBack()*(opdt.getAmountAdd()/opdt.getQuan()));
					opdt.setQuan(opdt.getQuan()-oldDt.getQuanBack());//bl_op_dt明细部分退费
					opdt.setAmount(opdt.getAmount()-oldDt.getAmount());	
					break;
				}
			}
			if (!flag) {
				newOpList.add(opdt);
			}
		}
		return newOpList;
	}

	/**
	 * 部分退逻辑 params使用节点 pkPv pkSettle returnPkcgop
	 */
	private Map<String, Object> rePayIns(InsSgsybVisit visit, List<BlOpDt> newOpList, String bka893, IUser user,
			String preParam) throws Exception {

		Set<String> pkCgs = new HashSet<String>();
		for (BlOpDt blOpDt : newOpList) {
			pkCgs.add(blOpDt.getPkCgop());
		}
		Map<String, Object> reqMap = new HashMap<String, Object>();
		Map<String, Object> retMap = new HashMap<String, Object>();
		reqMap.put("pkPv", visit.getPkPv());
		reqMap.put("rePay", "1");
		reqMap.put("pkCgops", pkCgs);
		List<Map<String, Object>> dts = zsrmSgsMapper.qryChargeDetailNoUpload(reqMap);
		
		InsSgsybSt insSgsSt=new InsSgsybSt();
		Double amtAdd = 0d;//特诊加收金额
		Double amtSpec = 0d;//特需费用
		List<BlOpDt> allDts = zsrmSgsMapper.qryChargeDts(reqMap);
		for (BlOpDt dt : allDts) {
			amtAdd= amtAdd + dt.getAmountAdd();
			if("96".equals(dt.getCodeBill())){
				amtSpec =amtSpec + dt.getAmount();	
			}		
		}
		
		insSgsSt.setAggregateAmount(amtSpec);
		insSgsSt.setMedicarePayments(0d);
		insSgsSt.setPatientsPay(amtSpec);
		if (dts == null || dts.size() == 0) {
			return (Map<String, Object>) ApplicationUtils.beanToMap(insSgsSt);
		}

		// 收费时提取门诊业务信息
		String retStr;
		reqMap.clear();

		reqMap.put("function_id", "bizh110102");
		reqMap.put("bka895", "aaz218");
		reqMap.put("bka896", visit.getAaz218());
		reqMap.put("akb020", INSUSERID);
		reqMap.put("bka006", "410");// 医疗待遇类型 工伤门诊
		retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (!(retStr != null && "1".equals(retMap.get("return_code")))) {
			throw new BusException("预结算失败： bizh110102 " + CommonUtils.getString(retMap.get("return_code_message")));
		}
		// 调用预结算接口
		Map<String, Object> paramPreMap = new HashMap<String, Object>();
		User userInfo = (User) user;
		reqMap.clear();
		paramPreMap.put("function_id", "bizh110105");
		paramPreMap.put("akb020", INSUSERID);
		paramPreMap.put("aaz218", visit.getAaz218());
		paramPreMap.put("bka014", userInfo.getCodeEmp());
		paramPreMap.put("bka015", userInfo.getNameEmp());
		paramPreMap.put("bka893", bka893);

		paramPreMap.put("feeinfo", dts);

		retStr = ybFileSigned(ApplicationUtils.beanToJson(paramPreMap), user);
		retMap = JsonUtil.readValue(retStr, Map.class);
		if (retStr != null && "1".equals(retMap.get("return_code"))) {

			List<Map<String, Object>> tmpRet = (List<Map<String, Object>>) retMap.get("payinfo");
			InsSgsybSt insStCancel = JsonUtil.readValue(JsonUtil.writeValueAsString(tmpRet.get(0)), InsSgsybSt.class);
			insStCancel.setAmount(new Double(insStCancel.getAkc264()));// 总金额
			insStCancel.setAmtGrzhzf(new Double(insStCancel.getAkb066()));// 个人账户
			insStCancel.setAmtGrzf(new Double(insStCancel.getBka831()));// 现金
			insStCancel.setAmtJjzf(new Double(insStCancel.getBka832()));// 基金
			insStCancel.setYbPksettle(insStCancel.getAaz218() + "_" + insStCancel.getBka001());

			// 如果有医保就诊信息，保存相关数据,即非单边账情况
			if (visit != null) {
				insStCancel.setPkPv(visit.getPkPv());
				insStCancel.setPkPi(visit.getPkPi());
				insStCancel.setPkHp(visit.getPkHp());
				insStCancel.setPkVisit(visit.getPkVisit());

			}
			if ("1".equals(bka893)) {
				ApplicationUtils.setDefaultValue(insStCancel, true);
				insertInsSt(insStCancel);
			}
			insStCancel.setAggregateAmount(new Double(insStCancel.getAkc264())+amtAdd+amtSpec);
			insStCancel.setPatientsPay(new Double(insStCancel.getBka831())+amtAdd+amtSpec);
			insStCancel.setMedicarePayments(new Double(insStCancel.getBka832()));
			insStCancel.setYBPreReturn(dts);
			insStCancel.setyBPreIntoParam(preParam);
			return (Map<String, Object>) ApplicationUtils.beanToMap(insStCancel);
		} else {
			Log.error("bizh110105 医保重新结算失败，" + INSUSERID + CommonUtils.getString(retMap.get("return_code_message")));
			throw new BusException("bizh110105医保重新结算失败，" + CommonUtils.getString(retMap.get("return_code_message")));
		}

	}

	/**
	 * @param 015001014006
	 *            门诊退费
	 * @throws Exception
	 */
	public Map<String, Object> mzHpSetttleCancel(String params, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		Map<String, Object> preRetMap = (Map<String, Object>) paramMap.get("ybPreReturnInfo");
		if (CommonUtils.isNull(paramMap.get("ybPkSettle")))// his结算失败 医保退费
		{
			InsSgsybSt insSt = DataBaseHelper.queryForBean("select * from ins_sgsyb_st where pk_settle=? ",
					InsSgsybSt.class, paramMap.get("pkSettle"));
			if(insSt!= null) {
				paramMap.put("ybPkSettle", insSt.getYbPksettle());
			}else{
				return null;
			}		
		}
		if (CommonUtils.isNull(paramMap.get("ybPkSettle"))) {
			throw new BusException("医保预退费失败，未查询到对应的医保结算Id");
		}
		// 查询医保结算信息
		InsSgsybSt insSt = DataBaseHelper.queryForBean("select * from ins_sgsyb_st where yb_pksettle=? ",
				InsSgsybSt.class, paramMap.get("ybPkSettle"));

		if (insSt != null) {
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("pkPv", insSt.getPkPv());
			reqMap.put("ybPksettle", paramMap.get("ybPkSettle"));
			reqMap.put("bka893", "1");
			reqMap = insSettleCancle(JsonUtil.writeValueAsString(reqMap), user);

			// 部分退逻辑
			if (preRetMap.get("yBPreIntoParam") != null) {

				// 查询医保登记信息
				InsSgsybVisit visit = DataBaseHelper.queryForBean("select * from ins_sgsyb_visit where pk_pv=?",
						InsSgsybVisit.class, paramMap.get("pkPv"));
				if (visit == null) {
					throw new BusException("医保预退费失败，未查询到对应的医保登记记录");
				}
				List<BlOpDt> noCancleDt = noCancleDt(preRetMap.get("yBPreIntoParam").toString());// 不退费费用明细
				if (noCancleDt != null && noCancleDt.size() > 0) {
					Map<String, Object> rePayIns = rePayIns(visit, noCancleDt, "1", user, params);
					if (rePayIns != null) {
						return rePayIns;
					} else {
						throw new BusException("医保预退费失败，部分退返回信息错误");
					}
				}else{
					throw new BusException("医保退费失败，未获取到重收费用明细");
				}
			} else {
				return null;
			}

		} else {
			throw new BusException("结算主键" + paramMap.get("pkSettle") + " 未查询到医保结算记录");
		}

	}

	/**
	 * 医保预退费、退费接口 bka893='1'时 为退费
	 */
	public Map<String, Object> insSettleCancle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		InsSgsybVisit visit = new InsSgsybVisit();
		// 退费业务校验
		if (CommonUtils.isNotNull(paramMap.get("pkPv"))) {
			visit = DataBaseHelper.queryForBean("select * from ins_sgsyb_visit where pk_pv=? and ROWNUM=1 ",
					InsSgsybVisit.class, paramMap.get("pkPv"));
			if (visit == null) {
				Log.error("医保退费失败，未获取到登记信息" + params);
				throw new BusException("医保退费失败，未获取到登记信息" + params);
			}

		}

		if (CommonUtils.isNotNull(paramMap.get("ybPksettle")))// his结算失败 医保退费
		{
			// yBPkSettle长度应大于21位，就诊登记号（aaz218(20位)）+“_”+费用序号(bka001(1位))
			String yBPkSettle = CommonUtils.getString(paramMap.get("ybPksettle"));
			if (yBPkSettle.length() <= 21) {
				Log.error("调用医保退费失败，非法入参" + yBPkSettle);
				throw new BusException("调用医保退费失败，非法入参" + yBPkSettle);
			}
			String bka001 = yBPkSettle.substring(21);
			String aaz218 = yBPkSettle.substring(0, 20);
			String bka893 = "1".equals(paramMap.get("bka893")) ? "1" : "0"; // 默认预0 0-预算，1-结算

			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("function_id", "bizh110103");// 获取所有结算批次信息
			reqMap.put("bka895", "aaz218");
			reqMap.put("bka896", aaz218);
			reqMap.put("akb020", INSUSERID);
			reqMap.put("bka006", "410");
			reqMap.put("bka001", bka001);
			String retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user);
			Map<String, Object> retMap = JsonUtil.readValue(retStr, Map.class);

			if ("1".equals(retMap.get("return_code"))) {

				if (retStr.contains(aaz218)) {
					// 医保退费
					reqMap.clear();
					reqMap.put("function_id", "bizh110105");
					reqMap.put("akb020", INSUSERID);
					reqMap.put("aaz218", aaz218);
					reqMap.put("bka893", bka893);// 0-预算，1-结算
					reqMap.put("feeinfo", retMap.get("feeinfo"));
					retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user);
					retMap = JsonUtil.readValue(retStr, Map.class);
					if ("1".equals(retMap.get("return_code"))) {// 退费成功，
						InsSgsybSt insSt = DataBaseHelper.queryForBean(
								"select * from ins_sgsyb_st where aaz218=? and bka001= ?", InsSgsybSt.class, aaz218,
								bka001);

						if (insSt != null) {
							List<Map<String, Object>> tmpRet = (List<Map<String, Object>>) retMap.get("payinfo");

							InsSgsybSt insStCancel = JsonUtil.readValue(JsonUtil.writeValueAsString(tmpRet.get(0)),
									InsSgsybSt.class);

							insStCancel.setAmount(new Double(insStCancel.getAkc264()));// 总金额
							insStCancel.setAmtGrzhzf(new Double(insStCancel.getAkb066()));// 个人账户
							insStCancel.setAmtGrzf(new Double(insStCancel.getBka831()));// 现金
							insStCancel.setAmtJjzf(new Double(insStCancel.getBka832()));// 基金
							insStCancel.setYbPksettle(insStCancel.getAaz218() + "_" + insStCancel.getBka001());

							// 如果有医保就诊信息，保存相关数据,即非单边账情况
							if (visit != null) {
								insStCancel.setPkPv(visit.getPkPv());
								insStCancel.setPkPi(visit.getPkPi());
								insStCancel.setPkHp(visit.getPkHp());
								insStCancel.setPkVisit(visit.getPkVisit());
								insStCancel.setYbPksettleCancel(insSt.getYbPksettle());
								insStCancel.setPkInsstCancel(insSt.getPkInsst());
							}
							if ("1".equals(bka893)) {
								ApplicationUtils.setDefaultValue(insStCancel, true);
								insertInsSt(insStCancel);
							}
							return (Map<String, Object>) ApplicationUtils.beanToMap(insStCancel);
						}
						return retMap;// 若数据库无收费记录，则直接返回医保退费结果
					} else {
						Log.error("bizh110105 医保退费失败，" + yBPkSettle
								+ CommonUtils.getString(retMap.get("return_code_message")));
						throw new BusException(
								"bizh110105 医保退费失败，" + CommonUtils.getString(retMap.get("return_code_message")));
					}
				} else {
					Log.error("bizh110103  医保退费时提取门诊业务信息失败，  " + yBPkSettle
							+ CommonUtils.getString(retMap.get("return_code_message")));
					throw new BusException(
							"bizh110103  医保退费时提取门诊业务信息失败，" + CommonUtils.getString(retMap.get("return_code_message")));
				}
			} else if (CommonUtils.getString(retMap.get("return_code_message")).contains("已经办理过退费，不能重复退费")) {

				retMap.clear();
				InsSgsybSt insSt = DataBaseHelper.queryForBean(
						"select * from ins_sgsyb_st where aaz218=? and bka001= ?", InsSgsybSt.class, aaz218, bka001);
				retMap.put("ybPksettle", paramMap.get("ybPksettle"));
				// retMap.put("ybtczf", insSt.getBka832());
				retMap.put("amtJjzf", insSt.getBka832());
				return retMap;
			} else {
				throw new BusException("医保预退费失败，" + CommonUtils.getString(retMap.get("return_code_message")));
			}

		} else {
			Log.error("省工伤退费失败,参数：" + params);
			throw new BusException("省工伤退费失败，未获取到结算ID");
		}
	}

	/**
	 * 插入医保结算表
	 */
	private void insertInsSt(InsSgsybSt insSt) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);

		try {
			insSt.setDateSt(new Date());
			DataBaseHelper.insertBean(insSt);
			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new RuntimeException("保存医保结算信息失败，请重试：" + e);
		}
	}

	/**
	 * @param 015001014005
	 *            将PkSettle结算主键更新到INS_QGYB_ST表
	 */
	public void updatePkSettle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = paramMap.get("pkPv").toString();

		String pkSettle = null;
		String ywlx = null;// 业务类型
		String ybPksettle = null;// 医保结算主键

		if (paramMap.containsKey("pkSettle") && paramMap.get("pkSettle") != null) {
			pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		}
		if (paramMap.containsKey("yWLX") && paramMap.get("yWLX") != null) {
			ywlx = CommonUtils.getString(paramMap.get("yWLX"));
		}
		if (paramMap.containsKey("pkPtmzjs") && paramMap.get("pkPtmzjs") != null) {
			ybPksettle = CommonUtils.getString(paramMap.get("pkPtmzjs"));
		}
		if ("1".equals(CommonUtils.getString(ywlx))) {// 门诊结算
			if (CommonUtils.isEmptyString(pkSettle) && !CommonUtils.isEmptyString(ybPksettle)) {
				String pkSettleCancel = zsrmSgsMapper.qryPksettleCanc(pkPv, ybPksettle);
				DataBaseHelper.execute(
						"update ins_sgsyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and yb_pksettle = ?",
						pkSettleCancel, pkPv, ybPksettle);
				return;
			}
			if (!CommonUtils.isEmptyString(ybPksettle)) {
				DataBaseHelper.execute(
						"update ins_sgsyb_st set pk_settle=? where del_flag='0' and PK_PV=? and yb_pksettle = ?",
						pkSettle, pkPv, ybPksettle);
				// DataBaseHelper.execute("update ins_qgyb_visit set EU_STATUS_ST='1' where del_flag='0' and PK_VISIT in (select PK_VISIT from
				// ins_szyb_st where pk_insst = ?)",pkInsst);
			}
		} else {// 暂留住院
			DataBaseHelper.execute(
					"update ins_sgsyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and nvl(pk_settle,'0')='0'",
					pkSettle, pkPv);
			return;
		}

	}

	/**
	 * 将一个XML数据，做为post请求的参数，发起http请求,编码utf-8，用于医保系统，返回json字符串
	 * 
	 * @param qsStr
	 * @return
	 */
	public String ybFileSigned(String paramsJson, IUser user) {

		JSONObject paramsObj = JSONObject.parseObject(paramsJson);
		String functionId = (String) paramsObj.get("function_id");
		logger.info(functionId + " 参数：" + paramsJson);
		String sessionId = RedisUtils.getCacheObj("session_id", String.class);// 取某一作用域的唯一标识
		String resultState = "";
		JSONObject json = new JSONObject();
		try {
			if (!"sys0001".equals(functionId)) {
				if (null == sessionId || "".equals(sessionId)) {
					ybLogin(user);
				}
				paramsObj.put("session_id", RedisUtils.getCacheObj("session_id", String.class));
				paramsObj.put("akb020", MEDICAL_INSTITUTION_CODE);

				if ("bizh110104".equals(functionId) || "bizh110105".equals(functionId)
						|| "bizh110106".equals(functionId)) {

					User u = (User) user;
					paramsObj.put("bka014", u.getCodeEmp());
					paramsObj.put("bka015", u.getNameEmp());
				}
			}
			paramsJson = ZsrmSgsUtils.makeXml(paramsObj);
			//发送http请求
			String result = HttpClientUtil.sendHttpPost(REJECTED_URL, paramsJson, new HashMap<String, String>());
			logger.info(functionId + " 出参：" + result);
			json = TestXml2Json.xml2Json(result);
			resultState = json.get("return_code").toString();
			if("1".equals(resultState)) {
				return json.toString();
			}else if ("-9".equals(resultState)) {
				//登录session_id失效，需要重新登录
				ybLogin(user);
			} else {
				String jsonStr = "{'return_code':'-1','return_code_message':'医保返回的错误信息："
						+ json.get("return_code_message").toString() + "'}";
				json = JSONObject.parseObject(jsonStr);
			}
		} catch (Exception e) {
			logger.error("sendHttpPostError:", e.getMessage());
			String jsonStr = "{'return_code':'-1','return_code_message':'" + e.getMessage() + "'}";
			json = JSONObject.parseObject(jsonStr);
			// throw new BusException("请求异常："+e.getMessage());
		} 
		return json.toString();
	}

	/**
	 * 医保登录
	 */
	public String ybLogin(IUser user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("function_id", "sys0001");
		map.put("userid", INSUSERID);
		map.put("password", PASSWORD);
		try {
			JSONObject obj = JSONObject.parseObject(JSONObject.toJSONString(map));
			String jsonStr = ybFileSigned(obj.toString(), user);
			JSONObject jsonObj = JSONObject.parseObject(jsonStr);
			RedisUtils.setCacheObj("session_id", jsonObj.getString("session_id"), 24 * 60 * 60);
			return jsonObj.getString("session_id");
		} catch (Exception e) {
			logger.error("医保登录接口异常，原因：", e.getMessage());
			throw new BusException("医保登录接口异常，原因：" + e.getMessage());
		}
	}


	/**
	 * 省工伤医保结算对账信息查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getSGSCheckInsuranceSum(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
		return zsrmSgsMapper.qrySgsMedicalInsuranceSum(mapParam);
	}

	/**
	 * 获取患者信息
	 *	015001014010
	 * @return
	 */
	public ResponseJson getZyPatientInfo(String param, IUser user) {
		ResponseJson result = new ResponseJson();
		ZyPersonParam zyPersonParam = JsonUtil.readValue(param, ZyPersonParam.class);
		if (StringUtils.isBlank(zyPersonParam.getBka896())) {
			throw new BusException("证件号码不能为空");
		}
		zyPersonParam.setFunctionId("bizh120001");
		zyPersonParam.setAkb020(INSUSERID);
		zyPersonParam.setBka006("420");
		String paramJson = ApplicationUtils.beanToJson(zyPersonParam);
		String retStr = ybFileSigned(paramJson, user);
 		InsSgsZyPi insSgsZyPi = JsonUtil.readValue(retStr, InsSgsZyPi.class);
		String returnCode = insSgsZyPi.getReturn_code();
		if ("1".equals(returnCode)) {
			result.setData(insSgsZyPi.getPersoninfo());
			return result;
		} else {
			result.setStatus(-1);
			result.setDesc("bizh120001|"+paramJson+"|获取患者信息失败:" + insSgsZyPi.getReturn_code_message());
			return result;
		}
	}

	/**
	 * 入院登记
	 * 015001014011
	 * @param param
	 * @param user
	 * @return
	 */
	public ResponseJson zyRegister(String param,IUser user){
		ResponseJson result = new ResponseJson();
		ZyRegparam zyRegparam = JsonUtil.readValue(param, ZyRegparam.class);
		zyRegparam.setFunctionId("bizh120103");
		zyRegparam.setAkb020(INSUSERID);
		zyRegparam.setBka006("420");
		String pkDept = zyRegparam.getBka021();
		if(StringUtils.isBlank(pkDept)){
			throw new BusException("病区信息不能为空");
		}
//		根据病区主键，查询病区编码和名称
		BdOuDept dept = zsrmSgsMapper.qryDeptInfoByPkDept(pkDept);
		zyRegparam.setBka021(dept.getCodeDept());
		zyRegparam.setBka022(dept.getNameDept());
		String regParamJson = ApplicationUtils.beanToJson(zyRegparam);
		String retStr = ybFileSigned(regParamJson, user);
		InsSgsZyReg insSgsZyReg = JsonUtil.readValue(retStr, InsSgsZyReg.class);
		if("1".equals(insSgsZyReg.getReturn_code())){
			String aaz218 = insSgsZyReg.getAaz218();
//			登记成功，保存医保登记信息
			InsSgsybVisit visit = new InsSgsybVisit();
			ApplicationUtils.copyProperties(visit,zyRegparam.getPersonInfo());
			visit.setBka026(zyRegparam.getBka026());
			visit.setAaz218(aaz218);
			visit.setPkPi(zyRegparam.getPkPi());
			visit.setAka130(zyRegparam.getAka130());
			try {
				ApplicationUtils.setDefaultValue(visit,true);
				DataBaseHelper.insertBean(visit);
			} catch (Exception e) {
//				his保存失败,进行入院撤销
				HashMap<String, String> map = new HashMap<>();
				map.put("function_id","bizh120109");
				map.put("akb020",INSUSERID);
				map.put("aaz218",insSgsZyReg.getAaz218());
				String paramJson = ApplicationUtils.beanToJson(map);
				String cancelRet = ybFileSigned(paramJson, user);
				InsSgsPubParam insSgsPubParam = JsonUtil.readValue(cancelRet, InsSgsPubParam.class);
				result.setStatus(-1);
				if("1".equals(insSgsPubParam.getReturn_code())) {
					result.setDesc("bizh120109|"+paramJson+"|住院医保登记成功，HIS保存登记信息失败，原因：" + e.getMessage());
				}else{
					result.setDesc("bizh120109|"+paramJson+"|住院医保登记成功，HIS保存登记信息失败,撤销登记失败,原因:"+insSgsPubParam.getReturn_code_message());
				}
				return result;
			}
			result.setData(aaz218);
			return result;
		}else{
			result.setStatus(-1);
			result.setDesc("bizh120103|"+regParamJson+"|入院登记失败，原因："+insSgsZyReg.getReturn_code_message());
			return result;
		}
	}

	/**
	 * 保存his信息到医保登记表中
	 * 015001014013
	 * @param param
	 * @param user
	 */
	public void saveZyHpRegRelationship(String param,IUser user){
//		检验入参
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paramMap.get("pkPv");
		if(StringUtils.isBlank(pkPv)){
			throw new BusException("就诊主键为空，医保登记表保存HIS信息时失败");
		}
		String ybRegPk = (String) paramMap.get("ybRegPk");
		if(StringUtils.isBlank(ybRegPk)){
			throw new BusException("医保登记主键为空，医保登记表保存HIS信息时失败");
		}
		String pkPi = (String) paramMap.get("pkPi");
//		根据就诊主键，查询医保主计划
		BdHp bdHp = zsrmSgsMapper.qryPvHp(pkPv);
		InsSgsybVisit visit = new InsSgsybVisit();
		visit.setPkPv(pkPv);
		visit.setPkPi(pkPi);
		visit.setPkHp(bdHp.getPkHp());
		visit.setAaz218(ybRegPk);
		zsrmSgsMapper.updateInsSgsYbVisit(visit);
	}

	/**
	 * 查询入院后的业务信息
	 * 015001014014
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ZyBusinessInfo> getZyBusinessInfo(String param,IUser user){
		ZyBusinessParam zyBusinessParam = JsonUtil.readValue(param, ZyBusinessParam.class);
		zyBusinessParam.setFunctionId("bizh120102");
		zyBusinessParam.setAkb020(INSUSERID);
		String ret = ybFileSigned(ApplicationUtils.beanToJson(zyBusinessParam), user);
		InsSgsZyBusiness insSgsZyBusiness = JsonUtil.readValue(ret, InsSgsZyBusiness.class);
		if("1".equals(insSgsZyBusiness.getReturn_code())){
			return insSgsZyBusiness.getBusinessInfo();
		}else{
			throw new BusException("查询入院后的业务信息失败,原因:"+insSgsZyBusiness.getReturn_code_message());
		}
	}

	/**
	 * 上传住院费用信息
	 * 住院加收规则:bl_ip_dt.amount_add属于自费费用,不上传医保;
	 * 015001014015
	 * @param param
	 * @param user
	 */
	public ResponseJson upLoadZyChargeDetail(String param, IUser user){
		ResponseJson responseJson = new ResponseJson();
		Map paramMap = JsonUtil.readValue(param, Map.class);
		HashMap<String, Object> map = new HashMap<>();
		map.put("function_id","bizh120002");
		map.put("akb020",INSUSERID);
		Object ybRegInfo = paramMap.get("ybRegInfo");
		InsSgsZyReg insSgsZyReg = new InsSgsZyReg();
		ApplicationUtils.copyProperties(insSgsZyReg,ybRegInfo);
		map.put("aaz218",insSgsZyReg.getAaz218());
//		查询患者的住院费用明细,去除了特需费
		List<ZyChargeDetail> list = zsrmSgsMapper.qryZyChargeDetail(paramMap);
		List<ZyChargeDetail> updateList = new ArrayList<>();
		if(list != null && !list.isEmpty()) {
//			费用信息列表添加序列号,计算金额
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setAaz213(String.valueOf(i+1));
				String bka056 = list.get(i).getBka056();
				String bka057 = list.get(i).getBka057();
				BigDecimal bka058 = new BigDecimal(bka056).multiply(new BigDecimal(bka057));
				list.get(i).setBka058(bka058.toString());
			}
//			每次交易上传不能超过100条费用明细
			int toIndex = 100;
			for(int i=0;i<list.size();i+=100){
				if(i+100>list.size()){
					toIndex = list.size();
				}
				List<ZyChargeDetail> subList = list.subList(i, toIndex);
				map.put("feeinfo",subList);
				String paramJson = ApplicationUtils.beanToJson(map);
				String ret = ybFileSigned(paramJson, user);
				InsSgsPubParam insSgsPubParam = JsonUtil.readValue(ret, InsSgsPubParam.class);
				if("1".equals(insSgsPubParam.getReturn_code())){
//					上传成功的，需要更新bl_ip_dt中的flag_insu标记
					updateList.addAll(subList);
				}else{
//					上传失败的话,需要把之前的明细撤销
					responseJson.setStatus(-1);
					if(i>0){
						HashMap<String, String> cancelMap = new HashMap<>();
						cancelMap.put("function_id","bizh120004");
						cancelMap.put("akb020",INSUSERID);
						cancelMap.put("aaz218",insSgsZyReg.getAaz218());
						String cancelParamJson = ApplicationUtils.beanToJson(cancelMap);
						String cancelRet = ybFileSigned(cancelParamJson, user);
						InsSgsPubParam cancelResult = JsonUtil.readValue(cancelRet, InsSgsPubParam.class);
						if(!"1".equals(cancelResult.getReturn_code())){
							responseJson.setDesc("bizh120004|"+cancelParamJson+"|部分费用明细上传失败,明细撤销失败:"+cancelResult.getReturn_code_message());
							return responseJson;
						}
					}
					responseJson.setDesc("bizh120002|"+paramJson+"|费用明细上传失败:"+insSgsPubParam.getReturn_code_message());
					return responseJson;
				}
			}
			if(!updateList.isEmpty()){
				zsrmSgsMapper.updateFlagInsu(updateList);
			}
		}
		return responseJson;
	}

	/**
	 * 查询患者入院登记信息
	 * 015001014016
	 * @param param
	 * @param user
	 * @return
	 */
	public Map getZyRegInfo(String param,IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paramMap.get("pkPv");
		if(StringUtils.isBlank(pkPv)){
			throw new BusException("查询登记信息失败,原因:无法从参数中获取pkPv");
		}
		InsSgsybVisit visit = zsrmSgsMapper.qryInsSgsYbVisit(pkPv);
		if(visit == null || StringUtils.isBlank(visit.getAaz218())){
			throw new BusException("该患者没有住院登记信息");
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("YbRegPk",visit.getAaz218());
		map.put("YbRegInfo",visit);
		return map;
	}

	/**
	 * 住院预结算
	 * 015001014017
	 * @param param
	 * @param user
	 */
	public ResponseJson preZySettle(String param,IUser user){
		ResponseJson responseJson = new ResponseJson();
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String aaz218 = (String)paramMap.get("aaz218");
		if(StringUtils.isBlank(aaz218)){
			throw new BusException("预结算失败，参数中没有就医登记号");
		}
        HashMap<String, String> map = new HashMap<>();
        map.put("function_id","bizh120003");
        map.put("akb020",INSUSERID);
        map.put("aaz218",aaz218);
        map.put("bka438",(String)paramMap.get("bka438"));
		String paramJson = ApplicationUtils.beanToJson(map);
		String result = ybFileSigned(paramJson, user);
		InsSgsZyPreSettle preSettle = JsonUtil.readValue(result, InsSgsZyPreSettle.class);
		if("1".equals(preSettle.getReturn_code())){
            InsSgsybSt insSgsybSt = new InsSgsybSt();
            BeanUtils.copyPropertiesIgnoreNull(preSettle,insSgsybSt);
			Object ybRegInfo = paramMap.get("zyRegInfo");
			String jsonStr = JsonUtil.writeValueAsString(ybRegInfo);
			InsSgsybVisit visit = JsonUtil.readValue(jsonStr, InsSgsybVisit.class);
			BeanUtils.copyPropertiesIgnoreNull(visit,insSgsybSt);
//            BeanUtils.copyPropertiesIgnoreNull(zyRegInfo,insSgsybSt);
            ApplicationUtils.setDefaultValue(insSgsybSt,true);
            insSgsybSt.setAmount(Double.valueOf(insSgsybSt.getAkc264()));
            insSgsybSt.setAmtGrzhzf(Double.valueOf(insSgsybSt.getAkb066()));
            insSgsybSt.setAmtGrzf(Double.valueOf(insSgsybSt.getBka831()));
            insSgsybSt.setAmtJjzf(Double.valueOf(insSgsybSt.getBka832()));
//            装配返回结果
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("Grzhzf",insSgsybSt.getAkb066());
            resultMap.put("Fyze",insSgsybSt.getAkc264());
            resultMap.put("Xxzf",insSgsybSt.getBka831());
            resultMap.put("YbPreSettleInfo",preSettle);
            resultMap.put("SettlementInfo",map);
            resultMap.put("PkVisit",insSgsybSt.getPkVisit());
            responseJson.setData(resultMap);
            return responseJson;
        }else{
			responseJson.setStatus(-1);
			responseJson.setDesc("bizh120003|"+paramJson+"|住院预结算失败,原因:"+preSettle.getReturn_code_message());
			return responseJson;
		}
	}

	/**
	 * 出院登记
	 * 015001014018
	 * @param param
	 * @param user
	 */
	public ResponseJson zyHpDis(String param,IUser user){
		ResponseJson responseJson = new ResponseJson();
		ZyHpDisParam disParam = JsonUtil.readValue(param, ZyHpDisParam.class);
		disParam.setFunctionId("bizh120105");
		disParam.setAkb020(INSUSERID);
		disParam.setBka032(DateUtils.getDateStr(new Date(System.currentTimeMillis())));
		String paramJson = ApplicationUtils.beanToJson(disParam);
		String result = ybFileSigned(paramJson, user);
		InsSgsZyPreSettle insSgsZyPreSettle = JsonUtil.readValue(result, InsSgsZyPreSettle.class);
		if(!"1".equals(insSgsZyPreSettle.getReturn_code())){
			responseJson.setStatus(-1);
			responseJson.setDesc("bizh120105|"+paramJson+"|出院登记失败,原因:"+insSgsZyPreSettle.getReturn_code_message());
			return responseJson;
		}
		return responseJson;
	}

	/**
	 * 出院结算
	 * 015001014021
	 * @param param
	 * @param user
	 * @return
	 */
	public ResponseJson zyHpSettle(String param,IUser user){
		ResponseJson responseJson = new ResponseJson();
		Map paramMap = JsonUtil.readValue(param, Map.class);
		HashMap<String, String> map = new HashMap<>();
		String aaz218 = (String) paramMap.get("aaz218");
		map.put("function_id","bizh120106");
		map.put("akb020",INSUSERID);
		map.put("aaz218",aaz218);
		map.put("bka046",(String) paramMap.get("bka046"));
		map.put("bka047",(String) paramMap.get("bka047"));
		String paramJson = ApplicationUtils.beanToJson(map);
		String result = ybFileSigned(paramJson, user);
		InsSgsZyPreSettle settle = JsonUtil.readValue(result, InsSgsZyPreSettle.class);
		if("1".equals(settle.getReturn_code())){
			InsSgsybSt insSgsybSt = new InsSgsybSt();
			Object ybRegInfo = paramMap.get("ybRegInfo");
			String jsonStr = JsonUtil.writeValueAsString(ybRegInfo);
			InsSgsybVisit visit = JsonUtil.readValue(jsonStr, InsSgsybVisit.class);
			BeanUtils.copyPropertiesIgnoreNull(settle,insSgsybSt);
			BeanUtils.copyPropertiesIgnoreNull(visit,insSgsybSt);
			insSgsybSt.setDateSt(new Date(System.currentTimeMillis()));
			insSgsybSt.setAmount(Double.valueOf(insSgsybSt.getAkc264()));
			insSgsybSt.setAmtGrzhzf(Double.valueOf(insSgsybSt.getAkb066()));
			insSgsybSt.setAmtGrzf(Double.valueOf(insSgsybSt.getBka831()));
			insSgsybSt.setAmtJjzf(Double.valueOf(insSgsybSt.getBka832()));
			ApplicationUtils.setDefaultValue(insSgsybSt,true);
			try {
				DataBaseHelper.insertBean(insSgsybSt);
			} catch (Exception e) {
//				保存结算信息失败的话,结算撤销
				HashMap<String, String> settleCancelParam = new HashMap<>();
				settleCancelParam.put("function_id","bizh120107");
				settleCancelParam.put("akb020",INSUSERID);
				settleCancelParam.put("aaz218",aaz218);
				String cancelParamJson = ApplicationUtils.beanToJson(settleCancelParam);
				String cancelResult = ybFileSigned(cancelParamJson, user);
				InsSgsPubParam cancelSettle = JsonUtil.readValue(cancelResult, InsSgsPubParam.class);
				if(!"1".equals(cancelSettle.getReturn_code())){
					responseJson.setStatus(-1);
					responseJson.setDesc("bizh120107|"+cancelParamJson+"|HIS保存医保结算信息失败,医保结算撤销失败,原因:"+cancelSettle.getReturn_code_message());
					return responseJson;
				}
//				出院登记撤销
				HashMap<String, String> disCancelParam = new HashMap<>();
				disCancelParam.put("function_id","bizh120108");
				disCancelParam.put("akb020",INSUSERID);
				disCancelParam.put("aaz218",aaz218);
				String disCancelJson = ApplicationUtils.beanToJson(disCancelParam);
				String disCancelResult = ybFileSigned(disCancelJson, user);
				InsSgsPubParam disCancel = JsonUtil.readValue(disCancelResult, InsSgsPubParam.class);
				if(!"1".equals(disCancel)){
					responseJson.setStatus(-1);
					responseJson.setDesc("bizh120108|"+disCancelJson+"|HIS保存医保结算信息失败,出院登记撤销失败,原因:"+disCancel.getReturn_code_message());
					return responseJson;
				}
				responseJson.setStatus(-1);
				responseJson.setDesc("bizh120106|"+paramJson+"|HIS保存医保结算信息失败,原因:"+e.getMessage());
				return responseJson;
			}
			//            装配返回结果
			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put("Grzhzf",insSgsybSt.getAkb066());
			resultMap.put("YbSettleInfo",settle);
			resultMap.put("SettlementInfo",map);
			responseJson.setData(resultMap);
			return responseJson;
		}else{
			responseJson.setStatus(-1);
			responseJson.setDesc("bizh120106|"+paramJson+"|出院结算失败,原因:"+settle.getReturn_code_message());
			return responseJson;
		}
	}

	/**
	 * 住院登记信息修改
	 * 015001014019
	 * @param param
	 * @param user
	 */
	public ResponseJson hpAdtChange(String param,IUser user){
		ResponseJson responseJson = new ResponseJson();
		ZyRegModifyParam modifyParam = JsonUtil.readValue(param, ZyRegModifyParam.class);
		modifyParam.setFunctionId("bizh120104");
		modifyParam.setAkb020(INSUSERID);
//		查询病区编码和名称
		String pkDeptNs = modifyParam.getBka021();
		if(StringUtils.isNotBlank(pkDeptNs)){
			BdOuDept dept = zsrmSgsMapper.qryDeptInfoByPkDept(pkDeptNs);
			modifyParam.setBka021(dept.getCodeDept());
			modifyParam.setBka022(dept.getNameDept());
		}
//		查询科室编码和名称
		String pkDept = modifyParam.getBka019();
		if(StringUtils.isNotBlank(pkDept)){
			BdOuDept dept = zsrmSgsMapper.qryDeptInfoByPkDept(pkDept);
			modifyParam.setBka019(dept.getCodeDept());
			modifyParam.setBka020(dept.getNameDept());
		}
		modifyParam.setBka025(modifyParam.getZyRegInfo().getBka025());
//		修改医保住院登记信息
		String paramJson = ApplicationUtils.beanToJson(modifyParam);
		String result = ybFileSigned(paramJson, user);
		InsSgsPubParam insSgsPubParam = JsonUtil.readValue(result, InsSgsPubParam.class);
		if(!"1".equals(insSgsPubParam.getReturn_code())){
			responseJson.setStatus(-1);
			responseJson.setDesc("bizh120104|"+paramJson+"|修改住院登记信息失败,原因:"+insSgsPubParam.getReturn_code_message());
			return responseJson;
		}else{
			return responseJson;
//			保存修改信息,ins_sgsyb_visit中没有这些字段,无需更新
//			InsSgsybVisit visit = new InsSgsybVisit();
//			ApplicationUtils.copyProperties(visit,modifyParam);
//			zsrmSgsMapper.updateInsSgsYbVisit(visit);
		}
	}


	/**
	 * 查询患者的住院信息
	 * 015001014020
	 * @param param
	 * @param user
	 * @return
	 */
	public ZyHpDisParam getIpInfo(String param,IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paramMap.get("pkPv");
		if(StringUtils.isBlank(pkPv)){
			throw new BusException("查询患者住院信息失败,参数中pkpv为空");
		}
		ZyHpDisParam disParam = zsrmSgsMapper.qryIpInfoByPkPv(pkPv);
		return disParam;
	}

	/**
	 * 入院登记撤销,出院登记撤销,删除住院业务费用明细,取消出院结算
	 * 015001014022
	 * @param param
	 * @param user
	 */
	public ResponseJson zyPubCancel(String param,IUser user){
		ResponseJson responseJson = new ResponseJson();
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String functionId = (String) paramMap.get("functionId");
		paramMap.put("akb020",INSUSERID);
		paramMap.put("function_id",functionId);
		String paramJson = ApplicationUtils.beanToJson(paramMap);
		String result = ybFileSigned(paramJson, user);
		InsSgsPubParam insSgsPubParam = JsonUtil.readValue(result, InsSgsPubParam.class);
		if(!"1".equals(insSgsPubParam.getReturn_code())){
			responseJson.setStatus(-1);
			responseJson.setDesc(functionId+"|"+paramJson+"|"+insSgsPubParam.getReturn_code_message());
		}
		return responseJson;
	}

	/**
	 * 住院结算信息提取
	 * 015001014023
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSgsZySettleInfo getZySettleInfo(String param,IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("akb020",INSUSERID);
		String result = ybFileSigned(ApplicationUtils.beanToJson(paramMap), user);
		InsSgsZySettleInfo settleInfo = JsonUtil.readValue(result, InsSgsZySettleInfo.class);
		if("1".equals(settleInfo.getReturn_code())){
			return settleInfo;
		}else{
			throw new BusException("住院结算信息提取出错,愿意:"+settleInfo.getReturn_code_message());
		}
	}

	/**
	 * 查询住院收费明细信息
	 * 015001014024
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ZyFeeDetail> getZyFeeDetail(String param,IUser user){
		ZyFeeDetailParam zyFeeDetailParam = JsonUtil.readValue(param, ZyFeeDetailParam.class);
		zyFeeDetailParam.setAkb020(INSUSERID);
		String result = ybFileSigned(ApplicationUtils.beanToJson(zyFeeDetailParam), user);
		InsSgsZyFeeDetail feeDetail = JsonUtil.readValue(result, InsSgsZyFeeDetail.class);
		if("1".equals(feeDetail.getReturn_code())){
			return feeDetail.getList();
		}else{
			throw new BusException("查询住院收费明细失败,原因:"+feeDetail.getReturn_code_message());
		}
	}
}
