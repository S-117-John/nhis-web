package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.ExamApply;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.EventElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.OrganizationElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ReceiverElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SenderElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ServiceElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareNameElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareProviderElement;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;

public class ExamApplyImpl implements Message {

    private ExamApply examApply;

    public ExamApply getExamApply() {
        return examApply;
    }

    public void setExamApply(ExamApply examApply) {
        this.examApply = examApply;
    }

    public ExamApplyImpl(ExamApply examApply) {
        this.examApply = examApply;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("Control")&&param.get("Control")!=null&& !StringUtils.isEmpty(param.get("Control").toString())){

            //基础数据待维护  后期编写
            if("NW".equalsIgnoreCase(param.get("Control").toString())){
                //新增
                serviceCode = "S0041";serviceName = "检查申请信息新增服务";eventCode = "E004101";eventName = "开立放射申请";
                if("0201".equals(examApply.getExamCategoryCode())||"0202".equals(examApply.getExamCategoryCode())||"0219".equals(examApply.getExamCategoryCode())){
                    serviceCode = "S0041";serviceName = "检查申请信息新增服务";eventCode = "E004101";eventName = "开立放射申请";
                }
                if("0207".equals(examApply.getExamCategoryCode())||"0208".equals(examApply.getExamCategoryCode())){
                    serviceCode = "S0041";serviceName = "检查申请信息新增服务";eventCode = "E004302";eventName = "开立超声申请";
                }
                if("06".equals(examApply.getExamCategoryCode())){//0209
                    serviceCode = "S0041";serviceName = "检查申请信息新增服务";eventCode = "E004105";eventName = "开立电生理申请";
                    examApply.setExamCategoryName("心电图");
                }
                if("0213".equals(examApply.getExamCategoryCode())||"0217".equals(examApply.getExamCategoryCode())){
                    serviceCode = "S0041";serviceName = "检查申请信息新增服务";eventCode = "E004103";eventName = "开立内镜申请";
                }
            }else if("RU".equalsIgnoreCase(param.get("Control").toString())) {
                //更新
                serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004201";eventName = "更新放射申请";
                if("0201".equals(examApply.getExamCategoryCode())||"0202".equals(examApply.getExamCategoryCode())||"0219".equals(examApply.getExamCategoryCode())){
                    serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004201";eventName = "更新放射申请";
                }
                if("0207".equals(examApply.getExamCategoryCode())||"0208".equals(examApply.getExamCategoryCode())){
                    serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004302";eventName = "更新超声申请";
                }
                if("0209".equals(examApply.getExamCategoryCode())){
                    serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004210";eventName = "确认住院电生理申请";
                }
                if("0213".equals(examApply.getExamCategoryCode())||"0217".equals(examApply.getExamCategoryCode())){
                    serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004203";eventName = "更新内镜申请";
                }
            }
        }

        ServiceElement serviceElement  = new ServiceElement(serviceCode,serviceName);
        EventElement eventElement = new EventElement(eventCode,eventName);
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS","医院信息管理系统"),
                new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1200",
                new SoftwareNameElement("ESB","集成平台"),
                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(examApply);
        Map<String,Object> map = new HashMap<>();
        map.put("EXAM_APPLY",dataElements);
        RequestBody requestBody = new RequestBody(
                serviceElement,
                eventElement,
                senderElement,
                receiverElement,
                map
        );
        return requestBody;
    }
}
