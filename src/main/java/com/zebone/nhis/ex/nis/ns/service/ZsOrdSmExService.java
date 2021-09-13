package com.zebone.nhis.ex.nis.ns.service;

import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.bl.pub.service.IpCgPubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.vo.ZsLisCxInfo;
import com.zebone.nhis.ex.nis.pub.dao.ZsSmExMapper;
import com.zebone.nhis.ex.pub.vo.CnOrdAndAppVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.common.support.UserContext;

@Service
public class ZsOrdSmExService {

	@Resource
	private	ZsSmExMapper zsSmExMapper;

	@Autowired
	public IpCgPubService ipCgPubService;
	
	/**
	 * 根据条形码，获取lis相关记录
	 * @param paramMap
	 * @return
	 */
	public List<ZsLisCxInfo> getRisInfoFromLis(Map<String,Object> paramMap){
		List<ZsLisCxInfo> list = zsSmExMapper.nisGetZyInfoFromHis(paramMap);
		return list;
	}
	
	/**
	 * 根据条形码，获取对应执行单信息 -- 作废
	 * @param paramMap
	 * @return
	 */
	public List<ExlistPubVo> getCxExlistByOrdsn(Map<String,Object> paramMap){
		List<ExlistPubVo> list = zsSmExMapper.getExlistByOrdsn(paramMap);
		return list;
	}
	
	/**
	 * 根据条形码，更新lis库的相关记录
	 * @param barcode
	 * @return
	 */
	public int updateLisInfo(String barcode){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("barCode", barcode);
		paramMap.put("dateEx",new Date());
		paramMap.put("codeEmpEx", UserContext.getUser().getCodeEmp());
		int count = zsSmExMapper.nisSetGatherInfoToLis(paramMap);
		return count;
	}
	
	/**
	 * 根据执行单主键更新医嘱执行单相关状态 -- 作废
	 * @param ordsn
	 */
	public int updateNhisRisCxOcc(String pkExoccs) {
		Map<String, Object> upMap = new HashMap<String, Object>();
		upMap.put("dateOcc", new Date());
		upMap.put("pkOrg", UserContext.getUser().getPkOrg());
		upMap.put("pkDept", UserContext.getUser().getPkDept());
		upMap.put("pkEmp", UserContext.getUser().getPkEmp());
		upMap.put("nameEmp", UserContext.getUser().getNameEmp());
		if(CommonUtils.isEmptyString(pkExoccs)) return 0;
		String[] exoccs = pkExoccs.split(",");
		upMap.put("pkExoccs", exoccs);
		int count = zsSmExMapper.updateCxExlist(upMap);
		return count;
	}

	/**
	 * 根据条形码，获取执行单 + 申请单信息
	 * 2018-03-23 扫码执行调试后添加
	 * @param paramMap
	 * @return
	 */
	public List<CnOrdAndAppVo> getExAndAppByOrdsn(Map<String,Object> paramMap){
		List<CnOrdAndAppVo> list = zsSmExMapper.getExAndAppByOrdsn(paramMap);
		return list;
	}
	
}
