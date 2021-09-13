package com.zebone.nhis.bl.pub.syx.service;

import com.zebone.nhis.bl.ipcg.support.YBProcessUtils;
import com.zebone.nhis.bl.pub.service.HpService;
import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvPrintProcessUtils;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.syx.dao.IpSettleGzgyMapper;
import com.zebone.nhis.bl.pub.syx.vo.BlInvSearch;
import com.zebone.nhis.bl.pub.syx.vo.InvVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.base.bd.price.BdInvcate;
import com.zebone.nhis.common.module.bl.*;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgySt;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.pub.vo.PatiCardVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.shiro.util.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service
public class IpSettleGzgyService {

	@Autowired
	private PareAccoutService pareAccoutService;

	@Autowired
	private BlIpPubMapper blIpPubMapper;

	@Autowired
	private PriceStrategyService priceStrategyService;

	@Autowired
	private InvSettltService invSettltService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private BalAccoutService balAccoutService;

	@Resource
	private IpSettleGzgyMapper ipSettleGzgyMapper;

	@Autowired
	private HpService hpService;

	/**
	 * 特殊项目结算
	 * 
	 * @param settlevo
	 * @param stVo
	 */
	public void gzgySpItemIpSettle(SettleInfo settlevo, BlSettle stVo) {

		InsGzgySt insGzgySt = new InsGzgySt();

		Map<String, Object> retMap = DataBaseHelper.queryForMap(
				"select code_org from bd_ou_org where pk_org = ?", UserContext
						.getUser().getPkOrg());

		if (retMap != null && retMap.size() > 0
				&& retMap.get("codeOrg") != null)
			insGzgySt.setCodeOrg(CommonUtils.getString(retMap.get("codeOrg"))); // 机构编码

		insGzgySt.setPkPv(stVo.getPkPv());
		insGzgySt.setPkHp(stVo.getPkInsurance());
		insGzgySt.setPkSettle(stVo.getPkSettle());
		insGzgySt.setAmount(settlevo.getAmountSt().doubleValue());
		insGzgySt.setAmountPi((settlevo.getAmountPi().subtract(settlevo
				.getAmountUnit())).doubleValue());
		insGzgySt.setAmountIns(settlevo.getAmountInsu().doubleValue());
		insGzgySt.setAmountInsDrug(0D);
		insGzgySt
				.setAmountUnit(settlevo.getAmountUnit() == null ? new Double(0)
						: settlevo.getAmountUnit().doubleValue());// 鍗曚綅鏀粯閲戦

		DataBaseHelper.insertBean(insGzgySt);
	}

	/**
	 * 公医统一结算策略
	 * 
	 * @param settlevo
	 * @param stVo
	 */
	public void gzgyIpSettle(SettleInfo settlevo, BlSettle stVo) {
		/** 组织结算数据写表ins_gzgy_st */
		InsGzgySt insGzgySt = new InsGzgySt();

		/** 校验是否是单位医保，只有单位医保走药费限额 */
		Map<String, Object> euHptypeMap = DataBaseHelper.queryForMap(
				"select eu_hptype from bd_hp where pk_hp = ?",
				new Object[] { stVo.getPkInsurance() });

		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, stVo.getPkPv());

		String euHptype = "";
		if (euHptypeMap != null && euHptypeMap.size() > 0
				&& euHptypeMap.get("euHptype") != null)
			euHptype = CommonUtils.getString(euHptypeMap.get("euHptype"));

		Map<String, Object> paramMap = new HashMap<>();
		if (settlevo.getPkCgips() != null && settlevo.getPkCgips().size() > 0){
			paramMap.put("pkList", settlevo.getPkCgips());
		}
		paramMap.put("pkPv", stVo.getPkPv());
		// 格式化开始时间
		String dateBegin = dateTrans(stVo.getDateBegin()).substring(0, 8)
				+ "000000";
		Date begin = dateTrans(dateBegin);
		paramMap.put("begin", begin);
		// 现在有患者出院后补费，计费时间为当前时间，如果用患者出院时间为查询条件查询记费信息，会出现漏查情况，故如果是出院结算date_end赋值为当前时间
		if (stVo.getDtSttype() != null && stVo.getDtSttype().equals("10")){
			paramMap.put("end", DateUtils.strToDate("20991231235959"));
		}else{
			paramMap.put("end", stVo.getDateEnd());
		}
		// 查询是否是本院职工
		Map<String, Object> retMap = ipSettleGzgyMapper.qryPvByzgFlag(paramMap);

		String attrVal = "";
		Map<String, Object> valMap = ipSettleGzgyMapper
				.qryPvInsuAttrVal(paramMap);
		if (valMap != null && valMap.size() > 0
				&& valMap.get("valAttr") != null)
			attrVal = CommonUtils.getString(valMap.get("valAttr"));
		// 只有广州公医或本院职工走药费限额
		if (EnumerateParameter.ONE.equals(attrVal)
				|| (retMap != null && retMap.size() > 0 && 
						EnumerateParameter.ONE.equals(CommonUtils.getString(retMap.get("valAttr"))))) {
			// 计算amt_pi和amt_hp
			Double amtPi = 0D;
			Double amtHp = 0D;
			Double stDrugAmt = 0D;

			/** 住院费用分解 */
			// 1.查询就诊药品费用
			List<Map<String, Object>> drugList = ipSettleGzgyMapper
					.qryDrugCostByPv(paramMap);
			Map<String, Object> drugAmtMap = ipSettleGzgyMapper
					.qryStDrugAmt(paramMap);// 查询本次就诊已结算的药费
			if (drugAmtMap != null && drugAmtMap.size() > 0
					&& drugAmtMap.get("stamtdrug") != null)
				stDrugAmt = CommonUtils.getDouble(drugAmtMap.get("stamtdrug"));

			if (drugList != null && drugList.size() > 0) {
				for (Map<String, Object> drugMap : drugList) {
					if (drugMap == null || drugMap.size() <= 0)
						continue;

					if (BlcgUtil.converToTrueOrFalse(CommonUtils
							.getString(drugMap.get("flagSettle"))))
						continue;

					// 获取药费限额(如果为空则没有限制，赋值为999999)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("drugquota")))
							|| CommonUtils.getDouble(drugMap.get("drugquota")) == 0D)
						drugMap.put("drugquota", 999999D);
					// 获取已结算医保药费(如果为空,赋值为0)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("amountInsDrug"))))
						drugMap.put("amountInsDrug", 0D);

					// amount_ins_drug>=drugquota
					if (stDrugAmt >= CommonUtils.getDouble(drugMap
							.get("drugquota"))) {
						amtPi = CommonUtils.getDouble((BigDecimal) drugMap
								.get("amount"));
						amtHp = 0D;
						insGzgySt.setAmountInsDrug(amtHp); // 医保支付药费
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) >= CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// >=drugquota
						// amt_pi=amount_ins_drug+ amount_drug
						// -drugquota+((drugquota-amount_ins_drug)*ratio_pi)
						// +(amount-amount_drug)
						amtPi = MathUtils.add(MathUtils.add(MathUtils.sub(
								MathUtils.add(stDrugAmt, CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("drugquota"))), MathUtils.mul(
								MathUtils.sub(CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("drugquota")), stDrugAmt),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("ratioPi")))), MathUtils.sub(
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("amount")), CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))));
						// amt_hp= amount - amt_pi
						amtHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amtPi);
						insGzgySt.setAmountInsDrug(amtHp); // 医保支付药费
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) < CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// <drugquota
						// amt_pi= amount_drug *ratio_pi+(amount-amount_drug)
						amtPi = MathUtils.add(
								MathUtils.mul(CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug")),
										CommonUtils
												.getDouble((BigDecimal) drugMap
														.get("ratioPi"))),
								MathUtils.sub(CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amount")), CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))));

						// amt_hp= amount -amt_pi
						amtHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amtPi);
						insGzgySt.setAmountInsDrug(amtHp); // 医保支付药费
					}
					// 自费金额+加收金额
					amtPi = MathUtils.add(amtPi, CommonUtils
							.getDouble((BigDecimal) drugMap.get("amountAdd")));
				}
			}

			// 2.查询非药品费用
			// Map<String,Object> itemMap =
			// ipSettleGzgyMapper.qryItemCostByPv(paramMap);
			// if(itemMap!=null && itemMap.size()>0){
			// //amt_pi=amount-amount_hp
			// amtPi = MathUtils.add(amtPi,
			// MathUtils.sub(CommonUtils.getDouble((BigDecimal)itemMap.get("amount")),
			// CommonUtils.getDouble((BigDecimal)itemMap.get("amountHp"))));
			// amtHp = MathUtils.add(amtHp,
			// CommonUtils.getDouble((BigDecimal)itemMap.get("amountHp")));
			// }

			// 2.非药品待结算费用策略
			Map<String, Object> amtMap = ipGyItemSettle(pvvo, paramMap);
			amtPi = MathUtils.add(amtPi,
					CommonUtils.getDouble(amtMap.get("amtPi")));
			amtHp = MathUtils.add(amtHp,
					CommonUtils.getDouble(amtMap.get("amtHp")));

			amtPi = CommonUtils.getDoubleObject(BigDecimal.valueOf(amtPi)
					.setScale(2, BigDecimal.ROUND_HALF_UP));
			insGzgySt.setAmountPi(stVo.getAmountPi().doubleValue()); // 患者自付金额
			insGzgySt.setAmountIns(stVo.getAmountInsu().doubleValue()); // 医保支付金额
		} else {
			insGzgySt.setAmountPi(stVo.getAmountPi().doubleValue()); // 患者自付金额
			insGzgySt.setAmountIns(stVo.getAmountInsu().doubleValue()); // 医保支付金额
		}

		Map<String, Object> codeOrgMap = DataBaseHelper.queryForMap(
				"select code_org from bd_ou_org where pk_org = ?", UserContext
						.getUser().getPkOrg());

		if (codeOrgMap != null && codeOrgMap.size() > 0
				&& codeOrgMap.get("codeOrg") != null)
			insGzgySt.setCodeOrg(CommonUtils.getString(codeOrgMap
					.get("codeOrg"))); // 机构编码

		insGzgySt.setPkPv(stVo.getPkPv()); // 就诊记录
		insGzgySt.setPkHp(stVo.getPkInsurance()); // 医保类型
		insGzgySt.setPkSettle(stVo.getPkSettle()); // 结算主键
		insGzgySt.setAmount(settlevo.getAmountSt().doubleValue()); // 结算总额
		// insGzgySt.setAmountPi(settlevo.getAmountPi().doubleValue()); //患者自付金额
		insGzgySt.setAmountInsDrug(insGzgySt.getAmountInsDrug() == null ? 0D
				: insGzgySt.getAmountInsDrug());
		insGzgySt.setAmountUnit(0D);// 单位支付金额

		/** 保存ins_gzgy_st表 */
		DataBaseHelper.insertBean(insGzgySt);
		/** 计算出来的amount_pi、amount_ins重新回填bl_settle表保证两表数据一致 */
		// DataBaseHelper.execute("update bl_settle set amount_pi=?,amount_insu=? where pk_settle=?"
		// , new
		// Object[]{insGzgySt.getAmountPi(),insGzgySt.getAmountIns(),stVo.getPkSettle()});
	}

	private Map<String, Object> set2Scal(Map<String, Object> map) {
		if (map != null) {
			for (Entry<String, Object> e : map.entrySet()) {
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

	private String qryBdPayerByEuType(String pkOrg) {
		String pkPayer = "";
		Map<String, Object> map = DataBaseHelper
				.queryForMap("select pk_payer from bd_payer where eu_type='0' ");
		if (map != null) {
			pkPayer = (String) map.get("pkPayer");
		}
		return pkPayer;
	}

	private List<Map<String, Object>> getHpPlans(String PkPv) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		// 1.pv_insurance表里的医保
		StringBuffer sql = new StringBuffer(
				"select bh.pk_payer,insu.sort_no,bh.eu_hptype,insu.pk_hp,insu.flag_maj");
		sql.append(" from pv_insurance insu");
		sql.append(" inner join  bd_hp bh");
		sql.append(" on insu.pk_hp = bh.pk_hp ");
		sql.append("  where insu.pk_pv = ?");
		sql.append(" order by insu.sort_no asc,insu.flag_maj desc");
		res.addAll(DataBaseHelper.queryForList(sql.toString(), PkPv));
		return res;
	}

	private Map<String, Object> getHpInfo(Double price,
			Map<String, Object> HpInfo) {
		Map<String, Object> res = new HashMap<String, Object>();
		// TODO 第三方医保处理过程
		res.put("amtHps", BigDecimal.ZERO);
		return res;
	}

	/**
	 * 判断患者是否有过中途结算，如果有，返回上次的结算日期
	 * 
	 * @param PkPv
	 * @return
	 */
	private Date checkMidSettle(String PkPv) {
		Map<String, Object> map = DataBaseHelper
				.queryForMap(
						"select count(1) amt from bl_settle where dt_sttype = 11 and flag_canc = 0 and pk_pv = ? and pk_settle not in (select pk_settle_canc from bl_settle where pk_pv = ?)",
						PkPv, PkPv);
		int cnt = 0;
		if (map.get("amt") instanceof BigDecimal) {
			BigDecimal amt = amtTrans(map.get("amt"));
			cnt = amt.intValue();
		} else {
			cnt = map.get("amt") == null ? 0 : (Integer) map.get("amt");
		}

		if (cnt > 0) {
			Map<String, Object> dateInfo = DataBaseHelper
					.queryForMap(
							"select date_end dateEnd from bl_settle where  dt_sttype = 11 and flag_canc = 0 and  pk_pv = ? order by  date_end desc",
							PkPv);
			return (Date) dateInfo.get("dateend");
		}
		return null;
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

	private void updDepositInfo(String pkPv, String pkSettle, Date begin,
			Date end, List<String> pkDepoList, String isStPre, String euSttype) {
		// 成伟要求：操作bl_deposit表只使用pk_pv字段查询。
		StringBuffer sql = new StringBuffer(
				"update bl_deposit set flag_settle = '1', pk_settle = ? where eu_dptype = '9' and flag_settle <> 1 and pk_pv = ? ");
		// 系统参数BL0040值为0时，判断有无选择指定预交金信息参与结算，如果有，只更新选择的预交金信息
		if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals("0")
				&& !CommonUtils.isEmptyString(euSttype) && euSttype.equals("1")) {
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

	private void setDepoInfo(BlDeposit fromSettle, BlSettle stVo) {
		fromSettle.setPkDepo(NHISUUID.getKeyId());
		fromSettle.setFlagSettle("1");
		fromSettle.setEuPvtype("3");
		fromSettle.setPkSettle(stVo.getPkSettle());
	}

	private void settleForDeposit(BlSettle blSettle, IUser user, String pkPv) {
		// //4.1.1）计算可存款金额 = 患者预交金 - 患者自付金额；
		BigDecimal amountPi = blSettle.getAmountPi() == null ? BigDecimal.ZERO
				: blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep() == null ? BigDecimal.ZERO
				: blSettle.getAmountPrep();
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
		balAccoutService.saveMonOperation(blDepoPi, user, pkPv, null,
				blDepoPi.getDtPaymode());

	}

	private void settleForArrears(BlSettle blSettle) {
		// //4.2.1计算欠款金额 = 患者自付金额 – 患者预交金。
		BigDecimal amountPi = blSettle.getAmountPi() == null ? BigDecimal.ZERO
				: blSettle.getAmountPi();
		BigDecimal amountPrep = blSettle.getAmountPrep() == null ? BigDecimal.ZERO
				: blSettle.getAmountPrep();
		BigDecimal deposit = amountPi.subtract(amountPrep);
		// 4.2.2生成欠费数据：
		BlSettleAr vo = new BlSettleAr();
		vo.setAmtAr(deposit.doubleValue());
		vo.setAmtPay(0.0);
		vo.setFlagCl("0");
		vo.setPkOrg(blSettle.getPkOrg());
		vo.setPkSettle(blSettle.getPkSettle());
		vo.setPkSettlear(NHISUUID.getKeyId());
		DataBaseHelper.insertBean(vo);

	}

	/**
	 * 处理外部接口支付数据
	 * 
	 * @param pkDepo
	 * @param blExtPay
	 */
	private void saveBlExtPay(String pkDepo, BlExtPay blExtPay) {
		if (blExtPay != null) {
			BlExtPay blExtPayTwo = new BlExtPay();
			blExtPayTwo = DataBaseHelper.queryForBean(
					" SELECT * FROM bl_ext_pay WHERE trade_no = ?",
					BlExtPay.class, blExtPay.getTradeNo());
			blExtPayTwo.setPkDepo(pkDepo);
			blExtPayTwo.setPkPi(blExtPay.getPkPi());
			blExtPayTwo.setPkPv(blExtPay.getPkPv());
			DataBaseHelper.updateBeanByPk(blExtPayTwo, false);
			/*
			 * if(blExtPay.getEuPaytype().equals("0")||blExtPay.getEuPaytype().
			 * equals("1")){//支付宝||微信 //支付宝、微信回调的时候已经生成，这里只需要插入交款记录主键，关联起来就可以
			 * 
			 * }else if(blExtPay.getEuPaytype().equals("2")){//银行
			 * blExtPay.setDateAp(new Date()); blExtPay.setPkDepo(pkDepo);
			 * blExtPay.setSysname("综合服务平台");
			 * DataBaseHelper.insertBean(blExtPay); }
			 */
		}
	}

	/**
	 * 更新住院计费表
	 * 
	 * @param pkPv
	 * @param stVo
	 * @param begin
	 * @param end
	 */
	private void updChargeInfo(String pkPv, BlSettle stVo, Date begin,
			Date end, List<String> pkCgips) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", stVo.getPkSettle());
		StringBuffer sqlSt;
		Map<String, Object> amtStMap;
		BigDecimal amtSt;// 结算金额AmtSt
		StringBuffer cgipsArgs = new StringBuffer("");
		if (begin != null && end != null) {
			// 格式化开始时间
			String date = dateTrans(begin).substring(0, 8) + "000000";
			Date dateBegin = dateTrans(date);
			paramMap.put("begin", dateBegin);
			paramMap.put("end", end);
		}
		if (pkCgips != null && pkCgips.size() > 0) {
			paramMap.put("pkList", pkCgips);
			for (int i = 0; i < pkCgips.size(); i++) {
				if ((i) % 999 == 0) {
					if (i != 0)
						cgipsArgs.append(") or blip.pk_cgip in ( '"
								+ pkCgips.get(i).trim() + "'");
					else
						cgipsArgs.append(" blip.pk_cgip in ( '"
								+ pkCgips.get(i).trim() + "'");
				} else {
					cgipsArgs.append(" ,'" + pkCgips.get(i).trim() + "'");
				}
			}
			cgipsArgs.append(")");
			sqlSt = new StringBuffer(
					" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' and ("
							+ cgipsArgs.toString() + ")");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,
					DateUtils.strToDate(DateUtils.dateToStr("yyyyMMdd", begin),
							"yyyyMMdd"), DateUtils.strToDate(
							DateUtils.dateToStr("yyyyMMdd", end), "yyyyMMdd"));
			amtSt = amtTrans(amtStMap.get("amtst"));
		} else {
			sqlSt = new StringBuffer(
					" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' ");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,
					DateUtils.strToDate(DateUtils.dateToStr("yyyyMMdd", begin),
							"yyyyMMdd"), DateUtils.strToDate(
							DateUtils.dateToStr("yyyyMMdd", end), "yyyyMMdd"));
			amtSt = amtTrans(amtStMap.get("amtst"));
		}
		if (amtSt.compareTo(stVo.getAmountSt()) != 0) {
			throw new BusException("传入总金额" + stVo.getAmountSt() + "不等于实时计算总金额"
					+ amtSt + "，请确认后继续！");
		}
		ipSettleGzgyMapper.updateBlInfo(paramMap);
	}

	public void insertInvo(List<BlInvoice> invo, List<BlInvoiceDt> invoDt) {
		for (BlInvoice vo : invo) {
			setDefaultValue(vo, true);
		}
		for (BlInvoiceDt vo : invoDt) {
			setDefaultValue(vo, true);
		}
		DataBaseHelper.batchUpdate(
				DataBaseHelper.getInsertSql(BlInvoice.class), invo);
		DataBaseHelper.batchUpdate(
				DataBaseHelper.getInsertSql(BlInvoiceDt.class), invoDt);
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

	public static void piAccDetailVal(BlDeposit dp) {

		String getPiA = "Select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)";
		PiAcc pa = DataBaseHelper.queryForBean(getPiA, PiAcc.class,
				dp.getPkPi());
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

	/**
	 * 交易号：007003003029 查询药品限额
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Double qryDrugquota(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");

		Map<String, Object> drugquotaList = DataBaseHelper.queryForMap(
				"select drugquota from ins_gzgy_pv where pk_pv=?", pkPv);

		Double drugquota = 0D;

		if (drugquotaList != null && drugquotaList.size() > 0)
			drugquota = CommonUtils.getDouble((BigDecimal) drugquotaList
					.get("drugquota"));

		return drugquota;
	}

	/**
	 * 交易号：007003003030 保存药品限额
	 * 
	 * @param param
	 * @param user
	 */
	public void saveDrugquota(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		DataBaseHelper
				.execute(
						"update ins_gzgy_pv set drugquota=? where pk_pv=?",
						new Object[] { paramMap.get("drugquota"),
								paramMap.get("pkPv") });
	}

	/**
	 * 交易号：007003003031 查询患者主医保是否是公医医保
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> qryPvInsuAttrVal(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		Map<String, Object> res = new HashMap<>();
		if (paramMap.get("isFlag") != null
				&& !CommonUtils
						.isEmptyString(paramMap.get("isFlag").toString()))
			res = ipSettleGzgyMapper.qryPvInsuAttrVal(paramMap);
		else {
			res = ipSettleGzgyMapper.qryPvEuHptype(paramMap);
			Map<String, Object> valMap = ipSettleGzgyMapper
					.qryPvByzgFlag(paramMap);
			if (valMap != null && valMap.size() > 0)
				res.put("valAttr", CommonUtils.getString(valMap.get("valAttr")));
		}

		return res;
	}

	/**
	 * 交易号：007003003034
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String dealZcSettleData(String param, IUser user) {

		// 1.参数接收
		SettleInfo allData = JsonUtil.readValue(param, SettleInfo.class);
		// BlDeposit fromSettle = allData.getDeposit();//收退找零金额
		List<BlDeposit> depoList = allData.getDepoList();// 保存多条支付
		BlDeposit depositAcc = allData.getDepositAcc();// 账户支付金额
		String pkPv = allData.getPkPv();// 就诊主键
		String euSttype = allData.getEuSttype();// 结算类型
		String euStresult = allData.getEuStresult();// 结算结果类型
		String dateBegin = allData.getDateBegin();
		String dateEnd = allData.getDateEnd();// 结算截止时间
		String flagHbPrint = allData.getFlagHbPrint();// 是否合并特诊发票
		// String dateBegin = "";//开始时间
		List<InvInfoVo> invoInfos = allData.getInvos();// 发票信息
		BlExtPay blExtPay = allData.getBlExtPay();// 第三方支付交易记录 xr+
		String flagSpItemCg = allData.getFlagSpItemCg();
		List<String> pkCgips = allData.getPkCgips();
		List<String> pkDepoList = allData.getPkDepoList();

		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		/** 校验患者就诊状态是否是结束 */
		if (EnumerateParameter.ZERO.equals(euSttype) && !EnumerateParameter.TWO.equals(pvvo.getEuStatus()))
			throw new BusException("患者就诊状态发生变化，请刷新页面重新结算！");

		/** 校验患者是否结算过 */
		if (BlcgUtil.converToTrueOrFalse(pvvo.getFlagSettle()))
			throw new BusException("该患者已结算过，不可重复结算！");

		if (CommonUtils.isEmptyString(dateBegin)){
			dateBegin = dateTrans(pvvo.getDateBegin());
		}

		/** 特殊项目结算逻辑：分解医保支付金额(单位支付、医保支付) */
		if (BlcgUtil.converToTrueOrFalse(flagSpItemCg)) {
			// dateEnd = 出院日期
			dateEnd = DateUtils.dateToStr("yyyyMMddHHmmss", pvvo.getDateEnd());
			// 特殊项目结算时查询有没有关联的退费主键，有则把退费的明细一并结算
			List<String> backList = ipSettleGzgyMapper.qryPkCgBack(pkCgips);
			if (backList != null && backList.size() > 0){
				pkCgips.addAll(backList);
			}
		}

		// 2.结算数据准备 结算明细数据准备
		// 2.1结算数据准备，处理了账户支付后，患者账户相关pi_acc,pi_acc_detial表
		Map<String, Object> stDataMap = settleData2(allData, pkPv, euSttype,
				dateBegin, dateEnd, user, euStresult, pkCgips);
		List<BlSettleDetail> stDtVos = (List<BlSettleDetail>) stDataMap
				.get("detail");
		BlSettle stVo = (BlSettle) stDataMap.get("settle");
		for (BlSettleDetail vo : stDtVos) {
			setDefaultValue(vo, true);
			vo.setPkStdt(NHISUUID.getKeyId());
		}
		setDefaultValue(stVo, true);
		// 2.2结算数据写入
		DataBaseHelper.insertBean(stVo);
		DataBaseHelper.batchUpdate(
				DataBaseHelper.getInsertSql(BlSettleDetail.class), stDtVos);

		// 校验系统参数BL0040(0:不结转预交金 1:结转预交金)
		String isStPre = ApplicationUtils.getSysparam("BL0040", false);
		// 3.结算结果类型判断，更新付款表-bl_deposit
		// 3.1更新收款表
		updDepositInfo(pkPv, stVo.getPkSettle(), dateTrans(dateBegin),
				dateTrans(dateEnd), pkDepoList, isStPre, euSttype);
		// 3.2写入结算时账户支付的收付款记录（结算信息标志已加入）
		if (depositAcc != null) {
			setDepoInfo(depositAcc, stVo);
			depositAcc.setEuDptype(euSttype);
			setDefaultValue(depositAcc, true);
			DataBaseHelper.insertBean(depositAcc);
		}
		// 3.3写入结算时，多退少补的收付款记录（结算信息标志已加入）
		if ((depoList != null && depoList.size() > 0)
				|| stVo.getEuStresult().equals(EnumerateParameter.ONE)) {
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

					DataBaseHelper.batchUpdate(
							DataBaseHelper.getInsertSql(BlDeposit.class),
							depoList);
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
					DataBaseHelper.batchUpdate(
							DataBaseHelper.getInsertSql(BlDeposit.class),
							depoList);
					// DataBaseHelper.insertBean(fromSettle);
				}

			} else if (EnumerateParameter.ONE.equals(euSttype)) {// 中途结算，少收多不退，但是存预交金
				// 参数值为1时，可以将退款金额结转为预交金，故更新票据使用号
				if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals(EnumerateParameter.ONE)) {
					/** 校验是否需要更新发票数据 */
					for (BlDeposit vo : depoList) {
						if (EnumerateParameter.NEGA.equals(vo.getEuDirect())) {
							// 调用发票使用确认服务，完成发票更新。
							commonService.confirmUseEmpInv(depoList.get(0)
									.getPkEmpinvoice(), new Long(1));
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
						rtn.setCodeDepo(null);
						rtn.setPkEmpinv(null);
						setDefaultValue(rtn, true);
						DataBaseHelper.insertBean(rtn);
						// 参数值为1时，退款信息结转为预交金
						if (!CommonUtils.isEmptyString(isStPre)
								&& isStPre.equals(EnumerateParameter.ONE)) {
							// 3.3.2 存一笔预交金的收款记录
							fromSettle.setEuDptype(EnumerateParameter.NINE);
							fromSettle.setEuDirect(EnumerateParameter.ONE);
							fromSettle.setBankNo(null);
							fromSettle.setPayInfo(null);
							fromSettle.setPkStMid(stVo.getPkSettle());
							BigDecimal amt = fromSettle.getAmount();
							fromSettle.setAmount(amt
									.multiply(new BigDecimal(-1)));
							fromSettle.setNote("中途结算转存");
							fromSettle.setFlagSettle(EnumerateParameter.ZERO);
							fromSettle.setPkSettle(null);
							fromSettle.setDateReptBack(null);
							// 收款时生成流水号0606
							if (CommonUtils.isEmptyString(fromSettle
									.getCodeDepo()))
								fromSettle.setCodeDepo(ApplicationUtils
										.getCode("0606"));
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
				// 参数值为0时，因为退款信息不结转预交金，故过滤掉退款信息
				if (!CommonUtils.isEmptyString(isStPre) && isStPre.equals(EnumerateParameter.ZERO)) {
					for (int i = depoList.size() - 1; i >= 0; i--) {
						if (depoList.get(i).getEuDirect().equals(EnumerateParameter.NEGA))
							depoList.remove(depoList.get(i));
					}
				}
				if (depoList != null && depoList.size() > 0)
					DataBaseHelper.batchUpdate(
							DataBaseHelper.getInsertSql(BlDeposit.class),
							depoList);

				// DataBaseHelper.insertBean(fromSettle);
			}
			if (blExtPay != null) {
				for (BlDeposit fromSettle : depoList) {
					// 处理第三方支付交易数据
					saveBlExtPay(fromSettle.getPkDepo(), blExtPay);
				}
			}

		}
		// 处理医保结算业务,根据医保配置文件yb.properties配置医保处理类进行处理相应医保业务
		YBProcessUtils.dealYBSettleMethod(allData, stVo);

		// 4.更新住院记费表
		updChargeInfo(pkPv, stVo, dateTrans(dateBegin),
				dateTrans(EnumerateParameter.ZERO.equals(euSttype) ? "20991231235959" : dateEnd),
				pkCgips);

		if (invoInfos != null && invoInfos.size() > 0) {
			// 5发票数据准备
			Map<String, Object> invMap = invSettltService.invoData(pkPv,
					pvvo.getFlagSpec(), invoInfos, user, dateBegin, dateEnd,
					stVo.getPkSettle(), flagHbPrint);
			List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");
			List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>) invMap.get("invDt");
			// 5.1写发票表bl_invoice和发票明细表bl_invoice_dt
			insertInvo(invo, invoDt);
			// 6.写发票和结算关系表bl_st_inv；
			insertInvoSt(invo, stVo);
			// 7.调用发票使用确认服务，完成发票更新。
			commonService.confirmUseEmpInv(invoInfos.get(0).getInv()
					.getPkEmpinv(), new Long(invo.size()));
		}

		// 8.结算结果类型判断，存款结算 调用账户充值服务，将预交金合计-结算金额合计的差额记入患者账户,已处理

		// 9.更新pv表的结算字段；
		if (!EnumerateParameter.ONE.equals(euSttype)) {
			DataBaseHelper
					.execute(
							"update pv_encounter set flag_settle = '1',eu_status = '3',flag_in='0' where pk_pv = ? ",
							pvvo.getPkPv());
		}

		/* HL7消息发送-数据组装 */
		User userinfo = (User) user;
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
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("doCode", userinfo.getCodeEmp());
		paramMap.put("doName", userinfo.getNameEmp());
		if (invoInfos != null && invoInfos.get(0) != null
				&& invoInfos.get(0).getInv() != null) {
			paramMap.put("settleNo", invoInfos.get(0).getInv().getCurCodeInv());
		}
		paramMap.put("totalAmount", allData.getAmountSt());// 总费用
		paramMap.put("selfAmount", allData.getAmountPi());// 自费
		paramMap.put("amtInsu", allData.getAmountInsu());// 医保费用
		paramMap.put("amtType", amtType);// 结算类别
		paramMap.put("amtKind", amtKind);// 结算方式
		paramMap.put("pkPv", allData.getPkPv());// 就诊主键
		PlatFormSendUtils.sendBlSettleMsg(paramMap);
		paramMap = null;

		return stVo.getPkSettle();

	}

	/**
	 * 
	 * @param pkPv
	 *            患者就诊
	 * @param euSttype
	 *            结算类型 0出院结算，1中途结算
	 * @param dateEndTime
	 *            中途结算时间
	 * @return
	 */
	public Map<String, Object> settleData2(SettleInfo settleInfo, String pkPv,
			String euSttype, String dateBegin, String dateEnd, IUser user,
			String euStresult, List<String> pkCgips) {
		// 1.1查询患者就诊信息
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		Date dateEndTime = dateTrans(dateEnd);
		User userInfo = (User) user;
		BlSettle blSettle = new BlSettle();
		blSettle.setDateBegin(dateTrans(dateBegin));
		blSettle.setPkPv(pkPv);// 当前患者就诊主键
		// 预缴金额AmtPrep

		Map<String, Object> amtPrepMap = DataBaseHelper
				.queryForMap(
						"select sum(depo.amount)  AmtPrep from bl_deposit depo where depo.pk_pv = ? ",
						pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		blSettle.setAmountPrep(amtPrep);
		// 1.生成结算信息

		blSettle.setPkSettle(NHISUUID.getKeyId());
		// 结算编码
		blSettle.setCodeSt(ApplicationUtils.getCode("0605"));
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

		blSettle.setAmountPrep(settleInfo.getAmountPrep()); // 患者预交金合计
		blSettle.setAmountSt(settleInfo.getAmountSt());// 患者费用合计
		blSettle.setAmountPi(settleInfo.getAmountPi());// 患者自付合计
		// blSettle.setAmountInsu(settleInfo.getAmountInsuThird()==null?BigDecimal.ZERO:settleInfo.getAmountInsuThird());//第三方医保接口返回的医保支付
		blSettle.setAmountInsu(BigDecimal.valueOf(MathUtils.sub(settleInfo
				.getAmountSt().doubleValue(), settleInfo.getAmountPi()
				.doubleValue())));
		blSettle.setAmountDisc(MathUtils.sub(blSettle.getAmountSt()
				.doubleValue(), MathUtils.add(blSettle.getAmountPi()
				.doubleValue(), blSettle.getAmountInsu().doubleValue())));
		BigDecimal amtAcc = settleInfo.getAmountAcc();
		// 校验是否是特殊项目结算
		// if(!CommonUtils.isEmptyString(settleInfo.getFlagSpItemCg()) &&
		// BlcgUtil.converToTrueOrFalse(settleInfo.getFlagSpItemCg()) &&
		// settleInfo.getAmountUnit()!=null &&
		// Double.compare(settleInfo.getAmountUnit().doubleValue(), 0D)!=0){
		// blSettle.setAmountPi((settleInfo.getAmountPi().add(settleInfo.getAmountUnit())).setScale(2,BigDecimal.ROUND_HALF_UP));//患者自付合计
		// blSettle.setAmountInsu(blSettle.getAmountSt().subtract(blSettle.getAmountPi()));
		// }

		if (amtAcc != null && amtAcc.compareTo(BigDecimal.ZERO) > 0) {
			// 调用患者账户服务
			patiAccountChange(amtAcc, pvvo);
		}

		// 计算特诊总费用
		String begin = dateBegin.substring(0, 8) + "000000";// 格式化开始时间
		Double amountAdd = blIpPubMapper.qryAmountAddByPv(pkCgips, pkPv,
				dateTrans(begin),
				"0".equals(euSttype) ? dateTrans("20991231235959")
						: dateEndTime);
		blSettle.setAmountAdd(amountAdd);

		blSettle.setFlagCanc("0");
		blSettle.setReasonCanc(null);
		blSettle.setPkSettleCanc(null);
		blSettle.setFlagArclare("0");
		blSettle.setFlagCc("0");
		blSettle.setPkCc(null);
		// 2.生成结算明细 并处理相关金额赋值
		// List<BlSettleDetail> detailVos = handleSettleDetailC(pkPv, blSettle,
		// userInfo.getPkOrg(),euSttype,dateBegin,dateEnd,pkCgips);
		List<BlSettleDetail> detailVos = getSettleDetail(blSettle, pvvo);
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("settle", blSettle);
		resMap.put("detail", detailVos);
		return resMap;

	}

	/**
	 * 组装bl_settle_Detail信息
	 * 
	 * @param blSettle
	 * @param pvvo
	 * @return
	 */
	private List<BlSettleDetail> getSettleDetail(BlSettle blSettle,
			PvEncounter pvvo) {
		List<BlSettleDetail> dts = new ArrayList<>();
		if (MathUtils.compareTo(blSettle.getAmountInsu().doubleValue(), 0D) > 0) { // 医保支付
			// 组装医保支付明细
			BlSettleDetail dtInsu = new BlSettleDetail();
			dtInsu.setPkSettle(blSettle.getPkSettle());
			dtInsu.setPkInsurance(pvvo.getPkInsu()); // 主医保
			// 查询主医保付款方
			Map<String, Object> retMap = DataBaseHelper.queryForMap(
					"select pk_payer from bd_hp where pk_hp = ?",
					pvvo.getPkInsu());

			String pkPayer = "";
			if (retMap != null && retMap.size() > 0
					&& retMap.get("pkPayer") != null)
				pkPayer = CommonUtils.getString(retMap.get("pkPayer"));

			dtInsu.setPkPayer(pkPayer); // 付款方
			dtInsu.setAmount(blSettle.getAmountInsu().doubleValue()); // 医保支付金额
			dts.add(dtInsu);
		}

		if ((MathUtils.compareTo(blSettle.getAmountPi().doubleValue(), 0D) > 0)) {// 患者自付
			// 组装患者支付明细
			BlSettleDetail dt = new BlSettleDetail();
			dt.setPkSettle(blSettle.getPkSettle());
			dt.setAmount(blSettle.getAmountPi().doubleValue()); // 医保支付金额
			// 查询付款方为本人的主键
			Map<String, Object> retMap = DataBaseHelper
					.queryForMap(
							"select pk_payer from bd_payer where code = '00' or name like '%本人'",
							new Object[] {});

			String pkPayer = "";
			if (retMap != null && retMap.size() > 0
					&& retMap.get("pkPayer") != null)
				pkPayer = CommonUtils.getString(retMap.get("pkPayer"));

			// 如果没有医保支付金额，则写主医保主键
			if (MathUtils.compareTo(blSettle.getAmountInsu().doubleValue(), 0D) <= 0) {
				dt.setPkInsurance(pvvo.getPkInsu()); // 主医保
				// 查询主医保付款方
				Map<String, Object> payerMap = DataBaseHelper.queryForMap(
						"select pk_payer from bd_hp where pk_hp = ?",
						pvvo.getPkInsu());

				if (payerMap != null && payerMap.size() > 0
						&& payerMap.get("pkPayer") != null)
					pkPayer = CommonUtils.getString(payerMap.get("pkPayer"));
			}

			dt.setPkPayer(pkPayer); // 付款方
			dts.add(dt);
		}

		// 优惠明细
		if (MathUtils.compareTo(blSettle.getAmountDisc(), 0D) > 0) {
			BlSettleDetail dtDisc = new BlSettleDetail();
			dtDisc.setPkSettle(blSettle.getPkSettle());
			dtDisc.setAmount(blSettle.getAmountDisc());
			// 查询患者优惠对应的付款方
			StringBuffer sqlCate = new StringBuffer(
					" select bh.pk_hp,bh.pk_payer from pi_cate picate");
			sqlCate.append(" inner join bd_hp bh on bh.pk_hp = picate.pk_hp ");
			sqlCate.append(" where picate.pk_picate = ?");
			Map<String, Object> cateMap = DataBaseHelper.queryForMap(
					sqlCate.toString(), pvvo.getPkPicate());
			if (cateMap != null && cateMap.size() > 0) {
				dtDisc.setPkInsurance(cateMap.get("pkHp") != null ? CommonUtils
						.getString(cateMap.get("pkHp")) : null); // 优惠主键
				dtDisc.setPkPayer(cateMap.get("pkPayer") != null ? CommonUtils
						.getString(cateMap.get("pkPayer")) : null);
			}
			dts.add(dtDisc);
		}

		return dts;
	}

	private void updChargeInfo2(String pkPv, String pkSettle, Date begin,
			Date end) {
		String sql = "update bl_ip_dt set flag_settle = 1, pk_settle = ? where  flag_settle <> 1 and pk_pv = ? and  date_cg >= ? and date_cg <= ?";
		DataBaseHelper.execute(sql, pkSettle, pkPv, begin, end);

	}

	private List<BlSettleDetail> handleSettleDetailC(String PkPv,
			BlSettle blSettle, String pkOrg, String euSttype, String begin,
			String end, List<String> pkCgips) {
		Map<String, BlSettleDetail> res = new HashMap<String, BlSettleDetail>();
		// 1.查询记费表的数据，组织记费阶段的数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkPv", PkPv);
		params.put("dateBegin", begin);
		params.put("dateEnd", end);
		params.put("euSttype", euSttype);
		params.put("pkCgips", pkCgips);
		List<BlIpDt> cgVos = blIpPubMapper.qryCgIps(params);
		Double amtDisc = 0.0;
		Double amtHp = 0.0;
		Double patiSelf = 0.0;
		for (BlIpDt vo : cgVos) {
			Double amtHpTemp = 0.0;
			if (1.0 == vo.getRatioSelf()) {
				Double price = MathUtils.sub(vo.getPriceOrg(), vo.getPrice());
				amtHpTemp = MathUtils.mul(price, vo.getQuan());
			} else {
				amtHpTemp = MathUtils.mul(
						MathUtils.mul(vo.getQuan(), vo.getPrice()),
						MathUtils.sub(1.0, vo.getRatioSelf()));
			}
			amtHp = MathUtils.add(amtHp.doubleValue(), amtHpTemp);
			amtDisc = MathUtils
					.add(amtDisc,
							MathUtils.sub(
									MathUtils.sub(vo.getAmount(),
											vo.getAmountPi()), amtHpTemp));
			patiSelf = MathUtils.add(patiSelf, vo.getAmountPi());
		}

		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"Select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, PkPv);
		List<BlSettleDetail> resvo = new ArrayList<BlSettleDetail>();

		// 2.1处理患者优惠的记费明细数据
		if (amtDisc > 0) {
			BlSettleDetail voDisc = new BlSettleDetail();
			voDisc.setAmount(amtDisc.doubleValue());
			StringBuffer sqlCate = new StringBuffer(
					" select picate.pk_hp, bh.pk_payer, bh.eu_hptype from pi_cate picate");
			sqlCate.append(" inner join bd_hp bh on bh.pk_hp = picate.pk_hp ");
			sqlCate.append(" where picate.pk_picate = ?");
			List<Map<String, Object>> discHpMap = DataBaseHelper.queryForList(
					sqlCate.toString(), pvvo.getPkPicate());
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
			String pkPayer = (majorHp != null && majorHp.get("pkPayer") != null) ? (String) majorHp
					.get("pkPayer") : "";
			String pkHp = (majorHp != null && majorHp.get("pkHp") != null) ? (String) majorHp
					.get("pkHp") : "";
			voHp.setPkPayer(pkPayer);
			voHp.setPkInsurance(pkHp);
			res.put(pkHp, voHp);
		}

		// 3.循环处理内部医保的结算策略和<=3的各类型医保
		for (Map<String, Object> map : hpPlans) {
			// 3.1数据准备
			String pkPayer = (map != null && map.get("pkPayer") != null) ? (String) map
					.get("pkPayer") : "";
			String pkHp = (map != null && map.get("pkHp") != null) ? (String) map
					.get("pkHp") : "";

			int EuHptype = -1;
			if (map.get("euHptype") != null) {
				EuHptype = Integer.parseInt((String) map.get("euHptype"));
			}

			// 3.2处理结算策略的各类数据
			if (EuHptype > 3) {// 该情况下可出现4,9
				BigDecimal amountInnerD = priceStrategyService
						.qryPatiSettlementAmountAllocationInfo(new BigDecimal(
								patiSelf), pvvo.getPkOrg(), Constant.IP, pkHp);
				if (patiSelf > amountInnerD.doubleValue()) {
					// 返回的自付金额要是比当前的小，则是有分摊的，故写入一些明细记录
					BlSettleDetail voInner = null;
					Double hpTemp = MathUtils.sub(patiSelf,
							amountInnerD.doubleValue());
					if (res.get(pkHp) != null) {
						voInner = res.get(pkHp);
						voInner.setAmount(MathUtils.add(voInner.getAmount(),
								hpTemp));
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
				BigDecimal amtHp3 = hpMap.get("amtHps") == null ? BigDecimal.ZERO
						: (BigDecimal) hpMap.get("amtHps");
				if (amtHp3.compareTo(BigDecimal.ZERO) > 0) {
					BlSettleDetail voHp3 = null;
					if (res.get(pkHp) != null) {
						voHp3 = res.get(pkHp);
						voHp3.setAmount(MathUtils.add(amtHp3.doubleValue(),
								voHp3.getAmount()));
						// 给主表赋值
						// blSettle.setAmountInsu(amtHp3.add(blSettle.getAmountInsu()));
					} else {
						voHp3 = new BlSettleDetail();
						voHp3.setAmount(amtHp3.doubleValue());
						voHp3.setPkPayer(pkPayer);
						voHp3.setPkSettle(blSettle.getPkSettle());
						voHp3.setPkInsurance(pkHp);
						// 给主表赋值
						// blSettle.setAmountInsu(amtHp3);
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
		// PkInsurance
		// voSelf.setPkInsurance(null);
		voSelf.setPkInsurance(pvvo.getPkInsu());
		// 当保险计划为空时，付款方应该也为空
		voSelf.setPkPayer(qryBdPayerByEuType(pkOrg));
		res.put("pkSelf", voSelf);

		// 5.给主表赋值
		// blSettle.setAmountPi(new BigDecimal(patiSelf).setScale(2,
		// BigDecimal.ROUND_HALF_UP));//不保留两位小数，保存的时候会报错
		resvo.addAll(res.values());

		return resvo;
	}

	/**
	 * 交易号：007003003038 查询患者费用明细 患者就诊主键，开始日期，结束日期 不能为空
	 * 
	 * @param map
	 *            {pkPvs,dateBegin,dateEnd,type：0非物品，1物品}
	 * @return
	 */
	public List<Map<String, Object>> queryBlCgIpDetailsFT(String param,
			IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		map.put("pkHp", "");
		map.put("attrVal", "");// 广州公医标志
		String flagSpec = CommonUtils.getString(map.get("flagSpec"));
		if (CommonUtils.isNotNull(map.get("dateBegin"))) {
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin"))
					.substring(0, 8) + "000000");
		}
		if (CommonUtils.isNotNull(map.get("dateEnd"))) {
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd"))
					.substring(0, 8) + "235959");
		}
		if (CommonUtils.isNotNull(map.get("hpdicttype"))
				&& "1".equals(CommonUtils.getString(map.get("hpdicttype")))) {
			// 查询患者主医保
			Map<String, Object> hpMap = ipSettleGzgyMapper
					.qryHpInfoByPv(CommonUtils.getString(map.get("pkPv")));
			if (hpMap != null && hpMap.size() > 0) {
				map.put("pkHp", hpMap.get("pkHp"));
				map.put("attrVal", hpMap.get("valAttr"));
			}
		}
		if(!map.containsKey("flagShenzhen")){
			map.put("flagShenzhen", "0");
		}

		List<Map<String, Object>> result = new ArrayList<>();

		if (CommonUtils.isEmptyString(flagSpec))
			result = ipSettleGzgyMapper.queryBlCgIpDetails(map);
		else if (flagSpec.equals("1"))
			result = ipSettleGzgyMapper.queryBlCgIpDetailsT(map);
		else if (flagSpec.equals("0"))
			result = ipSettleGzgyMapper.queryBlCgIpDetailsFT(map);

		/** 省公医处理逻辑：如果项目可报销，标志显示为空，否则显示自费 */
		if (result != null && result.size() > 0) {
			String pkPv = CommonUtils.getString(result.get(0).get("pkPv"));
			// 获取系统参数BL0033（广州医保省公医上级编码）
			String sysParam = ApplicationUtils.getSysparam("BL0033", true);
			// 查询患者就诊医保信息
			Map<String, Object> hpMap = ipSettleGzgyMapper.qryHpInfoByPv(pkPv);
			if (hpMap != null
					&& hpMap.size() > 0
					&& hpMap.get("valAttr") != null
					&& "1".equals(CommonUtils.getString(hpMap.get("valAttr"))) // 公医
					&& hpMap.get("faCode") != null
					&& (CommonUtils.getString(hpMap.get("faCode")).equals(
							sysParam) || CommonUtils.getString(
							hpMap.get("faCode")).equals("06"))// 就诊医保上级编码等于参数值
			) {
				for (Map<String, Object> dtMap : result) {
					if (dtMap.get("itemHptype") != null
							&& !("自费".equals(CommonUtils.getString(dtMap
									.get("itemHptype")))))
						dtMap.put("itemHptype", "");
				}
			}

		}

		return result;
	}

	/**
	 * 交易号：007003003037 获取患者费用信息(特诊加收)
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public PatiCardVo getPatiFee(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String flagSpec = JsonUtil.getFieldValue(param, "flagSpec");
		if (CommonUtils.isEmptyString(pkPv))
			throw new BusException("未获取到患者就诊信息！");
		PatiCardVo pativo = new PatiCardVo();
		pativo.setPkPv(pkPv);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPv", pkPv);
		BigDecimal yjj = processNull(ipSettleGzgyMapper.getYjFee(paramMap));
		BigDecimal dbj = processNull(ipSettleGzgyMapper.getDbFee(paramMap));
		BigDecimal zfy = processNull(ipSettleGzgyMapper.getTotalFee(paramMap));
		BigDecimal ztfy_n = processNull(ipSettleGzgyMapper
				.getZtNPdFee(paramMap));
		BigDecimal ztfy = processNull(ipSettleGzgyMapper.getZtPdFee(paramMap));
		BigDecimal gd = processNull(ipSettleGzgyMapper.getGdFee(paramMap));

		BigDecimal totalAmt = new BigDecimal(0);

		if (BlcgUtil.converToTrueOrFalse(flagSpec))
			totalAmt = ipSettleGzgyMapper.getTotalFeeT(paramMap);
		else
			totalAmt = ipSettleGzgyMapper.getTotalFeeFT(paramMap);

		pativo.setAccfee(dbj.doubleValue());
		pativo.setTotalfee(CommonUtils.getDouble(totalAmt));
		pativo.setPrefee(yjj.doubleValue());
		pativo.setGdfee(gd.doubleValue());

		BigDecimal zt = ztfy_n.add(ztfy);
		pativo.setZtfee(zt.doubleValue());

		// 余额 = 预交金-总费用
		BigDecimal ye = yjj.subtract(zfy);
		pativo.setYuefee(ye.doubleValue());
		return pativo;
	}

	/**
	 * 处理空值
	 * 
	 * @param args
	 * @return
	 */
	private BigDecimal processNull(BigDecimal args) {
		if (args == null) {
			args = new BigDecimal(0);
		}
		return args;
	}

	/**
	 * 交易号：007003003039
	 * 
	 * @param param
	 *            DateBegin:'开始日期'DateEnd:'结束日期' PkPv:'就诊主键'
	 * @param user
	 * @return 结算金额 预交金额? 医保支付 患者支付 账户支付 结算应收
	 */
	@SuppressWarnings("unused")
	public Map<String, Object> getAmtInfo(String param, IUser user) {

		Map<String, Object> res = new HashMap<String, Object>();
		User userinfo = (User) user;
		@SuppressWarnings("unchecked")
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);

		String pkPv = (String) paraMap.get("pkPv");
		String dateBegin = CommonUtils.getString(paraMap.get("dateBegin"))
				.substring(0, 8) + "000000";
		Date begin = dateTrans(dateBegin);
		String dateEnd = (String) paraMap.get("dateEnd");
		Date end = dateTrans(dateEnd);
		String euSttype = (String) paraMap.get("euSttype");
		Double amtInsuThird = Double
				.valueOf(paraMap.get("amtInsuThird") == null ? "0" : paraMap
						.get("amtInsuThird").toString());// xr+ 第三方医保有前端传过来

		// 校验是否是出院结算，如果是出院结算则把end赋值为20991231235959
		if (!CommonUtils.isEmptyString(euSttype) && "10".equals(euSttype)) {
			end = dateTrans("20991231235959");
		}

		// 定义一些公共变量
		StringBuffer sqlSt;
		Map<String, Object> amtStMap;
		BigDecimal amtSt;// 结算金额AmtSt
		// String cgips =
		// paraMap.get("pkCgips").toString().substring(paraMap.get("pkCgips").toString().indexOf("[")+1,paraMap.get("pkCgips").toString().lastIndexOf("]"));
		List<String> cgipsArgs = (List<String>) paraMap.get("pkCgips");
		// 本次結算的預交金主鍵
		List<String> pkDepoList = (List<String>) paraMap.get("pkDepoList");
		String flagSpItemCg = CommonUtils
				.getString(paraMap.get("flagSpItemCg"));

		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);

		// String[] cgipsArgs = cgips.split(",");
		if (cgipsArgs != null) {
			res.put("pkCgips", cgipsArgs);
			StringBuffer pkCgips = new StringBuffer("");
			for (int i = 0; i < cgipsArgs.size(); i++) {
				if ((i) % 999 == 0) {
					if (i != 0)
						pkCgips.append(") or blip.pk_cgip in ( '"
								+ cgipsArgs.get(i).trim() + "'");
					else
						pkCgips.append(" blip.pk_cgip in ( '"
								+ cgipsArgs.get(i).trim() + "'");
				} else {
					pkCgips.append(" ,'" + cgipsArgs.get(i).trim() + "'");
				}
			}
			pkCgips.append(")");
			// 结算金额AmtSt
			sqlSt = new StringBuffer(
					" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' and ("
							+ pkCgips.toString() + ")");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,
					begin, end);
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			// 冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv

			StringBuffer strikeInvSt = new StringBuffer(
					" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1 and ("
							+ pkCgips.toString() + ")");
			strikeInvSt
					.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			Map<String, Object> strikeInvStMap = DataBaseHelper.queryForMap(
					strikeInvSt.toString(), pkPv, begin, end);
			BigDecimal strikeInvStAmt = amtTrans(strikeInvStMap.get("amtst"));
			res.put("AmtStrikeInv", strikeInvStAmt);
		} else {
			// 结算金额AmtSt
			sqlSt = new StringBuffer(
					" select Sum(amount) AmtSt from bl_ip_dt blip where blip.flag_settle = '0' ");
			sqlSt.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			amtStMap = DataBaseHelper.queryForMap(sqlSt.toString(), pkPv,
					begin, end);
			amtSt = amtTrans(amtStMap.get("amtst"));
			res.put("AmtSt", amtSt);
			// 冲账结算发票金额=新增收退金额+被冲账结算金额AmtStrikeInv

			StringBuffer strikeInvSt = new StringBuffer(
					" select Sum(amount) AmtSt from bl_ip_dt blip where 1=1  ");
			strikeInvSt
					.append(" and blip.pk_pv = ?  and blip.date_hap >= ? and blip.date_hap <= ?");
			Map<String, Object> strikeInvStMap = DataBaseHelper.queryForMap(
					strikeInvSt.toString(), pkPv, begin, end);
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
		Map<String, Object> amtPrepMap = DataBaseHelper.queryForMap(
				sqlPrep.toString(), pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		res.put("AmtPrep", amtPrep);
		// //医保支付 第三方医保报销金额 + 内部医保报销金额；
		// //第三方医保报销金额由第三方医保接口返回；AmtInsu
		BlSettle tempSt = new BlSettle();

		if (tempSt.getAmountInsu() != null
				&& tempSt.getAmountInsu().compareTo(BigDecimal.ZERO) != 0) {
			res.put("AmtInsuThird", tempSt.getAmountInsu());
		} else {
			res.put("AmtInsuThird", BigDecimal.ZERO);
		}

		BigDecimal amountPi = new BigDecimal(0);
		BigDecimal amtInsu = new BigDecimal(0);

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("pkPv", pkPv);

		// 查询是否是本院职工
		Map<String, Object> retMap = ipSettleGzgyMapper.qryPvByzgFlag(paramMap);

		String attrVal = "";
		Map<String, Object> valMap = ipSettleGzgyMapper
				.qryPvInsuAttrVal(paramMap);
		if (valMap != null && valMap.size() > 0
				&& valMap.get("valAttr") != null)
			attrVal = CommonUtils.getString(valMap.get("valAttr"));

		// 只有广州公医或本院职工走药费限额
		if (attrVal.equals("1")
				|| (retMap != null && retMap.size() > 0 && CommonUtils
						.getString(retMap.get("valAttr")).equals("1"))) {
			// 计算amt_pi和amt_hp
			Double amtPi = 0D;
			Double amtHp = 0D;
			Double stDrugAmt = 0D;

			/** 住院费用分解 */
			// 1.查询就诊药品费用
			paramMap.put("end", end);
			paramMap.put("begin", begin);
			if (cgipsArgs != null && cgipsArgs.size() > 0)
				paramMap.put("pkList", cgipsArgs);
			List<Map<String, Object>> drugList = ipSettleGzgyMapper
					.qryDrugCostByPv(paramMap);
			Map<String, Object> drugAmtMap = ipSettleGzgyMapper
					.qryStDrugAmt(paramMap);// 查询本次就诊已结算的药费
			if (drugAmtMap != null && drugAmtMap.size() > 0
					&& drugAmtMap.get("stamtdrug") != null)
				stDrugAmt = CommonUtils.getDouble(drugAmtMap.get("stamtdrug"));

			if (drugList != null && drugList.size() > 0) {
				for (Map<String, Object> drugMap : drugList) {
					if (drugMap == null || drugMap.size() <= 0)
						continue;

					if (BlcgUtil.converToTrueOrFalse(CommonUtils
							.getString(drugMap.get("flagSettle"))))
						continue;

					// 获取药费限额(如果为空则没有限制，赋值为999999)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("drugquota")))
							|| CommonUtils.getDouble(drugMap.get("drugquota")) == 0D)
						drugMap.put("drugquota", 999999D);
					// 获取已结算医保药费(如果为空,赋值为0)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("amountInsDrug"))))
						drugMap.put("amountInsDrug", 0D);

					// amount_ins_drug>=drugquota
					if (stDrugAmt >= CommonUtils.getDouble(drugMap
							.get("drugquota"))) {
						amtPi = CommonUtils.getDouble((BigDecimal) drugMap
								.get("amount"));
						amtHp = 0D;
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) >= CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// >=drugquota
						// amt_pi=amount_ins_drug+ amount_drug
						// -drugquota+((drugquota-amount_ins_drug)*ratio_pi)
						// +(amount-amount_drug)
						amtPi = MathUtils.add(MathUtils.add(MathUtils.sub(
								MathUtils.add(stDrugAmt, CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("drugquota"))), MathUtils.mul(
								MathUtils.sub(CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("drugquota")), stDrugAmt),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("ratioPi")))), MathUtils.sub(
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("amount")), CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))));
						// amt_hp= amount - amt_pi
						amtHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amtPi);
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) < CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// <drugquota
						// amt_pi= amount_drug *ratio_pi+(amount-amount_drug)
						amtPi = MathUtils.add(
								MathUtils.mul(CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug")),
										CommonUtils
												.getDouble((BigDecimal) drugMap
														.get("ratioPi"))),
								MathUtils.sub(CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amount")), CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))));

						// amt_hp= amount -amt_pi
						amtHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amtPi);
					}
					// 自费金额+加收金额
					amtPi = MathUtils.add(amtPi, CommonUtils
							.getDouble((BigDecimal) drugMap.get("amountAdd")));
				}
			}

			// 2.查询非药品费用
			// Map<String,Object> itemMap =
			// ipSettleGzgyMapper.qryItemCostByPv(paramMap);
			// if(itemMap!=null && itemMap.size()>0){
			// //amt_pi=amount-amount_hp
			// amtPi = MathUtils.add(amtPi,
			// MathUtils.sub(CommonUtils.getDouble((BigDecimal)itemMap.get("amount")),
			// CommonUtils.getDouble((BigDecimal)itemMap.get("amountHp")))
			// );
			// amtHp = MathUtils.add(amtHp,
			// CommonUtils.getDouble((BigDecimal)itemMap.get("amountHp")));
			// }

			// 2.非药品待结算费用策略
			Map<String, Object> amtMap = ipGyItemSettle(pvvo, paramMap);

			amtPi = MathUtils.add(amtPi,
					CommonUtils.getDouble(amtMap.get("amtPi")));
			amtHp = MathUtils.add(amtHp,
					CommonUtils.getDouble(amtMap.get("amtHp")));

			amountPi = BigDecimal.valueOf(MathUtils.sub(
					CommonUtils.getDouble(amtPi), amtInsuThird));
			amountPi = amountPi.setScale(2, BigDecimal.ROUND_HALF_UP);
			res.put("AmtPi",
					amountPi.compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal(0)
							: amountPi);

			amtInsu = BigDecimal.valueOf(MathUtils.add(MathUtils.sub(
					CommonUtils.getDoubleObject(amtSt),
					CommonUtils.getDoubleObject(amountPi)), amtInsuThird));
			amtInsu = amtInsu.setScale(2, BigDecimal.ROUND_HALF_UP);
			res.put("AmtInsu", amtInsu);
		} else {
			paramMap.put("pkPv", pkPv);
			paramMap.put("end", end);
			paramMap.put("begin", begin);
			if (cgipsArgs != null && cgipsArgs.size() > 0)
				paramMap.put("pkList", cgipsArgs);

			Map<String, Object> stAmtMap = ipSettleGzgyMapper
					.qryStInfoByPv(paramMap);

			Double amtPi = 0D;
			Double amtHp = 0D;

			if (stAmtMap != null && stAmtMap.size() > 0) {
				amtPi = MathUtils.sub(CommonUtils
						.getDoubleObject((BigDecimal) stAmtMap.get("amount")),
						CommonUtils.getDoubleObject((BigDecimal) stAmtMap
								.get("amountHp")));
				amtHp = CommonUtils.getDoubleObject((BigDecimal) stAmtMap
						.get("amountHp"));
			}

			amountPi = new BigDecimal(amtPi - amtInsuThird);
			amountPi = amountPi.setScale(2, BigDecimal.ROUND_HALF_UP);
			res.put("AmtPi",
					amountPi.compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal(0)
							: amountPi);

			amtInsu = new BigDecimal(amtHp + amtInsuThird);
			amtInsu = amtInsu.setScale(2, BigDecimal.ROUND_HALF_UP);
			res.put("AmtInsu", amtInsu);
		}

		// 校验是否是特殊项目结算
		/** 特殊项目结算逻辑：分解医保支付金额(单位支付、医保支付) */
		if (BlcgUtil.converToTrueOrFalse(flagSpItemCg)) {
			// 查询单位支付比例
			String strRatioUnit = ipSettleGzgyMapper.qryHpRatioUnit(pvvo
					.getPkInsu());
			Double ratioUnit = CommonUtils.isEmptyString(strRatioUnit) ? 0D
					: Double.valueOf(strRatioUnit);

			// 单位支付金额(医保金额*单位支付比例)
			BigDecimal amountUnit = (BigDecimal
					.valueOf(MathUtils.mul(MathUtils.sub(
							CommonUtils.getDouble(res.get("AmtSt")),
							CommonUtils.getDouble(res.get("AmtPi"))), ratioUnit)))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			// 医保支付金额(医保金额 *(1-单位支付比例))
			BigDecimal amountInsu = BigDecimal.valueOf(MathUtils.mul(MathUtils
					.sub(CommonUtils.getDouble(res.get("AmtSt")),
							CommonUtils.getDouble(res.get("AmtPi"))), MathUtils
					.sub(1D, ratioUnit)));

			amountPi = amountUnit.add(BigDecimal.valueOf(CommonUtils
					.getDouble(res.get("AmtPi"))));
			amtInsu = BigDecimal.valueOf(
					CommonUtils.getDouble(res.get("AmtSt"))).subtract(amountPi);

			res.put("AmtPi", amountPi);
			res.put("AmtInsu", amtInsu);
			res.put("AmtUnit", amountUnit);
		}

		List<Map<String, Object>> hpPlans = getHpPlans(pkPv);
		for (Map<String, Object> map : hpPlans) {
			String flagMaj = (String) map.get("flagMaj");
			if ("1".equals(flagMaj)) {
				res.put("PkInsurance", map.get("pkHp"));
			}
		}
		// 账户余额,授权账户的余额也可以使用
		// Map<String,Object> amtAccMap =
		// DataBaseHelper.queryForMap("select  Amt_acc,pk_piacc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?",
		// pkPv);
		PiAcc account = pareAccoutService
				.getPatiAccountAvailableBalanceByPv(pkPv);
		BigDecimal amtAcc = BigDecimal.ZERO;
		String pkAcc = null;
		if (account != null
				&& !PiConstant.ACC_EU_STATUS_2.equals(account.getEuStatus())) {// 存在账户且未冻结的情况
			amtAcc = account.getAmtAcc() == null ? BigDecimal.ZERO : account
					.getAmtAcc();
			pkAcc = account.getPkPiacc();
		}
		res.put("pkAcc", pkAcc);
		// 患者支付 本次结算金额合计-预交金额-医保支付；AmtPi =AmtSt - AmtPrep -AmtInsu
		BigDecimal amtGet = amtSt.subtract(amtPrep).subtract(amtInsu);
		amtGet = amtGet.setScale(2, BigDecimal.ROUND_HALF_UP);

		if (amtGet.compareTo(BigDecimal.ZERO) < 0) {// 预交金够，患者不用支付，账户支付为0
			res.put("AmtAcc", BigDecimal.ZERO);
			res.put("AmtGet", amtGet);
			return set2Scal(res);
		} else {// 预交金不够
			if (amtAcc.compareTo(BigDecimal.ZERO) > 0) {// 如果患者账户有钱，先用
				amtGet = amtGet.subtract(amtAcc);
				if (amtGet.compareTo(BigDecimal.ZERO) < 0) {// 账户有钱且足以支付应交费用，还需支付剩下的
					res.put("AmtAcc", amtSt.subtract(amtPrep).subtract(amtInsu));
					res.put("AmtGet", BigDecimal.ZERO);
					return set2Scal(res);
				} else {// 账户有钱，但不足以支付应交费用，还需支付0
					res.put("AmtAcc", amtAcc);
					res.put("AmtGet", amtGet);
					return set2Scal(res);
				}
			} else {// 如果患者账户没钱，账户支付为0
				res.put("AmtAcc", BigDecimal.ZERO);
				res.put("AmtGet", amtGet);
				return set2Scal(res);
			}
		}
	}

	/**
	 * 交易号：007003003040 作废当前使用发票
	 * 
	 * @param param
	 * @param user
	 */
	public void canelInv(String param, IUser user) {
		BillInfo bill = JsonUtil.readValue(param, BillInfo.class);

		// 作废发票，写表bl_invoice
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
		
		//获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
		//String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
		String eBillFlag = invSettltService.getBL0031ByNameMachine(bill.getNameMachine());
		if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
			invSettltService.invCanl(bill, user);
		}

		// 7.调用发票使用确认服务，完成发票更新。
		commonService.confirmUseEmpInv(bill.getPkEmpinv(), new Long(1));
	}

	/**
	 * 交易号：007003003041 对就诊患者进行冻结
	 * 
	 * @param param
	 * @param user
	 */
	public Map<String, Object> freezePv(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		DataBaseHelper
				.execute(
						"update pv_ip set flag_frozen='1',date_frozen=?,pk_emp_frozen=?,name_emp_frozen=? where pk_pv=? and flag_frozen='0'",
						new Date(), UserContext.getUser().getPkEmp(),
						UserContext.getUser().getNameEmp(), pkPv);

		Map<String, Object> ret = DataBaseHelper
				.queryForMap(
						"select flag_frozen,date_frozen,pk_emp_frozen,name_emp_frozen from pv_ip where pk_pv = ?",
						new Object[] { pkPv });
		return ret;
	}

	/**
	 * 交易号：007003003042 对就诊患者进行解冻
	 * 
	 * @param param
	 * @param user
	 */
	public Map<String, Object> unFreezePv(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		DataBaseHelper
				.execute(
						"update pv_ip set flag_frozen='0',date_frozen=?,pk_emp_frozen=?,name_emp_frozen=? where pk_pv=? and flag_frozen='1'",
						new Date(), UserContext.getUser().getPkEmp(),
						UserContext.getUser().getNameEmp(), pkPv);

		Map<String, Object> ret = DataBaseHelper
				.queryForMap(
						"select flag_frozen,date_frozen,pk_emp_frozen,name_emp_frozen from pv_ip where pk_pv = ?",
						new Object[] { pkPv });
		return ret;
	}

	/**
	 * 交易码：007003003048 查询患者冻结状态
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> qryPvFrezen(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		Map<String, Object> ret = DataBaseHelper
				.queryForMap(
						"select flag_frozen,date_frozen,pk_emp_frozen,name_emp_frozen from pv_ip where pk_pv = ?",
						new Object[] { pkPv });
		return ret;
	}

	/**
	 * 交易号：007003003044 查询未结算患者发票信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public BlInvSearch qryPvInvInfo(String param, IUser user) {
		// 1.参数接收
		BlInvSearch blInv = JsonUtil.readValue(param, BlInvSearch.class);
		// 获取用户信息
		User userInfo = UserContext.getUser();

		// 获取患者就诊信息
		PvEncounter pv = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, blInv.getPkPv());

		// String dateBegin =
		// CommonUtils.getString(pv.getDateBegin()).substring(0, 8)+"000000";
		String dateBegin = dateTrans(pv.getDateBegin()).substring(0, 8)
				+ "000000";
		String dateEnd = "";
		if ("0".equals(blInv.getDtSttype()))
			dateEnd = "20991231235959";
		else
			dateEnd = dateTrans(blInv.getDateEnd());

		if (BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())
				&& !BlcgUtil.converToTrueOrFalse(blInv.getFlagHbPrint())) // 特诊患者打印两张发票
			return handleAmtBySpec(blInv, userInfo, pv.getPkPv(),
					pv.getFlagSpec(), dateBegin, dateEnd, blInv.getPkCgips());

		// 返回值
		BlInvSearch rtnBlInv = new BlInvSearch();

		List<InvItemVo> itemVos = ipSettleGzgyMapper.QryInvItemInfo(
				pv.getPkPv(), userInfo.getPkOrg(), dateBegin, dateEnd,
				blInv.getPkCgips());

		List<BlInvoice> invs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();
		// Map<String,Object> res = new HashMap<String,Object>();
		BlAmtVo amtRe = ipSettleGzgyMapper.QryAmtAndPi(pv.getPkPv(), dateBegin,
				dateEnd, blInv.getPkCgips());
		double amt = 0.0;
		double amtPi = 0.0;
		if (amtRe != null) {
			amt = amtRe.getAmtSum();
			amtPi = amtRe.getAmtPi();
		}

		Double rate = 1.0;
		if (amt <= 0) {
			rtnBlInv.setInvs(invs);
			rtnBlInv.setInvDts(invDts);
			return rtnBlInv;
		}
		// 重新获取住院发票号
		// List<BillInfo> billList = getInvInfo(invoInfos.size());
		// int count = 0;
		for (InvInfoVo vo : blInv.getInvos()) {
			// 重新赋值
			vo.setInv(vo.getInv());
			// count++;

			rate = MathUtils.div(vo.getAmount().doubleValue(), amt);
			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(userInfo.getPkOrg());
			inv.setPkInvcate(vo.getInv().getPkInvcate());
			inv.setPkEmpinvoice(vo.getInv().getPkEmpinv());
			inv.setCodeInv(vo.getInv().getCurCodeInv());
			inv.setDateInv(new Date());

			inv.setAmountInv(vo.getAmount().doubleValue());
			double amt_pi = MathUtils.mul(rate, amtPi);
			inv.setAmountPi(amt_pi);
			inv.setNote("住院发票" + vo.getNote());
			inv.setPkEmpInv(userInfo.getPkEmp());
			inv.setNameEmpInv(userInfo.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");
			invs.add(inv);
			for (InvItemVo invItem : itemVos) {
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(inv.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				// invDt.setAmount(MathUtils.mul(rate, invItem.getAmount()));
				invDt.setAmount(invItem.getAmount());
				invDts.add(invDt);
			}

		}

		// 查询患者医保是否是广州公医
		String valSql = "select attr.val_attr,hp.eu_hptype from bd_hp hp "
				+ " inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict "
				+ " inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp and tmp.dt_dicttype='03' "
				+ " inner join PV_ENCOUNTER pv on hp.PK_HP = pv.PK_INSU "
				+ " where pv.pk_pv = ? and tmp.code_attr='0301' ";
		Map<String, Object> varAttrMap = DataBaseHelper.queryForMap(valSql,
				pv.getPkPv());

		String varAttr = "";
		String euHptype = "";

		if (varAttrMap != null && varAttrMap.size() > 0
				&& varAttrMap.get("valAttr") != null) {
			varAttr = CommonUtils.getString(varAttrMap.get("valAttr"));
			euHptype = CommonUtils.getString(varAttrMap.get("euHptype"));
		}

		if ((!CommonUtils.isEmptyString(varAttr) && varAttr.equals("1"))
				|| (!CommonUtils.isEmptyString(euHptype) && euHptype
						.equals("4"))) {
			// 计算amt_pi和amt_hp
			Double amountPi = 0D;
			Double amountHp = 0D;
			Double stDrugAmt = 0D;

			// 1.查询就诊药品费用
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("pkPv", pv.getPkPv());
			paramMap.put("begin", dateTrans(dateTrans(blInv.getDateBegin())
					.substring(0, 8) + "000000"));
			paramMap.put("end", DateUtils.strToDate(dateEnd));
			if (blInv.getPkCgips() != null && blInv.getPkCgips().size() > 0)
				paramMap.put("pkList", blInv.getPkCgips());
			List<Map<String, Object>> drugList = ipSettleGzgyMapper
					.qryDrugCostByPv(paramMap);
			Map<String, Object> drugAmtMap = ipSettleGzgyMapper
					.qryStDrugAmt(paramMap);// 查询本次就诊已结算的药费
			if (drugAmtMap != null && drugAmtMap.size() > 0
					&& drugAmtMap.get("stamtdrug") != null)
				stDrugAmt = CommonUtils.getDouble(drugAmtMap.get("stamtdrug"));

			if (drugList != null && drugList.size() > 0) {
				for (Map<String, Object> drugMap : drugList) {
					if (drugMap == null || drugMap.size() <= 0)
						continue;

					if (BlcgUtil.converToTrueOrFalse(CommonUtils
							.getString(drugMap.get("flagSettle"))))
						continue;

					// 获取药费限额(如果为空则没有限制，赋值为999999)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("drugquota")))
							|| CommonUtils.getDouble(drugMap.get("drugquota")) == 0D)
						drugMap.put("drugquota", 999999D);
					// 获取已结算医保药费(如果为空,赋值为0)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("amountInsDrug"))))
						drugMap.put("amountInsDrug", 0D);

					// amount_ins_drug>=drugquota
					if (stDrugAmt >= CommonUtils.getDouble(drugMap
							.get("drugquota"))) {
						amountPi = CommonUtils.getDouble((BigDecimal) drugMap
								.get("amount"));
						amountHp = 0D;
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) >= CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// >=drugquota
						// amt_pi=amount_ins_drug+ amount_drug
						// -drugquota+((drugquota-amount_ins_drug)*ratio_pi)
						// +(amount-amount_drug)
						amountPi = MathUtils.add(MathUtils.sub(MathUtils.add(
								stDrugAmt, CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("drugquota"))), MathUtils.sub(
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("amount")), CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))));
						// amt_hp= amount - amt_pi
						amountHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amtPi);
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) < CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// <drugquota
						// amt_pi= amount_drug *ratio_pi+(amount-amount_drug)
						amountPi = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("amountDrug")));

						// amt_hp= amount -amt_pi
						amountHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amtPi);
					}
					// 自费金额+加收金额
					amountPi = MathUtils.add(amountPi, CommonUtils
							.getDouble((BigDecimal) drugMap.get("amountAdd")));
				}
			}
			rtnBlInv.setCezf(amountPi);// 超额自费药
		}

		rtnBlInv.setFlagGy(varAttr);// 广州公医标志(1为公医)
		rtnBlInv.setInvs(invs);
		rtnBlInv.setInvDts(invDts);
		return rtnBlInv;

	}

	/**
	 * 查询特诊患者发票数据
	 * 
	 * @param invoInfos
	 * @param user
	 * @param pkPv
	 * @param flagSpec
	 * @param dateBegin
	 * @param dateEnd
	 * @param pkCgips
	 */
	private BlInvSearch handleAmtBySpec(BlInvSearch blInv, User user,
			String pkPv, String flagSpec, String dateBegin, String dateEnd,
			List<String> pkCgips) {
		// 返回值
		BlInvSearch rtnBlInv = new BlInvSearch();

		List<InvInfoVo> invoInfos = blInv.getInvos();
		// 非特诊发票明细
		List<InvItemVo> itemVos = ipSettleGzgyMapper.qryPvNotSpecInvItem(pkPv,
				dateBegin, dateEnd, pkCgips);
		// 特诊发票明细
		List<InvItemVo> itemVosT = ipSettleGzgyMapper.qryPvSpecInvItem(pkPv,
				dateBegin, dateEnd, pkCgips);
		// 非特诊总费用
		BlAmtVo amtRe = ipSettleGzgyMapper.qryPvAmtAndPiFT(pkPv, dateBegin,
				dateEnd, pkCgips);
		// 特诊总费用
		BlAmtVo amtReT = ipSettleGzgyMapper.qryPvAmtAndPiT(pkPv, dateBegin,
				dateEnd, pkCgips);

		List<BlInvoice> invs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();
		// Map<String,Object> res = new HashMap<String,Object>();

		// 重新获取住院发票号
		// List<BillInfo> billList = getInvInfo(invoInfos.size());

		/** 封装发票信息：默认第一次循环为非特诊发票，第二次循环为特诊发票 */
		for (int i = 0; i < invoInfos.size(); i++) {
			// 重新获取住院发票服务
			invoInfos.get(i).setInv(invoInfos.get(i).getInv());

			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(user.getPkOrg());
			inv.setPkInvcate(invoInfos.get(i).getInv().getPkInvcate());
			inv.setPkEmpinvoice(invoInfos.get(i).getInv().getPkEmpinv());
			inv.setCodeInv(invoInfos.get(i).getInv().getCurCodeInv());
			inv.setDateInv(new Date());

			inv.setPkEmpInv(user.getPkEmp());
			inv.setNameEmpInv(user.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");

			if (i == 0) {
				inv.setAmountInv(amtRe.getAmtSum());
				inv.setAmountPi(amtRe.getAmtPi());
				inv.setNote("");
				invs.add(inv);
				for (InvItemVo invItem : itemVos) {
					BlInvoiceDt invDt = new BlInvoiceDt();
					invDt.setPkInvoicedt(NHISUUID.getKeyId());
					invDt.setPkInvoice(inv.getPkInvoice());
					invDt.setPkBill(invItem.getPkInvcateitem());
					invDt.setNameBill(invItem.getName());
					invDt.setCodeBill(invItem.getCode());
					invDt.setAmount(invItem.getAmount());
					invDts.add(invDt);
				}
			} else {
				inv.setAmountInv(amtReT.getAmtSum());
				inv.setAmountPi(amtReT.getAmtPi());
				inv.setNote("(特)");
				invs.add(inv);
				for (InvItemVo invItem : itemVosT) {
					BlInvoiceDt invDt = new BlInvoiceDt();
					invDt.setPkInvoicedt(NHISUUID.getKeyId());
					invDt.setPkInvoice(inv.getPkInvoice());
					invDt.setPkBill(invItem.getPkInvcateitem());
					invDt.setNameBill(invItem.getName());
					invDt.setCodeBill(invItem.getCode());
					invDt.setAmount(invItem.getAmount());
					invDts.add(invDt);
				}
			}
		}

		// 查询患者医保是否是广州公医
		String valSql = "select attr.val_attr,hp.eu_hptype from bd_hp hp "
				+ " inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict "
				+ " inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp and tmp.dt_dicttype='03' "
				+ " inner join PV_ENCOUNTER pv on hp.PK_HP = pv.PK_INSU "
				+ " where pv.pk_pv = ? and tmp.code_attr='0301' ";
		Map<String, Object> varAttrMap = DataBaseHelper.queryForMap(valSql,
				pkPv);

		String varAttr = "";
		String euHptype = "";
		if (varAttrMap != null && varAttrMap.size() > 0
				&& varAttrMap.get("valAttr") != null) {
			varAttr = CommonUtils.getString(varAttrMap.get("valAttr"));
			euHptype = CommonUtils.getString(varAttrMap.get("euHptype"));
		}

		if ((!CommonUtils.isEmptyString(varAttr) && varAttr.equals("1"))
				|| (!CommonUtils.isEmptyString(euHptype) && euHptype
						.equals("4"))) {
			// 计算amt_pi和amt_hp
			Double amountPi = 0D;
			Double amountHp = 0D;
			Double stDrugAmt = 0D;

			// 1.查询就诊药品费用
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("pkPv", pkPv);
			paramMap.put("begin", dateTrans(dateTrans(blInv.getDateBegin())
					.substring(0, 8) + "000000"));
			paramMap.put("end", DateUtils.strToDate(dateEnd));
			if (blInv.getPkCgips() != null && blInv.getPkCgips().size() > 0)
				paramMap.put("pkList", blInv.getPkCgips());
			List<Map<String, Object>> drugList = ipSettleGzgyMapper
					.qryDrugCostByPv(paramMap);
			Map<String, Object> drugAmtMap = ipSettleGzgyMapper
					.qryStDrugAmt(paramMap);// 查询本次就诊已结算的药费
			if (drugAmtMap != null && drugAmtMap.size() > 0
					&& drugAmtMap.get("stamtdrug") != null)
				stDrugAmt = CommonUtils.getDouble(drugAmtMap.get("stamtdrug"));

			if (drugList != null && drugList.size() > 0) {
				for (Map<String, Object> drugMap : drugList) {
					if (drugMap == null || drugMap.size() <= 0)
						continue;

					if (BlcgUtil.converToTrueOrFalse(CommonUtils
							.getString(drugMap.get("flagSettle"))))
						continue;

					// 获取药费限额(如果为空则没有限制，赋值为999999)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("drugquota")))
							|| CommonUtils.getDouble(drugMap.get("drugquota")) == 0D)
						drugMap.put("drugquota", 999999D);
					// 获取已结算医保药费(如果为空,赋值为0)
					if (CommonUtils.isEmptyString(CommonUtils.getString(drugMap
							.get("amountInsDrug"))))
						drugMap.put("amountInsDrug", 0D);

					// amount_ins_drug>=drugquota
					if (stDrugAmt >= CommonUtils.getDouble(drugMap
							.get("drugquota"))) {
						amountPi = CommonUtils.getDouble((BigDecimal) drugMap
								.get("amount"));
						amountHp = 0D;
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) >= CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// >=drugquota
						// amt_pi=amount_ins_drug+ amount_drug
						// -drugquota+((drugquota-amount_ins_drug)*ratio_pi)
						// +(amount-amount_drug)
						amountPi = MathUtils.add(MathUtils.sub(MathUtils.add(
								stDrugAmt, CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("drugquota"))), MathUtils.sub(
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("amount")), CommonUtils
										.getDouble((BigDecimal) drugMap
												.get("amountDrug"))));
						// amt_hp= amount - amt_pi
						amountHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amountPi);
					} else if (MathUtils.add(stDrugAmt,
							CommonUtils.getDouble(drugMap.get("amountDrug"))) < CommonUtils
							.getDouble(drugMap.get("drugquota"))) { // amount_ins_drug+
																	// amount_drug
																	// <drugquota
						// amt_pi= amount_drug *ratio_pi+(amount-amount_drug)
						amountPi = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								CommonUtils.getDouble((BigDecimal) drugMap
										.get("amountDrug")));

						// amt_hp= amount -amt_pi
						amountHp = MathUtils.sub(CommonUtils
								.getDouble((BigDecimal) drugMap.get("amount")),
								amountPi);
					}
					// 自费金额+加收金额
					amountPi = MathUtils.add(amountPi, CommonUtils
							.getDouble((BigDecimal) drugMap.get("amountAdd")));
				}
			}
			rtnBlInv.setCezf(amountPi);// 超额自费药
		}

		rtnBlInv.setFlagGy(varAttr);// 广州公医标志(1为公医)
		rtnBlInv.setInvs(invs);
		rtnBlInv.setInvDts(invDts);
		return rtnBlInv;
	}

	/**
	 * 交易号：007003003045 获取患者医疗证号或医保卡号
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String qryPvInsurNoOrMcno(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		// 校验该患者是否是公医患者
		Map<String, Object> retMap = ipSettleGzgyMapper
				.qryPvInsuAttrVal(paramMap);
		if (retMap == null || retMap.size() <= 0)
			throw new BusException("未获取到患者主医保信息！");

		String curNo = "";
		// 公医患者查询医疗证号
		if (!CommonUtils.isEmptyString(CommonUtils.getString(retMap
				.get("valAttr")))
				&& CommonUtils.getString(retMap.get("valAttr")).equals("1")) {
			Map<String, Object> noMap = DataBaseHelper.queryForMap(
					"select MCNO from INS_GZGY_PV where pk_pv = ?", paramMap
							.get("pkPv").toString());
			if (noMap != null && noMap.size() > 0)
				curNo = CommonUtils.getString(noMap.get("mcno"));
		} else {// 非公医查询医保卡号
			Map<String, Object> curMap = DataBaseHelper
					.queryForMap(
							"select insur_no from pi_master pi left join PV_ENCOUNTER pv on pi.pk_pi = pv.pk_pi where pv.pk_pv = ?",
							paramMap.get("pkPv").toString());

			if (curMap != null && curMap.size() > 0
					&& curMap.get("insurNo") != null)
				curNo = CommonUtils.getString(curMap.get("insurNo"));
		}
		return curNo;
	}

	/**
	 * 交易码：007003003046 查询票据信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryInvInfo(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> retList = new ArrayList<>();
		if (paramMap.get("oldPre") != null && paramMap.get("oldBegin") != null
				&& paramMap.get("oldEnd") != null) {
			paramMap.put("oldBegin",
					CommonUtils.getString(paramMap.get("oldPre")).trim()
							+ CommonUtils.getString(paramMap.get("oldBegin"))
									.trim());
			paramMap.put("oldEnd", CommonUtils
					.getString(paramMap.get("oldPre")).trim()
					+ CommonUtils.getString(paramMap.get("oldEnd")).trim());
			paramMap.put("pkEmp", UserContext.getUser().getPkEmp());

			retList = ipSettleGzgyMapper.qryInvInfo(paramMap);
		}
		return retList;
	}

	/**
	 * 交易码：007003003047 保存票据信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> saveInvInfo(String param, IUser user) {
		List<InvVo> saveList = JsonUtil.readValue(param,
				new TypeReference<List<InvVo>>() {
				});

		Map<String, Object> paramMap = new HashMap<>();
		List<String> newInvList = new ArrayList<>();
		for (InvVo vo : saveList) {
			newInvList.add(vo.getNewCodeInv());
		}
		paramMap.put("pkList", newInvList);
		// 校验新发票号码必须未使用
		// List<String> newInvCode = ipSettleGzgyMapper.qryInvIsUse(paramMap);
		//
		// if(newInvCode!=null && newInvCode.size()>0){
		// StringBuffer errMsg = new StringBuffer("新的发票号码");
		// for(int i=0; i<newInvCode.size(); i++){
		// if(i==0)
		// errMsg.append(newInvCode.get(i));
		// else
		// errMsg.append(","+newInvCode.get(i));
		// }
		// errMsg.append("已被使用，请修改！");
		// throw new BusException(errMsg.toString());
		// }

		// 保存修改的新票据号
		List<String> pkList = new ArrayList<>();
		String pkEmp = UserContext.getUser().getPkEmp();
		for (InvVo vo : saveList) {
			if (vo.getFlagCc().equals("0")
					&& !CommonUtils.isEmptyString(vo.getNewCodeInv())) {
				pkList.add(vo.getPkInvoice());
				String upSql = "update bl_invoice set code_inv=? where code_inv=? and pk_emp_inv=? and pk_invoice = ? and flag_cc_cancel='0' and flag_cc='0'";
				DataBaseHelper.execute(upSql,
						new Object[] { vo.getNewCodeInv(), vo.getCodeInv(),
								pkEmp, vo.getPkInvoice() });
			}
		}

		// 保存完成后查询保存成功的信息并返回
		paramMap.put("pkList", pkList);
		List<Map<String, Object>> retList = ipSettleGzgyMapper
				.qryInvInfo(paramMap);

		return retList;
	}

	/**
	 * 交易号：007003003049 修改当前使用的发票号
	 * 
	 * @param param
	 * @param user
	 */
	public void updateInvCode(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		String pkEmpinv = CommonUtils.getString(paramMap.get("pkEmpinv"));
		Long newInvCode = CommonUtils.getLong(paramMap.get("newInvCode"));

		// 查询需要修改的票据信息
		BlEmpInvoice invInfo = DataBaseHelper.queryForBean(
				"select * from bl_emp_invoice where PK_EMPINV = ?",
				BlEmpInvoice.class, new Object[] { pkEmpinv });

		/** 校验新的当前号码不能小于开始号码且不能大于结束号码 */
		if (!(newInvCode >= invInfo.getBeginNo() && newInvCode <= invInfo
				.getEndNo()))
			throw new BusException("新发票号必须大于开始号码("
					+ invInfo.getBeginNo().toString() + ")并且小于结束号码("
					+ invInfo.getEndNo().toString() + ")！");

		/** 校验新的当前号码不能被使用（前缀+号码） */
		StringBuffer codeInv = new StringBuffer("");
		// 查詢票據分類
		BdInvcate invCate = DataBaseHelper.queryForBean(
				"select * from BD_INVCATE where PK_INVCATE = ?",
				BdInvcate.class, new Object[] { invInfo.getPkInvcate() });
		if (invCate.getLength() != null) {
			String curNo = this.flushLeft("0", invCate.getLength(),
					newInvCode.toString());
			if (!CommonUtils.isEmptyString(invCate.getPrefix()))
				codeInv.append(invCate.getPrefix());
			if (!CommonUtils.isEmptyString(invInfo.getInvPrefix()))
				codeInv.append(invInfo.getInvPrefix());
			codeInv.append(curNo);
		} else {
			if (!CommonUtils.isEmptyString(invCate.getPrefix()))
				codeInv.append(invCate.getPrefix());
			if (!CommonUtils.isEmptyString(invInfo.getInvPrefix()))
				codeInv.append(invInfo.getInvPrefix());
			codeInv.append(newInvCode.toString());
		}

		Integer count = DataBaseHelper.queryForScalar(
				"select count(1) from bl_invoice where code_inv=?",
				Integer.class, new Object[] { codeInv.toString() });
		if (count > 0)
			throw new BusException("新发票号已被使用！");

		/** 更新当前发票号 */
		// 计算可用张数
		Long newCntUse = invInfo.getEndNo() - newInvCode + 1;
		DataBaseHelper
				.execute(
						"update bl_emp_invoice set cnt_use=?,cur_no=? where pk_empinv=? and cur_no=?",
						new Object[] { newCntUse, newInvCode, pkEmpinv,
								invInfo.getCurNo() });

	}

	/**
	 * 左边补齐 c 要填充的字符 length 填充后字符串的总长度 content 要格式化的字符串
	 */
	private String flushLeft(String c, long length, String content) {
		String str = "";
		String cs = "";
		if (content.length() > length) {
			throw new BusException("异常：当前号码长度与指定长度不一致！");
		} else {
			for (int i = 0; i < length - content.length(); i++) {
				cs = cs + c;
			}
		}
		str = cs + content;
		return str;
	}

	/**
	 * 交易号：007003003050 查询患者最近一次结算时间
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Date qryDateStByPv(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		List<Date> dateList = ipSettleGzgyMapper.qryDateStByPv(paramMap);

		Date dateSt = null;

		if (dateList != null && dateList.size() > 0)
			dateSt = dateList.get(0);

		return dateSt;
	}

	/**
	 * 住院公医非药品结算策略
	 */
	public Map<String, Object> ipGyItemSettle(PvEncounter pv,
			Map<String, Object> paramMap) {
		paramMap.put("pkPv", pv.getPkPv());
		paramMap.put("pkHp", pv.getPkInsu());

		BigDecimal amtPi = BigDecimal.valueOf(0D);
		BigDecimal amtHp = BigDecimal.valueOf(0D);

		/** 1.1非药品策略 */
		// List<GyStItemVo> itemList =
		// ipSettleGzgyMapper.qryGyItemStList(paramMap);//查询待结算非药品金额信息
		// Map<String,Object> hpMap =
		// ipSettleGzgyMapper.qryHpInfoByPv(pv.getPkPv());//查询患者就诊医保信息
		// List<GyHpDivInfo> materList =
		// ipSettleGzgyMapper.qryMaterialsInfo(paramMap);//查询高值耗材策略信息
		// String sysFaCode = ApplicationUtils.getSysparam("BL0033",
		// true);//获取BL0033（广州医保省公医上级编码）
		//
		// if(itemList!=null && itemList.size()>0){
		// for(GyStItemVo itemVo : itemList){
		// /**1.1.1自费费用策略*/
		// if(itemVo.getRatioSelf().compareTo(BigDecimal.valueOf(1D))==0){//自负比例为1
		// amtPi = amtPi.add(itemVo.getAmt());
		// continue;
		// }else if("11".equals(itemVo.getDtItemtype())){/**1.1.2床位费*/
		// BigDecimal bedAmtPi = BigDecimal.valueOf(0D);
		// //当床位单价price>床位限价bedquota时
		// if(itemVo.getPrice().compareTo(itemVo.getBedquota())>0){
		// //自付金额=amt-(bedquota*quan)+bedquota*quan*hp.rate_ip
		// bedAmtPi =
		// itemVo.getAmt().subtract(itemVo.getBedquota().multiply(itemVo.getQuan())).add(itemVo.getBedquota().multiply(itemVo.getQuan()).multiply(itemVo.getRateIp()));
		// amtPi = amtPi.add(bedAmtPi);
		// //医保金额=amt-自负金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(bedAmtPi));
		// }else{
		// //自付金额=amt*hp.rate_ip
		// bedAmtPi = itemVo.getAmt().multiply(itemVo.getRateIp());
		// amtPi = amtPi.add(bedAmtPi);
		// //医保金额=amt-自负金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(bedAmtPi));
		// }
		// }else if(itemVo.getAmountMax()!=null &&
		// itemVo.getAmountMax().compareTo(BigDecimal.valueOf(0D))>=0){/**1.1.3特殊项目（amount_max>=0）*/
		// //当eu_calcmode=0时，rate_pi=hp.rate_ip，否则rate_pi=sp.ratio
		// BigDecimal ratePi = BigDecimal.valueOf(0D);
		// BigDecimal tsAmtPi = BigDecimal.valueOf(0D);
		// if(itemVo.getEuCalcmode().compareTo(BigDecimal.valueOf(0D))==0){
		// ratePi = itemVo.getRateIp();
		// }else{
		// ratePi = itemVo.getRatio();
		// }
		// //当单价price<=amount_max时
		// if(itemVo.getPrice().compareTo(itemVo.getAmountMax())<=0){
		// //自付金额=amt*rate_pi
		// tsAmtPi = itemVo.getAmt().multiply(ratePi);
		// amtPi = amtPi.add(tsAmtPi);
		// //医保金额=amt-自负金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(tsAmtPi));
		// }else{
		// //自付金额=amt-(amount_max*quan)+(amount_max*quan*rate_pi)
		// tsAmtPi =
		// itemVo.getAmt().subtract(itemVo.getAmountMax().multiply(itemVo.getQuan())).add(itemVo.getAmountMax().multiply(itemVo.getQuan()).multiply(ratePi));
		// amtPi = amtPi.add(tsAmtPi);
		// //医保金额=amt-自负金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(tsAmtPi));
		// }
		// }else if(itemVo.getAmountMax()==null &&
		// itemVo.getDtItemtype().startsWith("07")){/**1.1.4高值耗材（amount_max is
		// null and item.dt_itemtype like '07%'）*/
		//
		// BigDecimal gzAmtPi = BigDecimal.valueOf(0D);
		// BigDecimal ratePi = null;
		// if(materList!=null && materList.size()>0){
		// boolean flagSucc = false;//标志是否匹配到策略信息
		// for(GyHpDivInfo hvInfo : materList){
		// //当eu_calcmode=0时，rate_pi=hp.rate_ip，否则rate_pi=hv.ratio
		// if(itemVo.getEuCalcmode()==null ||
		// itemVo.getEuCalcmode().compareTo(BigDecimal.valueOf(0D))==0){
		// ratePi = itemVo.getRateIp();
		// }else{
		// ratePi = hvInfo.getRatio();
		// }
		// if(ratePi!=null){
		// //省公医
		// if(!CommonUtils.isEmptyString(sysFaCode) &&
		// sysFaCode.equals(CommonUtils.getString(hpMap.get("faCode")))){
		// //当price>=price_min and price<price_max时
		// if(itemVo.getPrice().compareTo(hvInfo.getPriceMin())>=0 &&
		// itemVo.getPrice().compareTo(hvInfo.getPriceMax())<0){
		// //自付金额=amt*ratio_init+amt*(1-ratio_init)*rate_pi
		// gzAmtPi =
		// itemVo.getAmt().multiply(hvInfo.getRatioInit()).add(itemVo.getAmt().multiply(BigDecimal.valueOf(1D).subtract(hvInfo.getRatioInit())).multiply(ratePi));
		// amtPi = amtPi.add(gzAmtPi);
		// //医保金额=amt-自付金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(gzAmtPi));
		// flagSucc = true;
		// }
		// }else{//区公医
		// //只处理item.dt_itemtype='0701'的记录
		// if("0701".equals(itemVo.getDtItemtype()) &&
		// (itemVo.getPrice().compareTo(hvInfo.getPriceMin())>=0 &&
		// itemVo.getPrice().compareTo(hvInfo.getPriceMax())<0)){
		// //自付金额=amt*ratio_init+amt*(1-ratio_init)*rate_pi
		// gzAmtPi =
		// itemVo.getAmt().multiply(hvInfo.getRatioInit()).add(itemVo.getAmt().multiply(BigDecimal.valueOf(1D).subtract(hvInfo.getRatioInit())).multiply(ratePi));
		// amtPi = amtPi.add(gzAmtPi);
		// //医保金额=amt-自付金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(gzAmtPi));
		// flagSucc = true;
		// }
		// }
		// }
		// }
		// //没有匹配到符合的策略信息
		// if(!flagSucc){
		// //医保金额=amt*hp.rate_ip
		// gzAmtPi = itemVo.getAmt().multiply(itemVo.getRateIp());
		// amtPi = amtPi.add(gzAmtPi);
		// //医保金额=amt-自付金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(gzAmtPi));
		// }
		// }else{
		// //医保金额=amt*hp.rate_ip
		// gzAmtPi = itemVo.getAmt().multiply(itemVo.getRateIp());
		// amtPi = amtPi.add(gzAmtPi);
		// //医保金额=amt-自付金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(gzAmtPi));
		// }
		// }else
		// if(itemVo.getDtItemtype().startsWith("01")){/**1.1.5诊查费（item.dt_itemtype
		// like '01%')*/
		// BigDecimal zcAmtPi = BigDecimal.valueOf(0D);
		// //当诊查费单价price>诊查费限价dtquota_ip时
		// if(itemVo.getDtquotaIp()==null ||
		// itemVo.getDtquotaIp().compareTo(BigDecimal.valueOf(0D))==0)
		// itemVo.setDtquotaIp(BigDecimal.valueOf(999999D));
		// if(itemVo.getPrice().compareTo(itemVo.getDtquotaIp())>0){
		// //自付金额=amt-(dtquota_ip*quan)+dtquota_ip*quan*hp.rate_ip
		// zcAmtPi =
		// itemVo.getAmt().subtract(itemVo.getDtquotaIp().multiply(itemVo.getQuan())).add(itemVo.getDtquotaIp().multiply(itemVo.getQuan()).multiply(itemVo.getRateIp()));
		// amtPi = amtPi.add(zcAmtPi);
		// //医保金额=amt-自付金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(zcAmtPi));
		// }else{
		// //自付金额=amt*hp.rate_ip
		// zcAmtPi = itemVo.getAmt().multiply(itemVo.getRateIp());
		// amtPi = amtPi.add(zcAmtPi);
		// //医保金额=amt-自付金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(zcAmtPi));
		// }
		// }else{/**1.1.6 其他费用*/
		// BigDecimal qtAmtPi = new BigDecimal(0);
		// //医保金额=amt*hp.rate_ip
		// qtAmtPi = itemVo.getAmt().multiply(itemVo.getRateIp());
		// amtPi = amtPi.add(qtAmtPi);
		// //医保金额=amt-自付金额
		// amtHp = amtHp.add(itemVo.getAmt().subtract(qtAmtPi));
		// }
		// }
		//
		// //给入参自负金额和医保金额赋值
		// //amountPi = MathUtils.add(amountPi, amtPi.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue());
		// }
		// Map<String,Object> amtMap = new HashMap<>();
		// amtMap.put("amtPi", amtPi);
		// amtMap.put("amtHp", amtHp);

		// 查询非药品待结算金额
		Date begin = (Date) paramMap.get("begin");
		Date end = (Date) paramMap.get("end");
		String pkSettle = (String) paramMap.get("pkSettle");
		@SuppressWarnings("unchecked")
		List<String> pkList = (List<String>) paramMap.get("pkList");
		@SuppressWarnings("unchecked")
		List<String> pkCgips = (List<String>) paramMap.get("pkList");
		Map<String, Object> itemAmtMap = ipSettleGzgyMapper.qryItemCgInfo(
				pv.getPkPv(), begin, end, pkSettle, pkList, pkCgips);
		if (itemAmtMap != null && itemAmtMap.size() > 0) {
			amtPi = BigDecimal.valueOf(CommonUtils.getDouble(itemAmtMap
					.get("amtPi") == null ? 0D : itemAmtMap.get("amtPi")));
			BigDecimal amtSt = BigDecimal.valueOf(CommonUtils
					.getDouble(itemAmtMap.get("amt") == null ? 0D : itemAmtMap
							.get("amt")));
			BigDecimal amtAdd = BigDecimal.valueOf(CommonUtils
					.getDouble(itemAmtMap.get("amountAdd") == null ? 0D
							: itemAmtMap.get("amountAdd")));
			amtPi = amtPi.add(amtAdd);
			if (amtSt != null && amtSt.compareTo(BigDecimal.valueOf(0D)) >= 0) {
				amtHp = amtSt.subtract(amtPi);
			}
		}

		Map<String, Object> amtMap = new HashMap<>();
		amtMap.put("amtPi", amtPi);
		amtMap.put("amtHp", amtHp);
		return amtMap;
	}

	/**
	 * 交易号：007003003052 修改患者结算途径
	 * 
	 * @param param
	 * @param user
	 */
	public void updatePvStway(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		// 修改患者结算途径
		String upSql = "update pv_ip set dt_stway=?,date_prest=?,pk_emp_prest=?,name_emp_prest=? where pk_pv=?";
		DataBaseHelper.execute(
				upSql,
				new Object[] { paramMap.get("euSettleWay"), new Date(),
						UserContext.getUser().getPkEmp(),
						UserContext.getUser().getUserName(),
						paramMap.get("pkPv") });

		// 中山二院--调用平安APP出院结算通知接口推送消息
		ExtSystemProcessUtils.processExtMethod("PAAPP", "noticeSettle", param);

	}

	/**
	 * 交易号：007003003053 判断当前结算未打印发票且未取消结算数量-中二线上缴费
	 */
	public Map<String, Object> getBlSettleCount(String param, IUser user) {
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		if (pkSettle == null) {
			throw new BusException("传入参数有空值");
		}
		int count = DataBaseHelper
				.queryForScalar(
						"select Count(1) cnt  from bl_settle st where st.pk_settle=? and st.flag_canc ='0' and not exists (select 1 from bl_st_inv si where st.pk_settle=si.pk_settle)",
						Integer.class, pkSettle);
		Map<String, Object> stwayMap = new HashMap<>();
		stwayMap.put("count", count);
		return stwayMap;
	}

	/**
	 * 获取患者列表-增加线上缴费部分-中二专用
	 * 
	 * @param param
	 * @param user
	 */
	public List<Map<String, Object>> QryPatiOrOnlineList(String param,
			IUser user) {

		@SuppressWarnings("unchecked")
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String euSttype = (String) paraMap.get("euSttype");
		String pkOrg = (String) paraMap.get("pkOrg");
		@SuppressWarnings("unused")
		String codeDept = (String) paraMap.get("codeDept");
		String typeDept = (String) paraMap.get("typeDept");

		StringBuffer sql = new StringBuffer(
				"select pv.pk_pv, pi.pk_pi,pv.code_pv,pi.code_ip, pi.name_pi,dept.name_dept,ip.flag_frozen,ip.dt_stway,ip.dt_sttype_ins,ip.date_notice,Case When bind.flag_valid='1' Then '已绑定' Else '未绑定' End flag_validValue,bind.flag_valid,bind.dt_platform");
		sql.append(" from pv_encounter pv");
		sql.append(" inner join pv_ip ip on pv.pk_pv=ip.pk_pv");
		sql.append(" inner join pi_master pi on pv.pk_pi=pi.pk_pi");
		sql.append(" inner join bd_ou_dept dept on pv.pk_dept=dept.pk_dept");
		sql.append(" left outer join pi_bind bind On bind.pk_pi=pi.pk_pi");
		sql.append(" where dept.pk_org = ? ");
		// 护士站增加护士站查询条件
		if ("02".equals(typeDept)) {
			sql.append(" and pv.pk_dept_ns=?");
		}

		if ("0".equals(euSttype)) {// 出院结算
			sql.append(" and pv.eu_status = 2 ");
			// 读取系统参数BL0009，如果参数值=1，列表显示就诊为“结束”状态，且已经预结算的患者列表；如果参数值=0，列表显示就诊为“结束”的患者列表；
			String type = ApplicationUtils.getSysparam("BL0009", false);
			if ("1".equals(type)) {
				sql.append(" and ip.flag_prest = 1");
			}
		} else {// 中途结算 当前患者列表为“就诊”状态；
			sql.append(" and pv.eu_status in(1,2) ");
			sql.append(" and exists (select 1 from bl_ip_dt dt where dt.pk_pv = pv.pk_pv and flag_settle = '0') ");
		}
		// 新增线上结算查询条件
		if (paraMap.get("isAtlSt") != null
				&& !CommonUtils.isEmptyString(CommonUtils.getString(paraMap
						.get("isAtlSt")))) {
			if (CommonUtils.getString(paraMap.get("isAtlSt")).equals("0")) {
				sql.append(" and (ip.dt_stway<>1  or ip.dt_stway is null or ip.dt_stway='') ");
			} else if (CommonUtils.getString(paraMap.get("isAtlSt"))
					.equals("1")) {
				sql.append(" and (ip.dt_stway<>0 or ip.dt_stway is null or ip.dt_stway='') ");
			}
		}

		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		if ("02".equals(typeDept)) {
			res = DataBaseHelper.queryForList(sql.toString(), pkOrg,
					UserContext.getUser().getPkDept());
		} else if (typeDept != null && "08".equals(typeDept.substring(0, 2))) {
			res = DataBaseHelper.queryForList(sql.toString(), pkOrg);
		}
		return res;
	}

	public Map<String, Object> qryStwayVo(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String pkPi = JsonUtil.getFieldValue(param, "pkPi");
		/* 待结算金预交金总额 */
		Map<String, Object> amtPrepMap = DataBaseHelper
				.queryForMap(
						"select sum(depo.amount) AmtPrep from bl_deposit depo Where depo.pk_pv=?  And  flag_settle='0' And eu_dptype='9' ",
						pkPv);
		BigDecimal amtPrep = amtTrans(amtPrepMap.get("amtprep"));
		/* 待结算总费用 */
		Map<String, Object> amtMap = DataBaseHelper
				.queryForMap(
						"select sum(blip.quan*blip.amount) amt from bl_ip_dt blip  where blip.pk_pv=? And  blip.flag_settle='0' ",
						pkPv);
		BigDecimal amt = amtTrans(amtMap.get("amt"));
		int count = DataBaseHelper
				.queryForScalar(
						"select Count(1) cnt from pi_bind where pk_pi=? and  dt_platform='1' and flag_valid='1' ",
						Integer.class, pkPi);
		Map<String, Object> stwayMap = new HashMap<>();
		stwayMap.put("amtPrep", amtPrep);
		stwayMap.put("amt", amt);
		stwayMap.put("count", count);
		return stwayMap;
	}

	/**
	 * 增加线上缴费部分补发票-中二专用
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	private BlInvPrint getPrintDataByPkSettle(String pkSettle) {
		BlInvPrint res = new BlInvPrint();
		if (!CommonUtils.isEmptyString(pkSettle)) {
			BlSettle st = DataBaseHelper.queryForBean(
					"SELECT * FROM bl_settle where pk_settle = ?",
					BlSettle.class, pkSettle);
			List<BlSettleDetail> blSettleDetail = DataBaseHelper.queryForList(
					" SELECT * FROM Bl_Settle_Detail WHERE pk_settle = ?",
					BlSettleDetail.class, pkSettle);
			List<BlStInv> stInvs = DataBaseHelper.queryForList(
					"SELECT * FROM bl_st_inv where pk_settle = ?",
					BlStInv.class, pkSettle);
			List<BlDeposit> blDepositList = DataBaseHelper
					.queryForList(
							"SELECT * FROM bl_deposit WHERE pk_settle = ? and (eu_dptype = '0' or eu_dptype = '1')",
							BlDeposit.class, pkSettle);
			List<BlInvoice> inv = new ArrayList<BlInvoice>();
			for (BlStInv stInv : stInvs) {
				BlInvoice invvo = DataBaseHelper
						.queryForBean(
								" SELECT * FROM bl_invoice WHERE flag_cancel= 0 and pk_invoice = ?",
								BlInvoice.class, stInv.getPkInvoice());
				if (invvo != null
						&& !CommonUtils.isEmptyString(invvo.getPkInvoice())) {
					inv.add(invvo);
					List<BlInvoiceDt> invDt = DataBaseHelper
							.queryForList(
									" SELECT * FROM bl_invoice_dt WHERE pk_invoice = ?",
									BlInvoiceDt.class, invvo.getPkInvoice());
					invvo.setInvDt(invDt);
				}
			}

			res.setBlInvoice(inv);
			res.setBlSettle(st);
			res.setBlDepositList(blDepositList);
			res.setBlSettleDetail(blSettleDetail);
		}
		return res;
	}

	/**
	 * 增加线上缴费部分补打发票-中二专用
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BlInvPrint stInvMakeUpPrint(String param, IUser user) {
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkSettle = (String) paraMap.get("pkSettle");
		List<String> codeInv = (List<String>) paraMap.get("codeInv");
		String pkEmpinv = (String) paraMap.get("pkEmpinv");
		BlInvPrint old = getPrintDataByPkSettle(pkSettle);
		Map<String, Object> rePrintMap = new HashMap<>();// 重打发票个性化服务入参
		// 调用发票查询服务，判断是否有足够的发票张数，如果不够，提示用户“发票剩余数量不足，请设置!”；
		// 作废原发票号码；
		for (BlInvoice inv : old.getBlInvoice()) {
			DataBaseHelper.execute(
					"update bl_invoice  set flag_cancel = 1,date_cancel = ?,"
							+ "  pk_emp_cancel = ?,name_emp_cancel = ?"
							+ " where code_inv = ?", new Date(), UserContext
							.getUser().getPkEmp(), UserContext.getUser()
							.getNameEmp(), inv.getCodeInv());
		}
		List<BlInvoice> newInvs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> newInvDts = new ArrayList<BlInvoiceDt>();
		List<BlStInv> stInvs = new ArrayList<BlStInv>();
		BlInvoice newVo = new BlInvoice();
		String oldPk = newVo.getPkInvoice();
		newVo.setPkEmpInv(UserContext.getUser().getPkEmp());
		newVo.setPkEmpinvoice(pkEmpinv);
		newVo.setNameEmpInv(UserContext.getUser().getNameEmp());
		if (codeInv != null) {
			newVo.setCodeInv(codeInv.get(0));
		}
		newVo.setAmountInv(old.getBlSettle().getAmountSt().doubleValue());
		newVo.setAmountPi(old.getBlSettle().getAmountPi().doubleValue());
		newVo.setDateInv(new Date());
		newVo.setPrintTimes(1);
		newVo.setFlagCancel("0");
		newVo.setFlagCc("0");
		newVo.setFlagCcCancel("0");
		ApplicationUtils.setDefaultValue(newVo, true);
		rePrintMap.put(oldPk, newVo);// 灵璧个性化重打发票服务入参
		List<InvItemVo> itemVos = ipSettleGzgyMapper
				.QryInvItemInfoByPkSettle(pkSettle);
		for (InvItemVo invItem : itemVos) {
			BlInvoiceDt invDt = new BlInvoiceDt();
			invDt.setPkInvoicedt(NHISUUID.getKeyId());
			invDt.setPkInvoice(newVo.getPkInvoice());
			invDt.setPkBill(invItem.getPkInvcateitem());
			invDt.setNameBill(invItem.getName());
			invDt.setCodeBill(invItem.getCode());
			invDt.setAmount(invItem.getAmount());
			newInvDts.add(invDt);
		}

		BlStInv stInv = new BlStInv();
		stInv.setPkInvoice(newVo.getPkInvoice());
		stInv.setPkSettle(pkSettle);
		stInv.setPkOrg(UserContext.getUser().getPkOrg());
		ApplicationUtils.setDefaultValue(stInv, true);
		stInvs.add(stInv);
		newInvs.add(newVo);
		// 写发票表bl_invoice；
		DataBaseHelper.insertBean(newVo);
		// 写发票明细表bl_invoice_dt；
		DataBaseHelper.batchUpdate(
				DataBaseHelper.getInsertSql(BlInvoiceDt.class), newInvDts);
		// 写发票结算关联表bl_st_inv；
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class),
				stInvs);

		DataBaseHelper
				.execute(
						"update BL_DEPOSIT set PK_EMP_REPT=?,NAME_EMP_REPT=?,DATE_REPT=? where pk_settle=?",
						new Object[] { UserContext.getUser().getPkEmp(),
								UserContext.getUser().getNameEmp(), new Date(),
								pkSettle });
		// 重打发票个性化服务
		InvPrintProcessUtils.invRePrint(rePrintMap);

		// 调用发票使用确认服务，完成发票更新。
		try {
			commonService.confirmUseEmpInv(pkEmpinv, new Long(newInvs.size()));
		} catch (Exception e) {
			throw new BusException("发票更新失败，请确认票据是否充足！");
		}
		return getPrintDataByPkSettle(pkSettle);
	}

	public void updateSettleInfo(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkSettle = (String) paramMap.get("pkSettle");
		String dateEnd = (String) paramMap.get("dateEnd");
		// Date dateEnd = DateUtils.strToDate(reqVo.getDateEnd(),
		// "yyyy-MM-dd HH:mm:ss");
		String end = dateEnd + " 00:00:00";
		String sql = "update bl_settle set date_end = TO_DATE(?, 'SYYYY-MM-DD HH24:MI:SS') where pk_settle = ?";
		DataBaseHelper.execute(sql, end, pkSettle);
	}

	@SuppressWarnings("unchecked")
	public BlInvPrint getInvPreSettle(String param, IUser user) {
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkSettle = (String) paraMap.get("pkSettle");
		List<String> codeInv = (List<String>) paraMap.get("codeInv");
		String pkEmpinv = (String) paraMap.get("pkEmpinv");
		double amountSt = (double) paraMap.get("amountSt");
		double amountPi = (double) paraMap.get("amountPi");
		BlInvPrint old = getPrintDataByPkSettle(pkSettle);
		Map<String, Object> rePrintMap = new HashMap<>();// 重打发票个性化服务入参
		// 调用发票查询服务，判断是否有足够的发票张数，如果不够，提示用户“发票剩余数量不足，请设置!”；
		// 作废原发票号码；
		for (BlInvoice inv : old.getBlInvoice()) {
			DataBaseHelper.execute(
					"update bl_invoice  set flag_cancel = 1,date_cancel = ?,"
							+ "  pk_emp_cancel = ?,name_emp_cancel = ?"
							+ " where code_inv = ?", new Date(), UserContext
							.getUser().getPkEmp(), UserContext.getUser()
							.getNameEmp(), inv.getCodeInv());
		}
		List<BlInvoice> newInvs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> newInvDts = new ArrayList<BlInvoiceDt>();
		List<BlStInv> stInvs = new ArrayList<BlStInv>();
		BlInvoice newVo = new BlInvoice();
		String oldPk = newVo.getPkInvoice();
		newVo.setPkEmpInv(UserContext.getUser().getPkEmp());
		newVo.setPkEmpinvoice(pkEmpinv);
		newVo.setNameEmpInv(UserContext.getUser().getNameEmp());
		if (codeInv != null) {
			newVo.setCodeInv(codeInv.get(0));
			newVo.setPkInvcate(codeInv.get(1));
		}
		if (amountSt > 0) {
			newVo.setAmountInv(amountSt);
		} else {
			newVo.setAmountInv(old.getBlSettle().getAmountSt().doubleValue());
		}
		if (amountPi > 0) {
			newVo.setAmountPi(amountPi);
		} else {
			newVo.setAmountPi(old.getBlSettle().getAmountPi().doubleValue());
		}
		newVo.setDateInv(new Date());
		newVo.setPrintTimes(1);
		newVo.setFlagCancel("0");
		newVo.setFlagCc("0");
		newVo.setFlagCcCancel("0");
		ApplicationUtils.setDefaultValue(newVo, true);
		rePrintMap.put(oldPk, newVo);// 灵璧个性化重打发票服务入参
		List<InvItemVo> itemVos = ipSettleGzgyMapper
				.QryInvItemInfoByPkSettle(pkSettle);
		for (InvItemVo invItem : itemVos) {
			BlInvoiceDt invDt = new BlInvoiceDt();
			invDt.setPkInvoicedt(NHISUUID.getKeyId());
			invDt.setPkInvoice(newVo.getPkInvoice());
			invDt.setPkBill(invItem.getPkInvcateitem());
			invDt.setNameBill(invItem.getName());
			invDt.setCodeBill(invItem.getCode());
			invDt.setAmount(invItem.getAmount());
			newInvDts.add(invDt);
		}

		BlStInv stInv = new BlStInv();
		stInv.setPkInvoice(newVo.getPkInvoice());
		stInv.setPkSettle(pkSettle);
		stInv.setPkOrg(UserContext.getUser().getPkOrg());
		ApplicationUtils.setDefaultValue(stInv, true);
		stInvs.add(stInv);
		newInvs.add(newVo);
		// 写发票表bl_invoice；
		DataBaseHelper.insertBean(newVo);
		// 写发票明细表bl_invoice_dt；
		DataBaseHelper.batchUpdate(
				DataBaseHelper.getInsertSql(BlInvoiceDt.class), newInvDts);
		// 写发票结算关联表bl_st_inv；
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class),
				stInvs);
		DataBaseHelper
				.execute(
						"update BL_SETTLE set PK_EMP_ST=?,NAME_EMP_ST=? where pk_settle=?",
						new Object[] { UserContext.getUser().getPkEmp(),
								UserContext.getUser().getNameEmp(), pkSettle });
		// 重打发票个性化服务
		InvPrintProcessUtils.invRePrint(rePrintMap);

		// 调用发票使用确认服务，完成发票更新。
		try {
			commonService.confirmUseEmpInv(pkEmpinv, new Long(newInvs.size()));
		} catch (Exception e) {
			throw new BusException("发票更新失败，请确认票据是否充足！");
		}
		return getPrintDataByPkSettle(pkSettle);
	}
}
