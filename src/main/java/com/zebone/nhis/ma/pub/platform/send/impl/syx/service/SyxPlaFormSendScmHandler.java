package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSendScmMapper;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.ConsisPrescDtlvw;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.ConsisPrescMstvw;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.DataPrescription;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.DataPrescriptionDetail;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.DcDictDrug;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.ReqSubject;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.RequestHrVo;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.ResResult;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.Response;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.ResponseHrVo;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.Sender;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 发送scm领域下的内容
 * @author jd
 *
 */
@Service
public class SyxPlaFormSendScmHandler {
	private Logger logger = LoggerFactory.getLogger("syx.hrSyxConsis");
	
	@Resource
	private SyxPlatFormSendScmMapper sendScmMapper;
	/**
	 * 处方数据上传接口（his作为调用方推送处方信息）
	 * @param paramMap{pkPreses:"处方主键集合"}
	 */
	@SuppressWarnings("unchecked")
	public void sendHrPresInfo(Map<String, Object> paramMap){
		long timeStart=new Date().getTime();
		String reqxml="";
		String resXml="";
		try {
			logger.info("\n\n*******************************************************************************************************************************************");
			logger.info("====================华润包药机处方接口上传开始==============================================================");
			HIPMessageServerUtils hiputil=new HIPMessageServerUtils();
			RequestHrVo reqHrvo=new RequestHrVo();
			List<String> pkPreses=(List<String>) paramMap.get("pkPresocces");
			Set<String> pkPresocces=new HashSet<String>(pkPreses);
			
			if(pkPreses==null|| pkPreses.size()<=0){
				logger.info("====================华润包药机处方上传【201】接口：状态【失败】，失败原因：未获得处方数据，时间【"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"】====================");
			}
			//构建请求参数
			List<ConsisPrescMstvw> presList=sendScmMapper.qryPresInfo(pkPreses);
			List<ConsisPrescDtlvw> presDtList=sendScmMapper.qryPresDtInfo(pkPreses);
			reqHrvo.setOpsystem("HIS");
			reqHrvo.setOptype("201");
			reqHrvo.setOpwinid("");
			reqHrvo.setOpip("");
			reqHrvo.setOpmanno(UserContext.getUser().getCodeEmp());
			reqHrvo.setOpmanname(UserContext.getUser().getNameEmp());
			Integer itemNo=1;
			for (ConsisPrescMstvw conPres : presList) {
				List<ConsisPrescDtlvw> consdts=conPres.getConsisPrescDtlvw();
				for (ConsisPrescDtlvw presDt : presDtList) {
					if(presDt.getPkPresocc().equals(conPres.getPkPresocc())){
						presDt.setItemNo(itemNo.toString());
						consdts.add(presDt);
					}
				}
				itemNo++;
			}
			reqHrvo.setPrescMstvws(presList);
			reqxml=XmlUtil.beanToXml(reqHrvo, RequestHrVo.class);
			resXml=hiputil.sendPrivateHIPService("HisTransData", reqxml);
			ResponseHrVo resHrvo=(ResponseHrVo) XmlUtil.XmlToBean(resXml, ResponseHrVo.class);
			if("1".equals(resHrvo.getRetcode())){//成功
				StringBuffer sql=new StringBuffer();
				sql.append("UPDATE ex_pres_occ pres ");
				sql.append(" SET pres.date_reg =?, pres.flag_reg='1',pres.flag_prep='1',pres.winno_conf=?");
				sql.append(" WHERE pres.pk_presocc in ("+CommonUtils.convertSetToSqlInPart(pkPresocces, "pk_presocc")+") AND pres.flag_cg='1' AND pres.flag_reg='0' AND pres.flag_canc='0'");
				
				DataBaseHelper.update(sql.toString(), new Object[]{new Date(),resHrvo.getRetmsg()});
			}else{
				logger.info("====================华润包药机处方上传【201】接口：状态【失败】，失败原因：【"+resHrvo.getRetmsg()+"】,处方执行主键信息【"+pkPreses.toString()+"】====================");
			}
		} catch (Exception e) {
			logger.info("====================华润包药机处方上传【201】接口：状态【失败】，失败原因：his代码逻辑处理异常，详细：【"+e.getMessage()+"】====================");

		}finally{
			long timeEnd=new Date().getTime();
			logger.info("====================请求的消息：\n"+reqxml);
			logger.info("====================响应的消息：\n"+resXml);
			logger.info("====================华润包药机处方接口上传结束：共耗时："+(timeEnd-timeStart)+"ms==============================================================");
		}
		
	}

	/**
	 * 处方数据上传,调用平台处方更新/新增操作
	 * @param paramMap
	 */
	@SuppressWarnings("unchecked")
	public void sendPtPresInfo(Map<String, Object> paramMap){
		long timeStart=new Date().getTime();
		String reqxml="";
		String resXml="";
		try {
			logger.info("\n\n\n*******************************************************************************************************************************************");
			logger.info("==================== HIP平台数据上传【S160】接口开始==============================================================");
			HIPMessageServerUtils hiputil=new HIPMessageServerUtils();
			Request reqvo=new Request();
			List<String> pkPreses=(List<String>) paramMap.get("pkPresocces");
			
			if(pkPreses==null|| pkPreses.size()<=0){
				logger.info("====================HIP平台处方数据上传【S160】接口：未获得处方数据，时间【"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss")+"】====================");
			}
			User user=UserContext.getUser();
			List<DataPrescription> presList=sendScmMapper.upPresInfoToHip(pkPreses);
			List<DataPrescriptionDetail> presDtList=sendScmMapper.upPresDtInfoToHip(pkPreses);
			reqvo.setActionId(NHISUUID.getKeyId());
			reqvo.setCreateTime(DateUtils.getDateTimeStr(new Date()));
			reqvo.setId(NHISUUID.getKeyId());
			reqvo.setActionId("PrescriptionAddAndUpdate");
			reqvo.setActionName("处方注册更新服务");
			Sender sender=reqvo.getSender();
			sender.setSystemId("HIS");
			sender.setSystemName("医嘱系统");
			sender.setSenderId(user.getCodeEmp());
			sender.setSendername(user.getNameEmp());
			ReqSubject subject=reqvo.getSubject();
			subject.setAction("0");
			for (DataPrescription dataPres : presList) {
				List<DataPrescriptionDetail> presdts=dataPres.getDataPrescriptionDetail();
				for (DataPrescriptionDetail presDt : presDtList) {
					if(presDt.getPkPresocc().equals(dataPres.getPkPresocc())){
						presdts.add(presDt);
					}
				}
			}
			subject.setDataPrescription(presList);
			reqxml=XmlUtil.beanToXml(reqvo, Request.class);
			resXml=hiputil.sendPrivateHIPService("PrescriptionAddAndUpdate", reqxml);
			Response resvo=(Response) XmlUtil.XmlToBean(resXml, Response.class);
			ResResult result=resvo.getResult();
			if("AE".equals(result.getId())){//上传处方数据失败
				logger.info("====================HIP平台数据上传【S160】接口：状态【失败】，请求消息Id:"+result.getRequestId()+"，请求时间："+result.getRequestTime()+"失败原因："+result.getText()+",上传处方执行主键："+pkPreses.toString());
			}
		} catch (Exception e) {
			logger.info("====================HIP平台处方数据上传【S160】接口：his代码逻辑处理异常，异常原因："+e.getMessage());
		}finally{
			long timeEnd=new Date().getTime();
			logger.info("====================请求的消息：\n"+reqxml);
			logger.info("====================响应的消息：\n"+resXml);
			logger.info("====================HIP平台数据上传【S160】接口结束：共耗时："+(timeEnd-timeStart)+"ms==============================================================");
		}
	}
	
	/**
	 * 010005002014
	 * 开始发药调用包药机数据接口
	 * @param paramMap
	 */
	@SuppressWarnings("unchecked")
	public void sendHrPresStart(String param,IUser user){
		long timeStart=new Date().getTime();
		String reqxml="";
		String resXml="";
		try {
			logger.info("\n\n\n*******************************************************************************************************************************************");
			logger.info("==================== 华润包药机【开始发药202】接口开始==============================================================");
			Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
			if(paramMap==null){
				throw new BusException("未获取到处方上传数据！");
			}
			HIPMessageServerUtils hiputil=new HIPMessageServerUtils();
			User douser=(User)user;
			List<ConsisPrescMstvw> presList=sendScmMapper.qryStartPresInfo(paramMap);
			RequestHrVo reqvo=new RequestHrVo();
			reqvo.setOpsystem("HIS");
			reqvo.setOpwinid(paramMap.get("winno").toString());
			reqvo.setOptype("202");
			reqvo.setOpip("");
			reqvo.setOpmanno(douser.getCodeEmp());
			reqvo.setOpmanname(douser.getNameEmp());
			reqvo.setPrescMstvws(presList);
			reqxml=XmlUtil.beanToXml(reqvo, RequestHrVo.class);
			resXml=hiputil.sendPrivateHIPService("HisTransData", reqxml);
			ResponseHrVo resHrvo=(ResponseHrVo) XmlUtil.XmlToBean(resXml, ResponseHrVo.class);
			if(!"1".equals(resHrvo.getRetcode())){
				String messsage="开始发药失败,请进行核对：失败原因：【"+resHrvo.getRetmsg()+"】";
				throw new BusException(messsage);
			}
		} 
		finally{
			long timeEnd=new Date().getTime();
			logger.info("====================请求的消息：\n"+reqxml);
			logger.info("====================响应的消息：\n"+resXml);
			logger.info("====================华润包药机【开始发药202】接口结束：共耗时："+(timeEnd-timeStart)+"ms==============================================================");
		}
	}
	
	/**
	 * 结束发药调用包药机数据接口
	 * @param paramMap{"winno":"发药窗口号"，"pkPresocces":[处方执行主键集合]}
	 */
	@SuppressWarnings("unchecked")
	public void sendHrPresEnd(Map<String, Object> paramMap){
		long timeStart=new Date().getTime();
		String reqxml="";
		String resXml="";
		try {
			logger.info("\n\n\n*******************************************************************************************************************************************");
			logger.info("==================== 华润包药机接口【发药完成203】接口开始==============================================================");
			if(paramMap==null){
				throw new BusException("未获取到处方上传数据！");
			}
			HIPMessageServerUtils hiputil=new HIPMessageServerUtils();
			User douser=UserContext.getUser();
			List<String> pkPresoccList=new ArrayList<String>();
			pkPresoccList.add(paramMap.get("pkPresocc").toString());
			paramMap.put("pkPresocces", pkPresoccList);
			List<ConsisPrescMstvw> presList=sendScmMapper.qryStartPresInfo(paramMap);
			RequestHrVo reqvo=new RequestHrVo();
			reqvo.setOpsystem("HIS");
			reqvo.setOpwinid(paramMap.get("winno").toString());
			reqvo.setOptype("203");
			reqvo.setOpip("");
			reqvo.setOpmanno(douser.getCodeEmp());
			reqvo.setOpmanname(douser.getNameEmp());
			reqvo.setPrescMstvws(presList);
			reqxml=XmlUtil.beanToXml(reqvo, RequestHrVo.class);
			resXml=hiputil.sendPrivateHIPService("HisTransData", reqxml);
			ResponseHrVo resHrvo=(ResponseHrVo) XmlUtil.XmlToBean(resXml, ResponseHrVo.class);
			if(!"1".equals(resHrvo.getRetcode())){
				List<String> pkPresocces=(List<String>) paramMap.get("pkPresocces");
				String messsage="状态【失败】,失败原因：【"+resHrvo.getRetmsg()+"】,上传处方执行主键：【"+pkPresocces.toString()+"】,发药窗口："+paramMap.get("winno").toString();
				throw new BusException(messsage);
			}
		} catch (Exception e) {
			logger.info("====================华润包药机接口【发药完成203】："+e.getMessage());
		}finally{
			long timeEnd=new Date().getTime();
			logger.info("====================请求的消息：\n"+reqxml);
			logger.info("====================响应的消息：\n"+resXml);
			logger.info("====================华润包药机接口【发药完成203】接口结束：共耗时："+(timeEnd-timeStart)+"ms==============================================================");
		}
	}
	
	/**
	 * 205 处方退费接口
	 * @param paramMap
	 */
	public void sendHrReturnDrugCgInfo(Map<String,Object> paramMap){
		if(paramMap==null)return;
		long timeStart=new Date().getTime();
		String reqxml="";
		String resXml="";
		try {
			logger.info("\n\n\n*******************************************************************************************************************************************");
			logger.info("==================== 华润包药机处方退费接口（全退）【205】接口开始==============================================================");
			List<ConsisPrescMstvw> presList=sendScmMapper.qryReturnPresInfo(paramMap);
			if(presList==null ||presList.size()<=0)return;
			List<String> upPresNos=new ArrayList<String>();
			for (ConsisPrescMstvw pres : presList) {
				upPresNos.add(pres.getPrescNo());
			}
			HIPMessageServerUtils hiputil=new HIPMessageServerUtils();
			User douser=UserContext.getUser();
			RequestHrVo reqvo=new RequestHrVo();
			reqvo.setOpsystem("HIS");
			reqvo.setOpwinid("");
			reqvo.setOptype("205");
			reqvo.setOpip("");
			reqvo.setOpmanno(douser.getCodeEmp());
			reqvo.setOpmanname(douser.getNameEmp());
			reqvo.setPrescMstvws(presList);
			reqxml=XmlUtil.beanToXml(reqvo, RequestHrVo.class);
			resXml=hiputil.sendPrivateHIPService("HisTransData", reqxml);
			ResponseHrVo resHrvo=(ResponseHrVo) XmlUtil.XmlToBean(resXml, ResponseHrVo.class);
			if("1".equals(resHrvo.getRetcode())){//成功
				logger.info("====================华润包药机处方退费接口（全退）【205】：状态【成功】，返回信息：【"+resHrvo.getRetmsg()+"】,HIS上传处方信息："+upPresNos.toString()+"");
			}else{//失败
				logger.info("====================华润包药机处方退费接口（全退）【205】：状态【失败】，返回信息：【"+resHrvo.getRetmsg()+"】,HIS上传处方信息："+upPresNos.toString());
			}
		} catch (Exception e) {
			logger.info("====================华润包药机处方退费接口（全退）【205】:状态，HIS处理逻辑失败，异常信息："+e.getMessage());
		}finally{
			long timeEnd=new Date().getTime();
			logger.info("====================请求的消息：\n"+reqxml);
			logger.info("====================响应的消息：\n"+resXml);
			logger.info("====================华润包药机处方退费接口（全退）【205】接口结束：共耗时："+(timeEnd-timeStart)+"ms==============================================================");
		}
	}
	
	/**
	 * 上传药品字典信息
	 * @param paramMap
	 */
	public void sendPdDictMessage(Map<String,Object> paramMap){
		long timeStart=new Date().getTime();
		if(paramMap==null)return;
		List<DcDictDrug> pdList=sendScmMapper.qryPdDictInfo(paramMap);
		if(pdList==null || pdList.size()<=0)return;
		HIPMessageServerUtils hiputil=new HIPMessageServerUtils();
		Request reqvo=new Request();
		User user=UserContext.getUser();
		String reqxml="";
		String resXml="";
		try {
			logger.info("\n\n\n*******************************************************************************************************************************************");
			logger.info("==================== HIP平台药品字典注册/更新数据上传【S162】接口开始==============================================================");
			reqvo.setActionId(NHISUUID.getKeyId());
			reqvo.setCreateTime(DateUtils.getDateTimeStr(new Date()));
			reqvo.setId(NHISUUID.getKeyId());
			reqvo.setActionId("DrugDictAddAndUpdate");
			reqvo.setActionName("药品字典注册更新服务");
			Sender sender=reqvo.getSender();
			sender.setSystemId("HIS");
			sender.setSystemName("医嘱系统");
			sender.setSenderId(user.getCodeEmp());
			sender.setSendername(user.getNameEmp());
			ReqSubject subject=reqvo.getSubject();
			if("_UPDATE".equals(paramMap.get("STATUS"))){
			subject.setAction("1");
			}else{
				subject.setAction("0");
			}
			subject.setDcDictDrug(pdList.get(0));
			
			reqxml=XmlUtil.beanToXml(reqvo, Request.class);
			resXml=hiputil.sendPrivateHIPService("DrugDictAddAndUpdate", reqxml);
			Response resvo=(Response) XmlUtil.XmlToBean(resXml, Response.class);
			ResResult result=resvo.getResult();
			if("AE".equals(result.getId())){//上传药品数据失败
				logger.info("====================HIP平台药品字典注册/更新数据上传【S162】接口：状态【失败】，请求消息Id:"+result.getRequestId()+"，请求时间："+result.getRequestTime()+"失败原因："+result.getText()+",药品主键："+paramMap.get("pkPd"));
			}
		} catch (Exception e) {
			logger.info("====================HIP平台药品字典注册/更新数据上传【S162】接口：状态【失败】，HIS业务处理失败，异常原因：【"+e.getMessage()+"】,药品主键："+paramMap.get("pkPd"));
		}finally{
			long timeEnd=new Date().getTime();
			logger.info("====================请求的消息：\n"+reqxml);
			logger.info("====================响应的消息：\n"+resXml);
			logger.info("====================HIP平台数据上传【S162】接口结束：共耗时："+(timeEnd-timeStart)+"ms==============================================================");
		}
	}
}
