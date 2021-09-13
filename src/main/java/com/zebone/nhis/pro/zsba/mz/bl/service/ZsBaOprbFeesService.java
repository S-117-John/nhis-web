package com.zebone.nhis.pro.zsba.mz.bl.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.OpcgQryWrapMapper;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.vo.BlOpPubRebackFeesVo;
import com.zebone.nhis.bl.pub.vo.BlPubSettleVo;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccShare;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpClientUtils;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlInvoice;
import com.zebone.nhis.pro.zsba.mz.bl.dao.ZsBaOprbFeesMapper;
import com.zebone.nhis.pro.zsba.mz.bl.vo.OprbFeesDto;
import com.zebone.nhis.pro.zsba.mz.pub.service.ZsbaOpCgPubService;
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
 * 中山市博爱医院门诊退费后台客户化服务
 * @author zhangtao
 */
@Service
public class ZsBaOprbFeesService {
	private Logger logger = LoggerFactory.getLogger("nhis.ZsBaOprbFeesServiceLog");
	
	@Autowired
	private OpcgQryWrapMapper opcgQryWrapMapper; // 查询已结算的费用执行单

	@Autowired
	private ZsbaOpCgPubService opcgPubService; // 门诊计费

	@Autowired
	private CommonService commonService; // 发票信息

	@Autowired
	private BalAccoutService balAccoutService; // 账户信息

	@Autowired
	@Resource
	private PdStOutPubService pdStOutPubService; // 库存预留量

	@Autowired
	private CgQryMaintainService cgQryMaintainService;

	@Autowired
	private BoaiInvSettltService invSettltService;
	
	@Autowired
	private ZsBaOprbFeesMapper zsBaOprbFeesMapper; // 查询已结算的费用执行单

	/**
	 * 查询已经结算的费用申请单（处方）detail
	 * 022006006018->022003027085
	 * @param param
	 * @param user
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> querySettledPresFeesDetailToReBack(String param, IUser user) {
		OprbFeesDto oprbFeesDto = JsonUtil.readValue(param, OprbFeesDto.class);
		List<Map<String, Object>> settlePressItem = new ArrayList<Map<String, Object>>();
		Map<String, String> mapParam = new HashMap<>();
		mapParam.put("pkPress", oprbFeesDto.getPkPress());
		mapParam.put("pkSettle", oprbFeesDto.getPkSettle());
		settlePressItem = zsBaOprbFeesMapper.querySettlePressItem(mapParam);//处方药品
		settlePressItem.addAll(zsBaOprbFeesMapper.querySettlePressAddItem(mapParam));//处方药品附加
		//euAdditem!=0非附加的项目直接通过计费主键获取可退数量更新列表中的可退数量
		List<String> pkCgops = settlePressItem.stream().filter(m -> !"0".equals(MapUtils.getString(m, "euAdditem")))
				.map(m -> MapUtils.getString(m, "pkCgop")).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(pkCgops)){
			String pkCgopInSql = CommonUtils.convertListToSqlInPart(pkCgops);
			String dtSql = "SELECT cg.quan+ISNULL(bk.quan, 0) quan,cg.PK_CGOP "+
			"FROM bl_op_dt cg "+
			"LEFT OUTER JOIN ( "+
			"SELECT SUM(back.quan) quan,PK_CGOP_BACK "+
			"FROM bl_op_dt back "+
			"WHERE back.PK_CGOP_BACK IN ("+pkCgopInSql+") "+
			"AND BACK.flag_SETTLE = '1' "+
			"GROUP BY back.PK_CGOP_BACK "+
			") bk ON cg.PK_CGOP=bk.PK_CGOP_BACK "+
			"WHERE cg.PK_CGOP IN ("+pkCgopInSql+")";
			List<Map<String,Object>> result = DataBaseHelper.queryForList(dtSql);
			for(Map<String, Object> map : settlePressItem){
				for(Map<String, Object> bkMap : result){
					if(MapUtils.getString(map, "pkCgop").equals(MapUtils.getString(bkMap, "pkCgop"))){
						map.put("canBack", bkMap.get("quan"));
					}
				}
			}
		}
		return settlePressItem;
	}
	
	/**
	 * 交易号：022006006011->022003027009
	 * 查询已经结算的费用申请单（检治）detail
	 * @param param
	 * @param user
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> querySettledLRFeesDetailToReBack(String param, IUser user) {
		OprbFeesDto oprbFeesDto = JsonUtil.readValue(param, OprbFeesDto.class);
		List<Map<String, Object>> settleAssistItem = new ArrayList<Map<String, Object>>();
		Map<String, String> mapParam = new HashMap<>();
		mapParam.put("pkCnords", oprbFeesDto.getPkCnords());
		mapParam.put("pkSettle", oprbFeesDto.getPkSettle());
		if(!CommonUtils.isEmptyString(oprbFeesDto.getFlagEtce()) && "1".equals(oprbFeesDto.getFlagEtce())){
			settleAssistItem = zsBaOprbFeesMapper.qryEtceterasItem(mapParam);
		} else if(!CommonUtils.isEmptyString(oprbFeesDto.getPkCgops())){
			mapParam.put("pkCgops", oprbFeesDto.getPkCgops());
			settleAssistItem.addAll(zsBaOprbFeesMapper.qryCgItemByPkCgop(mapParam));
		} else {
			settleAssistItem =  zsBaOprbFeesMapper.querySettleAssistItem(mapParam);
		}
		//euAdditem!=0非附加的项目直接通过计费主键获取可退数量更新列表中的可退数量
		List<String> pkCgops = settleAssistItem.stream().filter(m -> !"0".equals(MapUtils.getString(m, "euAdditem")))
				.map(m -> MapUtils.getString(m, "pkCgop")).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(pkCgops)){
			String pkCgopInSql = CommonUtils.convertListToSqlInPart(pkCgops);
			String dtSql = "SELECT cg.quan+ISNULL(bk.quan, 0) quan,cg.PK_CGOP "+
			"FROM bl_op_dt cg "+
			"LEFT OUTER JOIN ( "+
			"SELECT SUM(back.quan) quan,PK_CGOP_BACK "+
			"FROM bl_op_dt back "+
			"WHERE back.PK_CGOP_BACK IN ("+pkCgopInSql+") "+
			"AND BACK.flag_SETTLE = '1' "+
			"GROUP BY back.PK_CGOP_BACK "+
			") bk ON cg.PK_CGOP=bk.PK_CGOP_BACK "+
			"WHERE cg.PK_CGOP IN ("+pkCgopInSql+")";
			List<Map<String,Object>> result = DataBaseHelper.queryForList(dtSql);
			for(Map<String, Object> map : settleAssistItem){
				for(Map<String, Object> bkMap : result){
					if(MapUtils.getString(map, "pkCgop").equals(MapUtils.getString(bkMap, "pkCgop"))){
						map.put("canBack", bkMap.get("quan"));
					}
				}
			}
		}
		return settleAssistItem;
	}
	
	/**
	 * 交易号：022006006009->022003027010
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
				zsBaOprbFeesMapper.querySettlePressRecord(mapParam);
		// 查询检查检验
		List<Map<String, Object>> settleAssists =
				zsBaOprbFeesMapper.querySettleAssistRecord(mapParam);

		//查询医嘱附加费用信息
		/*
		List<Map<String, Object>> stEtceterasList =
				zsBaOprbFeesMapper.qryEtceterasList(mapParam);
		*/
		if(settlePress != null && settlePress.size() > 0){
			settle.addAll(settlePress);
		}
		if(settleAssists != null && settleAssists.size() > 0){
			settle.addAll(settleAssists);
		}
		/*
		if(stEtceterasList!=null && stEtceterasList.size()>0){
			settle.addAll(stEtceterasList);
		}
		*/

		return settle;
	}
	
	/**
	 * 交易号：022006006008->022003027011
	 * 门诊退费结算
	 * 总体思路：
	 * 		1 --退费目前只支持一个结算下的退费。
	 * 		2 --若药房没有发药的，必须全部退；若发了药的，退多少药就能退多少费。
	 * 		3 --部分退费支持的情况：（发药支持，部分退费就支持）
	 * 				a --一个结算下面多个处方，可以单处方退。
	 * 				b --一个处方下面多个明细，可以单明细退。
	 * 				c --一个明细下面有多个数量，可以退部分数量。
	 * @param param
	 * @param user
	 */
	public OprbFeesDto opRebackFeesSettlement(String param, IUser user) {
		logger.info("门诊退费结算入参==>>"+param);
		OprbFeesDto oprbFeesDto = JsonUtil.readValue(param, OprbFeesDto.class);
		List<BlOpDt> blOpDts = oprbFeesDto.getBlOpDts();
				
		//校验结算:为空或者存在多个结算
		validBlopdtCreateRefundParam(blOpDts);

		// 调用退费
		BlOpPubRebackFeesVo blOpPubRebackFeesVo = opcgPubService.blOpRefound(blOpDts);
		List<BlOpDt> blOpDtNegaAll = blOpPubRebackFeesVo.getBlOpDtOlds(); //当前结算下的所有明细的冲负记录,bl_op_dt表全部冲负记录已插入
		List<BlOpDt> blOpDtLeft = blOpPubRebackFeesVo.getBlOpDtNews(); //当前结算下的剩余未退费的明细记录,bl_op_dt表未退费记录重新插入

		BigDecimal rebackAmount = BigDecimal.ZERO;
		for (BlOpDt blOpDt : blOpDtNegaAll) {
			rebackAmount = rebackAmount.add(new BigDecimal(blOpDt.getAmount()));
		}
		oprbFeesDto.setRebackAmount(rebackAmount);

		// 全部退费：生成其他表的冲负记录
		BlSettle negaBlSettle = new BlSettle();
		if(blOpDtNegaAll != null && blOpDtNegaAll.size() > 0){
			// 生成当前结算的冲负记录
			negaBlSettle = createNegaBlSettle((User)user, blOpDtNegaAll, oprbFeesDto);
			// 生成负的结算明细
			createNegaBlSettleDetail(oprbFeesDto, negaBlSettle);
			// 生成负的交款记录
			createNegaBlDeposit((User)user, oprbFeesDto, negaBlSettle);
			// 更新结算明细表
			updateBlOpDts(blOpDtNegaAll, negaBlSettle);
			// 作废之前的发票(注意是把之前结算的所有发票全部作废)
			updateBlInvoice((User)user, blOpDts,oprbFeesDto.getMachineName());
			// 更新处方执行明细,需要重新结算的不更新
			updateExPresOccDt(blOpDtNegaAll,blOpDtLeft);
			// 更新医技执行单 ，需要重新结算的不更新
			updateExAssistOcc(blOpDtNegaAll,blOpDtLeft);
			// 更新检查申请单，需要重新结算的不更新
			updateCnRisApply(blOpDtNegaAll,blOpDtLeft);
			// 更新检验申请单，需要重新结算的不更新
			updateCnLabApply(blOpDtNegaAll,blOpDtLeft);
			// 解除全部退费的处方中未发药的药品对应的预留量，需要重新结算的不解除
			relieveReservedAmount(blOpDtNegaAll,blOpDtLeft);
			//更新处方执行单状态为取消--yangxue增加，需要重新结算的处方不更新
			updateExPresOcc(blOpDtNegaAll,blOpDtLeft,(User)user);
		}
		oprbFeesDto.setPkSettleCanc(negaBlSettle.getPkSettle());
		if(CommonUtils.isEmptyString(oprbFeesDto.getFlagUntlFees()) || !"1".equals(oprbFeesDto.getFlagUntlFees())){
			// 部分退费： 处理剩余未退费的部分，重新结算
			if(blOpDtLeft != null && blOpDtLeft.size() > 0){
				// 重新结算新生成相关表
				LeftPartReSettle(blOpDtLeft, oprbFeesDto, blOpDts.get(0).getPkSettle(), oprbFeesDto.getMedicarePayments(), blOpDts);
				//返回新生成的明细
				oprbFeesDto.setBlOpDtsNew(blOpDtLeft);
				
				/*
				 * TODO: lipz 2021-05-29 更新未退重结后的执行记录的结算主键
				 */
				String sql = "UPDATE EX_ASSIST_OCC SET PK_SETTLE=? WHERE PK_PV=? and PK_CNORD=?";
				for(BlOpDt opDt : blOpDtLeft){
					if(StringUtils.isNotEmpty(opDt.getPkCnord())){
						DataBaseHelper.execute(sql, oprbFeesDto.getPkSettle(), opDt.getPkPv(), opDt.getPkCnord());
					}
				}

				//更新重新结算bl_settle表的pk_insurance字段，以上次结算的为准
				if(!CommonUtils.isEmptyString(oprbFeesDto.getPkSettle())){
					DataBaseHelper.execute(
							"update bl_settle set pk_insurance=? where pk_settle = ?",
							new Object[]{negaBlSettle.getPkInsurance(),oprbFeesDto.getPkSettle()});
				}
				//记录重收的pk_settle
				if(!CommonUtils.isEmptyString(oprbFeesDto.getPkSettleCanc())){
					DataBaseHelper.execute(
							"update bl_settle set pk_settle_recharge=? where pk_settle = ?",
							new Object[]{oprbFeesDto.getPkSettle(),oprbFeesDto.getPkSettleCanc()});
				}
			}
		}
		String praa=JsonUtil.writeValueAsString(blOpDtNegaAll);
		//门诊处方退费
		//opcgPubService.sendORPMsg(blOpDtNegaAll,null,"CR");

		//发送门诊收费信息至平台
		Map<String,Object> paramVo=new HashMap<String, Object>();
		paramVo.put("blOpDts", blOpDts);
		paramVo.put("Control", "CR");
		PlatFormSendUtils.sendBlOpSettleMsg(paramVo);

		//收费项目状态发送至急诊系统
		Set<String> pkBlOpDt = new HashSet<String>();
		for (BlOpDt blOpDt : blOpDts) {
			pkBlOpDt.add(blOpDt.getPkCgop());
		}
		Map<String,Object> paramEmrVo= new HashMap<>(16);
		paramEmrVo.put("pkList", pkBlOpDt);
		paramEmrVo.put("euSettle", "2");//0：删除，1：已收费，2退费
		//此处有问题，如果是部分退，his会全退重收，未退的收费项目主键会重新生成，第二次退费就会找不到急诊系统对应的原收费项目信息
		ExtSystemProcessUtils.processExtMethod("EmrStSp", "saveOrUpdateCharges", new Object[]{paramEmrVo});

		ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "savaOpReturnConsumable", param,blOpDts);
		
		// 通知体检系统 已退费
		String pkcgop = blOpDts.get(0).getPkCgop();
		String pvSql = "select PK_PV, EU_PVTYPE from PV_ENCOUNTER where PK_PV=(select PK_PV from BL_OP_DT where PK_CGOP=?)";
		Map<String, Object> pvMap = DataBaseHelper.queryForMap(pvSql, pkcgop);
		if("4".equals(pvMap.get("euPvtype"))) {
			StringJoiner pkcgops = new StringJoiner("','", "'", "'");
			for(BlOpDt dt : blOpDts) {
				pkcgops.add(dt.getPkCgop());
			}
			String pkCnordSql = "select DISTINCT ord.ORDSN, dt.PK_CNORD from BL_OP_DT dt INNER JOIN CN_ORDER ord on ord.PK_CNORD=dt.PK_CNORD where dt.PK_CGOP in ("+pkcgops.toString()+")";
			List<Map<String, Object>> pkCnordList = DataBaseHelper.queryForList(pkCnordSql);
			Set<String> ordsnSet = new HashSet<String>();
			for (Map<String, Object> map : pkCnordList) {
				if(map!=null && map.containsKey("pkCnord") && map.get("pkCnord")!=null) {
					ordsnSet.add(map.get("pkCnord").toString());
				}
			}
			if(ordsnSet.size()>0) {
				pushTjRefund(pvMap.get("pkPv").toString(), ordsnSet);
			}
		}
		return oprbFeesDto;
	}
	
	/**
	 * 方法作用：1、检验前台传递的blopdt是否为空；2、检验是否存在多笔结算；
	 * @param blOpDts
	 */
	private void validBlopdtCreateRefundParam(List<BlOpDt> blOpDts) {
		if (blOpDts == null || blOpDts.size() == 0) {
			throw new BusException("传入结算数据为空，请联系管理员");
		}
		String pkSettle = blOpDts.get(0).getPkSettle();
		for (BlOpDt blOpDt : blOpDts) {
			if (!pkSettle.equals(blOpDt.getPkSettle())) {
				throw new BusException("不能同时退费多笔结算！请联系管理员");
			}
		}
	}

	/**
	 * 生成当前结算的冲负记录
	 * @param user
	 * @param blOpDts
	 */
	private BlSettle createNegaBlSettle(User user, List<BlOpDt> blOpDtNegaAll, OprbFeesDto oprbFeesDto) {
		String pkSettle = blOpDtNegaAll.get(0).getPkSettle();
		BlSettle bs = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle=?",
						BlSettle.class, new Object[] { pkSettle });
		if (bs == null){
			throw new BusException("不存在次结算记录");
		}
		bs.setDtSttype("21");// 取消结算
		bs.setAmountSt(new BigDecimal(-1).multiply(bs.getAmountSt()));
		bs.setAmountPi(new BigDecimal(-1).multiply(bs.getAmountPi()));
		bs.setAmountInsu(new BigDecimal(-1).multiply(bs.getAmountInsu()));
		bs.setAmountAdd(MathUtils.mul(-1D, bs.getAmountAdd()));
		bs.setAmountDisc(MathUtils.mul(-1D, bs.getAmountDisc()));
		bs.setAmountPrep(new BigDecimal(-1).multiply(bs.getAmountPrep()));
		bs.setAmountRound(new BigDecimal(-1).multiply(bs.getAmountRound()));
		bs.setDateSt(new Date());
		bs.setPkDeptSt(user.getPkDept());
		bs.setPkEmpSt(user.getPkEmp());
		bs.setNameEmpSt(user.getNameEmp());
		bs.setPkOrg(user.getPkOrg());
		bs.setPkSettleCanc(pkSettle);
		bs.setFlagCanc(EnumerateParameter.ZERO);
		bs.setPkCc(null);
		bs.setFlagCc(EnumerateParameter.ZERO);
		bs.setCodeSt(ApplicationUtils.getCode("0604"));
		ApplicationUtils.setDefaultValue(bs, true);
		DataBaseHelper.insertBean(bs);

		//修改原结算记录的flag_canl为“1”
	    DataBaseHelper.execute("update bl_settle set flag_canc='1' where pk_settle = ?", pkSettle);
	    
	    //如果有医保退费结算主键则回写到医保结算表退费记录
	    logger.info("退费结算医保计算主键信息==>>pk_pv：" + bs.getPkPv() + "；setlId：" + oprbFeesDto.getSetlId() + "；pkSettle：" + pkSettle + "；cancelPkSettle：" + bs.getPkSettle());
  		if(StringUtils.isNotEmpty(oprbFeesDto.getSetlId())){
  			// 查询医保结算信息
  			if(oprbFeesDto.getSetlId().startsWith("gs")){
  	  			DataBaseHelper.execute("update ins_sgsyb_st set pk_settle=? where PK_PV=? and yb_pksettle=?", bs.getPkSettle(), bs.getPkPv(), oprbFeesDto.getSetlId());
  			}else{
				DataBaseHelper.execute("update ins_qgyb_st set pk_settle=? where PK_PV=? and yb_pksettle=?", bs.getPkSettle(), bs.getPkPv(), oprbFeesDto.getSetlId());
  			}
  		}
	  	
		return bs;
	}

	/**
	 * 根据退费的结算主键  生成负的结算明细记录
	 * @param oprbFeesDto
	 * @param negaBlSettle
	 */
	private void createNegaBlSettleDetail(OprbFeesDto oprbFeesDto, BlSettle negaBlSettle) {
		BlOpDt blOpDt = oprbFeesDto.getBlOpDts().get(0);
		String pkSettle = blOpDt.getPkSettle();
		String sql = "select * from bl_settle_detail where pk_settle=?";
		List<BlSettleDetail> blSettleDetails = DataBaseHelper.queryForList(sql, BlSettleDetail.class, new Object[] {pkSettle});
		for (BlSettleDetail blSettleDetail : blSettleDetails) {
			// 新增记录
			blSettleDetail.setAmount(-1 * blSettleDetail.getAmount());
			// 关联退费结算主键
			blSettleDetail.setPkSettle(negaBlSettle.getPkSettle());
			ApplicationUtils.setDefaultValue(blSettleDetail, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), blSettleDetails);
	}

	/**
	 * 生成负的交款记录
	 * @param user
	 * @param oprbFeesDto
	 * @param negaBlSettle
	 */
	private void createNegaBlDeposit(User user, OprbFeesDto oprbFeesDto, BlSettle negaBlSettle) {
		// 用传递的结算主键查询上次交款信息
		BlOpDt blOpDt = oprbFeesDto.getBlOpDts().get(0);
		String pkSettle = blOpDt.getPkSettle();
		List<BlDeposit> blDeposits = DataBaseHelper.queryForList("select * from bl_deposit where pk_settle=?",
												BlDeposit.class, new Object[] {pkSettle});
		List<BlExtPay> trdBlExtPays = new ArrayList<>();
		//前台退费时退款付款方式集合
		List<BlDeposit> newBlDeposits = oprbFeesDto.getBlDeposits();
		
		for (BlDeposit blDeposit : blDeposits) {
			//是否按现金退
			/*if(EnumerateParameter.ONE.equals(oprbFeesDto.getNoPratRet())) {
				blDeposit.setDtPaymode(IDictCodeConst.CASH);
			}else {
				trdBlExtPays.addAll(blDepositBydtPayMode(blDeposit, oprbFeesDto));
			}*/
			/**20210427此处更新为人医的处理逻辑**/
			for (BlDeposit newBlDeposit : newBlDeposits) {
				if(newBlDeposit.getPkDepo().equals(blDeposit.getPkDepo())) {
					blDeposit.setDtPaymode(newBlDeposit.getDtPaymode());
				}
			}
			trdBlExtPays.addAll(blDepositBydtPayMode(blDeposit, oprbFeesDto));
			
			blDeposit.setEuDptype("4");// 收付款类型:取消结算
			blDeposit.setEuDirect("-1");
			blDeposit.setAmount(new BigDecimal(-1).multiply(blDeposit.getAmount()));
			blDeposit.setDatePay(new Date());
			blDeposit.setPkDept(user.getPkDept());
			blDeposit.setPkEmpPay(user.getPkEmp());
			blDeposit.setNameEmpPay(user.getNameEmp());
			// 结算主键要放的是退款的结算主键
			blDeposit.setPkSettle(negaBlSettle.getPkSettle());
			blDeposit.setPkDepoBack(blDeposit.getPkDepo());
			blDeposit.setPkCc(null);
			blDeposit.setFlagCc(EnumerateParameter.ZERO);
			ApplicationUtils.setDefaultValue(blDeposit, true);
			
			//退款支付方式中存在医保个帐的需要修改 INS_QGYB_ST、INS_SGSYB_ST表的amt_grzhzf个人账户支付字段
			if("12".equals(blDeposit.getDtPaymode()) || "13".equals(blDeposit.getDtPaymode()) || "14".equals(blDeposit.getDtPaymode())){
				logger.info("退费结算医保个账记录==>>pk_pv：" + negaBlSettle.getPkPv() + "；setlId：" + oprbFeesDto.getSetlId() + "；amount：" + blDeposit.getAmount());
				if(StringUtils.isEmpty(oprbFeesDto.getSetlId())){
					throw new BusException("有医保个账退费数据，但未传入医保退费结算主键[setlId]");
				}
				// 查询医保结算信息
				if(oprbFeesDto.getSetlId().startsWith("gs")){
					DataBaseHelper.execute("update ins_sgsyb_st set amt_grzhzf=? where PK_PV=? and yb_pksettle=?", blDeposit.getAmount(), negaBlSettle.getPkPv(), oprbFeesDto.getSetlId());
				}else{
					DataBaseHelper.execute("update ins_qgyb_st set amt_grzhzf=? where PK_PV=? and yb_pksettle=?", blDeposit.getAmount(), negaBlSettle.getPkPv(), oprbFeesDto.getSetlId());
				}
			}
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), blDeposits);
		
		// 存在第三方支付方式，冲负bl_ext_pay表
		if(trdBlExtPays != null && trdBlExtPays.size() > 0){
//			thirdPartyPaymentService.registeredRefundRecords(bankCardParam, oprbFeesDto.getBlExtPayBankList());
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlExtPay.class), trdBlExtPays);
		}
	}

	/**
	 * 通过收退款方式来操作bl_deposit:账户支付操作账户相关表；银行卡支付操作银行卡相关表
	 * @param blDeposit
	 */
	private List<BlExtPay> blDepositBydtPayMode(BlDeposit blDeposit, OprbFeesDto oprbFeesDto){
		List<BlExtPay> blExtPays = new ArrayList<>();
		String dtPaymode = blDeposit.getDtPaymode();
 		if(dtPaymode.equalsIgnoreCase(IDictCodeConst.CASH)) {
			return blExtPays;
		}
		else if(dtPaymode.equalsIgnoreCase(IDictCodeConst.PATIACCOUNT)) {
			BlDepositPi blDepositPi = new BlDepositPi();
			ApplicationUtils.setDefaultValue(blDepositPi, true);
			blDepositPi.setEuDirect(EnumerateParameter.ONE);
			blDepositPi.setPkPi(blDeposit.getPkPi());
			blDepositPi.setAmount(blDeposit.getAmount());
			blDepositPi.setDtPaymode(IDictCodeConst.PATIACCOUNT);
			blDepositPi.setPkEmpPay(UserContext.getUser().getPkEmp());
			blDepositPi.setNameEmpPay(UserContext.getUser().getNameEmp());
			// 向账户退当时患者扣除的款项  作废：(退款时不写收退款信息)
			//balAccoutService.saveMonOperation(blDepositPi, UserContext.getUser(), blDeposit.getPkPv(), "1", blDepositPi.getDtPaymode());

			PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(oprbFeesDto.getPkPi());// 患者账户信息

			/*
			 * 查询患者存在的账户共享关系 , 如果共享关系不为空说明本次账户扣款是扣除的父账户的
			 */
			PiAccShare piAccShare = DataBaseHelper.queryForBean("select * from pi_acc_share where pk_pi_use=? and del_flag='0'", PiAccShare.class, oprbFeesDto.getPkPi());
			PiAcc piAccParent = null;

			if (piAccShare != null) {
				// 父账户
				blDepositPi.setPkPi(piAccShare.getPkPi());

				ApplicationUtils.setDefaultValue(piAccParent, false);
			} else {
				ApplicationUtils.setDefaultValue(piAcc, false);
			}

			//退款时写消费记录
			balAccoutService.piAccDetailVal(piAccParent == null ? piAcc : piAccParent, blDepositPi, oprbFeesDto.getPkPv(), "2");
		}/*else {
			// 第三方的新旧码
			if(oprbFeesDto.getBlExtPayBankList() == null ) return blExtPays;
			BlExtPayBankVo blExt = oprbFeesDto.getBlExtPayBankList().get(0);
			switch (dtPaymode) {
				case IDictCodeConst.BANKCARD:
					BlExtPay blExtPayBank = thirdPartyPaymentService.refundBankCardBlExtPay(blDeposit, blExt);
					blExtPays.add(blExtPayBank);
					break;
				case IDictCodeConst.WECHAT:
				case IDictCodeConst.ALI:
					BlExtPay weixinAliblExtPay = thirdPartyPaymentService.refundWeixinOrAliPayBlExtPay(blDeposit, blExt);
					blExtPays.add(weixinAliblExtPay);
					break;
				default:
					break;
			}
		}*/
		return blExtPays;
	}

	/**
	 * 更新收费明细表，添加主键
	 * @param blOpDtNegaAll
	 * @param negaBlSettle
	 */
	private void updateBlOpDts(List<BlOpDt> blOpDtNegaAll, BlSettle negaBlSettle) {
		// 更新表bl_op_dt，pk_settle=结算主键，flag_settle=1；
		for (BlOpDt blOpDt : blOpDtNegaAll) {
			blOpDt.setPkSettle(negaBlSettle.getPkSettle()); // 将退款结算主键给全退明细的记录
			blOpDt.setFlagSettle(EnumerateParameter.ONE);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlOpDt.class), blOpDtNegaAll);
	}

	/**
	 * 更新表bl_invoice，flag_cancel=1，date_cancel=当前日期，pk_emp_cancel和name_emp_cancel为当前用户；
	 * @param user
	 * @param blOpDts
	 */
	private void updateBlInvoice(User user, List<BlOpDt> blOpDts,String machineName) {
		String pkSettle = blOpDts.get(0).getPkSettle();

		//获取BL0031（收费结算启用电子票据），参数值为1时调用冲红电子票据接口
		String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
		//String eBillFlag = invSettltService.getBL0031ByNameMachine(machineName);
		if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
			try{
				invSettltService.billCancel(pkSettle, user);
			}catch (Exception e) {
				throw new BusException("退费失败：" + e.getMessage());
			}
		}

		// 作废之前的发票(注意是把之前结算的未作废发票全部作废)
		String updateBlInvoice = "select * from bl_invoice bi inner join bl_st_inv bsi on bi.pk_invoice=bsi.pk_invoice where bsi.pk_settle=? and bi.flag_cancel!='1'";
		List<ZsbaBlInvoice> blInvoices = DataBaseHelper.queryForList(updateBlInvoice, ZsbaBlInvoice.class, new Object[] { pkSettle });
		for (ZsbaBlInvoice blInvoice : blInvoices) {
			blInvoice.setFlagCancel(EnumerateParameter.ONE);
			blInvoice.setDateCancel(new Date());
			blInvoice.setPkEmpCancel(user.getPkEmp());
			blInvoice.setNameEmpCancel(user.getNameEmp());
			blInvoice.setFlagBack("1");//退票标记
			//20210427正常能走到这里的话说明电子票据冲红是成功的，为避免冲红成功但是接口未返回结果导致冲红信息未正常回写影响日结退票数量这里直接再次更新电子票据退票标志为1
			if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
				blInvoice.setFlagCancelEbill("1");
			}
			ApplicationUtils.setDefaultValue(blInvoice, false);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ZsbaBlInvoice.class), blInvoices);
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
					//20210305测试过程发现该金额精度问题导致小数位很长，每次计算保留两位小数
					exPresOccDt.setAmountRet(old_amount_ret.add(add_amount).setScale(2, RoundingMode.HALF_UP));
				}
			}
		}

		if(exPresOccDts.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExPresOccDt.class), exPresOccDts);
		}
	}

	/**
	 * 结算中剩余未结算的部分   重新结算(退费使用)
	 * @param blOpDtLeft
	 * @param oprbFeesDto
	 * @param pkSettleSrc 原结算主键
	 * @return OprbFeesDto
	 */
	private OprbFeesDto LeftPartReSettle(List<BlOpDt> blOpDtLeft, OprbFeesDto oprbFeesDto,String pkSettleSrc,BigDecimal amtInsuThird,List<BlOpDt> blOpDtNegaAll) {
		Set<String> pkBlOpDtInSql = new HashSet<String>();
		List<BlDeposit> blDepositNews = new ArrayList<BlDeposit>();

		String sqlBlDeposit = "select * from bl_deposit where pk_settle=? ";
		List<BlDeposit> blDeposits = DataBaseHelper.queryForList(sqlBlDeposit, BlDeposit.class,pkSettleSrc);
		
		//前台退费时退款付款方式集合
		List<BlDeposit> newBlDeposits = oprbFeesDto.getBlDeposits();
		if(newBlDeposits != null) {
			for (BlDeposit newDeposit : newBlDeposits) { 
				for (BlDeposit oldDeposit : blDeposits) {
					//原始收款金额-退款金额=本次应收
					if(oldDeposit.getPkDepo().equals(newDeposit.getPkDepo())) {
						BlDeposit blDeposit = new BlDeposit();
						blDeposit.setAmount(oldDeposit.getAmount().subtract(newDeposit.getAmount().abs()));
						blDeposit.setDtPaymode(newDeposit.getDtPaymode());
						blDeposit.setPayInfo(oldDeposit.getPayInfo());
						blDeposit.setNote(newDeposit.getNote());
						blDeposit.setNameWork(newDeposit.getNameWork());
						blDeposit.setNameOpera(newDeposit.getNameOpera());
						blDeposit.setEuRemove(oldDeposit.getEuRemove());
						blDeposit.setPkEmpRemove(oldDeposit.getPkEmpRemove());
						blDeposit.setDateRemove(oldDeposit.getDateRemove());
						blDeposit.setNoteRemove(oldDeposit.getNoteRemove());
						blDeposit.setDtBank(oldDeposit.getDtBank());// TODO : lipz 2021-05-31 退费重收生成新的付款记录时，取原交易记录的类型
						blDepositNews.add(blDeposit);
					}
				}
			}
			//第三方不支持部分退，his业务全退后按现金重收
			if((blDepositNews == null || blDepositNews.size() == 0)) {
				if(newBlDeposits.size() == 1) {
					BigDecimal sumAmount = BigDecimal.ZERO;
					for (BlDeposit oldDeposit : blDeposits) {
						sumAmount = sumAmount.add(oldDeposit.getAmount().abs());
					}
					BlDeposit blDeposit = new BlDeposit();
					blDeposit.setAmount(sumAmount.subtract(newBlDeposits.get(0).getAmount().abs()));
					blDeposit.setDtPaymode(newBlDeposits.get(0).getDtPaymode());
					blDeposit.setNote(newBlDeposits.get(0).getNote());
					blDeposit.setNameWork(newBlDeposits.get(0).getNameWork());
					blDeposit.setNameOpera(newBlDeposits.get(0).getNameOpera());
					blDepositNews.add(blDeposit);
				}else{
					throw new BusException("BlDeposit付款方式记录数据有误，请联系管理员！");
				}
			}
		}
		
		// 组装结算参数
		BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
		blPubSettleVo.setPkPi(oprbFeesDto.getPkPi());
		blPubSettleVo.setBlOpDts(blOpDtLeft);
		blPubSettleVo.setCodeInv(oprbFeesDto.getInVoiceNo());
		blPubSettleVo.setPkEmpinvoice(oprbFeesDto.getPkEmpinvoice());
		blPubSettleVo.setPkInvcate(oprbFeesDto.getPkInvcate());
		blPubSettleVo.setDepositList(blDepositNews);
		blPubSettleVo.setPkPi(oprbFeesDto.getPkPi());
		blPubSettleVo.setPkPv(oprbFeesDto.getPkPv());
		blPubSettleVo.setPkBlOpDtInSql(CommonUtils.convertSetToSqlInPart(pkBlOpDtInSql, "pk_cgop"));
		blPubSettleVo.setMachineName(oprbFeesDto.getMachineName());
		blPubSettleVo.setAmtRound(oprbFeesDto.getNewAmtRound());

		//查询发票信息
		InvoiceInfo inv = new InvoiceInfo();
		inv.setPkInvcate(oprbFeesDto.getPkInvcate());
		inv.setPkEmpinvoice(oprbFeesDto.getPkEmpinvoice());
		inv.setCodeInv(oprbFeesDto.getInVoiceNo());

		//查询发票明细信息
		List<String> pkList = new ArrayList<>();
		for(BlOpDt dt : blOpDtLeft){
			pkList.add(dt.getPkCgop());
		}
		Map<String,Object> qryParam = new HashMap<>();
		qryParam.put("pkList", pkList);
		qryParam.put("pkOrg", UserContext.getUser().getPkOrg());
		inv.setBlInDts(opcgQryWrapMapper.qryInvDtByCg(qryParam));
		inv.setPkSettleOld(blOpDtNegaAll.get(0).getPkSettle());

		List<InvoiceInfo> invList = new ArrayList<>();
		invList.add(inv);
		blPubSettleVo.setInvoiceInfo(invList);
		oprbFeesDto.setInvoiceInfo(invList);
		//获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
		//部分退时如果开启电子票据参数时则直接打印电子票据，不再打印纸质票据。未开启电子票据参数时则打印纸质票据
		String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
		//String eBillFlag = invSettltService.getBL0031ByNameMachine(blPubSettleVo.getMachineName());
		if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
			blPubSettleVo.setFlagPrint("0");
		}
		// 调用结算
		List<BlInvoiceDt> blInvoiceDts = null;
		List<BlInvoice> blInvoice = null;
		Map<String,Object> result =  opcgPubService.accountedSettlement(blPubSettleVo, true, amtInsuThird, oprbFeesDto.getReChargeSetlId(), BigDecimal.ZERO, null, null); // 不校验同一明细下的统一收费状态，支持部分退款
		if(result!=null){
			blInvoiceDts = (List<BlInvoiceDt>)result.get("invDt");
			blInvoice = (List<BlInvoice>)result.get("inv");
			/**更新第三方外部支付接口表
			 * 外部支付接口表pk_depo和结算缴款pk_depo相关联
			 * 根据结算关联的bl_deposit表的pk_depo字段查询bl_ext_pay表信息，如果匹配到，则更新bl_ext_pay的pk_depo字段
			 * 
			if(blDeposits!=null && blDeposits.size()>0){
				BlExtPay payInfo = DataBaseHelper.queryForBean(
						"select * from BL_EXT_PAY  where FLAG_PAY='1' and PK_DEPO=?",
						BlExtPay.class, new Object[]{blDeposits.get(0).getPkDepo()});

				if(payInfo!=null && !CommonUtils.isEmptyString(payInfo.getPkExtpay())){
					//查询新生成的bl_deposit记录，更新bl_ext_pay表pk_depo
					String pkSettle = (String)result.get("pkSettle");
					List<BlDeposit> newDepo = DataBaseHelper.queryForList(
							"select * from bl_deposit where pk_settle = ?",
							BlDeposit.class, new Object[]{pkSettle});
					if(newDepo!=null && newDepo.size()>0){
						payInfo.setPkDepo(newDepo.get(0).getPkDepo());
						DataBaseHelper.updateBeanByPk(payInfo);//更新bl_ext_pay表
					}
				}
			}*/
		}

		if(result.get("pkSettle")!= null){
		    oprbFeesDto.setPkSettle(result.get("pkSettle").toString());
		}

		// 更新发票领用表
		if (blInvoice != null && blInvoice.size() > 0d)// 目前单张发票
		{
			//未开启电子票据参数时更新纸质票据信息
			if(CommonUtils.isEmptyString(eBillFlag) || !"1".equals(eBillFlag))
			{
				for (BlInvoice invoInfo : blInvoice) {
					// 单张更新
					commonService.confirmUseEmpInv(invoInfo.getPkEmpinvoice(), 1L);
				}
			}

			// 返回发票明细
			oprbFeesDto.setBlInvoiceDts(blInvoiceDts);
		}
		//合并处方，以物品为单位设置库存预留量，分科室处理药品预留量（同一科室同一药品合并）。
		//settlePdOutService.makeSdPresAssitRecords(blOpDtLeft);0
		oprbFeesDto.setPkSettle(CommonUtils.getString(result.get("pkSettle")));
		return oprbFeesDto;
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
			if (leftblOpDts != null && leftblOpDts.size() > 0) {
				for (BlOpDt leftdt : leftblOpDts) {
					if (CommonUtils.isNotNull(blOpDtNega.getPkCnord())
							&& blOpDtNega.getPkCnord().equals(leftdt.getPkCnord())
							&& (MathUtils.add(blOpDtNega.getQuan(), leftdt.getQuan()).compareTo(0D) == 0)) {
						has = true;
						break;
					}
				}
			}
			if (!has) {
				pkCnords.add(blOpDtNega.getPkCnord());
			}
		}
		
		String pkCnordStr =  CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord");
		String exAssOccSql = "select * from ex_assist_occ where eu_status<>'1' and (flag_refund='0' or flag_refund is null) and pk_cnord in ("+ pkCnordStr +")";
		List<ExAssistOcc> assistOccs = DataBaseHelper.queryForList(exAssOccSql, ExAssistOcc.class, new Object[] {});

		// 如果医嘱关联的收费项目下有未退费的记录，不修改Ex_assist_occ的值。否则置为flag_refund已退。
		String opDtSql = "select * from bl_op_dt dt inner join BL_SETTLE st on dt.pk_settle = st.pk_settle "
				+ "	where pk_cnord in ("+ pkCnordStr + ") and st.DT_STTYPE= '00'  and "
				+ " not exists (select 1 from bl_settle back where st.pk_settle=back.pk_settle_canc and st.pk_pi=back.pk_pi)";
		List<BlOpDt> bList = DataBaseHelper.queryForList(opDtSql, BlOpDt.class, new Object[]{});

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
		List<CnRisApply> cnRisApplies = DataBaseHelper.queryForList("select * from cn_ris_apply where eu_status='1' and pk_cnord in ("
				+ CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")", CnRisApply.class, new Object[]{});
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
	 * 生成解除预留参数
	 * @param pdlist
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
	 * 交易号 ： 022006006007->022003027012
	 * 退费预结算
	 * @param param
	 * @param user
	 * @return OprbFeesDto
	 */
	public OprbFeesDto countRebackFeesAmount(String param, IUser user) {
		logger.info("门诊退费预结算入参==>>"+param);
		/*
		 * 大原则 收的时候优先从账户里扣钱，退的时候优先退现金（但是退的现金不能大于当时结算时交的现金数），然后再往其他里退
		 * 思路：
		 * @author zhangmenghao
			A 全退的话，怎么收费怎么显示退费字段（取到总金额，医保，患者支付）。
				---（bl_settle中包括了上次结算的支付信息，先拿到这些----结算总金额AMOUNT_ST，患者自费金额AMOUNT_PI，第三方医保金额AMOUNT_INSU）
			B 部分退的话，在全退的基本上重新收剩余部分，这个涉及之前收退款方式。
				----如果之前是1种收退款方式（账户，现金，银行卡）的情况：
					处理逻辑是：
						1  根据结算主键查询bl_settle得到当前全部明细结算时的结算总金额AMOUNT_ST，患者自费金额AMOUNT_PI，第三方医保金额AMOUNT_INSU
						2   然后再根据当前的价格策略重新计算剩余部分医保支付多少，患者自付多少，
						----------二者相减得出总金额退多少，医保支付退多少，患者支付退多少。
						3  要返回给前台的是结算总额退多少，医保金额退多少，退患者多少（不同支付方式的合计）：
						----------【金额退多少，账户退多少，银行卡退多少，微信推多少，支付宝退多少】
				----如果之前是2种收退款方式（账户，现金，银行卡，微信，支付宝）的情况：
						1,2步的考虑一样，细化第3步：组合方式为账户和其他方式的组合考虑。
						先考虑账户，账户不够的话用其他方式弥补，把退的明细返回界面。
		 */

		OprbFeesDto oprbFeesDto = JsonUtil.readValue(param, OprbFeesDto.class);
		BigDecimal AggregateAmount = oprbFeesDto.getAggregateAmount();
		BigDecimal PatientsPay = oprbFeesDto.getPatientsPay();
		BigDecimal MedicarePayments = oprbFeesDto.getMedicarePayments();

		// 0  本次需要退费的明细    + 校验结算:为空或者存在多个结算
		List<BlOpDt> blOpDts = oprbFeesDto.getBlOpDts();
		validBlopdtCreateRefundParam(blOpDts);   // 校验空或者多个结算
		String pkSettle = blOpDts.get(0).getPkSettle();
		
		// 1  查询当前结算金额分配和患者自付的收退款方式,验证收退款方式总和金额和患者自付金额是否相等
		Map<String, BigDecimal> allAlreadySettleAmount = allAlreadySettleAmount(pkSettle);
		Map<String, BlDeposit> allDtPayMode = allSettleRecordsRefund(pkSettle);
		validatingDirtyRecord(allAlreadySettleAmount, allDtPayMode); // 校验是否存在收退款方式脏数据

		// 查询出本次结算的所有明细
		List<BlOpDt> blOpdtAlls = DataBaseHelper.queryForList("select * from bl_op_dt where pk_settle=?", BlOpDt.class, pkSettle);

		//查询本次结算信息
		BlSettle stInfo = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
		if(stInfo!=null){
			oprbFeesDto.setOldAmtRound(stInfo.getAmountRound());
			
			/**20210624本次结算身份如果是日间手术[此代码00011一经启用不可随意更改]不允许门诊退费**/
			BdHp bdHp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ?", BdHp.class, stInfo.getPkInsurance());
			if(bdHp!=null && bdHp.getCode().equals("00011")) {
				throw new BusException("本次门诊以【门诊日间手术】身份结算的不允许退费！");
			}
			
			/**20210616退费时将患者就诊身份改为本次退费结算的身份**/
			String updatePvHpSql = "update pv_encounter set pk_insu=(select top 1 pk_insurance from bl_settle where pk_settle=?) where pk_pv=?";
			DataBaseHelper.execute(updatePvHpSql, stInfo.getPkSettle(), stInfo.getPkPv());
		}

		// 2   是否是部分退费的情况(抽取成公共方法，供预退费接口调用)
		//     --根据返回的值判断，为空则是全退，否则是部分退，如果是药品，被部分退药的情况， 也算部分退费
		List<BlOpDt> blOpDtNews = opcgPubService.blOpDtPart(blOpDts, blOpdtAlls);

		//2.1 是否按现金退
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList("select code from bd_defdoc where code_defdoclist='110100' and DEL_FLAG ='0' and VAL_ATTR like '%4%'", new Object[]{});

		// 3 全部退费(blDeposits)
		oprbFeesDto = allRefundDetail(allAlreadySettleAmount, allDtPayMode, oprbFeesDto);
		//是否按现金退
		for (BlDeposit blDeposit : oprbFeesDto.getBlDeposits()) {
			if(itemList != null && itemList.size() > 0) {
	        	for (Map<String, Object> map : itemList) {
	        		if(blDeposit.getDtPaymode().equals(map.get("code"))) {
	        			blDeposit.setDtPaymode(IDictCodeConst.CASH);
	        		} 		
				}
	        }
		}
	    // 4   部分退    重新结算剩余部分
		if(!CollectionUtils.isEmpty(blOpDtNews)){

			checkCancelAllSettle(pkSettle);
			// 4.1 重新取就诊准备重新结算
			/*
			String sql = "select * from pv_encounter where eu_pvtype < '3' and eu_status <> '9' and  pk_pi=? and pk_pv=? ";
			PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql, PvEncounter.class,
							new Object[]{blOpDtNews.get(0).getPkPi(), blOpDtNews.get(0).getPkPv()});
			*/
			for(BlOpDt blOpDtNew : blOpDtNews){
				blOpDtNew.setPkInsu(stInfo.getPkInsurance());
			}

			OpCgTransforVo opCgTransforVo = new OpCgTransforVo();
			opCgTransforVo.setPkPi(blOpDtNews.get(0).getPkPi());
			opCgTransforVo.setBlOpDts(blOpDtNews);

			opCgTransforVo.setAggregateAmount(AggregateAmount == null ? BigDecimal.ZERO : AggregateAmount);
			opCgTransforVo.setAmtInsuThird(MedicarePayments == null ? BigDecimal.ZERO : MedicarePayments);
			opCgTransforVo.setXjzf(PatientsPay == null ? BigDecimal.ZERO : PatientsPay);

			// 4.2  计算经过医保和价格策略处理后的金额
			OpCgTransforVo leftInsSettle = opcgPubService.returnInsComputSettle(user, opCgTransforVo);
			
			// 4.3.21 之前结算的收款方式只有一种方式
			if(allDtPayMode.size() == 1) {   
				// 患者自付预退金额-剩余部分重新结算患者自费部分=退款部分
				OprbFeesDto partRefundOneMode = singleRefundMode(allAlreadySettleAmount, allDtPayMode, leftInsSettle);
				partRefundOneMode.setBlOpDts(blOpDts);
				partRefundOneMode.setPkPi(blOpDts.get(0).getPkPi());
				partRefundOneMode.setOldAmtRound(stInfo.getAmountRound());
				//是否按现金退
				for (BlDeposit blDeposit : partRefundOneMode.getBlDeposits()) {
					if(itemList != null && itemList.size() > 0) {
			        	for (Map<String, Object> map : itemList) {
			        		if(blDeposit.getDtPaymode().equals(map.get("code"))) {
			        			blDeposit.setDtPaymode(IDictCodeConst.CASH);
			        		} 		
						}
			        }
				}
				return partRefundOneMode;
			}else{   
				// 4.3.32 收退款方式有多种
				OprbFeesDto partRefundtwoMode = twoRefundMode(allAlreadySettleAmount, allDtPayMode, leftInsSettle);
				partRefundtwoMode.setBlOpDts(blOpDts);
				partRefundtwoMode.setPkPi(blOpDts.get(0).getPkPi());
				partRefundtwoMode.setOldAmtRound(stInfo.getAmountRound());
				//是否按现金退
				for (BlDeposit blDeposit : partRefundtwoMode.getBlDeposits()) {
					if(itemList != null && itemList.size() > 0) {
			        	for (Map<String, Object> map : itemList) {
			        		if(blDeposit.getDtPaymode().equals(map.get("code"))) {
			        			blDeposit.setDtPaymode(IDictCodeConst.CASH);
			        		} 		
						}
			        }
				}
				return partRefundtwoMode;
			}
		}
		
		return oprbFeesDto;
	}
	
	/**
	 * 全退预结算的返回参数
	 * @param allAlreadySettleAmount
	 * @param allDtPayMode
	 * @param oprbFeesDto
	 */
	public OprbFeesDto allRefundDetail(Map<String, BigDecimal> allAlreadySettleAmount, Map<String, BlDeposit> allDtPayMode,
			OprbFeesDto oprbFeesDto) {
		BigDecimal aggregateAmountRefund = allAlreadySettleAmount.get("aggregateAmount");
		oprbFeesDto.setAggregateAmount(new BigDecimal(-1).multiply(aggregateAmountRefund.abs())); // 退费总金额
		BigDecimal medicarePaymentsRefund = allAlreadySettleAmount.get("medicarePayments");
		oprbFeesDto.setMedicarePayments(new BigDecimal(-1).multiply(medicarePaymentsRefund.abs())); // 医保退费
		BigDecimal patientsPayRefund = allAlreadySettleAmount.get("patientsPay");
		oprbFeesDto.setPatientsPay(new BigDecimal(-1).multiply(patientsPayRefund.abs())); // 退患者自付

		//多种支付方式
		List<BlDeposit> blDeposits = new ArrayList<BlDeposit>();
		BigDecimal rebackAmount = BigDecimal.ZERO;
		for (Map.Entry<String,BlDeposit>  entry : allDtPayMode.entrySet()) {
			BlDeposit blDeposit = entry.getValue();
			//账户支付
			if(IDictCodeConst.PATIACCOUNT.equals(entry.getKey())) {				
				PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)",
																PiAcc.class, blDeposit.getPkAcc());
				//查询患者账户是否有押金，如有则账户余额-押金
				Double amtAcc = DataBaseHelper.queryForScalar(
						"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
						Double.class, new Object[]{oprbFeesDto.getPkPi()});
				if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
					piAcc.setAmtAcc(piAcc.getAmtAcc().subtract(BigDecimal.valueOf(amtAcc)));
				}
				// 退账户
				BigDecimal account = allDtPayMode.get(IDictCodeConst.PATIACCOUNT).getAmount();
				oprbFeesDto.setAccountPay(new BigDecimal(-1).multiply(account.abs()));
				// 账户余额
				oprbFeesDto.setAccountBalance(piAcc.getAmtAcc());
			}
			BigDecimal amount = blDeposit.getAmount().abs();
			rebackAmount = rebackAmount.add(amount);
			blDeposit.setAmount(new BigDecimal(-1).multiply(amount));
			blDeposits.add(blDeposit);
		}
		
		oprbFeesDto.setBlDeposits(blDeposits);
		oprbFeesDto.setRebackAmount(new BigDecimal(-1).multiply(rebackAmount.abs()));//应退金额
		
		return oprbFeesDto;
	}
	
	/**
	 * 当收退款方式只有一种时， 返回的退款信息
	 * 2020-1-11 应收费加入分四舍五入逻辑，所以退费也加入四舍五入处理逻辑（对本次自付退费金额进行分位四舍五入）
	 * @param allAlreadySettleAmount
	 * @param allDtPayMode
	 * @param leftInsSettle
	 * @return OprbFeesDto 一种方式的预退金额分配
	 */
	public OprbFeesDto singleRefundMode(Map<String, BigDecimal> allAlreadySettleAmount, Map<String, BlDeposit> allDtPayMode,
								  OpCgTransforVo leftInsSettle)
	{
		OprbFeesDto singleModeFee = partRefundThreeAmount(allAlreadySettleAmount, leftInsSettle);
		BigDecimal patientsPayRefund = singleModeFee.getPatientsPay();
		
		// 患者自付部分 --账户
		boolean patiAccountFlag = allDtPayMode.containsKey(IDictCodeConst.PATIACCOUNT);
		if(patiAccountFlag){
			BlDeposit blDeposit = allDtPayMode.get(IDictCodeConst.PATIACCOUNT);
			PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)",
															PiAcc.class, blDeposit.getPkAcc());
			//查询患者账户是否有押金，如有责账户余额-押金
			Double amtAcc = DataBaseHelper.queryForScalar(
					"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
					Double.class, new Object[]{piAcc.getPkPi()});
			if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
				piAcc.setAmtAcc(piAcc.getAmtAcc().subtract(BigDecimal.valueOf(amtAcc)));
			}

			// 退账户
			singleModeFee.setAccountPay(patientsPayRefund);
			// 退完后的账户余额
			//singleModeFee.setAccountBalance(piAcc.getAmtAcc().add(patientsPayRefund.abs()));
			singleModeFee.setAccountBalance(piAcc.getAmtAcc());//只显示账户余额(不加预退费金额)，成哥要求
			return singleModeFee;
		}	
			
		List<BlDeposit> trdBlDeposits = trdBlDeposits(allDtPayMode, BigDecimal.ZERO, patientsPayRefund);
		if(trdBlDeposits!=null && trdBlDeposits.size()>0){
			singleModeFee.setBlDeposits(trdBlDeposits);
		}else{
			singleModeFee.setRebackAmount(patientsPayRefund);
			for(String key : allDtPayMode.keySet()){
				BlDeposit depo = allDtPayMode.get(key);
				depo.setAmount(patientsPayRefund);
				List<BlDeposit> blDeposits = new ArrayList<>();
				blDeposits.add(depo);
				singleModeFee.setBlDeposits(blDeposits);
				break;
			}
			
			return singleModeFee;
		}
		return singleModeFee;
	}

	/**
	 * 当收退款方式有多种时， 返回的退款信息（按比例分配）（注意：计算时存在差额，需要将差额特殊处理）
	 * 2020-1-11 应收费加入分四舍五入逻辑，所以退费也加入四舍五入处理逻辑（对本次自付退费金额进行分位四舍五入）
	 * @param allAlreadySettleAmount
	 * @param allDtPayMode
	 * @param leftInsSettle
	 * @return OprbFeesDto 多种方式的预退金额分配
	 */
	public OprbFeesDto twoRefundMode(Map<String, BigDecimal> allAlreadySettleAmount, Map<String, BlDeposit> allDtPayMode,
										OpCgTransforVo leftInsSettle)
	{
		OprbFeesDto twoModeFee = partRefundThreeAmount(allAlreadySettleAmount, leftInsSettle);
		
		BigDecimal patientsPayRefund = twoModeFee.getPatientsPay(); 

		List<BlDeposit> blDeposits = new ArrayList<>();
		BigDecimal rebackAmount = BigDecimal.ZERO;
		boolean isCash = false;
		
		//多种付款方式，按比例分配
		for (Map.Entry<String,BlDeposit> entry : allDtPayMode.entrySet()) {
			String dtPaymode = entry.getKey();
			BlDeposit blDeposit = entry.getValue();
			//收款时此付款方式的支付金额
			BigDecimal oldPaySumAmount = blDeposit.getAmount().abs();
			//收款时整笔结算的患者自付金额（患者自付金额-上次短款金额）
			BigDecimal oldSumAmount = (allAlreadySettleAmount.get("patientsPay").subtract(allAlreadySettleAmount.get("roundAmount"))).abs();
			//本次退给患者自付总额
			BigDecimal refSumAmount = patientsPayRefund.abs();
			//（按比例计算）收款时此付款方式的支付金额 / 收款时整笔结算的患者自付金额 * 本次退给患者自付总额 = 此付款方式需要退患者自费金额
			BigDecimal amount = (oldPaySumAmount.divide(oldSumAmount,4,BigDecimal.ROUND_HALF_DOWN)
					.multiply(refSumAmount).setScale(4, BigDecimal.ROUND_HALF_DOWN)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			blDeposit.setAmount(new BigDecimal(-1).multiply(amount));
			
			//现金
			if(IDictCodeConst.CASH.equals(dtPaymode)) {
				isCash = true;
			}
			
			//患者账户
			if(IDictCodeConst.PATIACCOUNT.equals(dtPaymode)) {
				PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)",
						PiAcc.class, blDeposit.getPkAcc());
				//查询患者账户是否有押金，如有责账户余额-押金
				Double amtAcc = DataBaseHelper.queryForScalar(
				"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
				Double.class, new Object[]{piAcc.getPkPi()});
				if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
					piAcc.setAmtAcc(piAcc.getAmtAcc().subtract(BigDecimal.valueOf(amtAcc)));
					twoModeFee.setAccountPay(blDeposit.getAmount()); // 退到账户的钱，（剩余部分-总部分）
					twoModeFee.setAccountBalance(piAcc.getAmtAcc());//只显示账户余额(不加预退费金额)，成哥要求
				}	
			}
			//比例计算后的应退金额
			rebackAmount = rebackAmount.add(blDeposit.getAmount());
			blDeposits.add(blDeposit);
		}
		//计算比例后的差额
		BigDecimal diffAmount = BigDecimal.ZERO;//差额
		rebackAmount = rebackAmount.abs();//计算后的应退金额
		BigDecimal patientsPay = patientsPayRefund.abs();//实际应退金额
		if(!(rebackAmount.compareTo(patientsPay) == 0)) {
			diffAmount = patientsPay.subtract(rebackAmount);
		}
		if(!(diffAmount.compareTo(BigDecimal.ZERO) == 0)) {
			//如果有现金，差额计算到现金中
			if(isCash) {
				for (BlDeposit blDeposit : blDeposits) {
					if(IDictCodeConst.CASH.equals(blDeposit.getDtPaymode())) {
						blDeposit.setAmount(new BigDecimal(-1).multiply(blDeposit.getAmount().abs().add(diffAmount)));
						break;
					}
				}
			}else {
				//将差额计算到第一笔数据中
				blDeposits.get(0).setAmount(new BigDecimal(-1).multiply(blDeposits.get(0).getAmount().abs().add(diffAmount)));
			}
		}
		//计算后的应退金额加上差额=实际应退金额
		twoModeFee.setRebackAmount(new BigDecimal(-1).multiply(rebackAmount.abs().add(diffAmount)));//应退金额
		twoModeFee.setBlDeposits(blDeposits);
		return twoModeFee;
	}

	/**
	 * 预退 本次结算下的所有结算明细全部(账户退的金额，现金退的金额，银行卡退的明细)
	 * @param pkSettle
	 * @param blBankList
	 * @return Map<String, BlDeposit> 所有退款方式的明细
	 */
	public Map<String, BlDeposit> allSettleRecordsRefund(String pkSettle)
	{
		Map<String,	BlDeposit> refundMap = new HashMap<>();
		// 本次结算下的付款方式
		List<BlDeposit> blDeposits = DataBaseHelper.queryForList("select * from bl_deposit where pk_settle=?",
																      BlDeposit.class, pkSettle);
		for (BlDeposit blDeposit : blDeposits) {
			//如果收退款方式为银行卡的话
			if(blDeposit.getDtPaymode().equals(IDictCodeConst.BANKCARD)){
				//获取银行卡交易信息
				String extSql = "select * from bl_ext_pay where trade_no = ?";
				BlExtPay blBank = DataBaseHelper.queryForBean(extSql, BlExtPay.class, blDeposit.getPayInfo());
				if(blBank!=null){
					blDeposit.setOutTradeNo(blBank.getOutTradeNo());
					blDeposit.setPayResult(blBank.getResultPay());
					blDeposit.setSerialNum(blBank.getSerialNo());
					blDeposit.setExtAmount(blBank.getAmount());
					blDeposit.setBankTime(blBank.getDatePay());
				}
			}else if(blDeposit.getDtPaymode().equals(IDictCodeConst.ALI) ||
					blDeposit.getDtPaymode().equals(IDictCodeConst.WECHAT)){
				String weixinAli = "select * from bl_ext_pay where serial_no = ?";
				BlExtPay blExtPay = DataBaseHelper.queryForBean(weixinAli, BlExtPay.class, blDeposit.getPayInfo());
				if(blExtPay!=null){
					blDeposit.setExtAmount(blExtPay.getAmount());
				}
			}
			refundMap.put(blDeposit.getDtPaymode(), blDeposit);
		}
		return refundMap;
	}

	/**
	 * 本次结算全部结算明细中的总金额，医保支付金额，患者自付金额
	 * @param pkSettle
	 * @param refundMap
	 * @return Map<String, Double>
	 */
	public Map<String, BigDecimal> allAlreadySettleAmount(String pkSettle) {
		Map<String, BigDecimal> allSettleAmount = new HashMap<String, BigDecimal>();
		// 拿到本次结算全部结算明细中的总金额，医保支付金额，患者自付金额
		BlSettle allBlSettle = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle=?",
																		BlSettle.class, pkSettle);
		allSettleAmount.put("aggregateAmount",
										allBlSettle.getAmountSt() == null ? BigDecimal.ZERO : allBlSettle.getAmountSt());// 总金额
		allSettleAmount.put("medicarePayments",
										allBlSettle.getAmountInsu() == null ? BigDecimal.ZERO : allBlSettle.getAmountInsu());// 医保支付金额
		allSettleAmount.put("patientsPay",
										allBlSettle.getAmountPi() == null ? BigDecimal.ZERO :allBlSettle.getAmountPi());// 患者自付金额
		allSettleAmount.put("roundAmount",
				allBlSettle.getAmountRound() == null ? BigDecimal.ZERO :allBlSettle.getAmountRound());// 患者舍入金额

		return allSettleAmount;
	}

	/**
	 * 验证结算明细和收退款金额的总和是否一致
	 * @param allAlreadySettleAmount
	 * @param allDtPayMode
	 */
	private void validatingDirtyRecord(Map<String, BigDecimal> allAlreadySettleAmount, Map<String, BlDeposit> allDtPayMode)
	{
		BigDecimal patientsPay = allAlreadySettleAmount.get("patientsPay");
		if(patientsPay == null){
			patientsPay = BigDecimal.ZERO; //初始化
			if(allDtPayMode.size() > 0){
				throw new BusException("原结算记录中患者自付金额为空，但存在支付方式明细，请联系管理员！");
			}
		}

//		double amount = 0D;
//		for (Entry<String, BlDeposit> dtPayMode : allDtPayMode.entrySet()) {
//			amount += dtPayMode.getValue().getAmount();
//		}
//
//		if(patientsPay.doubleValue() != Math.abs(amount)){
//			throw new BusException("原结算记录中患者自付金额与支付方式明细不相等，请联系管理员！");
//		}
	}

	/**
	 * 当收退款方式只有一种时， 返回的退款信息
	 * 2020-1-11 应收费加入分四舍五入逻辑，所以退费也加入四舍五入处理逻辑（对本次自付退费金额进行分位四舍五入）
	 * @param allAlreadySettleAmount
	 * @param allDtPayMode
	 * @param leftInsSettle
	 * @return OprbFeesDto 一种方式的预退金额分配
	 */
	public OprbFeesDto singleRefundMode(Map<String, BigDecimal> allAlreadySettleAmount, Map<String, BlDeposit> allDtPayMode,
								  OpCgTransforVo leftInsSettle,boolean isNoPratRet)
	{
		OprbFeesDto singleModeFee = partRefundThreeAmount(allAlreadySettleAmount, leftInsSettle);
		BigDecimal patientsPayRefund = singleModeFee.getPatientsPay().setScale(1, RoundingMode.HALF_UP); // 负值！ 本次患者自付的负总金额（进行分位四舍五入）
		
		// 患者自付部分 --账户
		boolean patiAccountFlag = allDtPayMode.containsKey(IDictCodeConst.PATIACCOUNT);
		if(patiAccountFlag){
			BlDeposit blDeposit = allDtPayMode.get(IDictCodeConst.PATIACCOUNT);
			PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)",
															PiAcc.class, blDeposit.getPkAcc());
			//查询患者账户是否有押金，如有责账户余额-押金
			Double amtAcc = DataBaseHelper.queryForScalar(
					"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
					Double.class, new Object[]{piAcc.getPkPi()});
			if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
				piAcc.setAmtAcc(piAcc.getAmtAcc().subtract(BigDecimal.valueOf(amtAcc)));
			}

			// 退账户
			singleModeFee.setAccountPay(patientsPayRefund);
			// 退完后的账户余额
			//singleModeFee.setAccountBalance(piAcc.getAmtAcc().add(patientsPayRefund.abs()));
			singleModeFee.setAccountBalance(piAcc.getAmtAcc());//只显示账户余额(不加预退费金额)，成哥要求
			return singleModeFee;
		}	
		
		//不支持部分退的支付信息按现金退
		if(isNoPratRet) {
			List<BlDeposit> blDeposits = new ArrayList<BlDeposit>();
			BlDeposit blDepo = new BlDeposit();
			blDepo.setDtPaymode(IDictCodeConst.CASH);
			blDepo.setAmount(patientsPayRefund);
			blDeposits.add(blDepo);
			singleModeFee.setRebackAmount(patientsPayRefund);//应退金额
			singleModeFee.setBlDeposits(blDeposits);
			singleModeFee.setNoPratRet(EnumerateParameter.ONE);
			return singleModeFee;
		}
		
		List<BlDeposit> trdBlDeposits = trdBlDeposits(allDtPayMode, BigDecimal.ZERO, patientsPayRefund);
		if(trdBlDeposits!=null && trdBlDeposits.size()>0){
			singleModeFee.setBlDeposits(trdBlDeposits);
		}else{
			singleModeFee.setRebackAmount(patientsPayRefund);
			for(String key : allDtPayMode.keySet()){
				BlDeposit depo = allDtPayMode.get(key);
				depo.setAmount(patientsPayRefund);
				List<BlDeposit> blDeposits = new ArrayList<>();
				blDeposits.add(depo);
				singleModeFee.setBlDeposits(blDeposits);
				break;
			}

			return singleModeFee;
		}
		return singleModeFee;
	}

	/**
	 * 当收退款方式有多种时， 返回的退款信息（按比例分配）（注意：计算时存在差额，需要将差额特殊处理）
	 * 2020-1-11 应收费加入分四舍五入逻辑，所以退费也加入四舍五入处理逻辑（对本次自付退费金额进行分位四舍五入）
	 * @param allAlreadySettleAmount
	 * @param allDtPayMode
	 * @param leftInsSettle
	 * @return OprbFeesDto 多种方式的预退金额分配
	 */
	public OprbFeesDto twoRefundMode(Map<String, BigDecimal> allAlreadySettleAmount, Map<String, BlDeposit> allDtPayMode,
										OpCgTransforVo leftInsSettle,boolean isNoPratRet)
	{
		OprbFeesDto twoModeFee = partRefundThreeAmount(allAlreadySettleAmount, leftInsSettle);
		
		BigDecimal patientsPayRefund = twoModeFee.getPatientsPay().setScale(1, RoundingMode.HALF_UP); // 负值！ 本次患者自付的负总金额（进行分位四舍五入）

		List<BlDeposit> blDeposits = new ArrayList<>();
		BigDecimal rebackAmount = BigDecimal.ZERO;
		boolean isCash = false;
		
		//不支持部分退的支付信息按现金退
		if(isNoPratRet) {
			BlDeposit blDepo = new BlDeposit();
			blDepo.setDtPaymode(IDictCodeConst.CASH);
			blDepo.setAmount(patientsPayRefund);
			blDeposits.add(blDepo);
			twoModeFee.setRebackAmount(patientsPayRefund);//应退金额
			twoModeFee.setBlDeposits(blDeposits);
			twoModeFee.setNoPratRet(EnumerateParameter.ONE);
			return twoModeFee;
		}
		//多种付款方式，按比例分配
		for (Map.Entry<String,BlDeposit> entry : allDtPayMode.entrySet()) {
			String dtPaymode = entry.getKey();
			BlDeposit blDeposit = entry.getValue();
			//收款时此付款方式的支付金额
			BigDecimal oldPaySumAmount = blDeposit.getAmount().abs();
			//收款时整笔结算的患者自付金额（患者自付金额-上次短款金额）
			BigDecimal oldSumAmount = (allAlreadySettleAmount.get("patientsPay").subtract(allAlreadySettleAmount.get("roundAmount"))).abs();
			//本次退给患者自付总额
			BigDecimal refSumAmount = patientsPayRefund.abs();
			//（按比例计算）收款时此付款方式的支付金额 / 收款时整笔结算的患者自付金额 * 本次退给患者自付总额 = 此付款方式需要退患者自费金额
			BigDecimal amount = (oldPaySumAmount.divide(oldSumAmount,4,BigDecimal.ROUND_HALF_DOWN)
					.multiply(refSumAmount).setScale(4, BigDecimal.ROUND_HALF_DOWN)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			blDeposit.setAmount(new BigDecimal(-1).multiply(amount));
			
			//现金
			if(IDictCodeConst.CASH.equals(dtPaymode)) {
				isCash = true;
			}
			
			//患者账户
			if(IDictCodeConst.PATIACCOUNT.equals(dtPaymode)) {
				PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)",
						PiAcc.class, blDeposit.getPkAcc());
				//查询患者账户是否有押金，如有责账户余额-押金
				Double amtAcc = DataBaseHelper.queryForScalar(
				"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
				Double.class, new Object[]{piAcc.getPkPi()});
				if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
					piAcc.setAmtAcc(piAcc.getAmtAcc().subtract(BigDecimal.valueOf(amtAcc)));
					twoModeFee.setAccountPay(blDeposit.getAmount()); // 退到账户的钱，（剩余部分-总部分）
					twoModeFee.setAccountBalance(piAcc.getAmtAcc());//只显示账户余额(不加预退费金额)，成哥要求
				}	
			}
			//比例计算后的应退金额
			rebackAmount = rebackAmount.add(blDeposit.getAmount());
			blDeposits.add(blDeposit);
		}
		//计算比例后的差额
		BigDecimal diffAmount = BigDecimal.ZERO;//差额
		rebackAmount = rebackAmount.abs();//计算后的应退金额
		BigDecimal patientsPay = patientsPayRefund.abs();//实际应退金额
		if(!(rebackAmount.compareTo(patientsPay) == 0)) {
			diffAmount = patientsPay.subtract(rebackAmount);
		}
		if(!(diffAmount.compareTo(BigDecimal.ZERO) == 0)) {
			//如果有现金，差额计算到现金中
			if(isCash) {
				for (BlDeposit blDeposit : blDeposits) {
					if(IDictCodeConst.CASH.equals(blDeposit.getDtPaymode())) {
						blDeposit.setAmount(new BigDecimal(-1).multiply(blDeposit.getAmount().abs().add(diffAmount)));
					}
				}
			}else {
				//将差额计算到第一笔数据中
				blDeposits.get(0).setAmount(new BigDecimal(-1).multiply(blDeposits.get(0).getAmount().abs().add(diffAmount)));
			}
		}
		//计算后的应退金额加上差额=实际应退金额
		twoModeFee.setRebackAmount(new BigDecimal(-1).multiply(rebackAmount.abs().add(diffAmount)));//应退金额
		twoModeFee.setBlDeposits(blDeposits);
		return twoModeFee;
	}

	/*
	 * 1  只有第三方支付方式：集成微信，支付宝，银行卡第三方支付
	 * 2  账户和第三支付支付方式
	 */
	private List<BlDeposit> trdBlDeposits(Map<String, BlDeposit> allDtPayMode, BigDecimal otherModePay, BigDecimal patientsPayRefund) {
		// 银行卡
		boolean bankCardFlag = allDtPayMode.containsKey(IDictCodeConst.BANKCARD);
		BlDeposit trdBlDeposit = null;
		if(bankCardFlag){
			trdBlDeposit = allDtPayMode.get(IDictCodeConst.BANKCARD);
			BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where trade_no = ?",BlExtPay.class,new Object[]{ trdBlDeposit.getPayInfo()});
			if(blExtPay!=null){
				trdBlDeposit.setOutTradeNo(blExtPay.getOutTradeNo());
				trdBlDeposit.setPayResult(blExtPay.getResultPay());
				trdBlDeposit.setSerialNum(blExtPay.getSerialNo());
				trdBlDeposit.setExtAmount(blExtPay.getAmount());
				trdBlDeposit.setBankTime(blExtPay.getDatePay());
			}

		}
		// 微信
		boolean weixinFlag = allDtPayMode.containsKey(IDictCodeConst.WECHAT);
		if(weixinFlag){
			trdBlDeposit = allDtPayMode.get(IDictCodeConst.WECHAT);
		}
		// 支付宝
		boolean aliPayFlag = allDtPayMode.containsKey(IDictCodeConst.ALI);
		if(aliPayFlag){
			trdBlDeposit = allDtPayMode.get(IDictCodeConst.ALI);
		}

		if(otherModePay.compareTo(BigDecimal.ZERO) == 0){
			// 支付方式只有第三方支付
			if(trdBlDeposit!=null){
				trdBlDeposit.setAmount(patientsPayRefund);
			}
		}else if(patientsPayRefund.compareTo(BigDecimal.ZERO) == 0){
			// 支付方式是两种的情况：账户+第三方支付
			BigDecimal lastAmount = trdBlDeposit.getAmount();
			trdBlDeposit.setAmount(otherModePay.subtract(lastAmount)); 
		}

		List<BlDeposit> blDeposits = new ArrayList<>();
		if(trdBlDeposit!=null){
			blDeposits.add(trdBlDeposit);
		}

		return blDeposits;
	}

	/**
	 * 部分退费时，退费总金额，退医保金额，退患者支付金额
	 * @param allAlreadySettleAmount
	 * @param leftInsSettle
	 * @return OprbFeesDto
	 */
	public OprbFeesDto partRefundThreeAmount(Map<String, BigDecimal> allAlreadySettleAmount, OpCgTransforVo leftInsSettle) {
		// 退费是返回的金额是负的
		OprbFeesDto singleModeFee = new OprbFeesDto();

		BigDecimal aggregateAmountRefund =
				leftInsSettle.getAggregateAmount().subtract(allAlreadySettleAmount.get("aggregateAmount"));
		singleModeFee.setAggregateAmount(aggregateAmountRefund);// 要退的总金额

		/**medicarePayments这个在退费预结算里返回时包含了优惠金额，所以单独增加了医保金额amtInsuThird返回**/
		//BigDecimal medicarePaymentsRefund = leftInsSettle.getMedicarePayments().subtract(allAlreadySettleAmount.get("medicarePayments"));
		BigDecimal medicarePaymentsRefund = leftInsSettle.getAmtInsuThird().subtract(allAlreadySettleAmount.get("medicarePayments"));
		singleModeFee.setMedicarePayments(medicarePaymentsRefund);// 要退的医保

		BigDecimal patientsPayRefund =
				leftInsSettle.getPatientsPay().subtract(allAlreadySettleAmount.get("patientsPay"));
		singleModeFee.setPatientsPay(patientsPayRefund);// 退患者自付总额
		singleModeFee.setOldAmtRound(allAlreadySettleAmount.get("roundAmount"));//上次结算现金短款

		return singleModeFee;
	}

	/**
	 * 全退预结算的返回参数
	 * @param allAlreadySettleAmount
	 * @param allDtPayMode
	 * @param oprbFeesDto
	 */
	public OprbFeesDto allRefundDetail(Map<String, BigDecimal> allAlreadySettleAmount, Map<String, BlDeposit> allDtPayMode,
			OprbFeesDto oprbFeesDto,boolean isNoPratRet) {
		BigDecimal aggregateAmountRefund = allAlreadySettleAmount.get("aggregateAmount");
		oprbFeesDto.setAggregateAmount(new BigDecimal(-1).multiply(aggregateAmountRefund.abs())); // 退费总金额
		BigDecimal medicarePaymentsRefund = allAlreadySettleAmount.get("medicarePayments");
		oprbFeesDto.setMedicarePayments(new BigDecimal(-1).multiply(medicarePaymentsRefund.abs())); // 医保退费
		BigDecimal patientsPayRefund = allAlreadySettleAmount.get("patientsPay");
		oprbFeesDto.setPatientsPay(new BigDecimal(-1).multiply(patientsPayRefund.abs())); // 退患者自付

		//多种支付方式
		List<BlDeposit> blDeposits = new ArrayList<BlDeposit>();
		BigDecimal rebackAmount = BigDecimal.ZERO;
		for (Map.Entry<String,BlDeposit>  entry : allDtPayMode.entrySet()) {
			BlDeposit blDeposit = entry.getValue();
			//账户支付
			if(IDictCodeConst.PATIACCOUNT.equals(entry.getKey())) {				
				PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)",
																PiAcc.class, blDeposit.getPkAcc());
				//查询患者账户是否有押金，如有则账户余额-押金
				Double amtAcc = DataBaseHelper.queryForScalar(
						"select sum(deposit) sum_amt from pi_card where pk_pi = ?",
						Double.class, new Object[]{oprbFeesDto.getPkPi()});
				if(amtAcc!=null && amtAcc.compareTo(0D)!=0){
					piAcc.setAmtAcc(piAcc.getAmtAcc().subtract(BigDecimal.valueOf(amtAcc)));
				}
				// 退账户
				BigDecimal account = allDtPayMode.get(IDictCodeConst.PATIACCOUNT).getAmount();
				oprbFeesDto.setAccountPay(new BigDecimal(-1).multiply(account.abs()));
				// 账户余额
				oprbFeesDto.setAccountBalance(piAcc.getAmtAcc());
			}
			BigDecimal amount = blDeposit.getAmount().abs();
			rebackAmount = rebackAmount.add(amount);
			blDeposit.setAmount(new BigDecimal(-1).multiply(amount));
			blDeposits.add(blDeposit);
		}
		
		//是否按现金退
		if(isNoPratRet) {
			List<BlDeposit> depos = new ArrayList<BlDeposit>();
			BlDeposit blDepo = new BlDeposit();
			blDepo.setDtPaymode(IDictCodeConst.CASH);
			blDepo.setAmount(new BigDecimal(-1).multiply(rebackAmount.abs()));
			depos.add(blDepo);
			oprbFeesDto.setRebackAmount(new BigDecimal(-1).multiply(rebackAmount.abs()));//应退金额
			oprbFeesDto.setBlDeposits(depos);
			oprbFeesDto.setNoPratRet(EnumerateParameter.ONE);
			return oprbFeesDto;
		}
		oprbFeesDto.setBlDeposits(blDeposits);
		oprbFeesDto.setRebackAmount(new BigDecimal(-1).multiply(rebackAmount.abs()));//应退金额
		
		return oprbFeesDto;
	}

	/**
	 * 部分退费校验，第三方订单不可部分退费
	 * @param pkSettle
	 */
	private void checkCancelAllSettle(String pkSettle) {
		//深大-健康160支付订单校验
		List<BlExtPay> payList = DataBaseHelper.queryForList("select * from bl_ext_pay where pk_settle=?", BlExtPay.class,pkSettle);
		if(payList != null && payList.size() > 0) {
			if(!CommonUtils.isEmptyString(payList.get(0).getSysname())) {
				String sysname = "";
				switch (payList.get(0).getSysname()) {
					case "Y160":
						sysname = "健康160";
						break;
					case "2500":
						sysname = "自助机";
						break;
					case "2510":
						sysname = "微信公众号";
						break;
					case "2520":
						sysname = "健康160";
						break;
					case "健康网":
						sysname = "健康网";
						break;
					default:
						break;
				}
				if (!CommonUtils.isEmptyString(sysname)) {
					throw new BusException(sysname + "支付订单不支持部分退费！");
				}
			}
		}
	}
	
	
	/**
	 * 个人体检-退费回写
	 */
	private void pushTjRefund(String pkPv, Set<String> ordsnSet) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					// 查询系统前缀
					String itemSql = "select TOP 1 SHORTNAME, NAME from BD_DEFDOC where CODE_DEFDOCLIST=? and CODE=?";
					Map<String, Object> dictMap = DataBaseHelper.queryForMap(itemSql, "BAXTJKQZ", "TJXT_URI");
					if(dictMap!=null && dictMap.containsKey("name")) {
						String url = dictMap.get("name").toString() + "/interface.asmx/AcceptMessage";
						String InterfaceCode = "ReturnPayByHIS";
						String CompanyCode = "peis";
						String MessageID = DateUtils.getDate("yyyyMMddHHmmss") + new Random().nextInt(99999);
						
						String headStr = "<head><InterfaceCode>IFC</InterfaceCode><CompanyCode>CC</CompanyCode><MessageID>MID</MessageID></head>";
						String reqStr = "<hisregisterid>PKPV</hisregisterid>";
						String orderList = "<orderinfo><orderid>CNORDSN</orderid></orderinfo>";
						
						StringBuffer params = new StringBuffer("<message>");
						params.append(headStr.replace("IFC", InterfaceCode).replace("CC", CompanyCode).replace("MID", MessageID));
						params.append("<req>");
						params.append(reqStr.replace("PKPV", pkPv));
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
		
}