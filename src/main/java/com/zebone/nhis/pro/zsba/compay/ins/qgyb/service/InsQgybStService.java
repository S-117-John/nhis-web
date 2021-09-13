package com.zebone.nhis.pro.zsba.compay.ins.qgyb.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.service.InsPubSignInService;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao.InsQgybFeedetailMapper;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao.InsQgybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbFunUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Setlinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input5204Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output5204Fymx;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaDiseinfoQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaFeedetailQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaPvQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaSignInQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input4101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input5204;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData5204;
import com.zebone.nhis.pro.zsba.compay.pub.vo.PayPosTr;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 医保结算
 * @author Administrator
 *
 */
@Service
public class InsQgybStService {
	
	@Autowired
	private InsPubSignInService insPubSignInService;
	
	@Autowired
	private InsQgybFeedetailMapper insQgybFeedetailMapper;
	
	@Autowired
	private InsQgybPvMapper insQgybPvMapper;
	
	/**
	 * 获取取消医保结算的数据
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getCancelData(String param,IUser user){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		//code
		//-1：不能取消结算，需弹窗提示
		//0：不需要扣个账、不需要取消医保结算
		//1：不需要扣个账、需要取消医保结算
		//2：护士站撤销个账、取消医保结算
		//3:  收费处取消结算、提示撤销个账
		//4：收费处取消结算、提示填单
		//5: 收费处取消结算、电子凭证撤销个账
		returnMap.put("code", "0");
		returnMap.put("msg", "");
		JSONObject jo = JSONObject.fromObject(param);
		String pkSettle = jo.getString("pkSettle");
		String dtDepttype = jo.getString("dtDepttype");
		
		//查询his结算记录和结算科室
		String sql = "select t2.code_dept, t1.date_st from bl_settle t1 inner join bd_ou_dept t2 on t1.pk_dept_st = t2.pk_dept  where t1.pk_settle = ? and t1.del_flag = '0'";
		Map<String,Object> blSettleMap = DataBaseHelper.queryForMap(sql, pkSettle);
		
		//查询医保结算记录
		String stSql = "select * from ins_st_qg where pk_settle = ? and del_flag = '0'";
		InsZsbaStQg insSt = DataBaseHelper.queryForBean(stSql, InsZsbaStQg.class, pkSettle);
		
		//查询三代卡或电子凭证记录
		String trSql = "select * from pay_pos_tr where pk_settle = ? and del_flag = '0'";
		PayPosTr tr = DataBaseHelper.queryForBean(trSql, PayPosTr.class, pkSettle);
		
		//查询结算科室类型，用于区分是在护士站还是在收费处结算的
		String deptSql = "select dt_depttype from bd_ou_dept where code_dept = ?";
		Map<String,Object> deptMap = DataBaseHelper.queryForMap(deptSql, blSettleMap.get("codeDept"));
		
		//13:取消结算成功
		if (insSt != null && !insSt.getStatus().equals("13")) {
			returnMap.put("pkPi", insSt.getPkPi());  
			returnMap.put("pkPv", insSt.getPkPv());  
			returnMap.put("mdtrtId", insSt.getMdtrtId());  
			returnMap.put("setlId", insSt.getSetlId());  
			returnMap.put("psnNo", insSt.getPsnNo());  
			returnMap.put("posSn", insSt.getPosSn());  
			
			 // 护士站医保费用只能撤销当天的,不是当天的提示去收费处，收费处当天可以撤销，不是当天需要填单
			if(!DateUtils.dateToStr("yyyyMMdd", insSt.getSetlTime()).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
				//不是当天的
				if(insSt.getPosSn()!=null){
					if(dtDepttype.equals("08")){// 收费处取消结算
						returnMap.put("ndk", insSt.getNdk());
						returnMap.put("kh", insSt.getKh());
						returnMap.put("grsxh", insSt.getCbh());
						returnMap.put("gz", insSt.getAcctPay().toString());
						returnMap.put("yybh", "H003");
						returnMap.put("code", "4");
						returnMap.put("msg", "收费处取消医保结算，提示填单");
					}else{//护士站取消结算
						returnMap.put("code", "-1");
						returnMap.put("msg", "护士站只能撤销当天的个账，不是当天的请到收费窗口退费");
					}
				}else{
					returnMap.put("code", "1");
					returnMap.put("msg", "没有个账，可以直接医保取消结算");
				}
			}else{
				//当天的
				if(insSt.getPosSn()!=null){//判断是否有个账
					if(dtDepttype.equals("08")){//判断在哪取消结算的
						if(!deptMap.get("dtDepttype").equals("08")){//判断在哪进行结算的
							returnMap.put("ndk", insSt.getNdk());
							returnMap.put("kh", insSt.getKh());
							returnMap.put("grsxh", insSt.getCbh());
							returnMap.put("gz", insSt.getAcctPay().toString());
							returnMap.put("yybh", "H003");
							
							returnMap.put("code", "4");
							returnMap.put("msg", "收费处取消医保结算，提示填单");
						}else{
							if(insSt.getNdk().equals("3") && tr.getSjly()!=null && tr.getSjly().equals("02")){
								returnMap.put("posSn", tr.getPzh());
								returnMap.put("kh", tr.getSbkh());
								returnMap.put("gz", tr.getSamt4());
								returnMap.put("sfz", tr.getSidentity());
								returnMap.put("xm", tr.getSname());
								returnMap.put("xtckh", tr.getXtckh());
								returnMap.put("code", "5");
								returnMap.put("msg", "收费处取消结算、电子凭证撤销个账");
							}else{
								returnMap.put("ndk", insSt.getNdk());
								returnMap.put("code", "3");
								returnMap.put("msg", "收费处取消医保结算，提示撤销个账");
							}
						}
					}else{
						if(!deptMap.get("dtDepttype").equals("08")){//判断在哪结算的
							//护士站结算、护士站取消
							returnMap.put("code", "2");
							returnMap.put("msg", "护士站撤销个账、取消医保结算");
						}else{
							returnMap.put("code", "-1");
							returnMap.put("msg", "护士站只能撤销护士站扣的个账，收费处的请到收费窗口退费");
						}
					}
				}else{
					returnMap.put("code", "1");
					returnMap.put("msg", "没有个账，可以直接医保取消结算");
				}
			}
		}else{
			returnMap.put("code", "0");
			returnMap.put("msg", "不需要进行医保的取消结算");
		}
		if(!returnMap.get("code").toString().equals("-1")){
			//判断是否是银行卡缴费
			BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from bl_deposit where pk_settle = ?",BlDeposit.class, pkSettle);
			if(blDeposit==null || !blDeposit.getDtPaymode().equals("3")){
				if(blDeposit!=null && blDeposit.getDtPaymode().equals("1")){//现金结算
					if(!dtDepttype.equals("08")){
						returnMap.put("code", "-1");
						returnMap.put("msg", "护士站不允许现金退费，请到收费窗口退费");
					}
				}
			}else{
				//银行卡缴费
				if(dtDepttype.equals("08")){
					
				}else{
					//银联pos机如果开通退货功能，可支持不是当天的退费，如果没开通，只有当天的才能撤销
					//目前以没开通的处理
					if(blSettleMap.get("dateSt").toString().substring(0, 8).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
						
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", "银行卡缴费的需要去人工窗口撤销费用!");
					}
				}
			}
		}
		return returnMap;
	}

	/**
	 * 获取结算单数据
	 * @param param
	 * @param user
	 */
	public Map<String,Object>  getSettleSingle(String param,IUser user){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("code", "0");
		returnMap.put("msg", "");
		
		DecimalFormat df = new DecimalFormat("#0.00#");
		
		JSONObject jo = JSONObject.fromObject(param);
		String pkSettle = jo.getString("pkSettle");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");

		String sql = "select * from ins_st_qg where pk_settle = ? and del_flag = '0'";
		InsZsbaStQg st = DataBaseHelper.queryForBean(sql, InsZsbaStQg.class, pkSettle);
		sql = "select * from ins_pv_qg where pk_inspvqg = ?";
		InsZsbaPvQg pv = DataBaseHelper.queryForBean(sql, InsZsbaPvQg.class, st.getPkInspvqg());
		sql = "select a.* from PI_MASTER a inner join BL_SETTLE b on a.pk_pi = b.pk_pi where pk_settle = ?";
		PiMaster pi = DataBaseHelper.queryForBean(sql, PiMaster.class, pkSettle);
		sql = "select a.* from ins_dict a inner join ins_dicttype b on a.pk_insdicttype = b.pk_insdicttype and b.code_type = ? and b.del_flag = '0'";
		sql += " inner join bd_hp c on b.pk_hp = c.pk_hp  and c.code='00051' and c.del_flag = '0' ";
		sql += " where a.code=? and a.del_flag = '0'";
		Map<String, Object> dictMap = DataBaseHelper.queryForMap(sql, "hosp_lv", "02");
		returnMap.put("fixmedinsName", "中山市博爱医院");
		returnMap.put("fixmedinsCode","H44200100009");
		returnMap.put("hospLv", dictMap.get("name"));
		returnMap.put("mdtrtId", st.getMdtrtId());
		returnMap.put("setlId", st.getSetlId());
		if(st.getPsnName()==null || st.getPsnName().equals("-")){
			returnMap.put("psnName", pi.getNamePi());
		}else{
			returnMap.put("psnName", st.getPsnName());
		}
		if(st.getGend()==null || st.getGend().equals("-")){
			returnMap.put("gend", pi.getDtSex().equals("02")?"男":pi.getDtSex().equals("03")?"女":"未知");
		}else{
			returnMap.put("gend", st.getGend().equals("1")?"男":st.getGend().equals("2")?"女":"未知");
		}
		if(st.getBrdy()==null || st.getBrdy().length()==0 || st.getBrdy().equals("-")){
			returnMap.put("brdy", DateUtils.formatDate(pi.getBirthDate(), "yyyy-MM-dd"));
		}else{
			returnMap.put("brdy", st.getBrdy());
		}
		/*returnMap.put("psnName", st.getPsnName());
		returnMap.put("gend", st.getGend().equals("1")?"男":st.getGend().equals("2")?"女":"未知");
		returnMap.put("brdy", st.getBrdy());*/
		//returnMap.put("brdy", "2020-10-16");
		if(st.getPsnNo()==null){
			returnMap.put("psnNo", pv.getPsnNo());
		}else{
			returnMap.put("psnNo", st.getPsnNo());
		}
		
		dictMap = DataBaseHelper.queryForMap(sql, "psn_type", st.getPsnType());
		returnMap.put("psnType", dictMap==null?st.getPsnType():dictMap.get("name"));
		returnMap.put("empName", pv.getEmpName());//单位名称
		returnMap.put("tel", pv.getTel());//联系电话
		returnMap.put("mdtrtCertNo", st.getMdtrtCertNo());
		returnMap.put("iptNo", pv.getIptNo());
		returnMap.put("admDeptName", pv.getAdmDeptName());
		returnMap.put("admBed", pv.getAdmBed());
		returnMap.put("begntime", pv.getBegntime().substring(0, 10));
		returnMap.put("endtime", pv.getEndtime().substring(0, 10));
		try {
			int zyts = daysBetween(DateUtils.parseDate(pv.getBegntime()), DateUtils.parseDate(pv.getEndtime()));
			returnMap.put("zyts", zyts==0?1:zyts);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//住院天数
		dictMap = DataBaseHelper.queryForMap(sql, "insutype", pv.getInsutype());
		returnMap.put("insutype", dictMap.get("name"));
		returnMap.put("dscgMaindiagName", pv.getDscgMaindiagName());
		String disSql = "select * from ins_diseinfo_qg where mdtrt_id = ? and inout_diag_type = '2' and del_flag = '0' and maindiag_flag = '1'";
		InsZsbaDiseinfoQg diseInfo = DataBaseHelper.queryForBean(disSql, InsZsbaDiseinfoQg.class, st.getMdtrtId());
		returnMap.put("disDiagName", diseInfo.getDiagName());//出院诊断
		dictMap = DataBaseHelper.queryForMap(sql, "med_type", pv.getMedType());
		returnMap.put("medType", dictMap.get("name"));
		returnMap.put("setlTime",  st.getSetlTime());
		returnMap.put("medfeeSumamt",  df.format(st.getMedfeeSumamt().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("fundPaySumamt",  df.format(st.getFundPaySumamt().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("grzf",  df.format(st.getMedfeeSumamt().subtract(st.getFundPaySumamt()).setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("actPayDedc",  df.format(st.getActPayDedc().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("xzf",  df.format(st.getFulamtOwnpayAmt().add(st.getOverlmtSelfpay()).add(st.getPreselfpayAmt()).setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("dnzf",  df.format(st.getMedfeeSumamt().subtract(st.getFundPaySumamt()).subtract(new BigDecimal(returnMap.get("xzf").toString())).setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("hifpPay",  df.format(st.getHifpPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("hifobPay",  df.format(st.getHifobPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("hifmiPay",  df.format(st.getHifmiPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("hifesPay",  df.format(st.getHifesPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("mafPay",  df.format(st.getMafPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("cvlservPay",  df.format(st.getCvlservPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("othPay",  df.format(st.getOthPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		returnMap.put("acctPay",  df.format(st.getAcctPay().setScale(2, BigDecimal.ROUND_HALF_UP)));
		
		StringBuffer sb = new StringBuffer("");
		sb.append("select med_chrgitm_type, sum(det_item_fee_sumamt) as det_item_fee_sumamt, ");
		//sb.append(" sum(inscp_scp_amt) as inscp_scp_amt, sum(preselfpay_amt) as  preselfpay_amt");
		sb.append(" sum(a.fulamt_ownpay_amt+a.overlmt_amt) as inscp_scp_amt,");
		sb.append(" sum(preselfpay_amt) as preselfpay_amt");
		sb.append(" from INS_FEEDETAIL_QG a ");
		sb.append(" where a.del_flag = '0' and mdtrt_id = ?");
		sb.append(" group by a.med_chrgitm_type");
		sb.append(" order by a.med_chrgitm_type");
		List<Map<String, Object>> feeList = DataBaseHelper.queryForList(sb.toString(), pv.getMdtrtId());
		
		StringBuffer sb2 = new StringBuffer("");
		sb2.append("select med_chrgitm_type, sum(det_item_fee_sumamt) as det_item_fee_sumamt, ");
		//sb.append(" sum(inscp_scp_amt) as inscp_scp_amt, sum(preselfpay_amt) as  preselfpay_amt");
		sb2.append(" sum(a.fulamt_ownpay_amt+a.overlmt_amt) as inscp_scp_amt,");
		sb2.append(" sum(preselfpay_amt) as preselfpay_amt");
		sb2.append(" from INS_FEEDETAIL2301_QG a ");
		sb2.append(" where a.del_flag = '0' and mdtrt_id = ?");
		sb2.append(" group by a.med_chrgitm_type");
		sb2.append(" order by a.med_chrgitm_type");
		
		if(feeList.size()==0){
			InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
			if(signIn.getCode().equals("0")){
				Input5204 input  = new Input5204();
				Input5204Data data = new Input5204Data();
				data.setPsn_no(pv.getPsnNo());
				data.setSetl_id(st.getSetlId());
				data.setMdtrt_id(st.getMdtrtId());
				input.setData(data);
				OutputData5204 paramOut = YbFunUtils.fun5204(input, pv.getInsuplcAdmdvs(), signIn.getSignNo());
				if(paramOut.getInfcode()!=null &&paramOut.getInfcode().equals("0")){
					//insQgybFeedetailMapper.updateInsFeedetailQg(pv.getPkInspvqg());
					for (Output5204Fymx result : paramOut.getOutput()) {
						//将医保返回的信息set到实体类中
						DataBaseHelper.insertBean(YbToInsFeedetail(result, st));
					}
					feeList = DataBaseHelper.queryForList(sb.toString(), pv.getMdtrtId());
					if(feeList.size()==0){
						feeList = DataBaseHelper.queryForList(sb2.toString(), pv.getMdtrtId());
					}
					//组装结算清单中14项的数据
					returnMap.put("list", assembleQdmx(feeList));
				}else{
					returnMap.put("code", -1);
					returnMap.put("msg", paramOut.getErr_msg()+paramOut.getMessage());
				}
			}else{
				returnMap.put("code", signIn.getCode());
				returnMap.put("msg", signIn.getMsg());
			}
		}else{
			//if(feeList.size()==0){
				feeList = DataBaseHelper.queryForList(sb2.toString(), pv.getMdtrtId());
			//}
			returnMap.put("list", assembleQdmx(feeList));
		}
		return returnMap;
	}

	/**
	 *  将医保返回的值赋值到费用明细实体类
	 * @param result
	 * @param st
	 * @return
	 */
	private InsZsbaFeedetailQg YbToInsFeedetail(Output5204Fymx result, InsZsbaStQg st){
		InsZsbaFeedetailQg feedetail = new InsZsbaFeedetailQg();
		feedetail.setPkInspvqg(st.getPkInspvqg());
		feedetail.setPkPi(st.getPkPi());
		feedetail.setPkPv(st.getPkPv());
		feedetail.setPsnNo(st.getPsnNo());
		feedetail.setMdtrtId(result.getMdtrt_id());
		feedetail.setSetlId(result.getSetl_id());
		feedetail.setFeedetlSn(result.getFeedetl_sn());
		feedetail.setRxDrordNo(result.getRx_drord_no());
		feedetail.setMedType(result.getMed_type());
		try {
			feedetail.setFeeOcurTime(DateUtils.parseDate(result.getFee_ocur_time(), "yyyy-MM-dd HH:mm:ss"));
			feedetail.setOptTime(DateUtils.parseDate(result.getOpt_time(), "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		feedetail.setCnt(new BigDecimal(result.getCnt()==null?"0":result.getCnt()));
		feedetail.setPric(new BigDecimal(result.getPric()==null?"0":result.getPric()));
		feedetail.setSinDosDscr(result.getSin_dos_dscr());
		feedetail.setUsedFrquDscr(result.getUsed_frqu_dscr());
		feedetail.setPrdDays(new BigDecimal(result.getPrd_days()==null?"0":result.getPrd_days()));
		feedetail.setMedcWayDscr(result.getMedc_way_dscr());
		feedetail.setDetItemFeeSumamt(new BigDecimal(result.getDet_item_fee_sumamt()==null?"0":result.getDet_item_fee_sumamt()));
		feedetail.setPricUplmtAmt(new BigDecimal(result.getPric_uplmt_amt()==null?"0":result.getDet_item_fee_sumamt()));
		feedetail.setSelfpayProp(new BigDecimal(result.getSelfpay_prop()==null?"0":result.getSelfpay_prop()));
		feedetail.setFulamtOwnpayAmt(new BigDecimal(result.getFulamt_ownpay_amt()==null?"0":result.getFulamt_ownpay_amt()));
		feedetail.setOverlmtAmt(new BigDecimal(result.getOverlmt_amt()==null?"0":result.getOverlmt_amt()));
		feedetail.setPreselfpayAmt(new BigDecimal(result.getPreselfpay_amt()==null?"0":result.getPreselfpay_amt()));
		feedetail.setInscpScpAmt(new BigDecimal(result.getInscp_scp_amt()==null?"0":result.getInscp_scp_amt()));
		feedetail.setChrgitmLv(result.getChrgitm_lv());
		feedetail.setHilistCode(result.getHilist_code());
		feedetail.setHilistName(result.getHilist_name());
		feedetail.setListType(result.getList_type());
		feedetail.setMedListCodg(result.getMed_list_codg());
		feedetail.setMedinsListCodg(result.getMedins_list_codg());
		feedetail.setMedinsListName(result.getMedins_list_name());
		feedetail.setMedChrgitmType(result.getMed_chrgitm_type());
		feedetail.setProdname(result.getProdname());
		feedetail.setSpec(result.getSpec());
		feedetail.setDosformName(result.getDosform_name());
		feedetail.setBilgDeptCodg(result.getBilg_dept_codg());
		feedetail.setBilgDeptName(result.getBilg_dept_name());
		feedetail.setBilgDrCodg(result.getBilg_dr_codg());
		feedetail.setBilgDrName(result.getBilg_dr_name());
		feedetail.setAcordDeptCodg(result.getAcord_dept_codg());
		feedetail.setAcordDeptName(result.getAcord_dept_name());
		feedetail.setOrdersDrCode(result.getOrders_dr_code());
		feedetail.setOrdersDrName(result.getOrders_dr_name());
		feedetail.setLmtUsedFlag(result.getLmt_used_flag());
		feedetail.setHospPrepFlag(result.getHosp_prep_flag());
		feedetail.setHospApprFlag(result.getHosp_appr_flag());
		feedetail.setTcmdrugUsedWay(result.getTcmdrug_used_way());
		feedetail.setProdplacType(result.getProdplac_type());
		feedetail.setBasMednFlag(result.getBas_medn_flag());
		feedetail.setHiNegoDrugFlag(result.getHi_nego_drug_flag());
		feedetail.setChldMedcFlag(result.getChld_medc_flag());
		feedetail.setEtipFlag(result.getEtip_flag());
		feedetail.setEtipHospCode(result.getEtip_hosp_code());
		feedetail.setDscgTkdrugFlag(result.getDscg_tkdrug_flag());
		feedetail.setListSpItemFlag(result.getList_sp_item_flag());
		feedetail.setMatnFeeFlag(result.getMatn_fee_flag());
		feedetail.setDrtReimFlag(result.getDrt_reim_flag());
		feedetail.setMemo(result.getMemo());
		feedetail.setOpterId(result.getOpter_id());
		feedetail.setOpterName(result.getOpter_name());
		return feedetail;
	}
	
	/**
	 * 组装结算清单中14项的数据
	 * @param feeList
	 * @return
	 */
	private List<Map<String, Object>> assembleQdmx(List<Map<String, Object>> feeList){
		DecimalFormat df = new DecimalFormat("#0.00#");
		//报表中费用分类是分左右的，但报表无法实现这功能，只能用两个子报表来实现，所以需要两个list
		List<Map<String, Object>> bbFeeList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		Double zfy = 0d;
		Double zf = 0d;
		Double bfxmzf = 0d;
		boolean fymxFlag = false;
		
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("01")){
				map.put("fymc", "床位费");
				map.put("zfy", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy").toString());
				zf += Double.parseDouble(map.get("zf").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc", "床位费");
			map.put("zfy", "0.00");
			map.put("zf", "0.00");
			map.put("bfxmzf","0.00");
		}else{
			fymxFlag = false;
		}
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("09")){
				map.put("fymc2", "西药费");
				map.put("zfy2", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf2", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf2", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy2").toString());
				zf += Double.parseDouble(map.get("zf2").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf2").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc2", "西药费");
			map.put("zfy2", "0.00");
			map.put("zf2", "0.00");
			map.put("bfxmzf2","0.00");
		}else{
			fymxFlag = false;
		}
		bbFeeList.add(map);
		map = new HashMap<String, Object>();
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("10")){
				map.put("fymc", "中草费");
				map.put("zfy", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy").toString());
				zf += Double.parseDouble(map.get("zf").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc", "中草费");
			map.put("zfy", "0.00");
			map.put("zf", "0.00");
			map.put("bfxmzf","0.00");
		}else{
			fymxFlag = false;
		}
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("11")){
				map.put("fymc2", "中成药");
				//map.put("fymc2", feeMap.get("name"));
				map.put("zfy2", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf2", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf2", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy2").toString());
				zf += Double.parseDouble(map.get("zf2").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf2").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc2", "中成药");
			map.put("zfy2", "0.00");
			map.put("zf2", "0.00");
			map.put("bfxmzf2","0.00");
		}else{
			fymxFlag = false;
		}
		bbFeeList.add(map);
		map = new HashMap<String, Object>();
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("02")){
				map.put("fymc", "诊查费");
				map.put("zfy", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy").toString());
				zf += Double.parseDouble(map.get("zf").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc", "诊查费");
			map.put("zfy", "0.00");
			map.put("zf", "0.00");
			map.put("bfxmzf","0.00");
		}else{
			fymxFlag = false;
		}
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("03")){
				map.put("fymc2", "检查费");
				map.put("zfy2", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf2", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf2", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy2").toString());
				zf += Double.parseDouble(map.get("zf2").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf2").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc2", "检查费");
			map.put("zfy2", "0.00");
			map.put("zf2", "0.00");
			map.put("bfxmzf2","0.00");
		}else{
			fymxFlag = false;
		}
		bbFeeList.add(map);
		map = new HashMap<String, Object>();
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("05")){
				map.put("fymc", "治疗费");
				map.put("zfy", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy").toString());
				zf += Double.parseDouble(map.get("zf").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc", "治疗费");
			map.put("zfy", "0.00");
			map.put("zf", "0.00");
			map.put("bfxmzf","0.00");
		}else{
			fymxFlag = false;
		}
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("07")){
				map.put("fymc2", "护理费");
				map.put("zfy2", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf2", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf2", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy2").toString());
				zf += Double.parseDouble(map.get("zf2").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf2").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc2", "护理费");
			map.put("zfy2", "0.00");
			map.put("zf2", "0.00");
			map.put("bfxmzf2","0.00");
		}else{
			fymxFlag = false;
		}
		bbFeeList.add(map);
		map = new HashMap<String, Object>();
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("06")){
				map.put("fymc", "手术费");
				map.put("zfy", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy").toString());
				zf += Double.parseDouble(map.get("zf").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc", "手术费");
			map.put("zfy", "0.00");
			map.put("zf", "0.00");
			map.put("bfxmzf","0.00");
		}else{
			fymxFlag = false;
		}
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("04")){
				map.put("fymc2", "化验费");
				map.put("zfy2", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf2", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf2", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy2").toString());
				zf += Double.parseDouble(map.get("zf2").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf2").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc2", "化验费");
			map.put("zfy2", "0.00");
			map.put("zf2", "0.00");
			map.put("bfxmzf2","0.00");
		}else{
			fymxFlag = false;
		}
		bbFeeList.add(map);
		map = new HashMap<String, Object>();
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("08")){
				map.put("fymc", "材料费");
				//map.put("fymc", feeMap.get("name"));
				map.put("zfy", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy").toString());
				zf += Double.parseDouble(map.get("zf").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc", "材料费");
			map.put("zfy", "0.00");
			map.put("zf", "0.00");
			map.put("bfxmzf","0.00");
		}else{
			fymxFlag = false;
		}
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("13")){
				map.put("fymc2", "挂号费");
				map.put("zfy2", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf2", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf2", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy2").toString());
				zf += Double.parseDouble(map.get("zf2").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf2").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc2", "挂号费");
			map.put("zfy2", "0.00");
			map.put("zf2", "0.00");
			map.put("bfxmzf2","0.00");
		}else{
			fymxFlag = false;
		}
		bbFeeList.add(map);
		map = new HashMap<String, Object>();
		Double zfyQt = 0d;
		Double zfQt = 0d;
		Double bfxmzfQt = 0d;
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")==null || feeMap.get("medChrgitmType").equals("14")||feeMap.get("medChrgitmType").equals("null")){
				map.put("fymc", "其它");
				Double zfyqt = Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString());
				Double zfqt = Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString());
				Double bfxmzfqt = Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString());
				zfyQt = zfyQt + zfyqt;
				zfQt = zfQt + zfqt;
				bfxmzfQt = bfxmzfQt + bfxmzfqt;
				zfy += zfyqt;
				zf += zfqt;
				bfxmzf += bfxmzfqt;
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc", "其它");
			zfyQt = 0d;
			zfQt = 0d;
			bfxmzfQt = 0d;
		}else{
			fymxFlag = false;
		}
		map.put("zfy", df.format(zfyQt));
		map.put("zf", df.format(zfQt));
		map.put("bfxmzf",df.format(bfxmzfQt));
		
		for (Map<String, Object> feeMap : feeList) {
			if(feeMap.get("medChrgitmType")!=null && feeMap.get("medChrgitmType").equals("12")){
				map.put("fymc2", "一般诊疗费");
				map.put("zfy2", df.format(Double.parseDouble(feeMap.get("detItemFeeSumamt")==null?"0.00":feeMap.get("detItemFeeSumamt").toString())));
				map.put("zf2", df.format(Double.parseDouble(feeMap.get("inscpScpAmt")==null?"0.00":feeMap.get("inscpScpAmt").toString())));
				map.put("bfxmzf2", df.format(Double.parseDouble(feeMap.get("preselfpayAmt")==null?"0.00":feeMap.get("preselfpayAmt").toString())));
				zfy += Double.parseDouble(map.get("zfy2").toString());
				zf += Double.parseDouble(map.get("zf2").toString());
				bfxmzf += Double.parseDouble(map.get("bfxmzf2").toString());
				fymxFlag = true;
			}
		}
		if(!fymxFlag){
			map.put("fymc2", "一般诊疗费");
			map.put("zfy2", "0.00");
			map.put("zf2", "0.00");
			map.put("bfxmzf2","0.00");
		}else{
			fymxFlag = false;
		}
		bbFeeList.add(map);
		
		//合计
		map = new HashMap<String, Object>();
		map.put("fymc", "合计");
		map.put("zfy", df.format(zfy));
		map.put("zf", df.format(zf));
		map.put("bfxmzf", df.format(bfxmzf));
		bbFeeList.add(map);
		return bbFeeList;
	}
	
	/**
	 * 下载医保费用明细（022003024047）
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object>  get5204Data(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String pkInsstqg = jo.getString("pkInsstqg");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkInsstqg", pkInsstqg);
		
		InsZsbaStQg st = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_insstqg = ?", InsZsbaStQg.class, pkInsstqg);
		
		List<Map<String, Object>> iteminfo2301MapList  = insQgybPvMapper.get4101Iteminfo2301(paramMap);
		List<Map<String, Object>> iteminfo5204MapList  = insQgybPvMapper.get4101Iteminfo5204(paramMap);
		
		if((iteminfo2301MapList!=null && iteminfo2301MapList.size()!=0)||(iteminfo5204MapList!=null && iteminfo5204MapList.size()!=0))
		{
			returnMap.put("code", 0);
			returnMap.put("msg", "已有医保结算明细，无需下载！");
		}else{
			InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
			if(signIn.getCode().equals("0")){
				Input5204 input  = new Input5204();
				Input5204Data data = new Input5204Data();
				data.setPsn_no(st.getPsnNo());
				data.setSetl_id(st.getSetlId());
				data.setMdtrt_id(st.getMdtrtId());
				input.setData(data);
				OutputData5204 paramOut = YbFunUtils.fun5204(input, "442000", signIn.getSignNo());
				if(paramOut.getInfcode()!=null &&paramOut.getInfcode().equals("0")){
					//insQgybFeedetailMapper.updateInsFeedetailQg(pv.getPkInspvqg());
					for (Output5204Fymx result : paramOut.getOutput()) {
						//将医保返回的信息set到实体类中
						DataBaseHelper.insertBean(YbToInsFeedetail(result, st));
						returnMap.put("code", 0);
						returnMap.put("msg", "调用成功！");
					}
				}else{
					returnMap.put("code", -1);
					returnMap.put("msg", paramOut.getErr_msg()+paramOut.getMessage());
				}
			}else{
				returnMap.put("code", signIn.getCode());
				returnMap.put("msg", signIn.getMsg());
			}
		}
		return returnMap;
	}
	
	/**
	 * 计算两个日期值之间相差的天数
	 * @param sendDate
	 * @param startDate
	 * @return
	 */
	public static int daysBetween(Date sendDate, Date startDate) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	        sendDate = sdf.parse(sdf.format(sendDate));
	        startDate = sdf.parse(sdf.format(startDate));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(sendDate);
	    long time1 = cal.getTimeInMillis();
	    cal.setTime(startDate);
	    long time2 = cal.getTimeInMillis();
	    long between_days = (time2 - time1) / (1000 * 3600 * 24);
	    return Integer.parseInt(String.valueOf(between_days));
	}
}
