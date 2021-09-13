package com.zebone.nhis.base.bd.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.base.bd.dao.WFMapper;
import com.zebone.nhis.base.bd.vo.BdWfOrdArguParam;
import com.zebone.nhis.base.bd.vo.BdWfcateParam;
import com.zebone.nhis.common.module.base.bd.wf.BdWf;
import com.zebone.nhis.common.module.base.bd.wf.BdWfOrdArgu;
import com.zebone.nhis.common.module.base.bd.wf.BdWfOrdArguDept;
import com.zebone.nhis.common.module.base.bd.wf.BdWfcate;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class WfService {
	
	@Autowired
	private WFMapper wfMapper;
	
	
	/**
	 * 医嘱流向模式分类及明细的保存和更新
	 * @param param
	 * @param user
	 */
	@Transactional
	public BdWfcate saveBdWfcate(String param , IUser user){
		
		BdWfcateParam bdWfcateParam = JsonUtil.readValue(param,BdWfcateParam.class);
		BdWfcate bdWfcate = bdWfcateParam.getBdWfcate();
		List<BdWf> wfList = bdWfcateParam.getBdWf();
		if(wfList == null){
			wfList = Collections.EMPTY_LIST;
		}
		//检查分类重复
		Map<String,String> params = new HashMap<String,String>();
		params.put("pkWfcate", bdWfcate.getPkWfcate());
		params.put("pkOrg", bdWfcate.getPkOrg());
		int countCode = 0;
		int countName = 0;
		//检查code
		params.put("code", bdWfcate.getCode());
		countCode = wfMapper.BdWfcateCheckExist(params);
		if(countCode > 0) throw new BusException("流向分类编码重复！");
		//检查name
		params.remove("code");
		params.put("name",bdWfcate.getName());
		countName = wfMapper.BdWfcateCheckExist(params);
		if(countName > 0) throw new BusException("流向分类名称重复！");
		
		//检查明细重复
		Map<String,String> dtCodeMap = new HashMap<String,String>();
		Map<String,String> dtNameMap = new HashMap<String,String>();
		for (BdWf bdWf : wfList) {
			dtCodeMap.put(bdWf.getCode(), "code");
			dtNameMap.put(bdWf.getName(), "name");
		}
		if(dtCodeMap.size() < wfList.size()) throw new BusException("流向编码重复！");
		if(dtNameMap.size() < wfList.size()) throw new BusException("流向名称重复！");
		
		//保存 更新
		String pkWfcate = bdWfcate.getPkWfcate();
		if(pkWfcate == null){
			DataBaseHelper.insertBean(bdWfcate);
			pkWfcate = bdWfcate.getPkWfcate();
		}else{
			DataBaseHelper.updateBeanByPk(bdWfcate,false);
		}
		DataBaseHelper.execute("delete from BD_WF where PK_WFCATE = ?", new Object[]{pkWfcate});
		for (BdWf bdWf : wfList) {
			bdWf.setPkWfcate(pkWfcate);
			DataBaseHelper.insertBean(bdWf);
		}
		return bdWfcate;
	}
	
	/**
	 * 医嘱流向模式分类的查询
	 * @param param
	 * @param user
	 */
	public List<BdWfcate> searchBdWfcate(String param , IUser user){
		
		Map<String,String> jo = JsonUtil.readValue(param, HashMap.class);
		String wfcateCode = jo.get("wfcateCode");
		String wfcateName = jo.get("wfcateName");
		String wfCode = jo.get("wfCode");
		String wfName = jo.get("wfName");
		String pkOrg = ((User)user).getPkOrg();
		
		List<String> pkWfCates = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pkOrg", pkOrg);
		if(StringUtils.isNotBlank(wfCode) || StringUtils.isNotBlank(wfName)){
			params.put("code", wfCode);
			params.put("name", wfName);
			pkWfCates = wfMapper.BdWfSelectPkWf(params);
			if(pkWfCates.size() == 0) 
				return null;
		}
		
		params.put("pkWfCates", pkWfCates);
		params.put("code", wfcateCode);
		params.put("name", wfcateName);
		return wfMapper.BdWfCateSelectAll(params);
		
	}
	
	
	/**
	 * 医嘱流向模式分类删除
	 * @param param
	 * @param user
	 */
	@Transactional
	public void deleteBdWfcate(String param , IUser user){
		String pkWfcate = JSON.parseObject(param).getString("pkWfcate");
		DataBaseHelper.execute("delete from BD_WFCATE where PK_WFCATE = ?", new Object[]{pkWfcate});
		DataBaseHelper.execute("delete from BD_WF where PK_WFCATE = ?", new Object[]{pkWfcate});
	}
	
	
	/**
	 * 医嘱流向模式的保存和更新
	 * @param param
	 * @param user
	 */
	@Transactional
	public BdWf saveBdWf(String param , IUser user){
		
		BdWf bdWf = JsonUtil.readValue(param,BdWf.class);
		Map<String,String> params = new HashMap<String,String>();
		params.put("pkWf", bdWf.getPkWf());
		params.put("pkOrg", bdWf.getPkOrg());
		int countCode = 0;
		//检查code
		params.put("code", bdWf.getCode());
		countCode = wfMapper.BdWfCheckExist(params);
		if(countCode > 0) throw new BusException("流向分类编码重复！");
		
		
		if(bdWf.getPkWf() == null){
			DataBaseHelper.insertBean(bdWf);
		}else{
			DataBaseHelper.updateBeanByPk(bdWf,false);
		}
		return bdWf;
	}
	
	
	/**
	 * 医嘱流向模式参数及明细的保存和更新
	 * @param param
	 * @param user
	 */
	@Transactional
	public BdWfOrdArgu saveBdWfOrdArgu(String param , IUser user){
		BdWfOrdArguParam bdWfOrdArguParam = JsonUtil.readValue(param, BdWfOrdArguParam.class);
		BdWfOrdArgu bdWfOrdArgu = bdWfOrdArguParam.getBdWfOrdArgu();
		List<BdWfOrdArguDept> deptList = bdWfOrdArguParam.getBdWfOrdArguDept();
		if(deptList == null){
			deptList = Collections.EMPTY_LIST;
		}
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("pkWfargu", bdWfOrdArgu.getPkWfargu());
//		params.put("pkOrg", bdWfOrdArgu.getPkOrg());
//		params.put("pkWf", bdWfOrdArgu.getPkWf());
//		//检查执行机构
//		if(StringUtils.isNotBlank(bdWfOrdArgu.getPkOrgExec())){
//			params.put("pkOrgExec", bdWfOrdArgu.getPkOrgExec());
//			if(wfMapper.BdWfOrdArguCheckExist(params) > 0) throw new BusException("执行机构重复！");
//		}
//		//检查执行科室
//		if(StringUtils.isNotBlank(bdWfOrdArgu.getPkDept())){
//			params.remove("pkOrgExec");
//			params.put("pkDept", bdWfOrdArgu.getPkDept());
//			if(wfMapper.BdWfOrdArguCheckExist(params) > 0) throw new BusException("执行科室重复！");
//		}
		
		Map<String,String> map = new HashMap<String,String>();
		for (BdWfOrdArguDept bdWfOrdArguDept : deptList) {
			map.put(bdWfOrdArguDept.getPkDept(), "dept");
		}
		if(map.size() < deptList.size()){
			throw new BusException("执行科室重复！");
		}
		
		String pkWfargu = bdWfOrdArgu.getPkWfargu();
		if(pkWfargu == null){
			DataBaseHelper.insertBean(bdWfOrdArgu);
			pkWfargu = bdWfOrdArgu.getPkWfargu();
		}else{
			DataBaseHelper.updateBeanByPk(bdWfOrdArgu,false);
		}
		DataBaseHelper.execute("delete from BD_WF_ORD_ARGU_DEPT where PK_WFARGU = ?", new Object[]{pkWfargu});
		for (BdWfOrdArguDept bdWfOrdArguDept : deptList) {
			bdWfOrdArguDept.setPkWfargu(pkWfargu);
			DataBaseHelper.insertBean(bdWfOrdArguDept);
		}
		return bdWfOrdArgu;
	}
	
	/**
	 * 医嘱流向模式参数的删除
	 * @param param
	 * @param user
	 */
	@Transactional
	public void deleteBdWfOrdArgu(String param , IUser user){
		String pkWfargu = JSON.parseObject(param).getString("pkWfargu");
		DataBaseHelper.execute("delete from BD_WF_ORD_ARGU where PK_WFARGU = ?", new Object[]{pkWfargu});
		DataBaseHelper.execute("delete from BD_WF_ORD_ARGU_DEPT where PK_WFARGU = ?", new Object[]{pkWfargu});
	}
	

}
