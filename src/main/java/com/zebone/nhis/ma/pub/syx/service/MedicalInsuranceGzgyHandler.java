package com.zebone.nhis.ma.pub.syx.service;

import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyPv;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.compay.pub.syx.service.GzybCostForecastService;
import com.zebone.nhis.compay.pub.syx.vo.CostForecastVo;
import com.zebone.nhis.pv.pub.vo.AdtRegParam;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class MedicalInsuranceGzgyHandler {

	@Autowired
	MedicalInsuranceGzgyService gzgyService;
	@Autowired
	private GzybCostForecastService gzybCostForecastService;

	public Object invokeMethod(String methodName, Object... args) {
		Object result = null;
		switch (methodName) {
		case "AddInsGzgyPv":
			this.AddInsGzgyPv(args);
			break;
		case "qryCostForecast":
			this.qryCostForecast(args);
			break;
		}
		return result;
	}

	/**
	 * 2018-09-21 中二需求 ： 广州公医或单位医保患者，同时更新ins_gzgy_pv表
	 * @param args
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void AddInsGzgyPv(Object[] args) {
		if (args != null) {
			PvEncounter pvEncounter = (PvEncounter) args[2];
			PiMaster piMaster = (PiMaster) args[1];
			AdtRegParam regParam = (AdtRegParam) args[0];
			String paramGZGY = ApplicationUtils.getSysparam("BL0023", false);// 获取是否启用广州公用医保
			if ("1".equals(paramGZGY)) {
				Map<String, Object> valAttrMap = DataBaseHelper.queryForMap("select attr.val_attr,eu_hptype from bd_hp hp inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict" + " inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp and tmp.dt_dicttype='03'" + " where hp.pk_hp=? and tmp.code_attr='0301'", new Object[] { pvEncounter.getPkInsu() });

				if ((valAttrMap != null && ("1".equals(valAttrMap.get("valAttr")))) || (valAttrMap != null && EnumerateParameter.FOUR.equals(valAttrMap.get("euHptype")))) { // 主医保是单位医保或是广州公医
					InsGzgyPv insGzgyPv = new InsGzgyPv();
					insGzgyPv.setPkPv(pvEncounter.getPkPv());
					insGzgyPv.setPkPi(piMaster.getPkPi());
					insGzgyPv.setPkHp(pvEncounter.getPkInsu());
					insGzgyPv.setEuPvtype("3");
					insGzgyPv.setDelFlag("0");
					insGzgyPv.setMcno(CommonUtils.isEmptyString(regParam.getMcno()) ? "" : regParam.getMcno().trim());
					insGzgyPv.setDictSpecunit(regParam.getDictSpecunit());
					DataBaseHelper.insertBean(insGzgyPv);
				}
			}
		}
	}
	
	//医保预测
	private void qryCostForecast(Object[] args) {
		if (args != null) {
			String param =(String)args[0];
			IUser user =(IUser)args[1];
			Map<String,Object> rtnM =(Map<String,Object>)args[2];
			
			CostForecastVo costForecastVo=gzybCostForecastService.qryCostForecastVo(param, user);
			
			rtnM.put("Amount", costForecastVo.getAmtSum());//总费用
			rtnM.put("AmtPrep", costForecastVo.getAmtPrep());//未结预交金
			rtnM.put("AmtHp", costForecastVo.getAmtHp());//医保预测费用
			rtnM.put("AmtAcc", costForecastVo.getAmtAcc());//可用余额
			rtnM.put("IsArrearage", "0".equals(ApplicationUtils.getSysparam("BL0004", false)) ? false : costForecastVo.isArrearage());//是否欠费
			rtnM.put("AmtNosettle", costForecastVo.getAmtNosettle());//未结费用
			rtnM.put("Ztfee", costForecastVo.getZtfee());//在途费用
			rtnM.put("AmountPaid", costForecastVo.getAmountPaid());//估计自付
			rtnM.put("AmtPrepBalance", costForecastVo.getAmtPrepBalance());//预交金余额
			rtnM.put("ArrearageAmt", costForecastVo.getArrearageAmt());//欠费限额
			rtnM.put("AmtIpAcc", costForecastVo.getAmtIpAcc());//绿色通道额度
			rtnM.put("Deptlimitamt", costForecastVo.getDeptlimitamt());//科室限额
			rtnM.put("Quotaamt", costForecastVo.getQuotaAmt());//纳入定额
			rtnM.put("Surpluslimitamt", costForecastVo.getSurpluslimitamt());//科室剩余限额
			rtnM.put("ZtFeeList", costForecastVo.getZtfeelist());//在途费用明细列表
		}
	}

}
