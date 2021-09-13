package com.zebone.nhis.cn.pub.service;

import com.google.common.collect.Lists;
import com.zebone.nhis.cn.ipdw.vo.OrderParam;
import com.zebone.nhis.cn.opdw.vo.PvEncounterVo;
import com.zebone.nhis.cn.pub.dao.CnPubMapper;
import com.zebone.nhis.cn.pub.vo.BdDefdocVo;
import com.zebone.nhis.cn.pub.vo.CnOrderPrintVo;
import com.zebone.nhis.cn.pub.vo.CnParamDict;
import com.zebone.nhis.cn.pub.vo.PdStrockVo;
import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.cp.CpRecExpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.cn.ipdw.CpRecExpParam;
import com.zebone.nhis.common.module.cn.ipdw.PvDiagDt;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvAdt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.service.MessageService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MessageUtils;
import com.zebone.nhis.ma.inv.vo.NewNoReParam;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CnPubService {
	@Autowired
	private MessageService msgService;
	@Autowired
	private CnPubMapper cnPubMapper;

	private static String foodType="13";
	/**
	 * 作废医嘱
	 * @param cnOrds
	 * @param user
	 */
	public void cancleOrder(List<CnOrder> cnOrds,IUser user) {
		if(cnOrds==null ||cnOrds.size()<=0) return ;
		User u = user==null?UserContext.getUser():(User)user ;
		List<String> pkOrds = new ArrayList<String>();
		List<Integer> ordsnPs = new ArrayList<Integer>();
		List<CnOrder> FoodOrds = new ArrayList<>();//饮食医嘱

		//作废医嘱是否需要护士核对(只处理作废标志、作废人、作废时间)
		String ifNeedNurseChk = ApplicationUtils.getSysparam("CN0028", false);
		if(ifNeedNurseChk==null) ifNeedNurseChk="0";
		if(ifNeedNurseChk.equals("1")){
			List<CnOrder> revokeList=new ArrayList<CnOrder>();
			List<CnOrder> updateList=new ArrayList<CnOrder>();
			Date d = new Date();
			for(CnOrder order: cnOrds){

				order.setFlagErase("1");
				order.setDateErase(d);
				order.setPkEmpErase(u.getPkEmp());
				order.setNameEmpErase(u.getNameEmp());
				order.setTs(order.getDateErase());

				if(!order.getEuStatusOrd().equals("0")&&!order.getEuStatusOrd().equals("1")){
					order.setEuStatusOrd("9");
					revokeList.add(order);
				}else{
					updateList.add(order);
				}

			}
			if(updateList.size()>0){
				//更新标记
				DataBaseHelper.batchUpdate("update cn_order set flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
	                                      + "where pk_cnord =:pkCnord and eu_status_ord in ('0','1') ", updateList);
			}
			if(revokeList.size()>0){
			    //作废医嘱
				DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",revokeList);
				DataBaseHelper. batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
						                  + "where pk_cnord =:pkCnord and eu_status_ord not in ('0','1') ", revokeList);
			}
			return;
		}

		Date d = new Date();
		for(CnOrder order: cnOrds){
			pkOrds.add(order.getPkCnord());
			if(foodType.equals(order.getCodeOrdtype()) && ("2".equals(order.getEuStatusOrd()) || "3".equals(order.getEuStatusOrd()) || "4".equals(order.getEuStatusOrd()))){
				FoodOrds.add(order);
			}
			ordsnPs.add(order.getOrdsnParent());
			order.setEuStatusOrd("9");
			order.setFlagErase("1");
			order.setDateErase(d);
			order.setPkEmpErase(u.getPkEmp());
			order.setNameEmpErase(u.getNameEmp());
			order.setTs(order.getDateErase());
		}
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pk_cnord", pkOrds);
		//作废非药品医嘱
		ceaseNoPdOrd(pkOrdMap);
	     //2、作废药品医嘱时，如果已生成医嘱执行单并且未请领，删除执行单；
	    String delPdExList = "delete  from ex_order_occ  "+
				 "where exists (select 1 "+
				          "from cn_order  "+
				         "where ex_order_occ.pk_cnord = cn_order.pk_cnord "+
				           "and ex_order_occ.pk_pdapdt is null "+
				           "and cn_order.pk_cnord in (:pk_cnord) "+
				           "and cn_order.flag_durg = '1')";
	    DataBaseHelper.update(delPdExList, pkOrdMap);
	    //3.中二作废：护士取消执行就应该可以作废，不需要护士申请退药并且等药房退药后才可以作废医嘱
	    String exOrdOccSql = " select name_ord from cn_order ord where ord.pk_cnord in (:pk_cnord) and ord.flag_durg='1' and ord.flag_self='0' and ord.flag_note='0' and ord.flag_bl='1' and exists (select 1 from ex_order_occ occ where ord.pk_cnord=occ.pk_cnord and occ.pk_pdapdt is not null and occ.eu_status<'9')" ;
		List<Map<String, Object>> exOrdOccList = DataBaseHelper.queryForList(exOrdOccSql, pkOrdMap);
		if(exOrdOccList!=null&&exOrdOccList.size()>0){
			String str="";
			for(Map<String, Object> pdap : exOrdOccList){
				String nameOrd = pdap.get("nameOrd").toString();
				if(StringUtils.isEmpty(str)){
					str = "【"+nameOrd+"】"+"\n";
				}else {
					str +=  "【"+nameOrd+"】"+"\n";
				}
			}
			throw new BusException(str+"\n药品已经请领不能撤销，请联系护士进修取消请领操作后再进行撤销操作!");
		}
	    //3、判断是否有未完成的有效请领单
	   /* String pdApplyList = "select ap.name_emp_ap, ap.date_ap, apdt.pk_pdapdt "+
					  "from ex_pd_apply ap "+
					 "inner join ex_pd_apply_detail apdt on ap.pk_pdap = apdt.pk_pdap "+
					 "where ap.flag_cancel = '0' and apdt.flag_stop = '0' and apdt.flag_finish = '0' and apdt.pk_cnord in (:pk_cnord)";
		List<Map<String, Object>> pdAplistNoFin = DataBaseHelper.queryForList(pdApplyList, pkOrdMap);
		if(pdAplistNoFin!=null&&pdAplistNoFin.size()>0){
			String str="";
			for(Map<String, Object> pdap : pdAplistNoFin){
				String date_ap = pdap.get("dateAp").toString().substring(0, 19);
				if(StringUtils.isEmpty(str)){
					str = date_ap;
				}else if(str.indexOf(date_ap)<0){
					str += "和"+ date_ap;
				}
			}
			throw new BusException("该医嘱有未完成请领单，不能作废！"+"\n"+"请告知护士检查 申请日期为"+str+"的请领单!");
		}*/
		//4.判断发药数量是否为0
	/*	String pdDeListSql = "select (case when sum(pdde.eu_direct * abs(pdde.quan_pack)) is null then 0 else sum(pdde.eu_direct * abs(pdde.quan_pack)) end ) as sum_quan_de "+
                             "from ex_pd_de pdde where pdde.pk_cnord in (:pk_cnord)";
		List<Map<String, Object>> pdDelistQuan = DataBaseHelper.queryForList(pdDeListSql, pkOrdMap);
		BigDecimal sum_quan_de =  (BigDecimal) pdDelistQuan.get(0).get("sumQuanDe");
		if(sum_quan_de.compareTo(BigDecimal.ZERO)>0){
			throw new BusException("该医嘱未退药，不能作废！ 请告知护士  完成该医嘱的退药流程后  再作废!");
		}*/
		//查询所有护嘱
		Map<String,Object> nsOrdMap = new HashMap<String,Object>();
		nsOrdMap.put("ordsnP", ordsnPs);
		String nsOrdExSql = "select distinct ord.ordsn, ord.name_ord, dept.name_dept from ex_order_occ exlist"+
			 " inner join cn_order ord on exlist.pk_cnord = ord.pk_cnord and ord.ordsn_parent in (:ordsnP) and ord.del_flag='0' and ord.flag_durg = '0' and ord.flag_doctor = '0' "+
			 " inner join bd_ou_dept dept on ord.pk_dept_exec = dept.pk_dept and dept.del_flag='0'"+
			 " where  exlist.eu_status = '1' and exlist.flag_canc = '0'";
		List<Map<String,Object>> nsOrdExList = DataBaseHelper.queryForList(nsOrdExSql, nsOrdMap);
		if(nsOrdExList!=null && nsOrdExList.size()>0){
			String ns_name_ord_str="";
			String ns_dept_exec = (String) nsOrdExList.get(0).get("nameDept");
			for(Map<String, Object> exlist:nsOrdExList){
				String ns_ord_name = (String) exlist.get("nameOrd");
				String ns_sortno = String.valueOf(exlist.get("ordsn")) ;
				if("".equals(ns_name_ord_str)){
					ns_name_ord_str = "["+ns_ord_name+"(护嘱序号为"+ns_sortno+")]";
				}else{
					ns_name_ord_str +=","+"\n"+"["+ns_ord_name+"(护嘱序号为"+ns_sortno+")]";
				}
			}
			throw new BusException("待作废医嘱关联的护嘱"+"\n"+ns_name_ord_str+"需要["+ns_dept_exec+"]取消所有执行单后，医生方可作废!");
		}
		String nsOrdUnExSql="delete from ex_order_occ  where exists (select 1 from cn_order  "+
           " where ex_order_occ.pk_cnord = cn_order.pk_cnord and ex_order_occ.eu_status = '0' and cn_order.ordsn_parent in (:ordsnP) and cn_order.flag_doctor='0' and cn_order.flag_durg='0') ";
		DataBaseHelper.update(nsOrdUnExSql, nsOrdMap);
		DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",cnOrds);

	    //作废医嘱
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  where pk_cnord =:pkCnord ", cnOrds);
		//作废医嘱关联的护嘱
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  where ordsn_parent =:ordsnParent and flag_doctor='0' ", cnOrds);

		//作废饮食医嘱时更新患者营养情况
		if(FoodOrds.size()>0){
			DataBaseHelper.update(" UPDATE PV_IP SET dt_level_nutr=NULL where pk_pv=:pkPv ", FoodOrds.get(0));
		}
	}

	//取消产房医嘱
	public void cancleOrderLab(List<CnOrder> cnOrds,IUser user) {
		if(cnOrds==null ||cnOrds.size()<=0) return ;
		User u = user==null?UserContext.getUser():(User)user ;
		List<String> pkOrds = new ArrayList<String>();
		List<Integer> ordsnPs = new ArrayList<Integer>();
		List<CnOrder> FoodOrds = new ArrayList<>();//饮食医嘱

		//作废医嘱是否需要护士核对(只处理作废标志、作废人、作废时间)
			List<CnOrder> revokeList=new ArrayList<CnOrder>();
			List<CnOrder> updateList=new ArrayList<CnOrder>();
			Date d = new Date();
			for(CnOrder order: cnOrds){
				order.setFlagErase("1");
				order.setDateErase(d);
				order.setPkEmpErase(u.getPkEmp());
				order.setNameEmpErase(u.getNameEmp());
				order.setTs(order.getDateErase());
				order.setEuStatusOrd("9");
			}
		//作废医嘱
		DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",cnOrds);
		DataBaseHelper. batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
				+ "where pk_cnord =:pkCnord and eu_status_ord not in ('0','9') ", cnOrds);

	}

	public  void ceaseNoPdOrd(Map<String, Object> pkOrdMap) {

		//医技申请（检查、检验、手术、输血 在提交后不能作废）
		String exListSql = "select * from (  "+
				 "SELECT ord.code_apply,ord.name_ord, "+
					"CASE WHEN ord.code_ordtype LIKE '03%' THEN lis.eu_status WHEN ord.code_ordtype LIKE '02%' THEN ris.eu_status WHEN ord.code_ordtype LIKE '04%' THEN	op.eu_status WHEN ord.code_ordtype LIKE '12%' THEN trans.eu_status END eu_status "+
					 "FROM	cn_order ord "+
					"INNER JOIN pv_encounter pv ON pv.pk_pv = ord.pk_pv "+
					"INNER JOIN ex_order_occ exlist ON ord.pk_cnord = exlist.pk_cnord "+
					"LEFT OUTER JOIN cn_lab_apply lis ON ord.pk_cnord = lis.pk_cnord "+
					"LEFT OUTER JOIN cn_ris_apply ris ON ord.pk_cnord = ris.pk_cnord "+
					"LEFT OUTER JOIN cn_op_apply op ON ord.pk_cnord = op.pk_cnord "+
					"LEFT OUTER JOIN cn_trans_apply trans ON ord.pk_cnord = trans.pk_cnord "+
					"LEFT OUTER JOIN bd_ordtype ordtype ON ord.code_ordtype = ordtype.code "+
					"LEFT OUTER JOIN bd_ou_dept dept ON ord.pk_dept_exec = dept.pk_dept "+
					"WHERE exlist.eu_status = '0' "+
					"AND (	ordtype.code LIKE '02%' OR ordtype.code LIKE '03%' OR ordtype.code LIKE '04%' OR ordtype.code LIKE '12%') "+
					"AND ord.pk_cnord in (:pk_cnord)" +
					") tab where tab.eu_status='1' ";

		List<Map<String, Object>> commitList  = DataBaseHelper.queryForList(exListSql,pkOrdMap);
		String throwStr="";
		for(Map<String,Object> map :commitList){
			throwStr+="["+map.get("nameOrd")+"]需要取消提交后才能作废!"+"\n";
		}
		if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);


		//--------begin--------需求变更：变更前非药品医嘱未执行可以作废，变更为当非药品医嘱存在未取消的医嘱执行单时禁止作废----begin--------and ord.flag_note='0' and ord.flag_bl='1'-------------
		exListSql = "select distinct ord.pk_cnord,ord.name_ord,dept.name_dept from cn_order ord "+
						" inner join ex_order_occ occ on ord.pk_cnord=occ.pk_cnord  and occ.eu_status<'9' "+
						" inner join bd_ou_dept dept on occ.pk_dept_occ = dept.pk_dept and dept.del_flag='0' "+
					" where ord.flag_durg='0'  and ord.pk_cnord in (:pk_cnord) ";
		//查询执行单/非药品/未取消执行的/均不可以撤销
		List<Map<String, Object>> exList  = DataBaseHelper.queryForList(exListSql,pkOrdMap);
		throwStr="";
		for(Map<String,Object> map :exList){
			throwStr+="["+map.get("nameOrd")+"]需要["+map.get("nameDept")+"]先取消所有执行单才能作废!"+"\n";
		}
		if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);

		/*
		exListSql = "select distinct ord.name_ord,ex.pk_cnord,ex.eu_status,dept.name_dept "+
						   "from ex_order_occ ex "+
						        "inner join cn_order ord on ex.pk_cnord = ord.pk_cnord  and ord.flag_durg='0' and ord.flag_bl='1' "+
						        "inner join bd_ou_dept dept on ex.pk_dept_occ = dept.pk_dept and dept.del_flag='0' "+
						   "where ex.eu_status=1 and ex.flag_canc='0' and ex.pk_cnord in(:pk_cnord)";
		//查询执行单/非药品/未取消执行的/均不可以撤销
		List<Map<String, Object>> exList  = DataBaseHelper.queryForList(exListSql,pkOrdMap);
	    throwStr="";
	    for(Map<String,Object> map :exList){
	    	throwStr+="["+map.get("nameOrd")+"]需要["+map.get("nameDept")+"]先取消所有执行单才能作废!"+"\n";
	    }
	    if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);
	    //作废医嘱，删除未执行的医嘱执行单
	       //1、作废非药品医嘱时，如果已生成医嘱执行单并且未执行，删除执行单；
	    String delNoPdExList = "delete  from ex_order_occ  "+
                "where exists (select 1 "+
                            "from cn_order  "+
                         "where ex_order_occ.pk_cnord = cn_order.pk_cnord "+
                           "and ex_order_occ.eu_status = 0 "+
                           "and cn_order.pk_cnord in (:pk_cnord) "+
                        "and cn_order.flag_durg='0')";
	    DataBaseHelper.update(delNoPdExList, pkOrdMap);
	    */
		//--------end--------需求变更：变更前非药品医嘱未执行可以作废，变更为当非药品医嘱存在未取消的医嘱执行单时禁止作废----end---------------------
	}
	/**
	 * 根据医嘱写入CA记录
	 * @param cnOrds
	 */
	public void caRecordByOrd(boolean qryCnPk,List<CnOrder> cnOrds) {
		if(cnOrds!=null && cnOrds.size()>0){
			 List<CnSignCa> cnSignCa = new ArrayList<CnSignCa>();
			 Map<Integer, String> ordsnM = new HashMap<Integer,String>();
			 if(qryCnPk)ordsnM = getPkCnord(cnOrds);
			 for(CnOrder order : cnOrds){
				 if(order.getCnSignCa()==null){
					 continue;
				 }
				 CnSignCa signCa = order.getCnSignCa();
				 signCa.setPkSignca(NHISUUID.getKeyId());
				 signCa.setEuButype("0");//医嘱
				 signCa.setPkBu(StringUtils.isBlank(order.getPkCnord()) ? ordsnM.get(order.getOrdsn()) :order.getPkCnord());
				 if(StringUtils.isNotBlank(order.getPkOrg()))signCa.setPkOrg(order.getPkOrg());
				 if(StringUtils.isNotBlank(order.getPkEmpOrd()))signCa.setCreator(order.getPkEmpOrd());
				 if(CommonUtils.isEmptyString(signCa.getCreator())){
					 signCa.setCreator(UserContext.getUser().getPkEmp());
				 }
				 signCa.setTs(new Date());
				 signCa.setDelFlag("0");
				 signCa.setCreateTime(signCa.getTs());
				 cnSignCa.add(signCa);
			 }
			 if(cnSignCa.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnSignCa.class), cnSignCa);
		 }
	}
	/**
	 * 根据医嘱号对应医嘱主键
	 * @param cpOrdList
	 * @return
	 */
	public Map<Integer, String> getPkCnord(List<CnOrder> cpOrdList) {
		List<Integer> ordsn = new ArrayList<Integer>();
		for(CnOrder order : cpOrdList){
			ordsn.add(order.getOrdsn());
		}
		Map<String,Object> ordsnMap = new HashMap<String,Object>();
		ordsnMap.put("ordsn", ordsn);
		List<Map<String,Object>> recOrd= DataBaseHelper.queryForList("select ordsn,pk_cnord from cn_order where ordsn in(:ordsn) and del_flag='0'", ordsnMap);
		Map<Integer,String> ordsnM = new HashMap<Integer,String>();
		for(Map<String,Object> ordP : recOrd)
		{
		 ordsnM.put(((BigDecimal)ordP.get("ordsn")).intValue(),(String)ordP.get("pkCnord"));
		}
		return ordsnM;
	}
	/**
	 * 写入CA记录
	 * @param cnSignCa
	 */
	public void caRecord(List<CnSignCa> cnSignCa) {
		 if(cnSignCa==null ||cnSignCa.size()<=0) return;
		 for(CnSignCa signCa : cnSignCa){
			 signCa.setEuButype("0");//医嘱
			 ApplicationUtils.setDefaultValue(signCa, true);
		 }
		 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnSignCa.class), cnSignCa);
	}
	/**
	 * 路径外医嘱变异
	 * @param
	 * @param u
	 */
	public void recExpOrder(boolean qryCnPk,List<CnOrder> cpOrdLists,User u) {
		if(cpOrdLists!=null&&cpOrdLists.size()>0){
			Date dateNow = new Date();
			List<CnOrder> cpOrdList = new ArrayList<CnOrder>();
			for(CnOrder order :cpOrdLists){
				if(StringUtils.isNotBlank(order.getPkCprec())){
					cpOrdList.add(order);
				}
			}
			if(cpOrdList.size()<=0) return;
			Map<Integer, String> ordsnM = new HashMap<Integer,String>();
			if(qryCnPk)ordsnM = getPkCnord(cpOrdList);

			List<CpRecExp> cpRecExpList = new ArrayList<CpRecExp>();
			for(CnOrder order : cpOrdList){
				CpRecExp cpRecExp = new CpRecExp();
				cpRecExp.setPkRecexp(NHISUUID.getKeyId());
				cpRecExp.setEuTasktype("0");//0 医嘱，1 诊疗工作，2 护理工作
				cpRecExp.setDtCpexptype("05");//路径外
				cpRecExp.setPkOrg(u==null?order.getPkOrg():u.getPkOrg());
				cpRecExp.setPkEmpExp(u==null?order.getPkEmpOrd():u.getPkEmp());
				cpRecExp.setNameEmpExp(u==null?order.getNameEmpOrd():u.getNameEmp());

				cpRecExp.setPkCprec(order.getPkCprec());
				cpRecExp.setPkCpphase(order.getPkCpphase());
				cpRecExp.setPkCnord(StringUtils.isNotBlank(order.getPkCnord())?order.getPkCnord():ordsnM.get(order.getOrdsn()).toString());
				cpRecExp.setPkCpexp(order.getPkCpexp());
				cpRecExp.setNameExp(order.getNameExp());
				cpRecExp.setNote(order.getExpNote());
				cpRecExp.setDateExp(dateNow);
				cpRecExp.setCreator(u==null?order.getPkEmpOrd():u.getPkEmp());
				cpRecExp.setCreateTime(dateNow);
				cpRecExp.setTs(dateNow);

				cpRecExpList.add(cpRecExp);
			}

			if(cpRecExpList.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecExp.class), cpRecExpList);
			}
			
			/*
			List<CpRecExpDt> cpRecExpDtList = new ArrayList<CpRecExpDt>();
			CpRecExp cpRecExp = null;
			for(CnOrder order : cpOrdList){
				if(cpRecExp==null){
					cpRecExp = new CpRecExp();
					cpRecExp.setPkRecexp(NHISUUID.getKeyId());
					cpRecExp.setDtCpexptype("05");//路径外
					cpRecExp.setPkOrg(u==null?order.getPkOrg():u.getPkOrg());
					cpRecExp.setPkCpexp(order.getPkCpexp());
					cpRecExp.setPkCprec(order.getPkCprec());
					cpRecExp.setDateExp(new Date());
					cpRecExp.setPkEmpExp(u==null?order.getPkEmpOrd():u.getPkEmp());
					cpRecExp.setNote(order.getExpNote());
				}
				CpRecExpDt cpRecExpDt = new CpRecExpDt();
				cpRecExpDt.setPkRecexpdt(NHISUUID.getKeyId());
				cpRecExpDt.setPkRecexp(cpRecExp.getPkRecexp());
				cpRecExpDt.setEuType("2"); //2医嘱 3工作
				cpRecExpDt.setPkCnord(StringUtils.isNotBlank(order.getPkCnord())?order.getPkCnord():ordsnM.get(order.getOrdsn()).toString());
				cpRecExpDt.setPkOrg(u==null?order.getPkOrg():u.getPkOrg());
				cpRecExpDt.setTs(new Date());
				cpRecExpDtList.add(cpRecExpDt);
			}
		   if(cpRecExp!=null)DataBaseHelper.insertBean(cpRecExp);
		   if(cpRecExpDtList.size()>0)DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecExpDt.class), cpRecExpDtList);
		   */
		}
	}
	/**
	 * 路径外医嘱变异
	 */
	public void DealOutCpOrdExp(List<CpRecExp> cpRecExpList,User userInfo) {
		Date dateNow = new Date();
		if(cpRecExpList!=null && cpRecExpList.size()>0){
			for(CpRecExp cpRecExp: cpRecExpList ){
				if(StringUtils.isBlank(cpRecExp.getPkRecexp())){
					cpRecExp.setCreator(userInfo.getPkEmp());
					cpRecExp.setTs(dateNow);
					cpRecExp.setCreateTime(dateNow);
					cpRecExp.setDelFlag("0");
					DataBaseHelper.insertBean(cpRecExp);
				}
				else{
					DataBaseHelper.updateBeanByPk(cpRecExp,false);
				}
			}
		}
	}


	//同上
	public void recExpOrder(List<CpRecExpParam> cpRecExpLists) {
		if(cpRecExpLists!=null&&cpRecExpLists.size()>0){
			List<CpRecExpParam> cpRecExpList = new ArrayList<CpRecExpParam>();
			for(CpRecExpParam order :cpRecExpLists){
				if(StringUtils.isNotBlank(order.getPkCprec())){
					cpRecExpList.add(order);
				}
			}
			if(cpRecExpList.size()<=0) return;
			CpRecExp cpRecExp = null;
			List<CpRecExpDt> cpRecExpDtList = new ArrayList<CpRecExpDt>();
			for(CpRecExpParam order : cpRecExpList){
				if(cpRecExp==null){
					cpRecExp = new CpRecExp();
					cpRecExp.setPkRecexp(NHISUUID.getKeyId());
					cpRecExp.setDtCpexptype("05");//路径外
					cpRecExp.setPkOrg(order.getPkOrg());
					cpRecExp.setPkCpexp(order.getPkCpexp());
					cpRecExp.setPkCprec(order.getPkCprec());
					cpRecExp.setDateExp(new Date());
					cpRecExp.setPkEmpExp(order.getPkEmpExp());
					cpRecExp.setNote(order.getExpNote());
				}
				CpRecExpDt cpRecExpDt = new CpRecExpDt();
				cpRecExpDt.setPkRecexpdt(NHISUUID.getKeyId());
				cpRecExpDt.setPkRecexp(cpRecExp.getPkRecexp());
				cpRecExpDt.setEuType("2"); //2医嘱 3工作
				cpRecExpDt.setPkCnord(order.getPkCnord());
				cpRecExpDt.setPkOrg(order.getPkOrg());
				cpRecExpDt.setTs(new Date());
				cpRecExpDtList.add(cpRecExpDt);
			}
		   if(cpRecExp!=null)DataBaseHelper.insertBean(cpRecExp);
		   if(cpRecExpDtList.size()>0)DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CpRecExpDt.class), cpRecExpDtList);
		}
	}

	public  void sendMessage(String subject,List<CnOrder> cnOrds,boolean flagStop){
		if(cnOrds==null ||cnOrds.size()==0) return;
		List<Map<String,Object>> ords = DataBaseHelper.queryForList("select ord.name_ord,ord.pk_pv,ord.pk_dept_ns,pv.bed_no,pv.name_pi from cn_order ord inner join pv_encounter pv on pv.pk_pv=ord.pk_pv and pv.del_flag='0' where ord.pk_cnord=? ", new Object[]{cnOrds.get(0).getPkCnord()});
		if(ords==null ||ords.size()==0||ords.get(0).size()<=0) return;
		String content = "";
		if(flagStop){
			content = "【"+ords.get(0).get("bedNo").toString()+"】床【"+ords.get(0).get("namePi").toString()+"】有新停医嘱需要处理";
		}else{
			content = "【"+ords.get(0).get("bedNo").toString()+"】床【"+ords.get(0).get("namePi").toString()+"】有新开医嘱需要处理";
		}
		//消息主题与内容赋值内容一样
		subject = content;
		SysMessage msg=MessageUtils.createSysMessageOrd(subject,content,cnOrds.get(0).getPkPv(),cnOrds.get(0).getPkDeptNs(),true);
		if(msg==null) return;
		msgService.saveMessage(msg);
	}
	public void sendMessage(List<String> pkCns,String subject){
		if(pkCns==null ||pkCns.size()==0) return;
		List<Map<String,Object>> cnOrds = DataBaseHelper.queryForList("select ord.name_ord,ord.pk_pv,ord.pk_dept_ns,pv.bed_no,pv.name_pi from cn_order ord inner join pv_encounter pv on pv.pk_pv=ord.pk_pv and pv.del_flag='0' where ord.pk_cnord=? ", new Object[]{pkCns.get(0)});
		if(cnOrds==null ||cnOrds.size()==0||cnOrds.get(0).size()<=0) return;
		SysMessage msg=MessageUtils.createSysMessageOrd(subject, "【"+cnOrds.get(0).get("bedNo").toString()+"】床【"+cnOrds.get(0).get("namePi").toString()+"】有医嘱需要处理", cnOrds.get(0).get("pkPv").toString(),cnOrds.get(0).get("pkDeptNs").toString(),true);
		msgService.saveMessage(msg);
	}
	public String fomartTs(){
		String tsStr="";
		if(Application.isSqlServer()){
			tsStr=" CONVERT(varchar,ts,20) = to_char(:ts,'yyyy-mm-dd hh24:mi:ss') " ;
    	}else{
    		tsStr=" ts = :ts ";
    	}
		return tsStr;
	}
	public String setTs(Date date){
		return  "  ts=to_date('"+ DateFormatUtils.format(date,"yyyy/MM/dd HH:mm:ss")+"','yyyy/mm/dd hh24:mi:ss') " ;
	}
	/**
	 * 根据pk和ts查询医嘱 数量
	 * @param
	 * @return
	 */
	public int qryCnordByts(List<CnOrder> ordList) {

    	int tsOrd = 0;
        if(Application.isSqlServer()){
        	tsOrd = sqlServerQryCnordByts(ordList);
        }else{
        	tsOrd = oracleQryCnordByts(ordList);
        }
		return tsOrd;
	}
	private int oracleQryCnordByts(List<CnOrder> ordList) {
		StringBuilder sbl = new StringBuilder();
		for(CnOrder order : ordList){
			sbl.append("(")
					.append("'").append(order.getPkCnord()).append("'")
					.append(",").append("to_date('"+DateFormatUtils.format(order.getTs(),"yyyy/MM/dd HH:mm:ss")+"','yyyy/mm/dd hh24:mi:ss')")
					.append(")").append(",");
		}
		return DataBaseHelper.queryForScalar("select count(1) from cn_order where (pk_cnord,ts) in ("+sbl.substring(0,sbl.length()-1)+") and del_flag='0'", Integer.class);
	}
	private int sqlServerQryCnordByts(List<CnOrder> ordList) {
		StringBuilder sbl = new StringBuilder();
		for(CnOrder order : ordList){
			sbl.append("(")
					.append("pk_cnord='").append(order.getPkCnord())
					.append("' and CONVERT(varchar(100), ts, 20)='").append(DateFormatUtils.format(order.getTs(),"yyyy-MM-dd HH:mm:ss")).append("'")
					.append(")").append("or");

		}
		return DataBaseHelper.queryForScalar("select count(1) from cn_order where ("+sbl.substring(0, sbl.length()-2)+" ) and del_flag='0'", Integer.class);
	}

	public List<CnOrder> haveTsOrd(List<CnOrder> cnOrds) {
		List<CnOrder> haveTs = new ArrayList<CnOrder>();
		for(CnOrder ord : cnOrds){
			if(ord.getTs()!=null){
				haveTs.add(ord);
			}
		}
		return haveTs;
	}
	public void vaildUpdateCnOrdts(List<CnOrder> cnOrds){
		if(cnOrds==null || cnOrds.size()<=0) return;
		List<CnOrder>  pkCns = vailRepeatPk(cnOrds);
		int canCount = qryCnordByts(cnOrds);
		if(pkCns.size()!=canCount) throw new BusException("界面数据已改变，请先刷新!");
	}
	//去掉重复的医嘱，避免数量比较错误
	private List<CnOrder> vailRepeatPk(List<CnOrder> cnOrds) {
		List<CnOrder> cns = new ArrayList<CnOrder>();
		List<String> pks = new ArrayList<String>();
		for(CnOrder ord : cnOrds){
			String pkCnOrd = ord.getPkCnord();
			if(!pks.contains(pkCnOrd)){
				pks.add(pkCnOrd);
				cns.add(ord);
			}
		}
		return cns;
	}
	public  List<String> splitPkTsValidOrd(List<String> pkOrdList) {
		List<String> pkCnList = new ArrayList<String>();
		List<CnOrder> ords = new ArrayList<CnOrder>();
		for(String pk :pkOrdList){
			String pkcn = pk.split(",")[0];
			pkCnList.add(pkcn);
			CnOrder o = new CnOrder();
			o.setPkCnord(pkcn);
			o.setTs(DateUtils.strToDate(pk.split(",")[1],"yyyyMMddHHmmss"));
			ords.add(o);
		}
		vaildUpdateCnOrdts(ords);
		return pkCnList;
	}

	public  String splitPkTsValidOrd(String pkCnord) {
		List<String> pkCnOrds = new ArrayList<String>();
		pkCnOrds.add(pkCnord);
	    return splitPkTsValidOrd(pkCnOrds).get(0);
	}
	public String getDeptArguvalBool(String pkDept,String codeArgu,String defVale) {
		String pivasArgu = "SELECT ARGUVAL FROM BD_RES_PC_ARGU WHERE PK_DEPT=? AND CODE_ARGU=? and flag_stop='0' " ;
	    Map<String,Object> pivasM = DataBaseHelper.queryForMap(pivasArgu, new Object[]{pkDept,codeArgu});
	    String s = defVale;
	    if(pivasM!=null && pivasM.get("arguval")!=null && ("0".equals(s) ||"1".equals(s))) s=pivasM.get("arguval").toString();
		return s;
	}
	public Date getOutOrdDate(String pkPv){
		String sql  = "select date_start from cn_order where pk_pv=? and code_ordtype='1102' and flag_erase='0' and del_flag='0' ";
		List<CnOrder> list = DataBaseHelper.queryForList(sql, CnOrder.class, new Object[]{pkPv});
		if(list==null || list.size()==0) return null;
		return list.get(0).getDateStart();
	}
	public void updateDateStart(List<CnOrder> list){
		if(list==null||list.size()==0) return;
		Date d = getOutOrdDate(list.get(0).getPkPv());
		if(d!=null){
			for(CnOrder cn : list){
				if(cn.getDateStart()==null ||(cn.getDateStart()!=null && cn.getDateStart().compareTo(d)>0)) cn.setDateStart(d);
			}
			DataBaseHelper.batchUpdate(" update cn_order set date_start=:dateStart where pk_cnord=:pkCnord and del_flag='0'", list);
		}
	}
	public String qryOrdDesc(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map == null || map.size() <= 0) return "";
		String qryType = map.get("qryType") == null ? "" : map.get("qryType").toString(); //pd药品  item收费项目 set套餐 bdOrd医嘱项目
		if (StringUtils.isBlank(qryType)) return "";
		if (Application.isSqlServer()) {
			map.put("dbType", "sqlserver");
		} else {
			map.put("dbType", "oracle");
		}
		StringBuilder sbl = new StringBuilder();
		if ("pd".equals(qryType))
			sbl.append(qryPdDesc(map));
		else if ("item".equals(qryType))
			sbl.append(qryItemDesc(map));
		else if ("set".equals(qryType))
			sbl.append(qryItemSet(map));
		else if ("bdOrd".equals(qryType))
			sbl.append(qryBdOrd(map));
		else if("bdOrdLis".equals(qryType))
			sbl.append(qryBdOrdLis(map));
		else if("bdOrdRis".equals(qryType))
			sbl.append(qryBdOrdRis(map));
		String pkOrd = MapUtils.getString(map, "pkOrd");
		if(StringUtils.isNotBlank(pkOrd) && StringUtils.isNotBlank(MapUtils.getString(map, "pkHp"))){
			map.put("list", Lists.newArrayList(pkOrd));
			List<Map<String, Object>> list = cnPubMapper.queryHpRateFormat(map);
			if(CollectionUtils.isNotEmpty(list)) {
				sbl.append("【收费项目】｛");
				list.stream().forEach(vo ->
						sbl.append(MapUtils.getString(vo, "name")).append(" ").append(MapUtils.getString(vo, "hpRate")).append(" "));
				sbl.append("｝");
			}
		}
		return sbl.toString();
	}
	
	/**
	 * 门诊医生站西药sheet点击开立的项目时查询对应西药的医保信息是医保还是限制还是自费
	 * 交易号：004001005049
	 * @param param
	 * @param user
	 * @return 
	 */
	public String qryOrdMedicalInsurance(String param, IUser user){
		String retInfo ="(ZF)";
		
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap != null)
		{
			String pkOrd = (String)paramMap.get("pkOrd");
			String nameOrdView = (String)paramMap.get("nameOrdView");
			StringBuilder sb = new StringBuilder();
			Object[] objArray = null;
			sb.append("SELECT pd.pk_pd as pkpd, alias.alias as alias, ");
			sb.append("CASE ");
			sb.append("WHEN pd.pk_pd IS NOT NULL THEN ");
			sb.append("CASE ");
			sb.append("WHEN itemmap.aka036 = '1' THEN ");
			sb.append("'(XZ)' ");
			sb.append("WHEN itemmap.bkm032 IN ('01', '02', '03') THEN ");
			sb.append("'(YB)' ");
			sb.append("ELSE ");
			sb.append("'(ZF)' ");
			sb.append("END ");
			sb.append("ELSE ");
			sb.append("CASE ");
			sb.append("WHEN itemmap.AKE001 IS NULL THEN ");
			sb.append("'(ZF)' ");
			sb.append("ELSE ");
			sb.append("'(YB)' ");
			sb.append("END ");
			sb.append("END AS ybinfo ");
			sb.append("FROM   bd_pd pd ");
			sb.append("INNER  JOIN bd_pd_as alias ");
			sb.append("ON     (pd.pk_pd = alias.pk_pd AND alias.del_flag = '0') ");
			sb.append("LEFT   JOIN view_cn_ins_szyb_itemmap itemmap ");
			sb.append("ON     itemmap.pk_item = pd.pk_pd ");
			if(pkOrd != null && !"".equals(pkOrd))
			{
				sb.append("WHERE  pd.pk_pd = ? ");
				objArray = new Object[]{pkOrd, nameOrdView};
			}
			else
			{
				sb.append("WHERE  pd.pk_pd is null ");
				objArray = new Object[]{nameOrdView};
			}
			sb.append("AND    pd.del_flag = '0' ");
			sb.append("AND    alias.alias = ? ");
			String sql = sb.toString();
			List<Map<String, Object>> objList = DataBaseHelper.queryForList(sql, objArray);
			for(Map<String, Object> mapItem : objList) 
			{
				retInfo = (String)mapItem.get("ybinfo");
				break;
			}
		}
		
		
		return retInfo;
	}
	
	

	private String qryItemSet(Map<String, Object> map) {

		return "";
	}

	private String rntBankStr(Object content){
		if(content==null)
			return "";
		else
			return content.toString();
	}
	private String qryItemDesc(Map<String, Object> map) {
		String rtn="";
		String emty = "   ";
		String bdItemHpEulevel="";
		String bdPdNameInd="";
		String hpCgDivItemNote="";//"目录中不存在或者没有对应关系";
		String bdItemHp="";//医保内容
		String pkOrd = map.get("pkOrd")==null?"": map.get("pkOrd").toString();
		if(StringUtils.isBlank(pkOrd)) return "";
		List<Map<String,Object>> itemBaseInfo = new ArrayList<Map<String, Object>>();
		String searchBdOrd = map.get("searchOrd")==null?"":map.get("searchOrd").toString();
		if("1".equals(searchBdOrd)){
			itemBaseInfo = cnPubMapper.queryOrdBaseInfo(map);
		}else{
			itemBaseInfo = cnPubMapper.queryItemBaseInfo(map);
		}
		String jcjySpecFlag = map.get("jcjySpec")==null?"":map.get("jcjySpec").toString();
		String zlSpecFlag = map.get("zlSpec")==null?"":map.get("zlSpec").toString();

		if(itemBaseInfo==null||itemBaseInfo.size()==0) return "";
		List<Map<String, Object>> dicttype = cnPubMapper.queryHpdicttype(map);
		for(Map<String,Object> ord:itemBaseInfo ){
			String pdBase = "";
			bdItemHp="";
			bdItemHpEulevel="";
			hpCgDivItemNote="";
			if("1".equals(jcjySpecFlag) || "1".equals(zlSpecFlag))
			{
				if(ord.get("codeStd") != null && !"".equals(ord.get("codeStd").toString())) pdBase = pdBase + "【编码】"+rntBankStr(ord.get("codeStd"))+emty;				
			}
			else
			{
				if(ord.get("code") != null && !"".equals(ord.get("code").toString())) pdBase = pdBase + "【编码】"+rntBankStr(ord.get("code"))+emty;				
			}
			if(ord.get("name") != null && !"".equals(ord.get("name").toString())) pdBase = pdBase +"【名称】"+rntBankStr(ord.get("name"))+emty;
			if(ord.get("spec") != null && !"".equals(ord.get("spec").toString())) pdBase = pdBase +"【规格】"+rntBankStr(ord.get("spec"))+emty;
			if("1".equals(jcjySpecFlag) || "1".equals(zlSpecFlag))
			{
				if(ord.get("price") != null && !"".equals(ord.get("price").toString())) pdBase = pdBase +"【单价】"+rntBankStr(ord.get("price"))+"元/"+rntBankStr(ord.get("nameUnit"))+emty;		
			}
			else
			{
				if(ord.get("price") != null && !"".equals(ord.get("price").toString())) pdBase = pdBase +"【单价】"+rntBankStr(ord.get("price"))+"/"+rntBankStr(ord.get("nameUnit"))+emty;		
			}
			
			if(ord.get("descItem") != null && !"".equals(ord.get("descItem").toString())) pdBase = pdBase +"【项目内涵】"+rntBankStr(ord.get("descItem"))+emty;
			if(ord.get("exceptItem") != null && !"".equals(ord.get("exceptItem").toString())) pdBase = pdBase +"【除外内容】"+rntBankStr(ord.get("exceptItem"))+emty;
			if(!"1".equals(jcjySpecFlag) && !"1".equals(zlSpecFlag))
			{
				if(ord.get("note") != null && !"".equals(ord.get("note").toString())) pdBase = pdBase +"【备注】"+rntBankStr(ord.get("note"));				
			}
			//if(!"".equals(pdBase)) pdBase= pdBase +"\r\n";
			if((dicttype!=null&&dicttype.size()==1)){
				//【市医保】乙类（5%）
				map.put("dtHpdicttype", dicttype.get(0).get("dtHpdicttype").toString());
				map.put("pkOrd", rntBankStr(ord.get("pkItem")));
				if(dicttype.get(0).get("hpdicttypename") != null && !"".equals(dicttype.get(0).get("hpdicttypename").toString())) {
					bdItemHp = getBdItemHpEulevel(map);
					if(!"".equals(bdItemHp))
						bdItemHpEulevel = "【"+dicttype.get(0).get("hpdicttypename").toString()+"】"+getBdItemHpEulevel(map)+emty;
				}
				List<Map<String, Object>> gyType = cnPubMapper.queryGyType(map);
				if((gyType!=null&&gyType.size()==1)){
					hpCgDivItemNote= rntBankStr(gyType.get(0).get("note"));
				}
			}


			String pdYb = "";
			if(!"".equals(bdItemHpEulevel)) pdYb = pdYb + bdItemHpEulevel+emty;
			if(!"".equals(hpCgDivItemNote)) pdYb = pdYb +"【记账】"+hpCgDivItemNote+emty;
			if(!"".equals(pdBase)) pdBase= pdBase +emty;
 
			String isSd = map.get("isSd")==null?"0": map.get("isSd").toString();
			if("1".equals(isSd)){
				map.put("code", rntBankStr(ord.get("code")));
				String YbType= qryItemYb(map);
				rtn+="("+YbType+")";
			}
			rtn+=pdBase + pdYb +"\r\n";
		}

		//灵璧需求
		map.put("pkOrd", pkOrd);
		List<Map<String,Object>> OrdList = cnPubMapper.qryOrd(map);
		String pdOther = "";
		if(OrdList!=null && OrdList.size()>0){
			Map<String,Object> ord1 = OrdList.get(0);
			if (ord1.get("descOrd") != null && !"".equals(ord1.get("descOrd").toString()))  pdOther = pdOther + "【项目内涵】" + rntBankStr(ord1.get("descOrd")) + emty;
			if (ord1.get("exceptOrd") != null && !"".equals(ord1.get("exceptOrd").toString()))  pdOther = pdOther + "【除外内容】" + rntBankStr(ord1.get("exceptOrd")) + emty;
			pdOther =  pdOther + emty;
		}
		return rtn+pdOther + "\r\n" ;
	}

	/**
	 * 药品提示信息，格式如下：
	 * 05158  奥沙利铂甘露醇注射液/辰雅注射液  【规格】100ml:50mg/瓶  【单价】283.64元/瓶【剂量】100ml
	    【市医保】乙类（5%）【记账】区公费  【备注】对帕类衍生物有过敏者禁....【FDA分级】D级【说明书】孕妇禁用 【贵重级别】普通药品
	 * @return
	 */
	private String qryPdDesc(Map<String, Object> map) {
		String emty = "   ";
		String bdItemHpEulevel="";
		String bdPdNameInd="";
		String hpCgDivItemNote="";
		String bdItemHp="";//医保内容
		String pkPd = map.get("pkOrd")==null?"": map.get("pkOrd").toString();
		if(StringUtils.isBlank(pkPd)) return "";
		String sqlStr = "select pd.flag_precious ,case pd.flag_precious when '1' then '贵重物品' else '普通物品' end precious,pd.vol,   pd.pk_unit_vol, pd.note, pd.pk_unit_pack, pd.price, pd.spec, pd.name_chem,pd.name,  pd.code_hp,unitP.name as name_unit_pack,unitV.name as name_unit_vol  from bd_pd pd left join bd_unit unitP on unitP.pk_unit=pd.pk_unit_pack and unitP.del_flag='0' left join bd_unit unitV on unitV.pk_unit = pd.pk_unit_vol and unitV.del_flag='0' where pd.pk_pd=? and pd.del_flag='0'";
		List<Map<String,Object>> bdmap = DataBaseHelper.queryForList(sqlStr, new Object[]{pkPd});
		if(bdmap==null||bdmap.size()!=1) return "";
		Map<String,Object> pd = bdmap.get(0);

		List<Map<String, Object>> dicttype = cnPubMapper.queryHpdicttype(map);
		if((dicttype!=null&&dicttype.size()==1)){
			//【市医保】乙类（5%）
			map.put("dtHpdicttype", dicttype.get(0).get("dtHpdicttype").toString());
			if(dicttype.get(0).get("hpdicttypename") != null && !"".equals(dicttype.get(0).get("hpdicttypename").toString())) {
				bdItemHp = getBdItemHpEulevel(map);
				if(!"".equals(bdItemHp))
					bdItemHpEulevel = "【"+dicttype.get(0).get("hpdicttypename").toString()+"】"+getBdItemHpEulevel(map)+emty;
			}
			List<Map<String, Object>> gyType = cnPubMapper.queryGyType(map);
			if((gyType!=null&&gyType.size()==1)){
				hpCgDivItemNote= rntBankStr(gyType.get(0).get("note"));
			}

			//【适应症】
			String descFit = map.get("descFit")==null?"": map.get("descFit").toString();
		    bdPdNameInd = StringUtils.isBlank(descFit) ? getBdPdNameInd(map):descFit;
		}
		//附加属性
		String sqlStr1 = "select bdAtt.pk_pd, attd.CODE_ATT,attd.name_att, bdAtt.val_att from BD_PD_ATT bdAtt inner join BD_PD_ATT_DEFINE attd on bdAtt.PK_PDATTDEF = attd.PK_PDATTDEF where CODE_ATT='BA15' and bdAtt.pk_pd=? and bdAtt.DEL_FLAG='0' and attd.DEL_FLAG='0' ";
		List<Map<String,Object>> bdmap1= DataBaseHelper.queryForList(sqlStr1, new Object[]{pkPd});

		String pdBase =rntBankStr(pd.get("codeHp")) +emty+  rntBankStr(pd.get("name"))+"/"+rntBankStr(pd.get("nameChem"))+emty;
		if(pd.get("spec")!=null && !"".equals(pd.get("spec").toString())) pdBase = pdBase + "【规格】"+rntBankStr(pd.get("spec")) +emty;
		if(pd.get("price")!=null && !"".equals(pd.get("price").toString())) pdBase = pdBase +"【单价】"+rntBankStr(pd.get("price"))+"元/"+rntBankStr(pd.get("nameUnitPack")) +emty;
		if(pd.get("vol")!=null && !"".equals(pd.get("vol").toString())) pdBase = pdBase + "【剂量】"+rntBankStr(pd.get("vol"))+rntBankStr(pd.get("nameUnitVol"));
		//if(!"".equals(pdBase)) pdBase= pdBase +"\r\n";
		String pdYb=bdItemHpEulevel;
		if(hpCgDivItemNote !=null && !"".equals(hpCgDivItemNote)) pdYb = pdYb +"【记账】"+hpCgDivItemNote+emty;
		if(bdPdNameInd!=null && !"".equals(bdPdNameInd)) pdYb = pdYb +"【适应症】"+bdPdNameInd+emty;
		if(pd.get("note")!=null && !"".equals(pd.get("note").toString())) pdYb = pdYb +"【备注】"+rntBankStr(pd.get("note"))+emty;
		if(pd.get("precious")!=null && !"".equals(pd.get("precious").toString())) pdYb = pdYb +"【贵重级别】"+rntBankStr(pd.get("precious"));

		String BA15="";
		if(bdmap1!=null && bdmap1.size()>0 && bdmap1.get(0)!=null && bdmap1.get(0).get("valAtt")!=null) {
			if(bdmap1.get(0).get("nameAtt")!=null && !"".equals(bdmap1.get(0).get("nameAtt").toString())){
				BA15="【"+bdmap1.get(0).get("nameAtt").toString()+"】"+bdmap1.get(0).get("valAtt").toString();
			}else{
				BA15="【注意事项】"+bdmap1.get(0).get("valAtt").toString();
			}

		}

		//灵璧需求
		List<Map<String,Object>> OrdList = cnPubMapper.qryOrd(map);
		String pdOther = "";
		if(OrdList!=null && OrdList.size()>0){
			Map<String,Object> ord = OrdList.get(0);
			if (ord.get("descOrd") != null && !"".equals(ord.get("descOrd").toString()))  pdOther = pdOther + "【项目内涵】" + rntBankStr(ord.get("descOrd")) + emty;
			if (ord.get("exceptOrd") != null && !"".equals(ord.get("exceptOrd").toString()))  pdOther = pdOther + "【除外内容】" + rntBankStr(ord.get("exceptOrd")) + emty;
			pdOther =  pdOther + "\r\n";
		}

		//深大客户化
		String isSd = map.get("isSd")==null?"0": map.get("isSd").toString();
		if("1".equals(isSd)){
			String YbType= qryYb(map);
			return "("+YbType+")"+pdBase +emty +pdYb+emty+BA15 + "\r\n"+pdOther;
		}else{
			return pdBase +emty +pdYb+emty+BA15 + "\r\n"+pdOther;
		}

	}

	//【市医保】乙类（5%）
	private String getBdItemHpEulevel(Map<String, Object> map) {
		String itemHpSql = null;
			itemHpSql = "select case eu_level when '0' then '甲类' when '1' then '乙类' else '自费' end eu_level,"
					+ "cast((case when ratio_self is null then 1 else ratio_self  end)*100 as decimal(5,2)) ratio_self from bd_item_hp where pk_item=? and dt_hpdicttype=? and del_flag='0'";
		List<Map<String,Object>> itemHp = DataBaseHelper.queryForList(itemHpSql, new Object[]{map.get("pkOrd").toString(),map.get("dtHpdicttype").toString()});
		if((itemHp==null||itemHp.size()!=1)) return "";
		String eulevel = itemHp.get(0).get("euLevel")==null?"":itemHp.get(0).get("euLevel").toString();
		String ratioSelf = itemHp.get(0).get("ratioSelf")==null?"":"("+itemHp.get(0).get("ratioSelf").toString()+"%)";
		
		//wangpengyong task 7439 门诊和住院医生站药品医保自付比例是否显示，灵璧中医院不显示
		if ("false".equals(ApplicationUtils.getPropertyValue("cn.percent.enable", ""))) {
			 ratioSelf = "";
		}
		return eulevel + ratioSelf;
	}

	//【适应症】
	private String getBdPdNameInd(Map<String, Object> map) {
		String nameIndSql="select ind.desc_ind from bd_pd_ind ind  inner join bd_pd_indpd indpd on indpd.pk_pdind = ind.pk_pdind and ind.code_indtype=? and indpd.pk_pd=? and ind.del_flag='0' and indpd.del_flag='0'";
		List<Map<String,Object>> nameInd = DataBaseHelper.queryForList(nameIndSql, new Object[]{map.get("dtHpdicttype").toString(),map.get("pkOrd").toString()});
		if((nameInd==null||nameInd.size()!=1)) return "";
		return rntBankStr(nameInd.get(0).get("descInd"));
	}

	//查询频次执行时间
	public List<Map<String,Object>> qryFreqTime (String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = cnPubMapper.qryFreqTime(paramMap);
		return list;
	}

	//判断两个科室是不是在同一个大专科
	public int qryTheSameSpecialty (String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		int count= cnPubMapper.qryTheSameSpecialty(paramMap);
		return count;
	}

	//获得医嘱主键拼接条件--pkCnord
	private String getPkCnordStr(List<? extends CnOrder> ordList){
		StringBuffer sbfSql = new StringBuffer();
		String rtnStr="";
		if(ordList!=null && ordList.size()>0){
			for(CnOrder order : ordList){
				sbfSql.append("'"+order.getPkCnord()+"',");
			}
			sbfSql.deleteCharAt(sbfSql.length()-1);
			rtnStr=sbfSql.toString();
		}
		return rtnStr;
	}


	/**
	 *根据分类编码获取其默认数据
	 */
	public BdDefdocVo qryBdDefdocFlagDef(String param,IUser user) {
		String codeDefdoclist = JsonUtil.getFieldValue(param, "dictType");
		BdDefdocVo bdDefdocVo = cnPubMapper.qryBdDefdocFlagDef(codeDefdoclist);
		return bdDefdocVo;
	}

	/**
	 * 患者主诊断转换
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryDiag (String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = cnPubMapper.qryDiag(paramMap);
		return list;
	}

	/**
	 * 查询医嘱项目信息
	 * @param map
	 * @return
	 */
	private String qryBdOrd(Map<String, Object> map) {
		String str="";
		String emty="";
		List<Map<String,Object>> OrdList = cnPubMapper.qryOrd(map);
		for(Map<String,Object> ord:OrdList ) {
			String pdBase = "";
			if (ord.get("code") != null && !"".equals(ord.get("code").toString()))  pdBase = pdBase + "【编码】" + rntBankStr(ord.get("code")) + emty;
			if (ord.get("name") != null && !"".equals(ord.get("name").toString()))  pdBase = pdBase + "【名称】" + rntBankStr(ord.get("name")) + emty;
			if (ord.get("pricestr") != null && !"".equals(ord.get("pricestr").toString()))  pdBase = pdBase + "【单价】" + rntBankStr(ord.get("pricestr")) + emty;
			if (ord.get("descOrd") != null && !"".equals(ord.get("descOrd").toString()))  pdBase = pdBase + "【项目内涵】" + rntBankStr(ord.get("descOrd")) + emty;
			if (ord.get("exceptOrd") != null && !"".equals(ord.get("exceptOrd").toString()))  pdBase = pdBase + "【除外内容】" + rntBankStr(ord.get("exceptOrd")) + emty;
			if (ord.get("note") != null && !"".equals(ord.get("note").toString()))  pdBase = pdBase + "【备注】" + rntBankStr(ord.get("note")) + emty;
			if (ord.get("dateslot") != null && !"".equals(ord.get("dateslot").toString()))  pdBase = pdBase + "【检验时间】" + rntBankStr(ord.get("dateslot")) + emty;
			if (ord.get("durationRpt") != null && !"".equals(ord.get("durationRpt").toString()))  pdBase = pdBase + "【报告时间】" + rntBankStr(ord.get("durationRpt")) + emty;
			str = str + pdBase + "\r\n";
		}

		//灵璧需求
		List<Map<String,Object>> OrdList1 = cnPubMapper.qryOrd(map);
		String pdOther = "";
		if(OrdList1!=null && OrdList1.size()>0){
			Map<String,Object> ord = OrdList.get(0);
			if (ord.get("descOrd") != null && !"".equals(ord.get("descOrd").toString()))  pdOther = pdOther + "【项目内涵】" + rntBankStr(ord.get("descOrd")) + emty;
			if (ord.get("exceptOrd") != null && !"".equals(ord.get("exceptOrd").toString()))  pdOther = pdOther + "【除外内容】" + rntBankStr(ord.get("exceptOrd")) + emty;
			pdOther =  pdOther + "\r\n";
		}
			return str + "\r\n"+pdOther;
	}

	/**
	 * 查询医嘱项目信息--检验
	 * @param map
	 * @return
	 */
	private String qryBdOrdLis(Map<String, Object> map) {
		String str="";
		String emty="";
		String hh="\r\n";
		List<Map<String,Object>> OrdList = cnPubMapper.qryOrd(map);
		if(OrdList!=null && OrdList.size()>0){
			Map<String,Object> ord = OrdList.get(0);
			String pdBase = "";
			if (ord.get("dateslot") != null && !"".equals(ord.get("dateslot").toString()))  pdBase = pdBase + "【检验时间】" + rntBankStr(ord.get("dateslot")) + hh;
			if (ord.get("durationRpt") != null && !"".equals(ord.get("durationRpt").toString()))  pdBase = pdBase + "【报告时间】" + rntBankStr(ord.get("durationRpt")) + hh;
			if (ord.get("descOrd") != null && !"".equals(ord.get("descOrd").toString()))  pdBase = pdBase + "【项目内涵】" + rntBankStr(ord.get("descOrd")) + hh;
			if (ord.get("code") != null && !"".equals(ord.get("code").toString()))  pdBase = pdBase + "【编码】" + rntBankStr(ord.get("code")) + hh;
			if (ord.get("flagEmer") != null && !"".equals(ord.get("flagEmer").toString())) {
				String flagEmer=rntBankStr(ord.get("flagEmer"));
				if("1".equals(flagEmer)) pdBase = pdBase + "【报告加急】 允许"+ hh;
				else pdBase = pdBase + "【报告加急】 不允许"+ hh;
			}
			/**
			 * 以下内容不显示
			 if (ord.get("name") != null && !"".equals(ord.get("name").toString()))  pdBase = pdBase + "【名称】" + rntBankStr(ord.get("name")) + emty;
			 if (ord.get("pricestr") != null && !"".equals(ord.get("pricestr").toString()))  pdBase = pdBase + "【单价】" + rntBankStr(ord.get("pricestr")) + emty;

			 if (ord.get("exceptOrd") != null && !"".equals(ord.get("exceptOrd").toString()))  pdBase = pdBase + "【除外内容】" + rntBankStr(ord.get("exceptOrd")) + emty;
			 if (ord.get("note") != null && !"".equals(ord.get("note").toString()))  pdBase = pdBase + "【备注】" + rntBankStr(ord.get("note")) + emty;
			 */

			str = str + pdBase + "\r\n";
		}

		return str;
	}

	/**
	 * 查询医嘱项目信息--检查
	 * @param map
	 * @return
	 */
	private String qryBdOrdRis(Map<String, Object> map) {
		String str="";
		String emty="";
		String hh="\r\n";
		List<Map<String,Object>> OrdList = cnPubMapper.qryOrd(map);
		if(OrdList!=null && OrdList.size()>0){
			Map<String,Object> ord = OrdList.get(0);
			String pdBase = "";
			if (ord.get("note") != null && !"".equals(ord.get("note").toString()))  pdBase = pdBase + "【备注】" + rntBankStr(ord.get("note")) + hh;
			if (ord.get("descOrd") != null && !"".equals(ord.get("descOrd").toString()))  pdBase = pdBase + "【项目内涵】" + rntBankStr(ord.get("descOrd")) + hh;
			if (ord.get("exceptOrd") != null && !"".equals(ord.get("exceptOrd").toString()))  pdBase = pdBase + "【除外内容】" + rntBankStr(ord.get("exceptOrd")) + hh;
			if (ord.get("code") != null && !"".equals(ord.get("code").toString()))  pdBase = pdBase + "【编码】" + rntBankStr(ord.get("code")) + hh;
			if (ord.get("flagEmer") != null && !"".equals(ord.get("flagEmer").toString())) {
				String flagEmer=rntBankStr(ord.get("flagEmer"));
				if("1".equals(flagEmer)) pdBase = pdBase + "【报告加急】 允许"+ hh;
				else pdBase = pdBase + "【报告加急】 不允许"+ hh;
			}
			/**
			 *以下内容不显示
			if (ord.get("name") != null && !"".equals(ord.get("name").toString()))  pdBase = pdBase + "【名称】" + rntBankStr(ord.get("name")) + emty;
			if (ord.get("pricestr") != null && !"".equals(ord.get("pricestr").toString()))  pdBase = pdBase + "【单价】" + rntBankStr(ord.get("pricestr")) + emty;
			if (ord.get("dateslot") != null && !"".equals(ord.get("dateslot").toString()))  pdBase = pdBase + "【检验时间】" + rntBankStr(ord.get("dateslot")) + emty;
			if (ord.get("durationRpt") != null && !"".equals(ord.get("durationRpt").toString()))  pdBase = pdBase + "【报告时间】" + rntBankStr(ord.get("durationRpt")) + emty;
			 */

			str = str + pdBase + "\r\n";
		}
		return str;
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
		List<Map<String, Object>> queryPati = cnPubMapper.queryPati(paramMap);
		return queryPati != null ? queryPati.get(0) : null ;
	}

	//药品关联医保项目
	public String qryYb(Map<String, Object> map){
		String nameIndSql="select pk_item,aka036,bkm032,ake001 from view_cn_ins_szyb_itemmap  where pk_item=?";
		List<Map<String,Object>> nameInd = DataBaseHelper.queryForList(nameIndSql, new Object[]{map.get("pkOrd").toString()});
		if((nameInd==null||nameInd.size()==0)) return "ZF"; //如果为空则自费
		if(nameInd.size()>0) {
			String aka036 = rntBankStr(nameInd.get(0).get("aka036"));
			String bkm032 = rntBankStr(nameInd.get(0).get("bkm032"));
			if("1".equals(aka036)) {
				return "XZ";
			}
			else if("01".equals(bkm032) ||"02".equals(bkm032) || "03".equals(bkm032)){
				return "YB";
			}else{
				return "ZF";
			}
		}
		return "ZF";

	}

	//计费项目关联医保信息
	public String qryItemYb(Map<String, Object> map){
		String nameIndSql="select aka036,bkm032,ake001 from ins_szyb_itemmap " +
				"where CODE_HOSP=? and EU_HPDICTTYPE='01' " +
				"and SYSDATE BETWEEN DATE_BEGIN and DATE_END ";
		List<Map<String,Object>> nameInd = DataBaseHelper.queryForList(nameIndSql, new Object[]{map.get("code").toString()});
		if((nameInd==null||nameInd.size()==0)) return "ZF"; //如果为空则自费
		if(nameInd.size()>0) {
			String aka036 = rntBankStr(nameInd.get(0).get("aka036"));
			String bkm032 = rntBankStr(nameInd.get(0).get("bkm032"));
			if("1".equals(aka036)) {
				return "XZ";
			}
			else if("01".equals(bkm032) ||"02".equals(bkm032) || "03".equals(bkm032)){
				return "YB";
			}else{
				return "ZF";
			}
		}
		return "ZF";
	}



	public void PatiSave(String param , IUser user){
		PiMaster pait=JsonUtil.readValue(param, PiMaster.class);
		PvEncounter pven=JsonUtil.readValue(param, PvEncounter.class);
		Map<String,Object> pvOp=JsonUtil.readValue(param, Map.class);

		User u = (User)user;
		if(pven.getPkPv()==null) {
			throw new BusException("请传入患者就诊主键！");
		}
		if(pait.getPkPi()==null) {
			throw new BusException("请传入患者主键！");
		}

		//更新患者信息
		DataBaseHelper.update("update pi_master set dt_idtype=:dtIdtype,id_no = :idNo,name_pi = :namePi,dt_sex=:dtSex,birth_date=:birthDate,mobile = :mobile,addrcode_cur=:addrcodeCur,addr_cur=:addrCur,modifier=:modifier where pk_pi = :pkPi " , pait);
		//更新患者就诊信息
		DataBaseHelper.update("update pv_encounter set name_pi = :namePi,dt_sex=:dtSex,addrcode_cur=:addrcodeCur,addr_cur_dt=:addrCurDt,height=:height,weight=:weight,idno_agent=:idnoAgent,name_agent=:nameAgent,tel_agent=:telAgent,dt_spcdtype=:dtSpcdtype,flag_card_chk=:flagCardChk,modifier=:modifier where pk_pv = :pkPv " , pven);
		//更新患者疾病信息
		int nUpdateCount = DataBaseHelper.update("update cn_emr_op set sbp=:sbp,dbp=:dbp,modifier=:modifier,HEART_RATE=:heartRate where pk_pv = :pkPv " , pvOp);
		if(nUpdateCount == 0)
		{
			//插入病历记录
			CnOpEmrRecord cnOpEmrRecord = new CnOpEmrRecord();
			Date curDate = new Date();
			cnOpEmrRecord.setPkOrg(u.getPkOrg());
			cnOpEmrRecord.setPkPi(pven.getPkPi());
			cnOpEmrRecord.setPkPv(pven.getPkPv());
			//cnOpEmrRecord.setPkHp(pven.getPkInsu());
			cnOpEmrRecord.setDelFlag("0");
			cnOpEmrRecord.setCreator(u.getPkEmp());
			cnOpEmrRecord.setCreateTime(curDate);
			if(pvOp.containsKey("sbp") && pvOp.get("sbp") != null)
			{
				int sbp = Integer.valueOf(pvOp.get("sbp").toString());
				cnOpEmrRecord.setSbp(sbp);
			}
			if(pvOp.containsKey("dbp") && pvOp.get("dbp") != null)
			{
				int dbp = Integer.valueOf(pvOp.get("dbp").toString());
				cnOpEmrRecord.setDbp(dbp);
			}
            if(pvOp.containsKey("heartRate") && pvOp.get("heartRate") != null)
            {
                int heartRate = Integer.valueOf(pvOp.get("heartRate").toString());
                cnOpEmrRecord.setHeartRate(heartRate);
            }
			cnOpEmrRecord.setTs(curDate);
			cnOpEmrRecord.setDateEmr(curDate);
            DataBaseHelper.insertBean(cnOpEmrRecord);			
		}
		//更新患者医保相关信息
		DataBaseHelper.update("update ins_szyb_visit_city set cka303=:cka303,cka304=:cka304,cme320=:cme320,amc021=:amc021,cme331=:cme331,alc005=:alc005,cka305=:cka305 where pk_pv = :pkPv " , pvOp);
		
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		PlatFormSendUtils.sendUpPiInfoMsg(paramMap);
	}

	/**
	 * 医嘱打印查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPrintLs(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		if(mapParam==null)throw new BusException("未传入有效参数");
		if(mapParam.get("pkPv")==null) throw new BusException("未传入患者主键");
		if(mapParam.get("euAlways")==null) throw new BusException("未传入医嘱类型");
		List<Map<String,Object>> printList = cnPubMapper.qryPrint(mapParam);
		return printList;
	}

	/**
	 * 根据医嘱主键查询医嘱的扩展属性值
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOrderAttr(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		if(mapParam==null)throw new BusException("未传入有效参数");
		if(mapParam.get("pkItems")==null) throw new BusException("未传入要查询的医嘱主键");
		//如果是查个体则走该分支
		if(mapParam.get("all") == null || StringUtils.isEmpty(mapParam.get("all").toString())){
			if(mapParam.get("codeAttr")==null) throw new BusException("未传入扩展属性编码");
			List<Map<String,Object>> printList = DataBaseHelper.queryForList("select * from BD_DICTATTR where val_attr ='1' and code_attr=:codeAttr and del_flag='0' and pk_dict IN ("+mapParam.get("pkItems")+")",mapParam);
			return printList;
		}else if(StringUtils.isNotEmpty(MapUtils.getString(mapParam,"batch"))){
			List<Map<String,Object>> printList = DataBaseHelper.queryForList("select TEMP.dt_dicttype as dict_type,TEMP.code_attr,TEMP.name_attr,attb.val_attr,attb.PK_DICT from bd_dictattr_temp temp LEFT JOIN bd_dictattr attb ON attb.pk_dictattrtemp = temp.pk_dictattrtemp AND attb.del_flag = '0' where temp.del_flag = '0' and attb.val_attr is not null and attb.pk_dict IN ("+mapParam.get("pkItems")+")",mapParam);
			return printList;
		}else{
			if(mapParam.get("codeAttr")==null) throw new BusException("未传入扩展属性编码");
			List<Map<String,Object>> printList = DataBaseHelper.queryForList("select TEMP.dt_dicttype as dict_type,TEMP.code_attr,TEMP.name_attr,attb.val_attr,attb.PK_DICT from bd_dictattr_temp temp LEFT JOIN bd_dictattr attb ON attb.pk_dictattrtemp = temp.pk_dictattrtemp AND attb.del_flag = '0' where temp.del_flag = '0' and temp.code_attr=:codeAttr and attb.pk_dict IN ("+mapParam.get("pkItems")+")",mapParam);
			return printList;
		}
	}

	/**
	 * 查询单个药品附加属性
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOrdAttrs(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		if(mapParam==null)throw new BusException("未传入有效参数");
		if(mapParam.get("pkPd")==null) throw new BusException("未传入要查询的医嘱主键");

		StringBuffer sql=new StringBuffer();
		sql.append(" select att.pk_pd,def.code_att,att.val_att from BD_PD_ATT att ");
		sql.append(" INNER JOIN BD_PD_ATT_DEFINE def on DEF.PK_PDATTDEF=att.PK_PDATTDEF ");
		sql.append(" where att.pk_pd=:pkPd and att.DEL_FLAG='0' and def.DEL_FLAG='0' ");
		List<Map<String,Object>> printList = DataBaseHelper.queryForList(sql.toString(),mapParam);
		return printList;
	}

	/**
	 * 更新pv_encounter的desc_epid字段值
	 * @param param
	 * @param user
	 * @return
	 */
	public void patDescEpidSave(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		if(mapParam==null)throw new BusException("未传入有效参数");
		if(mapParam.get("pkPv")==null) throw new BusException("未传入要要更新的就诊主键");
		if(mapParam.get("descEpid")==null) throw new BusException("未传入传入流行病史");
		//更新患者疾病信息
		DataBaseHelper.update("update pv_encounter set desc_epid=:descEpid where pk_pv = :pkPv " ,mapParam);
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
		List<Map<String,Object>> rtn = (List<Map<String, Object>>) cnPubMapper.qryStoreByDept(map);
		return rtn;
	}

	/**
	 * 查询医嘱是否可以作废
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryCanCle(String param,IUser user){

		List<String> ords=(List<String>)JsonUtil.readValue(param, new TypeReference<List<String>>() {});
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("ords",ords);
		List<Map<String,Object>> rtList = cnPubMapper.qryOrdOcc(paramMap);
		return rtList;
	}
    
	/**
	 * 查询医嘱是否已作废
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOrdCan(String param,IUser user){

		List<String> ords=(List<String>)JsonUtil.readValue(param, new TypeReference<List<String>>() {});
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("ords",ords);
		List<Map<String,Object>> rtList = cnPubMapper.qryOrdCan(paramMap);
		return rtList;
	}

	/***
	 * 查询科室业务线交易码
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getLinesBusiness(String param, IUser user) {

		@SuppressWarnings("unchecked")
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		if (mapParam.get("pkDept") == null || mapParam.get("dtButype") == null || mapParam.get("dtDepttype") == null) {
			throw new BusException("参数不正确");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select busa.pk_dept,busa.pk_org,busa.time_begin,busa.time_end,dept.NAME_DEPT as name, dept.CODE_DEPT as code,dept.PY_CODE,dept.D_CODE  ") ;
		sql.append("from bd_dept_bus bus inner join bd_dept_bu bu on bus.pk_deptbu=bu.pk_deptbu ");
		sql.append(" inner join bd_dept_bus busa on bus.pk_deptbu=busa.pk_deptbu  ");
		sql.append(" INNER JOIN BD_OU_DEPT dept on busa.PK_DEPT = dept.pk_dept ");
		sql.append(" where busa.dt_depttype=? and bu.dt_butype= ? and  bus.pk_dept=? ");
		sql.append(" and ( 1=1 ");
		if( mapParam.get("name")!=null && !"".equals(mapParam.get("name").toString())){
			sql.append( "or dept.NAME_DEPT like '%"+mapParam.get("name").toString()+"%'");
		}
		if( mapParam.get("pyCode")!=null && !"".equals(mapParam.get("pyCode").toString())){
			sql.append( "or dept.PY_CODE like '%"+mapParam.get("pyCode").toString()+"%'");
		}
		if( mapParam.get("code")!=null && !"".equals(mapParam.get("code").toString())){
			sql.append( "or dept.CODE_DEPT like '%"+mapParam.get("code").toString()+"%'");
		}
		if( mapParam.get("dcode")!=null && !"".equals(mapParam.get("dcode").toString())){
			sql.append( "or dept.D_CODE like '%"+mapParam.get("dcode").toString()+"%'");
		}
		sql.append(" )");
		sql.append(" and busa.del_flag = '0' and bu.del_flag = '0' and bus.del_flag = '0' ");
		List<Map<String, Object>> resultList = DataBaseHelper.queryForList(sql.toString(),
				mapParam.get("dtDepttype"), mapParam.get("dtButype"), mapParam.get("pkDept"));
		if (resultList == null || resultList.size()==0) throw new BusException("此科室未在业务线维护其对应的科室");
		List<Map<String, Object>> ret=new ArrayList<Map<String, Object>>();
		/** 获取药房增加执行时刻判断 */
		Date nowTime=new Date();
		for (Map<String, Object> result: resultList) {
			Date timeBegin = DateUtils.getDateMorning(nowTime,0);
			Date timeEnd = DateUtils.getDateMorning(nowTime,1);
			if(result.get("timeBegin")!=null ){
				timeBegin = DateUtils.strToDate(DateUtils.getDate() +" "+ result.get("timeBegin").toString(),"yyyy-MM-dd HH:mm:ss");
			}
			if(result.get("timeEnd")!=null){
				timeEnd = DateUtils.strToDate(DateUtils.getDate() +" "+ result.get("timeEnd").toString(),"yyyy-MM-dd HH:mm:ss") ;
			}
			if(DateUtils.getSecondBetween(timeEnd,timeBegin)>0 ){ //开始时间大于结束时间
				if(DateUtils.getHour(nowTime)>DateUtils.getHour(timeEnd))
					timeEnd=DateUtils.getSpecifiedDay(timeEnd,1);
				else
					timeBegin = DateUtils.getSpecifiedDay(timeBegin, -1);
			}

			if(DateUtils.getSecondBetween(timeBegin,nowTime)>=0 && DateUtils.getSecondBetween(nowTime,timeEnd)>=0){
				ret.add(result);
				continue;
			}
		}
		return ret;

	}

	/***
	 * 获取药品库存量
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPdStrockQuan(String param, IUser user) {
		PdStrockVo mapParam = JsonUtil.readValue(param, PdStrockVo.class);
		if(mapParam.getPkDept()==null)
			throw new BusException("科室参数不正确");
		String pkDept=mapParam.getPkDept().toString();

		List<String> pkPds=mapParam.getPkPds();
		if(pkPds==null)
			throw new BusException("药品参数不正确");
		List<Map<String,Object>> rtnqry=cnPubMapper.qryPdQuanByStork(mapParam);
		//数据处理
		List<Map<String,Object>> rtn=new ArrayList<Map<String,Object>>();
		for (String pkPd:pkPds){
			Map<String,Object> map = getMapOrd(pkPd,pkDept,rtnqry);
			rtn.add(map);
		}
		return  rtn;
	}

	/***
	 * 获取药品库存量--多科室
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPdStrocks(String param, IUser user) {
		CnParamDict mapParam = JsonUtil.readValue(param, CnParamDict.class);
		List<PdStrockVo> dictParam=mapParam.getDictParam();

		if(dictParam==null || dictParam.size()==0)
			throw new BusException("参数不正确");

		List<Map<String,Object>> rtnqry=new ArrayList<Map<String,Object>>();

		for (PdStrockVo vo:dictParam){
			rtnqry.add(qryPd(vo));
		}
		//List<Map<String,Object>> rtnqry=cnPubMapper.qryPdStorkByDept(mapParam);
		return  rtnqry;
	}

	private Map<String,Object> qryPd(PdStrockVo vo){
		Map<String,Object> map=new HashMap<String,Object>();
		List<Map<String,Object>> list=cnPubMapper.qryPdStorkByDept(vo);
		if(list==null || list.size()==0){
			map.put("pkPd",vo.getPkPd());
			map.put("pkDept",vo.getPkDept());
			map.put("pkUnit",vo.getPkUnit());
			map.put("stop","1");
		}else{
			map=list.get(0);
		}
		return map;
	}

	public Map<String,Object> getMapOrd(String pkPd,String pkDept,List<Map<String,Object>> pdList){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPd",pkPd);
		map.put("pkDept",pkDept);
		map.put("quan","0");
		map.put("flagStopEr","0");
		map.put("flagStopOp","0");
		map.put("flagStop","0");
		if(pdList!=null && pdList.size()>0){
			for (Map<String,Object> vo : pdList){
				if(vo.get("pkPd")!=null && pkPd.equals(vo.get("pkPd").toString()) ){
					map.put("quan",vo.get("quan").toString());
					map.put("flagStopEr",vo.get("flagStopEr").toString());
					map.put("flagStopOp",vo.get("flagStopOp").toString());
					map.put("flagStop",vo.get("flagStop").toString());
					break;
				}
			}
		}
		return map;
	}

	/**
	 * 查询患者的所有诊断记录--外部接口
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPiDiag(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkPi")==null){
			throw new BusException("患者主键为空！");
		}

		List<Map<String,Object>> rtn = cnPubMapper.qryPiDiag(paramMap);
		for (Map<String, Object> map : rtn) {
			String pkPvdiag = (String)map.get("pkPvdiag");
			List<PvDiagDt> pvdiagDt = cnPubMapper.qryPvdiagDt(pkPvdiag);
			map.put("pvdiagdts", pvdiagDt);
		}
		return rtn;
	}

	/***
	 * 单个药品查询库存量
	 * @param param
	 * @param user
	 * @return
	 */
	public Map qryPdStork(String param, IUser user) {
		PdStrockVo mapParam = JsonUtil.readValue(param, PdStrockVo.class);
		if(mapParam.getPkDept()==null)
			throw new BusException("科室参数不正确");
		if(mapParam.getPkPd()==null)
			throw new BusException("药品参数不正确");

		Map<String,Object> rtnqry=cnPubMapper.qryPdQuanByStorkOne(mapParam);

		return rtnqry;
	}

	public Map qryDeptAttr(String param, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkDept=MapUtils.getString(paramMap,"pkDept");
		if(StringUtils.isBlank(pkDept)){
			throw new BusException("请传入当前科室！");
		}
		String code=MapUtils.getString(paramMap,"code");
		if(StringUtils.isBlank(code)){
			throw new BusException("请传入当前科室属性编码！");
		}
		StringBuffer sql=new StringBuffer();
		sql.append(" select DEPT.PK_DEPT,DEPT.NAME_DEPT,ATTR.CODE_ATTR,ATTR.VAL_ATTR from BD_OU_DEPT dept INNER JOIN bd_dictattr attr on attr.pk_dict = dept.PK_DEPT ");
		sql.append(" where ATTR.CODE_ATTR=? ");
		sql.append(" and dept.pk_dept=? ");
		Map<String,Object> rtnMap=DataBaseHelper.queryForMap(sql.toString(),new Object[]{code,pkDept});
		return rtnMap;
	}

	public void setHpRateFormat(Map<String, Object> paramMap, List<Map<String, Object>> reqOrderlist){
		if (reqOrderlist != null && reqOrderlist.size() > 0 && MapUtils.getObject(paramMap,"hpRate")!=null && org.apache.commons.lang3.StringUtils.isNotBlank(MapUtils.getString(paramMap,"pkHp"))){
			List<String> pkOrds = reqOrderlist.stream().map(vo -> MapUtils.getString(vo, "pkOrd")).collect(Collectors.toList());
			paramMap.put("list", pkOrds);
			List<Map<String, Object>> rateList = cnPubMapper.queryHpRateFormat(paramMap);
			Map<String, List<Map<String, Object>>> reteMap = rateList.stream().collect(Collectors.groupingBy(vo -> MapUtils.getString(vo, "pkOrd")));
			reqOrderlist.stream().forEach(vo -> {
				List<Map<String, Object>> rates = (List<Map<String, Object>>)MapUtils.getObject(reteMap, MapUtils.getString(vo, "pkOrd"));
				if(CollectionUtils.isNotEmpty(rates) && rates.size() ==1){
					//只有一个的时候展示，hh
					vo.put("hpRate", MapUtils.getString(rates.get(0), "hpRate"));
				}
			});
		}
	}

	public List<Map<String, Object>> queryHpRateFormat(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		return cnPubMapper.queryHpRateFormat(paramMap);
	}

	public List<Map<String, Object>> queryPvYlz(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		String sql="";

		String pkPv=MapUtils.getString(paramMap,"pkPv");
		if(StringUtils.isNotEmpty(pkPv)){
			sql="SELECT pv.pk_wg_org,pv.pk_wg FROM pv_encounter pv WHERE pv.pk_pv =?";
			return  DataBaseHelper.queryForList(sql,new Object[]{pkPv});
		}

		String pkDept=MapUtils.getString(paramMap,"pkDept");
		if(StringUtils.isNotEmpty(pkDept)){
			sql="SELECT wg.pk_wg, wg.code_wg,wg.name_wg FROM org_dept_wg wg " +
					"INNER JOIN org_dept_wg_emp wgemp ON wg.pk_wg = wgemp.pk_wg " +
					"WHERE wg.pk_dept =? AND wg.del_flag = '0' ";
			return  DataBaseHelper.queryForList(sql,new Object[]{pkDept});
		}

		return null;
	}

	/**
	 * 更新pv_encounter的cadre_level字段值
	 * @param param
	 * @param user
	 * @return
	 */
	public void savePatcadreLevel(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		if(mapParam==null)throw new BusException("未传入有效参数");
		if(mapParam.get("pkPv")==null) throw new BusException("未传入要要更新的就诊主键！");
		if(!mapParam.containsKey("cadreLevel")) throw new BusException("未传入传干部级别！");
		//更新患者干部级别
		DataBaseHelper.update("update pv_encounter set cadre_level=:cadreLevel where pk_pv =:pkPv " ,mapParam);
	}
}
