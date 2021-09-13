package com.zebone.nhis.ma.pub.syx.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.syx.dao.LisApplyMapper;
import com.zebone.nhis.ma.pub.syx.vo.TExamineItemSetListForIP;
import com.zebone.nhis.ma.pub.syx.vo.TExamineRequestForIP;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * lis系统业务处理服务(有事物)
 * @author yangxue
 *
 */
@Service
public class LisSystemService {
	
	@Autowired
	private LisApplyMapper lisApplyMapper;
	
	/**
	 * 查询待同步申请单列表
	 * @param map
	 * @return
	 */
	public List<TExamineRequestForIP> queryLisAppList(Map<String,Object> map){
		List<TExamineRequestForIP> rslist= null;
		rslist = lisApplyMapper.queryLisAppList(map);
		return rslist;
	}
	
	/**
	 * 查询门诊待同步申请单列表
	 * @param map
	 * @return
	 */
	public List<TExamineRequestForIP> queryOpLisAppList(Map<String,Object> map){
		List<TExamineRequestForIP> rslist= null;
		rslist = lisApplyMapper.queryOpLisAppList(map);
		return rslist;
	}

	/**
	 * 查询待同步申请单明细
	 * @param map
	 * @return
	 */
	public List<TExamineItemSetListForIP> queryLisFeeList(Map<String,Object> map){
		List<TExamineItemSetListForIP> rslist= null;
		rslist = lisApplyMapper.queryLisFeeList(map);
		return rslist;
	}
	
	/**
	 * 同步检验申请单
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sysLisApplyAndDt(List<TExamineRequestForIP> appList,List<TExamineItemSetListForIP> dtList){
		//1、校验申请单是否重复 - 重复则不发
		List<TExamineRequestForIP> oldAppList = lisApplyMapper.queryLisAppListFromLis(appList);
		if(null != oldAppList && oldAppList.size() > 0){
			for(TExamineRequestForIP app : oldAppList){
				for (int i = appList.size() - 1; i >= 0; i--) {
					if (appList.get(i).getExamineRequestID().equals(app.getExamineRequestID())){
						if(!appList.get(i).getPatientID().equals(app.getPatientID()) 
								&& appList.get(i).getIptimes() != app.getIptimes() ){
							throw new BusException("申请单号["+app.getExamineRequestID()+"]重复!");
						}
						appList.remove(i);
					}
				}
			}
		}
		
		//2、校验申请明细是否重复 - 重复则不发
		List<TExamineItemSetListForIP> oldDtList = lisApplyMapper.queryLisFeeListFromLis(dtList);
		if(null != oldDtList && oldDtList.size() > 0){
			for (TExamineItemSetListForIP dt : oldDtList) {
				for (int i = dtList.size() - 1; i >= 0; i--) {
					if (dtList.get(i).getExamineRequestID().equals(dt.getExamineRequestID()) 
							&& dtList.get(i).getItemSetID().equals(dt.getItemSetID()))
						dtList.remove(i);
				}
			}
		}
		
		if(null != dtList && dtList.size() > 0) {
			//10.31 lxw 校验 com.zebone.nhis.ma.pub.syx.dao.LisApplyMapper-queryLisFeeList中ItemSetID不能为空
			for (TExamineItemSetListForIP itemSet : dtList) {				
				if(itemSet.getItemSetID()==null || StringUtils.isEmpty(itemSet.getItemSetID().toString())){
					throw new BusException("医嘱【"+itemSet.getItemSetDesc()+"】old_id为空！ 请联系管理员！\n 医嘱号【"+itemSet.getOrdsn()+"】");
				}
			}
		}
		
		if(null != appList && appList.size() > 0) {
			for (TExamineRequestForIP app : appList) {
				app.setInsertDateTime(new Date());
				app.setReceiveDateTime(new Date());
				DataBaseHelper.insertBean(app);
			}
//			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(TExamineRequestForIP.class),appList);
		}

		if(null != dtList && dtList.size() > 0) {
			for (TExamineItemSetListForIP itemSet : dtList) {
				if(null == itemSet.getItemSetPrice())
					itemSet.setItemSetPrice(Double.valueOf(0));
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(TExamineItemSetListForIP.class),dtList);
		}
	}
	
	/**
	 * 更新NHIS检验申请单状态
	 * @return
	 */
	public void updateLabAppStatus(List<String> appList){
		if(null == appList || appList.size() < 1)  return;
		lisApplyMapper.updateLabApplyToChk(appList);
	}
	
	/**
	 * 查询 当前操作人旧ID
	 * @return
	 */
	public String queryOldIdByPkEmp(Map<String,Object> map){
		if(null == map)  return null;
		String oldId = lisApplyMapper.queryOldIdByPkEmp(map);
		return oldId;
	}
	
	/**
	 * 删除Lis库中的申请单以及明细信息
	 * @param appList
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void delLisAppFromLis(List<TExamineRequestForIP> appList,Long empOldId){
		if(null == appList || appList.size() < 1)  return;
		List<TExamineRequestForIP> oldAppList = lisApplyMapper.queryLisAppListFromLis(appList);
		if(null == oldAppList || oldAppList.size() < 1) return;
//		List<Long> delCodeList = new ArrayList<Long>();//待删除的申请单
		String upCode = "";//待更新的检验单
		for (TExamineRequestForIP app : oldAppList) {
			if("1".equals(app.getFlag()))
				throw new BusException("患者【"+app.getPatientName() 
						+ "】的检验申请单【"+app.getExamineRequestID()+"】检验系统已打印标签!");
			if("2".equals(app.getFlag()))
				throw new BusException("患者【"+app.getPatientName() 
						+ "】的检验申请单【"+app.getExamineRequestID()+"】检验系统已出结果!");
			
			upCode += "'"+app.getExamineRequestID()+"',";//现逻辑，走作废：作废申请单，否则删除申请单
//			delCodeList.add(app.getExamineRequestID());//原逻辑，走删除
		}
		
		//存在需删除的执行删除申请单及明细
//		if(delCodeList.size() >0) {
//			lisApplyMapper.delExamineRequestForIP(delCodeList);
//			lisApplyMapper.delExamineItemSetListForIP(delCodeList);
//		}
		
		//存在需作废的采取作废申请单 
		if(!CommonUtils.isEmptyString(upCode)) {
			upCode = upCode.substring(0,upCode.length() - 1);
			Map<String,Object> upMap = new HashMap<String,Object>();
			upMap.put("dateCanc", new Date());
			upMap.put("empId", empOldId);
			DataBaseHelper.update("UPDATE tExamineRequestForIP SET CancleFlag = '1' "
					+ " ,CancleTime =:dateCanc ,CancleEmployeeID =:empId "
//					+ " ,LisCancelFlag = '1' ,LisCancelBy =:empId ,LisCancelTime =:dateCanc ,LisCancelMsg = '病区取消' "
					+ " WHERE Flag = '0' AND ExamineRequestID in("+upCode+")", upMap);
//			lisApplyMapper.updateExamineRequestForIP(upCodeList);//因为dataBaseHelper自动设置ts导致无法采用该方式
		}
	}
	
}
