package com.zebone.nhis.ma.pub.platform.zsrm.service.impl;

import cn.org.zxrl.annotation.ZxRateLimit;
import cn.org.zxrl.util.LimitAlgorithmEnum;
import cn.org.zxrl.util.LimitTypeEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchDeptVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchDoctorVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.OpApply;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Period;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.SchAppChanged;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.SchDeptOutcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.SchDoctorOutcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.SchOutcome;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsphSchMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.zsrm.service.ZsphSchService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ImplicitRulesResolver;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.ma.pub.zsrm.vo.DrugPresUsecateVo;
import com.zebone.nhis.pro.zsba.common.support.DateUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZsphSchServiceImpl implements ZsphSchService {
    private static final Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private ZsphSchMapper zsphSchMapper;

    @Override
    public void getSch(String param, ResultListener listener) {
        try{
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
            if(CollectionUtils.isEmpty(parameterList)){
                listener.error("未传入parameter");
                return;
            }
            Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
            if(StringUtils.isBlank(MapUtils.getString(paramMap,"deptnno"))){
                listener.error("科室编码：deptnno不能为空");
                return;
            }
            if(StringUtils.isBlank(MapUtils.getString(paramMap,"clinicdate"))){
                listener.error("日期不能为空");
                return;
            }
            List<SchVo> schVoList = zsphSchMapper.getSchbyParam(paramMap);
            if(CollectionUtils.isNotEmpty(schVoList)){
                List<Entry> entryList = new ArrayList<>();
                for(SchVo schVo:schVoList){
                    Entry entry = new Entry(new Response());
                    Response response = entry.getResponse();
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    SchOutcome outcome = new SchOutcome();
                    outcome.setResourceType("Schedule");
                    outcome.setIdentifier(Arrays.asList(new Identifier(null,schVo.getPkSch())));
                    outcome.setActive(true);
                    outcome.setServiceCategory(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding(schVo.getEuSrvtype(),TransfTool.getSrvText(schVo.getEuSrvtype()))))));
                    outcome.setServiceType(new TextElement(schVo.getNameDateslot()));
                    //科室、医生信息
                    outcome.setActor(Lists.newArrayList());
                    if(StringUtils.isNotBlank(schVo.getCodeEmp())){
                        //医生信息
                        Outcome actor = new Outcome();
                        actor.setResourceType("Practitioner");
                        actor.setIdentifier(Arrays.asList(new Identifier(null,schVo.getCodeEmp())));
                        actor.setName(Arrays.asList(new TextElement(schVo.getNameEmp())));
                        outcome.getActor().add(actor);
                    }
                    if(StringUtils.isNotBlank(schVo.getCodeDept())){
                        //科室信息
                        Outcome actor = new Outcome();
                        actor.setResourceType("Location");
                        actor.setIdentifier(Arrays.asList(new Identifier(null,schVo.getCodeDept())));
                        actor.setName(schVo.getNameDept());
                        outcome.getActor().add(actor);
                    }
                    outcome.setPlanningHorizon(new Period(DateUtils.parseDate(schVo.getDateWork()+" "+ schVo.getTimeBegin())
                            ,DateUtils.parseDate(schVo.getDateWork()+" "+ schVo.getTimeBegin())));
                    response.setOutcome(BeanMap.create(outcome));
                    entryList.add(entry);
                }
                listener.success(entryList);
            } else {
                listener.error("没有数据");
            }
        } catch (BusException e) {
            listener.error(e.getMessage());
        } catch (Exception e){
            logger.error("查询排班信息异常：",e);
            listener.error(e.getMessage());
        }
    }

    @ZxRateLimit(algorithmType = LimitAlgorithmEnum.RD_COUNTER,checkType = LimitTypeEnum.CUSTOM,limit = 1)
    @Override
    public void getSchApptDept(String param, ResultListener listener) {
        try{
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            //List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
            Map<String, Object> paramMap = null;
            /*if(CollectionUtils.isNotEmpty(parameterList)){
                paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
            } else {
                paramMap = new HashMap<>();
            }

            //已沟通，对方只查询当天的。然后支持时间段查询~
            String now = FastDateFormat.getInstance("yyyy-MM-dd").format(new Date());
            String start=MapUtils.getString(paramMap,"start"),end=MapUtils.getString(paramMap,"end");
            if(StringUtils.isNotBlank(start)) {
                start = start.length() == 5?(now+" "+start):start;
            }
            if(StringUtils.isNotBlank(end)) {
                end = end.length() == 5?(now+" "+end):end;
            }
            paramMap.put("now", now);
            paramMap.put("start", start);
            paramMap.put("end", end);*/
            List<SchDeptVo> schDeptVoList = zsphSchMapper.getSchApptDept(paramMap);
            if(CollectionUtils.isNotEmpty(schDeptVoList)){

                List<Entry> entryList = new ArrayList<>();
                for(SchDeptVo vo:schDeptVoList){
                    Entry entry = new Entry(new Response());
                    Response response = entry.getResponse();
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    SchDeptOutcome outcome = new SchDeptOutcome();
                    outcome.setResourceType("getAppointDeptList");
                    outcome.setIdentifier(Arrays.asList(new Identifier("specialtyId",vo.getCode())));//专科代码
                    outcome.setName(vo.getName());
                    outcome.setDescription(vo.getDescRes());
                    //专科所属诊区 我们没有这个，给成和上面一样
                    outcome.setPhysicalType(new CodeableConcept(Arrays.asList(new Coding(vo.getCodeDept(),vo.getNameDept()))));
                    //父级
                    Optional<SchDeptVo> fatherDept = schDeptVoList.stream().filter(schDept -> {
                        return StringUtils.isNotBlank(vo.getPkFather()) && StringUtils.equals(vo.getPkFather(), schDept.getPkSchres());
                    }).findFirst();
                    Outcome father = new Outcome();
                    father.setResourceType("Organization");
                    father.setIdentifier(Arrays.asList(new Identifier("specialtyFatherId",fatherDept.isPresent()?fatherDept.get().getCode():"")));
                    father.setName(fatherDept.isPresent()?fatherDept.get().getName():"");
                    outcome.setManagingOrganization(father);//父专科信息
                    //子部门信息？
                    Location partOf = new Location();
                    Optional<SchDeptVo> sonDepts = schDeptVoList.stream().filter(schDept -> {
                        return StringUtils.isNotBlank(vo.getPkSchres()) && StringUtils.equals(vo.getPkSchres(), schDept.getPkFather());
                    }).findFirst();
                    partOf.setIdentifier(Arrays.asList(new Identifier("specialtyVoList",sonDepts.isPresent()?sonDepts.get().getCode():"")));
                    outcome.setPartOf(partOf);
                    outcome.setAvailabilityExceptions(vo.getSpec());
                    List<Extension> list = new ArrayList<>();
                    Extension treatarea = new Extension();
                    treatarea.setUrl("treatarea");
                    treatarea.setValueString(vo.getNameDept());
                    list.add(treatarea);
                    Extension deptSortNo = new Extension();
                    deptSortNo.setUrl("deptSortNo");
                    deptSortNo.setValueString(vo.getDeptSortNo());
                    list.add(deptSortNo);
                    Extension treatmentaddress = new Extension();
                    treatmentaddress.setUrl("treatmentaddress");
                    treatmentaddress.setValueString(vo.getNamePlace());
                    list.add(treatmentaddress);
                    outcome.setExtension(list);

                    response.setOutcome(BeanMap.create(outcome));
                    entryList.add(entry);
                }
                listener.success(entryList);
            } else {
                listener.error("没有数据");
            }
        } catch (BusException e) {
            listener.error(e.getMessage());
        } catch (Exception e){
            logger.error("查询排班信息异常：",e);
            listener.error(e.getMessage());
        }
    }

    @ZxRateLimit(algorithmType = LimitAlgorithmEnum.RD_COUNTER,checkType = LimitTypeEnum.CUSTOM,limit = 1)
    @Override
    public List<Entry> getSchApptDoc(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"resourceCode"))){
            throw new BusException("资源编码：resourceCode不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"clinicDate"))){
            throw new BusException("日期：clinicDate不能为空");
        }else{
            Date clinicDate = DateUtils.strToDate(MapUtils.getString(paramMap,"clinicDate"), "yyyy-MM-dd");
            if(new Date().after(clinicDate)){
                paramMap.put("clinicDate",DateUtils.getDate());
            }
            //MapUtils.getString(paramMap,"")
        }
        Date clinicDate =DateUtils.strToDate(MapUtils.getString(paramMap,"clinicDate")+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date clinicDate2 = null;
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"clinicDate2"))){
           clinicDate2 =clinicDate;
            paramMap.put("clinicDate2",MapUtils.getString(paramMap,"clinicDate"));
        }else{
            clinicDate2 =DateUtils.strToDate(MapUtils.getString(paramMap,"clinicDate2")+" 59:59:59", "yyyy-MM-dd HH:mm:ss");
        }


        /*  信息科要求不限制查询天数，根据开始和结算时间查
        int day = DateUtils.getDateSpace(clinicDate,clinicDate2);
        if(day>16){
            clinicDate2 = DateUtils.getSpecifiedDay(clinicDate,14);
            paramMap.put("clinicDate2",DateUtils.dateToStr("yyyy-MM-dd",clinicDate2));
        }*/
        List<SchDoctorVo> doctorVoList = null;
        paramMap.put("channelSource", StringUtils.upperCase(ImplicitRulesResolver.getApiInfo().getSourceSign()));
        if("1".equals(MapUtils.getString(paramMap,"isDisClass"))){
            doctorVoList = zsphSchMapper.getSchApptDoctor(paramMap);
            doctorVoList=getShareSchDocInfos(doctorVoList,paramMap);
        }else{
            //获取的是一一天的，没区分午别（数量已经累加），下面过滤每个医生只留一条
            doctorVoList = zsphSchMapper.getSchApptDoctorClass(paramMap);
            doctorVoList=getShareSchDocInfos(doctorVoList,paramMap);
            doctorVoList = doctorVoList.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o->o.getCodeEmp()+ ";" + o.getDateWork()))), ArrayList::new)
            );
        }


        if(CollectionUtils.isEmpty(doctorVoList)){
            throw new BusException("没有数据");
        }
         Set<String> pkempSet = new HashSet<>();
        for (SchDoctorVo main : doctorVoList) {
            pkempSet.add(main.getPkEmp());
        }
        //根据信息得到号源
        if(doctorVoList != null && doctorVoList.size() > 0){
            //查人员照片
            Map<String, Map<String, Object>> empMap = DataBaseHelper.queryListToMap("select PK_EMP as key_,PHOTO from BD_OU_EMPLOYEE where pk_emp in ("+ CommonUtils.convertSetToSqlInPart(pkempSet, "pk_emp")+")");
            //服务不多一次先查
            Map<String, Map<String, Object>> srvMap = DataBaseHelper
                    .queryListToMap("select so.pk_schsrv as key_, sum(t.price) as price from sch_srv_ord so "
                            + "inner join bd_ord_item ot on so.pk_ord = ot.pk_ord "
                            + "inner join bd_item t on ot.pk_item=t.pk_item where so.del_flag = '0' and ot.del_flag = '0' " + "group by so.pk_schsrv");
            for (SchDoctorVo main : doctorVoList) {
                if (srvMap.get(main.getPkSchsrv()) != null&&srvMap.get(main.getPkSchsrv()).get("price")!=null) {
                    main.setPriceSchsrv(srvMap.get(main.getPkSchsrv()).get("price").toString());
                }
                if(empMap.get(main.getPkEmp()) != null&&empMap.get(main.getPkEmp()).get("photo")!= null){
                    main.setPhoto((Blob) empMap.get(main.getPkEmp()).get("photo"));
                }
            }
        }


        List<Entry> entryList = Lists.newArrayList();
        for (SchDoctorVo vo : doctorVoList) {
            Entry entry = new Entry();
            Response response = new Response();
            entry.setResponse(response);
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            SchDoctorOutcome outcome = new SchDoctorOutcome();
            outcome.setResourceType("Practitioner");
            outcome.setId(NHISUUID.getKeyId());
            outcome.setImplicitRules("RYXZ");
            outcome.setIdentifier(Lists.newArrayList(new Identifier("emplno", vo.getCodeEmp())));
            outcome.setName(new TextElement(vo.getNameEmp()));
            outcome.setDescription(vo.getIntroduction());
            outcome.setAddress(Lists.newArrayList(new TextElement(vo.getNamePlaceArea()==null?"":vo.getNamePlaceArea())));
            //这里不应该每次去获取二进制照片，设置空
            /*if(vo.getPhoto()!=null){
                Base64 base64 = new Base64();
                try {
                    Map<String, String> photo = Maps.newHashMap();
                    Blob blob = vo.getPhoto();
                    String content = base64.encodeBase64String(blob.getBytes(1L, (int)blob.length()));
                    if (null!=content) {
                        String zpContent = "data:image/jpg;base64,";
                        zpContent+=content;
                        photo.put("data", zpContent);
                        outcome.setPhoto(Lists.newArrayList(photo));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }*/

            Map<String, String> photo = Maps.newHashMap();
            photo.put("data", "");
            outcome.setPhoto(Lists.newArrayList(photo));
            outcome.setQualification(Lists.newArrayList(new CodeableConcept(Lists.newArrayList(new Coding(vo.getDtEmpsrvtype(), vo.getEmpsrvText())))));

            List<Map<String, Object>> extension = Lists.newArrayList();
            outcome.setExtension(extension);
            Map<String, Object> docVisitDate = Maps.newHashMap();
            docVisitDate.put("url", "docVisitDate");
            docVisitDate.put("valueDateTime", vo.getDateWork());//医生出诊时间，格式:yyyy-MM-dd hh:mi:ss
            extension.add(docVisitDate);
            Map<String, Object> research = Maps.newHashMap();
            research.put("url", "research");
            research.put("valueString", vo.getSpec());//研究方向与成果
            extension.add(research);

            Map<String, Object> specialtyId = Maps.newHashMap();
            specialtyId.put("url", "specialtyId");//专科识符
            Map<String, String> specialtyIdVal = Maps.newHashMap();
            specialtyIdVal.put("code", vo.getCodeDept());
            specialtyIdVal.put("display", vo.getNameDept());
            specialtyId.put("valueCoding", specialtyIdVal);
            extension.add(specialtyId);

            Map<String, Object> areaId = Maps.newHashMap();
            areaId.put("url", "areaId");//诊区
            Map<String, String> areaIdVal = Maps.newHashMap();
            areaIdVal.put("code", vo.getCodeDeptArea());
            areaIdVal.put("display", vo.getNameDeptArea());
            areaId.put("valueCoding", areaIdVal);
            extension.add(areaId);

            Map<String, Object> bookingNum = Maps.newHashMap();
            bookingNum.put("url", "bookingNum");
            bookingNum.put("valueInteger", vo.getCntAppt());//可预约数
            extension.add(bookingNum);

            Map<String, Object> pkSchres = Maps.newHashMap();
            pkSchres.put("url", "pkSchres");
            pkSchres.put("valueString", vo.getPkSchres()==null?"":vo.getPkSchres());//资源主键
            extension.add(pkSchres);

            Map<String, Object> serviceType = Maps.newHashMap();
            serviceType.put("url", "serviceType");
            serviceType.put("valueString", vo.getNameDateslot()==null?"":vo.getNameDateslot());//午别
            extension.add(serviceType);

            Map<String, Object> sortno = Maps.newHashMap();
            sortno.put("url", "sortno");
            sortno.put("valueString", vo.getSortno()==null?"":vo.getSortno());//分组序号
            extension.add(sortno);

            /** 诊金费用*/
            Map<String, Object> registerFee = Maps.newHashMap();
            registerFee.put("url", "registerFee");
            registerFee.put("valueString", vo.getPriceSchsrv()==null?"":vo.getPriceSchsrv());
            extension.add(registerFee);

            /** 特需费用*/
            Map<String, Object> specialFee = Maps.newHashMap();
            specialFee.put("url", "specialFee");
            specialFee.put("valueString", vo.getPrice()==null?"":vo.getPrice());
            extension.add(specialFee);

            /** 排序*/
            Map<String, Object> doctorSortNo = Maps.newHashMap();
            doctorSortNo.put("url", "doctorSortNo");
            doctorSortNo.put("valueString", vo.getDocterSortNo()==null?"":vo.getDocterSortNo());
            extension.add(doctorSortNo);

            response.setOutcome(BeanMap.create(outcome));
            entryList.add(entry);
        }
        return entryList;
    }

    @ZxRateLimit(algorithmType = LimitAlgorithmEnum.RD_COUNTER,checkType = LimitTypeEnum.CUSTOM,limit = 1)
    @Override
    public List<Entry> getSchApptTime(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"clinicDate"))){
            throw new BusException("日期：clinicDate不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"pkSchres"))){
            throw new BusException("资源主键：pkSchres不能为空");
        }
        List<SchVo> schVoList = zsphSchMapper.getSchApptTime(paramMap);
        if(CollectionUtils.isNotEmpty(schVoList)){
            List<Entry> entryList = new ArrayList<>();
            for(SchVo schVo:schVoList){
                String dateEnd = MapUtils.getString(paramMap,"clinicDate")+" "+schVo.getTimeEnd();
                Date clinicDate = DateUtils.strToDate(dateEnd, "yyyy-MM-dd HH:mm:ss");
                if(new Date().before(clinicDate)){
                    Entry entry = new Entry(new Response());
                    Response response = entry.getResponse();
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    SchOutcome outcome = new SchOutcome();
                    outcome.setResourceType("Schedule");
                    outcome.setIdentifier(Arrays.asList(new Identifier(null,schVo.getPkSch())));
                    outcome.setActive(true);
                    outcome.setServiceCategory(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding(schVo.getEuSrvtype(),TransfTool.getSrvText(schVo.getEuSrvtype()))))));
                    outcome.setServiceType(new TextElement(schVo.getNameDateslot()));
                    //科室、医生信息
                    outcome.setActor(Lists.newArrayList());
                    //医生信息
                    Outcome actor = new Outcome();
                    actor.setResourceType("Practitioner");
                    actor.setIdentifier(Arrays.asList(new Identifier(null,schVo.getCodeEmp())));
                    actor.setName(Arrays.asList(new TextElement(schVo.getNameEmp())));
                    outcome.getActor().add(actor);
                    //科室信息
                    Outcome location = new Outcome();
                    location.setResourceType("Location");
                    location.setIdentifier(Arrays.asList(new Identifier(null,schVo.getCodeDept())));
                    location.setName(schVo.getNameDept());
                    outcome.getActor().add(location);
                    outcome.setServiceType(new TextElement(schVo.getNameDateslot()));
                    List<Extension> extensionList = Lists.newArrayList();
                    Extension startTime = new Extension();
                    startTime.setUrl("startTime");
                    startTime.setValueString(schVo.getTimeBegin());
                    extensionList.add(startTime);
                    Extension endTime = new Extension();
                    endTime.setUrl("endTime");
                    endTime.setValueString(schVo.getTimeEnd());
                    extensionList.add(endTime);
                    outcome.setExtension(extensionList);
                    Extension timeStr = new Extension();
                    timeStr.setUrl("timeStr");
                    timeStr.setValueString(schVo.getTimeBegin()+"-"+schVo.getTimeEnd());
                    extensionList.add(timeStr);

                    Extension cntAppt = new Extension();
                    cntAppt.setUrl("appointNums");
                    cntAppt.setValueString(schVo.getCntAppt());
                    extensionList.add(cntAppt);
                    Extension treatarea = new Extension();
                    treatarea.setUrl("treatarea");
                    treatarea.setValueString(schVo.getNameDeptArea());
                    extensionList.add(treatarea);
                    Extension treatmentaddress = new Extension();
                    treatmentaddress.setUrl("treatmentaddress");
                    treatmentaddress.setValueString(schVo.getNamePlaceArea());
                    extensionList.add(treatmentaddress);
                    outcome.setExtension(extensionList);
                    response.setOutcome(BeanMap.create(outcome));
                    entryList.add(entry);
                }
            }
            if(entryList.size()>0){
                return entryList;
            }else{
                throw new BusException("没有数据");
            }
        } else {
            throw new BusException("没有数据");
        }
    }

    /**
     * 获取共享医生信息
     * @return
     */
    private List<SchDoctorVo> getShareSchDocInfos(List<SchDoctorVo> schDoctorVoList,Map<String,Object> paramMap){
        List<String> pkEmpList=new ArrayList<>();
        List<String> pkSchresList=new ArrayList<>();

        if(schDoctorVoList!=null && schDoctorVoList.size()>0) {
            schDoctorVoList.forEach(m -> {
                if (!pkSchresList.contains(m.getPkSchres())) {
                    pkSchresList.add(m.getPkSchres());
                }
            });
        }else{
            schDoctorVoList=new ArrayList<>();
        }
        paramMap.put("pkSchresList",pkSchresList);
        List<SchDoctorVo> tempDocList=new ArrayList<>();
        if("1".equals(MapUtils.getString(paramMap,"isDisClass"))) {
            tempDocList=zsphSchMapper.getSchApptDoctorShare(paramMap);
            if(tempDocList!=null && tempDocList.size()>0) {
                schDoctorVoList.addAll(tempDocList);
            }
            schDoctorVoList.sort(Comparator.comparing(SchDoctorVo::getSortno));
        }else{
            tempDocList=zsphSchMapper.getSchApptDoctorClassShare(paramMap);
            if(tempDocList!=null && tempDocList.size()>0) {
                schDoctorVoList.addAll(tempDocList);
            }
            schDoctorVoList.sort(Comparator.comparing(SchDoctorVo::getNameEmp));
        }
        return schDoctorVoList;
    }


    /**
     * 根据预约主键信息，更新sch_appt.pk_pi
     * @param param
     * @return
     */
    @Override
    public void updateSchApptByPkSchApp(String param) {
        Map<String,Object> requestBody = ZsphMsgUtils.fromJson(param, Map.class);
        Object entry = MapUtils.getObject(requestBody, "entry");
        if(entry ==null){
            throw new BusException("未传入entry");
        }
        List<Map<String,Object>> entryMapList = (List<Map<String,Object>>) entry;
        //获取到预约数据节点
        List<SchAppChanged> opApplyList = new ArrayList<>();
        entryMapList.stream().forEach(mp -> {
            Map<String, Object> resourceMap = null;
            if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                opApplyList.add(TransfTool.mapToBean(SchAppChanged.class, resourceMap));
            }
        });
        if(CollectionUtils.isEmpty(opApplyList)){
            throw new BusException("未传入resource数据节点");
        }
        //三方条传入
        SchAppChanged schAppChanged = opApplyList.get(0);
        if(StringUtils.isBlank(schAppChanged.getCodeOp())){
            throw new BusException("预约患者编码：codeOp不能为空");
        }
        if(StringUtils.isBlank(schAppChanged.getPkSchApp())){
            throw new BusException("预约主键：pkSchApp不能为空");
        }
        if(StringUtils.isBlank(schAppChanged.getCodeOpChanged())){
            throw new BusException("变更后的预约患者编码：codeOpChanged不能为空");
        }
        List<Map<String, Object>> listSch = DataBaseHelper.queryForList("select sch.PK_SCHAPPT,schpv.PK_PV from sch_appt sch inner join sch_appt_pv schpv on sch.PK_SCHAPPT = schpv.PK_SCHAPPT where sch.PK_SCHAPPT = ?",schAppChanged.getPkSchApp());
        if(listSch ==null ||listSch.size()==0){
            throw new BusException("根据预约主键为获取到预约记录");
        }
        Map<String, Object> mapPi = DataBaseHelper.queryForMap("select pk_pi from PI_MASTER where CODE_OP =? ",schAppChanged.getCodeOpChanged());
        if(mapPi ==null){
            throw new BusException("根据患者codeOpChanged编码未获取到患者信息！");
        }
        Map<String, Object> mapSch = listSch.get(0);
        if(StringUtils.isNotBlank(MapUtils.getString(mapSch,"pkPv"))){
            throw new BusException("该次预约已经接诊，不能更新预约记录！");
        }
        DataBaseHelper.update("update sch_appt set pk_pi = ? where PK_SCHAPPT = ?",new Object[] {MapUtils.getString(mapPi,"pkPi"),schAppChanged.getPkSchApp() });


    }
}
