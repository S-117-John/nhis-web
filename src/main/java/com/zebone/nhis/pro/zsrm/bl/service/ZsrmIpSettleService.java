package com.zebone.nhis.pro.zsrm.bl.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import org.apache.shiro.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.ipcg.support.YBProcessUtils;
import com.zebone.nhis.bl.pub.service.HpService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleAr;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsrm.bl.dao.ZsrmIpSettleMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ZsrmIpSettleService {

	@Autowired
	private PriceStrategyService priceStrategyService;
	@Autowired
	private BalAccoutService balAccoutService;
	@Autowired
	private BlIpPubMapper blIpPubMapper;
	@Autowired
	private InvSettltService invSettltService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private HpService hpService;
	@Autowired
	private PareAccoutService pareAccoutService;

	@Autowired
	private ZsrmIpSettleMapper zsrmIpSettleMapper;

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	/**
	 * add 结算服务-暂为灵璧项目增加，后期修改
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> dealSettleDataLb(String param, IUser user) {

		// 1.参数接收
		SettleInfo allData = JsonUtil.readValue(param, SettleInfo.class);
		// BlDeposit fromSettle = allData.getDeposit();//收退找零金额
		BlDeposit depositAcc = allData.getDepositAcc();// 账户支付金额
		String pkPv = allData.getPkPv();// 就诊主键
		String euSttype = allData.getEuSttype();// 结算类型
		String euStresult = allData.getEuStresult();// 结算结果类型
		String dateEnd = allData.getDateEnd();// 结算截止时间
		String flagHbPrint = allData.getFlagHbPrint();// 是否合并特诊发票
		String dateBegin = "";// 开始时间
		List<InvInfoVo> invoInfos = allData.getInvos();// 发票信息
		List<BlExtPay> blExtPayList = allData.getBlExtPayList();// 第三方支付交易记录 xr+
		List<BlDeposit> depoList = allData.getDepoList();// 收款退款记录-因灵璧项目含笔退款记录，故使用此字段
		List<String> pkDepoList = allData.getPkDepoList();
		String flagPrint = allData.getFlagPrint();// 是否打印纸质票据

		String flagSpItemCg = allData.getFlagSpItemCg();
		List<String> pkCgips = allData.getPkCgips();

		Map<String, Object> mapReturn = new HashMap<>(16);

		PvEncounter pvvo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?", PvEncounter.class,
				pkPv);

		/** 校验患者就诊状态是否是结束 */
		if (EnumerateParameter.ZERO.equals(euSttype) && !EnumerateParameter.TWO.equals(pvvo.getEuStatus()))
			throw new BusException("患者就诊状态发生变化，请刷新页面重新结算！");

		if (CommonUtils.isEmptyString(dateBegin)) {
			dateBegin = dateTrans(pvvo.getDateBegin());
		}
		/** 特殊项目结算逻辑：分解医保支付金额(单位支付、医保支付) */
		if (BlcgUtil.converToTrueOrFalse(flagSpItemCg)) {
			// dateEnd = 出院日期
			dateEnd = DateUtils.dateToStr("yyyyMMddHHmmss", pvvo.getDateEnd());
			// 特殊项目结算时查询有没有关联的退费主键，有则把退费的明细一并结算
			List<String> backList = zsrmIpSettleMapper.qryPkCgBack(pkCgips);
			if (backList != null && backList.size() > 0){
				pkCgips.addAll(backList);
			}
		}

		// yangxue 修改为手动事物
		// 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		BlSettle stVo = null;

		try {

			// 2.结算数据准备 结算明细数据准备
			// 2.1结算数据准备，处理了账户支付后，患者账户相关pi_acc,pi_acc_detial表
			Map<String, Object> stDataMap = settleData(allData, pkPv, euSttype, dateBegin, dateEnd, user, euStresult,
					pkDepoList);
			List<BlSettleDetail> stDtVos = (List<BlSettleDetail>) stDataMap.get("detail");
			stVo = (BlSettle) stDataMap.get("settle");
			for (BlSettleDetail vo : stDtVos) {
				setDefaultValue(vo, true);
				vo.setPkStdt(NHISUUID.getKeyId());
			}
			setDefaultValue(stVo, true);
			// 2.2结算数据写入
			DataBaseHelper.insertBean(stVo);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), stDtVos);

			// 校验系统参数BL0040(0:不结转预交金 1:结转预交金)
			String isStPre = ApplicationUtils.getSysparam("BL0040", false);

			// 3.结算结果类型判断，更新付款表-bl_deposit
			// 3.1更新收款表
			// updDepositInfo(pkPv,stVo.getPkSettle(),dateTrans(dateBegin),dateTrans(dateEnd));
			updDepositInfo(pkPv, stVo.getPkSettle(), dateTrans(dateBegin), dateTrans(dateEnd), pkDepoList, isStPre,
					euSttype);
			// 3.2写入结算时账户支付的收付款记录（结算信息标志已加入）
			if (depositAcc != null) {
				setDepoInfo(depositAcc, stVo);
				depositAcc.setEuDptype(euSttype);
				setDefaultValue(depositAcc, true);
				DataBaseHelper.insertBean(depositAcc);
			}
			// if (blDepositList!=null){
			// for (BlDeposit blDeposit:blDepositList){
			// if ("0".equals(euSttype) && !EnumerateParameter.ONE.equals(euStresult)){//欠款结算不写bl_deposit
			// setDepoInfo(blDeposit,stVo);
			// blDeposit.setEuDptype("0");
			// setDefaultValue(blDeposit, true);
			// DataBaseHelper.insertBean(blDeposit);
			//
			// if (blExtPay!=null&&"1".equals(blDeposit.getEuDirect())){
			// saveBlExtPay(blDeposit.getPkDepo(),blExtPay);
			// }
			// }
			// }
			// mapReturn.put("blDepositList",blDepositList);
			// }

			// 3.3写入结算时，多退少补的收付款记录（结算信息标志已加入）
			if ((depoList != null && depoList.size() > 0) || stVo.getEuStresult().equals(EnumerateParameter.ONE)) {
				if (EnumerateParameter.ZERO.equals(euSttype)) {// 出院结算

					if (EnumerateParameter.TWO.equals(euStresult)) {
						// 3.3.1存款结算
						settleForDeposit(stVo, user, pkPv);// 账户充值过程

						for (BlDeposit fromSettle : depoList) {
							setDepoInfo(fromSettle, stVo);
							fromSettle.setEuDptype(EnumerateParameter.ZERO);
							setDefaultValue(fromSettle, true);
							fromSettle.setDateReptBack(null);
							fromSettle.setPkDepo(NHISUUID.getKeyId());
						}

						DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depoList);
						// DataBaseHelper.insertBean(fromSettle);
					} else if (EnumerateParameter.ONE.equals(euStresult)) {
						// 3.3.2欠款结算
						settleForArrears(stVo);// 欠费记录过程
					} else {// 3.3.3正常结算
						for (BlDeposit fromSettle : depoList) {
							setDepoInfo(fromSettle, stVo);
							// 结算类型
							fromSettle.setEuDptype(EnumerateParameter.ZERO);
							setDefaultValue(fromSettle, true);
							fromSettle.setDateReptBack(null);
							fromSettle.setPkDepo(NHISUUID.getKeyId());
						}
						DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depoList);
						// DataBaseHelper.insertBean(fromSettle);
					}

					List<BlDeposit> dList = new ArrayList<>();
					dList.addAll(depoList);

					mapReturn.put("blDepositList", dList);
				} else if (EnumerateParameter.ONE.equals(euSttype)) {// 中途结算，少收多不退，但是存预交金
					// 参数值为1时，可以将退款金额结转为预交金，故更新票据使用号
					if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals(EnumerateParameter.ONE)) {
						/** 校验是否需要更新发票数据 */
						for (BlDeposit vo : depoList) {
							if (EnumerateParameter.NEGA.equals(vo.getEuDirect())) {
								// 调用发票使用确认服务，完成发票更新。
								commonService.confirmUseEmpInv(depoList.get(0).getPkEmpinvoice(), new Long(1));
								break;
							}
						}
					}

					for (BlDeposit fromSettle : depoList) {
						// 3.3结算类型为中途结算时，如果预交金额合计>结算费用合计，将其差额生成新的预交金记录（eu_dptype=9），写bl_deposit表，并在pk_st_mid字段记录中途结算主键；如果预交金合计<结算费用合计，处理方式同出院结算；
						if (EnumerateParameter.NEGA.equals(fromSettle.getEuDirect())) {// 预交金多了，先退预交金，后转存预交金，要写两条记录
							// 3.3.1 本次结算退多余预交金的记录
							BlDeposit rtn = (BlDeposit) fromSettle.clone();
							setDepoInfo(rtn, stVo);
							rtn.setEuDptype(EnumerateParameter.ONE);
							rtn.setReptNo(null);
							rtn.setPkEmpinv(null);
							setDefaultValue(rtn, true);
							DataBaseHelper.insertBean(rtn);
							// 参数值为1时，退款信息结转为预交金
							if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals(EnumerateParameter.ONE)) {
								// 3.3.2 存一笔预交金的收款记录
								fromSettle.setEuDptype(EnumerateParameter.NINE);
								fromSettle.setEuDirect(EnumerateParameter.ONE);
								fromSettle.setBankNo(null);
								fromSettle.setPayInfo(null);
								fromSettle.setPkStMid(stVo.getPkSettle());
								BigDecimal amt = fromSettle.getAmount();
								fromSettle.setAmount(amt.multiply(new BigDecimal(-1)));
								fromSettle.setNote("中途结算转存");
								fromSettle.setFlagSettle(EnumerateParameter.ZERO);
								fromSettle.setPkSettle(null);
								fromSettle.setDateReptBack(null);
								// 收款时生成流水号0606
								if (CommonUtils.isEmptyString(fromSettle.getCodeDepo()))
									fromSettle.setCodeDepo(ApplicationUtils.getCode("0606"));
							}
						} else {// 预交金少了，收款
							fromSettle.setEuDptype(EnumerateParameter.ONE);
							fromSettle.setEuDirect(EnumerateParameter.ONE);
							setDepoInfo(fromSettle, stVo);
						}
						setDefaultValue(fromSettle, true);
						fromSettle.setDateReptBack(null);
						fromSettle.setPkDepo(NHISUUID.getKeyId());
					}

					List<BlDeposit> dList = new ArrayList<>();
					dList.addAll(depoList);

					mapReturn.put("blDepositList", dList);

					// 参数值为0时，因为退款信息不结转预交金，故过滤掉退款信息
					if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals(EnumerateParameter.ZERO)) {
						for (int i = depoList.size() - 1; i >= 0; i--) {
							if (depoList.get(i).getEuDirect().equals(EnumerateParameter.NEGA))
								depoList.remove(depoList.get(i));
						}
					}
					if (depoList != null && depoList.size() > 0)
						DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depoList);

					// DataBaseHelper.insertBean(fromSettle);
				}

				if (blExtPayList != null && blExtPayList.size() > 0) {
					for (BlExtPay blExtPay : blExtPayList) {
						for (BlDeposit fromSettle : depoList) {
							if ("1".equals(fromSettle.getEuDirect())
									&& fromSettle.getDtPaymode().equals(blExtPay.getEuPaytype())) {
								// 处理第三方支付交易数据
								saveBlExtPay(fromSettle.getPkDepo(), blExtPay);
								break;
							}
						}
					}

				}

			}

			// //3.3写入结算时，多退少补的收付款记录（结算信息标志已加入）
			// if(fromSettle!=null){
			// if(EnumerateParameter.ZERO.equals(euSttype)){//出院结算
			// setDepoInfo(fromSettle, stVo);
			// fromSettle.setEuDptype("0");
			// if(EnumerateParameter.TWO.equals(euStresult)){
			// //3.3.1存款结算
			// settleForDeposit(stVo,user,pkPv);//账户充值过程
			// setDefaultValue(fromSettle, true);
			// fromSettle.setDateReptBack(null);
			// DataBaseHelper.insertBean(fromSettle);
			// }else if(EnumerateParameter.ONE.equals(euStresult)){
			// //3.3.2欠款结算
			// settleForArrears(stVo);//欠费记录过程
			// }else{//3.3.3正常结算
			// setDefaultValue(fromSettle, true);
			// fromSettle.setDateReptBack(null);
			// DataBaseHelper.insertBean(fromSettle);
			// }
			//
			// }else if(EnumerateParameter.ONE.equals(euSttype)){//中途结算，少收多不退，但是存预交金
			//
			// //3.3结算类型为中途结算时，如果预交金额合计>结算费用合计，将其差额生成新的预交金记录（eu_dptype=9），写bl_deposit表，并在pk_st_mid字段记录中途结算主键；如果预交金合计<结算费用合计，处理方式同出院结算；
			// if(EnumerateParameter.NEGA.equals(fromSettle.getEuDirect())){//预交金多了，先退预交金，后转存预交金，要写两条记录
			// //3.3.1 本次结算退多余预交金的记录
			// BlDeposit rtn = (BlDeposit)fromSettle.clone();
			// setDepoInfo(rtn, stVo);
			// rtn.setEuDptype("1");
			// rtn.setReptNo(null);
			// rtn.setPkEmpinv(null);
			// setDefaultValue(rtn, true);
			// DataBaseHelper.insertBean(rtn);
			// //3.3.2 存一笔预交金的收款记录
			// fromSettle.setEuDptype("9");
			// fromSettle.setEuDirect("1");
			// fromSettle.setBankNo(null);
			// fromSettle.setPayInfo(null);
			// fromSettle.setPkStMid(stVo.getPkSettle());
			// BigDecimal amt = fromSettle.getAmount();
			// fromSettle.setAmount(amt.multiply(new BigDecimal(-1)));
			// fromSettle.setNote("中途结算转存");
			// fromSettle.setFlagSettle("0");
			// fromSettle.setPkSettle(null);
			// fromSettle.setDateReptBack(null);
			// }else{//预交金少了，收款
			// fromSettle.setEuDptype("1");
			// fromSettle.setEuDirect("1");
			// setDepoInfo(fromSettle, stVo);
			// }
			// setDefaultValue(fromSettle, true);
			// fromSettle.setDateReptBack(null);
			// DataBaseHelper.insertBean(fromSettle);
			// }
			// if(blExtPay!=null&&fromSettle!=null){
			// //处理第三方支付交易数据
			// saveBlExtPay(fromSettle.getPkDepo(),blExtPay);
			// }
			//
			// } else if(EnumerateParameter.ZERO.equals(euSttype) && EnumerateParameter.ONE.equals(euStresult)){//欠款结算
			// //3.3.2欠款结算
			// settleForArrears(stVo);//欠费记录过程
			// }

			// 处理医保结算业务,根据医保配置文件yb.properties配置医保处理类进行处理相应医保业务
			YBProcessUtils.dealYBSettleMethod(allData, stVo);

			// 4.更新住院记费表
			updChargeInfo(pkPv, stVo.getPkSettle(), dateTrans(dateBegin),
					dateTrans(EnumerateParameter.ZERO.equals(euSttype) ? "20991231235959" : dateEnd));
			if (EnumerateParameter.ZERO.equals(euSttype)) {// 欠款结算到此结束不做发票处理
				if (EnumerateParameter.ONE.equals(euStresult)) {
					DataBaseHelper.execute("update pv_encounter set flag_settle = '1',eu_status = '3' where pk_pv = ? ",
							pvvo.getPkPv());
					mapReturn.put("pkSettle", stVo.getPkSettle());
					platformTransactionManager.commit(status); // 提交事务
					return mapReturn;
				}
			}
			if (invoInfos != null && invoInfos.size() > 0) {
				// 5发票数据准备
				Map<String, Object> invMap = new HashMap<String, Object>(16);

				// 获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
				// String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
				String eBillFlag = invSettltService.getBL0031ByNameMachine(invoInfos.get(0).getNameMachine());
				if (!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)) {
					invMap = invSettltService.eBillOutpatient(pkPv, invoInfos, user, dateBegin, dateEnd,
							stVo.getPkSettle(), flagPrint);
				} else {
					invMap = invSettltService.invoData(pkPv, pvvo.getFlagSpec(), invoInfos, user, dateBegin, dateEnd,
							stVo.getPkSettle(), flagHbPrint);
				}

				List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");
				List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>) invMap.get("invDt");
				// 5.1写发票表bl_invoice和发票明细表bl_invoice_dt
				insertInvo(invo, invoDt);
				// 6.写发票和结算关系表bl_st_inv；
				insertInvoSt(invo, stVo);
				// 7.调用发票使用确认服务，完成发票更新。
				// 新增codeInv(纸质票据号)是否为空，空则不更新票据号
				if (invo != null && invo.size() > 0) {
					for (BlInvoice invInfo : invo) {
						if (!CommonUtils.isEmptyString(invInfo.getCodeInv())) {
							commonService.confirmUseEmpInv(invoInfos.get(0).getInv().getPkEmpinv(), new Long(1));
						}
					}
				}
			}

			// 8.结算结果类型判断，存款结算 调用账户充值服务，将预交金合计-结算金额合计的差额记入患者账户,已处理

			// 9.更新pv表的结算字段；
			if (!EnumerateParameter.ONE.equals(euSttype)) {
				DataBaseHelper.execute("update pv_encounter set flag_settle = '1',eu_status = '3' where pk_pv = ? ",
						pvvo.getPkPv());
			}

			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			throw new BusException("结算失败：" + e.getMessage());
		}
		/* HL7消息发送-数据组装 */
		String amtKind = euSttype.equals(EnumerateParameter.ZERO) ? "3" : "";
		amtKind = euSttype.equals(EnumerateParameter.ONE) ? "2" : "1";
		List<Map<String, Object>> hpMap = getHpPlans(allData.getPkPv());
		String amtType = "";
		for (Map<String, Object> map : hpMap) {
			if (map.get("euHptype").equals(EnumerateParameter.ZERO)) {
				amtType = "01";
			} else if (map.get("euHptype").equals(EnumerateParameter.THREE)) {
				amtType = "03";
			} else {
				amtType = "02";
			}
		}
		User userinfo = (User) user;
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("doCode", userinfo.getCodeEmp());
		paramMap.put("doName", userinfo.getNameEmp());
		if (invoInfos != null && invoInfos.get(0) != null && invoInfos.get(0).getInv() != null) {
			paramMap.put("settleNo", invoInfos.get(0).getInv().getCurCodeInv());
		}
		paramMap.put("totalAmount", allData.getAmountSt());// 总费用
		paramMap.put("selfAmount", allData.getAmountPi());// 自费
		paramMap.put("amtInsu", allData.getAmountInsu());// 医保费用
		paramMap.put("amtType", amtType);// 结算类别
		paramMap.put("amtKind", amtKind);// 结算方式
		paramMap.put("pkPv", allData.getPkPv());// 就诊主键
		paramMap.put("invoInfos", invoInfos);// 发票信息；深大使用
		PlatFormSendUtils.sendBlSettleMsg(paramMap);

		paramMap = null;
		mapReturn.put("pkSettle", stVo.getPkSettle());
		return mapReturn;

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

	/**
	 *
	 * @param pkPv
	 *            患者就诊
	 * @param euSttype
	 *            结算类型 0出院结算，1中途结算
	 * @param
	 * @return
	 */
	public Map<String, Object> settleData(SettleInfo settleInfo, String pkPv, String euSttype, String dateBegin,
										  String dateEnd, IUser user, String euStresult, List<String> pkDepoList) {
		// 1.1查询患者就诊信息
		PvEncounter pvvo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?", PvEncounter.class,
				pkPv);
		Date dateEndTime = dateTrans(dateEnd);
		User userInfo = (User) user;
		BlSettle blSettle = new BlSettle();
		blSettle.setDateBegin(dateTrans(dateBegin));
		blSettle.setPkPv(pkPv);// 当前患者就诊主键
		// 预缴金额AmtPrep
		// 校验系统参数BL0040(0:不结转预交金 1:结转预交金)
		String isStPre = ApplicationUtils.getSysparam("BL0040", false);
		StringBuffer sql = new StringBuffer(
				"select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle='0' and depo.pk_pv = ? ");

		// 系统参数BL0040值为0时，判断有无选择指定预交金信息参与结算，如果有，只更新选择的预交金信息
		if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals("0") && !CommonUtils.isEmptyString(euSttype)
				&& euSttype.equals("1")) {
			if (pkDepoList != null && pkDepoList.size() > 0) {
				sql.append(" and pk_depo in (");
				for (int i = 0; i < pkDepoList.size(); i++) {
					if (i == 0) {
						sql.append("'" + pkDepoList.get(i).trim() + "'");
					} else {
						sql.append(",'" + pkDepoList.get(i).trim() + "'");
					}
				}
				sql.append(" ) ");
			} else {
				sql.append(" and 1=0 ");
			}
		}
		Map<String, Object> amtPrepMap = DataBaseHelper.queryForMap(sql.toString(), pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		blSettle.setAmountPrep(amtPrep);
		// 1.生成结算信息

		blSettle.setPkSettle(NHISUUID.getKeyId());
		// 结算编码
		if (CommonUtils.isEmptyString(blSettle.getCodeSt())) {
			blSettle.setCodeSt(ApplicationUtils.getCode("0605"));
		}
		blSettle.setPkOrg(userInfo.getPkOrg());// 当前机构
		blSettle.setPkPi(pvvo.getPkPi());// 当前患者主键
		blSettle.setEuPvtype("3");// 就诊类型
		blSettle.setPkInsurance(pvvo.getPkInsu());// 患者主医保

		// 结算类型
		if ("0".equals(euSttype)) {
			blSettle.setDtSttype("10"); // 结算类型--出院结算
			blSettle.setDateEnd(pvvo.getDateEnd());
		} else if ("1".equals(euSttype)) {
			blSettle.setDtSttype("11");// 结算类型--中途结算
			blSettle.setDateEnd(dateEndTime);
		} else if ("20".equals(euSttype)) {
			blSettle.setDtSttype("20");// 结算冲账
			blSettle.setDateEnd(dateEndTime);
		}
		blSettle.setEuStresult(euStresult);

		blSettle.setDateSt(new Date());// 当前日期
		blSettle.setPkOrgSt(userInfo.getPkOrg());// 当前机构
		blSettle.setPkDeptSt(userInfo.getPkDept());// 当前部门
		blSettle.setPkEmpSt(userInfo.getPkEmp());// 当前用户
		blSettle.setNameEmpSt(userInfo.getNameEmp());// 当前用户姓名

		blSettle.setAmountRound(settleInfo.getAmtRound() != null ? settleInfo.getAmtRound() : new BigDecimal(0D));
		blSettle.setAmountPrep(settleInfo.getAmountPrep()); // 患者预交金合计
		blSettle.setAmountSt(settleInfo.getAmountSt());// 患者费用合计
		blSettle.setAmountPi(settleInfo.getAmountPi());// 患者自付合计
		// blSettle.setAmountInsu(settleInfo.getAmountInsuThird()==null?BigDecimal.ZERO:settleInfo.getAmountInsuThird());//第三方医保接口返回的医保支付
		blSettle.setAmountInsu(BigDecimal.valueOf(
				MathUtils.sub(settleInfo.getAmountSt().doubleValue(), settleInfo.getAmountPi().doubleValue())));
		BigDecimal amtAcc = settleInfo.getAmountAcc();

		if (amtAcc != null && amtAcc.compareTo(BigDecimal.ZERO) > 0) {
			// 调用患者账户服务
			patiAccountChange(amtAcc, pvvo);
		}

		// 计算特诊总费用
		// 计算特诊总费用
		String begin = dateBegin.substring(0, 8) + "000000";// 格式化开始时间
		Double amountAdd = blIpPubMapper.qryAmountAddByPv(null, pkPv, dateTrans(begin),
				"0".equals(euSttype) ? dateTrans("20991231235959") : dateEndTime);
		blSettle.setAmountAdd(amountAdd);

		blSettle.setFlagCanc("0");
		blSettle.setReasonCanc(null);
		blSettle.setPkSettleCanc(null);
		blSettle.setFlagArclare("0");
		blSettle.setFlagCc("0");
		blSettle.setPkCc(null);
		// 2.生成结算明细 并处理相关金额赋值
		List<BlSettleDetail> detailVos = handleSettleDetailC(pkPv, blSettle, userInfo.getPkOrg(), euSttype, dateBegin,
				dateEnd);
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("settle", blSettle);
		resMap.put("detail", detailVos);
		return resMap;

	}

	public static void setDefaultValue(Object obj, boolean flag) {

		User user = UserContext.getUser();

		Map<String, Object> default_v = new HashMap<String, Object>();
		if (flag) { // 如果新增
			default_v.put("pkOrg", user.getPkOrg());
			default_v.put("creator", user.getPkEmp());
			default_v.put("createTime", new Date());
			default_v.put("delFlag", "0");
		}

		default_v.put("ts", new Date());
		default_v.put("modifier", user.getPkEmp());

		Set<String> keys = default_v.keySet();

		for (String key : keys) {
			Field field = ReflectHelper.getTargetField(obj.getClass(), key);
			if (field != null) {
				ReflectHelper.setFieldValue(obj, key, default_v.get(key));
			}
		}

	}

	public String dateTrans(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = null;
		if (date != null) {
			temp = sdf.format(date);
		}

		return temp;
	}

	private BigDecimal amtTrans(Object amt) {
		if (amt == null) {
			return BigDecimal.ZERO;
		} else {
			return (BigDecimal) amt;
		}
	}

	private void updChargeInfo(String pkPv, String pkSettle, Date begin, Date end) {
		String sql = "update bl_ip_dt set flag_settle = 1, pk_settle = ? where  flag_settle <> 1 and pk_pv = ? and  date_hap >= ? and date_hap <= ?";

		// 格式化开始时间
		String date = dateTrans(begin).substring(0, 8) + "000000";
		Date dateBegin = dateTrans(date);

		DataBaseHelper.execute(sql, pkSettle, pkPv, dateTrans(date), end);

	}

	private void settleForDeposit(BlSettle blSettle, IUser user, String pkPv) {
		// //4.1.1）计算可存款金额 = 患者预交金 - 患者自付金额；
		BigDecimal amountPi = blSettle.getAmountPi() == null ? BigDecimal.ZERO : blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep() == null ? BigDecimal.ZERO : blSettle.getAmountPrep();
		BigDecimal deposit = amountPrep.subtract(amountPi);
		// 4.1.2）生成支付数据：
		BlDeposit blDepo = new BlDeposit();
		blDepo.setEuDirect("-1");
		blDepo.setEuDptype("0");
		blDepo.setPkPi(blSettle.getPkPi());
		blDepo.setPkPv(blSettle.getPkPv());
		blDepo.setAmount(deposit);
		blDepo.setDtPaymode("5");
		blDepo.setDatePay(new Date());
		blDepo.setPkDept(blSettle.getPkDeptSt());
		blDepo.setPkEmpPay(blSettle.getPkEmpSt());
		blDepo.setNameEmpPay(blSettle.getNameEmpSt());
		blDepo.setFlagAcc("0");
		blDepo.setFlagSettle("1");
		blDepo.setNote("余额存入患者账户");
		blDepo.setPkDepo(NHISUUID.getKeyId());
		blDepo.setFlagReptBack("0");
		blDepo.setEuPvtype("3");
		DataBaseHelper.insertBean(blDepo);
		// 4.1.3）生成存款数据：
		BlDepositPi blDepoPi = new BlDepositPi();
		blDepoPi.setPkDepopi(NHISUUID.getKeyId());
		blDepoPi.setPkPi(blSettle.getPkPi());
		blDepoPi.setEuDirect("1");
		blDepoPi.setAmount(deposit);// 存款金额
		blDepoPi.setDtPaymode("5");// （内部转账）
		blDepoPi.setDatePay(new Date());// 当前日期
		blDepoPi.setPkDept(blSettle.getPkDeptSt());// 当前部门
		blDepoPi.setPkEmpPay(blSettle.getPkEmpSt());// 当前用户
		blDepoPi.setNameEmpPay(blSettle.getNameEmpSt());// 当前用户姓名
		blDepoPi.setReptNo("");
		blDepoPi.setPkSettle(blSettle.getPkSettle());
		blDepoPi.setNote("存款结算转入");
		balAccoutService.saveMonOperation(blDepoPi, user, pkPv, null, blDepoPi.getDtPaymode());

	}

	private void settleForArrears(BlSettle blSettle) {
		// //4.2.1计算欠款金额 = 患者自付金额 – 患者预交金。
		BigDecimal amountPi = blSettle.getAmountPi() == null ? BigDecimal.ZERO : blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep() == null ? BigDecimal.ZERO : blSettle.getAmountPrep();
		BigDecimal deposit = amountPi.subtract(amountPrep);
		// 4.2.2生成欠费数据：
		BlSettleAr vo = new BlSettleAr();
		vo.setAmtAr(deposit.doubleValue());
		vo.setAmtPay(0.0);
		vo.setFlagCl("0");
		vo.setPkOrg(blSettle.getPkOrg());
		vo.setPkSettle(blSettle.getPkSettle());
		vo.setPkSettlear(NHISUUID.getKeyId());
		vo.setDatePay(new Date());
		vo.setPkEmpPay(blSettle.getPkEmpSt());
		vo.setNameEmpPay(blSettle.getNameEmpSt());
		DataBaseHelper.insertBean(vo);

	}

	/**
	 * 处理外部接口支付数据
	 *
	 * @param pkDepo
	 * @param blExtPay
	 */
	private void saveBlExtPay(String pkDepo, BlExtPay blExtPay) {
		String sql = "update BL_EXT_PAY set PK_DEPO= ?  where PK_EXTPAY = ?  ";

		DataBaseHelper.update(sql, new Object[] { pkDepo, blExtPay.getPkExtpay() });
	}

	public void insertInvo(List<BlInvoice> invo, List<BlInvoiceDt> invoDt) {
		for (BlInvoice vo : invo) {
			setDefaultValue(vo, true);
		}
		for (BlInvoiceDt vo : invoDt) {
			setDefaultValue(vo, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), invo);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), invoDt);
	}

	public void insertInvoSt(List<BlInvoice> invos, BlSettle stVo) {
		for (BlInvoice invo : invos) {
			BlStInv vo = new BlStInv();
			vo.setPkInvoice(invo.getPkInvoice());
			vo.setPkOrg(invo.getPkOrg());
			vo.setPkSettle(stVo.getPkSettle());
			vo.setPkStinv(NHISUUID.getKeyId());
			setDefaultValue(vo, true);
			DataBaseHelper.insertBean(vo);
		}

	}

	private List<Map<String, Object>> getHpPlans(String PkPv) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		// 1.pv_insurance表里的医保
		StringBuffer sql = new StringBuffer("select bh.pk_payer,insu.sort_no,bh.eu_hptype,insu.pk_hp,insu.flag_maj");
		sql.append(" from pv_insurance insu");
		sql.append(" inner join  bd_hp bh");
		sql.append(" on insu.pk_hp = bh.pk_hp ");
		sql.append("  where insu.pk_pv = ?");
		sql.append(" order by insu.sort_no asc,insu.flag_maj desc");
		res.addAll(DataBaseHelper.queryForList(sql.toString(), PkPv));
		return res;
	}

	private void setDepoInfo(BlDeposit fromSettle, BlSettle stVo) {
		fromSettle.setPkDepo(NHISUUID.getKeyId());
		fromSettle.setFlagSettle("1");
		fromSettle.setEuPvtype("3");
		fromSettle.setPkSettle(stVo.getPkSettle());
		fromSettle.setCodeDepo(ApplicationUtils.getCode("0606"));
	}

	private void updDepositInfo(String pkPv, String pkSettle, Date begin, Date end, List<String> pkDepoList,
								String isStPre, String euSttype) {
		// 成伟要求：操作bl_deposit表只使用pk_pv字段查询。
		StringBuffer sql = new StringBuffer(
				"update bl_deposit set flag_settle = '1', pk_settle = ? where eu_dptype = '9' and flag_settle <> 1 and pk_pv = ? ");
		// 系统参数BL0040值为0时，判断有无选择指定预交金信息参与结算，如果有，只更新选择的预交金信息
		if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals("0") && !CommonUtils.isEmptyString(euSttype)
				&& euSttype.equals("1")) {
			if (pkDepoList != null && pkDepoList.size() > 0) {
				sql.append(" and pk_depo in (");
				for (int i = 0; i < pkDepoList.size(); i++) {
					if (i == 0) {
						sql.append("'" + pkDepoList.get(i).trim() + "'");
					} else {
						sql.append(",'" + pkDepoList.get(i).trim() + "'");
					}
				}
				sql.append(" ) ");
			} else {
				sql.append(" and 1=0 ");
			}
		}
		DataBaseHelper.execute(sql.toString(), pkSettle, pkPv);
	}

	private void patiAccountChange(BigDecimal amtAcc, PvEncounter pvvo) {
		BlDeposit dp = new BlDeposit();
		dp.setPkPi(pvvo.getPkPi());
		dp.setEuDirect("-1");
		dp.setAmount(amtAcc.multiply(new BigDecimal(-1)));
		dp.setDtPaymode("4");
		dp.setPkEmpPay(UserContext.getUser().getPkEmp());
		dp.setNameEmpPay(UserContext.getUser().getNameEmp());
		piAccDetailVal(dp);
	}

	private List<BlSettleDetail> handleSettleDetailC(String PkPv, BlSettle blSettle, String pkOrg, String euSttype,
													 String begin, String end) {
		Map<String, BlSettleDetail> res = new HashMap<String, BlSettleDetail>();
		// 1.查询记费表的数据，组织记费阶段的数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkPv", PkPv);
		params.put("dateBegin", begin);
		params.put("dateEnd", end);
		params.put("euSttype", euSttype);
		List<BlIpDt> cgVos = blIpPubMapper.qryCgIps(params);
		Double amtDisc = 0.0;
		Double amtHp = 0.0;
		Double patiSelf = 0.0;
		for (BlIpDt vo : cgVos) {
			Double amtHpTemp = 0.0;
			if (1.0 != vo.getRatioSelf()) { // 计算内部优惠金额
				// Double price = MathUtils.sub(vo.getPriceOrg(), vo.getPrice());
				// amtHpTemp= MathUtils.mul(price, vo.getQuan());

				amtHpTemp = MathUtils.sub(vo.getAmount(), vo.getAmountPi());
				amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
			} else if (1.0 != vo.getRatioDisc()) {// 计算内部医保报销金额
				// amtHpTemp = MathUtils.mul(MathUtils.mul(vo.getQuan(), vo.getPrice()), MathUtils.sub(1.0, vo.getRatioSelf()));

				amtHpTemp = MathUtils.sub(vo.getAmount(), vo.getAmountPi());
				amtDisc = MathUtils.add(amtDisc, amtHpTemp);
			}
			// amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
			// amtDisc =MathUtils.add(amtDisc, MathUtils.sub(MathUtils.sub(vo.getAmount(),vo.getAmountPi()),amtHpTemp));
			patiSelf = MathUtils.add(patiSelf, vo.getAmountPi());
		}

		PvEncounter pvvo = DataBaseHelper.queryForBean("Select * from pv_encounter where pk_pv = ?", PvEncounter.class,
				PkPv);
		List<BlSettleDetail> resvo = new ArrayList<BlSettleDetail>();

		// 2.1处理患者优惠的记费明细数据
		if (amtDisc > 0) {
			BlSettleDetail voDisc = new BlSettleDetail();
			voDisc.setAmount(amtDisc.doubleValue());
			StringBuffer sqlCate = new StringBuffer(
					" select picate.pk_hp, bh.pk_payer, bh.eu_hptype from pi_cate picate");
			sqlCate.append(" inner join bd_hp bh on bh.pk_hp = picate.pk_hp ");
			sqlCate.append(" where picate.pk_picate = ?");
			List<Map<String, Object>> discHpMap = DataBaseHelper.queryForList(sqlCate.toString(), pvvo.getPkPicate());
			String pkPayer = "";
			String pkHp = "";
			if (discHpMap != null && discHpMap.size() > 0) {
				Map<String, Object> tempDisc = discHpMap.get(0);
				if (tempDisc.get("pkPayer") != null) {
					pkPayer = (String) tempDisc.get("pkPayer");
				}
				if (tempDisc.get("pkHp") != null) {
					pkHp = (String) tempDisc.get("pkHp");
				}
			}
			voDisc.setPkSettle(blSettle.getPkSettle());
			voDisc.setPkPayer(pkPayer);
			voDisc.setPkInsurance(pkHp);
			res.put(pkHp, voDisc);
		}
		List<Map<String, Object>> hpPlans = getHpPlans(PkPv);// 1.获取患者的医保列表
		if (hpPlans == null || hpPlans.size() <= 0) {

			resvo.addAll(res.values());
			return resvo;
		}
		Map<String, Object> majorHp = null;
		for (Map<String, Object> map : hpPlans) {
			String flagMaj = "";
			if (map.get("flagMaj") != null) {
				flagMaj = (String) map.get("flagMaj");
			}
			if ("1".equals(flagMaj)) {
				majorHp = map;
				break;
			}
		}

		// 2.2处理内部医保的记费明细数据
		if (amtHp > 0 && majorHp != null) {
			BlSettleDetail voHp = new BlSettleDetail();
			voHp.setAmount(amtHp.doubleValue());
			voHp.setPkSettle(blSettle.getPkSettle());
			String pkPayer = (majorHp != null && majorHp.get("pkPayer") != null) ? (String) majorHp.get("pkPayer") : "";
			String pkHp = (majorHp != null && majorHp.get("pkHp") != null) ? (String) majorHp.get("pkHp") : "";
			voHp.setPkPayer(pkPayer);
			voHp.setPkInsurance(pkHp);
			res.put(pkHp, voHp);
		}

		// 3.循环处理内部医保的结算策略和<=3的各类型医保
		for (Map<String, Object> map : hpPlans) {
			// 3.1数据准备
			String pkPayer = (map != null && map.get("pkPayer") != null) ? (String) map.get("pkPayer") : "";
			String pkHp = (map != null && map.get("pkHp") != null) ? (String) map.get("pkHp") : "";

			int EuHptype = -1;
			if (map.get("euHptype") != null) {
				EuHptype = Integer.parseInt((String) map.get("euHptype"));
			}

			// 3.2处理结算策略的各类数据
			if (EuHptype > 3) {// 该情况下可出现4,9
				BigDecimal amountInnerD = priceStrategyService.qryPatiSettlementAmountAllocationInfo(
						new BigDecimal(patiSelf), pvvo.getPkOrg(), Constant.IP, pkHp);
				if (patiSelf > amountInnerD.doubleValue()) {
					// 返回的自付金额要是比当前的小，则是有分摊的，故写入一些明细记录
					BlSettleDetail voInner = null;
					Double hpTemp = MathUtils.sub(patiSelf, amountInnerD.doubleValue());
					if (res.get(pkHp) != null) {
						voInner = res.get(pkHp);
						voInner.setAmount(MathUtils.add(voInner.getAmount(), hpTemp));
					} else {
						voInner = new BlSettleDetail();
						voInner.setAmount(hpTemp);
						voInner.setPkSettle(blSettle.getPkSettle());
						voInner.setPkInsurance(pkHp);
						voInner.setPkPayer(pkPayer);
					}
					res.put(pkHp, voInner);
					patiSelf = amountInnerD.doubleValue();
				}
			} else {
				Map<String, Object> hpMap = hpService.getHpInfo(patiSelf, map);
				BigDecimal amtHp3 = hpMap.get("amtHps") == null ? BigDecimal.ZERO : (BigDecimal) hpMap.get("amtHps");
				if (amtHp3.compareTo(BigDecimal.ZERO) > 0) {
					BlSettleDetail voHp3 = null;
					if (res.get(pkHp) != null) {
						voHp3 = res.get(pkHp);
						voHp3.setAmount(MathUtils.add(amtHp3.doubleValue(), voHp3.getAmount()));
						// 给主表赋值
						blSettle.setAmountInsu(amtHp3.add(blSettle.getAmountInsu()));
					} else {
						voHp3 = new BlSettleDetail();
						voHp3.setAmount(amtHp3.doubleValue());
						voHp3.setPkPayer(pkPayer);
						voHp3.setPkSettle(blSettle.getPkSettle());
						voHp3.setPkInsurance(pkHp);
						// 给主表赋值
						blSettle.setAmountInsu(amtHp3);
					}
					res.put(pkHp, voHp3);
					patiSelf = MathUtils.sub(patiSelf, amtHp3.doubleValue());
				}
			}

		}

		// 4.结算时患者自付金额。
		BlSettleDetail voSelf = new BlSettleDetail();
		voSelf.setAmount(patiSelf.doubleValue());
		voSelf.setPkSettle(blSettle.getPkSettle());
		voSelf.setPkInsurance(null);
		// 当保险计划为空时，付款方应该也为空
		voSelf.setPkPayer(qryBdPayerByEuType(pkOrg));
		res.put("pkSelf", voSelf);

		// 5.给主表赋值
		// blSettle.setAmountPi(new BigDecimal(patiSelf).setScale(2, BigDecimal.ROUND_HALF_UP));//不保留两位小数，保存的时候会报错
		resvo.addAll(res.values());

		return resvo;
	}

	public static void piAccDetailVal(BlDeposit dp) {

		String getPiA = "Select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)";
		PiAcc pa = DataBaseHelper.queryForBean(getPiA, PiAcc.class, dp.getPkPi());
		if (pa != null && EnumerateParameter.ONE.equals(pa.getEuStatus())) {
			if (pa.getAmtAcc() == null || "".equals(pa.getAmtAcc())) {
				pa.setAmtAcc(BigDecimal.ZERO);
			}
			BigDecimal amtAcc = pa.getAmtAcc().add(dp.getAmount());
			if (amtAcc.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusException("信用额度小于0！");
			} else {
				pa.setAmtAcc(amtAcc);
			}
			DataBaseHelper.updateBeanByPk(pa, false);
			PiAccDetail pad = new PiAccDetail();
			pad.setPkPiacc(pa.getPkPiacc());
			pad.setPkPi(pa.getPkPi());
			pad.setDateHap(new Date());
			pad.setEuOptype(EnumerateParameter.ONE);
			pad.setEuDirect(dp.getEuDirect());
			pad.setAmount(dp.getAmount());
			pad.setPkDepopi(null);
			pad.setAmtBalance(pa.getAmtAcc());
			pad.setPkEmpOpera(dp.getPkEmpPay());
			pad.setNameEmpOpera(dp.getNameEmpPay());
			DataBaseHelper.insertBean(pad);
		} else {
			throw new BusException("该账户已冻结或已被删除，不可收退款");
		}

	}

	private String qryBdPayerByEuType(String pkOrg) {
		String pkPayer = "";
		Map<String, Object> map = DataBaseHelper.queryForMap("select pk_payer from bd_payer where eu_type='0' ");
		if (map != null) {
			pkPayer = (String) map.get("pkPayer");
		}
		return pkPayer;
	}

	/**
	 * @param param DateBegin:'开始日期'DateEnd:'结束日期' PkPv:'就诊主键'
	 * @param user
	 * @return 结算金额  预交金额? 医保支付 患者支付 账户支付 结算应收
	 */
	@SuppressWarnings("unused")
	public Map<String, Object> GetAmtInfo(String param, IUser user) {

		Map<String, Object> res = new HashMap<String, Object>();
		User userinfo = (User) user;
		@SuppressWarnings("unchecked")
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String) paraMap.get("pkPv");
		//String dateBegin =(String)paraMap.get("dateBegin");
		String dateBegin = CommonUtils.getString(paraMap.get("dateBegin"))
				.substring(0, 8) + "000000";
		Date begin = dateTrans(dateBegin);
		String dateEnd = (String) paraMap.get("dateEnd");
		Date end = dateTrans(dateEnd);
		String euSttype = (String) paraMap.get("euSttype");
		Double amtInsuThird = Double.valueOf(paraMap.get("amtInsuThird") == null ? "0" : paraMap.get("amtInsuThird").toString());// xr+  第三方医保有前端传过来
		//定义一些公共变量
		StringBuffer sqlSt;
		Map<String, Object> amtStMap;
		BigDecimal amtSt;//结算金额AmtSt
		//String cgips = paraMap.get("pkCgips").toString().substring(paraMap.get("pkCgips").toString().indexOf("[")+1,paraMap.get("pkCgips").toString().lastIndexOf("]"));
		List<String> cgipsArgs = (List<String>) paraMap.get("pkCgips");
		// 本次結算的預交金主鍵
		List<String> pkDepoList = (List<String>) paraMap.get("pkDepoList");

		// 校验是否是出院结算，如果是出院结算则把end赋值为20991231235959
		if (!CommonUtils.isEmptyString(euSttype) && "10".equals(euSttype)) {
			end = dateTrans("20991231235959");
		}

		//String[] cgipsArgs = cgips.split(",");
		if (cgipsArgs != null && cgipsArgs.size() > 0) {
			String pkCgips = "";
			int i = 0;
			for (String pkcgip : cgipsArgs) {
				if (i == 0) {
					pkCgips += "'" + pkcgip.trim() + "'";
				} else {
					pkCgips += ",'" + pkcgip.trim() + "'";
				}
				i++;
			}
			//结算金额AmtSt
			sqlSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' and blip.pk_cgip in (" + pkCgips + ")");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv, begin, end);
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			//冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv

			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1 and blip.pk_cgip in (" + pkCgips + ")");
			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			Map<String, Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv, begin, end);
			BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
			res.put("AmtStrikeInv", strikeInvStAmt);
		} else {
			//结算金额AmtSt
			sqlSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' ");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv, begin, end);
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			//冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv

			StringBuffer strikeInvSt = new StringBuffer(" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1  ");
			strikeInvSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			Map<String, Object> strikeInvStMap = DataBaseHelper.queryForMap(strikeInvSt.toString(), pkPv, begin, end);
			BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
			res.put("AmtStrikeInv", strikeInvStAmt);
		}
		// 预缴金额AmtPrep
		// 成伟要求：操作bl_deposit表只使用pk_pv字段查询。
		// StringBuffer sqlPrep = new
		// StringBuffer("select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and  depo.pk_pv = ? and depo.date_pay >= ? and depo.date_pay <= ?");
		StringBuffer sqlPrep = new StringBuffer(
				"select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.eu_dptype=9 and depo.flag_settle = '0' and  depo.pk_pv = ? ");
		String isStPre = ApplicationUtils.getSysparam("BL0040", false);
		if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals("0")
				&& !CommonUtils.isEmptyString(euSttype)
				&& euSttype.equals("11")) {
			if (pkDepoList != null && pkDepoList.size() > 0) {
				res.put("pkDepoList", pkDepoList);
				sqlPrep.append(" and depo.pk_depo in (");
				for (int i = 0; i < pkDepoList.size(); i++) {
					if (i == 0) {
						sqlPrep.append("'" + pkDepoList.get(i).trim() + "'");
					} else {
						sqlPrep.append(",'" + pkDepoList.get(i).trim() + "'");
					}
				}
				sqlPrep.append(" ) ");
			} else {
				sqlPrep.append(" and 1=0 ");
			}
		}

		Map<String, Object> amtPrepMap = DataBaseHelper.queryForMap(sqlPrep.toString(), pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		res.put("AmtPrep", amtPrep);
		//医保支付	第三方医保报销金额 + 内部医保报销金额；
		//第三方医保报销金额由第三方医保接口返回；AmtInsu
		BlSettle tempSt = new BlSettle();
		List<BlSettleDetail> details = handleSettleDetailC(pkPv,
				tempSt, userinfo.getPkOrg(), euSttype, dateBegin, dateEnd, cgipsArgs);
		Double amtTemp = 0.0;
		Double amtPiTemp = 0.0;
		String payerSelf = qryBdPayerByEuType(userinfo.getPkOrg());
		for (BlSettleDetail vo : details) {
			if (!payerSelf.equals(vo.getPkPayer())) {
				amtTemp += vo.getAmount();
			} else {
				//res.put("AmtPi", vo.getAmount()-amtInsuThird);
				amtPiTemp += vo.getAmount();
			}
		}
		BigDecimal amtPi = new BigDecimal(amtPiTemp - amtInsuThird);
		amtPi = amtPi.setScale(2, BigDecimal.ROUND_HALF_UP);
		res.put("AmtPi", amtPi.compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal(0) : amtPi);

		BigDecimal amtInsu = new BigDecimal(amtTemp + amtInsuThird);
		amtInsu = amtInsu.setScale(2, BigDecimal.ROUND_HALF_UP);
		res.put("AmtInsu", amtInsu);
		if (tempSt.getAmountInsu() != null && tempSt.getAmountInsu().compareTo(BigDecimal.ZERO) != 0) {
			res.put("AmtInsuThird", tempSt.getAmountInsu());
		} else {
			res.put("AmtInsuThird", BigDecimal.ZERO);
		}
//		List<Map<String,Object>> hpPlans = getHpPlans(pkPv);
//		for(Map<String,Object> map : hpPlans){
//			String flagMaj = (String)map.get("flagMaj");
//			if("1".equals(flagMaj)){
//				res.put("PkInsurance", map.get("pkHp"));
//			}
//		}

		//获取患者主医保(注释上面代码，目前存在PV_INSURANCE表和PV_ENCOUNTER表医保不一致问题，故直接获取PV_ENCOUNTER的pk_insu)
		String pkInsu = DataBaseHelper.queryForScalar(
				"select pk_insu from PV_ENCOUNTER where pk_pv = ?",
				String.class, new Object[]{pkPv});
		res.put("PkInsurance", pkInsu);

		//账户余额,授权账户的余额也可以使用
		//Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc,pk_piacc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", pkPv);
		PiAcc account = pareAccoutService.getPatiAccountAvailableBalanceByPv(pkPv);
		BigDecimal amtAcc = BigDecimal.ZERO;
		String pkAcc = null;
		if (account != null && !PiConstant.ACC_EU_STATUS_2.equals(account.getEuStatus())) {//存在账户且未冻结的情况
			amtAcc = account.getAmtAcc() == null ? BigDecimal.ZERO : account.getAmtAcc();
			pkAcc = account.getPkPiacc();
		}
		res.put("pkAcc", pkAcc);
		//患者支付	本次结算金额合计-预交金额-医保支付；AmtPi =AmtSt - AmtPrep -AmtInsu
		BigDecimal amtGet = amtSt.subtract(amtPrep).subtract(amtInsu);
		amtGet = amtGet.setScale(2, BigDecimal.ROUND_HALF_UP);

		if (amtGet.compareTo(BigDecimal.ZERO) < 0) {//预交金够，患者不用支付，账户支付为0
			res.put("AmtAcc", BigDecimal.ZERO);
			res.put("AmtGet", amtGet);
			return set2Scal(res);
		} else {//预交金不够
			if (amtAcc.compareTo(BigDecimal.ZERO) > 0) {//如果患者账户有钱，先用
				amtGet = amtGet.subtract(amtAcc);
				if (amtGet.compareTo(BigDecimal.ZERO) < 0) {//账户有钱且足以支付应交费用，还需支付剩下的
					res.put("AmtAcc", amtSt.subtract(amtPrep).subtract(amtInsu));
					res.put("AmtGet", BigDecimal.ZERO);
					return set2Scal(res);
				} else {//账户有钱，但不足以支付应交费用，还需支付0
					res.put("AmtAcc", amtAcc);
					res.put("AmtGet", amtGet);
					return set2Scal(res);
				}
			} else {//如果患者账户没钱，账户支付为0
				res.put("AmtAcc", BigDecimal.ZERO);
				res.put("AmtGet", amtGet);
				return set2Scal(res);
			}
		}
	}

	private Map<String, Object> set2Scal(Map<String, Object> map) {
		if (map != null) {
			for (Map.Entry<String, Object> e : map.entrySet()) {
				String key = e.getKey();
				if (e.getValue() != null) {
					if (isNumber(e.getValue().toString())) {
						BigDecimal val = new BigDecimal(e.getValue().toString());
						map.put(key, val.setScale(2, BigDecimal.ROUND_HALF_UP));
					} else {
						map.put(key, e.getValue());
					}
				} else {
					map.put(key, e.getValue());
				}
			}
		}
		return map;
	}

	public static boolean isNumber(String str) {
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}

	private List<BlSettleDetail> handleSettleDetailC(String PkPv, BlSettle blSettle, String pkOrg, String euSttype,
													 String begin, String end, List<String> cgipsArgs) {
		Map<String, BlSettleDetail> res = new HashMap<String, BlSettleDetail>();
		// 1.查询记费表的数据，组织记费阶段的数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkPv", PkPv);
		params.put("dateBegin", begin);
		params.put("dateEnd", end);
		params.put("euSttype", euSttype);
		params.put("pkCgips", cgipsArgs);
		List<BlIpDt> cgVos = blIpPubMapper.qryCgIps(params);
		Double amtDisc = 0.0;
		Double amtHp = 0.0;
		Double patiSelf = 0.0;
		for (BlIpDt vo : cgVos) {
			Double amtHpTemp = 0.0;
			if (1.0 != vo.getRatioSelf()) { // 计算内部优惠金额
				// Double price = MathUtils.sub(vo.getPriceOrg(), vo.getPrice());
				// amtHpTemp= MathUtils.mul(price, vo.getQuan());

				amtHpTemp = MathUtils.sub(vo.getAmount(), vo.getAmountPi());
				amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
			} else if (1.0 != vo.getRatioDisc()) {// 计算内部医保报销金额
				// amtHpTemp = MathUtils.mul(MathUtils.mul(vo.getQuan(), vo.getPrice()), MathUtils.sub(1.0, vo.getRatioSelf()));

				amtHpTemp = MathUtils.sub(vo.getAmount(), vo.getAmountPi());
				amtDisc = MathUtils.add(amtDisc, amtHpTemp);
			}
			// amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
			// amtDisc =MathUtils.add(amtDisc, MathUtils.sub(MathUtils.sub(vo.getAmount(),vo.getAmountPi()),amtHpTemp));
			patiSelf = MathUtils.add(patiSelf, vo.getAmountPi());
		}

		PvEncounter pvvo = DataBaseHelper.queryForBean("Select * from pv_encounter where pk_pv = ?", PvEncounter.class,
				PkPv);
		List<BlSettleDetail> resvo = new ArrayList<BlSettleDetail>();

		// 2.1处理患者优惠的记费明细数据
		if (amtDisc > 0) {
			BlSettleDetail voDisc = new BlSettleDetail();
			voDisc.setAmount(amtDisc.doubleValue());
			StringBuffer sqlCate = new StringBuffer(
					" select picate.pk_hp, bh.pk_payer, bh.eu_hptype from pi_cate picate");
			sqlCate.append(" inner join bd_hp bh on bh.pk_hp = picate.pk_hp ");
			sqlCate.append(" where picate.pk_picate = ?");
			List<Map<String, Object>> discHpMap = DataBaseHelper.queryForList(sqlCate.toString(), pvvo.getPkPicate());
			String pkPayer = "";
			String pkHp = "";
			if (discHpMap != null && discHpMap.size() > 0) {
				Map<String, Object> tempDisc = discHpMap.get(0);
				if (tempDisc.get("pkPayer") != null) {
					pkPayer = (String) tempDisc.get("pkPayer");
				}
				if (tempDisc.get("pkHp") != null) {
					pkHp = (String) tempDisc.get("pkHp");
				}
			}
			voDisc.setPkSettle(blSettle.getPkSettle());
			voDisc.setPkPayer(pkPayer);
			voDisc.setPkInsurance(pkHp);
			res.put(pkHp, voDisc);
		}
		List<Map<String, Object>> hpPlans = getHpPlans(PkPv);// 1.获取患者的医保列表
		if (hpPlans == null || hpPlans.size() <= 0) {

			resvo.addAll(res.values());
			return resvo;
		}
		Map<String, Object> majorHp = null;
		for (Map<String, Object> map : hpPlans) {
			String flagMaj = "";
			if (map.get("flagMaj") != null) {
				flagMaj = (String) map.get("flagMaj");
			}
			if ("1".equals(flagMaj)) {
				majorHp = map;
				break;
			}
		}

		// 2.2处理内部医保的记费明细数据
		if (amtHp > 0 && majorHp != null) {
			BlSettleDetail voHp = new BlSettleDetail();
			voHp.setAmount(amtHp.doubleValue());
			voHp.setPkSettle(blSettle.getPkSettle());
			String pkPayer = (majorHp != null && majorHp.get("pkPayer") != null) ? (String) majorHp.get("pkPayer") : "";
			String pkHp = (majorHp != null && majorHp.get("pkHp") != null) ? (String) majorHp.get("pkHp") : "";
			voHp.setPkPayer(pkPayer);
			voHp.setPkInsurance(pkHp);
			res.put(pkHp, voHp);
		}

		// 3.循环处理内部医保的结算策略和<=3的各类型医保
		for (Map<String, Object> map : hpPlans) {
			// 3.1数据准备
			String pkPayer = (map != null && map.get("pkPayer") != null) ? (String) map.get("pkPayer") : "";
			String pkHp = (map != null && map.get("pkHp") != null) ? (String) map.get("pkHp") : "";

			int EuHptype = -1;
			if (map.get("euHptype") != null) {
				EuHptype = Integer.parseInt((String) map.get("euHptype"));
			}

			// 3.2处理结算策略的各类数据
			if (EuHptype > 3) {// 该情况下可出现4,9
				BigDecimal amountInnerD = priceStrategyService.qryPatiSettlementAmountAllocationInfo(
						new BigDecimal(patiSelf), pvvo.getPkOrg(), Constant.IP, pkHp);
				if (patiSelf > amountInnerD.doubleValue()) {
					// 返回的自付金额要是比当前的小，则是有分摊的，故写入一些明细记录
					BlSettleDetail voInner = null;
					Double hpTemp = MathUtils.sub(patiSelf, amountInnerD.doubleValue());
					if (res.get(pkHp) != null) {
						voInner = res.get(pkHp);
						voInner.setAmount(MathUtils.add(voInner.getAmount(), hpTemp));
					} else {
						voInner = new BlSettleDetail();
						voInner.setAmount(hpTemp);
						voInner.setPkSettle(blSettle.getPkSettle());
						voInner.setPkInsurance(pkHp);
						voInner.setPkPayer(pkPayer);
					}
					res.put(pkHp, voInner);
					patiSelf = amountInnerD.doubleValue();
				}
			} else {
				Map<String, Object> hpMap = hpService.getHpInfo(patiSelf, map);
				BigDecimal amtHp3 = hpMap.get("amtHps") == null ? BigDecimal.ZERO : (BigDecimal) hpMap.get("amtHps");
				if (amtHp3.compareTo(BigDecimal.ZERO) > 0) {
					BlSettleDetail voHp3 = null;
					if (res.get(pkHp) != null) {
						voHp3 = res.get(pkHp);
						voHp3.setAmount(MathUtils.add(amtHp3.doubleValue(), voHp3.getAmount()));
						// 给主表赋值
						blSettle.setAmountInsu(amtHp3.add(blSettle.getAmountInsu()));
					} else {
						voHp3 = new BlSettleDetail();
						voHp3.setAmount(amtHp3.doubleValue());
						voHp3.setPkPayer(pkPayer);
						voHp3.setPkSettle(blSettle.getPkSettle());
						voHp3.setPkInsurance(pkHp);
						// 给主表赋值
						blSettle.setAmountInsu(amtHp3);
					}
					res.put(pkHp, voHp3);
					patiSelf = MathUtils.sub(patiSelf, amtHp3.doubleValue());
				}
			}

		}

		// 4.结算时患者自付金额。
		BlSettleDetail voSelf = new BlSettleDetail();
		voSelf.setAmount(patiSelf.doubleValue());
		voSelf.setPkSettle(blSettle.getPkSettle());
		voSelf.setPkInsurance(null);
		// 当保险计划为空时，付款方应该也为空
		voSelf.setPkPayer(qryBdPayerByEuType(pkOrg));
		res.put("pkSelf", voSelf);

		// 5.给主表赋值
		// blSettle.setAmountPi(new BigDecimal(patiSelf).setScale(2, BigDecimal.ROUND_HALF_UP));//不保留两位小数，保存的时候会报错
		resvo.addAll(res.values());

		return resvo;
	}
}
