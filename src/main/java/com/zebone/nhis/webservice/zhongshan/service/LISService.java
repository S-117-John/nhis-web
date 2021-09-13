package com.zebone.nhis.webservice.zhongshan.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.BlIpMedicalExeService;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pub.service.ZsbaLisColAngCgService;
import com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo;
import com.zebone.nhis.webservice.zhongshan.dao.MedMapper;
import com.zebone.nhis.webservice.zhongshan.vo.BlCgVo;
import com.zebone.nhis.webservice.zhongshan.vo.CheckInParam;
import com.zebone.nhis.webservice.zhongshan.vo.PvEncounterVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class LISService {
	
	@Resource
	private MedMapper medMapper;//获取相关信息
	@Resource
	private MtsService mtsService;//记录流水操作人
	@Resource
	private IpCgPubService ipCgPubService;//住院记费公共类
    @Resource
    private BlIpMedicalExeService blIpMedExSer;//医技执行
    @Resource
    private ZsbaLisColAngCgService lisColAndCgSer;//检验采集/取消采集

	/**
	 * 1、条码打印与试管费用加收(LIS01)
	 * @param chkParam
	 * @return
	 */
	public String updateLabApplySampNoService(CheckInParam chkParam) {		
		// 校验传参
		if (!"LIS01".equals(chkParam.getFunc_id()))
			throw new BusException( "功能编号不为LIS01!");
		String msg = chkParam.toString();
		if (msg != null)
			return msg;
		
		//1、校验患者是否存在，并获取当前就诊信息
		PvEncounterVo pv = qryNhisPv(chkParam.getPatient_id(),chkParam.getAdmiss_times());//1.1 校验患者是否存在就诊记录
		
		//2.校验并设置默认操作人、机构、科室
		User u = SetEmpAndDept(chkParam, pv.getPkDeptNs());
		
		//3.试管收费编码判断,为空直接更新条码号，不为空计费并更新条码号
		if (chkParam.getCharge_code() != null && !CommonUtils.isEmptyString(chkParam.getCharge_code()))
			tubeBillBlCg(chkParam,pv);//1.2试管收费
		
		//4.更新条码号
		updateLabSampNo(chkParam.getBar_code(), pv.getPkPv(), chkParam.getOrd_list());//1.3 根据医嘱号更新医嘱状态
		
		//5.记录操作人
		addOpeRecord(chkParam, pv,"L0","条码打印",chkParam.getPrint_time(),u);//L0:条码打印
		return null;
	}

	/**
	 * 2、更新标本状态(LIS02) 
	 * @param chkParam
	 * @return
	 */
	public String updateLabApplyStatusService(CheckInParam chkParam) {
		
		// 校验传参
		if (!"LIS02".equals(chkParam.getFunc_id()))
			return "1|功能编号不为LIS02!";
		String msg = chkParam.toString();
		if (msg != null)
			return msg;
		
		//1.校验操作时间是否有误
		Date dateEx = checkOpeTime(chkParam);
		
		//2、校验患者是否存在，并获取当前就诊信息
		PvEncounterVo pv = qryNhisPv(chkParam.getPatient_id(),chkParam.getAdmiss_times());//1.1 校验患者是否存在就诊记录
		
		//3.校验并设置默认操作人、机构、科室
		User user = SetEmpAndDept(chkParam, pv.getPkDept());
		
		//4、更新标本状态
		updateLabStatus(chkParam, dateEx, pv,user);
		return null;
	}
	
	/**
	 * 2.1 校验操作时间是否有误
	 * @param chkParam
	 * @return
	 */
	private Date checkOpeTime(CheckInParam chkParam) {
		String dateOcc = chkParam.getOpe_time();
		Date dateEx = new Date();
		try {
			dateEx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateOcc);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new BusException(" 操作时间格式不对，更新失败！");
		}
		return dateEx;
	}
	
	/**
	 * 1.1 获取当前在院就诊记录
	 * @param patient_id
	 * @param admiss_time
	 * @return
	 */
	public PvEncounterVo qryNhisPv(String patient_id,String admiss_time) {
		String qrySql = "select pi.code_pi,pi.code_ip, pv.*  from pi_master pi "
				+ " inner join pv_encounter pv on pv.pk_pi = pi.pk_pi and pv.del_flag = '0'"
				+ " inner join pv_ip ip on ip.pk_pv = pv.pk_pv and ip.ip_times =?  and ip.del_flag = '0'"
				+ " where pi.code_pi =?  and pv.eu_status in('1','2','3')"; 
		PvEncounterVo pvvo =  DataBaseHelper.queryForBean(qrySql,PvEncounterVo.class, new Object[]{admiss_time,patient_id});
		if(pvvo == null)
			throw new BusException( "该患者不存在有效就诊的记录！");
		return pvvo;
	}
	
	/**
	 * 1.2根据医嘱号，更新条形码
	 * @param bar_code
	 * @param ord_list
	 */
	public void updateLabSampNo(String barCode, String pkPv, List<String> ordList) {

		//2020-04-13 原逻辑：传入的参数为医嘱号
//		String udeSql = "update cn_lab_apply set samp_no=:sampNo where exists (select 1 from cn_order ord "
//				+ " where ord.pk_cnord = cn_lab_apply.pk_cnord and ord.ordsn in (:ordsn) and ord.pk_pv =:pkPv) "
//				+ " and cn_lab_apply.del_flag='0' ";
		
		//2020-04-13 现逻辑：传入的参数为去掉前缀【CNLIS】的申请单号，更新时需要将前缀拼接上即可
		List<String> appList = new ArrayList<String>();
		for (String str : ordList) {
			appList.add("CNLIS" + str);
		}
		String upSql="update cn_lab_apply set samp_no=:sampNo where exists (select 1 from cn_order ord "
				+ " where ord.pk_cnord = cn_lab_apply.pk_cnord and ord.code_apply in (:appList) and ord.pk_pv =:pkPv) "
				+ " and cn_lab_apply.del_flag='0' ";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPv", pkPv);
		map.put("sampNo", barCode);
		//2020-04-13 原逻辑为医嘱号，调整为申请单号
		//map.put("ordsn", ordList);
		map.put("appList", appList);
		DataBaseHelper.update(upSql, map);
	}
	
	/**
	 * 1.3 试管加收费用
	 * @param param 传入参数
	 * @param pv 患者就诊
	 * @return
	 */
	public void tubeBillBlCg(CheckInParam param,PvEncounter pv) {
		
		//1.校验操作时间是否有误
		Date dateEx = checkOpeTime(param);
		
		//2、校验是否存在该记费项目
		BdItem item = DataBaseHelper.queryForBean("select * from bd_item where del_flag = '0' "
				+ " and code = ? ", BdItem.class, param.getCharge_code());
		if(item == null)
			throw new BusException( "记费项目：不存在编码为【"+ param.getCharge_code() +"】 的记费项目！"); 
		
		List<BlPubParamVo> cgParams = new ArrayList<BlPubParamVo>();
		BlPubParamVo cgParam = new BlPubParamVo();
		cgParam.setPkOrg(pv.getPkOrg());
		cgParam.setPkItem(item.getPkItem());//记费项目
		cgParam.setPkPi(pv.getPkPi());//患者主键
		cgParam.setPkPv(pv.getPkPv());//患者就诊主键
		cgParam.setEuPvType(pv.getEuPvtype());//就诊类型
		cgParam.setFlagPd("0");
		cgParam.setFlagPv("0");
		cgParam.setInfantNo("0");
		cgParam.setDateHap(dateEx);
		cgParam.setPkOrgApp(pv.getPkOrg());
		cgParam.setPkDeptApp(pv.getPkDept());
		cgParam.setPkDeptNsApp(pv.getPkDeptNs());
		cgParam.setPkEmpApp(UserContext.getUser().getPkEmp());
		cgParam.setNameEmpApp(UserContext.getUser().getNameEmp());
		cgParam.setPkOrgEx(pv.getPkOrg());
		cgParam.setPkDeptEx(pv.getPkDeptNs());
		cgParam.setPkEmpCg(UserContext.getUser().getPkEmp());
		cgParam.setNameEmpCg(UserContext.getUser().getNameEmp());
		cgParam.setPkDeptCg(pv.getPkDeptNs());
		cgParam.setQuanCg(1.0);
		cgParam.setPrice(item.getPrice());
		cgParams.add(cgParam);
		BlPubReturnVo cgRtns =  new BlPubReturnVo();
		
		//6.执行记费(单条记费)
		cgRtns = ipCgPubService.chargeIpBatch(cgParams,false);
		
		if(cgRtns==null || (cgRtns.getBids()==null || cgRtns.getBids().size()<1)){
			throw new BusException("住院记费失败！");
		}
	}
	
	/**
	 * 2.1 更新标本状态
	 * @param chkParam 2标本发送、3标本接收、4标本拒收、5上机、6报告
	 * @param dateEx
	 * @param pv
	 */
	private void updateLabStatus(CheckInParam chkParam, Date dateEx, PvEncounterVo pv,User user) {
		String setStr="";
		String operType ="";//操作类型
		String operName = "";//操作名称
		switch (chkParam.getOpe_type()) {
			case "1"://标本采集
				operType = "L1";
				operName = "采集标本";
				lisColAndCgSer.updateNhisRisApp(chkParam.getBar_code(), user);//调用与执行确认的统一方法，处理标本采集
				break;
			case "2"://标本发送
				operType = "L2";
				operName = "发送标本";
				break;
			case "3"://标本接收
				operType = "L3";
				operName = "接收标本";
				setStr=" eu_status= (case when eu_status = '4' then '4' else '3' end), date_acpt=:opeTime ";
				ExOccCg(chkParam,pv,dateEx);//医技记费
				break;
			case "4"://取消采集 
				operType = "L6";
				operName = "取消采集";
				lisColAndCgSer.CancelCollection(chkParam.getBar_code(), user);//调用与执行确认的统一方法，处理取消采集
				break;
			case "5"://取消发送
				operType = "L5";
				operName = "取消发送";
				break;
			case "6"://报告
				operType = "L4";
				operName = "编写报告";
				setStr=" eu_status= '4' ,date_rpt=:opeTime";
				saveLisResult(chkParam,pv,dateEx);//插入/更新检验结果
				break;
			case "7"://取消接收
				operType = "L7";
				operName = "取消接收";
				setStr=" eu_status= '2', date_acpt=null ";
				break;
	    	case "9":// (撤销登记  + 退费),eu_status=0
	    		operType = "L9";
				operName = "撤销登记";
	    		setStr= " eu_status ='0' ";
				cancelIpEx(chkParam,pv );// 取消执行 + 退费
				break;
			default: break;
		}
		
		//4、记录操作日志
		addOpeRecord(chkParam, pv,operType,operName,chkParam.getOpe_time(),user);//L0:条码打印
		
		//5、更新申请单状态
		if(StringUtils.isEmpty(setStr)) return;
		String setSql=" update cn_lab_apply set "+setStr
				+ " where exists (select 1 from cn_order ord "
				+ " where ord.pk_cnord = cn_lab_apply.pk_cnord and ord.pk_pv=:pkPv and ord.eu_status_ord < 9) "
				+ " and samp_no=:sampNo ";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPv", pv.getPkPv());
		map.put("opeTime", dateEx);
		map.put("sampNo", chkParam.getBar_code());
		DataBaseHelper.update(setSql, map);
	}
	/**
	 * 取消医技执行
	 * 根据医嘱主键，查询医技执行记录、执行单，调用 退费（更新执行记录，执行单  + 记费）
	 *
	 * @param param
	 * @param user
	 */
	private void cancelIpEx(CheckInParam chkParamMap, PvEncounter pv ) {

		//1. 根据条形码组装记费记录
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("sampNo", chkParamMap.getBar_code());
		paramMap.put("pkPv", pv.getPkPv());
		List<BlCgVo> allExList = medMapper.querylisForCg(paramMap);
		if(allExList == null || allExList.size() < 1) {
			throw new BusException(" 未查询到医嘱，条形码："+ chkParamMap.getBar_code()+"，撤销失败！");
		}
		//2.退费
		for (BlCgVo blCgVo: allExList){
			//3.更新医技执行记录
			Object[] args = new Object[4];
			args[0] = new Date();
			args[1] = UserContext.getUser().getPkEmp(); 
			args[2] = UserContext.getUser().getNameEmp();
			args[3] = blCgVo.getPkOrdexdt();
			DataBaseHelper.execute("update ex_assist_occ  set flag_canc=1,date_canc=?," +
					"pk_emp_canc=?,name_emp_canc=?,eu_status=9" +
					" where pk_assocc=? and flag_canc=0", args);
			args[3] = UserContext.getUser().getPkDept();
			blIpMedExSer.retCg(blCgVo.getPkOrdexdt(), UserContext.getUser(), args, "0");
		}
	}

	/**
	 * 2.2 记费，并更新执行单
	 * @param param
	 * @param pv
	 * @param dateEx
	 */
	public void ExOccCg(CheckInParam param, PvEncounter pv , Date dateEx){
		
		//1. 根据条形码组装记费记录
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("sampNo", param.getBar_code());
		paramMap.put("pkPv", pv.getPkPv());
		List<BlCgVo> allExList = medMapper.querylisForCg(paramMap);
		
		if(allExList == null || allExList.size() < 1) return;
		List<BlPubParamVo> cgParams = new ArrayList<BlPubParamVo>();
		for (BlCgVo blParam : allExList) {
			if(blParam.getPkCg() == null || CommonUtils.isEmptyString(blParam.getPkCg())){
				blParam.setPkEmpCg(UserContext.getUser().getPkEmp());
				blParam.setNameEmpCg(UserContext.getUser().getNameEmp());
				blParam.setDateHap(dateEx);
				blParam.setEuBltype("2");
				cgParams.add(blParam);
			}
		}

		//2. 存在未记费的执行单，调用住院患者记费接口，写表bl_ip_dt，并更新相关执行单
		if(cgParams == null || cgParams.size() < 1) return;
		BlPubReturnVo cgRtns = ipCgPubService.chargeIpBatch(cgParams,false);
		
		if(cgRtns==null || cgRtns.getBids()==null || cgRtns.getBids().size()<cgParams.size()){
			throw new BusException("接收标本时，记费调用失败！");
		}
		for (BlIpDt blIpDt : cgRtns.getBids()) {
			Object [] args = new Object [5];
			args[0] = dateEx;
			args[1] = UserContext.getUser().getPkEmp();
			args[2] = UserContext.getUser().getNameEmp();
			args[3] = blIpDt.getPkCgip();
			args[4] = blIpDt.getPkOrdexdt();
			DataBaseHelper.execute("update ex_order_occ set date_occ=?, pk_emp_occ=?,name_emp_occ=?,pk_cg=?,eu_status=1 where pk_exocc=? ",args);
		}		
	}

	/**
	 * 设置当前操作人|操作科室|操作机构
	 * @param param
	 * @param pkDept
	 * @return
	 */
	private User SetEmpAndDept(CheckInParam param, String pkDept) {
		
		//1. 校验操作人是否存在
		BdOuEmployee empOper = DataBaseHelper.queryForBean("select * from bd_ou_employee where del_flag = '0' "
				+ " and code_emp = ? ", BdOuEmployee.class, param.getOpe_code());
		if(empOper == null){
			empOper = new BdOuEmployee();
			empOper.setPkEmp(ApplicationUtils.getPropertyValue("lis.OpePkEmp", ""));
			empOper.setCodeEmp(param.getOpe_code());
			empOper.setNameEmp(ApplicationUtils.getPropertyValue("lis.OpeNameEmp", ""));
			empOper.setPkOrg(ApplicationUtils.getPropertyValue("lis.PkOrg", ""));
		}
		
		//赋值当前操作人，操作科室 = 医嘱执行科室
		User user = new User();
		user.setCodeEmp(param.getOpe_code());
		user.setPkEmp(empOper.getPkEmp());
		user.setNameEmp(empOper.getNameEmp());
		user.setPkOrg(empOper.getPkOrg());
		user.setPkDept(pkDept);
		UserContext.setUser(user);
		return user;
	}
	
//	/**
//	 * 3.获取检验申请单 调用方：LIS系统(在LIS系统接收检验申请的时候调用)
//	 * @param paramMap
//	 * @return
//	 */
//	public List<Map<String,Object>> qryLabApplyInfo(Map<String,Object> paramMap){
//		return medMapper.qryLabApplyInfo(paramMap);
//	}
//
//	public String tranJson(List<Map<String, Object>> labList) {
//		String json = JSONUtils.toJSONString(labList);
//		return "0|成功|" + json;
//	}
	
	/**
	 * 更新/插入检验结果
	 */
	private void saveLisResult(CheckInParam chkParam,PvEncounter pv,Date dateEx){
		if(chkParam.getRpt_id()==null||chkParam.getRpt_id().equals("")){
			throw new BusException("报告审核时，报告单号不能为空！");
		}
		if(chkParam.getBar_code()==null||chkParam.getBar_code().equals("")){
			throw new BusException("报告审核时，条码号不能为空！");
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("sampNo", chkParam.getBar_code());
		paramMap.put("pkPv", pv.getPkPv());
		List<CnOrder> ordList = medMapper.querylisForOrd(paramMap);
		for (CnOrder ord : ordList) {
			//查询 检查结果
			ExLabOcc exLabOcc = DataBaseHelper.queryForBean(" select * from ex_lab_occ where pk_pv = ? and pk_cnord=? and del_flag = '0' ", ExLabOcc.class, pv.getPkPv(),ord.getPkCnord());
			if(exLabOcc != null) {
				DataBaseHelper.update("update ex_lab_occ set code_rpt = ? where pk_pv = ? and pk_cnord = ?", chkParam.getRpt_id(),pv.getPkPv(),ord.getPkCnord());
			}else{
				exLabOcc = new ExLabOcc();
				//申请单号
				exLabOcc.setCodeApply(ord.getCodeApply());
				exLabOcc.setDateChk(dateEx);
				exLabOcc.setDateOcc(dateEx);
				exLabOcc.setDateRpt(dateEx);
				exLabOcc.setEuResult("0");
				exLabOcc.setFlagChk("1");
				exLabOcc.setDelFlag("0");
				exLabOcc.setNameEmpChk(UserContext.getUser().getNameEmp());
				exLabOcc.setNameEmpOcc(UserContext.getUser().getNameEmp());
				exLabOcc.setPkCnord(ord.getPkCnord());
				exLabOcc.setPkDeptOcc(UserContext.getUser().getPkDept());
				exLabOcc.setPkEmpChk(UserContext.getUser().getPkEmp());
				exLabOcc.setPkEmpOcc(UserContext.getUser().getPkEmp());
				exLabOcc.setPkOrg(UserContext.getUser().getPkOrg());
				exLabOcc.setPkOrgOcc(UserContext.getUser().getPkOrg());
				exLabOcc.setPkPi(pv.getPkPi());
				exLabOcc.setPkPv(pv.getPkPv());
				//报告单号
				exLabOcc.setCodeRpt(chkParam.getRpt_id());

				DataBaseHelper.insertBean(exLabOcc);
			}
		}
	}
	
	/**
	 * 插入操作人
	 * @param chkParam
	 * @param pv
	 * @param operType 检验：L0:条码打印L1:采集标本L2:发送标本L3:接收标本L4:编写报告L5:审核报告L6:取消采集L7:取消申请L8:取消报告
	 * @param operName
	 * @param operTime
	 * @param codeEmp
	 */
	private void addOpeRecord(CheckInParam chkParam, PvEncounterVo pv ,String operType ,String operName,String operTime,User u) {
		Map<String,Object> mtsParam = new HashMap<String,Object>();
		
		boolean flagPrint = false;//是否为打印条码
		if(null != chkParam.getOrd_list() && chkParam.getOrd_list().size() > 0){
			List<String> appList = new ArrayList<String>();
			for (String str : chkParam.getOrd_list()) {
				appList.add("CNLIS" + str);
			}			
			mtsParam.put("appList", appList);
			flagPrint = true;
		}
		mtsParam.put("rec_no",chkParam.getBar_code());		//rec_no:医技系统记录号
		mtsParam.put("pk_pv",pv.getPkPv());					//pk_pv：就诊id
		mtsParam.put("code_pi",chkParam.getPatient_id());	//code_pi：患者编码
		mtsParam.put("code_ip",pv.getCodeIp());				//code_ip:住院号
		mtsParam.put("times",chkParam.getAdmiss_times());	//times:就诊次数
		mtsParam.put("mts_type","LIS");						//mts_type:医技类型(LIS:检验 PACS：检查 ECG:心电)
		mtsParam.put("oper_type",operType);					//oper_type:操作类型-检验：L0:条码打印L1:采集标本L2:发送标本L3:接收标本L4:编写报告L5:审核报告L6:取消采集L7:取消申请L8:取消报告
		mtsParam.put("oper_name",operName);					//oper_name：操作名称
		mtsParam.put("oper_time",operTime);					//oper_time:操作时间
		mtsParam.put("emp_code",u.getCodeEmp());			//emp_code:人员编码
		mtsParam.put("emp_name",u.getNameEmp());			//emp_name:人员名称
		List<CnLabApplyVo> labAppList = DataBaseHelper.queryForList("select ord.name_ord ord_name,ord.code_apply code_app"
				+ ", app.* from cn_order ord "
				+ " inner join cn_lab_apply app on app.pk_cnord = ord.pk_cnord "
				+ " where ord.del_flag = '0' and app.del_flag = '0' and "
				+ (flagPrint ? " ord.code_apply in(:appList) " : " app.samp_no=:rec_no"), CnLabApplyVo.class, mtsParam);
		if(null != labAppList && labAppList.size() > 0){
			for (CnLabApplyVo app : labAppList) {
				mtsParam.put("mts_name",app.getOrdName());		//mts_name:医技名称
				mtsParam.put("req_no",app.getCodeApp());		//req_no:申请单号
				mtsService.updateMtsOperRec(mtsParam);
			}
		}
	}
	
}
