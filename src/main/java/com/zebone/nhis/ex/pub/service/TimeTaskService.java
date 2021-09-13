package com.zebone.nhis.ex.pub.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 定时任务抽象类
 * @author yangxue
 *
 */
public abstract class TimeTaskService {


		
	/**
	 * 执行方法(机构的获取方式待定)
	 * @throws BusException 
	 */
	public Map<String,String> exec(String pk_org) throws BusException{
		//获取病区
		List<Map<String,Object>> deptList = getNsDepts(pk_org);
		if(null == deptList || deptList.size() == 0){
			return null;
		}
		StringBuilder msg = new StringBuilder("");
		StringBuilder name_dept_str = new StringBuilder("");
		for(Map<String,Object> deptMap : deptList){
			String pk_dept = CommonUtils.getString(deptMap.get("pkDept"));
			//try{
				//执行
				String errMsg = execute(pk_org,pk_dept);
				if(null != errMsg && !"".equals(errMsg)){
					msg.append(errMsg);
				}
			//}catch(Exception e){//如果异常，记录科室日志
				//name_dept_str.append(CommonUtils.getString(deptMap.get("nameDept"))+",");
			//}
			
		}
		Map<String,String> result = new HashMap<String,String>();
		//组装报错信息
		if(msg != null && !msg.toString().equals("")){
			result.put("msg", "提示信息："+msg.toString());
		}
		if(name_dept_str != null && !name_dept_str.toString().equals("")){
			result.put("customStatus", name_dept_str.toString()+"以上科室执行失败！");
		}
		return result;		
	}
	
	/**
	 * 科室记费服务
	 * @param context
	 * @throws BusException 
	 */
	protected abstract String execute(String pk_org,String pk_dept) throws BusException;


	/**
	 * 患者根据病区分组
	 * @param deptList
	 * @param piList
	 * @return
	 */
	protected Map<String, List<Map<String, Object>>> groupPiList(
			List<Map<String, Object>> deptList, List<Map<String, Object>> piList) {
		Map<String,List<Map<String,Object>>> deptpimap = new HashMap<String,List<Map<String,Object>>>();
		for(Map<String,Object> deptMap : deptList){
			String pk_dept = CommonUtils.getString(deptMap.get("pkDept"));
			for(Map<String,Object> piMap : piList){
				Object piDept = piMap.get("pkDeptNs");
				List<Map<String,Object>> list = deptpimap.get(pk_dept);
				if(piDept != null && pk_dept.equals(CommonUtils.getString(piDept))){
					if(null == list){
						list = new ArrayList<Map<String,Object>>();
						list.add(piMap);
						deptpimap.put(pk_dept, list);
					}else{
						list.add(piMap);
					}
				}else{
					continue;
				}
			}
		}
		return deptpimap;
	}
	
	/**
	 * 获取病区集合
	 * @return
	 * @throws BusException 
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> getNsDepts(String pk_org) throws BusException{
		String sql = "select distinct a.pk_dept, a.name_dept from bd_ou_dept a  "+
				" inner join bd_dept_bus b on a.pk_dept = b.pk_dept and b.dt_depttype = '02'  and b.del_flag='0' "+
				" inner join bd_dept_bu c on c.pk_deptbu = b.pk_deptbu  and c.dt_butype = '01' "+
				" where  a.pk_org = ? ";
		List<Map<String,Object>> list = (List<Map<String, Object>>)DataBaseHelper.queryForList(sql, new Object[]{pk_org});
		return list;
	}
	
	/**
	 * 获取所有住院患者
	 * @param pkOrg
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> getAllPatiList(String pkOrg) throws BusException{
		return getPvList(pkOrg,null,null,null);
	}

	protected List<Map<String,Object>> getPvList(String pkOrg, String pkDept,String pkPi,String pkPv){
		StringBuilder sqlStr = new StringBuilder();
		List<Object> paramList = new ArrayList<>();
		paramList.add(pkOrg);
		if(StringUtils.isNotBlank(pkDept)){
			sqlStr.append(" and pv.pk_dept_ns = ? ");
			paramList.add(pkDept);
		}
		if(StringUtils.isNotBlank(pkPi)){
			sqlStr.append(" and pv.pk_pi = ? ");
			paramList.add(pkPi);
		}
		if(StringUtils.isNotBlank(pkPv)){
			sqlStr.append(" and pv.pk_pv = ? ");
			paramList.add(pkPv);
		}
		String sql = "select pv.bed_no, pv.name_pi, pv.pk_pv, pv.date_admit, pv.pk_dept_ns,pv.pk_pi "+
				" from pv_encounter pv where pv.pk_org = ? "+ sqlStr.toString()+
				" and pv.flag_in = '1' order by pv.pk_dept_ns, pv.bed_no ";
		return DataBaseHelper.queryForList(sql, paramList.toArray());
	}
}
