package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.dao.InsPubPvOutMapper;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InHosInitData;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsPvWi;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsStWi;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.OutHosInitData;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.PersonRowInfo;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.SngsChargeDetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.UpdateStatusReturnData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybPvMapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 外部医保-省内工伤医保就诊登记
 * @author lipz
 *
 */
@Service
public class InsPvWiService {
	
	
	@Autowired
	private InsZsybPvMapper insPvMapper;
	@Autowired
	private InsPubPvOutMapper insPubPvOutMapper;
	
	
	/**
	 * 1.省内工伤医保 - 获取入院登记界面初始化数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InHosInitData getSngsInitInHospData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		
		//获取普通入院登记信息
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
		if(basicDataList.size()==0){
			throw new BusException("查不到该患者的住院信息，无法获取数据！");
		}
		
		//设置返回前端初始化数据
		InHosInitData inHosInitData = new InHosInitData();
		
		Map<String,Object> baseData = basicDataList.get(0);
		inHosInitData.setIpTimes(baseData.get("ip_times")==null?null:baseData.get("ip_times").toString());//住院次数 
		inHosInitData.setDtSex(baseData.get("dt_sex")==null?null:baseData.get("dt_sex").toString());//性别编码 
		inHosInitData.setBka020(baseData.get("name_dept")==null?null:baseData.get("name_dept").toString());//就诊科室名称	 
		inHosInitData.setBka022(baseData.get("name_dept_ns")==null?null:baseData.get("name_dept_ns").toString());//病区名称
		
		// 查询是否存在已入院的登记信息
		InsPvWi insPvWi = DataBaseHelper.queryForBean("select * from ins_pv_wi where del_flag='0' and pk_pv=?", InsPvWi.class, pkPv);
		if(insPvWi!=null){
			inHosInitData.setPkInspvwi(insPvWi.getPkInspvwi());//省内工伤医保登记主键
			
			inHosInitData.setPkPi(insPvWi.getPkPi());
			inHosInitData.setPkPv(insPvWi.getPkPv());
			inHosInitData.setPkInsu(insPvWi.getPkInsu());
			inHosInitData.setNamePi(insPvWi.getAac003());//	患者姓名
			inHosInitData.setBirthDate(insPvWi.getAac006());// 出生日期
			
			inHosInitData.setAka130(insPvWi.getAka130());//	业务类型，见码表，41：工伤门诊、42：工伤住院
			inHosInitData.setBka006(insPvWi.getBka006());//	医疗待遇类型	见码表
			inHosInitData.setBka017(insPvWi.getBka017());//	住院时间	格式：yyyyMMdd
			inHosInitData.setBka019(insPvWi.getBka019());//	就诊科室	 
			inHosInitData.setBka021(insPvWi.getBka021());//	病区编码	
			inHosInitData.setBka023(insPvWi.getBka023());//	床位号	 	
			inHosInitData.setBka025(insPvWi.getBka025());//	住院号	 
			inHosInitData.setBka026(insPvWi.getBka026());//	诊断	疾病ICD编码
			inHosInitData.setBka503(insPvWi.getBka503());//	医师编码
			
			inHosInitData.setAaz218(insPvWi.getAaz218());//	就医登记号
			inHosInitData.setAae140(insPvWi.getAae140());//	险种编码,工伤：410
			
			inHosInitData.setStatus(insPvWi.getStatus());
			
			PersonRowInfo gSPatientInfo = new PersonRowInfo();
			gSPatientInfo.setAaa027(insPvWi.getAaa027());
			gSPatientInfo.setAab001(insPvWi.getAab001());
			gSPatientInfo.setAac001(insPvWi.getAac001());
			gSPatientInfo.setAac002(insPvWi.getAac002());
			gSPatientInfo.setAac003(insPvWi.getAac003());
			gSPatientInfo.setAac004(insPvWi.getAac004());
			gSPatientInfo.setAac006(insPvWi.getAac006());
			//gSPatientInfo.setAac013(insPvWi.getaac)
			gSPatientInfo.setAae005(insPvWi.getAae005());
			//gSPatientInfo.setAae030(insPvWi.getaae)
			//gSPatientInfo.setAae031(insPvWi.getaae)
			gSPatientInfo.setAae140(insPvWi.getAae140());
			//gSPatientInfo.setAaz267(insPvWi.getaaz)
			gSPatientInfo.setBaa027(insPvWi.getBaa027());
			gSPatientInfo.setBka004(insPvWi.getBka004());
			gSPatientInfo.setBka005(insPvWi.getBka005());
			gSPatientInfo.setBka006(insPvWi.getBka006());
			gSPatientInfo.setBka008(insPvWi.getBka008());
			gSPatientInfo.setBka026(insPvWi.getBka026());
			gSPatientInfo.setBka042(insPvWi.getBka042());
			//gSPatientInfo.setBka888(insPvWi.getbka8)

			inHosInitData.setgSPatientInfo(gSPatientInfo);
		}else{
			inHosInitData.setPkPi(baseData.get("pk_pi")==null?null:baseData.get("pk_pi").toString());
			inHosInitData.setPkPv(pkPv);
			inHosInitData.setNamePi(baseData.get("name_pi")==null?null:baseData.get("name_pi").toString());//患者姓名
			Date birthDate = baseData.get("birth_date")==null?null:DateUtils.strToDate(baseData.get("birth_date").toString(), "yyyyMMdd");
			inHosInitData.setBirthDate(birthDate==null?null:DateUtils.dateToStr("yyyyMMdd", birthDate));//出生日期	格式：yyyyMMdd
			
			inHosInitData.setAka130("42");//业务类型，见码表，41：工伤门诊、42：工伤住院
			inHosInitData.setBka006("");//医疗待遇类型	见码表
			Date dateBegin = baseData.get("date_begin")==null?null:DateUtils.strToDate(baseData.get("date_begin").toString(), "yyyyMMdd");
			inHosInitData.setBka017(dateBegin==null?null:DateUtils.dateToStr("yyyyMMdd", dateBegin));//住院时间	格式：yyyyMMdd
			inHosInitData.setBka019(baseData.get("code_insdept")==null?null:baseData.get("code_insdept").toString());//就诊科室	 
			inHosInitData.setBka021(baseData.get("pk_dept_ns")==null?null:baseData.get("pk_dept_ns").toString());//病区编码	
			inHosInitData.setBka023(baseData.get("bed_no")==null?null:baseData.get("bed_no").toString());//床位号	 	
			inHosInitData.setBka025(baseData.get("code_ip")==null?null:baseData.get("code_ip").toString());//住院号	 
			
			param_h = new HashMap<String,Object>();
			param_h.put("pkPv", pkPv);
			List<Map<String,Object>> diagDataList = insPvMapper.getDiagData(param_h);
			if(diagDataList.size()>0){
				Map<String,Object> diagData = diagDataList.get(0);
				inHosInitData.setBka026(diagData.get("diagcode")==null?null:diagData.get("diagcode").toString());//	诊断	疾病ICD编码
			}
			
			inHosInitData.setBka503("");//	医师编码
			inHosInitData.setStatus(null);
		}
		
		return inHosInitData;
	}

	/**
	 * 2.省内工伤医保 - 保存或修改医保入院登记信息
	 * @param param
	 * @param user
	 * @return
	 */
	public InsPvWi saveYbInHosData(String param , IUser user){
		InsPvWi insPvWi = JsonUtil.readValue(param, InsPvWi.class);
		try {
			if(StringUtils.isEmpty(insPvWi.getPkPi()) || StringUtils.isEmpty(insPvWi.getPkPv())){
				throw new BusException("患者主键[pkPi]、患者就诊主键[pkPv]不能为空！");
			}
			if(StringUtils.isEmpty(insPvWi.getPkInsu())){
				throw new BusException("医保主计划主键[pkInsu]不能为空 ！");
			}
			if(StringUtils.isEmpty(insPvWi.getAaz218())){
				throw new BusException("就医登记号[aaz218]不能为空！");
			}
			if(StringUtils.isEmpty(insPvWi.getStatus())){
				throw new BusException("状态标志[status]不能为空！");
			}
			
			/*
			 * 先删后增
			 */
			InsPvWi oldInsPvWi = DataBaseHelper.queryForBean("select * from ins_pv_wi where aaz218=?", InsPvWi.class, new Object[]{insPvWi.getAaz218()});
			if(oldInsPvWi!=null){
				DataBaseHelper.execute("delete from ins_pv_wi where pk_inspvwi=?", new Object[]{oldInsPvWi.getPkInspvwi()});
			}
			insPvWi.setEuPvtype("3");
			insPvWi.setInsType("3");
			insPvWi.setPkInspvwi(null);
			insPvWi.setPkOrg(UserContext.getUser().getPkOrg());
			ApplicationUtils.setDefaultValue(insPvWi, true);
			DataBaseHelper.insertBean(insPvWi);
		} catch (Exception e) {
			throw new BusException("保存省内工伤医保登记信息异常："+e.getMessage());
		}
		return insPvWi;
	}

	/**
	 * 3.省内工伤医保 - 获取待上传明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SngsChargeDetailsData> getChargeDetails(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String dateBegin =  jo.getString("dateBegin");
		String dateEnd = DateUtils.getDateTime();
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		param_h.put("dateBegin", dateBegin);
		param_h.put("dateEnd", dateEnd);
		
		List<Map<String,Object>> basicDataList = insPubPvOutMapper.getChargeDetails(param_h);
		basicDataList.addAll(insPubPvOutMapper.getChargeDetailsToYp(param_h));
		
		List<SngsChargeDetailsData> ydmx = new ArrayList<SngsChargeDetailsData>();
		for(int i=0; i<basicDataList.size(); i++){
			SngsChargeDetailsData detailsData = new SngsChargeDetailsData();
			detailsData.setBka063(user.getLoginName());//录入人工号	
			detailsData.setBka064(user.getUserName());//录入人姓名	
			detailsData.setAke005(basicDataList.get(i).get("code")==null?null:basicDataList.get(i).get("code").toString());//医院药品项目编码	 
			detailsData.setAke006(basicDataList.get(i).get("name")==null?null:basicDataList.get(i).get("name").toString());//医院药品项目名称	 
			
			detailsData.setBka056(basicDataList.get(i).get("price")==null?null:basicDataList.get(i).get("price").toString());//单价	精确到小数点后4位
			detailsData.setBka057(basicDataList.get(i).get("quan")==null?null:basicDataList.get(i).get("quan").toString());//用量	精确到小数点后2位
			detailsData.setBka058(basicDataList.get(i).get("amount").toString());//金额	精确到小数点后2位,负数时为退费
			detailsData.setAaz213(String.valueOf(i+1));//费用序列号	
			detailsData.setAka036(basicDataList.get(i).get("flag_fit")==null?"0":basicDataList.get(i).get("flag_fit").toString());//限制使用标志	0：否，1:是
			if(basicDataList.get(i).get("date_pres")==null){
				//处方时间为null的时候用费用发生日期当处方时间
				Date dateHap = basicDataList.get(i).get("date_hap")==null?new Date():DateUtils.strToDate(basicDataList.get(i).get("date_hap").toString(), "yyyy-MM-dd HH:mm:ss");
				detailsData.setBka051(DateUtils.dateToStr("yyyyMMdd", dateHap));//费用发生日期	格式：yyyyMMdd 
			}else{
				Date datePres = DateUtils.strToDate(basicDataList.get(i).get("date_pres").toString(), "yyyy-MM-dd HH:mm:ss");
				detailsData.setBka051(DateUtils.dateToStr("yyyyMMdd", datePres));//费用发生日期	格式：yyyyMMdd 
			}
			detailsData.setBka061("0");
			detailsData.setAaz267(basicDataList.get(i).get("flag_fit")==null?null:basicDataList.get(i).get("flag_fit").toString());
			ydmx.add(detailsData);
		}
		return ydmx;
	}

	/**
	 * 4.省内工伤医保 - 获取出院院登记界面数据
	 * @param param
	 * @param user
	 * @return
	 */
	public OutHosInitData getSngsInitOutHospData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		List<Map<String,Object>> drDataList = insPvMapper.getYdDischargeRegistrationData(param_h);
		if(drDataList.size()==0){
			throw new BusException("查不到该患者的住院信息，无法获取数据！");
		}
		
		OutHosInitData outHosData = new OutHosInitData();
		Map<String,Object> drData = drDataList.get(0);
		
		// 查询是否存在已入院的登记信息
		InsPvWi insPvWi = DataBaseHelper.queryForBean("select * from ins_pv_wi where del_flag='0' and pk_pv=?", InsPvWi.class, pkPv);
		if(insPvWi==null){
			return outHosData;
		}
		
		String status = insPvWi.getStatus().trim();
//		if(status.equals("1") || status.equals("3") || status.equals("4") || status.equals("6") || status.equals("7")){
			outHosData.setPkInspvwi(insPvWi.getPkInspvwi());
			outHosData.setPkInsu(insPvWi.getPkInsu());
			outHosData.setPkPi(insPvWi.getPkPi());
			outHosData.setPkPv(insPvWi.getPkPv());
			
			outHosData.setBka033(user.getLoginName());//登记人员工号	 
			outHosData.setBka034(user.getUserName());//登记人姓名	
			outHosData.setAaz218(insPvWi.getAaz218());//医保就诊登记号
			
			Date cyrq = drData.get("date_end")==null?new Date():DateUtils.strToDate(drData.get("date_end").toString(), "yyyy-MM-dd HH:mm:ss");
			outHosData.setBka032(DateUtils.dateToStr("yyyyMMdd", cyrq));//出院日期	格式：yyyyMMdd
			
			String ryqk = drData.get("dt_level_dise")==null?null:drData.get("dt_level_dise").toString();
			if(ryqk!=null){
				if(ryqk.equals("00")){
					ryqk = "1";
				}else if((ryqk.equals("01"))){
					ryqk = "2";
				}else if((ryqk.equals("02"))){
					ryqk = "3";
				}else{
					//03为其他，医保没有医保，暂时当3（医保：一般）来用
					ryqk = "3";
				}
			}else{
				ryqk = "3";
			}
			outHosData.setBkf003(drData.get("dt_outcomes")==null?"":drData.get("dt_outcomes").toString());//入院情况	 
			//his 1:急诊 2：门诊；  医保 1:门诊2：急诊    除了急诊 其他的都转成门诊
			String dtIntype = "1";
			if(drData.get("dt_outcomes")==null || !drData.get("dt_outcomes").toString().equals("1")){
				dtIntype = "1";
			}else{
				dtIntype = "2";
			}
			outHosData.setBkf002(dtIntype);//入院方式	
			outHosData.setStatus(status);
			
			InsStWi st = DataBaseHelper.queryForBean("select * from ins_st_wi where del_flag='0' and pk_pv=?", InsStWi.class, new Object[]{pkPv});
			if(st!=null){
				outHosData.setBka031(st.getBka031());//出院诊断	 
				outHosData.setBkf004(st.getBkf004());//出院转归情况	 
			}else{
				List<Map<String,Object>> cyDiagDataList = new ArrayList<Map<String,Object>>();
				if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
					cyDiagDataList = insPvMapper.getCyDiagData(param_h);
				}else{
					cyDiagDataList = insPvMapper.getCyDiagData2(param_h);
				}
				if(cyDiagDataList!=null && cyDiagDataList.size()>0){
					Map<String,Object> cyDiagData = cyDiagDataList.get(0);
					//outHosData.setBka031(cyDiagData.get("diagcode").toString() + cyDiagData.get("diagname").toString());//出院诊断	 
					outHosData.setBka031(cyDiagData.get("diagcode").toString());//出院诊断	 
				}else{
					outHosData.setBka031("*");//出院诊断	 
				}
				
				if(drData.get("dt_outcomes")==null){
					outHosData.setBkf004("");
				}else{
					String bkf004 = drData.get("dt_outcomes").toString();
					if(bkf004.equals("00")){
						outHosData.setBkf004("01");//出院转归情况	 
					}else if(bkf004.equals("01")){
						outHosData.setBkf004("02");
					}else if(bkf004.equals("04")){
						outHosData.setBkf004("03");
					}else if(bkf004.equals("03")){
						outHosData.setBkf004("05");
					}else if(bkf004.equals("04")){
						outHosData.setBkf004("99");
					}
				}
			}
//		}else{
//			throw new BusException("当前医保状态为："+status+",不能进行医保出院登记！");
//		}
		return outHosData;
	}

	/**
	 * 5.省内工伤医保 - 修改医保状态
	 * 1入院登记成功，2入院登记失败；3资料维护成功，4资料维护失败；
	 * 5出院登记成功，6出院登记失败；7取消出院登记成功，
	 * 8取消出院登记失败；9取消入院登记成功，
	 * 10取消入院登记失败；11结算成功，
	 * 12结算失败；13取消结算成功，14取消结算失败；15跨月取消结算成功，
	 * 16跨月取消结算失败
	 * 
	 * 主要是 7取消出院登记成功，9取消入院登记成功，13取消结算成功，15跨月取消结算成功使用
	 * @param param
	 * @param user
	 * @return
	 */
	public UpdateStatusReturnData updateStatusByPk(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkInspvwi = jo.getString("pkInspvwi");
		String status = jo.getString("status");
		UpdateStatusReturnData returnData = new UpdateStatusReturnData();
		if(StringUtils.isEmpty(pkInspvwi) && StringUtils.isEmpty(status)){
			throw new BusException("pkInspvwi、status不能为空！");
		}
		
		InsPvWi insPvWi = DataBaseHelper.queryForBean("select * from ins_pv_wi where del_flag='0' and pk_inspvwi=?", InsPvWi.class, new Object[]{pkInspvwi});
		if(insPvWi==null){
			throw new BusException("通过主键pkInspvwi="+pkInspvwi+"，未找到登记记录！");
		}
		
		// 取消出院登记、取消入院登记判断是否结算过，结果过假删、没结算过真删医保结算记录
		if("7".equals(status) || "9".equals(status)){
			InsStWi insStWi = DataBaseHelper.queryForBean("select * from ins_st_wi where del_flag='0' and aaz218=?", InsStWi.class, new Object[]{insPvWi.getAaz218()});
			if(insStWi!=null){
				if(insStWi.getPkSettle()!=null||(insPvWi.getStatus().equals("11")||insPvWi.getStatus().equals("13")||insPvWi.getStatus().equals("14")
						||insPvWi.getStatus().equals("15")||insPvWi.getStatus().equals("16"))){
					insStWi.setDelFlag("1");
					DataBaseHelper.updateBeanByPk(insStWi, false);
				}else{
					DataBaseHelper.execute("delete from ins_st_wi where pk_insstwi=?", new Object[]{insStWi.getPkInsstwi()});
				}
			}
		}
		//取消结算将原来的假删，再添加一条负的，再添加一条新的未结算的
		else if("13".equals(status) || "15".equals(status)){
			InsStWi insStWi = DataBaseHelper.queryForBean("select * from ins_st_wi where del_flag='0' and aaz218=?", InsStWi.class, new Object[]{insPvWi.getAaz218()});
			if(insStWi!=null){
				insStWi.setDelFlag("1");
				DataBaseHelper.updateBeanByPk(insStWi, false);
				
				InsStWi insStWiNew = (InsStWi)insStWi.clone();

				InsStWi insStWiCan = insStWi;
				insStWiCan.setAkc264(insStWiCan.getAkc264().subtract(insStWiCan.getAkc264()).subtract(insStWiCan.getAkc264()));
				insStWiCan.setBka831(insStWiCan.getBka831().subtract(insStWiCan.getBka831()).subtract(insStWiCan.getBka831()));
				insStWiCan.setBka832(insStWiCan.getBka832().subtract(insStWiCan.getBka832()).subtract(insStWiCan.getBka832()));
				insStWiCan.setBka825(insStWiCan.getBka825().subtract(insStWiCan.getBka825()).subtract(insStWiCan.getBka825()));
				insStWiCan.setBka826(insStWiCan.getBka826().subtract(insStWiCan.getBka826()).subtract(insStWiCan.getBka826()));
				insStWiCan.setBka838(insStWiCan.getBka838().subtract(insStWiCan.getBka838()).subtract(insStWiCan.getBka838()));
				insStWiCan.setAkb067(insStWiCan.getAkb067().subtract(insStWiCan.getAkb067()).subtract(insStWiCan.getAkb067()));
				insStWiCan.setAkb066(insStWiCan.getAkb066().subtract(insStWiCan.getAkb066()).subtract(insStWiCan.getAkb066()));
				insStWiCan.setBka821(insStWiCan.getBka821().subtract(insStWiCan.getBka821()).subtract(insStWiCan.getBka821()));
				insStWiCan.setBka839(insStWiCan.getBka839().subtract(insStWiCan.getBka839()).subtract(insStWiCan.getBka839()));
				insStWiCan.setAke039(insStWiCan.getAke039().subtract(insStWiCan.getAke039()).subtract(insStWiCan.getAke039()));
				insStWiCan.setAke035(insStWiCan.getAke035().subtract(insStWiCan.getAke035()).subtract(insStWiCan.getAke035()));
				insStWiCan.setAke026(insStWiCan.getAke026().subtract(insStWiCan.getAke026()).subtract(insStWiCan.getAke026()));
				insStWiCan.setAke029(insStWiCan.getAke029().subtract(insStWiCan.getAke029()).subtract(insStWiCan.getAke029()));
				insStWiCan.setBka841(insStWiCan.getBka841().subtract(insStWiCan.getBka841()).subtract(insStWiCan.getBka841()));
				insStWiCan.setBka842(insStWiCan.getBka842().subtract(insStWiCan.getBka842()).subtract(insStWiCan.getBka842()));
				insStWiCan.setBka840(insStWiCan.getBka840().subtract(insStWiCan.getBka840()).subtract(insStWiCan.getBka840()));
				insStWiCan.setPkInsstwi(null);
				insStWiCan.setPkSettle(null);
				insStWiCan.setCreator(null);
				insStWiCan.setCreateTime(null);
				insStWiCan.setTs(null);
				insStWiCan.setPkInsstwiCan(insStWi.getPkInsstwi());
				DataBaseHelper.insertBean(insStWiCan);
				
				insStWiNew.setPkInsstwi(null);
				insStWiNew.setPkSettle(null);
				insStWiNew.setCreator(null);
				insStWiNew.setCreateTime(null);
				insStWiNew.setTs(null);
				insStWiNew.setDelFlag("0");
				DataBaseHelper.insertBean(insStWiNew);
				
				//返回值
				returnData.setPkInsstwi(insStWiCan.getPkInsstwi());
			}
		}
		
		if("9".equals(status)){
			insPvWi.setDelFlag("1");
		}
		
		// 更新医保就诊登记记录的状态
		insPvWi.setStatus(status);
		DataBaseHelper.updateBeanByPk(insPvWi, false);
		return returnData;
	}

	/**
	 * 功能号：022003004072
	 * 根据就诊主键获取医保登记信息，用于判断是否可以医保出院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public InsPvWi getInsPvWi(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		
		InsPvWi insPvWi = DataBaseHelper.queryForBean("select * from ins_pv_wi where del_flag='0' and pk_pv=?", InsPvWi.class, new Object[]{pkPv});
		return insPvWi;
	}

}
