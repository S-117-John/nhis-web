package com.zebone.nhis.scm.ipdedrug.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.ipdedrug.dao.IpDeDrugPrintMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发退药单打印接口
 * @author yangxue
 *
 */
@Service
public class IpPdDeDrugPrintService {
	@Resource
	private IpDeDrugPrintMapper ipDeDrugPrintMapper;
    /**
     * 打印发药汇总单
     * @param param{codeDe}   
     * @param user
     * @return
     */
	public List<Map<String,Object>> printIpPdDeSummary(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null ||CommonUtils.isNull(paramMap.get("codeDe")))
			throw new BusException("未获取到需要打印的发药单号！");
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkDeptDe",((User)user).getPkDept());
		return ipDeDrugPrintMapper.qryIpDeDrugSummary(paramMap);
	}
	/**
	 * 打印发药明细单
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> printIpPdDeDetail(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null ||CommonUtils.isNull(paramMap.get("codeDe")))return null;
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkDeptDe",((User)user).getPkDept());
		return ipDeDrugPrintMapper.qryIpDeDrugDetail(paramMap);
	}
	/**
	 * 根据处方单号查询发药处方类型列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPresPdDeList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null ||CommonUtils.isNull(paramMap.get("codeDe")))
			throw new BusException("未获取到需要打印的发药单号！");
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		paramMap.put("pkDept", ((User)user).getPkDept());
		return ipDeDrugPrintMapper.queryDePresTypeList(paramMap);
	}
	/**
	 * 设置打印标志
	 * @param param
	 * @param user
	 */
	public void setPrintFlag(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null ||CommonUtils.isNull(paramMap.get("codeDe")))
			throw new BusException("未获取到需要打印的发药单号！");
		String updateSql = " update ex_pd_de set flag_prt='1' where code_de=:codeDe";
		if(paramMap.get("pkPddecate")!=null&&!"".equals(paramMap.get("pkPddecate"))){
			updateSql = updateSql + " and pk_pddecate = :pkPddecate ";
		}
		DataBaseHelper.update(updateSql, paramMap);
	}
	
	/**
	 * 008004001022
	 * 根据请领单主键更新打印状态
	 * @param param
	 * @param user
	 */
	public void updatePrintApply(String param,IUser user){
		List<String> pkPdaps=JsonUtil.readValue(param, new TypeReference<List<String>>() {});
		if(pkPdaps==null || pkPdaps.size()<=0){
			throw new BusException("未获取到需要打印的请领单号");
		}
		int count=ipDeDrugPrintMapper.checkPrintApply(pkPdaps);
		if(count>0)
		{
			throw new BusException("当前所选申请单存在作废单据，请核对后在处理！");
		}
		List<String> upSqls=new ArrayList<String>();
		for (String pkPdAp : pkPdaps) {
			StringBuffer sql=new StringBuffer("update ex_pd_apply set eu_print='2'");
			sql.append(" where pk_pdap='");
			sql.append(pkPdAp);
			sql.append("' and flag_cancel='0' and (eu_print='0' or eu_print is null)");
			upSqls.add(sql.toString());
		}
		DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
	}
	
	/**
	 * 008004001033
	 * 住院处方发药:中二需求（汇总打印）
	 * @param param{"codeDe":"发药单号","pkDeptAp":"申请科室","pkPreses":"处方集合"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPrintPresSum(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param,Map.class);
		if(paramMap==null)return null;
		List<Map<String,Object>> resData=ipDeDrugPrintMapper.qryPrintPresSum(paramMap);
		return resData;
	}
	
	/**
	 * 住院处方发药：查询发药单记录
	 * @param param{"dateBegin":"开始时间","dateEnd":"结束时间","pkDeptDe":"发药药房科室","pkDeptAp":"申请科室","codeIp":"住院号"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPresListData(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		paramMap.put("pkDeptDe", ((User)user).getPkDept());
		List<Map<String,Object>> resData=ipDeDrugPrintMapper.qryPresListData(paramMap);
		return resData;
	}
	
	/**
	 * 住院处方打印标签
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPrintLableData(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		User us=(User)user;
		paramMap.put("pkDeptDe", us.getPkDept());
		return ipDeDrugPrintMapper.qryPrintLableData(paramMap);
	}
	
	/**
	 * 008004001037
	 * 住院草药处方打印
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPrintHrebPresInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param,Map.class);
		if(paramMap==null)return null;
		List<Map<String,Object>> resData=ipDeDrugPrintMapper.qryPrintHrebPresInfo(paramMap);
		return resData;
	}
}
