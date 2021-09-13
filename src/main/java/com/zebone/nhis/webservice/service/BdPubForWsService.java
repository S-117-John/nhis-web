package com.zebone.nhis.webservice.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.ApplicationUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.vo.BdSerialNoWs;
import com.zebone.nhis.webservice.vo.deptvo.DeptTypesVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 基础数据域webservcie专用公共服务
 * @author yangxue
 *
 */
@Service
public class BdPubForWsService {
   @Resource
   private BdPubForWsMapper bdPubForWsMapper;
   
   /**
    * 查询系统所有启用状态的机构信息
    * @author leiminjian
    * @return
    */
	public List<Map<String,Object>> getOrgInfo(Map<String,Object> paramMap){
		List<Map<String,Object>> orgMap = bdPubForWsMapper.LbqueryOrgs();
		return orgMap;
	}
	
	/**
    * 查询系统所有启用状态的机构信息
    * @author leiminjian
    * @return
    */
	public List<Map<String,Object>> getOrgList(Map<String,Object> paramMap){
		List<Map<String,Object>> orgMap = bdPubForWsMapper.queryOrg(paramMap);
		return orgMap;
	}
	/**
    * 查询机构院区信息
    * @author zhangtao
    * @param paramMap: pkOrg:机构唯一标识
    * @return
    */
	public List<Map<String,Object>> getOrgAreaInfo(Map<String,Object> paramMap){
		List<Map<String,Object>> deptMap = bdPubForWsMapper.queryOrgArea(paramMap);
		return deptMap;
	}
	/**
    * 查询科室信息
    * @author leiminjian
    * @param paramMap: pkOrg:机构唯一标识 ; codeOrg：机构编码
    * @return
    */
	public List<Map<String,Object>> getDeptInfo(Map<String,Object> paramMap){
		List<Map<String,Object>> deptMap = bdPubForWsMapper.queryDept(paramMap);
		return deptMap;
	}
	
	/**
    * 查询医生信息
    * @author leiminjian
    * @param paramMap: pkDept：科室唯一标识 ; codeDept：科室编码(二选一)   flagActive :启用状态 0 停用 1启用   delFlag：删除标志 0 未删除 1删除
    * @return
    */
	public List<Map<String,Object>> getEmpInfo(Map<String,Object> paramMap){
		List<Map<String,Object>> deptMap = bdPubForWsMapper.queryEmployee(paramMap);
		return deptMap;
	}
    /**
     * 查询收费项目分类
     * @param paramMap
     * @return
     */
	public List<Map<String,Object>> getItemCateInfo(Map<String,Object> paramMap){
		return bdPubForWsMapper.getItemCateInfo(null);
	}
	/**
	 * 查询医保信息
	 * @return
	 */
	public List<Map<String,Object>> getHpInfo(Map<String,Object> paramMap){
		return bdPubForWsMapper.getHpInfo(paramMap);
	}
   
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public int getSerialNo(String tableName, String fieldName, int count, IUser user){
		if(tableName==null) return 0;
		Double sn = bdPubForWsMapper.selectSn(tableName.toUpperCase(), fieldName.toUpperCase()); 
		if(sn==null){
			BdSerialNoWs initSn = new BdSerialNoWs();
			initSn.setPkSerialno(NHISUUID.getKeyId());
			initSn.setPkOrg(CommonUtils.getGlobalOrg());
			initSn.setNameTb(tableName.toUpperCase());
			initSn.setNameFd(fieldName.toUpperCase());
			initSn.setValInit((short)1000);
			initSn.setVal((short)1000);
			bdPubForWsMapper.initSn(initSn);
		}
		int ret = ApplicationUtils.getSerialNo(tableName,fieldName,count);
		return ret;
	}
	
	public int getSerialNo(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		return this.getSerialNo(paramMap.get("tableName"), paramMap.get("fieldName"), 
					Integer.parseInt(paramMap.get("count")), user);
	}

    /**
     * 查询科室属性
     * @param list
     * @return
     */
	public List<DeptTypesVo> getDeptTypes(List<String> list) {
		return bdPubForWsMapper.getDeptTypes(list);
	}
}
