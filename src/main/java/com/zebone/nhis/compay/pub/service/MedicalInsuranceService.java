package com.zebone.nhis.compay.pub.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.ma.pub.platform.syx.vo.PiMasterEmpiInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybDict;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybDicttype;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.compay.pub.dao.MedicalInsuranceMapper;
import com.zebone.nhis.compay.pub.vo.DictTypesData;
import com.zebone.nhis.compay.pub.vo.InsCommonMedical;
import com.zebone.nhis.compay.pub.vo.InsCommonTableParam;
import com.zebone.nhis.compay.pub.vo.InsItemResult;
import com.zebone.nhis.compay.pub.vo.InsItemsBySchsrvData;
import com.zebone.nhis.compay.pub.vo.MedicalInsuranceVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * ????????????
 *
 * @author Administrator
 */
@Service
public class MedicalInsuranceService {

    @Autowired
    private MedicalInsuranceMapper medicalInsuranceMapper;

    @Autowired
    private CgQryMaintainService cgQryMaintainService;

    private Logger logger = LoggerFactory.getLogger("com.zebone");

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     */
    public void saveMedicalInsurance(String param, IUser user) {
        MedicalInsuranceVo medicalInsuranceVo = JsonUtil.readValue(param,
                MedicalInsuranceVo.class);

        String pkHp = medicalInsuranceVo.getPkHp();
        List<DictTypesData> dictTypes = medicalInsuranceVo.getDictTypes();
        List<InsZsybDicttype> insDicttypes = new ArrayList<>();
        List<InsZsybDict> insDicts = new ArrayList<>();

        if (dictTypes == null || dictTypes.size() == 0) {
            throw new BusException("????????????????????????????????????");
        }

        for (DictTypesData dictTypesData : dictTypes) {
            // ??????????????????????????????
            InsZsybDicttype insDicttype = new InsZsybDicttype();
            ApplicationUtils.setDefaultValue(insDicttype, true);
            insDicttype.setPkHp(pkHp);
            insDicttype.setCodeType(dictTypesData.getCodeType());
            insDicttype.setNameType(dictTypesData.getNameType());
            insDicttype.setSpcode(dictTypesData.getSpcode());
            insDicttype.setdCode(dictTypesData.getdCode());
            insDicttypes.add(insDicttype);

            for (InsZsybDict dictsList : dictTypesData.getDicts()) {
                // ??????????????????????????????
                InsZsybDict insDict = new InsZsybDict();
                ApplicationUtils.setDefaultValue(insDict, true);
                insDict.setPkInsdicttype(insDicttype.getPkInsdicttype());
                insDict.setName(dictsList.getName());
                insDict.setCode(dictsList.getCode());
                insDict.setSpcode(dictsList.getSpcode());
                insDict.setdCode(dictsList.getdCode());
                insDicts.add(insDict);
            }
        }
        if (insDicttypes.size() > 0) {
            DataBaseHelper.batchUpdate(
                    DataBaseHelper.getInsertSql(InsZsybDicttype.class),
                    insDicttypes);
        }
        if (insDicts.size() > 0) {
            DataBaseHelper.batchUpdate(
                    DataBaseHelper.getInsertSql(InsZsybDict.class), insDicts);
        }
    }

    /**
     * ?????????????????????
     *
     * @param param
     * @param user
     */
    public void saveCommonMedical(String param, IUser user) {
        InsCommonTableParam insCommonParam = JsonUtil.readValue(param,
                InsCommonTableParam.class);
        String tableName = CommonUtils.getString(insCommonParam.getTableName());
        String primaryKey = CommonUtils.getString(insCommonParam
                .getPrimaryKey());
        String uniqueKey = CommonUtils.getString(insCommonParam.getUniqueKey());
        List<String> dataColumn = insCommonParam.getDataColumn();
        List<List<String>> dataValue = insCommonParam.getDataValue();

        List<Map<String, Object>> valueList = new ArrayList<>();
        Set<String> primaryStr = new HashSet<>();
        StringBuffer keyBuffer = new StringBuffer();
        keyBuffer.append(":pk,");
        for (int i = 0; i < dataColumn.size(); i++) {
            keyBuffer.append(":").append(dataColumn.get(i)).append(",");
        }
        keyBuffer.append(":pkOrg,:creator,:createTime,:delFlag,:ts,:modifier");

        // ???????????????????????????????????????value list
        for (List<String> list : dataValue) {
            if (list.size() != dataColumn.size()) {
                throw new BusException("??????????????????????????????????????????");
            }
            Map<String, Object> colValueMap = new LinkedHashMap<>();

            colValueMap.put("pk", NHISUUID.getKeyId());
            for (int i = 0; i < dataColumn.size(); i++) {
                // ??????????????????column??????????????????
                if (dataColumn.get(i).equalsIgnoreCase(uniqueKey)) {
                    primaryStr.add(list.get(i));
                }
                colValueMap.put(dataColumn.get(i), list.get(i));
            }

            colValueMap.put("pkOrg", UserContext.getUser().getPkOrg());
            colValueMap.put("creator", UserContext.getUser().getPkEmp());
            colValueMap.put("createTime", new Date());
            colValueMap.put("delFlag", "0");
            colValueMap.put("ts", new Date());
            colValueMap.put("modifier", UserContext.getUser().getPkEmp());
            valueList.add(colValueMap);
        }
        // ?????????????????????primaryKey??????????????? ----???????????????????????????????????????

        String primarySql = "delete from " + tableName + " where " + uniqueKey
                + " = ?";
        Iterator<String> it = primaryStr.iterator();
        while (it.hasNext()) {
            DataBaseHelper.execute(primarySql, new Object[]{it.next()});
        }

        // ??????1 ??????????????????????????????, ????????????????????????, ??????????????????
        StringBuffer insertBuffer = new StringBuffer();
        insertBuffer
                .append("insert into ")
                .append(tableName)
                .append("(")
                .append(primaryKey)
                .append(",")
                .append(dataColumn.toString().substring(1,
                        dataColumn.toString().length() - 1))
                .append(", PK_ORG, CREATOR, CREATE_TIME, DEL_FlAG, TS, MODIFIER)")
                .append(" values(").append(keyBuffer.toString()).append(")");

        DataBaseHelper.batchUpdate(CommonUtils.getString(insertBuffer),
                valueList);

    }

    /**
     * ???????????????????????????,????????????
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<List<Object>> selectCommonMedical(String param, IUser user) {
        InsCommonMedical insCommonMedical = JsonUtil.readValue(param,
                InsCommonMedical.class);
        List<String> dataColumn = insCommonMedical.getDataColumn();
        int pageIndex = CommonUtils.getInteger(insCommonMedical.getPageIndex());
        int pageSize = CommonUtils.getInteger(insCommonMedical.getPageSize());
        // ????????????
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> queryForList = medicalInsuranceMapper
                .selectCommonMedical(insCommonMedical);

        List<List<Object>> returnList = new ArrayList<>();
        // ??????mapper??????????????????????????????????????? ?????????list
        for (int i = 0; i < queryForList.size(); i++) {
            List<Object> mapList = new LinkedList<>();
            Map<String, Object> itemMap = queryForList.get(i);
            for (String dataItem : dataColumn) {
                mapList.add(itemMap.get(dataItem));
            }
            returnList.add(mapList);
        }

        Page<List<Object>> page = MyBatisPage.getPage();
        page.setRows(returnList);
        return page;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     */
    public void deleteInsItem(String param, IUser user) {
        // ?????????????????????
        String pkInsItemMap = JsonUtil.readValue(param, String.class);
        DataBaseHelper
                .update("update INS_XIAN_ITEM_MAP set del_flag='1' where PK_INSITEMMAP=? and del_flag = '0'",
                        new Object[]{pkInsItemMap});
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     */
    public void saveInsitemResult(String param, IUser user) {
        @SuppressWarnings("unchecked")
        List<InsItemResult> insItems = JsonUtil.readValue(param, List.class);

        String sql = "update INS_XIAN_ITEM_MAP set eu_examine=:euExamine and examine_note=:examineNote"
                + " where pk_hp=:pkHp and pk_item=:pkItem and code_insitem=:codeInsitem and del_flag='0'";

        DataBaseHelper.batchUpdate(sql, insItems);

        // for (InsItemResult insItem : insItems) {
        // String pkHp = CommonUtils.getString(insItem.getPkHp());
        // String pkItem = CommonUtils.getString(insItem.getPkItem());
        // String codeInsitem = CommonUtils.getString(insItem.getCodeInsitem());
        // String euExamine = CommonUtils.getString(insItem.getEuExamine());
        // String examineNote = CommonUtils.getString(insItem.getExamineNote());
        // Map<String, String> map = new HashMap<>();
        // map.put("pkHp", pkHp);
        // map.put("pkItem", pkItem);
        // map.put("codeInsitem", codeInsitem);
        // map.put("euExamine", euExamine);
        // map.put("examineNote", examineNote);
        //
        // String usql =
        // "update INS_XIAN_ITEM_MAP set eu_examine=:euExamine and examine_note=:examineNote "
        // +
        // " where pk_hp=:pkHp and pk_item=:pkItem and code_insitem=:codeInsitem and del_flag = '0'";
        // DataBaseHelper.update(usql, InsXaybItemMap.class, map);
        // }
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public PiMaster createPatientByInsCard(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        PiMaster master = JsonUtil.readValue(param, PiMaster.class);
        String ybInfo = null;
        if (JsonUtil.getJsonNode(param, "yBPatientInfo") != null) {
            ybInfo = JsonUtil.getJsonNode(param, "yBPatientInfo").toString();
        }
        String idNo = CommonUtils.getString(paramMap.get("idNo"));// ???????????????
        String insurNo = CommonUtils.getString(paramMap.get("insurNo"));// ????????????
        String pkHp = CommonUtils.getString(paramMap.get("pkHp"));// ????????????
        String namePi = CommonUtils.getString(paramMap.get("namePi"));// ????????????
        Date birthDate = DateUtils.strToDate(paramMap.get("birthDate")
                .toString());
        String mobile = CommonUtils.getString(paramMap.get("mobile"));// ????????????
        String dtSex = CommonUtils.getString(paramMap.get("dtSex"));// ????????????
        String pkInsu = CommonUtils.getString(paramMap.get("pkInsu"));// ?????????

        if (StringUtils.isBlank(idNo) && StringUtils.isBlank(insurNo)) {
            throw new BusException("?????????????????????????????????????????????");
        }

        String insCode = null;
        if (paramMap.get("insCode") != null) {
            insCode = CommonUtils.getString(paramMap.get("insCode"));// ????????????????????????
        }
        Map<String, Object> queryForMap = null;
        if (!org.springframework.util.StringUtils.isEmpty(idNo)) { // ?????????????????????
            queryForMap = DataBaseHelper
                    .queryForMap(
                            "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                    + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.id_no=? and pm.del_flag='0'",
                            new Object[]{idNo.toUpperCase()});
        }

		if (queryForMap != null) {
			master.setNamePi(namePi);
		}
        if (queryForMap == null) { // ???????????????
            if (insCode != null
                    && (insCode.equals("04") || insCode.equals("03")) 
                    && !CommonUtils.isEmptyString(insurNo) ) // ??????????????????,???????????????
            {
                queryForMap = DataBaseHelper
                        .queryForMap(
                                "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                        + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.insur_no=? and pm.del_flag='0'",
                                new Object[]{insurNo.toUpperCase()});
            }

        }

        // ??????-????????????
        if (queryForMap == null) {
            // PiMaster master = new PiMaster();
            // ???????????????????????????
//            String codeIp = CommonUtils.getString(paramMap.get("codeIp"));// ?????????
//            if (CommonUtils.isEmptyString(codeIp)) {
//                codeIp = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL);
//                master.setCodeIp(codeIp);//???????????????
//                logger.info("==============???" + master.getNamePi() + "??????????????????????????????????????????:" + master.getCodeIp() + "============");
//            }
            Map<String, Object> picateMap = DataBaseHelper
                    .queryForMap(
                            "select pk_picate from pi_cate where flag_def='1' and del_flag='0'",
                            new Object[]{});

            if (picateMap != null && picateMap.get("pkPicate") != null) {
                master.setPkPicate(picateMap.get("pkPicate").toString());// ???????????????????????????
            }

            String codePi = ApplicationUtils
                    .getCode(SysConstant.ENCODERULE_CODE_HZ);
            master.setCodePi(codePi);// ????????????
            master.setIdNo(idNo);// ????????????
            master.setDtIdtype("01");// ???????????? --01
            master.setInsurNo(insurNo);// ????????????
            master.setNamePi(namePi);// ??????
            master.setDtSex(dtSex);// ????????????
            master.setBirthDate(birthDate);// ????????????
            master.setMobile(mobile);// ????????????
            //master.setCodeIp(codeIp);
            master.setCodeOp(ApplicationUtils
                    .getCode(SysConstant.ENCODERULE_CODE_MZBL)); // ?????????
            master.setCodeEr(ApplicationUtils.getCode("0303"));

            ApplicationUtils.setDefaultValue(master, true);
            DataBaseHelper.insertBean(master);
            // ??????????????????
            PiInsurance insurance = new PiInsurance();
            insurance.setPkPi(master.getPkPi());
            insurance.setPkHp(pkHp);
            insurance.setDelFlag("0");
            insurance.setCreateTime(new Date());
            DataBaseHelper.insertBean(insurance);
            // ????????????????????????PiAcc??????
            PiAcc acc = new PiAcc();
            acc.setPkPi(master.getPkPi());
            //acc.setCodeAcc(codeIp);
            acc.setAmtAcc(BigDecimal.ZERO);
            acc.setCreditAcc(BigDecimal.ZERO);
            acc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
            DataBaseHelper.insertBean(acc);
        } else {// ????????????
            //?????????????????????????????????
//            String codeIp = CommonUtils.getString(queryForMap.get("codeIp"));// ?????????
//            if (CommonUtils.isEmptyString(codeIp)) {
//                codeIp = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL);
//                master.setCodeIp(codeIp);//???????????????
//                DataBaseHelper.update("update pi_master set code_ip=? where pk_pi=?", new Object[]{codeIp, master.getPkPi()});
//                logger.info("==============???" + master.getNamePi() + "??????????????????????????????????????????:" + master.getCodeIp() + "============");
//            }
            // ????????????????????????????????????????????????
            master.setPkPi(queryForMap.get("pkPi").toString());
            master.setInsurNo(insurNo);// ????????????
            // if (picateMap != null && picateMap.get("pkPicate") != null) {
            // master.setPkPicate(picateMap.get("pkPicate").toString());//
            // ???????????????????????????
            // }

            //??????pk_pi????????????????????????addrcode_birth???addrcode_origin???addrcode_regi???addrcode_cur????????????????????????????????????????????????
            PiMaster piAddrInfo = DataBaseHelper.queryForBean(
                    "select addrcode_birth,addrcode_origin,addrcode_regi,addrcode_cur from PI_MASTER where pk_pi = ?",
                    PiMaster.class,
                    new Object[]{queryForMap.get("pkPi").toString()}
                    );

            if(piAddrInfo!=null){
                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeBirth())){
                    master.setAddrcodeBirth(null);
                    master.setAddrBirth(null);
                }

                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeOrigin())){
                    master.setAddrcodeOrigin(null);
                    master.setAddrOrigin(null);
                }

                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeRegi())){
                    master.setAddrcodeRegi(null);
                    master.setAddrRegi(null);
                }

                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeCur())){
                    master.setAddrcodeCur(null);
                    master.setAddrCur(null);
                }
            }

            //??????lb??????bug[27158],???????????????????????????mapper???xml????????????????????????????????????????????????????????????????????????
            medicalInsuranceMapper.updatrPiMaster(master);
           /* String insurSql = "update pi_master set INSUR_NO = ? ,"
                    + " ADDRCODE_BIRTH = ? ,ADDR_BIRTH = ? ,ADDRCODE_ORIGIN"
                    + " = ? ,ADDR_ORIGIN = ?,ADDRCODE_REGI = ? ,ADDR_REGI = ?,ADDR_REGI_DT = ?,POSTCODE_REGI = ?,ADDRCODE_CUR = ?,"
                    + "ADDR_CUR = ? ,ADDR_CUR_DT = ? ,POSTCODE_CUR = ?,POSTCODE_WORK = ?"
                    + " where del_flag = '0' and pk_pi = ?";
            DataBaseHelper.update(
                    insurSql,
                    new Object[]{insurNo, master.getAddrcodeBirth(),
                            master.getAddrBirth(), master.getAddrcodeOrigin(),
                            master.getAddrOrigin(), master.getAddrcodeRegi(),
                            master.getAddrRegi(), master.getAddrRegiDt(),
                            master.getPostcodeRegi(), master.getAddrcodeCur(),
                            master.getAddrCur(), master.getAddrCurDt(),
                            master.getPostcodeCur(), master.getPostcodeWork(),
                            queryForMap.get("pkPi").toString()});*/
            
            // ???????????????????????????????????????????????????
            DataBaseHelper
                    .update("update pi_insurance set flag_def='0' where pk_pi=? and del_flag='0'",
                            new Object[]{queryForMap.get("pkPi").toString()});

            Map<String, Object> map = new HashMap<>();
            map.put("pkHp", pkHp);
            map.put("pkPi", queryForMap.get("pkPi").toString());
            // ??????????????????
            String insuranceSql = "update pi_insurance set PK_HP=:pkHp, FLAG_DEF='1' "
                    + " where del_flag='0' and pk_pi=:pkPi ";
            DataBaseHelper.update(insuranceSql, map);

            Date date = new Date();
            Calendar no = Calendar.getInstance();
            no.setTime(date);
            no.set(Calendar.DATE, no.get(Calendar.DATE) - 3);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String begin = sdf.format(no.getTime()).concat("000000");
            String end = sdf.format(date).concat("235959");

            // ?????????????????????PV_ENCOUNTER???????????????
            Map<String, Object> pvMap = DataBaseHelper
                    .queryForMap(
                            "select * from "
                                    + "PV_ENCOUNTER where pk_pi = ? and del_flag = '0'"
                                    + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                    + " create_time > to_date(?, 'yyyymmddhh24miss')",
                            new Object[]{queryForMap.get("pkPi").toString(),
                                    end, begin});
            // ??????????????????
            if (pvMap != null && pvMap.get("pkPi") != null) {
                DataBaseHelper
                        .update("update PV_ENCOUNTER set pk_insu = ? where pk_pi= ? and del_flag='0' and eu_pvtype='3' "
                                        + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                        + " create_time > to_date(?, 'yyyymmddhh24miss')",
                                new Object[]{pkHp,
                                        pvMap.get("pkPi").toString(), end,
                                        begin});

            }
        }
        logger.info("==========???????????????????????????" + master.getNamePi() + "????????????????????????:" + master.getCodeIp() + "=========");
        PiMaster piMaster = null;

        if (insCode != null && insCode.equals("04") || (StringUtils.isBlank(idNo))) // ??????????????????
        {
            piMaster = DataBaseHelper
                    .queryForBean(
                            "select * from pi_master where del_flag ='0' and insur_no=?",
                            PiMaster.class, insurNo);
        }
        if(piMaster==null){
            piMaster = DataBaseHelper.queryForBean(
                    "select * from pi_master where del_flag ='0' and id_no=? order by CREATE_TIME desc ",
                    PiMaster.class, idNo);
        }
        return piMaster;
    }

    //???????????????   ???????????????1???????????????????????????????????????????????? 2???????????????????????????????????????????????????????????????????????????
    private String getCodeIp(String codeIp) {
        if (!CommonUtils.isEmptyString(codeIp)) {
            codeIp = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL);
        }
        return codeIp;
    }

    /**
     * ???????????????????????? ????????????????????????????????????????????????----??????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<InsItemsBySchsrvData> getItemsBySchsrv(String param, IUser user) {
        Map<String, Object> mapListParam = JsonUtil.readValue(param, Map.class);
        List<InsItemsBySchsrvData> mapResultList = new ArrayList<>();
        InsItemsBySchsrvData qryThisServicePrice = null;
        List<Map<String, Object>> opDtLists = (List<Map<String, Object>>) mapListParam
                .get("opDtList");// ??????????????????(?????????????????????)
        // Map<String, Object> mapSchsrv =
        // cgQryMaintainService.qrySchSrvByPkSchsrv(mapListParam);
        // ?????????????????????????????????????????????????????????
        List<String> pkItems = cgQryMaintainService
                .qrySchSrvOrdsByPkSchsrv(mapListParam);
        // ?????????????????????????????????????????????pkitem?????????????????????list?????????map
        Map<String, Double> mapOpDtLists = new HashMap<String, Double>();
        for (Map<String, Object> mapTemp : opDtLists) {
            pkItems.add(mapTemp.get("pkItem").toString());
            mapOpDtLists.put(mapTemp.get("pkItem").toString(),
                    Double.parseDouble(mapTemp.get("quan").toString()));
        }
        for (String pkItem : pkItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("pkItem", pkItem);
            itemMap.put("pkOrg", ((User) user).getPkOrg());
            itemMap.put("pkHp", mapListParam.get("pkInsu").toString());// ??????????????????
            itemMap.put("dateHap", Calendar.getInstance().getTime()); // ???????????????
            Map<String, Object> bdItem = cgQryMaintainService
                    .qryBdItemByPk(itemMap);
            BdHp bdHap = cgQryMaintainService.qryBdHpInfo(itemMap);
            // ??????????????????????????????
            if (bdItem == null) {
                throw new BusException("???" + pkItem + "???" + ":????????????????????????");
            }
            if (bdItem.get("flagActive") == null
                    || "0".equals(bdItem.get("flagActive").toString())) {
                throw new BusException("???" + bdItem.get("name") + "???"
                        + ":????????????????????????");
            }
            if (bdItem.get("delFlag") == null
                    || "1".equals(bdItem.get("delFlag").toString())) {
                throw new BusException("???" + bdItem.get("name") + "???"
                        + ":????????????????????????");
            }
            qryThisServicePrice = queryOraginalMessage(itemMap);
            // ??????????????????????????????
            if (qryThisServicePrice == null) {
                throw new BusException("???" + bdItem.get("name") + "??? ????????????????????????"
                        + bdHap.getName() + "??????????????????");
            }
            // ?????????????????????????????????1??????????????????????????????????????????????????????
            if (mapOpDtLists.containsKey(pkItem)) {
                qryThisServicePrice.setQuan(mapOpDtLists.get(pkItem)
                        .doubleValue());
            } else {
                qryThisServicePrice.setQuan(1.0D);
            }
            qryThisServicePrice.setAmount(qryThisServicePrice.getPrice()
                    * qryThisServicePrice.getQuan());

            mapResultList.add(qryThisServicePrice);
        }
        return mapResultList;
    }

    // ??????????????????????????????
    private InsItemsBySchsrvData queryOraginalMessage(
            Map<String, Object> mapParam) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select item.pk_item,item.name as itemName,price.PRICE,item.spec,unit.name as unitName,item.code ");
        sql.append(" from bd_item item ");
        sql.append("inner join bd_item_price price on item.pk_item=price.pk_item and price.pk_org = ? and price.flag_stop = 0 and nvl(price.del_flag,'0') = '0'");
        sql.append(" left join BD_UNIT unit on item.PK_UNIT = unit.pk_unit ");
        sql.append(" inner join bd_pricetype_cfg cfg on price.eu_pricetype=cfg.eu_pricetype and cfg.pk_hp=? and cfg.pk_org=? and nvl(cfg.del_flag,'0') = '0' ");
        sql.append(" where item.pk_item = ? and price.date_begin <= ? and price.date_end > ? and nvl(item.del_flag,'0') = '0'  ");

        InsItemsBySchsrvData qryThisServicePrice = DataBaseHelper.queryForBean(
                sql.toString(),
                InsItemsBySchsrvData.class,
                new Object[]{mapParam.get("pkOrg"), mapParam.get("pkHp"),
                        mapParam.get("pkOrg"), mapParam.get("pkItem"),
                        mapParam.get("dateHap"), mapParam.get("dateHap")});
        // new Object[] {mapParam.get("pkHp"), mapParam.get("pkItem")});
        return qryThisServicePrice;
    }

    // ????????????????????????bd_pd????????????sql
    private String buildPdSql(String joinType) {
        /**
         * bd_pd??????pk_pd???INS_XAYB_ITEM_MAP??????????????????pk_item
         * INS_XAYB_ITEM_MAP???INS_XAYB_ITEM???????????????????????? map?????? code_Insitem; // ??????????????????
         * ------item?????? ybtyxmbm; //???????????????????????? map?????? ybxxzch; // ?????????????????????
         * ------item?????? zcxxxlh; //?????????????????????
         */
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select pd.pk_pd as pkItems,")
                .append("pd.name as hisName,")
                .append("pd.short_name as shortName,")
                .append("pd.pk_factory as factoryName,")
                .append("pd.spcode as hisspcode,")
                .append("pd.spec as hisSpec,")
                .append("pd.dt_dosage as dosageName,")
                .append("pd.pk_unit_pack as unitPackName,")
                .append("pd.price as hisPrice, ")
                .append("pd.dt_abrd as abordFlag,")
                .append("map.EU_EXAMINE,")
                .append("map.EXAMINE_NOTE from bd_pd pd ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM_MAP map on map.pk_item = pd.PK_PD ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM xayb on xayb.ybtyxmbm = map.code_Insitem and xayb.zcxxxlh = map.ybxxzch");

        return sBuffer.toString();
    }

    // ????????????????????????bd_item????????????sql
    private String buildItemSql(String joinType) {
        /**
         * bd_item??????pk_item???INS_XAYB_ITEM_MAP??????????????????pk_item
         * INS_XAYB_ITEM_MAP???INS_XAYB_ITEM???????????????????????? map?????? code_Insitem; // ??????????????????
         * ------item?????? ybtyxmbm; //???????????????????????? map?????? ybxxzch; // ?????????????????????
         * ------item?????? zcxxxlh; //?????????????????????
         */
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select item.pk_item as pkItems,")
                .append("item.name as hisName,")
                .append("item.name_prt as shortName,")
                .append("null as factoryName,")
                .append("item.spcode as hisspcode,")
                .append("item.spec as hisSpec,")
                .append("null as dosageName,")
                .append("item.pk_unit as unitPackName,")
                .append("item.price as hisPrice,")
                .append("null as abordFlag,")
                .append("map.EU_EXAMINE,")
                .append("map.EXAMINE_NOTE from bd_item item ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM_MAP map on map.pk_item = item.pk_item ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM xayb on xayb.ybtyxmbm = map.code_Insitem and xayb.zcxxxlh = map.ybxxzch");

        return sBuffer.toString();
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public PiMaster updatePatientByInsCard(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        PiMaster master = JsonUtil.readValue(param, PiMaster.class);
        String idNo = CommonUtils.getString(paramMap.get("idNo"));// ???????????????
        String insurNo = CommonUtils.getString(paramMap.get("insurNo"));// ????????????
        String pkHp = CommonUtils.getString(paramMap.get("pkHp"));// ????????????
        String insCode = null;
        Map<String, Object> queryForMap = null;
        if (paramMap.get("insCode") != null) {
            insCode = CommonUtils.getString(paramMap.get("insCode"));// ????????????????????????
            if (!org.springframework.util.StringUtils.isEmpty(insCode)) {
                if (insCode.equals("07")) { // ??????
                    queryForMap = DataBaseHelper
                            .queryForMap(
                                    "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                            + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.id_no=? and pm.del_flag='0'",
                                    new Object[]{idNo.toUpperCase()});
                } else if (insCode.equals("04")) { // ??????
                    queryForMap = DataBaseHelper
                            .queryForMap(
                                    "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                            + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.insur_no=? and pm.del_flag='0'",
                                    new Object[]{insurNo.toUpperCase()});
                } else if (insCode.equals("03")) { // ?????????
                    queryForMap = DataBaseHelper
                            .queryForMap(
                                    "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                            + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.id_no=? and pm.del_flag='0'",
                                    new Object[]{idNo.toUpperCase()});

                    if (queryForMap == null) {
                        queryForMap = DataBaseHelper
                                .queryForMap(
                                        "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                                + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.insur_no=? and pm.del_flag='0'",
                                        new Object[]{insurNo.toUpperCase()});
                    }

                }

            }
        }

        if (queryForMap != null) {

            String insurSql = "update pi_master set INSUR_NO = ?  where del_flag = '0' and pk_pi = ?";
            DataBaseHelper.update(insurSql, new Object[]{insurNo,
                    queryForMap.get("pkPi").toString()});

            DataBaseHelper
                    .update("update pi_insurance set flag_def='0' where pk_pi=? and del_flag='0'",
                            new Object[]{queryForMap.get("pkPi").toString()});
            Map<String, Object> map = new HashMap<>();
            map.put("pkHp", pkHp);
            map.put("pkPi", queryForMap.get("pkPi").toString());
            String insuranceSql = "update pi_insurance set PK_HP=:pkHp, FLAG_DEF='1' "
                    + " where del_flag='0' and pk_pi=:pkPi ";
            DataBaseHelper.update(insuranceSql, map);
            Date date = new Date();
            Calendar no = Calendar.getInstance();
            no.setTime(date);
            no.set(Calendar.DATE, no.get(Calendar.DATE));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String begin = sdf.format(no.getTime()).concat("000000");
            String end = sdf.format(date).concat("235959");
            Map<String, Object> pvMap = DataBaseHelper
                    .queryForMap(
                            "select * from "
                                    + "PV_ENCOUNTER where pk_pi = ? and del_flag = '0'"
                                    + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                    + " create_time > to_date(?, 'yyyymmddhh24miss')",
                            new Object[]{queryForMap.get("pkPi").toString(),
                                    end, begin});
            // ??????????????????
            if (pvMap != null && pvMap.get("pkPi") != null) {
                DataBaseHelper
                        .update("update PV_ENCOUNTER set pk_insu = ? where pk_pi= ? and del_flag='0'"
                                        + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                        + " create_time > to_date(?, 'yyyymmddhh24miss')",
                                new Object[]{pkHp,
                                        pvMap.get("pkPi").toString(), end,
                                        begin});

            }

            PiMaster piMaster = null;
            if (insCode.equals("04")) // ??????????????????
            {
                piMaster = DataBaseHelper
                        .queryForBean(
                                "select * from pi_master where del_flag ='0' and insur_no=?",
                                PiMaster.class, insurNo);
            } else if (insCode.equals("03")) {
                piMaster = DataBaseHelper.queryForBean(
                        "select * from pi_master where del_flag ='0' and id_no=?",
                        PiMaster.class, idNo);
                if (piMaster == null) {
                    piMaster = DataBaseHelper
                            .queryForBean(
                                    "select * from pi_master where del_flag ='0' and insur_no=?",
                                    PiMaster.class, insurNo);
                }
            } else {
                piMaster = DataBaseHelper.queryForBean(
                        "select * from pi_master where del_flag ='0' and id_no=?",
                        PiMaster.class, idNo);
            }
            logger.info("==========???????????????????????????" + piMaster.getNamePi() + "??????????????????:" + piMaster.getCodeIp() + "=========");
            return piMaster;
        } else {
            return null;
        }


    }

}
