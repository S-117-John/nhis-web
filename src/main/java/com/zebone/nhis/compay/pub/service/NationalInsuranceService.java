package com.zebone.nhis.compay.pub.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.qgyb.*;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDict;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.compay.ins.zsrm.service.qgyb.ZsrmQGUtils;
import com.zebone.nhis.compay.ins.zsrm.vo.InsQgybIteminfo;
import com.zebone.nhis.compay.ins.zsrm.vo.InsQgybOpspdiseinfo;
import com.zebone.nhis.compay.pub.dao.NationalInsuranceMapper;
import com.zebone.nhis.compay.pub.vo.*;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class NationalInsuranceService {

    private Logger logger = LoggerFactory.getLogger("nhis.ZsrmQGLog");
    @Value("#{applicationProperties['qgyb.url']}")
    private String HSA_URL;
    @Value("#{applicationProperties['qgyb.accountCode']}")
    private String HSA_ACCOUNT_CODE;
    @Value("#{applicationProperties['qgyb.paasid']}")
    private String HSA_PAASID;
    @Value("#{applicationProperties['qgyb.secretKey']}")
    private String SECRETKEY;
    @Value("#{applicationProperties['qgyb.fixmedins_name']}")
    private String FIXMEDINS_NAME;
    @Value("#{applicationProperties['qgyb.fixmedins_code']}")
    private String fixmedinsCode;
    @Value("#{applicationProperties['qgyb.version']}")
    private String VERSION;
    @Value("#{applicationProperties['qgyb.uploadFile.path']}")
    private String uploadFilePath;
    @Value("#{applicationProperties['qgyb.mdtrtareaAdmvs']}")
    private String MdtrtareaAdmvs;

    private static Pattern linePattern = Pattern.compile("_([a-z])");
    private static Pattern humpPattern = Pattern.compile("\\B(\\p{Upper})(\\p{Lower}*)");

    @Resource
    private NationalInsuranceMapper nationalInsuranceMapper;

    /**
     * ????????????015001015001
     * ????????????????????????(1101)
     * pkPi
     * pkPv
     * mdtrtCertNo
     */
    public HisInsuResVo getPersonInfo(String params, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String pkPi = CommonUtils.getPropValueStr(paramMap, "pkPi");
        String pkPv = CommonUtils.getPropValueStr(paramMap, "pkPv");
        PiMaster pi = null;
        if (CommonUtils.isNotNull(pkPi)) {
            pi = DataBaseHelper.queryForBean("select * from pi_master where pk_pi=?", PiMaster.class,
                    pkPi);
        }

        if (paramMap.containsKey("mdtrtCertNo")) {
            switch (CommonUtils.getPropValueStr(paramMap,"mdtrtCertNo")){
                case "01"://????????????
                    break;
                case "02"://?????????
                    // ??????????????????=15?????????18??????<15??? ?????????
                    String cernNo = CommonUtils.getString(paramMap.get("mdtrtCertNo"));
                    if (cernNo.length() == 15) {
                        // 15?????????????????????18???
                        paramMap.put("mdtrtCertNo", ZsrmQGUtils.getEighteenIDCard(cernNo));
                    } else {
                        if (pi != null && !"01".equals(pi.getDtIdtype())) {
                            // ????????????????????????
                            paramMap.put("mdtrtCertType", "99");
                            paramMap.put("certno", pi.getIdNo());
                        }
                    }
                    break;
                case "03"://?????????
                    // ??????????????????=15?????????18??????<15??? ?????????
                    String certno = CommonUtils.getString(paramMap.get("certno"));
                    if (certno.length() == 15) {
                        // 15?????????????????????18???
                        paramMap.put("certno", ZsrmQGUtils.getEighteenIDCard(certno));
                    }
                    break;
                default:;
            }
        }

        //??????Http??????????????????
        HisInsuResVo insuResVo = sendHttpPost("1101",getInputMap("data",paramMap), user);

        InsPiInfoVo inspi = JsonUtil.readValue((String)insuResVo.getResVo().getOutput(), InsPiInfoVo.class);

        if (CommonUtils.isNotNull(pkPv)) {
            String sql = "select * from ins_qgyb_master where psn_cert_type='01' and certno=? ";
            InsQgybMaster insMaster = DataBaseHelper.queryForBean(sql, InsQgybMaster.class,
                    inspi.getBaseinfo().getCertno());
            if (insMaster != null) {
                DataBaseHelper.deleteBeanByPk(insMaster);
                DataBaseHelper.execute("delete ins_qgyb_insutype where pk_insupi=?", insMaster.getPkInsupi());
                DataBaseHelper.execute("delete ins_qgyb_idet where pk_insupi=?", insMaster.getPkInsupi());
            }
            insMaster = inspi.getBaseinfo();
            ApplicationUtils.setDefaultValue(insMaster, true);
            insMaster.setPkPi(pkPi);
            String insu390 = "";
            String insu310 = "";
            List<InsQgybInsutype> insutypeList = new ArrayList<InsQgybInsutype>();
            for (InsQgybInsutype insu : inspi.getInsuinfo()) {
                //????????????????????????????????????
                if(EnumerateParameter.ONE.equals(insu.getPsnInsuStas())) {
                    if("390".equals(insu.getInsutype())){
                        insu390 = insu.getInsutype();
                    }
                    if("310".equals(insu.getInsutype())){
                        insu310 = insu.getInsutype();
                    }
                    insu.setPkInsupi(insMaster.getPkInsupi());
                    ApplicationUtils.setDefaultValue(insu, true);
                    insutypeList.add(insu);
                }
            }
            inspi.setInsuinfo(insutypeList);
            for (InsQgybIdet idet : inspi.getIdetinfo()) {
                idet.setPkInsupi(insMaster.getPkInsupi());
                ApplicationUtils.setDefaultValue(idet, true);
            }

            DataBaseHelper.insertBean(insMaster);
            if(insutypeList != null && insutypeList.size() > 0) {
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybInsutype.class), insutypeList);
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybIdet.class), inspi.getIdetinfo());


            if(CommonUtils.isNull(insu390) && CommonUtils.isNull(insu310)) {
                throw new BusException("?????????1101??????????????????????????????????????????  ");
            }
            //?????????????????????1???????????????????????????  2?????????310-?????????????????????390-??????
            paramMap.put("insutype", CommonUtils.isNotNull(insu310) ? insu310 : insu390);
            paramMap.put("pkPv", pkPv);
            nationalInsuranceMapper.updatePv(paramMap);
        }
        return insuResVo;
    }

    /**
     * ????????????015001015002
     * ??????(9001)
     * @param params
     *
     */
    public HisInsuResVo signOper(String params, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        User userInfo = (User)user;
        HisInsuResVo insuResVo = null;
        if ("true".equals(ApplicationUtils.getPropertyValue("qgyb.signInFlag", ""))) {
            Map<String, Object> reqMap = new HashMap();
            reqMap.put("opterNo", paramMap.get("opterNo"));
            reqMap.put("mac", paramMap.get("mac"));
            reqMap.put("ip", paramMap.get("ip"));

            insuResVo = sendHttpPost("9001",getInputMap("signIn",reqMap),user);

            //????????????????????????
            if(EnumerateParameter.ZERO.equals(insuResVo.getInfcode())){
                String preStJsonStr=JsonUtil.getJsonNode(insuResVo.getResJsonStr(), "output").toString();
                Map<String, Object> retMap = JsonUtil.readValue(
                        JsonUtil.getJsonNode(preStJsonStr, "signinoutb"),
                        Map.class);

                InsSignInQg signVo = new InsSignInQg();
                signVo.setOpterNo(CommonUtils.getPropValueStr(paramMap,"opterNo"));
                signVo.setMac(CommonUtils.getPropValueStr(paramMap,"mac"));
                signVo.setIp(CommonUtils.getPropValueStr(paramMap,"ip"));
                signVo.setSignTime(new Date());
                signVo.setSignNo(CommonUtils.getPropValueStr(retMap,"signNo"));
                signVo.setStatus(EnumerateParameter.ONE);

                ApplicationUtils.setDefaultValue(signVo,true);

                DataBaseHelper.insertBean(signVo);
            }
        }

        return insuResVo;
    }

    /**
     * ?????? 9002
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo signBackOper(String params, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        User userInfo = (User)user;
        HisInsuResVo insuResVo = null;
        if ("true".equals(ApplicationUtils.getPropertyValue("qgyb.signInFlag", ""))) {
            Object obj = RedisUtils.getCacheObj("qgybSignIn" + userInfo.getCodeEmp());
            if (obj == null) {
                Map<String, Object> reqMap = new HashMap();
                reqMap.put("opterNo", paramMap.get("opterNo"));
                reqMap.put("signNo", paramMap.get("signNo"));

                insuResVo = sendHttpPost("9002",getInputMap("signOut",reqMap),user);

                //????????????????????????????????????
                if(EnumerateParameter.ZERO.equals(insuResVo.getInfcode())){
                    DataBaseHelper.execute(
                            "update INS_SIGN_IN_QG set SIGN_OUT_TIME = sysdate,status = '2' where opter_no = ? and status = '1'"
                            , new Object[]{paramMap.get("opterNo")}
                    );
                }
            }
        }

        return insuResVo;
    }

    /**
     * ????????????015001015003
     * ????????????(2201)
     * @param params pkSch,psnNo:???????????????insutype:???????????????mdtrtCertType:?????????????????????mdtrtCertNo:??????????????????
     *               iptOtpNo:??????/????????????atddrNo:???????????????drName:???????????????deptCode:???????????????deptName:???????????????caty:??????
     *               begntime:????????????
     * @param user
     * @return
     */
    public HisInsuResVo outPatientRegister(String params, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = null;

        Map<String, Object> inputMap = new HashMap<>(16);
        //??????????????????pkPv?????????pkPv?????????pkPv????????????
        if(paramMap.containsKey("pkPv") && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"pkPv"))){
            inputMap.putAll(nationalInsuranceMapper.qryInsRegPre(CommonUtils.getPropValueStr(paramMap,"pkPv")));

        }else{
            Map<String,Object> resInfo = nationalInsuranceMapper.qrySrvResInfo(paramMap);

            inputMap.put("psnNo",paramMap.get("psnNo"));
            inputMap.put("insutype",paramMap.get("insutype"));
            inputMap.put("begntime",paramMap.get("begntime"));
            inputMap.put("mdtrtCertType",paramMap.get("mdtrtCertType"));
            inputMap.put("mdtrtCertNo",paramMap.get("mdtrtCertNo"));
            inputMap.put("iptOtpNo",paramMap.get("iptOtpNo"));
            inputMap.put("atddrNo",resInfo.get("codeEmp"));
            inputMap.put("drName",resInfo.get("nameEmp"));
            inputMap.put("deptCode",resInfo.get("codeDept"));
            inputMap.put("deptName",resInfo.get("nameDept"));
            inputMap.put("caty",paramMap.get("caty"));
        }

        insuResVo = sendHttpPost("2201",getInputMap("data",inputMap) ,user);

        return insuResVo;
    }

    /**
     * ????????????015001015004
     * ????????????(2202)
     * @param params    pkPv???
     *                  pkPv??????????????????????????????????????????psnNo??????????????????mdtrtId???????????????iptOtpNo????????????
     * @param user
     * @return
     */
    public HisInsuResVo outPatientRegCanl(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = null;

        Map<String, Object> inputMap = new HashMap<>(16);

        if(paramMap.containsKey("pkPv") && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"pkPv"))){
            List<InsQgybVisit> visitList = nationalInsuranceMapper.qryVisitInfo(paramMap);
            if(visitList!=null && visitList.size()>0){
                PiMaster piVo = DataBaseHelper.queryForBean(
                        "select * from pi_master where pk_pi = ?",
                        PiMaster.class,visitList.get(0).getPkPi());

                inputMap.put("psnNo",visitList.get(0).getPsnNo());
                inputMap.put("mdtrtId",visitList.get(0).getMdtrtId());
                inputMap.put("iptOtpNo","OPSD001");//piVo.getCodeOp()
            }
        }else{
            inputMap.put("psnNo",paramMap.get("psnNo"));
            inputMap.put("mdtrtId",paramMap.get("mdtrtId"));
            inputMap.put("iptOtpNo",paramMap.get("iptOtpNo"));
        }

        insuResVo = sendHttpPost("2202", getInputMap("data",inputMap),user);

        //????????????????????????????????????
        DataBaseHelper.execute("update ins_qgyb_visit set del_flag = '1' where mdtrt_id = ?",new Object[]{paramMap.get("mdtrtId")});

        return insuResVo;
    }

    /**
     * ??????????????????A
     * @param params    pkPv,medType:????????????
     * @param user
     * @return
     */
    public HisInsuResVo mdtrtinfoUpload(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();
        //????????????
        String medType = CommonUtils.getPropValueStr(paramMap,"medType");
        //????????????
        String diseCodg = CommonUtils.getPropValueStr(paramMap,"diseCodg");
        //????????????
        String diseName = CommonUtils.getPropValueStr(paramMap,"diseName");
        //????????????????????????
        String birctrlType = CommonUtils.getPropValueStr(paramMap,"birctrlType");
        //?????????????????????????????????
        String birctrlMatnDate = CommonUtils.getPropValueStr(paramMap,"birctrlMatnDate");
        //????????????
        String matnType = CommonUtils.getPropValueStr(paramMap,"matnType");
        //?????????
        String gesoVal = CommonUtils.getPropValueStr(paramMap,"gesoVal");

        //???????????????????????????
        List<Map<String,Object>> visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);
        if(visitListMap==null || visitListMap.size()<=0){
            paramMap.remove("medType");
            visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);

            if(visitListMap==null || visitListMap.size()<=0){
                insuResVo.setInfcode("-1");
                insuResVo.setErrMsg(String.format("???????????????????????????????????????????????????!"));

                throw new BusException(JsonUtil.writeValueAsString(insuResVo));
            }
        }



        //Map<String,Object> inputPvMap = (Map<String,Object>)ApplicationUtils.beanToMap(pvInfo);

        Map<String,Object> inputPvMap = new HashMap<>(16);
        inputPvMap.put("mainCondDscr",visitListMap.get(0).get("mainCondDscr"));
        inputPvMap.put("diseCodg",diseCodg);
        inputPvMap.put("diseName",diseName);
        inputPvMap.put("birctrlType",birctrlType);
        inputPvMap.put("birctrlMatnDate",birctrlMatnDate);
        inputPvMap.put("matnType",matnType);
        inputPvMap.put("gesoVal",gesoVal);
        inputPvMap.put("mdtrtId",visitListMap.get(0).get("mdtrtId"));
        inputPvMap.put("medType",medType);
        inputPvMap.put("psnNo",visitListMap.get(0).get("psnNo"));
        inputPvMap.put("begntime",visitListMap.get(0).get("begntime"));

        Map<String,Object> inputMap = getInputMap("mdtrtinfo",inputPvMap);
        inputMap.put("diseinfo",nationalInsuranceMapper.qryPvDiag(paramMap));//??????????????????

        insuResVo = sendHttpPost("2203A",inputMap,user);

        //??????????????????????????????ins_qgyb_pv???
        try {
            InsQgybPV pvInfo = new InsQgybPV();
            pvInfo.setDiseCodg(diseCodg);
            pvInfo.setDiseName(diseName);
            pvInfo.setBirctrlType(birctrlType);
            pvInfo.setBirctrlMatnDate(!CommonUtils.isEmptyString(birctrlMatnDate)?DateUtils.strToDate(birctrlMatnDate):null);
            pvInfo.setMatnType(matnType);
            pvInfo.setGesoVal(gesoVal);
            pvInfo.setMedType(medType);
            pvInfo.setMdtrtId(CommonUtils.getPropValueStr(visitListMap.get(0),"mdtrtId"));
            pvInfo.setMainCondDscr(CommonUtils.getPropValueStr(visitListMap.get(0),"mainCondDscr"));
            pvInfo.setPkPv(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPv"));
            pvInfo.setPkPi(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPi"));
            pvInfo.setPkHp(CommonUtils.getPropValueStr(visitListMap.get(0),"pkHp"));
            pvInfo.setNamePi(CommonUtils.getPropValueStr(visitListMap.get(0),"namePi"));
            pvInfo.setPkPv(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPv"));

            ApplicationUtils.setDefaultValue(pvInfo,true);
            DataBaseHelper.insertBean(pvInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return insuResVo;
    }

    /**
     * ????????????
     * @param params    pkPv,pkCgList ??????????????????
     * @param user
     * @return
     */
    public HisInsuResVo insuBillUpload(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        //????????????????????????
        List<Map<String, Object>> dts = new ArrayList<>();

        //?????????????????????????????????????????????
        if(paramMap.containsKey("flagRebackFee")
                && EnumerateParameter.ONE.equals(CommonUtils.getPropValueStr(paramMap,"flagRebackFee"))){
            //??????????????????????????????????????????????????????????????????pkCgop???quan???price
            List<BlOpDt> oldDtList = JsonUtil.readValue(
                    JsonUtil.getJsonNode(params, "newUploadList"),
                    new TypeReference<List<BlOpDt>>() {
                    });

            paramMap.put("rePay", "0");

            //??????????????????????????????
            dts = nationalInsuranceMapper.qryChargeDetailNoUpload(paramMap);

            //????????????????????????
            if(dts==null || dts.size()<=0){
                insuResVo.setInfcode("-1");
                insuResVo.setErrMsg("HIS????????????:???????????????????????????????????????????????????????????????????????????");

                throw new BusException(JsonUtil.writeValueAsString(insuResVo));
            }

            dts.stream()
                    .forEach(newDt->{
                        List<BlOpDt> oldVoList = oldDtList.stream().filter(oldDt-> oldDt.getPkCgop().equals(CommonUtils.getPropValueStr(newDt,"pkCg")))
                                .collect(Collectors.toList());

                        newDt.remove("cnt");
                        newDt.remove("detItemFeeSumamt");
                        newDt.put("cnt",oldVoList.get(0).getQuan());
                        newDt.put("det_item_fee_sumamt",MathUtils.mul(oldVoList.get(0).getQuan(),CommonUtils.getDouble(newDt.get("pric"))));
                    });
        }else{
            //?????????????????????????????????????????????bl_op_dt????????????????????????????????????????????????
            if(paramMap.containsKey("flagReg")
                && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"flagReg"))
                && EnumerateParameter.ONE.equals(CommonUtils.getPropValueStr(paramMap,"flagReg"))
            ){
                List<BlOpDt> itemList = JsonUtil.readValue(
                        JsonUtil.getJsonNode(params, "itemList"),
                        new TypeReference<List<BlOpDt>>() {
                        });

                List<String> pkItemList = itemList.stream().map(vo-> vo.getPkItem()).collect(Collectors.toList());

                if(pkItemList==null && pkItemList.size()<=0){
                    insuResVo.setInfcode("-1");
                    insuResVo.setErrMsg("HIS????????????:?????????????????????????????????????????????????????????");

                    throw new BusException(JsonUtil.writeValueAsString(insuResVo));
                }

                //??????????????????????????????
                List<InsQgybVisit> visitList = nationalInsuranceMapper.qryVisitInfo(paramMap);

                if(visitList==null || visitList.size()<=0){
                    insuResVo.setInfcode("-1");
                    insuResVo.setErrMsg(String.format("HIS????????????:?????????????????????????????????????????????????????????(pk_visit:%s)???",paramMap.get("pkVisit")));

                    throw new BusException(JsonUtil.writeValueAsString(insuResVo));
                }

                //????????????????????????
                List<Map<String,Object>> upList = new ArrayList<>();

                paramMap.put("pkList",pkItemList);
                List<Map<String,Object>> uploadItemList = nationalInsuranceMapper.qryRegChargeDetail(paramMap);

                itemList.stream().forEach(itemVo -> {
                    Map<String,Object> dtMap = new HashMap<>(16);

                    List<Map<String,Object>> upMap = uploadItemList.stream()
                            .filter(upVo -> itemVo.getPkItem().equals(CommonUtils.getPropValueStr(upVo,"pkItem"))).collect(Collectors.toList());

                    if(upMap!=null && upMap.size()>0){
                        dtMap.put("feedetlSn",ApplicationUtils.getCode("0602"));
                        dtMap.put("mdtrtId",visitList.get(0).getMdtrtId());
                        dtMap.put("psnNo",visitList.get(0).getPsnNo());
                        dtMap.put("chrgBchno",CommonUtils.getPropValueStr(paramMap,"chrgBchno"));
                        dtMap.put("rxCircFlag","0");
                        dtMap.put("feeOcurTime",DateUtils.getDateStr(new Date()));
                        dtMap.put("medListCodg",CommonUtils.getPropValueStr(upMap.get(0),"medListCodg"));
                        dtMap.put("medinsListCodg",CommonUtils.getPropValueStr(upMap.get(0),"medinsListCodg"));
                        dtMap.put("detItemFeeSumamt",itemVo.getAmount());
                        dtMap.put("cnt",itemVo.getQuan());
                        dtMap.put("pric",itemVo.getPrice());
                        dtMap.put("bilgDeptCodg",CommonUtils.getPropValueStr(paramMap,"codeDept"));
                        dtMap.put("bilgDeptName",CommonUtils.getPropValueStr(paramMap,"codeDeptName"));
                        dtMap.put("bilgDrCodg",CommonUtils.getPropValueStr(paramMap,"codeEmp"));
                        dtMap.put("bilgDrName",CommonUtils.getPropValueStr(paramMap,"codeEmpName"));
                        dtMap.put("hospApprFlag","0");
                        dtMap.put("nameCg",CommonUtils.getPropValueStr(upMap.get(0),"nameCg"));

                        upList.add(dtMap);
                    }
                });

                dts = upList;

            }else{//??????????????????
                paramMap.put("rePay", "1");
                dts = nationalInsuranceMapper.qryChargeDetailNoUpload(paramMap);
            }
        }

        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (dts != null && dts.size() > 0) {
            StringBuffer errorMsg = new StringBuffer("");
            dts.stream()
                    .filter(m -> CommonUtils.isEmptyString(CommonUtils.getPropValueStr(m, "medListCodg"))
                            || CommonUtils.isEmptyString(CommonUtils.getPropValueStr(m, "medinsListCodg")))
                    .forEach(map -> {
                        errorMsg.append(String.format("%s,\n",CommonUtils.getPropValueStr(map, "nameCg")));
                    });
            // ?????????????????????????????????
            if (errorMsg != null && !CommonUtils.isEmptyString(errorMsg.toString())) {
                insuResVo.setInfcode("-1");
                insuResVo.setErrMsg(String.format("HIS?????????????????????????????????????????????\n%s",errorMsg.toString()));

                throw new BusException(JsonUtil.writeValueAsString(insuResVo));
            }

            Map<String,Object> inputMap = new HashMap<>(16);
            inputMap.put("feedetail",dts);

            insuResVo = sendHttpPost("2204",inputMap,user);

            //?????????????????????flag_iusu??????
            //??????????????????????????????
            if(!paramMap.containsKey("flagReg")
                    || !EnumerateParameter.ONE.equals(CommonUtils.getPropValueStr(paramMap,"flagReg"))){
                UpDtInsuParam paramVo = new UpDtInsuParam();
                paramVo.setFlagInsu("1");
                paramVo.setFlagIp(false);
                paramVo.setHisCgList(dts);
                //???????????????????????????   result?????????????????????
                paramVo.setInsuCgList(JsonUtil.readValue(
                        JsonUtil.getJsonNode((String)insuResVo.getResVo().getOutput(), "result"),
                        new TypeReference<List<InsQgybCg>>() {
                        })
                );

                paramVo.setPkCgList(
                        JsonUtil.readValue(
                                JsonUtil.getJsonNode(params, "pkCgList"),
                                new TypeReference<List<String>>() {
                                })
                );
                paramVo.setPkPv(CommonUtils.getPropValueStr(paramMap,"pkPv"));

                prvUpDtFlagInsu(paramVo);
            }
        }else{
            insuResVo.setInfcode("0");
        }

        return insuResVo;
    }

    /**
     * ??????????????????
     * @param params pkPv
     * @param user
     * @return
     */
    public HisInsuResVo insuBillUploadCanl(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        List<Map<String,Object>> visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);
        if(visitListMap==null || visitListMap.size()<=0){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format("???????????????????????????????????????????????????!"));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }

        paramMap.clear();
        paramMap.put("port", "2205");
        paramMap.put("mdtrtId", visitListMap.get(0).get("mdtrtId"));
        paramMap.put("psnNo", visitListMap.get(0).get("psnNo"));
        paramMap.put("chrgBchno", "0000");


        Map<String,Object> inputMap = getInputMap("data",paramMap);
        insuResVo = sendHttpPost("2205",inputMap,user);

        //?????????????????????flag_iusu??????
        UpDtInsuParam paramVo = new UpDtInsuParam();
        paramVo.setFlagInsu("0");
        paramVo.setFlagIp(false);
        paramVo.setPkPv(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPv"));

        prvUpDtFlagInsu(paramVo);

        return insuResVo;
    }

    /**
     * ???????????????insuBillUpload
     * @param params    pkPv,medType:????????????,amountSt:?????????,chrgBchno:???????????????
     * @param user
     * @return
     */
    public HisInsuResVo mzHpHuaJia(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        //???????????????????????????
        List<Map<String,Object>> visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);
        if(visitListMap==null || visitListMap.size()<=0){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format("???????????????????????????????????????????????????!"));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }

        Map<String, Object> reqMap = new HashMap<>(16);
        reqMap.put("psnNo", visitListMap.get(0).get("psnNo"));
        reqMap.put("mdtrtCertType", visitListMap.get(0).get("mdtrtCertType"));
        reqMap.put("mdtrtCertNo", visitListMap.get(0).get("mdtrtCertNo"));
        reqMap.put("medType", paramMap.get("medType"));//??????????????????????????????????????????????????????????????????????????????
        reqMap.put("medfeeSumamt", paramMap.get("amountSt"));
        reqMap.put("psnSetlway", "01");
        reqMap.put("mdtrtId", visitListMap.get(0).get("mdtrtId"));
        reqMap.put("chrgBchno", paramMap.get("chrgBchno"));
        reqMap.put("acctUsedFlag", "1");
        reqMap.put("insutype", visitListMap.get(0).get("insutype"));

        Map<String,Object> inputMap = getInputMap("data",reqMap);
        insuResVo = sendHttpPost("2206",inputMap,user);

        return insuResVo;
    }

    /**
     * ????????????
     * @param params    pkPv,preStJsonStr:???????????????Json?????????,
     * @param user
     * @return
     */
    public HisInsuResVo mzHpJiaokuan(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        //???????????????????????????
        List<Map<String,Object>> visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);
        if(visitListMap==null || visitListMap.size()<=0){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format("???????????????????????????????????????????????????!"));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }

        //???????????????????????????
        
       /*
        InsQgybSt ybStVo = JsonUtil.readValue(
                JsonUtil.getJsonNode(CommonUtils.getPropValueStr(paramMap,"preStJsonStr"), "setlinfo"),
                InsQgybSt.class);
                */
        String preStJsonStr=JsonUtil.getJsonNode(CommonUtils.getPropValueStr(paramMap,"preStJsonStr"), "output").toString();
        InsQgybSt ybStVo = JsonUtil.readValue(
                JsonUtil.getJsonNode(preStJsonStr, "setlinfo"),
                InsQgybSt.class);
        Map<String, Object> reqMap = new HashMap<>(16);
        reqMap.put("psnNo", visitListMap.get(0).get("psnNo"));
        reqMap.put("mdtrtCertType", visitListMap.get(0).get("mdtrtCertType"));
        reqMap.put("mdtrtCertNo", visitListMap.get(0).get("mdtrtCertNo"));
        reqMap.put("medType", paramMap.get("medType"));//??????????????????????????????????????????????????????????????????????????????
        reqMap.put("medfeeSumamt", paramMap.get("amountSt"));
        reqMap.put("psnSetlway", "01");
        reqMap.put("mdtrtId", visitListMap.get(0).get("mdtrtId"));
        reqMap.put("chrgBchno", paramMap.get("chrgBchno"));
        reqMap.put("acctUsedFlag", "1");
        reqMap.put("insutype", visitListMap.get(0).get("insutype"));

        reqMap.put("invono", "");
        reqMap.put("fulamtOwnpayAmt", ybStVo.getFulamtOwnpayAmt());
        reqMap.put("overlmtSelfpay", ybStVo.getOverlmtSelfpay());
        reqMap.put("preselfpayAmt", ybStVo.getPreselfpayAmt());
        reqMap.put("inscpScpAmt", ybStVo.getInscpScpAmt());

        Map<String,Object> inputMap = getInputMap("data",reqMap);
        insuResVo = sendHttpPost("2207",inputMap,user);

        //?????????????????????his???????????????????????????????????????(??????)

        //?????????????????????????????????????????????
        InsQgybSt stInsertVo = JsonUtil.readValue(
                JsonUtil.getJsonNode(humpToLine((String)insuResVo.getResVo().getOutput()), "setlinfo"),
                InsQgybSt.class);
        stInsertVo.setPkPv(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPv"));
        stInsertVo.setPkPi(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPi"));
        stInsertVo.setPkHp(CommonUtils.getPropValueStr(visitListMap.get(0),"pkHp"));
        stInsertVo.setPkVisit(CommonUtils.getPropValueStr(visitListMap.get(0),"pkVisit"));
        stInsertVo.setDateSt(new Date());//???????????? setl_time  ?????????
        stInsertVo.setAmount(stInsertVo.getMedfeeSumamt());// ?????????
        stInsertVo.setAmtGrzhzf(stInsertVo.getAcctPay());// ????????????
        stInsertVo.setAmtGrzf(stInsertVo.getPsnCashPay());// ??????
        stInsertVo.setAmtJjzf(stInsertVo.getFundPaySumamt());// ??????
        stInsertVo.setPsnNo(CommonUtils.getPropValueStr(visitListMap.get(0),"psnNo"));
        stInsertVo.setAmtGrzh(stInsertVo.getBalc());
        //??????????????????????????????
        List<InsQgybPV> insuPvList = nationalInsuranceMapper.qryInsuPvNoStInfo(paramMap);
        if(insuPvList!=null && insuPvList.size()>0){
            stInsertVo.setPkInspv(insuPvList.get(0).getPkInspv());
        }
        
        ApplicationUtils.setDefaultValue(stInsertVo,true);

        if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPv"))){
            DataBaseHelper.execute("update INS_QGYB_ST set del_flag='1' where pk_pv=? and pk_settle is null", visitListMap.get(0).get("pkPv"));
        }

        DataBaseHelper.insertBean(stInsertVo);

        //???????????????????????????
        List<InsQgybStDt> ybstDtList = JsonUtil.readValue(
                JsonUtil.getJsonNode(humpToLine((String)insuResVo.getResVo().getOutput()), "setldetail"),
                new TypeReference<List<InsQgybStDt>>() {
                });

        if(ybstDtList!=null && ybstDtList.size()>0){
            ybstDtList.stream()
                    .forEach(dt->{
                                ApplicationUtils.setDefaultValue(dt,true);
                                dt.setPkInsst(stInsertVo.getPkInsst());
                            });

            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class),ybstDtList);
        }

        //??????5204????????????????????????
        Map<String,Object> cgParamMap = new HashMap<>(16);
        cgParamMap.put("pkInsst",stInsertVo.getPkInsst());
        cgParamMap.put("mdtrtId",stInsertVo.getMdtrtId());
        cgParamMap.put("psnNo",stInsertVo.getPsnNo());
        cgParamMap.put("setlId",stInsertVo.getSetlId());

        //????????????????????????????????????????????????
        try{
            searchStChargeDtls(JsonUtil.writeValueAsString(cgParamMap),user);
        }catch(Exception ex){
            //??????????????????
            reqMap = new HashMap<String, Object>(16);
            reqMap.put("mdtrtId", stInsertVo.getMdtrtId());
            reqMap.put("psnNo", stInsertVo.getPsnNo());
            reqMap.put("setlId", stInsertVo.getSetlId());
            inputMap = getInputMap("data",reqMap);
            insuResVo = sendHttpPost("2208",inputMap,user);

            //??????????????????
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format(ex.getMessage()));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }


        return insuResVo;
    }

    /**
     * ?????????????????? 5204
     * ?????????????????????ins_qgyb_st_cg???
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo searchStChargeDtls(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        Map<String, Object> reqMap = new HashMap<String, Object>(16);
        reqMap.put("mdtrtId", CommonUtils.getPropValueStr(paramMap,"mdtrtId"));
        reqMap.put("psnNo", CommonUtils.getPropValueStr(paramMap,"psnNo"));
        reqMap.put("setlId", CommonUtils.getPropValueStr(paramMap,"setlId"));
        Map<String, Object> inputMap = getInputMap("data",reqMap);
        HisInsuResVo insuResVo = sendHttpPost("5204",inputMap,user);

        //???????????????ins_qgyb_st_cg
        List<InsQgybStCg> stCgList = JsonUtil.readValue(
                JsonUtil.getJsonNode(insuResVo.getResJsonStr(), "output"),
                new TypeReference<List<InsQgybStCg>>() {
                });

        if(stCgList!=null && stCgList.size()>0){
            //??????????????????
            InsQgybSt stInfo = nationalInsuranceMapper.qryInsuStInfo(paramMap);

            if(stInfo!=null && !CommonUtils.isEmptyString(stInfo.getPkInsst())){
                stCgList.stream().forEach(stVo->{
                    ApplicationUtils.setDefaultValue(stVo,true);
                    stVo.setPkInsst(stInfo.getPkInsst());
                    stVo.setPkPi(stInfo.getPkPi());
                    stVo.setPkPv(stInfo.getPkPv());
                });

                //??????????????????????????????????????????????????????
                DataBaseHelper.execute("delete from ins_qgyb_st_cg where pk_insst = ?",new Object[]{paramMap.get("pkInsst")});

                //??????
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStCg.class),stCgList);
            }
        }

        return insuResVo;
    }

    /**
     * ?????????????????????His???????????? pkPv,pkSettle,setlId:??????ID
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo saveHisYbRelationship(String params, IUser user)    {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();
        
        String pkSettle= CommonUtils.getPropValueStr(paramMap,"pkSettle");
        String setlId= CommonUtils.getPropValueStr(paramMap,"pkInsst");
     
        paramMap.remove("pkSettle");
        paramMap.put("setlId",setlId);
        //??????pkPv???setlId????????????????????????
        InsQgybSt stVo = nationalInsuranceMapper.qryYbStInfo(paramMap);
        if(stVo==null || CommonUtils.isEmptyString(stVo.getPkInsst())){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg("?????????????????????HIS????????????????????????????????????????????????????????????");
        }else{
            stVo.setPkSettle(pkSettle);
            ApplicationUtils.setDefaultValue(stVo,false);

            DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybSt.class),stVo);
        }

        return insuResVo;
    }

    /**
     * ??????????????????
     * @param params    pkSettle
     * @param user
     * @return
     */
    public HisInsuResVo insSettleCancle(String params, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        //????????????????????????
        InsQgybSt stVo = nationalInsuranceMapper.qryYbStInfo(paramMap);
        if(stVo==null || CommonUtils.isEmptyString(stVo.getPkInsst())){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg("?????????????????????HIS????????????????????????????????????????????????????????????");
        }else{
            Map<String, Object> reqMap = new HashMap<String, Object>(16);
            reqMap.put("mdtrtId", stVo.getMdtrtId());
            reqMap.put("psnNo", stVo.getPsnNo());
            reqMap.put("setlId", stVo.getSetlId());
            Map<String, Object> inputMap = getInputMap("data",reqMap);
            insuResVo = sendHttpPost("2208",inputMap,user);

            //?????????????????????????????????????????????
            InsQgybSt stInsertVo = JsonUtil.readValue(
                    JsonUtil.getJsonNode(humpToLine((String)insuResVo.getResVo().getOutput()), "setlinfo"),
                    InsQgybSt.class);
            stInsertVo.setPkPv(stVo.getPkPv());
            stInsertVo.setPkPi(stVo.getPkPi());
            stInsertVo.setPkHp(stVo.getPkHp());
            stInsertVo.setPkVisit(stVo.getPkVisit());
            stInsertVo.setAmount(stInsertVo.getMedfeeSumamt());// ?????????
            stInsertVo.setAmtGrzhzf(stInsertVo.getAcctPay());// ????????????
            stInsertVo.setAmtGrzf(stInsertVo.getPsnCashPay());// ??????
            stInsertVo.setAmtJjzf(stInsertVo.getFundPaySumamt());// ??????
            stInsertVo.setPkInsstCancel(stVo.getPkInsst());
            stInsertVo.setSetlIdCancel(stVo.getSetlId());
            stInsertVo.setPsnNo(stVo.getPsnNo());

            ApplicationUtils.setDefaultValue(stInsertVo,true);
            DataBaseHelper.insertBean(stInsertVo);

            //???????????????????????????
            List<InsQgybStDt> ybstDtList = JsonUtil.readValue(
                    JsonUtil.getJsonNode((String)insuResVo.getResVo().getOutput(), "setldetail"),
                    new TypeReference<List<InsQgybStDt>>() {
                    });

            if(ybstDtList!=null && ybstDtList.size()>0){
                ybstDtList.stream()
                        .forEach(dt->{
                            ApplicationUtils.setDefaultValue(dt,true);
                            dt.setPkInsst(stInsertVo.getPkInsst());
                        });

                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class),ybstDtList);
            }
        }

        return  insuResVo;
    }

    /**
     * ??????????????????-???????????????????????????????????????bl_op_dt???bl_ip_dt???flag_insu??????
     * @param params
     * @param user
     */
    public void UpChargeFlagInsuByPk(String params, IUser user){
        UpDtInsuParam paramVo = JsonUtil.readValue(params, UpDtInsuParam.class);

        prvUpDtFlagInsu(paramVo);
    }

	/**
     *
     * ?????????????????????
	 * @param 
	 */
	public Map<String, Object> preReturnSettlement(String params, IUser user) {
	    HisInsuResVo insuResVo = new HisInsuResVo();
		Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

		// ????????????????????????
        InsQgybSt stVo = nationalInsuranceMapper.qryYbStInfo(paramMap);
        if(stVo==null || CommonUtils.isEmptyString(stVo.getPkInsst())){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg("?????????????????????HIS????????????????????????????????????????????????????????????");
        }else{
        	
        }

		Map<String, Object> stMap = new HashMap<String, Object>();

		// ??????????????????????????????????????????????????????0
		stMap.put("aggregateAmount", 0);
		stMap.put("patientsPay", 0);
		stMap.put("medicarePayments", 0);

		return stMap;
	}


    /**
     * ????????????015001015010
     * ????????????
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo zyHpReg(String params, IUser user){
        HisInsuResVo insuResVo = new HisInsuResVo();
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

        Map<String,Object> inputMap = new HashMap<>(16);
        inputMap.put("diseinfo",paramMap.get("diseinfo"));
        paramMap.remove("diseinfo");
        inputMap.put("mdtrtinfo",paramMap);

        insuResVo = sendHttpPost("2401",inputMap ,user);

        return insuResVo;
    }

    /**
     * ????????????015001015022
     * ??????????????????
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo hpAdmitCancel(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        Map<String, Object> inputMap = new HashMap<>(16);
        if(paramMap.containsKey("pkVisit") && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"pkVisit"))){
            InsQgybVisit visitInfo = DataBaseHelper.queryForBean(
                    "select * from ins_qgyb_visit where pk_visit = ?"
                    ,InsQgybVisit.class,new Object[]{paramMap.get("pkVisit")});
            if(visitInfo!=null && !CommonUtils.isEmptyString(visitInfo.getPkVisit())){
                inputMap.put("psnNo",visitInfo.getPsnNo());
                inputMap.put("mdtrtId",visitInfo.getMdtrtId());
            }
        }else if(paramMap.containsKey("pkPv") && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"pkPv"))){
            List<InsQgybVisit> visitList = nationalInsuranceMapper.qryVisitInfo(paramMap);
            if(visitList!=null && visitList.size()>0){
                inputMap.put("psnNo",visitList.get(0).getPsnNo());
                inputMap.put("mdtrtId",visitList.get(0).getMdtrtId());
            }
        }else{
            inputMap.put("psnNo",paramMap.get("psnNo"));
            inputMap.put("mdtrtId",paramMap.get("mdtrtId"));
        }

        insuResVo = sendHttpPost("2404", getInputMap("data",inputMap),user);

        return insuResVo;
    }
    /**
     * ????????????015001015018
     * ???????????? 2402
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo zyHpDis(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

        HisInsuResVo insuResVo = new HisInsuResVo();

        Map<String,Object> inputMap = new HashMap<>(16);
        inputMap.put("diseinfo",paramMap.get("diseinfo"));
        paramMap.remove("diseinfo");
        inputMap.put("dscginfo",paramMap);

        insuResVo = sendHttpPost("2402",inputMap ,user);

        return insuResVo;
    }
    /**
     * ????????????
     * ?????????????????? 2403
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo saveZyHpRegRelationship(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

        HisInsuResVo insuResVo = new HisInsuResVo();

        Map<String,Object> inputMap = new HashMap<>(16);
        inputMap.put("diseinfo",paramMap.get("diseinfo"));
        paramMap.remove("diseinfo");
        
        inputMap.put("adminfo",paramMap);

        insuResVo = sendHttpPost("2403",inputMap ,user);

        return insuResVo;
    }
    

    /**
     * ????????????015001015019
     * ??????????????????
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo zyHpDisCancel(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

        HisInsuResVo insuResVo = new HisInsuResVo();

        insuResVo = sendHttpPost("2405",getInputMap("data",paramMap) ,user);

        return insuResVo;
    }

    /**
     * ????????????015001015014
     * ??????????????????
     * @param params    pkPv,pkCgList ??????????????????
     * @param user
     * @return
     */
    public HisInsuResVo insuBillIpUpload(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        List<Map<String, Object>> dts = nationalInsuranceMapper.qryIpChargeDetailNoUpload(paramMap);

        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (dts != null && dts.size() > 0) {
            StringBuffer errorMsg = new StringBuffer("");
            dts.stream()
                    .filter(m -> CommonUtils.isEmptyString(CommonUtils.getPropValueStr(m, "medListCodg"))
                            || CommonUtils.isEmptyString(CommonUtils.getPropValueStr(m, "medinsListCodg")))
                    .forEach(map -> {
                        errorMsg.append(String.format("%s,\n",CommonUtils.getPropValueStr(map, "nameCg")));
                    });
            // ?????????????????????????????????
            if (errorMsg != null && !CommonUtils.isEmptyString(errorMsg.toString())) {
                insuResVo.setInfcode("-1");
                insuResVo.setErrMsg(String.format("HIS?????????????????????????????????????????????\n%s",errorMsg.toString()));

                throw new BusException(JsonUtil.writeValueAsString(insuResVo));
            }

            /*??????List??????99??????1???????????????*/
            Map<String, List<Map<String,Object>>> upmap = new HashMap<>(16);            //???map??????????????????????????????
            int listSize = dts.size();
            int toIndex = 99;//99
            int keyToken = 0;
            for (int i = 0; i < dts.size(); i += 99) {
                if (i + 99 > listSize) {        //?????????toIndex????????????99????????????????????????newList???????????????
                    toIndex = listSize - i;
                }
                List<Map<String,Object>> newList = dts.subList(i, i + toIndex);
                upmap.put("upNum" + keyToken, newList);
                keyToken++;
            }

            Map<String,Object> inputMap = new HashMap<>(16);

            //??????
            for (List<Map<String,Object>> uploadVo : upmap.values()) {
                //??????HTTP??????
                inputMap.clear();
                inputMap.put("feedetail",uploadVo);

                insuResVo = sendHttpPost("2301",inputMap,user);

                //?????????????????????flag_iusu??????
                UpDtInsuParam paramVo = new UpDtInsuParam();
                paramVo.setFlagInsu("1");
                paramVo.setFlagIp(true);
                paramVo.setHisCgList(uploadVo);
                //???????????????????????????   result?????????????????????
                paramVo.setInsuCgList(JsonUtil.readValue(
                        JsonUtil.getJsonNode((String)insuResVo.getResVo().getOutput(), "result"),
                        new TypeReference<List<InsQgybCg>>() {
                        })
                );

                paramVo.setPkCgList(
                        uploadVo.stream().map(vo-> CommonUtils.getString(vo.get("pkCg"))).collect(Collectors.toList())
                );
                paramVo.setPkPv(CommonUtils.getPropValueStr(paramMap,"pkPv"));

                prvUpDtFlagInsu(paramVo);
            }
        }else{
            insuResVo.setInfcode("0");
        }

        return insuResVo;
    }

    /**
     * ????????????015001015015
     * ???????????????????????????????????? pkPv
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo zyBillDel(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        List<Map<String,Object>> visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);
        if(visitListMap==null || visitListMap.size()<=0){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format("???????????????????????????????????????????????????!"));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }

        paramMap.clear();
        paramMap.put("mdtrtId", visitListMap.get(0).get("mdtrtId"));
        paramMap.put("psnNo", visitListMap.get(0).get("psnNo"));
        paramMap.put("feedetlSn", "0000");

        Map<String,Object> inputMap = getInputMap("data",paramMap);
        insuResVo = sendHttpPost("2302",inputMap,user);

        //?????????????????????flag_iusu??????
        UpDtInsuParam paramVo = new UpDtInsuParam();
        paramVo.setFlagInsu("0");
        paramVo.setFlagIp(true);
        paramVo.setPkPv(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPv"));

        prvUpDtFlagInsu(paramVo);

        return insuResVo;
    }

    /**
     * ????????????015001015017
     * ???????????????
     * @param params    pkPv,medType:????????????,amountSt:?????????
     * @param user
     * @return
     */
    public HisInsuResVo preZyHpSettle(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        //???????????????????????????
        List<Map<String,Object>> visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);
        if(visitListMap==null || visitListMap.size()<=0){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format("???????????????????????????????????????????????????!"));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }

        Map<String, Object> reqMap = new HashMap<>(16);
        reqMap.put("psnNo", visitListMap.get(0).get("psnNo"));
        reqMap.put("mdtrtCertType", visitListMap.get(0).get("mdtrtCertType"));
        reqMap.put("mdtrtCertNo", visitListMap.get(0).get("mdtrtCertNo"));
        reqMap.put("medfeeSumamt", nationalInsuranceMapper.qryIpChargeTotalAmt(paramMap));
        reqMap.put("psnSetlway", "01");
        reqMap.put("mdtrtId", visitListMap.get(0).get("mdtrtId"));
        reqMap.put("acctUsedFlag", "1");
        reqMap.put("insutype", visitListMap.get(0).get("insutype"));
        reqMap.put("midSetlFlag", "0");

        Map<String,Object> inputMap = getInputMap("data",reqMap);
        insuResVo = sendHttpPost("2303",inputMap,user);

        return insuResVo;
    }

    /**
     * ????????????015001015016
     * ????????????????????????
     * @param params    pkPv,preStJsonStr:???????????????Json?????????,
     * @param user
     * @return
     */
    public HisInsuResVo zyHpSettle(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        //???????????????????????????
        List<Map<String,Object>> visitListMap = nationalInsuranceMapper.qryVisitDtls(paramMap);
        if(visitListMap==null || visitListMap.size()<=0){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format("???????????????????????????????????????????????????!"));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }

        //???????????????????????????
        String preStJsonStr=JsonUtil.getJsonNode(CommonUtils.getPropValueStr(paramMap,"preStJsonStr"), "output").toString();
        InsQgybSt ybStVo = JsonUtil.readValue(
                JsonUtil.getJsonNode(humpToLine(preStJsonStr), "setlinfo"),
                InsQgybSt.class);
        Map<String, Object> reqMap = new HashMap<>(16);
        reqMap.put("psnNo", visitListMap.get(0).get("psnNo"));
        reqMap.put("mdtrtCertType", visitListMap.get(0).get("mdtrtCertType"));
        reqMap.put("mdtrtCertNo", visitListMap.get(0).get("mdtrtCertNo"));
        reqMap.put("medfeeSumamt", ybStVo.getMedfeeSumamt());
        reqMap.put("psnSetlway", "01");
        reqMap.put("mdtrtId", visitListMap.get(0).get("mdtrtId"));
        reqMap.put("acctUsedFlag", "1");
        reqMap.put("insutype", visitListMap.get(0).get("insutype"));
        reqMap.put("midSetlFlag","0");
        reqMap.put("invono", "");
        reqMap.put("fulamtOwnpayAmt", ybStVo.getFulamtOwnpayAmt());
        reqMap.put("overlmtSelfpay", ybStVo.getOverlmtSelfpay());
        reqMap.put("preselfpayAmt", ybStVo.getPreselfpayAmt());
        reqMap.put("inscpScpAmt", ybStVo.getInscpScpAmt());

        Map<String,Object> inputMap = getInputMap("data",reqMap);
        insuResVo = sendHttpPost("2304",inputMap,user);

        //?????????????????????????????????????????????
        InsQgybSt stInsertVo = JsonUtil.readValue(
                JsonUtil.getJsonNode(humpToLine((String)insuResVo.getResVo().getOutput()), "setlinfo"),
                InsQgybSt.class);
        stInsertVo.setPkPv(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPv"));
        stInsertVo.setPkPi(CommonUtils.getPropValueStr(visitListMap.get(0),"pkPi"));
        stInsertVo.setPkHp(CommonUtils.getPropValueStr(visitListMap.get(0),"pkHp"));
        stInsertVo.setPkVisit(CommonUtils.getPropValueStr(visitListMap.get(0),"pkVisit"));
        stInsertVo.setDateSt(new Date());//???????????? setl_time  ?????????

        stInsertVo.setAmount(stInsertVo.getMedfeeSumamt());// ?????????
        stInsertVo.setAmtGrzhzf(stInsertVo.getAcctPay());// ????????????
        stInsertVo.setAmtGrzf(stInsertVo.getPsnCashPay());// ??????
        stInsertVo.setAmtJjzf(stInsertVo.getFundPaySumamt());// ??????
        stInsertVo.setPsnNo(CommonUtils.getPropValueStr(visitListMap.get(0),"psnNo"));
        stInsertVo.setAmtGrzh(stInsertVo.getBalc());

        ApplicationUtils.setDefaultValue(stInsertVo,true);

        DataBaseHelper.execute("update INS_QGYB_ST set del_flag='1' where pk_pv=? and pk_settle is null", visitListMap.get(0).get("pkPv"));
        DataBaseHelper.insertBean(stInsertVo);

        //???????????????????????????
        List<InsQgybStDt> ybstDtList = JsonUtil.readValue(
                JsonUtil.getJsonNode(humpToLine((String)insuResVo.getResVo().getOutput()), "setldetail"),
                new TypeReference<List<InsQgybStDt>>() {
                });

        if(ybstDtList!=null && ybstDtList.size()>0){
            ybstDtList.stream()
                    .forEach(dt->{
                        ApplicationUtils.setDefaultValue(dt,true);
                        dt.setPkInsst(stInsertVo.getPkInsst());
                    });

            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class),ybstDtList);
        }

        //??????5204????????????????????????
        Map<String,Object> cgParamMap = new HashMap<>(16);
        cgParamMap.put("pkInsst",stInsertVo.getPkInsst());
        cgParamMap.put("mdtrtId",stInsertVo.getMdtrtId());
        cgParamMap.put("psnNo",stInsertVo.getPsnNo());
        cgParamMap.put("setlId",stInsertVo.getSetlId());

        //????????????????????????????????????????????????
        try{
            searchStChargeDtls(JsonUtil.writeValueAsString(cgParamMap),user);
        }catch(Exception ex){
            //??????????????????
            reqMap = new HashMap<String, Object>(16);
            reqMap.put("mdtrtId", stInsertVo.getMdtrtId());
            reqMap.put("psnNo", stInsertVo.getPsnNo());
            reqMap.put("setlId", stInsertVo.getSetlId());
            inputMap = getInputMap("data",reqMap);
            insuResVo = sendHttpPost("2305",inputMap,user);

            //??????????????????
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(String.format(ex.getMessage()));

            throw new BusException(JsonUtil.writeValueAsString(insuResVo));
        }

        return insuResVo;
    }

    /**
     * ????????????015001015021
     * ????????????????????????    ???????????????????????????????????????????????????  2305
     * @param params    pkSettle
     * @param user
     * @return
     */
    public HisInsuResVo insIpSettleCancle(String params, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo insuResVo = new HisInsuResVo();

        //????????????????????????
        InsQgybSt stVo = nationalInsuranceMapper.qryYbStInfo(paramMap);
        if(stVo==null || CommonUtils.isEmptyString(stVo.getPkInsst())){
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg("?????????????????????HIS????????????????????????????????????????????????????????????");
        }else{
            Map<String, Object> reqMap = new HashMap<String, Object>(16);
            reqMap.put("mdtrtId", stVo.getMdtrtId());
            reqMap.put("psnNo", stVo.getPsnNo());
            reqMap.put("setlId", stVo.getSetlId());
            Map<String, Object> inputMap = getInputMap("data",reqMap);
            insuResVo = sendHttpPost("2305",inputMap,user);

            //?????????????????????????????????????????????
            InsQgybSt stInsertVo = JsonUtil.readValue(
                    JsonUtil.getJsonNode(humpToLine((String)insuResVo.getResVo().getOutput()), "setlinfo"),
                    InsQgybSt.class);
            stInsertVo.setPkPv(stVo.getPkPv());
            stInsertVo.setPkPi(stVo.getPkPi());
            stInsertVo.setPkHp(stVo.getPkHp());
            stInsertVo.setPkVisit(stVo.getPkVisit());
            stInsertVo.setAmount(stInsertVo.getMedfeeSumamt());// ?????????
            stInsertVo.setAmtGrzhzf(stInsertVo.getAcctPay());// ????????????
            stInsertVo.setAmtGrzf(stInsertVo.getPsnCashPay());// ??????
            stInsertVo.setAmtJjzf(stInsertVo.getFundPaySumamt());// ??????
            stInsertVo.setPkInsstCancel(stVo.getPkInsst());
            stInsertVo.setSetlIdCancel(stVo.getSetlId());
            stInsertVo.setPsnNo(stVo.getPsnNo());
            stInsertVo.setPsnType(stVo.getPsnType());
            stInsertVo.setInsutype(stVo.getInsutype());
            stInsertVo.setMdtrtCertType(stVo.getMdtrtCertType());
            stInsertVo.setDateSt(new Date());

            ApplicationUtils.setDefaultValue(stInsertVo,true);
            DataBaseHelper.insertBean(stInsertVo);

            //???????????????????????????
            List<InsQgybStDt> ybstDtList = JsonUtil.readValue(
                    JsonUtil.getJsonNode((String)insuResVo.getResVo().getOutput(), "setldetail"),
                    new TypeReference<List<InsQgybStDt>>() {
                    });

            if(ybstDtList!=null && ybstDtList.size()>0){
                ybstDtList.stream()
                        .forEach(dt->{
                            ApplicationUtils.setDefaultValue(dt,true);
                            dt.setPkInsst(stInsertVo.getPkInsst());
                        });

                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybStDt.class),ybstDtList);
            }
        }

        return  insuResVo;
    }
    /**
     * ??????????????????flag_insu??????
     * pkPv,pkCgList ??????????????????,flagIp ????????????(true:???????????????false:????????????)
     */
    private void prvUpDtFlagInsu(UpDtInsuParam paramVo){
        if(paramVo!=null){
            Map<String,Object> paramMap = (Map<String,Object>)ApplicationUtils.beanToMap(paramVo);

            if(paramVo.isFlagIp()){
                if(EnumerateParameter.ZERO.equals(paramVo.getFlagInsu())){
                    DataBaseHelper.execute(
                            "delete from ins_qgyb_cg city where exists (select 1 from bl_ip_dt dt where dt.pk_pv = ? and dt.flag_insu='1' and (dt.flag_settle = '0' OR dt.PK_SETTLE IS NULL) and dt.pk_cgip = city.pk_cgop)"
                            , paramVo.getPkPv()
                    );
                }

                nationalInsuranceMapper.updateIpdtFlagInsuByPk(paramMap);

                //???????????????INS_SZYB_CITYCG
                if(paramVo.getInsuCgList()!=null && paramVo.getInsuCgList().size()>0){
                    paramVo.getInsuCgList().forEach(m-> {
                        ApplicationUtils.setDefaultValue(m,true);
                        m.setPkPv(paramVo.getPkPv());
                        //m.setPkCgop(paramVo.getHisCgList().stream().filter(map-> CommonUtils.getPropValueStr(map,"feedetlSn").equals(m.getFeedetlSn())).collect(Collectors.toList()).get(0).get("pkCg").toString());
                    });

                    String sqlStr = "delete from ins_qgyb_cg where pk_cgop in ("
                            + CommonUtils.convertSetToSqlInPart(new HashSet<>(paramVo.getPkCgList()), "pk_cgop")+")";
                    DataBaseHelper.batchUpdate(sqlStr,paramVo.getPkCgList());

                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybCg.class), paramVo.getInsuCgList());
                }
            }else{
                if(EnumerateParameter.ZERO.equals(paramVo.getFlagInsu())){
                    DataBaseHelper.execute(
                            "delete from ins_qgyb_cg city where exists (select 1 from bl_op_dt dt where dt.pk_pv = ? and dt.flag_insu='1' and (dt.flag_settle = '0' OR dt.PK_SETTLE IS NULL) and dt.pk_cgop = city.pk_cgop)"
                            , paramVo.getPkPv()
                    );
                }

                nationalInsuranceMapper.updateOpdtFlagInsuByPk(paramMap);

                //???????????????INS_SZYB_CITYCG
                if(paramVo.getInsuCgList()!=null && paramVo.getInsuCgList().size()>0){
                    paramVo.getInsuCgList().forEach(m-> {
                        ApplicationUtils.setDefaultValue(m,true);
                        m.setPkPv(paramVo.getPkPv());
                        //m.setPkCgop(paramVo.getHisCgList().stream().filter(map-> CommonUtils.getPropValueStr(map,"feedetlSn").equals(m.getFeedetlSn())).collect(Collectors.toList()).get(0).get("pkCg").toString());
                    });

                    String sqlStr = "delete from ins_qgyb_cg where pk_cgop in ("
                            + CommonUtils.convertSetToSqlInPart(new HashSet<>(paramVo.getPkCgList()), "pk_cgop")+")";
                    DataBaseHelper.batchUpdate(sqlStr,paramVo.getPkCgList());

                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybCg.class), paramVo.getInsuCgList());
                }
            }
        }
    }

    private Map<String,Object> getInputMap(String key,Map<String,Object> inputParam){
        Map<String,Object> paramMap = new HashMap<>(16);
        paramMap.put(key,inputParam);

        return paramMap;
    }
    
    /**
     *
     * @param port  ?????????
     * @param inputParam    input??????  key:????????????   value:???
     * @param user  ????????????
     */
    @SuppressWarnings("unchecked")
    public HisInsuResVo sendHttpPost(String port,Map<String, Object> inputParam,IUser user) {
        User userInfo = (User) user;

        //??????http????????????
        Map<String, String> map = this.getHeaderElement();

        NationalHeadReqVo reqVo = new NationalHeadReqVo();
        reqVo.setInfno(port);
        reqVo.setMsgid(String.format("%s%s%s", fixmedinsCode, DateUtils.dateToStr("yyyyMMddHHmmss", new Date()),getSNcode()));
        reqVo.setMdtrtareaAdmvs(MdtrtareaAdmvs);//???????????????????????????
        reqVo.setInsuplcAdmdvs(MdtrtareaAdmvs);//???????????????????????????
        reqVo.setRecerSysCode("01");//???????????????????????????
        reqVo.setSigntype("SM3");
        reqVo.setInfver(VERSION);
        reqVo.setOpterType("1");
        reqVo.setOpterName(userInfo.getNameEmp());
        reqVo.setOpter(userInfo.getCodeEmp());
        reqVo.setInfTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
        reqVo.setFixmedinsCode(fixmedinsCode);
        reqVo.setFixmedinsName(FIXMEDINS_NAME);
        reqVo.setCainfo("");//????????????(9001??????)
        reqVo.setSignNo("");
        //??????inputParam
        Map<String,Object> input = new HashMap<>(16);
        reqVo.setInput(inputParam);

        //????????????????????????
        String jsonStr = JsonUtil.writeValueAsString(reqVo);
        JSONObject objTem = JSON.parseObject(jsonStr);
        objTem = transLine(objTem);//??????????????????json
        jsonStr = JsonUtil.writeValueAsString(objTem);

        HisInsuResVo resVo = new HisInsuResVo();

        StringBuffer msgStr = new StringBuffer();
        try {
            msgStr.append(String.format("\n%s",DateUtils.getDateTime()));
            msgStr.append(String.format("\n??????????????????:%s",port));
            msgStr.append(String.format("\n????????????requestJson:%s",jsonStr));

            String resJson = HttpClientUtil.sendHttpPost(String.format("%s%s",HSA_URL,port), jsonStr, map);

            resJson = lineToHump(resJson);

            msgStr.append(String.format("\n%s",DateUtils.getDateTime()));
            msgStr.append(String.format("\n????????????responseJson:%s",resJson));

            NationalResVo natResVo = JsonUtil.readValue(resJson, NationalResVo.class);
            //output??????String??????
            JSONObject jsonObject = JSONObject.parseObject(resJson);
            // ?????????key???shoppingCartItemList??????
            String outputStr = jsonObject.getString("output");
            natResVo.setOutput(outputStr);
            resVo.setResVo(natResVo);
            resVo.setInfcode(natResVo.getInfcode());
            resVo.setReqJsonStr(jsonStr);
            resVo.setResJsonStr(resJson);
            resVo.setInsuLog(msgStr.toString());

            if(!EnumerateParameter.ZERO.equals(resVo.getInfcode())){
                resVo.setInfcode("-1");
                resVo.setErrMsg(String.format("??????port[%s],??????????????????:%s",port,natResVo.getErrMsg()));

                throw new BusException(JsonUtil.writeValueAsString(resVo));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resVo.setInfcode("-1");
            resVo.setErrMsg(String.format("HIS????????????:?????????????????????????????????port[%s],%s",port,e.getMessage()));

            resVo.setInsuLog(msgStr.toString());
            throw new BusException(JsonUtil.writeValueAsString(resVo));
        }

        return resVo;
    }

    public HisInsuResVo sendHttpPost(String port,Map<String, Object> inputParam){
        return sendHttpPost(port, inputParam,getUser());
    }

    private User getUser(){
        return Optional.ofNullable(UserContext.getUser()).orElseThrow(()-> new BusException("???????????????????????????????????????"));
    }
    /* ??????0607?????????????????????????????? */
    private String getSNcode() {
        String SNcode = ApplicationUtils.getCode("0607");
        if (SNcode != null && SNcode.length() >= 4) {
            SNcode = SNcode.substring(SNcode.length() - 4, SNcode.length());
            return SNcode;
        } else {
            throw new BusException("?????????????????????0607???????????????????????????????????????SNcode");
        }
    }

    private Map<String, String> getHeaderElement() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("x-tif-paasid", HSA_PAASID);
        map.put("x-tif-nonce", RandomStringUtils.random(6, true, true));
        map.put("x-tif-timestamp", Long.toString(ZsrmQGUtils.getCurrentUnixSeconds()));
        map.put("x-tif-signature", ZsrmQGUtils.getSHA256Str(
                map.get("x-tif-timestamp") + SECRETKEY + map.get("x-tif-nonce") + map.get("x-tif-timestamp")));
        return map;
    }

    /*
     * ??????Json??????????????????json
     * */
    private JSONObject transLine(JSONObject objTem) {
        JSONObject jsonObject = new JSONObject();
        Set<String> keySet = objTem.keySet();
        for (String key : keySet) {
            Object objR = objTem.get(key);
            if(objR==null)
                continue;

            if (objR instanceof JSONObject) {
                JSONObject deal = transLine((JSONObject) objR);
                jsonObject.put(humpToLine(key), deal);
                continue;
            }else if (objR instanceof JSONArray) {
                JSONArray jsonA = new JSONArray();
                JSONArray jsonArray = objTem.getJSONArray(key);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonDeal = transLine(jsonArray.getJSONObject(i));
                    jsonA.add(jsonDeal);
                }
                jsonObject.put(humpToLine(key), jsonA);
            }else{
                jsonObject.put(humpToLine(key), objR);
                continue;
            }

        }
        return jsonObject;
    }

    /**
     * ???????????????????????????????????????
     * @param str
     * @return
     */
    private boolean checkStrUpperCase(String str){
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            if(Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ??????????????????
     * @param str
     * @return
     */
    private String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * ?????????????????? 
     * @param str
     * @return
     */
	public static String humpToLine(String str) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}


    /**
     * ????????????015001015023
     * ???????????????????????????3301???
     * @param params
     * @param user
     * @return
     */
	public HisInsuResVo uploadMatchCatalog(String params, IUser user){
        HisItemInfo hisItemInfo = JsonUtil.readValue(params, HisItemInfo.class);
        HisInsuResVo hisInsuVo = new HisInsuResVo();
        if(hisItemInfo!=null){
            //??????????????????????????????
            List<Map<String,Object>> uoloadList = JsonUtil.readValue(
                    JsonUtil.getJsonNode(params, "params"),
                    new TypeReference<List<Map<String,Object>>>() {
                    });

            uoloadList.stream()
                    .forEach(uploadMap->{
                        //????????????????????????
                        Map<String, Object> inputMap = getInputMap("data",uploadMap);
                        HisInsuResVo resVo = new HisInsuResVo();
                        try{
                             resVo = sendHttpPost("3301",inputMap,user);
                        }catch(Exception ex){
                             resVo = JsonUtil.readValue(ex.getMessage(), HisInsuResVo.class);
                        }

                        //????????????????????????
                        if(EnumerateParameter.ZERO.equals(resVo.getInfcode())){
                            //???????????????????????????
                            DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '1', UPLOAD_DATE = ? WHERE PK_ITEMMAP = ?", new Object[]{new Date(),uploadMap.get("pkItemmap")});
                            //??????????????????????????????
                            hisInsuVo.setInsuLog(String.format("%s\n%s",hisInsuVo.getInsuLog(),resVo.getInsuLog()));
                        }else{
                            hisInsuVo.setInsuLog(String.format("%s\n%s",hisInsuVo.getInsuLog(),resVo.getInsuLog()));
                            hisInsuVo.setErrMsg(String.format("%s\n%s",hisInsuVo.getErrMsg(),resVo.getErrMsg()));
                        }
                    });
        }
        hisInsuVo.setInfcode("0");

        return hisInsuVo;
    }

    /**
     * ????????????015001015024
     * ????????????????????????(3302)
     * @param param
     * @param user
     * @return
     */
    public HisInsuResVo cancelMatchCatalog(String param, IUser user){
        HisItemInfo hisItemInfo = JsonUtil.readValue(param, HisItemInfo.class);
        HisInsuResVo hisInsuVo = new HisInsuResVo();

        if(hisItemInfo!=null){
            //??????????????????????????????
            List<Map<String,Object>> canlList = JsonUtil.readValue(
                    JsonUtil.getJsonNode(param, "params"),
                    new TypeReference<List<Map<String,Object>>>() {
                    });

            canlList.stream()
                    .forEach(canlMap->{
                        //????????????????????????????????????
                        canlMap.put("fixmedinsCode",fixmedinsCode);

                        Map<String, Object> inputMap = getInputMap("data",canlMap);
                        HisInsuResVo resVo = new HisInsuResVo();
                        try{
                            resVo = sendHttpPost("3302",inputMap,user);
                        }catch(Exception ex){
                            resVo = JsonUtil.readValue(ex.getMessage(), HisInsuResVo.class);
                        }

                        //????????????????????????
                        if(EnumerateParameter.ZERO.equals(resVo.getInfcode())){
                            //???????????????????????????
                            DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '3' WHERE PK_ITEMMAP = ?", new Object[]{canlMap.get("pkItemmap")});
                            //??????????????????????????????
                            hisInsuVo.setInsuLog(String.format("%s\n%s",hisInsuVo.getInsuLog(),resVo.getInsuLog()));
                        }else{
                            hisInsuVo.setInsuLog(String.format("%s\n%s",hisInsuVo.getInsuLog(),resVo.getInsuLog()));
                            hisInsuVo.setErrMsg(String.format("%s\n%s",hisInsuVo.getErrMsg(),resVo.getErrMsg()));
                        }
                    });
        }
        hisInsuVo.setInfcode("0");

        return hisInsuVo;
    }


	//???????????????????????????

    /**
     * 020001001021
     * ??????????????????
     * ?????????????????????????????????????????????????????????
     * 1???????????????????????????????????????????????????????????????
     * 2???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? = ?????????????????? + ????????????????????? + ????????????????????????????????????
     * 3????????????????????? = ??????????????????????????????????????????????????????????????????????????????????????????+ ?????????????????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????+ ???????????????????????? + ????????????????????????????????????????????????????????????
     * 4??????????????????????????????????????????????????????
     * @param psnNo ????????????
     * @param setlId ??????ID
     * @param mdtrtId ??????ID
     * @return
     */
    public HisInsuResVo expenseDetailsQuery(String psnNo,String setlId,String mdtrtId,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("setlId",setlId);
        paramMap.put("mdtrtId",mdtrtId);
        HisInsuResVo insuResVo = sendHttpPost("5204",getInputMap("data",paramMap), user);
        String result = (String) insuResVo.getResVo().getOutput();
        return insuResVo;
    }


    /**
     * ??????????????????
     * 1?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 2???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? = ?????????????????? + ????????????????????? + ????????????????????????????????????
     * 3????????????????????? = ??????????????????????????????????????????????????????????????????????????????????????????+ ?????????????????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????+ ???????????????????????? + ????????????????????????????????????????????????????????????
     * 4??????????????????????????????????????????????????????
     * @param psnNo ????????????
     * @param setlId ??????ID
     * @param mdtrtId ??????ID
     * @return
     */
    public HisInsuResVo settlementInformationQuery(String psnNo,String setlId,String mdtrtId,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("setlId",setlId);
        paramMap.put("mdtrtId",mdtrtId);
        HisInsuResVo insuResVo = sendHttpPost("5203",getInputMap("data",paramMap), user);
        String result = (String) insuResVo.getResVo().getOutput();
        return insuResVo;
    }


    /**
     * ??????????????????
     * ???????????????????????????????????????????????????????????????????????????
     * ?????????????????????????????????????????????????????????
     * @param psnNo
     * @param begntime
     * @param endtime
     * @param medType
     * @param mdtrtId
     * @return
     */
    public HisInsuResVo medicalInformationQuery(String psnNo,Date begntime,Date endtime,String medType,String mdtrtId,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("begntime",DateFormatUtils.format(begntime,"yyyy-MM-dd HH:mm:ss"));
        paramMap.put("endtime",DateFormatUtils.format(endtime,"yyyy-MM-dd HH:mm:ss" ));
        paramMap.put("medType",medType);
        paramMap.put("mdtrtId",mdtrtId);
        HisInsuResVo insuResVo = sendHttpPost("5201",getInputMap("data",paramMap), user);
        String result = (String) insuResVo.getResVo().getOutput();
        return insuResVo;
    }

    /**
     * ??????????????????
     * @param trtDclaDetlSn ???????????????????????????
     * @param psnNo ????????????
     * @param memo ??????
     */
    public HisInsuResVo transferToHospitalForRecordCancellation(String trtDclaDetlSn,String psnNo,String memo,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("trtDclaDetlSn",trtDclaDetlSn);
        paramMap.put("memo",memo);
        HisInsuResVo insuResVo = sendHttpPost("2502",getInputMap("data",paramMap), user);
        String result = (String) insuResVo.getResVo().getOutput();
        return insuResVo;
    }

    /**
     * ????????????
     * @param param
     * @param user
     * @return
     */
    public HisInsuResVo transferToHospitalForRecord(TransferToHospitalForRecordParam param, IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",param.getPsnNo());
        paramMap.put("insutype",param.getInsutype());
        paramMap.put("mdtrtId",param.getMdtrtId());
        paramMap.put("tel",param.getTel());
        paramMap.put("addr",param.getAddr());
        paramMap.put("insuOptins",param.getInsuOptins());
        paramMap.put("diagCode",param.getDiagCode());
        paramMap.put("diagName",param.getDiagName());
        paramMap.put("diseCondDscr",param.getDiseCondDscr());
        paramMap.put("reflinMedinsNo",param.getReflinMedinsNo());
        paramMap.put("reflinMedinsName",param.getReflinMedinsName());
        paramMap.put("mdtrtareaAdmdvs",param.getMdtrtareaAdmdvs());
        paramMap.put("hospAgreReflFlag",param.getHospAgreReflFlag());
        paramMap.put("reflType",param.getReflType());

        paramMap.put("reflRea",param.getReflRea());
        paramMap.put("reflOpnn",param.getReflOpnn());
        try{
            paramMap.put("reflDate",DateFormatUtils.format(param.getReflDate(),"yyyy-MM-dd"));
            paramMap.put("begndate", DateFormatUtils.format(param.getBegndate(),"yyyy-MM-dd"));
            paramMap.put("enddate",DateFormatUtils.format(param.getEnddate(),"yyyy-MM-dd" ));
        }catch (Exception e){

        }
        paramMap.put("reflUsedFlag",param.getReflUsedFlag());
        HisInsuResVo insuResVo = sendHttpPost("2501",getInputMap("refmedin",paramMap), user);
        String result = (String) insuResVo.getResVo().getOutput();
        return insuResVo;
    }

    /**
     * ????????????????????????
     * @param ver ???????????????
     */
    public HisInsuResVo tcmSyndromeCatalogDownload(String ver,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("ver",ver);
        HisInsuResVo insuResVo = sendHttpPost("1315",getInputMap("data",paramMap), user);
        return insuResVo;
    }

    /**
     * ????????????
     * @param code ????????????
     * @param ver ???????????????
     * @param user
     * @return
     */
    public HisInsuResVo catalogDownload(String code,String ver,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("ver",ver);
        HisInsuResVo insuResVo = sendHttpPost(code,getInputMap("data",paramMap), user);
        return insuResVo;
    }

    public HisInsuResVo catalogDownload(String updateTime,int pageNum,int pageSize,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("updtTime",updateTime);
        paramMap.put("pageNum",pageNum);
        paramMap.put("pageSize",pageSize);
        paramMap.put("valiFlag","0");
        HisInsuResVo insuResVo = sendHttpPost("1304",getInputMap("data",paramMap), user);
        return insuResVo;
    }


    //?????????????????????????????????

    /**
     *6.5.3.2????????????????????????
     * @param psnNo ????????????
     * @param bizAppyType ?????????????????? 01	?????????????????????	08	?????????????????? 03	??????????????????????????????
     * @param user
     * @return
     */
    public HisInsuResVo qryRecordDd(String psnNo,String bizAppyType,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("bizAppyType",bizAppyType);
        HisInsuResVo insuResVo = sendHttpPost("5302",getInputMap("data",paramMap), user);
        return insuResVo;
    }

    /**
     * 6.5.2.6????????????????????????
     * @param psnNo ????????????
     * @param cumYm ????????????
     * @param user
     * @return
     */
    public HisInsuResVo qryPersonnelAcc(String psnNo,String cumYm,IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("cumYm",cumYm);
        HisInsuResVo insuResVo = sendHttpPost("5206",getInputMap("data",paramMap), user);
        return insuResVo;
    }

    /**
     * ??????????????????
     * @param psnNo ????????????
     * @param begntime ????????????
     * @param endtime ????????????
     * @param user
     * @return
     */
    public HisInsuResVo qryInHospitalInfo(String psnNo,String begntime,String endtime, IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("begntime",begntime);
        paramMap.put("endtime",endtime);
        HisInsuResVo insuResVo = sendHttpPost("5303",getInputMap("data",paramMap), user);
        return insuResVo;
    }

    /**
     * ??????????????????
     * @param psnNo ????????????
     * @param begntime ????????????
     * @param endtime ????????????
     * @param user
     * @return
     */
    public HisInsuResVo qryTurnHospitalInfo(String psnNo, Date begntime, Date endtime, IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("psnNo",psnNo);
        paramMap.put("begntime",begntime);
        paramMap.put("endtime",endtime);
        HisInsuResVo insuResVo = sendHttpPost("5304",getInputMap("data",paramMap), user);
        return insuResVo;
    }

    /**
     *?????????????????????????????????
     * @param user
     * @return
     */
    public HisInsuResVo qryOrgInfo(IUser user){
        Map<String, Object> paramMap  = new HashMap<>(16);
        paramMap.put("fixmedinsName",FIXMEDINS_NAME);
        paramMap.put("fixmedinsCode",fixmedinsCode);
        paramMap.put("fixmedinsType","1");//1	??????????????????	2	??????????????????
        HisInsuResVo insuResVo = sendHttpPost("1201",getInputMap("medinsinfo",paramMap), user);
        return insuResVo;
    }

    /**
     * ???????????????
     *020001001038
     * @param user
     * @return
     */
    public HisInsuResVo downloadDictionary(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String type = MapUtils.getString(paramMap,"type");
        String name = MapUtils.getString(paramMap,"name");

        Map<String, Object> param  = new HashMap<>(16);
        String parentValue = MapUtils.getString(paramMap,"parentValue");
        String admdvs = MapUtils.getString(paramMap,"admdvs");
        String date = MapUtils.getString(paramMap,"date");
        String valiFlag = MapUtils.getString(paramMap,"valiFlag");
        param.put("type",type);
        param.put("parentValue",parentValue);
        param.put("admdvs",admdvs);
        param.put("date",date);
        param.put("valiFlag",valiFlag);
        Map<String, Object> inputMap = getInputMap("data",param);


        HisInsuResVo result = sendHttpPost("1901",inputMap,user);
        String resultJson = (String) result.getResVo().getOutput();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(resultJson,JsonObject.class);
        String jasonStr =jsonObject.get("list").toString();
        List<InsDictionaryVo> info =JSONObject.parseArray(jasonStr,InsDictionaryVo.class);
        String code_type="qg_"+type;
        List<InsSzybDict>  datalist = DataBaseHelper.queryForList(" select * from ins_szyb_dict where DEL_FLAG=0 and code_type=? and flag_chd='1' and CODE is not null", InsSzybDict.class,code_type);
        List<InsSzybDict> updatelist=new ArrayList<>() ;
        List<InsSzybDict> insetlist=new ArrayList<>() ;
        for (InsDictionaryVo dicData : info) {
            if(datalist!=null&&datalist.size()>0){
                String code=dicData.getValue();
                List<InsSzybDict> pubParamVoList = datalist.stream().filter(v ->code.equals(v.getCode()) && code_type.equals(v.getCodeType())).collect(Collectors.toList());
                if(pubParamVoList!=null&&pubParamVoList.size()>0)
                {
                    for (InsSzybDict item : pubParamVoList)
                    {
                        item.setName(dicData.getLabel());
                        updatelist.add(item);
                    }
                }else{
                    InsSzybDict insdic=new  InsSzybDict();
                    ApplicationUtils.setDefaultValue(insdic,true);
                    String spcode=CommonUtils.getPycode(dicData.getLabel());
                    insdic.setSpcode(spcode);
                    insdic.setCode(dicData.getValue());
                    insdic.setName(dicData.getLabel());
                    insdic.setCodeType(code_type);
                    insdic.setEuHpdicttype("01");
                    insdic.setFlagChd("1");
                    insdic.setFlagDef("0");
                    insetlist.add(insdic);
                }
            }else{
                InsSzybDict insdic=new  InsSzybDict();
                ApplicationUtils.setDefaultValue(insdic,true);
                String spcode=CommonUtils.getPycode(dicData.getLabel());
                insdic.setSpcode(spcode);
                insdic.setCode(dicData.getValue());
                insdic.setName(dicData.getLabel());
                insdic.setCodeType(code_type);
                insdic.setEuHpdicttype("01");
                insdic.setFlagChd("1");
                insdic.setFlagDef("0");
                insetlist.add(insdic);
            }
        }
        if(updatelist!=null&&updatelist.size()>0)
        {
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(InsSzybDict.class),updatelist);
        }
        if(insetlist!=null&&insetlist.size()>0)
        {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybDict.class),insetlist);
        }
        String sql="select * from ins_szyb_dict where DEL_FLAG=0 and code_type =? and flag_chd='0' and CODE is null";
        List<InsSzybDict>  namelist = DataBaseHelper.queryForList(sql, InsSzybDict.class,code_type);
        if(namelist==null|namelist.size()==0)
        {
            InsSzybDict insdic=new  InsSzybDict();
            ApplicationUtils.setDefaultValue(insdic,true);
            String spcode=CommonUtils.getPycode(name);
            insdic.setSpcode(spcode);
            insdic.setName(name);
            insdic.setCodeType(code_type);
            insdic.setEuHpdicttype("01");
            insdic.setFlagChd("0");
            insdic.setFlagDef("0");
            DataBaseHelper.insertBean(insdic);
        }
        return result;
    }


    /**
     * 020001001039
     * ????????????????????????
     * @param params
     * @param user
     * @return
     */
    public List<InsAllopatryClearDetailVo> getAllopatryClearDetail(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String type = MapUtils.getString(paramMap,"type");
        Map<String, Object> inputMap = getInputMap("data",paramMap);
        HisInsuResVo result = sendHttpPost("3260",inputMap,user);
        String resultJson = (String) result.getResVo().getOutput();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(resultJson,JsonObject.class);
        String jasonStr =jsonObject.get("list").toString();
        List<InsAllopatryClearDetailVo> datainfo =JSONObject.parseArray(jasonStr,InsAllopatryClearDetailVo.class);
        List<String> mdtrtIdList = new ArrayList<>();
        for (InsAllopatryClearDetailVo dicData : datainfo) {
            mdtrtIdList.add(dicData.getMdtrtId());
        }
        List<InsAllopatryClearDetailVo>  datalist = DataBaseHelper.queryForList(" select pi.code_ip,pi.name_pi,pi.insur_no from ins_qgyb_visit vi inner join pi_master pi on pi.pk_pi=vi.pk_pi where vi.mdtrt_id in(?)", InsAllopatryClearDetailVo.class,mdtrtIdList);
        List<InsAllopatryClearDetailVo>  record =new ArrayList<>();
        for (InsAllopatryClearDetailVo item : datainfo) {
            String id=item.getMdtrtId();
            List<InsAllopatryClearDetailVo> paramVoList = datalist.stream().filter(v ->v.getMdtrtId().equals(id)).collect(Collectors.toList());
            if(paramVoList!=null&&paramVoList.size()>0)
            {
                for (InsAllopatryClearDetailVo info : paramVoList) {
                    InsAllopatryClearDetailVo detail = new InsAllopatryClearDetailVo();
                    detail.setMdtrtId(info.getMdtrtId());
                    detail.setCodeIp(info.getCodeIp());
                    detail.setNamePi(info.getNamePi());
                    detail.setInsurNo(info.getInsurNo());
                    detail.setSeqno(item.getSeqno());
                    detail.setMdtrtarea(item.getMdtrtarea());
                    detail.setMedinsNo(item.getMedinsNo());
                    detail.setCertno(item.getCertno());
                    detail.setMdtrtSetlTime(item.getMdtrtSetlTime());
                    detail.setSetlSn(item.getSetlSn());
                    detail.setFulamtAdvpayFlag(item.getFulamtAdvpayFlag());
                    detail.setMedfeeSumamt(item.getMedfeeSumamt());
                    detail.setOptinsPaySumamt(item.getOptinsPaySumamt());
                    record.add(detail);
                }
            }
        }
        return record;
    }


    /**
     *020001001041
     *????????????????????????
     * @param user
     * @return
     */
    public HisInsuResVo allopatryClearConfirm(String params,String detailParam, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        InsAllopatryClearDetailParam[] detailList = JsonUtil.readValue(detailParam, InsAllopatryClearDetailParam[].class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", paramMap);
        map.put("detail", detailList);
        HisInsuResVo insuResVo = sendHttpPost("3261",map, user);
        return insuResVo;
    }

    /**
     * 020001001040
     * ??????????????????????????????
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo allopatryClearBack(String params,IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        Map<String, Object> inputMap = getInputMap("data",paramMap);
        HisInsuResVo insuResVo = sendHttpPost("3262",inputMap, user);
        return insuResVo;
    }

    /**
     * 020001001043
     *????????????????????????????????????
     * @param params
     * @param user
     * @return
     */
    public List<MatchCatalogVo> getMatchCatalogData(String params,IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        Map<String, Object> inputMap = getInputMap("data",paramMap);
        HisInsuResVo insuResVo = sendHttpPost("1317",inputMap, user);
        String resultJson = (String) insuResVo.getResVo().getOutput();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(resultJson,JsonObject.class);
        String jasonStr =jsonObject.get("data").toString();;
        List<MatchCatalogVo> data =JSONObject.parseArray(jasonStr,MatchCatalogVo.class);
        return data;
    }
    /**
     * 020001001044
     * ??????????????????????????????????????????
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo uploadSetlinfo(String params,IUser user) {
        User userInfo = (User) user;
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String setlId=MapUtils.getString(paramMap,"pkSettle");//?????????
        String codePv=MapUtils.getString(paramMap,"codePv");//?????????
        String deptName = "";
        String pkDept = userInfo.getPkDept();
        if(StringUtils.isNotBlank(pkDept)) {
            deptName = DataBaseHelper.queryForScalar("select name_dept from bd_ou_dept where pk_dept=?", String.class, pkDept);
        }
        Map<String,Object> inputMap = new HashMap<>(16);
        //	?????????????????????????????????setlinfo???
        List<InsQgybSetlinfo> qgybSetInfo= nationalInsuranceMapper.qryInsuranceSettleDetail(paramMap);

        int size=qgybSetInfo.size();
        if(qgybSetInfo.size()>0){
            InsQgybSetlinfo qgybsetlinfo=qgybSetInfo.get(0);
          //???????????????????????????
            Date bithDate =DateUtils.strToDate(qgybsetlinfo.getBrdy(),"yyyy-MM-dd HH:mm:ss") ;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(bithDate);
            long time = calendar.getTimeInMillis();
            long days = (System.currentTimeMillis() - time) / (1000 * 3600 * 24);
            if (days > 365L) {
                qgybsetlinfo.setAge((int) days / 365);
            } else {
                qgybsetlinfo.setNwbAge((int) days);
            }
            String ventUsedDur=qgybsetlinfo.getVentUsedDura();
            if(ventUsedDur.equals("0/0")||ventUsedDur.equals("/"))
            {
                qgybsetlinfo.setVentUsedDura(null);
            }
            String bfadmComaDura=qgybsetlinfo.getPwcryBfadmComaDura();
            if(bfadmComaDura.equals("0/0/0")||bfadmComaDura.equals("//"))
            {
                qgybsetlinfo.setPwcryBfadmComaDura(null);
            }
            String afadmComaDura=qgybsetlinfo.getPwcryAfadmComaDura();
            if(afadmComaDura.equals("0/0/0")||afadmComaDura.equals("//"))
            {
                qgybsetlinfo.setPwcryAfadmComaDura(null);
            }

            qgybsetlinfo.setHsorg(fixmedinsCode);
            qgybsetlinfo.setHsorgOpter(userInfo.getUserName());
            qgybsetlinfo.setMedinsFillDept(deptName);
            qgybsetlinfo.setMedinsFillPsn(userInfo.getUserName());
            inputMap.put("setlinfo",qgybsetlinfo);
        }
       //2?????????????????????,???????????????payinfo
        List<Map<String,Object>> payInfoList = nationalInsuranceMapper.qryInsQgybPayInfo(paramMap);
        if(!payInfoList.isEmpty()) {
            inputMap.put("payinfo",payInfoList);
        }
        List<Map<String,String>> diagList = nationalInsuranceMapper.qryInsQgybDiagInfo(paramMap);
        List<InsQgybSetlDiseinfo> insQgybDiseinfolist = new ArrayList<InsQgybSetlDiseinfo>();
        List<InsQgybOpspdiseinfo> insQgybOpspdiseinfolist = new ArrayList<InsQgybOpspdiseinfo>();
        if(!diagList.isEmpty()){
            for (Map<String, String> map : diagList) {
                String clrType = map.get("clrType");
                if("21".equals(clrType)){
                    //4???????????????????????????????????????diseinfo???
                    InsQgybSetlDiseinfo insQgybDiseinfo = new InsQgybSetlDiseinfo();
                    ZsrmQGUtils.mapToBean(insQgybDiseinfo,map);
                    insQgybDiseinfolist.add(insQgybDiseinfo);

                }else{
                    //3????????????????????????????????????????????????opspdiseinfo???
                    InsQgybOpspdiseinfo insQgybOpspdiseinfo = new InsQgybOpspdiseinfo();
                    ZsrmQGUtils.mapToBean(insQgybOpspdiseinfo,map);
                    insQgybOpspdiseinfolist.add(insQgybOpspdiseinfo);

                }
            }
        }
        if(insQgybDiseinfolist.size()>0)
        {
            inputMap.put("diseinfo",insQgybDiseinfolist);
        }
        if(insQgybOpspdiseinfolist.size()>0)
        {
            inputMap.put("opspdiseinfo",insQgybOpspdiseinfolist);
        }
        //5???????????????????????????????????????iteminfo???
        List<Map<String,String>> itemList = nationalInsuranceMapper.qryInsQgybItemInfo(paramMap);
        String[] medChrgitmTypes = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14"};
        List<InsQgybIteminfo> insQgybIteminfolist = new ArrayList<InsQgybIteminfo>();
        for (Map<String, String> map : itemList) {
            InsQgybIteminfo insQgybIteminfo = new InsQgybIteminfo();
            ZsrmQGUtils.mapToBean(insQgybIteminfo,map);
            insQgybIteminfo.setOthAmt(insQgybIteminfo.getAmt().subtract(insQgybIteminfo.getClaaSumfee()).subtract(insQgybIteminfo.getClabAmt()).subtract(insQgybIteminfo.getFulamtOwnpayAmt()));
            String medChrgitm = insQgybIteminfo.getMedChrgitm();
            if (Arrays.binarySearch(medChrgitmTypes,medChrgitm)<0){
                insQgybIteminfo.setMedChrgitm("14");
            }
            insQgybIteminfolist.add(insQgybIteminfo);
        }
        inputMap.put("iteminfo",insQgybIteminfolist);
        //6???????????????????????????????????????oprninfo???
        List<Map<String,Object>> oprnList = nationalInsuranceMapper.qryInsQgybOprnInfo(paramMap);
        if(!oprnList.isEmpty()){
            inputMap.put("oprninfo",oprnList);
        }
        //7???????????????????????????????????????icuinfo???
       // inputMap.put("icuinfo",null);
        HisInsuResVo insuResVo =new HisInsuResVo();
        try {
            insuResVo = sendHttpPost("4101",inputMap, user);
        }catch (Exception e) {
            insuResVo.setInfcode("-1");
            insuResVo.setErrMsg(e.getMessage());
        }
        return insuResVo;
    }

     /**
      * 020001001045
     *????????????????????????
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo getPayRecord(String params,IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", paramMap);
        HisInsuResVo insuResVo = sendHttpPost("90100",map, user);
        return insuResVo;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public String getFixmedinsCode() {
        return fixmedinsCode;
    }
}
