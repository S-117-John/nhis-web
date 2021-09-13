package com.zebone.nhis.ex.nis.ns.service;

import java.util.ArrayList;
import java.util.Date;
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
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.nis.ns.dao.DrugRetreatMapper;
import com.zebone.nhis.ex.nis.ns.support.ExlistPrintSortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.vo.ExPdApplyDetailVo;
import com.zebone.nhis.ex.nis.ns.vo.ExPdApplyVo;
import com.zebone.nhis.ex.nis.ns.vo.MedicineVo;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class DrugRetreatService {
	@Autowired
	private DrugRetreatMapper drugRetreatMapper;
	
	/**
	 * 查询执行列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryOccList(String param,IUser user){
//		List<String> pkpvs=JsonUtil.readValue(param, List.class);
		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(null == map)
			throw new BusException("未获取到待查询数据！");
		if(null == map.get("pkPvs") || CommonUtils.isEmptyString(map.get("pkPvs").toString()))
			throw new BusException("未获取到待查询患者主键！");
		List<String> pkpvs = (List<String>)map.get("pkPvs");
		if(null == pkpvs || pkpvs.size() < 1)
			throw new BusException("未获取到待查询患者主键！");
		
		List<Map<String,Object>> list=drugRetreatMapper.queryOccList(map);
		if(list == null || list.size()<=0) return null;
		
		new ExlistPrintSortByOrdUtil().ordGroup(list);//设置同组标志
		
		//添加对应的费用明细
		List<MedicineVo> delist = drugRetreatMapper.queryMedicineList(map);
		if(delist!=null&&delist.size()>0){
			for(Map<String,Object> exvo:list){
				List<MedicineVo> pddelist = new ArrayList<MedicineVo>();
				for(MedicineVo devo:delist){
					if(exvo.get("pkExocc").equals(devo.getPkExocc())){
						pddelist.add(devo);
					}
				}
				exvo.put("medicineVoList", pddelist);
			}
		}
		return list;
	}

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
		
		//添加对应的费用明细
		List<MedicineVo> delist = drugRetreatMapper.queryMedicineListBySyx(map);
		if(delist!=null&&delist.size()>0){
			for(Map<String,Object> exvo:list){
				List<MedicineVo> pddelist = new ArrayList<MedicineVo>();
				for(MedicineVo devo:delist){
					if(exvo.get("pkExocc").equals(devo.getPkExocc())){
						pddelist.add(devo);
					}
				}
				exvo.put("medicineVoList", pddelist);
			}
		}
		
		return list;
	}
	
	/**
	 * 写退药申请表
	 * @param param
	 * @param user
	 * @return
	 */
	public String addExPdApply(String param,IUser user){
		ExPdApplyVo expdapplyvo =JsonUtil.readValue(param, ExPdApplyVo.class);
		if(expdapplyvo.getExPdApplyDetailVoList() == null || expdapplyvo.getExPdApplyDetailVoList().size() < 1) 
			throw new BusException("请选择需要生成退请领的执行单！");
		ExPdApply exPdApply=new ExPdApply();
		expdapplyvo.setCodeApply(ExSysParamUtil.getAppCode());
		expdapplyvo.setPkPdap(NHISUUID.getKeyId());
		ApplicationUtils.copyProperties(exPdApply,expdapplyvo);
		Map<String,ExPdApplyDetailVo> dtMap = new HashMap<String,ExPdApplyDetailVo>();
		Map<String,List<String>> exMap = new HashMap<String,List<String>>();
		//合并相同医嘱的执行单
		for (ExPdApplyDetailVo  exPdApplyDetailVo : expdapplyvo.getExPdApplyDetailVoList()) {
			List<String> pkExList = new ArrayList<String>();
			boolean flag = true;//是否需要增加
			for (String pkCnord : dtMap.keySet()) {
				//医嘱主键与收费主键相同的执行单合并
				if((exPdApplyDetailVo.getPkCnord()+"||"+exPdApplyDetailVo.getPkCgip()).equals(pkCnord)){
					ExPdApplyDetailVo dtVo = dtMap.get(pkCnord);
					dtVo.setQuanMin(dtVo.getQuanMin()+exPdApplyDetailVo.getQuanMin());
					dtVo.setQuanPack(dtVo.getQuanPack()+exPdApplyDetailVo.getQuanPack());
					dtMap.put(pkCnord, dtVo);
					List<String> exlist = exMap.get(pkCnord);
					exlist.add(exPdApplyDetailVo.getPkExOcc());
					exMap.put(pkCnord, exlist);
					flag = false;
					break;
				}
			}
			if(flag){
				exPdApplyDetailVo.setPkPdap(expdapplyvo.getPkPdap());
				dtMap.put(exPdApplyDetailVo.getPkCnord()+"||"+exPdApplyDetailVo.getPkCgip(), exPdApplyDetailVo);
				pkExList.add(exPdApplyDetailVo.getPkExOcc());
				exMap.put(exPdApplyDetailVo.getPkCnord()+"||"+exPdApplyDetailVo.getPkCgip(), pkExList);
			}
		}
		ExPdApplyDetail exPdApplyDetail=new ExPdApplyDetail();
		//插入请领明细
		 for (Map.Entry<String,ExPdApplyDetailVo> entry:dtMap.entrySet()) {
			 ApplicationUtils.copyProperties(exPdApplyDetail, entry.getValue());
			 exPdApplyDetail.setAmount(MathUtils.mul(exPdApplyDetail.getPrice(),exPdApplyDetail.getQuanPack()));
			 DataBaseHelper.insertBean(exPdApplyDetail);
			 Map<String,Object> paramMap = new HashMap<String,Object>();
			 List<String> pklist = exMap.get(entry.getKey());
			 paramMap.put("pkExocc", pklist);
			 //使用pk_exevent字段区分是否是由停医嘱产生的退请领记录
			 if(pklist!=null&&pklist.size()>0){
				 int cnt =  DataBaseHelper.update("update ex_order_occ set pk_pdback = '"+exPdApplyDetail.getPkPdapdt()+"',pk_exevent='"+exPdApply.getPkPdap()+"' where pk_exocc in (:pkExocc) and (pk_pdback is null or pk_pdback='') ",paramMap);
				 if(cnt != pklist.size()){
					 throw new BusException("您选择的执行记录可能已被他人修改，请刷新后重试！");
				 }   	 
			 }
		  }
		DataBaseHelper.insertBean(exPdApply);
		return exPdApply.getPkPdap();
	}
	
	/**
	 * 执行确认（取消因停止医嘱取消的执行单，变更为执行状态）
	 */
	@SuppressWarnings("unchecked")
	public void reConfirmEx(String param,IUser user){
		List<String> pkExlist=JsonUtil.readValue(param, List.class);
		if(pkExlist==null||pkExlist.size()<=0)
			return;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		paramMap.put("nameEmp", ((User)user).getNameEmp());
		paramMap.put("pkExList",pkExlist);
		paramMap.put("dateCur", DateUtils.getDefaultDateFormat().format(new Date()));
		drugRetreatMapper.updateExList(paramMap);
	}
	
	/**
	 * 退药单查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryRtnApply(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null)
			map = new HashMap<String,Object>();
		if(CommonUtils.isNull(map.get("pkDeptAp")))
			map.put("pkDeptAp", ((User)user).getPkDept());
		if(map.get("dateBegin")!=null){
			map.put("dateBegin",map.get("dateBegin").toString()+"000000");
		}
		if(map.get("dateEnd")!=null){
			map.put("dateEnd",map.get("dateEnd").toString()+"235959");
		}	
		return drugRetreatMapper.queryRtnApply(map);
	}

	/**
	 * 更新打印状态
	 * @param param
	 * @param user
	 */
	public void updatePrtFlag(String param,IUser user){
		List<String> pklist = JsonUtil.readValue(param, new TypeReference<List<String>>(){});
		if(pklist==null||pklist.size()<=0)
			throw new BusException("未获取到需要更新打印状态的申请单！");
		String [] sqls = new String[pklist.size()];
		for(int i =0;i<pklist.size();i++){
			sqls[i] = "update ex_pd_apply set eu_print = 1 where pk_pdap ='"+pklist.get(i)+"'";
		}
		DataBaseHelper.batchUpdate(sqls);
	}
}
