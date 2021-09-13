package com.zebone.nhis.cn.ipdw.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.zebone.nhis.cn.ipdw.service.CnIpOrdSyncService;
import com.zebone.nhis.cn.ipdw.vo.CnBlIpDtVo;
import com.zebone.nhis.cn.ipdw.vo.PvDiagVo;
import com.zebone.nhis.cn.ipdw.vo.ReqTmpItemVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrderSync;
import com.zebone.nhis.common.module.ma.tpi.ems.JgPacsReq;
import com.zebone.nhis.common.module.ma.tpi.ems.YjSq01;
import com.zebone.nhis.common.module.ma.tpi.ems.YjSq02;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

/**
 * 住院医嘱数据同步处理类
 * @author chengjia
 *
 */
@Service
public class CnIpOrdSyncHandler {
	
	@Resource
	private CnIpOrdSyncService ordSyncService;
	
	public static final String reqMapPropName = "sync.ordtype.seqtab.map";
	
	/**
	 * 同步医嘱信息
	 * @param list
	 * @param u
	 */
	public void saveOrderSync(List<CnOrderSync> list,User u,String optType){
		//System.out.println("saveOrderSync 1111");
		DataSourceRoute.putAppId("jghis");
		try {
			ordSyncService.saveOrderSync(list,u,optType);
		} catch (Exception e) {
			throw e;
		}finally{
			DataSourceRoute.putAppId("default");
			//System.out.println("saveOrderSync default");
		}
	
	}
	
	/**
	 * 同步申请信息
	 * @param list
	 * @param u
	 */
	public void saveReqInfoSync(List<CnOrderSync> list,User u,String optType,String reqType,PvDiag diag){
		//System.out.println("saveOrderSync 1111");
		DataSourceRoute.putAppId("jghis");
		try {
			if(optType.equals("N")){
				//新增
				List<ReqTmpItemVo> reqItemList=new ArrayList<>();
				List<YjSq02> itemList=new ArrayList<>();
				for (CnOrderSync cnOrderSync : list) {
					//Integer sn = ordSyncService.createSyncHisSn("YJ_ZY01");
					//cnOrderSync.setReqSn(sn);
					cnOrderSync.setReqSn(Integer.parseInt(cnOrderSync.getCodeApply()));
					//改为code_apply前台生成
					String reqTabNo=getDictCodeMap(reqMapPropName,cnOrderSync.getCodeOrdtype());
					if(reqTabNo.equals("")){
						if(reqType.equals("LIS")){
							reqTabNo="40";
						}else{
							reqTabNo="42";
						}
					}
					
					String xmxh=cnOrderSync.getCodeOrd();
					if(cnOrderSync.getFlagSet()!=null&&cnOrderSync.getFlagSet().equals("1")) xmxh=cnOrderSync.getCodeOrdSeq();
					//int no = ordSyncService.queryReqTabNo(xmxh);
					//if(no<=0) no=Integer.parseInt(reqTabNo);
					int no = ordSyncService.queryReqTabNo(xmxh);
					if(no==0) no = Integer.parseInt(reqTabNo);
					cnOrderSync.setReqTabNo(no);
					if(reqType.equals("PACS")){
						//String reqTmpNo = ordSyncService.queryReqTmpNo(no);
						/*if(reqTmpNo==null||reqTmpNo.equals("")){
							reqTmpNo="XR";
						}*/
						String reqTmpNo="XR";
						String codeType=cnOrderSync.getCodeType();
						if(codeType==null) codeType="";
						if(codeType.equals("1")){
							reqTmpNo="CT";
						}else if(codeType.equals("2")||codeType.equals("18")){
							reqTmpNo="US";
						}else if(codeType.equals("4")){
							reqTmpNo="ES";
						}else if(codeType.equals("5")){
							reqTmpNo="XR";
						}else if(codeType.equals("6")){
							reqTmpNo="US";
						}else if(codeType.equals("7")){
							reqTmpNo="XR";}
						else if(codeType.equals("16")){
							reqTmpNo="MR";
						}
						cnOrderSync.setReqTmpNo(reqTmpNo);
						
					}
					
				}
				
				itemList = new ArrayList<YjSq02>();
				YjSq02 item;
				//获取申请单模板明细
				Map<String,Object> map = new HashMap<String,Object>();
				List<Integer> mbxhs = new ArrayList<>();
				for (CnOrderSync syncOrd : list) {
					mbxhs.add(syncOrd.getReqTabNo());
				}
				map.put("mbxhs", mbxhs);
				reqItemList=ordSyncService.queryReqTmpItems(map);
				for (CnOrderSync ord : list) {
					if(reqItemList!=null&&reqItemList.size()>0){
						//获取申请单项目数量，生成序号
						for (ReqTmpItemVo reqTmpItemVo : reqItemList) {
							Integer mbxh=reqTmpItemVo.getMbxh();
							if(mbxh==ord.getReqTabNo()){
								item=new YjSq02();
								Integer itemSn = ordSyncService.createSyncHisSn("YJ_SQ02");
								item.setSbxh(itemSn);
								item.setYjxh(ord.getReqSn());
								item.setMzzy(2);
								item.setNbmc(reqTmpItemVo.getNbmc());
								String dxmc=reqTmpItemVo.getDxmc();
								String dxqz=reqTmpItemVo.getDxmc();
								String nbmc=reqTmpItemVo.getNbmc();
								if(dxqz==null) dxqz="";
								if(dxmc==null) dxmc="";
								if(nbmc==null) nbmc="";
								item.setDxmc(dxmc);
								String value="";
								item.setDxqz(value);
								//dxmc in ('X_M','J_E','MS','Z_D','LCYX')
								if(dxmc.equals("X_M")){
									//项目
									value=ord.getDescOrd();
								}else if(dxmc.equals("J_E")){
									//金额
									value=ord.getPrice()==null?"":ord.getPrice().toString();
								}else if(dxmc.equals("MS")){
									//描述
									value=ord.getNoteDise();
									if(reqType.equals("LIS")) value=ord.getNoteLis();
								}else if(dxmc.equals("Z_D")){
									//诊断
									value=ord.getNameDiagRis();
									if(reqType.equals("LIS")) value=ord.getDescDiagLis();
									if(value==null||value.equals("")){
										if(diag!=null){
											value=diag.getDescDiag();
										}
									}
//								}else if(dxmc.equals("LCYX")){
//									//临床(印象)
//									value=ord.getNoteDise();
//								}else if(dxqz.indexOf("临床印象")>=0){
//									value=ord.getNoteDise();
//								}else if(dxqz.indexOf("病史及特征")>=0){
//									value=ord.getNoteDise();
//								}else if(dxqz.indexOf("病历摘要")>=0){
//									value=ord.getNoteDise();
//								}else if(dxqz.indexOf("症    状")>=0){
//									value=ord.getNoteDise();
//								}else if(dxqz.indexOf("检查部位")>=0){
//									value=ord.getDescBody();
//								}else if(dxqz.indexOf("目  的")>=0){
//									value=ord.getPurposeRis();
								//dxqz '临床印象' '病史及特征' '病历摘要' '症    状' '检查部位' '目  的'
								}else if(mbxh==38 && nbmc.equals("item12")){
									//超声检查申请单
									value=ord.getNoteDise();//@todo
								}else if(mbxh==40 && nbmc.equals("item23")){
									//检验申请单/样本类型
									value=ord.getSamptypeName();
								}else if(mbxh==40 && nbmc.equals("item24")){
									//检验申请单
									value="";
								}else if(mbxh==41 && nbmc.equals("item23")){
									//X线检查申请单/LCYX
									value=ord.getNoteDise();
								}else if(mbxh==42 && nbmc.equals("item44")){
									//透视单
									value="";
								}else if(mbxh==43 && nbmc.equals("item40")){
									//心功能检查申请单
									value=ord.getNoteDise();
								}else if(mbxh==43 && nbmc.equals("item41")){
									//心功能检查申请单
									value="";
								}else if(mbxh==44 && nbmc.equals("item21")){
									//CT检查申请单
									value="";
								}else if(mbxh==44 && nbmc.equals("item23")){
									//CT检查申请单/诊断/LCYX
//									value=ord.getNameDiagRis();
//									if(value==null||value.equals("")) value=diag.getDescDiag();
									value=ord.getNoteDise();
								}else if(mbxh==44 && nbmc.equals("item26")){
									//CT检查申请单
									value="";
								}else if(mbxh==44 && nbmc.equals("item50")){
									//GMS
									value=ord.getNoteRis();
								}else if(mbxh==53 && nbmc.equals("item40")){
									//心电图检查申请单/病情
									value=ord.getNoteDise();
								}else if(mbxh==53 && nbmc.equals("item41")){
									//心电图检查申请单
									value="";
								}else if(mbxh==54 && nbmc.equals("item40")){
									//Holter申请单
									value=ord.getNoteDise();
								}else if(mbxh==54 && nbmc.equals("item41")){
									//Holter申请单
									value="";
								}else if(mbxh==55 && nbmc.equals("item40")){
									//运动平板试验
									value=ord.getNoteDise();
								}else if(mbxh==55 && nbmc.equals("item41")){
									//运动平板试验
									value="";
								}else if(mbxh==56 && nbmc.equals("item40")){
									//动态血压申请单
									value=ord.getNoteDise();
								}else if(mbxh==56 && nbmc.equals("item41")){
									//动态血压申请单
									value="";
								}else if(mbxh==57 && nbmc.equals("item40")){
									//肢体动脉检查
									value=ord.getNoteDise();
								}else if(mbxh==57 && nbmc.equals("item41")){
									//肢体动脉检查
									value="";
								}else if(mbxh==58 && nbmc.equals("item41")){
									//脑电图申请单
									value=ord.getNoteDise();
								}else if(mbxh==58 && nbmc.equals("item47")){
									//脑电图申请单
									value="";
								}else if(mbxh==58 && nbmc.equals("item52")){
									//脑电图申请单/ECG
									value="";
								}else if(mbxh==58 && nbmc.equals("item53")){
									//脑电图申请单/VEEG
									value="";
								}else if(mbxh==58 && nbmc.equals("item54")){
									//脑电图申请单
									value="";
								}else if(mbxh==58 && nbmc.equals("item55")){
									//脑电图申请单
									value="";
								}else if(mbxh==59 && nbmc.equals("item44")){
									//MR检查申请单
									value=ord.getNoteDise();
								}else if(mbxh==59 && nbmc.equals("item36")){
									//MR检查申请单/GMS
									value="";
								}else if(mbxh==59 && nbmc.equals("item46")){
									//MR检查申请单
									value="";
								}else if(mbxh==59 && nbmc.equals("item49")){
									//MR检查申请单/LCYX
									value=ord.getNoteDise();
								}else if(mbxh==59 && nbmc.equals("item69")){
									//MR检查申请单
									value="";	
								}else if(mbxh==61 && nbmc.equals("item40")){
									//肺功能检查申请单
									value=ord.getNoteDise();
								}else if(mbxh==61 && nbmc.equals("item41")){
									//肺功能检查申请单
									value="";
								}else if(mbxh==63 && nbmc.equals("item40")){
									//超声心动图申请单
									value=ord.getNoteDise();
								}else if(mbxh==63 && nbmc.equals("item41")){
									//超声心动图申请单
									value="";
								}else if(mbxh==70 && nbmc.equals("item44")){
									//功能检查申请单
									value=ord.getPurposeRis();
								}
									
								item.setDxqz(value);
								
								itemList.add(item);
								
							}
						}
					}
				}
				ordSyncService.saveReqInfoSync(list,u,optType,reqType,itemList);
				if(reqType.equals("PACS")){
					savePacsReqInfoSync(list,u,optType,diag,null);
				}
			}else if(optType.equals("C")||optType.equals("D")){
				//删除或作废
				List<YjSq01> reqItemDel=ordSyncService.saveReqInfoSync(list,u,optType,reqType,null);
				if(reqType.equals("PACS")||reqType.equals("OBS")){
					savePacsReqInfoSync(list,u,optType,diag,reqItemDel);
				}
			}
			
			
		} catch (Exception e) {
			throw e;
		}finally{
			DataSourceRoute.putAppId("default");
			//System.out.println("saveOrderSync default");
		}
	
	}
	
	/**
	 * 同步申请信息(PACS)
	 * @param list
	 * @param u
	 */
	public void savePacsReqInfoSync(List<CnOrderSync> list,User u,String optType,PvDiag diag,List<YjSq01> reqItemDel){
		if(list==null||list.size()==0) return;
		String pkPv=list.get(0).getPkPv();
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkPv", pkPv);
		List<Map<String,Object>> patMapList=ordSyncService.queryPatList(map);
		
		DataSourceRoute.putAppId("jgPacs");
		try {
			if(optType.equals("N")){
				//新增
				if(patMapList!=null&&patMapList.size()>0){
					ordSyncService.savePacsReqInfoSync(list,u,optType,patMapList.get(0),diag,null);
				}
			}else if(optType.equals("C")||optType.equals("D")){
				//删除或作废
				ordSyncService.savePacsReqInfoSync(list,u,optType,null,diag,reqItemDel);
			}
			
		} catch (Exception e) {
			throw e;
		}finally{
			DataSourceRoute.putAppId("default");
		}
	
	}
	
	/**核对取消签署医嘱状态
	 * @param list
	 * @param u
	 * @return
	 */
	public String checkCancelSign(List<CnBlIpDtVo> list,User u,String codePv){
		
		DataSourceRoute.putAppId("jghis");
		try {
			return ordSyncService.checkCancelSign(list, u,codePv);
		} catch (Exception e) {
			return e.getMessage();
			//throw e;
		}finally{
			DataSourceRoute.putAppId("default");
			//System.out.println("checkCancelSign default");
		}

	}
	
	/**核对取消停止医嘱状态
	 * @param list
	 * @param u
	 * @return
	 */
	public String checkCancelStop(List<CnBlIpDtVo> list,User u,String codePv){
//		//判断是否可以取消停止开关（目前只有急诊项目在用）
//		String checkCancelStopSwitch=ApplicationUtils.getPropertyValue("sync.check.cancelStop.switch", "0");
//		if(checkCancelStopSwitch!=null&&checkCancelStopSwitch.equals("1")){
			DataSourceRoute.putAppId("jghis");
			try {
				return ordSyncService.checkCancelStop(list, u,codePv);
			} catch (Exception e) {
				//throw e;
				return e.getMessage();
			}finally{
				DataSourceRoute.putAppId("default");
				//System.out.println("checkCancelStop default");
			}
//		}
		
	}

	/**
	 * 同步诊断信息
	 * @param list
	 * @param u
	 */
	public void syncDiagInfo(List<PvDiagVo> diagList,User u){
		DataSourceRoute.putAppId("jghis");
		try {
			for (PvDiagVo pvDiagVo : diagList) {
				Integer sn = ordSyncService.createSyncHisSnYs("YS_ZY_JBZD");
				//System.out.println("sn:"+sn);
				pvDiagVo.setSn(sn);
			}
			
			ordSyncService.saveDiagInfoSync(diagList,u);
			
		} catch (Exception e) {
			throw e;
		}finally{
			DataSourceRoute.putAppId("default");
		}
	
	}
	
	
	/**
	 * 获取his流水号
	 * @param list
	 * @param u
	 */
	public Integer createSyncHisSnMax(String code){
		DataSourceRoute.putAppId("jghis");
		try {
			Integer sn = ordSyncService.createSyncHisSnMax(code);
			
			return sn;
			
		} catch (Exception e) {
			throw e;
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	/**
	 * 获取his申请单号
	 * @param list
	 * @param u
	 */
	public Integer createSyncHisSn(String param,IUser user){
		DataSourceRoute.putAppId("jghis");
		try {
			//目前LIS、PACS取自同一流水号
			Integer sn = ordSyncService.createSyncHisSn("YJ_ZY01");
			
			return sn;
			
		} catch (Exception e) {
			throw e;
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	
	
	public static String getDictCodeMap(String field,String code){
		String str=ApplicationUtils.getPropertyValue(field, "");
		if(str==null||str.equals("")) return "";
		Map<String,String> map=(Map<String, String>) JSONUtils.parse(str);
		if(map==null) return "";
		if(!map.containsKey(code)) return "";
		String rtnCode=map.get(code);
		if(rtnCode==null) rtnCode="";
		return rtnCode;
	}
	
	
}
