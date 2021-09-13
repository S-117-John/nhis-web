package com.zebone.nhis.task.dumped.service;

import com.zebone.nhis.base.pub.service.DumpPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 历史资料转存定时器
 * @author ds
 *
 */
@Service
public class DumpTaskService {
	private static Logger log = LoggerFactory.getLogger("com.zebone");
	
	@Resource
	private DumpPubService dumpPubService;
	
	public void executeTask(QrtzJobCfg cfg){
		String pkOrg = cfg.getJgs();
		Map<String,Object> paramMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
		if(CommonUtils.isEmptyString(pkOrg)){
			throw new BusException("未获取到机构信息！");
		}
		List<String> orgArr=validForArray(cfg);
		if(orgArr!=null&&orgArr.size()>0) {
			for (String pkOrgTemp : orgArr) {
				User u = new User();
				u.setPkOrg(pkOrgTemp);
				UserContext.setUser(u);
				log.info("****************定时任务开始执行数据转存*****************");
				log.info("****************定时任务开始执行数据转存---住院*****************");
				execIp();
				log.info("****************定时任务开始执行数据转存---门诊*****************");
				execOp();
				log.info("****************执行转存排班记录*****************");
				String defaultDays = ApplicationUtils.getSysparam("PUB0004", false);
				if (paramMap.get("days") == null) {
					dumpPubService.batInsertSchSchByDate(DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(defaultDays)) + " 00:00:00");
					dumpPubService.batInsertSchTicketByDate(DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(defaultDays)) + " 00:00:00");
				} else {
					dumpPubService.batdelSchSchByDate(DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(paramMap.get("days").toString())) + " 00:00:00");
//				dumpPubService.batInsertSchTicketByDate(DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(paramMap.get("days").toString()))+" 00:00:00");
				}
				dumpPubService.batInsertPvQueByDate(DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(defaultDays)) + " 00:00:00");
				log.info("****************定时任务结束执行数据转存*****************");
			}
		}
	}

	/**
	 * 多机构模式获取机构主键
	 * @param cfg
	 * @return
	 */
	private List<String> validForArray(QrtzJobCfg cfg) {
		String pkOrg = cfg.getJgs();
		List<String> pkOrgs=new ArrayList<>();
		if (StringUtils.isBlank(pkOrg)) {
			throw new BusException("请先对任务授权");
		}
		if (pkOrg != null ) {
			pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(), "");
			if(!pkOrg.contains(",")){
				pkOrgs.add(pkOrg);
			}else {
				List<String> org_temp= Arrays.asList(pkOrg.split(","));
				if(org_temp!=null && org_temp.size()>0){
					pkOrgs.addAll(org_temp);
				}
			}
		} else if (CommonUtils.getGlobalOrg().equals(pkOrg)) {
			throw new BusException("请将任务授权给具体机构");
		}
		if (StringUtils.isBlank(cfg.getJobparam())) {
			throw new BusException("请设置任务执行参数");
		}
		return pkOrgs;
	}

	/**
	 * 住院转存
	 */
	private void execIp(){
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("euPvtype", "3");
		execDumpDataGroupDept(param);
	}
	/**
	 * 门诊转存
	 */
	private void execOp(){
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("euPvtype", "1");
		execDumpDataGroupDept(param);
		
	}
	
	/**
	 * 按科室执行
	 * @param param
	 */
	private void execDumpDataGroupDept(Map<String,Object> param){
		List<Map<String,Object>> deptList=dumpPubService.queryDeptList(param);
		if(null==deptList || deptList.size()==0){
			log.info("****************按科室或病区没有查到可以转存的科室，转存失败*****************");
		}else{
			for (Map<String, Object> map : deptList) {
				param.put("pkDept", map.get("pkDept"));
				param.put("nameDept", map.get("nameDept"));
				execDumpData(param);
			}
		}
	}
	/**
	 * 执行患者转存
	 * @param param
	 */
	private void execDumpData(Map<String,Object> param){
		//查询符合条件的转存患者
		List<Map<String,Object>> listPiOp=dumpPubService.queryPiMasterByDept(param);
		if(null==listPiOp || listPiOp.size()==0){
			log.info("*******"+param.get("nameDept")+"科室，没有查询到可以转存的患者信息，转存失败******");
		}else{
			List<String> pkPvList=new ArrayList<String>();
			for (Map<String, Object> map : listPiOp) {
				int cnt=Integer.parseInt(String.valueOf(map.get("cnt")));
				if(cnt>0){
					pkPvList.add(String.valueOf(map.get("pkPv")));
				}
			}
			if(null==pkPvList || pkPvList.size()==0){
				log.info("*******没有查询到次数大于0的可以转存的患者信息，转存失败******");
			}else{
				log.info("*******"+param.get("nameDept")+"科室，开始转存*****");
				try {
					dumpPubService.batDumpData(pkPvList,param.get("euPvtype").toString());
				} catch (Exception e) {
					// TODO: handle exception
					log.info("*****"+param.get("nameDept")+"科室，数据转存出现异常，转存失败："+e.getMessage()+"******");
				}
				log.info("*****"+param.get("nameDept")+"科室，转存完成****");
			}
		}
	}
}
