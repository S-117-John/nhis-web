package com.zebone.nhis.task.sch.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiLock;
import com.zebone.nhis.common.module.pi.PiLockDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.task.sch.dao.SchApptBlackMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

@Service
public class SchApptTaskService {
	
	@Autowired
	private SchApptBlackMapper schApptMapper;
	
	//private Logger logger = LoggerFactory.getLogger("nhis.quartz");

    public void GenSchApptBlackTask(QrtzJobCfg cfg) {
    	//logger.info("================对爽约迟到患者锁定定时任务开始================");
		String pkOrg = cfg.getJgs();
		if(StringUtils.isBlank(pkOrg)){
			throw new BusException("请先对任务授权");
		}
		if(pkOrg!=null && pkOrg.contains(",")){
			pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(),"").replace(",","");
		} else if(CommonUtils.getGlobalOrg().equals(pkOrg)){
			throw new BusException("请将任务授权给具体机构！");
		}
		User user = new User();
		user.setPkOrg(pkOrg);
		UserContext.setUser(user);
		// 允许锁定的爽约次数
		String breakCnt = ApplicationUtils.getSysparam("SCH0002",false);
		// 查询爽约的天数范围
		String breakDays = ApplicationUtils.getSysparam("SCH0003",false);
		// 允许锁定的就诊迟到次数
		String lateTimer = ApplicationUtils.getSysparam("SCH0006",false);

		DateTime now = DateTime.now();
		DateTime begin = now.plusDays(0 - NumberUtils.toInt(breakDays,0));
		String dateBegin = begin.toString("yyyyMMdd") + "000000";
		String dateEnd = now.toString("yyyyMMdd") + "235959";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("breakCnt", NumberUtils.toInt(breakCnt,0));
		params.put("dateBegin", dateBegin);

		params.put("dateEnd", dateEnd);
		params.put("lateTimer", NumberUtils.toInt(lateTimer,0));

		List<Map<String, Object>> blackList = schApptMapper.genApplyBlacklist(params);
		List<Map<String, Object>> blackLateList = schApptMapper.genApplyBlackLatelist(params);

		List<Map<String, Object>> pkPiList = new ArrayList<>();
		HashMap<String,Object> hashMap = new HashMap<>();

		for (Map<String, Object> map : blackList) {
			if(hashMap.containsValue(map.get("pkPi"))){
				continue;
			}else{
				hashMap.put("pkPi", map.get("pkPi"));
				Map<String,Object> blackMap = new HashMap<String, Object>();
				blackMap.put("pkPi", map.get("pkPi").toString());
				blackMap.put("lockType", "0");
				pkPiList.add(blackMap);
			}
		}
		for (Map<String, Object> map : blackLateList) {
			if(hashMap.containsValue(map.get("pkPi"))){
				continue;
			}else{
				hashMap.put("pkPi", map.get("pkPi"));
				Map<String,Object> blackMap = new HashMap<String, Object>();
				blackMap.put("pkPi", map.get("pkPi").toString());
				blackMap.put("lockType", "1");
				pkPiList.add(blackMap);
			}
		}

		LockSchAppt(pkPiList);
    	
    	//logger.info("================对爽约迟到患者锁定定时任务结束================");
    }
    
    public void LockSchAppt(List<Map<String, Object>> pkPiList){
    	List<PiLock> piLocks = new ArrayList<>();
		List<PiLockDt> piLockDts = new ArrayList<>();
		//读取参数SCH0007，获取黑名单锁定的预约挂号天数，用于生成锁定结束时间;		
    	int lateTimer = NumberUtils.toInt(ApplicationUtils.getSysparam("SCH0007",false),0);
		for (Map<String, Object> map : pkPiList) {
			PiLock piLock = new PiLock();
			piLock.setPkPilock(NHISUUID.getKeyId());
			piLock.setEuLocktype(map.get("lockType").toString());
			//piLock.setNote(note);
			piLock.setDateBegin(new Date());			
			Date date = new Date();
			try {
				date = plusDay(lateTimer,date);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			piLock.setDateEnd(date);
			piLock.setPkPi(map.get("pkPi").toString());
			piLock.setPkOrg("~                     ");
			piLocks.add(piLock);
			PiLockDt piLockDt = new PiLockDt();
			piLockDt.setPkPilockdt(NHISUUID.getKeyId());
			piLockDt.setPkOrg("~                     ");
			piLockDt.setPkPi(map.get("pkPi").toString());
			piLockDt.setEuLocktype(map.get("lockType").toString());
			piLockDt.setEuDirect("1");
			piLockDt.setDateLock(new Date());			
			//piLockDt.setNote(note);
			piLockDts.add(piLockDt);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLock.class), piLocks);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockDt.class), piLockDts);	
    }
    
    /**
     * 给指定日期加上天数
     * @param num
     * @param newDate
     * @return
     */
    public Date plusDay(int num,Date newDate) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date  currdate = format.parse(newDate);       
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        newDate = ca.getTime();                       
        return newDate;
    }
    /**
     * 坪山口腔定时处理爽约患者是连续爽约
     * @param cfg
     */
    public void GenSchApptBlackPskqTask(QrtzJobCfg cfg) {
    	//logger.info("================对爽约迟到患者锁定定时任务开始================");
		String pkOrg = cfg.getJgs();
		if(StringUtils.isBlank(pkOrg)){
			throw new BusException("请先对任务授权");
		}
		if(pkOrg!=null && pkOrg.contains(",")){
			pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(),"").replace(",","");
		} else if(CommonUtils.getGlobalOrg().equals(pkOrg)){
			throw new BusException("请将任务授权给具体机构！");
		}
		User user = new User();
		user.setPkOrg(pkOrg);
		UserContext.setUser(user);
		// 允许锁定的爽约次数
		String breakCnt = ApplicationUtils.getSysparam("SCH0002",false);
		int cnt=NumberUtils.toInt(breakCnt,0);
		if(0==cnt){
			throw new BusException("请维护SCH0002参数");
		}
		// 查询爽约的天数范围
		String breakDays = ApplicationUtils.getSysparam("SCH0003",false);
		int breakcnt=NumberUtils.toInt(breakDays,0);
		if(0==cnt){
			throw new BusException("请维护SCH0003参数");
		}
		DateTime now = DateTime.now();
		DateTime begin = now.plusDays(0 - NumberUtils.toInt(breakDays,0));
		String dateBegin = begin.toString("yyyyMMdd") + "000000";
		String dateEnd = now.toString("yyyyMMdd") + "235959";

		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("breakCnt", NumberUtils.toInt(breakCnt,0));
		params.put("dateBegin", dateBegin);
		params.put("dateEnd", dateEnd);

		List<Map<String, Object>> blackList = schApptMapper.genApplyBlackPskqlist(params);
		//按pkpi分组
		Map<String, List<Map<String, Object>>> maps = blackList.stream().collect(Collectors.groupingBy(this::customKey));
		List<Map<String, Object>> pkPiList = new ArrayList<>();
		for (String pkPi : maps.keySet()) {
			List<Map<String, Object>> pvs = maps.get(pkPi);
			if(pvs.size()==0){
				continue;
			}
			if("0".equals(String.valueOf(pvs.get(0).get("flagWas")))){
				continue;
			}
			BigDecimal wasCnt=BigDecimal.ZERO;//序号
			int pvno=0;//爽约次数
			for (int i = 0; i < pvs.size(); i++) {
				Map<String, Object> pv=pvs.get(i);
				String flagWas=String.valueOf(pv.get("flagWas"));
				if("1".equals(flagWas)){
					if(i==0){
						wasCnt=(BigDecimal)pv.get("no");
						pvno=1;
					}else{
						BigDecimal big=((BigDecimal)pv.get("no")).subtract(wasCnt);
						if(big.compareTo(new BigDecimal("1"))==0){
							wasCnt=(BigDecimal)pv.get("no");
							pvno=pvno+1;
						}
					}
				}
			}
			if(pvno>=cnt){
				Map<String,Object> blackMap = new HashMap<String, Object>();
				blackMap.put("pkPi", pkPi);
				blackMap.put("lockType", "0");
				pkPiList.add(blackMap);
			}
		}
		LockSchApptPskq(pkPiList);
    	
    	//logger.info("================对爽约迟到患者锁定定时任务结束================");
    }
    public void LockSchApptPskq(List<Map<String, Object>> pkPiList){
    	List<PiLock> piLocks = new ArrayList<>();
    	List<PiLock> piLockUpds = new ArrayList<>();
		List<PiLockDt> piLockDts = new ArrayList<>();
		//读取参数SCH0007，获取黑名单锁定的预约挂号天数，用于生成锁定结束时间;		
    	int lateTimer = NumberUtils.toInt(ApplicationUtils.getSysparam("SCH0007",false),0);
		for (Map<String, Object> map : pkPiList) {
			PiLock piloc = DataBaseHelper.queryForBean("select * from pi_lock where pk_pi = ?",PiLock.class, map.get("pkPi"));
			if(null==piloc){
				PiLock piLock = new PiLock();
				piLock.setPkPilock(NHISUUID.getKeyId());
				piLock.setEuLocktype(map.get("lockType").toString());
				//piLock.setNote(note);
				piLock.setDateBegin(new Date());			
				Date date = new Date();
				try {
					date = plusDay(lateTimer,date);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				piLock.setDateEnd(date);
				piLock.setPkPi(map.get("pkPi").toString());
				piLock.setPkOrg("~                     ");
				piLocks.add(piLock);
			}else{
				Date date = new Date();
				try {
					date = plusDay(lateTimer,date);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				piloc.setDateEnd(date);
				piloc.setEuLocktype(map.get("lockType").toString());
				piLockUpds.add(piloc);
			}
			PiLockDt piLockDt = new PiLockDt();
			piLockDt.setPkPilockdt(NHISUUID.getKeyId());
			piLockDt.setPkOrg("~                     ");
			piLockDt.setPkPi(map.get("pkPi").toString());
			piLockDt.setEuLocktype(map.get("lockType").toString());
			piLockDt.setEuDirect("1");
			piLockDt.setDateLock(new Date());			
			//piLockDt.setNote(note);
			piLockDts.add(piLockDt);
		}
		if(piLocks.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLock.class), piLocks);
		}
		if(piLockUpds.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PiLock.class), piLockUpds);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockDt.class), piLockDts);	
    }
	private  String customKey(Map<String,Object> map){
	    return map.get("pkPi").toString();
	}
	/**
     * 坪山口腔定时处理解锁黑名单患者
     * @param cfg
     */
    public void GenSchApptUnBlackPskqTask(QrtzJobCfg cfg) {
		String pkOrg = cfg.getJgs();
		if(StringUtils.isBlank(pkOrg)){
			throw new BusException("请先对任务授权");
		}
		if(pkOrg!=null && pkOrg.contains(",")){
			pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(),"").replace(",","");
		} else if(CommonUtils.getGlobalOrg().equals(pkOrg)){
			throw new BusException("请将任务授权给具体机构！");
		}
		User user = new User();
		user.setPkOrg(pkOrg);
		UserContext.setUser(user);
		
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> blackList = schApptMapper.genApplyUnBlackPskqlist(params);
		//按pkpi分组
		Map<String, List<Map<String, Object>>> maps = blackList.stream().collect(Collectors.groupingBy(this::customKey));
		List<Map<String, Object>> pkPiList = new ArrayList<>();
		for (String pkPi : maps.keySet()) {
			List<Map<String, Object>> pvs = maps.get(pkPi);
			if(pvs.size()==0){
				continue;
			}
			if("0".equals(String.valueOf(pvs.get(0).get("flagWas")))){
				Map<String,Object> blackMap = new HashMap<String, Object>();
				blackMap.put("pkPi", pkPi);
				blackMap.put("lockType", "0");
				pkPiList.add(blackMap);
			}
		}
		unLockSchApptPskq(pkPiList);
    }
    public void unLockSchApptPskq(List<Map<String, Object>> pkPiList){
    	if(pkPiList.size()>0){
	    	StringBuilder whereCase = new StringBuilder();
			whereCase.append("where pk_pi in (");
			List<PiLockDt> piLockDts = new ArrayList<>();
			for (int i = 0; i < pkPiList.size(); i++) {
				Map<String, Object> map=pkPiList.get(i);
				String pkPi = map.get("pkPi").toString();
				if (i == 0) {
					whereCase.append("'").append(pkPi).append("'");
				} else {
					whereCase.append(",'").append(pkPi).append("'");
				}
				PiLockDt piLockDt = new PiLockDt();
				piLockDt.setPkPilockdt(NHISUUID.getKeyId());
				piLockDt.setPkOrg("~                     ");
				piLockDt.setPkPi(map.get("pkPi").toString());
				piLockDt.setEuLocktype(map.get("lockType").toString());
				piLockDt.setEuDirect("-1");
				piLockDt.setDateLock(new Date());	
				piLockDts.add(piLockDt);
			}
			whereCase.append(")");
			DataBaseHelper.execute("delete from pi_lock " + whereCase);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockDt.class), piLockDts);	
    	}
    }
}
