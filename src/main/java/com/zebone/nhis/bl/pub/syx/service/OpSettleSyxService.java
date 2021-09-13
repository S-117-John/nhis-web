package com.zebone.nhis.bl.pub.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.bl.pub.support.InvSettltService;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.InvMagService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.support.InvPrintProcessUtils;
import com.zebone.nhis.bl.pub.syx.dao.OpSettleSyxMapper;
import com.zebone.nhis.bl.pub.syx.vo.BlOpDtRefundVo;
import com.zebone.nhis.bl.pub.syx.vo.BlSettleRefundVo;
import com.zebone.nhis.bl.pub.syx.vo.OpGyAmtVo;
import com.zebone.nhis.bl.pub.syx.vo.OpStInfoVo;
import com.zebone.nhis.bl.pub.syx.vo.PiParamVo;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 门诊结算公共服务--syx客户化
 * @author yangxue
 *
 */
@Service
public class OpSettleSyxService {

	@Resource
	private OpSettleSyxMapper opSettleSyxMapper;
	@Autowired
	private OpcgPubHelperService opcgPubHelperService;
	@Autowired
	private BalAccoutService balAccoutService;
	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	@Autowired
	private OpCgPubService opcgPubService; // 门诊计费
	@Autowired
	private CommonService commonService; // 发票信息
	@Autowired
	private PdStOutPubService pdStOutPubService; // 库存预留量
	@Autowired
	private OgCgStrategyPubService ogCgStrategyPubService;
	@Autowired
	private InvMagService invMagService;
	@Autowired
	private InvSettltService invSettltService;

	  /**
	    * 查询门诊收费患者banner信息
	    * 交易码：
	    * @param param{codePv,pkPv}
	    * @param user
	    * @return
	    */
	public List<Map<String,Object>> queryPiBannerInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||(CommonUtils.isNull(paramMap.get("cardNo"))&&CommonUtils.isNull(paramMap.get("codePv"))&&CommonUtils.isNull(paramMap.get("pkPv"))))
			throw new BusException("查询患者信息时未传入codePv或pkPv");
		paramMap.put("curDate",DateUtils.getDateStr(new Date()));
		if(Application.isSqlServer()){
			paramMap.put("dbType", "sqlserver");
		}else{
			paramMap.put("dbType", "oracle");
		}

		List<Map<String,Object>> result =  opSettleSyxMapper.queryPiBannerInfo(paramMap);
		return result;
	}

	/**
	 * 查询有效就诊记录
	 * @param{euPvtype,namePi,mobile,idno,pkDept,codeOp,dateBegin,dateEnd}
	 * @return
	 */
	public List<Map<String,Object>> queryValidPvInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null)
			throw new BusException("查询患者信息时未传入任何查询条件");
		if(CommonUtils.isNotNull(paramMap.get("dateBegin")))
			paramMap.put("dateBegin", paramMap.get("dateBegin").toString()+"000000");
		if(CommonUtils.isNotNull(paramMap.get("dateEnd")))
			paramMap.put("dateEnd", paramMap.get("dateEnd").toString()+"235959");
		List<Map<String,Object>> result =  opSettleSyxMapper.queryPvInfoByCon(paramMap);
		return result;
	}

	/**
	 * 作废发票
	 * @param param
	 * @param user
	 * @return
	 */
	public Long eraseInvoice(String param,IUser user){
		//Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		BillInfo bill = JsonUtil.readValue(param, BillInfo.class);
		if(bill==null|| CommonUtils.isEmptyString(bill.getPkEmpinv())||CommonUtils.isNull(bill.getCurNo()))
			throw new BusException("未获取到参数pkEmpinv及curNo!");
		//校验当前用户是否还有可用发票
		Integer cnt = 0;
		try{
			cnt = DataBaseHelper.queryForScalar("select cnt_use from bl_emp_invoice where pk_empinv = ?", Integer.class, new Object[]{bill.getPkEmpinv()});
		}catch(Exception e){//没有符合条件的查询结果情况
			cnt = -1;
		}
		if(cnt<=0)
			throw new BusException("当前发票领用记录已无可用发票，请重新领用新号段");

		//作废发票，写表bl_invoice
		BlInvoice inv = new BlInvoice();
		inv.setPkInvcate(bill.getPkInvcate());
		inv.setPkEmpinvoice(bill.getPkEmpinv());
		inv.setCodeInv(bill.getCurCodeInv());
		inv.setAmountInv(0D);
		inv.setAmountPi(0D);
		inv.setNote("作废空发票");
		inv.setPrintTimes(0);
		inv.setFlagCancel("1");
		inv.setDateCancel(new Date());
		inv.setPkEmpCancel(UserContext.getUser().getPkEmp());
		inv.setNameEmpCancel(UserContext.getUser().getNameEmp());
		inv.setFlagCc("0");
		inv.setFlagCcCancel("0");
		DataBaseHelper.insertBean(inv);

		//更新发票号码，返回给前台
		DataBaseHelper.update("update bl_emp_invoice set cur_no=cur_no+1,cnt_use=cnt_use-1 where pk_empinv = ? and cur_no = ? ", new Object[]{bill.getPkEmpinv(),bill.getCurNo()});
		Long curNo = CommonUtils.getLong(bill.getCurNo())+1;
		return curNo;
	}
	/**
	 * 修改发票号
	 * @param param{pkEmpinv，modifyNo，invPrefix}
	 * @param user
	 */
	public Long modifyInvoice(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkEmpinv"))||CommonUtils.isNull(paramMap.get("modifyNo")))
			throw new BusException("未获取到参数pkEmpinv、modifyNo!");
		String codeInv = CommonUtils.getString(paramMap.get("invPrefix"))+CommonUtils.getString(paramMap.get("modifyNo"));
		//校验需要修改的发票号是否被使用
		Integer	cnt = DataBaseHelper.queryForScalar("select count(1) from bl_invoice where code_inv = ? and del_flag='0'", Integer.class, new Object[]{codeInv});
		if(cnt>0)
			throw new BusException("当前发票号已被使用，请重新设置！");
		//校验需要修改的发票号是否在票据领用区间
		Integer	valid = DataBaseHelper.queryForScalar("select count(1) from bl_emp_invoice where pk_empinv = ? and begin_no <= ? and end_no >= ?", Integer.class, new Object[]{paramMap.get("pkEmpinv"),paramMap.get("modifyNo"),paramMap.get("modifyNo")});
		if(valid<=0) {
			BlEmpInvoice inv = DataBaseHelper.queryForBean("select * from  bl_emp_invoice where pk_empinv = ?", BlEmpInvoice.class, new Object[]{paramMap.get("pkEmpinv")});
			throw new BusException("当前发票号不在当前领用记录的["+inv.getBeginNo()+"~"+inv.getEndNo()+"]票号区间，请重新设置！");
		}
		//更新当前号为修改的发票号码
		DataBaseHelper.update("update bl_emp_invoice set cur_no=? where pk_empinv = ?", new Object[]{paramMap.get("modifyNo"),paramMap.get("pkEmpinv")});
		return null;
	}
	/**
	 * 查询已结算信息 --退费使用
	 * 交易码：
	 * @param param{pkPv,codeInv,dateBegin,dateEnd}
	 * @param user
	 * @return
	 */
	public List<BlSettleRefundVo> querySettleInfoForRefund(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
//		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkPv")))
//			throw new BusException("未获取到参数pkPv！");
		if(CommonUtils.isNotNull(paramMap.get("dateBegin")))
			paramMap.put("dateBegin", paramMap.get("dateBegin").toString()+"000000");
		if(CommonUtils.isNotNull(paramMap.get("dateEnd")))
			paramMap.put("dateEnd", paramMap.get("dateEnd").toString()+"235959");
		List<BlSettleRefundVo> list = opSettleSyxMapper.querySettleInfoForRefund(paramMap);
		return list;
	}

	/**
	 * 根据结算信息，查询结算费用明细
	 * @param param{pkSettle}
	 * @param user
	 * @return
	 */
	public List<BlOpDtRefundVo> querySettleRefundDetail(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkSettle")))
			throw new BusException("未获取到参数pkSettle！");
		List<BlOpDtRefundVo> list = opSettleSyxMapper.querySettleRefundDetail(paramMap);
		return list;
	}
	/**
	 * 发票重打或发票补打
	 * @param param{printType 1重打(作废),2补打(查询),newInvCode,invalidInvCode,quaryInvCode}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> modifyAndPrintInv(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("printType")))
			throw new BusException("请传入补打或重打对应类型printType");
		String printType = CommonUtils.getString(paramMap.get("printType"));
		String newInvCode = CommonUtils.getString(paramMap.get("newInvCode"));
		String invalidInvCode = CommonUtils.getString(paramMap.get("invalidInvCode"));
		String quaryInvCode = CommonUtils.getString(paramMap.get("quaryInvCode"));
		if("1".equals(printType)&&(CommonUtils.isNull(newInvCode)||CommonUtils.isNull(invalidInvCode)))
			throw new BusException("重打发票时，请传入新发票号及作废票号");
		if("2".equals(printType)&&(CommonUtils.isNull(quaryInvCode)))
			throw new BusException("补打发票时，请传入需要补打的票号");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		//重打（作废旧发票号）
        if("1".equals(printType)){
        	// 根据结算主键查询作废结算时对应的发票
    		BlInvoice blInvoice = DataBaseHelper.queryForBean("select * from BL_INVOICE where code_inv = ?", BlInvoice.class, new Object[]{invalidInvCode});
    		if(blInvoice==null)
	        	throw new BusException("无号码为["+invalidInvCode+"]的发票信息，无法作废重打！");
        	//生成新的发票信息
    		BlInvoice newinv = new  BlInvoice();
	        ApplicationUtils.copyProperties(newinv, blInvoice);
	        newinv.setPkInvoice(null);//删除主键，生成新的主键
	        newinv.setCodeInv(newInvCode);
	        ApplicationUtils.setDefaultValue(newinv, true);
	        DataBaseHelper.insertBean(newinv);
	        //更新发票明细
	        DataBaseHelper.update(" update bl_invoice_dt set pk_invoice = ? where pk_invoice = ?", new Object[]{newinv.getPkInvoice(),blInvoice.getPkInvoice()});
	        //更新发票结算关系表
	        DataBaseHelper.update(" update bl_st_inv set pk_invoice = ? where pk_invoice = ?",new Object[]{newinv.getPkInvoice(),blInvoice.getPkInvoice()});
	        //作废发票信息
    		opcgPubHelperService.updateRefoundBlInvoice(blInvoice);
    		//查询发票信息返回
    		result = opSettleSyxMapper.queryInvoiceDtByCode(newInvCode);
        }
        //补打
	    if("2".equals(printType)){
	    	 result = opSettleSyxMapper.queryInvoiceDtByCode(quaryInvCode);
	    }

	    return result;
	}

	/**
	 * 门诊退费结算--中二
	 * @param
	 * @param user
	 * @return
	 */
	public String refundSettle(String param,IUser user){
		List<BlOpDtRefundVo> refundlist = JsonUtil.readValue(param,new TypeReference<List<BlOpDtRefundVo>>(){});
		if(refundlist==null||refundlist.size()<=0)
			throw new BusException("请选择需要退费的明细！");
		User u = (User)user;
		//本次退费结算对应结算主键
		String pkSettle = refundlist.get(0).getPkSettle();
		String pkPv = refundlist.get(0).getPkPv();
		String pkPi = refundlist.get(0).getPkPi();
		//本次退费结算对应结算明细集合
		List<BlOpDt> alldt = new ArrayList<BlOpDt>();
		//需要退费集合
		List<BlOpDt> rtnlist = new ArrayList<BlOpDt>();
		//需要重新记费结算集合
		List<BlOpDt> cglist = new ArrayList<BlOpDt>();
		alldt = opSettleSyxMapper.queryCgDetailsByPk(refundlist);
		//分组需要退费明细,同时校验是否是同一次结算
		for(BlOpDt dt:alldt){
			if(!pkSettle.equals(dt.getPkSettle()))
				throw new BusException("您选择的退费信息存在多笔结算记录，无法退费！");
			boolean hasFlag = false;
			BlOpDt cgvo = new BlOpDt();//为dt重新创建一个对象用来生成新的记费明细
			ApplicationUtils.copyProperties(cgvo, dt);
			for(BlOpDtRefundVo rtndt:refundlist){
				if(rtndt.getPkCgop().equals(dt.getPkCgop())){
					rtndt.setPkPd(dt.getPkPd());
					dt.setQuanBack(rtndt.getQuanBack());//退费数量
					//生成退费集合
					if("1".equals(rtndt.getFlagCheck())){
						rtnlist.add(dt);
					   //退费数量小于记费数量，重新记费时，未退部分重收
					    if(MathUtils.compareTo(rtndt.getQuan(), rtndt.getQuanBack())>0){
					    	cgvo.setFlagRecharge("1");
					    	cgvo.setQuan(MathUtils.sub(rtndt.getQuan(), rtndt.getQuanBack()));//需要重新记费数量
					    	cgvo.setQuanCg(rtndt.getQuan());//原始记费数量
					    	cgvo.setAmount(MathUtils.mul(dt.getPrice(), cgvo.getQuan()));
					    	cgvo.setAmountHppi(MathUtils.mul(MathUtils.mul(dt.getPrice(), cgvo.getQuan()),dt.getRatioSelf()));
					    	Double amt = MathUtils.sub(cgvo.getAmountHppi(), MathUtils.mul(MathUtils.mul(dt.getPriceOrg(), MathUtils.sub(1D, dt.getRatioDisc())), cgvo.getQuan()));
					    	if(MathUtils.compareTo(amt, 0D)<0 && cgvo.getQuan()>0){
					    		cgvo.setAmountPi(0D);
					    	}else{
					    		cgvo.setAmountPi(amt);
					    	}

					    	cglist.add(cgvo);
					    }
					   hasFlag = true;
					}
					break;
				}
			}
			if(!hasFlag){
				cglist.add(cgvo);
			}
		}
		//1.生成全部退费结算信息
		this.generateRtnSettleInfo(alldt, pkSettle, pkPi, pkPv, u);
		//2.处理已生成执行单信息
		this.processOccInfo(refundlist,alldt, rtnlist,u);
		//3.需要记费记录生成记费未结算记录
		for(BlOpDt dt:cglist){
			dt.setFlagSettle("0");
			dt.setPkSettle("");
			dt.setFlagAcc("0");
			dt.setPkAcc("");
			dt.setFlagRecharge("1");
			dt.setDateCg(new Date());
			dt.setPkCgopBack(null);
			ApplicationUtils.setDefaultValue(dt, true);
		}
		if(cglist!=null&&cglist.size()>0)
		 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), cglist);
		return pkPv;
	}



	/**
	 * 处理已生成执行单信息
	 * @param alldt
	 * @param rtnlist
	 * @param cglist
	 */
	private void processOccInfo(List<BlOpDtRefundVo> refundlist,List<BlOpDt> alldt,List<BlOpDt> rtnlist,User u){
		if(alldt==null||alldt.size()<=0)
			return;
		//全部更新处方明细
		updateExPresOccDt(refundlist);
		//全部更新医技执行单
		updateExAssistOcc(alldt);
		// 更新检查申请单
		updateCnRisApply(alldt);
		// 更新检验申请单
		updateCnLabApply(alldt);
		// 解除全部退费的处方预留量
		relieveReservedAmount(alldt);
		//更新处方执行单状态为取消
		updateExPresOcc(alldt,rtnlist,u);


	}
	/**
	 * 生成退费结算信息(退费时全部充负)
	 * @param alldt
	 * @param pkSettle
	 * @param pkPi
	 * @param pkPv
	 * @param u
	 */
	private void generateRtnSettleInfo(List<BlOpDt> alldt,String pkSettle,String pkPi,String pkPv,User u){
		Map<String,Object> mapParam = new HashMap<String,Object>();
		// 根据结算主键查询结算信息
		mapParam.put("pkSettle", pkSettle);
		BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
		// 生成退费结算信息
		String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle);
		// 生成退费明细
		opcgPubHelperService.generateRefoundRecord(alldt, pkSettleCanc);// 传入新的结算主键
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

		// 根据结算主键查询作废结算时对应的发票
		List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
		// 更新作废发票信息
		if(blInvoices!=null&&blInvoices.size()>0){
			for(BlInvoice inv:blInvoices){
				opcgPubHelperService.updateRefoundBlInvoice(inv);
			}
		}

	}

	/**
	 * 更新处方执行记录
	 * @param blOpDtNegaAll
	 */
	private void updateExPresOcc(List<BlOpDt> blOpDtNegaAll,List<BlOpDt> rtnlist,User u){
		Set<String> presSet = new HashSet<String>();
		Set<String> presPartSet = new HashSet<String>();
		for (BlOpDt blOpDtNega : blOpDtNegaAll) {
			//没有勾选退费的医嘱不取消执行单
			boolean flagCanl = false;
			for(BlOpDt rtndt:rtnlist){
				if(rtndt.getPkCgop().equals(blOpDtNega.getPkCgop())){
					flagCanl = true;
					//存在部分退的处方执行单不取消
					if(MathUtils.compareTo(rtndt.getQuan(), rtndt.getQuanBack())>0){
						presPartSet.add(rtndt.getPkPres());
					}
					break;
				}
			}

			if(flagCanl){
				presSet.add(blOpDtNega.getPkPres());
			}
		}
		//移除含部分退的处方主键
		Set<String> newPresSet = new HashSet<String>();
		for(String pkPres:presSet){
			if(CommonUtils.isEmptyString(pkPres))
				continue;
			if(presPartSet!=null&&presPartSet.size()>0){
				if(!presPartSet.contains(pkPres))
					 newPresSet.add(pkPres);
			}else{
				newPresSet.add(pkPres);
			}
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPress", newPresSet);
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("dateNow", new Date());
		paramMap.put("ts", new Date());
		if(newPresSet!=null && newPresSet.size()>0){
			DataBaseHelper.update("update ex_pres_occ  set ex_pres_occ.eu_status = '9',ex_pres_occ.flag_canc='1',ex_pres_occ.pk_emp_canc=:pkEmp,ex_pres_occ.name_emp_canc=:nameEmp,ex_pres_occ.date_canc=:dateNow,ex_pres_occ.ts=:ts where  "
					+ "  ex_pres_occ.pk_pres in (:pkPress) ", paramMap);
		}
		paramMap.put("IsReturnDrug", "1");
		PlatFormSendUtils.sendCnPresOpMsg(paramMap);
	}
	/**
	 * 更新处方执行明细
	 * @param blOpDtNegaAll
	 */
	private void updateExPresOccDt(List<BlOpDtRefundVo> alldt) {
		Set<String> pkCnords = new HashSet<String>();
 		for (BlOpDtRefundVo dt : alldt) {
			if("1".equals(dt.getFlagPd()))
			     pkCnords.add(dt.getPkCnord());
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
			for (BlOpDtRefundVo blOpDtNega : alldt) {
				if(exPresOccDt.getPkCnord().equals(blOpDtNega.getPkCnord())&&
						exPresOccDt.getPkPd().equals(blOpDtNega.getPkPd())){
					exPresOccDt.setQuanRet(blOpDtNega.getQuanBack());
					exPresOccDt.setOrdsRet(blOpDtNega.getOrds()); // 退费付数1
					exPresOccDt.setAmountRet(new BigDecimal(MathUtils.mul(blOpDtNega.getPrice().doubleValue(), blOpDtNega.getQuanBack())));
				}
			}
		}

		if(exPresOccDts.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExPresOccDt.class), exPresOccDts);
		}
	}
	/**
	 * 更新医疗执行单
	 * @param alldt 全退收费明细列表
	 * @param
	 */
	public void updateExAssistOcc(List<BlOpDt> alldt){
		Set<String> pkCnords = new HashSet<>();
		for (BlOpDt blOpDtNega : alldt) {
			if("0".equals(blOpDtNega.getFlagPd()))
				pkCnords.add(blOpDtNega.getPkCnord());
		}

		List<ExAssistOcc> assistOccs = DataBaseHelper.queryForList(
						"select * from ex_assist_occ where  (flag_refund='0' or flag_refund is null) "
								+ " and pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")",
						ExAssistOcc.class, new Object[] {});
		for (ExAssistOcc assistOcc : assistOccs) {
			//不更新取消标志
			assistOcc.setFlagRefund(EnumerateParameter.ONE);
		}
		if(assistOccs.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExAssistOcc.class), assistOccs);
		}
	}

	/**
	 * 更新检查申请单
	 * @param blOpDtNegaAll
	 */
	private void updateCnRisApply(List<BlOpDt> alldt)
	{
		// 医嘱
		Set<String> pkCnords = new HashSet<>();
		for (BlOpDt blOpDtNega : alldt) {
			if("0".equals(blOpDtNega.getFlagPd())){
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
	private void updateCnLabApply(List<BlOpDt> alldt)
	{
		// 医嘱
		Set<String> pkCnords = new HashSet<>();
		for (BlOpDt blOpDtNega : alldt) {
			if("0".equals(blOpDtNega.getFlagPd())){
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
	 * 解除全部退费的处方中药品对应的预留量
	 * @param blOpDtNegaAll
	 */
	private void relieveReservedAmount(List<BlOpDt> alldt) {
		Set<String> pkPress = new HashSet<>();
		for(BlOpDt blOpDtNega : alldt){
			if(!CommonUtils.isEmptyString(blOpDtNega.getPkPres())){
				pkPress.add(blOpDtNega.getPkPres());
			}
		}
		if(pkPress == null || pkPress.size() <= 0)
			return;
		String pdsql = "select dt.pk_cnord,dt.pk_pd,dt.pk_unit,dt.quan_cg*dt.pack_size as quan_min,dt.quan_cg,dt.pack_size,occ.pk_dept_ex "+
					  " from ex_pres_occ_dt dt inner join ex_pres_occ occ on dt.pk_presocc=occ.pk_presocc "+
			      	  " where occ.pk_pres in ("
			      	  			+ CommonUtils.convertSetToSqlInPart(pkPress, "pk_pres") + ")";
	   List<Map<String,Object>> pdlist = DataBaseHelper.queryForList(pdsql, new Object[]{});

	   if(CollectionUtils.isEmpty(pdlist)){
		   return;
	   }

	   //根据退费明细的执行科室合并物品，统一进行预留量处理
	   refundPdSetPdNum(pdlist);

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
     * 修改患者信息
     * @param param
     * @param user
     */
	public void modifyPiInfo(String param,IUser user){
		PiParamVo pivo = JsonUtil.readValue(param,PiParamVo.class);
		if(pivo==null||CommonUtils.isEmptyString(pivo.getPkPi())||CommonUtils.isEmptyString(pivo.getPkPv()))
			throw new BusException("未传入患者唯一标识pkPi或pkPv");
		pivo.setTs(new Date());
		pivo.setModifier(((User)user).getPkEmp());
		//1.更新患者信息
		opSettleSyxMapper.updatePiMaster(pivo);
		//2.校验医保计划是否有变更
		PvEncounter pvvo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ? ", PvEncounter.class, new Object[]{pivo.getPkPv()});
		//3.更新就诊信息
		opSettleSyxMapper.updatePvEncounter(pivo);
		//4.医保计划变更的情况下，根据医保更新记费明细
		if(!pvvo.getPkInsu().equals(pivo.getPkInsu())||(CommonUtils.isNotNull(pivo.getPkPicate())&&!pvvo.getPkPicate().equals(pivo.getPkPicate()))){
		//更新主医保
		DataBaseHelper.update("update pv_insurance set pk_hp =:pkInsu, modifier =:modifier, ts =:ts where pk_pv =:pkPv and del_flag = '0' ", pivo);

		//TODO 医保变化费用重算
		//TODO 患者分类费用重算
		opcgPubService.updateBlOpDtCgRate(pivo.getPkPv(), pivo.getPkInsu(), pivo.getPkPicate(),pvvo.getPkInsu(),pvvo.getPkPicate());
		}


        //发送患者信息修改ADT^A08
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
        PlatFormSendUtils.sendUpPiInfoMsg(paramMap);
	}
	
    /**
     * 交易号：007002004019
     * 修改就诊信息-修改就诊医保计划
     * @param param
     * @param user
     */
	public void modifyPvInfo(String param,IUser user){
		PiParamVo pivo = JsonUtil.readValue(param,PiParamVo.class);
		if(pivo==null||CommonUtils.isEmptyString(pivo.getPkInsu())||CommonUtils.isEmptyString(pivo.getPkPv())){
			throw new BusException("未传入患者唯一标识pkInsu或pkPv");
		}
		opSettleSyxMapper.updatePvEncounter(pivo);
	}
	/**
     * 查询患者信息
     * @param param{pkPi，pkPv}
     * @param user
     */
	public PiParamVo queryPiInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkPi"))||CommonUtils.isNull(paramMap.get("pkPv")))
			throw new BusException("未传入患者唯一标识pkPi或pkPv");
		return opSettleSyxMapper.queryPiInfo(paramMap);
	}

	/**
	 * 交易号：007002004011
	 * 查询预结算金额数据
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qrySettlementInfo(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		List<BlOpDt> opList = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "opList"),
				new TypeReference<List<BlOpDt>>() {
				});

		if(CommonUtils.isEmptyString(pkPv))
			throw new BusException("未传入患者唯一标识pkPv！");

		PvEncounter pvVo = DataBaseHelper.queryForBean("select * from Pv_Encounter where pk_pv=?", PvEncounter.class, new Object[]{pkPv});
		OpGyAmtVo amtVo = ogCgStrategyPubService.opSyxSettle(pvVo,opList);

		Map<String,Object> retMap = new HashMap<>();
		retMap.put("sumAmount", amtVo.getAmountSt());
		retMap.put("sumAmountPi", amtVo.getAmountPi());

		return retMap;
	}

	/**
	 * 交易号：007002004012
	 * 查询患者诊断是否包含自费诊断
	 * @param param
	 * @param user
	 * @return
	 */
	public List<String> qryZfDiagByPv(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if(CommonUtils.isEmptyString(pkPv))
			throw new BusException("未传入患者唯一标识pkPv！");

		return opSettleSyxMapper.qryZfDiagByPv(pkPv);
	}

	/**
	 * 交易号：007002004013
	 * 查询就诊患者的有效结算记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<OpStInfoVo> qryStInfoByPv(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");

		if(CommonUtils.isEmptyString(pkPv))
			throw new BusException("未传入患者唯一标识pkPv！");

		//获取系统参数Bl0020,门诊发票打印日期范围
		String sysDay = ApplicationUtils.getSysparam("BL0020", false);
		if(CommonUtils.isEmptyString(sysDay))
			sysDay = "30";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pkPv);
		//开始日期
		paramMap.put("dateBegin", DateUtils.addDate(new Date(), -(Integer.valueOf(sysDay)), 3, "yyyyMMddHHmmss"));
		//结束日期
		paramMap.put("dateEnd", DateUtils.getDate("yyyyMMddHHmmss"));

		List<OpStInfoVo> stList = opSettleSyxMapper.qryStInfoByPv(paramMap);
		if(stList!=null && stList.size()>0){
			for(OpStInfoVo stInfo : stList){
				//查询结算对应记费明细
				List<Map<String,Object>> opdtList = DataBaseHelper.queryForList(
						"select unit.name as unit_name,dt.* from bl_op_dt dt inner join bd_unit unit on unit.pk_unit = dt.pk_unit"
						+ " where dt.pk_settle=?",
						new Object[]{stInfo.getPkSettle()});
				stInfo.setOpdtList(opdtList);
			}
		}

		return stList;
	}

	/**
	 * 交易号：007002004014
	 * 保存结算发票信息，并返回发票信息
	 * @param param
	 * @param user
	 */
	public BlInvPrint saveStInvInfo(String param,IUser user){
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		String nameMachine = JsonUtil.getFieldValue(param, "nameMachine");

		if(CommonUtils.isEmptyString(pkSettle))
			throw new BusException("未传入结算唯一标识pkSettle！");

		//查询结算信息
		BlSettle stInfo = DataBaseHelper.queryForBean(
				"select * from bl_settle where pk_settle = ?",
				BlSettle.class, new Object[]{pkSettle});

		/**
		 * 因门诊发票保存逻辑每个项目的规则不一样，故各项目通过配置文件来写自己项目的逻辑分支
		 * */
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("nameMachine", nameMachine); // 本地计算机名称
		paramMap.put("euType", EnumerateParameter.ZERO); // 获取门诊发票
		String strJson = JsonUtil.writeValueAsString(paramMap);

		//校验BL0008参数，参数值为1时查询当前用户所使用的票据信息
		String flagPrtPaper = ApplicationUtils.getSysparam("BL0008",false);

		Map<String, Object> resMap = null;
		if(!CommonUtils.isEmptyString(flagPrtPaper) && "1".equals(flagPrtPaper)){
			resMap = invMagService.searchCanUsedEmpInvoices(strJson, user);
		}

		List<InvoiceInfo> invoiceInfos = new ArrayList<>();
		InvoiceInfo inv = new InvoiceInfo();
		inv.setMachineName(nameMachine);//本地计算机名称
		if(resMap!=null && resMap.size()>0){
			inv.setPkInvcate(CommonUtils.getString(resMap.get("pkInvcate")));// 票据分类主键
			inv.setPkEmpinvoice(CommonUtils.getString(resMap.get("pkEmpinv")));// 票据领用主键
			inv.setCodeInv(CommonUtils.getString(resMap.get("curCodeInv")));
		}
		inv.setFlagPrint("0");
		invoiceInfos.add(inv);

		List<BlInvoice> blInvoices = new ArrayList<>(); // 发票
		List<BlInvoiceDt> blInvoiceDts = new ArrayList<BlInvoiceDt>(); // 发票明细
		List<BlStInv> blStInvs = new ArrayList<BlStInv>(); // 写发票与结算的关系

		Map<String,Object> invMap = InvPrintProcessUtils.saveOpInvInfo(stInfo, invoiceInfos);
		if(invMap==null || invMap.size()<=0){
			throw new BusException("发票信息组织失败，请检查！");
		}

		blInvoices = (List<BlInvoice>)invMap.get("inv");
		blInvoiceDts = (List<BlInvoiceDt>)invMap.get("invDt");
		blStInvs = (List<BlStInv>)invMap.get("stInv");

		if(blInvoices!=null && blInvoices.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), blInvoices); // 批量插入发票
		}
		if(blInvoiceDts!=null && blInvoiceDts.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), blInvoiceDts); // 批量插入发票明细
		}
		if(blStInvs!=null && blStInvs.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), blStInvs); // 批量写发票与结算的关系
		}

		// 更新发票领用表
		String eBillFlag = invSettltService.getBL0031ByNameMachine(invoiceInfos.get(0).getMachineName());

		if(CommonUtils.isEmptyString(eBillFlag) || !"1".equals(eBillFlag)){
			if (blInvoices != null && blInvoices.size() > 0)
			{
				for (BlInvoice invoInfo : blInvoices) {
					// 单张更新
					commonService.confirmUseEmpInv(invoInfo.getPkEmpinvoice(), 1L);
				}
			}
		}

		/**查询发票信息返回*/
		BlInvPrint invPrint = this.qryInvPrint(param, user);

		return invPrint;
	}

	/**
	 * 交易号：
	 * 查询发票信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BlInvPrint qryInvPrint(String param,IUser user){
		Map<String,String> paraMap = JsonUtil.readValue( param, Map.class);
		String pkSettle = paraMap.get("pkSettle");
		BlInvPrint  bllnvPrint = new BlInvPrint();
		//查询住院发票信息
		bllnvPrint = InvPrintProcessUtils.getIpInvPrintDataByPkSettle(pkSettle);
		return  bllnvPrint;
	}

	/**
	 *  交易号：007002004015
	 * 查询门诊结算药品取药药房信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryStDurgDeptEx(String param,IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue( param, Map.class);

		List<Map<String,Object>> retMap = opSettleSyxMapper.qryStDurgDeptEx(paraMap);

		return retMap;
	}

	/**
	 * 获取门诊发票
	 * @param count	需要打印几张发票
	 * @return
	 */
	private List<BillInfo> getInvInfo(String nameMachine,IUser user){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("nameMachine", nameMachine); 	//本地计算机名称
		paramMap.put("euType", EnumerateParameter.ZERO);	//获取住院发票

		String strJson = JsonUtil.writeValueAsString(paramMap);

		List<BillInfo> billList = new ArrayList<>();
		Map<String,Object> resMap = invMagService.searchCanUsedEmpInvoices(strJson,user);

		//获取票据张数
		int count = 1;
		for(int k=0; k<count; k++){
			BillInfo bill = new BillInfo();

			bill.setCurNo(CommonUtils.getInt(resMap.get("curNo")));
			bill.setInvPrefix(resMap.get("invPrefix")!=null?resMap.get("invPrefix").toString():"");
			bill.setPrefix(resMap.get("prefix")!=null?resMap.get("prefix").toString():"");
//			bill.setCntUse(CommonUtils.getInt(resMap.get("cntUse")));
			bill.setPkEmpinv(CommonUtils.getString(resMap.get("pkEmpinv")));
			bill.setPkInvcate(CommonUtils.getString(resMap.get("pkInvcate")));

			String curNo = CommonUtils.getString(bill.getCurNo()+k);

			if(resMap.get("length")!=null){
				bill.setLength(CommonUtils.getInteger(resMap.get("length")));
				StringBuffer sbf = new StringBuffer(curNo);
				for(int i=0; i<bill.getLength()-curNo.length(); i++){
					sbf.insert(0, "0");
				}
				curNo = sbf.toString();
			}

			bill.setCurCodeInv(bill.getPrefix()
					+bill.getInvPrefix()
					+curNo);

			/**校验新获取的发票号是否重复，重复则抛出异常。*/
			int invCount = DataBaseHelper.queryForScalar(
					"select count(1) from bl_invoice where code_inv = ?"
					, Integer.class, bill.getCurCodeInv());

			if(invCount>0)
				throw new BusException("发票号码["+bill.getCurCodeInv()+"]已经被使用，请到票据管理进行修改！");

			billList.add(bill);
		}


		return billList;
	}

	/**
	 * 交易号：007002004016
	 * 根据票据号查询对应结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BlSettle qryStInfoByCodeInv(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue( param, Map.class);

		//根据票据号查询对应结算信息
		BlSettle stInfo = opSettleSyxMapper.qryStInfoByCodeInv(paramMap);
		return stInfo;
	}

	/**
	 * 交易号：007002004017
	 * 查询门诊就诊号编码规则
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryOppvCodeRule(String param,IUser user){
		String code = "0301";
		Map<String,Object> retMap = DataBaseHelper.queryForMap(
				"select * from bd_encoderule where code = ?  and del_flag ='0'"
				, new Object[]{code});
		return retMap;
	}

	/**
	 * 交易号：007002004018
	 * 修改患者就诊状态为结束
	 * @param param
	 * @param user
	 */
	public void upPvStatus(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
    	if(paramMap!=null&&CommonUtils.isNotNull(paramMap.get("pkPv"))){
    		DataBaseHelper.update(" update pv_encounter set eu_locked='0' where pk_pv=:pkPv and eu_locked='1' ", paramMap);
    	}
	}

}
