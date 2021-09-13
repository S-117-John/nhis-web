package com.zebone.nhis.scm.material.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.material.dao.MtlDeptPdAppMapper;
import com.zebone.nhis.scm.material.vo.MtlPdPlanDtVo;
import com.zebone.nhis.scm.material.vo.MtlPdPlanVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 物资-发票验收
 * @author chengjia
 *
 */
@Service
public class MtlDeptPdAppService {
	
	@Resource
	private MtlDeptPdAppMapper deptPdAppMapper;
	
	/**
     * 查询科室申请记录008007016001
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<MtlPdPlanVo> queryDeptPdAppList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		
		String begin = paramMap.get("beginDate").toString().substring(0, 8)+"000000";
		String end = paramMap.get("endDate").toString().substring(0, 8)+"235959";
		paramMap.put("beginDate", begin);
		paramMap.put("endDate", end);
		
		return deptPdAppMapper.queryDeptPdAppList(paramMap);
	}	

	
    /**
     * 保存科室领用申请单008007016002
     * @param param
     * @param user
     */
    public MtlPdPlanVo saveDeptPdApp(String param,IUser user){
    	MtlPdPlanVo planVo = JsonUtil.readValue(param, MtlPdPlanVo.class);
    	if(planVo == null ||planVo.getDtList() == null || planVo.getDtList().size()==0) return null;

    	PdPlan pdPlan = new PdPlan();
    	ApplicationUtils.copyProperties(pdPlan, planVo);
    	pdPlan.setFlagPlan("0");
    	//保存
    	if(CommonUtils.isEmptyString(pdPlan.getPkPdplan())){
    		//新增
    		DataBaseHelper.insertBean(pdPlan);
    	}else{
    		//修改
    		DataBaseHelper.updateBeanByPk(pdPlan,false);
    	}
    	
    	String pkPdplan = pdPlan.getPkPdplan();
    	planVo.setPkPdplan(pkPdplan);
    	List<PdPlanDetail> insertList = new ArrayList<PdPlanDetail>();
    	List<PdPlanDetail> updateList = new ArrayList<PdPlanDetail>();
    	for(MtlPdPlanDtVo dtVo:planVo.getDtList()){
    		dtVo.setPkPdplan(pkPdplan);
    		//科室物品申领，单位是1，当前数量=基本数量

    		if(!CommonUtils.isEmptyString(dtVo.getPkPdplandt())){
    			if(dtVo.getStatus()!=null&&dtVo.getStatus().equals("upd")){
        			updateList.add(dtVo);
    			}
    		}else{
    			dtVo.setPkPdplandt(NHISUUID.getKeyId());
	    		ApplicationUtils.setBeanComProperty(dtVo, true);
	    		insertList.add(dtVo);
    		}
    	}
    	//新增明细
    	if(insertList!=null&&insertList.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdPlanDetail.class),insertList);
    	}
    	//更新明细
    	if(updateList!=null&&updateList.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PdPlanDetail.class),updateList);
    	}
    	//删除明细
    	if(planVo.getDelDtList()!=null&&planVo.getDelDtList().size()>0){
    		DataBaseHelper.getJdbcTemplate().batchUpdate("delete from pd_plan_detail where pk_pdplandt = ? ", planVo.getDelDtList());
    	}
    	return planVo;
    }

	/**
     * 查询科室领用申请明细008007016003
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<MtlPdPlanDtVo> queryDeptPdAppDtList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		return deptPdAppMapper.queryDeptPdAppDtList(paramMap);
	}  
	
	/**
     * 查询科室领用消耗记录008007016004
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<MtlPdPlanDtVo> queryPdDtUsedList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<MtlPdPlanDtVo> rtnList= deptPdAppMapper.queryPdDtUsedList(paramMap);
		for(int i=0;rtnList!=null&&i<rtnList.size();i++){
			MtlPdPlanDtVo dtVo=rtnList.get(i);
			Double quanPack=dtVo.getQuanPack();
			if(quanPack!=null){
				quanPack=MathUtils.upRound(quanPack);
				dtVo.setQuanPack(quanPack);
			}
		}
		return rtnList;
	}

	/**
     * 按条件过滤方式查询物品信息008007016005
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<MtlPdPlanDtVo> queryPdListByConds(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		return deptPdAppMapper.queryPdListByConds(paramMap);
	}  
	/**
     * 提交科室领用申请单008007016006
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public void sumbitDeptPdApp(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("pkPdplan")==null||map.get("submitType")==null) return;
		
		String pkPdplan=map.get("pkPdplan").toString();
		String submitType=map.get("submitType").toString();
		String sql="";
		Date ts=new Date();
		if(submitType.equals("submit")){
			sql=" update pd_plan set eu_status='1',ts=? where pk_pdplan=? and eu_status='0'";
		}else{
			sql=" update pd_plan set eu_status='0',ts=? where pk_pdplan=? and eu_status='1'";
		}
		
		DataBaseHelper.update(sql, new Object[]{ts,pkPdplan});
	} 
	
	/**
     * 删除科室领用申请单008007016007
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unchecked")
	public void deleteDeptPdApp(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("pkPdplan")==null) return;
		
		String pkPdplan=map.get("pkPdplan").toString();
		String sql="";
		
		//删除申请单明细
		sql=" delete from  pd_plan_detail" +
		    "  where pk_pdplan = ? and "+
		          " exists (select 1 "+
	                        " from pd_plan pla "+
	                       " where pd_plan_detail.pk_pdplan=pla.pk_pdplan and "+
	                       "       pla.eu_status='0')";
		DataBaseHelper.execute(sql, new Object[]{pkPdplan});

		//删除申请单
		sql=" delete from  pd_plan where pk_pdplan = ? and eu_status='0'";
		DataBaseHelper.execute(sql, new Object[]{pkPdplan});

	} 	
}
