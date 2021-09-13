package com.zebone.nhis.pro.zsba.nm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStDetailsMapper;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStMapper;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiSt;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiStDetails;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 对账
 * @author lipz
 *
 */
@Service
public class NmBillService {
	
	@Autowired private NmCiStMapper stMapper;
	@Autowired private NmCiStDetailsMapper detailsMapper;
	
	/**
	 * 对账列表数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> billList(String param , IUser user){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkOrg = params.get("pkOrg");
		if(StringUtils.isEmpty(pkOrg)){
			pkOrg = UserContext.getUser().getPkOrg();
		}
		String pkDept = params.get("pkDept");
		String inputDept = params.get("inputDept");
		String codePv = params.get("codePv");
		String pvType = params.get("pvType");
		String dateBegin = params.get("dateBegin");
		String dateEnd = params.get("dateEnd");
		String isPay = params.get("isPay");
		
		if(StringUtils.isEmpty(dateBegin) || StringUtils.isEmpty(dateEnd)){
			throw new BusException("请选择开始、结束日期！");
		}
		
		List<Map<String, Object>> settData = getSettData(pkOrg, pkDept, inputDept, codePv, pvType, isPay, dateBegin, dateEnd);
		for (Map<String, Object> map : settData) {
			String pkCiSt = map.get("pkCiSt").toString();
			map.put("annalList", getAnnalData(pkCiSt)); 
			
			Object payMethod = map.get("payMethod");
			if(payMethod!=null){
				map.put("payList", getPayData(pkCiSt, payMethod.toString()));
			}else{
				map.put("payList", new ArrayList<>());
			}
			
		}
		resultMap.put("settList", settData);
		
		return resultMap;
	}
	
	
	private List<Map<String, Object>> getSettData(String pkOrg,String pkDept,String inputDept,String codePv,String pvType,String isPay,String dateBegin,String dateEnd){
		StringBuffer sql = new StringBuffer(" select d.NAME_DEPT,s.pk_ci_st, s.pv_type, s.pk_pv, s.code_pv, s.times, pv.BED_NO, s.name_pi, s.create_time as sett_date, s.amount, s.refund_amount, s.is_pay, s.pay_method, s.pk_pay, s.pk_refund ");
		sql.append(" from nm_ci_st s ");
		sql.append(" LEFT JOIN BD_OU_DEPT d on d.PK_DEPT = s.pk_dept ");
		sql.append(" LEFT JOIN PV_ENCOUNTER pv on pv.PK_PV = s.pk_pv ");
		
		StringBuffer whereSql = new StringBuffer(" select pk_ci_st from nm_ci_st_details where is_sett='1' ");
		if(StringUtils.isNotEmpty(pkOrg)){
			whereSql.append(" and  pk_org='"+pkOrg+"' ");
		}
		if(StringUtils.isNotEmpty(pkDept)){
			whereSql.append(" and  pk_dept='"+pkDept+"' ");
		}
		if(StringUtils.isNotEmpty(inputDept)){
			whereSql.append(" and  input_dept='"+inputDept+"' ");
		}
		if(StringUtils.isNotEmpty(codePv)){
			whereSql.append(" and  code_pv='"+codePv+"' ");
		}
		if(StringUtils.isNotEmpty(isPay)){
			whereSql.append(" and  is_pay='"+isPay+"' ");
		}
		if(StringUtils.isNotEmpty(pvType)){
			whereSql.append(" and  pv_type='"+pvType+"' ");
		}
		if(StringUtils.isNotEmpty(dateBegin)){
			whereSql.append(" and  date_annal>='"+dateBegin+"' ");
		}
		if(StringUtils.isNotEmpty(dateEnd)){
			whereSql.append(" and  date_annal<='"+dateEnd+"' ");
		}
		
		sql.append(" where s.pk_ci_st in (" +whereSql.toString()+ ") ");
		sql.append(" order by sett_date desc ");
		
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}

	private List<Map<String, Object>> getAnnalData(String pkCiSt){
		StringBuffer sql = new StringBuffer(" SELECT pk_ci_std, name_pi, date_annal, i.name_item, num_ord, ci_price, total, is_pay ");
		sql.append(" FROM nm_ci_st_details d, nm_charge_item i  ");
		sql.append(" WHERE d.pk_ci=i.pk_ci AND pk_ci_st='"+pkCiSt+"' ");
		sql.append(" ORDER BY date_annal asc ");
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}
	
	private List<Map<String, Object>> getPayData(String pkCiSt, String payMethod){
		StringBuffer sql = new StringBuffer();
		if(PayConfig.PAY_METHOD_ALI.equals(payMethod)){
			sql.append(" select PK_PAY_ALI_RECORD as pk_pay, OUT_TRADE_NO, TOTAL_AMOUNT,  ");
			sql.append(" case when ORDER_TYPE='pay' then (case when REFUND_TRADE_NO is null then (case when TRADE_STATE='SUCCESS' then '1' else '0' end) else '2' end ) ");//支付类型，有无退款
			sql.append(" else (case when TRADE_STATE='SUCCESS' then '2' else '3' end ) end TRADE_STATE ");// 退款类型，成功与否
			sql.append(" ,PRODUCT_ID FROM PAY_ALI_RECORD ");
		}else{
			sql.append(" select PK_PAY_WECHAT_RECORD as pk_pay, OUT_TRADE_NO, TOTAL_AMOUNT, ");
			sql.append(" case when ORDER_TYPE='pay' then (case when REFUND_TRADE_NO is null then (case when TRADE_STATE='SUCCESS' then '1' else '0' end) else '2' end ) ");//支付类型，有无退款
			sql.append(" else (case when TRADE_STATE='SUCCESS' then '2' else '3' end ) end TRADE_STATE ");// 退款类型，成功与否
			sql.append(" , PRODUCT_ID FROM PAY_WECHAT_RECORD ");
		}
		sql.append(" WHERE PRODUCT_ID='"+pkCiSt+"' AND SYSTEM_MODULE='"+PayConfig.system_module_nm+"' order by create_time asc");
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}

	/**
	 * 根据结算记录的付款标志 更新 计费记录的付款标记
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void updateAnnalIsPay(String param , IUser user){
		
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkCiSt = params.get("pkCiSt");
		String pkCiStd = params.get("pkCiStd");

		if(StringUtils.isEmpty(pkCiSt)){
			throw new BusException("必填参数pkCiSt不能为空！");
		}
		if(StringUtils.isEmpty(pkCiStd)){
			throw new BusException("必填参数pkCiStd不能为空！");
		}
		
		NmCiSt st = stMapper.getById(pkCiSt);
		NmCiStDetails std = detailsMapper.getById(pkCiStd);
		if(st==null){
			throw new BusException("未找到结算记录！");
		}
		if(std==null){
			throw new BusException("未找到计费记录！");
		}
		
		std.setIsPay(st.getIsPay());
		std.setModityTime(new Date());
		detailsMapper.updateCiStd(std);
		
	}
	
	/**
	 * 根据付款记录的付款标志 更新 结算和计费记录的付款标记
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void updateSettIsPay(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkCiSt = params.get("pkCiSt");
		String pkPay = params.get("pkPay");
		if(StringUtils.isEmpty(pkCiSt)){
			throw new BusException("必填参数pkCiSt不能为空！");
		}
		if(StringUtils.isEmpty(pkPay)){
			throw new BusException("必填参数pkPay不能为空！");
		}
		
		NmCiSt st = stMapper.getById(pkCiSt);
		if(st==null){
			throw new BusException("未找到结算记录！");
		}
		
		StringBuffer sql = new StringBuffer();
		if(PayConfig.PAY_METHOD_ALI.equals(st.getPayMethod())){
			sql.append(" select TRADE_STATE, PRODUCT_ID, ORDER_TYPE FROM PAY_ALI_RECORD WHERE PK_PAY_ALI_RECORD=? ");
		}else{
			sql.append(" select TRADE_STATE, PRODUCT_ID, ORDER_TYPE FROM PAY_WECHAT_RECORD WHERE PK_PAY_WECHAT_RECORD=? ");
		}
		Map<String, Object> data = DataBaseHelper.queryForMap(sql.toString(), new Object[]{pkPay});
		if(data.isEmpty()){
			throw new BusException("未找到付款记录！");
		}
		String productId = data.get("productId").toString();
		String tradeState = data.get("tradeState").toString();
		String orderType = data.get("orderType").toString();
		if(!productId.equals(st.getPkCiSt())){
			throw new BusException("付款记录 关联 结算记录的主键不一致！");
		}
		String isPay = "";
		if(orderType.equals("pay")){
			if(tradeState.equals("INIT")){
				String delSql = "";
				if(PayConfig.PAY_METHOD_ALI.equals(st.getPayMethod())){
					delSql = " DELETE FROM PAY_ALI_RECORD WHERE PK_PAY_ALI_RECORD='"+pkPay+"' ";
				}else{
					delSql = " DELETE FROM PAY_WECHAT_RECORD WHERE PK_PAY_WECHAT_RECORD='"+pkPay+"' ";
				}
				DataBaseHelper.update(delSql);
			}else{
				isPay = tradeState.equals("SUCCESS")?"1":"0";
			}
		}else{
			isPay = tradeState.equals("SUCCESS")?"2":"0";
		}
		if(StringUtils.isNotEmpty(isPay)){
			//更新结算记录付款标记
			st.setIsPay(isPay);
			st.setModityTime(new Date());
			stMapper.updateCiSt(st);
			//更新计费记录付款标记
			String annalSql = " select * from nm_ci_st_details where pk_ci_st=? ";
			List<NmCiStDetails> stdList = DataBaseHelper.queryForList(annalSql, NmCiStDetails.class, new Object[]{pkCiSt});
			for (NmCiStDetails std : stdList) {
				std.setIsPay(isPay);
				std.setModityTime(new Date());
				detailsMapper.updateCiStd(std);
			}
		}
	}
	

}
