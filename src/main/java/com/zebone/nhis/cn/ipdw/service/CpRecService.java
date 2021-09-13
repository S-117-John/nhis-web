package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnOrderMapper;
import com.zebone.nhis.cn.ipdw.dao.CpRecMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CnOpApplyVo;
import com.zebone.nhis.cn.ipdw.vo.ExitCpRecParam;
import com.zebone.nhis.cn.ipdw.vo.RecPhaseOrdParam;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.cp.CpRec;
import com.zebone.nhis.common.module.cn.cp.CpRecDt;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.cp.CpRecExpDt;
import com.zebone.nhis.common.module.cn.cp.CpRecPhase;
import com.zebone.nhis.common.module.cn.cp.CpRecReason;
import com.zebone.nhis.common.module.cn.cp.CpTempOrd;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CpRecService {
    @Autowired
	private CpRecMapper cpRecDao;
	@Autowired
	private BdSnService bdSnService;
	@Autowired
	private CnOrderMapper CnOrderDao ;
	@Autowired
	private CnPubService cnPubService ;
	
    public List<Map<String,Object>> qryCpRec(String param, IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pk_pv = (String)map.get("pkPv");
		if(StringUtils.isEmpty(pk_pv)) return new ArrayList<Map<String,Object>>();
		return cpRecDao.qryCpRec(pk_pv);
    }
    public List<Map<String,Object>> qryCpRecReason(String param, IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	String qryType = (String)map.get("qryType");
    	if("qrytemp".equals(qryType)) {
    		String pkCptemp = (String) map.get("pkCptemp");
    		String tempType = (String) map.get("tempType");
    		if(StringUtils.isEmpty(pkCptemp)) throw new BusException("请选择路径模板!");
    		return cpRecDao.qryCpTempReason(pkCptemp,tempType);
    	}else if("qrycp".equals(qryType)){
    		String pkCprec = (String) map.get("pkCprec");
    		if(StringUtils.isEmpty(pkCprec)) throw new BusException("请选择要查看的路径!");
    		return cpRecDao.qryCpRecReason(pkCprec);
    	}
    	return new ArrayList<Map<String,Object>>();
    }
    public void saveCpRec(String param,IUser user){
    	ExitCpRecParam exitCpRecParam = JsonUtil.readValue(param, ExitCpRecParam.class);
    	if("1".equals(exitCpRecParam.getIsTurnToCp())){ //转路径
        	//第一步，退出当前路径
        	exitCpRecPub(user, exitCpRecParam, null);
        	//第二步，启用新路径
        	String execCprec = saveCpRecPub(user, exitCpRecParam); //待启用路径阶段
        	//第三步，启用新阶段
        	Map<String,Object> map = new HashMap<String,Object>();
        	map.put("execCprec", execCprec);
        	map.put("execCpphase", "");
        	map.put("pkCpphase", exitCpRecParam.getPkCpphase());
        	map.put("expType", exitCpRecParam.getExpType());
        	map.put("pkCpexp", exitCpRecParam.getPhasePkCpexp());
        	map.put("expNote", exitCpRecParam.getPhaseExpNote());
        	startRecPhase(user, map);
    	}else{
    		saveCpRecPub(user, exitCpRecParam);
    	}
    }
	private String saveCpRecPub(IUser user, ExitCpRecParam exitCpRecParam) {
		String pk_pv = exitCpRecParam.getPkPv();
		if(StringUtils.isEmpty(pk_pv))throw new BusException("请选择患者!");
		String pk_cptemp = exitCpRecParam.getPkCptemp();
		if(StringUtils.isEmpty(pk_cptemp))throw new BusException("请选择路径模板!");
		String pk_diag =exitCpRecParam.getPkDiag();
		if(StringUtils.isEmpty(pk_diag))throw new BusException("请选择诊断!"); 
		List<CpRecReason> reason = exitCpRecParam.getCpRecReason();
		if(reason==null ||reason.size()==0)throw new BusException("请选择路径启用原因!"); 
		String euReason =exitCpRecParam.getEuReason();
		if(StringUtils.isEmpty(euReason))throw new BusException("请维护路径模板原因类型!"); 
		int countCpTemp = DataBaseHelper.queryForScalar("select  count(pk_cptemp) from cp_rec  where pk_pv=? and del_flag='0' and eu_status='0' and pk_cptemp=? ", Integer.class, pk_pv,pk_cptemp);
		if(countCpTemp>0) throw new BusException("该路径已启用!");
		String eu_rec_type = exitCpRecParam.getEuRecType();
		User u = (User)user;
		CpRec cpRec = new CpRec();
		int count = DataBaseHelper.queryForScalar("select  count(pk_cptemp) from cp_rec  where pk_pv=? and del_flag='0' and eu_status='0' and eu_rectype='1' ", Integer.class, pk_pv);
		if(count>0){
			if(!"true".equals(eu_rec_type)){
				throw new BusException("已存在主路径，若要启用并发路径，请勾选并发路径再操作!");
			}else{
//				cpRec.setEuRecType("2");
			}
		}else{
//			cpRec.setEuRecType("1");
		}

		cpRec.setPkCprec(NHISUUID.getKeyId());
		cpRec.setPkPv(pk_pv);
		cpRec.setPkCptemp(pk_cptemp);
//		cpRec.setPkDiag(pk_diag);
		cpRec.setPkOrg(u.getPkOrg());
		cpRec.setEuStatus("1".equals(euReason)?"0":"2"); //0为启用，2为拒绝
		cpRec.setDateStart(new Date());
		cpRec.setPkEmpStart(u.getPkEmp());
		cpRec.setNameEmpStart(u.getNameEmp());
		cpRec.setFlagTransfer("0");
		//保存启用原因
		for(CpRecReason recReason :reason){
			recReason.setPkRecreason(NHISUUID.getKeyId());
			recReason.setPkCprec(cpRec.getPkCprec());
			recReason.setDateRec(new Date());
			recReason.setPkOrg(u.getPkOrg());
			recReason.setDelFlag("0");
			recReason.setTs(new Date());
		}
		DataBaseHelper.insertBean(cpRec);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecReason.class), reason);
		return cpRec.getPkCprec();
	}
    public  String finCpRec(String param,IUser user){	
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	String finStep = (String) map.get("finStep");
		String pk_cprec = (String) map.get("pkCprec");
		if(StringUtils.isEmpty(pk_cprec)) throw new BusException("请选择要完成的路径!");
		if(StringUtils.isEmpty(finStep)) throw new BusException("前台传入的finStep为空");
		String hasNoStop="";
		String pk_cptemp = (String) map.get("pkCptemp");
		if(StringUtils.isEmpty(pk_cptemp))throw new BusException("选择待完成的路径中对应的路径模板主键为空!");
		//查询启用路径的最后一个阶段是否启用
		Map<String,String> cLastPhase = cpRecDao.qryTempLastPhase(pk_cprec,pk_cptemp);
		//查询有未停止的长期医嘱，提示停用
		List<Map<String,Object>> recUnStopOrd = cpRecDao.qryRecUnStopOrd("allPhase", "",pk_cprec);
		if("qry".equals(finStep)){
			if(cLastPhase==null)  throw new BusException("该路径最后一个阶段未启用，不可以点完成！"); 
			//查询有必选项目未做的医嘱，提示是否变异
			String exec_cpphase = cLastPhase.get("pkCpphase");
			int necOrd = cpRecDao.qryTempNecOrd(exec_cpphase,pk_cprec);
			if(necOrd>0)throw new BusException("最后一个阶段有必选项目未做,请使用或弃用!"); 
			getTempNsNecWork(pk_cprec, exec_cpphase); 
			if(recUnStopOrd.size()>0) hasNoStop="该路径的阶段有未停止的长期医嘱,是否停止到当前时间?";
			else{
				String pk_cpphase = (String) cLastPhase.get("pkCpphase");
				finRec(user, pk_cprec, pk_cpphase); 
			} 
			
		}else{
			String pk_cpphase = (String) cLastPhase.get("pkCpphase");
			User u = finRec(user, pk_cprec, pk_cpphase);  			
			if("stop".equals(finStep)){
				stopOrdByListMap(u, recUnStopOrd);	
			}
		}
		return hasNoStop;
    }
    /**
     * 路径模板阶段护士必选项目
     * @param pk_cprec
     * @param exec_cpphase
     */
    
	private void getTempNsNecWork(String pk_cprec, String exec_cpphase) {
		List<Map<String,Object>> nsNecOrd = cpRecDao.qryTempNsNecOrd(exec_cpphase, pk_cprec);
		if(nsNecOrd!=null&&nsNecOrd.size()>0){
			String expStr="";
			for(Map<String,Object> omap :nsNecOrd){
				expStr+="["+omap.get("nameAct").toString()+"]";
			}
			throw new BusException("请通知护士:路径模板["+nsNecOrd.get(0).get("nameCp").toString()+"]的阶段["+nsNecOrd.get(0).get("namePhase")+"]中"+expStr+"是必选项目,请护士确认使用或者弃用!");
		}
	}
	private User finRec(IUser user, String pk_cprec, String pk_cpphase) {
		//更改路径、阶段完成状态
		User u = (User) user;
		CpRec cpRec = new CpRec();
		cpRec.setPkCprec(pk_cprec);
		cpRec.setEuStatus("1");
		cpRec.setPkEmpEnd(u.getPkEmp());
		cpRec.setNameEmpEnd(u.getNameEmp());
		cpRec.setDateEnd(new Date());
		cpRec.setTs(new Date());
		List<CpRec> cpRecList = new ArrayList<CpRec>();
		cpRecList.add(cpRec);
		DataBaseHelper.batchUpdate("update cp_rec set pk_cprec=:pkCprec,eu_status=:euStatus,date_end=:dateEnd,pk_emp_end=:pkEmpEnd,name_emp_end=:nameEmpEnd,ts=:ts  where pk_cprec=:pkCprec", cpRecList);
		CpRecPhase cpRecPhase = new CpRecPhase();
		List<CpRecPhase> cpRecPhaseList = new ArrayList<CpRecPhase>();
		cpRecPhase.setEuStatus("1");
		cpRecPhase.setPkEmpEnd(u.getPkEmp());
		cpRecPhase.setNameEmpEnd(u.getNameEmp());
		cpRecPhase.setDateEnd(new Date());
		cpRecPhase.setPkCprec(pk_cprec);
		cpRecPhase.setPkCpphase(pk_cpphase);
		cpRecPhase.setTs(new Date());
		cpRecPhaseList.add(cpRecPhase);
		DataBaseHelper.batchUpdate("update cp_rec_phase set eu_status=:euStatus,date_end=:dateEnd,pk_emp_end=:pkEmpEnd,name_emp_end=:nameEmpEnd,ts=:ts where eu_status='0' and pk_cprec=:pkCprec and pk_cpphase=:pkCpphase ", cpRecPhaseList);
		return u;
	}
    public void exitCpRec(String param,IUser user){
    	ExitCpRecParam exitCpRecParam = JsonUtil.readValue(param, ExitCpRecParam.class);
    	List<CpRecReason> cpRecReason = exitCpRecParam.getCpRecReason();
    	if(cpRecReason==null || cpRecReason.size()==0)  throw new BusException("请填写路径退出原因!");
    	exitCpRecPub(user, exitCpRecParam, cpRecReason);
    }
	private void exitCpRecPub(IUser user, ExitCpRecParam exitCpRecParam,
			List<CpRecReason> cpRecReason) {
		String pkCprec= exitCpRecParam.getPkCprec();
    	if(StringUtils.isEmpty(pkCprec)) throw new BusException("请选择要退出的路径!");
    	String pkCpexp = exitCpRecParam.getPkCpexp();
    	if(StringUtils.isEmpty(pkCpexp)) throw new BusException("请选择变异项目!");
    	User u = (User) user;
    	CpRec cpRec = new CpRec();
    	cpRec.setPkCprec(pkCprec);
    	cpRec.setEuStatus("9");
    	cpRec.setPkEmpExit(u.getPkEmp());
    	cpRec.setNameEmpExit(u.getNameEmp());
    	cpRec.setDateExit(new Date());
    	cpRec.setTs(cpRec.getDateExit());
    	cpRec.setDelFlag("0");
    	DataBaseHelper.updateBeanByPk(cpRec,false);
    	if(cpRecReason!=null){
    		for(CpRecReason recReason:cpRecReason){
    			recReason.setPkRecreason(NHISUUID.getKeyId());
    			recReason.setPkOrg(u.getPkOrg());
    			recReason.setPkCprec(pkCprec);
    			recReason.setDateRec(new Date());
    			recReason.setDelFlag("0");
    			recReason.setTs(new Date());
    		}
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecReason.class), cpRecReason);
    	}
    	CpRecExp cpRecExp = new CpRecExp();
    	cpRecExp.setPkRecexp(NHISUUID.getKeyId());
    	cpRecExp.setPkCprec(pkCprec);
    	cpRecExp.setDtCpexptype("01");//退出路径
    	cpRecExp.setDateExp(new Date());
    	cpRecExp.setPkEmpExp(u.getPkEmp());
    	cpRecExp.setPkOrg(u.getPkOrg());
     	cpRecExp.setPkCpexp(pkCpexp); 
     	cpRecExp.setNote(exitCpRecParam.getExpNote());
    	CpRecExpDt cpRecExpDt = new CpRecExpDt();
    	cpRecExpDt.setPkRecexp(cpRecExp.getPkRecexp());
    	cpRecExpDt.setEuType("0");
    	cpRecExpDt.setPkOrg(u.getPkOrg());
    	DataBaseHelper.insertBean(cpRecExp);
    	DataBaseHelper.insertBean(cpRecExpDt);
  	  //路径退出，已启用阶段医嘱存在必做的医嘱/工作直接弃用
    	List<Map<String,Object>> necOrdList = cpRecDao.qryUnUseNecOrdByRec(pkCprec);
    	giveUpUnUseNec(user, pkCprec, pkCpexp,"0", necOrdList);
    	List<Map<String,Object>> necWorkList = cpRecDao.qryUnUseNecWorkByRec(pkCprec);
    	giveUpUnUseNec(user, pkCprec, pkCpexp,"1", necWorkList);
     //长期医嘱停在当前
    	List<Map<String,Object>> recUnStopOrd = cpRecDao.qryRecUnStopOrd("allPhase", "",pkCprec);
		stopOrdByListMap(u, recUnStopOrd);	
      //路径退出，已启用的阶段都得是退出状态
    	DataBaseHelper.update("update cp_rec_phase set eu_status='9',pk_emp_exit=?,name_emp_exit=?,date_exit=?,ts=? where pk_cprec=? and eu_status='0' ", u.getPkEmp(),u.getNameEmp(),cpRec.getDateExit(),cpRec.getDateExit(),pkCprec);
	}
	private void stopOrdByListMap(User u, List<Map<String, Object>> recUnStopOrd) {
		List<CnOrder> stopOrds = new ArrayList<CnOrder>();
		for(Map<String,Object> ordMap : recUnStopOrd){
			CnOrder order = new CnOrder();
			order.setPkCnord((String)ordMap.get("pkCnord"));
			order.setFlagStop(Constants.TRUE);
			order.setPkEmpStop(u.getPkEmp());
			order.setNameEmpStop(u.getNameEmp());
			order.setDateStop(new Date());
			order.setTs(order.getDateStop());
			stopOrds.add(order);
		}
		DataBaseHelper.batchUpdate("update cn_order set flag_stop=:flagStop, pk_emp_stop=:pkEmpStop, name_emp_stop=:nameEmpStop,date_stop=:dateStop,ts=:ts  where pk_cnord=:pkCnord and  eu_status_ord not in ('4','9') ", stopOrds);
	}
	private void giveUpUnUseNec(IUser user, String pkCprec, String pkCpexp,String euType,
			List<Map<String, Object>> necOrdList) {
		if(necOrdList!=null && necOrdList.size()>0){
		    List<RecPhaseOrdParam> ordParamList = new ArrayList<RecPhaseOrdParam>();
	        for(Map<String,Object> temp : necOrdList){
	        	 RecPhaseOrdParam ordParam = new RecPhaseOrdParam();
	        	 String pkRecdt = (String) temp.get("pkRecdt");
	        	 String pkCpord = (String) temp.get("pkCpord");
	        	 String pkRecphase = (String) temp.get("pkRecphase");
	        	 if("0".equals(euType))ordParam.setPkCpord(pkCpord);
	        	 else ordParam.setPkTempwork(pkCpord);
	        	 ordParam.setPkRecphase(pkRecphase);
	        	 ordParam.setPkCprec(pkCprec);
	        	 ordParam.setPkCpexp(pkCpexp);
	        	 ordParam.setEuType(euType);
	        	 ordParam.setPkRecdt(pkRecdt);
	        	 ordParamList.add(ordParam);
	        }
	        giveUpRecPhaseOrd(user, ordParamList);
    	}
	}
    public String saveRecPhase(String param,IUser user){   	
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	String finStep = (String) map.get("finStep");
    	if(StringUtils.isEmpty(finStep)) throw new BusException("前台传入的finStep为空!");
    	String hasNoStop="";
    	String pkPv = (String)map.get("pkPv");
    	if(StringUtils.isEmpty(pkPv)) throw new BusException("请选择患者!");
		String pkCptemp = (String) map.get("pkCptemp");
		if(StringUtils.isEmpty(pkCptemp)) throw new BusException("请选择启用路径!");
		String pk_cpphase = (String) map.get("pkCpphase"); //待启用阶段
		if(StringUtils.isEmpty(pk_cpphase)) throw new BusException("请选择要启用的阶段!");
		Map<String,Object> phase = DataBaseHelper.queryForMap("select pk_cprec,pk_cpphase from cp_rec where del_flag='0' and eu_status='0' and pk_cptemp=? and pk_pv=? ", pkCptemp,pkPv);
		if(phase!=null){
			map.put("execCprec", (String)phase.get("pkCprec"));
			map.put("execCpphase", (String)phase.get("pkCpphase"));
		}
    	if("qry".equals(finStep)){
    		if(phase==null) throw new BusException("请先启用路径再启用阶段!");
    		String  exec_cpphase = (String) phase.get("pkCpphase"); //已启用阶段
    		if(StringUtils.isNotEmpty(exec_cpphase)){//有正在执行的阶段
    			if(pk_cpphase.equals(exec_cpphase)) throw new BusException("已启用的阶段不能再启用！");
    			String exec_cprec = (String)phase.get("pkCprec");
    	    	//查询已启用的模板阶段有必选项目未做的医嘱，提示使用或弃用
    	    	int necOrd = cpRecDao.qryTempNecOrd(exec_cpphase,exec_cprec);
    	    	if(necOrd>0) throw new BusException("已启用的阶段有必选项目未做,请使用或弃用!");
    	    	getTempNsNecWork(exec_cprec, exec_cpphase); 
    			//查询有未停止的长期医嘱，提示停用
    	    	List<Map<String,Object>>  recUnStopOrd = cpRecDao.qryRecUnStopOrd("phaseUse", exec_cpphase,exec_cprec);
    	    	if(recUnStopOrd.size()>0) hasNoStop="已启用的阶段有未停止的长期医嘱,是否启用新阶段?";
    	    	else startRecPhase(user, map);
    		}else { //启用的第一个阶段
    			startRecPhase(user, map);
    		}
    	}else{
    		startRecPhase(user, map);
    	}
      return hasNoStop;
 
    }
	private void startRecPhase(IUser user, Map<String, Object> map) {
		String  exec_cprec = (String) map.get("execCprec");
		String  exec_cpphase = (String) map.get("execCpphase"); //已启用阶段
		String pk_cpphase = (String) map.get("pkCpphase"); //待启用阶段
		User u = (User)user;
		//写入启用阶段记录
		CpRecPhase cpRecPhase = new CpRecPhase();
		cpRecPhase.setPkCprec(exec_cprec);
		cpRecPhase.setPkOrg(u.getPkOrg());
		cpRecPhase.setPkCpphase(pk_cpphase);
		cpRecPhase.setDateStart(new Date());
		cpRecPhase.setPkEmpStart(u.getPkEmp());
		cpRecPhase.setNameEmpStart(u.getNameEmp());
		cpRecPhase.setEuStatus("0");
		DataBaseHelper.insertBean(cpRecPhase);
        //写入路径应用明细
		List<CpRecDt> recDtList = new ArrayList<CpRecDt>();
		String pkRecphase = cpRecPhase.getPkRecphase();
		List<CpTempOrd> tempOrdList = DataBaseHelper.queryForList("select * from cp_temp_ord where pk_cpphase=? ", CpTempOrd.class, pk_cpphase);
		if(tempOrdList!=null && tempOrdList.size()>0){
			for(CpTempOrd temp : tempOrdList){
				CpRecDt cpRecDt = new CpRecDt();
				cpRecDt.setPkCpord(temp.getPkCpord());
//				cpRecDt.setEuType("0"); //医嘱
//				cpRecDt.setEuRole("0");//医生
				setRecDt(u, pkRecphase, cpRecDt);
				recDtList.add(cpRecDt);
			}		 
		}
		List<Map<String,Object>> workOrdList = DataBaseHelper.queryForList("select tempwork.pk_tempwork ,cpaction.eu_role  from cp_temp_work tempwork,bd_cp_action cpaction where tempwork.pk_cpaction = cpaction.pk_cpaction and tempwork.pk_cpphase=? ", pk_cpphase);
		if(workOrdList!=null && workOrdList.size()>0){
			for(Map<String,Object> temp : workOrdList){
				CpRecDt cpRecDt = new CpRecDt();
//				cpRecDt.setPkTempwork((String)temp.get("pkTempwork"));
//				cpRecDt.setEuType("1"); //医嘱
//				cpRecDt.setEuRole((String)temp.get("euRole")); //医生或护士
				setRecDt(u, pkRecphase, cpRecDt);
				recDtList.add(cpRecDt);
			}
		}
	    if(recDtList.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecDt.class), recDtList);
		//更新路径使用记录，回写当前阶段
		DataBaseHelper.update("update cp_rec set pk_cpphase=? where pk_cprec=? ", pk_cpphase,exec_cprec);
		//启用一个阶段成功后，上一个正使用的阶段是完成状态，完成状态但是也可能有长期医嘱还有效
		DataBaseHelper.update("update cp_rec_phase set eu_status='1' ,date_end=?,pk_emp_end=?,name_emp_end=?  where pk_cprec=?  and pk_cpphase=? ", cpRecPhase.getDateStart(),u.getPkEmp(),u.getNameEmp(),exec_cprec,exec_cpphase);
		//阶段发生变异		
		String expType = (String) map.get("expType");	
		if(StringUtils.isNotEmpty(expType)){			
			String pkCpexp = (String) map.get("pkCpexp");
			String expNote = (String) map.get("expNote");
			CpRecExp cpRecExp = new CpRecExp();
				cpRecExp.setDtCpexptype(expType);//退出路径
				cpRecExp.setPkOrg(u.getPkOrg());
				cpRecExp.setPkCpexp(pkCpexp);
				cpRecExp.setPkCprec(exec_cprec);
				cpRecExp.setDateExp(new Date());
				cpRecExp.setPkEmpExp(u.getPkEmp());
				cpRecExp.setPkRecexp(NHISUUID.getKeyId());
				cpRecExp.setNote(expNote);
			CpRecExpDt cpRecExpDt = new CpRecExpDt();
				cpRecExpDt.setPkRecexp(cpRecExp.getPkRecexp());
				cpRecExpDt.setEuType("1"); //阶段
				cpRecExpDt.setPkCpphase(pk_cpphase);
				cpRecExpDt.setPkOrg(u.getPkOrg());
				DataBaseHelper.insertBean(cpRecExp);
				DataBaseHelper.insertBean(cpRecExpDt);
		}
	}
	private void setRecDt(User u, String pkRecphase, CpRecDt cpRecDt) {
		cpRecDt.setPkRecdt(NHISUUID.getKeyId());
		cpRecDt.setPkOrg(u.getPkOrg());
		cpRecDt.setPkRecphase(pkRecphase);
		cpRecDt.setEuStatus("0"); //未使用
		cpRecDt.setTs(new Date());
		cpRecDt.setDelFlag(Constants.FALSE);
	}

    public Map<String,List<Map<String,Object>>> qryRecPhaseOrd(String param,IUser user){
    	Map<String,List<Map<String,Object>>> rtn = new HashMap<String,List<Map<String,Object>>>();
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	String pkCprec = (String)map.get("pkCprec");
		if(StringUtils.isEmpty(pkCprec)) return rtn; 
		String pkCpphase = (String)map.get("pkCpphase");
		if(StringUtils.isEmpty(pkCpphase)) return rtn;
		List<Map<String,Object>> recPhaseOrd = cpRecDao.qryRecPhaseOrd(pkCprec,pkCpphase,((User)user).getPkOrg());//阶段医嘱
		List<Map<String,Object>> recPhaseAction = cpRecDao.qryRecPhaseAction(pkCprec,pkCpphase);
		List<Map<String,Object>> recAllPhaseOrd = new ArrayList<Map<String,Object>>();
    	String allPhaseChk = (String)map.get("allPhaseChk");
        if("1".equals(allPhaseChk)){  //全部医嘱
          List<Map<String,Object>> recFinishPhaseUnStopOrd = cpRecDao.qryRecAllValidPhaseOrd(pkCprec);
          if(recFinishPhaseUnStopOrd!=null && recFinishPhaseUnStopOrd.size()>0)recAllPhaseOrd.addAll(recFinishPhaseUnStopOrd);
        }
        if(recPhaseOrd!=null&&recPhaseOrd.size()>0)recAllPhaseOrd.addAll(recPhaseOrd);
		List<Map<String,Object>> recPhaseCnOrd = cpRecDao.qryRecPhaseCnOrd(map);
		rtn.put("recPhaseOrd", recAllPhaseOrd);
		rtn.put("recPhaseAction", recPhaseAction);
		rtn.put("recPhaseCnOrd", recPhaseCnOrd);
		return rtn;
    }
    public void stopRecOrd(String param,IUser user){
    	List<RecPhaseOrdParam> ordParamList = JsonUtil.readValue(param, new TypeReference<List<RecPhaseOrdParam>>(){});
    	if(ordParamList==null ||ordParamList.size()<=0) return;
    	List<CnOrder> ords = new ArrayList<CnOrder>();
    	User u = (User)user;
    	String btnType = ordParamList.get(0).getBtnType();
    	if("stop".equals(btnType)){ //停止操作
    		for(RecPhaseOrdParam order : ordParamList){
    			CnOrder cnOrder = new CnOrder();
    			cnOrder.setFlagStop(Constants.TRUE);
    			cnOrder.setPkEmpStop(u.getPkEmp());
    			cnOrder.setNameEmpStop(u.getNameEmp());
    			cnOrder.setTs(new Date());
    			cnOrder.setDateStop(order.getDateStop());
    			cnOrder.setPkCnord(order.getPkCnord());
    			cnOrder.setCnSignCa(order.getCnSignCa());
    			ords.add(cnOrder);
    		}
    		if(ords.size()>0) DataBaseHelper.batchUpdate("update cn_order set flag_stop=:flagStop, pk_emp_stop=:pkEmpStop, name_emp_stop=:nameEmpStop,date_stop=:dateStop,ts=:ts  where pk_cnord=:pkCnord  and  eu_status_ord not in ('4','9') ", ords);	
    	}else if("canl".equals(btnType)){ //作废操作
    		CpRecExp cpRecExp = null;
    		List<CpRecExpDt> cpRecExpDtList = new ArrayList<CpRecExpDt>();
    		for(RecPhaseOrdParam order : ordParamList){
    			CnOrder cnOrder = new CnOrder();
    			cnOrder.setPkCnord(order.getPkCnord());
    			cnOrder.setCnSignCa(order.getCnSignCa());
    			ords.add(cnOrder);
    			order.setExpType("3"); //必做项目未做
    			if("1".equals(order.getFlagNec())){
    				cpRecExp = getCpRecExp(u, "0", cpRecExp, cpRecExpDtList, order);
    			}
    		}
    		cnPubService.cancleOrder(ords, user);
    		if(cpRecExp!=null) DataBaseHelper.insertBean(cpRecExp);
    		if(cpRecExpDtList.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecExpDt.class), cpRecExpDtList);
    	}
    	this.cnPubService.caRecordByOrd(false, ords);
    }
    public void useRecPhaseOrd(String param,IUser user){
    	List<RecPhaseOrdParam> ordParamList = JsonUtil.readValue(param, new TypeReference<List<RecPhaseOrdParam>>(){});
    	User u = (User) user;
    	if(ordParamList.size()==0) return;
    	List<CpRecDt> recDtList = new ArrayList<CpRecDt>();
    	CpRecExp cpRecExp = null;
    	List<CpRecExpDt> cpRecExpDtList = new ArrayList<CpRecExpDt>();
    	String  euType = ordParamList.get(0).getEuType();
    	if("1".equals(euType)){//活动
    		for(RecPhaseOrdParam ordParam : ordParamList){
    			//医嘱变异
        		cpRecExp = getCpRecExp(u, euType,cpRecExp, cpRecExpDtList, ordParam);
        		//路径应用明细
        		CpRecDt recDt = new CpRecDt();
        		recDt.setEuStatus("1"); //使用
        		recDt.setDateEx(new Date());
        		recDt.setPkEmpEx(u.getPkEmp());
        		recDt.setNameEmpEx(u.getNameEmp());
        		recDt.setPkCnord(null);
        		recDt.setPkRecdt(ordParam.getPkRecdt());
        		recDt.setTs(recDt.getDateEx());
        		recDtList.add(recDt);
    		}
    	}else{
        	List<CnOrder> ordList = new ArrayList<CnOrder>();
        	List<CnRisApply> risList = new ArrayList<CnRisApply>();
        	List<CnLabApply> lisList = new ArrayList<CnLabApply>();
        	List<CnOpApply> opList = new ArrayList<CnOpApply>();
        	List<CnOpSubjoin> opDtVO = new ArrayList<CnOpSubjoin>();
        	int ordsnC = bdSnService.getSerialNo("CN_ORDER", "ORDSN", ordParamList.size(), user);
        	Map<String,Integer> ordsnM = new HashMap<String,Integer>();
             for(RecPhaseOrdParam ordP : ordParamList)
             {
            	 ordsnM.put(ordP.getPkCpord(), ordsnC);
            	 ordsnC++;
             }
        	for(RecPhaseOrdParam ordParam : ordParamList){        		
        		//医嘱
        		CnOrder cnord = new CnOrder();
        		cnord.setCodeOrd(ordParam.getCodeOrd());
        		cnord.setNameOrd(ordParam.getNameOrd());
        		cnord.setEuAlways(ordParam.getEuAlways());
        		cnord.setPkCnord(NHISUUID.getKeyId());
        		ordParam.setPkCnord(cnord.getPkCnord());
        		cpRecExp = getCpRecExp(u, euType,cpRecExp, cpRecExpDtList, ordParam);
        		cnord.setDateStart(ordParam.getDateStart());
        		cnord.setDateEnter(cnord.getDateStart());
        		cnord.setPkOrg(u.getPkOrg());
        		cnord.setOrdsn(ordsnM.get(ordParam.getPkCpord()));
        		cnord.setOrdsnParent(ordsnM.get(ordParam.getPkParent()));
        		cnord.setEuPvtype("3");
        		cnord.setCodeOrdtype(ordParam.getCodeOrdtype());
        		cnord.setPkPv(ordParam.getPkPv());
        		cnord.setPkPi(ordParam.getPkPi());
        		cnord.setPkDeptNs(ordParam.getPkDeptNs());
        		cnord.setPkOrd(ordParam.getPkOrd());
        		cnord.setQuan(ordParam.getQuan());
        		cnord.setPkUnit(ordParam.getPkUnit());
        		cnord.setDosage(ordParam.getDosage());
        		cnord.setPkUnitDos(ordParam.getPkUnitDos());
        		cnord.setCodeFreq(ordParam.getCodeFreq());
        		cnord.setCodeSupply(ordParam.getCodeSupply());
        		cnord.setDripSpeed(ordParam.getDripSpeed());
        		cnord.setPkDeptExec(ordParam.getPkDeptExec());
        		cnord.setPkOrgExec(ordParam.getPkOrgExec());
        		cnord.setFlagEmer(ordParam.getFlagEmer());
        		cnord.setFlagThera(ordParam.getFlagThera());
        		cnord.setFlagPrev(ordParam.getFlagPrev());
        		cnord.setFlagFit(ordParam.getFlagFit());
        		cnord.setFlagDurg(ordParam.getFlagDurg());
        		cnord.setFlagBl(ordParam.getFlagBl());
        		cnord.setInfantNo(ordParam.getInfantNo());
        		cnord.setFlagFirst(ordParam.getFlagFirst());
        		cnord.setPkDept(u.getPkDept());
        		cnord.setPkEmpInput(u.getPkEmp());
        		cnord.setNameEmpInput(u.getNameEmp());
        		cnord.setPkEmpOrd(u.getPkEmp());
        		cnord.setNameEmpOrd(u.getNameEmp());
        		cnord.setSpec(ordParam.getSpec());
        		String sfalse = Constants.FALSE;
        		cnord.setFlagSign(Constants.TRUE);
        		cnord.setEuStatusOrd("1");
        		cnord.setQuanBed(new Double(0));   
        		cnord.setQuanCg(cnord.getQuanBed());
        		cnord.setPackSize(cnord.getQuanBed());
        		cnord.setPriceCg(cnord.getQuanBed());
        		cnord.setDateSign(new Date());
        		cnord.setTs(cnord.getDateSign());
        		cnord.setPkEmpChk(u.getPkEmp());
        		cnord.setNameEmpChk(u.getNameEmp());
        		cnord.setFlagSelf(sfalse);
        		cnord.setFlagNote(sfalse);
        		cnord.setFlagBase(sfalse);
        		cnord.setFlagStop(sfalse);
        		cnord.setFlagStopChk(sfalse);
        		cnord.setFlagErase(sfalse);
        		cnord.setFlagEraseChk(sfalse);
        		cnord.setFlagCp(Constants.TRUE);
        	    cnord.setFlagDoctor(Constants.TRUE);
        	    cnord.setFlagPrint(sfalse);
        	    cnord.setFlagMedout(sfalse);
        	    cnord.setDelFlag(sfalse);  
        	    cnord.setCnSignCa(ordParam.getCnSignCa());
        	    ordList.add(cnord);
        	   //检查
        	    if(ordParam.getCodeOrdtype().startsWith(Constants.RIS_SRVTYPE)){
        	    	CnRisApply risVO = ordParam.getRisVo();
        	       	cnord.setCodeApply(risVO.getCodeApply());
        	    	cnord.setPkDeptExec(risVO.getPkDeptExec());
        	    	cnord.setPkOrgExec(risVO.getPkOrgExec());
        	    	cnord.setQuan(risVO.getQuan());
        	    	cnord.setFlagEmer(risVO.getFlagEmer());
        	    	cnord.setNoteOrd(risVO.getNoteOrd());
        	    	risVO.setPkCnord(cnord.getPkCnord());
        	    	risVO.setPkOrdris(NHISUUID.getKeyId());
        	    	risVO.setPkOrg(u.getPkOrg());
        	    	risVO.setEuStatus("1");
        	    	risVO.setDelFlag("0");
        	    	risVO.setTs(new Date());
        	    	risList.add(risVO);
        	    }else if(ordParam.getCodeOrdtype().startsWith(Constants.LIS_SRVTYPE)){   //检验
        	    	CnLabApply lisVO = ordParam.getLisVo();
        	       	cnord.setCodeApply(lisVO.getCodeApply());
        	    	cnord.setPkDeptExec(lisVO.getPkDeptExec());
        	    	cnord.setPkOrgExec(lisVO.getPkOrgExec());
        	    	cnord.setQuan(lisVO.getQuan());
        	    	cnord.setFlagEmer(lisVO.getFlagEmer());
        	    	cnord.setNoteOrd(lisVO.getNoteOrd());
        	    	lisVO.setPkCnord(cnord.getPkCnord());
        	    	lisVO.setPkOrg(u.getPkOrg());
        	    	lisVO.setPkOrdlis(NHISUUID.getKeyId());
        	     	lisVO.setEuStatus("1");
        	     	lisVO.setDelFlag("0");
        	    	lisVO.setTs(new Date());
        	    	lisList.add(lisVO);
        	    }else if(ordParam.getCodeOrdtype().startsWith("04")){   //手术
        	    	CnOpApply opVO = ordParam.getOpVo();
        	    	cnord.setCodeApply(opVO.getCodeApply());
        	    	cnord.setPkDeptExec(opVO.getPkDeptExec());
        	    	cnord.setPkOrgExec(opVO.getPkOrgExec());
        	    	opVO.setPkCnord(cnord.getPkCnord());
        	    	opVO.setPkOrg(u.getPkOrg());
        	    	opVO.setPkOrdop(NHISUUID.getKeyId());
        	    	opVO.setEuStatus("1");
        	    	opVO.setDelFlag("0");
        	    	opVO.setTs(new Date());
        	    	opVO.setDateApply(cnord.getDateStart());
        	    	opList.add(opVO);
        	    	opDtVO = opVO.getSubOpList();
        	    	if(opDtVO!=null){
        	    		for(CnOpSubjoin dt : opDtVO){
            	    		dt.setPkCnopjoin(NHISUUID.getKeyId());
            	    		dt.setPkOrdop(opVO.getPkOrdop());
            	    		dt.setDelFlag("0");
            	    		dt.setTs(new Date());
            	    	}
        	    	}
        	    }
        	    CpRecDt recDt = new CpRecDt();
        		recDt.setEuStatus("1"); //使用
        		recDt.setDateEx(new Date());
        		recDt.setPkEmpEx(u.getPkEmp());
        		recDt.setNameEmpEx(u.getNameEmp());
        		recDt.setPkCnord(cnord.getPkCnord());
        		recDt.setPkRecdt(ordParam.getPkRecdt());
        		recDt.setTs(recDt.getDateEx());
        		recDtList.add(recDt);
        	}
        	if(ordList.size()>0)DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), ordList);
        	if(risList.size()>0)DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), risList);
        	if(lisList.size()>0)DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), lisList);
        	if(opList.size()>0)DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpApply.class), opList);
        	if(opDtVO!=null && opDtVO.size()>0)DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpSubjoin.class), opDtVO);
        	List<Map<String,Object>> sortIvMapList = CnOrderDao.qrySortIv(ordParamList.get(0).getPkPv());
        	if(sortIvMapList!=null && sortIvMapList.size()>0){
        		List<CnOrder> sortIvVolist = new ArrayList<CnOrder>();
        		int snum = 1;
        		for(Map<String,Object> sort :sortIvMapList){
        			CnOrder cnOrder = new CnOrder();
        			cnOrder.setPkCnord(sort.get("pkCnord").toString());
        			cnOrder.setSortIv(snum);
        			sortIvVolist.add(cnOrder);
        			snum++;
        		}
        		if(sortIvVolist.size()>0){
        			DataBaseHelper.batchUpdate("update cn_order set sort_iv=:sortIv where pk_cnord=:pkCnord ", sortIvVolist);
        		}
        	}
        	cnPubService.caRecordByOrd(false, ordList);
        	cnPubService.sendMessage("新医嘱", ordList,false);
    	}
    	if(recDtList.size()>0)DataBaseHelper.batchUpdate("update cp_rec_dt set eu_status=:euStatus, date_ex=:dateEx,pk_emp_ex=:pkEmpEx,name_emp_ex=:nameEmpEx,pk_cnord=:pkCnord,ts=:ts where pk_recdt=:pkRecdt", recDtList);
    	if(cpRecExp!=null) DataBaseHelper.insertBean(cpRecExp);
    	if(cpRecExpDtList.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecExpDt.class), cpRecExpDtList);
    	
    }
	private CpRecExp getCpRecExp(User u, String euType,CpRecExp cpRecExp,
			List<CpRecExpDt> cpRecExpDtList, RecPhaseOrdParam ordParam) {
		if(StringUtils.isNotEmpty(ordParam.getExpType())){ 
			if(cpRecExp==null){
				cpRecExp = new CpRecExp();
				cpRecExp.setDtCpexptype(ordParam.getExpType());//变异或推后
				
				cpRecExp.setPkOrg(u.getPkOrg());
				cpRecExp.setPkCpexp(ordParam.getPkCpexp());
				cpRecExp.setPkCprec(ordParam.getPkCprec());
				cpRecExp.setDateExp(new Date());
				cpRecExp.setPkEmpExp(u.getPkEmp());
				cpRecExp.setPkRecexp(NHISUUID.getKeyId());
				cpRecExp.setNote(ordParam.getExpNote());
			}
			CpRecExpDt cpRecExpDt = new CpRecExpDt();
			cpRecExpDt.setPkRecexpdt(NHISUUID.getKeyId());
			cpRecExpDt.setPkRecexp(cpRecExp.getPkRecexp());
			cpRecExpDt.setPkCpphase(ordParam.getPkCpphase());
			cpRecExpDt.setEuType(("0".equals(euType))?"2":"3"); //2医嘱 3工作
			cpRecExpDt.setPkCpord(ordParam.getPkCpord());
			cpRecExpDt.setPkCnord(ordParam.getPkCnord());
			cpRecExpDt.setPkTempwork(ordParam.getPkTempwork());
			cpRecExpDt.setPkOrg(u.getPkOrg());
			cpRecExpDt.setTs(new Date());
			cpRecExpDtList.add(cpRecExpDt);
		}
		return cpRecExp;
	}
    public void giveUpRecPhaseOrd(String param,IUser user){
    	List<RecPhaseOrdParam> ordParamList = JsonUtil.readValue(param, new TypeReference<List<RecPhaseOrdParam>>(){});
    	giveUpRecPhaseOrd(user, ordParamList);
    }
	private void giveUpRecPhaseOrd(IUser user,
			List<RecPhaseOrdParam> ordParamList) {
		if(ordParamList==null ||ordParamList.size()<=0) return;
		User u = (User)user;
    	CpRecExp cpRecExp = new CpRecExp();
	    	cpRecExp.setPkRecexp(NHISUUID.getKeyId());
	    	cpRecExp.setPkCprec(ordParamList.get(0).getPkCprec());
	    	cpRecExp.setDtCpexptype("04");//必做项目未做
	    	cpRecExp.setDateExp(new Date());
	    	cpRecExp.setPkEmpExp(u.getPkEmp());
	    	cpRecExp.setPkOrg(u.getPkOrg());
	     	cpRecExp.setPkCpexp(ordParamList.get(0).getPkCpexp()); 
	    List<CpRecExpDt> recExpDts = new ArrayList<CpRecExpDt>();
	    List<CpRecDt> recDts = new ArrayList<CpRecDt>();
	    for(RecPhaseOrdParam phaseOrd : ordParamList){
	    	CpRecExpDt cpRecExpDt = new CpRecExpDt();
	    	cpRecExpDt.setPkRecexp(cpRecExp.getPkRecexp());
	    	cpRecExpDt.setEuType(("0".equals(phaseOrd.getEuType()))?"2":"3"); //2医嘱 3工作
	    	cpRecExpDt.setPkOrg(u.getPkOrg());
	    	cpRecExpDt.setPkCpord(phaseOrd.getPkCpord());
	    	cpRecExpDt.setPkRecexpdt(NHISUUID.getKeyId());
	    	recExpDts.add(cpRecExpDt);
	    	   //路径应用明细
    	    CpRecDt cpRecDt = new CpRecDt();
    	    cpRecDt.setPkRecdt(phaseOrd.getPkRecdt());
    	    cpRecDt.setEuStatus("2"); //弃用
    	    cpRecDt.setPkEmpEx(u.getPkEmp());
    		cpRecDt.setNameEmpEx(u.getNameEmp());
    	    cpRecDt.setTs(new Date());
    	    recDts.add(cpRecDt);
	    }
	    DataBaseHelper.insertBean(cpRecExp);
	    if(recExpDts.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecExpDt.class), recExpDts);
	    if(recDts.size()>0)DataBaseHelper.batchUpdate("update cp_rec_dt set eu_status=:euStatus,pk_emp_ex=:pkEmpEx,name_emp_ex=:nameEmpEx,ts=:ts where pk_recdt=:pkRecdt", recDts);
	}
    public Map<String,Object> qryRisOrd(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pkCnord = (String)map.get("pkCnord");
		if(StringUtils.isEmpty(pkCnord)) return new HashMap<String,Object>();
		return cpRecDao.qryRisOrd(pkCnord);
    }
    public Map<String,Object> qryLabOrd(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pkCnord = (String)map.get("pkCnord");
		if(StringUtils.isEmpty(pkCnord)) return new HashMap<String,Object>();
		return cpRecDao.qryLisOrd(pkCnord);
    }
    public CnOpApplyVo qryOpOrd(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pkCnord = (String)map.get("pkCnord");
		if(StringUtils.isEmpty(pkCnord)) throw new BusException("前台传入pkCnord为空!");
		CnOpApplyVo opApply = cpRecDao.qryOpOrd(pkCnord);
		if(opApply!=null){
			List<CnOpSubjoin> opDts = cpRecDao.qryOpDtOrd(opApply.getPkOrdop());
			opApply.setSubOpList(opDts);
		}
		return opApply;
    }
    public List<Map<String,Object>> qryCateOrdDt(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	String pkCateord = (String)map.get("pkCateord");
    	if(StringUtils.isEmpty(pkCateord)) throw new BusException("前台传入的主键pkCateord为空!");
    	Map<String,Object> cateOrd = DataBaseHelper.queryForMap("select name_ord,eu_catetype,eu_ordtype from bd_cp_cateord where  pk_cateord=? ", pkCateord);
    	if(cateOrd==null) throw new BusException("请检查bd_cp_cateord!");
    	String euCatetype = (String)cateOrd.get("euCatetype");
    	String euOrdtype = (String)cateOrd.get("euOrdtype");
    	if(StringUtils.isEmpty(euCatetype)) throw new BusException("请维护该分类医嘱的分类类型!");
    	if(StringUtils.isEmpty(euOrdtype)) throw new BusException("请维护该分类医嘱的医嘱类型!");
    	map.put("pkOrg", ((User)user).getPkOrg());
    	map.put("nameOrd", (String)cateOrd.get("nameOrd"));
    	if("0".equals(euCatetype)){ //名称
    		if("0".equals(euOrdtype)){ //非药品
    			map.put("qryType", "nameNoPd");
    		}else if("1".equals(euOrdtype)){ //药品
    			map.put("qryType", "namePd");
    		}
    	}else if("1".equals(euCatetype)){ //字典
	   		if("0".equals(euOrdtype)){ //非药品
	   			map.put("qryType", "dictNoPd");
	    	}else if("1".equals(euOrdtype)){ //药品
	    		map.put("qryType", "dictPd");	
	    	}
    	}
    	return cpRecDao.qryCateOrdDt(map);
    }
    public List<Map<String,Object>> qryCpTempByDiag(String param, IUser user){
    	
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	String pkDiag = (String) paramMap.get("pkDiag");
    	List<Map<String,Object>> rtnList = new ArrayList<Map<String,Object>>();
    	if(StringUtils.isNotBlank(pkDiag)){
    		User u = (User)user;
    		rtnList = DataBaseHelper.queryForList("select cptemp.code_cp, cptemp.name_cp,cptemp.pk_cptemp,cptemp.amount_ref,cptemp.days_min,cptemp.days_max,cptemp.version "   
				     +"from cp_temp_diag  cpdiag  "
				     +"inner join  cp_temp cptemp on cpdiag.pk_cptemp = cptemp.pk_cptemp and cptemp.del_flag='0' and cptemp.eu_status='2'  "
				     +"inner join cp_temp_dept cpdept on cpdept.pk_cptemp = cptemp.pk_cptemp and cpdept.del_flag='0' and (cpdept.pk_org_use='~' or ( cpdept.pk_org_use=? and cpdept.pk_dept_use=?)) "  
				     +"where  cpdiag.del_flag='0' and cpdiag.pk_diag=?", u.getPkOrg(),u.getPkDept(),pkDiag);
    	}
    
		return rtnList;	
    }
}
