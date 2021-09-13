package com.zebone.nhis.compay.ins.shenzhen.service.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDictMap;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.shenzhen.dao.base.ShenZhenMediBaseDictMapMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 
 * @author 
 *
 */
@Service
public class ShenZhenMediBaseDictService {
	
	@Autowired
	private ShenZhenMediBaseDictMapMapper szMdeiBaseMapper ;
	
	/**
	 * 015001011044
	    *   根据医保单位  查询左侧医保数据基础对照
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsSzybDictMap> getMediType(String param, IUser user) {
		String euHpdicttype = JsonUtil.getFieldValue(param, "euHpdicttype");
		List<InsSzybDictMap> mediType = szMdeiBaseMapper.getMediType(euHpdicttype);
		return mediType;
		
	}
	
	/**
	 * 
	 * 015001011045
	    *   保存左侧医保基础数据对照(保存组)
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveMediType(String param, IUser user) {
		InsSzybDictMap insSzybDictmap = JsonUtil.readValue(param, InsSzybDictMap.class);
		if (insSzybDictmap == null ) {
			throw new BusException("未获取到要保存的数据！");
		}
		//如存在主键就更新，不存在则添加
		if (insSzybDictmap.getPkInsdictmap() == null || "".equals(insSzybDictmap.getPkInsdictmap())) {
			ApplicationUtils.setBeanComProperty(insSzybDictmap, true);
			insSzybDictmap.setModifier(null);
			insSzybDictmap.setModifierTime(null);
			DataBaseHelper.insertBean(insSzybDictmap);
		}else {
			ApplicationUtils.setBeanComProperty(insSzybDictmap, false);
			insSzybDictmap.setModifier(user.getUserName());
			insSzybDictmap.setModifierTime(new Date());
			DataBaseHelper.updateBeanByPk(insSzybDictmap, false);
		}
	}
	
	/**
	 * 015001011057
	   *      删除组数据
	 * @param param
	 * @param user
	 */
	public void delMediType(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String codeType = (String) paramMap.get("codeType");
		String pkInsdictmap = (String)paramMap.get("pkInsdictmap");
		String upSql = "update ins_szyb_dictmap  set Del_flag = '1' where code_type = ?";
		String sql = "update ins_szyb_dictmap  set Del_flag = '1' where pk_insdictmap = ?";
		DataBaseHelper.update(sql, new Object[] {pkInsdictmap });
		DataBaseHelper.update(upSql, new Object[] {codeType });//删除组时同时删除组的对照信息

	}
	
	/**
	 * 015001011058
	 *	医保基础数据对照查询接口     
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> querySZMediBaseData(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> list = szMdeiBaseMapper.querySZMediBaseData(paramMap);
		return list;
	}
	
	/**
	 * 015001011061
	 *     批量删除（更新） 医保基础对照数据信息
	 * @param param
	 * @param user
	 * @return
	 */
	public void batchUpdateInsSzybInfo(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<InsSzybDictMap> delList = (List<InsSzybDictMap>) paramMap.get("delList");
		szMdeiBaseMapper.batchUpdateInsSzybInfo(delList);

	}
	
	/**
	 * 015001011063
	   *    深圳医保基础数据对照信息保存
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsSzybDictMap> saveInsSzybDictInfo(String param, IUser user) {
		List<InsSzybDictMap> updateList = JsonUtil.readValue(param, new TypeReference<List<InsSzybDictMap>>(){});
		if(updateList ==null||updateList.size()<=0) {
			throw new BusException("未获取到要保存的信息！");
		}
    	for(InsSzybDictMap vo: updateList){
//    		InsSzybDictMap dt = new InsSzybDictMap();
//    		ApplicationUtils.copyProperties(dt, vo);
    		if(CommonUtils.isEmptyString(vo.getPkInsdictmap())){
    			ApplicationUtils.setBeanComProperty(vo, true);
    			vo.setModifier(null);
    			vo.setModifierTime(null);
    			DataBaseHelper.insertBean(vo);
    		}else{
    			ApplicationUtils.setBeanComProperty(vo, false);
    			vo.setModifier(user.getUserName());
    			vo.setModifierTime(new Date());
    			DataBaseHelper.updateBeanByPk(vo,false);
    		}
    	}
		return updateList;
	}

}
