package com.zebone.nhis.ma.pub.syx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdPivas;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.syx.dao.PivasSystemMapper;
import com.zebone.nhis.ma.pub.syx.vo.OrderOccVo;
import com.zebone.nhis.ma.pub.syx.vo.Torders;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 住院静配发药pivas接口:中二需求
 * @author jd_em
 *
 */
@Service
public class PivasSystemHandler {
	
	@Resource
	private PivasSystemMapper pivasSystemMapper;
	@Resource
	private PivasSystemService pivasSystemService;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private Logger logger = LoggerFactory.getLogger("ma.syxInterface");
	
	public Object invokeMethod(String methodName,Object...args){
		Object result = null;
		
    	switch(methodName){
    	case "saveTorders":
    		this.saveTorders(args);
    		break;
    	case "qryPivasCgItem":
    		result = this.qryPivasCgItem(args);
    		break;
    	case "updatePivas":
    		this.updatePivas(args);
    		break;
    	case "checkPivasOut":
    		result=this.checkPivasOut(args);
    		break;
    	case "updatePivasOut":
    		this.updatePivasOut(args);
    		break;
    	case "updatePivasOutByCancelEx":
    		this.updatePivasOutByCancelEx(args);
    		break;
    	case "updatePivasOutByCancelChk":
    		this.updatePivasOutByCancelChk(args);
    		break;
    	}
    	
    	return result;
	}
	
	/**
	 * 
	 * @param args
	 */

	@SuppressWarnings("unchecked")
	public void saveTorders(Object...args){
		List<ExPdApplyDetail> extDtlist = (List<ExPdApplyDetail>)args[0];
		List<String> pkPdapdts=new ArrayList<String>();
		for (ExPdApplyDetail expddt : extDtlist) {
			if("1".equals(expddt.getFlagPivas())){//判断是否是静配发药
				pkPdapdts.add(expddt.getPkPdapdt());
			}
		}
		if(pkPdapdts.size()<=0)return ;
		try {
			List<Torders> tordList=pivasSystemMapper.qryPivasList(pkPdapdts);
			String params=JsonUtil.writeValueAsString(tordList);
			tordList=JsonUtil.readValue(params, new TypeReference<List<Torders>>() {});
			DataSourceRoute.putAppId("pivasdb");//切换至正式数据源
			for (Torders tord : tordList) {
				String age=tord.getAge();
				tord.setCancelflag(0);
				tord.setCanceltime(DateUtils.strToDate("1900-01-01 00:00:00"));
				tord.setCnsmflag(0);
				tord.setCnsmtime(DateUtils.strToDate("1900-01-01 00:00:00"));
				String daid=tord.getExecdaid().toString();
				daid=daid.substring(0, daid.length()-2);
				tord.setDaid(daid);
				tord.setAge(age.replace("岁", "Y").replace("月","M").replace("日", "D"));
			}
			for (Torders torders : tordList) {
				if(CommonUtils.isEmptyString(torders.getDieasename())){
					throw new BusException(torders.getDepartmentname()+",床号："+torders.getSickbedno()+",患者姓名："+torders.getPatientname()+",诊断信息未录入，请联系医护人员补录数据！");
				}else if(!CommonUtils.isNotNull(torders.getEmployeeid())){
					throw new BusException("医生："+torders.getEmployeename()+",工号："+torders.getEmployeeno()+",对应老系统人员id未维护,请联系管理员进行数据维护【BD_OU_EMPLOYEE.OLD_ID】!");
				}
			}
			pivasSystemService.saveTorders(tordList);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.info("住院静配数据上传接口："+e.getMessage());
			throw new BusException(e.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	private List<Map<String,Object>> qryPivasCgItem(Object...args){
		List<Map<String,Object>> itemMap = new ArrayList<>();
		
		try {
			DataSourceRoute.putAppId("pivasdb");//切换至正式数据源
			//DataSourceRoute.putAppId("yy004002");
			itemMap = pivasSystemService.qryPivasCgItem();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DataSourceRoute.putAppId("default");
		}
		
		return itemMap;
	}
	
	@SuppressWarnings("unchecked")
	private void updatePivas(Object...args){
		List<String> pkList = (List<String>)args[0];
		try {
			DataSourceRoute.putAppId("pivasdb");//切换至正式数据源
			//DataSourceRoute.putAppId("yy004002");
			pivasSystemService.updatePivas(pkList);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	/**
	 * 获取已经入仓的静配数据返回前台，做提示用途
	 * @param args
	 * @return
	 */
	public List<Map<String,Object>> checkPivasOut(Object...args){
		List<String> ordsnKeys;
		List<String> ordsnGroupKeys;
		try {
			String param=(String) args[0];
			List<OrderOccVo> occVos=queryPivasData(param);
			ordsnKeys = new ArrayList<String>();
			ordsnGroupKeys = new ArrayList<String>();
			ordsnKeys = new ArrayList<String>();
			ordsnGroupKeys = new ArrayList<String>();
			for (OrderOccVo map : occVos) {
				if(!"1".equals(map.getFlagPivas()))continue;//过滤掉非静配药品
				Date datePlan=map.getDatePlan();
				String str=sdf.format(datePlan).substring(6,8);
				String ordsnKey=map.getOrdsn().toString()+str;
				String ordsnGroupKey=ordsnKey+"-"+DateUtils.dateToStr("yyyy-MM-dd HH:mm", datePlan)+":00";
				ordsnKeys.add(ordsnKey);
				ordsnGroupKeys.add(ordsnGroupKey);
			}
			if(ordsnKeys.size()<=0)return null;
			DataSourceRoute.putAppId("pivasdb");
			List<Map<String,Object>> result=pivasSystemService.checkPivasOut(ordsnKeys,ordsnGroupKeys);

			DataSourceRoute.putAppId("default");
			List<ExPdPivas> pivasList=new ArrayList<ExPdPivas>();
			for (Map<String, Object> map : result) {
				for (OrderOccVo  occvo: occVos) {
					String datePlan=DateUtils.dateToStr("YYYY-MM-dd HH:mm:ss", occvo.getDatePlan());
					String str=datePlan.substring(8,10);
					String ordsnKey=occvo.getOrdsn()+str;
					String ordsnGroupKey=ordsnKey+"-"+DateUtils.dateToStr("yyyy-MM-dd HH:mm", occvo.getDatePlan())+":00";
					if(ordsnGroupKey.equals(map.get("ordsnPk"))){
						ExPdPivas pivas=new ExPdPivas();
						pivas.setPkPdpivas(NHISUUID.getKeyId());
						pivas.setPkOrg(UserContext.getUser().getPkOrg());
						pivas.setPkExocc(occvo.getPkExocc());
						pivas.setPkPdapdt(occvo.getPkPdapdt());
						pivas.setFlagIn("1");
						pivasList.add(pivas);
					}
				}
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdPivas.class),pivasList);
			return result;
		} catch (Exception e) {
			logger.info("住院静配退药数据检验接口："+e.getMessage());
			return null;
		}
		finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	/**
	 * 根据pkCnords查询医嘱序号和计划执行时间（停止核对时需要）
	 * @param param
	 * @return
	 */
	private List<OrderOccVo> queryPivasData(String param){
		List<String> pkCnords=new ArrayList<String>();
		List<OrderOccVo> occVos=JsonUtil.readValue(param, new TypeReference<List<OrderOccVo>>() {});
		for (OrderOccVo occvo : occVos) {
			if(occvo.getDatePlan()==null){
				pkCnords.add(occvo.getPkCnord());
			}
		}
		if(pkCnords.size()!=0){
			return pivasSystemMapper.queryPivasData(pkCnords);
		}else{
			return occVos;
		}
		
		
	}
	/**
	 * 住院静配退药接口，更新取消标志
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public void updatePivasOut(Object... args){
		try {
			String param=(String) args[0];
			List<Map<String,Object>> paramLists=JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {});
			List<Map<String,Object>> exOrdOccList=new ArrayList<Map<String,Object>>();
			for (Map<String, Object> map : paramLists) {
				List<Map<String,Object>> mapList=(List<Map<String, Object>>) map.get("exOrdOccList");
				exOrdOccList.addAll(mapList);
			}
			List<Map<String,Object>> paramList=new ArrayList<Map<String,Object>>();
			String dateNow=DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> map : exOrdOccList) {
				if(!"1".equals(map.get("flagPivas")))continue;
				Map<String,Object> paramMap=new HashMap<String,Object>();
				String dateStr=map.get("datePlan").toString();
				String ordsn=map.get("ordsn")+dateStr.substring(6,8);
				paramMap.put("ordsn", ordsn);
				String datePlan=dateStr.substring(0, 4)+"-"+dateStr.substring(4,6)+"-"+dateStr.substring(6,8);
				String timeStr=dateStr.substring(8,10)+":"+dateStr.substring(10,12);
				paramMap.put("datePlan", datePlan);
				paramMap.put("timePlan", timeStr);
				paramMap.put("dateNow", dateNow);
				paramList.add(paramMap);
			}
			DataSourceRoute.putAppId("pivasdb");
			pivasSystemService.updatePivasOut(paramList);
		} catch (Exception e) {
			logger.info("住院静配退药数据修改接口："+e.getMessage());
			throw new BusException(e.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	/**
	 * 住院静配退药接口，更新取消标志
	 * @param args
	 */
	public void updatePivasOutByCancelEx(Object... args){
		try {
			String param=(String) args[0];
			List<Map<String,Object>> exOrdOccList=JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {});
			List<Map<String,Object>> paramList=new ArrayList<Map<String,Object>>();
			String dateNow=DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> map : exOrdOccList) {
				if(!"1".equals(map.get("flagPivas")))continue;
				Map<String,Object> paramMap=new HashMap<String,Object>();
				String dateStr=map.get("datePlan").toString();
				String ordsn=map.get("ordsn")+dateStr.substring(6,8);
				paramMap.put("ordsn", ordsn);
				String datePlan=dateStr.substring(0, 4)+"-"+dateStr.substring(4,6)+"-"+dateStr.substring(6,8);
				String timeStr=dateStr.substring(8,10)+":"+dateStr.substring(10,12);
				paramMap.put("datePlan", datePlan);
				paramMap.put("timePlan", timeStr);
				paramMap.put("dateNow", dateNow);
				paramList.add(paramMap);
			}
			DataSourceRoute.putAppId("pivasdb");
			pivasSystemService.updatePivasOut(paramList);
		} catch (Exception e) {
			logger.info("住院静配退药数据修改接口："+e.getMessage());
			throw new BusException(e.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
	/**
	 * 住院静配退药接口，更新取消标志(停止医嘱核对时)
	 * @param args
	 */
	public void updatePivasOutByCancelChk(Object... args){
		try {
			String param=(String) args[0];
			List<OrderOccVo> exOrdOccList=queryPivasData(param);
			List<Map<String,Object>> paramList=new ArrayList<Map<String,Object>>();
			String dateNow=DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
			for (OrderOccVo map : exOrdOccList) {
				if(!"1".equals(map.getFlagPivas()))continue;
				Map<String,Object> paramMap=new HashMap<String,Object>();
				String dateStr=sdf.format(map.getDatePlan());
				String ordsn=map.getOrdsn()+dateStr.substring(6,8);
				paramMap.put("ordsn", ordsn);
				String datePlan=dateStr.substring(0, 4)+"-"+dateStr.substring(4,6)+"-"+dateStr.substring(6,8);
				String timeStr=dateStr.substring(8,10)+":"+dateStr.substring(10,12);
				paramMap.put("datePlan", datePlan);
				paramMap.put("timePlan", timeStr);
				paramMap.put("dateNow", dateNow);
				paramList.add(paramMap);
			}
			DataSourceRoute.putAppId("pivasdb");
			pivasSystemService.updatePivasOut(paramList);
		} catch (Exception e) {
			logger.info("住院静配退药数据修改接口："+e.getMessage());
			throw new BusException(e.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
}
