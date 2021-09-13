package com.zebone.nhis.ex.nis.qry.service;

import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.fee.dao.BlCgIpQueryMapper;
import com.zebone.nhis.ex.nis.qry.dao.ExListQueryMapper;
import com.zebone.nhis.ex.nis.qry.dao.OrderQueryMapper;
import com.zebone.nhis.ex.pub.support.SortByOrdMapUtil;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 医嘱联查服务
 * @author yangxue
 *
 */
@Service
public class QueryOrderService {
	@Resource
	private OrderQueryMapper ordQueryMapper;
	@Resource
	private ExListQueryMapper exlistQueryMapper;
	@Resource
	private BlCgIpQueryMapper blCgIpQueryMapper;
	
     /**
      * 根据患者查询医嘱列表
      * @param param{pkPv,nameOrd,euAlways,ordtype,durgType}
      * @param user
      * @return
      */
	 public List<Map<String,Object>> queryOrdByPv(String param,IUser user){
		 
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 
		 map.put("curDateBegin", DateUtils.getDateStr(new Date())+"000000");
		 map.put("curDateEnd", DateUtils.getDateStr(new Date())+"235959");
		 
		 //String pk_dept_cur = ((User)user).getPkDept();
		 //map.put("pkDeptNs", pk_dept_cur);
		 
		 //2018-1-2 添加医嘱状态的查询条件

		 if(map.get("status") != null && !CommonUtils.isEmptyString(map.get("status").toString())){
			 String[] status = map.get("status").toString().split(",");
			 List<String> euStatus = new ArrayList<String>();
			 for(String str : status){
				 euStatus.add(str.trim());
			 }
			 map.put("euStatus", euStatus);
		 }
		 List<Map<String,Object>> ordlist = ordQueryMapper.queryOrdByCon(map);
		 if(ordlist!=null&&ordlist.size()>0){   		 
    		 new SortByOrdMapUtil().ordGroup(ordlist);
    	 }
		 return ordlist;
	 }
	 /**
	  * 根据医嘱主键查询执行单列表
	  * @param param{pkOrd}
	  * @param user
	  * @return
	  */
	 public List<Map<String,Object>> queryExListByOrd(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 return exlistQueryMapper.queryExlistByCon(map);
	 }
	 /**
	  * 根据医嘱主键计费明细列表
	  * @param param{pkOrd}
	  * @param user
	  * @return
	  */
	 public List<Map<String,Object>> queryBlCgIpDetailsByOrd(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 List<Map<String,Object>> mapList =  blCgIpQueryMapper.queryBlCgIpDetailsByOrd(map);
		 List<Map<String,Object>> mapListRes = new ArrayList<>();
		 //将退费记录放到收费记录之后，原有的保持按收费时间排序
		 for(Map<String,Object> ma : mapList){
		 	//如果是收费的直接放到list里面
		 	if(StringUtils.isBlank(MapUtils.getString(ma,"pkCgipBack"))){
				mapListRes.add(ma);
			}
		 	//在判断该收费有没有退费记录，如果有放到该条后面
			 for(Map<String,Object> m : mapList){
			 	//有可能一条计费对应多条退费
			 	if(StringUtils.isNotBlank(MapUtils.getString(m,"pkCgipBack"))&&
						MapUtils.getString(ma,"pkCgip").equals(MapUtils.getString(m,"pkCgipBack"))){
					mapListRes.add(m);
				}
			 }

		 }
		 return mapListRes;
	 }
	
	 /**
      * 根据科室查询医嘱列表
      * @param param{nameOrd,euAlways,ordtype,durgType}
      * @param user
      * @return
      */
	 public List<Map<String,Object>> queryOrdByDept(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 map.put("curDateBegin", DateUtils.getDateStr(new Date())+"000000");
		 map.put("curDateEnd", DateUtils.getDateStr(new Date())+"235959");
		 
		 if(map.get("status") != null && !CommonUtils.isEmptyString(map.get("status").toString())){
			 String[] status = map.get("status").toString().split(",");
			 List<String> euStatus = new ArrayList<String>();
			 for (String str : status) {
				 euStatus.add(str.trim());
			 }
			 map.put("euStatus", euStatus);
		 }
		 
		 if(map.get("nameOrd") != null && !CommonUtils.isEmptyString(map.get("nameOrd").toString())){
			 String[] nameOrds = map.get("nameOrd").toString().split("，");
			 List<String> ords = new ArrayList<String>();
			 for (String str : nameOrds) {
				 ords.add(str.trim());
			 }
			 map.put("nameOrds", ords);
		 }
		 
		 if(map.get("spcode") != null && !CommonUtils.isEmptyString(map.get("spcode").toString())){
			 String[] spcodes = map.get("spcode").toString().split(",");
			 List<String> sp = new ArrayList<String>();
			 for (String str : spcodes) {
				 sp.add(str.trim());
			 }
			 map.put("spcodes", sp);
		 }
		 map.remove("nameOrd");
		 map.remove("status");
		 map.remove("spcode");
		 List<Map<String,Object>> ordlist = ordQueryMapper.queryOrdByDept(map);
		 if(ordlist!=null&&ordlist.size()>0){   		 
    		 new SortByOrdMapUtil().ordGroup(ordlist);
    	 }
		 return ordlist;
	 }
	 
	 /**
      * 更新计划执行时间
      * @param param{pkExocc,datePlan}
      * @param user
      * @return
      */
	 public void updateDatePlan(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 if(null == map)
			 throw new BusException("未获取到待更新执行时间的相关入参!");
		 if(null == map.get("pkExocc") || CommonUtils.isEmptyString(map.get("pkExocc").toString()))
			 throw new BusException("未获取到待更新执行时间的【执行单主键】!");
		 if(null == map.get("datePlan") || CommonUtils.isEmptyString(map.get("datePlan").toString()))
			 throw new BusException("未获取到待更新执行时间的【执行时间】!");
		 map.put("ts", new Date());
		 int cnt = DataBaseHelper.update("update ex_order_occ set date_plan=to_date('"+map.get("datePlan").toString()+"','yyyyMMddHH24Miss') "
		 		+ ",ts=:ts where pk_exocc=:pkExocc and del_flag='0' and eu_status='0' ", map);
		 if(1 != cnt)
			 throw new BusException("更新执行时间失败!");
	 }
	 
	 /**
      * 更新执行科室
      * @param param{pkCnord,pkDeptEx}
      * @param user
      * @return
      */
	 public void updateDeptEx(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 if(null == map)
			 throw new BusException("未获取到待更新执行科室的相关入参!");
		 if(null == map.get("ordsn") || CommonUtils.isEmptyString(map.get("ordsn").toString()))
			 throw new BusException("未获取到待更新执行科室的医嘱号!");
		 if(null == map.get("pkDeptExecNew") || CommonUtils.isEmptyString(map.get("pkDeptExecNew").toString()))
			 throw new BusException("未获取到待更新执行科室的执行科室主键!");
		 String pkCnord = map.get("pkCnord").toString();
//		 String ordsnParent = map.get("ordsnParent").toString();
		 String pkDeptExOld = map.get("pkDeptExec").toString();
		 String pkDeptEx = map.get("pkDeptExecNew").toString();
		 
		 //1、校验是否 已作废、已请领、已执行
		 int cnt = DataBaseHelper.queryForScalar("select count(1) from cn_order ord"
				 + " left join ex_order_occ occ on occ.pk_cnord = ord.pk_cnord and occ.del_flag = '0'"
				 + " left join ex_pd_apply_detail dt on dt.pk_cnord = ord.pk_cnord"
				 + " where (ord.flag_erase = '1' or occ.eu_status = '1' or dt.pk_cnord is not null)"
				 + " and ord.pk_cnord = ? ", Integer.class ,new Object[]{pkCnord});//【仅处理单条】
//				 + " and ordsn_parent = ? ", Integer.class ,new Object[]{ordsnParent});//【可处理同组】
		 if(cnt > 0)
			 throw new BusException("所选医嘱非原始数据，不可修改执行科室!");
		 
		 //2、校验执行单的科室  =？ 医嘱的执行科室
		 cnt = DataBaseHelper.queryForScalar("select count(1) from cn_order ord"
				 + " left join ex_order_occ occ on occ.pk_cnord = ord.pk_cnord and occ.del_flag = '0'"
				 + " where occ.pk_dept_occ <> ord.pk_dept_exec "
				 + " and ord.pk_cnord = ? ", Integer.class ,new Object[]{pkCnord});//【仅处理单条】
//				 + " and ordsn_parent = ? ", Integer.class ,new Object[]{ordsnParent});//【可处理同组】
		 if(cnt > 0)
			 throw new BusException("执行科室动态生成，不能修改!");
		 
		 //3、同组的执行科室不一致也不予以修改[仅处理单条的，同组不作判断]
//		 cnt = DataBaseHelper.queryForScalar("select count(distinct ord.pk_dept_exec) from cn_order ord where ordsn_parent = ? ", Integer.class ,new Object[]{ordsnParent});
//		 if(cnt > 0)
//			 throw new BusException("同组的执行科室不一致，不能修改!");
		 
		 int cntOrd = DataBaseHelper.update("update cn_order set pk_dept_exec = ? ,ts= ?"
				+ " where pk_cnord = ? and pk_dept_exec = ? and del_flag='0' and flag_erase ='0' ", new Object[]{ pkDeptEx,new Date(),pkCnord,pkDeptExOld});//【仅处理单条 - 支持护嘱】
//		 		+ " where pk_cnord = ? and pk_dept_exec = ? and del_flag='0' and flag_erase ='0' and flag_doctor = '1' ", new Object[]{ pkDeptEx,new Date(),pkCnord,pkDeptExOld});//【仅处理单条】
//		 		+ " where ordsn_parent = ? and del_flag='0' and flag_erase ='0' and flag_doctor = '1' ", new Object[]{ pkDeptEx,new Date(),ordsnParent});//【可处理同组】
		 if(cntOrd < 1)
			 throw new BusException("更新医嘱的执行科室失败!");
		 
		 DataBaseHelper.update("update ex_order_occ set pk_dept_occ = ? ,ts= ? "
				 + " where pk_cnord in (select pk_cnord from cn_order ord where ord.pk_cnord = ? and ord.flag_erase = '0' and ord.del_flag = '0') "//【仅处理单条  - 支持护嘱】
//				 + " where pk_cnord in (select pk_cnord from cn_order ord where ord.flag_doctor = '1' and ord.pk_cnord = ? and ord.flag_erase = '0') "//【仅处理单条】
//				 + " where pk_cnord in (select pk_cnord from cn_order ord where ord.flag_doctor = '1' and ord.ordsn_parent = ? and ord.flag_erase = '0') "//【可处理同组】
				 + " and eu_status = '0' and del_flag = '0' and pk_dept_occ = ? ", new Object[]{ pkDeptEx,new Date(),pkCnord,pkDeptExOld});
	 }
	 
	 /**
	  * 根据就诊主键查询患者就诊相关信息
	  * @param param{pkPv}
	  * @param user
	  * @return
	  */
	 public List<Map<String,Object>> queryPiInfoByPv(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 return ordQueryMapper.queryPiInfoByPv(map);
	 }
}
