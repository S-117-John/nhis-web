package com.zebone.nhis.pro.zsba.mz.ins.zsba.service.sgs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

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

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.FileUtils;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.dao.ZsbaSgsMapper;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.Injuryorbirthinfo;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybSt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsSgsPi;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsSgsybSt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsSgsybVisit;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.Personinfo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ZsbaSgsService {

	private Logger logger = LoggerFactory.getLogger("nhis.ZsbaSGSLog");

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
	private ZsbaSgsMapper zsbaSgsMapper;

	/**
	 * 门诊收费预结算
	 * 015001014001->022003027037
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

		List<Map<String, Object>> dts = zsbaSgsMapper.qryChargeDetailNoUpload(paramMap);

		// 待上传明细为空，直接反回，可能只有特需费用
		if (dts == null || dts.size() == 0) {

			Double dtAmt = 0d;
			// 检索非特需费用
			List<BlOpDt> dtRet = zsbaSgsMapper.qryChargeDetailNoUploadSpec(paramMap);
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
			//insSgsybSt.setAmtGrzhzf(new Double(insSgsybSt.getAkb066()));// 个人账户
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
	 * 门诊收费
	 * 015001014002->022003027038
	 */
	public Map<String, Object> mzHpJiaokuan(String params, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// 公共参数
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> retMap = new HashMap<>();
		String retStr = "";
		
		String pkPv = paramMap.get("pkPv").toString();
		
		// 封装医保结算参数
		reqMap = (Map<String, Object>) paramMap.get("ybPreSettlParam");
		if (reqMap.containsKey("function_id")) {
			reqMap.put("function_id", "bizh110105");
		}
		if (reqMap.containsKey("bka893")) {
			reqMap.put("bka893", "1");
		}
		// 调用结算
		retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user);
		// 解析验证结算结果
		retMap = JsonUtil.readValue(retStr, Map.class);
		if ("1".equals(CommonUtils.getString(retMap.get("return_code")))) {
			
			List<Map<String, Object>> tmpRet = (List<Map<String, Object>>) retMap.get("payinfo");
			Map<String, Object> stMap = tmpRet.get(0);

			// 查询医保就诊登记
			String visitSql = "select top 1 * from ins_sgsyb_visit where pk_pv=? order by create_time desc ";
			InsSgsybVisit visit = DataBaseHelper.queryForBean(visitSql, InsSgsybVisit.class, pkPv);

			InsSgsybSt insSgsybSt = JsonUtil.readValue(JsonUtil.writeValueAsString(stMap), InsSgsybSt.class);
			insSgsybSt.setPkPv(pkPv);
			insSgsybSt.setPkPi(visit.getPkPi());
			insSgsybSt.setPkHp(visit.getPkHp());
			insSgsybSt.setPkVisit(visit.getPkVisit());

			insSgsybSt.setAmount(new Double(insSgsybSt.getAkc264()));// 总金额
			//insSgsybSt.setAmtGrzhzf(new Double(insSgsybSt.getAkb066()));// 个人账户
			insSgsybSt.setAmtGrzf(new Double(insSgsybSt.getBka831()));// 现金
			insSgsybSt.setAmtJjzf(new Double(insSgsybSt.getBka832()));// 基金
			insSgsybSt.setYbPksettle(insSgsybSt.getAaz218() + "_" + insSgsybSt.getBka001());

			insertInsSt(insSgsybSt);

			retMap = stMap;
			retMap.put("yBPkSettle", insSgsybSt.getYbPksettle());

			/* 暂留，因结算接口已返回费用序号 // 调-次 退费时提取门诊业务信息 目的是获取批次号 
			reqMap.clear(); 
			reqMap.put("function_id", "bizh110103"); 
			reqMap.put("bka895", "aaz218");
			reqMap.put("bka896", visit.getAaz218()); 
			reqMap.put("akb020", USERID); 
			reqMap.put("bka006", "410");
			reqMap.put("bka001", "0"); // 0-获取所有结算批次信息 
			retStr = ybFileSigned(ApplicationUtils.beanToJson(reqMap), user); 
			*/

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
			// 兼容自助机、公众号未传递费用明细的情况
			if(paramMap.containsKey("codeEmp") && paramMap.get("codeEmp").toString().startsWith("999")){
				errMsg = "";
			}else{
				errMsg = "未传入费用信息(pkCgops)";
			}
		}else{
			Set<String> pkCgops = new HashSet<String>((List<String>) paramMap.get("pkCgops"));
			String sql = "select * from bl_op_dt where flag_settle='1' and pk_pv=?" + " and pk_cgop in("+ CommonUtils.convertSetToSqlInPart(pkCgops, "pk_cgop") + ")";
			List<Map<String,Object>> list = DataBaseHelper.queryForList(sql, paramMap.get("pkPv"));
			if (list.size() > 0) {
				errMsg = "数据已变更，请刷新重试";
			}
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
		
		// 查询医保登记信息
		InsSgsybVisit piVisit = DataBaseHelper.queryForBean("select top 1 * from ins_sgsyb_visit where pk_pv=? and del_flag='0'", InsSgsybVisit.class, pkPv);

		reqMap = zsbaSgsMapper.qryInsBasePre(pkPv);

		reqMap=(Map<String, Object>)paramMap.get("yBPatientInfo");
		InsSgsPi pi =   JsonUtil.readValue(ApplicationUtils.beanToJson(reqMap), InsSgsPi.class);

		if (piVisit == null || CommonUtils.isNull(piVisit.getAaz218()))// 医保挂号
		{
			Personinfo piInfo = pi.getPersoninfo().get(0);
			Injuryorbirthinfo injuryorbirthinfo = new Injuryorbirthinfo();
			if (pi.getInjuryorbirthinfo() != null) {
				injuryorbirthinfo = pi.getInjuryorbirthinfo().get(0);
			}

			reqMap = zsbaSgsMapper.qryInsRegPre(pkPv);
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
				PvEncounter pvencounter = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=? and eu_pvtype in (1,2,4) and del_flag='0' ", PvEncounter.class, pkPv);
				BdHp hp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp=? and del_flag='0' ", BdHp.class, pvencounter.getPkInsu());
				if (hp == null) {
					throw new RuntimeException("医保登记失败,未查询到对应的险种信息" + pvencounter.getCodePv() + pvencounter.getPkInsu());
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
					platformTransactionManager.rollback(status); // 添加失败 回滚事务
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
	 * 015001014009->022003027104
	 */
	public InsSgsPi getPersonInfo(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		
		String pkPv=CommonUtils.getPropValueStr(paramMap, "pkPv");
		String bka895=CommonUtils.getPropValueStr(paramMap, "bka895");
		if(CommonUtils.isNotNull(pkPv) && "aac001".equals(bka895)){
			
		} else if(CommonUtils.isNotNull(pkPv)){
			Map<String, Object> baseMap = zsbaSgsMapper.qryInsBasePre(pkPv);
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
	 * 门诊退费预结算
	 * 015001014003->022003027039
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
				List<BlOpDt> allDts = zsbaSgsMapper.qryChargeDts(reqMap);
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
		Map<String, Object> stMap = zsbaSgsMapper.qryBlSt(paramMap);
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
		List<Map<String, Object>> dts = zsbaSgsMapper.qryChargeDetailNoUpload(reqMap);
		
		InsSgsybSt insSgsSt=new InsSgsybSt();
		Double amtAdd = 0d;//特诊加收金额
		Double amtSpec = 0d;//特需费用
		List<BlOpDt> allDts = zsbaSgsMapper.qryChargeDts(reqMap);
		for (BlOpDt dt : allDts) {
			amtAdd= amtAdd + dt.getAmountAdd();
			if("96".equals(dt.getCodeBill())){
				amtSpec =amtSpec + dt.getAmount();	
			}		
		}
		
		/**单条明细部分退，金额=单价*重收数量**/
		/*for(BlOpDt newOp :newOpList){
			for(Map<String, Object> dt :dts){
				if(newOp.getPkCgop().equals(dt.get("pkCgop"))){
					dt.put("bka057", newOp.getQuan());//数量
					dt.put("bka058", ((newOp.getAmount()-newOp.getAmountAdd())/newOp.getQuan())*newOp.getQuan());//总额
				}
			}
		}*/
		
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
			//insStCancel.setAmtGrzhzf(new Double(insStCancel.getAkb066()));// 个人账户
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
	 * 门诊退费
	 * 015001014004->022003027040
	 */
	@SuppressWarnings("unchecked")
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
				throw new BusException("结算主键：" + paramMap.get("pkSettle") + "，未查询到省工伤医保结算记录！");
				//return null;
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
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			//退费医保结算主键要返回前端传到HIS正式退费结算接口用作HIS退费结算主键等数据回写
			String ybCancelPksettle = reqMap.get("ybPksettle")!=null?String.valueOf(reqMap.get("ybPksettle")):"";
			logger.info("工伤医保退费结算主键==>>"+ybCancelPksettle);
			
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
						logger.info("工伤医保部分退费主键==>>"+ybCancelPksettle+"；重新结算主键==>>"+rePayIns.get("ybPksettle"));
						rePayIns.put("setlId", rePayIns.get("ybPksettle"));
						rePayIns.put("ybCancelPksettle", ybCancelPksettle);
						return rePayIns;
					} else {
						throw new BusException("医保预退费失败，部分退返回信息错误");
					}
				}else{
					throw new BusException("医保退费失败，未获取到重收费用明细");
				}
			} else {
				returnMap.put("ybCancelPksettle", ybCancelPksettle);
				return returnMap;
			}
		} else {
			throw new BusException("结算主键" + paramMap.get("pkSettle") + " 未查询到医保结算记录");
		}
	}

	/**
	 * 医保预退费、退费接口 bka893='1'时 为退费
	 * 015001014006->022003027041
	 */
	public Map<String, Object> insSettleCancle(String params, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		InsSgsybVisit visit = new InsSgsybVisit();
		// 退费业务校验
		if (CommonUtils.isNotNull(paramMap.get("pkPv"))) {
			visit = DataBaseHelper.queryForBean("select top 1 * from ins_sgsyb_visit where pk_pv=?",
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
					if ("1".equals(retMap.get("return_code"))) {// 退费成功
						InsSgsybSt insSt = DataBaseHelper.queryForBean(
								"select * from ins_sgsyb_st where aaz218=? and bka001= ?", InsSgsybSt.class, aaz218,
								bka001);

						if (insSt != null) {
							List<Map<String, Object>> tmpRet = (List<Map<String, Object>>) retMap.get("payinfo");

							InsSgsybSt insStCancel = JsonUtil.readValue(JsonUtil.writeValueAsString(tmpRet.get(0)),
									InsSgsybSt.class);

							insStCancel.setAmount(new Double(insStCancel.getAkc264()));// 总金额
							//insStCancel.setAmtGrzhzf(new Double(insStCancel.getAkb066()));// 个人账户
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
								/**20210518查询如果存在相同医保结算主键的记录则不再重复插入**/
								InsSgsybSt insSgsSt = DataBaseHelper.queryForBean("select * from ins_sgsyb_st where YB_PKSETTLE=?", InsSgsybSt.class, insStCancel.getYbPksettle());
								if(insSgsSt==null){
									insertInsSt(insStCancel);
								}
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
	 * 将PkSettle结算主键更新到INS_SGSYB_ST表
	 * 015001014005->022003027042
	 */
	public void updatePkSettle(String params, IUser user) {
		logger.debug("将PkSettle结算主键更新到INS_SGSYB_ST表==>>"+params);
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
		String pkPv = paramMap.get("pkPv").toString();

		String pkSettle = null;
		String pkSettleCanc = null;
		String ywlx = null;// 业务类型
		String ybPksettle = null;// 医保结算主键
		String reChargeYbPksettle = null;// 医保部分退重新结算主键

		// 结算主键
		if (paramMap.containsKey("pkSettle") && paramMap.get("pkSettle") != null) {
			pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		}
		// 取消结算主键
		if (paramMap.containsKey("pkSettleCanc") && paramMap.get("pkSettleCanc") != null) {
			pkSettleCanc = CommonUtils.getString(paramMap.get("pkSettleCanc"));
		}
		if (paramMap.containsKey("yWLX") && paramMap.get("yWLX") != null) {
			ywlx = CommonUtils.getString(paramMap.get("yWLX"));
		}
		// 医保结算ID(收费或者退费)
		if (paramMap.containsKey("pkPtmzjs") && paramMap.get("pkPtmzjs") != null) {
			ybPksettle = CommonUtils.getString(paramMap.get("pkPtmzjs"));
		}
		// 部分退医保重结ID
		if (paramMap.containsKey("jsId") && paramMap.get("jsId") != null) {
			reChargeYbPksettle = CommonUtils.getString(paramMap.get("jsId"));
		}
		if ("1".equals(CommonUtils.getString(ywlx))) {// 门诊结算
			// 部分退重结
			if (CommonUtils.isNotNull(pkSettle) && CommonUtils.isNotNull(pkSettleCanc) && CommonUtils.isNotNull(ybPksettle) && CommonUtils.isNotNull(reChargeYbPksettle)) {
				DataBaseHelper.execute("update ins_sgsyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and yb_pksettle = ?", pkSettle, pkPv, reChargeYbPksettle);//重新结算
				DataBaseHelper.execute("update ins_sgsyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and yb_pksettle = ?", pkSettleCanc, pkPv, ybPksettle);//全退
				return;
			}
			// 收费
			if (CommonUtils.isNotNull(pkSettle) && CommonUtils.isNotNull(ybPksettle)) {
				DataBaseHelper.execute("update ins_sgsyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and yb_pksettle = ?", pkSettle, pkPv, ybPksettle);
				return;
			}
			// 退费
			if (CommonUtils.isNotNull(pkSettleCanc) && CommonUtils.isNotNull(ybPksettle)) {
				DataBaseHelper.execute("update ins_sgsyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and yb_pksettle = ?", pkSettleCanc, pkPv, ybPksettle);
				return;
			}
			/*if (CommonUtils.isEmptyString(pkSettle) && !CommonUtils.isEmptyString(ybPksettle)) {
				String pkSettleCancel = zsbaSgsMapper.qryPksettleCanc(pkPv, ybPksettle);
				DataBaseHelper.execute(
						"update ins_sgsyb_st set pk_settle =?  where del_flag='0' and PK_PV=? and yb_pksettle = ?",
						pkSettleCancel, pkPv, ybPksettle);
				return;
			}
			if (!CommonUtils.isEmptyString(ybPksettle)) {
				DataBaseHelper.execute(
						"update ins_sgsyb_st set pk_settle=? where del_flag='0' and PK_PV=? and yb_pksettle = ?",
						pkSettle, pkPv, ybPksettle);
			}*/
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
		logger.info(functionId + " 入参：" + paramsJson);
		FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：医保功能号："+functionId + "\n"+paramsJson, "mzyb-"+UserContext.getUser().getCodeEmp()+".txt");
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

				if ("bizh110104".equals(functionId) || "bizh110105".equals(functionId) || "bizh110106".equals(functionId)) {
					User u = (User) user;
					paramsObj.put("bka014", u.getCodeEmp());
					paramsObj.put("bka015", u.getNameEmp());
				}
			}
			paramsJson = ZsbaSgsUtils.makeXml(paramsObj);
			//发送http请求
			String result = HttpClientUtil.sendHttpPost(REJECTED_URL, paramsJson, new HashMap<String, String>());
			logger.info(functionId + " 出参：" + result);
			FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 出参：医保功能号："+functionId + "\n"+result, "mzyb-"+UserContext.getUser().getCodeEmp()+".txt");
			json = TestXml2Json.xml2Json(result);
			resultState = json.get("return_code").toString();
			if("1".equals(resultState)) {
				return json.toString();
			}else if ("-9".equals(resultState)) {
				ybLogin(user);//登录session_id失效，需要重新登录
			} else {
				String jsonStr = "{'return_code':'-1','return_code_message':'医保返回的错误信息：" + json.get("return_code_message").toString() + "'}";
				json = JSONObject.parseObject(jsonStr);
			}
		} catch (Exception e) {
			logger.error("sendHttpPostError:", e.getMessage());
			String jsonStr = "{'return_code':'-1','return_code_message':'" + e.getMessage() + "'}";
			json = JSONObject.parseObject(jsonStr);
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
	 * 015001014008->022003027043
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getSGSCheckInsuranceSum(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
		return zsbaSgsMapper.qrySgsMedicalInsuranceSum(mapParam);
	}
}