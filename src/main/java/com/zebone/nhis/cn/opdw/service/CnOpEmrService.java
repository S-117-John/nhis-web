package com.zebone.nhis.cn.opdw.service;

import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.support.ApplicationUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.opdw.vo.CnOpEmrRecordVo;
import com.zebone.nhis.cn.opdw.vo.CnOpEmrTempVo;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrExRecord;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.cn.opdw.PvDocEx;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;



@Service
public class CnOpEmrService {
	

	 /**
	  * 查询当前患者门诊病历
	  * @param param
	  * @param user
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public CnOpEmrRecordVo qryEmrForPkPv(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		 
		 String sql = "select * from cn_emr_op emr where emr.pk_pv = '"+pkpv+"'";
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		 
		 CnOpEmrRecordVo ret = null;	
		 for(Map<String,Object> map : ps){
			 ret = new CnOpEmrRecordVo();
			 BeanUtils.copyProperties(ret, map);
		 }
		 
		 if(null != ret){
			 Map<String,Object> m = DataBaseHelper.queryForMapFj("select p.flag_first from PV_OP p where p.pk_pv=?", new Object[]{pkpv});
			 ret.setFlagFirst(MapUtils.getString(m,"flagFirst","1"));
		 }
		 
		 return ret;		 
	 }
	 
	 /**
	  * 查询当前可用模板
	  * @param param
	  * @param user
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public List<CnOpEmrTempVo> qryEmrTemp(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String euRange = JsonUtil.getFieldValue(param, "euRange");
		 String val = JsonUtil.getFieldValue(param, "val");
		 
		 String sql = "select t.*,cate.name as cate_name,d.diagname as diag_name from bd_opemr_tempcate cate inner join bd_opemr_temp t on t.pk_tempcate = cate.pk_tempcate "
		 		    + " left outer join bd_term_diag d on t.pk_diag=d.pk_diag ";
		 
		 if(StringUtils.isEmpty(euRange)){
			 return execQryTempCate(sql);
		 }
		 
		 if(StringUtils.isEmpty(val)){
			 return execQryTempCate(sql);
		 }
		 
		 if("0".equals(euRange)){
			 sql += " where cate.pk_org='"+val+"' and  cate.eu_range=0 AND t.FLAG_ACTIVE = '1'";
			 return execQryTempCate(sql);
		 }
		 
		 if("1".equals(euRange)){
			 sql += " where cate.pk_dept='"+val+"' and cate.eu_range=1 AND t.FLAG_ACTIVE = '1'";
			 return execQryTempCate(sql);
		 }
		 
		 if("2".equals(euRange)){
			 sql += " where cate.pk_emp='"+val+"' and cate.eu_range=2 AND t.FLAG_ACTIVE = '1'";
			 return execQryTempCate(sql);
		 }		
		 
		 return null;		 
	 }
	 
	 /**
	  * 执行查询当前用户的门诊模板
	  * @param sql
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 private List<CnOpEmrTempVo> execQryTempCate(String sql) throws IllegalAccessException, InvocationTargetException{
		 sql += " order by t.ts";
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		 
		 List<CnOpEmrTempVo> ret = new ArrayList<CnOpEmrTempVo>();		 
				 
		 for(Map<String,Object> map : ps){
			 CnOpEmrTempVo boe = new CnOpEmrTempVo();
			 BeanUtils.copyProperties(boe, map);	
			 
			 ret.add(boe);
		 }
		 
		 return ret;
	 }
	 
	 /**
	  * 保存更新门诊病历
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public CnOpEmrRecord  saveOpEmr(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
	 	CnOpEmrRecordVo boeVo = JsonUtil.readValue(param,CnOpEmrRecordVo.class);
		User u = (User)user;
		 CnOpEmrRecord boe = new CnOpEmrRecord();
		 BeanUtils.copyProperties(boe,boeVo);
		if(StringUtils.isEmpty(boe.getPkEmrop())){
		    boe.setCreateTime(new Date());
		    
		    boe.setCreator(u.getPkEmp()); 
		    boe.setDateEmr(new Date());
			DataBaseHelper.insertBean(boe);
		}
		else{
			boe.setModifier(u.getPkEmp());
			boe.setModityTime(new Date());
			DataBaseHelper.updateBeanByPk(boe,false);
		}
		if(StringUtils.isNotBlank(boeVo.getFlagFirst()) && StringUtils.isNotBlank(boeVo.getPkPv())){
			DataBaseHelper.update("update pv_op set flag_first=? where pk_pv=?",new Object[]{boeVo.getFlagFirst(),boeVo.getPkPv()});
		}
		return boe;
	 }
	 
	 /**
	  * 保存更新门诊病历---预写
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public CnOpEmrExRecord  saveOpEmrEx(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
	 	CnOpEmrRecordVo boeVo = JsonUtil.readValue(param,CnOpEmrRecordVo.class);
		User u = (User)user;
		 CnOpEmrExRecord boe = new CnOpEmrExRecord();
		 BeanUtils.copyProperties(boe,boeVo);
		if(StringUtils.isEmpty(boe.getPkEmrop())){
		    boe.setCreateTime(new Date());
		    boe.setCreator(u.getPkEmp()); 
		    boe.setDateEmr(new Date());
		    boe.setDelFlag("0");
		    PvDocEx docEx=new PvDocEx();
		    if(boe.getPvDoc()!=null && boe.getPvDoc().getPkPvdoc()==null) {
				BeanUtils.copyProperties(docEx,boe.getPvDoc());
				docEx.setDelFlag("0");
				DataBaseHelper.insertBean(docEx);
			}
		    boe.setPkEvent(docEx.getPkPvdoc());
			DataBaseHelper.insertBean(boe);
			
		}
		else{
			boe.setModifier(u.getPkEmp());
			boe.setModityTime(new Date());
			DataBaseHelper.updateBeanByPk(boe,false);
			if(boe.getPvDoc()!=null && boe.getPvDoc().getPkPvdoc()!=null) {
				DataBaseHelper.updateBeanByPk(boe.getPvDoc(),false);
			}
		}
		return boe;
	 }
	 
	 public List<CnOpEmrExRecord> qryOpEmrEx(String param, IUser user){
		 Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		 String sql="select ex.*,docex.*,dept.NAME_DEPT from cn_emr_op_ex ex left join BD_OU_DEPT dept on ex.PK_DEPT=dept.PK_DEPT left join pv_doc_ex docex on ex.PK_EVENT=docex.PK_PVDOC where ex.pk_pi=? and ex.CREATE_TIME>=? and ex.CREATE_TIME<? and ex.del_flag='0'";
		 List<CnOpEmrExRecord> list=DataBaseHelper.queryForList(sql, CnOpEmrExRecord.class,new Object[]{map.get("pkPi"),map.get("start"),map.get("end")});
		 for (CnOpEmrExRecord cnOpEmrExRecord : list) {
			if(cnOpEmrExRecord.getPkEvent()!=null) {
				String docSql="select * from PV_DOC_EX where PK_PVDOC=?";
				 PvDocEx docEx=DataBaseHelper.queryForBean(docSql, PvDocEx.class, new Object[]{cnOpEmrExRecord.getPkEvent()});
				 cnOpEmrExRecord.setPvDoc(docEx);
			}
		 }
		 return list;
	 }
	 
	 /**
	  * 查询病人历史病历
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public List<CnOpEmrRecordVo>  qryOpEmrForPeroid(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String pkpi = JsonUtil.getFieldValue(param,"pkpi");
		 String start = JsonUtil.getFieldValue(param,"startd");
		 String end = JsonUtil.getFieldValue(param,"endd");
		 String sql = "select emr.*,diag.desc_diag as dmiss_diag, org.name_org,dept.name_dept,pv.date_begin as date_clinic from cn_emr_op emr left join pv_diag diag on diag.pk_pv=emr.pk_pv "
				    + " inner join bd_ou_org org on org.pk_org = emr.pk_org inner join bd_ou_dept dept on dept.pk_dept = emr.pk_dept"
				    + "  inner join pv_encounter pv on pv.pk_pv=emr.pk_pv "
		 		    + " where emr.pk_pi='"+pkpi+"' and ";
		 sql = sql + " emr.date_emr >= to_date('"+start+"','YYYYMMDDHH24MISS') and "+" emr.date_emr<to_date('"+end+"','YYYYMMDDHH24MISS')";

		 List<Map<String,Object>> ds = DataBaseHelper.queryForList(sql);
		 List<CnOpEmrRecordVo> ret = new ArrayList<CnOpEmrRecordVo>();
		 
		 for(Map<String,Object> map : ds){
			 CnOpEmrRecordVo coe = new CnOpEmrRecordVo();
			 BeanUtils.copyProperties(coe, map);	
			 
			 ret.add(coe);
		 }		 
		 
		 return ret;
	 }
	 
	 public static Date addDate(Date date,long day) throws ParseException{
		 long time = date.getTime();
		 day = day*24*60*60*1000; 
		 time+=day;
		 return new Date(time);
	 }
	 
	 /**
	  * 查询门诊病历打印信息
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public List<CnOpEmrRecordVo>  qryOpEmrRecPrint(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String pkPv = JsonUtil.getFieldValue(param,"pkPv");
		 
		 String sql = " select pi.code_pi,"+
				 	" pi.name_pi,"+
				 	" sex.name sex,"+
				 	" pv.age_pv,"+
				 	" pv.date_begin,"+
				 	" dept.name_dept,"+
				 	" org.name_org,"+
				 	" pv.name_emp_phy,"+
				 	" diag.desc_diag,"+
				 	" emr.problem,"+
				 	" emr.present,"+
				 	" emr.history,"+
				 	" emr.allergy,"+
				 	" emr.height,"+
				 	" emr.weight,"+
				 	" emr.temperature,"+
				 	" emr.sbp,"+
				 	" emr.dbp,"+
				 	" emr.exam_phy,"+
				 	" emr.exam_aux,"+
				 	" emr.note "+
			  " from pi_master pi"+
			  " inner join pv_encounter pv on pi.pk_pi=pv.pk_pi"+
			  " inner join bd_defdoc sex on pi.dt_sex=sex.code and sex.code_defdoclist='000000'"+
			  " inner join bd_ou_dept dept on pv.pk_dept=dept.pk_dept"+
			  " inner join bd_ou_org org on pv.pk_org=org.pk_org"+
			  " left outer join pv_diag diag on pv.pk_pv=diag.pk_pv and diag.flag_maj=1"+
			  " inner join cn_emr_op emr on pv.pk_pv=emr.pk_pv"+
			  " where pv.pk_pv='"+pkPv+"'";
		 List<Map<String,Object>> list = DataBaseHelper.queryForList(sql);
		 List<CnOpEmrRecordVo> rtnList = new ArrayList<CnOpEmrRecordVo>();
		 CnOpEmrRecordVo vo = new CnOpEmrRecordVo();
		 CnOpEmrRecordVo ord = new CnOpEmrRecordVo();
		 if(list!=null&&list.size()>0){
			 BeanUtils.copyProperties(vo, list.get(0));	
		 }
		 if(vo.getWeight()!=null&&vo.getWeight().doubleValue()>0&&vo.getHeight()!=null&&vo.getHeight().doubleValue()>0){
			 double tmp=(new Double(vo.getHeight()) / 100) * (new Double(vo.getHeight()) / 100);
			 double bmi = new Double(vo.getWeight()) / tmp;
			 vo.setBmi(bmi);
		 }
		 //诊疗措施-处方
		 sql = "select pres.pres_no,"+
				 	" ord.name_ord,"+
				 	" ord.dosage,"+
				 	" unit.name unit,"+
				 	" sup.name supply,"+
				 	" freq.name freq,"+
				 	" ord.days,"+
				 	" ord.ordsn,"+
				 	" ord.ordsn_parent "+
			  " from cn_prescription pres"+
			  " inner join cn_order ord on pres.pk_pres=ord.pk_pres"+
			  " inner join bd_unit unit on ord.pk_unit_dos=unit.pk_unit"+
			  " inner join bd_term_freq freq on ord.code_freq=freq.code"+
			  " inner join bd_supply sup on ord.code_supply=sup.code"+
			  " inner join cn_emr_op emr on pres.pk_pv=emr.pk_pv"+
			  " where pres.pk_pv='"+pkPv+"'";
		 list = DataBaseHelper.queryForList(sql);
		 for(Map<String,Object> map : list){
			 ord = new CnOpEmrRecordVo();
			 BeanUtils.copyProperties(ord, vo);	
			 BeanUtils.copyProperties(ord, map);	
			 rtnList.add(ord);
		 }		
		 //诊疗措施-检治
		 sql = "select ord.name_ord,"+
				 	//" ord.quan as dosage,"+
				 	" round(ord.quan,3) as dosage,"+
				 	" round(ord.QUAN,3) AS quan," +
				 	//" ord.quan,"+
				 	" ord.days,"+
				 	" freq.name freq,"+
				 	" unit.NAME unit "+
			  " from cn_order ord "+
			  " inner join bd_term_freq freq on ord.code_freq=freq.code"+
			  " LEFT  JOIN BD_UNIT unit ON ord.PK_UNIT_DOS = unit.PK_UNIT "+
			  " where ord.pk_pv='"+pkPv+"' AND ord.FLAG_DURG = '0'";
		 list = DataBaseHelper.queryForList(sql);
		 for(Map<String,Object> map : list){
			 ord = new CnOpEmrRecordVo();
			 BeanUtils.copyProperties(ord, vo);	
			 BeanUtils.copyProperties(ord, map);	
			 rtnList.add(ord);
		 }	
		 
		 return rtnList;
	 }
	 
	 
	 /**
	  * 删除门诊病历
	  * @param param
	  * @param user
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public void delOpEmr(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
		 String pkEmrop = JsonUtil.getFieldValue(param, "pkEmrop");
		 String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		 String pkDoc = JsonUtil.getFieldValue(param, "pkDoc");
		 String hosCode = ApplicationUtils.getPropertyValue("emr.hos.code", "");
		 //if (hosCode != null && hosCode.equals("szkq")) {
			 if (StringUtils.isNotBlank(pkEmrop)) {
				 CnOpEmrRecord coe = new CnOpEmrRecord();
				 coe.setPkEmrop(pkEmrop);
				 coe.setDelFlag("1");
				 DataBaseHelper.updateBeanByPk(coe, false);
			 } else if (StringUtils.isNotBlank(pkPv)) {
				 String sql = "update cn_emr_op set del_flag='1' where  pk_pv = '" + pkPv + "' AND PK_DOC = '"+pkDoc+"'";
				 DataBaseHelper.execute(sql);
			 }
//		 } else {
//			 if (StringUtils.isNotBlank(pkEmrop)) {
//				 CnOpEmrRecord coe = new CnOpEmrRecord();
//				 coe.setPkEmrop(pkEmrop);
//				 DataBaseHelper.deleteBeanByPk(coe);
//			 } else if (StringUtils.isNotBlank(pkPv)) {
//				 String sql = "delete from cn_emr_op where pk_pv='" + pkPv + ",";
//				 DataBaseHelper.execute(sql);
//			 }
//		 }
	 }

	/**
	 * 查询当前患者是否有门诊病历
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public int qryEmrPv(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String empType=JsonUtil.getFieldValue(param,"empType");

		List<Map<String,Object>> ps = new ArrayList<Map<String,Object>>();
		if(!StringUtils.isBlank(empType) && "2".equals(empType)){ //简易病历
			String sql = "select * from cn_emr_op emr where emr.pk_pv = '"+pkpv+"'";
			ps= DataBaseHelper.queryForList(sql);
		}else if(!StringUtils.isBlank(empType) && "1".equals(empType)){ //都昌病历
			String sql = "SELECT * FROM PV_DOC WHERE pk_pv = '"+pkpv+"'";
			ps= DataBaseHelper.queryForList(sql);
		}

		return ps.size();
	}
}
