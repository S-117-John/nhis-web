package com.zebone.nhis.cn.opdw.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.zebone.platform.common.support.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.util.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.cn.opdw.dao.CnOpPatiOverviewMapper;
import com.zebone.nhis.cn.opdw.vo.ApptPatient;
import com.zebone.nhis.cn.opdw.vo.OpPatiInfo;
import com.zebone.nhis.cn.opdw.vo.OpPatiOvervieVo;
import com.zebone.nhis.cn.opdw.vo.PvEncounterVo;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.SM4Utils;
import com.zebone.nhis.pv.pub.service.RegPubService;
import com.zebone.nhis.pv.pub.vo.PvOpParam;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊医生站--患者信息总览,就诊历史查询服务
 * @author Roger
 */
@Service
public class CnOpPaitOverviewService {
	
	@Autowired
	private CnOpPatiOverviewMapper cnOpPatiOverviewMapper;
	
	@Autowired
	private  RegPubService regPubService; 
	
	@Autowired
	private PareAccoutService pareAccoutService;
	
	static Integer ADDCONST = 9000;
	
	
	/**
	 * 患者校验
	 * @param param
	 * @param user pk_pv,pk_dept,pk_emp
	 * @return
	 */
	public List<String> checkPati(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkPv = paramMap.get("pkPv");
		String pkEmp = paramMap.get("pkEmp");
		String pkDept = paramMap.get("pkDept");
		List<String> res = null;
		
	    if(CommonUtils.isEmptyString(pkPv) || CommonUtils.isEmptyString(pkEmp) || CommonUtils.isEmptyString(pkDept)){
	    	return res;
	    }
	    
		PvOp  pvOp =  cnOpPatiOverviewMapper.qryPvOpByPkPv(pkPv);
		List<SchResource> schRess = cnOpPatiOverviewMapper.qrySchResByCon(pkEmp, pkDept);
		 SchResource schRes = schRess!=null&&schRess.size()>0?schRess.get(0):null;
		 if(schRes==null){
			 throw new BusException("当前医生没有排班资源，请排班！");
		 }
		if(pvOp == null || schRes == null || CommonUtils.isEmptyString(schRes.getPkSchres())) return res;
		if(schRes.getPkSchres().equals(pvOp.getPkRes())){
			res = new ArrayList<String>();
			res.add("true");
		}
		return res;
	}
	
	
	/**
	 * 患者总览信息查询
	 * @param param
	 * @param user
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public OpPatiOvervieVo qryPatiOverviewInfo(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkPv = paramMap.get("pkPv");
		
		/*OpPatiOvervieVo res = new OpPatiOvervieVo();
		if(!CommonUtils.isEmptyString(pkPv)){
			res = cnOpPatiOverviewMapper.qryEmrDataByPkpv(pkPv)==null?new OpPatiOvervieVo():cnOpPatiOverviewMapper.qryEmrDataByPkpv(pkPv);
			res.setLabOccVos(cnOpPatiOverviewMapper.qryLabDataByPkpv(pkPv));
			res.setRisOccVos(cnOpPatiOverviewMapper.qryRisDataByPkpv(pkPv));
			res.setOrdVos(cnOpPatiOverviewMapper.qryOrdDataByPkpv(pkPv));
		}*/
		OpPatiOvervieVo res = null;
		if(!CommonUtils.isEmptyString(pkPv)){
			
			List<OpPatiOvervieVo> ps = cnOpPatiOverviewMapper.qryEmrDataByPkpv(pkPv);
			if(ps!=null && ps.size()>0){
				for(OpPatiOvervieVo opPati : ps){
					res = new OpPatiOvervieVo();
					res = opPati;
				}
			}else{
				res = new OpPatiOvervieVo();
			}
			res.setLabOccVos(cnOpPatiOverviewMapper.qryLabDataByPkpv(pkPv));
			res.setRisOccVos(cnOpPatiOverviewMapper.qryRisDataByPkpv(pkPv));
			res.setOrdVos(cnOpPatiOverviewMapper.qryOrdDataByPkpv(pkPv));
		}
		
		return res;
	}
	
	/**
	 * 查询门诊患者历史就诊(含门诊、急诊、住院等)
	 * 交易号：004003004006
	 * @param param
	 * @param user
	 * @return 
	 */
	public List<PvEncounterVo> qryPkpiHistoryPvInfo(String param,IUser user) {
		List<PvEncounterVo> pvEncounterList = new ArrayList<PvEncounterVo>();
		int SEARCH_LIMIT_COUNT = 99;//查询前100条
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap != null)
		{
			String pkPi = (String)paramMap.get("pkPi");
			if(pkPi != null && !"".equals(pkPi))
			{
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT pv.pk_pv as pkpv, ");
				sb.append("pv.date_begin as datebegin, ");
				sb.append("pv.pk_dept as pkdept, ");
				sb.append("pv.eu_pvtype as enpvtype, ");
				sb.append("pv.pk_emp_phy as pkempphy, ");
				sb.append("pv.name_emp_phy as nameempphy, ");
				sb.append("diag.name_diag as namediag ");
				sb.append("FROM pv_encounter pv ");
				sb.append("LEFT JOIN pv_diag diag ");
				sb.append("ON (pv.pk_pv = diag.pk_pv and diag.flag_maj = '1') ");
				sb.append("WHERE 1 = 1 ");
				sb.append("AND pv.flag_cancel = '0' ");
				int nParam = 0;
				//判断是否有科室参数
				String pkDept = (String)paramMap.get("pkDept");
				if(pkDept != null && !"".equals(pkDept))
				{
					sb.append("AND pv.pk_dept = ? ");
					nParam = nParam | 4;
				}
				//判断是否有起始日期
				String dateBegin = (String)paramMap.get("dateBegin");
				if(dateBegin != null && !"".equals(dateBegin))
				{
					sb.append("AND pv.date_begin >= to_date(substr(?, 1, 8)||'000000', 'yyyymmddhh24miss') ");
					nParam = nParam | 2;
				}
				//判断是否有截止日期
				String dateEnd = (String)paramMap.get("dateEnd");
				if(dateEnd != null && !"".equals(dateEnd))
				{
					sb.append("AND pv.date_begin <= to_date( substr(?, 1, 8)||'235959', 'yyyymmddhh24miss') ");
					nParam = nParam | 1;
				}
				sb.append("AND pv.pk_pi = ? ");
				sb.append("ORDER  BY pv.date_begin DESC ");
				String sql = sb.toString();
				Object[] objArray = null;
				if(nParam == 7)
				{
					objArray = new Object[]{pkDept, dateBegin, dateEnd, pkPi};
				}
				else if(nParam == 6)
				{
					objArray = new Object[]{pkDept, dateBegin, pkPi};
				}
				else if(nParam == 5)
				{
					objArray = new Object[]{pkDept, dateEnd, pkPi};
				}
				else if(nParam == 4)
				{
					objArray = new Object[]{pkDept, pkPi};
				}
				else if(nParam == 3)
				{
					objArray = new Object[]{dateBegin, dateEnd, pkPi};
				}
				else if(nParam == 2)
				{
					objArray = new Object[]{dateBegin, pkPi};
				}
				else if(nParam == 1)
				{
					objArray = new Object[]{dateEnd, pkPi};
				}
				else if(nParam == 0)
				{
					objArray = new Object[]{pkPi};
				}
				List<Map<String, Object>> pvList = DataBaseHelper.queryForList(sql, objArray);
				Map<String, PvEncounterVo> mapPvEncounterVo = new HashMap<String, PvEncounterVo>();
				//就诊主键
				//StringBuilder sbInStrOfPkpv = new StringBuilder();
				Set<String> pkPvs = new HashSet<String>();
				//是否限制前100条
				String limit = (String)paramMap.get("limit");
				int nIndex = 0;
				for(Map<String, Object> mapItem : pvList) 
				{
					if("1".equals(limit))
					{
						if(nIndex > SEARCH_LIMIT_COUNT)
						{
							break;
						}
					}
					PvEncounterVo pvEncounterVo = new PvEncounterVo();
					pvEncounterVo.setPkPv((String)mapItem.get("pkpv"));
					pvEncounterVo.setDateBegin((Date)mapItem.get("datebegin"));
					pvEncounterVo.setPkDept((String)mapItem.get("pkdept"));
					pvEncounterVo.setEuPvtype((String)mapItem.get("enpvtype"));
					pvEncounterVo.setPkEmpPhy((String)mapItem.get("pkempphy"));
					pvEncounterVo.setNameEmpPhy((String)mapItem.get("nameempphy"));
					pvEncounterVo.setNameDiag((String)mapItem.get("namediag"));
					pvEncounterList.add(pvEncounterVo);
					mapPvEncounterVo.put(pvEncounterVo.getPkPv(), pvEncounterVo);
					if("1".equals(pvEncounterVo.getEuPvtype()) || "2".equals(pvEncounterVo.getEuPvtype()))
					{
						//do nothing
						/*if(sbInStrOfPkpv.length() > 0)
						{
							sbInStrOfPkpv.append(",");
						}
						sbInStrOfPkpv.append("'");
						sbInStrOfPkpv.append(pvEncounterVo.getPkPv());
						sbInStrOfPkpv.append("'");*/
						pkPvs.add(pvEncounterVo.getPkPv());
						//暂时设置处方金额为0，在后面获取计算
						pvEncounterVo.setRecipeTotalMoney(new BigDecimal(0));
						pvEncounterVo.setRecipeTotalMoneyForPayed(new BigDecimal(0));
					}
					else
					{
						//设置处方金额为0
						pvEncounterVo.setRecipeTotalMoney(new BigDecimal(0));
						pvEncounterVo.setRecipeTotalMoneyForPayed(new BigDecimal(0));
					}
					
					nIndex++;
				}
				//查询处方费用
				if(pkPvs.size() > 0)
				{
					sb = new StringBuilder();
					sb.append("SELECT bod.AMOUNT as amount, ");
					sb.append("bod.flag_settle as flagsettle, ");
					sb.append("bod.pk_pv as pkpv ");
					sb.append("FROM   bl_op_dt bod ");
					sb.append("WHERE  EXISTS (SELECT 1 ");
					sb.append("FROM   CN_ORDER co ");
					sb.append("WHERE  co.pk_cnord = bod.pk_cnord ");
					sb.append("AND    (co.pk_pv IN ( ");
					sb.append(CommonUtils.convertSetToSqlInPart(pkPvs, "co.pk_pv"));
					sb.append("))) ");
					sb.append("AND    (bod.pk_pv IN ( ");
					sb.append(CommonUtils.convertSetToSqlInPart(pkPvs, "bod.pk_pv"));
					sb.append(")) ");
					sql = sb.toString();

					List<Map<String, Object>> bodList = DataBaseHelper.queryForList(sql, new Object[]{});
					String pkPvTemp = null;
					String flagSettle = null;
					BigDecimal amount = null;
					for(Map<String, Object> mapItem : bodList) 
					{
						pkPvTemp = (String)mapItem.get("pkpv");
						flagSettle = (String)mapItem.get("flagsettle");
						if(mapItem.get("amount") != null)
						{
							amount = new BigDecimal(mapItem.get("amount").toString());
						}
						else
						{
							amount = new BigDecimal(0);
						}
						PvEncounterVo pvEncounterVoTemp = mapPvEncounterVo.get(pkPvTemp);
						BigDecimal recipeTotalMoney = pvEncounterVoTemp.getRecipeTotalMoney();
						if(recipeTotalMoney == null)
						{
							pvEncounterVoTemp.setRecipeTotalMoney(amount);
						}
						else
						{
							pvEncounterVoTemp.setRecipeTotalMoney(recipeTotalMoney.add(amount));
						}
						if("1".equals(flagSettle))
						{
							BigDecimal recipeTotalMoneyForPayed = pvEncounterVoTemp.getRecipeTotalMoneyForPayed();
							if(recipeTotalMoneyForPayed == null)
							{
								pvEncounterVoTemp.setRecipeTotalMoneyForPayed(amount);
							}
							else
							{
								pvEncounterVoTemp.setRecipeTotalMoneyForPayed(recipeTotalMoneyForPayed.add(amount));
							}
						}
					}
				}				
			}
		}
		
		//return cnDiagMapper.qryCnDiag((String)map.get("pkPv"));
		
		return pvEncounterList;
	}
	
	public OpPatiOvervieVo qryPatiHistoryInfo(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkPv = paramMap.get("pkPv");
/*		OpPatiOvervieVo res  = new OpPatiOvervieVo();
		if(!CommonUtils.isEmptyString(pkPv)){
			res = cnOpPatiOverviewMapper.qryEmrDataByPkpv(pkPv)==null?new OpPatiOvervieVo():cnOpPatiOverviewMapper.qryEmrDataByPkpv(pkPv);
			List<PvDiag> diag = cnOpPatiOverviewMapper.qryPvDiagByPkPv(pkPv);
			res.setDescDiag(diag!=null && diag.size()>0?diag.get(0).getDescDiag():null);
			res.setOrdVos(cnOpPatiOverviewMapper.qryOrdDataByPkpv(pkPv));
			res.setRptVos(cnOpPatiOverviewMapper.qryRptDataByPkpv(pkPv));
		}*/
		
		OpPatiOvervieVo res  = null;
		if(!CommonUtils.isEmptyString(pkPv)){
			List<OpPatiOvervieVo> ps = cnOpPatiOverviewMapper.qryEmrDataByPkpv(pkPv);
			if(ps!=null && ps.size()>0){
				for(OpPatiOvervieVo opPati : ps){
					 res = new OpPatiOvervieVo();
					 res = opPati;
				 }
			}else{
				res = new OpPatiOvervieVo();
			}
			 
			List<PvDiag> diag = cnOpPatiOverviewMapper.qryPvDiagByPkPv(pkPv);
			res.setDescDiag(diag!=null && diag.size()>0?diag.get(0).getDescDiag():null);
			res.setOrdVos(cnOpPatiOverviewMapper.qryOrdDataByPkpv(pkPv));
			res.setRptVos(cnOpPatiOverviewMapper.qryRptDataByPkpv(pkPv));
		}
		
		return res;
	}
	
	/**
	 * 叫号
	 * @param param
	 * @param user
	 */
	public void  updateWaitPati(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkPv = paramMap.get("pkPv");
		String dateClinic = paramMap.get("dateClinic");
		String pkEmpPhy = paramMap.get("pkEmpPhy");
		String nameEmpPhy = paramMap.get("nameEmpPhy");
		PvEncounter pvvo  = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?", PvEncounter.class, pkPv);
		if(pvvo!=null){
			pvvo.setPkEmpPhy(pkEmpPhy);
			pvvo.setNameEmpPhy(nameEmpPhy);
			pvvo.setEuStatus(EnumerateParameter.ONE);
			pvvo.setDateClinic(dateTrans(dateClinic));
		}
		DataBaseHelper.updateBeanByPk(pvvo,false);
	}
	
	/**
	 * 取消接诊
	 * @param param
	 * @param user
	 */
	public void  cancelPati(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkPv = paramMap.get("pkPv");
		String type = ApplicationUtils.getSysparam("PV0008", false);
		Map<String,Object> ordCntMap = DataBaseHelper.queryForMap("select count(1) cnt from cn_order ord where ord.pk_pv=?", pkPv);
		
		int ordCnt = 0;
		if(ordCntMap!=null && ordCntMap.size()>0){
			if(ordCntMap.get("cnt") instanceof BigDecimal){
				BigDecimal amt = amtTrans(ordCntMap.get("cnt"));
				ordCnt = amt.intValue();
			}else{
				ordCnt = ordCntMap.get("cnt")==null?0:(Integer)ordCntMap.get("cnt");
			}
		}
		
		
		Map<String,Object> emrCntMap = DataBaseHelper.queryForMap("select count(1) cnt from  cn_emr_op emr where emr.pk_pv=?", pkPv);
		int emrCnt = 0;
		if(emrCntMap!=null && emrCntMap.size()>0){
			if(emrCntMap.get("cnt") instanceof BigDecimal){
				BigDecimal amt = amtTrans(emrCntMap.get("cnt"));
				emrCnt = amt.intValue();
			}else{
				emrCnt = emrCntMap.get("cnt")==null?0:(Integer)emrCntMap.get("cnt");
			}
			
		}
		
		if(emrCnt<=0 && ordCnt<=0){
			DataBaseHelper.execute("update pv_encounter set eu_status=0, date_clinic=null, pk_emp_phy=null,name_emp_phy=null where pk_pv=?", pkPv);
			if(EnumerateParameter.ONE.equals(type)){//分诊叫号模式：
				//调用分诊叫号接口，更新分诊状态为“取消”。
			}
		}else{
			throw new BusException("当已开立医嘱或已有病历时，不能取消接诊!");
		}
	}
	
	/**
	 * 查询可加号患者列表
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public List<PiMaster> getPiMasterList(String param, IUser user){
		PiMaster master = JsonUtil.readValue(param, PiMaster.class);
		List<PiMaster> list = cnOpPatiOverviewMapper.getPiMasterList(master);		
		return list;
	} 
	
	/**
	 * @throws ParseException 
	 * 查询预约患者列表
	 * @param  param
	 * @return user
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	public List<ApptPatient> getApptPatientList(String param, IUser user) throws ParseException{
		Map<String,Object> para = JsonUtil.readValue(param, Map.class);
		
		String pkDept =  UserContext.getUser().getPkDept();
		para.put("pkDeptEx", pkDept);
		Calendar calCurr =Calendar.getInstance();
		calCurr.setTimeInMillis(System.currentTimeMillis());
		calCurr.set(Calendar.HOUR_OF_DAY, 0);
		calCurr.set(Calendar.MINUTE, 0);
		calCurr.set(Calendar.SECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		Date nowDate = sdf.parse(sdf.format(date));
		para.put("dateAppt", calCurr.getTime());
		List<ApptPatient> list = cnOpPatiOverviewMapper.getApptPatientList(para);
		for(int i =list.size() - 1; i >= 0; i--){
			ApptPatient lis = list.get(i);
			String timeBegin = lis.getTimeBegin();
			String timeEnd = lis.getTimeEnd();
			Date beginTime = sdf.parse(timeBegin);
			Date endTime = sdf.parse(timeEnd);
			if(nowDate.getTime()<beginTime.getTime() || nowDate.getTime()>endTime.getTime()){
				list.remove(i);
			}
		}
		return list;
	} 
	
	
	/**
	 * 加号处理
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void addRegPati(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkPi = paramMap.get("pkPi");
		String pkEmp = UserContext.getUser().getPkEmp();
		String pkDept =  UserContext.getUser().getPkDept();
		
		List<SchResource> schRess = cnOpPatiOverviewMapper.qrySchResByCon(UserContext.getUser().getPkEmp(), pkDept);
		 if(schRess==null || schRess.size()==0){
			 throw new BusException("当前医生没有排班资源，请排班！");
		 }
		String pkDateslot = null;
		Date dateWork = null;
		SchSch sch = null;
		Date dateEnd = null;
		//1.判断当前是否还有可加号数
		List<BdCodeDateslot> res = cnOpPatiOverviewMapper.getCurDateSlot();
		if(res!=null && res.size()>0){			
			for(BdCodeDateslot vo:res){
				long now = System.currentTimeMillis();
				Calendar calBegin = setDate(vo.getTimeBegin(),now);
				Calendar calCurr =Calendar.getInstance();
				calCurr.setTimeInMillis(now);
				Calendar calEnd = setDate(vo.getTimeEnd(),now);			
				if(isInRange(calBegin, calCurr, calEnd)){
					dateEnd = calEnd.getTime();
					pkDateslot = vo.getPkDateslot();
					calCurr.set(Calendar.HOUR_OF_DAY, 0);
					calCurr.set(Calendar.MINUTE, 0);
					calCurr.set(Calendar.SECOND, 0);
					dateWork = calCurr.getTime();
					break;
				}
			}
		}else{
			throw new BusException(" 当前午别模式未维护，请维护！"); 
		}
		
		if(dateWork!=null && pkDateslot!=null){
			
		}else{
			throw new BusException(" 当前午别模式未维护，请维护！"); 
		}
		Map<String,Object> schParam = new HashMap<String, Object>();
		schParam.put("dateWork", dateWork);
		schParam.put("pkDateslot", pkDateslot);
		Integer ticket = null;
		List<SchSch> schList = null;
		for(SchResource schRes : schRess){
			if("1".equals(schRes.getEuRestype())){
				schParam.put("pkSchres", schRes.getPkSchres());		
				schList = cnOpPatiOverviewMapper.getAddSch(schParam);
				break;
			}					
		}
		if(schParam.get("pkSchres")==null){
			for(SchResource schRes : schRess){
				schParam.put("pkSchres", schRes.getPkSchres());		
				schList = cnOpPatiOverviewMapper.getAddSch(schParam);
				if(schList!=null && schList.size()>0){
					break;
				}
			}
		}
		
		if(schList!=null && schList.size()>0){
			for(int i = 0;i<schList.size();i++){
				sch = schList.get(i);
				if(sch.getCntAdd()-sch.getCntOver()<=0 ){
					if(i== schList.size()-1){
						throw new BusException("没有可加号数，加号失败！"); 
					}					
				}else{
					ticket = sch.getCntOver()+1+ADDCONST;
					break;
				}
			}
			 
			
		}else{
			throw new BusException("没有可加号数，加号失败！"); 
		}

		//判断该患者在该科室是否已有加号信息
		String sql = "select count(1) from sch_appt_pv sap inner join sch_appt sa "
				+ "on sa.pk_schappt=sap.pk_schappt where sap.del_flag='0' "
				+ "and sap.eu_apptmode='1' and sa.pk_dept_ex=? and sa.pk_pi=? and sa.end_time>? ";
		int count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkDept,pkPi,new Date()});
		if(count >0){
			throw new BusException("您在当前科室已加过号，请去挂号处缴费！");
		}				
		
		//2.加号处理
		 //2.1写表sch_appt
		  SchAppt schAppt = new SchAppt();
			schAppt.setPkOrg(UserContext.getUser().getPkOrg());
			schAppt.setEuSchclass("0");
			schAppt.setPkSch(sch.getPkSch());
			schAppt.setDateAppt(dateWork);
			schAppt.setPkDateslot(pkDateslot);
			schAppt.setPkSchres(sch.getPkSchres());
			schAppt.setPkSchsrv(sch.getPkSchsrv());
			schAppt.setPkPi(pkPi);
			schAppt.setPkOrgEx(UserContext.getUser().getPkOrg());
			schAppt.setPkDeptEx(pkDept);
			schAppt.setDateReg(new Date());
		    schAppt.setPkDeptReg(pkDept);
		    schAppt.setPkEmpReg(pkEmp);
		    schAppt.setNameEmpReg(UserContext.getUser().getNameEmp());
		    schAppt.setEuStatus("0");
		    schAppt.setFlagPay("0");
		    schAppt.setFlagNotice("0");
		    schAppt.setFlagCancel("0");
		    schAppt.setFlagNoticeCanc("0");
		    schAppt.setEndTime(dateEnd);
		    schAppt.setTicketNo(ticket==null?null:ticket.toString());
		    ApplicationUtils.setDefaultValue(schAppt, true);
	       DataBaseHelper.insertBean(schAppt);
		   //2.2写表sch_appt_pv
		   SchApptPv apptPv = new SchApptPv();
			   apptPv.setPkOrg(UserContext.getUser().getPkOrg());
			   apptPv.setPkSchappt(schAppt.getPkSchappt());
			   apptPv.setEuApptmode("1");
			   apptPv.setPkEmpPhy(UserContext.getUser().getPkEmp());
			   apptPv.setNameEmpPhy(UserContext.getUser().getNameEmp());
			   apptPv.setFlagPv("0");
			   ApplicationUtils.setDefaultValue(apptPv, true);
			DataBaseHelper.insertBean(apptPv);
			//2.3更新表sch_sch；
			DataBaseHelper.execute(" update sch_sch  set cnt_over=cnt_over+1 where pk_sch=? ", sch.getPkSch());
	
	} 
	
	/**
	 * 加号处理
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void addRegPatiByDateTime(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkPi = paramMap.get("pkPi");
		String pkEmp = UserContext.getUser().getPkEmp();
		String pkDept =  UserContext.getUser().getPkDept();
		
		List<SchResource> schRess = cnOpPatiOverviewMapper.qrySchResByCon(UserContext.getUser().getPkEmp(), pkDept);
		if(schRess==null || schRess.size()==0){
			throw new BusException("当前医生没有排班资源，请排班！");
		}
		
		Map<String,Object> schParam = new HashMap<String, Object>();
		schParam.put("dateWork", DateUtils.getDate("yyyyMMdd") + "000000");//yyyyMMdd
		schParam.put("timeWork", DateUtils.getDate("HH:mm:ss"));//HHmmss
		
		
		List<SchSch> schList = null;
		for(SchResource schRes : schRess){
			if("1".equals(schRes.getEuRestype())){
				schParam.put("pkSchres", schRes.getPkSchres());		
				schList = cnOpPatiOverviewMapper.getAddSchByDateTime(schParam);
				break;
			}					
		}
		if(schParam.get("pkSchres")==null){
			for(SchResource schRes : schRess){
				schParam.put("pkSchres", schRes.getPkSchres());		
				schList = cnOpPatiOverviewMapper.getAddSchByDateTime(schParam);
				if(schList!=null && schList.size()>0){
					break;
				}
			}
		}
		
		if(schList == null || schList.size() < 1)
			throw new BusException("没有当前可用的排班，加号失败！"); 
		
		SchSch sch = null;
		Integer ticket = null;
		if(schList!=null && schList.size()>0){
			for(int i = 0;i<schList.size();i++){
				sch = schList.get(i);
				if(sch.getCntAdd()-sch.getCntOver()<=0 ){
					if(i== schList.size()-1){
						throw new BusException("没有可加号数，加号失败！"); 
					}					
				}else{
					ticket = sch.getCntOver()+1+ADDCONST;
					break;
				}
			}
		}

		//判断该患者在该科室是否已有加号信息
		String sql = "select count(1) from sch_appt_pv sap "
				+ "inner join sch_appt sa on sa.pk_schappt=sap.pk_schappt "
				+ "where sap.del_flag='0' and sap.eu_apptmode='1' "
				+ "and sa.pk_dept_ex=? and sa.pk_pi=? and sa.end_time>? ";
		int count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkDept,pkPi,new Date()});
		if(count >0){
			throw new BusException("您在当前科室已加过号，请去挂号处缴费！");
		}				
		
		//2.加号处理
		//2.1写表sch_appt
		SchAppt schAppt = new SchAppt();
		schAppt.setPkOrg(UserContext.getUser().getPkOrg());
		schAppt.setEuSchclass("0");
		schAppt.setPkSch(sch.getPkSch());
		schAppt.setDateAppt(sch.getDateWork());
		schAppt.setPkDateslot(sch.getPkDateslot());
		schAppt.setPkSchres(sch.getPkSchres());
		schAppt.setPkSchsrv(sch.getPkSchsrv());
		schAppt.setPkPi(pkPi);
		schAppt.setPkOrgEx(UserContext.getUser().getPkOrg());
		schAppt.setPkDeptEx(pkDept);
		schAppt.setDateReg(new Date());
		schAppt.setPkDeptReg(pkDept);
		schAppt.setPkEmpReg(pkEmp);
		schAppt.setNameEmpReg(UserContext.getUser().getNameEmp());
		schAppt.setEuStatus("0");
		schAppt.setFlagPay("0");
		schAppt.setFlagNotice("0");
		schAppt.setFlagCancel("0");
		schAppt.setFlagNoticeCanc("0");
		schAppt.setBeginTime(new Date());
		//结束时间 = 开始时间 + sch.minutePer
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, sch.getMinutePer());
		schAppt.setEndTime(c.getTime());
		schAppt.setTicketNo(ticket==null?null:ticket.toString());
		ApplicationUtils.setDefaultValue(schAppt, true);
		DataBaseHelper.insertBean(schAppt);
		
		//2.2写表sch_appt_pv
		SchApptPv apptPv = new SchApptPv();
		apptPv.setPkOrg(UserContext.getUser().getPkOrg());
		apptPv.setPkSchappt(schAppt.getPkSchappt());
		apptPv.setEuApptmode("1");
		apptPv.setPkEmpPhy(UserContext.getUser().getPkEmp());
		apptPv.setNameEmpPhy(UserContext.getUser().getNameEmp());
		apptPv.setFlagPv("0");
		ApplicationUtils.setDefaultValue(apptPv, true);
		DataBaseHelper.insertBean(apptPv);
		
		//2.3更新表sch_sch；
		DataBaseHelper.execute(" update sch_sch  set cnt_over=cnt_over+1 where pk_sch=? ", sch.getPkSch());
	
	} 
	
	/**
	 * 诊间支付
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void payFees(String param, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		String pkPi = (String)paramMap.get("pkPi");
		String pkPv = (String)paramMap.get("pkPv");
		BigDecimal amount = (BigDecimal)paramMap.get("amount");
		
		//1.账户扣款
		BlDeposit dp = new BlDeposit();
		dp.setPkPi(pkPi);
		dp.setPkPv(pkPv);
		dp.setAmount(amount.multiply(new BigDecimal(-1)));
		dp.setEuDirect("-1");
		dp.setPkEmpPay(UserContext.getUser().getPkEmp());
		dp.setNameEmpPay(UserContext.getUser().getNameEmp());
		PiAcc pa = piAccDetailVal(dp);
		
		//2.更新门急诊收费明细----为后期的医保及发票提供判断指标
		DataBaseHelper.execute(" update bl_op_dt set flag_acc=1,pk_acc = ? where pk_pi = ?",pa.getPkPiacc(),pkPi);
	}
	
	/**
	 * 预约签到
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void apptPati(String param, IUser user){
		ApptPatient pati  = JsonUtil.readValue(param, new TypeReference<ApptPatient>(){});
		List<PvOpParam> paramList = new ArrayList<PvOpParam>();
		PvOpParam vo = new PvOpParam();
		vo.setOrderNo("1");
		vo.setPkPi(pati.getPkPi());
		String pkPicate = pati.getPkPicate();
		if(pkPicate != null){
			int count = DataBaseHelper.queryForScalar("select count(1) from PI_CATE where pk_picate = ? and del_flag = '0'", Integer.class, pkPicate);
			if(count == 0){//如果根据患者分类参数无法获取记录，说明该值是错误的，置为空，重新查询默认的
				pkPicate = null;
			}
		}
		if(pkPicate == null || "".equals(pkPicate)){
			PiCate piCate = DataBaseHelper.queryForBean("select * from PI_CATE where flag_def = '1' and del_flag = '0'", PiCate.class);
			pkPicate = piCate.getPkPicate();
		}
		vo.setPkPicate(pkPicate);
		SchAppt appt = DataBaseHelper.queryForBean("select * from sch_appt where pk_schappt = ?", SchAppt.class, pati.getPkSchappt());
		vo.setPkSchsrv(pati.getPkSchappt());
		vo.setPkSchsrv(appt.getPkSchsrv());
		vo.setPkRes(appt.getPkSchres());
		vo.setPkDateslot(appt.getPkDateslot());
		vo.setPkSch(appt.getPkSch());
		vo.setPkAppo(appt.getPkSchappt());
		String pkHp = "";
		PiInsurance insu = DataBaseHelper.queryForBean("select * from pi_insurance where pk_pi = ? and flag_def = '1'", PiInsurance.class, pati.getPkPi());
		if(insu != null){
			int count = DataBaseHelper.queryForScalar("select count(1) from bd_hp where pk_hp = ? and del_flag = '0'", Integer.class, insu.getPkHp());
			if(count == 0){ //错误的医保信息
				
			}else{
				pkHp = insu.getPkHp();
			}			
		}
		if("".equals(pkHp)){ //如果无法获取患者医保，取医保类型为全自费的第一条
			List<PiInsurance> insuList = DataBaseHelper.queryForList("select pk_hp from bd_hp where EU_HPTYPE = '0' and FLAG_OP = '1' and del_flag = '0'", PiInsurance.class);
			if(CollectionUtils.isNotEmpty(insuList)){
				pkHp = insuList.get(0).getPkHp();
			}else{
				throw new BusException("无法获取患者医保信息 ！");
			}
		}
		vo.setPkInsu(pkHp);
		vo.setPkDeptPv(UserContext.getUser().getPkDept());
		vo.setPkEmpPv(UserContext.getUser().getPkEmp());
		vo.setNameEmpPv(UserContext.getUser().getNameEmp());
		paramList.add(vo);
		regPubService.saveAppoPvEncounterAndOp(paramList, user);
		
		DataBaseHelper.update("UPDATE SCH_APPT SET EU_STATUS = '1' WHERE PK_SCHAPPT = ?", new Object[]{pati.getPkSchappt()});
		
	}
	
	/**
	 * 查询初始化入院通知单
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public OpPatiInfo qryPatiOpInfo(String param, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		String pkPv = (String)paramMap.get("pkPv");	
		OpPatiInfo opPatiInfo = cnOpPatiOverviewMapper.qryPatiOpInfo(pkPv);
		if(BeanUtils.isNotNull(opPatiInfo)) {
			int count = DataBaseHelper.queryForScalar("select count(1) from pv_ip_notice where pk_pv_op=? and EU_STATUS=2", 
					Integer.class,pkPv);
			opPatiInfo.setIsExists(Integer.valueOf(count));
			
			String diagType = MapUtils.getString(paramMap,"diagType");
			if("1".equals(diagType)){ //住院诊断编码字典中山
				Map<String, String> map=new HashMap<>();
				map.put("pkDiag",opPatiInfo.getPkDiagMaj());
				List<Map<String,Object>> retMap=cnOpPatiOverviewMapper.qryCnDaigByPk(map);
				if(retMap!=null && retMap.size()>0){
					String name = MapUtils.getString(retMap.get(0),"name");
					if(org.apache.commons.lang3.StringUtils.isNotEmpty(name)){
						opPatiInfo.setDescDiag(name);
					}
				}
			}
		}
		return opPatiInfo;
		//return cnOpPatiOverviewMapper.qryPatiOpInfo(pkPv);
	}
	
	/**
	 * 住院预约
	 * @param  param
	 * @return user
	 * @throws 
	 */
	public void toIp(String param, IUser user){
		PvIpNotice notice= JsonUtil.readValue(param, new TypeReference<PvIpNotice>(){});
		
		//1.数据校验
		int count = DataBaseHelper.queryForScalar("select count(1) from pv_ip_notice ipn "
				+ "where ipn.pk_pv_op=? and ipn.pk_dept_ip=? and ipn.eu_status<2  ", 
				Integer.class,notice.getPkPvOp(), notice.getPkDeptIp());
		if(count!=0 ){
			//2.更新表pv_ip_notice；
			notice.setPkOrg(UserContext.getUser().getPkOrg());
			notice.setPkInNotice(notice.getPkInNotice());
			notice.setPkPvIp(notice.getPkPvIp());
			notice.setPkPvOp(notice.getPkPvOp());
			notice.setDateValid(notice.getDateValid());
			notice.setPkDeptIp(notice.getPkDeptIp());
			notice.setPkDeptNsIp(notice.getPkDeptNsIp());
			notice.setDtLevelDise(notice.getDtLevelDise());
			notice.setDateAdmit(notice.getDateAdmit());
		    notice.setEuStatus("0");
		    notice.setPkDiagMaj(notice.getPkDiagMaj());
		    notice.setDescDiagMaj(notice.getDescDiagMaj());
		    notice.setDescDiagEls(notice.getDescDiagIp());
		    notice.setNote(notice.getNote());
		    DataBaseHelper.updateBeanByPk(notice, false);
		}else{
		//3.写表pv_ip_notice；
		notice.setPkOrg(UserContext.getUser().getPkOrg());
		notice.setPkInNotice(notice.getPkInNotice());
		notice.setPkPvIp(notice.getPkPvIp());
		notice.setPkPvOp(notice.getPkPvOp());
		notice.setDateValid(notice.getDateValid());
		notice.setPkDeptIp(notice.getPkDeptIp());
		notice.setPkDeptNsIp(notice.getPkDeptNsIp());
		notice.setDtLevelDise(notice.getDtLevelDise());
		notice.setDateAdmit(notice.getDateAdmit());
	    notice.setEuStatus("0");
	    notice.setPkDiagMaj(notice.getPkDiagMaj());
	    notice.setNote(notice.getNote());
	    notice.setDescDiagMaj(notice.getDescDiagMaj());
	    notice.setDescDiagEls(notice.getDescDiagIp());
	    ApplicationUtils.setDefaultValue(notice, true);
	    DataBaseHelper.insertBean(notice);
		}
	}
	
	
	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if(StringUtils.hasText(date)){
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return temp;
	}
	
	public Calendar setDate(String time,long now) {
		String[] timeStr = time.trim().split(":");
		if(timeStr!=null && timeStr.length!=3){
			throw new BusException("午别时间维护错误，请修改！");
		}
		
		int hour = Integer.parseInt(timeStr[0]);
		int min = Integer.parseInt(timeStr[1]);
		int sec = Integer.parseInt(timeStr[2]);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		return cal;
	}
	
	public boolean isInRange(Calendar calBegin,Calendar curr,Calendar calEnd){		
		return curr.after(calBegin) && curr.before(calEnd);
	}
     
	public PiAcc piAccDetailVal(BlDeposit dp){
		
		// 如果患者有授权账户取授权账户，如果没有的取患者账户，再没有返回null
		PiAcc pa = pareAccoutService.queryParentOrPiAccByPkPi(dp.getPkPi());
		
		if(pa!=null&&EnumerateParameter.ONE.equals(pa.getEuStatus())){
			if(pa.getAmtAcc()==null||"".equals(pa.getAmtAcc())){
				pa.setAmtAcc(BigDecimal.ZERO);
			}
			
			// 查询当前用户是否有授权用户，如果有，获取授权用户的账户余额和账户可用余额.没有的话看否有患者账户，没有返回null.有的话用患者自己的余额。
			Map<String, BigDecimal> piAccMap = pareAccoutService.getPatiAccountAvailableBalance(dp.getPkPi());
			BigDecimal accLimit = piAccMap.get("accLimit");
			BigDecimal acc = piAccMap.get("acc");
			
			//考虑(授权账户或者患者账户)的账户余额和信用额度是否够用
			BigDecimal amtAcc= accLimit.multiply(dp.getAmount());
			BigDecimal accAmount = acc.multiply(dp.getAmount());
			if(amtAcc.compareTo(BigDecimal.ZERO)<0){
				throw new BusException("账户余额及信用额度不够.");
			}else{
				pa.setAmtAcc(accAmount);
			}
			//在账户扣费
			DataBaseHelper.updateBeanByPk(pa,false);
			
			PiAccDetail pad=new PiAccDetail();
			pad.setPkPiacc(pa.getPkPiacc());
			pad.setPkPi(pa.getPkPi());
			pad.setPkPv(dp.getPkPv());
			pad.setDateHap(new Date());
			pad.setEuOptype(EnumerateParameter.TWO);
			pad.setEuDirect(dp.getEuDirect());
			pad.setAmount(dp.getAmount());
			pad.setPkDepopi(null);
			pad.setAmtBalance(pa.getAmtAcc());
			pad.setPkEmpOpera(dp.getPkEmpPay());
			pad.setNameEmpOpera(dp.getNameEmpPay());
			DataBaseHelper.insertBean(pad);
		}else{
			throw new BusException("该账户已冻结或已被删除，不可收退款");
		}
			return pa;
		}
	
	private BigDecimal amtTrans(Object amt) {
		if(amt == null){
			return BigDecimal.ZERO;
		}else{
			return (BigDecimal)amt;
		}
	}
	
	/**
	 * 查询患者就诊记录 【交易号】004003004003
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPvVisitingRecordInfo(String param, IUser user) {
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		if(paramMap == null || (CommonUtils.isNull(paramMap.get("codeOp")) 
				&& CommonUtils.isNull(paramMap.get("pkDept"))
				&& CommonUtils.isNull(paramMap.get("dateBegin"))
				&& CommonUtils.isNull(paramMap.get("dateEnd"))
				&& CommonUtils.isNull(paramMap.get("codePv"))
				&& CommonUtils.isNull(paramMap.get("pkEmpPhy"))
				&& CommonUtils.isNull(paramMap.get("namePi")))) {
			throw new BusException("交易号：【004003004003】数据量过大，参数：【codeOp、codePv、namePi、pkDept、dateBegin、dateEnd、pkEmpPhy】不能同时为空！");
		}
		if(paramMap.get("dateEnd") != null){
			paramMap.put("dateEnd", DateUtils.getDateStr(DateUtils.strToDate(paramMap.get("dateEnd")))+"235959");
		}
		User u=(User)user;
		paramMap.put("pkOrg",u.getPkOrg());
		return cnOpPatiOverviewMapper.qryVisitingInfo(paramMap);
	}
	
	/**
	 * 查询门诊就诊费用明细【交易号】004003004004
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPvVisitingRecordCharges(String param, IUser user) {
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		return cnOpPatiOverviewMapper.qryVisitingCharges(paramMap);
	}
	
	/**
	 * 查询门诊就诊诊断记录【交易号】004003004005
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPvVisitingRecordDiag(String param, IUser user) {
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		return cnOpPatiOverviewMapper.qryVisitingDiag(paramMap);
	}
	
	/**
	 * 得到参数值及对应key的SM4加密值【交易号】004003003023
	 * @param param
	 * @param user
	 * @return
	 */
	public String getParamSm4Value(String param, IUser user) {
		//String pkPv = JsonUtil.getFieldValue(param,"pkPv");
		String key = JsonUtil.getFieldValue(param,"key");
		String paramValue = JsonUtil.getFieldValue(param,"paramValue");
    	SM4Utils sm4 = new SM4Utils(key, false);
    	String cipherText = sm4.encryptData_ECB(paramValue);
		return cipherText;
	}
}
