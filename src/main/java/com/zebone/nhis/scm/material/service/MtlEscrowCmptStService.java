package com.zebone.nhis.scm.material.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.scm.material.dao.MtlEscrowCmptStMapper;
import com.zebone.nhis.scm.material.dao.MtlReceiptAcceptMapper;
import com.zebone.nhis.scm.material.vo.MtlPdStDtQryVo;
import com.zebone.nhis.scm.material.vo.MtlPdStDtVo;
import com.zebone.nhis.scm.material.vo.MtlPdStVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 物资-代管消耗结算
 * @author chengjia
 *
 */
@Service
public class MtlEscrowCmptStService {
	@Resource
	private MtlEscrowCmptStMapper escrowCmptStMMapper;
	
	/**
	 * 查询待结算记录008007013001
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MtlPdStVo> queryPdStTdList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		String begin = paramMap.get("beginDate").toString().substring(0, 8)+"000000";
		String end = paramMap.get("endDate").toString().substring(0, 8)+"235959";
		paramMap.put("beginDate", begin);
		paramMap.put("endDate", end);

		List<MtlPdStVo> rtnist = escrowCmptStMMapper.queryPdStTdList(paramMap);
		
		return rtnist;
	}

	/**
	 * 查询已结算记录信息008007013002
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MtlPdStVo> queryPdStDnList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		String begin = paramMap.get("beginDate").toString().substring(0, 8)+"000000";
		String end = paramMap.get("endDate").toString().substring(0, 8)+"235959";
		paramMap.put("beginDate", begin);
		paramMap.put("endDate", end);
		

		List<MtlPdStVo> rtnist = escrowCmptStMMapper.queryPdStDnList(paramMap);
		
		return rtnist;
	}
	
	/**
	 * 查询待结算明细记录008007013003
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MtlPdStDtVo> queryPdStTdDetail(String param,IUser user){
		List<String> pkPdSts = JsonUtil.readValue(param, ArrayList.class);
		
		if(pkPdSts == null || pkPdSts.size() <=0) return null;
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPdSts", pkPdSts);

		List<MtlPdStDtVo> rtnist = escrowCmptStMMapper.queryPdStTdDetail(paramMap);
		
		return rtnist;
	}
	
	/**
	 * 查询已结算明细记录008007013004
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MtlPdStDtVo> queryPdStDnDetail(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);

		List<MtlPdStDtVo> rtnist = escrowCmptStMMapper.queryPdStDnDetail(paramMap);
		
		return rtnist;
	}	
	
	/**
	 * 结算处理008007013005
	 * @param param
	 * @param user
	 */
	public void savePdStDtSettle(String param,IUser user){
		List<MtlPdStDtVo> list = JsonUtil.readValue(param,new TypeReference<List<MtlPdStDtVo>>(){});
		if(list == null || list.size() ==0 ) return;
		
		User u = (User)user;
		String pkEmp = u.getPkEmp();
		String nameEmp = u.getNameEmp();
		String sql="";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Date ts=new Date();
		for(MtlPdStDtVo item : list){
			sql="update pd_st_detail "+
				      " set pk_supplyer = :pkSupplyer,"+
				          " receipt_no = :receiptNo,"+
				          " disc = :disc,"+
				          " amount_pay = :amountPay,"+
				          " flag_chk_rpt='1',"+
				          " date_chk_rpt = :dateChkRpt,"+
				          " pk_emp_chk_rpt = :pkEmpChkRpt,"+
				          " name_emp_chk_rpt = :nameEmpChkRpt,"+
				          " note = :note,"+
				          " ts=:ts"+
				" where pk_pdstdt=:pkPdstdt";
			paramMap = new HashMap<String, Object>();
			paramMap.put("pkPdstdt",item.getPkPdstdt());
			paramMap.put("pkSupplyer",item.getPkSupplyer());
			paramMap.put("receiptNo",item.getReceiptNo());
			paramMap.put("disc",item.getDisc());
			paramMap.put("amountPay",item.getAmountPay());	
			paramMap.put("pkEmpChkRpt",item.getAmountPay());
			paramMap.put("dateChkRpt",item.getDateChkRpt());
			paramMap.put("pkEmpChkRpt",pkEmp);
			paramMap.put("nameEmpChkRpt",nameEmp);
			paramMap.put("note",item.getNote());
			paramMap.put("ts",ts);
			
			DataBaseHelper.update(sql,paramMap);
		}

	}
	


}
