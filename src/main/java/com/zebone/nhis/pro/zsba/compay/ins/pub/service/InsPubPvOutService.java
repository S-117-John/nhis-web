package com.zebone.nhis.pro.zsba.compay.ins.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.dao.InsPubPvOutMapper;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsPubSaveDetailedParam;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsRegData;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubPvOut;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubTransid;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsPubDetailed;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.YdChargeDetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.YdHosInitialData;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaPvQg;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InHosInitData;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsPvWi;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.PersonRowInfo;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.HosInitialData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsPubPvOutService {
	
	@Autowired
	private InsPubPvOutMapper InsPubPvOutMapper;
	
	@Autowired
	private InsZsybPvMapper insPvMapper;
	
	/**
	 * 获取医保入院登记界面公共初始化数据
	 * @param param
	 * @param user
	 * @return
	 */
	public HosInitialData getInsHosData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		//String pkPi = jo.getString("pkPi");
		String pkPv = jo.getString("pkPv");
		
		HosInitialData hosInitialData = new HosInitialData();
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		
		//获取普通入院登记信息
		List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
		if(basicDataList.size()==0){
			throw new BusException("查不到该患者的住院信息，无法获取数据！");
		}
		
		hosInitialData.setPkPi(basicDataList.get(0).get("pk_pi")!=null?basicDataList.get(0).get("pk_pi").toString():null);//患者主键
		hosInitialData.setPkPv(pkPv);//就诊主键
		hosInitialData.setCodeIp(basicDataList.get(0).get("code_ip")!=null?basicDataList.get(0).get("code_ip").toString():null);//住院号
		hosInitialData.setIpTimes(basicDataList.get(0).get("ip_times")!=null?basicDataList.get(0).get("ip_times").toString():null);//住院次数 
		hosInitialData.setNamePi(basicDataList.get(0).get("name_pi")!=null?basicDataList.get(0).get("name_pi").toString():null);//患者姓名
		hosInitialData.setDtSex(basicDataList.get(0).get("dt_sex")!=null?basicDataList.get(0).get("dt_sex").toString():null);//性别编码
		hosInitialData.setBirthDate(basicDataList.get(0).get("birth_date")!=null?basicDataList.get(0).get("birth_date").toString():null);//出生日期
		//hosInitialData.setPkDept(basicDataList.get(0).get("pk_dept")!=null?basicDataList.get(0).get("pk_dept").toString():null);//入院科室主键
		hosInitialData.setNameDept(basicDataList.get(0).get("name_dept")!=null?basicDataList.get(0).get("name_dept").toString():null);//入院科室名称
		//hosInitialData.setPkDeptNs(basicDataList.get(0).get("pk_dept_ns")!=null?basicDataList.get(0).get("pk_dept_ns").toString():null);//入院病区主键
		hosInitialData.setBqdm(basicDataList.get(0).get("name_dept_ns")!=null?basicDataList.get(0).get("name_dept_ns").toString():null);//入院病区名称
		hosInitialData.setCwdh(basicDataList.get(0).get("bed_no")!=null?basicDataList.get(0).get("bed_no").toString():null);//当前床号
		
		hosInitialData.setDtIdtype(basicDataList.get(0).get("dt_idtype")!=null?basicDataList.get(0).get("dt_idtype").toString():null);
		hosInitialData.setGmsfhm(basicDataList.get(0).get("id_no")!=null?basicDataList.get(0).get("id_no").toString():"");

		hosInitialData.setGrsxh(basicDataList.get(0).get("insur_no")!=null?basicDataList.get(0).get("insur_no").toString():null);//参保号
		hosInitialData.setPkHp(null);//医保主计划
		hosInitialData.setPkInsu(basicDataList.get(0).get("pk_insu")!=null?basicDataList.get(0).get("pk_insu").toString():null);
		hosInitialData.setRyrq(basicDataList.get(0).get("date_begin")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_begin").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//入院时间
		hosInitialData.setCyrq(basicDataList.get(0).get("date_end")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_end").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//出院时间
		//returnMap.put("pkEmpTre", basicDataList.get(0).get("pk_emp_tre")); //收治医生主键
		//returnMap.put("nameEmpTre", basicDataList.get(0).get("name_emp_tre")); //收治医生姓名
		param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>> ();
		if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
			 diagDataList = insPvMapper.getDiagData(param_h);
		}else{
			diagDataList = insPvMapper.getDiagData2(param_h);
		}
		if(diagDataList.size()==0){
			PvIp pvIp = DataBaseHelper.queryForBean("select * from pv_ip where pk_pv = ?", PvIp.class, pkPv);
			hosInitialData.setRyzd(pvIp.getNameDiag());
			hosInitialData.setRyzdgjdm(pvIp.getCodeDiag());//入院ICD-10编码	
		}else{
			//入院诊断哪里不知道做了什么修改，导致诊断总有为空的，所以这里所有诊断数据都要加上为空判断
			for(int i=0; i<diagDataList.size(); i++){
				if(i==0){
					if(diagDataList.get(i) != null){
						if(diagDataList.get(i).get("diagname")!=null){
							hosInitialData.setRyzd(diagDataList.get(i).get("diagname").toString());
						}
						if(diagDataList.get(i).get("diagcode")!=null){
							hosInitialData.setRyzdgjdm(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码	
						}
					}
				}else if(i==1){
					if(diagDataList.get(i) != null){
						if(diagDataList.get(i).get("diagname")!=null){
							hosInitialData.setRyzd2(diagDataList.get(i).get("diagname").toString());
						}
						if(diagDataList.get(i).get("diagcode")!=null){
							hosInitialData.setRyzdgjdm2(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码2
						}
					}
				}else if(i==2){
					if(diagDataList.get(i) != null){
						if(diagDataList.get(i).get("diagname")!=null){
							hosInitialData.setRyzd3(diagDataList.get(i).get("diagname").toString());
						}
						if(diagDataList.get(i).get("diagcode")!=null){
							hosInitialData.setRyzdgjdm3(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码3	
						}
					}
				}else if(i==3){
					if(diagDataList.get(i) != null){
						if(diagDataList.get(i).get("diagname")!=null){
							hosInitialData.setRyzd4(diagDataList.get(i).get("diagname").toString());
						}
					}
					if(diagDataList.get(i).get("diagcode")!=null){
						hosInitialData.setRyzdgjdm4(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码4	
					}
				}
			}
		}
		return hosInitialData;
	}
	
	/**
	 * 根据医保身份获取医保入院登记界面初始化数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsRegData getInsHosDataType(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		//String pkPi = jo.getString("pkPi");
		String pkPv = jo.getString("pkPv");
		String ybCode = jo.getString("ybCode");
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		
		InsRegData insRegData = new InsRegData();
		if(ybCode.equals("00021")||ybCode.equals("00022")||ybCode.equals("00023")||ybCode.equals("00024")||ybCode.equals("00025")){
			HosInitialData hosInitialData = new HosInitialData();
			//获取普通入院登记信息
			List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
			if(basicDataList.size()==0){
				throw new BusException("查不到该患者的住院信息，无法获取数据！");
			}
			
			hosInitialData.setPkPi(basicDataList.get(0).get("pk_pi")!=null?basicDataList.get(0).get("pk_pi").toString():null);//患者主键
			hosInitialData.setPkPv(pkPv);//就诊主键
			hosInitialData.setCodeIp(basicDataList.get(0).get("code_ip")!=null?basicDataList.get(0).get("code_ip").toString():null);//住院号
			hosInitialData.setIpTimes(basicDataList.get(0).get("ip_times")!=null?basicDataList.get(0).get("ip_times").toString():null);//住院次数 
			hosInitialData.setNamePi(basicDataList.get(0).get("name_pi")!=null?basicDataList.get(0).get("name_pi").toString():null);//患者姓名
			hosInitialData.setDtSex(basicDataList.get(0).get("dt_sex")!=null?basicDataList.get(0).get("dt_sex").toString():null);//性别编码
			hosInitialData.setBirthDate(basicDataList.get(0).get("birth_date")!=null?basicDataList.get(0).get("birth_date").toString():null);//出生日期
			//hosInitialData.setPkDept(basicDataList.get(0).get("pk_dept")!=null?basicDataList.get(0).get("pk_dept").toString():null);//入院科室主键
			hosInitialData.setNameDept(basicDataList.get(0).get("name_dept")!=null?basicDataList.get(0).get("name_dept").toString():null);//入院科室名称
			//hosInitialData.setPkDeptNs(basicDataList.get(0).get("pk_dept_ns")!=null?basicDataList.get(0).get("pk_dept_ns").toString():null);//入院病区主键
			hosInitialData.setBqdm(basicDataList.get(0).get("name_dept_ns")!=null?basicDataList.get(0).get("name_dept_ns").toString():null);//入院病区名称
			hosInitialData.setCwdh(basicDataList.get(0).get("bed_no")!=null?basicDataList.get(0).get("bed_no").toString():null);//当前床号
			
			//身份证
			//if(basicDataList.get(0).get("dt_idtype").equals("01")){
				hosInitialData.setGmsfhm(basicDataList.get(0).get("id_no")!=null?basicDataList.get(0).get("id_no").toString():null);
/*			}else{
				hosInitialData.setGmsfhm("");
			}*/
			hosInitialData.setGrsxh(basicDataList.get(0).get("insur_no")!=null?basicDataList.get(0).get("insur_no").toString():null);//参保号
			hosInitialData.setPkHp(null);//医保主计划
			hosInitialData.setRyrq(basicDataList.get(0).get("date_begin")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_begin").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//入院时间
			hosInitialData.setCyrq(basicDataList.get(0).get("date_end")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_end").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//出院时间
			hosInitialData.setYsbh(basicDataList.get(0).get("code_emp").toString());
			//returnMap.put("pkEmpTre", basicDataList.get(0).get("pk_emp_tre")); //收治医生主键
			//returnMap.put("nameEmpTre", basicDataList.get(0).get("name_emp_tre")); //收治医生姓名
			
			InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_pv = ?", InsZsBaYbPv.class, pkPv);
			if(insPv==null){
				insPv = new InsZsBaYbPv();
				insPv.setZzysxm(basicDataList.get(0).get("name_emp_tre")!=null?basicDataList.get(0).get("name_emp_tre").toString():null);
				insPv.setRyqk(basicDataList.get(0).get("dt_outcomes")!=null?basicDataList.get(0).get("dt_outcomes").toString():null);
				param_h = new HashMap<String,Object>();
				param_h.put("pkPv", pkPv);
				List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>> ();
				if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
					 diagDataList = insPvMapper.getDiagData(param_h);
				}else{
					diagDataList = insPvMapper.getDiagData2(param_h);
				}
				for(int i=0; i<diagDataList.size(); i++){
					if(diagDataList.get(i)!=null){
						if(i==0){
							if(diagDataList.get(i).get("diagname")!=null){
								insPv.setRyzd(diagDataList.get(i).get("diagname").toString());
							}
							if(diagDataList.get(i).get("diagcode")!=null){
								insPv.setRyzdgjdm(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码	
							}
						}else if(i==1){
							if(diagDataList.get(i).get("diagname")!=null){
								insPv.setRyzd2(diagDataList.get(i).get("diagname").toString());
							}
							if(diagDataList.get(i).get("diagcode")!=null){
								hosInitialData.setRyzdgjdm2(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码2
							}
						}else if(i==2){
							if(diagDataList.get(i).get("diagname")!=null){
								insPv.setRyzd3(diagDataList.get(i).get("diagname").toString());
							}
							if(diagDataList.get(i).get("diagcode")!=null){
								hosInitialData.setRyzdgjdm3(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码3	
							}
						}else if(i==3){
							if(diagDataList.get(i).get("diagname")!=null){
								insPv.setRyzd4(diagDataList.get(i).get("diagname").toString());
							}
							if(diagDataList.get(i).get("diagcode")!=null){
								hosInitialData.setRyzdgjdm4(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码4	
							}
						}
					}
				}
				hosInitialData.setStatus(null);//状态标志
			}else{
				hosInitialData.setPkInsu(insPv.getPkInsu());//nhis-医保计划主键
				hosInitialData.setBqdm(insPv.getBqdm());//入院病区名称
				hosInitialData.setCwdh(insPv.getCwdh());//当前床号
				hosInitialData.setGmsfhm(insPv.getGmsfhm());//身份证
				hosInitialData.setGrsxh(insPv.getGrsxh());//参保号
				hosInitialData.setPkHp(insPv.getPkHp());//医保主计划
				hosInitialData.setRyrq(insPv.getRyrq());//入院时间
				hosInitialData.setCyrq(insPv.getCyrq());//出院时间
				hosInitialData.setStatus(insPv.getStatus());//状态标志
				
				BdTermDiag  diag2 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getRyzd2());
				BdTermDiag  diag3 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getRyzd3());
				BdTermDiag  diag4 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getRyzd4());
				
				hosInitialData.setRyzdgjdm2(diag2==null?null:diag2.getDiagcode());
				hosInitialData.setRyzdgjdm3(diag3==null?null:diag3.getDiagcode());
				hosInitialData.setRyzdgjdm4(diag4==null?null:diag4.getDiagcode());
			}
			
			hosInitialData.setPkInspv(insPv.getPkInspv());//中山住院医保登记主键
			hosInitialData.setRyzd(insPv.getRyzd()); //入院诊断
			hosInitialData.setRyzdgjdm(insPv.getRyzdgjdm()==null?null:insPv.getRyzdgjdm().trim());//入院ICD-10编码
			hosInitialData.setRyzd2(insPv.getRyzd2());//入院诊断2
			hosInitialData.setRyzd3(insPv.getRyzd3());//入院诊断3
			hosInitialData.setRyzd4(insPv.getRyzd4());//入院诊断4
			hosInitialData.setZzysxm(insPv.getZzysxm());//主诊医师姓名
			hosInitialData.setJsffbz(insPv.getJsffbz());//旧伤复发标志
			hosInitialData.setWsbz(insPv.getWsbz());//外伤标志
			hosInitialData.setSsrq(insPv.getSsrq());//受伤日期
			hosInitialData.setSylb(insPv.getSylb());//生育类别
			hosInitialData.setZszh(insPv.getZszh());//准生证号
			hosInitialData.setJzjlh(insPv.getJzjlh());//医保就诊登记号
			hosInitialData.setXzlx(insPv.getXzlx());//险种类型
			hosInitialData.setRyqk(insPv.getRyqk());//病情转归(入院情况)
			
			insRegData.setHosInitialData(hosInitialData);
		}else if(ybCode.equals("00031") || ybCode.equals("0004")){
			YdHosInitialData hosInitialData = new YdHosInitialData();
			//获取普通入院登记信息
			List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
			if(basicDataList.size()==0){
				throw new BusException("查不到该患者的住院信息，无法获取数据！");
			}
			
			hosInitialData.setPkPi(basicDataList.get(0).get("pk_pi")!=null?basicDataList.get(0).get("pk_pi").toString():null);//患者主键
			hosInitialData.setPkPv(pkPv);//就诊主键
			hosInitialData.setAkc190(basicDataList.get(0).get("code_ip")!=null?basicDataList.get(0).get("code_ip").toString():null);//住院号
			hosInitialData.setIpTimes(basicDataList.get(0).get("ip_times")!=null?basicDataList.get(0).get("ip_times").toString():null);//住院次数 
			hosInitialData.setAac003(basicDataList.get(0).get("name_pi")!=null?basicDataList.get(0).get("name_pi").toString():null);//患者姓名
			hosInitialData.setAac004(basicDataList.get(0).get("dt_sex")!=null?basicDataList.get(0).get("dt_sex").toString():null);//性别编码
			hosInitialData.setAac006(basicDataList.get(0).get("birth_date")!=null?DateUtils.strToDate(basicDataList.get(0).get("birth_date").toString(), "yyyyMMdd"):null);//出生日期
			hosInitialData.setPkDept(basicDataList.get(0).get("pk_dept")!=null?basicDataList.get(0).get("pk_dept").toString():null);//入院科室主键
			hosInitialData.setNameDept(basicDataList.get(0).get("name_dept")!=null?basicDataList.get(0).get("name_dept").toString():null);//入院科室名称
			hosInitialData.setAkf001(basicDataList.get(0).get("code_insdept")!=null?basicDataList.get(0).get("code_insdept").toString():null);//医保科室编码
			hosInitialData.setNameInsdept(basicDataList.get(0).get("name_insdept")!=null?basicDataList.get(0).get("name_insdept").toString():null);//医保科室名称
			hosInitialData.setYzz018(basicDataList.get(0).get("pk_dept_ns")!=null?basicDataList.get(0).get("pk_dept_ns").toString():null);//入院病区主键
			hosInitialData.setYzz019(basicDataList.get(0).get("name_dept_ns")!=null?basicDataList.get(0).get("name_dept_ns").toString():null);//入院病区名称
			hosInitialData.setYkc012(basicDataList.get(0).get("bed_no")!=null?basicDataList.get(0).get("bed_no").toString():null);//当前床号
			hosInitialData.setAka130("21");//医疗类别
			hosInitialData.setAac043("90");//默认90
			//身份证
			//hosInitialData.setAac044("120103199805161212");
			if(basicDataList.get(0).get("dt_idtype").equals("01")){
				hosInitialData.setAac044(basicDataList.get(0).get("id_no")!=null?basicDataList.get(0).get("id_no").toString():null);
			}
			hosInitialData.setYkc701(basicDataList.get(0).get("date_begin")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_begin").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//入院时间
			hosInitialData.setAae004(basicDataList.get(0).get("name_rel")!=null?basicDataList.get(0).get("name_rel").toString():null);
			hosInitialData.setAae005(basicDataList.get(0).get("tel_rel")!=null?basicDataList.get(0).get("tel_rel").toString():null);
			
			InsZsPubPvOut InsPubPvOut = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and pk_pv = ?", InsZsPubPvOut.class, pkPv);
			
			if(InsPubPvOut==null){
				InsPubPvOut = new InsZsPubPvOut();
				InsPubPvOut.setAke022(basicDataList.get(0).get("name_emp_tre")!=null?basicDataList.get(0).get("name_emp_tre").toString():null);
				param_h = new HashMap<String,Object>();
				param_h.put("pkPv", pkPv);
				List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>>();
				if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
					diagDataList = insPvMapper.getDiagData(param_h);
				}else{
					diagDataList = insPvMapper.getDiagData2(param_h);
				}
				for(int i=0; i<diagDataList.size(); i++){
					if(diagDataList.get(i)!=null){
						if(i==0){
							if(diagDataList.get(i).get("diagname")!=null){
								InsPubPvOut.setAkc050(diagDataList.get(i).get("diagname").toString());
							}
							if(diagDataList.get(i).get("diagcode")!=null){
								InsPubPvOut.setAkc193(diagDataList.get(i).get("diagcode").toString());//入院ICD-10编码	
							}
						}else if(i==1){
							if(diagDataList.get(i).get("diagcode")!=null){
								//InsPubPvOut.setYkc601(diagDataList.get(i).get("diagname").toString());
								InsPubPvOut.setYkc601(diagDataList.get(i).get("diagcode").toString());
							}
						}else if(i==2){
							if(diagDataList.get(i).get("diagcode")!=null){
								//InsPubPvOut.setYkc602(diagDataList.get(i).get("diagname").toString());
								InsPubPvOut.setYkc602(diagDataList.get(i).get("diagcode").toString());
							}
						}
					}
				}
				hosInitialData.setStatus(null);//状态标志
				hosInitialData.setBtnUpload("0");
			}else{
				hosInitialData.setPkInsu(InsPubPvOut.getPkInsu());//医保主计划主键
				hosInitialData.setYzz018(InsPubPvOut.getYzz018());//入院病区号
				hosInitialData.setYzz019(InsPubPvOut.getYzz019());//入院病区名称
				hosInitialData.setYkc012(InsPubPvOut.getYkc012());//当前床号
				hosInitialData.setAka130(InsPubPvOut.getAka130());//医疗类别
				hosInitialData.setYkc701(InsPubPvOut.getYkc701());//入院时间
				hosInitialData.setStatus(InsPubPvOut.getStatus().trim());//状态标志
				hosInitialData.setAac043(InsPubPvOut.getAac043());//证件类型
				hosInitialData.setAac044(InsPubPvOut.getAac044());//证件号码
				hosInitialData.setTransid0212(InsPubPvOut.getTransid0212());//就诊登记交易流水号
				hosInitialData.setYab060(InsPubPvOut.getYab060());
				hosInitialData.setAac002(InsPubPvOut.getAac002());
				hosInitialData.setAab301(InsPubPvOut.getAab301());
				hosInitialData.setPkInspvout(InsPubPvOut.getPkInspvout());//异地住院医保登记主键
				hosInitialData.setYkc700(InsPubPvOut.getYkc700());//医保就诊登记号
				hosInitialData.setAae140(InsPubPvOut.getAae140());//险种类型
				hosInitialData.setInsType(InsPubPvOut.getInsType());//医保类型
				hosInitialData.setAae004(InsPubPvOut.getAae004());
				hosInitialData.setAae005(InsPubPvOut.getAae005());
				hosInitialData.setAka130(InsPubPvOut.getAka130());
				hosInitialData.setYkc701(InsPubPvOut.getYkc701());
				
				InsZsPubTransid it = DataBaseHelper.queryForBean("select * from INS_TRANSID where DEL_FLAG = '0' and PK_INSPVOUT = ? and  TRANSID0302 is null", InsZsPubTransid.class, InsPubPvOut.getPkInspvout());
				if(it==null){
					hosInitialData.setBtnUpload("1");
				}else{
					hosInitialData.setBtnUpload("0");
				}
			}
			
			hosInitialData.setAkc050(InsPubPvOut.getAkc050()); //入院诊断
			hosInitialData.setAkc193(InsPubPvOut.getAkc193());//入院ICD-10编码
			hosInitialData.setYkc601(InsPubPvOut.getYkc601());//入院诊断2
			hosInitialData.setYkc602(InsPubPvOut.getYkc602());//入院诊断3
			hosInitialData.setAke022(InsPubPvOut.getAke022());//主诊医师姓名
			hosInitialData.setYkc679(InsPubPvOut.getYkc679());//住院原因
			insRegData.setYdHosInitialData(hosInitialData);
		}else if(ybCode.equals("00032")){
			//获取普通入院登记信息
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
				gSPatientInfo.setAac013(insPvWi.getAac013());
				gSPatientInfo.setAae005(insPvWi.getAae005());
				gSPatientInfo.setAae030(insPvWi.getAae030());
				gSPatientInfo.setAae031(insPvWi.getAae031());
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
				inHosInitData.setBka021(baseData.get("code_dept_ns")==null?null:baseData.get("code_dept_ns").toString());//病区编码	
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
			insRegData.setInHosInitData(inHosInitData);
		}else if(ybCode.equals("00051")||ybCode.equals("00052")||ybCode.equals("00053")||ybCode.equals("00054")
				||ybCode.equals("00058")||ybCode.equals("00059")){//全国医保
			//获取普通入院登记信息
			List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
			if(basicDataList.size()==0){
				throw new BusException("查不到该患者的住院信息，无法获取数据！");
			}
			// 查询是否存在已入院的登记信息
			InsZsbaPvQg insPvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where del_flag='0' and pk_pv=?", InsZsbaPvQg.class, pkPv);
			if(insPvQg==null){
				insPvQg = new InsZsbaPvQg();
				//insPvQg.setPkPv(basicDataList.get(0).get("pk_pv").toString());
				insPvQg.setMdtrtCertType("02");
				insPvQg.setMdtrtCertNo(basicDataList.get(0).get("id_no")!=null?basicDataList.get(0).get("id_no").toString():null);
				insPvQg.setMedType("21");
			}
			insRegData.setQgHosInitialData(insPvQg);
		}
		else{
			throw new BusException("请选择医保的身份！");
		}
		
		return insRegData;
	}
	
	
	/**
	 * 获取异地医保入院登记界面初始化数据(现在是使用上面的统一接口，这个应该没用了)
	 * @param param
	 * @param user
	 * @return
	 */
	public YdHosInitialData getInsYdHosData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		//String pkPi = jo.getString("pkPi");
		String pkPv = jo.getString("pkPv");
		
		YdHosInitialData hosInitialData = new YdHosInitialData();
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		
		//获取普通入院登记信息
		List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
		if(basicDataList.size()==0){
			throw new BusException("查不到该患者的住院信息，无法获取数据！");
		}
		
		hosInitialData.setPkPi(basicDataList.get(0).get("pk_pi")!=null?basicDataList.get(0).get("pk_pi").toString():null);//患者主键
		hosInitialData.setPkPv(pkPv);//就诊主键
		hosInitialData.setAkc190(basicDataList.get(0).get("code_ip")!=null?basicDataList.get(0).get("code_ip").toString():null);//住院号
		hosInitialData.setIpTimes(basicDataList.get(0).get("ip_times")!=null?basicDataList.get(0).get("ip_times").toString():null);//住院次数 
		hosInitialData.setAac003(basicDataList.get(0).get("name_pi")!=null?basicDataList.get(0).get("name_pi").toString():null);//患者姓名
		hosInitialData.setAac004(basicDataList.get(0).get("dt_sex")!=null?basicDataList.get(0).get("dt_sex").toString():null);//性别编码
		hosInitialData.setAac006(basicDataList.get(0).get("birth_date")!=null?DateUtils.strToDate(basicDataList.get(0).get("birth_date").toString(), "yyyyMMdd"):null);//出生日期
		hosInitialData.setPkDept(basicDataList.get(0).get("pk_dept")!=null?basicDataList.get(0).get("pk_dept").toString():null);//入院科室主键
		hosInitialData.setNameDept(basicDataList.get(0).get("name_dept")!=null?basicDataList.get(0).get("name_dept").toString():null);//入院科室名称
		hosInitialData.setAkf001(basicDataList.get(0).get("code_insdept")!=null?basicDataList.get(0).get("code_insdept").toString():null);//医保科室编码
		hosInitialData.setNameInsdept(basicDataList.get(0).get("name_insdept")!=null?basicDataList.get(0).get("name_insdept").toString():null);//医保科室名称
		hosInitialData.setYzz018(basicDataList.get(0).get("pk_dept_ns")!=null?basicDataList.get(0).get("pk_dept_ns").toString():null);//入院病区主键
		hosInitialData.setYzz019(basicDataList.get(0).get("name_dept_ns")!=null?basicDataList.get(0).get("name_dept_ns").toString():null);//入院病区名称
		hosInitialData.setYkc012(basicDataList.get(0).get("bed_no")!=null?basicDataList.get(0).get("bed_no").toString():null);//当前床号
		hosInitialData.setAka130("21");//医疗类别
		hosInitialData.setAac043("90");//默认90
		//身份证
		//hosInitialData.setAac044("120103199805161212");
		if(basicDataList.get(0).get("dt_idtype").equals("01")){
			hosInitialData.setAac044(basicDataList.get(0).get("id_no")!=null?basicDataList.get(0).get("id_no").toString():null);
		}
		hosInitialData.setYkc701(basicDataList.get(0).get("date_begin")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_begin").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//入院时间
		hosInitialData.setAae004(basicDataList.get(0).get("name_rel")!=null?basicDataList.get(0).get("name_rel").toString():null);
		hosInitialData.setAae005(basicDataList.get(0).get("tel_rel")!=null?basicDataList.get(0).get("tel_rel").toString():null);
		
		InsZsPubPvOut InsPubPvOut = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and pk_pv = ?", InsZsPubPvOut.class, pkPv);
		
		if(InsPubPvOut==null){
			InsPubPvOut = new InsZsPubPvOut();
			InsPubPvOut.setAke022(basicDataList.get(0).get("name_emp_tre")!=null?basicDataList.get(0).get("name_emp_tre").toString():null);
			param_h = new HashMap<String,Object>();
			param_h.put("pkPv", pkPv);
			List<Map<String,Object>> diagDataList = insPvMapper.getDiagData(param_h);
			for(int i=0; i<diagDataList.size(); i++){
				if(i==0){
					InsPubPvOut.setAkc050(diagDataList.get(i).get("diagname").toString());
					InsPubPvOut.setAkc193(diagDataList.get(i).get("diagcode").toString());//入院ICD-10编码	
				}else if(i==1){
					InsPubPvOut.setYkc601(diagDataList.get(i).get("diagname").toString());
				}else if(i==2){
					InsPubPvOut.setYkc602(diagDataList.get(i).get("diagname").toString());
				}
			}
			hosInitialData.setStatus(null);//状态标志
			hosInitialData.setBtnUpload("0");
		}else{
			hosInitialData.setPkInsu(InsPubPvOut.getPkInsu());//医保主计划主键
			hosInitialData.setYzz018(InsPubPvOut.getYzz018());//入院病区号
			hosInitialData.setYzz019(InsPubPvOut.getYzz019());//入院病区名称
			hosInitialData.setYkc012(InsPubPvOut.getYkc012());//当前床号
			hosInitialData.setAka130(InsPubPvOut.getAka130());//医疗类别
			hosInitialData.setYkc701(InsPubPvOut.getYkc701());//入院时间
			hosInitialData.setStatus(InsPubPvOut.getStatus().trim());//状态标志
			hosInitialData.setAac043(InsPubPvOut.getAac043());//证件类型
			hosInitialData.setAac044(InsPubPvOut.getAac044());//证件号码
			hosInitialData.setTransid0212(InsPubPvOut.getTransid0212());//就诊登记交易流水号
			hosInitialData.setYab060(InsPubPvOut.getYab060());
			hosInitialData.setAac002(InsPubPvOut.getAac002());
			hosInitialData.setAab301(InsPubPvOut.getAab301());
			hosInitialData.setPkInspvout(InsPubPvOut.getPkInspvout());//异地住院医保登记主键
			hosInitialData.setYkc700(InsPubPvOut.getYkc700());//医保就诊登记号
			hosInitialData.setAae140(InsPubPvOut.getAae140());//险种类型
			hosInitialData.setInsType(InsPubPvOut.getInsType());//医保类型
			hosInitialData.setAae004(InsPubPvOut.getAae004());
			hosInitialData.setAae005(InsPubPvOut.getAae005());
			hosInitialData.setAka130(InsPubPvOut.getAka130());
			
			InsZsPubTransid it = DataBaseHelper.queryForBean("select * from INS_TRANSID where DEL_FLAG = '0' and PK_INSPVOUT = ? and  TRANSID0302 is null", InsZsPubTransid.class, InsPubPvOut.getPkInspvout());
			if(it==null){
				hosInitialData.setBtnUpload("1");
			}else{
				hosInitialData.setBtnUpload("0");
			}
		}
		
		hosInitialData.setAkc050(InsPubPvOut.getAkc050()); //入院诊断
		hosInitialData.setAkc193(InsPubPvOut.getAkc193());//入院ICD-10编码
		hosInitialData.setYkc601(InsPubPvOut.getYkc601());//入院诊断2
		hosInitialData.setYkc602(InsPubPvOut.getYkc602());//入院诊断3
		hosInitialData.setAke022(InsPubPvOut.getAke022());//主诊医师姓名
		return hosInitialData;
	}
	
	
	/**
	 * 异地医保入院登记、取消入院登记、更新登记资料、出院登记、取消出院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsPubPvOut saveInsPubPvOut(String param , IUser user){
		InsZsPubPvOut InsPubPvOut = JsonUtil.readValue(param, InsZsPubPvOut.class);
		try{
			if(DateUtils.getYear(InsPubPvOut.getAae036())==1){
				InsPubPvOut.setAae036(null);
			}
			if(DateUtils.getYear(InsPubPvOut.getAac006())==1){
				InsPubPvOut.setAac006(null);
			}
			if(DateUtils.getYear(InsPubPvOut.getAke014())==1){
				InsPubPvOut.setAke014(null);
			}
			if(DateUtils.getYear(InsPubPvOut.getYkc701())==1){
				InsPubPvOut.setYkc701(null);
			}
			if(DateUtils.getYear(InsPubPvOut.getYkc702())==1){
				InsPubPvOut.setYkc702(null);
			}
			if(DateUtils.getYear(InsPubPvOut.getYkc018())==1){
				InsPubPvOut.setYkc018(null);
			}
			if(InsPubPvOut.getPkInspvout()==null){
				DataBaseHelper.insertBean(InsPubPvOut);
				//updatePkHp(InsPubPvOut.getInsType(),InsPubPvOut.getPkPv());
			}else{
				DataBaseHelper.updateBeanByPk(InsPubPvOut, false);
			}
		}catch(Exception e){
			throw new BusException(e.getMessage());
		}

		InsPubPvOut.setStatus(InsPubPvOut.getStatus().trim());
		return InsPubPvOut;
	}
	
	/**
	 * 修改患者身份信息
	 * @param insType
	 * @param pkPv
	 */
	private void updatePkHp(String insType, String pkPv){
		String insuName = null;
		if(insType.equals("1")){
			insuName = "省内医保";
		}else if(insType.equals("2")){
			insuName = "跨省医保";
		}
		//修改 pv_encounter
		PvEncounter pe = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?", PvEncounter.class, pkPv);
		BdHp bdHp = DataBaseHelper.queryForBean("select * from bd_hp where name = ?", BdHp.class, insuName);
		pe.setPkInsu(bdHp.getPkHp());
		DataBaseHelper.updateBeanByPk(pe, false);
		
		// 修改pv_insurance
		PvInsurance pvInsurance =DataBaseHelper.queryForBean("select * from pv_insurance where pk_pv = ? and flag_maj='1'", PvInsurance.class, pkPv);
		pvInsurance.setPkHp(bdHp.getPkHp());
		DataBaseHelper.updateBeanByPk(pvInsurance, false);
		
		//更新pv_in_notice
		PvIpNotice pvIpNotice = DataBaseHelper.queryForBean("select * from pv_insurance where pk_pv_ip = ? ", PvIpNotice.class, pkPv);
		pvIpNotice.setPkHp(bdHp.getPkHp());
		DataBaseHelper.updateBeanByPk(pvIpNotice, false);
	}
	
	/**
	 * 取消异地医保入院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public void cancelRegister(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String aae011 = jo.getString("aae011");
		String aae036 = jo.getString("aae036");
		String transid = jo.getString("transid");
		String otransid = jo.getString("otransid");
		InsZsPubPvOut pv = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and transid0212 = ?", InsZsPubPvOut.class, otransid);
		pv.setTransid0214(transid);
		pv.setAae011(aae011);
		pv.setAae036(DateUtils.strToDate(aae036));
		pv.setStatus("9");
		pv.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(pv, false);
	}
	
	/**
	 * 获取异地出院登记界面初始数据
	 * @param param
	 * @param user
	 * @return
	 */
	public YdHosInitialData getDischargeRegistrationData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		
		List<Map<String,Object>> drData = insPvMapper.getYdDischargeRegistrationData(param_h);
		if(drData.size()==0){
			throw new BusException("查不到该患者的住院信息，无法获取数据！");
		}
		
		YdHosInitialData yd = new YdHosInitialData();
		
		InsZsPubPvOut InsPubPvOut = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and pk_pv = ?", InsZsPubPvOut.class, pkPv);
		//5:出院登记成功   8:取消入院登记失败
		if(InsPubPvOut==null){
			return yd;
			//throw new BusException("没有医保登记记录！");
		}else if(InsPubPvOut.getStatus().equals("1")||InsPubPvOut.getStatus().equals("3")||InsPubPvOut.getStatus().equals("4")
				||InsPubPvOut.getStatus().equals("6")||InsPubPvOut.getStatus().equals("7")){
			//if(InsPubPvOut.getAkc196()==null){
				List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>>();
				if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
					 diagDataList = insPvMapper.getCyDiagData(param_h);
				}else{
					diagDataList = insPvMapper.getCyDiagData2(param_h);
				}
				for(int i=0; i<diagDataList.size(); i++){
					if(diagDataList.get(i)!=null){
						if(i==0){
							if(diagDataList.get(i).get("diagcode")!=null){
								yd.setAkc196(diagDataList.get(i).get("diagcode").toString());//出院ICD-10编码	
							}
							if(diagDataList.get(i).get("diagname")!=null){
								yd.setAkc185(diagDataList.get(i).get("diagname").toString());
							}else{
								if(diagDataList.get(i).get("diagcode")!=null){
									BdTermDiag bdTermDiag = DataBaseHelper.queryForBean("select * from BD_TERM_DIAG where DIAGCODE = ? and del_flag = '0' ", BdTermDiag.class, diagDataList.get(i).get("diagcode"));
									if(bdTermDiag!=null && bdTermDiag.getDiagname()!=null){
										yd.setAkc185(bdTermDiag.getDiagname());
									}
								}
							}
						}else if(i==1){
							if(diagDataList.get(i).get("diagname")!=null){
								yd.setAkc188(diagDataList.get(i).get("diagname").toString());
							}
						}else if(i==2){
							if(diagDataList.get(i).get("diagname")!=null){
								yd.setAkc189(diagDataList.get(i).get("diagname").toString());
							}
						}else{
							break;
						}
					}
				}
			//}
			yd.setYkc702(drData.get(0).get("date_end")==null?null:DateUtils.strToDate(drData.get(0).get("date_end").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"));
			yd.setYkc683(drData.get(0).get("desc_op")==null?null:drData.get(0).get("desc_op").toString());
			//yd.setYkc195(drData.get(0).get("dt_outcomes")==null?null:drData.get(0).get("dt_outcomes").toString());
			yd.setYkc195("9");//默认为9
			yd.setAkf002(drData.get(0).get("code_insdept")==null?null:drData.get(0).get("code_insdept").toString());
			yd.setYzz088(drData.get(0).get("pk_dept_ns_dis")==null?null:drData.get(0).get("pk_dept_ns_dis").toString());
			yd.setYzz089(drData.get(0).get("name_dept_ns")==null?null:drData.get(0).get("name_dept_ns").toString());
			yd.setYkc016(drData.get(0).get("bed_no")==null?null:drData.get(0).get("bed_no").toString());
			yd.setAke021(drData.get(0).get("name_emp_tre")==null?null:drData.get(0).get("name_emp_tre").toString());
			//这个的科室目前取的his的，到时需要转成医保的
			yd.setNameDept(drData.get(0).get("name_dept")==null?null:drData.get(0).get("name_dept").toString());
			Date dateBegin =DateUtils.strToDate(drData.get(0).get("date_begin").toString().substring(0, 19).replace("-", "").replace(":", "").replace(" ", ""));
			Date dateEnd = drData.get(0).get("date_end")==null?new Date():DateUtils.strToDate(drData.get(0).get("date_end").toString().substring(0, 19).replace("-", "").replace(":", "").replace(" ", ""));
			int dayNumber = DateUtils.getDateSpace(dateBegin, dateEnd);
			yd.setAkb063(dayNumber);
		}else{
			yd.setAkf002(InsPubPvOut.getAkf002());
			yd.setYzz088(InsPubPvOut.getYzz088());
			yd.setYzz089(InsPubPvOut.getYzz089());
			yd.setYkc016(InsPubPvOut.getYkc016());
			yd.setAkc185(InsPubPvOut.getAkc185());
			yd.setAkc196(InsPubPvOut.getAkc196());
			yd.setAkc188(InsPubPvOut.getAkc188());
			yd.setAkc189(InsPubPvOut.getAkc189());
			yd.setAke021(InsPubPvOut.getAke021());
			yd.setYkc195(InsPubPvOut.getYkc195());
			yd.setYkc683(InsPubPvOut.getYkc683());
			yd.setYkc702(InsPubPvOut.getYkc702());
			//目前为空，到时根据编码去科室对照表取
			yd.setNameDept(null);
			yd.setAkb063(InsPubPvOut.getAkb063());
			yd.setTransid0215(InsPubPvOut.getTransid0215());
			yd.setYab060(InsPubPvOut.getYab060());
		}
		yd.setPkInspvout(InsPubPvOut.getPkInspvout());
		yd.setYkc700(InsPubPvOut.getYkc700());
		yd.setAab301(InsPubPvOut.getAab301());
		yd.setAac002(InsPubPvOut.getAac002());
		yd.setAac043(InsPubPvOut.getAac043());
		yd.setAac044(InsPubPvOut.getAac044());
		yd.setInsType(InsPubPvOut.getInsType().trim());
		yd.setStatus(InsPubPvOut.getStatus().trim());

		return yd;
	}
	
	/**
	 * 获取待上传明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<YdChargeDetailsData> getChargeDetails(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		//String dateBegin =  jo.getString("dateBegin");
		String dateEnd = DateUtils.getDateTime();
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		//param_h.put("dateBegin", dateBegin);
		param_h.put("dateEnd", dateEnd);
		
		List<Map<String,Object>> basicDataList = InsPubPvOutMapper.getChargeDetails(param_h);
		List<Map<String,Object>> YpBasicDataList = InsPubPvOutMapper.getChargeDetailsToYp(param_h);
		/*String xmTip = "";
		for(int i=0; i<basicDataList.size(); i++){
			if(basicDataList.get(i).get("xmbh")==null || StringUtils.isEmpty(basicDataList.get(i).get("xmbh").toString())){
				xmTip += basicDataList.get(i).get("name") + "("+basicDataList.get(i).get("code")+")、";
			}
		}
		if(xmTip.length()>0){
			xmTip = xmTip.substring(0, xmTip.length()-1);
			xmTip += "以上诊疗项目未进行医保目录对照，请联系医保科进行对照;";
		}
		
		String ypTip = "";
		for(int i=0; i<YpBasicDataList.size(); i++){
			if(YpBasicDataList.get(i).get("xmbh")==null || StringUtils.isEmpty(YpBasicDataList.get(i).get("xmbh").toString())){
				ypTip += YpBasicDataList.get(i).get("name") + "("+YpBasicDataList.get(i).get("code")+")、";
			}
		}
		if(ypTip.length()>0){
			ypTip = xmTip.substring(0, xmTip.length()-1);
			ypTip += "以上药品未进行医保目录对照，请联系药剂科进行对照;";
		}
		if(xmTip.length()>0 || ypTip.length()>0){
			throw new BusException(xmTip+xmTip);
		}*/
		basicDataList.addAll(YpBasicDataList);
		List<YdChargeDetailsData> ydmx = new ArrayList<YdChargeDetailsData>();
		for(int i=0; i<basicDataList.size(); i++){
			YdChargeDetailsData detailsData = new YdChargeDetailsData();
			detailsData.setAkc220(basicDataList.get(i).get("pres_no")==null?null:basicDataList.get(i).get("pres_no").toString());//处方单据号, 可空
			detailsData.setYkc610(String.valueOf(i+1));//医院费用序列号  累加
			detailsData.setYka111(basicDataList.get(i).get("code_insclass")==null?null:basicDataList.get(i).get("code_insclass").toString());//大类代码（结算项目分类）
			detailsData.setYka112(basicDataList.get(i).get("name_insclass")==null?null:basicDataList.get(i).get("name_insclass").toString());//大类名称
			detailsData.setAke001(basicDataList.get(i).get("xmbh")==null?null:basicDataList.get(i).get("xmbh").toString());//社保三个目录统一编码
			detailsData.setAke002(basicDataList.get(i).get("zwmc")==null?null:basicDataList.get(i).get("zwmc").toString());//社保三个目录名称
			detailsData.setAke114(basicDataList.get(i).get("yjjypbm")==null?null:basicDataList.get(i).get("yjjypbm").toString());//国家药品编码本位码
			detailsData.setAka185(basicDataList.get(i).get("flag_fit")==null?"0":basicDataList.get(i).get("flag_fit").toString());//监控使用标志
			//detailsData.setYke230(yke230);//医用材料的注册证产品名称
			//detailsData.setYke231(yke231);//医用材料的食药监注册号
			detailsData.setAke005(basicDataList.get(i).get("code")==null?null:basicDataList.get(i).get("code").toString());//医疗机构三个目录编码
			detailsData.setAke006(basicDataList.get(i).get("name")==null?null:basicDataList.get(i).get("name").toString());//医疗机构三个目录名称
			detailsData.setAkc226(basicDataList.get(i).get("quan")==null?null:basicDataList.get(i).get("quan").toString());//数量
			detailsData.setAkc225(basicDataList.get(i).get("price")==null?null:new BigDecimal(basicDataList.get(i).get("price").toString()).setScale(4, BigDecimal.ROUND_HALF_UP).toString());//单价
			//String amount = basicDataList.get(i).get("amount")==null?null:basicDataList.get(i).get("amount").toString();
			//Double total = Double.parseDouble(basicDataList.get(i).get("amount").toString())*Double.parseDouble(basicDataList.get(i).get("ratio_disc").toString());
			detailsData.setAkc264(new BigDecimal(basicDataList.get(i).get("amount").toString()).setScale(4, BigDecimal.ROUND_HALF_UP).toString());//医疗费总额
			//detailsData.setYkc611(ykc611);//产地
			//detailsData.setYkc615(ykc615);//特项标志
			//detailsData.setAka074(aka074);//规格
			//detailsData.setAka067(aka067);//药品剂量单位
			//detailsData.setAka070(aka070);//剂型
			//detailsData.setAkc056(akc056);//医师执业证编码
			//detailsData.setAkc273(akc273);//开具医师
			//detailsData.setAae386(aae386);//科室名称
			if(basicDataList.get(i).get("date_pres")==null){
				//处方时间为null的时候用费用发生日期当处方时间
				detailsData.setAkc221(basicDataList.get(i).get("date_hap")==null?DateUtils.getDateTime():basicDataList.get(i).get("date_hap").toString());//处方单据时间
			}else{
				detailsData.setAkc221(basicDataList.get(i).get("date_pres").toString());//处方单据时间
			}
			
			detailsData.setAae011(user.getLoginName());//经办人
			detailsData.setAae036(DateUtils.getDate());//经办时间
			ydmx.add(detailsData);
		}
		return ydmx;
	}

	/**
	 * 保存0301、0302交易流水号
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveTransid(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String transid = jo.getString("transid");
		String otransid = jo.getString("otransid");
		String pkInsPubPvOut = jo.getString("pkInspvout");
		String errorcode = jo.getString("errorcode");
		String errormsg = jo.getString("errormsg");
		
		if(otransid==null || otransid.equals("null")){
			InsZsPubTransid it = new InsZsPubTransid();
			it.setPkPv(pkPv);
			it.setTransid0301(transid);
			it.setPkInspvout(pkInsPubPvOut);
			it.setErrorcode(Integer.parseInt(errorcode));
			it.setErrormsg(errormsg);
			DataBaseHelper.insertBean(it);
		}else{
			String sql = "update INS_TRANSID set TRANSID0302 = ?, ERRORCODE=?, ERRORMSG=? where DEL_FLAG = '0' and PK_INSPVOUT = ? and TRANSID0302 is null";
			DataBaseHelper.execute(sql, transid, errorcode, errormsg, pkInsPubPvOut);
			
/*			InsTransid it = DataBaseHelper.queryForBean("select * from ins_transid where del_flag = '0' and transid0301 = ? and pk_pv = ?", InsTransid.class, otransid, pkPv);
			it.setTransid0302(transid);
			it.setErrorcode(Integer.parseInt(errorcode));
			it.setErrormsg(errormsg);
			DataBaseHelper.updateBeanByPk(it, false);*/
		}
	}
	
	/**
	 * 费用明细回退
	 */
/*	public void cancelUpload(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String transid = jo.getString("transid");
		//String otransid = jo.getString("otransid");
		String pkInsPubPvOut = jo.getString("pkInsPubPvOut");
		String errorcode = jo.getString("errorcode");
		String errormsg = jo.getString("errormsg");
		
		String sql = "update ins_transid set transid0302 = ?";
	}*/
	
	/**
	 * 获取异地医保预结算所需数据
	 * @param param
	 * @param user
	 * @return
	 */
	public YdHosInitialData getPreSettleParam(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String dateBegin =  jo.getString("dateBegin");
		
		//医保未入院，不需要医保结算 1
		//医保未出院，需做上传明细、出院登记 2
		//医保已出院、未结算，需做预结算 3
		//医保已结算，需提示    0
		
		YdHosInitialData yd = new YdHosInitialData();
		
		yd = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and pk_pv = ?", YdHosInitialData.class, pkPv);
		
		if(yd==null){
			//医保未入院
			yd = new YdHosInitialData();
			yd.setPreSettle("1");
			return yd;
		}
		
		if(yd.getStatus().equals("1") || yd.getStatus().equals("3") || yd.getStatus().equals("4")
				||yd.getStatus().equals("6") || yd.getStatus().equals("7") || yd.getStatus().equals("10")){
			//医保未出院，需做上传明细、出院登记
			yd.setPreSettle("2");
		}else if(!(yd.getStatus().equals("5") || yd.getStatus().equals("8") || yd.getStatus().equals("13"))){
			//5：出院登记成功 8：取消出院登记失败 13：取消结算成功    只有这3中状态才可以进行结算
			//当前状态不可以进行结算
			yd.setPreSettle("0");
			return yd;
		}else{
			yd.setPreSettle("3");//需做出院登记
		}

/*		InsTransid it = DataBaseHelper.queryForBean("select * from ins_transid where del_flag = '0' and pk_InsPubPvOut = ? and  transid_0302 is null", InsTransid.class, pv.getPkInsPubPvOut());
		if(it!=null){//不为null就是已经录入明细了，录入明细就必须用医保结算
			
		}*/
		
		//计算总费用
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		param_h.put("dateBegin", dateBegin);
		param_h.put("dateEnd", DateUtils.getDate("yyyy/MM/dd HH:mm:ss"));
		
		List<Map<String,Object>> basicDataList = InsPubPvOutMapper.getChargeDetails(param_h);
		basicDataList.addAll(InsPubPvOutMapper.getChargeDetailsToYp(param_h));
		BigDecimal totalAmount = new BigDecimal(0);  
		for(int i=0; i<basicDataList.size(); i++){
			totalAmount = totalAmount.add(new BigDecimal(basicDataList.get(i).get("amount").toString()).multiply(new BigDecimal(basicDataList.get(i).get("ratio_disc").toString())));  
		}
		yd.setAkc264(totalAmount);
		return yd;
	}
	
	public void saveybDetailed(String param , IUser user){
		InsPubSaveDetailedParam dataParam = JsonUtil.readValue(param, InsPubSaveDetailedParam.class);
		if(dataParam.getJzjlh()!=null){
			String sql = "delete from ins_detailed where jzjlh = ?";
			DataBaseHelper.execute(sql, dataParam.getJzjlh());
			for (InsPubDetailed data : dataParam.getZsMx()) {
				DataBaseHelper.insertBean(data);
			}
		}
	}
	
	/**
	 * 获取医保入院登记资料
	 */
	public InsRegData getInsData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		InsRegData insRegData = new InsRegData();
		String sql = "select b.code, b.name  from PV_ENCOUNTER a inner join bd_hp b on a.PK_INSU = b.pk_hp where pk_pv = ?";
		Map<String, Object>  hp = DataBaseHelper.queryForMap(sql, pkPv);
		if(hp!=null){
			String code = hp.get("code").toString();
			insRegData.setHpCode(code);
			if(code.equals("00023")||code.equals("00024")||code.equals("00025")||code.equals("00022")||code.equals("00021")){
				sql = "select * from ins_pv where pk_pv = ? and DEL_FLAG = '0'";
				HosInitialData hosInitialData = DataBaseHelper.queryForBean(sql, HosInitialData.class, pkPv);
				if(hosInitialData==null){
					insRegData.setCode("0");
					insRegData.setMessage("患者身份为："+hp.get("name").toString()+"，但还未进行医保登记");
				}else{
					insRegData.setHosInitialData(hosInitialData);
					insRegData.setCode("1");
				}
			}else if(code.equals("00031")||code.equals("0004")){
				sql = "select * from ins_pv_out where pk_pv = ? and DEL_FLAG = '0'";
				YdHosInitialData ydHosInitialData = DataBaseHelper.queryForBean(sql, YdHosInitialData.class, pkPv);
				if(ydHosInitialData != null){
					insRegData.setYdHosInitialData(ydHosInitialData);
					insRegData.setCode("2");
				}else{
					insRegData.setCode("0");
					insRegData.setMessage("患者身份为："+hp.get("name").toString()+"，但还未进行医保登记");
				}
			}else if(code.equals("00032")){
				insRegData.setCode("-2");
				insRegData.setMessage(hp.get("name").toString()+"请去收费处重新登记");
			}else{
				insRegData.setCode("0");
				insRegData.setMessage(hp.get("name").toString()+"不是医保身份");
			}
		}else{
			insRegData.setCode("-1");
			insRegData.setMessage("没有查到患者就诊信息");
		}
		return insRegData;
	}

	
	/**
	 * 功能号：022003004073
	 * 获取医保登记资料(取消(医院)入院的判断，如果有医保登记信息，不允许取消入院)
	 */
	public InsRegData getInsDataBy(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		InsRegData insRegData = new InsRegData();
		String sql = "select b.code, b.name  from PV_ENCOUNTER a inner join bd_hp b on a.PK_INSU = b.pk_hp where pk_pv = ?";
		Map<String, Object>  hp = DataBaseHelper.queryForMap(sql, pkPv);
		if(hp!=null){
			String code = hp.get("code").toString();
			insRegData.setHpCode(code);
			if(code.equals("00023")||code.equals("00024")||code.equals("00025")||code.equals("00022")||code.equals("00021")){
				sql = "select * from ins_pv where pk_pv = ? and DEL_FLAG = '0'";
				HosInitialData hosInitialData = DataBaseHelper.queryForBean(sql, HosInitialData.class, pkPv);
				if(hosInitialData!=null){
					insRegData.setCode("1");
					insRegData.setMessage("该病人有中山医保信息，请先去'医保维护'界面取消医保入院登记");
				}else{
					insRegData.setCode("0");
					insRegData.setMessage("没有医保登记信息");
				}
			}else if(code.equals("00031")||code.equals("0004")){
				sql = "select * from ins_pv_out where pk_pv = ? and DEL_FLAG = '0'";
				YdHosInitialData ydHosInitialData = DataBaseHelper.queryForBean(sql, YdHosInitialData.class, pkPv);
				if(ydHosInitialData != null){
					insRegData.setMessage("该病人有省内或跨省医保信息，请先去'医保维护'界面取消医保入院登记");
					insRegData.setCode("2");
				}else{
					insRegData.setCode("0");
					insRegData.setMessage("没有医保登记信息");
				}
			}else if(code.equals("00032")){
				sql = "select * from ins_pv_wi where pk_pv = ? and DEL_FLAG = '0'";
				InsPvWi insPvWi = DataBaseHelper.queryForBean(sql, InsPvWi.class, pkPv);
				if(insPvWi!=null){
					insRegData.setMessage("该病人有省内工伤医保信息，请先去'医保维护'界面取消医保入院登记");
					insRegData.setCode("3");
				}else{
					insRegData.setCode("0");
					insRegData.setMessage("没有医保登记信息");
				}	
			}else if(code.substring(0, 4).equals("0005")){
				sql = "select * from ins_pv_qg where pk_pv = ? and DEL_FLAG = '0'";
				InsZsbaPvQg insPvQg = DataBaseHelper.queryForBean(sql, InsZsbaPvQg.class, pkPv);
				if(insPvQg!=null){
					insRegData.setMessage("该病人有全国医保信息，请先去'医保维护'界面取消医保入院登记");
					insRegData.setCode("3");
				}else{
					insRegData.setCode("0");
					insRegData.setMessage("没有医保登记信息");
				}	
			}else{
				insRegData.setCode("0");
				insRegData.setMessage(hp.get("name").toString()+"不是医保身份");
			}
		}else{
			insRegData.setCode("-1");
			insRegData.setMessage("没有查到患者就诊信息");
		}
		return insRegData;
	}

}