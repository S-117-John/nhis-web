package com.zebone.nhis.pro.zsba.mz.bl.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
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
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.pro.sd.bl.vo.StDepoInfoVo;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlInvoice;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlOpDt;
import com.zebone.nhis.pro.zsba.mz.bl.dao.ZsbaSelfPayTrInsuMapper;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.service.qgyb.ZsbaQGService;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.service.sgs.ZsbaSgsService;
import com.zebone.nhis.pro.zsba.mz.pub.support.BoaiInvSettltService;
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
public class ZsbaSelfPayTrInsuService {
	private Logger logger = LoggerFactory.getLogger("nhis.ZsbaSelfPayTrInsuService");
	
	@Resource
	private ZsbaSelfPayTrInsuMapper zsbaSelfPayTrInsuMapper;
	
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
	private BoaiInvSettltService boaiInvSettltService;
	
	@Autowired
	private PdStOutPubService pdStOutPubService; // 库存预留量
	
	@Autowired
	private ZsbaQGService zsbaQGService;
	
	@Autowired
	private ZsbaSgsService zsbaSgsService;
	
	/**
	 * 交易号：007002003040->022003027106
	 * 查询患者门诊挂号结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOpRegStInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		
		List<Map<String,Object>> retList = zsbaSelfPayTrInsuMapper.qryOpRegStInfo(paramMap);
		
		return retList;
	}
	
	/**
	 * 交易号：007002003041->022003027107
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
	 * 交易号：007002003044->022003027109
	 * 根据结算主键复制本次结算下的记费明细信息到bl_op_dt表
	 */
	public void copyOpStOrder(String param,IUser user){
		List<String> pkList = JsonUtil.readValue(param,new TypeReference<List<String>>(){});
		if(pkList==null || pkList.size()<=0){
			throw new BusException("未传入结算信息，无法复制医嘱！");
		}
		
		//通过结算主键查询结算记录
		BlSettle blSettle = DataBaseHelper.queryForBean(
				"select * from bl_settle where pk_settle=?",
				BlSettle.class,new Object[]{pkList.get(0)});
		if(blSettle!=null){
			/**20210712本次结算身份如果是日间手术[此代码00011一经启用不可随意更改]不允许做退费重结操作**/
			BdHp bdHp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ?", BdHp.class, blSettle.getPkInsurance());
			if(bdHp!=null && bdHp.getCode().equals("00011")) {
				throw new BusException("本次结算无法复制，门诊以【门诊日间手术】身份结算的不允许做退费重结操作！");
			}
			//患者是否存在已复制待结算处方，否则无法再次复制
			List<BlOpDt> blOpDts = DataBaseHelper.queryForList(
					"select * from bl_op_dt where pk_pi=? and flag_recharge='1' and pk_settle is null and pk_settle_before is not null",
					BlOpDt.class,new Object[]{blSettle.getPkPi()});
			if(blOpDts.size()>0){
				throw new BusException("已存在复制且未结算的处方明细，请先结算之前复制的处方或点击[清空复制处方]按钮清除之前的数据再操作！");
			}
			
			/**复制结算处方明细数据**/
			List<ZsbaBlOpDt> dtList = zsbaSelfPayTrInsuMapper.qryDtListByPkSettle(pkList);
			if(dtList!=null && dtList.size()>0){
				//Set<String> pkcnordList = new HashSet<String>();
				for(ZsbaBlOpDt dtVo : dtList){
					dtVo.setPkSettleBefore(dtVo.getPkSettle());//记录原结算主键
					dtVo.setFlagRecharge("1");
					dtVo.setFlagSettle("0");
					dtVo.setPkSettle(null);
					dtVo.setPkInvoice(null);
					ApplicationUtils.setDefaultValue(dtVo, true);
					
					//记录所有计费明细pk_cnord
					//if(StringUtils.isNotEmpty(dtVo.getPkCnord())) {
					//	pkcnordList.add(dtVo.getPkCnord());
					//}
				}
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsbaBlOpDt.class), dtList);
				//1.复制处方后更新就诊表状态
				DataBaseHelper.execute("update pv_encounter set eu_locked='0',eu_status='2' where pk_pv=? and del_flag='0'", dtList.get(0).getPkPv());
				//2.复制处方后更新CN_ORDER表医嘱有效日期字段DATE_EFFE为当天23点
				//if(pkcnordList!=null && pkcnordList.size()>0){
				//	DataBaseHelper.execute("update cn_order set date_effe=(convert(varchar(10),getdate(),120)+' 23:59:59') where pk_cnord in ("+CommonUtils.convertSetToSqlInPart(pkcnordList, "pk_cnord")+")", new Object[]{});
				//}
			}
		}else{
			throw new BusException("找不到本次结算记录信息！");
		}
	}
	
	/**
	 * 交易号：007002003045->022003027124
	 * 根据pk_cnord修改执行记录关联的pkSettle信息
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
		Set<String> presList = zsbaSelfPayTrInsuMapper.qryPresOccByPkSettle(paramMap);
		if(presList!=null && presList.size()>0){
			//更新pkSettle信息
			DataBaseHelper.execute("update ex_pres_occ set pk_settle = ? where pk_presocc in ("+CommonUtils.convertSetToSqlInPart(presList, "pk_presocc")+")", new Object[]{paramMap.get("pkSettle")});
		}
		
		//查询医技执行信息
		Set<String> assistList = zsbaSelfPayTrInsuMapper.qryAssistOccByPkSettle(paramMap);
		if(assistList!=null && assistList.size()>0){
			//更新pkSettle信息
			DataBaseHelper.execute("update ex_assist_occ set pk_settle = ? where pk_assocc in ("+CommonUtils.convertSetToSqlInPart(assistList, "pk_assocc")+")", new Object[]{paramMap.get("pkSettle")});
		}
	}
	
	/**
	 * 交易号：007002003046->022003027110
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
		
		/*查询医嘱处方执行信息
		Integer presCnt = DataBaseHelper.queryForScalar(
				"select count(1) from ex_pres_occ where pk_settle = ?",
				Integer.class, new Object[]{paramMap.get("pkSettle")});
		if(presCnt!=null && presCnt>0){
			throw new BusException("此次结算下有对应的执行单记录，无法退费，请联系管理员！");
		}*/
		
		//查询医技执行信息
		Integer assistCnt = DataBaseHelper.queryForScalar(
				"select count(1) from EX_ASSIST_OCC where PK_SETTLE = ?",
				Integer.class, new Object[]{paramMap.get("pkSettle")});
		if(assistCnt!=null && assistCnt>0){
			throw new BusException("此次结算下有对应的执行单记录，无法退费，请联系管理员！");
		}
	}
	
	/**
	 * 检查患者是否存在复制处方且未完成重结，否则无法再次复制
	 * 交易号：022003027138
	 */
	public void queryExistNoChargeData(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0 
				|| !paramMap.containsKey("pkSettle") || CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			throw new BusException("未传入结算信息,修改执行单记录失败！");
		}
		
		//此处接口改为通过结算主键找到pkPi,然后看患者是否存在已复制待结算处方，否则无法再次复制
		BlSettle blSettle = DataBaseHelper.queryForBean(
				"select * from bl_settle where pk_settle=?",
				BlSettle.class,new Object[]{paramMap.get("pkSettle")});
		if(blSettle!=null){
			List<BlOpDt> blOpDts = DataBaseHelper.queryForList(
					"select * from bl_op_dt where pk_pi=? and flag_recharge='1' and pk_settle is null and pk_settle_before is not null",
					BlOpDt.class,new Object[]{blSettle.getPkPi()});
			if(blOpDts.size()>0){
				throw new BusException("已存在复制且未结算的处方明细，请先结算之前复制的处方或点击[清空复制处方]按钮清除之前的数据再操作！");
			}
		}else{
			throw new BusException("找不到本次结算记录信息！");
		}
	}
	
	/**
	 * 交易号：007002003047->022003027111
	 * 门诊退费(深大自费转医保使用，只支持自费结算退费)
	 * @param param
	 * @param user
	 */
	public String opStReBack(String param,IUser user){
		logger.info("门诊自费转医保退费结算入参==>>"+param);
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
	    String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
		//String eBillFlag = boaiInvSettltService.getBL0031ByNameMachine(CommonUtils.getString(paramMap.get("nameMachine")));
		if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
			try{
				boaiInvSettltService.billCancelNoNewTrans(blSettle.getPkSettle(), user);
			}catch (Exception e) {
				throw new BusException("退号失败：" + e.getMessage());
			}
		}
		
		
		//查询是否有关联的执行单信息，如果有则更新执行单信息
		List<String> pkStList = new ArrayList<>();
		
		Integer presCnt = zsbaSelfPayTrInsuMapper.qryPresOccCnt(paramMap);
		
		//查询医技执行信息
		Integer assistCnt = zsbaSelfPayTrInsuMapper.qryAssOccCnt(paramMap);

		if((presCnt!=null && presCnt.intValue()>0) || (assistCnt!=null && assistCnt.intValue()>0)){
			//查询医嘱处方执行信息
			List<String> presList = zsbaSelfPayTrInsuMapper.qryPresOccPkSettle(paramMap);
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
			List<String> assList = zsbaSelfPayTrInsuMapper.qryAssOccPkSettle(mapParam);
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
			List<BlSettle> stList = zsbaSelfPayTrInsuMapper.qryStInfoByPkSettle(pkStList);
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
	
	/**
	 * 博爱改身份重结功能-门诊退费(支持所有身份==>先复制处方重新收费再将上次结算的全部退回然后将处方、医技关联到新的结算记录)
	 * 交易号：022003027135
	 */
	@SuppressWarnings({ "unchecked" })
	public String zsbaOpStReBack(String param,IUser user){
		logger.info("博爱改身份重结自动退费入参==>>"+param);
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0 || !paramMap.containsKey("pkSettle") || CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettle")))){
			throw new BusException("改身份重结==>>未传入原结算信息，退费失败！");
		}
		//获取退费交款支付方式列表(入参不传此参数也无影响)
		List<Map<String,Object>> newBlDeposits = CommonUtils.isNotNull(paramMap.get("blDeposits"))?((List<Map<String,Object>>)paramMap.get("blDeposits")):null;
		// 根据结算主键查询结算信息
		Map<String,Object> mapParam = new HashMap<>();
		mapParam.put("pkSettle", paramMap.get("pkSettle"));
		BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
		if(blSettle == null) {
			throw new BusException("改身份重结==>>没有查询到原结算信息！");
		}
		// 正式退费前将原结算处方明细复制(新流程由收费员人工操作复制处方)
		/**List<ZsbaBlOpDt> dtList = zsbaSelfPayTrInsuMapper.findDtListByPkSettle(paramMap);
		if(dtList!=null && dtList.size()>0){
			//Set<String> pkcnordList = new HashSet<String>();
			for(ZsbaBlOpDt dtVo : dtList){
				dtVo.setPkSettleBefore(dtVo.getPkSettle());//记录原结算主键
				dtVo.setFlagRecharge("1");
				dtVo.setFlagSettle("0");
				dtVo.setPkSettle(null);
				dtVo.setPkInvoice(null);
				ApplicationUtils.setDefaultValue(dtVo, true);
				
				//记录所有计费明细pk_cnord
				//if(StringUtils.isNotEmpty(dtVo.getPkCnord())) {
				//	pkcnordList.add(dtVo.getPkCnord());
				//}
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ZsbaBlOpDt.class), dtList);
			//1.复制处方后更新就诊表状态
			DataBaseHelper.execute("update pv_encounter set eu_locked='0',eu_status='2' where pk_pv=? and del_flag='0'", blSettle.getPkPv());
			//2.复制处方后更新CN_ORDER表医嘱有效日期字段DATE_EFFE为当天23点
			//if(pkcnordList!=null && pkcnordList.size()>0){
			//	DataBaseHelper.execute("update cn_order set date_effe=(convert(varchar(10),getdate(),120)+' 23:59:59') where pk_cnord in ("+CommonUtils.convertSetToSqlInPart(pkcnordList, "pk_cnord")+")", new Object[]{});
			//}
		}**/
		// 生成退费结算信息
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
		List<BlDeposit> refundBlDeposits = createNegaBlDeposit(UserContext.getUser(),blDeposits,pkSettleCanc,newBlDeposits);

		// 如果挂号时打印的发票要作废发票(其实博爱无挂号收费的业务)
		String BL0008_code = ApplicationUtils.getSysparam("BL0008", false);
		if (EnumerateParameter.ONE.equals(BL0008_code)) {
			// 根据结算主键查询作废结算时对应的发票
			List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
			if(blInvoices!=null&&blInvoices.size()>0){
				for(BlInvoice inv : blInvoices){
					// 更新作废发票信息
					opcgPubHelperService.updateRefoundBlInvoice(inv);
				}
			}
		}
		//获取BL0031（收费结算启用电子票据），参数值为1时调用冲红电子票据接口
	    String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
		//String eBillFlag = boaiInvSettltService.getBL0031ByNameMachine(CommonUtils.getString(paramMap.get("nameMachine")));
		if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
			try{
				boaiInvSettltService.billCancelNoNewTrans(blSettle.getPkSettle(), user);
				
				// 作废之前的发票(注意是把之前结算的未作废发票全部作废)
				User userInfo = (User)user;
				String updateBlInvoice = "select * from bl_invoice bi inner join bl_st_inv bsi on bi.pk_invoice=bsi.pk_invoice where bsi.pk_settle=? and bi.flag_cancel!='1'";
				List<ZsbaBlInvoice> blInvoices = DataBaseHelper.queryForList(updateBlInvoice, ZsbaBlInvoice.class, new Object[]{blSettle.getPkSettle()});
				for (ZsbaBlInvoice blInvoice : blInvoices) {
					blInvoice.setFlagCancel(EnumerateParameter.ONE);
					blInvoice.setDateCancel(new Date());
					blInvoice.setPkEmpCancel(userInfo.getPkEmp());
					blInvoice.setNameEmpCancel(userInfo.getNameEmp());
					blInvoice.setFlagBack(EnumerateParameter.ONE);//退票标记
					//20210427正常能走到这里的话说明电子票据冲红是成功的，为避免冲红成功但是接口未返回结果导致冲红信息未正常回写影响日结退票数量这里直接再次更新电子票据退票标志为1
					if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
						blInvoice.setFlagCancelEbill(EnumerateParameter.ONE);
					}
					ApplicationUtils.setDefaultValue(blInvoice, false);
				}
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ZsbaBlInvoice.class), blInvoices);
			}catch (Exception e) {
				throw new BusException("改身份重结==>>电子票据冲红失败：" + e.getMessage());
			}
		}
		
		/**改身份全部重结无需更新相关处方、医技执行记录表，重新结算完成后调接口将处方、医技等信息全部自动关联到新的结算主键即可**/
		/*//查询出重新结算的费用信息
		String rechargePkSettle = paramMap.get("rechargePkSettle")!=null?String.valueOf(paramMap.get("rechargePkSettle")):"";
		if(StringUtils.isNotEmpty(rechargePkSettle)){
			//重新结算的费用集合
			List<BlOpDt> newDtList = DataBaseHelper.queryForList(
					"select * from bl_op_dt where pk_settle = ?",
					BlOpDt.class,new Object[]{rechargePkSettle});
			
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
				// 更新处方执行单状态为取消，需要重新结算的不更新
				updateExPresOcc(oldDtList,newDtList,(User)user);
			}
		}else{
			throw new BusException("改身份重结==>>没有查到重新结算的结算主键！");
		}*/
		/*if(StringUtils.isNotEmpty(param)){
			throw new BusException("系统提示：程序测试控制失败！");
		}*/
		
		/**判断原结算是否有外部医保结算否则做医保结算撤销处理**/
		BdHp bdHp = DataBaseHelper.queryForBean("select * from BD_HP where PK_HP=?", BdHp.class, new Object[]{blSettle.getPkInsurance()});
		if(bdHp!=null && StringUtils.isNotEmpty(bdHp.getDtExthp())){
			//判断退费交款方式是否存在个账
			BigDecimal amtGrzhzf = null;
			for(BlDeposit rbd : refundBlDeposits){
				if(rbd.getDtPaymode().equals("12") || rbd.getDtPaymode().equals("13") || rbd.getDtPaymode().equals("14")){
					//一次结算交款方式个账只会存在一条
					amtGrzhzf = rbd.getAmount();
					break;
				}
			}
			//1.全国医保
			if(bdHp.getDtExthp().equals("08")){
				Map<String, Object> qgMap = new HashMap<String, Object>();
				qgMap.put("pkPv", blSettle.getPkPv());
				qgMap.put("pkSettle", blSettle.getPkSettle());
				Map<String, Object> ybPreReturnInfo = new HashMap<String, Object>();
				ybPreReturnInfo.put("yBPreIntoParam", null);//全退(无需传递重结信息)
				qgMap.put("ybPreReturnInfo", ybPreReturnInfo);
				Map<String, Object> insRefundMap = zsbaQGService.mzHpSetttleCancel(JSONUtils.toJSONString(qgMap), user);
				logger.debug("改身份重结==>>全国医保撤销结算结果如下："+insRefundMap.toString());
				//关联医保退费结算记录中的院内冲负结算主键并回写退费个账交款金额
				if(insRefundMap.get("ybCancelPksettle")!=null){
					String insCancelSettId = String.valueOf(insRefundMap.get("ybCancelPksettle"));
					if(amtGrzhzf!=null){
						DataBaseHelper.execute("update ins_qgyb_st set pk_settle=?,amt_grzhzf=? where PK_PV=? and yb_pksettle=?", pkSettleCanc, amtGrzhzf, blSettle.getPkPv(), insCancelSettId);
					}else{
						DataBaseHelper.execute("update ins_qgyb_st set pk_settle=? where PK_PV=? and yb_pksettle=?", pkSettleCanc, blSettle.getPkPv(), insCancelSettId);
					}
				}
			}
			//2.省工伤
			if(bdHp.getDtExthp().equals("05")){
				Map<String, Object> sgsMap = new HashMap<String, Object>();
				sgsMap.put("pkPv", blSettle.getPkPv());
				sgsMap.put("pkSettle", blSettle.getPkSettle());
				sgsMap.put("yBPreIntoParam", null);
				try {
					Map<String, Object> insRefundMap = zsbaSgsService.mzHpSetttleCancel(JSONUtils.toJSONString(sgsMap), user);
					logger.debug("改身份重结==>>省工伤撤销结算结果如下："+insRefundMap.toString());
					//关联医保退费结算记录中的院内冲负结算主键并回写退费个账交款金额
					if(insRefundMap.get("ybCancelPksettle")!=null){
						String insCancelSettId = String.valueOf(insRefundMap.get("ybCancelPksettle"));
						if(amtGrzhzf!=null){
							DataBaseHelper.execute("update ins_sgsyb_st set pk_settle=?,amt_grzhzf=? where PK_PV=? and yb_pksettle=?", pkSettleCanc, amtGrzhzf, blSettle.getPkPv(), insCancelSettId);
						}else{
							DataBaseHelper.execute("update ins_sgsyb_st set pk_settle=? where PK_PV=? and yb_pksettle=?", pkSettleCanc, blSettle.getPkPv(), insCancelSettId);
						}
					}
				} catch (Exception e) {
					throw new BusException("改身份重结==>>省工伤撤销结算失败：" + e.getMessage());
				}
			}
		}
		return pkSettleCanc;
	}
	
	/**
	 * 获取退费重结支付单信息-待完善暂未使用
	 * 交易号：022003027160
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findRefundReChargeInfo(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0 || !paramMap.containsKey("pkSettleCanc") || CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkSettleCanc")))){
			throw new BusException("未传入结算信息！");
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String pkSettleCanc = CommonUtils.getString(paramMap.get("pkSettleCanc"));
		//根据退费结算主键获取退费结算记录
		BlSettle blSettle = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle=?", BlSettle.class, new Object[]{pkSettleCanc});
		if(blSettle!=null){
			returnMap.put("refundDate", DateUtils.formatDate(blSettle.getDateSt(), "yyyy-MM-dd HH:mm:ss"));//退费日期
			returnMap.put("codeSt", blSettle.getCodeSt());//退费结算号
			//通过pkPi获取患者信息
			PiMaster piMaster = DataBaseHelper.queryForBean("select * from pi_master where pk_pi=?", PiMaster.class, new Object[]{blSettle.getPkPi()});
			//通过pkPv获取就诊信息
			PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, new Object[]{blSettle.getPkPv()});
			if(pvEncounter!=null) {
				returnMap.put("namePi", pvEncounter.getNamePi());//患者姓名
				returnMap.put("codeOp", piMaster.getCodeOp());//门诊ID
				returnMap.put("datePv", DateUtils.formatDate(pvEncounter.getDateBegin(), "yyyy-MM-dd HH:mm:ss"));//就诊日期
			}
			//通过结算主键获取退费发票和重开发票信息
			BlInvoice cancelInvoice = DataBaseHelper.queryForBean("select * from bl_invoice where pk_invoice=(select top 1 pk_invoice from bl_st_inv where pk_settle=?)", BlInvoice.class, new Object[]{blSettle.getPkSettleCanc()});
			if(cancelInvoice!=null){
				returnMap.put("refundEbillNo", cancelInvoice.getEbillno());//退费发票号码
				returnMap.put("refundEbillAmount", cancelInvoice.getAmountInv()*(-1));//退费发票金额
			}
			BlInvoice rechargeInvoice = DataBaseHelper.queryForBean("select * from bl_invoice where pk_invoice=(select top 1 pk_invoice from bl_st_inv where pk_settle=?)", BlInvoice.class, new Object[]{blSettle.getPkSettleRecharge()});
			if(rechargeInvoice!=null){
				returnMap.put("rechargeEbillNo", rechargeInvoice.getEbillno());//重开发票号码
				returnMap.put("rechargeEbillAmount", rechargeInvoice.getAmountInv());//重开发票金额
			}
		}else{
			throw new BusException("找不到该退费结算记录信息！");
		}
		return returnMap;
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
	 * 交易号：007002003050->022003027112
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
				DataBaseHelper.execute("update cn_order set dt_hpprop = ? where pk_cnord in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_cnord")+" ) or pk_pres in (select pk_pres from CN_PRESCRIPTION where pres_no in ("+CommonUtils.convertSetToSqlInPart(pkList, "pres_no")+"))", new Object[]{hpprop});
				
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
	 * 交易号：007002003055->022003027113
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
			
			/**20210417退费后需要回写flag_print2='2'用于PACS系统自动删除已发送至三方的申请**/
			if(StringUtils.isNotEmpty(cnRisApply.getFlagPrint2()) && cnRisApply.getFlagPrint2().equals("1")){
				cnRisApply.setFlagPrint2("2");
			}
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
					zsbaSelfPayTrInsuMapper.querySettlePressRecord(mapParam);
		// 查询检查检验
		List<Map<String, Object>> settleAssists =
					zsbaSelfPayTrInsuMapper.querySettleAssistRecord(mapParam);

		//查询医嘱附加费用信息
		List<Map<String, Object>> stEtceterasList =
					zsbaSelfPayTrInsuMapper.qryEtceterasList(mapParam);

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
}
