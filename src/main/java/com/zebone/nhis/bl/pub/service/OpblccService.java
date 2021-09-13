package com.zebone.nhis.bl.pub.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.OpblccMapper;
import com.zebone.nhis.bl.pub.support.CgProcessUtils;
import com.zebone.nhis.bl.pub.vo.InsuCountVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcCradVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcLeaderVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcPayAndPiVo;
import com.zebone.nhis.bl.pub.vo.OpBlCcVo;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcInv;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlPi;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.support.BlConstant;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊日结账、汇总结账服务类
 * 
 * @author wangpeng
 * @date 2016年10月21日
 *
 */
@Service
public class OpblccService {
	
	@Resource
	private OpblccMapper opblccMapper;
	
	/**
	 * 交易号：007002002002<br>
	 * 根据结账记录获取结账明细<br>
	 * <pre>
	 * 1、结算、账户收退款查询：结算查询 bl_cc_pay 表里eu_paytype=0 ； 账户查询bl_cc_pay 表里eu_paytype=2 ；
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return IpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月21日
	 */
	public OpBlCcVo getIpBlCcVo(String param, IUser user){
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		String pkCc = blCc.getPkCc();
		OpBlCcVo vo = new OpBlCcVo();
		
		//日结账信息,包含发票以及作废信息
		blCc = opblccMapper.getBlCcById(pkCc);
		vo.setBlCc(blCc);
		
		//结算信息
		List<BlCcPay> blCcPayList = opblccMapper.getBlCcPayListByPkCc(pkCc);
		vo.setBlCcPayList(blCcPayList);
		
		//患者账户
		List<BlCcPay> blCcPayPiList = opblccMapper.getBlCcPayPiListByPkCc(pkCc);
		vo.setBlCcPayPiList(blCcPayPiList);
		
		//支付信息
		List<BlSettleDetail> blSettleDetailList = opblccMapper.getBlSettleDetaiListByPkCc(pkCc);
		vo.setBlSettleDetailList(blSettleDetailList);
		
		//挂号信息
		List<InsuCountVo> insuCountVoList = opblccMapper.getInsuCountVoListByPkCc(pkCc);
		if(CollectionUtils.isNotEmpty(insuCountVoList)){
			int cnt = 0;
			int cancekCnt = 0;
			for(InsuCountVo ic : insuCountVoList){
				if(ic.getCnt() != null){
					cnt += ic.getCnt();
				}
				if(ic.getCntCancel() != null){
					cancekCnt += ic.getCntCancel();
				}
			}
			vo.setCnt(cnt);
			vo.setCancekCnt(cancekCnt);
			vo.setInsuCountVoList(insuCountVoList);
		}
		
		/**日结账个性化信息查询，每个项目如果有特定的参数要查询，要在这个分支下写*/
  		Map<String,Object> rtn = CgProcessUtils.processOpOperatorAccount(vo);
  		if(rtn!=null&&"true".equals(rtn.get("enable"))&&rtn.get("result")!=null){
			return (OpBlCcVo)rtn.get("result");
		}
		
		return vo;
	}
	
	/**
	 * 
	 * 交易号：007002002005<br>
	 * 查询已结账业务--挂号记录列表<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月15日
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getBlCcPvEncounterList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		List<Map<String,Object>> mapEnList = new ArrayList<>();
		
		//先查挂号基本信息，再处理挂号费问题
		if(Application.isSqlServer()){
			mapEnList = opblccMapper.getBlCcPvEncounterList(paramMap);
		}else{
			mapEnList = opblccMapper.getBlCcPvListOracle(paramMap);
		}
		
		
//		StringBuffer sql = new StringBuffer("select pv.pk_pv as key_, sum(cg.amount) amt ");
//		sql.append("from pv_encounter pv ");
//		sql.append("inner join pv_op op on pv.pk_pv=op.pk_pv and op.del_flag = '0' ");
//		sql.append("inner join bl_settle st on st.pk_pv = pv.pk_pv and st.del_flag = '0' and st.eu_pvtype < 3 ");
//		sql.append("inner join bl_op_dt cg on cg.pk_settle=st.pk_settle and cg.flag_pv='1' and cg.del_flag = '0' ");
//		sql.append("inner join bl_cc cc on st.pk_cc=cc.pk_cc and cc.del_flag = '0' ");
//		sql.append("where cc.pk_cc = ? ");
//		Object[] paramSql = {paramMap.get("pkCc").toString()};
//		int i = 1;
//		if(paramMap.get("codePv") != null){
//			sql.append("and pv.code_pv = ? ");
//			paramSql = Arrays.copyOf(paramSql, 1+i);
//			paramSql[i] = paramMap.get("codePv").toString();
//			i++;
//		}
//		if(paramMap.get("namePi") != null){
//			sql.append("and pv.name_pi like ? ");
//			paramSql = Arrays.copyOf(paramSql, 1+i);
//			paramSql[i] = "%"+paramMap.get("namePi").toString()+"%";
//		}
//		sql.append("group by pv.pk_pv ");
//		Map<String, Map<String, Object>> sumAmtMap = DataBaseHelper.queryListToMap(sql.toString(), paramSql);
//		
//		for(Map<String,Object> mapEn : mapEnList){
//			if(sumAmtMap.get(mapEn.get("pkPv")) != null){
//				mapEn.putAll(sumAmtMap.get(mapEn.get("pkPv")));
//			}
//		}
		
		return mapEnList;		
	}
	
	/**
	 * 
	 * 交易号：007002002006<br>
	 * 查询未结账详细信息<br>
	 * <pre>
	 * 1、获取起始日期：如果是第一次结账，从bl_settle表取当前用户最小date_st，否则，取上次结账截止时间+1秒；截止日期：参数截止日期；
	 * 2、获取结算金额，医保金额；
	 * 3、查询表bl_deposit获取结算收款和退款：
	 * 4、查询表bl_deposit_pi获取账户收款和退款；
	 * 5、查询表bl_settle、bl_settle_detail获取支付信息（收费结算）；
	 * 6、查询表pv_encounter获取挂号信息；
	 * 7、查询表bl_invoice获取发票以及作废发票信息
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月25日
	 */
	public OpBlCcVo getInfoForIpBlCc(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		Date dateEnd = blCc.getDateEnd();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkEmp", u.getPkEmp());
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", dateEnd));
		mapParam.put("euCctype",blCc.getEuCctype());//结账类型: 0-门诊日结;1-门诊挂号日结;2-门诊收费日结
		
		OpBlCcVo vo = new OpBlCcVo();		
		//获取起始日期
		Date dateBegin;
		BlCc blCcLast = new BlCc();
		if(Application.isSqlServer()){ //是否sqlserver
			blCcLast = opblccMapper.getLastBlCcByUserIdForSql(u.getPkEmp());
		}else{
			blCcLast = opblccMapper.getLastBlCcByUserId(mapParam);
		}
		if(blCcLast != null){
			Date begin = blCcLast.getDateEnd();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(begin);
			calendar.add(Calendar.SECOND, 1);
			dateBegin = calendar.getTime();
			if(dateEnd.getTime() < dateBegin.getTime()){
				throw new BusException("截止时间不能早于上次结账截止时间！");
			}
		}else{
			BlSettle blSettle = new BlSettle();
			if(Application.isSqlServer()){
				blSettle = opblccMapper.getLastBlSettleByUserIdForSql(u.getPkEmp());
			}else{
				blSettle = opblccMapper.getLastBlSettleByUserId(mapParam);
			}
			
			if(blSettle != null){
				dateBegin = blSettle.getDateSt();
				if(dateEnd.getTime() < dateBegin.getTime()){
					throw new BusException("截止时间不能早于最早的结算时间！");
				}
			}else{
				throw new BusException("该操作员不存在结算记录！");
			}
		}
		blCc.setDateBegin(dateBegin);
		
		//结算信息
		String dtStypesql="";
		if("1".equals(mapParam.get("euCctype")))
			dtStypesql="and (st.dt_sttype = '00' or st.dt_sttype = '20')";
		else if("2".equals(mapParam.get("euCctype")))
			dtStypesql="and (st.dt_sttype = '01' or st.dt_sttype = '21')";
		Map<String, Object> settleMap = DataBaseHelper.queryForMap("select nvl(sum(st.amount_st),0) amt_st,"
				+ "nvl(sum(st.amount_insu),0) amt_insu "
				+ "from bl_settle st "
				+ "where del_flag = '0' and st.eu_pvtype in (1,2,4) and (st.flag_cc = '0' or st.flag_cc is null) and "
				+ "st.pk_org =:pkOrg and st.pk_emp_st =:pkEmp and to_char(st.date_st,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr "
				+dtStypesql, mapParam);
		blCc.setAmtSettle(new Double(settleMap.get("amtSt").toString())); //结算金额
		blCc.setAmtInsu(new Double(settleMap.get("amtInsu").toString())); //医保金额		
		//vo.setBlCc(blCc);
		
		//结算收款和退款
		List<BlCcPay> blCcPayList = opblccMapper.getBlDepositNoCcList(mapParam);
		vo.setBlCcPayList(blCcPayList);
		
		//账户收款和退款
		List<BlCcPay> blCcPayPiList = opblccMapper.getBlDepositPiNoCcList(mapParam);
		vo.setBlCcPayPiList(blCcPayPiList);
		
		//支付信息
		List<BlSettleDetail> blSettleDetailList = opblccMapper.getBlSettleDetailNoCcList(mapParam);
		vo.setBlSettleDetailList(blSettleDetailList);
		
		//挂号信息
		List<InsuCountVo> insuCountVoList = opblccMapper.getInsuCountVoList(mapParam);
		if(CollectionUtils.isNotEmpty(insuCountVoList)){
			int cnt = 0;
			int cancekCnt = 0;
			for(InsuCountVo ic : insuCountVoList){
				if(ic.getCnt() != null){
					cnt += ic.getCnt();
				}
				if(ic.getCntCancel() != null){
					cancekCnt += ic.getCntCancel();
				}
			}
			vo.setCnt(cnt);
			vo.setCancekCnt(cancekCnt);
			vo.setInsuCountVoList(insuCountVoList);
		}
		
		//发票信息
		List<BlInvoice> blInvoiceList = opblccMapper.getBlInvoiceGroup(mapParam);
		if(blInvoiceList != null){
			String invInfo = "";
			int cntInv = 0;
			for(BlInvoice bi : blInvoiceList){
				if(bi.getCnt() != null){
					cntInv += bi.getCnt();
				}
				if(!CommonUtils.isEmptyString(bi.getMinCodeInv()) && !CommonUtils.isEmptyString(bi.getMaxCodeInv())){
					invInfo += bi.getMinCodeInv() + "～" + bi.getMaxCodeInv() + " ";
				}
			}
			blCc.setInvInfo(invInfo);
			blCc.setCntInv(cntInv);
			vo.setBlInvoiceList(blInvoiceList);
		}		
		
		//发票作废信息
		List<BlInvoice> blInvoiceCancelList = opblccMapper.getBlInvoiceCancelList(mapParam);
		if(CollectionUtils.isNotEmpty(blInvoiceCancelList)){
			String invInfoCanc = "";
			int cntInvCanc = blInvoiceCancelList.size();
			int i = 0;
			for(BlInvoice bi : blInvoiceCancelList){
				if(!CommonUtils.isEmptyString(bi.getCodeInv())){
					if(i == 0){
						invInfoCanc += bi.getCodeInv();
					}else{
						invInfoCanc += ", " + bi.getCodeInv();
					}
				}
				i++;
			}
			blCc.setCntInvCanc(cntInvCanc);
			blCc.setInvInfoCanc(invInfoCanc);
			vo.setBlInvoiceCancelList(blInvoiceCancelList);
		}
		blCc.setPkEmpOpera(u.getPkEmp());
		blCc.setNameEmpOpera(u.getNameEmp());
		vo.setBlCc(blCc);
		
		
		/**日结账个性化信息查询，每个项目如果有特定的参数要查询，要在这个分支下写*/
  		Map<String,Object> rtn = CgProcessUtils.processOpOperatorAccount(vo);
  		if(rtn!=null&&"true".equals(rtn.get("enable"))&&rtn.get("result")!=null){
			return (OpBlCcVo)rtn.get("result");
		}
		
		return vo;
	}
	
	/**
	 * 
	 * 交易号：007002002007<br>
	 * 保存结账信息<br>
	 * <pre>
	 * 1、获取未结账信息；
	 * 2、保存结账表bl_cc:结账类型eu_cctype=0,操作员pk_emp_opera=:pkEmp,操作员姓名name_emp_opera=nameEmp,操作员结账日期时间date_cc=system,财务交割标志flag_clare=0,组长日结标志flag_leader=0,结账状态eu_status=0;
	 * 3、保存结账付款表bl_cc_pay（结算收付款和患者账户收付款）：操作员结账主键pk_cc=blcc.pk_cc,收付款类型eu_paytype:0结算收付款，2账户收付款;
	 * 4、保存收费结算-操作员结账_票据明细信息表bl_cc_inv：操作员结账主键pk_cc=blcc.pk_cc;作废标志flag_canc，作废发票时为1，否则为0;核销标志flag_wg=0;
	 * 5、更新bl_settle、bl_deposit、bl_deposit_pi、bl_invoice设置结账标志为1,操作员结账主键为blcc.pk_cc;
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月25日
	 */
	public OpBlCcVo saveBlCc(String param, IUser user){
		User u = UserContext.getUser();
		
		//获取未结账信息
		OpBlCcVo vo = getInfoForIpBlCc(param, user);
		
		//保存结账表bl_cc
		String pkCc = NHISUUID.getKeyId(); //患者账户收退款先使用
		BlCc blCc = vo.getBlCc();
		if(blCc == null){
			throw new BusException("该操作员不存在待结算记录！");
		}
		if(CommonUtils.isEmptyString(blCc.getEuCctype())){
			blCc.setEuCctype(BlConstant.CC_EU_CCTYPE_0); //结账类型:0 门诊结账
		}
		blCc.setPkCc(pkCc);

		blCc.setPkEmpOpera(u.getPkEmp());
		blCc.setNameEmpOpera(u.getNameEmp());
		//患者账户收退款
		BigDecimal amtPi = new BigDecimal("0");
		BigDecimal amtPiBack = new BigDecimal("0");
		List<BlCcPay> blCcPayPiList = vo.getBlCcPayPiList();		
		List<BlCcPay> payForInsert = new ArrayList<BlCcPay>();
		if(CollectionUtils.isNotEmpty(blCcPayPiList)){
			for(BlCcPay pay : blCcPayPiList){
				if(pay.getDtPaymode() != null){ //排除收付款方式为空的错误数据
					if(pay.getAmt() != null){
						amtPi = amtPi.add(new BigDecimal(pay.getAmt()));
					}					
					if(pay.getAmtBack() != null){
						amtPiBack = amtPiBack.add(new BigDecimal(pay.getAmtBack()));
					}					

					//操作员结账-付款方式
					pay.setPkCcpay(NHISUUID.getKeyId());
					pay.setPkOrg(u.getPkOrg());
					pay.setPkCc(pkCc);
					pay.setEuPaytype(BlConstant.CCPAY_EU_PAYTYPE_2); //2账户收付款
					pay.setCreator(u.getPkEmp());
					pay.setCreateTime(new Date());
					pay.setModifier(u.getPkEmp());
					pay.setDelFlag("0");
					pay.setTs(new Date());
					payForInsert.add(pay);
				}
			}
		}
		blCc.setAmtPi(amtPi.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		blCc.setAmtPiBack(amtPiBack.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		blCc.setDateCc(new Date());
		blCc.setFlagClear("0");
		blCc.setFlagLeader("0");
		blCc.setEuStatus("0");
		//查詢捨入金額
		if(vo!=null && vo.getBlSettleDetailList()!=null && vo.getBlSettleDetailList().size()>0){
			for(BlSettleDetail stDet : vo.getBlSettleDetailList()){
				if(!CommonUtils.isEmptyString(stDet.getPkInsurance()) 
						&& stDet.getPkInsurance().equals("2")){
					blCc.setAmtRound(stDet.getAmount());
				}
					
			}
		}
		
		DataBaseHelper.insertBean(blCc);
		
		//保存结账付款表bl_cc_pay（结算收付款和患者账户收付款）
		//结算
		List<BlCcPay> blCcPayList = vo.getBlCcPayList();
		if(CollectionUtils.isNotEmpty(blCcPayList)){
			for(BlCcPay pay : blCcPayList){
				if(pay.getDtPaymode() != null){ //排除收付款方式为空的错误数据
					//操作员结账-付款方式
					pay.setPkCcpay(NHISUUID.getKeyId());
					pay.setPkOrg(u.getPkOrg());
					pay.setPkCc(pkCc);
					pay.setEuPaytype(BlConstant.CCPAY_EU_PAYTYPE_0); //0结算收付款
					pay.setCreator(u.getPkEmp());
					pay.setCreateTime(new Date());
					pay.setModifier(u.getPkEmp());
					pay.setDelFlag("0");
					pay.setTs(new Date());
					payForInsert.add(pay);
				}
			}		
		}
		//统一保存，包含结算以及患者账户
		if(payForInsert.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcPay.class), payForInsert);
		}		
		
		//保存结算票据表bl_cc_inv（使用发票和作废发票）
		List<BlInvoice> blInvoiceList = vo.getBlInvoiceList();
		List<BlInvoice> blInvoiceCancelList = vo.getBlInvoiceCancelList(); //作废时flag_cancel=1
		List<BlCcInv> blCcInvlist = new ArrayList<BlCcInv>();
		if(CollectionUtils.isNotEmpty(blInvoiceList)){
			if(CollectionUtils.isNotEmpty(blInvoiceCancelList)){
				blInvoiceList.addAll(blInvoiceCancelList);
			}			
			for(BlInvoice blInv : blInvoiceList){
				BlCcInv inv = new BlCcInv();
				inv.setPkCcinv(NHISUUID.getKeyId());
				inv.setPkOrg(u.getPkOrg());
				inv.setPkCc(pkCc);
				inv.setPkInvcate(blInv.getPkInvcate());
				if("1".equals(blInv.getFlagCancel())){ //作废
					inv.setBeginNo(blInv.getCodeInv());
					inv.setEndNo(blInv.getCodeInv());
					inv.setFlagCanc("1");
				}else{
					inv.setBeginNo(blInv.getMinCodeInv());
					inv.setEndNo(blInv.getMaxCodeInv());
					inv.setFlagCanc("0");
				}				
				inv.setFlagWg("0");
				inv.setCreator(u.getPkEmp());
				inv.setCreateTime(new Date());
				inv.setModifier(u.getPkEmp());
				inv.setDelFlag("0");
				inv.setTs(new Date());				
				blCcInvlist.add(inv);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcInv.class), blCcInvlist);
		}
		
		//更新业务表
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkCc", pkCc);
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));
		mapParam.put("pkEmp", u.getPkEmp());
		mapParam.put("euCctype", blCc.getEuCctype());

		Set<String> pkList = opblccMapper.qryAllPkSt(mapParam);
		if (pkList != null && pkList.size() > 0)
		{
			//更新收费结算-结算记录表bl_settle
			//更新bl_settle表
			String sqlSt = "update bl_settle   set flag_cc = 1,pk_cc =  ? where  flag_cc = 0 and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
			DataBaseHelper.execute(sqlSt.toString(), new Object[]{blCc.getPkCc()});
			//更新bl_deposit表
			String sqlDepo = " update bl_deposit set flag_cc = 1, pk_cc = ? where flag_cc = 0  and pk_settle in ("+CommonUtils.convertSetToSqlInPart(pkList, "pk_settle")+")";
			DataBaseHelper.execute(sqlDepo.toString(), new Object[]{blCc.getPkCc()});
		}

		//更新收费结算-交款记录表bl_deposit_pi
		DataBaseHelper.update("update bl_deposit_pi set flag_cc = '1', pk_cc =:pkCc, modifier =:modifier, ts =:ts where pk_org =:pkOrg and pk_emp_pay =:modifier and (flag_cc = '0' or flag_cc is null) and to_char(date_pay,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ", mapParam);
		
		// 更新收费结算-发票记录表bl_invoice
		Set<String> pkInvs = opblccMapper.qryAllPkInv(mapParam);
		Set<String> pkInvsCancel = opblccMapper.qryAllPkInvCancel(mapParam);

		DataBaseHelper.update(
				"update bl_invoice inv set inv.flag_cc = '1', inv.pk_cc =:pkCc, inv.modifier =:modifier, inv.ts =:ts "
						+ "where inv.del_flag = '0'  and (inv.flag_cc = 0 or inv.pk_cc is null) "
						+ "and pk_emp_inv = :pkEmp and to_char(inv.date_inv,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr "
						+ "and inv.pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvs, "pk_invoice") + ") ",
				mapParam);
		// 更新作废发票记录表bl_invoice
		StringBuffer sqlCancUpd = new StringBuffer(
				"update bl_invoice inv set FLAG_CC_CANCEL = '1',PK_CC_CANCEL=:pkCc ");
		sqlCancUpd.append("where (FLAG_CC_CANCEL='0' or FLAG_CC_CANCEL is null) and pk_org=:pkOrg "
				+ "and PK_EMP_CANCEL =:pkEmp and to_char(inv.date_cancel,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ");

		if ("2".equals(mapParam.get("euCctype"))) {
			sqlCancUpd.append(" and ( inv.pk_invoice in ("
					+ CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ") or inv.note='作废空发票' )");
		} else {
			sqlCancUpd.append(" and inv.pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ")");

		}
		DataBaseHelper.update(sqlCancUpd.toString(), mapParam);

		//更新电子票据信息
		DataBaseHelper.update(
				"update bl_invoice inv set inv.FLAG_CC_EBILL = '1', inv.PK_CC_EBILL =:pkCc, inv.modifier =:modifier, inv.ts =:ts "
						+ "where inv.del_flag = '0'  and (inv.FLAG_CC_EBILL = 0 or inv.PK_CC_EBILL is null) "
						+ "and PK_EMP_EBILL = :pkEmp and to_char(inv.DATE_EBILL,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr "
						+ "and inv.pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvs, "pk_invoice") + ") ",
				mapParam);

		StringBuffer sqlCancUpdEbill = new StringBuffer(
				"update bl_invoice inv set FLAG_CC_CANCEL_EBILL = '1',PK_CC_CANCEL_EBILL=:pkCc ");
		sqlCancUpdEbill.append("where (FLAG_CC_CANCEL_EBILL='0' or FLAG_CC_CANCEL_EBILL is null) and pk_org=:pkOrg "
				+ "and PK_EMP_CANCEL_EBILL =:pkEmp and to_char(inv.DATE_EBILL_CANCEL,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ");

		if ("2".equals(mapParam.get("euCctype"))) {
			sqlCancUpdEbill.append(" and ( inv.pk_invoice in ("
					+ CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ") or inv.note='作废空发票' )");
		} else {
			sqlCancUpdEbill.append(" and inv.pk_invoice in (" + CommonUtils.convertSetToSqlInPart(pkInvsCancel, "pk_invoice") + ")");

		}
		DataBaseHelper.update(sqlCancUpdEbill.toString(), mapParam);

		return vo;
	}
	
	/**
	 * 
	 * 交易号：007002002010<br>
	 * 查询未结账业务--挂号记录列表<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月15日
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getNoBlCcPvEncounterList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		User u = UserContext.getUser();
		paramMap.put("pkOrg", u.getPkOrg());
		paramMap.put("pkEmp", u.getPkEmp());
		
		List<Map<String,Object>> resList = new ArrayList<>();
		//先查挂号基本信息，再处理挂号费问题
		if(Application.isSqlServer()){
			resList = opblccMapper.getNoBlCcPvEncounterList(paramMap);
		}else{
			resList = opblccMapper.getNoBlCcPvListOracle(paramMap);
		}
		
		return resList;		
	}
	
	/**
	 * 
	 * 交易号：007002002011<br>
	 * 取消结账<br>
	 * <pre>
	 * 1、验证是否可以取消结账：只有最后的一次日结账才可以取消；
	 * 2、删除结账相关表：结账付款表bl_cc_pay、结算票据表bl_cc_inv、结账表bl_cc；
	 * 3、更新业务表：结算bl_settle、结算收付款bl_deposit、账户收付款bl_deposit_pi、结算发票（含作废）bl_invoice，把对应的结账标志置为0，结账（作废）主键置为null
	 * </pre>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月25日
	 */
	public void deleteBlCc(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		String pkCc = blCc.getPkCc();
		if(blCc.getEuCctype()==null)
			blCc.setEuCctype("0");
		
		//验证，最后一次才可以取消
		int count = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = ? and cc.del_flag = '0' "
				+ "and to_char(cc.date_end,'yyyy-MM-DD HH24:MI:SS') > (select to_char(blcc.date_end,'yyyy-MM-DD HH24:MI:SS') from bl_cc blcc where blcc.pk_cc = ? and blcc.pk_emp_opera=cc.pk_emp_opera and blcc.eu_cctype = ?)",
				Integer.class, blCc.getEuCctype(),pkCc,blCc.getEuCctype());
		if(count > 0){
			throw new BusException("只能取消最大日期的结账!");
		}
		//只有结账状态的可以取消
		int count_jz = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = ? and cc.del_flag = '0' "
				+ "and cc.eu_status = '0' and cc.pk_cc = ? ", Integer.class, blCc.getEuCctype(),pkCc);
		if(count_jz != 1){
			throw new BusException("只能取消状态为结账的记录!");
		}
		
		//删除结账付款表bl_cc_pay、结算票据表bl_cc_inv、结账表bl_cc
		DataBaseHelper.execute("delete from bl_cc_pay where pk_cc = ?", pkCc);
		DataBaseHelper.execute("delete from bl_cc_inv where pk_cc = ?", pkCc);
		DataBaseHelper.execute("delete from bl_cc where pk_cc = ?", pkCc);
		
		//更新业务表
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkCc", pkCc);
		//结算bl_settle
		DataBaseHelper.update("update bl_settle set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//结算收付款bl_deposit
		DataBaseHelper.update("update bl_deposit set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//账户收付款bl_deposit_pi
		DataBaseHelper.update("update bl_deposit_pi set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//结算发票bl_invoice
		DataBaseHelper.update("update bl_invoice set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
		//结算作废发票bl_invoice
		DataBaseHelper.update("update bl_invoice set flag_cc_cancel = '0', pk_cc_cancel = null, modifier=:modifier, ts=:ts where pk_cc_cancel =:pkCc", mapParam);
		//结算电子发票bl_invoice
		DataBaseHelper.update("update bl_invoice set FLAG_CC_EBILL = '0', PK_CC_EBILL = null, modifier=:modifier, ts=:ts where PK_CC_EBILL =:pkCc ", mapParam);
		//结算电子作废发票bl_invoice
		DataBaseHelper.update("update bl_invoice set FLAG_CC_CANCEL_EBILL = '0', PK_CC_CANCEL_EBILL = null, modifier=:modifier, ts=:ts where PK_CC_CANCEL_EBILL =:pkCc", mapParam);
	}
	
	/**
	 * 
	 * 交易号：007002002012<br>
	 * 查询未汇总结账信息<br>
	 * <pre>
	 * 1、查询时按照 bl_cc.date_end <= 参数截止时间查询
	 * </pre>
	 *  
	 * @param  param
	 * @param  user
	 * @return OpBlCcLeaderVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月27日
	 */
	public OpBlCcLeaderVo getNoOpBlCcLeaderVo(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		//OpBlCcLeaderVo vo = new OpBlCcLeaderVo();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));
		
		//获取开始时间、截止时间、汇总人
		OpBlCcLeaderVo vo = DataBaseHelper.queryForBean("select min(date_begin) date_begin, max(date_end) date_end from bl_cc "
				+ "where del_flag = '0' and eu_cctype = '0' and eu_status = '0' and (flag_leader = '0' or flag_leader is null) "
				+ "and pk_org = ? "
				+ "and to_char(date_end,'yyyy-MM-DD HH24:MI:SS') <= ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd())});
		if(vo.getDateBegin() == null){
			throw new BusException("不存在未汇总结账信息！");
		}
		//获取上此结账信息
		OpBlCcLeaderVo dateBeginVo = DataBaseHelper.queryForBean("select max(date_leader) date_begin from bl_cc "
				+ "where del_flag = '0' and eu_cctype = '0' and eu_status = '1' and flag_leader = '1' "
				+ "and pk_org = ? "
				+ "and to_char(date_end,'yyyy-MM-DD HH24:MI:SS') <= ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd())});
	    if(dateBeginVo.getDateBegin() != null){
	    	long time = dateBeginVo.getDateBegin().getTime();
	    	dateBeginVo.getDateBegin().setTime(time + 1000); 
	    	vo.setDateBegin(dateBeginVo.getDateBegin());
	    }
		
		vo.setDateEnd(blCc.getDateEnd());
		vo.setNameEmpLeader(u.getNameEmp());
		
		//按操作员分组
		List<OpBlCcPayAndPiVo> operList = opblccMapper.getNoBlCcPayGroupByOperaList(mapParam);
		vo.setOperList(operList);
		
		//按付款方式分组
		List<OpBlCcPayAndPiVo> paymodeList = opblccMapper.getNoBlCcPayGroupByPaymodeList(mapParam);
		vo.setPaymodeList(paymodeList);		
		
		return vo;
	}
	
	/**
	 * 
	 * 交易号：007002002013<br>
	 * 保存门诊汇总结账<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return OpBlCcLeaderVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月27日
	 */
	public Map<String, Object> saveBlCcLeader(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		Date ts = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dateLeader", blCc.getDateEnd());
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("modifierName", u.getNameEmp());
		mapParam.put("dateLeader", blCc.getDateEnd());
		mapParam.put("ts", ts);
		mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));
		
		//更新操纵员结账表:bl_cc.date_leader=sysdate
		DataBaseHelper.update("update bl_cc set flag_leader = '1', pk_emp_leader =:modifier, name_emp_leader =:modifierName, date_leader =:dateLeader, eu_status = '1', modifier=:modifier, ts=:ts "
				+ "where pk_org =:pkOrg and eu_cctype = '0' and flag_leader = '0' and to_char(date_end,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr",mapParam);
		
		return map;
	}
	
	/**
	 * 
	 * 交易号：007002002015<br>
	 * 查询已汇总的门诊汇总结账详情<br>
	 * <pre>
	 * 1、查询时按照 bl_cc.date_leader = 参数门诊汇总结账时间查询
	 * </pre>
	 *  
	 * @param  param
	 * @param  user
	 * @return OpBlCcLeaderVo
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月27日
	 */
	public OpBlCcLeaderVo getOpBlCcLeaderVo(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		//OpBlCcLeaderVo vo = new OpBlCcLeaderVo();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkEmpLeader", u.getPkEmp());
		mapParam.put("dateLeaderStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader()));
		
		//获取开始时间、截止时间、汇总人
		OpBlCcLeaderVo vo = DataBaseHelper.queryForBean("select min(date_begin) date_begin, max(date_end) date_end from bl_cc "
				+ "where del_flag = '0' and flag_leader = '1' and eu_cctype = '0' "
				+ "and pk_org = ? "
				+ "and pk_emp_leader = ? "
				+ "and to_char(date_leader,'yyyy-MM-DD HH24:MI:SS') = ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), u.getPkEmp(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader())});
		if(vo.getDateBegin() == null){
			throw new BusException("不存在汇总结账信息！");
		}
		//获取上此结账信息
		OpBlCcLeaderVo dateBeginVo = DataBaseHelper.queryForBean("select max(date_leader) date_begin from bl_cc "
				+ "where del_flag = '0' and flag_leader = '1' and eu_cctype = '0' "
				+ "and pk_org = ? "
				+ "and pk_emp_leader = ? "
				+ "and to_char(date_leader,'yyyy-MM-DD HH24:MI:SS') < ? ", OpBlCcLeaderVo.class, new Object[]{u.getPkOrg(), u.getPkEmp(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader())});
		if(dateBeginVo.getDateBegin() != null){
			long time = dateBeginVo.getDateBegin().getTime();
	    	dateBeginVo.getDateBegin().setTime(time + 1000); 
	    	vo.setDateBegin(dateBeginVo.getDateBegin());
	    }
		//制表人
		vo.setNameEmpLeader(u.getNameEmp());
		vo.setDateEnd(blCc.getDateLeader());
		//按操作员分组
		List<OpBlCcPayAndPiVo> operList = opblccMapper.getBlCcPayGroupByOperaList(mapParam);
		vo.setOperList(operList);
		
		//按付款方式分组
		List<OpBlCcPayAndPiVo> paymodeList = opblccMapper.getBlCcPayGroupByPaymodeList(mapParam);
		vo.setPaymodeList(paymodeList);		
		
		return vo;
	}
	
	/***
	 * 交易号：007002002016<br>
	 * 取消门诊汇总结账<br>
	 * 
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年10月29日
	 */
	public void deleteBlCcLeader(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		
		//校验能否取消汇总结账
		int count = DataBaseHelper.queryForScalar("select count(1) from bl_cc cc "
				+ "where cc.del_flag = '0' and  cc.pk_org = ? and cc.eu_cctype = '0' and cc.flag_leader = '1' and cc.pk_emp_leader = ? and to_char(cc.date_leader,'yyyy-MM-DD HH24:MI:SS') > ?", Integer.class, new Object[]{u.getPkOrg(), u.getPkEmp(), DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader())});
		if(count > 0){
			throw new BusException("只能取消最大日期的汇总结账!");
		}
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkEmpLeader", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("dateLeaderStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateLeader()));
		
		DataBaseHelper.update("update bl_cc "
				+ "set flag_leader = '0', pk_emp_leader = null, name_emp_leader = null, date_leader = null, eu_status = '0', modifier =:pkEmpLeader, ts =:ts "
				+ "where eu_cctype = '0' and flag_leader = '1' and pk_org =:pkOrg and pk_emp_leader =:pkEmpLeader and to_char(date_leader,'yyyy-MM-DD HH24:MI:SS') =:dateLeaderStr ", mapParam);
	
	}
	/**
	 * 非就诊业务结账处理接口
	 * @param param
	 * @param user
	 */
	public OpBlCcCradVo saveNonTreatment(String param, IUser user){
		 BlPi bPi = JsonUtil.readValue(param, BlPi.class);
		 User u = UserContext.getUser();		
		 OpBlCcCradVo oCradVo = getBlCcCrad(param, user);
		 String pkCc = NHISUUID.getKeyId(); //患者账户收退款先使用
		 //2.写结账表bl_cc
		 BlCc blCc = oCradVo.getBlCc();
		    blCc.setPkCc(pkCc);			
			blCc.setEuCctype("51");
			blCc.setDateEnd(bPi.getDateBl());
			blCc.setDateCc(new Date());
			blCc.setFlagClear(EnumerateParameter.ZERO);
			blCc.setFlagLeader(EnumerateParameter.ZERO);
			blCc.setEuStatus(EnumerateParameter.ZERO);
			DataBaseHelper.insertBean(blCc);
			List<BlCcPay> blCcPayPiList = oCradVo.getBlCcPay();		
			List<BlCcPay> payForInsert = new ArrayList<BlCcPay>();
			if(CollectionUtils.isNotEmpty(blCcPayPiList)){
				for(BlCcPay pay : blCcPayPiList){					
						//操作员结账-付款方式
						pay.setPkCcpay(NHISUUID.getKeyId());
						pay.setPkOrg(u.getPkOrg());
						pay.setPkCc(pkCc);
						pay.setEuPaytype("51"); //非就诊结账（结算）
						pay.setCreator(u.getPkEmp());
						pay.setCreateTime(new Date());
						pay.setModifier(u.getPkEmp());
						pay.setDelFlag("0");
						pay.setTs(new Date());
						payForInsert.add(pay);
					}
				}
			//统一保存，包含结算以及患者账户
			if(payForInsert.size() > 0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlCcPay.class), payForInsert);
			}
			//更新业务表
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("pkOrg", u.getPkOrg());
			mapParam.put("modifier", u.getPkEmp());
			mapParam.put("ts", new Date());
			mapParam.put("pkCc", pkCc);
			mapParam.put("dateEndStr", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", blCc.getDateEnd()));									
			//更新收费结算-交款记录表bl_deposit_pi
			DataBaseHelper.update("update bl_deposit_pi set flag_cc = '1', pk_cc =:pkCc, modifier =:modifier, ts =:ts where pk_org =:pkOrg and pk_emp_pay =:modifier and (flag_cc = '0' or flag_cc is null) and to_char(date_pay,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ", mapParam);
			DataBaseHelper.update("update bl_pi set flag_cc = '1', pk_cc =:pkCc where pk_org =:pkOrg and pk_emp_bl =:modifier and flag_cc = 0 and to_char(date_bl,'yyyy-MM-DD HH24:MI:SS') <=:dateEndStr ", mapParam);
			return oCradVo;		
	}
				
	//未结账业务记录
	public OpBlCcCradVo getBlCcCrad(String param, IUser user){
		User u = UserContext.getUser();
		OpBlCcCradVo oCradVo = new OpBlCcCradVo();
		BlPi bPi = JsonUtil.readValue(param, BlPi.class);
		String blPiSql = "select sum(bp.amount) amt, sum(bp.quan) cnt_trade,bp.dt_paymode, bp.eu_butype  from bl_pi bp "
			 		+ " where bp.pk_org = ? and  bp.pk_emp_bl = ? and bp.flag_cc = '0' and bp.date_bl <= ? group by bp.dt_paymode,  bp.eu_butype ";				 
		List<BlCcPay> blccpay = DataBaseHelper.queryForList(blPiSql,BlCcPay.class, u.getPkOrg(),u.getPkEmp(),bPi.getDateBl());
		BlCc blCc = new BlCc();
		Date dateEnd = bPi.getDateBl();
		 Date dateBegin = null;
			BlCc blCcLast = new BlCc();
			if(Application.isSqlServer()){ //是否sqlserver
				blCcLast = DataBaseHelper.queryForBean("select top 1 * from bl_cc "
						+ " where del_flag = '0' and eu_cctype = '51' "
						+ " and pk_emp_opera = ? order by date_end desc ", BlCc.class, u.getPkEmp());
			}else{				
				blCcLast = DataBaseHelper.queryForBean("select t.* from "
						+ "( select * from bl_cc where del_flag = '0' and eu_cctype = '51' and pk_emp_opera = ? order by date_end desc ) t "
						+ " where rownum = 1", BlCc.class, u.getPkEmp());
			}
			if(blCcLast != null){
				Date begin = blCcLast.getDateEnd();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(begin);
				calendar.add(Calendar.SECOND, 1);
				dateBegin = calendar.getTime();
				if(dateEnd.getTime() < dateBegin.getTime()){
					throw new BusException("截止时间不能早于上次结账截止时间！");
				}
			}else{
				BlPi blP = new BlPi();
				if (Application.isSqlServer()) {
					blP = DataBaseHelper.queryForBean(" select top 1 *  from bl_pi where pk_emp_bl = ? and del_flag='0'  order by date_bl  ", BlPi.class, u.getPkEmp());
				}else {
					blP = DataBaseHelper.queryForBean(" select t.*  from (select * from bl_pi where pk_emp_bl = ? and del_flag='0'  order by date_bl) t "
							+ " where rownum = 1  ", BlPi.class, u.getPkEmp());				
				}
				if (blP !=null ) {
					dateBegin = blP.getDateBl();
					if (dateEnd.getTime() < dateBegin.getTime()) {
						throw new BusException("截止时间不能早于最早的结算时间！");
					}
				}else {
					throw new BusException("该操作员不存在收费记录！");
				}				
			}
			blCc.setDateBegin(dateBegin);
			blCc.setDateEnd(bPi.getDateBl());
			blCc.setPkEmpOpera(u.getPkEmp());
			blCc.setNameEmpOpera(u.getNameEmp());
			blCc.setPkOrg(u.getPkOrg());
			oCradVo.setBlCc(blCc);
			oCradVo.setBlCcPay(blccpay);
		return oCradVo;		
	}
	/**
	 * 非就诊业务取消结账
	 * @param param
	 * @param user
	 */
	public void deleteNonBlCc(String param, IUser user){
		User u = UserContext.getUser();
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		String pkCc = blCc.getPkCc();		
		//验证，最后一次才可以取消
		int count = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = '51' and cc.del_flag = '0' "
				+ "and cc.date_cc > (select blcc.date_cc from bl_cc blcc where blcc.pk_cc = ? and blcc.pk_emp_opera=cc.pk_emp_opera and blcc.eu_cctype = '51')", Integer.class, pkCc);
		if(count > 0){
			throw new BusException("只能取消最大日期的结账!");
		}
		//只有结账状态的可以取消
		int count_jz = DataBaseHelper.queryForScalar("select nvl(count(1),0) from bl_cc cc where cc.eu_cctype = '51' and cc.del_flag = '0' "
				+ "and cc.eu_status = '0' and cc.pk_cc = ? ", Integer.class, pkCc);
		if(count_jz != 1){
			throw new BusException("只能取消状态为结账的记录!");
		}
		//删除结账付款表bl_cc_pay、结算票据表bl_cc_inv、结账表bl_cc
		DataBaseHelper.execute("delete from bl_cc_pay where pk_cc = ?", pkCc);
		DataBaseHelper.execute("delete from bl_cc where pk_cc = ?", pkCc);
		//更新业务表
				Map<String, Object> mapParam = new HashMap<String, Object>();
				mapParam.put("pkOrg", u.getPkOrg());
				mapParam.put("modifier", u.getPkEmp());
				mapParam.put("ts", new Date());
				mapParam.put("pkCc", pkCc);			
				//账户收付款bl_deposit_pi
				DataBaseHelper.update("update bl_deposit_pi set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);
	            //非就诊业务记录bl_pi
				DataBaseHelper.update("update bl_pi set flag_cc = '0', pk_cc = null, modifier=:modifier, ts=:ts where pk_cc =:pkCc", mapParam);    
	}
	/**
	 * 根据结账记录显示结账信息
	 * @param param
	 * @param user
	 * @return
	 */
	public OpBlCcCradVo getBlccList(String param, IUser user){
		BlCc blCc = JsonUtil.readValue(param, BlCc.class);
		OpBlCcCradVo oCradVo = new OpBlCcCradVo();
		String pkCc = blCc.getPkCc();
		blCc = DataBaseHelper.queryForBean("select * from bl_cc where pk_cc = ? and eu_cctype = '51' ", BlCc.class, pkCc);
		List<BlCcPay> blccpay = DataBaseHelper.queryForList("select * from BL_CC_PAY where pk_cc = ? and eu_paytype = '51'", BlCcPay.class, pkCc);
		oCradVo.setBlCc(blCc);
		oCradVo.setBlCcPay(blccpay);
	    return oCradVo;			
	}
	/**
	 * 查询已日结结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryBlSettleInfoByCC(String param,IUser user){ 
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return opblccMapper.getBlCcSettleInfo(paramMap);
	}
	/**
	 * 查询未日结结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryBlSettleInfoByUnCC(String param,IUser user){ 
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		return opblccMapper.getNoBlCcSettleInfo(paramMap);
	}
	
	/**
	 * 交易号：007002002008
	 * 未日结中查询收费收退款信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchBlCcInfoUnSum(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		List<Map<String,Object>> resList = new ArrayList<>();
		if(Application.isSqlServer()){
			resList = opblccMapper.searchBlCcUnSum(paramMap);
		}else{
			resList = opblccMapper.searchBlCcUnSumOracle(paramMap);
		}
		return resList;
	}
	
	/**
	 * 交易号：007002002003
	 * 查询已结账业务结算记录列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchBlCcInfoByCc(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		List<Map<String,Object>> resList = new ArrayList<>();
		if(Application.isSqlServer()){
			resList = opblccMapper.searchBlCcByCc(paramMap);
		}else{
			resList = opblccMapper.searchBlCcByCcOracle(paramMap);
		}
		return resList;
	}

	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if (StringUtils.hasText(date)) {
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return temp;
	}
	
}
