package com.zebone.nhis.scm.st.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.pub.service.PdStPubService;
import com.zebone.nhis.scm.st.dao.AllocationAppMapper;
import com.zebone.nhis.scm.st.vo.AllocationAppDtVo;
import com.zebone.nhis.scm.st.vo.AllocationAppVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 调拨申请
 * @author yangxue
 *
 */
@Service
public class AllocationAppService {
    @Resource
	private AllocationAppMapper allocationAppMapper;
    @Resource
    private PdStPubService pdStPubService;
    /**
     * 根据不同条件查询调拨计划
     * @param param{datePlanBegin，datePlanEnd，pkEmpMak，euStatus，codePlan}
     * @param user
     * @return
     */
    public List<AllocationAppVo> queryAllocationList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(CommonUtils.isNotNull(map.get("datePlanEnd"))){
    		map.put("datePlanEnd",CommonUtils.getString(map.get("datePlanEnd")).substring(0, 8)+"235959");
    	}
    	if(CommonUtils.isNull(map.get("dtPlantype"))){
    		map.put("pkStore", ((User)user).getPkStore());
    		map.put("dtPlantype", "0301");
    	}
    	return allocationAppMapper.queryAllocationPlanList(map);
    }
    /**
     * 查询调拨明细
     * @param param{pkPdplan}
     * @param user
     * @return
     */
    public List<AllocationAppDtVo> queryAllocationPlanDt(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	map.put("pkStoreSelf", ((User)user).getPkStore());
    	return allocationAppMapper.queryAllocationPlanDt(map);
    }
    /**
     * 查询库存下限物品列表
     * @param param{pkStore}
     * @param user
     * @return
     */
    public List<AllocationAppDtVo> queryStockMinList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到仓库主键");
    	map.put("pkStoreSelf", ((User)user).getPkStore());
    	return allocationAppMapper.queryStockMinList(map);
    }
    /**
     * 查询历史调拨申请（默认当年）
     * @param param
     * @param user
     * @return
     */
    public List<AllocationAppVo> queryHisApp(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(CommonUtils.isNull(map.get("dtPlantype"))){
    		map.put("pkStore", ((User)user).getPkStore());
    		map.put("dtPlantype", "0301");
    	}
    	String year = DateUtils.getCurrYear();
    	if(CommonUtils.isNull(map.get("dateBegin"))){
    		map.put("dateBegin", year+"0101000000");
    	}
    	if(CommonUtils.isNull(map.get("dateEnd"))){
    		map.put("dateEnd", year+"1231235959");
    	}
    	map.put("pkStoreSelf", ((User)user).getPkStore());
    	return allocationAppMapper.queryHisAllocationPlan(map);
    }
    /**
     * 查询历史调拨申请明细
     * @param {pkPdplan}
     * @return
     */
    public List<AllocationAppDtVo> queryHisAppDt(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到调拨申请单主键");
    	map.put("pkStoreSelf", ((User)user).getPkStore());
    	return allocationAppMapper.queryHisAllocationPlanDt(map);
    }
    /**
     * 根据条件查询物品信息
     * @param param{euDrugtype，dtPois，dtAbrd，dtAnti，minFlag,pkStore}
     * @param user
     * @return
     */
    public List<AllocationAppDtVo> queryPdList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到查询条件！");
    	map.put("pkStoreSelf", ((User)user).getPkStore());
    	return allocationAppMapper.queryPdByCon(map);
    }
    /**
     * 保存调拨申请
     * @param param
     * @param user
     */
    public String saveAllocation(String param,IUser user){
    	AllocationAppVo appvo = JsonUtil.readValue(param, AllocationAppVo.class);
    	if(appvo == null ||appvo.getDtlist() == null || appvo.getDtlist().size()<=0){
    		throw new BusException("未获取到调拨申请明细数据！");
    	}
    	//保存前校验，明细中的物品是否在当前仓库物品字典中存在
    	List<String> pkPdlist = new ArrayList<String>();
    	for(AllocationAppDtVo dt : appvo.getDtlist()){
    		pkPdlist.add(dt.getPkPd());
    	}
    	if(!"0201".equals(appvo.getDtPlantype())){//科室申请不校验
    		List<Map<String,Object>> pdlist = pdStPubService.verfyPdIsInStore(((User)user).getPkStore(), ((User)user).getPkDept(), pkPdlist);
        	//传入的调拨明细与仓库中获取到的物品数量不等，则说明存在当前仓库不存在的物品，需要提示
        	if(pdlist==null||pdlist.size()!=pkPdlist.size()){
        		StringBuilder pdName = new StringBuilder("");
        		for(AllocationAppDtVo dt:appvo.getDtlist()){
        			boolean hasFlag = false;
        			for(Map<String,Object> pdMap:pdlist){
        				if(dt.getPkPd().equals(CommonUtils.getString(pdMap.get("pkPd")))){
            				hasFlag = true;
            				break;
            			}
        			}
        			if(!hasFlag){
        				pdName.append("【").append(dt.getPdname()).append("】\n");
        			}
        		}
        		if(pdName.toString().length()>4)
        			throw new BusException(""+pdName.toString()+"在当前仓库物品字典中不存在，\n需先添加后方可申请调拨！");
        	}
    	}
    	PdPlan planvo = new PdPlan();
    	ApplicationUtils.copyProperties(planvo, appvo);
    	//保存调拨计划
    	if(CommonUtils.isEmptyString(planvo.getPkPdplan())){//新增
    		//planvo.setDtPlantype("0301");
        	planvo.setFlagAcc("0");
        	planvo.setEuStatus("0");
    		DataBaseHelper.insertBean(planvo);
    	}else{
    		DataBaseHelper.updateBeanByPk(planvo,false);
    	}
    	
    	String pkplan = planvo.getPkPdplan();
    	List<PdPlanDetail> dtlist = new ArrayList<PdPlanDetail>();
    	List<PdPlanDetail> updatelist = new ArrayList<PdPlanDetail>();
    	for(AllocationAppDtVo dt:appvo.getDtlist()){
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
		ExtSystemProcessUtils.processExtMethod("HRPSERVICE", "sendAllocationRequest",pk_pdplan);
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
    public Map<String,Object> getQuanStk(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到获取库存量的条件！");
    	List<Map<String,Object>> result = allocationAppMapper.queryQuanStk(map);
    	map.put("pkStoreSelf", UserContext.getUser().getPkStore());
    	List<Map<String,Object>> quanStkSelf=allocationAppMapper.queryStoreQuanStk(map);
    	Map<String,Object> resMap=new HashMap<String,Object>();
    	if(result!=null&&result.size()>0){
    		if(result.get(0)==null ||result.get(0).get("quanStk")==null)resMap.put("quanStk", 0.00);
    		resMap.put("quanStk", CommonUtils.getDouble(result.get(0).get("quanStk")));
    	}
    	if(quanStkSelf!=null && quanStkSelf.size()>0){
    		if(quanStkSelf.get(0)==null ||quanStkSelf.get(0).get("quan")==null)resMap.put("quanStkSelf", 0.00);
    		resMap.put("quanStkSelf", CommonUtils.getDouble(quanStkSelf.get(0).get("quan")));
    	}
    	return resMap;
    }
    
    /**
     * 通过消耗计算查询调拔申请
     */
    public List<AllocationAppDtVo> getPdStoreByConsumers(String param,IUser user){
    	AllocationAppDtVo vo = JsonUtil.readValue(param, AllocationAppDtVo.class);
    	User u = UserContext.getUser();
		String accounts = vo.getAccounts();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkStore", u.getPkStore());
		List<AllocationAppDtVo> laadv = new ArrayList<AllocationAppDtVo>();
		List<AllocationAppDtVo> list = new ArrayList<AllocationAppDtVo>();
		if(CommonUtils.isNotNull(vo.getDtSttypes())) {
			map.put("dtSttypes", vo.getDtSttypes());
		}
		if ("1".equals(vo.getCalculateWay())) {
			map.put("cnt", vo.getCnt());
			map.put("cntPlan", vo.getCntPlan());
			String endDate=vo.getDateEnd().substring(0,8);
			map.put("dateStart", endDate+"000000");
			map.put("dateEnd", endDate+"235959");
			map.put("targetStore", vo.getTargetStore());
			map.put("pkStoreSelf", ((User)user).getPkStore());
			if("0".equals(accounts)){
				laadv = allocationAppMapper.qryByConsumer(map);
			}else if("1".equals(accounts)){
				laadv = allocationAppMapper.qryByInstore(map);
			}
			//向上取整
			for (int i = 0; i < laadv.size(); i++) {
				AllocationAppDtVo pdStore=laadv.get(i);
				pdStore.setQuanPack(Math.ceil(pdStore.getQuanPack()));
			}
		}else{
			map.put("upperDays", vo.getUpperDays());
			map.put("lowerDays", vo.getLowerDays());
			map.put("euDrugtype",vo.getEuDrugtype());
			//消耗周期天数
			int days = DateUtils.getDateSpace(DateUtils.strToDate(vo.getDateStart()), DateUtils.strToDate(vo.getDateEnd2()));
			map.put("days", new Integer(days));
			map.put("dateStart2", vo.getDateStart().substring(0, 8) + "000000");
			map.put("dateEnd2", vo.getDateEnd2().substring(0, 8) + "235959");
			list = allocationAppMapper.qryByConsumer2(map);
			for (AllocationAppDtVo pdst : list){
				if(pdst.getQuanStk() < pdst.getLowQuanMin() && pdst.getQuanMin()>0)
				{
					pdst.setQuanPack(Math.ceil(pdst.getQuanMin()));
					laadv.add(pdst);
				}
			}
		}
		if("1".equals(vo.getIsStockMax())){
			laadv=laadv.stream().filter(m->CommonUtils.getDouble(m.getQuanStkSelf())<=m.getQuanPack().doubleValue()).collect(Collectors.toList());
		}
    	return laadv;
    }
    /**
     * 通过记费记录，生成科室请领明细
     */
    public List<AllocationAppDtVo> getPdStoreByCg(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<AllocationAppDtVo> laadv = new ArrayList<AllocationAppDtVo>();
	    laadv = allocationAppMapper.queryDeptAppByCg(map);
		//向上取整
		for (int i = 0; i < laadv.size(); i++) {
			AllocationAppDtVo pdStore=laadv.get(i);
			pdStore.setQuanPack(Math.ceil(pdStore.getQuanPack()));
		}
    	return laadv;
    }
    /**
     * 取基数药目录
     * @param param
     * @param user
     * @return
     */
    public List<AllocationAppDtVo> getPdBaseList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<AllocationAppDtVo> laadv = allocationAppMapper.queryDeptPdBase(map);
    	return laadv;
    }
}
