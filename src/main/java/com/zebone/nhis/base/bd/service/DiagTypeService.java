package com.zebone.nhis.base.bd.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.DiagTypeMapper;
import com.zebone.nhis.common.module.base.bd.code.BdTermDiagTreatway;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 疾病诊治方式后台配置程序
 * @author gejianwen
 *
 */
@Service
public class DiagTypeService {
	
	@Autowired
	private DiagTypeMapper diagTypeMapper;
	
	/**
	 * trans code 001002004024
	 * 保存疾病治疗方式
	 * @param param
	 * @param user
	 */
	public void saveDiagTypeList(String param , IUser user){
		List<BdTermDiagTreatway> param_list = JsonUtil.readValue(param,  new TypeReference<List<BdTermDiagTreatway>>(){});
		User us = (User)user;
		Date date = new Date();
		for(BdTermDiagTreatway bdTermDiagTreatway : param_list){
			bdTermDiagTreatway.setTs(date);
			if (StringUtils.isNotBlank(bdTermDiagTreatway.getPkDiagtreatway())) {
				bdTermDiagTreatway.setModifier(us.getPkEmp());
				bdTermDiagTreatway.setModityTime(date);
//				DataBaseHelper.updateBeanByPk(bdTermDiagTreatway,false);
				diagTypeMapper.updateByPk(bdTermDiagTreatway);
			}else {
				bdTermDiagTreatway.setPkOrg(us.getPkOrg());
				bdTermDiagTreatway.setCreator(us.getPkEmp());
				bdTermDiagTreatway.setCreateTime(new Date());
				bdTermDiagTreatway.setDelFlag("0");
				DataBaseHelper.insertBean(bdTermDiagTreatway);
			}
		}		
	}
	
	/**
	 * trans code 001002004025
	 * 获取保存疾病治疗方式列表
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BdTermDiagTreatway> getDiagTypeListOld(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		BdTermDiagTreatway btd = JsonUtil.readValue(param, BdTermDiagTreatway.class);
		if(null == btd) throw new BusException("前台数据错误");	
		
		User u = UserContext.getUser();
		String sql = "select * from BD_TERM_DIAG_TREATWAY bd where 1=1 ";
		if(!StringUtils.isEmpty(btd.getCodeDiag())) sql += "and bd.CODE_DIAG like '%"+btd.getCodeDiag()+"%' ";
		if(!StringUtils.isEmpty(btd.getNameDiag())) sql += "and bd.NAME_DIAG like '%"+btd.getNameDiag()+"%' ";
		if(!StringUtils.isEmpty(btd.getDtTreatway())) sql += "and bd.DT_TREATWAY like '%"+btd.getDtTreatway()+"%' ";
		if(!StringUtils.isEmpty(u.getPkOrg())) sql += "and bd.PK_ORG = '"+u.getPkOrg()+"'";
		
		sql += " order by bd.ts";
		
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql);
		if(null == ll) return null;
		
		List<BdTermDiagTreatway> ret = new ArrayList<BdTermDiagTreatway>();		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		for(Map<String,Object> m : ll){
			btd = new BdTermDiagTreatway();
			BeanUtils.copyProperties(btd, m);
			ret.add(btd);
		}
		
		return ret;		
	}
	
	/**
	 * trans code 001002004026
	 * 删除疾病治疗方式
	 * @param param
	 * @param user
	 */
	public void delDiagTypeList(String param , IUser user){
		List<BdTermDiagTreatway> param_list = JsonUtil.readValue(param,  new TypeReference<List<BdTermDiagTreatway>>(){});		
		for(BdTermDiagTreatway btd : param_list){
			diagTypeMapper.delTermDiagTreatway(btd.getPkDiagtreatway());
		}		
	}
	
	/**
	 * 删除时调用，校验是否被引用
	 * 001002004030
	 * @param param
	 * @param user
	 * @return
	 */
	public int isQuote(String param ,IUser user){
		List<BdTermDiagTreatway> param_list = JsonUtil.readValue(param,  new TypeReference<List<BdTermDiagTreatway>>(){}); 
		int aa = 0;
		Map<String,String> params = new HashMap<String,String>();
		for(BdTermDiagTreatway btd : param_list){
			params.put("code", btd.getCodeDiag());
			params.put("treatway", btd.getDtTreatway());
			int count = diagTypeMapper.isQuote(params);
			if (count > 0) {
				aa = 1;
			}
		}
		return aa;
				
		
	}
	
	/**
	 * 001002004025
	 * 查询诊治方式数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdTermDiagTreatway> getDiagTypeList(String param ,IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if(map==null)return null;
		User us=(User)user;
		map.put("pkOrg", "~                               ");
		map.put("pkOrg2", UserContext.getUser().getPkOrg());
		return diagTypeMapper.qryDiagtreatway(map);
	}
	
}
