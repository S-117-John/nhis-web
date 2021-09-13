package com.zebone.nhis.pro.sd.bl.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.pro.sd.bl.dao.SelfPayTrInsuMapper;
import com.zebone.nhis.pro.sd.bl.vo.StDepoInfoVo;
import com.zebone.nhis.bl.pub.dao.OpcgQryWrapMapper;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.service.CgStrategyPubService;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.bl.pub.vo.OprbFeesDto;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 自费转医保后台服务
 * @author Administrator
 *
 */
@Service
public class SelfPayTrInsuService {
	
	@Resource
	private SelfPayTrInsuMapper selfPayTrInsuMapper;
	
	@Resource
	private CgStrategyPubService cgStrategyPubService;
	
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	
	@Resource
	private CgQryMaintainService cgQryMaintainService;
	
	@Resource
	private OpcgPubHelperService opcgPubHelperService;
	
	@Resource
	private BalAccoutService balAccoutService;
	
	@Resource
	private InvSettltService invSettltService;
	
	@Autowired
	private PdStOutPubService pdStOutPubService; // 库存预留量
	
	/**
	 * 交易号：007002003040
	 * 查询患者门诊挂号结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOpRegStInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		
		List<Map<String,Object>> retList = selfPayTrInsuMapper.qryOpRegStInfo(paramMap);
		
		return retList;
	}
	
	/**
	 * 交易号：007002003041
	 * 查询结算收费明细信息
	 * @param param
	 * @param user
	 * @return
	 */
	public StDepoInfoVo qryStChangeItem(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		StDepoInfoVo retVo = new StDepoInfoVo();
		
		String pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		
		//查询本次挂号结算信息
		BlSettle stInfo = DataBaseHelper.queryForBean(
				"select * from bl_settle where pk_settle= ?",
				BlSettle.class, new Object[]{pkSettle});
		
		//查询本次挂号结算下的收费明细
		List<BlOpDt> opList = DataBaseHelper.queryForList(
				"select * from bl_op_dt where pk_settle = ?",
				BlOpDt.class,new Object[]{pkSettle});
		
		//查询本次结算下的收款明细
		List<BlDeposit> depoList = DataBaseHelper.queryForList(
				"select * from bl_deposit where pk_settle = ?",
				BlDeposit.class,new Object[]{pkSettle});
		
		//转换记费明细Vo
		List<ItemPriceVo> stOpList = getPriceList(opList);
		
		retVo.setStOpList(stOpList);
		retVo.setStDepoList(depoList);
		
		
		if(paramMap.containsKey("pkHp") && paramMap.get("pkHp")!=null && stOpList!=null && stOpList.size()>0){
			Map<String,Object> hpParam = new HashMap<String,Object>();
			hpParam.put("pkHp",paramMap.get("pkHp"));
			hpParam.put("euPvType", "1");
			List<HpVo> hpList = cgStrategyPubMapper.queryHpList(hpParam);
			if(hpList==null||hpList.size()<=0)
				throw new BusException("所选医保类型【"+CommonUtils.getString(paramMap.get("nameInsu"))+"】不存在或已删除！");
			BdHp hp = new BdHp();
			ApplicationUtils.copyProperties(hp, hpList.get(0));
			
			PvEncounter pv = DataBaseHelper.queryForBean(
					"select * from pv_encounter where pk_pv = ?",
					PvEncounter.class, new Object[]{stInfo.getPkPv()});
			
			hpParam.put("pkPicate", pv.getPkPicate());
			hpParam.put("pkHp", null);
			List<HpVo> cateList = cgStrategyPubMapper.queryHpList(hpParam);//患者分类对应医保
			
			//调用医保记费策略
			List<ItemPriceVo> list = cgStrategyPubService.getItemPriceByCgDiv(hp, pv, stOpList);
			//调用优惠记费策略
			if(cateList!=null&&cateList.size()>0)
				list = cgStrategyPubService.getItemDiscRatioByCgDiv(cateList.get(0), pv, list);
			
			retVo.setPriceList(list);
		}
		
		return retVo;
	}
	
	/**
	 * 交易号：007002003044
	 * 根据结算主键复制本次结算下的记费明细信息到bl_op_dt表，flag_settle='0',pk_settle=null,flag_recharge='1'
	 * @param param
	 * @param user
	 */
	public void copyOpStOrder(String param,IUser user){
		List<String> pkList = JsonUtil.readValue(param,
				new TypeReference<List<String>>() {
				});
		
		if(pkList==null || pkList.size()<=0){
			throw new BusException("未传入结算信息，无法复制医嘱！");
		}
		String codeCg = ApplicationUtils.getCode("0601");
		int sortNo = 0;
		List<BlOpDt> dtList = selfPayTrInsuMapper.qryDtListByPkSettle(pkList);
		if(dtList!=null && dtList.size()>0){
			for(BlOpDt dtVo : dtList){
				dtVo.setFlagRecharge("1");
				dtVo.setFlagSettle("0");
				dtVo.setPkSettle(null);
				dtVo.setPkInvoice(null);
				sortNo++;
				dtVo.setCodeCg(codeCg);
				dtVo.setSortno(sortNo);
				ApplicationUtils.setDefaultValue(dtVo, true);
			}
			
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), dtList);
		}
	}
	
	/**
	 * 交易号：007002003045
	 * 根据pk_cnord修改修改执行记录关联的pkSettle信息
	 * @param param
	 * @param user
	 */
	public void upExOccStInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0 
				|| !paramMap.containsKey("pkSettle") || CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			throw new BusException("未传入结算信息,修改执行单记录失败！");
		}
		
		//查询医嘱处方执行信息
		Set<String> presList = selfPayTrInsuMapper.qryPresOccByPkSettle(paramMap);
		if(presList!=null && presList.size()>0){
			//更新pkSettle信息
			DataBaseHelper.execute("update ex_pres_occ set pk_settle = ? where pk_presocc in ("+CommonUtils.convertSetToSqlInPart(presList, "pk_presocc")+")", new Object[]{paramMap.get("pkSettle")});
		}
		
		//查询医技执行信息
		Set<String> assistList = selfPayTrInsuMapper.qryAssistOccByPkSettle(paramMap);
		if(assistList!=null && assistList.size()>0){
			//更新pkSettle信息
			DataBaseHelper.execute("update ex_assist_occ set pk_settle = ? where pk_assocc in ("+CommonUtils.convertSetToSqlInPart(assistList, "pk_assocc")+")", new Object[]{paramMap.get("pkSettle")});
		}
	}
	
	/**
	 * 交易号：007002003046
	 * 根据pkSettle查询此次结算下是否有执行单信息
	 * @param param
	 * @param user
	 */
	public void qryExOccByPkSettle(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0 
				|| !paramMap.containsKey("pkSettle") || CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			throw new BusException("未传入结算信息,修改执行单记录失败！");
		}
		
		//查询医嘱处方执行信息
		Integer presCnt = DataBaseHelper.queryForScalar(
				"select count(1) from ex_pres_occ where pk_settle = ?",
				Integer.class, new Object[]{paramMap.get("pkSettle")});
		if(presCnt!=null && presCnt>0){
			throw new BusException("此次结算下有对应的执行单记录，无法退费，请联系管理员！");
		}
		
		//查询医技执行信息
		Integer assistCnt = DataBaseHelper.queryForScalar(
				"select count(1) from EX_ASSIST_OCC where PK_SETTLE = ?",
				Integer.class, new Object[]{paramMap.get("pkSettle")});
		if(assistCnt!=null && assistCnt>0){
			throw new BusException("此次结算下有对应的执行单记录，无法退费，请联系管理员！");
		}
		
	}
	
	/**
	 * 交易号：007002003047
	 * 门诊退费(深大自费转医保使用，只支持自费结算退费)
	 * @param param
	 * @param user
	 */
	public String opStReBack(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0 
				|| !paramMap.containsKey("pkSettle") || CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			throw new BusException("未传入结算信息,退费失败！");
		}
		List<Map<String,Object>> newBlDeposits = CommonUtils.isNotNull(paramMap.get("blDeposits")) ? ((List<Map<String,Object>>)paramMap.get("blDeposits")) : null;
		
		Map<String,Object> mapParam = new HashMap<>();
		// 根据结算主键查询结算信息
		mapParam.put("pkSettle", paramMap.get("pkSettle"));
		BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
		// 生成退费结算信息
		if(blSettle == null) {
			throw new BusException("没有查询到患者的结算信息");
		}
		String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle,"21");
		// 生成退费明细
		List<BlOpDt> blOpDts = DataBaseHelper.queryForList(
				"select * from bl_op_dt where pk_settle = ?",
				BlOpDt.class,new Object[]{paramMap.get("pkSettle")});
		
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
		List<BlDeposit> negaBlDeposits = createNegaBlDeposit(UserContext.getUser(),blDeposits,pkSettleCanc,newBlDeposits);
		//加了非空判断
		if(negaBlDeposits != null){
			for (BlDeposit negaBlDeposit : negaBlDeposits) {
				if (negaBlDeposit.getDtPaymode().equals(IDictCodeConst.PATIACCOUNT)) {
					// 更新患者账户，调用患者账户消费服务
					BlDepositPi blDepositPi = new BlDepositPi();
					ApplicationUtils.setDefaultValue(blDepositPi, true);
					blDepositPi.setEuDirect(EnumerateParameter.ONE);
					blDepositPi.setPkPi(blSettle.getPkPi());
					blDepositPi.setAmount(negaBlDeposit.getAmount().abs());
					blDepositPi.setDtPaymode(EnumerateParameter.FOUR);
					blDepositPi.setPkEmpPay(UserContext.getUser().getPkEmp());
					blDepositPi.setNameEmpPay(UserContext.getUser().getNameEmp());
					PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(blSettle.getPkPi());// 患者账户信息
					ApplicationUtils.setDefaultValue(piAcc, false);
					piAcc.setAmtAcc((piAcc.getAmtAcc() == null ? BigDecimal.ZERO : piAcc.getAmtAcc()).add(blDepositPi.getAmount()));
					balAccoutService.piAccDetailVal(piAcc, blDepositPi, blSettle.getPkPv(), null);
				}
			}
		}

		String BL0008_code = ApplicationUtils.getSysparam("BL0008", false);
		// 如果挂号时打印的发票，要作废发票
		if (EnumerateParameter.ONE.equals(BL0008_code)) {
			// 根据结算主键查询作废结算时对应的发票
			List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
			if(blInvoices!=null&&blInvoices.size()>0){
				for(BlInvoice inv:blInvoices){
					// 更新作废发票信息
					opcgPubHelperService.updateRefoundBlInvoice(inv);
				}
			}
		}
		
		//获取BL0031（收费结算启用电子票据），参数值为1时调用冲红电子票据接口
	    // String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
		String eBillFlag = invSettltService.getBL0031ByNameMachine(CommonUtils.getString(paramMap.get("nameMachine")));
		if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
			try{
				invSettltService.billCancelNoNewTrans(blSettle.getPkSettle(), user);
			}catch (Exception e) {
				throw new BusException("退号失败：" + e.getMessage());
			}
		}
		
		
		//查询是否有关联的执行单信息，如果有则更新执行单信息
		List<String> pkStList = new ArrayList<>();
		
		Integer presCnt = selfPayTrInsuMapper.qryPresOccCnt(paramMap);
		
		//查询医技执行信息
		Integer assistCnt = selfPayTrInsuMapper.qryAssOccCnt(paramMap);

		if((presCnt!=null && presCnt.intValue()>0) || (assistCnt!=null && assistCnt.intValue()>0)){
			//查询医嘱处方执行信息
			List<String> presList = selfPayTrInsuMapper.qryPresOccPkSettle(paramMap);
			if(presList!=null && presList.size()>0){
				//过滤本次结算的信息
				for(int i=presList.size()-1; i>=0; i--){
					if(!presList.get(i).equals(CommonUtils.getString(paramMap.get("pkSettle")))){
						pkStList.add(presList.get(i));
					}
				}
			}
		}
		
		if((presCnt!=null && presCnt.intValue()>0) || (assistCnt!=null && assistCnt.intValue()>0)){
			//查询医技执行信息
			List<String> assList = selfPayTrInsuMapper.qryAssOccPkSettle(mapParam);
			if(assList!=null && assList.size()>0){
				//过滤本次结算的信息
				for(int i=assList.size()-1; i>=0; i--){
					if(!assList.get(i).equals(CommonUtils.getString(paramMap.get("pkSettle")))){
						pkStList.add(assList.get(i));
					}
				}
			}
		}
		
		//查询出重新结算的费用信息
		if(pkStList!=null && pkStList.size()>0){
			//理论只会查出一条结算信息，以防万一使用集合处理
			List<BlSettle> stList = selfPayTrInsuMapper.qryStInfoByPkSettle(pkStList);
			BlSettle stInfo = stList.get(0);
			
			//重新结算的费用集合
			List<BlOpDt> newDtList = DataBaseHelper.queryForList(
					"select * from bl_op_dt where pk_settle = ?",
					BlOpDt.class,new Object[]{stInfo.getPkSettle()});
			
			if(newDtList!=null && newDtList.size()>0){
				//查询原始结算冲负费用集合
				List<BlOpDt> oldDtList = DataBaseHelper.queryForList(
						"select * from bl_op_dt where pk_settle = ?",
						BlOpDt.class,new Object[]{pkSettleCanc});
				
				// 更新处方执行明细,需要重新结算的不更新
				updateExPresOccDt(oldDtList,newDtList);
				// 更新医技执行单 ，需要重新结算的不更新
				updateExAssistOcc(oldDtList,newDtList);
				// 更新检查申请单，需要重新结算的不更新
				updateCnRisApply(oldDtList,newDtList);
				// 更新检验申请单，需要重新结算的不更新
				updateCnLabApply(oldDtList,newDtList);
				// 解除全部退费的处方中未发药的药品对应的预留量，需要重新结算的不解除
				relieveReservedAmount(oldDtList,newDtList);
				//更新处方执行单状态为取消--yangxue增加，需要重新结算的处方不更新
				updateExPresOcc(oldDtList,newDtList,(User)user);
			}
		}
		return pkSettleCanc;	
	}
	
	
	
	private List<BlDeposit> createNegaBlDeposit(User user,List<BlDeposit> blDeposits,String pkSettleCanc,List<Map<String,Object>> newBlDeposits) {
		if (CollectionUtils.isEmpty(blDeposits)){
			return null;
		}
		
		// 用传递的结算主键查询上次交款信息
		for (BlDeposit blDeposit : blDeposits) {
			//是否按现金退
			if(newBlDeposits != null && newBlDeposits.size() > 0) {
				for (Map<String,Object> newBlDeposit : newBlDeposits) {
					String pkDepo = CommonUtils.getPropValueStr(newBlDeposit, "pkDepo");
					String dtPaymode = CommonUtils.getPropValueStr(newBlDeposit, "dtPaymode");
					if(blDeposit.getPkDepo().equals(pkDepo)) {
						if(CommonUtils.isNotNull(dtPaymode)) {
							blDeposit.setDtPaymode(dtPaymode);
						}
					}
				}
			}
			blDeposit.setEuDptype("4");// 收付款类型:取消结算
			blDeposit.setEuDirect("-1");
			blDeposit.setAmount(new BigDecimal(-1).multiply(blDeposit.getAmount()));
			blDeposit.setDatePay(new Date());
			blDeposit.setPkDept(user.getPkDept());
			blDeposit.setPkEmpPay(user.getPkEmp());
			blDeposit.setNameEmpPay(user.getNameEmp());
			// 结算主键要放的是退款的结算主键
			blDeposit.setPkSettle(pkSettleCanc);
			blDeposit.setPkDepoBack(blDeposit.getPkDepo());
			blDeposit.setPkCc(null);
			blDeposit.setFlagCc(EnumerateParameter.ZERO);
			ApplicationUtils.setDefaultValue(blDeposit, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), blDeposits);
		
		return blDeposits;
	}
	
	
	/**
	 * 转换记费明细Vo
	 * @param opList
	 * @return
	 */
	private List<ItemPriceVo> getPriceList(List<BlOpDt> opList){
		List<ItemPriceVo> list = new ArrayList<>();
		
		if(opList!=null && opList.size()>0){
			for(BlOpDt opVo : opList){
				ItemPriceVo itemvo = new ItemPriceVo();
				itemvo.setPkItem(opVo.getPkItem());
				itemvo.setPkItemcate(opVo.getPkItemcate());
				itemvo.setNameCg(opVo.getNameCg());
				itemvo.setFlagPd("0");
				itemvo.setFlagPv(opVo.getPkPv());
				itemvo.setPackSize(opVo.getPackSize());
				itemvo.setPkCnord(opVo.getPkCnord());
				itemvo.setDateCg(opVo.getDateCg());
				
				itemvo.setPkEmpApp(opVo.getPkEmpApp());
				itemvo.setNameEmpApp(opVo.getNameEmpApp());
				itemvo.setPkDeptEx(opVo.getPkDeptEx());
				itemvo.setPkDeptCg(opVo.getPkDeptCg());
				itemvo.setPkEmpCg(opVo.getPkEmpCg());
				itemvo.setNameEmpCg(opVo.getNameEmpCg());
				
				itemvo.setPkDeptApp(opVo.getPkDeptApp());
				itemvo.setPkItemOld(opVo.getPkItem());
				itemvo.setPkOrgApp(opVo.getPkOrgApp());
				itemvo.setPkOrgEx(opVo.getPkOrgEx());
				itemvo.setPkPi(opVo.getPkPi());
				itemvo.setPkPres(opVo.getPkPres());
				itemvo.setPkPv(opVo.getPkPv());
				itemvo.setPkUnitPd(opVo.getPkUnitPd());
				itemvo.setPrice(opVo.getPrice());
				itemvo.setPriceOrg(opVo.getPrice());
				itemvo.setPriceCs(opVo.getPrice());
				itemvo.setQuanOld(opVo.getQuan());
				itemvo.setQuan(opVo.getQuan());
				itemvo.setSpec(opVo.getSpec());
				itemvo.setPkEmpEx(opVo.getPkEmpEx());
				itemvo.setNameEmpEx(opVo.getNameEmpEx());
				
				list.add(itemvo);
			}
		}
		
		return list;
	}
	
	/**
	 * 交易号：007002003050
	 * 清空复制医嘱信息
	 * @param param
	 * @param user
	 */
	public void delCopyOrdInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0 
				|| !paramMap.containsKey("pkPv") || CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkPv")))){
			throw new BusException("未传入患者就诊信息pkPv,删除失败！");
		}
		
		DataBaseHelper.execute(
				"delete from bl_op_dt where pk_pv = ? and flag_settle = '0' and flag_recharge = '1'",
				new Object[]{paramMap.get("pkPv")});
	}
	
	/**
	 * 交易号：007002003052
	 * 保存医嘱医保类型
	 * @param param
	 * @param user
	 */
	public void saveOrdHppropInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap!=null || paramMap.size()>0){
			String hpprop = null;
			if(paramMap.containsKey("dtHpprop")){
				hpprop = CommonUtils.getString(paramMap.get("dtHpprop"));
			}

			List<String> pkCnordList = JsonUtil.readValue(JsonUtil.getJsonNode(param, "pkCnordList"),
					new TypeReference<List<String>>() {
					});

			Set<String> pkList = null;
			if(pkCnordList!=null && pkCnordList.size()>0){
				pkList = new HashSet<>(pkCnordList);
			}

			if(!CommonUtils.isEmptyString(hpprop) && pkList!=null && pkList.size()>0){
				DataBaseHelper.execute("update cn_order set dt_hpprop = ? where pk_pv = ? and (pk_cnord in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_cnord")+" ) or pk_pres in (select pk_pres from CN_PRESCRIPTION where pres_no in ("+CommonUtils.convertSetToSqlInPart(pkList, "pres_no")+"))) ", new Object[]{hpprop,paramMap.get("pkPv")});
				
				DataBaseHelper.execute("update CN_PRESCRIPTION set dt_hpprop = ? where pres_no in ("+CommonUtils.convertSetToSqlInPart(pkList, "pres_no")+")", new Object[]{hpprop});
			}
		}
	}
	
	/**
	 * 交易号：007002003053
	 * 查询全自费医保信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qrySelfPayInsu(String param,IUser user){
		List<Map<String,Object>> hpInfoList = DataBaseHelper.queryForList(
				"select * from bd_hp where del_flag = '0' and EU_HPTYPE = '0'",
				new Object[]{});
		
		return hpInfoList;
	}
	
	/**
	 * 交易号：007002003054
	 * 修改患者就诊医保计划
	 * @param param
	 * @param user
	 */
	public void upPvInsuInfo(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String pkInsu = JsonUtil.getFieldValue(param, "pkInsu");
		
		//pkInsu为空时改为全自费
		if(CommonUtils.isEmptyString(pkInsu)){
			List<BdHp> hpInfoList = DataBaseHelper.queryForList(
					"select * from bd_hp where del_flag = '0' and EU_HPTYPE = '0'",
					BdHp.class,new Object[]{});
			
			pkInsu = hpInfoList.get(0).getPkHp();
		}
		
		DataBaseHelper.execute(
				"update PV_ENCOUNTER set pk_insu = ? where pk_pv = ?",
				new Object[]{pkInsu,pkPv});
	}
	
	/**
	 * 交易号：007002003055
	 * 复制微信160医嘱信息
	 * @param param
	 * @param user
	 */
	public void copyWeChatDtInfo(String param,IUser user){
		List<BlOpDt> dtList = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "opdtList"),
				new TypeReference<List<BlOpDt>>() {
				});
		if(dtList!=null && dtList.size()>0){
			// 1 查询出本次结算的所有明细
			List<BlOpDt> blOpdtAlls = DataBaseHelper.queryForList("select * from bl_op_dt where pk_settle=?", 
												BlOpDt.class, dtList.get(0).getPkSettle());
			
			List<BlOpDt> blOpDtNews = new ArrayList<>();
			String codeCg = ApplicationUtils.getCode("0601");
			int sortNo = 0;
			for (BlOpDt blOpDt : dtList) {
				//差异数量
				double quanDiff = MathUtils.sub(blOpDt.getQuanCg(), blOpDt.getQuanBack());
				if(quanDiff==0){
					quanDiff = blOpDt.getQuanCg();
				}
				BlOpDt blOpDtNew = new BlOpDt();
				//根据原始记费记录，计算患者自付金额
				for(BlOpDt oriOpDt : blOpdtAlls){
					if(oriOpDt.getPkCgop().equals(blOpDt.getPkCgop())){
						ApplicationUtils.copyProperties(blOpDtNew, oriOpDt);
						if(quanDiff!=blOpDt.getQuanCg()){
							blOpDtNew.setQuan(quanDiff);
							//自付金额=单个数量的自付金额*差异数量
							blOpDtNew.setAmountPi(MathUtils.mul(MathUtils.div(oriOpDt.getAmountPi(), oriOpDt.getQuan()), quanDiff));
							blOpDtNew.setAmount(MathUtils.mul(MathUtils.div(oriOpDt.getAmount(), oriOpDt.getQuan()),quanDiff));
							blOpDtNew.setAmountHppi(MathUtils.mul(MathUtils.div(oriOpDt.getAmountHppi(), oriOpDt.getQuan()),quanDiff));
						}
						blOpDtNew.setFlagSettle(EnumerateParameter.ZERO);
						blOpDtNew.setPkSettle(null);
						blOpDtNew.setFlagRecharge("1");
						blOpDtNew.setFlagSettle("0");
						blOpDtNew.setPkSettle(null);
						blOpDtNew.setPkInvoice(null);
						blOpDtNew.setPkCgopOld(oriOpDt.getPkCgop());
						blOpDtNew.setDateCg(new Date());
						
						sortNo++;
						blOpDtNew.setCodeCg(codeCg);
						blOpDtNew.setSortno(sortNo);
						ApplicationUtils.setDefaultValue(blOpDtNew, true);
						blOpDtNews.add(blOpDtNew);
						break;
					}
				}
			}
			
			if(blOpDtNews!=null && blOpDtNews.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blOpDtNews);
			}
			
		}
		
	}
	
	/**
	 * 更新处方执行记录
	 * @param blOpDtNegaAll
	 */
	private void updateExPresOcc(List<BlOpDt> blOpDtNegaAll,List<BlOpDt> blOpDtLeft,User u){
		List<String> presList = new ArrayList<String>();
//		for (BlOpDt blOpDtNega : blOpDtNegaAll) {
//			presList.add(blOpDtNega.getPkPres());
//		}
		
		for (BlOpDt blOpDtNega : blOpDtNegaAll) {
			boolean has = false;
			if(blOpDtLeft!=null&&blOpDtLeft.size()>0){
				for(BlOpDt leftdt:blOpDtLeft){
					if("1".equals(blOpDtNega.getFlagPd()) && !CommonUtils.isEmptyString(blOpDtNega.getPkPres())&&blOpDtNega.getPkPres().equals(leftdt.getPkPres())){
						has = true;
						break;
					}
				}
			}
			
			if(!has){
				presList.add(blOpDtNega.getPkPres());
			}
		}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(presList!=null && presList.size()>0){
			paramMap.put("pkPress", presList);
			paramMap.put("pkEmp", u.getPkEmp());
			paramMap.put("nameEmp", u.getNameEmp());
			paramMap.put("dateNow", new Date());
			DataBaseHelper.update("update ex_pres_occ  set ex_pres_occ.eu_status = '9',ex_pres_occ.flag_canc='1',ex_pres_occ.pk_emp_canc=:pkEmp,ex_pres_occ.name_emp_canc=:nameEmp,ex_pres_occ.date_canc=:dateNow where  "
					+ " exists( select 1 from ex_pres_occ_dt dt where dt.pk_presocc = ex_pres_occ.pk_presocc and dt.quan_cg=dt.quan_ret) and ex_pres_occ.flag_conf='0' and ex_pres_occ.pk_pres in (:pkPress) ", paramMap);
		}
		presList = null;
		paramMap = null;
	}
	
	/**
	 * 解除全部退费的处方中未发药的药品对应的预留量
	 * @param blOpDtNegaAll
	 */
	private void relieveReservedAmount(List<BlOpDt> blOpDtNegaAll,List<BlOpDt> blOpDtLeft) {
		Set<String> pkPress = new HashSet<>();
		for(BlOpDt blOpDtNega : blOpDtNegaAll){
			if(!CommonUtils.isEmptyString(blOpDtNega.getPkPres())){
				pkPress.add(blOpDtNega.getPkPres());
			}
		}
		if(pkPress != null && pkPress.size() > 0){
			String pdsql = "select dt.pk_cnord,dt.pk_pd,dt.pk_unit,dt.quan_cg*dt.pack_size as quan_min,dt.quan_cg,dt.pack_size,occ.pk_dept_ex "+
						  " from ex_pres_occ_dt dt inner join ex_pres_occ occ on dt.pk_presocc=occ.pk_presocc "+
				      	  " where occ.flag_conf='0' and occ.pk_pres in (" 
				      	  			+ CommonUtils.convertSetToSqlInPart(pkPress, "pk_pres") + ")";
		   List<Map<String,Object>> pdlist = DataBaseHelper.queryForList(pdsql, new Object[]{});
		   
		   if(CollectionUtils.isEmpty(pdlist)){
			   return;
		   }
		   //需要重新结算的部分不解除预留
		   if(blOpDtLeft!=null&&blOpDtLeft.size()>0){
			 //复制一份新的需要重新结算的收费明细用来操作
				List<BlOpDt> tempLeftDt = new ArrayList<BlOpDt>();
				for(BlOpDt opdt:blOpDtLeft){
					try {
						tempLeftDt.add((BlOpDt)opdt.clone());
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
			   Iterator<Map<String,Object>> iterPd = pdlist.iterator();
		        while (iterPd.hasNext()) {
		        	Map<String,Object> occPdMap = iterPd.next();
		        	 String pkCnord = CommonUtils.getString(occPdMap.get("pkCnord"));
					 String pkPd = CommonUtils.getString(occPdMap.get("pkPd"));
					//避免破坏原来集合内容，使用新复制的集合进行操作
		        	Iterator<BlOpDt> iterdt = tempLeftDt.iterator();
		        	boolean flagRemove = false;
			        while (iterdt.hasNext()) {
			        	BlOpDt dtleft = iterdt.next();
			        	if(CommonUtils.isNotNull(pkCnord)&&pkCnord.equals(dtleft.getPkCnord())&&pkPd.equals(dtleft.getPkPd())){
			        		Double quanCg = CommonUtils.getDouble(occPdMap.get("quanCg"));
							Double packSize = CommonUtils.getDouble(occPdMap.get("packSize"));
							//需要重新记费的数量大于执行数量，则不解除预留
							if(MathUtils.compareTo(dtleft.getQuan(),quanCg)>=0){
								flagRemove = true;
							}else{
								occPdMap.put("quanCg", MathUtils.sub(quanCg, dtleft.getQuan()));
								occPdMap.put("quanMin", MathUtils.mul(MathUtils.sub(quanCg, dtleft.getQuan()), packSize));
							}
			        		iterdt.remove();
			        	}
			        }
			        //是否不解除预留
			        if(flagRemove){
			        	iterPd.remove();
			        }
		        }
		   }
		   //再次判断是否还存在可解除预留的药品列表
		   if(CollectionUtils.isEmpty(pdlist)){
			   return;
		   }
		   //根据退费明细的执行科室合并物品，统一进行预留量处理
		   refundPdSetPdNum(pdlist);
		}
	}
	
	/**
	 * 生成解除预留参数
	 * @return PdOutParamVo
	 */
    private PdOutParamVo convertToParam(Map<String,Object> pdmap){
		PdOutParamVo pdOutParam = new PdOutParamVo();
		pdOutParam.setPackSize(CommonUtils.getInteger(pdmap.get("packSize")));
		pdOutParam.setPkPd(CommonUtils.getString(pdmap.get("pkPd")));
		pdOutParam.setPkUnitPack(CommonUtils.getString(pdmap.get("pkUnit")));
		pdOutParam.setQuanMin(CommonUtils.getDouble(pdmap.get("quanMin")));
		pdOutParam.setQuanPack(CommonUtils.getDouble(pdmap.get("quanCg")));
    	return pdOutParam;
    }
	
	/**
	 * 根据退费明细的执行科室合并物品，统一进行预留量处理
	 * @param pdlist
	 */
	private void refundPdSetPdNum(List<Map<String, Object>> pdlist)
	{
		Map<String, List<PdOutParamVo>> pkDeptExParams = new HashMap<>();
		   // 分科室放置未发药的收费明细
			all: for (Map<String, Object> pdmap : pdlist) {
				   String pkDeptEx = CommonUtils.getString(pdmap.get("pkDeptEx")); //当前收费明细的执行科室
				   if(!pkDeptExParams.containsKey(pkDeptEx)){
					   List<PdOutParamVo> pdDeptExList = new ArrayList<>(); 
					   pdDeptExList.add(convertToParam(pdmap));
					   pkDeptExParams.put(pkDeptEx, pdDeptExList);
			   }else{
				   	//相同科室要合并物品
					List<PdOutParamVo> pdVolist = pkDeptExParams.get(pkDeptEx);
					boolean flag = true;
					int size = pdVolist.size();
					for(int i = 0; i < size; i++){
							if(pdVolist.get(i).getPkPd().equals(pdmap.get("pkPd"))){
								double quanMin = pdVolist.get(i).getQuanMin() + CommonUtils.getDouble(pdmap.get("quanMin"));
								double quanCg =  pdVolist.get(i).getQuanPack() + CommonUtils.getDouble(pdmap.get("quanCg"));
								pdVolist.get(i).setQuanMin(quanMin);
								pdVolist.get(i).setQuanPack(quanCg);
								break all;
							}
						}
					pdVolist.add(convertToParam(pdmap));
			   }
		   }
		   
		   Set<Entry<String, List<PdOutParamVo>>> pkStoreEntrys = pkDeptExParams.entrySet();
			for (Entry<String, List<PdOutParamVo>> pkStoreEntry : pkStoreEntrys) {
				String pkStore = pkStoreEntry.getKey();
				//根据pk_store取对应的List<PdOutParamVo>作为一个批次进行库存量处理 
				pdStOutPubService.setPdUnPrepNum(pkDeptExParams.get(pkStore), null, pkStore,"1");
			}
	}
	
	/**
	 * 更新检查申请单
	 * @param blOpDtNegaAll
	 */
	private void updateCnRisApply(List<BlOpDt> blOpDtNegaAll,List<BlOpDt> blOpDtLeft) 
	{
		// 医嘱
		Set<String> pkCnords = new HashSet<>();
		for (BlOpDt blOpDtNega : blOpDtNegaAll) {
			if ("1".equals(blOpDtNega.getEuAdditem())) {
				continue;
			}
			boolean has = false;
			if(blOpDtLeft!=null&&blOpDtLeft.size()>0){
				for(BlOpDt leftdt:blOpDtLeft){
					if(CommonUtils.isNotNull(blOpDtNega.getPkCnord())&&blOpDtNega.getPkCnord().equals(leftdt.getPkCnord())){
						has = true;
						break;
					}
				}
			}
			if(!has){
				pkCnords.add(blOpDtNega.getPkCnord());
			}
		}
		List<CnRisApply> cnRisApplies = DataBaseHelper.queryForList(
						"select * from cn_ris_apply where eu_status='1' and pk_cnord in (" 
											+ CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")",
						CnRisApply.class, new Object[] {});
		for (CnRisApply cnRisApply : cnRisApplies) {
			cnRisApply.setEuStatus(EnumerateParameter.ZERO);
		}
		if(cnRisApplies.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnRisApply.class), cnRisApplies);
		}
	}
	
	/**
	 * 更新检验申请单
	 * @param blOpDtNegaAll
	 */
	private void updateCnLabApply(List<BlOpDt> blOpDtNegaAll,List<BlOpDt> blOpDtLeft) 
	{
		// 医嘱
		Set<String> pkCnords = new HashSet<>();
		for (BlOpDt blOpDtNega : blOpDtNegaAll) {
			if ("1".equals(blOpDtNega.getEuAdditem())) {
				continue;
			}
			boolean has = false;
			if(blOpDtLeft!=null&&blOpDtLeft.size()>0){
				for(BlOpDt leftdt:blOpDtLeft){
					if(CommonUtils.isNotNull(blOpDtNega.getPkCnord())&&blOpDtNega.getPkCnord().equals(leftdt.getPkCnord())){
						has = true;
						break;
					}
				}
			}
			if(!has){
				pkCnords.add(blOpDtNega.getPkCnord());
			}
		}
		List<CnLabApply> cnLabApplies = DataBaseHelper.queryForList(
					"select * from cn_lab_apply where eu_status='1' and pk_cnord in (" 
							+ CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")",
					CnLabApply.class, new Object[] {});
		for (CnLabApply cnLabApply : cnLabApplies) {
			cnLabApply.setEuStatus(EnumerateParameter.ZERO);
		}
		if(cnLabApplies.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnLabApply.class), cnLabApplies);
		}
		
	}
	
	/**
	 * 更新医疗执行单(只更新被退费的部分)
	 * @param blOpDts 全退收费明细列表
	 * @param leftblOpDts 未退收费明细列表
	 */
	public void updateExAssistOcc(List<BlOpDt> blOpDts,List<BlOpDt> leftblOpDts){
		Set<String> pkCnords = new HashSet<>();
		for (BlOpDt blOpDtNega : blOpDts) {
			if ("1".equals(blOpDtNega.getEuAdditem())) {
				continue;
			}
			boolean has = false;
			if(leftblOpDts!=null&&leftblOpDts.size()>0){
				for(BlOpDt leftdt:leftblOpDts){
					if(CommonUtils.isNotNull(blOpDtNega.getPkCnord())&&blOpDtNega.getPkCnord().equals(leftdt.getPkCnord())){
						has = true;
						break;
					}
				}
			}
			
			if(!has){
				pkCnords.add(blOpDtNega.getPkCnord());
			}
		}
		
		List<ExAssistOcc> assistOccs = DataBaseHelper.queryForList(
						"select * from ex_assist_occ where eu_status<>'1' and (flag_refund='0' or flag_refund is null) "
								+ " and pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")",
						ExAssistOcc.class, new Object[] {});
		
		// 如果医嘱关联的收费项目下有未退费的记录，不修改Ex_assist_occ的值。否则置为flag_refund已退。
		List<BlOpDt> bList = DataBaseHelper.queryForList("select * from bl_op_dt dt inner join BL_SETTLE st on dt.pk_settle = st.pk_settle "
															+ "	where pk_cnord in ("+ CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")"
															+ " and st.DT_STTYPE= '00'  and "
															+ " not exists (select 1 from bl_settle back where st.pk_settle=back.pk_settle_canc and st.pk_pi=back.pk_pi)",
										BlOpDt.class, new Object[]{});	
		
		if(CollectionUtils.isEmpty(bList)){
			for (ExAssistOcc assistOcc : assistOccs) {
				assistOcc.setFlagRefund(EnumerateParameter.ONE);
			}		
			if(assistOccs.size() > 0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExAssistOcc.class), assistOccs);
			}
		}
	}
	
	/**
	 * 更新处方执行明细
	 * @param blOpDtNegaAll
	 */
	private void updateExPresOccDt(List<BlOpDt> blOpDtNegaAll,List<BlOpDt> blOpDtLeft) {
		Set<String> pkCnords = new HashSet<String>();
		for (BlOpDt blOpDtNega : blOpDtNegaAll) {
			pkCnords.add(blOpDtNega.getPkCnord());
		}
		
		// 查询处方执行明细
		List<ExPresOccDt> exPresOccDts = DataBaseHelper.queryForList(
						"select * from ex_pres_occ_dt where pk_cnord in (" 
									+ CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")",
									ExPresOccDt.class, new Object[] {});
		if(exPresOccDts == null||exPresOccDts.size()<=0){
			return;
		}
		
		for (ExPresOccDt exPresOccDt : exPresOccDts) {
			for (BlOpDt blOpDtNega : blOpDtNegaAll) {
				if(exPresOccDt.getPkCnord().equals(blOpDtNega.getPkCnord())&&
						exPresOccDt.getPkPd().equals(blOpDtNega.getPkPd())){
					//未退数量
					double quanRemain = 0.0;
					if(blOpDtLeft!=null&&blOpDtLeft.size()>0){
						//避免破坏原集合内容，复制一份新的需要重新结算的收费明细用来操作
						List<BlOpDt> tempLeftDt = new ArrayList<BlOpDt>();
						for(BlOpDt opdt:blOpDtLeft){
							try {
								tempLeftDt.add((BlOpDt)opdt.clone());
							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}
						}
						Iterator<BlOpDt> iter = tempLeftDt.iterator();
				        while (iter.hasNext()) {
				        	BlOpDt dtleft = iter.next();
				        	if(blOpDtNega.getPkCnord().equals(dtleft.getPkCnord())&&blOpDtNega.getPkPd().equals(dtleft.getPkPd())){
								quanRemain = MathUtils.add(quanRemain,dtleft.getQuan());
								iter.remove();
				        	}
				        }
					}
					
					// 更新  累加quan_ret
					double quan_ret = MathUtils.sub(Math.abs(blOpDtNega.getQuan()), quanRemain);
					exPresOccDt.setQuanRet(MathUtils.add(exPresOccDt.getQuanRet(),quan_ret));
					if(!MathUtils.equ(quan_ret, 0.0)){
						exPresOccDt.setOrdsRet(1.0D); // 退费付数1
					}
					
					// 累加amount_ret，退费金额；
					BigDecimal old_amount_ret = exPresOccDt.getAmountRet();
					BigDecimal add_amount = new BigDecimal(MathUtils.mul(quan_ret,blOpDtNega.getPriceOrg()));					
					exPresOccDt.setAmountRet(old_amount_ret.add(add_amount));					
				}
			}
		}
		
		if(exPresOccDts.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExPresOccDt.class), exPresOccDts);
		}
	}
	
	/**
	 * 查询已经结算的费用申请单（处方,检治）
	 * 思路：通过查询机构和患者id查询所有的符合时间范围的处方和检查治疗，发票是作为过滤条件。
	 * 	  与文档思路有所不同，文档是先查出pkSettle，然后再根据每个pkSettle分别查询对应的处方和检查治疗。
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> querySettledFeesToReBack(String param, IUser user) {
		OprbFeesDto oprbFeesDto = JsonUtil.readValue(param, OprbFeesDto.class);
		List<Map<String, Object>>  settle = new ArrayList<Map<String, Object>>();
		Map<String, String> mapParam = new HashMap<>();
		mapParam.put("pkSettle",oprbFeesDto.getPkSettle());
		// 查询处方
		List<Map<String, Object>> settlePress =
					selfPayTrInsuMapper.querySettlePressRecord(mapParam);
		// 查询检查检验
		List<Map<String, Object>> settleAssists =
					selfPayTrInsuMapper.querySettleAssistRecord(mapParam);

		//查询医嘱附加费用信息
		List<Map<String, Object>> stEtceterasList =
					selfPayTrInsuMapper.qryEtceterasList(mapParam);

		if(settlePress != null && settlePress.size() > 0){
			settle.addAll(settlePress);
		}
		if(settleAssists != null && settleAssists.size() > 0){
			settle.addAll(settleAssists);
		}
		if(stEtceterasList!=null && stEtceterasList.size()>0){
			settle.addAll(stEtceterasList);
		}

		return settle;
	}

	/**
	 * 交易号：022004005007
	 * 强制退号
	 * @param param
	 * @param user
	 */
	public List<BlDeposit> conCanlReg(String param,IUser user){
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);
		if(regvo==null||CommonUtils.isNull(regvo.getPkPv()))
			throw new BusException("退号时未获取到就诊信息！");

		String pkPv = regvo.getPkPv();
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
			selfPayTrInsuMapper.updateSchAppt(updateMap);
		}
		//更新就诊记录
		selfPayTrInsuMapper.updatePvEncounter(updateMap);

		//发送退号信息到平台
		Map<String,Object>  paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		paramMap.put("nameEmp", ((User)user).getNameEmp());
		paramMap.put("codeEmp", ((User)user).getCodeEmp());
		PlatFormSendUtils.sendPvOpCancelRegMsg(paramMap);
		paramMap = null;

		return negaBlDeposits;
	}
	
}
