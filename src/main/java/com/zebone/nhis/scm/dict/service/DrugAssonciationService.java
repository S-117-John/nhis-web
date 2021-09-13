package com.zebone.nhis.scm.dict.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.scm.dict.dao.DrugAssonciationMapper;
import com.zebone.nhis.scm.dict.vo.BdPdRelVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;



@Service
public class DrugAssonciationService {
	
	@Autowired
	private DrugAssonciationMapper drugAssionciationMapper;
	
	/**
	 * 查询皮试标识的药品信息
	 * @return
	 */
	public List<Map<String, Object>> queryBdPdList(String param,IUser user){
		List<Map<String, Object>> map = DataBaseHelper.queryForList("select pd.pk_pd, pd.code,  pd.name,  pd.spec, pd.spcode,pd.flag_stop from bd_pd pd where pd.flag_st='1' and pd.del_flag='0'", new Object[]{});
		return map;
	}
	
	/**
	 * 查询皮试药品关联的皮试剂
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryByPkPd(String param,IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		String pkPd = (String) map.get("pkPd");
		List<Map<String,Object>> lbprv = drugAssionciationMapper.queryByPkPd(pkPd);
		return lbprv;
	}
	
	/**
	 * 保存皮试药品关联信息
	 * @param param
	 * @param user
	 */
	public void saveBdPdRel(String param,IUser user){
		List<BdPdRelVo> bprv = JsonUtil.readValue(param, new TypeReference<List<BdPdRelVo>>() {});
		//BdPdRelVo bprv = JsonUtil.readValue(param, BdPdRelVo.class);
		if(bprv == null) return;
		for (BdPdRelVo bdPdRelVo : bprv) {
			if(bdPdRelVo.getPkPdrel()==null)
			{
				List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
				
				if(StringUtils.isBlank(bdPdRelVo.getPkPdrel())){
					list = DataBaseHelper.queryForList("select rel.pk_pdrel, rel.pk_pd_rel,rel.sortno,rel.note from bd_pd_rel rel "
							+" where rel.pk_pd= ? and rel.eu_reltype='0' and rel.del_flag='0' ",new Object[]{bdPdRelVo.getPkPd()});	
				}
				
				DataBaseHelper.insertBean(bdPdRelVo);
			}else{
				DataBaseHelper.updateBeanByPk(bdPdRelVo,false);
			}
		}
	}
	
	/**
	 * 删除皮试药品关联信息
	 * @param param
	 * @param user
	 */
	public void delByPkPdrel(String param,IUser user){
		Map<Object,String> map = JsonUtil.readValue(param, Map.class);
		String pkPdrel = map.get("pkPdrel");
		drugAssionciationMapper.delByPkPdrel(pkPdrel);
	}
}
