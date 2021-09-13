package com.zebone.nhis.pro.zsba.compay.ins.qgyb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.pro.zsba.compay.ins.pub.service.InsPubSignInService;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao.InsQgybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbFunUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Iteminfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Oprninfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Payinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Setlinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaSignInQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input4101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData4101;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

@Service
public class InsQgybHandler {

	@Autowired
	private InsPubSignInService insPubSignInService;
	
	@Autowired
	private InsQgybPvMapper insQgybPvMapper;
	
	@Autowired
	private InsQgRecService insQgRecService;
	
	/**
	 * 上传结算清单
	 * @param param
	 * @param user
	 */
	public Map<String, Object> uploadSettlementList(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String pkInsstqg = jo.getString("pkInsstqg");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkInsstqg", pkInsstqg);
		
		InsZsbaStQg st = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_insstqg = ?", InsZsbaStQg.class, pkInsstqg);
		paramMap.put("pkPv", st.getPkPv());
		StringBuffer piSql = new StringBuffer("select b.code_ip, c.ip_times from PV_ENCOUNTER a inner join PI_MASTER b on a.pk_pi = b.pk_pi");
		piSql.append(" inner join pv_ip c on a.pk_pv = c.pk_pv and a.pk_pv = ?");
		Map<String, Object> piMap = DataBaseHelper.queryForMap(piSql.toString(), st.getPkPv());
		paramMap.put("codeIp", piMap.get("codeIp"));
		paramMap.put("ipTimes", piMap.get("ipTimes"));
		
		Map<String, Object> setlinfoMap  = insQgybPvMapper.get4101Setlinfo(paramMap);
		List<Map<String, Object>> payinfoMapList  = insQgybPvMapper.get4101Payinfo(paramMap);
		List<Map<String, Object>> iteminfo2301MapList  = insQgybPvMapper.get4101Iteminfo2301(paramMap);
		List<Map<String, Object>> iteminfo5204MapList  = insQgybPvMapper.get4101Iteminfo5204(paramMap);
		
		//切换数据源
		DataSourceRoute.putAppId("BAGL_bayy");
		List<Map<String, Object>> diseinfoMapList  = insQgybPvMapper.get4101DiseinfoBaxt(paramMap);
		List<Map<String, Object>> oprninfoMapList  = insQgybPvMapper.get4101OprninfoBaxt(paramMap);
		//切回默认数据源
		DataSourceRoute.putAppId("default");
		
		Input4101 input = new Input4101();
		Input4101Setlinfo setlinfo = new Input4101Setlinfo();
		List<Input4101Payinfo> payinfoList = new ArrayList<Input4101Payinfo>();
		List<Input4101Diseinfo> diseinfoList = new ArrayList<Input4101Diseinfo>();
		List<Input4101Iteminfo> iteminfoList = new ArrayList<Input4101Iteminfo>();
		List<Input4101Oprninfo> oprninfoList = new ArrayList<Input4101Oprninfo>();
		
		if(setlinfoMap!=null){
				if(diseinfoMapList!=null && diseinfoMapList.size()!=0)
				{
					if((iteminfo2301MapList!=null && iteminfo2301MapList.size()!=0)||(iteminfo5204MapList!=null && iteminfo5204MapList.size()!=0))
					{
						InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
						if(signIn.getCode().equals("0")){
							setlinfo.setMdtrt_id(setlinfoMap.get("mdtrt_id")==null?null:setlinfoMap.get("mdtrt_id").toString());
							setlinfo.setSetl_id(setlinfoMap.get("setl_id")==null?null:setlinfoMap.get("setl_id").toString());
							setlinfo.setFixmedins_name(setlinfoMap.get("fixmedins_name")==null?null:setlinfoMap.get("fixmedins_name").toString());
							setlinfo.setFixmedins_code(setlinfoMap.get("fixmedins_code")==null?null:setlinfoMap.get("fixmedins_code").toString());
							setlinfo.setHi_setl_lv(setlinfoMap.get("hi_setl_lv")==null?null:setlinfoMap.get("hi_setl_lv").toString());
							setlinfo.setHi_no(setlinfoMap.get("hi_no")==null?null:setlinfoMap.get("hi_no").toString());
							setlinfo.setMedcasno(setlinfoMap.get("medcasno")==null?null:setlinfoMap.get("medcasno").toString());
							setlinfo.setDcla_time(setlinfoMap.get("dcla_time")==null?null:setlinfoMap.get("dcla_time").toString());
							setlinfo.setPsn_name(setlinfoMap.get("psn_name")==null?null:setlinfoMap.get("psn_name").toString());
							setlinfo.setGend(setlinfoMap.get("gend")==null?null:setlinfoMap.get("gend").toString());
							setlinfo.setBrdy(setlinfoMap.get("brdy")==null?null:setlinfoMap.get("brdy").toString());
							setlinfo.setAge(setlinfoMap.get("age")==null?null:setlinfoMap.get("age").toString());
							setlinfo.setNtly(setlinfoMap.get("ntly")==null?null:setlinfoMap.get("ntly").toString());
							setlinfo.setNwb_age(setlinfoMap.get("nwb_age")==null?null:Integer.parseInt(setlinfoMap.get("nwb_age").toString())>365?null:setlinfoMap.get("nwb_age").toString());
							setlinfo.setNaty(setlinfoMap.get("naty")==null?null:setlinfoMap.get("naty").toString());
							setlinfo.setPatn_cert_type(setlinfoMap.get("patn_cert_type")==null?null:setlinfoMap.get("patn_cert_type").toString());
							setlinfo.setCertno(setlinfoMap.get("certno")==null?null:setlinfoMap.get("certno").toString());
							setlinfo.setPrfs(setlinfoMap.get("prfs")==null?null:setlinfoMap.get("prfs").toString());
							setlinfo.setCurr_addr(setlinfoMap.get("curr_addr")==null?null:setlinfoMap.get("curr_addr").toString());
							setlinfo.setEmp_name(setlinfoMap.get("emp_name")==null?null:setlinfoMap.get("emp_name").toString());
							setlinfo.setEmp_addr(setlinfoMap.get("emp_addr")==null?null:setlinfoMap.get("emp_addr").toString());
							setlinfo.setEmp_tel(setlinfoMap.get("emp_tel")==null?null:setlinfoMap.get("emp_tel").toString());
							setlinfo.setPoscode(setlinfoMap.get("poscode")==null?null:setlinfoMap.get("poscode").toString());
							setlinfo.setConer_name(setlinfoMap.get("coner_name")==null?null:setlinfoMap.get("coner_name").toString());
							setlinfo.setPatn_rlts(setlinfoMap.get("patn_rlts")==null?null:setlinfoMap.get("patn_rlts").toString());
							setlinfo.setConer_addr(setlinfoMap.get("coner_addr")==null?null:setlinfoMap.get("coner_addr").toString());
							setlinfo.setConer_tel(setlinfoMap.get("coner_tel")==null?null:setlinfoMap.get("coner_tel").toString());
							setlinfo.setHi_type(setlinfoMap.get("hi_type")==null?null:setlinfoMap.get("hi_type").toString());
							setlinfo.setInsuplc(setlinfoMap.get("insuplc")==null?null:setlinfoMap.get("insuplc").toString());
							setlinfo.setSp_psn_type(setlinfoMap.get("sp_psn_type")==null?null:setlinfoMap.get("sp_psn_type").toString());
							setlinfo.setNwb_adm_type(setlinfoMap.get("nwb_adm_type")==null?null:setlinfoMap.get("nwb_adm_type").toString());
							setlinfo.setNwb_bir_wt(setlinfoMap.get("nwb_bir_wt")==null?null:setlinfoMap.get("nwb_bir_wt").toString());
							setlinfo.setNwb_adm_wt(setlinfoMap.get("nwb_adm_wt")==null?null:setlinfoMap.get("nwb_adm_wt").toString());
							setlinfo.setOpsp_diag_caty(setlinfoMap.get("opsp_diag_caty")==null?null:setlinfoMap.get("opsp_diag_caty").toString());
							setlinfo.setOpsp_mdtrt_date(setlinfoMap.get("opsp_mdtrt_date")==null?null:setlinfoMap.get("opsp_mdtrt_date").toString());
							setlinfo.setIpt_med_type(setlinfoMap.get("ipt_med_type")==null?null:setlinfoMap.get("ipt_med_type").toString());
							setlinfo.setAdm_way(setlinfoMap.get("adm_way")==null?null:setlinfoMap.get("adm_way").toString());
							setlinfo.setTrt_type(setlinfoMap.get("trt_type")==null?null:setlinfoMap.get("trt_type").toString());
							setlinfo.setAdm_time(setlinfoMap.get("adm_time")==null?null:setlinfoMap.get("adm_time").toString());
							setlinfo.setAdm_caty(setlinfoMap.get("adm_caty")==null?null:setlinfoMap.get("adm_caty").toString());
							//转科科别 数据库存的是名字，得单独查
							//setlinfo.setRefldept_dept(setlinfoMap.get("refldept_dept")==null?null:setlinfoMap.get("refldept_dept").toString());
							setlinfo.setDscg_time(setlinfoMap.get("dscg_time")==null?null:setlinfoMap.get("dscg_time").toString());
							setlinfo.setDscg_caty(setlinfoMap.get("dscg_caty")==null?null:setlinfoMap.get("dscg_caty").toString());
							setlinfo.setAct_ipt_days(setlinfoMap.get("act_ipt_days")==null?null:setlinfoMap.get("act_ipt_days").toString());
							setlinfo.setOtp_wm_dise(setlinfoMap.get("otp_wm_dise")==null?null:setlinfoMap.get("otp_wm_dise").toString());
							setlinfo.setWm_dise_code(setlinfoMap.get("wm_dise_code")==null?null:setlinfoMap.get("wm_dise_code").toString());
							setlinfo.setOtp_tcm_dise(setlinfoMap.get("otp_tcm_dise")==null?null:setlinfoMap.get("otp_tcm_dise").toString());
							setlinfo.setTcm_dise_code(setlinfoMap.get("tcm_dise_code")==null?null:setlinfoMap.get("tcm_dise_code").toString());
							//诊断代码计数  需要单独查
							setlinfo.setDiag_code_cnt(diseinfoMapList.size()+"");
							//手术操作代码计数
							//setlinfo.setOprn_oprt_code_cnt(oprninfoMapList.size()+"");
							setlinfo.setVent_used_dura(setlinfoMap.get("vent_used_dura")==null?null:setlinfoMap.get("vent_used_dura").toString());
							String pwcry_bfadm_coma_dura = "";
							String pwcry_afadm_coma_dura = "";
							if(setlinfoMap.get("coma_day_bef")!=null){
								pwcry_bfadm_coma_dura += setlinfoMap.get("coma_day_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "0";
							}
							if(setlinfoMap.get("coma_hour_bef")!=null){
								pwcry_bfadm_coma_dura += "/"+setlinfoMap.get("coma_hour_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "/0";
							}
							if(setlinfoMap.get("coma_min_bef")!=null){
								pwcry_bfadm_coma_dura += "/"+setlinfoMap.get("coma_min_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "/0";
							}
							if(pwcry_bfadm_coma_dura.equals("0/0/0")){
								pwcry_bfadm_coma_dura = null;
							}
							if(setlinfoMap.get("coma_day_after")!=null){
								pwcry_afadm_coma_dura += setlinfoMap.get("coma_day_after").toString();
							}else{
								pwcry_afadm_coma_dura += "0";
							}
							if(setlinfoMap.get("coma_hour_after")!=null){
								pwcry_afadm_coma_dura += "/"+setlinfoMap.get("coma_hour_after").toString();
							}else{
								pwcry_afadm_coma_dura += "/0";
							}
							if(setlinfoMap.get("coma_min_after")!=null){
								pwcry_afadm_coma_dura += "/"+setlinfoMap.get("coma_min_after").toString();
							}else{
								pwcry_afadm_coma_dura += "/0";
							}
							if(pwcry_afadm_coma_dura.equals("0/0/0")){
								pwcry_afadm_coma_dura = null;
							}
							setlinfo.setPwcry_bfadm_coma_dura(pwcry_bfadm_coma_dura);
							setlinfo.setPwcry_afadm_coma_dura(pwcry_afadm_coma_dura);
							//setlinfo.setBld_cat(setlinfoMap.get("bld_cat")==null?null:setlinfoMap.get("bld_cat").toString());
							//setlinfo.setBld_amt(setlinfoMap.get("bld_amt")==null?null:setlinfoMap.get("bld_amt").toString());
							//setlinfo.setBld_unt(setlinfoMap.get("bld_unt")==null?null:setlinfoMap.get("bld_unt").toString());
							//setlinfo.setSpga_nurscare_days(setlinfoMap.get("spga_nurscare_days")==null?null:setlinfoMap.get("spga_nurscare_days").toString());
							//setlinfo.setLv1_nurscare_days(setlinfoMap.get("lv1_nurscare_days")==null?null:setlinfoMap.get("lv1_nurscare_days").toString());
							//setlinfo.setScd_nurscare_days(setlinfoMap.get("scd_nurscare_days")==null?null:setlinfoMap.get("scd_nurscare_days").toString());
							//setlinfo.setLv3_nurscare_days(setlinfoMap.get("lv3_nurscare_days")==null?null:setlinfoMap.get("lv3_nurscare_days").toString());
							setlinfo.setDscg_way(setlinfoMap.get("dscg_way")==null?null:setlinfoMap.get("dscg_way").toString());
							setlinfo.setAcp_medins_name(setlinfoMap.get("acp_medins_name")==null?null:setlinfoMap.get("acp_medins_name").toString());
							setlinfo.setAcp_optins_code(setlinfoMap.get("acp_optins_code")==null?null:setlinfoMap.get("acp_optins_code").toString());
							setlinfo.setBill_code(setlinfoMap.get("bill_code")==null?null:setlinfoMap.get("bill_code").toString());
							setlinfo.setBill_no(setlinfoMap.get("bill_no")==null?null:setlinfoMap.get("bill_no").toString());
							setlinfo.setBiz_sn(setlinfoMap.get("biz_sn")==null?null:setlinfoMap.get("biz_sn").toString());
							//setlinfo.setDays_rinp_flag_31(setlinfoMap.get("days_rinp_flag_31")==null?null:setlinfoMap.get("days_rinp_flag_31").toString());
							//setlinfo.setDays_rinp_pup_31(setlinfoMap.get("days_rinp_pup_31")==null?null:setlinfoMap.get("days_rinp_pup_31").toString());
							setlinfo.setChfpdr_name(setlinfoMap.get("chfpdr_name")==null?null:setlinfoMap.get("chfpdr_name").toString());
							setlinfo.setChfpdr_code(setlinfoMap.get("chfpdr_code")==null?null:setlinfoMap.get("chfpdr_code").toString());
							setlinfo.setSetl_begn_date(setlinfoMap.get("setl_begn_date")==null?null:setlinfoMap.get("setl_begn_date").toString());
							setlinfo.setSetl_end_date(setlinfoMap.get("setl_end_date")==null?null:setlinfoMap.get("setl_end_date").toString());
							setlinfo.setPsn_selfpay(setlinfoMap.get("psn_selfpay")==null?null:setlinfoMap.get("psn_selfpay").toString());
							setlinfo.setPsn_ownpay(setlinfoMap.get("psn_ownpay")==null?null:setlinfoMap.get("psn_ownpay").toString());
							setlinfo.setAcct_pay(setlinfoMap.get("acct_pay")==null?null:setlinfoMap.get("acct_pay").toString());
							setlinfo.setPsn_cashpay(setlinfoMap.get("psn_cashpay")==null?null:setlinfoMap.get("psn_cashpay").toString());
							setlinfo.setHi_paymtd(setlinfoMap.get("hi_paymtd")==null?null:setlinfoMap.get("hi_paymtd").toString());
							setlinfo.setHsorg(setlinfoMap.get("hsorg")==null?null:setlinfoMap.get("hsorg").toString());
							setlinfo.setHsorg_opter(setlinfoMap.get("hsorg_opter")==null?null:setlinfoMap.get("hsorg_opter").toString());
							User currUser = UserContext.getUser();
							BdOuDept dept = DataBaseHelper.queryForBean(" select * from BD_OU_DEPT where pk_dept = ?", BdOuDept.class, currUser.getPkDept());
							setlinfo.setMedins_fill_dept(dept.getNameDept());
							setlinfo.setMedins_fill_psn(user.getUserName());
							
							
							//基金支付信息（节点标识：payinfo）
							for (Map<String, Object> map : payinfoMapList) {
								Input4101Payinfo payinfo = new Input4101Payinfo();
								payinfo.setFund_pay_type(map.get("fund_pay_type").toString());
								payinfo.setFund_payamt(map.get("fund_payamt").toString());
								payinfoList.add(payinfo);
							}
							input.setPayinfo(payinfoList);
							
							//门诊慢特病诊断信息（节点标识：opspdiseinfo）
							//住院的不用管
							
							//住院诊断信息（节点标识：diseinfo）
							for (Map<String, Object> map : diseinfoMapList) {
								Input4101Diseinfo diseinfo = new Input4101Diseinfo();
								diseinfo.setDiag_type(map.get("maindiag_flag").toString().equals("1")?"1":"2");
								diseinfo.setDiag_code(map.get("diag_code").toString());
								diseinfo.setDiag_name(map.get("diag_name").toString());
								diseinfo.setAdm_cond_type(map.get("adm_cond_type").toString());
								diseinfo.setMaindiag_flag(map.get("maindiag_flag").toString().equals("1")?"1":"0");
								diseinfoList.add(diseinfo);
							}
							input.setDiseinfo(diseinfoList);
							
							// 收费项目信息（节点标识：iteminfo）
							if(iteminfo2301MapList!=null && iteminfo2301MapList.size()>0){
								for (Map<String, Object> map : iteminfo2301MapList) {
									Input4101Iteminfo iteminfo = new Input4101Iteminfo();
									iteminfo.setMed_chrgitm(map.get("med_chrgitm_type") == null?"0":map.get("med_chrgitm_type").toString());
									iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
									iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
									iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
									iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
									iteminfo.setOth_amt("0.00");
									iteminfoList.add(iteminfo);
								}
							}else{
								for (Map<String, Object> map : iteminfo5204MapList) {
									Input4101Iteminfo iteminfo = new Input4101Iteminfo();
									iteminfo.setMed_chrgitm(map.get("med_chrgitm_type") == null?"0":map.get("med_chrgitm_type").toString());
									iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
									iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
									iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
									iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
									iteminfo.setOth_amt("0.00");
									iteminfoList.add(iteminfo);
								}
							}
							input.setIteminfo(iteminfoList);
							
							String error = null;
							
							//手术操作信息（节点标识：oprninfo）
							int ssNum = 0;
							if(oprninfoMapList.size()>0){
								for(int i=0; i<oprninfoMapList.size(); i++){
									Map<String, Object> map = oprninfoMapList.get(i);
									StringBuffer ssSql = new StringBuffer("select * from ins_zzfs_zd where zzfs_code = ? ");
									List<Map<String, Object>> ybssMapList = DataBaseHelper.queryForList(ssSql.toString(), map.get("oprn_oprt_code").toString());
									//if(ybssMapList!=null&&ybssMapList.size()>0){
										Input4101Oprninfo oprninfo = new Input4101Oprninfo();
										if(i==0){
											oprninfo.setOprn_oprt_type("1");  //1	主要手术及操作	2	其他手术及操作
										}else{
											oprninfo.setOprn_oprt_type("2");  //1	主要手术及操作	2	其他手术及操作
										}
										if(ybssMapList!=null&&ybssMapList.size()>0){
											oprninfo.setOprn_oprt_code(ybssMapList.get(0).get("zzfsCode2").toString());
										}else{
											oprninfo.setOprn_oprt_code(map.get("oprn_oprt_code").toString());
										}
										//oprninfo.setOprn_oprt_code(ybssMapList.get(0).get("zzfsCode2").toString());//手术码要匹配诊治编码库的编码，匹配不到的不上传，匹配到的上传医保手术编码2.0
										oprninfo.setOprn_oprt_name(map.get("oprn_oprt_name").toString());
										oprninfo.setOprn_oprt_date(map.get("oprn_oprt_date").toString());
										oprninfo.setAnst_way(map.get("anst_way")==null?null:map.get("anst_way").toString());
										
										String ybysSql = "select * from lsb_ybys where xm = ? and ybysdm is not null and ybysdm!=''";
										if(map.get("oper_dr_name") != null){
											List<Map<String, Object>> ybysMapList = DataBaseHelper.queryForList(ybysSql, map.get("oper_dr_name").toString());
											if(ybysMapList!=null&&ybysMapList.size()>0){
												oprninfo.setOper_dr_code(ybysMapList.get(0).get("ybysdm").toString());
											}else{
												if(map.get("oper_dr_code")==null){
													ybysSql = "select * from BD_OU_EMPLOYEE where name_emp = ?";
													List<Map<String, Object>> empMapList = DataBaseHelper.queryForList(ybysSql, map.get("oper_dr_name").toString());
													if(empMapList!=null&&empMapList.size()>0){
														oprninfo.setOper_dr_code(empMapList.get(0).get("codeEmp").toString());
													}else{
														error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"的医生("+map.get("oper_dr_name").toString()+")的编码为空!";
														break;
													}
												}else{
													oprninfo.setOper_dr_code(map.get("oper_dr_code").toString());
												}
												
											}
										}else{
											error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"的医生姓名为空!";
											break;
										}
										
										oprninfo.setOper_dr_name(map.get("oper_dr_name").toString());
										if(map.get("anst_dr_code")!=null){
											List<Map<String, Object>> ybmzsMapList = DataBaseHelper.queryForList(ybysSql, map.get("anst_dr_name").toString());
											if(ybmzsMapList!=null&&ybmzsMapList.size()>0){
												oprninfo.setAnst_dr_code(ybmzsMapList.get(0).get("ybysdm").toString());
											}else{
												oprninfo.setAnst_dr_code(map.get("oper_dr_code").toString());
											}
										}else{
											oprninfo.setAnst_dr_code(map.get("anst_dr_code")==null?null:map.get("anst_dr_code").toString());
										}
										oprninfo.setAnst_dr_name(map.get("anst_dr_name")==null?null:map.get("anst_dr_name").toString());
										oprninfoList.add(oprninfo);
										ssNum++;
									/*}else{
										error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"("+map.get("oprn_oprt_code").toString()+")查不到医保2.0手术编码!";
										break;
									}*/
								}
								input.setOprninfo(oprninfoList);
							}
							setlinfo.setOprn_oprt_code_cnt(ssNum+"");
							input.setSetlinfo(setlinfo);
							
							//重症监护信息（节点标识：icuinfo）
							//先不传了，后面再补上
							
							if(error==null){
								OutputData4101 out4101 = YbFunUtils.fun4101(input,  signIn.getSignNo());
								if(out4101.getInfcode()==null || !out4101.getInfcode().equals("0")){
									Map<String, Object> ry = new HashMap<String, Object>();
									returnMap.put("code", "-1");
									returnMap.put("msg",  out4101.getErr_msg()+(out4101.getMessage()==null?"":out4101.getMessage()));
								}else{
									//DataBaseHelper.execute("update ins_st_qg set SETL_LIST_ID = ?, DCLA_TIME = ?, MEDINS_FILL_DEPT = ?, MEDINS_FILL_PSN = ? where pk_insstqg=? ", 
										//	out4101.getOutput().getSetl_list_id(), setlinfo.getDcla_time(), setlinfo.getMedins_fill_dept(), setlinfo.getMedins_fill_psn(), pkInsstqg);
									returnMap.put("setlListId", out4101.getOutput().getSetl_list_id());
									returnMap.put("dclaTime", setlinfo.getDcla_time());
									returnMap.put("medinsFillDept", setlinfo.getMedins_fill_dept());
									returnMap.put("medinsFillPsn", setlinfo.getMedins_fill_psn());
									returnMap.put("pkInsstqg", pkInsstqg);
									returnMap.put("code", "0");
									returnMap.put("msg",  "");
								}
							}else{
								returnMap.put("code", "-1");
								returnMap.put("msg",  error);
							}
						}else{
							returnMap.put("code", signIn.getCode());
							returnMap.put("msg",  signIn.getMsg());
						}
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", "没有查询到收费项目信息！");
					}
				}else{
					returnMap.put("code", "-1");
					returnMap.put("msg", "没有查询到住院诊断信息！");
				}
		}else{
			EmrHomePage page = DataBaseHelper.queryForBean("select * from emr_home_page where pk_pv = ?", EmrHomePage.class, st.getPkPv());
			if(page==null){
				PvEncounter pv = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ? and creator like '%导入%'", PvEncounter.class, st.getPkPv());
				if(pv==null){
					returnMap.put("code", "-1");
					returnMap.put("msg", "查询不到该患者病案首页信息！");
				}else{
					//returnMap.put("code", "-1");
					//returnMap.put("msg", "该患者为旧系统导入新系统结算的患者，暂时无法上传医保结算单！");
					Map<String, Object> setlinfoJxtMap  = insQgybPvMapper.get4101SetlinfoJxt(paramMap);
					//List<Map<String, Object>> diseinfoMapJxtList  = insQgybPvMapper.get4101DiseinfoJxt(paramMap);
					//List<Map<String, Object>> oprninfoMapJxtList  = insQgybPvMapper.get4101OprninfoJxt(paramMap);
					if(setlinfoJxtMap!=null){
						/*if(diseinfoMapJxtList!=null && diseinfoMapJxtList.size()!=0)
						{*/
							if((iteminfo2301MapList!=null && iteminfo2301MapList.size()!=0)||(iteminfo5204MapList!=null && iteminfo5204MapList.size()!=0))
							{
								InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
								if(signIn.getCode().equals("0")){
									setlinfo.setMdtrt_id(setlinfoJxtMap.get("mdtrt_id")==null?null:setlinfoJxtMap.get("mdtrt_id").toString());
									setlinfo.setSetl_id(setlinfoJxtMap.get("setl_id")==null?null:setlinfoJxtMap.get("setl_id").toString());
									setlinfo.setFixmedins_name(setlinfoJxtMap.get("fixmedins_name")==null?null:setlinfoJxtMap.get("fixmedins_name").toString());
									setlinfo.setFixmedins_code(setlinfoJxtMap.get("fixmedins_code")==null?null:setlinfoJxtMap.get("fixmedins_code").toString());
									setlinfo.setHi_setl_lv(setlinfoJxtMap.get("hi_setl_lv")==null?null:setlinfoJxtMap.get("hi_setl_lv").toString());
									setlinfo.setHi_no(setlinfoJxtMap.get("hi_no")==null?null:setlinfoJxtMap.get("hi_no").toString());
									setlinfo.setMedcasno(setlinfoJxtMap.get("medcasno")==null?null:setlinfoJxtMap.get("medcasno").toString());
									setlinfo.setDcla_time(setlinfoJxtMap.get("dcla_time")==null?null:setlinfoJxtMap.get("dcla_time").toString());
									setlinfo.setPsn_name(setlinfoJxtMap.get("psn_name")==null?null:setlinfoJxtMap.get("psn_name").toString());
									setlinfo.setGend(setlinfoJxtMap.get("gend")==null?null:setlinfoJxtMap.get("gend").toString());
									setlinfo.setBrdy(setlinfoJxtMap.get("brdy")==null?null:setlinfoJxtMap.get("brdy").toString());
									setlinfo.setAge(setlinfoJxtMap.get("age")==null?null:setlinfoJxtMap.get("age").toString());
									setlinfo.setNtly(setlinfoJxtMap.get("ntly")==null?null:setlinfoJxtMap.get("ntly").toString());
									setlinfo.setNwb_age(setlinfoJxtMap.get("nwb_age")==null?null:Integer.parseInt(setlinfoJxtMap.get("nwb_age").toString())>365?null:setlinfoJxtMap.get("nwb_age").toString());
									setlinfo.setNaty(setlinfoJxtMap.get("naty")==null?null:setlinfoJxtMap.get("naty").toString());
									setlinfo.setPatn_cert_type(setlinfoJxtMap.get("patn_cert_type")==null?null:setlinfoJxtMap.get("patn_cert_type").toString());
									setlinfo.setCertno(setlinfoJxtMap.get("certno")==null?null:setlinfoJxtMap.get("certno").toString());
									setlinfo.setPrfs(setlinfoJxtMap.get("prfs")==null?null:setlinfoJxtMap.get("prfs").toString());
									setlinfo.setCurr_addr(setlinfoJxtMap.get("curr_addr")==null?null:setlinfoJxtMap.get("curr_addr").toString());
									setlinfo.setEmp_name(setlinfoJxtMap.get("emp_name")==null?null:setlinfoJxtMap.get("emp_name").toString());
									setlinfo.setEmp_addr(setlinfoJxtMap.get("emp_addr")==null?null:setlinfoJxtMap.get("emp_addr").toString());
									setlinfo.setEmp_tel(setlinfoJxtMap.get("emp_tel")==null?null:setlinfoJxtMap.get("emp_tel").toString());
									setlinfo.setPoscode(setlinfoJxtMap.get("poscode")==null?null:setlinfoJxtMap.get("poscode").toString());
									setlinfo.setConer_name(setlinfoJxtMap.get("coner_name")==null?null:setlinfoJxtMap.get("coner_name").toString());
									setlinfo.setPatn_rlts(setlinfoJxtMap.get("patn_rlts")==null?null:setlinfoJxtMap.get("patn_rlts").toString());
									setlinfo.setConer_addr(setlinfoJxtMap.get("coner_addr")==null?null:setlinfoJxtMap.get("coner_addr").toString());
									setlinfo.setConer_tel(setlinfoJxtMap.get("coner_tel")==null?null:setlinfoJxtMap.get("coner_tel").toString());
									setlinfo.setHi_type(setlinfoJxtMap.get("hi_type")==null?null:setlinfoJxtMap.get("hi_type").toString());
									setlinfo.setInsuplc(setlinfoJxtMap.get("insuplc")==null?null:setlinfoJxtMap.get("insuplc").toString());
									setlinfo.setSp_psn_type(setlinfoJxtMap.get("sp_psn_type")==null?null:setlinfoJxtMap.get("sp_psn_type").toString());
									setlinfo.setNwb_adm_type(setlinfoJxtMap.get("nwb_adm_type")==null?null:setlinfoJxtMap.get("nwb_adm_type").toString());
									setlinfo.setNwb_bir_wt(setlinfoJxtMap.get("nwb_bir_wt")==null?null:setlinfoJxtMap.get("nwb_bir_wt").toString());
									setlinfo.setNwb_adm_wt(setlinfoJxtMap.get("nwb_adm_wt")==null?null:setlinfoJxtMap.get("nwb_adm_wt").toString());
									setlinfo.setOpsp_diag_caty(setlinfoJxtMap.get("opsp_diag_caty")==null?null:setlinfoJxtMap.get("opsp_diag_caty").toString());
									setlinfo.setOpsp_mdtrt_date(setlinfoJxtMap.get("opsp_mdtrt_date")==null?null:setlinfoJxtMap.get("opsp_mdtrt_date").toString());
									setlinfo.setIpt_med_type(setlinfoJxtMap.get("ipt_med_type")==null?null:setlinfoJxtMap.get("ipt_med_type").toString());
									setlinfo.setAdm_way(setlinfoJxtMap.get("adm_way")==null?null:setlinfoJxtMap.get("adm_way").toString());
									setlinfo.setTrt_type(setlinfoJxtMap.get("trt_type")==null?null:setlinfoJxtMap.get("trt_type").toString());
									setlinfo.setAdm_time(setlinfoJxtMap.get("adm_time")==null?null:setlinfoJxtMap.get("adm_time").toString());
									setlinfo.setAdm_caty(setlinfoJxtMap.get("adm_caty")==null?null:setlinfoJxtMap.get("adm_caty").toString());
									//转科科别 数据库存的是名字，得单独查
									//setlinfo.setRefldept_dept(setlinfoMap.get("refldept_dept")==null?null:setlinfoMap.get("refldept_dept").toString());
									setlinfo.setDscg_time(setlinfoJxtMap.get("dscg_time")==null?null:setlinfoJxtMap.get("dscg_time").toString());
									setlinfo.setDscg_caty(setlinfoJxtMap.get("dscg_caty")==null?null:setlinfoJxtMap.get("dscg_caty").toString());
									setlinfo.setAct_ipt_days(setlinfoJxtMap.get("act_ipt_days")==null?null:setlinfoJxtMap.get("act_ipt_days").toString());
									setlinfo.setOtp_wm_dise(setlinfoJxtMap.get("otp_wm_dise")==null?null:setlinfoJxtMap.get("otp_wm_dise").toString());
									setlinfo.setWm_dise_code(setlinfoJxtMap.get("wm_dise_code")==null?null:setlinfoJxtMap.get("wm_dise_code").toString());
									setlinfo.setOtp_tcm_dise(setlinfoJxtMap.get("otp_tcm_dise")==null?null:setlinfoJxtMap.get("otp_tcm_dise").toString());
									setlinfo.setTcm_dise_code(setlinfoJxtMap.get("tcm_dise_code")==null?null:setlinfoJxtMap.get("tcm_dise_code").toString());
									//诊断代码计数  需要单独查
									setlinfo.setDiag_code_cnt(diseinfoMapList.size()+"");
									//手术操作代码计数
									//setlinfo.setOprn_oprt_code_cnt(oprninfoMapJxtList.size()+"");
									setlinfo.setVent_used_dura(setlinfoJxtMap.get("vent_used_dura")==null?null:setlinfoJxtMap.get("vent_used_dura").toString());
									String pwcry_bfadm_coma_dura = "";
									String pwcry_afadm_coma_dura = "";
									if(setlinfoJxtMap.get("coma_day_bef")!=null){
										pwcry_bfadm_coma_dura += setlinfoJxtMap.get("coma_day_bef").toString();
									}else{
										pwcry_bfadm_coma_dura += "0";
									}
									if(setlinfoJxtMap.get("coma_hour_bef")!=null){
										pwcry_bfadm_coma_dura += "/"+setlinfoJxtMap.get("coma_hour_bef").toString();
									}else{
										pwcry_bfadm_coma_dura += "/0";
									}
									if(setlinfoJxtMap.get("coma_min_bef")!=null){
										pwcry_bfadm_coma_dura += "/"+setlinfoJxtMap.get("coma_min_bef").toString();
									}else{
										pwcry_bfadm_coma_dura += "/0";
									}
									if(pwcry_bfadm_coma_dura.equals("0/0/0")){
										pwcry_bfadm_coma_dura = null;
									}
									if(setlinfoJxtMap.get("coma_day_after")!=null){
										pwcry_afadm_coma_dura += setlinfoJxtMap.get("coma_day_after").toString();
									}else{
										pwcry_afadm_coma_dura += "0";
									}
									if(setlinfoJxtMap.get("coma_hour_after")!=null){
										pwcry_afadm_coma_dura += "/"+setlinfoJxtMap.get("coma_hour_after").toString();
									}else{
										pwcry_afadm_coma_dura += "/0";
									}
									if(setlinfoJxtMap.get("coma_min_after")!=null){
										pwcry_afadm_coma_dura += "/"+setlinfoJxtMap.get("coma_min_after").toString();
									}else{
										pwcry_afadm_coma_dura += "/0";
									}
									if(pwcry_afadm_coma_dura.equals("0/0/0")){
										pwcry_afadm_coma_dura = null;
									}
									setlinfo.setPwcry_bfadm_coma_dura(pwcry_bfadm_coma_dura);
									setlinfo.setPwcry_afadm_coma_dura(pwcry_afadm_coma_dura);
									//setlinfo.setBld_cat(setlinfoMap.get("bld_cat")==null?null:setlinfoMap.get("bld_cat").toString());
									//setlinfo.setBld_amt(setlinfoMap.get("bld_amt")==null?null:setlinfoMap.get("bld_amt").toString());
									//setlinfo.setBld_unt(setlinfoMap.get("bld_unt")==null?null:setlinfoMap.get("bld_unt").toString());
									//setlinfo.setSpga_nurscare_days(setlinfoMap.get("spga_nurscare_days")==null?null:setlinfoMap.get("spga_nurscare_days").toString());
									//setlinfo.setLv1_nurscare_days(setlinfoMap.get("lv1_nurscare_days")==null?null:setlinfoMap.get("lv1_nurscare_days").toString());
									//setlinfo.setScd_nurscare_days(setlinfoMap.get("scd_nurscare_days")==null?null:setlinfoMap.get("scd_nurscare_days").toString());
									//setlinfo.setLv3_nurscare_days(setlinfoMap.get("lv3_nurscare_days")==null?null:setlinfoMap.get("lv3_nurscare_days").toString());
									setlinfo.setDscg_way(setlinfoJxtMap.get("dscg_way")==null?null:setlinfoJxtMap.get("dscg_way").toString());
									setlinfo.setAcp_medins_name(setlinfoJxtMap.get("acp_medins_name")==null?null:setlinfoJxtMap.get("acp_medins_name").toString());
									setlinfo.setAcp_optins_code(setlinfoJxtMap.get("acp_optins_code")==null?null:setlinfoJxtMap.get("acp_optins_code").toString());
									setlinfo.setBill_code(setlinfoJxtMap.get("bill_code")==null?null:setlinfoJxtMap.get("bill_code").toString());
									setlinfo.setBill_no(setlinfoJxtMap.get("bill_no")==null?null:setlinfoJxtMap.get("bill_no").toString());
									setlinfo.setBiz_sn(setlinfoJxtMap.get("biz_sn")==null?null:setlinfoJxtMap.get("biz_sn").toString());
									//setlinfo.setDays_rinp_flag_31(setlinfoMap.get("days_rinp_flag_31")==null?null:setlinfoMap.get("days_rinp_flag_31").toString());
									//setlinfo.setDays_rinp_pup_31(setlinfoMap.get("days_rinp_pup_31")==null?null:setlinfoMap.get("days_rinp_pup_31").toString());
									setlinfo.setChfpdr_name(setlinfoJxtMap.get("chfpdr_name")==null?null:setlinfoJxtMap.get("chfpdr_name").toString());
									setlinfo.setChfpdr_code(setlinfoJxtMap.get("chfpdr_code")==null?null:setlinfoJxtMap.get("chfpdr_code").toString());
									setlinfo.setSetl_begn_date(setlinfoJxtMap.get("setl_begn_date")==null?null:setlinfoJxtMap.get("setl_begn_date").toString());
									setlinfo.setSetl_end_date(setlinfoJxtMap.get("setl_end_date")==null?null:setlinfoJxtMap.get("setl_end_date").toString());
									setlinfo.setPsn_selfpay(setlinfoJxtMap.get("psn_selfpay")==null?null:setlinfoJxtMap.get("psn_selfpay").toString());
									setlinfo.setPsn_ownpay(setlinfoJxtMap.get("psn_ownpay")==null?null:setlinfoJxtMap.get("psn_ownpay").toString());
									setlinfo.setAcct_pay(setlinfoJxtMap.get("acct_pay")==null?null:setlinfoJxtMap.get("acct_pay").toString());
									setlinfo.setPsn_cashpay(setlinfoJxtMap.get("psn_cashpay")==null?null:setlinfoJxtMap.get("psn_cashpay").toString());
									setlinfo.setHi_paymtd(setlinfoJxtMap.get("hi_paymtd")==null?null:setlinfoJxtMap.get("hi_paymtd").toString());
									setlinfo.setHsorg(setlinfoJxtMap.get("hsorg")==null?null:setlinfoJxtMap.get("hsorg").toString());
									setlinfo.setHsorg_opter(setlinfoJxtMap.get("hsorg_opter")==null?null:setlinfoJxtMap.get("hsorg_opter").toString());
									User currUser = UserContext.getUser();
									BdOuDept dept = DataBaseHelper.queryForBean(" select * from BD_OU_DEPT where pk_dept = ?", BdOuDept.class, currUser.getPkDept());
									setlinfo.setMedins_fill_dept(dept.getNameDept());
									setlinfo.setMedins_fill_psn(user.getUserName());
									input.setSetlinfo(setlinfo);
									
									//基金支付信息（节点标识：payinfo）
									for (Map<String, Object> map : payinfoMapList) {
										Input4101Payinfo payinfo = new Input4101Payinfo();
										payinfo.setFund_pay_type(map.get("fund_pay_type").toString());
										payinfo.setFund_payamt(map.get("fund_payamt").toString());
										payinfoList.add(payinfo);
									}
									input.setPayinfo(payinfoList);
									
									//门诊慢特病诊断信息（节点标识：opspdiseinfo）
									//住院的不用管
									
									//住院诊断信息（节点标识：diseinfo）
									for (Map<String, Object> map : diseinfoMapList) {
										Input4101Diseinfo diseinfo = new Input4101Diseinfo();
										diseinfo.setDiag_type(map.get("maindiag_flag").toString().equals("1")?"1":"2");
										diseinfo.setDiag_code(map.get("diag_code").toString());
										diseinfo.setDiag_name(map.get("diag_name").toString());
										diseinfo.setAdm_cond_type(map.get("adm_cond_type").toString());
										diseinfo.setMaindiag_flag(map.get("maindiag_flag").toString().equals("1")?"1":"0");
										diseinfoList.add(diseinfo);
									}
									input.setDiseinfo(diseinfoList);
									
									// 收费项目信息（节点标识：iteminfo）
									if(iteminfo2301MapList!=null && iteminfo2301MapList.size()>0){
										for (Map<String, Object> map : iteminfo2301MapList) {
											Input4101Iteminfo iteminfo = new Input4101Iteminfo();
											iteminfo.setMed_chrgitm(map.get("med_chrgitm_type") == null?"0":map.get("med_chrgitm_type").toString());
											iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
											iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
											iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
											iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
											iteminfo.setOth_amt("0.00");
											iteminfoList.add(iteminfo);
										}
									}else{
										for (Map<String, Object> map : iteminfo5204MapList) {
											Input4101Iteminfo iteminfo = new Input4101Iteminfo();
											iteminfo.setMed_chrgitm(map.get("med_chrgitm_type") == null?"0":map.get("med_chrgitm_type").toString());
											iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
											iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
											iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
											iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
											iteminfo.setOth_amt("0.00");
											iteminfoList.add(iteminfo);
										}
									}
									input.setIteminfo(iteminfoList);
									
									
									String error = null;
									//手术操作信息（节点标识：oprninfo）
									int ssNum = 0;
									if(oprninfoMapList.size()>0){
										for(int i=0; i<oprninfoMapList.size(); i++){
											Map<String, Object> map = oprninfoMapList.get(i);
											StringBuffer ssSql = new StringBuffer("select * from ins_zzfs_zd where zzfs_code = ? ");
											List<Map<String, Object>> ybssMapList = DataBaseHelper.queryForList(ssSql.toString(), map.get("oprn_oprt_code").toString());
											//if(ybssMapList!=null&&ybssMapList.size()>0){
												Input4101Oprninfo oprninfo = new Input4101Oprninfo();
												if(i==0){
													oprninfo.setOprn_oprt_type("1");  //1	主要手术及操作	2	其他手术及操作
												}else{
													oprninfo.setOprn_oprt_type("2");  //1	主要手术及操作	2	其他手术及操作
												}
												if(ybssMapList!=null&&ybssMapList.size()>0){
													oprninfo.setOprn_oprt_code(ybssMapList.get(0).get("zzfsCode2").toString());
												}else{
													oprninfo.setOprn_oprt_code(map.get("oprn_oprt_code").toString());
												}
												//oprninfo.setOprn_oprt_code(ybssMapList.get(0).get("zzfsCode2").toString());//手术码要匹配诊治编码库的编码，匹配不到的不上传，匹配到的上传医保手术编码2.0
												oprninfo.setOprn_oprt_name(map.get("oprn_oprt_name").toString());
												oprninfo.setOprn_oprt_date(map.get("oprn_oprt_date").toString());
												oprninfo.setAnst_way(map.get("anst_way")==null?null:map.get("anst_way").toString());
												
												String ybysSql = "select * from lsb_ybys where xm = ? and ybysdm is not null and ybysdm!=''";
												if(map.get("oper_dr_name") != null){
													List<Map<String, Object>> ybysMapList = DataBaseHelper.queryForList(ybysSql, map.get("oper_dr_name").toString());
													if(ybysMapList!=null&&ybysMapList.size()>0){
														oprninfo.setOper_dr_code(ybysMapList.get(0).get("ybysdm").toString());
													}else{
														if(map.get("oper_dr_code")==null){
															ybysSql = "select * from BD_OU_EMPLOYEE where name_emp = ?";
															List<Map<String, Object>> empMapList = DataBaseHelper.queryForList(ybysSql, map.get("oper_dr_name").toString());
															if(empMapList!=null&&empMapList.size()>0){
																oprninfo.setOper_dr_code(empMapList.get(0).get("codeEmp").toString());
															}else{
																error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"的医生("+map.get("oper_dr_name").toString()+")的编码为空!";
																break;
															}
														}else{
															oprninfo.setOper_dr_code(map.get("oper_dr_code").toString());
														}
														
													}
												}else{
													error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"的医生姓名为空!";
													break;
												}
												
												oprninfo.setOper_dr_name(map.get("oper_dr_name").toString());
												if(map.get("anst_dr_code")!=null){
													List<Map<String, Object>> ybmzsMapList = DataBaseHelper.queryForList(ybysSql, map.get("anst_dr_name").toString());
													if(ybmzsMapList!=null&&ybmzsMapList.size()>0){
														oprninfo.setAnst_dr_code(ybmzsMapList.get(0).get("ybysdm").toString());
													}else{
														oprninfo.setAnst_dr_code(map.get("oper_dr_code").toString());
													}
												}else{
													oprninfo.setAnst_dr_code(map.get("anst_dr_code")==null?null:map.get("anst_dr_code").toString());
												}
												oprninfo.setAnst_dr_name(map.get("anst_dr_name")==null?null:map.get("anst_dr_name").toString());
												oprninfoList.add(oprninfo);
												ssNum++;
											/*}else{
												error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"("+map.get("oprn_oprt_code").toString()+")查不到医保2.0手术编码!";
												break;
											}*/
										}
										input.setOprninfo(oprninfoList);
									}
									setlinfo.setOprn_oprt_code_cnt(ssNum+"");
									input.setSetlinfo(setlinfo);
									
									//重症监护信息（节点标识：icuinfo）
									//先不传了，后面再补上
									
									if(error==null){
										OutputData4101 out4101 = YbFunUtils.fun4101(input,  signIn.getSignNo());
										if(out4101.getInfcode()==null || !out4101.getInfcode().equals("0")){
											Map<String, Object> ry = new HashMap<String, Object>();
											returnMap.put("code", "-1");
											returnMap.put("msg",  out4101.getErr_msg()+(out4101.getMessage()==null?"":out4101.getMessage()));
										}else{
											//DataBaseHelper.execute("update ins_st_qg set SETL_LIST_ID = ?, DCLA_TIME = ?, MEDINS_FILL_DEPT = ?, MEDINS_FILL_PSN = ? where pk_insstqg=? ", 
												//	out4101.getOutput().getSetl_list_id(), setlinfo.getDcla_time(), setlinfo.getMedins_fill_dept(), setlinfo.getMedins_fill_psn(), pkInsstqg);
											returnMap.put("setlListId", out4101.getOutput().getSetl_list_id());
											returnMap.put("dclaTime", setlinfo.getDcla_time());
											returnMap.put("medinsFillDept", setlinfo.getMedins_fill_dept());
											returnMap.put("medinsFillPsn", setlinfo.getMedins_fill_psn());
											returnMap.put("pkInsstqg", pkInsstqg);
											returnMap.put("code", "0");
											returnMap.put("msg",  "");
										}
									}else{
										returnMap.put("code", "-1");
										returnMap.put("msg",  error);
									}
								}else{
									returnMap.put("code", signIn.getCode());
									returnMap.put("msg",  signIn.getMsg());
								}
							}else{
								returnMap.put("code", "-1");
								returnMap.put("msg", "没有查询到收费项目信息！");
							}
						/*}else{
							returnMap.put("code", "-1");
							returnMap.put("msg", "没有查询到住院诊断信息！");
						}*/
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", "该患者为旧系统导入新系统结算的患者，查不到旧系统的医保结算单数据！");
					}
				}
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", "没有查询到结算清单信息！");
			}
		}
		return returnMap;
	}

	/**
	 * 获取结算清单打印数据
	 * @param param
	 * @param user
	 */
	public Map<String, Object> getSettlementListPrintData(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String pkInsstqg = jo.getString("pkInsstqg");
		//String ip = jo.getString("ip");
		//String mac = jo.getString("mac");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkInsstqg", pkInsstqg);
		
		InsZsbaStQg st = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_insstqg = ?", InsZsbaStQg.class, pkInsstqg);
		paramMap.put("pkPv", st.getPkPv());
		
		Map<String, Object> setlinfoMap  = insQgybPvMapper.get4101SetlinfoPrint(paramMap);
		List<Map<String, Object>> payinfoMapList  = insQgybPvMapper.get4101Payinfo(paramMap);
		List<Map<String, Object>> iteminfo2301MapList  = insQgybPvMapper.get4101Iteminfo2301(paramMap);
		List<Map<String, Object>> iteminfo5204MapList  = insQgybPvMapper.get4101Iteminfo5204(paramMap);
		
		//切换数据源
		DataSourceRoute.putAppId("BAGL_bayy");
		List<Map<String, Object>> diseinfoMapList  = insQgybPvMapper.get4101DiseinfoBaxt(paramMap);
		List<Map<String, Object>> oprninfoMapList  = insQgybPvMapper.get4101OprninfoBaxt(paramMap);
		//切回默认数据源
		DataSourceRoute.putAppId("default");
		
		Map<String, Object> otherMap = new HashMap<String, Object>();
		
		//组装诊断数据
		List<Map<String, Object>> diseinfoZyMapList  = insQgybPvMapper.get4101DiseinfoZy(paramMap);
		List<Map<String, Object>> diseinfoXyMapList  = insQgybPvMapper.get4101DiseinfoXy(paramMap);
		
		List<Map<String, Object>> diseinfoPrintList = new ArrayList<Map<String,Object>>();
		if(diseinfoZyMapList.size()>=diseinfoXyMapList.size()){
			for (int i = 0; i < diseinfoZyMapList.size(); i++) {
				Map<String, Object> zyMap = diseinfoZyMapList.get(i);
				Map<String, Object> printMap = new HashMap<String, Object>();
				printMap.put("zd4", zyMap.get("diag_name").toString());
				printMap.put("zd5", zyMap.get("diag_code").toString());
				if(zyMap.get("adm_cond_type")!=null){
					if(zyMap.get("adm_cond_type").toString().equals("1")){
						printMap.put("zd6", "有");
					}else if(zyMap.get("adm_cond_type").toString().equals("2")){
						printMap.put("zd6", "临床未确定");
					}else if(zyMap.get("adm_cond_type").toString().equals("3")){
						printMap.put("zd6", "情况不明");
					}else if(zyMap.get("adm_cond_type").toString().equals("4")){
						printMap.put("zd6", "无");
					}
				}
				
				if(diseinfoXyMapList.size()>=(i+1)){
					Map<String, Object> xyMap = diseinfoXyMapList.get(i);
					printMap.put("zd1", xyMap.get("diag_name").toString());
					printMap.put("zd2", xyMap.get("diag_code").toString());
					if(xyMap.get("adm_cond_type")!=null){
						if(xyMap.get("adm_cond_type").toString().equals("1")){
							printMap.put("zd3", "有");
						}else if(xyMap.get("adm_cond_type").toString().equals("2")){
							printMap.put("zd3", "临床未确定");
						}else if(xyMap.get("adm_cond_type").toString().equals("3")){
							printMap.put("zd3", "情况不明");
						}else if(xyMap.get("adm_cond_type").toString().equals("4")){
							printMap.put("zd3", "无");
						}
					}
				}else{
					printMap.put("zd1", "");
					printMap.put("zd2", "");
					printMap.put("zd3", "");
				}
				diseinfoPrintList.add(printMap);
			}
		}else{
			for (int i = 0; i < diseinfoXyMapList.size(); i++) {
				Map<String, Object> xyMap = diseinfoXyMapList.get(i);
				Map<String, Object> printMap = new HashMap<String, Object>();
				printMap.put("zd1", xyMap.get("diag_name").toString());
				printMap.put("zd2", xyMap.get("diag_code").toString());
				if(xyMap.get("adm_cond_type")!=null){
					if(xyMap.get("adm_cond_type").toString().equals("1")){
						printMap.put("zd3", "有");
					}else if(xyMap.get("adm_cond_type").toString().equals("2")){
						printMap.put("zd3", "临床未确定");
					}else if(xyMap.get("adm_cond_type").toString().equals("3")){
						printMap.put("zd3", "情况不明");
					}else if(xyMap.get("adm_cond_type").toString().equals("4")){
						printMap.put("zd3", "无");
					}
				}
				if(diseinfoZyMapList.size()>=(i+1)){
					Map<String, Object> zyMap = diseinfoZyMapList.get(i);
					printMap.put("zd4", zyMap.get("diag_name").toString());
					printMap.put("zd5", zyMap.get("diag_code").toString());
					if(zyMap.get("adm_cond_type")!=null){
						if(zyMap.get("adm_cond_type").toString().equals("1")){
							printMap.put("zd6", "有");
						}else if(zyMap.get("adm_cond_type").toString().equals("2")){
							printMap.put("zd6", "临床未确定");
						}else if(zyMap.get("adm_cond_type").toString().equals("3")){
							printMap.put("zd6", "情况不明");
						}else if(zyMap.get("adm_cond_type").toString().equals("4")){
							printMap.put("zd6", "无");
						}
					}
				}else{
					printMap.put("zd4", "");
					printMap.put("zd5", "");
					printMap.put("zd6", "");
				}
				diseinfoPrintList.add(printMap);
			}
		}
		returnMap.put("diseinfo", diseinfoPrintList);
		
		Input4101 input = new Input4101();
		Input4101Setlinfo setlinfo = new Input4101Setlinfo();
		List<Input4101Payinfo> payinfoList = new ArrayList<Input4101Payinfo>();
		List<Input4101Iteminfo> iteminfoList = new ArrayList<Input4101Iteminfo>();
		List<Input4101Oprninfo> oprninfoList = new ArrayList<Input4101Oprninfo>();
		
		if(setlinfoMap!=null){
				if(diseinfoMapList!=null && diseinfoMapList.size()!=0)
				{
					if((iteminfo2301MapList!=null && iteminfo2301MapList.size()!=0)||(iteminfo5204MapList!=null && iteminfo5204MapList.size()!=0))
					{
						//InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
						//if(signIn.getCode().equals("0")){
						otherMap.put("curr_addr_prov", setlinfoMap.get("curr_addr_prov")==null?null:setlinfoMap.get("curr_addr_prov").toString());
						otherMap.put("curr_addr_city", setlinfoMap.get("curr_addr_city")==null?null:setlinfoMap.get("curr_addr_city").toString());
						otherMap.put("curr_addr_county", setlinfoMap.get("curr_addr_county")==null?null:setlinfoMap.get("curr_addr_county").toString());
						otherMap.put("hifp_pay", setlinfoMap.get("hifp_pay")==null?null:setlinfoMap.get("hifp_pay").toString());
						otherMap.put("oth_pay", setlinfoMap.get("oth_pay")==null?null:setlinfoMap.get("oth_pay").toString());
						otherMap.put("hifmi_pay", setlinfoMap.get("hifmi_pay")==null?null:setlinfoMap.get("hifmi_pay").toString());
						otherMap.put("maf_pay", setlinfoMap.get("maf_pay")==null?null:setlinfoMap.get("maf_pay").toString());
						otherMap.put("cvlserv_pay", setlinfoMap.get("cvlserv_pay")==null?null:setlinfoMap.get("cvlserv_pay").toString());
						otherMap.put("hifob_pay", setlinfoMap.get("hifob_pay")==null?null:setlinfoMap.get("hifob_pay").toString());
						otherMap.put("hifes_pay", setlinfoMap.get("hifes_pay")==null?null:setlinfoMap.get("hifes_pay").toString());
						
							setlinfo.setMdtrt_id(setlinfoMap.get("mdtrt_id")==null?null:setlinfoMap.get("mdtrt_id").toString());
							setlinfo.setSetl_id(setlinfoMap.get("setl_id")==null?null:setlinfoMap.get("setl_id").toString());
							setlinfo.setFixmedins_name(setlinfoMap.get("fixmedins_name")==null?null:setlinfoMap.get("fixmedins_name").toString());
							setlinfo.setFixmedins_code(setlinfoMap.get("fixmedins_code")==null?null:setlinfoMap.get("fixmedins_code").toString());
							setlinfo.setHi_setl_lv(setlinfoMap.get("hi_setl_lv")==null?null:setlinfoMap.get("hi_setl_lv").toString());
							setlinfo.setHi_no(setlinfoMap.get("hi_no")==null?null:setlinfoMap.get("hi_no").toString());
							setlinfo.setMedcasno(setlinfoMap.get("medcasno")==null?null:setlinfoMap.get("medcasno").toString());
							setlinfo.setDcla_time(setlinfoMap.get("dcla_time")==null?null:setlinfoMap.get("dcla_time").toString());
							setlinfo.setPsn_name(setlinfoMap.get("psn_name")==null?null:setlinfoMap.get("psn_name").toString());
							setlinfo.setGend(setlinfoMap.get("gend")==null?null:setlinfoMap.get("gend").toString());
							setlinfo.setBrdy(setlinfoMap.get("brdy")==null?null:setlinfoMap.get("brdy").toString());
							setlinfo.setAge(setlinfoMap.get("age")==null?null:setlinfoMap.get("age").toString());
							setlinfo.setNtly(setlinfoMap.get("ntly")==null?null:setlinfoMap.get("ntly").toString());
							setlinfo.setNwb_age(setlinfoMap.get("nwb_age")==null?null:Integer.parseInt(setlinfoMap.get("nwb_age").toString())>365?null:setlinfoMap.get("nwb_age").toString());
							setlinfo.setNaty(setlinfoMap.get("naty")==null?null:setlinfoMap.get("naty").toString());
							setlinfo.setPatn_cert_type(setlinfoMap.get("patn_cert_type")==null?null:setlinfoMap.get("patn_cert_type").toString());
							setlinfo.setCertno(setlinfoMap.get("certno")==null?null:setlinfoMap.get("certno").toString());
							setlinfo.setPrfs(setlinfoMap.get("prfs")==null?null:setlinfoMap.get("prfs").toString());
							setlinfo.setCurr_addr(setlinfoMap.get("curr_addr")==null?null:setlinfoMap.get("curr_addr").toString());
							setlinfo.setEmp_name(setlinfoMap.get("emp_name")==null?null:setlinfoMap.get("emp_name").toString());
							setlinfo.setEmp_addr(setlinfoMap.get("emp_addr")==null?null:setlinfoMap.get("emp_addr").toString());
							setlinfo.setEmp_tel(setlinfoMap.get("emp_tel")==null?null:setlinfoMap.get("emp_tel").toString());
							setlinfo.setPoscode(setlinfoMap.get("poscode")==null?null:setlinfoMap.get("poscode").toString());
							setlinfo.setConer_name(setlinfoMap.get("coner_name")==null?null:setlinfoMap.get("coner_name").toString());
							setlinfo.setPatn_rlts(setlinfoMap.get("patn_rlts")==null?null:setlinfoMap.get("patn_rlts").toString());
							setlinfo.setConer_addr(setlinfoMap.get("coner_addr")==null?null:setlinfoMap.get("coner_addr").toString());
							setlinfo.setConer_tel(setlinfoMap.get("coner_tel")==null?null:setlinfoMap.get("coner_tel").toString());
							setlinfo.setHi_type(setlinfoMap.get("hi_type")==null?null:setlinfoMap.get("hi_type").toString());
							setlinfo.setInsuplc(setlinfoMap.get("insuplc")==null?null:setlinfoMap.get("insuplc").toString());
							setlinfo.setSp_psn_type(setlinfoMap.get("sp_psn_type")==null?null:setlinfoMap.get("sp_psn_type").toString());
							setlinfo.setNwb_adm_type(setlinfoMap.get("nwb_adm_type")==null?null:setlinfoMap.get("nwb_adm_type").toString());
							setlinfo.setNwb_bir_wt(setlinfoMap.get("nwb_bir_wt")==null?null:setlinfoMap.get("nwb_bir_wt").toString());
							setlinfo.setNwb_adm_wt(setlinfoMap.get("nwb_adm_wt")==null?null:setlinfoMap.get("nwb_adm_wt").toString());
							setlinfo.setOpsp_diag_caty(setlinfoMap.get("opsp_diag_caty")==null?null:setlinfoMap.get("opsp_diag_caty").toString());
							setlinfo.setOpsp_mdtrt_date(setlinfoMap.get("opsp_mdtrt_date")==null?null:setlinfoMap.get("opsp_mdtrt_date").toString());
							setlinfo.setIpt_med_type(setlinfoMap.get("ipt_med_type")==null?null:setlinfoMap.get("ipt_med_type").toString());
							setlinfo.setAdm_way(setlinfoMap.get("adm_way")==null?null:setlinfoMap.get("adm_way").toString());
							setlinfo.setTrt_type(setlinfoMap.get("trt_type")==null?null:setlinfoMap.get("trt_type").toString());
							setlinfo.setAdm_time(setlinfoMap.get("adm_time")==null?null:setlinfoMap.get("adm_time").toString());
							setlinfo.setAdm_caty(setlinfoMap.get("adm_caty")==null?null:setlinfoMap.get("adm_caty").toString());
							//转科科别 数据库存的是名字，得单独查
							//setlinfo.setRefldept_dept(setlinfoMap.get("refldept_dept")==null?null:setlinfoMap.get("refldept_dept").toString());
							setlinfo.setDscg_time(setlinfoMap.get("dscg_time")==null?null:setlinfoMap.get("dscg_time").toString());
							setlinfo.setDscg_caty(setlinfoMap.get("dscg_caty")==null?null:setlinfoMap.get("dscg_caty").toString());
							setlinfo.setAct_ipt_days(setlinfoMap.get("act_ipt_days")==null?null:setlinfoMap.get("act_ipt_days").toString());
							setlinfo.setOtp_wm_dise(setlinfoMap.get("otp_wm_dise")==null?null:setlinfoMap.get("otp_wm_dise").toString());
							setlinfo.setWm_dise_code(setlinfoMap.get("wm_dise_code")==null?null:setlinfoMap.get("wm_dise_code").toString());
							setlinfo.setOtp_tcm_dise(setlinfoMap.get("otp_tcm_dise")==null?null:setlinfoMap.get("otp_tcm_dise").toString());
							setlinfo.setTcm_dise_code(setlinfoMap.get("tcm_dise_code")==null?null:setlinfoMap.get("tcm_dise_code").toString());
							//诊断代码计数  需要单独查
							setlinfo.setDiag_code_cnt(diseinfoMapList.size()+"");
							//手术操作代码计数
							//setlinfo.setOprn_oprt_code_cnt(oprninfoMapList.size()+"");
							setlinfo.setVent_used_dura(setlinfoMap.get("vent_used_dura")==null?null:setlinfoMap.get("vent_used_dura").toString());
							String pwcry_bfadm_coma_dura = "";
							String pwcry_afadm_coma_dura = "";
							if(setlinfoMap.get("coma_day_bef")!=null){
								pwcry_bfadm_coma_dura += setlinfoMap.get("coma_day_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "0";
							}
							if(setlinfoMap.get("coma_hour_bef")!=null){
								pwcry_bfadm_coma_dura += "/"+setlinfoMap.get("coma_hour_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "/0";
							}
							if(setlinfoMap.get("coma_min_bef")!=null){
								pwcry_bfadm_coma_dura += "/"+setlinfoMap.get("coma_min_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "/0";
							}
							if(pwcry_bfadm_coma_dura.equals("0/0/0")){
								pwcry_bfadm_coma_dura = null;
							}
							if(setlinfoMap.get("coma_day_after")!=null){
								pwcry_afadm_coma_dura += setlinfoMap.get("coma_day_after").toString();
							}else{
								pwcry_afadm_coma_dura += "0";
							}
							if(setlinfoMap.get("coma_hour_after")!=null){
								pwcry_afadm_coma_dura += "/"+setlinfoMap.get("coma_hour_after").toString();
							}else{
								pwcry_afadm_coma_dura += "/0";
							}
							if(setlinfoMap.get("coma_min_after")!=null){
								pwcry_afadm_coma_dura += "/"+setlinfoMap.get("coma_min_after").toString();
							}else{
								pwcry_afadm_coma_dura += "/0";
							}
							if(pwcry_afadm_coma_dura.equals("0/0/0")){
								pwcry_afadm_coma_dura = null;
							}
							setlinfo.setPwcry_bfadm_coma_dura(pwcry_bfadm_coma_dura);
							setlinfo.setPwcry_afadm_coma_dura(pwcry_afadm_coma_dura);
							//setlinfo.setBld_cat(setlinfoMap.get("bld_cat")==null?null:setlinfoMap.get("bld_cat").toString());
							//setlinfo.setBld_amt(setlinfoMap.get("bld_amt")==null?null:setlinfoMap.get("bld_amt").toString());
							//setlinfo.setBld_unt(setlinfoMap.get("bld_unt")==null?null:setlinfoMap.get("bld_unt").toString());
							//setlinfo.setSpga_nurscare_days(setlinfoMap.get("spga_nurscare_days")==null?null:setlinfoMap.get("spga_nurscare_days").toString());
							//setlinfo.setLv1_nurscare_days(setlinfoMap.get("lv1_nurscare_days")==null?null:setlinfoMap.get("lv1_nurscare_days").toString());
							//setlinfo.setScd_nurscare_days(setlinfoMap.get("scd_nurscare_days")==null?null:setlinfoMap.get("scd_nurscare_days").toString());
							//setlinfo.setLv3_nurscare_days(setlinfoMap.get("lv3_nurscare_days")==null?null:setlinfoMap.get("lv3_nurscare_days").toString());
							setlinfo.setDscg_way(setlinfoMap.get("dscg_way")==null?null:setlinfoMap.get("dscg_way").toString());
							setlinfo.setAcp_medins_name(setlinfoMap.get("acp_medins_name")==null?null:setlinfoMap.get("acp_medins_name").toString());
							setlinfo.setAcp_optins_code(setlinfoMap.get("acp_optins_code")==null?null:setlinfoMap.get("acp_optins_code").toString());
							setlinfo.setBill_code(setlinfoMap.get("bill_code")==null?null:setlinfoMap.get("bill_code").toString());
							setlinfo.setBill_no(setlinfoMap.get("bill_no")==null?null:setlinfoMap.get("bill_no").toString());
							setlinfo.setBiz_sn(setlinfoMap.get("biz_sn")==null?null:setlinfoMap.get("biz_sn").toString());
							//setlinfo.setDays_rinp_flag_31(setlinfoMap.get("days_rinp_flag_31")==null?null:setlinfoMap.get("days_rinp_flag_31").toString());
							//setlinfo.setDays_rinp_pup_31(setlinfoMap.get("days_rinp_pup_31")==null?null:setlinfoMap.get("days_rinp_pup_31").toString());
							setlinfo.setChfpdr_name(setlinfoMap.get("chfpdr_name")==null?null:setlinfoMap.get("chfpdr_name").toString());
							setlinfo.setChfpdr_code(setlinfoMap.get("chfpdr_code")==null?null:setlinfoMap.get("chfpdr_code").toString());
							setlinfo.setSetl_begn_date(setlinfoMap.get("setl_begn_date")==null?null:setlinfoMap.get("setl_begn_date").toString());
							setlinfo.setSetl_end_date(setlinfoMap.get("setl_end_date")==null?null:setlinfoMap.get("setl_end_date").toString());
							setlinfo.setPsn_selfpay(setlinfoMap.get("psn_selfpay")==null?null:setlinfoMap.get("psn_selfpay").toString());
							setlinfo.setPsn_ownpay(setlinfoMap.get("psn_ownpay")==null?null:setlinfoMap.get("psn_ownpay").toString());
							setlinfo.setAcct_pay(setlinfoMap.get("acct_pay")==null?null:setlinfoMap.get("acct_pay").toString());
							setlinfo.setPsn_cashpay(setlinfoMap.get("psn_cashpay")==null?null:setlinfoMap.get("psn_cashpay").toString());
							setlinfo.setHi_paymtd(setlinfoMap.get("hi_paymtd")==null?null:setlinfoMap.get("hi_paymtd").toString());
							setlinfo.setHsorg(setlinfoMap.get("hsorg")==null?null:setlinfoMap.get("hsorg").toString());
							setlinfo.setHsorg_opter(setlinfoMap.get("hsorg_opter")==null?null:setlinfoMap.get("hsorg_opter").toString());
							User currUser = UserContext.getUser();
							BdOuDept dept = DataBaseHelper.queryForBean(" select * from BD_OU_DEPT where pk_dept = ?", BdOuDept.class, currUser.getPkDept());
							setlinfo.setMedins_fill_dept(dept.getNameDept());
							setlinfo.setMedins_fill_psn(user.getUserName());
							input.setSetlinfo(setlinfo);
							
							//基金支付信息（节点标识：payinfo）
							for (Map<String, Object> map : payinfoMapList) {
								Input4101Payinfo payinfo = new Input4101Payinfo();
								payinfo.setFund_pay_type(map.get("fund_pay_type").toString());
								payinfo.setFund_payamt(map.get("fund_payamt").toString());
								payinfoList.add(payinfo);
							}
							input.setPayinfo(payinfoList);
							
							//门诊慢特病诊断信息（节点标识：opspdiseinfo）
							//住院的不用管
							
							//住院诊断信息（节点标识：diseinfo）

							
							// 收费项目信息（节点标识：iteminfo）
							if(iteminfo2301MapList!=null && iteminfo2301MapList.size()>0){
								for (Map<String, Object> map : iteminfo2301MapList) {
									Input4101Iteminfo iteminfo = new Input4101Iteminfo();
									iteminfo.setMed_chrgitm(map.get("name") == null?"0":map.get("name").toString());
									iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
									iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
									iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
									iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
									iteminfo.setOth_amt("0.00");
									iteminfoList.add(iteminfo);
								}
							}else{
								for (Map<String, Object> map : iteminfo5204MapList) {
									Input4101Iteminfo iteminfo = new Input4101Iteminfo();
									iteminfo.setMed_chrgitm(map.get("name") == null?"0":map.get("name").toString());
									iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
									iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
									iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
									iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
									iteminfo.setOth_amt("0.00");
									iteminfoList.add(iteminfo);
								}
							}
							input.setIteminfo(iteminfoList);
							
							String error = null;
							
							//手术操作信息（节点标识：oprninfo）
							int ssNum = 0;
							if(oprninfoMapList.size()>0){
								for(int i=0; i<oprninfoMapList.size(); i++){
									Map<String, Object> map = oprninfoMapList.get(i);
									StringBuffer ssSql = new StringBuffer("select * from ins_zzfs_zd where zzfs_code = ? ");
									List<Map<String, Object>> ybssMapList = DataBaseHelper.queryForList(ssSql.toString(), map.get("oprn_oprt_code").toString());
									//if(ybssMapList!=null&&ybssMapList.size()>0){
										Input4101Oprninfo oprninfo = new Input4101Oprninfo();
										if(i==0){
											oprninfo.setOprn_oprt_type("1");  //1	主要手术及操作	2	其他手术及操作
										}else{
											oprninfo.setOprn_oprt_type("2");  //1	主要手术及操作	2	其他手术及操作
										}
										if(ybssMapList!=null&&ybssMapList.size()>0){
											oprninfo.setOprn_oprt_code(ybssMapList.get(0).get("zzfsCode2").toString());
										}else{
											oprninfo.setOprn_oprt_code(map.get("oprn_oprt_code").toString());
										}
										//oprninfo.setOprn_oprt_code(ybssMapList.get(0).get("zzfsCode2").toString());//手术码要匹配诊治编码库的编码，匹配不到的不上传，匹配到的上传医保手术编码2.0
										oprninfo.setOprn_oprt_name(map.get("oprn_oprt_name").toString());
										oprninfo.setOprn_oprt_date(map.get("oprn_oprt_date").toString());
										oprninfo.setAnst_way(map.get("anst_way")==null?null:map.get("anst_way").toString());
										
										String ybysSql = "select * from lsb_ybys where xm = ? and ybysdm is not null and ybysdm!=''";
										if(map.get("oper_dr_name") != null){
											List<Map<String, Object>> ybysMapList = DataBaseHelper.queryForList(ybysSql, map.get("oper_dr_name").toString());
											if(ybysMapList!=null&&ybysMapList.size()>0){
												oprninfo.setOper_dr_code(ybysMapList.get(0).get("ybysdm").toString());
											}else{
												if(map.get("oper_dr_code")==null){
													ybysSql = "select * from BD_OU_EMPLOYEE where name_emp = ?";
													List<Map<String, Object>> empMapList = DataBaseHelper.queryForList(ybysSql, map.get("oper_dr_name").toString());
													if(empMapList!=null&&empMapList.size()>0){
														oprninfo.setOper_dr_code(empMapList.get(0).get("codeEmp").toString());
													}else{
														error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"的医生("+map.get("oper_dr_name").toString()+")的编码为空!";
														break;
													}
												}else{
													oprninfo.setOper_dr_code(map.get("oper_dr_code").toString());
												}
												
											}
										}else{
											error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"的医生姓名为空!";
											break;
										}
										
										oprninfo.setOper_dr_name(map.get("oper_dr_name").toString());
										if(map.get("anst_dr_code")!=null){
											List<Map<String, Object>> ybmzsMapList = DataBaseHelper.queryForList(ybysSql, map.get("anst_dr_name").toString());
											if(ybmzsMapList!=null&&ybmzsMapList.size()>0){
												oprninfo.setAnst_dr_code(ybmzsMapList.get(0).get("ybysdm").toString());
											}else{
												oprninfo.setAnst_dr_code(map.get("oper_dr_code").toString());
											}
										}else{
											oprninfo.setAnst_dr_code(map.get("anst_dr_code")==null?null:map.get("anst_dr_code").toString());
										}
										oprninfo.setAnst_dr_name(map.get("anst_dr_name")==null?null:map.get("anst_dr_name").toString());
										oprninfoList.add(oprninfo);
										ssNum++;
									/*}else{
										error = setlinfo.getPsn_name()+"的手术："+map.get("oprn_oprt_name").toString()+"("+map.get("oprn_oprt_code").toString()+")查不到医保2.0手术编码!";
										break;
									}*/
								}
								input.setOprninfo(oprninfoList);
							}
							setlinfo.setOprn_oprt_code_cnt(ssNum+"");
							input.setSetlinfo(setlinfo);
							
							//重症监护信息（节点标识：icuinfo）
							//先不传了，后面再补上
							
							if(error==null){
								returnMap.put("code", "0");
								returnMap.put("msg",  "");
								returnMap.put("input", input);
								returnMap.put("other", otherMap);
							}else{
								returnMap.put("code", "-1");
								returnMap.put("msg",  error);
							}
							
						/*}else{
							returnMap.put("code", signIn.getCode());
							returnMap.put("msg",  signIn.getMsg());
						}*/
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", "没有查询到收费项目信息！");
					}
				}else{
					returnMap.put("code", "-1");
					returnMap.put("msg", "没有查询到住院诊断信息！");
				}
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg", "没有查询到结算清单信息！");
		}
		return returnMap;
	}

}
