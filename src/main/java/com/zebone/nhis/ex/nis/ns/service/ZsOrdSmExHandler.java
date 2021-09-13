package com.zebone.nhis.ex.nis.ns.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.vo.ZsLisCxInfo;
import com.zebone.nhis.ex.nis.pub.service.ZsbaLisColAngCgService;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.CnOrdAndAppVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ZsOrdSmExHandler {
	
	@Resource
	private ZsOrdSmExService zsOrdSmExService;
	
	@Resource
	private ZsbaLisColAngCgService LisColAngCgByBaService;
	
	/**
	 * 根据条形码，获取相关检验采血记录
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ExlistPubVo> getNisZyInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null || paramMap.get("barCode") == null 
				|| CommonUtils.isEmptyString(paramMap.get("barCode").toString()))
			throw new BusException("未获取到待执行确认的条形码！");
		
		//1、【lis库】，定位医嘱号
		DataSourceRoute.putAppId("LIS_bayy");
		List<ZsLisCxInfo> lisInfos = zsOrdSmExService.getRisInfoFromLis(paramMap);
		if(lisInfos == null || lisInfos.size() < 1) 
			throw new BusException("未获取到与条形码对应的记录！");
		
		List<String> ordsns = new ArrayList<String>();
		for (ZsLisCxInfo zsLisCxInfo : lisInfos) {
			ordsns.add(zsLisCxInfo.getBcYzId());
		}
		if(ordsns.size() < 1)
			throw new BusException("未获取到与条形码对应的医嘱号！");
		paramMap.put("ordsns", ordsns);
		
		//2、【Nhis库】，查询相关执行单
		DataSourceRoute.putAppId("default");
		List<ExlistPubVo> list = zsOrdSmExService.getCxExlistByOrdsn(paramMap);
		new ExListSortByOrdUtil().ordGroup(list);
		return list;
	}
	
	/**
	 * 更新检查采血的执行单
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void updateNisZyInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null || paramMap.get("barCode") == null 
				|| CommonUtils.isEmptyString(paramMap.get("barCode").toString()))
			throw new BusException("未获取到待执行确认的条形码！");
		
		if(paramMap.get("pkExoccs") == null 
				|| CommonUtils.isEmptyString(paramMap.get("pkExoccs").toString()))
			throw new BusException("未获取到待执行确认的执行单！");
		
		//1、【lis】库，更新lis的存储过程
		DataSourceRoute.putAppId("LIS_bayy");
		String barCode = paramMap.get("barCode").toString();
		zsOrdSmExService.updateLisInfo(barCode);//根据条形码，更新lis记录
		
		//2、【Nhis】库，根据执行单主键，更新相关执行单
		DataSourceRoute.putAppId("default");
		String pkExOccs = paramMap.get("pkExoccs").toString();
		zsOrdSmExService.updateNhisRisCxOcc(pkExOccs);//根据执行单主键更新Nhis记录
	}
	
	/**
	 * 更新 lis库采集时间、采集人，Nhis 库申请单中的采集人、采集时间、采集科室
	 * 返回当前条码对应的申请单，执行单信息
	 * 2018-03-23 扫码执行调试后添加
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnOrdAndAppVo> updateNisZyInfoRtnEx(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null || paramMap.get("barCode") == null 
				|| CommonUtils.isEmptyString(paramMap.get("barCode").toString()))
			throw new BusException("未获取到待执行确认的条形码！");
		
		User u = (User)user;
		
		DataSourceRoute.putAppId("LIS_bayy");
		//1.1 【lis库】，定位医嘱号
		List<ZsLisCxInfo> lisInfos = zsOrdSmExService.getRisInfoFromLis(paramMap);
		if(lisInfos == null || lisInfos.size() < 1)
			throw new BusException("未获取到与条形码对应的记录！");

		List<String> ordsns = new ArrayList<String>();
		for (ZsLisCxInfo zsLisCxInfo : lisInfos) {
			ordsns.add(zsLisCxInfo.getBcYzId());
		}
		if(ordsns.size() < 1)
			throw new BusException("未获取到与条形码对应的医嘱号！");

		//1.2 【lis库】,更新lis的标本接收人以及接收时间
		String barCode = paramMap.get("barCode").toString();
		zsOrdSmExService.updateLisInfo(barCode);//根据条形码，更新lis记录
		
		DataSourceRoute.putAppId("default");
		//2.2 【Nhis库】，根据医嘱号，更新相关申请单的采集人信息
		LisColAngCgByBaService.updateNhisRisApp(barCode,u);//根据执行单主键更新Nhis记录
		//2.1【Nhis库】，查询相关执行单 + 申请单相关信息
		paramMap.put("ordsns", ordsns);
		List<CnOrdAndAppVo> list = zsOrdSmExService.getExAndAppByOrdsn(paramMap);
		if( null != list && list.size() > 0)
			new ExListSortByOrdUtil().ordGroup(list);
		return list;
	}

	/**
	 *取消采集
	 */
	public List<CnOrdAndAppVo> CancelCollection(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		User u = (User)user;
        String sampNo = paramMap.get("barCode").toString();
        if(paramMap == null || paramMap.get("barCode") == null || CommonUtils.isEmptyString(sampNo))
			throw new BusException("未获取到待取消执行的条形码！");

        DataSourceRoute.putAppId("LIS_bayy");
        //1.1 【lis库】，定位医嘱号
        List<ZsLisCxInfo> lisInfos = zsOrdSmExService.getRisInfoFromLis(paramMap);
        if(lisInfos == null || lisInfos.size() < 1){
            throw new BusException("lis 系统中 未获取到与条形码对应的记录！");
        }
        List<String> ordsns = new ArrayList<String>();
        for (ZsLisCxInfo zsLisCxInfo : lisInfos) {
            ordsns.add(zsLisCxInfo.getBcYzId());
        }
        if(ordsns.size() < 1){
            throw new BusException("lis 系统中 未获取到与条形码对应的医嘱号！");
        }

        //【Nhis库】，执行取消采集操作【退费，还原申请单状态】
        DataSourceRoute.putAppId("default");
        int rtn = LisColAngCgByBaService.CancelCollection(sampNo,u);
        if (rtn <= 0){
			throw new BusException("条码号："+sampNo+" 取消采集失败！");
		};

		//【Nhis库】，查询相关执行单 + 申请单相关信息
		paramMap.put("ordsns", ordsns);
		List<CnOrdAndAppVo> list = zsOrdSmExService.getExAndAppByOrdsn(paramMap);
		if(null != list && list.size() > 0)
			new ExListSortByOrdUtil().ordGroup(list);
		return list;
	}
}
