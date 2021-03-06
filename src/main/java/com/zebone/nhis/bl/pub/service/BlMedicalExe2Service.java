package com.zebone.nhis.bl.pub.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.dao.BlMedicalExe2Mapper;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.RisPrintInfoVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsrm.bl.vo.PageMedListVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ???????????????????????????????????????????????????
 */
@Service
public class BlMedicalExe2Service {

    @Autowired
    private BlIpMedicalExeService blIpMedicalExeService;
    @Autowired
    private OpCgPubService opCgPubService;
    @Resource
    private BlMedicalExe2Mapper blMedicalExe2Mapper;
    @Autowired
    private IpCgPubService ipCgPubService;

    /**
     * ??????????????????-????????????-??????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpMedicalApp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateBeginPlan"))) {
            paramMap.put("dateBeginPlan", CommonUtils.getString(paramMap.get("dateBeginPlan")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEndPlan"))) {
            paramMap.put("dateEndPlan", CommonUtils.getString(paramMap.get("dateEndPlan")).substring(0, 8) + "235959");
        }
        
        if(CommonUtils.isNotNull(paramMap.get("dateBegin")) && CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
        	Date dateBegin = DateUtils.strToDate(MapUtils.getString(paramMap, "dateBegin"));
            Date dateEnd = DateUtils.strToDate(MapUtils.getString(paramMap, "dateEnd"));
            int day = DateUtils.getDateSpace(dateBegin, dateEnd);
            if (day > 31) {
                throw new BusException("?????????????????????????????????31???!");
            }
        }
        
        paramMap.put("recentDay", DateUtils.dateToStr("yyyyMMdd", DateUtils.getSpecifiedDay(new Date(), -3)) + "000000");
        List<Map<String, Object>> list = blMedicalExe2Mapper.qryIpMedAppInfo(paramMap);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Map<String, Object>> maps = new ArrayList<>(list.size());
            List<String> pkExoccs = list.stream().map(m -> MapUtils.getString(m, "pkExocc")).distinct().collect(Collectors.toList());
            List<Map<String, Object>> list1 = getMedBlIpdtInfo(pkExoccs);
            boolean notEm = CollectionUtils.isNotEmpty(list1);
            list.forEach(a -> {
                //??????
                if ("9".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream()
                                .filter(p -> MapUtils.getString(a, "pkExocc").equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    a.put("cnt", cnt);
                    a.put("itemNum", itemNum);
                    maps.add(a);
                }
                //?????????
                if ("1".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream().filter(p -> MapUtils.getString(a, "pkExocc")
                                .equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    boolean isFit = ("1".equals(MapUtils.getString(a, "euStatusOcc")) && cnt == 0) || cnt > 0;
                    if (isFit) {
                        a.put("cnt", cnt);
                        a.put("itemNum", itemNum);
                        maps.add(a);
                    }
                }
                // ?????????
                if ("0".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream().filter(p -> MapUtils.getString(a, "pkExocc")
                                .equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    boolean isFit = ("0".equals(MapUtils.getString(a, "euStatusOcc")) && cnt == 0) && itemNum == 0;
                    if (isFit) {
                        a.put("cnt", cnt);
                        a.put("itemNum", itemNum);
                        maps.add(a);
                    }
                }
                //?????????
                if ("2".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream().filter(p -> MapUtils.getString(a, "pkExocc")
                                .equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    boolean isFit = cnt == 0 && itemNum > 0;
                    if (isFit) {
                        a.put("cnt", cnt);
                        a.put("itemNum", itemNum);
                        maps.add(a);
                    }
                }
            });
            return maps;
        }
        return list;
    }

    /**
     * @Description sqlServer??????????????????????????????2100???
     * @auther wuqiang
     * @Date 2021-04-26
     * @Param [pkExoccs]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    private List<Map<String, Object>> getMedBlIpdtInfo(List<String> pkExoccs){
        List<Map<String, Object>> list1=new ArrayList<>(pkExoccs.size()*2);
       String sql="select nvl(sum(dt.AMOUNT) ,0) cnt, " +
               "        dt.pk_ordexdt, " +
               "        dt.pk_dept_ex, " +
               "        count(1) as item_num " +
               "        from bl_ip_dt dt " +
               "        where  " +
               "            dt.pk_ordexdt in  (" + CommonUtils.convertSetToSqlInPart(new HashSet<>(pkExoccs), "pk_ordexdt")+ ")"+
               "        group by dt.pk_ordexdt, dt.pk_dept_ex";
        list1=DataBaseHelper.queryForList(sql);
        return list1;
    }

    /**
     * ??????????????????-????????????-?????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public PageMedListVo queryOpMedicalApp(String param, IUser user) {
        PageMedListVo pageMedListVo = new PageMedListVo();
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        }
        Date dateBegin =DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateBegin")));
        Date dateEnd =DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateEnd")));
        int day = DateUtils.getDateSpace(dateBegin,dateEnd);
        if(day>31){
            throw new BusException("?????????????????????????????????31???!");
        }
        int pageIndex = CommonUtils.getInteger(CommonUtils.getString(paramMap.get("pageIndex")));
        int pageSize = CommonUtils.getInteger(CommonUtils.getString(paramMap.get("pageSize")));
        // ????????????
        String pkOrg = UserContext.getUser().getPkOrg();
        paramMap.put("pkOrg",pkOrg);
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> list = blMedicalExe2Mapper.qryOpMedAppInfo(paramMap);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        pageMedListVo.setMedList(list);
        pageMedListVo.setTotalCount(page.getTotalCount());
        return pageMedListVo;
    }

    /**
     * ??????????????????-????????????-?????????????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryIpBlDtAndOcc(String param, IUser user) {
        // Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> paramMaps = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        // //??????????????????
        // Double totalAmount =
        // DataBaseHelper.queryForScalar("select case when sum(cg.amount) is null then 0 else sum(cg.amount) end totalAmount  from bl_ip_dt cg where cg.pk_cnord = ? and cg.pk_ordexdt = ? ",
        // Double.class, paramMap.get("pkCnord"),paramMap.get("pkExocc"));
        // ?????????????????????????????????
        List<String> pkExoccList = Lists.newArrayList();// ???????????????????????????
        List<String> pkCnordList = Lists.newArrayList();// ??????????????????
        String pkCnordFather = null; // ???????????????
        String pkExoccFather = null; // ????????????????????????
        for (Map<String, Object> temp : paramMaps) {
            pkExoccList.add(temp.get("pkExocc").toString());
            pkCnordList.add(temp.get("pkCnord").toString());
            if ("0".equals(temp.get("isFather").toString())) {// ?????????
                pkCnordFather = temp.get("pkCnord").toString();
                pkExoccFather = temp.get("pkExocc").toString();
            }
        }
        Double totalAmount = blMedicalExe2Mapper.getTotalAmount(pkExoccList);
        List<Map<String, Object>> dtlist = null;
        if (totalAmount <= 0D) {
            // ?????????????????????????????????
            // dtlist = blMedicalExe2Mapper.qryIpMedBlDtCharge(paramMap);
            // ???????????????????????????????????????
            dtlist = blMedicalExe2Mapper.qryIpMedBlDtCharge_refactor(pkCnordList);
        } else {
            // ?????????????????????????????????
            // dtlist = blMedicalExe2Mapper.qryIpMedBlDtInfo(paramMap);
            // ????????????????????????????????? ??????
            Map<String, Object> pkListMap = Maps.newHashMap();
            // ?????????????????????
            pkListMap.put("pkCnordList", pkCnordList);
            pkListMap.put("pkExoccList", pkExoccList);
            dtlist = blMedicalExe2Mapper.qryIpMedBlDtInfoRefactors(pkListMap);
            //?????????????????????????????????????????????
            if (dtlist != null) {
                dtlist.addAll(blMedicalExe2Mapper.qryIpMedBlDtPartialRefundInfo(pkListMap));
            } else {
                dtlist = blMedicalExe2Mapper.qryIpMedBlDtPartialRefundInfo(pkListMap);
            }
        }
        // ?????????????????????
        // List<Map<String, Object>> occlist =
        // blMedicalExe2Mapper.queryExAssistOccList(paramMap);
        // ?????????????????????????????????
        if (pkCnordFather == null || pkExoccFather == null) {
            throw new BusException("???????????????????????????????????????????????????????????????????????????\n????????????????????????????????????????????????????????????????????????????????????");
        }
        List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList_refactor(pkCnordFather, pkExoccFather);
        for (Map<String, Object> map : occlist) {
            if (map.get("pkMsp") != null) {
                map.put("pkMsp", map.get("pkMsp").toString().trim());
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // ??????????????????
        result.put("dtlist", dtlist);
        // ??????????????????
        result.put("occlist", occlist);
        return result;
    }

    /**
     * ??????????????????-????????????-???????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryOpBlDtAndOcc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> dtlist = blMedicalExe2Mapper.qryOpMedBlDtInfo(paramMap);
        List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList(paramMap);
        for (Map<String, Object> map : occlist) {
            if (map.get("pkMsp") != null) {
                map.put("pkMsp", map.get("pkMsp").toString().trim());
            }
        }
        // ??????????????????
        result.put("dtlist", dtlist);
        // ??????????????????
        result.put("occlist", occlist);
        return result;
    }

    /**
     * ??????????????????-????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryMedicalApp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        String euPvtype = CommonUtils.getString(paramMap.get("euPvtype"));
        if (!CommonUtils.isEmptyString(euPvtype)) {
            if (EnumerateParameter.THREE.equals(euPvtype)) // ??????
                list = blMedicalExe2Mapper.qryMedAppInfoIp(paramMap);
            else
                // ??????
                list = blMedicalExe2Mapper.qryMedAppInfoOp(paramMap);
        } else {
            list.addAll(blMedicalExe2Mapper.qryMedAppInfoIp(paramMap));
            list.addAll(blMedicalExe2Mapper.qryMedAppInfoOp(paramMap));
        }
        return list;
    }

    /**
     * ??????????????????-???????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpBlOcc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList(paramMap);
        // ??????????????????
        return occlist;
    }

    /**
     * ??????????????????-????????????????????????????????? ??????????????????-?????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void cancleExocc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> updateMap = new HashMap<>();
        // ????????????
        updateMap.put("pkAssOccs", (List<String>) paramMap.get("pkAssOccs"));
        updateMap.put("pkEmpCanc", UserContext.getUser().getPkEmp());
        updateMap.put("nameEmpCanc", UserContext.getUser().getNameEmp());
        updateMap.put("dateCanc", DateUtils.dateToStr("yyyyMMddHHssmm", new Date()));
        blMedicalExe2Mapper.cancleExocc(updateMap);
        // ?????????????????????1???????????? 2019-01-04?????????????????????????????????????????????????????????
        // List<Map<String, Object>> qryMap =
        // DataBaseHelper.queryForList("SELECT a.PK_CNORD,substr(a.CODE_ORDTYPE,0,2) as type FROM CN_ORDER a WHERE a.ORDSN_PARENT IN (SELECT b.ORDSN_PARENT FROM CN_ORDER b WHERE b.PK_CNORD=?)",
        // paramMap.get("pkCnord").toString());
        // blMedicalExe2Mapper.updateApply(qryMap);
        // DataBaseHelper.execute("update cn_ris_apply set eu_status='1' where pk_cnord=? and eu_status='3'",
        // new Object[] { paramMap.get("pkCnord").toString() });
    }

    /**
     * ??????????????????-???????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void cancleCharge(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String PkExocc = (String) paramMap.get("pkExocc");
        Map<String, Object> retMap = DataBaseHelper.queryForMap("select sum(quan) quan from bl_ip_dt where pk_ordexdt=?", PkExocc);
        Double quan = retMap != null ? CommonUtils.getDouble(retMap.get("quan")) : 0D;
        if (quan <= 0)
            throw new BusException("???????????????????????????!");
        // ?????????????????????????????????
        String PkCg = blMedicalExe2Mapper.queryCharge(PkExocc);
        // ???????????????????????????????????????????????????
        List<Map<String, Object>> blIpDt = blMedicalExe2Mapper.queryBlIpDt(PkCg);
        // ?????????????????????
        Object[] args = new Object[4];
        blIpMedicalExeService.retCg(PkExocc, (User) user, args, "1");
        // ????????????????????????
        DataBaseHelper.execute("update ex_order_occ set date_occ= null, pk_emp_occ= null, name_emp_occ= null, pk_cg= null, eu_status='0'  where pk_exocc=? ", PkExocc);
        // ?????????????????????????????????????????????????????????
        DataBaseHelper.execute("delete from ex_assist_occ where pk_exocc=? and flag_occ='0'", PkExocc);
    }

    /**
     * ??????????????????-???????????????????????????,?????????????????????(????????????) 007004002035
     *
     * @param param
     * @param user
     * @return
     */
    public void refunedSingleItem(String param, IUser user) {
        List<Map<String, String>> chargeItems = JsonUtil.readValue(JsonUtil.getJsonNode(param, "dts"), new TypeReference<List<Map<String, String>>>() {
        });
        List<String> pkExoccs = JsonUtil.readValue(JsonUtil.getJsonNode(param, "pkExoccs"), new TypeReference<List<String>>() {
        });
        Set<String> pkPvList = new HashSet<>();
        List<String> PkCgList = Lists.newArrayList();
        for (Map<String, String> item : chargeItems) {
            if (item.get("pkCgip") == null)
                continue;
            PkCgList.add(item.get("pkCgip").toString());
            pkPvList.add(item.get("pkPv").toString());
        }
        if (PkCgList.size() <= 0)
            return;
        List<PvEncounter> pvEncounterList = DataBaseHelper.queryForList(
                "select * from PV_ENCOUNTER where pk_pv in ("
                        + CommonUtils.convertSetToSqlInPart(pkPvList, "pk_pv") + ")",
                PvEncounter.class, new Object[]{});
        for (PvEncounter pv : pvEncounterList) {
            if (!"1".equals(pv.getEuStatus())) {
                throw new BusException("?????????" + pv.getNamePi() + "?????????????????????????????????????????????");
            }
        }
        // ???????????????????????????????????????????????????
        List<BlIpDt> blIpDt = blMedicalExe2Mapper.queryBlIpDt_refactor(PkCgList);
        // ??????????????????
        List<RefundVo> refundVoList = Lists.newArrayList();
        for (BlIpDt temp : blIpDt) {
            RefundVo refundVo = new RefundVo();
            refundVo.setNameEmp(UserContext.getUser().getNameEmp());
            refundVo.setPkCgip(temp.getPkCgip());
            refundVo.setPkOrdexdt(temp.getPkOrdexdt());
            refundVo.setPkDept(UserContext.getUser().getPkDept());
            refundVo.setPkEmp(UserContext.getUser().getPkEmp());
            refundVo.setPkOrg(UserContext.getUser().getPkOrg());
            for (Map<String, String> map : chargeItems) {
                boolean isUseDefQuan = temp.getPkCgip().equals(MapUtils.getString(map, "pkCgip")) &&
                        StringUtils.isNotBlank(MapUtils.getString(map, "quanRet"));
                if (isUseDefQuan) {
                    refundVo.setQuanRe(Double.parseDouble(chargeItems.get(0).get("quanRet")));
                    refundVo.setNoteCg(chargeItems.get(0).get("noteCg"));
                    break;
                }
                refundVo.setQuanRe(temp.getQuan());
                break;
            }
            refundVoList.add(refundVo);
        }
        // ?????????????????????
        ipCgPubService.refundInBatch(refundVoList);
        // ???????????????????????????????????????????????????
        Integer itemCount = blMedicalExe2Mapper.isRefund(pkExoccs);
        // ???????????????????????????????????????
        if (itemCount == null || itemCount <= 0) {
            // ????????????????????????
            blMedicalExe2Mapper.updateExOrdOcc(pkExoccs);
            // ????????????????????????????????????1????????????
            Set<String> pkExSet = new HashSet<String>(pkExoccs);
            List<Map<String, Object>> pkMap = DataBaseHelper.queryForList("select occ.pk_cnord,substr(ord.code_ordtype,0,2) as type from ex_order_occ occ inner join cn_order ord on ord.pk_cnord=occ.pk_cnord where pk_exocc in(" + CommonUtils.convertSetToSqlInPart(pkExSet, "pk_exocc") + ")", new Object[]{});
            for (Map<String, Object> temp : pkMap) {
                if ("02".equals(temp.get("type")))
                    DataBaseHelper.execute("update cn_ris_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
                if ("03".equals(temp.get("type")))
                    DataBaseHelper.execute("update cn_lab_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
                if ("12".equals(temp.get("type")))
                    DataBaseHelper.execute("update cn_trans_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
            }
            // ??????????????????????????????????????????
            blMedicalExe2Mapper.deleteExOrdOccDt(pkExoccs);
            blMedicalExe2Mapper.deleteExOrdOcc(pkExoccs);
        }
        //???????????????????????????????????????
        Map<String, Object> paramListMap = new HashMap<String, Object>();
        paramListMap.put("dtlist", chargeItems);
        paramListMap.put("type", "I");
        paramListMap.put("Control", "CR");
        Set<String> pkExSet = new HashSet<String>(pkExoccs);
        List<Map<String, Object>> pkMap = DataBaseHelper.queryForList("select occ.pk_cnord,substr(ord.code_ordtype,0,2) as type from ex_order_occ occ inner join cn_order ord on ord.pk_cnord=occ.pk_cnord where pk_exocc in(" + CommonUtils.convertSetToSqlInPart(pkExSet, "pk_exocc") + ")", new Object[]{});
        List<String> pkCnordList = new ArrayList<String>();
        for (Map<String, Object> temp : pkMap) {
            pkCnordList.add(temp.get("pkCnord").toString());
        }
        paramListMap.put("pkCnords", pkCnordList);
        PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
        paramListMap = null;
    }

    /**
     * ??????????????????-???????????????????????????,????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void cancleCharge_refactor(String param, IUser user) {
        List<String> pkExoccList = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        // ???????????????????????????????????????????????????
        Integer quan = blMedicalExe2Mapper.isRefund(pkExoccList);
        if (quan == null || quan <= 0)
            throw new BusException("???????????????????????????!");
        // ?????????????????????????????????
        List<String> PkCgList = blMedicalExe2Mapper.queryCharge_refactor(pkExoccList);
        // ???????????????????????????????????????????????????
        List<BlIpDt> blIpDt = blMedicalExe2Mapper.queryBlIpDt_refactor(PkCgList);
        // ??????????????????
        List<RefundVo> refundVoList = Lists.newArrayList();
        for (BlIpDt temp : blIpDt) {
            RefundVo refundVo = new RefundVo();
            refundVo.setNameEmp(UserContext.getUser().getNameEmp());
            refundVo.setPkCgip(temp.getPkCgip());
            refundVo.setPkDept(UserContext.getUser().getPkDept());
            refundVo.setPkEmp(UserContext.getUser().getPkEmp());
            refundVo.setPkOrg(UserContext.getUser().getPkOrg());
            refundVo.setQuanRe(temp.getQuan());
            refundVoList.add(refundVo);
        }
        // ?????????????????????
        ipCgPubService.refundInBatch(refundVoList);
        // ????????????????????????
        blMedicalExe2Mapper.updateExOrdOcc(pkExoccList);
        // ????????????????????????????????????1????????????
        Set<String> pkExSet = new HashSet<String>(pkExoccList);
        List<Map<String, Object>> pkMap = DataBaseHelper.queryForList("select occ.pk_cnord,substr(ord.code_ordtype,0,2) as type from ex_order_occ occ inner join cn_order ord on ord.pk_cnord=occ.pk_cnord where pk_exocc in(" + CommonUtils.convertSetToSqlInPart(pkExSet, "pk_exocc") + ")", new Object[]{});
        for (Map<String, Object> temp : pkMap) {
            if ("02".equals(temp.get("type")))
                DataBaseHelper.execute("update cn_ris_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
            if ("03".equals(temp.get("type")))
                DataBaseHelper.execute("update cn_lab_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
            if ("12".equals(temp.get("type")))
                DataBaseHelper.execute("update cn_trans_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
        }
        // ?????????????????????????????????????????????????????????
        blMedicalExe2Mapper.deleteExOrdOccDt(pkExoccList);
        blMedicalExe2Mapper.deleteExOrdOcc(pkExoccList);
    }

    /**
     * ??????????????????-??????-????????????:????????????
     *
     * @param param ????????????
     * @param user
     * @return list -???????????????
     */
    public List<BlOpDt> saveDtList(String param, IUser user) {
        // ???????????????????????????????????????
        List<BlOpDt> dtlist = JsonUtil.readValue(param, new TypeReference<List<BlOpDt>>() {
        });
        if (dtlist == null || dtlist.size() <= 0)
            return null;
        List<BlPubParamVo> params = new ArrayList<BlPubParamVo>();
        List<BlOpDt> rtnlist = new ArrayList<BlOpDt>();
        //???????????????set?????? ????????????
        for (BlOpDt dt : dtlist) {
            if (CommonUtils.isEmptyString(dt.getPkCgop())) {
                params.add(constructBlParam(dt));
            } else if ("1".equals(dt.getEuAdditem()) && !CommonUtils.isEmptyString(dt.getPkCgop())) {
                // ??????????????????????????????????????????,??????????????????(?????????????????????????????????????????????)
                DataBaseHelper.execute("delete from bl_op_dt where pk_cgop = ?", dt.getPkCgop());
                params.add(constructBlParam(dt));
            } else if ((dt.getEuAdditem() == null || "0".equals(dt.getEuAdditem())) && !CommonUtils.isEmptyString(dt.getPkCgop())) {
                // ??????????????????????????????????????????????????????
                rtnlist.add(dt);
            }
        }
        // ????????????????????????
        if (params.size() > 0) {
            BlPubReturnVo rtnvo = opCgPubService.blOpCgBatch(params);
            if (rtnvo != null && rtnvo.getBods() != null && rtnvo.getBods().size() > 0)
                rtnlist.addAll(rtnvo.getBods());
        }
        return rtnlist;
    }

    private BlPubParamVo constructBlParam(BlOpDt dt) {
        // ??????????????????????????????
        BlPubParamVo dtparam = new BlPubParamVo();
        dtparam.setBatchNo(dt.getBatchNo());
        dtparam.setDateExpire(dt.getDateExpire());
        dtparam.setDateHap(new Date());
        dtparam.setEuAdditem("1");
        dtparam.setEuPvType("1");// ????????????????????????
        dtparam.setFlagPd(dt.getFlagPd());
        dtparam.setFlagPv("0");// ????????????
        dtparam.setNameEmpApp(dt.getNameEmpApp());// ???????????????????????????????????????????????????????????????
        dtparam.setNameEmpCg(dt.getNameEmpCg());
        dtparam.setPkOrgApp(dt.getPkOrgApp());
        dtparam.setPkEmpApp(dt.getPkEmpApp());
        dtparam.setPkEmpCg(dt.getPkEmpCg());
        dtparam.setNamePd(dt.getNameCg());
        dtparam.setPackSize(dt.getPackSize());
        dtparam.setPkCnord(dt.getPkCnord());
        dtparam.setPkDeptApp(dt.getPkDeptApp());
        dtparam.setPkDeptCg(dt.getPkDeptCg());
        dtparam.setPkDeptEx(dt.getPkDeptEx());
        dtparam.setPkEmpApp(dt.getPkEmpApp());
        dtparam.setPkEmpCg(dt.getPkEmpCg());
        if (BlcgUtil.converToTrueOrFalse(dt.getFlagPd())) // ??????
            dtparam.setPkOrd(dt.getPkItem());
        dtparam.setPkItem(dt.getPkItem());
        dtparam.setPkOrg(dt.getPkOrg());
        dtparam.setPkOrgApp(dt.getPkOrgApp());
        dtparam.setPkOrgEx(dt.getPkOrgEx());
        dtparam.setPkPi(dt.getPkPi());
        dtparam.setPkPres(dt.getPkPres());
        dtparam.setPkPv(dt.getPkPv());
        dtparam.setPkUnitPd(dt.getPkUnitPd());
        dtparam.setPrice(dt.getPrice());
        dtparam.setPriceCost(dt.getPriceCost());
        dtparam.setQuanCg(dt.getQuanCg());
        dtparam.setSpec(dt.getSpec());
        dtparam.setFlagHasPdPrice("1");
        dtparam.setPkDeptAreaapp(dt.getPkDeptAreaapp());
        dtparam.setPkDeptJob(dt.getPkDeptJob());
        return dtparam;
    }

    /**
     * ??????????????????-???????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> excList(String param, IUser user) {
        List<Map<String, Object>> paramMapList = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        for (Map<String, Object> paramMap : paramMapList) {
            paramMap.put("dateOcc", DateUtils.getDateTime());
            paramMap.put("pkEmpOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkEmpOcc")) ? MapUtils.getString(paramMap, "pkEmpOcc") : UserContext.getUser().getPkEmp());
            paramMap.put("nameEmpOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "nameEmpOcc")) ? MapUtils.getString(paramMap, "nameEmpOcc") : UserContext.getUser().getNameEmp());
            paramMap.put("pkOrgOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkOrgOcc")) ? MapUtils.getString(paramMap, "pkOrgOcc") : UserContext.getUser().getPkOrg());
            paramMap.put("pkDeptOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkDeptOcc")) ? MapUtils.getString(paramMap, "pkDeptOcc") : UserContext.getUser().getPkDept());
            // ????????????
            if (Integer.parseInt(paramMap.get("euPvtype").toString()) == 1) {
                // ????????????
                int count = blMedicalExe2Mapper.opDataCheck(paramMap.get("pkCnord").toString());
                if (count > 0) {
                    throw new BusException("??????????????????????????????!");
                }
            }
            if (Integer.parseInt(paramMap.get("euPvtype").toString()) == 3) {
                // ?????????
                List<Map<String, Object>> ipData = blMedicalExe2Mapper.ipDataCheck(paramMap.get("pkCnord").toString());
                for (Map<String, Object> map : ipData) {
                    if (map.get("pkCgip") == null) {
                        throw new BusException("????????????????????????????????????");
                    }
                }
            }
            // ????????????
            try {
                blMedicalExe2Mapper.medExeOcc(paramMap);
            } catch (Exception e) {
                throw new BusException("???????????????");
            }
            // ???????????????????????????????????????????????????????????????????????????????????????
            List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList(paramMap);
            for (Map<String, Object> map : occlist) {
                if (map.get("pkMsp") != null) {
                    map.put("pkMsp", map.get("pkMsp").toString().trim());
                }
                if (map.get("dateAppt") != null) {
                    // ???????????????????????????????????????(?????????????????????)????????????????????????????????????????????????
                    DataBaseHelper.execute("update sch_appt  set eu_status='1' where pk_schappt in(select pk_schappt from sch_appt_ord where pk_assocc=?) and eu_status='0'", map.get("pkAssocc"));
                    // ??????????????????????????????????????????
                    DataBaseHelper.execute("update sch_appt_ord set flag_exec='1' where pk_assocc=? and flag_exec='0'", map.get("pkAssocc"));
                }
            }
            // ?????????????????????3????????????
            DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            //???????????????????????????????????????
            Map<String, Object> paramListMap = new HashMap<String, Object>();
            paramListMap.put("dtlist", paramMapList);
            paramListMap.put("type", "I");
            paramListMap.put("Control", "OK");
            PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
            paramListMap = null;
            // ??????????????????????????????????????????
            return occlist;
        }
        return null;
    }
    // /???????????????????????? ,?????????????????????????????????????????????????????????????????????????????????

    /**
     * ????????????
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> savePatiCgInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<BlPubParamVo> dtlist = (List<BlPubParamVo>) paramMap.get("dtlist");
        String pkCnord = paramMap.get("pkCnord").toString();
        CnOrder cnord = DataBaseHelper.queryForBean("select pk_org,pk_ord from cn_order where pk_cnord = ?", CnOrder.class, pkCnord);
        String pkExocc = "";
        if (paramMap.get("pkExocc") != null) {
            pkExocc = paramMap.get("pkExocc").toString();
        }
        String dtListStr = JsonUtil.writeValueAsString(dtlist);
        dtlist = JsonUtil.readValue(dtListStr, new TypeReference<List<BlPubParamVo>>() {
        });
        for (BlPubParamVo blPubParamVo : dtlist) {
            blPubParamVo.setDateHap(new Date());
            // ????????????????????????????????????
            blPubParamVo.setPkOrgApp(cnord.getPkOrg());
            blPubParamVo.setPkDeptEx(UserContext.getUser().getPkDept());
            // ???????????????
            blPubParamVo.setPkOrdexdt(pkExocc);
            blPubParamVo.setPkCnord(pkCnord);
            // blPubParamVo.setPkOrd(cnord.getPkOrd());
            if (BlcgUtil.converToTrueOrFalse(blPubParamVo.getFlagPd()))
                blPubParamVo.setPkOrd(blPubParamVo.getPkItem());
        }
        if (dtlist == null || dtlist.size() <= 0)
            throw new BusException("???????????????????????????!");
        BlPubReturnVo chargeIpBatch = new BlPubReturnVo();
        if (paramMap.get("chargeCnt").toString().equals("?????????"))// ???????????????:?????????????????????
            chargeIpBatch = ipCgPubService.chargeIpBatch(dtlist, false);
        else
            // ???????????????:?????????
            chargeIpBatch = ipCgPubService.chargeIpBatch(dtlist, false);
        List<BlIpDt> bids = chargeIpBatch.getBids();
        ExAssistOcc exAssistOcc = new ExAssistOcc();
        Date date = new Date();
        for (BlIpDt blIpDt : bids) {
            // ??????????????????????????????ex_order_occ new Date() date_occ=?,
            DataBaseHelper.execute("update ex_order_occ set date_occ=?, pk_emp_occ=?, name_emp_occ=?, pk_cg=?, eu_status='1',pk_org_occ=?, pk_dept_occ=?  where pk_exocc=? ", new Date(), ((User) user).getPkEmp(), ((User) user).getNameEmp(), blIpDt.getPkCgip(), ((User) user).getPkOrg(), ((User) user).getPkDept(), pkExocc);
            // 2018-10-15??????????????????????????????????????????????????????
            // ????????????????????????
            // DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord=? and  eu_status<'3' ",
            // pkCnord);
            // ????????????
            DataBaseHelper.execute("update cn_order set pk_org_exec=?, pk_dept_exec=? where pk_cnord=? ", ((User) user).getPkOrg(), ((User) user).getPkDept(), pkCnord);
        }
        int count = DataBaseHelper.queryForScalar("select count(1) from ex_assist_occ where pk_exocc=?", Integer.class, pkExocc);
        // ???????????????????????????????????????????????????????????????????????????
        List<ExAssistOcc> exAssistOccList = Lists.newArrayList();
        if (count <= 0) {
            // ????????????????????????????????????????????????(?????????????????????????????????)
            // ???????????????????????????
            CnOrder cnOrder = DataBaseHelper.queryForBean("select quan from cn_order where pk_cnord=? ", CnOrder.class, pkCnord);
            int len = (cnOrder.getQuan()).intValue();
            for (int i = 0; i < len; i++) {
                exAssistOcc = constructExAssistOcc(bids.get(0), pkExocc, user);
                DataBaseHelper.insertBean(exAssistOcc);
                if ("03".equals(cnOrder.getCodeOrdtype()))// ????????????????????????????????????
                    exAssistOccList.add(exAssistOcc);
            }
        }
        List<Map<String, Object>> dtChargelist = blMedicalExe2Mapper.qryIpMedBlDtInfo(paramMap);
        // ????????????????????????
        // ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx",
        // pkExocc);
        return dtChargelist;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> savePatiCgInfo_refactor(String param, IUser user) {
        List<Map<String, Object>> paramMapList = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {});
        // ????????????????????????????????????????????????
        List<BlPubParamVo> dtAllList = Lists.newArrayList();
        Map<String, Object> pExOrderOcc = null;// ???????????????????????????????????????
        List<ExAssistOccDt> exAssistOccDtList = Lists.newArrayList();// ?????????????????????
        List<String> pkExoccList = Lists.newArrayList();// ???????????????????????????
        List<String> pkCnordList = Lists.newArrayList();// ??????????????????
        Set<String> pkPvList = new HashSet<>();
        //???????????????????????????
        String dateTs = null;

        //???true??????????????????????????????????????????????????????????????????????????????
        boolean bFlagSpec = false;
        // ??????????????????????????????????????????
        for (Map<String, Object> occAndDt : paramMapList) {
            // ???????????????
            Map<String, Object> exOrderOcc = (Map<String, Object>) occAndDt.get("exOrderOcc");
            if(!bFlagSpec && occAndDt.containsKey("flagSpec"))
            {
            	String flagSpec = (String)occAndDt.get("flagSpec");
            	if("1".equals(flagSpec))
            	{
            		bFlagSpec = true;
            	}
            }
            pkExoccList.add(exOrderOcc.get("pkExocc").toString());
            pkCnordList.add(exOrderOcc.get("pkCnord").toString());
            pkPvList.add(exOrderOcc.get("pkPv").toString());
            if (exOrderOcc.containsKey("ts")){
                dateTs=DateUtils.getDateTimeStr( DateUtils.strToDate(exOrderOcc.get("ts").toString()));
            }
            if (exOrderOcc.get("ordsnParent").toString().equals(exOrderOcc.get("ordsn").toString()))
                pExOrderOcc = exOrderOcc;
            // ????????????
            List<BlPubParamVo> dtlist = (List<BlPubParamVo>) occAndDt.get("dtlist");
            if (dtlist == null || dtlist.size() <= 0)
                continue;
            // ?????????????????????list?????????Map???????????????
            String dtListStr = JsonUtil.writeValueAsString(dtlist);
            dtlist = JsonUtil.readValue(dtListStr, new TypeReference<List<BlPubParamVo>>() {
            });
            //??????????????????pkOrd?????????????????????pkOrd????????????????????????????????????????????????????????????????????????????????? 2020.05.18
            List<BlPubParamVo> newDtList = new ArrayList<BlPubParamVo>();
            boolean flagAdd = false;
            // ??????????????????
            for (BlPubParamVo blPubParamVo : dtlist) {
                if (blPubParamVo.getDateHap() == null) {
                    blPubParamVo.setDateHap(new Date());
                }
                // ?????????????????????????????????????????????????????????????????????
                blPubParamVo.setPkOrgApp(blPubParamVo.getPkOrg());
                blPubParamVo.setPkDeptEx(blPubParamVo.getPkDeptEx());
                blPubParamVo.setPkDeptNsApp(exOrderOcc.get("pkDeptNs").toString());
                blPubParamVo.setEuBltype("2");
                blPubParamVo.setPkDeptCg(UserContext.getUser().getPkDept());
                // ????????????????????????????????????
                blPubParamVo.setPkOrdexdt(exOrderOcc.get("pkExocc").toString());
                // ???????????????????????????
                blPubParamVo.setOrdsnParent(Integer.parseInt(exOrderOcc.get("ordsnParent").toString()));
                blPubParamVo.setOrdsn(Integer.parseInt(exOrderOcc.get("ordsn").toString()));
                // ?????????????????????????????????
                blPubParamVo.setDateStart(DateUtils.strToDate(exOrderOcc.get("dateStart").toString()));
                blPubParamVo.setCodeOrdtype(exOrderOcc.get("codeOrdtype").toString());
                // ????????????????????????????????????????????????????????????????????????????????????????????????
                if ("1".equals(blPubParamVo.getIsNewItem())) {
                    blPubParamVo.setPkOrdexdt(pExOrderOcc.get("pkExocc").toString());
                    blPubParamVo.setPkDeptApp(pExOrderOcc.get("pkDeptApp").toString());
                }
                //pkOrd???????????????????????????????????????
                if (CommonUtils.isNotNull(blPubParamVo.getPkOrd()) && "0".equals(blPubParamVo.getIsNewItem())) {
                    if (!flagAdd) {
                        // ????????????????????????????????????pk_pd??????pkOrd???
                        if (BlcgUtil.converToTrueOrFalse(blPubParamVo.getFlagPd())) {
                            blPubParamVo.setPkOrd(blPubParamVo.getPkItem());
                        }
                        //????????????????????????
                        blPubParamVo.setPkItem(null);
                        blPubParamVo.setQuanCg(1.0D);
                        newDtList.add(blPubParamVo);
                        flagAdd = true;
                    }
                } else {
                    if (BlcgUtil.converToTrueOrFalse(blPubParamVo.getFlagPd())) {
                        blPubParamVo.setPkOrd(blPubParamVo.getPkItem());
                    }
                    newDtList.add(blPubParamVo);
                }
            }
            if (newDtList != null && newDtList.size() > 0) {
                dtAllList.addAll(newDtList);
            }
            // ?????????????????????????????????
            ExAssistOccDt exAssistOccDt = new ExAssistOccDt();
            if (pExOrderOcc != null) {
                exAssistOccDt.setFlagMaj("1"); // ???????????????
            } else {
                exAssistOccDt.setFlagMaj("0");
            }
            exAssistOccDt.setPkCnord(exOrderOcc.get("pkCnord").toString());
            exAssistOccDt.setPkExocc(exOrderOcc.get("pkExocc").toString());
            exAssistOccDt.setPkOrd(exOrderOcc.get("pkOrd").toString());
            exAssistOccDtList.add(exAssistOccDt);
        }
        List<PvEncounter> pvEncounterList = DataBaseHelper.queryForList(
                "select * from PV_ENCOUNTER where pk_pv in ("
                        + CommonUtils.convertSetToSqlInPart(pkPvList, "pk_pv") + ")",
                PvEncounter.class, new Object[]{});
        for (PvEncounter pv : pvEncounterList) {
            if (!"1".equals(pv.getEuStatus())) {
                throw new BusException("?????????" + pv.getNamePi() + "?????????????????????????????????????????????");
            }
        }
        if (pExOrderOcc != null && pExOrderOcc.get("pkOrdris") != null && !"".equals(pExOrderOcc.get("pkOrdris").toString())) {
            Integer risCount = DataBaseHelper.queryForScalar("select count(1) from cn_ris_apply where pk_ordris=? and eu_status='0' ", Integer.class, pExOrderOcc.get("pkOrdris").toString());
            if (risCount != null && risCount > 0) {
                throw new BusException("??????????????????????????????????????????");
            }
        }
        if (StringUtils.isNotBlank(dateTs)){
            Integer count = DataBaseHelper.queryForScalar("select count(1) as count from ex_order_occ where pk_exocc=? and to_char(ts,'YYYYMMDDHH24MISS')='" + dateTs + "'", Integer.class, pExOrderOcc.get("pkExocc").toString());
            if (count <= 0)
                throw new BusException("???????????????????????????????????????????????????????????????");
        }
        if (pExOrderOcc != null && pExOrderOcc.get("pkExocc") != null && !"".equals(pExOrderOcc.get("pkExocc").toString())) {
            Integer count = DataBaseHelper.queryForScalar("select count(1) as count from ex_order_occ where pk_exocc=?", Integer.class, pExOrderOcc.get("pkExocc").toString());
            if (count <= 0)
                throw new BusException("??????????????????????????????????????????");
        } else {
            throw new BusException("???????????????????????????????????????????????????????????????????????????????????????????????????????????????");
        }
        // ???????????????????????????????????????
        BlPubReturnVo chargeIpBatch = ipCgPubService.chargeIpBatch(dtAllList, false);
        // ??????????????????
        List<BlIpDt> bids = chargeIpBatch.getBids();
        if (bids == null || bids.size() <= 0) {
            throw new BusException("??????????????????????????????????????????????????????????????????????????????");
        }
        // ??????????????????????????????ex_order_occ
        Map<String, Object> updateOccParam = Maps.newHashMap();
        updateOccParam.put("dateOcc", pExOrderOcc.get("dateOcc"));
        updateOccParam.put("pkEmpOcc", ((User) user).getPkEmp());
        updateOccParam.put("nameEmpOcc", ((User) user).getNameEmp());
        updateOccParam.put("pkOrgOcc", ((User) user).getPkOrg());
        updateOccParam.put("pkDeptOcc", ((User) user).getPkDept());
        updateOccParam.put("pkExoccList", pkExoccList);
        if(bFlagSpec)
        {
        	blMedicalExe2Mapper.updateOrdOccSpec(updateOccParam); 
        }
        else
        {
            blMedicalExe2Mapper.updateOrdOcc(updateOccParam);        	
        }
        // ???????????? cn_order
        Map<String, Object> updateOrderParam = Maps.newHashMap();
        updateOrderParam.put("pkOrgExec", ((User) user).getPkOrg());
        updateOrderParam.put("pkDeptExec", ((User) user).getPkDept());
        updateOrderParam.put("pkCnordList", pkCnordList);
        blMedicalExe2Mapper.updateCnOrder(updateOrderParam);
        // ????????????????????????????????????
        if (pExOrderOcc.get("codeOrdtype") != null) {
            String codeOrdtype = pExOrderOcc.get("codeOrdtype").toString().substring(0, 2);
            Set<String> pkCnordSet = new HashSet<String>(pkCnordList);
            switch (codeOrdtype) {
                case "02":
                    DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
                    break;
                case "03":
                    DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
                    break;
                case "12":
                    DataBaseHelper.execute("update cn_trans_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
                    break;
                default:
                    break;
            }
        }
        // ????????????????????????????????????????????????????????????0????????????????????????0????????????????????????
        int count = blMedicalExe2Mapper.isCharging(pkExoccList);
        if (count <= 0) {
            // ??????????????????????????????
            ExAssistOcc exAssistocc = new ExAssistOcc();
            exAssistocc.setPkOrg(((User) user).getPkOrg());
            exAssistocc.setPkCnord(pExOrderOcc.get("pkCnord").toString());
            exAssistocc.setPkPv(pExOrderOcc.get("pkPv").toString());
            exAssistocc.setPkPi(pExOrderOcc.get("pkPi").toString());
            exAssistocc.setEuPvtype("3");
            // ????????????
            exAssistocc.setCodeOcc(ApplicationUtils.getCode("0503"));
            exAssistocc.setPkEmpOrd(((User) user).getPkEmp());// ??????????????????
            exAssistocc.setNameEmpOrd(((User) user).getNameEmp());// ??????????????????
            exAssistocc.setPkDept(((User) user).getPkDept());
            exAssistocc.setDateOrd(new Date());
            exAssistocc.setQuanOcc(Double.parseDouble(pExOrderOcc.get("quan").toString()));
            exAssistocc.setTimesOcc(1);
            exAssistocc.setTimesTotal(1);
            exAssistocc.setPkOrgOcc(pExOrderOcc.get("pkOrgOcc").toString());
            exAssistocc.setPkDeptOcc(pExOrderOcc.get("pkDeptOcc").toString());
            exAssistocc.setFlagOcc("0");
            exAssistocc.setFlagCanc("0");
            exAssistocc.setPkExocc(pExOrderOcc.get("pkExocc").toString());
            exAssistocc.setInfantNo("0");
            exAssistocc.setEuStatus("0");
            exAssistocc.setFlagPrt("0");
            exAssistocc.setFlagRefund("0");
            DataBaseHelper.insertBean(exAssistocc);
            // ?????????????????????????????????
            for (ExAssistOccDt temp : exAssistOccDtList) {
                temp.setPkAssocc(exAssistocc.getPkAssocc());
                temp.setPkAssoccdt(NHISUUID.getKeyId());
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOccDt.class), exAssistOccDtList);
        }
        Map<String, Object> pkListMap = Maps.newHashMap();
        pkListMap.put("pkExoccList", pkExoccList);
        pkListMap.put("pkCnordList", pkCnordList);
        List<Map<String, Object>> dtChargelist = blMedicalExe2Mapper.qryIpMedBlDtInfo_refactor(pkListMap);
        if (dtChargelist == null || dtChargelist.size() <= 0) {
            throw new BusException("???????????????????????????????????????");
        }
        // ????????????????????????
        if (pExOrderOcc != null) {
            List<String> PkExoccsFather = Lists.newArrayList();
            PkExoccsFather.add(pExOrderOcc.get("pkExocc").toString());
            ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx", pkExoccList);
        }
        //???????????????????????????????????????
        Map<String, Object> paramListMap = new HashMap<String, Object>();
        paramListMap.put("dtlist", paramMapList);
        paramListMap.put("type", "I");
        paramListMap.put("Control", "OK");
        paramListMap.put("pkCnords", pkCnordList);
        PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
        paramListMap = null;
        return dtChargelist;
    }

    // ????????????ex_assist_occ???????????????
    private ExAssistOcc constructExAssistOcc(BlIpDt blIpDt, String pkExocc, IUser user) {
        // ???????????????????????????
        ExAssistOcc exAssietocc = new ExAssistOcc();
        exAssietocc.setPkOrg(blIpDt.getPkOrg());
        exAssietocc.setPkCnord(blIpDt.getPkCnord());
        exAssietocc.setPkPv(blIpDt.getPkPv());
        exAssietocc.setPkPi(blIpDt.getPkPi());
        exAssietocc.setEuPvtype("3");
        // ????????????
        exAssietocc.setCodeOcc(ApplicationUtils.getCode("0503"));
        exAssietocc.setPkEmpOrd(((User) user).getPkEmp());// ??????????????????
        exAssietocc.setNameEmpOrd(((User) user).getNameEmp());// ??????????????????
        exAssietocc.setPkDept(blIpDt.getPkDeptApp());
        exAssietocc.setDateOrd(new Date());
        exAssietocc.setQuanOcc(blIpDt.getQuan());
        exAssietocc.setTimesOcc(1);
        exAssietocc.setTimesTotal(1);
        exAssietocc.setPkOrgOcc(blIpDt.getPkOrgEx());
        exAssietocc.setPkDeptOcc(blIpDt.getPkDeptEx());
        exAssietocc.setFlagOcc("0");
        exAssietocc.setFlagCanc("0");
        exAssietocc.setPkExocc(pkExocc);
        exAssietocc.setInfantNo("0");
        exAssietocc.setEuStatus("0");
        exAssietocc.setFlagPrt("0");
        exAssietocc.setFlagRefund("0");
        return exAssietocc;
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void deleteOcc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        DataBaseHelper.execute("delete from bl_op_dt where pk_cgop = ?", paramMap.get("pkCgop"));
    }

    /**
     * ????????????007004001014 ????????????pk????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryTotalAmount(String param, IUser user) {
        String pkCnord = JsonUtil.getFieldValue(param, "pkCnord");
        Map<String, Object> amtInfo = DataBaseHelper.queryForMap("select sum(amount) amount,sum(amount_pi) amountPi,sum(amount-amount_pi) amountHppi from bl_op_dt where flag_settle='0' and pk_cnord = ?", pkCnord);
        return amtInfo;
    }

    /**
     * ????????????007004001015 ?????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Integer qryOpIsCharge(String param, IUser user) {
        String pkCnord = JsonUtil.getFieldValue(param, "pkCnord");
        Integer count = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord=? and flag_settle='0'", Integer.class, pkCnord);
        return count;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qrycgSet(String param, IUser user) {
        String pkBlCgset = JsonUtil.getFieldValue(param, "pkBlCgset");
        List<Map<String, Object>> cgList = blMedicalExe2Mapper.qrycgSet(pkBlCgset);
        return cgList;
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<RisPrintInfoVo> qryRisPrintInfo(String param, IUser user) {
        List<String> pkPvs = JsonUtil.readValue(JsonUtil.getJsonNode(param, "pkOrdris"), new TypeReference<List<String>>() {
        });
        String pkDeptExec = JsonUtil.getFieldValue(param, "pkDeptExec");
        List<RisPrintInfoVo> printInfo = blMedicalExe2Mapper.qryRisPrintInfo(pkPvs, pkDeptExec);
        return printInfo;
    }

    /**
     * ???????????????????????????????????????????????????????????????
     * 007004002048
     *
     * @param param
     * @param user
     * @return
     */
    public List<RisPrintInfoVo> qryAppPrtByPkPv(String param, IUser user) {
        List<String> pkPvs = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        Map<String, Object> qryPam = Maps.newHashMap();
        qryPam.put("pkPvs", pkPvs);
        qryPam.put("pkDept", UserContext.getUser().getPkDept());
        List<RisPrintInfoVo> risPrtInfos = blMedicalExe2Mapper.qryAppPrtByPkPv(qryPam);
        return risPrtInfos;
    }

    /**
     * ????????????????????????????????? 007004002036
     *
     * @param param
     * @param user
     */
    public void updateRisPrtFlag(String param, IUser user) {
        List<String> pkOrdriss = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        Set<String> PkOrdrisSet = new HashSet<String>(pkOrdriss);
        DataBaseHelper.update("update cn_ris_apply set flag_print2='1' where pk_ordris in (" + CommonUtils.convertSetToSqlInPart(PkOrdrisSet, "pk_ordris") + ")", new Object[]{});
    }

    /**
     * ??????????????????????????? 007004002037
     *
     * @return
     */
    public List<Map<String, Object>> qryUsuItem(String param, IUser user) {
        Map<String, Object> qryParam = Maps.newHashMap();
        qryParam.put("dateBegin", DateUtils.addDate(new Date(),-1,2,"yyyy-MM-dd HH:mm:ss"));
        qryParam.put("dateEnd", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        qryParam.put("pkDeptEx", UserContext.getUser().getPkDept());
        List<Map<String, Object>> usuItem = blMedicalExe2Mapper.qryUsuItem(qryParam);
        return usuItem;
    }

    /**
     * ???????????????????????? 007004002038
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryDeptItem(String param, IUser user) {
        String status = JsonUtil.getFieldValue(param, "status");
        Map<String, Object> qryParam = Maps.newHashMap();
        qryParam.put("status", status);
        qryParam.put("pkDept", UserContext.getUser().getPkDept());
        return blMedicalExe2Mapper.qryDeptItem(qryParam);
    }

    /**
     * ??????????????????
     * 007004002039
     *
     * @param param
     * @param user
     */
    public void cancelApply(String param, IUser user) {
        List<Map<String, Object>> celApplyList = JsonUtil.readValue(param, List.class);
        if (celApplyList.size() > 0) {
            for (Map<String, Object> celMap : celApplyList) {
                if ("02".equals(MsgUtils.getPropValueStr(celMap, "codeOrdtype").substring(0, 2))) {
                    String pkOrdries = MsgUtils.getPropValueStr(celMap, "pkOrdris");
                    if (StringUtils.isNotBlank(pkOrdries)) {
                        String sql = "SELECT count(1) FROM CN_RIS_APPLY ris INNER JOIN EX_ORDER_OCC occ ON occ.PK_CNORD=ris.PK_CNORD WHERE occ.EU_STATUS='1' AND ris.PK_ORDRIS ='" + pkOrdries + "'";
                        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class);
                        if (count != null && count > 0) {
                            throw new BusException("??????????????????????????????????????????????????????");
                        } else {
                            blMedicalExe2Mapper.cancelApply(pkOrdries);
                        }
                    }
            	   /* if (pkOrdries != null && pkOrdries.size() > 0) {
            	      blMedicalExe2Mapper.cancelApply(pkOrdries);
            	    }*/
                } else if ("03".equals(MsgUtils.getPropValueStr(celMap, "codeOrdtype").substring(0, 2))) {
                    String pkCnords = MsgUtils.getPropValueStr(celMap, "pkCnord");
                    if (StringUtils.isNotBlank(pkCnords)) {
                        String sql = "SELECT count(1) FROM cn_lab_apply lab INNER JOIN EX_ORDER_OCC occ ON occ.PK_CNORD=lab.PK_CNORD WHERE occ.EU_STATUS='1' AND occ.PK_CNORD ='" + pkCnords + "'";
                        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class);
                        if (count != null && count > 0) {
                            throw new BusException("??????????????????????????????????????????????????????");
                        } else {
                            DataBaseHelper.update("update cn_lab_apply set eu_status ='0' where pk_cnord = ?", new Object[]{pkCnords});
                        }
                    }
                } else if ("12".equals(MsgUtils.getPropValueStr(celMap, "codeOrdtype").substring(0, 2))) {
                    String pkCnords = MsgUtils.getPropValueStr(celMap, "pkCnord");
                    if (StringUtils.isNotBlank(pkCnords)) {
                        String sql = "SELECT count(1) FROM cn_trans_apply apply INNER JOIN EX_ORDER_OCC occ ON occ.PK_CNORD=apply.PK_CNORD WHERE occ.EU_STATUS='1' AND occ.PK_CNORD ='" + pkCnords + "'";
                        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class);
                        if (count != null && count > 0) {
                            throw new BusException("??????????????????????????????????????????????????????");
                        } else {
                            DataBaseHelper.update("update cn_trans_apply set eu_status ='0' where pk_cnord = ?", new Object[]{pkCnords});
                        }
                    }
                }
            }
        }
    }

    /**
     * ?????????????????????
     * 007004002040
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryCgDt(String param, IUser user) {
        List<String> pkCnords = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        Set<String> pkCnordSet = new HashSet<String>(pkCnords);
        String sql = "SELECT * FROM (SELECT NAME_CG,SPEC,QUAN,PRICE,AMOUNT,PK_CNORD,DATE_CG,NAME_EMP_CG,flag_settle FROM BL_IP_DT UNION ALL SELECT NAME_CG,SPEC,QUAN,PRICE,AMOUNT,PK_CNORD,DATE_CG,NAME_EMP_CG,flag_settle FROM BL_OP_DT) cg WHERE cg.PK_CNORD in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ")";
        List<Map<String, Object>> blIpDt = DataBaseHelper.queryForList(sql, new Object[]{});
        return blIpDt;
    }

    /**
     * ???????????????????????????????????????
     * 007004002046
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPiInfo(String param, IUser user) {
        Map<String, Object> qryPam = JsonUtil.readValue(param, Map.class);
        qryPam.put("pkDeptExec", UserContext.getUser().getPkDept());
        List<Map<String, Object>> maps = blMedicalExe2Mapper.qryPiInfo(qryPam);
        return maps;
    }

    /**
     * ??????pkpv????????????????????????????????????????????????
     * 007004002047
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryApplyByPkPv(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        String euCharge = JsonUtil.getFieldValue(param, "euCharge");
        List<Map<String, Object>> applyInfos = blMedicalExe2Mapper.qryApplyByPkPv(pkPv, UserContext.getUser().getPkDept(), euCharge);
        return applyInfos;
    }

    /**
     * ??????????????????-????????????-?????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryOpBaMedicalApp(String param, IUser user) {
    	long date1 = System.currentTimeMillis();
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        }
        // ????????????
        int pageIndex = CommonUtils.getInteger(MapUtils.getString(paramMap, "pageIndex"));
        int pageSize = CommonUtils.getInteger(MapUtils.getString(paramMap, "pageSize"));
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> list = blMedicalExe2Mapper.queryOpBaMedicalApp(paramMap);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        if (CollectionUtils.isNotEmpty(list)){
            Set<String> pkCnords=list.stream().map(m->MapUtils.getString(m,"pkCnord")).collect(Collectors.toSet());
            StringBuilder sql=new StringBuilder();

            sql.append("select isnull(sum(dt.amount),0 )cnt, dt.pk_cnord ");
            sql.append  (" from bl_op_dt dt   where dt.PK_CNORD in ( " );
            sql.append (CommonUtils.convertSetToSqlInPart(pkCnords,"dt.PK_CNORD"));
               sql.append( " ) and dt.flag_settle = '1'    group by dt.pk_cnord");

            List<Map<String, Object>> blOplist=DataBaseHelper.queryForList(sql.toString());
            sql=new StringBuilder();
            sql.append("SELECT occ.pk_cnord,COUNT(*) num ,'1' EU_STATUS from ex_assist_occ occ where occ.pk_cnord  in ( ");
            sql.append(CommonUtils.convertSetToSqlInPart(pkCnords,"occ.pk_cnord") );
            sql.append("  )and  occ.EU_STATUS = '1' GROUP BY  occ.pk_cnord ");
            sql.append("union all");
            sql.append(" SELECT occ.pk_cnord,COUNT(1) numUn ,'0' EU_STATUS from ex_assist_occ occ where  occ.pk_cnord in( ");
            sql.append(CommonUtils.convertSetToSqlInPart(pkCnords,"occ.pk_cnord"));
            sql.append(" )and  occ.EU_STATUS = '0' and (occ.flag_refund ='0' or occ.flag_refund is null) GROUP BY occ.pk_cnord");
            List<Map<String, Object>> exOcclist=DataBaseHelper.queryForList(sql.toString());;
            list.forEach(m->{
                Integer cnt = 0;
                Integer num = 0;
                Integer numUn = 0;
                String euStatusName="????????????";
                List<Map<String, Object>> list2 =  blOplist.stream()
                        .filter(p -> MapUtils.getString(m, "pkCnord").equals(MapUtils.getString(p, "pkCnord")))
                        .collect(Collectors.toList());
                cnt=CollectionUtils.isNotEmpty(list2)?((list2.get(0).get("cnt") instanceof BigDecimal) ?
                        ((BigDecimal)list2.get(0).get("cnt")).intValue() : (Integer)list2.get(0).get("cnt")) : 0;
               list2 =   exOcclist.stream()
                        .filter(p -> MapUtils.getString(m, "pkCnord").equals(MapUtils.getString(p, "pkCnord"))
                             && "1".equals(MapUtils.getString(p, "euStatus"))
                        )
                        .collect(Collectors.toList());
                num=CollectionUtils.isNotEmpty(list2)?((list2.get(0).get("num") instanceof BigDecimal) ?
                        ((BigDecimal)list2.get(0).get("num")).intValue() : (Integer)list2.get(0).get("num")) : 0;
                list2 =  exOcclist.stream()
                        .filter(p -> MapUtils.getString(m, "pkCnord").equals(MapUtils.getString(p, "pkCnord"))
                                && "0".equals(MapUtils.getString(p, "euStatus"))
                        )
                        .collect(Collectors.toList());
                numUn=CollectionUtils.isNotEmpty(list2)?((list2.get(0).get("num") instanceof BigDecimal) ?
                        ((BigDecimal)list2.get(0).get("num")).intValue() : (Integer)list2.get(0).get("num")) : 0;
                if (num==0 &&numUn>0){
                    euStatusName="?????????";
                } else if (num>0 &&numUn>0){
                    euStatusName="????????????";
                }else if (num>0&&numUn==0){
                    euStatusName="?????????";

                }
               m.put("cnt",cnt);
                m.put("num",num);
                m.put("numUn",numUn);
                m.put("euStatusName",euStatusName);
            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("exAssistOccList", list);
        map.put("totalCount", page.getTotalCount());
        return map;
    }
}
