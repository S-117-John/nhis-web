package com.zebone.nhis.ma.pub.sd.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDictMap;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStDiff;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsTkybSt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.sd.dao.CmrInsuMapper;
import com.zebone.nhis.ma.pub.sd.vo.cmrInsu.*;
import com.zebone.nhis.ma.pub.support.AesUtils;
import com.zebone.nhis.ma.pub.support.WSUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 泰康商业保险平台接口服务类
 */
@Service
public class CmrInsuService implements InitializingBean {

    //应用Id
    private String cmrInsuAppId;
    //应用秘钥
    private String secret;
    // 默认为 client_credential
    private String grantType;
    //业务接口地址
    private String cmrInsuUrl;
    //获取Token地址
    private String cmrInsuTokenUrl;
    //token令牌
    private String accessToken;
    //token令牌过期时间  单位秒
    private int expiresIn;
    //token令牌读取时间(用来计算是否过期)
    private Date tokenDate;
    //发送方编码
    private String senderCode;
    //发送方名称
    private String senderName;
    //接收方编码
    private String receiverCode;
    //接收方名称
    private String receiverName;
    //提供方编号
    private String providerCode;
    //提供方名称
    private String providerName;
    //标准的版本号信息
    private String versionCode;

    //加密函数key,根据实际修改
    private static final String ENCRYPT_KEY = "d39525819968e65m";
    //解密函数key,根据实际修改
    private static final String DECRYPT_KEY = "d39525819968e65m";

    //日志
    private Logger tkLog = LoggerFactory.getLogger("nhis.CmrInsu");


    @Autowired
    private CmrInsuMapper cmrInsuMapper;

    public void afterPropertiesSet() throws Exception {
        cmrInsuAppId = ApplicationUtils.getPropertyValue("ciInsu.cmrInsuAppId", "");
        secret = ApplicationUtils.getPropertyValue("ciInsu.secret", "");
        senderCode = ApplicationUtils.getPropertyValue("ciInsu.senderCode", "");
        senderName = ApplicationUtils.getPropertyValue("ciInsu.senderName", "");
        receiverCode = ApplicationUtils.getPropertyValue("ciInsu.receiverCode", "");
        receiverName = ApplicationUtils.getPropertyValue("ciInsu.receiverName", "");
        versionCode = ApplicationUtils.getPropertyValue("ciInsu.versionCode", "");
        cmrInsuUrl = ApplicationUtils.getPropertyValue("ciInsu.cmrInsuUrl", "");
        cmrInsuTokenUrl = ApplicationUtils.getPropertyValue("ciInsu.cmrInsuTokenUrl", "");
        grantType = ApplicationUtils.getPropertyValue("ciInsu.grantType", "");

        if (!CommonUtils.isEmptyString(senderName)) {
            senderName = new String(senderName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
        }
        if (!CommonUtils.isEmptyString(receiverName)) {
            receiverName = new String(receiverName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
        }

    }

    /**
     * 需要构造token在过期前30秒定时刷新机制，所有使用此token的地方需要在使用前调用此函数获取最新的token，防止token过期或失效
     */
    private CiTokenDataVo getToken() {
        CiTokenDataVo dataVo = new CiTokenDataVo();
        //组装地址参数
        StringBuffer hpptUrl = new StringBuffer("");
        hpptUrl.append(String.format("%s%s", cmrInsuTokenUrl, "api/token/access_token?"));
        hpptUrl.append(String.format("appId=%s", cmrInsuAppId));
        hpptUrl.append(String.format("&secret=%s", secret));
        hpptUrl.append(String.format("&grant_type=%s", grantType));
        hpptUrl.append(String.format("&senderCode=%s", senderCode));
        hpptUrl.append("&providerCode=");

        String resJson = HttpClientUtil.sendHttpGet(new HttpGet(hpptUrl.toString()));
        //返回值resJson未加密
        if (!CommonUtils.isEmptyString(resJson)) {
            CiTokenResVo resTokenVo = JsonUtil.readValue(resJson, CiTokenResVo.class);
            //获取result信息，校验Token是否获取成功
            CiTokenResultVo resultVo = resTokenVo.getResult();
            if ("0".equals(resultVo.getCode())) {
                //成功,得到token
                dataVo = resTokenVo.getData();
            } else if ("ERR0003".equals(resultVo.getCode()) || "ERR0004".equals(resultVo.getCode())) {
                throw new BusException("商保平台获取Token出错：" + resultVo.getMsg());
            } else {
                throw new BusException("商保平台获取Token出错：" + resultVo.getMsg());
            }
        } else {
            throw new BusException("商保平台获取Token出错：调用令牌 Token 获取接口未返回任何参数，请联系管理员！");
        }

        return dataVo;
    }

    /**
     * 发送Post请求
     *
     * @param param
     */
    private CiPackageVo sendPost(List<Object> param, Map<String, Object> paramInpt, User user) {
        //String busseID = null;
        //String pkPv = null;

        //组织报文头部信息
        CiHeadVo headVo = new CiHeadVo();
        headVo.setBusseID(CommonUtils.getPropValueStr(paramInpt, "busseID"));
        headVo.setSendTradeNum(String.format("%s-%s-%s", DateUtils.getDateTimeStr(new Date()), senderCode, ApplicationUtils.getCode("9901")));
        headVo.setSenderCode(senderCode);
        headVo.setSenderName(senderName);
        headVo.setReceiverCode(receiverCode);
        headVo.setReceiverName(receiverName);
        headVo.setProviderCode(providerCode);
        headVo.setProviderName(providerName);
        headVo.setStandardVersionCode(versionCode);
        //查询患者医保信息
        if (paramInpt.containsKey("pkPv")) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("pkPv", paramInpt.get("pkPv"));

            Map<String, Object> hpMap = cmrInsuMapper.qryHpInfoByPkpv(paramMap);
            if (hpMap != null && hpMap.size() > 0) {
                hpMap.get("pkHp");
                headVo.setIntermediaryCode(getDictMapInfo("intermediaryCode", CommonUtils.getPropValueStr(hpMap, "code"), null, null).getCodeInsur());
                headVo.setIntermediaryName(CommonUtils.getPropValueStr(hpMap, "name"));
            }
        } else {
            headVo.setIntermediaryCode("4");
            headVo.setIntermediaryName("自费");
        }

        headVo.setHosorgNum(user.getCodeEmp());
        headVo.setHosorgName(user.getNameEmp());
        headVo.setSystemType("1");
        headVo.setBusenissType("2");
        headVo.setStandardVersionCode(versionCode);
        headVo.setRecordCount(CommonUtils.getString(param.size()));

        //组织主参数入参
        CiPackageVo packParam = new CiPackageVo();
        packParam.setHead(headVo);
        packParam.setBody(param);
        packParam.setAdditionInfo(new CiAdditionInfo());

        Map<String, CiPackageVo> reqMap = new HashMap<>(16);
        reqMap.put("package", packParam);

        //请求参数转为json格式
        String reqJson = JsonUtil.writeValueAsString(reqMap);
        System.out.println("入参（" + MapUtils.getString(paramInpt, "busseID") + "）：\n" + reqJson + "\n");
        //json加密
        String reqParam = AesUtils.encryptByAes(ENCRYPT_KEY, reqJson);

        //校验token是否过期
        checkToken();

        Map<String, String> headerMap = new HashMap<>(16);
        headerMap.put("access_token", accessToken);
        String url = String.format("%sbiz/inbound/s?senderCode=%s&providerCode=", cmrInsuUrl, senderCode);
        String resParam = HttpClientUtil.sendHttpPostJson(url, reqParam, headerMap);

        if (resParam.contains("Token已过期")) {
            //重新获取Token信息
            accessToken = null;
            checkToken();

            //重新调用接口
            headerMap.clear();
            headerMap.put("access_token", accessToken);
            resParam = HttpClientUtil.sendHttpPostJson(url, reqParam, headerMap);
        }

        //响应结果解密
        String resJson = AesUtils.decryptByAes(DECRYPT_KEY, resParam);
        //解析package信息
        JSONObject jsonObject = JSONObject.parseObject(resJson);
        // 获取到key为shoppingCartItemList的值
        String packageJson = jsonObject.getString("package");
        System.out.print("出参（" + MapUtils.getString(paramInpt, "busseID") + "）：\n" + packageJson + "\n");
        CiPackageVo resVo = JsonUtil.readValue(packageJson, CiPackageVo.class);
        resVo.setResJson(packageJson);

        if (!"1".equals(resVo.getAdditionInfo().getErrorCode()) && !"0".equals(resVo.getAdditionInfo().getErrorCode())) {
            //调用接口失败，弹出报错信息
            throw new BusException(String.format("商保平台提示(业务接口:%s,交易流水:%s):%s", CommonUtils.getPropValueStr(paramInpt, "busseID"), resVo.getAdditionInfo().getReceiverTradeNum(), resVo.getAdditionInfo().getErrorMsg()));
        }

        return resVo;
    }

    /**
     * 校验Token是否过期，过期获取新token
     */
    private void checkToken() {
        if (!CommonUtils.isEmptyString(accessToken)) {
            //判断是否过期
            String chkDate = DateUtils.addDate(tokenDate, expiresIn, 6, "yyyy-MM-dd HH:mm:ss");
            Integer sec = DateUtils.getSecondBetween(new Date(), DateUtils.strToDate(chkDate, "yyyy-MM-dd HH:mm:ss"));
            if (sec < 180) {
                CiTokenDataVo dataVo = getToken();
                accessToken = dataVo.getAccess_token();
                expiresIn = dataVo.getExpires_in();
                tokenDate = new Date();
            }
        } else {
            CiTokenDataVo dataVo = getToken();
            accessToken = dataVo.getAccess_token();
            expiresIn = dataVo.getExpires_in();
            tokenDate = new Date();
        }
    }

    /**
     * 客户身份校验
     *
     * @param args
     */
    public Map<String, Object> sendS200(Object[] args) {
        /*
          入参
          pkPi:患者主键
          pkPv:就诊主键
          dtIdtype:证件类型
          idNo:证件号
          namePi:姓名
          dtSex:性别
          birthDate:出生日期
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];

        IUser user = (IUser) args[1];
        User u = (User) user;

        Map<String, Object> rtnMap = new HashMap<>(16);

        CiReqPatiVo reqPatiVo = new CiReqPatiVo();
        reqPatiVo.setCommercialBillNum(null);
        reqPatiVo.setTreatType(null);
        reqPatiVo.setQueryDate(null);
        reqPatiVo.setProfessionalCode(receiverCode);
        reqPatiVo.setProfessionalName(receiverName);

        List<PiMaster> piList = new ArrayList<>();
        if (!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "pkPi"))) {
            piList = cmrInsuMapper.getPimasterInfo(paramMap);
        }

        if (piList != null && piList.size() > 0) {
            PiMaster piInfo = piList.get(0);

            reqPatiVo.setCredentialType(getDictMapInfo("insuCredentialType", piInfo.getDtIdtype(), null, null).getCodeInsur());
            reqPatiVo.setCredentialNum(piInfo.getIdNo());
            reqPatiVo.setName(piInfo.getNamePi());
            reqPatiVo.setGender(getDictMapInfo("insuGender", piInfo.getDtSex(), null, null).getCodeInsur());
            reqPatiVo.setBirthday(DateUtils.getDateStr(piInfo.getBirthDate()));
        } else {
            //使用传入的信息查询患者商保信息
            reqPatiVo.setCredentialType(getDictMapInfo("insuCredentialType", CommonUtils.getPropValueStr(paramMap, "dtIdtype"), null, null).getCodeInsur());
            reqPatiVo.setCredentialNum(CommonUtils.getPropValueStr(paramMap, "idNo"));
            reqPatiVo.setName(CommonUtils.getPropValueStr(paramMap, "namePi"));
            reqPatiVo.setGender(getDictMapInfo("insuGender", CommonUtils.getPropValueStr(paramMap, "dtSex"), null, null).getCodeInsur());
            reqPatiVo.setBirthday(CommonUtils.getPropValueStr(paramMap, "birthDate"));
        }

        List<Object> reqList = new ArrayList<>();
        reqList.add(reqPatiVo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        paramInpt.put("busseID", "S200");
        CiPackageVo packageVo = sendPost(reqList, paramInpt, u);
        List<CiInsuPatiInfo> patiList = JsonUtil.readValue(JsonUtil.getJsonNode(packageVo.getResJson(), "body"), new TypeReference<List<CiInsuPatiInfo>>() {
        });

        return (Map<String, Object>) ApplicationUtils.beanToMap(patiList.get(0));
    }

    /**
     * 建立就诊档案
     *
     * @param args
     */
    public Map<String, Object> sendS210(Object[] args) {
        /*
          入参
          pkPi:患者主键
          pkPv:就诊主键
          professionalCode:商保公司代码
          professionalName:商保公司名称
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];

        IUser user = (IUser) args[1];
        User u = (User) user;

        //查询患者就诊基本信息
        List<Map<String, Object>> pvList = cmrInsuMapper.qryPvInfo(paramMap);
        if (pvList == null || pvList.size() <= 0) {
            throw new BusException("His系统提示：获取人员就诊信息失败，就诊人员信息查询为空！");
        }

        //查询患者入院诊断信息
        paramMap.put("dtDiagtype", "0100");
        List<CiIcdVo> diagList = cmrInsuMapper.qryDiagInfo(paramMap);
        List<CiIcdVo> cndiagList = cmrInsuMapper.qrycnDiagInfo(paramMap);

        CiCrtePatiVo reqpvInfo = TransfTool.mapToBean(CiCrtePatiVo.class, pvList.get(0));
        reqpvInfo.setProfessionalCode(CommonUtils.getPropValueStr(paramMap, "professionalCode"));
        reqpvInfo.setProfessionalName(CommonUtils.getPropValueStr(paramMap, "professionalName"));
        reqpvInfo.setMedicalType(getDictMapInfo("insuMedicalType", reqpvInfo.getMedicalType(), null, null).getCodeInsur());
        reqpvInfo.setCredentialType(getDictMapInfo("insuCredentialType", reqpvInfo.getCredentialType(), null, null).getCodeInsur());
        reqpvInfo.setGender(getDictMapInfo("insuGender", reqpvInfo.getGender(), null, null).getCodeInsur());
        reqpvInfo.setRace(getDictMapInfo("insuRace", reqpvInfo.getRace(), null, null).getCodeInsur());
        if (diagList != null && diagList.size() > 0) {
            reqpvInfo.setInHosClinicalDiagnosis(diagList.get(0).getDiagnosisName());
        } else if (cndiagList != null && cndiagList.size() > 0) {
            reqpvInfo.setInHosClinicalDiagnosis(cndiagList.get(0).getDiagnosisName());
        }

        if (diagList != null && paramMap.size() > 0) {
            reqpvInfo.setInHosDiagnosisList(diagList);
        }

        List<Object> reqList = new ArrayList<>();
        reqList.add(reqpvInfo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        paramInpt.put("busseID", "S210");
        paramInpt.put("pkPv", paramMap.get("pkPv"));

        CiPackageVo packageVo = sendPost(reqList, paramInpt, u);

        //处理his方业务，保存商保登记信息到数据库

        List<CiResCrtePatiVo> regList = JsonUtil.readValue(JsonUtil.getJsonNode(packageVo.getResJson(), "body"), new TypeReference<List<CiResCrtePatiVo>>() {
        });

        return (Map<String, Object>) ApplicationUtils.beanToMap(regList.get(0));
    }


    /**
     * 就诊档案修改
     */
    public Map<String, Object> sendS230(Object[] args) {

        /*
          入参
          pkPi:患者主键
          pkPv:就诊主键
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];

        IUser user = (IUser) args[1];
        User u = (User) user;

        //查询患者就诊基本信息
        List<Map<String, Object>> pvList = cmrInsuMapper.qryPvInfo(paramMap);
        if (pvList == null || pvList.size() <= 0) {
            throw new BusException("His系统提示：获取人员就诊信息失败，就诊人员信息查询为空！");
        }

        CiUpPatiVo reqpvInfo = TransfTool.mapToBean(CiUpPatiVo.class, pvList.get(0));

        reqpvInfo.setMedicalType(getDictMapInfo("insuMedicalType", reqpvInfo.getMedicalType(), null, null).getCodeInsur());
        reqpvInfo.setCredentialType(getDictMapInfo("insuCredentialType", reqpvInfo.getCredentialType(), null, null).getCodeInsur());
        //住院天数
        int ipDays = DateUtils.getDateSpaceByIn(DateUtils.strToDate(reqpvInfo.getTreatDate()), !CommonUtils.isEmptyString(reqpvInfo.getDischDate()) ? DateUtils.strToDate(reqpvInfo.getDischDate()) : new Date());
        reqpvInfo.setHospitalDay(String.valueOf(ipDays == 0 ? 1 : ipDays));

        //查询患者入院诊断信息
        paramMap.put("dtDiagtype", "0100");
        List<CiIcdVo> diagList = cmrInsuMapper.qryDiagInfo(paramMap);
        if (diagList != null && diagList.size() > 0) {
            reqpvInfo.setInHosDiagnosisList(diagList);
        }

        //查询患者出院诊断信息
        paramMap.put("dtDiagtype", "0109");
        List<CiIcdVo> diagOutList = cmrInsuMapper.qryDiagInfo(paramMap);
        if (diagOutList != null && diagOutList.size() > 0) {
            reqpvInfo.setDischDiagnosisList(diagOutList);
        }

        List<Object> reqList = new ArrayList<>();
        reqList.add(reqpvInfo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        paramInpt.put("busseID", "S230");
        paramInpt.put("pkPv", paramMap.get("pkPv"));

        CiPackageVo packageVo = sendPost(reqList, paramInpt, u);
        List<CiResCrtePatiVo> regList = JsonUtil.readValue(JsonUtil.getJsonNode(packageVo.getResJson(), "body"), new TypeReference<List<CiResCrtePatiVo>>() {
        });

        return (Map<String, Object>) ApplicationUtils.beanToMap(regList.get(0));
    }

    /**
     * 撤销就诊档案
     */
    public void sendS240(Object[] args) {
        /*
          入参
          pkPi:患者主键
          pkPv:就诊主键
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];

        IUser user = (IUser) args[1];
        User u = (User) user;

        PvEncounter pvInfo = cmrInsuMapper.qryPvinfo(paramMap);

        if (pvInfo == null) {
            throw new BusException("His系统提示：获取人员就诊信息失败，就诊人员信息查询为空！");
        }

        CiRegCanlReqVo reqVo = new CiRegCanlReqVo();
        reqVo.setMedicalNum(pvInfo.getCodePv());
        reqVo.setUpdateBy(u.getNameEmp());

        List<Object> reqList = new ArrayList<>();
        reqList.add(reqVo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        paramInpt.put("busseID", "S240");
        paramInpt.put("pkPv", paramMap.get("pkPv"));

        CiPackageVo packageVo = sendPost(reqList, paramInpt, u);
    }

    /**
     * 病历信息上传
     */
    public void sendS340(Object[] args) {

        /*
          入参
          pkPi:患者主键
          pkPv:就诊主键
          ipNum:住院号(可选，住院患者必须传入)
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];

        IUser user = (IUser) args[1];
        User u = (User) user;
        String pkPv = (String) paramMap.get("pkPv");

        //病历基本信息
        CiUpMedicalRecordVo medicalRecordVo = new CiUpMedicalRecordVo();
        //手术列表
        List<CiOperationVo> operationList = new ArrayList<CiOperationVo>();
        //诊断列表,入院、出院、门诊
        List<CiIcdVo> inHosDiagnosisList = new ArrayList<>();
        List<CiIcdVo> dischDiagnosisList = new ArrayList<>();
        List<CiIcdVo> outpatientDiagnosisList = new ArrayList<>();


        //获取病历上传所需数据(基本信息、诊断)
        List<Map<String, Object>> pvList = cmrInsuMapper.qryPvMedicalRecord(paramMap);
        if (CollectionUtils.isEmpty(pvList)) {
            throw new BusException("His系统提示：获取人员就诊信息失败，就诊人员信息查询为空！");
        }
        String emrResp = "";
        try {
            String strEmr = "<HtRequest>\n" +
                    "\t<MedicalDocument>\n" +
                    "\t\t<moduleid>HospitalRecord</moduleid>\n" +
                    "\t\t<ipid>" + MapUtils.getString(pvList.get(0), "medicalNum") + "</ipid>\n" +
//                    "\t\t<ipid>210004436</ipid>\n" +
                    "\t</MedicalDocument>\n" +
                    "</HtRequest>\n";
            String servicePath = ApplicationUtils.getPropertyValue("ciinsu.emrRecord", "");
            Object[] resObj = (Object[])WSUtil.invoke(servicePath, "intoEMR", "SERVICEMEDICALDOCUMENT", strEmr);
            emrResp = resObj[0].toString();
        } catch (Exception e) {
            throw new BusException("获取病历内容失败！\n电子病历接口调用失败：" + e.getMessage());
        }
        if (StringUtils.isEmpty(emrResp)) {
            throw new BusException("获取病历内容失败！\n未获取到患者病历信息！");
        }

        //组装基本信息
        medicalRecordVo.setMedicalNum(MapUtils.getString(pvList.get(0), "medicalNum"));
        medicalRecordVo.setHospitalRecordId(MapUtils.getString(pvList.get(0), "medicalNum"));
        medicalRecordVo.setInHospitalNum(MapUtils.getString(pvList.get(0), "inHospitalNum"));
        medicalRecordVo.setCheifComplaint("");//主述
        medicalRecordVo.setHistoryPresentIllness("");//现病史
        medicalRecordVo.setPastDiseaseHistory("");//既往史
        medicalRecordVo.setPersonalHistory("");//个人史
        medicalRecordVo.setObstetricalHistory("");//婚育史
        medicalRecordVo.setMenstruationHistory("");//月经史
        medicalRecordVo.setFamilyHistory("");//家族史
        medicalRecordVo.setInHosClinicalDiagnosis("");//入院临床诊断
        medicalRecordVo.setOperationProcedure("");//手术经过
        medicalRecordVo.setDiagnosisTreatment("");//诊治经过
        medicalRecordVo.setAttendingPhysician("");//主治医师
        medicalRecordVo.setDischargeStatus("");//出院情况
        medicalRecordVo.setDischargeStatus("");//出院医嘱
        medicalRecordVo.setPhysicalExamination("");//体格检查
        medicalRecordVo.setJuniorCollege("");//专科情况
        medicalRecordVo.setAuxiliaryExamination("");//辅助检查
        medicalRecordVo.setTotalRecordInfo(emrResp);//全量病历信息

        //组装诊断信息
        for (Map<String, Object> pv : pvList) {
            CiIcdVo diag = new CiIcdVo();
            diag.setDiagnosisCode(MapUtils.getString(pv, "diagnosisCode"));
            diag.setDiagnosisName(MapUtils.getString(pv, "diagnosisName"));
            diag.setDiagSort(MapUtils.getString(pv, "diagSort"));
            switch (MapUtils.getString(pv, "dtDiagtype")) {
                case "0109": //出院诊断
                    dischDiagnosisList.add(diag);
                    break;
                case "0100": //入院诊断
                    inHosDiagnosisList.add(diag);
                    break;
                case "0000": //门诊诊断
                    outpatientDiagnosisList.add(diag);
                    break;
            }
        }
        medicalRecordVo.setInHosDiagnosisList(inHosDiagnosisList);
        medicalRecordVo.setDischDiagnosisList(dischDiagnosisList);
        medicalRecordVo.setOutpatientDiagnosisList(outpatientDiagnosisList);


        //获取手术列表
        List<Map<String, Object>> opInfos = DataBaseHelper.queryForList("select " +
                "nvl(diag.DIAGCODE, '--') operation_code," +
                "nvl(op.NAME_OP, op.DESC_OP) operation_name," +
                "to_char(op.DATE_PLAN,'yyyymmddhh24miss') operation_time " +
                "from CN_ORDER ord " +
                "inner join CN_OP_APPLY op on op.PK_CNORD = ord.PK_CNORD " +
                "left join BD_TERM_DIAG diag on diag.PK_DIAG = op.PK_OP where ord.PK_PV = ?", new Object[]{pkPv});
        for (Map<String, Object> op : opInfos) {
            CiOperationVo opVo = new CiOperationVo();
            opVo.setOperationCode(MapUtils.getString(op, "operationCode"));
            opVo.setOperationName(MapUtils.getString(op, "operationName"));
            opVo.setOperationTime(MapUtils.getString(op, "operationTime"));
            operationList.add(opVo);
            medicalRecordVo.setOperationList(operationList);
        }

        List<Object> reqList = new ArrayList<>();
        reqList.add(medicalRecordVo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        paramInpt.put("busseID", "S340");
        paramInpt.put("pkPv", pkPv);

        CiPackageVo packageVo = sendPost(reqList, paramInpt, u);
    }

    /**
     * 获取对照信息
     *
     * @param codeHis
     * @param codeType 必传
     * @return
     */
    private InsSzybDictMap getDictMapInfo(String codeType, String codeHis, String pkHis, String codeInsur) {
        Map<String, Object> parmaMap = new HashMap<>(16);
        parmaMap.put("codeType", codeType);
        parmaMap.put("codeHis", codeHis);
        parmaMap.put("pkHis", pkHis);
        parmaMap.put("codeInsur", codeInsur);

        List<InsSzybDictMap> dictList = cmrInsuMapper.getDictMapInfo(parmaMap);
        if (dictList == null || dictList.size() == 0) {
            throw new BusException(String.format("His系统提示：获取对照信息失败！codeType(%s),codeHis(%s),pkHis(%s),codeInsur(%s)", codeType, codeHis, pkHis, codeInsur));
        }

        return dictList.get(0);
    }

    /**
     * 费用明细上传
     * 可以同时上传多条收费明细，单次交易上传不允许超过100条； 如果想退部分明细，可以采用单价为正常单价，数量为负值的方式调用本交易（也可以调用S270交易撤销明细）
     *
     * @param args
     */
    public void sendS250(Object[] args) {
        /*
         * 入参
         * pkPi:患者主键
         * pkPv:就诊主键
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];
        IUser user = (IUser) args[1];
        User u = (User) user;

        /*查询待上传费用明细*/
        List<CiUploadDtl> upLoadList = cmrInsuMapper.qryUpLoadDtList(paramMap);

        if (upLoadList != null && upLoadList.size() > 0) {
            upLoadList.forEach(dtinfo -> dtinfo.setUpdateBy(u.getNameEmp()));

            /*分组List，每99条为1组进行上传*/
            Map<String, List> upmap = new HashMap<>(16);            //用map存起来新的分组后数据
            int listSize = upLoadList.size();
            int toIndex = 99;
            int keyToken = 0;
            for (int i = 0; i < upLoadList.size(); i += 99) {
                if (i + 99 > listSize) {        //作用为toIndex最后没有900条数据则剩余几条newList中就装几条
                    toIndex = listSize - i;
                }
                List newList = upLoadList.subList(i, i + toIndex);
                upmap.put("upNum" + keyToken, newList);
                keyToken++;
            }

            Map<String, Object> paramInpt = new HashMap<>(16);
            paramInpt.put("busseID", "S250");
            paramInpt.put("pkPv", paramMap.get("pkPv"));

            //上传
            for (List uploadVo : upmap.values()) {
                //发送HTTP请求
                sendPost(uploadVo, paramInpt, u);
            }
        }
    }

    /**
     * 住院费用明细撤销
     */
    public void sendS260(Object[] args) {
        /*
         * 入参
         * pkPi:患者主键
         * pkPv:就诊主键
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];
        IUser user = (IUser) args[1];
        User u = (User) user;


        PvEncounter pvVo = cmrInsuMapper.qryPvinfo(paramMap);

        if (pvVo != null) {
            CiReqCanlUpdaload reqParam = new CiReqCanlUpdaload();
            reqParam.setMedicalNum(pvVo.getCodePv());
            reqParam.setUpdateDate(DateUtils.getDateTimeStr(new Date()));
            reqParam.setUpdateBy(u.getNameEmp());

            List<Object> reqList = new ArrayList<>();
            reqList.add(reqParam);

            Map<String, Object> paramInpt = new HashMap<>(16);
            paramInpt.put("busseID", "S260");
            paramInpt.put("pkPv", paramMap.get("pkPv"));

            //发送HTTP请求
            sendPost(reqList, paramInpt, u);
        }
    }

    /**
     * 费用预结算(S290、S291)
     * S290费用预结算最后不生成结算数据，只是为了在费用结算前给医院端提供一个试算的服务
     * S291输入、输出参数同费用预结算(S290)，费用结算时发票号不可为空
     *
     * @param args
     */
    public Map<String, Object> sendS290(Object[] args) {
        /*
         * 入参
         * pkPi:患者主键
         * pkPv:就诊主键
         * codeSt:S291结算时传入
         * flagMidSt:中途结算标识  true:中途结算  flase:非中途结算
         * dateSt:结算日期
         * dateEnd:出院日期
         * amountSt:总金额
         * amountCmrInsu:商保支付  结算时传入
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];
        IUser user = (IUser) args[1];
        User u = (User) user;

        Map<String, Object> rtnMap = new HashMap<>(16);

        //结算编码，预结算时传入空，结算传入值
        String codeSt = CommonUtils.getPropValueStr(paramMap, "codeSt");

        //获取患者就诊信息
        PvEncounter pvVo = cmrInsuMapper.qryPvinfo(paramMap);
        PvIp pvipVo = cmrInsuMapper.qryPvIpinfo(paramMap);

        //查询患者医保信息
        BdHp hpVo = DataBaseHelper.queryForBean(
                "select * from bd_hp where del_flag='0' and pk_hp = ?",
                BdHp.class, new Object[]{pvVo.getPkInsu()}
        );

        CiReqStVo reqVo = new CiReqStVo();
        reqVo.setMedicalNum(pvVo.getCodePv());
        //codeSt为空时标识预结算
        if (!CommonUtils.isEmptyString(codeSt)) {
            reqVo.setInvoiceNO(null);
            reqVo.setBillNum(codeSt);
            reqVo.setBusinessSerialNum(codeSt);
            reqVo.setCommercialInsurancePayment(CommonUtils.getDouble(paramMap.get("amountCmrInsu")));//商保支付
        }
        reqVo.setRelationNum(null);
        reqVo.setMedicalType(getDictMapInfo("insuMedicalType", pvVo.getEuPvtype(), null, null).getCodeInsur());
        reqVo.setSettleDate(DateUtils.getDateTimeStr(new Date()));
        reqVo.setDischDate(DateUtils.getDateTimeStr(DateUtils.strToDate(CommonUtils.getPropValueStr(paramMap, "dateSt"), "yyyyMMddHHmmss")));//中途结算时传入值
        reqVo.setDischDate(DateUtils.getDateTimeStr(pvVo.getDateEnd()));
        reqVo.setDischCause(getDictMapInfo("insudischCause", pvipVo.getDtOutcomes(), null, null).getCodeInsur());//出院原因
        reqVo.setHospitalDay(DateUtils.getDateSpaceByIn(pvVo.getDateBegin(), DateUtils.strToDate(CommonUtils.getPropValueStr(paramMap, "dateEnd"))));
        //查询诊断信息集合
        paramMap.put("dtDiagtype", "0109");
        List<CiIcdVo> icdList = cmrInsuMapper.qryDiagInfo(paramMap);
        if (icdList != null && icdList.size() > 0) {
            reqVo.setDischDiagnosisList(icdList);
            reqVo.setDischClinicalDiagnosis(icdList.get(icdList.size() - 1).getDiagnosisName());
        } else {
            throw new BusException("未查询到患者出院诊断信息！");
        }
        reqVo.setAreaCode(null);

        reqVo.setAreaName(null);
        reqVo.setMedicalInsurance(null);
        reqVo.setPersonnelCat(null);
        reqVo.setIsMedicalInsuranceSettlement(CommonUtils.isEmptyString(hpVo.getDtExthp()) ? "0" : "1");
        reqVo.setIsPaidByDiagnosis("0");
        reqVo.setSumMoney(CommonUtils.getDouble(paramMap.get("amountSt")));
        reqVo.setUpdateBy(u.getNameEmp());
        //发票费用列表
        reqVo.setInvoiceFeeList(cmrInsuMapper.qryInvDtList(paramMap));
        //医保患者查询医保结算信息
        if (!CommonUtils.isEmptyString(hpVo.getDtExthp())) {
            //医保原始费用列表
            reqVo.setMedFeeList(cmrInsuMapper.qryMedFeeList(paramMap));
            if ("03".equals(hpVo.getDtExthp())) {
                //查询深圳医保结算信息
                InsSzybStCity cityVo = cmrInsuMapper.qryStcityByPkPv(paramMap);
                reqVo.setSelfCareAmount(0D);
                reqVo.setSelfAmount(MathUtils.sub(CommonUtils.getDouble(paramMap.get("amountSt")), MathUtils.add(cityVo.getAmtJjzf(), cityVo.getAmtGrzhzf())));
                reqVo.setInInsureMoney(MathUtils.add(cityVo.getAmtJjzf(), cityVo.getAmtGrzhzf()));
                reqVo.setMedicareFundCost(cityVo.getAmtJjzf());//医保基金
                reqVo.setMedicarePayLine(0D);
                reqVo.setHosBearMoney(0D);
                reqVo.setPriorBurdenMoney(0D);
                reqVo.setFundMoney(cityVo.getAmtJjzf());//统筹基金支付
                reqVo.setCivilServantFundMoney(0D);
                reqVo.setSeriousFundMoney(0D);
                reqVo.setAccountFundMoney(cityVo.getAmtGrzhzf());
                reqVo.setCivilSubsidy(0D);
                reqVo.setOtherFundMoney(0D);
                reqVo.setCashMoney(MathUtils.sub(CommonUtils.getDouble(paramMap.get("amountSt")), MathUtils.add(cityVo.getAmtJjzf(), cityVo.getAmtGrzhzf())));//自费支付
            } else {
                //查询异地医保结算信息
                InsSzybStDiff diffVo = cmrInsuMapper.qryStdiffByPkPv(paramMap);
                if (diffVo != null) {
                    reqVo.setSumMoney(diffVo.getAkc264());
                    reqVo.setSelfCareAmount(0D);
                    reqVo.setSelfAmount(diffVo.getAkc253());
                    reqVo.setInInsureMoney(diffVo.getYka319());
                    reqVo.setMedicareFundCost(diffVo.getAkb068());//医保基金
                    reqVo.setMedicarePayLine(diffVo.getAka151());
                    reqVo.setHosBearMoney(0D);
                    reqVo.setPriorBurdenMoney(0D);
                    reqVo.setFundMoney(diffVo.getAke039());//统筹基金支付
                    reqVo.setCivilServantFundMoney(diffVo.getAke035());
                    reqVo.setSeriousFundMoney(diffVo.getYkc630());
                    reqVo.setAccountFundMoney(diffVo.getAkb066());
                    reqVo.setCivilSubsidy(diffVo.getYkc641());
                    reqVo.setOtherFundMoney(diffVo.getYkc639());
                    reqVo.setCashMoney(diffVo.getYkc624());//自费支付
                } else {
                    throw new BusException("未查询到异地医保结算信息！");
                }
            }
        }

        List<Object> reqList = new ArrayList<>();
        reqList.add(reqVo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        if (CommonUtils.isEmptyString(codeSt)) paramInpt.put("busseID", "S290");
        else paramInpt.put("busseID", "S291");
        paramInpt.put("pkPv", paramMap.get("pkPv"));

        //发送HTTP请求
        CiPackageVo resVo = sendPost(reqList, paramInpt, u);

        List<CiResStVo> stResList = JsonUtil.readValue(JsonUtil.getJsonNode(resVo.getResJson(), "body"), new TypeReference<List<CiResStVo>>() {
        });
        if (stResList != null && stResList.size() > 0) {
            CiResStVo stResVo = stResList.get(0);
            if (CommonUtils.isEmptyString(codeSt)) {
                InsTkybSt instVo = new InsTkybSt();
                instVo.setPkInsst(NHISUUID.getKeyId());
                instVo.setPkPi(pvVo.getPkPi());
                instVo.setPkPv(pvVo.getPkPv());
                instVo.setBillNum(stResVo.getBillNum());
                instVo.setSettleSerialNum(stResVo.getSettleSerialNum());
                instVo.setSettleDate(DateUtils.strToDate(stResVo.getSettleDate()));
                instVo.setMedicalCost(stResVo.getMedicalCost());
                instVo.setCommercialInsuranceCost(stResVo.getCommercialInsuranceCost());
                instVo.setSbFundPay(stResVo.getSbFundPay());
                instVo.setPersonalAmount(stResVo.getPersonalAmount());
                instVo.setAccumCompAmount(stResVo.getAccumCompAmount());
                instVo.setInsurHospAccumulatePayment(stResVo.getInsuranceHospitalAccumulatePayment());
                instVo.setInsuranceOutpatient(stResVo.getInsuranceOutpatient());
                instVo.setResidualAmount(stResVo.getResidualAmount());
                instVo.setIndemnitySign(stResVo.getIndemnitySign());
                instVo.setNonImmediateReason(stResVo.getNonImmediateReason());
                instVo.setWaiverSign(stResVo.getWaiverSign());
                instVo.setWaiverReason(stResVo.getWaiverReason());
                instVo.setCalculateInformation(stResVo.getCalculateInformation());
                instVo.setRemark(stResVo.getRemark());
                instVo.setPromptInformation(stResVo.getPromptInformation());
                instVo.setPolicyFileName(stResVo.getPolicyFileName());

                ApplicationUtils.setDefaultValue(instVo, true);
                DataBaseHelper.insertBean(instVo);
                rtnMap = (Map<String, Object>) ApplicationUtils.beanToMap(instVo);
            } else {
//                InsTkybSt stInfo = cmrInsuMapper.qryTkStInfo(paramMap);
                InsTkybSt stInfo = new InsTkybSt();
                ApplicationUtils.setDefaultValue(stInfo, true);
                stInfo.setPkPv(pvVo.getPkPv());
                stInfo.setPkPi(pvVo.getPkPi());
                //修改商保信息
                stInfo.setBillNum(stResVo.getBillNum());
                stInfo.setSettleSerialNum(stResVo.getSettleSerialNum());
                stInfo.setSettleDate(CommonUtils.isEmptyString(stResVo.getSettleDate()) ? null : DateUtils.strToDate(stResVo.getSettleDate()));
                stInfo.setMedicalCost(stResVo.getMedicalCost());
                stInfo.setCommercialInsuranceCost(stResVo.getCommercialInsuranceCost());
                stInfo.setSbFundPay(stResVo.getSbFundPay());
                stInfo.setPersonalAmount(stResVo.getPersonalAmount());
                stInfo.setAccumCompAmount(stResVo.getAccumCompAmount());
                stInfo.setInsurHospAccumulatePayment(stResVo.getInsuranceHospitalAccumulatePayment());
                stInfo.setInsuranceOutpatient(stResVo.getInsuranceOutpatient());
                stInfo.setResidualAmount(stResVo.getResidualAmount());
                stInfo.setIndemnitySign(stResVo.getIndemnitySign());
                stInfo.setNonImmediateReason(stResVo.getNonImmediateReason());
                stInfo.setWaiverSign(stResVo.getWaiverSign());
                stInfo.setWaiverReason(stResVo.getWaiverReason());
                stInfo.setCalculateInformation(stResVo.getCalculateInformation());
                stInfo.setRemark(stResVo.getRemark());
                stInfo.setPromptInformation(stResVo.getPromptInformation());
                stInfo.setPolicyFileName(stResVo.getPolicyFileName());
                DataBaseHelper.insertBean(stInfo);
                //返回商保结算信息
                rtnMap = (Map<String, Object>) ApplicationUtils.beanToMap(stInfo);
            }

        } else {
            throw new BusException("商保预结算失败，没有解析到预结算返回值参数！");
        }

        return rtnMap;
    }

    /**
     * 商保取消结算
     * 022004005011
     *
     * @param args
     */
    public void sendS270(Object[] args) {
        /*
         * 入参
         * pkPi:患者主键
         * pkPv:就诊主键
         * pkSettle:结算主键
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];
        IUser user = (IUser) args[1];
        User u = (User) user;

        InsTkybSt stInfo = cmrInsuMapper.qryTkStInfo(paramMap);

        CiReqCanlStVo reqVo = new CiReqCanlStVo();
        reqVo.setRevokeDate(DateUtils.getDateTimeStr(new Date()));
        reqVo.setUpdateBy(u.getNameEmp());
        reqVo.setIsRetainedFlg("0");
        reqVo.setSettleSerialNum(stInfo.getSettleSerialNum());
        reqVo.setMedicalNum(stInfo.getMedicalNum());
        reqVo.setBillNum(stInfo.getBillNum());

        List<Object> reqList = new ArrayList<>();
        reqList.add(reqVo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        paramInpt.put("busseID", "S270");
        paramInpt.put("pkPv", paramMap.get("pkPv"));

        //发送HTTP请求
        CiPackageVo resVo = sendPost(reqList, paramInpt, u);

    }

    /**
     * 理赔申请
     * 在客户确认之前支持重复调用
     *
     * @param args
     */
    public Map<String, Object> sendS300(Object[] args) {
        /*
         * 入参
         * pkPi:患者主键
         * pkPv:就诊主键
         * pkSettle:结算主键
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];
        IUser user = (IUser) args[1];
        User u = (User) user;

        Map<String, Object> rtnMap = new HashMap<>(16);

        //查询商保结算信息
        InsTkybSt stInfo = cmrInsuMapper.qryTkStInfo(paramMap);
        if (stInfo != null) {
            CiReqClaimVo reqVo = new CiReqClaimVo();
            reqVo.setSettleSerialNum(stInfo.getSettleSerialNum());

            List<Object> reqList = new ArrayList<>();
            reqList.add(reqVo);

            Map<String, Object> paramInpt = new HashMap<>(16);
            paramInpt.put("busseID", "S300");
            paramInpt.put("pkPv", paramMap.get("pkPv"));

            //发送HTTP请求
            CiPackageVo resVo = sendPost(reqList, paramInpt, u);

            List<CiResClaimVo> stResList = JsonUtil.readValue(JsonUtil.getJsonNode(resVo.getResJson(), "body"), new TypeReference<List<CiResClaimVo>>() {
            });
            stResList.get(0).setSettleSerialNum(stInfo.getSettleSerialNum());

            rtnMap = (Map<String, Object>) ApplicationUtils.beanToMap(stResList.get(0));

            DataBaseHelper.update("update INS_TKYB_ST set CONFIRMATION=?,CASE_STATUS=?,MNG_FINAL_PAY=?,CASE_END_DATE=? where PK_PV=?",
                    new Object[]{stResList.get(0).getConfirmation(), stResList.get(0).getCaseStatus(), stResList.get(0).getMngFinalPay(),
                            DateUtils.strToDate(stResList.get(0).getCaseEndDate(), "yyyyMMddHHmmss"), MapUtils.getString(paramMap, "pkPv")});

        } else {
            //无查询到商保结算信息

        }

        return rtnMap;
    }

    /**
     * 理赔结果确认
     *
     * @param args
     */
    public void sendS310(Object[] args) {
        /*
         * 入参
         * pkPi:患者主键
         * pkPv:就诊主键
         * acceptFlag:确认标识(0不接受,1接受)
         * settleSerialNum:结算流水号
         */
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) args[0];
        IUser user = (IUser) args[1];
        User u = (User) user;

        CiReqClaimAffVo reqVo = new CiReqClaimAffVo();
        reqVo.setSettleSerialNum(CommonUtils.getPropValueStr(paramMap, "settleSerialNum"));
        reqVo.setAcceptFlag(CommonUtils.getPropValueStr(paramMap, "acceptFlag"));

        List<Object> reqList = new ArrayList<>();
        reqList.add(reqVo);

        Map<String, Object> paramInpt = new HashMap<>(16);
        paramInpt.put("busseID", "S310");
        paramInpt.put("pkPv", paramMap.get("pkPv"));

        //发送HTTP请求
        CiPackageVo resVo = sendPost(reqList, paramInpt, u);

        //保存商保结算信息
        DataBaseHelper.execute(
                "update ins_tkyb_st set accept_Flag= ?  where pk_pv = ? and settle_Serial_Num = ?"
                , new Object[]{CommonUtils.getPropValueStr(paramMap, "acceptFlag"), paramMap.get("pkPv"), paramMap.get("settleSerialNum")}
        );

    }


}
