package com.zebone.nhis.base.bd.service;

import com.zebone.nhis.base.bd.vo.BdDictattrTempVO;
import com.zebone.nhis.common.module.base.bd.code.BdDictattrTemp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DictattrTempService {

	/**
	 * 查询数据字典类型
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getDefdoc(String param, IUser user) {

		List<Map<String, Object>> queryForList = DataBaseHelper.queryForList("select dttype.code,dttype.name from bd_defdoc dttype where dttype.code_defdoclist='800003' and dttype.del_flag='0'",new Object[] {});
		return queryForList;
	}

	/**
	 * 查询数据字典类型下的扩展属性模板
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getDictattrTemp(String param, IUser user) {
		String dtDicttype = JsonUtil.getFieldValue(param, "dtDicttype");
		List<Map<String, Object>> list = DataBaseHelper.queryForList("select tmp.pk_dictattrtemp,tmp.code_attr,tmp.name_attr,tmp.desc_attr,tmp.val_attr,tmp.pk_org_use from bd_dictattr_temp tmp where tmp.dt_dicttype=? and tmp.del_flag='0' ", new Object[]{dtDicttype});
		return list;
	}


	/**
	 * 001002002116
	 * 获取人员字典0704权限
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeAuthority(String param, IUser user) {
		String code = JsonUtil.getFieldValue(param, "code");
		String name = JsonUtil.getFieldValue(param, "name");
		List<Map<String, Object>> list = DataBaseHelper.queryForList("select VAL_ATTR from bd_dictattr attr inner join bd_ou_employee ou on attr.pk_dict = ou.PK_EMP  where CODE_ATTR='0704' and ou.code_emp=? and name_emp=? and ou.del_flag=0", new Object[]{code,name});
		return list;
	}


	/**
	 * 保存扩展属性模板
	 * @param param
	 * @param user
	 */
	public void saveDictattrTemp(String param, IUser user) {
		List<BdDictattrTempVO> readValue = JsonUtil.readValue(param,new TypeReference<List<BdDictattrTempVO>>() {});
		for (BdDictattrTempVO bd : readValue) {
			BdDictattrTemp temp = new BdDictattrTemp();
			ApplicationUtils.copyProperties(temp, bd);
			if (StringUtils.isBlank(bd.getPkDictattrtemp())) {
				//checkRepeatValue(bd); //在前台控制
				DataBaseHelper.insertBean(temp);
			} else {
				if("1".equals(bd.getRowStatus())){
					//checkRepeatValue(bd);//在前台控制
					DataBaseHelper.updateBeanByPk(temp,false);
				}
			}
		}

	}
	
	/**
	 * 删除扩展属性模板
	 * @param param
	 * @param user
	 */
	public void delDictattrTemp(String param, IUser user){
		//BdDictattrTemp temp = JsonUtil.readValue(param, BdDictattrTemp.class);
		BdDictattrTemp temp = JsonUtil.readValue(param, new TypeReference<BdDictattrTemp>() {
		});
		//删除校验
		List<Map<String, Object>> execute = DataBaseHelper.queryForList("select count(1) as num from bd_dictattr where pk_dictattrtemp=?", new Object[]{temp.getPkDictattrtemp()});
		if(Integer.parseInt((execute.get(0).get("num")).toString())>0){
			throw new BusException("该模板已被使用，请先取消该模板的使用!");
		}
		DataBaseHelper.execute("update bd_dictattr_temp set del_flag='1' where pk_dictattrtemp=? and del_flag='0' ", new Object[]{temp.getPkDictattrtemp()});
	}
	
	/**
	 * 校验重复值
	 * @param BdDictattrTemp
	 */
	private void checkRepeatValue(BdDictattrTemp dt) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (!StringUtils.isBlank(dt.getPkDictattrtemp())) {// 修改校验
			list = DataBaseHelper.queryForList("select tmp.code_attr,tmp.name_attr from bd_dictattr_temp tmp where tmp.dt_dicttype=? and tmp.pk_dictattrtemp != ? and tmp.del_flag='0' ",new Object[] { dt.getPkDictattrtemp(),dt.getDtDicttype()});
		} else {// 新增校验
			list = DataBaseHelper.queryForList("select tmp.code_attr,tmp.name_attr from bd_dictattr_temp tmp where tmp.dt_dicttype=? and tmp.del_flag='0'",new Object[]{dt.getDtDicttype()});
		}
		for (Map<String, Object> m : list) {
			if (dt.getCodeAttr().equals(m.get("codeAttr"))) {
				throw new BusException("您的属性编码重复!");
			}
			if (dt.getNameAttr().equals(m.get("nameAttr"))) {
				throw new BusException("您的属性名称重复!");
			}
		}
	}

}
