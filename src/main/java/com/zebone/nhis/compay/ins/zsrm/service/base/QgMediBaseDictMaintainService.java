package com.zebone.nhis.compay.ins.zsrm.service.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.zsrm.dao.base.ZsrmQgBaseDictMaintainMapper;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybDict;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class QgMediBaseDictMaintainService {

	@Autowired
	private ZsrmQgBaseDictMaintainMapper zsrmQgBaseDictMaintainMapper;
	
	/**
	 * 交易号：015001011062
	 * 根据医保单位查询 医保基础数据类型
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsQgybDict> querySZYBBaseDictType(String param, IUser user){
		String euHpdicttype = JsonUtil.getFieldValue(param, "euHpdicttype");
		List<InsQgybDict> insSZybTypeList = zsrmQgBaseDictMaintainMapper.querySZYBBaseDictType(euHpdicttype);
		return insSZybTypeList;
		
	}
	
	/**
	 * 交易号：015001011052
	 * 根据医保单位查询 医保基础数据类型
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsQgybDict> querySZYBBaseDictInfo(String param, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<InsQgybDict> insSZybTypeList = zsrmQgBaseDictMaintainMapper.querySZYBBaseDictInfo(paramMap);
		return insSZybTypeList;
		
	}
	
	/**
	 * 交易号：015001011053
	 * 保存医保基础数据 信息（组）
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveSZYBType(String param, IUser user){
		InsQgybDict insSzybDict = JsonUtil.readValue(param, InsQgybDict.class);
		if (insSzybDict == null ) {
			throw new BusException("未获取到要保存的数据！");
		}
		//如果为空主键就更新，不存在则添加
		if (insSzybDict.getPkInsdict() == null || "".equals(insSzybDict.getPkInsdict())) {
			ApplicationUtils.setBeanComProperty(insSzybDict, true);
			insSzybDict.setModifier(null);
			insSzybDict.setModifierTime(null);
			DataBaseHelper.insertBean(insSzybDict);
		}else {
			ApplicationUtils.setBeanComProperty(insSzybDict, false);
			insSzybDict.setModifier(user.getUserName());
			insSzybDict.setModifierTime(new Date());
			DataBaseHelper.updateBeanByPk(insSzybDict, false);
		}
	}
	
	/**
	 *       交易号： 015001011054
	   *      删除组数据
	 * @param param
	 * @param user
	 */
	public void delSzybType(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String codeType = (String) paramMap.get("codeType");
		String pkInsdict = (String)paramMap.get("pkInsdict");
		String upSql = "update ins_qgyb_dict  set Del_flag = '1' where code_type = ?";
		String sql = "update ins_qgyb_dict  set Del_flag = '1' where pk_insdict = ?";
		DataBaseHelper.update(sql, new Object[] {pkInsdict });
		DataBaseHelper.update(upSql, new Object[] {codeType });//删除组时同时删除组的对照信息

	}
	/**
	 *       交易号： 015001011055
	 *      删除行数据
	 * @param param
	 * @param user
	 */
	public void batchUpdateSZYBBaseDict(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<InsQgybDict> delList = (List<InsQgybDict>) paramMap.get("delList");
		zsrmQgBaseDictMaintainMapper.batchUpdateSZYBBaseDict(delList);
	}

	/**
	 * 015001011056
	   *    保存 深圳医保基础数据信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsQgybDict> saveSzybBaseDictInfo(String param, IUser user) {
		List<InsQgybDict> updateList = JsonUtil.readValue(param, new TypeReference<List<InsQgybDict>>(){});
		if(updateList ==null||updateList.size()<=0) {
			throw new BusException("未获取到要保存的信息！");
		}
    	for(InsQgybDict vo: updateList){
    		if(CommonUtils.isEmptyString(vo.getPkInsdict())){
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
