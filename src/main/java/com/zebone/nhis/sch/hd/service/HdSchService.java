package com.zebone.nhis.sch.hd.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.pi.PiHd;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.sch.hd.SchSchHd;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.sch.hd.dao.HdSchMapper;
import com.zebone.nhis.sch.hd.vo.CopySchVo;
import com.zebone.nhis.sch.hd.vo.SchBedVo;
import com.zebone.nhis.sch.hd.vo.SchPiHdVo;
import com.zebone.nhis.sch.hd.vo.SchedulingByBed;
import com.zebone.nhis.sch.hd.vo.WeekVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 透析排班服务
 * @author IBM
 *
 */
@Service
public class HdSchService {
	@Resource
	private HdSchMapper hdSchMapper;
	
	/**
	 * 查询排班信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryDialysisSchHd(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//pkOrg 当前机构  
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkOrg")))
			throw new BusException("未获取到要查询的机构");	
		if(!CommonUtils.isNull(paramMap.get("dateBegin"))){
			Date beginDate=DateUtils.strToDate(paramMap.get("dateBegin").toString(), "yyyyMMddHHmmss");
			paramMap.put("dateBegin", DateUtils.dateToStr("yyyy-MM-dd", beginDate));
		}else{
			paramMap.put("dateBegin", null);
		}
		
		if(!CommonUtils.isNull(paramMap.get("dateEnd"))){
			Date DateEnd=DateUtils.strToDate(paramMap.get("dateEnd").toString(), "yyyyMMddHHmmss");
			paramMap.put("dateEnd", DateUtils.dateToStr("yyyy-MM-dd", DateEnd));
		}else{
			paramMap.put("dateEnd", null);
		}
		
		List<Map<String,Object>> hdSchList=hdSchMapper.queryDialysisSchHd(paramMap);
		return hdSchList;		
	}
	
	/**
	 * 取消排班
	 * @param param
	 * @param user
	 */
	public void cancleSchHd(String param,IUser user){
		SchSchHd hd=JsonUtil.readValue(param, SchSchHd.class);
		Map<String,String> paramMap = new HashMap<String,String>();
		
		paramMap.put("pkSchhd", hd.getPkSchhd());
		
		//查询患者是否已重新排班
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkOrg", hd.getPkOrg());
		List<Map<String,Object>> hdSchList=hdSchMapper.queryDialysisSchHd(map);
		//查询患者是否已就诊或已取消
		List<Map<String,Object>> result=hdSchMapper.queryCancleSchHd(paramMap);
		if(result.size()>0){
			String ifCancle=(result.get(0).get("ifcancle")).toString();
			if(ifCancle.equals("0")){
				hd.setDateCanc(new Date());
				hd.setPkEmpCanc(((User)user).getPkEmp());
				hd.setNameEmpCanc(((User)user).getNameEmp());
				DataBaseHelper.update("update sch_sch_hd set flag_canc='1',date_canc=:dateCanc,pk_emp_canc=:pkEmpCanc,name_emp_canc=:nameEmpCanc where pk_schhd=:pkSchhd and flag_canc='0' and flag_confirm='0'",hd);
			}else{
				throw new BusException("床位已占用，无法恢复！");
			}
		}else{
			throw new BusException("患者已就诊，无法取消！");
		}
	}
	
	/**
	 * 恢复排班
	 * @param param
	 * @param user
	 */
	public void recoverySchHd(String param,IUser user){
		SchSchHd hd=JsonUtil.readValue(param, SchSchHd.class);
		Map<String,String> paramMap = new HashMap<String,String>();	
		paramMap.put("pkSchhd", hd.getPkSchhd());
		//Date dateHd=DateUtils.strToDate(hd.getDateHd().toString(), "yyyyMMddHHmmss");
		paramMap.put("dateHd", DateUtils.dateToStr("yyyy-MM-dd", hd.getDateHd()));
		paramMap.put("pkDateslot", hd.getPkDateslot());
		paramMap.put("pkHdbed", hd.getPkHdbed());
		//查询治疗日期，班次，床位是否被占用
		List<Map<String,Object>> result=hdSchMapper.queryRecoverySchHd(paramMap);
		List<Map<String,Object>> codeAccList = DataBaseHelper.queryForList("select PK_HDBED from BD_RES_HDBED where PK_HDBED = ? ", hd.getPkHdbed());
		if(!(codeAccList.size()>0)){
			throw new BusException("床位已删除，恢复失败！");
		}
		if(result.size()>0){
			String ifRecovery=result.get(0).get("ifrecovery").toString();
			if(ifRecovery.equals("0")){
				DataBaseHelper.update("update sch_sch_hd set flag_canc='0',date_canc=null,pk_emp_canc=null,name_emp_canc=null where pk_schhd=:pkSchhd and flag_canc='1'",hd);
			}else{
				throw new BusException("床位已占用，无法恢复！");
			}
		}else{
			throw new BusException("床位已占用，无法恢复！");
		}
	}
	
	/**
	 * 透析排班-床位视角
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SchedulingByBed> dialysisScheduling(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		List<SchedulingByBed> result=Lists.newArrayList();
		if(paramMap==null)
			throw new BusException("未获取到要查询的参数");
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkOrg")))
			throw new BusException("未获取到要查询的机构");
		if(paramMap==null||CommonUtils.isNull(paramMap.get("startDate")))
			throw new BusException("未获取到要查询的日期");
		
		Date beginDate=DateUtils.strToDate(paramMap.get("startDate"), "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		cal.add(Calendar.DATE, +6);
		Date afterDate=cal.getTime();

		//查询符合条件的排班
		List<SchBedVo> hdSchList = hdSchMapper.querySchHd(paramMap);
		//List<SchBedVo> hdSchList=new ArrayList<SchBedVo>();
		
		//查询符合的床位
		List<SchBedVo> hdBedList = hdSchMapper.queryHdbedIsNull(paramMap);
		
		if(hdBedList==null || hdBedList.size()==0){
			throw new BusException("未获取到符合患者信息的床位");
		}
		
		//床位主键pk
		List<String> listPk=new ArrayList<String>();
		
		for(SchBedVo sch:hdBedList){			
			SchedulingByBed schBed=new SchedulingByBed();
			schBed.setPkHdbed(sch.getPkHdbed());
			schBed.setDtHdtypeCopy(sch.getDtHdtype());//校验床位类型
			if(!listPk.contains(sch.getPkHdbed())){
				schBed.setCodeBed(sch.getCodeBed());
				schBed.setNameBed(sch.getNameBed());
				schBed.setMsp(sch.getMsp());
				schBed.setDtHdtype(sch.getDtHdtype());
			}
			schBed.setNameDateslot(sch.getNameDateslot());
			schBed.setPkDateslot(sch.getPkDateslot());
			
			List<WeekVo> days=new ArrayList<WeekVo>();
			Date stDate=beginDate;
			while(stDate.compareTo(afterDate)<=0){
				WeekVo week= new WeekVo();
				for(SchBedVo sch1:hdSchList){
					String sd1=DateUtils.dateToStr("yyyy-MM-dd", sch1.getDateHd());
					String sd2=DateUtils.dateToStr("yyyy-MM-dd", stDate);
					if(sch1.getPkHdbed().equals(sch.getPkHdbed()) && sch1.getPkDateslot().equals(sch.getPkDateslot()) && sd1.equals(sd2)){
						week.setPkSchhd(sch1.getPkSchhd());
						week.setDateHd(sch1.getDateHd());
						week.setPkPi(sch1.getPkPi());
						week.setNamePi(sch1.getNamePi());
						break;
					}
				}
				days.add(week);
				cal.setTime(stDate);
				cal.add(Calendar.DATE, +1);
				stDate=cal.getTime();
			}
			schBed.setOneDay(days);
			result.add(schBed);
			listPk.add(sch.getPkHdbed());
		}
		return result;
	}
	
	/**
	 * 排班查询-患者视角
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SchPiHdVo> qureySchByPihd(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		List<SchPiHdVo> result=Lists.newArrayList();
		if(paramMap==null)
			throw new BusException("未获取到要查询的参数");
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkOrg")))
			throw new BusException("未获取到要查询的机构");
		if(paramMap==null||CommonUtils.isNull(paramMap.get("startDate")))
			throw new BusException("未获取到要查询的日期");
		
		Date beginDate=DateUtils.strToDate(paramMap.get("startDate"), "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		//查询符合条件的排班
		List<SchBedVo> hdSchList = hdSchMapper.querySchHd(paramMap);
				
		//查询符合的患者
		List<SchBedVo> hdPiList = hdSchMapper.queryPiHdIsNull(paramMap);
		
		for(SchBedVo sch:hdPiList){
			SchPiHdVo piHd=new SchPiHdVo();
			piHd.setPkPi(sch.getPkPi());
			piHd.setNamePi(sch.getNamePi());
			piHd.setCodePi(sch.getCodePi());
			piHd.setCodeHd(sch.getCodeHd());
			piHd.setCntWeek(sch.getCntWeek());
			
			Date stDate=beginDate;
			
			//第一天
			String sd=DateUtils.dateToStr("yyyy-MM-dd", stDate);
			SchBedVo one=lookupSch(hdSchList,sch,sd);
			if(one !=null){
				piHd.setNameDateslotOne(one.getNameDateslot());
				piHd.setCodeBedOne(one.getCodeBed());
			}
			//第二天
			cal.setTime(stDate);
			cal.add(Calendar.DATE, +1);
			stDate=cal.getTime();
			sd=DateUtils.dateToStr("yyyy-MM-dd", stDate);			
			SchBedVo two=lookupSch(hdSchList,sch,sd);
			if(two !=null){
				piHd.setNameDateslotTwo(two.getNameDateslot());
				piHd.setCodeBedTwo(two.getCodeBed());
			}
			//第3天
			cal.setTime(stDate);
			cal.add(Calendar.DATE, +1);
			stDate=cal.getTime();
			sd=DateUtils.dateToStr("yyyy-MM-dd", stDate);			
			SchBedVo Three=lookupSch(hdSchList,sch,sd);
			if(Three !=null){
				piHd.setNameDateslotThree(Three.getNameDateslot());
				piHd.setCodeBedThree(Three.getCodeBed());
			}
			//第4天
			cal.setTime(stDate);
			cal.add(Calendar.DATE, +1);
			stDate=cal.getTime();
			sd=DateUtils.dateToStr("yyyy-MM-dd", stDate);			
			SchBedVo Four=lookupSch(hdSchList,sch,sd);
			if(Four !=null){
				piHd.setNameDateslotFour(Four.getNameDateslot());
				piHd.setCodeBedFour(Four.getCodeBed());
			}
			//第5天
			cal.setTime(stDate);
			cal.add(Calendar.DATE, +1);
			stDate=cal.getTime();
			sd=DateUtils.dateToStr("yyyy-MM-dd", stDate);			
			SchBedVo Five=lookupSch(hdSchList,sch,sd);
			if(Five !=null){
				piHd.setNameDateslotFive(Five.getNameDateslot());
				piHd.setCodeBedFive(Five.getCodeBed());
			}
			//第6天
			cal.setTime(stDate);
			cal.add(Calendar.DATE, +1);
			stDate=cal.getTime();
			sd=DateUtils.dateToStr("yyyy-MM-dd", stDate);			
			SchBedVo Six=lookupSch(hdSchList,sch,sd);
			if(Six !=null){
				piHd.setNameDateslotSix(Six.getNameDateslot());
				piHd.setCodeBedSix(Six.getCodeBed());
			}
			//第7天
			cal.setTime(stDate);
			cal.add(Calendar.DATE, +1);
			stDate=cal.getTime();
			sd=DateUtils.dateToStr("yyyy-MM-dd", stDate);			
			SchBedVo Seven=lookupSch(hdSchList,sch,sd);
			if(Seven !=null){
				piHd.setNameDateslotSeven(Seven.getNameDateslot());
				piHd.setCodeBedSeven(Seven.getCodeBed());
			}
			result.add(piHd);
		}
		
		return result;
	}
	
	//患者视角-周日内排班查询
	public SchBedVo lookupSch(List<SchBedVo> hdSchList,SchBedVo vo,String dt){
		SchBedVo schVo=null;
		for(SchBedVo sch:hdSchList){
			String sd1=DateUtils.dateToStr("yyyy-MM-dd", sch.getDateHd());			
			if(vo.getPkPi().equals(sch.getPkPi()) && sd1.equals(dt)){
				schVo= sch;
				break;
			}			
		}
		return schVo;
	}
	
	/**
	 * 排班处理-保存排班
	 * @param param
	 * @param user
	 */
	public void saveSchSchPh(String param,IUser user){		
		List<SchSchHd> schSchHdList=JsonUtil.readValue(param,
				new TypeReference<List<SchSchHd>>() {
				});
		if (schSchHdList == null || schSchHdList.size() <= 0)
			throw new BusException("未获取到要保存的内容！");

		for (SchSchHd schSchHd : schSchHdList) {
			if (CommonUtils.isEmptyString(schSchHd.getPkSchhd())) {// 新增
				schSchHd.setFlagCanc("0");
				schSchHd.setFlagConfirm("0");
				DataBaseHelper.insertBean(schSchHd);
			} else {// 更新
				DataBaseHelper.updateBeanByPk(schSchHd, false);
			}
		}
	}
	
	/**
	 * 复制排班
	 * @param param
	 * @param user
	 */
	public void copySchSchPh(String param,IUser user){
		CopySchVo copySch = JsonUtil.readValue(param, CopySchVo.class);
		if(copySch==null)
			throw new BusException("未获取到排班信息！");
		List<SchedulingByBed> schList=copySch.getSchList();
		Date start =copySch.getStart();
		Date end =copySch.getEnd();
		Boolean fiCover=copySch.getIfCover().equals("0")?false:true;
		
		//先把原数据拿出来
		List<SchSchHd> schOldList=new ArrayList<SchSchHd>();
		for(SchedulingByBed sch : schList) 
		{
			List<WeekVo> week=sch.getOneDay();
			for(WeekVo vo:week){
				if(vo.getPkSchhd()!=null){
					SchSchHd schHd=new SchSchHd();
					schHd.setPkSchhd(vo.getPkSchhd());
				    schHd.setPkPi(vo.getPkPi());
				    schHd.setPkDateslot(sch.getPkDateslot());
				    schHd.setPkHdbed(sch.getPkHdbed());
				    schHd.setDateHd(vo.getDateHd());
				    schHd.setDateOpt(new Date());
				    schOldList.add(schHd);
				}  
			}
		}
		
		//新生成的排班,给需要复制的排班，不包含最后一天的数据	
		List<SchSchHd> schNewList=new ArrayList<SchSchHd>();
		//最终需要存入的list	
		List<SchSchHd> schSaveList=new ArrayList<SchSchHd>();;
		for(SchSchHd sch:schOldList){
			Date d1=start;
			while(d1.compareTo(end)<=0){  //时间循环
				int day1=d1.getDay();
				int day2=sch.getDateHd().getDay();
				if(day1==day2){   //判断星期是否相等
					//根据星期重新生成排班信息
				    SchSchHd schHd=new SchSchHd();
				    schHd.setPkPi(sch.getPkPi());
				    schHd.setPkDateslot(sch.getPkDateslot());
				    schHd.setPkHdbed(sch.getPkHdbed());
				    schHd.setDateHd(d1);
				    //其他
				    schHd.setPkOrg(((User)user).getPkOrg());				    
				    schHd.setPkDept(((User)user).getPkDept());
				    schHd.setPkEmpOpt(((User)user).getPkEmp());
				    schHd.setNameEmpOpt(((User)user).getUserName());
				    schHd.setPkDept(((User)user).getPkDept());				    
				    schHd.setDateOpt(new Date());
				    schHd.setFlagCanc("0");
				    schHd.setFlagConfirm("0");
				    schNewList.add(schHd);
				    schSaveList.add(schHd);
				}
				d1=getSomeDay(d1,1);
			}
		}
		
		//查询符合条件的排班
		Map<String,String> map=new HashMap<String,String>();
		map.put("pkOrg", ((User)user).getPkOrg());
		map.put("pkDeptNs", copySch.getPkDeptNs());
		String d1=DateUtils.dateToStr("yyyy-MM-dd", start);
		String d2=DateUtils.dateToStr("yyyy-MM-dd", end);
		map.put("startDate", d1);
		map.put("endDate", d2);
		List<SchBedVo> hdSchList = hdSchMapper.querySchHd(map);	
		
		
		//当选择覆盖时，删除对应时间内的已有排班数据
		if(fiCover){
			for(SchBedVo vo:hdSchList){
				String pk=vo.getPkSchhd();
				String sql_d = "DELETE  FROM sch_sch_hd where pk_schhd=?";
				int t=DataBaseHelper.execute(sql_d, new Object[]{pk});
			}
		}else{ //当选择不覆盖时，移除已有床位排班信息
			for(SchSchHd sch:schNewList){
				for(SchBedVo vo:hdSchList){
					//床位已占用的移除
					if(vo.getPkHdbed().equals(sch.getPkHdbed()) && vo.getPkDateslot().equals(sch.getPkDateslot()) && vo.getDateHd().equals(sch.getDateHd()) ){
						schSaveList.remove(sch);						
					}
					//对应用户今天已有排班的删除
					if(vo.getPkPi().equals(sch.getPkPi()) && vo.getDateHd().equals(sch.getDateHd()) ){
						schSaveList.remove(sch);
					}
				}
			}
		}

		//保存新排班
		for (SchSchHd schSchHd : schSaveList) {			
			DataBaseHelper.insertBean(schSchHd);
		}

	}
	
	//获取日期
	public static Date getSomeDay(Date date, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
	    return calendar.getTime();
	}
	
	/**
	 * 查询排班
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> querySch(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null ||CommonUtils.isNull(map.get("pkPi"))||CommonUtils.isNull(map.get("dateCur")))
			throw new BusException("未获取到要查询的患者信息");
		//日期格式转换
		Date dateCur=DateUtils.strToDate(map.get("dateCur").toString(), "yyyy-MM-dd");
		String dateHd=DateUtils.dateToStr("yyyy-MM-dd", dateCur);
		map.put("dateHd", dateHd);
		
		List<Map<String,Object>> result=hdSchMapper.querySchHdBusiness(map);
		return result;
	}
}
