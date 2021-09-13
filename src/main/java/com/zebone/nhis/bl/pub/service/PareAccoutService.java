package com.zebone.nhis.bl.pub.service;

import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.acc.PiAccShare;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 判断是否有授权账户及账户额度操作
 * @author Administrator
 * 
 */
@Service
public class PareAccoutService {
	
	@Autowired
	private CgQryMaintainService cgQryMaintainService;

	/**
	 * 查询（患者账户或者授权账户）账户可用余额
	 * @param pkPi
	 * @param pkOrg
	 */
	public Map<String, BigDecimal> getPatiAccountAvailableBalance(String pkPi) {

		/**
		 * 根据设计：一个患者只能有一个账户，如果存在患者授权的账户，子账户只能用父账户的钱，即使自己的账户有钱，也只能用父账户的钱，
		 * 不够的部分拿现金支付
		 */
		// 构造返回值Map<String,Double>: 放两个数据：一个放当前账户余额，另一个放账户可用余额，考虑信用额度。
		Map<String, BigDecimal> accountMap = new HashMap<>();		

		// 查询患者存在的账户共享关系 --增加 and del_flag='0'查询表中存在的授权关系
		PiAccShare piAccShare = cgQryMaintainService.qryParentPiAccByPkPi(pkPi);
		
		BigDecimal accountLimit = BigDecimal.ZERO;
		//如果账户被冻结，账户金额按0处理 2017-11-16
		// 存在父账户
		if (piAccShare != null) {
			// 查询父账户
			PiAcc piAccParent = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)", PiAcc.class, piAccShare.getPkPiacc());
			if(PiConstant.ACC_EU_STATUS_2.equals(piAccParent.getEuStatus())){ //账户被冻结
				accountMap.put("acc", BigDecimal.ZERO);
				accountMap.put("creditAcc", BigDecimal.ZERO);
				accountMap.put("accLimit", BigDecimal.ZERO);
			}else{
				BigDecimal amtAccParent = piAccParent.getAmtAcc();
				accountMap.put("acc", amtAccParent);
				BigDecimal creditAccParent = piAccParent.getCreditAcc();
				accountMap.put("creditAcc", creditAccParent);
				if (amtAccParent.compareTo(BigDecimal.ZERO)>=0) {
					accountLimit = creditAccParent.add(amtAccParent);
				} else {
					accountLimit = creditAccParent.subtract(amtAccParent.abs());
				}
				if(accountLimit.compareTo(BigDecimal.ZERO)<0) {
					accountLimit = BigDecimal.ZERO;
				}
				accountMap.put("accLimit", accountLimit);
				
			}	
			
		} else {
			// 查询患者的当前的账户
			PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)", PiAcc.class, pkPi);
			if (piAcc == null){
				return accountMap;
			}
			if(PiConstant.ACC_EU_STATUS_2.equals(piAcc.getEuStatus())){ //账户被冻结
				accountMap.put("acc", BigDecimal.ZERO);
				accountMap.put("creditAcc", BigDecimal.ZERO);
				accountMap.put("accLimit", BigDecimal.ZERO);
			}else{
				BigDecimal amtAcc = piAcc.getAmtAcc();
				accountMap.put("acc", amtAcc);
				BigDecimal creditAcc = piAcc.getCreditAcc();
				accountMap.put("creditAcc", creditAcc);
				if (amtAcc.compareTo(BigDecimal.ZERO) >= 0) {
					accountLimit = creditAcc.add(amtAcc);
				} else {
					accountLimit = creditAcc.subtract(amtAcc.abs());
				}
				if(accountLimit.compareTo(BigDecimal.ZERO)<0) {
					accountLimit = BigDecimal.ZERO;
				}
				accountMap.put("accLimit", accountLimit);
			}
		}
		return accountMap;
	}
	
	/**
	 * 查询（患者账户或者授权账户）账户余额
	 * Lb自助机webService调用查询
	 * @param pkPi
	 * @param pkOrg
	 */
	public Map<String, BigDecimal> getPatiAccountAvailableBalance(String param, IUser user){
		String pkpi = JsonUtil.getFieldValue(param, "pkPi");
		return getPatiAccountAvailableBalance(pkpi);
		
	}
	/*
	 * 查询（患者账户或者授权账户）账户可用余额
	 * @param pkPv
	 * @param pkOrg
	 */
	public PiAcc getPatiAccountAvailableBalanceByPv(String pkPv) {

		/**
		 * 根据设计：一个患者只能有一个账户，如果存在患者授权的账户，子账户只能用父账户的钱，即使自己的账户有钱，也只能用父账户的钱，
		 * 不够的部分拿现金支付
		 */
        Map<String,Object> piMap = DataBaseHelper.queryForMap("select pk_pi from pv_encounter where pk_pv = ? ", new Object[]{pkPv});
        if(piMap==null||piMap.get("pkPi")==null)
        	return null;
		PiAccShare piAccShare = cgQryMaintainService.qryParentPiAccByPkPi(piMap.get("pkPi").toString());
		// 存在父账户
		if (piAccShare != null) {
			// 查询父账户
			PiAcc piAccParent = DataBaseHelper.queryForBean("select * from pi_acc where pk_piacc=? and (del_flag='0' or del_flag is null)", PiAcc.class, piAccShare.getPkPiacc());
			return piAccParent;
		} else {
			// 查询患者的当前的账户
			PiAcc piAcc = DataBaseHelper.queryForBean("select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)", PiAcc.class, piMap.get("pkPi").toString());
			if (piAcc!=null){
				//查询押金不可用押金
				String sql=" select nvl(sum(DEPOSIT) ,0) as deposit from PI_CARD where FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2') and PK_PI= ?";
				Map<String,Object > map=DataBaseHelper.queryForMap(sql,new Object[]{piMap.get("pkPi").toString()});
				BigDecimal nAmout=BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("deposit"))));
				piAcc.setAmtAcc(piAcc.getAmtAcc().subtract(nAmout));
			}

			return piAcc;
		}
	}
	/**
	 * 根据pkpi来获取患者账户（有授权用户的取授权账户，没有授权账户的取患者账户，没有患者账户取null）
	 * 设计假设：取账户是用来支付，考虑账户冻结情况。
	 * @param pkPi
	 * @return
	 */
	public PiAcc queryParentOrPiAccByPkPi(String pkPi)
	{
		PiAcc piAcc = null;

		// 查询患者存在的账户共享关系 --增加 and del_flag='0'查询表中存在的授权关系
		PiAccShare piAccShare = cgQryMaintainService.qryParentPiAccByPkPi(pkPi);
		
		// 存在授权账户，用授权账户 
		if (piAccShare != null) {
			String preSql = "select * from pi_acc where pk_piacc=?  and nvl(del_flag,'0')='0'";
			piAcc = DataBaseHelper.queryForBean(preSql, PiAcc.class, piAccShare.getPkPiacc());
			// eu_code = '2' 当前账户被冻结
			if (EnumerateParameter.TWO.equals(piAcc.getEuStatus())){
				throw new BusException("父账户被冻结，不能使用账户支付，请使用其他付款方式。");
			}
		}else{
			// 查询患者自己账户信息
			String sql = "select * from pi_acc where pk_pi=?  and nvl(del_flag,'0')='0'";
			piAcc = DataBaseHelper.queryForBean(sql, PiAcc.class, pkPi);
			// 不存在患者账户
			if(piAcc == null){
				return piAcc;
			}
			// eu_code = '2' 当前账户被冻结
			if(EnumerateParameter.TWO.equals(piAcc.getEuStatus())){
				throw new BusException("当前账户被冻结，不能使用账户支付，请使用其他付款方式。");
			}
		}
		return piAcc;
	}
}
