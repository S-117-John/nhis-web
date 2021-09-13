package com.zebone.nhis.webservice.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zebone.nhis.webservice.syx.vo.platForm.BlOpDtVo;
import com.zebone.nhis.webservice.syx.vo.platForm.HpVo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybSt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzybTrialstVo;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormOnlineMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.OnlineBlopdtVo;
import com.zebone.nhis.webservice.syx.vo.platForm.OnlineOpAmtVo;
import com.zebone.nhis.webservice.syx.vo.platForm.OnlineSettlementVo;
import com.zebone.nhis.webservice.syx.vo.platForm.PlatFormOnlineInfo;
import com.zebone.nhis.webservice.syx.vo.platForm.PlatFormOnlineInfoReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.PlatFormOnlineInfoResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.PlatFormOnlineInfoResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.PlatFormOnlineInfoResSubject;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class PlatFormOnlineService {

	@Autowired
	private PlatFormOnlineMapper onlineMapper;

	/**
	 * 2.3.4.1 通过PKPV获取待结算费用明细
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String getDetailslist(String content) throws Exception {

		PlatFormOnlineInfoReqSubject req = (PlatFormOnlineInfoReqSubject) XmlUtil
				.XmlToBean(content, PlatFormOnlineInfoReqSubject.class);
		List<PlatFormOnlineInfo> payonlineList = req.getSubject();
		PlatFormOnlineInfo payonline = payonlineList.get(0);
		/** 查询待结算费用明细 */
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(
				"pkPv",
				CommonUtils.isEmptyString(payonline.getPkPv()) ? "" : payonline
						.getPkPv());// 患者就诊主键
		List<OnlineBlopdtVo> opdItems = onlineMapper
				.queryNoSettleInfoForCgByPkPv(paramMap);
		PlatFormOnlineInfoResSubject subject = new PlatFormOnlineInfoResSubject();// subject
		PlatFormOnlineInfoResResult result = new PlatFormOnlineInfoResResult();// result
		PlatFormOnlineInfoResExd exd = new PlatFormOnlineInfoResExd();// response
		// res
		// subject
		if (opdItems != null && opdItems.size() > 0) {
			result.setText("未查询到患者待结算费用明细!");
		} else {
			subject.setItem(opdItems);

			// result
			result.setText("处理成功!");
		}
		// result
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setSubject(subject);
		// response
		exd.setResult(result);

		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd),
				PlatFormOnlineInfoResExd.class);
		return xml;
	}

	/**
	 * 2.3.4.2 医保试算
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String getTrialSettlementInfo(String content) throws Exception {

		PlatFormOnlineInfoReqSubject req = (PlatFormOnlineInfoReqSubject) XmlUtil
				.XmlToBean(content, PlatFormOnlineInfoReqSubject.class);
		List<PlatFormOnlineInfo> payonlineList = req.getSubject();
		PlatFormOnlineInfo payonline = payonlineList.get(0);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(
				"pkPv",
				CommonUtils.isEmptyString(payonline.getPkPv()) ? "" : payonline
						.getPkPv());// 患者就诊主键
		String resultCode = "0";
		String resultText = "处理失败!";
		PlatFormOnlineInfoResSubject subject = new PlatFormOnlineInfoResSubject();// subject
		PlatFormOnlineInfoResResult result = new PlatFormOnlineInfoResResult();// result
		PlatFormOnlineInfoResExd exd = new PlatFormOnlineInfoResExd();// response
		InsGzybTrialstVo trialettlementvo = new InsGzybTrialstVo();
		PvEncounter pvVo = new PvEncounter();
		PiMaster master = new PiMaster();
		List<BlOpDt> opList = new ArrayList<BlOpDt>();
		OnlineOpAmtVo amtVo = new OnlineOpAmtVo();
		// 查询医保计划类型
		String euHptype = qryEuHpType(payonline.getPkPv());
		// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if (euHptype.equals(EnumerateParameter.ONE)) {
			// 医保
			trialettlementvo = onlineMapper.queryTrialstByPkPv(paramMap);
			if (trialettlementvo == null) {
				resultCode = "-1";
				resultText = "处理失败,未查询到患者医保预结算信息，请检查！";
			} else {
				// subject
				subject.setTrialettlementvo(trialettlementvo);
				// result
			}
		} else {
			// 除医保外
			pvVo = DataBaseHelper.queryForBean(
					"select * from Pv_Encounter where pk_pv=?",
					PvEncounter.class, new Object[] { payonline.getPkPv() });
			if (pvVo == null) {
				resultCode = "-1";
				resultText = "处理失败,未查询到患者登记信息，请检查！";
			} else {
				Map<String, Object> nameDept = DataBaseHelper.queryForMap(
						"select name_dept from bd_ou_dept where pk_dept = ?",
						pvVo.getPkDept());
				if (nameDept.get("nameDept") != null
						&& nameDept.get("nameDept") != "") {
					master = DataBaseHelper.queryForBean(
							"select * from pi_master where pk_pi = ?",
							PiMaster.class, new Object[] { pvVo.getPkPi() });
					if (master == null) {
						resultCode = "-1";
						resultText = "处理失败,未查询到患者基本信息，请检查！";
					} else {
						opList = onlineMapper.queryCgDetailsByPk(paramMap);
						if (opList == null) {
							resultCode = "-1";
							resultText = "处理失败,未查询到患者记费信息，请检查！";
						}
						amtVo = opSyxSettle(pvVo, opList);
						trialettlementvo.setPkPv(pvVo.getPkPv());
						trialettlementvo.setNamePi(pvVo.getNamePi());
						trialettlementvo.setType("2");
						trialettlementvo.setPvcodeIns("");
						trialettlementvo.setAmtSt(CommonUtils.getString(amtVo
								.getAmountSt()));
						trialettlementvo.setAmtInsu(CommonUtils.getString(amtVo
								.getAmountInsu()));
						trialettlementvo.setZhzfje("0.00");
						trialettlementvo.setBfxmzfje("0.00");
						trialettlementvo.setQfje("0.00");
						trialettlementvo.setGrzfje(CommonUtils.getString(amtVo
								.getAccountPrepaid()));
						trialettlementvo.setAmtPi(CommonUtils.getString(amtVo
								.getAmountPi()));
						trialettlementvo.setGrzf(CommonUtils.getString(amtVo
								.getAmountPi()));
						trialettlementvo.setCxzfje("0.00");
						trialettlementvo.setFypc("");
						trialettlementvo.setIdNo(master.getIdNo());
						trialettlementvo.setCodeIp(master.getCodeIp());
						trialettlementvo.setDateInp(pvVo.getDateBegin());
						trialettlementvo.setDateOut(pvVo.getDateEnd());
						trialettlementvo.setDateSt(new Date());
						trialettlementvo.setPkDept(pvVo.getPkDept());
						trialettlementvo.setNameDept(nameDept.get("nameDept")
								.toString());
						trialettlementvo.setPkEmpphy(pvVo.getPkEmpPhy());
						trialettlementvo.setNameEmpphy(pvVo.getNameEmpPhy());

						// subject
						subject.setTrialettlementvo(trialettlementvo);
						// result
					}
				} else {
					resultCode = "-1";
					resultText = "处理失败,未查询到患者入院科室信息，请检查！";
				}
			}
		}
		// res
		// result
		result.setId(resultCode.equals("0") ? "AA" : "AE");
		result.setText(resultCode.equals("0") ? "处理成功!" : resultText);
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setSubject(subject);
		// response
		exd.setResult(result);
		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd),
				PlatFormOnlineInfoResExd.class);
		return xml;
	}

	/**
	 * 2.3.4.3 通过PKPV获取医保结算信息
	 * 
	 * @param content
	 * @return throws Exception
	 */
	public String getSettlementInfo(String content) throws Exception {

		PlatFormOnlineInfoReqSubject req = (PlatFormOnlineInfoReqSubject) XmlUtil
				.XmlToBean(content, PlatFormOnlineInfoReqSubject.class);
		List<PlatFormOnlineInfo> payonlineList = req.getSubject();
		PlatFormOnlineInfo payonline = payonlineList.get(0);

		PlatFormOnlineInfoResSubject subject = new PlatFormOnlineInfoResSubject();// subject
		PlatFormOnlineInfoResResult result = new PlatFormOnlineInfoResResult();// result
		PlatFormOnlineInfoResExd exd = new PlatFormOnlineInfoResExd();// response
		/** 查询医保ins_gzyb_st、ins_gzyb_st_city结算信息 */
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(
				"pkPv",
				CommonUtils.isEmptyString(payonline.getPkPv()) ? "" : payonline
						.getPkPv());// 患者就诊主键
		OnlineSettlementVo settlementvo = onlineMapper
				.querysettlementvoByPkPv(paramMap);
		if (settlementvo == null) {
			result.setText("未查询到患者医保结算信息!");
		} else {
			// res
			// subject
			subject.setSettlementvo(settlementvo);
			// result
			result.setText("处理成功!");
		}
		// result
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setSubject(subject);
		// response
		exd.setResult(result);

		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd),
				PlatFormOnlineInfoResExd.class);
		return xml;
	}

	/**
	 * 2.3.4.4 HIS结算
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String getHisSettlementInfo(String content) throws Exception {

		PlatFormOnlineInfoReqSubject req = (PlatFormOnlineInfoReqSubject) XmlUtil
				.XmlToBean(content, PlatFormOnlineInfoReqSubject.class);
		List<PlatFormOnlineInfo> payonlineList = req.getSubject();
		PlatFormOnlineInfo payonline = payonlineList.get(0);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(
				"pkPv",
				CommonUtils.isEmptyString(payonline.getPkPv()) ? "" : payonline
						.getPkPv());// 患者就诊主键
		paramMap.put("codeHpst", CommonUtils.isEmptyString(payonline
				.getCodeHpst()) ? "" : payonline.getCodeHpst());// 就医登记号

		// 获取平台支付信息

		PlatFormOnlineInfoResSubject subject = new PlatFormOnlineInfoResSubject();// subject
		PlatFormOnlineInfoResResult result = new PlatFormOnlineInfoResResult();// result
		PlatFormOnlineInfoResExd exd = new PlatFormOnlineInfoResExd();// response
		BlExtPay extPay = new BlExtPay();
		/** 查询HIS结算信息 */
		BlSettle stVoTemp = DataBaseHelper
				.queryForBean(
						"select * from bl_settle where dt_sttype='01' and flag_canc='0' and pk_pv = ?",
						BlSettle.class, new Object[] { payonline.getPkPv() });

		Double amountSt = 0.0;
		if (stVoTemp != null) {
			amountSt = processNull(Double.parseDouble(stVoTemp.getAmountSt()
					.toString()));
		}
		// 查询医保计划类型
		String euHptype = qryEuHpType(payonline.getPkPv());
		// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if (euHptype.equals(EnumerateParameter.ONE)) {
			// 医保
			/** 根据医保结算号查询医保结算ins_gzyb_st信息 */
			GzybSt gzybstvo = onlineMapper.querygzybstvoByPkPv(paramMap);
			if (gzybstvo == null) {
				result.setText("未查询到患者医保结算信息!");
			} else {

				if (gzybstvo.getAmount() != amountSt) {
					result.setText("返回医保结算金额与HIS结算金额不一致!");
				} else {
					// res
					// subject
					subject.setStVoTemp(stVoTemp);
					DataBaseHelper
							.execute(
									"update INS_GZYB_ST set PK_SETTLE=? where PK_PV=? ",
									stVoTemp.getPkSettle(), payonline.getPkPv());
					DataBaseHelper
							.execute(
									"update INS_GZYB_VISIT set EU_STATUS_ST='1' where PK_PV=? ",
									payonline.getPkPv());
					/** HIS将移动平台支付信息写入外部支付接口记录表bl_ext_pay */
					// 写外部交易接口支付bl_ext_pay
					extPay.setPkExtpay(NHISUUID.getKeyId());
					extPay.setPkOrg(stVoTemp.getPkOrg());
					extPay.setAmount(BigDecimal.valueOf(CommonUtils
							.getDouble(amountSt)));
					extPay.setDtBank("");
					extPay.setNameBank("");
					extPay.setFlagPay("1");
					extPay.setSysname("");
					extPay.setPkPi(stVoTemp.getPkPi());
					extPay.setPkPv(stVoTemp.getPkPv());
					extPay.setResultPay("");
					extPay.setRefundNo("");
					extPay.setDateRefund(null);
					extPay.setEuBill("0");
					extPay.setPkBill("");
					extPay.setDateBill(null);
					ApplicationUtils.setDefaultValue(extPay, true);
					// 将平台支付信息赋值到下边
					// extPay.setEuPaytype();
					// extPay.setDateAp();
					// extPay.setDatePay();
					// extPay.setTradeNo();
					// extPay.setSerialNo();
					// extPay.setDescPay();
					// extPay.setPkDepo();
					// DataBaseHelper.insertBean(extPay);
					// result
					result.setText("处理成功!");
				}
			}
		} else {
			// 除医保外
			// 写外部交易接口支付bl_ext_pay
			extPay.setPkExtpay(NHISUUID.getKeyId());
			extPay.setPkOrg(stVoTemp.getPkOrg());
			extPay.setAmount(BigDecimal.valueOf(CommonUtils.getDouble(amountSt)));
			extPay.setDtBank("");
			extPay.setNameBank("");
			extPay.setFlagPay("1");
			extPay.setSysname("");
			extPay.setPkPi(stVoTemp.getPkPi());
			extPay.setPkPv(stVoTemp.getPkPv());
			extPay.setResultPay("");
			extPay.setRefundNo("");
			extPay.setDateRefund(null);
			extPay.setEuBill("0");
			extPay.setPkBill("");
			extPay.setDateBill(null);
			ApplicationUtils.setDefaultValue(extPay, true);
			// 将平台支付信息赋值到下边
			// extPay.setEuPaytype();
			// extPay.setDateAp();
			// extPay.setDatePay();
			// extPay.setTradeNo();
			// extPay.setSerialNo();
			// extPay.setDescPay();
			// extPay.setPkDepo();
			// DataBaseHelper.insertBean(extPay);
			// result
			result.setText("处理成功!");
		}

		// result
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setSubject(subject);
		// response
		exd.setResult(result);

		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd),
				PlatFormOnlineInfoResExd.class);
		return xml;
	}

	/**
	 * 2.3.4.5 取消医保结算
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String cancelSettlement(String content) throws Exception {

		PlatFormOnlineInfoReqSubject req = (PlatFormOnlineInfoReqSubject) XmlUtil
				.XmlToBean(content, PlatFormOnlineInfoReqSubject.class);
		List<PlatFormOnlineInfo> payonlineList = req.getSubject();
		PlatFormOnlineInfo payonline = payonlineList.get(0);

		PlatFormOnlineInfoResSubject subject = new PlatFormOnlineInfoResSubject();// subject
		PlatFormOnlineInfoResResult result = new PlatFormOnlineInfoResResult();// result
		PlatFormOnlineInfoResExd exd = new PlatFormOnlineInfoResExd();// response

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(
				"pkPv",
				CommonUtils.isEmptyString(payonline.getPkPv()) ? "" : payonline
						.getPkPv());// 患者就诊主键
		/** 根据医保结算号查询医保结算ins_gzyb_st信息 */
		GzybSt gzybstvo = onlineMapper.querygzybstvoByPkPv(paramMap);
		@SuppressWarnings("unused")
		String jydjh = "";// 就医登记号

		// res
		// subject
		if (gzybstvo == null) {
			result.setText("未查询到患者医保结算信息!");
		} else {
			jydjh = gzybstvo.getPvcodeIns();
			// 通过就医登记号进行医保结算撤销
			// subject
			// subject.setSettlementvo(settlementvo);
			// result
			result.setText("处理成功!");
		}
		// result
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setSubject(subject);
		// response
		exd.setResult(result);

		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd),
				PlatFormOnlineInfoResExd.class);
		return xml;
	}

	private Double processNull(Double args) {
		if (args == null) {
			args = new Double(0);
		}
		return args;
	}

	/**
	 * 查询患者的医保类型
	 * 
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public String qryEuHpType(String pkpv) throws BusException {
		String pkhp = "";// 医保计划主键
		String euPvtype = "";// 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		String stSql = "select pk_hp from PV_INSURANCE  where pk_pv=? and FLAG_MAJ='1' and (del_flag='0' or del_flag is null) ";
		Map<String, Object> pkHpMap = DataBaseHelper.queryForMap(stSql, pkpv);
		if (pkHpMap != null && pkHpMap.get("pkHp") != null) {
			pkhp = pkHpMap.get("pkHp").toString();// 医保计划主键
		}

		Map<String, Object> rateIpMap = DataBaseHelper
				.queryForMap(
						"select eu_hptype from BD_HP where  pk_hp = ? and (del_flag = '0' or del_flag is null) ",
						pkhp);
		if (rateIpMap != null) {
			if (rateIpMap.get("euPvtype") != null
					&& rateIpMap.get("euPvtype") != "") {
				euPvtype = rateIpMap.get("euPvtype").toString();
			}
		}
		return euPvtype;
	}

	/**
	 * 广州公医结算策略
	 * 
	 * @param pv
	 */
	public OnlineOpAmtVo opSyxSettle(PvEncounter pv, List<BlOpDt> blOpDts) {
		// 查询患者主医保是否是广州公医
		Map<String, Object> qryMap = new HashMap<>();
		qryMap.put("pkPv", pv.getPkPv());
		Map<String, Object> hpMap = onlineMapper.qryPvInsuAttrVal(qryMap);

		// 二、生成结算
		OnlineOpAmtVo amtVo = new OnlineOpAmtVo();

		// 获取需要结算的记费主键
		List<String> pkCgOpList = new ArrayList<>();
		for (BlOpDt vo : blOpDts) {
			if (vo.getRatioDisc() == null)
				vo.setRatioDisc(0D);
			pkCgOpList.add(vo.getPkCgop());
		}

		// 校验患者主医保0301参数是否为1
		if (hpMap != null
				&& hpMap.size() > 0
				&& hpMap.get("valAttr") != null
				&& EnumerateParameter.ONE.equals(CommonUtils.getString(hpMap
						.get("valAttr")))) {
			amtVo.setHpCodeAttr(CommonUtils.getString(hpMap.get("valAttr")));// 0301属性值
			/** 校验患者是否是急诊，急诊不需要判断定点医院，按照按普通门诊策略处理 */
			// 查询患者患者急诊就诊类型sql
			boolean emergencyFlag = false;
			if (emergencyFlag) {
				/** 公医普通门诊策略 */
				amtVo = gyGeneralOpSt(amtVo, pv, pkCgOpList);
			} else {
				/** 判断当前机构是否为当前患者的定点医院，非定点医院按照自费处理 */
				// 查询定点医院sql
				boolean disHosFlag = true;
				if (disHosFlag) {
					// 查询患者医保就诊模式（0普通门诊，1门诊慢病，2门诊特殊病）
					qryMap.put("pkHp", pv.getPkInsu());
					String hpStatus = onlineMapper.qryHpStatusByPv(qryMap);
					if (CommonUtils.isEmptyString(hpStatus))
						throw new BusException("请维护患者主医保就诊模式！");
					// 查询患者主诊断
					String nameDiag = onlineMapper.qryNamediagByPv(qryMap);
					qryMap.put("nameDiag", nameDiag);
					// 查询患者主医保类型(true:省公医 false:区公医)
					boolean proGyFlag = qryHpTypeByPv(pv.getPkInsu());
					if (proGyFlag) {
						switch (hpStatus) {
						case EnumerateParameter.ZERO:// 普通门诊
							amtVo = gyGeneralOpSt(amtVo, pv, pkCgOpList);
							break;
						case EnumerateParameter.ONE:// 门诊慢病(目前省公医门诊慢病先按照普通门诊策略结算)
							amtVo = gyGeneralOpSt(amtVo, pv, pkCgOpList);
							break;
						case EnumerateParameter.TWO:// 门诊特殊病(目前省公医门诊特殊病先按照普通门诊策略结算)
							amtVo = gyGeneralOpSt(amtVo, pv, pkCgOpList);
							break;
						default:
							break;
						}
					} else {
						switch (hpStatus) {
						case EnumerateParameter.ZERO:// 普通门诊
							amtVo = gyGeneralOpSt(amtVo, pv, pkCgOpList);
							break;
						case EnumerateParameter.ONE:// 门诊慢病
							amtVo.setNameDiag(nameDiag);
							amtVo = gyChronicOpSt(amtVo, pv, pkCgOpList);
							break;
						case EnumerateParameter.TWO:// 门诊特殊病
							amtVo.setNameDiag(nameDiag);
							amtVo = gySpdisOpSt(amtVo, pv, pkCgOpList);
							break;
						default:
							break;
						}
					}
				} else {
					/** 按照自费处理 */
					amtVo = gyExpenseOpSt(amtVo, pv, pkCgOpList);
				}
			}
		} else { // 默认按照正常情况处理
			for (BlOpDt bpt : blOpDts) {
				amtVo.setAmountSt(amtVo.getAmountSt().add(
						BigDecimal.valueOf(bpt.getAmount())));
				amtVo.setAmountPi(amtVo.getAmountPi().add(
						BigDecimal.valueOf(bpt.getAmountPi())));
				// 医保优惠计费部分
				// amtVo.setAmountInsu(amtVo.getAmountInsu().add(BigDecimal.valueOf(((bpt.getPriceOrg()
				// - bpt.getPrice()) + (bpt.getPriceOrg() * (1 -
				// bpt.getRatioSelf()))) * bpt.getQuan())));
				amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
				if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
					// 患者优惠
					amtVo.setDiscAmount(amtVo.getDiscAmount().add(
							BigDecimal.valueOf(MathUtils.mul(
									MathUtils.mul(bpt.getPriceOrg(),
											1 - bpt.getRatioDisc()),
									bpt.getQuan()))));
				}
				if ("1".equals(bpt.getFlagAcc())) {
					amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(
							BigDecimal.valueOf(bpt.getAmountPi())));
				}
			}
			// amountInsu = amountSt - amountPi
			amtVo.setAmountInsu(amtVo.getAmountSt().subtract(
					amtVo.getAmountPi()));
		}

		return amtVo;
	}

	/**
	 * 公医普通门诊结算策略
	 * 
	 * @param amtVo
	 * @param pv
	 */
	private OnlineOpAmtVo gyGeneralOpSt(OnlineOpAmtVo amtVo, PvEncounter pv,
			List<String> pkCgOpList) {
		BigDecimal amtPi = new BigDecimal(0); // 记录患者自付
		BigDecimal amtHp = new BigDecimal(0); // 记录医保支付

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("pkList", pkCgOpList);
		// 校验患者记费明细中的自付比例，如果有超过2种不同的自付比例弹出提示
		Map<String, Object> countMap = onlineMapper.qryRatioCountByPv(paramMap);
		if (countMap != null && countMap.size() >= 2)
			throw new BusException("项目自付比例设置有误，请修改!");

		// 查询患者待结算药费
		Map<String, Object> drugMap = onlineMapper
				.qrygyGeneralOpDrugAmt(paramMap);
		if (drugMap != null && drugMap.size() > 0) {
			// 获取待结算药费信息
			BigDecimal amtDrug = BigDecimal.valueOf(CommonUtils
					.getDouble(drugMap.get("amtDrug")));// 药费总金额
			BigDecimal amtHpdrug = BigDecimal.valueOf(CommonUtils
					.getDouble(drugMap.get("amtHpdrug")));// 可报销药费
			BigDecimal rateOp = BigDecimal.valueOf(CommonUtils
					.getDouble(drugMap.get("rateOp")));// 门诊自付比例
			BigDecimal drugquotaOp = BigDecimal.valueOf(CommonUtils
					.getDouble(drugMap.get("drugquotaOp")));// 药费限额
			if (drugquotaOp.compareTo(new BigDecimal(0)) == 0)
				drugquotaOp = new BigDecimal(999999D);

			paramMap.put("pkPi", pv.getPkPi());
			paramMap.put("pkPv", pv.getPkPv());
			paramMap.put("pkHp", pv.getPkInsu());
			paramMap.put("dateBegin", DateUtils.getDate("yyyyMMddHHmmss")
					.substring(0, 8) + "000000");
			paramMap.put("dateEnd", DateUtils.getDate("yyyyMMddHHmmss")
					.substring(0, 8) + "235959");
			// 查询患者当日诊次记录以及诊次药品费用
			List<Map<String, Object>> stDrugList = onlineMapper
					.qryTodayDrugAmtByPi(paramMap);

			// 查询医保计划扩展属性“0302”（广州公医普通门诊报销限制包含药费的诊次数）值
			String strVisitCount = onlineMapper.qryOpVisitCount(paramMap);
			Integer visitCount = 0;
			if (!CommonUtils.isEmptyString(strVisitCount))
				visitCount = Integer.valueOf(strVisitCount);

			boolean matchSuccess = false;// 标记是否匹配成功
			Integer insDrugCount = 0;// 标记amount_ins_drug>0的记录数
			if (stDrugList != null && stDrugList.size() > 0) {
				// 获取amount_ins_drug>0的记录数
				insDrugCount = getInsDrugCount(stDrugList);

				for (Map<String, Object> stDrugMap : stDrugList) {
					BigDecimal amountInsDrug = BigDecimal.valueOf(CommonUtils
							.getDouble(stDrugMap.get("amountInsDrug")));
					BigDecimal insAmtHpdrug = BigDecimal.valueOf(CommonUtils
							.getDouble(stDrugMap.get("amtHpdrug")));// 已结算药费

					// 用入口参数pk_pv匹配返回诊次记录结果中的pk_pv
					if (CommonUtils.getString(drugMap.get("pkPv")).equals(
							CommonUtils.getString(stDrugMap.get("pkPv")))) {
						// 如过amount_ins_drug>0或amount_ins_drug=0且其他amount_ins_drug>0的记录数<val_attr
						if (amountInsDrug.compareTo(new BigDecimal(0)) > 0
								|| (amountInsDrug.compareTo(new BigDecimal(0)) == 0 && insDrugCount < visitCount)) {

							if (insAmtHpdrug.compareTo(drugquotaOp) >= 0) {// 如果已结算amt_hpdrug>=药费限额drugquota_op
								amtPi = amtDrug;// 患者支付 amt_pi=amt_drug
								amtHp = new BigDecimal(0);// 医保支付 amt_hp=0
							} else if (insAmtHpdrug.add(amtHpdrug).compareTo(
									drugquotaOp) < 0) {// 如果已结算amt_hpdrug+未结算amt_hpdrug<药费限额drugquota_op
								// 患者支付amt_pi=未结amt_hpdrug*rate_op+
								// (amt_drug-amt_hpdrug)
								amtPi = (amtHpdrug.multiply(rateOp))
										.add(amtDrug.subtract(amtHpdrug));
								// 医保支付amt_hp=amt_drug-amt_pi
								amtHp = amtDrug.subtract(amtPi);
							} else if (insAmtHpdrug.add(amtHpdrug).compareTo(
									drugquotaOp) >= 0) {// 如果已结算amt_hpdrug+未结算amt_hpdrug>=药费限额drugquota_op
								// 患者支付amt_pi=(未结amt_hpdrug+已结amt_hpdrug-drugquota_op)+((drugquota_op-已结amt_hpdrug)*rate_op)
								// + (amt_drug-amt_hpdrug)
								amtPi = ((amtHpdrug.add(insAmtHpdrug))
										.subtract(drugquotaOp)).add(
										(drugquotaOp.subtract(insAmtHpdrug))
												.multiply(rateOp)).add(
										amtDrug.subtract(amtHpdrug));
								// 医保支付amt_hp=amt_drug -amt_pi
								amtHp = amtDrug.subtract(amtPi);
							}
							matchSuccess = true;
						} else if (amountInsDrug.compareTo(new BigDecimal(0)) == 0
								&& insDrugCount >= visitCount) {
							amtPi = amtDrug;// 患者支付 amt_pi=amt_drug
							amtHp = new BigDecimal(0);// 医保支付 amt_hp=0
							matchSuccess = true;
						}
						break;
					}
				}
			}

			// 无匹配到诊次记录结果中的pk_pv
			if (!matchSuccess) {
				// 判断返回amt_hpdrug>0的诊次记录数，如果大于等于val_attr
				if (insDrugCount >= visitCount) {
					amtPi = amtDrug;// amt_pi=amt_drug
					amtHp = new BigDecimal(0);// amt_hp=0
				} else if (insDrugCount < visitCount) {
					if (amtHpdrug.compareTo(drugquotaOp) > 0) {// 如果未结amt_hpdrug>drugquota_op
						// amt_pi=(未结amt_hpdrug-drugquota_op)+drugquota_op*rate_op+
						// (amt_drug-amt_hpdrug)
						amtPi = amtHpdrug.subtract(drugquotaOp)
								.add(drugquotaOp.multiply(rateOp))
								.add(amtDrug.subtract(amtHpdrug));
						// amt_hp=amt_drug-amt_pi
						amtHp = amtDrug.subtract(amtPi);
					} else if (amtHpdrug.compareTo(drugquotaOp) <= 0) {// 如果未结amt_hpdrug<=drugquota_op
						// amt_pi=未结amt_hpdrug*rate_op+ (amt_drug-amt_hpdrug)
						amtPi = amtHpdrug.multiply(rateOp).add(
								amtDrug.subtract(amtHpdrug));
						// amt_hp=amt_drug-amt_pi
						amtHp = amtDrug.subtract(amtPi);
					}
				}
			}

			amtVo.setAmountDrug(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));// 医保报销药金额
		}

		/** 挂号费处理 */
		// 查询患者有无结算过(结算过不再处理挂号费)
		Integer stCnt = onlineMapper.qryStInfoByPv(paramMap);
		if (stCnt > 0)
			paramMap.put("flagSettle", "0");

		Map<String, Object> feeMap = onlineMapper.qryExamFeeInfo(paramMap);// 查询诊查费用集合
		if (feeMap != null && feeMap.size() > 0) {
			if (feeMap.get("amtPv") != null
					&& CommonUtils.getDouble(feeMap.get("amtPv")) > 0D) {
				BigDecimal amtPv = BigDecimal.valueOf(CommonUtils
						.getDouble(feeMap.get("amtPv")));// 诊查费
				BigDecimal dtquotaOp = BigDecimal.valueOf(CommonUtils
						.getDouble(feeMap.get("dtquotaOp")));// 限额
				BigDecimal rateOp = BigDecimal.valueOf(CommonUtils
						.getDouble(feeMap.get("rateOp")));// 门诊自负比例

				if (dtquotaOp.compareTo(BigDecimal.valueOf(0D)) == 0)
					dtquotaOp = BigDecimal.valueOf(999999D);

				if (amtPv.compareTo(dtquotaOp) <= 0) {
					// amtPi = amtPi.add(amtPv.multiply(rateOp));
					amtHp = amtHp.add(amtPv.subtract(amtPv.multiply(rateOp)));
				} else if (amtPv.compareTo(dtquotaOp) > 0) {
					BigDecimal examPi = amtPv.subtract(dtquotaOp).add(
							dtquotaOp.multiply(rateOp));// 自费部分
					// amtPi = amtPi.add(examPi);
					amtHp = amtHp.add(amtPv.subtract(examPi));
				}

				// 查询诊查费用的主键
				List<String> pkList = onlineMapper.qryExamPkList(paramMap);
				if (pkList != null && pkList.size() > 0)
					pkCgOpList.addAll(pkList);
			}
		}

		// 查询非药品待结算金额
		Map<String, Object> itemAmtMap = onlineMapper
				.qrygyGeneralOpItemAmt(paramMap);
		if (itemAmtMap != null && itemAmtMap.size() > 0) {
			// amt_pi=amt_hppi
			amtPi = amtPi.add(BigDecimal.valueOf(CommonUtils
					.getDouble(itemAmtMap.get("amtHppi"))));
			// amt_hp=amt_hp
			amtHp = amtHp.add(BigDecimal.valueOf(CommonUtils
					.getDouble(itemAmtMap.get("amtHp"))));
		}

		// 查询所有待结算的明细
		List<BlOpDt> blOpDts = onlineMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(
					BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				// 患者优惠
				amtVo.setDiscAmount(amtVo
						.getDiscAmount()
						.add(BigDecimal.valueOf(MathUtils.mul(
								MathUtils.mul(bpt.getPriceOrg(),
										1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(
						BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}

		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));
		amtPi = amtVo.getAmountSt().subtract(amtVo.getAmountInsu());
		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));

		// amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		// //重新结算医保支付(amountSt-amountPi)
		// amtHp = amtVo.getAmountSt().subtract(amtVo.getAmountPi());
		// amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));

		return amtVo;
	}

	/**
	 * 公医慢病结算策略
	 * 
	 * @param amtVo
	 * @param pv
	 */
	private OnlineOpAmtVo gyChronicOpSt(OnlineOpAmtVo amtVo, PvEncounter pv,
			List<String> pkCgOpList) {
		BigDecimal amtPi = new BigDecimal(0); // 记录患者自付
		BigDecimal amtHp = new BigDecimal(0); // 记录医保支付

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("nameDiag", amtVo.getNameDiag());
		paramMap.put("pkList", pkCgOpList);

		/** 判断患者是否属于慢病类型,不属于按照自费处理 */
		Integer cnt = onlineMapper.qryChronicTypeByDiag(paramMap);// 查询诊断是否属于慢病类型
		if (cnt <= 0)// 自费策略
			return gyExpenseOpSt(amtVo, pv, pkCgOpList);

		// 查询公医慢病门诊待结算金额
		Map<String, Object> amtMap = onlineMapper.qrygyChronicOpAmt(paramMap);
		if (amtMap != null && amtMap.size() > 0) {
			BigDecimal amt = BigDecimal.valueOf(CommonUtils
					.getDoubleObject(amtMap.get("amt")));// 总金额
			BigDecimal amtHppi = BigDecimal.valueOf(CommonUtils
					.getDoubleObject(amtMap.get("amtHppi")));// 患者支付

			// amt_pi=amt_hppi
			amtPi = amtHppi;
			// amt_hp=amt-amt_hppi
			amtHp = amt.subtract(amtHppi);
		}

		// 查询所有待结算的明细
		List<BlOpDt> blOpDts = onlineMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(
					BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				// 患者优惠
				amtVo.setDiscAmount(amtVo
						.getDiscAmount()
						.add(BigDecimal.valueOf(MathUtils.mul(
								MathUtils.mul(bpt.getPriceOrg(),
										1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(
						BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}

		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		// 重新结算医保支付(amountSt-amountPi)
		amtHp = amtVo.getAmountSt().subtract(amtVo.getAmountPi());
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));

		return amtVo;
	}

	/**
	 * 公医门诊特殊病策略
	 * 
	 * @param amtVo
	 * @param pv
	 */
	private OnlineOpAmtVo gySpdisOpSt(OnlineOpAmtVo amtVo, PvEncounter pv,
			List<String> pkCgOpList) {
		BigDecimal amtPi = new BigDecimal(0); // 记录患者自付
		BigDecimal amtHp = new BigDecimal(0); // 记录医保支付

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("nameDiag", amtVo.getNameDiag());
		paramMap.put("dateBegin", DateUtils.getDate("yyyyMMddHHmmss")
				.substring(0, 8) + "000000");
		paramMap.put("dateEnd",
				DateUtils.getDate("yyyyMMddHHmmss").substring(0, 8) + "235959");
		paramMap.put("pkList", pkCgOpList);

		Map<String, Object> diseMap = onlineMapper.qrySpdisInfoByPv(paramMap);
		// 特殊病信息为空时或者门诊特殊病有效期已过（flag_valid=‘0’），按全自费处理
		if (diseMap == null || diseMap.size() <= 0
				|| CommonUtils.getString(diseMap.get("flagValid")).equals("0"))
			return gyExpenseOpSt(amtVo, pv, pkCgOpList);

		// 按患者医保类型下的住院比例重新计算记费明细
		List<BlOpDtVo> opVoList = onlineMapper.qryOpListByPv(paramMap);
		List<BlOpDt> upDtList = new ArrayList<>();
		if (opVoList != null && opVoList.size() > 0) {
			for (BlOpDtVo vo : opVoList) {
				vo.setRatioSelf(vo.getRetePi());
				// amount_hppi=price*quan*ratio_self
				vo.setAmountHppi(MathUtils.mul(
						MathUtils.mul(vo.getPrice(), vo.getQuan()),
						vo.getRatioSelf()));
				// amount_pi=amount_hppi-(price_org*quan*ratio_disc)
				Double amt = MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(
						MathUtils.mul(vo.getPriceOrg(), vo.getQuan()),
						vo.getRatioDisc()));
				if (MathUtils.compareTo(amt, 0D) < 0 && vo.getQuan() > 0) {
					vo.setAmountPi(0D);
				} else {
					vo.setAmountPi(amt);
				}

				try {
					upDtList.add((BlOpDt) vo.clone());
				} catch (CloneNotSupportedException e) {
					throw new BusException("克隆转换BlOpDt对象时发生错误，请检查!");
				}
			}
			// 更新费用明细
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(BlOpDt.class),
					upDtList);
		}

		/** 特病策略计算，返回计算结果 */
		paramMap.put("pkPi", pv.getPkPi());
		paramMap.put("dateBegin", DateUtils.getFirstDayOfGiven(
				DateUtils.getDate("yyyyMMdd"), "yyyyMMdd",2));
		paramMap.put("dateEnd", DateUtils.getFirstDayOfNext(
				DateUtils.getDate("yyyyMMdd"), "yyyyMMdd",2));
		// 查询公医特病门诊待结算金额
		Map<String, Object> amtMap = onlineMapper.qrySpdisOpAmt(paramMap);
		// 获取本月已结算信息
		Map<String, Object> stAmtMap = onlineMapper.qrySpdisStOpAmt(paramMap);

		BigDecimal diseAmt = new BigDecimal(0);// 病种限额
		BigDecimal amtCur = new BigDecimal(0);// 待结算总金额
		BigDecimal amtPiCur = new BigDecimal(0);// 待结算患者支付金额
		BigDecimal amtHpCur = new BigDecimal(0);// 待结算医保支付金额
		BigDecimal stAmtTotal = new BigDecimal(0);// 已结算总金额
		BigDecimal stAmtPiTotal = new BigDecimal(0);// 已结算患者支付金额
		BigDecimal stAmtHpTotal = new BigDecimal(0);// 已结算医保支付金额

		diseAmt = BigDecimal.valueOf(CommonUtils.getDouble(diseMap
				.get("amount")));
		if (amtMap != null && amtMap.size() > 0) {
			amtCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap
					.get("amtCur")));
			amtPiCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap
					.get("amtPiCur")));
			amtHpCur = BigDecimal.valueOf(CommonUtils.getDouble(amtMap
					.get("amtHpCur")));
		}
		if (stAmtMap != null && stAmtMap.size() > 0) {
			stAmtTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap
					.get("amtTotal")));
			stAmtPiTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap
					.get("amtPiTotal")));
			stAmtHpTotal = BigDecimal.valueOf(CommonUtils.getDouble(stAmtMap
					.get("amtHpTotal")));
		}

		if (stAmtHpTotal.compareTo(diseAmt) >= 0) {// 当amt_hp_total
													// >=dise.amount时（本月记账额已超过病种限额）
			// amt_pi=amt_cur(本次结算总金额)
			amtPi = amtCur;
			// amt_hp=0
			amtHp = new BigDecimal(0);
		} else if ((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmt) >= 0) {// 当amt_hp_total+amt_hp_cur>=dise.amount时（本月记账额+本次待记账额超过病种限额）
			// amt_pi=amt_pi_cur+(amt_hp_total+amt_hp_cur-dise.amount)
			amtPi = amtPiCur
					.add((stAmtHpTotal.add(amtHpCur).subtract(diseAmt)));
			// amt_hp=amt_cur-amt_pi
			amtHp = amtCur.subtract(amtPi);
		} else if ((stAmtHpTotal.add(amtHpCur)).compareTo(diseAmt) < 0) {// 当amt_hp_total+amt_hp_cur<dise.amount时（本月记账额+本次待记账额未超过病种限额）
			// amt_pi=amt_pi_cur
			amtPi = amtPiCur;
			// amt_hp=amt_hp_cur
			amtHp = amtHpCur;
		}

		// 查询所有待结算的明细
		List<BlOpDt> blOpDts = onlineMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(
					BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				// 患者优惠
				amtVo.setDiscAmount(amtVo
						.getDiscAmount()
						.add(BigDecimal.valueOf(MathUtils.mul(
								MathUtils.mul(bpt.getPriceOrg(),
										1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(
						BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}

		amtVo.setAmountPi(amtPi.setScale(2, BigDecimal.ROUND_HALF_UP));
		// 重新结算医保支付(amountSt-amountPi)
		amtHp = amtVo.getAmountSt().subtract(amtVo.getAmountPi());
		amtVo.setAmountInsu(amtHp.setScale(2, BigDecimal.ROUND_HALF_UP));

		return amtVo;
	}

	/**
	 * 公医自费策略
	 * 
	 * @param amtVo
	 * @param pv
	 */
	private OnlineOpAmtVo gyExpenseOpSt(OnlineOpAmtVo amtVo, PvEncounter pv,
			List<String> pkCgOpList) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("pkList", pkCgOpList);

		// 查询所有待结算的明细
		List<BlOpDt> blOpDts = onlineMapper.qryOpcgsByPv(paramMap);
		for (BlOpDt bpt : blOpDts) {
			amtVo.setAmountSt(amtVo.getAmountSt().add(
					BigDecimal.valueOf(bpt.getAmount())));
			amtVo.setAmountPi(amtVo.getAmountPi().add(
					BigDecimal.valueOf(bpt.getAmountPi())));
			// 医保优惠计费部分
			// amtVo.setAmountInsu(amtVo.getAmountInsu().add(BigDecimal.valueOf(((bpt.getPriceOrg()
			// - bpt.getPrice()) + (bpt.getPriceOrg() * (1 -
			// bpt.getRatioSelf()))) * bpt.getQuan())));
			amtVo.setPkDisc(bpt.getPkDisc());// 优惠类型
			if (CommonUtils.isEmptyString(amtVo.getPkDisc())) {
				// 患者优惠
				amtVo.setDiscAmount(amtVo
						.getDiscAmount()
						.add(BigDecimal.valueOf(MathUtils.mul(
								MathUtils.mul(bpt.getPriceOrg(),
										1 - bpt.getRatioDisc()), bpt.getQuan()))));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				amtVo.setAccountPrepaid(amtVo.getAccountPrepaid().add(
						BigDecimal.valueOf(bpt.getAmountPi())));
			}
		}

		amtVo.setAmountPi(amtVo.getAmountSt());
		amtVo.setAmountInsu(new BigDecimal(0));

		return amtVo;
	}

	/**
	 * 获取amount_ins_drug>0的记录数
	 * 
	 * @param stDrugList
	 * @return
	 */
	private Integer getInsDrugCount(List<Map<String, Object>> stDrugList) {
		Integer count = 0;
		for (Map<String, Object> map : stDrugList) {
			BigDecimal amountInsDrug = BigDecimal.valueOf(CommonUtils
					.getDouble(map.get("amountInsDrug")));
			if (amountInsDrug.compareTo(new BigDecimal(0)) > 0)
				count++;
		}
		return count;
	}

	/**
	 * 查询患者医保公医类型
	 * 
	 * @param pkPv
	 * @return true:省公医 false:区公医
	 */
	private boolean qryHpTypeByPv(String pkHp) {
		boolean flag = false;

		// 公医类型，读取参数BL0033（广州医保省公医上级编码）
		String sysCode = ApplicationUtils.getSysparam("BL0033", true);
		// 查询患者主医保的上级医保编码
		Map<String, Object> qryMap = new HashMap<>();
		qryMap.put("pkHp", pkHp);
		HpVo hp = onlineMapper.queryHp(qryMap);
		if (sysCode.equals(hp.getFaCode()))
			flag = true;
		else
			flag = false;

		return flag;
	}
}
