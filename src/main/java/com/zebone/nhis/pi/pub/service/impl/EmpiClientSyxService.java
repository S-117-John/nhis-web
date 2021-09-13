package com.zebone.nhis.pi.pub.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxinmy.weixin4j.util.StringUtil;
import com.zebone.nhis.base.pub.service.BdPubService;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.XmlProcessUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.Address;
import com.zebone.nhis.ma.pub.platform.syx.vo.AsOtherIDs;
import com.zebone.nhis.ma.pub.platform.syx.vo.Author;
import com.zebone.nhis.ma.pub.platform.syx.vo.Card;
import com.zebone.nhis.ma.pub.platform.syx.vo.CardVo;
import com.zebone.nhis.ma.pub.platform.syx.vo.HipEmpi;
import com.zebone.nhis.ma.pub.platform.syx.vo.Item1;
import com.zebone.nhis.ma.pub.platform.syx.vo.Linkman;
import com.zebone.nhis.ma.pub.platform.syx.vo.MinimumDegreeMatch;
import com.zebone.nhis.ma.pub.platform.syx.vo.ParameterList;
import com.zebone.nhis.ma.pub.platform.syx.vo.Part;
import com.zebone.nhis.ma.pub.platform.syx.vo.Patient;
import com.zebone.nhis.ma.pub.platform.syx.vo.PatientEmpi;
import com.zebone.nhis.ma.pub.platform.syx.vo.PatientPerson;
import com.zebone.nhis.ma.pub.platform.syx.vo.PiMasterEmpiInfo;
import com.zebone.nhis.ma.pub.platform.syx.vo.RegistrationEvent;
import com.zebone.nhis.ma.pub.platform.syx.vo.RegistrationRequest;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.Response;
import com.zebone.nhis.ma.pub.platform.syx.vo.Subject;
import com.zebone.nhis.ma.pub.service.SysLogService;
import com.zebone.nhis.pi.pub.dao.EmpiClientSyxMapper;
import com.zebone.nhis.pi.pub.support.EmpiClientInterFace;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 主索引对接接口 （由于与集成平台使用的同一个webservice服务，因此此处直接使用ma下syx目录生成的平台服务接口）
 * @author yangxue
 *
 */
@Service("empiClientSyxService")
public class EmpiClientSyxService implements EmpiClientInterFace {
	@Autowired
	private  CommonService commonService;
	
	@Autowired
	private BdPubService bdPubService;
	
	@Autowired
	private EmpiClientSyxMapper empiClientSyxMapper;
	
	private Logger logger = LoggerFactory.getLogger("nhis.empi");
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdl = new SimpleDateFormat("yyyy-MM-dd");
	
	private HIPMessageServerUtils hipMessageServerUtils=new HIPMessageServerUtils();
	
	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date temp = null;
		if(!StringUtil.isEmpty(date)){
			try {
				temp = sdf.parse(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return temp;
	}
	/**
	 * 患者注册服务
	 */
	@Override
	public Object addEmpi(PiMaster piMaster) {
		String type = ApplicationUtils.getSysparam("PUB0001", false);
		if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&(type.indexOf("4")>-1)){
			try {
				if(queryEmpi(piMaster)==null){
					return	doAddEmpi(piMaster);
				}else{
					return updateEmpi(piMaster);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 患者注册服务-业务处理
	 * @param piMaster
	 * @return
	 */
	private Object doAddEmpi(PiMaster piMaster){
		String type = ApplicationUtils.getSysparam("PUB0001", false);
		if (!StringUtils.isNotBlank(type)||"0".equals(type)||(type.indexOf("4")<0))
			return null;
		String msgContext="";
		String msgType="";
		String msgId="";
		
		String req = null;//调用EMPI请求信息XML
		String res = null;//调用EMPI响应信息XML
		String mpi = piMaster.getMpi();
		Response resobj = null;//EMPI响应信息对象
		//调用EMPI保存服务
		List<Map<String, Object>> resList = null;
		
		req = convertPiToRequstXml(piMaster,"0","PRPA_IN201311UV02.xsd");
		System.err.println(req);
		res = hipMessageServerUtils.sendHIPService("PatientInfoRegister", req);
		System.err.println(res);
		if(StringUtils.isNoneBlank(res)&&res.indexOf("<HIP_EMPI>")>-1){
			resList = createResMapList(res);
		}else {
			resobj = XmlProcessUtils.toResponseEntity(res,"MCCI_IN000002UV01");
			
		}	
		if((resobj!=null)&& resobj.getAcknowledgement()!=null&&"AA".equals(resobj.getAcknowledgement().getTypeCode())){
			if(resobj.getAcknowledgement().getAcknowledgementDetail()!=null&&resobj.getAcknowledgement().getAcknowledgementDetail().getText()!=null)
				 mpi = resobj.getAcknowledgement().getAcknowledgementDetail().getText().getDescription().getValue();
		}else if (resList!=null) {
			
		}else {
			//消息发送失败，记录日志
			String errText="";
			if(resobj!=null && resobj.getAcknowledgement()!=null){
				errText=resobj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
				msgId=resobj.getAcknowledgement().getTargetMessage().getId().getExtension();
			}
			msgContext=hipMessageServerUtils.getSoapXML("PatientInfoRegister", req);
			SysLogService.saveSysMsgRec("send", msgContext, errText,msgType, msgId);
			return null;
		}
		
		//更新患者mpi字段
		if(CommonUtils.isNotEmpty(mpi))
			DataBaseHelper.update("update pi_master set mpi = ? where pk_pi = ?", new Object[]{mpi,piMaster.getPkPi()});
		return resList;
	}
	/**
	 * 更新患者信息服务
	 */
	@Override
	public Object updateEmpi(PiMaster piMaster) {
		String type = ApplicationUtils.getSysparam("PUB0001", false);
		if (!StringUtils.isNotBlank(type)||"0".equals(type)||(type.indexOf("4")<0))
			return null;
		try {
			String msgId= "";
			String msgContext= "";
			String msgType= "";
			String req = convertPiToRequstXml(piMaster,"1","PRPA_IN201314UV02.xsd");
			String res = hipMessageServerUtils.sendHIPService("PatientInfoUpdate", req);//调用EMPI更新服务
			System.err.println(req);
			System.err.println(res);
			Response resobj = null;
			List<Map<String, Object>> resList = null;
			String mpi = "";
			if(StringUtils.isNoneBlank(res) && res.indexOf("<HIP_EMPI>")>-1){
				resList = createResMapList(res);
			}else{
				resobj = XmlProcessUtils.toResponseEntity(res,"MCCI_IN000002UV01");
			}
			if(resobj==null||resobj.getAcknowledgement()==null||"AE".equals(resobj.getAcknowledgement().getTypeCode())){
				//发送失败，记录发送失败记录
				String errText="";
				if(resobj!=null && resobj.getAcknowledgement()!=null){
				    errText=resobj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
					msgId=resobj.getAcknowledgement().getTargetMessage().getId().getExtension();
				}
				msgContext=hipMessageServerUtils.getSoapXML("PatientInfoUpdate", req);
				SysLogService.saveSysMsgRec("send", msgContext, errText,msgType, msgId);
			}else {
				mpi = resobj.getAcknowledgement().getAcknowledgementDetail().getText().getDescription().getValue();
			}
			if(CommonUtils.isNotEmpty(mpi))
				DataBaseHelper.update("update pi_master set mpi = ? where pk_pi = ?", new Object[]{mpi,piMaster.getPkPi()});
			
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
   /**
    * 查询患者
    */
	@Override
	public Object queryEmpi(PiMaster piMaster) {
		String type = ApplicationUtils.getSysparam("PUB0001", false);
		if (!StringUtils.isNotBlank(type)||"0".equals(type)||(type.indexOf("4")<0))
			return null;
		try {
			if(piMaster==null) return null;
			String msgContext="";
			String msgType="";
			String msgId="";
			
			String req = null;//调用EMPI请求信息XML
			String res = null;//调用EMPI响应信息XML
			String mpi = piMaster.getMpi();
			Response resobj = null;//EMPI响应信息对象
			
			List<Map<String, Object>> resList = null;
			//调用EMPI查询服务 
			if(mpi==null||"".equals(mpi)){
				//先查询EMPI中的是否存在，存在则取empi患者主索引值
				req = convertPiToRequstXml(piMaster,"2","PRPA_IN201305UV02.xsd");
				System.err.println(req);
				res =  hipMessageServerUtils.sendHIPService("PatientInfoQuery", req);//调用EMPI查询服务
				System.err.println(res);
				if(StringUtils.isNoneBlank(res) && res.indexOf("<HIP_EMPI>")>-1){
					resList = createResMapList(res);
				}else{
					resobj = XmlProcessUtils.toResponseEntity(res,"PRPA_IN201306UV02");
					resList = createResObjMapList(resobj);
				}
				//消息发送成功，取患者主索引值
				if((resobj!=null||resList!=null)&&resobj.getAcknowledgement()!=null&&"AA".equals(resobj.getAcknowledgement().getTypeCode())){
					if(resobj.getAcknowledgement().getAcknowledgementDetail()!=null&&resobj.getAcknowledgement().getAcknowledgementDetail().getText()!=null)
					    mpi = resobj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
				}else{
					//发送失败，记录发送失败记录
					String errText="";
					if(resobj!=null && resobj.getAcknowledgement()!=null){
					    errText=resobj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
						msgId=resobj.getAcknowledgement().getTargetMessage().getId().getExtension();
					}
					msgContext=hipMessageServerUtils.getSoapXML("PatientInfoQuery", req);
					SysLogService.saveSysMsgRec("receive", msgContext, errText,msgType, msgId);
					return null;
				}
			}else{//his系统存在empi患者信息
				return piMaster;
			}
			
			if(resList!=null && resList.size()==1){//返回唯一时需要进行的处理
				PiMaster oldPi= ApplicationUtils.mapToBean(resList.get(0),PiMaster.class);
				oldPi.setBirthDate(dateTrans(MsgUtils.getPropValueStr(resList.get(0), "birthday")));
				String piSql="";
				String idNo="";			
				idNo=get15Ic(oldPi.getIdNo());
				//通过住院号查询PI_MASTER记录，若没有则新增
				piSql="select count(1) from pi_master where code_ip=?";
				int count=0;
				count=DataBaseHelper.queryForScalar(piSql, Integer.class, new Object[]{oldPi.getCodeIp()});	
				if(count<1){
					piSql="select count(1) from pi_master where id_no=?";	
					//先查18位身份证
					count=DataBaseHelper.queryForScalar(piSql, Integer.class, new Object[]{oldPi.getIdNo()});		
					if(count<1){
					//再查15位身份证
					 count=DataBaseHelper.queryForScalar(piSql, Integer.class, new Object[]{idNo});		
					 if(count<1){
					//HIS系统中不存在主索引患者信息，目前进行补录数据
					oldPi.setPkOrg(UserContext.getUser().getPkOrg());
					oldPi.setPkPi(NHISUUID.getKeyId());
					oldPi.setPkPicate(empiClientSyxMapper.qryCate());//患者类型
					if(!StringUtils.isNotBlank(oldPi.getCodeOp()))
						oldPi.setCodeOp(ApplicationUtils.getCode("0202"));// 门诊号
					oldPi.setCodeEr(ApplicationUtils.getCode("0303"));// 急诊号
					oldPi.setCodePi(ApplicationUtils.getCode("0201"));//患者编码
					if(!StringUtils.isNotBlank(oldPi.getCodeIp()))
						oldPi.setCodeIp(ApplicationUtils.getCode("0203"));//住院号
					oldPi.setTs(new Date());
					//oldPi.setBirthDate(new Date());
					DataBaseHelper.insertBean(oldPi);
					
					//保存医保计划
					PiInsurance piInsur=new PiInsurance();
					piInsur.setPkInsurance(NHISUUID.getKeyId());
					piInsur.setPkPi(oldPi.getPkPi());
					piInsur.setSortNo(0L);
					String pkHp=resList.get(0).get("pkHp").toString();
					piInsur.setPkHp(pkHp);
					piInsur.setFlagDef("1");
					piInsur.setCreateTime(new Date());
					piInsur.setCreator(UserContext.getUser().getPkEmp());
					piInsur.setTs(new Date());
					piInsur.setDateBegin(DateUtils.getDateMorning(new Date(), 0));
					String dateEnd= DateUtils.addDate(new Date(), 10, 1, "yyyy-MM-dd HH:mm:ss");
					piInsur.setDateEnd(DateUtils.getDateMorning(DateUtils.strToDate(dateEnd), 1));
					DataBaseHelper.insertBean(piInsur);
					}
				}
				}else if(CommonUtils.isNotEmpty(mpi)){
					String sql="update pi_master set mpi=? where pk_pi=?";
					DataBaseHelper.update(sql, new Object[]{mpi,oldPi.getPkPi()});
				}
			}
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
			//logger.debug(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 将18位身份证号转化为15位返回,非18位身份证号原值返回
	 * @param identityCard 18位身份证号
	 * @return 15身份证
	 */
	public String get15Ic(String identityCard) {
		String retId = "";
		if(null == identityCard){
			return retId;
		}
		if(identityCard.length() == 18){
			retId = identityCard.substring(0, 6) + identityCard.substring(8, 17);
		}else{
			return identityCard;
		}
		return retId;
	}
	/**
	 * 患者合并接口
	 */
	@Override
	public Object combineEmpi(Object object) {
		String type = ApplicationUtils.getSysparam("PUB0001", false);
		if (!StringUtils.isNotBlank(type)||"0".equals(type)||(type.indexOf("4")<0))
			return null;
		try {
			String msgContext="";
			String msgType="";
			String msgId="";
			
			Request req = new Request("PRPA_IN201304UV02.xsd");//调用EMPI请求信息XML
			String res = null;//调用EMPI响应信息XML
			Response resobj = null;//EMPI响应信息对象
			PiMasterEmpiInfo piMasterEmpiInfo=(PiMasterEmpiInfo)object;
			req = combineEmpiXml(req,piMasterEmpiInfo,"PRPA_IN201304UV02.xsd");
			String reqStr = XmlProcessUtils.toRequestXml(req,"PRPA_IN201304UV02.xsd".substring(0, "PRPA_IN201304UV02.xsd".indexOf(".")));
			System.err.println(reqStr);
			res =  hipMessageServerUtils.sendHIPService("PatientInfoMerge", reqStr);//调用EMPI查询服务
			System.err.println(res);
			resobj = XmlProcessUtils.toResponseEntity(res,"MCCI_IN000002UV01");
			//消息发送成功，取患者主索引值
			if(resobj!=null&&resobj.getAcknowledgement()!=null&&"AA".equals(resobj.getAcknowledgement().getTypeCode())){
				String newMpi=resobj.getAcknowledgement().getAcknowledgementDetail().getText().getDescription().getValue();
				DataBaseHelper.update("update pi_master set mpi = ? where pk_pi = ?", new Object[]{newMpi,piMasterEmpiInfo.getOldPkPi()});
			}else{
				//发送失败，记录发送失败记录
				String errText="";
				if(resobj!=null && resobj.getAcknowledgement()!=null){
					errText=resobj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
					msgId=resobj.getAcknowledgement().getTargetMessage().getId().getExtension();
				}
				msgContext=hipMessageServerUtils.getSoapXML("PatientInfoQuery", reqStr);
				SysLogService.saveSysMsgRec("receive", msgContext, errText,msgType, msgId);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return null;
	}
	
	 /**
	  * 查询本地的empi的患者信息
	  * @param param
	  * @param user
	  * @return
	  */
  	public Object queryRepetitionPi(String param, IUser user) {
  		String type = ApplicationUtils.getSysparam("PUB0001", false);
		if (!StringUtils.isNotBlank(type)||"0".equals(type)||(type.indexOf("4")<0))
			return null;
  		PiMaster master = JsonUtil.readValue(param, PiMaster.class);
  		if(!StringUtils.isNotBlank(master.getIdNo())){
  			master.setIdNo("");
  		}
  		int count=empiClientSyxMapper.qryRepetitionPi(master.getIdNo());
  		if(count<1){
  			//本地没有empi患者查询主索引系统数据
  			return queryEmpi(master);
  		}else{
  			//本地有empi患者直接将患者信息展示在前台上
  			return master;
  		}
  	}
  	
  	/**
  	 * 合并empi患者信息
  	 * @param param
  	 * @param user
  	 */
  	public void combinEmpiInfo(String param,IUser user){
  		String type = ApplicationUtils.getSysparam("PUB0001", false);
		if (!StringUtils.isNotBlank(type)||"0".equals(type)||(type.indexOf("4")<0))
			return ;
  		try {
			PiMasterEmpiInfo piMasterEmpiInfo = JsonUtil.readValue(param, PiMasterEmpiInfo.class);
			if(piMasterEmpiInfo==null)return;
			combineEmpi(piMasterEmpiInfo);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
  	}
	/**
     * 此方法不实现
     */
	@Override
	public Object queryRelateEmpi(String code, String idtype, String cardNo) {
		return null;
	}
	
	/**
	 * 将患者信息转换为empi请求对象
	 * @param piMaster
	 * @param serviceType 调用服务类型(0:新增患者,1:更新患者,2:查询患者)
	 * @return
	 */
    private String convertPiToRequstXml(PiMaster piMaster,String serviceType,String xsd){
    	Request req = new Request(xsd); 
    	//TODO 添加属性转换代码，根据文档《S001.个人身份注册/更新/查询服务v1.0.docx》中消息模型说明，Request中不需要属性需要设置成null
    	if("0".equals(serviceType)){///新增时，按文档设置新增属性
    		req = getPiRegister(req,piMaster,true,"PRPA_IN201311UV02");
    	}else if("1".equals(serviceType)){////更新时，按文档设置更新属性
    		req = getPiRegister(req,piMaster,false,"PRPA_IN201314UV02");
    	}else{//按文档设置查询属性
    		req=qrypiMaster(req ,piMaster);
    	}
    	
    	//翻译字典类字段方法，BdPubService.getBdDefDocStd
    	//公共属性设置
    	req.getId().setRoot("2.16.156.10011.2.5.1.1");
    	//调用ma下Request对象转xml服务，将Request信息转换为webservice请求xml参数
    	return XmlProcessUtils.toRequestXml(req,xsd.substring(0, xsd.indexOf(".")));
    }
    
 
    
    /**
     * 拼接查询和更新xml
     * @param req
     * @param piMaster
     * @param isAdd
     * @param action
     * @return
     */
    private Request getPiRegister(Request req,PiMaster piMaster , boolean isAdd,String action){
    	req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
    	req.getInteractionId().setExtension(action);
    	req.getProcessingCode().setCode("P");
    	req.getProcessingModeCode();
    	req.getAcceptAckCode().setCode("AL");
    	req.getReceiver().setTypeCode("RCV");
    	req.getReceiver().getDevice().setClassCode("DEV");
    	req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
    	
    	req.getSender().setTypeCode("SND");
    	req.getSender().getDevice().setClassCode("DEV");
    	req.getSender().getDevice().setDeterminerCode("INSTANCE");
    	req.getControlActProcess().setClassCode("CACT");
    	req.getControlActProcess().setMoodCode("EVN");
    	Subject subject=req.getControlActProcess().getSubject();
    	subject.setTypeCode("SUBJ");
    	RegistrationRequest regireRequest=subject.getRegistrationRequest();
    	regireRequest.setClassCode("REG");
    	regireRequest.setMoodCode("RQO");
    	regireRequest.getStatusCode().setCode("active");
    	regireRequest.getSubject1().setTypeCode("SBJ");
    	Patient patient=regireRequest.getSubject1().getPatient();
    	patient.setClassCode("PAT");
    	patient.getStatusCode().setCode("active");
    	patient.getPatientPerson().getName().setXSI_TYPE("LIST_EN");
    	patient.getPatientPerson().getTelecom().setXSI_TYPE("DSET_TEL");
    	patient.getPatientPerson().getAddr().setXSI_TYPE("LIST_AD");
    	patient.getPatientPerson().getAsEmployee().setClassCode("EMP");
    	patient.getPatientPerson().getAsEmployee().getEmployerOrganization().setClassCode("ORG");
    	patient.getPatientPerson().getAsEmployee().getEmployerOrganization().setDeterminerCode("INSTANCE");
    	patient.getPatientPerson().getAsEmployee().getEmployerOrganization().getName().setXSI_TYPE("LIST_EN");
    	patient.getPatientPerson().getAsEmployee().getEmployerOrganization().getContactParty().setClassCode("CON");
    	patient.getPatientPerson().getAsEmployee().getEmployerOrganization().getContactParty().getTelecom().setXSI_TYPE("DSET_TEL");
    	patient.getProviderOrganization().setClassCode("ORG");
    	patient.getProviderOrganization().setDeterminerCode("INSTANCE");
    	patient.getCoveredPartyOf().setTypeCode("COV");
    	patient.getCoveredPartyOf().getCoverageRecord().setClassCode("COV");
    	patient.getCoveredPartyOf().getCoverageRecord().setMoodCode("EVN");
    	patient.getCoveredPartyOf().getCoverageRecord().getBeneficiary().setTypeCode("BEN");
    	patient.getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().setClassCode("MBR");
    	
    	Author author=regireRequest.getAuthor();
    	author.setTypeCode("AUT");
    	author.getAssignedEntity().setClassCode("ASSIGNED");
    	author.getAssignedEntity().getAssignedPerson().setClassCode("PSN");
    	author.getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
    	author.getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("LIST_EN");
    	
    	req.getId().setExtension(UUID.randomUUID().toString().replace("-", ""));
    	req.getCreationTime().setValue(sdf.format(new Date()));
    	req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
    	req.getReceiver().getDevice().getId().getItem().setExtension("192.168.8.234");
    	if(!isAdd)
    		req.getReceiver().getDevice().getId().getItem().setExtension(piMaster.getMpi());
    	req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
    	req.getSender().getDevice().getId().getItem().setExtension("HIS");
    	patient.getId().getItem().setExtension(piMaster.getCodePi());
    	patient.getId().getItem().setRoot("2.16.156.10011.2.5.1.4");
    	patient.getStatusCode().setCode("active");
    	if(piMaster.getCreateTime()==null)
    		piMaster.setCreateTime(new Date());
    	patient.getEffectiveTime().getAny().setValue(sdf.format(piMaster.getCreateTime()));
    	Map<String, Object> piMap = empiClientSyxMapper.qryPiMap(piMaster.getPkPi());
    	
    	patient.getPatientPerson().getId().getItem().setRoot("2.16.156.10011.1.3");
    	patient.getPatientPerson().getId().getItem().setExtension("");
    	if ("居民身份证".equals(MsgUtils.getPropValueStr(piMap, "idtypeName"))) {
    		patient.getPatientPerson().getId().getItem().setExtension(piMaster.getIdNo());
		}
    	
    	patient.getPatientPerson().getIdCategory().setCodeSystem("2.16.156.10011.2.3.1.1");
    	
    	patient.getPatientPerson().getIdCategory().setCodeSystemName("身份证件类别代码表");
    	
    	patient.getPatientPerson().getIdCategory().setCode(MsgUtils.getPropValueStr(piMap, "idtypeCode"));
    	
    	patient.getPatientPerson().getIdCategory().getDisplayName().setValue(MsgUtils.getPropValueStr(piMap, "idtypeName"));
    	
    	//10     	
    	Part partName = new Part();
    	partName.setValue(piMaster.getNamePi()+"^"+MsgUtils.getPropValueStr(piMap, "codeIp")+"^"+MsgUtils.getPropValueStr(piMap, "codeOp"));
    	//姓名
    	patient.getPatientPerson().getName().getItem().getParts().add(partName);
    	
    	//联系电话
    	String mobile = "";
    	if(StringUtils.isNotBlank(piMaster.getMobile())){
    		mobile=piMaster.getMobile();
    	}else {
			mobile=piMaster.getTelNo();
		}
    	//patient.setPatie(mobile);
    	//BdDefdoc docSex = new BdDefdoc();
    	if(StringUtils.isNotBlank(piMaster.getDtSex())){
    	//查询性别
    		//docSex = bdPubService.getBdDefDocStd(piMaster.getDtSex(), "000000");
    		}
    	//性别
    	patient.getPatientPerson().getAdministrativeGenderCode().setCode(MsgUtils.getPropValueStr(piMap, "sexCode"));
    	
    	/***/
    	PatientPerson patientPerson = patient.getPatientPerson();
    	
    	//15 性别
    	patientPerson.getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
    	//性别
    	patientPerson.getAdministrativeGenderCode().getDisplayName().setValue(MsgUtils.getPropValueStr(piMap, "sexName"));
    	
    	patientPerson.getTelecom().getItem().setValue(mobile);
    	//17出生时间
    	patientPerson.getBirthTime().setValue(sd.format(piMaster.getBirthDate()));
    	if(StringUtils.isNotBlank(piMaster.getAddrcodeRegi()));
    	piMaster.setAddrcodeRegi("");
    	
    	if(piMaster.getAddrcodeCur()!=null){
    		Map<String, Object> addr = empiClientSyxMapper.getAddr(piMaster.getAddrcodeCur());
    		if(addr!=null){
    			//18,19
    	    	List<Part> parts = patientPerson.getAddr().getItem().getParts();
    	    	Part part = new Part();
    	    	part.setType("SAL");
    	    	part.setValue(piMaster.getAddrCur()+piMaster.getAddrCurDt());
    	    	parts.add(part);
    	    	
    	    	//20
    	    	Part partPro = new Part();
    	    	partPro.setType("STA");
    	    	partPro.setValue(MsgUtils.getPropValueStr(addr, "prov"));
    	    	parts.add(partPro);
    	    	
    	    	Part partCity = new Part();
    	    	partCity.setType("CTY");
    	    	partCity.setValue(MsgUtils.getPropValueStr(addr, "city"));
    	    	parts.add(partCity);
    	    	
    	    	Part partDis = new Part();
    	    	partDis.setType("CNT");
    	    	partDis.setValue(MsgUtils.getPropValueStr(addr, "dist"));
    	    	parts.add(partDis);
    	    	
    	    	Part partVil = new Part();
    	    	partVil.setType("STB");
    	    	partVil.setValue(MsgUtils.getPropValueStr(addr, "nameDiv"));
    	    	parts.add(partVil);
    	    	
    	    	int subLen = piMaster.getAddrCurDt().length();
    	    	if(StringUtils.isNotBlank(piMaster.getAddrCurDt())&&piMaster.getAddrCurDt().length()>1&&piMaster.getAddrCurDt().charAt(piMaster.getAddrCurDt().length()-1)=='号'){
    	    		Pattern p = Pattern.compile("[0-9]");
    	    		for (int i = piMaster.getAddrCurDt().length()-1; i >=1; i--) {
    	    			Matcher mat = p.matcher(piMaster.getAddrCurDt().charAt(i)+"");
    	    			Matcher matUp = p.matcher(piMaster.getAddrCurDt().charAt(i-1)+"");
    	    			if(mat.find()&&!matUp.find()){
    	    				subLen=i;
    	    				break;
    	    			}
    				}
    	    		
    	    	}
    	    	 
    	    	Part partHam = new Part();
    	    	partHam.setType("STR");
    	    	partHam.setValue(piMaster.getAddrCurDt().substring(0, subLen));
    	    	parts.add(partHam);
    	    	
    	    	Part partMark = new Part();
    	    	partMark.setType("BNR");
    	    	partMark.setValue(piMaster.getAddrCurDt().substring(subLen));
    	    	parts.add(partMark);
    	    	
    	    	Part partZip = new Part();
    	    	partZip.setType("ZIP");
    	    	partZip.setValue(piMaster.getPostcodeCur());
    	    	parts.add(partZip);
    		}
    	}
    	
    	
    	
    	//BdDefdoc docMarry = new BdDefdoc();
    	if(StringUtils.isNotBlank(piMaster.getDtMarry())){
    	//查询婚姻状况
    	 	//docMarry = bdPubService.getBdDefDocStd(piMaster.getDtMarry(), "000006");
    	 	}
    	//34婚姻状况
    	patientPerson.getMaritalStatusCode().setCode(MsgUtils.getPropValueStr(piMap, "marryCode"));
    	patientPerson.getMaritalStatusCode().setCodeSystem("2.16.156.10011.2.3.3.5");
    	
    	patientPerson.getMaritalStatusCode().getDisplayName().setValue(MsgUtils.getPropValueStr(piMap, "marryName"));
    	
    	//BdDefdoc docNat = new BdDefdoc();
    	//查询民族
    	if(StringUtils.isNotBlank(piMaster.getDtNation())){
    		 //docNat = bdPubService.getBdDefDocStd(piMaster.getDtNation(), "000003");
    		 }
    	
    	patientPerson.getEthnicGroupCode().getItem().setCode(MsgUtils.getPropValueStr(piMap, "nationCode"));
    	
    	patientPerson.getEthnicGroupCode().getItem().setCodeSystem("2.16.156.10011.2.3.3.3");
    	
    	patientPerson.getEthnicGroupCode().getItem().getDisplayName().setValue(MsgUtils.getPropValueStr(piMap, "nationName"));
    	
    	//BdDefdoc docOccu = new BdDefdoc();
    	if(StringUtils.isNotBlank(piMaster.getDtOccu())){
    	//查询职业分类
    		//docOccu = bdPubService.getBdDefDocStd(piMaster.getDtOccu(), "000010");
    		}
    	//40
    	patientPerson.getAsEmployee().getOccupationCode().setCode(MsgUtils.getPropValueStr(piMap, "occuCode"));
    	
    	patientPerson.getAsEmployee().getOccupationCode().setCodeSystem("2.16.156.10011.2.3.3.7");
    	
    	patientPerson.getAsEmployee().getOccupationCode().getDisplayName().setValue(MsgUtils.getPropValueStr(piMap, "occuName"));
    	
    	//工作单位
    	patientPerson.getAsEmployee().getEmployerOrganization().getName().getItem().getPart().setValue(piMaster.getUnitWork());
    	
    	patientPerson.getAsEmployee().getEmployerOrganization().getContactParty().getTelecom().getItem().setValue(piMaster.getTelWork());
    	
    	//45健康卡号
    	//String hicNo = empiClientSyxMapper.getHicNo(piMaster.getPkPi());
    	List<AsOtherIDs> asOtherIDs = patientPerson.getAsOtherIDs();
    	AsOtherIDs asOtherIDHic = new AsOtherIDs();
		asOtherIDHic.getId().getItem().setExtension(MsgUtils.getPropValueStr(piMap, "hicNo"));
		asOtherIDHic.setClassCode("PAT");
		asOtherIDHic.getScopingOrganization().setClassCode("ORG");
		asOtherIDHic.getScopingOrganization().setDeterminerCode("INSTANCE");
		
		asOtherIDHic.getId().getItem().setRoot("2.16.156.10011.1.19");
    	//健康卡发放单位
		asOtherIDHic.getScopingOrganization().getId().getItem().setExtension(UserContext.getUser().getPkOrg());
    	
		asOtherIDHic.getScopingOrganization().getId().getItem().setRoot("2.16.156.10011.1.5");
    	
		asOtherIDs.add(asOtherIDHic);
		
		AsOtherIDs asOtherRec = new AsOtherIDs();
		asOtherRec.setClassCode("PAT");
		asOtherRec.getScopingOrganization().setClassCode("ORG");
		asOtherRec.getScopingOrganization().setDeterminerCode("INSTANCE");
		
    	//50
		asOtherRec.getId().getItem().setRoot("2.16.156.10011.1.2");
    	
		asOtherRec.getScopingOrganization().getId().getItem().setExtension(UserContext.getUser().getPkOrg());
    	
		asOtherRec.getScopingOrganization().getId().getItem().setRoot("2.16.156.10011.1.5");
		asOtherIDs.add(asOtherRec);
		
		//BdDefdoc docRel = new BdDefdoc();
    	if(StringUtils.isNotBlank(piMaster.getDtRalation())){
		//查询联系人
    		 //docRel = bdPubService.getBdDefDocStd(piMaster.getDtRalation(), "000013");
    		 }
    	
		//联系人000013
    	patientPerson.getPersonalRelationship().getCode().setCode(MsgUtils.getPropValueStr(piMap, "ralationCode"));
    	patientPerson.getPersonalRelationship().getCode().setCodeSystem("2.16.156.10011.2.3.1.34");
    	patientPerson.getPersonalRelationship().getCode().setCodeSystemName("家庭关系代码");
    	patientPerson.getPersonalRelationship().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(piMap, "ralationName"));
    	patientPerson.getPersonalRelationship().getTelecom().getItem().setValue(piMaster.getTelRel());
    	patientPerson.getPersonalRelationship().getTelecom().setXSI_TYPE("DSET_TEL");
    	patientPerson.getPersonalRelationship().getRelationshipHolder1().setClassCode("PSN");
    	patientPerson.getPersonalRelationship().getRelationshipHolder1().setDeterminerCode("INSTANCE");
    	patientPerson.getPersonalRelationship().getRelationshipHolder1().getName().setXSI_TYPE("LIST_EN");
    	
    	patientPerson.getPersonalRelationship().getRelationshipHolder1().setClassCode("PSN");
    	patientPerson.getPersonalRelationship().getRelationshipHolder1().setDeterminerCode("INSTANCE");
    	//55
    	patientPerson.getPersonalRelationship().getRelationshipHolder1().getName().getItem().getPart().setValue(piMaster.getNameRel());
    	
    	//组织机构
    	patient.getProviderOrganization().getId().getItem().setExtension("455416037");
    	
    	patient.getProviderOrganization().getId().getItem().setRoot("2.16.156.10011.1.5");
    	
    	//String nameOrg =qryNameorg(UserContext.getUser().getPkOrg());
    	patient.getProviderOrganization().getName().setXSI_TYPE("LIST_EN");
    	patient.getProviderOrganization().getName().getItem().getPart().setValue("中山大学孙逸仙纪念医院");
    	patient.getProviderOrganization().getContactParty().setClassCode("CON");
    	
    	//Map<String, Object> hpType = empiClientSyxMapper.getHpType(piMaster.getPkPi());
    	
    	patient.getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().getCode().setCode(MsgUtils.getPropValueStr(piMap, "hpCode"));
    	
    	//60
    	patient.getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().getCode().setCodeSystem("2.16.156.10011.2.3.1.248");
    	
    	patient.getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().getCode().setCodeSystemName(piMaster.getInsurNo());
    	
    	patient.getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(piMap, "hpName"));
    	
    	//Map<String, Object> creator = empiClientSyxMapper.getCreator(UserContext.getUser().getPkEmp());
    	
    	author.getAssignedEntity().getId().getItem().setExtension(UserContext.getUser().getCodeEmp());
    	
    	author.getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
    	
    	author.getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue(UserContext.getUser().getNameEmp());
    	return req;
    }
    
    /***
     * 拼接查询xml对象
     * @param req
     * @param piMaster
     * @return
     */
    private Request qrypiMaster(Request req, PiMaster piMaster) {
    	if(!StringUtils.isNotBlank(piMaster.getPkPi())&&StringUtils.isNotBlank(piMaster.getIdNo())&&"01".equals(piMaster.getDtIdtype())){
    		 PiMaster master = empiClientSyxMapper.qryPiMasterById(piMaster.getIdNo());
    		 if(master !=null)
    			 piMaster = master;
    	}
    		
    	req.getId().setRoot("2.16.156.10011.2.5.1.1");
    	req.getId().setExtension(UUID.randomUUID().toString().replace("-",""));
    	req.getCreationTime().setValue(sdf.format(new Date()));
    	req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
    	req.getInteractionId().setExtension("PRPA_IN201305UV02");
    	req.getProcessingCode().setCode("P");
    	req.getAcceptAckCode().setCode("AL");
    	req.getReceiver().setTypeCode("RCV");
    	req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
    	req.getReceiver().getDevice().setClassCode("DEV");
    	req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
    	req.getReceiver().getDevice().getId().getItem().setExtension(piMaster.getMpi());
    	
    	if(!StringUtils.isNotBlank(piMaster.getCreator()))
    		piMaster.setCreator("");
    	req.getSender().setTypeCode("SND");
    	req.getSender().getDevice().setClassCode("DEV");
    	req.getSender().getDevice().setDeterminerCode("INSTANCE");
    	req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
    	req.getSender().getDevice().getId().getItem().setExtension("HIS");
    	
    	req.getControlActProcess().setClassCode("CACT");
    	req.getControlActProcess().setMoodCode("EVN");
    	req.getControlActProcess().getQueryByParameter().getQueryId().setExtension(UUID.randomUUID().toString().replace("-",""));
    	req.getControlActProcess().getQueryByParameter().getStatusCode().setCode("new");
    	req.getControlActProcess().getQueryByParameter().getInitialQuantity().setValue("2");
    	
    	MinimumDegreeMatch minimumDegreeMatch = req.getControlActProcess().getQueryByParameter().getMatchCriterionList().getMinimumDegreeMatch();
    	minimumDegreeMatch.getValue().setXSI_TYPE("INT");
    	minimumDegreeMatch.getValue().setValue("100");
    	minimumDegreeMatch.getSemanticsText().setValue("匹配程度");
    	
    	ParameterList parameterList = req.getControlActProcess().getQueryByParameter().getParameterList();
    	parameterList.getId().setRoot("2.16.156.10011.2.5.1.4");
    	parameterList.getId().setExtension(piMaster.getPkPi());
    	
    	BdDefdoc docSex = new BdDefdoc();
    	if(StringUtils.isNotBlank(piMaster.getDtSex()))
    		docSex = bdPubService.getBdDefDocStd(piMaster.getDtSex(), "000000");
    	
    	parameterList.getLivingSubjectAdministrativeGender().getValue().setCode(docSex.getCodeStd());
    	parameterList.getLivingSubjectAdministrativeGender().getValue().setCodeSystem("2.16.156.10011.2.3.3.4");
    	parameterList.getLivingSubjectAdministrativeGender().getValue().getDisplayName().setValue(docSex.getNameStd());
    	parameterList.getLivingSubjectAdministrativeGender().getSemanticsText().setValue("患者性别");
    	
    	parameterList.getLivingSubjectId().getValue().getItem().setRoot("2.16.156.10011.1.3");
    	parameterList.getLivingSubjectId().getValue().getItem().setExtension(piMaster.getIdNo());
    	parameterList.getLivingSubjectId().getSemanticsText().setValue("患者身份证号");
    	
    	parameterList.getLivingSubjectName().getValue().getItem().getPart().setValue(piMaster.getNamePi());
    	parameterList.getLivingSubjectName().getSemanticsText().setValue("患者姓名");
//    	req.setXMLNS_XSI("http://www.w3.org/2001/XMLSchema-instance");
//    	req.setXmlns("urn:hl7-org:v3");
    	return req;
	}
    
    /**
	 * 拼接合并xml
	 * @param piMastersMap
	 * @param string
	 * @return
	 */
    private Request combineEmpiXml(Request req, PiMasterEmpiInfo piMasterInfo, String action) {
    	req.getId().setRoot("2.16.156.10011.2.5.1.1");
    	req.getId().setExtension(UUID.randomUUID().toString().replace("-",""));
    	req.getCreationTime().setValue(sdf.format(new Date()));
    	req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
    	req.getInteractionId().setExtension("PRPA_IN201305UV02");
    	req.getProcessingCode().setCode("P");
    	req.getAcceptAckCode().setCode("AL");
    	req.getReceiver().setTypeCode("RCV");
    	req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
    	req.getReceiver().getDevice().setClassCode("DEV");
    	req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
    	req.getReceiver().getDevice().getId().getItem().setExtension(piMasterInfo.getMpi());
    	
//    	if(!StringUtils.isNotBlank(piMaster.getCreator()))
//    		piMaster.setCreator("");
//    	Map<String, Object> creator = empiClientSyxMapper.getCreator(piMaster.getCreator());
    	req.getSender().setTypeCode("SND");
    	req.getSender().getDevice().setClassCode("DEV");
    	req.getSender().getDevice().setDeterminerCode("INSTANCE");
    	req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
    	req.getSender().getDevice().getId().getItem().setExtension(piMasterInfo.getOldMpi());
    	
    	
    	req.getControlActProcess().setClassCode("CACT");
    	req.getControlActProcess().setMoodCode("EVN");
    	req.getControlActProcess().getSubject().setTypeCode("SUBJ");
    	req.getControlActProcess().getSubject().getRegistrationEvent().setClassCode("REG");
    	req.getControlActProcess().getSubject().getRegistrationEvent().setMoodCode("EVN");
    	RegistrationEvent registrationEvent = req.getControlActProcess().getSubject().getRegistrationEvent();
    	
    	registrationEvent.getStatusCode().setCode("active");
    	registrationEvent.getSubject1().setTypeCode("SBJ");
    	registrationEvent.getSubject1().getPatient().setClassCode("PAT");
    	registrationEvent.getSubject1().getPatient().getId().getItem().setRoot("2.16.156.10011.2.5.1.4");
    	if(!StringUtils.isNotBlank(piMasterInfo.getPkPi()))
    		piMasterInfo.setPkPi("");
    	Map<String, Object> creatInfo = empiClientSyxMapper.getCreatInfo(piMasterInfo.getPkPi());
    	registrationEvent.getSubject1().getPatient().getId().getItem().setExtension(piMasterInfo.getPkPi());
    	registrationEvent.getSubject1().getPatient().getStatusCode().setCode("active");
    	
    	String time = MsgUtils.getPropValueStr(creatInfo, "createTime");
    	if (StringUtils.isNotBlank(time)) {
			time = time.replaceAll("-", "");
			time = time.replaceAll(" ", "");
			time = time.replaceAll(":", "");
			if(time.length()>14)
				time = time.substring(0,13);
		}
    	registrationEvent.getSubject1().getPatient().getEffectiveTime();
    	registrationEvent.getSubject1().getPatient().getEffectiveTime().getAny().setValue(time);
    	
    	String idNo = "";
    	String idCodeStd = "";
    	String idNameStd = "";
    	List<CardVo> cardList = piMasterInfo.getCardList();
    	if(cardList != null && cardList.size()>0){
				idNo = cardList.get(0).getCardNo();
				idCodeStd = cardList.get(0).getCodeCardType();
				idNameStd = cardList.get(0).getNameCardType(); 
    	}
    	registrationEvent.getSubject1().getPatient().getPatientPerson().getId().getItem().setRoot("2.16.156.10011.1.3");
    	registrationEvent.getSubject1().getPatient().getPatientPerson().getId().getItem().setExtension(idNo);
    	registrationEvent.getSubject1().getPatient().getPatientPerson().getIdCategory().setCodeSystem("2.16.156.10011.2.3.1.1");
    	registrationEvent.getSubject1().getPatient().getPatientPerson().getIdCategory().setCodeSystemName("身份证件类别代码表");
    	
    	
    	registrationEvent.getSubject1().getPatient().getPatientPerson().getIdCategory().setCode(idCodeStd);
    	registrationEvent.getSubject1().getPatient().getPatientPerson().getIdCategory().getDisplayName().setValue(idNameStd);

    	registrationEvent.getSubject1().getPatient().getPatientPerson().getName().setXSI_TYPE("LIST_EN");
    	registrationEvent.getSubject1().getPatient().getPatientPerson().getName().getItem().getPart().setValue(piMasterInfo.getNamePi());

    	registrationEvent.getCustodian().setTypeCode("CST");
    	registrationEvent.getCustodian().getAssignedEntity().setClassCode("ASSIGNED");
    	registrationEvent.getCustodian().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
    	
    	registrationEvent.getCustodian().getAssignedEntity().getId().getItem().setExtension(UserContext.getUser().getPkEmp());
    	registrationEvent.getCustodian().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
    	registrationEvent.getCustodian().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
    	registrationEvent.getCustodian().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("LIST_EN");
    	registrationEvent.getCustodian().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue(UserContext.getUser().getNameEmp());
  
    	registrationEvent.getReplacementOf().setTypeCode("RPLC");
    	registrationEvent.getReplacementOf().getPriorRegistration().setClassCode("REG");
    	registrationEvent.getReplacementOf().getPriorRegistration().setMoodCode("EVN");
    	registrationEvent.getReplacementOf().getPriorRegistration().getStatusCode().setCode("obsolete");
    	registrationEvent.getReplacementOf().getPriorRegistration().getSubject1().setTypeCode("SBJ");
    	registrationEvent.getReplacementOf().getPriorRegistration().getSubject1().getPriorRegisteredRole().setClassCode("PAT");
    	registrationEvent.getReplacementOf().getPriorRegistration().getSubject1().getPriorRegisteredRole().getId().getItem().setRoot("2.16.156.10011.2.5.1.4");
    	
    	String oldPkPi = piMasterInfo.getOldPkPi();
    	if(StringUtils.isEmpty(oldPkPi) && StringUtils.isNotBlank(piMasterInfo.getOldMpi()))
    		oldPkPi = empiClientSyxMapper.getpkPiByMpi(piMasterInfo.getOldMpi());
    	registrationEvent.getReplacementOf().getPriorRegistration().getSubject1().getPriorRegisteredRole().getId().getItem().setExtension(oldPkPi);
    	
		return req;
	}
    
    private String qryNameorg(String pkOrg){
    	String sql="select name_org from bd_ou_org where del_flag='0' and pk_org=?";
    	Map<String,Object> paramMap=DataBaseHelper.queryForMap(sql, new Object[]{pkOrg});
    	if(paramMap!=null){
    		return paramMap.get("nameOrg").toString();
    	}else{
    		return "";
    	}
    }
    
    /**
     * 处理多个返回对象:非标准节点
     * @param res
     * @return
     */
    private List<Map<String, Object>> createResMapList(String res) {
    	HipEmpi hip =doXmlRetInfo(res);
    	String stat = hip.getResult().getSTAT();
    	//如果失败则不处理
    	if(StringUtils.isNotBlank(stat)&&stat.equals("1")) return null;
		 List<PatientEmpi> patients = hip.getPatients();
		 if(patients ==null || patients.size()<1)  return null;
		 List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		 
		 for (PatientEmpi patient : patients) {
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("codePi", patient.getSOURCE_CODE());//患者编码
			resMap.put("pkPi", patient.getSOURCE_ID());
			resMap.put("mpi",patient.getMPI());
			resMap.put("codeIp", patient.getINPNO());
			resMap.put("codeOp", patient.getOUTPNO());
			resMap.put("namePi",patient.getPI_NAME());
			String dtSex=qryDefdocdata("000000", patient.getCODE_SEX());
			resMap.put("dtSex",dtSex);
			resMap.put("nameSex",patient.getNAME_SEX());
			resMap.put("birthday",patient.getBIRTHDAY());
			resMap.put("birthDate",patient.getBIRTHDAY());
			resMap.put("codeHpType",patient.getCODE_TYPE_INSURANCE());
			resMap.put("nameHpType",patient.getNAME_TYPE_INSURANCE());
			resMap.put("codeMari",patient.getCODE_MARI());
			resMap.put("nameMari",patient.getNAME_MARI());
			resMap.put("codeOccu",patient.getCODE_OCCU());
			resMap.put("nameOccu",patient.getNAME_OCCU());
			resMap.put("bloodType",patient.getBLOOD_TYPE());
			resMap.put("rh",patient.getRH());
			resMap.put("codeEdu",patient.getCODE_EDU());
			resMap.put("nameEdu",patient.getNAME_EDU());
			resMap.put("codeDegree",patient.getCODE_DEGREE());
			resMap.put("nameDegree",patient.getNAME_DEGREE());
			resMap.put("codeCountry",patient.getCODE_COUNTRY());
			resMap.put("nameCountry",patient.getNAME_COUNTRY());
			resMap.put("codeNation",patient.getCODE_NATION());
			resMap.put("nameNation",patient.getNAME_NATION());
			resMap.put("teleno",patient.getTELENO());
			resMap.put("mobileno",patient.getMOBILENO());
			resMap.put("email",patient.getEMAIL());
			resMap.put("nameWoekunit",patient.getNAME_WORKUNIT());
			resMap.put("addrWorkunit",patient.getADDR_WORKUNIT());
			resMap.put("phoneWorkunit",patient.getPHONE_WORKUNIT());
			resMap.put("postcodeWorkunit",patient.getPOSTCODE_WORKUNIT());
			resMap.put("createDate",patient.getCREATE_DATE());
			resMap.put("codeCreateuser",patient.getCODE_CREATEUSER());
			resMap.put("nameCreateuser",patient.getNAME_CREATEUSER());
			resMap.put("extData",patient.getEXT_DATA());
			resMap.put("levelSecrecy",patient.getLEVEL_SECRECY());
			
			List<Card> cards = patient.getCards();
			if(cards!=null && cards.size()>0){
				ArrayList<Map<String, Object>> cardList = new ArrayList<Map<String, Object>>();
				for (Card card : cards) {
					HashMap<String, Object> cardMap = new HashMap<String,Object>();
					cardMap.put("codeCardType", card.getCODE_CARD_TYPE());
					cardMap.put("nameCardType", card.getNAME_CARD_TYPE());
					cardMap.put("caedNo",card.getCARD_NO());
					cardMap.put("flagMake",card.getFLAG_MAKE());
					cardList.add(cardMap);
				}
				resMap.put("cardList", cardList);
			}
			
			List<Address> addressList = patient.getAddressList();
			if(addressList!=null&&addressList.size()>0){
				ArrayList<Map<String, Object>> addresss = new ArrayList<Map<String, Object>>();
				for (Address address : addressList) {
					HashMap<String, Object> addressMap = new HashMap<String,Object>();
					addressMap.put("codeAddrType", address.getCODE_ADDR_TYPE());
					addressMap.put("nameAddrType",address.getNAME_ADDR_TYPE());
					addressMap.put("addrProv",address.getADDR_PROV());
					addressMap.put("addrCity",address.getADDR_CITY());
					addressMap.put("addrCounty",address.getADDR_COUNTY());
					addressMap.put("addrCountry",address.getADDR_COUNTRY());
					addressMap.put("addrVillage",address.getADDR_VILLAGE());
					addressMap.put("addrHouseNum",address.getADDR_HOUSE_NUM());
					addressMap.put("nostructureAddr",address.getNOSTRUCTURE_ADDR());
					addressMap.put("addrZipcode",address.getADDR_ZIPCODE());
					addressMap.put("flagMake",address.getFLAG_MAKE());
					addresss.add(addressMap);
				}
				resMap.put("addressList", addresss);
				
			}
			
			List<Linkman> linkmanList = patient.getLinkmans();
			if(linkmanList!=null&&linkmanList.size()>0){
				ArrayList<Map<String, Object>> linkmans = new ArrayList<Map<String, Object>>();
				for (Linkman linkman : linkmanList) {
					HashMap<String, Object> linkmanMap = new HashMap<String,Object>();
					linkmanMap.put("codeContRelat", linkman.getCODE_CONT_RELAT());
					linkmanMap.put("nameContRelat", linkman.getNAME_CONT_RELAT());
					linkmanMap.put("contactName", linkman.getCONTACT_NAME());
					linkmanMap.put("contactTel", linkman.getCONTACT_TEL());
					linkmanMap.put("contactAddr", linkman.getCONTACT_ADDR());
					linkmanMap.put("flagMake", linkman.getFLAG_MAKE());
				}
				resMap.put("linkmanList", linkmans);
				
			}
			resMapList.add(resMap);
		 }
		 return resMapList;
	}
    
    /**
     * empi标准返回患者信息组建his的pi_master数据
     * @param res
     * @return
     */
    private List<Map<String, Object>> createResObjMapList(Response res) {
		String stat = res.getAcknowledgement().getTypeCode();
		// 如果失败则不处理
		if (!StringUtils.isNotBlank(stat)|| (StringUtils.isNotBlank(stat) && !stat.equals("AA")))
			return null;

		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		RegistrationEvent event = res.getControlActProcess().getSubject().getRegistrationEvent();
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("namePi", event.getSubject1().getPatient().getPatientPerson().getName().getItem().getPart().getValue());
		resMap.put("pkPi",event.getSubject1().getPatient().getId().getItem().getExtension());
		List<Item1> ids = event.getSubject1().getPatient().getId().getItemOnes();
		for (Item1 item : ids) {
			
				
			if ("2.16.156.10011.2.5.1.5".equals(item.getRoot())) 
				resMap.put("codeIp",item.getExtension());
			if ("2.16.156.10011.2.5.1.6".equals(item.getRoot())) 
				resMap.put("codeOp",item.getExtension());
		}
		resMap.put("pkOrg", UserContext.getUser().getPkOrg());// 机构主键
		resMap.put("barcode", "");// 条形码
		resMap.put("pkPicate", "");// TODO 患者分类外键是否写成固定值
		resMap.put("namePi", event.getSubject1().getPatient().getPatientPerson().getName().getItem().getPart().getValue());// 患者姓名
		resMap.put("photoPi", "");// 患者照片
		String idNotype = event.getSubject1().getPatient().getPatientPerson().getIdCategory().getCode();
		String dtIdtype = qryDefdocdata("000007", idNotype);// 证件类型
		String idNo = event.getSubject1().getPatient().getPatientPerson().getId().getItem().getExtension();// 平台证件号码
		/**
		 * 平台证件类型编码 CV02.01.101 01 居民身份证 02 居民户口簿 03 护照 04 军官证 05 驾驶证 06
		 * 港澳居民来往内地通行证 07 台湾居民来往内地通行证 Z08 医保卡号 Z09 原始系统唯一标识号 Z10 健康卡号 99
		 * 其他法定有效证件
		 */
		if ("Z08".equals(idNotype)) {// 医保卡号
			resMap.put("insurNo", idNo);
		}
		if ("Z10".equals(idNotype)) {// 健康卡号
			resMap.put("hicNo", idNo);
		}
		resMap.put("dtIdtype", dtIdtype);// 证件类型
		resMap.put("idNo", idNo);
		resMap.put("mpi", res.getAcknowledgement().getAcknowledgementDetail().getText().getValue());
		resMap.put("flagEhr", "");// 健康档案建立标志

		/**
		 * 性别分类代码-平台GB/T 2261.1 0 未知的性别 1 男性 2 女性 9 未说明的性别
		 */
		String codeSex = event.getSubject1().getPatient().getPatientPerson().getAdministrativeGenderCode().getCode();
		String dtSex = qryDefdocdata("000000", codeSex);
		// 性别
		resMap.put("dtSex", dtSex);
		resMap.put("nameSex", event.getSubject1().getPatient().getPatientPerson().getAdministrativeGenderCode().getDisplayName().getValue());
		resMap.put("birthday", event.getSubject1().getPatient().getPatientPerson().getBirthTime().getValue());
		//resMap.put("birthDate",dateTrans(event.getSubject1().getPatient().getPatientPerson().getBirthTime().getValue()));
		/**
		 * GB/T 2261.2-2003 10 未婚 20 已婚 21 初婚 22 再婚 23 复婚 30 丧偶 40 离婚 90
		 * 未说明的婚姻状况
		 */
		String marrCode = event.getSubject1().getPatient().getPatientPerson()
				.getMaritalStatusCode().getCode();
		String dtMarry = qryDefdocdata("000006", marrCode);
		resMap.put("dtMarry", dtMarry);
		// 婚姻状况
		resMap.put("marriageCode", event.getSubject1().getPatient().getPatientPerson().getMaritalStatusCode().getCode());
		resMap.put("marriageName", event.getSubject1().getPatient().getPatientPerson().getMaritalStatusCode().getDisplayName().getValue());

		/**
		 * GB/T 6565-2009 0 国家机机关、党群组织、企业、事业单位负贵人 1 专业技术人员 3 办事人员和有关人员 4
		 * 商业、服务业人员 5 农、林、牧、渔、水利业生产人员 6 生产、运输设备操作人员及有关人员 X 军人 Y 不便分类的其他从业人员
		 */
		String occuCode = event.getSubject1().getPatient().getPatientPerson().getAsEmployee().getOccupationCode().getCode();
		String dtOccu = qryDefdocdata("000010", occuCode);
		resMap.put("dtOccu", dtOccu);// 职业编码
		// 职业类别代码
		resMap.put("occcuCode", event.getSubject1().getPatient().getPatientPerson().getAsEmployee().getOccupationCode().getCode());
		resMap.put("occcuName", event.getSubject1().getPatient().getPatientPerson().getAsEmployee().getOccupationCode().getDisplayName().getValue());

		resMap.put("dtEdu", "");// 学历编码
		resMap.put("dtCountry", "");// 国籍编码
		resMap.put("dtNation", "");// 民族编码
		resMap.put("telNo", event.getSubject1().getPatient().getPatientPerson().getTelecom().getItem().getValue());// 电话号码
		resMap.put("mobile", "");// 手机号码
		resMap.put("wechatNo", "");// 微信号码
		resMap.put("email", "");// 邮箱地址
		String ralaCode = event.getSubject1().getPatient().getPatientPerson().getPersonalRelationship().getCode().getCode();
		String dtRalation = qryDefdocdata("000013", ralaCode);
		resMap.put("dtRalation", dtRalation);
		resMap.put("nameRel", event.getSubject1().getPatient().getPatientPerson().getPersonalRelationship().getCode().getDisplayName().getValue());
		// 联系人电话
		resMap.put("telRel", event.getSubject1().getPatient().getPatientPerson().getPersonalRelationship().getTelecom().getItem().getValue());
		
		String natCode=event.getSubject1().getPatient().getPatientPerson().getEthnicGroupCode().getItem().getCode();
		String dtNation=qryDefdocdata("000003", natCode);
		resMap.put("dtNation", dtNation);
		// 使用医保
		resMap.put("codeHpType", event.getSubject1().getPatient().getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().getCode().getCode());
		resMap.put("nameHpType", event.getSubject1().getPatient().getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().getCode().getDisplayName().getValue());
		
		String codeHp=event.getSubject1().getPatient().getCoveredPartyOf().getCoverageRecord().getBeneficiary().getBeneficiary().getCode().getCode();
		String pkHp=qryEmpiHpRelHis(codeHp);
		resMap.put("pkHp",pkHp);
		// 登记人
		resMap.put("creator", UserContext.getUser().getPkEmp());
		resMap.put("createTime", new Date());
		resMap.put("ts", new Date());
		// 联系地址 -非结构化地址
		List<Part> parts = event.getSubject1().getPatient().getPatientPerson().getAddr().getItem().getParts();
		if (parts != null && parts.size() > 0) {
			for (Part part : parts) {
				resMap.put(part.getType(), part.getValue());
			}
		}
		// 民族
		resMap.put("nationName", event.getSubject1().getPatient().getPatientPerson().getEthnicGroupCode().getItem().getDisplayName().getValue());
		// 工作单位名称
		resMap.put("workAdd", event.getSubject1().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().getName().getItem().getPart().getValue());
		// 工作联系电话
		resMap.put("workTel", event.getSubject1().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().getContactParty().getTelecom().getItem().getValue());
		// 健康卡号 城乡居民健康档案编号等
		List<AsOtherIDs> asOtherIDs = event.getSubject1().getPatient().getPatientPerson().getAsOtherIDs();
		if (asOtherIDs != null && asOtherIDs.size() > 0) {
			for (AsOtherIDs asOtherID : asOtherIDs) {
				String root = asOtherID.getId().getItem().getRoot();
				if (StringUtils.isNotBlank(root)
						&& root.equals("2.16.156.10011.1.19")) {
					resMap.put("hicNo", asOtherID.getId().getItem().getExtension());
					resMap.put("hicWork", asOtherID.getScopingOrganization().getId().getItem().getExtension());
				} else if (StringUtils.isNotBlank(root)&& root.equals("2.16.156.10011.1.2")) {
					resMap.put("ehrNo", asOtherID.getId().getItem().getExtension());
					resMap.put("ehrWork", asOtherID.getScopingOrganization().getId().getItem().getExtension());
				}
			}
		}
		resMapList.add(resMap);
		return resMapList;
    }
    
    /**
     * 处理empi非标准返回的节点信息
     * @param inputXml
     * @return
     */
    private HipEmpi doXmlRetInfo(String inputXml){
    	inputXml=inputXml.replaceAll("</soapenv:Body></soapenv:Envelope>", "");
    	String head =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Body>";
    	inputXml = inputXml.substring(head.length());
    	return (HipEmpi)XmlUtil.XmlToBean(inputXml, HipEmpi.class);
    }
    
    /**
     * 平台数据接口码表数据转换 -平台对应编码转换为his对应编码
     * @param codeList his码表父级分类
     * @param code 平台对应编码
     * @return
     */
    private String qryDefdocdata(String codeList,String code){
    	if(StringUtils.isBlank(code))return "";
    	String sql="select code from bd_defdoc where code_defdoclist=? and code_std=? and del_flag='0'";
    	String resCode="";
    	Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, new Object[]{codeList,code});
    	if(resMap!=null){
    		if(resMap.get("code")!=null){
    			resCode=resMap.get("code").toString();
    		}else{
    			resCode="99";
    		}
    	}
    	return resCode;
    }
    
    /**
     * empi系统和his系统关于医保计划的同步处理
     * @param codeHp 平台医保编码
     * @return
     */
    private String qryEmpiHpRelHis(String codeHp){
    	String sql="select pk_hp from bd_hp where code=? and del_flag='0'";
    	String resCode="";
    	Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, new Object[]{codeHp});
    	if(resMap!=null&&resMap.get("pkHp")!=null){
    		resCode=resMap.get("pkHp").toString();
    	}
    	return resCode;
    }
}
