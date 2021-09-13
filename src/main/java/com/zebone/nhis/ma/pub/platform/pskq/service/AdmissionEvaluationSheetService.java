package com.zebone.nhis.ma.pub.platform.pskq.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.ma.pub.platform.pskq.dao.AdmissionEvaluationSheetDao;
import com.zebone.nhis.ma.pub.platform.pskq.model.AdmissionEvaluationSheet;
import com.zebone.nhis.ma.pub.platform.pskq.model.SingleRiskAssessment;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 入院评估单
 *
 * @author 卡卡西
 */
@Service
public class AdmissionEvaluationSheetService {

    public String send(String pkPv) throws Exception {


        AdmissionEvaluationSheetDao dao = SpringContextHolder.getBean(AdmissionEvaluationSheetDao.class);
        String sql = "select * from PV_ENCOUNTER where PK_PV = ?";
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,pkPv);
        sql = "select * from PI_MASTER where PK_PI = ?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,pvEncounter.getPkPi());

        List<Map<String, Object>> assessmentResult = dao.singleRiskAssessment(pkPv);
        if(assessmentResult.size()>0){
            SingleRiskAssessment singleRiskAssessment = new SingleRiskAssessment();
            singleRiskAssessment.setPkPatient(piMaster.getCodePi());
            singleRiskAssessment.setEncounterId("10_2000_"+pvEncounter.getCodePv());
            singleRiskAssessment.setPatientName(pvEncounter.getNamePi());
            singleRiskAssessment.setGenderCode(pvEncounter.getDtSex());

            Set<String> valueList = assessmentResult.stream().filter(a->"总分".equals(a.get("labelName"))).map(a->MapUtils.getString(a,"labelValue")).collect(Collectors.toSet());
            //风险评估类别
            singleRiskAssessment.setAssessmentType("压疮风险评估");
            singleRiskAssessment.setAssessmentName("Braden压疮风险评估护理单");
            singleRiskAssessment.setAssessmentValue(valueList.stream().findFirst().orElse(""));
            valueList = assessmentResult.stream().filter(a->"压疮风险级别".equals(a.get("labelName"))).map(a->MapUtils.getString(a,"labelValue")).collect(Collectors.toSet());
            singleRiskAssessment.setAssessmentLevel(valueList.stream().findFirst().orElse(""));
            List<DataElement> dataElementList  = MessageBodyUtil.dataElementsFactory(singleRiskAssessment);

            RequestBody requestBody = new RequestBody.Builder()
                    .service("S2801", "风险评估单新增")
                    .event("E280101", "发布风险评估单")
                    .sender()
                    .receiver()
                    .build();
            SenderElement senderElement = new SenderElement(
                    "2000",
                    new SoftwareNameElement("HIS", "医院信息管理系统"),
                    new SoftwareProviderElement("Zebone", "江苏振邦智慧城市信息系统有限公司"),
                    new OrganizationElement("10", "南方医科大学深圳口腔医院（坪山）")
            );
            ReceiverElement receiverElement = new ReceiverElement(
                    "1200",
                    new SoftwareNameElement("ESB", "集成平台"),
                    new SoftwareProviderElement("Caradigm", "恺恩泰（北京）科技有限公司"),
                    new OrganizationElement("10", "南方医科大学深圳口腔医院（坪山）")
            );
            Map<String, Object> map = new HashMap<>(16);
            map.put("SINGLE_RISK_ASSESSMENT", dataElementList);
            requestBody.setMessage(map);
            requestBody.setSender(senderElement);
            requestBody.setReceiver(receiverElement);
            String requestString = JSON.toJSONString(requestBody);
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            String responseString = httpRestTemplate.postForString(requestString);
            ResponseBody responseBody = JsonUtil.readValue(responseString, ResponseBody.class);
        }



        return "";

    }


    public static void main(String[] args) throws DocumentException {
        //1.创建Reader对象
        SAXReader reader = new SAXReader();
        //2.加载xml
        Document document = reader.read(new File("C:/Users/卡卡西/Desktop/a.xml"));
        //3.获取根节点
        Element rootElement = document.getRootElement();
        Iterator iterator = rootElement.elementIterator();
        Map<String,Object> result = new HashMap<>(16);
        String content = "";
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if ("BodyText".equals(element.getName())) {
                content = element.getStringValue();

            }

        }



    }

}
