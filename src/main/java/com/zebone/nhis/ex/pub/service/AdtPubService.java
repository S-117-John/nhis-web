package com.zebone.nhis.ex.pub.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.dao.AdtPubMapper;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * 出入院及出入科公共服务类
 * @author yangxue
 *
 */ 
@Service
public class AdtPubService {
    
	@Autowired
	private AdtPubMapper adtPubMapper;
	/***
     * 出院或转科需要校验的数据  
     * @param param{pkPv}
     * @return
     */
    public Map<String,Object> getDeptOutVerfyData(Map<String,Object> param,IUser u){
    	User user = (User)u;
    	
    	//针对ADT调用时，当前科室并不是患者的就诊病区，会导致查询不到患者的未处理的数据
    	//if(CommonUtils.isNull(param.get("pkDeptNs")))
    		//param.put("pkDeptNs",user.getPkDept());
    	param.put("pkDeptOcc",user.getPkDept());
    	
    	List<Map<String,Object>> ordData = adtPubMapper.queryOrdByPkPv(param);//未停未作废医嘱
    	List<Map<String,Object>> risData = adtPubMapper.queryRisByPkPv(param);//未执行执行单
    	List<Map<String,Object>> apData = adtPubMapper.queryPdapByPkPv(param);//未完全请领单
    	List<Map<String,Object>> opData = adtPubMapper.queryOpByPkPv(param);//未完成手术申请单
    	List<Map<String,Object>> packBedData = adtPubMapper.queryPacketBedByPv(param);//包床记录
    	List<ExlistPubVo> exListData = adtPubMapper.queryExlistByPv(param);//未执行执行单
    	//同组排序
    	 new ExListSortByOrdUtil().ordGroup(exListData);
    	List<Map<String,Object>> cpData = adtPubMapper.queryCpByPv(param);//未完成/退出在径记录;
    	List<Map<String,Object>> infData = adtPubMapper.queryInfByPv(param);//未退诊的同病区婴儿信息
    	
    	Map<String,Object> result = new HashMap<String,Object>();
    	if (!isEmpty(ordData)) {
    		result.put("ordData", ordData);
		}
    	if (!isEmpty(risData)) {
    		result.put("risData", risData);
		}
    	if (!isEmpty(apData)) {
    		result.put("apData", apData);
		}
    	if (!isEmpty(opData)) {
    		result.put("opData", opData);
		}
    	if (!isEmpty(packBedData)) {
    		result.put("packBedData", packBedData);
		}
    	if (!isEmpty(exListData)) {
    		result.put("exListData", exListData);
		}
    	if (!isEmpty(cpData)) {
    		result.put("cpData", cpData);
		}
    	if (!isEmpty(infData)) {
    		result.put("infData", infData);
		}
    	return result;
    }
    /**
     * 校验是否为空
     * @param obj
     * @return
     */
    private boolean isEmpty(Object obj){
    	if(obj == null ) return true;
    	return false;
    }
    
    /***
     * 出院或转科需要校验的数据  
     * 【2018-09-06】中山二院新需求添加出院日不可收费用核查
     * @param param{pkPv}
     * @return
     */
    public Map<String,Object> getDeptOutVerfyDataBySyx(Map<String,Object> param,IUser u){
    	User user = (User)u;
    	param.put("pkDeptOcc",user.getPkDept());
    	if(Application.isSqlServer()){
    		param.put("dbType", "sqlserver");
	   	 }else{
	   		param.put("dbType", "oracle");
	   	 }
    	if(null != param.get("dateEnd") && !CommonUtils.isEmptyString(param.get("dateEnd").toString()))
    		param.put("dateEndDay",param.get("dateEnd").toString().substring(0,8));
    	List<Map<String,Object>> ordData = adtPubMapper.queryOrdByPkPv(param);//未停未作废医嘱
		List<Map<String,Object>> ordReaseData = adtPubMapper.queryOrdReaseByPkPv(param);//作废未核对医嘱
		if(ordReaseData.size() > 0){
			ordData.addAll(ordReaseData);
		}
    	List<Map<String,Object>> risData = adtPubMapper.queryRisByPkPv(param);//未执行执行单
		if(param.get("isChkNs") != null && "1".equals(param.get("isChkNs"))){
			risData = adtPubMapper.queryRisByPkPvForBoAi(param);//未执行执行单以及计费未做执行单
			//根据医嘱附加属性BA001判断，如果BA001=3 申请单状态<3不允许出院；如果BA001=2  申请单状态<2不允许出院；其他 申请单状态<1不允许出院。（博爱）
			if(risData.size() > 0){
				List<Map<String, Object>> risDataNew = new ArrayList<>();
				for(Map<String, Object> map : risData){
					String BA001 = map.containsKey("valAttr") ? map.get("valAttr") != null ? map.get("valAttr").toString() :"" : "";
					String euStatus = map.containsKey("euStatusApply") ? map.get("euStatusApply") != null ? map.get("euStatusApply").toString() :"" : "";
					if(BA001 != null && euStatus != null && !"".equals(euStatus)){
						int status = Integer.parseInt(euStatus);
						if("3".equals(BA001) && status < 3){
							risDataNew.add(map);
						}else if("2".equals(BA001) && status < 2){
							risDataNew.add(map);
						}else if(!"3".equals(BA001) && !"2".equals(BA001) && status < 1){
							risDataNew.add(map);
						}
					}else{
						risDataNew.add(map);
					}
				}
				risData = risDataNew;
			}
		}
    	List<Map<String,Object>> apData = adtPubMapper.queryPdapByPkPv(param);//未完全请领单
    	List<Map<String,Object>> opData = adtPubMapper.queryOpByPkPv(param);//未完成手术申请单
    	List<Map<String,Object>> packBedData = adtPubMapper.queryPacketBedByPv(param);//包床记录
    	List<ExlistPubVo> exListData = adtPubMapper.queryExlistByPv(param);//未执行执行单
    	List<Map<String,Object>> hpCgChkData = new ArrayList<>();//出院日不可收取费用
		List<Map<String,Object>> nsCgChkData = new ArrayList<>();
		if(param.get("isDeptChg") != null && !"1".equals(param.get("isDeptChg"))){
			hpCgChkData = adtPubMapper.queryHpCgChkByPv(param);//出院日不可收取费用
		}
		if(param.get("isChkNs") != null && "1".equals(param.get("isChkNs"))){
			if(Application.isSqlServer()){
				nsCgChkData = adtPubMapper.queryNsCgChkByPv(param);//不符合医保规范的护理费用
			}else{
				nsCgChkData = adtPubMapper.queryNsCgChkByPvForOrcl(param);//不符合医保规范的护理费用
			}
			if(nsCgChkData.size() > 0){
				hpCgChkData.addAll(nsCgChkData);
			}
		}

    	List<Map<String,Object>> groupCgChkData = null;
    	if(Application.isSqlServer()){
    		groupCgChkData = adtPubMapper.queryGroupByPvForSql(param);//校验同组的收费项目不能超收
		}else{
			groupCgChkData = adtPubMapper.queryGroupCgChkByPv(param);//校验同组的收费项目不能超收
		}
    	//List<Map<String,Object>> groupCgChkData = adtPubMapper.queryGroupCgChkByPv(param);//校验同组的收费项目不能超收
    	//同组排序
    	 new ExListSortByOrdUtil().ordGroup(exListData);
    	List<Map<String,Object>> cpData = adtPubMapper.queryCpByPv(param);//未完成/退出在径记录;
    	List<Map<String,Object>> infData = adtPubMapper.queryInfByPv(param);//未退诊的同病区婴儿信息 --2020-04-09 处理bug-25689 不判断病区条件 
    	
    	Map<String,Object> result = new HashMap<String,Object>();
    	if (!isEmpty(ordData)) {
    		result.put("ordData", ordData);
		}
    	if (!isEmpty(risData)) {
    		result.put("risData", risData);
		}
    	if (!isEmpty(apData)) {
    		result.put("apData", apData);
		}
    	if (!isEmpty(opData)) {
    		result.put("opData", opData);
		}
    	if (!isEmpty(packBedData)) {
    		result.put("packBedData", packBedData);
		}
    	if (!isEmpty(exListData)) {
    		result.put("exListData", exListData);
		}
    	if (!isEmpty(cpData)) {
    		result.put("cpData", cpData);
		}
    	if (!isEmpty(infData)) {
    		result.put("infData", infData);
		}
    	if(!isEmpty(hpCgChkData)){
    		double amount  = 0.0 ;
    		for (Map<String, Object> map : hpCgChkData) {
    			amount += Double.parseDouble(map.get("amount").toString()) ;
    		}
    		if(amount > 0)
    			result.put("hpCgChkData", hpCgChkData);
    	}
    	if(!isEmpty(groupCgChkData)){
    		result.put("groupCgChkData", groupCgChkData);
    	}
    	return result;
    }
    
    /**
     * 删除今日收费的医保核查费用
     * @param param
     */
    public void delCgByHpChk(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkPv") == null ||"".equals(paramMap.get("pkPv").toString())){
			throw new BusException("处理医保核查费用的入参pkPv为空！");
		}
		
		if(Application.isSqlServer()){
			paramMap.put("dbType", "sqlserver");
	   	 }else{
	   		paramMap.put("dbType", "oracle");
	   	 }
			
    	List<Map<String,Object>> hpCgChkData = adtPubMapper.queryHpCgChkByPv(paramMap);//出院日不可收取费用
    	if(null == hpCgChkData || hpCgChkData.size() < 1) return;
    	
    	List<Object[]> delList = new ArrayList<Object[]>();
    	for (Map<String, Object> map : hpCgChkData) {
    		delList.add(new Object[]{map.get("pkCgip")});
    	}
//    	DataBaseHelper.getJdbcTemplate().batchUpdate("update bl_ip_dt set del_flag = '1'"
//    			+ " ,ts = to_date('"+DateUtils.getDateTimeStr(new Date())+"','yyyyMMddHH24MISS') "
//    			+ " where pk_cgip= ? and del_flag = '0' and flag_settle = '0' ", delList);
    }
	/**
	 * 判断是否存在费用发生日期大于出院日期的费用明细
	 * @param pkPv
	 * @param endDate
	 * @return
	 */
	public Map<String,String> countBlIpDtAfterDateEnd(String pkPv, Date endDate){
		return adtPubMapper.countBlIpDtAfterDateEnd(pkPv, DateUtils.formatDate(endDate,"yyyy-MM-dd HH:mm:ss"));
	}

}
