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
                listener.error("未传入parameter");
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
                listener.error("请至少传入一个有效参数");
                return;
            }
            //这三个条件有任何一个有值，就判断三个都不许有值
            if(StringUtils.isNotBlank(MapUtils.getString(paramMap,"deptCode"))
                    ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"begindata"))
                    ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"enddata"))){
                if(StringUtils.isBlank(MapUtils.getString(paramMap,"deptCode"))
                        ||StringUtils.isBlank(MapUtils.getString(paramMap,"begindata"))
                        ||StringUtils.isBlank(MapUtils.getString(paramMap,"enddata"))){
                    listener.error("组合查询必须包含deptCode+begindata+enddata");
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
                    encounter.setDiagnosis(EncounterBuild.createDiag(pvDiagList));//诊断
                    response.setOutcome(BeanMap.create(encounter));
                    entryList.add(entry);
                }
                listener.success(entryList);
            } else {
                listener.error("没有数据");
            }
        } catch (BusException e) {
            listener.error(e.getMessage());
        } catch (Exception e){
            logger.error("查询门诊就诊信息异常：",e);
            listener.error(e.getMessage());
        }
    }

    /**
     *  预约|取消预约
     * 1. 不新增患者，会先调用 建档接口 > 同时会先调用获取排班相关接口
     * 2.接受患者预约(或者取消）,按照一次预约一条处理<br>
     * 3.    SchApptPv 已经写入，但是pkPv没有值，******缴费后要更新****
     * @param param
     */
    @Override
    public void saveAppt(String param){
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<LinkedTreeMap<String, String>> extensionList = new ArrayList<>();
        List<Map<String, Object>> entryList = (List<Map<String, Object>>) objEntry;
        //获取到预约数据节点
        List<ApptResource> apptList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                apptList.add(TransfTool.mapToBean(ApptResource.class, resourceMap));
                extensionList.addAll((List<LinkedTreeMap<String, String>>) resourceMap.get("extension"));
            }
        });
        if(CollectionUtils.isEmpty(apptList)){
            throw new BusException("未传入预约信息");
        }
        ApptResource apptResource = apptList.get(0);
        Patient patient = apptResource.getSubject();
        if(CollectionUtils.isEmpty(apptResource.getAppointment())){
            throw new BusException("appointment集合为空");
        }
        Appointment appointment = apptResource.getAppointment().get(0);
        if(patient == null || appointment ==null){
            throw new BusException("subject、appointment都不能为空");
        }
        if(!Arrays.asList("booked","cancelled").contains(appointment.getStatus())){
            throw new BusException("预约接口只支持booked|预约、cancelled|取消两个状态");
        }
        //预约序号标识 -- 等于His预约主键
        String pkSchappt = null;
        if(CollectionUtils.isNotEmpty(appointment.getIdentifier())){
            Identifier identifier = appointment.getIdentifier().get(0);
            if(identifier!=null && "id/bookNo".equalsIgnoreCase(identifier.getSystem())){
                pkSchappt = identifier.getValue();
            }
        }
        if(StringUtils.isBlank(pkSchappt)) {
            throw new BusException("id/bookNo不能为空");
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
                    //预约来源,,我们是字典，看了几个库，完全没法对应暂时直接存储
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
            throw new BusException("extension中SchId不能为空");
        }
        if("booked".equalsIgnoreCase(appointment.getStatus())) {
            //必须使用患者id
            Identifier patientId = patient.getIdentifier().stream().filter(identifier -> {
                return "code/patientId".equalsIgnoreCase(identifier.getSystem());
            }).findFirst().get();
            if(patientId==null || StringUtils.isBlank(patientId.getValue())){
                throw new BusException("code/patientId不能为空");
            }
            if(StringUtils.isBlank(appointment.getStart())||StringUtils.isBlank(appointment.getEnd())){
                throw new BusException("start、end（预约起止时间）都不能为空");
            }
            PiMasterRegVo piMasterRegVo = DataBaseHelper.queryForBean("select pk_pi,pk_org,pk_emp,name_pi,MOBILE,FLAG_REALMOBILE,birth_date,ID_NO,idno_rel from pi_master where del_flag = '0' and code_op = ?",
                    PiMasterRegVo.class, new Object[]{patientId.getValue()});
            if(piMasterRegVo == null){
                throw new BusException("依据患者编码未获取到患者信息");
            }
            if (StringUtils.isBlank(piMasterRegVo.getIdNo())&&StringUtils.isBlank(piMasterRegVo.getIdnoRel())){
                throw new BusException("按实名制就诊规定，无证件号码患者无法进行线上预约，请携带相关证件到人工收费窗口完善个人证件信息。");
            }
            /*
            if(!"1".equals(piMasterRegVo.getFlagRealmobile())){
                throw new BusException("您预留的手机号码："+piMasterRegVo.getMobile()+"，未经过有效认证，请到官微、自助机或人工窗口进行认证后再进行预约!!");
            }*/
            Map<String, Object> dateWorkMap = DataBaseHelper.queryForMap("select to_char(sch.DATE_WORK,'yyyy-MM-dd') DATE_WORK,dept.code_dept,dept.dt_medicaltype from SCH_SCH sch left join bd_ou_dept dept on sch.pk_dept = dept.pk_dept  where sch.PK_SCH=?", new Object[]{pkSch});
            if(MapUtils.isEmpty(dateWorkMap)){
                throw new BusException("未获取到对应排班");
            }
            //儿科科室
            Map<String, Object> codeTypeMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='CN0001' and pk_org=?",UserContext.getUser().getPkOrg());
            //获取限制年龄
            Map<String, Object> codePv0019Map = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0019' and pk_org=?",UserContext.getUser().getPkOrg());
            //获取内科限制年龄
            Map<String, Object> codePv0046Map = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag = '0' and  code='PV0046' and pk_org=?",UserContext.getUser().getPkOrg());
            int age = 0;
            try {
                age = DateUtils.getAge(piMasterRegVo.getBirthDate());
            } catch (Exception e) {
                throw new BusException("年龄计算出错，请检查患者的出生日期！");
            }
            if(null != codeTypeMap){
                String card[] = codeTypeMap.get("val").toString().split(",");
                if(codePv0019Map!=null&&StringUtils.isNotBlank(MapUtils.getString(codePv0019Map,"val"))){
                    for (int i = 0; i < card.length; i++) {
                        if((card[i]).equals(MapUtils.getString(dateWorkMap,"codeDept"))){
                            if(age>MapUtils.getInteger(codePv0019Map,"val")){
                                throw new BusException("儿科只能预约"+MapUtils.getInteger(codePv0019Map,"val")+"岁以下的患者！");
                            }
                        }
                    }
                }
            }
            if(codePv0046Map!=null&&StringUtils.isNotBlank(MapUtils.getString(codePv0046Map,"val"))){
                if("01".equals(MapUtils.getString(dateWorkMap,"dtMedicaltype"))&&
                        age<MapUtils.getInteger(codePv0046Map,"val")){
                    throw new BusException("内科只能预约"+MapUtils.getInteger(codePv0046Map,"val")+"岁以上的患者！");
                }

            }


            if(StringUtils.isNotBlank(dateApp) &&
                    !StringUtils.equals(dateApp,MapUtils.getString(dateWorkMap,"dateWork"))){
                throw new BusException("传入的AppointVisitDate的值有误，与排班对应日期不一致");
            }

            piMasterRegVo.setPkSch(pkSch);
            piMasterRegVo.setPkSchappt(pkSchappt);
            //接口的都属于外部预约
            piMasterRegVo.setDtApptype("2");
            piMasterRegVo.setNote(orderidExt);
            /**
             * ① 获取--更新--失败再回滚的方式。有一定几率出现后来者拿到的号比早来的号小的情况。退号也会导致这个，所以不管
             * ② 使用数据库排它锁，避免1的情况出现。
             */
            SchTicket ticket =null;
            try{
                //获取号表~~get中有update自治事务。依据PV0044配置是否允许获取时段之前的号
                Map<String,Object> ticketParam = new HashMap<>();
                ticketParam.put("pkSch",piMasterRegVo.getPkSch());
                ticketParam.put("startTime",appointment.getStart());
                ticketParam.put("endTime",appointment.getEnd());
                //是否查询外部号
                ticketParam.put("isExternal",true);
                ticket = ticketPubService.getUnusedAppTicket(ticketParam);
                piMasterRegVo.setTicketNo(ticket.getTicketno());
                piMasterRegVo.setStartTime(ticket.getBeginTime());
                piMasterRegVo.setEndTime(ticket.getEndTime());
                regCommonService.saveAppt(piMasterRegVo);
            } catch (Exception e){
                //异常了要回滚号
                ticketPubService.setTicketUnused(ticket);
                if(e instanceof DuplicateKeyException){
                    throw new BusException("id/bookNo已存在，请重新传入");
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
        //推送给平台
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
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"idno"))//身份证号
                && StringUtils.isBlank(MapUtils.getString(paramMap,"phone"))//电话号码
                && StringUtils.isBlank(MapUtils.getString(paramMap,"name"))//姓名
                && StringUtils.isBlank(MapUtils.getString(paramMap,"begindata"))//姓名
                && StringUtils.isBlank(MapUtils.getString(paramMap,"enddata"))//姓名
                && StringUtils.isBlank(MapUtils.getString(paramMap,"deptCode"))//姓名
                && StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))) {//患者编码
            throw new BusException("至少传入一个查询参数idno|phone|name|patientId");
        }
        //这三个条件有任何一个有值，就判断三个都必须有值
        if(StringUtils.isNotBlank(MapUtils.getString(paramMap,"deptCode"))
                ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"begindata"))
                ||StringUtils.isNotBlank(MapUtils.getString(paramMap,"enddata"))){
            if(StringUtils.isBlank(MapUtils.getString(paramMap,"deptCode"))
                    ||StringUtils.isBlank(MapUtils.getString(paramMap,"begindata"))
                    ||StringUtils.isBlank(MapUtils.getString(paramMap,"enddata"))){
                throw new BusException("组合查询必须包含deptCode+begindata+enddata");
            }
        }
        String status = MapUtils.getString(paramMap,"status");//状态
        if(StringUtils.isNotBlank(status)){
            if(!Arrays.asList("booked","cancelled").contains(status)){
                throw new BusException("预约接口只支持booked|预约、cancelled|取消两个状态");
            } else{
                paramMap.put("apptStatus","booked".equals(status)?"0":"1");
            }
        }
        //限制了一下时间,按条件查询时只能查2个月内的符合条件的数据
        paramMap.put("limitDate",DateUtils.addDate(new Date(),-2,2,"yyyyMMddHHmmss"));
        List<SchApptVo> apptList = opSendMapper.getSchAppt(paramMap);
        if(CollectionUtils.isEmpty(apptList)){
            throw new BusException("依据传入条件未获取到预约信息");
        }
        List<Entry> entryList = new ArrayList<>();
        for (SchApptVo schApptVo : apptList) {
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);

            ApptResource apptResource = new ApptResource();
            apptResource.setResourceType("Encounter");
            apptResource.setId(NHISUUID.getKeyId());
            //患者信息
            Patient subject = new Patient();
            subject.setResourceType("Patient");
            subject.setIdentifier(Lists.newArrayList(new Identifier("code/patientId",schApptVo.getCodeOp())
                    ,new Identifier("code/idNo",schApptVo.getIdNo())));
            subject.setName(Arrays.asList(new TextElement(schApptVo.getNamePi())));
            subject.setTelecom(Arrays.asList(new Identifier("phone",schApptVo.getMobile())));
            subject.setBirthDate(schApptVo.getBirthDate());
            subject.setGender("02".equals(schApptVo.getDtSex())?"male":"03".equals(schApptVo.getDtSex())?"female":"other");//患者性别
            apptResource.setSubject(subject);
            //预约信息
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
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到数据节点
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                TriageVo vo = TransfTool.mapToBean(TriageVo.class, resourceMap);
                if(vo.getQueNo() == null){
                    throw new BusException("未传入队列号字段queNo");
                }
                String codePv=vo.getRegisterId(),codeDept = vo.getDeptCode(),codeDoctor=vo.getDoctorCode();
                PvEncounter pvEncounter = DataBaseHelper.queryForBean("select PK_PV,pi.CODE_PI,EU_STATUS,pi.CODE_OP from PV_ENCOUNTER pv " +
                        " inner join PI_MASTER pi on pv.PK_PI=pi.PK_PI where pv.CODE_PV=? and pv.EU_PVTYPE in('1','2','4')",PvEncounter.class, new Object[]{codePv});
                if(pvEncounter == null) {
                    throw new BusException("依据就诊编码未获取到就诊信息registerId:"+codePv);
                }
                if(!StringUtils.equals(vo.getPatientId(), pvEncounter.getCodeOp())) {
                    throw new BusException("传入的患者ID和就诊信息不匹配patientId:"+vo.getPatientId());
                }
                Map<String, Object> dept = DataBaseHelper.queryForMap("select PK_DEPT from BD_OU_DEPT where CODE_DEPT=? and DEL_FLAG='0'", new Object[]{codeDept});
                if(MapUtils.isEmpty(dept)) {
                    throw new BusException("依据科室编码未获取到科室信息："+codeDept);
                }
                Map<String, Object> doctor = DataBaseHelper.queryForMap("select PK_EMP,NAME_EMP from BD_OU_EMPLOYEE where CODE_EMP=? and DEL_FLAG='0'", new Object[]{codeDoctor});
                if(MapUtils.isEmpty(doctor)) {
                    throw new BusException("依据医生编码未获取到医生信息："+codeDoctor);
                }
                PvQue pvQue = DataBaseHelper.queryForBean("select PK_PVQUE from PV_QUE where PK_PV=?",PvQue.class,new Object[]{pvEncounter.getPkPv()});
                if(pvQue == null) {
                    pvQue = new PvQue();
                    pvQue.setSortno(0);//没有传入，占位
                } else {
                    if(!EnumerateParameter.ZERO.equals(pvEncounter.getEuStatus())){
                        throw new BusException("患者已经处于就诊状态，分诊失败："+codePv);
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
                    throw new BusException("operationTime时间格式化异常");
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
        //检查、检验、输血。只改状态，且只能改为比当前大的状态，不回退状态，不关注费用等
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if(objEntry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
        //获取到数据节点
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                String type = MapUtils.getString(resourceMap,"implicitRules");
                List<Map<String,Object>> list = (List<Map<String, Object>>) MapUtils.getObject(resourceMap,"identifier");
                if(CollectionUtils.isEmpty(list)){
                    throw new BusException("未传入identifier");
                }
                //解析申请单号
                Optional<Map<String, Object>> apply = list.stream().filter(idf -> {
                    return StringUtils.isNotBlank(MapUtils.getString(idf, "system"))
                            && StringUtils.equals(MapUtils.getString(idf, "system"), "id/applyno")
                            && StringUtils.isNotBlank(MapUtils.getString(idf, "value"));
                }).findFirst();
                if(!apply.isPresent()){
                    throw new BusException("identifier中未传入id/applyno");
                }

                String applyCode = MapUtils.getString(apply.get(), "value");
                String status = MapUtils.getString(resourceMap, "status");//1.开立 2.核对 3.采样 4.上机 5.报告 6.审核  7.发布  8.取消
                boolean jy=StringUtils.equalsIgnoreCase(type,"JYSQDZTXG"),//检验
                        jc=StringUtils.equalsIgnoreCase(type,"JCSQDZTXG"),//检查
                        sx=StringUtils.equalsIgnoreCase(type,"SXSQDZTXG"),//输血
                        ss=StringUtils.equalsIgnoreCase(type,"SSSQDZTXG");//手术
                String occurrenceDateTime = MapUtils.getString(resourceMap, "occurrenceDateTime");
                String pkCnord = null;
                if(jc){
                    //解析申请单号
                    List<String> pkCnordList = list.stream().filter(idf ->
                            StringUtils.isNotBlank(MapUtils.getString(idf, "system"))
                                    && StringUtils.equals(MapUtils.getString(idf, "system"), "id/applyDetailNo")
                                    && StringUtils.isNotBlank(MapUtils.getString(idf, "value"))
                    ).map(ord -> MapUtils.getString(ord, "value")).collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(pkCnordList)){
                        throw new BusException("identifier中未传入id/applyDetailNo");
                    }
                    pkCnord = pkCnordList.get(0);
                    if(DataBaseHelper.queryForScalar("select count(1) from cn_order where pk_cnord=? and CODE_APPLY=?"
                            ,Integer.class,new Object[]{pkCnord,applyCode}) ==0){
                        throw new BusException("依据单号和医嘱键未获取到医嘱单据信息");
                    }
                }
                if(StringUtils.isBlank(status)){
                    throw new BusException("status为空");
                }
                //解析执行医生
                List<Map<String,Object>> perfromObj = (List<Map<String, Object>>) MapUtils.getObject(resourceMap,"performer");
                if(CollectionUtils.isEmpty(perfromObj)){
                    throw new BusException("未传入performer");
                }
                List<LocationOp> performerList = JsonUtil.readValue(ZsphMsgUtils.getJsonStr(perfromObj), new TypeReference<List<LocationOp>>() {});
                List<LocationOp> doctors = performerList.stream().filter(lc -> "Practitioner".equalsIgnoreCase(lc.getResourceType())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(doctors)) {
                    throw new BusException("未传入执行人信息");
                }
                LocationOp doctor = doctors.get(0);
                if(doctor.getIdentifier()==null || StringUtils.isBlank(doctor.getIdentifier().getValue())) {
                    throw new BusException("未传入执行人编码");
                }
                List<LocationOp> depts = performerList.stream().filter(lc -> "Location".equalsIgnoreCase(lc.getResourceType())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(depts)) {
                    throw new BusException("未传入执行科室信息");
                }
                LocationOp dept = depts.get(0);
                if(dept.getIdentifier()==null || StringUtils.isBlank(dept.getIdentifier().getValue())) {
                    throw new BusException("未传入执行科室编码");
                }
                String codeDoctor = doctor.getIdentifier().getValue();
                String codeDept = dept.getIdentifier().getValue();
                BdOuEmployee employee = DataBaseHelper.queryForBean("select NAME_EMP,PK_EMP,PK_ORG from BD_OU_EMPLOYEE where CODE_EMP =? and DEL_FLAG='0'",
                        BdOuEmployee.class, new Object[]{codeDoctor});
                if(employee == null){
                    throw new BusException("依据传入医生编码未获取到医生信息");
                }
                BdOuDept bdOuDept = DataBaseHelper.queryForBean("select name_dept,pk_dept from BD_OU_DEPT where code_dept =? and DEL_FLAG='0'",
                        BdOuDept.class, new Object[]{codeDept});
                if(bdOuDept == null){
                    throw new BusException("依据传入科室编码未获取到科室信息");
                }
                //解析申请单项目类别~~his没有细分类状态，只关注单据状态
//                List<Map<String,Object>> listCagtegory = (List<Map<String, Object>>) MapUtils.getObject(resourceMap,"category");
//                if(CollectionUtils.isEmpty(listCagtegory)){
//                    throw new BusException("未传入category");
//                }
//                Category category = TransfTool.mapToBean(Category.class, listCagtegory.get(0));
//                if(CollectionUtils.isEmpty(category.getCoding())){
//                    throw new BusException("未传入category>coding");
//                }
//                String nameCate = category.getCoding().get(0).getDisplay();
                String qrySql = null,modSql=null;
                String euStatus = null;
                if(jc){//检查
                    if("2".equals(status)){
                        euStatus = "2";
                    } else if(status.compareTo("2")>0 && status.compareTo("6")<=0){
                        euStatus = "3";
                    }else if("7".equals(status)){
                        euStatus = "4";
                    }else if("8".equals(status)){//取消
                        euStatus = "1";
                    }
                    modSql = "update CN_RIS_APPLY set modifier=?,ts=?,eu_status=? where PK_CNORD=? ";
                } else if(jy){//检验
                    if("2".equals(status)){
                        euStatus = "2";
                    } else if(status.compareTo("2")>0 && status.compareTo("7")<=0){
                        euStatus = "3";
                    }else if("8".equals(status)){
                        euStatus = "4";
                    }else if("1".equals(status)){//取消
                        euStatus = "1";
                    }
                    qrySql = "select distinct lab.PK_ORDLIS pk,ord.PK_CNORD FROM CN_LAB_APPLY lab inner join CN_ORDER ord on lab.PK_CNORD=ord.PK_CNORD where CODE_APPLY=?";
                    modSql = "update CN_LAB_APPLY set modifier=?,ts=?,eu_status=? where PK_ORDLIS in";
                } else if(sx){//输血
                    if(status.compareTo("2")>=0 && status.compareTo("6")<=0){
                        euStatus = "4";
                    }else if("7".equals(status)){
                        euStatus = "5";
                    }
                    qrySql="select distinct tran.PK_ORDBT pk,ord.PK_CNORD FROM CN_TRANS_APPLY tran inner join CN_ORDER ord on tran.PK_CNORD=ord.PK_CNORD where CODE_APPLY=?";
                    modSql = "update CN_TRANS_APPLY set modifier=?,ts=?,eu_status=? where PK_ORDBT in";
                } else if(ss){//手术
                    if(status.compareTo("2")>=0 && status.compareTo("6")<=0){
                        euStatus = "3";
                    }else if("7".equals(status)){
                        euStatus = "5";
                    }
                    qrySql="select op.PK_ORDOP pk,ord.PK_CNORD FROM CN_OP_APPLY op inner join CN_ORDER ord on op.PK_CNORD=ord.PK_CNORD where CODE_APPLY=?";
                    modSql = "update CN_OP_APPLY set modifier=?,ts=?,eu_status=? where PK_ORDOP in";
                } else {
                    throw new BusException("implicitRules传入有误,请确认");
                }

                String pkCnordStr = null;
                List<Map<String,Object>> qryData = null;
                if(jc) {
                    pkCnordStr = "'" + pkCnord+ "'";
                } else {
                    qryData = DataBaseHelper.queryForList(qrySql,new Object[]{applyCode});
                    if(CollectionUtils.isEmpty(qryData)){
                        throw new BusException("依据传入单号未匹配到具体单据信息");
                    }
                    pkCnordStr = CommonUtils.convertListToSqlInPart(qryData.stream().map(map -> MapUtils.getString(map,"pkCnord")).collect(Collectors.toList()));
                }

                //更改申请单状态
                if(euStatus !=null){
                    if(jc){
                        DataBaseHelper.update(modSql,new Object[]{codeDoctor,new Date(),euStatus,pkCnord});
                    } else {
                        String pkStr = CommonUtils.convertListToSqlInPart(qryData.stream().map(map -> MapUtils.getString(map,"pk")).collect(Collectors.toList()));
                        DataBaseHelper.update(modSql + "("+pkStr+") ",new Object[]{codeDoctor,new Date(),euStatus});
                    }
                } else {
                    logger.info("更新申请单状态时未匹配到正确状态"+status);
                }

                List<Map<String ,Object>> listDept = QueryUtils.queryOccDeptByCodeEmp(codeDoctor);
                String pkDeptArea = null;
                String pkDeptJob = null;
                if(listDept !=null&&listDept.size()>0){
                    pkDeptArea = MapUtils.getString(listDept.get(0),"pkDeptArea");
                    pkDeptJob = MapUtils.getString(listDept.get(0),"pkDeptJob");
                }

                        //更改执行单状态（符合条件时，取消执行单、或者“执行”）
                if(StringUtils.equals(status,"8") && !jy){
                    //取消执行
                    if(ss){
                        DataBaseHelper.execute("update cn_op_apply set eu_status='1' where eu_status='5' and pk_cnord in ("+pkCnordStr+")");
                        //只有住院才有这个，先放着
                        DataBaseHelper.execute("update ex_order_occ set eu_status='0',flag_canc='1',pk_dept_canc=?,"
                                        + "date_canc=?,pk_emp_canc=?,name_emp_canc=?,modifier=?,ts=? "
                                        + "where pk_cnord in ("+pkCnordStr+") and eu_status='1' ",
                                new Object[]{ bdOuDept.getPkDept(), new Date(),employee.getPkEmp(), employee.getNameEmp(), employee.getPkEmp(), new Date()});

                    } else {
                        DataBaseHelper.update("update ex_assist_occ SET flag_occ='0',date_occ=null,pk_emp_occ=null,name_emp_occ=null,pk_emp_conf=null,name_emp_conf=null,pk_msp=null,eu_status='0',date_canc=?,pk_emp_canc ='"+employee.getPkEmp()+"',name_emp_canc='"+employee.getNameEmp()+"',pk_dept_job =null " +
                                " where flag_occ='1' and flag_canc='0' and PK_CNORD in ("+pkCnordStr+")",new Object[]{new Date()});
                    }
                } else if(status.compareTo("4")>=0 && status.compareTo("7")<=0 && !jy){
                    //TODO 平台状态为？时待医院确认：处理执行单状态、检验、检查；手术状态
/*                    Integer count = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in("+pkCnordStr+") and flag_settle='0'", Integer.class);
                    if(count>0){
                        throw new BusException("有未结算费用，不能执行");
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
                }else if(jy){//1.开立 2.核对 3.打印 4.采样 5.送检 6.签收 7.登记 8.报告  9.取消
                    if(StringUtils.equals(status,"3")){//检验确定3打印不能退费改执行1开立改未执行

                        Date current =  new Date();
                        //更新住院执行单为执行
                        DataBaseHelper.update(" update ex_order_occ set eu_status='1', date_occ = ?,pk_dept_occ = ?, pk_org_occ = ?," +
                                        " pk_emp_occ=?, name_emp_occ=? WHERE PK_CNORD in ("+pkCnordStr+")"
                                ,new Object[]{current,bdOuDept.getPkDept(),employee.getPkOrg(),employee.getPkEmp(),employee.getNameEmp()});
                        //更新门诊执行单为执行
                        DataBaseHelper.update("update ex_assist_occ SET pk_dept_area=?,pk_dept_job=?,TS=?,FLAG_OCC=1,DATE_OCC =?,PK_EMP_OCC=?,NAME_EMP_OCC=?,EU_STATUS=1,PK_ORG_OCC=?,PK_DEPT_OCC=?,pk_emp_conf=?,name_emp_conf=? " +
                                        " WHERE flag_occ = 0 and flag_canc = '0' and flag_refund = '0' and PK_CNORD in ("+pkCnordStr+")"
                                ,new Object[]{pkDeptArea,pkDeptJob,current,current,employee.getPkEmp(),employee.getNameEmp(),employee.getPkOrg(),bdOuDept.getPkDept(),employee.getPkEmp(),employee.getNameEmp()});
                        //在拓展属性【0113】属性值为1的项目则更新收费项目执行科室到当前执行科室
                        DataBaseHelper.execute("update bl_op_dt cg set cg.pk_dept_ex=?,cg.pk_emp_ex=?,cg.name_emp_ex=? where pk_cnord in ("+pkCnordStr+") and " +
                                " flag_settle='1' And  Exists(Select 1 From bd_dictattr attr where attr.code_attr='0113' and " +
                                " cg.pk_item=attr.pk_dict)",new Object[]{bdOuDept.getPkDept(),employee.getPkEmp(),employee.getNameEmp()});
                    }else if(StringUtils.equals(status,"1")){
                        //住院更新执行单为未执行
                        DataBaseHelper.execute("update ex_order_occ set eu_status='0',flag_canc='1',pk_dept_canc=?,"
                                        + "date_canc=?,pk_emp_canc=?,name_emp_canc=?,modifier=?,ts=? "
                                        + "where pk_cnord in ("+pkCnordStr+") and eu_status='1' ",
                                new Object[]{ bdOuDept.getPkDept(), new Date(),employee.getPkEmp(), employee.getNameEmp(), employee.getPkEmp(), new Date()});
                        //门诊更新执行单为未执行
                        DataBaseHelper.update("update ex_assist_occ SET flag_occ='0',date_occ=null,pk_emp_occ=null,name_emp_occ=null,pk_msp=null,pk_emp_conf =null,name_emp_conf=null,pk_dept_job=null,pk_dept_area=null,eu_status='0',date_canc=?,pk_emp_canc =?,name_emp_canc=? " +
                                " where flag_occ='1' and flag_canc='0' and PK_CNORD in ("+pkCnordStr+")",new Object[]{new Date(),employee.getPkEmp(),employee.getNameEmp()});
                    }
                }

            }
        });
    }

    /**
     * 保存就诊
     * @param param
     */
    @Override
    public List<Entry> saveOpcg(String param) {
        Map<String, Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object objEntry = MapUtils.getObject(requestBody, "entry");
        if (objEntry == null) {
            throw new BusException("未传入entry");
        }
        List<Map<String, Object>> entryList = (List<Map<String, Object>>) objEntry;
        //获取到数据节点
        List<OpcgData> opCgList = new ArrayList<>();
        entryList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                opCgList.add(TransfTool.mapToBean(OpcgData.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(opCgList)){
            throw new BusException("未传入体检信息信息");
        }
        //支持一个患者一条
        OpcgData opcgData = opCgList.get(0);
        PvEncounter pv = null;
        if(opcgData != null){
            //1.患者信息
            String sql = "select * from pi_master pi where code_op=?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, new Object[]{opcgData.getPatientid()});
            if (piMaster == null) {
                throw new BusException("根据patientid【"+opcgData.getPatientid()+"】未获取到患者信息！");
            }
            String hpSql = "select * from BD_HP where del_flag = '0' and eu_hptype ='0' ";
            BdHp bdHp = DataBaseHelper.queryForBean(hpSql,BdHp.class);

            //3.User信息构建
            User user = new User();
            Map<String ,Object> emp =QueryUtils.queryPkEmpByCode(opcgData.getCodeDoc());
            if(emp==null){
                throw new BusException("根据医生code未获取到医生信息");
            }
            BdOuDept dept=QueryUtils.getDeptInfo(opcgData.getCodeDept());
            if(dept==null){
                throw new BusException("根据科室code未获取到科室信息");
            }
            user.setPkEmp(MapUtils.getString(emp, "pkEmp"));//医生就当做操作人
            user.setCodeEmp(MapUtils.getString(emp, "codeEmp"));
            user.setNameEmp(MapUtils.getString(emp, "nameEmp"));
            user.setPkOrg(MapUtils.getString(emp, "pkOrg"));//机构
            //4.生成就诊记录
            String pkPv = NHISUUID.getKeyId();
            pv = savePvEncounter(piMaster, pkPv,bdHp,user,dept,opcgData.getFlagSpec());
            //5.生成体检就诊记录，写表pv_pe
            savePvPe(piMaster,pv,user);
            //6.保存诊断信息
            //暂时用icd码，调试的时候根据实际情况改
            if(StringUtils.isNotBlank(opcgData.getCodeICD())){
                savePvDiag(user,pkPv,opcgData.getCodeICD(),opcgData.getNameICD());
            }
            List<CgData> cgDataList = opcgData.getCgDataList();
            if(cgDataList!=null&&cgDataList.size()>0){
                //保存收费项目
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
                // 添加至需记费参数集合
                List<BlPubParamVo> params = new ArrayList<>();
                for (CgData opParam : cgDataList) {
                    Map<String, Object> item = itemMaps.get(opParam.getCodeItem());
                    // 添加至需记费参数集合
                    BlPubParamVo dtparam = new BlPubParamVo();
                    dtparam.setPkDeptAreaapp(opParam.getCodeDeptArea());
                    dtparam.setPkDeptJob(opParam.getCodeDept());
                    dtparam.setBatchNo(MapUtils.getString(item, "batchNo"));
                    dtparam.setDateExpire(null);
                    dtparam.setDateHap(new Date());
                    dtparam.setEuAdditem("1");
                    dtparam.setEuPvType("4");// 就诊类型默认门诊
                    dtparam.setFlagPd(MapUtils.getString(item, "flagPd"));
                    dtparam.setFlagPv("0");// 非挂号费
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
                    if (BlcgUtil.converToTrueOrFalse(MapUtils.getString(item,"flagPd"))) // 药品
                        dtparam.setPkOrd(MapUtils.getString(item,"pkItem"));
                    if(StringUtils.isBlank(MapUtils.getString(item,"pkItem"))){
                        throw new BusException("收费项目"+opParam.getCodeItem()+"不存在，请核对收费项目编码！");
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
                // 调用批量记费方法
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
            issue.setDiagnostics("成功");
            issue.setSeverity("infomation");
            issue.setCodePv(pv.getCodePv());
            outcome.setIssue(Arrays.asList(issue));
            response.setOutcome(BeanMap.create(outcome));
        }
        return Arrays.asList(entry);
    }

    /**
     * 删除体检费用
     * @param param
     */
    @Override
    public void deleteOpcgpushStatus(String param){
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
        //每条消息只有一个删除一个患者（三方不会传多条）
        for(Map<String,Object> map: mapList){
            if(MapUtils.getString(map,"codePv") == null){
                throw new BusException("未传入codePv信息");
            }
            //查询医嘱信息
            String sql = " select pv.pk_pv from pv_encounter pv  where pv.CODE_PV = ?";
            List<Map<String,Object>> orderMap = DataBaseHelper.queryForList(sql,MapUtils.getString(map,"codePv"));
            //未找到对应就诊记录
            if(orderMap==null ||orderMap.size()==0){
                throw new BusException("未找到有效的就诊记录,codePv为："+MapUtils.getString(map,"codePv"));
            }
            String sqlSettle = "select count(1)  from BL_OP_DT where flag_settle='1' and pk_pv =?";
            Integer count = DataBaseHelper.queryForScalar(sqlSettle, Integer.class,MapUtils.getString(orderMap.get(0),"pkPv"));
            if(count>0){
                throw new BusException("该患者已经结算，不能进行体检信息删除，请先退费,codePv为："+MapUtils.getString(map,"codePv"));
            }
            //删除就诊记录（只能删除体检的）
             DataBaseHelper.update("update PV_ENCOUNTER set DEL_FLAG = '1' where code_pv =? and  EU_PVTYPE = '4' ",new Object[] {MapUtils.getString(map,"codePv")});
             DataBaseHelper.update("update PV_PE set DEL_FLAG = '1' where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
             //删除费用明细
             DataBaseHelper.execute("delete BL_OP_DT where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
            // TODO: 2021-02-24 删除诊断
            DataBaseHelper.execute("delete pv_diag where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
            DataBaseHelper.execute("delete cn_diag where pk_pv =? ",new Object[] {MapUtils.getString(orderMap.get(0),"pkPv")});
        }
    }

    private void  savePvDiag(User us,String pkPv,String codeICD,String nameICD){
        List<Map<String,Object>> daig = zsphOpMapper.qryDaigByCode(codeICD);
        if(daig !=null&&daig.size()>0){
            //保存cn_diag
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
     * 保存就诊记录
     * @param master
     * @param pkPv
     * @return
     */
    private PvEncounter savePvEncounter(PiMaster master, String pkPv, BdHp bdHp, User user,BdOuDept bdOuDept,String flagSpec){
        // 保存就诊记录
        PvEncounter pvEncounter = new PvEncounter();
        //pvEncounter.setPkOrg(master.getPkOrg());
        pvEncounter.setPkPv(pkPv);
        pvEncounter.setFlagSpec(StringUtils.isNotBlank(flagSpec)?flagSpec:"0");
        pvEncounter.setPkPi(master.getPkPi());
        pvEncounter.setPkDept(bdOuDept.getPkDept());//就诊科室
        pvEncounter.setCodePv(ApplicationUtils.getCode("0301"));
        pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_4);// 体检
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_2); // 结束
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(master.getBirthDate(),null));
        pvEncounter.setAddress(master.getAddress());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("0");
        pvEncounter.setDtMarry(master.getDtMarry());
        pvEncounter.setPkInsu(bdHp.getPkHp());//默认自费
        pvEncounter.setPkPicate(master.getPkPicate());
        pvEncounter.setPkEmpReg(user.getPkEmp());
        pvEncounter.setNameEmpReg(user.getNameEmp());
        pvEncounter.setPkEmpPhy(user.getPkEmp());
        pvEncounter.setNameEmpPhy(user.getNameEmp());
        pvEncounter.setDateBegin(new Date());//挂号收费日期
        //pvEncounter.setDateReg(appointMentDate);//挂号的排班日期
        //pvEncounter.setDateClinic(appointMentDate);//看诊日期
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
     * 保存体检属性
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
