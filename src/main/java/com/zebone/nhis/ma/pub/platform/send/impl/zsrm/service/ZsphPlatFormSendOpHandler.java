package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.EncounterBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.RequestBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.OpSendMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.ZsrmOpSchMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchApptVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.MsgIndexData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.LocalLocation;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.scm.QueCallResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.scm.QueCallVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.triage.TriageVo;
import com.zebone.nhis.ma.pub.platform.zsrm.service.ZsrmPhSettleService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.ReportResponseVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZsphPlatFormSendOpHandler {

    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private RequestTemplate sendTool;

    @Resource
    private OpSendMapper opSendMapper;

    @Resource
    private ZsrmOpSchMapper zsrmOpSchMapper;

    @Resource
    private ZsrmPhSettleService zsrmPhSettleService;

    /**
     * 门诊就诊信息发送<br>
     * 挂号/退号成功后发送
     */
    public void sendPvOpRegMsg(Map<String, Object> paramMap) {
        boolean flagAdd = EnumerateParameter.ZERO.equals(MapUtils.getString(paramMap, "isAdd", "1"));
        String pkPv = MapUtils.getString(paramMap, "pkPv");
        log.info("门诊就诊信息开始pkPv:{},flagAdd:{}", pkPv, flagAdd);
        List<PvEncounterVo> pvEncounters = opSendMapper.getPvEncounter(pkPv);
        if(CollectionUtils.isEmpty(pvEncounters)){
            throw new BusException("依据pkPv未获取到就诊信息:"+pkPv);
        }
        PvEncounterVo pvEncounterMsg = pvEncounters.get(0);
        //若是已经就诊状态，获取诊断信息
        List<PvDiag> diagList = null;
        if(StringUtils.isNotBlank(pvEncounterMsg.getEuStatus()) && Integer.valueOf(pvEncounterMsg.getEuStatus())>1){
            diagList = DataBaseHelper.queryForList("select * from PV_DIAG where PK_PV=? and del_flag='0' order by sort_no",PvDiag.class, new Object[]{pkPv});
        }
        Encounter encounter = EncounterBuild.create(pvEncounterMsg);
        BusinessBase businessBase = RequestBuild.create(encounter);
        encounter.setImplicitRules(flagAdd?"MZJZXXXZ":"MZJZXXXG");
        encounter.setDiagnosis(EncounterBuild.createDiag(diagList));//诊断
        if(flagAdd){
            Location regDept = new Location();
            regDept.setIdentifier(Lists.newArrayList(new Identifier("regdept",pvEncounterMsg.getCodeDept())));//挂号科室
            encounter.getLocation().add(new LocalLocation(regDept));
        }
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codeOp(pvEncounterMsg.getCodeOp())
                        .codePi(pvEncounterMsg.getCodePi())
                        .codePv(pvEncounterMsg.getCodePv())
                        .build())
                .remoteMethod("Encounter").build();
        log.info("门诊就诊信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),encounter.getImplicitRules());

        SendAndResolve.getInstance().send(flagAdd? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    /**
     * 预约登记发送<br>
     *     预约登记、取消预约
     * @param paramMap
     */
    public void sendPvApptRegMsg(Map<String, Object> paramMap) {
        boolean flagAdd = EnumerateParameter.ONE.equals(MapUtils.getString(paramMap, "isAdd","1"));
        String pkSchappt = MapUtils.getString(paramMap, "pkSchappt");
        String cancelReason = MapUtils.getString(paramMap, "note");
        log.info("患者预约信息开始pkSchappt:{},flagAdd:{}",pkSchappt,flagAdd);
        Map<String,Object> param = new HashMap<>();
        param.put("pkSchappt",pkSchappt);
        List<SchApptVo> apptList = opSendMapper.getSchAppt(param);
        if(CollectionUtils.isEmpty(apptList)){
            log.info("患者预约发送开始.未获取到预约信息,结束。");
            return;
        }

        SchApptVo schApptVo = apptList.get(0);
        ApptResource apptResource = getApptResource(flagAdd, cancelReason, schApptVo);
        BusinessBase businessBase = RequestBuild.create(apptResource);

        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codeOp(schApptVo.getCodeOp())
                        .codePi(schApptVo.getCodePi())
                        .build())
                .remoteMethod("Appointment").build();
        log.info("患者预约信息信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),apptResource.getImplicitRules());
        SendAndResolve.getInstance().send(flagAdd? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    /**
     * 排班信息停止,1.取消已经预约的患者   2.给平台发取消患者消息
     * <br> 启用的不做任何操作
     * @param paramMap
     */
    public void sendPvApptRegOfSchStop(Map<String,Object> paramMap) {
        if (paramMap == null) return;
        List<String> pkSchs = (List<String>) paramMap.get("pkSchs");
        if (pkSchs == null || pkSchs.size() == 0) return;
        List<SchApptVo> schApptStopList = zsrmOpSchMapper.getSchApptStopList(pkSchs);
        if(CollectionUtils.isNotEmpty(schApptStopList)){
            BusinessBase businessBase = RequestBuild.create(null);
            List<Entry> list = businessBase.getEntry();
            for (SchApptVo schApptVo : schApptStopList) {
                ApptResource apptResource = getApptResource(false, "HIS-排班停止后发送取消", schApptVo);
                list.add(new Entry(apptResource));
            }
            RequestData requestData = RequestData.newBuilder()
                    .id(businessBase.getId())
                    .data(JSON.toJSONString(businessBase))
                    .remoteMethod("Appointment").build();
            log.info("排班停止影响患者预约信息信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                    ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),"MZYYXXXG");
            SendAndResolve.getInstance().send(HttpMethod.PUT,requestData);
        }
    }

    private ApptResource getApptResource(boolean flagAdd, String cancelReason, SchApptVo schApptVo) {
        ApptResource apptResource = new ApptResource();
        apptResource.setResourceType("Encounter");
        apptResource.setImplicitRules(flagAdd?"MZYYXXXZ":"MZYYXXXG");
        //患者信息
        Patient subject = new Patient();
        subject.setResourceType("Patient");
        subject.setIdentifier(Lists.newArrayList(new Identifier("code/patientId",schApptVo.getCodeOp())
                ,new Identifier("code/idNo",schApptVo.getIdNo()==null?"":schApptVo.getIdNo())));
        subject.setName(Arrays.asList(new TextElement(schApptVo.getNamePi())));
        subject.setTelecom(Arrays.asList(new Identifier("phone",schApptVo.getMobile())));
        subject.setBirthDate(schApptVo.getBirthDate()==null?new Date():schApptVo.getBirthDate());
        subject.setActive(true);
        subject.setGender("02".equals(schApptVo.getDtSex())?"male":"03".equals(schApptVo.getDtSex())?"female":"other");//患者性别
        apptResource.setSubject(subject);
        //预约信息
        Appointment appointment = new Appointment();
        appointment.setResourceType("Appointment");
        appointment.setIdentifier(Arrays.asList(new Identifier("id/bookNo",schApptVo.getPkSchappt())));
        appointment.setStatus("1".equals(schApptVo.getFlagCancel())?"cancelled":"booked");
        if("cancelled".equals(appointment.getStatus())){
            apptResource.setImplicitRules("MZYYXXQX");
        }
        appointment.setCancelationReason(new TextElement(cancelReason));
        appointment.setDescription(schApptVo.getNameDateslot());
        appointment.setStart(DateUtils.formatDate(schApptVo.getBeginTime(),"HH:mm:ss"));
        appointment.setEnd(DateUtils.formatDate(schApptVo.getEndTime(),"HH:mm:ss"));
        appointment.setCreated(schApptVo.getCreateTime());
        appointment.setAppointmentType(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding(schApptVo.getResCode(), schApptVo.getResName())))));
        Outcome actor = new Outcome();
        actor.setResourceType("Practitioner");
        actor.setIdentifier(Arrays.asList(new Identifier("docCode",schApptVo.getCodeEmp())));
        actor.setName(new TextElement(schApptVo.getNameEmp()));
        Outcome location = new Outcome();
        location.setResourceType("Location");
        location.setIdentifier(Arrays.asList(new Identifier("deptCode",schApptVo.getCodeDept())));
        location.setName(new TextElement(schApptVo.getNameDept()));
        appointment.setActor(Lists.newArrayList(actor,location));
        Extension sch = new Extension();
        sch.setUrl("SchId");
        sch.setValueString(schApptVo.getPkSch());
        String orderidExt = schApptVo.getOrderidExt();
        String code="";
        String name = "";
        if(StringUtils.isNotBlank(orderidExt)){
            String[] paths = orderidExt.split("\\|");
            if(paths!=null) {
                int len = paths.length;
                if (len > 0) {
                    code = paths[0];
                    if (len > 1) {
                        name = paths[1];
                    }
                }
            }
        }
        Extension appointFrom = new Extension();
        appointFrom.setUrl("AppointFrom");
        appointFrom.setValueCodeableConcept(new CodeableConcept(Arrays.asList(new Coding(code,name))));
        Extension appointVisitDate = new Extension();
        appointVisitDate.setUrl("AppointVisitDate");
        appointVisitDate.setValueString(DateUtils.formatDate(schApptVo.getDateAppt(),"yyyy-MM-dd"));
        Extension remark = new Extension();
        remark.setUrl("remark");
        //诊间预约备注写在了sch_appt 的note中
        String note = schApptVo.getNote()==null?"":schApptVo.getNote();
        remark.setValueString(schApptVo.getNoteAppt()==null?note:schApptVo.getNoteAppt());
        Extension treatarea = new Extension();
        treatarea.setUrl("treatarea");
        treatarea.setValueString(schApptVo.getNameDeptArea());
        Extension treatareacode = new Extension();
        treatareacode.setUrl("treatareacode");
        treatareacode.setValueString(schApptVo.getCodeDeptArea());

        Extension specialistname = new Extension();
        specialistname.setUrl("specialistname");
        specialistname.setValueString(schApptVo.getResouName());

        Extension specialistcode = new Extension();
        specialistcode.setUrl("specialistcode");
        specialistcode.setValueString(schApptVo.getResouCode());

        Extension treatmentaddress = new Extension();
        treatmentaddress.setUrl("treatmentaddress");
        treatmentaddress.setValueString(schApptVo.getNamePlaceArea());
        appointment.setExtension(Lists.newArrayList(sch,appointFrom,appointVisitDate,remark,treatarea,treatareacode,specialistname,specialistcode,treatmentaddress));
        apptResource.setAppointment(Lists.newArrayList(appointment));
        return apptResource;
    }

    /**
     * 排班信息停止恢复发送短信给受影响的患者
     * @param paramMap
     */
    public void sendShortMsgForSchUpdate(Map<String,Object> paramMap) {
        if (paramMap == null) return;
        List<String> pkSchs = (List<String>) paramMap.get("pkSchs");
        if (pkSchs == null || pkSchs.size() == 0) return;
        List<ShortMsgSchApptInfoVo> schApptInfoVoList = zsrmOpSchMapper.getSchApptInfoList(pkSchs);
        if (schApptInfoVoList.size() > 0) {
            for (ShortMsgSchApptInfoVo schApptvo : schApptInfoVoList) {
                String telNo = "";
                if (CommonUtils.isEmptyString(schApptvo.getMobile())) {
                    telNo = schApptvo.getTelNo();
                } else {
                    telNo = schApptvo.getMobile();
                }
                if (CommonUtils.isEmptyString(telNo)) {
                    continue;
                }
                String endTime = "",beginTime = "";
                if(schApptvo.getBeginTime() != null){
                    beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(schApptvo.getBeginTime());
                }
                if(schApptvo.getEndTime() != null){
                    endTime = new SimpleDateFormat("HH:mm").format(schApptvo.getEndTime());
                }
                String dateWork = beginTime+"~"+endTime;
                String message = "";
                if ("1".equals(schApptvo.getFlagStop())) {
                    message = "温馨提示：您预约的中山市人民医院，%s，%s医生，由于%s日停诊，您的预约号已取消，敬请谅解！";
                }
                if ("0".equals(schApptvo.getFlagStop())) {
                    message = "温馨提示：您预约的中山市人民医院，%s，%s医生，%s日接诊恢复，您的预约号已正常，敬请谅解！";
                }
                message = String.format(message, new Object[]{schApptvo.getResouName(), schApptvo.getNameEmp(), dateWork});

                SchShortMsgResource shorMsgvo = new SchShortMsgResource(telNo, message);
                shorMsgvo.setImplicitRules("sendSmsNow");
                shorMsgvo.setServiceDomain("interfaceCenterTwo");
                BusinessBase businessBase = RequestBuild.create(shorMsgvo);
                RequestData requestData = RequestData.newBuilder()
                        .id(businessBase.getId())
                        .data(JSON.toJSONString(businessBase))
                        .msgIndexData(MsgIndexData.newBuilder()
                                .codeOp(schApptvo.getCodeOp())
                                .codePi(schApptvo.getCodePi())
                                .build())
                        .remoteMethod("sendSmsNow").build();
                log.info("门诊排班调整发送短信至患者构造完成id:{},data:{},remoteMethod:{},implicitRules:{}", requestData.getId(), requestData.getData(), requestData.getRemoteMethod(), shorMsgvo.getImplicitRules());

                SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
            }
        }

    }

    /**
     * 排班信息停止恢复发送短信给受影响的患者(阿里短信平台)
     * @param paramMap
     */
    public void sendNewShortMsgForSchUpdate(Map<String,Object> paramMap) {
        if (paramMap == null) return;
        List<String> pkSchs = (List<String>) paramMap.get("pkSchs");
        if (pkSchs == null || pkSchs.size() == 0) return;
        List<ShortMsgSchApptInfoVo> schApptInfoVoList = zsrmOpSchMapper.getSchApptInfoList(pkSchs);
        if (schApptInfoVoList.size() > 0) {
            for (ShortMsgSchApptInfoVo schApptvo : schApptInfoVoList) {
                String telNo = "";
                if (CommonUtils.isEmptyString(schApptvo.getMobile())) {
                    telNo = schApptvo.getTelNo();
                } else {
                    telNo = schApptvo.getMobile();
                }
                if (CommonUtils.isEmptyString(telNo)) {
                    continue;
                }
                String endTime = "",beginTime = "";
                if(schApptvo.getBeginTime() != null){
                    beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(schApptvo.getBeginTime());
                }
                if(schApptvo.getEndTime() != null){
                    endTime = new SimpleDateFormat("HH:mm").format(schApptvo.getEndTime());
                }
                String dateWork = beginTime+"~"+endTime;
                SchNewShortMsgResource shorMsgvo = new SchNewShortMsgResource();
                if ("1".equals(schApptvo.getFlagStop())) {
                    Map<String,Object> stopMap = new HashMap<>();
                    stopMap.put("patName",schApptvo.getNamePi());
                    stopMap.put("patId",schApptvo.getCodeOp());
                    stopMap.put("depName",schApptvo.getResouName());
                    stopMap.put("docName",schApptvo.getNameEmp());
                    stopMap.put("bookingTime",beginTime);
                    shorMsgvo.setImplicitRules("sendSmsNow2");
                    shorMsgvo.setServiceDomain("interfaceCenterTwo");
                    shorMsgvo.setTelNo(telNo);
                    shorMsgvo.setTemplateCode("SMS013");
                    shorMsgvo.setJsonStr(stopMap);
                }
                BusinessBase businessBase = RequestBuild.create(shorMsgvo);
                RequestData requestData = RequestData.newBuilder()
                        .id(businessBase.getId())
                        .data(JSON.toJSONString(businessBase))
                        .msgIndexData(MsgIndexData.newBuilder()
                                .codeOp(schApptvo.getCodeOp())
                                .codePi(schApptvo.getCodePi())
                                .build())
                        .remoteMethod("sendSmsNow2").build();
                log.info("门诊排班调整发送短信至患者构造完成:{},data:{},remoteMethod:{},implicitRules:{}", requestData.getId(), requestData.getData(), requestData.getRemoteMethod(), shorMsgvo.getImplicitRules());

                SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
            }
        }

    }

    /**
     *
     * 预约成功，取消预约发送短信
     * @param paramMap
     */
    public void sendShortMsgApptRegMsg(Map<String, Object> paramMap) {
        String pkSchappt = MapUtils.getString(paramMap, "pkSchappt");
        log.info("患者预约信息开始pkSchappt:{},flagAdd:{}", pkSchappt);
        Map<String, Object> param = new HashMap<>();
        param.put("pkSchappt", pkSchappt);
        List<SchApptVo> apptList = opSendMapper.getSchAppt(param);
        if (CollectionUtils.isEmpty(apptList)) {
            log.info("患者预约短信发送开始.未获取到预约信息,结束。");
            return;
        }
        SchApptVo apptVo=apptList.get(0);
        String message="";
        if(CommonUtils.isNull(apptVo.getMobile())){
            log.info("患者预约短信发送开始.未获取手机号！");
        }
        String endTime = "",beginTime = "";
        if(apptVo.getBeginTime() != null){
            beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(apptVo.getBeginTime());
        }
        if(apptVo.getEndTime() != null){
            endTime = new SimpleDateFormat("HH:mm").format(apptVo.getEndTime());
        }
        String dateWork = beginTime+"~"+endTime;
        if("9".equals(apptVo.getEuStatus())){//取消
            message="您好，您预约的中山市人民医院医生已成功取消，您无需到诊。\n" +
                    "姓名：%s\n" +
                    "医院：中山市人民医院\n" +
                    "科室：%s\n" +
                    "医生：%s\n" +
                    "就诊时间：%s\n" +
                    "备注：就诊ID号[%s]，已经成功取消预约，无需到诊";
            message = String.format(message, new Object[]{
                    apptVo.getNamePi(), apptVo.getResouName(),
                    apptVo.getNameEmp(),dateWork,
                    apptVo.getCodeOp()});

        }else{//预约
            message="您好，您已经成功预约中山市人民医院医生。请务必按时到诊。\n" +
                    "就诊人：%s\n" +
                    "就诊卡号：%s\n" +
                    "挂号科室：%s\n" +
                    "看诊医生：%s\n" +
                    "就诊时间：%s\n" +
                    "备注：就诊地点在%s，请提前15分钟到诊，若无法到诊请提前在官方微信取消预约。\n";
            message = String.format(message, new Object[]{
                    apptVo.getNamePi(),apptVo.getCodeOp(), apptVo.getResouName(), apptVo.getNameEmp(),
                    beginTime,StringUtils.isNotBlank(apptVo.getNamePlaceArea())?apptVo.getNamePlaceArea() : ""});

        }
        SchShortMsgResource shorMsgvo = new SchShortMsgResource(apptVo.getMobile(), message);
        shorMsgvo.setImplicitRules("sendSmsNow");
        shorMsgvo.setServiceDomain("interfaceCenterTwo");
        BusinessBase businessBase = RequestBuild.create(shorMsgvo);
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codeOp(apptVo.getCodeOp())
                        .codePi(apptVo.getCodePi())
                        .build())
                .remoteMethod("sendSmsNow").build();
        log.info("患者预约短信发送构造完成id:{},data:{},remoteMethod:{},implicitRules:{}", requestData.getId(), requestData.getData(), requestData.getRemoteMethod(), shorMsgvo.getImplicitRules());

        SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
    }


    /**
     *
     * 预约成功，取消预约发送短信(阿里短信平台)
     * @param paramMap
     */
    public void sendNewShortMsgApptRegMsg(Map<String, Object> paramMap) {
        String pkSchappt = MapUtils.getString(paramMap, "pkSchappt");
        log.info("患者预约信息开始pkSchappt:{},flagAdd:{}", pkSchappt);
        Map<String, Object> param = new HashMap<>();
        param.put("pkSchappt", pkSchappt);
        List<SchApptVo> apptList = opSendMapper.getSchAppt(param);
        if (CollectionUtils.isEmpty(apptList)) {
            log.info("患者预约短信发送开始.未获取到预约信息,结束。");
            return;
        }
        SchApptVo apptVo=apptList.get(0);
        if(CommonUtils.isNull(apptVo.getMobile())){
            log.info("患者预约短信发送开始.未获取手机号！");
        }
        String endTime = "",beginTime = "";
        if(apptVo.getBeginTime() != null){
            beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(apptVo.getBeginTime());
        }
        if(apptVo.getEndTime() != null){
            endTime = new SimpleDateFormat("HH:mm").format(apptVo.getEndTime());
        }
        String dateWork = beginTime+"~"+endTime;
        SchNewShortMsgResource shorMsgvo = new SchNewShortMsgResource();
        if("9".equals(apptVo.getEuStatus())){//取消
            Map<String,Object> cancelMap = new HashMap<>();
            cancelMap.put("patName",apptVo.getNamePi());
            cancelMap.put("patId",apptVo.getCodeOp());
            cancelMap.put("depName",apptVo.getResouName());
            cancelMap.put("docName",apptVo.getNameEmp());
            cancelMap.put("bookingTime",beginTime);
            //cancelMap.put("address",StringUtils.isNotBlank(apptVo.getNamePlaceArea())?apptVo.getNamePlaceArea() : "");
            shorMsgvo.setImplicitRules("sendSmsNow2");
            shorMsgvo.setServiceDomain("interfaceCenterTwo");
            shorMsgvo.setTelNo(apptVo.getMobile());
            shorMsgvo.setTemplateCode("SMS012");
            shorMsgvo.setJsonStr(cancelMap);
        }else{//预约
            Map<String,Object> apptMap = new HashMap<>();
            apptMap.put("patName",apptVo.getNamePi());
            apptMap.put("patId",apptVo.getCodeOp());
            apptMap.put("depName",apptVo.getResouName());
            apptMap.put("docName",apptVo.getNameEmp());
            apptMap.put("bookingTime",beginTime);
            apptMap.put("address",StringUtils.isNotBlank(apptVo.getNamePlaceArea())?apptVo.getNamePlaceArea() : "");
            shorMsgvo.setImplicitRules("sendSmsNow2");
            shorMsgvo.setServiceDomain("interfaceCenterTwo");
            shorMsgvo.setTelNo(apptVo.getMobile());
            shorMsgvo.setTemplateCode("SMS002");
            shorMsgvo.setJsonStr(apptMap);
        }
        BusinessBase businessBase = RequestBuild.create(shorMsgvo);
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codeOp(apptVo.getCodeOp())
                        .codePi(apptVo.getCodePi())
                        .build())
                .remoteMethod("sendSmsNow2").build();
        log.info("患者预约短信发送构造完成id:{},data:{},remoteMethod:{},implicitRules:{}", requestData.getId(), requestData.getData(), requestData.getRemoteMethod(), shorMsgvo.getImplicitRules());

        SendAndResolve.getInstance().send(HttpMethod.POST, requestData);

    }

    /**
     * 给指定日期加上天数
     * @param num
     * @param newDate
     * @return
     */
    public Date plusDay(int num,Date newDate) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date  currdate = format.parse(newDate);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        newDate = ca.getTime();
        return newDate;
    }
    /**
     *
     * 预约定时任务发送(阿里短信平台)
     */
    public void sendNewTastShortMsgApptRegMsg() {
        Map<String, Object> param = new HashMap<>();
        Date  date=null;
        try {
           date =  plusDay(1,new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        param.put("begindata", DateUtils.getDateStr(date)+"000000");
        param.put("enddata", DateUtils.getDateStr(date)+"235959");
        List<SchApptVo> apptList = opSendMapper.getSchAppt(param);
        if (CollectionUtils.isEmpty(apptList)) {
            log.info("患者预约短信发送开始.未获取到预约信息,结束。");
            return;
        }
        for(SchApptVo apptVo :apptList){
            if(CommonUtils.isNull(apptVo.getMobile())){
                log.info("患者预约短信发送开始.未获取手机号！");
            }
            String beginTime = "";
            if(apptVo.getBeginTime() != null){
                beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(apptVo.getBeginTime());
            }
            SchNewShortMsgResource shorMsgvo = new SchNewShortMsgResource();
            if(!"9".equals(apptVo.getEuStatus())){//取消
                Map<String,Object> apptMap = new HashMap<>();
                apptMap.put("patName",apptVo.getNamePi());
                apptMap.put("patId",apptVo.getCodeOp());
                apptMap.put("depName",apptVo.getResouName());
                apptMap.put("docName",apptVo.getNameEmp());
                apptMap.put("bookingTime",beginTime);
                apptMap.put("address",StringUtils.isNotBlank(apptVo.getNamePlaceArea())?apptVo.getNamePlaceArea() : "");
                shorMsgvo.setImplicitRules("sendSmsNow2");
                shorMsgvo.setServiceDomain("interfaceCenterTwo");
                shorMsgvo.setTelNo(apptVo.getMobile());
                shorMsgvo.setTemplateCode("SMS002");
                shorMsgvo.setJsonStr(apptMap);
            }
            BusinessBase businessBase = RequestBuild.create(shorMsgvo);
            RequestData requestData = RequestData.newBuilder()
                    .id(businessBase.getId())
                    .data(JSON.toJSONString(businessBase))
                    .msgIndexData(MsgIndexData.newBuilder()
                            .codeOp(apptVo.getCodeOp())
                            .codePi(apptVo.getCodePi())
                            .build())
                    .remoteMethod("sendSmsNow2").build();
            log.info("患者预约短信发送构造完成id:{},data:{},remoteMethod:{},implicitRules:{}", requestData.getId(), requestData.getData(), requestData.getRemoteMethod(), shorMsgvo.getImplicitRules());

            SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
        }
    }

    /**
     * 短信手机验证
     * @param paramMap
     */
    public void sendShortMsgForCheckPhone(Map<String,Object> paramMap) {
        if (paramMap == null || CommonUtils.isNull(MapUtils.getString(paramMap,"phone")) ||CommonUtils.isNull(MapUtils.getString(paramMap,"checkCode"))) return;
        String telNo = MapUtils.getString(paramMap,"phone");
        String minute = MapUtils.getString(paramMap,"minute");
        String checkCode = MapUtils.getString(paramMap,"checkCode");
        SchShortMsgResource shorMsgvo = new SchShortMsgResource(telNo, minute,checkCode);
        shorMsgvo.setImplicitRules("sendSmsCheck");
        shorMsgvo.setServiceDomain("interfaceCenterTwo");

        BusinessBase businessBase = RequestBuild.create(shorMsgvo);
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .remoteMethod("sendSmsCheck").build();
        log.info("患者注册手机认证构造完成id:{},data:{},remoteMethod:{},implicitRules:{}", requestData.getId(), requestData.getData(), requestData.getRemoteMethod(), shorMsgvo.getImplicitRules());

        SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
    }

    public void sendCnOpCall(Map<String,Object> paramMap){
        String pkPv = MapUtils.getString(paramMap, "pkPv");
        String userId = MapUtils.getString(paramMap, "userId");
        String type = MapUtils.getString(paramMap, "type");
        if(StringUtils.isBlank(pkPv)){
            throw new BusException("未传入pkPv");
        }
        List<PvEncounterVo> pvEncounters = opSendMapper.getPvEncounter(pkPv);
        if(CollectionUtils.isEmpty(pvEncounters)){
            throw new BusException("依据pkPv未获取到就诊信息:"+pkPv);
        }
        PvEncounterVo encounterVo = pvEncounters.get(0);
        Map<String, Object> queMap = DataBaseHelper.queryForMap("select sortno from PV_QUE where PK_PV=?", new Object[]{encounterVo.getPkPv()});
        if(MapUtils.isEmpty(queMap)){
            throw new BusException("依据pkPv未获取到分诊签到队列信息:"+pkPv);
        }
        Map<String, Object> doctorMap = DataBaseHelper.queryForMap("select emp.CODE_EMP,emp.NAME_EMP from BD_OU_USER usr inner join BD_OU_EMPLOYEE emp on usr.PK_EMP=emp.PK_EMP where PK_USER=?",
                new Object[]{userId});
        Map<String, Object> cardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where PK_PI=? and FLAG_ACTIVE='1' and DEL_FLAG='0'",
                new Object[]{encounterVo.getPkPi()});
        String operationType=null;
        if(StringUtils.equals(type,EnumerateParameter.ONE)){
            operationType = "叫号";
        } else if(StringUtils.equals(type,EnumerateParameter.TWO)){
            operationType = "重呼";
        } else if(StringUtils.equals(type,EnumerateParameter.THREE)){
            operationType = "过号";
        }
        TriageVo triageVo = new TriageVo();
        triageVo.setImplicitRules("FZZTXZ");
        triageVo.setIdNo(encounterVo.getIdNo());
        triageVo.setCardNo(MapUtils.getString(cardMap,"cardNo","0000"));
        triageVo.setPatientId(encounterVo.getCodeOp());
        triageVo.setPatientName(encounterVo.getNamePi());
        triageVo.setSex("02".equals(encounterVo.getDtSex())?"男":"03".equals(encounterVo.getDtSex())?"女":"未知");
        triageVo.setCardType("就诊卡");
        triageVo.setPhoneNumber(encounterVo.getTelNo());
        triageVo.setRegisterTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date()));
        triageVo.setDeptCode(encounterVo.getCodeDept());
        triageVo.setDeptName(encounterVo.getNameDept());
        triageVo.setDoctorCode(MapUtils.getString(doctorMap,"codeEmp"));
        triageVo.setDoctorName(MapUtils.getString(doctorMap,"nameEmp"));
        triageVo.setMarkDesc(TransfTool.getSrvText(encounterVo.getEuSrvtype()));
        triageVo.setCurrentCallNum(MapUtils.getString(queMap,"sortno"));
        triageVo.setClinicType(TransfTool.getSrvText(encounterVo.getEuSrvtype()));
        triageVo.setOperationTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date()));
        triageVo.setOperationType(operationType);
        triageVo.setIpAdress(MapUtils.getString(paramMap,"ipAddress","0.0.0.0.0"));
        BusinessBase businessBase = RequestBuild.create(triageVo);
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codePi(encounterVo.getCodePi())
                        .codeOp(encounterVo.getCodeOp())
                        .codePv(encounterVo.getCodePv())
                        .build())
                .remoteMethod("TriageStatus").build();
        log.info("推送分诊状态（叫号，过号，重呼，签到）id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),triageVo.getImplicitRules());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    public void sendConfirmDosage(Map<String, Object> paramMap) {
        List<String> pkPresoccsList = (List<String>) MapUtils.getObject(paramMap,"pkPresoccs");
        if(CollectionUtils.isEmpty(pkPresoccsList)){
            throw new BusException("未传入pkPresoccs集合");
        }
        //0 上屏  |  1 下屏|2 取消配药下屏（代表还可以上屏）
        String opType = MapUtils.getString(paramMap,"opType");
        if(StringUtils.isBlank(opType)){
            throw new BusException("未传入opType");
        }
        List<QueCallVo> queCallVoList = opSendMapper.getDisposeInfo(paramMap);
        QueCallResource resource = new QueCallResource();
        resource.setResourceType("resource");
        resource.setImplicitRules("YFPDJH");
        resource.setOccurrenceDateTime(new Date());
        resource.setQueCallVos(queCallVoList);
        BusinessBase businessBase = RequestBuild.create(resource);

        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(ZsphMsgUtils.writeValueAsString(businessBase))
                .remoteMethod("PharmacyQueue").build();
        log.info("推送药房排队叫号接口id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),resource.getImplicitRules());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * 入院申请发送消息
     * @param paramMap
     */
    public void sendOpToIpMsg(Map<String, Object> paramMap) {
        String pkPv = MapUtils.getString(paramMap, "pkPvOp");
        String state = MapUtils.getString(paramMap, "state");
        boolean flagAdd = StringUtils.isBlank(MapUtils.getString(paramMap, "pkInNotice"));
        if(StringUtils.isBlank(pkPv)){
            throw new BusException("未传入pkPv");
        }
        OpPatiInfo opPatiInfo = opSendMapper.qryPatiOpInfo(pkPv);
        if(opPatiInfo == null){
            throw new BusException("依据pkPv未获取到就诊信息:"+pkPv);
        }
       /* Map<String,Object> bdTrm = DataBaseHelper.queryForMap("select diagcode code,diagname name from BD_TERM_DIAG where pk_diag = '"+opPatiInfo.getPkDiagMaj()+"'");
        if(bdTrm == null){
            bdTrm = DataBaseHelper.queryForMap("select code_cd code,name_cd name from BD_CNDIAG where pk_cndiag = '"+opPatiInfo.getPkDiagMaj()+"'");
        }*/
        List<Map<String,Object>> bdTrm  = opSendMapper.qryDaigByPk(opPatiInfo.getPkDiagMaj());
        IpApplyVo ipApplyVo = new IpApplyVo();
        ipApplyVo.setResourceType("funcno");
        ipApplyVo.setImplicitRules(flagAdd ? "submitRequestxz" : "submitRequestxg");
        ipApplyVo.setAddress(opPatiInfo.getAddrCurDt() == null ? "" : opPatiInfo.getAddrCurDt());
        ipApplyVo.setAdmissionMethod(opPatiInfo.getDtWayIp() == null ? "" : opPatiInfo.getDtWayIp());
        if ("0".equals(opPatiInfo.getFlagChgDept())) {
            ipApplyVo.setAdmissionOptions("1");
        } else {
            ipApplyVo.setAdmissionOptions("2");
        }
        ipApplyVo.setHisOrderId(opPatiInfo.getCodePv());
        ipApplyVo.setApplyAdmissionTime(opPatiInfo.getDateAdmit() == null ? DateUtils.dateToStr("yyyy-MM-dd", new Date()) : DateUtils.dateToStr("yyyy-MM-dd", opPatiInfo.getDateAdmit()));
        ipApplyVo.setApplyDepartCode(opPatiInfo.getCodeDeptOp() == null ? "" : opPatiInfo.getCodeDeptOp());
        ipApplyVo.setApplyDepartName(opPatiInfo.getNameDeptOp() == null ? "" : opPatiInfo.getNameDeptOp());
        Map<String, Object> doctorMap = DataBaseHelper.queryForMap("select CODE_EMP,NAME_EMP from  BD_OU_EMPLOYEE   where PK_EMP=?",
                new Object[]{opPatiInfo.getPkEmpOp()});
        ipApplyVo.setApplyDoctorJobNum(MapUtils.getString(doctorMap, "codeEmp", ""));
        ipApplyVo.setApplyDoctorName(MapUtils.getString(doctorMap, "nameEmp", ""));
        ipApplyVo.setApplyTime(opPatiInfo.getTs() == null ? null : DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", opPatiInfo.getTs()));
        ipApplyVo.setContactTel(opPatiInfo.getTelRel() == null ? "" : opPatiInfo.getTelRel());
        if (bdTrm != null && bdTrm.size() > 0) {
            ipApplyVo.setDiagnosisCode(MapUtils.getString(bdTrm.get(0), "code", ""));
            ipApplyVo.setDiagnosisName(MapUtils.getString(bdTrm.get(0), "name", ""));
        }else{
            ipApplyVo.setDiagnosisCode("");
            ipApplyVo.setDiagnosisName("");
        }

        ipApplyVo.setEmergencyContact(opPatiInfo.getNameRel()==null?"":opPatiInfo.getNameRel());
        ipApplyVo.setHisOutPatientId(opPatiInfo.getCodeOp());
        ipApplyVo.setHisOutPatientTimes("1");
        ipApplyVo.setHisPatientId(opPatiInfo.getCodeOp());
        ipApplyVo.setHospitalId(opPatiInfo.getCodeOrg());
        ipApplyVo.setIsGuard(opPatiInfo.getFlagIcu());
        ipApplyVo.setIsIsolation(opPatiInfo.getFlagIsolate());
        ipApplyVo.setPatientAge(opPatiInfo.getAgePv()==null?"":opPatiInfo.getAgePv());
        ipApplyVo.setPatientBirth(opPatiInfo.getBirthDate()==null?null:DateUtils.dateToStr("yyyy-MM-dd",opPatiInfo.getBirthDate()));
        ipApplyVo.setPatientIdCard(opPatiInfo.getIdNo()==null?"":opPatiInfo.getIdNo());
        if("1".equals(opPatiInfo.getFlagSpec())){//是否收治特诊 0 普通1   1.特诊2-vip
            ipApplyVo.setPatientLevelNo("2");
        }else{
            ipApplyVo.setPatientLevelNo("1");
        }

        ipApplyVo.setPatientName(opPatiInfo.getNamePi()==null?"":opPatiInfo.getNamePi());
        ipApplyVo.setPatientSex(opPatiInfo.getSexCode()==null?"":opPatiInfo.getSexCode());
        ipApplyVo.setPatientSource("门诊");
        ipApplyVo.setPatientTypeNo(opPatiInfo.getDtPatCls()==null?"":opPatiInfo.getDtPatCls());
        ipApplyVo.setRemark(opPatiInfo.getNote()==null?"":opPatiInfo.getNote());
        ipApplyVo.setRequestBedNum(opPatiInfo.getBedNo()==null?"":opPatiInfo.getBedNo());
        String telNoRel=opPatiInfo.getTelRel()==null?"":opPatiInfo.getTelRel();
        ipApplyVo.setPatientTel(opPatiInfo.getMobile()==null?telNoRel:opPatiInfo.getMobile());
        ipApplyVo.setRequestDepartCode(opPatiInfo.getCodeDept()==null?"":opPatiInfo.getCodeDept());
        ipApplyVo.setRequestDepartName(opPatiInfo.getNameDept()==null?"":opPatiInfo.getNameDept());
        ipApplyVo.setCovid19Check(opPatiInfo.getFlagCovidCheck());
        /** 1-是 2-否 */
        if("0".equals(opPatiInfo.getFlagCovidCheck())){
            ipApplyVo.setCovid19Check("2");
        }else if("1".equals(opPatiInfo.getFlagCovidCheck())){
            ipApplyVo.setCovid19Check("1");
        }else{
            ipApplyVo.setCovid19Check("");
        }
        ipApplyVo.setCovid19Date(opPatiInfo.getDateCovid()==null?"":DateUtils.dateToStr("yyyy-MM-dd",opPatiInfo.getDateCovid()));
        ipApplyVo.setCovid19Result(opPatiInfo.getEuResultCovid()==null?"":opPatiInfo.getEuResultCovid());
        ipApplyVo.setRequestWardCode(opPatiInfo.getWardCode());
        ipApplyVo.setRequestWardName(opPatiInfo.getWardName());
        if("9".equals(state)){
            ipApplyVo.setStatus("9");
        }else{
            ipApplyVo.setStatus("1");
        }
        BusinessBase businessBase = RequestBuild.create(ipApplyVo);
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codePi(opPatiInfo.getCodePi())
                        .codeOp(opPatiInfo.getCodeOp())
                        .codePv(opPatiInfo.getCodePv())
                        .build())
                .remoteMethod("submitRequest").build();
        log.info("推送入院通知成功，id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),ipApplyVo.getImplicitRules());
        SendAndResolve.getInstance().send(flagAdd? HttpMethod.POST:HttpMethod.PUT,requestData);

    }

    /**
     * 发送微信预约消息
     *     预约成功、取消预约
     * @param paramMap
     */
    public void sendWeChatApptRegMsg(Map<String, Object> paramMap) {
        String pkSchappt = MapUtils.getString(paramMap, "pkSchappt");
        log.info("患者预约信息开始pkSchappt:{},flagAdd:{}",pkSchappt);
        Map<String,Object> param = new HashMap<>();
        param.put("pkSchappt",pkSchappt);
        List<SchApptVo> apptList = opSendMapper.getSchAppt(param);
        if(CollectionUtils.isEmpty(apptList)){
            log.info("患者预约微信模板发送开始.未获取到预约信息,结束。");
            return;
        }
        SchApptVo schApptVo = apptList.get(0);
        WeChatResource weChatResource = null;

        if("9".equals(schApptVo.getEuStatus())){//取消
             weChatResource = getWeChatApptCancelResource(schApptVo);

        }else{//预约
             if(isDeptTicket(schApptVo)){
                 weChatResource = getWeChatApptResourceForDept(schApptVo);
             }else {
                 weChatResource = getWeChatApptResource(schApptVo);
             }
        }
        BusinessBase businessBase = RequestBuild.create(weChatResource);

        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder()
                        .codePi(schApptVo.getCodePi())
                        .codeOp(schApptVo.getCodeOp())
                        .build())
                .remoteMethod("Appointment").build();
        log.info("患者预约信息信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }


    /**
     * 发送微信消息
     *     排版停止
     * @param paramMap
     */
    public void sendWeChatApptStopRegMsg(Map<String, Object> paramMap) {
        if (paramMap == null) return;
        List<String> pkSchs = (List<String>) paramMap.get("pkSchs");
        if (pkSchs == null || pkSchs.size() == 0) return;
        List<ShortMsgSchApptInfoVo> schApptInfoVoList = zsrmOpSchMapper.getSchApptInfoList(pkSchs);
        BusinessBase businessBase = RequestBuild.create(null);
        List<Entry> list = businessBase.getEntry();
        if (schApptInfoVoList.size() > 0) {
            for (ShortMsgSchApptInfoVo schApptvo : schApptInfoVoList) {
                if ("1".equals(schApptvo.getFlagStop())) {
                    WeChatResource weChatResource = getWeChatApptStopResource(schApptvo);
                    list.add(new Entry(weChatResource));
                }
            }
        }
        if(list.size()==1){
            return;
        }
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .remoteMethod("Appointment").build();
        log.info("患者预约信息信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    private WeChatResource getWeChatApptResource(SchApptVo schApptVo) {
        WeChatResource weChatResource = new WeChatResource();
        weChatResource.setImplicitRules("TemplateMsg");
        DataJson dataJson = new DataJson();
        dataJson.setBusinessType("A1");
        dataJson.setPatientId(schApptVo.getCodeOp());
        dataJson.setDataFirst("您好，您已经成功预约中山市人民医院医生。请务必按时到诊。");
        dataJson.setDataRemark("就诊地点在"+schApptVo.getNamePlaceArea()==null?"":schApptVo.getNamePlaceArea()+"，分诊到诊，若无法到诊请提前在官方微信取消预约。");
        dataJson.setRedirectUri("");
        KeyWords keyWords = new KeyWords();
        keyWords.setKeyword1(new Identifiers(schApptVo.getNamePi()));
        keyWords.setKeyword2(new Identifiers(schApptVo.getCodeOp()));
        keyWords.setKeyword3(new Identifiers(schApptVo.getResouName()));
        keyWords.setKeyword4(new Identifiers(schApptVo.getNameEmp()));
        String endTime = "",beginTime = "";
        if(schApptVo.getBeginTime() != null){
            beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(schApptVo.getBeginTime());
        }
        if(schApptVo.getEndTime() != null){
            endTime = new SimpleDateFormat("HH:mm").format(schApptVo.getEndTime());
        }
        keyWords.setKeyword5(new Identifiers(beginTime+"~"+endTime));
        dataJson.setKeyWords(keyWords);
        weChatResource.setDataJson(dataJson);
        return weChatResource;
    }
    private WeChatResource getWeChatApptResourceForDept(SchApptVo schApptVo) {
        WeChatResource weChatResource = new WeChatResource();
        weChatResource.setImplicitRules("TemplateMsg");
        DataJson dataJson = new DataJson();
        dataJson.setBusinessType("A1");
        dataJson.setPatientId(schApptVo.getCodeOp());
        dataJson.setDataFirst("您好，您已经成功登记在中山市人民医院"+schApptVo.getNameDept()+"。");
        dataJson.setDataRemark("日期和时间若有另外的通知，请以另外通知为准。");
        dataJson.setRedirectUri("");
        KeyWords keyWords = new KeyWords();
        keyWords.setKeyword1(new Identifiers(schApptVo.getNamePi()));

        keyWords.setKeyword2(new Identifiers(schApptVo.getCodeOp()));
        keyWords.setKeyword3(new Identifiers(schApptVo.getNameDept()));
        keyWords.setKeyword4(new Identifiers(schApptVo.getNameEmp()));
        String endTime = "",beginTime = "";
        if(schApptVo.getBeginTime() != null){
            beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(schApptVo.getBeginTime());
        }
        if(schApptVo.getEndTime() != null){
            endTime = new SimpleDateFormat("HH:mm").format(schApptVo.getEndTime());
        }
        keyWords.setKeyword5(new Identifiers(beginTime+"~"+endTime));
        dataJson.setKeyWords(keyWords);
        weChatResource.setDataJson(dataJson);
        return weChatResource;
    }

    /**
     * 判断是否是科室号
     * @param schApptVo
     * @return
     */
    private boolean isDeptTicket(SchApptVo schApptVo){
        if(CommonUtils.isNotNull(schApptVo.getResCode())){
            StringBuffer sql=new StringBuffer();
            sql.append("select count(1) from SCH_RESOURCE res where res.PK_EMP is null");
            sql.append(" and PK_SCHSRV is not null  and PK_DEPT_AREA is not null");
            sql.append(" and res.DT_DATESLOTTYPE is not null  and res.CODE=?");

            int count=DataBaseHelper.queryForScalar(sql.toString(),Integer.class,new Object[]{schApptVo.getResCode()});
            if(count>0){
                return true;
            }
        }
        return false;
    }
    private WeChatResource getWeChatApptCancelResource(SchApptVo schApptVo) {
        WeChatResource weChatResource = new WeChatResource();
        weChatResource.setImplicitRules("TemplateMsg");
        DataJson dataJson = new DataJson();
        dataJson.setBusinessType("A3");
        dataJson.setPatientId(schApptVo.getCodeOp());
        dataJson.setDataFirst("您好，您预约的中山市人民医院医生已成功取消，您无需到诊。");
        dataJson.setDataRemark("就诊ID号["+schApptVo.getCodeOp()+"]，已经成功取消预约，无需到诊。");
        dataJson.setRedirectUri("");
        KeyWords keyWords = new KeyWords();
        keyWords.setKeyword1(new Identifiers(schApptVo.getNamePi()));
        keyWords.setKeyword2(new Identifiers(schApptVo.getNameOrg()));
        keyWords.setKeyword3(new Identifiers(schApptVo.getNameDept()));
        keyWords.setKeyword4(new Identifiers(schApptVo.getNameEmp()));
        String endTime = "",beginTime = "";
        if(schApptVo.getBeginTime() != null){
            beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(schApptVo.getBeginTime());
        }
        if(schApptVo.getEndTime() != null){
            endTime = new SimpleDateFormat("HH:mm").format(schApptVo.getEndTime());
        }
        keyWords.setKeyword5(new Identifiers(beginTime+"~"+endTime));
        dataJson.setKeyWords(keyWords);
        weChatResource.setDataJson(dataJson);
        return weChatResource;
    }

    private WeChatResource getWeChatApptStopResource(ShortMsgSchApptInfoVo schApptVo) {
        WeChatResource weChatResource = new WeChatResource();
        weChatResource.setImplicitRules("TemplateMsg");
        DataJson dataJson = new DataJson();
        dataJson.setBusinessType("T1");
        dataJson.setPatientId(schApptVo.getCodeOp());
        dataJson.setDataFirst("您好，您预约的中山市人民医院的医生已停诊，您的预约号已被取消，请您改约！敬请谅解！");
        dataJson.setDataRemark("就诊ID号["+schApptVo.getCodeOp()+"]，您的预约号已被取消，请您改约！敬请谅解！");
        dataJson.setRedirectUri("");
        KeyWords keyWords = new KeyWords();
        keyWords.setKeyword1(new Identifiers(schApptVo.getNamePi()));
        keyWords.setKeyword2(new Identifiers(schApptVo.getNameOrg()));
        keyWords.setKeyword3(new Identifiers(schApptVo.getResouName()));
        keyWords.setKeyword4(new Identifiers(schApptVo.getNameEmp()));
        String endTime = "",beginTime = "";
        if(schApptVo.getBeginTime() != null){
            beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(schApptVo.getBeginTime());
        }
        if(schApptVo.getEndTime() != null){
            endTime = new SimpleDateFormat("HH:mm").format(schApptVo.getEndTime());
        }
        keyWords.setKeyword5(new Identifiers(beginTime+"~"+endTime));
        dataJson.setKeyWords(keyWords);
        weChatResource.setDataJson(dataJson);
        return weChatResource;
    }

    /**
     *  查询医嘱信息
     * @param param
     * @return
     */
    public Boolean sendMedicalAdviceInfo(Map<String,Object> param){
        if(param == null) throw  new BusException("未传入parameter");
        List<Map<String,Object>> mapList = (List<Map<String,Object>>)param.get("parameter");
        List<Parameter> parameterList = new ArrayList<>();
        mapList.forEach(m -> {
            Parameter parameter = new Parameter();
            parameter.setName(m.get("name").toString());
            parameter.setValueString(m.get("valueString").toString());
            parameterList.add(parameter);
        });
        Map<String,Object> paramMap = new HashMap<>();
        for (Parameter parameter : parameterList){
            paramMap.put(parameter.getName(), parameter.getValueString());
        }
        if( paramMap.get("patientNo") == null || paramMap.get("patientNo") == ""){
            throw new BusException("patientNo不能为空！");
        }
        if(paramMap.get("orderCode") == null || paramMap.get("orderCode") == ""){
            throw  new BusException("orderCode不能为空！");
        }
        BusinessBase businessBase = RequestBuild.create(null);
        List<Entry> entryList = businessBase.getEntry();
        PhResource resource = new PhResource();
        resource.setResourceType("Parameters");
        resource.setImplicitRules("getorderinfo");
        resource.setParameter(parameterList);
        businessBase.getEntry().add(new Entry(){{setResource(resource);};});
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .remoteMethod("MedicationRequest")
                .build();
        log.info("查询医嘱信息构造完成id:{},data:{},remoteMethod:{},imlicitRules:{}"
                , requestData.getId(), requestData.getData(), requestData.getRemoteMethod());
        Object obj = SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
        if (obj != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 患者诊毕推送待缴费信息模板内容
     */
    public void sendWeChatApptFinishClinic(Map<String, Object> paramMap) {
        if (paramMap == null || paramMap.get("pkPv") == null || StringUtils.isBlank(paramMap.get("pkPv").toString()))
            throw new BusException("未传入parameter");
        String pkPv = paramMap.get("pkPv").toString();
        BigDecimal bigDecimal = DataBaseHelper.queryForScalar("select case when sum(AMOUNT) is null then 0 when sum(AMOUNT) is not null then sum(AMOUNT) end as amountPi from BL_OP_DT where PK_PV = '" + pkPv + "'  and FLAG_SETTLE = '0'", BigDecimal.class);
        if (bigDecimal.compareTo(new BigDecimal(0)) == 0) {
            return;
        }
        Map<String, Object> map = DataBaseHelper.queryForMap("select pi.CODE_OP  op,pv.NAME_PI  name,pv.CODE_PV  code,bod.NAME_DEPT  dept from PV_ENCOUNTER pv inner join PI_MASTER pi on pv.PK_PI=pi.PK_PI inner join BD_OU_DEPT bod on pv.PK_DEPT = bod.PK_DEPT  where PK_PV='" + pkPv + "'", new HashMap());
        if (map == null || map.size() < 1) {
            throw new BusException("根据就诊主键未查询到相关信息");
        }
        BusinessBase businessBase = RequestBuild.create(null);
        List<Entry> list = businessBase.getEntry();
        WeChatResource weChatResource = new WeChatResource();
        weChatResource.setImplicitRules("TemplateMsg");
        DataJson dataJson = new DataJson();
        dataJson.setBusinessType("M1");
        dataJson.setPatientId(map.get("op").toString());
        dataJson.setDataFirst("您好，您今天在中山市人民医院门诊产生了新的费用，为避免后续无法就诊，请您尽快缴费。");
        dataJson.setDataRemark("请关注中山市人民医院官方微信缴费，或到门诊自助机或人工窗口缴费。");
        dataJson.setRedirectUri("");
        KeyWords keyWords = new KeyWords();
        keyWords.setKeyword1(new Identifiers(map.get("name").toString()));
        keyWords.setKeyword2(new Identifiers(map.get("op").toString()));
        keyWords.setKeyword3(new Identifiers(map.get("dept").toString()));
        keyWords.setKeyword4(new Identifiers(bigDecimal.toString()));
        dataJson.setKeyWords(keyWords);
        weChatResource.setDataJson(dataJson);
        list.add(new Entry(weChatResource));
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .remoteMethod("TemplateMsg").build();
        log.info("门诊待缴费提醒信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                , requestData.getId(), requestData.getData(), requestData.getRemoteMethod());
        System.out.println(requestData.getData());
        SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
    }

    /**
     * 结算成功后，推送结算指引信息模板内容
     */
    public void sendWeChatApptFinishSettle(Map<String, Object> paramMap) {
        String pkSettle = MapUtils.getString(paramMap, "pkSettle");
        if (StringUtils.isBlank(pkSettle)) {
            throw new BusException("pkSettle不可为空");
        }
        ReportResponseVo reportResponseVo = zsrmPhSettleService.aginPrintReport(JSONObject.toJSONString(paramMap), UserContext.getUser());
        String val = "";
        if (reportResponseVo != null) {
            List<LinkedHashMap<String, Object>> extensions = reportResponseVo.getExtensions();
            for (int i = 0; i < extensions.size(); i++) {
                val += extensions.get(i).get("valueString").toString();
            }
        }
        Map<String, Object> stringObjectMap = DataBaseHelper.queryForMap("select GET_ZHIYIN(settle.PK_SETTLE) settle, pi.CODE_OP code,pi.NAME_PI name from BL_SETTLE settle inner join PI_MASTER pi on settle.PK_PI = pi.PK_PI where PK_SETTLE='" + pkSettle + "'", new HashMap());
        String settle = MapUtils.getString(stringObjectMap, "settle");
        String code = MapUtils.getString(stringObjectMap, "code");
        String name = MapUtils.getString(stringObjectMap, "name");
        BusinessBase businessBase = RequestBuild.create(null);
        List<Entry> list = businessBase.getEntry();
        WeChatResource weChatResource = new WeChatResource();
        weChatResource.setImplicitRules("TemplateMsg");
        DataJson dataJson = new DataJson();
        dataJson.setBusinessType("M2");
        dataJson.setPatientId(code);
        dataJson.setDataFirst("您好，您的就诊ID号是" + code + ",您已缴费完成，请按下面的指引信息办理事务。");
        dataJson.setDataRemark("门诊大楼二楼西药房4号窗取药|检查预约信息" + settle);
        dataJson.setRedirectUri("");
        KeyWords keyWords = new KeyWords();
        keyWords.setKeyword1(new Identifiers(name));
        keyWords.setKeyword2(new Identifiers("缴费后事务提醒"));
        if (StringUtils.isBlank(val)) {
            dataJson.setDataRemark(settle);
        } else {
            dataJson.setDataRemark(settle + val);
        }
        keyWords.setKeyword3(new Identifiers(""));
        keyWords.setKeyword4(new Identifiers(""));
        keyWords.setKeyword5(new Identifiers(""));
        dataJson.setKeyWords(keyWords);
        weChatResource.setDataJson(dataJson);
        list.add(new Entry(weChatResource));
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .remoteMethod("TemplateMsg").build();
        log.info("门诊缴费完成提醒信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                , requestData.getId(), requestData.getData(), requestData.getRemoteMethod());
        System.out.println(requestData.getData());
        SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
    }

    /**
     * 向微信推送当天未缴费信息模板内容
     */
    public void sendWeiXinForNotPayCost(Map<String, Object> paramMap) {
        String dayString = MapUtils.getString(paramMap, "day");
        if (StringUtils.isBlank(dayString)) {
            throw new BusException("入参不能为空");
        }
        Integer day;
        try {
            day = Integer.valueOf(dayString);
        } catch (Exception e) {
            throw new BusException("入参未按照规定格式");
        }
        List<Map<String, Object>> sumAmount = opSendMapper.getSumAmount(day);
        for (Map<String, Object> map : sumAmount) {
            String codePv = MapUtils.getString(map, "codePv");
            String codeOp = MapUtils.getString(map, "codeOp");
            String namePi = MapUtils.getString(map, "namePi");
            String nameDept = MapUtils.getString(map, "nameDept");
            String amount = MapUtils.getString(map, "amount");
            BusinessBase businessBase = RequestBuild.create(null);
            List<Entry> list = businessBase.getEntry();
            WeChatResource weChatResource = new WeChatResource();
            weChatResource.setImplicitRules("TemplateMsg");
            DataJson dataJson = new DataJson();
            dataJson.setBusinessType("M1");
            dataJson.setPatientId(codeOp);
            dataJson.setDataFirst("您好，您今天在中山市人民医院门诊产生了新的费用，为避免后续无法就诊，请您尽快缴费。");
            dataJson.setDataRemark("请关注中山市人民医院官方微信缴费，或到门诊自助机或人工窗口缴费。");
            dataJson.setRedirectUri("");
            KeyWords keyWords = new KeyWords();
            keyWords.setKeyword1(new Identifiers(namePi));
            keyWords.setKeyword2(new Identifiers(codeOp));
            keyWords.setKeyword3(new Identifiers(nameDept));
            keyWords.setKeyword4(new Identifiers(amount));
            dataJson.setKeyWords(keyWords);
            weChatResource.setDataJson(dataJson);
            list.add(new Entry(weChatResource));
            RequestData requestData = RequestData.newBuilder()
                    .id(businessBase.getId())
                    .data(JSON.toJSONString(businessBase))
                    .remoteMethod("TemplateMsg").build();
            log.info("门诊待缴费提醒信息构造完成id:{},data:{},remoteMethod:{},implicitRules:{}"
                    , requestData.getId(), requestData.getData(), requestData.getRemoteMethod());
            System.out.println(requestData.getData());
            SendAndResolve.getInstance().send(HttpMethod.POST, requestData);
        }
    }
}
