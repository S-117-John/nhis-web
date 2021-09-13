package com.zebone.nhis.ma.pub.lb.service;

import java.lang.reflect.Field;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.lb.vo.RequConsumablesQuerVo;
import com.zebone.nhis.ma.pub.lb.vo.RequConsumablesVo;
import com.zebone.nhis.ma.pub.lb.vo.HighValueHRPVo;
import com.zebone.platform.modules.dao.DataBaseHelper;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.scm.st.PdSingle;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.pub.lb.dao.LbHighValueConsumMapper;
import com.zebone.nhis.ma.pub.lb.vo.HighValuePdstVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

import org.springframework.web.client.RestTemplate;

/**
 * 灵璧高值耗材接口实现
 * @author jd
 *
 */
@Service
public class LbHighValueConsumService {
	
	@Resource
	private LbHighValueConsumMapper consumMapper;
	private Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");
	/**
	 * 高值耗材记费
	 * @param ipdtList
	 */
	public void saveConsumable(List<BlIpDt> ipdtList,List<String> barcodes){
		ApplicationUtils apputil = new ApplicationUtils();
		List<HighValuePdstVo> resList=consumMapper.queryOutstparam(barcodes);
		if(resList==null || resList.size()==0)return;
		PdStVo stvo=new PdStVo();
		List<PdStDtVo> stdtList=new ArrayList<PdStDtVo>();
		int index=1;
		for (BlIpDt ipdt : ipdtList) {
			for (HighValuePdstVo map : resList) {
				BeanUtils.copyProperties(map, stvo);
				if(ipdt.getBarcode().equals(map.getBarcode())){
					PdStDtVo stdtvo=new PdStDtVo();
					BeanUtils.copyProperties(map, stdtvo);
					stdtvo.setSortNo(index);
					stdtvo.setPackSize(1);
					stdtvo.setQuanPack(ipdt.getQuan());
					stdtvo.setQuanMin(ipdt.getQuan());
					stdtvo.setQuanOutstore(0D);
					stdtvo.setAmount(ipdt.getAmount());
					stdtvo.setAmountCost(ipdt.getAmount());
					stdtvo.setAmountPay(0D);
					stdtvo.setFlagSingle("1");
					stdtvo.setPriceCurrent(stdtvo.getPrice());
					stdtvo.setPriceCostCurrent(stdtvo.getPrice());
					
					
					List<PdSingle> signList=new ArrayList<PdSingle>(); 
					PdSingle single=new PdSingle();
					BeanUtils.copyProperties(map, single);
					single.setEuStatus("1");
					signList.add(single);
					stdtvo.setSinList(signList);
					stdtList.add(stdtvo);
					index++;
				}
			}
		}
		User user=UserContext.getUser(); 
		stvo.setDtSttype("0202");//科室领用
		stvo.setCodeSt(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_RKD));
		stvo.setDateSt(new Date());
		stvo.setEuStatus("0");
		stvo.setEuDirect("-1");
		stvo.setPkOrgLk(user.getPkOrg());
		stvo.setPkDeptLk(user.getPkDept());
		stvo.setFlagPay("0");
		stvo.setPkEmpOp(user.getPkEmp());
		stvo.setNameEmpOp(user.getNameEmp());
		stvo.setFlagChk("0");
		stvo.setNote("高值耗材记费出库");
		stvo.setFlagPu("0");
		
		stvo.setDtlist(stdtList);
		System.out.println(JsonUtil.writeValueAsString(stvo));
		ResponseJson  result =  apputil.execService("SCM", "MtlPdOutStService", "savePdSt", stvo,UserContext.getUser());
		if(result.getStatus()<0){
			throw new BusException(result.getDesc());
		}
		stvo=(PdStVo) result.getData();
	    stvo.setEuStatus("1");
	    stvo.setFlagChk("1");
	    stvo.setPkEmpChk(user.getPkEmp());
	    stvo.setNameEmpChk(user.getNameEmp());
	    stvo.setDateChk(new Date());
	    result =  apputil.execService("SCM", "MtlPdOutStService", "submitPdSt", stvo,UserContext.getUser());
	    if(result.getStatus()<0){
			throw new BusException(result.getDesc());
		}
	}

	/**
	 * 高值耗材-通过材料编码查询高值耗材对应收费项目
	 * @return
	 */
	public String qryHightItemBybarcodeHrp(String barcode){
        //1.创建hrp接口调用请求数据
		Map<String, Object> requConsumablesMap = new HashMap<String, Object>();
		requConsumablesMap.put("HRPBARCODE", barcode);
		requConsumablesMap.put("REUESTTIME", DateUtils.getDate());
		//根据接口反馈编码查询对应收费项目
		StringBuffer sql=new StringBuffer();
		sql.append("select pk_item from  bd_item where code=? ");
		//跟传入物品编码调用hrp查询接口
		//PurchaseByCode:hrp查询接口
		HighValueHRPVo highValueHRPVo = doPost(JsonUtil.writeValueAsString(requConsumablesMap),"PurchaseByCode");
		if(isNotNull(highValueHRPVo)){
			if("SUCCESS".equals(highValueHRPVo.getCode())){
	        	if(highValueHRPVo.getData().size()>0){
	        		List<Map<String,Object>> resList= DataBaseHelper.queryForList(sql.toString(), new Object[]{highValueHRPVo.getData().get(0).getMATERIALNUMBER()});
	        		//List<Map<String,Object>> resList= DataBaseHelper.queryForList(sql.toString(), new Object[]{"n0818003057"});
	        		if(resList==null || resList.size()==0 ||resList.get(0)==null || resList.get(0).get("pkItem")==null){
	        			throw new BusException("未获取到对应材料条码【"+barcode+"】的收费项目，请联系管理员进行核对！");
	        		}
	        		if(resList.size()!=1){
	        			throw new BusException("获取到对应材料条码【"+barcode+"】的收费项目存在多条，请联系管理员进行核对！");
	        		}
	        		return resList.get(0).get("pkItem").toString();
	        	}else{
	        		throw new BusException("为获取到【"+barcode+"】的相关收费信息");
	        	}
	        }else{
	        	throw new BusException("HRP材料查询结果："+highValueHRPVo.getMessage());
	        }
        }else{
        	throw new BusException("HRP材料查询结果为空！！！");
        }
		
	}

	/**
	 * 高值耗材记费-外部HRP接口
	 * @param ipdtList
	 */
	public void saveConsumableHrp(List<BlIpDt> ipdtList,List<String> barcodes){
		List<RequConsumablesVo> RequConsList = new ArrayList<>();
		User user=UserContext.getUser();
		for (BlIpDt ipdt:ipdtList){
			for(String barc: barcodes){
				if(ipdt.getBarcode().equals(barc)){
					RequConsumablesVo requConsumablesVo = new RequConsumablesVo();
					requConsumablesVo.setHRPBARCODE(ipdt.getBarcode());//HRP条形码
					requConsumablesVo.setHISBARCODE(ipdt.getBarcode());//HIS条形码
					requConsumablesVo.setCHARGEAMOUNT(ipdt.getPrice());//HIS收费金额
					requConsumablesVo.setCHARGEDATE(ipdt.getDateHap());//HIS收费日期
					requConsumablesVo.setFHISMATNUMBER(ipdt.getCodeCg());//HIS物料编码,收费项目编码
					requConsumablesVo.setFHISMATNAME(ipdt.getNameCg());//HIS物料名称
					requConsumablesVo.setFHISSUPNUMBER("");//HIS供应商
					requConsumablesVo.setFHISSUPNAME("");//HIS供应商名称
					//查询记费科室
					Map<String, Object> DeptCgMap = getDeptInfoByPkDept(ipdt.getPkDeptCg());
					requConsumablesVo.setFHISDEPNUMBER(getPropValueStr(DeptCgMap,"codeDept"));//HIS部门编码
					requConsumablesVo.setFHISDEPNAME(getPropValueStr(DeptCgMap,"nameDept"));//HIS部门名称
					//查询记费科室
					Map<String, Object> DeptAppMap = getDeptInfoByPkDept(ipdt.getPkDeptApp());
					requConsumablesVo.setHISAPPLYDEPTNUMBER(getPropValueStr(DeptAppMap,"codeDept"));//HIS申请部门编码
					requConsumablesVo.setHISAPPLYDEPTNAME(getPropValueStr(DeptAppMap,"nameDept"));//HIS申请部门名称
					
					//查询患者信息
					String sql="select code_pi,name_pi,code_ip from pi_master where pk_pi=?";
			    	Map<String,Object> piMasterMap=DataBaseHelper.queryForMap(sql, ipdt.getPkPi());
					
					requConsumablesVo.setPATIENTNUMBER(getPropValueStr(piMasterMap,"codePi"));//患者编码
					requConsumablesVo.setPATIENTNAME(getPropValueStr(piMasterMap,"namePi"));//患者姓名
					requConsumablesVo.setOUTPATIENTNUMBER(getPropValueStr(piMasterMap,"codeIp"));//门诊号/住院号
					requConsumablesVo.setREGISTRATIONNO("");//注册证号
					requConsumablesVo.setMANUFACTURER("");//生产商

					RequConsList.add(requConsumablesVo);
				}
			}
		}
		//list<B>转json ,key转大写。
		String RequCons="{\"data\":"+JsonUtil.writeValueAsString(RequConsList,"yyyy-MM-dd HH:mm:ss").toUpperCase()+"}";
		logger.info("高值耗材记费:{}",RequCons);
		HighValueHRPVo highValueHRPVo = doPost(RequCons,"HisPayment");
		logger.info("高值耗材记费响应:{}",highValueHRPVo.getMessage());
		if(isNotNull(highValueHRPVo)){
			if(!"SUCCESS".equals(highValueHRPVo.getCode())){
	        	throw new BusException("HRP材料记费结果："+highValueHRPVo.getMessage());
			}
        }else{
        	throw new BusException("HRP材料记费结果为空！！！");
        }
	}
	/**
	 * 高值耗材退费费-外部HRP接口
	 */
	public void savaReturnConsumableHrp(BlPubReturnVo blretvo){
		List<RequConsumablesQuerVo> countList = new ArrayList<>();
		//判断住院收费明细
		if(null != blretvo.getBids() && blretvo.getBids().size()>0){
			for(BlIpDt blIpDt :blretvo.getBids()){
				if(StringUtils.isNotEmpty(blIpDt.getBarcode())){
					RequConsumablesQuerVo requConsumablesVo = new RequConsumablesQuerVo();
					requConsumablesVo.setHRPBARCODE(blIpDt.getBarcode());
					requConsumablesVo.setREUESTTIME(new Date());
					countList.add(requConsumablesVo);
					String RequCons="{\"data\":"+JsonUtil.writeValueAsString(countList,"yyyy-MM-dd HH:mm:ss").toUpperCase()+"}";
					HighValueHRPVo highValueHRPVo = doPost(RequCons,"HisRefund");
			        if(isNotNull(highValueHRPVo)){
			        	if(!"SUCCESS".equals(highValueHRPVo.getCode())){
				        	throw new BusException("HRP材料退费结果："+highValueHRPVo.getMessage());
						}
			        }else{
			        	logger.info("高值耗材退费异常入参：{}",RequCons);
			        	throw new BusException("HRP材料退费结果为空！！！");
			        }
				}
			}
		}
		//判断门诊收费明细
		if(null != blretvo.getBods() &&  blretvo.getBods().size()>0){
			for(BlOpDt blOpDt :blretvo.getBods()){
				if(StringUtils.isNotEmpty(blOpDt.getBarcode())){
					RequConsumablesQuerVo requConsumablesVo = new RequConsumablesQuerVo();
					requConsumablesVo.setHRPBARCODE(blOpDt.getBarcode());
					requConsumablesVo.setREUESTTIME(new Date());
					countList.add(requConsumablesVo);
					String RequCons="{\"data\":"+JsonUtil.writeValueAsString(countList,"yyyy-MM-dd HH:mm:ss").toUpperCase()+"}";
					HighValueHRPVo highValueHRPVo = doPost(RequCons,"HisRefund");
					if(isNotNull(highValueHRPVo)){
			        	if(!"SUCCESS".equals(highValueHRPVo.getCode())){
				        	throw new BusException("HRP材料退费结果："+highValueHRPVo.getMessage());
						}
			        }else{
			        	logger.info("高值耗材退费异常入参：{}",RequCons);
			        	throw new BusException("HRP材料退费结果为空！！！");
			        }
				}
			}
		}
	}
	/**
	 *
	 * @param mobiles json数据
	 * @param method 方法名称
	 * @return
	 */
	public HighValueHRPVo doPost(String mobiles,String method) {
		//请求地址
		StringBuffer url = new StringBuffer();
		url.append(ApplicationUtils.getPropertyValue("ext.system.address", "http://192.168.44.146:8012/api/By/"));
		url.append(method);
		RestTemplate Temp = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		requestHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> requestEntity = new HttpEntity<String>(mobiles, requestHeaders);
		String result=null;
		logger.info("高值耗材doPost()请求前{}",mobiles);
		try {
			result = Temp.postForObject(url.toString(), requestEntity, String.class);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("高值耗材doPost()异常信息：{}",e.getMessage());
		}
		logger.info("高值耗材doPost{}响应后{}",result);
		if(StringUtils.isEmpty(result)){
			throw new BusException("HRP接口响应结果为空！！！");
		}
		HighValueHRPVo highValueHRPVo = JSONObject.parseObject(result, HighValueHRPVo.class);	
		return highValueHRPVo;
	}
	
	/**
     * 根据科室主键查询编码和名称
     * @param pkDept
     * @return map{codeDept,nameDept}
     */
    public Map<String,Object> getDeptInfoByPkDept(String pkDept){
    	String sql="select code_dept,name_dept from bd_ou_dept where pk_dept=?";
    	Map<String,Object> map=DataBaseHelper.queryForMap(sql, pkDept);
    	return map;
    }
    
	/**
	 * 取文本内容
	 * @param map
	 * @return
	 */
	public static String getPropValueStr(Map<String, Object> map,String key) {
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}
	
	public static boolean isNotNull(Object obj){
		if(obj==null){
			return false;
		}
	    try {
	        for (Field f : obj.getClass().getDeclaredFields()) {
	            f.setAccessible(true);
	            if (f.get(obj) != null) {
	                return true;
	            }
	        }
	    }catch (IllegalAccessException e){

	    }
	    return false;
	}
}
