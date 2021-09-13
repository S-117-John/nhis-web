package com.zebone.nhis.scm.pub.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.util.StringUtils;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;


/**
 * 门诊药房公共服务
 * @author Roger
 *
 */
public class OpDrugPubUtils {
	
	
	/**
	 * 更新窗口业务量
	 * @param type 类型 （0配药 1发药）
	 * @param pkDept 科室
	 * @param winno 窗口号
	 * @param val 增量 val（正数为增加，负数为减少）
	 * @throws Exception 
	 */
	public static void updWinVol(int type,String pkDept,String winno,int val) throws BusException{
		
		if(StringUtils.hasText(winno) && StringUtils.hasText(pkDept)){
			StringBuffer sql = new StringBuffer("update bd_dept_unit set cnt_bu=cnt_bu+?"); 
		     sql.append(" where eu_unittype=1 and");
			 sql.append(" eu_butype=? and");	
			 sql.append(" pk_dept=? and");
			 sql.append(" code=?");
           DataBaseHelper.execute(sql.toString(), new Object[]{val,type,pkDept,winno});
		}else{
			throw new BusException("更新窗口业务量异常,参数缺失！");
		}
	}
	
	
	/**
	 * 分配窗口
	 * @param type   类型 type （0配药 1发药）
	 * @param pkDept 科室
	 * @param winno  窗口号
	 * @return
	 */
	public static Map<String,Object> getWin(int type,String pkDept){
		
		Map<String,Object> res = new HashMap<String,Object>();
		if(StringUtils.hasText(pkDept)){
			StringBuffer sql = new StringBuffer(" select du.code,min(du.cnt_bu) val from bd_dept_unit du where du.eu_unittype=1 and du.flag_online = 1 ");
			sql.append(" and du.pk_dept=?");
			sql.append(" and du.eu_butype=? group by du.code order by val");		
            List<Map<String,Object>> result = DataBaseHelper.queryForList(sql.toString(), new Object[]{pkDept,type});
            if(result !=null && result.size()>0){
            	res = result.get(0);
            }else{
            	String sql_dept = "select name_dept from bd_ou_dept where pk_dept=?";
            	Map<String,Object> deptMap=DataBaseHelper.queryForMap(sql_dept, new Object[]{pkDept});
            	if(deptMap!=null && deptMap.get("nameDept")!=null){
            		String typeTxt = "0".equals(type) ? "配药" : "1".equals(type) ? "发药" : "";
                	throw new BusException(deptMap.get("nameDept").toString()+"当前没有在线的"+ typeTxt +"窗口！");
            	}
            }
		}
		return res;
	}
	/***
	 * 根据参数EX0030获取窗口分配方式
	 * @param paraMap {"pkDept":"发药药房","pkDeptPres":"开立科室"}
	 * @return {"winnoConf":"发药窗口","winnoPrep":"配药窗口"}
	 */
	public static Map<String,Object> getWin(String pkDept,String pkDeptPres,String pkDeptAreaapp) {
		String winType = ApplicationUtils.getDeptSysparam("EX0030", pkDept);//门诊药房窗口分配方式：0 按业务量，1 按开立科室
		String giveType=ApplicationUtils.getDeptSysparam("EX0001", pkDept);//门诊药房发配药模式：1-配药发药模式，2-直接发药模式
		
		if(CommonUtils.isEmptyString(winType)){
			throw new BusException("请维护系统参数【EX0030】！");
		}
		
		if(CommonUtils.isEmptyString(giveType)){
			throw new BusException("请维护系统参数【EX0001】！");
		}

		String sql="select name_dept from bd_ou_dept where pk_dept=?";
		Map<String,Object> deptMap=DataBaseHelper.queryForMap(sql,new Object[]{pkDept});

		Map<String,Object> resMap=new HashMap<String,Object>();
		if (EnumerateParameter.ZERO.equals(winType)) {//按业务量
			//查询发药/配药窗口
			StringBuffer cntSql=new StringBuffer();
			cntSql.append("select count(1) cnt, du.code  winno_conf, dul.code winno_prep");
			cntSql.append(" from bd_dept_unit du");
			cntSql.append(" left outer join ex_pres_occ pres on du.pk_dept = pres.pk_dept_ex and");
			cntSql.append(" du.code = pres.winno_conf and pres.flag_reg = '1' and  pres.flag_conf = '0' and pres.flag_canc = '0'");
			cntSql.append(" left outer join bd_dept_unit dul on du.pk_deptunit_rl = dul.pk_deptunit");
			cntSql.append(" where du.pk_dept =? ");
			cntSql.append(" and du.eu_unittype='1' and du.eu_butype='1' and du.flag_online='1' and du.del_flag='0'");
			cntSql.append(" group by du.code, dul.code order by count(1)");
			List<Map<String,Object>> qryAdPortfolio = DataBaseHelper.queryForList(cntSql.toString(), new Object[]{pkDept});
			if (qryAdPortfolio!=null&&qryAdPortfolio.size()>0) {
				resMap=qryAdPortfolio.get(0);
			}else {
				throw new BusException("签到失败，科室【"+MapUtils.getString(deptMap,"nameDept")+"】,系统参数【EX0030】='0'当前没有在线窗口！");
			}
		}else if(EnumerateParameter.ONE.equals(winType)){//按开立科室
			StringBuffer deptSql=new StringBuffer();
			deptSql.append("SELECT  count(1), du.code  winno_conf,  dul.code winno_prep");
			deptSql.append(" FROM bd_dept_unit du");
			deptSql.append(" INNER JOIN bd_dept_unit_obj ob ON du.pk_deptunit = ob.pk_deptunit");
			deptSql.append(" left JOIN ex_pres_occ pres ON du.pk_dept = pres.pk_dept_ex AND");
			deptSql.append(" du.code = pres.winno_conf AND pres.flag_reg = '1' AND pres.flag_conf = '0' AND  pres.flag_canc = '0'");
			deptSql.append(" LEFT OUTER JOIN bd_dept_unit dul ON du.pk_deptunit_rl = dul.pk_deptunit");
			deptSql.append(" WHERE du.pk_dept =? AND du.eu_unittype='1' AND ");
			deptSql.append(" du.eu_butype='1' AND du.flag_online='1' AND du.del_flag='0' AND");
			deptSql.append(" ob.eu_objtype='0' AND ob.pk_dept=?");
			deptSql.append(" group by du.code, dul.code order by count(1)");
			//查询发药/配药窗口
			List<Map<String,Object>> qryAdPkDept = DataBaseHelper.queryForList(deptSql.toString(), new Object[]{pkDept,pkDeptPres});
			if (qryAdPkDept!=null&&qryAdPkDept.size()>0) {
				resMap= qryAdPkDept.get(0);
			}else {
				throw new BusException("签到失败，科室【"+MapUtils.getString(deptMap,"nameDept")+"】,系统参数【EX0030】='1'没有在线窗口！");
			}
		}else{
			StringBuffer deptSql=new StringBuffer();
			deptSql.append("SELECT  count(1), du.code  winno_conf,  dul.code winno_prep,du.eu_usecate");
			deptSql.append(" FROM bd_dept_unit du");
			deptSql.append(" INNER JOIN bd_dept_unit_obj ob ON du.pk_deptunit = ob.pk_deptunit");
			deptSql.append(" left JOIN ex_pres_occ pres ON du.pk_dept = pres.pk_dept_ex AND");
			deptSql.append(" du.code = pres.winno_conf AND pres.flag_reg = '1' AND pres.flag_conf = '0' AND  pres.flag_canc = '0'");
			deptSql.append(" LEFT OUTER JOIN bd_dept_unit dul ON du.pk_deptunit_rl = dul.pk_deptunit");
			deptSql.append(" WHERE du.pk_dept =? AND du.eu_unittype='1' AND ");
			deptSql.append(" du.eu_butype='1' AND du.flag_online='1' AND du.del_flag='0' AND");
			deptSql.append(" ob.eu_objtype='1' AND ob.pk_dept=?");
			deptSql.append(" group by du.code, dul.code,du.eu_usecate order by count(1)");
			//查询发药/配药窗口
			List<Map<String,Object>> qryAdPkDept  = DataBaseHelper.queryForList(deptSql.toString(), new Object[]{pkDept,pkDeptAreaapp});
			if (qryAdPkDept!=null&&qryAdPkDept.size()>0) {
				resMap= qryAdPkDept.get(0);
			}else {
				throw new BusException("签到失败，科室【"+MapUtils.getString(deptMap,"nameDept")+"】,系统参数【EX0030】='2'没有在线窗口！");
			}
		}

		if(EnumerateParameter.ONE.equals(giveType)){//配药发药
			if(!CommonUtils.isNotNull(resMap.get("winnoConf"))){
				throw new BusException("科室【"+MapUtils.getString(deptMap,"nameDept")+"】,【EX0001】配药发药时，未找到在线发药窗口，请维护！");
			}else if(!CommonUtils.isNotNull(resMap.get("winnoPrep"))){
				throw new BusException("科室【"+MapUtils.getString(deptMap,"nameDept")+"】,【EX0001】配药发药时，未找到发药窗口【"+resMap.get("winnoConf")+"】关联在线配药窗口，请维护！");
			}
		}else{//直接发药
			if(!CommonUtils.isNotNull(resMap.get("winnoConf"))){
				throw new BusException("科室【"+MapUtils.getString(deptMap,"nameDept")+"】【EX0001】直接发药时，未找到在线发药窗口，请维护！");
			}
		}
		return resMap;
	}

}
