package com.zebone.nhis.base.bd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.HdResBedMapper;
import com.zebone.nhis.common.module.base.bd.res.BdResHdbed;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 透析床位管理
 * @author IBM
 *
 */
@Service
public class HdResBedService {
	@Resource
	private HdResBedMapper hdResBedMapper;
	
	/**
	 * 查询透析床位信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryDialysisBeds(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		//pkOrg 当前机构  
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkOrg")))
			throw new BusException("未获取到要查询的机构");	
		List<Map<String,Object>> resBedList=hdResBedMapper.queryDialysisBeds(paramMap);
		return resBedList;		
	}
	
	/**
	 * 保存透析床位信息
	 * @param param
	 * @param user
	 */
	public void saveResBed(String param,IUser user){
		BdResHdbed resBed=JsonUtil.readValue(param, BdResHdbed.class);
		//判断是否有新增参数
		if (resBed == null)
			throw new BusException("未获取到需要保存的初始数据");
		Map<String,String> paramMap =new HashMap<String,String>();
		paramMap.put("pkOrg", resBed.getPkOrg());
		
		//查询床号是否重复
		paramMap.put("codeBed", null);
		paramMap.put("nameBed", resBed.getNameBed());
		List<Map<String,Object>> resNameList=hdResBedMapper.queryDialysisBeds(paramMap);
		if(resNameList.size()>0 &&(resBed.getPkHdbed()==null||"".equals(CommonUtils.getString(resBed.getPkHdbed())))){
			throw new BusException("床位名称已被占用！");
		}else{
			//判断已被占用的床位是否为原修改床位的
			for(Map<String,Object> o : resNameList){
				if(!o.get("pkHdbed").equals(resBed.getPkHdbed()))
					throw new BusException("床位名称已被占用！");				
			}
		}
		
		//查询编码是否重复
		paramMap.put("codeBed", resBed.getCodeBed());
		paramMap.put("nameBed", null);
		List<Map<String,Object>> resBedList=hdResBedMapper.queryDialysisBeds(paramMap);
		if(resBedList.size()>0 &&(resBed.getPkHdbed()==null||"".equals(CommonUtils.getString(resBed.getPkHdbed())))){
			throw new BusException("床位编码已被占用！");	
		}else{
			//判断已被占用的床位是否为原修改床位的
			for(Map<String,Object> o : resBedList){
				if(!o.get("pkHdbed").equals(resBed.getPkHdbed()))
					throw new BusException("床位编码已被占用！");				
			}
		}
		
		//判断新增或修改
		if(resBed.getPkHdbed()==null||"".equals(CommonUtils.getString(resBed.getPkHdbed()))){
			DataBaseHelper.insertBean(resBed);
		}else{
			DataBaseHelper.update("update BD_RES_HDBED set CODE_BED=:codeBed,NAME_BED=:nameBed,SORTNO=:sortno,PK_DEPT=:pkDept,PK_DEPT_NS=:pkDeptNs,PK_MSP=:pkMsp,DT_HDTYPE=:dtHdtype,FLAG_OPEN=:flagOpen,DT_DATESLOTTYPE=:dtDateslottype,NOTE=:note where  PK_HDBED=:pkHdbed",resBed);
		}			
	}
	
	/**
	 * 删除透析床位信息
	 * @param param
	 * @param user
	 */
	public void deleteResBed(String param,IUser user){
		BdResHdbed resBed=JsonUtil.readValue(param, BdResHdbed.class);
		//判断是否有新增参数
		if (resBed == null)
			throw new BusException("未获取到需要保存的初始数据");
		
		//查询是否符合删除条件
		if(resBed==null||CommonUtils.isNull(resBed.getPkHdbed()))
			throw new BusException("未获取到患者床位");
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("pkHdbed", resBed.getPkHdbed());
		List<Map<String,Object>> result=hdResBedMapper.queryPvEncounterByBedNo(paramMap);
		if(result.size()>0){
			String ifDelete=result.get(0).get("ifdelete").toString();
			int YOrN = Integer.parseInt(ifDelete);
			if(YOrN==0){
				String sql_d = "DELETE FROM BD_RES_HDBED where PK_HDBED=?";
				int t=DataBaseHelper.execute(sql_d, new Object[]{resBed.getPkHdbed()});
				//DataBaseHelper.deleteBeanByPk(resBed);
			}else{
				throw new BusException("该床位已被占用，不能删除！");
			}
		}
		else{
			throw new BusException("不符合删除条件");
		}		
	}
}
