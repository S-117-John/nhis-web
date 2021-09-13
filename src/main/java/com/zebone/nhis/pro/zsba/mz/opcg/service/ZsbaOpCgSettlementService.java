package com.zebone.nhis.pro.zsba.mz.opcg.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.pub.service.BlOpMedicalExeService;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.bl.pub.syx.vo.BlOpDtRefundVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.BlPubSettleVo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdDept;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpClientUtils;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlOpDt;
import com.zebone.nhis.pro.zsba.mz.bl.service.ZsbaSelfPayTrInsuService;
import com.zebone.nhis.pro.zsba.mz.opcg.dao.ZsbaOpCgQueryMapper;
import com.zebone.nhis.pro.zsba.mz.opcg.vo.IsStopOpParamVo;
import com.zebone.nhis.pro.zsba.mz.opcg.vo.PibaseVo;
import com.zebone.nhis.pro.zsba.mz.opcg.vo.ZsbaOpCgTransforVo;
import com.zebone.nhis.pro.zsba.mz.pub.service.ZsbaOpCgPubService;
import com.zebone.nhis.pro.zsba.mz.pub.service.ZsbaSettlePdOutService;
import com.zebone.nhis.scm.pub.vo.PdStockVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊费用结算服务
 * @author gongxy
 */
@Service
public class ZsbaOpCgSettlementService {
	
	private static Logger log = LoggerFactory.getLogger(ZsbaOpCgSettlementService.class);

	@Autowired private ZsbaOpCgPubService opcgPubService;
	@Autowired private OpcgPubHelperService opcgPubHelperService;
	@Autowired private CgQryMaintainService cgQryMaintainService;
	@Autowired private CommonService commonService;
	@Autowired private PareAccoutService pareAccoutService;
	@Autowired private ZsbaSettlePdOutService settlePdOutService; // 处理库存、处方和医技执行单
	@Autowired private ZsbaOpCgQueryMapper opCgQueryMapper;
	@Resource private BlOpMedicalExeService medicalExeService;
	@Autowired private BalAccoutService balAccoutService;
	@Autowired private ZsbaSelfPayTrInsuService zsbaSelfPayTrInsuService;
	
	/**
	 * 挂号预结算
	 * 007002001001->022003027049
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Map<String, Object>> countRegisteredAccountingSettlement(String param, IUser user) {

		// 考虑到一个病人挂多个号
		List<Map<String, Object>> mapListParam = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
		});
		
		if(mapListParam == null || mapListParam.size()<=0)
			throw new BusException("未获取到结算相关信息！");
		//校验患者是否在院，且是否主医保计划不允许门诊住院同时使用
		verfyPiHp(mapListParam);
		List<Map<String, Object>> mapResultList = new ArrayList<Map<String, Object>>();
		
		BigDecimal aggregateAmount = BigDecimal.ZERO;// 金额合计
		BigDecimal medicarePayments = BigDecimal.ZERO;// 医保支付
		BigDecimal accountBalance = BigDecimal.ZERO;// 账户余额
		BigDecimal accLimit = BigDecimal.ZERO;// 账户可用余额（考虑信用额度）
		BigDecimal creditAcc = BigDecimal.ZERO;// 信用额度
		//Map<String, Double> result = new HashMap<String, Double>();
		String pkPi = mapListParam.get(0).get("pkPi").toString();
		//是否是诊间支付
		boolean isClinicFlag = false;
		if(mapListParam.get(0).get("isClinicFlag")!= null){
		isClinicFlag = BlcgUtil.converToTrueOrFalse(mapListParam.get(0).get("isClinicFlag").toString());
		}
		if (EnumerateParameter.ZERO.equals(ApplicationUtils.getSysparam("PI0007", false))) {//判断是否是读卡模式
			Map<String, BigDecimal> map = accountBalance(mapListParam, pkPi);
			accountBalance = map.get("accountBalance");
			accLimit = map.get("accLimit");
			creditAcc = map.get("creditAcc");
		}else { //读卡模式
			//判断是否有卡号
			if (mapListParam.get(0).get("cardNo")!=null&&!StringUtils.isEmpty(mapListParam.get(0).get("cardNo").toString())) {		 
			 Map<String, BigDecimal> map = accountBalance(mapListParam, pkPi);
			 accountBalance = map.get("accountBalance");
			 accLimit = map.get("accLimit");
			 creditAcc = map.get("creditAcc");
			}else {
				accountBalance = BigDecimal.ZERO;
			}
		}
		BigDecimal accountReceivable = BigDecimal.ZERO;//结算应收
		BigDecimal accountTotalPay = BigDecimal.ZERO;//账户支付
		for (Map<String, Object> mapParam : mapListParam) {
			mapParam.put("pkOrg", ((User) user).getPkOrg());
			// 一次算一个挂号的金额
			Map resultOnePati = opcgPubService.registerPreParedSettlement(mapParam);
			Map<String, Object> mapList = new HashMap<>();
			mapList.put("orderNo", mapParam.get("orderNo"));// 加入排序号
			BigDecimal patientsPay = new BigDecimal(resultOnePati.get("patientsPay")==null?"0":resultOnePati.get("patientsPay").toString());// 患者支付
			//如果是诊间支付，可以使用信用额度，否则不可以使用信用额度
			if(isClinicFlag){
				//使用累加了信用额度的账户余额判断患者余额是否够用
				if (accLimit.compareTo(patientsPay)>=0) {
					accountTotalPay = accountTotalPay.add(patientsPay);
					mapList.put("accountPay", accountTotalPay);
					accountBalance = accountBalance.subtract(patientsPay);
					mapList.put("accountReceivable", 0D);
				} else {
					throw new BusException("账户余额不足，请充值！"+"  本次挂号费为"+patientsPay+"元， 账户余额"+accountBalance+"元。");
				}
			}else{
				//使用患者账户实际余额判断是否够用
				if (accountBalance.compareTo(patientsPay)>=0) {
					accountTotalPay = accountTotalPay.add(patientsPay);
					mapList.put("accountPay", accountTotalPay);
					accountBalance = accountBalance.subtract(patientsPay);
					mapList.put("accountReceivable", 0D);//结算应收 = 患者支付-账户支付
				} else {
					accountTotalPay = accountTotalPay.add(accountBalance);
					mapList.put("accountPay", accountTotalPay);
					//账户余额小于患者支付额度，账户余额全部被使用，因此置为0
					//accountBalance = accountBalance.subtract(accLimit);
					accountBalance = BigDecimal.ZERO;
					//结算应收，不追加信用额度，杨雪修改
					//accountReceivable = accountReceivable.add((patientsPay.subtract(accLimit)));
					//结算应收 = 患者支付-账户支付
					accountReceivable = accountReceivable.add((patientsPay.subtract(accountTotalPay)));
					mapList.put("accountReceivable", accountReceivable);
				}
			}
			accLimit = accountBalance.add(creditAcc);
			
			aggregateAmount = new BigDecimal(resultOnePati.get("aggregateAmount")==null?"0":resultOnePati.get("aggregateAmount").toString());
			medicarePayments = new BigDecimal(resultOnePati.get("medicarePayments")==null?"0":resultOnePati.get("medicarePayments").toString());
			
			mapList.put("aggregateAmount", aggregateAmount);// 合计金额
			mapList.put("medicarePayments", medicarePayments);// 医保支付
			mapList.put("patientsPay", patientsPay);// 患者支付
			mapList.put("accountBalance", accountBalance);//账户余额
			
			mapResultList.add(mapList);
		}
		
		return mapResultList;
	}
	
	/**
	 * 校验主医保计划是否不允许门诊住院同时使用
	 */
    private void verfyPiHp(List<Map<String, Object>> mapListParam){
    	String pkPi = mapListParam.get(0).get("pkPi").toString();
    	PvEncounter pv = opCgQueryMapper.queryPvAndHpInof(pkPi);
    	if(pv==null||CommonUtils.isEmptyString(pv.getPkPv()))
    		return;
    	for(Map<String, Object> param:mapListParam){
    		List<Map<String, Object>> insuLists = (List<Map<String, Object>>) param.get("insuList");// 医保计划主键(包括辅医保主键)
    		if(insuLists==null||insuLists.size()<=0) return;
    		String pkInsuMain = "";
    		for (Map<String, Object> mapTemp : insuLists) {
    			if (EnumerateParameter.ONE.equals(mapTemp.get("flagMaj").toString())) {
    				pkInsuMain = mapTemp.get("pkHp").toString();
    			}
    		}
    		if(pkInsuMain!=null&&pkInsuMain.equals(pv.getPkInsu())){
    			throw new BusException("该患者已在院，所使用的主医保不允许门诊住院同时使用！");
    		}
    	}
    	
    }
	
    /**
	 * 退号预结算
	 * 007002001002->022003027050
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OpCgTransforVo countRefoundRegisteredSettlement(String param, IUser user) {

		// 支持单个号退
		Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);
		mapParam.put("pkOrg", ((User) user).getPkOrg());
		mapParam.put("pkEmp", ((User) user).getPkEmp());
		mapParam.put("empName", ((User) user).getNameEmp());
		OpCgTransforVo rrpv = opcgPubService.registrationPreparedRefound(mapParam);
		return rrpv;
	}

	/**
	 * 查询患者未结算的收费信息（仅挂号）
	 * 交易号： 007002003025->022003027052
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettleOnlyPv(String param, IUser user) {

		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		opCgTransforVo.setCurDate(new Date());
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkPi", opCgTransforVo.getPkPi());
		mapParam.put("curDate", opCgTransforVo.getCurDate());
		mapParam.put("pkOrg", ((User) user).getPkOrg());
		if (opCgTransforVo.getPkPv() != null) {
			mapParam.put("pkPv", opCgTransforVo.getPkPv());
		}
		List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettleOnlyPv(mapParam);
		return mapResult;
	}
	
	/**
	 * 暂不知哪里用到
	 * 根据患者就诊主键pk_pv查询本次就诊待支付费用
	 * 007002003030->022003027053
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettleBypkPv(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String pkPv = map.get("pkPv").toString();
		List<BlPatiCgInfoNotSettleVO> list = queryPatiCgInfoNotSettleBypkPvSql(pkPv);
		return list;
	}
	//查询本次就诊待支付费用
	private List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettleBypkPvSql(String pkPv) {
		StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" cate.name itemcate, pres.pres_no presNo, dt.name_cg nameCg, dt.spec, ");
			sql.append(" unit.name unit, dt.quan, dt.amount, dt.amount_pi amountPi, dt.pk_cnord pkCnord,  ");
			sql.append(" dt.flag_pv flagPv, bod.name_dept nameDept, bod.code_dept codeDept, pres.dt_prestype, ");
			sql.append(" dt.pk_dept_ex pkDeptEx, pd.eu_drugtype euDrugType, dt.name_emp_app nameEmpOrd, ");
			sql.append(" dt.pk_emp_app pkEmpApp, boe.code_emp, pd.code AS itemCode ");
		sql.append(" FROM ");
			sql.append(" bl_op_dt dt ");
			sql.append(" INNER JOIN bd_itemcate cate ON dt.pk_itemcate=cate.pk_itemcate ");
			sql.append(" LEFT JOIN bd_unit unit ON dt.pk_unit=unit.pk_unit ");
			sql.append(" LEFT OUTER JOIN cn_prescription pres ON dt.pk_pres=pres.pk_pres ");
			sql.append(" LEFT OUTER JOIN bd_pd pd ON dt.pk_pd=pd.pk_pd ");
			sql.append(" LEFT JOIN bd_ou_dept bod ON dt.pk_dept_app=bod.pk_dept ");
			sql.append(" LEFT JOIN bd_ou_employee boe ON dt.pk_emp_app=boe.pk_emp ");
			sql.append(" LEFT JOIN CN_ORDER ord on ord.PK_CNORD = dt.PK_CNORD ");
		sql.append(" WHERE ");
			sql.append(" dt.FLAG_SETTLE='0' AND dt.DEL_FLAG='0' AND dt.FLAG_ACC='0' AND dt.PK_CGOP_BACK is null ");
//			sql.append(" and (dt.PK_CNORD is null or ord.DATE_EFFE>=GETDATE()) ");//医嘱有效时间大于当前时间
			sql.append(" and (pres.DT_PRESTYPE!='07' or pres.DT_PRESTYPE is NULL) ");//过滤外流处方
			sql.append(" AND dt.pk_pv=? ");
		return DataBaseHelper.queryForList(sql.toString(), BlPatiCgInfoNotSettleVO.class, pkPv);
	}
	

	/**
	 * 窗口收费处用
	 * 根据患者的主键查询患者未结算的收费信息(包括处方，检查，检验)
	 * 007002003001->022003027051
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettle(String param, IUser user) {

		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		/*是否不过滤医嘱有效期
		if(!EnumerateParameter.ONE.equals(opCgTransforVo.getFilterOrdEffeDate())) {
			String curtime = DateUtils.getDateTimeStr(new Date());
			mapParam.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
		}*/
		mapParam.put("pkPi", opCgTransforVo.getPkPi());
		mapParam.put("pkOrg", ((User) user).getPkOrg());
		if (opCgTransforVo.getPkPv() != null) {
			mapParam.put("pkPv", opCgTransforVo.getPkPv());
		}
		if (opCgTransforVo.getPkDeptExec() != null) {
			mapParam.put("pkDeptExec", opCgTransforVo.getPkDeptExec());
		}
		mapParam.put("notDisplayFlagPv", opCgTransforVo.getNotDisplayFlagPv());
		mapParam.put("isNotShowPv", opCgTransforVo.getIsNotShowPv());
		List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettle(mapParam);
		String isShowWinno = ApplicationUtils.getSysparam("EX0079",false);
		if("1".equals(isShowWinno)){
			Object resObj=ExtSystemProcessUtils.processExtMethod("DrugWinnoRule", "getNotSettlePresInfo",mapResult);
			if(resObj!=null ){
				return (List<BlPatiCgInfoNotSettleVO>)resObj;
			}
		}
		if(!mapResult.isEmpty()){
			//同一次就诊有一部分已结算时，再次结算部分时需要把就诊状态改回就诊结束
			String updatePvSql = "update PV_ENCOUNTER set EU_STATUS='2' where EU_STATUS='3' and PK_PV=?";
			DataBaseHelper.execute(updatePvSql, opCgTransforVo.getPkPv());
		}
		
		/**遍历明细列表返回自备药标志**/
		if(mapResult.size()>0) {
			Set<String> pkCgops = new HashSet<String>();
			for(BlPatiCgInfoNotSettleVO vo : mapResult){
				pkCgops.add(vo.getPkCgop());
			}
			String pkCgopInSql = CommonUtils.convertSetToSqlInPart(pkCgops, "pk_cgop");
			String sql = "select dt.PK_CGOP pkcgop,ISNULL(ord.FLAG_SELF, '0') flagself from BL_OP_DT dt INNER JOIN CN_ORDER ord on ord.PK_CNORD=dt.PK_CNORD where dt.PK_CGOP in (" + pkCgopInSql + ")";
			List<Map<String,Object>> blOpDts = DataBaseHelper.queryForList(sql, new Object[]{});
			for(BlPatiCgInfoNotSettleVO vo : mapResult){
				for(Map<String,Object> dt : blOpDts){
					if(vo.getPkCgop().equals(String.valueOf(dt.get("pkcgop")))){
						vo.setFlagSelf(String.valueOf(dt.get("flagself")));
						break;
					}
				}
			}
		}
		
		return mapResult;
	}
	
	
	/**
	 * 门诊收费预结算服务
	 * 交易号：007002003002->022003027054
	 * @param param
	 * @param user
	 * @return
	 */
	public OpCgTransforVo countOpcgAccountingSettlement(String param, IUser user) {
		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		/* 验证是否存在 合理用药 未审核或不通过的数据 */
		List<Map<String, Object>> listMap = getEuPass(opCgTransforVo.getPkPv());
		if(!listMap.isEmpty()){
			throw new BusException("处方审核结果（不通过或者正在人工审核）");
		}
		
		String pvSql = "select EU_STATUS, EU_LOCKED from PV_ENCOUNTER where PK_PV=?";
		Map<String, Object> pvMap = DataBaseHelper.queryForMap(pvSql, opCgTransforVo.getPkPv());
		if(pvMap==null){
			throw new BusException("未查询到有效就诊记录！");
		}
		/* 
		 * TODO：lipz - 获取参数：是否控制交费和就诊状态，1控制、0不控制
		 */
		String isLoadSql = "select top 1 code, name, val from BD_SYSPARAM where CODE=?";
		Map<String, Object> isLoadMap = DataBaseHelper.queryForMap(isLoadSql, "PV0057");
		if(isLoadMap!=null && isLoadMap.containsKey("val") && isLoadMap.get("val")!=null) {
			if("1".equals(isLoadMap.get("val").toString())) {
				if("1".equals(pvMap.get("euLocked").toString())) {
					throw new BusException("本次就诊（医生未诊毕），请稍候再试！");
				}
			}
		}
		
		// 门诊预结算考虑医保的金额计算
		OpCgTransforVo rgv = opcgPubService.useInsComputSettle(user, opCgTransforVo);
		
		// 读卡模式判断卡号是否为空
		if (opCgTransforVo.getCardNo()!=null&&!opCgTransforVo.getCardNo().isEmpty()) {
			// 查询返回账户余额、账户支付、患者自付金额
			rgv = accountPayment(opCgTransforVo, rgv);
		}else {
			rgv.setAccountBalance(BigDecimal.ZERO);
			rgv.setAccountPay(BigDecimal.ZERO);
			rgv.setAccountReceivable(rgv.getPatientsPay());	
		}	
		rgv.setPkPi(opCgTransforVo.getPkPi());
		rgv.setPkPv(opCgTransforVo.getPkPv());
		return rgv;
	}
	
	private List<Map<String, Object>> getEuPass(String pkPv){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SUM(dt.AMOUNT) AS amount, pres.eu_pass ");
		sql.append(" FROM BL_OP_DT dt LEFT OUTER JOIN cn_prescription pres ON dt.pk_pres=pres.pk_pres ");
		sql.append(" WHERE dt.del_flag='0' AND dt.pk_cgop_back IS NULL AND dt.flag_settle='0' ");
		sql.append(" AND pres.DT_PRESTYPE<>'07' and pres.eu_pass<>'0' ");
		sql.append(" AND dt.pk_pv=? GROUP BY pres.eu_pass ");
		return DataBaseHelper.queryForList(sql.toString(), pkPv); 
	}
	
	
	/**
	 * 门诊收费结算服务(一定要减少与数据库的交互,提高速度)
	 * 007002003003->022003027055
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OpCgTransforVo mergeOpcgAccountedSettlement(String param, IUser user) {
		log.debug("HIS正式结算接口入参："+param);
		// 1.必传入参验证
		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		ZsbaOpCgTransforVo zsbaOpCgTransforVo = JsonUtil.readValue(param, ZsbaOpCgTransforVo.class);
		if(CommonUtils.isEmptyString(opCgTransforVo.getPkPv())){
			throw new BusException("未传入参数pkPv!");
		}
		if(CommonUtils.isEmptyString(opCgTransforVo.getPkPi())){
			throw new BusException("未传入参数pkPi!");
		}
			
		// 2.判断是否对发票信息校验LB自助机使用, -2：不处理
		if(opCgTransforVo.getInvStatus()==null || !("-2").equals(opCgTransforVo.getInvStatus())){
			// 校验BL0008参数是否是1(结算打印发票)
			if ("1".equals(ApplicationUtils.getSysparam("BL0008", false)) 
					&& CollectionUtils.isEmpty(opCgTransforVo.getInvoiceInfo())){
				throw new BusException("未传入本次结算的发票信息！");
			}
		}
		
		// 结算时与就诊中的处理
		String pvSql = "select EU_STATUS, EU_LOCKED, EU_PVTYPE from PV_ENCOUNTER where PK_PV=?";
		Map<String, Object> pvMap = DataBaseHelper.queryForMap(pvSql, opCgTransforVo.getPkPv());
		if(pvMap==null){
			throw new BusException("未查询到有效就诊记录！");
		}
		/* 
		 * TODO：lipz - 获取参数：是否控制交费和就诊状态，1控制、0不控制
		 */
		String isLoadSql = "select top 1 code, name, val from BD_SYSPARAM where CODE=?";
		Map<String, Object> isLoadMap = DataBaseHelper.queryForMap(isLoadSql, "PV0057");
		if(isLoadMap!=null && isLoadMap.containsKey("val") && isLoadMap.get("val")!=null) {
			if("1".equals(isLoadMap.get("val").toString())) {
				if("1".equals(pvMap.get("euLocked").toString())) {
					throw new BusException("本次就诊（医生未诊毕），请稍候再试！");
				}
			}
		}
		if("3".equals(pvMap.get("euStatus").toString())){
			throw new BusException("本次就诊已交费，不需要重复缴费！");
		}
		
		//自助机医保个帐参数
		BigDecimal ybgzAmt = BigDecimal.ZERO;
		if(opCgTransforVo.getXjzf()!=null && opCgTransforVo.getXjzf().compareTo(BigDecimal.ZERO)>0 && 
				StringUtils.isNotEmpty(opCgTransforVo.getMachineName()) && opCgTransforVo.getMachineName().startsWith("999")){
			ybgzAmt = opCgTransforVo.getXjzf();
			opCgTransforVo.setXjzf(BigDecimal.ZERO);
		}
				
		// 兼容自助机、公众号不登录调用接口服务时，取不到登录人信息的问题
		if(StringUtils.isNotEmpty(opCgTransforVo.getMachineName()) && opCgTransforVo.getMachineName().startsWith("999")){
			
			String sql = "select emp.PK_ORG, job.PK_DEPT, emp.PK_EMP, emp.NAME_EMP from BD_OU_EMPLOYEE emp INNER JOIN BD_OU_EMPJOB job on job.PK_EMP=emp.PK_EMP where emp.CODE_EMP=?";
			List<Map<String,Object>> data = DataBaseHelper.queryForList(sql, opCgTransforVo.getMachineName());
			if(data.isEmpty()){
				throw new BusException("未找到收费员信息，不能进行结算");
			}
			User emp = new User();
			emp.setPkOrg(data.get(0).get("pkOrg").toString());
			emp.setPkDept(data.get(0).get("pkDept").toString());
			emp.setPkEmp(data.get(0).get("pkEmp").toString());
			emp.setNameEmp(data.get(0).get("nameEmp").toString());
			UserContext.setUser(emp);
			
			log.error("[公众号/自助机]进入自费正式结算: {}-{}-{}", opCgTransforVo.getMachineName(), opCgTransforVo.getPkPv(), emp.getNameEmp());
		}
		/*
		 *  在公众号进入结算时，[医保身份]需修改为[全自费]，并对应删除医保就诊登记数据
		 *  2021-05-01 lipz
		 */
		if("99998".equals(opCgTransforVo.getMachineName()) || "99999".equals(opCgTransforVo.getMachineName())){
			String pvHpSql = "select hp.PK_HP, hp.DT_EXTHP from PV_ENCOUNTER pv INNER JOIN BD_HP hp on hp.PK_HP = pv.PK_INSU where pv.PK_PV=?";
			Map<String, Object> pvHpMap = DataBaseHelper.queryForMap(pvHpSql, opCgTransforVo.getPkPv());
			if(!pvHpMap.isEmpty() && pvHpMap.containsKey("dtExthp") 
					&& ("08".equals(pvHpMap.get("dtExthp").toString()) || "05".equals(pvHpMap.get("dtExthp").toString()))
					){
				//修改院内就诊身份
				String updateHpSql = "update PV_ENCOUNTER set PK_INSU=(select PK_HP from BD_HP where CODE='0001') where PK_PV=?";
				int updateHpNum = DataBaseHelper.execute(updateHpSql, opCgTransforVo.getPkPv());
				log.error("公众号自费正式结算，修改就诊身份为全自费，{}-{}-{}-{}", "PV_ENCOUNTER", opCgTransforVo.getPkPv(), pvHpMap.get("dtExthp").toString(), updateHpNum);
				//更新主医保
				int updatePvInsNum = DataBaseHelper.execute("update pv_insurance set pk_hp=(select PK_HP from BD_HP where CODE='0001') where pk_pv=? and del_flag='0'", opCgTransforVo.getPkPv());
				log.error("公众号自费正式结算，修改就诊身份为全自费，{}-{}-{}-{}", "PV_INSURANCE", opCgTransforVo.getPkPv(), pvHpMap.get("dtExthp").toString(), updatePvInsNum);
			}
		}
		
		// 3.外部医保支付金额
		opCgTransforVo.setAmtInsuThird(opCgTransforVo.getAmtInsuThird()==null?BigDecimal.valueOf((int)0):opCgTransforVo.getAmtInsuThird());
		opCgTransforVo.setCurDate(new Date());
		
		/*
		 * 4.计费明细
		 * TODO: 2021-03-09 lipz 兼容自助机、公众号结算时，不传入明细<默认结算全部未结算的明细>和付款流水记录<在支付完成时已同时生成>
		 */
		List<BlOpDt> blOpDts = opCgTransforVo.getBlOpDts();
		Set<String> pkBlOpDt = new HashSet<String>();
		if(blOpDts!=null && blOpDts.size()>0){
			for (BlOpDt blOpDt : blOpDts) {
				pkBlOpDt.add(blOpDt.getPkCgop());
			}
			// 4.1加工前台传过来的数据
			String pkBlOpDtInSql = CommonUtils.convertSetToSqlInPart(pkBlOpDt, "pk_cgop");
			String sql = "select * from bl_op_dt where pk_cgop in (" + pkBlOpDtInSql + ")";
			blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] {});
		}else{
			// 4.2公众号、自助机结算时未传入明细，则根据就诊主键获取未结算的计费明细
			StringBuffer sql = new StringBuffer(" SELECT dt.* FROM BL_OP_DT dt ");
			sql.append(" LEFT OUTER JOIN cn_prescription pres ON dt.pk_pres=pres.pk_pres ");
			sql.append(" LEFT JOIN CN_ORDER ord ON ord.PK_CNORD = dt.PK_CNORD ");
			sql.append(" WHERE ");
			sql.append(" dt.FLAG_SETTLE='0' AND dt.DEL_FLAG='0' AND dt.FLAG_ACC='0' AND dt.PK_CGOP_BACK is null ");
//			sql.append(" AND (dt.PK_CNORD is null or ord.DATE_EFFE>=GETDATE()) ");//医嘱有效时间大于当前时间
			sql.append(" and (pres.DT_PRESTYPE!='07' or pres.DT_PRESTYPE is NULL) ");//过滤外流处方
			sql.append(" AND dt.pk_pv=? ");
			blOpDts = DataBaseHelper.queryForList(sql.toString(), BlOpDt.class, new Object[] {opCgTransforVo.getPkPv()});
			for (BlOpDt blOpDt : blOpDts) {
				pkBlOpDt.add(blOpDt.getPkCgop());
			}
		}
		
		// 5.分组已结算挂号费
		List<BlOpDt> pvSettleBlOpDts = new ArrayList<BlOpDt>();//已结算挂号费集合
		List<BlOpDt> noSettleBlOpDts = new ArrayList<BlOpDt>();//未结算集合
		Set<String> noSettlePkBlOpDt = new HashSet<String>();
		Set<String> noSettlePkCnord = new HashSet<String>();
		for (BlOpDt blOpDt : blOpDts) {
			if("1".equals(blOpDt.getFlagSettle()) && "1".equals(blOpDt.getFlagPv())){
				pvSettleBlOpDts.add(blOpDt);
			}else{
				noSettleBlOpDts.add(blOpDt);
				noSettlePkBlOpDt.add(blOpDt.getPkCgop());
				if(blOpDt.getPkCnord()!=null) {
					noSettlePkCnord.add(blOpDt.getPkCnord());
				}
			}
		}
		// 5.1处理已结算挂号费，退费重结
		if(pvSettleBlOpDts!=null && pvSettleBlOpDts.size()>0){
			List<BlOpDt> newpvlist = this.processPvBlOpDtSettleInfo(pvSettleBlOpDts, UserContext.getUser());//重新结算新生成记费记录
			if(newpvlist!=null&&newpvlist.size()>0){
				noSettleBlOpDts.addAll(newpvlist);
				for(BlOpDt newdt : newpvlist){
					noSettlePkBlOpDt.add(newdt.getPkCgop());
					if(newdt.getPkCnord()!=null) {
						noSettlePkCnord.add(newdt.getPkCnord());
					}
				}
			}
		}
		
		/* 6.支付信息为空时新增一条现金0元的信息，门诊退费时需要用到此支付信息 */
		if(opCgTransforVo.getBlDeposits()==null || opCgTransforVo.getBlDeposits().size()<=0){
			List<BlDeposit> depoList = new ArrayList<>();
			BlDeposit depo = new BlDeposit();
			depo.setDtPaymode("1");
			depo.setAmount(BigDecimal.valueOf(0D));
			depoList.add(depo);
			opCgTransforVo.setBlDeposits(depoList);
		}else
		if(opCgTransforVo.getBlDeposits()!=null && opCgTransforVo.getBlDeposits().size()==1){
			if("4".equals(opCgTransforVo.getBlDeposits().get(0).getDtPaymode()) && 
					opCgTransforVo.getBlDeposits().get(0).getAmount().compareTo(BigDecimal.valueOf(0D))==0){
				opCgTransforVo.getBlDeposits().get(0).setDtPaymode("1");
			}
		}
		
		// 7.组装结算数据
		BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
		blPubSettleVo.setPkPi(opCgTransforVo.getPkPi());
		blPubSettleVo.setPkPv(opCgTransforVo.getPkPv());
		blPubSettleVo.setBlOpDts(noSettleBlOpDts);
		blPubSettleVo.setInvoiceInfo(opCgTransforVo.getInvoiceInfo()); // 获取发票列表
		blPubSettleVo.setDepositList(opCgTransforVo.getBlDeposits());
		blPubSettleVo.setInvStatus(opCgTransforVo.getInvStatus());//判断是否对发票信息校验LB自助机使用, -2：不处理
		blPubSettleVo.setPkBlOpDtInSql(CommonUtils.convertSetToSqlInPart(noSettlePkBlOpDt, "pk_cgop"));
		blPubSettleVo.setMachineName(opCgTransforVo.getMachineName());
		blPubSettleVo.setAmtRound(opCgTransforVo.getAmtRound());
		blPubSettleVo.setFlagPrint(opCgTransforVo.getFlagPrint());
		
		// 8.结算服务
		Map<String,Object> result = opcgPubService.accountedSettlement(blPubSettleVo, true, opCgTransforVo.getAmtInsuThird(),
				opCgTransforVo.getSetlId(), ybgzAmt, zsbaOpCgTransforVo.getPkExtPay(), opCgTransforVo.getAggregateAmount());
		// 8.1设置关联结算主键
		for (BlOpDt blOpDt : noSettleBlOpDts) {
			blOpDt.setPkSettle(CommonUtils.getString(result.get("pkSettle")));
		}
		opCgTransforVo.setPkSettle(CommonUtils.getString(result.get("pkSettle")));
		// 8.2更新就诊记录就诊状态
		String pvStatusSql = "update PV_ENCOUNTER set EU_STATUS='3', EU_LOCKED='0' where PK_PV=?";
		DataBaseHelper.execute(pvStatusSql, opCgTransforVo.getPkPv());
		
		// 9.更新发票领用表
		List<BlInvoice> blInvoice = null;
		List<BlInvoiceDt> blInvoiceDt = null;
		if(result!=null){
			blInvoice = (List<BlInvoice>)result.get("inv");
			blInvoiceDt = (List<BlInvoiceDt>)result.get("invDt");
		}
		if (blInvoice != null && blInvoice.size() > 0)//打印纸质发票时才更新
		{
			for (BlInvoice invoInfo : blInvoice) {
				if(invoInfo!=null && !CommonUtils.isEmptyString(invoInfo.getCodeInv())){
					commonService.confirmUseEmpInv(invoInfo.getPkEmpinvoice(), 1L);// 单张更新
				}
			}
			// 9.1返回发票明细
			opCgTransforVo.setBlInvoiceDts(blInvoiceDt);
			opCgTransforVo.setBlInvoices(blInvoice);
		}
		
		// 10.退费重结的项目不生成处方、医技执行
		List<BlOpDt> exDtList = new ArrayList<>();
		for(BlOpDt dt : noSettleBlOpDts){
			if(CommonUtils.isEmptyString(dt.getFlagRecharge()) || !"1".equals(dt.getFlagRecharge())){
				exDtList.add(dt);
			}
		}
		if(exDtList!=null && exDtList.size()>0){
			settlePdOutService.makeSdPresAssitRecords(exDtList); 
		}
		
		// 11.发送门诊收费信息至平台
		Map<String,Object> paramVo=JsonUtil.readValue(param, Map.class);
		paramVo.put("Control", "OK");
		paramVo.put("Source", opCgTransforVo.getSource());
		paramVo.put("pkSettle", opCgTransforVo.getPkSettle());
		PlatFormSendUtils.sendBlOpSettleMsg(paramVo);
		
		// 12.收费项目状态发送至急诊系统
		Map<String,Object> paramEmrVo= new HashMap<>(16);
		paramEmrVo.put("pkList", pkBlOpDt);
		paramEmrVo.put("euSettle", "1");//0：删除，1：已收费，2退费
		ExtSystemProcessUtils.processExtMethod("EmrStSp", "saveOrUpdateCharges", new Object[]{paramEmrVo});
		
		/**
		 * 13.院内结算完成判断是否为复制处方改身份重结，结算明细重结标志为1且原结算主键不为空(正常只有收费窗口做退费重结操作才会有这种情况)
		 * 改身份重结只能复制单次结算处方明细(单次结算必须全部明细复制)且只能进行单次结算复制明细进行重结，前端需要控制重新结算勾选的明细必须为相同结算下复制的全部处方明细才行
		 * 正常门诊收费模块不能对复制处方进行结算，而改身份重结模块也不能对非复制处方进行结算，前端需要控制
		 */
		String pkBlOpDtInSql = CommonUtils.convertSetToSqlInPart(pkBlOpDt, "pk_cgop");
		String sql = "select * from bl_op_dt where pk_cgop in (" + pkBlOpDtInSql + ")";
		List<ZsbaBlOpDt> zsbaBlOpDts = DataBaseHelper.queryForList(sql, ZsbaBlOpDt.class, new Object[]{});
		boolean isRecharge = false;
		String pkSettleBefore = "";
		for(ZsbaBlOpDt dt : zsbaBlOpDts){
			if(StringUtils.isNotEmpty(dt.getFlagRecharge()) && dt.getFlagRecharge().equals("1") && StringUtils.isNotEmpty(dt.getPkSettleBefore())){
				isRecharge = true;
				pkSettleBefore = dt.getPkSettleBefore();
				break;
			}
		}
		//有重结标志为且原结算主键不为空则表示本次为改身份重结
		if(isRecharge && StringUtils.isNotEmpty(pkSettleBefore)){
			//改身份后重新结算的结算主键
			String pkSettleRecharge = CommonUtils.getString(result.get("pkSettle"));
			//原结算做全退处理(重新结算已重新收费)
			Map<String, Object> reParam = new HashMap<String, Object>();
			reParam.put("pkSettle", pkSettleBefore);
			String pkSettleCanc = zsbaSelfPayTrInsuService.zsbaOpStReBack(JSONUtils.toJSONString(reParam), user);
			if(StringUtils.isNotEmpty(pkSettleCanc)){
				log.debug("【修改身份退费重结】原结算主键："+pkSettleBefore+"==>>退费结算主键："+pkSettleCanc+"==>>重新结算主键："+pkSettleRecharge);
				//将重结主键关联到退费结算记录
				DataBaseHelper.execute("update bl_settle set pk_settle_recharge=? where pk_settle=?", pkSettleRecharge, pkSettleCanc);
				//通过结算主键查询计费明细关联执行记录表根据pk_cnord修改执行记录关联结算主键
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("pkSettle", pkSettleRecharge);
				zsbaSelfPayTrInsuService.upExOccStInfo(JSONUtils.toJSONString(paramMap), user);
				//将原结算支付方式列表(全退)+退费结算主键+重新结算主键返回
				List<BlDeposit> refundBlDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(reParam);
				for(BlDeposit blDeposit : refundBlDeposits){
					blDeposit.setAmount(new BigDecimal(-1).multiply(blDeposit.getAmount()));
				}
				opCgTransforVo.setRefundBlDeposits(refundBlDeposits);
				opCgTransforVo.setPkSettleCanc(pkSettleCanc);
				opCgTransforVo.setPkSettleRecharge(pkSettleRecharge);
			}else{
				throw new BusException("原结算HIS业务退费信息获取失败！");
			}
		}
		
		/**  以下以为不应该影响结算流程 **/
		
		// 异步：如果存在外流处方，则进行已收费通知
		cfwlPush(opCgTransforVo.getPkPv(), opCgTransforVo.getPatientsPay()==null?0D:opCgTransforVo.getPatientsPay().doubleValue());
		
		// 异步：推送缴费指引通知
		chargeGuidePush(opCgTransforVo.getPkPv(), opCgTransforVo.getPkSettle());
		
		// 异步：处理生殖中心开单缴费转诊B超分诊
		toBcTriage(opCgTransforVo.getPkPv(), opCgTransforVo.getPkSettle());
		
		// 异步：通知体检系统 已收费
		if("4".equals(pvMap.get("euPvtype"))) {
			if(noSettlePkCnord.size()>0) {
				pushTj(opCgTransforVo.getPkPv(), 
						CommonUtils.getString(result.get("codeSt")),
						CommonUtils.getString(result.get("pkEmpSt")), 
						CommonUtils.getString(result.get("amtPi")), 
						noSettlePkCnord);
			}
		}
		
		// 用于客户端判断 提示 重点对象 发票签名
		opCgTransforVo.setSource(CommonUtils.getString(result.get("zdbjTipMsg")));
		return opCgTransforVo;
	}
	
	/**
	 * 查询发票所需要的信息
	 * @param pkInvoice
	 * @return
	 */
	private Map<String, Object> queryBlInvoiceInfo(String pkInvoice) {

		if (StringUtils.isEmpty(pkInvoice))
			throw new BusException("请传入需要查询票据的主键");
		Map<String, Object> rst = new HashMap<String, Object>();
		BlInvoice blInvoice = DataBaseHelper.queryForBean("select * from bl_invoice  where pk_invoice=?", BlInvoice.class, pkInvoice);
		BdOuOrg orgInfo = DataBaseHelper.queryForBean("select * from bd_ou_org where pk_org=?", BdOuOrg.class, blInvoice.getPkOrg());
		rst.put("NameOrg", orgInfo.getNameOrg());
		rst.put("CodeInv", blInvoice.getCodeInv());
		rst.put("DateInv", blInvoice.getDateInv());
		rst.put("AmountInv", blInvoice.getAmountInv());
		rst.put("AmountInvDX", blInvoice.getAmountInv());
		rst.put("AmountPi", blInvoice.getAmountPi());
		rst.put("NameEmpInv", blInvoice.getNameEmpInv());
		rst.put("PrintTimes", blInvoice.getPrintTimes());
		StringBuilder sql = new StringBuilder();
		sql.append(" select pv.name_pi,pv.eu_pvtype,pv.date_begin,pv.date_end,pv.dt_sex,dept.name_dept,pv.code_pv CodeNo ");
		sql.append(" from pv_encounter pv inner join bl_settle settle on pv.pk_pv=settle.pk_pv");
		sql.append(" inner join bd_ou_dept dept on dept.pk_dept=pv.pk_dept");
		sql.append(" inner join bl_st_inv bsi on bsi.pk_settle=settle.pk_settle and bsi.pk_invoice=?");
		Map<String, Object> mapPatiInfo = DataBaseHelper.queryForMap(sql.toString(), pkInvoice);
		rst.putAll(mapPatiInfo);
		sql.setLength(0);
		// 查询结算明细
		sql.append("select detail.* from bl_settle_detail detail inner join bl_settle settle on detail.pk_settle=settle.pk_settle");
		sql.append(" inner join bl_st_inv bsi on bsi.pk_settle=settle.pk_settle and bsi.pk_invoice=?");
		return rst;
	}
	
	
	public OpCgTransforVo accountPayment(OpCgTransforVo opCgTransforVo,OpCgTransforVo rgv){
		PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(opCgTransforVo.getPkPi());// 患者账户信息
		if (piAcc != null) {
			Map<String, BigDecimal> accountMap =  pareAccoutService.getPatiAccountAvailableBalance(opCgTransforVo.getPkPi());
			//结算时的账户余额，不追加信用额度
			BigDecimal accountAvailableBalance = accountMap.get("acc");
			//查询患者账户是否有押金，如有责账户余额-押金
			Double amtAcc = DataBaseHelper.queryForScalar(
					"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
					Double.class, new Object[]{opCgTransforVo.getPkPi()});
			if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
				accountAvailableBalance = accountAvailableBalance.subtract(BigDecimal.valueOf(amtAcc));

				//计算出的可用余额小于0时赋值为0
				if(accountAvailableBalance.compareTo(BigDecimal.valueOf(0D))<0){
					accountAvailableBalance = BigDecimal.valueOf(0D);
				}
			}
			// 扫码枪支付，直接返回金额
			if(opCgTransforVo.getIsSaoMaPay()){
				rgv.setAccountPay(BigDecimal.ZERO);
				rgv.setAccountReceivable(rgv.getPatientsPay());
			}else{
				// 有父账户，取父账户的可用额度；没有父账户，取患者用户的可用额度
				if (accountAvailableBalance.compareTo(rgv.getPatientsPay())>=0) {
					rgv.setAccountPay(rgv.getPatientsPay());
					rgv.setAccountReceivable(BigDecimal.ZERO);
				} else {
					rgv.setAccountPay(accountAvailableBalance);
					rgv.setAccountReceivable(rgv.getPatientsPay().subtract(accountAvailableBalance));
				}
				rgv.setAccountBalance(accountAvailableBalance);
			}
		} else {
			rgv.setAccountBalance(BigDecimal.ZERO);
			rgv.setAccountPay(BigDecimal.ZERO);
			rgv.setAccountReceivable(rgv.getPatientsPay());
		}
		return rgv;
		
	}
	public  Map<String, BigDecimal> accountBalance(List<Map<String, Object>> mapListParam,String pkPi){
		/**
		 * 此处逻辑为：先看患者有无账户；再看患者有无父账户，如果有父账户，完全不考虑患者账户。如果没有父账户，再考虑患者账户。
		 */
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		BigDecimal accountBalance = BigDecimal.ZERO;// 账户余额
		BigDecimal accLimit = BigDecimal.ZERO;// 账户可用余额（考虑信用额度）
		BigDecimal creditAcc = BigDecimal.ZERO;// 信用额度
		// 查询患者自己账户信息			
		PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(pkPi);// 患者账户信息
		// 存在患者账户
		if(piAcc != null){
			// 存在父账户的话，查询父账户余额和可用额度；不存在父账户，查询患者余额和可用额度
			Map<String,BigDecimal> accountAvailableBalance = pareAccoutService.getPatiAccountAvailableBalance(pkPi);
			// 账户余额和账户可用额度
			accountBalance = accountAvailableBalance.get("acc");
			accLimit = accountAvailableBalance.get("accLimit");
			creditAcc = accountAvailableBalance.get("creditAcc");//处理信用卡额度被取消的情况
			// 处理信用额度为0 但是账户为负的情况
			if(creditAcc.compareTo(BigDecimal.ZERO)==0 && accountBalance.compareTo(BigDecimal.ZERO) < 0){
				accountBalance = BigDecimal.ZERO;
			}
		}
		map.put("accountBalance", accountBalance);
		map.put("creditAcc", creditAcc);
		map.put("accLimit", accLimit);
		return map;
	}
	
	/**
	 * 查询收治医生姓名
	 * 007002003034->022003027056
	 * @param param
	 * @param user
	 * @return
	 */
	public String getDrawDocName(String param,IUser user){
		PvEncounter pvEncounter2 = JsonUtil.readValue(param, PvEncounter.class);
		String pkPv = pvEncounter2.getPkPv();
		PvEncounter pvEncounter = opCgQueryMapper.getDrawDocName(pkPv);
		String name = pvEncounter.getPkEmpPhy();
		return name;
		
	}
	
	/**
	 * 根据机构和医嘱项目字典主键查询对应的机构和科室关系
	 * 007002003035->022003027057
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrdDept> getBdOrdDeptExec(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, HashMap.class);
		List<BdOrdDept> bdOrdDeptList = opCgQueryMapper.getBdOrdDepts(params);
		return bdOrdDeptList;
		
	}
	
	
	/**
	 * 查询当前物品库存量
	 * 007002003036->022003027058
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PdStockVo> queryStockList(String param,IUser user){
		Map<String, Object> params = JsonUtil.readValue(param, HashMap.class);
		List<PdStockVo> pdStocks = opCgQueryMapper.getStocks(params);
		return pdStocks;		
	}
	

	/**
	 * 判断是否门诊停用
	 * 007002003037->022003027059
	 * @param param
	 * @param user
	 * @return
	 */
	public int getIsStopOp(String param,IUser user){
		IsStopOpParamVo isStopOpParamVo = JsonUtil.readValue(param, IsStopOpParamVo.class);
		Map<String, String> params = new HashMap<String, String>();
		int count = 0;
		params.put("code", isStopOpParamVo.getCode());
		params.put("pkOrg", isStopOpParamVo.getPkOrg());
		params.put("pkDept", isStopOpParamVo.getPkDept());
		count = opCgQueryMapper.getIsStopOp(params);
		return count;
	}
	
	/**
	 * 处理已结算挂号费，重新结算
	 * @param pvSettleBlOpDts
	 * @return
	 */
	private List<BlOpDt> processPvBlOpDtSettleInfo(List<BlOpDt> pvSettleBlOpDts,User u){
		if(pvSettleBlOpDts==null||pvSettleBlOpDts.size()<=0)
			return null;	
		Map<String,Object> mapParam = new HashMap<String,Object>();
		String pkSettle = pvSettleBlOpDts.get(0).getPkSettle();
		List<BlOpDt> insertDtlist = new ArrayList<BlOpDt>();
		//TODO 未重新计算优惠策略
		for(BlOpDt dt:pvSettleBlOpDts){
			if(!pkSettle.equals(dt.getPkSettle()))
				throw new BusException("本次重新结算挂号费，非同一次结算数据，无法重结！");
			BlOpDt newdt = new BlOpDt();
			ApplicationUtils.copyProperties(newdt, dt);
			newdt.setFlagSettle("0");
			newdt.setPkSettle("");
			newdt.setFlagAcc("0");
			newdt.setPkAcc("");
			newdt.setDateCg(new Date());
			newdt.setNameEmpCg(u.getNameEmp());
			newdt.setPkEmpCg(u.getPkEmp());
			newdt.setPkDeptCg(u.getPkDept());
			ApplicationUtils.setDefaultValue(newdt, true);
			insertDtlist.add(newdt);
		}
		String pkPi = pvSettleBlOpDts.get(0).getPkPi();
		String pkPv = pvSettleBlOpDts.get(0).getPkPv();
		mapParam.put("pkPi", pkPi);
		// 根据结算主键查询结算信息
		mapParam.put("pkSettle", pkSettle);
		BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
		// 生成退费结算信息
		String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle);
		// 生成退费明细
		opcgPubHelperService.generateRefoundRecord(pvSettleBlOpDts, blSettle.getPkSettle());// 传入新的结算主键
		// 根据结算主键查询结算明细
		List<BlSettleDetail> blSettleDetail = cgQryMaintainService.qryBlSettleDetailInfoByBlSettle(mapParam);
		// 生成退费结算明细
		opcgPubHelperService.generateRefoundSettleDetail(blSettleDetail,pkSettleCanc);

		// 根据结算主键查询交款记录信息
		List<BlDeposit> blDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(mapParam);
		
		// 生成退费的交款记录信息
		List<BlDeposit> negaBlDeposits = opcgPubHelperService.generateRefoundBlDeposits(blDeposits,pkSettleCanc);
		
		//加了非空判断
		if(negaBlDeposits != null){
			for (BlDeposit negaBlDeposit : negaBlDeposits) {
				if (negaBlDeposit.getDtPaymode().equals(IDictCodeConst.PATIACCOUNT)) {
					// 更新患者账户，调用患者账户消费服务
					BlDepositPi blDepositPi = new BlDepositPi();
					ApplicationUtils.setDefaultValue(blDepositPi, true);
					blDepositPi.setEuDirect(EnumerateParameter.ONE);
					blDepositPi.setPkPi(pkPi);
					blDepositPi.setAmount(negaBlDeposit.getAmount().abs());
					blDepositPi.setDtPaymode(EnumerateParameter.FOUR);
					blDepositPi.setPkEmpPay(u.getPkEmp());
					blDepositPi.setNameEmpPay(u.getNameEmp());
					PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(pkPi);// 患者账户信息
					ApplicationUtils.setDefaultValue(piAcc, false);
					piAcc.setAmtAcc((piAcc.getAmtAcc() == null ? BigDecimal.ZERO : piAcc.getAmtAcc()).add(blDepositPi.getAmount()));
					balAccoutService.piAccDetailVal(piAcc, blDepositPi, pkPv, null);
				}
			}
		}

		String BL0002_code = ApplicationUtils.getSysparam("BL0002", false);
		// 如果挂号时打印的发票，要作废发票
		if (EnumerateParameter.ONE.equals(BL0002_code)) {
			// 根据结算主键查询作废结算时对应的发票
			List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
			if(blInvoices!=null&&blInvoices.size()>0){
				for(BlInvoice inv:blInvoices){
					// 更新作废发票信息
					opcgPubHelperService.updateRefoundBlInvoice(inv);
				}
			}
		}
		//插入新的收费明细
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), insertDtlist);
		//返回新的收费明细
		return insertDtlist;
	}
	
	/**
	 * 门诊结算时上传检验检查申请单
	 * 007002003038->022003027060
	 * @param param
	 * @param user
	 */
	public void saveOpLisPacs(String param,IUser user){
		
		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);

		List<BlOpDt> blOpDts = opCgTransforVo.getBlOpDts();
		List<String> pkCnords = new ArrayList<>();
		for (BlOpDt blOpDt : blOpDts) {
			if(StringUtils.isNotBlank(blOpDt.getPkCnord())){
				pkCnords.add(blOpDt.getPkCnord());				
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("pkCnords", pkCnords);
		
		//检验
		List<String> lisPkCnords = new ArrayList<>();
		//检查
		List<String> pacsPkCnords = new ArrayList<>();
		
		//查询所有医嘱
		List<CnOrder> list = opCgQueryMapper.qryCnOrderList(map);
		for (CnOrder cnOrder : list) {
			//02-检查，03-检验
			if("02".equals(cnOrder.getCodeOrdtype())){
				pacsPkCnords.add(cnOrder.getPkCnord());
			}else if("03".equals(cnOrder.getCodeOrdtype())){
				lisPkCnords.add(cnOrder.getPkCnord());
			}
		}
		
		//上传检验项目
		if(lisPkCnords.size() > 0){
			ExtSystemProcessUtils.processExtMethod("LIS", "saveLisApply",lisPkCnords,true);
		}
		//上传检查项目
		if(pacsPkCnords.size() > 0){
			ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx",null,pacsPkCnords,true);
		}
	}
	
	/**
	 * 门诊退费时上传检验检查申请单
	 * 007002003039->022003027061
	 * @param param
	 * @param user
	 */
	public void delOpLisPacs(String param,IUser user){
		
		List<BlOpDtRefundVo> refundlist = JsonUtil.readValue(param,new TypeReference<List<BlOpDtRefundVo>>(){});
		
		List<String> pkCnords = new ArrayList<>();
		for (BlOpDtRefundVo vo : refundlist) {
			if(StringUtils.isNotBlank(vo.getPkCnord())){
				pkCnords.add(vo.getPkCnord());				
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("pkCnords", pkCnords);
		
		//检验
		List<String> lisPkCnords = new ArrayList<>();
		
		//查询所有医嘱
		List<CnOrder> list = opCgQueryMapper.qryCnOrderList(map);
		for (CnOrder cnOrder : list) {
			//03-检验
			if("03".equals(cnOrder.getCodeOrdtype())){
				lisPkCnords.add(cnOrder.getPkCnord());
			}
		}
		
		//上传检验项目
		if(lisPkCnords.size() > 0){
			ExtSystemProcessUtils.processExtMethod("LIS", "delLisApply",lisPkCnords,true);
		}
	}
	
	/*
	 * 外流处方已收费通知
	 * @param codePv
	 * @param amount
	 */
	private void cfwlPush(String pkPv, Double amount){
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					String wlSql = "SELECT PK_PRES FROM CN_PRESCRIPTION WHERE DT_PRESTYPE='07' AND PK_PV=?";
					Map<String, Object> wlMap = DataBaseHelper.queryForMap(wlSql, pkPv);
					if(wlMap!=null && wlMap.containsKey("pkPres")){//当次就诊存在 外流处方 的
						String sql = "select CODE_PV from PV_ENCOUNTER where PK_PV=?";
						Map<String, Object> map = DataBaseHelper.queryForMap(sql, pkPv);
						if(map!=null && map.containsKey("codePv")){
							ExtSystemProcessUtils.processExtMethod("preOutflow", "updatePayInfo", new Object[]{map.get("codePv").toString(), amount});
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	/*
	 * 缴费指引通知
	 * @param pkPv
	 * @param amount
	 */
	private void chargeGuidePush(String pkPv, String pkSettle){
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					// 查询就诊人关联的微信openId
					StringBuffer sql = new StringBuffer();
					sql.append(" SELECT pi.CODE_OP, pi.NAME_PI, wm.WX_OPEN_ID ");
					sql.append(" FROM PV_ENCOUNTER pv ");
					sql.append(" INNER JOIN PI_MASTER pi ON pi.PK_PI= pv.PK_PI ");
					sql.append(" INNER JOIN t_patient_wx_mapper wm on wm.PATIENT_ID=pi.CODE_OP ");
					sql.append(" WHERE pv.PK_PV=? ");
					sql.append(" GROUP BY pi.CODE_OP,pi.NAME_PI,wm.WX_OPEN_ID ");
					List<Map<String, Object>> openIds = DataBaseHelper.queryForList(sql.toString(), pkPv);
					if(openIds!=null && openIds.size()>0) {
						// 查询系统前缀
						String itemSql = "select TOP 1 SHORTNAME, NAME from BD_DEFDOC where CODE_DEFDOCLIST=? and CODE=?";
						Map<String, Object> dictMap = DataBaseHelper.queryForMap(itemSql, "BAXTJKQZ", "NHIS_API_URI");
						if(dictMap!=null && dictMap.containsKey("name")) {
							// 遍历调用接口推送消息
							String url = dictMap.get("name").toString() + "/nhis_api/wechat/push/openPush";
							for (Map<String, Object> map : openIds) {
								Map<String, Object> param = new HashMap<String, Object>();
								param.put("type", "10");
								param.put("codeOp", pkPv+"-"+pkSettle+ "-"+ map.get("codeOp").toString());
								param.put("first", "您好，"+map.get("namePi").toString()+"有就诊指引，请注意 按时处理，谢谢。");
								param.put("keyword", "就诊指引单@-@"+DateUtils.getDateTime());
								param.put("remark", "点击【详情】查看就诊条码和指引单信息到对应窗口扫码执行。");
								param.put("openId", map.get("wxOpenId").toString());
								
								HttpClientUtils.getMap(url, param);
							}
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/*
	 * 如果包含【202030501	生殖分院】开立的项目【阴道B超_y1 | 腹部B超(子宫、附件)_y2】
	 * 需要调用生殖科内转诊系统自动挂 B超号
	 * @param pkPv
	 * @param pkSettle
	 */
	public void toBcTriage(String pkPv, String pkSettle){
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					String deptSql = "select top 1 PK_DEPT,CODE_DEPT,NAME_DEPT from BD_OU_DEPT where CODE_DEPT=?";
					Map<String, Object> deptMap = DataBaseHelper.queryForMap(deptSql, "202030501");
					if(deptMap==null){
						return;
					}
					String pkDept = deptMap.get("pkDept").toString();
					
					String itemSql = "select CODE, NAME from BD_DEFDOC where CODE_DEFDOCLIST=?";
					List<Map<String, Object>> itemList = DataBaseHelper.queryForList(itemSql, "BASZ001");
					if(itemList==null){
						return;
					}
					
					StringJoiner codes = new StringJoiner("','", "'", "'");
					for (Map<String, Object> itemMap : itemList) {
						codes.add(itemMap.get("code").toString());
					}
					if(codes.length()>2){
						StringBuffer ordSql = new StringBuffer();
						ordSql.append(" SELECT ord.CODE_ORD, ord.NAME_ORD ");
						ordSql.append(" FROM bl_op_dt dt INNER JOIN CN_ORDER ord on ord.PK_CNORD = dt.PK_CNORD ");
						ordSql.append(" WHERE dt.PK_SETTLE=? and dt.PK_DEPT_APP=? and ord.CODE_ORD in ("+ codes.toString() +") ");
						List<Map<String, Object>> ordList = DataBaseHelper.queryForList(ordSql.toString(), pkSettle, pkDept);
						if(ordList!=null && ordList.size()>0){
							toPush(pkPv);
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	private void toPush(String pkPv){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT TOP 1 ");
			sql.append(" pi.CODE_OP,vo.OP_TIMES, pi.NAME_PI, pi.BIRTH_DATE, pi.DT_SEX, pi.MOBILE,emp.CODE_EMP ");
		sql.append(" FROM ");
			sql.append(" PI_MASTER pi ");
			sql.append(" INNER JOIN PV_ENCOUNTER pv ON pv.PK_PI= pi.PK_PI ");
			sql.append(" INNER JOIN PV_OP vo on vo.PK_PV=pv.PK_PV ");
			sql.append(" INNER JOIN BD_OU_EMPLOYEE emp on emp.PK_EMP=vo.PK_EMP_PV ");
		sql.append(" WHERE ");
			sql.append(" vo.PK_PV=? ");
		
		Map<String, Object> deptMap = DataBaseHelper.queryForMap(sql.toString(), pkPv);
		if(deptMap!=null){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("visitType", "1");//分诊类型(1B超，2手术)
			param.put("patientId", deptMap.get("codeOp"));//患者ID
			param.put("doctorCode", deptMap.get("codeEmp"));//医生工号
			
			// 查询系统前缀
			String itemSql = "select TOP 1 SHORTNAME, NAME from BD_DEFDOC where CODE_DEFDOCLIST=? and CODE=?";
			Map<String, Object> dictMap = DataBaseHelper.queryForMap(itemSql, "BAXTJKQZ", "NHIS_API_URI");
			if(dictMap!=null && dictMap.containsKey("name")) {
				String url = dictMap.get("name").toString() + "/nhis_api/sz/bc/toTriage";
				HttpClientUtils.getMap(url, param);
			}
		}
	}
		
	/*
	 * 个人体检-收费回写
	 */
	private void pushTj(String pkPv, String codeSt, String pkEmpSt, String amtPi, Set<String> ordsnSet) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					// 查询系统前缀
					String itemSql = "select TOP 1 SHORTNAME, NAME from BD_DEFDOC where CODE_DEFDOCLIST=? and CODE=?";
					Map<String, Object> dictMap = DataBaseHelper.queryForMap(itemSql, "BAXTJKQZ", "TJXT_URI");
					if(dictMap!=null && dictMap.containsKey("name")) {
						String url = dictMap.get("name").toString() + "/interface.asmx/AcceptMessage";
						String InterfaceCode = "PayByHIS";
						String CompanyCode = "peis";
						String MessageID = DateUtils.getDate("yyyyMMddHHmmss") + new Random().nextInt(99999);
						
						String headStr = "<head><InterfaceCode>IFC</InterfaceCode><CompanyCode>CC</CompanyCode><MessageID>MID</MessageID></head>";
						String reqStr = "<hisregisterid>PKPV</hisregisterid><paycost>AMTPI</paycost><invoiceno>INVCODE</invoiceno><payoperid>PKEMPST</payoperid>";
						String orderList = "<orderinfo><orderid>CNORDSN</orderid></orderinfo>";
						
						StringBuffer params = new StringBuffer("<message>");
						params.append(headStr.replace("IFC", InterfaceCode).replace("CC", CompanyCode).replace("MID", MessageID));
						params.append("<req>");
						params.append(reqStr.replace("PKPV", pkPv).replace("AMTPI", amtPi).replace("INVCODE", codeSt).replace("PKEMPST", pkEmpSt));
						params.append("<orderlist>");
						for(String ordsn : ordsnSet) {
							params.append(orderList.replace("CNORDSN", ordsn));
						}
						params.append("</orderlist>");
						params.append("</req>");
						params.append("</message>");
								
						Map<String, Object> reqMap = new HashMap<String, Object>();
						reqMap.put("requestxml", params.toString());
						
						// 不需要关注调用结果是否成功
						String result = HttpClientUtils.getMap(url, reqMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	/**
	 * 获取指引单内容数据
	 * @param param	{"pkPv":"就诊主键","pkSettle":"结算主键"}
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getVisitSettleGuideInfo(String param, IUser user){
		 Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		 if(paramMap.isEmpty()){
			 throw new BusException("必填参数【pkPv、pkSettle】不能为空。");
		 }
		 if(!paramMap.containsKey("pkPv") && paramMap.get("pkPv")!=null){
			 throw new BusException("参数【pkPv】不能为空。");
		 }
		 if(!paramMap.containsKey("pkSettle") && paramMap.get("pkSettle")!=null){
			 throw new BusException("参数【pkSettle】不能为空。");
		 }
		 
		 String settleSql = "select PK_PV,FLAG_CANC, AMOUNT_ST from BL_SETTLE where PK_SETTLE=?";
		 Map<String, Object> settleMap = DataBaseHelper.queryForMap(settleSql, paramMap.get("pkSettle").toString());
		 if(settleMap!=null){
			 if(settleMap.containsKey("flagCanc") && "1".equals(settleMap.get("flagCanc").toString())){
				 throw new BusException("本次结算已退费，不可打印指引单。");
			 }
			 if(settleMap.containsKey("amountSt")){
				 BigDecimal amtSt = new BigDecimal(settleMap.get("amountSt").toString());
				 if(amtSt.compareTo(BigDecimal.ZERO)<0){
					 throw new BusException("退费记录，不可打印指引单。");
				 }
			 }
		 }
		
		Map<String, Object> returnMap = new HashMap<>();
		
		// 1.获取本次结算收费信息
		StringBuffer stSql = new StringBuffer();
		stSql.append(" SELECT st.amount_st, st.amount_insu, ");
		stSql.append(" ISNULL(ISNULL(qg.amt_grzhzf, gs.amt_grzhzf), 0) amount_gz, ");
		stSql.append(" st.amount_pi - ISNULL(ISNULL(qg.amt_grzhzf, gs.amt_grzhzf), 0) amount_pi, ");
		stSql.append(" st.amount_disc, st.name_emp_st, ");
		stSql.append(" CONVERT(VARCHAR(16), st.date_st, 120) date_st, st.flag_canc  ");
		stSql.append(" FROM BL_SETTLE st ");
		stSql.append(" LEFT JOIN ins_sgsyb_st gs on gs.PK_SETTLE=st.PK_SETTLE ");
		stSql.append(" LEFT JOIN ins_qgyb_st qg on qg.PK_SETTLE=st.PK_SETTLE ");
		stSql.append(" WHERE st.PK_SETTLE = ? ");
		List<Map<String, Object>> stMapList = DataBaseHelper.queryForList(stSql.toString(), paramMap.get("pkSettle").toString());
		if(stMapList.isEmpty()){
			throw new BusException("未找到患者结算数据。");
		}
		Map<String, Object> stMap = stMapList.get(0);
		returnMap.put("settleData", stMap);
		
		// 2.获取患者基本信息
		StringBuffer pvSql = new StringBuffer();
		pvSql.append(" SELECT pi.CODE_OP, pv.NAME_PI, pv.AGE_PV, ");
		pvSql.append(" CASE pv.DT_SEX WHEN '03' THEN '女' WHEN '02' THEN '男' ELSE '未知' END sex, ");
		pvSql.append(" dept.NAME_DEPT, pv.name_emp_phy name_emp, ");
		pvSql.append(" CASE WHEN (hp.DT_EXTHP IS NOT NULL and hp.DT_EXTHP!='00' and hp.DT_EXTHP!='') THEN '医保' ELSE '普通' END AS name_case, ");
		pvSql.append(" hp.NAME name_hp, CONVERT(VARCHAR(16), pv.DATE_CLINIC, 120) visit_time ");
		pvSql.append(" FROM BL_SETTLE st ");
		pvSql.append(" LEFT JOIN PV_ENCOUNTER pv ON pv.PK_PV= st.PK_PV ");
		pvSql.append(" LEFT JOIN PI_MASTER pi ON pi.PK_PI= pv.PK_PI ");
		pvSql.append(" LEFT JOIN BD_OU_DEPT dept on dept.PK_DEPT=pv.PK_DEPT ");
		pvSql.append(" LEFT JOIN BD_HP hp on hp.PK_HP=st.PK_INSURANCE ");
		pvSql.append(" WHERE st.PK_SETTLE=? ");
		List<Map<String, Object>> pvData = DataBaseHelper.queryForList(pvSql.toString(), paramMap.get("pkSettle").toString());
		if(pvData.isEmpty()){
			 throw new BusException("未找到患者就诊数据。");
		}
		returnMap.put("pvData", pvData.get(0));
			
		// 3.获取本次结算发票信息
		StringBuffer invSql = new StringBuffer();
		invSql.append(" SELECT 'OP' + st.CODE_ST AS code_buz, inv.AMOUNT_INV, inv.AMOUNT_PI, CONVERT(VARCHAR(16), inv.create_time,120) create_time, ");
		invSql.append(" ISNULL(inv.CODE_INV, inv.ebillno) code_inv, ");
		invSql.append(" ISNULL(inv.code_sn, inv.ebillbatchcode) code_sn, ");
		invSql.append(" ISNULL(inv.billbatchcode, inv.checkcode) code_jym, ");
		invSql.append(" ISNULL(inv.name_emp_inv, inv.name_emp_ebill) name_emp, inv.URL_EBILL  ");
		invSql.append(" FROM BL_ST_INV sti INNER JOIN BL_INVOICE inv ON inv.PK_INVOICE= sti.PK_INVOICE  ");
		invSql.append(" INNER JOIN BL_SETTLE st on st.PK_SETTLE = sti.PK_SETTLE ");
		invSql.append(" WHERE sti.PK_SETTLE=? ");
		invSql.append(" and (inv.flag_cc_cancel='0' or inv.flag_cc_cancel is null ) ");
		invSql.append(" and (inv.flag_cancel_ebill='0' or inv.flag_cancel_ebill is null) ");
		
		List<Map<String, Object>> invData = DataBaseHelper.queryForList(invSql.toString(), paramMap.get("pkSettle").toString());
		returnMap.put("invData", invData.isEmpty()?new HashMap<String, Object>():invData.get(0));
		
		// 4.发票收费项
		StringBuffer invItemSql = new StringBuffer();
		invItemSql.append(" SELECT bii.NAME AS item_type, SUM ( dt.AMOUNT ) AS amount ");
		invItemSql.append(" FROM BL_OP_DT dt ");
		invItemSql.append(" INNER JOIN BD_INVCATE_ITEM bii ON bii.CODE = dt.CODE_BILL ");
		invItemSql.append(" INNER JOIN BD_INVCATE ic ON ic.PK_INVCATE = bii.PK_INVCATE AND ic.CODE= '01' ");
		invItemSql.append(" LEFT OUTER JOIN cn_prescription pres ON dt.pk_pres= pres.pk_pres ");
		invItemSql.append(" LEFT JOIN CN_ORDER ord ON ord.PK_CNORD = dt.PK_CNORD ");
		invItemSql.append(" WHERE dt.del_flag= '0' AND dt.pk_cgop_back IS NULL AND dt.flag_settle = '1' ");
		invItemSql.append(" AND ( pres.DT_PRESTYPE!= '07' OR pres.DT_PRESTYPE IS NULL )  ");
		invItemSql.append(" AND dt.PK_SETTLE = ? ");
		invItemSql.append(" GROUP BY bii.NAME ");
		
		List<Map<String, Object>> invItemData = DataBaseHelper.queryForList(invItemSql.toString(), paramMap.get("pkSettle").toString());
		returnMap.put("invItemDataList", invItemData);
		
		// 5.获取本次结算对应的指引信息
		List<Map<String, Object>> guideDataList = new ArrayList<Map<String, Object>>();
		StringBuffer notes = new StringBuffer("");
		
		StringBuffer guideSql = new StringBuffer();
		guideSql.append(" select * from view_zyd where pk_settle=? and pk_pv=? ");
		List<Map<String, Object>> guideData = DataBaseHelper.queryForList(guideSql.toString(), paramMap.get("pkSettle").toString(), paramMap.get("pkPv").toString());
		for (Map<String, Object> map : guideData) {
			Map<String, Object> guide = new HashMap<>();
			guide.put("itemName", map.get("item")==null?"":map.get("item").toString().trim().replaceAll("\t", "").replaceAll("\r\n", ""));
			guide.put("address", map.get("address")==null?"":map.get("address").toString().trim().replaceAll("\t", "").replaceAll("\r\n", ""));
			guide.put("reportTime", map.get("reporttime")==null?"":map.get("reporttime").toString().trim().replaceAll("\t", "").replaceAll("\r\n", ""));
			guideDataList.add(guide);
		
			if(map.get("note")!=null && StringUtils.isNotEmpty(map.get("note").toString().trim())){
				notes.append(map.get("note").toString()).append(";");
			}
		}
		returnMap.put("guideDataList", guideDataList);//项目指引
		returnMap.put("notes", notes.toString().replaceAll("\t", "").replaceAll("\r\n", ""));//备注说明
		
		// 6.处方单信息 名称、单价、数量、金额
		List<Map<String, Object>> orderDataList = new ArrayList<Map<String, Object>>();
		StringBuffer orderSql = new StringBuffer();
		orderSql.append(" select * from view_zyd_cnorder where pk_settle=? and pk_pv=? ");
		List<Map<String, Object>> orderData = DataBaseHelper.queryForList(orderSql.toString(), paramMap.get("pkSettle").toString(), paramMap.get("pkPv").toString());
		for (Map<String, Object> map : orderData) {
			Map<String, Object> order = new HashMap<>();
			order.put("orderName", map.get("nameOrd"));
			order.put("price", map.get("price"));
			order.put("num", map.get("quan"));
			order.put("total", map.get("amount"));
			orderDataList.add(order);
		}
		returnMap.put("orderDataList", orderDataList);//处方单
		return returnMap;
	}
	
	
	/**
	 * 查询结算费用明细信息
	 * 交易号：022003027114
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> querySettleCgDetail(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> detailList = opCgQueryMapper.querySettleCgDetail(paramMap);
		if(detailList.size()>0){
			paramMap.put("pkPv", detailList.get(0).get("pkPv"));
		}
		Map<String,Object> settleInfo = opCgQueryMapper.querySettleInfo(paramMap);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("settleInfo", settleInfo);
		returnMap.put("detailList", detailList);
		return returnMap;
	}
	
	/**
	 * 
	 * 查询患者结算列表
	 * 交易号：022003027130
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPatientSettleList(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = opCgQueryMapper.queryPatientSettleList(paramMap);
		return list;
	}
	
	/**
	 * 查询患者就诊信息列表
	 * 交易号：022003027143
	 */
	public List<PibaseVo> getPibaseVoList(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkOrg = UserContext.getUser().getPkOrg();
        encounter.setPkOrg(pkOrg);
        // 当就诊类型为3时，使用date_begin字段匹配参数dateClinic；其余情况下，使用date_clinic字段匹配参数dateClinic
        if ("3".equals(encounter.getEuPvtype())) {
            if (encounter.getDateClinic() != null) {
                encounter.setDateBegin(encounter.getDateClinic());
                encounter.setDateClinic(null);
            }
        } else if ("1".equals(encounter.getEuPvtype())
                || "2".equals(encounter.getEuPvtype())
                || chkIsOpOrIp(encounter.getEuPvtypes())) {
            //yangxue 注释
            //encounter.setDateEnd(new Date());
        }
        List<PibaseVo> voList = opCgQueryMapper.getPibaseVoList(encounter);
        return voList;
    }
	//检验 就诊类型中是否包含门诊或急诊
    private boolean chkIsOpOrIp(String[] pvTypes) {
        boolean isOp = false;
        if (null == pvTypes || pvTypes.length < 1)
            return isOp;
        else {
            for (String str : pvTypes) {
                if ("1".equals(str)) {
                    isOp = true;
                    break;
                } else if ("2".equals(str)) {
                    isOp = true;
                    break;
                } else
                    continue;
            }
        }
        return isOp;
    }
}
