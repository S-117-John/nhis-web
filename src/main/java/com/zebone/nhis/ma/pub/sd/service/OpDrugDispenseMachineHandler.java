package com.zebone.nhis.ma.pub.sd.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jasig.cas.client.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.sd.dao.OpDrugDispenseMachineMapper;
import com.zebone.nhis.ma.pub.sd.vo.OpDrugBaseStoreReq;
import com.zebone.nhis.ma.pub.sd.vo.OpDrugDispensePresInfo;
import com.zebone.nhis.ma.pub.sd.vo.OpDrugDispensePresReq;
import com.zebone.nhis.ma.pub.sd.vo.OpPdBaseStoreInfo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;


/**
 * 深大门诊发药机调用第三方服务接口
 * @author jd_em
 *
 */
@Service
public class OpDrugDispenseMachineHandler {
	
	@Resource
	private OpDrugDispenseMachineMapper dispenseMachineMapper;
	
	@Resource
	private OpDrugDispenseMachineService dispenseMachineService;
	
	private  static final String URL_REQUEST=ApplicationUtils.getPropertyValue("scm.opdt.packmachine.webservice.url", "http://10.0.3.178:8033/HospitalService_mz.asmx");
	
	
	private static Logger log = LoggerFactory.getLogger("nhis.iron");
	
	/**
	 * 接口服务调用统一接口
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception 
	 */
	public Object invokeMethod(String methodName,Object...args) {
		Object obj=new Object();
		String winType = ApplicationUtils.getDeptSysparam("EX0055", UserContext.getUser().getPkDept());
		if(!"1".equals(winType)){
			return obj;
		}
		if(CommonUtils.isNull(URL_REQUEST)){
			return obj;
		}
	    switch(methodName){
	    	case "upLoadPresInfo":
				this.upLoadPresInfo(args);
	    		break;
	    	case "startDoDrug":
	    		this.getDrugBoxInfo(args);
	    		break;
	    	case "confirmPres":
	    		this.execDrugPresInfo(args);
	    		break;
	    	case "getMedInfo":
	    		this.upLoadDrugBaseInfo(args);
	    		break;
	    	default:
	    		break;
	    	} 
    	return obj;
	}
	
	
	/**
	 * 门诊结算处方数据上传
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void upLoadPresInfo(Object... args) {
		if(args==null)return;
		List<String> pkPresocces=(List<String>) args[0];
		if(pkPresocces==null ||pkPresocces.size()==0)return;

		Map<String,Object> qryParam=new HashMap<String,Object>();
		qryParam.put("pkPresocces", pkPresocces);
		String pkStoreTxt=ApplicationUtils.getPropertyValue("scm.opdt.packmachine.store.value", "c23d472cf0884b119e4d718af0fb7f45");
		List<String> pkStoreList=new ArrayList<>();
		
		if(CommonUtils.isNotNull(pkStoreTxt)){
			String [] str=pkStoreTxt.split(",");
			for (int i = 0; i < str.length; i++) {
				pkStoreList.add(str[i]);
			}
			qryParam.put("pkStoreList", pkStoreList);
		}
		List<OpDrugDispensePresInfo> presList=dispenseMachineMapper.queryPresInfoUpload(qryParam);
		List<String> presNos = new ArrayList<String>();
		for (OpDrugDispensePresInfo OpDrug : presList) {

			BigDecimal dosageOld = new BigDecimal(OpDrug.getDosage());
			dosageOld=getNumberForZero(dosageOld);
			OpDrug.setMedPerDos("每次"+dosageOld + OpDrug.getNameUnitDos());
			
			BigDecimal dosage = new BigDecimal(0);
			System.out.println("药品名称：" + OpDrug.getMedName() + "标签打印单位:" + ("1".equals(OpDrug.getEuLabeltype()) ? "基本单位" : "医疗单位")  + "-------"
					+"开立的用量单位"+ 
					(OpDrug.getPkUnitDos().equals(OpDrug.getPkUnitMin()) ? "基本单位：" + OpDrug.getNameUnitMin() : 
						(OpDrug.getPkUnitDos().equals(OpDrug.getPkUnitVol()) ? "医疗单位1：" + OpDrug.getNameUnitVol()  : "医疗单位2：" +  OpDrug.getNameUnitWt()))
					+ "转换前的用量：" + OpDrug.getMedPerDos());
			if(EnumerateParameter.ZERO.equals(OpDrug.getEuLabeltype()) && OpDrug.getPkUnitDos().equals(OpDrug.getPkUnitMin())){
				dosage = new BigDecimal((MathUtils.mul(OpDrug.getDosage(), OpDrug.getVol()))).setScale(4, BigDecimal.ROUND_HALF_UP);
				dosage=getNumberForZero(dosage);
				OpDrug.setMedPerDos("每次"+dosage + OpDrug.getNameUnitVol());
			}
			if(EnumerateParameter.ONE.equals(OpDrug.getEuLabeltype()) && OpDrug.getPkUnitDos().equals(OpDrug.getPkUnitVol())){
				dosage = new BigDecimal((MathUtils.div(OpDrug.getDosage(), OpDrug.getVol()))).setScale(4, BigDecimal.ROUND_HALF_UP);
				dosage=getNumberForZero(dosage);
				OpDrug.setMedPerDos("每次"+dosage + OpDrug.getNameUnitMin());
			}
			if(EnumerateParameter.ONE.equals(OpDrug.getEuLabeltype()) && OpDrug.getPkUnitDos().equals(OpDrug.getPkUnitWt())){
				dosage = new BigDecimal((MathUtils.div(OpDrug.getDosage(), OpDrug.getWeight()))).setScale(4, BigDecimal.ROUND_HALF_UP);
				dosage=getNumberForZero(dosage);
				OpDrug.setMedPerDos("每次"+dosage + OpDrug.getNameUnitWt());
			}
			System.out.println("药品名称：" + OpDrug.getMedName() + "标签打印单位:" + ("1".equals(OpDrug.getEuLabeltype()) ? "基本单位" : "医疗单位")  + "-------"
					+"开立的用量单位"+ 
					(OpDrug.getPkUnitDos().equals(OpDrug.getPkUnitMin()) ? "基本单位：" + OpDrug.getNameUnitMin() : 
						(OpDrug.getPkUnitDos().equals(OpDrug.getPkUnitVol()) ? "医疗单位1：" + OpDrug.getNameUnitVol()  : "医疗单位2：" +  OpDrug.getNameUnitWt()))
					+ "转换后的用量：" + OpDrug.getMedPerDos());
			presNos.add(OpDrug.getPresNo());
		}
		OpDrugDispensePresReq reqvo=new OpDrugDispensePresReq();
		reqvo.setMed(presList);
		try{
			String reqXml= XmlUtil.beanToXml(reqvo, OpDrugDispensePresReq.class);
			System.out.println(reqXml);
			
			Object resMsg=postHttp("XMLPresInfo", "GetPresNoByXML", reqXml);
			if(resMsg!=null){
				String result=XmlUtils.getTextForElement(resMsg.toString(), "string");
				if(CommonUtils.isNotNull(result) && !"0".equals(result)){//数据上传成功，窗口号返回正常
					dispenseMachineService.updatePresWinno(presNos, result);
					log.info("门诊发药机-门诊结算处方数据上传:接口返回正常,请求入参\n"+reqXml);
					log.info("门诊发药机-门诊结算处方数据上传:接口返回正常,接口返回\n"+result);
				}else{
					log.error("门诊发药机-门诊结算处方数据上传:接口返回异常,请求入参\n"+reqXml);
					log.error("门诊发药机-门诊结算处方数据上传:接口返回异常,接口返回\n"+result);
				}
			}
		}catch(Exception e){
			log.error("门诊发药机-门诊结算处方数据上传：接口构建异常，异常原因,"+e.getLocalizedMessage());
		}
	} 

	/**
	 * 发药寻框操作
	 * @param args winno:"窗口号"， presNo:"处方号" 
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getDrugBoxInfo(Object... args) {
		if(args==null)return;
		Map<String,Object> paramMap=(Map<String, Object>) args[0];
		if(paramMap==null)return;
		if(paramMap.containsKey("winno")&& CommonUtils.isNotNull(paramMap.get("winno")) && paramMap.containsKey("presNoList")){
			List<String> presNoList=(List<String>) paramMap.get("presNoList");
			if(presNoList==null ||presNoList.size()==0)return;
			StringBuffer erroMsg=new StringBuffer();
			try{
				for (String presNo : presNoList) {
					Map<String,Object> params=new HashMap<String, Object>();
					params.put("PID", presNo);
					params.put("windowNo", paramMap.get("winno"));
					Object resMsg=postHttp(params, "GetBoxInfo");
					if(resMsg!=null){
						String result=XmlUtils.getTextForElement(resMsg.toString(), "string");
						if(CommonUtils.isNotNull(result) && "1".equals(result)){//接口调用成功
							log.info("门诊发药机-发药寻框操作:接口返回正常,请求入参\n pid={}&windowNo={}",presNo,paramMap.get("winno"));
							log.info("门诊发药机-发药寻框操作:接口返回正常,接口返回\n"+result);
						}else{
							erroMsg.append("处方号：");
							erroMsg.append(presNo);
							erroMsg.append("\n");
						}
					}
				}
			}catch(Exception e){
				log.error("门诊发药机-发药寻框操作:接口构建异常\n"+e.getLocalizedMessage());
			}
			/*if(CommonUtils.isNotNull(erroMsg)){
				
				throw new BusException(erroMsg.toString()+"药框闪烁灯指令接口调用失败！");
			}*/		
		}
		
	}
	
	/**
	 * 发药确认操作
	 * @param args winno:"窗口号"， presNo:"处方号" 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void execDrugPresInfo(Object ... args){
		if(args==null)return;
		Map<String,Object> paramMap=(Map<String, Object>) args[0];
		if(paramMap==null)return;
		if(paramMap.containsKey("winno")&& CommonUtils.isNotNull(paramMap.get("winno")) && paramMap.containsKey("pkPresocc") &&CommonUtils.isNotNull(paramMap.get("pkPresocc") )){
			Map<String,Object> presNo=DataBaseHelper.queryForMap("select pres_no from ex_pres_occ where pk_presocc=?", new Object[]{paramMap.get("pkPresocc")});
			if(presNo==null)return;
			Map<String,Object> postMap=new HashMap<String,Object>();
			postMap.put("PID", presNo.get("presNo"));
			postMap.put("windowNo", paramMap.get("winno"));
			try{
				Object resMsg=postHttp(postMap, "ConfirmPres");
				//Object resMsg=WSUtil.invoke(URL_REQUEST, "ConfirmPres", reqXml);
				if(resMsg!=null){
					String result=XmlUtils.getTextForElement(resMsg.toString(), "string");
					if(CommonUtils.isNotNull(result) && "1".equals(result)){//接口调用成功
						//log.info("门诊发药机-发药确认操作:接口返回正常,请求入参\n"+reqXml);
						log.info("门诊发药机-发药确认操作:接口返回正常,请求入参\n pid={}&windowNo={}",presNo,paramMap.get("winno"));
						log.info("门诊发药机-发药确认操作:接口返回正常,接口返回\n"+result);
					}else{
						log.error("门诊发药机-发药确认操作:接口返回正常,请求入参\n pid={}&windowNo={}",presNo,paramMap.get("winno"));
						log.error("门诊发药机-发药确认操作:接口返回异常,接口返回\n"+result);
					}
				}
			}catch(Exception e){
				log.error("门诊发药机-发药确认操作：接口构建异常，异常原因,"+e.getLocalizedMessage());
			}
		}
		
	}
	
	/**
	 * 仓库药品数据上传
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void upLoadDrugBaseInfo(Object ...args) {
		if(args==null)return;
		List<BdPdStore> pdStoList=(List<BdPdStore>) args[0];
		if(pdStoList==null || pdStoList.size()==0)return;
		List<String> pkPdstores=new ArrayList<String>();
		for (BdPdStore bdPdStore : pdStoList) {
			pkPdstores.add(bdPdStore.getPkPdstore());
		}
		String pkStoreTxt=ApplicationUtils.getPropertyValue("scm.opdt.packmachine.store.value", "");
		List<String> pkStoreList=new ArrayList<>();
		if(CommonUtils.isNotNull(pkStoreTxt)){
			String [] str=pkStoreTxt.split(",");
			for (int i = 0; i < str.length; i++) {
				pkStoreList.add(str[i]);
			}
		}
		Map<String,Object> qryMap=new HashMap<String,Object>();
		qryMap.put("pkPdstores", pkPdstores);
		qryMap.put("pkStores",pkStoreList);
		List<OpPdBaseStoreInfo> resList= dispenseMachineMapper.queryPdStoreInfo(qryMap);
		if(resList==null ||resList.size()==0)return;
		OpDrugBaseStoreReq reqvo=new OpDrugBaseStoreReq();
		reqvo.setStoreBaseInfo(resList);
		String reqXml="";
		try {
			reqXml=XmlUtil.beanToXml(reqvo, OpDrugBaseStoreReq.class);
			log.info(reqXml);
			String resMsg=postHttp("XMLMedInfo","GetMedInfo",reqXml);
			if(CommonUtils.isNotNull(resMsg)){
				String result=XmlUtils.getTextForElement(resMsg.toString(), "string");
				if(CommonUtils.isNotNull(result) && "1".equals(result)){//接口调用成功
					log.info("门诊发药机：【药品上传 GetMedInfo】入参："+reqXml);
					log.info("门诊发药机：【药品上传 GetMedInfo】调用成功\n"+resMsg);
				}else{
					log.error("门诊发药机：【药品上传  GetMedInfo】入参："+reqXml);
					log.error("门诊发药机：【药品上传 GetMedInfo】调用失败，返回内容:"+resMsg);
				}
			}else{
				log.error("门诊发药机：【药品上传  GetMedInfo】入参："+reqXml);
				log.error("门诊发药机：【药品上传  GetMedInfo】调用失败，无内容返回");
			}
		} catch (Exception e) {
			log.error("门诊发药机：【药品上传 GetMedInfo】构建失败，失败原因："+e.getMessage());
		}
		
	}
	
	/**
	 * 通过Http post 方式调用web服务
	 * @param param 参数名称
	 * @param method 方法名
	 * @param message 消息
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
   private String postHttp(String param,String method,String message) throws Exception {
	        String responseMsg = "";
	        HttpClient httpClient = new HttpClient();
	        httpClient.getParams().setContentCharset("utf-8");
	        PostMethod postMethod = new PostMethod(URL_REQUEST+"/"+method);
	        postMethod.addParameter(param, message);
	        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	        try {
	            httpClient.executeMethod(postMethod);
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            InputStream in = postMethod.getResponseBodyAsStream();
	            int len = 0;
	            byte[] buf = new byte[1024];
	            while((len=in.read(buf))!=-1){
	                out.write(buf, 0, len);
	            }
	            responseMsg = out.toString("UTF-8");
	        } finally {
	            postMethod.releaseConnection();
	        }
	        return responseMsg;
	    }
   
   /**
	 * 通过Http post 方式调用web服务
	 * @param paramMap 参数集合
	 * @param method 方法名
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
  private String postHttp(Map<String,Object> paramMap,String method) throws Exception {
	        String responseMsg = "";
	        HttpClient httpClient = new HttpClient();
	        httpClient.getParams().setContentCharset("utf-8");
	        PostMethod postMethod = new PostMethod(URL_REQUEST+"/"+method);
	        for (String key : paramMap.keySet()) {
	        	 postMethod.addParameter(key, paramMap.get(key).toString());
			}
	        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	        try {
	            httpClient.executeMethod(postMethod);
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            InputStream in = postMethod.getResponseBodyAsStream();
	            int len = 0;
	            byte[] buf = new byte[1024];
	            while((len=in.read(buf))!=-1){
	                out.write(buf, 0, len);
	            }
	            responseMsg = out.toString("UTF-8");
	        } finally {
	            postMethod.releaseConnection();
	        }
	        return responseMsg;
	    }
  
  	  /**
  	   * 用量舍零转化
  	   * @param dosage
  	   * @return
  	   */
  private  BigDecimal getNumberForZero(BigDecimal dosage){
		String resTxt="";
		if(dosage!=null ){
			String bigtxt=dosage.setScale(4,BigDecimal.ROUND_HALF_UP).toString();
			if(bigtxt.contains(".")){
				for (int i = bigtxt.length()-1; i>=0; i--) {
					char txt=bigtxt.charAt(i);
					if(!"0".equals(String.valueOf(txt))){
						resTxt=bigtxt.substring(0, i+1);
						break;
					}
				}
				if(resTxt.lastIndexOf(".")==resTxt.length()-1){

					resTxt=resTxt.replace(".", "");
				}
			}else{
				resTxt=bigtxt;
			}
		}
		
		return new BigDecimal(resTxt);
}
	
}
