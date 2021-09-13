package com.zebone.nhis.ex.nis.qry.service;

import java.lang.reflect.Array;
import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ex.nis.qry.dao.PdApplyQueryMapper;
import com.zebone.nhis.scm.ipdedrug.vo.IpDeDrugDto;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病区领药查询
 * @author yangxue
 *
 */
@Service
public class QueryPdApplyService {
	@Resource
	private PdApplyQueryMapper pdApplyQueryMapper;
    /**
     * 根据病区查询请领单列表
     * @param param{pkDeptNs,dateEnd,dateBegin}
     * @param user
     * @return
     */
	public List<Map<String,Object>> queryPdAppByDept(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0,8)+"235959");
		}
		return pdApplyQueryMapper.queryPdApply(map);
	}

	/**
	 * 查询统计请领单、退药单
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPdAppByDeptCount(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0,8)+"235959");
		}
		List<Map<String,Object>> list = pdApplyQueryMapper.queryPdApplyCount(map);
		//对list按，CODE_DE，NAME_DECATE，EU_DIRECT，并得出没合并前的条数
		int lssueCount=0,returnCount=0;
		Map<String,Map<String,Object>> mapNew = new HashMap<>();
		for(Map<String,Object> vo:list){
			String key = MapUtils.getString(vo,"codeDe")+MapUtils.getString(vo,"nameDecate")+MapUtils.getString(vo,"euDirect");
			if(!mapNew.containsKey(key)){
				if("1".equals(MapUtils.getString(vo,"euDirect"))){
					lssueCount++;
				}else{
					returnCount++;
				}
				mapNew.put(key, vo);
			}
		}
		List<Map<String,Object>> result = new ArrayList<>();
		for(Map.Entry<String, Map<String,Object>> entry : mapNew.entrySet()){
			Map<String,Object> mapValue = entry.getValue();
			if("1".equals(MapUtils.getString(mapValue,"euDirect"))){
				mapValue.put("lssueCount",lssueCount);
			}else{
				mapValue.put("returnCount",returnCount);
			}
			result.add(mapValue);
		}
		return result;
	}
	/**
	 * 根据请领单查询请领明细
	 * @param param{pkPdap,flagDe,flagPivas,pdname，euAlways}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPdApDetail(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list =  pdApplyQueryMapper.queryPdApDetail(map);
		//if(list!=null&&list.size()>0){
			//new SortByOrdMapUtil().ordGroup(list);
		//}
		return list;
	}
	/**
	 * 领药综合查询
	 * @param param{namePi,codePv,dateBegin,dateEnd,flagPivas,pdname，euAlways,euAptype}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPdApDeTogether(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) map = new HashMap<String,Object>();
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		String dateBegin = CommonUtils.getString(map.get("dateBegin"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
		if(dateBegin!=null&&!dateBegin.equals("")){
			map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		}
		map.put("pkDeptNs", ((User)user).getPkDept());
		List<Map<String,Object>> list = pdApplyQueryMapper.queryPdApDeTogether(map);
		//if(list!=null&&list.size()>0){
		//	new SortByOrdMapUtil().ordGroup(list);
		//}
		return list;
	}
	
	/**
	 * 取消请领
	 * @param param{选中行所有数据}
	 * @param user
	 */
	public void cancelApply(String param,IUser user){
		Map<String,Object> map= JsonUtil.readValue(param, Map.class);
		if(map == null) throw new BusException("未获取到需要取消的请领单信息！");
		String direct = CommonUtils.getString(map.get("euDirect"));
		User u = (User)user;
		map.put("dateCanc", new Date());
		//校验请领单是否发药
//		String sql = "select pk_pdap from ex_pd_apply where (flag_cancel = '1' or date_de is not null ) and pk_pdap = :pkPdap  ";
//		Map<String,Object> result = DataBaseHelper.queryForMap(sql, map);
//		if(result!=null&&!CommonUtils.isEmptyString(CommonUtils.getString(result.get("pkPdap")))){
//			throw new BusException("该请领单已经发药或取消，不允许取消!");
//		}
		if("1".equals(direct)){//请领，更新医嘱执行单数据
			StringBuilder dtAp = new StringBuilder("update ex_order_occ set pk_pdapdt = null,ts=:dateCanc   ");
			dtAp.append("where pk_pdapdt in (select dt.pk_pdapdt from ex_pd_apply_detail dt ");
			dtAp.append("inner join ex_pd_apply ap on dt.pk_pdap = ap.pk_pdap ");
			dtAp.append("where ap.pk_pdap = :pkPdap) ");
			DataBaseHelper.update(dtAp.toString(), map);	
		}else{//退请领，还原执行单
			StringBuilder dtAp = new StringBuilder("update ex_order_occ set pk_pdback = null,flag_canc='0',eu_status=(CASE WHEN pk_emp_occ='' or pk_emp_occ is  null  THEN '0' ELSE '1' end) ,pk_dept_canc = null,");
			dtAp.append(" pk_emp_canc=null,name_emp_canc=null,date_canc=null, ts=:dateCanc  ");
			dtAp.append("where pk_pdback in (select dt.pk_pdapdt from ex_pd_apply_detail dt ");
			dtAp.append("inner join ex_pd_apply ap on dt.pk_pdap = ap.pk_pdap ");
			dtAp.append("where ap.pk_pdap = :pkPdap) and pk_exevent!=:pkPdap");
			DataBaseHelper.update(dtAp.toString(), map);	
			//将停嘱退药产生的退请领单只更新pk_pdback字段
			DataBaseHelper.update("update ex_order_occ set pk_pdback = null where pk_exevent=:pkPdap", map);
		}
		//执行更新请领状态
		StringBuilder upAp = new StringBuilder("update ex_pd_apply set flag_cancel = '1' , pk_emp_cancel = :pkEmp, ");
		upAp.append("name_emp_cancel = :nameEmp , pk_dept_cancel = :pkDept , date_cancel = :dateCanc ,");
		upAp.append("eu_status = '9',ts=:dateCanc  where pk_pdap = :pkPdap and eu_status = '0' ");
		upAp.append(" and not exists (select 1 from ex_pd_apply ap "
				+ "inner join ex_pd_apply_detail dt on dt.pk_pdap = ap.pk_pdap where ap.pk_pdap = :pkPdap and dt.flag_finish = '1' )");
		map.put("pkEmp", u.getPkEmp());
		map.put("nameEmp", u.getNameEmp());
		map.put("pkDept", u.getPkDept());
		int cnt = DataBaseHelper.update(upAp.toString(), map);
		//校验请领单是否发药
		if(cnt != 1)
			throw new BusException("该请领单已经发药或取消，不允许取消!");
		
		//更新请领单明细状态
		map.put("reason", "病区取消");
		StringBuilder updateSql = new StringBuilder("update ex_pd_apply_detail set flag_stop = '1' , reason_stop = :reason,");
		updateSql.append("pk_emp_stop = :pkEmp, name_emp_stop = :nameEmp,ts=:dateCanc  ");
		updateSql.append("where pk_pdap = :pkPdap and flag_de = '0' and flag_stop = '0'");
		int cnt_dt = DataBaseHelper.update(updateSql.toString(), map);
		if(cnt_dt < 1) 
			throw new BusException("该请领单明细未取消成功，取消申请单失败!");
	}
	
	/**
	 * 取消请领明细
	 * @param param
	 * @param user
	 */
	public void cancelApplyDt(String param,IUser user){
		List<String> list= JsonUtil.readValue(param, ArrayList.class);
		if(list == null || list.size() <= 0) throw new BusException("未获取到需要取消领药的信息！");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		//更新前校验
		String valSql = "select pk_pdapdt from ex_pd_apply_detail  where pk_pdapdt in (:pks) and (flag_de = '1' or flag_stop = '1')";
		paramMap.put("pks", list);
		List<Map<String,Object>> result = (List<Map<String,Object>>) DataBaseHelper.queryForList(valSql,paramMap);
		if(null != result && result.size() > 0){
			throw new BusException("选中请领单明细已经执行，不能停止！");
		}
		User u = (User)user;
		//更新请领单明细
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("pkDept", u.getPkDept());
		paramMap.put("dateCanc", new Date());
		paramMap.put("reason", "药房停发");
		String updateSql = "update ex_pd_apply_detail set flag_stop = '1' , reason_stop = :reason,pk_emp_stop = :pkEmp, name_emp_stop = :nameEmp,ts=:dateCanc  where pk_pdapdt in (:pks)";
		DataBaseHelper.update(updateSql, paramMap);
		//更新请领的结束标志
		/*sql注释：当处理的请领明细对应的请领单中没有flag_finish,flag_stop全部为'0'的数据把flag_finish设置为'1'
		 * not exists判断flag_de,flag_stop,exists判断获取当前选择的请领*/
		StringBuilder upApSql = new StringBuilder("update ex_pd_apply  set ex_pd_apply.flag_finish = '1', ex_pd_apply.eu_status = '9',  ");
		upApSql.append("ex_pd_apply.flag_cancel = '1' , ex_pd_apply.pk_emp_cancel = :pkEmp, " );
		upApSql.append("ex_pd_apply.name_emp_cancel = :nameEmp , ex_pd_apply.pk_dept_cancel = :pkDept , ex_pd_apply.date_cancel = :dateCanc,"); 
		upApSql.append("ex_pd_apply.ts = :dateCanc where eu_status = '0' and not exists (select dt.pk_pdap ");
		upApSql.append(" from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap = dt.pk_pdap and dt.flag_stop!='1') ");
		upApSql.append(" and  exists (select dt.pk_pdap ");
		upApSql.append(" from ex_pd_apply_detail dt where dt.pk_pdapdt in (:pks) and ex_pd_apply.pk_pdap = dt.pk_pdap )");
		DataBaseHelper.update(upApSql.toString(), paramMap);
		//更新医嘱服务数据
		StringBuilder dtAp = new StringBuilder("update ex_order_occ set pk_pdapdt = null,ts=:dateCanc ");
		dtAp.append("where pk_pdapdt in (select dt.pk_pdapdt from ex_pd_apply_detail dt ");
		dtAp.append("where dt.pk_pdapdt in (:pks) and dt.flag_de !='1' and dt.eu_direct = 1 )");
		DataBaseHelper.update(dtAp.toString(), paramMap);
	
	}
	
	 /**
     * 查询停发药品
     * @return
     */
   public List<Map<String,Object>> queryPdApStopDe(String param,IUser user){
	   Map<String,Object> paramMap  = JsonUtil.readValue(param, Map.class);
	   //未获取到请领科室，默认查询当前科室
	   if(paramMap==null||CommonUtils.isNull(paramMap.get("pkDeptAp"))){
		   paramMap = new HashMap<String,Object>();
		   paramMap.put("pkDeptAp", ((User)user).getPkDept());
	   }
	   return pdApplyQueryMapper.queryPdApStopDe(paramMap);
   }
	
   /**
	 * 更新打印状态
	 * @param param{选中行所有数据}
	 * @param user
	 */
	public void updateAppPrint(String param,IUser user){
		Map<String,Object> map= JsonUtil.readValue(param, Map.class);
		if(map == null || null == map.get("pkPdap")) 
			throw new BusException("未获取到需要更新打印状态的请领单信息！");
		
		//执行更新请领状态
		StringBuilder upAp = new StringBuilder("update ex_pd_apply set eu_print = '1' where pk_pdap = ? and (eu_print = '0' or eu_print is null) ");
		DataBaseHelper.update(upAp.toString(), new Object[]{ map.get("pkPdap")});
	}
	
	/**
     * 005002006031
     * 病区领药查询的取消退药服务
     *
     * @param param
     * @param user
     */
    public void cancelCollarDrug(String param, IUser user) {
        List<ExPdApplyDetail> exPdapdts = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {
        });
        if (exPdapdts == null) return;
        Set<String> pkPdapdtSet = new HashSet<String>();
        Set<String> pkPdapSet = new HashSet<String>();
        for (ExPdApplyDetail expdapdt : exPdapdts) {
            pkPdapdtSet.add(expdapdt.getPkPdapdt());
            pkPdapSet.add(expdapdt.getPkPdap());
        }
        String euDirect = exPdapdts.get(0).getEuDirect();
        User nuser = (User) user;
        String pkPdapdts = CommonUtils.convertSetToSqlInPart(pkPdapdtSet, "pk_pdapdt");
        //1.更新请领明细中flag_cancel,pk_emp_back,name_emp_back,date_bace,note
        StringBuffer upapdtSql = new StringBuffer("update ex_pd_apply_detail set flag_canc=1 ,");
        upapdtSql.append(" pk_emp_back=?,name_emp_back=? ,date_back=? ,note='药房退回' ");
        upapdtSql.append(" where pk_pdapdt in (");
        upapdtSql.append(pkPdapdts);
        upapdtSql.append(") and (flag_canc='0' or flag_canc is null) and flag_de='0' and flag_stop='0'");
        int count = DataBaseHelper.update(upapdtSql.toString(), new Object[]{nuser.getPkEmp(), nuser.getNameEmp(), new Date()});
        if (count != pkPdapdtSet.size()) {
            throw new BusException("您本次提交的药品发放明细中已有被其他人处理的记录，请刷新请领单后重新处理！");
        }
        //2.更新执行单，去除请领标志
        StringBuffer upExSql = new StringBuffer("update ex_order_occ ");
        if("-1".equals(euDirect)) {
        	upExSql.append(" set pk_pdback=null");
            upExSql.append(" where exists (select 1 from ex_pd_apply_detail dt");
            upExSql.append(" where dt.flag_de='0') and pk_pdback in (");
            upExSql.append(pkPdapdts);
            upExSql.append(")");
        }else{
        	upExSql.append(" set pk_pdapdt=null");
            upExSql.append(" where exists (select 1 from ex_pd_apply_detail dt");
            upExSql.append(" where dt.flag_de='0') and pk_pdapdt in (");
            upExSql.append(pkPdapdts);
            upExSql.append(")");
        }
        DataBaseHelper.update(upExSql.toString());
        String pkPdaps = CommonUtils.convertSetToSqlInPart(pkPdapSet, "pk_pdap");
        //3.更新请领单--如果请领明细全部被退回，同时更新请领单为取消状态(不包含已经发药的)
        StringBuffer upPddts = new StringBuffer("update ex_pd_apply  set ex_pd_apply.flag_cancel='1',ex_pd_apply.eu_status='9' ");
        upPddts.append(" ,ex_pd_apply.pk_emp_cancel=? ,ex_pd_apply.name_emp_cancel=?,ex_pd_apply.date_cancel=? ");
        upPddts.append(" where not exists (select 1 from ex_pd_apply_detail dt ");
        upPddts.append(" where ex_pd_apply.pk_pdap=dt.pk_pdap and (dt.flag_canc='0' or dt.flag_canc is null ))");
        upPddts.append(" and not exists (select 1 from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap=dt.pk_pdap  and dt.flag_finish='1'  )");
        upPddts.append(" and ex_pd_apply.pk_pdap in (");
        upPddts.append(pkPdaps);
        upPddts.append(")");
        DataBaseHelper.update(upPddts.toString(), new Object[]{nuser.getPkEmp(), nuser.getNameEmp(), new Date()});

        //4.更新请领单-如果请领单对应的明细即包含已经完成发药，或者退回处理的将请领单置为完成状态
        StringBuffer upPdaps = new StringBuffer("update ex_pd_apply   set ex_pd_apply.flag_finish='1',ex_pd_apply.eu_status='1' where ex_pd_apply.pk_pdap in (");
        upPdaps.append(pkPdaps);
        upPdaps.append(") and exists (select 1 from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap=dt.pk_pdap and dt.flag_finish='1') ");
        upPdaps.append(" and not exists (select 1 from ex_pd_apply_detail dt1  where ex_pd_apply.pk_pdap=dt1.pk_pdap and dt1.flag_finish='0' and (dt1.flag_canc='0' or dt1.flag_canc is null) and dt1.flag_stop='0') ");
        DataBaseHelper.update(upPdaps.toString());
    }
	
}
