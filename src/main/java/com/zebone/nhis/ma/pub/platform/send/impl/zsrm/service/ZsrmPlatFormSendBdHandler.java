package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.base.bd.support.Constants;
import com.zebone.nhis.base.ou.vo.EmpAndJobParam;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.res.OrgDeptWg;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.scm.pub.BdFactory;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.pub.BdPdAttDefine;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.EnumUrlType;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.MsgIndexData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Code;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Period;
import com.zebone.nhis.ma.pub.platform.zsrm.support.QueryUtils;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.zebone.nhis.ma.pub.platform.zsrm.support.QueryUtils.getBdFactoryInfo;


@Service
public class ZsrmPlatFormSendBdHandler {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
    @Resource
    private RequestTemplate sendTool;

    //发送机构信息
    public void sendBdOuOrgMsg(Map<String, Object> paramMap){
        //获取客户端传递数据
        BdOuOrg bdOuOrg = (BdOuOrg)paramMap.get("bdOuOrg");
        //删除时查询机构信息
        if("DELETE".equals(paramMap.get("STATUS"))){
            bdOuOrg=DataBaseHelper.queryForBean("select * from bd_ou_org where pk_org=?", BdOuOrg.class, bdOuOrg.getPkOrg());
        }
        //创建组装消息推送实体
        String id = NHISUUID.getKeyId();
        Outcome outcome = new Outcome();
        outcome.setResourceType("Organization");
        outcome.setId(id);
        if("ADD".equals(paramMap.get("STATUS"))){
            outcome.setImplicitRules("MDMJGXZ");//新增
        }else{
            outcome.setImplicitRules("MDMJGXG");//修改
        }
        outcome.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203 ","JGBH",null))) ,"code/orgcode",bdOuOrg.getCodeOrg())));
        if(EnumerateParameter.ZERO.equals(bdOuOrg.getFlagActive()) || "DELETE".equals(paramMap.get("STATUS"))){
            outcome.setActive(false);
        }else{
            outcome.setActive(true);
        }

        //1集团医院2医院3院区4公司5政府机构6其他医疗机构9其他机构
        outcome.setType(Arrays.asList(new CodeableConcept(Arrays.asList(TransfTool.getOrgtype("code/orgtype",bdOuOrg.getDtHosptype())))));
        outcome.setName(bdOuOrg.getNameOrg());
        outcome.setAlias(Arrays.asList(bdOuOrg.getNameOrg()));
        //创建推送消息格式，将组装消息实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(outcome))
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Organization").build();
        //消息推送
        //SendAndResolve.getInstance().send("ADD".equals(paramMap.get("STATUS"))? HttpMethod.POST:HttpMethod.PUT,requestData);
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    //发送科室信息新增、删除消息推送
    public void sendBdOuDeptMsg(Map<String, Object> paramMap){
        BdOuDept dept = (BdOuDept)paramMap.get("dept");
        //创建消息实体并赋值
        DeptOutLocation deptOutLocation = new DeptOutLocation();
        String id = NHISUUID.getKeyId();
        deptOutLocation.setResourceType("Location");
        deptOutLocation.setId(id);
        String rleCode = MapUtils.getString(paramMap,"STATUS");
        deptOutLocation.setImplicitRules("_ADD".equals(rleCode)?"MDMKSXZ":"MDMKSXG");//新增
        deptOutLocation.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","deptno",null))),"id/deptno",dept.getCodeDept())));
        deptOutLocation.setName(dept.getNameDept());//科室名称
        deptOutLocation.setType(Arrays.asList(new CodeableConcept(Arrays.asList(TransfTool.getDeptType(dept.getCodeDept().substring(2,4))))));
        List<BdExtension> bdExtensionList = new ArrayList<>();
        if(EnumerateParameter.ZERO.equals(dept.getFlagActive()) || "_DELETE".equals(rleCode)){
            //科室停用或删除时只传递状态
            deptOutLocation.setStatus("inactive");//停用
        }else{
            deptOutLocation.setStatus("active");//启用
            deptOutLocation.setAlias(Arrays.asList(dept.getShortname()));//科室别名
            deptOutLocation.setDescription(dept.getDeptDesc());//科室介绍
            deptOutLocation.setTelecom(Arrays.asList(new Identifier("phone",dept.getTelnoDept())));
            //科室地址信息
            deptOutLocation.setAddress(new Address(dept.getNamePlace(),"Address",null));
            //上级科室资源--父级科室编码
            Map<String,Object> deptPart = DataBaseHelper.queryForMap("select CODE_DEPT from bd_ou_dept where pk_dept = ? ", dept.getPkFather());
            deptOutLocation.setPartOf(new Address(null,"Location",Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","ParentCode",null))),"id/parentdeptno",MapUtils.getString(deptPart,"codeDept")))));
            bdExtensionList.add(new BdExtension("rank",String.valueOf(dept.getBednum()),null));
        }


        bdExtensionList.add(new BdExtension("source_system","HIS",null));
        deptOutLocation.setExtension(bdExtensionList);
        //创建推送消息格式并将组织数据实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(deptOutLocation))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(dept.getCodeDept()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Location").build();
        //消息推送
        SendAndResolve.getInstance().send("_ADD".equals(rleCode)? HttpMethod.POST:HttpMethod.PUT,requestData);
    }
    ////发送科室信息修改消息推送
    public void sendBdOuDeptUpdatrMsg(Map<String, Object> paramMap){
        BdOuDept dept = (BdOuDept)paramMap.get("dept");
        //创建消息实体并赋值
        DeptOutLocation deptOutLocation = new DeptOutLocation();
        String id = NHISUUID.getKeyId();
        deptOutLocation.setResourceType("Location");
        deptOutLocation.setId(id);
        deptOutLocation.setImplicitRules("MDMKSXG");//新增
        deptOutLocation.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","deptno",null))),"id/deptno",dept.getCodeDept())));
        //修改科室信息只传递修改字段信息
        deptOutLocation.setStatus("active");//启用
        BdOuDept updBeforeDept = (BdOuDept)paramMap.get("beforeModification");
        deptOutLocation.setName(dept.getNameDept());//科室名称
        deptOutLocation.setType(Arrays.asList(new CodeableConcept(Arrays.asList(TransfTool.getDeptType(dept.getCodeDept().substring(2,4))))));
        if(!StringUtils.equalsIgnoreCase(dept.getShortname(),updBeforeDept.getShortname())){
            deptOutLocation.setAlias(Arrays.asList(dept.getShortname()));//科室别名
        }
        if(!StringUtils.equalsIgnoreCase(dept.getDeptDesc(),updBeforeDept.getDeptDesc())){
            deptOutLocation.setDescription(dept.getDeptDesc());//科室介绍
        }
        if(!StringUtils.equalsIgnoreCase(dept.getTelnoDept(),updBeforeDept.getTelnoDept())){
            deptOutLocation.setTelecom(Arrays.asList(new Identifier("phone",dept.getTelnoDept())));
        }
        if(!StringUtils.equalsIgnoreCase(dept.getNamePlace(),updBeforeDept.getNamePlace())){
            //科室地址信息
            deptOutLocation.setAddress(new Address(dept.getNamePlace(),"Address",null));
        }
        if(!StringUtils.equalsIgnoreCase(dept.getPkFather(),updBeforeDept.getPkFather())){
            //上级科室资源--父级科室编码
            Map<String,Object> deptPart = DataBaseHelper.queryForMap("select CODE_DEPT from bd_ou_dept where pk_dept = ? ", dept.getPkFather());
            deptOutLocation.setPartOf(new Address(null,"Location",Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","ParentCode",null))),"id/parentdeptno",MapUtils.getString(deptPart,"codeDept")))));
        }
        List<BdExtension> bdExtensionList = new ArrayList<>();
        bdExtensionList.add(new BdExtension("source_system","HIS",null));
        if(!StringUtils.equalsIgnoreCase(String.valueOf(dept.getBednum()),String.valueOf(updBeforeDept.getBednum()))){
            bdExtensionList.add(new BdExtension("rank",String.valueOf(dept.getBednum()),null));
        }
        deptOutLocation.setExtension(bdExtensionList);
        //创建推送消息格式并将组织数据实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(deptOutLocation))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(dept.getCodeDept()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Location").build();
        //消息推送
        SendAndResolve.getInstance().send(HttpMethod.PUT,requestData);
    }

    //发生新增、删除人员信息
    public void sendBdOuEmpMsg(Map<String, Object> paramMap){
        Map<String,Object> empMap = (Map<String,Object>)paramMap.get("emp");
        List<Map<String,Object>> empJobsList=(List<Map<String,Object>>)paramMap.get("empJobs");
        String rleCode =MapUtils.getString(paramMap,"STATUS");
        //创建消息实体并赋值
        Practitioner person = new Practitioner();
        String id = NHISUUID.getKeyId();
        person.setResourceType("Practitioner");
        person.setId(id);
        //MDMRYXG:人员信息修改;MDMRYXZ:人员信息新增
        person.setImplicitRules("_ADD".equals(rleCode)?"MDMRYXZ":"MDMRYXG");//人员信息新增
        //id/emplno id/idno 固定值分别标识人员工号和身份证号
        //emplno  固定值标识人员工号   idno  人员身份证标识符
        List<Identifier> identifierList = new ArrayList<>();
        identifierList.add(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","emplno",null))),"id/emplno",MapUtils.getString(empMap,"codeEmp"),null,null));
        identifierList.add(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","idno",null))),"id/idno",MapUtils.getString(empMap,"idno"),"official",null));
        person.setIdentifier(identifierList);
        person.setName(Arrays.asList(new Identifier(null,null,null,"official",MapUtils.getString(empMap,"nameEmp"))));
        //是否有效true:有效, false：无效
        List<BdExtension> extension = new ArrayList<>();
        if(EnumerateParameter.ZERO.equals(MapUtils.getString(paramMap,"flagActive")) || "_DELETE".equals(rleCode)){
            person.setActive(false);
        }else{
            person.setActive(true);
            //phone:电话;fax:传真;email:邮箱;pager:传呼机;url:个人网站;sms:短信;other:微信
            //home:家庭;work:工作;temp:临时;old:旧的;mobile:移动电话
            List<Identifier> TelecomList = new ArrayList<>();
            if(StringUtils.isNotEmpty(MapUtils.getString(empMap,"mobile"))){
                TelecomList.add(new Identifier(null,"phone",MapUtils.getString(empMap,"mobile"),"mobile",null));//电话-移动电话
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
            //地址种类如home/work/birth等
            person.setAddress(Arrays.asList(new Identifiers("physical",null,null,"work",MapUtils.getString(empMap,"addr"))));
            //male:男性;female:女性;other:其他;unknown:未说明的性别
            person.setGender(TransfTool.getSex(MapUtils.getString(empMap,"dt_sex")));
            Date birthday = TransfTool.getPropValueDates(empMap, "birthday");
            person.setBirthDate(DateUtils.dateToStr("yyyy-MM-dd",birthday==null?new Date():birthday));//出生日期
            //code/loginAcount登录账号标识
            List<Map<String,Object>> codeUserList = DataBaseHelper.queryForList("select pk_user,code_user from bd_ou_user where pk_emp=?", MapUtils.getString(empMap,"pkEmp"));
            //角色
            List<Map<String,Object>> codeRoleList = new ArrayList<>();
            if(codeUserList.size()>0){
                codeRoleList = DataBaseHelper.queryForList("select role.name_role,role.code_role from bd_ou_role role left join bd_ou_user_role userRolr on userRolr.pk_role=role.pk_role where userRolr.del_flag='0' and userRolr.pk_user=?", MapUtils.getString(codeUserList.get(0),"pkUser"));
                extension.add(new BdExtension("code/loginAcount",MapUtils.getString(codeUserList.get(0),"codeUser"),null));
            }
            List<Condition> qualificationList = new ArrayList<>();
            List<BdContained> containedList = new ArrayList<>();
            for(Map<String,Object> empJobsMap:empJobsList){
                //role:角色|job:职务|title:职称|partTimeJobs:兼职情况
                String jobCode =MapUtils.getString(empJobsMap,"jobname");
                if(StringUtils.isNotBlank(jobCode)){
                    qualificationList.add(new Condition(null,new CodeableConcept(Arrays.asList(new Coding("job",jobCode,TransfTool.getBdDefdocName("010301",jobCode))))));
                }
                String dtEmpjob =MapUtils.getString(empJobsMap,"dtEmpjob");
                if(StringUtils.isNotBlank(dtEmpjob)){
                    qualificationList.add(new Condition(null,new CodeableConcept(Arrays.asList(new Coding("title",dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob))))));
                }
                //code/dept:所属科室标识符
                Map<String,Object> deptMap =DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?",empJobsMap.get("pkDept"));
                //organization:所属院区编码
                Map<String,Object> orgMap =DataBaseHelper.queryForMap("select code_org,name_org from bd_ou_org where pk_org = ?",empJobsMap.get("pkOrg"));
                //List<BdExtension> codBdExtenList = new ArrayList<>();
                List<BdCodeableConcept> bdCodeableConcepts = new ArrayList<>();
                //角色层
                if(codeRoleList.size()>0){
                    bdCodeableConcepts.add(new BdCodeableConcept("roleLevel",Arrays.asList(new BdCoding(MapUtils.getString(codeRoleList.get(0),"code_role"),MapUtils.getString(codeRoleList.get(0),"nameRole")))));
                }
                //行政职务名称
                bdCodeableConcepts.add(new BdCodeableConcept("admPosition",Arrays.asList(new BdCoding(jobCode,TransfTool.getBdDefdocName("010301",jobCode)))));
                //科室编码
                //extension.add(new BdExtension("code/dept",null,new BdCodeableConcept(Arrays.asList(new BdCoding(MapUtils.getString(deptMap,"codeDept"),MapUtils.getString(deptMap,"nameDept"))))));
                extension.add(new BdExtension("code/dept",new Coding(MapUtils.getString(deptMap,"codeDept",""),MapUtils.getString(deptMap,"nameDept",""))));
                //院区编码
                extension.add(new BdExtension("organization",new Coding(MapUtils.getString(deptMap,"codeOrg",""),MapUtils.getString(deptMap,"nameOrg",""))));
                //extension.add(new BdExtension("organization",null,new BdCodeableConcept(Arrays.asList(new BdCoding(MapUtils.getString(orgMap,"codeOrg"),MapUtils.getString(orgMap,"nameOrg"))))));
                //在职时间 开始时间duty_date、截止时间date_left
                extension.add(new BdExtension("period",new ValuePeriod(TransfTool.getPropValueDates(empJobsMap, "dutyDate"),TransfTool.getPropValueDates(empJobsMap, "dateLeft"))));
                //业务岗位
                if(StringUtils.isNotBlank(dtEmpjob)){
                    extension.add(new BdExtension("busPost",new Coding(null,dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob))));
                }
                //是否实习生标识
                Boolean isTrainee="09".equals(MapUtils.getString(empMap, "dtEmpsrvtype"));
                extension.add(new BdExtension("isTrainee",isTrainee));
                //是否在职标识-date_left
                if(StringUtils.isNoneEmpty(MapUtils.getString(empJobsMap, "dateLeft"))){
                    DateTime dt = DateTime.now();
                    Boolean isOnJob = MapUtils.getString(empJobsMap, "dateLeft","").compareTo(dt.toString("YYYY-MM-dd"))>0;
                    extension.add(new BdExtension("isOnJob",isOnJob));
                }else{
                    extension.add(new BdExtension("isOnJob",true));
                }
                //入职日期标识
                extension.add(new BdExtension("entryTime",TransfTool.getPropValueDates(empJobsMap, "dutyDate")));
                List<BdCodeableConcept>  specialtyList= Arrays.asList(new BdCodeableConcept(Arrays.asList(new BdCoding(dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob)))));
                if(specialtyList.size()>0){
                    containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),specialtyList,null));
                }else{
                    containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),null,null));
                }
//                if(specialtyList.size()>0){
//                    containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),specialtyList,NHISUUID.getKeyId()));
//                }else{
//                    containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),null,NHISUUID.getKeyId()));
//                }
            }
            if(qualificationList.size()>0){
                person.setQualification(qualificationList);//职位
            }

            //兼职科室代码标识
            person.setContained(containedList);
        }
        extension.add(new BdExtension("source_system","HIS",null));
        //登录账号标识
        person.setExtension(extension);
        //创建推送消息格式并将组织数据实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(person))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(MapUtils.getString(empMap,"codeEmp")).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Practitioner").build();
        //消息推送
        SendAndResolve.getInstance().send("_ADD".equals(rleCode)? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    //发生修改人员信息
    public void sendBdOuEmpUpdateMsg(Map<String, Object> paramMap){
        //获取修改前数据
        EmpAndJobParam employee = (EmpAndJobParam)paramMap.get("beforeModification");
        if(employee== null){
            log.info("修改人员信息beforeModification未null,{}", JsonUtil.writeValueAsString(paramMap));
            return;
        }
        BdOuEmployee empBasd = employee.getEmp();
        //获取修改后数据
        Map<String,Object> empMap = (Map<String,Object>)paramMap.get("emp");
        List<Map<String,Object>> empJobsList=(List<Map<String,Object>>)paramMap.get("empJobs");
        String rleCode =MapUtils.getString(paramMap,"STATUS");
        //创建消息实体并赋值
        Practitioner person = new Practitioner();
        String id = NHISUUID.getKeyId();
        person.setResourceType("Practitioner");
        person.setId(id);
        //MDMRYXG:人员信息修改;MDMRYXZ:人员信息新增
        person.setImplicitRules("_ADD".equals(rleCode)?"MDMRYXZ":"MDMRYXG");//人员信息新增
        //id/emplno id/idno 固定值分别标识人员工号和身份证号
        //emplno  固定值标识人员工号   idno  人员身份证标识符
        List<Identifier> identifierList = new ArrayList<>();
        identifierList.add(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","emplno",null))),"id/emplno",MapUtils.getString(empMap,"codeEmp"),null,null));
        if(!StringUtils.equalsIgnoreCase(empBasd.getIdno(),MapUtils.getString(empMap,"idno"))){
            identifierList.add(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","idno",null))),"id/idno",MapUtils.getString(empMap,"idno"),"official",null));
        }
        person.setIdentifier(identifierList);
        person.setName(Arrays.asList(new Identifier(null,null,null,"official",MapUtils.getString(empMap,"nameEmp"))));
        //是否有效true:有效, false：无效
        if(EnumerateParameter.ZERO.equals(MapUtils.getString(paramMap,"flagActive"))){
            person.setActive(false);
        }else{
            person.setActive(true);
        }
        //phone:电话;fax:传真;email:邮箱;pager:传呼机;url:个人网站;sms:短信;other:微信
        //home:家庭;work:工作;temp:临时;old:旧的;mobile:移动电话
        List<Identifier> TelecomList = new ArrayList<>();
        if(!StringUtils.equalsIgnoreCase(empBasd.getMobile(),MapUtils.getString(empMap,"mobile"))&& StringUtils.isNotEmpty(MapUtils.getString(empMap,"mobile"))){
            TelecomList.add(new Identifier(null,"phone",MapUtils.getString(empMap,"mobile"),"mobile",null));//电话-移动电话
        }
        if(!StringUtils.equalsIgnoreCase(empBasd.getEmail(),MapUtils.getString(empMap,"email"))&& StringUtils.isNotEmpty(MapUtils.getString(empMap,"email"))){
            TelecomList.add(new Identifier(null,"email",MapUtils.getString(empMap,"email"),"work",null));
        }
        if(!StringUtils.equalsIgnoreCase(empBasd.getWorkphone(),MapUtils.getString(empMap,"workphone"))&& StringUtils.isNotEmpty(MapUtils.getString(empMap,"workphone"))){
            TelecomList.add(new Identifier(null,"phone",MapUtils.getString(empMap,"workphone"),"work",null));
        }
        if(!StringUtils.equalsIgnoreCase(empBasd.getHomephone(),MapUtils.getString(empMap,"homephone"))&& StringUtils.isNotEmpty(MapUtils.getString(empMap,"homephone"))){
            TelecomList.add(new Identifier(null,"phone",MapUtils.getString(empMap,"homephone"),"home",null));
        }
        person.setTelecom(TelecomList);
        //地址种类如home/work/birth等
        if(!StringUtils.equalsIgnoreCase(empBasd.getAddr(),MapUtils.getString(empMap,"addr"))){
            person.setAddress(Arrays.asList(new Identifiers("physical",null,null,"work",MapUtils.getString(empMap,"addr"))));
        }
        //male:男性;female:女性;other:其他;unknown:未说明的性别
        if(!StringUtils.equalsIgnoreCase(empBasd.getDtSex(),MapUtils.getString(empMap,"dtSex"))){
            person.setGender(TransfTool.getSex(MapUtils.getString(empMap,"dt_sex")));
        }
        Date birthday = TransfTool.getPropValueDates(empMap, "birthday");
        person.setBirthDate(DateUtils.dateToStr("yyyy-MM-dd",birthday==null?new Date():birthday));//出生日期
        List<BdExtension> extension = new ArrayList<>();
        //code/loginAcount登录账号标识
        List<Map<String,Object>> codeUserList = DataBaseHelper.queryForList("select pk_user,code_user from bd_ou_user where pk_emp=?", MapUtils.getString(empMap,"pkEmp"));
        //角色
        List<Map<String,Object>> codeRoleList = new ArrayList<>();
        if(codeUserList.size()>0){
            codeRoleList = DataBaseHelper.queryForList("select role.name_role,role.code_role from bd_ou_role role left join bd_ou_user_role userRolr on userRolr.pk_role=role.pk_role where userRolr.del_flag='0' and userRolr.pk_user=?", MapUtils.getString(codeUserList.get(0),"pkUser"));
            extension.add(new BdExtension("code/loginAcount",MapUtils.getString(codeUserList.get(0),"codeUser"),null));
        }
        List<Condition> qualificationList = new ArrayList<>();
        List<BdContained> containedList = new ArrayList<>();
        for(Map<String,Object> empJobsMap:empJobsList){
            //role:角色|job:职务|title:职称|partTimeJobs:兼职情况
            String jobCode =MapUtils.getString(empJobsMap,"jobname");
            if(StringUtils.isNotBlank(jobCode)){
                qualificationList.add(new Condition(null,new CodeableConcept(Arrays.asList(new Coding("job",jobCode,TransfTool.getBdDefdocName("010301",jobCode))))));
            }
            String dtEmpjob =MapUtils.getString(empJobsMap,"dtEmpjob");
            if(StringUtils.isNotBlank(dtEmpjob)){
                qualificationList.add(new Condition(null,new CodeableConcept(Arrays.asList(new Coding("title",dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob))))));
            }
            //code/dept:所属科室标识符
            Map<String,Object> deptMap =DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?",empJobsMap.get("pkDept"));
            //organization:所属院区编码
            Map<String,Object> orgMap =DataBaseHelper.queryForMap("select code_org,name_org from bd_ou_org where pk_org = ?",empJobsMap.get("pkOrg"));
            //List<BdExtension> codBdExtenList = new ArrayList<>();
            List<BdCodeableConcept> bdCodeableConcepts = new ArrayList<>();
            //角色层
            if(codeRoleList.size()>0){
                bdCodeableConcepts.add(new BdCodeableConcept("roleLevel",Arrays.asList(new BdCoding(MapUtils.getString(codeRoleList.get(0),"code_role"),MapUtils.getString(codeRoleList.get(0),"nameRole")))));
            }
            //行政职务名称
            bdCodeableConcepts.add(new BdCodeableConcept("admPosition",Arrays.asList(new BdCoding(jobCode,TransfTool.getBdDefdocName("010301",jobCode)))));
            //科室编码
            //extension.add(new BdExtension("code/dept",null,new BdCodeableConcept(Arrays.asList(new BdCoding(MapUtils.getString(deptMap,"codeDept"),MapUtils.getString(deptMap,"nameDept"))))));
            extension.add(new BdExtension("code/dept",new Coding(MapUtils.getString(deptMap,"codeDept",""),MapUtils.getString(deptMap,"nameDept",""))));
            //院区编码
            //extension.add(new BdExtension("organization",null,new BdCodeableConcept(Arrays.asList(new BdCoding(MapUtils.getString(orgMap,"codeOrg"),MapUtils.getString(orgMap,"nameOrg"))))));
            extension.add(new BdExtension("organization",new Coding(MapUtils.getString(deptMap,"codeOrg",""),MapUtils.getString(deptMap,"nameOrg",""))));
            //在职时间 开始时间duty_date、截止时间date_left
            extension.add(new BdExtension("period",new ValuePeriod(TransfTool.getPropValueDates(empJobsMap, "dutyDate"),TransfTool.getPropValueDates(empJobsMap, "dateLeft"))));
            //业务岗位
            extension.add(new BdExtension("busPost",new Coding(null,dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob))));
            //是否实习生标识
            Boolean isTrainee="09".equals(MapUtils.getString(empMap, "dtEmpsrvtype"));
            extension.add(new BdExtension("isTrainee",isTrainee));
            //是否在职标识-date_left
            if(StringUtils.isNoneEmpty(MapUtils.getString(empJobsMap, "dateLeft"))){
                DateTime dt = DateTime.now();
                Boolean isOnJob = MapUtils.getString(empJobsMap, "dateLeft").compareTo(dt.toString("YYYY-MM-dd"))>0;
                extension.add(new BdExtension("isOnJob",isOnJob));
            }else{
                extension.add(new BdExtension("isOnJob",true));
            }
            //入职日期标识
            extension.add(new BdExtension("entryTime",TransfTool.getPropValueDates(empJobsMap, "dutyDate")));
            List<BdCodeableConcept>  specialtyList= Arrays.asList(new BdCodeableConcept(Arrays.asList(new BdCoding(dtEmpjob,TransfTool.getBdDefdocName("010306",dtEmpjob)))));
            if(specialtyList.size()>0){
                containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),specialtyList,null));
            }else{
                containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),null,null));
            }
//            if(specialtyList.size()>0){
//                containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),specialtyList,NHISUUID.getKeyId()));
//            }else{
//                containedList.add(new BdContained("PractitionerRole",bdCodeableConcepts,Arrays.asList(new BdLocation("Location",Arrays.asList(new Identifier(null,MapUtils.getString(deptMap,"codeDept"))),MapUtils.getString(deptMap,"nameDept"))),null,NHISUUID.getKeyId()));
//            }
        }
        extension.add(new BdExtension("source_system","HIS",null));
        person.setQualification(qualificationList);//职位
        //登录账号标识
        person.setExtension(extension);
        //兼职科室代码标识
        person.setContained(containedList);
        //创建推送消息格式并将组织数据实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(person))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(MapUtils.getString(empMap,"codeEmp")).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Practitioner").build();
        //消息推送
        SendAndResolve.getInstance().send("_ADD".equals(rleCode)? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    //诊断字典
    public void sendBdTermDiagMsg(Map<String, Object> paramMap){
        BdTermDiag diag = (BdTermDiag)paramMap.get("diag");
        String rleCode =MapUtils.getString(paramMap,"STATUS");
        CodeSystem codeSystem = new CodeSystem();
        String id = NHISUUID.getKeyId();
        codeSystem.setResourceType("CodeSystem");
        codeSystem.setId(id);
        //诊断分类ICD10、ICD9
        String dtEmpjobName =TransfTool.getBdDefdocName("060000",diag.getDtSystem());
        codeSystem.setIdentifier(Arrays.asList(new Identifier(null,dtEmpjobName==null?"":dtEmpjobName)));
        boolean valueBoolean=true;
        if(EnumerateParameter.ONE.equals(diag.getFlagStop()) || "_DELETE".equals(rleCode)){
            valueBoolean=false;
        }
        List<Property> propertyList = new ArrayList<>();
        //是否启用：isactive：启用标志
        propertyList.add(new Property("isactive",valueBoolean));

        if("02".equals(diag.getDtDiagtype())){
            //MDMSSXG:手术诊断信息修改;MDMSSXZ:手术诊断信息新增
            codeSystem.setImplicitRules("_ADD".equals(rleCode)?"MDMSSXZ":"MDMSSXG");
            //手术等级
            if(StringUtils.isNotBlank(diag.getDtOplevel())){
                Map<String,Object> levelMap = DataBaseHelper.queryForMap("select BA_CODE,NAME from bd_defdoc where DEL_FLAG = '0' and code_defdoclist='030305' and code=?",diag.getDtOplevel());
                propertyList.add(new Property("level",new Coding(null,MapUtils.getString(levelMap,"baCode"),MapUtils.getString(levelMap,"name"))));
            }
            //标准手术代码
            propertyList.add(new Property("icd9Code",new Coding(null,diag.getDiagcode(),diag.getDiagname())));
            if("_ADD".equals(rleCode)){
                //创建时间
                propertyList.add(new Property("createDate",new Date()));
            }else{
                //修改时间
                propertyList.add(new Property("lastUpdateDate",new Date()));
            }
            codeSystem.setConcept(Arrays.asList(new Concept(diag.getDiagcode(),diag.getDiagname(),propertyList)));
        }else{
            //MDMZDXG:诊断信息修改;MDMZDXZ:诊断信息新增|
            codeSystem.setImplicitRules("_ADD".equals(rleCode)?"MDMZDXZ":"MDMZDXG");
            //上级诊断
            if(StringUtils.isNotBlank(diag.getPkFather())){
                Map<String,Object>  fathDiagMap= DataBaseHelper.queryForMap("select * from BD_TERM_DIAG where PK_DIAG=?",diag.getPkFather());
                propertyList.add(new Property("parentcode",new Coding(null,MapUtils.getString(fathDiagMap,"diagcode"),MapUtils.getString(fathDiagMap,"diagname"))));
            }
            //infectflag：传染病标志
            if("01".equals(diag.getDtDiagtype())){
                propertyList.add(new Property("infectflag",true));
            }else{
                propertyList.add(new Property("infectflag",false));
            }
            //cancerflag：肿瘤标志
            propertyList.add(new Property("cancerflag",false));
            //genderflag:男女适用性标识:1:男 2:女 3：通用--NHIS无默认通用
            propertyList.add(new Property("genderflag",null,"3"));
            //chronicflag：慢病标志标识
            propertyList.add(new Property("chronicflag",!"".equals(diag.getDtCodePh())));
            //标准诊断标识
            propertyList.add(new Property("icd10Code",new Coding(null,diag.getDiagcode(),diag.getDiagname())));
            codeSystem.setConcept(Arrays.asList(new Concept(diag.getDiagcode(),diag.getDiagname(),propertyList)));
        }
        //创建推送消息格式并将组织数据实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(codeSystem))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(diag.getDiagcode()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Basic").build();
        //消息推送
        SendAndResolve.getInstance().send("_ADD".equals(rleCode)? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    //收费项目字典
    public void sendBdItemMsg(Map<String, Object> paramMap){
        String rleCode =MapUtils.getString(paramMap,"state");
        Map<String,Object> itemMap = (Map<String,Object>)paramMap.get("item");
        ChargeItem chargeItem = new ChargeItem();
        String id = NHISUUID.getKeyId();
        chargeItem.setResourceType("ChargeItem");
        //MDMSFXMXZ:收费项目字典新增,MDMSFXMXG:收费项目字典修改
        chargeItem.setImplicitRules("_ADD".equals(rleCode)?"MDMSFXMXZ":"MDMSFXMXG");
        chargeItem.setId(id);
        chargeItem.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203","SFBH",null))),"id/chargeitemno",MapUtils.getString(itemMap,"code"),"official",null)));
        //项目状态：0启用，1停用
        if(EnumerateParameter.ZERO.equals(MapUtils.getString(itemMap,"flagActive")) || "_DELETE".equals(rleCode)){
            chargeItem.setStatus(EnumerateParameter.ONE);
        }else{
            chargeItem.setStatus(EnumerateParameter.ZERO);
        }
        //标识符描述
        chargeItem.setCode(new CodeableConcept(Arrays.asList(new Coding("feeitem",MapUtils.getString(itemMap,"code"),MapUtils.getString(itemMap,"name")))));
        List<Map<String, Object>> itemPriceList = DataBaseHelper.queryForList("select * from bd_item_price where flag_stop='0' and pk_item=? ", MapUtils.getString(itemMap,"pkItem"));
        //有效周期
        chargeItem.setOccurrencePeriod(new ValuePeriod(TransfTool.getPropValueDates(itemPriceList.get(0),"dateBegin"),TransfTool.getPropValueDates(itemPriceList.get(0),"dateEnd")));
        //收费项目创建时间
        chargeItem.setEnteredDate(TransfTool.getPropValueDates(itemMap, "createTime"));
        List<BdExtension> extensionList = new ArrayList<>();
        //项目单位
        Map<String, Object> unitMap = DataBaseHelper.queryForMap("select name from bd_unit where pk_unit=? ", MapUtils.getString(itemMap, "pkUnit"));
        extensionList.add(new BdExtension("unit", MapUtils.getString(unitMap, "name"), null));
        //项目成本价格
        extensionList.add(new BdExtension("costprice", new BigDecimal(MapUtils.getString(itemPriceList.get(0), "price"))));
        //项目单价
        extensionList.add(new BdExtension("unitprice", new BigDecimal(MapUtils.getString(itemPriceList.get(0), "price"))));
        //输入码1
        extensionList.add(new BdExtension("inputcode1", "", null));
        //输入码2
        extensionList.add(new BdExtension("inputcode2", "", null));
        //医嘱标志
        extensionList.add(new BdExtension("ordersign", false));
        //报销标志0自费，1甲类，2乙类
        Map<String, Object> itemHpMap = DataBaseHelper.queryForMap("select * from bd_item_hp where pk_item=? ", MapUtils.getString(itemMap, "pkItem"));
        if (EnumerateParameter.ZERO.equals(MapUtils.getString(itemHpMap, "euLevel"))) {
            extensionList.add(new BdExtension("code/reimbursementcode", null, null, null, null, null, null, EnumerateParameter.ONE));
        } else if (EnumerateParameter.ONE.equals(MapUtils.getString(itemHpMap, "euLevel"))) {
            extensionList.add(new BdExtension("code/reimbursementcode", null, null, null, null, null, null, EnumerateParameter.TWO));
        } else {
            extensionList.add(new BdExtension("code/reimbursementcode", null, null, null, null, null, null, EnumerateParameter.ZERO));
        }
        //门诊住院标志0全院，1门诊，2住院
        extensionList.add(new BdExtension("inoutflag",null,null,null,null,null,null,EnumerateParameter.ZERO));
        //默认执行科室:科室编码
        extensionList.add(new BdExtension("code/defaultexec", null, null, null, null, null, null, ""));
        //物价代码
        extensionList.add(new BdExtension("code/pricecode", null, null, null, null, null, null, MapUtils.getString(itemMap, "code")));
        //进口标志
        if (!"01".equals(MapUtils.getString(itemMap, "dtSanitype"))) {
            extensionList.add(new BdExtension("importflag", true));
        } else {
            extensionList.add(new BdExtension("importflag", false));
        }
        //扩展字段1
        extensionList.add(new BdExtension("extend1", "", null));
        //扩展字段2
        extensionList.add(new BdExtension("extend2", "", null));
        //扩展字段3
        extensionList.add(new BdExtension("extend3", "", null));
        //备注
        extensionList.add(new BdExtension("remark", MapUtils.getString(itemMap, "note"), null));
        //项目大类
        extensionList.add(new BdExtension("code/chargeitemclass", null, null, null, null, null, null, MapUtils.getString(itemMap, "dtItemtype")));
        //项目自费比例
        extensionList.add(new BdExtension("selfexpenditure", "", null));
        //国家项目编码
        extensionList.add(new BdExtension("code/nationalitem", MapUtils.getString(itemMap, "codeStd"), null));
        //国家项目分码
        extensionList.add(new BdExtension("code/nationalitemdivision", "", null));
        //附加材料
        extensionList.add(new BdExtension("additionalmaterials",false));
        //贵重耗材标志
        if("0701".equals(MapUtils.getString(itemMap, "dtItemtype"))){
            extensionList.add(new BdExtension("preciousconsumables",true));
        }else{
            extensionList.add(new BdExtension("preciousconsumables",false));
        }
        //项目内涵
        extensionList.add(new BdExtension("itemconnotation",MapUtils.getString(itemMap, "descItem"),null));
        //检查项目标识
        if("04".equals(MapUtils.getString(itemMap, "dtItemtype").toString().substring(0,2))){
            extensionList.add(new BdExtension("checkitemflag",true));
        }else{
            extensionList.add(new BdExtension("checkitemflag",false));
        }
        //主项目分类
        extensionList.add(new BdExtension("code/mainitem", null, null, null, null, null, null, ""));
        //耗材标志
        if("07".equals(MapUtils.getString(itemMap, "dtItemtype").toString().substring(0,2))){
            extensionList.add(new BdExtension("consumablesflag",true));
        }else{
            extensionList.add(new BdExtension("consumablesflag",false));
        }

        Map<String, Object> cateMap = DataBaseHelper.queryForMap("select code from bd_itemcate where pk_itemcate=? ", MapUtils.getString(itemMap, "pkItemcate"));
        //门诊发票项目
        extensionList.add(new BdExtension("code/outinvoiceitem",null,null,null,null,null,null,MapUtils.getString(cateMap, "code")));
        //门诊会计项目
        extensionList.add(new BdExtension("code/outaccountitem",null,null,null,null,null,null,MapUtils.getString(cateMap, "code")));
        //住院发票项目
        extensionList.add(new BdExtension("code/inputinvoiceitem",null,null,null,null,null,null,MapUtils.getString(cateMap, "code")));
        //住院会计项目
        extensionList.add(new BdExtension("code/inputaccountitem",null,null,null,null,null,null,MapUtils.getString(cateMap, "code")));
        //财务核算项目
        Map<String, Object> auditMap = DataBaseHelper.queryForMap("select code from bd_audit where pk_audit=? ", MapUtils.getString(itemMap, "pkAudit"));
        extensionList.add(new BdExtension("code/financialaccountitem",null,null,null,null,null,null,MapUtils.getString(auditMap, "code")));
        //病案统计项目
        extensionList.add(new BdExtension("code/medicalitem",null,null,null,null,null,null,MapUtils.getString(itemMap, "dtChcate")));
        chargeItem.setExtension(extensionList);

        //创建推送消息格式并将组织数据实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(chargeItem))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(MapUtils.getString(itemMap,"code")).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("ChargeItem").build();
        log.info("json 实体:" + requestData.getData());
        //消息推送
        SendAndResolve.getInstance().send("_ADD".equals(rleCode)? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    //医嘱项目字典-诊疗项目字典
    public void sendBdOrdMsg(Map<String, Object> paramMap){
        BdOrd bdOrd = (BdOrd)paramMap.get("bdOrds");
        String rleCode =MapUtils.getString(paramMap,"state");
        String id = NHISUUID.getKeyId();
        bdOrdSubstance substance = new bdOrdSubstance();
        substance.setResourceType("Substance");
        substance.setId(id);
        //MDMZLXZ:诊疗项目字典新增,MDMZLXG:诊疗项目字典修改
        substance.setImplicitRules("_ADD".equals(rleCode) ? "MDMZLXZ" : "MDMZLXG");
        substance.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203", "ZLBH", null))), "id/substanceno", bdOrd.getCode(), "", null)));
        //项目状态：0启用，1停用
        if(EnumerateParameter.ONE.equals(bdOrd.getFlagActive()) || "_DELETE".equals(rleCode)){
            substance.setStatus(EnumerateParameter.ONE);
        }else{
            substance.setStatus(EnumerateParameter.ZERO);
        }
        //项目编码、名称
        substance.setCode(new CodeableConcept(Arrays.asList(new Coding("code/substancecode",bdOrd.getCode(),bdOrd.getName()))));
        //诊疗项目分类。分类编码、名称
        substance.setCategory(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("code/substancecategory",bdOrd.getCodeOrdtype(),MapUtils.getString(DataBaseHelper.queryForMap("select name from bd_ordtype where code=? and del_flag='0'",bdOrd.getCodeOrdtype()),"name"))))));
        //诊疗项目医嘱信息 医嘱编码、医嘱名称
        substance.setSubstanceCodeableConcept(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("code/ordercode",bdOrd.getCode(),bdOrd.getName())))));
        List<BdExtension> exceptionList = new ArrayList<>();
        //医嘱费用
        exceptionList.add(new BdExtension("ordercost","",null));
        //默认执行科室，科室编码
        List<Map<String,Object>> ordDeptList = DataBaseHelper.queryForList("select dept.name_dept,dept.code_dept from bd_ord_dept ordDept left join bd_ou_dept dept on dept.pk_dept=ordDept.pk_dept  where ordDept.flag_def='1' and ordDept.pk_ord=? and ordDept.pk_org=?",bdOrd.getPkOrd(),MapUtils.getString(paramMap,"pkOrg"));
        if(ordDeptList != null &&ordDeptList.size()>0){
            exceptionList.add(new BdExtension("code/defaultexec",null,null,null,null,null,null,MapUtils.getString(ordDeptList.get(0),"codeDept")));
        }else{
            exceptionList.add(new BdExtension("code/defaultexec",null,null,null,null,null,null,""));
        }
        //检查部位
        Map<String, Object> ordRisMap = DataBaseHelper.queryForMap("select dt_body from bd_ord_ris where pk_ord=?", bdOrd.getPkOrd());
        exceptionList.add(new BdExtension("bodysite",TransfTool.getBdDefdocName("030101",MapUtils.getString(ordRisMap,"dtBody")),null));
        //体位
        exceptionList.add(new BdExtension("position", "", null));
        //检查方法
        exceptionList.add(new BdExtension("method", "", null));
        //包含介质
        exceptionList.add(new BdExtension("media", "", null));
        //性别限制:0-限制男性|1-限制女性|2-不限制
        if (EnumerateParameter.ONE.equals(bdOrd.getEuSex())) {
            exceptionList.add(new BdExtension("genderconstraint", null, null, null, null, null, null, EnumerateParameter.ZERO));
        } else if (EnumerateParameter.TWO.equals(bdOrd.getEuSex())) {
            exceptionList.add(new BdExtension("genderconstraint", null, null, null, null, null, null, EnumerateParameter.ONE));
        } else {
            exceptionList.add(new BdExtension("genderconstraint", null, null, null, null, null, null, EnumerateParameter.TWO));
        }
        //诊疗场景:0-仅门诊|1-仅住院|2-仅急诊|3-仅体检|4-无限制
        exceptionList.add(new BdExtension("scenario", null, null, null, null, null, null, ""));
        //输入码1
        exceptionList.add(new BdExtension("inputcode1", "", null));
        //输入码2
        exceptionList.add(new BdExtension("inputcode2", "", null));
        //扩展字段1
        exceptionList.add(new BdExtension("extend1", "", null));
        //扩展字段2
        exceptionList.add(new BdExtension("extend2", "", null));
        //扩展字段3
        exceptionList.add(new BdExtension("extend3", "", null));
        //备注
        exceptionList.add(new BdExtension("remark", bdOrd.getNote(), null));
        //按部位收费:是|否
        exceptionList.add(new BdExtension("feebodysiteflag", true));
        //对应收费费项目标识
        List<BdCoding> codingList = new ArrayList<>();
        //收费项目：药品或收费项目:code/drugcode|chargeitemcode、收费项目编码、收费项目名称
        //查询收费项目
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList("select code,name,ordItem.quan from bd_item item left join bd_ord_item ordItem on ordItem.pk_item=item.pk_item where ordItem.pk_ord=? ", bdOrd.getPkOrd());
        List<Map<String, Object>> pdList = DataBaseHelper.queryForList("select code,name,ordItem.quan from bd_pd pd left join bd_ord_item ordItem on ordItem.pk_item=pd.pk_pd where ordItem.pk_ord=? ", bdOrd.getPkOrd());
        for (Map<String, Object> itemMap : itemList) {
            //收费项目
            codingList.add(new BdCoding("code/chargeitemcode", MapUtils.getString(itemMap, "code"), MapUtils.getString(itemMap, "quan"), null, MapUtils.getString(itemMap, "name")));
        }
        for (Map<String, Object> pdMap : pdList) {
            //药品
            codingList.add(new BdCoding("code/chargeitemcode", MapUtils.getString(pdMap, "code"), MapUtils.getString(pdMap, "quan"), null, MapUtils.getString(pdMap, "name")));
        }
        //对应收费费项目
        exceptionList.add(new BdExtension("code/chargeitemcode", codingList));
        //排序号
        exceptionList.add(new BdExtension("sortnumber", "", null));
        //诊疗项目样本类型:检验：血清/痰/分泌物等；检查/治疗如无则空
        exceptionList.add(new BdExtension("code/sampletype",null,null,null,null,null,null,""));
        //医嘱打印样本标志
        exceptionList.add(new BdExtension("orderprintflag",false));
        //是否可开急诊标志
        if(EnumerateParameter.ONE.equals(bdOrd.getFlagEr())){
            exceptionList.add(new BdExtension("emergencytreatmentflag",true));
        }else{
            exceptionList.add(new BdExtension("emergencytreatmentflag",false));
        }
        substance.setExtension(exceptionList);

        //创建推送消息格式并将组织数据实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(substance))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(bdOrd.getCode()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Substance").build();
        log.info("json 实体：" + requestData.getData());
        //消息推送
        SendAndResolve.getInstance().send("_ADD".equals(rleCode)? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    public void sendBdPdMsg(Map<String, Object> paramMap){
        if(paramMap == null || paramMap.get("STATUS") == null || paramMap.get("pkPd")==null){
            throw new BusException("推送时未获取到药品信息！");
        }
        String status = MapUtils.getString(paramMap,"STATUS");
        BdPd bdPd =  DataBaseHelper.queryForBean("select * from bd_pd where pk_pd = ?",BdPd.class,paramMap.get("pkPd"));
        if("_ADD".equals(status)){
            sendSavedBdPdMsg(bdPd);
        }else{
            BdPd pdOld = null;
            if("_UPDATE".equals(status)){
                pdOld = (BdPd)paramMap.get("pdOld");
            }
            sendUpdateBdPdMsg(pdOld,bdPd,status);
        }

    }

    /**
     * 新增药品
     * @param bdPd
     */
    public void sendSavedBdPdMsg(BdPd bdPd){
        if(bdPd == null){
            throw new BusException("根据药品主键未获取到药品信息！");
        }
        BdPdMsg bdPdMsg = new BdPdMsg();
        bdPdMsg.setResourceType("Medication");
        bdPdMsg.setImplicitRules("MDMYPXZ");
        String id = NHISUUID.getKeyId();
        bdPdMsg.setId(id);
        //编码会改变所以用id
        bdPdMsg.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203 ","YPBH",null))) ,"id/drugno",bdPd.getPkPd(),"official",null)));
        bdPdMsg.setCode(new BdPdCoding(new Coding("code/drugcode",bdPd.getCode(),bdPd.getName())));
        bdPdMsg.setStatus(bdPd.getFlagStop());
        BdDefdoc dtDosage = QueryUtils.getbdDefdocInfo("030400",bdPd.getDtDosage());
        if(dtDosage == null){
            throw new BusException("未获取到药品剂型!");
        }
        bdPdMsg.setForm(new Code(Arrays.asList(new Coding("code/formcode",bdPd.getDtDosage()==null?"":bdPd.getDtDosage(),dtDosage.getName()==null?"":dtDosage.getName()))));
        //bdPdMsg.setForm(Arrays.asList(new Coding("code/formcode",bdPd.getDtDosage()==null?"":bdPd.getDtDosage(),dtDosage.getName()==null?"":dtDosage.getName())));

        BdUnit bdUnit = QueryUtils.queryUnitById(bdPd.getPkUnitPack());
        bdPdMsg.setAmount(new MumeratorCode(new Mumerator(bdPd.getPackSize()==null?"":bdPd.getPackSize().toString(),bdUnit==null?"":bdUnit.getName())));

        //厂商
        BdFactory bdFactory = getBdFactoryInfo(bdPd.getPkFactory());
        String bdFactoryName = "";
        if(bdFactory != null){
            bdFactoryName = bdFactory.getName();
        }
        bdPdMsg.setManufacturer(new Manufacturer(Arrays.asList(new Identifier(null,bdFactory==null?"":bdFactory.getCode())),bdFactoryName));

        bdPdMsg.setBatch(new Batch(bdPd.getApprNo(),bdPd.getDateValidReg()==null?"":DateUtils.formatDate(bdPd.getDateValidReg(), "yyyy-MM-dd")));

        bdPdMsg.setExtension(new ArrayList<>());

        //bdPdMsg.getExtension().add(new ExtensionPd("manufacturer",bdFactoryName==null?"":bdFactoryName));
        bdPdMsg.getExtension().add(new ExtensionPd("generic",bdPd.getNameChem()==null?"":bdPd.getNameChem()));
        bdPdMsg.getExtension().add(new ExtensionPd("chemical",bdPd.getNameGen()==null?"":bdPd.getNameGen()));
        bdPdMsg.getExtension().add(new ExtensionPd("spec",bdPd.getSpec()==null?"":bdPd.getSpec()));
        BdItemHp bdItemHp = QueryUtils.getBdItemHpInfo(bdPd.getPkPd());
        String type = "";
        if(bdItemHp != null){
            //his 0甲类 1乙类 2自费
            //平台 0 自费，1甲类，2乙类
            if("0".equals(bdItemHp.getEuLevel())) type = "1";
            if("1".equals(bdItemHp.getEuLevel())) type = "2";
            if("2".equals(bdItemHp.getEuLevel())) type = "0";
        }
        bdPdMsg.getExtension().add(new ExtensionPd("reimbursement_type",null,null,type));
        String drugName ="";
        if("0".equals(bdPd.getEuDrugtype())) drugName = "西药";
        if("1".equals(bdPd.getEuDrugtype())) drugName = "成药";
        if("2".equals(bdPd.getEuDrugtype())) drugName = "草药";
        bdPdMsg.getExtension().add(new ExtensionPd("category",drugName));
        bdPdMsg.getExtension().add(new ExtensionPd("quality",bdPd.getNote()==null?"":bdPd.getNote()));
        bdPdMsg.getExtension().add(new ExtensionPd("engregular",bdPd.getShortName()));
        bdPdMsg.getExtension().add(new ExtensionPd("grade",null,null,""));//项目等级无
        bdPdMsg.getExtension().add(new ExtensionPd("basedose",null,null,null,Double.parseDouble(bdPd.getPackSize().toString())));
        bdPdMsg.getExtension().add(new ExtensionPd("doseunit",bdUnit != null?bdUnit.getName():""));
        String nameUsecate = "";
        if("0".equals(bdPd.getEuUsecate())) nameUsecate = "口服";
        if("1".equals(bdPd.getEuUsecate())) nameUsecate = "外用";
        if("2".equals(bdPd.getEuUsecate())) nameUsecate = "注射";
        if("3".equals(bdPd.getEuUsecate())) nameUsecate = "大输液";
        if("9".equals(bdPd.getEuUsecate())) nameUsecate = "其他";
        bdPdMsg.getExtension().add(new ExtensionPd("usage",nameUsecate==null?"":nameUsecate));
        BdTermFreq bdTermFreq = QueryUtils.queryBdTermFreqByCode(bdPd.getCodeFreq());
        if(bdTermFreq != null){
            bdPdMsg.getExtension().add(new ExtensionPd("frequency",bdTermFreq.getName()==null?"":bdTermFreq.getName()));
        }
        BdUnit Unit = QueryUtils.queryUnitById(bdPd.getPkUnitDef());
        if(bdPd.getDosageDef() != null && Unit != null){
            bdPdMsg.getExtension().add(new ExtensionPd("oncedose",bdPd.getDosageDef().toString()+Unit.getName()));
        }
        bdPdMsg.getExtension().add(new ExtensionPd("caution",bdPd.getNote()==null?"":bdPd.getNote()));
        bdPdMsg.getExtension().add(new ExtensionPd("phyfunction",""));
        bdPdMsg.getExtension().add(new ExtensionPd("selfmadeflag",null,"1".equals(bdPd.getDtMade())?true:false));
        //bdPdMsg.getExtension().add(new ExtensionPd("octflag","",null));没有oct标志
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
        BdDefdoc dtSource = QueryUtils.getbdDefdocInfo("080015",bdPd.getEuSource());
        bdPdMsg.getExtension().add(new ExtensionPd("fsdrug",new Coding(bdPd.getEuSource()==null?"":bdPd.getEuSource(),dtSource==null?"":dtSource.getName())));
        bdPdMsg.getExtension().add(new ExtensionPd("preciousflag",null,"1".equals(bdPd.getFlagPrecious())?true:false));
        BdPdAttDefine bdPdAtt = QueryUtils.getBdPdAttInfo(bdPd.getPkPd(),"0503");
        if(bdPdAtt!=null){
            bdPdMsg.getExtension().add(new ExtensionPd("assistflag",null,"1".equals(bdPdAtt.getValDef())?true:false));
        }
        bdPdMsg.getExtension().add(new ExtensionPd("pricemode",bdPd.getEuPdprice()==null?"":bdPd.getEuPdprice()));
        bdPdMsg.getExtension().add(new ExtensionPd("calculatemode",bdPd.getEuPap()==null?"":bdPd.getEuPap()));
        bdPdMsg.getExtension().add(new ExtensionPd("quotaorratio",null,null,null,"0".equals(bdPd.getEuPap())?bdPd.getPapRate():bdPd.getAmtPap()));
        bdPdMsg.getExtension().add(new ExtensionPd("retailprice",bdPd.getPrice()==null?"":bdPd.getPrice().toString()));
        bdPdMsg.getExtension().add(new ExtensionPd("validityDate",bdPd.getValidCnt()==null?"":bdPd.getValidCnt().toString()));
        bdPdMsg.getExtension().add(new ExtensionPd("validityUnit",bdPd.getEuValidUnit()==null?"":bdPd.getEuValidUnit()));

        //创建推送消息格式，将组装消息实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(bdPdMsg))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(bdPd.getCode()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Medication").build();
        log.info("sendSavedBdPdMsg**************json 实体***********：" + requestData.getData());
        //消息推送
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    public void sendUpdateBdPdMsg(BdPd bdPdOld,BdPd bdPd,String status){
        BdPdMsg bdPdMsg = new BdPdMsg();
        bdPdMsg.setResourceType("Medication");
        bdPdMsg.setImplicitRules("MDMYPXG");
        String id = NHISUUID.getKeyId();
        bdPdMsg.setId(id);
        if("_UPDATE".equals(status)){
            //更新
            BdPd changePd = ZsphMsgUtils.compareFields(bdPdOld,bdPd,new String[]{"ts"});
            if(changePd == null){
                return;
            }
            //编码会改变所以用id
            bdPdMsg.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203 ","YPBH",null))) ,"id/drugno",bdPd.getPkPd(),"official",null)));
            bdPdMsg.setCode(new BdPdCoding(new Coding("code/drugcode",bdPd.getCode(),bdPd.getName())));
            bdPdMsg.setStatus(bdPd.getFlagStop());
            if(StringUtils.isNotBlank(changePd.getDtDosage())){
                BdDefdoc dtDosage = QueryUtils.getbdDefdocInfo("030400",changePd.getDtDosage());
                if(dtDosage == null){
                    throw new BusException("未获取到药品剂型!");
                }
                bdPdMsg.setForm(new Code(Arrays.asList(new Coding("code/formcode",bdPd.getDtDosage()==null?"":bdPd.getDtDosage(),dtDosage.getName()==null?"":dtDosage.getName()))));
                //bdPdMsg.setForm(Arrays.asList(new Coding("code/formcode",changePd.getDtDosage(),dtDosage.getName())));
            }

            BdUnit bdUnit = QueryUtils.queryUnitById(bdPd.getPkUnitPack());
            if(bdUnit!=null){
                bdPdMsg.setAmount(new MumeratorCode(new Mumerator(bdPd.getPackSize()==null?"":bdPd.getPackSize().toString(),bdUnit==null?"":bdUnit.getName())));
            }

            //厂商
            BdFactory bdFactory = getBdFactoryInfo(changePd.getPkFactory());
            String bdFactoryName = null;
            if(bdFactory != null){
                bdFactoryName = bdFactory.getName();
                bdPdMsg.setManufacturer(new Manufacturer(Arrays.asList(new Identifier(null,bdFactory==null?"":bdFactory.getCode())),bdFactoryName));
            }

            if(bdPd.getApprNo()!=null){
                bdPdMsg.setBatch(new Batch(bdPd.getApprNo(),bdPd.getDateValidReg()==null?"":DateUtils.formatDate(bdPd.getDateValidReg(), "yyyy-MM-dd")));
            }


            bdPdMsg.setExtension(new ArrayList<>());


            if(StringUtils.isNotBlank(changePd.getNameChem())){
                bdPdMsg.getExtension().add(new ExtensionPd("generic",changePd.getNameChem()));
            }
            if(StringUtils.isNotBlank(changePd.getNameGen())){
                bdPdMsg.getExtension().add(new ExtensionPd("chemical",changePd.getNameGen()));
            }
            if(StringUtils.isNotBlank(changePd.getSpec())) {
                bdPdMsg.getExtension().add(new ExtensionPd("spec",changePd.getSpec()));
            }
            BdItemHp bdItemHp = QueryUtils.getBdItemHpInfo(changePd.getPkPd());
            String type = null;
            if(bdItemHp != null){
                //his 0甲类 1乙类 2自费
                //平台 0 自费，1甲类，2乙类
                if("0".equals(bdItemHp.getEuLevel())) type = "1";
                if("1".equals(bdItemHp.getEuLevel())) type = "2";
                if("2".equals(bdItemHp.getEuLevel())) type = "0";
            }
            if(StringUtils.isNotBlank(type)){
                bdPdMsg.getExtension().add(new ExtensionPd("reimbursement_type",null,null,type));
            }
            String drugName =null;
            if("0".equals(changePd.getEuDrugtype())) drugName = "西药";
            if("1".equals(changePd.getEuDrugtype())) drugName = "成药";
            if("2".equals(changePd.getEuDrugtype())) drugName = "草药";
            if(StringUtils.isNotBlank(drugName)) {
                bdPdMsg.getExtension().add(new ExtensionPd("category", drugName));
                bdPdMsg.getExtension().add(new ExtensionPd("categoryclass", changePd.getEuDrugtype()));
            }
            if(StringUtils.isNotBlank(changePd.getNote())) {
                bdPdMsg.getExtension().add(new ExtensionPd("quality",changePd.getNote()));
            }
            if(StringUtils.isNotBlank(changePd.getShortName())) {
                bdPdMsg.getExtension().add(new ExtensionPd("engregular",changePd.getShortName()));
            }
            if(changePd.getPackSize() != null){
                bdPdMsg.getExtension().add(new ExtensionPd("basedose",changePd.getPackSize().toString()));
            }
            bdPdMsg.getExtension().add(new ExtensionPd("doseunit",bdUnit != null?bdUnit.getName():null));
            String nameUsecate = null;
            if("0".equals(changePd.getEuUsecate())) nameUsecate = "口服";
            if("1".equals(changePd.getEuUsecate())) nameUsecate = "外用";
            if("2".equals(changePd.getEuUsecate())) nameUsecate = "注射";
            if("3".equals(changePd.getEuUsecate())) nameUsecate = "大输液";
            if("9".equals(changePd.getEuUsecate())) nameUsecate = "其他";
            bdPdMsg.getExtension().add(new ExtensionPd("usage",nameUsecate));
            BdTermFreq bdTermFreq = QueryUtils.queryBdTermFreqByCode(changePd.getCodeFreq());
            if(bdTermFreq != null){
                bdPdMsg.getExtension().add(new ExtensionPd("frequency",bdTermFreq.getName()));
            }
            BdUnit Unit = QueryUtils.queryUnitById(changePd.getPkUnitDef());
            if(bdPd.getDosageDef() != null && Unit != null){
                bdPdMsg.getExtension().add(new ExtensionPd("oncedose",changePd.getDosageDef().toString()+Unit.getName()));
            }
            if(StringUtils.isNotBlank(changePd.getNote())) {
                bdPdMsg.getExtension().add(new ExtensionPd("caution",changePd.getNote()));
            }
            if(StringUtils.isNotBlank(changePd.getDtMade())){
                bdPdMsg.getExtension().add(new ExtensionPd("selfmadeflag",null,"1".equals(changePd.getDtMade())?true:false));
            }
            //bdPdMsg.getExtension().add(new ExtensionPd("octflag","",null));没有oct标志
            if(StringUtils.isNotBlank(changePd.getFlagSt())){
                bdPdMsg.getExtension().add(new ExtensionPd("skintestflag",null,"1".equals(changePd.getFlagSt())?true:false));
            }
            if(StringUtils.isNotBlank(changePd.getDtPois())){
                bdPdMsg.getExtension().add(new ExtensionPd("specialflag",null,!"00".equals(changePd.getDtPois())?true:false));
            }
            BdUnit unitMin =  QueryUtils.queryUnitById(changePd.getPkUnitMin());
            if(unitMin != null){
                bdPdMsg.getExtension().add(new ExtensionPd("basicpackingunit",unitMin.getName()));
            }
            if(StringUtils.isNotBlank(changePd.getFlagPrecious())){
                bdPdMsg.getExtension().add(new ExtensionPd("essentialdrug",null,"1".equals(changePd.getFlagPrecious())?true:false));
            }
            if(StringUtils.isNotBlank(changePd.getDtAbrd())){
                bdPdMsg.getExtension().add(new ExtensionPd("importflag",null,"2".equals(changePd.getDtAbrd())?true:false));
            }
            BdDefdoc dtSource = QueryUtils.getbdDefdocInfo("080015",bdPd.getEuSource());
            if(dtSource!=null){
                bdPdMsg.getExtension().add(new ExtensionPd("fsdrug",new Coding(bdPd.getEuSource()==null?"":bdPd.getEuSource(),dtSource==null?"":dtSource.getName())));
            }
            if(StringUtils.isNotBlank(changePd.getFlagPrecious())){
                bdPdMsg.getExtension().add(new ExtensionPd("preciousflag",null,"0".equals(bdPd.getFlagPrecious())?true:false));
            }
            BdPdAttDefine bdPdAtt = QueryUtils.getBdPdAttInfo(bdPd.getPkPd(),"0503");
            if(bdPdAtt!=null){
                bdPdMsg.getExtension().add(new ExtensionPd("assistflag",null,"1".equals(bdPdAtt.getValDef())?true:false));
            }
            if(StringUtils.isNotBlank(changePd.getEuPdprice())) {
                bdPdMsg.getExtension().add(new ExtensionPd("pricemode",bdPd.getEuPdprice()==null?"":bdPd.getEuPdprice()));
            }
            if(StringUtils.isNotBlank(changePd.getEuPap())) {
                bdPdMsg.getExtension().add(new ExtensionPd("calculatemode",bdPd.getEuPap()==null?"":bdPd.getEuPap()));
            }
            if(changePd.getPapRate()!=null||changePd.getAmtPap()!=null){
                bdPdMsg.getExtension().add(new ExtensionPd("quotaorratio",null,null,null,"0".equals(bdPd.getEuPap())?bdPd.getPapRate():bdPd.getAmtPap()));
            }
            if(changePd.getPrice()!=null) {
                bdPdMsg.getExtension().add(new ExtensionPd("retailprice",bdPd.getPrice()==null?"":bdPd.getPrice().toString()));
            }
            if(changePd.getValidCnt()!=null ||StringUtils.isNotBlank(changePd.getEuValidUnit())){
                bdPdMsg.getExtension().add(new ExtensionPd("validityDate",bdPd.getValidCnt()==null?"":bdPd.getValidCnt().toString()));
                bdPdMsg.getExtension().add(new ExtensionPd("validityUnit",bdPd.getEuValidUnit()==null?"":bdPd.getEuValidUnit()));
            }
        }else{
            //删除的时候将药品变为停用
            bdPdMsg.setIdentifier(Arrays.asList(new Identifier(new CodeableConcept(Arrays.asList(new Coding("http://hl7.org/fhir/v2/0203 ","YPBH",null))) ,"id/drugno",bdPd.getCode(),"official","")));
            if("1".equals(bdPd.getDelFlag())){
                bdPdMsg.setStatus("1");
            }
        }
        //创建推送消息格式，将组装消息实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(bdPdMsg))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(bdPd.getCode()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Medication").build();
        log.info("sendUpdateBdPdMsg**************json 实体***********：" + requestData.getData());
        //消息推送
        SendAndResolve.getInstance().send(HttpMethod.PUT,requestData);

    }


    /**
     * 发送频次信息
     * @param paramMap
     */
    public void sendBdTermFreqMsg(Map<String, Object> paramMap) {
        BdTermFreq freq = (BdTermFreq)MapUtils.getObject(paramMap,"freq");
        if(freq==null){
            throw new BusException("未获取到传入的频次对象");
        }
        List<BdTermFreq> bdTermFreqList = DataBaseHelper.queryForList(
                "select * from bd_term_freq where del_flag = '0' and code = ? and pk_org = ?",
                BdTermFreq.class, freq.getCode(), freq.getPkOrg());
        if(CollectionUtils.isEmpty(bdTermFreqList)){
            throw new BusException("依据Code未获取到频次信息:"+freq.getCode());
        }
        BdTermFreq bdTermFreq = bdTermFreqList.get(0);
        BdDataVo bdDataVo = new BdDataVo();
        String id = NHISUUID.getKeyId();
        bdDataVo.setId(id);
        bdDataVo.setImplicitRules("YZPCZD");
        bdDataVo.setResourceType("CodeSystem");
        Concept concept = new Concept();
        concept.setCode(bdTermFreq.getCode());
        concept.setDisplay(bdTermFreq.getName());
        List<Property> property = new ArrayList<>();
        property.add(new Property("isactive","0".equals(bdTermFreq.getDelFlag())?true:false));
        property.add(new Property("serialNo",bdTermFreq.getSortNo()!=null?bdTermFreq.getSortNo().toString():"",null,null,null,null));
        property.add(new Property("cycleType",bdTermFreq.getEuCycle(),null,null,null,null));
        property.add(new Property("cycleCount",bdTermFreq.getCntCycle()!=null?bdTermFreq.getCntCycle().toString():"",null,null,null,null));
        property.add(new Property("count",bdTermFreq.getCnt()!=null?bdTermFreq.getCnt().toString():"",null,null,null,null));
        property.add(new Property("description",bdTermFreq.getNote(),null,null,null,null));
        property.add(new Property("longFlag",bdTermFreq.getEuAlways(),null,null,null,null));
        concept.setProperty(property);
        bdDataVo.setConcept(Arrays.asList(concept));
       //创建推送消息格式，将组装消息实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(bdDataVo))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(bdTermFreq.getCode()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("CodeSystem").build();
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * 发送用法信息
     * @param paramMap
     */
    public void sendBdSupplyMsg(Map<String, Object> paramMap) {
        BdSupply supply = (BdSupply)MapUtils.getObject(paramMap,"supply");
        if(supply==null){
            throw new BusException("未获取到传入的用法对象");
        }
        List<BdSupply> bdSupplyList = DataBaseHelper.queryForList(
                "select * from bd_supply where code = ? and del_flag='0'",
                BdSupply.class, supply.getCode());
        if(CollectionUtils.isEmpty(bdSupplyList)){
            throw new BusException("依据Code未获取到频次信息:"+supply.getCode());
        }
        BdSupply bdSupply = bdSupplyList.get(0);
        BdDataVo bdDataVo = new BdDataVo();
        String id = NHISUUID.getKeyId();
        bdDataVo.setId(id);
        bdDataVo.setImplicitRules("YPYFZD");
        bdDataVo.setResourceType("CodeSystem");
        Concept concept = new Concept();
        concept.setCode(bdSupply.getCode());
        concept.setDisplay(bdSupply.getName());
        List<Property> property = new ArrayList<>();
        property.add(new Property("isactive","0".equals(bdSupply.getDelFlag())?true:false));
        property.add(new Property("serialNo",bdSupply.getSortNo()!=null?bdSupply.getSortNo().toString():"",null,null,null,null));
        property.add(new Property("inputCode",bdSupply.getSpcode(),null,null,null,null));
        concept.setProperty(property);
        bdDataVo.setConcept(Arrays.asList(concept));
        //创建推送消息格式，将组装消息实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(bdDataVo))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(bdSupply.getCode()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("CodeSystem").build();
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * 发送医疗组信息
     * @param paramMap
     */
    public void sendOrgDeptWgMsg(Map<String, Object> paramMap) {
        String pkWg = MapUtils.getString(paramMap,"pkWg");
        String status = MapUtils.getString(paramMap,"status");
        if(StringUtils.isBlank(pkWg)){
            throw new BusException("未获取到传入的医疗组主键pkWg");
        }
        OrgDeptWg orgDeptWg = DataBaseHelper.queryForBean(
                "select * from org_dept_wg  where pk_wg = ? ",
                OrgDeptWg.class, pkWg);
        if(orgDeptWg==null){
            throw new BusException("依据pk_wg未获取到医疗组信息:"+pkWg);
        }
        List<Map<String,Object>> orgDeptWgEmpList = DataBaseHelper.queryForList(
                "select emp.eu_role_pvpsn,emp.date_begin,emp.date_end,emp.del_flag,bd.code_emp from org_dept_wg_emp emp LEFT JOIN BD_OU_EMPLOYEE bd on BD.pk_emp=emp.pk_emp where emp.pk_wg = ?",pkWg);
        //如果不是发布状态不发消息
        if(!Constants.EU_STATUS_PUBLISH.equals(orgDeptWg.getEuStatus())){
            return;
        }
        String id = NHISUUID.getKeyId();
        OrgDeptWgMsgVo orgDeptWgMsgVo = new OrgDeptWgMsgVo();
        orgDeptWgMsgVo.setResourceType("Group");
        orgDeptWgMsgVo.setId(id);
        orgDeptWgMsgVo.setImplicitRules("YLZZD");
        orgDeptWgMsgVo.setIdentifier(Arrays.asList(new Identifier("groupCode",orgDeptWg.getCodeWg())));
        orgDeptWgMsgVo.setActive("0".equals(orgDeptWg.getDelFlag())?true:false);
        orgDeptWgMsgVo.setType("practitioner");
        orgDeptWgMsgVo.setName(orgDeptWg.getNameWg());
        Map<String,Object> dept =getDeptInfo(orgDeptWg.getPkDept());
        orgDeptWgMsgVo.setManagingEntity(new EntityWg("Organization",null,
                Arrays.asList(new Identifier("deptCode",dept !=null?MapUtils.getString(dept,"codeDept"):""))));
        List<Member> memberList = new ArrayList<>();
        for(Map<String,Object> emp:orgDeptWgEmpList){
            Member member = new Member();
            EntityWg entityWg = new EntityWg();
            entityWg.setResourceType("PractitionerRole");
            String roleNo = "";
            if("0".equals(MapUtils.getString(emp,"euRolePvpsn")))roleNo="主任";
            if("1".equals(MapUtils.getString(emp,"euRolePvpsn")))roleNo="主管";
            if("2".equals(MapUtils.getString(emp,"euRolePvpsn")))roleNo="住院";
            if("3".equals(MapUtils.getString(emp,"euRolePvpsn")))roleNo="进修";
            if("4".equals(MapUtils.getString(emp,"euRolePvpsn")))roleNo="实习";
            entityWg.setIdentifier(Arrays.asList(new Identifier("roleNo",roleNo)));
            entityWg.setPractitioner(Arrays.asList(new Identifier("employeeNo",MapUtils.getString(emp,"codeEmp"))));
            member.setEntity(entityWg);
            member.setPeriod(new Period(MapUtils.getObject(emp,"dateBegin")!=null?(Date) MapUtils.getObject(emp,"dateBegin"):null,MapUtils.getObject(emp,"dateEnd")!=null?(Date) MapUtils.getObject(emp,"dateEnd"):null));
            member.setInactive("1".equals(MapUtils.getString(emp,"delFlag"))?false:true);
            memberList.add(member);
        }
        orgDeptWgMsgVo.setMember(memberList);
        //创建推送消息格式，将组装消息实体转json
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(orgDeptWgMsgVo))
                .msgIndexData(MsgIndexData.newBuilder().codeOther(orgDeptWg.getCodeWg()).build())
                .urlType(EnumUrlType.BASE)
                .remoteMethod("Group").build();
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    public Map<String,Object> getDeptInfo(String pkDept){
        return DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = '"+pkDept+"'");
    }



}


