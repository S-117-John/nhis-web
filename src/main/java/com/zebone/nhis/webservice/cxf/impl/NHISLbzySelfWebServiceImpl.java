package com.zebone.nhis.webservice.cxf.impl;

import com.foxinmy.weixin4j.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.*;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.acc.PiCardDetail;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.pi.pub.service.PiCodeFactory;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.webservice.cxf.INHISLbzySelfWebService;
import com.zebone.nhis.webservice.lbzy.dao.SelfServiceMapper;
import com.zebone.nhis.webservice.lbzy.model.*;
import com.zebone.nhis.webservice.lbzy.model.ipin.QueryInHospital;
import com.zebone.nhis.webservice.lbzy.model.ipin.QueryInHospitalDay;
import com.zebone.nhis.webservice.lbzy.model.ipin.QueryInHospitalDetail;
import com.zebone.nhis.webservice.lbzy.model.ipin.QueryInPay;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryPayRecordDetailList;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryPayRecordDetailResult;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryToPresResult;
import com.zebone.nhis.webservice.lbzy.model.reg.ApptTimeItemResult;
import com.zebone.nhis.webservice.lbzy.model.reg.ApptTimeItemlList;
import com.zebone.nhis.webservice.lbzy.model.reg.RegApptVo;
import com.zebone.nhis.webservice.lbzy.service.LbzySelfService;
import com.zebone.nhis.webservice.lbzy.support.LbzySelfTools;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NHISLbzySelfWebServiceImpl implements INHISLbzySelfWebService {

    @Autowired
    private SelfServiceMapper selfServiceMapper;

    @Autowired
    private PiCodeFactory piCodeFactory;

    @Autowired
    private LbzySelfService lbzySelfService;

    private final static Logger logger = LoggerFactory.getLogger("nhis.lbzySelfServiceLog");

    private Map dom2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    private static final String PREFIX_XML = "<Response>";
    private static final String SUFFIX_XML = "</Response>";

    private static final String PREFIX_CDATA = "<![CDATA[";

    private static final String SUFFIX_CDATA = "]]>";


    private String mapToXml(Map<String, Object> parm, boolean isAddCDATA) {
        StringBuffer strbuff = new StringBuffer(PREFIX_XML);
        if (null != parm) {
            for (Map.Entry<String, Object> entry : parm.entrySet()) {
                strbuff.append("<").append(entry.getKey()).append(">");
                if (isAddCDATA) {
                    strbuff.append(PREFIX_CDATA);
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                    strbuff.append(SUFFIX_CDATA);
                } else {
                    if (null != entry.getValue()) {
                        if("List".equals(entry.getKey())){
                            strbuff.append(itemMapToXml((Map<String, Object>) entry.getValue(),false));
                        }else {
                            strbuff.append(entry.getValue());
                        }

                    }
                }
                strbuff.append("</").append(entry.getKey()).append(">");
            }
        }
        return strbuff.append(SUFFIX_XML).toString();
    }

    private String itemMapToXml(Map<String, Object> parm, boolean isAddCDATA) {
        StringBuffer strbuff = new StringBuffer();
        if (null != parm) {
            strbuff.append("<Item>");
            for (Map.Entry<String, Object> entry : parm.entrySet()) {
                strbuff.append("<").append(entry.getKey()).append(">");
                if (isAddCDATA) {
                    strbuff.append(PREFIX_CDATA);
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                    strbuff.append(SUFFIX_CDATA);
                } else {
                    if (null != entry.getValue()) {

                        strbuff.append(entry.getValue());

                    }
                }
                strbuff.append("</").append(entry.getKey()).append(">");
            }
            strbuff.append("</Item>");
        }
        return strbuff.toString();
    }

    @Override
    public String queryHISTime(String param) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(new ByteArrayInputStream(param.getBytes()));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element incomingForm = document.getRootElement();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String ss = dateTimeFormatter.format(localDateTime);
        Map<String, Object> result = new HashMap<>(16);
        result.put("ResultCode", "0");
        result.put("ErrorMsg", "");
        result.put("HISDateTime", ss);
        return mapToXml(result, false);
    }

    /**
     * 获取患者档案信息
     *
     * @param param
     * @return
     */
    @Override
    public String queryCardInfo(String param) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(new ByteArrayInputStream(param.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Element incomingForm = document.getRootElement();
        Map map = dom2Map(incomingForm);
        String cardNo = MapUtils.getString(map, "CardNO");
        String cardType = MapUtils.getString(map, "CardTypeID");
        if (StringUtils.isAnyEmpty(cardNo,cardType)) {
            throw new BusException("卡类型和卡号都不不能为空");
        }

        StringBuilder sbl = new StringBuilder("select pi.CODE_PI,pi.NAME_PI,pi.DT_SEX,pi.ID_NO,pi.BIRTH_DATE,pi.PK_PI,nvl(pi.mobile,pi.tel_no) mobile,card.EU_STATUS,card.CARD_NO from PI_MASTER pi left join PI_CARD card on pi.PK_PI=card.PK_PI where 1=1 ");
        Object object[] = {cardNo};
        if("5".equals(cardType)){
            sbl.append(" and pi.CODE_PI=?");
        } else if("2".equals(cardType)){
            sbl.append(" and pi.ID_NO=?");
        } else{
            sbl.append(" and card.CARD_NO=? and card.dt_cardtype=?");
            object =  new Object[]{cardNo,cardType};
        }
        List<Map<String, Object>> cardList = DataBaseHelper.queryForList(sbl.toString(), object);
        if(CollectionUtils.isEmpty(cardList)){
            throw new BusException("未查询到患者信息");
        }
        Map<String,Object> cardMap = cardList.get(0);
        String pkPi = MapUtils.getString(cardMap, "pkPi");
        Map<String, Object> accMap = DataBaseHelper.queryForMap("select AMT_ACC from PI_ACC where PK_PI = ?", pkPi);

        String patientId = MapUtils.getString(cardMap, "codePi", "");
        String patientName = MapUtils.getString(cardMap, "namePi", "");
        String patientSexID = LbzySelfTools.getSex(0,MapUtils.getString(cardMap, "dtSex"));
        String iDCardNO = MapUtils.getString(cardMap, "idNo", "");
        String balance = MapUtils.getString(accMap, "amtAcc", "");
        Date birthdayDate = (Date) MapUtils.getObject(cardMap, "birthDate", new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = simpleDateFormat.format(birthdayDate);
        String cardStatus = MapUtils.getString(cardMap, "euStatus", "");
        int patientAge;
        try {
            patientAge = DateUtils.getAge(birthdayDate);
        } catch (Exception e) {
            patientAge = 0;
        }

        Map<String,Object> result = Maps.newHashMap();
        result.put("ResultCode", "0");
        result.put("ErrorMsg", "");
        result.put("PatientID", patientId);
        result.put("PatientName", patientName);
        result.put("PatientSexID", patientSexID);
        result.put("IDCardNO", iDCardNO);
        result.put("Balance", balance);
        result.put("Birthday", birthday);
        result.put("PatientAge", patientAge);
        result.put("CardStatus", cardStatus);
        result.put("Mobile", MapUtils.getString(cardMap, "mobile"));
        result.put("BankCardNO", "");
        result.put("CardNO", "");

        return mapToXml(result, false);
    }

    /**
     * 3.1	建立档案信息
     *
     * @param param
     * @return
     */
    @Transactional(rollbackForClassName = { "Exception", "RuntimeException" })
    @Override
    public String createCardInfo(String param) {
        SAXReader saxReader = new SAXReader();
        saxReader.setEncoding("UTF-8");

        Document document = null;
        try {
            document = saxReader.read(new ByteArrayInputStream(param.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new BusException(e.getMessage());
        }
        Element incomingForm = document.getRootElement();
        Map paramMap = dom2Map(incomingForm);
        Map<String, Object> result = new HashMap<>(16);
        result.put("ResultCode", "0");
        result.put("ErrorMsg", "");

        String iDCardNO = MapUtils.getString(paramMap, "IDCardNO");
        String name = MapUtils.getString(paramMap, "PatientName");
        String cardNo = MapUtils.getString(paramMap, "CardNO");
        if(StringUtils.isAnyBlank(name,iDCardNO,cardNo)){
            throw new BusException("姓名、身份证号、卡号都不能为空");
        }
        if(DataBaseHelper.queryForScalar("select count(1) from PI_CARD where CARD_NO=? and DEL_FLAG='0'"
                ,Integer.class,new Object[]{cardNo})>0){
            throw new BusException("卡号已经注册，不允许重复提交");
        }
        Map<String, Object> piMap = DataBaseHelper.queryForMap("select pk_pi,code_pi from pi_master where id_no=? and name_pi=?", new Object[]{iDCardNO, name});
        String pkPi,codePi;
        User user = getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        if(MapUtils.isEmpty(piMap)){
            //0201
            codePi = ApplicationUtils.getCode("0201");
            //0202
            String codeOp = ApplicationUtils.getCode("0202");

            String codeIp = ApplicationUtils.getCode("0203");

            String sql = "select * from pi_master where del_flag = '0' and code_pi = ?";
            List<Map<String, Object>> piMasterList = DataBaseHelper.queryForList(sql, codePi);
            if(piMasterList.size()>0){
                result.put("ResultCode", "-1");
                result.put("ErrorMsg", "患者编码重复");
                return mapToXml(result, false);
            }
            sql = "select * from pi_master where del_flag = '0' and code_op = ?";
            piMasterList = DataBaseHelper.queryForList(sql, codeOp);
            if(piMasterList.size()>0){
                result.put("ResultCode", "-1");
                result.put("ErrorMsg", "患者门诊编码重复");
                return mapToXml(result, false);
            }
            PiMaster piMaster = new PiMaster();
            piMaster.setCodePi(codePi);
            piMaster.setCodeOp(codeOp);
            piMaster.setCodeIp(codeIp);
            piMaster.setNamePi(MapUtils.getString(paramMap, "PatientName", ""));
            piMaster.setDtSex(LbzySelfTools.getSex(1,MapUtils.getString(paramMap, "PatientSexID")));
            piMaster.setDtIdtype("01");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                piMaster.setBirthDate(formatter.parse(MapUtils.getString(paramMap, "Birthday", "")));
            } catch (ParseException e) {
            }
            piMaster.setIdNo(iDCardNO);
            piMaster.setAddrCurDt(MapUtils.getString(paramMap, "Address", ""));
            piMaster.setMobile(MapUtils.getString(paramMap, "Mobile", ""));
            piMaster.setCreator(MapUtils.getString(paramMap, "UserID", ""));
            sql = "select * from pi_cate where CODE = ?";
            PiCate piCate = DataBaseHelper.queryForBean(sql,PiCate.class,"01");
            piMaster.setPkPicate(piCate.getPkPicate());
            DataBaseHelper.insertBean(piMaster);
            pkPi= piMaster.getPkPi();

            //患者信息-账户
            PiAcc piAcc = new PiAcc();
            piAcc.setPkPi(pkPi);
            piAcc.setCodeAcc(piMaster.getCodeIp());
            piAcc.setAmtAcc(BigDecimal.ZERO);
            piAcc.setCreditAcc(BigDecimal.ZERO);
            piAcc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
            DataBaseHelper.insertBean(piAcc);

            //患者信息-保险计划
            sql = "select pk_hp from bd_hp where eu_hptype=0 and del_flag=0";
            BdHp bdHp = DataBaseHelper.queryForBean(sql,BdHp.class);
            PiInsurance piInsurance = new PiInsurance();
            piInsurance.setPkPi(piMaster.getPkPi());
            piInsurance.setSortNo(0L);
            piInsurance.setPkHp(bdHp!=null?bdHp.getPkHp():"");
            piInsurance.setDateBegin(new Date());
            piInsurance.setDateEnd(new Date());
            piInsurance.setFlagDef("1");
            piInsurance.setCreator(user.getPkEmp());
            piInsurance.setModifier(user.getPkEmp());
            piInsurance.setDelFlag("0");
            DataBaseHelper.insertBean(piInsurance);
        } else {
            pkPi = MapUtils.getString(piMap, "pkPi");
            codePi = MapUtils.getString(piMap, "codePi");
        }

        //卡信息
        PiCard piCard = new PiCard();
        piCard.setPkPi(pkPi);
        piCard.setEuStatus("0");
        piCard.setDtCardtype(MapUtils.getString(paramMap, "CardTypeID", ""));
        piCard.setCardNo(MapUtils.getString(paramMap, "CardNO", ""));
        piCard.setDtCardtype("01");
        piCard.setDateBegin(new Date());
        piCard.setSortNo(1);
        //查询就诊卡有效时间
        Map<String, Object> depDateMap = DataBaseHelper.queryForMap("select val from bd_sysparam where del_flag='0' and code='PI0001' and pk_org=? ",user.getPkOrg());
        int day = MapUtils.getIntValue(depDateMap, "val");
        Calendar date = Calendar.getInstance();
        date.setTime(piCard.getDateBegin());
        date.add(Calendar.DATE,day);
        piCard.setDateEnd(date.getTime());
        piCard.setFlagActive("1");
        DataBaseHelper.insertBean(piCard);
        DataBaseHelper.execute("update PI_CARD set FLAG_ACTIVE='0' where DT_CARDTYPE='01' and pk_pi=? and CARD_NO !=?", new Object[] { pkPi, piCard.getCardNo() });
        PiCardDetail cardDetail = new PiCardDetail();
        cardDetail.setPkPicard(piCard.getPkPicard());
        cardDetail.setPkPi(pkPi);
        cardDetail.setCardNo(piCard.getCardNo());
        cardDetail.setEuOptype(EnumerateParameter.ZERO);
        cardDetail.setPkEmpOpera(user.getPkEmp());
        cardDetail.setNameEmpOpera(user.getNameEmp());
        cardDetail.setDateHap(new Date());
        cardDetail.setDelFlag(EnumerateParameter.ZERO);
        DataBaseHelper.insertBean(cardDetail);

        result.put("ResultCode", "0");
        result.put("ErrorMsg", "");
        result.put("PatientID", codePi);
        result.put("TranSerialNO", "");
        result.put("TranTime", "");
        result.put("Balance", "");
        return mapToXml(result, false);
    }

    /**
     * 4.1	门诊充值
     *
     * @param param
     * @return
     */
    @Override
    public String doPrePay(String param) {
        Map<String, Object> paramMap = parseParam(param);
        Map<String, Object> paramList = (Map<String, Object>) MapUtils.getObject(paramMap, "List");
        Map<String, Object> paramItem = (Map<String, Object>) MapUtils.getObject(paramList, "Item");
        PiCard piCard = findPiCardByCardNo(MapUtils.getString(paramMap, "CardNO"));
//        PiAcc piAcc = findPiAccByPkPi(piCard.getPkPi());
        String receiptNo = ApplicationUtils.getCode("0603");
        User user = getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        BlExtPay extPay = new BlExtPay();
        extPay.setAmount(new BigDecimal(MapUtils.getString(paramItem, "Amount")));
        extPay.setEuPaytype(LbzySelfTools.getPayCode(1,MapUtils.getString(paramItem, "PayTypeID")));
        extPay.setFlagPay("0");
        extPay.setDatePay(new Date());
        String pOSTransNO = MapUtils.getString(paramItem, "POSTransNO");
        extPay.setTradeNo(pOSTransNO.split("|")[0]);
        extPay.setPkPi(piCard.getPkPi());
        DataBaseHelper.insertBean(extPay);

        Map<String,Object> hisParamMap = new HashMap<>(16);
        hisParamMap.put("euOptype", EnumerateParameter.ONE);
        hisParamMap.put("dtPaymode",  MapUtils.getString(paramMap, "CardTypeID"));
        hisParamMap.put("euDirect", EnumerateParameter.ONE);
        hisParamMap.put("pkPi", piCard.getPkPi());
        hisParamMap.put("amount", MapUtils.getString(paramItem, "Amount"));
        hisParamMap.put("datePay", new Date());
        hisParamMap.put("pkDept", ((User) user).getPkDept());
        hisParamMap.put("pkEmpPay", ((User) user).getPkEmp());
        hisParamMap.put("nameEmpPay", ((User) user).getNameEmp());
        hisParamMap.put("reptNo", receiptNo);
        ResponseJson  resultData =  ApplicationUtils.execService("PI", "CarddealService", "saveMonOperation", hisParamMap,user);
        if(resultData.getStatus() != Constant.SUC){
            throw new BusException(StringUtils.trimToEmpty(resultData.getErrorMessage())+" "+ StringUtils.trimToEmpty(resultData.getDesc()));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("ResultCode", "0");
        result.put("ErrorMsg", "");
        result.put("TranSerialNO", "");
        result.put("TranTime", "");
        result.put("Balance", "");
        return mapToXml(result, false);
    }

    /**
     * 查询门诊充值记录
     *
     * @param param
     * @return
     */
    @Override
    public String queryPrePayRecord(String param) {
        Map<String,Object> paramMap = parseParam(param);
        Map<String, Object> result = new HashMap<>(16);
        String codePi = MapUtils.getString(paramMap,"PatientID");
        String startTime = MapUtils.getString(paramMap,"StartDate");
        String endTime = MapUtils.getString(paramMap,"EndDate");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(StringUtils.isEmpty(codePi)){
            result.put("ResultCode", "1");
            result.put("ErrorMsg", "参数无效");
            return mapToXml(result,false);
        }

        try {
            paramMap.put("startTime",simpleDateFormat.parse(startTime));
            paramMap.put("endTime",simpleDateFormat.parse(endTime));
        } catch (ParseException e) {
            result.put("ResultCode", "1");
            result.put("ErrorMsg", "参数无效");
            return mapToXml(result,false);
        }
        paramMap.put("codePi",codePi);
        List<PiAccDetail> piAccDetailList = selfServiceMapper.findPiAccDetail(paramMap);
        if(piAccDetailList.size()==0){
            throw new BusException("未获取到数据");
        }
        List<QueryPrePayItem> itemList = piAccDetailList.stream().map(a->{
            QueryPrePayItem item = new QueryPrePayItem();
            item.setAmount(a.getAmount()+"");
            item.setPayTypeName("");
            item.setTranSerialNO(a.getPkAccdt());
            item.setTranTime(simpleDateFormat.format(a.getDateHap()));
            item.setUserID(a.getPkEmpOpera());
            return item;
        }).collect(Collectors.toList());
        QueryPrePayResult queryPrePayResult = new QueryPrePayResult();
        queryPrePayResult.setResultCode("0");
        QueryPrePayList queryPrePayList = new QueryPrePayList();
        queryPrePayList.setObjectList(itemList);
        queryPrePayResult.setList(queryPrePayList);
        return XmlUtil.beanToXml(queryPrePayResult, QueryPrePayResult.class);
    }

    /**
     * 查询挂号类别
     *
     * @param param
     * @return
     */
    @Override
    public String queryRegType(String param) {
//        Map<String,Object> paramMap = parseParam(param);
        QueryRegTypeResult result = new QueryRegTypeResult();
        result.setResultCode("0");
        QueryRegTypeList queryRegTypeList = new QueryRegTypeList();
        queryRegTypeList.setObjectList(Lists.newArrayList(new QueryRegTypeItem("0","普通")
                ,new QueryRegTypeItem("1","专家")
                ,new QueryRegTypeItem("2","特诊")
                ,new QueryRegTypeItem("3","优惠")
                ,new QueryRegTypeItem("9","急诊")
        ));
        result.setList(queryRegTypeList);
        return XmlUtil.beanToXml(result, QueryRegTypeResult.class);
    }

    /**
     * 查询一级挂号科室
     *
     * @param param
     * @return
     */
    @Override
    public String queryDepartmentLevelOneList(String param) {
        Map<String,Object> paramMap = parseParam(param);
        String userID = MapUtils.getString(paramMap,"UserID");
        String date = MapUtils.getString(paramMap,"Date");
        User user = getDefaultUser(userID);
        paramMap.put("pkOrg", user.getPkOrg());
        paramMap.put("dateBegin",date);
        paramMap.put("dateEnd",date);

        RegisterDeptResult registerDeptResult = new RegisterDeptResult();
        registerDeptResult.resultCode = "0";
        DeptItemList itemItemXml = new DeptItemList();

        List<Map<String,Object>> resultMap = selfServiceMapper.getRegisterDeptList(paramMap);
        if(resultMap.size()==0){
            throw new BusException("未获取到数据");
        }
        List<DeptItem> deptItemList = new ArrayList<>();
        resultMap.forEach(a -> {
            DeptItem deptItem = new DeptItem();
            deptItem.setDepartmentID(MapUtils.getString(a,"codeDept",""));
            deptItem.setDepartmentName(MapUtils.getString(a,"nameDept",""));
            deptItem.setIsChooseDoctor("1");
            deptItem.setIsEndPoint("1");
            deptItem.setCost("0");
            deptItemList.add(deptItem);
        });

        List<DeptItem> resultList = deptItemList.stream().filter(distinctByKey(DeptItem::getDepartmentID)).collect(Collectors.toList());

        itemItemXml.objectList = resultList;
        registerDeptResult.list = itemItemXml;

        return XmlUtil.beanToXml(registerDeptResult, RegisterDeptResult.class);
    }

    /**
     * 查询医生列表
     *
     * @param param
     * @return
     */
    @Override
    public String queryDoctorList(String param) {
        Map<String,Object> paramMap = parseParam(param);
        String date = MapUtils.getString(paramMap,"Date");
        String departmentID = MapUtils.getString(paramMap,"DepartmentID");
        String userID = MapUtils.getString(paramMap,"UserID");
        User user = getDefaultUser(userID);
        String day = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        paramMap.put("pkOrg", user.getPkOrg());
        paramMap.put("date",date);
        paramMap.put("timeStr",StringUtils.equals(date,day)?DateUtils.formatDate(new Date(),"HH:mm:ss"):null);
        paramMap.put("codeDept",departmentID);
        paramMap.put("euSrvtype",MapUtils.getString(paramMap,"RegTypeID"));
        paramMap.put("pkSch",MapUtils.getString(paramMap,"RegScheduleCode"));
        RegisterDoctorResult registerDoctorResult = new RegisterDoctorResult();
        registerDoctorResult.setResultCode("0");
        DoctorItemList list = new DoctorItemList();
        list.setObjectList(selfServiceMapper.getDoctorList(paramMap));
        if(CollectionUtils.isEmpty(list.getObjectList())){
            throw new BusException("未获取到数据");
        }
        registerDoctorResult.setList(list);

        return XmlUtil.beanToXml(registerDoctorResult, RegisterDoctorResult.class);
    }

    /**
     * 挂号
     *
     * @param param
     * @return
     */
    @Override
    public String doReg(String param) {
        Map<String,Object> paramMap = parseParam(param);
        if(StringUtils.isAnyEmpty(MapUtils.getString(paramMap,"PatientID"),MapUtils.getString(paramMap,"ScheduleCode"))){
            throw new BusException("患者ID,排班编码 都不能为空");
        }
        getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        RegApptVo regApptVo = lbzySelfService.doReg(paramMap);
        regApptVo.setResultCode("0");
        regApptVo.setErrorMsg("");
        return XmlUtil.beanToXml(regApptVo, RegApptVo.class);
    }

    /**
     * 预约挂号
     *
     * @param param
     * @return
     */
    @Override
    public String doPreReg(String param) {
        Map<String,Object> paramMap = parseParam(param);
        //SCH_SCH主键
        String scheduleCode = MapUtils.getString(paramMap,"ScheduleCode");
        String codePi = MapUtils.getString(paramMap,"PatientID");
        User user = getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        PiMaster piMaster = DataBaseHelper.queryForBean("select name_pi,pk_pi from PI_MASTER where code_pi=?", PiMaster.class, new Object[]{codePi});
        if(piMaster == null){
            throw new BusException("依据PatientID未获取到患者信息");
        }
        //获取排班信息
        SchSch sch = DataBaseHelper.queryForBean("select sch.PK_SCHRES,sch.PK_SCHSRV,sch.PK_DATESLOT,sch.EU_SCHCLASS " +
                " from SCH_SCH sch inner join SCH_RESOURCE res on sch.pk_schres=res.pk_schres inner join bd_ou_employee emp on res.pk_emp=emp.pk_emp" +
                " where sch.PK_SCH=? and emp.code_emp=? and sch.FLAG_STOP='0'",SchSch.class,new Object[]{scheduleCode, MapUtils.getString(paramMap,"DoctorID")});
        if(sch == null){
            throw new BusException("依据排班和医生编码未获取到有效排班信息");
        }
        SchSrv srv = DataBaseHelper.queryForBean("select EU_SRVTYPE from SCH_SRV where PK_SCHSRV=?",SchSrv.class,new Object[]{sch.getPkSchsrv()});
        PiMasterRegVo piMasterRegVo = new PiMasterRegVo();
        piMasterRegVo.setEuPvtype(srv==null?EnumerateParameter.ONE:("9".equals(srv.getEuSrvtype())?PvConstant.ENCOUNTER_EU_PVTYPE_2:PvConstant.ENCOUNTER_EU_PVTYPE_1));
        piMasterRegVo.setPkSch(scheduleCode);
        piMasterRegVo.setPkSchres(sch.getPkSchres());
        piMasterRegVo.setPkSchsrv(sch.getPkSchsrv());
        piMasterRegVo.setPkDateslot(sch.getPkDateslot());
        piMasterRegVo.setDateAppt(DateUtil.parseDate(MapUtils.getString(paramMap,"Date"),"yyyy-MM-dd"));
        piMasterRegVo.setEuSchclass(sch.getEuSchclass());
        piMasterRegVo.setOutsideOrderId(RandomStringUtils.randomNumeric(10));
        piMasterRegVo.setOrderSource("zzj");
        piMasterRegVo.setPkPi(piMaster.getPkPi());
        piMasterRegVo.setPkOrg(user.getPkOrg());
        piMasterRegVo.setNamePi(piMaster.getNamePi());
        ResponseJson result = ApplicationUtils.execService("WPV", "RegSyxHandler", "saveApptSchRegInfo", piMasterRegVo, user);
        if(result.getStatus()!= Constant.SUC){
            throw new BusException((StringUtils.trimToEmpty(result.getErrorMessage())+" "+ StringUtils.trimToEmpty(result.getDesc())));
        }
        piMasterRegVo = (PiMasterRegVo)result.getData();
        RegApptVo regApptVo = selfServiceMapper.qryApptInfo(piMasterRegVo.getPkAppt(),piMasterRegVo.getPkPi());
        if(regApptVo == null){
            throw new BusException("预约挂号结果-处理异常");
        }
        regApptVo.setResultCode("0");
        return XmlUtil.beanToXml(regApptVo, RegApptVo.class);
    }

    @Override
    public String payPreReg(String param){
        Map<String,Object> paramMap = parseParam(param);
        String scheduleCode = MapUtils.getString(paramMap,"ScheduleCode");
        String codePi = MapUtils.getString(paramMap,"PatientID");
        String codeAppt = MapUtils.getString(paramMap,"RegFlow");
        String date = MapUtils.getString(paramMap,"Date");
        if(StringUtils.isAnyEmpty(scheduleCode,codePi,codeAppt,date)){
            throw new BusException("日期|患者ID|流水号|排班编码 都不能为空");
        }
        getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        return XmlUtil.beanToXml(lbzySelfService.confirmApptReg(paramMap),RegApptVo.class);
    }

    @Override
    public String cancelOrder(String param){
        Map<String,Object> paramMap = parseParam(param);
        String codePi = MapUtils.getString(paramMap,"PatientID");
        String codeAppt = MapUtils.getString(paramMap,"RegFlow");
        getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        SchAppt schAppt = DataBaseHelper.queryForBean("select * from SCH_APPT appt inner join pi_master pi on appt.PK_PI=pi.PK_PI where CODE=? and pi.CODE_PI=?",
                SchAppt.class, new Object[]{codeAppt,codePi});
        if(schAppt == null){
            throw new BusException("没有对应预约信息！");
        }
        if("9".equals(schAppt.getEuStatus())){
            throw new BusException("该患者已经取消预约");
        }
        if("1".equals(schAppt.getEuStatus())){
            throw new BusException("该患者已经就诊过了");
        }
        if(schAppt.getEndTime().before(new Date())){
            throw new BusException("预约已过期，不能取消");
        }
        lbzySelfService.cancelAppt(schAppt);
        paramMap.clear();
        paramMap.put("ResultCode","0");
        return mapToXml(paramMap,false);
    }

    /**
     * 预约挂号查询
     *
     * @param param
     * @return
     */
    @Override
    public String queryPreRegRecord(String param) {
        Map<String,Object> paramMap = parseParam(param);
        String startDateString = MapUtils.getString(paramMap,"StartDate");
        String endDateString = MapUtils.getString(paramMap,"EndDate");
        String codePi = MapUtils.getString(paramMap,"PatientID");
        String userID = MapUtils.getString(paramMap,"UserID");
        getDefaultUser(userID);
        paramMap.put("startDate",startDateString);
        paramMap.put("endDate",endDateString);
        paramMap.put("codePi",codePi);
        List<PreRegRecordItem> recordItemList = selfServiceMapper.queryPreRegRecord(paramMap);
        if(recordItemList.size()==0){
            throw new BusException("未获取到符合条件的预约记录");
        }
        //设置费用
        for (PreRegRecordItem item : recordItemList) {
            paramMap.clear();
            paramMap.put("pkPres",item.getPkSchres());
            paramMap.put("pkSchsrv", item.getPkSchsrv());
            List<ItemPriceVo> voList = selfServiceMapper.qryCofirmRegFee(paramMap);
            if(voList.size() >0){
                item.setRegCost(voList.stream().mapToDouble(vo ->MathUtils.mul(vo.getPrice(),vo.getQuan())).sum());
            }
        }
        PreRegRecordList preRegRecordList = new PreRegRecordList(recordItemList);
        PreRegRecordResult preRegRecordResult = new PreRegRecordResult.Builder()
                .resultCode("0").errorMsg("").list(preRegRecordList).build();
        return XmlUtil.beanToXml(preRegRecordResult,PreRegRecordResult.class);
    }

    /**
     * 缴费
     *
     * @param param
     * @return
     */
    @Override
    public String doPay(String param) {
        Map<String,Object> paramMap = parseParam(param);
        if(StringUtils.isAnyBlank(MapUtils.getString(paramMap,"PatientID")
                ,MapUtils.getString(paramMap,"RegisterNO"))){
            throw new BusException("患者ID、挂号流水都不能为空");
        }
        getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        return mapToXml(lbzySelfService.opPay(paramMap), false);
    }

    /**
     * 查询待缴费
     *
     * @param param
     * @return
     */
    @Override
    public String queryToPayRegList(String param) {
        Map<String,Object> paramMap = parseParam(param);
        String codePi = MapUtils.getString(paramMap,"PatientID");
        if(StringUtils.isEmpty(codePi)){
            throw new BusException("患者ID必传");
        }
        getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        return XmlUtil.beanToXml(lbzySelfService.queryToPayRegList(codePi),QueryToPayResult.class);
    }

    /**
     * 查询待缴费--以处方为单位
     *
     * @param param
     * @return
     */
    @Override
    public String queryToPayList(String param) {
        Map<String,Object> paramMap = parseParam(param);
        String codePi = MapUtils.getString(paramMap,"PatientID");
        String codePv = MapUtils.getString(paramMap,"RegisterNO");
        if(StringUtils.isEmpty(codePi)){
            throw new BusException("患者ID必传");
        }
        return XmlUtil.beanToXml(lbzySelfService.queryToPayList(codePi,codePv), QueryToPresResult.class);
    }

    /**
     * 待缴费处方明细
     *
     * @param param
     * @return
     */
    @Override
    public String querytopaydetail(String param) {
        Map<String,Object> paramMap = parseParam(param);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"PatientID"))){
            throw new BusException("患者ID不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"RecipeSEQ"))){
            throw new BusException("处方号不能为空");
        }
        List<QueryToPayDetailItem> feesList = lbzySelfService.queryToPayDetail(paramMap);
        QueryToPayDetailItem.QueryToPayResult queryToPayResult = new QueryToPayDetailItem.QueryToPayResult();
        queryToPayResult.resultCode = "0";
        QueryToPayDetailItem.QueryToPayList queryToPayList = new QueryToPayDetailItem.QueryToPayList();
        queryToPayList.objectList = feesList;
        queryToPayResult.list = queryToPayList;
        if(CollectionUtils.isEmpty(feesList)){
            throw new BusException("未获取到数据");
        }
        return XmlUtil.beanToXml(queryToPayResult,queryToPayResult.getClass());
    }

    /**
     * 查询缴费记录列表
     *
     * @param param
     * @return
     */
    @Override
    public String queryPayRecordList(String param) {
        Map<String,Object> paramMap = parseParam(param);
        Map<String,Object> resultMap = new HashMap<>(16);
        String codePi = MapUtils.getString(paramMap,"PatientID");
        String startTime = MapUtils.getString(paramMap,"BeginDate");
        String endTime = MapUtils.getString(paramMap,"EndDate");
        String codeEmp = MapUtils.getString(paramMap,"UserID");
        if(StringUtils.isEmpty(codePi)){
            resultMap.put("ResultCode","-1");
            resultMap.put("ErrorMsg","请输入患者ID");
            return mapToXml(resultMap,false);
        }
        if(StringUtils.isAnyEmpty(startTime,endTime)){
            resultMap.put("ResultCode","-1");
            resultMap.put("ErrorMsg","开始时间|结束时间都不能为空");
            return mapToXml(resultMap,false);
        }
        paramMap.clear();
        paramMap.put("codePi",codePi);
        paramMap.put("startTime",startTime);
        paramMap.put("endTime",endTime);
        paramMap.put("codeEmp",codeEmp);
        QueryPayRecordResult result = new QueryPayRecordResult();
        result.setResultCode("0");
        QueryPayRecordList list = new QueryPayRecordList();
        list.setObjectList(selfServiceMapper.findBlDeposit(paramMap));
        result.setList(list);
        if(CollectionUtils.isEmpty(list.getObjectList())){
            throw new BusException("未获取到数据");
        }
        return XmlUtil.beanToXml(result,QueryPayRecordResult.class);
    }

    @Override
    public String queryPayRecordDetail(String param){
        Map<String,Object> paramMap = parseParam(param);
        String codePv = MapUtils.getString(paramMap,"RegisterNO");
        String codeSt = MapUtils.getString(paramMap,"InvoiceNO");
        String codeEmp = MapUtils.getString(paramMap,"UserID");
        if(StringUtils.isEmpty(codePv)){
            throw new BusException("挂号单据号不能为空");
        }
        paramMap.clear();
        paramMap.put("codePv",codePv);
        paramMap.put("codeSt",codeSt);
        paramMap.put("codeEmp",codeEmp);
        QueryPayRecordDetailResult result = new QueryPayRecordDetailResult();
        result.setResultCode("0");
        QueryPayRecordDetailList list = new QueryPayRecordDetailList();
        list.setObjectList(selfServiceMapper.qryPayRecordDetail(paramMap));
        result.setList(list);
        if(CollectionUtils.isEmpty(list.getObjectList())){
            throw new BusException("未获取到数据");
        }
        return XmlUtil.beanToXml(result,QueryPayRecordDetailResult.class);
    }
    /**
     * 查询患者未打印的检验报告列表
     *
     * @param param
     * @return
     */
    @Override
    public String queryLaboratoryRecordList(String param) {
        return null;
    }

    /**
     * 查询医院收费项目信息
     * @param param
     * @return
     */
    @Override
    public String queryDrugList(String param) {
        Map<String,Object> paramMap = parseParam(param);
        String type = MapUtils.getString(paramMap,"Type");
        String start = MapUtils.getString(paramMap,"Start");
        String end = MapUtils.getString(paramMap,"End");
        if(StringUtils.isEmpty(type)||StringUtils.isEmpty(start)||StringUtils.isEmpty(end)){
            throw new BusException("无效的参数");
        }
        MyBatisPage.startPage(Integer.valueOf(start), Integer.valueOf(end));
        QueryDrugResult result = new QueryDrugResult();
        result.setResultCode("0");
        QueryDrugList list = new QueryDrugList();
        list.setObjectList("1".equals(type)?selfServiceMapper.findDrug(paramMap):selfServiceMapper.findItem(paramMap));
        result.setList(list);
        if(CollectionUtils.isEmpty(list.getObjectList())){
            throw new BusException("未获取到数据");
        }
        return XmlUtil.beanToXml(result,QueryDrugResult.class);
    }

    /**
     * 查询挂号记录
     * @param param
     * @return
     */
    @Override
    public String queryRegRecord(String param) {
        Map<String,Object> paramMap = parseParam(param);
        String patientId = MapUtils.getString(paramMap,"PatientID");
        String start = MapUtils.getString(paramMap,"StartDate");
        String end = MapUtils.getString(paramMap,"EndDate");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            paramMap.put("startTime",simpleDateFormat.parse(start));
            paramMap.put("endTime",DateUtils.getDateMorning(simpleDateFormat.parse(end),1));
            paramMap.put("codePi",patientId);
        } catch (Exception e){
            throw new BusException(e);
        }
        QueryRegRecordList list = new QueryRegRecordList();
        list.setObjectList(selfServiceMapper.findRegRecord(paramMap));
        QueryRegRecordResult result = new QueryRegRecordResult();
        result.setResultCode("0");
        result.setList(list);
        if(CollectionUtils.isEmpty(list.getObjectList())){
            throw new BusException("未获取到数据");
        }
        return XmlUtil.beanToXml(result,QueryRegRecordResult.class);
    }

    /**
     * 查询预约时段列表
     *
     * @param param
     * @return
     */
    @Override
    public String queryPreRegDoctorList(String param) {
        Map<String,Object> paramMap = parseParam(param);
        if(StringUtils.isEmpty(MapUtils.getString(paramMap,"Date"))){
            throw new BusException("预约日期不能为空");
        }
        ApptTimeItemResult result = new ApptTimeItemResult();
        result.setResultCode("0");
        result.setList(new ApptTimeItemlList(Lists.newArrayList(selfServiceMapper.qryAptTime(paramMap))));
        if(CollectionUtils.isEmpty(result.getList().getObjectList())){
            throw new BusException("未获取到数据");
        }
        return XmlUtil.beanToXml(result, ApptTimeItemResult.class);
    }

    @Override
    public String queryInHospitalInfo(String param){
        Map<String,Object> paramMap = parseParam(param);
        String cardType = MapUtils.getString(paramMap, "CardTypeID");
        String card = MapUtils.getString(paramMap, "CardNO");
        if(StringUtils.isAnyBlank(card,cardType)){
            throw new BusException("卡号、卡类型都不能为空");
        }
        Map<String,Object> qryMap = Maps.newHashMap();
        qryMap.put("5".equals(cardType)?"codeIp":"idNo", card);
        QueryInHospital result = null;
        List<QueryInHospital> inHospitals = selfServiceMapper.queryInHospital(qryMap);
        if(inHospitals.size() ==0){
            throw new BusException("未获取到有效在院信息");
        }
        if(inHospitals.stream().map(QueryInHospital::getPkPv).collect(Collectors.toSet()).size()>1){
            throw new BusException("查到多条就诊记录，请联系管理员处理");
        }
        result = inHospitals.get(0);
        Map<String, Object> map = DataBaseHelper.queryForMap("select sum(dt.AMOUNT) total_Cost, sum(amount_hppi) pub_Cost,sum(amount_pi) pay_cost,sum(dt.AMOUNT) own_Cost" +
                " from BL_IP_DT dt where dt.PK_PV=? and dt.FLAG_SETTLE='0'", new Object[]{result.getPkPv()});
        result.setTotalCost(MapUtils.getDoubleValue(map, "totalCost",0D));
        result.setPubCost(MapUtils.getDoubleValue(map, "pubCost",0D));
        result.setPayCost(MapUtils.getDoubleValue(map, "payCost",0D));
        result.setOwnCost(MapUtils.getDoubleValue(map, "ownCost",0D));
        map = DataBaseHelper.queryForMap("select sum(AMOUNT) balance from BL_DEPOSIT where PK_PV=? and FLAG_SETTLE='0' and EU_PVTYPE='3' and eu_dptype='9'", new Object[]{result.getPkPv()});
        result.setPrePayBalance(MapUtils.getDoubleValue(map, "balance",0D));
        result.setResultCode("0");
        return XmlUtil.beanToXml(result, QueryInHospital.class);
    }

    @Override
    public String prePayInHospital(String param){
        Map<String,Object> paramMap = parseParam(param);
        getDefaultUser(MapUtils.getString(paramMap,"UserID"));
        return mapToXml(lbzySelfService.prePayInHospital(paramMap),false);
    }

    @Override
    public String queryPrePayInHospital(String param){
        Map<String,Object> paramMap = parseParam(param);
        if(StringUtils.isAnyBlank(MapUtils.getString(paramMap,"InPatientID")
                ,MapUtils.getString(paramMap,"StartDate")
                ,MapUtils.getString(paramMap,"EndDate"))){
            throw new BusException("住院号,开始日期，结束日期都不能为空");
        }
        paramMap.put("StartDate", MapUtils.getString(paramMap,"StartDate").replaceAll("-",""));
        paramMap.put("EndDate", MapUtils.getString(paramMap,"EndDate").replaceAll("-",""));
        List<QueryInPay> inPays = selfServiceMapper.qryPrePay(paramMap);
        if(CollectionUtils.isEmpty(inPays)){
            throw new BusException("未获取到记录");
        }
        return LbzySelfTools.geResSuccess(inPays,QueryInPay.class);
    }

    @Override
    public String queryInHospitalPayRecordList(String param){
        Map<String,Object> paramMap = parseParam(param);
        if(StringUtils.isAnyBlank(MapUtils.getString(paramMap,"InPatientID")
                ,MapUtils.getString(paramMap,"BeginDate")
                ,MapUtils.getString(paramMap,"EndDate"))){
            throw new BusException("住院号,开始时间，结束时间都不能为空");
        }
        List<QueryInHospitalDay> list = selfServiceMapper.queryFeeInDay(paramMap);
        if(CollectionUtils.isEmpty(list)){
            throw new BusException("未获取到记录");
        }
        return LbzySelfTools.geResSuccess(list,QueryInHospitalDay.class);
    }

    @Override
    public String queryInHospitalPayRecordDetail(String param){
        Map<String,Object> paramMap = parseParam(param);
        if(StringUtils.isAnyBlank(MapUtils.getString(paramMap,"InPatientID")
                ,MapUtils.getString(paramMap,"BeginDate")
                ,MapUtils.getString(paramMap,"EndDate"))){
            throw new BusException("住院号,开始时间，结束时间都不能为空");
        }
        List<QueryInHospitalDetail> details = selfServiceMapper.queryInHospitalDetail(paramMap);
        if(CollectionUtils.isEmpty(details)){
            throw new BusException("未获取到记录");
        }
        return LbzySelfTools.geResSuccess(details,QueryInHospitalDetail.class);
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
    private PiCard findPiCardByCardNo(String cardNo) {
        String sql = "select * from PI_CARD card where CARD_NO = ?  order by CREATE_TIME desc";

        List<PiCard> piCardList = DataBaseHelper.queryForList(sql, PiCard.class, cardNo);

        return piCardList.stream().findFirst().orElse(null);
    }


    private Map<String, Object> parseParam(String param) {
        SAXReader saxReader = new SAXReader();
        saxReader.setEncoding("UTF-8");
        Document document = null;
        try {
            document = saxReader.read(new ByteArrayInputStream(param.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Element incomingForm = document.getRootElement();
        Map paramMap = dom2Map(incomingForm);
        return paramMap;
    }

    private User getDefaultUser(String deviceId) {
        User user = new User();
        String sql = "SELECT emp.*,ej.pk_dept FROM bd_ou_employee emp" +
                " left join bd_ou_empjob ej on emp.PK_EMP=ej.PK_EMP and ej.IS_MAIN ='1' and ej.DEL_FLAG='0' WHERE emp.CODE_EMP = ?";
        Map<String, Object> bdOuMap = DataBaseHelper.queryForMap(sql,deviceId);
        if(MapUtils.isEmpty(bdOuMap)) {
            throw new BusException("依据UserID未获取到有效用户");
        }
        user.setPkOrg(MapUtils.getString(bdOuMap, "pkOrg"));
        user.setNameEmp(MapUtils.getString(bdOuMap, "nameEmp"));
        user.setPkOrg(MapUtils.getString(bdOuMap, "pkOrg"));
        user.setNameEmp(MapUtils.getString(bdOuMap, "nameEmp"));
        user.setPkEmp(MapUtils.getString(bdOuMap, "pkEmp"));
        user.setPkDept(MapUtils.getString(bdOuMap, "pkDept"));
        user.setCodeEmp(MapUtils.getString(bdOuMap, "codeEmp"));
        UserContext.setUser(user);
        return user;
    }

}
