package com.zebone.nhis.webservice.syx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormCreateACardMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.CreateACardReq;
import com.zebone.nhis.webservice.syx.vo.platForm.CreateACardReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.CreateACardRes;
import com.zebone.nhis.webservice.syx.vo.platForm.CreateACardResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.CreateACardResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.CreateACardResSubject;
import com.zebone.platform.modules.dao.DataBaseHelper;


/**
 * 预约对帐信息查询接口
 * @author cuijiansheng 2019.7.18
 */
@Service
public class PlatFormCreateACardService {

	@Autowired
	private PlatFormCreateACardMapper mapper;
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public String createACard(String content) throws Exception {
		
		CreateACardReqSubject req= (CreateACardReqSubject)XmlUtil.XmlToBean(content, CreateACardReqSubject.class);
		
		//String sysparam = ApplicationUtils.getSysparam("PI0003", true);

		CreateACardReq createACardReq = req.getSubject().get(0);
		List<Map<String,Object>> list = mapper.createACard(createACardReq);
		//处理代码，0-成功，1-失败
		String resultCode;
		//处理结果描述
		String resultDesc;
		//患者ID
		String userHisPatientId;
		//诊疗卡号
		String patientCardNo;
		//如果HIS中不存在该注册患者，在HIS中注册患者信息
		if(list == null || list.size() == 0){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			//写表pi_master
			PiMaster piMaster = new PiMaster();
			piMaster.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
			piMaster.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
			piMaster.setIdNo(createACardReq.getUserCardId());
			piMaster.setNamePi(createACardReq.getUserName());
			piMaster.setDtSex(createACardReq.getUserGender());
			piMaster.setBirthDate(sdf.parse(createACardReq.getUserBirthday()));
			piMaster.setMobile(createACardReq.getUserMobile());
			piMaster.setAddrCurDt(createACardReq.getUserAddress());
			if(req.getSubject().get(0).getOrderType().equals("9")){
				piMaster.setCreator("9-医享网支付宝");				
			}
			if(req.getSubject().get(0).getOrderType().equals("11")){
				piMaster.setCreator("11-平安");				
			}			
			piMaster.setNote("1-平台已认证");
			String pkPicate = DataBaseHelper.queryForScalar("select pk_picate from pi_cate where flag_def='1' and del_flag='0'", String.class);
			piMaster.setPkPicate(pkPicate);
			DataBaseHelper.insertBean(piMaster);
			
			/*写表pi_card*/
			PiCard piCard = new PiCard();
			piCard.setPkPi(piMaster.getPkPi());
			piCard.setSortNo(1);
			piCard.setDtCardtype("01");
			piCard.setCardNo(createACardReq.getUserCardId());
			if(createACardReq.getBeginDate() != null && createACardReq.getBeginDate().length() != 0){				
				piCard.setDateBegin(sdf.parse(createACardReq.getBeginDate()));
			}
			if(createACardReq.getEndDate() != null && createACardReq.getEndDate().length() != 0){				
				piCard.setDateEnd(sdf.parse(createACardReq.getEndDate()));
			}
			
			piCard.setFlagActive("1");
			piCard.setEuStatus("0");
			DataBaseHelper.insertBean(piCard);
			
			resultCode = "0";
			resultDesc = "新增成功！";
			userHisPatientId = piMaster.getCodePi();
			patientCardNo = piMaster.getCodeOp();
		}else{
			resultCode = "1";
			resultDesc = "医院系统中已存在相匹配的患者记录，不允许再新增患者记录。";
			userHisPatientId = list.get(0).get("CODE_PI").toString();
			patientCardNo = list.get(0).get("CODE_OP").toString();
		}
		
		//res
		CreateACardRes res = new CreateACardRes();
		res.setResultCode(resultCode);
		res.setResultDesc(resultDesc);
		res.setUserHisPatientId(userHisPatientId);
		res.setPatientCardNo(patientCardNo);
		
		//subject
		CreateACardResSubject subject = new CreateACardResSubject();
		subject.setRes(res);
		
		//result
		CreateACardResResult result = new CreateACardResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		result.setSubject(subject);
		
		//response
		CreateACardResExd exd = new CreateACardResExd();
		exd.setResult(result);
		
		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), CreateACardResExd.class);
        return xml;
	}
}
