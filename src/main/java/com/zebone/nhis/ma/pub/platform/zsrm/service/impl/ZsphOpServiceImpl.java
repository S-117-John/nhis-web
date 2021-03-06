package com.zebone.nhis.ma.pub.platform.zsrm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.internal.LinkedTreeMap;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnDiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiagDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvPe;
import com.zebone.nhis.common.module.pv.PvQue;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.EncounterBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.OpSendMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchApptVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.LocationOp;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.triage.TriageVo;
import com.zebone.nhis.ma.pub.platform.zsrm.bus.RegCommonService;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsphOpMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsrmPiMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.zsrm.service.ZsphOpService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.QueryUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.PiMasterRegVo;
import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZsphOpServiceImpl implements ZsphOpService {

    private static final Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");
    @Resource
    private ZsphOpMapper zsphOpMapper;
    @Resource
    private OpSendMapper opSendMapper;
    @Resource
    ZsrmPiMapper zsrmPiMapper;
    @Autowired
    private RegCommonService regCommonService;
    @Autowired
    private TicketPubService ticketPubService;
    @Autowired
    private OpCgPubService opCgPubService;
    @Override
    public void getPvOpInfo(String param, ResultListener listener) {
        try{
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
            if(CollectionUtils.isEmpty(parameterList)){
                listener.error("?????????parameter");
                return;
            }
            Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);

            if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientno"))
                    && StringUtils.isBlank(MapUtils.getString(paramMap,"visitno"))
                    && StringUtils.isBlank(MapUtils.getString(paramMap,"idNo"))
                    && StringUtils.isBlank(MapUtils.getString(paramMap,"name"))
                    && StringUtils.isBlank(MapUtils.getString(paramMap,"begindata"))
                    && StringUtils.isBlank(MapUtils.getString(paramMap,"enddata"))
                    && StringUtils.isBlank(MapUtils.getString(paramMap,"deptCode"))){
                listener.error("?????????????????????????????????");
                return;
            }
            //?????????????????????????????????????????????????????????????????????
            if(StringUtils.isNotBlank(MapUtils.getString(paramMap,"deptCode"))
                    ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"begindata"))
                    ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"enddata"))){
                if(StringUtils.isBlank(MapUtils.getString(paramMap,"deptCode"))
                        ||StringUtils.isBlank(MapUtils.getString(paramMap,"begindata"))
                        ||StringUtils.isBlank(MapUtils.getString(paramMap,"enddata"))){
                    listener.error("????????????????????????deptCode+begindata+enddata");
                    return;
                }
            }

            List<PvEncounterVo> pvEncounterVos = zsphOpMapper.getPvEncounter(paramMap);
            if(CollectionUtils.isNotEmpty(pvEncounterVos)){
                Set<String> pkPvList = new HashSet<>();
                for(PvEncounterVo encounterVo:pvEncounterVos){
                    pkPvList.add(encounterVo.getPkPv());
                }
                List<PvDiag> diagList = DataBaseHelper.queryForList("select * from PV_DIAG where PK_PV in ("+CommonUtils.convertSetToSqlInPart(pkPvList, "PK_PV")+") and del_flag='0' order by sort_no",PvDiag.class, new Object[]{});;
                List<Entry> entryList = new ArrayList<>();
                for(PvEncounterVo encounterVo:pvEncounterVos){
                    Entry entry = new Entry();
                    Response response = new Response();
                    entry.setResponse(response);
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    Encounter encounter = EncounterBuild.create(encounterVo);
                    encounter.setImplicitRules("getoutvisitinfo");
                    List<PvDiag> pvDiagList = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(diagList)){
                        for(PvDiag pv:diagList){
                            if(StringUtils.equals(encounterVo.getPkPv(),pv.getPkPv())){
                                pvDiagList.add(pv);
                            }
                        }
                    }
                    encounter.setDiagnosis(EncounterBuild.createDiag(pvDiagList));//??????
                    response.setOutcome(BeanMap.create(encounter));
                    entryList.add(entry);
                }
                listener.success(entryList);
            } else {
                listener.error("????????????");
            }
        } catch (BusException e) {
            listener.error(e.getMessage());
        } catch (Exception e){
            logger.error("?????????????????????????????????",e);
            listener.error(e.getMessage());
        }
    }

    /**
     *  ??????|????????????
     * 1. ?????????????????????????????? ???????????? > ??????????????????????????????????????????
     * 2.??????????????????(???????????????,??????????????????????????????<br>
     * 3.    SchApptPv ?????????????????????pkPv????????????******??????????????????****
     * @param param
     */
    @Override
    public void saveAppt(String param){
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("?????????entry");
        }
        List<LinkedTreeMap<String, String>> extensionList = new ArrayList<>();
        List<Map<String, Object>> entryList = (List<Map<String, Object>>) objEntry;
        //???????????????????????????
        List<ApptResource> apptList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                apptList.add(TransfTool.mapToBean(ApptResource.class, resourceMap));
                extensionList.addAll((List<LinkedTreeMap<String, String>>) resourceMap.get("extension"));
            }
        });
        if(CollectionUtils.isEmpty(apptList)){
            throw new BusException("?????????????????????");
        }
        ApptResource apptResource = apptList.get(0);
        Patient patient = apptResource.getSubject();
        if(CollectionUtils.isEmpty(apptResource.getAppointment())){
            throw new BusException("appointment????????????");
        }
        Appointment appointment = apptResource.getAppointment().get(0);
        if(patient == null || appointment ==null){
            throw new BusException("subject???appointment???????????????");
        }
        if(!Arrays.asList("booked","cancelled").contains(appointment.getStatus())){
            throw new BusException("?????????????????????booked|?????????cancelled|??????????????????");
        }
        //?????????????????? -- ??????His????????????
        String pkSchappt = null;
        if(CollectionUtils.isNotEmpty(appointment.getIdentifier())){
            Identifier identifier = appointment.getIdentifier().get(0);
            if(identifier!=null && "id/bookNo".equalsIgnoreCase(identifier.getSystem())){
                pkSchappt = identifier.getValue();
            }
        }
        if(StringUtils.isBlank(pkSchappt)) {
            throw new BusException("id/bookNo????????????");
        }

        String pkSch = null,orderidExt = null,dateApp=null;
        List<Extension> extensions = appointment.getExtension();
        if(CollectionUtils.isNotEmpty(extensions)){
            for (Extension extension : extensions) {
                if("SchId".equalsIgnoreCase(extension.getUrl())){
                    pkSch = extension.getValueString();
                } else if("AppointFrom".equalsIgnoreCase(extension.getUrl())
                        && extension.getValueCodeableConcept()!=null
                        && CollectionUtils.isNotEmpty(extension.getValueCodeableConcept().getCoding())) {
                    //????????????,,????????????????????????????????????????????????????????????????????????
                    Coding coding = extension.getValueCodeableConcept().getCoding().get(0);
                    if(coding!=null){
                        orderidExt =coding.getCode()+"|"+coding.getDisplay();
                    }
                } else if("AppointVisitDate".equalsIgnoreCase(extension.getUrl())){
                    dateApp = extension.getValueDate();
                }
            }
        }
        if(StringUtils.isBlank(pkSch)){
            throw new BusException("extension???SchId????????????");
        }
        if("booked".equalsIgnoreCase(appointment.getStatus())) {
            //??????????????????id
            Identifier patientId = patient.getIdentifier().stream().filter(identifier -> {
                return "code/patientId".equalsIgnoreCase(identifier.getSystem());
            }).findFirst().get();
            if(patientId==null || StringUtils.isBlank(patientId.getValue())){
                throw new BusException("code/patientId????????????");
            }
            if(StringUtils.isBlank(appointment.getStart())||StringUtils.isBlank(appointment.getEnd())){
                throw new BusException("start???end???????????????????????????????????????");
            }
            PiMasterRegVo piMasterRegVo = DataBaseHelper.queryForBean("select pk_pi,pk_org,pk_emp,name_pi,MOBILE,FLAG_REALMOBILE,birth_date,ID_NO,idno_rel from pi_master where del_flag = '0' and code_op = ?",
                    PiMasterRegVo.class, new Object[]{patientId.getValue()});
            if(piMasterRegVo == null){
                throw new BusException("??????????????????????????????????????????");
            }
            if (StringUtils.isBlank(piMasterRegVo.getIdNo())&&StringUtils.isBlank(piMasterRegVo.getIdnoRel())){
                throw new BusException("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            }
            /*
            if(!"1".equals(piMasterRegVo.getFlagRealmobile())){
                throw new BusException("???????????????????????????"+piMasterRegVo.getMobile()+"????????????????????????????????????????????????????????????????????????????????????????????????!!");
            }*/
            Map<String, Object> dateWorkMap = DataBaseHelper.queryForMap("select to_char(sch.DATE_WORK,'yyyy-MM-dd') DATE_WORK,dept.code_dept,dept.dt_medicaltype from SCH_SCH sch left join bd_ou_dept dept on sch.pk_dept = dept.pk_dept  where sch.PK_SCH=?", new Object[]{pkSch});
            if(MapUtils.isEmpty(dateWorkMap)){
                throw new BusException("????????????????????????");
            }
            //????????????
            Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",UserContext.getUser().getPkOrg());
            //??????????????????
            Map<String, Object> codePv0019Map = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",UserContext.getUser().getPkOrg());
            //????????????????????????
            Map<String, Object> codePv0046Map = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0046' and pk_org=?",UserContext.getUser().getPkOrg());
            int age = 0;
            try {
                age = DateUtils.getAge(piMasterRegVo.getBirthDate());
            } catch (Exception e) {
                throw new BusException("??????????????????????????????????????????????????????");
            }
            if(null != codeTypeMap){
                String card[] = codeTypeMap.get("val").toString().split(",");
                if(codePv0019Map!=null&&StringUtils.isNotBlank(MapUtils.getString(codePv0019Map,"val"))){
                    for (int i = 0; i < card.length; i++) {
                        if((card[i]).equals(MapUtils.getString(dateWorkMap,"codeDept"))){
                            if(age>MapUtils.getInteger(codePv0019Map,"val")){
                                throw new BusException("??????????????????"+MapUtils.getInteger(codePv0019Map,"val")+"?????????????????????");
                            }
                        }
                    }
                }
            }
            if(codePv0046Map!=null&&StringUtils.isNotBlank(MapUtils.getString(codePv0046Map,"val"))){
                if("01".equals(MapUtils.getString(dateWorkMap,"dtMedicaltype"))&&
                        age<MapUtils.getInteger(codePv0046Map,"val")){
                    throw new BusException("??????????????????"+MapUtils.getInteger(codePv0046Map,"val")+"?????????????????????");
                }

            }


            if(StringUtils.isNotBlank(dateApp) &&
                    !StringUtils.equals(dateApp,MapUtils.getString(dateWorkMap,"dateWork"))){
                throw new BusException("?????????AppointVisitDate?????????????????????????????????????????????");
            }

            piMasterRegVo.setPkSch(pkSch);
            piMasterRegVo.setPkSchappt(pkSchappt);
            //??????????????????????????????
            piMasterRegVo.setDtApptype("2");
            piMasterRegVo.setNote(orderidExt);
            /**
             * ??? ??????--??????--??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
             * ??? ?????????????????????????????????1??????????????????
             */
            SchTicket ticket =null;
            try{
                //????????????~~get??????update?????????????????????PV0044??????????????????????????????????????????
                Map<String,Object> ticketParam = new HashMap<>();
                ticketParam.put("pkSch",piMasterRegVo.getPkSch());
                ticketParam.put("startTime",appointment.getStart());
                ticketParam.put("endTime",appointment.getEnd());
                //?????????????????????
                ticketParam.put("isExternal",true);
                ticket = ticketPubService.getUnusedAppTicket(ticketParam);
                piMasterRegVo.setTicketNo(ticket.getTicketno());
                piMasterRegVo.setStartTime(ticket.getBeginTime());
                piMasterRegVo.setEndTime(ticket.getEndTime());
                regCommonService.saveAppt(piMasterRegVo);
            } catch (Exception e){
                //?????????????????????
                ticketPubService.setTicketUnused(ticket);
                if(e instanceof DuplicateKeyException){
                    throw new BusException("id/bookNo???????????????????????????");
                } else{
                    throw e;
                }
            }

        } else if("cancelled".equalsIgnoreCase(appointment.getStatus())) {
            SchAppt appt = new SchAppt();
            if (extensionList.size() > 0) {
                for (LinkedTreeMap<String, String> extension : extensionList) {
                    if (extension != null && "source_system".equals(extension.get("url"))) {
                        appt.setNameEmpCancel(extension.get("valueString"));
                    }
                }
            }
            appt.setPkSchappt(pkSchappt);
            appt.setPkSch(pkSch);
            appt.setNote(appointment.getCancelationReason() != null ? appointment.getCancelationReason().getText() : null);
            regCommonService.cancelAppt(appt);
        }
        //???????????????
        Map<String,Object> msgPiParam = new HashMap<String,Object>();
        msgPiParam.put("pkSchappt",pkSchappt);
        for(ApptResource app:apptList){
            if(!"MZYYXXXZ".equals(app.getImplicitRules())){
                msgPiParam.put("isAdd","0");
            }
        }
        PlatFormSendUtils.sendSchApptReg(msgPiParam);
    }

    @Override
    public List<Entry> getApptInfo(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("?????????parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"idno"))//????????????
                && StringUtils.isBlank(MapUtils.getString(paramMap,"phone"))//????????????
                && StringUtils.isBlank(MapUtils.getString(paramMap,"name"))//??????
                && StringUtils.isBlank(MapUtils.getString(paramMap,"begindata"))//??????
                && StringUtils.isBlank(MapUtils.getString(paramMap,"enddata"))//??????
                && StringUtils.isBlank(MapUtils.getString(paramMap,"deptCode"))//??????
                && StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))) {//????????????
            throw new BusException("??????????????????????????????idno|phone|name|patientId");
        }
        //?????????????????????????????????????????????????????????????????????
        if(StringUtils.isNotBlank(MapUtils.getString(paramMap,"deptCode"))
                ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"begindata"))
                ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"enddata"))){
            if(StringUtils.isBlank(MapUtils.getString(paramMap,"deptCode"))
                    ||StringUtils.isBlank(MapUtils.getString(paramMap,"begindata"))
                    ||StringUtils.isBlank(MapUtils.getString(paramMap,"enddata"))){
                throw new BusException("????????????????????????deptCode+begindata+enddata");
            }
        }
        String status = MapUtils.getString(paramMap,"status");//??????
        if(StringUtils.isNotBlank(status)){
            if(!Arrays.asList("booked","cancelled").contains(status)){
                throw new BusException("?????????????????????booked|?????????cancelled|??????????????????");
            } else{
                paramMap.put("apptStatus","booked".equals(status)?"0":"1");
            }
        }
        //?????????????????????,???????????????????????????2?????????????????????????????????
        paramMap.put("limitDate",DateUtils.addDate(new Date(),-2,2,"yyyyMMddHHmmss"));
        List<SchApptVo> apptList = opSendMapper.getSchAppt(paramMap);
        if(CollectionUtils.isEmpty(apptList)){
            throw new BusException("??????????????????????????????????????????");
        }
        List<Entry> entryList = new ArrayList<>();
        for (SchApptVo schApptVo : apptList) {
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);

            ApptResource apptResource = new ApptResource();
            apptResource.setResourceType("Encounter");
            apptResource.setId(NHISUUID.getKeyId());
            //????????????
            Patient subject = new Patient();
            subject.setResourceType("Patient");
            subject.setIdentifier(Lists.newArrayList(new Identifier("code/patientId",schApptVo.getCodeOp())
                    ,new Identifier("code/idNo",schApptVo.getIdNo())));
            subject.setName(Arrays.asList(new TextElement(schApptVo.getNamePi())));
            subject.setTelecom(Arrays.asList(new Identifier("phone",schApptVo.getMobile())));
            subject.setBirthDate(schApptVo.getBirthDate());
            subject.setGender("02".equals(schApptVo.getDtSex())?"male":"03".equals(schApptVo.getDtSex())?"female":"other");//????????????
            apptResource.setSubject(subject);
            //????????????
            Appointment appointment = new Appointment();
            appointment.setResourceType("Appointment");
            appointment.setIdentifier(Arrays.asList(new Identifier("id/bookNo",schApptVo.getPkSchappt())));
            appointment.setStatus("1".equals(schApptVo.getFlagCancel())?"cancelled":"booked");
            appointment.setCancelationReason(new TextElement("~"));
            appointment.setDescription(schApptVo.getNameDateslot());
            appointment.setStart(DateUtils.formatDate(schApptVo.getBeginTime(),"HH:mm:ss"));
            appointment.setEnd(DateUtils.formatDate(schApptVo.getEndTime(),"HH:mm:ss"));
            appointment.setCreated(schApptVo.getCreateTime());
            appointment.setAppointmentType(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding(schApptVo.getResCode(),schApptVo.getResName())))));
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
            remark.setValueString(schApptVo.getNoteAppt()==null?"":schApptVo.getNoteAppt());
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
            response.setOutcome(BeanMap.create(apptResource));
            entryList.add(entry);;
        }

        return entryList;
    }

    @Override
    public void triageSign(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("?????????entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //?????????????????????
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                TriageVo vo = TransfTool.mapToBean(TriageVo.class, resourceMap);
                if(vo.getQueNo() == null){
                    throw new BusException("????????????????????????queNo");
                }
                String codePv=vo.getRegisterId(),codeDept = vo.getDeptCode(),codeDoctor=vo.getDoctorCode();
                PvEncounter pvEncounter = DataBaseHelper.queryForBean("select PK_PV,pi.CODE_PI,EU_STATUS,pi.CODE_OP from PV_ENCOUNTER pv " +
                        " inner join PI_MASTER pi on pv.PK_PI=pi.PK_PI where pv.CODE_PV=? and pv.EU_PVTYPE in('1','2','4')",PvEncounter.class, new Object[]{codePv});
                if(pvEncounter == null) {
                    throw new BusException("??????????????????????????????????????????registerId:"+codePv);
                }
                if(!StringUtils.equals(vo.getPatientId(), pvEncounter.getCodeOp())) {
                    throw new BusException("???????????????ID????????????????????????patientId:"+vo.getPatientId());
                }
                Map<String, Object> dept = DataBaseHelper.queryForMap("select PK_DEPT from BD_OU_DEPT where CODE_DEPT=? and DEL_FLAG='0'", new Object[]{codeDept});
                if(MapUtils.isEmpty(dept)) {
                    throw new BusException("?????????????????????????????????????????????"+codeDept);
                }
                Map<String, Object> doctor = DataBaseHelper.queryForMap("select PK_EMP,NAME_EMP from BD_OU_EMPLOYEE where CODE_EMP=? and DEL_FLAG='0'", new Object[]{codeDoctor});
                if(MapUtils.isEmpty(doctor)) {
                    throw new BusException("?????????????????????????????????????????????"+codeDoctor);
                }
                PvQue pvQue = DataBaseHelper.queryForBean("select PK_PVQUE from PV_QUE where PK_PV=?",PvQue.class,new Object[]{pvEncounter.getPkPv()});
                if(pvQue == null) {
                    pvQue = new PvQue();
                    pvQue.setSortno(0);//?????????????????????
                } else {
                    if(!EnumerateParameter.ZERO.equals(pvEncounter.getEuStatus())){
                        throw new BusException("????????????????????????????????????????????????"+codePv);
                    }
                }

                pvQue.setSortno(Integer.valueOf(vo.getQueNo()));
                pvQue.setSortnoAdj(Integer.valueOf(vo.getQueNo()));
                pvQue.setEuStatus(EnumerateParameter.ONE);
                pvQue.setEuType(EnumerateParameter.ZERO);
                pvQue.setPkPv(pvEncounter.getPkPv());
                pvQue.setPkEmpPhy(MapUtils.getString(doctor,"pkEmp"));
                pvQue.setNameEmpPhy(MapUtils.getString(doctor,"nameEmp"));
                pvQue.setPkDept(MapUtils.getString(dept,"pkDept"));
                try {
                    pvQue.setDateSign(DateUtils.parseDate(vo.getOperationTime()));
                } catch (ParseException e) {
                    throw new BusException("operationTime?????????????????????");
                }
                if(pvQue.getPkPvque() == null){
                    DataBaseHelper.insertBean(pvQue);
                } else {
                    DataBaseHelper.updateBeanByPk(pvQue);
                }

            }
        });


    }

    @Override
    public void modApplyStatus(String param) {
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("?????????entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //?????????????????????
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                String type = MapUtils.getString(resourceMap,"implicitRules");
                List<Map<String,Object>> list = (List<Map<String, Object>>) MapUtils.getObject(resourceMap,"identifier");
                if(CollectionUtils.isEmpty(list)){
                    throw new BusException("?????????identifier");
                }
                //??????????????????
                Optional<Map<String, Object>> apply = list.stream().filter(idf -> {
                    return StringUtils.isNotBlank(MapUtils.getString(idf, "system"))
                            && StringUtils.equals(MapUtils.getString(idf, "system"), "id/applyno")
                            && StringUtils.isNotBlank(MapUtils.getString(idf, "value"));
                }).findFirst();
                if(!apply.isPresent()){
                    throw new BusException("identifier????????????id/applyno");
                }

                String applyCode = MapUtils.getString(apply.get(), "value");
                String status = MapUtils.getString(resourceMap, "status");//1.?????? 2.?????? 3.?????? 4.?????? 5.?????? 6.??????  7.??????  8.??????
                boolean jy=StringUtils.equalsIgnoreCase(type,"JYSQDZTXG"),//??????
                        jc=StringUtils.equalsIgnoreCase(type,"JCSQDZTXG"),//??????
                        sx=StringUtils.equalsIgnoreCase(type,"SXSQDZTXG"),//??????
                        ss=StringUtils.equalsIgnoreCase(type,"SSSQDZTXG");//??????
                String occurrenceDateTime = MapUtils.getString(resourceMap, "occurrenceDateTime");
                String pkCnord = null;
                if(jc){
                    //??????????????????
                    List<String> pkCnordList = list.stream().filter(idf ->
                            StringUtils.isNotBlank(MapUtils.getString(idf, "system"))
                                    && StringUtils.equals(MapUtils.getString(idf, "system"), "id/applyDetailNo")
                                    && StringUtils.isNotBlank(MapUtils.getString(idf, "value"))
                    ).map(ord -> MapUtils.getString(ord, "value")).collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(pkCnordList)){
                        throw new BusException("identifier????????????id/applyDetailNo");
                    }
                    pkCnord = pkCnordList.get(0);
                    if(DataBaseHelper.queryForScalar("select count(1) from cn_order where pk_cnord=? and CODE_APPLY=?"
                            ,Integer.class,new Object[]{pkCnord,applyCode}) ==0){
                        throw new BusException("??????????????????????????????????????????????????????");
                    }
                }
                if(StringUtils.isBlank(status)){
                    throw new BusException("status??????");
                }
                //??????????????????
                List<Map<String,Object>> perfromObj = (List<Map<String, Object>>) MapUtils.getObject(resourceMap,"performer");
                if(CollectionUtils.isEmpty(perfromObj)){
                    throw new BusException("?????????performer");
                }
                List<LocationOp> performerList = JsonUtil.readValue(ZsphMsgUtils.getJsonStr(perfromObj), new TypeReference<List<LocationOp>>() {});
                List<LocationOp> doctors = performerList.stream().filter(lc -> "Practitioner".equalsIgnoreCase(lc.getResourceType())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(doctors)) {
                    throw new BusException("????????????????????????");
                }
                LocationOp doctor = doctors.get(0);
                if(doctor.getIdentifier()==null || StringUtils.isBlank(doctor.getIdentifier().getValue())) {
                    throw new BusException("????????????????????????");
                }
                List<LocationOp> depts = performerList.stream().filter(lc -> "Location".equalsIgnoreCase(lc.getResourceType())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(depts)) {
                    throw new BusException("???????????????????????????");
                }
                LocationOp dept = depts.get(0);
                if(dept.getIdentifier()==null || StringUtils.isBlank(dept.getIdentifier().getValue())) {
                    throw new BusException("???????????????????????????");
                }
                String codeDoctor = doctor.getIdentifier().getValue();
                String codeDept = dept.getIdentifier().getValue();
                BdOuEmployee employee = DataBaseHelper.queryForBean("select NAME_EMP,PK_EMP,PK_ORG from BD_OU_EMPLOYEE where CODE_EMP =? and DEL_FLAG='0'",
                        BdOuEmployee.class, new Object[]{codeDoctor});
                if(employee == null){
                    throw new BusException("????????????????????????????????????????????????");
                }
                BdOuDept bdOuDept = DataBaseHelper.queryForBean("select name_dept,pk_dept from BD_OU_DEPT where code_dept =? and DEL_FLAG='0'",
                        BdOuDept.class, new Object[]{codeDept});
                if(bdOuDept == null){
                    throw new BusException("????????????????????????????????????????????????");
                }
                //???????????????????????????~~his?????????????????????????????????????????????
//                List<Map<String,Object>> listCagtegory = (List<Map<String, Object>>) MapUtils.getObject(resourceMap,"category");
//                if(CollectionUtils.isEmpty(listCagtegory)){
//                    throw new BusException("?????????category");
//                }
//                Category category = TransfTool.mapToBean(Category.class, listCagtegory.get(0));
//                if(CollectionUtils.isEmpty(category.getCoding())){
//                    throw new BusException("?????????category>coding");
//                }
//                String nameCate = category.getCoding().get(0).getDisplay();
                String qrySql = null,modSql=null;
                String euStatus = null;
                if(jc){//??????
                    if("2".equals(status)){
                        euStatus = "2";
                    } else if(status.compareTo("2")>0 && status.compareTo("6")<=0){
                        euStatus = "3";
                    }else if("7".equals(status)){
                        euStatus = "4";
                    }else if("8".equals(status)){//??????
                        euStatus = "1";
                    }
                    modSql = "update CN_RIS_APPLY set modifier=?,ts=?,eu_status=? where PK_CNORD=? ";
                } else if(jy){//??????
                    if("2".equals(status)){
                        euStatus = "2";
                    } else if(status.compareTo("2")>0 && status.compareTo("7")<=0){
                        euStatus = "3";
                    }else if("8".equals(status)){
                        euStatus = "4";
                    }else if("1".equals(status)){//??????
                        euStatus = "1";
                    }
                    qrySql = "select distinct lab.PK_ORDLIS pk,ord.PK_CNORD FROM CN_LAB_APPLY lab inner join CN_ORDER ord on lab.PK_CNORD=ord.PK_CNORD where CODE_APPLY=?";
                    modSql = "update CN_LAB_APPLY set modifier=?,ts=?,eu_status=? where PK_ORDLIS in";
                } else if(sx){//??????
                    if(status.compareTo("2")>=0 && status.compareTo("6")<=0){
                        euStatus = "4";
                    }else if("7".equals(status)){
                        euStatus = "5";
                    }
                    qrySql="select distinct tran.PK_ORDBT pk,ord.PK_CNORD FROM CN_TRANS_APPLY tran inner join CN_ORDER ord on tran.PK_CNORD=ord.PK_CNORD where CODE_APPLY=?";
                    modSql = "update CN_TRANS_APPLY set modifier=?,ts=?,eu_status=? where PK_ORDBT in";
                } else if(ss){//??????
                    if(status.compareTo("2")>=0 && status.compareTo("6")<=0){
                        euStatus = "3";
                    }else if("7".equals(status)){
                        euStatus = "5";
                    }
                    qrySql="select op.PK_ORDOP pk,ord.PK_CNORD FROM CN_OP_APPLY op inner join CN_ORDER ord on op.PK_CNORD=ord.PK_CNORD where CODE_APPLY=?";
                    modSql = "update CN_OP_APPLY set modifier=?,ts=?,eu_status=? where PK_ORDOP in";
                } else {
                    throw new BusException("implicitRules????????????,?????????");
                }

                String pkCnordStr = null;
                List<Map<String,Object>> qryData = null;
                if(jc) {
                    pkCnordStr = "'" + pkCnord+ "'";
                } else {
                    qryData = DataBaseHelper.queryForList(qrySql,new Object[]{applyCode});
                    if(CollectionUtils.isEmpty(qryData)){
                        throw new BusException("????????????????????????????????????????????????");
                    }
                    pkCnordStr = CommonUtils.convertListToSqlInPart(qryData.stream().map(map -> MapUtils.getString(map,"pkCnord")).collect(Collectors.toList()));
                }

                //?????????????????????
                if(euStatus !=null){
                    if(jc){
                        DataBaseHelper.update(modSql,new Object[]{codeDoctor,new Date(),euStatus,pkCnord});
                    } else {
                        String pkStr = CommonUtils.convertListToSqlInPart(qryData.stream().map(map -> MapUtils.getString(map,"pk")).collect(Collectors.toList()));
                        DataBaseHelper.update(modSql + "("+pkStr+") ",new Object[]{codeDoctor,new Date(),euStatus});
                    }
                } else {
                    logger.info("????????????????????????????????????????????????"+status);
                }

                List<Map<String ,Object>> listDept = QueryUtils.queryOccDeptByCodeEmp(codeDoctor);
                String pkDeptArea = null;
                String pkDeptJob = null;
                if(listDept !=null&&listDept.size()>0){
                    pkDeptArea = MapUtils.getString(listDept.get(0),"pkDeptArea");
                    pkDeptJob = MapUtils.getString(listDept.get(0),"pkDeptJob");
                }

                        //?????????????????????????????????????????????????????????????????????????????????
                if(StringUtils.equals(status,"8") && !jy){
                    //????????????
                    if(ss){
                        DataBaseHelper.execute("update cn_op_apply set eu_status='1' where eu_status='5' and pk_cnord in ("+pkCnordStr+")");
                        //????????????????????????????????????
                        DataBaseHelper.execute("update ex_order_occ set eu_status='0',flag_canc='1',pk_dept_canc=?,"
                                        + "date_canc=?,pk_emp_canc=?,name_emp_canc=?,modifier=?,ts=? "
                                        + "where pk_cnord in ("+pkCnordStr+") and eu_status='1' ",
                                new Object[]{ bdOuDept.getPkDept(), new Date(),employee.getPkEmp(), employee.getNameEmp(), employee.getPkEmp(), new Date()});

                    } else {
                        DataBaseHelper.update("update ex_assist_occ SET flag_occ='0',date_occ=null,pk_emp_occ=null,name_emp_occ=null,pk_emp_conf=null,name_emp_conf=null,pk_msp=null,eu_status='0',date_canc=?,pk_emp_canc ='"+employee.getPkEmp()+"',name_emp_canc='"+employee.getNameEmp()+"',pk_dept_job =null " +
                                " where flag_occ='1' and flag_canc='0' and PK_CNORD in ("+pkCnordStr+")",new Object[]{new Date()});
                    }
                } else if(status.compareTo("4")>=0 && status.compareTo("7")<=0 && !jy){
                    //TODO ?????????????????????????????????????????????????????????????????????????????????????????????
/*                    Integer count = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in("+pkCnordStr+") and flag_settle='0'", Integer.class);
                    if(count>0){
                        throw new BusException("?????????????????????????????????");
                    }*/
                    Date current =  new Date();
                    DataBaseHelper.update("update ex_assist_occ SET TS=?,FLAG_OCC=1,DATE_OCC =?,PK_EMP_OCC=?,NAME_EMP_OCC=?,EU_STATUS=1,PK_ORG_OCC=?,PK_DEPT_OCC=?,pk_emp_conf=?,name_emp_conf=?,pk_dept_job=?,pk_dept_area=? " +
                                    " WHERE flag_occ = 0 and flag_canc = '0' and flag_refund = '0' and PK_CNORD in ("+pkCnordStr+")"
                            ,new Object[]{current,current,employee.getPkEmp(),employee.getNameEmp(),employee.getPkOrg(),bdOuDept.getPkDept(),employee.getPkEmp(),employee.getNameEmp(),pkDeptJob,pkDeptArea});
                    DataBaseHelper.update(" update ex_order_occ set eu_status='1', date_occ = ?,pk_dept_occ = ?, pk_org_occ = ?," +
                            " pk_emp_occ=?, name_emp_occ=? WHERE PK_CNORD in ("+pkCnordStr+")"
                            ,new Object[]{current,bdOuDept.getPkDept(),employee.getPkOrg(),employee.getPkEmp(),employee.getNameEmp()}
                    );

                    if(ss){
                        DataBaseHelper.update("update cn_op_apply set eu_status='5',FLAG_FINISH_ANAE='1'  where PK_CNORD in ("+pkCnordStr+")");
                    }
                }else if(jy){//1.?????? 2.?????? 3.?????? 4.?????? 5.?????? 6.?????? 7.?????? 8.??????  9.??????
                    if(StringUtils.equals(status,"3")){//????????????3???????????????????????????1??????????????????

                        Date current =  new Date();
                        //??????????????????????????????
                        DataBaseHelper.update(" update ex_order_occ set eu_status='1', date_occ = ?,pk_dept_occ = ?, pk_org_occ = ?," +
                                        " pk_emp_occ=?, name_emp_occ=? WHERE PK_CNORD in ("+pkCnordStr+")"
                                ,new Object[]{current,bdOuDept.getPkDept(),employee.getPkOrg(),employee.getPkEmp(),employee.getNameEmp()});
                        //??????????????????????????????
                        DataBaseHelper.update("update ex_assist_occ SET pk_dept_area=?,pk_dept_job=?,TS=?,FLAG_OCC=1,DATE_OCC =?,PK_EMP_OCC=?,NAME_EMP_OCC=?,EU_STATUS=1,PK_ORG_OCC=?,PK_DEPT_OCC=?,pk_emp_conf=?,name_emp_conf=? " +
                                        " WHERE flag_occ = 0 and flag_canc = '0' and flag_refund = '0' and PK_CNORD in ("+pkCnordStr+")"
                                ,new Object[]{pkDeptArea,pkDeptJob,current,current,employee.getPkEmp(),employee.getNameEmp(),employee.getPkOrg(),bdOuDept.getPkDept(),employee.getPkEmp(),employee.getNameEmp()});
                        //??????????????????0113???????????????1???????????????????????????????????????????????????????????????
                        DataBaseHelper.execute("update bl_op_dt cg set cg.pk_dept_ex=?,cg.pk_emp_ex=?,cg.name_emp_ex=? where pk_cnord in ("+pkCnordStr+") and " +
                                " flag_settle='1' And  Exists(Select 1 From bd_dictattr attr where attr.code_attr='0113' and " +
                                " cg.pk_item=attr.pk_dict)",new Object[]{bdOuDept.getPkDept(),employee.getPkEmp(),employee.getNameEmp()});
                    }else if(StringUtils.equals(status,"1")){
                        //?????????????????????????????????
                        DataBaseHelper.execute("update ex_order_occ set eu_status='0',flag_canc='1',pk_dept_canc=?,"
                                        + "date_canc=?,pk_emp_canc=?,name_emp_canc=?,modifier=?,ts=? "
                                        + "where pk_cnord in ("+pkCnordStr+") and eu_status='1' ",
                                new Object[]{ bdOuDept.getPkDept(), new Date(),employee.getPkEmp(), employee.getNameEmp(), employee.getPkEmp(), new Date()});
                        //?????????????????????????????????
                        DataBaseHelper.update("update ex_assist_occ SET flag_occ='0',date_occ=null,pk_emp_occ=null,name_emp_occ=null,pk_msp=null,pk_emp_conf =null,name_emp_conf=null,pk_dept_job=null,pk_dept_area=null,eu_status='0',date_canc=?,pk_emp_canc =?,name_emp_canc=? " +
                                " where flag_occ='1' and flag_canc='0' and PK_CNORD in ("+pkCnordStr+")",new Object[]{new Date(),employee.getPkEmp(),employee.getNameEmp()});
                    }
                }

            }
        });
    }

    /**
     * ????????????
     * @param param
     */
    @Override
    public List<Entry> saveOpcg(String param) {
        Map<String, Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if (objEntry == null) {
            throw new BusException("?????????entry");
        }
        List<Map<String, Object>> entryList = (List<Map<String, Object>>) objEntry;
        //?????????????????????
        List<OpcgData> opCgList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                opCgList.add(TransfTool.mapToBean(OpcgData.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(opCgList)){
            throw new BusException("???????????????????????????");
        }
        //????????????????????????
        OpcgData opcgData = opCgList.get(0);
        PvEncounter pv = null;
        if(opcgData != null){
            //1.????????????
            String sql = "select * from pi_master pi where code_op=?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, new Object[]{opcgData.getPatientid()});
            if (piMaster == null) {
                throw new BusException("??????patientid???"+opcgData.getPatientid()+"??????????????????????????????");
            }
            String hpSql = "select * from BD_HP where del_flag = '0' and eu_hptype ='0' ";
            BdHp bdHp = DataBaseHelper.queryForBean(hpSql,BdHp.class);

            //3.User????????????
            User user = new User();
            Map<String ,Object> emp =QueryUtils.queryPkEmpByCode(opcgData.getCodeDoc());
            if(emp==null){
                throw new BusException("????????????code????????????????????????");
            }
            BdOuDept dept=QueryUtils.getDeptInfo(opcgData.getCodeDept());
            if(dept==null){
                throw new BusException("????????????code????????????????????????");
            }
            user.setPkEmp(MapUtils.getString(emp, "pkEmp"));//????????????????????????
            user.setCodeEmp(MapUtils.getString(emp, "codeEmp"));
            user.setNameEmp(MapUtils.getString(emp, "nameEmp"));
            user.setPkOrg(MapUtils.getString(emp, "pkOrg"));//??????
            //4.??????????????????
            String pkPv = NHISUUID.getKeyId();
            pv = savePvEncounter(piMaster, pkPv,bdHp,user,dept,opcgData.getFlagSpec());
            //5.?????????????????????????????????pv_pe
            savePvPe(piMaster,pv,user);
            //6.??????????????????
            //?????????icd??????????????????????????????????????????
            if(StringUtils.isNotBlank(opcgData.getCodeICD())){
                savePvDiag(user,pkPv,opcgData.getCodeICD(),opcgData.getNameICD());
            }
            List<CgData> cgDataList = opcgData.getCgDataList();
            if(cgDataList!=null&&cgDataList.size()>0){
                //??????????????????
                String codeItem = "";
                int i = 0;
                for (CgData opParam : cgDataList) {
                    if (i == 0) {
                        codeItem += "'" + opParam.getCodeItem() + "'";
                    } else {
                        codeItem += ",'" + opParam.getCodeItem() + "'";
                    }
                    i++;
                }
                String sqlItem = "select i.code as key_ ,i.* from (select  bi.pk_Item,bi.CODE as code,bi.name as name,bi.spec,bi.price,bu.name as unitname ,bi.PK_ITEMCATE,'0' as flag_Pd,null as batch_No,bi.PK_UNIT as pk_Unit_Pd,1 as pack_Size,null as price_Cost " +
                        "                 from BD_ITEM bi left join BD_UNIT bu on bu.pk_unit=bi.pk_unit where bi.del_flag='0' and bi.FLAG_ACTIVE='1' " +
                        "                 union" +
                        "                 select  bp.pk_pd as pk_item,bp.code as code,bp.name as name, bp.spec as spec,bp.price/bp.PACK_SIZE as price,bu.name as unitname ,null as PK_ITEMCATE,'1' as flag_Pd,'~' as batch_No,bp.PK_UNIT_MIN as pk_Unit_Pd,1 as pack_Size,bp.price as price_Cost " +
                        "                 from bd_pd bp left join BD_UNIT bu on bu.pk_unit=bp.pk_unit_min where bp.flag_gmp='1' and bp.FLAG_STOP ='0' and bp.del_flag='0' ) i\n" +
                        " where i.CODE in("+codeItem+")";
                Map<String, Map<String, Object>> itemMaps = DataBaseHelper.queryListToMap(sqlItem);
                // ??????????????????????????????
                List<BlPubParamVo> params = new ArrayList<>();
                for (CgData opParam : cgDataList) {
                    Map<String, Object> item = itemMaps.get(opParam.getCodeItem());
                    // ??????????????????????????????
                    BlPubParamVo dtparam = new BlPubParamVo();
                    dtparam.setPkDeptAreaapp(opParam.getCodeDeptArea());
                    dtparam.setPkDeptJob(opParam.getCodeDept());
                    dtparam.setBatchNo(MapUtils.getString(item, "batchNo"));
                    dtparam.setDateExpire(null);
                    dtparam.setDateHap(new Date());
                    dtparam.setEuAdditem("1");
                    dtparam.setEuPvType("4");// ????????????????????????
                    dtparam.setFlagPd(MapUtils.getString(item, "flagPd"));
                    dtparam.setFlagPv("0");// ????????????
                    Map<String, Object> empApp = QueryUtils.queryPkEmpByCode(opParam.getCodeEmp());
                    dtparam.setNameEmpApp(MapUtils.getString(empApp, "nameEmp"));
                    dtparam.setNameEmpCg(MapUtils.getString(empApp, "nameEmp"));
                    dtparam.setPkOrgApp(user.getPkOrg());
                    dtparam.setPkEmpApp(MapUtils.getString(empApp,"pkEmp"));
                    dtparam.setPkEmpCg(MapUtils.getString(empApp,"pkEmp"));

                    dtparam.setNamePd(MapUtils.getString(item,"name"));
                    dtparam.setPackSize(MapUtils.getInteger(item,"packSize"));
                    dtparam.setPkCnord(null);
                    BdOuDept deptApp =QueryUtils.getDeptInfo(opParam.getCodeDept());
                    BdOuDept deptEx =QueryUtils.getDeptInfo(opParam.getCodeDeptEx());

                    dtparam.setPkDeptApp(deptApp.getPkDept());
                    dtparam.setPkDeptCg(deptEx.getPkDept());
                    dtparam.setPkDeptEx(deptEx.getPkDept());
                    if (BlcgUtil.converToTrueOrFalse(MapUtils.getString(item,"flagPd"))) // ??????
                        dtparam.setPkOrd(MapUtils.getString(item,"pkItem"));
                    if(StringUtils.isBlank(MapUtils.getString(item,"pkItem"))){
                        throw new BusException("????????????"+opParam.getCodeItem()+"??????????????????????????????????????????");
                    }
                    dtparam.setPkItem(MapUtils.getString(item,"pkItem"));
                    dtparam.setPkOrg(user.getPkOrg());
                    dtparam.setPkOrgApp(user.getPkOrg());
                    dtparam.setPkOrgEx(user.getPkOrg());
                    dtparam.setPkPi(pv.getPkPi());
                    dtparam.setPkPres(null);
                    dtparam.setPkPv(pv.getPkPv());
                    dtparam.setPkUnitPd(MapUtils.getString(item,"pkUnitPd"));
                    dtparam.setPrice(MapUtils.getDouble(item,"price"));
                    dtparam.setPriceCost(MapUtils.getDouble(item,"priceCost"));
                    dtparam.setQuanCg(opParam.getQuanCg());
                    dtparam.setSpec(MapUtils.getString(item,"spec"));
                    dtparam.setFlagHasPdPrice("1");
                    dtparam.setRatioSelf(StringUtils.isNotBlank(opParam.getRatioPock())? Double.parseDouble(opParam.getRatioPock()):null);
                    dtparam.setRatioDisc(StringUtils.isNotBlank(opParam.getRatioDisc())? Double.parseDouble(opParam.getRatioDisc()):1.0);
                    params.add(dtparam);
                }
                // ????????????????????????
                if (params.size() > 0) {
                    BlPubReturnVo rtnvo = opCgPubService.blOpCgBatch(params);
                }
            }
        }
        Entry entry = new Entry(new Response());
        if(pv != null){
            Response response = entry.getResponse();
            Outcome outcome = new Outcome();
            outcome.setResourceType("OperationOutcome");
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            Issue issue = new Issue();
            issue.setCode("informational");
            issue.setDiagnostics("??????");
            issue.setSeverity("infomation");
            issue.setCodePv(pv.getCodePv());
            outcome.setIssue(Arrays.asList(issue));
            response.setOutcome(BeanMap.create(outcome));
        }
        return Arrays.asList(entry);
    }

    /**
     * ??????????????????
     * @param param
     */
    @Override
    public void deleteOpcgpushStatus(String param){
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("?????????entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //??????????????????????????????
        List<Map<String,Object>> mapList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                mapList.add(TransfTool.mapToBean(Map.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(mapList)){
            throw new BusException("????????????????????????");
        }
        //?????????????????????????????????????????????????????????????????????
        for(Map<String,Object> map: mapList){
            if(MapUtils.getString(map,"codePv") == null){
                throw new BusException("?????????codePv??????");
            }
            //??????????????????
            String sql = " select pv.pk_pv from pv_encounter pv  where pv.CODE_PV = ?";
            List<Map<String,Object>> orderMap = DataBaseHelper.queryForList(sql,MapUtils.getString(map,"codePv"));
            //???????????????????????????
            if(orderMap==null ||orderMap.size()==0){
                throw new BusException("??????????????????????????????,codePv??????"+MapUtils.getString(map,"codePv"));
            }
            String sqlSettle = "select count(1)  from BL_OP_DT where flag_settle='1' and pk_pv =?";
            Integer count = DataBaseHelper.queryForScalar(sqlSettle, Integer.class,MapUtils.getString(orderMap.get(0),"pkPv"));
            if(count>0){
                throw new BusException("?????????????????????????????????????????????????????????????????????,codePv??????"+MapUtils.getString(map,"codePv"));
            }
            //?????????????????????????????????????????????
             DataBaseHelper.update("update PV_ENCOUNTER set DEL_FLAG = '1' where code_pv =? and  EU_PVTYPE = '4' ",new Object[] {MapUtils.getString(map,"codePv")});
             DataBaseHelper.update("update PV_PE set DEL_FLAG = '1' where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
             //??????????????????
             DataBaseHelper.execute("delete BL_OP_DT where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
            // TODO: 2021-02-24 ????????????
            DataBaseHelper.execute("delete pv_diag where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
            DataBaseHelper.execute("delete cn_diag where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
        }
    }

    private void  savePvDiag(User us,String pkPv,String codeICD,String nameICD){
        List<Map<String,Object>> daig = zsphOpMapper.qryDaigByCode(codeICD);
        if(daig !=null&&daig.size()>0){
            //??????cn_diag
            CnDiag cnDiag = new CnDiag();
            cnDiag.setPkOrg(us.getPkOrg());
            cnDiag.setPkEmpDiag(us.getPkEmp());
            cnDiag.setNameEmpDiag(us.getNameEmp());
            cnDiag.setDateDiag(new Date());
            cnDiag.setPkPv(pkPv);
            cnDiag.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_4);
            DataBaseHelper.insertBean(cnDiag);
            CnDiagDt cnDiagDt = new CnDiagDt();
            cnDiagDt.setPkCndiag(cnDiag.getPkCndiag());
            cnDiagDt.setPkDiag(MapUtils.getString(daig.get(0),"pkDiag"));
            cnDiagDt.setDescDiag(nameICD);
            cnDiagDt.setFlagMaj("1");
            cnDiagDt.setSortNo(0);
            DataBaseHelper.insertBean(cnDiagDt);
            PvDiag pvDiag = new PvDiag();
            pvDiag.setSortNo(0);
            pvDiag.setPkPv(pkPv);
            pvDiag.setPkDiag(MapUtils.getString(daig.get(0),"pkDiag"));
            pvDiag.setDescDiag(nameICD);
            pvDiag.setFlagMaj("1");
            pvDiag.setDateDiag(new Date());
            pvDiag.setPkEmpDiag(us.getPkEmp());
            pvDiag.setNameEmpDiag(us.getNameEmp());
            pvDiag.setNameDiag(nameICD);
            pvDiag.setCodeIcd(codeICD);
            DataBaseHelper.insertBean(pvDiag);
        }
    }

    /**
     * ??????????????????
     * @param master
     * @param pkPv
     * @return
     */
    private PvEncounter savePvEncounter(PiMaster master, String pkPv, BdHp bdHp, User user,BdOuDept bdOuDept,String flagSpec){
        // ??????????????????
        PvEncounter pvEncounter = new PvEncounter();
        //pvEncounter.setPkOrg(master.getPkOrg());
        pvEncounter.setPkPv(pkPv);
        pvEncounter.setFlagSpec(StringUtils.isNotBlank(flagSpec)?flagSpec:"0");
        pvEncounter.setPkPi(master.getPkPi());
        pvEncounter.setPkDept(bdOuDept.getPkDept());//????????????
        pvEncounter.setCodePv(ApplicationUtils.getCode("0301"));
        pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_4);// ??????
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_2); // ??????
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(master.getBirthDate(),null));
        pvEncounter.setAddress(master.getAddress());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("0");
        pvEncounter.setDtMarry(master.getDtMarry());
        pvEncounter.setPkInsu(bdHp.getPkHp());//????????????
        pvEncounter.setPkPicate(master.getPkPicate());
        pvEncounter.setPkEmpReg(user.getPkEmp());
        pvEncounter.setNameEmpReg(user.getNameEmp());
        pvEncounter.setPkEmpPhy(user.getPkEmp());
        pvEncounter.setNameEmpPhy(user.getNameEmp());
        pvEncounter.setDateBegin(new Date());//??????????????????
        //pvEncounter.setDateReg(appointMentDate);//?????????????????????
        //pvEncounter.setDateClinic(appointMentDate);//????????????
        pvEncounter.setFlagCancel("0");
        pvEncounter.setDtIdtypeRel("01");
        pvEncounter.setDtPvsource(master.getDtSource());
        pvEncounter.setNameRel(master.getNameRel());
        pvEncounter.setIdnoRel(master.getIdnoRel());
        pvEncounter.setTelRel(master.getTelRel());
        pvEncounter.setEuPvmode("0");
        pvEncounter.setEuStatusFp("0");
        pvEncounter.setEuLocked("0");
        pvEncounter.setEuDisetype("0");
        pvEncounter.setTs(new Date());
        DataBaseHelper.insertBean(pvEncounter);
        return pvEncounter;
    }

    /**
     * ??????????????????
     * @param master
     * @param pv
     * @param user
     * @return
     */
    public PvPe savePvPe(PiMaster master, PvEncounter pv, User user){
        PvPe pvPe = new PvPe();
        pvPe.setPkPv(pv.getPkPv());
        pvPe.setPkPi(master.getPkPi());
        pvPe.setEffectiveB(new Date());
        pvPe.setTicketno(0L);
        DataBaseHelper.insertBean(pvPe);
        return pvPe;
    }
}
