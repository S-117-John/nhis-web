package com.zebone.nhis.scm.material.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.material.dao.MtlAllAppMapper;
import com.zebone.nhis.scm.material.vo.MtlAllAppDtVo;
import com.zebone.nhis.scm.material.vo.MtlAllAppVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
 
/**
 * 调拨申请
 * @author wj
 */
@Service
public class MtlAllAppService {
    @Resource
	private MtlAllAppMapper allocatAppMapper;
   
    /**
     * 根据不同条件查询调拨计划
     * @param param{datePlanBegin，datePlanEnd，pkEmpMak，euStatus，codePlan}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlAllAppVo> queryAllocationList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	map.put("pkStore", ((User)user).getPkStore());
    	if(CommonUtils.isNotNull(map.get("datePlanEnd"))){
    		map.put("datePlanEnd",CommonUtils.getString(map.get("datePlanEnd")).substring(0, 8)+"235959");
    	}
    	List<MtlAllAppVo> list = allocatAppMapper.queryAllocationPlanList(map);
    	return list;
    }
    /**
     * 查询调拨明细
     * @param param{pkPdplan}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlAllAppDtVo> queryAllocationPlanDt(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	map.put("pkStore", ((User)user).getPkStore());

    	return allocatAppMapper.queryAllocationPlanDt(map);
    }
    /**
     * 查询库存下限物品列表
     * @param param{pkStore}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlAllAppDtVo> queryStockMinList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到仓库主键");
    	return allocatAppMapper.queryStockMinList(map);
    }
    /**
     * 查询历史调拨申请（默认当年）
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlAllAppVo> queryHisApp(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	map.put("pkStore", ((User)user).getPkStore());
    	String year = DateUtils.getCurrYear();
    	map.put("dateBegin", year+"0101000000");
    	map.put("dateEnd", year+"1231235959");
    	return allocatAppMapper.queryHisAllocationPlan(map);
    }
    /**
     * 查询历史调拨申请明细
     * @param {pkPdplan}
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlAllAppDtVo> queryHisAppDt(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到调拨申请单主键");
    	return allocatAppMapper.queryHisAllocationPlanDt(map);
    }
    /**
     * 根据条件查询物品信息
     * @param param{euDrugtype，dtPois，dtAbrd，dtAnti，minFlag,pkStore}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlAllAppDtVo> queryPdList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到查询条件！");
    	return allocatAppMapper.queryPdByCon(map);
    }
    
    /**
     * 保存调拨申请
     * @param param
     * @param user
     */
    public String saveAllocation(String param,IUser user){
    	MtlAllAppVo appvo = JsonUtil.readValue(param, MtlAllAppVo.class);
    	if(appvo == null ||appvo.getDtlist() == null || appvo.getDtlist().size()<=0){
    		throw new BusException("未获取到调拨申请明细数据！");
    	}
    	PdPlan planvo = new PdPlan();
    	ApplicationUtils.copyProperties(planvo, appvo);
    	//保存调拨计划
    	if(CommonUtils.isEmptyString(planvo.getPkPdplan())){//新增
    		planvo.setDtPlantype("0301");
        	planvo.setFlagAcc("0");
        	planvo.setEuStatus("0");
    		DataBaseHelper.insertBean(planvo);
    	}else{
    		DataBaseHelper.updateBeanByPk(planvo,false);
    	}
    	
    	String pkplan = planvo.getPkPdplan();
    	List<PdPlanDetail> dtlist = new ArrayList<PdPlanDetail>();
    	List<PdPlanDetail> updatelist = new ArrayList<PdPlanDetail>();
    	for(MtlAllAppDtVo dt : appvo.getDtlist()){
    		if(!CommonUtils.isEmptyString(dt.getPkPdplandt())){
    		     updatelist.add(dt);
    		}else{
    		ApplicationUtils.setBeanComProperty(dt, true);
    		dt.setPkPdplan(pkplan);
    		dt.setPkPdplandt(NHISUUID.getKeyId());
    		dt.setFlagFinish("0");
    		dt.setQuanDe(0.00);
    		dtlist.add(dt);
    		}
    	}
    	//保存明细
    	if(dtlist!=null&&dtlist.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdPlanDetail.class),dtlist);
    	}
    	//更新明细
    	if(updatelist!=null&&updatelist.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PdPlanDetail.class),updatelist);
    	}
    	//删除明细
    	if(appvo.getDelDtList()!=null&&appvo.getDelDtList().size()>0){
    		DataBaseHelper.getJdbcTemplate().batchUpdate("delete from pd_plan_detail where pk_pdplandt = ? ", appvo.getDelDtList());
    	}
    	return pkplan;
    }
    
    /**
     * 删除调拨申请
     * @param param{pkPdplan}
     * @param user
     */
    public void deleteAllocation(String param,IUser user){
    	String pk_pdplan = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_pdplan)) throw new BusException("未获取到调拨申请单主键！");
    	StringBuilder deldt_sql = new StringBuilder("delete from pd_plan_detail  where pd_plan_detail.pk_pdplan = ? and ");
    	deldt_sql.append(" exists (select 1   from pd_plan   where pd_plan_detail.pk_pdplan=pd_plan.pk_pdplan ");
    	deldt_sql.append("and pd_plan.eu_status='0')");
    	DataBaseHelper.execute(deldt_sql.toString(), new Object[]{pk_pdplan});
		String delapp_sql = "delete from pd_plan  where pk_pdplan = ? and eu_status = '0'";
		DataBaseHelper.execute(delapp_sql, new Object[]{pk_pdplan});
    }
    
    /**
     * 提交调拨申请
     * @param param{pk_pdplan}
     * @param user
     */
    public void submitAllocation(String param,IUser user){
    	String pk_pdplan = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_pdplan)) throw new BusException("未获取到调拨申请单主键！");
    	StringBuilder update = new StringBuilder("update pd_plan  set  ");
    	update.append(" ts = to_date('").append(DateUtils.getDefaultDateFormat().format(new Date())).append("','YYYYMMDDHH24MISS'),eu_status='1' ");
    	update.append(" where pk_pdplan = ? and eu_status='0'");
    	DataBaseHelper.update(update.toString(), new Object[]{pk_pdplan});
    }
    
    /**
     * 取消提交
     * @param param{pk_pdplan}
     * @param user
     */
    public void cancelSubAllo(String param,IUser user){
    	String pk_pdplan = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_pdplan)) throw new BusException("未获取到调拨申请单主键！");
    	StringBuilder update = new StringBuilder("update pd_plan  set eu_status='0' ");
    	update.append(",ts = to_date('").append(DateUtils.getDefaultDateFormat().format(new Date())).append("','YYYYMMDDHH24MISS') " );
    	update.append("where pk_pdplan = ? and eu_status='1'");
    	DataBaseHelper.update(update.toString(), new Object[]{pk_pdplan});
    }
    
    /**
     * 根据某个物品和服务仓库，查询库存量
     * @param param{pkStore，pkPd}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public Double getQuanStk(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到获取库存量的条件！");
    	List<Map<String,Object>> result = allocatAppMapper.queryQuanStk(map);
    	if(result!=null&&result.size()>0){
    		if(result.get(0)==null) return 0.00;
    		return CommonUtils.getDouble(result.get(0).get("quanStk"));
    	}
    	return 0.00;
    }
    
    /**
     * 通过消耗计算查询调拔申请
     */
    @SuppressWarnings("unchecked")
	public List<MtlAllAppDtVo> getPdStoreByConsumers(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(null == map)
			throw new BusException("未获取到消耗计算的入参！");
		String cntType = map.get("cntType").toString();
		List<MtlAllAppDtVo> dtList = new ArrayList<MtlAllAppDtVo>();
		if(!"0".equals(cntType) && !"1".equals(cntType)){
			throw new BusException("cntType传入错误！");
		}
		map.put("euDirect","0".equals(cntType)?"-1":"1");
    	return allocatAppMapper.queryQuanNeedByPu(map);
    }
}
