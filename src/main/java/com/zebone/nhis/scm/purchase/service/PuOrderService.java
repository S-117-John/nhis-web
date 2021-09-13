package com.zebone.nhis.scm.purchase.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.purchase.PdPurchase;
import com.zebone.nhis.common.module.scm.purchase.PdPurchaseDt;
import com.zebone.nhis.scm.pub.vo.PdPlanDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderVo;
import com.zebone.nhis.scm.purchase.dao.PuOrderMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 采购订单
 * @author yangxue
 *
 */
@Service
public class PuOrderService {
	@Resource
	private PuOrderMapper puOrderMapper;
	/**
	 * 采购计划查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPuPlan(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		//查询已提交采购计划
		map.put("planType", IDictCodeConst.DT_PLANTYPE_BUY);//基础字典
		map.put("pkOrg", ((User)user).getPkOrg());
		map.put("flagChk",ApplicationUtils.getSysparam("SCM0006",false));
		List<Map<String,Object>> list = puOrderMapper.queryPuPlanByCon(map);
		if(list!=null&&list.size()>0){
			for(Map<String,Object> result_map:list){
				result_map.put("dtlist", puOrderMapper.queryPuPlanDtByCon(result_map));
			}
		}
		return list;
	}
	/**
	 * 根据采购计划，查询采购明细
	 * @param param{pkPdplans，pkSupplyer}
	 * @param user
	 * @return
	 */
	public List<PdPlanDtVo> queryPuPlanDt(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		return puOrderMapper.queryPuPlanDtByCon(map);
	}

	/**
	 * 退回采购计划
	 * @param param{pkPdplan}
	 * @param user
	 */
	public void rtnPuPlan(String param,IUser user){
		String pkPdPlan = JsonUtil.readValue(param, String.class);
	    String sql = "update pd_plan  set eu_status='0', ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') where pk_pdplan=? and eu_status='1'";
	    DataBaseHelper.update(sql,new Object[]{pkPdPlan});
	}
	/**
	 * 修改采购计划
	 * @param param{dtlist}
	 * @param user
	 */
	public void updatePuPlanDt(String param,IUser user){
		List<PdPlanDtVo> dtlist = JsonUtil.readValue(param, new TypeReference<List<PdPlanDtVo>>(){});
		if(dtlist == null||dtlist.size()<=0)return;
		String[] sqls = new String[dtlist.size()];
		for(int i=0;i<dtlist.size();i++){
			sqls[i] = "update pd_plan_detail set quan_pack = "+dtlist.get(i).getQuanPack()+",quan_min = "+dtlist.get(i).getQuanPack()+"*pack_size "
					+ ",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
							+ " where pk_pdplandt = '"+dtlist.get(i).getPkPdplandt()+"'";
		}
		DataBaseHelper.batchUpdate(sqls);
	}
	/**
	 * 保存采购订单
	 * @param param{PuOrderVo,}
	 * @param user
	 */
	public PuOrderVo savePuOrder(String param,IUser user){
		PuOrderVo vo = JsonUtil.readValue(param, PuOrderVo.class);
		if(vo == null ||vo.getDt().size()<=0) throw new BusException("未获取到需要保存的数据");
		PdPurchase order = new PdPurchase();
		ApplicationUtils.copyProperties(order, vo);
		if(!CommonUtils.isEmptyString(vo.getPkPdpu())){//修改的保存
			DataBaseHelper.updateBeanByPk(order,false);
			List<PuOrderDtVo> list = vo.getDt();
			//更新的执行更新，插入的执行插入
			List<PdPurchaseDt> insert_list = new ArrayList<PdPurchaseDt>();
			List<PdPurchaseDt> update_list = new ArrayList<PdPurchaseDt>();
			for(PuOrderDtVo dtvo:list){
				String pk_pudt = dtvo.getPkPdpudt();
				if(!CommonUtils.isEmptyString(pk_pudt)){//更新
					update_list.add(dtvo);
				}else{
					dtvo.setPkPdpu(vo.getPkPdpu());
					dtvo.setPkPdpudt(NHISUUID.getKeyId());
					ApplicationUtils.setBeanComProperty(dtvo,true);
					insert_list.add(dtvo);
				}
			}
			if(insert_list!=null&&insert_list.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdPurchaseDt.class), insert_list);
			}
			if(update_list!=null&&update_list.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PdPurchaseDt.class), update_list);
			}
		}else{//新增的保存
			ApplicationUtils.setBeanComProperty(order, true);
			DataBaseHelper.insertBean(order);
			List<PuOrderDtVo> list = vo.getDt();
			String pk_pdpu = order.getPkPdpu();
			for(PuOrderDtVo dtvo:list){
				dtvo.setPkPdpu(pk_pdpu);
				dtvo.setPkPdpudt(NHISUUID.getKeyId());
				ApplicationUtils.setBeanComProperty(dtvo,true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdPurchaseDt.class), list);
		}
		if(vo.getDelDtList()!=null&&vo.getDelDtList().size()>0){
			DataBaseHelper.getJdbcTemplate().batchUpdate("delete from pd_purchase_dt where pk_pdpudt = ? ", vo.getDelDtList());
		}
		vo.setPkPdpu(order.getPkPdpu());
		
		return vo;
	}
	/**
	 * 审核采购订单
	 * @param param
	 * @param user
	 */
	public PuOrderVo approvePuOrder(String param,IUser user){
		PuOrderVo vo = JsonUtil.readValue(param, PuOrderVo.class);
		String pk_pdpu = vo.getPkPdpu();
	    if(!CommonUtils.isEmptyString((vo.getPkPdPlan()))){//由采购计划生成的订单，需要插入并更新采购计划
	    	PuOrderVo result = savePuOrder(param,user);//插入采购计划
	    	if(result == null) return null;
	    	List<PuOrderDtVo> dtlist = result.getDt();
	    	if(dtlist==null||dtlist.size()<=0) return null;
	    	Set<String> sqls = new HashSet<String>();
	    	String[] sqlsdt = new String[dtlist.size()];
			for(int i=0;i<dtlist.size();i++){
				sqlsdt[i] = "update pd_plan_detail set pk_pdpudt = '"+dtlist.get(i).getPkPdpudt()+"' ,flag_finish='1'"
						+",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
						+ " where pk_pdplandt = '"+dtlist.get(i).getPkPdplandt()+"'";
				sqls.add("update pd_plan set eu_status = '2',flag_acc='1' "
						+",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
						+ "where pk_pdplan = '"+dtlist.get(i).getPkPdplan()+"'");
			}
			DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));//更新采购计划
			DataBaseHelper.batchUpdate(sqlsdt);//更新明细
			if(result!=null)
				vo = result;
			pk_pdpu = result.getPkPdpu();
		}
	    //更新采购订单信息
	    String update_pu_sql = "update pd_purchase set eu_status='1',flag_chk='1',pk_emp_chk=:pkEmp,name_emp_chk=:nameEmp,date_chk=:dateChk,ts=:dateChk where pk_pdpu = :pkPdpu";
	    Map<String,Object> paramMap = new HashMap<String,Object>();
	    paramMap.put("pkEmp", ((User)user).getPkEmp());
	    paramMap.put("nameEmp", ((User)user).getNameEmp());
	    paramMap.put("dateChk", new Date());
	    paramMap.put("pkPdpu",pk_pdpu);
	    DataBaseHelper.update(update_pu_sql,paramMap);
	    return vo;
	}
	/**
	 * 取消订单
	 * @param param{pkPdpu}
	 * @param user
	 */
	public void cancelPuOrder(String param,IUser user){
		String pkPdPu = JsonUtil.readValue(param, String.class);
		if("".equals(pkPdPu))return;
		String update_Sql = "update pd_purchase  set eu_status='9',flag_canc='1',"+
       " date_canc=:dateCan,pk_emp_canc=:pkEmp,name_emp_canc=:nameEmp,ts=:dateCan  "+
       " where pk_pdpu=:pkPdpu and eu_status='1' and flag_canc='0'";
		Map<String,Object> paramMap = new HashMap<String,Object>();
	    paramMap.put("pkEmp", ((User)user).getPkEmp());
	    paramMap.put("nameEmp", ((User)user).getNameEmp());
	    paramMap.put("dateCan", new Date());
	    paramMap.put("pkPdpu",pkPdPu);
	    DataBaseHelper.update(update_Sql,paramMap);
	}
	/**
	 * 删除订单{pkPdpu}
	 * @param param
	 * @param user
	 */
	public void deletePuOrder(String param,IUser user){
		String pkPdPu = JsonUtil.readValue(param, String.class);
		if("".equals(pkPdPu))return;
		String delete_Sql = "delete from pd_purchase where pk_pdpu = ? and eu_status='0'";
		DataBaseHelper.execute(delete_Sql, new Object[]{pkPdPu});
		String delete_dt_sql = "delete from pd_purchase_dt  where pd_purchase_dt.pk_pdpu=? and  "+
        " exists (select 1 from pd_purchase  where pd_purchase_dt.pk_pdpu=pd_purchase.pk_pdpu and pd_purchase.eu_status='0')";
		DataBaseHelper.execute(delete_dt_sql, new Object[]{pkPdPu});
	}
	/**
	 * 查询采购订单
	 * @param param{pkOrg,dtPutype,codePu,pkSupplyer,datePuBegin,datePuEnd,pkEmpMak,euStatus}
	 * @param user
	 * @return
	 */
	public List<PuOrderVo> queryPuOrder(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNotNull(map.get("datePuBegin"))){
			map.put("datePuBegin", CommonUtils.getString(map.get("datePuBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("datePuEnd"))){
			map.put("datePuEnd", CommonUtils.getString(map.get("datePuEnd")).substring(0, 8)+"235959");
		}
		return puOrderMapper.queryPuOrderList(map);
	}
	
	/**
	 * 查询采购订单明细
	 * @param param{pkPdpu}
	 * @param user
	 * @return
	 */
	public List<PuOrderDtVo> queryPuOrderDt(String param,IUser user){
		String pk_pdpu = JsonUtil.readValue(param, String.class);
		return puOrderMapper.queryPuOrderDtList(pk_pdpu);
	}
	
	//保存退库单
	//修改退库单
	//审核退库单
	
	
}
