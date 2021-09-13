package com.zebone.nhis.cn.ipdw.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.cn.ipdw.vo.CnIpPressAuth;
import com.zebone.nhis.cn.pub.dao.PatiListQryMapper;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.res.OrgDeptWgEmp;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.service.QueryRemainFeeService;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PatiListQryService {

	@Autowired(required = false)
	private PatiListQryMapper patiListQryDao ;
	@Autowired
	private CnIpPressAuthService authService;
	@Autowired
	private CnPubService cnPubService;
	@Autowired
	private QueryRemainFeeService exPubService;
	
	
	//查询单个病人信息
	public Map<String,Object> querySinglePat(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		Map<String,Object> patInfo = patiListQryDao.querySinglePat(paramMap);
		
		//护士操作转科，目标科室还未接收时，需要通过转科记录下确定病人最新的科室、病区
		String sqlStr="select adt.pk_dept,adt.pk_dept_ns,DEPT.name_dept as deptphyname,deptns.name_dept as deptnsname from pv_adt adt LEFT OUTER JOIN bd_ou_dept dept " +
				"on adt.pk_dept=DEPT.pk_dept and DEPT.del_flag='0' LEFT OUTER JOIN bd_ou_dept deptns " +
				"on adt.pk_dept_ns=deptns.pk_dept and deptns.del_flag='0' where adt.del_flag='0' and adt.DATE_END is null " +
				"and adt.pk_pv=:pkPv order by date_begin desc ";
		List<Map<String,Object>> pvAdtList = DataBaseHelper.queryForList(sqlStr, paramMap);
		if(pvAdtList!=null && pvAdtList.size()>0){
			Map<String,Object> pvAdtMap=pvAdtList.get(0);
			patInfo.put("pk_dept", pvAdtMap.get("pkDept"));
			patInfo.put("pk_dept_ns", pvAdtMap.get("pkDeptNs"));
			patInfo.put("deptphyname", pvAdtMap.get("deptphyname"));
			patInfo.put("deptnsname", pvAdtMap.get("deptnsname"));
		}
		
		return patInfo;
	}

	public List<Map<String,Object>> queryPatiList(String param , IUser user) {
		
		String patiTypeCode = JSON.parseObject(param).getString("patiTypeCode");
		if(StringUtils.isEmpty(patiTypeCode)) 	throw new BusException("前台传的患者分类编码(patiTypeCode)为空!");
		Map<String,Object> map = new HashMap<String,Object>();
		User loginUser = (User) user ;
		String login_pk_dept = loginUser.getPkDept();
		map.put("login_pk_dept", login_pk_dept);
		map.put("loginPkEmp", loginUser.getPkEmp());
		if("ip_labor".equals(patiTypeCode)){
			map.put("bedno", JSON.parseObject(param).getString("bedno"));
			List<Map<String,Object>> laborList =  patiListQryDao.queryLaborPati(map);
			if(laborList!=null&&laborList.size()>0){
				for(Map<String,Object> maplabor :laborList){
					putDtEmpsrvtype(maplabor, loginUser);
				}
			}
			return laborList;
		}
		List<Map<String,Object>> rtnListMap = new ArrayList<Map<String,Object>>();
		map.put("patiTypeCode", patiTypeCode);
		if("ip_transf".equals(patiTypeCode)){ //转科患者
			rtnListMap = patiListQryDao.queryTransPatiList(map);
		}else if("ip_consult".equals(patiTypeCode)){ //会诊患者
			map.put("dbType", MultiDataSource.getCurDbType());
			map.put("pkEmp",((User) user).getPkEmp());
			rtnListMap = patiListQryDao.queryConsultPatiList(map);
		}else if("ip_leave".equals(patiTypeCode)){
			String leave_date_begin = JSON.parseObject(param).getString("leave_date_begin");
			String leave_date_end = JSON.parseObject(param).getString("leave_date_end");
			map.put("leave_date_begin", leave_date_begin);
			map.put("leave_date_end", leave_date_end);
			rtnListMap = patiListQryDao.qryLeavePatiList(map);
		}else if("ip_dept_ns".equals(patiTypeCode)){ //病区患者
			map.put("bedno", JSON.parseObject(param).getString("bedno"));
			//查询当前科室诊区
			String sql="select bus.pk_dept from bd_dept_bus bus " +
					"inner join  bd_dept_bus bu on bus.pk_deptbu=bu.pk_deptbu and bu.dt_depttype='01' " +
					"where bus.dt_depttype='02' and bu.pk_dept='"+login_pk_dept+"'";
			List<Map<String,Object>> list=DataBaseHelper.queryForList(sql);
			if(list!=null && list.size()>0){
				map.put("pkDeptNsS",list);
				rtnListMap = patiListQryDao.queryDeptNsPatiList(map);
			}

		}else{ //科室患者
			map.put("login_pk_psn", loginUser.getPkEmp());
		    if("ip_wg".equals(patiTypeCode)) {
				List<String> pkWgList = new ArrayList<String>();
				List<OrgDeptWgEmp> pkWgListRtn  = DataBaseHelper.queryForList("select pk_wg from org_dept_wg_emp where del_flag='0' and pk_emp =?", OrgDeptWgEmp.class, new Object[]{loginUser.getPkEmp()});
				for(OrgDeptWgEmp wg : pkWgListRtn){
					pkWgList.add(wg.getPkWg());
				}
				if(pkWgList.size()==0) return rtnListMap;
				map.put("pkWgList", pkWgList);
			}else if("ip_bed".endsWith(patiTypeCode)){
				map.put("bedno", JSON.parseObject(param).getString("bedno"));
			}else if("ip_med".equals(patiTypeCode)){
				map.put("codeIp", JSON.parseObject(param).getString("bedno"));
			}
			rtnListMap = patiListQryDao.queryDeptPatiList(map);
		}
		return rtnListMap;
	}

	/**
	 * 根据患者住院号或者名字查询诊断信息vo列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map> getPibaseVoByPi(String param, IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		String pkOrg = UserContext.getUser().getPkOrg();
		if (MapUtils.getString(map,"dateClinic")!=null){
			 map.put("dateClinic", DateUtils.strToDate(MapUtils.getString(map, "dateClinic"), "yyyyMMddHHssmm"));
		}
		map.put("pkOrg",pkOrg);
		List<Map> voList = patiListQryDao.getPibaseVoByPi(map);
		return voList;
	}
	
	//查询在院患者床位信息列表：004004001019
	public List<Map<String,Object>> queryPatiBedList(String param , IUser user) {
		
		String patiTypeCode = JsonUtil.getFieldValue(param, "patiTypeCode");
		/*
		 * 我的患者：ip_my
		 * 医疗组患者:ip_wg
		 * 科室患者:ip_dept
		 */
		List<Map<String,Object>> rtnListMap = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		User loginUser = (User) user ;
		map.put("login_pk_dept", loginUser.getPkDept());
		map.put("patiTypeCode", patiTypeCode);
		map.put("login_pk_psn", loginUser.getPkEmp());
		Boolean flagNs = "1".equals(JsonUtil.getFieldValue(param, "flagNs"));

		if(flagNs){
			if("ip_dept_ns".equals(patiTypeCode)){ //病区患者
				//查询当前科室诊区
				String sql="select bus.pk_dept from bd_dept_bus bus " +
						"inner join  bd_dept_bus bu on bus.pk_deptbu=bu.pk_deptbu and bu.dt_depttype='01' " +
						"where bus.dt_depttype='02' and bu.pk_dept='"+loginUser.getPkDept()+"'";
				List<Map<String,Object>> list=DataBaseHelper.queryForList(sql);
				if(list!=null && list.size()>0){
					map.put("pkDeptNsS",list);
				}
			}else if("ip_wg".equals(patiTypeCode)) { //医疗组患者
					List<String> pkWgList = new ArrayList<String>();
					List<OrgDeptWgEmp> pkWgListRtn  = DataBaseHelper.queryForList("select pk_wg from org_dept_wg_emp where del_flag='0' and pk_emp =?", OrgDeptWgEmp.class, new Object[]{loginUser.getPkEmp()});
					for(OrgDeptWgEmp wg : pkWgListRtn){
						pkWgList.add(wg.getPkWg());
					}
					if(pkWgList.size()==0) return rtnListMap;
					map.put("pkWgList", pkWgList);
			}
			rtnListMap = patiListQryDao.queryPatiBedNsList(map);
		}else{
			//医疗组患者
			if("ip_wg".equals(patiTypeCode)) {
				List<String> pkWgList = new ArrayList<String>();
				List<OrgDeptWgEmp> pkWgListRtn  = DataBaseHelper.queryForList("select pk_wg from org_dept_wg_emp where del_flag='0' and pk_emp =?", OrgDeptWgEmp.class, new Object[]{loginUser.getPkEmp()});
				for(OrgDeptWgEmp wg : pkWgListRtn){
					pkWgList.add(wg.getPkWg());
				}
				if(pkWgList.size()==0) return rtnListMap;
				map.put("pkWgList", pkWgList);
			}

			rtnListMap = patiListQryDao.queryPatiBedList(map);
		}

		return rtnListMap;
	}
	
   public List<Map<String,Object>> qryLeavePatiList(String param , IUser user) {

		Map<String,Object> map = new HashMap<String,Object>();
		User loginUser = (User) user ;
		String login_pk_dept = loginUser.getPkDept();
		//map.put("login_pk_dept", login_pk_dept);
		map.put("login_pk_psn", loginUser.getPkEmp());
		String leave_date_begin = JSON.parseObject(param).getString("begin_date");
		String leave_date_end = JSON.parseObject(param).getString("end_date");
		String patiname = JSON.parseObject(param).getString("patiname");
		String pvcode = JSON.parseObject(param).getString("pvcode");
		String paticode = JSON.parseObject(param).getString("paticode");
		String pkDept = JSON.parseObject(param).getString("pkDept");
		String pkEmpPhy = JSON.parseObject(param).getString("pkEmpPhy");
		String pkDeptOut = JSON.parseObject(param).getString("pkDeptOut");
		map.put("pkDept", pkDept);
	   map.put("pkDeptOut", pkDeptOut);
		map.put("pkEmpPhy", pkEmpPhy);
		map.put("leave_date_begin", leave_date_begin);
		map.put("leave_date_end", leave_date_end);
		map.put("patiname", patiname);
		map.put("pvcode", pvcode);
		map.put("paticode", paticode);
		List<Map<String,Object>> rtnListMap = new ArrayList<Map<String,Object>>();
		rtnListMap = patiListQryDao.qryLeavePatiList(map);
		return rtnListMap;
	}
	
	public Map<String,Object> queryDeptPatiNum(String param , IUser user){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> condiMap = new HashMap<String,Object>();
		User loginUser = (User) user ;
		String pk_dept = loginUser.getPkDept();
		String pk_org = loginUser.getPkOrg();
		String dateTomorrow = null;
		DateTime dt = new DateTime();
		String date = dt.toString("yyyy-MM-dd HH:mm:ss").substring(0, 10);
		condiMap.put("date_begin", date+" 00:00:00");
		condiMap.put("date_end", date+" 23:59:59");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date2 = sdf.parse(date);
			Date date3 = addDate(date2, 1);
			dateTomorrow = sdf.format(date3).substring(0, 10);
		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		condiMap.put("date_tomorrow_begin", dateTomorrow+" 00:00:00");
		condiMap.put("date_tomorrow_end", dateTomorrow+" 23:59:59");
		condiMap.put("pk_dept", pk_dept);
		condiMap.put("pk_org", pk_org);
		int CountTotal = patiListQryDao.qryDeptPatiTotal(pk_dept);
		int CountToDay = patiListQryDao.qryDeptOpToday(condiMap);
		int CountTomorrow = patiListQryDao.qryDeptOpTomorrow(condiMap);
		int CountIn = patiListQryDao.qryDeptPatiInNum(condiMap);
		int CountOut = patiListQryDao.qryDeptPatiOutNum(condiMap);
		int CountTranIn = patiListQryDao.qryDeptPatiTransInNum(condiMap);
		int CountTranOut = patiListQryDao.queryDeptPatiTransOutNum(condiMap);
		int CountDisease = patiListQryDao.queryDeptPatiDiseaseNum(condiMap);
		int CountDanger = patiListQryDao.queryDeptPatiDangerNum(condiMap);
		int CountCpRec = patiListQryDao.queryDeptCpRecNum(condiMap);
		map.put("CountTotal", CountTotal);
		map.put("CountToDay", CountToDay);
		map.put("CountTomorrow", CountTomorrow);
		map.put("CountIn", CountIn);
		map.put("CountOut", CountOut);
		map.put("CountTranIn", CountTranIn);
		map.put("CountTranOut", CountTranOut);
		map.put("CountDisease", CountDisease);
		map.put("CountDanger", CountDanger);
		map.put("CountCpRec",CountCpRec);
		putDtEmpsrvtype(map, loginUser);
		return map;
	}
	public static Date addDate(Date date,long day) throws ParseException{
		 long time = date.getTime();
		 day = day*24*60*60*1000; 
		 time+=day;
		 return new Date(time);
	 }

	public Map<String,Object> qryPiCgAmount(String param , IUser user){
		Map<String,Object> m = JsonUtil.readValue(param, Map.class);
		String pkPv = m.get("pkPv").toString();
		if(StringUtils.isBlank(pkPv)) return null;
		Map<String,Object> rtnM = new HashMap<String,Object>();
		//病种限额
		setDiagAmout(m, rtnM);
		//过敏史
		List<Map<String,Object>> piAl = DataBaseHelper.queryForList("select name_al,note from pi_allergic where  pk_pi=? and del_flag='0'", new Object[]{m.get("pkPi")});
		if(piAl!=null&&piAl.size()>=0){
			String strPiAl="";
			String strNote="";
			for(Map<String,Object> al : piAl){
				if(al!=null){
					 if (m.get("strAllergic") != null)
					 {
						if("True".equals(m.get("strAllergic").toString()) )
						{
							strPiAl += al.get("nameAl")!=null ?(al.get("nameAl").toString()):"";
							
							if(al.get("nameAl")!=null && al.get("note")!=null )
							{
								strPiAl += al.get("note").toString();
							}
							strPiAl += al.get("nameAl")!=null ?",":"";
						}
	
						else
						    strPiAl+=al.get("nameAl")!=null ?al.get("nameAl").toString()+",":"";
					 }
					 else
					 {
						 strPiAl+=al.get("nameAl")!=null ?al.get("nameAl").toString()+",":"";
					 }

				}
			}
			if(StringUtils.isNotBlank(strPiAl)){
				strPiAl=strPiAl.substring(0, strPiAl.length()-1);
				rtnM.put("PiNameAl",strPiAl);
			}
		}

		//出院带药处方张数最大值
		Map<String, Object> attrMap = DataBaseHelper.queryForMap("select nvl(max(attr.val_attr),0) as attr  from bd_hp hp inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict  inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp where tmp.code_attr='0305' and  hp.pk_hp=? ", new Object[]{m.get("pkHp")});
		rtnM.put("MaxSheetMedoutPres",attrMap.get("attr").toString());
		//出院带药处方用药天数最大值
		attrMap = DataBaseHelper.queryForMap("select nvl(max(attr.val_attr),0) as attr  from bd_hp hp inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict  inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp where tmp.code_attr='0304' and  hp.pk_hp=? ", new Object[]{m.get("pkHp")});
		rtnM.put("MaxDayMedoutPres",attrMap.get("attr").toString());
		//参照医保目录
		attrMap = DataBaseHelper.queryForMap("select nvl(max(attr.val_attr),'00')as attr  from bd_hp hp inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict  inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp where tmp.code_attr='0318' and  hp.pk_hp=? ", new Object[]{m.get("pkHp")});
		rtnM.put("HpRefDirectory",attrMap.get("attr").toString());
		//医保计划拓展属性【0329】设置的比例
		attrMap = DataBaseHelper.queryForMap("select nvl(max(attr.val_attr),'0')as attr  from bd_hp hp inner join bd_dictattr attr on hp.pk_hp=attr.pk_dict  inner join bd_dictattr_temp tmp on attr.pk_dictattrtemp=tmp.pk_dictattrtemp where tmp.code_attr='0329' and  hp.pk_hp=? ", new Object[]{m.get("pkHp")});
		rtnM.put("HpProportion",attrMap.get("attr").toString());
		//BL0004--住院医生开立医嘱时是否控制欠费（0不控制，1控制,2提示欠费信息）

		//BL0038--是否使用医保预测费用计算
		if("0".equals(ApplicationUtils.getSysparam("BL0038", false))){
			List<Map<String,Object>> piCg = patiListQryDao.qryPiAmount(m);
			if(piCg!=null && piCg.size()>0){
				Map<String,Object> piCgM = piCg.get(0);
				if(piCgM!=null){
					//总费用
					rtnM.put("Amount", piCgM.get("amt")!=null ?piCgM.get("amt").toString():"0");
					//医保预测费用
					rtnM.put("AmtHp", piCgM.get("amtHp")!=null ?piCgM.get("amtHp").toString():"0");
				}
			}
			//未结费用
			Map<String, Object> amtNoSettle= DataBaseHelper.queryForMap("select sum(amount) amt from bl_ip_dt where flag_settle='0' and del_flag='0' and pk_pv=?",pkPv);
			if (amtNoSettle != null) {
				rtnM.put("AmtNosettle", amtNoSettle.get("amt")!=null ?amtNoSettle.get("amt").toString():"0");
			}
			//是否欠费
			String IsArrearage=ApplicationUtils.getSysparam("BL0004", false);
			if("1".equals(IsArrearage)){
				rtnM.put("IsArrearage", !exPubService.isArrearage(pkPv,"",BigDecimal.ZERO));
			}else{
				rtnM.put("IsArrearage", false);
			}

			//预交金
			Map<String, Object> amtPrepmap = DataBaseHelper.queryForMap("select sum(amount) amt from bl_deposit where eu_dptype='9' and flag_settle='0' and pk_pv=?",pkPv);
			if (amtPrepmap != null) {
				rtnM.put("AmtPrep", amtPrepmap.get("amt")!=null ?amtPrepmap.get("amt").toString():"0");
			}
			
			//绿色担保金
			Map<String, Object> amtCredit = DataBaseHelper.queryForMap("select sum(amt_credit) amt from PV_IP_ACC where  flag_canc='0' and del_flag='0' and pk_pv=?",pkPv);
			if (amtCredit != null) {		
				rtnM.put("amtIpAcc", amtCredit.get("amt")!=null ?amtCredit.get("amt").toString():"0"); 
			}

			//药品费用
			Map<String, Object> pdmap = DataBaseHelper.queryForMap("select sum(dt.amount) amt from bl_ip_dt dt,bd_audit ba where dt.code_audit=ba.code and dt.del_flag='0' and ba.code in ('94','95','96','97') and nvl(dt.flag_settle,'0')='0' and pk_pv=?",pkPv);
			if (amtPrepmap != null) {
				rtnM.put("AmtPd", pdmap.get("amt")!=null ?pdmap.get("amt").toString():"0");
			}

			//可用余额=预交金+医保预测+绿色担保金-未结费用
			Double dAmtPrep = CommonUtils.getDouble(rtnM.get("AmtPrep"));
			Double dAmtHp =CommonUtils.getDouble(rtnM.get("AmtHp"));
			Double dAmtCredit =CommonUtils.getDouble(rtnM.get("amtIpAcc"));
			Double dAmtNosettle =CommonUtils.getDouble(rtnM.get("AmtNosettle"));
			Double dAmtAcc1 =   MathUtils.sub(MathUtils.add(MathUtils.add(dAmtPrep, dAmtHp),dAmtCredit),dAmtNosettle);
			Double dAmtAcc =   MathUtils.sub(MathUtils.add(dAmtPrep, dAmtHp),dAmtNosettle);
			rtnM.put("AmtAcc", dAmtAcc.toString());//可用余额--界面上可用余额不变
			
		}else{
			ServiceLocator.getInstance().getBean(this.getClass()).queryExtCost(rtnM,param,user);
		}

		return rtnM;
	}

	public void queryExtCost(Map<String,Object> rtnM,String param , IUser user){
		DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
		transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_NOT_SUPPORTED);
		DataSourceTransactionManager tm = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
		TransactionStatus transStatus = tm.getTransaction(transDefinition);
		try{
			ExtSystemProcessUtils.processExtMethod("medicalInsurance", "qryCostForecast", param, user,rtnM);
			tm.commit(transStatus);
		}catch(Exception e){
			tm.rollback(transStatus);
			rtnM.put("IsArrearage", false);
		}
	}
	private void setDiagAmout(Map<String, Object> m, Map<String, Object> rtnM) {
		//String type = ApplicationUtils.getSysparam("CN0027", false); @todo取消系统参数，走优先级，单病种优先
		Double TreatwayAmt =0.0;
		List<Map<String,Object>> diagDivCgT = DataBaseHelper.queryForList("select div.amount amt_total,cate.pk_itemcate,cate.rate,div.amount*cate.rate amt_itemcate from bd_hp_diagdiv div "+
                " inner join bd_hp_diagdiv_itemcate cate on div.pk_totaldiv=cate.pk_totaldiv and cate.del_flag='0'  where div.pk_hp=? AND div.pk_diag=? and div.del_flag='0' ",new Object[]{m.get("pkHp")!=null?m.get("pkHp").toString():"",m.get("pkDiag")!=null?m.get("pkDiag").toString():""} );
        if(diagDivCgT!=null && diagDivCgT.size()>0){
                 Map<String,Object> diagDivCgM = diagDivCgT.get(0);
                 if(diagDivCgM!=null){
                	 TreatwayAmt =  Double.parseDouble(diagDivCgM.get("amtTotal").toString());
                 }
        }
        if(TreatwayAmt==0){ //分值次之
        	List<Map<String,Object>> piCgT =patiListQryDao.qryPiTreatWayAmount(m);
        	if(piCgT!=null && piCgT.size()>0){
        		Map<String,Object> piCgM = piCgT.get(0);
        		if(piCgM!=null){ //病种限额系数已被改造至疾病诊治字典中，此处不在判断参数
        			//String valxs = ApplicationUtils.getSysparam("CN0016", false);
        			//if(StringUtils.isBlank(valxs)) throw new BusException("请维护当前机构【病种限额系数】参数（CN0016）！");
        			if(piCgM.get("val")!=null) {
        				TreatwayAmt = MathUtils.mul(Double.parseDouble(piCgM.get("val").toString()),Double.parseDouble("0"));
        			}
        		}
        	}
        }
		rtnM.put("TreatwayAmt", TreatwayAmt);
	}
	private void putDtEmpsrvtype(Map<String, Object> map, User loginUser) {
		String sql = "select dt_empsrvtype  from bd_ou_employee  where pk_emp=?";
		Map<String,Object> emp = DataBaseHelper.queryForMap(sql, new Object[]{loginUser.getPkEmp()});
		if(emp==null) throw new BusException("当前用户未关联人员！");
		Object dtEmpsrvtype = emp.get("dtEmpsrvtype");
		if(dtEmpsrvtype==null || StringUtils.isBlank(dtEmpsrvtype.toString())) throw new BusException("请维护人员的医疗项目权限!否则无法正常工作!");
		map.put("DtEmpsrvtype", dtEmpsrvtype);
	}
	public Map<String,Object> qryStoreByDept(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String dtButype = (String) JsonUtil.readValue(param, Map.class).get("dtButype");
		//标志调用此方法是否是手术管理 true：是 false：否
		String isOperation = (String) JsonUtil.readValue(param, Map.class).get("isOperation");
		if(dtButype.length()>16){ 
			return patiChangeQry(dtButype);
		}
		return loginUserQry(user, dtButype,isOperation);
	}

	private Map<String, Object> loginUserQry(IUser user, String dtButype,String isOperation)
			throws IllegalAccessException, InvocationTargetException {
		User u = (User) user;
		Map<String,Object> map = new HashMap<String,Object>(); 
		map.put("dtButype", dtButype);
		if("01".equals(dtButype)){
			map.put("deptType", "02");
			map.put("deptTypeName", "护理单元");
		}else{
			map.put("deptType", "0402");
			map.put("deptTypeName", "西药房");
		}
		
		map.put("pkDept", u.getPkDept());
		
		Map<String,Object> mapRtn = new HashMap<String,Object>();
		
		/**如果isOperation为true则不调用科室与药房业务线*/
		if(CommonUtils.isEmptyString(isOperation) ||
				!BlcgUtil.converToTrueOrFalse(isOperation)){
			
			List<Map<String,Object>> rtn = (List<Map<String, Object>>) patiListQryDao.qryStoreByDept(map);
			if(rtn.size()!=1) {
				Map<String,Object> deptMap =DataBaseHelper.queryForMap("select name_dept from bd_ou_dept where pk_dept=?", ((User)user).getPkDept());
				throw new BusException("请维护好临床科室["+deptMap.get("nameDept")+"]与"+map.get("deptDtypeName")+"的业务线！");
			}
			mapRtn = rtn.get(0);
			
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("deptType", "0402");
			map2.put("dtButype", "09");
			map2.put("pkDept",u.getPkDept());
			List<Map<String,Object>> rtn2 = (List<Map<String, Object>>) patiListQryDao.qryStoreByDept(map2);
			if(rtn2.size()!=1) {
				Map<String,Object> deptMap =DataBaseHelper.queryForMap("select name_dept from bd_ou_dept where pk_dept=?", ((User)user).getPkDept());
				mapRtn.put("DeptAlreadyException","请维护好临床科室["+deptMap.get("nameDept")+"]与成药房的业务线！");
			}else{
				
		       Map<String,Object> ret = rtn2.get(0);
		       mapRtn.put("pkDeptAlready", ret.get("pkDept"));
		       mapRtn.put("pkOrgAlready", ret.get("pkOrg"));
			}
		}
		
	    CnIpPressAuth auth = authService.getDoctorAuth(user);
	    if(auth==null) throw new BusException("请维护好用户["+((User)user).getNameEmp()+"]的医疗处方权限！");
	    mapRtn.put("FlagPres", auth.isFlagPres()?"1":"0");
	    mapRtn.put("FlagAnes", auth.isFlagAnes()?"1":"0");
	    mapRtn.put("FlagSpirOne", auth.isFlagSpirOne()?"1":"0");
	    mapRtn.put("FlagSpirSec", auth.isFlagSpirSec()?"1":"0");
	    mapRtn.put("FlagPoi", auth.isFlagPoi()?"1":"0");
	    mapRtn.put("EuDrtype", auth.getEuDrtype()==null?"":auth.getEuDrtype());//解决医师类别为null报错问题
	    mapRtn.put("DtEmpsrvtype", auth.getDtEmpsrvtype()==null?"":auth.getDtEmpsrvtype());
	    mapRtn.put("AntiCode", auth.getAntiCode()==null?"":auth.getAntiCode());
	    String s = cnPubService.getDeptArguvalBool(u.getPkDept(), "CN0015","0"); //0为不启用1为启用
	    mapRtn.put("IsUsePivas", s);
		String useLastNum = "1";
	    mapRtn.put("UseLastNum", useLastNum);
		String longDefCodeFreq = ApplicationUtils.getSysparam("CN0018", false);
		if(StringUtils.isBlank(longDefCodeFreq)) {
			//longDefCodeFreq="qd";
			throw new BusException("频次:"+longDefCodeFreq+"在频次字典中没有维护！");
		}
		String defCntSql = "select cnt from bd_term_freq where code=? and del_flag='0' " ;
		List<BdTermFreq> cnts = DataBaseHelper.queryForList(defCntSql, BdTermFreq.class, new Object[]{longDefCodeFreq});
		if(cnts==null ||cnts.size()==0) throw new BusException("频次:"+longDefCodeFreq+"在频次字典中未维护！");
		if(cnts.size()>1) throw new BusException("频次:"+longDefCodeFreq+"在频次字典中维护重复！");
		Long defCntM = cnts.get(0).getCnt();
	    String onceDefCodeFreq = ApplicationUtils.getSysparam("CN0019", false);
	    if(StringUtils.isBlank(onceDefCodeFreq)) {
	    	//onceDefCodeFreq="once";
	    	throw new BusException("频次:"+onceDefCodeFreq+"在频次字典中没有维护！");
	    }
	    String euUsecate = ApplicationUtils.getSysparam("SCM0003", false);
	    mapRtn.put("EuUsecate", euUsecate==null?"":euUsecate.toString());
	    mapRtn.put("LongDefCodeFreq", longDefCodeFreq);
	    mapRtn.put("DefCnt", defCntM);
	    mapRtn.put("OnceDefCodeFreq", onceDefCodeFreq);
	    String empPdSql = "SELECT emp.PK_EMP,store.PK_PD FROM BD_PD_STORE_EMP emp INNER JOIN BD_PD_STORE store ON store.PK_PDSTORE = emp.PK_PDSTORE AND store.DEL_FLAG='0' WHERE emp.DEL_FLAG='0' and emp.pk_emp!=? ";
	    List<Map<String,Object>> empPdlist = DataBaseHelper.queryForList(empPdSql, new Object[]{u.getPkEmp()});
	    mapRtn.put("EmpPD", (empPdlist==null ||empPdlist.size()==0)?"":empPdlist);
	    
	    try{
			ExtSystemProcessUtils.processExtMethod("InsSzybCity", "loginUserQry", user,mapRtn);
		}catch(Exception e){
		}
	    
	    return mapRtn;
	}
	private Map<String, Object> patiChangeQry(String dtButype) {
		//兼顾产房
		int labor = DataBaseHelper.queryForScalar("select count(1) from pv_encounter pv "+
					 " inner join pv_labor lab on lab.pk_pv = pv.pk_pv and lab.eu_status = '1' and lab.flag_in = '1' and lab.del_flag='0'"+
					 " where  pv.pk_pv = ? and pv.flag_cancel != '1'  and pv.eu_pvtype = '3'  and pv.eu_status > 0 and pv.flag_in ='1' and pv.del_flag='0' ", Integer.class, dtButype);
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("isLabor", labor>0?"1":"0"); //是否在产房
		rtnMap.put("isCpRec", "0"); //默认无临床路径
		//查询临床路径
		Map<String,Object> recMap =DataBaseHelper.queryForMap("select date_start ,pk_cprec  from cp_rec where pk_pv=? and del_flag='0' and eu_status='0' and eu_rectype='1'", dtButype);
		if(recMap!=null) { //有主路径
			recMap.put("dateStart", recMap.get("dateStart").toString().substring(0, 19));
			rtnMap.putAll(recMap);
			rtnMap.put("isCpRec", "1"); //有临床路径
			return rtnMap;
		}
		List<Map<String,Object>> recList=DataBaseHelper.queryForList("select date_start ,pk_cprec  from cp_rec where pk_pv=? and del_flag='0' and eu_status='0' order by date_start ", dtButype);
		if(recList!=null && recList.size()>0) {
			Map<String,Object> map = recList.get(0);
			map.put("dateStart", map.get("dateStart").toString().substring(0, 19));
			rtnMap.putAll(map);
			rtnMap.put("isCpRec", "1"); //有临床路径
			return rtnMap;
		}
		return rtnMap;
	}
	/**
	 * 患者信息查询---修改查询
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> patiInfor(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkPv")==null) {
			throw new BusException("请传入患者就诊主键！");
		}

		Map<String,Object> ret=patiListQryDao.queryPati(paramMap);

		String YBEdit= MapUtils.getString(paramMap,"edityb");
		if("1".equals(YBEdit) && StringUtils.isNotEmpty(MapUtils.getString(ret,"pkPv"))){
			List<Map> ybMap=patiListQryDao.searchPvDeti(paramMap);
			if(ybMap!=null && ybMap.size()>0){
				ret.putAll(ybMap.get(0));
			}
		}
		return ret;
	}
	
	public void PatiSave(String param , IUser user){
		PiMaster pait = JsonUtil.readValue(param, PiMaster.class);
		PvEncounter pven = JsonUtil.readValue(param, PvEncounter.class);
		PvIp pvip = JsonUtil.readValue(param, PvIp.class);
		User user1=(User)user;
		if (pven.getPkPv() == null) {
			throw new BusException("请传入患者就诊主键！");
		}
		if (pait.getPkPi() == null) {
			throw new BusException("请传入患者主键！");
		}
		//更新患者信息
		String editPiSql = " update pi_master set dt_idtype=:dtIdtype,id_no = :idNo,name_pi = :namePi,dt_sex=:dtSex,birth_date=:birthDate,mobile = :mobile," +
				" addrcode_cur=:addrcodeCur,addr_cur=:addrCur,name_rel=:nameRel,dt_ralation=:dtRalation,tel_rel=:telRel,addr_rel=:addrRel,modifier=:modifier," +
				" dt_marry=:dtMarry,tel_no=:telNo,tel_work=:telWork," +
				" addr_cur_dt=:addrCurDt," +
				" postcode_cur=:postcodeCur,dt_idtype_rel=:dtIdtypeRel,idno_rel=:idnoRel where pk_pi=:pkPi";

		DataBaseHelper.update(editPiSql, pait);
		//更新患者就诊信息
		String editPvSql = " update pv_encounter set name_pi = :namePi,dt_sex=:dtSex,addrcode_cur=:addrcodeCur,addr_cur=:addrCur,addr_cur_dt=:addrCurDt,name_rel=:nameRel," +
						   " dt_ralation=:dtRalation,tel_rel=:telRel,addr_rel=:addrRel,height=:height,weight=:weight,dt_spcdtype=:dtSpcdtype,modifier=:modifier,dt_marry=:dtMarry," +
						   " postcode_regi=:postcodeRegi,postcode_cur=:postcodeCur where pk_pv=:pkPv" ;
		DataBaseHelper.update(editPvSql, pven);
		//更新患者住院信息
		DataBaseHelper.update("update pv_ip set dt_level_dise=:dtLevelDise,modifier=:modifier where pk_pv = :pkPv ", pvip);

		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		String YBEdit= MapUtils.getString(map,"edityb");
		if("1".equals(YBEdit)) {
			InsQgybPV ins = JsonUtil.readValue(param, InsQgybPV.class);
			//医保信息
			if (org.apache.commons.lang3.StringUtils.isBlank(ins.getPkInspv())) { //新增
				ins.setPkOrg(user1.getPkOrg());
				ins.setPkPi(pven.getPkPi());
				ins.setPkPv(pven.getPkPv());
				ins.setPkHp(pven.getPkInsu());
				ins.setDelFlag("0");
				ins.setCreator(user1.getPkEmp());
				ins.setCreateTime(new Date());
				ins.setTs(new Date());
				DataBaseHelper.insertBean(ins);
			} else {
				DataBaseHelper.update(" update ins_qgyb_pv set med_type=:medType,dise_codg=:diseCodg,dise_name=:diseName,birctrl_type=:birctrlType,birctrl_matn_date=:birctrlMatnDate,matn_type=:matnType,geso_val=:gesoVal where pk_pv=:pkPv", ins);
			}
		}
		// 发送患者就诊信息至平台
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkPv",pven.getPkPv());
		paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
		paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
		paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		paramMap.put("STATUS", "_UPDATE");
		PlatFormSendUtils.sendPvInfoMsg(paramMap);
	}

	/**
	 * 手术科室查询默认药房  004004002018
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Map<String, Object>  qryStoreByDeptNew(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("dtButype")==null)
			throw new BusException("请传入业务线类型！");
		User u = (User) user;
		String dtButype=map.get("dtButype").toString();
		map.put("dtButype", dtButype);
		map.put("deptType", "0402");
		map.put("pkDept", u.getPkDept());
		List<Map<String, Object>> rtn = (List<Map<String, Object>>) patiListQryDao.qryStoreByDept(map);
		if(CollectionUtils.isEmpty(rtn))
			throw new BusException("请维护好当前科室业务线！");
		return rtn.get(0);
	}

	/**
	 * 根据科室查询业务线
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryDeptLine(String param,IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null || map.get("pkDept")==null)
			throw new BusException("请传入对应科室！");
		map.put("deptType", "0402");
		List<Map<String,Object>> rtn = (List<Map<String, Object>>) patiListQryDao.qryStoreByDept(map);
		return rtn;
	}
}
