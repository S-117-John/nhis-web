package com.zebone.nhis.base.bd.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.base.bd.dao.CodeMapper;
import com.zebone.nhis.base.bd.vo.BdCodeDateslotParam;
import com.zebone.nhis.base.bd.vo.BdCodeDateslotSecVO;
import com.zebone.nhis.base.bd.vo.BdDefdocSaveParam;
import com.zebone.nhis.base.bd.vo.BdReportVo;
import com.zebone.nhis.base.bd.vo.BdSysTempSaveParam;
import com.zebone.nhis.base.bd.vo.BdSysparamExt;
import com.zebone.nhis.base.bd.vo.BdSysparamVO;
import com.zebone.nhis.common.module.base.bd.code.BdAdminDivision;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotSec;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotTime;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoclist;
import com.zebone.nhis.common.module.base.bd.code.BdEncoderule;
import com.zebone.nhis.common.module.base.bd.code.BdReport;
import com.zebone.nhis.common.module.base.bd.code.BdReportParam;
import com.zebone.nhis.common.module.base.bd.code.BdSysparam;
import com.zebone.nhis.common.module.base.bd.code.BdSysparamTemp;
import com.zebone.nhis.common.module.base.bd.code.BdTempPrt;
import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendar;
import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendardate;
import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendrule;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * ???????????? - ????????????
 * @author ywj
 */
@Service
public class CodeService {
	
	
	@Autowired
	private CodeMapper codeMapper;
	
	
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public BdDefdoclist saveBdDefdoclist(String param , IUser user){
		BdDefdoclist bdDefdoclist = JsonUtil.readValue(param,BdDefdoclist.class);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("pkDefdoclist", bdDefdoclist.getPkDefdoclist());
		params.put("pkOrg", bdDefdoclist.getPkOrg());
		int countCode = 0;
		//??????code
		params.put("code", bdDefdoclist.getCode());
		countCode = codeMapper.BdDefdoclistCheckExist(params);
		if(countCode > 0) throw new BusException("?????????????????????");
		
		boolean isAdd=true;
		if(bdDefdoclist.getPkDefdoclist() == null){
			DataBaseHelper.insertBean(bdDefdoclist);
		}else{
			String flagSys = codeMapper.searchDefdoclist(bdDefdoclist.getPkDefdoclist());
			if("1".equals(flagSys)){
				throw new BusException("???????????????????????????,?????????????????????");
			}else{
				DataBaseHelper.updateBeanByPk(bdDefdoclist,false);
				isAdd = false;
			}


		}
		if(!isAdd) {			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkDefdoclist", bdDefdoclist.getPkDefdoclist());
			paramMap.put("isAdd", isAdd);
			paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());//??????????????????????????????????????????
			PlatFormSendUtils.sendBdDefDocMsg(paramMap);
		}
		return bdDefdoclist;
	}
	
	/**
	 * ???????????????????????????
	 * @param param
	 * @param user
	 */
	public void deleteBdDefdoclist(String param , IUser user){
		
		BdDefdoclist bdDefdoclist = JsonUtil.readValue(param,BdDefdoclist.class);
		
		int count = DataBaseHelper.queryForScalar("select count(1) from BD_DEFDOC "
				+ "where CODE_DEFDOCLIST = ? and DEL_FLAG = '0'",Integer.class,bdDefdoclist.getCode());
		
		if(count == 0){
			String flagSys = codeMapper.searchDefdoclist(bdDefdoclist.getPkDefdoclist());
			if("1".equals(flagSys)){
				throw new BusException("???????????????????????????,?????????????????????");
			}else {
				DataBaseHelper.update("update BD_DEFDOCLIST set del_flag = '1' where PK_DEFDOCLIST = ?", new Object[]{bdDefdoclist.getPkDefdoclist()});
			}
		}else{
			throw new BusException("????????????????????????");
		}
	}
	
	
	/**
	 * ??????????????????????????????????????????
	 * @param param
	 * @param user
	 */
	@Transactional
	public BdDefdocSaveParam saveBdDefdoc(String param , IUser user){
		//??????????????????
		BdDefdocSaveParam bdDefdocSaveParam = JsonUtil.readValue(param,BdDefdocSaveParam.class);
		//????????????????????????
		String codeDefdoclist = bdDefdocSaveParam.getCodeDefdoclist();
		//??????????????????????????????
		List<BdDefdoc> bdDefdocs = bdDefdocSaveParam.getBdDefdoc();
		//????????????????????????????????????
		List<String> list = bdDefdocSaveParam.getDelPkDefdocs();

		Set<String> updateSet = bdDefdocSaveParam.getUpdatePkDefdocs();

		Map<String,String> map = new HashMap<String,String>();
		//???Map???????????? ????????????
		for (BdDefdoc bdDefdoc : bdDefdocs) {
			map.put(bdDefdoc.getCode(), "code");
		}
		if(map.size() < bdDefdocs.size()){
			throw new BusException("???????????????");
		}
		if(list!=null&&list.size()>0)
			codeMapper.delDefdocsByList(list);
		//DataBaseHelper.update("update BD_DEFDOC set del_flag = '1' where CODE_DEFDOCLIST = ?", new Object[]{codeDefdoclist});
		List<String> insertPk=new ArrayList<String>();//???????????????????????????????????????????????????
		boolean isAdd=false;
		
		for (BdDefdoc bdDefdoc : bdDefdocs) {
			if(bdDefdoc.getPkDefdoc() == null){
				DataBaseHelper.insertBean(bdDefdoc);
				insertPk.add(bdDefdoc.getPkDefdoc());
				isAdd=true;
			}else if(updateSet!=null&&updateSet.size()>0){
				for (String UpdatePkDefdocs : updateSet) {
					if (UpdatePkDefdocs.equals(bdDefdoc.getPkDefdoc())){
						String flagSys = codeMapper.searchDefdoc(bdDefdoc.getPkDefdoc());
						if ("1".equals(flagSys)){
							throw new BusException("???????????????????????????,???????????????");
						}else{
							DataBaseHelper.updateBeanByPk(bdDefdoc,false);
						}
					}
				}

			}
		}

	  //?????????????????????
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());//??????????????????????????????????????????
		paramMap.put("bdDefdoc",bdDefdocSaveParam.getBdDefdoc());
		paramMap.put("delPkDefdocs",bdDefdocSaveParam.getDelPkDefdocs());
		paramMap.put("isAdd",isAdd);
		paramMap.put("delPkDeflist",list);		
		PlatFormSendUtils.sendBdDefDocMsg(paramMap);		
		return bdDefdocSaveParam;
	}

	/**
	 * ?????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void deleteBdDefdoc (String param , IUser user){
		String pkDefdoc = JSON.parseObject(param).getString("pkDefdoc");
		if(pkDefdoc != null){
			String flagSys = codeMapper.searchDefdoc(pkDefdoc);
			if ("1".equals(flagSys)){
				throw new BusException("???????????????????????????,???????????????");
			}else{
				DataBaseHelper.update("update BD_DEFDOC set del_flag = '1' where PK_DEFDOC = ?",pkDefdoc );
			}
		}
	}

	/**
	 * ???????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void checkDelete(String param , IUser user) {
		String pkDefdoc = JSON.parseObject(param).getString("pkDefdoc");
		if (pkDefdoc != null) {
			String flagSys = codeMapper.searchDefdoc(pkDefdoc);
			if ("1".equals(flagSys)) {
				throw new BusException("???????????????????????????,???????????????");
			}
		}else{
			throw new BusException("????????????????????????");
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 */
	public BdWorkcalendrule saveBdWorkcalendrule(String param , IUser user){
		
		BdWorkcalendrule bdWorkcalendrule = JsonUtil.readValue(param,BdWorkcalendrule.class);
		Map<String,String> params = new HashMap<String,String>();
		params.put("pkWorkcalendrule", bdWorkcalendrule.getPkWorkcalendrule());
		//params.put("pkOrg", bdWorkcalendrule.getPkOrg());
		int countCode = 0;
		int countName = 0;
		//??????code
		params.put("code", bdWorkcalendrule.getCode());
		countCode = codeMapper.BdWorkcalendruleCheckExist(params);
		if(countCode > 0) throw new BusException("?????????????????????");
		//??????name
		params.remove("code");
		params.put("name",bdWorkcalendrule.getName());
		countName = codeMapper.BdWorkcalendruleCheckExist(params);
		if(countName > 0) throw new BusException("?????????????????????");
		
		if(bdWorkcalendrule.getPkWorkcalendrule() == null){
			DataBaseHelper.insertBean(bdWorkcalendrule);
		}else{
			DataBaseHelper.updateBeanByPk(bdWorkcalendrule,false);
		}
		return bdWorkcalendrule;
	}
	
	/**
	 * ?????????????????????
	 * @param param
	 * @param user
	 */
	public void deleteBdWorkcalendrule(String param , IUser user){
		
		String pkWorkcalendrule = JSON.parseObject(param).getString("pkWorkcalendrule");
		
		int count = DataBaseHelper.queryForScalar("select count(1) from BD_WORKCALENDAR "
				+ "where PK_WORKCALENDRULE = ?",Integer.class,pkWorkcalendrule);
		
		if(count == 0){
			DataBaseHelper.update("update BD_WORKCALENDRULE set del_flag = '1' where PK_WORKCALENDRULE = ?", new Object[]{pkWorkcalendrule});
		}else{
			throw new BusException("????????????????????????");
		}
	}
	
	
	
	
	/**
	 * ????????????????????????
	 * @param param
	 * @param user
	 */
	public BdWorkcalendar saveBdWorkcalendar(String param , IUser user){
		
		BdWorkcalendar bdWorkcalendar = JsonUtil.readValue(param,BdWorkcalendar.class);
		Map<String,String> params = new HashMap<String,String>();
		params.put("pkWorkcalendar", bdWorkcalendar.getPkWorkcalendar());
		params.put("pkOrg", bdWorkcalendar.getPkOrg());
		int countCode = 0;
		int countName = 0;
		//??????code
		params.put("code", bdWorkcalendar.getCode());
		countCode = codeMapper.BdWorkcalendarCheckExist(params);
		if(countCode > 0) throw new BusException("?????????????????????");
		//??????name
		params.remove("code");
		params.put("name",bdWorkcalendar.getName());
		countName = codeMapper.BdWorkcalendarCheckExist(params);
		if(countName > 0) throw new BusException("?????????????????????");
		//????????????
		if("1".equals(bdWorkcalendar.getCalendarDef())){
			params.remove("name");
			params.put("calendarDef","1");
			int count = codeMapper.BdWorkcalendarCheckExist(params);
			if( count > 0 ) throw new BusException("???????????????????????????");
		}
		
		
		if(bdWorkcalendar.getPkWorkcalendar() == null){

			DataBaseHelper.insertBean(bdWorkcalendar);
		}else{
			DataBaseHelper.updateBeanByPk(bdWorkcalendar,false);
		}
		return bdWorkcalendar;
	}
	
	/**
	 * ???????????????
	 * @param param
	 * @param user
	 */
	@Transactional
	public void deleteBdWorkcalendar(String param , IUser user){
		String pkWorkcalendar = JSON.parseObject(param).getString("pkWorkcalendar");
		DataBaseHelper.execute("delete from BD_WORKCALENDAR where pk_workcalendar = ?", new Object[]{pkWorkcalendar});
		DataBaseHelper.execute("delete from BD_WORKCALENDARDATE where pk_workcalendar = ?", new Object[]{pkWorkcalendar});
	}
	
	/**
	 * ?????????????????????
	 * @param param
	 * @param user
	 */
	@Transactional
	public List<BdWorkcalendardate> generateBdWorkcalendardate(String param , IUser user){
		JSONObject jo = JSON.parseObject(param);
		String pkWorkcalendrule = jo.getString("pkWorkcalendrule");
		String pkWorkcalendar = jo.getString("pkWorkcalendar");
		String year = jo.getString("year");
		//???????????????????????????
		
		int count = DataBaseHelper.queryForScalar("select count(1) from BD_WORKCALENDARDATE "
				+ "where pk_workcalendar = ? and substr(to_char(CALENDARDATE,'YYYYMMDD'),1,4) = ? and DEL_FLAG = '0'",Integer.class,pkWorkcalendar,year);
		if(count > 0){
			throw new BusException("?????????????????????");
		}
		//1???????????????????????????=0???	  2???????????????????????????=1???   3???????????????????????????=2???
		BdWorkcalendrule rule = DataBaseHelper.queryForBean("select * from BD_WORKCALENDRULE where pk_workcalendrule = ? ", BdWorkcalendrule.class, new Object[]{pkWorkcalendrule});
		Map<String,String> ruleMap = new HashMap<String,String>();
		ruleMap.put("1", rule.getMonday());
		ruleMap.put("2", rule.getTuesday());
		ruleMap.put("3", rule.getWednesday());
		ruleMap.put("4", rule.getThursday());
		ruleMap.put("5", rule.getFriday());
		ruleMap.put("6", rule.getSaturday());
		ruleMap.put("7", rule.getSunday());
		ruleMap.put("ondutytime", rule.getOndutytime());
		ruleMap.put("offdutytime", rule.getOffdutytime());
		
		DateTime dtStart = new DateTime(year + "-01-01");
		DateTime dtEnd = new DateTime(year + "-12-31");
		int days = Days.daysBetween(dtStart,dtEnd).getDays() + 1 ;
		
		DateTime dtTemp ;
		BdWorkcalendardate bdWorkcalendardate ;
		List<BdWorkcalendardate> dateList = new ArrayList<BdWorkcalendardate>();
		for (int i = 0; i < days; i++) {
			bdWorkcalendardate = new BdWorkcalendardate();
			dtTemp = dtStart.plusDays(i);
			
			bdWorkcalendardate.setPkWorkcalendar(pkWorkcalendar);
			bdWorkcalendardate.setCalendardate(dtTemp.toDate());
			bdWorkcalendardate.setOndutytime(ruleMap.get("ondutytime"));
			bdWorkcalendardate.setOffdutytime(ruleMap.get("offdutytime"));
			bdWorkcalendardate.setDatetype(ruleMap.get(dtTemp.getDayOfWeek()+""));
			bdWorkcalendardate.setWeekno(dtTemp.getDayOfWeek()+"");
			bdWorkcalendardate.setDelFlag("0");
			DataBaseHelper.insertBean(bdWorkcalendardate);
			dateList.add(bdWorkcalendardate);
			
			bdWorkcalendardate = null;
			dtTemp = null;
		}
		
		return dateList;
	}
	
	/**
	 * ?????????????????????
	 * @param param
	 * @param user
	 */
	@Transactional
	public void updateBdWorkcalendardate(String param , IUser user){
		List<BdWorkcalendardate> list = JSON.parseArray(param,BdWorkcalendardate.class);
		DateTime now = DateTime.parse(DateTime.now().toString("yyyy-MM-dd"));
		DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");
		for (BdWorkcalendardate bdWorkcalendardate : list) {
			DateTime Workcalendardate = new DateTime(bdWorkcalendardate.getCalendardate());
			if(Days.daysBetween(now, Workcalendardate).getDays() <= 0){
				throw new BusException("???????????????????????????????????????");
			}
			DateTime on = DateTime.parse(bdWorkcalendardate.getOndutytime(),dtf);
			DateTime off = DateTime.parse(bdWorkcalendardate.getOffdutytime(),dtf);
			if(Minutes.minutesBetween(on, off).getMinutes() <= 0){
				throw new BusException("????????????????????????????????????");
			}
		}
		for (BdWorkcalendardate bdWorkcalendardate : list) {
			DataBaseHelper.updateBeanByPk(bdWorkcalendardate,false);
		}
	}
	
	public static void main(String[] args) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");
		DateTime now1 = DateTime.parse("08:00:00",dtf);
		DateTime now2 = DateTime.parse("07:00:00",dtf);
		System.out.println(now1);
		System.out.println(now2);
		System.out.println(Minutes.minutesBetween(now1, now2).getMinutes());
	}
	
	
	
	/**
	 * ??????????????????????????????(???????????????????????????????????????)
	 * @param param
	 * @param user
	 */
	@Transactional
	public BdCodeDateslot saveBdCodeDateslot(String param , IUser user){
		BdCodeDateslotParam bdCodeDateslotParam = JsonUtil.readValue(param, BdCodeDateslotParam.class);
		BdCodeDateslot bdCodeDateslot = bdCodeDateslotParam.getBdCodeDateslot();
		List<BdCodeDateslotSecVO> secList = bdCodeDateslotParam.getBdCodeDateslotSec();
		List<BdCodeDateslotTime> timeList = bdCodeDateslotParam.getBdCodeDateslotTime();
		
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("pkDateslot", bdCodeDateslot.getPkDateslot());
		params.put("pkOrg", bdCodeDateslot.getPkOrg());
		int countCode = 0;
		//??????code
		params.put("codeDateslot", bdCodeDateslot.getCodeDateslot());
		countCode = codeMapper.BdCodeDateslotCheckExist(params);
		if(countCode > 0) throw new BusException("???????????????????????????");
		//??????name
		//params.remove("codeDateslot");
		//int countName = 0;
		//params.put("nameDateslot",bdCodeDateslot.getNameDateslot());
		//countName = codeMapper.BdCodeDateslotCheckExist(params);
		//if(countName > 0) throw new BusException("???????????????????????????");
		
		//????????????
		Map<String,String> codeMap = new HashMap<String,String>();
		Map<String,String> nameMap = new HashMap<String,String>();
		if(secList != null){
			for (BdCodeDateslotSec bdCodeDateslotSec : secList) {
				nameMap.put(bdCodeDateslotSec.getNameTime(), "name");
			}
			if(nameMap.size() < secList.size()) throw new BusException("???????????????????????????");
		}
		
		if(timeList != null){
			nameMap.clear();
			for (BdCodeDateslotTime bdCodeDateslotTime : timeList) {
				codeMap.put(bdCodeDateslotTime.getCode(), "code");
				nameMap.put(bdCodeDateslotTime.getName(), "name");
			}
			if(codeMap.size() < timeList.size()) throw new BusException("???????????????????????????");
			if(nameMap.size() < timeList.size()) throw new BusException("???????????????????????????");
		}
		
		
		String pkDateslot = bdCodeDateslot.getPkDateslot();
		if(StringUtils.isBlank(pkDateslot)){
			DataBaseHelper.insertBean(bdCodeDateslot);
			pkDateslot = bdCodeDateslot.getPkDateslot();
		}else{
			DataBaseHelper.updateBeanByPk(bdCodeDateslot,false);
		}
		//????????????sortno
		List<BdCodeDateslot> bdcodelist = codeMapper.getBdCodeDateslotForSortNo(bdCodeDateslot.getDtDateslottype());
		for (int i = 0; i < bdcodelist.size(); i++) {
			BdCodeDateslot bcd = bdcodelist.get(i);
			bcd.setSortno(i+1);
		}
		codeMapper.batchUpdateBdCodeDateslotSortNo(bdcodelist);
		
		
		if(timeList != null){
			DataBaseHelper.execute("delete from BD_CODE_DATESLOT_TIME where PK_DATESLOT = ?", new Object[]{pkDateslot});
			for (BdCodeDateslotTime bdCodeDateslotTime : timeList) {
				bdCodeDateslotTime.setPkDateslot(pkDateslot);
				bdCodeDateslotTime.setPkDateslottime(null);
				DataBaseHelper.insertBean(bdCodeDateslotTime);
			}
		}
		if(secList != null){
			DataBaseHelper.execute("delete from BD_CODE_DATESLOT_SEC where PK_DATESLOT = ?", new Object[]{pkDateslot});
			for (BdCodeDateslotSecVO bdCodeDateslotSec : secList) {
				//?????????????????????????????????
				String secTime = bdCodeDateslotSec.getSecTime();
				if(StringUtils.isNotBlank(secTime)){
					String[] timeArr = secTime.split("???");
					bdCodeDateslotSec.setTimeBegin(timeArr[0]);
					bdCodeDateslotSec.setTimeEnd(timeArr[1]);
				}			
				bdCodeDateslotSec.setPkDateslot(pkDateslot);
				bdCodeDateslotSec.setPkDateslotsec(null);
				BdCodeDateslotSec lotSec = new BdCodeDateslotSec();
				ApplicationUtils.copyProperties(lotSec, bdCodeDateslotSec);
				DataBaseHelper.insertBean(lotSec);
			}
			
		}
		return bdCodeDateslot;
	}
	
	
	/**
	 * ?????????????????????
	 * @param param
	 * @param user
	 */
	@Transactional
	public void deleteBdCodeDateslot(String param , IUser user){
		String pkDateslot = JSON.parseObject(param).getString("pkDateslot");
		//??????????????????????????????????????????????????????
		int count = DataBaseHelper.queryForScalar("select count(1) from SCH_PLAN_WEEK "
				+ "where PK_DATESLOT = ? and DEL_FLAG = '0'",Integer.class,pkDateslot);
		if(count > 0) throw new BusException("?????????????????????????????????????????????");
		DataBaseHelper.execute("delete from BD_CODE_DATESLOT where PK_DATESLOT = ?", new Object[]{pkDateslot});
		DataBaseHelper.execute("delete from BD_CODE_DATESLOT_SEC where PK_DATESLOT = ?", new Object[]{pkDateslot});
		DataBaseHelper.execute("delete from BD_CODE_DATESLOT_TIME where PK_DATESLOT = ?", new Object[]{pkDateslot});
	}
	
	
	
	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 */
	public BdCodeDateslotSec saveBdCodeDateslotSec(String param , IUser user){
		
		BdCodeDateslotSec bdCodeDateslotSec = JsonUtil.readValue(param,BdCodeDateslotSec.class);
		if(bdCodeDateslotSec.getPkDateslotsec() == null){
			DataBaseHelper.insertBean(bdCodeDateslotSec);
		}else{
			DataBaseHelper.updateBeanByPk(bdCodeDateslotSec,false);
		}
		return bdCodeDateslotSec;
	}
	
	
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public BdCodeDateslotTime saveBdCodeDateslotTime(String param , IUser user){
		BdCodeDateslotTime bdCodeDateslotTime = JsonUtil.readValue(param,BdCodeDateslotTime.class);
		if(bdCodeDateslotTime.getPkDateslottime() == null){
			DataBaseHelper.insertBean(bdCodeDateslotTime);
		}else{
			DataBaseHelper.updateBeanByPk(bdCodeDateslotTime,false);
		}
		return bdCodeDateslotTime;
	}
	
	
	
	/**
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 */
/*	public BdAdminDivision saveBdAdminDivision(String param , IUser user){
		BdAdminDivision bdAdminDivision = JsonUtil.readValue(param,BdAdminDivision.class);
		Map<String,String> params = new HashMap<String, String>();
		params.put("pkDivision", bdAdminDivision.getPkDivision());
		params.put("codeDiv", bdAdminDivision.getCodeDiv());
		int count = codeMapper.BdAdminDivisionCheckExist(params);
		if(count > 0){
			throw new BusException("???????????????????????????");
		}
		if(bdAdminDivision.getPkDivision() == null){
			DataBaseHelper.insertBean(bdAdminDivision);
		}else{
			DataBaseHelper.updateBeanByPk(bdAdminDivision,false);
		}
		return bdAdminDivision;
	}*/
	
	public void saveBdAdminDivision(String param , IUser user){
		List<BdAdminDivision> divisionList = JsonUtil.readValue(param, new TypeReference<List<BdAdminDivision>>() {
		});
		Map<String,String> codeMap = new HashMap<String,String>();
		User loginUser = (User)user;
		/**??????????????????????????????????????????**/
		if(divisionList!=null && divisionList.size()>0){
			int len = divisionList.size();
			//??????????????????????????????????????????????????????
			for(int i = 0; i<len; i++){
				String code = divisionList.get(i).getCodeDiv();
				String pkDivision = divisionList.get(i).getPkDivision();
				if(codeMap.containsKey(code)){
					throw new BusException("???????????????????????????");
				}
			    codeMap.put(code,pkDivision);
			}
			
			//????????????????????????????????????????????????????????????????????????????????????
			for(String key:codeMap.keySet()){
				String pkDivision = codeMap.get(key);
				int count_code=-1;
				if(pkDivision!=null && !pkDivision.equals("")){
					count_code = DataBaseHelper.queryForScalar(
							"select count(1) from BD_ADMIN_DIVISION"+" where del_flag = '0' and CODE_DIV = ? and PK_DIVISION != ?", Integer.class,
							key,pkDivision);
				} else {
					count_code = DataBaseHelper.queryForScalar(
							"select count(1) from BD_ADMIN_DIVISION"+" where del_flag = '0' and CODE_DIV = ?", Integer.class,
							key);
				}
				
				if(count_code>0){
					throw new BusException("???????????????????????????");
				}
			}
			
			List<String> insertPk=new ArrayList<String>();//???????????????????????????????????????????????????
			
			/**???????????????????????????*/
			for(BdAdminDivision division : divisionList){
				if(division.getPkDivision() == null){
					division.setCreator(loginUser.getId());
					division.setCreateTime(new Date());
					division.setDelFlag("0");
					DataBaseHelper.insertBean(division);
					insertPk.add(division.getPkDivision());
				}else{
					DataBaseHelper.updateBeanByPk(division,false);
				}
			}
		}
	}

	
	/**
	 * ?????????????????????
	 * @param param
	 * @param user
	 */
	public void deleteBdAdminDivision(String param , IUser user){
		
		String pkDivision = JSON.parseObject(param).getString("pkDivision");
		
		int count = DataBaseHelper.queryForScalar("select count(1) from BD_ADMIN_DIVISION "
				+ "where PK_FATHER = ? and DEL_FLAG = '0'",Integer.class,pkDivision);
		if(count == 0){
			DataBaseHelper.update("update BD_ADMIN_DIVISION set del_flag = '1' where PK_DIVISION = ?", new Object[]{pkDivision});
		}else{
			throw new BusException("?????????????????????????????????");
		}
		
	}

	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 */
	public BdReportVo saveReport(String param , IUser user){

		BdReportVo report = JsonUtil.readValue(param, BdReportVo.class);
		//??????????????????????????????
		try {
			Base64 base64 = new Base64();
			if(report.getCondition()!=null)
				report.setCondition(new String(base64.decode(report.getCondition()), "UTF-8"));
	        if(report.getSql()!=null)
	        	report.setSql(new String(base64.decode(report.getSql()), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//String pkOrg = ((User)user).getPkOrg();
		if(report.getPkReport() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_report "
					+ "where del_flag = '0' and code = ? ", Integer.class, report.getCode());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_report "
					+ "where del_flag = '0' and name = ? ", Integer.class, report.getName());
			if(count_code == 0 && count_name == 0){
				BdReport rpt = new BdReport();
				ApplicationUtils.copyProperties(rpt, report);
				ApplicationUtils.setDefaultValue(rpt, true);
				DataBaseHelper.insertBean(rpt);
				report.setPkReport(rpt.getPkReport());
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("????????????????????????????????????");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("????????????????????????????????????");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("????????????????????????????????????????????????");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_report "
					+ "where del_flag = '0' and code = ?  and pk_report != ?", Integer.class, report.getCode(),report.getPkReport());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_report "
					+ "where del_flag = '0' and name = ?  and pk_report != ?", Integer.class, report.getName(),report.getPkReport());
			if(count_code == 0 && count_name == 0){
				BdReport rpt = new BdReport();
				ApplicationUtils.copyProperties(rpt, report);
				ApplicationUtils.setDefaultValue(rpt, false);
				DataBaseHelper.updateBeanByPk(rpt,false);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("????????????????????????????????????");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("????????????????????????????????????");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("????????????????????????????????????????????????");
				}
			}
		}
		//??????????????????
		if(report.getParams()!=null&&report.getParams().size()>0){
			//?????????????????????
			DataBaseHelper.execute("delete from bd_report_param where pk_report=?", report.getPkReport());
			for(BdReportParam paramvo:report.getParams()){
				paramvo.setPkReport(report.getPkReport());
				ApplicationUtils.setDefaultValue(paramvo, true);
				DataBaseHelper.insertBean(paramvo);
			}
		}else{//???????????????
			DataBaseHelper.execute("delete from bd_report_param where pk_report=?", report.getPkReport());
		}
		//????????????????????????????????????????????????CONDITION???????????????
		report.setCondition(null);
		return report;
	}
	
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 */
	public void delReport(String param , IUser user){
		String pkReport = JsonUtil.getFieldValue(param, "pkReport");
		DataBaseHelper.execute("delete from  bd_report_param where pk_report = ?", new Object[]{pkReport});
		DataBaseHelper.execute("delete from  bd_report  where pk_report = ?", new Object[]{pkReport});
	}
	
	
	/**
	 * ??????????????????<br>
	 * ????????????001002002035
	 * @param param
	 * @param user
	 */
	public BdTempPrt saveTempPrt(String param , IUser user){

		BdTempPrt tempprt = JsonUtil.readValue(param, BdTempPrt.class);
		if(tempprt!=null&&CommonUtils.isNull(tempprt.getPkOrg())){
			tempprt.setPkOrg("~");//?????????????????????
		}	
		if(tempprt.getPkTempprt() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_temp_prt "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%'", Integer.class, tempprt.getCode(),tempprt.getPkOrg());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_temp_prt "
					+ "where del_flag = '0' and name = ? and pk_org like ?||'%'", Integer.class, tempprt.getName(),tempprt.getPkOrg());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(tempprt);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("??????????????????????????????????????????");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("??????????????????????????????????????????");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("??????????????????????????????????????????????????????");
				}
			}
		}else{			
				DataBaseHelper.updateBeanByPk(tempprt,false);						
		}
		//????????????????????????????????????????????????data_rpt?????????
		tempprt.setDataRpt(null);
		return tempprt;
	}
	
	/**
	 * 
	 * ??????????????????<br>
	 * ????????????001002002055<br>
	 * <pre>
	 * 1???????????????????????????????????????????????????????????????????????????
	 * 2??????????????????????????????????????????????????????????????????????????????????????? 
	 * </pre>
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2017???2???27???
	 */
	public void saveTempPrtList(String param , IUser user){
		User u = UserContext.getUser();
		List<BdTempPrt> paramList = JsonUtil.readValue(param, new TypeReference<List<BdTempPrt>>() {
		});
		if(CollectionUtils.isNotEmpty(paramList)){
			String pkOrg = paramList.get(0).getPkOrg();
			if(pkOrg.startsWith("~")){
				throw new BusException("???????????????????????????????????????????????????");
			}else{
				int size = paramList.size();
				String[] codes = new String[size];
				for(int i=0;i<size;i++){
					codes[i] = paramList.get(i).getCode();
				}
				//??????????????????????????????????????????
				this.codeMapper.deleteBdTempPrtByCodes(pkOrg, codes);			
				
				//???????????????????????????????????????????????????
				//pkOrg???????????????
				String pubPkOrg = "~                               ";
				List<BdTempPrt> prtList = this.codeMapper.getBdTempPrtListByCodes(pubPkOrg, codes);
				for(BdTempPrt prt : prtList){
					prt.setPkTempprt(NHISUUID.getKeyId());
					prt.setPkOrg(pkOrg);
					prt.setCreator(u.getPkEmp());
					prt.setCreateTime(new Date());
					prt.setModifier(u.getPkEmp());
					prt.setDelFlag("0");
					prt.setTs(new Date());
				}
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdTempPrt.class),prtList);
			}				
		}else{
			throw new BusException("???????????????");
		}
	}
	
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getTempPrtByPk(String param , IUser user){
		
		String pkTempprt = JsonUtil.getFieldValue(param, "pkTempprt");
		
		Map<String, Object> temp = DataBaseHelper.queryForMapFj("select * from bd_temp_prt where del_flag='0' and pk_tempprt = ?", pkTempprt);
		
		return temp;
	}
	/**
	 * ?????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getTempPrtByPkFromOrdex(String param,IUser user){
		String pkTempprt = JsonUtil.getFieldValue(param, "pkTempprt");
		
		Map<String, Object> temp = DataBaseHelper.queryForMapFj("select pk_tempordex as pk_tempprt,pk_org,code,name,spcode,data_temp as data_rpt,printer from bd_temp_ordex  where del_flag='0' and pk_tempordex = ?", pkTempprt);
		
		return temp;
	}
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public BdReportVo getReportByPk(String param , IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkReport = null;
		BdReportVo report = null;
		StringBuilder sb  = new StringBuilder("select * from bd_report where del_flag='0' ");
		if(paramMap.get("pkReport") != null){
			pkReport = paramMap.get("pkReport").toString();
			report = DataBaseHelper.queryForBean(sb.append(" and pk_report = ?").toString(), BdReportVo.class, pkReport);
		}else{
			List<Object> paramList = new ArrayList<Object>();
			if(paramMap.get("code") != null){
				paramList.add(paramMap.get("code"));
				sb.append(" and code  = ? ");
			}
			if(paramMap.get("pkOrg") != null){
				paramList.add(paramMap.get("pkOrg"));
				sb.append(" and pk_org  = ? ");
			} 
			report = DataBaseHelper.queryForBean(sb.toString(), BdReportVo.class,paramList.toArray(new Object[0]));
		}
		if(report!=null&&!CommonUtils.isEmptyString(report.getPkReport())){
			paramMap.put("pkReport", report.getPkReport());
			report.setParams(DataBaseHelper.queryForList("select * from bd_report_param where pk_report = :pkReport order by sort_no ", BdReportParam.class, paramMap));
		}
		
		return report;
	}
	
	/**
	 * ????????????????????????
	 * @param param
	 * @param user
	 */
	public void saveSysparamList(String param, IUser user){
		List<BdSysparam> sysparamList = JsonUtil.readValue(param, new TypeReference<List<BdSysparam>>() {
		});
		String pkEmp = ((User) user).getPkEmp();
		
		if(sysparamList != null && sysparamList.size() != 0){
			for(BdSysparam sysparam : sysparamList){
				sysparam.setModifier(pkEmp);
				sysparam.setTs(new Date());
			}
			String sql = "update bd_sysparam set code =:code, name =:name, val =:val, "
					+ "desc_param =:descParam, note =:note, modifier =:modifier, del_flag =:delFlag, ts =:ts "
					+ "where pk_sysparam =:pkSysparam";
			DataBaseHelper.batchUpdate(sql, sysparamList);
			//RedisUtils.delCache("sys:sysparam");
			for(BdSysparam sysparam : sysparamList){
				if(CommonUtils.isEmptyString(sysparam.getPkOrg())) 
					continue;
				else if(sysparam.getPkOrg().contains("~"))
					RedisUtils.delCache("sys:sysparam:public");
				else
					RedisUtils.delCache("sys:sysparam:"+sysparam.getPkOrg());
			}
		}
	}
	
	/**
	 * trans code 001002002046
	 * ????????????????????????
	 * @param param
	 * @param user
	 */
	public void saveSysparamTemp(String param , IUser user){

		//BdSysparamTemp paramTemp = JsonUtil.readValue(param, BdSysparamTemp.class);
		BdSysTempSaveParam paramTempList = JsonUtil.readValue(param, BdSysTempSaveParam.class);
		BdSysparamTemp paramTemp = paramTempList.getEditParam();
		List<BdSysparamExt> sysparamList = paramTempList.getSysparamList();
		//????????????????????????????????????
		List<BdSysparamVO> delsysparamList = paramTempList.getDelsysparamList();
		if(StringUtils.isEmpty(paramTemp.getFlagPub())) paramTemp.setFlagPub("0");
		
    	List<String> delSysparamSql = new ArrayList<String>();
		String PkDeptListStr = "'";
		if(delsysparamList.size() > 0){ //??????????????????
			for(int i = 0; i < delsysparamList.size(); i++){
				BdSysparamVO delsysparam = delsysparamList.get(i);
                //String delSql = "delete from bd_sysparam where pk_sysparam = ? ";
                //DataBaseHelper.execute(delSql,delsysparam.getPkSysparam());
				delSysparamSql.add("delete from bd_sysparam where pk_sysparam = '"+delsysparam.getPkSysparam()+"'");
				if(i < delsysparamList.size() -1){
					PkDeptListStr += delsysparam.getPkDept() + "','";
				}else{
					PkDeptListStr += delsysparam.getPkDept() + "'";
				}
			}
		}
		//????????????
        if(delSysparamSql!= null && delSysparamSql.size() > 0){
           DataBaseHelper.batchUpdate(delSysparamSql.toArray(new String[0]));
        }
		
		if(paramTemp.getPkParamtemp() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_sysparam_temp "
					+ "where del_flag = '0' and code = ?", Integer.class, paramTemp.getCode());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_sysparam_temp "
					+ "where del_flag = '0' and name = ?", Integer.class, paramTemp.getName());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(paramTemp);
				
				if (sysparamList != null){
					for (BdSysparam bdSysparam : sysparamList) {
						DataBaseHelper.update("update bd_sysparam set val= ?  where pk_sysparam= ?", new Object[]{bdSysparam.getVal(),bdSysparam.getPkSysparam()});
					}
				}
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("???????????????????????????????????????");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("???????????????????????????????????????");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("???????????????????????????????????????????????????");
				}
			}
		}else{
			String name = "";
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_sysparam_temp "
					+ "where del_flag = '0' and code = ? and pk_paramtemp != ?", Integer.class, paramTemp.getCode(),paramTemp.getPkParamtemp());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_sysparam_temp "
					+ "where del_flag = '0' and name = ? and pk_paramtemp != ?", Integer.class, paramTemp.getName(),paramTemp.getPkParamtemp());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(paramTemp,false);
				if (sysparamList != null){
					for (BdSysparamExt bdSysparam : sysparamList) {
						if(null == bdSysparam.getPkSysparam()) {//??????bug 33965
							DataBaseHelper.update("update bd_res_pc_argu set arguval= ?  where pk_pcargu= ?", new Object[]{bdSysparam.getVal(),bdSysparam.getPkPcargu()});
						} else {
							DataBaseHelper.update("update bd_sysparam set val= ?  where pk_sysparam= ?", new Object[]{bdSysparam.getVal(),bdSysparam.getPkSysparam()});
						}
						if("1".equals(paramTemp.getEuRange())) {//?????????
							name="sys:sysparam:"+bdSysparam.getPkOrg();
							RedisUtils.delCache(name);
						}else if("0".equals(paramTemp.getEuRange())){//?????????                       //?????????
							name="sys:sysparam:public";
							RedisUtils.delCache(name);
						}else{
							name="sys:sysparam:"+bdSysparam.getPkOrg();
							RedisUtils.delCache(name);
						}
					}
				}

			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("???????????????????????????????????????");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("???????????????????????????????????????");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("???????????????????????????????????????????????????");
				}
			}
		}

		if(delsysparamList.size() > 0){
				String delSql = "delete from bd_res_pc_argu where pk_dept in ("+PkDeptListStr+") and code_argu = ?";
				DataBaseHelper.execute(delSql,paramTemp.getCode());
		}
	}
	
	/**
	 * ????????????????????????
	 * @param param
	 * @param user
	 */
	public void delSysparamTemp(String param , IUser user){
		
		String pkParamtemp = JsonUtil.getFieldValue(param, "pkParamtemp");
		
		Map<String, Object> tempMap = DataBaseHelper.queryForMap("select code,name from bd_sysparam_temp where del_flag = '0' and pk_paramtemp = ?", pkParamtemp);
		if(tempMap != null){
			Integer param_count = DataBaseHelper.queryForScalar("select count(1) from bd_sysparam where del_flag = '0' and code = ? and name = ?", Integer.class, tempMap.get("code").toString(), tempMap.get("name").toString());
			if(param_count == 0){
				DataBaseHelper.update("update bd_sysparam_temp set del_flag = '1' where pk_paramtemp = ?", new Object[]{pkParamtemp});
			}else{
				throw new BusException("?????????????????????????????????????????????");
			}
		}else{
			throw new BusException("?????????????????????");
		}
	}
	
	/**
	 * ????????????????????????
	 * @param param
	 * @param user
	 */
	public void releaseSysparamTemp(String param , IUser user){

		String pkParamtemp = JsonUtil.getFieldValue(param, "pkParamtemp");
		List<BdSysparam> paramlist = DataBaseHelper.queryForList("select * from bd_sysparam where del_flag = '0' and pk_paramtemp = ?", BdSysparam.class, pkParamtemp);
		if(paramlist!=null && paramlist.size()!=0){//??????????????????????????????????????????????????????????????????????????????
			DataBaseHelper.execute("delete from bd_sysparam where pk_paramtemp = ?", pkParamtemp);
		}
		Map<String, Object> releaseTmpMap = DataBaseHelper.queryForMap("select * from bd_sysparam_temp where del_flag = '0' and pk_paramtemp = ?", pkParamtemp);
		if(releaseTmpMap != null){
			BdSysparam sysparam = new BdSysparam();
			sysparam.setCode(releaseTmpMap.get("code").toString());
			sysparam.setName(releaseTmpMap.get("name").toString());
			sysparam.setVal(null == releaseTmpMap.get("valDef") ? null: releaseTmpMap.get("valDef").toString());
			sysparam.setDescParam(releaseTmpMap.get("descParam")==null?null:releaseTmpMap.get("descParam").toString());
			sysparam.setNote(releaseTmpMap.get("note")==null?null:releaseTmpMap.get("note").toString());
			sysparam.setPkParamtemp(pkParamtemp);
			String euRange = releaseTmpMap.get("euRange").toString();
			String name="";
			if("1".equals(euRange)){//?????????
				List<Map<String, Object>> orgList = DataBaseHelper.queryForList("select * from BD_OU_ORG where del_flag = '0' and pk_org not like '~%'");
				for(Map<String, Object> org : orgList){
					sysparam.setPkSysparam(null);
					sysparam.setPkOrg(org.get("pkOrg").toString());
					DataBaseHelper.insertBean(sysparam);
					
					name="sys:sysparam:"+sysparam.getPkOrg();
					RedisUtils.delCache(name);

				}
			}else if("0".equals(euRange)){                       //?????????
				sysparam.setPkOrg("~                               ");
				DataBaseHelper.insertBean(sysparam);
				name="sys:sysparam:public";
				RedisUtils.delCache(name);
			}else{
				sysparam.setPkOrg(((User)user).getPkOrg());
				//BUG25200 ????????????????????????????????????????????????bd_sysparam
				//DataBaseHelper.insertBean(sysparam);
				name="sys:sysparam:"+sysparam.getPkOrg();
				RedisUtils.delCache(name);
			}
			
			//???????????????????????????????????????
			DataBaseHelper.update("update bd_sysparam_temp set flag_pub = '1' where pk_paramtemp = ?", new Object[]{pkParamtemp});
			
			//RedisUtils.delCache("sys:sysparam");
			//??????????????????????????????????????????????????????
			if( ("0".equals(euRange) || "1".equals(euRange)) && sysparam.getCode() != null && !"".equals(sysparam.getCode())){
				String delSql = "delete from bd_res_pc_argu where code_argu = ?";
				DataBaseHelper.execute(delSql,sysparam.getCode());
			}
			
		}else{
			throw new BusException("?????????????????????");
		}
		
		
	}
	
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public BdEncoderule saveBdEncoderule(String param , IUser user){
		
		BdEncoderule coderule = JsonUtil.readValue(param, BdEncoderule.class);
		String pkOrg = ((User) user).getPkOrg();
		
		if(coderule.getPkEncoderule() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_encoderule "
					+ "where del_flag = '0' and code = ? and pk_org = ?", Integer.class, coderule.getCode(),pkOrg);
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_encoderule "
					+ "where del_flag = '0' and name = ? and pk_org = ?", Integer.class, coderule.getName(),pkOrg);
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(coderule);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("??????????????????????????????????????????");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("??????????????????????????????????????????");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("??????????????????????????????????????????????????????");
				}
			}
		}else{
			if("1".equals(coderule.getFlagSys())){//??????????????????????????????
				throw new BusException("?????????????????????????????????");
			}else{
				int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_encoderule "
						+ "where del_flag = '0' and code = ? and pk_org = ? and pk_encoderule != ?", Integer.class, coderule.getCode(),pkOrg,coderule.getPkEncoderule());
				int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_encoderule "
						+ "where del_flag = '0' and name = ? and pk_org = ? and pk_encoderule != ?", Integer.class, coderule.getName(),pkOrg,coderule.getPkEncoderule());
				if(count_code == 0 && count_name == 0){
					DataBaseHelper.updateBeanByPk(coderule,false);
				}else{
					if(count_code != 0 && count_name == 0){
						throw new BusException("??????????????????????????????????????????");
					}
					if(count_code == 0 && count_name != 0){
						throw new BusException("??????????????????????????????????????????");
					}
					if(count_code != 0 && count_name != 0){
						throw new BusException("??????????????????????????????????????????????????????");
					}
				}
			}
		}
		return coderule;
	}
	
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 */
	public void delBdEncoderule(String param , IUser user){
		
		String pkEncoderule = JsonUtil.getFieldValue(param, "pkEncoderule");
		
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select * from bd_encoderule where del_flag = '0' and pk_encoderule = ?", pkEncoderule);
		if(queryForMap != null){
			int val_init = Integer.parseInt(queryForMap.get("valInit").toString());//?????????
			Long val = Long.parseLong(queryForMap.get("val").toString());//?????????
			String flagSys = "";
			if(queryForMap.get("flagSys") != null){
				flagSys = queryForMap.get("flagSys").toString(); //??????????????????
			}
			if("1".equals(flagSys)){
				throw new BusException("??????????????????????????????????????????");
			}else if(val == val_init){
				DataBaseHelper.update("update bd_encoderule set del_flag = '1' where pk_encoderule = ?", new Object[]{pkEncoderule});
			}else{
				throw new BusException("???????????????????????????????????????");
			}
		}else{
			throw new BusException("???????????????????????????");
		}
	}
	/**
	 * ???????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdReport> getReportListByUsergrp(String param,IUser user){
		if(user==null) return null;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkUser", user.getId());
		
		List<String> pkReports=codeMapper.getReportByUser(paramMap);
		String sql="select * from bd_report where pk_report in ("+CommonUtils.convertListToSqlInPart(pkReports)+")";
		return DataBaseHelper.queryForList(sql, BdReport.class, new Object[]{});
	}
	
	//???????????????001002002011
	public List<Map<String, Object>> searchWorkcalendars(String param,IUser user){
		
		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
		
		return codeMapper.searchWorkcalendars(pkOrg);
	}
	
	//???????????????????????????001002002010
	public List<Map<String, Object>> searchWorkRules(String param,IUser user){
		
		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
		String pkWorkcalendrule = JsonUtil.getFieldValue(param, "pkWorkcalendrule");
		
		return codeMapper.searchWorkRules(pkOrg,pkWorkcalendrule);
	}
	//????????????????????????????????????:001002002019
	public List<Map<String, Object>> getDatesSecByDates(String param,IUser user){
		
		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
		String pkDateslot = JsonUtil.getFieldValue(param, "pkDateslot");
		
		return codeMapper.getDatesSecByDates(pkOrg,pkDateslot);
	}
	//????????????????????????????????????:001002002020
	public List<Map<String, Object>> getDatesTimeByDates(String param,IUser user){
		
		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
		String pkDateslot = JsonUtil.getFieldValue(param, "pkDateslot");
		
		return codeMapper.getDatesTimeByDates(pkOrg,pkDateslot);
	}

	/**
	 * ????????????????????????????????????????????????
     * ????????????001002002105
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getTempPrtList(String param , IUser user){

	    String dtOrdExType = JsonUtil.getFieldValue(param, "dtOrdExType");
		String dtPrttemp = JsonUtil.getFieldValue(param, "dtPrtTemp");
		String codeDefDocList = JsonUtil.getFieldValue(param, "codeDefDocList");
		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
		String printCode = JsonUtil.getFieldValue(param, "printCode");
		Map<String,Object> map = new HashMap<>();
		map.put("codeDefDocList",codeDefDocList);
		map.put("dtPrtTemp",dtPrttemp);
        map.put("dtOrdExType",dtOrdExType);
		map.put("pkOrg",pkOrg);
        map.put("printCode",printCode);
		List<Map<String,Object>> result = codeMapper.getTempPrtList(map);

		return result;
	}

	/**
	 * ??????????????????????????????????????????
	 * 001002002106
	 * @param
	 * @return
	 */
	public List<Map<String, Object>> getOrgListByPkParamTemp(String param,IUser user){
		//String pkParamTemp = JsonUtil.getFieldValue(param, "pkParamTemp");
		//List<Map<String,Object>> result = codeMapper.getOrgListByPkParamTemp(pkParamTemp);
		String code = JsonUtil.getFieldValue(param, "code");
		List<Map<String,Object>> result = codeMapper.getOrgListByCodeTemp(code);

		return result;
	}

  /**
   * 001002002108
   *????????????
   * @param param
   * @param user
   */
  public void deleteOrgList(String param, IUser user) {
		String pkParamTemp = JsonUtil.getFieldValue(param, "pkParamTemp");
		codeMapper.deleteOrgList(pkParamTemp);
        int count = DataBaseHelper.queryForScalar("select count(1) from bd_sysparam " +
              "where pk_paramtemp = ?", Integer.class,pkParamTemp);
        if(count == 0){
            DataBaseHelper.update("update bd_sysparam_temp set flag_pub = '0' where pk_paramtemp = ?", new Object[]{pkParamTemp});
        }
	}
}
