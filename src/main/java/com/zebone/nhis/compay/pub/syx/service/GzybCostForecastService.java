package com.zebone.nhis.compay.pub.syx.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.pub.syx.dao.QryCostForecastMapper;
import com.zebone.nhis.compay.pub.syx.vo.*;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GzybCostForecastService {
	@Autowired
	private QryCostForecastMapper qryCostMapper;
	@Autowired
	private PvInfoPubService pvInfoPubService;
	/**
	 * 通过就诊主键pkpv查询患者相关费用数据以进行费用预测处理
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public CostForecastVo qryCostForecastVo(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> paraMap = JsonUtil.readValue(param, Map.class);
		User u = (User) user;
		Double amtsum = 0.0;// 总费用
		Double amtNosettle = 0.0;// 未结费用
		Double amtPi = 0.0;// 内部医保患者自付
		Double amtPrep = 0.0;// 预交金
		Double amtHp = 0.0;// 医保预测
		boolean isArrearage = false;// 是否欠费
		String pkhp = "";// 医保计划主键
		Double qfx = 0.0;// 起付线
		String mode = "";// 计费方式
		Double amount = 0.0; // 记账金额
		Double quotaamt = 0.0;// 纳入定额
		Double limitamt = 0.0;// 超限额费用
		/* 0甲类1乙类2自费 */
		Double amountsumpi = 0.0;// 全费
		Double amountpi = 0.0;// 自费
		Double amountClass = 0.0;// 部分自费(乙类)
		Double amountjia = 0.0;// 甲类
		Double rateip = 0.0;// 住院自付比例
		Double amountPaid = 0.0;// 自付金额
		Double amtAcc = 0.0;// 可用余额
		Double bedfee = 0.0;// 总床位费
		String dtexthp = "";
		Double ypzje = 0.0; // 比例为5%的药品总金额
		Double zlfje = 0.0; // 比例为10%和15%的治疗费
		Double jcfje = 0.0; // 比例为15%的检查费
		Double clfoneje = 0.0; // 比例为10%的材料费
		Double clftwoje = 0.0; // 比例为20%的材料费
		Double alljcfje = 0.0; // 所有比例检查费
		Double alljyfje = 0.0;// 所有比例的检验费
		Double zfjcjyf = 0.0; // 自费的（检查费和检验费）合计数
		Double otherje = 0.0;// 其他费合计
		Double yjj_fac = new Double(1);// 预交金系数
		Double kfxe = new Double(0);// 控费限额
		Double ztfee = 0.0;// 在途费用金额
		Double amtPrepBalance = 0.0;// 预交金余额
		Double deptlimitamt = 0.0;// 科室限额
		Double surpluslimitamt = 0.0;// 剩余限额
		Double arrearageAmt = 0.0;// 限额控制的金额
		//预测医保支付 --> 医保预测表(bl_settle_budget)字段：基金支付金额+个人账户支付金额
		Double amtHpPay = 0.0;
		//预测可用余额 --> 总费用-预交金-预测医保支付 -- (作废)--> 预交金-总费用+预测医保支付
		Double amtHpUsable = 0.0;
		
		CostForecastVo castVo = new CostForecastVo();
		String pkpv = (String) paraMap.get("pkPv");
		if (CommonUtils.isEmptyString(pkpv)){
			throw new BusException("入参患者住院登记主键为空！");
		}
		CostForecastAmtVo amtVo = qryCostMapper.qryAmtVo(pkpv);// 查询各项费用金额
		if (amtVo != null) {
			amtsum = processNull(amtVo.getAmt());// 总费用
			amtNosettle = processNull(amtVo.getAmtNosettle());// 未结费用
			amtPi = processNull(amtVo.getAmtPi());// 内部医保患者自付
		}
		Double amtIpAcc = 0.0;// 担保金额
		Map<String, Object> amtIpAccMap = DataBaseHelper
				.queryForMap(
						"select sum(AMT_CREDIT) amt from PV_IP_ACC where FLAG_CANC='0' and pk_pv=?",
						pkpv);// 查询担保金
		if (amtIpAccMap != null) {
			if (amtIpAccMap.get("amt") != null && amtIpAccMap.get("amt") != "") {
				amtIpAcc = processNull(Double.parseDouble(amtIpAccMap
						.get("amt").toString()));// 担保金额
			}
		}
		Map<String, Object> amtPrepmap = DataBaseHelper
				.queryForMap(
						"select sum(amount) amt from bl_deposit where eu_dptype='9' and flag_settle='0' and pk_pv=?",
						pkpv);// 查询预交金金额
		if (amtPrepmap != null) {
			if (amtPrepmap.get("amt") != null && amtPrepmap.get("amt") != "") {
				amtPrep = processNull(Double.parseDouble(amtPrepmap.get("amt")
						.toString()));// 预交金金额
			}
		}
		// amtPrep=amtPrep+amtIpAcc;
		// 1.通过pkPv查询医保计划主键
		String stSql = "select pk_hp from PV_INSURANCE  where pk_pv=? and FLAG_MAJ='1' and (del_flag='0' or del_flag is null) ";
		Map<String, Object> pkHpMap = DataBaseHelper.queryForMap(stSql, pkpv);
		if (pkHpMap != null && pkHpMap.get("pkHp") != null) {
			pkhp = pkHpMap.get("pkHp").toString();// 医保计划主键
		}
		// 住院自付比例
		Map<String, Object> rateIpMap = DataBaseHelper
				.queryForMap(
						"select rate_ip,dt_exthp from BD_HP where  PK_HP=? and (del_flag='0' or del_flag is null) ",
						pkhp);
		if (rateIpMap != null) {
			if (rateIpMap.get("rateIp") != null
					&& rateIpMap.get("rateIp") != "") {
				rateip = processNull(Double.parseDouble(rateIpMap.get("rateIp")
						.toString()));
			}
			if (rateIpMap.get("dtExthp") != null
					&& rateIpMap.get("dtExthp") != "") {
				dtexthp = rateIpMap.get("dtExthp").toString();
			}
		}
		// if (dtexthp != null && dtexthp != "") {
		List<CostHpDictAttrVo> hpDictAttrList = qryCostMapper.queryHpDictAttr(
				u.getPkOrg(), pkhp);// 查询医保计划拓展属性
		if (hpDictAttrList != null && hpDictAttrList.size() > 0) {
			for (CostHpDictAttrVo map : hpDictAttrList) {
				if (map.getCodeattr() != null) {
					if ("0309".equals(map.getCodeattr())) {
						qfx = processNull(Double.parseDouble(map.getValattr()
								.toString()));// 起付线
					}
				}
				if (map.getCodeattr() != null) {
					if ("0310".equals(map.getCodeattr())) {
						mode = map.getValattr();// 计费方式
					}
				}
			}
		}
		List<CostForecastItemVo> itemlist = qryCostMapper.qryCostItemVo(pkpv);// 取医保目录及项目分类
		if (itemlist != null && itemlist.size() > 0) {
			for (CostForecastItemVo itemvo : itemlist) {
				if (itemvo.getEustaple() != null
						&& !"005".equals(itemvo.getRatCode())) {
					if ("2".equals(itemvo.getEustaple())) {
						amountpi += itemvo.getAmount();// 自费
					} else if ("1".equals(itemvo.getEustaple())) {
						amountClass += processNull(Double.parseDouble(itemvo
								.getAmount().toString()))
								* processNull(Double.parseDouble(itemvo
										.getRatio().toString()));// 部分自费(乙类)
					} else {
						amountjia += itemvo.getAmount();// 甲类
					}
				}
				if ("005".equals(itemvo.getRatCode())) {
					if (itemvo.getAmount() != null) {
						if (itemvo.getAmount() > 37
								&& itemvo.getAmount() != 280
								&& itemvo.getAmount() != 70) {
							bedfee += (itemvo.getAmount() - 37);// 床位费累加
						}
					}
				}
				if ("002".equals(itemvo.getRatCode())) {
					if (itemvo.getRatio() != null) {
						alljcfje += itemvo.getAmount();// 检查费累加
					}
					if ("2".equals(itemvo.getEustaple())) {
						zfjcjyf += itemvo.getAmount();// 自费的（检查费和检验费）合计数
					}
				}
				if (itemvo.getRatCode() != null
						&& "1".equals(itemvo.getEustaple())) {
					if ("001".equals(itemvo.getRatCode())) {
						if (itemvo.getRatio() != null) {
							if (processNull(Double.parseDouble(itemvo
									.getRatio().toString())) == 0.05) {
								ypzje += itemvo.getAmount()
										* Double.parseDouble(itemvo.getRatio());// 药品费累加
							} else {
								otherje += itemvo.getAmount()
										* processNull(Double.parseDouble(itemvo
												.getRatio()));// 除5%比例外的药品费累加
							}
						}
					} else if ("002".equals(itemvo.getRatCode())) {
						if (itemvo.getRatio() != null) {
							if (processNull(Double.parseDouble(itemvo
									.getRatio().toString())) == 0.15) {
								jcfje += itemvo.getAmount()
										* processNull(Double.parseDouble(itemvo
												.getRatio()));// 15%比例检查费累加
							} else {
								otherje += itemvo.getAmount();// 除15%比例外的检查费累加
							}
						}

					} else if ("003".equals(itemvo.getRatCode())) {
						if (itemvo.getRatio() != null) {
							if (processNull(Double.parseDouble(itemvo
									.getRatio().toString())) == 0.15
									|| processNull(Double.parseDouble(itemvo
											.getRatio().toString())) == 0.10) {
								ypzje += itemvo.getAmount()
										* processNull(Double.parseDouble(itemvo
												.getRatio()));// 治疗费累加
							} else {
								otherje += itemvo.getAmount();// 除10%、15%比例外的治疗费累加
							}
						}
					} else if ("004".equals(itemvo.getRatCode())) {
						if (itemvo.getRatio() != null) {
							if (processNull(Double.parseDouble(itemvo
									.getRatio().toString())) == 0.10) {
								clfoneje += itemvo.getAmount()
										* processNull(Double.parseDouble(itemvo
												.getRatio()));// 比例为10%的材料费累加
							} else if (processNull(Double.parseDouble(itemvo
									.getRatio().toString())) == 0.2) {
								clftwoje += itemvo.getAmount()
										* processNull(Double.parseDouble(itemvo
												.getRatio()));// 比例为20%的材料费累加
							} else {
								otherje += itemvo.getAmount();// 除10%、20%比例外的材料费累加
							}
						}
					} else {
						otherje += itemvo.getAmount();// 其他分类费累加
					}
				}
				amountsumpi += itemvo.getAmount();// 全费
			}
		}

		// 计费方式mode:1职工医保2居民医保
		if (mode != null) {
			if ("1".equals(mode)) {
				// 自费=（单日床位费-37）*天数（当日床位费小于37时为0）+自费费用的合计数（根据目录类别）
				amountpi = bedfee + amountpi;
				quotaamt = amtNosettle - amountpi - amountClass;// 纳入定额=总费用-自费-部分自费
				amount = (quotaamt - qfx) * (1 - rateip);// 记账金额=（纳入定额-起付线）*对应比例
				amountPaid = amtNosettle - amount;// 自付金额=总费用-记账金额
			} else if ("2".equals(mode)) {
				// 自费=（单日床位费-37）*天数（当日床位费小于37时为0）+自费费用的合计数
				amountpi = bedfee + amountpi;
				// 部分自费=比例为5%的（西药费+中成药费+中草药）合计数*3+比例为（10%和15%的治疗费）合计数*2+比例为15%的检查费（MR费、B超费、CT费、X光费等）合计数*2+比例为10%材料费*3+比例为20%的材料费*2.5+其他
				amountClass = (ypzje * 3) + (zlfje * 2) + (jcfje * 2)
						+ (clfoneje * 3) + (clftwoje * 2.5) + otherje;
				// 超限额费用=所有比例检查费+所有比例的检验费-自费的（检查费和检验费）合计数-比例为15%的检查费（MR费、B超费、CT费、X光费等）合计数*2-1500
				limitamt = alljcfje + alljyfje - zfjcjyf - jcfje * 2 - 1500;
				if (limitamt > 0) {
					quotaamt = amtNosettle - amountpi - amountClass - limitamt;// 纳入定额=总费用-自费-部分自费-超限额费用
				} else {
					quotaamt = amtNosettle - amountpi - amountClass;
				}
				amount = (quotaamt - qfx) * (1 - rateip);// 记账金额=（纳入定额-500）*对应比例（80%或70%）（未成年及学生为80%，其他居民为70%）
				amountPaid = amtNosettle - amount;// 自付金额=总费用-记账金额
			} else if ("3".equals(mode) || "4".equals(mode)) {
				amountPaid = bedfee + amountsumpi;// 自付金额=（单日床位费-37）*天数+自费费用合计(不论甲乙自)
			} else if ("5".equals(mode) || "6".equals(mode)) {
				amountPaid = amtNosettle * rateip;// 自付金额
			} else {
				amountPaid = amountsumpi;// 自付金额
			}
			// amtAcc = (amtPrep + amtIpAcc) - amountPaid;// 可用余额
			amtHp = amtNosettle - amountPaid;// 医保预测额
		} else {
			amountPaid = amtPi;// 自付金额
			// amtAcc = (amtPrep + amtIpAcc) - amountPaid;// 可用余额
			amtHp = 0.00;// 医保预测额
		}
		// } else {
		// amountPaid = amtNosettle;// 自付金额
		// amtAcc = (amtPrep + amtIpAcc) - amtsum;// 可用余额
		// }

		List<Map<String, Object>> kfresult = qryCostMapper.getYjFactor(pkpv);// 查询预交金系数

		if (kfresult != null && kfresult.size() > 0) {
			yjj_fac = processNull(Double.parseDouble(kfresult.get(0)
					.get("factorPrep").toString()));
			kfxe = processNull(Double.parseDouble(kfresult.get(0)
					.get("amtCred").toString()));
		}
		yjj_fac = yjj_fac == null ? new Double(1) : yjj_fac;
		kfxe = kfxe == null ? new Double(0) : kfxe;

		// 在途费用
		List<CostZtFeeVo> ztfeelist = qryCostMapper.qryZtFeeVo(pkpv);// 取在途费用
		if (ztfeelist != null && ztfeelist.size() > 0) {
			for (CostZtFeeVo ztitemvo : ztfeelist) {
				ztfee += ztitemvo.getAmount();
			}
		}
		String deptParam = ApplicationUtils.getSysparam("BL0039", false);
		if (deptParam != null && deptParam != "") {
			deptlimitamt = processNull(Double.parseDouble(deptParam));// 科室限额=读医保科室限额参数BL0039
			surpluslimitamt = deptlimitamt - quotaamt;// 剩余限额=科室限额-纳入定额
		}
		amtPrepBalance = amtPrep*yjj_fac - amountPaid;// 预交金余额=未结预交金-估计自付
		amtAcc = amtPrepBalance + amtIpAcc + kfxe;
		arrearageAmt = amtAcc;// + kfxe;// 限额控制的金额= 可用余额+控费限额
		if (pvInfoPubService.isLimiteFee(pkpv)){
			if (arrearageAmt.doubleValue() > 0) {
				isArrearage = false;
			} else {
				isArrearage = true;
			}
		}else {  isArrearage=false;}

		// 查询预测医保支付
		Map<String, Object> amtHpPayMap = DataBaseHelper
				.queryForMap(
						"select sum(amount_insu) + sum(amount_pidiv)  amt_hp_pay  from bl_settle_budget where del_flag='0' and pk_pv=?",
						pkpv);
		if (amtHpPayMap != null) {
			if (amtIpAccMap.get("amtHpPay") != null && amtHpPayMap.get("amtHpPay") != "") {
				// 预测医保支付
				amtHpPay = processNull(Double.parseDouble(amtIpAccMap.get("amtHpPay").toString()));
			}
		}

		
		//预测可用余额
		//amtHpUsable = amtsum - amtPrep - amtHpPay;
		amtHpUsable = amtPrep -amtsum + amtHpPay;
		
		/* 返回前台数据 */
		if (amtHp < 0) {
			amtHp = 0.0;
			amountPaid = amtNosettle;
		}
		castVo.setAmtPrep(amtPrep);// 未结预交金
		castVo.setAmtSum(amtsum);// 总费用
		castVo.setAmtNosettle(amtNosettle);// 未结费用
		castVo.setAmtPi(amtPi);// 内部医保患者自付
		castVo.setAmountPaid(amountPaid);// 估计自付
		castVo.setAmtAcc(amtAcc);// 可用余额
		castVo.setAmtHp(amtHp);// 医保预测
		castVo.setAmtPrepBalance(amtPrepBalance);// 预交金余额
		castVo.setArrearage(isArrearage);// 是否欠费
		castVo.setAmtIpAcc(amtIpAcc);// 绿色通道额度
		castVo.setQuotaAmt(quotaamt);// 纳入定额
		castVo.setArrearageAmt(arrearageAmt);// 欠费限额
		castVo.setZtfee(ztfee);// 在途费用金额
		castVo.setDeptlimitamt(deptlimitamt); // 科室限额
		castVo.setSurpluslimitamt(surpluslimitamt);// 剩余限额
		castVo.setZtfeelist(ztfeelist);// 在途费用明细
		//预测医保支付 --> 医保预测表(bl_settle_budget)字段：基金支付金额+个人账户支付金额
		castVo.setAmtHpPay(amtHpPay);
		//预测可用余额 --> 总费用-预交金-预测医保支付
		castVo.setAmtHpUsable(amtHpUsable);

		return castVo;
	}

	@SuppressWarnings("unused")
	private Double processNull(Double args) {
		if (args == null) {
			args = new Double(0);
		}
		return args;
	}
}
