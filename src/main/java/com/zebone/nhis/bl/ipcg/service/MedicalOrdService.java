package com.zebone.nhis.bl.ipcg.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.ipcg.dao.MedicalOrdMapper;
import com.zebone.nhis.bl.ipcg.vo.MedicalAppVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.service.EmployeePubService;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 医技医嘱服务
 * @author yangxue
 *
 */
@Service
public class MedicalOrdService {
	@Resource
    private MedicalOrdMapper medicalOrdMapper;
	
	/**
	 * 查询检查检验申请单列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MedicalAppVo> queryAppList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null || paramMap.get("pkDeptExec") ==null ||CommonUtils.isEmptyString(paramMap.get("pkDeptExec").toString()))
			throw new BusException("未获取到医技执行科室！");
		if(paramMap.get("dateBegin")!=null){
			paramMap.put("dateBegin",CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(paramMap.get("dateEnd")!=null){
			paramMap.put("dateEnd",CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8)+"235959");
		}
		return medicalOrdMapper.qryAppInfo(paramMap);
	}
	/**
	 * 查询医技医嘱模板内容
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnOrder> queryMedicalOrdList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		if(paramMap == null || paramMap.get("pkDept") ==null ||CommonUtils.isEmptyString(paramMap.get("pkDept").toString()))
			throw new BusException("未获取到当前科室！");
		if(paramMap.get("spcode") != null)
		paramMap.put("spcode",paramMap.get("spcode").toString().toUpperCase());

		return medicalOrdMapper.queryMedicalOrdList(paramMap);
	}
	
}
