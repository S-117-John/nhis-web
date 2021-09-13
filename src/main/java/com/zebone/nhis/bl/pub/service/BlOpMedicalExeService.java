package com.zebone.nhis.bl.pub.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zebone.nhis.bl.pub.pay.service.ClinicPayService;
import com.zebone.nhis.bl.pub.vo.BlDepositPiParam;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.ClinicPayInputVo;
import com.zebone.nhis.bl.pub.vo.MedExeChargeVo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlOpFee;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ex.pub.support.OrderFreqCalCountHandler;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 医疗执行服务（门急诊）
 */
@Service
public class BlOpMedicalExeService {
	
	@Autowired
	private OpCgPubService opCgPubService;
	@Autowired
	private BlIpPubMapper  blIpPubMapper;
	@Autowired
	private BalAccoutService balAccoutService;	
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Autowired
	private PareAccoutService pareAccoutService;
	@Autowired
	private SettlePdOutService settlePdOutService; // 处理库存、处方和医技执行单
	@Autowired
	private ClinicPayService clinicPayService;
	
	private static String NEW = "N";
	private static String UPDATE = "U";
	private static String REMOVE = "R";
	private static String LOAD = "L";
	
	public void chargeOpInBatch(String param,IUser user){
		List<MedExeChargeVo> vos = JsonUtil.readValue(param, new TypeReference<List<MedExeChargeVo>>(){});
		if(vos==null || vos.size()<0)return;
		List<BlPubParamVo> params = new ArrayList<BlPubParamVo>();
		for(MedExeChargeVo vo : vos){
			if(REMOVE.equals(vo.getRowStatus())){
				DataBaseHelper.execute(" delete from bl_op_dt where pk_cgop = ?", vo.getPkCgop());
				continue;
			}else if(LOAD.equals(vo.getRowStatus())){
				continue;
			}
			BlPubParamVo paramVo = new BlPubParamVo();
			paramVo.setQuanCg(vo.getQuan());
			paramVo.setPkItem(vo.getPkItem());
			paramVo.setPkPv(vo.getPkPv());
			paramVo.setPkPi(vo.getPkPi());
			paramVo.setPkOrg(UserContext.getUser().getPkOrg());
			paramVo.setPkOrgApp(UserContext.getUser().getPkOrg());
			paramVo.setPkOrgEx(UserContext.getUser().getPkOrg());
			paramVo.setPkCnord(vo.getPkCnord());
			paramVo.setFlagPd("0");
			paramVo.setDateHap(new Date());
			paramVo.setPkDeptApp(UserContext.getUser().getPkDept());
			paramVo.setPkDeptEx(UserContext.getUser().getPkDept());
			paramVo.setPkEmpApp(UserContext.getUser().getPkEmp());
			paramVo.setNameEmpApp(UserContext.getUser().getNameEmp());
			paramVo.setPkDeptCg(UserContext.getUser().getPkDept());
			params.add(paramVo);
		}
		opCgPubService.blOpCg(params);
	}
	@SuppressWarnings("unchecked")
	public List<BlOpFee> qryOrdFees(String param,IUser user){
		List<String> pkOrds = JsonUtil.readValue(param, List.class);
		List<BlOpFee> res = new ArrayList<BlOpFee>();
		if(pkOrds!=null && pkOrds.size()>0){
			res = blIpPubMapper.qryOrdFees(pkOrds);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public void medExeOp(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<String> PkAssOccs = (List<String>)map.get("pkAssOccs");
		String dateOcc = (String)map.get("dateOcc");
		if(PkAssOccs!=null && PkAssOccs.size()>0 && StringUtils.hasText(dateOcc)){
			map.put("dateOcc", dateTrans(dateOcc));
			map.put("pkEmp", UserContext.getUser().getPkEmp());
			map.put("nameEmp", UserContext.getUser().getNameEmp());
			//1.更新门诊医疗执行单
			blIpPubMapper.medExeOp(map);		
			
			//2.更新医嘱表			
			List<CnOrder> cnOrds = blIpPubMapper.qryOrdByPkAssOcc(PkAssOccs);
			if(cnOrds != null && cnOrds.size()>0){
				//2.1更新医嘱cn_order
				blIpPubMapper.updOrder(cnOrds);
				List<String> applyPks =  new ArrayList<String>();
				List<String>  paPks =  new ArrayList<String>();
				for(CnOrder cnord : cnOrds){
					if(cnord.getCodeOrdtype()!=null && cnord.getCodeOrdtype().equals("0204")){
						paPks.add(cnord.getPkCnord());
					}
					if(cnord.getCodeOrdtype()!=null && cnord.getCodeOrdtype().startsWith("02")){
						applyPks.add(cnord.getPkCnord());
					}
				}
				//2.2 如果检查医嘱，更新检查申请表
				if(applyPks.size()>0){
					blIpPubMapper.updOrderApply(applyPks);
				}
				//2.2  如果病理医嘱，更新病理申请表；
				if(paPks.size()>0){
					blIpPubMapper.updOrderPa(paPks);
				}
			}	
			
		}
	}
	
	/**
	 * 007004001007  医技执行 --- 账户支付
	 * @param param
	 * @param user
	 */
	public void payByAcc(String param,IUser user){
		BlDepositPiParam depoPi = JsonUtil.readValue(param, BlDepositPiParam.class);
		BlDepositPi depoForSave = JsonUtil.readValue(param, BlDepositPi.class);
		//BlDepositPi depoPi = (BlDepositPi)map.get("depo");
		String pkPv = depoPi.getPkPv();
		String euOptype =  depoPi.getEuOptype();
		
		// 1、 生成结算表、结算明细表、付款记录表
		List<String> list = depoPi.getPkCnord();
		Set<String> pkCnords = new HashSet<>(list);
		
		String sqlAllBod = " select * from bl_op_dt where pk_cnord in ( " 
									+ CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") 
									+ ") and flag_settle='0' ";//flag_settle=0过滤医嘱未结算记录
		List<BlOpDt> allBlOpDts = DataBaseHelper.queryForList(sqlAllBod, BlOpDt.class);
		if(allBlOpDts == null){
			throw new BusException("结算记录为空，不能进行账户支付!");
		}
		BlOpDt blOpDt = allBlOpDts.get(0);
		
		// 2 走了内部医保和价格优惠计算应收的总金额
		Map<String, Double> reducedPrice = clinicPayService.reducedPrice(allBlOpDts);
		ClinicPayInputVo inputParam = new ClinicPayInputVo();
		inputParam.setEuPvtype(EnumerateParameter.ONE);
		inputParam.setPkPv(pkPv);
		reducedPrice = clinicPayService.insuranceReduceAmout(inputParam, reducedPrice, UserContext.getUser().getPkOrg());
		
		// 3 根据上步的金额建结算和明细表
		BlSettle blSettle = createBlSettle(reducedPrice, blOpDt);
		String pkSettle = blSettle.getPkSettle();
		createBlSettleDetail(allBlOpDts, pkSettle);
		createBlDeposit(allBlOpDts, blSettle);
		
		// 4：处理账户余额
		ApplicationUtils.setDefaultValue(depoForSave, true);
		depoForSave.setModityTime(null);
//		depoForSave.setAmount(blSettle.getAmountPi());
		balAccoutService.saveMonOperation(depoForSave, user, pkPv, euOptype,depoForSave.getDtPaymode());
		
		
		// 5 更新收费项目明细表支付状态
		for(BlOpDt allBlOpDt : allBlOpDts){
			allBlOpDt.setFlagAcc(EnumerateParameter.ONE);
			allBlOpDt.setFlagSettle(EnumerateParameter.ONE);
			allBlOpDt.setPkSettle(pkSettle); 
			ApplicationUtils.setDefaultValue(allBlOpDt, false);
		}
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class), allBlOpDts);
		
		// 三、处理业务计费表及处方、检查检验、库存相关表
		for (String pkCnord: list) {
			if (EnumerateParameter.ONE.equals(depoPi.getFlagYp())) {//处方信息
				List<BlOpDt> blOpDts = DataBaseHelper.queryForList(" select * from bl_op_dt where pk_cnord=? ", BlOpDt.class, pkCnord); 
				// 处理处方、检查检验、库存相关表
				settlePdOutService.makeSdPresAssitRecords(blOpDts);
			}else {	
				CnOrder ord =  DataBaseHelper.queryForBean(" select * from cn_order where pk_cnord = ?", CnOrder.class, pkCnord);		
				PvEncounter pvvo = DataBaseHelper.queryForBean(" select * from pv_encounter where pk_pv = ?", PvEncounter.class, pkPv);
				// 3 写表 EX_ASSIST_OCC 	
				String sqll = "select count(1) as ct from ex_assist_occ where pk_cnord=? and flag_refund != '1'";
				int mapCount = DataBaseHelper.queryForScalar(sqll,Integer.class, pkCnord);
				if( mapCount ==0 ){
					// 调用生成医技执行单
					createExAssistOcc(pkPv, pvvo, pkCnord, ord);
				}
				
			}
		}
	}
	
	/*
	 * 付款方式表
	 */
	private void createBlDeposit(List<BlOpDt> allBlOpDts, BlSettle blSettle) {
		PiAcc piAcc = balAccoutService.queryParentOrPiAccByPkPi(blSettle.getPkPi());
		BlDeposit blDeposit = new BlDeposit();
		blDeposit.setPkOrg(blSettle.getPkOrg());
		blDeposit.setPkAcc(piAcc.getPkPiacc());
		blDeposit.setEuDirect(EnumerateParameter.ONE);
		blDeposit.setPkPi(blSettle.getPkPi());
		blDeposit.setPkPv(blSettle.getPkPv());
		blDeposit.setAmount(blSettle.getAmountPi());     // 支付金额
		blDeposit.setDtPaymode(EnumerateParameter.FOUR); //支付方式
		blDeposit.setDatePay(new Date());
		blDeposit.setPkDept(blSettle.getPkDeptSt());
		blDeposit.setPkEmpPay(blSettle.getPkEmpSt());
		blDeposit.setNameEmpPay(blSettle.getNameEmpSt());
		blDeposit.setFlagSettle(EnumerateParameter.ONE);
		blDeposit.setPkSettle(blSettle.getPkSettle());
		
		ApplicationUtils.setDefaultValue(blDeposit, true);
		DataBaseHelper.insertBean(blDeposit);
	}
	
	/*
	 * 生成结算明细
	 */
	private void createBlSettleDetail(List<BlOpDt> allBlOpDts, String pkSettle) {
		List<BlSettleDetail> blSettleDetails = new ArrayList<>();
		for (BlOpDt blOpDt : allBlOpDts) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(pkSettle);
			blSettleDetail.setAmount(blOpDt.getAmount());// 患者微信/支付宝支付的金额
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), blSettleDetails);
	}
	
	/*
	 * 生成结算记录
	 */
	private BlSettle createBlSettle(Map<String, Double> reducedPrice, BlOpDt blOpDt) {
		double amountSt = reducedPrice.get("amountSt");// 结算金额
		double amountPi = reducedPrice.get("amountPi");// 患者自付金额
		double amountInsu = reducedPrice.get("amountInsu");// 医保支付金额
		double discAmount = reducedPrice.get("discAmount");// 患者优惠金额
		
		BlSettle blSettle = new BlSettle();
		blSettle.setAmountSt(new BigDecimal(amountSt));  // 结算金额
		blSettle.setAmountPi(new BigDecimal(amountPi));
		blSettle.setAmountInsu(new BigDecimal(amountInsu).add(new BigDecimal(discAmount))); // 经过医保计划处理后的医保分摊
		blSettle.setPkOrg(UserContext.getUser().getPkOrg());
		blSettle.setPkPi(blOpDt.getPkPi());
		blSettle.setPkPv(blOpDt.getPkPv());
		blSettle.setDtSttype("00");  // 结算类型00
		//查询此次结算的医保计划
		PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", 
																	PvEncounter.class, blOpDt.getPkPv());
		blSettle.setPkInsurance(pvEncounter.getPkInsu()); //医保计划
		blSettle.setEuStresult(EnumerateParameter.ZERO); //正常结算
		blSettle.setFlagCc(EnumerateParameter.ZERO); //操作员结账标志
		blSettle.setFlagArclare(EnumerateParameter.ZERO); //欠费结清标志
		blSettle.setEuPvtype(EnumerateParameter.ONE); //就诊类型：门诊
		
		blSettle.setDateSt(new Date());
		blSettle.setPkOrgSt(UserContext.getUser().getPkOrg());
		blSettle.setPkDeptSt(UserContext.getUser().getPkDept());
		blSettle.setPkEmpSt(UserContext.getUser().getPkEmp());
		blSettle.setNameEmpSt(UserContext.getUser().getNameEmp());
		ApplicationUtils.setDefaultValue(blSettle, true);
		DataBaseHelper.insertBean(blSettle);
		return blSettle;
	}
	/*
	 * 写表 EX_ASSIST_OCC 
	 */
	private void createExAssistOcc(String pkPv, PvEncounter pvvo, String pkCnord, CnOrder ord) {
	   ExAssistOcc assit = new ExAssistOcc();		
       assit.setPkOrg(UserContext.getUser().getPkOrg());//pk_org，当前机构；
       assit.setPkCnord(pkCnord);//pk_cnord，医嘱主键；
       assit.setPkPv(pkPv);//		   pk_pv，患者就诊；
       assit.setPkPi(pvvo.getPkPi());//		   pk_pi，患者主键；
       assit.setEuPvtype(pvvo.getEuPvtype()); //		   eu_pvtype，就诊类型；
       assit.setCodeOcc(ApplicationUtils.getCode("0503"));//执行单号，调用编码规则接口获取；
       assit.setPkDept(ord.getPkDept());//		   pk_dept，开立科室；
       assit.setPkEmpOrd(ord.getPkEmpOrd());//		   pk_emp_ord，开立医生；
       assit.setNameEmpOrd(ord.getNameEmpOrd());//		   name_emp_ord，开立医生姓名；
       assit.setDateOrd(ord.getDateStart());//		   date_ord，开立日期；
       assit.setTimesOcc(1);//	   times_occ，执行顺序号（按计划执行日期排序赋值）；
       assit.setQuanOcc(ord.getQuan());//		   quan_occ，每次执行数量；
	   Date dateEnd = DateUtils.getSpecifiedDay(ord.getDateStart(), ord.getDays().intValue());
	   OrderAppExecVo orderAppExecVo = new OrderFreqCalCountHandler().calCount(ord.getCodeOrdtype(), ord.getCodeFreq(), ord.getDateStart(), dateEnd, ord.getQuan(), false);
	   assit.setDatePlan(orderAppExecVo.getExceList().get(0).getExceTime());//	   date_plan，计划执行日期，从频次字典获取；
	   assit.setTimesTotal((int)orderAppExecVo.getCount());//		   times_total，总执行次数，频次*天数；  
       assit.setPkOrgOcc(ord.getPkOrgExec());//		       pk_org_occ，执行机构；
       assit.setPkDeptOcc(ord.getPkDeptExec());//		   pk_dept_occ，执行科室；
       assit.setFlagOcc("0");//		   flag_occ，0；
       assit.setFlagCanc("0");//		   flag_canc，0；
       assit.setInfantNo("0");//		   infant_no，0；
       assit.setEuStatus("0");//		   eu_status，0；
       assit.setFlagPrt("0");//		   flag_prt，0。
       assit.setFlagRefund(EnumerateParameter.ZERO);
       ApplicationUtils.setDefaultValue(assit, true);
       DataBaseHelper.insertBean(assit);
	}
	
	
	
	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if(StringUtils.hasText(date)){
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}		
		return temp;
	}
	/**
	 * 执行科室结算（账户结算和其他结算两种方式）
	 * @param param
	 * @param user
	 * @return
	 */
	public String departmentDeductions(String param,IUser user){
		BlDepositPi depoForSave = JsonUtil.readValue(param, BlDepositPi.class);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkPi", depoForSave.getPkPi());
		String status = "";
		PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(depoForSave.getPkPi());// 患者账户信息
		if (piAcc!=null) {
			Map<String, BigDecimal> accountMap =  pareAccoutService.getPatiAccountAvailableBalance(depoForSave.getPkPi());
			BigDecimal accountAvailableBalance = accountMap.get("accLimit");//可用余额
			if (accountAvailableBalance.compareTo(depoForSave.getAmount().abs())>=0) { //可用余额大于需支付的金额，调用账户支付接口
				status = EnumerateParameter.ONE;
			}else {
				status = EnumerateParameter.ZERO;
			}
		} else {
			status = "该患者没有可用账户";
		}
		return status;		
	}
	
}

