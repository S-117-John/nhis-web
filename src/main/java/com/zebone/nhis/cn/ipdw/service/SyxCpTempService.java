package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.util.StringUtil;

import com.zebone.nhis.base.ou.vo.BdOuDeptType;
import com.zebone.nhis.cn.ipdw.dao.SyxCpTempMapper;
import com.zebone.nhis.cn.ipdw.vo.BdFlowSpVo;
import com.zebone.nhis.cn.ipdw.vo.SyxCpTempDeptVo;
import com.zebone.nhis.cn.ipdw.vo.SyxCpTempFormPhaseVo;
import com.zebone.nhis.cn.ipdw.vo.SyxCpTempPhaseOrdVo;
import com.zebone.nhis.cn.ipdw.vo.SyxCpTempPhaseVo;
import com.zebone.nhis.cn.ipdw.vo.SyxCpTempVo;
import com.zebone.nhis.common.module.base.bd.wf.BdFlowBp;
import com.zebone.nhis.common.module.cn.cp.SyxCpTemp;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempCpord;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempDept;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempDiag;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempPhase;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SyxCpTempService {
   
	@Autowired
	public SyxCpTempMapper cpTempDao;
	
	public List<Map<String,Object>> qryCpTemp(String param, IUser user){
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		User u = (User)user;
		String pkDept = u.getPkDept();
		boolean findPublishDept = findPublishDept(pkDept);
		if(!findPublishDept) paramMap.put("pkDept", u.getPkDept());
		List<Map<String,Object>> cpTemp = cpTempDao.qryCpTemp(paramMap);
		return cpTemp;
	}
	private boolean findPublishDept(String pkDept) {
		List<BdOuDeptType> deptType = DataBaseHelper.queryForList("select * from bd_ou_dept_type where pk_dept=? and del_flag='0'", BdOuDeptType.class, new Object[]{pkDept});
		boolean findPublishDept = false;
		if(deptType!=null&&deptType.size()>0){
			for(BdOuDeptType dept:deptType){
				String codeDeptType = dept.getDtDepttype();
				if(StringUtils.isNotBlank(codeDeptType)){
					if(codeDeptType.indexOf("08")!=-1){
						findPublishDept = true;
					}
				}
			}
		}
		return findPublishDept;
	}
	public void delCpTemp(String param, IUser user){
		//pkCptemp
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		String pkCptemp = paramMap.get("pkCptemp")!=null?paramMap.get("pkCptemp").toString():"";
		if(StringUtils.isBlank(pkCptemp)) throw new BusException("??????pkCptemp?????????");
		//????????????
		int tempUse = DataBaseHelper.queryForScalar("select count(1) from cp_rec where pk_cptemp=?", Integer.class, new Object[]{pkCptemp});
		if(tempUse>0){
			throw new BusException("???????????????????????????????????????");
		}
		//????????????
		DataBaseHelper.update("delete from cp_temp where pk_cptemp=?", new Object[]{pkCptemp});
		DataBaseHelper.update("delete from cp_temp_dept where pk_cptemp=?", new Object[]{pkCptemp});
		DataBaseHelper.update("delete from cp_temp_diag where pk_cptemp=?", new Object[]{pkCptemp});
		String dbType = MultiDataSource.getCurDbType();
		if("sqlserver".equals(dbType)){
			DataBaseHelper.update("delete from cp_temp_cpord where exists (select * from cp_temp_phase b where cp_temp_cpord.pk_cpphase = b.pk_cpphase and b.pk_cptemp=?) ", new Object[]{pkCptemp});
		}else{
			DataBaseHelper.update("delete from cp_temp_cpord a where exists (select * from cp_temp_phase b where a.pk_cpphase = b.pk_cpphase and b.pk_cptemp=?) ", new Object[]{pkCptemp});
		}		
		DataBaseHelper.update("delete from cp_temp_phase where pk_cptemp=?", new Object[]{pkCptemp});
	}
	public void submitCpTemp(String param, IUser user){
		//pkCptemp
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		List<String> pkCptemp = paramMap.get("pkCptemp")!=null?(List<String>)paramMap.get("pkCptemp"):null;
		if(pkCptemp==null||pkCptemp.size()<=0) throw new BusException("??????pkCptemp?????????");
		User u = (User)user;
		Date d = new Date();
		//????????????????????????
		for(String pk : pkCptemp){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("pkCptemp", pk);
			VailPhaseDay(m);
		}
		//1.??????????????????????????????????????????
		List<BdFlowSpVo> bdFlow =  cpTempDao.qryBdFlow();
		if(bdFlow!=null && bdFlow.size()>0){
			if(bdFlow.size()>1){
			  throw new BusException("???????????????????????????????????????????????????");	
			}
			List<BdFlowBp> bpList = new ArrayList<BdFlowBp>();
			//2.?????????????????????????????????????????????????????????bd_flow_bp
			BdFlowSpVo flowSp = bdFlow.get(0);
			for(String pktemp :pkCptemp){
				BdFlowBp bdFlowBp = new BdFlowBp();
				bdFlowBp.setPkOrg(u.getPkOrg());
				bdFlowBp.setPkFlowbp(NHISUUID.getKeyId());
				bdFlowBp.setPkFlow(flowSp.getPkFlow());
				bdFlowBp.setPkBppk(pktemp);
				bdFlowBp.setCodeFlow(flowSp.getCodeFlow());
				bdFlowBp.setNameFlow(flowSp.getNameFlow());
				bdFlowBp.setDateSubmit(d);
				bdFlowBp.setTs(d);
				bdFlowBp.setEuResult("0");//0 ????????????1 ?????????9 ??????
				if("0".equals(flowSp.getEuType())){ //0 ?????? 1 ??????
                     //0 ???????????????1?????????
					 bdFlowBp.setPkDept("0".equals(flowSp.getEuDepttype())?flowSp.getPkDept():u.getPkDept());
				}else if("1".equals(flowSp.getEuType())){
					bdFlowBp.setPkEmp(flowSp.getPkEmp());
					bdFlowBp.setNameEmp(flowSp.getNameEmp());
				}
				bdFlowBp.setPkFlowstep(flowSp.getPkFlowstep());
				bpList.add(bdFlowBp);
			}
			if(bpList.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdFlowBp.class), bpList);
		}
		//3.?????????????????????
		Map<String,Object> tempMap = new HashMap<String,Object>();
		tempMap.put("pkCptemp", pkCptemp);
		tempMap.put("pkEmpEntry", u.getPkEmp());
		tempMap.put("nameEmpEntry", u.getNameEmp());
		tempMap.put("dateEntry", d);
		
		DataBaseHelper.update("update cp_temp set eu_status='1', pk_emp_entry=:pkEmpEntry, name_emp_entry=:nameEmpEntry, date_entry=:dateEntry where pk_cptemp in(:pkCptemp) and  eu_status='0' ", tempMap);	
	}
	public void useCpTemp(String param, IUser user){
		//pkCptemp
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		List<String> pkCptemp = paramMap.get("pkCptemp")!=null?(List<String>)paramMap.get("pkCptemp"):null;
		if(pkCptemp==null||pkCptemp.size()<=0) throw new BusException("??????pkCptemp?????????");
		User u = (User) user;
		Date d= new Date();
		Map<String,Object> tempMap = new HashMap<String,Object>();
		tempMap.put("pkCpTemp", pkCptemp);
		tempMap.put("pkEmpPub", u.getPkEmp());
		tempMap.put("nameEmpPub", u.getNameEmp());
		tempMap.put("datePub", d);
		DataBaseHelper.update("update cp_temp  set eu_status='8', pk_emp_pub=:pkEmpPub,  name_emp_pub=:nameEmpPub, date_pub=:datePub where pk_cptemp in(:pkCpTemp) and  eu_status='2' ", tempMap);
		
	}
	public void stopCpTemp(String param, IUser user){
		//pkCptemp/doType
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		String pkCptemp = paramMap.get("pkCptemp")!=null?paramMap.get("pkCptemp").toString():"";
		if(StringUtils.isBlank(pkCptemp)) throw new BusException("??????pkCptemp?????????");
		String isStop = paramMap.get("isStop")!=null?paramMap.get("isStop").toString():"";
		if(StringUtils.isBlank(isStop) ||!("1".equals(isStop)||"0".equals(isStop))) throw new BusException("??????/??????????????????");
		User u = (User) user;
		Date d= new Date();
		if("1".equals(isStop)){ //1.??????/0 ??????
			DataBaseHelper.update("update cp_temp set eu_status='9', pk_emp_stop=?, name_emp_stop=?, date_stop=? where pk_cptemp=? and eu_status='8' ", new Object[]{u.getPkEmp(),u.getNameEmp(),d,pkCptemp});
			
		}else if("0".equals(isStop)){
			DataBaseHelper.update("update cp_temp set eu_status='8',pk_emp_stop=?,name_emp_stop=?, date_stop=? where pk_cptemp=? and eu_status='9' ", new Object[]{u.getPkEmp(),u.getNameEmp(),d,pkCptemp});
		}
	}
	public SyxCpTempVo qryCpTempDetail(String param, IUser user){
		SyxCpTempVo cpTemp =  getCpTempDetail(param, user,false);
		return cpTemp;
	}
	public SyxCpTempPhaseOrdVo qryCpTempPhaseOrd(String param, IUser user){
		//pkCpphase
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		String pkCpphase = paramMap.get("pkCpphase")!=null?paramMap.get("pkCpphase").toString():"";
		if(StringUtils.isBlank(pkCpphase)) throw new BusException("??????pkCpphase?????????");
		SyxCpTempPhaseOrdVo ordVo = qryCpTempPhaseOrd(paramMap);
		return ordVo;
	}
	private SyxCpTempPhaseOrdVo qryCpTempPhaseOrd(Map<String, Object> paramMap) {
		SyxCpTempPhaseOrdVo ordVo = new SyxCpTempPhaseOrdVo();
		List<SyxCpTempCpord> phaseOrd = cpTempDao.qryCpTempPhaseOrd(paramMap);
		if(phaseOrd!=null && phaseOrd.size()>0){
			 List<SyxCpTempCpord> doctorWork = new ArrayList<SyxCpTempCpord>();
			 List<SyxCpTempCpord> mainOrd = new ArrayList<SyxCpTempCpord>();
			 List<SyxCpTempCpord> nsWork = new ArrayList<SyxCpTempCpord>();
			for(SyxCpTempCpord cpOrd :phaseOrd){
				if("0".equals(cpOrd.getEuTasktype())){ //0??????
					mainOrd.add(cpOrd);
				}else if("1".equals(cpOrd.getEuTasktype())){ //1 ????????????
					doctorWork.add(cpOrd);
				}else if("2".equals(cpOrd.getEuTasktype())){//2 ????????????
					nsWork.add(cpOrd);
				}
			}
			ordVo.setDoctorWork(doctorWork);
			ordVo.setNsWork(nsWork);
			ordVo.setPhaseOrd(mainOrd);
		}
		return ordVo;
	}
	public SyxCpTempVo saveCpTempDetail(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		SyxCpTempVo tempDetail = JsonUtil.readValue(param, SyxCpTempVo.class); 
		if(tempDetail==null) throw new BusException("???????????????????????????????????????");
		SyxCpTemp tempBase = tempDetail.getCpTemp(); 
		List<SyxCpTempDiag> cpTempDiag = tempDetail.getCpTempDiag();
		List<SyxCpTempDeptVo> cpTempDept = tempDetail.getCpTempDept();
		String  isUpdate = tempDetail.getIsUpdate();
        String tabPage = tempDetail.getTabPage();
        List<SyxCpTempCpord> cpTempPhaseOrdAdd = new ArrayList<SyxCpTempCpord>();
        List<SyxCpTempCpord> cpTempPhaseOrdDel = new ArrayList<SyxCpTempCpord>();
        List<SyxCpTempCpord> cpTempPhaseOrdUpd = new ArrayList<SyxCpTempCpord>();
        List<SyxCpTempPhase> cpTempPhaseSaved = new ArrayList<SyxCpTempPhase>();
        SyxCpTempPhaseVo cpTempPhaseSave = tempDetail.getCpTempPhaseVo(); //????????????
        SyxCpTempPhase curTempPhase = null;		
        List<SyxCpTempPhaseOrdVo> cpTempPhaseOrdSave = cpTempPhaseSave!=null ?  cpTempPhaseSave.getPhaseOrd():null; //?????????????????????
        List<String> delOrdByPhase = new ArrayList<String>();
        User u = (User)user;
        String pkCptemp="";
        String pkCpphase="";
        Date d = new Date();
		//????????????
		if("1".equals(isUpdate) ){ //??????
		    pkCptemp =  tempDetail.getPkCptemp(); 
			if(StringUtils.isNotBlank(pkCptemp))  {
				if("temp".equals(tabPage)){
					DataBaseHelper.update("delete from cp_temp where pk_cptemp=?", new Object[]{pkCptemp});
				}else if("diag".equals(tabPage)){
					DataBaseHelper.update("delete from cp_temp_diag where pk_cptemp=?", new Object[]{pkCptemp});
				}else if("dept".equals(tabPage)){
					DataBaseHelper.update("delete from cp_temp_dept where pk_cptemp=?", new Object[]{pkCptemp});
				}else if("phase".equals(tabPage)){
				    pkCpphase = cpTempPhaseSave!=null?cpTempPhaseSave.getPkCpphase():"";
					if(StringUtils.isNotBlank(pkCpphase)) {
						DataBaseHelper.update("delete from cp_temp_phase where pk_cpphase=?", new Object[]{cpTempPhaseSave.getPkCpphase()});
					}//????????????????????????
				}
			}
		}else{ //??????/?????????
		    pkCptemp = NHISUUID.getKeyId();
			if(tempBase==null){
				throw new BusException("???????????????????????????");
			}
		}
		if(tempBase!=null){
			int tempCodeCount = DataBaseHelper.queryForScalar("select count(1) from cp_temp where code_cp=? and version=? ", Integer.class, new Object[]{tempBase.getCodeCp(),tempBase.getVersion()});
			if(tempCodeCount>0) throw new BusException("?????????  ?????????"+tempBase.getCodeCp()+"???  ????????????"+tempBase.getVersion()+"??????????????????");
			int tempNameCount = DataBaseHelper.queryForScalar("select count(1) from cp_temp where name_cp=? and version=? ", Integer.class, new Object[]{tempBase.getNameCp(),tempBase.getVersion()});
			if(tempNameCount>0) throw new BusException("?????????  ?????????"+tempBase.getNameCp()+"???  ????????????"+tempBase.getVersion()+"??????????????????");
			
			tempDetail.setPkCptemp(pkCptemp);//????????????
			tempBase.setPkCptemp(pkCptemp);
			tempBase.setEuStatus("0"); //??????
			tempBase.setDelFlag("0");
			tempBase.setPkDept(u.getPkDept());
		}
		if(cpTempDiag!=null){
			for(SyxCpTempDiag diag : cpTempDiag){
				diag.setPkCpdiag(NHISUUID.getKeyId());
				diag.setPkCptemp(pkCptemp);
				diag.setPkOrg(u.getPkOrg());
				diag.setCreator(u.getPkEmp());
				diag.setCreateTime(d);
				diag.setTs(d);
				
			}
		}
		if(cpTempDept!=null){
			for(SyxCpTempDeptVo dept : cpTempDept){
				dept.setPkCpdept(NHISUUID.getKeyId()); 
				dept.setPkCptemp(pkCptemp);
				dept.setPkOrg(u.getPkOrg());
				dept.setCreator(u.getPkEmp());
				dept.setCreateTime(d);
				dept.setTs(d);
			}
		}
		if(cpTempPhaseSave!=null) {
			if("delete".equals(cpTempPhaseSave.getPhaseStatus())){
				delOrdByPhase.add(cpTempPhaseSave.getPkCpphase());
				cpTempPhaseSave = null;
			}else{
				curTempPhase = new SyxCpTempPhase(); 
				pkCpphase = StringUtils.isBlank(pkCpphase)?NHISUUID.getKeyId():pkCpphase;
				cpTempPhaseSave.setPkCpphase(pkCpphase);
				cpTempPhaseSave.setPkCptemp(pkCptemp);
				BeanUtils.copyProperties(curTempPhase, cpTempPhaseSave);
			}
		}
		if(cpTempPhaseOrdSave!=null){
			 for(SyxCpTempPhaseOrdVo ord :cpTempPhaseOrdSave){
				if("insert".equals(ord.getRowStatus())){
					ord.setPkCpord(StringUtil.isBlank(ord.getPkCpord())?NHISUUID.getKeyId():ord.getPkCpord());
					ord.setPkOrg(u.getPkOrg());
					ord.setPkCpphase(pkCpphase);
					ord.setDelFlag("0");
					ord.setTs(d);
					ord.setCreator(u.getPkEmp());
					ord.setCreateTime(d);
					cpTempPhaseOrdAdd.add(ord);
				}else if("update".equals(ord.getRowStatus())){
					ord.setTs(d);
					cpTempPhaseOrdUpd.add(ord);
				}else if("delete".equals(ord.getRowStatus())){
					cpTempPhaseOrdDel.add(ord);
				}
			}
	    }
		//?????????
		if(tempBase!=null) DataBaseHelper.insertBean(tempBase);
		if(cpTempDiag!=null && cpTempDiag.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SyxCpTempDiag.class), cpTempDiag);
		if(cpTempDept!=null && cpTempDept.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SyxCpTempDept.class), cpTempDept);
		if(curTempPhase!=null) DataBaseHelper.insertBean(curTempPhase);
		if(cpTempPhaseOrdAdd.size()>0){
			 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SyxCpTempCpord.class), cpTempPhaseOrdAdd);
		}
		if(cpTempPhaseOrdDel.size()>0){
			DataBaseHelper.batchUpdate("delete from cp_temp_cpord where pk_cpord=:pkCpord", cpTempPhaseOrdDel);
		}
		if(cpTempPhaseOrdUpd.size()>0){
			DataBaseHelper.batchUpdate("update cp_temp_cpord set flag_print=:flagPrint, code_op=:codeOp,name_op=:nameOp,dt_anae=:dtAnae,desc_op=:descOp, sortno=:sortno,eu_always=:euAlways,name_form=:nameForm,eu_cpordtype=:euCpordtype,code_ordtype=:codeOrdtype,pk_ord=:pkOrd,code_ord=:codeOrd,name_ord=:nameOrd,code_supply=:codeSupply,code_freq=:codeFreq,dt_pharm=:dtPharm,flag_nec=:flagNec,note=:note, ts=:ts where pk_cpord=:pkCpord", cpTempPhaseOrdUpd);
		}
		if(delOrdByPhase.size()>0){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pkPhase", delOrdByPhase);
			DataBaseHelper.update("delete from cp_temp_cpord where pk_cpphase in(:pkPhase)", map);
		}
		if(StringUtils.isNotBlank(pkCptemp)){
			cpTempPhaseSaved = DataBaseHelper.queryForList("select * from cp_temp_phase where pk_cptemp=? order by sortno", SyxCpTempPhase.class, new Object[]{pkCptemp});
			tempDetail.setCpTempPhase(cpTempPhaseSaved);
		}
		return tempDetail;
	}
	
	public SyxCpTempVo cloneTemplate(String param, IUser user){
		SyxCpTempVo cpTemp =  getCpTempDetail(param, user,true);
		return cpTemp;
	}
	private SyxCpTempVo getCpTempDetail(String param, IUser user ,boolean isClone){
		//pkCptemp 
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		String pkCptemp = paramMap.get("pkCptemp")!=null?paramMap.get("pkCptemp").toString():"";
		if(StringUtils.isBlank(pkCptemp)) throw new BusException("??????pkCptemp?????????");
		SyxCpTempVo tempDetail = new SyxCpTempVo();
		SyxCpTemp tempBase = cpTempDao.qryCpTempBase(paramMap);
		tempDetail.setCpTemp(tempBase);
		tempDetail.setPkCptemp(tempBase.getPkCptemp());
		List<BdFlowSpVo> flowSp = cpTempDao.qryCpTempFlow(paramMap);
		tempDetail.setFlowSp(flowSp);
		List<SyxCpTempDiag> cpTempDiag = cpTempDao.qryCpTempDiag(paramMap);
		tempDetail.setCpTempDiag(cpTempDiag);
		List<SyxCpTempDeptVo> cpTempDept = cpTempDao.qryCpTempDept(paramMap);
		tempDetail.setCpTempDept(cpTempDept);
		List<SyxCpTempPhase> cpTempPhase = cpTempDao.qryCpTempPhase(paramMap);
		tempDetail.setCpTempPhase(cpTempPhase);		
		if(isClone){
			User u = (User)user;
			List<String> phasePk = new ArrayList<String>();
			Map<String,String> phaseKeyOld = new HashMap<String,String>();
			String pkCptempNew = NHISUUID.getKeyId();
			tempBase.setPkCptemp(pkCptempNew);
			tempBase.setEuStatus("0");		//??????
			tempBase.setVersion(getVersion(tempBase.getCodeCp(),u.getPkDept()));
			tempDetail.setCpTemp(tempBase);
			tempDetail.setPkCptemp(pkCptempNew);
			DataBaseHelper.insertBean(tempBase);
			if(cpTempDiag!=null && cpTempDiag.size()>0){
				for(SyxCpTempDiag diag :cpTempDiag){
					diag.setPkCpdiag(NHISUUID.getKeyId());
					diag.setPkCptemp(pkCptempNew);
				}
				tempDetail.setCpTempDiag(cpTempDiag);
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SyxCpTempDiag.class),cpTempDiag);
			}
			if(cpTempDept!=null && cpTempDept.size()>0){
				for(SyxCpTempDeptVo dept :cpTempDept){
					dept.setPkCpdept(NHISUUID.getKeyId());
					dept.setPkCptemp(pkCptempNew);
				}
				tempDetail.setCpTempDept(cpTempDept);
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SyxCpTempDept.class),cpTempDept);
			}
			if(cpTempPhase!=null && cpTempPhase.size()>0){
				
				for(SyxCpTempPhase phase :cpTempPhase){
					String oldPhase = phase.getPkCpphase();
					String newPhase = NHISUUID.getKeyId();
					phaseKeyOld.put(oldPhase, newPhase);
					phasePk.add(oldPhase);
					phase.setPkCpphase(newPhase);
					phase.setPkCptemp(pkCptempNew);
				}
				for(SyxCpTempPhase phase :cpTempPhase){
					String pkCpPhasePre = phase.getPkCpphasePre();
					if(StringUtils.isNotBlank(pkCpPhasePre))phase.setPkCpphasePre(phaseKeyOld.get(pkCpPhasePre));
				}
				tempDetail.setCpTempPhase(cpTempPhase);
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SyxCpTempPhase.class),cpTempPhase);
			}
			if(phasePk.size()>0){
				List<SyxCpTempCpord> cloneOrd = new ArrayList<SyxCpTempCpord>();
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("pkCpphase", phasePk);
				List<SyxCpTempPhaseOrdVo> ordVo = DataBaseHelper.queryForList("select * from cp_temp_cpord where pk_cpphase in(:pkCpphase)", SyxCpTempPhaseOrdVo.class, map);
				if(ordVo!=null && ordVo.size()>0){
					for(SyxCpTempPhaseOrdVo ord :ordVo){
						ord.setPkCpord(NHISUUID.getKeyId());
						ord.setPkCpphase(phaseKeyOld.get(ord.getPkCpphase()));
						cloneOrd.add(ord);
					}
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SyxCpTempCpord.class), cloneOrd);
				}
			}
			tempDetail.setFlowSp(null);
			tempDetail.setCpTempForm(null); //?????????
		}
		return tempDetail;
	}
	
	private double getVersion(String cpCode,String dept) {
		Double ret = DataBaseHelper.queryForScalar("select max(version) from cp_temp where code_cp = ? and pk_dept=?", Double.class, new Object[]{cpCode,dept});
		return ret==null ? 1 : ret+1;
	}
	
	public SyxCpTempVo getCpTempDt(String param, IUser user){
		//pkCptemp/pkCpphase/isPkCptemp/tabPage 
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		String isPkCptemp = paramMap.get("isPkCptemp")!=null?paramMap.get("isPkCptemp").toString():"";
		String tabPage = paramMap.get("tabPage")!=null?paramMap.get("tabPage").toString():"";
		SyxCpTempVo tempDetail = new SyxCpTempVo();
		if("1".equals(isPkCptemp)){
			String pkCptemp = paramMap.get("pkCptemp")!=null?paramMap.get("pkCptemp").toString():"";
			if(StringUtils.isBlank(pkCptemp)) throw new BusException("??????pkCptemp?????????");
			if("diag".equals(tabPage)) {
				List<SyxCpTempDiag> cpTempDiag = cpTempDao.qryCpTempDiag(paramMap);
				tempDetail.setCpTempDiag(cpTempDiag);
			}else if("dept".equals(tabPage)){
				List<SyxCpTempDeptVo> cpTempDept = cpTempDao.qryCpTempDept(paramMap);
				tempDetail.setCpTempDept(cpTempDept);
			}else if("form".equals(tabPage)){
				List<SyxCpTempFormPhaseVo> cpTempForm = new ArrayList<SyxCpTempFormPhaseVo>();
				paramMap.put("euTasktype", "0");//0 ?????????1 ???????????????2 ????????????
				List<SyxCpTempFormPhaseVo> phase =  cpTempDao.qryCpTempForm(paramMap);
				paramMap.put("euTasktype", "1");//0 ?????????1 ???????????????2 ????????????
				List<SyxCpTempFormPhaseVo> doctorWork =  cpTempDao.qryCpTempForm(paramMap);
				paramMap.put("euTasktype", "2");//0 ?????????1 ???????????????2 ????????????
				List<SyxCpTempFormPhaseVo> nsWork =  cpTempDao.qryCpTempForm(paramMap);
				if(phase!=null&&phase.size()>0){
					for(SyxCpTempFormPhaseVo phaseVo :phase){
						phaseVo.setOrders(phaseVo.getTabOrds());
						String pkPhase  = phaseVo.getPkCpphase();
						if(doctorWork!=null && doctorWork.size()>0){
							for(SyxCpTempFormPhaseVo doctor :doctorWork){
								String doctorPkPhase  = doctor.getPkCpphase();
								if(pkPhase.equals(doctorPkPhase)){
									phaseVo.setClinics(doctor.getTabOrds());
									break;
								}
							}
						}
						if(nsWork!=null && nsWork.size()>0){
							for(SyxCpTempFormPhaseVo ns :nsWork){
								String nsPkPhase  = ns.getPkCpphase();
								if(pkPhase.equals(nsPkPhase)){
									phaseVo.setNursings(ns.getTabOrds());
									break;
								}
							}						
					    }
						cpTempForm.add(phaseVo);
					}
				}else if(doctorWork!=null && doctorWork.size()>0){
					for(SyxCpTempFormPhaseVo doctor :doctorWork){
						doctor.setClinics(doctor.getTabOrds());
						String doctorPkPhase  = doctor.getPkCpphase();
						if(nsWork!=null && nsWork.size()>0){
							for(SyxCpTempFormPhaseVo ns :nsWork){
								String nsPkPhase  = ns.getPkCpphase();
								if(doctorPkPhase.equals(nsPkPhase)){
									doctor.setNursings(ns.getTabOrds());
									break;
								}
							}						
					    }
						cpTempForm.add(doctor);
					}
				}else if(nsWork!=null && nsWork.size()>0){
					for(SyxCpTempFormPhaseVo ns :nsWork){
						ns.setNursings(ns.getTabOrds()); 
						cpTempForm.add(ns);
					}		
				}
        	tempDetail.setCpTempForm(cpTempForm);
			}
		}else {
			String pkCpphase = paramMap.get("pkCpphase")!=null?paramMap.get("pkCpphase").toString():"";
			if(StringUtils.isBlank(pkCpphase)) throw new BusException("??????pkCpphase?????????");
			SyxCpTempPhaseOrdVo ordVo = qryCpTempPhaseOrd(paramMap);
			tempDetail.setCpTempPhaseOrdVo(ordVo);
		}
		return tempDetail;
	}
	  //????????????????????????????????? /pk_cptemp
    private void VailPhaseDay(Map<String,Object>paramMap )
    {
    	SyxCpTemp tempBase = cpTempDao.qryCpTempBase(paramMap);
    	String tempName = tempBase.getNameCp();
    	double version = tempBase.getVersion();
    	String message = "???"+tempName+"???\n???????????????"+version+"???\n";
    	List<SyxCpTempDiag> cpTempDiag = cpTempDao.qryCpTempDiag(paramMap);
    	if(cpTempDiag==null || cpTempDiag.size()<=0){
    		throw new BusException(message+"????????????????????????????????????");
    	}
    	List<SyxCpTempDeptVo> cpTempDept = cpTempDao.qryCpTempDept(paramMap);
    	if(cpTempDept==null || cpTempDept.size()<=0){
    		throw new BusException(message+"????????????????????????????????????");
    	}
    	List<SyxCpTempPhase> cpTempPhase = cpTempDao.qryCpTempPhase(paramMap);
    	if(cpTempPhase==null || cpTempPhase.size()<2){	
    		throw new BusException(message+"?????????????????????????????????");
    	}
    	int count  = cpTempPhase.size();
        for (int i = 0; i < count; i++)
        {
        	SyxCpTempPhase p = cpTempPhase.get(i);
            if (i == 0)
            {
            	if( !"0".equals(p.getEuType()) || StringUtils.isNotBlank(p.getPkCpphasePre())){ //?????????
            		throw new BusException(message+"??????????????????"+p.getNamePhase()+"?????????????????????????????????!");
            	}
                if (1 != p.getDaysMin())
                {
                	throw new BusException(message+"?????????????????????????????????1????????????!");
                }
            }else if (i == count - 1)
            {
            	if( !"9".equals(p.getEuType())){ //?????????
            		throw new BusException(message+"??????????????????"+p.getNamePhase()+"?????????????????????????????????!");
            	}
                if (p.getDaysMax()>tempBase.getDaysMax() || p.getDaysMax()<tempBase.getDaysMin())
                {
                	throw new BusException(message+"???????????????????????????????????????????????????"+tempBase.getDaysMin()+"-"+tempBase.getDaysMax()+"????????????????????????!");
                }
                if(count==2){
                	SyxCpTempPhase pnext = cpTempPhase.get(1);
                	SyxCpTempPhase p0 = cpTempPhase.get(0);
                    if ((pnext.getDaysMin() > p0.getDaysMax() + 1) || (pnext.getDaysMax() <= p0.getDaysMax())) //??????????????????????????????????????????????????????????????????????????????/???????????????????????????????????????????????????????????????
                    {
                    	throw new BusException(message+"???????????????????????????????????????!\n?????????????????????????????????????????????????????????????????????????????????\n??????????????????????????????????????????????????????????????????");
                    }
                }
            }else
            {
            	if( !"1".equals(p.getEuType())){ //?????????
            		throw new BusException(message+"??????????????????"+p.getNamePhase()+"?????????????????????????????????!");
            	}
            	SyxCpTempPhase pnext = cpTempPhase.get(i + 1);
                if ((pnext.getDaysMin() > p.getDaysMax() + 1) || (pnext.getDaysMax() <= p.getDaysMax())) //??????????????????????????????????????????????????????????????????????????????/???????????????????????????????????????????????????????????????
                {
                	throw new BusException(message+"???????????????????????????????????????!\n?????????????????????????????????????????????????????????????????????????????????\n??????????????????????????????????????????????????????????????????");
                }
            }
            paramMap.put("pkCpphase", p.getPkCpphase());
        	List<SyxCpTempCpord> phaseOrd = cpTempDao.qryCpTempPhaseOrd(paramMap);
        	if(phaseOrd==null || phaseOrd.size()<=0) throw new BusException(message+"?????????????????????????????????????????????????????????"+p.getNamePhase()+"??????");
        }
    }
	
}
