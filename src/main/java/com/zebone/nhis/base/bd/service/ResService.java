package com.zebone.nhis.base.bd.service;

import com.zebone.nhis.base.bd.dao.OrgDeptWgMapper;
import com.zebone.nhis.base.bd.dao.ResMapper;
import com.zebone.nhis.base.bd.support.Constants;
import com.zebone.nhis.base.bd.vo.*;
import com.zebone.nhis.base.pub.dao.DeptMapper;
import com.zebone.nhis.base.pub.service.BdResPubService;
import com.zebone.nhis.common.module.base.bd.code.BdSysparamTemp;
import com.zebone.nhis.common.module.base.bd.res.*;
import com.zebone.nhis.common.module.base.bd.wf.BdFlowBp;
import com.zebone.nhis.common.module.base.bd.wf.BdFlowStep;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 资源定义服务
 * @author Xulj
 *
 */
@Service
public class ResService {
	
	@Autowired
	private ResMapper resMapper;
	
	@Resource
	private DeptMapper deptMapper;
	
	@Autowired
	private OrgDeptWgMapper orgDeptWgMapper;
	
	@Resource
	private BdResPubService bdResPubService;
	
	/**
	 * 保存工作站及参数
	 * @param param
	 * @param user
	 * @return
	 */
	public BdResPc savePcAndArgus(String param, IUser user){ 
		
		User u = (User)user;
		PcAndArgusParam pcAndArgus = JsonUtil.readValue(param, PcAndArgusParam.class);
		pcAndArgus.getPc().setAddr(pcAndArgus.getPc().getAddr().toUpperCase());
		pcAndArgus.getPc().setModifier(u.getPkEmp());
		pcAndArgus.getPc().setModityTime(new Date());
		BdResPc pc = pcAndArgus.getPc();
		List<BdResPcArgu> pcArgus = pcAndArgus.getPcargus();
		//挂号分配列表
		List<BdResPcDept> pcDepts = pcAndArgus.getPcDepts();
		
		/**保存或更新工作站*/
		if(pc.getPkPc() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_res_pc "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%'", Integer.class, pc.getCode(), pc.getPkOrg());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_res_pc "
					+ "where del_flag = '0' and name = ? and pk_org like ?||'%'", Integer.class, pc.getName(), pc.getPkOrg());
			int count_addr = DataBaseHelper.queryForScalar("select count(1) from bd_res_pc "
					+ "where del_flag = '0' and addr = ? and pk_org like ?||'%'", Integer.class, pc.getAddr(), pc.getPkOrg());
			if(count_code != 0){
				throw new BusException("工作站编码重复！");
			}else if(count_name != 0){
				throw new BusException("工作站名称重复！");
			}else if(count_addr != 0){
				throw new BusException("计算机名重复！");
			}else{
				pc.setEuAddrtype("0");
				DataBaseHelper.insertBean(pc);
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_res_pc "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%' and pk_pc != ?", Integer.class, pc.getCode(), pc.getPkOrg(), pc.getPkPc());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_res_pc "
					+ "where del_flag = '0' and name = ? and pk_org like ?||'%' and pk_pc != ?", Integer.class, pc.getName(), pc.getPkOrg(), pc.getPkPc());
			int count_addr = DataBaseHelper.queryForScalar("select count(1) from bd_res_pc "
					+ "where del_flag = '0' and addr = ? and pk_org like ?||'%' and pk_pc != ?", Integer.class, pc.getAddr(), pc.getPkOrg(), pc.getPkPc());
			if(count_code != 0){
				throw new BusException("工作站编码重复！");
			}else if(count_name != 0){
				throw new BusException("工作站名称重复！");
			}else if(count_addr != 0){
				throw new BusException("计算机名重复！");
			}else{
				DataBaseHelper.updateBeanByPk(pc,false);
			}
		}
		
		
		/**保存或更新工作站参数*/
		if(pcArgus != null && pcArgus.size() != 0){
			
			/**校验工作站参数编码、名称的唯一性*/
			Map<String,String> codeArguMap = new HashMap<String,String>();
			Map<String,String> nameArguMap = new HashMap<String,String>();
			int len = pcArgus.size();
			for(int i=0; i<len; i++){
				String codeArgu = pcArgus.get(i).getCodeArgu();
				String nameArgu = pcArgus.get(i).getNameArgu();
				if(codeArguMap.containsKey(codeArgu)){
					throw new BusException("工作站参数编码不唯一！");
				}
				if(nameArguMap.containsKey(nameArgu)){
					throw new BusException("工作站参数名称不唯一！");
				}
				codeArguMap.put(codeArgu, pcArgus.get(i).getPkPcargu());
				nameArguMap.put(nameArgu, pcArgus.get(i).getPkPcargu());
			}
			
			//先全删再恢复的方式（软删除）
			String pkpc = pc.getPkPc();
			DataBaseHelper.update("update bd_res_pc_argu set del_flag = '1', modifier=? , modity_time=?  where pk_pc = ?",pc.getModifier(),pc.getModityTime(),pkpc);
			for(BdResPcArgu pcargu : pcArgus){
				if(pcargu.getPkPcargu() != null){
					pcargu.setDelFlag("0");//恢复
					pcargu.setPkPc(pkpc);
					DataBaseHelper.updateBeanByPk(pcargu, false);
				}else{
					pcargu.setPkPc(pkpc);
					DataBaseHelper.insertBean(pcargu);
				}
			}
		}else{
			String pkpc = pc.getPkPc();
			//DataBaseHelper.update("update bd_res_pc_argu set del_flag = '1' where pk_pc = ?",new Object[]{pkpc});
			DataBaseHelper.update("update bd_res_pc_argu set del_flag = '1' , modifier=? , modity_time=? where pk_pc = ?",pc.getModifier(),pc.getModityTime(),pkpc);
		}
		
		//保存挂号分配
		DataBaseHelper.execute("update bd_res_pc_dept set del_flag = '1' , modifier=? , modity_time=? where pk_pc = ?",pc.getModifier(),pc.getModityTime(), pc.getPkPc());
		if(pcDepts != null && pcDepts.size() > 0){
			for (BdResPcDept bdResPcDept : pcDepts) {
				if(StringUtils.isNotBlank(bdResPcDept.getPkPcdept())){
					bdResPcDept.setDelFlag("0");
					DataBaseHelper.updateBeanByPk(bdResPcDept, false);
				}else{
					/*String sql = "select que.pkDept from bd_qc_que que where que.pk_qcplatform = ?";
					Map<String, Object> map = DataBaseHelper.queryForMap(sql, bdResPcDept.getPkQcplatform());*/
					bdResPcDept.setPkPc(pc.getPkPc());
					bdResPcDept.setPkOrg(u.getPkOrg());
					//bdResPcDept.setPkDept(map.get("pkDept")==null?null:map.get("pkDept").toString());
					DataBaseHelper.insertBean(bdResPcDept);
				}
			}
		}
		
		return pc;
	}
	
	/**
	 * 删除计算机工作站和参数
	 * @param param
	 * @param user
	 */
	public void delPcAndArgus(String param, IUser user){
		
		BdResPc pc = JsonUtil.readValue(param, BdResPc.class);
		
		DataBaseHelper.update("update bd_res_pc_argu set del_flag = '1', modifier=? , modity_time=?  where pk_pc = ?",pc.getModifier(),pc.getModityTime(), pc.getPkPc());
		DataBaseHelper.update("update bd_res_pc set del_flag = '1', modifier=? , modity_time=? where pk_pc = ?",pc.getModifier(),pc.getModityTime(), pc.getPkPc());
	}
	
	/**
	 * 保存医疗组和医疗组人员
	 * @param param
	 * @param user
	 * @return
	 */
	public OrgDeptWg saveWgAndEmps(String param, IUser user){
		WgAndEmpsParam wgAndEmps = JsonUtil.readValue(param, WgAndEmpsParam.class);
		OrgDeptWg wg = wgAndEmps.getWg();
		List<OrgDeptWgEmp> wgEmps = wgAndEmps.getWgemps();
		
		/**验证医疗组关联的人是否重复
		 * 至少2个，才验重*/
		if(wgEmps != null && wgEmps.size() > 1){
			
			Map<String, String> pkEmpMap = new HashMap<String, String>();
			int len = wgEmps.size();
			for(int i=0; i<len; i++){
				String pkEmp = wgEmps.get(i).getPkEmp();
				if(pkEmpMap.containsKey(pkEmp)){
					throw new BusException("医疗组关联的人员重复！");
				}
				pkEmpMap.put(pkEmp, wgEmps.get(i).getPkWgemp());
			}
			
		}
		
		//新增或更新 1.新增，2更新，3删除    用于消息发送
		String status="";
		/**保存或更新医疗组*/
		if(wg.getPkWg() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from org_dept_wg "
					+ "where del_flag = '0' and code_wg = ? and pk_dept = ?", Integer.class, wg.getCodeWg(), wg.getPkDept());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from org_dept_wg "
					+ "where del_flag = '0' and name_wg = ? and pk_dept = ?", Integer.class, wg.getNameWg(), wg.getPkDept());
			if(count_code == 0 && count_name == 0){
				wg.setEuStatus(Constants.EU_STATUS_START);
				DataBaseHelper.insertBean(wg);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("医疗组编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("医疗组名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("医疗组编码和名称都重复！");
				}
			}
			
			status = "1";
		}else{
			if(null != wg.getEuStatus()) {
				if(wg.getEuStatus().equals(Constants.EU_STATUS_PUBLISH)) {
					throw new BusException("已发布不能修改！");
				}
				//不允许提交人提交后再修改(审核人员除外)
				boolean approvePage = false;
				User  emp = (User)user;
				if(null != wgAndEmps.getIsParameterApprove() && wgAndEmps.getIsParameterApprove().equals("1")) {
					approvePage = true;
				}
				if(wg.getEuStatus().equals(Constants.EU_STATUS_SUBMIT) && !isModifyWg(emp,wg,approvePage)) {
					throw new BusException("已提交不能修改！");
				}
			}
			int count_code = DataBaseHelper.queryForScalar("select count(1) from org_dept_wg "
					+ "where del_flag = '0' and code_wg = ? and pk_dept = ? and pk_wg != ?", Integer.class, wg.getCodeWg(), wg.getPkDept(), wg.getPkWg());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from org_dept_wg "
					+ "where del_flag = '0' and name_wg = ? and pk_dept = ? and pk_wg != ?", Integer.class, wg.getNameWg(), wg.getPkDept(), wg.getPkWg());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(wg,false);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("医疗组编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("医疗组名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("医疗组编码和名称都重复！");
				}
			}
			
			status = "2";
		}
		
		/**保存或更新医疗组人员*/
		if(wgEmps != null && wgEmps.size() != 0){
			//先全删再恢复的方式（软删除）
			String pkwg = wg.getPkWg();
			DataBaseHelper.update("update org_dept_wg_emp set del_flag = '1' where pk_wg = ?",new Object[]{pkwg});
			for(OrgDeptWgEmp wgemp : wgEmps){
				if(wgemp.getPkWgemp() != null){
					wgemp.setDelFlag("0");//恢复
					wgemp.setPkWg(pkwg);
					DataBaseHelper.updateBeanByPk(wgemp, false);
				}else{
					wgemp.setPkWg(pkwg);
					DataBaseHelper.insertBean(wgemp);
				}
			}
		}else{
			throw new BusException("未添加人员！");
		}
		
		return wg;
	}
	
	/**  
	* <p>Desc: 走审批流,更新部门_医疗组表org_dept_wg字段属性</p>  
	* @param  : OrgDeptWg
	* @author : wangpengyong  
	* @date   : 2021年5月31日  
	*/  
	public void  updateWgFlow(boolean flowFlag,OrgDeptWg wg,User  emp) {
		if(flowFlag) {
			wg.setEuStatus(Constants.EU_STATUS_SUBMIT);
			wg.setPkEmpEntry(emp.getPkEmp());
			wg.setNameEmpEntry(emp.getNameEmp());
			wg.setDateEntry(new Date());
		} else {
			wg.setEuStatus(Constants.EU_STATUS_PUBLISH);
			wg.setPkEmpPub(emp.getPkEmp());
			wg.setNameEmpPub(emp.getNameEmp());
			wg.setDatePub(new Date());
		}
	} 
	
	
	/**  
	* <p>Desc:判断当前登录人是否可以再修改</p>  
	* @param  : 
	* @author : wangpengyong  
	* @date   : 2021年6月4日  
	*/  
	public boolean isModifyWg(User  emp,OrgDeptWg wg,boolean approvePage){
		boolean canModify = false;
		
		BdFlowBpExt bdFlow = null;
		List<BdFlowBpExt> flowConfigList = orgDeptWgMapper.getFlowConfig();
		if(null != flowConfigList && flowConfigList.size() > 0) {
			bdFlow = flowConfigList.get(0);
			
			//所有待审批的部门或人员
			List<String>  userOrDeptList = new ArrayList<String>();
			String bfs = "SELECT * from  bd_flow_step  where pk_flow=?";
			List<BdFlowStep> fbList = DataBaseHelper.queryForList(bfs, BdFlowStep.class,new Object[]{bdFlow.getPkFlow()});
			if(null != fbList && fbList.size() > 0) {
				for(BdFlowStep bf:fbList) {
					if(bdFlow.getEuType().equals("0")) {//部门审批
						String  euDeptType = bf.getEuDepttype();
						if(null != euDeptType && euDeptType.equals("1") && !userOrDeptList.contains(wg.getPkDept())) {
							userOrDeptList.add(wg.getPkDept());
						} else {
							if(null != bf.getPkDept() && !userOrDeptList.contains(bf.getPkDept())) {
								userOrDeptList.add(bf.getPkDept());
							}
						}
					} else {
						if(null != bf.getPkEmp() && !userOrDeptList.contains(bf.getPkEmp())) {
							userOrDeptList.add(bf.getPkEmp());
						}
					}
				}
			}
			
			if(approvePage) {//提交后的只允许审核部门的人员修改
				if(bdFlow.getEuType().equals("0")) {//部门审批
					  if(userOrDeptList.contains(emp.getPkDept())) {
						  canModify = true;
					  }
					} else {
					  if(userOrDeptList.contains(emp.getPkEmp())) {
							  canModify = true;
					  }
					}
			}
		} else {
			canModify = true;//不使用审批流，可以修改
		}
		return canModify;		
	}
	 
	/**  
	* <p>Desc:医疗组审核查询待审的医疗组列表</p>  
	* @param  : 
	* @author : wangpengyong  
	* @date   : 2021年6月3日  
	*/  
	public List<OrgDeptWg> queryApproveWg(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		if(null == paramMap) {
			paramMap = new HashMap<String,String>();
		}
		User  emp = (User)user;
		paramMap.put("curEmp", emp.getPkEmp());
		paramMap.put("curDept", emp.getPkDept());
		 
		List<OrgDeptWg> resBedList=orgDeptWgMapper.queryApproveWg(paramMap);
		return resBedList;		
	}
	
	/**  
	* <p>Desc:医疗组管理提交</p>  
	* @param  : 
	* @author : wangpengyong  
	* @date   : 2021年5月31日  
	*/  
	public void submitFlowWg(String param, IUser user){
		OrgDeptWg wg = JsonUtil.readValue(param, OrgDeptWg.class);
		OrgDeptWg app = DataBaseHelper.queryForBean("SELECT * from  org_dept_wg where PK_WG=?", OrgDeptWg.class, wg.getPkWg());
		if(null != app && !app.getEuStatus().equals(Constants.EU_STATUS_START)) {
			throw new BusException("状态是未提交才能提交!");
		}

		//task6786增加医疗组管理审核功能   wangpengyong
		User  emp = (User)user;
		boolean flowFlag = false;
		BdFlowBpExt bdFlow = null;
		List<BdFlowBpExt> flowConfigList = orgDeptWgMapper.getFlowConfig();
		if(null != flowConfigList && flowConfigList.size() > 0) {
			flowFlag = true;
			bdFlow = flowConfigList.get(0);
		}
		
		//更新部分字段
		updateWgFlow(flowFlag,wg,emp);
		DataBaseHelper.updateBeanByPk(wg,false);
		
		//生成第一个审批节点的待审批记录
		if(flowFlag) {
			addDpStartFlow(emp,bdFlow,wg.getPkWg());
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkWg", wg.getPkWg());
		paramMap.put("status", "add");
		//发送消息
		PlatFormSendUtils.sendOrgDeptWgMsg(paramMap);

	}
	
	/**  
	* <p>Desc:医疗组管理审核通过</p>  
	* @param  : 
	* @author : wangpengyong  
	* @date   : 2021年5月31日  
	*/  
	public void approveFlowWg(String param, IUser user){
		OrgDeptWg wg = JsonUtil.readValue(param, OrgDeptWg.class);
		String  bppk = wg.getPkWg();//业务主键
		
		OrgDeptWg app = DataBaseHelper.queryForBean("SELECT * from  org_dept_wg where PK_WG=?", OrgDeptWg.class, wg.getPkWg());
		if(null != app) {
			if(app.getEuStatus().equals(Constants.EU_STATUS_START)) {
				throw new BusException("医疗组未提交!");
			} else if(app.getEuStatus().equals(Constants.EU_STATUS_END)) {
				throw new BusException("医疗组已审批结束!");
			} else if(app.getEuStatus().equals(Constants.EU_STATUS_PUBLISH)) {
				throw new BusException("医疗组已发布!");
			}
		}
		
		User  emp = (User)user;
		BdFlowBpExt bdFlow = null;
		List<BdFlowBpExt> flowConfigList = orgDeptWgMapper.getFlowConfig();
		if(null != flowConfigList && flowConfigList.size() > 0) {
			bdFlow = flowConfigList.get(0);
		}
		
		//查找审批到哪个节点了
		String nextStepSql = "SELECT pk_flowstep from  bd_flow_bp fb where fb.del_flag = '0' and fb.eu_result='0' and fb.pk_bppk = ?";
		List<Map<String,Object>> fbList = DataBaseHelper.queryForList(nextStepSql, new Object[]{bppk});
		if(null != fbList && fbList.size() > 0) {
			//前一节点(第n节点)
			String pkFlowstep = fbList.get(0).get("pkFlowstep").toString();
			
			//1.更新当前步骤的业务审核记录为审批通过(上一节点)(第n节点)
			String updateSql = "update bd_flow_bp set eu_result='1',pk_emp=?,name_emp=?,date_chk=?,note='通过' "
					+" where pk_bppk=? and pk_flowstep=? and eu_result='0' ";
			DataBaseHelper.update(updateSql, new Object[]{emp.getPkEmp(),emp.getNameEmp(),new Date(),bppk,pkFlowstep});
			
			//2.生成下一节点待审批记录
			addDpApproveFlow(emp,bdFlow,bppk,pkFlowstep);
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkWg", wg.getPkWg());
		paramMap.put("status", "add");
		//发送消息
		PlatFormSendUtils.sendOrgDeptWgMsg(paramMap);
	}
	
	/**  
	* <p>Desc:医疗组管理审核退回</p>  
	* @param  : 
	* @author : wangpengyong  
	* @date   : 2021年5月31日  
	*/  
	public void  backFlowWg(String param, IUser user){
		OrgDeptWg wg = JsonUtil.readValue(param, OrgDeptWg.class);
		String  bppk = wg.getPkWg();//业务主键
		User  emp = (User)user;
		
		OrgDeptWg app = DataBaseHelper.queryForBean("SELECT * from  org_dept_wg where PK_WG=?", OrgDeptWg.class, wg.getPkWg());
		if(null != app) {
			if(app.getEuStatus().equals(Constants.EU_STATUS_START)) {
				throw new BusException("医疗组未提交!");
			} else if(app.getEuStatus().equals(Constants.EU_STATUS_END)) {
				throw new BusException("医疗组已审批结束!");
			} else if(app.getEuStatus().equals(Constants.EU_STATUS_PUBLISH)) {
				throw new BusException("医疗组已发布!");
			}
		}
		
		//查找审批到哪个节点了
		String nextStepSql = "SELECT pk_flowstep from  bd_flow_bp fb where fb.del_flag = '0' and fb.eu_result='0' and fb.pk_bppk = ?";
		List<Map<String,Object>> fbList = DataBaseHelper.queryForList(nextStepSql, new Object[]{bppk});
		if(null != fbList && fbList.size() > 0) {
		   //第n个节点
		   String pkFlowstep = fbList.get(0).get("pkFlowstep").toString();
		   
		    //1.更新当前步骤的业务审核记录为审批退回
			String updateSql = "update bd_flow_bp set eu_result='9',pk_emp=?,name_emp=?,date_chk=?,note='退回' "
					+" where pk_bppk=? and pk_flowstep=? and eu_result='0' ";
			DataBaseHelper.update(updateSql, new Object[]{emp.getPkEmp(),emp.getNameEmp(),new Date(),bppk,pkFlowstep});
			
			//2.更新上一个节点的审批记录为未审核
		   //查看是否第一个审批节点   是则直接更新医疗组管理表即可 否则更新上一节点的审批流记录为未审核
		   String lastStepSql = "select flag_start  from bd_flow_step where pk_flowstep =? ";
		   List<Map<String,Object>> lsList = DataBaseHelper.queryForList(lastStepSql, new Object[]{pkFlowstep});
		   if(null != lsList && lsList.size() > 0) {
			   String flagStart = lsList.get(0).get("flagStart").toString();
			    if(null != flagStart && flagStart.equals("1")) {//开始节点
		    	    //更新org_dept_wg表
					String updateWgSql = "update org_dept_wg set eu_status='0',pk_emp_entry=null,name_emp_entry=null,date_entry=null where pk_wg=?";
					DataBaseHelper.update(updateWgSql, new Object[]{bppk});
			      
			    } else {//其他节点
					//根据当前节点的步骤序号查找上一节点的步骤序号(第n-1节点)
					List<BdFlowBpExt> stepList = orgDeptWgMapper.findNextOrPreStep(pkFlowstep,false);
					if(null != stepList && stepList.size() > 0) {
						String pkFlowPrestep = stepList.get(0).getPkFlowstep();
						
						String updatePreSql = "update bd_flow_bp set eu_result='0',date_chk=?,note='' "
								+" where pk_bppk=? and pk_flowstep=? and eu_result='1' ";
						DataBaseHelper.update(updatePreSql, new Object[]{new Date(),bppk,pkFlowPrestep});
					}
			    }
		   }
		}
	}
	
	/**  
	* <p>Desc: 生成审批流记录(发起节点)</p>  
	* @author : wangpengyong  
	* @date   : 2021年5月31日  
	*/  
	public void addDpStartFlow(User  emp,BdFlowBpExt bdFlow,String pkWgID) {
		 
			BdFlowBp flowBp = new BdFlowBp();
			flowBp.setPkOrg(emp.getPkOrg());
			flowBp.setPkBppk(pkWgID);
			flowBp.setPkFlow(bdFlow.getPkFlow());
			flowBp.setCodeFlow(bdFlow.getCodeFlow());
			flowBp.setNameFlow(bdFlow.getNameFlow());
			flowBp.setDateSubmit(new Date());
			flowBp.setPkFlowstep(bdFlow.getPkFlowstep());
			flowBp.setEuResult(Constants.FLOW_STATUS_WAIT);
			flowBp.setNote("提交");
			
		   //设置审批科室,审批人(人员审批只记录人员,科室审批记录科室和审批人,发起节点只记录科室，等到审批时再记录审批的人员)
		   String enType = bdFlow.getEuType();
		   String firstStepSql = "select eu_depttype,pk_dept,pk_emp from bd_flow_step  where pk_flowstep =? ";
		   List<Map<String,Object>> lsList = DataBaseHelper.queryForList(firstStepSql, new Object[]{bdFlow.getPkFlowstep()});
		   if(null != lsList && lsList.size() > 0) {
			   if(null != enType && enType.equals("1")) {//审批类型为人员审批
				    String  pkEmp = lsList.get(0).get("pkEmp").toString();
					flowBp.setPkEmp(pkEmp);
				} else {//科室审批
					 String euDepttype = lsList.get(0).get("euDepttype").toString();
					//审批的科室类别  0:其他科室  1:本科室
					if(null != euDepttype && euDepttype.equals("1")) { 
						//不用用登录人，应该找提交人的部门
						OrgDeptWg app = DataBaseHelper.queryForBean("SELECT * from  org_dept_wg where PK_WG=?", OrgDeptWg.class, pkWgID);
						if(null != app) {
							flowBp.setPkDept(app.getPkDept());
						}
					} else { 
						String pkDept = lsList.get(0).get("pkDept").toString();
						flowBp.setPkDept(pkDept);//下一节点的审批科室
					}
				}
			   
		   }
		  DataBaseHelper.insertBean(flowBp);
	}
	
	/**  
	* <p>Desc: 生成审批流记录(审批中的节点)</p>  
	* @author : wangpengyong  
	* @date   : 2021年5月31日  
	*/  
	public void addDpApproveFlow(User  emp,BdFlowBpExt bdFlow,String pkWgID,String curFlowStep) {
		
       //首先查看是否终审节点，终审直接更新医疗组管理表即可 否则记录下一节点的审批流记录
	   String lastStepSql = "select flag_end  from bd_flow_step where pk_flowstep =? ";
	   List<Map<String,Object>> lsList = DataBaseHelper.queryForList(lastStepSql, new Object[]{curFlowStep});
	   if(null != lsList && lsList.size() > 0) {
		   String flagEnd = lsList.get(0).get("flagEnd").toString();
		   if(null != flagEnd && flagEnd.equals("1")) {
			   //更新org_dept_wg表
				String updateSql = "update org_dept_wg set eu_status='8',pk_emp_chk=?,name_emp_chk=?,date_chk=?,pk_emp_pub=?,name_emp_pub=?,date_pub=? where pk_wg=?";
				DataBaseHelper.update(updateSql, new Object[]{emp.getPkEmp(),emp.getNameEmp(),new Date(),emp.getPkEmp(),emp.getNameEmp(),new Date(),pkWgID});
		   } else {//非终审
			    BdFlowBp flowBp = new BdFlowBp();
				flowBp.setPkOrg(emp.getPkOrg());
				flowBp.setPkBppk(pkWgID);
				flowBp.setPkFlow(bdFlow.getPkFlow());
				flowBp.setCodeFlow(bdFlow.getCodeFlow());
				flowBp.setNameFlow(bdFlow.getNameFlow());
				flowBp.setDateSubmit(new Date());
				flowBp.setEuResult(Constants.FLOW_STATUS_WAIT);
				flowBp.setPkFlowstepPre(curFlowStep);
				
				//根据当前节点的步骤序号查找下一节点的步骤序号(第n+1节点)
				List<BdFlowBpExt> stepList = orgDeptWgMapper.findNextOrPreStep(curFlowStep,true);
				if(null != stepList && stepList.size() > 0) {
					flowBp.setPkFlowstep(stepList.get(0).getPkFlowstep());
					
					//设置待审批科室,审批人
					String enType = bdFlow.getEuType();
					if(null != enType && enType.equals("1")) {//审批类型为人员审批
						flowBp.setPkEmp(stepList.get(0).getPkEmp());
					} else {//科室审批
						//审批的科室类别  0:其他科室  1:本科室
						String enDeptType = stepList.get(0).getEuDepttype();
						if(null != enDeptType && enDeptType.equals("1")) { 
							flowBp.setPkDept(bdFlow.getPkDept());//发起的科室
						} else { 
							flowBp.setPkDept(stepList.get(0).getPkDept());//下一节点的审批科室
						}
					}
				}
				DataBaseHelper.insertBean(flowBp);
		   }
	   }	
	}
	
	/**
	 * 删除医疗组和医疗组人员
	 * @param param
	 * @param user
	 */
	public void delWgAndEmps(String param, IUser user){
		
		OrgDeptWg wg = JsonUtil.readValue(param, OrgDeptWg.class);
		
		//如果患者就诊_医疗组表中有数据，不能删除
		Map<String, Object> rtnMap = DataBaseHelper.queryForMap(" select count(1) cnt  from pv_clinic_group where pk_wg =? ", wg.getPkWg());
		if(null != rtnMap && Integer.parseInt(rtnMap.get("cnt").toString()) > 0) {
			throw new BusException("该医疗组正在使用,不能删除！");
		}
		
		DataBaseHelper.update("update org_dept_wg_emp set del_flag = '1' where pk_wg = ?", new Object[]{wg.getPkWg()});
		DataBaseHelper.update("update org_dept_wg set del_flag = '1' where pk_wg = ?", new Object[]{wg.getPkWg()});
		
		//同时需要逻辑删除原来的记录  wangpengyong
		DataBaseHelper.update("update bd_flow_bp set del_flag = '1' where pk_bppk = ?", new Object[]{wg.getPkWg()});
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkWg", wg.getPkWg());
		paramMap.put("status", "del");
		//发送消息
		PlatFormSendUtils.sendOrgDeptWgMsg(paramMap);
	}

	/**
	 * 查询床位收费项目明细信息
	 * @param param  pkBed （床位主键）
	 * @param user （当前登录用户信息）
	 * @return  bdItemBed （床位收费项目明细信息）
	 */
	public  List<Map<String, Object>> getBdItemBeds(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if( null == paramMap.get("pkBed") || CommonUtils.isEmptyString(paramMap.get("pkBed").toString())){
			throw new BusException("未获取床位主键！");
		}
		String pkBed = paramMap.get("pkBed").toString();		
		List<Map<String, Object>> bedItemList = DataBaseHelper.queryForList("select bed.*,item.pk_unit,item.spec,item.price,item.price*bed.quan as amount,item.code from bd_item_bed bed "
				+ " inner join bd_item item on bed.pk_item = item.pk_item where bed.del_flag='0' and bed.pk_bed = ? order by bed.flag_add desc", new Object[]{pkBed});
		return bedItemList;
		
	}
	
	/**
	 * 保存床位
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BedExtendVO> saveResBedList(String param, IUser user){
	
		List<BedExtendVO> resbedList = JsonUtil.readValue(param, new TypeReference<List<BedExtendVO>>() {});
		
		User u = (User) user;
		String pkOrg = u.getPkOrg();
		Date date = new Date();
		
		if(resbedList != null && resbedList.size() != 0){
			/**校验---1.校验前台所传的list的每一条编码和名称的唯一性
			 * 
			 * 此处校验前台也做了，所以在此处，后台不返回校验信息，后台默认认为：前台所传的resbedList中的每一条床位信息的编码和名称互相都不重复
			 * */
			Map<String, String> codemap = new HashMap<String, String>();
			Map<String, String> namemap = new HashMap<String, String>();
			//要新增的床位费用明细集合
			List<BdItemBedVO> insertItemBed = new ArrayList<BdItemBedVO>();
			//要修改的床位费用明细集合
			List<BdItemBedVO> updateItemBed = new ArrayList<BdItemBedVO>();
			
			int len = resbedList.size();
			for(int i=0; i<len; i++){
				String code = resbedList.get(i).getCode();
				String name = resbedList.get(i).getName();
				if(codemap.containsKey(code)){
					continue;
				}
				if(namemap.containsKey(name)){
					continue;
				}
				codemap.put(code, resbedList.get(i).getPkBed());
				namemap.put(name, resbedList.get(i).getPkBed());
			}
			
			String pk_ward = null;
			if(resbedList.size() > 0){
				pk_ward = resbedList.get(0).getPkWard();
			}
			
			if(StringUtils.isEmpty(pk_ward)) throw new BusException("没有获得病区参数！");
			
			/**查询数据库中所有*/
			List<BdResBed> allist = this.resMapper.findDeptBeds(pkOrg, pk_ward);
			
			/**校验---2.resbedList与数据库比较校验编码和名称的重复性*/
			for(BdResBed dataBed : allist){
				if(codemap.containsKey(dataBed.getCode())){
					String pkBed = codemap.get(dataBed.getCode());
					if(pkBed == null){
						throw new BusException("当前机构护理单元中床位信息编码存在重复！");
					}else{
						if(!dataBed.getPkBed().equals(pkBed)){
							throw new BusException("当前机构护理单元中床位信息编码存在重复！");
						}
					}
				}
				
				if(namemap.containsKey(dataBed.getName())){
					String pkBed = namemap.get(dataBed.getName());
					if(pkBed == null){
						throw new BusException("当前机构护理单元中床位信息名称存在重复！");
					}else{
						if(!dataBed.getPkBed().equals(pkBed)){
							throw new BusException("当前机构护理单元中床位信息名称存在重复！");
						}
					}
				}
			}
			
			List<String> insertPk=new ArrayList<String>();//用于存储新增后的主键，调用平台时传入
			
			/**新增或更新到数据库*/
			int cnt = 0;
			boolean isAddInf = ExSysParamUtil.getFlagAddInfByDeptNs(pk_ward,pkOrg);
			for(BedExtendVO bdResBed : resbedList){
				BdResBed bed = new BdResBed();
				ApplicationUtils.copyProperties(bed,bdResBed);
				//2020-03-02 博爱任务 床位费支持维护多条费用
				//前台传递要新增和修改的集合
				List<BdItemBedVO> bdItemBeds = bdResBed.getBdItemBeds();
				//前台传递要删除的集合
				List<BdItemBedVO> delBdItemBeds = bdResBed.getDelBdItemBeds();
				
				if(bed.getPkBed() == null){
					DataBaseHelper.insertBean(bed);
					insertPk.add(bed.getPkBed());
					//2020-03-02 博爱任务 床位费支持维护多条费用
					if(bdItemBeds != null){
						for (BdItemBedVO bdItemBedVO : bdItemBeds) {
							bdItemBedVO.setPkBeditem(NHISUUID.getKeyId());
							bdItemBedVO.setPkBed(bed.getPkBed());
							bdItemBedVO.setPkOrg(pkOrg);
							bdItemBedVO.setTs(date);
							bdItemBedVO.setCreator(u.getPkEmp());
							bdItemBedVO.setCreateTime(date);
							insertItemBed.add(bdItemBedVO);
						}
					}
				}else{
					String querySql = "select * from bd_res_bed where pk_bed = ?";
					BdResBed brb = DataBaseHelper.queryForBean(querySql,BdResBed.class,bed.getPkBed());
					if(brb != null){
						bed.setTs(brb.getTs());
						cnt = DataBaseHelper.updateBeanByPk(bed, true);
						if(brb.getPkItemAdd() != null && bed.getPkItemAdd() == null){
							String updateSql = "update bd_res_bed set pk_item_add = null where pk_bed = ?";
							cnt = DataBaseHelper.update(updateSql,new Object[]{bed.getPkBed()});
						}
						if(brb.getPkItem() != null && bed.getPkItem() == null){
							String updateSql = "update bd_res_bed set pk_item = null where pk_bed = ?";
							cnt = DataBaseHelper.update(updateSql,new Object[]{bed.getPkBed()});
						}
						if(cnt < 1)
							throw new BusException("当前数据不是最新数据，请刷新后再修改！");
						
						if(bdItemBeds != null){
							for (BdItemBedVO bdItemBedVO : bdItemBeds) {
								//新增
								if(StringUtils.isEmpty(bdItemBedVO.getPkBeditem())){
									bdItemBedVO.setPkBeditem(NHISUUID.getKeyId());
									bdItemBedVO.setPkBed(bed.getPkBed());
									bdItemBedVO.setPkOrg(pkOrg);
									bdItemBedVO.setTs(date);
									bdItemBedVO.setCreator(u.getPkEmp());
									bdItemBedVO.setCreateTime(date);
									insertItemBed.add(bdItemBedVO);
								//修改
								}else{
									bdItemBedVO.setModifier(u.getPkEmp());
									bdItemBedVO.setTs(date);
									updateItemBed.add(bdItemBedVO);
								}
							}
						}
						
					}
				}				
				
				//1-博爱明细模式，清空bd_res_bed中的费用明细; 0-原模式清空bd_item_bed
				if("1".equals(bdResBed.getIsBedCharge())){
					//清空bd_item_bed
					DataBaseHelper.deleteBeanByWhere(new BdItemBedVO(),"pk_bed = '"+bed.getPkBed()+"'");

					//新增床位明细
					if(insertItemBed != null){
						DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdItemBedVO.class), insertItemBed);
					}
//					//修改床位明细
//					if(updateItemBed != null){
//						String sql = "update bd_item_bed set pk_item=:pkItem,quan=:quan,flag_add=:flagAdd,note=:note,modifier=:creator,ts=:ts  "
//								+ " where pk_beditem=:pkBeditem ";
//						DataBaseHelper.batchUpdate(sql, updateItemBed);
//					}
//					//删除床位明细
//					if(delBdItemBeds != null){
//						DataBaseHelper.batchUpdate("update bd_item_bed set del_flag='1' where pk_beditem=:pkBeditem ", delBdItemBeds);
//					}
//
//					String sql = "update bd_res_bed set PK_ITEM=null,PK_ITEM_ADD=null where PK_BED=:pkBed ";
//					DataBaseHelper.update(sql, bed);
				}else{
					if(delBdItemBeds != null){
						//DataBaseHelper.update("update bd_item_bed set del_flag='1' where pk_bed=:pkBed ", bed);
						DataBaseHelper.deleteBeanByWhere(new BdItemBedVO(),"pk_bed = '"+bed.getPkBed()+"'");
					}	
				}
				
				//是否允许维护婴儿床位，是则添加婴儿相关床位
				if(isAddInf){
					bdResPubService.isHaveBedAndAdd(pk_ward,bed,null,null,u);
				}
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
			paramMap.put("resbedList", resbedList);
			PlatFormSendUtils.sendBdResBedMsg(paramMap);
		}else{
			throw new BusException("未传入床位信息！");
		}
		
		return resbedList;
	}

	/**
	 * 删除床位
	 * @param param
	 * @param user
	 */
	public void delResBed(String param, IUser user){
		BdResBed resbed = JsonUtil.readValue(param, BdResBed.class);
		if(resbed == null){
			throw new BusException("未获取到待删除的床位记录！");
		}
		//是否允许维护 婴儿
		boolean isAddInf = ExSysParamUtil.getFlagAddInfByDeptNs(resbed.getPkWard(),UserContext.getUser().getPkOrg());
		String bedSpc = ExSysParamUtil.getSpcOfCodeBed();
		//获取婴儿床位分隔符
		if(isAddInf && CommonUtils.isEmptyString(bedSpc)){
			throw new BusException("请先维护婴儿床位分隔符！");
		}
		String infBedCode = resbed.getCode() + bedSpc;
		StringBuffer sqlBuf = new StringBuffer("select count(1) from bd_res_bed where del_flag = '0' and flag_ocupy = '1' ");
		//允许录入婴儿时，添加上婴儿床的床位校验
		if(isAddInf){
			sqlBuf.append("and (pk_bed = ? or code like '"+infBedCode+"%')");
		} else {
			sqlBuf.append("and pk_bed = ?");
		}
			
		int countBed = DataBaseHelper.queryForScalar(sqlBuf.toString(), Integer.class, resbed.getPkBed());
		
		if(countBed == 0){
			//删除之前获取要删除的信息
			Map<String,Object> delBedMap = DataBaseHelper.queryForMap("select * from bd_res_bed where pk_bed = ?", resbed.getPkBed());
			DataBaseHelper.execute("delete from bd_res_bed where pk_bed = ? ", new Object[]{resbed.getPkBed()});
			if(isAddInf){
				DataBaseHelper.execute("delete from bd_res_bed where code like '"+infBedCode+"%' and pk_ward = ? ", new Object[]{resbed.getPkWard()});
			}
			//删除床位明细
			DataBaseHelper.execute("delete bd_item_bed where pk_bed = ? ", new Object[]{resbed.getPkBed()});
			//发送删除床位消息至集成平台
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
			paramMap.put("delBed", delBedMap);
			PlatFormSendUtils.sendBdResBedMsg(paramMap);
		}else{
			throw new BusException("占用状态的床位不允许删除！");
		}
	}
	
	/**
	 * 修改婴儿床位号
	 * 2018-07-05 灵璧需求 ：修改有患者的婴儿床位号
	 * 1、更新婴儿就诊记录中的床号
	 * 2、更新就诊床位记录中的床号
	 * 3、清空原床位相关患者信息
	 * 4、更新当前床位相关患者信息
	 * @param param
	 * @param user
	 */
	public void saveInfBedNo(String param, IUser user){
		
		Map<String,Object> upParam = JsonUtil.readValue(param, Map.class);
		if( null == upParam.get("pkPi") || CommonUtils.isEmptyString(upParam.get("pkPi").toString()))
			throw new BusException("未获取到待更新的婴儿患者主键！");
		String pkPi = upParam.get("pkPi").toString();
		
		if( null == upParam.get("bedNoSrc") || CommonUtils.isEmptyString(upParam.get("bedNoSrc").toString()))
			throw new BusException("未获取到待更新的原床位号！");
		String bedNoSrc = upParam.get("bedNoSrc").toString();
		
		if( null == upParam.get("bedNoTar") || CommonUtils.isEmptyString(upParam.get("bedNoTar").toString()))
			throw new BusException("未获取到待更新的原床位号！");
		String bedNoTar = upParam.get("bedNoTar").toString();
		
		String pkDeptNs = UserContext.getUser().getPkDept();
		String pkOrg = UserContext.getUser().getPkOrg();
		
		//1、根据 选中床位行患者pk_pi、'当前病区'，定位患者就诊记录，更新目标床号[当前病区在院且就诊状态的患者]
		DataBaseHelper.update("update pv_encounter set bed_no=? where pk_pi=? and pk_dept_ns=? and flag_in='1' and eu_status='1' ", bedNoTar,pkPi,pkDeptNs);
		//2、根据 选中床位行患者pk_pi、'当前病区'， 定位患者就诊床位，更新目标床号[当前病区在院且就诊状态的患者]
		DataBaseHelper.update("update pv_bed set bedno=? where pk_dept_ns=? and pk_pv = (select pk_pv from pv_encounter where pk_pi=? and pk_dept_ns=? and flag_in='1' and eu_status='1') ", bedNoTar,pkDeptNs,pkPi,pkDeptNs);
		//3、根据 '原床位号'、'当前病区'、'当前机构'，清空原床位信息
		DataBaseHelper.update("update bd_res_bed set pk_pi=null ,flag_ocupy='0',eu_status='01' where code=? and pk_ward=? and pk_org=? ", bedNoSrc,pkDeptNs,pkOrg);
		//4、根据 '目标床位号'、'当前病区'、'当前机构'，更新目标床位信息
		DataBaseHelper.update("update bd_res_bed set pk_pi=? ,flag_ocupy='1',eu_status='02' where code=? and pk_ward=? and pk_org=? ",pkPi, bedNoTar,pkDeptNs,pkOrg);

	}
	
	/**
	 * 保存手术台和设备
	 * @param param
	 * @param user
	 * @return
	 */
	public BdResOpt saveOptAndFas(String param, IUser user){
		OptAndFasParam optAndFas = JsonUtil.readValue(param, OptAndFasParam.class);
		BdResOpt opt = optAndFas.getOpt();
		List<BdResOptFa> optfas = optAndFas.getOptFas();
		
		/**验证手术台设备编码是否重复
		 * 至少2个，才验重*/
		if(optfas != null && optfas.size() > 1){
			
			Map<String, String> codeMap = new HashMap<String, String>();
			int len = optfas.size();
			for(int i=0; i<len; i++){
				String code = optfas.get(i).getCodeFa();
				if(codeMap.containsKey(code)){
					throw new BusException("手术台设备编码重复！");
				}
				codeMap.put(code, optfas.get(i).getPkOptfa());
			}
			
		}
		
		/**保存或更新手术台*/
		if(opt.getPkOpt() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_res_opt "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%'", Integer.class, opt.getCode(), opt.getPkOrg());
			if(count_code != 0){
				throw new BusException("手术台编码重复！");
			}else{
				DataBaseHelper.insertBean(opt);
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_res_opt "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%' and pk_opt != ?", Integer.class, opt.getCode(), opt.getPkOrg(), opt.getPkOpt());
			if(count_code != 0){
				throw new BusException("手术台编码重复！");
			}else{
				DataBaseHelper.updateBeanByPk(opt,false);
			}
		}
		
		/**保存或更新手术台与设备关系*/
		//先全删再恢复的方式（软删除）
		String pkopt = opt.getPkOpt();
		DataBaseHelper.update("update bd_res_opt_fa set del_flag = '1' where pk_opt = ?",new Object[]{pkopt});
		if(optfas != null && optfas.size() != 0){
			for(BdResOptFa optfa : optfas){
				if(optfa.getPkOptfa() != null){
					optfa.setDelFlag("0");//恢复
					optfa.setPkOpt(pkopt);
					DataBaseHelper.updateBeanByPk(optfa, false);
				}else{
					optfa.setPkOpt(pkopt);
					DataBaseHelper.insertBean(optfa);
				}
			}
		}
		
		return opt;
		
	}
	
	/**
	 * 删除手术台
	 * @param param
	 * @param user
	 */
	public void delResOpt(String param, IUser user){
		
		BdResOpt opt = JsonUtil.readValue(param, BdResOpt.class);
	
		int count_opt = DataBaseHelper.queryForScalar("select count(1) from bd_res_opt where del_flag = '0' and hold_flag = '1' and pk_opt = ?", Integer.class, opt.getPkOpt());
		if(count_opt == 0){
			DataBaseHelper.update("update bd_res_opt set del_flag = '1' where pk_opt = ?", new Object[]{opt.getPkOpt()});
			DataBaseHelper.update("update bd_res_opt_fa set del_flag = '1' where pk_opt = ?", new Object[]{opt.getPkOpt()});
		}else{
			throw new BusException("占用状态的手术台不允许删除！");
		}
	}
	
	/**
	 * 保存医疗资源和设备
	 * @param param
	 * @param user
	 * @return
	 */
	public BdResMsp saveMspAndFas(String param, IUser user){
		MspAndFasParam MspAndFas = JsonUtil.readValue(param, MspAndFasParam.class);
		BdResMsp msp = MspAndFas.getMsp();
		List<BdResMspFa> mspfas = MspAndFas.getMspFas();
		
		/**验证医疗资源设备编码是否重复
		 * 至少2个，才验重*/
		if(mspfas != null && mspfas.size() > 1){
			
			Map<String, String> codeMap = new HashMap<String, String>();
			int len = mspfas.size();
			for(int i=0; i<len; i++){
				String code = mspfas.get(i).getCodeFa();
				if(codeMap.containsKey(code)){
					throw new BusException("医疗资源设备编码重复！");
				}
				codeMap.put(code, mspfas.get(i).getPkMspfa());
			}
			
		}
		
		/**保存或更新医技资源*/
		if(msp.getPkMsp() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_res_msp "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%'", Integer.class, msp.getCode(), msp.getPkOrg());
			if(count_code != 0){
				throw new BusException("医技资源编码重复！");
			}else{
				DataBaseHelper.insertBean(msp);
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_res_msp "
					+ "where del_flag = '0' and code = ? and pk_org like ?||'%' and pk_msp != ?", Integer.class, msp.getCode(), msp.getPkOrg(), msp.getPkMsp());
			if(count_code != 0){
				throw new BusException("医技资源编码重复！");
			}else{
				DataBaseHelper.updateBeanByPk(msp,false);
			}
		}
		
		/**保存或更新医技资源与设备关系*/
		//先全删再恢复的方式（软删除）
		String pkmsp = msp.getPkMsp();
		DataBaseHelper.update("update bd_res_msp_fa set del_flag = '1' where pk_msp = ?",new Object[]{pkmsp});
		if(mspfas != null && mspfas.size() != 0){
			for(BdResMspFa mspfa : mspfas){
				if(mspfa.getPkMspfa() != null){
					mspfa.setDelFlag("0");//恢复
					mspfa.setPkMsp(pkmsp);
					DataBaseHelper.updateBeanByPk(mspfa, false);
				}else{
					mspfa.setPkMsp(pkmsp);
					DataBaseHelper.insertBean(mspfa);
				}
			}
		}
		
		return msp;
		
	}
	
	/**
	 * 删除医疗资源
	 * @param param
	 * @param user
	 */
	public void delResMsp(String param, IUser user){
		
		BdResMsp msp = JsonUtil.readValue(param, BdResMsp.class);
	
		int count_msp = DataBaseHelper.queryForScalar("select count(1) from bd_res_msp where del_flag = '0' and hold_flag = '1' and pk_msp = ?", Integer.class, msp.getPkMsp());
		if(count_msp == 0){
			DataBaseHelper.update("update bd_res_msp set del_flag = '1' where pk_msp = ?", new Object[]{msp.getPkMsp()});
			DataBaseHelper.update("update bd_res_msp_fa set del_flag = '1' where pk_msp = ?", new Object[]{msp.getPkMsp()});
		}else{
			throw new BusException("占用状态的医技资源不允许删除！");
		}
	}
	
	/**
	 * 保存业务线定义及线下部门定义
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveDeptBuAndBuses(String param, IUser user){
		
		DeptBuAndBusesParam deptBuAndBuses = JsonUtil.readValue(param, DeptBuAndBusesParam.class);
		BdDeptBu deptbu = deptBuAndBuses.getDeptbu();
		List<BdDeptBus> deptbusList = deptBuAndBuses.getDeptbusList();
		
		String pkOrg = ((User)user).getPkOrg();
		
		if(deptbu.getPkDeptbu() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_dept_bu "
					+ "where del_flag = '0' and code = ? and dt_butype = ? and pk_org like ?||'%'", Integer.class, deptbu.getCode(), deptbu.getDtButype(), pkOrg);
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_dept_bu "
					+ "where del_flag = '0' and name = ? and dt_butype = ? and pk_org like ?||'%'", Integer.class, deptbu.getName(), deptbu.getDtButype(),pkOrg);
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(deptbu);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("同一类型下【业务线定义】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("同一类型下【业务线定义】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("同一类型下【业务线定义】编码和名称都重复！");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_dept_bu "
					+ "where del_flag = '0' and code = ? and dt_butype = ? and pk_org like ?||'%' and pk_deptbu != ?", Integer.class, deptbu.getCode(), deptbu.getDtButype(), pkOrg, deptbu.getPkDeptbu());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_dept_bu "
					+ "where del_flag = '0' and name = ? and dt_butype = ? and pk_org like ?||'%' and pk_deptbu != ?", Integer.class, deptbu.getName(), deptbu.getDtButype(), pkOrg, deptbu.getPkDeptbu());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(deptbu, false);
			}else{
				if(count_code != 0 && count_name == 0){
					throw new BusException("同一类型下【业务线定义】编码重复！");
				}
				if(count_code == 0 && count_name != 0){
					throw new BusException("同一类型下【业务线定义】名称重复！");
				}
				if(count_code != 0 && count_name != 0){
					throw new BusException("同一类型下【业务线定义】编码和名称都重复！");
				}
			}
		}
		
		if(deptbusList != null && deptbusList.size() != 0){
			/**校验业务线部门唯一性*/
			Map<String,String> pkDeptMap = new HashMap<String,String>();
			int len = deptbusList.size();
			for(int i=0; i<len; i++){
				//保存校验变更：科室不可重复 改成 科室+科室类型 不可重复
				String pkDept = deptbusList.get(i).getPkDept()+"#"+deptbusList.get(i).getDtDepttype();
				if(pkDeptMap.containsKey(pkDept)){
					throw new BusException("业务线下部门定义重复！");
				}
				pkDeptMap.put(pkDept, deptbusList.get(i).getPkDeptbus()+"#"+deptbusList.get(i).getDtDepttype());
			}
			
			//先全删再恢复的方式（软删除）
			String pkDeptbu = deptbu.getPkDeptbu(); 
			DataBaseHelper.update("update bd_dept_bus set del_flag = '1' where pk_deptbu = ?", new Object[]{pkDeptbu});
			for(BdDeptBus dbus : deptbusList){
				if(dbus.getPkDeptbus() != null){
					dbus.setDelFlag("0");//恢复
					dbus.setPkDeptbu(pkDeptbu);
					DataBaseHelper.updateBeanByPk(dbus, false);
				}else{
					dbus.setPkDeptbu(pkDeptbu);
					DataBaseHelper.insertBean(dbus);
				}
			}
			DataBaseHelper.execute("delete from bd_dept_bus where del_flag = '1' and pk_deptbu = ?", new Object[]{pkDeptbu});
		}else{
			String pkDeptbu = deptbu.getPkDeptbu();
			//DataBaseHelper.update("update bd_dept_bus set del_flag = '1' where pk_deptbu = ?", new Object[]{pkDeptbu});
			DataBaseHelper.execute("delete from bd_dept_bus where pk_deptbu = ?", new Object[]{pkDeptbu});
			
		}
		
	}
	
	/**
	 * 根据业务线定义主键删除
	 * @param param
	 * @param user
	 */
	public void delDeptBuAndBuses(String param, IUser user){
		String pkDeptbu = JsonUtil.getFieldValue(param, "pkDeptbu");
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select flag_sys from bd_dept_bu where del_flag = '0' and pk_deptbu = ?", pkDeptbu);
		if("1".equals(queryForMap.get("flagSys").toString())){
			throw new BusException("该业务线定义已被系统使用无法删除！");
		}else{
			//DataBaseHelper.update("update bd_dept_bus set del_flag ='1' where pk_deptbu = ?", new Object[]{pkDeptbu});
			//DataBaseHelper.update("update bd_dept_bu set del_flag ='1' where pk_deptbu = ?", new Object[]{pkDeptbu});
			DataBaseHelper.execute("delete from bd_dept_bus where pk_deptbu = ?", new Object[]{pkDeptbu});
			DataBaseHelper.execute("delete from bd_dept_bu where pk_deptbu = ?", new Object[]{pkDeptbu});		    
		}
	}
	
	/**
	 * 获取资源对象信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getResInfosByType(String param, IUser user){
		
		Map paramMap = JsonUtil.readValue(param, Map.class);
		
		String pkOrg = ((User)user).getPkOrg();
		int euRestype = Integer.valueOf(paramMap.get("euRestype").toString());
		String pkDeptBelong = null;
		if(paramMap.get("pkDeptBelong") != null){
			pkDeptBelong = paramMap.get("pkDeptBelong").toString();
		}
		
		List<Map<String, Object>> resmapList = new ArrayList<Map<String, Object>>();
		
		switch (euRestype) {
		case Constants.euRestype_dept:
			resmapList = this.resMapper.getBdOuDeptsByOrgDept(pkOrg, pkDeptBelong);
			break;
		case Constants.euRestype_emp:
			resmapList = this.resMapper.getBdOuEmployeesByOrgDept(pkOrg, pkDeptBelong);
			break;
		case Constants.euRestype_opt:
			resmapList = this.resMapper.getBdResOptsByOrgDept(pkOrg, pkDeptBelong);
			break;
		case Constants.euRestype_bed:
			resmapList = this.resMapper.getBdResBedsByOrgDept(pkOrg, pkDeptBelong);
			break;
		case Constants.euRestype_msp:
			resmapList = this.resMapper.getBdResMspsByOrgDept(pkOrg, pkDeptBelong);
			break;
		default:
			break;
		}
		
		return resmapList;
	}
	
	/**
	 * 保存部门下业务单元
	 * @param param
	 * @param user
	 */
//	public void saveDptUnit(String param, IUser user){
//		
//		BdDeptUnit dptUnit = JsonUtil.readValue(param, BdDeptUnit.class);
//		
//		String window = dptUnit.getEuUnittype();
//		if("0".equals(window)){//0--诊室
//			window = "诊室";
//		}else if("1".equals(window)){//1--药房窗口
//			window = "药房窗口";
//		}
//		String pkOrg = ((User)user).getPkOrg();
//		
//		if(dptUnit.getPkDeptunit() == null){
//			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_dept_unit "
//					+ "where del_flag = '0' and code = ? and pk_org like ?||'%' and pk_dept = ?", Integer.class, dptUnit.getCode(),pkOrg,dptUnit.getPkDept());
//			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_dept_unit "
//					+ "where del_flag = '0' and name = ? and pk_org like ?||'%' and pk_dept = ?", Integer.class, dptUnit.getName(),pkOrg,dptUnit.getPkDept());
//			if(count_code == 0 && count_name == 0){
//				DataBaseHelper.insertBean(dptUnit);
//			}else{
//				if(count_code != 0 && count_name == 0){
//					throw new BusException("当前科室内"+ window +"编码重复！");
//				}
//				if(count_code == 0 && count_name != 0){
//					throw new BusException("当前科室内"+ window +"名称重复！");
//				}
//				if(count_code != 0 && count_name != 0){
//					throw new BusException("当前科室内"+ window +"编码和名称都重复！");
//				}
//			}
//		}else{
//			int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_dept_unit "
//					+ "where del_flag = '0' and code = ? and pk_org like ?||'%' and pk_dept = ? and pk_deptunit != ?", Integer.class, dptUnit.getCode(), pkOrg,dptUnit.getPkDept(), dptUnit.getPkDeptunit());
//			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_dept_unit "
//					+ "where del_flag = '0' and name = ? and pk_org like ?||'%' and pk_dept = ? and pk_deptunit != ?", Integer.class, dptUnit.getName(), pkOrg,dptUnit.getPkDept(), dptUnit.getPkDeptunit());
//			if(count_code == 0 && count_name == 0){
//				DataBaseHelper.updateBeanByPk(dptUnit, false);
//			}else{
//				if(count_code != 0 && count_name == 0){
//					throw new BusException("当前科室内"+ window +"编码重复！");
//				}
//				if(count_code == 0 && count_name != 0){
//					throw new BusException("当前科室内"+ window +"名称重复！");
//				}
//				if(count_code != 0 && count_name != 0){
//					throw new BusException("当前科室内"+ window +"编码和名称都重复！");
//				}
//			}
//		}
//		
//	}
	
	/**
	 * 保存部门下业务单元
	 * @param param
	 * @param user
	 */
	public void saveDptUnitList(String param, IUser user){
		
		List<BdDeptUnit> unitlist = JsonUtil.readValue(param, new TypeReference<List<BdDeptUnit>>() {
		});
		
		if(unitlist != null && unitlist.size() != 0){
			
			String pkOrg = ((User) user).getPkOrg();
			//String pkDept = unitlist.get(0).getPkDept();//前台所传的单元为同一科室下的
			String window = unitlist.get(0).getEuUnittype();//前台所传的单元为同一单元类型的
			if("0".equals(window)){//0--诊室
				window = "诊室";
			}else if("1".equals(window)){//1--药房窗口
				window = "药房窗口";
			}
			
			/**校验---1.校验前台所传的list的每一条编码和名称的唯一性*/
			Map<String, String> codemap = new HashMap<String, String>();
			Map<String, String> namemap = new HashMap<String, String>();
			int len = unitlist.size();
			for(int i=0; i<len; i++){
				String code = unitlist.get(i).getCode();
				String name = unitlist.get(i).getName();
				if(codemap.containsKey(code)){
					throw new BusException(window+"编码重复，请确保编码的唯一性！");
				}
				if(namemap.containsKey(name)){
					throw new BusException(window+"名称重复，请确保名称的唯一性！");
				}
				codemap.put(code, unitlist.get(i).getPkDeptunit());
				namemap.put(name, unitlist.get(i).getPkDeptunit());
				
			}
			
			/**查询数据库中所有*/
			List<BdDeptUnit> allist = this.resMapper.findAllDeptUnits(pkOrg);
			
			/**校验---2.unitlist与数据库比较校验编码和名称的重复性*/
			for(BdDeptUnit dataUnit : allist){
				if(codemap.containsKey(dataUnit.getCode())){
					String pkDeptunit = codemap.get(dataUnit.getCode());
					if(pkDeptunit == null){
						throw new BusException(window+"编码重复，请确保编码的唯一性！");
					}else{
						if(!dataUnit.getPkDeptunit().equals(pkDeptunit)){
							throw new BusException(window+"编码重复，请确保编码的唯一性！");
						}
					}
				}
				
				if(namemap.containsKey(dataUnit.getName())){
					String pkDeptunit = namemap.get(dataUnit.getName());
					if(pkDeptunit == null){
						throw new BusException(window+"名称重复，请确保名称的唯一性！");
					}else{
						if(!dataUnit.getPkDeptunit().equals(pkDeptunit)){
							throw new BusException(window+"名称重复，请确保名称的唯一性！");
						}
					}
				}
				
			}
		    Set<String> pkDepts = new HashSet<>();
			/**新增或更新到数据库*/
			for(BdDeptUnit unit : unitlist){
				if(unit.getPkDeptunit() == null){
					DataBaseHelper.insertBean(unit);
				}else{
					DataBaseHelper.updateBeanByPk(unit,false);
				}
				pkDepts.add(unit.getPkDept());
			}

			if(window == "诊室") {
				Map<String,Object> msgParam = new HashMap<String,Object>();
				//发送诊室信息
				msgParam.put("pkDepts", pkDepts);
				msgParam.put("STATUS", "_ADD");
				PlatFormSendUtils.sendBdDeptUnitMsg(msgParam);
			}
		}
	}
	
	/**
	 * 删除部门下业务单元指定信息
	 * @param param
	 * @param user
	 */
	public void delDptUnit(String param, IUser user){
		
		String pkDeptunit = JsonUtil.getFieldValue(param, "pkDeptunit");
		
		int countPc = DataBaseHelper.queryForScalar("select count(1) from bd_res_pc where pk_deptunit=?", Integer.class, pkDeptunit);
		if(countPc > 0){
			throw new BusException("已被计算机工作站引用，无法删除！");
		}
		int countSchPlan = DataBaseHelper.queryForScalar("select count(1) from sch_plan_week where pk_deptunit=?", Integer.class, pkDeptunit);
		if(countSchPlan > 0){
			throw new BusException("已被排班计划引用，无法删除！");
		}
		int countSch = DataBaseHelper.queryForScalar("select count(1) from sch_sch where pk_deptunit=?", Integer.class, pkDeptunit);
		if(countSch > 0){
			throw new BusException("已被排班引用，无法删除！");
		}
		
		DataBaseHelper.execute("delete from bd_dept_unit where pk_deptunit=?", pkDeptunit);
	}
	
	/**
	 * 查询待选参数
	 * 001002003037
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdSysparamTemp> getWaitParam(String param,IUser user){
		Map<String, String> paramVol = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<BdSysparamTemp> vol = resMapper.getWaitParam(paramVol.get("pkPc"),paramVol.get("name"));		
		return vol;
	}
	
	/**
	 * 查询已选参数
	 * 001002003038
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdSysparamTemp> getSelectedParam(String param,IUser user){
		Map<String, String> paramVol = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<BdSysparamTemp> vol = resMapper.getSelectedParam(paramVol.get("pkPc"),paramVol.get("name"));
		return vol;
	}
	
	
	/**
	 * 001002003032
	 * 查询药房窗口
	 */
	@SuppressWarnings("unchecked")
	public List<BdDeptUnit> getDeptUnit(String param, IUser user) {
		//String pkdept = JsonUtil.getFieldValue(param, "pkDept");  
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		List<BdDeptUnit> deptUnitList = resMapper.getDeptUnit(map);//查询药房窗口
		return deptUnitList;
	}
	
	/**
	 * 查询窗口关联科室
	 * 001002003041
	 * @param pkDeptunit
	 * @return
	 */
	public List<BdDeptUnitObj> getAssociateDept(String param, IUser user) {
		String pkDeptunit=JsonUtil.getFieldValue(param, "pkDeptunit");
		String deptName=JsonUtil.getFieldValue(param, "deptName");
		List<BdDeptUnitObj> deptUnitObjList = resMapper.getDeptUnitObj(pkDeptunit,deptName);
		return deptUnitObjList;
	}
	
	/**
	 * 查询窗口关联的备选科室
	 * 001002003042
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOuDept> qryOptDept(String param, IUser user){
		Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("pkOrg", ((User)user).getPkOrg());
    	paramMap.put("flagOp", "1");
    	paramMap.put("flagActive", "1");
    	//paramMap.put("dtDepttype", "01");
    	String deptParam = ApplicationUtils.getSysparam("BD0003", false);
		if("1".equals(deptParam))
			return deptMapper.getDeptAndTypeInfos(paramMap);
		else 
			return deptMapper.getDeptInfos(paramMap);
	}
	
	/**
	 * 保存窗口关联的科室
	 * 001002003043
	 * @param param
	 * @param user
	 */
	public void saveDeptUnitObj(String param, IUser user){
		List<BdDeptUnitObj> duoList=JsonUtil.readValue(param, new TypeReference<List<BdDeptUnitObj>>() {
		});
		DataBaseHelper.execute("delete from bd_dept_unit_obj where pk_deptunit=?", new Object[]{duoList.get(0).getPkDeptunit()});
		if(duoList.size() == 1 && StringUtils.isBlank(duoList.get(0).getPkDept())){
			return;
		}
		User u=(User)user;
		for(BdDeptUnitObj b : duoList){
			b.setPkUnitobj(NHISUUID.getKeyId());
			b.setPkOrg(u.getPkOrg());
			b.setCreateTime(new Date());
			b.setCreator(u.getPkEmp());
			b.setTs(new Date());
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdDeptUnitObj.class), duoList);
	}

	public void modDeptUnitObj(String param, IUser user){
		List<Map<String,Object>> duoList=JsonUtil.readValue(param, List.class);
		if(duoList.stream().filter(v -> v==null || StringUtils.isBlank(MapUtils.getString(v,"pkUnitobj"))
				|| StringUtils.isBlank(MapUtils.getString(v,"levelNum"))).count() >0){
			throw new BusException("请传入详细参数");
		}
		DataBaseHelper.batchUpdate("update BD_DEPT_UNIT_OBJ set level_num=:levelNum where pk_unitobj=:pkUnitobj",duoList);
	}

	/**
	 * 删除窗口关联的科室
	 * 001002003044
	 * @param param
	 * @param user
	 */
	public void delDeptUnitObj(String param,IUser user){
		List<BdDeptUnitObj> delRowDeptList = JsonUtil.readValue(param, new TypeReference<List<BdDeptUnitObj>>() {
		});
		for(BdDeptUnitObj bd : delRowDeptList){
			DataBaseHelper.deleteBeanByPk(bd);
		}
	}
	
	//查询挂号分配列表：001002003048
	public List<Map<String, Object>> getPcDeptByPc(String param,IUser user){
		
		String pkPc = JsonUtil.getFieldValue(param, "pkPc");
		
		return resMapper.getPcDeptByPc(pkPc);
	}
	
	//查询未分配的分诊台列表：001002003049
	public List<Map<String, Object>> qryNotUsePcDept(String param,IUser user){
		
		User u = (User)user;
		
		String pkPc = JsonUtil.getFieldValue(param, "pkPc");
		
		return resMapper.qryNotUsePcDept(pkPc,u.getPkOrg());
	}
	
	//查询已分配的分诊台列表：001002003050
	public List<Map<String, Object>> qryAlUsePcDept(String param,IUser user){
		
		String pkPc = JsonUtil.getFieldValue(param, "pkPc");
		
		return resMapper.qryAlUsePcDept(pkPc);
	}
	
	//查询本机构工作站编码最大值：001002003051
	public Integer qryMaxCode(String param,IUser user){
		
		User u = (User)user;
		
		String maxCode = resMapper.qryMaxCode(u.getPkOrg());
		
		Integer code;
		try{
			code = Integer.valueOf(maxCode) + 1;
		}catch(Exception e){
			code = 0;
		}
		return code;
	}
	
	//门诊诊室设置查询科室列表：001002003052
	public List<Map<String, Object>> qryClinicSetDept(String param,IUser user){
		
		User u = (User)user;
		
		return resMapper.qryClinicSetDept(u.getPkOrg());
	}
	
	//门诊诊室设置查询查询科室下的诊室信息：001002003053
	public List<Map<String, Object>> qryClinicSetDeptUnit(String param,IUser user){
		
		String pkDept = JsonUtil.getFieldValue(param, "pkDept");
		
		return resMapper.qryClinicSetDeptUnit(pkDept);
	}


    /**
     * 批量设置床位费、包床费
     * @param param
     * @param user
     */
	public void EditResBeds(String param,IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        if( null == paramMap.get("pkBedStr") || CommonUtils.isEmptyString(paramMap.get("pkBedStr").toString())){
            throw new BusException("未获取床位主键！");
        };
//        if( null == paramMap.get("pkItem") || CommonUtils.isEmptyString(paramMap.get("pkItem").toString())){
//            throw new BusException("未获取床位费主键！");
//        };
//        if( null == paramMap.get("pkItemAdd") || CommonUtils.isEmptyString(paramMap.get("pkItemAdd").toString())){
//            throw new BusException("未获取包床费主键！");
//        };
        String pkBedStr = paramMap.get("pkBedStr").toString();
        String pkItem = paramMap.get("pkItem").toString();
        String pkItemAdd = paramMap.get("pkItemAdd").toString();
        String[] pkBedArr = pkBedStr.split(",");
        String pkBeds = "'";
        for(int i=0; i<pkBedArr.length; i++){
            if(i < pkBedArr.length -1){
                pkBeds += pkBedArr[i]+"','";
            }else{
                pkBeds += pkBedArr[i]+"'";
            }
        }
		String updattSql = "";
        if("".equals(pkItem) && !"".equals(pkItemAdd)){
			updattSql = "update BD_RES_BED set PK_ITEM_ADD = '"+pkItemAdd+"' where PK_BED in ("+pkBeds+") ";
			DataBaseHelper.update(updattSql);
		}else if(!"".equals(pkItem) && "".equals(pkItemAdd)){
			updattSql = "update BD_RES_BED set PK_ITEM = '"+pkItem+"' where PK_BED in ("+pkBeds+") ";
			DataBaseHelper.update(updattSql);
		}else if(!"".equals(pkItem) && !"".equals(pkItemAdd)){
			updattSql = "update BD_RES_BED set PK_ITEM = ?,PK_ITEM_ADD = ? where PK_BED in ("+pkBeds+") ";
			DataBaseHelper.update(updattSql,pkItem,pkItemAdd);
		}
    }

	/**
	 * 根据收费项目包床获取床位费用信息
	 * @param param  pkItem （收费项目主键）
	 * @param param  pkItemAdd （包床项目主键）
	 * @param user （当前登录用户信息）
	 * @return  bdItemBed （床位收费项目明细信息）
	 */
	public  List<Map<String, Object>> ReqBdItemBeds(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkItem = paramMap.get("pkItem").toString();
		String pkItemAdd = paramMap.get("pkItemAdd").toString();
		String reqSlq = "SELECT item.CODE, item.NAME, item.spec, 1 quan, unit.NAME unit, PRICE, PRICE amount, 0 flag_add " +
						"FROM BD_ITEM item LEFT JOIN BD_UNIT unit ON unit.PK_UNIT = item.PK_UNIT " +
						"WHERE FLAG_SET = '0'  AND PK_ITEM = '"+pkItem+"' " +
						"UNION all " +
						"SELECT item.CODE, item.NAME, item.spec, s.QUAN, unit.NAME, PRICE, s.QUAN * item.PRICE amount, 0 flag_add " +
						"FROM BD_ITEM item LEFT JOIN BD_UNIT unit ON unit.PK_UNIT = item.PK_UNIT INNER JOIN bd_item_set s ON s.PK_ITEM_CHILD = item.PK_ITEM " +
						"WHERE s.PK_ITEM = '"+pkItem+"'  " +" and item.FLAG_SET='0' and s.DEL_FLAG='0'"+
						"UNION all " +
						"SELECT item.CODE, item.NAME, item.spec, 1 QUAN, unit.NAME, PRICE, PRICE amount, 1 flag_add " +
						"FROM BD_ITEM item LEFT JOIN BD_UNIT unit ON unit.PK_UNIT = item.PK_UNIT " +
						"WHERE FLAG_SET = '0' AND PK_ITEM = '"+pkItemAdd+"' " +
						"UNION all " +
						"SELECT item.CODE, item.NAME, item.spec, s.QUAN, unit.NAME, PRICE, s.QUAN * item.PRICE amount, 1  flag_add " +
						"FROM BD_ITEM item LEFT JOIN BD_UNIT unit ON unit.PK_UNIT = item.PK_UNIT INNER JOIN bd_item_set s ON s.PK_ITEM_CHILD = item.PK_ITEM " +
						"WHERE s.PK_ITEM = '"+pkItemAdd+"'  "+"  and item.FLAG_SET='0' and s.DEL_FLAG='0' ";

		List<Map<String, Object>> bedItemList = DataBaseHelper.queryForList(reqSlq);
		return bedItemList;

	}
}