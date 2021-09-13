package com.zebone.nhis.ex.nis.ns.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.transcode.SysApplog;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.dao.OrderMapper;
import com.zebone.nhis.ex.nis.ns.support.OrderCheckSortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OrderCheckService {
	
	@Resource
	private OrderMapper orderMapper ;
	
	
    /**
     * 根据患者获取待核对医嘱列表
     * @param param{dateStopEnd,pkPvs,euAlways:0:长期，1：临时，9：全部,flagN:新开new,新停stop,其他other}
     * @param user
     * @return
     * @throws ParseException 
     */
    public List<OrderCheckVo> queryOrderCheckList(String param, IUser user) throws ParseException{
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 Map<String,Object> newMap = new HashMap<String,Object>();
    	 List<String> pvlist = (List)paramMap.get("pkPvs");
    	 if(pvlist == null || pvlist.size()<=0)
    		 throw new BusException("未获取到患者就诊信息！");
    	 newMap.put("pkPvs", pvlist);
    	 String pk_dept_cur = ((User)user).getPkDept();
    	 newMap.put("pkDeptNs", pk_dept_cur);
    	 if(Application.isSqlServer()){
    		 newMap.put("dbType", "sqlserver");
    	 }else{
    		 newMap.put("dbType", "oracle");
    	 }
    	 if(paramMap.get("flagN")!=null){
    		 newMap.put("flagN", paramMap.get("flagN"));
    	 }
    	 if(paramMap.get("euAlways")!=null&&!paramMap.get("euAlways").equals("9")){
    		newMap.put("euAlways", paramMap.get("euAlways"));
    	 }
    	 if(paramMap.get("ordtype")!=null && !CommonUtils.isEmptyString(paramMap.get("ordtype").toString())){
     		newMap.put("ordtype", paramMap.get("ordtype"));
     	 }
    	 if(paramMap.get("flagEmer")!=null && !CommonUtils.isEmptyString(paramMap.get("flagEmer").toString())){
    		 newMap.put("flagEmer", paramMap.get("flagEmer"));
    	 }
		if(paramMap.get("ordtypeRange")!=null && !CommonUtils.isEmptyString(paramMap.get("ordtypeRange").toString())){
			newMap.put("ordtypeRange", paramMap.get("ordtypeRange"));
		}
    	 String dateStopEnd  = ExSysParamUtil.getCheckStopEndTime();
    	 newMap.put("dateStopEnd", dateStopEnd);
    	 //排序方式
    	 String ordOrder = ExSysParamUtil.getOrdByOrder();
    	 newMap.put("ordOrder", ordOrder);

		newMap.put("curDateBegin", DateUtils.getDateStr(new Date())+"000000");
		newMap.put("curDateEnd", DateUtils.getDateStr(new Date())+"235959");
    	 
    	 List<OrderCheckVo> ordlist = new ArrayList<OrderCheckVo>();
 		 ordlist = orderMapper.queryOrderCheckList(newMap);
    	 if(ordlist!=null&&ordlist.size()>0){
    		 new OrderCheckSortByOrdUtil().ordGroup(ordlist);
    	 }
 		 return  ordlist;
    }
    /**
     * 根据患者获取已核对医嘱列表
     * @param param{pkPvs,euAlways:0:长期，1：临时，9：全部}
     * @param user
     * @return
     * @throws ParseException 
     */
    public List<OrderCheckVo> queryOrderEcList(String param, IUser user) throws ParseException{
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 Map<String,Object> newMap = new HashMap<String,Object>();
    	 List<String> pvlist = (List)paramMap.get("pkPvs");
    	 if(pvlist == null || pvlist.size()<=0)
    		 throw new BusException("未获取到患者就诊信息！");
    	 newMap.put("pkPvs", pvlist);
    	 String pk_dept_cur = ((User)user).getPkDept();
    	 newMap.put("pkDeptNs", pk_dept_cur);
    	 if(paramMap.get("euAlways")!=null&&!paramMap.get("euAlways").equals("9")){
    		newMap.put("euAlways", paramMap.get("euAlways"));
    	 }
    	 if(paramMap.get("flagN")!=null){
    		 newMap.put("flagN", paramMap.get("flagN"));
    	 }
    	 if(CommonUtils.isNotNull(paramMap.get("chkDate"))){
    		 newMap.put("chkDate",CommonUtils.getString(paramMap.get("chkDate")).substring(0, 8));
    	 }
    	 if(CommonUtils.isNotNull(paramMap.get("chkStopDate"))){
    		 newMap.put("chkStopDate",CommonUtils.getString(paramMap.get("chkStopDate")).substring(0, 8));
    	 }
    	 if(paramMap.get("ordtype")!=null && !CommonUtils.isEmptyString(paramMap.get("ordtype").toString())){
      		newMap.put("ordtype", paramMap.get("ordtype"));
      	 }
		if(paramMap.get("ordtypeRange")!=null && !CommonUtils.isEmptyString(paramMap.get("ordtypeRange").toString())){
			newMap.put("ordtypeRange", paramMap.get("ordtypeRange"));
		}
    	 if(Application.isSqlServer()){
    		 newMap.put("dbType", "sqlserver");
    	 }else{
    		 newMap.put("dbType", "oracle");
    	 }
    	 if(paramMap.get("flagEmer")!=null && !CommonUtils.isEmptyString(paramMap.get("flagEmer").toString())){
    		 newMap.put("flagEmer", paramMap.get("flagEmer"));
    	 }
    	 
    	 if(newMap.get("flagN") !=null && "new".equals(CommonUtils.getString(newMap.get("flagN")))){
    		 newMap.put("statusOrd", " in ('2','3') ");
    		 newMap.put("chkDate", null);
    	 }if(newMap.get("flagN") !=null && "cancel".equals(CommonUtils.getString(newMap.get("flagN")))){
    		 newMap.put("statusOrd", " in ('9') ");
    		 newMap.put("chkDate", null);
    	 }else{
    		 newMap.put("statusOrd", " >= '2' ");
    	 }
		newMap.put("curDateBegin", DateUtils.getDateStr(new Date())+"000000");
		newMap.put("curDateEnd", DateUtils.getDateStr(new Date())+"235959");
    	 //排序方式
    	 String ordOrder = ExSysParamUtil.getOrdByOrder();
    	 newMap.put("ordOrder", ordOrder);

    	 List<OrderCheckVo> ordlist = new ArrayList<OrderCheckVo>();
  		 ordlist = orderMapper.queryOrderEcList(newMap);
    	 if(ordlist!=null&&ordlist.size()>0){   		 
    		 new OrderCheckSortByOrdUtil().ordGroup(ordlist);
    	 }
    	return ordlist;
    }
    /**
     * 保存用法、末次
     * @param param
     * @param user
     */
    public void saveSupply(String param,IUser user){
    	List<OrderCheckVo> checkList = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
    	if(checkList != null && checkList.size()>0){
    		for(OrderCheckVo ordvo:checkList){
    		    String querySql = "select * from cn_order where pk_cnord = ?";
    		    CnOrder cnOrder = DataBaseHelper.queryForBean(querySql,CnOrder.class,ordvo.getPkCnord());
    			DataBaseHelper.update("update cn_order set code_supply =:codeSupply where pk_cnord =:pkCnord and code_supply != :codeSupply ", ordvo);
				DataBaseHelper.update("update cn_order set last_num  = :lastNum where pk_cnord =:pkCnord", ordvo);
				if(cnOrder != null){
                    querySql = "select * from bd_supply where code = ?";
                    BdSupply supply = DataBaseHelper.queryForBean(querySql,BdSupply.class,cnOrder.getCodeSupply());
                    if(supply != null){
                        SysApplog log = new SysApplog();
						User u = (User)user;
						log.setPkOrg(u.getPkOrg());
						log.setDateOp(new Date());
						log.setPkEmpOp(u.getPkEmp());
						log.setNameEmpOp(u.getNameEmp());
						log.setEuButype("5");
						log.setEuOptype("0");
						log.setPkObj(ordvo.getPkCnord());
						log.setObjname("cn_order");
						log.setContent("用法修改");
						log.setDelFlag("0");
						log.setNote(supply.getName()+"====>"+ordvo.getNameUsage());
						if(!supply.getCode().equals(ordvo.getCodeSupply())){
							DataBaseHelper.insertBean(log);
						}
                    }
                }
    		}
    	}
    }
    /**
     * 设置执行时间
     * @param param{orderlist,dateEx,val}
     * @param user
     */
    public void setOrdExPlanTime(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	if(paramMap == null||paramMap.get("orderlist")==null)
    		throw new BusException("未获取到要设置时间的医嘱列表！");
    	User u = (User)user;
    	StringBuilder update_sql = new StringBuilder("update cn_order set modifier=?,modity_time=?,ts=?");
    	//设置的分钟大于0，按开始时间计算计划执行时间
    	if(paramMap.get("val")!=null&&!"".equals(paramMap.get("val").toString())&&CommonUtils.getInteger(paramMap.get("val").toString())>0){
    		if(Application.isSqlServer()){
    			update_sql.append(",date_plan_ex = dateadd(mi,"+CommonUtils.getInteger(paramMap.get("val"))+",date_start) ");
    					
    		}else{//默认按oracle数据库
    			update_sql.append(",date_plan_ex = date_start+numtodsinterval("+CommonUtils.getInteger(paramMap.get("val"))+",'minute') ");
    		}
    	}else{//按执行时间设置计划执行时间
    		update_sql.append(",date_plan_ex = to_date('"+CommonUtils.getString(paramMap.get("dateEx"))+"','YYYYMMDDHH24MISS') ");
    	}
    	//设置临时执行人 - 主键
    	if(null != paramMap.get("pkEmp") && !CommonUtils.isEmptyString(paramMap.get("pkEmp").toString()))
    		update_sql.append(" ,pk_emp_ex ='"+paramMap.get("pkEmp").toString()+"'");
    	
    	//设置临时执行人 - 名称
    	if(null != paramMap.get("nameEmp") && !CommonUtils.isEmptyString(paramMap.get("pkEmp").toString()))
    		update_sql.append(" ,name_emp_ex ='"+paramMap.get("nameEmp").toString()+"'");
    	
    	update_sql.append(" where pk_cnord in ("+paramMap.get("orderlist")+")");
    	DataBaseHelper.execute(update_sql.toString(), new Object[]{u.getPkEmp(),new Date(),new Date()});
    	
    	//临时医嘱执行时间 --EX0073 修改临嘱执行时间是否同步修改执行单执行时间 1是 0否
    	String modifyOrderDateOcc = ApplicationUtils.getSysparam("EX0073",false);
    	if("1".equals(modifyOrderDateOcc)){
    		StringBuilder updateOccSql = new StringBuilder("update ex_order_occ set modifier=?,modity_time=?,ts=?");
        	//设置的分钟大于0，按开始时间计算计划执行时间
        	if(paramMap.get("val")!=null&&!"".equals(paramMap.get("val").toString())&&CommonUtils.getInteger(paramMap.get("val").toString())>0){
        		if(Application.isSqlServer()){
        			updateOccSql.append(",date_occ = dateadd(mi,"+CommonUtils.getInteger(paramMap.get("val"))+",date_start) ");
        					
        		}else{//默认按oracle数据库
        			updateOccSql.append(",date_occ = date_start+numtodsinterval("+CommonUtils.getInteger(paramMap.get("val"))+",'minute') ");
        		}
        	}else{//按执行时间设置计划执行时间
        		updateOccSql.append(",date_occ = to_date('"+CommonUtils.getString(paramMap.get("dateEx"))+"','YYYYMMDDHH24MISS') ");
        	}
        	//设置临时执行人 - 主键
        	if(null != paramMap.get("pkEmp") && !CommonUtils.isEmptyString(paramMap.get("pkEmp").toString()))
        		updateOccSql.append(" ,pk_emp_occ ='"+paramMap.get("pkEmp").toString()+"'");
        	
        	//设置临时执行人 - 名称
        	if(null != paramMap.get("nameEmp") && !CommonUtils.isEmptyString(paramMap.get("pkEmp").toString()))
        		updateOccSql.append(" ,name_emp_occ ='"+paramMap.get("nameEmp").toString()+"'");
        	
        	updateOccSql.append(" where pk_cnord in ("+paramMap.get("orderlist")+")");
        	DataBaseHelper.execute(updateOccSql.toString(), new Object[]{u.getPkEmp(),new Date(),new Date()});
    	}
    	
    }
    
    /**
     * 根据主医嘱获取护嘱列表
     * @param param{ordSnParent}
     * @param user
     * @return
     */
    public List<OrderCheckVo> queryOrderCheckNsList(String param, IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	return orderMapper.queryOrderNsList(paramMap);
    }
    /**
     * 医嘱核对--更新医嘱状态
     * param{checkList}
     */
  public void updateOrdState(List<String> clist,List<String> stoplist,List<String> eralist,User u,int total){
	  int cnt = 0;
    	//核对
    	if(clist != null && clist.size()>0) {
	    	Map<String,Object> map = new HashMap<String,Object>();
	    	//map.put("parentNos", clist);
	    	map.put("pkCnords", clist);
	    	map.put("pkEmpChk", u.getPkEmp());
	    	map.put("nameEmpChk",u.getNameEmp());
	    	map.put("dateChk", new Date());
	    	map.put("euStatus", "2");
	    	map.put("whereSql", " and eu_status_ord = 1 ");
	    	cnt = cnt + orderMapper.updateOrdInfo(map);
	    	//更新各类申请单状态
	    	DataBaseHelper.update(getUpdateSql("cn_consult_apply"),map);//2018-12-21 增加会诊的申请单处理
	    	DataBaseHelper.update(getUpdateSql("cn_trans_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_op_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_pa_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_ris_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_lab_apply"),map);
    	}
    	//停止核对
    	if(stoplist != null && stoplist.size()>0) {
	    	Map<String,Object> stopmap = new HashMap<String,Object>();
	    	stopmap.put("pkCnords", stoplist);
	    	stopmap.put("dateStopChk",new Date());
	    	stopmap.put("pkEmpStopChk", u.getPkEmp());
	    	stopmap.put("nameEmpStopChk", u.getNameEmp());
	    	stopmap.put("flagStopChk","1");
	    	stopmap.put("euStatus", "4");
	    	stopmap.put("whereSql", " and eu_status_ord >= 1 ");
	    	//先把护理医嘱的停止时间和停止人置上
	    	updateNsStopInfo(stoplist,stopmap);
	    	cnt = cnt + orderMapper.updateOrdInfo(stopmap);
    	}
    	//作废核对
    	if(eralist != null && eralist.size()>0) {
	    	Map<String,Object> eramap = new HashMap<String,Object>();
	    	eramap.put("pkCnords", eralist);
	    	eramap.put("dateEraseChk",new Date());
	    	eramap.put("pkEmpEraseChk", u.getPkEmp());
	    	eramap.put("nameEraseChk", u.getNameEmp());
	    	eramap.put("flagEraseChk","1");
	    	eramap.put("euStatus", "9");
	    	eramap.put("whereSql", " and eu_status_ord > 1 ");
	    	cnt = cnt + orderMapper.updateOrdInfo(eramap);
    	}
    	if(cnt<total)
    		throw new BusException("您提交的医嘱部分已经发生了变更，请刷新后重新提交！");
    	
    }
  /**
   * 医嘱核对--更新医嘱状态--校验时间戳
   * param{checkList}
   */
public void updateOrdStateByTs(List<OrderCheckVo> clist,List<String> pkclist,List<OrderCheckVo> stoplist,List<String> pkstoplist,List<OrderCheckVo> eralist,User u,int total,Set<String> dietPkPvs){
	int cnt = 0;
	String dateTs = "";
  	//核对
  	if(clist != null && clist.size()>0) {
	    	Map<String,Object> map = new HashMap<String,Object>();
	    	//map.put("parentNos", clist);
	    	map.put("pkCnords", pkclist);
	    	map.put("pkEmpChk", u.getPkEmp());
	    	map.put("nameEmpChk",u.getNameEmp());
	    	map.put("dateChk", new Date());
	    	map.put("euStatus", "2");
	    	map.put("whereSql", " and eu_status_ord = 1 ");
	    	for(OrderCheckVo vo:clist){
	    		dateTs = DateUtils.getDateTimeStr(vo.getTs());
	    		cnt = cnt + DataBaseHelper.update(this.generateUpdateSql("2", u,vo),vo);
	    	}
	    	
	    	//更新各类申请单状态
	    	DataBaseHelper.update(getUpdateSql("cn_consult_apply"),map);//2018-12-21 增加会诊的申请单处理
	    	DataBaseHelper.update(getUpdateSql("cn_trans_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_op_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_pa_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_ris_apply"),map);
	    	DataBaseHelper.update(getUpdateSql("cn_lab_apply"),map);
  	}
  	//停止核对
  	if(stoplist != null && stoplist.size()>0) {
	    	Map<String,Object> stopmap = new HashMap<String,Object>();
	    	stopmap.put("pkCnords", pkstoplist);
	    	stopmap.put("dateStopChk",new Date());
	    	stopmap.put("pkEmpStopChk", u.getPkEmp());
	    	stopmap.put("nameEmpStopChk", u.getNameEmp());
	    	stopmap.put("flagStopChk","1");
	    	stopmap.put("euStatus", "4");
	    	stopmap.put("whereSql", " and eu_status_ord >= 1 ");
	    	//先把护理医嘱的停止时间和停止人置上
	    	updateNsStopInfo(pkstoplist,stopmap);
	    	for(OrderCheckVo vo:stoplist){
	    		cnt = cnt + DataBaseHelper.update(this.generateUpdateSql("4", u,vo),vo);
	    	}
  	}
  	//作废核对
  	if(eralist != null && eralist.size()>0) {
  		for(OrderCheckVo vo:eralist){
    		cnt = cnt + DataBaseHelper.update(this.generateUpdateSql("9", u,vo),vo);
    	}
  	}
  	//更新患者饮食医嘱等级
  	if(dietPkPvs != null && dietPkPvs.size()>0){
  		//获取患者饮食医嘱等级
		StringBuilder sql = new StringBuilder("");
    	sql.append(" select ord.pk_pv,max(VAL_ATTR) AS dt_dietary from BD_DICTATTR t ");
    	sql.append(" inner join  CN_ORDER ord on ord.PK_ORD=t.PK_DICT ");
    	sql.append(" where t.CODE_ATTR ='0201' and ord.CODE_ORDTYPE='13' ");
    	sql.append(" and ord.EU_STATUS_ORD in('2','3','4') and ord.pk_pv in (");
    	sql.append(CommonUtils.convertSetToSqlInPart(dietPkPvs, "pk_pv"));
    	sql.append(") and t.DEL_FLAG='0' group by ord.pk_pv "); 
		List<Map<String, Object>> dietaryListMap = DataBaseHelper.queryForList(sql.toString());

		//更新患者饮食等级
		String updateSql = "update pv_ip set dt_dietary=:dtDietary where pk_pv =:pkPv";
		DataBaseHelper.batchUpdate(updateSql, dietaryListMap);	
  	}

  	if(cnt<total)
  		throw new BusException("您提交的医嘱部分已经发生了变更，请刷新后重新提交！");
  }
    /**
     * 获取更新申请单状态sql
     * @param tableName
     * @param sortno_parent
     * @return
     */
    private String getUpdateSql(String tableName){
		return "update "+tableName+"  set eu_status = 1 where pk_cnord  in (:pkCnords) ";
	}
    /**
     * 更新护理医嘱的停止时间
     * @param stoplist
     * @param stopmap
     */
    private void updateNsStopInfo(List<String> stoplist,Map<String,Object> stopmap){
    	StringBuilder sql = new StringBuilder("update cn_order  set ");
    	sql.append(" cn_order.date_stop=(select dr.date_stop from cn_order dr where cn_order.ordsn_parent=dr.ordsn and dr.flag_stop='1')");
    	sql.append(",cn_order.flag_stop='1' ,cn_order.pk_emp_stop = :pkEmpStopChk,cn_order.name_emp_stop = :nameEmpStopChk ");
    	sql.append(" where cn_order.pk_cnord in (:pkCnords) and cn_order.flag_doctor = '0' "); 
        DataBaseHelper.update(sql.toString(), stopmap);
    }
    /**
     * 更新护理医嘱为停止状态
     * @param pk_cnord
     * @param dateStop
     * @param stopmap
     */
    public void updateOrdNsStop(String pk_cnord,Date dateStop,Map<String,Object> stopmap){
    	if(CommonUtils.isEmptyString(pk_cnord)||dateStop==null) return;
    	stopmap.put("pkCnord", pk_cnord);
    	stopmap.put("dateStop", dateStop);
    	stopmap.put("ts", new Date());
    	StringBuilder sql = new StringBuilder("update cn_order  set ts=:ts,date_stop = :dateStop,flag_stop='1',pk_emp_stop = :pkEmp,name_emp_stop = :nameEmp");
    	sql.append(" where pk_cnord = :pkCnord and flag_doctor = '0' "); 
        DataBaseHelper.update(sql.toString(), stopmap);
    }
  
    /**
     * 查询医嘱对应的收费项目
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryFeeByOrd(String param, IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	if(Application.isSqlServer()){
    		paramMap.put("dbType", "sqlserver");
   	 	}else{
   	 		paramMap.put("dbType", "oracle");
   	 	}
    	list = orderMapper.queryOrdFeeByPk(paramMap);
    	return list;
    }

    /**
     * 查询医嘱对应的草药明细
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPresDtByOrd(String param, IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	list = orderMapper.queryPresDtByOrd(paramMap);
    	return list;
    }
    
    /**
     * 更新医嘱的静配标志
     * @param pk_cnord
     * @param dateStop
     * @param stopmap
     */
    public List<OrderCheckVo> updateOrdFlagPivas(String param, IUser user){
    	List<OrderCheckVo> pivasList = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
    	if(null == pivasList || pivasList.size() < 1)
    		throw new BusException("未获取到待更新静配标志的医嘱，请重新选择！");
    	for(OrderCheckVo ordvo:pivasList){
    		Date ts = new Date();
			int cnt = DataBaseHelper.update("update cn_order set flag_pivas =:flagPivas"
					+ ",ts= to_date('"+DateUtils.getDateTimeStr(ts)+"','YYYYMMDDHH24MISS')  "
					+ " where pk_cnord =:pkCnord and ts=:ts and flag_pivas != :flagPivas ", ordvo);
			if(cnt != 1)
				throw new BusException("当前选中数据已经不是最新，请刷新再作修改！");
			ordvo.setTs(ts);
		}
    	return pivasList;
    }
    
    /**
     * 医嘱核对时生成医嘱状态更新语句
     * @param status
     * @param u
     * @param checkOrdVo
     * @return
     */
    private String generateUpdateSql(String status,User u,OrderCheckVo vo){
 	   StringBuilder sql = new StringBuilder("	update cn_order set eu_status_ord ='").append(status).append("'");
 	   String curTime = DateUtils.getDateTimeStr(new Date());
 	   String dateTs = DateUtils.getDateTimeStr(vo.getTs());
 	   sql.append(",ts=to_date('"+curTime+"','YYYYMMDDHH24MISS')");
 	   if("2".equals(status)){//核对
 		   sql.append(",pk_emp_chk='").append(u.getPkEmp()).append("'");
 		   sql.append(",name_emp_chk='").append(u.getNameEmp()).append("'");
 		   sql.append(",date_chk=to_date('"+curTime+"','YYYYMMDDHH24MISS')");
 		   sql.append(",modifier='").append(u.getPkEmp()).append("'");
 		   sql.append(" where eu_status_ord = '1' ");
 	   }else if("4".equals(status)){//停止
 		   sql.append(",pk_emp_stop_chk='").append(u.getPkEmp()).append("'");
 		   sql.append(",name_emp_stop_chk='").append(u.getNameEmp()).append("'");
 		   sql.append(",date_stop_chk=to_date('"+curTime+"','YYYYMMDDHH24MISS')");
 		   sql.append(",flag_stop_chk='1'");
 		   sql.append(",modifier='").append(u.getPkEmp()).append("'");
 		   sql.append(" where eu_status_ord >= 1  ");
 	   }else if("9".equals(status)){
 		   sql.append(",pk_emp_erase_chk='").append(u.getPkEmp()).append("'");
 		   sql.append(",name_erase_chk='").append(u.getNameEmp()).append("'");
 		   sql.append(",date_erase_chk=to_date('"+curTime+"','YYYYMMDDHH24MISS')");
 		   sql.append(",flag_erase_chk='1'");
 		   sql.append(",modifier='").append(u.getPkEmp()).append("'");
 		   sql.append(" where eu_status_ord > 1  ");
 	   }
 	   sql.append(" and to_char(ts,'YYYYMMDDHH24MISS')='"+dateTs+"'");
 	   sql.append(" and pk_cnord=:pkCnord");
 	   return sql.toString();
    }
    
    /**
   * 根据医嘱写入CA记录
   * @param cnOrds
   */
  public void caRecrdByOrd(List<OrderCheckVo> cnOrds) {
    if(cnOrds.get(0).getCnSignCa()!=null){
       List<CnSignCa> cnSignCa = new ArrayList<CnSignCa>();
       Map<Integer, String> ordsnM = new HashMap<Integer,String>();
       for(CnOrder order : cnOrds){
         CnSignCa signCa = order.getCnSignCa();
         signCa.setPkSignca(NHISUUID.getKeyId());
         signCa.setEuButype("0");//医嘱
         signCa.setPkBu(StringUtils.isBlank(order.getPkCnord()) ? ordsnM.get(order.getOrdsn()) :order.getPkCnord());
         signCa.setCreator(UserContext.getUser().getPkEmp());
         signCa.setTs(new Date());
         signCa.setDelFlag("0");
         signCa.setCreateTime(signCa.getTs());
         signCa.setPkOrg(UserContext.getUser().getPkOrg());
         cnSignCa.add(signCa);
       }
       if(cnSignCa.size()>0){
         DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnSignCa.class), cnSignCa);
       } 
     }
  }
  
  /**
	 * 查询用法附加费
	 * @param param
	 * @param user
	 * @return
	 */
  public List<Map<String,Object>> querySupplyItem(String param, IUser user){
  	 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
  	 List<Map<String,Object>> mapList = new ArrayList<>();
  	 if(map==null){
		 return null;
	 }
  	 if(!CommonUtils.isNull(map.get("pkCnorder"))){
		 mapList = orderMapper.queryLisItemByPkCnorder(map);
	 }
  	 if(!CommonUtils.isNull(map.get("codeSupply"))){
		 map.put("euPvtype", "3");
		 List<Map<String,Object>> recList = orderMapper.querySupplyItemByCode(map);
		 mapList.addAll(recList);
	 }
	 return mapList;
  }


	/**
	 * 查询医嘱的用法修改记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SysApplog> querySupplyLogList(String param, IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<SysApplog> sysApplogList = new ArrayList<>();
		if(map==null){
			return null;
		}
		if(!CommonUtils.isNull(map.get("pkCnord"))){
			String pkCnord = map.get("pkCnord").toString().trim();
			String querySql = "select * from sys_applog where pk_obj = ?";
			sysApplogList = DataBaseHelper.queryForList(querySql,SysApplog.class,pkCnord);
		}
		return sysApplogList;
	}
 
    
}
