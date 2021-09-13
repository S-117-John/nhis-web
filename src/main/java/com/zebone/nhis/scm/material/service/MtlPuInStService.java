package com.zebone.nhis.scm.material.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.*;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.scm.material.dao.MtlPdStMapper;
import com.zebone.nhis.scm.pub.service.MtlPdStPubService;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 采购入库
 * @author wj
 *
 */
@Service
public class MtlPuInStService {
	
	@Resource
    private MtlPdStMapper pdStMapper;
	
	@Resource
	private MtlPdStPubService mtlPdStPubService;
	
	/**
	 *查询采购订单
	 * @param param{}
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryOrderList(String param,IUser user){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkOrg", ((User)user).getPkOrg());
		map.put("pkDept", ((User)user).getPkDept());
		map.put("pkStore", ((User)user).getPkStore());
		List<Map<String, Object>> list = pdStMapper.queryPuOrdList(map);
		return list;
	}
	
	/**
	 * 查询采购订单明细
	 * @param param{pkPdpu}
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryOrderDtList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		map.put("pkStore", ((User)user).getPkStore());
		map.put("curDate", DateUtils.getDefaultDateFormat().format(new Date()));
		map.put("pkOrg",((User)user).getPkOrg());
		List<Map<String, Object>> list =  pdStMapper.queryPuOrdDtList(map);
		return list;
	}
	
	/**
	 * 查询采购入库单(只查询direct=1的)
	 * @param param{pkStoreSt,codeSt,pkSupplyer,dateBegin,dateEnd,pkEmpOp,euStatus}
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPdStoreList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		map.put("euDirect", "1");
		List<Map<String, Object>> list = pdStMapper.queryPdStByCon(map);
		return list;
	}
	
	/**
	 * 查询采购入库单明细
	 * @param param{pkStore,pkPdst}
	 * @param user
	 * @return
	 */
	public List<PdStDtVo> queryPdStDetailList(String param,IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map == null || CommonUtils.isNull(map.get("pkPdst")))
			throw new BusException("入库单主键为空，无法获取明细！");
		List<PdStDtVo> list = pdStMapper.queryPdStDtByPk(map);
		return  list;
	}
	
	/**
	 * 保存采购入库信息
	 * @param param
	 * @param user
	 */
	public PdStVo  savePdSt(String param,IUser user){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		String euDirect = JsonUtil.getFieldValue(param, "euDirect").toString();
		return mtlPdStPubService.savePdSt(stvo,user,IDictCodeConst.DT_STTYPE_BUY,euDirect);
	}
	
	/**
	 * 提交-审核采购入库单
	 */
	public void submitPdst(String param,IUser user){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		User u =(User)user;
		if(stvo == null) 
			throw new BusException("未获取到审核采购入库单的相关入参！");
		List<PdStDtVo> dtList = stvo.getDtlist();
		if(null == dtList || dtList.size() < 1)
			throw new BusException("未获取到待审核采购入库单的明细记录！");
		
		String pk_pdst = stvo.getPkPdst();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPdst", pk_pdst);
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("dateChk", new Date());
		if(!CommonUtils.isEmptyString(stvo.getPkPdpu())){//采购订单生成入库单先保存
			pk_pdst = savePdSt(param,user).getPkPdst();
			//更新采购订单明细
				for(PdStDtVo stdt:stvo.getDtlist()){
					String update_sql_dt = "update pd_purchase_dt set quan_in_min = nvl(quan_in_min,0)+:quanMin,"+
							" lastdate_in =:dateChk, pk_emp_acc = :pkEmp, name_emp_acc = :nameEmp ,ts=:dateChk ";
					if(stdt.getQuanPuMin()>=(stdt.getQuanMin()+stdt.getQuanInMin())){
						update_sql_dt = update_sql_dt+",flag_acc='1' ";
					}
					update_sql_dt = update_sql_dt +" where pk_pdpudt = :pkPdPudt";
					paramMap.put("quanMin",stdt.getQuanMin());
					paramMap.put("pkPdPudt", stdt.getPkPdpudt());
					DataBaseHelper.update(update_sql_dt, paramMap);
				}
				//更新采购订单状态
				String update_pu="update pd_purchase set eu_status='2' "
						+",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
						+ " where  (select count(1) "+
						" from pd_purchase_dt where flag_acc='0' and  pk_pdpu = ? )<=0  and  pk_pdpu = ? ";
				DataBaseHelper.update(update_pu, new Object[]{stvo.getPkPdpu(),stvo.getPkPdpu()});
		}
		
		mtlPdStPubService.updatePdSt(paramMap);
		mtlPdStPubService.updatePdSingleStatus(EnumerateParameter.ONE,pk_pdst,EnumerateParameter.ONE,true);
		//如果选择了出向仓库或者出向部门，新增出库记录，忽略库存处理
		boolean dept_flag = !CommonUtils.isEmptyString(stvo.getPkDeptLk());
		boolean store_flag = !CommonUtils.isEmptyString(stvo.getPkStoreLk());
		if(dept_flag||store_flag){
			PdSt st_lk =  new PdSt();
		    ApplicationUtils.copyProperties(st_lk, stvo);
			st_lk.setPkPdst(null);
			st_lk.setCodeSt(ScmPubUtils.getOutStoreCode());
			st_lk.setEuDirect("-1");
			st_lk.setEuStatus("1");
			st_lk.setDateChk(new Date());
			st_lk.setFlagChk("1");
			st_lk.setNameEmpChk(u.getNameEmp());
			st_lk.setPkEmpChk(u.getPkEmp());
			if(dept_flag){
				st_lk.setDtSttype(IDictCodeConst.DT_STTYPE_DETPOUT);
			}
			if(store_flag){
				st_lk.setDtSttype(IDictCodeConst.DT_STTYPE_ALLOOUT);//以仓库为基准
			}
			st_lk.setPkOrgLk(u.getPkOrg());
			ApplicationUtils.setBeanComProperty(st_lk, true);
			DataBaseHelper.insertBean(st_lk);
			String pk_st_lk = st_lk.getPkPdst();
			List<PdStDetail> list_lk = new ArrayList<PdStDetail>();
			List<String> update_list = new ArrayList<String>();
			for(PdStDtVo stdtvo:stvo.getDtlist()){
				 update_list.add("update pd_st_detail set quan_outstore = quan_min ,flag_finish='1' where pk_pdstdt = '"+stdtvo.getPkPdstdt()+"'");
				 stdtvo.setPkPdst(pk_st_lk);
				 stdtvo.setPkPdstdt(NHISUUID.getKeyId());
				 ApplicationUtils.setBeanComProperty(stdtvo, true);
				 list_lk.add(stdtvo);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), list_lk);
			if(update_list!=null&&update_list.size()>0){
				DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
			}
		}else{
			//更新库存
			mtlPdStPubService.updateInStore(stvo.getDtlist(),u.getPkStore(),u.getPkDept());
		}
		
		
	}
	
	/**
	 * 删除采购入库单
	 * @param param{pkPdst}
	 * @param user
	 */
	public void deletePdst(String param,IUser user){
		String pkPdst = JsonUtil.readValue(param, String.class);
		if (CommonUtils.isEmptyString(pkPdst))
			throw new BusException("未获取到待删除的入库单主键！");
		//先删除单品数据
		DataBaseHelper.execute("delete from pd_single where eu_status = '0' and pk_pdstdt_in=?", new Object[] { pkPdst });
		mtlPdStPubService.deletePdst(param, user);
 	}
	
	/**
	 * 退回采购入库单
	 * @param param
	 * @param user
	 */
	public void rtnPdst(String param,IUser user){
		mtlPdStPubService.rtnPdst(param, user, IDictCodeConst.DT_STTYPE_RTN);
	}

	/**
	 * 根据条码扫描
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PdStDtVo> qryPkStoreByPkDept(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0) return null;
		paramMap.put("pkStore", ((User)user).getPkStore());
		paramMap.put("dateNow", DateUtils.getDate("yyyyMMddHHmmss"));//传入当前时间，用于过滤物品是否注册效期已过
		return pdStMapper.qryPkStoreByPkdept(paramMap);
	}

}
