package com.zebone.nhis.cn.ipdw.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnIpScheduleMapper;
import com.zebone.nhis.cn.ipdw.vo.SchIpDtVO;
import com.zebone.nhis.cn.ipdw.vo.SchIpVO;
import com.zebone.nhis.common.module.cn.ipdw.SchIp;
import com.zebone.nhis.common.module.cn.ipdw.SchIpDt;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnIpScheduleService {
	
	@Autowired
	private CnIpScheduleMapper cnIpScheduleMapper;
	
	//住院医生排班预览：004004013001
	public List<Map<String, Object>> qrySchPreviewList(String param,IUser user){
		
		User u = (User)user;
		String monthSch = JsonUtil.getFieldValue(param, "monthSch");
		
		return cnIpScheduleMapper.qrySchPreviewList(u.getPkDept(),monthSch);
	}
	
	//住院医生排班编辑查询：004004013002
	public List<Map<String, Object>> qrySchEditList(String param,IUser user){
		
		User u = (User)user;
		String monthSch = JsonUtil.getFieldValue(param, "monthSch");
		String date = JsonUtil.getFieldValue(param, "date");
		
		List<Map<String, Object>> schList = cnIpScheduleMapper.qrySchPreviewList(u.getPkDept(),monthSch);
		
		List<Map<String, Object>> docList = cnIpScheduleMapper.qryDoctorList(date,u.getPkDept());
		
		//医生列表
		for (Map<String, Object> doc : docList) {
			
			String docPkEmp = (String)doc.get("pkEmp");
			
			List<Map<String, Object>> list = new ArrayList<>();
			
			//排班记录
			for (Map<String, Object> sch : schList) {
								
				String schPkEmp = (String)sch.get("pkEmp");
				if(docPkEmp.equals(schPkEmp)){
					list.add(sch);
				}
			}
			doc.put("list", list);
		}
		
		return docList;
	}

	//住院医生排班编辑保存：004004013003
	public void saveSchEditList(String param,IUser user){
		
		User u = (User)user;
		SchIpVO vo = JsonUtil.readValue(param, SchIpVO.class);
		
		//排班记录主键
		String pkSchip = vo.getPkSchip();
		
		//生成当月的排班记录（一个月只能有一条）
		if(StringUtils.isBlank(pkSchip)){
			SchIp schIp = new SchIp();
			schIp.setEuType("0");
			schIp.setPkDept(u.getPkDept());
			schIp.setMonthSch(vo.getMonthSch());
			schIp.setDateSch(new Date());
			schIp.setPkEmpSch(u.getPkEmp());
			schIp.setNameEmpSch(u.getNameEmp());
			DataBaseHelper.insertBean(schIp);
			pkSchip = schIp.getPkSchip();
		}
		//排班记录列表
		List<SchIpDtVO> list = vo.getList();
		for (SchIpDtVO schIpDtVO : list) {
			List<SchIpDt> schIpDtList = schIpDtVO.getList();
			if(schIpDtList == null || schIpDtList.size() == 0)continue;
			
			for (SchIpDt schIpDt : schIpDtList) {
				if(StringUtils.isNotBlank(schIpDt.getPkSchipdt())){
					//若取消排班，则删除此明细
					if(StringUtils.isBlank(schIpDt.getDtSchtypedr())){
						DataBaseHelper.deleteBeanByPk(schIpDt);
					}else{
						DataBaseHelper.updateBeanByPk(schIpDt, false);						
					}
				}else if(StringUtils.isNotBlank(schIpDt.getDtSchtypedr())){
					
					schIpDt.setPkSchip(pkSchip);
					schIpDt.setPkEmp(schIpDtVO.getPkEmp());
					schIpDt.setNameEmp(schIpDtVO.getNameEmp());
					DataBaseHelper.insertBean(schIpDt);		
				}
			}
		}
	}
	
	//住院医生排班编辑删除：004004013004
	public void delSchEditList(String param,IUser user){
		
		String pkSchip = JsonUtil.getFieldValue(param, "pkSchip");
		
		DataBaseHelper.execute("delete from sch_ip_dt where pk_schip=?", pkSchip);
		DataBaseHelper.execute("delete from sch_ip where pk_schip=?", pkSchip);
	}
	
	//查询复制排班的年份：004004013005
	public List<Map<String, Object>> qrySchCopyYear(String param,IUser user){
		
		User u = (User)user;
		
		return cnIpScheduleMapper.qrySchCopyYear(u.getPkDept());
	}
	
	//查询复制排班的月份：004004013006
	public List<Map<String, Object>> qrySchCopyMonth(String param,IUser user){
		
		User u = (User)user;
		String yearSch = JsonUtil.getFieldValue(param, "yearSch");
		
		return cnIpScheduleMapper.qrySchCopyMonth(u.getPkDept(),yearSch);
	}
	
	//查询复制排班的人员：004004013007
	public List<Map<String, Object>> qrySchCopyEmployee(String param,IUser user){
		
		User u = (User)user;
		String monthSch = JsonUtil.getFieldValue(param, "monthSch");
		
		return cnIpScheduleMapper.qrySchCopyEmployee(u.getPkDept(),monthSch);
	}
	
	//复制排班的保存：004004013008
	public void saveSchCopy(String param,IUser user) throws ParseException{
			
		User u = (User)user;
		String pkEmps = JsonUtil.getFieldValue(param, "pkEmps");
		String monthSch = JsonUtil.getFieldValue(param, "monthSch");
		String pkSchip = JsonUtil.getFieldValue(param, "pkSchip");
		
		//切割人员主键组成list
		String[] pkEmpsArr = pkEmps.split(",");
		List<String> pkEmpList = Arrays.asList(pkEmpsArr);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkDept", u.getPkDept());
		map.put("monthSch", monthSch);
		map.put("pkEmps", pkEmpList);
		
		//需要复制的排班记录
		List<SchIpDt> list = cnIpScheduleMapper.qrySchCopyList(map);
				
		//获取当前的年月份
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 +"";
		if(month.length() == 1){
			month = "0" + month;
		}		
		
		//查询当前月的排班数据
		SchIp schIp = DataBaseHelper.queryForBean("select * from sch_ip where pk_schip = ?", SchIp.class,pkSchip);
		if(schIp != null && StringUtils.isNotBlank(schIp.getPkSchip())){
			DataBaseHelper.execute("delete from sch_ip_dt where pk_schip=?", schIp.getPkSchip());
			DataBaseHelper.execute("delete from sch_ip where pk_schip=?", schIp.getPkSchip());
		}
		schIp = new SchIp();
		schIp.setEuType("0");
		schIp.setPkDept(u.getPkDept());
		schIp.setMonthSch(year+month);
		schIp.setDateSch(new Date());
		schIp.setPkEmpSch(u.getPkEmp());
		schIp.setNameEmpSch(u.getNameEmp());
		DataBaseHelper.insertBean(schIp);
		
		for (SchIpDt schIpDt : list) {
			
			Calendar record = Calendar.getInstance();
			record.setTime(schIpDt.getDateWork());
			String day = record.get(Calendar.DATE) + "";
			if(day.length() == 1){
				day = "0" + day;
			}
			
			//根据日期查询排班记录
			String dateBegin = year+"-"+month+"-"+day+" 00:00:00";
			String dateEnd = year+"-"+month+"-"+day+" 23:59:59";
			
			//当前月存在排班记录时
			//查询当前月的同一日是否已有排班记录（同一天不能有两人排班）
			/*if(exitsSch){
				SchIpDt exitsDt = cnIpScheduleMapper.qrySchIpDtByDate(dateBegin,dateEnd);
				if(exitsDt != null){
					throw new BusException(year+"年"+month+"月"+day+"日已有重复的排班记录！");
				}
			}*/
			schIpDt.setPkSchipdt(null);
			schIpDt.setPkSchip(schIp.getPkSchip());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = simpleDateFormat.parse(dateBegin);
			schIpDt.setDateWork(date);
			DataBaseHelper.insertBean(schIpDt);
		}
	}
}
