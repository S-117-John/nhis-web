package com.zebone.nhis.webservice.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotSec;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotTime;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.vo.LBQueSchVo;
import com.zebone.nhis.webservice.vo.LbBmptSchedule;
import com.zebone.nhis.webservice.vo.LbBmptTimeInfo;
import com.zebone.nhis.webservice.vo.LbQueSchCount;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.SchForExtVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;
/**
 * ?????????????????????webservcie????????????
 * @author yangxue
 *
 */
@Service
public class SchPubForWsService {
	
	@Resource
	private SchPubForWsMapper schPubForWsMapper;

	private User u  = new User();
	/**
	 * ????????????????????????
	 * @param paramMap{pkOrg,??????}
	 * @return
	 */
	public List<SchForExtVo> getAppSchInfo(Map<String,Object> paramMap){
		ApplicationUtils apputil = new ApplicationUtils();
		u.setPkOrg(CommonUtils.getString(paramMap.get("pkOrg")));
		ResponseJson  rs =  apputil.execService("SCH", "SchExtPubService", "getSchInfo", paramMap,u);
		return (List<SchForExtVo>)rs.getData();
	}
	/**
	 * ????????????????????????(??????)
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getAppSchInfoForSelf(Map<String,Object> paramMap){
		ApplicationUtils apputil = new ApplicationUtils();
		u.setPkOrg(CommonUtils.getString(paramMap.get("pkOrg")));
		ResponseJson  rs =  apputil.execService("SCH", "SchExtPubService", "getSchInfoForSelf", paramMap,u);
		return (List<Map<String,Object>>)rs.getData();
	}
	/**
	 * ????????????????????????
	 * @param  pkOrg ??????
	 * @return
	 */
	public  List<Map<String, Object>> getSchInfo(String pkOrg){
		u.setPkOrg(pkOrg);
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson  rs =  apputil.execService("SCH", "SchService", "getSchInfoByResAndTime",null,u);
		return (List<Map<String, Object>>)rs.getData();
	}
	/**
	 * ????????????????????????
	 * @param paramMap{pkSch?????????}
	 * @return
	 */
	public Map<String,Object> getTickets(Map<String,Object> paramMap){
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson  rs =  apputil.execService("SCH", "SchApptService", "getSchTicketInfo",paramMap,u);
		return (Map<String,Object>)rs.getData();
	}
	/**
	 * ????????????????????????--??????????????????
	 * @param paramMap{pkPi,pkSch,ticketNo}
	 * @return
	 */
	public Map<String,Object> saveAppointment(Map<String,Object> paramMap){
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson  rs =  apputil.execService("SCH", "SchExtPubService", "applyRegister",paramMap,u);
		return (Map<String,Object>)rs.getData();
	}
	
	/**
	 * ??????????????????
	 * @param pkSchappt
	 * @return
	 */
	public Map<String,Object> cancelAppointment(Map<String,Object> paramMap){
		ApplicationUtils apputil = new ApplicationUtils();
		u.setPkEmp("extwebservice");
		u.setNameEmp("extwebservice");
		ResponseJson  rs =  apputil.execService("SCH", "SchExtPubService", "cancelApplyRegister",paramMap,u);
		return (Map<String,Object>)rs.getData();
	}
	
	/**
	 * ????????????
	 * @param requ
	 * @param ticket
	 * @return
	 */
	public Map<String,Object> lockReg(Map<String,Object> paramMap,User user) {
		Map<String, Object> result = new HashMap<>(16);
		SchTicket ticket = null;
		String pkSch = CommonUtils.getPropValueStr(paramMap, "pkSch");
		String sql = "select * from sch_sch where del_flag = '0' and pk_sch = ?";
		SchSch schSch = DataBaseHelper.queryForBean(sql,SchSch.class, pkSch);
		// 1.???????????????????????????
		if (schSch == null || "1".equals(schSch.getFlagStop())){
			result.put("message", "???????????????????????????????????????");
			result.put("result", "false");
			return result;
		}
		if(schSch.getCntTotal().intValue()<=schSch.getCntUsed().intValue()){
			result.put("message", "??????????????????????????????");
			result.put("result", "false");
			return result;
		}
		//??????????????????
		//?????? ????????? ???????????????????????????????????????????????????
		boolean flagTicket = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
				Integer.class, new Object[]{pkSch})>0;
		//2.????????????
		if(StringUtils.isNotBlank(CommonUtils.getPropValueStr(paramMap, "ticketno"))){
			List<SchTicket> tickets = DataBaseHelper.queryForList(
					"select * from sch_ticket where pk_sch = ? and ticketno=? and DEL_FLAG = '0' and FLAG_USED = '1'",
					SchTicket.class, pkSch,CommonUtils.getPropValueStr(paramMap, "ticketno"));
			ticket = tickets.get(0);
		}else if(flagTicket){
			List<SchTicket> tickets = new ArrayList<>();
			//??????????????????????????????
			if(CommonUtils.isNotNull(CommonUtils.getPropValueStr(paramMap, "pkDateslotsec"))){
				Map<String, Object> slotSecMap = DataBaseHelper.queryForMap("select * from bd_code_dateslot_sec where pk_dateslotsec=?", CommonUtils.getPropValueStr(paramMap, "pkDateslotsec"));
				//??????map????????????
				if(BeanUtils.isNotNull(slotSecMap)){
					tickets = DataBaseHelper.queryForList(
							"select * from sch_ticket where pk_sch = ? and TO_CHAR (begin_time, 'hh24:mi:ss') >=? and TO_CHAR (end_time, 'hh24:mi:ss') <=?  and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
							SchTicket.class, pkSch,slotSecMap.get("timeBegin"),slotSecMap.get("timeEnd"));
				}else{
					tickets = DataBaseHelper.queryForList(
							"select * from sch_ticket where pk_sch = ? and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
							SchTicket.class, pkSch);
				}
				
			}else{
				tickets = DataBaseHelper.queryForList(
						"select * from sch_ticket where pk_sch = ? and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
						SchTicket.class, pkSch);
			}
			
			if(tickets==null||tickets.size()<=0){
				result.put("message", "??????????????????????????????");
				result.put("result", "false");
				return result;
			}
			ticket = tickets.get(0);
			Map<String, Object> schTicketMap = new HashMap<String, Object>();
			//????????????
			schTicketMap.put("pkSchticket", ticket.getPkSchticket());
			//?????????????????????
			schTicketMap.put("flagUsed", "1");
			//???????????????
			schTicketMap.put("flagUseds", "0");
			//??????????????????
			int cnt = schPubForWsMapper.updateSchTicketFlagUsed(schTicketMap);
			if(cnt<=0){
				result.put("message", "???????????????????????????????????????????????????");
				result.put("result", "false");
				return result;
			}

		}else{//??????????????? ??????????????????
			//????????????????????????????????????????????????
			int cnt = schPubForWsMapper.updateSchSchTicketNo(pkSch);
			if(cnt<=0){
				result.put("message", "????????????????????????????????????");
				result.put("result", "false");
				return result;
			}
			ticket =  new SchTicket();
			SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
			ticket.setTicketno(sch.getTicketNo());
			ticket.setPkSch(pkSch);
		}

		//??????????????????
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkSchticket", ticket.getPkSchticket());
		List<Map<String, Object>> listMap = schPubForWsMapper.querySchInfo(map);

		BigDecimal amtAcc = BigDecimal.ZERO;
		if(0 != listMap.size()){
			for (int i = 0; i < listMap.size(); i++) {
				if(null != listMap.get(i).get("price")){
					Double pric = Double.parseDouble(listMap.get(i).get("price").toString());
					BigDecimal amt=BigDecimal.valueOf(pric);
					amtAcc=amtAcc.add(amt);
				}
			}
		}
		Map<String, Object> schResMap = DataBaseHelper.queryForMap("select dept.code_dept,dept.pk_dept,dept.name_dept from sch_resource res inner JOIN bd_ou_dept dept ON dept.pk_dept = res.pk_dept_belong  where res.pk_schres= ? AND res.eu_schclass = '0' ",
				schSch.getPkSchres());
		result.put("price", amtAcc);//???????????????
		result.put("ticketno", ticket.getTicketno());//?????????
		result.put("pkSchticket", ticket.getPkSchticket());//????????????
		result.put("pkDept", CommonUtils.getPropValueStr(schResMap, "pkDept"));//??????id
		result.put("nameDept", CommonUtils.getPropValueStr(schResMap, "nameDept"));//????????????
		result.put("codeDept", CommonUtils.getPropValueStr(schResMap, "codeDept"));//????????????
		result.put("pkSchsrv", schSch.getPkSchsrv());//??????????????????
		
		return result;
	}
	
	/**
	 * ???????????????????????????xml????????????
     * ???????????????????????????????????????
     * @param paramMap
     * @return
     */
    public List<LBQueSchVo> getQuerySch(LbSHRequestVo requ,User user) {    	
    	
    	List<LBQueSchVo> queSchList = new ArrayList<>();
    	DateTime dt = DateTime.now();
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkOrg", user.getPkOrg());
		boolean timDouble=true;
		//??????????????????????????????
		String ifFilter = ApplicationUtils.getSysparam("SCH0016", false);
		//?????????????????????????????????
		if(BeanUtils.isNotNull(requ.getEndDate()) && BeanUtils.isNotNull(requ.getStartDate())){
			paramMap.put("StartDate", requ.getStartDate());// ???????????????
			paramMap.put("EndDate", requ.getEndDate());// ??????????????????
			paramMap.put("total","0");
			timDouble=false;
		}else {
			//???????????????????????????????????????????????????????????????
			//??????????????????????????????????????????
			if(BeanUtils.isNotNull(requ.getRegDate()) && requ.getRegDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
				paramMap.put("nowDate", requ.getRegDate());
				paramMap.put("total","0");
				timDouble=false;
			}else{
				paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// ???????????????
			}
		}
		paramMap.put("codeDept", requ.getDeptCode());// ????????????
		paramMap.put("doctCode", requ.getDoctCode());// ????????????
		paramMap.put("flagStop", "0");//????????????
    	List<Map<String, Object>> schpubList = schPubForWsMapper.LbTodaySchInfosByDate(paramMap);
    	for (Map<String, Object> schpubMap:schpubList){
    		if(timDouble){
    			schpubMap.put("time", dt.toString("HH:mm:ss"));// ????????????
    		}
    		//???????????????????????????????????????
    		if("1".equals(ifFilter)) {
    			schpubMap.put("dtApptype", "0");//??????????????????????????? 	
    		}
    		if(StringUtils.isBlank(LbSelfUtil.getPropValueStr(schpubMap,"codeEmp"))){
    			queSchList.addAll(setLBQueSchVos(schpubMap));
    		}else if(LbSelfUtil.getPropValueStr(schpubMap,"euSrvtype").equals("1")){
    			queSchList.addAll(setLBQueSchVos(schpubMap));
    		}else{
    			queSchList.addAll(setLBQueSchVos(schpubMap));
    		}
		}
    	return queSchList;
    }
    
    /**
	 * ???????????????????????????xml????????????
     * ??????????????????????????????
     * @param paramMap
     * @return
     */
    public List<LBQueSchVo> getQuerySchAppt(Map<String,Object> paramMap) {    	
    	
    	List<LBQueSchVo> queSchList = new ArrayList<>();
    	List<LBQueSchVo> queSchListQt = new ArrayList<>();
    	List<LBQueSchVo> queSchListZj = new ArrayList<>();
    	List<LBQueSchVo> queSchListdept = new ArrayList<>();
    	
    	
    	List<Map<String, Object>> schpubList = schPubForWsMapper.QueryRegisteredRecords(paramMap);
    	for (Map<String, Object> schpubMap:schpubList){
    		if(StringUtils.isBlank(LbSelfUtil.getPropValueStr(schpubMap,"codeEmp"))){
    			queSchListdept.add(setLBQueSchVo(schpubMap));
    		}else if(LbSelfUtil.getPropValueStr(schpubMap,"euSrvtype").equals("1")){
    			queSchListZj.add(setLBQueSchVo(schpubMap));
    		}else{
    			queSchList.add(setLBQueSchVo(schpubMap));
    		}
		}
    	queSchList.addAll(queSchListdept);
    	queSchList.addAll(queSchListQt);
    	queSchList.addAll(queSchListZj);
    	return queSchList;
    }
    
    public List<LBQueSchVo> setLBQueSchVos(Map<String, Object> schpubMap){
    	List<LBQueSchVo> queSchList = new ArrayList<>();
    	List<LbQueSchCount> queCountList = new ArrayList<>();
    	List<Map<String, Object>> schSecList = schPubForWsMapper.querySchAppSec(schpubMap);
    	if(schSecList.size()<=0){
    		return queSchList;
    	}
		for(Map<String, Object> schSecMap:schSecList){
			if(0<Integer.valueOf(schSecMap.get("cnt").toString())){
				LbQueSchCount schCount=new LbQueSchCount();
				//???????????????
				schCount.setHaveCount(LbSelfUtil.getPropValueStr(schSecMap,"cnt"));
				//????????????????????????
				schCount.setAvailableCount(LbSelfUtil.getPropValueStr(schSecMap,"cntAppt"));
				//????????????
				schCount.setPhaseCode(LbSelfUtil.getPropValueStr(schSecMap,"pkDateslotsec"));
				String phase = LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot")+","+LbSelfUtil.getPropValueStr(schSecMap,"timeBegin").substring(0, 5)+"-"+LbSelfUtil.getPropValueStr(schSecMap,"timeEnd").substring(0, 5);
				schCount.setPhaseDesc(phase);//????????????
				queCountList.add(schCount);
			}
		}
		if(queCountList.size()<=0){
    		return queSchList;
    	}
		LBQueSchVo queSchVo = new LBQueSchVo();
		queSchVo.setPhaseDescName(LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot"));
		queSchVo.setSchCount(queCountList);
		queSchVo.setRegId(LbSelfUtil.getPropValueStr(schpubMap,"pkSch"));
		queSchVo.setRegDate(LbSelfUtil.getPropValueStr(schpubMap,"dateWork"));
		queSchVo.setBookType("1");
		queSchVo.setTypeCode(LbSelfUtil.getPropValueStr(schpubMap,"typecode"));
		queSchVo.setTypeName(LbSelfUtil.getPropValueStr(schpubMap,"typename"));
		queSchVo.setDeptCode(LbSelfUtil.getPropValueStr(schpubMap,"codeDept"));
		queSchVo.setDeptName(LbSelfUtil.getPropValueStr(schpubMap,"nameDept"));
		queSchVo.setDoctCode(LbSelfUtil.getPropValueStr(schpubMap,"codeEmp"));
		queSchVo.setDoctName(LbSelfUtil.getPropValueStr(schpubMap,"nameEmp"));
		queSchVo.setRankCode(LbSelfUtil.getPropValueStr(schpubMap,"code"));
		queSchVo.setRankName(LbSelfUtil.getPropValueStr(schpubMap,"name"));
		queSchVo.setSpec(LbSelfUtil.getPropValueStr(schpubMap,"spec"));
		queSchVo.setDeptDesc(LbSelfUtil.getPropValueStr(schpubMap,"deptDesc"));
		//????????????
		queSchVo.setAllCount(LbSelfUtil.getPropValueStr(schpubMap,"cntTotal"));
		//??????????????????
		queSchVo.setApptCount(LbSelfUtil.getPropValueStr(schpubMap,"cntAppt"));
		//?????????????????????
		queSchVo.setOutCount(LbSelfUtil.getPropValueStr(schpubMap,"cntUsed"));
		
		//??????????????????
		queSchVo.setStatus(LbSelfUtil.getPropValueStr(schpubMap,"flagStop"));
		//????????????
		queSchVo.setTotalFee(LbSelfUtil.getPropValueStr(schpubMap,"price"));
		//?????????
		queSchVo.setRegFee(LbSelfUtil.getPropValueStr(schpubMap,"price"));
		//?????????
		queSchVo.setTreatFee("");
		//?????????
		queSchVo.setServiceFee("");
		//????????????
		queSchVo.setOtherFee("");
		queSchVo.setLocation(LbSelfUtil.getPropValueStr(schpubMap,"namePlace"));
		
		queSchVo.setUnitName(LbSelfUtil.getPropValueStr(schpubMap,"unitName"));
		queSchList.add(queSchVo);
	
    	return queSchList;
    }

    public LBQueSchVo setLBQueSchVo(Map<String, Object> schpubMap){
    	LBQueSchVo queSchVo = new LBQueSchVo();
        queSchVo.setRegId(LbSelfUtil.getPropValueStr(schpubMap,"pkSch"));
		queSchVo.setRegDate(LbSelfUtil.getPropValueStr(schpubMap,"dateWork"));
		queSchVo.setBookType("1");
		queSchVo.setTypeCode(LbSelfUtil.getPropValueStr(schpubMap,"typecode"));
		queSchVo.setTypeName(LbSelfUtil.getPropValueStr(schpubMap,"typename"));
		queSchVo.setDeptCode(LbSelfUtil.getPropValueStr(schpubMap,"codeDept"));
		queSchVo.setDeptName(LbSelfUtil.getPropValueStr(schpubMap,"nameDept"));
		queSchVo.setDoctCode(LbSelfUtil.getPropValueStr(schpubMap,"codeEmp"));
		queSchVo.setDoctName(LbSelfUtil.getPropValueStr(schpubMap,"nameEmp"));
		queSchVo.setRankCode(LbSelfUtil.getPropValueStr(schpubMap,"code"));
		queSchVo.setRankName(LbSelfUtil.getPropValueStr(schpubMap,"name"));
		queSchVo.setPhaseCode(LbSelfUtil.getPropValueStr(schpubMap,"dtDateslottype"));
		String TotalSum;
		if(BeanUtils.isNotNull(schpubMap.get("dateWork")) && schpubMap.get("dateWork").toString().compareTo(DateTime.now().toString("YYYY-MM-dd"))>0){
			TotalSum = LbSelfUtil.getPropValueStr(schpubMap,"cntAppt");
		}else{
			TotalSum = LbSelfUtil.getPropValueStr(schpubMap,"cntTotal");
		}
		if(StringUtils.isNotBlank(TotalSum)){
		//????????????
		queSchVo.setAllCount(TotalSum);
		int usedSumInt = Integer.parseInt(TotalSum);
		int OutCount = Integer.parseInt(LbSelfUtil.getPropValueStr(schpubMap,"cntUsed"));
		int notUsed = usedSumInt-OutCount;
		String usedSum = String.valueOf(usedSumInt-notUsed);
		//?????????????????????
		queSchVo.setOutCount(LbSelfUtil.getPropValueStr(schpubMap,"cntUsed"));
		
		String notUsedStr = String.valueOf(notUsed);
		//???????????????
		queSchVo.setHaveCount(notUsedStr);
		}
		String phase=null;
		//?????????????????????????????????
		if(!("").equals(schpubMap.get("euStatus")) && null != schpubMap.get("euStatus")){
			//????????????
			queSchVo.setTicketNo(LbSelfUtil.getPropValueStr(schpubMap,"ticketNo"));
			phase = LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot")+","+LbSelfUtil.getPropValueStr(schpubMap,"beginTime")+"-"+LbSelfUtil.getPropValueStr(schpubMap,"endTime");
			try {
				Date beginTime = DateUtils.parseDate(LbSelfUtil.getPropValueStr(schpubMap,"dateWork")+" "+LbSelfUtil.getPropValueStr(schpubMap,"timeBegin"));
				beginTime = DateUtils.parseDate(DateUtils.addDate(beginTime, -2, 4, "yyyy-MM-dd HH:mm:ss"));
				Date endTime   = DateUtils.parseDate(LbSelfUtil.getPropValueStr(schpubMap,"dateWork")+" "+LbSelfUtil.getPropValueStr(schpubMap,"timeEnd"));
			    if(("0").equals(schpubMap.get("euStatus")) && !DateUtils.belongCalendar(new Date(), beginTime, endTime)){
			    	//??????????????????
				    queSchVo.setStatus("2");
			    }else{
			    	//????????????????????????
				    queSchVo.setStatus(LbSelfUtil.getPropValueStr(schpubMap,"euStatus"));
			    }
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			phase = LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot")+","+LbSelfUtil.getPropValueStr(schpubMap,"timeBegin").substring(0, 5)+"-"+LbSelfUtil.getPropValueStr(schpubMap,"timeEnd").substring(0, 5);
			//??????????????????
			queSchVo.setStatus(LbSelfUtil.getPropValueStr(schpubMap,"flagStop"));
		}
		queSchVo.setPhaseDesc(phase);
		//????????????
		queSchVo.setTotalFee(LbSelfUtil.getPropValueStr(schpubMap,"price"));
		//?????????
		queSchVo.setRegFee(LbSelfUtil.getPropValueStr(schpubMap,"price"));
		//?????????
		queSchVo.setTreatFee("");
		//?????????
		queSchVo.setServiceFee("");
		//????????????
		queSchVo.setOtherFee("");
		queSchVo.setLocation(LbSelfUtil.getPropValueStr(schpubMap,"namePlace"));
    	return queSchVo;
    }
	/**
	 * ??????????????????????????????xml????????????
     * ???????????????????????????????????????
     * @param paramMap
     * @return
     */
    public List<LbBmptTimeInfo> lbBmptTimeInfo(Map<String,Object> paramMap) {    	
    	List<LbBmptTimeInfo> TimeList = new ArrayList<>();
    	List<Map<String, Object>> schpubList = schPubForWsMapper.LbTodaySchInfosByDate(paramMap);
    	for (Map<String, Object> schpubMap:schpubList) {
    		
    		List<Map<String, Object>> schSecList = schPubForWsMapper.querySchAppSec(schpubMap);
    		for(Map<String, Object> schSecMap:schSecList){
    			LbBmptTimeInfo Time = new LbBmptTimeInfo();
    			if(0<Integer.valueOf(schSecMap.get("cntAppt").toString())){
    				//????????????????????????????????????
    	    		List<Map<String, Object>> tickets = DataBaseHelper.queryForList("select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' and to_char(begin_time, 'hh24:mi:ss')=? and to_char(end_time, 'hh24:mi:ss')=? order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",new Object[]{ LbSelfUtil.getPropValueStr(schpubMap,"pkSch"),LbSelfUtil.getPropValueStr(schSecMap,"timeBegin"),LbSelfUtil.getPropValueStr(schSecMap,"timeEnd")});
    	    		if(tickets.size()>0){
    	    			Time.setSourceID(LbSelfUtil.getPropValueStr(tickets.get(0),"pkSchticket"));//??????????????????
    	        		Time.setNum(LbSelfUtil.getPropValueStr(tickets.get(0),"ticketno"));//?????????????????????
    	    		}else{
    	    			//????????????????????????????????????????????????
    	    			int cnt = schPubForWsMapper.updateSchSchTicketNo(LbSelfUtil.getPropValueStr(schpubMap,"pkSch"));
    	    			//int cnt = DataBaseHelper.update("update sch_sch  set TICKET_NO = nvl(TICKET_NO,0)+1,cnt_used=cnt_used+1 where PK_SCH = ? and cnt_used<cnt_total", new Object[]{LbSelfUtil.getPropValueStr(schpubMap,"pkSch")});
    	    			 if(cnt<=0)
    	    				 throw new BusException("????????????????????????????????????");
    	    			 SchTicket  ticket =  new SchTicket();
    	    			 SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{LbSelfUtil.getPropValueStr(schpubMap,"pkSch")});
    	    			 Time.setNum(sch.getTicketNo());
    	    			 Time.setSourceID(LbSelfUtil.getPropValueStr(schpubMap,"pkSch"));
    	    		}
    	    		
    	    		Time.setServiceDate(LbSelfUtil.getPropValueStr(schpubMap,"dateWork"));//????????????
    	    		Time.setDepartmentCode(LbSelfUtil.getPropValueStr(schpubMap,"codeDept"));//????????????
    	    		Time.setDoctorCode(LbSelfUtil.getPropValueStr(schpubMap,"codeEmp"));//????????????
    	    		//????????????
    	    		Time.setStartTime(LbSelfUtil.getPropValueStr(schSecMap,"timeBegin"));//????????????????????????
    	    		Time.setEndTime(LbSelfUtil.getPropValueStr(schSecMap,"timeEnd"));//????????????????????????
    	    		Time.setAvailableNum(LbSelfUtil.getPropValueStr(schSecMap,"cntAppt"));//????????????????????????

    	    		TimeList.add(Time);
    			}
    		}
		}
    	return TimeList;
    }
    
	/**
	 * ??????????????????????????????xml????????????
     * ???????????????????????????????????????
     * @param paramMap
     * @return
     */
    public List<LbBmptSchedule> lbBmptgetQuerySch(Map<String,Object> paramMap) {    	
    	List<LbBmptSchedule> SchList = new ArrayList<>();
    	List<Map<String, Object>> schpubList = schPubForWsMapper.LbTodaySchInfosByDate(paramMap);
    	for (Map<String, Object> schpubMap:schpubList) {
    		LbBmptSchedule Schedule = new LbBmptSchedule();
    		Schedule.setServiceDate(LbSelfUtil.getPropValueStr(schpubMap,"dateWork"));//????????????
    		if(LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot").equals("??????")){
    			Schedule.setSessionCode("AM");//????????????,???PM
    		}else if(LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot").equals("??????")){
    			Schedule.setSessionCode("PM");//????????????,AM???
    		}
    		
    		Schedule.setSessionCodeName(LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot"));//??????????????????
    		Schedule.setDepartmentCode(LbSelfUtil.getPropValueStr(schpubMap,"codeDept"));//????????????
    		Schedule.setDepartmentName(LbSelfUtil.getPropValueStr(schpubMap,"nameDept"));//????????????
    		Schedule.setDoctorCode(LbSelfUtil.getPropValueStr(schpubMap,"codeEmp"));//????????????
    		Schedule.setDoctorName(LbSelfUtil.getPropValueStr(schpubMap,"nameEmp"));//????????????
    		Schedule.setDoctorTitle(LbSelfUtil.getPropValueStr(schpubMap,"name"));//????????????
    		//Schedule.setDoctorSpec("");//????????????
    		Schedule.setQueueType(LbSelfUtil.getPropValueStr(schpubMap,"euSrvtype"));//????????????????????????
    		Schedule.setAllNum(LbSelfUtil.getPropValueStr(schpubMap,"cntAppt"));//????????????
    		Schedule.setAvailableNum(LbSelfUtil.getPropValueStr(schpubMap,"cntAppt"));//????????????
    		Schedule.setFee(LbSelfUtil.getPropValueStr(schpubMap,"price"));//?????????
    		
    		SchList.add(Schedule);
    		Schedule = null;
		}
    	return SchList;
    }
	/**
	 * ????????????????????????????????????
	 */
	public List<Map<String, Object>> getSchMapList(Map<String, Object> map){		
		List<Map<String, Object>> NewMap = (List<Map<String, Object>>)schPubForWsMapper.querySchInfo(map);		
		return NewMap;
	}
	
	/**
	 * ??????????????????????????????
	 * @param param
	 *            { "nameMachine":"?????????", "euType":"????????????: 0 ???????????????1 ????????????" }
	 * 
	 * @return 
	 * {
     * "invPrefix":"????????????",
     * "curNo":"??????????????????",
     * "length":"???????????????"(??????null),
     * "curCodeInv":"??????????????????",
     * "cntUse":"????????????",
     * "pkEmpinv":"????????????",
     * "pkInvcate":"????????????"
     * }
	 */
	public Map<String,Object> searchCanUsedEmpInvoices(Map<String,Object> paramMap){
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson  rs =  apputil.execService("ma", "InvService", "searchCanUsedEmpInvoices",paramMap,u);
		return (Map<String,Object>)rs.getData();
	}
	
	/**
	 * ???????????????????????????xml????????????
     * ????????????????????????
     * @param paramMap
     * @return
     */
    public List<LBQueSchVo> getQueryRegisteredRecords(){
    	List<LBQueSchVo> queSchList = new ArrayList<>();
    	List<Map<String, Object>> schApptList = schPubForWsMapper.QueryRegisteredRecords(null);
    	for (int i = 0; i < schApptList.size(); i++) {
    		LBQueSchVo queSchVo = new LBQueSchVo();
    		queSchVo.setRegId(LbSelfUtil.getPropValueStr(schApptList.get(i),"pkSch"));
    		queSchVo.setRegDate(ArchUtils.dateTrans(new Date(),"yyyy-MM-dd"));
    		queSchVo.setBookType("1");
    		queSchVo.setTypeCode(LbSelfUtil.getPropValueStr(schApptList.get(i),"typecode"));
    		queSchVo.setTypeName(LbSelfUtil.getPropValueStr(schApptList.get(i),"typename"));
    		queSchVo.setDeptCode(LbSelfUtil.getPropValueStr(schApptList.get(i),"codeDept"));
    		queSchVo.setDeptName(LbSelfUtil.getPropValueStr(schApptList.get(i),"nameDept"));
    		queSchVo.setDoctCode(LbSelfUtil.getPropValueStr(schApptList.get(i),"codeEmp"));
    		queSchVo.setDoctName(LbSelfUtil.getPropValueStr(schApptList.get(i),"nameEmp"));
    		queSchVo.setRankCode(LbSelfUtil.getPropValueStr(schApptList.get(i),"code"));
    		queSchVo.setRankName(LbSelfUtil.getPropValueStr(schApptList.get(i),"name"));
    		queSchVo.setPhaseCode(LbSelfUtil.getPropValueStr(schApptList.get(i),"dtDateslottype"));
    		String phase = LbSelfUtil.getPropValueStr(schApptList.get(i),"dateslotName")+","+LbSelfUtil.getPropValueStr(schApptList.get(i),"timeBegin")+"-"+LbSelfUtil.getPropValueStr(schApptList.get(i),"timeEnd");
    		queSchVo.setPhaseDesc(phase);
    		String TotalSum = LbSelfUtil.getPropValueStr(schApptList.get(i),"cntTotal");
    		//????????????
    		queSchVo.setAllCount(TotalSum);
    		int usedSumInt = Integer.parseInt(TotalSum);
    		int notUsed = schPubForWsMapper.getSchTicketNotUsedSum(schApptList.get(i));
    		String usedSum = String.valueOf(usedSumInt-notUsed);
    		//?????????????????????
    		queSchVo.setOutCount(usedSum);
    		
    		String notUsedStr = String.valueOf(notUsed);
    		//???????????????
    		queSchVo.setHaveCount(notUsedStr);
    		//????????????
    		queSchVo.setStatus(LbSelfUtil.getPropValueStr(schApptList.get(i),"flagStop"));
    		//????????????
    		queSchVo.setTotalFee("");
    		//?????????
    		queSchVo.setRegFee(LbSelfUtil.getPropValueStr(schApptList.get(i),"price"));
    		//?????????
    		queSchVo.setTreatFee("");
    		//?????????
    		queSchVo.setServiceFee("");
    		//????????????
    		queSchVo.setOtherFee("");
    		queSchVo.setLocation(LbSelfUtil.getPropValueStr(schApptList.get(i),"namePlace"));
    		
    		queSchList.add(queSchVo);
    		queSchVo = null;
		}
    	return queSchList;
    }

    /**
     * ?????????????????????
     * @param pkSch
     * @return SchTicket
     */
	public SchTicket getUnusedAppTicket(String pkSch,String euPvtype){
		// ????????????and eu_pvtype = ?,euPvtype
		//and FLAG_APPT = '0' //?????????????????????????????????????????????
		List<SchTicket> tickets = DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = '0'  and flag_stop='0' and FLAG_USED = '0' order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ",
				SchTicket.class, pkSch);
		if(tickets==null||tickets.size()<=0){
			throw new BusException("????????????????????????????????????????????????");
		}
		SchTicket ticket = tickets.get(0);
		return ticket;
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param pkSch
	 * @param euPvtype
	 * @return
	 */
	public SchTicket getUnusedAppTicketFromSchs(String pkSch,String euPvtype){
		//????????????????????????????????????????????????
		int cnt = schPubForWsMapper.updateSchSchTicketNo(pkSch);
		 //int cnt =  DataBaseHelper.update("update sch_sch  set TICKET_NO = nvl(TICKET_NO,0)+1,cnt_used=cnt_used+1 where PK_SCH = ? and cnt_used<cnt_appt", new Object[]{pkSch});
		 if(cnt<=0)
			 throw new BusException("????????????????????????????????????");
		 SchTicket  ticket =  new SchTicket();
		 SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
		 ticket.setTicketno(sch.getTicketNo());
		 ticket.setPkSch(pkSch);
		 return ticket;
	}
    
	/**
	 * ????????????????????????(?????????)
	 * @param paramMap{pkSch?????????}
	 * @return
	 */
	public List<Map<String,Object>> getTicketsForSelf(Map<String,Object> paramMap){
		String pkSch = (String)paramMap.get("pkSch");
		String pkSchsrv = (String) paramMap.get("pkSchsrv");
		String dateWork = (String) paramMap.get("dateWork");
		String pkSchres = (String) paramMap.get("pkSchres");
		String pkDateslot = (String) paramMap.get("pkDateslot");
		SchSch schSch = null;
		Double price = null;
		
		if (pkSch != null) {
			schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ? ",
					SchSch.class, new Object[] { pkSch });
			pkDateslot = schSch.getPkDateslot();
			pkSchsrv = schSch.getPkSchsrv();
		 Map<String,Object> priceMap = DataBaseHelper.queryForMap("select distinct bi.price from sch_sch sch "
			         		+ "inner join sch_srv srv on sch.pk_schsrv = srv.pk_schsrv "
			         		+ "inner join sch_srv_ord ord on srv.pk_schsrv = ord.pk_schsrv "
			         		+ "inner join bd_ord_item item on ord.pk_ord = item.pk_ord "
			         		+ "inner join bd_item bi on item.pk_item = bi.pk_item "
			         		+ "where sch.pk_schsrv = ? and sch.del_flag = '0'", new Object[] {pkSchsrv});
		 price = Double.parseDouble(priceMap.get("price").toString());
		
	
		
		} else {
			try {  //?????????????????????????????????
				schSch = DataBaseHelper.queryForBean(
						"select * from sch_sch where del_flag = '0' and pk_schsrv = ? and date_work = to_date(?,'yyyy-mm-dd')  and pk_schres = ? and pk_dateslot = ?",
						SchSch.class, new Object[] { pkSchsrv, dateWork, pkSchres, pkDateslot });
				pkSch = schSch.getPkSch();
			} catch (Exception e) {
				throw new BusException("?????????????????????????????????");
			}
		}
		List<BdCodeDateslotSec> dateslotSecs = DataBaseHelper.queryForList(
				"select * from BD_CODE_DATESLOT_SEC where pk_dateslot = ? order by sortno", BdCodeDateslotSec.class,
				new Object[] { pkDateslot });
		
		List<BdCodeDateslot> dateslot = DataBaseHelper.queryForList("select * from BD_CODE_DATESLOT where pk_dateslot = ? order by sortno",BdCodeDateslot.class,new Object[]{pkDateslot});
		String type = dateslot.get(0).getNameDateslot();
		List<SchTicket> schTickets = DataBaseHelper.queryForList(
				"select * from sch_ticket where del_flag = '0' and FLAG_APPT='1' and pk_sch = ?", SchTicket.class,
				new Object[] { pkSch });
		DateTime begin = getSchBeginTime(pkDateslot, schSch.getDateWork());
        List<SchTicket> list = Lists.newArrayList();
		for (BdCodeDateslotSec dateslotSec : dateslotSecs) {
			DateTime end = begin.plusMinutes(dateslotSec.getSecmin());
			String sec = begin.toString("HH:mm") + "-" + end.toString("HH:mm");
			begin = end;
			for (int i = schTickets.size() - 1; i >= 0; i--) {
				SchTicket ticket = schTickets.get(i);
				Date beginTime = ticket.getBeginTime();
				String beginT = DateUtils.dateToStr("HH:mm", ticket.getBeginTime());
				ticket.setBeginTime(DateUtils.strToDate(beginT, "HH:mm"));
				DateTime ticketBegin = new DateTime(ticket.getBeginTime());
				if (Minutes.minutesBetween(end, ticketBegin).getMinutes() < 0) {
					ticket.setBeginTime(beginTime);
					ticket.setStrBeginTime(beginT);
					list.add(ticket);
					schTickets.remove(i);
				}
			}
			
		}
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> resultMap = Maps.newHashMap();
		//List<Map<String,Object>> listMapsch = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultMapsch = Maps.newHashMap();
		resultMapsch.put("schschs",list);
		resultMapsch.put("price", price);
		//listMapsch.add(resultMapsch);
		resultMap.put("type", type);
		resultMap.put("schTicketSecs", resultMapsch);
	    listMap.add(resultMap);
		return listMap;
	}
	/**
	 * ??????????????????????????????????????????-??????(?????????????????????)
	 * @param paramMap{pkSch?????????}
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> getTicketsGroupDate(Map<String,Object> paramMap){
		String pkSch = (String)paramMap.get("pkSch");
		String pkSchsrv = (String) paramMap.get("pkSchsrv");
		String pkDateslot = (String) paramMap.get("pkDateslot");
		SchSch schSch = null;
		Double price = null;
		
		schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ? ",
				SchSch.class, new Object[] { pkSch });
		pkDateslot = schSch.getPkDateslot();
		pkSchsrv = schSch.getPkSchsrv();
		//????????????
		Map<String,Object> priceMap = DataBaseHelper.queryForMap("select distinct bi.price from sch_sch sch "
			         		+ "left join sch_srv srv on sch.pk_schsrv = srv.pk_schsrv "
			         		+ "left join sch_srv_ord ord on srv.pk_schsrv = ord.pk_schsrv "
			         		+ "left join bd_ord_item item on ord.pk_ord = item.pk_ord "
			         		+ "left join bd_item bi on item.pk_item = bi.pk_item "
			         		+ "where sch.pk_schsrv = ? and sch.del_flag = '0'", new Object[] {pkSchsrv});
		price = Double.parseDouble(priceMap.get("price").toString());
		List<BdCodeDateslot> dateslot = DataBaseHelper.queryForList("select * from BD_CODE_DATESLOT where pk_dateslot = ? order by sortno",BdCodeDateslot.class,new Object[]{pkDateslot});
		String type = dateslot.get(0).getNameDateslot();
        List<Map<String,Object>> ticketList =schPubForWsMapper.getTicketsGroupDate(pkSch);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> resultMap = Maps.newHashMap();
		//List<Map<String,Object>> listMapsch = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultMapsch = Maps.newHashMap();
		resultMapsch.put("schschs",ticketList);
		resultMapsch.put("price", price);
		//listMapsch.add(resultMapsch);
		resultMap.put("type", type);
		resultMap.put("schTicketSecs", resultMapsch);
	    listMap.add(resultMap);
		return listMap;
	}
	/**
	 * ??????????????????????????????
	 * 
	 * @param pkDateslot
	 *            ??????????????????
	 * @param dateWork
	 *            ?????????????????????
	 * @return
	 */
	private DateTime getSchBeginTime(String pkDateslot, Date dateWork) {
		DateTime dt = new DateTime(dateWork);
		String dateWorkStr = dt.toString("MM-dd");
		String yeardate = dt.toString("yyyy-MM-dd");
		
		BdCodeDateslotTime bdCodeDateslotTime = DataBaseHelper.queryForBean(
				"select *  from bd_code_dateslot_time where del_flag = '0' " + "and pk_dateslot = ? "
						+ "and lpad(valid_month_begin,2,'0') || '-' || lpad(valid_day_begin,2,'0') <= ? "
						+ "and lpad(valid_month_end,2,'0') || '-' || lpad(valid_day_end,2,'0') >= ? ",
				BdCodeDateslotTime.class, pkDateslot, dateWorkStr, dateWorkStr);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime begin;
		if (bdCodeDateslotTime == null) {
			BdCodeDateslot bdCodeDateslot = DataBaseHelper.queryForBean(
					"select * from bd_code_dateslot where pk_dateslot = ?", BdCodeDateslot.class, pkDateslot);
			if (bdCodeDateslot == null)
				throw new BusException("?????????????????????");
			begin = DateTime.parse(yeardate + " " + bdCodeDateslot.getTimeBegin(), dtf);
		} else {
			begin = DateTime.parse(yeardate + " " + bdCodeDateslotTime.getTimeBegin(), dtf);
		}
		return begin;
	}
	
	/**
	 * ????????????????????????
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getRegistered(Map<String,Object> paramMap){
		List<Map<String,Object>> list = schPubForWsMapper.getRegistered(paramMap);
		return list;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getPiAppointment(Map<String,Object> paramMap){
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson rs = apputil.execService("SCH", "SchExtPubService", "getPiAppointment", paramMap, u);
		return (List<Map<String,Object>>)rs.getData();
	}
	/**
	 * ??????????????????-?????????????????????????????????
	 * @param param{pkPi,pkSch,registerDate,registerTime}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> saveAppointmentGroupDate(Map<String,Object> paramMap){
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pkSch", paramMap.get("pkSch"));
		String registerDateStr=(String) paramMap.get("registerDate");
		String registerTimeStr=(String) paramMap.get("registerTime");
		String [] timeArgs=registerTimeStr.split("-");
		queryMap.put("beginDate",registerDateStr+" "+timeArgs[0]);
		queryMap.put("endDate",registerDateStr+" "+timeArgs[1]);
		List<Map<String,Object>> list=schPubForWsMapper.queryTicketsBySchAndTimeList(queryMap);
		if(null == list || list.size() ==0 ){
			result.put("message", "??????????????????????????????????????????");
			result.put("result", "false");
			return result;
		}
		Map<String,Object> map=list.get(0);
		paramMap.put("ticketNo", map.get("ticketno"));
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson  rs =  apputil.execService("SCH", "SchExtPubService", "applyRegister",paramMap,u);
		return (Map<String,Object>)rs.getData();
	}
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getRegistRecord(Map<String, Object> paramMap) {
		List<Map<String, Object>> list = schPubForWsMapper.getRegistRecord(paramMap);
		return list;
	}
	
	/**
     * ???????????????????????????
	 * @param regvo
	 * @param map
	 */
	public BlExtPay saveBlExtPay(Map<String, Object> map,User user){
		BlExtPay extPay=new BlExtPay();
		extPay.setPkExtpay(NHISUUID.getKeyId());
		extPay.setPkOrg(user.getPkOrg());
		extPay.setAmount(new BigDecimal(CommonUtils.getPropValueStr(map,"payAmt")==""?0:Double.valueOf(CommonUtils.getPropValueStr(map,"payAmt"))));
		extPay.setEuPaytype(CommonUtils.getPropValueStr(map,"payType"));
		extPay.setFlagPay("1");//????????????
		extPay.setSerialNo(CommonUtils.getPropValueStr(map,"orderno"));//?????????
		extPay.setTradeNo(CommonUtils.getPropValueStr(map,"flowno"));//???????????????
		extPay.setSysname(CommonUtils.getPropValueStr(map,"sysname"));//?????????
		extPay.setPkPi(CommonUtils.getPropValueStr(map,"codePi"));//??????????????????
		extPay.setDescPay(CommonUtils.getPropValueStr(map,"descPay"));
		extPay.setResultPay(CommonUtils.getPropValueStr(map,"resultPay"));
		extPay.setCreator(user.getPkEmp());
		extPay.setCreateTime(new Date());
		extPay.setTs(new Date());
		extPay.setDelFlag("0");		
		
		DataBaseHelper.insertBean(extPay);
		return extPay;
	}
}
