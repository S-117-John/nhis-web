package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.modules.dao.build.BuildSql;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnIpPressMapper;
import com.zebone.nhis.cn.ipdw.vo.CnConsultApplyVO;
import com.zebone.nhis.cn.ipdw.vo.CnConsultResponseVO;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.cn.ipdw.CnConsultApply;
import com.zebone.nhis.common.module.cn.ipdw.CnConsultResponse;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ex.pub.support.QueryOrdWfService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnConsultApplyService {
	
	@Autowired      
	private BdSnService bdSnService;  
	
	@Autowired
	private CnIpPressMapper cnIpPressMapper;
	
	@Autowired
	private QueryOrdWfService queryOrdWfService;
	
	@Autowired
	private CnPubService cnPubService;
	
	public List<CnOrder> saveOrUpdateCnConsultApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnConsultApplyVO> applys = JsonUtil.readValue(param,new TypeReference<List<CnConsultApplyVO>>() {});	
		return saveOrUpdateCnConsultApply(applys,user);
	}
	/**
	 * 会诊申请保存(提取方法)
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<CnOrder> saveOrUpdateCnConsultApply(List<CnConsultApplyVO> applys,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnOrder> cnOrderList=new ArrayList();
		if(applys!=null && applys.size() > 0){
        vaildOrdts(applys);		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		Date dt = new Date();
		Date dd = cnPubService.getOutOrdDate(applys.get(0).getOrder().getPkPv());
		if (dd != null && dd.compareTo(dt) < 0) {
			dt = dd;
		}
		for(CnConsultApplyVO cca : applys){
			CnConsultApply ccay = new CnConsultApply();
			BeanUtils.copyProperties(ccay, cca);
			
			CnOrder co = cca.getOrder();
			if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
			if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;
			
			fillCnOrder(co,user);			
			//co.setDateStart(new Date());					
			//co.setTs(new Date());
			co.setTs(dt);
			co.setDosage(1.0);
			if(StringUtils.isBlank(co.getPkCnord())){
				if(StringUtils.isEmpty(co.getCodeApply())){
					String code = ApplicationUtils.getCode("0405");
					co.setCodeApply(code);
				}
				
				DataBaseHelper.insertBean(co);
				ccay.setPkCnord(co.getPkCnord());
				ccay.setEuStatus("0");
				ccay.setDateApply(dt);
				if(StringUtils.isBlank(ccay.getPkCons())) DataBaseHelper.insertBean(ccay);
				else DataBaseHelper.updateBeanByPk(ccay,false);
				
				List<CnConsultResponseVO> resptemp = cca.getResps();
				List<CnConsultResponseVO> resp =new ArrayList<CnConsultResponseVO>();
				for(CnConsultResponseVO cnConsultResponseVO :resptemp){
					cnConsultResponseVO.setPkOrgRep(cca.getPkOrg());
					resp.add(cnConsultResponseVO);
				}
				saveRespProcedure(resp,ccay.getPkCons());
				
				CnOrder cnorder=DataBaseHelper.queryForBean(
						"select * from cn_order where pk_cnord = ? and del_flag = '0'",
						CnOrder.class, co.getPkCnord());
				cnOrderList.add(cnorder);
				
				continue;
			}
			
			DataBaseHelper.updateBeanByPk(co,false);
			ccay.setPkCnord(co.getPkCnord());
			ccay.setEuStatus("0");
			DataBaseHelper.updateBeanByPk(ccay,false);	
			
			List<CnConsultResponseVO> resptemp = cca.getResps();
			List<CnConsultResponseVO> resp =new ArrayList<CnConsultResponseVO>();
			for(CnConsultResponseVO cnConsultResponseVO :resptemp){
				cnConsultResponseVO.setPkOrgRep(cca.getPkOrg());
				resp.add(cnConsultResponseVO);
			}
			saveRespProcedure(resp,ccay.getPkCons());
			
			CnOrder cnorder=DataBaseHelper.queryForBean(
					"select * from cn_order where pk_cnord = ? and del_flag = '0'",
					CnOrder.class, co.getPkCnord());
			cnOrderList.add(cnorder);
		}
		}
		return cnOrderList;
	}
	private void vaildOrdts(List<CnConsultApplyVO> applys) {
		List<CnOrder> list = new ArrayList<CnOrder>();
		for(CnConsultApplyVO cca : applys){	
			CnOrder co = cca.getOrder();
			if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
			if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;			
			if(!StringUtils.isBlank(co.getPkCnord())){
				list.add(co);
			}
		}
		if(list.size()>0) cnPubService.vaildUpdateCnOrdts(list);
	}

	private void fillCnOrder(CnOrder co,IUser user){
		co.setEuAlways("1");
		if(StringUtils.isEmpty(co.getEuStatusOrd())) co.setEuStatusOrd("0");
		
		co.setQuan(new Double(1));
		
		User u = (User)user;			
		/*Map para = new HashMap();
		para.put("code", "02");
		//para.put("pkDept", "72eb098f8b1e4e68b88c43fcdc37a694"); //TODO 测试硬编码
		para.put("pkDept", u.getPkDept());*/
		
		/*String 	deptExec = null;
		List<Map<String,Object>> depts = cnIpPressMapper.qryDeptExec(para);
		
		if(null == co.getDateSign()) co.setDateSign(new Date());
		if(null == co.getDateEnter()) co.setDateEnter(new Date());
		
		if(null != depts && depts.size() > 0){
			Map<String,Object> m = depts.get(0);
			deptExec = (String)m.get("pkDept");
		}
		co.setPkDeptExec(deptExec);*/
		
		//co.setPkDept(u.getPkDept());
		if(StringUtils.isBlank(co.getPkCnord())){
			co.setPkEmpInput(u.getPkEmp());
			co.setNameEmpInput(u.getUserName());
		}
		co.setEuPvtype("3");		
		
		List<Map<String,Object>> ds = cnIpPressMapper.qryBussinessLine(co.getPkDept());
		if(ds.size() > 0 && null != ds.get(0).get("pkDept") && StringUtils.isEmpty(co.getPkDeptExec())) co.setPkDeptExec((String)ds.get(0).get("pkDept"));
		
//		co.setPkEmpOrd(u.getPkEmp());
		co.setDescOrd(co.getNameOrd());
		
		//co.setPkDeptExec(co.getPkDept()); //TODO 测试硬编码，等医嘱流向出来再写
		
		co.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
//		co.setNameEmpOrd(u.getUserName()); //签署时写入
//		co.setPkEmpOrd(u.getPkEmp());
		co.setFlagDurg("0");
		
		/*BdWfOrdArguVo bwo = new BdWfOrdArguVo();
		bwo.setPkOrg(u.getPkOrg());
		bwo.setEuPvtype(co.getEuPvtype());
		bwo.setOrdrecur(co.getEuAlways());
		bwo.setOrderType(co.getCodeOrdtype());	
		if(StringUtils.isEmpty(bwo.getPkOrg()) || bwo.getPkOrg().indexOf("~") >= 0 )bwo.setPkOrg(co.getPkOrg());
		
		BdWfOrdArguDeptVo bwd = queryOrdWfService.exce(bwo);
		
		if(null != bwd){
			co.setPkDeptExec(bwd.getPkDept());
			co.setPkOrgExec(bwd.getPkOrgExec());
		}*/
		
		
		
		if(null == co.getOrdsn() || co.getOrdsn().intValue() == 0){
			co.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, u));
			co.setOrdsnParent(co.getOrdsn());
		}
		
		co.setFlagBase("0");
		co.setFlagBl("1");
		co.setFlagCp("0");
		co.setFlagDoctor("1");
		if(CommonUtils.isEmpty(co.getFlagEmer()))
			co.setFlagEmer("0");
		co.setFlagFirst("0");
		co.setFlagFit("0");
		co.setFlagMedout("0");
		co.setFlagNote("1");
		co.setFlagPrev("0");
		co.setFlagPrint("0");
		co.setFlagSelf("0");
		co.setFlagStop("0");
		co.setFlagThera("0");
		co.setFlagEraseChk("0");	
		
		if(null == co.getDateStart()) co.setDateStart(new Date());
		if(null == co.getDateEnter()) co.setDateEnter(new Date());	
		if(null == co.getDateEffe())  co.setDateEffe(new Date());
		
		co.setFlagErase("0");
		co.setFlagStopChk("0");
	}
	
	/**
	 * 查询本次住院的会诊记录
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<CnConsultApplyVO> getPatientReport(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnConsultApplyVO> ret = new ArrayList<CnConsultApplyVO>();
		
		String pv = JsonUtil.getFieldValue(param, "pv");		
		String sql = "select o.*,c.DATE_APPLY,c.DATE_CONS,c.DT_CONLEVEL,c.EU_STATUS,c.EU_TYPE,c.ILL_SUMMARY,c.PK_CONS,c.PK_DEPT,c.PK_DEPT_NS,c.REASON,c.examine,c.diagname,c.objective   "
				+ "     from cn_order o,cn_consult_apply c "
				+ " where o.pk_pv = '"+pv+"' and o.pk_cnord=c.pk_cnord and (c.del_flag is null or c.del_flag <> '1') "
						+ "order by c.date_apply desc";
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		for(Map<String,Object> map : ps){
			CnConsultApplyVO bv = new CnConsultApplyVO();
			BeanUtils.copyProperties(bv, map);	
			
			if(null == bv.getOrder()){
				CnOrder co = new CnOrder();
				BeanUtils.copyProperties(co, map);	
				bv.setOrder(co);
			}
			
			bv.setResps(loadResponseProcedure(bv.getPkCons()));			
			ret.add(bv);
		}		
	    
		return ret;	
	}
	
	/**
	 * 删除申请单
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		CnConsultApplyVO ccao = JsonUtil.readValue(param,CnConsultApplyVO.class);
		delApply(ccao);
	}
	/**
	 * 删除申请单(提取方法)
	 * @param param
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delApply(CnConsultApplyVO param) throws IllegalAccessException, InvocationTargetException{
		CnConsultApplyVO ccao = param;
		if(null == ccao) return;
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		CnConsultApply cca = new CnConsultApply();
		BeanUtils.copyProperties(cca, ccao);		
		CnOrder co = ccao.getOrder();
		List<CnOrder> list = new ArrayList<CnOrder>();
		list.add(co);
		cnPubService.vaildUpdateCnOrdts(list);
		co.setDelFlag("1");
		cca.setDelFlag("1");
		
		List<CnConsultResponseVO> resp = loadResponseProcedure(cca.getPkCons());
		
		if(null != resp){
			for(CnConsultResponseVO vo : resp){
				CnConsultResponse ccv = new CnConsultResponse();
				ccv.setPkConsrep(vo.getPkConsrep());
				DataBaseHelper.deleteBeanByPk(ccv);
			}
		}
		//saveRespProcedure(resp,null);
		
		//DataBaseHelper.updateBeanByPk(cca, false);
		//DataBaseHelper.updateBeanByPk(co, false);
		
		DataBaseHelper.deleteBeanByPk(co);
		DataBaseHelper.deleteBeanByPk(cca);
	}
	/**
	 * 设置申请单的签署状态
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void signApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnConsultApplyVO> applys = JsonUtil.readValue(param,new TypeReference<List<CnConsultApplyVO>>() {});		
		
		if(null == applys || applys.size() <= 0) return;
	    vaildOrdts(applys);	
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
 		
		List<CnOrder> cpOrderList = new ArrayList<CnOrder>();
		List<CnOrder> messageOrds = new ArrayList<CnOrder>();
		for(CnConsultApplyVO cca : applys){
			CnOrder co = cca.getOrder();			
			if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
			if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;
			if(StringUtils.isNotEmpty((cca.getPkCpexp()))){
				co.setPkCpexp(cca.getPkCpexp());
				co.setPkCprec(cca.getPkCprec());
				co.setExpNote(cca.getExpNote());
				cpOrderList.add(co);
			}
			fillCnOrder(co,user);			
			co.setFlagSign("1");
			User u = (User)user;
			co.setNameEmpOrd(u.getUserName()); //签署时写入
			co.setPkEmpOrd(u.getPkEmp());
			
			if(StringUtils.isEmpty(co.getEuStatusOrd()) || "0".equals(co.getEuStatusOrd())) co.setEuStatusOrd("1");
			if(null == co.getDateSign()) co.setDateSign(new Date());
			co.setTs(new Date());
			if(StringUtils.isBlank(co.getPkCnord())){				
				if(StringUtils.isEmpty((co.getCodeApply()))){
					String code = ApplicationUtils.getCode("0405");
					co.setCodeApply(code);
				}
				//co.setDateStart(new Date());
			
				DataBaseHelper.insertBean(co); 
			}
			else DataBaseHelper.updateBeanByPk(co,false);
			
			messageOrds.add(co);
			
			CnConsultApply ccao = new CnConsultApply();
			BeanUtils.copyProperties(ccao, cca);
			if(null == ccao.getEuStatus() || "0".equals(ccao.getEuStatus()) || "1".equals(ccao.getEuStatus())) ccao.setEuStatus("0");
			
			ccao.setPkCnord(co.getPkCnord());
			
			if(StringUtils.isBlank(ccao.getPkCons())) DataBaseHelper.insertBean(ccao);
			else DataBaseHelper.updateBeanByPk(ccao,false);		
			
			List<CnConsultResponseVO> resp = cca.getResps();
			saveRespProcedure(resp,ccao.getPkCons());
			
		}
		if(cpOrderList.size()>0) cnPubService.recExpOrder(false, cpOrderList, (User)user);
		cnPubService.sendMessage("新医嘱", messageOrds,false);
	}
	
	/**
	 * 申请单的作废
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void wasteApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		CnConsultApplyVO ccao = JsonUtil.readValue(param,CnConsultApplyVO.class);
		CnOrder o = ccao.getOrder();
		User u = (User)user;
		
		o.setFlagErase("1");
		o.setPkEmpErase(u.getPkEmp());
		o.setNameEmpErase(user.getUserName());
		o.setDateErase(new Date());
		o.setEuStatusOrd("9");
		
		List<CnConsultResponseVO> resp = loadResponseProcedure(ccao.getPkCons());
		
		if(null != resp){
			for(CnConsultResponseVO vo : resp) vo.setDelFlag("1");
		}
		saveRespProcedure(resp,null);
		
		DataBaseHelper.updateBeanByPk(o,false);	
		
		List<CnOrder> ords = new ArrayList<CnOrder>();
		ords.add(o);
		if(ords.size() > 0){
			Map<String,Object> pkOrdMap = new HashMap<String,Object>();
			pkOrdMap.put("pk_cnord", resp.get(0).getPkCnord());
			String apSql = "SELECT ord.name_ord, dept.name_dept, op.EU_STATUS AS op_status "+ 
					   "FROM CN_ORDER ord "+ 
					   "INNER JOIN BD_OU_DEPT dept ON dept.PK_DEPT = ord.PK_DEPT_NS AND dept.DEL_FLAG = '0' "+
					   "LEFT JOIN CN_CONSULT_APPLY  op ON op.PK_CNORD = ord.PK_CNORD AND op.DEL_FLAG = '0'" +
					   "WHERE ord.PK_CNORD IN (:pk_cnord) and ord.ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord in (:pk_cnord) ) AND ord.DEL_FLAG = '0' "+
					   "AND ord.FLAG_ERASE = '0'";
			List<Map<String,Object>> apList = DataBaseHelper.queryForList(apSql, pkOrdMap);
			String throwStr="";
			for(Map<String,Object> map :apList){
				if(map.get("opStatus") != null && "1".compareTo(map.get("opStatus").toString()) <= 0)
					throwStr+="["+map.get("nameOrd")+"]需要["+map.get("nameDept")+"]先取消提交才能作废!\n";
		}
		if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);
		BloodService.cancelOrder(ords, user);
		cnPubService.sendMessage("作废医嘱", ords,false);
	}
}	
	/**
	 * 获取会诊申请单的应答列表
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<CnConsultResponseVO> loadResponse(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		CnConsultApplyVO ccao = JsonUtil.readValue(param,CnConsultApplyVO.class);
			
		return loadResponseProcedure(ccao.getPkCons());
	}
	
	/**
	 * 获取会诊申请单的应答列表的具体执行方法
	 * @param pkCons
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private List<CnConsultResponseVO> loadResponseProcedure(String pkCons) throws IllegalAccessException, InvocationTargetException{
		String sql = "select resp.pk_org_rep,resp.pk_dept_rep, resp.pk_emp_rep,resp.pk_cons,resp.pk_consrep, dept.name_dept, "
                     +" emp.name_emp name_emp_rep,resp.date_rep,resp.con_reply,resp.con_advice "
                     + " from bd_ou_org org, bd_ou_dept dept, cn_consult_response resp,bd_ou_employee emp  "
				     + " where resp.pk_org_rep = org.pk_org and resp.pk_dept_rep = dept.pk_dept  and resp.pk_cons ='"+pkCons+"' and emp.pk_emp =  resp.pk_emp_rep"
				     +"  and (resp.del_flag<>1 or resp.del_flag is null)";
		
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		if(null == ps || ps.size() <= 0) return null;
		
		List<CnConsultResponseVO> ret = new ArrayList<CnConsultResponseVO>();
		
		for(Map<String,Object> map : ps){
			CnConsultResponseVO ccrv = new CnConsultResponseVO();
			BeanUtils.copyProperties(ccrv, map);
			ret.add(ccrv);
		}
		
		return ret;
		
	}
	
	/**
	 * 保存会诊申请单的应答列表
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void saveResponse(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnConsultResponseVO> applys = JsonUtil.readValue(param,new TypeReference<List<CnConsultResponseVO>>() {});	
		saveRespProcedure(applys,null);
		
	}
	
	/**
	 * 应答列表保存的具体实行类
	 * @param resp
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private void saveRespProcedure(List<CnConsultResponseVO> resp,String pk) throws IllegalAccessException, InvocationTargetException{
		if(null == resp) return;
		
		for(CnConsultResponseVO vo : resp){
			CnConsultResponse ccr = new CnConsultResponse();
			BeanUtils.copyProperties(ccr, vo);
			
			ccr.setDateInput(null);
			ccr.setDateRep(null);
			
			if(!StringUtils.isBlank(pk)) ccr.setPkCons(pk);
			
			if(StringUtils.isBlank(ccr.getPkConsrep())) DataBaseHelper.insertBean(ccr);
			else DataBaseHelper.updateBeanByPk(ccr,false);		
		}
		
	}
	
	/**
	 * 查询单条会诊记录(用于打印)
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public CnConsultApplyVO getSingleConsult(String param,IUser user) throws IllegalAccessException, InvocationTargetException{	
		String pkCons = JsonUtil.getFieldValue(param, "pkCons");
		String sql = "select * from cn_order o inner join cn_consult_apply c on o.pk_cnord=c.pk_cnord  where c.pk_cons='"+pkCons+"'";
		
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		if(null == ps || ps.size() <= 0) return null;
		
        ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		for(Map<String,Object> map : ps){
			CnConsultApplyVO bv = new CnConsultApplyVO();
			BeanUtils.copyProperties(bv, map);	
			
			if(null == bv.getOrder()){
				CnOrder co = new CnOrder();
				BeanUtils.copyProperties(co, map);	
				bv.setOrder(co);
			}
			
			bv.setResps(loadResponseProcedure(bv.getPkCons()));		
			return bv;
		}
		
		return null;		
	}
	
	/**
	 * 根据人员查询科室
	 * 004004005009
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOuDept> queryEmpAndDept(String param,IUser user){
		CnConsultResponseVO consultApplyVO = JsonUtil.readValue(param, CnConsultResponseVO.class);
		Map<String, String> params = new HashMap<String,String>();
		params.put("pkEmpRep", consultApplyVO.getPkEmpRep());
		List<BdOuDept> list = cnIpPressMapper.queryEmpAndDept(params);
		return list;
		
	}

	/**
	 * 门诊会诊应答修改
	 * @param param
	 * @param user
	 */
	public void modMeConsultResponse(String param,IUser user){
		CnConsultResponse responseParam = JsonUtil.readValue(param, new TypeReference<CnConsultResponse>() {});
		if(responseParam == null){
			throw  new BusException("请传入参数");
		}
		String pkCons = null;
		if(StringUtils.isBlank(pkCons = responseParam.getPkCons())){
			throw  new BusException("请传入参数pkCons");
		}

		//只从前台保存部分属性，重新new对象
		User u = (User)user;
		CnConsultResponse response = new CnConsultResponse();
		response.setPkCons(pkCons);
		response.setFlagRep(EnumerateParameter.ONE);
		response.setFlagFinish(EnumerateParameter.ONE);
		response.setPkEmpRep(u.getPkEmp());
		response.setNameEmpRep(u.getNameEmp());
		response.setDateRep(responseParam.getDateRep());
		response.setPkEmpInput(u.getPkEmp());
		response.setNameEmpInput(u.getNameEmp());
		response.setDateInput(new Date());
		response.setPkDeptRep(u.getPkDept());
		response.setConReply(responseParam.getConReply());
		response.setConAdvice(responseParam.getConAdvice());
		DataBaseHelper.updateBeanByWhere(response," pk_cons=:pkCons and pk_dept_rep=:pkDeptRep",false);
		//状态先改为2，若都已经应答了，就改为3
		DataBaseHelper.update("update cn_consult_apply set eu_status='2' where pk_cons=? " ,new Object[]{pkCons});
		DataBaseHelper.update("update cn_consult_apply set eu_status='3' where pk_cons=? " +
				" and not Exists(Select 1 From CN_CONSULT_RESPONSE t Where t.PK_CONS =cn_consult_apply.PK_CONS And nvl(t.FLAG_finish,'0') = '0')",new Object[]{pkCons});
	}
	
}
