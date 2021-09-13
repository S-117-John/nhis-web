package com.zebone.nhis.scm.st.service;

import java.util.*;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.nhis.scm.st.dao.InvoiceCheckMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 发票验收
 * @author yangxue
 *
 */
@Service
public class InvoiceCheckService {
	@Resource
	private InvoiceCheckMapper invCheckMapper;
	
	/**
	 * 查询采购入库单列表
	 * @param param{pkOrg,codeSt,pkSupplyer,dateBegin,dateEnd,pkEmpOp}
	 * @param user
	 * @return
	 */
	public List<PdStVo> queryPdPuSt(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null){
			map = new HashMap<String,Object>();
		}
		map.put("pkOrg", ((User)user).getPkOrg());
		if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		return invCheckMapper.queryPdPuStList(map);
	}
	/**
	 * 查询采购入库单明细
	 * @param param{pkPdSts}
	 * @param user
	 * @return
	 */
	public List<PdStDtVo> queryPdPuStDt(String param,IUser user){
		List<String> pkPdSts = JsonUtil.readValue(param, ArrayList.class);
		if(pkPdSts == null || pkPdSts.size() <=0) throw new BusException("未获取到单据主键！");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPdSts", pkPdSts);
		return invCheckMapper.queryPdPuStDtList(map);
	}
	
	/***
	* 查询已验收列表
	* @param map{receiptNo,pkSupplyer,dateBegin,dateEnd,pkEmpChkRpt}
	* @return
	*/
	public List<Map<String,Object>> queryPdInvoiceList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) throw new BusException("未获取到查询条件！");
		if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		return invCheckMapper.queryPdInvoiceList(map);
	}
	/***
	* 查询已验收明细列表
	* @param map{receiptNo,dateChk,pkSupplyer}
	* @return
	*/
	public List<PdStDtVo> queryPdInvoiceDtList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) throw new BusException("未获取到查询条件！");
		return invCheckMapper.queryPdInvoiceDtList(map);
	}
	/**
	 * 审核后更新入库信息
	 * @param param
	 * @param user
	 */
	public void updatePdStDt(String param,IUser user){
		List<PdStDtVo> list = JsonUtil.readValue(param,new TypeReference<List<PdStDtVo>>(){});
		if(list == null || list.size() <=0 ) throw new BusException("未获取到审核明细内容！");
		List<String> updateList = new ArrayList<String>();
		User u = (User)user;
		String pk_emp = u.getPkEmp();
		String emp_name = u.getNameEmp();
		Set<String> pkPdstSet = new HashSet<>();
		for(PdStDtVo dt : list){
			pkPdstSet.add(dt.getPkPdst());
			String note = dt.getNote()==null?"":dt.getNote();
			StringBuilder updateSql = new StringBuilder(" update pd_st_detail  set receipt_no = '")
			.append(dt.getReceiptNo())
			.append("',disc = ")
			.append(dt.getDisc())
			.append(",amount_pay = ")
            .append(dt.getAmountPay())
            .append(",date_chk_rpt = to_date('")
            .append(DateUtils.getDefaultDateFormat().format(new Date()))
            .append("','YYYYMMDDHH24MISS')")
            .append(",pk_emp_chk_rpt='")
            .append(pk_emp)
            .append("',name_emp_chk_rpt='")
            .append(emp_name)
            .append("',flag_chk_rpt='1',note='")
            .append(note)
            .append("',ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') ")
			.append(" where pk_pdstdt='")
            .append(dt.getPkPdstdt())
            .append("'");
			updateList.add(updateSql.toString());
		}
		if(pkPdstSet.size()>0){
			String sql = "update PD_ST set PK_SUPPLYER =? where PK_PDST in (" + CommonUtils.convertSetToSqlInPart(pkPdstSet, "PK_PDST") + ") ";
			DataBaseHelper.execute(sql, list.get(0).getPkSupplyer());
		}
		if(updateList!=null&&updateList.size()>0){
			DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));
		}
	}

	/**
	 * 修改发票号
	 * @param param
	 * @param user
	 */
	public void updatePdReceiptNo(String param,IUser user){
		List<PdStDtVo> list = JsonUtil.readValue(param,new TypeReference<List<PdStDtVo>>(){});
		if(list == null || list.size() <=0 ) throw new BusException("未获取到审核明细内容！");
		List<String> updateList = new ArrayList<String>();
		User u = (User)user;
		String pk_emp = u.getPkEmp();
		String emp_name = u.getNameEmp();
		Set<String> pkPdstSet = new HashSet<>();
		for(PdStDtVo dt : list){
			pkPdstSet.add(dt.getPkPdst());
			String note = dt.getNote()==null?"":dt.getNote();
			StringBuilder updateSql = new StringBuilder(" update pd_st_detail  set receipt_no = '")
					.append(dt.getReceiptNo())
					.append("',modifier='")
					.append(pk_emp)
					.append("',modity_time = to_date('")
					.append(DateUtils.getDefaultDateFormat().format(new Date()))
					.append("','YYYYMMDDHH24MISS')")
					.append(",note='")
					.append(note)
					.append("',ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') ")
					.append(" where pk_pdstdt='")
					.append(dt.getPkPdstdt())
					.append("'");
			updateList.add(updateSql.toString());
		}
		if(updateList!=null&&updateList.size()>0){
			DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));
		}
	}

}
