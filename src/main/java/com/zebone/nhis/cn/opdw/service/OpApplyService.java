package com.zebone.nhis.cn.opdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.opcg.vo.OpCgCnOrder;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.ipdw.service.CnReqService;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.opdw.dao.OpApplyMapper;
import com.zebone.nhis.cn.opdw.vo.OpApplyRecordVo;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdComm;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OpApplyService {

	@Autowired
	private OpApplyMapper OpApply ;

	@Autowired
	private OpCgPubService opCgPubService;

	@Autowired
	private PdStOutPubService pdStOutPubService;	
	
	@Autowired
	private CnReqService cnReqService;
	
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	/**
	 * 获得检查检验治疗医嘱
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getApplyRecord(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OpApply.getApplyRecord(map.get("pkPv").toString());
	}	
	public List<Map<String,Object>> getRisRecord(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OpApply.getRisRecord(map.get("pkPv").toString());
	}	
	public List<Map<String,Object>> getLabRecord(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		
		if (Application.isSqlServer()) {
			return OpApply.getLabRecord(map.get("pkPv").toString());
		}else{
			return OpApply.getLabRecordOracle(map.get("pkPv").toString());
		}

		
	}		
	/**
	 * 保存检查检验治疗医嘱
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveApplyRecord(String param,IUser user){
		OpApplyRecordVo OpApplyAll = JsonUtil.readValue(param, new TypeReference<OpApplyRecordVo>(){} );
		
		        // 关闭事务自动提交
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				TransactionStatus status = platformTransactionManager.getTransaction(def); 
				List<CnOrder> signCnOrderList  =  new ArrayList<CnOrder>();
				List<CnOrder> qryOrderMsg = null;
			    try{
			    	saveApplyRecord(OpApplyAll,user);
			    	
				    platformTransactionManager.commit(status); // 提交事务
				} catch (Exception e) {
					platformTransactionManager.rollback(status); // 添加失败 回滚事务；
				    e.printStackTrace();
					throw new RuntimeException("保存检查检验治疗医嘱信息失败：" + e.getMessage());
				}
	}
    
	
	/**
	 * 保存检查检验治疗医嘱(提取方法)
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveApplyRecord(OpApplyRecordVo OpApplyAll,IUser user){
		List<OpCgCnOrder> ApplyList = OpApplyAll.getOpApply();
		List<OpCgCnOrder> ApplyListDel = OpApplyAll.getOpApplyDel();
		List<CnRisApply> RisList = OpApplyAll.getOpRis();
		List<CnLabApply> LabList = OpApplyAll.getOpLab();
		List<OpCgCnOrder> OrderInsertList = new ArrayList<OpCgCnOrder>();
		List<OpCgCnOrder> OrderUpdateList = new ArrayList<OpCgCnOrder>();
		Set<String> pkCnOrderSet = new HashSet<String>();
		PvEncounter pvInfo = null;
		
		String sEffeDate = ApplicationUtils.getSysparam("CN0004", false);
		int IntEffeDate = 3;
		if (sEffeDate != null)
			IntEffeDate = Integer.parseInt(sEffeDate);

		if (ApplyList.size() == 0 && ApplyListDel.size() == 0)
			return;
		if (ApplyList.size() > 0)
			pvInfo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, ApplyList.get(0).getPkPv());
		for(int i=0; i<ApplyList.size(); i++){
			OpCgCnOrder apply = ApplyList.get(i);
			apply = SetOrderDefaultValue(apply, UserContext.getUser().getPkOrg(), IntEffeDate);
			if(Constants.RT_NEW.equals(apply.getRowStatus()))
			{   
				apply.setTs(new Date());
				OrderInsertList.add(apply);
			}
			else if(Constants.RT_UPDATE.equals(apply.getRowStatus()))
			{   
				apply.setTs(new Date());
				pkCnOrderSet.add(apply.getPkCnord());
				OrderUpdateList.add(apply);
			}
		}

		for(int i=0; i<ApplyListDel.size(); i++){
			CnOrder apply = ApplyListDel.get(i);
			//删除医嘱表
			String sql_del = "delete from cn_order where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除费用表
			sql_del = "delete from bl_op_dt where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除Ris表
			sql_del = "delete from cn_lab_apply where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除Lab表
			sql_del = "delete from cn_ris_apply where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除费用表
			sql_del = "delete from bl_op_dt where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
		}
		//保存order表和费用明细表bd_ord_dt
		if (OrderInsertList.size() > 0) {
			// 批量保存医嘱
			for (OpCgCnOrder orderInsert : OrderInsertList) {
				orderInsert.setEuStatusOrd("1");
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), OrderInsertList);
			drugOpCg(OrderInsertList, pvInfo);
		}
		if (OrderUpdateList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), OrderUpdateList);
			// 删除掉之前的计费
			String sql = "delete from bl_op_dt where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnOrderSet, "pk_cnord") + ")";
			DataBaseHelper.execute(sql, new Object[] {});
			drugOpCg(OrderUpdateList, pvInfo);
		}		
		
		//保存Ris表
		for(int i=0; i<RisList.size(); i++){
			CnRisApply ris = RisList.get(i);
			ris.setEuStatus(EnumerateParameter.ONE);
			if(Constants.RT_NEW.equals(ris.getRowStatus()))
				DataBaseHelper.insertBean(ris);
			else if(Constants.RT_UPDATE.equals(ris.getRowStatus()))
				DataBaseHelper.updateBeanByPk(ris, false);
		}
		
		//保存Lab表		
		for(int i=0; i<LabList.size(); i++){
			CnLabApply lab = LabList.get(i);
			lab.setEuStatus(EnumerateParameter.ONE);
			if(Constants.RT_NEW.equals(lab.getRowStatus()))
				DataBaseHelper.insertBean(lab);
			else if(Constants.RT_UPDATE.equals(lab.getRowStatus()))
				DataBaseHelper.updateBeanByPk(lab, false);
		}	
	}
	////////////////////////////////////////////////////////////
	private OpCgCnOrder SetOrderDefaultValue(OpCgCnOrder apply, String pkOrg,
			int intEffeDate) {
		Date dt = new Date();
		Date dateEffe = getDateAfter(dt, intEffeDate);  
		apply.setPkOrg(pkOrg);
		apply.setDateEffe(dateEffe);
		apply.setDateSign(dt);
		if(apply.getDateStart()==null){
		apply.setDateStart(dt); 
		}
		apply.setFlagFirst("0");
		apply.setFlagDurg("0");
		apply.setFlagSelf("0");
		apply.setFlagNote("0");		
		apply.setFlagBase("0");
		apply.setFlagBl("0");
		apply.setFlagStop("0");
		apply.setFlagStopChk("0");				
		apply.setFlagErase("0");
		apply.setFlagEraseChk("0");
		apply.setFlagCp("0");
		apply.setFlagDoctor("1");		//医生标志
		apply.setFlagPrint("0");
		apply.setFlagMedout("0");
		if (apply.getFlagEmer() == null || apply.getFlagEmer() == "")
			apply.setFlagEmer("0");
		apply.setFlagThera("0");				
		apply.setFlagPrev("0");		
		apply.setFlagFit("0");
		apply.setFlagSign("0");
		apply.setFlagDurg("0");  //诊疗、检查、检验项目标志
		apply.setOrds((long) 1);		
		apply.setQuanCg(apply.getQuan());  //设置计费数量
		return apply;
	}
	public void drugOpCg(List<OpCgCnOrder> occo, PvEncounter pvInfo) {
		User u = UserContext.getUser();
		String pkOrg = u.getPkOrg();
		String pkDept = u.getPkDept();
		List<BlPubParamVo> bods = new ArrayList<BlPubParamVo>();
		for (OpCgCnOrder opCgCnOrder : occo) {
			BlPubParamVo bpb = new BlPubParamVo();
			bpb.setPkOrg(pkOrg);
			bpb.setEuPvType(pvInfo.getEuPvtype());
			bpb.setPkPv(pvInfo.getPkPv());
			bpb.setPkPi(pvInfo.getPkPi());
			bpb.setPkOrd(opCgCnOrder.getPkOrd());
			bpb.setPkCnord(opCgCnOrder.getPkCnord());
			bpb.setPkPres(opCgCnOrder.getPkPres());
			bpb.setPkItem(null);
			bpb.setQuanCg(opCgCnOrder.getQuanCg());
			bpb.setPkOrgEx(opCgCnOrder.getPkOrgExec());
			bpb.setPkOrgApp(pvInfo.getPkOrg());
			bpb.setPkDeptEx(opCgCnOrder.getPkDeptExec());
			bpb.setPkDeptApp(pvInfo.getPkDept());
			bpb.setPkEmpApp(opCgCnOrder.getPkEmpOrd());
			bpb.setNameEmpApp(opCgCnOrder.getNameEmpOrd());
			bpb.setFlagPd(opCgCnOrder.getPkPres() == null ? "0" : "1");
			bpb.setNamePd(opCgCnOrder.getNameOrd());
			bpb.setFlagPv("0");
			bpb.setDateExpire(opCgCnOrder.getDateExpire());
			bpb.setPkUnitPd(opCgCnOrder.getPkUnitCg());
			bpb.setPackSize(opCgCnOrder.getPackSize().intValue());
			bpb.setPrice(opCgCnOrder.getPriceCg());
			bpb.setPriceCost(opCgCnOrder.getPriceCost());
			bpb.setDateHap(new Date());
			bpb.setPkDeptCg(pkDept);
			bpb.setPkEmpCg(u.getPkEmp());
			bpb.setNameEmpCg(u.getNameEmp());
			bods.add(bpb);
		}
		// 批量记费
		//opcgPubService.blOpCg(bods); yangxue 注释，改为批量模式
		opCgPubService.blOpCgBatch(bods);

	}
	/**
	 * 获得检查项目列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getRisTemplates(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map.get("flag").toString().equals("0"))
			return OpApply.getRisTemplates();
		else
			return OpApply.getRisTemplatesItem(map.get("pkOrg").toString());
		//	       orgord.pk_org=#{pkOrg,jdbcType=VARCHAR} and 
		//	       price.eu_pricetype=#{euPriceType,jdbcType=VARCHAR} and 
		//	       price.date_begin&lt;=to_date(#{beginDt,jdbcType=VARCHAR},'yyyy/mm/dd HH24:mi:ss') and 
		//	       price.date_end&gt;= to_date(#{endDt,jdbcType=VARCHAR},'yyyy/mm/dd HH24:mi:ss') and
	}	
	/**
	 * 获得检验项目列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getLabTemplates(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map.get("flag").toString().equals("0"))
			return OpApply.getLabTemplates();
		else
			return OpApply.getLabTemplatesItem(map.get("pkOrg").toString());
	}	

	/**
	 * 获得患者诊断和病历
	 * @param param
	 * @param user
	 * @return
	 */
	public String getDiagProblem(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String ret = "";
		String sql = "";
		String sql_problem = "select emr.problem ret from cn_emr_op emr where emr.pk_pv=?";
		String sql_diag = "select diag.desc_diag ret from pv_diag diag where diag.pk_pv=? and diag.del_flag = '0' ";
		if (map.get("flag").toString().equals("0"))
			sql = sql_problem;
		else
			sql = sql_diag;
		List<Map<String,Object>> ret_map = DataBaseHelper.queryForList(sql, map.get("pkPv").toString());
		if(ret_map.size() == 0 )
			ret = "";
		else
		{		
			for (Map<String,Object> ret_map_el : ret_map)
			{
				if (ret_map_el.get("ret") == null) {
					ret = "";
				}else {
					ret = ret + ret_map_el.get("ret").toString() + ";";					
				}
			}			
			//			if(ret_map.get(0).get("ret") == null)
			//				ret = "";
			//			else
			//			    ret = ret_map.get(0).get("ret").toString();
		}
		return ret;
	}

	/**
	 * 保存常用检查检验医嘱模板
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveApplyFavorites(String param, IUser user){
		List<BdOrdComm> favList = JsonUtil.readValue(param, new TypeReference<List<BdOrdComm>>(){});
		for (BdOrdComm item: favList)
			ApplicationUtils.setDefaultValue(item, true);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdComm.class), favList);
	}	

	public List<Map<String,Object>> getRisTemplatePerson(String param , IUser user){
		User u = (User)user;
		List<Map<String,Object>> list = OpApply.getRisTemplatePerson(u.getPkOrg(), u.getPkEmp());
		return list;
	}	
	public List<Map<String,Object>> getLisTemplatePerson(String param , IUser user){
		User u = (User)user;
		List<Map<String,Object>> list = OpApply.getLisTemplatePerson(u.getPkOrg(), u.getPkEmp());
		return list;
	}

	public List<Map<String,Object>> getRisBody(String param , IUser user){
		List<Map<String,Object>> list = DataBaseHelper.queryForList("select * from bd_defdoc where code_defdoclist = '030101' order by name");
		return list;
	}	

	public static Date getDateAfter(Date d, int day) {  
		Calendar now = Calendar.getInstance();  
		now.setTime(d);  
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
		return now.getTime();  
	}	

	/**
	 * 删除检验申请单
	 * @param param
	 * @param user
	 */
	public void delLabApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		Map<String, String> pkCnord = JsonUtil.readValue(param, Map.class);
		delLabApply(pkCnord);
	}
	/**
	 * 删除检验申请单(提取方法)
	 * @param param
	 * @param user
	 */
	public void delLabApply(Map param) throws IllegalAccessException, InvocationTargetException{
		Map<String, String> pkCnord = param;
		if(StringUtils.isBlank(pkCnord.get("pkCnord"))) return;

		CnOrder cnOrder = DataBaseHelper.queryForBean(
				"select * from cn_order where pk_cnord = ? and del_flag = '0'", CnOrder.class, pkCnord.get("pkCnord"));
		cnOrder.setDelFlag("1");

		DataBaseHelper.deleteBeanByWhere(new CnLabApply(), "pk_cnord='"+pkCnord.get("pkCnord")+"'");

		//删除收费明细检验申请单
		DataBaseHelper.update(
				"UPDATE bl_op_dt SET DEL_FLAG='1' WHERE pk_cnord =?",
				new Object[] { pkCnord.get("pkCnord") });

		DataBaseHelper.updateBeanByPk(cnOrder, false);
	}
	/**
	 * 删除检查申请单
	 * @param param
	 * @param user
	 */
	public void delRisApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		Map<String, String> pkCnord = JsonUtil.readValue(param, Map.class);
		delRisApply(pkCnord);
	}
	/**
	 * 删除检查申请单(提取方法)
	 * @param param
	 * @param user
	 */
	public void delRisApply(Map param) throws IllegalAccessException, InvocationTargetException{
		Map<String, String> pkCnord =param;
		if(StringUtils.isBlank(pkCnord.get("pkCnord"))) return;

		CnOrder cnOrder = DataBaseHelper.queryForBean(
				"select * from cn_order where pk_cnord = ? and del_flag = '0'", CnOrder.class, pkCnord.get("pkCnord"));
		cnOrder.setDelFlag("1");

		DataBaseHelper.deleteBeanByWhere(new CnRisApply(), "pk_cnord='"+pkCnord.get("pkCnord")+"'");
		//删除收费明细检验申请单
		DataBaseHelper.update(
				"UPDATE bl_op_dt SET DEL_FLAG='1' WHERE pk_cnord =?",
				new Object[] { pkCnord.get("pkCnord") });
		DataBaseHelper.updateBeanByPk(cnOrder, false);
	}
	/**
	 * 删除处置单
	 * @param param
	 * @param user
	 */
	public void delCnOrd(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		Map<String, String> pkCnord = JsonUtil.readValue(param, Map.class);
		delCnOrd(pkCnord);
	}
	/**
	 * 删除处置单(提取方法)
	 * @param param
	 * @param user
	 */
	public void delCnOrd(Map param) throws IllegalAccessException, InvocationTargetException{
		Map<String, String> pkCnord = param;
		if(StringUtils.isBlank(pkCnord.get("pkCnord"))) return;

		CnOrder cnOrder = DataBaseHelper.queryForBean(
				"select * from cn_order where pk_cnord = ? and del_flag = '0'", CnOrder.class,pkCnord.get("pkCnord"));
		cnOrder.setDelFlag("1");
		//删除收费明细检验申请单
		DataBaseHelper.update(
				"UPDATE bl_op_dt SET DEL_FLAG='1' WHERE pk_cnord =?",
				new Object[] { pkCnord.get("pkCnord") });
		DataBaseHelper.updateBeanByPk(cnOrder, false);
	}
	/**
	 * 保存检查检验治疗医嘱(急诊使用)
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveApplyRecordNems(OpApplyRecordVo OpApplyAll,IUser user){
		List<OpCgCnOrder> ApplyList = OpApplyAll.getOpApply();
		List<OpCgCnOrder> ApplyListDel = OpApplyAll.getOpApplyDel();
		List<CnRisApply> RisList = OpApplyAll.getOpRis();
		List<CnLabApply> LabList = OpApplyAll.getOpLab();
		List<OpCgCnOrder> OrderInsertList = new ArrayList<OpCgCnOrder>();
		List<OpCgCnOrder> OrderUpdateList = new ArrayList<OpCgCnOrder>();
		Set<String> pkCnOrderSet = new HashSet<String>();
		PvEncounter pvInfo = null;
		
		String sEffeDate = ApplicationUtils.getSysparam("CN0004", false);
		int IntEffeDate = 3;
		if (sEffeDate != null)
			IntEffeDate = Integer.parseInt(sEffeDate);

		if (ApplyList.size() == 0 && ApplyListDel.size() == 0)
			return;
		if (ApplyList.size() > 0)
			pvInfo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, ApplyList.get(0).getPkPv());
		for(int i=0; i<ApplyList.size(); i++){
			OpCgCnOrder apply = ApplyList.get(i);
			apply = SetOrderDefaultValue(apply, UserContext.getUser().getPkOrg(), IntEffeDate);
			if(Constants.RT_NEW.equals(apply.getRowStatus()))
			{
				OrderInsertList.add(apply);
			}
			else if(Constants.RT_UPDATE.equals(apply.getRowStatus()))
			{
				pkCnOrderSet.add(apply.getPkCnord());
				OrderUpdateList.add(apply);
			}
		}

		for(int i=0; i<ApplyListDel.size(); i++){
			CnOrder apply = ApplyListDel.get(i);
			//删除医嘱表
			String sql_del = "delete from cn_order where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除费用表
			sql_del = "delete from bl_op_dt where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除Ris表
			sql_del = "delete from cn_lab_apply where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除Lab表
			sql_del = "delete from cn_ris_apply where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
			//删除费用表
			sql_del = "delete from bl_op_dt where pk_cnord=?";
			DataBaseHelper.execute(sql_del, apply.getPkCnord());	
		}
		//保存order表和费用明细表bd_ord_dt
		if (OrderInsertList.size() > 0) {
			// 批量保存医嘱
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), OrderInsertList);
			drugOpCg(OrderInsertList, pvInfo);
		}
		if (OrderUpdateList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), OrderUpdateList);
			// 删除掉之前的计费
			String sql = "delete from bl_op_dt where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnOrderSet, "pk_cnord") + ")";
			DataBaseHelper.execute(sql, new Object[] {});
			drugOpCg(OrderUpdateList, pvInfo);
		}		
		
		//保存Ris表
		for(int i=0; i<RisList.size(); i++){
			CnRisApply ris = RisList.get(i);
			ris.setEuStatus(EnumerateParameter.ONE);
			if(Constants.RT_NEW.equals(ris.getRowStatus()))
				DataBaseHelper.insertBean(ris);
			else if(Constants.RT_UPDATE.equals(ris.getRowStatus()))
				DataBaseHelper.updateBeanByPk(ris, false);
		}
		
		//保存Lab表		
		for(int i=0; i<LabList.size(); i++){
			CnLabApply lab = LabList.get(i);
			lab.setEuStatus(EnumerateParameter.ONE);
			if(Constants.RT_NEW.equals(lab.getRowStatus()))
				DataBaseHelper.insertBean(lab);
			else if(Constants.RT_UPDATE.equals(lab.getRowStatus()))
				DataBaseHelper.updateBeanByPk(lab, false);
		}	
	}
}
