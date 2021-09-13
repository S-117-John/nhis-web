package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.aop.OrderVoid;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnNoticeService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnOpMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CnSignCaParam;
import com.zebone.nhis.cn.ipdw.vo.OpApplyQryParam;
import com.zebone.nhis.cn.ipdw.vo.OpHeadSignParam;
import com.zebone.nhis.cn.ipdw.vo.OpHeadSignQryParam;
import com.zebone.nhis.cn.ipdw.vo.OpPressVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpInfect;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.cn.ipdw.CpRecExpParam;
import com.zebone.nhis.common.module.cn.ipdw.ExOpSch;
import com.zebone.nhis.common.module.sch.appt.SchApptOp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnOpService {
	static org.apache.logging.log4j.Logger log = LogManager.getLogger(CnOpService.class);
 
	@Autowired      
	private CnOpMapper cnOpMapper;  
	@Autowired      
	private BdSnService bdSnService;  
	@Autowired      
	private CnPubService cnPubService;  

	@Autowired
	private CnNoticeService cnNoticeService;

	//private static final String FREQ_TMP = "once";
	//private static final String ORD_OP = "OP99999";		//手术医嘱的pk，后期参数化
	private static final String CODE_OP = "DEF99999";		//手术医嘱的code,约定
	private static SimpleDateFormat dateformat =  new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 获取手术申请使用的字典
	 * 麻醉方法、无菌程度、手术级别、手术体位、手术部位
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdDefdoc> getOpDictList(String param , IUser user){
		return cnOpMapper.getOpDictList();
	}
	
	/**
	 * 保存手术申请
	 * @param param
	 * @param user
	 */
	public void saveOpApplyList(String param , IUser user){
		List<CnOpApply> applyList = JsonUtil.readValue(param, new TypeReference<List<CnOpApply>>(){});
		User u = (User)user;
		//vaildOrdTs(applyList);
		Date dt = new Date();
		Date dd = cnPubService.getOutOrdDate(applyList.get(0).getPkPv());
		if (dd != null && dd.compareTo(dt) < 0) {
			dt = dd;
		}
		for(CnOpApply apply : applyList){
			CnOrder order = null;
			if( apply.getPkCnord() != null)
				order = cnOpMapper.selectOrder(apply.getPkCnord());
			else
				order = new CnOrder();
			if(Constants.RT_NEW.equals(apply.getRowStatus()) && (apply.getPkOrdop()==null || "".equals(apply.getPkOrdop())) ){
				apply.setPkOrdop(NHISUUID.getKeyId());
				order.setPkCnord(NHISUUID.getKeyId());
				apply.setPkCnord(order.getPkCnord());
				order.setPkPv(apply.getPkPv());
				order.setPkPi(apply.getPkPi());
				apply.setPkOrg(u.getPkOrg());
				order.setPkOrg(apply.getPkOrg());
				order.setCodeOrdtype("04");					//手术类型
				order.setEuAlways("1");						//临时
				//order.setPkOrd(ORD_OP);						//所有手术用同一个医嘱
				order.setCodeOrd(CODE_OP);
				order.setNameOrd(apply.getOpName());				//手术医嘱名称带格式
				order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));					
				order.setCodeApply(apply.getCodeApply());				
				order.setDescOrd(order.getNameOrd());	
				order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
				order.setQuanCg(1.0);
				order.setQuan(1.0); 			//医嘱的数量
				order.setDosage(1.0);
				order.setEuStatusOrd("0");		//保存
				order.setDateEnter(dt);			//暂用服务器时间，db时间后期处理
				order.setDateStart(dt);
				apply.setDateApply(dt);
				order.setPkDept(u.getPkDept());
				order.setPkDeptNs(apply.getPkDeptNs());		
				order.setPkEmpInput(u.getPkEmp());
				order.setNameEmpInput(u.getUserName());
				order.setDateSign(dt);
//				order.setPkEmpOrd(u.getPkEmp());   //签署时写入
//				order.setNameEmpOrd(u.getUserName());
				order.setEuPvtype("3");			//住院患者
				order.setOrdsnParent(order.getOrdsn());
				order.setPkOrgExec(apply.getPkOrgExec());
				order.setPkDeptExec(apply.getPkDeptExec());
				order.setFlagDoctor("1");
				order.setFlagErase("0");
				order.setFlagStopChk("0");
				order.setFlagStop("0");
				order.setFlagEraseChk("0");
				order.setFlagFirst(Constants.FALSE);
				order.setFlagDurg(Constants.FALSE);
				order.setFlagSelf(Constants.FALSE);
				order.setFlagNote(Constants.FALSE);
				order.setFlagBase(Constants.FALSE);
				order.setFlagBl(Constants.FALSE);
				order.setFlagStop(Constants.FALSE);
				order.setFlagCp(Constants.FALSE);
				order.setFlagPrint(Constants.FALSE);
				order.setFlagMedout(Constants.FALSE);
				order.setFlagEmer(Constants.FALSE);
				order.setFlagThera(Constants.FALSE);
				order.setFlagPrev(Constants.FALSE);
				order.setFlagFit(Constants.FALSE);
				order.setEuIntern(apply.getEuIntern());
				order.setTs(dt);
				apply.setTs(dt);
				apply.setDelFlag(Constants.FALSE);
				apply.setEuStatus("0");
				order.setNoteOrd(apply.getNote());
				DataBaseHelper.insertBean(apply);
				DataBaseHelper.insertBean(order);
				//cnPubService.updateDateStart(list);
				this.cnOpMapper.removeChildApply(apply.getPkOrdop());
				int i = 1;
				for(CnOpSubjoin child : apply.getSubOpList()){
					if(Constants.RT_REMOVE.equals(child.getRowStatus()))
						continue;
					child.setPkCnopjoin(NHISUUID.getKeyId());
					child.setPkOrdop(apply.getPkOrdop());
					child.setDelFlag(Constants.FALSE);
					child.setSortNo(i);
					child.setTs(dt);
					i++;
					DataBaseHelper.insertBean(child);
				}
			}else if(Constants.RT_UPDATE.equals(apply.getRowStatus())){			
				order.setNameOrd(apply.getOpName());
				order.setDescOrd(order.getNameOrd());
				order.setTs(dt);
				apply.setTs(dt);
				apply.setEuStatus("0");
				order.setNoteOrd(apply.getNote());
				order.setPkOrgExec(apply.getPkOrgExec());
				order.setPkDeptExec(apply.getPkDeptExec());
				DataBaseHelper.updateBeanByPk(apply, false);
				//空值更新
				if(apply.getPkDeptAnae() ==null && apply.getPkEmpAnae() == null){
					DataBaseHelper.update("update cn_op_apply set PK_DEPT_ANAE=:pkDeptAnae,PK_EMP_ANAE=:pkEmpAnae  where pk_cnord=:pkCnord ",apply);
				}
				if(apply.getEuErlevel() == null){
					DataBaseHelper.update("update cn_op_apply set EU_ERLEVEL=:euErlevel where pk_cnord=:pkCnord ",apply);
				}
				DataBaseHelper.updateBeanByPk(order, false);
				this.cnOpMapper.removeChildApply(apply.getPkOrdop());
				int i = 1;
				for(CnOpSubjoin child : apply.getSubOpList()){
					if(Constants.RT_REMOVE.equals(child.getRowStatus()))
						continue;
					child.setPkCnopjoin(NHISUUID.getKeyId());
					child.setPkOrdop(apply.getPkOrdop());
					child.setDelFlag(Constants.FALSE);
					child.setTs(dt);
					child.setSortNo(i);
					i++;
					DataBaseHelper.insertBean(child);
				}
				
			}else if(Constants.RT_REMOVE.equals(apply.getRowStatus())){
				DataBaseHelper.deleteBeanByPk(apply);
				DataBaseHelper.deleteBeanByPk(order);
				this.cnOpMapper.removeChildApply(apply.getPkOrdop());
			}	
		}
	}
	
	private void vaildOrdTs(List<CnOpApply> applyList) {
		List<CnOrder> ords = new ArrayList<CnOrder>();
		for(CnOpApply apply : applyList){
			if(Constants.RT_UPDATE.equals(apply.getRowStatus()) ||Constants.RT_REMOVE.equals(apply.getRowStatus())){
				CnOrder ord = new CnOrder();
				ord.setPkCnord(apply.getPkCnord());
				ord.setTs(apply.getTs());
				ords.add(ord);
			}
		}
	    if(ords.size()>0)cnPubService.vaildUpdateCnOrdts(ords);
	}

	/**
	 * 获取指定患者的手术申请
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnOpApply> getOpApplyList(String param , IUser user){
		//Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		User u = (User)user;
		//List<CnOpApply> ret = cnOpMapper.getOpApplyList(paramMap.get("pkPv"), u.getPkOrg());
		List<CnOpApply> ret = cnOpMapper.getOpApplyList(paramMap);
		List<String> pkOpList = new ArrayList<String>();
		HashMap<String, CnOpApply> hmApply = new HashMap<String, CnOpApply>();
		for(CnOpApply apply : ret){
			pkOpList.add(apply.getPkOrdop());
			hmApply.put(apply.getPkOrdop(), apply);
			
			String sqlStr="select * from ex_op_sch sch where sch.pk_cnord=? and sch.eu_status='1' and sch.del_flag='0' ";
			ExOpSch exOpSch=DataBaseHelper.queryForBean(sqlStr, ExOpSch.class,new Object[]{apply.getPkCnord()});
			if(exOpSch!=null){
				apply.setExOpSch(exOpSch);
			}
		}
		
		if(pkOpList.size()>0){
			List<CnOpSubjoin> childList = cnOpMapper.getChildApplyList(pkOpList);
			for(CnOpSubjoin child : childList){
				CnOpApply apply = hmApply.get(child.getPkOrdop());
				if(apply!=null)
					apply.getSubOpList().add(child);
			}
		}
		
		
		return ret;
	}
	/**
	 * 撤销手术申请
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@OrderVoid(param = "param")
	public void cancelOpApply(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		CnSignCaParam cnSignCaParam = JsonUtil.readValue(param, CnSignCaParam.class);
		List<String> pkOpList = cnSignCaParam.getPkCnList();
		List<CnSignCa> cnSignCaList = cnSignCaParam.getCnSignCaList();
		
		//暂时没考虑手术执行表（手麻接口）
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<CnOrder> ords = new ArrayList<CnOrder>();
		
		String sql = "select * from cn_order where pk_cnord='";
		for(String pkCnOrd : pkOpList){
			List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql+pkCnOrd+"'");
			if(null == ll || ll.size()<=0) continue;
			
			CnOrder co = new CnOrder();
			BeanUtils.copyProperties(co,ll.get(0));	
			
			if("9".equals(co.getEuStatusOrd())) continue;
			
			co.setFlagErase("1");
			co.setPkEmpErase(user.getId());
			co.setNameEmpErase(user.getUserName());
			co.setDateErase(new Date());
			co.setEuStatusOrd("9");
			co.setFlagBl("1");
			ords.add(co);	
		}
		
		if(ords.size() > 0){
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pk_cnord", pkOpList);
		String opSql = "SELECT ord.name_ord, dept.name_dept, op.EU_STATUS AS op_status "+ 
					   "FROM CN_ORDER ord "+ 
					   "INNER JOIN BD_OU_DEPT dept ON dept.PK_DEPT = ord.PK_DEPT_NS AND dept.DEL_FLAG = '0' "+
					   "LEFT JOIN CN_OP_APPLY op ON op.PK_CNORD = ord.PK_CNORD AND op.DEL_FLAG = '0'" +
					   "WHERE ord.PK_CNORD IN (:pk_cnord) and ord.ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord in (:pk_cnord) ) AND ord.DEL_FLAG = '0' "+
					   "AND ord.FLAG_ERASE = '0'";
		List<Map<String,Object>> opList = DataBaseHelper.queryForList(opSql, pkOrdMap);
		String throwStr="";
		for(Map<String,Object> map :opList){
			if(map.get("opStatus") != null && "1".compareTo(map.get("opStatus").toString()) <= 0)
				throwStr+="["+map.get("nameOrd")+"]需要["+map.get("nameDept")+"]先取消提交才能作废!\n";
		}
		if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);
		BloodService.cancelApply(ords, user);	
		cnPubService.caRecord(cnSignCaList);
		cnPubService.sendMessage("作废医嘱",ords,false);
		}
		//发送平台消息
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("control", "OC");//操作类型,灵璧作废手术申请使用
		paramMap.put("cancelOpApply", "");//方法名标志
		PlatFormSendUtils.sendOpApplyMsg(paramMap);
	}
	/**
	 * 签署手术申请
	 * @param param
	 */
	public void signOpApply(String param, IUser user){
		CnSignCaParam cnSignCaParam = JsonUtil.readValue(param, CnSignCaParam.class);
		List<String> pkOpLists = cnSignCaParam.getPkCnList();
		if(pkOpLists==null||pkOpLists.size()<=0) return;
		List<String> pkOpList = cnPubService.splitPkTsValidOrd(pkOpLists);
		List<CnSignCa> cnSignCaList = cnSignCaParam.getCnSignCaList();
		List<CpRecExpParam> cpRecExp = cnSignCaParam.getCpRecExp();
		User u = (User)user;
		Date d = new Date();
		signOpApply(pkOpList,u,d);
		updateOpStatus(pkOpList,d);
		//cnOpMapper.signOpApply(pkOpList,u.getPkEmp(),u.getNameEmp());
		cnPubService.caRecord(cnSignCaList);
		cnPubService.recExpOrder(cpRecExp);
		cnPubService.sendMessage(pkOpList,"新医嘱");
	}
	
	/**
	 * 取消签署手术申请
	 * @param param
	 */
	public void cancelSignOpApply(String param, IUser user){
		CnSignCaParam cnSignCaParam = JsonUtil.readValue(param, CnSignCaParam.class);
		List<String> pkOpLists = cnSignCaParam.getPkCnList();
		if(pkOpLists==null||pkOpLists.size()<=0) return;
		List<String> pkOpList = cnPubService.splitPkTsValidOrd(pkOpLists);//截取当前的字符串
		List<CnOrder> cnOrderByPkCnord = null;
		try {
			cnOrderByPkCnord = getCnOrderByPkCnord(pkOpList);//截取后的list，包含医嘱主键
		} catch (Exception e) {
			// TODO: handle exception
		}
		
//		List<CnSignCa> cnSignCaList = cnSignCaParam.getCnSignCaList();
//		List<CpRecExpParam> cpRecExp = cnSignCaParam.getCpRecExp();
		cancelSignOpApply(pkOpList);
//		cnPubService.caRecord(cnSignCaList);
//		cnPubService.recExpOrder(cpRecExp);
//		cnPubService.sendMessage(pkOpList,"新医嘱");
		
		if(cnSignCaParam.getCnSignCaList()!=null && cnSignCaParam.getCnSignCaList().size()>0){
			List<CnSignCa> cnSignCaList = new ArrayList<CnSignCa>();
			cnSignCaList.add(cnSignCaParam.getCnSignCaList().get(0));
			cnPubService.caRecord(cnSignCaList);
		}
		
		
		//手术申请发消息   ORM^O01
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("control", "CA");//操作类型，深度使用，灵璧过滤不处理条件
		paramMap.put("LBcontrol", "NO");//灵璧不推送判断条件
		paramMap.put("pkCnList",pkOpList);
		paramMap.put("cancelSignOpApply", "");//方法名标志
		PlatFormSendUtils.sendOpApplyMsg(paramMap);
	}
	

	private void updateOpStatus(List<String> pkOpList,Date d) {
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pkCnord", pkOpList);
		pkOrdMap.put("ts", d);
		DataBaseHelper.update("update cn_op_apply set ts=:ts,eu_status='0'  where pk_cnord in (:pkCnord) ",pkOrdMap);
	}
	/**
	 * 更新手术申请
	 * @param pkOpList
	 * @param u
	 * @param d
	 */
	public void signOpApply(List<String> pkOpList,User u,Date d) {
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pkCnord", pkOpList);
		pkOrdMap.put("pkEmpOrd", u.getPkEmp());
		pkOrdMap.put("nameEmpOrd", u.getNameEmp());
		pkOrdMap.put("dateSign", d);
		pkOrdMap.put("ts", d);
		DataBaseHelper.update("update cn_order set ts=:ts,eu_status_ord = '1', flag_sign='1' , pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd,date_sign =:dateSign  where ordsn_parent in (select ordsn_parent from cn_order where pk_cnord in (:pkCnord)  )   ",pkOrdMap);
	}
	
	private void cancelSignOpApply(List<String> pkOpList) {
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pkCnord", pkOpList);
		pkOrdMap.put("pkEmpOrd",null);
		pkOrdMap.put("nameEmpOrd", null);
		pkOrdMap.put("dateSign", null);
		DataBaseHelper.update("update cn_order set eu_status_ord = '0', flag_sign='0' , pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd,date_sign =:dateSign  where ordsn_parent in (select ordsn_parent from cn_order where pk_cnord in (:pkCnord)  )   ",pkOrdMap);
	}
	/**
	 * 查询手术安排
	 * @param param		{“codeApply”,”申请单号”}
	 * @param user
	 * @return
	 */
	public List<SchApptOp> getOpApptList(String param , IUser user) throws Exception {
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<SchApptOp> ret = cnOpMapper.getOpApptList(paramMap.get("codeApply"));
		return ret;
	}
	
	/**
	 * 查询手术执行
	 * @param param
	 * @param user
	 * @return
	 */
	public List<?> getOpExecList(String param, IUser user){
		return null;
	}
	
	public static void main(String[] args){
		for(int i=0; i<30; i++)
			System.out.println(NHISUUID.getKeyId());
	}
	
	/**
	 * 删除手术申请
	 * @param param
	 */
	public void removeOpApply(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pkOrdop = paramMap.get("pkOrdop");
		String pkCnordTs = paramMap.get("pkCnord");
	    String pkCnord = cnPubService.splitPkTsValidOrd(pkCnordTs);
		cnOpMapper.removeOpApply(pkOrdop);
		cnOpMapper.removeChildApply(pkOrdop);
		cnOpMapper.removeOpOrder(pkCnord);
		cnOpMapper.removeOpInfect(pkCnord);
	}
	/**
	 * 科主任确认查询手术申请单/审批手术申请单/取消审批手术申请单
	 * @throws Exception 
	 */
	public List<Map<String,Object>> oPHeadSign(String param, IUser user) throws Exception{
		OpHeadSignParam signParam = JsonUtil.readValue(param, OpHeadSignParam.class);
		String operaType = signParam.getOperaType();
         if("1".equals(operaType)){ //审核
			applyHeadSign(signParam.getOpApplys(),user);
		}else if("2".equals(operaType)){
			applyHeadCanelSign(signParam.getOpApplys());
		}
         List<Map<String,Object>> opApplyList = qryOpApplyHeadSign(signParam.getQryParam(),user);
         opApplyList.forEach(opApp ->{
        	 StringBuffer signSbf = new StringBuffer("");
        	 if(StringUtils.isNotBlank(MapUtils.getString(opApp, "descOp"))) {
        		 signSbf.append(String.format("%s、", MapUtils.getString(opApp, "descOp")));//描述
        	 }
        	 if(StringUtils.isNotBlank(MapUtils.getString(opApp, "oplevelName"))) {
        		 signSbf.append(String.format("%s、", MapUtils.getString(opApp, "oplevelName")));//等级
        	 }
        	 if(StringUtils.isNotBlank(MapUtils.getString(opApp, "nameDeptExec"))) {
        		 signSbf.append(String.format("%s、", MapUtils.getString(opApp, "nameDeptExec")));//手术科室
        	 }
        	 if(StringUtils.isNotBlank(MapUtils.getString(opApp, "nameDeptAnae"))) {
        		 signSbf.append(String.format("%s、", MapUtils.getString(opApp, "nameDeptAnae")));//麻醉科室
        	 }
        	 if(StringUtils.isNotBlank(MapUtils.getString(opApp, "note"))) {
        		 signSbf.append(String.format("%s、", MapUtils.getString(opApp, "note")));//备注
        	 }
        	 cnOpMapper.qryCnOpInfect(MapUtils.getString(opApp, "pkCnord")).forEach(infect->{
        		 signSbf.append(String.format("%s:", MapUtils.getString(infect, "infectName")));//感染类疾病
        		 signSbf.append(String.format("%s,", MapUtils.getString(infect, "infresultName")));//检验结果
        	 } );
        	 opApp.put("explainText", signSbf.substring(0,signSbf.length()-1));
         });
        return opApplyList;
	}

	private List<Map<String, Object>> qryOpApplyHeadSign(
			OpHeadSignQryParam qryParam, IUser user) throws Exception {
		User u = (User)user;
		Map<String,Object> qryMap = new HashMap<String,Object>();
		String str="";
		str=qryParam.getDatePlanBegin();	
		if(StringUtils.isNotBlank(str)){
			Date dtStart = DateUtils.getDefaultDateFormat().parse(str);
			qryMap.put("datePlanBegin", DateUtils.dateToStr("yyyy-MM-dd", dtStart)+" 00:00:00");
		}
		str=qryParam.getDatePlanEnd();	
		if(StringUtils.isNotBlank(str)){
			Date dtEnd = DateUtils.getDefaultDateFormat().parse(str);
			qryMap.put("datePlanEnd", DateUtils.dateToStr("yyyy-MM-dd", dtEnd)+" 23:59:59");
		}
		str=qryParam.getBedNo();	
		if(StringUtils.isNotBlank(str)){
			qryMap.put("bedNo", str);
		}
		str=qryParam.getEuOptype();	
		if(StringUtils.isNotBlank(str)){
			qryMap.put("euOptype", str);
		}
		str=qryParam.getFlagHead();	
		if(StringUtils.isNotBlank(str)){
			qryMap.put("flagHead", str);
		}
		str=qryParam.getNameEmpPhyOp();
		if(StringUtils.isNotBlank(str)){
			qryMap.put("nameEmpPhyOp", str);
		}
		str=qryParam.getNamePi();
		if(StringUtils.isNotBlank(str)){
			qryMap.put("namePi", str);
		}
		str=qryParam.getOpName();
		if(StringUtils.isNotBlank(str)){
			qryMap.put("opName", str);
		}
		str=qryParam.getPkDept();
		if(StringUtils.isNotBlank(str)){
			qryMap.put("pkDept", str);
		}
		qryMap.put("pkDeptExec", u.getPkDept());
		qryMap.put("pkOrg", u.getPkOrg());

		if(qryParam.getPkDeptList()!=null && qryParam.getPkDeptList().size()>0){
			qryMap.put("pkDeptList",qryParam.getPkDeptList());
		}
		return cnOpMapper.qryHeadSignApply(qryMap);
	}

	private void applyHeadCanelSign(List<CnOpApply> opApplys) {
		if(opApplys==null ||opApplys.size()==0) return;
		for(CnOpApply opApply : opApplys){
			opApply.setFlagHead("0");
			opApply.setPkEmpHead(null);
			opApply.setNameEmpHead(null);
			opApply.setDateHead(null);
			opApply.setTicketno(0);
		}
		DataBaseHelper.batchUpdate("update cn_op_apply set flag_head=:flagHead, pk_emp_head=:pkEmpHead,name_emp_head=:nameEmpHead,date_head=:dateHead,ticketno=:ticketno where pk_ordop=:pkOrdop and eu_status<=1 and flag_head='1' ", opApplys);
	}

	private void applyHeadSign(List<CnOpApply> opApplys,IUser user) {
		if(opApplys==null ||opApplys.size()==0) return;
		User u = (User)user;
		for(CnOpApply opApply : opApplys){
			opApply.setFlagHead("1");
			opApply.setPkEmpHead(u.getPkEmp());
			opApply.setNameEmpHead(u.getNameEmp());
			opApply.setDateHead(new Date());
		}
		DataBaseHelper.batchUpdate("update cn_op_apply set flag_head=:flagHead, pk_emp_head=:pkEmpHead,name_emp_head=:nameEmpHead,date_head=:dateHead,ticketno=:ticketno where pk_ordop=:pkOrdop and eu_status<=1 and flag_head='0' ", opApplys);
	}
	
	/**
	 * 查询当前机构下，当前科室下所有的手术申请
	 * 004004002010
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryOpApply(String param,IUser user){
		OpApplyQryParam opApply = JsonUtil.readValue(param, OpApplyQryParam.class);
		Map<String, Object> params = new HashMap<String,Object>();
		User u = (User) user;
		String str = opApply.getPkOrg();
		if(StringUtils.isNoneBlank(str)){
			params.put("pkOrg", str);
		}
		str = opApply.getPkDept();
		if (StringUtils.isNoneBlank(str)) {
			params.put("pkDept", str);
		}
		str = opApply.getCodeApply();
		if (StringUtils.isNoneBlank(str)) {
			params.put("codeApply", str);
		}
		str = opApply.getPkDeptNs();
		if (StringUtils.isNoneBlank(str)) {
			params.put("pkDeptNs", str);
		}
		str = opApply.getCodeIp();
		if (StringUtils.isNoneBlank(str)) {
			params.put("codeIp", str);
		}
		str = opApply.getBedNo();
		if (StringUtils.isNoneBlank(str)) {
			params.put("bedNo", str);
		}
		str = opApply.getNamePi();
		if (StringUtils.isNoneBlank(str)) {
			params.put("namePi", str);
		}
		str = opApply.getEuStatus();
		if (StringUtils.isNoneBlank(str)) {
			params.put("euStatus", str);
		}
		str = opApply.getTicketno();
		if (StringUtils.isNoneBlank(str)) {
			params.put("ticketno", str);
		}
		Date date = opApply.getDateStartBegin();
		if (date != null) {
			params.put("dateStartBegin", DateUtils.dateToStr("yyyy-MM-dd", date)+" 00:00:00");
		}
		date = opApply.getDateStartEnd();
		if (date != null) {
			params.put("dateStartEnd", DateUtils.dateToStr("yyyy-MM-dd", date)+" 23:59:59");
		}
		date = opApply.getDatePlanBegin();
		if (date != null) {
			params.put("datePlanBegin", DateUtils.dateToStr("yyyy-MM-dd", date)+" 00:00:00");
		}
		date = opApply.getDatePlanEnd();
		if (date != null) {
			params.put("datePlanEnd", DateUtils.dateToStr("yyyy-MM-dd", date)+" 23:59:59");
		}
		List<Map<String,Object>> opApplyList = cnOpMapper.qryOpApply(params);
		return opApplyList;
		
	}
	
	
	/**
	 * 保存手术申请及附加手术（重写）
	 * @param param
	 * @param user
	 */
	public OpPressVo saveOpApply(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		OpPressVo rtn=new OpPressVo();
		CnOpApply rtnOp=new CnOpApply();
		CnOpApply cnOpApplyMore= JsonUtil.readValue(param, CnOpApply.class);
		OpPressVo cnInfectMore= new OpPressVo();
		rtnOp=saveOpApply(cnOpApplyMore,user,param);
		
		BeanUtils.copyProperties(cnInfectMore, rtnOp);
		rtn=saveInfect(cnInfectMore,user,param);
		return rtn;
	}
	
	/**
	 * 同上
	 * @param cnOpApplyMore
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CnOpApply saveOpApply(CnOpApply cnOpApplyMore, IUser user,String param)throws IllegalAccessException, InvocationTargetException{
		CnOpApply rtn=new CnOpApply();
		List<CnOpSubjoin> subOpList = cnOpApplyMore.getSubOpList();
		List<CnOpSubjoin> subOpListForDel = cnOpApplyMore.getSubOpListForDel();
		Date dateNow = new Date();
		User userInfo = (User)user;
		
		//数据一致性验证
		if(cnOpApplyMore.getTs()!=null){
			List<String> pkCnordTsList = new ArrayList<String>();
			pkCnordTsList.add(cnOpApplyMore.getPkCnord()+","+dateformat.format(cnOpApplyMore.getTs()));
			if(pkCnordTsList==null||pkCnordTsList.size()<=0) return null;
			cnPubService.splitPkTsValidOrd(pkCnordTsList);
		}
		
		Date dd = cnPubService.getOutOrdDate(cnOpApplyMore.getPkPv());
		if (dd != null && dd.compareTo(dateNow) < 0) {
			dateNow = dd;
		}

		String pkWgOrg=null;
		String pkWg=null;
		if(StringUtils.isNotEmpty(cnOpApplyMore.getPkPv())){
			//判断是否符合开立条件
			if(checkPvInfo(cnOpApplyMore.getPkPv())){
				throw  new BusException("前患者就诊的科室为日间病房,住院超过允许开立医嘱的天数！不允许开立新医嘱！");
			}
			PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{cnOpApplyMore.getPkPv()});
			pkWgOrg=pvInfo.getPkWgOrg();
			pkWg=pvInfo.getPkWg();
		}

		CnOrder order = null;
		if( cnOpApplyMore.getPkCnord() != null){
			order = cnOpMapper.selectOrder(cnOpApplyMore.getPkCnord());
		}else{
			order = new CnOrder();
		}
			
		order.setPkWgOrg(pkWgOrg);//医疗组
		order.setPkWg(pkWg);
		if(StringUtils.isBlank(cnOpApplyMore.getPkOrdop())){
			order.setPkCnord(NHISUUID.getKeyId());
			order.setCodeOrdtype("04");					//手术类型
			order.setEuAlways("1");						//临时
			order.setCodeOrd(CODE_OP);
			order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));					
			order.setOrdsnParent(order.getOrdsn());		
			order.setDescOrd(order.getNameOrd());	
			order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
			order.setQuanCg(1.0);
			order.setQuan(1.0); 			//医嘱的数量
			order.setDosage(1.0);
			order.setEuStatusOrd("0");		//保存
			order.setEuPvtype("3");			//住院患者
			order.setFlagDoctor("1");
			order.setFlagErase("0");
			order.setFlagStopChk("0");
			order.setFlagStop("0");
			order.setFlagEraseChk("0");
			order.setFlagFirst(Constants.FALSE);
			order.setFlagDurg(Constants.FALSE);
			order.setFlagSelf(Constants.FALSE);
			order.setFlagNote(Constants.FALSE);
			order.setFlagBase(Constants.FALSE);
			order.setFlagBl(Constants.FALSE);
			order.setFlagStop(Constants.FALSE);
			order.setFlagPrint(Constants.FALSE);
			order.setFlagMedout(Constants.FALSE);
			order.setFlagEmer(Constants.FALSE);
			order.setFlagThera(Constants.FALSE);
			order.setFlagPrev(Constants.FALSE);
			order.setFlagFit(Constants.FALSE);
			order.setFlagCp(StringUtils.isBlank(cnOpApplyMore.getFlagCp()) ? Constants.FALSE:cnOpApplyMore.getFlagCp());
			order.setTs(dateNow);
			order.setDateEnter(dateNow);    //暂用服务器时间，db时间后期处理
			order.setDateStart(dateNow);
			order.setDateSign(dateNow);
			order.setPkDept(userInfo.getPkDept());
			order.setPkEmpInput(userInfo.getPkEmp());
			order.setNameEmpInput(userInfo.getUserName());

			order.setPkDept(cnOpApplyMore.getPkDept());
			order.setEuIntern(cnOpApplyMore.getEuIntern());
			order.setNoteOrd(cnOpApplyMore.getNote());
			order.setPkOrgExec(cnOpApplyMore.getPkOrgExec());
			order.setPkDeptExec(cnOpApplyMore.getPkDeptExec());
			order.setPkDeptNs(cnOpApplyMore.getPkDeptNs());	
			order.setCodeApply(cnOpApplyMore.getCodeApply());	
			order.setNameOrd(cnOpApplyMore.getOpName());				//手术医嘱名称带格式
			order.setPkPv(cnOpApplyMore.getPkPv());
			order.setPkPi(cnOpApplyMore.getPkPi());
			order.setPkOrg(cnOpApplyMore.getPkOrg());
			
			cnOpApplyMore.setPkOrdop(NHISUUID.getKeyId());
			cnOpApplyMore.setPkCnord(order.getPkCnord());
			cnOpApplyMore.setTs(dateNow);
			cnOpApplyMore.setDelFlag(Constants.FALSE);
			cnOpApplyMore.setEuStatus("0");
			cnOpApplyMore.setPkOrg(userInfo.getPkOrg());
			cnOpApplyMore.setDateApply(dateNow);
			
			DataBaseHelper.insertBean(cnOpApplyMore);
			DataBaseHelper.insertBean(order);
			
		}else{
			order.setNameOrd(cnOpApplyMore.getOpName());
			order.setDescOrd(order.getNameOrd());
			order.setTs(dateNow);
			order.setNoteOrd(cnOpApplyMore.getNote());
			order.setPkOrgExec(cnOpApplyMore.getPkOrgExec());
			order.setPkDeptExec(cnOpApplyMore.getPkDeptExec());
			
			cnOpApplyMore.setTs(dateNow);
			cnOpApplyMore.setEuStatus("0");
			
			DataBaseHelper.updateBeanByPk(order, false);
			DataBaseHelper.updateBeanByPk(cnOpApplyMore,false);

			if(cnOpApplyMore.getPkDeptAnae() ==null && cnOpApplyMore.getPkEmpAnae() == null){
				DataBaseHelper.update("update cn_op_apply set PK_DEPT_ANAE=:pkDeptAnae,PK_EMP_ANAE=:pkEmpAnae  where pk_cnord=:pkCnord ",cnOpApplyMore);
			}
			if(cnOpApplyMore.getEuErlevel() ==null){
				DataBaseHelper.update("update cn_op_apply set EU_ERLEVEL=:euErlevel where pk_cnord=:pkCnord ",cnOpApplyMore);
			}
		} 
		
		List<CnOpSubjoin> addCnOpSubjoin = new ArrayList<CnOpSubjoin>();
		List<CnOpSubjoin> updateCnOpSubjoin = new ArrayList<CnOpSubjoin>();
		if(subOpList != null)
		{
			for(CnOpSubjoin cnOpSub: subOpList ){
				cnOpSub.setPkOrdop(cnOpApplyMore.getPkOrdop());
				CnOpSubjoin subOp = new CnOpSubjoin();
				BeanUtils.copyProperties(subOp, cnOpSub);
				if(StringUtils.isBlank(subOp.getPkCnopjoin())){
					subOp.setPkCnopjoin(NHISUUID.getKeyId());
					subOp.setTs(dateNow);
					subOp.setCreateTime(dateNow);
					subOp.setCreator(userInfo.getPkEmp());
					addCnOpSubjoin.add(subOp);
				}else{
					updateCnOpSubjoin.add(subOp);
				}
			}
		}
		if (subOpListForDel != null && subOpListForDel.size() > 0) {
			DataBaseHelper.batchUpdate("delete from cn_op_subjoin where pk_cnopjoin = :pkCnopjoin ", subOpListForDel);
		}
		if (addCnOpSubjoin != null && addCnOpSubjoin.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpSubjoin.class), addCnOpSubjoin);
		}
		if (updateCnOpSubjoin != null && updateCnOpSubjoin.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOpSubjoin.class), updateCnOpSubjoin);
		}
		
		List<CnOpSubjoin> rtnCnOpSubjoinList= DataBaseHelper.queryForList("select * from cn_op_subjoin where del_flag='0' and pk_ordop = ?", CnOpSubjoin.class, new Object[] { cnOpApplyMore.getPkOrdop() });
		BeanUtils.copyProperties(rtn, cnOpApplyMore);
		
		//路径外医嘱变异记录
		if(cnOpApplyMore.getCpRecExpList()!=null && cnOpApplyMore.getCpRecExpList().size()>0 ){
			for(CpRecExp cpRecExpParam :cnOpApplyMore.getCpRecExpList()){
				cpRecExpParam.setPkCnord(order.getPkCnord() );
			}
			rtn.setCpRecExpList(cnOpApplyMore.getCpRecExpList());
		}
		if(cnOpApplyMore.getCnSignCa()!=null)
		{
			cnOpApplyMore.getCnSignCa().setPkBu(order.getPkCnord() );
			rtn.setCnSignCa(cnOpApplyMore.getCnSignCa());
		}
			
		
		rtn.setSubOpList(rtnCnOpSubjoinList);
		rtn.setCnOrder(order);
		
		return rtn;
	}
	
	public OpPressVo saveInfect(OpPressVo cnOpApplyMore,IUser user,String param) throws IllegalAccessException, InvocationTargetException{
		OpPressVo rtn=new OpPressVo();
		OpPressVo cnInfectMore= JsonUtil.readValue(param, OpPressVo.class);
		List<CnOpInfect> infectList = cnInfectMore.getInfectOpList();
		Date dateNow = new Date();
		User userInfo = (User)user;
		
		/*******     感染类型     start       ********/
		List<CnOpInfect> addInfect = new ArrayList<CnOpInfect>();
		List<CnOpInfect> updateInfect = new ArrayList<CnOpInfect>();
		if(infectList != null)
		{
			for(CnOpInfect cninfect: infectList ){
				cninfect.setPkCnord(cnOpApplyMore.getPkCnord());
				CnOpInfect subIn = new CnOpInfect();
				BeanUtils.copyProperties(subIn, cninfect);
				if(StringUtils.isBlank(subIn.getPkOpinfect())){
					subIn.setPkOpinfect(NHISUUID.getKeyId());
					subIn.setTs(dateNow);
					subIn.setCreateTime(dateNow);
					subIn.setCreator(userInfo.getPkEmp());
					addInfect.add(subIn);
				}else{
					updateInfect.add(cninfect);
				}
			}
		}
		if (addInfect != null && addInfect.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpInfect.class), addInfect);
		}
		if (updateInfect != null && updateInfect.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOpInfect.class), updateInfect);
		}
		
		List<CnOpInfect> rtnInfectList= DataBaseHelper.queryForList("select * from cn_op_infect where del_flag='0' and pk_cnord = ?", CnOpInfect.class, new Object[] { cnOpApplyMore.getPkCnord()});
		
		/*******     感染类型     end       ********/
		
		BeanUtils.copyProperties(rtn, cnOpApplyMore);
		
		rtn.setInfectOpList(rtnInfectList);		
		return rtn;
	}
	
	/**
	 * 签署手术申请（重写）
	 * @param param
	 * @param user
	 */
	public void signOpApply2(String param , IUser user)  throws IllegalAccessException, InvocationTargetException{
		
		User u = (User)user;
		Date d = new Date();
		//签署前先保存
		OpPressVo cnOpApplyMore=  this.saveOpApply(param,user);
		
		List<String> pkCnordList= new ArrayList<String>();
		pkCnordList.add(cnOpApplyMore.getPkCnord());
		signOpApply(pkCnordList,u,d);
		updateOpStatus(pkCnordList,d);
		
		if(cnOpApplyMore.getCnSignCa()!=null && !CommonUtils.isEmptyString(cnOpApplyMore.getCnSignCa().getEuOptype())){
			List<CnSignCa> cnSignCaList = new ArrayList<CnSignCa>();
			cnSignCaList.add(cnOpApplyMore.getCnSignCa());
			cnPubService.caRecord(cnSignCaList);
		}
		//变异记录保存
		cnPubService.DealOutCpOrdExp(cnOpApplyMore.getCpRecExpList(),u);
		cnPubService.sendMessage(pkCnordList,"新医嘱");
		CnOrder cnOrder = cnOpApplyMore.getCnOrder();//获取医嘱信息
		List<CnOrder> cnOrders = new ArrayList<CnOrder>();
		cnOrders.add(cnOrder);
		//保存临床提醒消息
		cnNoticeService.saveCnNotice(cnOrders);
		//手术申请发消息   ORM^O01
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("control", "NW");//操作类型
		paramMap.put("LBcontrol", "NO");//灵璧不推送判断条件
		paramMap.put("signOpApply2", "");//方法名标志
		paramMap.put("pkCnList",pkCnordList);
		PlatFormSendUtils.sendOpApplyMsg(paramMap);
	}
	/**
	 * 根据医嘱主键获得医嘱信息（手术）
	 * @param
	 * @return
	 */
	private List<CnOrder> getCnOrderByPkCnord(List<String> pkOpList){
//		String pkCnord = pkOpList.get(0);
//		List<CnOrder> queryForList = DataBaseHelper.queryForList("select * from cn_order  where pk_cnord = ?", CnOrder.class, pkCnord);
		List<CnOrder> qryOrderMsg = cnOpMapper.qryOpOrderMsg(pkOpList);
		return qryOrderMsg;
		
	}

	/**
	 * 获取指定患者的手术申请--重写
	 * @param param
	 * @param user
	 * @return
	 */
	public List<OpPressVo> getOpApplyListNew(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		User u = (User)user;
		List<OpPressVo> retOp=new ArrayList<OpPressVo>();
		List<OpPressVo> ret = cnOpMapper.getOpApplyListNew(paramMap);
		List<String> pkOpList = new ArrayList<String>();
		HashMap<String, OpPressVo> hmApply = new HashMap<String, OpPressVo>();
		List<String> pkCnordList = new ArrayList<String>();
		HashMap<String, OpPressVo> hmInfect = new HashMap<String, OpPressVo>();
		OpPressVo vo=null;
		for(OpPressVo apply : ret){
			vo=new OpPressVo();
			BeanUtils.copyProperties(vo, apply);
			pkOpList.add(apply.getPkOrdop());
			pkCnordList.add(apply.getPkCnord());
			hmInfect.put(apply.getPkCnord(), vo);
			hmInfect.put(apply.getPkOrdop(), vo);
			String sqlStr="select * from ex_op_sch sch where sch.pk_cnord=? and sch.eu_status='1' and sch.del_flag='0' ";
			ExOpSch exOpSch=DataBaseHelper.queryForBean(sqlStr, ExOpSch.class,new Object[]{apply.getPkCnord()});
			if(exOpSch!=null){
				vo.setExOpSch(exOpSch);
			}
			retOp.add(vo);
		}
		
		if(pkOpList.size()>0){
			List<CnOpSubjoin> childList = cnOpMapper.getChildApplyList(pkOpList);
			for(CnOpSubjoin child : childList){
				OpPressVo apply = hmInfect.get(child.getPkOrdop());
				if(apply!=null)
					apply.getSubOpList().add(child);
			}
		}
		
		if(pkCnordList.size()>0){
			List<CnOpInfect> childList = cnOpMapper.getInfectList(pkCnordList);
			for(CnOpInfect child : childList){
				OpPressVo apply = hmInfect.get(child.getPkCnord());
				if(apply!=null)
					apply.getInfectOpList().add(child);
			}
		}
		
		
		
		return retOp;
	}

	/*************************************医嘱属性 前端不需要处理 start**************************************/
	//处理日间病房医嘱开立期限的校验
	private boolean checkPvInfo(String pkPv){
		boolean ok=false;//默认通过校验
		String day=ApplicationUtils.getSysparam("CN0107", false);
		String sql="Select PV.* From pv_encounter pv \n" +
				"Inner Join bd_dictattr dict On pv.pk_dept=dict.pk_dict \n" +
				"Where  pv.pk_pv=? And dict.code_attr='0605' And dict.val_attr='1'";
		PvEncounter pvEncounter=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});

		if(org.apache.commons.lang3.StringUtils.isNotEmpty(day) && pvEncounter!=null){
			int dayIp= DateUtils.getDateSpace(pvEncounter.getDateBegin(), new Date());
			int dayI= Integer.parseInt(day);
			//判断
			if(dayI>0 && dayI<dayIp) ok=true;
		}
		return ok;
	}
	/*************************************医嘱属性 前端不需要处理 end**************************************/
}
