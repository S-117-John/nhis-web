package com.zebone.nhis.scm.material.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.module.scm.support.ScmConstant;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.material.dao.MtlPdPlanMapper;
import com.zebone.nhis.scm.material.vo.MtlQryPdPlanVo;
import com.zebone.nhis.scm.material.vo.MtlSavePdPlanVo;
import com.zebone.nhis.scm.material.vo.MtlPdPlanDtInfo;
import com.zebone.nhis.scm.material.vo.MtlPdPlanInfo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 物资耗材 - 采购计划
 * @author wj
 *
 */
@Service
public class MtlPdPlanService {
	
	@Autowired
	private MtlPdPlanMapper pdPlanMapper;
	
	/**
	 * 交易号：008007002007
	 * 删除 采购计划
	 * @param param
	 * @param user
	 * @return void
	 * @author wj
	 * @date 2018年07月18日
	 */
	@SuppressWarnings("unchecked")
	public void delPdPuPlan(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到删除采购计划的相关入参！");
		if(null == map.get("pkPdplan") || CommonUtils.isEmptyString(map.get("pkPdplan").toString()))
			throw new BusException("未获取待删除的采购计划主键！");
		String pkPdplan = map.get("pkPdplan").toString();
		
		//删除计划单明细
		DataBaseHelper.execute("delete from pd_plan_detail  where pd_plan_detail.pk_pdplan=? "
				+ "and exists (select 1 from pd_plan  "
				+ "where pd_plan_detail.pk_pdplan=pd_plan.pk_pdplan and pd_plan.eu_status=0 and pd_plan.del_flag=0)", pkPdplan);
		//删除计划单
		DataBaseHelper.execute("delete from pd_plan  "
				+ "where pd_plan.pk_pdplan=? and pd_plan.eu_status=0 and pd_plan.del_flag =0 ", pkPdplan);
	}
	
	/**
	 * 交易号：008007002008 保存 采购计划
	 * 
	 * @param param
	 * @param user
	 * @return MtlQryPdPlanVo
	 * @author wj
	 * @date 2018年07月18日
	 */
	public MtlQryPdPlanVo savePdPuPlan(String param, IUser user) {
		MtlSavePdPlanVo savePdPlan = JsonUtil.readValue(param,
				MtlSavePdPlanVo.class);
		// 1、校验入参是否正确
		if (savePdPlan == null)
			throw new BusException("未获取到待保存的采购计划相关入参！");
		PdPlan pdPlan = savePdPlan.getPdPlan();
		if (pdPlan == null)
			throw new BusException("未获取到待保存的采购计划相关内容！");
		if (CommonUtils.isEmptyString(pdPlan.getCodePlan()))
			throw new BusException("待保存的采购计划单号为空！");
		List<PdPlanDetail> dtlist = savePdPlan.getPdPlanDtList();
		if (dtlist == null)
			throw new BusException("未获取到待保存的采购计划明细！");
		if (dtlist.size() < 1)
			throw new BusException("采购计划明细至少有一条数据！");

		// 2、保存采购计划
		int cnt_code = DataBaseHelper.queryForScalar(
				"select count(1) from pd_plan "
						+ "where del_flag='0' and code_plan=?", Integer.class,
				pdPlan.getCodePlan());
		if ((CommonUtils.isEmptyString(pdPlan.getPkPdplan()) && cnt_code > 0)
				|| (!CommonUtils.isEmptyString(pdPlan.getPkPdplan()) && cnt_code > 1))
			throw new BusException("当前采购计划单号码【" + pdPlan.getCodePlan()
					+ "】，全局不唯一！");

		String pkStore = UserContext.getUser().getPkStore();
		String pkDept = UserContext.getUser().getPkDept();
		if (CommonUtils.isEmptyString(pkStore))
			throw new BusException("当前仓库不存在！");

		boolean flagNew = CommonUtils.isEmptyString(pdPlan.getPkPdplan());
		pdPlan.setPkDept(pkDept);
		pdPlan.setPkStore(pkStore);
		pdPlan.setDtPlantype(ScmConstant.PLAN_DT_PLANTYPE_0101);
		pdPlan.setPkEmpMak(UserContext.getUser().getPkEmp());
		pdPlan.setNameEmpMak(UserContext.getUser().getNameEmp());
		if (flagNew)
			DataBaseHelper.insertBean(pdPlan);
		else
			DataBaseHelper.updateBeanByPk(pdPlan, false);

		String pkPdplan = pdPlan.getPkPdplan();
		// 3、保存采购计划明细
		if (flagNew) {
			for (PdPlanDetail dt : dtlist) {
				dt.setPkPdplan(pdPlan.getPkPdplan());
				DataBaseHelper.insertBean(dt);
			}
		} else {
			// 3.2.1 删除不在当前已存在的主键的数据
			String pkPlandts = "";
			for (PdPlanDetail dt : dtlist) {
				if (!CommonUtils.isEmptyString(dt.getPkPdplandt()))
				{
					pkPlandts += "'" + dt.getPkPdplandt() + "',";
				}				
			}
			if (!CommonUtils.isEmptyString(pkPlandts)) {
				pkPlandts = pkPlandts.substring(0, pkPlandts.length() - 1);
				DataBaseHelper.execute("delete from pd_plan_detail "
						+ "where pk_pdplandt not in (" + pkPlandts
						+ ") and  pk_pdplan=? and del_flag ='0' ", pkPdplan);
			}

			// 通过主键获取采购计划及明细
			List<String> databaselist = new ArrayList<>(); //从数据库获取明细表中主键集合
			MtlQryPdPlanVo allinfo = qryPdPlanAllInfo(pkPdplan);
			if (allinfo != null) {
				List<MtlPdPlanDtInfo> pdPlanDtList = allinfo.getPdPlanDtList();
				if (pdPlanDtList != null && pdPlanDtList.size() > 0) {
					for (MtlPdPlanDtInfo mtlPdPlanDtInfo : pdPlanDtList) {
						databaselist.add(mtlPdPlanDtInfo.getPkPdplandt());
					}							
				}
			}

			// 3.2.2 插入|更新最新明细
			int i = 1;
			for (PdPlanDetail dt : dtlist) {
				dt.setPkPdplan(pdPlan.getPkPdplan());
				dt.setQuanDe(new Double("0"));
				dt.setFlagFinish("0");
				dt.setDelFlag("0");
				dt.setSortNo(i); // 排序，从1开始
				if (CommonUtils.isEmptyString(dt.getPkPdplandt()))
				{
					DataBaseHelper.insertBean(dt);
				}else{
					DataBaseHelper.updateBeanByPk(dt, false);
					if(databaselist.contains(dt.getPkPdplandt())){
						databaselist.remove(dt.getPkPdplandt());					
					}
					
				}
				i++;
			}
			
			if(databaselist.size() > 0){	
				for (String pkstring : databaselist) {
					DataBaseHelper.execute("update pd_plan_detail set del_flag ='1' where pk_pdplandt = ?",pkstring);
				}
			}						
			
		}

		// 4、如果采购计划单由科室提交的请领单生成，要更新需求汇总标志；
		String pkPdPlanList = savePdPlan.getPkPlanList();
		if (!CommonUtils.isEmptyString(pkPdPlanList)) {
			DataBaseHelper.update("update pd_plan set flag_plan='1' where pk_pdplan in ("+pkPdPlanList+") and dt_plantype='0201' and flag_plan='0'");
		}

		// 5、保存后获取最新内容返回前台用于展示
		MtlQryPdPlanVo allinfo = qryPdPlanAllInfo(pkPdplan);
		return allinfo;
	}

	/**
	 * 根据采购计划主键，查询采购计划信息
	 * @param pkPdplan
	 * @return
	 */
	private MtlQryPdPlanVo qryPdPlanAllInfo(String pkPdplan) {
		if(CommonUtils.isEmptyString(pkPdplan))
			throw new BusException("待查询的采购计划主键为空！");
		MtlQryPdPlanVo pdPlanInfo = new MtlQryPdPlanVo();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPdplan", pkPdplan);
		List<MtlPdPlanInfo> planList = pdPlanMapper.queryPdPlanList(map);
		if(planList == null || planList.size() < 1)
			throw new BusException("未获取到采购计划！");
		pdPlanInfo.setPdPlan(planList.get(0));
		List<MtlPdPlanDtInfo> planDtList = pdPlanMapper.queryPdPlanDtList(map);
		if(planDtList == null || planDtList.size() < 1)
			throw new BusException("未获取到采购计划明细！");
		pdPlanInfo.setPdPlanDtList(planDtList);;
		return pdPlanInfo;
	}

	/**
	 * 交易号：008007002009
	 * 提交采购计划单
	 * @param  param
	 * @param  user
	 * @return void
	 * @author wj
	 * @date 2018年07月18日
	 */
	public void updatePdPlan(String param, IUser user){
		User u = UserContext.getUser();
		PdPlan plan = JsonUtil.readValue(param, PdPlan.class);
		if(plan == null)
			throw new BusException("未获取到待提交的采购计划相关入参！");
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("modifier", u.getPkEmp());		
		mapParam.put("ts", new Date());
		mapParam.put("pkPdplan", plan.getPkPdplan());

		DataBaseHelper.update("update pd_plan set eu_status = '1', modifier=:modifier, ts=:ts where pk_pdplan=:pkPdplan and eu_status='0'", mapParam);
	}

	/**
	 * 交易码：008007002001
	 * 查询科室请领记录
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryDeptAppList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryDeptAppList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002002
	 * 查询需求汇总明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> querySumNeedList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到查询需求汇总明细的相关入参！");
		if(null == map.get("pkPdplan") || CommonUtils.isEmptyString(map.get("pkPdplan").toString()))
			throw new BusException("未获取待查询的采购计划主键！");
		List<Map<String,Object>> list = pdPlanMapper.querySumNeedList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002003
	 * 查询物品在途数量
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPdOnUsedQuan(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryPdOnUsedQuan(map);
		return list;
	}
	
	/**
	 * 交易码：008007002004
	 * 查询物品在途明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPdOnUsedList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryPdOnUsedList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002005
	 * 查询采购计划列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MtlPdPlanInfo> queryPdPlanList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<MtlPdPlanInfo> list = pdPlanMapper.queryPdPlanList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002006
	 * 查询采购计划列表 - 明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MtlPdPlanDtInfo> queryPdPlanDtList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<MtlPdPlanDtInfo> list = pdPlanMapper.queryPdPlanDtList(map);
		return list;
	}

	/**
	 * 交易码：008007002011
	 * 查询历史采购计划
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryHisPlanList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryHisPuPlanList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002012
	 * 查询历史采购计划 - 明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryHisPlanDtList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryHisPuPlanDtList(map);
		return list;
	}

	/**
	 * 交易码：008007002013
	 * 查询历史采购订单
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryHisOrdList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryHisPuOrdList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002014
	 * 查询历史采购订单 - 明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryHisOrdDtList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryHisPuOrdDtList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002010
	 * 消耗计算-按0消耗|1采购
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryNeedsByUsed(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null) 
			throw new BusException("未获取到消耗计算的相关入参！");
		if(null == map.get("cntType") || CommonUtils.isEmptyString(map.get("cntType").toString()))
			throw new BusException("未获取消耗计算的计算方式！");
		String cntType = map.get("cntType").toString();
		map.put("dateNow", DateUtils.getDate("yyyyMMddHHmmss"));//传入当前时间，用于过滤物品是否注册效期已过
		List<Map<String,Object>> list = null;
		if("0".equals(cntType))
			list = pdPlanMapper.queryNeedsByUsed(map);
		else if("1".equals(cntType))
			list = pdPlanMapper.queryNeedsByPu(map);
		return list;
	}
	
	/**
	 * 交易码：008007002015
	 * 按条件查询物品
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPdPlanByCon(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryPdPlanByCon(map);
		return list;
	}
	
	/**
	 * 交易码：008007002016
	 * 查询库存下限物品
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPdPlanByMin(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryPdPlanByMin(map);
		return list;
	}
	
	/**
	 * 交易码：008007002017
	 * 查询未过注册效期的物品
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryValidPdList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryValidPdList(map);
		return list;
	}
	
	/**
	 * 交易码：008007002018
	 * 查询未过注册效期和许可效期的供应商
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryValidSupList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = pdPlanMapper.queryValidSupList(map);
		return list;
	}
	
	/**
	 * 获取供应商经营效期、许可效期 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> querySupplyInfo(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNull(map.get("pkSuppler"))){
			throw new BusException("未获取到供应商主键信息！");
		}
		String pkSuppler = map.get("pkSuppler").toString();
		Map<String, Object> returnMap = DataBaseHelper.queryForMap("select * from bd_supplyer where del_flag = '0' and pk_supplyer = ?", pkSuppler);
		return returnMap;
	}
}
