package com.zebone.nhis.task.scm.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.task.scm.dao.ScmOpDrugpackMapper;
import com.zebone.nhis.task.scm.vo.OpDrugDispensePresInfo;
import com.zebone.nhis.task.scm.vo.OpDrugDispensePresReq;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 门诊发药机重发处理：根据定时任务设置
 * @author jd_em
 *
 */
@Service
public class ScmOpDrugpackTaskService {
	
	private  static final String URL_REQUEST=ApplicationUtils.getPropertyValue("scm.opdt.packmachine.webservice.url", "http://10.0.3.178:8033/HospitalService_mz.asmx");
	private static Logger log = LoggerFactory.getLogger("nhis.iron");

	@Resource
	private ScmOpDrugpackMapper scmOpDrugpackMapper;
	
	public void sendOpdrugPresinfoTask(QrtzJobCfg cfg){
		upLoadPresInfo();
	}
	
	public void upLoadPresInfo() {
		Date datenow=new Date();
		String dateEnd=DateUtils.dateToStr("yyyyMMddHHmmss", datenow);
        String dateStart=DateUtils.addDate(datenow, -60, 5, "yyyyMMddHHmmss");
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
		List<OpDrugDispensePresInfo> presList=scmOpDrugpackMapper.queryPresInfoUpload(paramMap);
		if(presList==null ||presList.size()==0)return;
		
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
					String sql="update EX_PRES_OCC set WINNO_CONF=?,note='iron' where PRES_NO in ("+CommonUtils.convertListToSqlInPart(presNos)+")";
					DataBaseHelper.execute(sql, new Object[]{result});
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
	 * 通过Http post 方式调用web服务
	 * @param param 参数名称
	 * @param method 方法名
	 * @param message 消息
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
   private String postHttp(String param,String method,String message) throws Exception {
	        String responseMsg = "<string>1</string>";
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
