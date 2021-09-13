package com.zebone.nhis.scm.opds.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.scm.pub.dao.SchOpdsPreMapper;
import com.zebone.nhis.scm.pub.support.OpDrugPubUtils;
import com.zebone.nhis.scm.pub.vo.OpPreRtnVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ScmOpPreDispenseService {
	
	
	@Autowired
	private SchOpdsPreMapper schOpdsPreMapper;

	/**
	 * 查询未配药_患者信息
	 * @param param pk_dept winno
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPrePatis(String param, IUser user) {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param,Map.class);
		String pkDept = (String)paraMap.get("pkDept");
		String winno  = (String)paraMap.get("winno");
		String status = (String)paraMap.get("status");
		String begin  = (String)paraMap.get("dateBegin");
		String end  = (String)paraMap.get("dateEnd");
		
		StringBuffer sql = new StringBuffer(" select distinct max(pres.date_reg) date_reg,pi.pk_pi,pi.code_op, pi.code_pi, pi.name_pi ");
		sql.append(" from pi_master pi inner join ex_pres_occ pres on pi.pk_pi=pres.pk_pi");
		sql.append(" where pres.flag_cg=1 and pres.pk_dept_ex=? and pres.flag_conf=0 and pres.flag_canc=0 and  pres.winno_prep=? and pres.eu_status!=9");
		    
		Object [] args = null; 
		if(EnumerateParameter.ZERO.equals(status)){
			//sql.append(" and pres.flag_prep=0 and pres.flag_susp=0");
			sql.append(" and pres.flag_prep=0 ");//未完成包含挂起
			args =new Object[] {pkDept,winno};
		}else if(EnumerateParameter.ONE.equals(status)){
			sql.append(" and pres.flag_prep=1 and pres.date_prep>=? and pres.date_prep<=? ");
			args =new Object[] {pkDept,winno,dateTrans(begin.substring(0, 8)),dateTrans(begin.substring(0, 8)+"235959")};
		}else{
			sql.append(" and pres.flag_prep=0 and pres.flag_susp=1");
			args =new Object[] {pkDept,winno};
		}		
		sql.append(" group by pi.pk_pi, pi.code_op, pi.code_pi, pi.name_pi order by date_reg");
		//2.患者信息				
		return DataBaseHelper.queryForList(sql.toString(), args);
	}
	
	/**
	 * 查询处方信息
	 * @param param pk_dept winno
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPres(String param, IUser user) {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param,Map.class);
		String pkDept = (String)paraMap.get("pkDept");
		String winno  = (String)paraMap.get("winno");
		String pkPi   = (String)paraMap.get("pkPi");
		String status = (String)paraMap.get("status");
		Date begin  = paraMap.get("dateBegin")==null?null:dateTrans(((String)paraMap.get("dateBegin")).substring(0,8));
		Date end  = paraMap.get("dateEnd")==null?null:dateTrans(((String)paraMap.get("dateEnd")).substring(0,8)+"235959");
		
		StringBuffer sql = new StringBuffer("select pres.winno_conf,pres.pk_presocc,pres.pk_pres,pres.pres_no,pres.date_pres,");
		sql.append(" pres.pk_dept_pres,dept.name_dept,pres.name_emp_pres,diag.diagname");
		sql.append(" from ex_pres_occ pres");
		sql.append(" inner join bd_ou_dept dept on pres.pk_dept_pres=dept.pk_dept");
		sql.append(" left outer join bd_term_diag diag on pres.pk_diag=diag.pk_diag");
		sql.append(" where pres.pk_pi=? and  pres.pk_dept_ex=? and pres.winno_prep=? and pres.flag_cg=1 and  pres.flag_conf=0 and pres.flag_canc=0 and pres.eu_status!=9");
	    
		Object [] args = null; 
		if(EnumerateParameter.ZERO.equals(status)){
			sql.append(" and pres.flag_prep=0");
			args =new Object[] {pkPi,pkDept,winno};
			
		}else if(EnumerateParameter.ONE.equals(status)){
			sql.append(" and pres.flag_prep=1 and pres.date_prep>=? and pres.date_prep<=? ");
			args =new Object[] {pkPi,pkDept,winno,begin,end};
		}else{
			sql.append(" and pres.flag_prep=0 and pres.flag_susp=1");
			args =new Object[] {pkPi,pkDept,winno};
		}		
		//2.处方信息				
		return DataBaseHelper.queryForList(sql.toString(), args);
	}
	
	/**
	 * 查询处方明细信息
	 * @param param pk_dept winno
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<OpPreRtnVo> qryPresDt(String param, IUser user) {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param,Map.class);
		String pkPresocc = (String)paraMap.get("pkPresocc");
		String pkStroe = (String)paraMap.get("pkStore");
		//2.处方明细信息
		List<OpPreRtnVo> res = null;
		if(StringUtils.hasText(pkPresocc)){
			res = schOpdsPreMapper.DispensingPresDt(pkStroe,pkPresocc);
		}
		return res;
	}

	
	/**
	 * 配药
	 * @param param
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public ExPresOcc preDispensing(String param, IUser user) throws Exception {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		User userInfo = (User)user;
		String pkPresocc = (String)paraMap.get("pkPresocc");//处方执行主键
		Date datePrep = paraMap.get("datePrep")==null?null:dateTrans((String)paraMap.get("datePrep"));//配药日期
		String pkDept = (String)paraMap.get("pkDept");//当前科室
		String winno = (String)paraMap.get("winno");//当前窗口
		
		//2.调用电子药柜接口，为每个药品分配药框编号；
		setBoxCode();
			
		//3.分配发药窗口
		Map<String,Object> winMap = OpDrugPubUtils.getWin(1,pkDept);
		String winnoOut = (String)winMap.get("code");
		
		//4.更新发药窗口工作量（增加处方数） 更新配药窗口工作量（减少处方数）
		OpDrugPubUtils.updWinVol(1,pkDept,winnoOut,1);
		OpDrugPubUtils.updWinVol(0,pkDept,winno,-1);
		
		//5.更新处方执行单；
		StringBuffer sql = new StringBuffer(" update ex_pres_occ set flag_susp=0, flag_prep=1, ");
		             sql.append(" date_prep=?, pk_emp_prep=?, name_emp_prep=?,");
		             sql.append(" eu_status=2, winno_conf=?   where pk_presocc=? and  flag_prep=0");
		 DataBaseHelper.execute(sql.toString(), new Object[]{datePrep,userInfo.getPkEmp(),userInfo.getNameEmp(),winnoOut,pkPresocc});		 
		
		 return  DataBaseHelper.queryForBean("SELECT * FROM ex_pres_occ WHERE pk_presocc=?", ExPresOcc.class, pkPresocc);
	}
	
	/**
	 * 配药 - 多条处方同时配药
	 * @param param
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<ExPresOcc> preDispensingByPati(String param, IUser user) throws Exception {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		User userInfo = (User)user;
		List<String> pkPresocc = (List<String>)paraMap.get("pkPresoccs");//处方执行主键
		Date datePrep = paraMap.get("datePrep")==null?null:dateTrans((String)paraMap.get("datePrep"));//配药日期
		String pkDept = (String)paraMap.get("pkDept");//当前科室
		String winno = (String)paraMap.get("winno");//当前窗口
		
		//2.调用电子药柜接口，为每个药品分配药框编号；
		setBoxCode();
			
		//3.分配发药窗口 - 当传入目标发药窗口为空时，获取新的发药窗口
		Map<String,Object> winMap = OpDrugPubUtils.getWin(1,pkDept);
		String winnoOut = (String)winMap.get("code");
		
		//4.更新发药窗口工作量（增加处方数） 更新配药窗口工作量（减少处方数）
		OpDrugPubUtils.updWinVol(1,pkDept,winnoOut,pkPresocc.size());
		OpDrugPubUtils.updWinVol(0,pkDept,winno,-pkPresocc.size());
		String pks = "(";
		for (String str : pkPresocc) {
			pks += "'"+ str +"',";
		}
		pks = pks.substring(0,pks.length() - 1);
		pks += ")";
		//5.更新处方执行单；
		StringBuffer sql = new StringBuffer(" update ex_pres_occ set flag_susp=0, flag_prep=1, ");
		             sql.append(" date_prep=?, pk_emp_prep=?, name_emp_prep=?,");
		             sql.append(" eu_status=2, winno_conf=?   where pk_presocc in "+ pks +" and  flag_prep=0");
		 DataBaseHelper.execute(sql.toString(), new Object[]{datePrep,userInfo.getPkEmp(),userInfo.getNameEmp(),winnoOut});		 
		
		 return  DataBaseHelper.queryForList("SELECT * FROM ex_pres_occ WHERE pk_presocc in " + pks , ExPresOcc.class, new Object[]{});
	}

	/**
	 * 调用电子药柜接口，为每个药品分配药框编号；
	 */
	private void setBoxCode() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 取消配药
	 * @param param
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void cancelPreDispensing(String param, IUser user) throws Exception {
		
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPresocc = (String)paraMap.get("pkPresocc");
		String pkDept = (String)paraMap.get("pkDept");
		
		ExPresOcc presVo = DataBaseHelper.queryForBean(
				"select * from ex_pres_occ where pk_presocc = ?",
				ExPresOcc.class, pkPresocc);
		
		String winnoPrep = presVo.getWinnoPrep();
		String winnoConf = presVo.getWinnoConf();
		
		//1.更新配药窗口工作量（增加处方数）更新发药窗口工作量（减少处方数）
		OpDrugPubUtils.updWinVol(1,pkDept,winnoConf,-1);
		OpDrugPubUtils.updWinVol(0,pkDept,winnoPrep,1);

		//2.更新处方执行单
		StringBuffer sql = new StringBuffer(" update ex_pres_occ  set flag_prep=0, date_prep=null,pk_emp_prep=null,");
		  			 sql.append(" name_emp_prep=null, eu_status=1, winno_conf=null");	
		             sql.append( " where pk_presocc=? and flag_prep=1  and flag_conf=0");		 
		 DataBaseHelper.execute(sql.toString(), new Object[]{pkPresocc});            
	}

	
	/**
	 * 转发
	 * @param param
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void transfer(String param, IUser user) throws BusException {
		
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkDept = (String)paraMap.get("pkDept");
		String winnoOut = (String)paraMap.get("winnoOut");
		String winnoIn = (String)paraMap.get("winnoIn");
		String pkPresocc= (String)paraMap.get("pkPresocc");
		
		//1	.转发校验，当前窗口必须是下线状态；
	    if(!offLineCheck(pkDept,winnoOut)){
	    	throw new BusException("当前窗口仍然在线，禁止转发！");
	    }
		//2.更新转入窗口工作量（增加处方数），更新转出窗口工作量（减少处方数），见3.9.6.6；
	    OpDrugPubUtils.updWinVol(0,pkDept,winnoIn,1);
	    OpDrugPubUtils.updWinVol(0,pkDept,winnoOut,-1);
		//3.更新处方执行单
		DataBaseHelper.execute(" update ex_pres_occ  set winno_prep=?  where pk_presocc=? and winno_prep=? and flag_prep=0", 
				new Object[]{winnoIn,pkPresocc,winnoOut});

	}

	/**
	 * 转发 - 多条处方同时转发
	 * @param param
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void transferByWin(String param, IUser user) throws BusException {
		
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkDept = (String)paraMap.get("pkDept");
		String winnoOut = (String)paraMap.get("winnoOut");
		String winnoIn = (String)paraMap.get("winnoIn");
		List<String> pkPresocc = (List<String>)paraMap.get("pkPresocc");//处方执行主键
		if(null == pkPresocc || pkPresocc.size() < 1)
			throw new BusException("当前窗口无需要转发的处方！");
		
		//1	.转发校验，当前窗口必须是下线状态；
	    if(!offLineCheck(pkDept,winnoOut)){
	    	throw new BusException("当前窗口仍然在线，禁止转发！");
	    }
	    
		//2.更新转入窗口工作量（增加处方数），更新转出窗口工作量（减少处方数），见3.9.6.6；
	    OpDrugPubUtils.updWinVol(0,pkDept,winnoIn,pkPresocc.size());
	    OpDrugPubUtils.updWinVol(0,pkDept,winnoOut,-pkPresocc.size());
	    
		//3.更新处方执行单
	    String pks = "(";
		for (String str : pkPresocc) {
			pks += "'" + str + "',";
		}
		pks = pks.substring(0,pks.length() - 1);
		pks += ")";
		DataBaseHelper.execute(" update ex_pres_occ  set winno_prep=?  where pk_presocc in "+ pks +" and winno_prep=? and flag_prep=0", 
				new Object[]{winnoIn,winnoOut});

	}


	private boolean offLineCheck(String pkDept, String winnoOut) {
		StringBuffer sql= new StringBuffer(" select du.flag_online from bd_dept_unit du where du.pk_dept=? and du.code=?");
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sql.toString(), new Object[]{pkDept,winnoOut});
		if(list!=null && list.size()>0){
			return !"1".equals((String)list.get(0).get("flagOnline"));
		}else{
			return false;
		}
		
	}

//	/**
//	 * 更新窗口业务量
//	 * @param type 类型 （0配药 1发药）
//	 * @param pkDept 科室
//	 * @param winno 窗口号
//	 * @param val 增量 val（正数为增加，负数为减少）
//	 * @throws Exception 
//	 */
//	public void updWinVol(int type,String pkDept,String winno,int val) throws BusException{
//		
//		if(StringUtils.hasText(winno) && StringUtils.hasText(pkDept)){
//			StringBuffer sql = new StringBuffer("update bd_dept_unit du set du.cnt_bu=du.cnt_bu+?"); 
//		     sql.append(" where du.eu_unittype=1 and");
//			 sql.append(" du.eu_butype=? and");	
//			 sql.append(" du.pk_dept=? and");
//			 sql.append(" du.code=?");
//           DataBaseHelper.execute(sql.toString(), new Object[]{val,type,pkDept,winno});
//		}else{
//			throw new BusException("更新窗口业务量异常,参数缺失！");
//		}
//	}
//	
//	
//	/**
//	 * 分配窗口
//	 * @param type   类型 type （0配药 1发药）
//	 * @param pkDept 科室
//	 * @param winno  窗口号
//	 * @return
//	 */
//	public Map<String,Object> getWin(int type,String pkDept){
//		
//		Map<String,Object> res = new HashMap<String,Object>();
//		if(StringUtils.hasText(pkDept)){
//			StringBuffer sql = new StringBuffer(" select du.code,min(du.cnt_bu) val from bd_dept_unit du where du.eu_unittype=1");
//			sql.append(" and du.pk_dept=?");
//			sql.append(" and du.eu_butype=? group by du_code");		
//            List<Map<String,Object>> result = DataBaseHelper.queryForList(sql.toString(), new Object[]{pkDept,type});
//            if(result !=null && result.size()>0){
//            	res = result.get(0);
//            }
//		}
//		return res;
//	}
	
	public Date dateTrans(String date) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if(StringUtils.hasText(date)){
			try {
				date = date.length()==8?date+"000000":date;		
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return temp;
	}
}
