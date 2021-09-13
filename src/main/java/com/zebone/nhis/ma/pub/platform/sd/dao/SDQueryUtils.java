package com.zebone.nhis.ma.pub.platform.sd.dao;

import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.collections.MapUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SDQueryUtils {
	@Resource
	private SDMsgMapper sDMsgMapper;
	@Resource
	private SDOpMsgMapper sDOpMsgMapper;

	/**
	 * 查询预交金信息，发票号
	 * @param codePv
	 * @return
	 */
	public List<Map<String, Object>>  queryDeposit(String bankNo,String codePv){
		if(codePv==null || "".equals(codePv) || bankNo==null || "".equals(bankNo)){
			return null;
		}
		StringBuilder sql = new StringBuilder()
		.append("select * from pv_encounter pv left join bl_deposit d on pv.pk_pv=d.pk_pv ")
		.append("left join bl_invoice iv on iv.pk_invoice=d.pk_settle ")
		.append("where  d.del_flag='0' and d.eu_dptype='9' and  d.eu_pvtype='3' ")
		.append("and d.bank_no=? and pv.code_pv=?");
		return DataBaseHelper.queryForList(sql.toString(),bankNo,codePv);
	}

	/**
	 * 跟据就诊主键查询费用统计
	 * @param pkPv
	 * @return
	 */
	public List<Map<String, Object>> queryCost(String pkPv){
		if(pkPv==null || "".equals(pkPv)){
			return null;
		}
		StringBuilder sql = new StringBuilder()
		.append("select cate.name name, sum(blip.amount) amt,")
		.append("sum(blip.amount_pi) amt_pi from bd_itemcate cate ")
		.append("inner join bl_ip_dt blip on cate.pk_itemcate=blip.pk_itemcate ")
		.append("where blip.flag_settle = 0 and blip.pk_pv = ? group by cate.name");
		return DataBaseHelper.queryForList(sql.toString(),pkPv);
	}



	/**
	 * 根据医嘱用法名称查询用法编码
	 *
	 * @param name
	 * @return
	 */
	public String qrySupplyCode(String name) {
		if (name.length() <= 0) {
			return null;
		}
		String sql = "select name  from bd_supply where code=?";
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, name);
		return SDMsgUtils.getPropValueStr(resMap, "name");
	}




	/**
	 * 发票主键查询发票明细
	 * @param pkInvoice
	 * @return
	 */
	public List<Map<String,Object>> queryInvoiceDt(String pkInvoice, String codeInv){
		//i.code_inv,idt.code_bill code,idt.name_bill name,idt.amount amount
		String sql = "select * from bl_invoice i left join BL_INVOICE_DT idt on i.pk_invoice=idt.pk_invoice where i.pk_invoice=? or i.code_inv=?";
		return DataBaseHelper.queryForList(sql, pkInvoice, codeInv);
	}

	/**
	 * 查询手术编码和名字
	 * @param pkOp
	 * @return
	 */
	public Map<String,Object> queryBdTermDiag(String pkOp){
		if(pkOp==null||"".equals(pkOp)){
			return null;
		}
		String sql = "select diagcode,diagname from BD_TERM_DIAG where DT_DIAGTYPE='02' and pk_diag=? ";
		return DataBaseHelper.queryForMap(sql, pkOp);
	}

	/***
	 * 根据pk_pv查询诊断
	 * @param pkPv
	 * @return
	 */
	public List<Map<String, Object>> queryDiagByPkpv(String pkPv){
		if(pkPv == null || "".equals(pkPv)){
			return null;
		}
		String sql = "select d.name_diag,d.code_icd,d.name_emp_diag,d.pk_pv, " +
				"(select t.old_id from bd_defdoc t where t.code_defdoclist='060005' and t.code=d.DT_DIAGTYPE) dt_diagtype " +
				"from pv_diag d where pk_pv=? order by d.flag_maj desc";

		return  DataBaseHelper.queryForList(sql, pkPv);
	}

	/**
	 * 根据pkDiagPre查询诊断（手术）
	 * @param pkDiagPre
	 * @return
	 */
	public Map<String,Object> queryDiag(String pkDiagPre){
		if (pkDiagPre==null||"".equals(pkDiagPre)){
			return null;
		}
		String sql = "select code_cd,code_icd,name_cd from bd_cndiag where pk_cndiag=?";
		return DataBaseHelper.queryForMap(sql, pkDiagPre);
	}

	/**
	 * 根据单位主键查询单位名称
	 *
	 * @param pkUnit
	 * @return
	 */
	public String qryUnitByPK(String pkUnit) {
		if (pkUnit==null||"".equals(pkUnit)){
			return null;
		}
		return MapUtils.getString(DataBaseHelper.queryForMap("select name from bd_unit where pk_unit=?", pkUnit),"name");
	}

	/**
	 * 根据医嘱频次编码查询频数
	 * @param code
	 * @return
	 */
	public String qryItemFreq(String code) {
		if (code==null||"".equals(code)){
			return null;
		}
		String sql = "select cnt from BD_TERM_FREQ where CODE=?";
		try{
			return DataBaseHelper.queryForScalar(sql, String.class, code);
		}catch(EmptyResultDataAccessException e){
			return "1";
		}
	}


	/**
	 * 根据科室编码查询外键
	 * @param code
	 * @return
	 */
	public Map<String,Object> queryDeptByCode(String code){
		if(code==null|| "".equals(code)){
			return null;
		}
		String sql = "select d.pk_org,d.pk_dept,d.code_dept,d.name_dept,grp.PK_USRGRP from bd_ou_dept d left join bd_ou_usrgrp grp on d.NAME_DEPT=grp.NAME_USRGRP where d.code_dept=? and ROWNUM=1";
		return DataBaseHelper.queryForMap(sql,code);
	}

	/**
	 * 根据用户编码查询外键
	 * @param code
	 * @return
	 */
	public Map<String,Object> queryEmpByCode(String code){
		if(code==null|| "".equals(code)){
			return null;
		}
		String sql = "select e.pk_org,e.pk_emp,e.name_emp from bd_ou_employee e where e.code_emp=?";
		return DataBaseHelper.queryForMap(sql,code);
	}

    /**
     * 根据用户主外键查询编码
     * @param pkUser
     * @return CODE:编码,NAME
     * 查询BD_OU_USER
     */
    public Map<String,Object> getUserCodeByPkUser(String pkUser){
    	if(pkUser==null || "".equals(pkUser)) {
    		return null;
    	}
    	String sql="select pk_Org,code_emp CODE,name_emp NAME from bd_ou_employee where pk_emp=?";
		return DataBaseHelper.queryForMap(sql,pkUser);
    }

    /**
     * 根据频次编号获取频次名称
     * @param codeFreq
     * @return
     */
    public Map<String,Object> getNameFreq(String codeFreq){
    	if(codeFreq==null||"".equals(codeFreq)){
    		return null;
    	}
    	String sql = "select name,name_print from bd_term_freq where code=?";
		return DataBaseHelper.queryForMap(sql, codeFreq);
    }

    /**
     * 获取病人基本信息MAP(门诊)
     * @param patMap
	 * @param pkPv
     * @return
     * @throws Exception
     */
    public Map<String, Object> getPatMapOut(String pkPv,Map<String,Object> patMap) throws Exception{
    	//处理病人基本信息参数，若patMap为空，则需要重新查询（能从业务系统中获取尽量传入此参数，避免多查询一次数据库）
    	if(patMap==null){
    		if(pkPv==null|| "".equals(pkPv)){
    			throw new Exception("病人信息和患者就诊pkPv不能同时为空！");
    		}
    		Map<String, Object> qryParam = new HashMap<String, Object>();
    		qryParam.put("pkPv", pkPv);
    		List<Map<String, Object>> list= sDMsgMapper.queryPatListOut(qryParam);
    		if(list==null||list.size()==0){
    			throw new Exception("病人信息为空！");
    		}
    		patMap=list.get(0);
    	}
		return patMap;
    }

    /**
     * 获取病人基本信息MAP
     * @param pkPv
	 * @param patMap
     * @return
     * @throws Exception
     */
    public Map<String, Object> getPatMap(String pkPv,Map<String,Object> patMap) throws Exception{
    	//处理病人基本信息参数，若patMap为空，则需要重新查询（能从业务系统中获取尽量传入此参数，避免多查询一次数据库）
    	if(patMap==null){
    		if(pkPv==null|| "".equals(pkPv)){
    			throw new Exception("病人信息和患者就诊pkPv不能同时为空！");
    		}
    		Map<String, Object> qryParam = new HashMap<>(16);
    		qryParam.put("pkPv", pkPv);
    		List<Map<String, Object>> list= sDMsgMapper.queryPatList(qryParam);
    		if(list==null||list.size()==0){
    			throw new Exception("病人信息为空！");
    		}
    		patMap=list.get(0);
    	}
		return patMap;
    }

    /**
     * 根据科室主键查询编码和名称
     * @param pkDept
     */
	public Map<String,Object> queryDeptByPk(String pkDept) {
		if(pkDept==null||"".equals(pkDept)){
    		return null;
    	}
    	String sql = "select d.pk_dept,d.code_dept,name_dept from bd_ou_dept d where d.pk_dept=? and del_flag = '0'";
		return DataBaseHelper.queryForMap(sql, pkDept);

	}

	/**
	 * 根据的cn_op_apply.dt_oplevel查询手术等级
	 * @param dtOplevel
	 * @return
	 */
	public Map<String,Object> queryOplevel(String dtOplevel){
		if(dtOplevel==null || "".equals(dtOplevel)){
			return null;
		}
		String sql = "select t.old_id from bd_defdoc t where t.code_defdoclist='030305' and t.code=?";
		return DataBaseHelper.queryForMap(sql, dtOplevel);
	}

	/**
	 * 根据的dtDiagtype查询诊断
	 * @param dtDiagtype
	 * @return
	 */
	public Map<String,Object> queryDiagtype(String dtDiagtype){
		if(dtDiagtype==null || "".equals(dtDiagtype)){
			return null;
		}
		String sql = "select t.old_id from bd_defdoc t where t.code_defdoclist='060005' and t.code=?";
		return DataBaseHelper.queryForMap(sql, dtDiagtype);
	}

	/**
	 * 查询婚姻编码
	 * @param dtMarry
	 * @return
	 */
	public Map<String,Object> queryMarryByCode(String dtMarry){
		if(dtMarry==null || "".equals(dtMarry)){
			return null;
		}
		String sql = "select t.old_id from bd_defdoc t where t.code_defdoclist='000006' and t.code=?";
		return DataBaseHelper.queryForMap(sql, dtMarry);
	}

	/**
	 * 通过医嘱主键查询检查检验相关数据
	 * @param pkCnordList  医嘱主键集合
	 * @return
	 */
	public List<Map<String,Object>> queryRisLisUtil(List<String> pkCnordList){
		StringBuilder pkCnords = new StringBuilder();
		if(pkCnordList != null){
		   for(int i=0;i< pkCnordList.size();i++){
			  if(pkCnordList.get(i)!=null){
				  if(i == pkCnordList.size()-1 ){
					  pkCnords.append("'").append(pkCnordList.get(i)).append("'");
					   //pkCnords += "'"+ pkCnordList.get(i) +"'";
				   }else {
					   pkCnords.append("'").append(pkCnordList.get(i)).append("',");
					   //pkCnords += "'"+ pkCnordList.get(i) +"',";
				   }
			  }
		   }
		}
		StringBuilder sql=new StringBuilder("SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,")
	   .append("co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.code_freq,co.flag_emer,co.NOTE_ORD,")
	   .append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,apply.dt_samptype,def.name name_samp FROM cn_order co ")
	   .append("inner join pi_master pm ON pm.pk_pi = co.pk_pi left join cn_lab_apply apply on apply.pk_cnord=co.pk_cnord ")
		.append("left join BD_DEFDOC def on def.code=apply.dt_samptype and def.CODE_DEFDOCLIST='030200'")
	   .append("where co.pk_cnord  in (")
	   .append(pkCnords)
	   .append(")");
		return DataBaseHelper.queryForList(sql.toString());
	}

	/**
	 * 查询bd_itemcate 服务分类(物资字典使用)
	 * @param code
	 */
	public Map<String,Object> queryBdItemcateByCode(String code) {
		if(code==null || "".equals(code)){
			return null;
		}
		String sql = "select pk_itemcate,code,name,pk_parent from bd_itemcate where code=?";
		return DataBaseHelper.queryForMap(sql, code);
	}

	/**
	 * 根据就诊主键查询结算记录
	 * @param pkPv
	 * @return
	 */
	public Map<String,Object> queryBlSettleByPv(String pkPv) {
		if(pkPv==null || "".equals(pkPv)){
			return null;
		}
		StringBuilder sql = new StringBuilder()
		.append("select s.pk_settle,s.eu_pvtype,s.dt_sttype,s.eu_stresult,")
		.append("nvl(s.amount_prep,0) amount_prep,")
		.append("nvl(s.amount_st,0) amount_st,")
		.append("nvl(s.amount_pi,0) amount_pi,")
		.append("nvl(s.amount_insu,0) amount_insu,")
		.append("nvl((s.amount_st-amount_prep),0) amount_total,")
		.append("nvl(s.amount_st-amount_prep-amount_insu,0) amount_self,")
		.append("s.pk_emp_st,s.name_emp_st,s.flag_cc,s.flag_canc,s.flag_arclare,s.code_st,")
		.append("s.pk_emp_receipt,s.name_emp_receipt,")
		.append("nvl(inv.AMOUNT_INV,0) AMOUNT_INV, nvl(inv.AMOUNT_PI,0) AMOUNT_PI_INV ")
		.append("from  bl_settle s left join bl_st_inv i on s.pk_settle=i.pk_settle ")
		.append("left join bl_invoice inv on inv.pk_invoice=i.pk_invoice ")
		.append("where rownum=1 and s.flag_canc='0' and s.pk_pv=? order by s.ts desc");
		return DataBaseHelper.queryForMap(sql.toString(), pkPv);
	}

	/**
	 * 根据结算主键查询发票信息
	 * @param pkSettle 结算主键
	 * @param codeInv	发票号
	 * @param condition 取消标志
	 * @return
	 */
	public List<Map<String,Object>> queryInvoiceByPkSettle(String pkSettle, String codeInv,String condition) {
		if((pkSettle==null || "".equals(pkSettle)) && (codeInv==null || "".equals(codeInv))){
			return null;
		}
		StringBuilder sql = new StringBuilder()
		.append("select s.PK_SETTLE,s.PK_PI,s.PK_PV,s.EU_PVTYPE,s.PK_INSURANCE,")
		.append("s.DT_STTYPE,s.EU_STRESULT,nvl(s.amount_prep,0) amount_prep,")
		.append("nvl(s.amount_st,0) amount_st,")
		.append("nvl(s.amount_pi,0) amount_pi,")
		.append("nvl(s.amount_insu,0) amount_insu,")
		.append("nvl((s.amount_st-amount_prep),0) amount_total,")
		.append("nvl(s.amount_st-amount_prep-amount_insu,0) amount_self,")
		.append("s.PK_ORG_ST,s.PK_DEPT_ST,s.PK_EMP_ST,s.NAME_EMP_ST,")
		.append("inv.PK_INVOICE,inv.PK_INVCATE,inv.PK_EMPINVOICE,inv.CODE_INV,")
		.append("nvl(inv.AMOUNT_INV,0) AMOUNT_INV, nvl(inv.AMOUNT_PI,0) AMOUNT_PI_INV,")
		.append("inv.NOTE,inv.PK_EMP_INV,")
		.append("inv.NAME_EMP_INV,inv.PRINT_TIMES,inv.FLAG_CANCEL,inv.PK_EMP_CANCEL,inv.NAME_EMP_CANCEL ")
		.append("from bl_settle s left join bl_st_inv i on s.pk_settle=i.pk_settle ")
		.append("left join bl_invoice inv on inv.pk_invoice=i.pk_invoice ")
		.append("where (s.pk_settle=? or inv.code_inv=?) ")
		.append(condition);
		return DataBaseHelper.queryForList(sql.toString(), pkSettle,codeInv);
	}
	/**
	 * 查询收费信息，根据结算主键
	 * @param pkSettle
	 * @return
	 */
	public Map<String,Object> querySettleByPk(String pkSettle){
		if(pkSettle==null || "".equals(pkSettle)){
			return null;
		}
		StringBuffer sql = new StringBuffer("select pk_pi,pk_pv,pk_settle from bl_settle where pk_settle=?");
		return DataBaseHelper.queryForMap(sql.toString(), pkSettle);
	}
	/**
	 *查询对应旧发票号
	 * @param pkSettle
	 * @param amount
	 * @return
	 */
	public Map<String,Object> queryOldCodeInvByPkSettle(String pkSettle,String amount){
		if((pkSettle==null || "".equals(pkSettle)) || (amount==null || "".equals(amount))){
			return null;
		}
		StringBuilder sql = new StringBuilder()
		.append("select inv.code_inv from bl_st_inv i ")
		.append("left join bl_invoice inv on i.pk_invoice=inv.pk_invoice ")
		.append("where inv.flag_cancel='1'and i.pk_settle=? and inv.amount_inv=?  ")
		.append("order by inv.code_inv desc");
		return DataBaseHelper.queryForMap(sql.toString(), pkSettle,amount);
	}
	/**
	 * 查询转科信息
	 * @param pkPv
	 * @return
	 */
	public Map<String,Object> queryAdt(String pkPv){
		if(pkPv==null || "".equals(pkPv)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select to_char(adt.DATE_BEGIN,'YYYYMMDDHH24MISS') date_Begin from PV_ADT adt ");
		sql.append("where adt.DATE_END is null and adt.PK_PV=?");
		return DataBaseHelper.queryForMap(sql.toString(), pkPv);
	}
	/**
	 * 根据codePi查询患者信息
	 * @param codePi
	 * @return
	 */
	public List<Map<String ,Object>> queryPiByCodePi(String codePi) {
		if(codePi ==null || "".equals(codePi)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select * from PI_MASTER where CODE_PI=?");
		return DataBaseHelper.queryForList(sql.toString(),codePi);
	}
	/**
	 * 根据CodePv查询pv表
	 * @param codePv
	 * @return
	 */
	public List<Map<String,Object>> queryPvByCodePv(String codePv){
		if(codePv ==null || "".equals(codePv)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select * from PV_ENCOUNTER pv left join PI_MASTER pi on pv.PK_PI=pi.PK_PI ");
		sql.append("where pv.CODE_PV=?");
		return DataBaseHelper.queryForList(sql.toString(),codePv);
	}
	/**
	 * 门诊体检使用，查询患者信息
	 * @param codePv
	 * @param isDel
	 * @return
	 */
	public List<Map<String ,Object>> queryPiPvOpbyPv(String codePv,boolean isDel) {
		if(codePv ==null || "".equals(codePv)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select pi.PK_PI,pv.PK_PV,(select code from BD_HP where pv.PK_INSU=PK_HP) code from PI_MASTER pi inner join PV_ENCOUNTER pv on pi.PK_PI=pv.PK_PI ")
		//.append("where pv.EU_PVTYPE='4' and pi.CODE_PI=? ")
		.append("where pv.CODE_PV=? ");
		if(!isDel){
			sql.append("and eu_status in (0,1,2,3)");
		}
		return DataBaseHelper.queryForList(sql.toString(),codePv);
	}

	/**
	 * 查询母亲信息（pv_infant）
	 */
	public Map<String, Object> queryMaInfo(String codeIp) {
		if(codeIp==null || "".equals(codeIp)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select pv.* from pv_infant inf ,PV_ENCOUNTER pv where inf.pk_pv=pv.pk_pv and inf.CODE =?");
		return DataBaseHelper.queryForMap(sql.toString(),codeIp);
	}

	/**
	 * 查询交款记录表 住院预交金--收费结算-交款记录
	 * @param pkpv
	 * @return
	 */
	public List<Map<String, Object>> queryBlDeposit(String pkpv) {
		if(pkpv==null || "".equals(pkpv)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select bl.AMOUNT,bl.EU_DIRECT,bl.NAME_EMP_PAY,bl.REPT_NO,to_char(bl.date_pay,'YYYYMMDDHH24MISS') date_pay");
		sql.append(" from bl_deposit bl where bl.EU_DPTYPE='9' and bl.PK_PV=? ");
		return DataBaseHelper.queryForList(sql.toString(),pkpv);
	}
	/**
	 * 查询入院诊断
	 * @param pkpv
	 * @return
	 */
	public Map<String, Object> getDiagPv(String pkpv) {
		if(pkpv==null || "".equals(pkpv)){
			return null;
		}
		String sql = "select name_diag from pv_ip where PK_PV=?";
		return DataBaseHelper.queryForMap(sql,pkpv);
	}
	/**
	 * 患者就诊总费用
	 * @param pkpv
	 * @return
	 */
	public Map<String, Object> getAcoumt(String pkpv) {
		if(pkpv==null || "".equals(pkpv)){
			return null;
		}
		String sql = "select sum(AMOUNT) sum from BL_IP_DT where PK_PV=?";
		return DataBaseHelper.queryForMap(sql,pkpv);

	}
	/**
	 * 根据第三方交易号查询his发票信息
	 * @param serialNo
	 */
	public Map<String, Object> queryBlExtPay(String serialNo) {
		Map<String, Object> result = new HashMap<>(16);
		String sql = "select dt.code_bill,dt.name_bill,pay.amount, to_char(d.date_pay, 'YYYYMMDDHH24MISS') date_pay,d.name_emp_pay, d.eu_dptype,d.rept_no,  pay.eu_paytype,pi.code_op,nvl(hp.name,'自费') name_insu,pv.code_pv from BL_EXT_PAY pay inner join BL_DEPOSIT d on pay.PK_DEPO = d.PK_DEPO left join BL_INVOICE inv on d.PK_EMPINVOICE = inv.PK_EMPINVOICE left join BL_INVOICE_DT dt on inv.PK_INVOICE = dt.PK_INVOICE left join  pi_master pi on pi.pk_pi = pay.pk_pi left join PV_ENCOUNTER pv on   pv.PK_PI = pay.PK_PI left join  bd_hp hp on  pv.pk_insu = hp.pk_hp where d.eu_direct='1' and pay.flag_pay='1' and pay.SERIAL_NO=?";
		List<Map<String, Object>> queryBlExtPay = DataBaseHelper.queryForList(sql,serialNo);
		if(queryBlExtPay!=null && queryBlExtPay.size()>0){
			result.putAll(queryBlExtPay.get(0));

			for(Map<String, Object> map : queryBlExtPay){
				String codeBill = SDMsgUtils.getPropValueStr(map, "codeBill");
				String amount = SDMsgUtils.getPropValueStr(map, "amount");
				amount = "".equals(amount)?"0":amount;
				switch (codeBill){
					//09	诊查费
					case "09":result.put("09", amount);break;
					//11	西药费
					case "11":result.put("11", amount);break;
					//12	中药费(中草药)
					case "12":result.put("12", amount);break;
					//13	材料费
					case "13":result.put("13", amount);break;
					//14	中成药
					case "14":result.put("14", amount);break;
					//21	检查费
					case "21":result.put("21", amount);break;
					//22	化验费
					case "22":result.put("22", amount);break;
					//31	治疗费
					case "31":result.put("31", amount);break;
					//33	手术费
					case "33":result.put("33", amount);break;
					//41	床位费
					case "41":result.put("41", amount);break;
					//42	护理费
					case "42":result.put("42", amount);break;
					//case "43":result.put("43", amount);break;//43	其他费
					//默认	其他费
					default:result.put("43", amount);break;
				}
			}
		}
		return result;
	}

	/**
	 * 查询 收费项目
	 * @param codeOrd
	 * @return
	 */
	public Map<String, Object> queryBdOrdByCode(String codeOrd) {
		if(codeOrd==null || "".equals(codeOrd)){
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select ord.PK_ORD, ord.PK_ORDTYPE, ord.CODE_ORDTYPE, ord.CODE code_ord, ord.NAME name_ord, ")
		.append("ord.NAME_PRT, ord.SPEC, ord.SPCODE,ord.D_CODE, ord.EU_EXCLUDE, ord.FLAG_NS, ord.FLAG_DR, ")
		.append("ord.CODE_FREQ, ord.QUAN_DEF, ord.FLAG_IP, ord.FLAG_OP,ord.FLAG_ER, ord.FLAG_HM, ord.FLAG_PE, ord.FLAG_EMR, ")
		.append("ord.FLAG_CG, ord.FLAG_PD, ord.FLAG_ACTIVE, ord.NOTE,ord.FLAG_UNIT, ord.PK_UNIT, ")
		.append("ord.EU_SEX,ord.FLAG_PED, ord.DT_ORDCATE, ord.OLD_ID, ord.AGE_MIN,ord.AGE_MAX, ord.ITEM_ID, ")
		.append("ord.DESC_ORD, ord.EXCEPT_ORD,ord.OLD_TYPE, ord.YB_ID, ord.FLAG_NOC, ord.CODE_EXT, ord.EU_ORDTYPE, ")
		.append("lab.DT_SAMPTYPE,lab.dt_colltype,lab.dt_contype, ")
		.append("ris.dt_type,ris.dt_body,ris.desc_att ")
		.append("from BD_ORD ord left join bd_ord_lab lab on ord.PK_ORD=lab.PK_ORD and lab.DEL_FLAG='0' ")
		.append("left join bd_ord_ris ris on ris.PK_ORD=ord.PK_ORD and ris.DEL_FLAG='0' ")
		.append("where ord.CODE=? and ord.DEL_FLAG='0' and ord.flag_active='1'");
		return DataBaseHelper.queryForMap(sql.toString(),codeOrd);
	}
	/**
	 * 查询入院通知单
	 * @param codePi
	 */
	public PvIpNotice queryPvIpNotice(String codePi) {
		if(codePi==null || "".equals(codePi)){
			return null;
		}
		StringBuilder sql = new StringBuilder("select notic.* from PV_IP_NOTICE notic ")
				.append("inner join PV_ENCOUNTER pv on notic.PK_PV_OP=pv.PK_PV ")
				.append("inner join PI_MASTER pi on notic.PK_PI=pi.PK_PI ")
				.append("where notic.DEL_FLAG='0' and DATE_VALID>sysdate and notic.EU_STATUS<1 ")
				.append("and pi.CODE_PI=? ");
		return DataBaseHelper.queryForBean(sql.toString(),PvIpNotice.class, codePi);
	}
	
	/**
	 * 查询草药全名
	 * @param pkCnord
	 * @return
	 */
	public String queryBdPd(String pkCnord){
		if(pkCnord==null || "".equals(pkCnord)){
			return null;
		}
		String name = sDOpMsgMapper.queryBdPd(pkCnord);
		return name==null?"":name;
	}

}
