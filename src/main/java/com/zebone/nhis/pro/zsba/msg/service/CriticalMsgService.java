package com.zebone.nhis.pro.zsba.msg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.support.CvMsg;
import com.zebone.nhis.common.module.base.support.CvMsgCust;
import com.zebone.nhis.common.module.base.support.CvMsgSend;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.msg.dao.CriticalMsgMapper;
import com.zebone.nhis.pro.zsba.msg.vo.ConditionVo;
import com.zebone.nhis.pro.zsba.msg.vo.CriticalMsgCustVo;
import com.zebone.nhis.pro.zsba.msg.vo.PacsVo;
import com.zebone.nhis.pro.zsba.msg.vo.PiPvVo;
import com.zebone.nhis.pro.zsba.msg.vo.Refuse;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
@Service
public class CriticalMsgService {
	@Resource
	private CriticalMsgMapper criticalMsgMapper; 
	/***
	 * 根据条件查询数据
 	 * @param param
	 * @param user
	 */
	public List<CvMsg> queryCriticalMsg(String param,IUser user){
		ConditionVo condition = JsonUtil.readValue(param, ConditionVo.class);
		List<CvMsg> list=criticalMsgMapper.queryCriticalMsg(condition);
		return list;
	}
	
	public List<CvMsg> queryCriticalMsg(ConditionVo condition){
		List<CvMsg> list=criticalMsgMapper.queryCriticalMsg(condition);
		return list;
	}
	/***
	 * 根据用户选择删除消息
 	 * @param param
	 * @param user
	 */
	public void deleteCriticalMsg(String param,IUser user){
		List<String> msgpk=JsonUtil.readValue(param, List.class);
		if(msgpk==null||msgpk.size()<=0){
			throw new BusException("请确认是否选中了消息信息");
		}
		criticalMsgMapper.deleteCriticalMsg(msgpk);
		criticalMsgMapper.deleteMsgCust(msgpk);
	}
	
	/***
	 * 修改用户选择的消息
	 * @param param
	 * @param user
	 */
	public void updateCriticalMsg(String param,IUser user){
		CriticalMsgCustVo cmv=JsonUtil.readValue(param, CriticalMsgCustVo.class);
		CvMsg cm=cmv.getCm();
		List<CvMsgCust> cmlist=cmv.getCvList();	
		if(cm==null){
			throw new BusException("请确认是否选中了消息信息");
		}
		if(CommonUtils.isEmptyString(cm.getPkMsg())){//主键如果为空,新增
			DataBaseHelper.insertBean(cm);
		}else{
			DataBaseHelper.updateBeanByPk(cm,false);
		}
		if(cmlist!=null){
			for (CvMsgCust cvMsgCust : cmlist) {
				cvMsgCust.setPkMsg(cm.getPkMsg());
				if(CommonUtils.isEmptyString(cvMsgCust.getPkMsgcust())){
					DataBaseHelper.insertBean(cvMsgCust);
				}
				else{
					DataBaseHelper.updateBeanByPk(cvMsgCust,false);
				}
			}
		}
		
	}
	/**
	 * 查询消息接受人
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CvMsgCust> queryMsgCust(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		String PkMsg=(String)map.get("pkMsg");
		List<CvMsgCust> list=criticalMsgMapper.queryMsgCust(PkMsg);
		return list;
	}
	/**
	 * 查询接收人类型
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CvMsgCust> queryMsgCust_PvCode(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		Refuse refuse=new Refuse();
		String PvCode=(String)map.get("pvCode");
		List<CvMsgCust> list=new ArrayList<CvMsgCust>();
		if("1".equals(map.get("doctor"))){
			refuse.setDoctor((String)map.get("doctor"));
			refuse.setPvcode(PvCode);
			List<CvMsgCust> temp=criticalMsgMapper.queryMsgCust_PvCode(refuse);
			if(temp!=null){
				for (CvMsgCust cvMsgCust : temp) {
					cvMsgCust.setEuRole("0");
					list.add(cvMsgCust);
				}
			}
			refuse.setDoctor("0");
		}
		if("1".equals(map.get("nurse"))){
	  		refuse.setNurse((String)map.get("nurse"));
			refuse.setPvcode(PvCode);
			List<CvMsgCust> temp=criticalMsgMapper.queryMsgCust_PvCode(refuse);
			for (CvMsgCust cvMsgCust : temp) {
				cvMsgCust.setEuRole("0");
				list.add(cvMsgCust);
			}
			refuse.setNurse("0");
		}
		if("1".equals(map.get("group"))){
	  		refuse.setGroup((String)map.get("group"));
			refuse.setPvcode(PvCode);
			List<CvMsgCust> temp=criticalMsgMapper.queryMsgCust_PvCode(refuse);
			for (CvMsgCust cvMsgCust : temp) {
				cvMsgCust.setEuRole("1");
				list.add(cvMsgCust);
			}
			refuse.setGroup("0");
		}
		if("1".equals(map.get("dept"))){
	  		refuse.setDept((String)map.get("dept"));
			refuse.setPvcode(PvCode);
			List<CvMsgCust> temp=criticalMsgMapper.queryMsgCust_PvCode(refuse);
			for (CvMsgCust cvMsgCust : temp) {
				cvMsgCust.setEuRole("2");
				list.add(cvMsgCust);
			}
			refuse.setDept("0");
		}
		if("1".equals(map.get("region"))){
	  		refuse.setRegion((String)map.get("region"));
			refuse.setPvcode(PvCode);
			List<CvMsgCust> temp=criticalMsgMapper.queryMsgCust_PvCode(refuse);
			for (CvMsgCust cvMsgCust : temp) {
				cvMsgCust.setEuRole("2");
				list.add(cvMsgCust);
			}
			refuse.setRegion("0");
		}
		
		return list;
	}

	
	
	
	/**
	 * 发送消息
	 * @param param
	 * @param user
	 */
	public void sendMsg(String param,IUser user){
		CriticalMsgCustVo cmv=JsonUtil.readValue(param, CriticalMsgCustVo.class);
		CvMsg cm=cmv.getCm();
		cm.setEuStatus("1");
		List<CvMsgCust> cmlist=cmv.getCvList();
		if(cm==null){
			throw new BusException("请确认是否选中了消息信息");
		}
		if(CommonUtils.isEmptyString(cm.getPkMsg())){//主键如果为空,新增
			DataBaseHelper.insertBean(cm);
		}else{
			DataBaseHelper.updateBeanByPk(cm,false);
		}
		if(cmlist!=null){
			for (CvMsgCust cvMsgCust : cmlist) {
				cvMsgCust.setPkMsg(cm.getPkMsg());
				CvMsgSend cms=new CvMsgSend();
				cms.setPkMsg(cm.getPkMsg());
				cms.setDateSend(new Date());
				cms.setCodeRecver(cvMsgCust.getCodeCust());
				cms.setNameRecver(cvMsgCust.getNameCust());
				DataBaseHelper.insertBean(cms);
				if(CommonUtils.isEmptyString(cvMsgCust.getPkMsgcust())){
					DataBaseHelper.insertBean(cvMsgCust);
				}
				else{
					DataBaseHelper.updateBeanByPk(cvMsgCust,false);
				}
			}
		}					
	}
	

	
	
	
	
	/**
	 * 查询发送消息记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CvMsgSend> queryMsgSend(String param,IUser user){
		List<String> cmslist=JsonUtil.readValue(param, List.class);
		if(cmslist==null||cmslist.size()<=0){
			throw new BusException("请确认是否选中了消息信息");
		}
		List<CvMsgSend> list=new ArrayList<CvMsgSend>();
		for (String string : cmslist) {
			List<CvMsgSend> temp=criticalMsgMapper.queryMsgSend(string);
			list.addAll(temp);
		}
		return list;
	}
	
	/**
	 * 阅读消息
	 * @param param
	 * @param user
	 * @return
	 */

	
	public List<CvMsg> queryMsgReplyOne(String param,IUser user){
		ConditionVo cv=JsonUtil.readValue(param, ConditionVo.class);
		String dateE=cv.getDateE();
		if(dateE!=null){
			 dateE=dateE.replaceAll("-", "");
			 cv.setDateE(dateE.substring(0,8)+"235959");
		}
		String dateS=cv.getDateS();
		if(dateS!=null){
			dateS = dateS.replaceAll("-", "");
			cv.setDateS(dateS.substring(0,8)+"000000");
		}
		List<CvMsg> list=criticalMsgMapper.queryMsgReplyOne(cv);
		return list;
	}
	
	
	public PiPvVo queryPiPvMsg(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		String type=(String)map.get("type");
		PiPvVo vo=new PiPvVo();
		if("pv".equals(type)){
			String pvcode=(String)map.get("code");
			String eupvtype=(String)map.get("eupvtype");
			vo=criticalMsgMapper.queryPiPvMsgByPv(pvcode, eupvtype);
		}else if("pi".equals(type)){
			String picode=(String)map.get("code");
			String eupvtype=(String)map.get("eupvtype");
			vo=criticalMsgMapper.queryPiPvMsgByPi(picode, eupvtype);
		} 
		return vo;
	}
	
	public void deleteCvMsgCust(String param,IUser user){
		List<CvMsgCust> cmc=JsonUtil.readValue(param, new TypeReference<List<CvMsgCust>>(){});
		for (CvMsgCust cvMsgCust : cmc) {
			DataBaseHelper.deleteBeanByPk(cvMsgCust);
		}
			
	}
	
	public void updateMsgSend(String param,IUser user){
		CvMsgSend cms=JsonUtil.readValue(param, CvMsgSend.class);
			DataBaseHelper.updateBeanByPk(cms,false);
			criticalMsgMapper.updateMsgSend(cms);
	}
	
	public List<CvMsgCust> queryCust(String param,IUser user){
		List<CvMsgCust> list=JsonUtil.readValue(param,new TypeReference<List<CvMsgCust>>(){});
		List<CvMsgCust> relist=new ArrayList<CvMsgCust>();
		for(int i=0;i<list.size();i++){
			String qq=list.get(i).getEuRole();
			if("2".equals(list.get(i).getEuRole())){
				List<String> emplist=criticalMsgMapper.queryCustByDept(list.get(i).getCodeCust());
				if(emplist==null || emplist.size()<=0){
					throw new BusException("请确认当前科室人员信息");
				}
				List<CvMsgCust> deptlist=criticalMsgMapper.queryCustByEmp(emplist);
				for(int j=0;j<deptlist.size();j++){
					deptlist.get(j).setNote(list.get(i).getNote());
				}
				relist.addAll(deptlist);
			}
			else if("1".equals(list.get(i).getEuRole())){
				List<String> emplist=criticalMsgMapper.queryCustByGroup(list.get(i).getCodeCust());
				if(emplist==null || emplist.size()<=0){
					throw new BusException("请确认当前科室人员信息");
				}
				List<CvMsgCust> grouplist=criticalMsgMapper.queryCustByEmp(emplist);
				for(int j=0;j<grouplist.size();j++){
					grouplist.get(j).setNote(list.get(i).getNote());
				}
				relist.addAll(grouplist);
			}
		}
		return relist;
	}
	
	/**
	 * 查询未阅读消息
	 * @param param
	 * @param user
	 * @return
	 */
	public String queryRead(String param,IUser user){
		List<String> recver =JsonUtil.readValue(param,List.class);
		String num = "0";
		if(recver!=null&&recver.size()>0){
			 num=criticalMsgMapper.queryRead(recver);
		}
		return num;
	}
	
	
	public List<PacsVo> checkMsg(List<CvMsg> list){
		List<String> codelist=new ArrayList<String>();
		for (CvMsg cvMsg : list) {
			codelist.add(cvMsg.getCodeSrc()==null?" ":cvMsg.getCodeSrc());
		}
		//过滤新的危机值
		if(list==null||list.size()<=0){
			List<PacsVo> pace=criticalMsgMapper.queryPacsByNull();
			return pace;
		}
		else {
			List<PacsVo> pace=criticalMsgMapper.queryPacs(codelist);
			return pace;
		}
	}
	public Map<String,Object> updateMsg(List<PacsVo> pacs,String dtsystype){
		Map<String,Object> map=new HashMap<String, Object>();
		List<CvMsg> msglist=new ArrayList<CvMsg>();
		List<CvMsgSend> sendlist=new ArrayList<CvMsgSend>();
		for (PacsVo pacsVo : pacs) {
			if(pacsVo==null){
				continue;
			}
			CvMsg cvmsg=new CvMsg();
			CvMsgSend cms=new CvMsgSend();
			cvmsg.setDateMsg(new Date());
			cvmsg.setCodePv(pacsVo.getInpatientNo());
			cvmsg.setCodePi(pacsVo.getPatientId());
			cvmsg.setNamePi(pacsVo.getName());
			cvmsg.setSubject(pacsVo.getItemName());
			cvmsg.setCodeSrc(pacsVo.getRecordSn());
			cvmsg.setDateSend(new Date());
			cvmsg.setDtSystype(dtsystype);
			cvmsg.setEuLevel("1");	
			cvmsg.setEuStatus("1");
			//发送科室
			cvmsg.setNameDept(pacsVo.getReportUnit());
			//发送人
			cvmsg.setNameSender(pacsVo.getReportDoctor());
			String n=NHISUUID.getKeyId();
			cvmsg.setPkMsg(NHISUUID.getKeyId());
			Map<String,Object> dept=criticalMsgMapper.queryDept(pacsVo.getPatientId());
			if(dept!=null){
			//诊断
			cvmsg.setDescDiag((String)dept.get("admiss_diag_str"));
			
			
			//床号
			cvmsg.setBedNo((String)dept.get("bed_no"));
			//住院号
			cvmsg.setCodePv((String)dept.get("inpatient_no"));
			
			
			}else
				continue;
			//科室
			cvmsg.setNameDeptPv(pacsVo.getApplyUnit());
			//开单医生
			cvmsg.setNameDr(pacsVo.getApplyDoctor());
			msglist.add(cvmsg);
			
			
			cms.setPkMsg(cvmsg.getPkMsg());
			cms.setDateSend(new Date());
			cms.setCodeDept((String)dept.get("dept"));
			sendlist.add(cms);
		}
		map.put(("msglist"), msglist);
		map.put("sendlist", sendlist);
		return map;
	}
	
	public void insertMsg(Map<String,Object> map){
		List<CvMsg> msglist = (List<CvMsg>)map.get("msglist");
		List<CvMsgSend> sendlist =(List<CvMsgSend>)map.get("sendlist");
		for (CvMsg cvMsg : msglist) {
			DataBaseHelper.insertBean(cvMsg);
		}
		for (CvMsgSend cvMsgSend : sendlist) {
			DataBaseHelper.insertBean(cvMsgSend);
		}
	}
	
	
	
}
