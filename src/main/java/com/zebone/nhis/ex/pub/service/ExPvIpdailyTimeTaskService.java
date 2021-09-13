package com.zebone.nhis.ex.pub.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pv.PvIpDaily;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.dao.ExPvIpdailyMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class ExPvIpdailyTimeTaskService {
	@Resource
	private ExPvIpdailyMapper exPvIpdailyMapper;
	
	private List<String> pkDeptArr=new ArrayList<String>();
	
	//处理输入参数
	public Map<String, Object> doInparamDeptReport(String[] pkOrgArr){
		String [] dateArr=DateUtils.getDate().split("-");
		String dateSa=dateArr[0]+dateArr[1]+dateArr[2]+"000000";
		Map<String, Object> map=new HashMap<String,Object>();
		pkDeptArr=exPvIpdailyMapper.queryDeptByOrg(pkOrgArr);//查询该机构下有多少病区
		if(pkDeptArr.size()==0||pkDeptArr==null){
			throw new BusException("未得到病区集合");
		}
		map.put("pkDepts",pkDeptArr);
		String dateEnd = "";
		String yesBegin = "";
		String yesEnd = "";
		//前前一天，用来统计期初人数
		String yesBeforeBegin = "";
		String yesBeforeEnd = "";
		if(!"".equals(dateSa)){
			dateEnd = dateSa.substring(0,8)+"235959";
			map.put("dateCurEnd", dateEnd);
			map.put("dateCurBegin", dateSa);
			try {
				yesBegin = DateUtils.getSpecifiedDateStr(DateUtils.getDefaultDateFormat().parse(dateSa), -1);
				map.put("yesBegin", yesBegin+"000000");
				yesBeforeBegin = DateUtils.getSpecifiedDateStr(DateUtils.getDefaultDateFormat().parse(dateSa), -2);
			} catch (ParseException e) {
				throw new BusException("转换传入的日期异常");
			}
			yesEnd = yesBegin+"235959";
			yesBeforeEnd = yesBeforeBegin+"235959";
			map.put("yesEnd", yesEnd);
			map.put("yesBeforeEnd", yesBeforeEnd);
		}
		return map;
	}

	//统计病区日报
	public void statDeptReport(String[] pkOrgArr){
		Map<String,Object> map=doInparamDeptReport(pkOrgArr);
		List<Map<String,Object>> bednumList=exPvIpdailyMapper.getBedNumByDept(map);//额定，实际，病区
		List<Map<String,Object>> inhospYdList=exPvIpdailyMapper.getQichuNumByDept(map);//期初=昨日留院，病区
		List<Map<String,Object>> admitList=exPvIpdailyMapper.getDayInNumByDept(map);//今日入院
		List<Map<String,Object>> dischargeList=exPvIpdailyMapper.getDayOutNumByDept(map);//今日出院
		List<Map<String,Object>> transOutList=exPvIpdailyMapper.getDeptAdtOutNumByDept(map);//转往他科
		List<Map<String,Object>> transInList=exPvIpdailyMapper.getDeptAdtInByDept(map);//他科转入
		List<Map<String,Object>> severenumList=exPvIpdailyMapper.getBzNumByDept(map);//病重人数
		List<Map<String,Object>> riskynumList=exPvIpdailyMapper.getBwNumByDept(map);//病危人数
		List<Map<String, Object>> deathnumList=exPvIpdailyMapper.getDeathByDept(map);//死亡人数
		map.put("hldj",ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_I", "") );
		List<Map<String,Object>> nurseFirstList=exPvIpdailyMapper.getHLNumByDept(map);//一级护理
		map.put("hldj",ApplicationUtils.getPropertyValue("ORDCODE_HLDJ_T", "") );
		List<Map<String,Object>> nurseSpecList=exPvIpdailyMapper.getHLNumByDept(map);//特级护理
		List<Map<String,Object>> accomnumList=exPvIpdailyMapper.getAttendNumByDept(map);//留陪人数
		for (int i = 0; i < pkDeptArr.size(); i++) {
			PvIpDaily pvIpDaily=new PvIpDaily();
			pvIpDaily.setPkDeptNs(pkDeptArr.get(i));
			if(bednumList.size()!=0 && bednumList!=null){
				for (Map<String, Object> bednum : bednumList) {
					if(bednum.get("pkDept").equals(pkDeptArr.get(i))){
						
						pvIpDaily.setBednum(new BigDecimal(bednum.get("bednum").toString()));
						pvIpDaily.setBednumOpen(new BigDecimal(bednum.get("bednumOpen").toString()));
						break;
					}
				}
			}
			if(inhospYdList.size()!=0 && inhospYdList!=null){
				for (Map<String, Object> inhospyd : inhospYdList) {
					if(inhospyd.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setInhospYd(new BigDecimal(inhospyd.get("inhospyd").toString()));
						break;
					}
				}
			}
			if(admitList.size()!=0 && admitList!=null){
				for (Map<String, Object> admit : admitList) {
					if(admit.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setAdmit(new BigDecimal(admit.get("admit").toString()));
						break;
					}
				}
			}
			if(dischargeList.size()!=0 && dischargeList!=null){
				for (Map<String, Object> discharge : dischargeList) {
					if(discharge.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setDischarge(new BigDecimal(discharge.get("discharge").toString()));
						break;
					}
				}
			}
			if(transOutList.size()!=0 && transOutList!=null){
				for (Map<String, Object> transOut : transOutList) {
					if(transOut.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setTransOut(new BigDecimal(transOut.get("transout").toString()));
						break;
					}
				}
			}
			if(transInList.size()!=0 && transInList!=null){
				for (Map<String, Object> transIn : transInList) {
					if(transIn.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setTransIn(new BigDecimal(transIn.get("transin").toString()));
						break;
					}
				}
			}
			if(severenumList.size()!=0 && severenumList!=null){
				for (Map<String, Object> severenum : severenumList) {
					if(severenum.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setSeverenum(new BigDecimal(severenum.get("severenum").toString()));
						break;
					}
				}
			}
			if(riskynumList.size()!=0 && riskynumList!=null){
				for (Map<String, Object> riskynum : riskynumList) {
					if(riskynum.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setRiskynum(new BigDecimal(riskynum.get("riskynum").toString()));
						break;
					}
				}
			}
			if(deathnumList.size()!=0 && deathnumList!=null){
				for (Map<String, Object> deathnum : deathnumList) {
					if(deathnum.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setDeathnum(new BigDecimal(deathnum.get("deathnum").toString()));
						break;
					}
				}
			}
			if(nurseFirstList.size()!=0 && nurseFirstList!=null){
				for (Map<String, Object> nurseFirst : nurseFirstList) {
					if(nurseFirst.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setNurseFirst(new BigDecimal(nurseFirst.get("nurseFirst").toString()));
						break;
					}
				}
			}
			if(nurseSpecList.size()!=0 && nurseSpecList!=null){
				for (Map<String, Object> nurseSpec : nurseSpecList) {
					if(nurseSpec.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setNurseSpec(new BigDecimal(nurseSpec.get("nurseSpec").toString()));
						break;
					}
				}
			}
			if(accomnumList.size()!=0 && accomnumList!=null){
				for (Map<String, Object> accomnum : accomnumList) {
					if(accomnum.get("pkDeptNs").equals(pkDeptArr.get(i))){
						pvIpDaily.setAccomnum(new BigDecimal(accomnum.get("accomnum").toString()));
						break;
					}
				}
			}
			if(pvIpDaily!=null){
				BigDecimal inhospYd=pvIpDaily.getInhospYd()!=null ? pvIpDaily.getInhospYd():new BigDecimal(0.00);
				BigDecimal admit=pvIpDaily.getAdmit()!=null ? pvIpDaily.getAdmit():new BigDecimal(0.00);
				BigDecimal transIn=pvIpDaily.getTransIn()!=null ? pvIpDaily.getTransIn():new BigDecimal(0.00);
				BigDecimal transOut=pvIpDaily.getTransOut()!=null ? pvIpDaily.getTransOut():new BigDecimal(0.00);
				BigDecimal discharge=pvIpDaily.getDischarge()!=null ? pvIpDaily.getDischarge():new BigDecimal(0.00);
				pvIpDaily.setInhosp(inhospYd.add(admit).add(transIn).subtract(transOut).subtract(discharge));
				pvIpDaily.setDateSa(DateUtils.getDateMorning(new Date(), 0));
				DataBaseHelper.insertBean(pvIpDaily);
			}
		}
	}
}
