package com.zebone.nhis.ex.pub.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.support.PatiFeeHandler;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询患者余额
 * @author yangxue
 *
 */
@Service
public class QueryRemainFeeService {
	/**
	 * 缓存每次请领实例化一个服务，
	 */
	Map<String,Double> remain = new HashMap<String,Double>();
	@Resource
	private PatiFeeHandler patiFeeHandler;

	@Autowired
	private PvInfoPubService pvInfoPubService;
	/**
	 * 是否控费
	 * @return
	 */
	public static boolean isControlFee(){
//		String paramChkFee = ApplicationUtils.getSysparam("BL0005", false,"请维护系统参数【BL0005-住院患者是否控制欠费】;"
//				+ "\n【机构】级别，取值范围【0/1】，默认值【0】;"
//				+ "\n备注：除住院医生外的其他业务节点是否控制欠费（0不控制1控制）！");// 除医生站外是否控制欠费
		
		//改为科室级别参数校验是否控费
		String paramChkFee = null;
		if(UserContext.getUser()!=null && !CommonUtils.isEmptyString(UserContext.getUser().getPkDept())){
			paramChkFee = ApplicationUtils.getDeptSysparam("BL0005", UserContext.getUser().getPkDept());
		}else{
			paramChkFee = "0";//默认不控费
		}
		
		if("1".equals(paramChkFee)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取缓存数据
	 * @return
	 */
	public Map<String, Double> getRemain() {
		return remain;
	}
	
	/**
	 * 获取是否有余额(预交金*系数+控费限额+担保金-已发生费用-在途费用)
	 * @param pk_pv
	 * @param pk_hp
	 * @param curAmt--本次计费金额
	 * @return
	 * @throws BusinessException
	 */
	public boolean isArrearage(String pk_pv,String pk_hp,BigDecimal curAmt)throws BusException{

		if (pvInfoPubService.isLimiteFee(pk_pv)){

			Double remain = MathUtils.add(getRemain(pk_pv,pk_hp), getAccFee(pk_pv));
			if(curAmt!=null){
				remain = MathUtils.sub(remain,curAmt.doubleValue());
			}
			if(remain.doubleValue() > 0)
				return true;
			else{
				return false;
			}

		}else {
			return true;
		}



	}
	
	/**
	 * 获取当前的余额
	 * @param pk_pv
	 * @param pk_hp
	 * @return
	 * @throws BusinessException
	 */
	public Double getRemain(String pk_pv,String pk_hp)throws BusException{
		if(pk_pv == null ||"".equals(pk_pv))
			 throw new BusException("患者就诊主键为空，无法获取患者余额！");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPv", pk_pv);
			Double fee = patiFeeHandler.getRemainFee(paramMap);
		return fee;
	}
	/**
	 * 获取担保金
	 * @param pk_pv
	 * @return
	 */
	public Double getAccFee(String pk_pv){
		if(pk_pv == null ||"".equals(pk_pv))
			 throw new BusException("患者就诊主键为空，无法获取患者余额！");
		String sql  = "select sum(amt_credit) as num from pv_ip_acc where pk_pv = ? and flag_canc = '0'";
		Map<String,Object> map = DataBaseHelper.queryForMap(sql, new Object[]{pk_pv});
		if(map!=null){
			return CommonUtils.getDouble(map.get("num"));
		}
		return 0.00;
	}
	
	
}
