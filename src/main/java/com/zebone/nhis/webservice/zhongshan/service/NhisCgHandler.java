package com.zebone.nhis.webservice.zhongshan.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.support.ResultJson;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class NhisCgHandler {
	
	@Resource
	private NhisCgService nhisCgService ;
	
	/**
	 * 1.记费
	 * @param param
	 * @return
	 */
	public String BlCg(Map<String, Object> param){
		String res = "0|成功|";
		
		//(1.1) 基本校验录入参数 -- 非空/格式
		String chk = ChkBlCgParam(param);
		if(!CommonUtils.isEmptyString(chk)) return chk;
		
		//(1.2)校验患者是否存在(住院/门诊患者)
		PvEncounter pv = new PvEncounter();
		if ("3".equals(param.get("pv_type").toString())) {
			pv = DataBaseHelper.queryForBean("select pv.* from pv_encounter pv"
					+ " inner join pi_master pi on pi.pk_pi = pv.pk_pi and pi.del_flag = '0'"
					+ " inner join pv_ip ip on ip.pk_pv = pv.pk_pv and ip.del_flag = '0'"
					+ " where pv.del_flag = '0' and pv.flag_in ='1' and pv.eu_status = '1' and pv.eu_pvtype = '3'"
					+ "   and pi.code_pi = ? and ip.ip_times = ? ",PvEncounter.class, param.get("code_pi") , param.get("times"));
		} else {
			pv = DataBaseHelper.queryForBean("select pv.* from pv_encounter pv"
					+ " inner join pi_master pi on pi.pk_pi = pv.pk_pi and pi.del_flag = '0'"
					+ " inner join pv_op op on op.pk_pv = pv.pk_pv and op.del_flag = '0'"
					+ " where pv.del_flag = '0' and pv.flag_in ='0' and pv.eu_pvtype = '1' and pv.eu_status = '1'"
					+ "   and pi.code_pi = ? and op.op_times = ? ",PvEncounter.class, param.get("code_pi") , param.get("times"));
		}
		if(pv == null) 
			throw new BusException( "该患者不存在正在就诊的记录！");
		
		//(1.3) 校验是否存在该记费项目
		BdItem item = DataBaseHelper.queryForBean("select * from bd_item where del_flag = '0' "
				+ " and code_ext = ? ", BdItem.class, param.get("code_item"));
		if(item == null)
			throw new BusException( "记费项目：不存在编码为【"+ param.get("code_item").toString() +"】 的记费项目！"); 
		
		//(1.4) 校验操作人是否存在
 		BdOuEmployee empOper = DataBaseHelper.queryForBean("select * from bd_ou_employee where del_flag = '0' "
				+ " and pk_emp = ? ", BdOuEmployee.class, param.get("emp_oper"));
		if(empOper == null)
			throw new BusException( "操作人：不存在编码为【"+ param.get("emp_oper").toString() +"】 的人员！"); 
		
		//(1.5) 校验执行科室是否存在
		BdOuDept deptEx = DataBaseHelper.queryForBean("select * from bd_ou_dept where del_flag = '0' "
				+ "and pk_dept = ? ",BdOuDept.class,param.get("dept_ex"));
		if(deptEx == null)
			throw new BusException( "执行科室 ： 不存在编码为【"+ param.get("dept_ex").toString() +"】的科室！"); 
		
		//将操作人，执行科室作为当前操作人，当前操作科室
		User user = new User();
		user.setPkDept(deptEx.getPkDept());
		user.setPkOrg(deptEx.getPkOrg());
		user.setPkEmp(empOper.getPkEmp());
		user.setNameEmp(empOper.getNameEmp());
		UserContext.setUser(user);
		
		//(1.6) 校验开立医生是否存在
		BdOuEmployee empApp = DataBaseHelper.queryForBean("select * from bd_ou_employee where del_flag = '0' "
				+ " and pk_emp = ? ", BdOuEmployee.class, param.get("emp_app"));
		if(empApp == null)
			throw new BusException( "开立医生：不存在编码为【"+ param.get("emp_app").toString() +"】 的人员！"); 
		
		//(1.7) 开立科室 ：非空则校验是否存在
		BdOuDept deptApp = null;
		if(param.get("dept_app") != null && !CommonUtils.isEmptyString(param.get("dept_app").toString())){
			deptApp = DataBaseHelper.queryForBean("select * from bd_ou_dept where del_flag = '0' "
					+ "and pk_dept = ? ",BdOuDept.class,param.get("dept_app"));
			if(deptApp == null)
				throw new BusException( "开立科室 ： 不存在编码为【"+ param.get("dept_app").toString() +"】的科室！"); 
		}
		
		//(1.8) 开立病区 ：非空则校验是否存在
		BdOuDept deptNsApp = null;
		if(param.get("dept_ns_app") != null && !CommonUtils.isEmptyString(param.get("dept_ns_app").toString())){
			deptNsApp = DataBaseHelper.queryForBean("select * from bd_ou_dept where del_flag = '0' "
					+ "and pk_dept = ? ",BdOuDept.class,param.get("dept_ns_app"));
			if(deptNsApp == null)
				throw new BusException( "开立病区 ： 不存在编码为【"+ param.get("dept_ns_app").toString() +"】的科室！"); 
		}
		
		//(1.9) 记费科室 ：非空则校验是否存在
		BdOuDept deptCg = null;
		if(param.get("dept_cg") != null && !CommonUtils.isEmptyString(param.get("dept_cg").toString())){
			deptCg = DataBaseHelper.queryForBean("select * from bd_ou_dept where del_flag = '0' "
					+ "and pk_dept = ? ",BdOuDept.class,param.get("dept_cg"));
			if(deptCg == null)
				throw new BusException( "记费科室 ： 不存在编码为【"+ param.get("dept_cg").toString() +"】的科室！"); 	
		}
		
		//(1.10) 查询是否开立出院医嘱
		List<CnOrder> ordList = DataBaseHelper.queryForList("select * from cn_order where del_flag = '0' "
				+ " and eu_status_ord in('2','3') and flag_erase = '0' and pk_pv = ? "
				+ " and code_ordtype in ('1102','1103') "
				+ " order by date_start desc  ", CnOrder.class, pv.getPkPv());
		CnOrder ord2 = null;
		if(null != ordList && ordList.size() > 0)
			ord2 = ordList.get(0);
		
		//2.记费
		String resText = nhisCgService.BlCg(param, pv, item, empOper, deptEx, empApp, deptApp, deptNsApp, deptCg ,ord2);
		
		return CommonUtils.getString(new ResultJson(res+resText).toString());
	}
	
	/**
	 * 1.1 校验记费参数
	 * @param param
	 * @return
	 */
	private String ChkBlCgParam(Map<String, Object> param){
		String res = "";
		
		//1.功能编码  - 非空，== NHISCG01
		if(param.get("func_id") == null || CommonUtils.isEmptyString(param.get("func_id").toString())){
			throw new BusException("记费时，功能编码【func_id】为空！");
		}else if(!"NHISCG01".equals(param.get("func_id").toString())){
			throw new BusException("记费时，功能编码【func_id】输入有误！");
		}
		
		//2.调用系统编码  - 非空
		if(param.get("send_type") == null || CommonUtils.isEmptyString(param.get("send_type").toString())){
			throw new BusException("记费时，调用系统编码【send_type】为空！");
		}
		
		//3.就诊类型(门诊/住院)  - 非空
		if(param.get("pv_type") == null || CommonUtils.isEmptyString(param.get("pv_type").toString())){
			throw new BusException("记费时，就诊类型【pv_type】为空！");
		}
		
		//4.患者编码  - 非空
		if(param.get("code_pi") == null || CommonUtils.isEmptyString(param.get("code_pi").toString())){
			throw new BusException( "记费时，患者编码【code_pi】为空！");
		}
		
		//5.就诊次数  - 非空
		if(param.get("times") == null || CommonUtils.isEmptyString(param.get("times").toString())){
			throw new BusException( "记费时，就诊次数【times】为空！");
		}
		
		//6.记费数量  - 非空
		if(param.get("quan_cg") == null || CommonUtils.isEmptyString(param.get("quan_cg").toString())){
			throw new BusException( "记费时，记费数量【quan_cg】为空！");
		}
		
		//7.执行科室  - 非空
		if(param.get("dept_ex") == null || CommonUtils.isEmptyString(param.get("dept_ex").toString())){
			throw new BusException( "记费时，执行科室【dept_ex】为空！");
		}
		
		//8.开立医生  - 非空
		if(param.get("emp_app") == null || CommonUtils.isEmptyString(param.get("emp_app").toString())){
			throw new BusException( "记费时，开立医生【emp_app】为空！");
		}
		
		//9.操作人 - 非空
		if(param.get("emp_oper") == null || CommonUtils.isEmptyString(param.get("emp_oper").toString())){
			throw new BusException( "记费时，操作人【emp_oper】为空！");
		}	
		
		return res;
	}
	
	/**
	 * 2.退费
	 * @param param
	 * @return
	 */
	public String BlCgRtn(Map<String, Object> param){
		String res = "0|成功|";
		
		//2.1校验退费参数是否正确
		String chk = ChkBlCgRtnParam(param);
		if(!CommonUtils.isEmptyString(chk))  return chk;
		
		//2.2 校验退费主键是否有效
		String pkCg = param.get("pk_cg").toString();
		BlIpDt blIp = new BlIpDt();//住院记费记录
		BlOpDt blOp = new BlOpDt();//门诊记费记录
		if("3".equals(param.get("pv_type").toString()))//住院
		{
			List<BlIpDt> listIps = DataBaseHelper.queryForList("select * from bl_ip_dt where del_flag = '0' "
					+ " and pk_cgip = ? or pk_cgip_back = ? ", BlIpDt.class, pkCg, pkCg);
			
			if(listIps == null || listIps.size() < 1)
				throw new BusException( "不存在记费主键为【"+ pkCg +"】 的【住院记费】记录！"); 
			else if(listIps.size() > 1)
				throw new BusException( "记费主键为【"+ pkCg +"】 的【住院记费】记录已退费，不能再次退费！");
			else
				blIp = listIps.get(0);
			
			if(blIp.getFlagSettle() == "1")
				throw new BusException( "记费主键为【"+ pkCg +"】 的【住院记费】记录已结算，不能退费！");
			else if(blIp.getFlagInsu() == "1")
				throw new BusException( "记费主键为【"+ pkCg +"】 的【住院记费】记录已上传医保，不能退费！");
			else if(blIp.getPkCgipBack() != null)
				throw new BusException( "记费主键 【"+ pkCg +"】 是【住院退费】记录，不能再退费！"); 
		}
		else {
			List<BlOpDt> listOps = DataBaseHelper.queryForList("select * from bl_op_dt where del_flag = '0' "
					+ " and pk_cgop = ? or pk_cgop_back = ? ", BlOpDt.class, pkCg, pkCg);

			if(listOps == null || listOps.size() < 1 )
				throw new BusException( "不存在记费主键为【"+ pkCg +"】 的【门诊记费】记录！"); 
			else if(listOps.size() > 1)
				throw new BusException( "记费主键为【"+ pkCg +"】 的【门诊记费】记录已退费，不能再次退费！"); 
			else
				blOp = listOps.get(0);

			if(blOp.getFlagSettle() == "1")
				throw new BusException( "记费主键为【"+ pkCg +"】 的【门诊记费】记录已结算，不能退费！"); 
			else if(blOp.getFlagInsu() == "1")
				throw new BusException( "记费主键为【"+ pkCg +"】 的【门诊记费】记录已上传医保，不能退费！");
			else if(blOp.getPkCgopBack() != null)
				throw new BusException( "记费主键 【"+ pkCg +"】 是【门诊退费】记录，不能再退费！"); 
		}
		
		//2.3 校验操作人是否存在
		BdOuEmployee empOper = DataBaseHelper.queryForBean("select * from bd_ou_employee where del_flag = '0' "
				+ " and pk_emp = ? ", BdOuEmployee.class, param.get("emp_oper"));
		if(empOper == null)
			throw new BusException( "操作人：不存在编码为【"+ param.get("emp_oper").toString() +"】 的人员！"); 
		
		//2.4 校验执行科室是否存在
		BdOuDept deptCg = DataBaseHelper.queryForBean("select * from bd_ou_dept where del_flag = '0' "
				+ "and pk_dept = ? ",BdOuDept.class,param.get("dept_cg"));
		if(deptCg == null)
			throw new BusException( "退费科室 ： 不存在编码为【"+ param.get("dept_cg").toString() +"】的科室！"); 
		
		//将操作人，退费科室作为当前操作人，当前操作科室
		User user = new User();
		user.setPkDept(deptCg.getPkDept());
		user.setPkOrg(deptCg.getPkOrg());
		user.setPkEmp(empOper.getPkEmp());
		user.setNameEmp(empOper.getNameEmp());
		UserContext.setUser(user);
		
		String pkExOcc = "";
		if(param.get("pk_exocc") != null && !CommonUtils.isEmptyString(param.get("pk_exocc").toString()) )
			pkExOcc = param.get("pk_exocc").toString();
		
		//3.退费
		String pkCgBack = nhisCgService.BlCgRtn(blIp, blOp , pkExOcc);
		res += "{\"pk_cg\":\""+pkCgBack+"\"}";
		
		return CommonUtils.getString(new ResultJson(res).toString());
	}

	/**
	 * 2.1校验退费参数
	 * @param param
	 * @return
	 */
	private String ChkBlCgRtnParam(Map<String, Object> param) {
		String res = "";
		
		// 1.功能编码 - 非空，== NHISCG02
		if (param.get("func_id") == null
				|| CommonUtils.isEmptyString(param.get("func_id").toString())) {
			throw new BusException("退费时，功能编码【func_id】为空！");
		} else if (!"NHISCG02".equals(param.get("func_id").toString())) {
			throw new BusException("退费时，功能编码【func_id】输入有误！");
		}

		// 2.调用系统编码 - 非空
		if (param.get("send_type") == null
				|| CommonUtils.isEmptyString(param.get("send_type").toString())) {
			throw new BusException("记费时，调用系统编码【send_type】为空！");
		}

		// 3.就诊类型(门诊/住院) - 非空
		if (param.get("pv_type") == null
				|| CommonUtils.isEmptyString(param.get("pv_type").toString())) {
			throw new BusException("记费时，就诊类型【pv_type】为空！");
		}

		// 4.记费主键 - 非空
		if (param.get("pk_cg") == null
				|| CommonUtils.isEmptyString(param.get("pk_cg").toString())) {
			throw new BusException("退费时，记费主键【pk_cg】为空！");
		}

		// 5.操作人 - 非空
		if (param.get("emp_oper") == null
				|| CommonUtils.isEmptyString(param.get("emp_oper").toString())) {
			throw new BusException("退费时，操作人【emp_oper】为空！");
		}

		// 6.退费科室 - 非空
		if (param.get("dept_cg") == null
				|| CommonUtils.isEmptyString(param.get("dept_cg").toString())) {
			throw new BusException("退费时，退费科室【dept_cg】为空！");
		}
		
		return res;
	}
	
}
