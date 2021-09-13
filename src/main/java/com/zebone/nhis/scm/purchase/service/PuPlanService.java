package com.zebone.nhis.scm.purchase.service;

import com.zebone.nhis.common.dao.BdOuPubMapper;
import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.module.scm.support.ScmConstant;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.scm.purchase.dao.PuPlanMapper;
import com.zebone.nhis.scm.purchase.vo.DrugAdminPlanDtRet;
import com.zebone.nhis.scm.purchase.vo.PdPlanParam;
import com.zebone.nhis.scm.purchase.vo.PdStoreParam;
import com.zebone.nhis.scm.purchase.vo.PlanUpParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 采购计划
 * 
 * @author wangpeng
 * @date 2016年11月1日
 *
 */
@Service
public class PuPlanService {
	
	@Resource
	private BdOuPubMapper bdOuPubMapper;
	
	@Resource
	private PuPlanMapper puPlanMapper;
	
	/**
	 * 交易号：008005001010<br>
	 * 获取计划单号
	 *  
	 * @param  param
	 * @param  user
	 * @return Map<String,String>
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月1日
	 */
	public Map<String, String> getPlanCode(String param, IUser user){
		Map<String, String> map = new HashMap<String, String>();
		//采购计划单号编码"012"
		String code = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_CGJHD);
		map.put("codePlan", code);
		return map;		
	}
	
	/**
	 * 交易号：008005001003<br>
	 * 删除采购计划单
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月1日
	 */
	public void deletePdPlan(String param, IUser user){
		PdPlan plan = JsonUtil.readValue(param, PdPlan.class);
		String pkPdplan = plan.getPkPdplan();
		
		//删除采购计划（物品请领）明细
		DataBaseHelper.execute("delete from pd_plan_detail  where pd_plan_detail.pk_pdplan = ? "
				+ "and exists (select 1 from pd_plan where pd_plan_detail.pk_pdplan=pd_plan.pk_pdplan and pd_plan.eu_status=0)", pkPdplan);
		
		//删除采购计划（物品请领）
		DataBaseHelper.execute("delete from pd_plan where pk_pdplan=? and eu_status=0", pkPdplan);	
	}
	
	/**
	 * 交易号：008005001004<br>
	 * 保存采购计划单<br>
	 * <pre>
	 * 1、planDetailList为null时不处理，planDetailList.size=0时表示全部删除；
	 * </pre>
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月1日
	 */
	public PdPlanParam savePdPlanAndDetail(String param, IUser user){
		User u = UserContext.getUser();
		PdPlanParam vo = JsonUtil.readValue(param, PdPlanParam.class);
		
		PdPlan pdPlan = vo.getPdPlan();
		pdPlan.setPkDept(u.getPkDept());
		//仓库
		//String pkStore = DataBaseHelper.queryForScalar("select pk_store from bd_store where pk_dept = ? and del_flag = '0' ", String.class, new Object[]{u.getPkDept()});
		String pkStore = u.getPkStore();
		if(StringUtils.isEmpty(pkStore)){
			throw new BusException("仓库不存在！");
		}
		pdPlan.setPkStore(pkStore);
		pdPlan.setDtPlantype(ScmConstant.PLAN_DT_PLANTYPE_0101); //采购计划 "0101"
		pdPlan.setFlagAcc("0");
		pdPlan.setPkEmpMak(u.getPkEmp());
		pdPlan.setNameEmpMak(u.getNameEmp());
		pdPlan.setDelFlag("0");
		if(StringUtils.isEmpty(pdPlan.getPkPdplan())){  //保存
			DataBaseHelper.insertBean(pdPlan);
		}else{
			DataBaseHelper.updateBeanByPk(pdPlan,false);
		}
		
		//为null时不处理，size=0时表示全部删除
		List<PdPlanDetail> planDetailList = vo.getPlanDetailList();
		if(planDetailList != null){
			//先全部软删除再统一更新或新增
			DataBaseHelper.update("delete from pd_plan_detail where pk_pdplan = ?", new Object[]{pdPlan.getPkPdplan()});
			int i = 1;
			for(PdPlanDetail pd : planDetailList){
				if(pd.getQuanPack()>0){
					pd.setPkPdplan(pdPlan.getPkPdplan());
					pd.setQuanDe(new Double("0"));
					pd.setFlagFinish("0");
					pd.setDelFlag("0");
					pd.setSortNo(i); //排序，从1开始

					DataBaseHelper.insertBean(pd);
					i++;
				}
			}
		}
		int countNum = DataBaseHelper.queryForScalar("select count(1) from pd_plan_detail "
				+ "where  pk_pdplan = ?", Integer.class, pdPlan.getPkPdplan());
		if(countNum==0){
			DataBaseHelper.update("delete from pd_plan where pk_pdplan = ?", new Object[]{pdPlan.getPkPdplan()});
			pdPlan.setPkPdplan(null);
		}
		vo.setPdPlan(pdPlan);
		return vo;
	}
	
	/**
	 * 交易号：008005001005<br>
	 * 提交采购计划单<br>
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月1日
	 */
	public void updatePdPlan(String param, IUser user){
		User u = UserContext.getUser();
		PdPlan plan = JsonUtil.readValue(param, PdPlan.class);
		String needChk = ApplicationUtils.getSysparam("SCM0006",false);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("modifier", u.getPkEmp());		
		mapParam.put("ts", new Date());
		mapParam.put("pkPdplan", plan.getPkPdplan());
		mapParam.put("flagChk", (StringUtils.isBlank(needChk)|| EnumerateParameter.ZERO.equals(needChk))?EnumerateParameter.ONE:EnumerateParameter.ZERO);
		DataBaseHelper.update("update pd_plan set eu_status = '1',flag_chk=:flagChk, modifier=:modifier, ts=:ts where pk_pdplan=:pkPdplan and eu_status='0'", mapParam);
	}
	
	/**
	 * 交易号：008005001008<br>
	 * 查询物品<br>
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年11月1日
	 */
	public List<PdStoreParam> getPdStoreVoList(String param, IUser user){
		User u = UserContext.getUser();
		PdStoreParam voParam = JsonUtil.readValue(param, PdStoreParam.class);
		
		String pkStore = u.getPkStore();
		//String pkStore = DataBaseHelper.queryForScalar("select pk_store from bd_store where pk_dept = ? and del_flag = '0' ", String.class, new Object[]{u.getPkDept()});
		if(StringUtils.isEmpty(pkStore)){
			throw new BusException("仓库不存在！");
		}
		voParam.setPkOrg(u.getPkOrg());
		voParam.setPkStore(pkStore);
		
		List<PdStoreParam> voList = puPlanMapper.getPdStoreVoList(voParam);
		//向上取整
		for (int i = 0; i < voList.size(); i++) {
			PdStoreParam pdStore=voList.get(i);
			if(pdStore.getStockMax()==null)
			{
				pdStore.setStockMax(0.00);
			}else{
				pdStore.setStockMax(Math.ceil(pdStore.getStockMax()));
			}
		}
		return voList;
	}
	
	/**
	 * 交易号：008005001013
	 * 消耗计算
	 */
	public List<PdStoreParam> getPdStoreByConsumer(String param, IUser user) {
		PdStoreParam vo = JsonUtil.readValue(param, PdStoreParam.class);
		User u = UserContext.getUser();
		//获取当前仓库
		String pkStore = u.getPkStore();
		//获取计算类型
		String account = vo.getAccount();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkStore", u.getPkStore());
		map.put("account",account );
		List<PdStoreParam> lpsp = new ArrayList<PdStoreParam>();
		List<PdStoreParam> list = new ArrayList<PdStoreParam>();
		map.put("dtSttypes",vo.getDtSttypes());
		if ("1".equals(vo.getCalculateWay())) {
			map.put("cnt", vo.getCnt());
			map.put("cntPlan", vo.getCntPlan());
			String endDate = vo.getDateEnd().substring(0, 8);
			map.put("dateStart", endDate + "000000");
			map.put("dateEnd", endDate + "235959");
			//判断计算类型
			if ("0".equals(account)) {
				lpsp = puPlanMapper.qryByConsumer(map);
			} else if ("1".equals(account)) {
				lpsp = puPlanMapper.qryByPurchase(map);
			}else{
				map.put("pkStores", vo.getPkStores());
				map.put("uPkStore", u.getPkStore());				
				lpsp = puPlanMapper.qryByConsumer3(map);
			}
			//向上取整
			for (int i = 0; i < lpsp.size(); i++) {
				PdStoreParam pdStore=lpsp.get(i);
				Double quanPack = new Double(pdStore.getQuanPack());
				pdStore.setRealQuanPack(quanPack);
				pdStore.setQuanPack(Math.ceil(pdStore.getQuanPack()));
			}
		} else {
			map.put("upperDays", vo.getUpperDays());
			map.put("lowerDays", vo.getLowerDays());
			map.put("euDrugtype",vo.getEuDrugtype());
			//消耗周期天数
			int days = DateUtils.getDateSpace(DateUtils.strToDate(vo.getDateStart()), DateUtils.strToDate(vo.getDateEnd2()));
			if(days==0){
				days=1;
			}
			map.put("days", new Integer(days));
			map.put("dateStart2", vo.getDateStart().substring(0, 8) + "000000");
			map.put("dateEnd2", vo.getDateEnd2().substring(0, 8) + "235959");
			list = puPlanMapper.qryByConsumer2(map);
			for (PdStoreParam pdst : list){
				if(pdst.getQuanStk() < pdst.getLowQuanMin())
				{
					pdst.setQuanPack(Math.ceil(pdst.getQuanMin()));
					lpsp.add(pdst);
				}
			}
		}
		return lpsp;
	}
	
	/**
	 * 008005001001
	 * 查询采购计划单
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPlanList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		User newUser=(User)user;
		paramMap.put("pkOrg", newUser.getPkOrg());
		return puPlanMapper.qryPlanList(paramMap);
	}
	
	/**
	 * 008005001006
	 * 查询历史采购计划
	 * @param param{ "datePlanBegin":"开始时间(精确到时分秒)", "datePlanEnd":"结束时间(精确到时分秒)" }
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryHistoryPlanList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		User newUser=(User)user;
		paramMap.put("pkStore", newUser.getPkStore());
		return puPlanMapper.qryHistoryPlanList(paramMap);
	}
	
	/**
	 * 008005001007
	 * 查询历史采购订单
	 * @param param{ "datePuBegin":"订单开始日期(精确到时分秒)", "datePuEnd":"订单截止日期(精确到时分秒)" }
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryHistoryOrdList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		User newUser=(User)user;
		paramMap.put("pkOrg", newUser.getPkOrg());
		paramMap.put("pkStore", newUser.getPkStore());
		return puPlanMapper.qryHistoryOrdList(paramMap);
	}
	
	/**
	 * 008005001011
	 * 查询历史采购计划明细
	 * @param param{"pkPdplan":"采购计划主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryHistoryPlanDts(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		User newUser=(User)user;
		paramMap.put("pkStore", newUser.getPkStore());
		return puPlanMapper.qryHistoryPlanDts(paramMap);
	}
	
	/**
	 * 008005001012
	 * 查询历史采购订单明细
	 * @param param{"pkPdpu":"采购订单主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryHistoryOrdDts(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		User newUser=(User)user;
		paramMap.put("pkStore", newUser.getPkStore());
		return puPlanMapper.qryHistoryOrdDts(paramMap);
		
	}
	
	/**
	 * 008005001014
	 * 采购计划上传药监，更新计划数据
	 * @param param
	 * @param user
	 */
	public void updateDrugPlanUp(String param,IUser user){
		PlanUpParam plan=JsonUtil.readValue(param, PlanUpParam.class);
		if(plan==null||CommonUtils.isEmptyString(plan.getPkPdplan())||plan.getPlanUp()==null||plan.getPlanUp().getMx()==null)return;
		String retPlanCode=plan.getPlanUp().getDdjhbh();
		List<String> planDtSqls=new ArrayList<String>();
		for (DrugAdminPlanDtRet planDt : plan.getPlanUp().getMx()) {
			String plandtSql="update pd_plan_detail set code_rtndt='"+planDt.getDdjhmxbh()+"' where pk_pdplan='"+plan.getPkPdplan()+"' and pk_pd='"+planDt.getCgjhdmxbh()+"'";
			planDtSqls.add(plandtSql);
		}
		String planSql="update pd_plan set flag_acc='1' ,code_rtn='"+retPlanCode+"' where pk_pdplan='"+plan.getPkPdplan()+"'";
		
		DataBaseHelper.batchUpdate(planDtSqls.toArray(new String[0]));
		DataBaseHelper.update(planSql, new Object[]{});
	}
	
	/**
	 * 查询药品的单价及箱包装等信息
	 * 交易号：008005001017
	 * @param param
	 * @param user
	 * @return 
	 */
    public List<Map<String, Object>> qryBdPdInfo(String param, IUser user) {
        @SuppressWarnings("unchecked")
		List<String> lstPkOrds = JsonUtil.readValue(param, List.class);

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		Set<String> setPkOrds = new HashSet<String>();
		if(lstPkOrds != null)
		{
	        for(String pkOrd : lstPkOrds) 
			{
	        	setPkOrds.add(pkOrd);
			}
		}
        if(setPkOrds.size() > 0)
        {
        	StringBuilder sb = new StringBuilder();
			sb.append("SELECT bp.price as price, bp.pack_size_max as packsizemax, bp.pk_pd as pkpd FROM BD_PD bp ");
			sb.append(" where (bp.pk_pd IN ( ");
			sb.append(CommonUtils.convertSetToSqlInPart(setPkOrds, "bp.pk_pd"));
			sb.append(")) ");
			String sql = sb.toString();

			retList = DataBaseHelper.queryForList(sql, new Object[]{});
        }
        return retList;
    }
}
