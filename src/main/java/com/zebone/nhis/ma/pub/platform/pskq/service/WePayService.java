package com.zebone.nhis.ma.pub.platform.pskq.service;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.platform.pskq.model.param.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqMesUtils;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class WePayService {
	private Logger logger = LoggerFactory.getLogger("nhis.PskqWebServiceLog");

    @Autowired
    private HttpRestTemplate httpRestTemplate;

    public String getQrCode(String param, IUser user){
        try{
            String id = JsonUtil.getFieldValue(param,"id");
            if(StringUtils.isEmpty(id)) {
            	return "";
            }
            String sql = "select pi.mpi, pi.code_pi,pi.code_op,pi.id_no,pi.name_pi ,pv.eu_pvtype,pv.code_pv,NAME_EMP_TRE from PV_ENCOUNTER pv left join pi_master pi on pv.pk_pi = pi.pk_pi where pv.PK_PV = ?";
			List<Map<String,Object>> patiList = DataBaseHelper.queryForList(sql,id);
			if(patiList == null || patiList.size() <= 0) {
				return "";
			}
            QrCodeParam qrCodeParam = new QrCodeParam();
            WeiXinPayInfo weiXinPayInfo = new WeiXinPayInfo();
            NotificationInfo notificationInfo = new NotificationInfo();
            qrCodeParam.setNotificationInfo(notificationInfo);
            qrCodeParam.setNotificationType("weixinpay");
            notificationInfo.setWeiXinPayInfo(weiXinPayInfo);
            weiXinPayInfo.setUserType("WX");
            weiXinPayInfo.setHospitalId("szkq");
            weiXinPayInfo.setPatientId(CommonUtils.getPropValueStr(patiList.get(0), "codeOp"));
            weiXinPayInfo.setCardNo(PskqMesUtils.pskqHisCode + PskqMesUtils.pskqSysCode + CommonUtils.getPropValueStr(patiList.get(0), "codePi"));
            weiXinPayInfo.setName(CommonUtils.getPropValueStr(patiList.get(0), "namePi"));
            weiXinPayInfo.setClinicID(PskqMesUtils.pskqHisCode +CommonUtils.getPropValueStr(patiList.get(0), "euPvtype") + "_" + PskqMesUtils.pskqSysCode + CommonUtils.getPropValueStr(patiList.get(0), "codePv"));
            weiXinPayInfo.setIdenNo(CommonUtils.getPropValueStr(patiList.get(0), "idNo"));
            weiXinPayInfo.setRecipeSeq("");
            weiXinPayInfo.setPactCode("");
            weiXinPayInfo.setOrderDept("");
            weiXinPayInfo.setOrderDoctor(CommonUtils.getPropValueStr(patiList.get(0), "nameEmpTre"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            weiXinPayInfo.setOrderDate(simpleDateFormat.format(new Date()));
            weiXinPayInfo.setSumMoney("");
            weiXinPayInfo.setYiBaoMoney("");
            weiXinPayInfo.setPrescMoney("");
            String result = XmlUtil.beanToXml(qrCodeParam,QrCodeParam.class);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            stringBuffer.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
            stringBuffer.append("<soap:Body>");
            stringBuffer.append("<test xmlns=\"http://cxf.webservice.nhis.zebone.com/\">");
            stringBuffer.append("<param><![CDATA[");
            stringBuffer.append("result");
            stringBuffer.append("]]></param>");
            stringBuffer.append("</test>");
            stringBuffer.append("</soap:Body>");
            stringBuffer.append("</soap:Envelope>");
            String responseBody = httpRestTemplate.postForXml(result);
            responseBody = StringEscapeUtils.unescapeXml(responseBody);
            System.out.println(responseBody);

            SAXReader reader = new SAXReader();
            Document document = null;
            StringReader stringReader = new StringReader(responseBody);
            InputSource inputSource = new InputSource(stringReader);
            try {
                document = reader.read(inputSource);
            } catch (DocumentException e) {
            }
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            String resultCode = "";
            String url = "";
            while (iterator.hasNext()){
                Element child = (Element) iterator.next();
                String name = child.getName();
                if("ResultCode".equals(name)){
                    resultCode = child.getText();
                }
                if("ResponseInfo".equals(name)){
                    Iterator iterator1 = child.elementIterator();
                    while (iterator1.hasNext()){
                        Element child1 = (Element) iterator1.next();
                        if("CodeUrl".equals(child1.getName())){
                            url = child1.getText();
                        }
                    }
                }
            }

            System.out.println(resultCode);
            System.out.println(url);

//            Result resultBody = (Result) XmlUtil.XmlToBean(Result.class,responseBody);
//            if(responseBody==null){
//                return "";
//            }
            if("1".equals(resultCode)){
                return "";
            }

            return url;
        }catch (Exception e){
            return e.getMessage();
        }

    }
    /**
     * 发送待预约挂号待缴费
     */
    public void sendPayNotice(String param, IUser user){
    	try{
    		String sql = "select pi.code_pi,pi.mpi,emp.NAME_EMP,bod.name_dept,sa.* from SCH_APPT sa left join PI_MASTER pi on pi.pk_pi=sa.pk_pi "
    				+ " left JOIN sch_resource res ON sa.pk_schres = res.pk_schres AND res.eu_schclass = '0' "
    				+ " LEFT JOIN bd_ou_employee emp ON emp.pk_emp = res.pk_emp "
    				+ " left join BD_OU_DEPT bod on bod.PK_DEPT=sa.PK_DEPT_EX "
    				+ " where sa.FLAG_CANCEL='0' and sa.DEL_FLAG='0' and TO_CHAR(sa.DATE_appt,'yyyyMMdd')=? ";
			List<Map<String,Object>> list = DataBaseHelper.queryForList(sql,DateUtils.getSpecifiedDateStr(new Date(), 2));
			if(list == null || list.size() <= 0) {
				logger.info("没有查询到待发送的挂号缴费列表");
			}
			NoticeParam noticeParam = new NoticeParam();
			NoticeNotification noticeNotification = new NoticeNotification();
			CommonInfo commonInfo = new CommonInfo();
            noticeParam.setNotificationType("common");
			for (Map<String, Object> map : list) {
				commonInfo = new CommonInfo();
				commonInfo.setHospitalId("szkq");
	            commonInfo.setPatientId(CommonUtils.getPropValueStr(map, "mpi"));
	            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            commonInfo.setNoticeTime(simpleDateFormat.format(new Date())+" 16:00:00");
	            String dtastr=CommonUtils.getPropValueStr(map, "beginTime").substring(0,16)+"-"+CommonUtils.getPropValueStr(map, "endTime").substring(11,16);
	            commonInfo.setNoticeType("3");
	            //commonInfo.setContent("温馨提示:您已预约"+dtastr+","+CommonUtils.getPropValueStr(map, "nameDept")+"("+CommonUtils.getPropValueStr(map, "nameEmp")+")医生，请提前半小时到达就诊区候诊！");
                String str="您好！您已于"+CommonUtils.getPropValueStr(map, "dateReg").substring(0,10)+"成功预约\n"
                        + "科室:"+CommonUtils.getPropValueStr(map, "nameDept")+"\n"
                        +"医生:"+CommonUtils.getPropValueStr(map, "nameEmp")+"\n"
                        +"就诊时间:"+CommonUtils.getPropValueStr(map, "beginTime").substring(0,16)+"\n"
                        +"请您于就诊当天提前15分钟来院候诊!\n"
                        +"温馨提示：1.医院已启用黑名单管理，连续爽约两次的患者将禁止其在线上预约挂号，仅可通过现场窗口挂号的方式就诊!\n"
                        +"联系电话:0755-89661906/0755-89667096  ";
	            commonInfo.setContent(str);
	            commonInfo.setTitle("就诊提醒");
	            noticeNotification.setCommonInfo(commonInfo);
	            noticeParam.setNoticeNotification(noticeNotification);
	            String result = XmlUtil.beanToXml(noticeParam,NoticeParam.class);
	            String responseBody = httpRestTemplate.postForXml(result);
	            responseBody = StringEscapeUtils.unescapeXml(responseBody);
	            System.out.println(responseBody);

	            SAXReader reader = new SAXReader();
	            Document document = null;
	            StringReader stringReader = new StringReader(responseBody);
	            InputSource inputSource = new InputSource(stringReader);
	            try {
	                document = reader.read(inputSource);
	            } catch (DocumentException e) {
	            }
	            Element rootElement = document.getRootElement();
	            Iterator iterator = rootElement.elementIterator();
	            String resultCode = "";
	            String resultDesc = "";
	            while (iterator.hasNext()){
	                Element child = (Element) iterator.next();
	                String name = child.getName();
	                if("ResultCode".equals(name)){
	                    resultCode = child.getText();
	                }
	                if("ResultDesc".equals(name)){
	                	resultDesc = child.getText();
	                }
	                
	            }
	            if(!StringUtils.isEmpty(resultCode)) {
	            	if("1".equals(resultCode)){
	            		logger.info("发送预约挂号待缴费接口返回失败："+resultDesc);
	            	}
	            }
			}
			
    	}catch (Exception e){
    		logger.info("发送预约挂号待缴费列表出错："+e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 发送缴费通知推送
     */
    public void sendStayPayNotice(Map<String, Object> paramMap){
    	try{
    		if(null==paramMap){
    			return ;
    		}
    		String pkPv=CommonUtils.getPropValueStr(paramMap, "pkPv");
    		if(CommonUtils.isEmptyString(pkPv)){
    			return ;
    		}
			List<Map<String,Object>> list = DataBaseHelper.queryForList("select * from BL_OP_DT where FLAG_SETTLE='0' and PK_PV=? ",pkPv);
			if(list == null || list.size() <= 0) {
				return ;
			}
			PiMaster piMaster=DataBaseHelper.queryForBean("SELECT * FROM PI_MASTER WHERE PK_PI=(SELECT pk_pi FROM PV_ENCOUNTER WHERE PK_PV=?)", PiMaster.class, pkPv);
			NoticeParam noticeParam = new NoticeParam();
			NoticeNotification noticeNotification = new NoticeNotification();
			CommonInfo commonInfo = new CommonInfo();
            noticeParam.setNotificationType("common");
			commonInfo.setHospitalId("szkq");
            commonInfo.setPatientId(piMaster.getMpi());
            commonInfo.setNoticeTime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
            
            commonInfo.setNoticeType("0");
            noticeNotification.setCommonInfo(commonInfo);
            noticeParam.setNoticeNotification(noticeNotification);
            String result = XmlUtil.beanToXml(noticeParam,NoticeParam.class);
            String responseBody = httpRestTemplate.postForXml(result);
            responseBody = StringEscapeUtils.unescapeXml(responseBody);
            System.out.println(responseBody);

            SAXReader reader = new SAXReader();
            Document document = null;
            StringReader stringReader = new StringReader(responseBody);
            InputSource inputSource = new InputSource(stringReader);
            try {
                document = reader.read(inputSource);
            } catch (DocumentException e) {
            }
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            String resultCode = "";
            String resultDesc = "";
            while (iterator.hasNext()){
                Element child = (Element) iterator.next();
                String name = child.getName();
                if("ResultCode".equals(name)){
                    resultCode = child.getText();
                }
                if("ResultDesc".equals(name)){
                	resultDesc = child.getText();
                }
                
            }
            if(!StringUtils.isEmpty(resultCode)) {
            	if("1".equals(resultCode)){
            		logger.info("发送缴费通知接口返回失败："+resultDesc);
            	}
            }
			
    	}catch (Exception e){
    		logger.info("发送缴费通知出错："+e.getMessage());
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

    }
}
