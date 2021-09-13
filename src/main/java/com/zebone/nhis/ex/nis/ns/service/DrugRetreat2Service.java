package com.zebone.nhis.ex.nis.ns.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.nis.ns.dao.DrugRetreatMapper2;
import com.zebone.nhis.ex.nis.ns.support.ExlistPrintSortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.vo.ExPdApplyVo;
import com.zebone.nhis.ex.nis.ns.vo.MedicineVo;
import com.zebone.nhis.ex.nis.ns.vo.OrderOccCgVo;
import com.zebone.nhis.ex.nis.ns.vo.OrderOccVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class DrugRetreat2Service {
	
	@Autowired
	private DrugRetreatMapper2 drugRetreatMapper;
	
	/**
	 * 查询执行列表 - 中山二院【单独使用】
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryOccListBySyx(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(null == map)
			throw new BusException("未获取到待查询数据！");
		if(null == map.get("pkPvs") || CommonUtils.isEmptyString(map.get("pkPvs").toString()))
			throw new BusException("未获取到待查询患者主键！");
		List<String> pkpvs = (List<String>)map.get("pkPvs");
		if(null == pkpvs || pkpvs.size() < 1)
			throw new BusException("未获取到待查询患者主键！");
		
		List<Map<String,Object>> list=drugRetreatMapper.queryOccListBySyx(map);
		if(list == null || list.size()<=0) return null;
		
		new ExlistPrintSortByOrdUtil().ordGroup(list);//设置同组标志
		
		return list;
	}
	/**
	 * 查询执行单对应的发药及记费列表 - 中山二院【单独使用】
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryCgListByOcc(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(null == map)
			throw new BusException("未获取到需要查询的参数！");
		if(null == map.get("pkExoccs") || CommonUtils.isEmptyString(map.get("pkExoccs").toString())||((List<String>)map.get("pkExoccs")).size()<=0)
			throw new BusException("未获取到待查询执行单主键！");
		if(null == map.get("pkPvs") || CommonUtils.isEmptyString(map.get("pkPvs").toString())||((List<String>)map.get("pkPvs")).size()<=0)
			throw new BusException("未获取到待查询患者主键！");
		List<Map<String,Object>> list = null;
		if(Application.isSqlServer()){
			if(map.get("isBoAi") != null && !"".equals(map.get("isBoAi").toString()) && "1".equals(map.get("isBoAi").toString())){
				list=drugRetreatMapper.queryCgListByOccForSqlServer(map);
			}else{
				list=drugRetreatMapper.queryCgListByOccSqlServer(map);
			}
		}else{
			if(map.get("isBoAi") != null && !"".equals(map.get("isBoAi").toString()) && "1".equals(map.get("isBoAi").toString())){
				list=drugRetreatMapper.queryCgListByOccForOrcl(map);
			}else{
				list=drugRetreatMapper.queryCgListByOcc(map);
			}
		}
		
		if(list == null || list.size()<=0) return null;
		
		//new ExlistPrintSortByOrdUtil().ordGroup(list);//设置同组标志
		
		return list;
	}
	
	/**
	 * 写退药申请表 - 中山二院【单独使用】
	 * @param param
	 * @param user
	 * @return
	 */
	public String addExPdApply(String param,IUser user){
		
		List<ExPdApplyVo>  pdAppList =JsonUtil.readValue(param, new TypeReference<List<ExPdApplyVo>>(){});
		if(null == pdAppList || pdAppList.size() < 1)
			throw new BusException("请选择需要生成退请领的执行单！");
		for (ExPdApplyVo exPdApplyVo : pdAppList) {
			
			createPdRtnInfo(exPdApplyVo,(User)user);
		}
		//调用静配接口完成静配中心退药操作，主要参数，ordsn和datePlan
		ExtSystemProcessUtils.processExtMethod("PIVAS", "updatePivasOut", param);
		return null;
	}
	/**
	 * 生成请退记录 -新 2020.03.18
	 * @param expdapplyvo
	 * @return
	 */
	private void createPdRtnInfo(ExPdApplyVo expdapplyvo,User user) {
		if(expdapplyvo.getCgList()==null || expdapplyvo.getCgList().size() < 1) 
			throw new BusException("未选择需要退药的记录！");
		//生成退请领前二次校验是否已被他人退药
		List<String> pkCgList = new ArrayList<String>();
		List<String> pkPvList = new ArrayList<String>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		for(OrderOccCgVo cg : expdapplyvo.getCgList()){
			pkCgList.add(cg.getPkCgip());
			pkPvList.add(cg.getPkPv());
		}
		paramMap.put("pkCgips", pkCgList);
		paramMap.put("pkPvs", pkPvList);
		List<OrderOccCgVo> cglist = drugRetreatMapper.queryCgListByPk(paramMap);
		if(cglist==null||cglist.size()<1)
			throw new BusException("本次提交的退药记录可能已被他人提交，请刷新后重试！");
		for(OrderOccCgVo cg: cglist){
			for(OrderOccCgVo cgrtn : expdapplyvo.getCgList()){
				if(cg.getPkCgip().equals(cgrtn.getPkCgip())&&cgrtn.getQuanRtn()>cg.getQuan()){
					throw new BusException("本次提交的退药记录可能已被他人提交，请刷新后重试！");
				}
			}
		}
		ExPdApply exPdApply=new ExPdApply();
		expdapplyvo.setCodeApply(ExSysParamUtil.getAppCode());
		expdapplyvo.setPkPdap(NHISUUID.getKeyId());
		ApplicationUtils.copyProperties(exPdApply,expdapplyvo);
		DataBaseHelper.insertBean(exPdApply);
		//生成退请领明细
		createApplyDetails(expdapplyvo.getCgList(),expdapplyvo.getExOrdOccList(),exPdApply.getPkPdap(),(User)user);
		
	}
	/**
	 * 根据计费记录创建请领明细-2020.03.18
	 * @return
	 */
	private void createApplyDetails(List<OrderOccCgVo> cglist,List<OrderOccVo> exOrdOccList,String pkPdap,User user){
		List<String> updatelist = new ArrayList<String>();
		for(OrderOccCgVo cg:cglist){
			if(cg.getQuanRtn()<=0)
				continue;
			ExPdApplyDetail dt = new ExPdApplyDetail();
			dt.setBatchNo(cg.getBatchNo());
			dt.setDateExpire(cg.getDateExpire());
			dt.setEuDetype("0");
			dt.setEuDirect("-1");
			dt.setFlagBase(cg.getFlagBase());
			dt.setFlagCanc("0");
			dt.setFlagDe("0");
			dt.setFlagEmer(cg.getFlagEmer());
			dt.setFlagFinish("0");
			dt.setFlagMedout(cg.getFlagMedout());
			dt.setFlagPivas(cg.getFlagPivas());
			dt.setFlagSelf(cg.getFlagSelf());
			dt.setFlagStop("0");
			dt.setOrds(0);
			dt.setPackSize(cg.getPackSize());
			dt.setPkCgip(cg.getPkCgip());
			dt.setPkCnord(cg.getPkCnord());
			//dt.setPkDeptAp(user.getPkDept());
			dt.setPkOrg(user.getPkOrg());
			//dt.setPkOrgAp(user.getPkOrg());
			dt.setPkPd(cg.getPkPd());
			dt.setPkPdap(pkPdap);
			dt.setPkPres(cg.getPkPres());
			dt.setPkPv(cg.getPkPv());
			dt.setPkUnit(cg.getPkUnit());
			dt.setQuanPack(Double.valueOf(cg.getQuanRtn()));
			dt.setPrice(cg.getPrice());
			dt.setPriceCost(cg.getPriceCost());
			dt.setAmount(MathUtils.mul(dt.getPrice(),dt.getQuanPack()));
			dt.setQuanMin(MathUtils.mul(Double.valueOf(cg.getPackSize()), Double.valueOf(cg.getQuanRtn())));
			DataBaseHelper.insertBean(dt);
			
			//更新执行单退药主键(当同一个医嘱对应多次领药，多个发药记录时，同时退药，执行单被更新的退药主键可能存在与请领对不上的情况)
			for(OrderOccVo occ:exOrdOccList){
				if(occ.getPkCnord().equals(dt.getPkCnord())){
				   StringBuffer updateSql=new StringBuffer();
				   updateSql.append("update ex_order_occ set pk_pdback = '");
				   updateSql.append(dt.getPkPdapdt());
				   updateSql.append("',pk_exevent='");
				   updateSql.append(pkPdap);
				   updateSql.append("' where pk_exocc='");
				   updateSql.append(occ.getPkExocc());
				   updateSql.append("' and (pk_pdback is null or pk_pdback='') ");
				   updateSql.append(" and exists(select 1 from ex_pd_de de where de.pk_pdapdt=ex_order_occ.pk_pdapdt and de.pk_pdde='"+cg.getPkOrdexdt()+"')");
				   //String updateSql = "update ex_order_occ set pk_pdback = '"+dt.getPkPdapdt()+"',pk_exevent='"+pkPdap+"' where pk_exocc='"+occ.getPkExocc()+"' and (pk_pdback is null or pk_pdback='') ";
				   updatelist.add(updateSql.toString());
				}		
		    }
		 }
		//批量更新
		if(updatelist!=null&&updatelist.size()>0)
			 DataBaseHelper.batchUpdate(updatelist.toArray(new String[0]));
		
	} 
	/**
	 * 生成请退记录--弃用
	 * @param expdapplyvo
	 * @return
	 */
	@Deprecated
	private String createPdBack(ExPdApplyVo expdapplyvo) {
		if(expdapplyvo.getExOrdOccList() == null || expdapplyvo.getExOrdOccList().size() < 1) 
			throw new BusException("请选择需要生成退请领的执行单！");
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> pkExOccs = new ArrayList<String>();
		for (OrderOccVo occ : expdapplyvo.getExOrdOccList()) {
			if(!CommonUtils.isEmptyString(occ.getPkExocc()))
				pkExOccs.add(occ.getPkExocc());
		}
		if(null == pkExOccs || pkExOccs.size() < 1)
			throw new BusException("未获取到待退药的执行单主键！");
		map.put("pkExoccs", pkExOccs);
		//添加对应的费用明细
		List<MedicineVo> delist = drugRetreatMapper.queryMedicineListBySyx(map);

		//1) 计算退药数量(整数取整，小数向下取整)
		List<ExPdApplyDetail> listCnt = new ArrayList<ExPdApplyDetail>();
		if(Application.isSqlServer()){
			listCnt = drugRetreatMapper.querySumCntByOrd(map);
		}else
			listCnt =drugRetreatMapper.querySumCntByOrdInOrcl(map);
		
		if(null == listCnt || listCnt.size() < 1) 
			throw new BusException("未获取到待退药的药品数量集合！");
		double cnt = 0;
		String namePd = "";
		Map<String,Integer> cntMap = new HashMap<String,Integer>();
		for (int i = 0; i < listCnt.size(); i++ ) {
			cnt = listCnt.get(i).getQuanPack().intValue() * 1.0;
			if(cnt == 0)
			{
				cnt = 1.00;				
			}
			if(0 == cnt){
				namePd = DataBaseHelper.queryForScalar("select name from bd_pd where pk_pd=? and del_flag='0' "
						, String.class, new Object[]{listCnt.get(i).getPkPd()});
				throw new BusException(namePd + "：本次退药数量为"+cnt+"，无法生成退药申请！");
			}
			listCnt.get(i).setQuanPack(cnt);
			cntMap.put(listCnt.get(i).getPkCnord() + "," + listCnt.get(i).getPkPd() + "," + listCnt.get(i).getPkUnit() 
					+ "," + listCnt.get(i).getPackSize(), i);
		}
		
		//2) 根据执行单关联的领药单获取发药时的明细记录；
		List<ExPdApplyDetail> listBatch =drugRetreatMapper.queryPdBatchByOrd(map);
		if(null == listBatch || listBatch.size() < 1) 
			throw new BusException("选中待退药的相关执行记录未发药，无法请退！");
		Map<String,List<Integer>> cntBatch = new HashMap<String,List<Integer>>();
		String key = "";
		List<Integer> value = null;
		for (int i = 0; i < listBatch.size(); i++ ) {
			key = listBatch.get(i).getPkCnord() + "," + listBatch.get(i).getPkPd() + "," 
					+ listBatch.get(i).getPkUnit() + "," + listBatch.get(i).getPackSize();
			value = cntBatch.containsKey(key) ? cntBatch.get(key) :	new ArrayList<Integer>();
			value.add(i);
			cntBatch.put(key,value);
		}
		
		//3) 生成退请领及明细
		ExPdApply exPdApply=new ExPdApply();
		expdapplyvo.setCodeApply(ExSysParamUtil.getAppCode());
		expdapplyvo.setPkPdap(NHISUUID.getKeyId());
		ApplicationUtils.copyProperties(exPdApply,expdapplyvo);
		key = "";
		value = null;
		double quan = 0;
		for (Map.Entry<String,Integer> entry:cntMap.entrySet()) {
			key = entry.getKey();
			cnt = listCnt.get(entry.getValue()).getQuanPack().doubleValue();//物品的合计数量
			value = cntBatch.get(key);//物品的批次
			if(null == value ||	value.size() <1) continue;
			for (Integer index : value) {
				quan = listBatch.get(index).getQuanPack().doubleValue();
				if(cnt > quan){
					setDefValAndInsertPdAp(quan,delist,listBatch.get(index),exPdApply.getPkPdap());
					cnt = cnt - quan;
					continue;
				}
				else{
					setDefValAndInsertPdAp(cnt, delist,listBatch.get(index),exPdApply.getPkPdap());
					break;
				}
			}
		}
		int cnt_dt = DataBaseHelper.queryForScalar("select count(1) from ex_pd_apply_detail where pk_pdap = ? ", Integer.class, new Object[]{exPdApply.getPkPdap()});
		if(cnt_dt < 1)
			throw new BusException("选中待退药的相关执行记录已经退药，请刷新后重新退药！");
		DataBaseHelper.insertBean(exPdApply);
		return exPdApply.getPkPdap();
	}
	/**
	 * 创建请领明细--弃用
	 * @param quan
	 * @param delist
	 * @param pdDtBatch
	 * @param pkPdAp
	 */
	@Deprecated
	private void setDefValAndInsertPdAp (double quan, List<MedicineVo> delist,ExPdApplyDetail pdDtBatch,String pkPdAp){
		ExPdApplyDetail pdDt = new ExPdApplyDetail();
		ApplicationUtils.copyProperties(pdDt, pdDtBatch);
		//1、从发药记录里面获取药品基本信息以及执行单相关信息
		if(null == delist || delist.size() < 1) return;
		List<String> pklist = new ArrayList<String>();
		MedicineVo medVo = null;
		int cnt = 0;
		for (MedicineVo med : delist) {
			if(med.getPkPd().equals(pdDt.getPkPd()) 
					&& med.getBatchNo().equals(pdDt.getBatchNo()) 
					&& med.getPkCnord().equals(pdDt.getPkCnord())){
				cnt = 0;
				for (String str : pklist) {
					if(str.equals(med.getPkExocc())) cnt ++;
				}
				if(cnt < 1)
					pklist.add(med.getPkExocc());
				medVo = med;
			}
		}
		pdDt.setOrds(medVo.getOrds());
		pdDt.setPkPv(medVo.getPkPv());
		pdDt.setPkPres(medVo.getPkPres());
		pdDt.setFlagBase(medVo.getFlagBase());
		pdDt.setFlagMedout(medVo.getFlagMedout());
		pdDt.setFlagSelf(medVo.getFlagSelf());
		pdDt.setPkCgip(medVo.getPkCgip());
		pdDt.setPkPdap(pkPdAp);
		pdDt.setFlagPivas(medVo.getFlagPivas());
		pdDt.setPkOrg(UserContext.getUser().getPkOrg());
		//pdDt.setPkDeptAp(UserContext.getUser().getPkDept());
		pdDt.setPrice(medVo.getPrice());
		pdDt.setPriceCost(medVo.getPriceCost());
		pdDt.setEuDirect("-1");
		pdDt.setFlagDe("0");
		pdDt.setFlagFinish("0");
		pdDt.setFlagStop("0");
		pdDt.setFlagCanc("0");
		pdDt.setDelFlag("0");
		pdDt.setQuanPack(quan);
		pdDt.setQuanMin(MathUtils.mul(pdDt.getPackSize()*1.0,quan));
		pdDt.setAmount(MathUtils.mul(pdDt.getPrice(),quan));
		DataBaseHelper.insertBean(pdDt);
		
		//使用pk_exevent字段区分是否是由停医嘱产生的退请领记录
		if(null == pklist || pklist.size() < 1) return;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkExocc", pklist);
		int cnt2 =  DataBaseHelper.update("update ex_order_occ set pk_pdback = '"+pdDt.getPkPdapdt()+"',pk_exevent='"+pkPdAp+"' where pk_exocc in (:pkExocc) and (pk_pdback is null or pk_pdback='') ",paramMap);
		if(cnt2 != pklist.size()){
			throw new BusException("您选择的执行记录可能已被他人修改，请刷新后重试！");
		}   	 
	}

	/**
	 * 退药申请时，如果包含静配，已经入仓的数据，前台返回数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> checkPivasInfo(String param,IUser user){
		List<Map<String,Object>> result=(List<Map<String,Object>>)ExtSystemProcessUtils.processExtMethod("PIVAS", "checkPivasOut", param);
		return result;
	}
}
