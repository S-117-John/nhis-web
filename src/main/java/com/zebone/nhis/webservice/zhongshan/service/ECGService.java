package com.zebone.nhis.webservice.zhongshan.service;

import com.zebone.nhis.bl.pub.service.BlIpMedicalExeService;
import com.zebone.nhis.bl.pub.vo.MedExeIpParam;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.zhongshan.dao.MedMapper;
import com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecord;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ECGService {

	@Resource
	private MedMapper medMapper;//查询mapper
	@Resource
	private  RISService risService;//pacs实现类
	@Resource
	private BlIpMedicalExeService blIpMedExSer;//医技记费

	
	/**
	 * 1.1 查询 其他数据库中的数据
	 * @param param
	 * @return
	 */
	public List<PacsApplyRecord> queryAppList(Map<String, Object> param){
		List<PacsApplyRecord> list = new ArrayList<PacsApplyRecord>();// 查询的申请单列表结果集
		list = medMapper.queryAppList(param);
		return list;
	}
	
	/**
	 * 1.2 查询【住院】NHIS库中的数据
	 * @param param
	 * @return
	 */
	public List<PacsApplyRecord> queryIpEcgAppListFromNHIS(Map<String, Object> param){
		List<PacsApplyRecord> list = new ArrayList<PacsApplyRecord>();// 查询的申请单列表结果集
		list = medMapper.queryIpEcgAppListFromNHIS(param);
		return list;
	}
	
	/**
	 * 2.1 根据传入的 operType判断更新其他库的字段
	 * @param operType
	 * @return sql
	 */
	public int updatePacsApp(Map<String, Object> param) {
		String operType = param.get("ope_type").toString();
		String sql = "update pacs_apply_record set ";
		switch (operType) {
		case "1":
			sql += " accept_flag = '1' ";// ope_type=1(更新接收) => accept_flag=1
			break;
		case "2"://登记状态，目前未处理
			return 1;
		case "3":
			sql += " report_flag = '1' ";// ope_type=3(更新报告) => report_flag=1
			break;
		case "4":
			sql += " report_flag = '2' ";// ope_type=4(报告撤销) => report_flag=2
			break;
		case "9":
			sql += " accept_flag = '0' ";// ope_type=9(撤销登记) => accept_flag=0
			break;
		default:
			break;
		}
		
		sql += " where 1 = 1 and patient_id is not null";
		if(CommonUtils.isNotNull(param.get("record_sn")) && !CommonUtils.isEmptyString(param.get("record_sn").toString())){
			sql += " and record_sn =:record_sn";
		}
		if(CommonUtils.isNotNull(param.get("uid")) && !CommonUtils.isEmptyString(param.get("uid").toString())){
			sql += " and uid =:uid";
		}
		return DataBaseHelper.update(sql,param);
	}

	/**
	 * 2.2 更新NHIS 数据库的申请单的状态
	 * @param param
	 * @return
	 */
	public int updateNhisIpApp(Map<String, Object> param){
		
		//1.设置当前操作患者相关信息
		BdOuDept dept = DataBaseHelper.queryForBean(" select * from bd_ou_dept where code_dept = ? ", BdOuDept.class, param.get("exec_dept"));
		if(dept == null) 
			throw new BusException(" 未查询到编码为["+param.get("exec_dept")+"]的执行科室，登记失败！");
		
		BdOuEmployee emp = DataBaseHelper.queryForBean(" select * from bd_ou_employee where code_emp = ? ", BdOuEmployee.class, param.get("opera"));
		if(emp == null)
			throw new BusException(" 未查询编码为["+param.get("opera")+"]的操作人，登记失败！");
				
		User user = new User();
		user.setPkDept(dept.getPkDept());
		user.setPkOrg(emp.getPkOrg());
		user.setNameEmp(emp.getNameEmp());
		user.setPkEmp(emp.getPkEmp());
		user.setCodeEmp(emp.getCodeEmp());
		UserContext.setUser(user);
		
		//2.根据操作编码，更新cn_ris_apply 的状态 eu_status
		String opeType = param.get("ope_type").toString();
		
		String appTpe = "";
		String appName = "";
		switch (opeType) {
		case "1"://接收状态，目前未处理
			return 1;
		case "2":
			param.put("euStatus", "3" );// ope_type=1(登记 + 记费) => eu_status=3
			//ipEx(param,user);//医技执行 + 记费
			risService.ipEx(param,user);
			appTpe = "P1";
			appName = "登记";
			break;
		case "3":
			param.put("euStatus", "4");// ope_type=3(更新报告) => eu_status=4
			saveRisResult(param);//更新报告结果
			appTpe = "P5";
			appName = "审核报告";
			break;
		case "4":
			param.put("euStatus", "3");// ope_type=4(报告撤销) => eu_status=3
			cancelRisResult(param);//清空报告结果
			appTpe = "P9";
			appName = "取消报告";
			break;
		case "9":
			param.put("euStatus", "1");// ope_type=6(取消登记  + 退费),仅登记状态可撤销 => eu_status=1
			risService.cancelIpExTem(param, user);// 取消执行 + 退费+把执行单修改为未执行状态
			appTpe = "P3";
			appName = "取消登记";
			break;
		default:
			break;
		}
		
		//插入操作日志
		risService.addOpeRecord(param,user,"ECG",appTpe,appName,"3");
		
		int resultCount = DataBaseHelper.update("update cn_ris_apply set eu_status =:euStatus where pk_cnord =:pkCnord  ",param);
		return resultCount;
	}
	
	/**
	 * 更新/插入 检查结果
	 */
	private void saveRisResult(Map<String, Object> param){
		
		//1.校验操作时间是否有误
		String dateOcc = param.get("ope_time").toString();
		Date dateEx = new Date();
		try {
			dateEx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateOcc);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new BusException(" 操作时间格式不对，登记失败！");
		}
		
		//2.查询 医嘱
		CnOrder ord = DataBaseHelper.queryForBean(" select * from cn_order where pk_cnord = ? ", CnOrder.class, param.get("pkCnord"));
		if(ord == null) 
			throw new BusException(" 未查询到相应医嘱，更新结果失败！");
		
		if(param.get("rpt_id")==null||param.get("rpt_id").equals("")){
			throw new BusException("报告审核时，报告单号不能为空！");
		}
		//3.查询 检查结果
		ExRisOcc exRisOcc = DataBaseHelper.queryForBean(" select * from ex_ris_occ where pk_cnord = ? and del_flag = '0' ", ExRisOcc.class, param.get("pkCnord"));
		if(exRisOcc != null) {
			param.put("pkEmp", UserContext.getUser().getPkEmp());
			param.put("curDate", new Date());
			DataBaseHelper.update("update ex_ris_occ set result_sub =:report_info,code_rpt=:rpt_id ,flag_chk = '1'"
					+ ", modifier=:pkEmp,modity_time=:curDate,TS =:curDate"
					+ " where pk_cnord =:pkCnord", param);
		}else{
		    exRisOcc = new ExRisOcc();
			exRisOcc.setCodeApply(ord.getCodeApply());
			exRisOcc.setDateChk(dateEx);
			exRisOcc.setDateOcc(dateEx);
			exRisOcc.setDateRpt(dateEx);
			exRisOcc.setEuResult("0");
			exRisOcc.setFlagChk("1");
			exRisOcc.setDelFlag("0");
			exRisOcc.setNameEmpChk(UserContext.getUser().getNameEmp());
			exRisOcc.setNameEmpOcc(UserContext.getUser().getNameEmp());
			exRisOcc.setNameOrd(ord.getNameOrd());
			exRisOcc.setPkCnord(ord.getPkCnord());
			exRisOcc.setPkDeptOcc(UserContext.getUser().getPkDept());
			exRisOcc.setPkEmpChk(UserContext.getUser().getPkEmp());
			exRisOcc.setPkEmpOcc(UserContext.getUser().getPkEmp());
			exRisOcc.setPkOrg(UserContext.getUser().getPkOrg());
			exRisOcc.setPkOrgOcc(UserContext.getUser().getPkOrg());
			exRisOcc.setPkPi(ord.getPkPi());
			exRisOcc.setPkPv(ord.getPkPv());
			exRisOcc.setResultSub(param.get("report_info")==null?null:param.get("report_info").toString());
			//申请单号
			exRisOcc.setCodeApply(param.get("uid")==null?null:param.get("uid").toString());
			//报告单号
			exRisOcc.setCodeRpt(param.get("rpt_id")==null?null:param.get("rpt_id").toString());
			DataBaseHelper.insertBean(exRisOcc);
		}
	}
	
	/**
	 * 清空检查结果
	 * @param param
	 */
	private void cancelRisResult(Map<String, Object> param){
		ExRisOcc exRisOcc = DataBaseHelper.queryForBean(" select * from ex_ris_occ where pk_cnord = ? ", ExRisOcc.class, param.get("pkCnord"));
		if(exRisOcc == null) 
			return;
		else{
			param.put("pkEmp", UserContext.getUser().getPkEmp());
			param.put("curDate", new Date());
			DataBaseHelper.update("update ex_ris_occ set result_sub = null,code_rpt = null,flag_chk = '0' "
					+ " ,modifier=:pkEmp,modity_time=:curDate,TS =:curDate where pk_cnord =:pkCnord " , param);
		}
	}
	
	/**
	 * 医技执行
	 * 根据医嘱主键，组装记费所需VO ，调用 记费（更新执行单 + 记费）
	 * @param param
	 */
	private void ipEx(Map<String, Object> param ,User user){
		
		//1.校验操作时间是否有误
		String dateOcc = param.get("ope_time").toString();
		Date dateEx = new Date();
		try {
			dateEx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateOcc);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new BusException(" 操作时间格式不对，登记失败！");
		}
		
		//2.查询 医嘱
		CnOrder ord = DataBaseHelper.queryForBean(" select * from cn_order where pk_cnord = ? ", CnOrder.class, param.get("pkCnord"));
		if(ord == null) 
			throw new BusException(" 未查询到相应医嘱，登记失败！");
		
		//3.查询 执行单
		ExOrderOcc exOrdOcc = DataBaseHelper.queryForBean(" select * from ex_order_occ where pk_cnord = ? ", ExOrderOcc.class, param.get("pkCnord"));
		if(exOrdOcc == null) 
			throw new BusException(" 未查询到相应执行单，登记失败！");
		
		//4. 校验是否 已执行/取消执行
		List<ExAssistOcc> exAssOccs = DataBaseHelper.queryForList(" select * from ex_assist_occ where pk_cnord = ? ", ExAssistOcc.class, param.get("pkCnord"));
		if(exAssOccs != null && exAssOccs.size() > 0) 
			throw new BusException(" 该医嘱已经执行，登记失败！");
		
		//5.拼装记费所需参数
		MedExeIpParam med = new MedExeIpParam();
		med.setExOrdOcc(exOrdOcc);//执行单数据
		
		med.setPkCnord(ord.getPkCnord());
		med.setPkOrg(ord.getPkOrg());
		med.setPkDeptApp(ord.getPkOrg());
		med.setPkPv(ord.getPkPv());
		med.setPkPi(ord.getPkPi());
		med.setOrdsnParent(ord.getOrdsnParent());
		med.setPkOrgApp(ord.getPkOrg());
		med.setPkDeptApp(ord.getPkDept());
		med.setPkEmpApp(ord.getPkEmpOrd());
		med.setNameEmpApp(ord.getNameEmpOrd());
		med.setInfantNo(ord.getInfantNo()+"");
		
		med.setPkOrdexdt(exOrdOcc.getPkExocc());
		med.setQuanCg(exOrdOcc.getQuanOcc());
		med.setPkOrgEx(exOrdOcc.getPkOrgOcc());
		med.setPkDeptEx(exOrdOcc.getPkDeptOcc());
		med.setDateOcc(dateEx);
	
		med.setFlagPd("0");
		med.setFlagPv("0");
		med.setEuPvType("3");
		med.setDateHap(new Date());
		
		List<MedExeIpParam> params = new ArrayList<MedExeIpParam>();
		params.add(med);
		
		//6.执行记费(并处理执行单相关记录)
		blIpMedExSer.ipExe4Inner(params, user.getPkOrg());
	}

	/**
	 * 取消医技执行
	 * 根据医嘱主键，查询医技执行记录、执行单，调用 退费（更新执行记录，执行单  + 记费）
	 * @param param
	 * @param user
	 */
	private void cancelIpEx(Map<String, Object> param , User user){
		
		//1.查询 需退费的执行记录
		ExAssistOcc exAssOcc = DataBaseHelper.queryForBean(" select * from ex_assist_occ where pk_cnord = ? and flag_canc = '0' ", ExAssistOcc.class, param.get("pkCnord"));
		if(exAssOcc == null) 
			throw new BusException(" 未查询到可撤销的医技执行记录，撤销登记失败！");
		
		//2.查询相应执行单
		ExOrderOcc exOrdOcc = DataBaseHelper.queryForBean(" select * from ex_order_occ where pk_cnord = ? ", ExOrderOcc.class, param.get("pkCnord"));
		if(exOrdOcc == null) 
			throw new BusException(" 未查询到相应执行单，撤销登记失败！");
		
		//3.更新医技执行记录
		Object[] args = new Object[4];
		args[0] = new Date();
		args[1] = UserContext.getUser().getPkEmp();
		args[2] = UserContext.getUser().getNameEmp();
		args[3] = exAssOcc.getPkAssocc();
		DataBaseHelper.execute("update ex_assist_occ  set flag_canc=1,date_canc=?,"+
			       "pk_emp_canc=?,name_emp_canc=?,eu_status=9"+
			" where pk_assocc=? and flag_canc=0", args);
		
		//4.退费
		blIpMedExSer.retCg(exOrdOcc.getPkExocc(), user,args,"0");
	}
	
	/**
	 * @Description 查询患者是否有门诊有效就诊记录
	 * @auther wuqiang
	 * @Date 2021-02-25
	 * @Param [paramMap]
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> getPkPvByOp(Map<String,Object> paramMap){
		return medMapper.getPkPvByOp(paramMap);
	}
	
	/**
	 * @Description 查询患者是否有住院有效就诊记录
	 * @auther wuqiang
	 * @Date 2021-02-25
	 * @Param [paramMap]
	 * @return java.util.List<java.lang.String>
	 */
	public   List<String> getPkPvByIp(Map<String,Object> paramMap){
		return medMapper.getPkPvByIp(paramMap);
	}
	
    /**
     * @Description 查询【门诊】NHIS库中的数据
     * @auther wuqiang
     * @Date 2021-02-25
     * @Param [param]
     * @return java.util.List<com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecord>
     */
	public List<PacsApplyRecord> queryOpEcgAppListFromNHIS(Map<String, Object> param) {
		return medMapper.queryOpEcgAppListFromNHIS(param);
	}


	/**
	 * 2.2 门诊 -更新NHIS 数据库的申请单的状态
	 * @param param
	 * @return
	 */
	public int updateNhisOpApp(Map<String, Object> param){

		//1.设置当前操作患者相关信息
		BdOuDept dept = DataBaseHelper.queryForBean(" select * from bd_ou_dept where code_dept = ? ", BdOuDept.class, param.get("exec_dept"));
		if(dept == null)
			throw new BusException(" 未查询到编码为["+param.get("exec_dept")+"]的执行科室，登记失败！");

		BdOuEmployee emp = DataBaseHelper.queryForBean(" select * from bd_ou_employee where code_emp = ? ", BdOuEmployee.class, param.get("opera"));
		if(emp == null)
			throw new BusException(" 未查询编码为["+param.get("opera")+"]的操作人，登记失败！");

		User user = new User();
		user.setPkDept(dept.getPkDept());
		user.setPkOrg(emp.getPkOrg());
		user.setNameEmp(emp.getNameEmp());
		user.setPkEmp(emp.getPkEmp());
		user.setCodeEmp(emp.getCodeEmp());
		UserContext.setUser(user);

		//2.根据操作编码，更新cn_ris_apply 的状态 eu_status
		String opeType = param.get("ope_type").toString();

		String appTpe = "";
		String appName = "";
		switch (opeType) {
			case "1"://接收状态，目前未处理
				return 1;
			case "2":
				param.put("euStatus", "3" );// ope_type=1(登记 ) => eu_status=3
				risService.opEx(param,user);
				appTpe = "P1";
				appName = "登记";
				break;
			case "3":
				param.put("euStatus", "4");// ope_type=3(更新报告) => eu_status=4
				saveRisResult(param);//更新报告结果
				appTpe = "P5";
				appName = "审核报告";
				break;
			case "4":
				param.put("euStatus", "3");// ope_type=4(报告撤销) => eu_status=3
				cancelRisResult(param);//清空报告结果
				appTpe = "P9";
				appName = "取消报告";
				break;
			case "9":
				param.put("euStatus", "1");// ope_type=6(取消登记  ),仅登记状态可撤销 => eu_status=1
				risService.cancelOpExTem(param, user);// 取消执行 +把执行单修改为未执行状态
				appTpe = "P3";
				appName = "取消登记";
				break;
			default:
				break;
		}

		//插入操作日志
		risService.addOpeRecord(param,user,"ECG",appTpe,appName,"1");

		int resultCount = DataBaseHelper.update("update cn_ris_apply set eu_status =:euStatus where pk_cnord =:pkCnord  ",param);
		return resultCount;
	}
}
