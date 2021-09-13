package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.codehaus.jackson.type.TypeReference;
import org.glassfish.grizzly.utils.ArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.vo.OrderCheckMsgVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.build.RequestBuild;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao.ZsrmCnSendMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.MsgIndexData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.SendAndResolve;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.BusinessBase;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Destination;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Specimen;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.ValuePeriod;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.BdExtension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Category;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Code;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.CodeLis;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Collection;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Encounter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Lis;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Locationlis;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.ReasonReference;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.RelevantHistory;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Ris;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.SpecimenLis;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Treatment;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.ConditionRecorder;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.PvDiagCondition;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ZsphPlatFormSendCnHandler {

    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

    @Resource
    private ZsrmCnSendMapper zsrmCnSendMapper;

    public void sendOpEmeOrdMag(Map<String,Object> paramMap){
        String pkPv = MapUtils.getString(paramMap,"pkPv");
        //先取消发送过加急的
        Map<String,Object> piMap = DataBaseHelper.queryForMap("select pi.code_op,pi.code_pi,pv.code_pv from PV_ENCOUNTER pv left join PI_MASTER pi on pv.pk_pi = pi.pk_pi where pv.pk_pv =?",pkPv);
        List<SysMsgRec> sysMsgRecRisList = DataBaseHelper.queryForList("select replace(replace(MSG_CONTENT,'\"status\":\"1\"','\"status\":\"8\"'),'\"implicitRules\":\"JCSQXZ\"','\"implicitRules\":\"JCSQGX\"') MSG_CONTENT  from SYS_MSG_REC where eu_eme = '02'  and  code_pv = ?",SysMsgRec.class,MapUtils.getString(piMap,"codePv"));
        List<SysMsgRec> sysMsgRecLisList = DataBaseHelper.queryForList("select replace(replace(MSG_CONTENT,'\"status\":\"1\"','\"status\":\"9\"'),'\"implicitRules\":\"JYSQXZ\"','\"implicitRules\":\"JYSQXG\"') MSG_CONTENT from SYS_MSG_REC where eu_eme = '03'  and  code_pv = ?",SysMsgRec.class,MapUtils.getString(piMap,"codePv"));
        MsgIndexData indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(piMap,"codeOp"))
                .codePi(MapUtils.getString(piMap,"codePi"))
                .codePv(MapUtils.getString(piMap,"codePv"))
                .build();
        //加急的申请单条发的
        Set<String> pkCnords = new HashSet<>();
        if(sysMsgRecRisList!=null&&sysMsgRecRisList.size()>0){
            for(SysMsgRec sys:sysMsgRecRisList){
                //判断做过的检查申请
                String json = sys.getMsgContent();
                Map<String,Object> requestBody = ZsphMsgUtils.fromJson(json, Map.class);
                Object objEntry = MapUtils.getObject(requestBody, "entry");
                List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
                String pkCnord = null;//申请单号
                //获取到检验数据节点
                List<Ris> risList = new ArrayList<>();
                entryList.stream().forEach(mp -> {
                    Map<String, Object> resourceMap = null;
                    if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                        risList.add(TransfTool.mapToBean(Ris.class, resourceMap));
                    }
                });
                for(Ris ris:risList){
                    //解析申请单主键
                    List<Identifier> identifier = ris.getIdentifier();
                    if(CollectionUtils.isNotEmpty(identifier)){
                        for(Identifier id :identifier){
                            if(id!=null && "id/applyDetailNo".equalsIgnoreCase(id.getSystem())){
                                pkCnord = id.getValue();
                                break;
                            }
                        }
                    }
                }

                //判断改申请单是否开始做了
                int count = DataBaseHelper.queryForScalar("select count(1) from CN_RIS_APPLY where pk_cnord=? and eu_status>2 ", Integer.class, pkCnord);
                if(count>0){
                    pkCnords.add(pkCnord);
                    continue;
                }
                RequestData requestData = RequestData.newBuilder()
                        .id(NHISUUID.getKeyId())
                        .data(sys.getMsgContent())
                        .msgIndexData(indexData)
                        .remoteMethod("ServiceRequest").build();
                log.info("检查申请构造完成id:{},data:{},remoteMethod:{}"
                        ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
                SendAndResolve.getInstance().send( HttpMethod.POST,requestData);
            }
        }
        if(sysMsgRecLisList!=null&&sysMsgRecLisList.size()>0){
            for(SysMsgRec sys:sysMsgRecLisList){
                //判断做过的检验申请
                String json = sys.getMsgContent();
                Map<String,Object> requestBody = ZsphMsgUtils.fromJson(json, Map.class);
                Object objEntry = MapUtils.getObject(requestBody, "entry");
                List<Map<String,Object>> entryList = (List<Map<String,Object>>) objEntry;
                String codeApply = null;//申请单号
                //获取到检验数据节点
                List<Lis> lisList = new ArrayList<>();
                entryList.stream().forEach(mp -> {
                    Map<String, Object> resourceMap = null;
                    if (mp != null && (resourceMap = (Map<String, Object>) mp.get("resource")) != null && StringUtils.isNotBlank(MapUtils.getString(resourceMap, "implicitRules"))) {
                        lisList.add(TransfTool.mapToBean(Lis.class, resourceMap));
                    }
                });
                for(Lis lis:lisList){
                    //解析申请单主键
                    List<Identifier> identifier = lis.getIdentifier();
                    if(CollectionUtils.isNotEmpty(identifier)){
                        for(Identifier id :identifier){
                            if(id!=null && "id/applyno".equalsIgnoreCase(id.getSystem())){
                                codeApply = id.getValue();
                                break;
                            }
                        }
                    }
                }

                //判断改申请单是否开始做了
                List<Map<String,Object>> listMap = DataBaseHelper.queryForList("select ord.pk_cnord FROM CN_LAB_APPLY lab inner join CN_ORDER ord on lab.PK_CNORD=ord.PK_CNORD where ord.CODE_APPLY=? and lab.eu_status>2", codeApply);
                if(listMap!=null&&listMap.size()>0){
                    pkCnords.add(MapUtils.getString(listMap.get(0),"pkCnord"));
                    continue;
                }
                RequestData requestData = RequestData.newBuilder()
                        .id(NHISUUID.getKeyId())
                        .data(sys.getMsgContent())
                        .msgIndexData(indexData)
                        .remoteMethod("ServiceRequest").build();
                log.info("检验申请构造完成id:{},data:{},remoteMethod:{}"
                        ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
                SendAndResolve.getInstance().send( HttpMethod.POST,requestData);
            }
        }
        StringBuilder sql=new StringBuilder("SELECT distinct(co.pk_cnord),co.pk_ord,co.code_supply,co.pk_dept_exec,co.pk_pv,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,");
        sql.append("dt.PK_SETTLE,bd4.code_emp code_emp_cg,dt.name_emp_cg,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer,");
        sql.append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,pm.code_op,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input, ");
        sql.append("(select sum(AMOUNT) from BL_OP_DT dt where co.PK_CNORD=dt.PK_CNORD group by co.PK_CNORD) price_cg, ");
        sql.append("(select dt_medicaltype from BD_OU_DEPT dept where dept.PK_DEPT=co.pk_dept) dt_medicaltype  ");
        sql.append("FROM cn_order co LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord ");
        sql.append("LEFT JOIN BD_OU_EMPLOYEE bd4 on BD4.pk_emp=dt.pk_emp_cg LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where co.flag_rescue='1' and co.pk_pv = '"+pkPv+"' ");
        if(pkCnords.size()>0){
            sql.append(" and co.pk_cnord not in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")");
        }
        List<Map<String,Object>> mapList= DataBaseHelper.queryForList(sql.toString());
        //发送检查检验确认消息
        sendOpApplyEnsureMsg(mapList,EnumerateParameter.ZERO,true);
    }
    /**
     * 门诊收费时，发送检查、检验，结算消息
     * @param paramMap
     * @throws Exception
     */
    public void sendBlOpSettleMsg(Map<String,Object> paramMap){
        String Control =  SDMsgUtils.getPropValueStr(paramMap,"Control");
        List<Map<String,Object>> opDtList = SDMsgUtils.lisBToLisMap((List<Object>)paramMap.get("blOpDts"));
        String pkCnords="";
        String pkCgops="";
        if(opDtList.size()>0){
            for (int i = 0; i < opDtList.size(); i++) {
                if(i==opDtList.size()-1){
                    if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop"))){
                        pkCgops+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop")+"'";
                    }else if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord"))){
                        pkCnords+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord")+"'";
                    }

                }else{
                    if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop"))){
                        pkCgops+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop")+"',";
                    }else if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord"))){
                        pkCnords+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord")+"',";
                    }

                }
            }
        }

        StringBuilder sql=new StringBuilder("SELECT distinct(co.pk_cnord),co.pk_ord,co.code_supply,co.pk_dept_exec,co.pk_pv,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,");
        sql.append("dt.PK_SETTLE,bd4.code_emp code_emp_cg,dt.name_emp_cg,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer,");
        sql.append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,pm.code_op,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input, ");
        sql.append("(select sum(AMOUNT) from BL_OP_DT dt where co.PK_CNORD=dt.PK_CNORD group by co.PK_CNORD) price_cg, ");
        sql.append("(select dt_medicaltype from BD_OU_DEPT dept where dept.PK_DEPT=co.pk_dept) dt_medicaltype  ");
        sql.append("FROM cn_order co LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord ");
        sql.append("LEFT JOIN BD_OU_EMPLOYEE bd4 on BD4.pk_emp=dt.pk_emp_cg LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 ");
        //收费的时候只发没有加急的，但是退费的时候需要把加急和未加急的都取消
        if(Control.equals("OK")){
            //门诊收费发送新增消息
           sql.append(" and (co.flag_rescue !='1' or co.flag_rescue is null or co.flag_rescue ='') ");
        }
        if(!pkCnords.equals("")){
            sql.append("and co.pk_cnord  in ("+pkCnords+")");
        }
        if(!pkCgops.equals("")){
            sql.append("and dt.pk_cgop  in ("+pkCgops+")");
        }

        List<Map<String,Object>> mapList= DataBaseHelper.queryForList(sql.toString());
        String isAdd="";
        if(Control.equals("OK")){
            //门诊收费发送新增消息
            isAdd = EnumerateParameter.ZERO;
        }else if(Control.equals("CR")){
            //退费退费发送更新消息
            isAdd = EnumerateParameter.ONE;
        }else{
            throw new BusException("消息发送失败！门诊收费和退费时无Control值");
        }
        //发送检查检验确认消息
        sendOpApplyEnsureMsg(mapList,isAdd,false);
        //发送体检结算信息
        settlerebackOpcg(isAdd,pkCgops,pkCnords);
        //推送计费信息--HIS计费或冲账（逆向业务）
        chargeItem(isAdd,pkCgops,pkCnords);
    }

    //发送
    public void settlerebackOpcg(String isAdd,String pkCgops,String pkCnords){
        if(StringUtils.isBlank(pkCnords)&&StringUtils.isBlank(pkCgops)){
            throw new BusException("未获取到计费主键！！");
        }
        StringBuilder sql = new StringBuilder("select pv.CODE_PV,pv.EU_PVTYPE,pi.code_op,pi.code_pi from BL_OP_DT bl inner join PV_ENCOUNTER pv on pv.pk_pv = bl.PK_PV inner join PI_MASTER pi on pv.PK_PI = pi.PK_PI where 1=1 ") ;
        if(StringUtils.isNotBlank(pkCnords)){
            sql.append("and bl.pk_cnord  in ("+pkCnords+")");
        }
        if(StringUtils.isNotBlank(pkCgops)){
            sql.append("and bl.pk_cgop  in ("+pkCgops+")");
        }
        List<Map<String,Object>> mapList= DataBaseHelper.queryForList(sql.toString());
        StringBuilder sqlEmp = new StringBuilder("select op.PK_CGOp,emp.CODE_EMP from BL_OP_DT op left join BL_SETTLE bl on bl.PK_SETTLE = op.PK_SETTLE left join BD_OU_EMPLOYEE emp on emp.PK_EMP=bl.pk_emp_st where 1=1 ") ;
        if(StringUtils.isNotBlank(pkCnords)){
            sqlEmp.append("and op.pk_cnord  in ("+pkCnords+")");
        }
        if(StringUtils.isNotBlank(pkCgops)){
            sqlEmp.append("and op.pk_cgop  in ("+pkCgops+")");
        }
        List<Map<String,Object>> mapEmpList= DataBaseHelper.queryForList(sqlEmp.toString());
        if(mapList!=null&&mapList.get(0)!=null){
            String euPvtype = MapUtils.getString(mapList.get(0),"euPvtype");
            if("4".equals(euPvtype)){
                boolean flagAdd = EnumerateParameter.ZERO.equals(isAdd);
                String id = NHISUUID.getKeyId();
                BusinessBase businessBase = new BusinessBase();
                businessBase.setResourceType("Bundle");
                businessBase.setId(id);
                businessBase.setType("message");
                businessBase.setTimestamp(new Date());
                businessBase.setEntry(new ArrayList<>());
                Entry entry = new Entry();
                entry.setResource(new PhResource());
                PhResource phResource = entry.getResource();
                phResource.setResourceType("MessageHeader");
                phResource.setId(NHISUUID.getKeyId());
                phResource.setDestination(Arrays.asList(new Destination("PT")));
                phResource.setSource(new Destination("HIS"));
                businessBase.getEntry().add(entry);
                Entry entryCg = new Entry();
                entryCg.setResource(new PhResource());
                BlSettle dataResource = new BlSettle();
                if(flagAdd){
                    dataResource.setResourceType("settleOpcg");
                    dataResource.setImplicitRules("settleOpcg");
                }else{
                    dataResource.setResourceType("rebackOpcg");
                    dataResource.setImplicitRules("rebackOpcg");
                }
                dataResource.setCodePv(MapUtils.getString(mapList.get(0),"codePv",""));
                dataResource.setEuStatus("1");
                dataResource.setPatientId(MapUtils.getString(mapList.get(0),"codeOp",""));
                dataResource.setPatientType("1");
                if(mapEmpList!=null&&mapEmpList.get(0)!=null){
                    dataResource.setChargeOperator(MapUtils.getString(mapEmpList.get(0),"codeEmp"));
                }else{
                    dataResource.setChargeOperator("");
                }
                entryCg.setResource(dataResource);
                businessBase.getEntry().add(entryCg);
                MsgIndexData indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(mapList.get(0),"codeOp"))
                            .codePi(MapUtils.getString(mapList.get(0),"codePi"))
                            .codePv(MapUtils.getString(mapList.get(0),"codePv"))
                            .build();
                RequestData requestData = RequestData.newBuilder()
                        .id(id)
                        .data(JSON.toJSONString(businessBase))
                        .msgIndexData(indexData)
                        .remoteMethod("settlerebackOpcg").build();
                log.info("体检结算构造完成id:{},data:{},remoteMethod:{}"
                        ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
                SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
            }

        }

    }
    
    //发送计费信息
    public void chargeItem(String isAdd,String pkCgops,String pkCnords) {
        List<Map<String, Object>> mapList = zsrmCnSendMapper.getChargeInfo(pkCnords, pkCgops);
        if (CollectionUtils.isNotEmpty(mapList)) {
            //boolean flagAdd = EnumerateParameter.ZERO.equals(isAdd);//退费先不发，不知道传什么字段让第三方区分计费和退费

            String id = NHISUUID.getKeyId();
            BusinessBase businessBase = new BusinessBase();
            businessBase.setResourceType("Bundle");
            businessBase.setId(id);
            businessBase.setType("message");
            businessBase.setTimestamp(new Date());
            businessBase.setEntry(new ArrayList<>());
            Entry entry = new Entry();
            entry.setResource(new PhResource());
            PhResource phResource = entry.getResource();
            phResource.setResourceType("MessageHeader");
            phResource.setId(NHISUUID.getKeyId());
            phResource.setDestination(Arrays.asList(new Destination("PT")));
            phResource.setSource(new Destination("HIS"));
            businessBase.getEntry().add(entry);
            for (Map<String, Object> map : mapList) {
                Entry entryCg = new Entry();
                entryCg.setResource(new PhResource());
                BlCharge charge = new BlCharge();
                charge.setResourceType("ChargeItem");
                charge.setId(NHISUUID.getKeyId());
                charge.setImplicitRules("JFXX");
                charge.setIdentifier(new ArrayList<>());
                charge.getIdentifier().add(new Identifier("barCode", CommonUtils.getPropValueStr(map, "barcode")));
                charge.setStatus("billed");

                BlSubject subject = new BlSubject();
                subject.setBirthDate(CommonUtils.getPropValueStr(map, "birthDate"));
                subject.setName(Arrays.asList(new TextElement(CommonUtils.getPropValueStr(map, "namePi"))));
                subject.setIdentifier(new Identifier("patientid", CommonUtils.getPropValueStr(map, "codeOp")));

                charge.setSubject(subject);

                BlEncounter encounter = new BlEncounter();

                String euPvtype = CommonUtils.getPropValueStr(map, "euPvtype");
                if ("1".equals(euPvtype)) {
                    encounter.setClas(new BlClas("AMB", "门诊"));
                } else if ("2".equals(euPvtype)) {
                    encounter.setClas(new BlClas("EMER", "急诊"));
                } else if ("3".equals(euPvtype)) {
                    encounter.setClas(new BlClas("IMP", "住院"));
                } else if ("4".equals(euPvtype)) {
                    encounter.setClas(new BlClas("PHY", "体检"));
                } else {
                    encounter.setClas(new BlClas("AMB", "门诊"));
                }

                BlDiagnosis diagnosis = new BlDiagnosis();
                diagnosis.setCode(new CodeableConcept(Arrays.asList(new Coding(CommonUtils.getPropValueStr(map, "codeIcd"), CommonUtils.getPropValueStr(map, "nameDiag")))));
                encounter.setDiagnosis(Arrays.asList(diagnosis));
                encounter.setIdentifier(new Identifier("visitno", CommonUtils.getPropValueStr(map, "codeOp")));

                BlLocation location = new BlLocation();
                location.setName(CommonUtils.getPropValueStr(map, "nameDept"));
                location.setIdentifier(Arrays.asList(new Identifier("dept", CommonUtils.getPropValueStr(map, "codeDept"))));
                encounter.setLocation(location);
                charge.setEncounter(encounter);

                charge.setOccurrenceDateTime(CommonUtils.getPropValueStr(map, "dateSt"));

                BlBasedOn basedOn = new BlBasedOn();
                basedOn.setCategory(Arrays.asList(new BlCategory(Arrays.asList(new Coding(CommonUtils.getPropValueStr(map, "codeCate"), CommonUtils.getPropValueStr(map, "nameCate"))))));
                basedOn.setIdentifier(Arrays.asList(new Identifier("orderNo", CommonUtils.getPropValueStr(map, "ordsn"))));
                basedOn.setCode(new BlCodeableConcept(CommonUtils.getPropValueStr(map, "spec"), Arrays.asList(new BlCoding(CommonUtils.getPropValueStr(map, "codeItem"), CommonUtils.getPropValueStr(map, "nameItem")))));
                charge.setBasedOn(Arrays.asList(basedOn));

                charge.setEnterer(new BlEnterer(new TextElement(CommonUtils.getPropValueStr(map, "nameEmpApp")),
                        Arrays.asList(new Identifier("operator", CommonUtils.getPropValueStr(map, "codeEmpApp")))));

                charge.setEnteredDate(CommonUtils.getPropValueStr(map, "dateSt"));

                charge.setSupportingInformation(new ArrayList<>());
                charge.getSupportingInformation().add(new BlEnterer(CommonUtils.getPropValueStr(map, "nameDeptApp")
                        , Arrays.asList(new Identifier("enterDept", CommonUtils.getPropValueStr(map, "codeDeptApp")))));
                charge.getSupportingInformation().add(new BlEnterer(Arrays.asList(new TextElement(CommonUtils.getPropValueStr(map, "nameEmpApp")))
                        , Arrays.asList(new Identifier("enterDoct", CommonUtils.getPropValueStr(map, "codeEmpApp")))));

                BlLocation actor = new BlLocation();
                actor.setName(CommonUtils.getPropValueStr(map, "nameDeptEx"));
                actor.setIdentifier(Arrays.asList(new Identifier("execDept", CommonUtils.getPropValueStr(map, "codeDeptEx"))));
                charge.setPerformer(Arrays.asList(new BlPerformer(actor)));
                String quan ="";
                String codeSt="";
                if("0".equals(isAdd)) {
                    quan = CommonUtils.getPropValueStr(map, "quan");
                    codeSt=CommonUtils.getPropValueStr(map, "codeSt");
                }else{
                    quan = CommonUtils.getString(MathUtils.mul(MapUtils.getDouble(map, "quan"),-1D),"-1");
                    codeSt="RET"+CommonUtils.getPropValueStr(map, "codeSt");
                }
                charge.setQuantity(new BlQuantity(Double.valueOf(quan), CommonUtils.getPropValueStr(map, "nameUnit")));
                charge.getIdentifier().add(new Identifier("receiveId",codeSt) );

                String price = CommonUtils.getPropValueStr(map, "price");
                charge.setPriceOverride(new BlPriceOverride(Double.valueOf(price)));
                entryCg.setResource(charge);
                businessBase.getEntry().add(entryCg);
            }
            MsgIndexData indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(mapList.get(0), "codeOp"))
                    .codePi(MapUtils.getString(mapList.get(0), "codePi"))
                    .codePv(MapUtils.getString(mapList.get(0), "codePv"))
                    .build();
            RequestData requestData = RequestData.newBuilder()
                    .id(id)
                    .data(JSON.toJSONString(businessBase))
                    .msgIndexData(indexData)
                    .remoteMethod("ChargeItem").build();
            log.info("收费/退费构造完成id:{},data:{},remoteMethod:{}"
                    , requestData.getId(), requestData.getData(), requestData.getRemoteMethod());
            SendAndResolve.getInstance().send(HttpMethod.POST, requestData);

        }


    }

    /**
     *
     * @param mapList
     * @param isAdd
     * @param flag 是否急
     */
    public void sendOpApplyEnsureMsg  (List<Map<String,Object>> mapList,String isAdd,boolean flag){
        List<Map<String,Object>> risMap = new ArrayList<>();//检查
        List<Map<String,Object>> treatmentMap = new ArrayList<>();//治疗
        for (Map<String, Object> map : mapList) {
            if(map.get("codeOrdtype")!=null){
                map.put("isAdd",isAdd);
                String codeOrdtype = map.get("codeOrdtype").toString().substring(0,2);
                if("02".equals(codeOrdtype)){
                    if("0207".equals(map.get("codeOrdtype"))){//心电不合单，单条发送
                        sendOpApplyRisMsg(Arrays.asList(map),flag);
                    }else{
                        //加急的单条发送，因为要发取消
                        if(flag){
                            sendOpApplyRisMsg(Arrays.asList(map),flag);
                        }else{
                            risMap.add(map);
                        }
                    }
                    //区分检查病理的申请单发送检验申请
                    int count = DataBaseHelper.queryForScalar("select count(1) from bd_ord ord "
                            + "where ord.pk_ord = ? and exists (select 1  from bd_dictattr_temp tmp inner join bd_dictattr att on tmp.pk_dictattrtemp = att.pk_dictattrtemp" +
                            " where tmp.dt_dicttype = '02' and att.VAL_ATTR = '1' and tmp.code_attr = '0209' and ord.pk_ord = att.pk_dict)",Integer.class,map.get("pkOrd"));
                    if(count>0){
                        sendOpApplyLisMsg(Arrays.asList(map),false,flag);
                    }
                }
                if("03".equals(codeOrdtype)){
                    sendOpApplyLisMsg(Arrays.asList(map),true,flag);
                }
                if("05".equals(codeOrdtype)){
                    treatmentMap.add(map);
                }
            }
        }
        //治疗多条发送
        if(treatmentMap.size()>0){
            sendOpApplyTreatmentMsg(treatmentMap);
        }
        //检查多条发送
        if(risMap.size()>0){
            sendOpApplyRisMsg(risMap,flag);
        }
    }

    public void sendOpApplyTreatmentMsg(List<Map<String, Object>> map){
        boolean flagAdd = EnumerateParameter.ZERO.equals(MapUtils.getString(map.get(0), "isAdd","1"));
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setId(id);
        businessBase.setType("message");
        businessBase.setTimestamp(new Date());
        businessBase.setEntry(new ArrayList<>());
        Entry entry = new Entry();
        entry.setResource(new PhResource());
        PhResource phResource = entry.getResource();
        phResource.setResourceType("MessageHeader");
        phResource.setId(NHISUUID.getKeyId());
        phResource.setDestination(Arrays.asList(new Destination("PT")));
        phResource.setSource(new Destination("HIS"));
        businessBase.getEntry().add(entry);
        List<String> pkCnords = new ArrayList<>();
        for(Map<String, Object> m:map){
            pkCnords.add(MapUtils.getString(m,"pkCnord"));
        }
        List<Map<String,Object>> treatList =  zsrmCnSendMapper.getTreatmentInfo(pkCnords);
        MsgIndexData indexData = null;
        if(treatList != null) {
            for (Map<String, Object> treat : treatList) {
                Entry entryTreatment = new Entry();
                Treatment treatment = new Treatment();
                treatment.setResourceType("Procedure");
                treatment.setIdentifier(new ArrayList<>());
                treatment.getIdentifier().add(new Identifier("applyNo",MapUtils.getString(treat,"codeApply","")));//申请单号
                treatment.getIdentifier().add(new Identifier("id/applyDetailNo",MapUtils.getString(treat,"pkCnord")));//医嘱号
                treatment.setImplicitRules("ZLSQD");
                treatment.setExtension(new ArrayList<>());
                if (flagAdd) {
                    treatment.getExtension().add(new Extension("feeStatus",1));//已收费
                    treatment.setStatus("1");
                } else {
                    treatment.getExtension().add(new Extension("feeStatus",0));//为收费
                    treatment.setStatus("8");
                }
                String codeOrdtype = MapUtils.getString(treat,"codeOrdtype");
                if(StringUtils.isNotBlank(codeOrdtype)){
                    treatment.setCategory(Arrays.asList(new Category(Arrays.asList(new Coding("applyTypeCode",codeOrdtype.substring(codeOrdtype.length() -2,codeOrdtype.length()),MapUtils.getString(treat,"nameOrdtype",""))))));
                }else{
                    throw new BusException("未维护医嘱的类型！！");
                }
                List<BdExtension> bdExtension = new ArrayList<>();
                bdExtension.add(new BdExtension("amount",new BigDecimal(MapUtils.getString(treat,"quanCg"))));
                bdExtension.add(new BdExtension("price",new BigDecimal(MapUtils.getString(treat,"priceCg"))));
                bdExtension.add(new BdExtension("usage",MapUtils.getString(treat,"nameSupply"),null));
                bdExtension.add(new BdExtension("frequency",MapUtils.getString(treat,"codeFreq"),null));
                bdExtension.add(new BdExtension("day",new BigDecimal(MapUtils.getString(treat,"days"))));
                bdExtension.add(new BdExtension("money",new BigDecimal(MapUtils.getString(treat,"quanCg")).multiply(new BigDecimal(MapUtils.getString(treat,"priceCg")))));
                treatment.setCode(new Category(Arrays.asList(new Coding("applyItem",MapUtils.getString(treat,"codeName",""),MapUtils.getString(treat,"nameOrd",""),bdExtension))));

                //患者信息
                Locationlis location = new Locationlis();
                location.setResourceType("Patient");
                location.setIdentifier(new Identifier("patientid",MapUtils.getString(treat,"codeOp","")));
                location.setName(Arrays.asList(new TextElement(MapUtils.getString(treat,"namePi",""))));
                String dtSex = MapUtils.getString(treat,"dtSex");
                location.setGender("02".equals(dtSex)?"male":"03".equals(dtSex)?"female":"other");//患者性别
                //location.setBirthDate(MapUtils.getString(risInfo,"birthDate"));
                Date birthday = TransfTool.getPropValueDates(treat, "birthDate");
                location.setBirthDate(DateUtils.dateToStr("yyyy-MM-dd",birthday==null?new Date():birthday));//出生日期
                location.setTelecom(Arrays.asList(new Identifier("phone",MapUtils.getString(treat,"mobile",""))));
                location.setAddress(Arrays.asList(new TextElement(MapUtils.getString(treat,"addrCurDt",""))));
                treatment.setSubject(location);

                //就诊类别
                Encounter encounter = new Encounter();
                encounter.setResourceType("Encounter");
                if(EnumerateParameter.ONE.equals(MapUtils.getString(treat,"euPvtype"))||EnumerateParameter.TWO.equals(MapUtils.getString(treat,"euPvtype"))){
                    encounter.setClas(new Coding("AMB","门诊"));
                }else if(EnumerateParameter.THREE.equals(MapUtils.getString(treat,"euPvtype"))){
                    encounter.setClas(new Coding("IMP","住院"));
                }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(treat,"euPvtype"))){
                    encounter.setClas(new Coding("PHY","体检"));
                }
                encounter.setIdentifier(new Identifier("id/visitno",MapUtils.getString(treat,"codePv")));
                treatment.setEncounter(encounter);

                //执行科室
                treatment.setPerformer(new ArrayList<>());
                Locationlis locationlis1 = new Locationlis();
                locationlis1.setResourceType("Practitioner");
                locationlis1.setIdentifier(new Identifier("code/exdeptcode",MapUtils.getString(treat,"codeDeptExec","")));
                locationlis1.setName(MapUtils.getString(treat,"nameDeptExec",""));
                treatment.getPerformer().add(locationlis1);

                //科室
                List<Code> code = new ArrayList<>();
                Map<String,Object> dept = getDeptInfo(MapUtils.getString(treat,"pkDept"));
                code.add(new Code(Arrays.asList(new Coding("code/reqdeptcode",MapUtils.getString(dept,"codeDept",""),MapUtils.getString(dept,"nameDept","")))));
                //住院用
                //Map<String,Object> deptNs = getDeptInfo(MapUtils.getString(treat,"pkDeptNs"));
                code.add(new Code(Arrays.asList(new Coding("code/wardcode",MapUtils.getString(treat,"codeDeptArea",""),MapUtils.getString(treat,"nameDeptArea","")))));
                //code.add(new Code(Arrays.asList(new Coding("code/bodno",MapUtils.getString(deptNs,"bedNo",""),MapUtils.getString(deptNs,"bedNo","")))));
                treatment.setLocationCode(code);

                //诊断
                treatment.setReasonReference(new ArrayList<>());
                ReasonReference reasonReference = new ReasonReference();
                reasonReference.setResourceType("Condition");
                reasonReference.setCode(new Code(Arrays.asList(new Coding("diagnosis",MapUtils.getString(treat,"codeIcd",""),MapUtils.getString(treat,"nameDiag","")))));
                treatment.getReasonReference().add(reasonReference);
                treatment.setNote(Arrays.asList(new TextElement(MapUtils.getString(treat,"noteOrd",""))));
                entryTreatment.setResource(treatment);
                businessBase.getEntry().add(entryTreatment);
                if(indexData == null){
                    indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(treat,"codeOp"))
                            .codePi(MapUtils.getString(treat,"codePi"))
                            .codePv(MapUtils.getString(treat,"codePv"))
                            .build();
                }
            }
        }else{
            throw new BusException("未获取到检查信息！！");
        }
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(indexData)
                .remoteMethod("Procedure").build();
        log.info("治疗申请构造完成id:{},data:{},remoteMethod:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);

    }

        //检查发送
    public  void sendOpApplyRisMsg(List<Map<String, Object>> map,boolean flag){
        boolean flagAdd = EnumerateParameter.ZERO.equals(MapUtils.getString(map.get(0), "isAdd","1"));
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setId(id);
        businessBase.setType("message");
        businessBase.setTimestamp(new Date());
        businessBase.setEntry(new ArrayList<>());
        Entry entry = new Entry();
        entry.setResource(new PhResource());
        PhResource phResource = entry.getResource();
        phResource.setResourceType("MessageHeader");
        phResource.setId(NHISUUID.getKeyId());
        phResource.setDestination(Arrays.asList(new Destination("PT")));
        phResource.setSource(new Destination("HIS"));
        businessBase.getEntry().add(entry);
        List<String> pkCnords = new ArrayList<>();
        for(Map<String, Object> m:map){
            pkCnords.add(MapUtils.getString(m,"pkCnord"));
        }
        List<Map<String,Object>> risList =  zsrmCnSendMapper.getRisInfo(pkCnords);
        MsgIndexData indexData = null;
        if(risList != null){
            for(Map<String,Object> risInfo : risList){
                Entry entryRis = new Entry();
                Ris ris = new Ris();
                ris.setResourceType("ServiceRequest");
                ris.setIdentifier(new ArrayList<>());
                ris.getIdentifier().add(new Identifier("id/applyno",MapUtils.getString(risInfo,"codeApply")));//申请单号
                ris.getIdentifier().add(new Identifier("id/applyDetailNo",MapUtils.getString(risInfo,"pkCnord")));//医嘱号
                ris.setIntent("order");

                String codeOrdtype = MapUtils.getString(risInfo,"codeOrdtype");
                if(StringUtils.isNotBlank(codeOrdtype)){
                    ris.setCategory(Arrays.asList(new Category(Arrays.asList(new Coding("applyTypeCode",codeOrdtype.substring(codeOrdtype.length() -2,codeOrdtype.length()),MapUtils.getString(risInfo,"nameOrdtype",""))))));
                }else{
                    throw new BusException("未维护医嘱的类型！！");
                }

                List<BdExtension> bdExtension = new ArrayList<>();
                bdExtension.add(new BdExtension("amount",new BigDecimal(MapUtils.getString(risInfo,"quanCg","1"))));
                bdExtension.add(new BdExtension("price",new BigDecimal(MapUtils.getString(risInfo,"priceCg"))));
                ris.setCode(new Category(Arrays.asList(new Coding("applyItem",MapUtils.getString(risInfo,"codeName",""),MapUtils.getString(risInfo,"nameOrd",""),bdExtension))));

                Locationlis location = new Locationlis();
                location.setResourceType("Patient");
                location.setIdentifier(new Identifier("patientid",MapUtils.getString(risInfo,"codeOp","")));
                location.setName(Arrays.asList(new TextElement(MapUtils.getString(risInfo,"namePi",""))));
                String dtSex = MapUtils.getString(risInfo,"dtSex");
                location.setGender("02".equals(dtSex)?"male":"03".equals(dtSex)?"female":"other");//患者性别
                //location.setBirthDate(MapUtils.getString(risInfo,"birthDate"));
                Date birthday = TransfTool.getPropValueDates(risInfo, "birthDate");
                location.setBirthDate(DateUtils.dateToStr("yyyy-MM-dd",birthday==null?new Date():birthday));//出生日期
                location.setTelecom(Arrays.asList(new Identifier("phone",MapUtils.getString(risInfo,"mobile",""))));
                location.setAddress(Arrays.asList(new TextElement(MapUtils.getString(risInfo,"addrCurDt",""))));
                location.setFlagSpec(MapUtils.getString(risInfo,"flagSpec"));//特诊标志
                ris.setSubject(location);

                //就诊类别
                Encounter encounter = new Encounter();
                encounter.setResourceType("Encounter");
                if(EnumerateParameter.ONE.equals(MapUtils.getString(risInfo,"euPvtype"))||EnumerateParameter.TWO.equals(MapUtils.getString(risInfo,"euPvtype"))){
                    encounter.setClas(new Coding("AMB","门诊"));
                }else if(EnumerateParameter.THREE.equals(MapUtils.getString(risInfo,"euPvtype"))){
                    encounter.setClas(new Coding("IMP","住院"));
                }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(risInfo,"euPvtype"))){
                    encounter.setClas(new Coding("PHY","体检"));
                }
                encounter.setIdentifier(new Identifier("id/visitno",MapUtils.getString(risInfo,"codePv")));
                //encounter.setName(new TextElement(MapUtils.getString(cn,"namePi")));
                ris.setEncounter(encounter);

                if(StringUtils.isNotBlank(ZsphMsgUtils.getPropValueStr(risInfo,"dateEnter"))){
                    try {
                        ris.setAuthoredOn(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(ZsphMsgUtils.getPropValueStr(risInfo,"dateEnter")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    ris.setAuthoredOn(null);
                }

                //申请医生
                Locationlis locationlisReq = new Locationlis();
                locationlisReq.setResourceType("Practitioner");
                locationlisReq.setIdentifier(new Identifier("id/applyDoc",MapUtils.getString(risInfo,"codeEmp","")));
                locationlisReq.setName(Arrays.asList(new TextElement(MapUtils.getString(risInfo,"nameEmp",""))));
                ris.setRequester(locationlisReq);

                //核对人
                ris.setPerformer(new ArrayList<>());
                Locationlis locationlis = new Locationlis();
                locationlis.setResourceType("Practitioner");
                locationlis.setIdentifier(new Identifier("id/verifyDoc",MapUtils.getString(map.get(0),"codeEmpCg","")));
                locationlis.setName(Arrays.asList(new TextElement(MapUtils.getString(map.get(0),"nameEmpCg",""))));
                Locationlis locationlis1 = new Locationlis();
                locationlis1.setResourceType("Practitioner");
                locationlis1.setIdentifier(new Identifier("code/exdeptcode",MapUtils.getString(risInfo,"codeDeptExec","")));
                locationlis1.setName(MapUtils.getString(risInfo,"nameDeptExec",""));
                ris.getPerformer().add(locationlis);
                ris.getPerformer().add(locationlis1);

                //ris.setLocationCode(new ArrayList<>());
                List<Code> code = new ArrayList<>();
                Map<String,Object> dept = getDeptInfo(MapUtils.getString(risInfo,"pkDept"));
                //ris.setLocationCode();
                code.add(new Code(Arrays.asList(new Coding("code/reqdeptcode",MapUtils.getString(dept,"codeDept",""),MapUtils.getString(dept,"nameDept","")))));
                //住院用
                if(EnumerateParameter.THREE.equals(MapUtils.getString(risInfo,"euPvtype"))){
                    Map<String,Object> deptNs = getDeptInfo(MapUtils.getString(risInfo,"pkDeptNs"));
                    code.add(new Code(Arrays.asList(new Coding("code/wardcode",MapUtils.getString(deptNs,"codeDept",""),MapUtils.getString(deptNs,"nameDept","")))));
                }else {
                    code.add(new Code(Arrays.asList(new Coding("code/wardcode",MapUtils.getString(risInfo,"codeDeptArea",""),MapUtils.getString(risInfo,"nameDeptArea","")))));
                }
                //ris.getLocationCode().add();
                //ris.getLocationCode().add();
                //code.add(new Code(Arrays.asList(new Coding("code/bodno",MapUtils.getString(deptNs,"bedNo",""),MapUtils.getString(deptNs,"bedNo","")))));
                ris.setLocationCode(code);

                ris.setReasonReference(new ArrayList<>());
                ReasonReference reasonReference = new ReasonReference();
                reasonReference.setResourceType("Condition");
                reasonReference.setCode(new Code(Arrays.asList(new Coding("diagnosis",MapUtils.getString(risInfo,"codeIcd",""),MapUtils.getString(risInfo,"nameDiag","")))));
                ris.getReasonReference().add(reasonReference);

                //标本
                ris.setSpecimen(new ArrayList<>());
                Specimen specimen = new Specimen();
                specimen.setResourceType("Specimen");
                specimen.setType(new Code(Arrays.asList(new Coding("Specimen",MapUtils.getString(risInfo,"dtSamptype",""),MapUtils.getString(risInfo,"labName","")))));
                //specimen.setType(new Coding("Specimen",MapUtils.getString(risInfo,"dtSamptype",""),MapUtils.getString(risInfo,"labName","")));
                //specimen.setIdentifier(new Identifier("Specimen",MapUtils.getString(cn,"dtSamptype"),MapUtils.getString(cn,"labName"),"specimen"));
                ris.getSpecimen().add(specimen);
                ris.setBodySite(Arrays.asList(new Code(Arrays.asList(new Coding("bodySite",MapUtils.getString(risInfo,"descBody",""),MapUtils.getString(risInfo,"nameBody",""))))));
                ris.setNote(Arrays.asList(new TextElement(MapUtils.getString(risInfo,"note",""))));
                ris.setRelevantHistory(Arrays.asList(new RelevantHistory("Provenance",Arrays.asList(new TextElement(MapUtils.getString(risInfo,"noteDise",""))))));
                ris.setExtension(new ArrayList<>());
                if (flagAdd) {
                    ris.setImplicitRules("JCSQXZ");
                    if(flag){
                        ris.getExtension().add(new Extension("feeStatus",0));//未收费
                    }else{
                        ris.getExtension().add(new Extension("feeStatus",1));//已收费
                    }
                    ris.setStatus("1");
                } else {
                    ris.setImplicitRules("JCSQGX");
                    ris.getExtension().add(new Extension("feeStatus",0));//为收费
                    ris.setStatus("8");
                }
                ris.getExtension().add(new Extension("examCount",Integer.parseInt(MapUtils.getString(risInfo,"quanCg","1"))));
                ris.getExtension().add(new Extension("gestationalWeek",MapUtils.getString(risInfo,"gesoVal",""),null));
                entryRis.setResource(ris);
                businessBase.getEntry().add(entryRis);

                if(indexData == null){
                    indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(risInfo,"codeOp"))
                            .codePi(MapUtils.getString(risInfo,"codePi"))
                            .codePv(MapUtils.getString(risInfo,"codePv"))
                            .euEme(flag?"02":null)
                            .build();
                }

            }
        }else{
            throw new BusException("未获取到检查信息！！");
        }
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(indexData)
                .remoteMethod("ServiceRequest").build();
        log.info("检查申请构造完成id:{},data:{},remoteMethod:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
        SendAndResolve.getInstance().send(flagAdd? HttpMethod.POST:HttpMethod.PUT,requestData);
    }

    /**
     * 检验发送
     * @param map
     * @param flag true 表示正常的检验申请，false 表示病理的检查申请发送检验消息
     * @param isEme 是否急
     */
    public void sendOpApplyLisMsg(List<Map<String, Object>> map,boolean flag,boolean isEme){
        boolean flagAdd = EnumerateParameter.ZERO.equals(MapUtils.getString(map.get(0), "isAdd","1"));
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setId(id);
        businessBase.setType("message");
        businessBase.setTimestamp(new Date());
        businessBase.setEntry(new ArrayList<>());
        Entry entry = new Entry();
        entry.setResource(new PhResource());
        PhResource phResource = entry.getResource();
        phResource.setResourceType("MessageHeader");
        phResource.setId(NHISUUID.getKeyId());
        phResource.setDestination(Arrays.asList(new Destination("PT")));
        phResource.setSource(new Destination("HIS"));
        businessBase.getEntry().add(entry);
        MsgIndexData indexData = null;
        List<String> pkCnords = new ArrayList<>();
        for(Map<String, Object> m:map){
            pkCnords.add(MapUtils.getString(m,"pkCnord"));
        }
        List<Map<String,Object>> lisList = null;
        if(flag){
            lisList =  zsrmCnSendMapper.getLisInfo(pkCnords);
        }else{
            lisList =  zsrmCnSendMapper.getRisInfo(pkCnords);
        }
        if(lisList != null){
            for(Map<String,Object> lisInfo :lisList){
                Entry entryLis = new Entry();
                Lis lis = new Lis();
                lis.setResourceType("ServiceRequest");
                if (flagAdd) {
                    lis.setImplicitRules("JYSQXZ");
                    lis.setStatus("1");
                } else {
                    lis.setImplicitRules("JYSQXG");
                    lis.setStatus("9");
                }
                lis.setIdentifier(new ArrayList<>());
                lis.getIdentifier().add(new Identifier("id/applyno",MapUtils.getString(lisInfo,"codeApply")));//申请单号

                lis.setIntent("order");

                lis.setCategory(Arrays.asList(new Category(Arrays.asList(new Coding("applyTypeCode",MapUtils.getString(lisInfo,"codeOrdtype",""),MapUtils.getString(lisInfo,"nameOrdtype",""))))));

                //lis.setCategory()));//分类

                lis.setCode(new Category(Arrays.asList(new Coding("examCode",MapUtils.getString(lisInfo,"codeOrd",""),MapUtils.getString(lisInfo,"nameOrd","")))));

                Locationlis location = new Locationlis();
                location.setResourceType("Patient");
                location.setIdentifier(new Identifier("patientid",MapUtils.getString(lisInfo,"codeOp","")));
                location.setName(Arrays.asList(new TextElement(MapUtils.getString(lisInfo,"namePi",""))));
                String dtSex = MapUtils.getString(lisInfo,"dtSex");
                location.setGender("02".equals(dtSex)?"male":"03".equals(dtSex)?"female":"other");//患者性别
                //location.setBirthDate(MapUtils.getString(lisInfo,"birthDate"));
                location.setFlagSpec(MapUtils.getString(lisInfo,"flagSpec"));
                Date birthday = TransfTool.getPropValueDates(lisInfo, "birthDate");
                location.setBirthDate(DateUtils.dateToStr("yyyy-MM-dd",birthday==null?new Date():birthday));//出生日期
                lis.setSubject(location);

                //就诊类别
                Encounter encounter = new Encounter();
                encounter.setResourceType("Encounter");
                if(EnumerateParameter.ONE.equals(MapUtils.getString(lisInfo,"euPvtype"))||EnumerateParameter.TWO.equals(MapUtils.getString(lisInfo,"euPvtype"))){
                    encounter.setClas(new Coding("AMB","门诊"));
                }else if(EnumerateParameter.THREE.equals(MapUtils.getString(lisInfo,"euPvtype"))){
                    encounter.setClas(new Coding("IMP","住院"));
                }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(lisInfo,"euPvtype"))){
                    encounter.setClas(new Coding("PHY","体检"));
                }
                encounter.setIdentifier(new Identifier("id/visitno",MapUtils.getString(lisInfo,"codePv")));
                //encounter.setName(new TextElement(MapUtils.getString(cn,"namePi")));
                lis.setEncounter(encounter);

                if(StringUtils.isNotBlank(ZsphMsgUtils.getPropValueStr(lisInfo,"dateEnter"))){
                    try {
                        lis.setAuthoredOn(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(ZsphMsgUtils.getPropValueStr(lisInfo,"dateEnter")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    lis.setAuthoredOn(null);
                }

                //申请医生
                Locationlis locationlisReq = new Locationlis();
                locationlisReq.setResourceType("Practitioner");
                locationlisReq.setIdentifier(new Identifier("id/applyDoc",MapUtils.getString(lisInfo,"codeEmp")));
                locationlisReq.setName(Arrays.asList(new TextElement(MapUtils.getString(lisInfo,"nameEmp"))));
                lis.setRequester(locationlisReq);

                //核对人
                lis.setPerformer(new ArrayList<>());
                Locationlis locationlis = new Locationlis();
                locationlis.setResourceType("Practitioner");
                //locationlis.setIdentifier(new Identifier("id/verifyDoc",MapUtils.getString(lisInfo,"codeEmpOrdChk","")));
                //locationlis.setName(Arrays.asList(new TextElement(MapUtils.getString(lisInfo,"nameEmpOrdChk",""))));
                locationlis.setIdentifier(new Identifier("id/verifyDoc",MapUtils.getString(map.get(0),"codeEmpCg","")));
                locationlis.setName(Arrays.asList(new TextElement(MapUtils.getString(map.get(0),"nameEmpCg",""))));
                Locationlis locationlis1 = new Locationlis();
                locationlis1.setResourceType("Practitioner");
                locationlis1.setIdentifier(new Identifier("code/exdeptcode",MapUtils.getString(lisInfo,"codeDeptExec","")));
                locationlis1.setName(MapUtils.getString(lisInfo,"nameDeptExec",""));
                lis.getPerformer().add(locationlis);
                lis.getPerformer().add(locationlis1);

                //lis.setLocationCode(new ArrayList<>());
                List<Code> code = new ArrayList<>();
                Map<String,Object> dept = getDeptInfo(MapUtils.getString(lisInfo,"pkDept"));
                code.add(new Code(Arrays.asList(new Coding("code/reqdeptcode",MapUtils.getString(dept,"codeDept",""),MapUtils.getString(dept,"nameDept","")))));
                if(EnumerateParameter.THREE.equals(MapUtils.getString(lisInfo,"euPvtype"))){
                    //到时候住院用，门诊显示诊区
                    Map<String,Object> deptNs = getDeptInfo(MapUtils.getString(lisInfo,"pkDeptNs"));
                    code.add(new Code(Arrays.asList(new Coding("code/wardcode",MapUtils.getString(deptNs,"codeDept",""),MapUtils.getString(deptNs,"nameDept","")))));
                }else{
                    code.add(new Code(Arrays.asList(new Coding("code/wardcode",MapUtils.getString(lisInfo,"codeDeptArea",""),MapUtils.getString(lisInfo,"nameDeptArea","")))));
                }
                //code.add(new Code(Arrays.asList(new Coding("code/bodno",MapUtils.getString(deptNs,"bedNo",""),MapUtils.getString(deptNs,"bedNo","")))));
                lis.setLocationCode(code);

                lis.setReasonReference(new ArrayList<>());
                ReasonReference reasonReference = new ReasonReference();
                reasonReference.setResourceType("Condition");
                reasonReference.setCode(new Code(Arrays.asList(new Coding("diagnosis",MapUtils.getString(lisInfo,"codeIcd",""),MapUtils.getString(lisInfo,"nameDiag","")))));
                lis.getReasonReference().add(reasonReference);

                //标本
                lis.setSpecimen(new ArrayList<>());
                SpecimenLis specimen = new SpecimenLis();
                specimen.setResourceType("Specimen");
                specimen.setType(new CodeLis(new Coding("Specimen",MapUtils.getString(lisInfo,"dtSamptype",""),MapUtils.getString(lisInfo,"labName",""))));
                //specimen.setType(new Coding("Specimen",MapUtils.getString(lisInfo,"dtSamptype",""),MapUtils.getString(lisInfo,"labName","")));
                Specimen specimen1 = new Specimen();
                specimen1.setResourceType("Practitioner");
                specimen1.setIdentifier(new Identifier("collectorId",MapUtils.getString(lisInfo,"codeEmpCol","")));
                specimen1.setName(Arrays.asList(new TextElement(MapUtils.getString(lisInfo,"nameEmpCol",""))));
                Collection collection = new Collection();
                collection.setCollector(specimen1);
                if(StringUtils.isNotBlank(ZsphMsgUtils.getPropValueStr(lisInfo,"dateCol"))){
                    try {
                        collection.setCollectedDateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(ZsphMsgUtils.getPropValueStr(lisInfo,"dateCol")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    collection.setCollectedDateTime(null);
                }

                specimen.setCollection(collection);//标本采集人
                specimen.setExtension(null);
                //specimen.setIdentifier(new Identifier("Specimen",MapUtils.getString(cn,"dtSamptype"),MapUtils.getString(cn,"labName"),"specimen"));
                lis.getSpecimen().add(specimen);
                lis.setNote(Arrays.asList(new TextElement(MapUtils.getString(lisInfo,"note",""))));
                //lis.setRelevantHistory(Arrays.asList(new RelevantHistory("Provenance",Arrays.asList(new TextElement(MapUtils.getString(lisInfo,"noteDise",""))))));
                entryLis.setResource(lis);
                businessBase.getEntry().add(entryLis);

                if(indexData == null){
                    indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(lisInfo,"codeOp"))
                            .codePi(MapUtils.getString(lisInfo,"codePi"))
                            .codePv(MapUtils.getString(lisInfo,"codePv"))
                            .euEme(isEme?"03":null)
                            .build();
                }
            }

        }else{
            throw new BusException("未获取到检查信息！！");
        }
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(indexData)
                .remoteMethod("ServiceRequest").build();
        log.info("检查申请构造完成id:{},data:{},remoteMethod:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
        SendAndResolve.getInstance().send(flagAdd? HttpMethod.POST:HttpMethod.PUT,requestData);

    }

    /**
     * 发送医嘱核对信息
     */
    public void sendExOrderCheckMsg(Map<String, Object> paramMap) {
        if(paramMap == null||paramMap.get("ordlistvo")==null) {
            throw new BusException("未获取到手术信息！！");
        }
        String param= JsonUtil.writeValueAsString(paramMap.get("ordlistvo"));
        List<OrderCheckMsgVo> listMap = JsonUtil.readValue(param, new TypeReference<List<OrderCheckMsgVo>>() {});
        for(OrderCheckMsgVo checkvo : listMap) {
            Map<String,Object> paramOrder=new HashMap<String,Object>();
            String codeOrdtype = checkvo.getCodeOrdtype();
            //手术申请
            if ("04".equals(codeOrdtype)) {
                paramOrder.put("pkCnord",checkvo.getPkCnord());
                sendOpApplyMsg(paramOrder);
            }
        }
    }

    public void sendOpApplyMsg(Map<String,Object> paramMap){
        boolean flagAdd = EnumerateParameter.ZERO.equals(MapUtils.getString(paramMap, "isAdd","1"));
        List<Map<String,Object>> resultList=zsrmCnSendMapper.qryOpApplyInfo(paramMap);
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setId(id);
        businessBase.setType("message");
        businessBase.setTimestamp(new Date());
        businessBase.setEntry(new ArrayList<>());
        Entry entry = new Entry();
        entry.setResource(new PhResource());
        PhResource phResource = entry.getResource();
        phResource.setResourceType("MessageHeader");
        phResource.setId(NHISUUID.getKeyId());
        phResource.setDestination(Arrays.asList(new Destination("PT")));
        phResource.setSource(new Destination("HIS"));
        businessBase.getEntry().add(entry);
        if(resultList != null){
            for(Map<String,Object> op : resultList){
                Entry entryLis = new Entry();
                OpApplyVo opApply = new OpApplyVo();
                opApply.setResourceType("ServiceRequest");
                if (flagAdd) {
                    opApply.setImplicitRules("ZYSSSQXZ");
                    opApply.setStatus("1");
                } else {
                    opApply.setImplicitRules("ZYSSSQXG");
                    opApply.setStatus("8");//手术申请的取消状态暂时没定
                }
                opApply.setIdentifier(new ArrayList<>());
                opApply.getIdentifier().add(new Identifier("id/applyno",MapUtils.getString(op,"codeApply","")));//申请单号

                opApply.setIntent("order");
                opApply.setCategory(Arrays.asList(new Category(Arrays.asList(new Coding("applyTypeCode",MapUtils.getString(op,"codeOrdtype",""),MapUtils.getString(op,"nameOrdtype",""))))));

                opApply.setPriority("routine");//文档上没说什么意思
                List<BdExtension> bd = new ArrayList<>();
                bd.add(new BdExtension("opLevelCode",new Coding(MapUtils.getString(op,"opLevelCode",""),MapUtils.getString(op,"opLevelName",""))));
                bd.add(new BdExtension("anMethCode",new Coding(MapUtils.getString(op,"opAsalevelCode",""),MapUtils.getString(op,"opAsalevelName",""))));
                bd.add(new BdExtension("inciTypeCode",new Coding(MapUtils.getString(op,"opIncitypeCode",""),MapUtils.getString(op,"opIncitypeName",""))));
                opApply.setCode(new Category(Arrays.asList(new Coding("operationCode",MapUtils.getString(op,"codeOrd",""),MapUtils.getString(op,"nameOrd",""),bd))));

                Locationlis location = new Locationlis();
                location.setResourceType("Patient");
                location.setIdentifier(new Identifier("patientid",MapUtils.getString(op,"codePi","")));
                location.setName(Arrays.asList(new TextElement(MapUtils.getString(op,"namePi",""))));

                String dtSex = MapUtils.getString(op,"dtSex");
                location.setGender("02".equals(dtSex)?"male":"03".equals(dtSex)?"female":"other");//患者性别
                location.setBirthDate(MapUtils.getString(op,"birthDate",""));
                opApply.setSubject(location);

                //就诊类别
                Encounter encounter = new Encounter();
                encounter.setResourceType("Encounter");
                if(EnumerateParameter.ONE.equals(MapUtils.getString(op,"euPvtype"))){
                    encounter.setClas(new Coding("AMB","门诊"));
                }else if(EnumerateParameter.TWO.equals(MapUtils.getString(op,"euPvtype"))){
                    encounter.setClas(new Coding("EMER","急诊"));
                }else if(EnumerateParameter.THREE.equals(MapUtils.getString(op,"euPvtype"))){
                    encounter.setClas(new Coding("IMP","住院"));
                }else if(EnumerateParameter.FOUR.equals(MapUtils.getString(op,"euPvtype"))){
                    encounter.setClas(new Coding("PHY","体检"));
                }
                encounter.setIdentifier(new Identifier("id/visitno",MapUtils.getString(op,"codePv")));
                //encounter.setName(new TextElement(MapUtils.getString(cn,"namePi")));
                opApply.setEncounter(encounter);

                if(StringUtils.isNotBlank(ZsphMsgUtils.getPropValueStr(op,"dateRpt"))){
                    try {
                        opApply.setAuthoredOn(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(ZsphMsgUtils.getPropValueStr(op,"dateEnter")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    opApply.setAuthoredOn(null);
                }

                //申请医生
                Locationlis locationlisReq = new Locationlis();
                locationlisReq.setResourceType("Practitioner");
                locationlisReq.setIdentifier(new Identifier("id/applyDoc",MapUtils.getString(op,"opEmpCode","")));
                locationlisReq.setName(MapUtils.getString(op,"opEmpName",""));
                opApply.setRequester(locationlisReq);

                //手术医生
                opApply.setPerformer(new ArrayList<>());
                Locationlis locationlis = new Locationlis();
                locationlis.setResourceType("Practitioner");
                locationlis.setIdentifier(new Identifier("id/surgenNo",MapUtils.getString(op,"codeEmpOp","")));
                locationlis.setName(MapUtils.getString(op,"nameEmpPhyOp",""));
                Locationlis locationlis1 = new Locationlis();
                locationlis1.setResourceType("Practitioner");
                locationlis1.setIdentifier(new Identifier("code/exdeptcode",MapUtils.getString(op,"codeDeptExec","")));
                locationlis1.setName(MapUtils.getString(op,"nameDeptExec",""));
                opApply.getPerformer().add(locationlis);
                opApply.getPerformer().add(locationlis1);

                opApply.setLocationCode(new ArrayList<>());
                Map<String,Object> dept = getDeptInfo(MapUtils.getString(op,"pkDept"));
                opApply.getLocationCode().add(new Coding("code/reqdeptcode",MapUtils.getString(dept,"codeDept",""),MapUtils.getString(dept,"nameDept","")));
                Map<String,Object> deptNs = getDeptInfo(MapUtils.getString(op,"pkDeptNs"));
                opApply.getLocationCode().add(new Coding("code/wardcode",MapUtils.getString(deptNs,"codeDept",""),MapUtils.getString(deptNs,"nameDept","")));
                opApply.getLocationCode().add(new Coding("code/bodno",MapUtils.getString(op,"bedNo",""),"床号"));

                opApply.setReasonReference(new ArrayList<>());
                ReasonReference reasonReference = new ReasonReference();
                reasonReference.setResourceType("Condition");
                reasonReference.setCode(new Code(Arrays.asList(new Coding("diagnosis",MapUtils.getString(op,"codeIcd",""),MapUtils.getString(op,"nameDiag",""))),MapUtils.getString(op,"descDiagPre")));
                opApply.getReasonReference().add(reasonReference);

                //只有手术体位，手术部位不存在
                opApply.setBodySite(Arrays.asList(new Code(Arrays.asList(new Coding("bodySite",MapUtils.getString(op,"dtPosi",""),MapUtils.getString(op,"namePosi",""))))));

                opApply.setNote(Arrays.asList(new TextElement(MapUtils.getString(op,"note",""))));
                opApply.setRelevantHistory(Arrays.asList(new RelevantHistory("Provenance",null)));
                entryLis.setResource(opApply);
                businessBase.getEntry().add(entryLis);
            }

            RequestData requestData = RequestData.newBuilder()
                    .id(id)
                    .data(JSON.toJSONString(businessBase))
                    .remoteMethod("sendOpApplyLisMsg").build();
            log.info("手术申请构造完成id:{},data:{},remoteMethod:{}"
                    ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
            SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
        }else{
            throw new BusException("根据医嘱未获取到手术信息！！");
        }
    }

    /***
     * 诊断发送
     * @param map
     */
    public void sendCnDiagMsg(Map<String, Object> map) {
        String pkPv = MapUtils.getString(map,"pkPv");
        if(StringUtils.isBlank(pkPv)){
            throw new BusException("pkPv未传入");
        }
        List<Map<String, Object>> pvDiagList = DataBaseHelper.queryForList("select diag.diagcode,diag.diagname,pv.eu_pvtype," +
                " pvdiag.PK_PVDIAG,pv.code_pv,pvdiag.flag_maj,pvdiag.sort_no,pvdiag.dt_diagtype,pvdiag.date_diag,pi.name_pi,pi.code_pi,pi.code_op,emp.code_emp,emp.name_emp" +
                " from pv_diag pvdiag   inner join pv_encounter pv on pv.pk_pv=pvdiag.pk_pv   " +
                " left join bd_term_diag diag on diag.pk_diag=pvdiag.pk_diag and nvl(diag.FLAG_STOP,'0')='0' and diag.DEL_FLAG='0'" +
                " left join pi_master pi on pv.pk_pi=pi.pk_pi " +
                " left join bd_ou_employee emp on emp.pk_emp=pvdiag.pk_emp_diag" +
                " where pvdiag.del_flag='0' and pvdiag.PK_PV=?", new Object[]{pkPv});
        if(CollectionUtils.isEmpty(pvDiagList)){
            throw new BusException("依据pkPv未获取到诊断信息:"+pkPv);
        }
        BusinessBase businessBase = RequestBuild.create(null);
        List<Entry> entryList = businessBase.getEntry();
        String implicitRules = "MZZDXZ";
        MsgIndexData indexData = null;
        for (Map<String, Object> pvDiagMap : pvDiagList) {
            PvDiagCondition condition = new PvDiagCondition();
            condition.setResourceType("Condition");
            condition.setId(NHISUUID.getKeyId());
            condition.setImplicitRules(implicitRules);
            condition.setExtension(Lists.newArrayList(new Extension("isprimary","1".equals(MapUtils.getString(pvDiagMap,"flagMaj")))));
            condition.setIdentifier(Arrays.asList(new Identifier("id/diagNo",MapUtils.getString(pvDiagMap,"pkPvdiag"))));
            //诊断类别标识符
            condition.setCategory(Lists.newArrayList(new CodeableConcept(Arrays.asList(new Coding(TransfTool.getPropValueStr(pvDiagMap,"dtDiagtype"),TransfTool.getBdDefdocName("060005",TransfTool.getPropValueStr(pvDiagMap,"dtDiagtype")))))));
            //患者诊断
            condition.setCode(new CodeableConcept(Arrays.asList(new Coding(TransfTool.getPropValueStr(pvDiagMap,"diagcode"),TransfTool.getPropValueStr(pvDiagMap,"diagname")))));
            //病人标识
            condition.setSubject(new ConditionRecorder("Patient",null,Arrays.asList(new Identifier("code/patientId",TransfTool.getPropValueStr(pvDiagMap,"codeOp"))),Arrays.asList(new TextElement(TransfTool.getPropValueStr(pvDiagMap,"namePi")))));
            //就诊信息
            condition.setEncounter(new ConditionRecorder("Encounter",TransfTool.getEuPvtype(TransfTool.getPropValueStr(pvDiagMap,"euPvtype")),new Identifier("id/visitno",TransfTool.getPropValueStr(pvDiagMap,"codePv"))));
            //诊断时间
            condition.setRecordedDate(TransfTool.getPropValueDates(pvDiagMap,"dateDiag"));
            //诊断医生
            condition.setRecorder(new ConditionRecorder("Practitioner",null,Arrays.asList(new Identifier("doctorCode",TransfTool.getPropValueStr(pvDiagMap,"codeEmp"))),Arrays.asList(new TextElement(TransfTool.getPropValueStr(pvDiagMap,"nameEmp")))));
            entryList.add(new Entry(condition));

            if(indexData == null){
                indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(pvDiagMap,"codeOp"))
                        .codePi(MapUtils.getString(pvDiagMap,"codePi"))
                        .codePv(MapUtils.getString(pvDiagMap,"codePv"))
                        .build();
            }
        }
        RequestData requestData = RequestData.newBuilder()
                .id(businessBase.getId())
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(indexData)
                .remoteMethod("Condition").build();
        log.info("推送诊断信息id:{},data:{},remoteMethod:{},implicitRules:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod(),implicitRules);
        SendAndResolve.getInstance().send(HttpMethod.POST,requestData);
    }

    /**
     * 门诊收费时，发送检查、检验，结算消息
     * @param paramMap
     * @throws Exception
     */
    public void sendCnPresOpMsg(Map<String,Object> paramMap) {
        if("1".equals(paramMap.get("IsPtOpen"))){
           //发送平台处方上传信息，只收费结算后进行发送
            paramMap.put("isAdd",EnumerateParameter.ZERO);
            sendPtPresInfo(paramMap);
        }
        if("1".equals(paramMap.get("IsReturnDrug"))){//门诊退费
            paramMap.put("isAdd",EnumerateParameter.ONE);
            sendPtPresInfo(paramMap);
        }
    }

    /**
     * 发送处方信息到平台
     * @param paramMap
     */
    public void sendPtPresInfo(Map<String,Object> paramMap){
        boolean flagAdd = EnumerateParameter.ZERO.equals(MapUtils.getString(paramMap, "isAdd","1"));
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setId(id);
        businessBase.setType("message");
        businessBase.setTimestamp(new Date());
        businessBase.setEntry(new ArrayList<>());
        Entry entry = new Entry();
        entry.setResource(new PhResource());
        PhResource phResource = entry.getResource();
        phResource.setResourceType("MessageHeader");
        phResource.setId(NHISUUID.getKeyId());
        phResource.setDestination(Arrays.asList(new Destination("PT")));
        phResource.setSource(new Destination("HIS"));
        businessBase.getEntry().add(entry);
        MsgIndexData indexData = null;
        List<String> pkPreses= null;
        if(flagAdd){
            pkPreses = (List<String>) paramMap.get("pkPresocces");
        }else{
            pkPreses = (List<String>) paramMap.get("pkPress");
        }
        if(pkPreses==null|| pkPreses.size()<=0){
           return;
        }
        List<Map<String,Object>> presesList =  zsrmCnSendMapper.getPtPresInfo(pkPreses);
        if(presesList == null){throw new BusException("未获取到处方信息！！");}
        for(Map<String,Object> preses :presesList){
            Entry entryLis = new Entry();
            CnPreses cnPreses = new CnPreses();
            cnPreses.setResourceType("MedicationRequest");
            cnPreses.setId(businessBase.getId());
            if(flagAdd){
                cnPreses.setImplicitRules("MZCFXZ");
                cnPreses.setStatus("S");
            }else{
                cnPreses.setImplicitRules("MECFQX");
                cnPreses.setStatus("C");
            }
            cnPreses.setIdentifier(Arrays.asList(new Identifier("id/recipeNo",MapUtils.getString(preses,"presNo"))));
            cnPreses.setStatusReason(new TextElement(""));//没有
            cnPreses.setCategory(Arrays.asList(new CodeableConcept(Arrays.asList(new Coding("orderCategory",MapUtils.getString(preses,"dtPrestype",""),TransfTool.getBdDefdocName("060101", MapUtils.getString(preses,"dtPrestype")))))));
            //处方项目
            cnPreses.setMedicationCodeableConcept(new CodeableConcept(Arrays.asList(new Coding("orderItem",MapUtils.getString(preses, "code"),MapUtils.getString(preses, "name")))));
            //项目属性            药品剂型不知道是什么
            cnPreses.setMedicationReference(new Reference("Medication",new CodeableConcept(Arrays.asList(new Coding("drugForm","/","/"))),new Numerator(MapUtils.getString(preses, "quan"),MapUtils.getString(preses, "unitName")),Arrays.asList(new Extension("spec",MapUtils.getString(preses,"spec"),null))));
            //患者基本信息codeOp、姓名、性别
            cnPreses.setSubject(new Reference("Patient",new Identifier("code/patientId",MapUtils.getString(preses,"codeOp")),Arrays.asList(new TextElement(MapUtils.getString(preses,"namePi"))),TransfTool.getSex(MapUtils.getString(preses, "dtSex"))));
            //申请单患者就诊信息
            cnPreses.setEncounter(new Reference("Encounter",new Identifier("code/visitNo",MapUtils.getString(preses,"codePv")),TransfTool.getEuPvtype(MapUtils.getString(preses,"euPvtype"))));
            //开方科室、病区
            cnPreses.setSupportingInformation(Arrays.asList(new Reference("Location",new Identifier("id/orderDeptId",MapUtils.getString(preses,"codeDept")),MapUtils.getString(preses,"nameDept"))));
            //处方开立时间
            cnPreses.setAuthoredOn(TransfTool.getPropValueDates(preses,"dateEnter"));
            //开方医生
            cnPreses.setRequester(new Reference("Practitioner",new Identifier("code/requesterId",MapUtils.getString(preses,"codeEmp")),Arrays.asList(new TextElement(MapUtils.getString(preses,"nameEmp")))));
            //审核医生
            cnPreses.setPerformer(new Reference("Practitioner",new Identifier("code/performerId",MapUtils.getString(preses,"empchkCode")),Arrays.asList(new TextElement(MapUtils.getString(preses,"empchkName")))));
            //录入者
            cnPreses.setRecorder(new Reference("Practitioner",new Identifier("code/recorderId",MapUtils.getString(preses,"empinputCode")),MapUtils.getString(preses,"empinputName")));
            //组号
            cnPreses.setGroupIdentifier(new Reference(new Identifier("code/groupId",MapUtils.getString(preses,"groupno"))));
            //嘱托
            cnPreses.setNote(Arrays.asList(new TextElement(MapUtils.getString(preses,"note"))));
            //药品使用说明
            cnPreses.setDosageInstruction(Arrays.asList(new Reference(null,new CodeableConcept(Arrays.asList(new Coding("code/route",MapUtils.getString(preses,"supplyCode"),MapUtils.getString(preses,"supplyName")))),Arrays.asList(new Timing(new TextElement(MapUtils.getString(preses,"freqName")),new Numerator(MapUtils.getString(preses,"dosage"),MapUtils.getString(preses,"unitdosName")))),new Identifier(null,MapUtils.getString(preses,"quanDisp")))));
            //处方有效期、执行科室
            cnPreses.setDispenseRequest(new Reference(new ValuePeriod(TransfTool.getPropValueDates(preses,"dateEnter"),TransfTool.getPropValueDates(preses,"dateEffe")),new Reference("Organization",MapUtils.getString(preses,"nameDeptexe"),Arrays.asList(new Identifier("code/perDeptid",MapUtils.getString(preses,"codeDeptexe"))))));
            entryLis.setResource(cnPreses);
            businessBase.getEntry().add(entryLis);
            if(indexData == null){
                indexData = MsgIndexData.newBuilder().codeOp(MapUtils.getString(preses,"codeOp"))
                        .codePi(MapUtils.getString(preses,"codePi"))
                        .codePv(MapUtils.getString(preses,"codePv"))
                        .build();
            }
        }
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(indexData)
                .remoteMethod("MedicationRequest").build();
        log.info("处方信息构造完成id:{},data:{},remoteMethod:{}"
                ,requestData.getId(),requestData.getData(),requestData.getRemoteMethod());
        SendAndResolve.getInstance().send(flagAdd? HttpMethod.POST:HttpMethod.PUT,requestData);

    }


    public void checkOpLisOrRis(Map<String, Object> paramMap) {
        List<Map<String,Object>> orderList = null;
        if(EnumerateParameter.ZERO.equals(MapUtils.getString(paramMap,"isAdd"))){//核对
            orderList = (List<Map<String,Object>>) MapUtils.getObject(paramMap, "ordlist");
        }else{//作废
            List<String> pkCnOrds = (List<String>) MapUtils.getObject(paramMap, "pkCnList",new ArrayList<String>());
            if(pkCnOrds==null||pkCnOrds.size()==0){throw new BusException("没有传入检验检查手术医嘱！！");}
            orderList = zsrmCnSendMapper.getCnOrderInfo(pkCnOrds);
        }
        if(CollectionUtils.isEmpty(orderList)){
            throw new BusException("没有传入需要核对的医嘱信息！！");
        }
        orderList.stream().forEach(vo->vo.put("isAdd",MapUtils.getString(paramMap,"isAdd")));
        Set<String> stringSet = new HashSet<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Map<String,Object> map : orderList){
            if("02".equals(MapUtils.getString(map,"codeOrdtype").substring(0,2))){
                stringSet.add(MapUtils.getString(map,"pkOrd"));
                mapList.add(map);
            }
            //手术申请
            if("04".equals(MapUtils.getString(map,"codeOrdtype").substring(0,2))){
                Map<String,Object> paramOrder=new HashMap<String,Object>();
                paramOrder.put("pkCnord",MapUtils.getString(map,"pkCnord"));
                paramOrder.put("isAdd",MapUtils.getString(paramMap,"isAdd"));
                sendOpApplyMsg(paramOrder);
            }
        }
        //区分检查病理的申请单发送检验申请
        Map<String, Map<String, Object>>  mapPathology = DataBaseHelper.queryListToMap("select ord.pk_ord as key_,ord.CODE_ORDTYPE from bd_ord ord "
                + "where ord.pk_ord in (" + CommonUtils.convertSetToSqlInPart(stringSet, "pk_ord") + ") and exists (select 1  from bd_dictattr_temp tmp inner join bd_dictattr att on tmp.pk_dictattrtemp = att.pk_dictattrtemp" +
                " where tmp.dt_dicttype = '02' and att.VAL_ATTR = '1' and tmp.code_attr = '0209' and ord.pk_ord = att.pk_dict)");

        if(mapPathology!=null){
            for(Map<String,Object> m : mapList){
                if(mapPathology.containsKey(MapUtils.getString(m,"pkOrd"))){
                    sendOpApplyLisMsg(Arrays.asList(m),false,false);
                }
            }
        }
        //得到检查按患者分组ris
        Map<String, List<Map<String, Object>>> risMap = orderList.stream().filter(vo->!"0207".equals(MapUtils.getString(vo,"codeOrdtype"))&&"02".equals(MapUtils.getString(vo,"codeOrdtype").substring(0,2)))
                .collect(Collectors.groupingBy(vo -> (MapUtils.getString(vo, "pkPv"))));
        //得到检验按患者分组
        Map<String, List<Map<String, Object>>> lisMap = orderList.stream().filter(vo->"03".equals(MapUtils.getString(vo,"codeOrdtype").substring(0,2)))
                .collect(Collectors.groupingBy(vo -> (MapUtils.getString(vo, "pkPv"))));
        //得到检查心电按患者分组ris
        Map<String, List<Map<String, Object>>> xdMap = orderList.stream().filter(vo->"0207".equals(MapUtils.getString(vo,"codeOrdtype")))
                .collect(Collectors.groupingBy(vo -> (MapUtils.getString(vo, "pkPv"))));

        for (Map.Entry<String, List<Map<String, Object>>> entry : risMap.entrySet()) {//检查
            sendOpApplyRisMsg(entry.getValue(),false);
        }
        for (Map.Entry<String, List<Map<String, Object>>> entry : lisMap.entrySet()) {//检验
            sendOpApplyLisMsg(entry.getValue(),true,false);
        }
        for (Map.Entry<String, List<Map<String, Object>>> entry : xdMap.entrySet()) {//心电
            sendOpApplyRisMsg(entry.getValue(),false);
        }


    }


    public Map<String,Object> getDeptInfo(String pkDept){
        return DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = '"+pkDept+"'");
    }



}
