package com.zebone.nhis.base.pub.service;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.pub.dao.CustomPrintMapper;
import com.zebone.nhis.base.pub.vo.CustomDeptVo;
import com.zebone.nhis.base.pub.vo.CustomPrintVo;
import com.zebone.nhis.common.module.base.bd.mk.BdTempOrdex;
import com.zebone.nhis.common.module.base.bd.mk.BdTempOrdexDept;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CustomPrintService {
	@Autowired
	CustomPrintMapper customPrintMapper;
	public List<BdTempOrdex> QueryCustomPrintList(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param,Map.class);
		String pkorg=(String)map.get("pkorg");
		List<BdTempOrdex> list=customPrintMapper.QueryCustomPrintList(pkorg);
		return list;
	}
	
	public List<CustomPrintVo> QueryDept(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param,Map.class);
		String pktempordex=(String)map.get("pktempordex");
		List<CustomPrintVo> list=customPrintMapper.QueryDept(pktempordex);
		return list;
	}
	
	public void UpdateCustomPrint(String param,IUser user){
		BdTempOrdex bd=JsonUtil.readValue(param,BdTempOrdex.class);
		if(CommonUtils.isEmptyString(bd.getPkTempordex())){
			Integer countCode = DataBaseHelper.queryForScalar("select count(*) from BD_TEMP_ORDEX where code=?", Integer.class, new Object[]{bd.getCode()});
			Integer countName = DataBaseHelper.queryForScalar("select count(*) from BD_TEMP_ORDEX where name=?", Integer.class, new Object[]{bd.getName()});
			if(countCode > 0){
				throw new BusException("该模板编码已存在！");
			}
			if(countName > 0){
				throw new BusException("该模板名称已存在！");
			}
			DataBaseHelper.insertBean(bd);
		}else{
			Integer countCode = DataBaseHelper.queryForScalar("select count(*) from BD_TEMP_ORDEX where code=? and PK_TEMPORDEX <> ?", Integer.class, new Object[]{bd.getCode(),bd.getPkTempordex()});
			Integer countName = DataBaseHelper.queryForScalar("select count(*) from BD_TEMP_ORDEX where name=? and PK_TEMPORDEX <> ?", Integer.class, new Object[]{bd.getName(),bd.getPkTempordex()});
			if(countCode > 0){
				throw new BusException("该模板编码已存在！");
			}
			if(countName > 0){
				throw new BusException("该模板名称已存在！");
			}
			DataBaseHelper.updateBeanByPk(bd,false);
		}
	}
	
	public void DeleteCustomPrint(String param,IUser user){
		BdTempOrdex bd=JsonUtil.readValue(param,BdTempOrdex.class);
		customPrintMapper.DeleteCustomDept(bd.getPkTempordex());
		DataBaseHelper.deleteBeanByPk(bd);
		
	}
	
	public List<CustomDeptVo> QueryCustomDept(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param,Map.class);
		String organizationid=(String)map.get("organizationid");
		String pktempordex=(String)map.get("pktempordex");
		List<CustomDeptVo> list=customPrintMapper.QueryCustomDept(organizationid, pktempordex);
		for (CustomDeptVo customDeptVo : list) {
			if(customDeptVo.getFlag() == null){
				customDeptVo.setFlag("0");
			}else customDeptVo.setFlag("1");	
		}
		return list;
	}
	/**
	 * 添加可用部门
	 * @param param
	 * @param user
	 */
	public void InsertCustomDept(String param,IUser user){
		List<BdTempOrdexDept> bdlist=JsonUtil.readValue(param,new TypeReference<List<BdTempOrdexDept>>(){});
		for (BdTempOrdexDept bd : bdlist) {
			if(CommonUtils.isEmptyString(bd.getPkTempdept())){
				DataBaseHelper.insertBean(bd);
			}
		}
	}
	/**
	 * 删除可用部门
	 * @param param
	 * @param user
	 */
	public void DeleteCustomDept(String param,IUser user){
		List<BdTempOrdexDept> bdlist=JsonUtil.readValue(param,new TypeReference<List<BdTempOrdexDept>>(){});
		for (BdTempOrdexDept bd : bdlist) {
			DataBaseHelper.deleteBeanByPk(bd);
		}
		
	}
	
	/**
	 * 根据主键获取打印模板信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getTempPrtByPk(String param , IUser user){
		
		String pkTempprt = JsonUtil.getFieldValue(param, "pkTempprt");
		
		Map<String, Object> temp = DataBaseHelper.queryForMapFj("select * from bd_temp_ordex where del_flag='0' and pk_tempordex = ?", pkTempprt);
		
		return temp;
	}
	
	/**
	 * 保存打印模板<br>
	 * 交易号：001002002035
	 * @param param
	 * @param user
	 */
	public BdTempOrdex saveTempPrt(String param , IUser user){

		BdTempOrdex tempprt = JsonUtil.readValue(param, BdTempOrdex.class);
		String pkOrg = ((User)user).getPkOrg();
		if(tempprt.getPkTempordex() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_temp_ordex "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%'", Integer.class, tempprt.getCode(),pkOrg);
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_temp_ordex "
					+ "where del_flag = '0' and name = ? and pk_org like ?||'%'", Integer.class, tempprt.getName(),pkOrg);
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(tempprt);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("当前机构内打印模板编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("当前机构内打印模板名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("当前机构内打印模板编码和名称都重复！");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_temp_ordex "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%' and pk_tempordex != ?", Integer.class, tempprt.getCode(),pkOrg,tempprt.getPkTempordex());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_temp_ordex "
					+ "where del_flag = '0' and name = ? and pk_org like ?||'%' and pk_tempordex != ?", Integer.class, tempprt.getName(),pkOrg,tempprt.getPkTempordex());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(tempprt,false);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("当前机构内打印模板编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("当前机构内打印模板名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("当前机构内打印模板编码和名称都重复！");
				}
			}
		}
		//防止响应过慢，返回前台时，不返回data_rpt大字段
		tempprt.setDataTemp(null);
		return tempprt;
	}
	
	public List<BdTempOrdex> QueryCustomPrintByPy(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param,Map.class);
		String filter=(String)map.get("filter");
		if(null == map.get("pkorg"))
			map.put("pkorg", ((User)user).getPkOrg());
		List<BdTempOrdex> list=customPrintMapper.QueryCustomPrintByPy(map);
		return list;
	}

	public List<BdTempOrdex> QueryCustomPrintListG(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param,Map.class);
		List<BdTempOrdex> list=customPrintMapper.QueryCustomPrintListG(map);
		return list;
	}
}
