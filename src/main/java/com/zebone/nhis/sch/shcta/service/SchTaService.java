package com.zebone.nhis.sch.shcta.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxinmy.weixin4j.util.StringUtil;
import com.zebone.nhis.common.module.sch.schta.SchTa;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.sch.shcta.dao.SchTaMapper;
import com.zebone.nhis.sch.shcta.vo.SchTaAutoCreateParam;
import com.zebone.nhis.sch.shcta.vo.SchTaEmp;
import com.zebone.nhis.sch.shcta.vo.SchTaQryParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SchTaService {
	
	@Autowired
	private SchTaMapper schTaDao;
    /**
     * 根据条件获取实习生排班情况
     * @param param
     * @param user
     * @return
     */
	public List<Map<String,Object>> GetSchTaByParam(String param,IUser user){
		SchTaQryParam qryPa = JsonUtil.readValue(param, SchTaQryParam.class);
		Map<String,Object> paM = new HashMap<String,Object>();
		String str=qryPa.getCodeEmp();
		if(StringUtils.isNotBlank(str)){
			paM.put("codeEmp", str);
		}
		str = qryPa.getPkEmp();
		if(StringUtils.isNotBlank(str)){
			paM.put("pkEmp", str);
		}
		str = qryPa.getNameEmp();
		if(StringUtils.isNotBlank(str)){
			paM.put("nameEmp", str);
		}
		str = qryPa.getPkDept();
		if(StringUtils.isNotBlank(str)){
			paM.put("pkDept", str);
		}
		if(qryPa.getDateBeginHead()!=null){
			
			paM.put("dateBeginHead", DateUtils.dateToStr("yyyy-MM-dd", qryPa.getDateBeginHead())+" 00:00:00");
		}
		if(qryPa.getDateBeginTail()!=null){
			paM.put("dateBeginTail", DateUtils.dateToStr("yyyy-MM-dd", qryPa.getDateBeginTail())+" 23:59:59");
		}
		if(qryPa.getDateEndHead()!=null){
			paM.put("dateEndHead", DateUtils.dateToStr("yyyy-MM-dd", qryPa.getDateEndHead())+" 00:00:00");
		}
		if(qryPa.getDateEndTail()!=null){
			paM.put("dateEndTail", DateUtils.dateToStr("yyyy-MM-dd", qryPa.getDateEndTail())+" 23:59:59");
		}
        return schTaDao.qrySchTaByParam(paM);
		
	}
	/**
	 * 保存实习生排班情况
	 * @param param
	 * @param user
	 */
	public List<String> SaveSchTa(String param,IUser user){	 
		List<SchTa> schTaList = JsonUtil.readValue(param, new TypeReference<List<SchTa>>(){});
		if(schTaList==null || schTaList.size()<=0) return null;
		List<String> allNoSchTas = new ArrayList<String>();
		User u = (User)user;
		for(SchTa ta : schTaList){
			if(StringUtils.isBlank(ta.getPkSchta())){
				if(vaildDeptSch(u.getPkOrg(),ta,allNoSchTas)){
					ta.setPkOrg(u.getPkOrg());
					ta.setDtTaschtype("01");//见习医生排班
					ta.setDateSch(new Date());
					ta.setPkEmpSch(u.getPkEmp());
					ta.setNameEmpSch(u.getNameEmp());
					DataBaseHelper.insertBean(ta);
				}
			}else if("1".equals(ta.getDelFlag())){
				DataBaseHelper.execute("delete from sch_ta where pk_schta=?", new Object[]{ta.getPkSchta()});
			}else {
				if(vaildDeptSch(u.getPkOrg(),ta,allNoSchTas)){
					DataBaseHelper.updateBeanByPk(ta, false);					
				}
			}
		}
		return allNoSchTas;
	}
	private boolean vaildDeptSch(String pkOrg,SchTa ta,List<String> allNoSchTas) {
		 List<String> taDepts = new ArrayList<String>();
		 taDepts.add(ta.getPkDept());
		 List<Map<String,Object>> deptSch = schTaDao.qryDeptSchTa(pkOrg, DateUtils.dateToStr("yyyy-MM-dd", ta.getDateBegin())+" 00:00:00", DateUtils.dateToStr("yyyy-MM-dd", ta.getDateEnd())+" 23:59:59", taDepts);
		 boolean isHavaDept=false;
		 for(Map<String,Object> map : deptSch){
			 Integer arguval = map.get("arguval")==null?0:Integer.parseInt(map.get("arguval").toString());
			 if(arguval==0) throw new  BusException(map.get("nameDept").toString()+" 没有设置实习人数上限！");
			 Integer empCount = map.get("empCount")==null?0:Integer.parseInt(map.get("empCount").toString());
			 if(empCount<arguval){ //排班人次<科室上限
				  ta.setPkDept(map.get("pkDept").toString());
		          isHavaDept = true;
		          break;  
	         }
		 }
		 if(!isHavaDept) { 
			 putNoSchTa(ta.getNameEmp(),ta.getDateBegin(),ta.getDateEnd(),allNoSchTas);
			 return false;
		 }
		return true;
	}
	/**
	 *  删除实习生排班情况
	 * @param param
	 * @param user
	 * @return
	 */
	public void DelSchTa(String param,IUser user){	 
		Map<String,Object> pM = JsonUtil.readValue(param, Map.class);
		Object pkEmp = pM.get("pkEmp");
		if(pkEmp==null) throw new BusException("删除失败!");
		DataBaseHelper.execute("delete from sch_ta where pk_emp=? ", new Object[]{pkEmp.toString()});
	}
	/**
	 * 查询待排班人员
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> QrySchTaEmp(String param,IUser user) throws Exception{
		Map<String,Object> m = JsonUtil.readValue(param, Map.class);
		Map<String,Object> p = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(m.get("dtEmpsrvtype").toString())){
			p.put("dtEmpsrvtype", m.get("dtEmpsrvtype"));
		}
		if(StringUtil.isNotBlank(m.get("dateBegin").toString())){
			Date dateBegin = DateUtils.getDefaultDateFormat().parse((String)m.get("dateBegin"));
			p.put("dateBeginHead", DateUtils.dateToStr("yyyy-MM-dd",dateBegin)+" 00:00:00");
			p.put("dateBeginTail", DateUtils.dateToStr("yyyy-MM-dd",dateBegin)+" 23:59:59");
			p.put("dateBeginQry", "dateBeginQry");
		}
		if(StringUtil.isNotBlank(m.get("dateEnd").toString())){
			Date dateEnd = DateUtils.getDefaultDateFormat().parse((String)m.get("dateEnd"));
			p.put("dateEndHead", DateUtils.dateToStr("yyyy-MM-dd",dateEnd)+" 00:00:00");
			p.put("dateEndTail", DateUtils.dateToStr("yyyy-MM-dd",dateEnd)+" 23:59:59");
			p.put("dateEndQry", "dateEndQry");
		}
		
        User u = (User)user;
        String pkDept = u.getPkDept();
        String dateEnd = DateUtils.dateToStr("yyyy-MM-dd", new Date())+" 23:59:59";
        p.put("pkDept", pkDept);
        p.put("dateEnd", dateEnd);
        
        return schTaDao.qrySchTaEmp(p);
	}
	/**
	 *  查询可排班科室
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> QrySchTaDept(String param,IUser user){
		User u = (User)user;
		String pkOrg = u.getPkOrg();
		return schTaDao.qrySchTaDept(pkOrg);
		
	}
	/**
	 * 自动排班处理
	 * @param param
	 * @param user
	 * @return
	 */
	public List<String> AutoCreateSchTa(String param,IUser user){
		
		SchTaAutoCreateParam taP = JsonUtil.readValue(param, SchTaAutoCreateParam.class);
		List<SchTaEmp> taEmps = taP.getTaEmps();
		List<String> taDepts = taP.getTaDeps();
		if(taEmps==null||taEmps.size()<=0) throw new BusException("请选择待排班实习生！");
		if(taDepts==null || taDepts.size()<=0) throw new BusException("请选择待轮转科室！");
		User u = (User) user;
		List<SchTa> allSchTas  = new ArrayList<SchTa>(); //排班记录
		List<String> allNoSchTas = new ArrayList<String>();
		Map<String,Integer> pkDeptAsUse = new HashMap<String,Integer>();
		for(SchTaEmp emp : taEmps){
			emp.setPkOrg(u.getPkOrg());
			List<SchTa> schTas = createSchTas(emp,u); //已排班
			if(schTas==null){ //固定科室		
				fixDeptSch(allSchTas,emp,taDepts,allNoSchTas,u,pkDeptAsUse);
			}else{ //轮转科室
				changeDeptSch(schTas,emp,taDepts,allNoSchTas,pkDeptAsUse);
				allSchTas.addAll(schTas);
			}
		}
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SchTa.class), allSchTas);  
        return allNoSchTas;
	}

	private void fixDeptSch(List<SchTa> schTas, SchTaEmp emp,
			List<String> taDepts, List<String> allNoSchTas,User u,Map<String,Integer> pkDeptAsUse) {
		 List<Map<String,Object>> deptSch = schTaDao.qryDeptSchTa(emp.getPkOrg(), DateUtils.dateToStr("yyyy-MM-dd", emp.getDateBegin())+" 00:00:00", DateUtils.dateToStr("yyyy-MM-dd", emp.getDateEnd())+" 23:59:59", taDepts);
		 boolean isHavaDept=false;
		 SchTa schTa = new SchTa();
		 for(Map<String,Object> map : deptSch){
			 Integer arguval = map.get("arguval")==null?0:Integer.parseInt(map.get("arguval").toString());
			 if(arguval==0) throw new  BusException(map.get("nameDept").toString()+" 没有设置实习人数上限！");
			 Integer empCount = map.get("empCount")==null?0:Integer.parseInt(map.get("empCount").toString());
			 if(empCount<arguval && vaildDeptLimit(arguval,pkDeptAsUse,map.get("pkDept").toString())){ //排班人次<科室上限
				  schTa.setPkDept(map.get("pkDept").toString());
				  pkDeptAsUse.put(schTa.getPkDept(), (pkDeptAsUse.get(schTa.getPkDept())==null?0:pkDeptAsUse.get(schTa.getPkDept()))+1);
		          isHavaDept = true;
		          break;  
	         }
		 }
		 if(!isHavaDept) { 
			 putNoSchTa(emp.getNameEmp(),emp.getDateBegin(),emp.getDateEnd(),allNoSchTas);
		 }else{
			    setSchTa(schTa,emp,u);
			    schTa.setDateBegin(emp.getDateBegin());
			    schTa.setDateEnd(emp.getDateEnd());
			    schTas.add(schTa);
		 }
	}

	private boolean vaildDeptLimit(Integer arguval,
			Map<String, Integer> pkDeptAsUse, String pkDept) {
		Integer c = pkDeptAsUse.get(pkDept);
		if(c!=null){
			if(c>=arguval) return false;
		}
		return true;
	}
	private void setSchTa(SchTa sch,SchTaEmp emp, User u) {
		sch.setPkSchta(NHISUUID.getKeyId());
		sch.setPkEmp(emp.getPkEmp());
		sch.setNameEmp(emp.getNameEmp());
		sch.setPkOrg(u.getPkOrg());
		sch.setDtTaschtype("01");//见习医生排班
		sch.setDateSch(new Date());
		sch.setPkEmpSch(u.getPkEmp());
		sch.setNameEmpSch(u.getNameEmp());	
		sch.setDelFlag("0");
	}
	private void putNoSchTa(String nameEmp,Date dateBegin,Date dateEnd, List<String> allNoSchTas) {
		String rtn = nameEmp+" 在时间段："+DateUtils.dateToStr("yyyy-MM-dd", dateBegin)+" 至"+DateUtils.dateToStr("yyyy-MM-dd", dateEnd)+" 期间未找到合适的实习科室！请手动修改排班！"+"\n";
		allNoSchTas.add(rtn); 	
	}
	private void changeDeptSch (List<SchTa> schTas,SchTaEmp emp,List<String> taDepts,List<String> allNoSchTas,Map<String,Integer> pkDeptAsUse) {
		 List<String> depts= new ArrayList<String>();
         for(int i=0;i<schTas.size();i++){
        	 SchTa ta = schTas.get(i);
        	 List<Map<String,Object>> deptSch = schTaDao.qryDeptSchTa(emp.getPkOrg(), DateUtils.dateToStr("yyyy-MM-dd", ta.getDateBegin())+" 00:00:00", DateUtils.dateToStr("yyyy-MM-dd", ta.getDateEnd())+" 23:59:59", taDepts);
        	 boolean isHavaDept=false;
        	 for(Map<String,Object> map : deptSch){
    			 Integer arguval = map.get("arguval")==null?0:Integer.parseInt(map.get("arguval").toString());
    			 if(arguval==0) throw new  BusException(map.get("nameDept").toString()+" 没有设置实习人数上限！");
    			 Integer empCount = map.get("empCount")==null?0:Integer.parseInt(map.get("empCount").toString());
    			 if(empCount<arguval&& vaildDeptLimit(arguval,pkDeptAsUse,map.get("pkDept").toString())){ //排班人次<科室上限
    				 String pkDept = map.get("pkDept").toString();
    				 if(!depts.contains(pkDept)){ //这个科室没有被排过   					 
    					  ta.setPkDept(pkDept);
    					  pkDeptAsUse.put(pkDept, (pkDeptAsUse.get(pkDept)==null?0:pkDeptAsUse.get(pkDept))+1);
    					  depts.add(pkDept);
        		          isHavaDept = true;
        		          break;  
    				 }
    	         }
    		 }
    		 if(!isHavaDept) {
    		    putNoSchTa(ta.getNameEmp(),ta.getDateBegin(),ta.getDateEnd(),allNoSchTas);
    		 }
         }
	}
 
	private void setDateBeginEnd(List<SchTa> schs ,SchTaEmp emp,User u) {
		Integer cycle = emp.getCycle();
		if(cycle==null||cycle==0) throw new BusException(emp.getNameEmp()+"的轮转周期(天)未维护！");
		int day = DateUtils.getDateSpace(emp.getDateBegin(), emp.getDateEnd())+1;
		if(day<=0) throw new BusException("维护"+emp.getNameEmp()+"的实习日期有问题!");
		Double c = 0.0;
		if(day<cycle) c=1.0;
		else c=	Math.ceil(day/cycle);
		//if(c==null||c<=0) throw new BusException("计算"+emp.getNameEmp()+"可待科室有问题!");	
		Date begin = emp.getDateBegin();
		int count = c.intValue();
		for(int i=0;i<count;i++){
			SchTa ta = new SchTa();
			setSchTa(ta,emp,u);
			ta.setDateBegin(begin);
			if(i==0){
				if(count!=1){ //不是只待一个科室
					ta.setDateEnd(DateUtils.strToDate(DateUtils.addDate(emp.getDateBegin(), cycle-1, 3, "yyyyMMddHHmmss")));
					begin = DateUtils.strToDate(DateUtils.addDate(ta.getDateEnd(), 1, 3, "yyyyMMddHHmmss"));
				}else{
					ta.setDateEnd(emp.getDateEnd());	
				}
			}else if(i==count-1){
				ta.setDateEnd(emp.getDateEnd());		 
			}else{
				ta.setDateEnd(DateUtils.strToDate(DateUtils.addDate(begin, cycle-1, 3, "yyyyMMddHHmmss")));
				begin = DateUtils.strToDate(DateUtils.addDate(ta.getDateEnd(), 1, 3, "yyyyMMddHHmmss"));
			}
			schs.add(ta);
		}
	}

	private List<SchTa> createSchTas(SchTaEmp emp,User u) {
		List<SchTa> schTas = new ArrayList<SchTa>();
		String euWorktype = emp.getEuWorktype();
		if(StringUtils.isBlank(euWorktype)){
			throw new BusException(emp.getNameEmp()+" 实习工作方式未维护！");
		}else if("0".equals(euWorktype)) //固定科室
		{
			return null;
			
		}else if("1".equals(euWorktype))//轮转科室
		{
			setDateBeginEnd(schTas,emp,u);
		}else{
			throw new BusException(emp.getNameEmp()+" 实习工作方式保存的值不在0和1范围内！");
		}
		return schTas;
	}
}
