package com.zebone.nhis.bl.opcg.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;

import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.opcg.dao.OpCgQueryMapper;
import com.zebone.nhis.bl.opcg.vo.IsStopOpParamVo;
import com.zebone.nhis.bl.pub.service.BlIpMedicalExeService;
import com.zebone.nhis.bl.pub.service.BlOpMedicalExeService;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.bl.pub.service.SettlePdOutService;
import com.zebone.nhis.bl.pub.syx.vo.BlOpDtRefundVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.BlPubSettleVo;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdDept;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.scm.pub.vo.PdStockVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * ????????????????????????
 * @author gongxy
 */
@Service
public class OpCgSettlementService {

	@Autowired
	private OpCgPubService opcgPubService;
	
	@Autowired
	private OpcgPubHelperService opcgPubHelperService;

	@Autowired
	private CgQryMaintainService cgQryMaintainService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PareAccoutService pareAccoutService;
	
	@Autowired
	private SettlePdOutService settlePdOutService; // ???????????????????????????????????????

	@Autowired
	private OpCgQueryMapper opCgQueryMapper;
	
	@Resource
	private BlOpMedicalExeService medicalExeService;
	
	@Autowired
	private BalAccoutService balAccoutService;
	
	/**
	 * ???????????????
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Map<String, Object>> countRegisteredAccountingSettlement(String param, IUser user) {

		// ?????????????????????????????????
		List<Map<String, Object>> mapListParam = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
		});
		
		if(mapListParam == null || mapListParam.size()<=0)
			throw new BusException("?????????????????????????????????");
		//????????????????????????????????????????????????????????????????????????????????????
		verfyPiHp(mapListParam);
		List<Map<String, Object>> mapResultList = new ArrayList<Map<String, Object>>();
		
		BigDecimal aggregateAmount = BigDecimal.ZERO;// ????????????
		BigDecimal medicarePayments = BigDecimal.ZERO;// ????????????
		BigDecimal accountBalance = BigDecimal.ZERO;// ????????????
		BigDecimal accLimit = BigDecimal.ZERO;// ??????????????????????????????????????????
		BigDecimal creditAcc = BigDecimal.ZERO;// ????????????
		//Map<String, Double> result = new HashMap<String, Double>();
		String pkPi = mapListParam.get(0).get("pkPi").toString();
		//?????????????????????
		boolean isClinicFlag = false;
		if(mapListParam.get(0).get("isClinicFlag")!= null){
		isClinicFlag = BlcgUtil.converToTrueOrFalse(mapListParam.get(0).get("isClinicFlag").toString());
		}
		if (EnumerateParameter.ZERO.equals(ApplicationUtils.getSysparam("PI0007", false))) {//???????????????????????????
			Map<String, BigDecimal> map = accountBalance(mapListParam, pkPi);
			accountBalance = map.get("accountBalance");
			accLimit = map.get("accLimit");
			creditAcc = map.get("creditAcc");
		}else { //????????????
			//?????????????????????
			if (mapListParam.get(0).get("cardNo")!=null&&!StringUtils.isEmpty(mapListParam.get(0).get("cardNo").toString())) {		 
			 Map<String, BigDecimal> map = accountBalance(mapListParam, pkPi);
			 accountBalance = map.get("accountBalance");
			 accLimit = map.get("accLimit");
			 creditAcc = map.get("creditAcc");
			}else {
				accountBalance = BigDecimal.ZERO;
			}
		}
		BigDecimal accountReceivable = BigDecimal.ZERO;//????????????
		BigDecimal accountTotalPay = BigDecimal.ZERO;//????????????
		for (Map<String, Object> mapParam : mapListParam) {
			mapParam.put("pkOrg", ((User) user).getPkOrg());
			// ??????????????????????????????
			Map resultOnePati = opcgPubService.registerPreParedSettlement(mapParam);
			Map<String, Object> mapList = new HashMap<>();
			mapList.put("orderNo", mapParam.get("orderNo"));// ???????????????
			BigDecimal patientsPay = new BigDecimal(resultOnePati.get("patientsPay")==null?"0":resultOnePati.get("patientsPay").toString());// ????????????
			//????????????????????????????????????????????????????????????????????????????????????
			if(isClinicFlag){
				//????????????????????????????????????????????????????????????????????????
				if (accLimit.compareTo(patientsPay)>=0) {
					accountTotalPay = accountTotalPay.add(patientsPay);
					mapList.put("accountPay", accountTotalPay);
					accountBalance = accountBalance.subtract(patientsPay);
					mapList.put("accountReceivable", 0D);
				} else {
					throw new BusException("?????????????????????????????????"+"  ??????????????????"+patientsPay+"?????? ????????????"+accountBalance+"??????");
				}
			}else{
				//????????????????????????????????????????????????
				if (accountBalance.compareTo(patientsPay)>=0) {
					accountTotalPay = accountTotalPay.add(patientsPay);
					mapList.put("accountPay", accountTotalPay);
					accountBalance = accountBalance.subtract(patientsPay);
					mapList.put("accountReceivable", 0D);//???????????? = ????????????-????????????
				} else {
					accountTotalPay = accountTotalPay.add(accountBalance);
					mapList.put("accountPay", accountTotalPay);
					//?????????????????????????????????????????????????????????????????????????????????0
					//accountBalance = accountBalance.subtract(accLimit);
					accountBalance = BigDecimal.ZERO;
					//???????????????????????????????????????????????????
					//accountReceivable = accountReceivable.add((patientsPay.subtract(accLimit)));
					//???????????? = ????????????-????????????
					accountReceivable = accountReceivable.add((patientsPay.subtract(accountTotalPay)));
					mapList.put("accountReceivable", accountReceivable);
				}
			}
			accLimit = accountBalance.add(creditAcc);
			
			aggregateAmount = new BigDecimal(resultOnePati.get("aggregateAmount")==null?"0":resultOnePati.get("aggregateAmount").toString());
			medicarePayments = new BigDecimal(resultOnePati.get("medicarePayments")==null?"0":resultOnePati.get("medicarePayments").toString());
			
			mapList.put("aggregateAmount", aggregateAmount);// ????????????
			mapList.put("medicarePayments", medicarePayments);// ????????????
			mapList.put("patientsPay", patientsPay);// ????????????
			mapList.put("accountBalance", accountBalance);//????????????
			
			mapResultList.add(mapList);
		}
		
		return mapResultList;
	}
	/**
	 * ????????????????????????????????????????????????????????????
	 */
    private void verfyPiHp(List<Map<String, Object>> mapListParam){
    	String pkPi = mapListParam.get(0).get("pkPi").toString();
    	PvEncounter pv = opCgQueryMapper.queryPvAndHpInof(pkPi);
    	if(pv==null||CommonUtils.isEmptyString(pv.getPkPv()))
    		return;
    	for(Map<String, Object> param:mapListParam){
    		List<Map<String, Object>> insuLists = (List<Map<String, Object>>) param.get("insuList");// ??????????????????(?????????????????????)
    		if(insuLists==null||insuLists.size()<=0) return;
    		String pkInsuMain = "";
    		for (Map<String, Object> mapTemp : insuLists) {
    			if (EnumerateParameter.ONE.equals(mapTemp.get("flagMaj").toString())) {
    				pkInsuMain = mapTemp.get("pkHp").toString();
    			}
    		}
    		if(pkInsuMain!=null&&pkInsuMain.equals(pv.getPkInsu())){
    			throw new BusException("??????????????????????????????????????????????????????????????????????????????");
    		}
    	}
    	
    }
	/**
	 * ???????????????
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OpCgTransforVo countRefoundRegisteredSettlement(String param, IUser user) {

		// ??????????????????
		Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);
		mapParam.put("pkOrg", ((User) user).getPkOrg());
		mapParam.put("pkEmp", ((User) user).getPkEmp());
		mapParam.put("empName", ((User) user).getNameEmp());
		OpCgTransforVo rrpv = opcgPubService.registrationPreparedRefound(mapParam);
		return rrpv;
	}

	/**
	 * ?????????????????????????????????????????????????????????(??????????????????????????????)
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettle(String param, IUser user) {

		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		//??????????????????????????????
		if(!EnumerateParameter.ONE.equals(opCgTransforVo.getFilterOrdEffeDate())) {
			String curtime = DateUtils.getDateTimeStr(new Date());
			mapParam.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //???????????????????????????
		}
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
		mapParam.put("termOfValidity",opCgTransforVo.getTermOfValidity());
		List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettle(mapParam);
		String isShowWinno = ApplicationUtils.getSysparam("EX0079",false);
		if("1".equals(isShowWinno)){
			Object resObj=ExtSystemProcessUtils.processExtMethod("DrugWinnoRule", "getNotSettlePresInfo",mapResult);
			if(resObj!=null ){
				return (List<BlPatiCgInfoNotSettleVO>)resObj;
			}
		}
		return mapResult;
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 * ???????????? 007002003025
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
	 * ????????????????????????pk_pv?????????????????????????????????
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
	//?????????????????????????????????
	private List<BlPatiCgInfoNotSettleVO> queryPatiCgInfoNotSettleBypkPvSql(String pkPv) {
		
		String sql = "select cate.name itemcate,pres.pres_no presNo,dt.name_cg nameCg,dt.spec,unit.name unit,"
				+ "dt.quan,dt.amount,dt.amount_pi amountPi ,dt.pk_cnord pkCnord,dt.flag_pv flagPv,bod.name_dept  nameDept,bod.code_dept codeDept,pres.dt_prestype,"
				+ "dt.pk_dept_ex pkDeptEx,pd.eu_drugtype euDrugType,dt.name_emp_app nameEmpOrd,dt.pk_emp_app pkEmpApp,boe.code_emp,pd.code as itemCode"
				+ " from bl_op_dt dt inner join bd_itemcate cate on dt.pk_itemcate=cate.pk_itemcate"
				+ " left join bd_unit unit on dt.pk_unit=unit.pk_unit"
				+ " left outer join cn_prescription pres on dt.pk_pres=pres.pk_pres "
				+ " left outer join bd_pd pd on dt.pk_pd=pd.pk_pd "
				+ " left join bd_ou_dept bod on dt.pk_dept_app = bod.pk_dept"
				+ " left join bd_ou_employee boe on dt.pk_emp_app = boe.pk_emp"
				+ " where dt.pk_pv=? and dt.flag_settle='0' and dt.flag_acc='0'";
		
		List<BlPatiCgInfoNotSettleVO> list = DataBaseHelper.queryForList(sql, BlPatiCgInfoNotSettleVO.class, pkPv);
		return list;
	}
	
	/**
	 * ???????????????????????????<br>
	 * ????????????007002003002<br>
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public OpCgTransforVo countOpcgAccountingSettlement(String param, IUser user) {

		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
//		double accountPrepaid = 0d;// ???????????? --?????????????????????????????????????????????????????????
		// ??????????????????????????????????????????
		OpCgTransforVo rgv = opcgPubService.useInsComputSettle(user, opCgTransforVo);
		// ???????????????????????????????????????????????????
		if (EnumerateParameter.ZERO.equals(ApplicationUtils.getSysparam("PI0007", false))) {
			rgv = accountPayment(opCgTransforVo, rgv);
		}else {
			if (opCgTransforVo.getCardNo()!=null&&!opCgTransforVo.getCardNo().isEmpty()) {//????????????????????????????????????
			rgv = accountPayment(opCgTransforVo, rgv);			
			}else {
				rgv.setAccountBalance(BigDecimal.ZERO);
				rgv.setAccountPay(BigDecimal.ZERO);
				rgv.setAccountReceivable(rgv.getPatientsPay());	
			}		
		}
		rgv.setPkPi(opCgTransforVo.getPkPi());
		
		return rgv;
	}
	
	
	/**
	 * ????????????????????????(????????????????????????????????????,????????????)
	 * @param param
	 * @param user
	 * @return
	 */
	public OpCgTransforVo mergeOpcgAccountedSettlement(String param, IUser user) {
		// ????????????
		OpCgTransforVo opCgTransforVo = JsonUtil.readValue(param, OpCgTransforVo.class);
		if(CommonUtils.isEmptyString(opCgTransforVo.getPkPv()))
			throw new BusException("???????????????pkPv!");
		if(CommonUtils.isEmptyString(opCgTransforVo.getPkPv()))
			throw new BusException("???????????????pkPi!");
		List<BlOpDt> blOpDts = opCgTransforVo.getBlOpDts();
		if (CollectionUtils.isEmpty(blOpDts))
			throw new BusException("???????????????????????????????????????");
		if (CollectionUtils.isEmpty(opCgTransforVo.getBlDeposits()))
			throw new BusException("????????????????????????????????????????????????");
		opCgTransforVo.getInvoiceInfo();
		//?????????????????????????????????LB???????????????, -2????????????
		if(opCgTransforVo.getInvStatus()==null || !("-2").equals(opCgTransforVo.getInvStatus())){
			//??????BL0008???????????????1(??????????????????)
			if ("1".equals(ApplicationUtils.getSysparam("BL0008", false)) && CollectionUtils.isEmpty(opCgTransforVo.getInvoiceInfo()))
				throw new BusException("???????????????????????????????????????");
		}
		Set<String> pkBlOpDt = new HashSet<String>();
		for (BlOpDt blOpDt : blOpDts) {
			pkBlOpDt.add(blOpDt.getPkCgop());
		}
		opCgTransforVo.setAmtInsuThird(opCgTransforVo.getAmtInsuThird()==null?BigDecimal.valueOf((int)0):opCgTransforVo.getAmtInsuThird());
		opCgTransforVo.setCurDate(new Date());
		// ??????????????????????????????
		String pkBlOpDtInSql = CommonUtils.convertSetToSqlInPart(pkBlOpDt, "pk_cgop");
		String sql = "select * from bl_op_dt where pk_cgop in (" + pkBlOpDtInSql + ")";
		blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] {});
		
		if(blOpDts!=null && pkBlOpDt.size()!=blOpDts.size()){
			throw new BusException("??????????????????????????????????????????????????????????????????");
		}
		//????????????????????????
		List<BlOpDt> pvSettleBlOpDts  = new ArrayList<BlOpDt>();//????????????????????????
		List<BlOpDt> noSettleBlOpDts  = new ArrayList<BlOpDt>();//???????????????
		Set<String> noSettlePkBlOpDt = new HashSet<String>();
		for (BlOpDt blOpDt : blOpDts) {
			if("1".equals(blOpDt.getFlagSettle())&&"1".equals(blOpDt.getFlagPv())){
				pvSettleBlOpDts.add(blOpDt);
			}else{
				for(BlOpDt opdt_temp:opCgTransforVo.getBlOpDts()){
					if(opdt_temp.getPkCgop().equals(blOpDt.getPkCgop())){
						blOpDt.setPkDeptEx(opdt_temp.getPkDeptEx());
						blOpDt.setWinnoConf(opdt_temp.getWinnoConf());
						blOpDt.setWinnoPrep(opdt_temp.getWinnoPrep());
					}
				}
				noSettleBlOpDts.add(blOpDt);
				noSettlePkBlOpDt.add(blOpDt.getPkCgop());
			}
		}
		//???????????????????????????????????????
		if(pvSettleBlOpDts!=null&&pvSettleBlOpDts.size()>0){
			//?????????????????????????????????
			List<BlOpDt> newpvlist = this.processPvBlOpDtSettleInfo(pvSettleBlOpDts,(User)user);
			if(newpvlist!=null&&newpvlist.size()>0){
				noSettleBlOpDts.addAll(newpvlist);
				for(BlOpDt newdt : newpvlist){
					noSettlePkBlOpDt.add(newdt.getPkCgop());
				}
			}
			
		}
		//???????????????????????????????????????0?????????????????????????????????????????????????????????
		if(opCgTransforVo.getBlDeposits()==null || opCgTransforVo.getBlDeposits().size()<=0){
			List<BlDeposit> depoList = new ArrayList<>();
			BlDeposit depo = new BlDeposit();
			depo.setDtPaymode("1");
			depo.setAmount(BigDecimal.valueOf(0D));
			depoList.add(depo);
			opCgTransforVo.setBlDeposits(depoList);
		}else if(opCgTransforVo.getBlDeposits()!=null && opCgTransforVo.getBlDeposits().size()==1){
			if("4".equals(opCgTransforVo.getBlDeposits().get(0).getDtPaymode()) && 
					opCgTransforVo.getBlDeposits().get(0).getAmount().compareTo(BigDecimal.valueOf(0D))==0){
				opCgTransforVo.getBlDeposits().get(0).setDtPaymode("1");
			}
		}
		BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
		blPubSettleVo.setPkPi(opCgTransforVo.getPkPi());
		blPubSettleVo.setPkPv(opCgTransforVo.getPkPv());
		blPubSettleVo.setBlOpDts(noSettleBlOpDts);
		blPubSettleVo.setInvoiceInfo(opCgTransforVo.getInvoiceInfo()); // ??????????????????
		blPubSettleVo.setDepositList(opCgTransforVo.getBlDeposits());
		blPubSettleVo.setInvStatus(opCgTransforVo.getInvStatus());//?????????????????????????????????LB???????????????, -2????????????
		blPubSettleVo.setPkBlOpDtInSql( CommonUtils.convertSetToSqlInPart(noSettlePkBlOpDt, "pk_cgop"));
		blPubSettleVo.setMachineName(opCgTransforVo.getMachineName());
		blPubSettleVo.setAmtRound(opCgTransforVo.getAmtRound());
		blPubSettleVo.setFlagPrint(opCgTransforVo.getFlagPrint());
		blPubSettleVo.setAmountDisc(opCgTransforVo.getAmountDisc());
		
		List<BlInvoice> blInvoice = null;
		List<BlInvoiceDt> blInvoiceDt = null;
		Map<String,Object> result =  opcgPubService.accountedSettlement(blPubSettleVo, true,opCgTransforVo.getAmtInsuThird());
		if(result!=null){
			blInvoice = (List<BlInvoice>)result.get("inv");
			blInvoiceDt = (List<BlInvoiceDt>)result.get("invDt");
		}
			
		// ?????????????????????
		if (blInvoice != null && blInvoice.size() > 0)//??????????????????????????????
		{
//			List<Map<String, Object>> blInvoiceInfoOtherInfo = new ArrayList<>();
			//List<InvoiceInfo> invoInfos = opCgTransforVo.getInvoiceInfo();
			for (BlInvoice invoInfo : blInvoice) {
				if(invoInfo!=null && !CommonUtils.isEmptyString(invoInfo.getCodeInv())){
					// ????????????
					commonService.confirmUseEmpInv(invoInfo.getPkEmpinvoice(), 1L);
				}
			}
			// ??????????????????
			opCgTransforVo.setBlInvoiceDts(blInvoiceDt);
			opCgTransforVo.setBlInvoices(blInvoice);
		}
		
		for (BlOpDt blOpDt : noSettleBlOpDts) {
			blOpDt.setPkSettle(CommonUtils.getString(result.get("pkSettle")));
		}
		opCgTransforVo.setPkSettle(CommonUtils.getString(result.get("pkSettle")));
		
		//???????????????????????????????????????????????????
		List<BlOpDt> exDtList = new ArrayList<>();
		for(BlOpDt dt : noSettleBlOpDts){
			if(CommonUtils.isEmptyString(dt.getFlagRecharge()) || !"1".equals(dt.getFlagRecharge()))
				exDtList.add(dt);
		}
		
		if(exDtList!=null && exDtList.size()>0)
			settlePdOutService.makeSdPresAssitRecords(exDtList); 
		
		//?????????????????????????????????
		Map<String,Object> paramVo=JsonUtil.readValue(param, Map.class);
		paramVo.put("Control", "OK");
		paramVo.put("Source", opCgTransforVo.getSource());
		paramVo.put("pkSettle", opCgTransforVo.getPkSettle());
		PlatFormSendUtils.sendBlOpSettleMsg(paramVo);
		
		//???????????????????????????????????????
		Map<String,Object> paramEmrVo= new HashMap<>(16);
		paramEmrVo.put("pkList", pkBlOpDt);
		paramEmrVo.put("euSettle", "1");//0????????????1???????????????2??????
		ExtSystemProcessUtils.processExtMethod("EmrStSp", "saveOrUpdateCharges", new Object[]{paramEmrVo});
		
		return opCgTransforVo;
	}

	/**
	 * ??????????????????????????????
	 * @param pkInvoice
	 * @return
	 */
	private Map<String, Object> queryBlInvoiceInfo(String pkInvoice) {

		if (StringUtils.isEmpty(pkInvoice))
			throw new BusException("????????????????????????????????????");
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
		// ??????????????????
		sql.append("select detail.* from bl_settle_detail detail inner join bl_settle settle on detail.pk_settle=settle.pk_settle");
		sql.append(" inner join bl_st_inv bsi on bsi.pk_settle=settle.pk_settle and bsi.pk_invoice=?");
		return rst;
	}
	
	
	public OpCgTransforVo accountPayment(OpCgTransforVo opCgTransforVo,OpCgTransforVo rgv){
		//Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("pkPi", opCgTransforVo.getPkPi());
		PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(opCgTransforVo.getPkPi());// ??????????????????
		if (piAcc != null) {
			Map<String, BigDecimal> accountMap =  pareAccoutService.getPatiAccountAvailableBalance(opCgTransforVo.getPkPi());
			//BigDecimal accountAvailableBalance = accountMap.get("accLimit");
			//????????????????????????????????????????????????
			BigDecimal accountAvailableBalance = accountMap.get("acc");
			//?????????????????????????????????????????????????????????-??????
			Double amtAcc = DataBaseHelper.queryForScalar(
					"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
					Double.class, new Object[]{opCgTransforVo.getPkPi()});
			if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
				accountAvailableBalance = accountAvailableBalance.subtract(BigDecimal.valueOf(amtAcc));

				//??????????????????????????????0????????????0
				if(accountAvailableBalance.compareTo(BigDecimal.valueOf(0D))<0){
					accountAvailableBalance = BigDecimal.valueOf(0D);
				}
			}
			// ????????????????????????????????????
			if(opCgTransforVo.getIsSaoMaPay()){
				rgv.setAccountPay(BigDecimal.ZERO);
				rgv.setAccountReceivable(rgv.getPatientsPay());
			}else{
				// ?????????????????????????????????????????????????????????????????????????????????????????????
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
		 * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		 */
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		BigDecimal accountBalance = BigDecimal.ZERO;// ????????????
		BigDecimal accLimit = BigDecimal.ZERO;// ??????????????????????????????????????????
		BigDecimal creditAcc = BigDecimal.ZERO;// ????????????
		// ??????????????????????????????			
		PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(pkPi);// ??????????????????
		// ??????????????????
		if(piAcc != null){
			// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			Map<String,BigDecimal> accountAvailableBalance = pareAccoutService.getPatiAccountAvailableBalance(pkPi);
			// ?????????????????????????????????
			accountBalance = accountAvailableBalance.get("acc");
			accLimit = accountAvailableBalance.get("accLimit");
			creditAcc = accountAvailableBalance.get("creditAcc");//???????????????????????????????????????
			// ?????????????????????0 ???????????????????????????
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
	 * ????????????????????????
	 * 007002003034
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
	 * ???????????????????????????????????????????????????????????????????????????
	 * 007002003035
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
	 * ???????????????????????????
	 * 007002003036
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
	 * ????????????????????????
	 * 007002003037
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
	 * ???????????????????????????????????????
	 * @param pvSettleBlOpDts
	 * @return
	 */
	private List<BlOpDt> processPvBlOpDtSettleInfo(List<BlOpDt> pvSettleBlOpDts,User u){
		if(pvSettleBlOpDts==null||pvSettleBlOpDts.size()<=0)
			return null;	
		Map<String,Object> mapParam = new HashMap<String,Object>();
		String pkSettle = pvSettleBlOpDts.get(0).getPkSettle();
		List<BlOpDt> insertDtlist = new ArrayList<BlOpDt>();
		//TODO ???????????????????????????
		for(BlOpDt dt:pvSettleBlOpDts){
			if(!pkSettle.equals(dt.getPkSettle()))
				throw new BusException("????????????????????????????????????????????????????????????????????????");
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
		// ????????????????????????????????????
		mapParam.put("pkSettle", pkSettle);
		BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
		// ????????????????????????
		String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle);
		// ??????????????????
		opcgPubHelperService.generateRefoundRecord(pvSettleBlOpDts, blSettle.getPkSettle());// ????????????????????????
		// ????????????????????????????????????
		List<BlSettleDetail> blSettleDetail = cgQryMaintainService.qryBlSettleDetailInfoByBlSettle(mapParam);
		// ????????????????????????
		opcgPubHelperService.generateRefoundSettleDetail(blSettleDetail,pkSettleCanc);

		// ??????????????????????????????????????????
		List<BlDeposit> blDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(mapParam);
		
		// ?????????????????????????????????
		List<BlDeposit> negaBlDeposits = opcgPubHelperService.generateRefoundBlDeposits(blDeposits,pkSettleCanc);
		
		//??????????????????
		if(negaBlDeposits != null){
			for (BlDeposit negaBlDeposit : negaBlDeposits) {
				if (negaBlDeposit.getDtPaymode().equals(IDictCodeConst.PATIACCOUNT)) {
					// ???????????????????????????????????????????????????
					BlDepositPi blDepositPi = new BlDepositPi();
					ApplicationUtils.setDefaultValue(blDepositPi, true);
					blDepositPi.setEuDirect(EnumerateParameter.ONE);
					blDepositPi.setPkPi(pkPi);
					blDepositPi.setAmount(negaBlDeposit.getAmount().abs());
					blDepositPi.setDtPaymode(EnumerateParameter.FOUR);
					blDepositPi.setPkEmpPay(u.getPkEmp());
					blDepositPi.setNameEmpPay(u.getNameEmp());
					PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(pkPi);// ??????????????????
					ApplicationUtils.setDefaultValue(piAcc, false);
					piAcc.setAmtAcc((piAcc.getAmtAcc() == null ? BigDecimal.ZERO : piAcc.getAmtAcc()).add(blDepositPi.getAmount()));
					balAccoutService.piAccDetailVal(piAcc, blDepositPi, pkPv, null);
				}
			}
		}

		String BL0002_code = ApplicationUtils.getSysparam("BL0002", false);
		// ????????????????????????????????????????????????
		if (EnumerateParameter.ONE.equals(BL0002_code)) {
			// ??????????????????????????????????????????????????????
			List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
			if(blInvoices!=null&&blInvoices.size()>0){
				for(BlInvoice inv:blInvoices){
					// ????????????????????????
					opcgPubHelperService.updateRefoundBlInvoice(inv);
				}
			}
		}
		//????????????????????????
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), insertDtlist);
		//????????????????????????
		return insertDtlist;
	}
	
	//?????????????????????????????????????????????007002003038
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
		
		//??????
		List<String> lisPkCnords = new ArrayList<>();
		//??????
		List<String> pacsPkCnords = new ArrayList<>();
		
		//??????????????????
		List<CnOrder> list = opCgQueryMapper.qryCnOrderList(map);
		for (CnOrder cnOrder : list) {
			//02-?????????03-??????
			if("02".equals(cnOrder.getCodeOrdtype())){
				pacsPkCnords.add(cnOrder.getPkCnord());
			}else if("03".equals(cnOrder.getCodeOrdtype())){
				lisPkCnords.add(cnOrder.getPkCnord());
			}
		}
		
		//??????????????????
		if(lisPkCnords.size() > 0){
			ExtSystemProcessUtils.processExtMethod("LIS", "saveLisApply",lisPkCnords,true);
		}
		//??????????????????
		if(pacsPkCnords.size() > 0){
			ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx",null,pacsPkCnords,true);
		}
	}
	
	//?????????????????????????????????????????????007002003039
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
		
		//??????
		List<String> lisPkCnords = new ArrayList<>();
		
		//??????????????????
		List<CnOrder> list = opCgQueryMapper.qryCnOrderList(map);
		for (CnOrder cnOrder : list) {
			//03-??????
			if("03".equals(cnOrder.getCodeOrdtype())){
				lisPkCnords.add(cnOrder.getPkCnord());
			}
		}
		
		//??????????????????
		if(lisPkCnords.size() > 0){
			ExtSystemProcessUtils.processExtMethod("LIS", "delLisApply",lisPkCnords,true);
		}
	}
}
