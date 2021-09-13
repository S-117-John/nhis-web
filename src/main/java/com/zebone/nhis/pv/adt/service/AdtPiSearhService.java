package com.zebone.nhis.pv.adt.service;

import com.zebone.nhis.pv.adt.dao.AdtPiSearhMapper;
import com.zebone.nhis.pv.adt.vo.PatientPvVo;
import com.zebone.nhis.pv.pub.support.PVSortByOrdMapUtil;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 患者综合查询
 * @author leiminjian
 * @date 2019-09-23
 *
 */
@Service
public class AdtPiSearhService {

	@Resource
	private AdtPiSearhMapper adtPiSearhMapper;
	
	/**
	 * 患者就诊信息查询 	003004003001
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PatientPvVo> searchPv(String param, IUser user){
		User u = UserContext.getUser();
		Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
//		if(paramap.get("pkPi")==null)
//			throw new BusException("请传入患者主键！");
		return adtPiSearhMapper.searchPv(paramap);
	}
	
	/**
	 * 患者处方信息查询   003004003002
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchPress(String param, IUser user){
		User u = UserContext.getUser();
		Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
		if(paramap.get("pkPv")==null)
			throw new BusException("请传入患者就诊主键！");
		
		return adtPiSearhMapper.searchPress(paramap);
	}
	
	/**
	 * 患者诊断信息查询 003004003003
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchDiag(String param, IUser user){
		User u = UserContext.getUser();
		Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
		if(paramap.get("pkPv")==null)
			throw new BusException("请传入患者就诊主键！");
		List<Map<String,Object>> qryRtn=new ArrayList<Map<String,Object>>();
		qryRtn.addAll(adtPiSearhMapper.queryPvDiag(paramap));
		//qryRtn.addAll(adtPiSearhMapper.queryCnDiag(paramap));
		return qryRtn;
	}
	

    /**
     * 查询住院患者医嘱信息
     * @param param
     * @param user
     * @return
     * @throws ParseException
     */
	public List<Map<String, Object>> queryPatiOrdList(String param, IUser user) throws Exception{
   	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
   	 Map<String,Object> newMap = new HashMap<String,Object>();
   	 List<String> pvlist = (List)paramMap.get("pkPvs");
   	 if(pvlist == null || pvlist.size()<=0)
   		 throw new BusException("未获取到患者就诊信息！");
   	 newMap.put("pkPvs", pvlist);
   	 //String pk_dept_cur = ((User)user).getPkDept();
   	 User u = UserContext.getUser();
   	 //newMap.put("pkDeptNs", pk_dept_cur);
   	 newMap.put("pkOrd", u.getPkOrg()); 
   	 List<Map<String, Object>> ordlist = new ArrayList<Map<String, Object>>();
		ordlist = adtPiSearhMapper.queryPatiOrdList(newMap);
   	 if(ordlist!=null&&ordlist.size()>0){
   		new PVSortByOrdMapUtil().ordGroup(ordlist);
   	 }
	 return  ordlist;
		 
   }
	
}
