package com.zebone.nhis.ma.pub.platform.zsrm.service;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.zsrm.annotation.EventSearch;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

public class ZsphRouterService {

    /**
     * 中山平台-机构信息查询
     * @param param
     * @return
     */
    @EventSearch("getorganizationinfo")
    public String getBdOuOrg(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService basicDataService = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(basicDataService.getBdOuOrg(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-科室信息查询
     * @param param
     * @return
     */
    @EventSearch("getdeptinfo")
    public String getBdOuDept(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(service.getBdOuDept(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-保存新增科室信息、修改
     * @param param
     * @return
     */
    @EventSearch("MDMKSXZ,MDMKSXG")
    public String saveBdOuDept(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(service.saveBdOuDept(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-人员信息查询
     * @param param
     * @return
     */
    @EventSearch("getemployeeinfo")
    public String getPractitioner(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(service.getPractitioner(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-人员信息新增、修改
     * @param param
     * @return
     */
    @EventSearch("MDMRYXZ,MDMRYXG")
    public String savePractitioner(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(service.savePractitioner(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-诊断信息修改保存
     * @param param
     * @return
     */
    @EventSearch("MDMZDXZ,MDMZDXG")
    public String savebasicoperationinfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);

        //创建响应数据
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        Outcome outcome = new Outcome();
        outcome.setResourceType("OperationOutcome");
        response.setStaus(ZsphConstant.RES_ERR_OTHER);
        Issue issue = new Issue();
        issue.setCode("informational");
        issue.setDiagnostics("成功");
        issue.setSeverity("informational");

        try{
            service.saveBdTermDiag(param);
        }catch (Exception e){
            issue.setCode("not-found");
            issue.setDiagnostics(e.getMessage());
            issue.setSeverity("error");
        }
        outcome.setIssue(Arrays.asList(issue));
        response.setOutcome(BeanMap.create(outcome));
        responseBody.getEntry().add(entry);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-手术诊断信息查询
     * 中山平台-诊断信息查询
     * @param param
     * @return
     */
    @EventSearch("getbasicoperationinfo,getbasicdiagnoseinfo")
    public String getbasicoperationinfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(service.getBdTermDiag(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-收费项目信息查询
     * @param param
     * @return
     */
    @EventSearch("getchargeiteminfo")
    public String getChargeBdIteminfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(service.getChargeBdIteminfo(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-诊疗项目信息查询
     * @param param
     * @return
     */
    @EventSearch("getsubstanceinfo")
    public String getChargeBdOrdinfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        responseBody.getEntry().add(new Entry(service.getChargeBdOrdinfo(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-患者基础信息查询
     * @param param
     * @return
     */
    @EventSearch("getpatientinfo")
    public String getPiMasterZsInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsrmPiService service = ServiceLocator.getInstance().getBean(ZsrmPiService.class);
        responseBody.setEntry(service.getpiMasterZsInfo(param));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-患者基础信息查询
     * @param param
     * @return
     */
    @EventSearch("EMPIRETURN")
    public String getPiMasterEmpiInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsrmPiService service = ServiceLocator.getInstance().getBean(ZsrmPiService.class);
        responseBody.setEntry(service.getPiMasterEmpiInfo(param));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-查询患者诊断
     * 根据就诊流水号
     * @param param
     * @return
     */
    @EventSearch("getdiagnosisinfo")
    public String getPvDiagZsInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsrmPiService service = ServiceLocator.getInstance().getBean(ZsrmPiService.class);
        responseBody.setEntry(service.getPvDiagZsInfo(param));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 3.16-门诊处方信息查询
     * @param param
     * @return
     */
    @EventSearch("getoutrecipeinfo")
    public String getOpCnorderZsInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        responseBody.setEntry(service.getOpCnorder(param));
        String result= ZsphMsgUtils.writeValueAsString(responseBody,"yyyy-MM-dd HH:mm:ss");
        return result;
    }

    /** 获取排班信息*/
    @EventSearch("getscheduleinfo")
    public String getSch(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("document")
                .build();
        ZsphSchService service = ServiceLocator.getInstance().getBean(ZsphSchService.class);
        service.getSch(param, new ResultListener() {
            @Override
            public void success(Object object) {
                responseBody.getEntry().addAll((List<Entry>)object);
            }

            @Override
            public void error(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }

            @Override
            public void exception(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }
        });

        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //手麻状态回传
    @EventSearch("SSZTGX")
    public String updataOpStateInfo(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        List<Entry> listEntry = service.updataOpStateInfo(param);
        responseBody.getEntry().addAll(listEntry);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /** 获取门诊就诊信息*/
    @EventSearch("getoutvisitinfo")
    public String getPvOpInfo(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .build();
        ZsphOpService service = ServiceLocator.getInstance().getBean(ZsphOpService.class);
        service.getPvOpInfo(param, new ResultListener() {
            @Override
            public void success(Object object) {
                responseBody.getEntry().addAll((List<Entry>)object);
            }

            @Override
            public void error(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }

            @Override
            public void exception(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }
        });

        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /** 门诊-患者预约*/
    @EventSearch("MZYYXXXZ,MZYYXXQX")
    public String savePvAppt(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .build();
        ZsphOpService service = ServiceLocator.getInstance().getBean(ZsphOpService.class);
        service.saveAppt(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }
    //发布检验报告
    @EventSearch("JYBGXZ")
    public String saveLisReportRelease(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.saveLisReportRelease(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //查询检验申请
    @EventSearch("getlisapplyinfo")
    public String getLisReportInfo(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.getLisReportInfo(param, new ResultListener() {
            @Override
            public void success(Object object) {
                responseBody.getEntry().addAll((List<Entry>)object);
            }

            @Override
            public void error(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }

            @Override
            public void exception(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }
        });
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //查询检查申请
    @EventSearch("getrisapplyinfo")
    public String getRisReportInfo(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.getRisReportInfo(param, new ResultListener() {
            @Override
            public void success(Object object) {
                responseBody.getEntry().addAll((List<Entry>)object);
            }

            @Override
            public void error(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }

            @Override
            public void exception(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }
        });
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /** 获取可预约科室列表*/
    @EventSearch("getAppointDeptList")
    public String getSchDept(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("document")
                .build();
        ZsphSchService service = ServiceLocator.getInstance().getBean(ZsphSchService.class);
        service.getSchApptDept(param, new ResultListener() {
            @Override
            public void success(Object object) {
                responseBody.getEntry().addAll((List<Entry>)object);
            }

            @Override
            public void error(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }

            @Override
            public void exception(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }
        });

        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //回收检验报告
    @EventSearch("JYBGHS")
    public String deleteLisReportRecovery(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.deleteLisReportRecovery(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //发布微生物报告
    @EventSearch("WSWBGXZ")
    public String saveBactReportRelease(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.saveBactReportRelease(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //回收微生物报告
    @EventSearch("WSWBGHS")
    public String deleteBactReportRecovery(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.deleteBactReportRecovery(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }


    //发布检查报告
    @EventSearch("JCBGFB")
    public String saveRisReportRelease(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.saveRisReportRelease(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //回收检查报告
    @EventSearch("JCBGHS")
    public String deleteRisReportRecovery(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.deleteRisReportRecovery(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //检查预约新增
    @EventSearch("JCYYXZ")
    public String saveMedicalMake(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.updateMedicalMake(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //检查预约修改
    @EventSearch("JCYYXG")
    public String updateMedicalMake(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.updateMedicalMake(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //检查预约取消预约
    @EventSearch("JCYYQX")
    public String cancelMedicalMake(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.updateMedicalMake(param);
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //检查预约签到
    @EventSearch("JCYYQD")
    public String signMedicalMake(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        // TODO: 2020-10-20  检查签到his中没有对应的状态，暂时不处理
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    //检查预约取消签到
    @EventSearch("JCYYQXQD")
    public String cancelSignMedicalMake(String param) {
        Object[] objects = new Object[1];
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        // TODO: 2020-10-20  检查取消签到his中没有对应的状态，暂时不处理
        List<Entry> entrySuccess = new ArrayList<>();
        Response response = ZsphMsgUtils.createSimpleSuccess("成功");
        entrySuccess.add(new Entry(response));
        responseBody.getEntry().addAll(entrySuccess);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 药品字典查询
     * @param param
     * @return
     */
    @EventSearch("getmedicationinfo")
    public String getBdPdInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphBasicDataService service = ServiceLocator.getInstance().getBean(ZsphBasicDataService.class);
        service.getBdPd(param, new ResultListener() {
            @Override
            public void success(Object object) {
                responseBody.getEntry().addAll((List<Entry>)object);
            }

            @Override
            public void error(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }

            @Override
            public void exception(Object object) {
                responseBody.getEntry().add(new Entry(ZsphMsgUtils.createErrResponse(String.valueOf(object))));
            }
        });
        return ZsphMsgUtils.writeValueAsString(responseBody);
    }

    /**3.41.回传分诊签到*/
    @EventSearch("FZQDXZ")
    public String triageSign(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("document")
                .build();
        ZsphOpService service = ServiceLocator.getInstance().getBean(ZsphOpService.class);
        service.triageSign(param);
        responseBody.getEntry().add(new Entry(ZsphMsgUtils.createSimpleSuccess("成功")));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }


    /** 获取可预约医生列表*/
    @EventSearch("getAppointDocList")
    public String getAppointDocList(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("document")
                .build();
        ZsphSchService service = ServiceLocator.getInstance().getBean(ZsphSchService.class);
        responseBody.getEntry().addAll(service.getSchApptDoc(param));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /** 获取可预约时间段列表*/
    @EventSearch("getAppointTimeList")
    public String getAppointTimeList(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("document")
                .build();
        ZsphSchService service = ServiceLocator.getInstance().getBean(ZsphSchService.class);
        responseBody.getEntry().addAll(service.getSchApptTime(param));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }
    /**患者预约信息查询*/
    @EventSearch("getappointmentinfo")
    public String getappointmentinfo(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("document")
                .build();
        ZsphOpService service = ServiceLocator.getInstance().getBean(ZsphOpService.class);
        responseBody.getEntry().addAll(service.getApptInfo(param));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }


    /**3.18.更新申请单状态*/
    @EventSearch("JYSQDZTXG,JCSQDZTXG,SXSQDZTXG,SSSQDZTXG")
    public String modApplyStatus(String param) {
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("document")
                .build();
        ZsphOpService service = ServiceLocator.getInstance().getBean(ZsphOpService.class);
        service.modApplyStatus(param);
        responseBody.getEntry().add(new Entry(ZsphMsgUtils.createSimpleSuccess("成功")));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**3.40.手麻补费服务*/
    @EventSearch("SSYZXZ")
    public String saveSupplementPrice(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsrmBlService service = ServiceLocator.getInstance().getBean(ZsrmBlService.class);
        responseBody.getEntry().add(new Entry(service.saveSupplementPrice(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-患者信息注册
     * @param param
     * @return
     */
    @EventSearch("YDDHZZC,YDDHZXG")
    public String savePiMaster(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsrmPiService service = ServiceLocator.getInstance().getBean(ZsrmPiService.class);
        responseBody.getEntry().addAll((service.savePiMaster(param)));
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 中山平台-三方获取电子发票
     * @param param
     * @return
     */
    @EventSearch("DZPJLBCX")
    public String getEBillByDate(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsrmBlService service = ServiceLocator.getInstance().getBean(ZsrmBlService.class);
        List<Entry> list = service.getEBillByDate(param);
        responseBody.getEntry().addAll(list);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 更新电子票据打印次数
     * @param param
     * @return
     */
    @EventSearch("DZPJDYCSHX")
    public String updateEBillPrintTimes(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsrmBlService service = ServiceLocator.getInstance().getBean(ZsrmBlService.class);
        service.updateEBillPrintTimes(param);
        responseBody.getEntry().add(new Entry(ZsphMsgUtils.createSimpleSuccess("成功")));
        return ZsphMsgUtils.getJsonStr(responseBody);

    }

    /**
     * 异步调用模式(不处理业务)
     * @param param
     * @return
     */
    @EventSearch("ACK")
    public String asynchronousCall(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        responseBody.getEntry().add(new Entry(ZsphMsgUtils.createSimpleSuccess("成功")));
        return ZsphMsgUtils.getJsonStr(responseBody);

    }

    /**
     * 异步调用模式(不处理业务)
     * @param param
     * @return
     */
    @EventSearch("TSHZZTBG")
    public String pushStatus(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        service.pushStatus(param);
        responseBody.getEntry().add(new Entry(ZsphMsgUtils.createSimpleSuccess("成功")));
        return ZsphMsgUtils.getJsonStr(responseBody);

    }

    /**
     * 3.81.体检-保存患者门诊费用信息接口
     * @param param
     * @return
     */
    @EventSearch("saveOpcg")
    public String saveOpcg(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphOpService service = ServiceLocator.getInstance().getBean(ZsphOpService.class);
        List<Entry> list = service.saveOpcg(param);
        responseBody.getEntry().addAll(list);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 删除体检费用
     * @param param
     * @return
     */
    @EventSearch("deleteOpcg")
    public String deleteOpcgpushStatus(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphOpService service = ServiceLocator.getInstance().getBean(ZsphOpService.class);
        service.deleteOpcgpushStatus(param);
        responseBody.getEntry().add(new Entry(ZsphMsgUtils.createSimpleSuccess("成功")));
        return ZsphMsgUtils.getJsonStr(responseBody);

    }

    /**
     * 中山平台-检查预约查询
     * @param param
     * @return
     */
    @EventSearch("JCYYCX")
    public String getRisAppointList(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        List<Entry> list = service.getRisAppointList(param);
        responseBody.getEntry().addAll(list);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 3.43.获取门诊费用主表服务
     * @param param
     * @return
     */
    @EventSearch("queryoutpfeemasterinfo")
    public String queryOutpfeeMasterInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsrmBlService service = ServiceLocator.getInstance().getBean(ZsrmBlService.class);
        List<Entry> list = service.queryOutpfeeMasterInfo(param);
        responseBody.getEntry().addAll(list);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 3.44.获取门诊费用明细服务
     * @param param
     * @return
     */
    @EventSearch("queryoutpfeedetailinfo")
    public String queryOutpfeeDetailInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsrmBlService service = ServiceLocator.getInstance().getBean(ZsrmBlService.class);
        List<Entry> list = service.queryoutpfeedetailinfo(param);
        responseBody.getEntry().addAll(list);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 根据预约主键信息，更新sch_appt.pk_pi
     * @param param
     * @return
     */
    @EventSearch("BGYYHZID")
    public String updateSchApptByPkSchApp(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphSchService service = ServiceLocator.getInstance().getBean(ZsphSchService.class);
        service.updateSchApptByPkSchApp(param);
        responseBody.getEntry().add(new Entry(ZsphMsgUtils.createSimpleSuccess("成功")));
        return ZsphMsgUtils.getJsonStr(responseBody);

    }

    /**
     * 3.101.查询门诊病历信息
     */
    @EventSearch("getmzrecordinfo")
    public String getMzRecordInfo(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        List<Entry> list = service.getMzRecordInfo(param);
        responseBody.getEntry().addAll(list);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }

    /**
     * 3.105.物价查询
     * @param param
     * @return
     */
    @EventSearch("PriceCheck")
    public String priceCheck(String param){
        ResponseBody responseBody = ResponseBody.newBuilder()
                .resourceType("Bundle")
                .type("message")
                .id(NHISUUID.getKeyId())
                .timestamp(new Date())
                .build();
        ZsphCnService service = ServiceLocator.getInstance().getBean(ZsphCnService.class);
        List<Entry> list = service.getPriceInquiry(param);
        responseBody.getEntry().addAll(list);
        return ZsphMsgUtils.getJsonStr(responseBody);
    }
}
