package com.zebone.nhis.pro.zsba.compay.ins.qgyb.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.ins.pub.service.InsPubSignInService;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbFunUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2501ARefmedin;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2501Refmedin;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2502Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaSignInQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaZybaQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2501;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2501A;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2502;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2501;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2501A;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2502;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybPvMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class InsQgZybaService {

	@Autowired
	private InsZsybPvMapper insPvMapper;
	
	@Autowired
	private InsPubSignInService insPubSignInService;
	
	//转院备案操作
	//1.获取待备案信息
	//2.如果还未做医保登记的，需要先获取人员编码，再做登记
	//3.已经做过登记的，直接备案
	//4.已经备案过的，可以撤销
	
	/**
	 * 获取转院备案所需信息
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InsZsbaZybaQg getZybaData(String param,IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		//1.判断是否已转院备案
		StringBuffer sql = new StringBuffer("select *, begnDate as begn_date, endDate as end_date from ins_zyba_qg where pk_pv = ? and flag_cancel  = '0' and del_flag = '0'");
		InsZsbaZybaQg zyba = DataBaseHelper.queryForBean(sql.toString(), InsZsbaZybaQg.class, pkPv);
		if(zyba==null){
			zyba = new InsZsbaZybaQg();
			//2.判断是否已医保入院登记
			sql = new StringBuffer("SELECT a.pk_pi,a.pk_pv,b.psn_no,b.insutype,a.tel_rel,a.addr_cur_dt,");
			sql.append(" b.insuplc_admdvs,b.dscg_maindiag_code,b.dscg_maindiag_name,b.main_cond_dscr,");
			sql.append(" b.mdtrt_id, b. begntime as begn_date, b.endtime as end_date ");
			sql.append(" FROM PV_ENCOUNTER a ");
			sql.append(" INNER JOIN ins_pv_qg b ON a.pk_pv = b.pk_pv and b.del_flag = '0'");
			sql.append(" INNER JOIN ins_st_qg c ON a.pk_pv = c.pk_pv and b.del_flag = '0'");
			sql.append(" WHERE a.pk_pv = ? ");
			Map<String, Object> map = DataBaseHelper.queryForMap(sql.toString(), pkPv);
			if(map!=null&&map.get("psnNo")!=null){
				zyba.setPkPi(map.get("pkPi").toString());
				zyba.setPkPv(map.get("pkPv").toString());
				zyba.setMdtrtId(map.get("mdtrtId").toString());
				zyba.setPsnNo(map.get("psnNo").toString());
				zyba.setInsutype(map.get("insutype").toString());
				zyba.setTel(map.get("telRel").toString());
				zyba.setAddr(map.get("addrCurDt").toString());
				zyba.setInsuOptins(map.get("insuplcAdmdvs").toString());
				zyba.setDiagCode(map.get("dscgMaindiagCode").toString());
				zyba.setDiagName(map.get("dscgMaindiagName").toString());
				zyba.setDiseCondDscr(map.get("mainCondDscr")==null?"":map.get("mainCondDscr").toString());
				zyba.setBegnDate(map.get("begnDate").toString());
				zyba.setEndDate(map.get("endDate").toString());
				zyba.setCode("0");
			}else{
				zyba.setCode("-1");
				zyba.setMsg("该患者在本院没有医保结算记录，无需转院登记；\n如需转院登记，请先进行医保结算！");
/*				//如果医保没有入院登记的话，需要单独查询入院诊断
				zyba.setPkPi(map.get("pkPi").toString());
				zyba.setPkPv(map.get("pkPv").toString());
				zyba.setPsnNo(map.get("psnNo")==null?"":map.get("psnNo").toString());
				zyba.setInsutype(map.get("insutype")==null?"":map.get("insutype").toString());
				zyba.setTel(map.get("telRel")==null?"":map.get("telRel").toString());
				zyba.setAddr(map.get("addrCurDt")==null?"":map.get("addrCurDt").toString());
				zyba.setInsuOptins(map.get("insuplcAdmdvs")==null?"":map.get("insuplcAdmdvs").toString());
				
				Map<String,Object> param_h = new HashMap<String,Object>();
				param_h.put("pkPv", pkPv);
				List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>> ();
				if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
					 diagDataList = insPvMapper.getDiagData(param_h);
				}else{
					diagDataList = insPvMapper.getDiagData2(param_h);
				}
				if(diagDataList!=null && diagDataList.size()>0){
					if(diagDataList.get(0).get("diagcode")!=null){
						zyba.setDiagCode(diagDataList.get(0).get("diagcode").toString());
					}else{
						zyba.setDiagCode("");
					}
					if(diagDataList.get(0).get("diagname")!=null){
						zyba.setDiagName(diagDataList.get(0).get("diagname").toString());
					}else{
						zyba.setDiagName("");
					}
					if(diagDataList.get(0).get("diagDesc")!=null){
						zyba.setDiseCondDscr(diagDataList.get(0).get("diagDesc").toString());
					}else{
						zyba.setDiseCondDscr("");
					}
				}else{
					zyba.setDiagCode("");
					zyba.setDiagName("");
					zyba.setDiseCondDscr("");
				}*/
			}
		}else{
			zyba.setCode("0");
			//zyba.setMsg("该患者已转院备案！");
		}
		return zyba;
	}

	/**
	 * 转院备案(中山2501A)
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InsZsbaZybaQg saveZyba(String param,IUser user){
		InsZsbaZybaQg zyba = JsonUtil.readValue(param, InsZsbaZybaQg.class);
		String ip = zyba.getIp();
		String mac = zyba.getMac();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			Input2501A  input = new Input2501A();
			Input2501ARefmedin refmedin = new Input2501ARefmedin();
			refmedin.setRefl_old_mdtrt_id(zyba.getMdtrtId());
			refmedin.setPsn_no(zyba.getPsnNo());
			refmedin.setInsutype(zyba.getInsutype());
			refmedin.setTel(zyba.getTel());
			refmedin.setAddr(zyba.getAddr());
			refmedin.setInsu_optins(zyba.getInsuOptins());
			refmedin.setDiag_code(zyba.getDiagCode());
			refmedin.setDiag_name(zyba.getDiagName());
			refmedin.setDise_cond_dscr(zyba.getDiseCondDscr());
			refmedin.setReflin_medins_name(zyba.getReflinMedinsName());
			refmedin.setReflin_medins_no(zyba.getReflinMedinsNo());
			refmedin.setMdtrtarea_admdvs(zyba.getMdtrtareaAdmdvs());
			refmedin.setHosp_agre_refl_flag(zyba.getHospAgreReflFlag());
			refmedin.setRefl_type(zyba.getReflType());
			//refmedin.setRefl_date(DateUtils.formatDate(zyba.getReflDate(), "yyyy-MM-dd"));
			refmedin.setRefl_date(zyba.getReflDate());
			refmedin.setRefl_rea(zyba.getReflRea());
			refmedin.setRefl_opnn(zyba.getReflOpnn());
			refmedin.setBegndate(zyba.getBegnDate());
			refmedin.setEnddate(zyba.getEndDate());
			input.setRefmedin(refmedin);
			OutputData2501A paramOut = YbFunUtils.fun2501A(input, refmedin.getInsu_optins(), signIn.getSignNo());
			
			if(paramOut.getInfcode()!=null &&paramOut.getInfcode().equals("0")){
				//保存his
				zyba.setTrtDclaDetlSn(paramOut.getOutput().getResult().getTrt_dcla_detl_sn());
				zyba.setFlagCancel("0");
				DataBaseHelper.insertBean(zyba);
				zyba.setCode("0");
				zyba.setMsg("转院备案成功");
			}else{
				zyba.setCode("-1");
				zyba.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
			}
		}else{
			zyba.setCode(signIn.getCode());
			zyba.setMsg(signIn.getMsg());
		}
		
		return zyba;
	}

	/**
	 * 转院备案(异地2501)
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InsZsbaZybaQg saveZybaYd(String param,IUser user){
		InsZsbaZybaQg zyba = JsonUtil.readValue(param, InsZsbaZybaQg.class);
		String ip = zyba.getIp();
		String mac = zyba.getMac();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			Input2501  input = new Input2501();
			Input2501Refmedin refmedin = new Input2501Refmedin();
			refmedin.setPsn_no(zyba.getPsnNo());
			refmedin.setInsutype(zyba.getInsutype());
			refmedin.setTel(zyba.getTel());
			refmedin.setAddr(zyba.getAddr());
			refmedin.setInsu_optins(zyba.getInsuOptins());
			refmedin.setDiag_code(zyba.getDiagCode());
			refmedin.setDiag_name(zyba.getDiagName());
			refmedin.setDise_cond_dscr(zyba.getDiseCondDscr());
			refmedin.setReflin_medins_name(zyba.getReflinMedinsName());
			refmedin.setReflin_medins_no(zyba.getReflinMedinsNo());
			refmedin.setMdtrtarea_admdvs(zyba.getMdtrtareaAdmdvs());
			refmedin.setHosp_agre_refl_flag(zyba.getHospAgreReflFlag());
			refmedin.setRefl_type(zyba.getReflType());
			//refmedin.setRefl_date(DateUtils.formatDate(zyba.getReflDate(), "yyyy-MM-dd"));
			refmedin.setRefl_date(zyba.getReflDate());
			refmedin.setRefl_rea(zyba.getReflRea());
			refmedin.setRefl_opnn(zyba.getReflOpnn());
			refmedin.setBegndate(zyba.getBegnDate());
			refmedin.setEnddate(zyba.getEndDate());
			input.setRefmedin(refmedin);
			OutputData2501 paramOut = YbFunUtils.fun2501(input, refmedin.getInsu_optins(), signIn.getSignNo());
			
			if(paramOut.getInfcode()!=null &&paramOut.getInfcode().equals("0")){
				//保存his
				zyba.setTrtDclaDetlSn(paramOut.getOutput().getReslut().getTrt_dcla_detl_sn());
				zyba.setFlagCancel("0");
				DataBaseHelper.insertBean(zyba);
				zyba.setCode("0");
				zyba.setMsg("转院备案成功");
			}else{
				zyba.setCode("-1");
				zyba.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
			}
		}else{
			zyba.setCode(signIn.getCode());
			zyba.setMsg(signIn.getMsg());
		}
		
		return zyba;
	}

	
	/**
	 * 转院备案撤销
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsbaZybaQg cancelZyba(String param,IUser user){
		InsZsbaZybaQg zyba = JsonUtil.readValue(param, InsZsbaZybaQg.class);
		String ip = zyba.getIp();
		String mac = zyba.getMac();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			if(zyba==null || StringUtils.isEmpty(zyba.getTrtDclaDetlSn()) || StringUtils.isEmpty(zyba.getPsnNo())
					||StringUtils.isEmpty(zyba.getInsuOptins()) ||StringUtils.isEmpty(zyba.getPkZybaQg())){
				zyba.setCode("-1");
				zyba.setMsg("住院备案主键、待遇申报明细流水号、人员编号、参保地不能空");
			}else{
				Input2502Data data = new Input2502Data();
				data.setTrt_dcla_detl_sn(zyba.getTrtDclaDetlSn());
				data.setPsn_no(zyba.getPsnNo());
				data.setMemo(zyba.getMemo());
				Input2502 input = new Input2502();
				input.setData(data);
				OutputData2502 paramOut = YbFunUtils.fun2502(input, zyba.getInsuOptins(), signIn.getSignNo());
				if(paramOut.getInfcode()!=null &&paramOut.getInfcode().equals("0")){
					InsZsbaZybaQg zybaCx = new InsZsbaZybaQg();
					zybaCx.setPkZybaQg(zyba.getPkZybaQg());
					zybaCx.setFlagCancel("1");
					zybaCx.setMemo(zyba.getMemo());
					DataBaseHelper.updateBeanByPk(zybaCx, false);
					zyba.setFlagCancel("1");
					zyba.setCode("0");
					zyba.setMsg("转院备案撤销成功");
				}else{
					zyba.setCode("-1");
					zyba.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
				}
			}
		}else{
			zyba.setCode(signIn.getCode());
			zyba.setMsg(signIn.getMsg());
		}
		return zyba;
	}
}
