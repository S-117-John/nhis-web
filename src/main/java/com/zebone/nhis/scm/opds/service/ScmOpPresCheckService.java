package com.zebone.nhis.scm.opds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.scm.pub.support.OpDrugPubUtils;
import com.zebone.nhis.scm.pub.vo.OpPreRtnVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class ScmOpPresCheckService {
	
	
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
		String pkPi   = (String)paraMap.get("pkPi");
		StringBuilder sql = new StringBuilder(" select pres.pk_presocc,pres.pk_pres,pres.pres_no, pres.date_pres,");
		 sql.append(" pres.pk_dept_pres,dept.name_dept,  pres.name_emp_pres,diag.diagname");
	     sql.append( " from ex_pres_occ pres inner join bd_ou_dept dept on pres.pk_dept_pres=dept.pk_dept");    
	     sql.append( " left outer join bd_term_diag diag on pres.pk_diag=diag.pk_diag");
	     sql.append( " where pres.pk_pi=? and  pres.flag_cg=1 and pres.pk_dept_ex=? and  pres.eu_status=0 ");
		//2.处方信息				
		return DataBaseHelper.queryForList(sql.toString(), pkPi,pkDept);
	}
	
	/**
	 * 查询未签到患者信息
	 * @param param pk_dept winno
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryPrePatis(String param, IUser user) {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param,Map.class);
		StringBuilder sql = new StringBuilder(" select distinct pi.pk_pi,pi.code_pi, pi.name_pi");
		sql.append(" from pi_master pi inner join ex_pres_occ pres on pi.pk_pi=pres.pk_pi ");
		sql.append(" where pres.flag_cg=1 and pres.pk_dept_ex=? and pres.eu_status=0 ");
		
		String pkDept = (String)paraMap.get("pkDept");
		//2.患者信息				
		return DataBaseHelper.queryForList(sql.toString(), pkDept);
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
		StringBuilder sql = new StringBuilder(" select dt.pk_presoccdt,dt.pk_pd,dt.pack_size,pd.name,fa.name factory,");
		sql.append(" pd.spec, unit.name unit,dt.quan_cg, dt.price,dt.amount_cg, ");
		sql.append( "  ord.dosage,unit_dos.name unitdos,sup.name supply, freq.name freq, ord.days");
		sql.append( " from bd_pd pd inner join bd_factory fa on pd.pk_factory=fa.pk_factory ");
		sql.append( " inner join ex_pres_occ_dt dt on pd.pk_pd=dt.pk_pd");
		sql.append("  inner join bd_unit unit on dt.pk_unit=unit.pk_unit");
		sql.append(" inner join cn_order ord on dt.pk_cnord=ord.pk_cnord");
		sql.append(" left join bd_unit unit_dos on ord.pk_unit_dos=unit_dos.pk_unit");
		sql.append(" left join bd_supply sup on ord.code_supply=sup.code");
		sql.append(" left join bd_term_freq freq on ord.code_freq=freq.code where dt.pk_presocc=?");

		//2.处方明细信息
		List<OpPreRtnVo> res = null;
		if(StringUtils.hasText(pkPresocc)){
			res = DataBaseHelper.queryForList(sql.toString(),OpPreRtnVo.class, pkPresocc);
		}
		return res;
	}
	
	/**
	 * 签到
	 * @param param pk_dept winno
	 * @param user
	 */
	public void check(String param, IUser user) {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param,Map.class);
		String pkPresocc = (String)paraMap.get("pkPresocc");
		String type = ApplicationUtils.getSysparam("EX0001", false);
		Map<String,Object> winMap = null;
		String sqlChk = "";
		if(EnumerateParameter.ONE.equals(type)){//配药
			winMap = OpDrugPubUtils.getWin(0,UserContext.getUser().getPkDept());
			sqlChk = " update ex_pres_occ  set eu_status=1, date_reg=?, winno_prep=?  "
					+ "where pk_presocc=? and flag_cg=1 and eu_status=0";
		}else{//发药
			winMap = OpDrugPubUtils.getWin(1,UserContext.getUser().getPkDept());
			sqlChk = " update ex_pres_occ set eu_status=1, date_reg=?,winno_conf=?   "
					+ " where pk_presocc=? and  flag_cg=1 and eu_status=0 ";
		}
		String winno = (String)winMap.get("code");
		if(CommonUtils.isEmptyString(winno)){
			throw new BusException("签到失败，当前没有在线窗口！");
		}
		DataBaseHelper.execute(sqlChk, new Date(),winno,pkPresocc);
	}
	
	/**
	 * 签到
	 * @param param pk_dept winno
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void checkByPati(String param, IUser user) {
		//1.参数获取
		List<String> pkPresocc = JsonUtil.readValue(param,new TypeReference<List<String>>(){});//处方执行主键
		if(null == pkPresocc)
			throw new BusException("请选择待签到的处方！");
		//拼接多条处方主键
		String pks = "(";
		for (String str : pkPresocc) {
			pks += "'"+ str +"',";
		}
		pks = pks.substring(0,pks.length() - 1);
		pks += ")";
		
		String type = ApplicationUtils.getSysparam("EX0001", false);
		Map<String,Object> winMap = null;
		String sqlChk = "";
		
		if(EnumerateParameter.ONE.equals(type)){//配药
			winMap = OpDrugPubUtils.getWin(0,UserContext.getUser().getPkDept());
			sqlChk = " update ex_pres_occ  set eu_status=1, date_reg=?, winno_prep=?,"
					+ "flag_reg='1' "
					+ "where pk_presocc in "+ pks +" and flag_cg=1 and eu_status=0";//添加更新签到标志flag_reg='1'的操作--jiangdong
		}else{//发药
			winMap = OpDrugPubUtils.getWin(1,UserContext.getUser().getPkDept());
			sqlChk = " update ex_pres_occ set eu_status=1, date_reg=?,winno_conf=?,   "
					+ "flag_reg='1' ,flag_prep='1' "
					+ " where pk_presocc in "+ pks +" and  flag_cg=1 and eu_status=0 ";//添加更新签到标志flag_reg='1'和配药标志flag_prep='1'的操作--jiangdong
		}
		
		String winno = (String)winMap.get("code");
		if(CommonUtils.isEmptyString(winno)){
			throw new BusException("签到失败，当前没有在线窗口！");
		}
		DataBaseHelper.execute(sqlChk, new Date(),winno);
		// 更新业务量
		if(EnumerateParameter.ONE.equals(type)){
			OpDrugPubUtils.updWinVol(0, UserContext.getUser().getPkDept(), winno, 1);//配药
		}else{
			OpDrugPubUtils.updWinVol(1, UserContext.getUser().getPkDept(), winno, 1);//发药
		}
		
	}

}
