package com.zebone.nhis.ma.pub.platform.zsrm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOccBact;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOccBactAl;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.RequestBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.ZsphPlatFormSendOpMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchDoctorVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.BdExtension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.DataTable;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.InvoiceOutcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.MzRecordSubject;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.OpApply;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.ApptResource;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsphLisRisMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsrmCnMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.zsrm.service.ZsphCnService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.QueryUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.MzRecordInfoVo;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.PriceInquiryVo;
import com.zebone.nhis.webservice.vo.ticketvo.Data;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ZsphCnServiceImpl implements ZsphCnService {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    @Resource
    private ZsphLisRisMapper zsphLisRisMapper;
    @Resource
    private ZsrmCnMapper zsrmCnMapper;
    @Resource
    private ZsphPlatFormSendOpMapper zsphPlatFormSendOpMapper;

    /**
     * 手术状态回传
     * @param param
     */
    @Override
    public List<Entry> updataOpStateInfo(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        //List<Entry> entry = requestBody.getEntry();
        List<Entry> entryList = new ArrayList<>();

        Object entry = MapUtils.getObject(requestBody, "entry");
        if(entry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryMapList = (List<Map<String,Object>>) entry;
        //获取到预约数据节点
        List<OpApply> opApplyList = new ArrayList<>();
        entryMapList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                opApplyList.add(TransfTool.mapToBean(OpApply.class, resourceMap));
            }
        });
        for(OpApply opApply : opApplyList){
            //OpApply opApply = (OpApply) en.getResource();
            if(opApply != null && "Procedure".equals(opApply.getResourceType())){
                String status = opApply.getStatus();//手术状态
                ValuePeriod performedPeriod = opApply.getPerformedPeriod();
                Date dateBegin = performedPeriod.getStart();
                List<Identifier> identifierList = opApply.getIdentifier();
                String codeApply = "";
                for(Identifier id:identifierList){
                    if("id/applyno".equals(id.getSystem())){
                        codeApply = id.getValue();
                    }
                }
                if(StringUtils.isBlank(status)&&StringUtils.isBlank(codeApply)){
                    throw new BusException("未获取到手术状态和手术唯一编码！");
                }
                int count = 0;
                //提交对应的登记
                if(EnumerateParameter.FOUR.equals(status)){
                    //将手术状态更新为提交
                    if(StringUtils.isNotBlank(codeApply)){
                       count =  DataBaseHelper.update("update cn_op_apply set eu_status='1' where pk_cnord in （SELECT pk_cnord from cn_order where code_apply = ?） ", new Object[]{codeApply});
                    }
                }else if(EnumerateParameter.FIVE.equals(status)){
                    //排班
                    if(StringUtils.isNotBlank(codeApply)){
                        count =  DataBaseHelper.update("update cn_op_apply set eu_status='2' where pk_cnord in （SELECT pk_cnord from cn_order where code_apply = ?） ", new Object[]{codeApply});
                    }
                }else if(EnumerateParameter.SIX.equals(status)||EnumerateParameter.SEVEN.equals(status)
                ||EnumerateParameter.EIGHT.equals(status)||EnumerateParameter.NINE.equals(status)){
                    //入手术室代码手术开始
                    if(StringUtils.isNotBlank(codeApply)){
                        count =  DataBaseHelper.update("update cn_op_apply set eu_status='3' where pk_cnord in （SELECT pk_cnord from cn_order where code_apply = ?） ", new Object[]{codeApply});
                    }
                }else if(EnumerateParameter.NINE.equals(status)){
                    //麻醉完成
                    if(StringUtils.isNotBlank(codeApply)){
                        count =  DataBaseHelper.update("update cn_op_apply set flag_finish_anae='1' where pk_cnord in （SELECT pk_cnord from cn_order where code_apply = ?） ", new Object[]{codeApply});
                    }
                }else if(EnumerateParameter.TEN.equals(status)){
                    //手术完成
                    if(StringUtils.isNotBlank(codeApply)){
                        count =  DataBaseHelper.update("update cn_op_apply set eu_status='5' where pk_cnord in （SELECT pk_cnord from cn_order where code_apply = ?） ", new Object[]{codeApply});
                        DataBaseHelper.update("update ex_order_occ set date_begin=?,date_occ=?,eu_status='1'  where pk_cnord in （SELECT pk_cnord from cn_order where code_apply = ?）  and eu_status='0' ", new Object[]{dateBegin,new Date(), codeApply});
                    }
                }else{
                    //其他状态暂时不处理
                    count = 1;
                }
                Entry entryRet = new Entry(new Response());
                if(count <1){
                    throw new BusException("更新手术表失败！");
                }else{
                    Response response = entryRet.getResponse();
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    Outcome outcome = new Outcome();
                    outcome.setResourceType("OperationOutcome");
                    outcome.setIssue(new ArrayList<>());
                    Issue issue = new Issue();
                    issue.setSeverity("information");
                    issue.setCode("informational");
                    issue.setDiagnostics("成功");
                    outcome.getIssue().add(issue);
                    response.setOutcome(BeanMap.create(outcome));
                    entryList.add(entryRet);
                }
            }
        }
        if(entryList.size()>0){
            return entryList;
        }else{
            throw new BusException("未获取到手术参数！");
        }
    }

    /**
     * 检验报告发布
     * @param param
     */
    @Override
    public void saveLisReportRelease(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到检验数据节点
        List<Lis> lisList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                lisList.add(TransfTool.mapToBean(Lis.class, resourceMap));
            }
        });
        for(Lis lis: lisList){
            if(CollectionUtils.isEmpty(lisList)){
                throw new BusException("未传入检验信息");
            }
            List<Identifier> identifier = lis.getIdentifier();
            if(CollectionUtils.isEmpty(identifier)){
                throw new BusException("identifier集合为空");
            }
            if(lis.getSubject() == null){
                throw new BusException("Subject对象为空");
            }
            if(lis.getResult() == null){
                throw new BusException("Result对象为空");
            }

            //获取检验单号和检验报告单号
            String applyNo = null;//申请单号
            String reportNo = null;//报告单号
            if(CollectionUtils.isNotEmpty(identifier)){
                //Identifier identifier = appointment.getIdentifier().get(0);
                for(Identifier id :identifier){
                    if(id!=null && "code/applyno".equalsIgnoreCase(id.getSystem())){
                        applyNo = id.getValue();
                    }
                    if(id!=null && "code/reportno".equalsIgnoreCase(id.getSystem())){
                        reportNo = id.getValue();
                    }
                }
            }
            //获取患者codeOp
            String codeOp = null;
            if(lis.getSubject()!=null && "patientid".equalsIgnoreCase(lis.getSubject().getIdentifier().getSystem())){
                codeOp = lis.getSubject().getIdentifier().getValue();
            }
            if(StringUtils.isBlank(codeOp)){
                throw new BusException("未获取到患者的codeOp");
            }
            //获取病人信息 codeOp
            List<Map<String, Object>> queryPiByCodeOp = QueryUtils.queryPiByCodeOp(codeOp);
            if(queryPiByCodeOp==null || queryPiByCodeOp.size()==0){
                throw new BusException("没有查到该病人信息:"+codeOp);
            }
            //获取审核人和报告人

            List<Locationlis> performer = lis.getPerformer();
            if(CollectionUtils.isEmpty(performer)){
                throw new BusException("performer节点不能为空:");
            }
            String empOccCode = null;
            String empChkCode = null;
            for(Locationlis lo : performer){
                Identifier iden = lo.getIdentifier();
                if(iden!=null && "reportdocId".equalsIgnoreCase(iden.getSystem())){
                    empOccCode = iden.getValue();
                }
                if(iden!=null && "verifydocId".equalsIgnoreCase(iden.getSystem())){
                    empChkCode = iden.getValue();
                }
            }
            Map<String,Object> occMap = QueryUtils.queryPkEmpByCode(empOccCode);
            Map<String,Object> chkMap = QueryUtils.queryPkEmpByCode(empChkCode);
/*            if(occMap == null||chkMap == null){
                throw new BusException("通过人员编码未获取的到审核人和执行人！");
            }*/
            //查询医嘱信息
            String sql = " select o.* from cn_order o where o.code_apply=?";
            Map<String, Object> orderMap = DataBaseHelper.queryForMap(sql, applyNo);
            //未找到对应检验医嘱
            if(orderMap==null){
                throw new BusException("未找到有效的检验医嘱,申请单号为："+applyNo);
            }
            //获取检验结果信息(多个检验结果)
            List<ExLabOcc> exLabOccList = new ArrayList<ExLabOcc>();
            List<Result> resultList = lis.getResult();//result传一个里面有多个结果
            if(resultList == null){
                throw new BusException("检验结果为空");
            }
            //获取报告日期
            Date dateRpt = lis.getEffectiveDateTime();
            //获取审核时间
            Date dateChk = lis.getIssued();
            for(Result result :resultList){
                List<Component> component = result.getComponent();
                for(Component co:component){
                    ExLabOcc exLabOcc = new ExLabOcc();
                    String pkLabocc = NHISUUID.getKeyId();
                    exLabOcc.setPkLabocc(pkLabocc);
                    exLabOcc.setPkOrg(MapUtils.getString(orderMap, "pkOrg"));
                    exLabOcc.setPkPi(MapUtils.getString(orderMap, "pkPi"));
                    exLabOcc.setPkPv(MapUtils.getString(orderMap,"pkPv"));
                    exLabOcc.setPkCnord(MapUtils.getString(orderMap,"pkCnord"));
                    exLabOcc.setCodeApply(MapUtils.getString(orderMap,"codeApply"));
                    exLabOcc.setPkOrgOcc(MapUtils.getString(orderMap,"pkOrgExec"));
                    exLabOcc.setPkDeptOcc(MapUtils.getString(orderMap,"pkDeptExec"));
                    exLabOcc.setPkOrd(MapUtils.getString(orderMap, "pkOrd"));
                    exLabOcc.setNameOrd(MapUtils.getString(orderMap,"nameOrd"));
                    exLabOcc.setEuType("0");
                    //审核日期
                    exLabOcc.setDateChk(dateChk);
                    //报告日期
                    exLabOcc.setDateRpt(dateRpt);

                    exLabOcc.setCodeRpt(reportNo);//报告唯一号
                    exLabOcc.setCodeSamp(applyNo);//样本号
                    if(co.getValueQuantity() != null){
                        exLabOcc.setUnit(co.getValueQuantity().getUnit());
                        Map<String,Object> unit = QueryUtils.queryUnitByNameUnit(co.getValueQuantity().getUnit());
                        if(unit!= null){
                            exLabOcc.setPkUnit(MapUtils.getString(unit,"pkUnit"));
                        }
                    }
                    exLabOcc.setDateOcc(new Date());
                    //获取审核人和报告人


                    exLabOcc.setPkEmpOcc(MapUtils.getString(occMap,"pkEmp",""));//执行人
                    exLabOcc.setNameEmpOcc(MapUtils.getString(occMap,"nameEmp","三方未对应"));
                    exLabOcc.setFlagChk("1");
                    //暂时没有序号
                    //exLabOcc.setSortNo(Integer.parseInt(""));
                    exLabOcc.setPkEmpChk(MapUtils.getString(chkMap,"pkEmp",""));//审核人
                    exLabOcc.setNameEmpChk(MapUtils.getString(chkMap,"nameEmp","三方未对应"));
                    //获取设备
                    Device device = result.getDevice();
                    if(device != null){
                        List<Parameter> deviceName = device.getDeviceName();
                        if(deviceName != null &&deviceName.size()>0){
                            exLabOcc.setPkMsp(MapUtils.getString(QueryUtils.queryPkMspByMspName(deviceName.get(0).getName()),"pkMsp"));
                        }
                    }
                    List<Coding> code = co.getCode().getCoding();
                    if(code == null){
                        throw new BusException("节点结果里面code不能为空！");
                    }
                    code.get(0).getCode();
                    exLabOcc.setCodeIndex(code.get(0).getCode());
                    exLabOcc.setNameIndex(code.get(0).getDisplay());
                    String value = null;
                    if(co.getValueQuantity() != null){
                        value =  co.getValueQuantity().getValue();
                        exLabOcc.setVal(value);
                    }

                    //exLabOcc.setUnit(co.getValueQuantity().getUnit());
                    //exLabOcc.setPkUnit(MapUtils.getString(QueryUtils.queryUnitByNameUnit(co.getValueQuantity().getUnit()),"pkUnit"));
                    //参考范围
                    TextElement references =co.getReferenceRange();
                    if(references != null){
                        String referencesText = references.getText();
                        if(referencesText!=null && !"".equals(referencesText)){
                            //判断是否数值范围 是则拆分
                            if(referencesText.contains("--")){
                                String[] split = referencesText.split("--");
                                exLabOcc.setValMin(split[0]);
                                exLabOcc.setValMax(split[1]);
                                if(StringUtils.isNotBlank(value) &&Double.parseDouble(split[0])<=Double.parseDouble(value)&&Double.parseDouble(value)<=Double.parseDouble(split[1])){
                                    exLabOcc.setEuResult("0");
                                }else if(StringUtils.isNotBlank(value) &&Double.parseDouble(split[0])>Double.parseDouble(value)){
                                    exLabOcc.setEuResult("-1");
                                }else{
                                    exLabOcc.setEuResult("1");
                                }
                            }else{
                                //汉字参考范围
                                exLabOcc.setValMin(referencesText);
                            }
                        }
                    }
                    exLabOcc.setDelFlag("0");
                    exLabOccList.add(exLabOcc);
                }
            }
            //保存结果
            int count = 0;
            if(exLabOccList.size()>0){
                count = QueryUtils.saveLisRptList(MapUtils.getString(orderMap, "pkCnord"),applyNo,exLabOccList);
            }
            if(count == 0){
                throw new BusException("数据错误，组装数据错误！");
            }
        }

    }


    /**
     * 查询检验申请
     * @param param
     * @param listener
     */
    public void getLisReportInfo(String param, ResultListener listener){
        try{
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
            if(CollectionUtils.isEmpty(parameterList)){
                listener.error("未传入parameter");
                return;
            }
            Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
            if(StringUtils.isBlank(MapUtils.getString(paramMap,"visitno")) && StringUtils.isBlank(MapUtils.getString(paramMap,"appyno"))
                    &&StringUtils.isBlank(MapUtils.getString(paramMap,"patientno"))&&StringUtils.isBlank(MapUtils.getString(paramMap,"id/applyDetailNo"))){
                listener.error("必须传入一个参数!!");
                return;
            }
            /*PvEncounter pvEncounter = DataBaseHelper.queryForBean("select pk_pi,pk_pv from pv_encounter where code_pv=?",PvEncounter.class, MapUtils.getString(paramMap,"visitno"));
            if(pvEncounter == null){
                listener.error("根据住院号未获取到就诊信息");
                return;
            }*/
            List<Map<String,Object>> cnLabApply = zsphLisRisMapper.getLisInfo(MapUtils.getString(paramMap,"visitno"),MapUtils.getString(paramMap,"appyno"),MapUtils.getString(paramMap,"patientno"),MapUtils.getString(paramMap,"id/applyDetailNo"));
            if(CollectionUtils.isNotEmpty(cnLabApply)){
                List<Entry> entryList = new ArrayList<>();
                for(Map<String,Object> cn:cnLabApply){
                    Entry entry = new Entry(new Response());
                    Response response = entry.getResponse();
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    Lis lis = new Lis();
                    lis.setResourceType("ServiceRequest");
                    lis.setImplicitRules("getlisapplyinfo");
                    lis.setIdentifier(new ArrayList<>());
                    lis.getIdentifier().add(new Identifier("code/applyno",MapUtils.getString(cn,"codeApply")));//申请单号

                    if(EnumerateParameter.ZERO.equals(MapUtils.getString(cn,"euStatus"))){
                        lis.setStatus("1");
                    }else if(EnumerateParameter.TWO.equals(MapUtils.getString(cn,"euStatus"))){//最终报告
                        lis.setStatus("3");
                    }else if(EnumerateParameter.THREE.equals(MapUtils.getString(cn,"euStatus"))){//作废报告
                        lis.setStatus("4");
                    }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(cn,"euStatus"))){//作废报告
                        lis.setStatus("7");
                    }
                    Integer cnt = DataBaseHelper.queryForScalar(
                            "select count(1) from bl_op_dt where flag_pv='1' and quan<0 and PK_CNORD = ?",
                            Integer.class, new Object[]{MapUtils.getString(cn,"pkCnord")});
                    if(cnt > 0){//退费代表取消
                        lis.setStatus("8");
                    }
                    lis.setIntent("order");

                    lis.setCategory(Arrays.asList(new Category(Arrays.asList(new Coding("applyTypeCode",MapUtils.getString(cn,"codeOrdtype"),MapUtils.getString(cn,"nameOrdtype"))))));

                    //lis.setCategory()));//分类

                    lis.setCode(new Category(Arrays.asList(new Coding("applyItem",MapUtils.getString(cn,"codeOrd"),MapUtils.getString(cn,"nameOrd")))));

                    Locationlis location = new Locationlis();
                    location.setResourceType("Patient");
                    location.setIdentifier(new Identifier("patientId",MapUtils.getString(cn,"codeOp")));
                    location.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"namePi"))));
                    String dtSex = MapUtils.getString(cn,"dtSex");
                    location.setGender("02".equals(dtSex)?"male":"03".equals(dtSex)?"female":"other");//患者性别
                    location.setBirthDate(MapUtils.getString(cn,"birthDate"));
                    lis.setSubject(location);

                    //就诊类别
                    Encounter encounter = new Encounter();
                    encounter.setResourceType("Encounter");
                    if(EnumerateParameter.ONE.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("AMB","门诊"));
                    }else if(EnumerateParameter.TWO.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("EMER","急诊"));
                    }else if(EnumerateParameter.THREE.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("IMP","住院"));
                    }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("PHY","体检"));
                    }
                    encounter.setIdentifier(new Identifier("id/visitno",MapUtils.getString(cn,"codePv")));
                    //encounter.setName(new TextElement(MapUtils.getString(cn,"namePi")));
                    lis.setEncounter(encounter);

                    if(StringUtils.isNotBlank(ZsphMsgUtils.getPropValueStr(cn,"dateRpt"))){
                        lis.setAuthoredOn(sdfDate.parse(ZsphMsgUtils.getPropValueStr(cn,"dateRpt")));
                    }else{
                        lis.setAuthoredOn(null);
                    }

                    //申请医生
                    Locationlis locationlisReq = new Locationlis();
                    locationlisReq.setResourceType("Practitioner");
                    locationlisReq.setIdentifier(new Identifier("id/applyDoc",MapUtils.getString(cn,"codeEmp")));
                    locationlisReq.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"nameEmp"))));
                    lis.setRequester(locationlisReq);

                    //核对人
                    lis.setPerformer(new ArrayList<>());
                    Locationlis locationlis = new Locationlis();
                    locationlis.setResourceType("Practitioner");
                    locationlis.setIdentifier(new Identifier("id/verifyDoc",MapUtils.getString(cn,"codeEmpOrdChk")));
                    locationlis.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"nameEmpOrdChk"))));
                    Locationlis locationlis1 = new Locationlis();
                    locationlis1.setResourceType("Practitioner");
                    locationlis1.setIdentifier(new Identifier("code/exdeptcode",MapUtils.getString(cn,"codeDeptExec")));
                    locationlis1.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"nameDeptExec"))));
                    lis.getPerformer().add(locationlis);
                    lis.getPerformer().add(locationlis1);

                    List<Code> code = new ArrayList<>();
                    Map<String,Object> dept = getDeptInfo(MapUtils.getString(cn,"pkDept"));
                    code.add(new Code(Arrays.asList(new Coding("code/reqdeptcode",MapUtils.getString(dept,"codeDept",""),MapUtils.getString(dept,"nameDept","")))));
                    Map<String,Object> deptNs = getDeptInfo(MapUtils.getString(cn,"pkDeptNs"));
                    code.add(new Code(Arrays.asList(new Coding("code/wardcode",MapUtils.getString(deptNs,"codeDept",""),MapUtils.getString(deptNs,"nameDept","")))));
                    code.add(new Code(Arrays.asList(new Coding("code/bodno",MapUtils.getString(deptNs,"bedNo",""),MapUtils.getString(deptNs,"bedNo","")))));
                    lis.setLocationCode(code);

                    lis.setReasonReference(new ArrayList<>());
                    ReasonReference reasonReference = new ReasonReference();
                    reasonReference.setResourceType("Condition");
                    reasonReference.setCode(new Code(Arrays.asList(new Coding("diagnosis",MapUtils.getString(cn,"diagcode"),MapUtils.getString(cn,"diagname")))));
                    lis.getReasonReference().add(reasonReference);

                    //标本
                    lis.setSpecimen(new ArrayList<>());
                    SpecimenLis specimen = new SpecimenLis();
                    specimen.setResourceType("Specimen");
                    specimen.setType(new CodeLis(new Coding("Specimen",MapUtils.getString(cn,"dtSamptype",""),MapUtils.getString(cn,"labName",""))));
                    //specimen.setType(new Coding("Specimen",MapUtils.getString(cn,"dtSamptype"),MapUtils.getString(cn,"labName")));
                    //specimen.setIdentifier(new Identifier("Specimen",MapUtils.getString(cn,"dtSamptype"),MapUtils.getString(cn,"labName"),"specimen"));
                    lis.getSpecimen().add(specimen);
                    lis.setNote(Arrays.asList(new TextElement(MapUtils.getString(cn,"note"))));
                    lis.setRelevantHistory(Arrays.asList(new RelevantHistory("Provenance",null)));
                    response.setOutcome(BeanMap.create(lis));
                    entryList.add(entry);
                }
                listener.success(entryList);
            }
        }catch (Exception e){
            listener.exception(e.getMessage());
        }
    }


    /**
     * 查询检查申请
     * @param param
     * @param listener
     */
    public void getRisReportInfo(String param, ResultListener listener){
        try{
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
            if(CollectionUtils.isEmpty(parameterList)){
                listener.error("未传入parameter");
                return;
            }
            Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
            if(StringUtils.isBlank(MapUtils.getString(paramMap,"visitno")) && StringUtils.isBlank(MapUtils.getString(paramMap,"appyno"))
                    &&StringUtils.isBlank(MapUtils.getString(paramMap,"patientno"))&&StringUtils.isBlank(MapUtils.getString(paramMap,"id/applyDetailNo"))){
                listener.error("必须传入一个参数!!");
                return;
            }
            /*PvEncounter pvEncounter = DataBaseHelper.queryForBean("select pk_pi,pk_pv from pv_encounter where code_pv=?",PvEncounter.class, MapUtils.getString(paramMap,"visitno"));
            if(pvEncounter == null){
                listener.error("根据住院号未获取到就诊信息");
                return;
            }*/
            List<Map<String,Object>> cnRisApply = zsphLisRisMapper.getRisInfo(MapUtils.getString(paramMap,"visitno"),MapUtils.getString(paramMap,"appyno"),MapUtils.getString(paramMap,"patientno"),MapUtils.getString(paramMap,"id/applyDetailNo"));
            if(CollectionUtils.isNotEmpty(cnRisApply)){
                List<Entry> entryList = new ArrayList<>();
                for(Map<String,Object> cn:cnRisApply){
                    Entry entry = new Entry(new Response());
                    Response response = entry.getResponse();
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    Ris ris = new Ris();
                    ris.setResourceType("ServiceRequest");
                    ris.setImplicitRules("getrisapplyinfo");
                    ris.setIdentifier(new ArrayList<>());
                    ris.getIdentifier().add(new Identifier("id/applyDetailNo",MapUtils.getString(cn,"pkCnord")));//医嘱号
                    ris.getIdentifier().add(new Identifier("code/applyno",MapUtils.getString(cn,"codeApply")));//申请单号
                    if(EnumerateParameter.ZERO.equals(MapUtils.getString(cn,"euStatus"))){
                        ris.setStatus("1");
                    }else if(EnumerateParameter.TWO.equals(MapUtils.getString(cn,"euStatus"))){
                        ris.setStatus("3");
                    }else if(EnumerateParameter.THREE.equals(MapUtils.getString(cn,"euStatus"))){
                        ris.setStatus("4");
                    }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(cn,"euStatus"))){
                        ris.setStatus("7");
                    }
                    Integer cnt = DataBaseHelper.queryForScalar(
                            "select count(1) from bl_op_dt where flag_pv='1' and quan<0 and PK_CNORD = ?",
                            Integer.class, new Object[]{MapUtils.getString(cn,"pkCnord")});
                    if(cnt > 0){//退费代表取消
                        ris.setStatus("8");
                    }
                    ris.setIntent("order");

                    String codeOrdtype = MapUtils.getString(cn,"codeOrdtype");
                    if(StringUtils.isNotBlank(codeOrdtype)){
                        ris.setCategory(Arrays.asList(new Category(Arrays.asList(new Coding("applyTypeCode",codeOrdtype.substring(codeOrdtype.length() -2,codeOrdtype.length()),MapUtils.getString(cn,"nameOrdtype",""))))));
                    }else{
                        throw new BusException("未维护医嘱的类型！！");
                    }
                    List<BdExtension> bdExtension = new ArrayList<>();
                    bdExtension.add(new BdExtension("amount",new BigDecimal(MapUtils.getString(cn,"quanCg"))));
                    bdExtension.add(new BdExtension("price",new BigDecimal(MapUtils.getString(cn,"priceCg"))));
                    ris.setCode(new Category(Arrays.asList(new Coding("applyItem",MapUtils.getString(cn,"codeOrd"),MapUtils.getString(cn,"nameOrd"),bdExtension))));

                    Locationlis location = new Locationlis();
                    location.setResourceType("Patient");
                    location.setIdentifier(new Identifier("patientId",MapUtils.getString(cn,"codeOp")));
                    location.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"namePi"))));
                    String dtSex = MapUtils.getString(cn,"dtSex");
                    location.setGender("02".equals(dtSex)?"male":"03".equals(dtSex)?"female":"other");//患者性别
                    location.setBirthDate(MapUtils.getString(cn,"birthDate"));
                    ris.setSubject(location);

                    //就诊类别
                    Encounter encounter = new Encounter();
                    encounter.setResourceType("Encounter");
                    if(EnumerateParameter.ONE.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("AMB","门诊"));
                    }else if(EnumerateParameter.TWO.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("EMER","急诊"));
                    }else if(EnumerateParameter.THREE.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("IMP","住院"));
                    }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(cn,"euPvtype"))){
                        encounter.setClas(new Coding("PHY","体检"));
                    }
                    encounter.setIdentifier(new Identifier("id/visitno",MapUtils.getString(cn,"codePv")));
                    //encounter.setName(new TextElement(MapUtils.getString(cn,"namePi")));
                    ris.setEncounter(encounter);

                    if(StringUtils.isNotBlank(ZsphMsgUtils.getPropValueStr(cn,"dateRpt"))){
                        ris.setAuthoredOn(sdfDate.parse(ZsphMsgUtils.getPropValueStr(cn,"dateRpt")));
                    }else{
                        ris.setAuthoredOn(null);
                    }

                    //申请医生
                    Locationlis locationlisReq = new Locationlis();
                    locationlisReq.setResourceType("Practitioner");
                    locationlisReq.setIdentifier(new Identifier("id/applyDoc",MapUtils.getString(cn,"codeEmp")));
                    locationlisReq.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"nameEmp"))));
                    ris.setRequester(locationlisReq);

                    //核对人
                    ris.setPerformer(new ArrayList<>());
                    Locationlis locationlis = new Locationlis();
                    locationlis.setResourceType("Practitioner");
                    locationlis.setIdentifier(new Identifier("id/verifyDoc",MapUtils.getString(cn,"codeEmpOrdChk")));
                    locationlis.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"nameEmpOrdChk"))));
                    Locationlis locationlis1 = new Locationlis();
                    locationlis1.setResourceType("Practitioner");
                    locationlis1.setIdentifier(new Identifier("code/exdeptcode",MapUtils.getString(cn,"codeDeptExec")));
                    locationlis1.setName(Arrays.asList(new TextElement(MapUtils.getString(cn,"nameDeptExec"))));
                    ris.getPerformer().add(locationlis);
                    ris.getPerformer().add(locationlis1);

                    //ris.setLocationCode(new ArrayList<>());
                    List<Code> code = new ArrayList<>();
                    Map<String,Object> dept = getDeptInfo(MapUtils.getString(cn,"pkDept"));
                    //ris.setLocationCode();
                    code.add(new Code(Arrays.asList(new Coding("code/reqdeptcode",MapUtils.getString(dept,"codeDept"),MapUtils.getString(dept,"nameDept")))));
                    Map<String,Object> deptNs = getDeptInfo(MapUtils.getString(cn,"pkDeptNs"));
                    code.add(new Code(Arrays.asList(new Coding("code/wardcode",MapUtils.getString(deptNs,"codeDept"),MapUtils.getString(deptNs,"nameDept")))));
                    //ris.getLocationCode().add();
                    //ris.getLocationCode().add();
                    code.add(new Code(Arrays.asList(new Coding("code/bodno",MapUtils.getString(deptNs,"bedNo"),MapUtils.getString(deptNs,"bedNo")))));
                    ris.setLocationCode(code);

                    ris.setReasonReference(new ArrayList<>());
                    ReasonReference reasonReference = new ReasonReference();
                    reasonReference.setResourceType("Condition");
                    reasonReference.setCode(new Code(Arrays.asList(new Coding("diagnosis",MapUtils.getString(cn,"diagcode"),MapUtils.getString(cn,"diagname")))));
                    ris.getReasonReference().add(reasonReference);

                    //标本
                    ris.setSpecimen(new ArrayList<>());
                    Specimen specimen = new Specimen();
                    specimen.setResourceType("Specimen");
                    specimen.setType(new Code(Arrays.asList(new Coding("Specimen",MapUtils.getString(cn,"dtSamptype",""),MapUtils.getString(cn,"labName","")))));
                    //specimen.setType(new Coding("Specimen",MapUtils.getString(cn,"dtSamptype"),MapUtils.getString(cn,"labName")));
                    //specimen.setIdentifier(new Identifier("Specimen",MapUtils.getString(cn,"dtSamptype"),MapUtils.getString(cn,"labName"),"specimen"));
                    ris.getSpecimen().add(specimen);
                    ris.setBodySite(Arrays.asList(new Code(Arrays.asList(new Coding("bodySite",MapUtils.getString(cn,"descBody"),MapUtils.getString(cn,"nameBody"))))));
                    ris.setNote(Arrays.asList(new TextElement(MapUtils.getString(cn,"note"))));
                    ris.setRelevantHistory(Arrays.asList(new RelevantHistory("Provenance",null)));
                    ris.setExtension(new ArrayList<>());
                    ris.getExtension().add(new Extension("feeStatus",1));
                    ris.getExtension().add(new Extension("examCount",Integer.parseInt(MapUtils.getString(cn,"quanCg"))));
                    response.setOutcome(BeanMap.create(ris));
                    entryList.add(entry);
                }
                listener.success(entryList);
            }
        }catch (Exception e){
            listener.exception(e.getMessage());
        }
    }

    public Map<String,Object> getDeptInfo(String pkDept){
        return DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = '"+pkDept+"'");
    }

    /**
     * 检验报告回收
     * @param param
     */
    @Override
    public void deleteLisReportRecovery(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到检验数据节点
        List<Lis> lisList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                lisList.add(TransfTool.mapToBean(Lis.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(lisList)){
            throw new BusException("未传入检验信息");
        }
        for(Lis lis: lisList){
            List<Identifier> identifier = lis.getIdentifier();
            if(CollectionUtils.isEmpty(identifier)){
                throw new BusException("identifier集合为空");
            }
            //获取检验单号和检验报告单号
            String applyNo = null;//申请单号
            String reportNo = null;//报告单号
            if(CollectionUtils.isNotEmpty(identifier)){
                for(Identifier id :identifier){
                    if(id!=null && "code/applyno".equalsIgnoreCase(id.getSystem())){
                        applyNo = id.getValue();
                    }
                    if(id!=null && "code/reportno".equalsIgnoreCase(id.getSystem())){
                        reportNo = id.getValue();
                    }
                }
            }
            //查询医嘱信息
            String sql = " select o.* from cn_order o where o.code_apply=?";
            Map<String, Object> orderMap = DataBaseHelper.queryForMap(sql, applyNo);
            //未找到对应检验医嘱
            if(orderMap==null){
                throw new BusException("未找到有效的检验医嘱,申请单号为："+applyNo);
            }
            QueryUtils.deleteLisRptList(MapUtils.getString(orderMap, "pkCnord"),applyNo);
        }

    }

    /**
     * 发布微生物报告
     * @param param
     */
    @Override
    public void saveBactReportRelease(String param) {
        Map<String, Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if (objEntry == null) {
            throw new BusException("未传入entry");
        }
        List<Map<String, Object>> entryList = (List<Map<String, Object>>) objEntry;
        //获取到检验数据节点
        List<LisBact> lisList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                lisList.add(TransfTool.mapToBean(LisBact.class, resourceMap));
            }
        });
        if (CollectionUtils.isEmpty(lisList)) {
            throw new BusException("未传入微生物检验信息");
        }
        for (LisBact lis : lisList) {
            List<Identifier> identifier = lis.getIdentifier();
            if (CollectionUtils.isEmpty(identifier)) {
                throw new BusException("identifier集合为空");
            }
            if (lis.getSubject() == null) {
                throw new BusException("Subject对象为空");
            }
            if (lis.getResult() == null) {
                throw new BusException("Result对象为空");
            }

            //获取检验单号和检验报告单号
            String applyNo = null;//申请单号
            String reportNo = null;//报告单号
            if (CollectionUtils.isNotEmpty(identifier)) {
                for (Identifier id : identifier) {
                    if (id != null && "id/applyno".equalsIgnoreCase(id.getSystem())) {
                        applyNo = id.getValue();
                    }
                    if (id != null && "id/reportno".equalsIgnoreCase(id.getSystem())) {
                        reportNo = id.getValue();
                    }
                }
            }
            //获取患者codeOp
            String codeOp = null;
            for(Identifier iden:lis.getSubject().getIdentifier()){
                if (lis.getSubject().getIdentifier() != null && "id/patientno".equalsIgnoreCase(iden.getSystem())) {
                    codeOp = iden.getValue();
                }
                if (StringUtils.isBlank(codeOp)) {
                    throw new BusException("未获取到患者的codeOp");
                }
            }

            //获取病人信息 codeOp
            List<Map<String, Object>> queryPiByCodeOp = QueryUtils.queryPiByCodeOp(codeOp);
            if (queryPiByCodeOp == null || queryPiByCodeOp.size() == 0) {
                throw new BusException("没有查到该病人信息:" + codeOp);
            }
            //获取审核人和报告人

            List<Locationlis> performer = lis.getPerformer();
            if (CollectionUtils.isEmpty(performer)) {
                throw new BusException("performer节点不能为空:");
            }
            String empOccCode = null;
            String empChkCode = null;
            for (Locationlis lo : performer) {
                Identifier iden = lo.getIdentifier();
                if (iden != null && "reportdocId".equalsIgnoreCase(iden.getSystem())) {
                    empOccCode = iden.getValue();
                }
                if (iden != null && "verifydocId".equalsIgnoreCase(iden.getSystem())) {
                    empChkCode = iden.getValue();
                }
            }
            Map<String, Object> occMap = QueryUtils.queryPkEmpByCode(empOccCode);
            Map<String, Object> chkMap = QueryUtils.queryPkEmpByCode(empChkCode);
/*            if (occMap == null || chkMap == null) {
                throw new BusException("通过人员编码未获取的到审核人和执行人！");
            }*/
            //查询医嘱信息
            String sql = " select o.* from cn_order o where o.code_apply=?";
            Map<String, Object> orderMap = DataBaseHelper.queryForMap(sql, applyNo);
            //未找到对应检验医嘱
            if (orderMap == null) {
                throw new BusException("未找到有效的检验医嘱,申请单号为：" + applyNo);
            }
            //获取检验结果信息(多个检验结果)
            List<ExLabOcc> exLabOccList = new ArrayList<ExLabOcc>();
            //微生物结果
            List<ExLabOccBact> exLabOccBactList = new ArrayList<ExLabOccBact>();
            List<ExLabOccBactAl> exLabOccBactAlList = new ArrayList<ExLabOccBactAl>();
            List<ResultBact> resultList = lis.getResult();//result传一个里面有多个结果
            if (resultList == null) {
                throw new BusException("检验结果为空");
            }
            //获取报告日期
            Date dateRpt = lis.getEffectiveDateTime();
            //获取审核时间
            Date dateChk = lis.getIssued();
            for(ResultBact result:resultList){
                List<ComponentBact> componentBact = result.getComponent();
                for(ComponentBact component:componentBact){
                    ExLabOcc exLabOcc = new ExLabOcc();
                    String pkLabocc = NHISUUID.getKeyId();
                    exLabOcc.setPkLabocc(pkLabocc);
                    exLabOcc.setPkOrg(MapUtils.getString(orderMap, "pkOrg"));
                    exLabOcc.setPkPi(MapUtils.getString(orderMap, "pkPi"));
                    exLabOcc.setPkPv(MapUtils.getString(orderMap, "pkPv"));
                    exLabOcc.setPkCnord(MapUtils.getString(orderMap, "pkCnord"));
                    exLabOcc.setCodeApply(MapUtils.getString(orderMap, "codeApply"));
                    exLabOcc.setPkOrgOcc(MapUtils.getString(orderMap, "pkOrgExec"));
                    exLabOcc.setPkDeptOcc(MapUtils.getString(orderMap, "pkDeptExec"));
                    exLabOcc.setPkOrd(MapUtils.getString(orderMap, "pkOrd"));
                    exLabOcc.setNameOrd(MapUtils.getString(orderMap, "nameOrd"));
                    exLabOcc.setEuType("1");
                    //审核日期
                    exLabOcc.setDateChk(dateChk);
                    //报告日期
                    exLabOcc.setDateRpt(dateRpt);

                    exLabOcc.setCodeRpt(reportNo);//报告唯一号
                    exLabOcc.setCodeSamp(applyNo);//样本号
                    exLabOcc.setDateOcc(new Date());
                    //获取审核人和报告人
                    exLabOcc.setPkEmpOcc(MapUtils.getString(occMap, "pkEmp",""));//执行人
                    exLabOcc.setNameEmpOcc(MapUtils.getString(occMap, "nameEmp","三方未对应"));
                    exLabOcc.setFlagChk("1");
                    //暂时没有序号
                    //exLabOcc.setSortNo(Integer.parseInt(""));
                    exLabOcc.setPkEmpChk(MapUtils.getString(chkMap, "pkEmp",""));//审核人
                    exLabOcc.setNameEmpChk(MapUtils.getString(chkMap, "nameEmp","三方未对应"));
                    //获取设备
                    Device device = result.getDevice();
                    if (device != null) {
                        List<Parameter> deviceName = device.getDeviceName();
                        if (deviceName != null && deviceName.size() > 0) {
                            exLabOcc.setPkMsp(MapUtils.getString(QueryUtils.queryPkMspByMspName(deviceName.get(0).getName()), "pkMsp"));
                        }
                    }
                    List<Coding> code = component.getCode().getCoding();
                    if (code == null) {
                        throw new BusException("节点结果里面code不能为空！");
                    }
                    exLabOcc.setCodeIndex(code.get(0).getCode());
                    exLabOcc.setNameIndex(code.get(0).getDisplay());
                    if(component.getValueQuantity() != null){
                        String value = component.getValueQuantity().getValue();
                        exLabOcc.setVal(value);
                        exLabOcc.setUnit(component.getValueQuantity().getUnit());
                        exLabOcc.setPkUnit(MapUtils.getString(QueryUtils.queryUnitByNameUnit(component.getValueQuantity().getUnit()), "pkUnit"));
                    }
                    if(component.getValueCodeableConcept() != null){
                        List<Coding> codeConcept = component.getValueCodeableConcept().getCoding();
                        exLabOcc.setVal1(codeConcept.get(0).getDisplay());
                    }
                    exLabOcc.setVal2(component.getValueString());
                    exLabOcc.setVal3(component.getValueBoolean());
                    exLabOcc.setDelFlag("0");
                    exLabOccList.add(exLabOcc);
                    //组装微生物报告表
                    //药敏报告，写ex_lab_occ_bact和ex_lab_occ_bact_al
                    ExLabOccBact bact = new ExLabOccBact();
                    String pkBact = NHISUUID.getKeyId();
                    bact.setPkBact(pkBact);
                    bact.setPkOrg(MapUtils.getString(orderMap, "pkOrg"));
                    bact.setPkLabocc(pkLabocc);
                    //药敏
                    if(result != null &&"YMJG".equals(result.getImplicitRules())){
                        List<Coding> code1 = component.getCode().getCoding();
                        if (code1 == null) {
                            throw new BusException("节点结果里面code不能为空！");
                        }
                        bact.setNamePd(code1.get(0).getDisplay());

                        if(component.getValueQuantity() != null){
                            String value = component.getValueQuantity().getValue();
                            bact.setValLab(value);
                        }
                        if(component.getValueCodeableConcept() != null){
                            List<Coding> codeConcept = component.getValueCodeableConcept().getCoding();
                            //bact.setEuAllevel(codeConcept.get(0).getCode());
                        }
                    }
                    //细菌
                    if(result != null &&"XJJG".equals(result.getImplicitRules())){
                        List<Coding> code2 = component.getCode().getCoding();
                        bact.setCodeBact(code2.get(0).getCode());
                        bact.setNameBact(code2.get(0).getDisplay());
                        bact.setNote(component.getValueString());
                    }
                    bact.setDelFlag("0");
                    bact.setTs(new Date());
                    exLabOccBactList.add(bact);
                    ExLabOccBactAl bactAl = new ExLabOccBactAl();
                    bactAl.setPkBact(pkBact);
                    bactAl.setPkBactal(NHISUUID.getKeyId());
                    bactAl.setPkOrg(MapUtils.getString(orderMap, "pkOrg"));
                    //bactAl.setSortNo(Integer.parseInt(SDMsgUtils.getPropValueStr(obxMap, "id")));
                    if(result != null &&"YMJG".equals(result.getImplicitRules())) {
                        List<Coding> code3 = component.getCode().getCoding();
                        if (code3 == null) {
                            throw new BusException("节点结果里面code不能为空！");
                        }
                        bactAl.setNamePd(code3.get(0).getDisplay());
                        bactAl.setCodePd(code3.get(0).getCode());
                        if(component.getValueQuantity() != null){
                            String value = component.getValueQuantity().getValue();
                            bactAl.setValLab(value);
                        }
                    }
                    //bactAl.setMic(0.00);最强抑菌浓度
                    bactAl.setEuAllevel("");
                    bactAl.setDelFlag("0");
                    bactAl.setTs(new Date());
                    exLabOccBactAlList.add(bactAl);

                }
            }
            //保存结果
            if (exLabOccList.size() == 0) {
                throw new BusException("数据错误，组装数据错误！");
            }
            if (exLabOccList.size() > 0) {
                QueryUtils.saveLisRptList(MapUtils.getString(orderMap, "pkCnord"), applyNo, exLabOccList);
            }
            if(exLabOccBactAlList!=null && exLabOccBactList!=null){
                QueryUtils.saveBactRptList(applyNo,exLabOccBactList,exLabOccBactAlList);
            }

        }
    }

    /**
     * 微生物检验报告回收
     * @param param
     */
    @Override
    public void deleteBactReportRecovery(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到检验数据节点
        List<Lis> lisList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                lisList.add(TransfTool.mapToBean(Lis.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(lisList)){
            throw new BusException("未传入检验信息");
        }
        for(Lis lis: lisList){
            List<Identifier> identifier = lis.getIdentifier();
            if(CollectionUtils.isEmpty(identifier)){
                throw new BusException("identifier集合为空");
            }
            //获取检验单号和检验报告单号
            String applyNo = null;//申请单号
            String reportNo = null;//报告单号
            if(CollectionUtils.isNotEmpty(identifier)){
                for(Identifier id :identifier){
                    if(id!=null && "code/applyno".equalsIgnoreCase(id.getSystem())){
                        applyNo = id.getValue();
                    }
                    if(id!=null && "code/reportno".equalsIgnoreCase(id.getSystem())){
                        reportNo = id.getValue();
                    }
                }
            }
            //查询医嘱信息
            String sql = " select o.* from cn_order o where  o.code_apply=?";
            CnOrder orderMap = DataBaseHelper.queryForBean(sql,CnOrder.class,applyNo);
            //未找到对应检验医嘱
            if(orderMap==null){
                throw new BusException("未找到有效的检验医嘱,申请单号为："+applyNo);
            }
            QueryUtils.deleteBactReportList(orderMap.getPkCnord(),applyNo);
        }

    }

    /**
     * 检查报告发布
     * @param param
     */
    @Override
    public void saveRisReportRelease(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到检验数据节点
        List<Ris> risList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                risList.add(TransfTool.mapToBean(Ris.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(risList)){
            throw new BusException("未传入检查信息");
        }
        for(Ris ris: risList){
            List<Identifier> identifier = ris.getIdentifier();
            if(CollectionUtils.isEmpty(identifier)){
                throw new BusException("identifier集合为空");
            }
            if(ris.getSubject() == null){
                throw new BusException("Subject对象为空");
            }
            if(ris.getResult() == null){
                throw new BusException("Result对象为空");
            }

            //获取检验单号和检验报告单号
            String applyNo = null;//申请单号
            String reportNo = null;//报告单号
            String pkCnord = null;//报告单号
            if(CollectionUtils.isNotEmpty(identifier)){
                //Identifier identifier = appointment.getIdentifier().get(0);
                for(Identifier id :identifier){
                    if(id!=null && "code/applyno".equalsIgnoreCase(id.getSystem())){
                        applyNo = id.getValue();
                    }
                    if(id!=null && "code/reportno".equalsIgnoreCase(id.getSystem())){
                        reportNo = id.getValue();
                    }
                    if(id!=null && "id/applyDetailNo".equalsIgnoreCase(id.getSystem())){
                        pkCnord = id.getValue();
                    }
                }
            }
            if(StringUtils.isBlank(pkCnord)){
                throw new BusException("identifier中未传入id/applyDetailNo");
            }
            //获取患者codeOp
            String codeOp = null;
            if(ris.getSubject()!=null && "patientid".equalsIgnoreCase(ris.getSubject().getIdentifier().getSystem())){
                codeOp = ris.getSubject().getIdentifier().getValue();
            }
            if(StringUtils.isBlank(codeOp)){
                throw new BusException("未获取到患者的codeOp");
            }
            //获取病人信息 codeOp
            List<Map<String, Object>> queryPiByCodeOp = QueryUtils.queryPiByCodeOp(codeOp);
            if(queryPiByCodeOp==null || queryPiByCodeOp.size()==0){
                throw new BusException("没有查到该病人信息:"+codeOp);
            }
            //获取审核人和报告人

            List<Locationlis> performer = ris.getPerformer();
            if(CollectionUtils.isEmpty(performer)){
                throw new BusException("performer节点不能为空:");
            }
            String empOccCode = null;
            String empChkCode = null;
            for(Locationlis lo : performer){
                Identifier iden = lo.getIdentifier();
                if(iden!=null && "reportdocId".equalsIgnoreCase(iden.getSystem())){
                    empOccCode = iden.getValue();
                }
                if(iden!=null && "verifydocId".equalsIgnoreCase(iden.getSystem())){
                    empChkCode = iden.getValue();
                }
            }
            Map<String,Object> occMap = QueryUtils.queryPkEmpByCode(empOccCode);
            Map<String,Object> chkMap = QueryUtils.queryPkEmpByCode(empChkCode);
/*            if(occMap == null||chkMap == null){
                throw new BusException("通过人员编码未获取到审核人和执行人！");
            }*/
            //查询医嘱信息
            String sql = " select o.* from cn_order o where o.pk_cnord=?";
            CnOrder orderMap = DataBaseHelper.queryForBean(sql,CnOrder.class,pkCnord);
            //未找到对应检验医嘱
            if(orderMap==null){
                throw new BusException("未找到有效的检查医嘱,医嘱主键为："+pkCnord);
            }
            //获取检验结果信息(多个检验结果)
            List<ExRisOcc> exRisOccList = new ArrayList<ExRisOcc>();
            List<ResultRis> resultList = ris.getResult();//result传一个里面有多个结果
            if(resultList == null){
                throw new BusException("检查结果为空");
            }
            //获取报告日期
            Date dateRpt = ris.getEffectiveDateTime();
            //获取审核时间
            Date dateChk = ris.getIssued();
            for(ResultRis result :resultList){
                ExRisOcc exRisOcc = new ExRisOcc();
                String pkRisocc = NHISUUID.getKeyId();
                exRisOcc.setPkRisocc(pkRisocc);
                exRisOcc.setPkOrg(orderMap.getPkOrg());
                exRisOcc.setPkPi(orderMap.getPkPi());
                exRisOcc.setPkPv(orderMap.getPkPv());
                exRisOcc.setPkCnord(orderMap.getPkCnord());
                exRisOcc.setCodeApply(orderMap.getCodeApply());
                exRisOcc.setPkOrgOcc(orderMap.getPkOrdExc());
                exRisOcc.setPkDeptOcc(orderMap.getPkDeptExec());
                exRisOcc.setPkOrd(orderMap.getPkOrd());
                exRisOcc.setNameOrd(orderMap.getNameOrd());
                exRisOcc.setTs(new Date());
                exRisOcc.setCreateTime(new Date());
                //审核日期
                exRisOcc.setDateChk(dateChk);
                //报告日期
                exRisOcc.setDateRpt(dateRpt);

                exRisOcc.setCodeRpt(reportNo);//报告唯一号
                exRisOcc.setDateOcc(new Date());
                //获取审核人和报告人
                exRisOcc.setPkEmpOcc(MapUtils.getString(occMap,"pkEmp",""));//执行人
                exRisOcc.setNameEmpOcc(MapUtils.getString(occMap,"nameEmp","三方未对应"));
                exRisOcc.setFlagChk("1");
                //暂时没有序号
                //exLabOcc.setSortNo(Integer.parseInt(""));
                exRisOcc.setPkEmpChk(MapUtils.getString(chkMap,"pkEmp",""));//审核人
                exRisOcc.setNameEmpChk(MapUtils.getString(chkMap,"nameEmp","三方未对应"));
                if(result.getMethod() != null){
                    exRisOcc.setExamway(result.getMethod().getText());
                }
                if(result.getComponent()== null){
                    throw new BusException("检查结果能为空!!");
                }
                for(Parameter co :result.getComponent()){
                    if(co != null && co.getCode()!= null && "检查所见".equals(co.getCode().getText())){
                        exRisOcc.setResultObj(co.getValueString());
                    }
                    if(co != null && co.getCode()!= null && "检查结论".equals(co.getCode().getText())){
                        exRisOcc.setResultSub(co.getValueString());
                    }
                }
                //获取设备
                List<Media> mediaList = ris.getMedia();
                if(mediaList == null){
                    throw new BusException("Media节点不能为空！");
                }
                Link link = mediaList.get(0).getLink();
                if(link != null){
                    String deviceName = link.getDeviceName();
                    exRisOcc.setPkMsp(MapUtils.getString(QueryUtils.queryPkMspByMspName(deviceName),"pkMsp"));

                }
                if(link.getContent() != null){
                    exRisOcc.setAddrImg(link.getContent().getUrl());
                }
                exRisOcc.setDelFlag("0");
                exRisOccList.add(exRisOcc);

            }
            //保存结果
            int count = 0;
            if(exRisOccList.size()>0){
                count = QueryUtils.saveRisRptList(orderMap.getPkCnord(),applyNo,exRisOccList);
            }
            if(count == 0){
                throw new BusException("数据错误，组装数据错误！");
            }
        }

    }

    /**
     * 检查报告回收
     * @param param
     */
    @Override
    public void deleteRisReportRecovery(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到检验数据节点
        List<Ris> RisList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                RisList.add(TransfTool.mapToBean(Ris.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(RisList)){
            throw new BusException("未传入检验信息");
        }
        for(Ris ris: RisList){
            List<Identifier> identifier = ris.getIdentifier();
            if(CollectionUtils.isEmpty(identifier)){
                throw new BusException("identifier集合为空");
            }
            //获取检验单号和检验报告单号
            String applyNo = null;//申请单号
            String reportNo = null;//报告单号
            String pkCnord = null;//医嘱号
            if(CollectionUtils.isNotEmpty(identifier)){
                for(Identifier id :identifier){
                    if(id!=null && "code/applyno".equalsIgnoreCase(id.getSystem())){
                        applyNo = id.getValue();
                    }
                    if(id!=null && "code/reportno".equalsIgnoreCase(id.getSystem())){
                        reportNo = id.getValue();
                    }
                    if(id!=null && "id/applyDetailNo".equalsIgnoreCase(id.getSystem())){
                        pkCnord = id.getValue();
                    }
                }
            }
            if(StringUtils.isBlank(pkCnord)){
                throw new BusException("identifier中未传入id/applyDetailNo");
            }
            //查询医嘱信息
            String sql = " select o.pk_cnord from cn_order o where  o.pk_cnord=?";
            CnOrder orderMap = DataBaseHelper.queryForBean(sql,CnOrder.class,pkCnord);
            //未找到对应检验医嘱
            if(orderMap==null){
                throw new BusException("未找到有效的检验医嘱,医嘱主键为："+pkCnord);
            }
            QueryUtils.deleteRisRptList(orderMap.getPkCnord(),applyNo);
        }

    }


    /**
     * 检查预约
     * @param param
     */
    @Override
    public void updateMedicalMake(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到数据节点
        List<RisSch> RisList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                RisList.add(TransfTool.mapToBean(RisSch.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(RisList)){
            throw new BusException("未传入预约信息");
        }
        List<String> list = new ArrayList<>();
        for(RisSch ch:RisList){
            List<Identifier> identifier = ch.getIdentifier();
            // codeApply = "";
            int i =0;
            List<Extension> extension = ch.getExtension();
            List<Actor> actor = ch.getActor();
            Date appointVisitDate = null;
            Date appointVisitDateStart = null;
            Date appointVisitDateEnd = null;
            String guideInfo = null;
            for(Extension ex:extension){
                if(ex != null && "AppointVisitDate".equals(ex.getUrl())){
                    String appointDate = ex.getValueDate();
                    if(StringUtils.isNotBlank(appointDate)){
                        appointVisitDate = DateUtils.strToDate(appointDate+" 00:00:00","yyyy-MM-dd HH:mm:ss");
                        appointVisitDateStart = DateUtils.strToDate(appointDate+" "+ch.getStart(),"yyyy-MM-dd HH:mm:ss");
                        appointVisitDateEnd = DateUtils.strToDate(appointDate+" "+ch.getEnd(),"yyyy-MM-dd HH:mm:ss");

                    }
                }
                if(ex != null && "GuideInfo".equals(ex.getUrl())){
                    guideInfo = ex.getValueString();
                }
            }
            String address = null;
            String codeEmp = null;
            for(Actor ac:actor){
                if(ac != null && "Location".equals(ac.getResourceType())){
                    TextElement textElement = ac.getAddress();
                    address = textElement.getText();
                }
                if(ac != null && "Practitioner".equals(ac.getResourceType())){
                    List<Identifier> identifierList = ac.getIdentifier();
                    if(identifierList!=null && "operator".equals(identifierList.get(0).getSystem())){
                        codeEmp = identifierList.get(0).getValue();
                    }
                }
            }
            String pkEmp = null;
            String nameEmp = null;
            Map<String, Object> empMap = DataBaseHelper.queryForMap("select * from bd_ou_employee where code_emp=?",codeEmp);
            if(empMap !=null){
                pkEmp = MapUtils.getString(empMap,"pkEmp");
                nameEmp = MapUtils.getString(empMap,"nameEmp");
            }
            Date created = null;
            if(ch.getCreated()!=null){
                created = DateUtils.strToDate(ch.getCreated(),"yyyy-MM-dd HH:mm:ss");
            }
            for(Identifier id:identifier){
                if(id != null && "id/applyDetailNo".equals(id.getSystem())){
                    i++;
                    String  pkCnord= id.getValue();
                    if(StringUtils.isBlank(pkCnord)){
                        throw new BusException("未获取到申请单的明细主键！！");
                    }
                    //查询医嘱信息
/*                    String sql = " select o.pk_cnord from cn_order o where  o.pk_cnord=?";
                    CnOrder orderMap = DataBaseHelper.queryForBean(sql,CnOrder.class,pkCnord);
                    //未找到对应检验医嘱
                    if(orderMap==null){
                        throw new BusException("未找到有效的医嘱,医嘱主键为："+pkCnord);
                    }*/

                    if("booked".equals(ch.getStatus())){//预约
                        // 更新检查申请单
                        DataBaseHelper.update("update EX_ASSIST_OCC set date_appt = ?,note = ? where pk_cnord = ?",new Object[] {appointVisitDate,address,pkCnord });
                        //list.add("update cn_ris_apply set eu_status = '2',note = '"+ch.getComment()+"',addr_appo = '"+address+"',notice_appo = '"+ch.getDescription()+"'," +
                        //        "note_appo = '"+ch.getComment()+"',date_begin_appo =to_date('"+appointVisitDateStart+"','yyyy-MM-DD HH24:MI:SS') ,date_end_appo =to_date('"+appointVisitDateEnd+"','yyyy-MM-DD HH24:MI:SS'),eu_status_appo = '1' where pk_cnord = '"+pkCnord+"'");
                        DataBaseHelper.update("update cn_ris_apply set eu_status = '2',note = ?,addr_appo = ?,notice_appo =?，note_appo = ?,date_begin_appo =? ,date_end_appo =?,eu_status_appo = '1',pk_emp_appo = ?,name_emp_appo = ?,date_appo =? where pk_cnord = ? ", new Object[] {guideInfo,address,ch.getDescription(),ch.getComment(),appointVisitDateStart,appointVisitDateEnd,pkEmp,nameEmp,created,pkCnord });
                        // 更新病理申请单
                        list.add("update cn_pa_apply set eu_status = '2' where pk_cnord = '"+pkCnord+"'");
                        //DataBaseHelper.update("update cn_pa_apply set eu_status = '2' where pk_cnord = ?", new Object[] {pkCnord });
                    }else if("failure".equals(ch.getStatus())){//预约失败
                        list.add("update cn_ris_apply set note = '预约失败',eu_status_appo = '2' where pk_cnord = '"+pkCnord+"'");
                        //DataBaseHelper.update("update cn_ris_apply set note = '预约失败' where pk_cnord = ?", new Object[] {pkCnord });
                    }else if("cancelled".equals(ch.getStatus())){//取消预约
                        String note = "";
                        if(ch.getCancelationReason() != null){
                            note = ch.getCancelationReason().getText();
                        }
                        // 更新检查申请单
                        list.add("update cn_ris_apply set eu_status = '5',note = '"+note+"',addr_appo = null,notice_appo = null,note_appo = null,date_begin_appo = null,date_end_appo = null,eu_status_appo = '9',pk_emp_appo = null,name_emp_appo = null where pk_cnord = '"+pkCnord+"'");
                        //DataBaseHelper.update("update cn_ris_apply set eu_status = '5',note = ? where pk_cnord = ?", new Object[] {note,pkCnord });
                    }else if("windosReserve".equals(ch.getStatus())){
                        list.add("update cn_ris_apply set note = '人工窗口预约',eu_status_appo = '3' where pk_cnord = '"+pkCnord+"'");
                    }else if("noapplyNo".equals(ch.getStatus())){
                        list.add("update cn_ris_apply set note = '未知的申请单号',eu_status_appo = null where pk_cnord = '"+pkCnord+"'");
                    }else if("nomessage".equals(ch.getStatus())){
                        list.add("update cn_ris_apply set note = '未知情况请稍后查询或咨询检查登记处',eu_status_appo = null where pk_cnord = '"+pkCnord+"'");
                    }else if("unReserve".equals(ch.getStatus())){
                        list.add("update cn_ris_apply set note = '无需预约',eu_status_appo = '4' where pk_cnord = '"+pkCnord+"'");
                    }
                }
            }
            if(list.size()>0){
                DataBaseHelper.batchUpdate(list.toArray(new String[0]));
            }

        }


    }

    /**
     * 3.16 门诊处方信息查询
     * @param param
     * @return
     */
    @Override
    public List<Entry> getOpCnorder(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("未查询到相关数据！！");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);

        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientno"))//身份证号
                && StringUtils.isBlank(MapUtils.getString(paramMap,"visitno"))//电话号码
                && StringUtils.isBlank(MapUtils.getString(paramMap,"begindate"))//姓名
                && StringUtils.isBlank(MapUtils.getString(paramMap,"enddate"))) {//患者编码
            throw new BusException("至少传入一个查询参数patientno|visitno|begindate|enddate");
        }
        //这三个条件有任何一个有值，就判断三个都必须有值
        if(StringUtils.isNotBlank(MapUtils.getString(paramMap,"begindate"))
                ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"enddate"))){
            if(StringUtils.isBlank(MapUtils.getString(paramMap,"begindate"))
                    ||StringUtils.isBlank(MapUtils.getString(paramMap,"enddate"))){
                throw new BusException("组合查询必须包含begindate+enddate");
            }
        }

         List<Entry> entryList = new ArrayList<>();
        List<Map<String, Object>> cnOrderList = zsrmCnMapper.getOpCnOrderBdOrd(paramMap);
        cnOrderList.addAll(zsrmCnMapper.getOpCnOrderBdPd(paramMap));
        Set<String> pkCnordSet = new HashSet<>();
        for (Map<String, Object> main : cnOrderList) {
            pkCnordSet.add(MapUtils.getString(main, "pkCnord"));
        }
        Map<String, Map<String, Object>> blMap = DataBaseHelper.queryListToMap("select dt.PK_CNORD as key_,employee.CODE_EMP code_emp_st,employee.NAME_EMP name_emp_st from BL_OP_DT dt " +
                "inner join BL_SETTLE bl on bl.PK_SETTLE = dt.PK_SETTLE left join bd_ou_employee  employee on employee.PK_EMP = bl.pk_emp_st where dt.PK_CNORD in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ")");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for(Map<String, Object> cnOrderMap:cnOrderList){
            OpCnOrder opCnOrder = new OpCnOrder();
            String outPrescriptionState =(String)cnOrderMap.get("outPrescriptionState");
            if (outPrescriptionState!=null&&outPrescriptionState!=""){
                Map<String, Object> stringObjectMap = DataBaseHelper.queryForMap("select bd.name as names  from BD_DEFDOC bd where bd.CODE_DEFDOCLIST='060103' and bd.code='" + outPrescriptionState + "'");
                if (stringObjectMap!=null&&stringObjectMap.size()>0){
                    opCnOrder.setOutPrescriptionState(stringObjectMap.get("names").toString());
                    System.out.println(opCnOrder.getOutPrescriptionState());
                }
            }
            if (cnOrderMap.get("outPrescriptionFlag")!=null&&cnOrderMap.get("outPrescriptionFlag")!=""){
                String outPrescriptionFlag =(String)cnOrderMap.get("outPrescriptionFlag");
                opCnOrder.setOutPrescriptionFlag(outPrescriptionFlag);
            }
            opCnOrder.setResourceType("MedicationRequest");
            opCnOrder.setId(requestBody.getId());
            opCnOrder.setImplicitRules("getoutrecipeinfo");
            //处方序号-医嘱序号
            opCnOrder.setIdentifier(Arrays.asList(new Identifier(null,"id/recipeNo",MapUtils.getString(cnOrderMap,"ordsn", ""))));
            //处方状态N-开立 V-审核 S-收费 E-执行 C-取消
            opCnOrder.setStatus(TransfTool.getEuStatusOrd(MapUtils.getString(cnOrderMap, "euStatusOrd")));
            //状态原因
            opCnOrder.setStatusReason(new TextElement(""));
            //处方类别
            opCnOrder.setCategory(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("orderCategory",MapUtils.getString(cnOrderMap,"dtPrestype",""),TransfTool.getBdDefdocName("060101", MapUtils.getString(cnOrderMap,"dtPrestype")))))));
            //处方项目
            opCnOrder.setMedicationCodeableConcept(new CodeableConcept(Arrays.asList(new Coding("orderItem",MapUtils.getString(cnOrderMap, "code"),MapUtils.getString(cnOrderMap, "name")))));
            //项目属性
            opCnOrder.setMedicationReference(new Reference("Medication",new CodeableConcept(Arrays.asList(new Coding("drugForm","/","/"))),new Numerator(MapUtils.getString(cnOrderMap, "quan"),MapUtils.getString(cnOrderMap, "unitName")),Arrays.asList(new Extension("spec",MapUtils.getString(cnOrderMap,"spec"),null))));
            //患者基本信息codeOp、姓名、性别
            opCnOrder.setSubject(new Reference("Patient",new Identifier("code/patientId",MapUtils.getString(cnOrderMap,"codeOp")),Arrays.asList(new TextElement(MapUtils.getString(cnOrderMap,"namePi"))),TransfTool.getSex(MapUtils.getString(cnOrderMap, "dtSex"))));
            //申请单患者就诊信息
            opCnOrder.setEncounter(new Reference("Encounter",new Identifier("code/visitNo",MapUtils.getString(cnOrderMap,"codePv")),TransfTool.getEuPvtype(MapUtils.getString(cnOrderMap,"euPvtype"))));
            //开方科室、病区
            opCnOrder.setSupportingInformation(Arrays.asList(new Reference("Location",new Identifier("id/orderDeptId",MapUtils.getString(cnOrderMap,"codeDept")),MapUtils.getString(cnOrderMap,"nameDept"))));
            //处方开立时间
            opCnOrder.setAuthoredOn(TransfTool.getPropValueDates(cnOrderMap,"dateEnter"));
            //开方医生
            opCnOrder.setRequester(new Reference("Practitioner",new Identifier("code/requesterId",MapUtils.getString(cnOrderMap,"codeEmp")),Arrays.asList(new TextElement(MapUtils.getString(cnOrderMap,"nameEmp")))));
            //审核医生
            opCnOrder.setPerformer(new Reference("Practitioner",new Identifier("code/performerId",MapUtils.getString(cnOrderMap,"empchkCode")),Arrays.asList(new TextElement(MapUtils.getString(cnOrderMap,"empchkName")))));
            //录入者
            opCnOrder.setRecorder(new Reference("Practitioner",new Identifier("code/recorderId",MapUtils.getString(cnOrderMap,"empinputCode")),MapUtils.getString(cnOrderMap,"empinputName")));
            //组号
            opCnOrder.setGroupIdentifier(new Reference(new Identifier("code/groupId",MapUtils.getString(cnOrderMap,"groupno"))));
            //嘱托
            opCnOrder.setNote(Arrays.asList(new TextElement(MapUtils.getString(cnOrderMap,"note"))));
            //药品使用说明
            opCnOrder.setDosageInstruction(Arrays.asList(new Reference(null,new CodeableConcept(Arrays.asList(new Coding("code/route",MapUtils.getString(cnOrderMap,"supplyCode"),MapUtils.getString(cnOrderMap,"supplyName")))),Arrays.asList(new Timing(new TextElement(MapUtils.getString(cnOrderMap,"freqName")),new Numerator(MapUtils.getString(cnOrderMap,"dosage"),MapUtils.getString(cnOrderMap,"unitdosName")))),new Identifier(null,MapUtils.getString(cnOrderMap,"quanDisp")))));
            //处方有效期、执行科室
            opCnOrder.setDispenseRequest(new Reference(new ValuePeriod(TransfTool.getPropValueDates(cnOrderMap,"dateEnter"),TransfTool.getPropValueDates(cnOrderMap,"dateEffe")),new Reference("Organization",MapUtils.getString(cnOrderMap,"nameDeptexe"),Arrays.asList(new Identifier("code/perDeptid",MapUtils.getString(cnOrderMap,"codeDeptexe"))))));

            List<BdExtension> extensionList = new ArrayList<>();
            if(blMap !=null){
                BdExtension tollman = new BdExtension();
                tollman.setUrl("tollman");
                Map<String, Object> map = blMap.get(MapUtils.getString(cnOrderMap, "pkCnord"));
                tollman.setValueCoding(new Coding(MapUtils.getString(map, "codeEmpSt", ""), MapUtils.getString(map, "nameEmpSt", "")));
                extensionList.add(tollman);
            }
            BdExtension thePharmacist = new BdExtension();
            thePharmacist.setUrl("thePharmacist");
            thePharmacist.setValueCoding(new Coding(MapUtils.getString(cnOrderMap, "codeEmpConf", ""), MapUtils.getString(cnOrderMap, "nameEmpConf", "")));
            extensionList.add(thePharmacist);
            opCnOrder.setExtension(extensionList);
            linkedHashMap.clear();
            linkedHashMap.put("code", "天");
            linkedHashMap.put("value", cnOrderMap.get("days"));
            opCnOrder.getDispenseRequest().setExpectedSupplyDuration(linkedHashMap);
            //创建响应数据
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setOutcome(BeanMap.create(opCnOrder));
            entryList.add(entry);
        }

        return entryList;
    }



    /**
     * 3.71 入院申请变更接口
     * @param param
     */
    @Override
    public void pushStatus(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到申请单数据节点
        List<Map<String,Object>> mapList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                mapList.add(TransfTool.mapToBean(Map.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(mapList)){
            throw new BusException("未传入申请单信息");
        }
        //每条消息只有一个申请单
        for(Map<String,Object> map: mapList){
            if(MapUtils.getString(map,"hisOrderId") == null){
                throw new BusException("未传入hisOrderId信息");
            }
            if(MapUtils.getString(map,"status") == null){
                throw new BusException("未传入status信息");
            }
            //查询医嘱信息
            String sql = " select pv.pk_pv from pv_encounter pv inner join pv_ip_notice ipno on pv.pk_pv = ipno.PK_PV_OP where pv.CODE_PV = ?";
            List<Map<String,Object>> orderMap = DataBaseHelper.queryForList(sql,MapUtils.getString(map,"hisOrderId"));
            //未找到对应检验医嘱
            if(orderMap==null ||orderMap.size()==0){
                throw new BusException("未找到有效的就诊记录,hisOrderId为："+MapUtils.getString(map,"hisOrderId"));
            }
            //入院
            if("50".equals(MapUtils.getString(map,"status"))){
                DataBaseHelper.update("update pv_ip_notice set EU_STATUS = '2' where PK_PV_OP in (select pk_pv from PV_ENCOUNTER where CODE_PV = ?)",new Object[] {MapUtils.getString(map,"hisOrderId")});
            }else if("70".equals(MapUtils.getString(map,"status"))){//取消入院
                DataBaseHelper.update("update pv_ip_notice set EU_STATUS = '9' where PK_PV_OP in (select pk_pv from PV_ENCOUNTER where CODE_PV = ?)",new Object[] {MapUtils.getString(map,"hisOrderId")});
            }
        }

    }

    /**
     * 检查预约查询
     * @param param
     * @return
     */
    @Override
    public List<Entry> getRisAppointList(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList =requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "ServiceRequest".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getParameter();
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientNo"))){
            throw new BusException("患者Id：patientNo不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"visitNo"))){
            throw new BusException("就诊号：visitNo不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"codeSt"))){
            throw new BusException("结算业务号：codeSt不能为空");
        }
        List<Map<String,Object>> risAppointList = zsphLisRisMapper.getRisAppointList(paramMap);
        if(CollectionUtils.isEmpty(risAppointList)){
            throw new BusException("通过该参数，没有数据！（业务处理失败|查询无结果）");
        }
        List<Entry> entryList = new ArrayList<>();
        for(Map<String, Object> app:risAppointList){
            RisSch risSch = new RisSch();
            risSch.setIdentifier(new ArrayList<>());
            risSch.getIdentifier().add(new Identifier("id/applyDetailNo",MapUtils.getString(app,"pkCnord")));//医嘱号
            risSch.getIdentifier().add(new Identifier("id/applyNo",MapUtils.getString(app,"codeApply")));//申请单号
            risSch.getIdentifier().add(new Identifier("id/bookNo",""));//申请单号
            if("1".equals(MapUtils.getString(app,"euStatusAppo"))){
                risSch.setStatus("booked");
            }else if("2".equals(MapUtils.getString(app,"euStatusAppo"))){
                risSch.setStatus("reserveIng");
            }else{
                risSch.setStatus("unReserve");
            }
            risSch.setCancelationReason(new TextElement(MapUtils.getString(app,"note")));
            if(StringUtils.isNotBlank(MapUtils.getString(app,"date_begin_appo"))){
                risSch.setStart(new DateTime(MapUtils.getString(app,"date_begin_appo")).toString("HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(MapUtils.getString(app,"date_end_appo"))){
                risSch.setEnd(new DateTime(MapUtils.getString(app,"date_end_appo")).toString("HH:mm:ss"));
            }
            risSch.setCreated(MapUtils.getString(app,"dateAppo"));
            Actor actor = new Actor();
            actor.setResourceType("Practitioner");
            actor.setIdentifier(Arrays.asList(new Identifier("operator",MapUtils.getString(app,"codeEmpAppo")==null?"":MapUtils.getString(app,"codeEmpAppo"))));
            actor.setName(Arrays.asList(new TextElement(MapUtils.getString(app,"nameEmpAppo")==null?"":MapUtils.getString(app,"nameEmpAppo"))));
            Actor actorLocation = new Actor();
            actorLocation.setResourceType("Location");
            actorLocation.setIdentifier(Arrays.asList(new Identifier("DeptCode",MapUtils.getString(app,"codeDeptExec")==null?"":MapUtils.getString(app,"codeDeptExec"))));
            actorLocation.setName(MapUtils.getString(app,"nameDeptExec")==null?"":MapUtils.getString(app,"nameDeptExec"));
            actorLocation.setAddress(new TextElement(MapUtils.getString(app,"addrAppo")==null?"":MapUtils.getString(app,"addrAppo")));
            risSch.setActor(new ArrayList<>());
            risSch.getActor().add(actor);
            risSch.getActor().add(actorLocation);
            risSch.setDescription(MapUtils.getString(app,"noticeAppo")==null?"":MapUtils.getString(app,"noticeAppo"));
            risSch.setComment(MapUtils.getString(app,"noteAppo")==null?"":MapUtils.getString(app,"noteAppo"));
            Extension extension = new Extension();
            extension.setUrl("AppointVisitDate");
            extension.setValueDate(MapUtils.getString(app,"dateBeginAppo")==null?"":MapUtils.getString(app,"dateBeginAppo"));
            risSch.setExtension(Arrays.asList(extension));
            //创建响应数据
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setOutcome(BeanMap.create(risSch));
            entryList.add(entry);
        }

        return entryList;

    }

    /**
     * @date 2021/05/21 13:56
     * @Description 3.101.查询门诊病历信息
     */

    @Override
    public List<Entry> getMzRecordInfo(String param) {
        //解析参数
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList =requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "Parameters".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getParameter();
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        String codePi=MapUtils.getString(paramMap,"patientno");
        String visitno=MapUtils.getString(paramMap,"visitno");
        String idno=MapUtils.getString(paramMap,"idno");
        if(StringUtils.isBlank(codePi)&&StringUtils.isBlank(visitno)&&StringUtils.isBlank(idno)){
            throw new BusException("患者Id,门诊号,证件号至少有一样不为空");
        }
        //查询数据库
        List<MzRecordInfoVo> mzRecordInfoVos = zsrmCnMapper.queryMzRecordInfo(paramMap);
        if (CollectionUtils.isEmpty(mzRecordInfoVos)||mzRecordInfoVos.size()==0){
            throw new BusException("未查询到结果");
        }
        List<Entry> entryList=new ArrayList<>();
        for (MzRecordInfoVo mzRecordInfoVo:mzRecordInfoVos){
            //包装返回参数

            Entry entry=new Entry();
            MzRecordInfoResponse response = new MzRecordInfoResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setStatus("active");
            //封装diagnose对象
            response.setDiagnose(new TextElement(mzRecordInfoVo.getDiagnose()));
            //封装disrecord对象
            response.setDisrecord(new TextElement(mzRecordInfoVo.getTreatmentPrograms()));
            //封装encounter对象
            EncounterBact encounter = new EncounterBact();
            Coding euPvtype = TransfTool.getEuPvtype(mzRecordInfoVo.getEuPvType());
            encounter.setClas(new Coding(euPvtype.getCode(),euPvtype.getDisplay()));
            String bedNo = mzRecordInfoVo.getBedNo();
            if (bedNo==null){
                bedNo="";
            }
            String cntOp = mzRecordInfoVo.getCntOp();
            if (cntOp==null){
                cntOp="";
            }
            //封装encounter对象下的Identifiers数组
            encounter.setIdentifier(Lists.newArrayList(
                    new Identifier("visitNo",mzRecordInfoVo.getCodePv()),
                    new Identifier("times",cntOp),
                    new Identifier("bedNo",bedNo)));
            response.setEncounter(encounter);
            //封装Identifier数组
            response.setIdentifier(Lists.newArrayList(
                    new Identifier("orderNo",mzRecordInfoVo.getPkEmrop())));
            //封装pasthistory对象
            response.setPasthistory(new TextElement(mzRecordInfoVo.getHistory()));
            //封装subject对象
            MzRecordSubject subject = new MzRecordSubject();
            subject.setBirthDate(mzRecordInfoVo.getBirthDate());
            String sex = TransfTool.getSex(mzRecordInfoVo.getDtSex());
            subject.setGender(sex);
            subject.setIdentifier(Lists.newArrayList(new Identifier("system",codePi)));
            subject.setName(new TextElement(mzRecordInfoVo.getNamePi()));
            response.setSubject(subject);
            response.setVisitTime(mzRecordInfoVo.getVisitTime()==null?"":mzRecordInfoVo.getVisitTime());
            entry.setResponse(response);
            entryList.add(entry);
        }

        return entryList;
    }

    /**
     * 物价接口查询
     * @param param
     * @return
     */
    @Override
    public List<Entry> getPriceInquiry(String param){
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList = new ArrayList<>();
        requestBody.getEntry().forEach(temp -> {
            if(temp.getResource().getParameter() !=null && temp.getResource().getParameter().size() != 0){
                parameterList.addAll(temp.getResource().getParameter());
            }
        });
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("未传入parameter");
        }
        Map<String,Object> paramMap = new HashMap<>();
        parameterList.forEach(p -> {
            paramMap.put("name",p.getName());
            paramMap.put("dCode", p.getdCode());
            paramMap.put("pyCode",p.getPyCode());
        });
        List<PriceInquiryVo> priceInquiryVoList = zsphPlatFormSendOpMapper.getPriceInquiry(paramMap);
        if(CollectionUtils.isEmpty(priceInquiryVoList) || priceInquiryVoList.size() == 0){
            throw new BusException("未查询到结果");
        }
        List<Entry> entryList = new ArrayList<>();
        Entry entry = new Entry();
        Response response = new Response();
        Map<String,Object> outcome = new HashMap<>();
        Issue issue = new Issue();
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        outcome.put("resourceType","OperationOutcome");
        issue.setDiagnostics("成功！");
        issue.setData(priceInquiryVoList);
        outcome.put("issue",issue);
        response.setOutcome(outcome);
        entry.setResponse(response);
        entryList.add(entry);
        return entryList;
    }
}
