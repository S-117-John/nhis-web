package com.zebone.nhis.ma.pub.platform.zsrm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.base.ou.vo.BdOuDeptType;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.scm.pub.BdFactory;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Code;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsphSchMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsrmBdMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.zsrm.service.ZsphBasicDataService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.QueryUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.ZsrmEmpAndJobParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.zebone.nhis.ma.pub.platform.zsrm.support.QueryUtils.getBdFactoryInfo;

@Service
public class ZsrmBasicDataServiceImpl implements ZsphBasicDataService {

    private static final Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");
    @Resource
    private ZsrmBdMapper zsrmBdMapper;
    @Resource
    private ZsphSchMapper zsphSchMapper;
    /**
     * ??????????????????
     * @param param
     */
    @Override
    public Response getBdOuOrg(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        PhResource phResource = ZsphMsgUtils.fromJson(param, PhResource.class);
        String codeOrg = null;
        //??????????????????
        if("orgcode".equals(phResource.getParameter().get(0).getName())){
            codeOrg = phResource.getParameter().get(0).getValueString();
            if(StringUtils.isBlank(codeOrg)){
                throw new BusException("????????????????????????????????????");
            }
        }else{
            throw new BusException("???????????????????????????????????????");
        }
        BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select * from bd_ou_org where code_org=?",BdOuOrg.class,codeOrg);
        if(!BeanUtils.isNotNull(bdOuOrg)){
            throw new BusException("??????????????????????????????");
        }
        //??????????????????????????????
        Outcome outcome = new Outcome();
        outcome.setResourceType("Organization");
        outcome.setId(requestBody.getId());
        outcome.setImplicitRules("getorganizationinfo");
        outcome.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203??","JGBH",null))) ,"code/orgcode",bdOuOrg.getCodeOrg())));
        if(EnumerateParameter.ZERO.equals(bdOuOrg.getFlagActive()) ||EnumerateParameter.ONE.equals(bdOuOrg.getDelFlag())){
            outcome.setActive(false);
        }else{
            outcome.setActive(true);
        }
        //1????????????2??????3??????4??????5????????????6??????????????????9????????????
        outcome.setType(Arrays.asList(new CodeableConcept(Arrays.asList(TransfTool.getOrgtype("code/orgtype",bdOuOrg.getDtHosptype())))));
        outcome.setName(bdOuOrg.getNameOrg());
        outcome.setAlias(Arrays.asList(bdOuOrg.getNameOrg()));

        //??????????????????
        List<Entry> entryList = new ArrayList<>();
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        response.setOutcome(BeanMap.create(outcome));
        return response;
    }

    /**
     * ??????????????????
     * @param param
     */
    @Override
    public Response getBdOuDept(String param) {
            //????????????
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            PhResource phResource = ZsphMsgUtils.fromJson(param, PhResource.class);
            String codeDept = null;
            //??????????????????
            if("deptno".equals(phResource.getParameter().get(0).getName())){
                codeDept = phResource.getParameter().get(0).getValueString();
                if(StringUtils.isBlank(codeDept)){
                    throw new BusException("????????????????????????????????????");
                }
            }else{
                throw new BusException("???????????????????????????????????????");
            }

            BdOuDept dept = DataBaseHelper.queryForBean("select * from bd_ou_dept where code_dept=?",BdOuDept.class,codeDept);
            if(!BeanUtils.isNotNull(dept)){
                throw new BusException("??????????????????????????????");
            }
            //???????????????????????????
            DeptOutLocation deptOutLocation = new DeptOutLocation();
            String id = NHISUUID.getKeyId();
            deptOutLocation.setResourceType("Location");
            deptOutLocation.setId(id);
            deptOutLocation.setImplicitRules("KSXZ");
            deptOutLocation.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","deptno",null))),"id/deptno",dept.getCodeDept())));
            deptOutLocation.setType(Arrays.asList(new CodeableConcept(Arrays.asList(TransfTool.getDeptType(dept.getCodeDept().substring(2,4))))));
            if(EnumerateParameter.ZERO.equals(dept.getFlagActive())){
                deptOutLocation.setStatus("inactive");//??????
            }else{
                deptOutLocation.setStatus("active");//??????
            }
            deptOutLocation.setName(dept.getNameDept());//????????????
            deptOutLocation.setAlias(Arrays.asList(dept.getShortname()));//????????????
            deptOutLocation.setDescription(dept.getDeptDesc());//????????????
            deptOutLocation.setTelecom(Arrays.asList(new Identifier("phone",dept.getTelnoDept())));

            //??????????????????
            deptOutLocation.setAddress(new Address(dept.getNamePlace(),"Address",null));
            //??????????????????--??????????????????
            Map<String,Object> deptPart = DataBaseHelper.queryForMap("select CODE_DEPT from bd_ou_dept where pk_dept = ? ", dept.getPkFather());
            deptOutLocation.setPartOf(new Address(null,"Location",Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","ParentCode",null))),"id/parentdeptno",TransfTool.getPropValueStr(deptPart,"codeDept")))));
            deptOutLocation.setExtension(Arrays.asList(new BdExtension("rank",String.valueOf(dept.getBednum()),null)));
            //??????????????????
            List<Entry> entryList = new ArrayList<>();
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setOutcome(BeanMap.create(deptOutLocation));
        return response;
    }

    /**
     * ???????????????????????????
     * @param param
     * @return
     */
    @Override
    public Response saveBdOuDept(String param) {
        //????????????
        DeptOutLocation deptOutLocation = ZsphMsgUtils.fromJson(param, DeptOutLocation.class);
        BdOuDept bdOuDept = new BdOuDept();
        for(Identifier identifier:deptOutLocation.getIdentifier()){
            if("id/deptno".equals(identifier.getSystem())){
                if(StringUtils.isNotEmpty(identifier.getValue())){
                    BdOuDept getbdOuDept=zsrmBdMapper.getZsrmBdouDept(identifier.getValue());
                    if(!BeanUtils.isNotNull(getbdOuDept)){
                        bdOuDept.setCodeDept(identifier.getValue());
                    }else{
                        bdOuDept=getbdOuDept;
                    }
                }else{
                    throw new BusException("??????????????????????????????");
                }
            }
        }
        //??????????????????
        if(CollectionUtils.isEmpty(deptOutLocation.getPartOf().getIdentifier()) && deptOutLocation.getPartOf().getIdentifier().size()>0){
            String fatherCode = deptOutLocation.getPartOf().getIdentifier().get(0).getValue();
            BdOuDept fatherDept = zsrmBdMapper.getZsrmBdouDept(fatherCode);
            if(BeanUtils.isNotNull(fatherDept)){
                bdOuDept.setPkFather(fatherDept.getPkDept());
            }
        }
        //????????????
        bdOuDept.setNameDept(String.valueOf(deptOutLocation.getName()));
        if("inactive".equals(deptOutLocation.getStatus())){
            bdOuDept.setFlagActive("0");//??????
        }else{
            bdOuDept.setFlagActive("1");//??????
        }
        //??????????????????
        if(BeanUtils.isNotNull(deptOutLocation.getAddress())){
            bdOuDept.setNamePlace(deptOutLocation.getAddress().getText());
        }
        //??????
        if(CollectionUtils.isNotEmpty(deptOutLocation.getAlias())){
            bdOuDept.setShortname(deptOutLocation.getAlias().get(0));
        }
        //??????
        bdOuDept.setDeptDesc(deptOutLocation.getDescription());
        Map<String, Object> orgMap = DataBaseHelper.queryForMap("select pk_org from bd_ou_org where code_org='0101' ",new Object[]{});
        bdOuDept.setPkOrg(StringUtils.isNotEmpty(bdOuDept.getPkOrg())?bdOuDept.getPkOrg():MapUtils.getString(orgMap,"pkOrg"));
        User user = new User();
        user.setPkOrg(bdOuDept.getPkOrg());
        UserContext.setUser(user);
        BdOuDept ret = saveDept(bdOuDept);
        if(ret==null){
            throw new BusException("?????????????????????????????????????????????");
        }

        //??????????????????
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        Outcome outcome = new Outcome();
        outcome.setResourceType("OperationOutcome");
        response.setStaus(ZsphConstant.RES_ERR_OTHER);
        Issue issue = new Issue();
        issue.setCode("informational");
        issue.setDiagnostics("??????");
        issue.setSeverity("informational");
        outcome.setIssue(Arrays.asList(issue));
        response.setOutcome(BeanMap.create(outcome));
        return response;
    }


    public BdOuDept saveDept(BdOuDept dept ){
        //???????????????????????????
        BdOuDept bd = saveDEPT(dept);
        List<BdOuDeptType> depts = dept.getDepts();
        if(CollectionUtils.isNotEmpty(depts)){
            for (BdOuDeptType bdOuDeptType : depts) {
                bdOuDeptType.setPkDept(dept.getPkDept());
                if(bdOuDeptType.getPkDepttype()==null){
                    DataBaseHelper.insertBean(bdOuDeptType);
                }else{
                    DataBaseHelper.updateBeanByPk(bdOuDeptType, false);
                }
            }
        }
        return bd;
    }

    public BdOuDept saveDEPT(BdOuDept dept){
        String delFlag = "0";
        if(dept.getPkDept() == null){
            int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where code_dept=? and pk_org like ?||'%' and DEL_FLAG = '0'",Integer.class,dept.getCodeDept(),dept.getPkOrg());
            int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where name_dept=? and pk_org like ?||'%' and DEL_FLAG = '0'",Integer.class,dept.getNameDept(),dept.getPkOrg());
            if(count_code == 0 && count_name == 0){
                dept.setCodeDept(codeDeptHandle(dept.getCodeDept()));
                dept.setModityTime(new Date());
                if(dept.getDelFlag() == null){
                    dept.setDelFlag(delFlag);
                }
                DataBaseHelper.insertBean(dept);
            }else{
                if(count_code != 0 && count_name == 0){
                    throw new BusException("?????????????????????");
                }else if(count_code == 0 && count_name != 0){
                    throw new BusException("?????????????????????");
                }else{
                    throw new BusException("?????????????????????????????????");
                }
            }
        }else{
            int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where code_dept=? and pk_org like ?||'%' and pk_dept != ? and DEL_FLAG = '0'",Integer.class,dept.getCodeDept(),dept.getPkOrg(),dept.getPkDept());
            int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
                    + "where name_dept=? and pk_org like ?||'%' and pk_dept != ? and DEL_FLAG = '0'",Integer.class,dept.getNameDept(),dept.getPkOrg(),dept.getPkDept());
            if(count_code == 0 && count_name == 0){
                ApplicationUtils.setDefaultValue(dept,false);
                dept.setModityTime(new Date());
                DataBaseHelper.updateBeanByPk(dept,false);
            }else{
                if(count_code != 0 && count_name == 0){
                    throw new BusException("?????????????????????");
                }else if(count_code == 0 && count_name != 0){
                    throw new BusException("?????????????????????");
                }else{
                    throw new BusException("?????????????????????????????????");
                }
            }
        }
        return dept;
    }
    public static String codeDeptHandle(String codeDept){
        String res = "";
        if(CommonUtils.isEmptyString(codeDept)){
            res =codeDept;
        }else{
            int numCount = Integer.parseInt(ApplicationUtils.getSysparam("BD0009",false,"????????????BD0009???????????????"));
            if(codeDept.length()<numCount){
                res = "00000000000000000000"+codeDept;
                res = res.substring(res.length()-numCount, res.length());
            }else{
                res = codeDept;
            }
        }
        return res;
    }

    /**
     * ??????????????????
     * @param param
     */
    @Override
    public Response getPractitioner(String param) {
        //????????????
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        PhResource phResource = ZsphMsgUtils.fromJson(param, PhResource.class);
        String codeEmp = null;
        //??????????????????
        if("emplno".equals(phResource.getParameter().get(0).getName())){
            codeEmp = phResource.getParameter().get(0).getValueString();
            if(StringUtils.isBlank(codeEmp)){
                throw new BusException("????????????????????????????????????");
            }
        }else{
            throw new BusException("???????????????????????????????????????");
        }

        Map<String, Object> empMap = DataBaseHelper.queryForMap("select * from bd_ou_employee where code_emp=?",codeEmp);
        if(MapUtils.isEmpty(empMap)){
            throw new BusException("??????"+codeEmp+"??????????????????");
        }
        List<Map<String, Object>> empJobsList = DataBaseHelper.queryForList("select * from bd_ou_empjob where code_emp=?",codeEmp);
        //???????????????????????????
        Practitioner person = new Practitioner();
        person.setResourceType("Practitioner");
        person.setId(requestBody.getId());
        //MDMRYXG:??????????????????;MDMRYXZ:??????????????????
        person.setImplicitRules("getemployeeinfo");//??????????????????
        //id/emplno id/idno ????????????????????????????????????????????????
        //emplno  ???????????????????????????   idno  ????????????????????????
        List<Identifier> identifierList = new ArrayList<>();
        identifierList.add(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","emplno",null))),"id/emplno",TransfTool.getPropValueStr(empMap,"codeEmp"),null,null));
        identifierList.add(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","idno",null))),"id/idno",TransfTool.getPropValueStr(empMap,"idno"),"official",null));
        person.setIdentifier(identifierList);
        //????????????true:??????, false?????????
        if(EnumerateParameter.ZERO.equals(TransfTool.getPropValueStr(empMap,"flagActive"))){
            person.setActive(false);
        }else{
            person.setActive(true);
        }
        person.setName(Arrays.asList(new Identifier(null,null,null,"official",TransfTool.getPropValueStr(empMap,"nameEmp"))));
        //phone:??????;fax:??????;email:??????;pager:?????????;url:????????????;sms:??????;other:??????
        //home:??????;work:??????;temp:??????;old:??????;mobile:????????????
        List<Identifier> TelecomList = new ArrayList<>();
        if(StringUtils.isNotEmpty(MapUtils.getString(empMap,"mobile"))){
            TelecomList.add(new Identifier(null,"phone",MapUtils.getString(empMap,"mobile"),"mobile",null));//??????-????????????
        }
        if(StringUtils.isNotEmpty(MapUtils.getString(empMap,"email"))){
            TelecomList.add(new Identifier(null,"email",MapUtils.getString(empMap,"email"),"work",null));
        }
        if(StringUtils.isNotEmpty(MapUtils.getString(empMap,"workphone"))){
            TelecomList.add(new Identifier(null,"phone",MapUtils.getString(empMap,"workphone"),"work",null));
        }
        if(StringUtils.isNotEmpty(MapUtils.getString(empMap,"homephone"))){
            TelecomList.add(new Identifier(null,"phone",MapUtils.getString(empMap,"homephone"),"home",null));
        }
        person.setTelecom(TelecomList);
        //???????????????home/work/birth???
        person.setAddress(Arrays.asList(new Identifiers("physical",null,null,"work",TransfTool.getPropValueStr(empMap,"addr"))));
        //male:??????;female:??????;other:??????;unknown:??????????????????
        person.setGender(TransfTool.getSex(TransfTool.getPropValueStr(empMap,"dt_sex")));
        Date birthday = TransfTool.getPropValueDates(empMap, "birthday");
        person.setBirthDate(DateUtils.dateToStr("yyyy-MM-dd",birthday==null?new Date():birthday));//????????????

        List<BdExtension> extension = new ArrayList<>();
        //code/loginAcount??????????????????
        List<Map<String,Object>> codeUserList = DataBaseHelper.queryForList("select pk_user,code_user from bd_ou_user where pk_emp=?", TransfTool.getPropValueStr(empMap,"pkEmp"));
        //??????
        List<Map<String,Object>> codeRoleList = new ArrayList<>();
        if(codeUserList.size()>0){
            codeRoleList = DataBaseHelper.queryForList("select role.name_role,role.code_role from bd_ou_role role left join bd_ou_user_role userRolr on userRolr.pk_role=role.pk_role where userRolr.del_flag='0' and userRolr.pk_user=?", TransfTool.getPropValueStr(codeUserList.get(0),"pkUser"));
            extension.add(new BdExtension("code/loginAcount",TransfTool.getPropValueStr(codeUserList.get(0),"codeUser"),null));
        }
        List<Condition> qualificationList = new ArrayList<>();
        List<BdContained> containedList = new ArrayList<>();
        for(Map<String,Object> empJobsMap:empJobsList){
            //role:??????|job:??????|title:??????|partTimeJobs:????????????
            String jobCode =TransfTool.getPropValueStr(empJobsMap,"jobname");
            if(StringUtils.isNotBlank(jobCode)){
                qualificationList.add(new Condition(null,new CodeableConcept(Arrays.asList(new Coding("job",jobCode,TransfTool.getBdDefdocName("010301",jobCode))))));
            }
            String dtEmpjob =TransfTool.getPropValueStr(empJobsMap,"dtEmpjob");
            if(StringUtils.isNotBlank(dtEmpjob)){
                qualificationList.add(new Condition(null,new CodeableConcept(Arrays.asList(new Coding("title",dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob))))));
            }
            //code/dept:?????????????????????
            Map<String,Object> deptMap =DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?",empJobsMap.get("pkDept"));
            //organization:??????????????????
            Map<String,Object> orgMap =DataBaseHelper.queryForMap("select code_org,name_org from bd_ou_org where pk_org = ?",empJobsMap.get("pkOrg"));
            //List<BdExtension> codBdExtenList = new ArrayList<>();
            List<BdCodeableConcept> bdCodeableConcepts = new ArrayList<>();
            //?????????
            if(codeRoleList.size()>0){
                bdCodeableConcepts.add(new BdCodeableConcept("roleLevel",Arrays.asList(new BdCoding(TransfTool.getPropValueStr(codeRoleList.get(0),"code_role"),TransfTool.getPropValueStr(codeRoleList.get(0),"nameRole")))));
            }
            //??????????????????
            bdCodeableConcepts.add(new BdCodeableConcept("admPosition",Arrays.asList(new BdCoding(jobCode,TransfTool.getBdDefdocName("010301",jobCode)))));
            //????????????
            extension.add(new BdExtension("code/dept",null,new BdCodeableConcept(Arrays.asList(new BdCoding(TransfTool.getPropValueStr(deptMap,"codeDept"),TransfTool.getPropValueStr(deptMap,"nameDept"))))));
            //????????????
            extension.add(new BdExtension("organization",null,new BdCodeableConcept(Arrays.asList(new BdCoding(TransfTool.getPropValueStr(orgMap,"codeOrg"),TransfTool.getPropValueStr(orgMap,"nameOrg"))))));
            //???????????? ????????????duty_date???????????????date_left
            extension.add(new BdExtension("period",new ValuePeriod(TransfTool.getPropValueDates(empJobsMap, "dutyDate"),TransfTool.getPropValueDates(empJobsMap, "dateLeft"))));
            //????????????
            extension.add(new BdExtension("busPost",new Coding(null,dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob))));
            //?????????????????????
            Boolean isTrainee="09".equals(TransfTool.getPropValueStr(empMap, "dtEmpsrvtype"));
            extension.add(new BdExtension("isTrainee",isTrainee));
            //??????????????????-date_left
            if(StringUtils.isNoneEmpty(TransfTool.getPropValueStr(empJobsMap, "dateLeft"))){
                DateTime dt = DateTime.now();
                Boolean isOnJob = TransfTool.getPropValueStr(empJobsMap, "dateLeft").compareTo(dt.toString("YYYY-MM-dd"))>0;
                extension.add(new BdExtension("isOnJob",isOnJob));
            }else{
                extension.add(new BdExtension("isOnJob",true));
            }
            //??????????????????
            extension.add(new BdExtension("entryTime",TransfTool.getPropValueDates(empJobsMap, "dutyDate")));
            List<BdCodeableConcept>  specialtyList= Arrays.asList(new BdCodeableConcept(Arrays.asList(new BdCoding(dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob)))));
            containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,TransfTool.getPropValueStr(deptMap,"codeDept"))),TransfTool.getPropValueStr(deptMap,"nameDept"))),specialtyList,NHISUUID.getKeyId()));
            if (containedList.size()>0){
                containedList.get(0).setId(null);
            }
        }
        person.setQualification(qualificationList);//??????
        //??????????????????
        person.setExtension(extension);
        //????????????????????????
        person.setContained(containedList);

            //??????????????????
            List<Entry> entryList = new ArrayList<>();
            Entry entry = new Entry(new Response());
            Response response = entry.getResponse();
            response.setStaus(ZsphConstant.RES_SUS_OTHER);
            response.setOutcome(BeanMap.create(person));
        return response;
    }

    /**
     * ???????????????????????????
     * @param param
     * @return
     */
    @Override
    public Response savePractitioner(String param) {
        //????????????
        Practitioner person = JsonUtil.readValue(param,Practitioner.class);
        ZsrmEmpAndJobParam EmpAndJobParam = new ZsrmEmpAndJobParam();
        BdOuEmployee emp = new BdOuEmployee();
        for(Identifier identifier:person.getIdentifier()){
            if("id/emplno".equals(identifier.getSystem())){
                if(StringUtils.isNotEmpty(identifier.getValue())){
                    BdOuEmployee getemp = DataBaseHelper.queryForBean("select * from bd_ou_employee where code_emp=?",BdOuEmployee.class, identifier.getValue());
                    if(!BeanUtils.isNotNull(getemp)){
                        emp.setCodeEmp(identifier.getValue());
                    }else{
                        emp=getemp;
                    }
                }else{
                    throw new BusException("????????????????????????????????????");
                }
            }else if("id/idno".equals(identifier.getSystem())){
                if(StringUtils.isNotEmpty(identifier.getValue())){
                    emp.setIdno(identifier.getValue());
                    emp.setDtIdentype("99");
                }
            }
        }
        //????????????
        if(person.getActive()){
            emp.setFlagActive("1");
        }else{
            emp.setFlagActive("0");
        }
        //???????????????
        if(person.getName().size()>0){
            for(Identifier identifier:person.getName()){
                if("official".equals(identifier.getUse())){
                    emp.setNameEmp(identifier.getText());
                }
            }
        }
        //??????????????????
        if(person.getTelecom().size()>0){
            for(Identifier identifier:person.getName()){
                if("work".equals(identifier.getUse()) && "email".equals(identifier.getSystem())){
                    emp.setEmail(identifier.getValue());
                }
                if("mobile".equals(identifier.getUse()) && "phone".equals(identifier.getSystem())){
                    emp.setMobile(identifier.getValue());
                }
                if("work".equals(identifier.getUse()) && "phone".equals(identifier.getSystem())){
                    emp.setWorkphone(identifier.getValue());
                }
                if("home".equals(identifier.getUse()) && "phone".equals(identifier.getSystem())){
                    emp.setHomephone(identifier.getValue());
                }
            }
        }
        //??????????????????
        emp.setBirthday(DateUtils.strToDate(person.getBirthDate(),"yyyy-MM-dd"));
        //????????????
        if(!"unknown".equals(person.getGender()) && !"other".equals(person.getGender()) ){
            emp.setDtSex("male".equals(person.getGender())?"02":"03");
        }else{
            emp.setDtSex("04");
        }
        EmpAndJobParam.setEmp(emp);
        String Jobname = null;
        List<Condition>  quaList = person.getQualification();
        if(person.getQualification().size()>0){
            List<Coding> codList = person.getQualification().get(0).getCode().getCoding();
            if(codList.size()>0){
                //????????????????????????
                Jobname=codList.get(0).getCode();
            }
        }
        Map<String, Object> orgMap = DataBaseHelper.queryForMap("select pk_org from bd_ou_org where code_org='0101' ",new Object[]{});
        person.setHospitalId(MapUtils.getString(orgMap,"pkOrg"));
        //??????????????????????????????
        List<BdOuEmpjob> empJobsList = new ArrayList<>();
        if(person.getExtension().size()>0){
            for(BdExtension bdExt:person.getExtension()){
                if("code/dept".equals(bdExt.getUrl())){
                    if(bdExt.getValueCoding() !=null){
                        String codeDept = bdExt.getValueCoding().getCode();//??????????????????
                        BdOuDept getbdOuDept=zsrmBdMapper.getZsrmBdouDept(codeDept);
                        if(BeanUtils.isNotNull(getbdOuDept)){
                            BdOuEmpjob ouEmpjob = new BdOuEmpjob();
                            ouEmpjob.setPkEmp(emp.getPkEmp());
                            ouEmpjob.setCodeEmp(emp.getCodeEmp());
                            ouEmpjob.setDtEmptype("1");//????????????
                            ouEmpjob.setPkDept(getbdOuDept.getPkDept());//????????????
                            ouEmpjob.setPkOrg(person.getHospitalId());
                            ouEmpjob.setDelFlag("0");
                            ouEmpjob.setJobname(Jobname);//????????????
                            empJobsList.add(ouEmpjob);
                        }
                    }else{
                        throw new BusException("??????????????????????????????");
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(empJobsList)){
            EmpAndJobParam.setEmpJobs(empJobsList);
        }
        User user = new User();
        user.setPkOrg(person.getHospitalId());
        UserContext.setUser(user);
        EmpAndJobParam.setFlagSendMsg("0");//?????????????????????
        ApplicationUtils aUtils = new ApplicationUtils();
        ResponseJson ret = aUtils.execService("base", "EmpService", "saveEmpAndJobInfos",EmpAndJobParam, user);
        if(0 != ret.getStatus()){
            throw new BusException(ret.getDesc());
        }else if(StringUtils.isNotEmpty(emp.getPkEmp())){
            //????????????????????????????????????????????????
            updateSchResource(EmpAndJobParam.getEmpJobs().get(0).getJobname(),emp.getPkEmp());
        }
        //??????????????????
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        Outcome outcome = new Outcome();
        outcome.setResourceType("OperationOutcome");
        response.setStaus(ZsphConstant.RES_ERR_OTHER);
        Issue issue = new Issue();
        issue.setCode("informational");
        issue.setDiagnostics("??????");
        issue.setSeverity("informational");
        outcome.setIssue(Arrays.asList(issue));
        response.setOutcome(BeanMap.create(outcome));
        return response;
    }

    //???????????????????????????????????????????????????
    public void updateSchResource(String code,String pkEmp){
        Map<String, Object> schsrvMap = DataBaseHelper.queryForMap("select srv.pk_schsrv From bd_defdoc def Inner Join sch_srv srv On def.val_attr=srv.code And srv.del_flag='0' Where def.code_defdoclist='010301' And def.del_flag='0' And Def.code=? ",code);
        if(MapUtils.isNotEmpty(schsrvMap)){
            schsrvMap.put("pkEmp",pkEmp);
            List<SchResource> resoList = zsphSchMapper.getResource(schsrvMap);
            if(resoList.size()>0){
                zsphSchMapper.updateSchResourceList(resoList);
            }
        }
    }
    /**
     * ????????????????????????
     * @param param
     */
    @Override
    public Response getBdTermDiag(String param) {
            //????????????
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            PhResource phResource = ZsphMsgUtils.fromJson(param, PhResource.class);
            String diagcode = null;
            //??????????????????
            if("operationno".equals(phResource.getParameter().get(0).getName())){
                diagcode = phResource.getParameter().get(0).getValueString();
                if(StringUtils.isBlank(diagcode)){
                    throw new BusException("????????????????????????????????????");
                }
            }else{
                throw new BusException("???????????????????????????????????????");
            }
            BdTermDiag diag = DataBaseHelper.queryForBean("select * from bd_term_diag where diagcode=?",BdTermDiag.class,diagcode);
            CodeSystem codeSystem = new CodeSystem();
            codeSystem.setResourceType("CodeSystem");
            codeSystem.setId(requestBody.getId());
            boolean valueBoolean=true;
            if(EnumerateParameter.ONE.equals(diag.getFlagStop())){
               valueBoolean=false;
            }
            List<Property> propertyList = new ArrayList<>();
            //???????????????isactive???????????????
            propertyList.add(new Property("isactive",valueBoolean));
            //????????????ICD10???ICD9
            String dtEmpjobName =TransfTool.getPropValueStr(DataBaseHelper.queryForMap("select name from bd_defdoc where DEL_FLAG = '0' and code_defdoclist='060000' and code=?",diag.getDtSystem()),"name");
            codeSystem.setIdentifier(Arrays.asList(new Identifier(null,dtEmpjobName==null?"":dtEmpjobName)));
            if("02".equals(diag.getDtDiagtype())){
                codeSystem.setImplicitRules("ICD9XZ");
                //????????????
                if(StringUtils.isNotBlank(diag.getDtOplevel())){
                    Map<String,Object> levelMap = DataBaseHelper.queryForMap("select BA_CODE,NAME from bd_defdoc where DEL_FLAG = '0' and code_defdoclist='030305' and code=?",diag.getDtOplevel());
                    propertyList.add(new Property("level",new Coding(null,TransfTool.getPropValueStr(levelMap,"baCode"),TransfTool.getPropValueStr(levelMap,"name"))));
                }
                //??????????????????
                propertyList.add(new Property("icd9Code",new Coding(null,diag.getDiagcode(),diag.getDiagname())));
                //????????????
                propertyList.add(new Property("createDate",new Date()));
                //????????????
                propertyList.add(new Property("lastUpdateDate",new Date()));
                codeSystem.setConcept(Arrays.asList(new Concept(diag.getDiagcode(),diag.getDiagname(),propertyList)));
            }else{
                //MDMZDXG:??????????????????;MDMZDXZ:??????????????????|
                codeSystem.setImplicitRules("DiagXZ");
                //????????????
                if(StringUtils.isNotBlank(diag.getPkFather())){
                    Map<String,Object>  fathDiagMap= DataBaseHelper.queryForMap("select * from BD_TERM_DIAG where PK_DIAG=?",diag.getPkFather());
                    propertyList.add(new Property("parentcode",new Coding(null,TransfTool.getPropValueStr(fathDiagMap,"diagcode"),TransfTool.getPropValueStr(fathDiagMap,"diagname"))));
                }
                //infectflag??????????????????
                if("01".equals(diag.getDtDiagtype())){
                    propertyList.add(new Property("infectflag",true));
                }else{
                    propertyList.add(new Property("infectflag",false));
                }
                //cancerflag???????????????
                propertyList.add(new Property("cancerflag",false));
                //genderflag:?????????????????????:1:??? 2:??? 3?????????--NHIS???????????????
                propertyList.add(new Property("genderflag",null,"3"));
                //chronicflag?????????????????????
                propertyList.add(new Property("chronicflag",!"".equals(diag.getDtCodePh())));
                //??????????????????
                propertyList.add(new Property("icd10Code",new Coding(null,diag.getDiagcode(),diag.getDiagname())));
                codeSystem.setConcept(Arrays.asList(new Concept(diag.getDiagcode(),diag.getDiagname(),propertyList)));
            }


        //??????????????????
        List<Entry> entryList = new ArrayList<>();
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        response.setOutcome(BeanMap.create(codeSystem));
        return response;
    }

    /**
     * ??????????????????????????????
     * @param param
     */
    @Override
    public void saveBdTermDiag(String param) {
        CodeSystem codeSystem = JsonUtil.readValue(param,CodeSystem.class);

        Boolean flagStop = true;
        String diagCode = null,diagNmae = null,diagCodeFat = null;

        List<Property> propertyList = codeSystem.getConcept().get(0).getProperty();
        for(Property property :propertyList){
            if("isactive".equals(property.getCode())){//????????????
                flagStop = property.getValueBoolean();
            }
            if("parentcode".equals(property.getCode())){//????????????
                diagCodeFat = property.getValueCoding().getCode();//??????????????????
            }
            if("icd10Code".equals(property.getCode())){
                diagCode = property.getValueCoding().getCode();//??????????????????
                diagNmae = property.getValueCoding().getDisplay();//??????????????????
            }
        }
        BdTermDiag diag = new BdTermDiag();
        if(StringUtils.isNotBlank(diagCode)){
            diag = DataBaseHelper.queryForBean("select * from BD_TERM_DIAG where diagcode = ? ", BdTermDiag.class, new Object[]{diagCode});
            BdTermDiag diagFat = new BdTermDiag();
            if(StringUtils.isNotBlank(diagCodeFat)){
                diagFat = DataBaseHelper.queryForBean("select pk_diag from BD_TERM_DIAG where diagcode = ? ", BdTermDiag.class, new Object[]{diagCodeFat});
                diag.setPkFather(diagFat.getPkDiag());
            }
            if(!BeanUtils.isNotNull(diag)){
                diag = new BdTermDiag();
                diag.setDtSystem("00");//ICD10
                diag.setDiagcode(diagCode);
                diag.setDiagname(diagNmae);
                diag.setFlagStop(flagStop?"0":"1");
                diag.setDtDiagtype("01");//??????
                DataBaseHelper.insertBean(diag);
            }else{
                diag.setDiagname(diagNmae);
                diag.setFlagStop(flagStop?"0":"1");
                DataBaseHelper.updateBeanByPk(diag,false);
            }
        }

        List<Concept> conceptList = codeSystem.getConcept();
        if(conceptList.size()>0){
            String codeCd = codeSystem.getConcept().get(0).getCode();//????????????
            String nameCd = codeSystem.getConcept().get(0).getDisplay();//????????????
            BdCndiag cndiag = DataBaseHelper.queryForBean("select * from BD_CNDIAG where code_cd = ? ", BdCndiag.class, new Object[]{codeCd});
            if(!BeanUtils.isNotNull(cndiag)){
                BdCndiag cndiags = new BdCndiag();
                cndiags.setPkOrg("~                               ");
                cndiags.setCodeCd(codeCd);//????????????
                cndiags.setNameCd(nameCd);//????????????
                cndiags.setPkDiag(diag.getPkDiag());
                cndiags.setCodeIcd(diagCode);
                cndiags.setDelFlag(flagStop?"0":"1");
                DataBaseHelper.insertBean(cndiags);
            }else{
                cndiag.setNameCd(nameCd);//????????????
                cndiag.setPkDiag(diag.getPkDiag());
                cndiag.setCodeIcd(diagCode);
                cndiag.setDelFlag(flagStop?"0":"1");
                DataBaseHelper.updateBeanByPk(cndiag,false);
            }
        }
    }

    /**
     * ??????????????????????????????
     * @param param
     */
    @Override
    public Response getChargeBdIteminfo(String param) {
        //????????????
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        PhResource phResource = ZsphMsgUtils.fromJson(param, PhResource.class);
        String code = null;
        //????????????????????????
        if("chargeitemno".equals(phResource.getParameter().get(0).getName())){
            code = phResource.getParameter().get(0).getValueString();
            if(StringUtils.isBlank(code)){
                throw new BusException("????????????????????????????????????");
            }
        }else{
            throw new BusException("???????????????????????????????????????");
        }
        Map<String,Object> itemMap = DataBaseHelper.queryForMap("select * from bd_item where code=?",code);
        ChargeItem chargeItem = new ChargeItem();
        chargeItem.setResourceType("ChargeItem");
        chargeItem.setImplicitRules("getchargeiteminfo");
        chargeItem.setId(requestBody.getId());
        chargeItem.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","SFBH",null))),"id/chargeitemno",TransfTool.getPropValueStr(itemMap,"code"),"official",null)));
        //???????????????0?????????1??????
        if(EnumerateParameter.ZERO.equals(TransfTool.getPropValueStr(itemMap,"flagActive")) || EnumerateParameter.ONE.equals(TransfTool.getPropValueStr(itemMap,"delFlag"))){
            chargeItem.setStatus(EnumerateParameter.ZERO);
        }else{
            chargeItem.setStatus(EnumerateParameter.ONE);
        }
        //???????????????
        chargeItem.setCode(new CodeableConcept(Arrays.asList(new Coding("feeitem",TransfTool.getPropValueStr(itemMap,"code"),TransfTool.getPropValueStr(itemMap,"name")))));
        List<Map<String, Object>> itemPriceList = DataBaseHelper.queryForList("select * from bd_item_price where flag_stop='0' and pk_item=? ", TransfTool.getPropValueStr(itemMap,"pkItem"));
        //????????????
        chargeItem.setOccurrencePeriod(new ValuePeriod(TransfTool.getPropValueDates(itemPriceList.get(0),"dateBegin"),TransfTool.getPropValueDates(itemPriceList.get(0),"dateEnd")));
        //????????????????????????
        chargeItem.setEnteredDate(TransfTool.getPropValueDates(itemMap, "createTime"));
        List<BdExtension> extensionList = new ArrayList<>();
        //????????????
        Map<String, Object> unitMap = DataBaseHelper.queryForMap("select name from bd_unit where pk_unit=? ", TransfTool.getPropValueStr(itemMap, "pkUnit"));
        extensionList.add(new BdExtension("unit", TransfTool.getPropValueStr(unitMap, "name"), null));
        //??????????????????
        extensionList.add(new BdExtension("costprice", new BigDecimal(TransfTool.getPropValueStr(itemPriceList.get(0), "price"))));
        //????????????
        extensionList.add(new BdExtension("unitprice", new BigDecimal(TransfTool.getPropValueStr(itemPriceList.get(0), "price"))));
        //?????????1
        extensionList.add(new BdExtension("inputcode1", "", null));
        //?????????2
        extensionList.add(new BdExtension("inputcode2", "", null));
        //????????????
        extensionList.add(new BdExtension("ordersign", false));
        //????????????0?????????1?????????2??????
        Map<String, Object> itemHpMap = DataBaseHelper.queryForMap("select * from bd_item_hp where pk_item=? ", TransfTool.getPropValueStr(itemMap, "pkItem"));
        if (EnumerateParameter.ZERO.equals(TransfTool.getPropValueStr(itemHpMap, "euLevel"))) {
            extensionList.add(new BdExtension("code/reimbursementcode", null, null, null, null, null, null, EnumerateParameter.ONE));
        } else if (EnumerateParameter.ONE.equals(TransfTool.getPropValueStr(itemHpMap, "euLevel"))) {
            extensionList.add(new BdExtension("code/reimbursementcode", null, null, null, null, null, null, EnumerateParameter.TWO));
        } else {
            extensionList.add(new BdExtension("code/reimbursementcode", null, null, null, null, null, null, EnumerateParameter.ZERO));
        }
        //??????????????????0?????????1?????????2??????
        extensionList.add(new BdExtension("inoutflag",null,null,null,null,null,null,EnumerateParameter.ZERO));
        //??????????????????:????????????
        extensionList.add(new BdExtension("code/defaultexec",null,null,null,null,null,null,""));
        //????????????
        extensionList.add(new BdExtension("code/pricecode", null, null, null, null, null, null, ""));
        //????????????
        if (!"01".equals(TransfTool.getPropValueStr(itemMap, "dtSanitype"))) {
            extensionList.add(new BdExtension("importflag", true));
        } else {
            extensionList.add(new BdExtension("importflag", false));
        }
        //????????????1
        extensionList.add(new BdExtension("extend1", "", null));
        //????????????2
        extensionList.add(new BdExtension("extend2", "", null));
        //????????????3
        extensionList.add(new BdExtension("extend3", "", null));
        //??????
        extensionList.add(new BdExtension("remark", TransfTool.getPropValueStr(itemMap, "note"), null));
        //????????????
        extensionList.add(new BdExtension("code/chargeitemclass", null, null, null, null, null, null, TransfTool.getPropValueStr(itemMap, "dtItemtype")));
        //??????????????????
        extensionList.add(new BdExtension("selfexpenditure", "", null));
        //??????????????????
        extensionList.add(new BdExtension("code/nationalitem", TransfTool.getPropValueStr(itemMap, "codeStd"), null));
        //??????????????????
        extensionList.add(new BdExtension("code/nationalitemdivision", "", null));
        //????????????
        extensionList.add(new BdExtension("additionalmaterials",false));
        //??????????????????
        if("0701".equals(TransfTool.getPropValueStr(itemMap, "dtItemtype"))){
            extensionList.add(new BdExtension("preciousconsumables",true));
        }else{
            extensionList.add(new BdExtension("preciousconsumables",false));
        }
        //????????????
        extensionList.add(new BdExtension("itemconnotation",TransfTool.getPropValueStr(itemMap, "descItem"),null));
        //??????????????????
        if("04".equals(TransfTool.getPropValueStr(itemMap, "dtItemtype").toString().substring(0,2))){
            extensionList.add(new BdExtension("checkitemflag",true));
        }else{
            extensionList.add(new BdExtension("checkitemflag",false));
        }
        //???????????????
        extensionList.add(new BdExtension("code/mainitem",null,null,null,null,null,null,""));
        //????????????
        if("07".equals(TransfTool.getPropValueStr(itemMap, "dtItemtype").toString().substring(0,2))){
            extensionList.add(new BdExtension("consumablesflag",true));
        }else{
            extensionList.add(new BdExtension("consumablesflag",false));
        }

        Map<String, Object> cateMap = DataBaseHelper.queryForMap("select code from bd_itemcate where pk_itemcate=? ", TransfTool.getPropValueStr(itemMap, "pkItemcate"));
        //??????????????????
        extensionList.add(new BdExtension("code/outinvoiceitem",null,null,null,null,null,null,TransfTool.getPropValueStr(cateMap, "code")));
        //??????????????????
        extensionList.add(new BdExtension("code/outaccountitem",null,null,null,null,null,null,TransfTool.getPropValueStr(cateMap, "code")));
        //??????????????????
        extensionList.add(new BdExtension("code/inputinvoiceitem",null,null,null,null,null,null,TransfTool.getPropValueStr(cateMap, "code")));
        //??????????????????
        extensionList.add(new BdExtension("code/inputaccountitem",null,null,null,null,null,null,TransfTool.getPropValueStr(cateMap, "code")));
        //??????????????????
        Map<String, Object> auditMap = DataBaseHelper.queryForMap("select code from bd_audit where pk_audit=? ", TransfTool.getPropValueStr(itemMap, "pkAudit"));
        extensionList.add(new BdExtension("code/financialaccountitem", null, null, null, null, null, null, TransfTool.getPropValueStr(auditMap, "code")));
        //??????????????????
        extensionList.add(new BdExtension("code/medicalitem", null, null, null, null, null, null, TransfTool.getPropValueStr(itemMap, "dtChcate")));
        chargeItem.setExtension(extensionList);
        System.out.println(JSONObject.toJSONString(chargeItem));
        //??????????????????
        List<Entry> entryList = new ArrayList<>();
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        response.setOutcome((Map<String, Object>) ZsphMsgUtils.beanToMap(chargeItem));
        /**
         * ???????????????map???  map???????????????????????????????????????long??????  ??????????????????
         */
        response.getOutcome().put("enteredDate", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", chargeItem.getEnteredDate()));
        return response;
    }

    /**
     * ??????????????????????????????
     * @param param
     */
    @Override
    public Response getChargeBdOrdinfo(String param) {
        //????????????
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        PhResource phResource = ZsphMsgUtils.fromJson(param, PhResource.class);
        String code = null;
        //????????????????????????
        for(Parameter parameter:phResource.getParameter()){
            if("substanceno".equals(parameter.getName())){
                code = parameter.getValueString();
                if(StringUtils.isNotBlank(code)){
                    break;
                }
            }
        }
        BdOrd bdOrd = DataBaseHelper.queryForBean("select * from bd_ord where code=?", BdOrd.class,code);
        if(!BeanUtils.isNotNull(bdOrd)){
            throw new BusException("??????"+code+"??????????????????????????????");
        }
        bdOrdSubstance substance = new bdOrdSubstance();
        substance.setResourceType("Substance");
        substance.setId(requestBody.getId());
        substance.setImplicitRules("getsubstanceinfo");
        substance.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","ZLBH",null))),"id/substanceno",bdOrd.getCode(),"",null)));
        //???????????????0?????????1??????
        if(EnumerateParameter.ONE.equals(bdOrd.getFlagActive()) || EnumerateParameter.ONE.equals(bdOrd.getDelFlag())){
            substance.setStatus(EnumerateParameter.ONE);
        }else{
            substance.setStatus(EnumerateParameter.ZERO);
        }
        //?????????????????????
        substance.setCode(new CodeableConcept(Arrays.asList(new Coding("code/substancecode",bdOrd.getCode(),bdOrd.getName()))));
        //??????????????????????????????????????????
        substance.setCategory(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("code/substancecategory",bdOrd.getCodeOrdtype(), MapUtils.getString(DataBaseHelper.queryForMap("select name from bd_ordtype where code=? and del_flag='0'",bdOrd.getCodeOrdtype()),"name"))))));
        //???????????????????????? ???????????????????????????
        substance.setSubstanceCodeableConcept(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("code/ordercode",bdOrd.getCode(),bdOrd.getName())))));
        List<BdExtension> exceptionList = new ArrayList<>();
        //????????????
        exceptionList.add(new BdExtension("ordercost", "", null));
        //?????????????????????????????????
        List<Map<String,Object>> ordDeptList = DataBaseHelper.queryForList("select dept.name_dept,dept.code_dept from bd_ord_dept ordDept left join bd_ou_dept dept on dept.pk_dept=ordDept.pk_dept  where ordDept.flag_def='1' and ordDept.pk_ord=? and ordDept.pk_org=?",bdOrd.getPkOrd(),phResource.getHospitalId());
        if(ordDeptList.size()>0){
            exceptionList.add(new BdExtension("code/defaultexec",null,null,null,null,null,null,TransfTool.getPropValueStr(ordDeptList.get(0),"codeDept")));
        }
         //????????????
        Map<String, Object> ordRisMap = DataBaseHelper.queryForMap("select dt_body from bd_ord_ris where pk_ord=?", bdOrd.getPkOrd());
        exceptionList.add(new BdExtension("bodysite",TransfTool.getBdDefdocName("030101",TransfTool.getPropValueStr(ordRisMap,"dtBody")),null));
        //??????
        exceptionList.add(new BdExtension("position", "", null));
        //????????????
        exceptionList.add(new BdExtension("method", "", null));
        //????????????
        exceptionList.add(new BdExtension("media", "", null));
        //????????????:0-????????????|1-????????????|2-?????????
        if(EnumerateParameter.ONE.equals(bdOrd.getEuSex())){
            exceptionList.add(new BdExtension("genderconstraint",null,null,null,null,null,null,EnumerateParameter.ZERO));
        } else if (EnumerateParameter.TWO.equals(bdOrd.getEuSex())) {
            exceptionList.add(new BdExtension("genderconstraint", null, null, null, null, null, null, EnumerateParameter.ONE));
        } else {
            exceptionList.add(new BdExtension("genderconstraint", null, null, null, null, null, null, EnumerateParameter.TWO));
        }
        //????????????:0-?????????|1-?????????|2-?????????|3-?????????|4-?????????

        exceptionList.add(new BdExtension("scenario", null, null, null, null, null, null, ""));
        //?????????1
        exceptionList.add(new BdExtension("inputcode1", "", null));
        //?????????2
        exceptionList.add(new BdExtension("inputcode2", "", null));
        //????????????1
        exceptionList.add(new BdExtension("extend1", "", null));
        //????????????2
        exceptionList.add(new BdExtension("extend2", "", null));
        //????????????3
        exceptionList.add(new BdExtension("extend3", "", null));
        //??????
        exceptionList.add(new BdExtension("remark", bdOrd.getNote(), null));
        //???????????????:???|???
        exceptionList.add(new BdExtension("feebodysiteflag", true));
        //???????????????????????????
        List<BdCoding> codingList = new ArrayList<>();
        //????????????????????????????????????:code/drugcode|chargeitemcode??????????????????????????????????????????
        //??????????????????
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList("select code,name from bd_item item left join bd_ord_item ordItem on ordItem.pk_item=item.pk_item where ordItem.pk_ord=? ", bdOrd.getPkOrd());
        List<Map<String, Object>> pdList = DataBaseHelper.queryForList("select code,name from bd_pd pd left join bd_ord_item ordItem on ordItem.pk_item=pd.pk_pd where ordItem.pk_ord=? ", bdOrd.getPkOrd());
        for(Map<String, Object> itemMap:itemList){
            //????????????
            codingList.add(new BdCoding("code/chargeitemcode",TransfTool.getPropValueStr(itemMap,"code"),null,null,TransfTool.getPropValueStr(itemMap,"name")));
        }
        for(Map<String, Object> pdMap:pdList){
            //??????
            codingList.add(new BdCoding("code/chargeitemcode",TransfTool.getPropValueStr(pdMap,"code"),null,null,TransfTool.getPropValueStr(pdMap,"name")));
        }
        //?????????????????????
        exceptionList.add(new BdExtension("code/chargeitemcode",codingList));
        //?????????
        exceptionList.add(new BdExtension("sortnumber", "", null));
        //????????????????????????:???????????????/???/?????????????????????/??????????????????
        exceptionList.add(new BdExtension("code/sampletype",null,null,null,null,null,null,""));
        //????????????????????????
        exceptionList.add(new BdExtension("orderprintflag",false));
        //????????????????????????
        if(EnumerateParameter.ONE.equals(bdOrd.getFlagEr())){
            exceptionList.add(new BdExtension("emergencytreatmentflag",true));
        }else{
            exceptionList.add(new BdExtension("emergencytreatmentflag",false));
        }
        substance.setExtension(exceptionList);

        //??????????????????
        List<Entry> entryList = new ArrayList<>();
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        response.setOutcome(BeanMap.create(substance));
        return response;
    }

    /**
     * ??????????????????
     * @param param
     * @param listener
     */
    @Override
    public void getBdPd(String param, ResultListener listener) {
        try{
            //????????????
            RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
            List<Parameter> parameterList = requestBody.getParameter();
            Set<String> codeSet = new HashSet<>();
            //??????????????????
            if(parameterList != null){
                for(Parameter pa : parameterList){
                    if("drugno".equals(pa.getName())){
                        codeSet.add(pa.getValueString());
                    }
                }
            }else{
                listener.error("??????????????????????????????");
                return;
            }
            if(codeSet.size()==0){
                listener.error("??????????????????????????????");
                return;
            }
            List<BdPd> BdPdList = DataBaseHelper.queryForList("select * from bd_pd where del_flag = '0' and code in (" + CommonUtils.convertSetToSqlInPart(codeSet, "code") + ") ", BdPd.class);
            if(CollectionUtils.isNotEmpty(BdPdList)){
                List<Entry> entryList = new ArrayList<>();
                for(BdPd bdPd:BdPdList){
                    Entry entry = new Entry(new Response());
                    Response response = entry.getResponse();
                    response.setStaus(ZsphConstant.RES_SUS_OTHER);
                    BdPdMsg bdPdMsg = new BdPdMsg();
                    bdPdMsg.setResourceType("Medication");
                    bdPdMsg.setImplicitRules("MDMYPXZ");
                    String id = NHISUUID.getKeyId();
                    bdPdMsg.setId(id);
                    //????????????????????????id
                    bdPdMsg.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203 ","YPBH",null))) ,"id/drugno",bdPd.getPkPd(),"official",null)));
                    bdPdMsg.setCode(new BdPdCoding(new Coding("code/drugcode",bdPd.getCode(),bdPd.getName())));
                    bdPdMsg.setStatus(bdPd.getFlagStop());
                    BdDefdoc dtDosage = QueryUtils.getbdDefdocInfo("030400",bdPd.getDtDosage());
                    if(dtDosage == null){
                        throw new BusException("????????????????????????!");
                    }
                    bdPdMsg.setForm(new Code(Arrays.asList(new Coding("code/formcode",bdPd.getDtDosage()==null?"":bdPd.getDtDosage(),dtDosage.getName()==null?"":dtDosage.getName()))));
                    //bdPdMsg.setForm(Arrays.asList(new Coding("code/formcode",bdPd.getDtDosage(),dtDosage.getName())));

                    //Amount ??????
                    Map<String,Object> packMap=DataBaseHelper.queryForMap("select name from bd_unit where pk_unit=?",new Object[]{bdPd.getPkUnitPack()});
                    bdPdMsg.setAmount(new MumeratorCode(new Mumerator(CommonUtils.getString(bdPd.getPackSize(),"1"),MapUtils.getString(packMap,"name"))) );

                    //manufacturer ??????
                    BdFactory bdFactory = getBdFactoryInfo(bdPd.getPkFactory());
                    bdPdMsg.setManufacturer(new Manufacturer(Arrays.asList(new Identifier(null,bdFactory.getCode())),bdFactory.getName()));

                    //batch ??????
                    bdPdMsg.setBatch(new Batch(bdPd.getApprNo(),bdPd.getDateValidReg()==null?"":DateUtils.formatDate(bdPd.getDateValidReg(), "yyyy-MM-dd")));

                    bdPdMsg.setExtension(new ArrayList<>());
                    //??????
                    String bdFactoryName = "";
                    if(bdFactory != null){
                        bdFactoryName = bdFactory.getName();
                    }
                    //bdPdMsg.getExtension().add(new ExtensionPd("manufacturer",bdFactoryName));
                    bdPdMsg.getExtension().add(new ExtensionPd("generic",bdPd.getNameChem()));
                    bdPdMsg.getExtension().add(new ExtensionPd("chemical",bdPd.getNameGen()));
                    bdPdMsg.getExtension().add(new ExtensionPd("spec",bdPd.getSpec()));
                    BdItemHp bdItemHp = QueryUtils.getBdItemHpInfo(bdPd.getPkPd());
                    String type = "";
                    if(bdItemHp != null){
                        //his 0?????? 1?????? 2??????
                        //?????? 0 ?????????1?????????2??????
                        if("0".equals(bdItemHp.getEuLevel())) type = "1";
                        if("1".equals(bdItemHp.getEuLevel())) type = "2";
                        if("2".equals(bdItemHp.getEuLevel())) type = "0";
                    }
                    bdPdMsg.getExtension().add(new ExtensionPd("reimbursement_type",type));

                    String drugName ="";
                    if("0".equals(bdPd.getEuDrugtype())) drugName = "??????";
                    if("1".equals(bdPd.getEuDrugtype())) drugName = "??????";
                    if("2".equals(bdPd.getEuDrugtype())) drugName = "??????";
                    bdPdMsg.getExtension().add(new ExtensionPd("category",drugName));

                    bdPdMsg.getExtension().add(new ExtensionPd("quality",bdPd.getNote()));

                    bdPdMsg.getExtension().add(new ExtensionPd("engregular",bdPd.getShortName()));
                    bdPdMsg.getExtension().add(new ExtensionPd("grade",""));//???????????????
                    bdPdMsg.getExtension().add(new ExtensionPd("basedose",null,null,null,CommonUtils.getDouble(bdPd.getPackSize())));
                    BdUnit bdUnit = QueryUtils.queryUnitById(bdPd.getPkUnitPack());
                    bdPdMsg.getExtension().add(new ExtensionPd("doseunit",bdUnit != null?bdUnit.getName():""));
                    String nameUsecate = "";
                    if("0".equals(bdPd.getEuUsecate())) nameUsecate = "??????";
                    if("1".equals(bdPd.getEuUsecate())) nameUsecate = "??????";
                    if("2".equals(bdPd.getEuUsecate())) nameUsecate = "??????";
                    if("3".equals(bdPd.getEuUsecate())) nameUsecate = "?????????";
                    if("9".equals(bdPd.getEuUsecate())) nameUsecate = "??????";
                    bdPdMsg.getExtension().add(new ExtensionPd("usage",nameUsecate));
                    BdTermFreq bdTermFreq = QueryUtils.queryBdTermFreqByCode(bdPd.getCodeFreq());
                    if(bdTermFreq != null){
                        bdPdMsg.getExtension().add(new ExtensionPd("frequency",bdTermFreq.getName()));
                    }
                    BdUnit Unit = QueryUtils.queryUnitById(bdPd.getPkUnitDef());
                    if(bdPd.getDosageDef() != null && Unit != null){
                        bdPdMsg.getExtension().add(new ExtensionPd("oncedose",bdPd.getDosageDef().toString()+Unit.getName()));
                    }
                    bdPdMsg.getExtension().add(new ExtensionPd("caution",bdPd.getNote()));
                    bdPdMsg.getExtension().add(new ExtensionPd("phyfunction",""));
                    bdPdMsg.getExtension().add(new ExtensionPd("selfmadeflag",null,"1".equals(bdPd.getDtMade())?true:false));
                    bdPdMsg.getExtension().add(new ExtensionPd("octflag",null,null));//??????oct??????
                    bdPdMsg.getExtension().add(new ExtensionPd("skintestflag",null,"1".equals(bdPd.getFlagSt())?true:false));
                    bdPdMsg.getExtension().add(new ExtensionPd("introduction",""));
                    bdPdMsg.getExtension().add(new ExtensionPd("specialflag",null,!"00".equals(bdPd.getDtPois())?true:false));
                    BdUnit unitMin =  QueryUtils.queryUnitById(bdPd.getPkUnitMin());
                    if(unitMin != null){
                        bdPdMsg.getExtension().add(new ExtensionPd("basicpackingunit",unitMin.getName()));
                    }else{
                        bdPdMsg.getExtension().add(new ExtensionPd("basicpackingunit",""));
                    }
                    bdPdMsg.getExtension().add(new ExtensionPd("essentialdrug",null,"1".equals(bdPd.getFlagPrecious())?true:false));
                    bdPdMsg.getExtension().add(new ExtensionPd("importflag",null,"2".equals(bdPd.getDtAbrd())?true:false));
                    Map<String,Object> sourceMap=DataBaseHelper.queryForMap("select code,name  from BD_DEFDOC where CODE_DEFDOCLIST='080015' and CODE=?",new Object[]{CommonUtils.getString(bdPd.getEuSource(),"")});
                    bdPdMsg.getExtension().add(new ExtensionPd("fsdrug",new Coding(MapUtils.getString(sourceMap,"code"),MapUtils.getString(sourceMap,"name"))));
                    bdPdMsg.getExtension().add(new ExtensionPd("preciousflag",null,"1".equals(bdPd.getFlagPrecious())?true:false));
                    //???????????????
                    StringBuffer assiPdSql=new StringBuffer("select att.VAL_ATT val from BD_PD_ATT att");
                    assiPdSql.append(" inner join BD_PD_ATT_DEFINE def on def.PK_PDATTDEF=att.PK_PDATTDEF");
                    assiPdSql.append(" where att.pk_pd=?  and  def.CODE_ATT='0503' and att.DEL_FLAG='0'");
                    List<Map<String,Object>> assiPdMapList=DataBaseHelper.queryForList(assiPdSql.toString(),new Object[]{bdPd.getPkPd()});
                    if(assiPdMapList!=null && assiPdMapList.size()>0) {
                        bdPdMsg.getExtension().add(new ExtensionPd("assistflag", null, "1".equals(CommonUtils.getString(assiPdMapList.get(0), "val")) ? true : false));
                    }
                    bdPdMsg.getExtension().add(new ExtensionPd("pricemode", bdPd.getEuPdprice()));
                    if("0".equals(bdPd.getEuPap())) {//??????
                        bdPdMsg.getExtension().add(new ExtensionPd("quotaorratio", null, null, null, bdPd.getPapRate()));
                    }else if("1".equals(bdPd.getEuPap())){
                        bdPdMsg.getExtension().add(new ExtensionPd("quotaorratio", null, null, null, bdPd.getAmtPap()));
                    }
                    bdPdMsg.getExtension().add(new ExtensionPd("calculatemode", bdPd.getEuPap()));
                    bdPdMsg.getExtension().add(new ExtensionPd("retailprice", CommonUtils.getString(bdPd.getPrice())));
                    bdPdMsg.getExtension().add(new ExtensionPd("validityDate", CommonUtils.getString(bdPd.getValidCnt())));
                    bdPdMsg.getExtension().add(new ExtensionPd("validityUnit", bdPd.getEuValidUnit()));
                    response.setOutcome(BeanMap.create(bdPdMsg));
                    entryList.add(entry);
                }
                listener.success(entryList);
            }else{
                listener.error("????????????");
            }
        } catch (BusException e) {
            listener.error(e.getMessage());
        } catch (Exception e){
            logger.error("???????????????????????????",e);
            listener.error(e.getMessage());
        }
    }
}
