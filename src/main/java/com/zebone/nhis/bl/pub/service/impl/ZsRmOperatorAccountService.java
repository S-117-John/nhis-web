package com.zebone.nhis.bl.pub.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.OpblccMapper;
import com.zebone.nhis.bl.pub.service.OperatorAccountService;
import com.zebone.nhis.bl.pub.vo.BlCcDt;
import com.zebone.nhis.bl.pub.vo.OpBlCcVo;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.common.support.UserContext;

@Service("ZsRmOperatorAccount")
public class ZsRmOperatorAccountService implements OperatorAccountService {
	@Autowired
	private OpblccMapper opblccMapper;

	@Override
	public OpBlCcVo getOpOperAccInfo(OpBlCcVo param) {
		Map<String, Object> paramMap = new HashMap<>(16);
		// 扩展参数
		Map<String, Object> extParam = new HashMap<>(16);
		if (CommonUtils.isEmptyString(param.getBlCc().getPkCc())) {
			paramMap.put("dateEnd", DateUtils.formatDate(param.getBlCc().getDateEnd(), "yyyyMMddHHmmss"));
			paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
			paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
			paramMap.put("pkCc", "");
			paramMap.put("euCctype", param.getBlCc().getEuCctype());

		} else {
			paramMap.put("pkCc", param.getBlCc().getPkCc());
		}
		/** 查询现金舍入金额 */
		Double amtRound = opblccMapper.qryOpAmtAround(paramMap);
		if (amtRound != null) {
			extParam.put("amtRound", amtRound);
		}

		/** 查询付款方式信息 */
		List<BlCcPay> payList = opblccMapper.qryPayModeList();

		if (payList != null && payList.size() > 0) {
			if (param.getBlCcPayList() != null && param.getBlCcPayList().size() > 0) {
				for (BlCcPay payMode : payList) {
					for (BlCcPay ccPay : param.getBlCcPayList()) {
						if (payMode.getDtPaymode().equals(ccPay.getDtPaymode())) {
							payMode.setAmt(ccPay.getAmt());
							payMode.setAmtBack(ccPay.getAmtBack());
							payMode.setCntTrade(ccPay.getCntTrade());
							payMode.setCntTradeBack(ccPay.getCntTradeBack());
							break;
						}
					}
				}
			}
			param.setBlCcPayListSd(payList);
		}

		// 医保付款信息
		List<Map<String, Object>> insuPayList = opblccMapper.qryOpInsuPayInfoZsrm(paramMap);

		if (insuPayList != null && insuPayList.size() > 0) {
			for (Map<String, Object> insuPayMap : insuPayList) {
				if (insuPayMap != null && insuPayMap.size() > 0) {
					switch (CommonUtils.getString(insuPayMap.get("code"))) {
					case "01":
						// 全国医保统筹支付
						BlCcPay insuPaytc = new BlCcPay();
						insuPaytc.setNamePaymode("全国医保统筹");
						insuPaytc.setAmt(CommonUtils.getDouble(insuPayMap.get("amt")));
						insuPaytc.setAmtBack(CommonUtils.getDouble(insuPayMap.get("amtBack")));
						param.getBlCcPayListSd().add(insuPaytc);
						extParam.put("tczf", insuPaytc.getAmtBack().toString());
						break;
					case "02":
						// 省工伤统筹支付
						BlCcPay insuPaytcsgs = new BlCcPay();
						insuPaytcsgs.setNamePaymode("省工伤统筹");
						insuPaytcsgs.setAmt(CommonUtils.getDouble(insuPayMap.get("amt")));
						insuPaytcsgs.setAmtBack(CommonUtils.getDouble(insuPayMap.get("amtBack")));
						param.getBlCcPayListSd().add(insuPaytcsgs);
						extParam.put("tczf", insuPaytcsgs.getAmtBack().toString());
						break;
					default:
						break;
					}
				}
			}
		}
		// 结算数
		List<Map<String, Object>> stCount = opblccMapper.qryOpStZsrm(paramMap);
		if (stCount != null && stCount.size() > 0) {

			/*BlCcPay insuPaytc = new BlCcPay();
			insuPaytc.setDtPaymode("*");
			insuPaytc.setNamePaymode("结算数");
			insuPaytc.setAmt(new Double(stCount.size()));
			param.getBlCcPayListSd().add(insuPaytc);*/
			extParam.put("stCount",stCount.size());
		}

		for (BlCcPay pay : param.getBlCcPayList()) {
			// 上缴现金
			if ("1".equals(pay.getDtPaymode())) {
				extParam.put("cashAmt", MathUtils.add(pay.getAmt() == null ? 0D : pay.getAmt(),
						pay.getAmtBack() == null ? 0D : pay.getAmtBack()));
			}
		}

		// param.setExtListParam(extListParam);
		param.setExtParam(extParam);

		return param;
	}

	@Override
	public BlCcDt getIpOperAccInfo(BlCcDt param) {
		return param;
	}
}
