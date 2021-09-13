package com.zebone.nhis.scm.opds.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccPddt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.nis.ns.support.ExlistPrintSortByOrdUtil;
import com.zebone.nhis.scm.opds.vo.ExPresOccVO;
import com.zebone.nhis.scm.opds.vo.ParamVo;
import com.zebone.nhis.scm.opds.vo.PresDtPrintInfo;
import com.zebone.nhis.scm.opds.vo.PresPrintInfo;
import com.zebone.nhis.scm.pub.dao.SchOpdsPreMapper;
import com.zebone.nhis.scm.pub.service.PdStOutBatchPubService;
import com.zebone.nhis.scm.pub.support.OpDrugPubUtils;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ScmOpDispenseService {	
	@Autowired
	private SchOpdsPreMapper schOpdsPreMapper;
	/**
	 * 查询患者信息
	 * @param param 
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
		
		StringBuffer sql = new StringBuffer(" select distinct  pi.pk_pi,pi.code_pi,pi.code_op,pi.name_pi,max(pres.date_reg) date_reg from pi_master pi");
		sql.append(" inner join ex_pres_occ pres on pi.pk_pi=pres.pk_pi ");
		sql.append(" where pres.flag_cg=1 and pres.pk_dept_ex=? and pres.winno_conf=? and pres.flag_canc=0 and pres.eu_status!=9");
		    
		Object [] args = null; 
		if(EnumerateParameter.ZERO.equals(status)){
			//sql.append(" and pres.flag_conf=0 and pres.flag_susp=0");
			sql.append(" and pres.flag_conf=0 ");//未完成包含暂挂
			args =new Object[] {pkDept,winno};
			
		}else if(EnumerateParameter.ONE.equals(status)){
			sql.append(" and pres.flag_conf=1 and pres.date_conf>=? and pres.date_conf<=? ");
			args =new Object[] {pkDept,winno,dateTrans(begin.substring(0, 8)),dateTrans(begin.substring(0, 8)+"235959")};
		}else{
			sql.append(" and pres.flag_conf=0 and pres.flag_susp=1");
			args =new Object[] {pkDept,winno};
		}		
		sql.append(" group by pi.pk_pi, pi.code_pi,pi.code_op,pi.name_pi order by date_reg");
		//2.患者信息				
		return DataBaseHelper.queryForList(sql.toString(), args);
	}
  /**
   * 门诊发药 （内部方法，不允许直接被交易码调用）
   * @param pkorgAp 请领机构
   * @param pkDeptAp 请领科室
   * @param pkDept
   * @param pkStore
   * @param pkPresocc
   * @param flagPresDts
   * @param winno
   * @param pdStOutPubService
   * @param pdOutParams
   */
	protected void dispend(String pkOrgAp,String pkDeptAp,String pkDept,String pkStore,String pkPresocc,Map<String,ExPresOccDt> flagPresDts,
			String winno,PdStOutBatchPubService pdStOutBatchPubService,Map<String,List<PdOutParamVo>> Out4Upd,List<PdOutParamVo> param4Out){
		    //1.调用供应链接口
	 		List<PdOutDtParamVo> pdOutRes = pdStOutBatchPubService.execStOut(param4Out,pkOrgAp,pkDeptAp, pkDept,pkStore,null,false);
	 		//2.拆分药品
	 		for(PdOutDtParamVo vo : pdOutRes){
	 			if(Out4Upd.get(vo.getPkPd()).size()>1){
	 				for(int i = 0;i<Out4Upd.get(vo.getPkPd()).size()-1;i++){
	 					PdOutParamVo paraTemp = Out4Upd.get(vo.getPkPd()).get(i);
	 					PdOutDtParamVo voTemp = (PdOutDtParamVo) vo.clone();
	 					voTemp.setQuanOutMin(paraTemp.getQuanMin());
	 					voTemp.setQuanOutPack(paraTemp.getQuanPack());
	 					pdOutRes.add(voTemp);
	 					vo.setQuanOutMin(vo.getQuanOutMin()-voTemp.getQuanOutMin());
	 					vo.setQuanOutPack(vo.getQuanOutPack()-voTemp.getQuanOutPack());
	 				}
	 			}
	 		}
	 		
	 		//3.更新处方执行单ex_pres_occ；
	 		Map<String,Object> updParam = new HashMap<String,Object>();
	 		updParam.put("flagConf", "1");
	 		updParam.put("dateConf", new Date());
	 		updParam.put("pkEmpConf", UserContext.getUser().getPkEmp());
	 		updParam.put("nameEmpConf",UserContext.getUser().getNameEmp());
	 		updParam.put("euStatus", "3");
	 		updParam.put("pkPresocc", pkPresocc);
	 		schOpdsPreMapper.updExPresOcc(updParam);
	 		
	 		//4.更新处方明细ex_pres_occ_dt；
	 		List<ExPresOccDt> presDtForUpd = new ArrayList<ExPresOccDt>();
	 		Map<String,List<PdOutDtParamVo>> flagOutRes = new HashMap<String,List<PdOutDtParamVo>>();
	 		for(PdOutDtParamVo vo : pdOutRes){
	 			List<PdOutDtParamVo> temps = flagOutRes.get(vo.getPkPdapdt())!=null?flagOutRes.get(vo.getPkPdapdt()):new ArrayList<PdOutDtParamVo>(); 
	 		    temps.add(vo);
	 		    flagOutRes.put(vo.getPkPdapdt(), temps);	
	 		}
	 		
	        for(Entry<String,List<PdOutDtParamVo>> e:flagOutRes.entrySet()){
	     	   ExPresOccDt presDt = flagPresDts.get(e.getKey());
	     	   List<PdOutDtParamVo> resOut = e.getValue();
	     	   double quanDe = 0.0; 
	     	   double amountDe = 0.0;
	     	   for(PdOutDtParamVo vo : resOut){
	     		   quanDe = MathUtils.add(quanDe, vo.getQuanOutPack());
	     		   Double priceTemp = MathUtils.mul(MathUtils.div(vo.getPrice(), vo.getPackSizePd().doubleValue()),vo.getPackSize().doubleValue());
	     		   Double temp = MathUtils.mul(presDt.getQuanCg()==0?1:presDt.getQuanCg(), priceTemp);
	     		   //temp = MathUtils.mul(temp,vo.getQuanOutPack());
	     		   amountDe = MathUtils.add(amountDe, temp);
	     	   }
	     	    presDt.setQuanDe(quanDe);
	     	    presDt.setOrdsDe(presDt.getOrdsCg()==0.0?1.0:presDt.getOrdsCg());
	     	    BigDecimal amt = new BigDecimal(amountDe).setScale(2,BigDecimal.ROUND_HALF_UP);
	 			presDt.setAmountDe(amt);
	 			ApplicationUtils.setDefaultValue(presDt, false);
	 			presDtForUpd.add(presDt);   
	        }
	 		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExPresOccDt.class), presDtForUpd);

	 		//5.更新处方明细操作流水ex_pres_occ_pddt；
	 		List<ExPresOccPddt> presPddt = new ArrayList<ExPresOccPddt>();
	 		for(PdOutDtParamVo vo : pdOutRes){
	 			ExPresOccPddt pddt = new ExPresOccPddt();
	 			pddt.setPkOrg(UserContext.getUser().getPkOrg());
	 			pddt.setPkPresoccdt(vo.getPkPdapdt());
	 			pddt.setDateDe(new Date());
	 			pddt.setEuDirect("1");
	 			pddt.setPkPd(vo.getPkPd());
	 			pddt.setBatchNo(vo.getBatchNo());
	 			pddt.setPkDeptDe(UserContext.getUser().getPkDept());
	 			pddt.setPkStore(pkStore);
	 			pddt.setPkUnit(vo.getPkUnitPack());
	 			pddt.setPackSize(vo.getPackSize());
	 			pddt.setQuanPack(vo.getQuanOutPack());
	 			pddt.setQuanMin(vo.getQuanOutMin());
	 			pddt.setPriceCost(vo.getPriceCost());
	 			pddt.setDateExpire(vo.getDateExpire());
	 			pddt.setPrice(vo.getPrice());
	 			ExPresOccDt dt = flagPresDts.get(vo.getPkPdapdt());
	 			pddt.setAmountCost(vo.getPriceCost()*vo.getQuanOutPack()*dt.getOrdsCg());
	 			pddt.setAmount(vo.getPrice()*vo.getQuanOutPack()*dt.getOrdsCg());
	 			pddt.setPkPdstdt(vo.getPkPdstdt());
	 			pddt.setNote(null);
	 			ApplicationUtils.setDefaultValue(pddt, true);
	 			presPddt.add(pddt);
	 			
	 		}
	 		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPresOccPddt.class), presPddt);
	          //6）更新窗口工作量（减少处方数），见3.9.6.6。
	 		OpDrugPubUtils.updWinVol(1,pkDept,winno,-1);
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
		
		StringBuffer sql = new StringBuffer("Select cnpres.dt_prestype,pres.pk_presocc, pres.pk_pres,pres.pres_no,pres.date_pres,pres.pk_dept_pres,dept.name_dept,pres.name_emp_pres,diag.diagname");
		sql.append(" from ex_pres_occ pres inner join bd_ou_dept dept on pres.pk_dept_pres=dept.pk_dept ");
		sql.append(" left outer join bd_term_diag diag on pres.pk_diag=diag.pk_diag ");
		sql.append(" inner join CN_PRESCRIPTION cnpres  on cnpres.pk_pres=pres.pk_pres ");
	    sql.append(" where pres.pk_pi=? and pres.flag_cg=1 and pres.pk_dept_ex=? and pres.winno_conf=? and pres.flag_canc=0 and pres.eu_status!=9");
		Object [] args = null; 
		if(EnumerateParameter.ZERO.equals(status)){
			sql.append(" and pres.flag_conf=0 ");
			args =new Object[] {pkPi,pkDept,winno};
			
		}else if(EnumerateParameter.ONE.equals(status)){
			sql.append(" and pres.flag_conf=1 and pres.date_conf>=? and pres.date_conf<=? ");
			args =new Object[] {pkPi,pkDept,winno,begin,end};
		}else{
			sql.append(" and pres.flag_conf=0 and pres.flag_susp=1");
			args =new Object[] {pkPi,pkDept,winno};
		}		
		//2.处方信息				
		return DataBaseHelper.queryForList(sql.toString(), args);
	}
	
	
	/**
	 * 转发
	 * @param param
	 * @param user
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	public void transfDispense(String param, IUser user) throws BusException {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPresocc = (String)paraMap.get("pkPresocc");//处方执行主键
		String pkDept = (String)paraMap.get("pkDept");//当前科室
		String winnoOut = (String)paraMap.get("winnoOut");//转出窗口
		String winnoIn = (String)paraMap.get("winnoIn");//转入窗口

		//2	转发校验，当前窗口必须是下线状态；
		Map<String,Object> chkMap = DataBaseHelper.queryForMap(" select du.flag_online from bd_dept_unit du where du.pk_dept=? and du.code=?", pkDept,winnoOut);
		String flagOnline = "";
		if(null!=chkMap.get("flagOnline")){
			flagOnline = (String)chkMap.get("flagOnline");
		}
		if("1".equals(flagOnline)){
			throw new BusException("当前窗口仍然在线，禁止转发！");
		}
		
		//3 窗口工作量更新
			//3.1更新转入窗口工作量（增加处方数），见3.9.6.6；
		OpDrugPubUtils.updWinVol(1,pkDept,winnoIn,1);			
			//3.2更新转出窗口工作量（减少处方数），见3.9.6.6；
		OpDrugPubUtils.updWinVol(1,pkDept,winnoOut,-1);		
			
		//4 更新处方执行单
		DataBaseHelper.execute(" update ex_pres_occ  set winno_conf=?  where pk_presocc=? and winno_conf=? and flag_conf=0", 
				new Object[]{winnoIn,pkPresocc,winnoOut});
	}
	
	/**
	 * 转发 - 多条处方同时转发
	 * @param param
	 * @param user
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	public void transfDispenseByWin(String param, IUser user) throws BusException {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkDept = (String)paraMap.get("pkDept");//当前科室
		String winnoOut = (String)paraMap.get("winnoOut");//转出窗口
		String winnoIn = (String)paraMap.get("winnoIn");//转入窗口
		List<String> pkPresocc = (List<String>)paraMap.get("pkPresocc");//处方执行主键
		if(null == pkPresocc || pkPresocc.size() < 1)
			throw new BusException("当前窗口无需要转发的处方！");

		//2	转发校验，当前窗口必须是下线状态；
		Map<String,Object> chkMap = DataBaseHelper.queryForMap(" select du.flag_online from bd_dept_unit du where du.pk_dept=? and du.code=?", pkDept,winnoOut);
		String flagOnline = "";
		if(null!=chkMap.get("flagOnline")){
			flagOnline = (String)chkMap.get("flagOnline");
		}
		if("1".equals(flagOnline)){
			throw new BusException("当前窗口仍然在线，禁止转发！");
		}
		
		//3 窗口工作量更新
			//3.1更新转入窗口工作量（增加处方数），见3.9.6.6；
		OpDrugPubUtils.updWinVol(1,pkDept,winnoIn,pkPresocc.size());			
			//3.2更新转出窗口工作量（减少处方数），见3.9.6.6；
		OpDrugPubUtils.updWinVol(1,pkDept,winnoOut,-pkPresocc.size());		
			
		//4 更新处方执行单
		String pks = "(";
		for (String str : pkPresocc) {
			pks += "'" + str + "',";
		}
		pks = pks.substring(0,pks.length() - 1);
		pks += ")";
		DataBaseHelper.execute(" update ex_pres_occ  set winno_conf=?  where pk_presocc in "+ pks +" and winno_conf=? and flag_conf=0", 
				new Object[]{winnoIn,pkPresocc,winnoOut});
	}
	
      private Date dateTrans(String date) {
		
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
      
      /**
  	 * 查询处方打印信息
  	 * @param param
  	 * @param user
  	 * @throws BusException
  	 */
  	@SuppressWarnings("unchecked")
  	public PresPrintInfo qryPresPrint(String param, IUser user) {
  		
  		Map<String,String> map = JsonUtil.readValue(param, Map.class);
  		String pkPresocc = map.get("pkPres");
  		ExPresOcc occ = DataBaseHelper.queryForBean(" select * from ex_pres_occ where pk_presocc = ?",ExPresOcc.class , pkPresocc);
  		String pkPres = occ.getPkPres();
  		
  		StringBuilder sbSql = new StringBuilder("select dept.name_dept, org.name_org,pv.name_pi,doc.name as name_sex,docpres.name as name_pres_type,pres.date_pres,pv.age_pv as age, ");
  		sbSql.append(" diag.desc_diag as name_diag,pres.pres_no,pv.code_pv from  cn_prescription pres");
  		sbSql.append(" inner join pv_encounter pv on pv.pk_pv = pres.pk_pv");
  		sbSql.append(" inner join bd_ou_org org on org.pk_org = pres.pk_org");
  		sbSql.append(" left join pv_diag diag  on diag.pk_pv = pv.pk_pv  and diag.flag_maj = 1");
  		sbSql.append(" inner join bd_defdoc doc on doc.code = pv.dt_sex and doc.CODE_DEFDOCLIST = '000000'");
  		sbSql.append("  inner join bd_defdoc docpres on docpres.code = pres.dt_prestype and docpres.CODE_DEFDOCLIST = '060101'"); //@todo将060102换位060101
  		sbSql.append(" inner join bd_ou_dept dept on dept.pk_dept = pv.pk_dept where pres.pk_pres = ?");
  		PresPrintInfo res = null;
  		List<PresPrintInfo> cnPres = DataBaseHelper.queryForList(sbSql.toString(), PresPrintInfo.class, pkPres);
  		res = cnPres!=null && cnPres.size()>0?cnPres.get(0):null;
  		
  		StringBuilder sbSqlDt = new StringBuilder(" select ord.quan_cg,freq.NAME as name_freq,ord.DATE_START,ord.NAME_ORD,ord.DOSAGE as dosge,ord.CODE_FREQ,supp.NAME as name_supply,ord.DAYS,unitcg.NAME as name_unit_cg,unit.NAME as name_unit from cn_order ord ");
  		sbSqlDt.append(" inner join CN_PRESCRIPTION pres on pres.PK_PRES = ord.PK_PRES ");
  		sbSqlDt.append(" left join BD_SUPPLY supp on ord.CODE_SUPPLY = supp.CODE");
  		sbSqlDt.append(" left join BD_TERM_FREQ freq on ord.CODE_FREQ = freq.CODE");
  		sbSqlDt.append(" left join BD_UNIT unitcg on ord.PK_UNIT_CG  = unitcg.PK_UNIT");
  		sbSqlDt.append(" left join BD_UNIT unit on ord.PK_UNIT  = unit.PK_UNIT where ord.PK_PRES = ?");
  		List<PresDtPrintInfo> cnPresDt = DataBaseHelper.queryForList(sbSqlDt.toString(), PresDtPrintInfo.class, pkPres);
  		if(res!=null)
  			res.setPresDt(cnPresDt);
  		return res;
  	}  
  	/**
	 * 草药代煎登记
	 * @param param
	 * @param user
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	public void reg(String param, IUser user)  {
		ParamVo vo = JsonUtil.readValue(param, ParamVo.class);
  		
  		String sql = "update ex_pres_occ  set flag_bo=1,  date_bo= ? , pk_emp_bo=?,name_emp_bo=?,"+
  	       " eu_status=4 where pk_presocc=? and flag_bo=0";
  		DataBaseHelper.execute(sql, vo.getDateBo(),vo.getPkEmpBo(),vo.getNameEmpBo(),vo.getPkPresocc());

		
	}
	
	/**
	 * 草药代煎发放
	 * @param param
	 * @param user
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	public void RegConf(String param, IUser user)  {
		ParamVo vo = JsonUtil.readValue(param, ParamVo.class);
  		String sql = "update ex_pres_occ set flag_bode=1,date_bode= ?,pk_emp_bode=?,name_emp_bode=?,eu_status=5 "+
  	       " where pk_presocc=? and flag_bo=1 and flag_bode=0 ";
  		DataBaseHelper.execute(sql, vo.getDateBode(),vo.getPkEmpBode(),vo.getNameEmpBode(),vo.getPkPresocc());

		
	}
	/**
	 * 查询未发放代煎草药处方
	 * @param param
	 * @param user
	 */
	public List<ExPresOcc> getExPresOcc(String param, IUser user){
		ExPresOccVO eo = JsonUtil.readValue(param, ExPresOccVO.class);
		String sql = "";
		if (Application.isSqlServer()) {
			 sql = "select pres.pk_presocc, pres.pres_no, pres.date_pres,  pres.date_reg, pres.eu_status,"
				       + "pres.pk_dept_pres,  dept.name_dept, pres.name_emp_pres,  pres.note "      
				       + "from ex_pres_occ pres"
				       + " inner join bd_ou_dept dept on pres.pk_dept_pres=dept.pk_dept"
				       + " where pres.pk_pi= ? and pres.dt_prestype='02' and pres.flag_conf=1 and pres.flag_bode=0 and pres.flag_canc=0 and "
				       + "pres.date_reg< dateadd(day,?,pres.date_pres)";
		} else {
			 sql = "select pres.pk_presocc, pres.pres_no, pres.date_pres,  pres.date_reg, pres.eu_status,"
				       + "pres.pk_dept_pres,  dept.name_dept, pres.name_emp_pres,  pres.note "      
				       + "from ex_pres_occ pres"
				       + " inner join bd_ou_dept dept on pres.pk_dept_pres=dept.pk_dept"
				       + " where pres.pk_pi=? and pres.dt_prestype='02' and pres.flag_conf=1 and pres.flag_bode=0 and pres.flag_canc=0 and "
				       + "pres.date_reg< ( ? + pres.date_pres)";
		}
		List<ExPresOcc> ec= DataBaseHelper.queryForList(sql, ExPresOcc.class, eo.getPkPi(),eo.getDays());
		
		return ec;
		
	}
	/**
	 * 查询已发放代煎草药处方
	 * @param param
	 * @param user
	 */
	public List<ExPresOcc> getExPresOccRelease(String param, IUser user){
		ExPresOccVO eo = JsonUtil.readValue(param, ExPresOccVO.class);
		String sql = "";
		if (Application.isSqlServer()) {
			 sql = "select pres.pk_presocc, pres.pres_no, pres.date_pres,  pres.date_reg, pres.eu_status,"
				       + "pres.pk_dept_pres,  dept.name_dept, pres.name_emp_pres,  pres.note "      
				       + "from ex_pres_occ pres"
				       + " inner join bd_ou_dept dept on pres.pk_dept_pres=dept.pk_dept"
				       + " where pres.pk_pi= ? and pres.dt_prestype='02'  and pres.flag_bode=1 and pres.flag_canc=0 and "
				       + "pres.date_reg< dateadd(day,?,pres.date_pres)";
		} else {
			 sql = "select pres.pk_presocc, pres.pres_no, pres.date_pres,  pres.date_reg, pres.eu_status,"
				       + "pres.pk_dept_pres,  dept.name_dept, pres.name_emp_pres,  pres.note "      
				       + "from ex_pres_occ pres"
				       + " inner join bd_ou_dept dept on pres.pk_dept_pres=dept.pk_dept"
				       + " where pres.pk_pi=? and pres.dt_prestype='02'  and pres.flag_bode=1 and pres.flag_canc=0 and "
				       + "pres.date_reg< (?+pres.date_pres)";
		}
		List<ExPresOcc> ec= DataBaseHelper.queryForList(sql, ExPresOcc.class, eo.getPkPi(),eo.getDays());

		return ec;
		
	}
	
	/**
	 * 获取停止或开始时的相关参数！
	 * @param param
	 * @param user
	 */
	public void startOrStop(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null)
			throw new BusException("未获取到停止或开始时的相关参数！");
	   String sql = "update bd_dept_unit set flag_online= :flagOnline1 "+
              " where eu_unittype=1 and eu_butype=1 and pk_dept=:pkDept and  code = :code and flag_online = :flagOnline2 ";
	   DataBaseHelper.update(sql, paramMap);
	}
	
	/**
	 * 查询普通门诊处方打印信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOpPresPrint(String param, IUser user){
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		if(map == null || map.get("pkPresOcc")==null)
   		 throw new BusException("未获取到处方信息！");
		
		//查询处方明细
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = schOpdsPreMapper.queryOpPresPrints(map);
		
		//对明细进行分组
		if(list!=null&&list.size()>0){
			new ExlistPrintSortByOrdUtil().presGroup(list);
		}
		
		return list;
	}
}
