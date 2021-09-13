package com.zebone.nhis.scm.opds.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.scm.opds.dao.ScmOpPresCheck2Mapper;
import com.zebone.nhis.scm.pub.support.OpDrugPubUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊处方签到---中二需求，目前单独配合中二的发配药使用
 * @author jd_em
 *
 */
@Service
public class ScmOpPresCheck2Service {
	
	@Resource
	private ScmOpPresCheck2Mapper scmOpPresCheck2Mapper;
	
	/**
	 * 008003006007
	 * 查询处方信息
	 * @param param {"pkDeptEx":"执行机构","pkPi":"患者主键"} 
	 * @param user
	 * return 患者处方信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPres(String param, IUser user) {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param,Map.class);
		if(paraMap==null) return null;
		//2.处方信息				
		return scmOpPresCheck2Mapper.qryPres(paraMap);
	}
	
	/**
	 * 008003006006
	 * 查询未签到患者信息
	 * @param param {"pkDeptEx":"执行机构"}
	 * @param user
	 * return {"codePi": "患者编码", "pkPi":"患者主键","namePi":"患者姓名" }
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPrePatis(String param, IUser user) {
		//String pkDeptEx = ((User)user).getPkDept();
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param,Map.class);
		if(paraMap==null) return null;
		//2.患者信息				
		return scmOpPresCheck2Mapper.qryPrePatis(paraMap);
	}
	
	/**
	 * 008003006008
	 * 查询处方明细信息
	 * @param param {"pkPresocc":"处方主键"}
	 * @param user
	 * return 处方详情
	 */
	public List<Map<String,Object>> qryPresDt(String param, IUser user) {
		List<String> paramList=JsonUtil.readValue(param, new TypeReference<ArrayList<String>>() {});
		
		if(paramList==null|| paramList.size()<=0) return null;
		//处方详情
		return scmOpPresCheck2Mapper.qryPresDt(paramList);
	}
	
	/**
	 * 签到
	 * @param param {"pkDept":"发药药房","pkPresocc":"处方主键","pkDeptPres":"开立科室"}
	 * @param user
	 * 
	 */
	public void check(String param, IUser user) {
		//1.参数获取
		List<Map<String, Object>> list=JsonUtil.readValue(param,new TypeReference<List<Map<String, Object>>>(){});
		if (list==null||list.size()<1)return;
		String pkDept=list.get(0).get("pkDept").toString();
		String pkDeptPres=list.get(0).get("pkDeptPres").toString();
		String pkDeptArea= MapUtils.getString(list.get(0),"pkDeptAreaapp");
		Map<String, Object> map =OpDrugPubUtils.getWin(pkDept,pkDeptPres,pkDeptArea);
		for (Map<String, Object> paraMap : list) {
			String type = ApplicationUtils.getDeptSysparam("EX0001", UserContext.getUser().getPkDept());//门诊发药模式:1，配药发药模式；2，直接发药模式；   
			//Map<String, Object> map = this.getWin(paraMap);//根据参数EX0030获取窗口分配方式
			paraMap.put("dateReg", new Date());//签到时间
			if(EnumerateParameter.ONE.equals(type)){//配药
				//设置发药/配药窗口
				paraMap.put("winnoConf", map.get("winnoConf"));
				paraMap.put("winnoPrep", map.get("winnoPrep"));
				scmOpPresCheck2Mapper.updateByDosage(paraMap);
			}else{//发药:需要指定发药窗口
				paraMap.put("winnoConf", map.get("winnoConf"));//设置发药窗口
				scmOpPresCheck2Mapper.updateByDispensing(paraMap);
			}
		}
		List<String> pkPresocces=new ArrayList<String>();
		for (Map<String, Object> pres : list) {
			pkPresocces.add(pres.get("pkPresocc").toString());
		}
		Map<String,Object> paramMap=new HashMap<String,Object>(); 
		String isOpenHrSysParam=ApplicationUtils.getDeptSysparam("EX0055", pkDept);
		paramMap.put("IsHrOpen", "1");
		paramMap.put("pkPresocces", pkPresocces);
		if("1".equals(isOpenHrSysParam)){
			ExtSystemProcessUtils.processExtMethod("PackMachineOp", "upLoadPresInfo",pkPresocces);
		}
		
		
	}
}
