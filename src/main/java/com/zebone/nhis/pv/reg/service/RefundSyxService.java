package com.zebone.nhis.pv.reg.service;

import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.*;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pv.reg.dao.RegSyxMapper;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中山二院--门诊挂号服务--退号等服务
 * @author Alvin
 *
 */
@Service
public class RefundSyxService {

	@Resource
	private RegSyxMapper regSyxMapper;
	@Autowired
	private CgQryMaintainService cgQryMaintainService;	
	@Autowired
	private OpcgPubHelperService opcgPubHelperService;
	@Autowired
	private BalAccoutService balAccoutService;
	@Autowired
	private InvSettltService invSettltService;
	
	
	
	/**
	 * 退号查询的确认信息,包含 患者信息+挂号信息，挂号费明细，支付明细
	 * @param param
	 * @param user
	 * @return
	 */
	public PiMasterRegVo getPvRegInfo(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("codePv"))) {
			throw new BusException("未传入查询条件codePv");
		}
		//查询就诊基本信息
		List<PiMasterRegVo> regVoList = regSyxMapper.queryPvEncounterByCon(paramMap);
		PiMasterRegVo regvo = null;
		if(regVoList.size() ==0 || StringUtils.isBlank((regvo = regVoList.get(0)).getPkPv()))
			return regvo;
		//查询费用明细
		List<ItemPriceVo> cglist = regSyxMapper.queryRegFeeItems(regvo.getPkPv());
		regvo.setItemList(cglist);
		List<BlDeposit> depositList = regSyxMapper.queryRegPayItems(regvo.getPkPv(),regvo.getEuStatus());
		regvo.setDepositList(depositList);
		List<BlExtPay>blExtPayList=null ;
		if (depositList!=null&& depositList.size()>0){
			blExtPayList=regSyxMapper.queryBlextPayItems(depositList.get(0).getPkDepo());
		}
		//查询挂号结算主键
		Map<String, Object> mapSt = DataBaseHelper.queryForMap("select pk_settle from bl_op_dt where PK_PV=? and flag_pv='1'",
				new Object[] { regvo.getPkPv() });
		if (mapSt != null && MapUtils.getString(mapSt, "pkSettle") != null) {
			regvo.setPkSettle(mapSt.get("pkSettle").toString());
		}
		
		regvo.setBlExtPaysList(blExtPayList);
		return regvo;
	}

	public List<PiMasterRegVo> getPvRegInoSample(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || (CommonUtils.isNull(paramMap.get("codePv")) && CommonUtils.isNull(paramMap.get("codeOp")))) {
			throw new BusException("未传入查询条件");
		}
		return regSyxMapper.queryPvEncounterByCon(paramMap);
	}
	/**
	 * 
	 * 交易号：003002002009<br>
	 * 退号<br>
	 * <pre>
	 * 0.校验是否可以退号
	 * 1.生成负的结算信息，写表bl_settle；
	   2.生成负的记费信息，写表bl_op_dt；
	   3.生成负的支付记录，写表bl_deposit；
	   4.取消就诊记录，更新pv_encounter；
	   5.更新排班表，写表sch_sch；
	   6.如果挂号类别为预约挂号，更新sch_appt；
	   7.作废发票信息，更新表bl_invoice
	 * </pre>
	 * @param param
	 * @param user
	 * @return 
	 * @throws
	 * @author yangxue
	 */
	public  List<BlDeposit>  cancelReg(String param,IUser user){
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);
		if(regvo==null||CommonUtils.isNull(regvo.getPkPv()))
			throw new BusException("退号时未获取到就诊信息！");

		String pkPv = regvo.getPkPv();
		//如果没有传入pkSch，自己查询一次，避免有号表时未传入，导致没释放
		Map<String, Object> schMap = DataBaseHelper.queryForMap("select PK_SCH from PV_OP where PK_PV=? union select PK_SCH from PV_OP where PK_PV=?",
				new Object[]{pkPv, pkPv});
		if(CommonUtils.isNull(regvo.getPkSch()) && schMap.size()>0){
			regvo.setPkSch(MapUtils.getString(schMap,"pkSch"));
		}
		validCancelRegByConf(pkPv);

		Map<String, Object> mapParam = new HashMap<String, Object>();
		User u = (User)user;
		mapParam.put("pkPv", pkPv);
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("haveSettle", true);//要过滤掉未退号，先去给医保上报缴费时生成的一正一负的数据
		// 根据就诊主键查询挂号记费明细
		List<BlOpDt> blOpDts = cgQryMaintainService.qryRegCostInfoByPkpv(mapParam);
		//有计费的才退费
		//退款记录
		List<BlDeposit> negaBlDeposits=null;
		if(CollectionUtils.isNotEmpty(blOpDts)){
			int size = blOpDts.size();
			String pkSettle = blOpDts.get(0).getPkSettle();
			String pkPi = blOpDts.get(0).getPkPi();
			mapParam.put("pkPi", pkPi);
			if (size > 1)
				for (int i = 1; i < size; i++) {
					if (!pkSettle.equals(blOpDts.get(i).getPkSettle())) {
						throw new BusException("此次挂号费用异常，形成了两笔结算信息。" + "pkPv:【" + blOpDts.get(i).getPkPv() + "】");
					}
				}
			// 根据结算主键查询结算信息
			mapParam.put("pkSettle", pkSettle);
			BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
			// 生成退费结算信息
			if(blSettle == null) {
				throw new BusException("没有查询到患者的挂号结算信息");
			}
			String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle,"20");
			// 生成退费明细
			BlOpDt regurnDef = new BlOpDt();
			regurnDef.setFlagRecharge("0");//重新生成待收费记录写1，否则写0
			opcgPubHelperService.generateRefoundRecord(blOpDts, pkSettleCanc,regurnDef);// 传入新的结算主键
			// 根据结算主键查询结算明细
			List<BlSettleDetail> blSettleDetail = cgQryMaintainService.qryBlSettleDetailInfoByBlSettle(mapParam);
			// 生成退费结算明细
			
			opcgPubHelperService.generateRefoundSettleDetail(blSettleDetail,pkSettleCanc);

			// 根据结算主键查询交款记录信息
			List<BlDeposit> blDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(mapParam);
			// 生成退费的交款记录信息
			 negaBlDeposits = opcgPubHelperService.generateRefoundBlDeposits(blDeposits,pkSettleCanc);
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
				//获取BL0031（收费结算启用电子票据），参数值为1时调用冲红电子票据接口
			     //String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
				String eBillFlag = invSettltService.getBL0031ByNameMachine(regvo.getNameMachine());
				if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
					try{
						invSettltService.billCancel(pkSettle, user);
					}catch (Exception e) {
						throw new BusException("取消结算失败：" + e.getMessage());
					}
				}
				
				// 根据结算主键查询作废结算时对应的发票
				List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
				if(blInvoices!=null&&blInvoices.size()>0){
					for(BlInvoice inv:blInvoices){
						// 更新作废发票信息
						opcgPubHelperService.updateRefoundBlInvoice(inv);
					}
				}
			}
		}
		if(StringUtils.isNotBlank(regvo.getPkSch())){
			//更新可用号数
			DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?", new Object[] { regvo.getPkSch() });
			//还原号表
			DataBaseHelper.update("update sch_ticket set FLAG_USED ='0' where ticketno = ? and pk_sch = ?", new Object[] { regvo.getTicketNo(),regvo.getPkSch()});
		}

		Map<String,Object> updateMap = new HashMap<String,Object>();
		updateMap.put("flagCancel", "1");
		updateMap.put("euStatus", "9");
		updateMap.put("pkEmpCancel", u.getPkEmp());
		updateMap.put("nameEmpCancel", u.getNameEmp());
		updateMap.put("dateCancel", new Date());
		updateMap.put("ts", new Date());
		updateMap.put("pkPv", regvo.getPkPv());
		updateMap.put("reasonCancel", regvo.getReasonCancel());
		//如果为预约挂号，更新预约就诊记录
		if(CommonUtils.isNotNull(regvo.getPkAppt())){
			updateMap.put("pkSchappt", regvo.getPkAppt());
			regSyxMapper.updateSchAppt(updateMap);
		}
		//更新就诊记录
		regSyxMapper.updatePvEncounter(updateMap);
		//发送退号信息到平台
		Map<String,Object>  paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		paramMap.put("nameEmp", ((User)user).getNameEmp());
		paramMap.put("codeEmp", ((User)user).getCodeEmp());
		PlatFormSendUtils.sendPvOpCancelRegMsg(paramMap);
		paramMap = null;
		return negaBlDeposits;
	}
	
	/**
	 * 交易号：007002001004
	 * 依据配置加入退号判断逻辑，没有配置的，没任何影响
	 * 已就诊的记录禁止退号，读取参数PV0043（门急诊业务根据医嘱数量限制能否退号），参数值为0时就诊状态=0时可以退号，参数值为1时只要没有开立医嘱即可退号；
	 * @param param
	 * @param user
	 * @return
	 */
	public void chkCanlRegInfo(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String specFlag = JsonUtil.getFieldValue(param, "specFlag");
		
		if(CommonUtils.isEmptyString(pkPv)){
			throw new BusException("退号校验，必须传入就诊记录主键！");
		}
		if("1".equals(specFlag))
		{
			Map<String, Object> mapPv = DataBaseHelper.queryForMap("select name_emp_phy,date_clinic from pv_encounter where pk_pv=? and eu_status in ('1','2','3')", new Object[]{pkPv});
			if(mapPv != null)
			{
				String nameEmpPhy = MapUtils.getString(mapPv,"nameEmpPhy");
				Date dateClinic = (Date)mapPv.get("dateClinic");
	        	String strDateClinic = DateUtils.formatDate(dateClinic, "yyyy-MM-dd HH:mm");
				throw new BusException("该病人于" + strDateClinic + "," + nameEmpPhy + "医生已接诊，请与医生沟通。如需强制退号，请录入相关原因!");
			}
			validCancelRegByConf(pkPv);
		}
		else if("2".equals(specFlag))
		{
			Map<String, Object> mapPv = DataBaseHelper.queryForMap("select eu_status from pv_encounter where pk_pv=? ", new Object[]{pkPv});
			if(mapPv != null)
			{
				String euStatus = MapUtils.getString(mapPv,"euStatus");
				if("9".equals(euStatus))
				{
					throw new BusException("该病人已是退号状态，无需再次退号!");
				}
				else if("0".equals(euStatus))
				{
					throw new BusException("该病人是登记状态，请点退号按钮退号!");
				}
			}
			else
			{
				throw new BusException("未找到该病人此次就诊记录!");
			}
		}
		else
		{
			validCancelRegByConf(pkPv);
		}
	}
	
	/**
	 *  依据配置加入退号判断逻辑，没有配置的，没任何影响
	 * 已就诊的记录禁止退号，读取参数PV0043（门急诊业务根据医嘱数量限制能否退号），参数值为0时就诊状态=0时可以退号，参数值为1时只要没有开立医嘱即可退号；
	 * @param pkPv
	 */
	public void validCancelRegByConf(String pkPv){
		String opConfig = ApplicationUtils.getSysparam("PV0043", false);
		if(StringUtils.isNotBlank(opConfig)) {
			if(StringUtils.isBlank(pkPv)){
				throw new BusException("退号校验，必须传入就诊记录主键！");
			}
			Map<String, Object> mapPv = DataBaseHelper.queryForMap("select EU_STATUS from PV_ENCOUNTER where PK_PV=?", new Object[]{pkPv});
			if(mapPv != null && EnumerateParameter.ZERO.equals(opConfig)
					&& !EnumerateParameter.ZERO.equals(MapUtils.getString(mapPv,"euStatus"))){
				throw new BusException("依据系统参数[PV0043]的配置，当前患者状态不允许退号！状态为" + MapUtils.getString(mapPv,"euStatus"));
			}
			
			
			Integer cnCount = DataBaseHelper.queryForScalar(
					"Select Count(1) From bl_settle st Where st.pk_pv = ?  And st.dt_sttype = '01'"
							+ "And Not Exists(Select 1 From bl_settle stcan Where st.pk_settle = stcan.pk_settle_canc)",
					Integer.class, new Object[]{pkPv});
			if (EnumerateParameter.ONE.equals(opConfig) && cnCount > 0) {
				throw new BusException("该患者存在未退费的结算记录，必须先退费才能退号!");
			}
		}

	}
}
