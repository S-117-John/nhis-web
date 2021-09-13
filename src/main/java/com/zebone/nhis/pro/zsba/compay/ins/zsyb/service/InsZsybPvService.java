package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.util.ArrayList;
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
import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.HosInitialData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbBear;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbInfo;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbOptDay;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbOptPb;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.TheHospital;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybPvService {
	
	@Autowired
	private InsZsybPvMapper insPvMapper;
	
	/**
	 * 根据住院号模糊查询住院信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPvInfo(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String codeIp = jo.getString("codeIp");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.pk_pv, t3.CODE_IP as code, t3.NAME_PI as name, t3.code_ip as py_code,t3.code_ip as d_code, t3.code_ip,t3.name_pi,  t2.IP_TIMES, t4.NAME as hp_name, t1.date_end, t5.NAME_DEPT, t1.flag_in ");
		sql.append("from PV_ENCOUNTER t1 INNER JOIN PV_IP t2 on t1.pk_pv = t2.PK_PV ");
		sql.append("INNER JOIN PI_MASTER t3 on t1.PK_PI = t3.PK_PI ");
		sql.append("INNER JOIN BD_HP t4 on t1.PK_INSU = t4.PK_HP ");
		sql.append("LEFT JOIN BD_OU_DEPT t5 on t2.PK_DEPT_DIS = t5.PK_DEPT ");
		sql.append("where t3.CODE_IP like ? and t1.DEL_FLAG = '0' order by t2.IP_TIMES asc");
		
		codeIp = codeIp+"%";
		
		List<Map<String,Object>> mapList = DataBaseHelper.queryForList(sql.toString(), codeIp);
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		for(int i=0; i<mapList.size(); i++){
			Map<String, Object> map =mapList.get(i);
			if(map.get("dateEnd")!=null&&map.get("flagIn").toString().length()!=0){
				map.put("dateEnd", map.get("dateEnd").toString().subSequence(0, 10));
			}
			if(map.get("flagIn").equals("0")){
				map.put("flagIn", "出院");
			}else {
				map.put("flagIn", "在院");
			}
			returnList.add(map);
		}
		return returnList;
	}
	
	/**
	 * 获取医保入院登记界面初始化数据（使用医保使用统一接口，这个应该没用了）
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
		
		//身份证
		if(basicDataList.get(0).get("dt_idtype").equals("01")){
			hosInitialData.setGmsfhm(basicDataList.get(0).get("id_no")!=null?basicDataList.get(0).get("id_no").toString():null);
		}else{
			hosInitialData.setGmsfhm("");
		}
		hosInitialData.setGrsxh(basicDataList.get(0).get("insur_no")!=null?basicDataList.get(0).get("insur_no").toString():null);//参保号
		hosInitialData.setPkHp(null);//医保主计划
		hosInitialData.setRyrq(basicDataList.get(0).get("date_begin")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_begin").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//入院时间
		hosInitialData.setCyrq(basicDataList.get(0).get("date_end")!=null?DateUtils.strToDate(basicDataList.get(0).get("date_end").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"):null);//出院时间
		//returnMap.put("pkEmpTre", basicDataList.get(0).get("pk_emp_tre")); //收治医生主键
		//returnMap.put("nameEmpTre", basicDataList.get(0).get("name_emp_tre")); //收治医生姓名
		
		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_pv = ?", InsZsBaYbPv.class, pkPv);
		if(insPv==null){
			insPv = new InsZsBaYbPv();
			insPv.setZzysxm(basicDataList.get(0).get("name_emp_tre")!=null?basicDataList.get(0).get("name_emp_tre").toString():null);
			insPv.setRyqk(basicDataList.get(0).get("dt_outcomes")!=null?basicDataList.get(0).get("dt_outcomes").toString():null);
			param_h = new HashMap<String,Object>();
			param_h.put("pkPv", pkPv);
			List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>>();
			if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
				 diagDataList = insPvMapper.getDiagData(param_h);
			}else{
				diagDataList = insPvMapper.getDiagData2(param_h);
			}
			for(int i=0; i<diagDataList.size(); i++){
				if(i==0){
					insPv.setRyzd(diagDataList.get(i).get("diagname").toString());
					insPv.setRyzdgjdm(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码	
				}else if(i==1){
					insPv.setRyzd2(diagDataList.get(i).get("diagname").toString());
					hosInitialData.setRyzdgjdm2(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码2
				}else if(i==2){
					insPv.setRyzd3(diagDataList.get(i).get("diagname").toString());
					hosInitialData.setRyzdgjdm3(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码3	
				}else if(i==3){
					insPv.setRyzd4(diagDataList.get(i).get("diagname").toString());
					hosInitialData.setRyzdgjdm4(diagDataList.get(i).get("diagcode").toString().trim());//入院ICD-10编码4	
				}
			}
			hosInitialData.setStatus(null);//状态标志
			
			String sql = "select pk_insu from pv_encounter where del_flag='0' and pk_pv=?";
			Map<String, Object> data = DataBaseHelper.queryForMap(sql, pkPv);
			if(!data.isEmpty() && data.containsKey("pkInsu")){
				hosInitialData.setPkInsu(data.get("pkInsu").toString());//nhis-医保计划主键 ，要从院内入院信息中查出
			}
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
		
		return hosInitialData;
	}
	
	/**
	 * 医保入院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbPv saveInsPv(String param , IUser user){
		InsZsBaYbPv insPv = JsonUtil.readValue(param, InsZsBaYbPv.class);
		if(insPv.getPkInsu()==null){
			throw new BusException("医保主计划不能为空！");
		}
		if(insPv.getPkInspv()==null){
			InsZsBaYbPv insPv2 = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_pv = ?", InsZsBaYbPv.class, insPv.getPkPv());
			if(insPv2!=null){
				throw new BusException("该患者已入院登记，不能再次登记！");
			}
			insPv.setYybh("H003");
			insPv.setEuPvtype("3");
			DataBaseHelper.insertBean(insPv);
			
			InsZsBaYbInfo insInfo = DataBaseHelper.queryForBean("select * from ins_info where del_flag = '0' and pk_pv = ?", InsZsBaYbInfo.class, insPv.getPkPv());
			insInfo.setPkInspv(insPv.getPkInspv());
			DataBaseHelper.updateBeanByPk(insInfo, false);
			
			//修改患者身份
			//updatePkHp(insPv.getPkHp(), insPv.getPkPv());
		}else{
			DataBaseHelper.updateBeanByPk(insPv, false);
		}
		//获取入院诊断2、3、4的icd编码
		BdTermDiag  diag2 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getRyzd2());
		BdTermDiag  diag3 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getRyzd3());
		BdTermDiag  diag4 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getRyzd4());
		
		insPv.setRyzdgjdm2(diag2==null?null:diag2.getDiagcode());
		insPv.setRyzdgjdm3(diag3==null?null:diag3.getDiagcode());
		insPv.setRyzdgjdm4(diag4==null?null:diag4.getDiagcode());
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", insPv.getPkPv());
		//获取普通入院登记信息
		List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
		insPv.setIpTimes(basicDataList.get(0).get("ip_times")!=null?basicDataList.get(0).get("ip_times").toString():null);//住院次数 
		insPv.setNamePi(basicDataList.get(0).get("name_pi")!=null?basicDataList.get(0).get("name_pi").toString():null);//患者姓名
		insPv.setDtSex(basicDataList.get(0).get("dt_sex")!=null?basicDataList.get(0).get("dt_sex").toString():null);//性别编码
		insPv.setBirthDate(basicDataList.get(0).get("birth_date")!=null?basicDataList.get(0).get("birth_date").toString():null);//出生日期
		insPv.setNameDept(basicDataList.get(0).get("name_dept")!=null?basicDataList.get(0).get("name_dept").toString():null);//入院科室名称
		return insPv;
	}
	
	/**
	 * 修改患者身份
	 * @param pkHp：医保身份编码
	 * @param pkPv：患者住院主键
	 */
	private void updatePkHp(String pkHp, String pkPv){
		String insuName = null;
		if(pkHp.equals("21")){//医疗住院
			insuName = "中山普通医保";
		}else if(pkHp.equals("31")){//外伤住院
			insuName = "中山工伤医保";
		}else if(pkHp.equals("33")){//工伤康复住院
			//暂时还没有
		}else if(pkHp.equals("41")){//生育住院
			insuName = "中山生育医保";
		}else if(pkHp.equals("47")){//计生手术住院
			insuName = "中山计生手术医保";
		}else if(pkHp.equals("2A")){//日间手术住院
			insuName = "中山日间手术医保";
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
	 * 修改医保入院登记资料
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbPv updateInsPv(String param , IUser user){
		InsZsBaYbPv insPv = JsonUtil.readValue(param, InsZsBaYbPv.class);
		if(insPv.getPkInspv()==null){
			throw new BusException("主键不能为空！");
		}else{
			DataBaseHelper.updateBeanByPk(insPv, false);
		}
		return insPv;
	}
	
	/**
	 * 取消入院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public void cancelRegister(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkInspv = jo.getString("pkInspv");
		String name = jo.getString("name");
		String status = jo.getString("status");
		String fhz = jo.getString("fhz");
		String msg = jo.getString("msg");
		InsZsBaYbPv insPv = new InsZsBaYbPv();
		insPv.setPkInspv(pkInspv);
		insPv.setStatus(status);
		insPv.setFhz(fhz);
		insPv.setMsg(msg);
		if(insPv.getStatus().equals("9")){
			insPv.setJzjlh("");
			insPv.setPkHp("");
			insPv.setDelFlag("1");
		}
		DataBaseHelper.updateBeanByPk(insPv, false);
		//根据pk_hp判断是否是生育、计生，是的话修改资料
		int a=name.indexOf("41");//生育
		if(a>=0){
			InsZsBaYbBear insBear = DataBaseHelper.queryForBean("select * from ins_bear where del_flag = '0' and pk_inspv = ?", InsZsBaYbBear.class, pkInspv);
			if(insBear!=null){
				insBear.setJzjlh("");
				insBear.setDelFlag("");
				DataBaseHelper.updateBeanByPk(insBear, false);
			}
		}
		a=name.indexOf("47");//计生
		if(a>=0){
			InsZsBaYbOptPb insOptPb = DataBaseHelper.queryForBean("select * from ins_opt_pb where del_flag = '0' and pk_inspv = ?", InsZsBaYbOptPb.class, pkInspv);
			if(insOptPb!=null){
				insOptPb.setJzjlh("");
				insOptPb.setDelFlag("");
				DataBaseHelper.updateBeanByPk(insOptPb, false);
			}
		}
	}
	
	/**
	 * 获取出院登记界面初始数据
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsBaYbPv getDischargeRegistrationData(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		
		TheHospital insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_pv = ?", TheHospital.class, pkPv);
		
		if(insPv==null){
			return insPv;
			//throw new BusException("参数错误，查不到医保入院信息！");
		}
		
		//5:出院登记成功   8:取消出院登记失败
		if(insPv.getStatus().trim().equals("5")||insPv.getStatus().trim().equals("8")||Integer.parseInt(insPv.getStatus().trim())>=11){
			return insPv;
		}
			
		if(insPv.getCyzd()==null){
//			insPv = new TheHospital();
			List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>> ();
			if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
				 diagDataList = insPvMapper.getCyDiagData(param_h);
			}else{
				diagDataList = insPvMapper.getCyDiagData2(param_h);
			}
			for(int i=0; i<diagDataList.size(); i++){
				if(diagDataList.get(i)!=null){
					if(i==0){
						if(diagDataList.get(i).get("diagname")!=null){
							insPv.setCyzd(diagDataList.get(i).get("diagname").toString());
						}
						if(diagDataList.get(i).get("diagcode")!=null){
							insPv.setCyzdgjdm(diagDataList.get(i).get("diagcode").toString());//出院ICD-10编码	
						}
					}else if(i==1){
						if(diagDataList.get(i).get("diagname")!=null){
							insPv.setCyzd2(diagDataList.get(i).get("diagname").toString());
						}
						if(diagDataList.get(i).get("diagcode")!=null){
							insPv.setCyzdgjdm2(diagDataList.get(i).get("diagcode").toString());//出院ICD-10编码	
						}
					}else if(i==2){
						if(diagDataList.get(i).get("diagname")!=null){
							insPv.setCyzd3(diagDataList.get(i).get("diagname").toString());
						}
						if(diagDataList.get(i).get("diagcode")!=null){
							insPv.setCyzdgjdm3(diagDataList.get(i).get("diagcode").toString());//出院ICD-10编码	
						}
					}else if(i==3){
						if(diagDataList.get(i).get("diagname")!=null){
							insPv.setCyzd4(diagDataList.get(i).get("diagname").toString());
						}
						if(diagDataList.get(i).get("diagcode")!=null){
							insPv.setCyzdgjdm4(diagDataList.get(i).get("diagcode").toString());//出院ICD-10编码	
						}
					}else{
						break;
					}
				}
			}
		}else{
			BdTermDiag  diag2 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getCyzd2());
			BdTermDiag  diag3 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getCyzd3());
			BdTermDiag  diag4 = DataBaseHelper.queryForBean("select * from bd_term_diag where del_flag = '0' and diagname = ?", BdTermDiag.class, insPv.getCyzd4());
			
			insPv.setCyzdgjdm2(diag2==null?null:diag2.getDiagcode());
			insPv.setCyzdgjdm3(diag3==null?null:diag3.getDiagcode());
			insPv.setCyzdgjdm4(diag4==null?null:diag4.getDiagcode());
		}
		
		//日间手术取维护的诊断
		InsZsBaYbOptDay insDay = DataBaseHelper.queryForBean("select * from ins_opt_day where del_flag = '0' and pk_inspv = ?", InsZsBaYbOptDay.class, insPv.getPkInspv());
		if(insDay!=null){
			insPv.setCyzdgjdm(insDay.getCyzdgjdm());
			insPv.setCyzd(insDay.getCyzd());
		}
		
		List<Map<String,Object>> drData = insPvMapper.getDischargeRegistrationData(param_h);
		if(drData.size()==0){
			throw new BusException("查不到该患者的住院信息，无法获取数据！");
		}
		insPv.setPkInspv(drData.get(0).get("pk_inspv").toString());
		insPv.setJzjlh(drData.get(0).get("JZJLH").toString());
		String cyrq = null;
		if(drData.get(0).get("date_end")!=null){
			cyrq = drData.get(0).get("date_end").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14);
		}
		insPv.setCyrq(cyrq==null?null:DateUtils.strToDate(cyrq, "yyyyMMddHHmmss"));
		insPv.setCyqk(drData.get(0).get("dt_outcomes")==null?null:drData.get(0).get("dt_outcomes").toString());
		String ryqk = drData.get(0).get("dt_level_dise")==null?null:drData.get(0).get("dt_level_dise").toString();
		if(ryqk!=null){
			if(ryqk.equals("00")){
				ryqk = "1";
			}else if((ryqk.equals("01"))){
				ryqk = "2";
			}else if((ryqk.equals("02"))){
				ryqk = "3";
			}else if((ryqk.equals("03"))){
				//03为其他，医保没有医保，暂时当3（医保：一般）来用
				ryqk = "3";
			}
		}
		insPv.setRyqk(ryqk);
		insPv.setSsmc(drData.get(0).get("desc_op")==null?null:drData.get(0).get("desc_op").toString());
		insPv.setTzcysj(drData.get(0).get("date_notice")==null?null:DateUtils.strToDate(drData.get(0).get("date_notice").toString().replace("-", "").replace(":", "").replace(" ", "").replace(".", "").substring(0, 14), "yyyyMMddHHmmss"));
		insPv.setJsffbz(drData.get(0).get("JSFFBZ")==null?null:drData.get(0).get("JSFFBZ").toString());
		insPv.setSylb(drData.get(0).get("SYLB")==null?null:drData.get(0).get("SYLB").toString());
		insPv.setZszh(drData.get(0).get("ZSZH")==null?null:drData.get(0).get("ZSZH").toString());
		
		return insPv;
	}
	
	/**
	 * 医保出院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveDischargeRegistration(String param , IUser user){
		InsZsBaYbPv insPv = JsonUtil.readValue(param, InsZsBaYbPv.class); //用这个方法接受参数，不知道为什么，Date的字段会少一年
		JSONObject jo = JSONObject.fromObject(param);
		String cyrq = jo.getString("cyrq");
		String tzcysj = jo.getString("tzcysj");
		insPv.setCyrq(DateUtils.strToDate(cyrq, "yyyy-MM-dd HH:mm:ss"));
		if(tzcysj==null||tzcysj.equals("null")){
			insPv.setTzcysj(null);
		}else{
			insPv.setTzcysj(DateUtils.strToDate(tzcysj, "yyyy-MM-dd HH:mm:ss"));
		}
		DataBaseHelper.updateBeanByPk(insPv, false);
	}
	
	/**
	 * 取消医保出院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public void cancelDischargeRegistration(String param , IUser user){
		InsZsBaYbPv insPv = JsonUtil.readValue(param, InsZsBaYbPv.class);
		DataBaseHelper.updateBeanByPk(insPv, false);
	}
	
	
}