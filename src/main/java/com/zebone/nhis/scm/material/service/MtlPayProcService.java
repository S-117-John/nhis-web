package com.zebone.nhis.scm.material.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.material.dao.MtlPayProcMapper;
import com.zebone.nhis.scm.material.vo.MtlPdPayVo;
import com.zebone.nhis.scm.material.vo.MtlPdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.st.vo.PdPayVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 物资-发票验收
 * @author chengjia
 *
 */
@Service
public class MtlPayProcService {
	
	@Resource
	private MtlPayProcMapper payProcMapper;
	
    /**
     * 查询待付款供应商列表008007015001
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPaySupplyerList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		
		return payProcMapper.queryPaySupplyerList(paramMap);
	}
	
	   /**
     * 查询待付款列表008007015002
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<MtlPdStDtVo> queryStPayList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		
		return payProcMapper.queryStPayList(paramMap);
	}	
	
  /**
  * 查询已付款列表008007015003
  * @param param
  * @param user
  * @return
  */
	@SuppressWarnings("unchecked")
	public List<MtlPdPayVo> queryPdPayList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		String begin = paramMap.get("beginDate").toString().substring(0, 8)+"000000";
		String end = paramMap.get("endDate").toString().substring(0, 8)+"235959";
		paramMap.put("beginDate", begin);
		paramMap.put("endDate", end);
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		
		return payProcMapper.queryPdPayList(paramMap);
	}
	
	/**
	  * 查询付款明细008007015004
	  * @param param
	  * @param user
	  * @return
	*/
	@SuppressWarnings("unchecked")
	public List<MtlPdStDtVo> queryPdPayDtList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		
		return payProcMapper.queryPdPayDtList(paramMap);
	}
	
	/**
	 * 确认付款
	 * @param param
	 * @param user
	 * @return
	 */
	public MtlPdPayVo savePdPayRec(String param,IUser user){
		MtlPdPayVo payVo = JsonUtil.readValue(param, MtlPdPayVo.class);
		if(payVo == null || payVo.getDtList()==null ||payVo.getDtList().size()==0) return null;
		PdPay pay = new PdPay();
		ApplicationUtils.setBeanComProperty(payVo, true);
		ApplicationUtils.copyProperties(pay, payVo);
		DataBaseHelper.insertBean(pay);
		String pkPay = pay.getPkPdpay();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Date ts=new Date();
		String sql;
		for(MtlPdStDtVo dtVo : payVo.getDtList()){
			//写表pd_pay
			//此处应该取打过折扣的应付款
			sql = " update pd_st_detail "
			       + " set pk_pdpay = :pkPdpay,"+
			             " flag_pay = '1',"+
			             " amount_pay = :amountPay, "+
			             " ts = :ts "
			+ " where pk_pdstdt = :pkPdstdt";
			paramMap = new HashMap<String, Object>();
			paramMap.put("pkPdpay",pkPay);
			paramMap.put("amountPay",pay.getAmountPay());
			paramMap.put("ts",ts);
			paramMap.put("pkPdstdt",dtVo.getPkPdstdt());
			
			DataBaseHelper.update(sql,paramMap);
			
			//更新采购入库与明细
			sql = "select count(1) as num "+
			      " from pd_st_detail dt"+
				  " where dt.pk_pdst = ?"+//'"+dt.getPkPdst()+"'
				  "   and dt.flag_pay = '0'";
			int num = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{dtVo.getPkPdst()});
	        if(num==0){
	        	sql=" update pd_st"+
	        		"    set flag_pay='1',"+
	        		"        ts=:ts"+
	        		"  where pk_pdst=:pkPdst"+
	        	    "    and flag_pay='0'";
	        	paramMap = new HashMap<String, Object>();
				paramMap.put("ts",ts);
				paramMap.put("pkPdst",dtVo.getPkPdst());
				
				DataBaseHelper.update(sql,paramMap);
	        } 
		}
		payVo.setPkPdpay(pkPay);
      return payVo;
	}
}
