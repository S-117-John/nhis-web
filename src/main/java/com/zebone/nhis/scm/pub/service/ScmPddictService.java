package com.zebone.nhis.scm.pub.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.pub.BdPdRest;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.scm.pub.dao.ScmPddictMapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 供应链字典服务
 * @author lijipeng
 *
 */
@Service
public class ScmPddictService {
	
	@Autowired
	private ScmPddictMapper scPddictMapper;
	
	/**
	 * 交易号：008001001077
	 * 查询药品列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getBdPdList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		String spcode = null;
		//将助记码转为大写
		if(paramMap.get("spcode")!=null){			
			spcode = paramMap.get("spcode").toString().toUpperCase();
			paramMap.put("spcode", spcode);
		}
		
		return scPddictMapper.searchBdPds(paramMap);
	}
	
	/**
	 * 交易号：008001001078
	 * 查询药品限制使用信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBdPdRest(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//获取当前机构
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		
		return scPddictMapper.searchPdRestByPkPd(paramMap);
	}
	
	/**
	 * 交易号：008001001079
	 * 保存药品限制使用信息
	 * @param param
	 * @param user
	 */
	public void saveBdPdRest(String param,IUser user){
		
		List<BdPdRest> restList = JsonUtil.readValue(param, new TypeReference<List<BdPdRest>>() {
		});
		
		Map<String, String> pkEmpMap = new HashMap<String, String>();	//存放类型为0(医生)的主键信息
		Map<String, String> pkDeptMap = new HashMap<String, String>();	//存放类型为1(科室)的主键信息
		Map<String, String> pkDiagMap = new HashMap<String, String>();	//存放类型为2(诊断)的主键信息
		Map<String,String>  pkPiMap=new HashMap<>();//存放类型3（患者） 的主键信息
		
		/**校验---1.校验前台所传的list的每一条信息的唯一性*/
		if(restList != null && restList.size() > 0){
			for(int i=0; i<restList.size(); i++){
				
				String pkEmp = restList.get(i).getPkEmp();
				String pkDept = restList.get(i).getPkDept();
				String pkDiag = restList.get(i).getPkDiag();
				String pkPi = restList.get(i).getPkPi();
				
				switch (restList.get(i).getEuCtrltype()) {
				case "0":	//医生
					if(pkEmpMap.containsKey(pkEmp)){
						throw new BusException("类型为[医生]的编码重复！");
					}
					pkEmpMap.put(pkEmp,restList.get(i).getPkPdrest());
					break;
				case "1":	//科室
					if(pkDeptMap.containsKey(pkDept)){
						throw new BusException("类型为[科室]的编码重复！");
					}
					pkDeptMap.put(pkDept, restList.get(i).getPkPdrest());
					break;
				case "2":	//诊断
					if(pkDiagMap.containsKey(pkDiag)){
						throw new BusException("类型为[诊断]的编码重复！");
					}
					pkDiagMap.put(pkDiag, restList.get(i).getPkPdrest());
					break;
					case "3":	//患者
						if(pkPiMap.containsKey(pkPi)){
							throw new BusException("类型为[患者]的编码重复！");
						}
						pkPiMap.put(pkPi, restList.get(i).getPkPdrest());
						break;
				default:
					break;
				}
				
			}
		}
		
		/**获取该药品数据库中药品限制使用信息*/
		Map<String,Object> paramMap = new HashMap<>();
		//获取当前机构
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		//获取药品主键
		paramMap.put("pkPd", restList.get(0).getPkPd());
		//查询信息
		List<Map<String,Object>> allList = scPddictMapper.searchPdRestByPkPd(paramMap);
		
		/**校验---2.校验前台所传的list的每一条编码和名称是否和数据库重复*/
		for(Map<String,Object> restMap : allList){
			
			String pkPdCate = "";
			
			switch (restMap.get("euCtrltype").toString()) {
			case "0":	//医生
				if(pkEmpMap.containsKey(restMap.get("pkEmp"))){
					pkPdCate = pkEmpMap.get(restMap.get("pkEmp"));
					if(pkPdCate == null){	//新增情况
						throw new BusException("类型为[医生]编码在数据库中已存在！");
					}else{					//修改情况
						if(!restMap.get("pkPdrest").equals(pkPdCate)){
							throw new BusException("类型为[医生]编码在数据库中已存在！");
						}
					}
				}
				
				break;
			case "1":	//科室
				if(pkDeptMap.containsKey(restMap.get("pkDept"))){
					pkPdCate = pkDeptMap.get(restMap.get("pkDept"));
					if(pkPdCate == null){
						throw new BusException("类型为[科室]编码在数据库中已存在！");
					}else{
						if(!restMap.get("pkPdrest").equals(pkPdCate)){
							throw new BusException("类型为[科室]编码在数据库中已存在！");
						}
					}
				}
				
				break;
			case "2":	//诊断
				if(pkDiagMap.containsKey(restMap.get("pkDiag"))){
					pkPdCate = pkDiagMap.get(restMap.get("pkDiag"));
					if(pkPdCate == null){
						throw new BusException("类型为[诊断]编码在数据库中已存在！");
					}else{
						if(!restMap.get("pkPdrest").equals(pkPdCate)){
							throw new BusException("类型为[诊断]编码在数据库中已存在！");
						}
					}
				}
				
				break;
				case "3":	//患者
					if(pkDiagMap.containsKey(restMap.get("pkPi"))){
						pkPdCate = pkDiagMap.get(restMap.get("pkPi"));
						if(pkPdCate == null){
							throw new BusException("类型为[患者]编码在数据库中已存在！");
						}else{
							if(!restMap.get("pkPdrest").equals(pkPdCate)){
								throw new BusException("类型为[患者]编码在数据库中已存在！");
							}
						}
					}

					break;
			default:
				break;
			}
		}
		
		/**新增或更新到数据库*/
		for(BdPdRest saveInfo : restList){
			if(CommonUtils.isEmptyString(saveInfo.getPkPdrest())){
				DataBaseHelper.insertBean(saveInfo);
			}else{
				DataBaseHelper.updateBeanByPk(saveInfo,false);
			}
		}
		
	}
	
	/**
	 * 交易号：008001001080
	 * 删除药品限制使用信息
	 * @param param
	 * @param user
	 */
	public void delBdPdRests(String param,IUser user){
		List<String> delList = JsonUtil.readValue(param, new TypeReference<List<String>>() {
		});
		
		if(delList!=null && delList.size()>0){
			//批量删除
			scPddictMapper.batchDelBdPdRest(delList);
		}
	}
	
	/**
	 * 查询仓库物品包装信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPdStorePack(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param, "pkPd");
		if(CommonUtils.isEmptyString(pkPd)) return null;
		List<Map<String,Object>> resList=scPddictMapper.qryPdStorePack(pkPd);
		return resList;
	}
	
	
	/**
	 * 交易码：008001001100
	 * 查询当前仓库下的药品出入库记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchPdStRecord(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStoreSt", UserContext.getUser().getPkStore());
		if( CommonUtils.isNotNull(paramMap.get("dateBegin")) &&
				CommonUtils.isNotNull(paramMap.get("dateEnd"))){
			String dateBegin = paramMap.get("dateBegin").toString();
			String dateEnd = paramMap.get("dateEnd").toString();
			if(dateBegin.equals(dateEnd))
			{
				paramMap.put("dateBegin", dateBegin.substring(0, dateBegin.length()-6)+"000000");
				paramMap.put("dateEnd", dateBegin.substring(0, dateBegin.length()-6)+"235959");
			}
		}
		
		List<Map<String,Object>> queryList = scPddictMapper.searchPdStRecord(paramMap);
		return queryList;
	}


	/**
	 * 008001001102
	 * 查询药品仓库单位信息
	 * @param param
	 * @param user
	 * @return
	 */
	public  List<Map<String,Object>> qryPdStorePackInfo(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param,"pkPd");
		if(CommonUtils.isNull(pkPd))return null;
		List<Map<String,Object>> resList=scPddictMapper.qryPdStorePackInfo(pkPd);
		return resList;
	}

	/**
	 * 008001001103
	 * 查询药品停用日志
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPdStopLogInfo(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param,"pkPd");
		if(CommonUtils.isNull(pkPd))return null;

		List<Map<String,Object>>  resList=scPddictMapper.qryPdStopLogInfo(pkPd);
		return resList;
	}

	/**
	 * 008001001104
	 * 查询药品平台编码列表数据
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryPdOutcodeList(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param,"pkPd");
		if(CommonUtils.isNull(pkPd))return null;
		String sql="select * from bd_pd_outcode where pk_pd=?";
		List<Map<String,Object>> resList=DataBaseHelper.queryForList(sql,new Object[]{pkPd});
		return resList;
	}
}
