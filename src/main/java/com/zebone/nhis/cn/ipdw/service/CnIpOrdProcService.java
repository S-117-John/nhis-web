package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.ipdw.dao.CnOrderSyncMapper;
import com.zebone.nhis.cn.ipdw.support.CnIpOrdSyncHandler;
import com.zebone.nhis.cn.ipdw.vo.CnBlIpDtVo;
import com.zebone.nhis.cn.ipdw.vo.CnOrderSyncVo;
import com.zebone.nhis.cn.ipdw.vo.PvDiagVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnOrderSync;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 住院医嘱特殊业务处理服务
 * @author chengjia
 *
 */
@Service
public class CnIpOrdProcService {
	@Autowired
	private CnIpOrdSyncHandler ordSyncHandler;	
	
	@Autowired
	private CnOrderSyncMapper cnOrderSyncMapper;
	
	@Autowired
	private CnIpOrdCgService cnIpOrdCgService;
	
	@Autowired
	private BdSnService bdSnService ;
	
	//同步医嘱信息
	public void syncSendOrds(List<CnOrder> cnOrds, User u,String optType) {
		syncSendOrds(cnOrds,u,optType,"");
	}
	
	/**
	 * 同步医嘱信息imp
	 * @param cnOrds
	 * @param u
	 * @param optType N录入 D删除 S停止O取消停止C作废
	 * @param reqType
	 */
	public void syncSendOrds(List<CnOrder> cnOrds, User u,String optType,String reqType) {
		//数据同步发送开关（目前只有急诊项目在用）
		String syncSendSwitch=ApplicationUtils.getPropertyValue("sync.send.switch", "0");
		int i,j,size;
		BlPubParamVo vo;
		if(syncSendSwitch!=null&&syncSendSwitch.equals("1")){
			if(cnOrds==null||cnOrds.size()==0) return;
			List<BlPubParamVo> cgParamList=new ArrayList<BlPubParamVo>();
	
			List<Integer> ordsns = new ArrayList<>();
			for (CnOrder order : cnOrds) {
				ordsns.add(order.getOrdsn());
			}
			String pkPv=cnOrds.get(0).getPkPv();
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPv",pkPv );
			paramMap.put("ordsns", ordsns);
			List<CnOrderSyncVo> listSign=new ArrayList<CnOrderSyncVo>();
			
			List<CnOrderSync> listSysn=new ArrayList<CnOrderSync>();
			int ordsnParent=0;
			Long orderGroupNo=new Long("0");
			if(optType!=null&&optType.equals("N")){
				listSign = cnOrderSyncMapper.queryCnOrderSync(paramMap);
				if(listSign==null||listSign.size()==0) return;
				size=listSign.size();
				
				//签署
				CnOrderSync ordParent=null;
				for(i=0;i<size;i++){
					CnOrderSync order = listSign.get(i);
					//if(order.getFlagDrug()!=null&&order.getFlagDrug().equals("1")) continue;
					
					vo =new BlPubParamVo();
					ApplicationUtils.copyProperties(vo, order);
					
					vo.setPkDeptEx(order.getPkDeptExec());
					if(vo.getPrice()==null) vo.setPrice(new Double("0"));
					if(vo.getPriceCost()==null) vo.setPriceCost(new Double("0"));
					if(order.getFlagNote()==null||order.getFlagNote().equals("0"))	cgParamList.add(vo);
					if(ordsnParent!=order.getOrdsnParent().intValue()){
						//int serialNo = bdSnService.getSerialNo("cn_order","order_group_no", 1, u);
						int serialNo=ordSyncHandler.createSyncHisSnMax("YZ_YZZH");
						orderGroupNo=new Long(serialNo);
						ordsnParent=order.getOrdsnParent().intValue();
					}
					order.setOrderGroupNo(orderGroupNo);
					if(order.getOrdsn().intValue()==order.getOrdsnParent().intValue()){
						order.setFlagMaster("1");
					}else{
						order.setFlagMaster("0");
					}
				}
				
				//计费服务
				if(cgParamList.size()>0) cnIpOrdCgService.blIpCgBatch(cgParamList);
				//end
				//List<CnOrderSyncVo> listSignNew=cnOrderSyncMapper.queryCnOrderSync(paramMap);
				//if(listSignNew==null||listSignNew.size()==0) return;
				List<CnBlIpDtVo> itemList=cnOrderSyncMapper.queryCnOrderIpBlDtSync(paramMap);
				boolean bHaveBl=false;
				CnOrderSync orderNew;
				for(i=0;i<listSign.size();i++){
					CnOrderSync order = listSign.get(i);
					bHaveBl=false;
					String flagMaster="0";
					for(j=0;itemList!=null&&j<itemList.size();j++){
						CnBlIpDtVo item=itemList.get(j);
						if(item==null) continue;
						if(item.getPkCnord()==null) continue;
						if(item.getPkCnord().equals(order.getPkCnord())){
							orderNew=new CnOrderSync();
							ApplicationUtils.copyProperties(orderNew, order);
							orderNew.setCodeOrd(item.getCode());
							orderNew.setNameOrd(item.getNameCg()+"/"+item.getSpec());
							orderNew.setSpec(item.getSpec());
							orderNew.setPriceCg(item.getPrice());
							orderNew.setQuanCg(item.getQuan());
							orderNew.setCodeUnitCg(item.getUnitCode());
							orderNew.setEuHptype(item.getEuHptype());
							//orderNew.setOrderNo(new Long(item.getChargeId()));
							orderNew.setPkCgip(item.getPkCgip());
							if(flagMaster.equals("0")){
								flagMaster="1";
								orderNew.setFlagMaster("1");
								orderNew.setCodeOrdSeq(item.getCode());
							}else{
								orderNew.setFlagMaster("0");
							}
							listSysn.add(orderNew);
							bHaveBl=true;
						}
					}
					if(!bHaveBl){
												
						listSysn.add(order);
					}
				}
				size=listSysn.size();
				
				int serialNo = bdSnService.getSerialNo("cn_order","order_no", size, u);
				for(i=0;i<listSysn.size();i++){
					CnOrderSync orderSync=listSysn.get(i);
					Long orderNo=new Long(serialNo+i);
					orderSync.setOrderNo(orderNo);
					String pkCgip=orderSync.getPkCgip();
					String pkCnord=orderSync.getPkCnord();
					if(pkCgip!=null&&!pkCgip.equals("")){
						DataBaseHelper.update("update bl_ip_dt set charge_id = ? where pk_cgip = ?",new Object[] { orderNo,pkCgip});
					}else{
						DataBaseHelper.update("update cn_order set order_no = ? where pk_cnord = ?",new Object[] { orderNo,pkCnord});
					}
				}
			}else{
				listSign = cnOrderSyncMapper.queryCnOrderSync(paramMap);
				if(listSign==null||listSign.size()==0) return;

				if(optType.equals("D")){
					//删除记费记录
					
					//listSysn.addAll(listSign);
					DataBaseHelper.batchUpdate("delete from bl_ip_dt where pk_pv=:pkPv and pk_cnord=:pkCnord and flag_settle='0'",listSign);
				}	

				//listSysn.addAll(listSign);
				List<CnBlIpDtVo> itemList=cnOrderSyncMapper.queryCnOrderIpBlDtSync(paramMap);
				boolean bHaveBl=false;
				CnOrderSync orderNew;
				for(i=0;i<listSign.size();i++){
					CnOrderSync order = listSign.get(i);
					bHaveBl=false;
					for(j=0;itemList!=null&&j<itemList.size();j++){
						CnBlIpDtVo item=itemList.get(j);
						if(item==null) continue;
						if(item.getPkCnord()==null) continue;
						if(item.getPkCnord().equals(order.getPkCnord())){
							orderNew=new CnOrderSync();
							ApplicationUtils.copyProperties(orderNew, order);
							orderNew.setCodeOrd(item.getCode());
							orderNew.setNameOrd(item.getNameCg()+"/"+item.getSpec());
							orderNew.setSpec(item.getSpec());
							orderNew.setPriceCg(item.getPrice());
							orderNew.setQuanCg(item.getQuan());
							orderNew.setCodeUnitCg(item.getUnitCode());
							orderNew.setEuHptype(item.getEuHptype());
							orderNew.setOrderNo(new Long(item.getChargeId()));
							orderNew.setPkCgip(item.getPkCgip());
							listSysn.add(orderNew);
							bHaveBl=true;
						}
					}
					if(!bHaveBl){
						//order.setOrderNo(new Long(order.getGroupno()));
						
						listSysn.add(order);
					}
				}
			}

			//数据同步方式：1、数据2消息
			ordSyncHandler.saveOrderSync(listSysn, u,optType);
			//System.out.println("syncSendOrds optType:"+optType);
			Long ordsn=new Long("-1");
			// X_M 	 J_E  MS Z_D  LCYX
			if(reqType!=null&&reqType.equals("LIS")||reqType.equals("PACS")||reqType.equals("OBS")){
				List<CnOrderSync> listReq=new ArrayList<>();
				for (CnOrderSync ordTmp : listSysn) {
					if(ordTmp.getCodeOrdtype()==null) continue;
					String ordTypeCls=ordTmp.getCodeOrdtype().substring(0,2);
					if(ordTypeCls.equals("02")||ordTypeCls.equals("03")){
						if(ordsn.intValue()!=ordTmp.getOrdsn().intValue()){
							listReq.add(ordTmp);
							ordsn=ordTmp.getOrdsn();
						}
					}
				}
				if(listReq.size()>0){
					PvDiag pvDiag=null;
					if(optType.equals("N")){
						pvDiag =DataBaseHelper.queryForBean("select * from pv_diag where pk_pv=? and flag_maj = '1' and del_flag='0' ", PvDiag.class, pkPv);	
					}
					
					//保存申请信息
					ordSyncHandler.saveReqInfoSync(listReq, u,optType,reqType,pvDiag);
				}
			}
		}
	}
	
	
	/**核对取消签署医嘱状态
	 * @param list
	 * @param u
	 * @return
	 */
	public String checkCancelSign(List<CnOrder> list,User u){
		//判断是否可以取消签署开关（目前只有急诊项目在用）
		String checkCancelStopSwitch=ApplicationUtils.getPropertyValue("sync.check.cancelStop.switch", "0");
		if(checkCancelStopSwitch!=null&&checkCancelStopSwitch.equals("1")){
			//根据医嘱号获取order_no
			if(list==null||list.size()==0) return "";
			
			List<Integer> ordsns = new ArrayList<>();
			for (CnOrder order : list) {
				ordsns.add(order.getOrdsn());
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPv", list.get(0).getPkPv());
			paramMap.put("ordsns", ordsns);
			List<CnBlIpDtVo> itemList=cnOrderSyncMapper.queryCnOrderIpBlDtSync(paramMap);
			if(itemList==null||itemList.size()==0) return "";
			
			return ordSyncHandler.checkCancelSign(itemList,u,itemList.get(0).getCodePv());
		}else{
			return "";
		}
	}
	
	/**核对取消停止医嘱状态
	 * @param list
	 * @param u
	 * @return
	 */
	public String checkCancelStop(List<CnOrder> list,User u){
		//判断是否可以取消停止开关（目前只有急诊项目在用）
		String checkCancelStopSwitch=ApplicationUtils.getPropertyValue("sync.check.cancelStop.switch", "0");
		if(checkCancelStopSwitch!=null&&checkCancelStopSwitch.equals("1")){
			//根据医嘱号获取order_no
			if(list==null||list.size()==0) return "";
			
			List<Integer> ordsns = new ArrayList<>();
			for (CnOrder order : list) {
				ordsns.add(order.getOrdsn());
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPv", list.get(0).getPkPv());
			paramMap.put("ordsns", ordsns);
			List<CnBlIpDtVo> itemList=cnOrderSyncMapper.queryCnOrderIpBlDtSync(paramMap);
			if(itemList==null||itemList.size()==0) return "";
			
			return ordSyncHandler.checkCancelStop(itemList,u,itemList.get(0).getCodePv());
		}
		return "";
	}
	
	//同步医嘱信息
	public void syncSendOrds2(List<CnOrder> cnOrds, User u,String optType) {
		//数据同步发送开关（目前只有急诊项目在用）
		String syncSendSwitch=ApplicationUtils.getPropertyValue("sync.send.switch", "0");
		if(syncSendSwitch!=null&&syncSendSwitch.equals("1")){
			List<Integer> ordsns = new ArrayList<>();
			for (CnOrder order : cnOrds) {
				ordsns.add(order.getOrdsn());
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkPv", cnOrds.get(0).getPkPv());
			paramMap.put("ordsns", ordsns);
			List<CnOrderSyncVo> listSign=cnOrderSyncMapper.queryCnOrderSync(paramMap);
			if(listSign==null||listSign.size()==0) return;
			List<BlPubParamVo> list=new ArrayList<BlPubParamVo>();
			for (CnOrderSync cnOrderSync : listSign) {
				BlPubParamVo vo =new BlPubParamVo();
				ApplicationUtils.copyProperties(vo, cnOrderSync);
				list.add(vo);
			}

			cnIpOrdCgService.blIpCgBatch(list);
			//数据同步方式：1、数据2消息
			//ordSyncHandler.saveOrderSync(listSign, u,optType);
			//System.out.println("syncSendOrds optType:"+optType);
		}
	}
	
	
	/**
	 * 同步病人就诊信息
	 * @param pkDept
	 * @param u
	 */
	public void syncPatEncInfo(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
    	
		cnOrderSyncMapper.syncPatEncInfo(map);
		
		String rtnCode=map.get("rtnCode")==null?"0":map.get("rtnCode").toString();
		if(!rtnCode.equals("")&&!rtnCode.equals("0")){
			String rtnMsg=map.get("rtnMsg")==null?"":map.get("rtnMsg").toString();
			throw new BusException(rtnMsg);  
		}
	}
	
	/**同步诊断信息
	 * @param diagList
	 * @param pkPv
	 */
	public void syncDiagInfo(List<PvDiag> diagList,User u) {
		String checkCancelStopSwitch=ApplicationUtils.getPropertyValue("sync.pvdiag.switch", "0");
		if(checkCancelStopSwitch!=null&&checkCancelStopSwitch.equals("1")){
			if(diagList==null||diagList.size()==0) return;
			String pkPv=diagList.get(0).getPkPv();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("pkPv", pkPv);
			List<PvDiagVo> list=cnOrderSyncMapper.querySyncPvDiags(map);
			
			ordSyncHandler.syncDiagInfo(list,u);
		}
	}
	
	/**
	 * 查询打印报表号(根据医嘱分类)
	 * @param param
	 * @param user
	 * @return
	 */
	public String getOrdTypeRptCode(String param , IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		
		String reportCode = DataBaseHelper.queryForScalar("select btp.code from BD_ORDTYPE_RELATION bor inner join bd_temp_prt btp on  bor.pk_tempprt=btp.pk_tempprt inner join  bd_ordtype bo on bor.pk_ordtype=bo.pk_ordtype where bo.code=? ", String.class, map.get("code"));
		
		return reportCode ;
	}
}
