package com.zebone.nhis.ma.pub.sd.service;

import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SdHighValueConsumHandler {
	
	public Object invokeMethod(String methodName,Object...args){
		Object obj=new Object();
    	switch(methodName){
	    	case "saveConsumable":
	    		this.saveConsumable(args);
	    		break;
	    	case "savaReturnConsumable":
	    		this.savaReturnConsumable(args);
	    		break;
	    	case "qryHightItemBybarcode":
	    	    obj= this.qryHightItemBybarcode(args);
	    		break;
	    	case "checkHighValueConsum":
	    	    this.checkHighValueConsum(args);
	    		break;
	    	case "qryHighIsDo":
	    		obj=this.qryHighIsDo(args);
	    		break;
	    	case "saveOpConsumable":
	    		this.saveOpConsumable(args);
	    		break;
	    	case "savaOpReturnConsumable":
	    		this.savaOpReturnConsumable(args);
	    		break;
    	} 
    	return obj;
	}
	
	/**
	 * 通过材料编码查询高值耗材中对应的收费项目
	 * @param param{"barcode":"材料编码"}
	 * @param user
	 * @return
	 */
	public String qryHightItemBybarcode(Object...args){
			String param=(String)args[0];
			String barcode=JsonUtil.getFieldValue(param, "barcode");
			if(CommonUtils.isEmptyString(barcode)){
				throw new BusException("未获取到材料条码！");
			}
			if(barcode.length()<9){
				throw new BusException("请输入正确高值耗材材料条码！");
			}
			String codeStd=barcode.substring(0, 9);
			String sql="select pk_item from bd_item where code_std=? ";
			List<Map<String,Object>> resList=DataBaseHelper.queryForList(sql, new Object[]{codeStd});
			if(resList==null || resList.size()==0 ||resList.get(0)==null || resList.get(0).get("pkItem")==null){
				throw new BusException("未获取到对应材料条码【"+barcode+"】的收费项目，请联系物价进行核对！");
			}
			if(resList.size()!=1){
				throw new BusException("获取到对应材料条码【"+barcode+"】的收费项目存在多条，请联系物价进行核对！");
			}

			sql="SELECT * FROM sd_high_value where barcode = ? ";
			List<Map<String,Object>> resList1=DataBaseHelper.queryForList(sql, new Object[]{barcode});
			if(resList1!=null && resList1.size()>0 && resList1.get(0)!=null && resList1.get(0).get("barcode")!=null){
				throw new BusException("对应材料条码【"+barcode+"】的收费项目已计费，请联系物价进行核对！");
			}
			return resList.get(0).get("pkItem").toString();
	}
	
	/**
	 * 校验数据：库存，当前无接口需要
	 * @param param{paramList：{"oldId":"老系统项目编码";"pkDeptEx":"执行科室";"barcode":"材料编码";"quanCg":"需要数量"}}
	 * @param user
	 */
	public void checkHighValueConsum(Object...args){
		
	}
	/**
	 * 高值耗材记费接口，修改库存（中间库）
	 * @param args
	 */
	public void saveConsumable(Object...args){
			String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
			if(args==null || args.length<2 ||args[1]==null)return ;
			BlPubReturnVo blretvo=new BlPubReturnVo();
			BeanUtils.copyProperties(args[1], blretvo);
			List<String> pkcgIps=new ArrayList<String>();
			String sqlIn="";
			if(blretvo!=null && blretvo.getBids()!=null && blretvo.getBids().size()>0){
				Set<String> pkItems=new HashSet<>();
				for (BlIpDt ipcgvo : blretvo.getBids()) {
					if(CommonUtils.isNull(ipcgvo.getBarcode()) && "0".equals(ipcgvo.getFlagPd())){
						pkItems.add(ipcgvo.getPkItem());
					}
					if(!CommonUtils.isNull(ipcgvo.getBarcode())){
						sqlIn="INSERT INTO sd_high_value (BARCODE, PK_CG, DEL_FLAG) VALUES (?,?,'0')";
						DataBaseHelper.execute(sqlIn,new Object[]{ipcgvo.getBarcode(),ipcgvo.getPkCgip()});
					}
				}
				if(pkItems.size()>0){
					String sql="select name ,spec from bd_item where dt_itemtype='0701' and pk_item in ("+CommonUtils.convertSetToSqlInPart(pkItems, "pk_item")+")";
					List<Map<String,Object>> resList=DataBaseHelper.queryForList(sql, new Object[]{});
					
					if(resList.size()>0){
						StringBuffer message=new StringBuffer();
						for (Map<String, Object> map : resList) {
							message.append("项目名称：【");
							message.append(map.get("name"));
							message.append("】，项目规格：【");
							message.append(map.get("spec"));
							message.append("】\n");
						}
						throw new BusException("以下高值耗材项目未录入材料条码，请删除后通过条码录入\n"+message);
					}
				    
				}
				for (BlIpDt ipcgvo : blretvo.getBids()) {
					if(CommonUtils.isNotNull(ipcgvo.getBarcode())){
						pkcgIps.add(ipcgvo.getPkCgip());
					}
				}
			}
			if(pkcgIps.size()>0){
				if(isHighValue==null){
					throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
				}
				if("0".equals(isHighValue))return;
				Map<String,Object> paramMap=new HashMap<>();
				paramMap.put("pkcgIpList", pkcgIps);
				PlatFormSendUtils.sendHighValueConSumIp(paramMap);
			}
	}
	
	
	/**
	 * 高值耗材记费接口，修改库存（中间库）
	 * @param args
	 */
	public void saveOpConsumable(Object...args){
			String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
			if(args==null || args.length<2 ||args[1]==null)return ;
			BlPubReturnVo blretvo=new BlPubReturnVo();
			BeanUtils.copyProperties(args[1], blretvo);

			List<String> pkcgOps=new ArrayList<String>();
			if(blretvo!=null && blretvo.getBods()!=null && blretvo.getBods().size()>0){
				List<BlOpDt> pubParamVoList = blretvo.getBods().stream().filter(v -> StringUtils.isNotBlank(v.getBarcode())).collect(Collectors.toList());
				if(pubParamVoList!=null&& pubParamVoList.size()>0) {
					Set<String> pkItems=new HashSet<>();
					String sqlIn = null;
					for (BlOpDt opcgvo : blretvo.getBods()) {
						if(!CommonUtils.isEmptyString(opcgvo.getBarcode())){
							sqlIn="INSERT INTO sd_high_value (BARCODE, PK_CG, DEL_FLAG) VALUES (?,?,'0')";
							DataBaseHelper.execute(sqlIn,new Object[]{opcgvo.getBarcode(),opcgvo.getPkCgop()});
						}
					}
				}
				for (BlOpDt ipcgvo : blretvo.getBods()) {
					if(CommonUtils.isNotNull(ipcgvo.getBarcode())){
						pkcgOps.add(ipcgvo.getPkCgop());
					}
				}
			}
			if(pkcgOps.size()>0){
				if(isHighValue==null){
					throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
				}
				if("0".equals(isHighValue))return;
				Map<String,Object> paramMap=new HashMap<>();
				paramMap.put("pkcgOpList", pkcgOps);
				paramMap.put("type", "OP");
				PlatFormSendUtils.sendHighValueConSumIp(paramMap);
			}
	}
	
	/**
	 * 高值耗材退费接口
	 * @param args
	 */
	public void savaReturnConsumable(Object...args){
		String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
		if(args==null || args.length<2 ||args[1]==null)return ;
		BlPubReturnVo blretvo=new BlPubReturnVo();
		BeanUtils.copyProperties(args[1], blretvo);
		List<String> pkcgIps=new ArrayList<String>();
		String sqlIn="";
		if(blretvo!=null && blretvo.getBids()!=null && blretvo.getBids().size()>0){
			for (BlIpDt ipcgvo : blretvo.getBids()) {
				if(CommonUtils.isNotNull(ipcgvo.getBarcode())){
					pkcgIps.add(ipcgvo.getPkCgip());
				}
				if(!CommonUtils.isNull(ipcgvo.getBarcode())){
					sqlIn="DELETE FROM sd_high_value where barcode=? ";
					DataBaseHelper.execute(sqlIn,new Object[]{ipcgvo.getBarcode()});
				}
			}
		}
		if(pkcgIps.size()>0){
			if(isHighValue==null){
				throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
			}
			if("0".equals(isHighValue))return;
			Map<String,Object> paramMap=new HashMap<>();
			paramMap.put("pkcgIpList", pkcgIps);
			PlatFormSendUtils.sendHighValueConSumIpBack(paramMap);
		}
			
	}
	/**
	 * 高值耗材退费接口
	 * @param args
	 */
	public void savaOpReturnConsumable(Object...args){
		String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
		if(args==null || args.length<2 ||args[1]==null)return ;
		BlPubReturnVo blretvo=new BlPubReturnVo();
		BeanUtils.copyProperties(args[1], blretvo);
		List<String> pkcgOps=new ArrayList<String>();
		String sqlIn="";
		if(blretvo!=null && blretvo.getBods()!=null && blretvo.getBods().size()>0){
			for (BlOpDt ipcgvo : blretvo.getBods()) {
				if(CommonUtils.isNotNull(ipcgvo.getBarcode())
						&& CommonUtils.isNotNull(ipcgvo.getPkCgop()))
				{
					pkcgOps.add(ipcgvo.getPkCgop());
				}
				if(CommonUtils.isNotNull(ipcgvo.getBarcode())){
					sqlIn="DELETE FROM sd_high_value where barcode=? ";
					DataBaseHelper.execute(sqlIn,new Object[]{ipcgvo.getBarcode()});
				}
			}
		}
		if(pkcgOps.size()>0){
			if(isHighValue==null){
				throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
			}
			if("0".equals(isHighValue))return;
			Map<String,Object> paramMap=new HashMap<>();
			paramMap.put("pkcgOpList", pkcgOps);
			paramMap.put("type", "OP");
			PlatFormSendUtils.sendHighValueConSumIpBack(paramMap);
		}
			
	}
	
	/**
	 * 校验执行科室是否启用高值耗材
	 * @param param
	 * @param user
	 * @return
	 */
	public int qryHighIsDo(Object... args){
		return 1;
	}
}
