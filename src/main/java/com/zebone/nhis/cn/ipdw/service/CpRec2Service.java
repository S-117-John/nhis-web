package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CpRec2Mapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CnIpPressVO;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.cp.CpIntegrateParam;
import com.zebone.nhis.common.module.cn.cp.CpRec;
import com.zebone.nhis.common.module.cn.cp.CpRecDt;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.cp.CpRecOrd;
import com.zebone.nhis.common.module.cn.cp.CpRecPhase;
import com.zebone.nhis.common.module.cn.cp.CpRecReason;
import com.zebone.nhis.common.module.cn.cp.CpTempCpord;
import com.zebone.nhis.common.module.cn.ipdw.BdCpReason;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdAnti;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdAntiApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CpRec2Service {
	@Autowired
	private CpRec2Mapper cpRecDao;
	@Autowired
	private CnPubService cnPubService;
	@Autowired
	private CnOpService cnOpService;
	@Autowired
	private CnIpPresService cnIpPresService;
	
	private static SimpleDateFormat dateformat =  new SimpleDateFormat("yyyyMMddHHmmss");
	
	//?????????????????????????????????????????????
	public List<Map<String,Object>> qryCpTempsByICD(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = cpRecDao.qryCpTempsByICD(paramMap);
		return list;
	}
	
	//???????????????????????????
	public List<Map<String,Object>> qryBdCpReason(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String sqlStr="select * from bd_cp_reason rs where rs.eu_reason<'9' and rs.del_flag='0'";
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sqlStr, paramMap);	
		return list;
	}
	
	//?????????????????????????????????
	public Map<String,Object> qryFireCpRec(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String sqlStr="select rec.*,temp.name_cp,temp.days_min,temp.days_max,temp.amount_ref from cp_rec rec inner join cp_temp temp on REC.pk_cptemp=temp.pk_cptemp  where rec.pk_pv= :pkPv  and rec.eu_rectype='1' ";//and rec.eu_status='1'
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sqlStr, paramMap);	
		if(list!=null && list.size()>0) {
			Map<String, Object> rtnMap = list.get(0);
			rtnMap.put("amt","0");
			sqlStr = "select sum(cg.amount) amt from pv_encounter pv " +
					"inner join bl_ip_dt cg on pv.pk_pv=cg.pk_pv " +
					" where pv.pk_pv=:pkPv and cg.flag_settle='0'";
			List<Map<String, Object>> amtList = DataBaseHelper.queryForList(sqlStr, paramMap);
			if (amtList != null && amtList.size() > 0) {
				if (StringUtils.isNotEmpty(MapUtils.getString(amtList.get(0), "amt"))) {
					rtnMap.put("amt", MapUtils.getString(amtList.get(0), "amt"));
				}
			}
			return rtnMap;
		}
		else
			return null;
	}
	
	//????????????????????????????????????????????????????????????????????????????????????
	public Map<String,Object> qryCpInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		
		Map<String,Object> cpInfoMap=new HashMap<String,Object>();
		//????????????
		List<Map<String,Object>>  tempPhaseList=qryCpTempPhase(param , user);
		//????????????
		List<Map<String,Object>>  recPhaseList=qryCpRecPhase(param , user);
		//????????????
		List<Map<String,Object>>  cpOrdList=qryCpOrd(param , user);
		//????????????
		List<Map<String,Object>>  cpExpList=qryCpExp(param , user);
		//????????????
		List<CnOpApply> cnOpList=cnOpService.getOpApplyList(param, user);
		//??????
		List<CnIpPressVO> cnPresList=cnIpPresService.qryCnIpPresAndOrds(param, user);
		
		Map<String,Object>  rtnMap=new HashMap<String,Object> ();
		Map<String, Object> maxGroupNoMap = DataBaseHelper.queryForMap("select nvl(max(groupno),0)+1 groupno from cn_order cn where cn.pk_pv= :pkPv and eu_always='0' ", paramMap);
		rtnMap.put("maxlgno", maxGroupNoMap.get("groupno").toString());
		maxGroupNoMap = DataBaseHelper.queryForMap("select nvl(max(groupno),0)+1 groupno from cn_order cn where cn.pk_pv= :pkPv and code_ordtype like '02%' ", paramMap);
		rtnMap.put("maxrisgno", maxGroupNoMap.get("groupno").toString());
		
		cpInfoMap.put("rtnMap", rtnMap);
		cpInfoMap.put("tempPhaseList", tempPhaseList);
		cpInfoMap.put("recPhaseList", recPhaseList);
		cpInfoMap.put("cpOrdList", cpOrdList);
		cpInfoMap.put("cpExpList", cpExpList);
		cpInfoMap.put("cnOpList", cnOpList);
		cpInfoMap.put("cnPresList", cnPresList);
		
		return cpInfoMap;
	}
	
	//???????????????????????????
	public List<Map<String,Object>> qryCpTempPhase(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = cpRecDao.qryCpTempPhase(paramMap);
		return list;
	}
	
	//????????????????????????
	public List<Map<String,Object>> qryCpRecPhase(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String sqlStr="select * from cp_rec_phase rs where rs.pk_cprec = :pkCprec and rs.del_flag='0'";
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sqlStr, paramMap);	
		return list;
	}
	
	//??????????????????
	public List<Map<String,Object>> qryCpOrd(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = cpRecDao.qryCpOrd(paramMap);
		return list;
	}
	
	//????????????????????????
	public List<Map<String,Object>> qryCpExp(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = cpRecDao.qryCpExp(paramMap);
		return list;
	}
	
	//??????????????????
	public List<Map<String,Object>> qryCpTaskDt(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String sqlStr="select * from bd_cp_task_dt dt where dt.pk_cptask = :pkCptask and dt.del_flag='0'";
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sqlStr, paramMap);	
		return list;
	}
	
	//??????/????????????
	public void applyorRejectCpTemp(String param , IUser user){
		User userInfo = (User)user;
		Date dateNow = new Date();
		CpIntegrateParam integrateParam = JsonUtil.readValue(param,CpIntegrateParam.class);
		CpRec cpRecVo=integrateParam.getCpRec();
		List<BdCpReason> cpReasonList = integrateParam.getCpReasonList();
		
		Map<String,Object>  unListParamMap=integrateParam.getUnListParamMap();
		String euStatus=cpRecVo.getEuStatus();//0 ?????????1 ?????????8 ?????????9 ??????
		String recReasonNote=null;
		if(unListParamMap.containsKey("recReasonNote")){
			recReasonNote=unListParamMap.get("recReasonNote").toString();
		}
		
		//??????cp_rec
		if(StringUtils.isBlank(cpRecVo.getPkCprec())){
			cpRecVo.setTs(dateNow);
			cpRecVo.setCreateTime(dateNow);
			cpRecVo.setCreator(userInfo.getPkEmp());
			cpRecVo.setDateStart(dateNow);
			DataBaseHelper.insertBean(cpRecVo);
			
			if("1".equals(euStatus)){
				DataBaseHelper.update("update pv_encounter set eu_pvmode='1' where pk_pv=? ",new Object[]{cpRecVo.getPkPv()});
			}
		}
		
		//??????cp_rec_reason
		List<CpRecReason> addCpRecReaList = new ArrayList<CpRecReason>();
		if(cpReasonList!=null && cpReasonList.size()>0){
			
			for(BdCpReason bdCpReasonVo: cpReasonList ){
				CpRecReason cpRecReason=new CpRecReason();
				cpRecReason.setPkRecreason(NHISUUID.getKeyId());
				cpRecReason.setPkOrg(cpRecVo.getPkOrg());
				cpRecReason.setPkCprec(cpRecVo.getPkCprec());
				cpRecReason.setPkCpreason(bdCpReasonVo.getPkCpreason());
				cpRecReason.setNameReason(bdCpReasonVo.getNameReason());
				cpRecReason.setDateRec(dateNow);
				cpRecReason.setNote(recReasonNote);
				cpRecReason.setDelFlag("0");
				cpRecReason.setTs(dateNow);
				cpRecReason.setCreateTime(dateNow);
				cpRecReason.setCreator(userInfo.getPkEmp());
				
				addCpRecReaList.add(cpRecReason);
			}
		}
		
		if (addCpRecReaList != null && addCpRecReaList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecReason.class), addCpRecReaList);
		}
		
	}
	
	//???????????????
	public void startNewPhase(String param , IUser user){
		User userInfo = (User)user;
		Date dateNow = new Date();
		CpIntegrateParam integrateParam = JsonUtil.readValue(param,CpIntegrateParam.class);
		CpRec cpRecVo=integrateParam.getCpRec();
		List<CpRecPhase> cpRecPhaseList = integrateParam.getCpRecPhaseList();
		List<CpRecExp>  cpRecExpList =   integrateParam.getCpRecExpList();
		String newPkRecphase="";
		
		//??????cp_rec_phase
		if(cpRecPhaseList!=null && cpRecPhaseList.size()>0){
			for(CpRecPhase cpRecPhaseVo: cpRecPhaseList ){
				cpRecPhaseVo.setDelFlag("0");
				if(StringUtils.isBlank(cpRecPhaseVo.getPkRecphase())){
					DataBaseHelper.insertBean(cpRecPhaseVo);
					newPkRecphase=cpRecPhaseVo.getPkRecphase();
				}
				else{
					DataBaseHelper.updateBeanByPk(cpRecPhaseVo,false);
				}
			}
		}
		
		//??????cp_rec_dt
		String sqlStr="select * from cp_temp_cpord cpord where cpord.pk_cpphase = ?  ";//and cpord.eu_tasktype<2
		List<CpTempCpord> tempOrdList = DataBaseHelper.queryForList(sqlStr,CpTempCpord.class, new Object[]{cpRecVo.getPkCpphase()});	
		List<CpRecDt> addCpRecDtList = new ArrayList<CpRecDt>();
		if(tempOrdList!=null && tempOrdList.size()>0){
			for(CpTempCpord cpTempOrdVo: tempOrdList ){
				CpRecDt cpRecDtVo=new CpRecDt();
				cpRecDtVo.setPkRecdt(NHISUUID.getKeyId());
				cpRecDtVo.setPkOrg(cpRecVo.getPkOrg());
				cpRecDtVo.setPkRecphase(newPkRecphase);
				cpRecDtVo.setPkCpord(cpTempOrdVo.getPkCpord());
				cpRecDtVo.setEuCpordtype(cpTempOrdVo.getEuTasktype());
				cpRecDtVo.setEuStatus("0");//0 ????????????1????????????2??????
				cpRecDtVo.setTs(dateNow);
				cpRecDtVo.setCreateTime(dateNow);
				cpRecDtVo.setCreator(userInfo.getPkEmp());
				
				addCpRecDtList.add(cpRecDtVo);
				
			}
		}
		
		if (addCpRecDtList != null && addCpRecDtList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecDt.class), addCpRecDtList);
		}
		
		//??????cp_rec
		DataBaseHelper.updateBeanByPk(cpRecVo,false);
		
		//??????cp_rec_exp
		saveCpExp(cpRecExpList,dateNow,userInfo);
	}
	
	//??????????????????
	public void saveCpExp(List<CpRecExp> cpRecExpList,Date dateNow,User userInfo ){
		if(cpRecExpList==null) return;
		for(CpRecExp recExp : cpRecExpList ){
			if(recExp!=null){
				if(StringUtils.isBlank(recExp.getPkRecexp())){
					recExp.setTs(dateNow);
					recExp.setCreateTime(dateNow);
					recExp.setCreator(userInfo.getPkEmp());
					recExp.setDelFlag("0");
					DataBaseHelper.insertBean(recExp);
				}
				else {
					recExp.setTs(dateNow);
					DataBaseHelper.updateBeanByPk(recExp,false);
				}
			}
		}
	}
	
	//??????????????????
	public void signCpOrd(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		User userInfo = (User)user;
		Date dateNow = new Date();
		String sqlStr="";
		List<CnOrder> updCnOrdList = new ArrayList<CnOrder>();
		List<CnRisApply> addCnRisList = new ArrayList<CnRisApply>();
		List<CnLabApply> addCnLisList = new ArrayList<CnLabApply>();
		
		CpIntegrateParam integrateParam = JsonUtil.readValue(param,CpIntegrateParam.class);
		List<CpRecOrd> cpOrdList = integrateParam.getCpOrdList();
		
		if(cpOrdList != null && cpOrdList.size()>0)
		{
			//--for??????cpOrdList---begin------
			for(CpRecOrd cpOrdVo: cpOrdList ){
				CnOrder ord = new CnOrder();
				String codeOrdtype = cpOrdVo.getCodeOrdtype();
				BeanUtils.copyProperties(ord, cpOrdVo);
				
				//??????cn_order????????????---begin---
				if(StringUtils.isBlank(ord.getPkCnord()) || ord.getTs()==null){
					//???????????????????????????PkCnord????????????????????????
					if(StringUtils.isBlank(ord.getPkCnord()) ){
						String pkCnord = NHISUUID.getKeyId();
						ord.setPkCnord(pkCnord);
					}
					
					ord.setTs(dateNow);
					ord.setFlagSign("1");
					ord.setDateSign(dateNow);
					ord.setCreateTime(dateNow);
					ord.setCreator(userInfo.getPkEmp());
					
					if(codeOrdtype.startsWith(Constants.RIS_SRVTYPE)){
						//String codeApply =  ApplicationUtils.getCode("0402");
						//ord.setCodeApply(codeApply);
						CnRisApply cnRisApply = new CnRisApply();
						String pkOrdris = NHISUUID.getKeyId();
						cnRisApply.setPkOrdris(pkOrdris);
						cnRisApply.setPkCnord(ord.getPkCnord());
						cnRisApply.setPkOrg(ord.getPkOrg());
						cnRisApply.setEuStatus("0");//??????????????????0 ??????
						cnRisApply.setTicketno(0);//?????????
						cnRisApply.setFlagBed(Constants.FALSE);
						cnRisApply.setFlagPrint(Constants.FALSE);
						cnRisApply.setFlagPrint2(Constants.FALSE);
						cnRisApply.setTs(dateNow);
						cnRisApply.setDescBody(ord.getDescBody());
						cnRisApply.setDtRistype(ord.getDtRistype());
						addCnRisList.add(cnRisApply);
						
					}else if(codeOrdtype.startsWith(Constants.LIS_SRVTYPE)){
						String codeApply = ApplicationUtils.getCode("0401");
						ord.setCodeApply(codeApply);
						CnLabApply cnLabApply = new CnLabApply();
						String pkOrdlis = NHISUUID.getKeyId();
						cnLabApply.setPkOrdlis(pkOrdlis);
						cnLabApply.setPkOrg(ord.getPkOrg());
						cnLabApply.setPkCnord(ord.getPkCnord());
						cnLabApply.setEuStatus("0");//??????????????????0??????
						cnLabApply.setFlagPrt(Constants.FALSE);
						cnLabApply.setTs(dateNow); 
						cnLabApply.setDtColtype(ord.getDtColltype());
						cnLabApply.setDtSamptype(ord.getDtSamptype());
						cnLabApply.setDtTubetype(ord.getDtContype());
						addCnLisList.add(cnLabApply);
					}

					/***
					 * ????????????cn_order
					 * ??????????????????????????????
					 * ??????????????????????????????
					 */
					CnOpApply opApply=cpOrdVo.getCnOpApply();
					CnOpApply rtnOpApply=new CnOpApply();
					CnIpPressVO presVo=cpOrdVo.getCnIpPres();
					CnIpPressVO rtnPresVo=new CnIpPressVO();
					
					if(opApply!=null){
						opApply.setFlagCp("1");
						rtnOpApply=cnOpService.saveOpApply(opApply, userInfo,param);
						BeanUtils.copyProperties(ord, rtnOpApply.getCnOrder());
						List<String> pkCnordList = new ArrayList<String>();
						pkCnordList.add(rtnOpApply.getPkCnord());
						cnOpService.signOpApply(pkCnordList,userInfo,ord.getTs());
						
					}else if(presVo!=null){
						String presJsonStr=ApplicationUtils.objectToJson(presVo);
						rtnPresVo=cnIpPresService.signPres(presJsonStr, user);
						ord.setPkPres(rtnPresVo.getPkPres());
						ord.setPkCnord(null);
					}else{
						
						DataBaseHelper.insertBean(ord);
					}
					
					
					
			    //??????cn_order????????????---end---
				}else{
					String flagSign=ord.getFlagSign();
					if("0".equals(flagSign)){
						ord.setFlagSign("1");
						ord.setDateSign(dateNow);
						ord.setPkEmpOrd(userInfo.getPkEmp());
						ord.setNameEmpOrd(userInfo.getNameEmp());
						
						updCnOrdList.add(ord);
					}else{
					//???????????????cn_order????????????????????????????????????????????????????????????????????????????????????????????????cn_order?????????????????????cp_rec_dt??????
						
					}
					
				}
				
				//?????????????????????
				CnOrdAntiApply antiItemApply = cpOrdVo.getSpecAntiVo();
				if(antiItemApply!=null){
					antiItemApply.setPkCnord(ord.getPkCnord());
					antiItemApply.setTs(dateNow);
					if(StringUtils.isBlank(antiItemApply.getPkOrdantiapply())){
						DataBaseHelper.insertBean(antiItemApply);
					}else{
						DataBaseHelper.updateBeanByPk(antiItemApply);
					}
				}
				//?????????????????????
				CnOrdAnti cnOrdAnti = cpOrdVo.getCnOrdAnti();
				if(cnOrdAnti!=null){
					cnOrdAnti.setPkCnord(ord.getPkCnord());
					cnOrdAnti.setPkOrg(userInfo.getPkOrg());
					cnOrdAnti.setPkEmpEntry(userInfo.getPkEmp());
					cnOrdAnti.setNameEmpEntry(userInfo.getNameEmp());
					cnOrdAnti.setDateEntry(dateNow);
					cnOrdAnti.setTs(dateNow);
					
					if(StringUtils.isBlank(cnOrdAnti.getPkOrdanti())){
						DataBaseHelper.insertBean(cnOrdAnti);
					}else{
						DataBaseHelper.updateBeanByPk(cnOrdAnti);
					}
				}
				
				
				if(StringUtils.isBlank(cpOrdVo.getPkRecdt())){
					//????????????cp_rec_dt
					CpRecDt recDt=new CpRecDt();
					BeanUtils.copyProperties(recDt, cpOrdVo);
					/**?????????????????????????????????PkCpord?????????????????????????????????
						?????????????????????????????????????????????????????????????????????????????????????????????????????????
						??????????????????
					**/
					recDt.setPkCpord(recDt.getPkCpord().substring(0,32));
					recDt.setPkCnord(ord.getPkCnord());
					recDt.setPkPres(ord.getPkPres());
					recDt.setEuStatus("1");
					recDt.setDateEx(ord.getTs());
					recDt.setPkEmpEx(userInfo.getPkEmp());
					recDt.setNameEmpEx(userInfo.getNameEmp());
					recDt.setCreator(userInfo.getPkEmp());
					recDt.setCreateTime(ord.getTs());
					recDt.setDelFlag("0");
					recDt.setTs(ord.getTs());
					DataBaseHelper.insertBean(recDt);
					
				}else{
					//????????????????????????cp_rec_dt
					Map<String,Object> mapParam = new HashMap<String,Object>();
					mapParam.put("pkCnord", ord.getPkCnord());
					mapParam.put("pkPres", ord.getPkPres());
					mapParam.put("pkRecdt", cpOrdVo.getPkRecdt());
					mapParam.put("ts", ord.getTs());
					mapParam.put("dateEx", ord.getTs());
					mapParam.put("pkEmpEx", userInfo.getPkEmp());
					mapParam.put("nameEmpEx", userInfo.getNameEmp());
					DataBaseHelper.update("update cp_rec_dt set pk_cnord=:pkCnord,pk_pres=:pkPres,eu_status='1',ts=:ts,date_ex=:dateEx,pk_emp_ex=:pkEmpEx,name_emp_ex=:nameEmpEx where pk_recdt = :pkRecdt  ", mapParam);
				}
				
			}
			//--for??????cpOrdList---end------
		}
		
		//??????????????????cn_order
		if (updCnOrdList != null && updCnOrdList.size() > 0) {
			//cnPubService.vaildUpdateCnOrdts(updCnOrdList);
			//DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), updCnOrdList);
			
			cnPubService.vaildUpdateCnOrdts(updCnOrdList);
			for(CnOrder upCnOrder : updCnOrdList){
				upCnOrder.setTs(dateNow);
			}
			StringBuffer sbfSql = new StringBuffer();
			sbfSql.setLength(0);
			sbfSql.append("update cn_order set ts=:ts,ordsn_parent=:ordsnParent,first_num = :firstNum,eu_always =:euAlways,date_start = :dateStart,code_ord=:codeOrd,");
			sbfSql.append("name_ord = :nameOrd,dosage=:dosage,pk_unit_dos=:pkUnitDos,quan=:quan,code_supply=:codeSupply,code_freq=:codeFreq,drip_speed=:dripSpeed,");
			sbfSql.append("flag_first=:flagFirst,pk_dept_exec=:pkDeptExec,pk_org_exec=:pkOrgExec,note_supply=:noteSupply,note_ord=:noteOrd,flag_thera=:flagThera,");
			sbfSql.append("flag_prev=:flagPrev,flag_fit=:flagFit,flag_medout=:flagMedout,flag_self=:flagSelf,flag_stop=:flagStop, pk_emp_stop=:pkEmpStop, name_emp_stop=:nameEmpStop,");
			sbfSql.append("date_stop=:dateStop,last_num=:lastNum,flag_durg=:flagDurg,flag_bl=:flagBl,code_ordtype=:codeOrdtype,spec=:spec,pk_unit=:pkUnit,flag_base=:flagBase,");
			sbfSql.append("quan_cg=:quanCg,dt_usagenote=:dtUsagenote,eu_st=:euSt,desc_ord=:descOrd,flag_pivas=:flagPivas,price_cg=:priceCg,days=:days,ords=:ords,dt_herbusage=:dtHerbusage,");
			sbfSql.append("FLAG_SIGN=:flagSign,DATE_SIGN=:dateSign,PK_EMP_ORD=:pkEmpOrd,NAME_EMP_ORD=:nameEmpOrd  where pk_cnord = :pkCnord");
			DataBaseHelper.batchUpdate(sbfSql.toString(), updCnOrdList);
			
		}
		//????????????????????????cn_ris_apply
		if(addCnRisList!=null && addCnRisList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), addCnRisList);
		}
		//????????????????????????cn_lab_apply
		if(addCnLisList!=null && addCnLisList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), addCnLisList);
		}
		
	}
	
	//??????????????????
	public void abandonCpOrd(String param , IUser user) {
		User userInfo = (User)user;
		Date dateNow = new Date();
		CpIntegrateParam integrateParam = JsonUtil.readValue(param,CpIntegrateParam.class);
		List<CpRecOrd> cpOrdList = integrateParam.getCpOrdList();
		
		if(cpOrdList!=null && cpOrdList.size()>0){
			for(CpRecOrd cpOrdVo: cpOrdList ){
				DataBaseHelper.update("update cp_rec_dt set eu_status='2',note = ? where pk_recdt = ? ",new Object[]{cpOrdVo.getNote(), cpOrdVo.getPkRecdt()} );
				CpRecExp recExp=cpOrdVo.getCpRecExp();
				
				if(recExp!=null){
					if(StringUtils.isBlank(recExp.getPkRecexp())){
						recExp.setTs(dateNow);
						recExp.setCreateTime(dateNow);
						recExp.setCreator(userInfo.getPkEmp());
						recExp.setDelFlag("0");
						DataBaseHelper.insertBean(recExp);
					}
					else DataBaseHelper.updateBeanByPk(recExp,false);
				}
			}
		}
	}
	
	//??????????????????
	public void signCpTreat(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		User userInfo = (User)user;
		Date dateNow = new Date();
		
		CpIntegrateParam integrateParam = JsonUtil.readValue(param,CpIntegrateParam.class);
		List<CpRecOrd> cpOrdList = integrateParam.getCpOrdList();
		
		if(cpOrdList != null && cpOrdList.size()>0)
		{
			for(CpRecOrd cpOrdVo: cpOrdList ){
				cpOrdVo.setEuStatus("1");
				cpOrdVo.setTs(dateNow);
				cpOrdVo.setDateEx(dateNow);
				cpOrdVo.setPkEmpEx(userInfo.getPkEmp());
				cpOrdVo.setNameEmpEx(userInfo.getNameEmp());
			}
			String sqlStr="update cp_rec_dt set pk_cnord=:pkCnord,eu_status=:euStatus,ts=:ts,date_ex=:dateEx,pk_emp_ex=:pkEmpEx,name_emp_ex=:nameEmpEx,note=:note where pk_recdt = :pkRecdt  ";
			DataBaseHelper.batchUpdate(sqlStr, cpOrdList);
		}
	}
	
	
	//??????
	public void quitCp(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		User userInfo = (User)user;
		Date dateNow = new Date();
		String sqlStr="";
		
		CpIntegrateParam integrateParam = JsonUtil.readValue(param,CpIntegrateParam.class);
		CpRec cpRecVo=integrateParam.getCpRec();
		List<CpRecPhase>  cpRecPhaseList= integrateParam.getCpRecPhaseList();
		List<CpRecExp>  cpRecExpList=integrateParam.getCpRecExpList();
		
		
		//?????????????????????cp_rec_phase
		if(cpRecPhaseList != null && cpRecPhaseList.size()>0)
		{
			for(CpRecPhase recPhase: cpRecPhaseList ){
				recPhase.setEuStatus("9");
				recPhase.setTs(dateNow);
				recPhase.setDateExit(dateNow);
				recPhase.setPkEmpExit(userInfo.getPkEmp());
				recPhase.setNameEmpExit(userInfo.getNameEmp());
			}
			sqlStr="update cp_rec_phase set eu_status=:euStatus,date_exit=:dateExit,ts=:ts,pk_emp_exit=:pkEmpExit,name_emp_exit=:nameEmpExit where pk_recphase = :pkRecphase  ";
			DataBaseHelper.batchUpdate(sqlStr, cpRecPhaseList);
		}
		
		//??????cp_rec_exp
		saveCpExp(cpRecExpList,dateNow,userInfo);
		
		//??????????????????cp_rec
		cpRecVo.setEuStatus("9");
		cpRecVo.setDateExit(dateNow);
		cpRecVo.setTs(dateNow);
		cpRecVo.setPkEmpExit(userInfo.getPkEmp());
		cpRecVo.setNameEmpExit(userInfo.getNameEmp());
		
		List<CpRec> tempRecList=new ArrayList<CpRec>();
		tempRecList.add(cpRecVo);
		sqlStr="update cp_rec set eu_status=:euStatus,date_exit=:dateExit,ts=:ts,pk_emp_exit=:pkEmpExit,name_emp_exit=:nameEmpExit where pk_cprec = :pkCprec  ";
		DataBaseHelper.batchUpdate(sqlStr, tempRecList);
		
		//??????pv_encounter
		DataBaseHelper.update("update pv_encounter set eu_pvmode='0' where pk_pv=? ",new Object[]{cpRecVo.getPkPv()});
		
	}
	
	//??????
	public void finishCp(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		User userInfo = (User)user;
		Date dateNow = new Date();
		String sqlStr="";
		
		CpIntegrateParam integrateParam = JsonUtil.readValue(param,CpIntegrateParam.class);
		CpRec cpRecVo=integrateParam.getCpRec();
		List<CpRecPhase>  cpRecPhaseList= integrateParam.getCpRecPhaseList();
		List<CpRecExp>  cpRecExpList =   integrateParam.getCpRecExpList();
		
		//?????????????????????cp_rec_phase
		if(cpRecPhaseList != null && cpRecPhaseList.size()>0)
		{
			for(CpRecPhase recPhase: cpRecPhaseList ){
				recPhase.setEuStatus("1");
				recPhase.setTs(dateNow);
				recPhase.setDateEnd(dateNow);
				recPhase.setPkEmpEnd(userInfo.getPkEmp());
				recPhase.setNameEmpEnd(userInfo.getNameEmp());
			}
			sqlStr="update cp_rec_phase set eu_status=:euStatus,date_end=:dateEnd,ts=:ts,pk_emp_end=:pkEmpEnd,name_emp_end=:nameEmpEnd where pk_recphase = :pkRecphase  ";
			DataBaseHelper.batchUpdate(sqlStr, cpRecPhaseList);
		}
		
		
		//??????????????????cp_rec
		cpRecVo.setEuStatus("8");
		cpRecVo.setDateEnd(dateNow);
		cpRecVo.setTs(dateNow);
		cpRecVo.setPkEmpEnd(userInfo.getPkEmp());
		cpRecVo.setNameEmpEnd(userInfo.getNameEmp());
		
		List<CpRec> tempRecList=new ArrayList<CpRec>();
		tempRecList.add(cpRecVo);
		sqlStr="update cp_rec set eu_status=:euStatus,date_end=:dateEnd,ts=:ts,pk_emp_end=:pkEmpEnd,name_emp_end=:nameEmpEnd where pk_cprec = :pkCprec  ";
		DataBaseHelper.batchUpdate(sqlStr, tempRecList);
		
		//??????cp_rec_exp
		saveCpExp(cpRecExpList,dateNow,userInfo);
	}
	
	//??????????????????
	public Map<String,Object> qryCpFormItem(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		
		//?????????????????????
		List<Map<String,Object>> cpTempFormPhaseList = cpRecDao.qryCpFormItem(paramMap);
		//????????????
		List<Map<String,Object>> cpTempDiagList = qryCpAdaptiveICDs(param,user);
		
		Map<String,Object> cpFormMap=new HashMap<String,Object>();
		cpFormMap.put("cpTempFormPhaseList", cpTempFormPhaseList);
		cpFormMap.put("cpTempDiagList", cpTempDiagList);
		
		return cpFormMap;
	}
	
	//????????????????????????
	public List<Map<String,Object>> qryCpAdaptiveICDs(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String sqlStr="select * from cp_temp_diag diag where diag.pk_cptemp = :pkCptemp and diag.del_flag='0'";
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sqlStr, paramMap);	
		return list;
	}
	
}
